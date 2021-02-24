/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * An implementation of {@link TreeWalker}.
 *
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Traversal-Range/traversal.html">
 * DOM-Level-2-Traversal-Range</a>
 * @author <a href="mailto:mike@10gen.com">Mike Dirolf</a>
 * @author Frank Danek
 * @author Ahmed Ashour
 */
public class DomTreeWalker implements TreeWalker {

    private final DomNode root_;
    private DomNode currentNode_;
    private final int whatToShow_;
    private final NodeFilter filter_;
    private final boolean expandEntityReferences_;

    /**
     * Creates an instance.
     *
     * @param root The root node of the TreeWalker. Must not be
     *          {@code null}.
     * @param whatToShow Flag specifying which types of nodes appear in the
     *          logical view of the TreeWalker. See {@link NodeFilter} for the
     *          set of possible Show_ values.
     * @param filter The {@link NodeFilter} to be used with this TreeWalker,
     *          or {@code null} to indicate no filter.
     * @param expandEntityReferences If false, the contents of
     *          EntityReference nodes are not present in the logical view.
     * @throws DOMException on attempt to create a TreeWalker with a root that
     *          is {@code null}.
     */

    public DomTreeWalker(final DomNode root, final int whatToShow, final NodeFilter filter,
            final boolean expandEntityReferences) throws DOMException {
        if (root == null) {
            Context.throwAsScriptRuntimeEx(new DOMException(DOMException.NOT_SUPPORTED_ERR,
                    "root must not be null"));
        }
        root_ = root;
        whatToShow_ = whatToShow;
        filter_ = filter;
        expandEntityReferences_ = expandEntityReferences;
        currentNode_ = root_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode getRoot() {
        return root_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWhatToShow() {
        return whatToShow_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeFilter getFilter() {
        return filter_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getExpandEntityReferences() {
        return expandEntityReferences_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode getCurrentNode() {
        return currentNode_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentNode(final Node currentNode) throws DOMException {
        if (currentNode == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
                    "currentNode cannot be set to null");
        }
        currentNode_ = (DomNode) currentNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode nextNode() {
        final DomNode leftChild = getEquivalentLogical(currentNode_.getFirstChild(), false);
        if (leftChild != null) {
            currentNode_ = leftChild;
            return leftChild;
        }
        final DomNode rightSibling = getEquivalentLogical(currentNode_.getNextSibling(), false);
        if (rightSibling != null) {
            currentNode_ = rightSibling;
            return rightSibling;
        }

        final DomNode uncle = getFirstUncleNode(currentNode_);
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
    private DomNode getFirstUncleNode(final DomNode n) {
        if (n == root_ || n == null) {
            return null;
        }

        final DomNode parent = n.getParentNode();
        if (parent == null) {
            return null;
        }

        final DomNode uncle = getEquivalentLogical(parent.getNextSibling(), false);
        if (uncle != null) {
            return uncle;
        }

        return getFirstUncleNode(parent);
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
    private DomNode getEquivalentLogical(final DomNode n, final boolean lookLeft) {
        // Base cases
        if (n == null) {
            return null;
        }
        if (isNodeVisible(n)) {
            return n;
        }

        // If a node is skipped, try getting one of its descendants
        if (isNodeSkipped(n)) {
            final DomNode child;
            if (lookLeft) {
                child = getEquivalentLogical(n.getLastChild(), lookLeft);
            }
            else {
                child = getEquivalentLogical(n.getFirstChild(), lookLeft);
            }

            if (child != null) {
                return child;
            }
        }

        // If this node is rejected or has no descendants that will work, go
        // to its sibling.
        return getSibling(n, lookLeft);
    }

    /**
     * Returns whether the node is visible by the TreeWalker.
     */
    private boolean isNodeVisible(final Node n) {
        if (acceptNode(n) == NodeFilter.FILTER_ACCEPT) {
            if (filter_ == null || filter_.acceptNode(n) == NodeFilter.FILTER_ACCEPT) {
                return expandEntityReferences_ || n.getParentNode() == null
                        || n.getParentNode().getNodeType() != Node.ENTITY_REFERENCE_NODE;
            }
        }
        return false;
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
        final int flag = getFlagForNode(n);

        if ((whatToShow_ & flag) != 0) {
            return NodeFilter.FILTER_ACCEPT;
        }
        // Skip, don't reject.
        return NodeFilter.FILTER_SKIP;
    }

    /**
     * Given a {@link Node}, return the appropriate constant for whatToShow.
     *
     * @param node the node
     * @return the whatToShow constant for the type of specified node
     */
    static int getFlagForNode(final Node node) {
        switch (node.getNodeType()) {
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

    /* Returns whether the node is skipped by the TreeWalker. */
    private boolean isNodeSkipped(final Node n) {
        return !isNodeVisible(n) && !isNodeRejected(n);
    }

    /* Returns whether the node is rejected by the TreeWalker. */
    private boolean isNodeRejected(final Node n) {
        if (acceptNode(n) == NodeFilter.FILTER_REJECT) {
            return true;
        }
        if (filter_ != null && filter_.acceptNode(n) == NodeFilter.FILTER_REJECT) {
            return true;
        }
        return !expandEntityReferences_ && n.getParentNode() != null
                && n.getParentNode().getNodeType() == Node.ENTITY_REFERENCE_NODE;
    }

    // Helper method for getEquivalentLogical
    private DomNode getSibling(final DomNode n, final boolean lookLeft) {
        if (n == null) {
            return null;
        }

        if (isNodeVisible(n)) {
            return null;
        }

        final DomNode sibling;
        if (lookLeft) {
            sibling = n.getPreviousSibling();
        }
        else {
            sibling = n.getNextSibling();
        }

        if (sibling == null) {
            // If this node has no logical siblings at or below it's "level", it might have one above
            if (n == root_) {
                return null;
            }
            return getSibling(n.getParentNode(), lookLeft);

        }
        return getEquivalentLogical(sibling, lookLeft);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode nextSibling() {
        if (currentNode_ == root_) {
            return null;
        }

        final DomNode newNode = getEquivalentLogical(currentNode_.getNextSibling(), false);

        if (newNode != null) {
            currentNode_ = newNode;
        }

        return newNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode parentNode() {
        if (currentNode_ == root_) {
            return null;
        }

        DomNode newNode = currentNode_;

        do {
            newNode = newNode.getParentNode();
        }
        while (newNode != null && !isNodeVisible(newNode) && newNode != root_);

        if (newNode == null || !isNodeVisible(newNode)) {
            return null;
        }
        currentNode_ = newNode;
        return newNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode previousSibling() {
        if (currentNode_ == root_) {
            return null;
        }

        final DomNode newNode = getEquivalentLogical(currentNode_.getPreviousSibling(), true);

        if (newNode != null) {
            currentNode_ = newNode;
        }

        return newNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode lastChild() {
        final DomNode newNode = getEquivalentLogical(currentNode_.getLastChild(), true);

        if (newNode != null) {
            currentNode_ = newNode;
        }

        return newNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode previousNode() {
        final DomNode newNode = getPreviousNode(currentNode_);

        if (newNode != null) {
            currentNode_ = newNode;
        }

        return newNode;
    }

    /**
     * Helper method to get the previous node in document order (preorder
     * traversal) from the given node.
     */
    private DomNode getPreviousNode(final DomNode n) {
        if (n == root_) {
            return null;
        }
        final DomNode left = getEquivalentLogical(n.getPreviousSibling(), true);
        if (left == null) {
            final DomNode parent = n.getParentNode();
            if (parent == null) {
                return null;
            }
            if (isNodeVisible(parent)) {
                return parent;
            }
        }

        DomNode follow = left;
        if (follow != null) {
            while (follow.hasChildNodes()) {
                final DomNode toFollow = getEquivalentLogical(follow.getLastChild(), true);
                if (toFollow == null) {
                    break;
                }
                follow = toFollow;
            }
        }
        return follow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode firstChild() {
        final DomNode newNode = getEquivalentLogical(currentNode_.getFirstChild(), false);

        if (newNode != null) {
            currentNode_ = newNode;
        }

        return newNode;
    }

}
