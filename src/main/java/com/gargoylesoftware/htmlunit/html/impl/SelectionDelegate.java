/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_13;

import java.io.Serializable;

import org.w3c.dom.ranges.Range;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * <p>Contains standard selection-related functionality used by various input elements.</p>
 *
 * <p>From <a href="http://www.whatwg.org/specs/web-apps/current-work/#selection">the HTML5 spec</a>:</p>
 *
 * <blockquote>Mostly for historical reasons, in addition to the browsing context's selection, each
 * textarea and input element has an independent selection. These are the text field selections.</blockquote>
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ronald Brill
 */
public class SelectionDelegate implements Serializable {

    /** The owner element. */
    private final SelectableTextInput element_;

    /** The field selection, which is independent of the browsing context's selection. */
    private final Range selection_;

    /**
     * Creates a new instance for the specified element.
     * @param element the owner element
     */
    public SelectionDelegate(final SelectableTextInput element) {
        element_ = element;
        selection_ = new SimpleRange(element, 0);
    }

    /**
     * Focuses the owner element and selects all of its text.
     */
    public void select() {
        element_.focus();
        setSelectionStart(0);
        setSelectionEnd(element_.getText().length());
    }

    /**
     * Returns the selected text in the owner element, or <tt>null</tt> if there is no selected text.
     * @return the selected text in the owner element, or <tt>null</tt> if there is no selected text
     */
    public String getSelectedText() {
        return selection_.toString();
    }

    /**
     * Returns the start position of the selected text in the owner element.
     * @return the start position of the selected text in the owner element
     */
    public int getSelectionStart() {
        return selection_.getStartOffset();
    }

    /**
     * Sets the start position of the selected text in the owner element.
     * @param selectionStart the start position of the selected text in the owner element
     */
    public void setSelectionStart(int selectionStart) {
        final int length = element_.getText().length();
        selectionStart = Math.max(0, Math.min(selectionStart, length));
        selection_.setStart(element_, selectionStart);
        if (selection_.getEndOffset() < selectionStart) {
            selection_.setEnd(element_, selectionStart);
        }
        makeThisTheOnlySelectionIfEmulatingIE();
    }

    /**
     * Returns the end position of the selected text in the owner element.
     * @return the end position of the selected text in the owner element
     */
    public int getSelectionEnd() {
        return selection_.getEndOffset();
    }

    /**
     * Sets the end position of the selected text in the owner element.
     * @param selectionEnd the end position of the selected text in the owner element
     */
    public void setSelectionEnd(int selectionEnd) {
        final int length = element_.getText().length();
        selectionEnd = Math.min(length, Math.max(selectionEnd, 0));
        selection_.setEnd(element_, selectionEnd);
        if (selection_.getStartOffset() > selectionEnd) {
            selection_.setStart(element_, selectionEnd);
        }
        makeThisTheOnlySelectionIfEmulatingIE();
    }

    private void makeThisTheOnlySelectionIfEmulatingIE() {
        final Page page = element_.getPage();
        if (page != null && page.isHtmlPage()) {
            final HtmlPage htmlPage = (HtmlPage) page;
            if (htmlPage.getWebClient().getBrowserVersion().hasFeature(GENERATED_13)) {
                htmlPage.setSelectionRange(selection_);
            }
        }
    }

}
