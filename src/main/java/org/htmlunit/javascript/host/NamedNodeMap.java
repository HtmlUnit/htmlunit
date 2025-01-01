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
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core.html#ID-1780488922">DOM Level 2 Core Spec</a>
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms763824.aspx">IXMLDOMNamedNodeMap</a>
 */
@JsxClass
public class NamedNodeMap extends HtmlUnitScriptable {

    private final org.w3c.dom.NamedNodeMap attributes_;

    /**
     * We need default constructors to build the prototype instance.
     */
    public NamedNodeMap() {
        super();
        attributes_ = null;
    }

    /**
     * JavaScript constructor.
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
        setParentScope(element.getScriptableObject());
        setPrototype(getPrototype(getClass()));

        attributes_ = element.getAttributes();
        setDomNode(element, false);
    }

    /**
     * Returns the element at the specified index, or {@link #NOT_FOUND} if the index is invalid.
     * <p>
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
     * Gets the specified attribute but does not handle the synthetic class attribute for IE.
     * @see #getNamedItem(String)
     *
     * @param name attribute name
     * @return the attribute node, {@code null} if the attribute is not defined
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
     * Gets the specified attribute.
     * @param name attribute name
     * @return the attribute node, {@code null} if the attribute is not defined
     */
    @JsxFunction
    public HtmlUnitScriptable getNamedItem(final String name) {
        return getNamedItemWithoutSytheticClassAttr(name);
    }

    /**
     * Gets the specified attribute.
     * @param namespaceURI the namespace URI of the node to retrieve.
     * @param localName the local name of the node to retrieve.
     * @return the attribute node, {@code null} if the attribute is not defined
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
     * Sets the specified attribute.
     * @param node the attribute
     */
    @JsxFunction
    public void setNamedItem(final Node node) {
        attributes_.setNamedItem(node.getDomNodeOrDie());
    }

    /**
     * Sets the specified attribute.
     * @param node the attribute
     */
    @JsxFunction
    public void setNamedItemNS(final Node node) {
        attributes_.setNamedItemNS(node.getDomNodeOrDie());
    }

    /**
     * Removes the specified attribute.
     * @param name the name of the item to remove
     */
    @JsxFunction
    public void removeNamedItem(final String name) {
        attributes_.removeNamedItem(name);
    }

    /**
     * Removes the specified attribute.
     * @param namespaceURI the namespace URI of the node to retrieve.
     * @param localName the local name of the node to retrieve.
     * @return the attribute node, {@code null} if the attribute is not defined
     */
    @JsxFunction
    public Attr removeNamedItemNS(final String namespaceURI, final String localName) {
        return (Attr) attributes_.removeNamedItemNS(namespaceURI, localName);
    }

    /**
     * Returns the item at the specified index.
     * @param index the index
     * @return the item at the specified index
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
     * Returns the number of attributes in this named node map.
     * @return the number of attributes in this named node map
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

    @JsxSymbol
    public Scriptable iterator() {
        return JavaScriptEngine.newArrayIteratorTypeValues(getParentScope(), this);
    }
}
