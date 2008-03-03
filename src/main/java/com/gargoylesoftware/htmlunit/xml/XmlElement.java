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

import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.DomNamespaceNode;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;

/**
 * An XML element.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class XmlElement extends DomNamespaceNode implements Element {

    private static final long serialVersionUID = -8119109851558707854L;

    /** Constant meaning that the specified attribute was not defined. */
    public static final String ATTRIBUTE_NOT_DEFINED = new String("");

    /** The map holding the namespaces, keyed by URI. */
    private Map<String, String> namespaces_ = new HashMap<String, String>();

    /** The map holding the attributes, keyed by name. */
    private Map<String, XmlAttr> attributes_;

    /**
     * Create an instance of a DOM node that can have a namespace.
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate.
     * @param page The page that contains this element.
     * @param attributes The attributes of this element.
     */
    protected XmlElement(final String namespaceURI, final String qualifiedName, final Page page,
            final Map<String, XmlAttr> attributes) {
        super(namespaceURI, qualifiedName, page);
        attributes_ = attributes;
        for (final XmlAttr attr : attributes.values()) {
            attr.setParentNode(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.ELEMENT_NODE;
    }

    /**
     * @return The same value as returned by {@link #getTagName()},
     */
    @Override
    public String getNodeName() {
        return getTagName();
    }

    /**
     * Return the tag name of this element.
     * @return the tag name of this element.
     */
    public String getTagName() {
        if (getNamespaceURI() == null) {
            return getLocalName();
        }
        else {
            return getQualifiedName();
        }
    }

    /**
     * Return the value of the specified attribute or an empty string.  If the
     * result is an empty string then it will be {@link #ATTRIBUTE_NOT_DEFINED}
     *
     * @param attributeName the name of the attribute
     * @return The value of the attribute or {@link #ATTRIBUTE_NOT_DEFINED}
     */
    public final String getAttributeValue(final String attributeName) {
        final XmlAttr attr = (XmlAttr) attributes_.get(attributeName);

        if (attr != null) {
            return attr.getNodeValue();
        }
        else {
            return ATTRIBUTE_NOT_DEFINED;
        }
    }

    /**
     * Returns the map holding the attributes, keyed by name.
     * @return the attributes map.
     */
    public Map<String, XmlAttr> getAttributesMap() {
        return attributes_;
    }
    /**
     * Set the value of the attribute specified by name.
     *
     * @param attributeName the name of the attribute
     * @param attributeValue The value of the attribute
     */
    public final void setAttribute(final String attributeName, final String attributeValue) {
        setAttributeValue(null, attributeName, attributeValue);
    }

    /**
     * Set the value of the attribute specified by namespace and qualified name.
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name (prefix:local) of the attribute.
     * @param attributeValue The value of the attribute
     */
    public final void setAttributeNS(final String namespaceURI, final String qualifiedName,
            final String attributeValue) {
        setAttributeValue(namespaceURI, qualifiedName, attributeValue);
    }

    /**
     * Set the value of the specified attribute.
     *
     * @param attributeName the name of the attribute
     * @param attributeValue The value of the attribute
     */
    public final void setAttributeValue(final String attributeName, final String attributeValue) {
        setAttributeValue(null, attributeName, attributeValue);
    }

    /**
     * Set the value of the specified attribute.
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the attribute
     * @param attributeValue The value of the attribute
     */
    public final void setAttributeValue(final String namespaceURI, final String qualifiedName,
            final String attributeValue) {
        final String value = attributeValue;

        if (attributes_ == Collections.EMPTY_MAP) {
            attributes_ = createAttributeMap(1);
        }
        final XmlAttr newAttr = addAttributeToMap((XmlPage) getPage(), attributes_, namespaceURI,
            qualifiedName, value);
        if (namespaceURI != null) {
            namespaces_.put(namespaceURI, newAttr.getPrefix());
        }
        attributes_.put(newAttr.getName(), newAttr);
    }

    /**
     * Removes an attribute specified by name from this element.
     * @param attributeName the attribute attributeName
     */
    public final void removeAttribute(final String attributeName) {
        attributes_.remove(attributeName.toLowerCase());
    }

    /**
     * Removes an attribute specified by namespace and local name from this element.
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param localName The name within the namespace.
     */
    public final void removeAttributeNS(final String namespaceURI, final String localName) {
        removeAttribute(getQualifiedName(namespaceURI, localName));
    }

    /**
     * Return the qualified name (prefix:local) for the namespace and local name.
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param localName The name within the namespace.
     * @return The qualified name or just local name if the namespace is not fully defined.
     */
    private String getQualifiedName(final String namespaceURI, final String localName) {
        final String qualifiedName;
        if (namespaceURI != null) {
            final String prefix = (String) namespaces_.get(namespaceURI);
            if (prefix != null) {
                qualifiedName = prefix + ':' + localName;
            }
            else {
                qualifiedName = localName;
            }
        }
        else {
            qualifiedName = localName;
        }
        return qualifiedName;
    }

    /**
     * Create an attribute map as needed by HtmlElement.  This is just used by the element factories.
     * @param attributeCount the initial number of attributes to be added to the map.
     * @return the attribute map.
     */
    @SuppressWarnings("unchecked")
    static Map<String, XmlAttr> createAttributeMap(final int attributeCount) {
        return (Map<String, XmlAttr>) ListOrderedMap.decorate(new HashMap(attributeCount)); // preserve insertion order
    }

    /**
     * Add an attribute to the attribute map.  This is just used by the element factories.
     * @param attributeMap the attribute map where the attribute will be added.
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the attribute
     * @param value The value of the attribute
     */
    static XmlAttr addAttributeToMap(final XmlPage page, final Map<String, XmlAttr> attributeMap,
            final String namespaceURI, final String qualifiedName, final String value) {
        final XmlAttr newAttr = new XmlAttr(page, namespaceURI, qualifiedName, value);
        attributeMap.put(qualifiedName, newAttr);
        return newAttr;
    }

    /**
     * recursively write the XML data for the node tree starting at <code>node</code>
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    @Override
    protected void printXml(final String indent, final PrintWriter printWriter) {
        final boolean hasChildren = (getFirstDomChild() != null);
        printWriter.print(indent + "<");
        printOpeningTagContentAsXml(printWriter);

        if (!hasChildren) {
            printWriter.println("/>");
        }
        else {
            printWriter.println(">");
            printChildrenAsXml(indent, printWriter);
            printWriter.println(indent + "</" + getTagName() + ">");
        }
    }

    /**
     * Prints the content between "&lt;" and "&gt;" (or "/&gt;") in the output of the tag name
     * and its attributes in xml format.
     * @param printWriter the writer to print in
     */
    protected void printOpeningTagContentAsXml(final PrintWriter printWriter) {
        printWriter.print(getTagName());
        for (final String name : attributes_.keySet()) {
            printWriter.print(" ");
            printWriter.print(name);
            printWriter.print("=\"");
            printWriter.print(StringEscapeUtils.escapeXml(((XmlAttr) attributes_.get(name)).getNodeValue()));
            printWriter.print("\"");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public org.w3c.dom.NamedNodeMap getAttributes() {
        return new NamedNodeMap(this);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String getAttribute(final String name) {
        throw new UnsupportedOperationException("XmlElement.getAttribute is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String getAttributeNS(final String namespaceURI, final String localName) throws DOMException {
        throw new UnsupportedOperationException("XmlElement.getAttributeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr getAttributeNode(final String name) {
        throw new UnsupportedOperationException("XmlElement.getAttributeNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr getAttributeNodeNS(final String namespaceURI, final String localName) throws DOMException {
        throw new UnsupportedOperationException("XmlElement.getAttributeNodeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public NodeList getElementsByTagName(final String name) {
        throw new UnsupportedOperationException("XmlElement.getElementsByTagName is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("XmlElement.getSchemaTypeInfo is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public boolean hasAttribute(final String name) {
        throw new UnsupportedOperationException("XmlElement.hasAttribute is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public boolean hasAttributeNS(final String namespaceURI, final String localName) throws DOMException {
        throw new UnsupportedOperationException("XmlElement.hasAttributeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr removeAttributeNode(final Attr oldAttr) throws DOMException {
        throw new UnsupportedOperationException("XmlElement.removeAttributeNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr setAttributeNode(final Attr newAttr) throws DOMException {
        throw new UnsupportedOperationException("XmlElement.setAttributeNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr setAttributeNodeNS(final Attr newAttr) throws DOMException {
        throw new UnsupportedOperationException("XmlElement.setAttributeNodeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setIdAttribute(final String name, final boolean isId) throws DOMException {
        throw new UnsupportedOperationException("XmlElement.setIdAttribute is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setIdAttributeNS(final String namespaceURI, final String localName, final boolean isId)
        throws DOMException {
        throw new UnsupportedOperationException("XmlElement.setIdAttributeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setIdAttributeNode(final Attr idAttr, final boolean isId) throws DOMException {
        throw new UnsupportedOperationException("XmlElement.setIdAttributeNode is not yet implemented.");
    }

    /**
     * Just for debug purposes.
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ClassUtils.getShortClassName(getClass())
            + "[<" + getTagName() + " ...>]";
    }
}
