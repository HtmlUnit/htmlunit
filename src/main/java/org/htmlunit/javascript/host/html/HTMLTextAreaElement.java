/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript.host.html;

import org.htmlunit.html.HtmlTextArea;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.javascript.host.dom.NodeList;

/**
 * The JavaScript object {@code HTMLTextAreaElement}.
 *
 * @author Mike Bowler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 */
@JsxClass(domClass = HtmlTextArea.class)
public class HTMLTextAreaElement extends HTMLElement {

    /** "Live" labels collection; has to be a member to have equality (==) working. */
    private NodeList labels_;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
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
     * {@inheritDoc}
     */
    @Override
    public HtmlTextArea getDomNodeOrDie() {
        return (HtmlTextArea) super.getDomNodeOrDie();
    }

    /**
     * Returns the value of the {@code value} attribute.
     * @return the value of the {@code value} attribute
     */
    @JsxGetter
    @Override
    public String getValue() {
        return getDomNodeOrDie().getText();
    }

    /**
     * Sets the value of the {@code value} attribute.
     * @param value the new value
     */
    @JsxSetter
    @Override
    public void setValue(final Object value) {
        if (null == value) {
            getDomNodeOrDie().setText("");
            return;
        }

        getDomNodeOrDie().setText(JavaScriptEngine.toString(value));
    }

    /**
     * Returns the number of columns in this text area.
     * @return the number of columns in this text area
     */
    @JsxGetter
    public int getCols() {
        final String s = getDomNodeOrDie().getAttributeDirect("cols");
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
                getDomNodeOrDie().setAttribute("cols", null);
                return;
            }
            getDomNodeOrDie().setAttribute("cols", Integer.toString(i));
        }
        catch (final NumberFormatException e) {
            getDomNodeOrDie().setAttribute("cols", "20");
        }
    }

    /**
     * Returns the number of rows in this text area.
     * @return the number of rows in this text area
     */
    @JsxGetter
    public int getRows() {
        final String s = getDomNodeOrDie().getAttributeDirect("rows");
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
            final int i = Float.valueOf(rows).intValue();
            if (i < 0) {
                getDomNodeOrDie().setAttribute("rows", null);
                return;
            }
            getDomNodeOrDie().setAttribute("rows", Integer.toString(i));
        }
        catch (final NumberFormatException e) {
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
        return getDomNodeOrDie().getDefaultValue();
    }

    /**
     * Sets the textarea's default value, used if the containing form gets reset.
     * @param defaultValue the textarea's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setDefaultValue(final String defaultValue) {
        getDomNodeOrDie().setDefaultValue(defaultValue);
    }

    /**
     * Gets the value of {@code textLength} attribute.
     * @return the text length
     */
    @JsxGetter
    public int getTextLength() {
        return getValue().length();
    }

    /**
     * Gets the value of {@code selectionStart} attribute.
     * @return the selection start
     */
    @JsxGetter
    public int getSelectionStart() {
        return getDomNodeOrDie().getSelectionStart();
    }

    /**
     * Sets the value of {@code selectionStart} attribute.
     * @param start selection start
     */
    @JsxSetter
    public void setSelectionStart(final int start) {
        getDomNodeOrDie().setSelectionStart(start);
    }

    /**
     * Gets the value of {@code selectionEnd} attribute.
     * @return the selection end
     */
    @JsxGetter
    public int getSelectionEnd() {
        return getDomNodeOrDie().getSelectionEnd();
    }

    /**
     * Sets the value of {@code selectionEnd} attribute.
     * @param end selection end
     */
    @JsxSetter
    public void setSelectionEnd(final int end) {
        getDomNodeOrDie().setSelectionEnd(end);
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
        getDomNodeOrDie().select();
    }

    /**
     * Gets the value of {@code readOnly} attribute.
     * @return the readOnly attribute
     */
    @JsxGetter
    public boolean isReadOnly() {
        return getDomNodeOrDie().isReadOnly();
    }

    /**
     * Sets the value of {@code readOnly} attribute.
     * @param readOnly the new value
     */
    @JsxSetter
    public void setReadOnly(final boolean readOnly) {
        getDomNodeOrDie().setReadOnly(readOnly);
    }

    /**
     * Returns the maximum number of characters in this text area.
     * @return the maximum number of characters in this text area
     */
    @JsxGetter
    public int getMaxLength() {
        final String maxLength = getDomNodeOrDie().getAttribute("maxLength");

        try {
            return Integer.parseInt(maxLength);
        }
        catch (final NumberFormatException e) {
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

            if (i < 0) {
                throw JavaScriptEngine.asJavaScriptException(getWindow(),
                        "New value for maxLength '" + maxLength + "' is smaller than zero.",
                        DOMException.INDEX_SIZE_ERR);
            }
            getDomNodeOrDie().setAttribute("maxLength", maxLength);
        }
        catch (final NumberFormatException e) {
            getDomNodeOrDie().setAttribute("maxLength", "0");
        }
    }

    /**
     * Returns the minimum number of characters in this text area.
     * @return the minimum number of characters in this text area
     */
    @JsxGetter
    public int getMinLength() {
        final String minLength = getDomNodeOrDie().getAttribute("minLength");

        try {
            return Integer.parseInt(minLength);
        }
        catch (final NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Sets minimum number of characters in this text area.
     * @param minLength minimum number of characters in this text area.
     */
    @JsxSetter
    public void setMinLength(final String minLength) {
        try {
            final int i = Integer.parseInt(minLength);

            if (i < 0) {
                throw JavaScriptEngine.throwAsScriptRuntimeEx(
                    new NumberFormatException("New value for minLength '" + minLength + "' is smaller than zero."));
            }
            getDomNodeOrDie().setAttribute("minLength", minLength);
        }
        catch (final NumberFormatException e) {
            getDomNodeOrDie().setAttribute("minLength", "0");
        }
    }

    /**
     * Returns the {@code placeholder} attribute.
     * @return the {@code placeholder} attribute
     */
    @JsxGetter
    public String getPlaceholder() {
        return getDomNodeOrDie().getPlaceholder();
    }

    /**
     * Sets the {@code placeholder} attribute.
     * @param placeholder the new {@code placeholder} value
     */
    @JsxSetter
    public void setPlaceholder(final String placeholder) {
        getDomNodeOrDie().setPlaceholder(placeholder);
    }

    /**
     * Returns the labels associated with the element.
     * @return the labels associated with the element
     */
    @JsxGetter
    public NodeList getLabels() {
        if (labels_ == null) {
            labels_ = new LabelsNodeList(getDomNodeOrDie());
        }
        return labels_;
    }

    /**
     * Checks whether the element has any constraints and whether it satisfies them.
     * @return if the element is valid
     */
    @JsxFunction
    public boolean checkValidity() {
        return getDomNodeOrDie().isValid();
    }

    /**
     * Returns the {@code required} property.
     * @return the {@code required} property
     */
    @JsxGetter
    public boolean isRequired() {
        return getDomNodeOrDie().isRequired();
    }

    /**
     * Sets the {@code required} property.
     * @param required the new value
     */
    @JsxSetter
    public void setRequired(final boolean required) {
        getDomNodeOrDie().setRequired(required);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getName() {
        return super.getName();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setName(final String newName) {
        super.setName(newName);
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public boolean isDisabled() {
        return super.isDisabled();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxSetter
    public void setDisabled(final boolean disabled) {
        super.setDisabled(disabled);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public HTMLFormElement getForm() {
        return super.getForm();
    }

    /**
     * @return whether the element is a candidate for constraint validation
     */
    @JsxGetter
    public boolean isWillValidate() {
        return getDomNodeOrDie().willValidate();
    }

    /**
     * @return a ValidityState with the validity states that this element is in.
     */
    @JsxGetter
    public ValidityState getValidity() {
        final ValidityState validityState = new ValidityState();
        validityState.setPrototype(getPrototype(validityState.getClass()));
        validityState.setParentScope(getParentScope());
        validityState.setDomNode(getDomNodeOrDie());
        return validityState;
    }

    /**
     * Sets the custom validity message for the element to the specified message.
     * @param message the new message
     */
    @JsxFunction
    public void setCustomValidity(final String message) {
        getDomNodeOrDie().setCustomValidity(message);
    }
}
