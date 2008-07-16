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
package com.gargoylesoftware.htmlunit.xml;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.DomAttr;

/**
 * An attribute of an element. Attributes are stored in {@link XmlElement},
 * but the XPath engine expects attributes to be in a {@link DomNode}.
 *
 * @version $Revision$
 * @author Sudhan Moghe
 */
public class XmlDomAttr extends DomAttr {

    private static final long serialVersionUID = 484008644303170949L;

    /**
     * Instantiate a new attribute.
     *
     * @param page the page that the attribute belongs to
     * @param namespaceURI the namespace that defines the attribute name (may be <tt>null</tt>)
     * @param qualifiedName the name of the attribute
     * @param value the value of the attribute
     */
    public XmlDomAttr(final Page page, final String namespaceURI, final String qualifiedName, final String value) {
        super(page, namespaceURI, qualifiedName, value);
    }
}
