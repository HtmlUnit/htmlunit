/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A JavaScript object for {@code NodeIterator}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class NodeIterator extends SimpleScriptable {

    private Node root_;
    private long whatToShow_;
    private Scriptable filter_;
    private Node referenceNode_;
    private boolean pointerBeforeReferenceNode_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public NodeIterator() {
    }

    /**
     * Returns the root node.
     * @return the root node
     */
    @JsxGetter
    public Node getRoot() {
        return root_;
    }

    /**
     * Returns the types of nodes being presented.
     * @return combined bitmask of {@link NodeFilter}
     */
    public double getWhatToShow() {
        return whatToShow_;
    }

    /**
     * Returns the filter.
     * @return the filter
     */
    public Scriptable getFilter() {
        return filter_;
    }

    /**
     * Returns the {@link Node} to which the iterator is anchored.
     * @return the reference node
     */
    public Node getReferenceNode() {
        return referenceNode_;
    }

    /**
     * Returns whether the {@link NodeIterator} is anchored before, or after the node.
     * @return whether it is anchored before or after the node
     */
    public boolean isPointerBeforeReferenceNode() {
        return pointerBeforeReferenceNode_;
    }

    /**
     * Creates a new instance.
     *
     * @param root The root node at which to begin the {@link NodeIterator}'s traversal
     * @param whatToShow an optional long representing a bitmask created by combining
     * the constant properties of {@link NodeFilter}
     * @param filter an object implementing the {@link NodeFilter} interface
     */
    public NodeIterator(final Node root, final double whatToShow, final Scriptable filter) {
        root_ = root;
        referenceNode_ = root;
        whatToShow_ = Double.valueOf(whatToShow).longValue();
        filter_ = filter;
        pointerBeforeReferenceNode_ = true;
    }

    /**
     * This operation is a no-op.
     */
    @JsxFunction
    public void detach() {
    }

    /**
     * Returns the next Node in the document, or null if there are none.
     * @return the next node
     */
    @JsxFunction
    public Node nextNode() {
        return traverse(true);
    }

    /**
     * Returns the previous Node in the document, or null if there are none.
     * @return the previous node
     */
    @JsxFunction
    public Node previousNode() {
        return traverse(false);
    }

    private Node traverse(final boolean next) {
        Node node = referenceNode_;
        boolean beforeNode = pointerBeforeReferenceNode_;
        do {
            if (next) {
                if (beforeNode) {
                    beforeNode = false;
                }
                else {
                    final Node leftChild = getChild(node, true);
                    if (leftChild != null) {
                        node = leftChild;
                    }
                    else {
                        final Node rightSibling = getSibling(node, false);
                        if (rightSibling != null) {
                            node = rightSibling;
                        }
                        else {
                            node = getFirstUncleNode(node);
                        }
                    }
                }
            }
            else {
                if (!beforeNode) {
                    beforeNode = true;
                }
                else {
                    final Node left = getSibling(node, true);
                    if (left == null) {
                        final Node parent = node.getParent();
                        if (parent == null) {
                            node = null;
                        }
                    }

                    Node follow = left;
                    if (follow != null) {
                        while (follow.hasChildNodes()) {
                            final Node toFollow = getChild(follow, false);
                            if (toFollow == null) {
                                break;
                            }
                            follow = toFollow;
                        }
                    }
                    node = follow;
                }
            }
        } while (node != null && (!(isNodeVisible(node)) || !isAccepted(node)));

        //apply filter here and loop

        referenceNode_ = node;
        pointerBeforeReferenceNode_ = beforeNode;
        return node;
    }

    private boolean isNodeVisible(final Node node) {
        return (whatToShow_ & TreeWalker.getFlagForNode(node)) != 0;
    }

    private boolean isAccepted(final Node node) {
        if (filter_ == null) {
            return true;
        }
        Function function = null;
        if (filter_ instanceof Function) {
            function = (Function) filter_;
        }
        final Object acceptNode = filter_.get("acceptNode", filter_);
        if (acceptNode instanceof Function) {
            function = (Function) acceptNode;
        }
        if (function != null) {
            final double value = Context.toNumber(function.call(Context.getCurrentContext(), getParentScope(),
                    this, new Object[] {node}));
            return value == NodeFilter.FILTER_ACCEPT;
        }
        return true;
    }

    /**
     * Helper method to get the first uncle node in document order (preorder
     * traversal) from the given node.
     */
    private Node getFirstUncleNode(final Node node) {
        if (node == root_ || node == null) {
            return null;
        }

        final Node parent = node.getParent();
        if (parent == null) {
            return null;
        }

        final Node uncle = getSibling(parent, false);
        if (uncle != null) {
            return uncle;
        }

        return getFirstUncleNode(parent);
    }

    private static Node getChild(final Node node, final boolean lookLeft) {
        if (node == null) {
            return null;
        }

        final Node child;
        if (lookLeft) {
            child = node.getFirstChild();
        }
        else {
            child = node.getLastChild();
        }

        return child;
    }

    private static Node getSibling(final Node node, final boolean lookLeft) {
        if (node == null) {
            return null;
        }

        final Node sibling;
        if (lookLeft) {
            sibling = node.getPreviousSibling();
        }
        else {
            sibling = node.getNextSibling();
        }

        return sibling;
    }
}
