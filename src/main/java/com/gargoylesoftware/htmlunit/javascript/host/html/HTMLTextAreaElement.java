/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TEXT_AREA_GET_MAXLENGTH_MAX_INT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TEXT_AREA_SET_COLS_NEGATIVE_THROWS_EXCEPTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TEXT_AREA_SET_COLS_THROWS_EXCEPTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TEXT_AREA_SET_MAXLENGTH_NEGATIVE_THROWS_EXCEPTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TEXT_AREA_SET_ROWS_NEGATIVE_THROWS_EXCEPTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TEXT_AREA_SET_ROWS_THROWS_EXCEPTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TEXT_AREA_SET_VALUE_NULL;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * The JavaScript object {@code HTMLTextAreaElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 */
@JsxClass(domClass = HtmlTextArea.class)
public class HTMLTextAreaElement extends FormField {

    /** "Live" labels collection; has to be a member to have equality (==) working. */
    private AbstractList labels_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public HTMLTextAreaElement() {
    }

    /**
     * Returns the type of this input.
     * @return the type of this input
     */
    @JsxGetter
    public String getType() {
        return "textarea";
    }

    /**
     * Returns the value of the {@code value} attribute.
     * @return the value of the {@code value} attribute
     */
    @Override
    public String getValue() {
        return ((HtmlTextArea) getDomNodeOrDie()).getText();
    }

    /**
     * Sets the value of the {@code value} attribute.
     * @param value the new value
     */
    @Override
    public void setValue(final Object value) {
        if (null == value && getBrowserVersion().hasFeature(JS_TEXT_AREA_SET_VALUE_NULL)) {
            ((HtmlTextArea) getDomNodeOrDie()).setText("");
            return;
        }

        ((HtmlTextArea) getDomNodeOrDie()).setText(Context.toString(value));
    }

    /**
     * Returns the number of columns in this text area.
     * @return the number of columns in this text area
     */
    @JsxGetter
    public int getCols() {
        final String s = getDomNodeOrDie().getAttribute("cols");
        try {
            return Integer.parseInt(s);
        }
        catch (final NumberFormatException e) {
            return 20;
        }
    }

    /**
     * Sets the number of columns in this text area.
     * @param cols the number of columns in this text area
     */
    @JsxSetter
    public void setCols(final String cols) {
        try {
            final int i = Float.valueOf(cols).intValue();
            if (i < 0) {
                if (getBrowserVersion().hasFeature(JS_TEXT_AREA_SET_COLS_NEGATIVE_THROWS_EXCEPTION)) {
                    throw new NumberFormatException("New value for cols '" + cols + "' is smaller than zero.");
                }
                getDomNodeOrDie().setAttribute("cols", null);
                return;
            }
            getDomNodeOrDie().setAttribute("cols", Integer.toString(i));
        }
        catch (final NumberFormatException e) {
            if (getBrowserVersion().hasFeature(JS_TEXT_AREA_SET_COLS_THROWS_EXCEPTION)) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
            getDomNodeOrDie().setAttribute("cols", "20");
        }
    }

    /**
     * Returns the number of rows in this text area.
     * @return the number of rows in this text area
     */
    @JsxGetter
    public int getRows() {
        final String s = getDomNodeOrDie().getAttribute("rows");
        try {
            return Integer.parseInt(s);
        }
        catch (final NumberFormatException e) {
            return 2;
        }
    }

    /**
     * Sets the number of rows in this text area.
     * @param rows the number of rows in this text area
     */
    @JsxSetter
    public void setRows(final String rows) {
        try {
            final int i = new Float(rows).intValue();
            if (i < 0) {
                if (getBrowserVersion().hasFeature(JS_TEXT_AREA_SET_ROWS_NEGATIVE_THROWS_EXCEPTION)) {
                    throw new NumberFormatException("New value for rows '" + rows + "' is smaller than zero.");
                }
                getDomNodeOrDie().setAttribute("rows", null);
                return;
            }
            getDomNodeOrDie().setAttribute("rows", Integer.toString(i));
        }
        catch (final NumberFormatException e) {
            if (getBrowserVersion().hasFeature(JS_TEXT_AREA_SET_ROWS_THROWS_EXCEPTION)) {
                throw Context.throwAsScriptRuntimeEx(e);
            }

            getDomNodeOrDie().setAttribute("rows", "2");
        }
    }

    /**
     * Returns the textarea's default value, used if the containing form gets reset.
     * @return the textarea's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getDefaultValue() {
        return ((HtmlTextArea) getDomNodeOrDie()).getDefaultValue();
    }

    /**
     * Sets the textarea's default value, used if the containing form gets reset.
     * @param defaultValue the textarea's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setDefaultValue(final String defaultValue) {
        ((HtmlTextArea) getDomNodeOrDie()).setDefaultValue(defaultValue);
    }

    /**
     * Gets the value of {@code textLength} attribute.
     * @return the text length
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public int getTextLength() {
        return getValue().length();
    }

    /**
     * Gets the value of {@code selectionStart} attribute.
     * @return the selection start
     */
    @JsxGetter
    public int getSelectionStart() {
        return ((HtmlTextArea) getDomNodeOrDie()).getSelectionStart();
    }

    /**
     * Sets the value of {@code selectionStart} attribute.
     * @param start selection start
     */
    @JsxSetter
    public void setSelectionStart(final int start) {
        ((HtmlTextArea) getDomNodeOrDie()).setSelectionStart(start);
    }

    /**
     * Gets the value of {@code selectionEnd} attribute.
     * @return the selection end
     */
    @JsxGetter
    public int getSelectionEnd() {
        return ((HtmlTextArea) getDomNodeOrDie()).getSelectionEnd();
    }

    /**
     * Sets the value of {@code selectionEnd} attribute.
     * @param end selection end
     */
    @JsxSetter
    public void setSelectionEnd(final int end) {
        ((HtmlTextArea) getDomNodeOrDie()).setSelectionEnd(end);
    }

    /**
     * Sets the selected portion of this input element.
     * @param start the index of the first character to select
     * @param end the index of the character after the selection
     */
    @JsxFunction
    public void setSelectionRange(final int start, final int end) {
        setSelectionStart(start);
        setSelectionEnd(end);
    }

    /**
     * Selects this element.
     */
    @JsxFunction
    public void select() {
        ((HtmlTextArea) getDomNodeOrDie()).select();
    }

    /**
     * Gets the value of {@code readOnly} attribute.
     * @return the readOnly attribute
     */
    @JsxGetter
    public boolean getReadOnly() {
        return ((HtmlTextArea) getDomNodeOrDie()).isReadOnly();
    }

    /**
     * Sets the value of {@code readOnly} attribute.
     * @param readOnly the new value
     */
    @JsxSetter
    public void setReadOnly(final boolean readOnly) {
        ((HtmlTextArea) getDomNodeOrDie()).setReadOnly(readOnly);
    }

    /**
     * Returns the maximum number of characters in this text area.
     * @return the maximum number of characters in this text area
     */
    @JsxGetter
    public Object getMaxLength() {
        final String maxLength = getDomNodeOrDie().getAttribute("maxLength");

        try {
            return Integer.parseInt(maxLength);
        }
        catch (final NumberFormatException e) {
            if (getBrowserVersion().hasFeature(JS_TEXT_AREA_GET_MAXLENGTH_MAX_INT)) {
                return Integer.MAX_VALUE;
            }
            return -1;
        }
    }

    /**
     * Sets maximum number of characters in this text area.
     * @param maxLength maximum number of characters in this text area.
     */
    @JsxSetter
    public void setMaxLength(final String maxLength) {
        try {
            final int i = Integer.parseInt(maxLength);

            if (i < 0 && getBrowserVersion().hasFeature(JS_TEXT_AREA_SET_MAXLENGTH_NEGATIVE_THROWS_EXCEPTION)) {
                throw Context.throwAsScriptRuntimeEx(
                    new NumberFormatException("New value for maxLength '" + maxLength + "' is smaller than zero."));
            }
            getDomNodeOrDie().setAttribute("maxLength", maxLength);
        }
        catch (final NumberFormatException e) {
            getDomNodeOrDie().setAttribute("maxLength", "0");
            return;
        }
    }

    /**
     * Returns the {@code placeholder} attribute.
     * @return the {@code placeholder} attribute
     */
    @JsxGetter
    public String getPlaceholder() {
        return ((HtmlTextArea) getDomNodeOrDie()).getPlaceholder();
    }

    /**
     * Sets the {@code placeholder} attribute.
     * @param placeholder the new {@code placeholder} value
     */
    @JsxSetter
    public void setPlaceholder(final String placeholder) {
        ((HtmlTextArea) getDomNodeOrDie()).setPlaceholder(placeholder);
    }

    /**
     * Returns the labels associated with the element.
     * @return the labels associated with the element
     */
    @JsxGetter(@WebBrowser(CHROME))
    public AbstractList getLabels() {
        if (labels_ == null) {
            labels_ = new LabelsHelper(getDomNodeOrDie());
        }
        return labels_;
    }

}
