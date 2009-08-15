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
package com.gargoylesoftware.htmlunit.html;

import java.io.Serializable;

import org.w3c.dom.ranges.Range;

/**
 * Contains standard selection-related functionality used by various input elements.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
class SelectionDelegate implements Serializable {

    private static final long serialVersionUID = -5611492671640559880L;

    /** The owner element. */
    private final SelectableTextInput element_;

    /**
     * Creates a new instance for the specified element.
     * @param element the owner element
     */
    public SelectionDelegate(final SelectableTextInput element) {
        element_ = element;
    }

    /**
     * Focuses the owner element and selects all of its text.
     */
    public void select() {
        element_.focus();
        element_.setSelectionStart(0);
        element_.setSelectionEnd(element_.getText().length());
    }

    /**
     * Returns the selected text in the owner element, or <tt>null</tt> if there is no selected text.
     * @return the selected text in the owner element, or <tt>null</tt> if there is no selected text
     */
    public String getSelectedText() {
        final Range selection = getSelectionInner();
        if (selection != null) {
            return element_.getText().substring(selection.getStartOffset(), selection.getEndOffset());
        }
        return null;
    }

    /**
     * Returns the start position of the selected text in the owner element.
     * @return the start position of the selected text in the owner element
     */
    public int getSelectionStart() {
        final Range selection = getSelectionInner();
        if (selection != null) {
            return selection.getStartOffset();
        }
        return element_.getText().length();
    }

    /**
     * Sets the start position of the selected text in the owner element.
     * @param selectionStart the start position of the selected text in the owner element
     */
    public void setSelectionStart(int selectionStart) {
        if (element_.getPage() instanceof HtmlPage) {
            final HtmlPage page = (HtmlPage) element_.getPage();
            final int length = element_.getText().length();
            selectionStart = Math.max(0, Math.min(selectionStart, length));
            page.getSelection().setStart(element_, selectionStart);
            if (page.getSelection().getEndContainer() != element_) {
                page.getSelection().setEnd(element_, length);
            }
            else if (page.getSelection().getEndOffset() < selectionStart) {
                page.getSelection().setEnd(element_, selectionStart);
            }
        }
    }

    /**
     * Returns the end position of the selected text in the owner element.
     * @return the end position of the selected text in the owner element
     */
    public int getSelectionEnd() {
        final Range selection = getSelectionInner();
        if (selection != null) {
            return selection.getEndOffset();
        }
        return element_.getText().length();
    }

    /**
     * Sets the end position of the selected text in the owner element.
     * @param selectionEnd the end position of the selected text in the owner element
     */
    public void setSelectionEnd(int selectionEnd) {
        if (element_.getPage() instanceof HtmlPage) {
            final HtmlPage page = (HtmlPage) element_.getPage();
            final int length = element_.getText().length();
            selectionEnd = Math.min(length, Math.max(selectionEnd, 0));
            page.getSelection().setEnd(element_, selectionEnd);
            if (page.getSelection().getStartContainer() != element_) {
                page.getSelection().setStart(element_, 0);
            }
            else if (page.getSelection().getStartOffset() > selectionEnd) {
                page.getSelection().setStart(element_, selectionEnd);
            }
        }
    }

    private Range getSelectionInner() {
        if (element_.getPage() instanceof HtmlPage) {
            final Range selection = ((HtmlPage) element_.getPage()).getSelection();
            if (selection.getStartContainer() == element_ && selection.getEndContainer() == element_) {
                return selection;
            }
        }
        return null;
    }

}
