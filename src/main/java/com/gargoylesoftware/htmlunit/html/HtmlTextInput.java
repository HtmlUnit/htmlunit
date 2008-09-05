/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.io.IOException;
import java.util.Map;

import org.w3c.dom.ranges.Range;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input" with type="text".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HtmlTextInput extends HtmlInput {

    private static final long serialVersionUID = -2473799124286935674L;

    private String valueAtFocus_;
    private boolean preventDefault_;

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlTextInput(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page type(final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey)
        throws IOException {
        if (isDisabled()) {
            return getPage();
        }
        preventDefault_ = false;
        return super.type(c, shiftKey, ctrlKey, altKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doType(final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        if (preventDefault_) {
            return;
        }
        final String value = getValueAttribute();
        if (c == '\b') {
            if (value.length() > 0) {
                setAttributeValue("value", value.substring(0, value.length() - 1));
            }
        }
        else if ((c == ' ' || !Character.isWhitespace(c))) {
            setAttributeValue("value", value + c);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preventDefault() {
        preventDefault_ = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isSubmittableByEnter() {
        return true;
    }

    /**
     * Returns the selected text contained in this HtmlTextInput, <code>null</code> if no selection (Firefox only).
     * @return the text
     */
    public String getSelectedText() {
        final Range selection = getThisSelection();
        if (selection != null) {
            return getValueAttribute().substring(selection.getStartOffset(), selection.getEndOffset());
        }
        return null;
    }

    private Range getThisSelection() {
        if (getPage() instanceof HtmlPage) {
            final Range selection = ((HtmlPage) getPage()).getSelection();
            if (selection.getStartContainer() == this && selection.getEndContainer() == this) {
                return selection;
            }
        }
        return null;
    }

    /**
     * Returns the selected text's start position (Firefox only).
     * @return the start position >= 0
     */
    public int getSelectionStart() {
        final Range selection = getThisSelection();
        if (selection != null) {
            return selection.getStartOffset();
        }
        return 0;
    }

    /**
     * Sets the selection start to the specified position (Firefox only).
     * @param selectionStart the start position of the text >= 0
     */
    public void setSelectionStart(int selectionStart) {
        if (getPage() instanceof HtmlPage) {
            final HtmlPage page = (HtmlPage) getPage();
            final int length = getValueAttribute().length();
            selectionStart = Math.max(0, Math.min(selectionStart, length));
            page.getSelection().setStart(this, selectionStart);
            if (page.getSelection().getEndContainer() != this) {
                page.getSelection().setEnd(this, length);
            }
            else if (page.getSelection().getEndOffset() < selectionStart) {
                page.getSelection().setEnd(this, selectionStart);
            }
        }
    }

    /**
     * Returns the selected text's end position (Firefox only).
     * @return the end position >= 0
     */
    public int getSelectionEnd() {
        final Range selection = getThisSelection();
        if (selection != null) {
            return selection.getEndOffset();
        }
        return 0;
    }

    /**
     * Sets the selection end to the specified position (Firefox only).
     * @param selectionEnd the end position of the text >= 0
     */
    public void setSelectionEnd(int selectionEnd) {
        if (getPage() instanceof HtmlPage) {
            final HtmlPage page = (HtmlPage) getPage();
            final int length = getValueAttribute().length();
            selectionEnd = Math.min(length, Math.max(selectionEnd, 0));
            page.getSelection().setEnd(this, selectionEnd);
            if (page.getSelection().getStartContainer() != this) {
                page.getSelection().setStart(this, 0);
            }
            else if (page.getSelection().getStartOffset() > selectionEnd) {
                page.getSelection().setStart(this, selectionEnd);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributeValue(final String namespaceURI, final String qualifiedName,
        final String attributeValue, final boolean cloning) {

        super.setAttributeValue(namespaceURI, qualifiedName, attributeValue, cloning);

        if (qualifiedName.equals("value") && !cloning) {
            setSelectionStart(attributeValue.length());
            setSelectionEnd(attributeValue.length());
        }
    }

    /**
     * Select all the text in this input.
     */
    public void select() {
        focus();
        setSelectionStart(0);
        setSelectionEnd(getValueAttribute().length());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void focus() {
        super.focus();
        // store current value to trigger onchange when needed at focus lost
        valueAtFocus_ = getValueAttribute();
    }

    @Override
    void removeFocus() {
        super.removeFocus();

        if (!valueAtFocus_.equals(getValueAttribute())) {
            executeOnChangeHandlerIfAppropriate(this);
        }
        valueAtFocus_ = null;
    }
}
