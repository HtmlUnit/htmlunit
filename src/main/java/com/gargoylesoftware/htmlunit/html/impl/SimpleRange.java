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

import org.apache.commons.lang.mutable.MutableBoolean;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.ranges.RangeException;

import com.gargoylesoftware.htmlunit.html.DomElement;

/**
 * Simple implementation of {@link Range}.
 * @version $Revision$
 * @author Marc Guillemot
 */
public class SimpleRange implements Range, Serializable {

    private static final long serialVersionUID = 5779974839466976193L;

    private org.w3c.dom.Node startContainer_, endContainer_;
    private int startOffset_, endOffset_;

    /**
     * Constructs a range without any content.
     */
    public SimpleRange() {
        // nothing
    }

    /**
     * Constructs a range for the provided element.
     * @param domElement the element for the range
     */
    public SimpleRange(final DomElement domElement) {
        startContainer_ = domElement;
        endContainer_ = domElement;
        startOffset_ = 0;
        endOffset_ = domElement.getTextContent().length();
    }

    private SimpleRange(final SimpleRange other) {
        startContainer_ = other.getStartContainer();
        endContainer_ = other.getEndContainer();
        startOffset_ = other.getStartOffset();
        endOffset_ = other.getEndOffset();
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
        return new SimpleRange(this);
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
    public DocumentFragment extractContents() throws DOMException {
        throw new RuntimeException("Not implemented!");
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
    public org.w3c.dom.Node getCommonAncestorContainer() throws DOMException {
        if (startContainer_ != null && endContainer_ != null) {
            for (org.w3c.dom.Node p1 = startContainer_; p1 != null; p1 = p1.getParentNode()) {
                for (org.w3c.dom.Node p2 = endContainer_; p2 != null; p2 = p2.getParentNode()) {
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
    public org.w3c.dom.Node getEndContainer() throws DOMException {
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
    public org.w3c.dom.Node getStartContainer() throws DOMException {
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
    public void insertNode(final org.w3c.dom.Node newNode) throws DOMException, RangeException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void selectNode(final org.w3c.dom.Node refNode) throws RangeException, DOMException {
        startContainer_ = refNode;
        startOffset_ = 0;
        endContainer_ = refNode;
        endOffset_ = refNode.getTextContent().length();
    }

    /**
     * {@inheritDoc}
     */
    public void selectNodeContents(final org.w3c.dom.Node refNode) throws RangeException, DOMException {
        startContainer_ = refNode.getFirstChild();
        startOffset_ = 0;
        endContainer_ = refNode.getLastChild();
        endOffset_ = refNode.getLastChild().getTextContent().length();
    }

    /**
     * {@inheritDoc}
     */
    public void setEnd(final org.w3c.dom.Node refNode, final int offset) throws RangeException, DOMException {
        endContainer_ = refNode;
        endOffset_ = offset;
    }

    /**
     * {@inheritDoc}
     */
    public void setEndAfter(final org.w3c.dom.Node refNode) throws RangeException, DOMException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void setEndBefore(final org.w3c.dom.Node refNode) throws RangeException, DOMException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void setStart(final org.w3c.dom.Node refNode, final int offset) throws RangeException, DOMException {
        startContainer_ = refNode;
        startOffset_ = offset;
    }

    /**
     * {@inheritDoc}
     */
    public void setStartAfter(final org.w3c.dom.Node refNode) throws RangeException, DOMException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void setStartBefore(final org.w3c.dom.Node refNode) throws RangeException, DOMException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void surroundContents(final org.w3c.dom.Node newParent) throws DOMException, RangeException {
        throw new RuntimeException("Not implemented!");
    }

    /**
     * Provides information of the text representation of the range.
     * @return a text representation
     */
    @Override
    public String toString() {
        final org.w3c.dom.Node ancestor = getCommonAncestorContainer();
        final StringBuilder sb = new StringBuilder();
        if (ancestor != null) {
            getText(ancestor, sb, new MutableBoolean(false));
        }
        return sb.toString();
    }

    private boolean getText(final org.w3c.dom.Node node, final StringBuilder sb, final MutableBoolean started) {
        final NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final org.w3c.dom.Node child = children.item(i);
            if (started.booleanValue()) {
                if (child == endContainer_) {
                    // We're finished getting text.
                    sb.append(endContainer_.getTextContent().substring(0, endOffset_));
                    return true;
                }
                // We're in the middle of getting text.
                if (child.hasChildNodes()) {
                    final boolean stop = getText(child, sb, started);
                    if (stop) {
                        return true;
                    }
                }
                else {
                    sb.append(child.getTextContent());
                }
            }
            else {
                started.setValue(child == startContainer_);
                if (started.booleanValue()) {
                    // We're starting to get text.
                    sb.append(startContainer_.getTextContent().substring(startOffset_));
                }
                else {
                    // We're still haven't started getting text.
                    final boolean stop = getText(child, sb, started);
                    if (stop) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
