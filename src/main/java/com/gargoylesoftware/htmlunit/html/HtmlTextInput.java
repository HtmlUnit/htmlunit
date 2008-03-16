/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Page;

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
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param page The page that contains this element
     * @param attributes the initial attributes
     */
    HtmlTextInput(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map<String, HtmlAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page type(final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey)
        throws IOException {
        if (this instanceof DisabledElement && ((DisabledElement) this).isDisabled()) {
            return getPage();
        }
        preventDefault_ = false;
        final Page page = super.type(c, shiftKey, ctrlKey, altKey);

        //TODO: handle backspace
        if (!Character.isWhitespace(c) && !preventDefault_) {
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
