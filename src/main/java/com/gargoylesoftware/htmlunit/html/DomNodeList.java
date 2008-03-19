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
package com.gargoylesoftware.htmlunit.html;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.NOPTransformer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.html.xpath.XPathUtils;

/**
 * A {@link NodeList} for {@link DomNode}s.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
class DomNodeList implements NodeList, Serializable {

    private static final long serialVersionUID = 1541399090797298342L;

    /** The XPath expression which dictates the contents of this node list. */
    private String xpath_;

    /** This node list's root node. */
    private DomNode node_;

    /** The transformer used to transform elements of this node list, if any. */
    private Transformer transformer_;

    /** Element cache, used to avoid XPath expression evaluation as much as possible. */
    private List<Object> cachedElements_;

    /**
     * Creates a new node list. The elements will be "calculated" using the specified XPath
     * expression applied on the specified node.
     * @param node the node to serve as root for the XPath expression
     * @param xpath the XPath expression which determines the elements of the node list
     */
    public DomNodeList(final DomNode node, final String xpath) {
        this(node, xpath, NOPTransformer.INSTANCE);
    }

    /**
     * Creates a new node list. The elements will be "calculated" using the specified XPath
     * expression applied on the specified node, and using the specified transformer.
     * @param node the node to serve as root for the XPath expression
     * @param xpath the XPath expression which determines the elements of the node list
     * @param transformer the transformer used to transform elements of this node list
     */
    public DomNodeList(final DomNode node, final String xpath, final Transformer transformer) {
        if (node != null) {
            node_ = node;
            xpath_ = xpath;
            transformer_ = transformer;
            final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl();
            node_.addDomChangeListener(listener);
            if (node_ instanceof HtmlElement) {
                ((HtmlElement) node_).addHtmlAttributeChangeListener(listener);
                cachedElements_ = null;
            }
        }
    }

    /**
     * Returns the nodes in this node list, caching as necessary.
     * @return the nodes in this node list
     */
    @SuppressWarnings("unchecked")
    private List<Object> getNodes() {
        if (cachedElements_ == null) {
            if (node_ != null) {
                cachedElements_ = XPathUtils.getByXPath(node_, xpath_);
            }
            else {
                cachedElements_ = new ArrayList<Object>();
            }
        }
        return cachedElements_;
    }

    /**
     * {@inheritDoc}
     */
    public int getLength() {
        return getNodes().size();
    }

    /**
     * {@inheritDoc}
     */
    public Node item(final int index) {
        return (DomNode) transformer_.transform(getNodes().get(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DomNodeList[" + xpath_ + "]";
    }

    /**
     * DOM change listener which clears the node cache when necessary.
     */
    private class DomHtmlAttributeChangeListenerImpl implements DomChangeListener, HtmlAttributeChangeListener {

        /**
         * {@inheritDoc}
         */
        public void nodeAdded(final DomChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void nodeDeleted(final DomChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void attributeAdded(final HtmlAttributeChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void attributeReplaced(final HtmlAttributeChangeEvent event) {
            cachedElements_ = null;
        }
    }

}
