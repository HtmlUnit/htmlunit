/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.NOPTransformer;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.xpath.XPathUtils;

/**
 * An XPath implementation of {@link org.w3c.dom.NodeList}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author <a href="mailto:tom.anderson@univ.oxon.org">Tom Anderson</a>
 */
class XPathDomNodeList<E extends DomNode> extends AbstractList<E> implements DomNodeList<E>, Serializable {

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
    public XPathDomNodeList(final DomNode node, final String xpath) {
        this(node, xpath, NOPTransformer.INSTANCE);
    }

    /**
     * Creates a new node list. The elements will be "calculated" using the specified XPath
     * expression applied on the specified node, and using the specified transformer.
     * @param node the node to serve as root for the XPath expression
     * @param xpath the XPath expression which determines the elements of the node list
     * @param transformer the transformer used to transform elements of this node list
     */
    public XPathDomNodeList(final DomNode node, final String xpath, final Transformer transformer) {
        if (node != null) {
            node_ = node;
            xpath_ = xpath;
            transformer_ = transformer;
            final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl(this);
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
    private List<Object> getNodes() {
        if (cachedElements_ == null) {
            if (node_ != null) {
                cachedElements_ = XPathUtils.getByXPath(node_, xpath_, null);
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
    @Override
    public int size() {
        return getLength();
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
    @SuppressWarnings("unchecked")
    public E get(final int index) {
        return (E) getNodes().get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "XPathDomNodeList[" + xpath_ + "]";
    }

    /**
     * DOM change listener which clears the node cache when necessary.
     */
    private static final class DomHtmlAttributeChangeListenerImpl
                                    implements DomChangeListener, HtmlAttributeChangeListener {

        private transient WeakReference<XPathDomNodeList<?>> nodeList_;

        private DomHtmlAttributeChangeListenerImpl(final XPathDomNodeList<?> nodeList) {
            super();

            nodeList_ = new WeakReference<XPathDomNodeList<?>>(nodeList);
        }

        /**
         * {@inheritDoc}
         */
        public void nodeAdded(final DomChangeEvent event) {
            clearCache();
        }

        /**
         * {@inheritDoc}
         */
        public void nodeDeleted(final DomChangeEvent event) {
            clearCache();
        }

        /**
         * {@inheritDoc}
         */
        public void attributeAdded(final HtmlAttributeChangeEvent event) {
            clearCache();
        }

        /**
         * {@inheritDoc}
         */
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            clearCache();
        }

        /**
         * {@inheritDoc}
         */
        public void attributeReplaced(final HtmlAttributeChangeEvent event) {
            clearCache();
        }

        private void clearCache() {
            final XPathDomNodeList<?> nodes = getNodeListOrNull();
            if (null != nodes) {
                nodes.cachedElements_ = null;
            }
        }

        private XPathDomNodeList<?> getNodeListOrNull() {
            if (null == nodeList_) {
                return null;
            }
            return nodeList_.get();
        }
    }

}
