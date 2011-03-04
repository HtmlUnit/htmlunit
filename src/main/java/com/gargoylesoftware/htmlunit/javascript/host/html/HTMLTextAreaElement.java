/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.util.regex.Pattern;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.host.FormField;

/**
 * The JavaScript object that represents a textarea.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
public class HTMLTextAreaElement extends FormField {

    private static final Pattern NORMALIZE_VALUE_PATTERN = Pattern.compile("([^\\r])\\n");

    /**
     * Creates an instance.
     */
    public HTMLTextAreaElement() {
        // Empty.
    }

    /**
     * Returns the type of this input.
     * @return the type of this input
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
        String value = ((HtmlTextArea) getDomNodeOrDie()).getText();
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.TEXTAREA_CRNL)) {
            value = NORMALIZE_VALUE_PATTERN.matcher(value).replaceAll("$1\r\n");
        }
        return value;
    }

    /**
     * Sets the value of the "value" attribute.
     * @param value the new value
     */
    @Override
    public void jsxSet_value(final String value) {
        ((HtmlTextArea) getDomNodeOrDie()).setText(value);
    }

    /**
     * Returns the number of columns in this text area.
     * @return the number of columns in this text area
     */
    public int jsxGet_cols() {
        int cols;
        try {
            final String s = getDomNodeOrDie().getAttribute("cols");
            cols = Integer.parseInt(s);
        }
        catch (final NumberFormatException e) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_173)) {
                cols = -1;
            }
            else {
                cols = 20;
            }
        }
        return cols;
    }

    /**
     * Sets the number of columns in this text area.
     * @param cols the number of columns in this text area
     */
    public void jsxSet_cols(final String cols) {
        int i;
        try {
            i = new Float(cols).intValue();
            if (i < 0) {
                throw new NumberFormatException();
            }
        }
        catch (final NumberFormatException e) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_110)) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
            i = 0;
        }
        getDomNodeOrDie().setAttribute("cols", Integer.toString(i));
    }

    /**
     * Returns the number of rows in this text area.
     * @return the number of rows in this text area
     */
    public int jsxGet_rows() {
        int rows;
        try {
            final String s = getDomNodeOrDie().getAttribute("rows");
            rows = Integer.parseInt(s);
        }
        catch (final NumberFormatException e) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_174)) {
                rows = -1;
            }
            else {
                rows = 2;
            }
        }
        return rows;
    }

    /**
     * Sets the number of rows in this text area.
     * @param rows the number of rows in this text area
     */
    public void jsxSet_rows(final String rows) {
        int i;
        try {
            i = new Float(rows).intValue();
            if (i < 0) {
                throw new NumberFormatException();
            }
        }
        catch (final NumberFormatException e) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_111)) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
            i = 0;
        }
        getDomNodeOrDie().setAttribute("rows", Integer.toString(i));
    }

    /**
     * Returns the textarea's default value, used if the containing form gets reset.
     * @return the textarea's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    public String jsxGet_defaultValue() {
        String value = ((HtmlTextArea) getDomNodeOrDie()).getDefaultValue();
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.TEXTAREA_CRNL)) {
            value = NORMALIZE_VALUE_PATTERN.matcher(value).replaceAll("$1\r\n");
        }
        return value;
    }

    /**
     * Sets the textarea's default value, used if the containing form gets reset.
     * @param defaultValue the textarea's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    public void jsxSet_defaultValue(final String defaultValue) {
        ((HtmlTextArea) getDomNodeOrDie()).setDefaultValue(defaultValue);
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
        return ((HtmlTextArea) getDomNodeOrDie()).getSelectionStart();
    }

    /**
     * Sets the value of "selectionStart" attribute.
     * @param start selection start
     */
    public void jsxSet_selectionStart(final int start) {
        ((HtmlTextArea) getDomNodeOrDie()).setSelectionStart(start);
    }

    /**
     * Gets the value of "selectionEnd" attribute.
     * @return the selection end
     */
    public int jsxGet_selectionEnd() {
        return ((HtmlTextArea) getDomNodeOrDie()).getSelectionEnd();
    }

    /**
     * Sets the value of "selectionEnd" attribute.
     * @param end selection end
     */
    public void jsxSet_selectionEnd(final int end) {
        ((HtmlTextArea) getDomNodeOrDie()).setSelectionEnd(end);
    }

    /**
     * Sets the selected portion of this input element.
     * @param start the index of the first character to select
     * @param end the index of the character after the selection
     */
    public void jsxFunction_setSelectionRange(final int start, final int end) {
        jsxSet_selectionStart(start);
        jsxSet_selectionEnd(end);
    }

    /**
     * Selects this element.
     */
    public void jsxFunction_select() {
        ((HtmlTextArea) getDomNodeOrDie()).select();
    }

    /**
     * Gets the value of "readOnly" attribute.
     * @return the readOnly attribute
     */
    public boolean jsxGet_readOnly() {
        return ((HtmlTextArea) getDomNodeOrDie()).isReadOnly();
    }

    /**
     * Sets the value of "readOnly" attribute.
     * @param readOnly the new value
     */
    public void jsxSet_readOnly(final boolean readOnly) {
        ((HtmlTextArea) getDomNodeOrDie()).setReadOnly(readOnly);
    }

}
