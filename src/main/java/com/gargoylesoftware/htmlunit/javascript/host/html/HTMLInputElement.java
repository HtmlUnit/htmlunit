/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONCLICK_USES_POINTEREVENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_FILES_UNDEFINED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_FILE_SELECTION_START_END_NULL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_COLOR_NOT_SUPPORTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_DATETIME_LOCAL_SUPPORTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_DATETIME_SUPPORTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_MONTH_SUPPORTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_WEEK_SUPPORTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ALIGN_FOR_INPUT_IGNORES_VALUES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_NUMBER_SELECTION_START_END_NULL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_TYPE_LOWERCASE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_UNSUPORTED_TYPE_EXCEPTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SELECT_FILE_THROWS;
import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.TextRange;
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
 * @author Anton Demydenko
 */
@JsxClass(domClass = HtmlInput.class)
public class HTMLInputElement extends HTMLElement {

    /** "Live" labels collection; has to be a member to have equality (==) working. */
    private AbstractList labels_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLInputElement() {
    }

    /**
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @JsxGetter
    public String getType() {
        final BrowserVersion browserVersion = getBrowserVersion();
        String type = getDomNodeOrDie().getTypeAttribute();
        type = type.toLowerCase(Locale.ROOT);
        return isSupported(type, browserVersion) ? type : "text";
    }

    /**
     * Returns whether the specified type is supported or not.
     * @param type
     * @param browserVersion
     * @return whether the specified type is supported or not
     */
    private static boolean isSupported(final String type, final BrowserVersion browserVersion) {
        boolean supported = false;
        switch (type) {
            case "date":
                supported = browserVersion.hasFeature(HTMLINPUT_TYPE_DATETIME_SUPPORTED);
                break;
            case "datetime-local":
                supported = browserVersion.hasFeature(HTMLINPUT_TYPE_DATETIME_LOCAL_SUPPORTED);
                break;
            case "month":
                supported = browserVersion.hasFeature(HTMLINPUT_TYPE_MONTH_SUPPORTED);
                break;
            case "time":
                supported = browserVersion.hasFeature(HTMLINPUT_TYPE_DATETIME_SUPPORTED);
                break;
            case "week":
                supported = browserVersion.hasFeature(HTMLINPUT_TYPE_WEEK_SUPPORTED);
                break;
            case "color":
                supported = !browserVersion.hasFeature(HTMLINPUT_TYPE_COLOR_NOT_SUPPORTED);
                break;
            case "email":
            case "text":
            case "submit":
            case "checkbox":
            case "radio":
            case "hidden":
            case "password":
            case "image":
            case "reset":
            case "button":
            case "file":
            case "number":
            case "range":
            case "search":
            case "tel":
            case "url":
                supported = true;
                break;

            default:
        }
        return supported;
    }

    /**
     * Sets the value of the attribute {@code type}.
     * Note: this replace the DOM node with a new one.
     * @param newType the new type to set
     */
    @JsxSetter
    public void setType(final String newType) {
        setType(newType, false);
    }

    /**
     * Sets the value of the attribute {@code type}.
     * Note: this replace the DOM node with a new one.
     * @param newType the new type to set
     * @param setThroughAttribute set type value through setAttribute()
     */
    private void setType(String newType, final boolean setThroughAttribute) {
        HtmlInput input = getDomNodeOrDie();

        final String currentType = input.getAttributeDirect("type");

        final BrowserVersion browser = getBrowserVersion();
        if (!currentType.equalsIgnoreCase(newType)) {
            if (newType != null && browser.hasFeature(JS_INPUT_SET_TYPE_LOWERCASE)) {
                newType = newType.toLowerCase(Locale.ROOT);
            }

            if (!isSupported(newType.toLowerCase(Locale.ROOT), browser)) {
                if (setThroughAttribute) {
                    newType = "text";
                }
                else if (browser.hasFeature(JS_INPUT_SET_UNSUPORTED_TYPE_EXCEPTION)) {
                    throw Context.reportRuntimeError("Invalid argument '" + newType
                            + "' for setting property type.");
                }
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
                final SgmlPage page = input.getPage();
                final HtmlInput newInput = (HtmlInput) page.getWebClient().getPageCreator().getHtmlParser()
                        .getFactory(HtmlInput.TAG_NAME)
                        .createElement(page, HtmlInput.TAG_NAME, attributes);

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
            getDomNodeOrDie().setValueAttribute("");
            getDomNodeOrDie().valueModifiedByJavascript();
            return;
        }

        final String val = Context.toString(newValue);
        final BrowserVersion browserVersion = getBrowserVersion();
        if (StringUtils.isNotEmpty(val) && "file".equalsIgnoreCase(getType())) {
            if (browserVersion.hasFeature(JS_SELECT_FILE_THROWS)) {
                throw Context.reportRuntimeError("InvalidStateError: "
                        + "Failed to set the 'value' property on 'HTMLInputElement'.");
            }
            return;
        }

        getDomNodeOrDie().setValueAttribute(val);
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
     *@return the checked property
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
     * Uses {@link #setType(String)} if attribute's name is type to
     * replace DOM node as well as long as we have subclasses of {@link HtmlInput}.
     * {@inheritDoc}
     */
    @Override
    public void setAttribute(final String name, final String value) {
        if ("type".equalsIgnoreCase(name)) {
            setType(value, true);
            return;
        }
        if ("value".equalsIgnoreCase(name)) {
            setDefaultValue(value);
        }
        else {
            super.setAttribute(name, value);
        }
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
    @JsxGetter({FF, FF78})
    public int getTextLength() {
        return getValue().length();
    }

    /**
     * Gets the value of {@code selectionStart} attribute.
     * @return the selection start
     */
    @JsxGetter
    public Object getSelectionStart() {
        final DomNode dom = getDomNodeOrDie();
        if (dom instanceof SelectableTextInput) {
            if ("number".equalsIgnoreCase(getType())
                    && getBrowserVersion().hasFeature(JS_INPUT_NUMBER_SELECTION_START_END_NULL)) {
                return null;
            }

            return ((SelectableTextInput) dom).getSelectionStart();
        }

        if (getBrowserVersion().hasFeature(HTMLINPUT_FILE_SELECTION_START_END_NULL)) {
            return null;
        }
        throw Context.reportRuntimeError("Failed to read the 'selectionStart' property from 'HTMLInputElement': "
                + "The input element's type (" + getType() + ") does not support selection.");
    }

    /**
     * Sets the value of {@code selectionStart} attribute.
     * @param start selection start
     */
    @JsxSetter
    public void setSelectionStart(final int start) {
        final DomNode dom = getDomNodeOrDie();
        if (dom instanceof SelectableTextInput) {
            if ("number".equalsIgnoreCase(getType())
                    && getBrowserVersion().hasFeature(JS_INPUT_NUMBER_SELECTION_START_END_NULL)) {
                throw Context.reportRuntimeError("Failed to set the 'selectionStart' property"
                        + "from 'HTMLInputElement': "
                        + "The input element's type ('number') does not support selection.");
            }

            ((SelectableTextInput) dom).setSelectionStart(start);
            return;
        }

        throw Context.reportRuntimeError("Failed to set the 'selectionStart' property from 'HTMLInputElement': "
                + "The input element's type (" + getType() + ") does not support selection.");
    }

    /**
     * Gets the value of {@code selectionEnd} attribute.
     * @return the selection end
     */
    @JsxGetter
    public Object getSelectionEnd() {
        final DomNode dom = getDomNodeOrDie();
        if (dom instanceof SelectableTextInput) {
            if ("number".equalsIgnoreCase(getType())
                    && getBrowserVersion().hasFeature(JS_INPUT_NUMBER_SELECTION_START_END_NULL)) {
                return null;
            }

            return ((SelectableTextInput) dom).getSelectionEnd();
        }

        if (getBrowserVersion().hasFeature(HTMLINPUT_FILE_SELECTION_START_END_NULL)) {
            return null;
        }
        throw Context.reportRuntimeError("Failed to read the 'selectionEnd' property from 'HTMLInputElement': "
                + "The input element's type (" + getType() + ") does not support selection.");
    }

    /**
     * Sets the value of {@code selectionEnd} attribute.
     * @param end selection end
     */
    @JsxSetter
    public void setSelectionEnd(final int end) {
        final DomNode dom = getDomNodeOrDie();
        if (dom instanceof SelectableTextInput) {
            if ("number".equalsIgnoreCase(getType())
                    && getBrowserVersion().hasFeature(JS_INPUT_NUMBER_SELECTION_START_END_NULL)) {
                throw Context.reportRuntimeError("Failed to set the 'selectionEnd' property"
                        + "from 'HTMLInputElement': "
                        + "The input element's type ('number') does not support selection.");
            }

            ((SelectableTextInput) dom).setSelectionEnd(end);
            return;
        }

        throw Context.reportRuntimeError("Failed to set the 'selectionEnd' property from 'HTMLInputElement': "
                + "The input element's type (" + getType() + ") does not support selection.");
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
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public int getMinLength() {
        final String attrValue = getDomNodeOrDie().getAttribute("minLength");
        return NumberUtils.toInt(attrValue, -1);
    }

    /**
     * Sets the value of {@code minLength} attribute.
     * @param length the new value
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
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
     * Gets the {@code border} attribute.
     * @return the {@code border} attribute
     */
    @JsxGetter(IE)
    public String getBorder() {
        return getDomNodeOrDie().getAttributeDirect("border");
    }

    /**
     * Sets the {@code border} attribute.
     * @param border the {@code border} attribute
     */
    @JsxSetter(IE)
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the {@code align} property.
     * @return the value of the {@code align} property
     */
    @JsxGetter
    public String getAlign() {
        if (getBrowserVersion().hasFeature(JS_ALIGN_FOR_INPUT_IGNORES_VALUES)) {
            return "";
        }
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
        if (htmlInput instanceof HtmlFileInput) {
            final File[] files = ((HtmlFileInput) htmlInput).getFiles();
            if (files == null || files.length == 0) {
                return ATTRIBUTE_NOT_DEFINED;
            }
            final File first = files[0];
            final String name = first.getName();
            if (name.isEmpty()) {
                return name;
            }
            return "C:\\fakepath\\" + name;
        }
        return htmlInput.getAttributeDirect("value");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAttribute(final String attributeName, final Integer flags) {
        final String superAttribute = super.getAttribute(attributeName, flags);
        if ("value".equalsIgnoreCase(attributeName)) {
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
        final Event event;
        if (getBrowserVersion().hasFeature(EVENT_ONCLICK_USES_POINTEREVENT)) {
            event = new PointerEvent(domNode, MouseEvent.TYPE_CLICK, false, false, false, MouseEvent.BUTTON_LEFT);
        }
        else {
            event = new MouseEvent(domNode, MouseEvent.TYPE_CLICK, false, false, false, MouseEvent.BUTTON_LEFT);
        }
        domNode.click(event, event.isShiftKey(), event.isCtrlKey(), event.isAltKey(), true);

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
    public Object getFiles() {
        final HtmlInput htmlInput = getDomNodeOrDie();
        if (htmlInput instanceof HtmlFileInput) {
            final FileList list = new FileList(((HtmlFileInput) htmlInput).getFiles());
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
    @Override
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
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public AbstractList getLabels() {
        if (labels_ == null) {
            labels_ = new LabelsHelper(getDomNodeOrDie());
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
    @Override
    @JsxFunction(IE)
    public TextRange createTextRange() {
        return super.createTextRange();
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
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public boolean isDisabled() {
        return super.isDisabled();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxSetter({CHROME, EDGE, FF, FF78})
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
}
