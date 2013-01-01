/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.ranges.Range;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * A JavaScript object for a TextRange (IE only).
 *
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms535872.aspx">MSDN documentation (1)</a>
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms533042.aspx">MSDN documentation (2)</a>
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author David Gileadi
 */
@JsxClass(browsers = @WebBrowser(IE))
public class TextRange extends SimpleScriptable {

    private static final Log LOG = LogFactory.getLog(TextRange.class);

    /** The wrapped selection range. */
    private Range range_;

    /**
     * Default constructor used to build the prototype.
     */
    public TextRange() {
        // Empty.
    }

    /**
     * Constructs a text range around the provided element.
     * @param elt the element to wrap
     */
    public TextRange(final HTMLElement elt) {
        range_ = new SimpleRange(elt.getDomNodeOrDie());
    }

    /**
     * Constructs a text range around the provided range.
     * @param range the initial range
     */
    public TextRange(final Range range) {
        range_ = range.cloneRange();
    }

    /**
     * Retrieves the text contained within the range.
     * @return the text contained within the range
     */
    @JsxGetter
    public String getText() {
        return range_.toString();
    }

    /**
     * Sets the text contained within the range.
     * @param text the text contained within the range
     */
    @JsxSetter
    public void setText(final String text) {
        if (range_.getStartContainer() == range_.getEndContainer()
            && range_.getStartContainer() instanceof SelectableTextInput) {
            final SelectableTextInput input = (SelectableTextInput) range_.getStartContainer();
            final String oldValue = input.getText();
            input.setText(oldValue.substring(0, input.getSelectionStart()) + text
                + oldValue.substring(input.getSelectionEnd()));
        }
    }

    /**
     * Retrieves the HTML fragment contained within the range.
     * @return the HTML fragment contained within the range
     */
    @JsxGetter
    public String getHtmlText() {
        final org.w3c.dom.Node node = range_.getCommonAncestorContainer();
        if (null == node) {
            return "";
        }
        final HTMLElement element = (HTMLElement) getScriptableFor(node);
        return element.getOuterHTML(); // TODO: not quite right, but good enough for now
    }

    /**
     * Duplicates this TextRange instance.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536416.aspx">MSDN documentation</a>
     * @return a duplicate of this TextRange instance
     */
    @JsxFunction
    public Object duplicate() {
        final TextRange range = new TextRange(range_.cloneRange());
        range.setParentScope(getParentScope());
        range.setPrototype(getPrototype());
        return range;
    }

    /**
     * Retrieves the parent element for the given text range.
     * The parent element is the element that completely encloses the text in the range.
     * If the text range spans text in more than one element, this method returns the smallest element that encloses
     * all the elements. When you insert text into a range that spans multiple elements, the text is placed in the
     * parent element rather than in any of the contained elements.
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536654.aspx">MSDN doc</a>
     * @return the parent element object if successful, or null otherwise.
     */
    @JsxFunction
    public Object parentElement() {
        final org.w3c.dom.Node parent = range_.getCommonAncestorContainer();
        if (null == parent) {
            if (null == range_.getStartContainer() || null == range_.getEndContainer()) {
                try {
                    final Window window = (Window) getParentScope();
                    final HtmlPage page = window.getDomNodeOrDie();
                    return page.getBody();
                }
                catch (final Exception e) {
                    // ok bad luck
                }
            }
            return null;
        }
        return getScriptableFor(parent);
    }

    /**
     * Collapses the range.
     * @param toStart indicates if collapse should be done to the start
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536371.aspx">MSDN doc</a>
     */
    @JsxFunction
    public void collapse(final boolean toStart) {
        range_.collapse(toStart);
    }

    /**
     * Makes the current range the active selection.
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536735.aspx">MSDN doc</a>
     */
    @JsxFunction
    public void select() {
        final HtmlPage page = (HtmlPage) getWindow().getDomNodeOrDie();
        page.setSelectionRange(range_);
    }

    /**
     * Changes the start position of the range.
     * @param unit specifies the units to move
     * @param count the number of units to move
     * @return the number of units moved
     */
    @JsxFunction
    public int moveStart(final String unit, final Object count) {
        if (!"character".equals(unit)) {
            LOG.warn("moveStart('" + unit + "') is not yet supported");
            return 0;
        }
        int c = 1;
        if (count != Undefined.instance) {
            c = (int) Context.toNumber(count);
        }
        if (range_.getStartContainer() == range_.getEndContainer()
            && range_.getStartContainer() instanceof SelectableTextInput) {
            final SelectableTextInput input = (SelectableTextInput) range_.getStartContainer();
            c = constrainMoveBy(c, range_.getStartOffset(), input.getText().length());
            range_.setStart(input, range_.getStartOffset() + c);
        }
        return c;
    }

    /**
     * Changes the end position of the range.
     * @param unit specifies the units to move
     * @param count the number of units to move
     * @return the number of units moved
     */
    @JsxFunction
    public int moveEnd(final String unit, final Object count) {
        if (!"character".equals(unit)) {
            LOG.warn("moveEnd('" + unit + "') is not yet supported");
            return 0;
        }
        int c = 1;
        if (count != Undefined.instance) {
            c = (int) Context.toNumber(count);
        }
        if (range_.getStartContainer() == range_.getEndContainer()
            && range_.getStartContainer() instanceof SelectableTextInput) {
            final SelectableTextInput input = (SelectableTextInput) range_.getStartContainer();
            c = constrainMoveBy(c, range_.getEndOffset(), input.getText().length());
            range_.setEnd(input, range_.getEndOffset() + c);
        }
        return c;
    }

    /**
     * Moves the text range so that the start and end positions of the range encompass
     * the text in the specified element.
     * @param element the element to move to
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536630.aspx">MSDN Documentation</a>
     */
    @JsxFunction
    public void moveToElementText(final HTMLElement element) {
        range_.selectNode(element.getDomNodeOrDie());
    }

    /**
     * Indicates if a range is contained in current one.
     * @param other the other range
     * @return <code>true</code> if <code>other</code> is contained within current range
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536371.aspx">MSDN doc</a>
     */
    @JsxFunction
    public boolean inRange(final TextRange other) {
        final Range otherRange = other.range_;

        final org.w3c.dom.Node start = range_.getStartContainer();
        final org.w3c.dom.Node otherStart = otherRange.getStartContainer();
        if (otherStart == null) {
            return false;
        }
        final short startComparison = start.compareDocumentPosition(otherStart);
        final boolean startNodeBefore = startComparison == 0
            || (startComparison & Node.DOCUMENT_POSITION_CONTAINS) != 0
            || (startComparison & Node.DOCUMENT_POSITION_PRECEDING) != 0;
        if (startNodeBefore && (start != otherStart || range_.getStartOffset() <= otherRange.getStartOffset())) {
            final org.w3c.dom.Node end = range_.getEndContainer();
            final org.w3c.dom.Node otherEnd = otherRange.getEndContainer();
            final short endComparison = end.compareDocumentPosition(otherEnd);
            final boolean endNodeAfter = endComparison == 0
                || (endComparison & Node.DOCUMENT_POSITION_CONTAINS) != 0
                || (endComparison & Node.DOCUMENT_POSITION_FOLLOWING) != 0;
            if (endNodeAfter && (end != otherEnd || range_.getEndOffset() >= otherRange.getEndOffset())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Sets the endpoint of the range based on the endpoint of another range..
     * @param type end point transfer type. One of "StartToEnd", "StartToStart", "EndToStart" and "EndToEnd"
     * @param other the other range
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536745.aspx">MSDN doc</a>
     */
    @JsxFunction
    public void setEndPoint(final String type, final TextRange other) {
        final Range otherRange = other.range_;

        final org.w3c.dom.Node target;
        final int offset;
        if (type.endsWith("ToStart")) {
            target = otherRange.getStartContainer();
            offset = otherRange.getStartOffset();
        }
        else {
            target = otherRange.getEndContainer();
            offset = otherRange.getEndOffset();
        }

        if (type.startsWith("Start")) {
            range_.setStart(target, offset);
        }
        else {
            range_.setEnd(target, offset);
        }
    }

    /**
     * Constrain the given amount to move the range by to the limits of the given current offset and text length.
     * @param moveBy the amount to move by
     * @param current the current index
     * @param textLength the text length
     * @return the moveBy amount constrained to the text length
     */
    protected int constrainMoveBy(int moveBy, final int current, final int textLength) {
        final int to = current + moveBy;
        if (to < 0) {
            moveBy -= to;
        }
        else if (to >= textLength) {
            moveBy -= (to - textLength);
        }
        return moveBy;
    }
}
