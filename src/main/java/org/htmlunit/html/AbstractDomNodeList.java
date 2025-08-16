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
package org.htmlunit.html;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Node;

/**
 * A generic DomNodeList implementation of {@link org.w3c.dom.NodeList}.
 *
 * @param <E> The element type
 *
 * @author Daniel Gredler
 * @author Tom Anderson
 * @author Ronald Brill
 */
public abstract class AbstractDomNodeList<E extends DomNode> extends AbstractList<E>
    implements DomNodeList<E>, Serializable {

    /** This node list's root node. */
    private final DomNode node_;

    /** Element cache, used to avoid XPath expression evaluation as much as possible. */
    private List<E> cachedElements_;

    /**
     * Creates a new node list. The elements will be "calculated" using the specified XPath
     * expression applied on the specified node.
     * @param node the node to serve as root for the XPath expression
     */
    public AbstractDomNodeList(final DomNode node) {
        super();
        node_ = node;

        if (node == null) {
            cachedElements_ = Collections.EMPTY_LIST;
            return;
        }

        final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl(this);
        node_.addDomChangeListener(listener);
    }

    /**
     * Returns the DOM node.
     * @return the DOM node
     */
    protected DomNode getDomNode() {
        return node_;
    }

    /**
     * Returns the elements.
     * @return the elements
     */
    protected abstract List<E> provideElements();

    /**
     * Returns the nodes in this node list, caching as necessary.
     * @return the nodes in this node list
     */
    private List<E> getNodes() {
        // a bit of a hack but i like to avoid synchronization
        // see https://github.com/HtmlUnit/htmlunit/issues/882
        //
        // there is a small chance that the cachedElements_ are
        // set to null after the assignment and before the return
        // but this is a race condition at all and depending on the
        // thread state the same overall result might happen also with sync
        List<E> shortLivedCache = cachedElements_;
        if (cachedElements_ == null) {
            shortLivedCache = provideElements();
            cachedElements_ = shortLivedCache;
        }
        return shortLivedCache;
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
    @Override
    public int getLength() {
        return getNodes().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node item(final int index) {
        return getNodes().get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E get(final int index) {
        return getNodes().get(index);
    }

    /**
     * DOM change listener which clears the node cache when necessary.
     */
    private static final class DomHtmlAttributeChangeListenerImpl implements DomChangeListener {

        private final transient WeakReference<AbstractDomNodeList<?>> nodeList_;

        DomHtmlAttributeChangeListenerImpl(final AbstractDomNodeList<?> nodeList) {
            super();

            nodeList_ = new WeakReference<>(nodeList);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void nodeAdded(final DomChangeEvent event) {
            clearCache();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void nodeDeleted(final DomChangeEvent event) {
            clearCache();
        }

        private void clearCache() {
            if (nodeList_ != null) {
                final AbstractDomNodeList<?> nodes = nodeList_.get();
                if (nodes != null && nodes.node_ != null) {
                    nodes.cachedElements_ = null;
                }
            }
        }
    }

}
