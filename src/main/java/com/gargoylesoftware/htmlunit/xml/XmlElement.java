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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.ClassUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;

/**
 * An XML element.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class XmlElement extends DomElement implements Element {

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
     * Sets the value of the attribute specified by name.
     *
     * @param attributeName the name of the attribute
     * @param attributeValue the value of the attribute
     */
    public final void setAttribute(final String attributeName, final String attributeValue) {
        setAttributeValue(null, attributeName, attributeValue);
    }

    /**
     * Sets the value of the attribute specified by namespace and qualified name.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name (prefix:local) of the attribute
     * @param attributeValue the value of the attribute
     */
    public final void setAttributeNS(final String namespaceURI, final String qualifiedName,
            final String attributeValue) {
        setAttributeValue(namespaceURI, qualifiedName, attributeValue);
    }

    /**
     * Sets the value of the specified attribute.
     *
     * @param attributeName the name of the attribute
     * @param attributeValue the value of the attribute
     */
    public final void setAttributeValue(final String attributeName, final String attributeValue) {
        setAttributeValue(null, attributeName, attributeValue);
    }

    /**
     * Sets the value of the specified attribute.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the attribute
     * @param attributeValue the value of the attribute
     */
    public final void setAttributeValue(final String namespaceURI, final String qualifiedName,
            final String attributeValue) {
        final String value = attributeValue;

        if (attributes() == Collections.EMPTY_MAP) {
            setAttributes(createAttributeMap(1));
        }
        final DomAttr newAttr = addAttributeToMap((XmlPage) getPage(), attributes(), namespaceURI,
            qualifiedName, value);
        if (namespaceURI != null) {
            namespaces().put(namespaceURI, newAttr.getPrefix());
        }
        attributes().put(newAttr.getName(), newAttr);
    }

    /**
     * Create an attribute map as needed by HtmlElement. This is just used by the element factories.
     * @param attributeCount the initial number of attributes to be added to the map
     * @return the attribute map
     */
    @SuppressWarnings("unchecked")
    static Map<String, DomAttr> createAttributeMap(final int attributeCount) {
        return ListOrderedMap.decorate(new HashMap<String, DomAttr>(attributeCount)); // preserve insertion order
    }

    /**
     * Adds an attribute to the specified attribute map. This is just used by the element factories.
     * @param page the XML page containing the attribute
     * @param attributeMap the attribute map where the attribute will be added
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the attribute
     * @param value the value of the attribute
     * @return the new attribute that was added to the specified attribute map
     */
    static DomAttr addAttributeToMap(final XmlPage page, final Map<String, DomAttr> attributeMap,
        final String namespaceURI, final String qualifiedName, final String value) {
        final DomAttr newAttr = new DomAttr(page, namespaceURI, qualifiedName, value);
        attributeMap.put(qualifiedName, newAttr);
        return newAttr;
    }

    /**
     * {@inheritDoc}
     */
    public String getAttribute(final String name) {
        final DomAttr attr = attributes().get(name);
        if (attr != null) {
            return attr.getValue();
        }
        return "";
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String getAttributeNS(final String namespaceURI, final String localName) {
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
    public Attr getAttributeNodeNS(final String namespaceURI, final String localName) {
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
    public NodeList getElementsByTagNameNS(final String namespace, final String name) {
        throw new UnsupportedOperationException("XmlElement.getElementsByTagNameNS is not yet implemented.");
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
    public Attr setAttributeNode(final Attr newAttr) {
        throw new UnsupportedOperationException("XmlElement.setAttributeNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr setAttributeNodeNS(final Attr newAttr) {
        throw new UnsupportedOperationException("XmlElement.setAttributeNodeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setIdAttribute(final String name, final boolean isId) {
        throw new UnsupportedOperationException("XmlElement.setIdAttribute is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setIdAttributeNS(final String namespaceURI, final String localName, final boolean isId) {
        throw new UnsupportedOperationException("XmlElement.setIdAttributeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setIdAttributeNode(final Attr idAttr, final boolean isId) {
        throw new UnsupportedOperationException("XmlElement.setIdAttributeNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ClassUtils.getShortClassName(getClass()) + "[<" + getTagName() + " ...>]";
    }

}
