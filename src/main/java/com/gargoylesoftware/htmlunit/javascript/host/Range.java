/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.commons.collections.ListUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * The JavaScript object that represents a Range.
 *
 * @see <a href="http://www.xulplanet.com/references/objref/Range.html">XULPlanet</a>
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Traversal-Range/ranges.html">
 * DOM-Level-2-Traversal-Range</a>
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class Range extends SimpleScriptable {
    private Node startContainer_, endContainer_;
    private int startOffset_, endOffset_;

    /**
     * Creates a new instance.
     */
    public Range() {
        // Empty.
    }

    /**
     * Creates a new instance.
     * @param document the HTML document creating the range
     */
    public Range(final HTMLDocument document) {
        startContainer_ = document;
        endContainer_ = document;
    }

    Range(final org.w3c.dom.ranges.Range w3cRange) {
        final DomNode domNodeStartContainer = (DomNode) w3cRange.getStartContainer();
        startContainer_ = (Node) (domNodeStartContainer).getScriptObject();
        startOffset_ = w3cRange.getStartOffset();

        final DomNode domNodeEndContainer = (DomNode) w3cRange.getEndContainer();
        endContainer_ = (Node) (domNodeEndContainer).getScriptObject();
        endOffset_ = w3cRange.getEndOffset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class< ? > hint) {
        return toW3C().toString();
    }

    /**
     * Gets the node within which the Range begins.
     * @return <code>undefined</code> if not initialized
     */
    public Object jsxGet_startContainer() {
        if (startContainer_ == null) {
            return Context.getUndefinedValue();
        }
        return startContainer_;
    }

    /**
     * Gets the node within which the Range ends.
     * @return <code>undefined</code> if not initialized
     */
    public Object jsxGet_endContainer() {
        if (endContainer_ == null) {
            return Context.getUndefinedValue();
        }
        return endContainer_;
    }

    /**
     * Gets the offset within the starting node of the Range.
     * @return <code>0</code> if not initialized
     */
    public int jsxGet_startOffset() {
        return startOffset_;
    }

    /**
     * Gets the offset within the end node of the Range.
     * @return <code>0</code> if not initialized
     */
    public int jsxGet_endOffset() {
        return endOffset_;
    }

    /**
     * Sets the attributes describing the start of a Range.
     * @param refNode the reference node
     * @param offset the offset value within the node
     */
    public void jsxFunction_setStart(final Node refNode, final int offset) {
        if (refNode == null) {
            throw Context.reportRuntimeError("It is illegal to call Range.setStart() with a null node.");
        }
        startContainer_ = refNode;
        startOffset_ = offset;
    }

    /**
     * Sets the start of the range to be after the node.
     * @param refNode the reference node
     */
    public void jsxFunction_setStartAfter(final Node refNode) {
        if (refNode == null) {
            throw Context.reportRuntimeError("It is illegal to call Range.setStartAfter() with a null node.");
        }
        startContainer_ = refNode.getParent();
        startOffset_ = getPositionInContainer(refNode) + 1;
    }

    /**
     * Sets the start of the range to be before the node.
     * @param refNode the reference node
     */
    public void jsxFunction_setStartBefore(final Node refNode) {
        if (refNode == null) {
            throw Context.reportRuntimeError("It is illegal to call Range.setStartBefore() with a null node.");
        }
        startContainer_ = refNode.getParent();
        startOffset_ = getPositionInContainer(refNode);
    }

    private int getPositionInContainer(final Node refNode) {
        int i = 0;
        Node node = refNode;
        while (node.jsxGet_previousSibling() != null) {
            node = node.jsxGet_previousSibling();
            ++i;
        }
        return i;
    }

    /**
     * Indicates if the range is collapsed.
     * @return <code>true</code> if the range is collapsed
     */
    public boolean jsxGet_collapsed() {
        return (startContainer_ == endContainer_ && startOffset_ == endOffset_);
    }

    /**
     * Sets the attributes describing the end of a Range.
     * @param refNode the reference node
     * @param offset the offset value within the node
     */
    public void jsxFunction_setEnd(final Node refNode, final int offset) {
        if (refNode == null) {
            throw Context.reportRuntimeError("It is illegal to call Range.setEnd() with a null node.");
        }
        endContainer_ = refNode;
        endOffset_ = offset;
    }

    /**
     * Sets the end of the range to be after the node.
     * @param refNode the reference node
     */
    public void jsxFunction_setEndAfter(final Node refNode) {
        if (refNode == null) {
            throw Context.reportRuntimeError("It is illegal to call Range.setEndAfter() with a null node.");
        }
        endContainer_ = refNode.getParent();
        endOffset_ = getPositionInContainer(refNode) + 1;
    }

    /**
     * Sets the end of the range to be before the node.
     * @param refNode the reference node
     */
    public void jsxFunction_setEndBefore(final Node refNode) {
        if (refNode == null) {
            throw Context.reportRuntimeError("It is illegal to call Range.setEndBefore() with a null node.");
        }
        startContainer_ = refNode.getParent();
        startOffset_ = getPositionInContainer(refNode);
    }

    /**
     * Select the contents within a node.
     * @param refNode Node to select from
     */
    public void jsxFunction_selectNodeContents(final Node refNode) {
        startContainer_ = refNode;
        startOffset_ = 0;
        endContainer_ = refNode;
        endOffset_ = refNode.jsxGet_childNodes().jsxGet_length();
    }

    /**
     * Selects a node and its contents.
     * @param refNode the node to select
     */
    public void jsxFunction_selectNode(final Node refNode) {
        jsxFunction_setStartBefore(refNode);
        jsxFunction_setEndAfter(refNode);
    }

    /**
     * Collapse a Range onto one of its boundaries.
     * @param toStart if <code>true</code>, collapses the Range onto its start; else collapses it onto its end
     */
    public void jsxFunction_collapse(final boolean toStart) {
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
     * Returns the deepest common ancestor container of the Range's two boundary points.
     * @return the deepest common ancestor container of the Range's two boundary points
     */
    public Object jsxGet_commonAncestorContainer() {
        final Node ancestor = getCommonAncestor();
        if (ancestor == null) {
            return Context.getUndefinedValue();
        }
        return ancestor;
    }

    /**
     * Returns the deepest common ancestor container of the Range's two boundary points.
     * @return the deepest common ancestor container of the Range's two boundary points
     */
    @SuppressWarnings("unchecked")
    private Node getCommonAncestor() {
        final List<Node> startAncestors = getAncestorsAndSelf(startContainer_);
        final List<Node> endAncestors = getAncestorsAndSelf(endContainer_);
        final List<Node> commonAncestors = ListUtils.intersection(startAncestors, endAncestors);
        if (commonAncestors.isEmpty()) {
            return null;
        }
        return commonAncestors.get(commonAncestors.size() - 1);
    }

    /**
     * Returns the ancestors of the specified node.
     * @param node the node to start with
     * @return the ancestors of the specified node
     */
    private List<Node> getAncestorsAndSelf(final Node node) {
        final List<Node> ancestors = new ArrayList<Node>();
        Node ancestor = node;
        while (ancestor != null) {
            ancestors.add(0, ancestor);
            ancestor = ancestor.getParent();
        }
        return ancestors;
    }

    /**
     * Parses an HTML snippet.
     * @param valueAsString text that contains text and tags to be converted to a document fragment
     * @return a document fragment
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:range.createContextualFragment">Mozilla documentation</a>
     */
    public Object jsxFunction_createContextualFragment(final String valueAsString) {
        final SgmlPage page = startContainer_.<DomNode>getDomNodeOrDie().getPage();
        final DomDocumentFragment fragment = new DomDocumentFragment(page);
        HTMLElement.parseHtmlSnippet(fragment, true, valueAsString);
        return fragment.getScriptObject();
    }

    /**
     * Moves this range's contents from the document tree into a document fragment.
     * @return the new document fragment containing the range contents
     */
    public Object jsxFunction_extractContents() {
        return toW3C().extractContents().getScriptObject();
    }

    /**
     * Returns a W3C {@link org.w3c.dom.ranges.Range} version of this object.
     * @return a W3C {@link org.w3c.dom.ranges.Range} version of this object
     */
    public SimpleRange toW3C() {
        return new SimpleRange(startContainer_.getDomNodeOrNull(), startOffset_,
            endContainer_.getDomNodeOrDie(), endOffset_);
    }

}
