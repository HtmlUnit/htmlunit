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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.mozilla.javascript.Context;

import com.gargoylesoftware.htmlunit.javascript.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * The javascript object that represents a Range.
 * @see <a href="http://www.xulplanet.com/references/objref/Range.html">XULPlanet</a>
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Traversal-Range/ranges.html">
 * DOM-Level-2-Traversal-Range</a>
 * @version $Revision: 1615 $
 * @author Marc Guillemot
 */
public class Range extends SimpleScriptable {
    private NodeImpl startContainer_, endContainer_;
    private int startOffset_, endOffset_;

    /**
     * Create an instance.
     */
    public Range() {
    }

    /**
     * Gets the node within which the Range begins.
     * @return <code>undefined</code> if not initialized
     */
    public Object jsxGet_startContainer() {
        if (startContainer_ == null) {
            return Context.getUndefinedValue();
        }
        else {
            return startContainer_;
        }
    }

    /**
     * Gets the node within which the Range ends.
     * @return <code>undefined</code> if not initialized
     */
    public Object jsxGet_endContainer() {
        if (endContainer_ == null) {
            return Context.getUndefinedValue();
        }
        else {
            return endContainer_;
        }
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
    public void jsxFunction_setStart(final NodeImpl refNode, final int offset) {
        startContainer_ = refNode;
        startOffset_ = offset;
    }

    /**
     * Sets the start of the range to be after the node
     * @param refNode the reference node
     */
    public void jsxFunction_setStartAfter(final NodeImpl refNode) {
        startContainer_ = (NodeImpl) refNode.jsxGet_parentNode();
        startOffset_ = getPositionInContainer(refNode) + 1;
    }

    /**
     * Sets the start of the range to be before the node
     * @param refNode the reference node
     */
    public void jsxFunction_setStartBefore(final NodeImpl refNode) {
        startContainer_ = (NodeImpl) refNode.jsxGet_parentNode();
        startOffset_ = getPositionInContainer(refNode);
    }

    private int getPositionInContainer(final NodeImpl refNode) {
        int i = 0;
        NodeImpl node = refNode;
        while (node.jsxGet_previousSibling() != null) {
            node = (NodeImpl) node.jsxGet_previousSibling();
            ++i;
        }
        return i;
    }

    /**
     * Indicates if the range is collapsed
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
    public void jsxFunction_setEnd(final NodeImpl refNode, final int offset) {
        endContainer_ = refNode;
        endOffset_ = offset;
    }

    /**
     * Sets the end of the range to be after the node
     * @param refNode the reference node
     */
    public void jsxFunction_setEndAfter(final NodeImpl refNode) {
        endContainer_ = (NodeImpl) refNode.jsxGet_parentNode();
        endOffset_ = getPositionInContainer(refNode) + 1;
    }

    /**
     * Sets the end of the range to be before the node
     * @param refNode the reference node
     */
    public void jsxFunction_setEndBefore(final NodeImpl refNode) {
        startContainer_ = (NodeImpl) refNode.jsxGet_parentNode();
        startOffset_ = getPositionInContainer(refNode);
    }

    /**
     * Select the contents within a node
     * @param refNode Node to select from
     */
    public void jsxFunction_selectNodeContents(final NodeImpl refNode) {
        startContainer_ = refNode;
        startOffset_ = 0;
        endContainer_ = refNode;
        endOffset_ = ((HTMLCollection) refNode.jsxGet_childNodes()).jsxGet_length();
    }
    
    /**
     * Select a node and its contents
     * @param refNode the node to select
     */
    public void jsxFunction_selectNode(final NodeImpl refNode) {
        jsxFunction_setStartBefore(refNode);
        jsxFunction_setEndAfter(refNode);
    }

    /**
     * Collapse a Range onto one of its boundary-points
     * @param toStart If <code>true</code>, collapses the Range onto its start; else collapses it onto its end.
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
     * Gets the deepest common ancestor container of the Range's two boundary-points.
     * @return the ancestor
     */
    public Object jsxGet_commonAncestorContainer() {
        if (startContainer_ == null) {
            return Context.getUndefinedValue();
        }

        final List startContainerAncestor = getAncestorsAndSelf(startContainer_);
        final List endContainerAncestor = getAncestorsAndSelf(endContainer_);
        
        final List commonAncestors = ListUtils.intersection(startContainerAncestor, endContainerAncestor);
        return (NodeImpl) commonAncestors.get(commonAncestors.size() - 1);
    }

    /**
     * Gets the ancestors of the node
     * @param node the node to start with
     * @return a list of node
     */
    protected List getAncestorsAndSelf(final NodeImpl node) {
        final List ancestors = new ArrayList();
        NodeImpl ancestor = (NodeImpl) node;
        while (ancestor != null) {
            ancestors.add(0, ancestor);
            ancestor = (NodeImpl) ancestor.jsxGet_parentNode();
        }
        return ancestors;
    }

    /**
     * Parses an html snippet.
     * @param fragment text that contains text and tags to be converted to a document fragment.
     * @return a document fragment
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:range.createContextualFragment">Mozilla documentation</a>
     */
    public Object jsxFunction_createContextualFragment(final String fragment) {
        getLog().warn("Range.createContextualFragment currently not implemented. Returning null");
        return null;
    }
}
