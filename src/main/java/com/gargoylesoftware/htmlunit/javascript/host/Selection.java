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

import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.w3c.dom.ranges.Range;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;
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

    private String type_ = "None";

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class< ? > hint) {
        final boolean ff = getBrowserVersion().isFirefox();
        if (ff && (String.class.equals(hint) || hint == null)) {
            final StringBuilder sb = new StringBuilder();
            for (Range r : getRanges()) {
                sb.append(r.toString());
            }
            return sb.toString();
        }
        return super.getDefaultValue(hint);
    }

    /**
     * Returns the node in which the selection begins.
     * @return the node in which the selection begins
     */
    public Node jsxGet_anchorNode() {
        final Range last = getLastRange();
        if (last == null) {
            return null;
        }
        return (Node) getScriptableNullSafe(last.getStartContainer());
    }

    /**
     * Returns the number of characters that the selection's anchor is offset within the anchor node.
     * @return the number of characters that the selection's anchor is offset within the anchor node
     */
    public int jsxGet_anchorOffset() {
        final Range last = getLastRange();
        if (last == null) {
            return 0;
        }
        return last.getStartOffset();
    }

    /**
     * Returns the node in which the selection ends.
     * @return the node in which the selection ends
     */
    public Node jsxGet_focusNode() {
        final Range last = getLastRange();
        if (last == null) {
            return null;
        }
        return (Node) getScriptableNullSafe(last.getEndContainer());
    }

    /**
     * Returns the number of characters that the selection's focus is offset within the focus node.
     * @return the number of characters that the selection's focus is offset within the focus node
     */
    public int jsxGet_focusOffset() {
        final Range last = getLastRange();
        if (last == null) {
            return 0;
        }
        return last.getEndOffset();
    }

    /**
     * Returns a boolean indicating whether the selection's start and end points are at the same position.
     * @return a boolean indicating whether the selection's start and end points are at the same position
     */
    public boolean jsxGet_isCollapsed() {
        final List<Range> ranges = getRanges();
        return (ranges.isEmpty() || (ranges.size() == 1 && ranges.get(0).getCollapsed()));
    }

    /**
     * Returns the number of ranges in the selection.
     * @return the number of ranges in the selection
     */
    public int jsxGet_rangeCount() {
        return getRanges().size();
    }

    /**
     * Returns the type of selection (IE only).
     * @return the type of selection
     */
    public String jsxGet_type() {
        return type_;
    }

    /**
     * Creates a TextRange object from the current text selection (IE only).
     * @return the created TextRange object
     */
    public TextRange jsxFunction_createRange() {
        final TextRange range;
        final Range first = getFirstRange();
        if (first != null) {
            range = new TextRange(first);
        }
        else {
            range = new TextRange(new SimpleRange());
        }
        range.setParentScope(getParentScope());
        range.setPrototype(getPrototype(range.getClass()));
        return range;
    }

    /**
     * Adds a range to the selection.
     * @param range the range to add
     */
    public void jsxFunction_addRange(final com.gargoylesoftware.htmlunit.javascript.host.Range range) {
        getRanges().add(range.toW3C());
    }

    /**
     * Removes a range from the selection.
     * @param range the range to remove
     */
    public void jsxFunction_removeRange(final com.gargoylesoftware.htmlunit.javascript.host.Range range) {
        getRanges().remove(range.toW3C());
    }

    /**
     * Removes all ranges from the selection.
     */
    public void jsxFunction_removeAllRanges() {
        getRanges().clear();
    }

    /**
     * Returns the range at the specified index.
     *
     * @param index the index of the range to return
     * @return the range at the specified index
     */
    public Range jsxFunction_getRangeAt(final int index) {
        final List<Range> ranges = getRanges();
        if (index < 0 || index >= ranges.size()) {
            throw Context.reportRuntimeError("Invalid range index: " + index);
        }
        return ranges.get(index);
    }

    /**
     * Collapses the current selection to a single point. The document is not modified.
     * @param parentNode the caret location will be within this node
     * @param offset the caret will be placed this number of characters from the beginning of the parentNode's text
     */
    public void jsxFunction_collapse(final Node parentNode, final int offset) {
        final List<Range> ranges = getRanges();
        ranges.clear();
        ranges.add(new SimpleRange(parentNode.getDomNodeOrDie(), offset));
    }

    /**
     * Moves the anchor of the selection to the same point as the focus. The focus does not move.
     */
    public void jsxFunction_collapseToEnd() {
        final Range last = getLastRange();
        if (last != null) {
            final List<Range> ranges = getRanges();
            ranges.clear();
            ranges.add(last);
            last.collapse(false);
        }
    }

    /**
     * Moves the focus of the selection to the same point at the anchor. The anchor does not move.
     */
    public void jsxFunction_collapseToStart() {
        final Range first = getFirstRange();
        if (first != null) {
            final List<Range> ranges = getRanges();
            ranges.clear();
            ranges.add(first);
            first.collapse(true);
        }
    }

    /**
     * Cancels the current selection, sets the selection type to none, and sets the item property to null (IE only).
     */
    public void jsxFunction_empty() {
        type_ = "None";
    }

    /**
     * Moves the focus of the selection to a specified point. The anchor of the selection does not move.
     * @param parentNode the node within which the focus will be moved
     * @param offset the number of characters from the beginning of parentNode's text the focus will be placed
     */
    public void jsxFunction_extend(final Node parentNode, final int offset) {
        final Range last = getLastRange();
        if (last != null) {
            last.setEnd(parentNode.getDomNodeOrDie(), offset);
        }
    }

    /**
     * Adds all the children of the specified node to the selection. The previous selection is lost.
     * @param parentNode all children of parentNode will be selected; parentNode itself is not part of the selection
     */
    public void jsxFunction_selectAllChildren(final Node parentNode) {
        final List<Range> ranges = getRanges();
        ranges.clear();
        ranges.add(new SimpleRange(parentNode.getDomNodeOrDie()));
    }

    /**
     * Returns the current HtmlUnit DOM selection ranges.
     * @return the current HtmlUnit DOM selection ranges
     */
    private List<Range> getRanges() {
        final HtmlPage page = (HtmlPage) getWindow().getDomNodeOrDie();
        return page.getSelectionRanges();
    }

    /**
     * Returns the first selection range in the current document, by document position.
     * @return the first selection range in the current document, by document position
     */
    private Range getFirstRange() {
        Range first = null;
        for (final Range range : getRanges()) {
            if (first == null) {
                first = range;
            }
            else {
                final org.w3c.dom.Node firstStart = first.getStartContainer();
                final org.w3c.dom.Node rangeStart = range.getStartContainer();
                if ((firstStart.compareDocumentPosition(rangeStart) & Node.DOCUMENT_POSITION_PRECEDING) != 0) {
                    first = range;
                }
            }
        }
        return first;
    }

    /**
     * Returns the last selection range in the current document, by document position.
     * @return the last selection range in the current document, by document position
     */
    private Range getLastRange() {
        Range last = null;
        for (final Range range : getRanges()) {
            if (last == null) {
                last = range;
            }
            else {
                final org.w3c.dom.Node lastStart = last.getStartContainer();
                final org.w3c.dom.Node rangeStart = range.getStartContainer();
                if ((lastStart.compareDocumentPosition(rangeStart) & Node.DOCUMENT_POSITION_FOLLOWING) != 0) {
                    last = range;
                }
            }
        }
        return last;
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
