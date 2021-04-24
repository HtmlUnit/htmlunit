/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParser;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import com.gargoylesoftware.htmlunit.util.MimeType;

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
public class DOMParser extends SimpleScriptable {

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
        if (type == null || Undefined.isUndefined(type)) {
            throw Context.reportRuntimeError("Missing 'type' parameter");
        }
        if (MimeType.TEXT_XML.equals(type)
                || "application/xml".equals(type)
                || MimeType.APPLICATION_XHTML.equals(type)
                || "image/svg+xml".equals(type)) {
            final XMLDocument document = new XMLDocument();
            document.setParentScope(getParentScope());
            document.setPrototype(getPrototype(XMLDocument.class));
            document.loadXML(str);
            return document;
        }

        if (MimeType.TEXT_HTML.equals(type)) {
            final WebWindow webWindow = getWindow().getWebWindow();
            final WebClient webClient = webWindow.getWebClient();
            final WebResponse webResponse = new StringWebResponse(str, webWindow.getEnclosedPage().getUrl());

            // a similar impl is in
            // com.gargoylesoftware.htmlunit.javascript.host.dom.DOMImplementation.createHTMLDocument(Object)
            try {
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
                htmlParser.parse(webResponse, page, false);
                return (HTMLDocument) page.getScriptableObject();
            }
            catch (final IOException e) {
                throw Context.reportRuntimeError("Parsing failed" + e.getMessage());
            }
        }

        throw Context.reportRuntimeError("Invalid 'type' parameter: " + type);
    }
}
