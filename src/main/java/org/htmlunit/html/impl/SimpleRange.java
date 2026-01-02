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
package org.htmlunit.html.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.htmlunit.SgmlPage;
import org.htmlunit.html.DomDocumentFragment;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomNodeList;
import org.htmlunit.html.DomText;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Simple implementation of an Range.
 *
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author James Phillpotts
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class SimpleRange implements Serializable {

    /** The start (anchor) container. */
    private DomNode startContainer_;

    /** The end (focus) container. */
    private DomNode endContainer_;

    /**
     * The start (anchor) offset; units are chars if the start container is a text node or an
     * input element, DOM nodes otherwise.
     */
    private int startOffset_;

    /**
     * The end (focus) offset; units are chars if the end container is a text node or an input
     * element, DOM nodes otherwise.
     */
    private int endOffset_;

    /**
     * Constructs a range without any content.
     */
    public SimpleRange() {
        // Empty.
    }

    /**
     * Constructs a range for the specified element.
     * @param node the node for the range
     */
    public SimpleRange(final DomNode node) {
        startContainer_ = node;
        endContainer_ = node;
        startOffset_ = 0;
        endOffset_ = getMaxOffset(node);
    }

    /**
     * Constructs a range for the provided element and start and end offset.
     * @param node the node for the range
     * @param offset the start and end offset
     */
    public SimpleRange(final DomNode node, final int offset) {
        startContainer_ = node;
        endContainer_ = node;
        startOffset_ = offset;
        endOffset_ = offset;
    }

    /**
     * Constructs a range for the provided elements and offsets.
     * @param startNode the start node
     * @param startOffset the start offset
     * @param endNode the end node
     * @param endOffset the end offset
     */
    public SimpleRange(final DomNode startNode, final int startOffset, final DomNode endNode, final int endOffset) {
        startContainer_ = startNode;
        endContainer_ = endNode;
        startOffset_ = startOffset;
        endOffset_ = endOffset;
        if (startNode == endNode && startOffset > endOffset) {
            endOffset_ = startOffset;
        }
    }

    /**
     * Duplicates the contents of this.
     * @return DocumentFragment that contains content equivalent to this
     */
    public DomDocumentFragment cloneContents() {
        // Clone the common ancestor.
        final DomNode ancestor = getCommonAncestorContainer();

        if (ancestor == null) {
            return new DomDocumentFragment(null);
        }
        final DomNode ancestorClone = ancestor.cloneNode(true);

        // Find the start container and end container clones.
        DomNode startClone = null;
        DomNode endClone = null;
        final DomNode start = startContainer_;
        final DomNode end = endContainer_;
        if (start == ancestor) {
            startClone = ancestorClone;
        }
        if (end == ancestor) {
            endClone = ancestorClone;
        }
        final Iterable<DomNode> descendants = ancestor.getDescendants();
        if (startClone == null || endClone == null) {
            final Iterator<DomNode> i = descendants.iterator();
            final Iterator<DomNode> ci = ancestorClone.getDescendants().iterator();
            while (i.hasNext()) {
                final DomNode e = i.next();
                final DomNode ce = ci.next();
                if (start == e) {
                    startClone = ce;
                }
                else if (end == e) {
                    endClone = ce;
                    break;
                }
            }
        }

        // Do remove from end first so that it can't affect the offset values

        // Remove everything following the selection end from the clones.
        if (endClone == null) {
            throw new IllegalStateException("Unable to find end node clone.");
        }
        deleteAfter(endClone, endOffset_);
        for (DomNode n = endClone; n != null; n = n.getParentNode()) {
            while (n.getNextSibling() != null) {
                n.getNextSibling().remove();
            }
        }

        // Remove everything prior to the selection start from the clones.
        if (startClone == null) {
            throw new IllegalStateException("Unable to find start node clone.");
        }
        deleteBefore(startClone, startOffset_);
        for (DomNode n = startClone; n != null; n = n.getParentNode()) {
            while (n.getPreviousSibling() != null) {
                n.getPreviousSibling().remove();
            }
        }

        final SgmlPage page = ancestor.getPage();
        final DomDocumentFragment fragment = new DomDocumentFragment(page);
        if (start == end) {
            fragment.appendChild(ancestorClone);
        }
        else {
            for (final DomNode n : ancestorClone.getChildNodes()) {
                fragment.appendChild(n);
            }
        }
        return fragment;
    }

    /**
     * Produces a new SimpleRange whose boundary-points are equal to the
     * boundary-points of this.
     * @return duplicated simple
     */
    public SimpleRange cloneRange() {
        return new SimpleRange(startContainer_, startOffset_, endContainer_, endOffset_);
    }

    /**
     * Collapse this range onto one of its boundary-points.
     * @param toStart if true, collapses the Range onto its start; else collapses it onto its end.
     */
    public void collapse(final boolean toStart) {
        if (toStart) {
            endContainer_ = startContainer_;
            endOffset_ = startOffset_;
        }
        else {
            startContainer_ = endContainer_;
            startOffset_ = endOffset_;
        }
    }

    /**
     * Removes the contents of this range from the containing document or
     * document fragment without returning a reference to the removed
     * content.
     */
    public void deleteContents() {
        final DomNode ancestor = getCommonAncestorContainer();
        if (ancestor != null) {
            deleteContents(ancestor);
        }
    }

    private void deleteContents(final DomNode ancestor) {
        final DomNode start;
        final DomNode end;
        if (isOffsetChars(startContainer_)) {
            start = startContainer_;
            String text = getText(start);
            if (startOffset_ > -1 && startOffset_ < text.length()) {
                text = text.substring(0, startOffset_);
            }
            setText(start, text);
        }
        else if (startContainer_.getChildNodes().getLength() > startOffset_) {
            start = (DomNode) startContainer_.getChildNodes().item(startOffset_);
        }
        else {
            start = startContainer_.getNextSibling();
        }
        if (isOffsetChars(endContainer_)) {
            end = endContainer_;
            String text = getText(end);
            if (endOffset_ > -1 && endOffset_ < text.length()) {
                text = text.substring(endOffset_);
            }
            setText(end, text);
        }
        else if (endContainer_.getChildNodes().getLength() > endOffset_) {
            end = (DomNode) endContainer_.getChildNodes().item(endOffset_);
        }
        else {
            end = endContainer_.getNextSibling();
        }
        boolean foundStart = false;
        boolean started = false;
        final Iterator<DomNode> i = ancestor.getDescendants().iterator();
        while (i.hasNext()) {
            final DomNode n = i.next();
            if (n == end) {
                break;
            }
            if (n == start) {
                foundStart = true;
            }
            if (foundStart && (n != start || !isOffsetChars(startContainer_))) {
                started = true;
            }
            if (started && !n.isAncestorOf(end)) {
                i.remove();
            }
        }
    }

    /**
     * Moves the contents of a Range from the containing document or document
     * fragment to a new DocumentFragment.
     * @return DocumentFragment containing the extracted contents
     * @throws DOMException in case of error
     */
    public DomDocumentFragment extractContents() throws DOMException {
        final DomDocumentFragment fragment = cloneContents();

        // Remove everything inside the range from the original nodes.
        deleteContents();

        // Build the document fragment using the cloned nodes, and return it.
        return fragment;
    }

    /**
     * @return true if startContainer equals endContainer and
     *         startOffset equals endOffset
     * @throws DOMException in case of error
     */
    public boolean isCollapsed() throws DOMException {
        return startContainer_ == endContainer_ && startOffset_ == endOffset_;
    }

    /**
     * @return the deepest common ancestor container of this range's two
     *         boundary-points.
     * @throws DOMException in case of error
     */
    public DomNode getCommonAncestorContainer() throws DOMException {
        if (startContainer_ != null && endContainer_ != null) {
            for (DomNode p1 = startContainer_; p1 != null; p1 = p1.getParentNode()) {
                for (DomNode p2 = endContainer_; p2 != null; p2 = p2.getParentNode()) {
                    if (p1 == p2) {
                        return p1;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return the Node within which this range ends
     */
    public DomNode getEndContainer() {
        return endContainer_;
    }

    /**
     * @return offset within the ending node of this
     */
    public int getEndOffset() {
        return endOffset_;
    }

    /**
     * @return the Node within which this range begins
     */
    public DomNode getStartContainer() {
        return startContainer_;
    }

    /**
     * @return offset within the starting node of this
     */
    public int getStartOffset() {
        return startOffset_;
    }

    /**
     * Inserts a node into the Document or DocumentFragment at the start of
     * the Range. If the container is a Text node, this will be split at the
     * start of the Range (as if the Text node's splitText method was
     * performed at the insertion point) and the insertion will occur
     * between the two resulting Text nodes. Adjacent Text nodes will not be
     * automatically merged. If the node to be inserted is a
     * DocumentFragment node, the children will be inserted rather than the
     * DocumentFragment node itself.
     * @param newNode The node to insert at the start of the Range
     */
    public void insertNode(final DomNode newNode) {
        if (isOffsetChars(startContainer_)) {
            final DomNode split = startContainer_.cloneNode(false);
            String text = getText(startContainer_);
            if (startOffset_ > -1 && startOffset_ < text.length()) {
                text = text.substring(0, startOffset_);
            }
            setText(startContainer_, text);
            text = getText(split);
            if (startOffset_ > -1 && startOffset_ < text.length()) {
                text = text.substring(startOffset_);
            }
            setText(split, text);
            insertNodeOrDocFragment(startContainer_.getParentNode(), split, startContainer_.getNextSibling());
            insertNodeOrDocFragment(startContainer_.getParentNode(), newNode, split);
        }
        else {
            insertNodeOrDocFragment(startContainer_, newNode,
                    (DomNode) startContainer_.getChildNodes().item(startOffset_));
        }

        setStart(newNode, 0);
    }

    private static void insertNodeOrDocFragment(final DomNode parent, final DomNode newNode, final DomNode refNode) {
        if (newNode instanceof DocumentFragment fragment) {

            final NodeList childNodes = fragment.getChildNodes();
            while (childNodes.getLength() > 0) {
                final Node item = childNodes.item(0);
                parent.insertBefore(item, refNode);
            }
        }
        else {
            parent.insertBefore(newNode, refNode);
        }
    }

    /**
     * Select a node and its contents.
     * @param node The node to select.
     */
    public void selectNode(final DomNode node) {
        startContainer_ = node;
        startOffset_ = 0;
        endContainer_ = node;
        endOffset_ = getMaxOffset(node);
    }

    /**
     * Select the contents within a node.
     * @param node Node to select from
     */
    public void selectNodeContents(final DomNode node) {
        startContainer_ = node.getFirstChild();
        startOffset_ = 0;
        endContainer_ = node.getLastChild();
        endOffset_ = getMaxOffset(node.getLastChild());
    }

    /**
     * Sets the attributes describing the end.
     * @param refNode the refNode
     * @param offset offset
     */
    public void setEnd(final DomNode refNode, final int offset) {
        endContainer_ = refNode;
        endOffset_ = offset;
    }

    /**
     * Sets the attributes describing the start.
     * @param refNode the refNode
     * @param offset offset
     */
    public void setStart(final DomNode refNode, final int offset) {
        startContainer_ = refNode;
        startOffset_ = offset;
    }

    /**
     * Reparents the contents of the Range to the given node and inserts the
     * node at the position of the start of the Range.
     * @param newParent The node to surround the contents with.
     */
    public void surroundContents(final DomNode newParent) {
        newParent.appendChild(extractContents());
        insertNode(newParent);
        setStart(newParent, 0);
        setEnd(newParent, getMaxOffset(newParent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof SimpleRange)) {
            return false;
        }
        final SimpleRange other = (SimpleRange) obj;
        return new EqualsBuilder()
            .append(startContainer_, other.startContainer_)
            .append(endContainer_, other.endContainer_)
            .append(startOffset_, other.startOffset_)
            .append(endOffset_, other.endOffset_).isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(startContainer_)
            .append(endContainer_)
            .append(startOffset_)
            .append(endOffset_).toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final DomDocumentFragment fragment = cloneContents();
        if (fragment.getPage() != null) {
            return fragment.asNormalizedText();
        }
        return "";
    }

    private static boolean isOffsetChars(final DomNode node) {
        return node instanceof DomText || node instanceof SelectableTextInput;
    }

    private static String getText(final DomNode node) {
        if (node instanceof SelectableTextInput input) {
            return input.getText();
        }
        return node.getTextContent();
    }

    private static void setText(final DomNode node, final String text) {
        if (node instanceof SelectableTextInput input) {
            input.setText(text);
        }
        else {
            node.setTextContent(text);
        }
    }

    private static void deleteBefore(final DomNode node, int offset) {
        if (isOffsetChars(node)) {
            String text = getText(node);
            if (offset > -1 && offset < text.length()) {
                text = text.substring(offset);
            }
            else {
                text = "";
            }
            setText(node, text);
        }
        else {
            final DomNodeList<DomNode> children = node.getChildNodes();
            for (int i = 0; i < offset && i < children.getLength(); i++) {
                final DomNode child = children.get(i);
                child.remove();
                i--;
                offset--;
            }
        }
    }

    private static void deleteAfter(final DomNode node, final int offset) {
        if (isOffsetChars(node)) {
            String text = getText(node);
            if (offset > -1 && offset < text.length()) {
                text = text.substring(0, offset);
                setText(node, text);
            }
        }
        else {
            final DomNodeList<DomNode> children = node.getChildNodes();
            for (int i = offset; i < children.getLength(); i++) {
                final DomNode child = children.get(i);
                child.remove();
                i--;
            }
        }
    }

    private static int getMaxOffset(final DomNode node) {
        return isOffsetChars(node) ? getText(node).length() : node.getChildNodes().getLength();
    }

    /**
     * @return a list with all nodes contained in this range
     */
    public List<DomNode> containedNodes() {
        final DomNode ancestor = getCommonAncestorContainer();
        if (ancestor == null) {
            return Collections.EMPTY_LIST;
        }

        final DomNode start;
        final DomNode end;
        if (isOffsetChars(startContainer_)) {
            start = startContainer_;
            String text = getText(start);
            if (startOffset_ > -1 && startOffset_ < text.length()) {
                text = text.substring(0, startOffset_);
            }
            setText(start, text);
        }
        else if (startContainer_.getChildNodes().getLength() > startOffset_) {
            start = (DomNode) startContainer_.getChildNodes().item(startOffset_);
        }
        else {
            start = startContainer_.getNextSibling();
        }
        if (isOffsetChars(endContainer_)) {
            end = endContainer_;
            String text = getText(end);
            if (endOffset_ > -1 && endOffset_ < text.length()) {
                text = text.substring(endOffset_);
            }
            setText(end, text);
        }
        else if (endContainer_.getChildNodes().getLength() > endOffset_) {
            end = (DomNode) endContainer_.getChildNodes().item(endOffset_);
        }
        else {
            end = endContainer_.getNextSibling();
        }

        boolean foundStart = false;
        boolean started = false;
        final List<DomNode> nodes = new ArrayList<>();
        for (final DomNode n : ancestor.getDescendants()) {
            if (n == end) {
                break;
            }
            if (n == start) {
                foundStart = true;
            }
            if (foundStart && (n != start || !isOffsetChars(startContainer_))) {
                started = true;
            }
            if (started && !n.isAncestorOf(end)) {
                nodes.add(n);
            }
        }
        return nodes;
    }
}
