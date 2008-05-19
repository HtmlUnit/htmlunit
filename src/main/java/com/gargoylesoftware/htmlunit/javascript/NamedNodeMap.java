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
package com.gargoylesoftware.htmlunit.javascript;

import org.apache.commons.collections.map.ListOrderedMap;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.xml.XmlElement;

/**
 * A collection of nodes that can be accessed by name.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core.html#ID-1780488922">DOM Level 2 Core Spec</a>
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms763824.aspx">IXMLDOMNamedNodeMap</a>
 */
public class NamedNodeMap extends SimpleScriptable implements ScriptableWithFallbackGetter, org.w3c.dom.NamedNodeMap {

    private static final long serialVersionUID = -1910087049570242560L;

    private final ListOrderedMap nodes_ = new ListOrderedMap();
    /**
     * Empty instance.
     */
    public static final org.w3c.dom.NamedNodeMap EMPTY_NODE_MAP = new NamedNodeMap();

    /**
     * Rhino requires default constructors.
     */
    public NamedNodeMap() {
        // Empty.
    }

    /**
     * Creates a new named node map for the specified element.
     *
     * @param element the owning element
     */
    public NamedNodeMap(final HtmlElement element) {
        for (final DomAttr attr : element.getAttributesCollection()) {
            nodes_.put(attr.getName(), attr);
        }
        setParentScope(element.getScriptObject());
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Creates a new named node map for the specified element.
     *
     * @param element the owning element
     */
    public NamedNodeMap(final XmlElement element) {
        for (final DomAttr attr : element.getAttributesMap().values()) {
            nodes_.put(attr.getName(), attr);
        }
        setParentScope(element.getScriptObject());
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Returns the element at the specified index, or <tt>NOT_FOUND</tt> if the index is invalid.
     *
     * {@inheritDoc}
     */
    @Override
    public final Object get(final int index, final Scriptable start) {
        final NamedNodeMap map = (NamedNodeMap) start;
        if (index >= 0 && index < map.nodes_.size()) {
            final DomNode attr = (DomNode) map.nodes_.getValue(index);
            return attr.getScriptObject();
        }
        return NOT_FOUND;
    }

    /**
     * Returns the element with the specified name, or <tt>NOT_FOUND</tt> if the name is invalid.
     *
     * {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        final DomNode attr = (DomNode) nodes_.get(name);
        if (attr != null) {
            return attr.getScriptObject();
        }
        return NOT_FOUND;
    }
    
    /**
     * Gets the specified attribute.
     * @param name attribute name
     * @return the attribute node, <code>null</code> if the attribute is not defined
     */
    public Object jsxFunction_getNamedItem(final String name) {
        final DomNode attr = (DomNode) nodes_.get(name);
        if (attr != null) {
            return attr.getScriptObject();
        }
        return null;
    }

    /**
     * Returns the number of attributes in this named node map.
     * @return the number of attributes in this named node map
     */
    public int jsxGet_length() {
        return nodes_.size();
    }

    /**
     * {@inheritDoc}
     */
    public int getLength() {
        return jsxGet_length();
    }

    /**
     * {@inheritDoc}
     */
    public Attr getNamedItem(final String name) {
        return (Attr) nodes_.get(name);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr getNamedItemNS(final String namespaceURI, final String localName) {
        throw new UnsupportedOperationException("NamedNodeMap.getNamedItemNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    public Attr item(final int index) {
        return (Attr) nodes_.getValue(index);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Node removeNamedItem(final String name) throws DOMException {
        throw new UnsupportedOperationException("NamedNodeMap.removeNamedItem is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Node removeNamedItemNS(final String namespaceURI, final String localName) throws DOMException {
        throw new UnsupportedOperationException("NamedNodeMap.removeNamedItemNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Node setNamedItem(final Node arg) throws DOMException {
        throw new UnsupportedOperationException("NamedNodeMap.setNamedItem is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Node setNamedItemNS(final Node arg) throws DOMException {
        throw new UnsupportedOperationException("NamedNodeMap.setNamedItemNS is not yet implemented.");
    }
}
