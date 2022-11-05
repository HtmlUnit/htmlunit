/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ranges.Range;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * A JavaScript object for {@code Selection}.
 *
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms535869.aspx">MSDN Documentation</a>
 * @see <a href="https://developer.mozilla.org/en/DOM/Selection">Gecko DOM Reference</a>
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass
public class Selection extends HtmlUnitScriptable {
    private static final String TYPE_NONE = "None";
    private static final String TYPE_CARET = "Caret";
    private static final String TYPE_RANGE = "Range";

    private String type_ = TYPE_NONE;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public Selection() {
    }

    /**
     * @return a string currently being represented by the selection object,
     * i.e. the currently selected text.
     */
    @JsxFunction(functionName = "toString")
    public String jsToString() {
        final StringBuilder sb = new StringBuilder();
        for (final Range r : getRanges()) {
            sb.append(r.toString());
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (getPrototype() != null && (String.class.equals(hint) || hint == null)) {
            return jsToString();
        }
        return super.getDefaultValue(hint);
    }

    /**
     * Returns the node in which the selection begins.
     * @return the node in which the selection begins
     */
    @JsxGetter
    public Node getAnchorNode() {
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
    @JsxGetter
    public int getAnchorOffset() {
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
    @JsxGetter
    public Node getFocusNode() {
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
    @JsxGetter
    public int getFocusOffset() {
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
    @JsxGetter
    public boolean isIsCollapsed() {
        final List<Range> ranges = getRanges();
        return ranges.isEmpty() || (ranges.size() == 1 && ranges.get(0).getCollapsed());
    }

    /**
     * Returns the number of ranges in the selection.
     * @return the number of ranges in the selection
     */
    @JsxGetter
    public int getRangeCount() {
        return getRanges().size();
    }

    /**
     * Returns the type of selection (IE only).
     * @return the type of selection
     */
    @JsxGetter({CHROME, EDGE, FF, FF_ESR})
    public String getType() {
        return type_;
    }

    /**
     * Adds a range to the selection.
     * @param range the range to add
     */
    @JsxFunction
    public void addRange(final com.gargoylesoftware.htmlunit.javascript.host.dom.Range range) {
        final SimpleRange rg = range.toW3C();
        getRanges().add(rg);

        if (TYPE_CARET.equals(type_) && rg.getCollapsed()) {
            return;
        }
        type_ = TYPE_RANGE;
    }

    /**
     * Removes a range from the selection.
     * @param range the range to remove
     */
    @JsxFunction
    public void removeRange(final com.gargoylesoftware.htmlunit.javascript.host.dom.Range range) {
        getRanges().remove(range.toW3C());

        if (getRangeCount() < 1) {
            type_ = TYPE_NONE;
        }
    }

    /**
     * Removes all ranges from the selection.
     */
    @JsxFunction
    public void removeAllRanges() {
        getRanges().clear();

        type_ = TYPE_NONE;
    }

    /**
     * Returns the range at the specified index.
     *
     * @param index the index of the range to return
     * @return the range at the specified index
     */
    @JsxFunction
    public com.gargoylesoftware.htmlunit.javascript.host.dom.Range getRangeAt(final int index) {
        final List<Range> ranges = getRanges();
        if (index < 0 || index >= ranges.size()) {
            throw Context.reportRuntimeError("Invalid range index: " + index);
        }
        final Range range = ranges.get(index);
        final com.gargoylesoftware.htmlunit.javascript.host.dom.Range jsRange =
            new com.gargoylesoftware.htmlunit.javascript.host.dom.Range(range);
        jsRange.setParentScope(getWindow());
        jsRange.setPrototype(getPrototype(com.gargoylesoftware.htmlunit.javascript.host.dom.Range.class));

        return jsRange;
    }

    /**
     * Collapses the current selection to a single point. The document is not modified.
     * @param parentNode the caret location will be within this node
     * @param offset the caret will be placed this number of characters from the beginning of the parentNode's text
     */
    @JsxFunction
    public void collapse(final Node parentNode, final int offset) {
        final List<Range> ranges = getRanges();
        ranges.clear();
        ranges.add(new SimpleRange(parentNode.getDomNodeOrDie(), offset));

        type_ = TYPE_CARET;
    }

    /**
     * Moves the anchor of the selection to the same point as the focus. The focus does not move.
     */
    @JsxFunction
    public void collapseToEnd() {
        final Range last = getLastRange();
        if (last != null) {
            final List<Range> ranges = getRanges();
            ranges.clear();
            ranges.add(last);
            last.collapse(false);
        }

        type_ = TYPE_CARET;
    }

    /**
     * Moves the focus of the selection to the same point at the anchor. The anchor does not move.
     */
    @JsxFunction
    public void collapseToStart() {
        final Range first = getFirstRange();
        if (first != null) {
            final List<Range> ranges = getRanges();
            ranges.clear();
            ranges.add(first);
            first.collapse(true);
        }

        type_ = TYPE_CARET;
    }

    /**
     * Cancels the current selection, sets the selection type to none.
     */
    @JsxFunction({CHROME, EDGE, FF, FF_ESR})
    public void empty() {
        removeAllRanges();
    }

    /**
     * Moves the focus of the selection to a specified point. The anchor of the selection does not move.
     * @param parentNode the node within which the focus will be moved
     * @param offset the number of characters from the beginning of parentNode's text the focus will be placed
     */
    @JsxFunction({CHROME, EDGE, FF, FF_ESR})
    public void extend(final Node parentNode, final int offset) {
        final Range last = getLastRange();
        if (last != null) {
            last.setEnd(parentNode.getDomNodeOrDie(), offset);

            type_ = TYPE_RANGE;
        }
    }

    /**
     * Adds all the children of the specified node to the selection. The previous selection is lost.
     * @param parentNode all children of parentNode will be selected; parentNode itself is not part of the selection
     */
    @JsxFunction
    public void selectAllChildren(final Node parentNode) {
        final List<Range> ranges = getRanges();
        ranges.clear();
        final SimpleRange rg = new SimpleRange(parentNode.getDomNodeOrDie());
        ranges.add(rg);

        if (rg.getCollapsed()) {
            type_ = TYPE_CARET;
        }
        else {
            type_ = TYPE_RANGE;
        }
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
        // avoid concurrent modification exception
        final List<Range> ranges = new ArrayList<>(getRanges());

        Range first = null;
        for (final Range range : ranges) {
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
        // avoid concurrent modification exception
        final List<Range> ranges = new ArrayList<>(getRanges());

        Range last = null;
        for (final Range range : ranges) {
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
     * @param object the HtmlUnit DOM object whose scriptable object is to be returned (may be {@code null})
     * @return the scriptable object corresponding to the specified HtmlUnit DOM object, or {@code null} if
     *         <code>object</code> was {@code null}
     */
    private HtmlUnitScriptable getScriptableNullSafe(final Object object) {
        final HtmlUnitScriptable scriptable;
        if (object != null) {
            scriptable = getScriptableFor(object);
        }
        else {
            scriptable = null;
        }
        return scriptable;
    }
}
