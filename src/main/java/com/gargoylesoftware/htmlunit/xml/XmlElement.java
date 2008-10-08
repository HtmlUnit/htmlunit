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

import java.util.Map;

import org.apache.commons.lang.ClassUtils;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;

/**
 * An XML element.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @deprecated As of 2.4, please use {@link DomElement} instead.
 */
@Deprecated
public class XmlElement extends DomElement {

    private static final long serialVersionUID = -8119109851558707854L;

    /**
     * Create an instance of a DOM node that can have a namespace.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the attributes of this element
     */
    protected XmlElement(final String namespaceURI, final String qualifiedName, final Page page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ClassUtils.getShortClassName(getClass()) + "[<" + getTagName() + " ...>]";
    }

}
