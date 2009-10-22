/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.impl;

import java.io.Serializable;
import java.util.Iterator;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.ranges.RangeException;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.DomText;

/**
 * Simple implementation of {@link Range}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Daniel Gredler
 */
public class SimpleRange implements Range, Serializable {

    private static final long serialVersionUID = 5779974839466976193L;

    /** The start (anchor) container. */
    private Node startContainer_;

    /** The end (focus) container. */
    private Node endContainer_;

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
    public SimpleRange(final Node node) {
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
    public SimpleRange(final Node node, final int offset) {
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
    public SimpleRange(final Node startNode, final int startOffset, final Node endNode, final int endOffset) {
        startContainer_ = startNode;
        endContainer_ = endNode;
        startOffset_ = startOffset;
        endOffset_ = endOffset;
    }

    /**
     * {@inheritDoc}
     */
    public DocumentFragment cloneContents() throws DOMException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public Range cloneRange() throws DOMException {
        return new SimpleRange(this.startContainer_, this.startOffset_, this.endContainer_, this.endOffset_);
    }

    /**
     * {@inheritDoc}
     */
    public void collapse(final boolean toStart) throws DOMException {
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
     * {@inheritDoc}
     */
    public short compareBoundaryPoints(final short how, final Range sourceRange) throws DOMException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void deleteContents() throws DOMException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void detach() throws DOMException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public DomDocumentFragment extractContents() throws DOMException {
        // Clone the common ancestor.
        final DomNode ancestor = (DomNode) getCommonAncestorContainer();
        final DomNode ancestorClone = ancestor.cloneNode(true);

        // Find the start container and end container clones.
        DomNode startClone = null;
        DomNode endClone = null;
        final DomNode start = (DomNode) startContainer_;
        final DomNode end = (DomNode) endContainer_;
        if (start == ancestor) {
            startClone = ancestorClone;
        }
        if (end == ancestor) {
            endClone = ancestorClone;
        }
        final Iterable< DomNode > descendants = ancestor.getDescendants();
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

        // Remove everything prior to the selection start from the clones.
        if (startClone == null) {
            throw Context.reportRuntimeError("Unable to find start node clone.");
        }
        deleteBefore(startClone, startOffset_);
        for (DomNode n = startClone; n != null; n = n.getParentNode()) {
            for (DomNode prev = n.getPreviousSibling(); prev != null; prev = prev.getPreviousSibling()) {
                prev.remove();
            }
        }

        // Remove everything following the selection end from the clones.
        if (endClone == null) {
            throw Context.reportRuntimeError("Unable to find end node clone.");
        }
        deleteAfter(endClone, endOffset_);
        for (DomNode n = endClone; n != null; n = n.getParentNode()) {
            for (DomNode next = n.getNextSibling(); next != null; next = next.getNextSibling()) {
                next.remove();
            }
        }

        // Remove everything inside the range from the original nodes.
        boolean foundStartNode = (ancestor == start); // whether or not we have found the start node yet
        boolean started = false; // whether or not we have found the start node *and* start offset yet
        boolean foundEndNode = false; // whether or not we have found the end node yet
        final Iterator<DomNode> i = ancestor.getDescendants().iterator();
        while (i.hasNext()) {
            final DomNode n = i.next();
            if (!foundStartNode) {
                foundStartNode = (n == start);
                if (foundStartNode && isOffsetChars(n)) {
                    started = true;
                    String text = getText(n);
                    text = text.substring(0, startOffset_);
                    setText(n, text);
                }
            }
            else if (!started) {
                final boolean atStart = (n.getParentNode() == start && n.getIndex() == startOffset_);
                final boolean beyondStart = !start.isAncestorOf(n);
                started = (atStart || beyondStart);
            }
            if (started) {
                if (!foundEndNode) {
                    foundEndNode = (n == end);
                }
                if (!foundEndNode) {
                    // We're inside the range.
                    if (!n.isAncestorOfAny(start, end)) {
                        i.remove();
                    }
                }
                else {
                    // We've reached the end of the range.
                    if (isOffsetChars(n)) {
                        String text = getText(n);
                        text = text.substring(endOffset_);
                        setText(n, text);
                    }
                    else {
                        final DomNodeList< DomNode > children = n.getChildNodes();
                        for (int j = endOffset_ - 1; j >= 0; j--) {
                            children.get(j).remove();
                        }
                    }
                    break;
                }
            }
        }

        // Build the document fragment using the cloned nodes, and return it.
        final SgmlPage page = ancestor.getPage();
        final DomDocumentFragment fragment = new DomDocumentFragment(page);
        for (DomNode n : ancestorClone.getChildNodes()) {
            fragment.appendChild(n);
        }
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    public boolean getCollapsed() throws DOMException {
        return startContainer_ == endContainer_ && startOffset_ == endOffset_;
    }

    /**
     * {@inheritDoc}
     */
    public Node getCommonAncestorContainer() throws DOMException {
        if (startContainer_ != null && endContainer_ != null) {
            for (Node p1 = startContainer_; p1 != null; p1 = p1.getParentNode()) {
                for (Node p2 = endContainer_; p2 != null; p2 = p2.getParentNode()) {
                    if (p1 == p2) {
                        return p1;
                    }
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Node getEndContainer() throws DOMException {
        return endContainer_;
    }

    /**
     * {@inheritDoc}
     */
    public int getEndOffset() throws DOMException {
        return endOffset_;
    }

    /**
     * {@inheritDoc}
     */
    public Node getStartContainer() throws DOMException {
        return startContainer_;
    }

    /**
     * {@inheritDoc}
     */
    public int getStartOffset() throws DOMException {
        return startOffset_;
    }

    /**
     * {@inheritDoc}
     */
    public void insertNode(final Node newNode) throws DOMException, RangeException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void selectNode(final Node node) throws RangeException, DOMException {
        startContainer_ = node;
        startOffset_ = 0;
        endContainer_ = node;
        endOffset_ = getMaxOffset(node);
    }

    /**
     * {@inheritDoc}
     */
    public void selectNodeContents(final Node node) throws RangeException, DOMException {
        startContainer_ = node.getFirstChild();
        startOffset_ = 0;
        endContainer_ = node.getLastChild();
        endOffset_ = getMaxOffset(node.getLastChild());
    }

    /**
     * {@inheritDoc}
     */
    public void setEnd(final Node refNode, final int offset) throws RangeException, DOMException {
        endContainer_ = refNode;
        endOffset_ = offset;
    }

    /**
     * {@inheritDoc}
     */
    public void setEndAfter(final Node refNode) throws RangeException, DOMException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void setEndBefore(final Node refNode) throws RangeException, DOMException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void setStart(final Node refNode, final int offset) throws RangeException, DOMException {
        startContainer_ = refNode;
        startOffset_ = offset;
    }

    /**
     * {@inheritDoc}
     */
    public void setStartAfter(final Node refNode) throws RangeException, DOMException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void setStartBefore(final Node refNode) throws RangeException, DOMException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void surroundContents(final Node newParent) throws DOMException, RangeException {
        throw new RuntimeException("Not implemented!");
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
        final StringBuilder sb = new StringBuilder();
        append(sb);
        return sb.toString();
    }

    private void append(final StringBuilder sb) {
        if (startContainer_ == endContainer_) {
            if (startOffset_ == endOffset_) {
                return;
            }
            else if (isOffsetChars(startContainer_)) {
                sb.append(getText(startContainer_).substring(startOffset_, endOffset_));
                return;
            }
        }

        final DomNode ancestor = (DomNode) getCommonAncestorContainer();
        final DomNode start = (DomNode) startContainer_;
        final DomNode end = (DomNode) endContainer_;
        boolean foundStartNode = (ancestor == start); // whether or not we have found the start node yet
        boolean started = false; // whether or not we have found the start node *and* start offset yet
        boolean foundEndNode = false; // whether or not we have found the end node yet
        final Iterator<DomNode> i = ancestor.getDescendants().iterator();
        while (i.hasNext()) {
            final DomNode n = i.next();
            if (!foundStartNode) {
                foundStartNode = (n == start);
                if (foundStartNode && isOffsetChars(n)) {
                    started = true;
                    String text = getText(n);
                    text = text.substring(startOffset_);
                    sb.append(text);
                }
            }
            else if (!started) {
                final boolean atStart = (n.getParentNode() == start && n.getIndex() == startOffset_);
                final boolean beyondStart = !start.isAncestorOf(n);
                started = (atStart || beyondStart);
            }
            if (started) {
                if (!foundEndNode) {
                    foundEndNode = (n == end);
                }
                if (!foundEndNode) {
                    // We're inside the range.
                    if (!n.isAncestorOfAny(start, end) && isOffsetChars(n)) {
                        sb.append(getText(n));
                    }
                }
                else {
                    // We've reached the end of the range.
                    if (isOffsetChars(n)) {
                        String text = getText(n);
                        text = text.substring(0, endOffset_);
                        sb.append(text);
                    }
                    else {
                        final DomNodeList<DomNode> children = n.getChildNodes();
                        for (int j = 0; j < endOffset_; j++) {
                            sb.append(getText(children.get(j)));
                        }
                    }
                    break;
                }
            }
        }
    }

    private static boolean isOffsetChars(final Node node) {
        return node instanceof DomText || node instanceof SelectableTextInput;
    }

    private static String getText(final Node node) {
        if (node instanceof SelectableTextInput) {
            return ((SelectableTextInput) node).getText();
        }
        return node.getTextContent();
    }

    private static void setText(final Node node, final String text) {
        if (node instanceof SelectableTextInput) {
            ((SelectableTextInput) node).setText(text);
        }
        else {
            node.setTextContent(text);
        }
    }

    private static void deleteBefore(final DomNode node, int offset) {
        if (isOffsetChars(node)) {
            String text = getText(node);
            text = text.substring(offset);
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
            text = text.substring(0, offset);
            setText(node, text);
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

    private static int getMaxOffset(final Node node) {
        return isOffsetChars(node) ? getText(node).length() : node.getChildNodes().getLength();
    }

}
