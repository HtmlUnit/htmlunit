/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.xml;

import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.DomNamespaceNode;
import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * An attribute of an element. Attributes are stored in {@link XmlElement},
 * but the XPath engine expects attributes to be in a {@link DomNode}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XmlAttr extends DomNamespaceNode implements Attr {

    private static final long serialVersionUID = 4832218455328064213L;

    private String value_;

    /**
     * Instantiates a new attribute.
     *
     * @param xmlElement the parent element
     * @param mapEntry the wrapped map entry
     * @deprecated Use constructor with explicit names.
     */
    @Deprecated
    public XmlAttr(final XmlElement xmlElement, final Map.Entry<String, String> mapEntry) {
        super(null, mapEntry.getKey(), xmlElement.getPage());
        value_ = mapEntry.getValue();
        setParentNode(xmlElement);
    }

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
        return (String) getValue();
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
    public void setValue(final String value) throws DOMException {
        value_ = value;
    }

    /**
     * Sets the parent node.
     * @param parent the parent node
     */
    @Override
    protected void setParentNode(final DomNode parent) {
        super.setParentNode(parent);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
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
