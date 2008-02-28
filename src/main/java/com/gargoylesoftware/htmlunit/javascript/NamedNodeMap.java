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
package com.gargoylesoftware.htmlunit.javascript;

import org.apache.commons.collections.map.ListOrderedMap;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.HtmlAttr;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

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

    private ListOrderedMap nodes_;

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
        nodes_ = new ListOrderedMap();
        for (final HtmlAttr attr : element.getAttributesCollection()) {
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
            final HtmlAttr attr = (HtmlAttr) map.nodes_.getValue(index);
            return attr.getScriptObject();
        }
        else {
            return NOT_FOUND;
        }
    }

    /**
     * Returns the element with the specified name, or <tt>NOT_FOUND</tt> if the name is invalid.
     *
     * {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        final HtmlAttr attr = (HtmlAttr) nodes_.get(name);
        if (attr != null) {
            return attr.getScriptObject();
        }
        else {
            return NOT_FOUND;
        }
    }
    
    /**
     * Gets the specified attribute.
     * @param name attribute name.
     * @return The attribute node, <code>null</code> if the attribute is not defined
     */
    public Object jsxFunction_getNamedItem(final String name) {
        final HtmlAttr attr = (HtmlAttr) nodes_.get(name);
        if (attr != null) {
            return attr.getScriptObject();
        }
        else {
            return null;
        }
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
     * Not yet implemented.
     */
    public HtmlAttr getNamedItem(final String name) {
        return (HtmlAttr) nodes_.get(name);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public HtmlAttr getNamedItemNS(final String namespaceURI, final String localName) {
        throw new UnsupportedOperationException("NamedNodeMap.getOwnerElement is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public HtmlAttr item(final int index) {
        return (HtmlAttr) nodes_.get(index);
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
