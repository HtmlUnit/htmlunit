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

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.DomNamespaceNode;

/**
 * An attribute of an element. Attributes are stored in {@link XmlElement}, but the XPath engine
 * expects attributes to be in a {@link com.gargoylesoftware.htmlunit.html.DomNode}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @deprecated As of 2.2, no more used, please use {@link DomAttr} instead.
 */
@Deprecated
public class XmlAttr extends DomNamespaceNode implements Attr {

    private static final long serialVersionUID = 4832218455328064213L;

    private String value_;

    /**
     * Instantiates a new attribute.
     *
     * @param page the page that the attribute belongs to
     * @param namespaceURI the namespace that defines the attribute name (may be <tt>null</tt>)
     * @param qualifiedName the name of the attribute
     * @param value the value of the attribute
     */
    public XmlAttr(final Page page, final String namespaceURI, final String qualifiedName, final String value) {
        super(namespaceURI, qualifiedName, page);
        value_ = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.ATTRIBUTE_NODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeValue() {
        return getValue();
    }

    /**
     * @return the qualified name of the attribute
     */
    public String getName() {
        return getQualifiedName();
    }

    /**
     * @return the value of the attribute
     */
    public String getValue() {
        return value_;
    }

    /**
     * {@inheritDoc}
     */
    public void setValue(final String value) {
        value_ = value;
    }

    /**
     * {@inheritDoc}
     */
    public Element getOwnerElement() {
        return (Element) getParentNode();
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("XmlAttr.getSchemaTypeInfo is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    public boolean getSpecified() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isId() {
        return "id".equals(getNodeName());
    }
}
