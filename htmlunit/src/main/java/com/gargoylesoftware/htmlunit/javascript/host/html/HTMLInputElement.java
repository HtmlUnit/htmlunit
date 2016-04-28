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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONCLICK_USES_POINTEREVENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_FILES_UNDEFINED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ALIGN_FOR_INPUT_IGNORES_VALUES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_TYPE_LOWERCASE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_VALUE_EMAIL_TRIMMED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_VALUE_URL_TRIMMED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SELECT_FILE_THROWS;
import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.io.IOException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.InputElementFactory;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent;
import com.gargoylesoftware.htmlunit.javascript.host.file.FileList;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * The JavaScript object for {@code HTMLInputElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = HtmlInput.class)
public class HTMLInputElement extends FormField {

    /** "Live" labels collection; has to be a member to have equality (==) working. */
    private AbstractList labels_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public HTMLInputElement() {
    }

    /**
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @JsxGetter
    public String getType() {
        return getDomNodeOrDie().getTypeAttribute().toLowerCase(Locale.ROOT);
    }

    /**
     * Sets the value of the attribute {@code type}.
     * Note: this replace the DOM node with a new one.
     * @param newType the new type to set
     */
    @JsxSetter
    public void setType(String newType) {
        HtmlInput input = getDomNodeOrDie();

        final String currentType = input.getAttribute("type");

        if (!currentType.equalsIgnoreCase(newType)) {
            if (newType != null && getBrowserVersion().hasFeature(JS_INPUT_SET_TYPE_LOWERCASE)) {
                newType = newType.toLowerCase(Locale.ROOT);
            }
            final AttributesImpl attributes = readAttributes(input);
            final int index = attributes.getIndex("type");
            if (index > -1) {
                attributes.setValue(index, newType);
            }
            else {
                attributes.addAttribute(null, "type", "type", null, newType);
            }

            // create a new one only if we have a new type
            if (ATTRIBUTE_NOT_DEFINED != currentType || !"text".equalsIgnoreCase(newType)) {
                final HtmlInput newInput = (HtmlInput) InputElementFactory.instance
                    .createElement(input.getPage(), "input", attributes);

                if (input.wasCreatedByJavascript()) {
                    newInput.markAsCreatedByJavascript();
                }

                if (input.getParentNode() == null) {
                    // the input hasn't yet been inserted into the DOM tree (likely has been
                    // created via document.createElement()), so simply replace it with the
                    // new Input instance created in the code above
                    input = newInput;
                }
                else {
                    input.getParentNode().replaceChild(newInput, input);
                }

                input.setScriptableObject(null);
                setDomNode(newInput, true);
            }
            else {
                super.setAttribute("type", newType);
            }
        }
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
            getDomNodeOrDie().setAttribute("value", "");
            return;
        }

        String val = Context.toString(newValue);
        final BrowserVersion browserVersion = getBrowserVersion();
        if (StringUtils.isNotEmpty(val) && "file".equalsIgnoreCase(getType())) {
            if (browserVersion.hasFeature(JS_SELECT_FILE_THROWS)) {
                throw Context.reportRuntimeError("InvalidStateError: "
                        + "Failed to set the 'value' property on 'HTMLInputElement'.");
            }
            return;
        }

        if (StringUtils.isBlank(val)) {
            if ("email".equalsIgnoreCase(getType()) && browserVersion.hasFeature(JS_INPUT_SET_VALUE_EMAIL_TRIMMED))  {
                val = "";
            }
            else if ("url".equalsIgnoreCase(getType()) && browserVersion.hasFeature(JS_INPUT_SET_VALUE_URL_TRIMMED)) {
                val = "";
            }
        }

        getDomNodeOrDie().setAttribute("value", val);
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
     *@return the checked property
     */
    @JsxGetter
    public boolean getChecked() {
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
     * Uses {@link #setType(String)} if attribute's name is type to
     * replace DOM node as well as long as we have subclasses of {@link HtmlInput}.
     * {@inheritDoc}
     */
    @Override
    public void setAttribute(final String name, final String value) {
        if ("type".equals(name)) {
            setType(value);
            return;
        }
        super.setAttribute(name, value);
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
    public boolean getDefaultChecked() {
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
    @JsxGetter(@WebBrowser(FF))
    public int getTextLength() {
        return getValue().length();
    }

    /**
     * Gets the value of {@code selectionStart} attribute.
     * @return the selection start
     */
    @JsxGetter
    public int getSelectionStart() {
        return ((SelectableTextInput) getDomNodeOrDie()).getSelectionStart();
    }

    /**
     * Sets the value of {@code selectionStart} attribute.
     * @param start selection start
     */
    @JsxSetter
    public void setSelectionStart(final int start) {
        ((SelectableTextInput) getDomNodeOrDie()).setSelectionStart(start);
    }

    /**
     * Gets the value of {@code selectionEnd} attribute.
     * @return the selection end
     */
    @JsxGetter
    public int getSelectionEnd() {
        return ((SelectableTextInput) getDomNodeOrDie()).getSelectionEnd();
    }

    /**
     * Sets the value of {@code selectionEnd} attribute.
     * @param end selection end
     */
    @JsxSetter
    public void setSelectionEnd(final int end) {
        ((SelectableTextInput) getDomNodeOrDie()).setSelectionEnd(end);
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
    @JsxGetter(@WebBrowser(CHROME))
    public int getMinLength() {
        final String attrValue = getDomNodeOrDie().getAttribute("minLength");
        return NumberUtils.toInt(attrValue, -1);
    }

    /**
     * Sets the value of {@code minLength} attribute.
     * @param length the new value
     */
    @JsxSetter(@WebBrowser(CHROME))
    public void setMinLength(final int length) {
        getDomNodeOrDie().setMinLength(length);
    }

    /**
     * Gets the {@code min} property.
     * @return the {@code min} property
     */
    @JsxGetter
    public String getMin() {
        return getDomNodeOrDie().getAttribute("min");
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
        return getDomNodeOrDie().getAttribute("max");
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
     * Gets the value of {@code readOnly} attribute.
     * @return the readOnly attribute
     */
    @JsxGetter
    public boolean getReadOnly() {
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
        return getDomNodeOrDie().getAttribute("alt");
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
     * Gets the {@code border} attribute.
     * @return the {@code border} attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getBorder() {
        return getDomNodeOrDie().getAttribute("border");
    }

    /**
     * Sets the {@code border} attribute.
     * @param border the {@code border} attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
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
        final boolean ignoreIfNoError = getBrowserVersion().hasFeature(JS_ALIGN_FOR_INPUT_IGNORES_VALUES);
        setAlign(align, ignoreIfNoError);
    }

    /**
     * Returns the value of the {@code src} attribute.
     * @return the value of the {@code src} attribute
     */
    @JsxGetter
    public String getSrc() {
        return getDomNodeOrDie().getSrcAttribute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void click() throws IOException {
        final HtmlInput domNode = getDomNodeOrDie();
        final boolean originalState = domNode.isChecked();
        final Event event;
        if (getBrowserVersion().hasFeature(EVENT_ONCLICK_USES_POINTEREVENT)) {
            event = new PointerEvent(domNode, MouseEvent.TYPE_CLICK, false, false, false, MouseEvent.BUTTON_LEFT);
        }
        else {
            event = new MouseEvent(domNode, MouseEvent.TYPE_CLICK, false, false, false, MouseEvent.BUTTON_LEFT);
        }
        domNode.click(event);

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
     * Returns the {@code required} attribute.
     * @return the {@code required} attribute
     */
    @JsxGetter
    public boolean isRequired() {
        return getDomNodeOrDie().isRequired();
    }

    /**
     * Sets the {@code required} attribute.
     * @param required the new attribute value
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
    public Object getFiles() {
        if (getDomNodeOrDie() instanceof HtmlFileInput) {
            final FileList list = new FileList(HtmlFileInput.splitFiles(getValue()));
            list.setParentScope(getParentScope());
            list.setPrototype(getPrototype(list.getClass()));
            return list;
        }
        if (getBrowserVersion().hasFeature(HTMLINPUT_FILES_UNDEFINED)) {
            return Undefined.instance;
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
    @Override
    @JsxGetter
    public int getWidth() {
        final String value = getDomNodeOrDie().getAttribute("width");
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
    @Override
    @JsxGetter
    public int getHeight() {
        final String value = getDomNodeOrDie().getAttribute("height");
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
    @JsxGetter(@WebBrowser(CHROME))
    public AbstractList getLabels() {
        if (labels_ == null) {
            labels_ = new LabelsHelper(getDomNodeOrDie());
        }
        return labels_;
    }

}
