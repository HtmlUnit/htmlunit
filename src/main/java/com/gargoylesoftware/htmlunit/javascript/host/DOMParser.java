/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;

/**
 * A JavaScript object for DOMParser.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 *
 * @see <a href="http://www.xulplanet.com/references/objref/DOMParser.html">XUL Planet</a>
 */
public class DOMParser extends SimpleScriptable {

    private static final long serialVersionUID = 3143102490697686971L;

    /**
     * JavaScript constructor.
     */
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
    public XMLDocument jsxFunction_parseFromString(final String str, final String contentType) {
        final XMLDocument document = new XMLDocument();
        document.setParentScope(getParentScope());
        document.setPrototype(getPrototype(XMLDocument.class));
        document.jsxFunction_loadXML(str);
        return document;
    }
}
