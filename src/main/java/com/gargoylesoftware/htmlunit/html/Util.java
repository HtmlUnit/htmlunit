/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic iterator over DOM nodes.
 *
 * <p>Concrete subclasses must implement the {@link #getFirstNode}
 * and {@link #getNextNode} methods for a specific iteration
 * strategy.</p>
 */
abstract class NodeIterator implements Iterator<DomNode> {

    private DomNode node_;

    /**
     * @param contextNode the starting node
     */
    public NodeIterator(final DomNode contextNode) {
        node_ = getFirstNode(contextNode);
    }

    /** {@inheritDoc} */
    public boolean hasNext() {
        return (node_ != null);
    }

    /** {@inheritDoc} */
    public DomNode next() {
        if (node_ == null) {
            throw new NoSuchElementException();
        }
        final DomNode ret = node_;
        node_ = getNextNode(node_);
        return ret;
    }

    /** {@inheritDoc} */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the first node for iteration.
     *
     * <p>This method must derive an initial node for iteration
     * from a context node.</p>
     *
     * @param contextNode the starting node
     * @return the first node in the iteration
     * @see #getNextNode
     */
    protected abstract DomNode getFirstNode(final DomNode contextNode);

    /**
     * Gets the next node for iteration.
     *
     * <p>This method must locate a following node from the
     * current context node.</p>
     *
     * @param contextNode the current node in the iteration
     * @return the following node in the iteration, or null
     * if there is none.
     * @see #getFirstNode
     */
    protected abstract DomNode getNextNode(final DomNode contextNode);
}
