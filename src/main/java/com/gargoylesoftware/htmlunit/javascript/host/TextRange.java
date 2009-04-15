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

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.w3c.dom.ranges.Range;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a TextRange.
 *
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms535872.aspx">MSDN doc</a>
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class TextRange extends SimpleScriptable {

    private static final long serialVersionUID = -3763822832184277966L;
    private boolean collapsed_ = false; // minimal effort to support collapse()

    /**
     * Retrieves the text contained within the range.
     * @return the text contained within the range
     */
    public Object jsxGet_text() {
        if (collapsed_) {
            return "";
        }

        final HtmlPage page = (HtmlPage) getWindow().getDomNodeOrDie();
        final Range selection = page.getSelection();
        // currently only working for text input and textarea
        if (selection.getStartContainer() == selection.getEndContainer()) {
            if (selection.getStartContainer() instanceof HtmlTextInput) {
                final HtmlTextInput input = (HtmlTextInput) selection.getStartContainer();
                return input.getValueAttribute().substring(input.getSelectionStart(), input.getSelectionEnd());
            }
            else if (selection.getStartContainer() instanceof HtmlTextArea) {
                final HtmlTextArea input = (HtmlTextArea) selection.getStartContainer();
                return input.getText().substring(input.getSelectionStart(), input.getSelectionEnd());
            }
        }
        return "";
    }

    /**
     * Sets the text contained within the range.
     * @param text the text contained within the range
     */
    public void jsxSet_text(final String text) {
        collapsed_ = false;
        final HtmlPage page = (HtmlPage) getWindow().getDomNodeOrDie();
        final Range selection = page.getSelection();
        // currently only working for text input and textarea
        if (selection.getStartContainer() == selection.getEndContainer()) {
            if (selection.getStartContainer() instanceof HtmlTextInput) {
                final HtmlTextInput input = (HtmlTextInput) selection.getStartContainer();
                final String oldValue = input.getValueAttribute();
                input.setValueAttribute(oldValue.substring(0, input.getSelectionStart()) + text
                        + oldValue.substring(input.getSelectionEnd()));
            }
            else if (selection.getStartContainer() instanceof HtmlTextArea) {
                final HtmlTextArea input = (HtmlTextArea) selection.getStartContainer();
                final String oldValue = input.getText();
                input.setText(oldValue.substring(0, input.getSelectionStart()) + text
                        + oldValue.substring(input.getSelectionEnd()));
            }
        }
    }

    /**
     * Duplicates this instance.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536416.aspx">MSDN doc</a>
     * @return a duplicate
     */
    public Object jsxFunction_duplicate() {
        final TextRange range = new TextRange();
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
    public Object jsxFunction_parentElement() {
        final HtmlPage page = (HtmlPage) getWindow().getDomNodeOrDie();
        final Range selection = page.getSelection();
        org.w3c.dom.Node parent = selection.getStartContainer();
        final org.w3c.dom.Node child = selection.getEndContainer();
        while (parent != null) {
            if (isChildOf(child, parent)) {
                return parent;
            }
            parent = parent.getParentNode();
        }
        return null;
    }

    /**
     * Returns if child is a (grand)child of the specified parent.
     */
    private boolean isChildOf(org.w3c.dom.Node child, final org.w3c.dom.Node parent) {
        while (child != null) {
            if (child == parent) {
                return true;
            }
            child = child.getParentNode();
        }
        return false;
    }

    /**
     * Collapses the range.
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536371.aspx">MSDN doc</a>
     */
    public void jsxFunction_collapse() {
        collapsed_ = true;
    }

    /**
     * Makes the current range the active selection.
     * Warning: dummy implementation!
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536735.aspx">MSDN doc</a>
     */
    public void jsxFunction_select() {
        // nothing yet
    }

    /**
     * Changes the start position of the range.
     * @param unit specifies the units to move
     * @param count the number of units to move
     * @return the number of units moved
     */
    public int jsxFunction_moveStart(final String unit, final Object count) {
        if ("characters".equals(unit)) {
            getLog().info("moveStart('" + unit + "' is not yet supported.");
        }
        int c = 1;
        if (count != Undefined.instance) {
            c = (int) Context.toNumber(count);
        }
        final HtmlPage page = (HtmlPage) getWindow().getDomNodeOrDie();
        final Range selection = page.getSelection();
        // currently only working for text input and textarea
        if (selection.getStartContainer() == selection.getEndContainer()) {
            if (selection.getStartContainer() instanceof HtmlTextInput) {
                final HtmlTextInput input = (HtmlTextInput) selection.getStartContainer();
                selection.setStart(input, selection.getStartOffset() + c);
            }
            else if (selection.getStartContainer() instanceof HtmlTextArea) {
                final HtmlTextArea input = (HtmlTextArea) selection.getStartContainer();
                selection.setStart(input, selection.getStartOffset() + c);
            }
        }
        return c;
    }

    /**
     * Changes the end position of the range.
     * @param unit specifies the units to move
     * @param count the number of units to move
     * @return the number of units moved
     */
    public int jsxFunction_moveEnd(final String unit, final Object count) {
        if ("characters".equals(unit)) {
            getLog().info("moveStart('" + unit + "' is not yet supported.");
        }
        int c = 1;
        if (count != Undefined.instance) {
            c = (int) Context.toNumber(count);
        }
        final HtmlPage page = (HtmlPage) getWindow().getDomNodeOrDie();
        final Range selection = page.getSelection();
        // currently only working for text input and textarea
        if (selection.getStartContainer() == selection.getEndContainer()) {
            if (selection.getStartContainer() instanceof HtmlTextInput) {
                final HtmlTextInput input = (HtmlTextInput) selection.getStartContainer();
                selection.setEnd(input, selection.getEndOffset() + c);
            }
            else if (selection.getStartContainer() instanceof HtmlTextArea) {
                final HtmlTextArea input = (HtmlTextArea) selection.getStartContainer();
                selection.setEnd(input, selection.getEndOffset() + c);
            }
        }
        return c;
    }
}
