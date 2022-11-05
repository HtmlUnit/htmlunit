/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLDOMNamedNodeMap.<br>
 * Adds support for namespaces and iteration through the collection of attribute nodes.<br>
 * String comparisons in this class are case-insensitive when used with an
 * {@link com.gargoylesoftware.htmlunit.html.HtmlElement},
 * but case-sensitive when used with a {@link com.gargoylesoftware.htmlunit.html.DomElement}.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms763824.aspx">MSDN documentation</a>
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(IE)
public class XMLDOMNamedNodeMap extends MSXMLScriptable {

    private final org.w3c.dom.NamedNodeMap attributes_;

    private int currentIndex_;

    /**
     * Creates an instance.
     */
    public XMLDOMNamedNodeMap() {
        attributes_ = null;
    }

    /**
     * Creates a new named node map for the specified node.
     *
     * @param node the owning node
     */
    public XMLDOMNamedNodeMap(final DomNode node) {
        setParentScope(node.getScriptableObject());
        setPrototype(getPrototype(getClass()));

        attributes_ = node.getAttributes();
        setDomNode(node, false);
    }

    /**
     * Returns the element at the specified index, or <code>NOT_FOUND</code> if the index is invalid.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public final Object get(final int index, final Scriptable start) {
        final XMLDOMNamedNodeMap startMap = (XMLDOMNamedNodeMap) start;
        final Object response = startMap.item(index);
        if (response != null) {
            return response;
        }
        return NOT_FOUND;
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
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Gets the specified attribute but does not handle the synthetic class attribute for IE.
     * @see #getNamedItem(String)
     *
     * @param name attribute name
     * @return the attribute node, {@code null} if the attribute is not defined
     */
    public Object getNamedItemWithoutSyntheticClassAttr(final String name) {
        if (attributes_ != null) {
            final DomNode attr = (DomNode) attributes_.getNamedItem(name);
            if (attr != null) {
                return attr.getScriptableObject();
            }
        }
        return null;
    }

    /**
     * Retrieves the attribute with the specified name.
     * @param name specifies the name of the attribute
     * @return the attribute node, {@code null} if the attribute is not defined
     */
    @JsxFunction
    public Object getNamedItem(final String name) {
        if (name == null || "null".equals(name)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        return getNamedItemWithoutSyntheticClassAttr(name);
    }

    /**
     * Allows random access to individual nodes within the collection.
     * @param index the index of the item within the collection; the first item is zero
     * @return the item at the specified index
     */
    @JsxFunction
    public Object item(final int index) {
        final DomNode attr = (DomNode) attributes_.item(index);
        if (attr != null) {
            return attr.getScriptableObject();
        }
        return null;
    }

    /**
     * Returns the next node in the collection.
     * @return the next node in the collection or {@code null} if there is no next node
     */
    @JsxFunction
    public Object nextNode() {
        return item(currentIndex_++);
    }

    /**
     * Removes an attribute from the collection.
     * @param name the string specifying the name of the attribute to remove from the collection
     * @return the node removed from the collection or {@code null} if the named node is not an attribute
     */
    @JsxFunction
    public Object removeNamedItem(final String name) {
        if (name == null || "null".equals(name)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        final DomNode attr = (DomNode) attributes_.removeNamedItem(name);
        if (attr != null) {
            return attr.getScriptableObject();
        }
        return null;
    }

    /**
     * Resets the iterator accessed via {@link #nextNode()}.
     */
    @JsxFunction
    public void reset() {
        currentIndex_ = 0;
    }

    /**
     * Adds the supplied node to the collection.
     * @param node the object containing the attribute to be added to the collection
     * @return the attribute successfully added to the collection
     */
    @JsxFunction
    public Object setNamedItem(final XMLDOMNode node) {
        if (node == null) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        attributes_.setNamedItem(node.getDomNodeOrDie());
        return node;
    }
}
