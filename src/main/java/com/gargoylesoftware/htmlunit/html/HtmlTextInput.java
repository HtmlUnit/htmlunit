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

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class HtmlTextInput extends HtmlInput {

    private static final long serialVersionUID = -2473799124286935674L;
    private boolean preventDefault_;

    private int selectionStart_;
    
    private int selectionEnd_;

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
        final Page page = super.type(c, shiftKey, ctrlKey, altKey);

        //TODO: handle backspace
        if ((c == ' ' || !Character.isWhitespace(c)) && !preventDefault_) {
            setValueAttribute(getValueAttribute() + c);
        }
        return page;
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
        String text = null;
        if (selectionStart_ != selectionEnd_) {
            text = getValueAttribute().substring(selectionStart_, selectionEnd_);
        }
        return text;
    }
    
    /**
     * Returns the selected text's start position (Firefox only).
     * @return the start position >= 0
     */
    public int getSelectionStart() {
        return selectionStart_;
    }

    /**
     * Sets the selection start to the specified position (Firefox only).
     * @param selectionStart the start position of the text >= 0
     */
    public void setSelectionStart(int selectionStart) {
        if (selectionStart < 0) {
            selectionStart = 0;
        }
        final int length = getValueAttribute().length();
        if (selectionStart > length) {
            selectionStart = length;
        }
        if (selectionEnd_ < selectionStart) {
            selectionEnd_ = selectionStart;
        }
        this.selectionStart_ = selectionStart;
    }
    
    /**
     * Returns the selected text's end position (Firefox only).
     * @return the end position >= 0
     */
    public int getSelectionEnd() {
        return selectionEnd_;
    }
 
    /**
     * Sets the selection end to the specified position (Firefox only).
     * @param selectionEnd the end position of the text >= 0
     */
    public void setSelectionEnd(int selectionEnd) {
        if (selectionEnd < 0) {
            selectionEnd = 0;
        }
        final int length = getValueAttribute().length();
        if (selectionEnd > length) {
            selectionEnd = length;
        }
        if (selectionEnd < selectionStart_) {
            selectionStart_ = selectionEnd;
        }
        this.selectionEnd_ = selectionEnd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributeValue(final String namespaceURI, final String qualifiedName,
            final String attributeValue) {
        super.setAttributeValue(namespaceURI, qualifiedName, attributeValue);
        if (qualifiedName.equals("value")) {
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
}
