/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript.host.dom;

import java.io.IOException;

import org.htmlunit.StringWebResponse;
import org.htmlunit.WebClient;
import org.htmlunit.WebResponse;
import org.htmlunit.WebWindow;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.parser.HTMLParser;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.html.HTMLDocument;
import org.htmlunit.javascript.host.xml.XMLDocument;
import org.htmlunit.util.MimeType;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code DOMParser}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 *
 * @see <a href="http://www.w3.org/TR/DOM-Parsing/">W3C Spec</a>
 * @see <a href="http://domparsing.spec.whatwg.org/">WhatWG Spec</a>
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/DOMParser">Mozilla Developer Network</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ff975060.aspx">MSDN</a>
 * @see <a href="http://www.xulplanet.com/references/objref/DOMParser.html">XUL Planet</a>
 */
@JsxClass
public class DOMParser extends HtmlUnitScriptable {

    /**
     * The constructor.
     */
    @JsxConstructor
    public DOMParser() {
    }

    /**
     * Parses the given Unicode string into a DOM document.
     * @param str the Unicode string to be parsed
     * @param type the MIME type of the string -
     *        <code>text/html</code>, <code>text/xml</code>, <code>application/xml</code>,
     *        <code>application/xhtml+xml</code>, <code>image/svg+xml</code>. Must not be {@code null}.
     * @return the generated document
     */
    @JsxFunction
    public Document parseFromString(final String str, final Object type) {
        try {
            final Document document = parseFromString(this, str, type);
            if (document == null) {
                throw Context.reportRuntimeError("Invalid 'type' parameter: " + type);
            }
            return document;
        }
        catch (final IOException e) {
            throw Context.reportRuntimeError("Parsing failed" + e.getMessage());
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Parses the given Unicode string into a DOM document.
     * @param scriptable the ScriptableObject this belongs to
     * @param str the Unicode string to be parsed
     * @param type the MIME type of the string -
     *        <code>text/html</code>, <code>text/xml</code>, <code>application/xml</code>,
     *        <code>application/xhtml+xml</code>, <code>image/svg+xml</code>. Must not be {@code null}.
     * @return the generated document
     * @throws IOException in case of error
     */
    public static Document parseFromString(final HtmlUnitScriptable scriptable, final String str, final Object type)
                throws IOException {
        if (type == null || Undefined.isUndefined(type)) {
            throw Context.reportRuntimeError("Missing 'type' parameter");
        }

        if (MimeType.TEXT_XML.equals(type)
                || MimeType.APPLICATION_XML.equals(type)
                || MimeType.APPLICATION_XHTML.equals(type)
                || "image/svg+xml".equals(type)) {
            final XMLDocument document = new XMLDocument();
            document.setParentScope(scriptable.getParentScope());
            document.setPrototype(scriptable.getPrototype(XMLDocument.class));
            document.loadXML(str);
            return document;
        }

        if (MimeType.TEXT_HTML.equals(type)) {
            final WebWindow webWindow = scriptable.getWindow().getWebWindow();
            final WebClient webClient = webWindow.getWebClient();
            final WebResponse webResponse = new StringWebResponse(str, webWindow.getEnclosedPage().getUrl());

            // a similar impl is in
            // org.htmlunit.javascript.host.dom.DOMImplementation.createHTMLDocument(Object)
            final HtmlPage page = new HtmlPage(webResponse, webWindow);
            page.setEnclosingWindow(null);
            final Window window = webWindow.getScriptableObject();

            // document knows the window but is not the windows document
            final HTMLDocument document = new HTMLDocument();
            document.setParentScope(window);
            document.setPrototype(window.getPrototype(document.getClass()));
            // document.setWindow(window);
            document.setDomNode(page);

            final HTMLParser htmlParser = webClient.getPageCreator().getHtmlParser();
            htmlParser.parse(webResponse, page, false, true);
            return page.getScriptableObject();
        }

        return null;
    }
}
