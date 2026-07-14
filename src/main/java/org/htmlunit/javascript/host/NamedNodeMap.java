/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSymbol;
import org.htmlunit.javascript.host.dom.Attr;
import org.htmlunit.javascript.host.dom.Node;

/**
 * A collection of nodes that can be accessed by name. String comparisons in this class are case-insensitive when
 * used with an {@link org.htmlunit.html.HtmlElement},
 * but case-sensitive when used with a {@link org.htmlunit.html.DomElement}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/NamedNodeMap">MDN Documentation</a>
 */
@JsxClass
public class NamedNodeMap extends HtmlUnitScriptable {

    private final org.w3c.dom.NamedNodeMap attributes_;

    /**
     * Default constructor for prototype instantiation.
     */
    public NamedNodeMap() {
        super();
        attributes_ = null;
    }

    /**
     * Creates an instance of this object.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Creates a new named node map for the specified element.
     *
     * @param element the owning element
     */
    public NamedNodeMap(final DomElement element) {
        super();
        setParentScope(element.getScriptableObject().getParentScope());
        setPrototype(getPrototype(getClass()));

        attributes_ = element.getAttributes();
        setDomNode(element, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object get(final int index, final Scriptable start) {
        final NamedNodeMap startMap = (NamedNodeMap) start;
        final Object response = startMap.item(index);
        if (response != null) {
            return response;
        }
        return NOT_FOUND;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        Object response = super.get(name, start);
        if (response != NOT_FOUND) {
            return response;
        }

        response = getNamedItem(name);
        if (response != null) {
            return response;
        }

        return NOT_FOUND;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Gets the specified attribute without handling the synthetic class attribute.
     *
     * @param name the attribute name
     * @return the attribute node, or {@code null} if not found
     * @see #getNamedItem(String)
     */
    public HtmlUnitScriptable getNamedItemWithoutSytheticClassAttr(final String name) {
        if (attributes_ != null) {
            final DomNode attr = (DomNode) attributes_.getNamedItem(name);
            if (attr != null) {
                return attr.getScriptableObject();
            }
        }

        return null;
    }

    /**
     * Returns the attribute node with the specified name.
     *
     * @param name the attribute name
     * @return the attribute node, or {@code null} if not defined
     */
    @JsxFunction
    public HtmlUnitScriptable getNamedItem(final String name) {
        return getNamedItemWithoutSytheticClassAttr(name);
    }

    /**
     * Returns the attribute node with the given namespace URI and local name.
     *
     * @param namespaceURI the namespace URI of the node to retrieve
     * @param localName the local name of the node to retrieve
     * @return the attribute node, or {@code null} if not found
     */
    @JsxFunction
    public Node getNamedItemNS(final String namespaceURI, final String localName) {
        if (attributes_ != null) {
            final DomNode attr = (DomNode) attributes_.getNamedItemNS(namespaceURI, localName);
            if (attr != null) {
                return attr.getScriptableObject();
            }
        }

        return null;
    }

    /**
     * Sets the specified attribute node.
     *
     * @param node the attribute node to set
     */
    @JsxFunction
    public void setNamedItem(final Node node) {
        attributes_.setNamedItem(node.getDomNodeOrDie());
    }

    /**
     * Sets the specified attribute node using its namespace URI and local name.
     *
     * @param node the attribute node to set
     */
    @JsxFunction
    public void setNamedItemNS(final Node node) {
        attributes_.setNamedItemNS(node.getDomNodeOrDie());
    }

    /**
     * Removes the attribute with the specified name.
     *
     * @param name the name of the attribute to remove
     */
    @JsxFunction
    public void removeNamedItem(final String name) {
        attributes_.removeNamedItem(name);
    }

    /**
     * Removes the attribute with the given namespace URI and local name.
     *
     * @param namespaceURI the namespace URI of the attribute to remove
     * @param localName the local name of the attribute to remove
     * @return the removed attribute node, or {@code null} if not found
     */
    @JsxFunction
    public Attr removeNamedItemNS(final String namespaceURI, final String localName) {
        return (Attr) attributes_.removeNamedItemNS(namespaceURI, localName);
    }

    /**
     * Returns the attribute node at the specified index.
     *
     * @param index the index
     * @return the attribute node at the given index, or {@code null} if out of range
     */
    @JsxFunction
    public HtmlUnitScriptable item(final int index) {
        final DomNode attr = (DomNode) attributes_.item(index);
        if (attr != null) {
            return attr.getScriptableObject();
        }
        return null;
    }

    /**
     * Returns the number of attributes in this map.
     *
     * @return the number of attributes
     */
    @JsxGetter
    public int getLength() {
        return attributes_.getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final int index, final Scriptable start) {
        return index >= 0 && index < getLength();
    }

    /**
     * Returns an iterator over the values in this map.
     *
     * @return the iterator
     */
    @JsxSymbol
    public Scriptable iterator() {
        return JavaScriptEngine.newArrayIteratorTypeValues(getParentScope(), this);
    }
}
