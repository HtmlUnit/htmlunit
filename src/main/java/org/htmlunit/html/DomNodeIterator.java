/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

/**
 * An implementation of {@link NodeIterator}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class DomNodeIterator implements NodeIterator, Serializable {

    private final DomNode root_;
    private final int whatToShow_;
    private final NodeFilter filter_;
    private DomNode referenceNode_;
    private final boolean expandEntityReferences_;
    private boolean pointerBeforeReferenceNode_;

    /**
     * Creates a new instance.
     *
     * @param root The root node at which to begin the {@link NodeIterator}'s traversal
     * @param whatToShow an optional int representing a bitmask created by combining
     *        the constant properties of {@link NodeFilter}
     * @param expandEntityReferences If false, the contents of
     *          EntityReference nodes are not present in the logical view.
     * @param filter an object implementing the {@link NodeFilter} interface
     */
    public DomNodeIterator(final DomNode root, final int whatToShow, final NodeFilter filter,
            final boolean expandEntityReferences) {
        root_ = root;
        referenceNode_ = root;
        whatToShow_ = whatToShow;
        filter_ = filter;
        expandEntityReferences_ = expandEntityReferences;
        pointerBeforeReferenceNode_ = true;
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
    public boolean getExpandEntityReferences() {
        return expandEntityReferences_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeFilter getFilter() {
        return filter_;
    }

    /**
     * Returns whether the {@link NodeIterator} is anchored before, or after the node.
     * @return whether it is anchored before or after the node
     */
    public boolean isPointerBeforeReferenceNode() {
        return pointerBeforeReferenceNode_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detach() {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode nextNode() {
        return traverse(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode previousNode() {
        return traverse(false);
    }

    private DomNode traverse(final boolean next) {
        DomNode node = referenceNode_;
        boolean beforeNode = pointerBeforeReferenceNode_;

        do {
            if (next) {
                if (beforeNode) {
                    beforeNode = false;
                }
                else if (node != null) {
                    final DomNode firstChild = node.getFirstChild();
                    if (firstChild != null) {
                        node = firstChild;
                    }
                    else {
                        final DomNode nextSibling = node.getNextSibling();
                        node = nextSibling == null ? getFirstUncleNode(node) : nextSibling;
                    }
                }
            }
            else {
                if (beforeNode) {
                    DomNode follow = node == null ? null : node.getPreviousSibling();
                    while (follow != null && follow.hasChildNodes()) {
                        follow = follow.getLastChild();
                    }
                    node = follow;
                }
                else {
                    beforeNode = true;
                }
            }
        }
        while (node != null && (!isNodeVisible(node) || !isAccepted(node)));

        referenceNode_ = node;
        pointerBeforeReferenceNode_ = beforeNode;
        return node;
    }

    private boolean isNodeVisible(final Node node) {
        return (whatToShow_ & HtmlDomTreeWalker.getFlagForNode(node)) != 0;
    }

    private boolean isAccepted(final Node node) {
        if (filter_ == null) {
            return true;
        }
        return filter_.acceptNode(node) == NodeFilter.FILTER_ACCEPT;
    }

    /**
     * Helper method to get the first uncle node in document order (preorder
     * traversal) from the given node.
     */
    private DomNode getFirstUncleNode(final DomNode startNode) {
        DomNode curr = startNode;
        while (curr != null && curr != root_) {
            final DomNode parent = curr.getParentNode();
            if (parent == null || parent == root_) {
                return null;
            }
            final DomNode uncle = parent.getNextSibling();
            if (uncle != null) {
                return uncle;
            }
            curr = parent;
        }
        return null;
    }
}
