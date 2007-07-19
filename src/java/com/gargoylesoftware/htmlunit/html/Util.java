/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Provides some utilities for working on the Html document.
 * @version $Revision$
 * @author Marc Guillemot
 */
public final class Util {

    /** Private constructor to prevent instantiation */
    private Util() {
        // does nothing
    }
    
    /**
     * Get an iterator over all following nodes, depth-first.
     *
     * @param contextNode The context node for the following axis.
     * @return A possibly-empty iterator (not null).
     */
    public static Iterator getFollowingSiblingAxisIterator(final DomNode contextNode) {
        return new NodeIterator(contextNode) {
            protected DomNode getFirstNode(final DomNode node) {
                return getNextNode(node);
            }
            protected DomNode getNextNode(final DomNode node) {
                return node.getNextSibling();
            }
        };
    }

    /**
     * Get an iterator over all preceding siblings.
     *
     * @param contextNode The context node for the preceding sibling axis.
     * @return A possibly-empty iterator (not null).
     */
    public static Iterator getPrecedingSiblingAxisIterator(final DomNode contextNode) {
        return new NodeIterator(contextNode) {
            protected DomNode getFirstNode(final DomNode node) {
                return getNextNode(node);
            }
            protected DomNode getNextNode(final DomNode node) {
                return node.getPreviousSibling();
            }
        };
    }
    
    /**
     * Get an iterator over all following nodes, depth-first.
     *
     * @param contextNode The context node for the following axis.
     * @return A possibly-empty iterator (not null).
     */
    public static Iterator getFollowingAxisIterator(final DomNode contextNode) {
        return new NodeIterator(contextNode) {
            protected DomNode getFirstNode(final DomNode node) {
                if (node == null) {
                    return null;
                } 
                else {
                    final DomNode sibling = node.getNextSibling();
                    if (sibling == null) {
                        return getFirstNode(node.getParentNode());
                    }
                    else {
                        return sibling;
                    }
                }
            }

            protected DomNode getNextNode(final DomNode node) {
                if (node == null) {
                    return null;
                }
                else {
                    DomNode n = node.getFirstChild();
                    if (n == null) {
                        n = node.getNextSibling();
                    }
                    if (n == null) {
                        return getFirstNode(node.getParentNode());
                    }
                    else {
                        return n;
                    }
                }
            }
        };
    }

    /**
     * Get an iterator over all preceding nodes, depth-first.
     *
     * @param contextNode The context node for the preceding axis.
     * @return A possibly-empty iterator (not null).
     */
    public static Iterator getPrecedingAxisIterator(final DomNode contextNode) {
        return new NodeIterator(contextNode) {
            protected DomNode getFirstNode(final DomNode node) {
                if (node == null) {
                    return null;
                }
                else {
                    final DomNode sibling = node.getPreviousSibling();
                    if (sibling == null) {
                        return getFirstNode(node.getParentNode());
                    }
                    else {
                        return sibling;
                    }
                }
            }
            protected DomNode getNextNode(final DomNode node) {
                if (node == null) {
                    return null;
                }
                else {
                    DomNode n = node.getLastChild();
                    if (n == null) {
                        n = node.getPreviousSibling();
                    }
                    if (n == null) {
                        return getFirstNode(node.getParentNode());
                    }
                    else {
                        return n;
                    }
                }
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
abstract class NodeIterator implements Iterator {

    private DomNode node_;

    /**
     * @param contextNode The starting node.
     */
    public NodeIterator(final DomNode contextNode) {
        node_ = getFirstNode(contextNode);
    }
    
    /** @inheritDoc Iterator#hasNext() */
    public boolean hasNext() {
        return (node_ != null);
    }
    
    /** @inheritDoc Iterator#next() */
    public Object next() {
        if (node_ == null) {
            throw new NoSuchElementException();
        }
        final DomNode ret = node_;
        node_ = getNextNode(node_);
        return ret;
    }
    
    /** @inheritDoc Iterator#remove() */
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Get the first node for iteration.
     *
     * <p>This method must derive an initial node for iteration
     * from a context node.</p>
     *
     * @param contextNode The starting node.
     * @return The first node in the iteration.
     * @see #getNextNode
     */
    protected abstract DomNode getFirstNode(final DomNode contextNode);
    
    /**
     * Get the next node for iteration.
     *
     * <p>This method must locate a following node from the
     * current context node.</p>
     *
     * @param contextNode The current node in the iteration.
     * @return The following node in the iteration, or null
     * if there is none.
     * @see #getFirstNode
     */
    protected abstract DomNode getNextNode(final DomNode contextNode);
}
