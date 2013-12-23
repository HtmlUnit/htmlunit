/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;

/**
 * A JavaScript object for DOMParser.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 *
 * @see <a href="http://www.xulplanet.com/references/objref/DOMParser.html">XUL Planet</a>
 */
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
public class DOMParser extends SimpleScriptable {

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // Empty.
    }

    /**
     * The string passed in is parsed into a DOM document.
     * @param str the UTF16 string to be parsed
     * @param contentType the content type of the string -
     *        either <tt>text/xml</tt>, <tt>application/xml</tt>, or <tt>application/xhtml+xml</tt>. Must not be NULL.
     * @return the generated document
     */
    @JsxFunction
    public XMLDocument parseFromString(final String str, final Object contentType) {
        if (Undefined.instance == contentType) {
            throw Context.reportRuntimeError("Missing 'contentType' parameter");
        }
        final XMLDocument document = new XMLDocument();
        document.setParentScope(getParentScope());
        document.setPrototype(getPrototype(XMLDocument.class));
        document.loadXML(str);
        return document;
    }
}
