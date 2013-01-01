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
 * Provides some utilities for working on the Html document.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public final class Util {

    /** Private constructor to prevent instantiation. */
    private Util() {
        // does nothing
    }

    /**
     * Gets an iterator over all following nodes, depth-first.
     *
     * @param contextNode the context node for the following axis
     * @return a possibly-empty iterator (not null)
     */
    public static Iterator<DomNode> getFollowingSiblingAxisIterator(final DomNode contextNode) {
        return new NodeIterator(contextNode) {
            @Override
            protected DomNode getFirstNode(final DomNode node) {
                return getNextNode(node);
            }
            @Override
            protected DomNode getNextNode(final DomNode node) {
                return node.getNextSibling();
            }
        };
    }

    /**
     * Gets an iterator over all preceding siblings.
     *
     * @param contextNode the context node for the preceding sibling axis
     * @return a possibly-empty iterator (not null)
     */
    public static Iterator<DomNode> getPrecedingSiblingAxisIterator(final DomNode contextNode) {
        return new NodeIterator(contextNode) {
            @Override
            protected DomNode getFirstNode(final DomNode node) {
                return getNextNode(node);
            }
            @Override
            protected DomNode getNextNode(final DomNode node) {
                return node.getPreviousSibling();
            }
        };
    }

    /**
     * Gets an iterator over all following nodes, depth-first.
     *
     * @param contextNode the context node for the following axis
     * @return a possibly-empty iterator (not null)
     */
    public static Iterator<DomNode> getFollowingAxisIterator(final DomNode contextNode) {
        return new NodeIterator(contextNode) {
            @Override
            protected DomNode getFirstNode(final DomNode node) {
                if (node == null) {
                    return null;
                }
                final DomNode sibling = node.getNextSibling();
                if (sibling == null) {
                    return getFirstNode(node.getParentNode());
                }
                return sibling;
            }

            @Override
            protected DomNode getNextNode(final DomNode node) {
                if (node == null) {
                    return null;
                }
                DomNode n = node.getFirstChild();
                if (n == null) {
                    n = node.getNextSibling();
                }
                if (n == null) {
                    return getFirstNode(node.getParentNode());
                }
                return n;
            }
        };
    }

    /**
     * Gets an iterator over all preceding nodes, depth-first.
     *
     * @param contextNode the context node for the preceding axis
     * @return a possibly-empty iterator (not null)
     */
    public static Iterator<DomNode> getPrecedingAxisIterator(final DomNode contextNode) {
        return new NodeIterator(contextNode) {
            @Override
            protected DomNode getFirstNode(final DomNode node) {
                if (node == null) {
                    return null;
                }
                final DomNode sibling = node.getPreviousSibling();
                if (sibling == null) {
                    return getFirstNode(node.getParentNode());
                }
                return sibling;
            }
            @Override
            protected DomNode getNextNode(final DomNode node) {
                if (node == null) {
                    return null;
                }
                DomNode n = node.getLastChild();
                if (n == null) {
                    n = node.getPreviousSibling();
                }
                if (n == null) {
                    return getFirstNode(node.getParentNode());
                }
                return n;
            }
        };
    }
}

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
