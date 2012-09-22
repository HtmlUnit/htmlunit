/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.w3c.dom.DOMException;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object that represents a TreeWalker.
 *
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Traversal-Range/traversal.html">
 * DOM-Level-2-Traversal-Range</a>
 * @version $Revision$
 * @author <a href="mailto:mike@10gen.com">Mike Dirolf</a>
 */
@JsxClass
public class TreeWalker extends SimpleScriptable {

    private Node root_, currentNode_;
    private long whatToShow_;
    private NodeFilter filter_;
    private boolean expandEntityReferences_;

    /**
     * Creates an instance.
     */
    public TreeWalker() {
    }

    /**
     * Creates an instance.
     *
     * @param root The root node of the TreeWalker. Must not be
     *          <code>null</code>.
     * @param whatToShow Flag specifying which types of nodes appear in the
     *          logical view of the TreeWalker. See {@link NodeFilter} for the
     *          set of possible Show_ values.
     * @param filter The {@link NodeFilter} to be used with this TreeWalker,
     *          or <code>null</code> to indicate no filter.
     * @param expandEntityReferences If false, the contents of
     *          EntityReference nodes are not present in the logical view.
     * @throws DOMException on attempt to create a TreeWalker with a root that
     *          is <code>null</code>.
     */
    public TreeWalker(final Node root,
                      final long whatToShow,
                      final NodeFilter filter,
                      final Boolean expandEntityReferences) throws DOMException {
        if (root == null) {
            Context.throwAsScriptRuntimeEx(new DOMException(DOMException.NOT_SUPPORTED_ERR,
                                   "root must not be null"));
        }
        root_ = root;
        whatToShow_ = whatToShow;
        filter_ = filter;
        expandEntityReferences_ = expandEntityReferences.booleanValue();
        currentNode_ = root_;
    }

    /**
     * Gets the root node of the TreeWalker, as specified when it was created.
     *
     * @return the root node of the TreeWalker
     */
    @JsxGetter
    public Node get_root() {
        return root_;
    }

    /**
     * Gets the whatToShow attribute of the TreeWalker. This attribute
     * determines which node types are presented via the TreeWalker. The set
     * of available constants is defined in {@link NodeFilter}.
     *
     * @return the value of the whatToShow attribute of the TreeWalker
     */
    @JsxGetter
    public long get_whatToShow() {
        if (whatToShow_ != NodeFilter.SHOW_ALL) {
            return 1;
        }
        return whatToShow_;
    }

    /**
     * Gets the filter used to screen nodes.
     *
     * @return the filter used to screen nodes
     */
    @JsxGetter
    public NodeFilter get_filter() {
        return filter_;
    }

    /**
     * Gets the flag specifying wherether or not to reject EntityReference nodes.
     *
     * @return the value of the expandEntityReferences flag
     */
    @JsxGetter
    public boolean get_expandEntityReferences() {
        return expandEntityReferences_;
    }

    /**
     * Gets the node at which the TreeWalker is currently positioned.
     *
     * @return the currentNode
     */
    @JsxGetter
    public Node get_currentNode() {
        return currentNode_;
    }

    /**
     * Sets the node at which the TreeWalker is currently positioned.
     *
     * @param currentNode The node to be used as the current position of the
     *          TreeWalker.
     * @throws DOMException on attempt to set currentNode to
     *          <code>null</code>.
     */
    @JsxSetter
    public void jsxSet_currentNode(final Node currentNode) throws DOMException {
        if (currentNode == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
                                   "currentNode cannot be set to null");
        }
        currentNode_ = currentNode;
    }

    /**
     * Given a node type, as defined in {@link Node}, return the appropriate
     * constant for whatToShow.
     *
     * @param type The node type to lookup.
     * @return the whatToShow constant for this node type.
     */
    private static int getFlagForNodeType(final short type) {
        switch (type) {
            case Node.ELEMENT_NODE:
                return NodeFilter.SHOW_ELEMENT;
            case Node.ATTRIBUTE_NODE:
                return NodeFilter.SHOW_ATTRIBUTE;
            case Node.TEXT_NODE:
                return NodeFilter.SHOW_TEXT;
            case Node.CDATA_SECTION_NODE:
                return NodeFilter.SHOW_CDATA_SECTION;
            case Node.ENTITY_REFERENCE_NODE:
                return NodeFilter.SHOW_ENTITY_REFERENCE;
            case Node.ENTITY_NODE:
                return NodeFilter.SHOW_ENTITY;
            case Node.PROCESSING_INSTRUCTION_NODE:
                return NodeFilter.SHOW_PROCESSING_INSTRUCTION;
            case Node.COMMENT_NODE:
                return NodeFilter.SHOW_COMMENT;
            case Node.DOCUMENT_NODE:
                return NodeFilter.SHOW_DOCUMENT;
            case Node.DOCUMENT_TYPE_NODE:
                return NodeFilter.SHOW_DOCUMENT_TYPE;
            case Node.DOCUMENT_FRAGMENT_NODE:
                return NodeFilter.SHOW_DOCUMENT_FRAGMENT;
            case Node.NOTATION_NODE:
                return NodeFilter.SHOW_NOTATION;
            default:
                return 0;
        }
    }

    /**
     * Test whether a specified node is visible in the logical view of a
     * TreeWalker, based solely on the whatToShow constant.
     *
     * @param n The node to check to see if it should be shown or not
     * @return a constant to determine whether the node is accepted, rejected,
     *          or skipped.
     */
    private short acceptNode(final Node n) {
        final short type = n.get_nodeType();
        final int flag = getFlagForNodeType(type);

        if ((whatToShow_ & flag) != 0) {
            return NodeFilter.FILTER_ACCEPT;
        }
        // Skip, don't reject.
        return NodeFilter.FILTER_SKIP;
    }

    /* Returns whether the node is visible by the TreeWalker. */
    private boolean isNodeVisible(final Node n) {
        if (acceptNode(n) == NodeFilter.FILTER_ACCEPT) {
            if (filter_ == null || filter_.acceptNode(n) == NodeFilter.FILTER_ACCEPT) {
                if (!expandEntityReferences_) {
                    if (n.getParent() != null && n.getParent().get_nodeType() == Node.ENTITY_REFERENCE_NODE) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /* Returns whether the node is rejected by the TreeWalker. */
    private boolean isNodeRejected(final Node n) {
        if (acceptNode(n) == NodeFilter.FILTER_REJECT) {
            return true;
        }
        if (filter_ != null && filter_.acceptNode(n) == NodeFilter.FILTER_REJECT) {
            return true;
        }
        if (!expandEntityReferences_) {
            if (n.getParent() != null && n.getParent().get_nodeType() == Node.ENTITY_REFERENCE_NODE) {
                return true;
            }
        }
        return false;
    }

    /* Returns whether the node is skipped by the TreeWalker. */
    private boolean isNodeSkipped(final Node n) {
        return (!isNodeVisible(n) && !isNodeRejected(n));
    }

    /**
     * Moves to and returns the closest visible ancestor node of the current
     * node. If the search for parentNode attempts to step upward from the
     * TreeWalker's root node, or if it fails to find a visible ancestor node,
     * this method retains the current position and returns null.
     *
     * @return The new parent node, or <code>null</code> if the current node
     *          has no parent in the TreeWalker's logical view.
     */
    @JsxFunction
    public Node parentNode() {
        if (currentNode_ == root_) {
            return null;
        }

        Node newNode = currentNode_;

        do {
            newNode = newNode.getParent();
        } while (newNode != null && !isNodeVisible(newNode) && newNode != root_);

        if (newNode == null || !isNodeVisible(newNode)) {
            return null;
        }
        currentNode_ = newNode;
        return newNode;
    }

    /**
     * Recursively find the logical node occupying the same position as this
     * _actual_ node. It could be the same node, a different node, or null
     * depending on filtering.
     *
     * @param n The actual node we are trying to find the "equivalent" of
     * @param lookLeft If true, traverse the tree in the left direction. If
     *          false, traverse the tree to the right.
     * @return the logical node in the same position as n
     */
    private Node getEquivalentLogical(final Node n, final boolean lookLeft) {
        // Base cases
        if (n == null) {
            return null;
        }
        if (isNodeVisible(n)) {
            return n;
        }

        // If a node is skipped, try getting one of its descendants
        if (isNodeSkipped(n)) {
            final Node child;
            if (lookLeft) {
                child = getEquivalentLogical(n.get_lastChild(), lookLeft);
            }
            else {
                child = getEquivalentLogical(n.get_firstChild(), lookLeft);
            }

            if (child != null) {
                return child;
            }
        }

        // If this node is rejected or has no decendants that will work, go
        // to its sibling.
        return getSibling(n, lookLeft);
    }

    // Helper method for getEquivalentLogical
    private Node getSibling(final Node n, final boolean lookLeft) {
        if (n == null) {
            return null;
        }

        if (isNodeVisible(n)) {
            return null;
        }

        final Node sibling;
        if (lookLeft) {
            sibling = n.get_previousSibling();
        }
        else {
            sibling = n.get_nextSibling();
        }

        if (sibling == null) {
            // If this node has no logical siblings at or below it's "level", it might have one above
            if (n == root_) {
                return null;
            }
            return getSibling(n.getParent(), lookLeft);

        }
        return getEquivalentLogical(sibling, lookLeft);
    }

    /**
     * Moves the TreeWalker to the first visible child of the current node,
     * and returns the new node. If the current node has no visible children,
     * returns <code>null</code>, and retains the current node.
     *
     * @return The new node, or <code>null</code> if the current node has no
     *          visible children in the TreeWalker's logical view.
     */
    @JsxFunction
    public Node firstChild() {
        final Node newNode = getEquivalentLogical(currentNode_.get_firstChild(), false);

        if (newNode != null) {
            currentNode_ = newNode;
        }

        return newNode;
    }

    /**
     * Moves the TreeWalker to the last visible child of the current node,
     * and returns the new node. If the current node has no visible children,
     * returns <code>null</code>, and retains the current node.
     *
     * @return The new node, or <code>null</code> if the current node has no
     *          visible children in the TreeWalker's logical view.
     */
    @JsxFunction
    public Node lastChild() {
        final Node newNode = getEquivalentLogical(currentNode_.get_lastChild(), true);

        if (newNode != null) {
            currentNode_ = newNode;
        }

        return newNode;
    }

    /**
      * Moves the TreeWalker to the previous sibling of the current node, and
      * returns the new node. If the current node has no visible previous
      * sibling, returns <code>null</code>, and retains the current node.
      *
      * @return The new node, or <code>null</code> if the current node has no
      *          previous sibling in the TreeWalker's logical view.
      */
    @JsxFunction
    public Node previousSibling() {
        if (currentNode_ == root_) {
            return null;
        }

        final Node newNode = getEquivalentLogical(currentNode_.get_previousSibling(), true);

        if (newNode != null) {
            currentNode_ = newNode;
        }

        return newNode;
    }

     /**
      * Moves the TreeWalker to the next sibling of the current node, and
      * returns the new node. If the current node has no visible next sibling,
      * returns <code>null</code>, and retains the current node.
      *
      * @return The new node, or <code>null</code> if the current node has no
      *          next sibling in the TreeWalker's logical view.
      */
    @JsxFunction
    public Node nextSibling() {
        if (currentNode_ == root_) {
            return null;
        }

        final Node newNode = getEquivalentLogical(currentNode_.get_nextSibling(), false);

        if (newNode != null) {
            currentNode_ = newNode;
        }

        return newNode;
    }

    /**
     * Moves the TreeWalker to the previous visible node in document order
     * relative to the current node, and returns the new node. If the current
     * node has no previous node, or if the search for previousNode attempts
     * to step upward from the TreeWalker's root node, returns
     * <code>null</code>, and retains the current node.
     *
     * @return The new node, or <code>null</code> if the current node has no
     *          previous node in the TreeWalker's logical view.
     */
    @JsxFunction
    public Node previousNode() {
        final Node newNode = getPreviousNode(currentNode_);

        if (newNode != null) {
            currentNode_ = newNode;
        }

        return newNode;
    }

    /**
     * Helper method to get the previous node in document order (preorder
     * traversal) from the given node.
     */
    private Node getPreviousNode(final Node n) {
        if (n == root_) {
            return null;
        }
        final Node left = getEquivalentLogical(n.get_previousSibling(), true);
        if (left == null) {
            final Node parent = n.getParent();
            if (parent == null) {
                return null;
            }
            if (isNodeVisible(parent)) {
                return parent;
            }
        }

        Node follow = left;
        while (follow.hasChildNodes()) {
            final Node toFollow = getEquivalentLogical(follow.get_lastChild(), true);
            if (toFollow == null) {
                break;
            }
            follow = toFollow;
        }
        return follow;
    }

    /**
     * Moves the TreeWalker to the next visible node in document order
     * relative to the current node, and returns the new node. If the current
     * node has no next node, or if the search for nextNode attempts to step
     * upward from the TreeWalker's root node, returns <code>null</code>, and
     * retains the current node.
     *
     * @return The new node, or <code>null</code> if the current node has no
     *          next node in the TreeWalker's logical view.
     */
    @JsxFunction
    public Node nextNode() {
        final Node leftChild = getEquivalentLogical(currentNode_.get_firstChild(), false);
        if (leftChild != null) {
            currentNode_ = leftChild;
            return leftChild;
        }
        final Node rightSibling = getEquivalentLogical(currentNode_.get_nextSibling(), false);
        if (rightSibling != null) {
            currentNode_ = rightSibling;
            return rightSibling;
        }

        final Node uncle = getFirstUncleNode(currentNode_);
        if (uncle != null) {
            currentNode_ = uncle;
            return uncle;
        }

        return null;
    }

    /**
     * Helper method to get the first uncle node in document order (preorder
     * traversal) from the given node.
     */
    private Node getFirstUncleNode(final Node n) {
        if (n == root_ || n == null) {
            return null;
        }

        final Node parent = n.getParent();
        if (parent == null) {
            return null;
        }

        final Node uncle = getEquivalentLogical(parent.get_nextSibling(), false);
        if (uncle != null) {
            return uncle;
        }

        return getFirstUncleNode(parent);
    }
}
