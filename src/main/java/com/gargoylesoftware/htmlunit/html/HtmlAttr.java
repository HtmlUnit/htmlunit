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
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;


/**
 * An attribute of an element. Attributes are stored in {@link HtmlElement},
 * but the xpath engine expects attributes to be in a {@link DomNode}.
 *
 * @version $Revision$
 * @author Denis N. Antonioli
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
public class HtmlAttr extends DomNamespaceNode implements Attr {

    private static final long serialVersionUID = 4832218455328064213L;

    private String value_;

    /**
     * Instantiate a new attribute.
     *
     * @param page The page that the attribute belongs to.
     * @param namespaceURI The namespace that defines the attribute name.  May be null.
     * @param qualifiedName The name of the attribute.
     * @param value The value of the attribute.
     */
    public HtmlAttr(final HtmlPage page, final String namespaceURI, final String qualifiedName, final String value) {
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
        return getHtmlValue();
    }

    /**
     * @return The qualified name of the attribute.
     */
    public String getName() {
        return getQualifiedName();
    }

    /**
     * @return The value of the attribute.
     */
    public String getValue() {
        return value_;
    }

    /**
     * @return The value of wrapped map entry.
     */
    public String getHtmlValue() {
        return getValue();
    }

    /**
     * Set the value of the attribute.
     * @param value new value to be stored in this entry.
     */
    public void setValue(final String value) {
        value_ = value;
    }

    /**
     * Set the value of the attribute.
     * @param value new value to be stored in this entry.
     * @return old value corresponding to the entry.
     */
    public String setHtmlValue(final String value) {
        final String oldValue = value_;
        value_ = value;
        return oldValue;
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
    public boolean getSpecified() {
        return true;
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
}
