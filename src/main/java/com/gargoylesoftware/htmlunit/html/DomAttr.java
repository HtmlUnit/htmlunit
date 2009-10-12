/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import org.w3c.dom.Attr;
import org.w3c.dom.TypeInfo;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * An attribute of an element. Attributes are stored in {@link HtmlElement},
 * but the XPath engine expects attributes to be in a {@link DomNode}.
 *
 * @version $Revision$
 * @author Denis N. Antonioli
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
public class DomAttr extends DomNamespaceNode implements Attr {

    private static final long serialVersionUID = 4832218455328064213L;

    private String value_;
    private boolean specified_;

    /**
     * Instantiate a new attribute.
     *
     * @param page the page that the attribute belongs to
     * @param namespaceURI the namespace that defines the attribute name (may be <tt>null</tt>)
     * @param qualifiedName the name of the attribute
     * @param value the value of the attribute
     */
    public DomAttr(final SgmlPage page, final String namespaceURI, final String qualifiedName, final String value) {
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
     * {@inheritDoc}
     */
    public String getName() {
        return getQualifiedName();
    }

    /**
     * {@inheritDoc}
     */
    public String getValue() {
        return value_;
    }

    /**
     * {@inheritDoc}
     */
    public void setValue(final String value) {
        value_ = value;
        specified_ = true;
    }

    /**
     * {@inheritDoc}
     */
    public DomElement getOwnerElement() {
        return (DomElement) getParentNode();
    }

    /**
     * {@inheritDoc}
     */
    public boolean getSpecified() {
        return specified_;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("HtmlAttr.getSchemaTypeInfo is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isId() {
        return "id".equals(getNodeName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[name=" + getNodeName() + " value=" + getNodeValue() + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCanonicalXPath() {
        return getParentNode().getCanonicalXPath() + "/@" + getName();
    }
}
