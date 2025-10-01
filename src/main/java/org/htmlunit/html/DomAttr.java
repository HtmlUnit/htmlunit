/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.html;

import org.htmlunit.SgmlPage;
import org.w3c.dom.Attr;
import org.w3c.dom.TypeInfo;

/**
 * An attribute of an element. Attributes are stored in {@link HtmlElement},
 * but the XPath engine expects attributes to be in a {@link DomNode}.
 *
 * @author Denis N. Antonioli
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
public class DomAttr extends DomNamespaceNode implements Attr {

    private String value_;
    private boolean specified_;

    /**
     * Instantiate a new attribute.
     *
     * @param page the page that the attribute belongs to
     * @param namespaceURI the namespace that defines the attribute name (maybe {@code null})
     * @param qualifiedName the name of the attribute
     * @param value the value of the attribute
     * @param specified {@code true} if this attribute was explicitly given a value in the source document,
     *        or if the application changed the value of the attribute
     */
    public DomAttr(final SgmlPage page, final String namespaceURI, final String qualifiedName, final String value,
        final boolean specified) {
        super(namespaceURI, qualifiedName, page);

        if (value != null && value.isEmpty()) {
            value_ = DomElement.ATTRIBUTE_VALUE_EMPTY;
        }
        else {
            value_ = value;
        }

        specified_ = specified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getNodeType() {
        return ATTRIBUTE_NODE;
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
    @Override
    public String getName() {
        return getQualifiedName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNodeValue(final String value) {
        setValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(final String value) {
        if (value != null
                && value.isEmpty()) {
            value_ = DomElement.ATTRIBUTE_VALUE_EMPTY;
        }
        else {
            value_ = value;
        }
        specified_ = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement getOwnerElement() {
        return (DomElement) getParentNode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getSpecified() {
        return specified_;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("DomAttr.getSchemaTypeInfo is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isId() {
        return DomElement.ID_ATTRIBUTE.equals(getNodeName());
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextContent() {
        return getNodeValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTextContent(final String textContent) {
        final boolean mappedElement =
                getOwnerDocument() instanceof HtmlPage
                && (DomElement.NAME_ATTRIBUTE.equals(getName()) || DomElement.ID_ATTRIBUTE.equals(getName()));
        if (mappedElement) {
            ((HtmlPage) getPage()).removeMappedElement(getOwnerElement(), false, false);
        }
        setValue(textContent);
        if (mappedElement) {
            ((HtmlPage) getPage()).addMappedElement(getOwnerElement(), false);
        }
    }
}
