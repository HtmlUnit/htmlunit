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
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * A collection of nodes that can be accessed by name. String comparisons in this class are case-insensitive when
 * used with an {@link com.gargoylesoftware.htmlunit.html.HtmlElement},
 * but case-sensitive when used with a {@link DomElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core.html#ID-1780488922">DOM Level 2 Core Spec</a>
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms763824.aspx">IXMLDOMNamedNodeMap</a>
 */
public class NamedNodeMap extends SimpleScriptable implements ScriptableWithFallbackGetter {

    private static final long serialVersionUID = -1910087049570242560L;

    private final ListOrderedMap nodes_ = new ListOrderedMap();

    /**
     * As per the <a href="http://www.w3.org/TR/REC-DOM-Level-1/level-one-core.html#ID-5DFED1F0">W3C</a> (and
     * some browser testing), this class should be case-sensitive when dealing with XML, and case-insensitive
     * when dealing with HTML.
     */
    private final boolean caseInsensitive_;

    /**
     * Rhino requires default constructors.
     */
    public NamedNodeMap() {
        caseInsensitive_ = true;
    }

    /**
     * Creates a new named node map for the specified element.
     *
     * @param element the owning element
     * @param caseInsensitive whether to ignore case or no
     */
    public NamedNodeMap(final DomElement element, final boolean caseInsensitive) {
        caseInsensitive_ = caseInsensitive;
        setParentScope(element.getScriptObject());
        setPrototype(getPrototype(getClass()));
/*
        // for IE, there is no node without attribute. A fresh create span has 82 attributes!
        // just create at least one here to ensure that JS code using this as test for IE will pass
        if (element.getAttributesMap().isEmpty() && caseInsensitive && getBrowserVersion().isIE()) {
            element.setAttribute("language", "");
        }
*/
        for (final DomAttr attr : element.getAttributesMap().values()) {
            String name = attr.getName();
            if (caseInsensitive) {
                name = name.toLowerCase();
            }
            nodes_.put(name, attr);
        }
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
    public Object getWithFallback(String name) {
        if (caseInsensitive_) {
            name = name.toLowerCase();
        }
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
    public Object jsxFunction_getNamedItem(String name) {
        if (caseInsensitive_) {
            name = name.toLowerCase();
        }
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
    public Attr getNamedItem(String name) {
        if (caseInsensitive_) {
            name = name.toLowerCase();
        }
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
