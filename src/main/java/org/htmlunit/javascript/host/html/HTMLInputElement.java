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

import static org.htmlunit.BrowserVersionFeatures.JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlCheckBoxInput;
import org.htmlunit.html.HtmlFileInput;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlNumberInput;
import org.htmlunit.html.HtmlRadioButtonInput;
import org.htmlunit.html.HtmlTextInput;
import org.htmlunit.html.impl.SelectableTextInput;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.DOMRectList;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.javascript.host.dom.NodeList;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.file.FileList;

/**
 * The JavaScript object for {@link HtmlInput}.
 *
 * @author Mike Bowler
 * @author Christian Sell
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 * @author Anton Demydenko
 */
@JsxClass(domClass = HtmlInput.class)
public class HTMLInputElement extends HTMLElement {

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
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @JsxGetter
    public String getType() {
        return getDomNodeOrDie().getType();
    }

    /**
     * Sets the value of the attribute {@code type}.
     * Note: this replaces the DOM node with a new one.
     * @param newType the new type to set
     */
    @JsxSetter
    public void setType(final String newType) {
        getDomNodeOrDie().changeType(newType, false);
    }

    /**
     * Sets the value of the JavaScript attribute {@code value}.
     *
     * @param newValue the new value
     */
    @JsxSetter
    @Override
    public void setValue(final Object newValue) {
        if (null == newValue) {
            getDomNodeOrDie().setValue("");
            getDomNodeOrDie().valueModifiedByJavascript();
            return;
        }

        final String val = JavaScriptEngine.toString(newValue);
        if ("file".equals(getType())) {
            if (StringUtils.isNotEmpty(val)) {
                throw JavaScriptEngine.asJavaScriptException(
                        getWindow(),
                        "Failed to set the 'value' property on 'HTMLInputElement'.",
                        DOMException.INVALID_STATE_ERR);
            }
            return;
        }

        getDomNodeOrDie().setValue(val);
        getDomNodeOrDie().valueModifiedByJavascript();
    }

    /**
     * Sets the checked property. Although this property is defined in Input it
     * doesn't make any sense for input's other than checkbox and radio. This
     * implementation does nothing. The implementations in Checkbox and Radio
     * actually do the work.
     *
     * @param checked True if this input should have the {@code checked} attribute set
     */
    @JsxSetter
    public void setChecked(final boolean checked) {
        getDomNodeOrDie().setChecked(checked);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlInput getDomNodeOrDie() {
        return (HtmlInput) super.getDomNodeOrDie();
    }

    /**
     * Returns the value of the checked property. Although this property is
     * defined in Input it doesn't make any sense for input's other than
     * checkbox and radio. This implementation does nothing. The
     * implementations in Checkbox and Radio actually do the work.
     *
     * @return the checked property
     */
    @JsxGetter
    public boolean isChecked() {
        return getDomNodeOrDie().isChecked();
    }

    /**
     * Select this element.
     */
    @JsxFunction
    public void select() {
        final HtmlInput input = getDomNodeOrDie();
        if (input instanceof HtmlTextInput) {
            ((HtmlTextInput) input).select();
        }
        // currently nothing for other input types
    }

    /**
     * Returns the input's default value, used if the containing form gets reset.
     * @return the input's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getDefaultValue() {
        return getDomNodeOrDie().getDefaultValue();
    }

    /**
     * Sets the input's default value, used if the containing form gets reset.
     * @param defaultValue the input's default value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533718.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setDefaultValue(final String defaultValue) {
        getDomNodeOrDie().setDefaultValue(defaultValue);
    }

    /**
     * Returns the input's default checked value, used if the containing form gets reset.
     * @return the input's default checked value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533715.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public boolean isDefaultChecked() {
        return getDomNodeOrDie().isDefaultChecked();
    }

    /**
     * Sets the input's default checked value, used if the containing form gets reset.
     * @param defaultChecked the input's default checked value, used if the containing form gets reset
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533715.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setDefaultChecked(final boolean defaultChecked) {
        getDomNodeOrDie().setDefaultChecked(defaultChecked);
    }

    /**
     * Gets the value of {@code textLength} attribute.
     * @return the text length
     */
    @JsxGetter({FF, FF_ESR})
    public int getTextLength() {
        return getValue().length();
    }

    /**
     * Gets the value of {@code selectionStart} attribute.
     * @return the selection start
     */
    @JsxGetter
    public Integer getSelectionStart() {
        final DomNode dom = getDomNodeOrDie();
        if (dom instanceof SelectableTextInput) {
            if ("number".equals(getType())) {
                return null;
            }

            return ((SelectableTextInput) dom).getSelectionStart();
        }

        return null;
    }

    /**
     * Sets the value of {@code selectionStart} attribute.
     * @param start selection start
     */
    @JsxSetter
    public void setSelectionStart(final int start) {
        final DomNode dom = getDomNodeOrDie();
        if (dom instanceof SelectableTextInput) {
            if ("number".equals(getType())) {
                throw JavaScriptEngine.asJavaScriptException(
                        getWindow(),
                        "Failed to set the 'selectionStart' property"
                                + "from 'HTMLInputElement': "
                                + "The input element's type ('number') does not support selection.",
                        DOMException.INVALID_STATE_ERR);
            }

            ((SelectableTextInput) dom).setSelectionStart(start);
            return;
        }

        throw JavaScriptEngine.asJavaScriptException(
                getWindow(),
                "Failed to set the 'selectionStart' property from 'HTMLInputElement': "
                        + "The input element's type (" + getType() + ") does not support selection.",
                DOMException.INVALID_STATE_ERR);
    }

    /**
     * Gets the value of {@code selectionEnd} attribute.
     * @return the selection end
     */
    @JsxGetter
    public Integer getSelectionEnd() {
        final DomNode dom = getDomNodeOrDie();
        if (dom instanceof SelectableTextInput) {
            if ("number".equals(getType())) {
                return null;
            }

            return ((SelectableTextInput) dom).getSelectionEnd();
        }

        return null;
    }

    /**
     * Sets the value of {@code selectionEnd} attribute.
     * @param end selection end
     */
    @JsxSetter
    public void setSelectionEnd(final int end) {
        final DomNode dom = getDomNodeOrDie();
        if (dom instanceof SelectableTextInput) {
            if ("number".equals(getType())) {
                throw JavaScriptEngine.asJavaScriptException(
                        getWindow(),
                        "Failed to set the 'selectionEnd' property"
                                + "from 'HTMLInputElement': "
                                + "The input element's type ('number') does not support selection.",
                        DOMException.INVALID_STATE_ERR);
            }

            ((SelectableTextInput) dom).setSelectionEnd(end);
            return;
        }

        throw JavaScriptEngine.asJavaScriptException(
                getWindow(),
                "Failed to set the 'selectionEnd' property from 'HTMLInputElement': "
                        + "The input element's type (" + getType() + ") does not support selection.",
                DOMException.INVALID_STATE_ERR);
    }

    /**
     * Gets the max length.
     * @return the max length
     */
    @JsxGetter
    public int getMaxLength() {
        final String attrValue = getDomNodeOrDie().getAttribute("maxLength");
        return NumberUtils.toInt(attrValue, -1);
    }

    /**
     * Sets the value of {@code maxLength} attribute.
     * @param length the new value
     */
    @JsxSetter
    public void setMaxLength(final int length) {
        getDomNodeOrDie().setMaxLength(length);
    }

    /**
     * Gets the {@code minLength}.
     * @return the {@code minLength}
     */
    @JsxGetter
    public int getMinLength() {
        final String attrValue = getDomNodeOrDie().getAttribute("minLength");
        return NumberUtils.toInt(attrValue, -1);
    }

    /**
     * Sets the value of {@code minLength} attribute.
     * @param length the new value
     */
    @JsxSetter
    public void setMinLength(final int length) {
        getDomNodeOrDie().setMinLength(length);
    }

    /**
     * Gets the {@code min} property.
     * @return the {@code min} property
     */
    @JsxGetter
    public String getMin() {
        return getDomNodeOrDie().getAttributeDirect("min");
    }

    /**
     * Sets the {@code min} property.
     * @param min the {@code min} property
     */
    @JsxSetter
    public void setMin(final String min) {
        getDomNodeOrDie().setAttribute("min", min);
    }

    /**
     * Gets the {@code max} property.
     * @return the {@code max} property
     */
    @JsxGetter
    public String getMax() {
        return getDomNodeOrDie().getAttributeDirect("max");
    }

    /**
     * Sets the {@code max} property.
     * @param max the {@code max} property
     */
    @JsxSetter
    public void setMax(final String max) {
        getDomNodeOrDie().setAttribute("max", max);
    }

    /**
     * Gets the {@code step} property.
     * @return the {@code step} property
     */
    @JsxGetter
    public String getStep() {
        return getDomNodeOrDie().getAttributeDirect("step");
    }

    /**
     * Sets the {@code step} property.
     * @param step the {@code step} property
     */
    @JsxSetter
    public void setStep(final String step) {
        getDomNodeOrDie().setAttribute("step", step);
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
     * Returns the value of the {@code alt} property.
     * @return the value of the {@code alt} property
     */
    @JsxGetter
    public String getAlt() {
        return getDomNodeOrDie().getAttributeDirect("alt");
    }

    /**
     * Returns the value of the {@code alt} property.
     * @param alt the value
     */
    @JsxSetter
    public void setAlt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Returns the value of the {@code align} property.
     * @return the value of the {@code align} property
     */
    @JsxGetter
    public String getAlign() {
        return getAlign(true);
    }

    /**
     * Sets the value of the {@code align} property.
     * @param align the value of the {@code align} property
     */
    @JsxSetter
    public void setAlign(final String align) {
        setAlign(align, false);
    }

    /**
     * Returns the value of the {@code src} attribute.
     * @return the value of the {@code src} attribute
     */
    @JsxGetter
    public String getSrc() {
        return getDomNodeOrDie().getSrc();
    }

    /**
     * Sets the value of the {@code src} attribute.
     * @param src the new value
     */
    @JsxSetter
    public void setSrc(final String src) {
        getDomNodeOrDie().setSrcAttribute(src);
    }

    /**
     * Returns the value of the JavaScript attribute {@code value}.
     *
     * @return the value of this attribute
     */
    @JsxGetter
    @Override
    public String getValue() {
        final HtmlInput htmlInput = getDomNodeOrDie();

        if (htmlInput instanceof HtmlNumberInput) {
            final String valueAttr = htmlInput.getValue();
            if (!valueAttr.isEmpty()) {
                if (org.htmlunit.util.StringUtils.equalsChar('-', valueAttr)
                        || org.htmlunit.util.StringUtils.equalsChar('+', valueAttr)) {
                    return "";
                }

                final int lastPos = valueAttr.length() - 1;
                if (lastPos >= 0 && valueAttr.charAt(lastPos) == '.') {
                    if (htmlInput.hasFeature(JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE)) {
                        return "";
                    }
                }
                try {
                    Double.parseDouble(valueAttr);
                }
                catch (final NumberFormatException e) {
                    return "";
                }
            }
        }

        return htmlInput.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAttribute(final String attributeName) {
        final String superAttribute = super.getAttribute(attributeName);
        if (DomElement.VALUE_ATTRIBUTE.equalsIgnoreCase(attributeName)) {
            if ((superAttribute == null || !superAttribute.isEmpty())
                    && getDefaultValue().isEmpty()) {
                return null;
            }
            if (!"file".equals(getType())) {
                return getDefaultValue();
            }
        }
        return superAttribute;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void click() throws IOException {
        final HtmlInput domNode = getDomNodeOrDie();
        final boolean originalState = domNode.isChecked();

        domNode.click(false, false, false, false, false, true, false);

        final boolean newState = domNode.isChecked();

        if (originalState != newState
                && (domNode instanceof HtmlRadioButtonInput || domNode instanceof HtmlCheckBoxInput)) {
            domNode.fireEvent(Event.TYPE_CHANGE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return true;
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
     * Returns the {@code size} attribute.
     * @return the {@code size} attribute
     */
    @JsxGetter
    public String getSize() {
        return getDomNodeOrDie().getSize();
    }

    /**
     * Sets the {@code size} attribute.
     * @param size the new {@code size} value
     */
    @JsxSetter
    public void setSize(final String size) {
        getDomNodeOrDie().setSize(size);
    }

    /**
     * Returns the {@code accept} attribute.
     * @return the {@code accept} attribute
     */
    @JsxGetter
    public String getAccept() {
        return getDomNodeOrDie().getAccept();
    }

    /**
     * Sets the {@code accept} attribute.
     * @param accept the new {@code accept} value
     */
    @JsxSetter
    public void setAccept(final String accept) {
        getDomNodeOrDie().setAccept(accept);
    }

    /**
     * Returns the {@code autocomplete} attribute.
     * @return the {@code autocomplete} attribute
     */
    @JsxGetter
    public String getAutocomplete() {
        return getDomNodeOrDie().getAutocomplete();
    }

    /**
     * Sets the {@code autocomplete} attribute.
     * @param autocomplete the new {@code autocomplete} value
     */
    @JsxSetter
    public void setAutocomplete(final String autocomplete) {
        getDomNodeOrDie().setAutocomplete(autocomplete);
    }

    /**
     * Returns the {@code files} property.
     * @return the {@code files} property
     */
    @JsxGetter
    public FileList getFiles() {
        final HtmlInput htmlInput = getDomNodeOrDie();
        if (htmlInput instanceof HtmlFileInput) {
            final FileList list = new FileList(((HtmlFileInput) htmlInput).getFiles());
            list.setParentScope(getParentScope());
            list.setPrototype(getPrototype(list.getClass()));
            return list;
        }
        return null;
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
     * Returns the {@code width} property.
     * @return the {@code width} property
     */
    @JsxGetter
    public int getWidth() {
        final String value = getDomNodeOrDie().getAttributeDirect("width");
        final Integer intValue = HTMLCanvasElement.getValue(value);
        if (intValue != null) {
            return intValue;
        }
        return 0;
    }

    /**
     * Sets the {@code width} property.
     * @param width the {@code width} property
     */
    @JsxSetter
    public void setWidth(final int width) {
        getDomNodeOrDie().setAttribute("width", Integer.toString(width));
    }

    /**
     * Returns the {@code height} property.
     * @return the {@code height} property
     */
    @JsxGetter
    public int getHeight() {
        final String value = getDomNodeOrDie().getAttributeDirect("height");
        final Integer intValue = HTMLCanvasElement.getValue(value);
        if (intValue != null) {
            return intValue;
        }
        return 0;
    }

    /**
     * Sets the {@code height} property.
     * @param height the {@code height} property
     */
    @JsxSetter
    public void setHeight(final int height) {
        getDomNodeOrDie().setAttribute("height", Integer.toString(height));
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
     * @return whether the element is a candidate for constraint validation
     */
    @JsxGetter
    public boolean getWillValidate() {
        return getDomNodeOrDie().willValidate();
    }

    /**
     * Sets the custom validity message for the element to the specified message.
     * @param message the new message
     */
    @JsxFunction
    public void setCustomValidity(final String message) {
        getDomNodeOrDie().setCustomValidity(message);
    }

    /**
     * Returns the value of the property {@code formnovalidate}.
     * @return the value of this property
     */
    @JsxGetter
    public boolean isFormNoValidate() {
        return getDomNodeOrDie().isFormNoValidate();
    }

    /**
     * Sets the value of the property {@code formnovalidate}.
     * @param value the new value
     */
    @JsxSetter
    public void setFormNoValidate(final boolean value) {
        getDomNodeOrDie().setFormNoValidate(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DOMRectList getClientRects() {
        if ("hidden".equals(getType())) {
            final Window w = getWindow();
            final DOMRectList rectList = new DOMRectList();
            rectList.setParentScope(w);
            rectList.setPrototype(getPrototype(rectList.getClass()));

            return rectList;
        }

        return super.getClientRects();
    }
}
