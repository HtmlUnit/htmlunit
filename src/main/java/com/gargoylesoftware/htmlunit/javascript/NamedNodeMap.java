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

import org.mozilla.javascript.Scriptable;

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

    private final org.w3c.dom.NamedNodeMap nodeMap_;

    /**
     * We need default constructors to build the prototype instance.
     */
    public NamedNodeMap() {
        nodeMap_ = null;
    }

    /**
     * Creates a new named node map for the specified element.
     *
     * @param element the owning element
     */
    public NamedNodeMap(final DomElement element) {
        setParentScope(element.getScriptObject());
        setPrototype(getPrototype(getClass()));

        nodeMap_ = element.getAttributes();
/*
        // for IE, there is no node without attribute. A fresh create span has 82 attributes!
        // just create at least one here to ensure that JS code using this as test for IE will pass
        if (element.getAttributesMap().isEmpty() && caseInsensitive && getBrowserVersion().isIE()) {
            element.setAttribute("language", "");
        }
        */
    }

    /**
     * Returns the element at the specified index, or <tt>NOT_FOUND</tt> if the index is invalid.
     *
     * {@inheritDoc}
     */
    @Override
    public final Object get(final int index, final Scriptable start) {
        final NamedNodeMap startMap = (NamedNodeMap) start;
        if (index >= 0 && index < startMap.nodeMap_.getLength()) {
            final DomNode attr = (DomNode) startMap.nodeMap_.item(index);
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
        final DomNode attr = (DomNode) nodeMap_.getNamedItem(name);
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
        final DomNode attr = (DomNode) nodeMap_.getNamedItem(name);
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
        return nodeMap_.getLength();
    }
}
