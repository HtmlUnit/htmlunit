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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * The JavaScript object that represents a textarea.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class HTMLTextAreaElement extends FormField {

    private static final long serialVersionUID = 49352135575074390L;

    /**
     * Create an instance.
     */
    public HTMLTextAreaElement() {
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Returns the type of this input.
     * @return the type
     */
    @Override
    public String jsxGet_type() {
        return "textarea";
    }

    /**
     * Returns the value of the "value" attribute.
     * @return the value of the "value" attribute
     */
    @Override
    public String jsxGet_value() {
        String value = ((HtmlTextArea) getHtmlElementOrDie()).getText();
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.TEXTAREA_CRNL)) {
            value = value.replaceAll("([^\\r])\\n", "$1\r\n");
        }
        return value;
    }

    /**
     * Sets the value of the "value" attribute.
     * @param value the new value
     */
    @Override
    public void jsxSet_value(final String value) {
        ((HtmlTextArea) getHtmlElementOrDie()).setText(value);
    }

    /**
     * Returns the textarea's default value, used if the containing form gets reset.
     * @return the textarea's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    public String jsxGet_defaultValue() {
        String value = ((HtmlTextArea) getHtmlElementOrDie()).getDefaultValue();
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.TEXTAREA_CRNL)) {
            value = value.replaceAll("([^\\r])\\n", "$1\r\n");
        }
        return value;
    }

    /**
     * Sets the textarea's default value, used if the containing form gets reset.
     * @param defaultValue the textarea's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    public void jsxSet_defaultValue(final String defaultValue) {
        ((HtmlTextArea) getHtmlElementOrDie()).setDefaultValue(defaultValue);
    }

    /**
     * Gets the value of "textLength" attribute.
     * @return the text length
     */
    public int jsxGet_textLength() {
        return jsxGet_value().length();
    }

    /**
     * Gets the value of "selectionStart" attribute.
     * @return the selection start
     */
    public int jsxGet_selectionStart() {
        return ((HtmlTextArea) getHtmlElementOrDie()).getSelectionStart();
    }

    /**
     * Sets the value of "selectionStart" attribute.
     * @param start selection start
     */
    public void jsxSet_selectionStart(final int start) {
        ((HtmlTextArea) getHtmlElementOrDie()).setSelectionStart(start);
    }

    /**
     * Gets the value of "selectionEnd" attribute.
     * @return the selection end
     */
    public int jsxGet_selectionEnd() {
        return ((HtmlTextArea) getHtmlElementOrDie()).getSelectionEnd();
    }

    /**
     * Sets the value of "selectionEnd" attribute.
     * @param end selection end
     */
    public void jsxSet_selectionEnd(final int end) {
        ((HtmlTextArea) getHtmlElementOrDie()).setSelectionEnd(end);
    }

    /**
     * Simulates a click on a scrollbar component (IE only).
     * @param scrollAction the type of scroll action to simulate
     */
    public void jsxFunction_doScroll(final String scrollAction) {
        // Ignore because we aren't displaying anything!
    }

    /**
     * Selects this element.
     */
    public void jsxFunction_select() {
        ((HtmlTextArea) getDomNodeOrDie()).select();
    }

}
