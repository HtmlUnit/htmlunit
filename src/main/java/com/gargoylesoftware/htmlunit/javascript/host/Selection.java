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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.w3c.dom.ranges.Range;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a Selection.
 *
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms535869.aspx">MSDN Documentation</a>
 * @see <a href="https://developer.mozilla.org/en/DOM/Selection">Gecko DOM Reference</a>
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class Selection extends SimpleScriptable {

    private static final long serialVersionUID = 3712755502782559551L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class< ? > hint) {
        final boolean ff = getBrowserVersion().isFirefox();
        if (ff && (String.class.equals(hint) || hint == null)) {
            return getPageSelection().toString();
        }
        return super.getDefaultValue(hint);
    }

    /**
     * Returns the node in which the selection begins.
     * @return the node in which the selection begins
     */
    public Node jsxGet_anchorNode() {
        return (Node) getScriptableNullSafe(getPageSelection().getStartContainer());
    }

    /**
     * Returns the number of characters that the selection's anchor is offset within the anchor node.
     * @return the number of characters that the selection's anchor is offset within the anchor node
     */
    public int jsxGet_anchorOffset() {
        return getPageSelection().getStartOffset();
    }

    /**
     * Returns the node in which the selection ends.
     * @return the node in which the selection ends
     */
    public Node jsxGet_focusNode() {
        return (Node) getScriptableNullSafe(getPageSelection().getEndContainer());
    }

    /**
     * Returns the number of characters that the selection's focus is offset within the focus node.
     * @return the number of characters that the selection's focus is offset within the focus node
     */
    public int jsxGet_focusOffset() {
        return getPageSelection().getEndOffset();
    }

    /**
     * Returns a boolean indicating whether the selection's start and end points are at the same position.
     * @return a boolean indicating whether the selection's start and end points are at the same position
     */
    public boolean jsxGet_isCollapsed() {
        return getPageSelection().getCollapsed();
    }

    /**
     * Returns the number of ranges in the selection.
     * @return the number of ranges in the selection
     */
    public int jsxGet_rangeCount() {
        final Range r = getPageSelection();
        final boolean valid = (r.getStartContainer() != null && r.getEndContainer() != null);
        if (valid) {
            return 1;
        }
        return 0;
    }

    /**
     * Collapses the current selection to a single point. The document is not modified.
     * @param parentNode the caret location will be within this node
     * @param offset the caret will be placed this number of characters from the beginning of the parentNode's text
     */
    public void jsxFunction_collapse(final Node parentNode, final int offset) {
        final Range selection = getPageSelection();
        selection.setStart(parentNode.getDomNodeOrDie(), offset);
        selection.setEnd(parentNode.getDomNodeOrDie(), offset);
    }

    /**
     * Moves the anchor of the selection to the same point as the focus. The focus does not move.
     */
    public void jsxFunction_collapseToEnd() {
        getPageSelection().collapse(false);
    }

    /**
     * Moves the focus of the selection to the same point at the anchor. The anchor does not move.
     */
    public void jsxFunction_collapseToStart() {
        getPageSelection().collapse(true);
    }

    /**
     * Creates a TextRange object from the current text selection.
     * @return the created TextRange object
     */
    public TextRange jsxFunction_createRange() {
        final TextRange range = new TextRange();
        range.setParentScope(getParentScope());
        range.setPrototype(getPrototype(range.getClass()));
        return range;
    }

    /**
     * Moves the focus of the selection to a specified point. The anchor of the selection does not move.
     * @param parentNode the node within which the focus will be moved
     * @param offset the number of characters from the beginning of parentNode's text the focus will be placed
     */
    public void jsxFunction_extend(final Node parentNode, final int offset) {
        final Range selection = getPageSelection();
        selection.setEnd(parentNode.getDomNodeOrDie(), offset);
    }

    /**
     * Adds all the children of the specified node to the selection. The previous selection is lost.
     * @param parentNode all children of parentNode will be selected; parentNode itself is not part of the selection
     */
    public void jsxFunction_selectAllChildren(final Node parentNode) {
        final Range selection = getPageSelection();
        selection.selectNodeContents(parentNode.getDomNodeOrDie());
    }

    /**
     * Returns the current HtmlUnit DOM selection range.
     * @return the current HtmlUnit DOM selection range
     */
    private Range getPageSelection() {
        final HtmlPage page = (HtmlPage) getWindow().getDomNodeOrDie();
        return page.getSelection();
    }

    /**
     * Returns the scriptable object corresponding to the specified HtmlUnit DOM object.
     * @param object the HtmlUnit DOM object whose scriptable object is to be returned (may be <tt>null</tt>)
     * @return the scriptable object corresponding to the specified HtmlUnit DOM object, or <tt>null</tt> if
     *         <tt>object</tt> was <tt>null</tt>
     */
    private SimpleScriptable getScriptableNullSafe(final Object object) {
        final SimpleScriptable scriptable;
        if (object != null) {
            scriptable = getScriptableFor(object);
        }
        else {
            scriptable = null;
        }
        return scriptable;
    }

}
