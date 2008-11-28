/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * A representation of an HTML element "html".
 *
 * @version $Revision$
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
public final class HtmlHtml extends HtmlElement {

    private static final long serialVersionUID = 8928934628059990747L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "html";

    /**
     * Creates a new instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlHtml(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute "lang". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute "lang" or an empty string if that attribute isn't defined
     */
    public String getLangAttribute() {
        return getAttribute("lang");
    }

    /**
     * Returns the value of the attribute "xml:lang". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute "xml:lang" or an empty string if that attribute isn't defined
     */
    public String getXmlLangAttribute() {
        return getAttribute("xml:lang");
    }

    /**
     * Returns the value of the attribute "dir". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute "dir" or an empty string if that attribute isn't defined
     */
    public String getTextDirectionAttribute() {
        return getAttribute("dir");
    }
}
