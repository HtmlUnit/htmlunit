/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.host.Attr;

/**
 * A collection of nodes that can be accessed by name. String comparisons in this class are case-insensitive when
 * used with an {@link com.gargoylesoftware.htmlunit.html.HtmlElement},
 * but case-sensitive when used with a {@link DomElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core.html#ID-1780488922">DOM Level 2 Core Spec</a>
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms763824.aspx">IXMLDOMNamedNodeMap</a>
 */
public class NamedNodeMap extends SimpleScriptable implements ScriptableWithFallbackGetter {

    private static final long serialVersionUID = -1910087049570242560L;

    private final org.w3c.dom.NamedNodeMap nodeMap_;
    /**
     * In IE nodes always have attributes. In a first time we only need to have a least one to make
     * the MockKit tests happy and take the first one according to IE order: "language".
     */
    private final String fakedAttributeName_ = "language";
    private Attr fakedAttributeNode_;

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
        setDomNode(element, false);
    }

    /**
     * Returns the element at the specified index, or <tt>NOT_FOUND</tt> if the index is invalid.
     *
     * {@inheritDoc}
     */
    @Override
    public final Object get(final int index, final Scriptable start) {
        final NamedNodeMap startMap = (NamedNodeMap) start;
        final Object response = startMap.jsxFunction_item(index);
        if (response != null) {
            return response;
        }

        return NOT_FOUND;
    }

    /**
     * Returns the element with the specified name, or <tt>NOT_FOUND</tt> if the name is invalid.
     *
     * {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        final Object response = jsxFunction_getNamedItem(name);
        if (response != null) {
            return response;
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
        else if (fakedAttributeName_.equalsIgnoreCase(name) && shouldFakeAttributeForIE()) {
            return getFakedAttributeNode();
        }

        return null;
    }

    /**
     * Returns the item at the specified index.
     * @param index the index
     * @return the item at the specified index
     */
    public Object jsxFunction_item(final int index) {
        final DomNode attr = (DomNode) nodeMap_.item(index);
        if (attr != null) {
            return attr.getScriptObject();
        }
        else if (index == 0 && shouldFakeAttributeForIE()) {
            return getFakedAttributeNode();
        }

        return null;
    }

    private boolean shouldFakeAttributeForIE() {
        return nodeMap_.getLength() == 0 && getBrowserVersion().isIE() && getDomNodeOrDie() instanceof HtmlElement;
    }

    /**
     * Gets the faked attribute node, with lazy creating
     * @return the right {@link Attr}
     */
    private Object getFakedAttributeNode() {
        if (fakedAttributeNode_ == null) {
            fakedAttributeNode_ = new Attr();
            fakedAttributeNode_.setParentScope(this);
            fakedAttributeNode_.setPrototype(getPrototype(Attr.class));
            fakedAttributeNode_.init(fakedAttributeName_, null);
        }
        return fakedAttributeNode_;
    }

    /**
     * Returns the number of attributes in this named node map.
     * @return the number of attributes in this named node map
     */
    public int jsxGet_length() {
        final int length = nodeMap_.getLength();
        // hack to avoid showing 0 elements as IE always has some
        if (length == 0 && getBrowserVersion().isIE() && getDomNodeOrDie() instanceof HtmlElement) {
            return 1;
        }

        return length;
    }
}
