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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_MOUSE_ON_DISABLED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_ATTRIBUTE_MIN_MAX_LENGTH_SUPPORTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_DOES_NOT_CLICK_SURROUNDING_ANCHOR;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.AbstractJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.regexp.RegExpJsToJavaConverter;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Wrapper for the HTML element "input".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Anton Demydenko
 */
public abstract class HtmlInput extends HtmlElement implements DisabledElement, SubmittableElement,
    FormFieldWithNameHistory {
    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "input";

    private String defaultValue_;
    private String originalName_;
    private Collection<String> newNames_ = Collections.emptySet();
    private boolean createdByJavascript_;
    private boolean valueModifiedByJavascript_;
    private Object valueAtFocus_;

    /**
     * Creates an instance.
     *
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    public HtmlInput(final SgmlPage page, final Map<String, DomAttr> attributes) {
        this(TAG_NAME, page, attributes);
    }

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    public HtmlInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
        defaultValue_ = getValueAttribute();
        originalName_ = getNameAttribute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttribute(final String attributeName, final String attributeValue) {
        if ("value".equals(attributeName)) {
            setValueAttribute(attributeValue);
        }
        else {
            super.setAttribute(attributeName, attributeValue);
        }
    }

    /**
     * Sets the content of the {@code value} attribute.
     *
     * @param newValue the new value
     */
    public void setValueAttribute(final String newValue) {
        WebAssert.notNull("newValue", newValue);
        super.setAttribute("value", newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameValuePair[] getSubmitNameValuePairs() {
        return new NameValuePair[]{new NameValuePair(getNameAttribute(), getValueAttribute())};
    }

    /**
     * Returns the value of the attribute {@code type}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code type} or an empty string if that attribute isn't defined
     */
    public final String getTypeAttribute() {
        final String type = getAttributeDirect("type");
        if (ATTRIBUTE_NOT_DEFINED == type) {
            return "text";
        }
        return type;
    }

    /**
     * Returns the value of the attribute {@code name}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code name} or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttributeDirect("name");
    }

    /**
     * <p>Return the value of the attribute "value". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.</p>
     *
     * @return the value of the attribute {@code value} or an empty string if that attribute isn't defined
     */
    public final String getValueAttribute() {
        return getAttributeDirect("value");
    }

    /**
     * Returns the value of the attribute {@code checked}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code checked} or an empty string if that attribute isn't defined
     */
    public final String getCheckedAttribute() {
        return getAttributeDirect("checked");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDisabledAttribute() {
        return getAttributeDirect("disabled");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isDisabled() {
        return hasAttribute("disabled");
    }

    /**
     * Returns the value of the attribute {@code readonly}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code readonly}
     * or an empty string if that attribute isn't defined.
     */
    public final String getReadOnlyAttribute() {
        return getAttributeDirect("readonly");
    }

    /**
     * Returns the value of the attribute {@code size}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code size}
     * or an empty string if that attribute isn't defined.
     */
    public final String getSizeAttribute() {
        return getAttributeDirect("size");
    }

    /**
     * Returns the value of the attribute {@code maxlength}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code maxlength}
     * or an empty string if that attribute isn't defined.
     */
    public final String getMaxLengthAttribute() {
        return getAttribute("maxLength");
    }

    /**
     * Gets the max length if defined, Integer.MAX_VALUE if none.
     * @return the max length
     */
    protected int getMaxLength() {
        final String maxLength = getMaxLengthAttribute();
        if (maxLength.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        try {
            return Integer.parseInt(maxLength.trim());
        }
        catch (final NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Returns the value of the attribute {@code minlength}. Refer to the
     * <a href='https://www.w3.org/TR/html5/sec-forms.html'>HTML 5</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code minlength}
     * or an empty string if that attribute isn't defined.
     */
    public final String getMinLengthAttribute() {
        return getAttribute("minLength");
    }

    /**
     * Gets the min length if defined, Integer.MIN_VALUE if none.
     * @return the min length
     */
    protected int getMinLength() {
        final String minLength = getMinLengthAttribute();
        if (minLength.isEmpty()) {
            return Integer.MIN_VALUE;
        }

        try {
            return Integer.parseInt(minLength.trim());
        }
        catch (final NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Returns the value of the attribute {@code src}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code src}
     * or an empty string if that attribute isn't defined.
     */
    public String getSrcAttribute() {
        return getSrcAttributeNormalized();
    }

    /**
     * Sets the {@code src} attribute.
     *
     * @param src the {@code src} attribute
     */
    public void setSrcAttribute(final String src) {
        setAttribute(HtmlElement.SRC_ATTRIBUTE, src);
    }

    /**
     * Returns the value of the attribute {@code alt}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code alt}
     * or an empty string if that attribute isn't defined.
     */
    public final String getAltAttribute() {
        return getAttributeDirect("alt");
    }

    /**
     * Returns the value of the attribute {@code usemap}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code usemap}
     * or an empty string if that attribute isn't defined.
     */
    public final String getUseMapAttribute() {
        return getAttributeDirect("usemap");
    }

    /**
     * Returns the value of the attribute {@code tabindex}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code tabindex}
     * or an empty string if that attribute isn't defined.
     */
    public final String getTabIndexAttribute() {
        return getAttributeDirect("tabindex");
    }

    /**
     * Returns the value of the attribute {@code accesskey}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code accesskey}
     * or an empty string if that attribute isn't defined.
     */
    public final String getAccessKeyAttribute() {
        return getAttributeDirect("accesskey");
    }

    /**
     * Returns the value of the attribute {@code onfocus}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onfocus}
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnFocusAttribute() {
        return getAttributeDirect("onfocus");
    }

    /**
     * Returns the value of the attribute {@code onblur}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onblur}
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnBlurAttribute() {
        return getAttributeDirect("onblur");
    }

    /**
     * Returns the value of the attribute {@code onselect}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onselect}
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnSelectAttribute() {
        return getAttributeDirect("onselect");
    }

    /**
     * Returns the value of the attribute {@code onchange}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onchange}
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnChangeAttribute() {
        return getAttributeDirect("onchange");
    }

    /**
     * Returns the value of the attribute {@code accept}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code accept}
     * or an empty string if that attribute isn't defined.
     */
    public final String getAcceptAttribute() {
        return getAttribute(HttpHeader.ACCEPT_LC);
    }

    /**
     * Returns the value of the attribute {@code align}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code align}
     * or an empty string if that attribute isn't defined.
     */
    public final String getAlignAttribute() {
        return getAttributeDirect("align");
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#reset()
     */
    @Override
    public void reset() {
        setValueAttribute(defaultValue_);
    }

    /**
     * {@inheritDoc}
     *
     * @see SubmittableElement#setDefaultValue(String)
     */
    @Override
    public void setDefaultValue(final String defaultValue) {
        setDefaultValue(defaultValue, true);
    }

    /**
     * Sets the default value, optionally also modifying the current value.
     * @param defaultValue the new default value
     * @param modifyValue Whether or not to set the current value to the default value
     */
    protected void setDefaultValue(final String defaultValue, final boolean modifyValue) {
        final String oldAttributeValue = defaultValue_;
        final HtmlAttributeChangeEvent event;
        if (defaultValue_ == ATTRIBUTE_NOT_DEFINED) {
            event = new HtmlAttributeChangeEvent(this, "value", defaultValue);
        }
        else {
            event = new HtmlAttributeChangeEvent(this, "value", oldAttributeValue);
        }

        defaultValue_ = defaultValue;
        if (modifyValue) {
            if (this instanceof HtmlFileInput) {
                super.setAttribute("value", defaultValue);
            }
            else {
                setValueAttribute(defaultValue);
            }
        }
        notifyAttributeChangeListeners(event, this, oldAttributeValue, true);
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#getDefaultValue()
     */
    @Override
    public String getDefaultValue() {
        return defaultValue_;
    }

    /**
     * {@inheritDoc} The default implementation returns {@code false}; only checkboxes and
     * radio buttons really care what the default checked value is.
     * @see SubmittableElement#isDefaultChecked()
     * @see HtmlRadioButtonInput#isDefaultChecked()
     * @see HtmlCheckBoxInput#isDefaultChecked()
     */
    @Override
    public boolean isDefaultChecked() {
        return false;
    }

    /**
     * Sets the {@code checked} attribute, returning the page that occupies this input's window after setting
     * the attribute. Note that the returned page may or may not be the original page, depending on
     * the presence of JavaScript event handlers, etc.
     *
     * @param isChecked {@code true} if this element is to be selected
     * @return the page that occupies this input's window after setting the attribute
     */
    public Page setChecked(final boolean isChecked) {
        // By default this returns the current page. Derived classes will override.
        return getPage();
    }

    /**
     * Sets the {@code readOnly} attribute.
     *
     * @param isReadOnly {@code true} if this element is read only
     */
    public void setReadOnly(final boolean isReadOnly) {
        if (isReadOnly) {
            setAttribute("readOnly", "readOnly");
        }
        else {
            removeAttribute("readOnly");
        }
    }

    /**
     * Returns {@code true} if this element is currently selected.
     * @return {@code true} if this element is currently selected
     */
    public boolean isChecked() {
        return hasAttribute("checked");
    }

    /**
     * Returns {@code true} if this element is read only.
     * @return {@code true} if this element is read only
     */
    public boolean isReadOnly() {
        return hasAttribute("readOnly");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean propagateClickStateUpdateToParent() {
        return !hasFeature(HTMLINPUT_DOES_NOT_CLICK_SURROUNDING_ANCHOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(final Event event) {
        if (event instanceof MouseEvent && hasFeature(EVENT_MOUSE_ON_DISABLED)) {
            return true;
        }

        return super.handles(event);
    }

    /**
     * Executes the onchange script code for this element if this is appropriate.
     * This means that the element must have an onchange script, script must be enabled
     * and the change in the element must not have been triggered by a script.
     *
     * @param htmlElement the element that contains the onchange attribute
     * @return the page that occupies this window after this method completes (may or
     *         may not be the same as the original page)
     */
    static Page executeOnChangeHandlerIfAppropriate(final HtmlElement htmlElement) {
        final SgmlPage page = htmlElement.getPage();
        final WebClient webClient = page.getWebClient();

        if (!webClient.isJavaScriptEngineEnabled()) {
            return page;
        }

        final AbstractJavaScriptEngine<?> engine = webClient.getJavaScriptEngine();
        if (engine.isScriptRunning()) {
            return page;
        }
        final ScriptResult scriptResult = htmlElement.fireEvent(Event.TYPE_CHANGE);

        if (webClient.containsWebWindow(page.getEnclosingWindow())) {
            // may be itself or a newly loaded one
            return page.getEnclosingWindow().getEnclosedPage();
        }

        if (scriptResult != null) {
            // current window doesn't exist anymore
            return webClient.getCurrentWindow().getEnclosedPage();
        }

        return page;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObservers) {
        if ("name".equals(qualifiedName)) {
            if (newNames_.isEmpty()) {
                newNames_ = new HashSet<>();
            }
            newNames_.add(attributeValue);
        }
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners,
                notifyMutationObservers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOriginalName() {
        return originalName_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getNewNames() {
        return newNames_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Marks this frame as created by javascript. This is needed to handle
     * some special IE behavior.
     */
    public void markAsCreatedByJavascript() {
        createdByJavascript_ = true;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns true if this frame was created by javascript. This is needed to handle
     * some special IE behavior.
     * @return true or false
     */
    public boolean wasCreatedByJavascript() {
        return createdByJavascript_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Marks this element as modified (value) by javascript. This is needed
     * to support maxlength/minlength validation.
     */
    public void valueModifiedByJavascript() {
        valueModifiedByJavascript_ = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void focus() {
        super.focus();
        // store current value to trigger onchange when needed at focus lost
        valueAtFocus_ = getInternalValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void removeFocus() {
        super.removeFocus();

        if (valueAtFocus_ != null && !valueAtFocus_.equals(getInternalValue())) {
            handleFocusLostValueChanged();
        }
        valueAtFocus_ = null;
    }

    void handleFocusLostValueChanged() {
        executeOnChangeHandlerIfAppropriate(this);
    }

    Object getInternalValue() {
        return getValueAttribute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.INLINE_BLOCK;
    }

    /**
     * Returns the value of the {@code size} attribute.
     *
     * @return the value of the {@code size} attribute
     */
    public String getSize() {
        return getAttributeDirect("size");
    }

    /**
     * Sets the {@code size} attribute.
     *
     * @param size the {@code size} attribute
     */
    public void setSize(final String size) {
        setAttribute("size", size);
    }

    /**
     * Sets the {@code maxLength} attribute.
     *
     * @param maxLength the {@code maxLength} attribute
     */
    public void setMaxLength(final int maxLength) {
        setAttribute("maxLength", String.valueOf(maxLength));
    }

    /**
     * Sets the {@code minLength} attribute.
     *
     * @param minLength the {@code minLength} attribute
     */
    public void setMinLength(final int minLength) {
        setAttribute("minLength", String.valueOf(minLength));
    }

    /**
     * Returns the value of the {@code accept} attribute.
     *
     * @return the value of the {@code accept} attribute
     */
    public String getAccept() {
        return getAttribute(HttpHeader.ACCEPT_LC);
    }

    /**
     * Sets the {@code accept} attribute.
     *
     * @param accept the {@code accept} attribute
     */
    public void setAccept(final String accept) {
        setAttribute(HttpHeader.ACCEPT_LC, accept);
    }

    /**
     * Returns the value of the {@code autocomplete} attribute.
     *
     * @return the value of the {@code autocomplete} attribute
     */
    public String getAutocomplete() {
        return getAttributeDirect("autocomplete");
    }

    /**
     * Sets the {@code autocomplete} attribute.
     *
     * @param autocomplete the {@code autocomplete} attribute
     */
    public void setAutocomplete(final String autocomplete) {
        setAttribute("autocomplete", autocomplete);
    }

    /**
     * Returns the value of the {@code placeholder} attribute.
     *
     * @return the value of the {@code placeholder} attribute
     */
    public String getPlaceholder() {
        return getAttributeDirect("placeholder");
    }

    /**
     * Sets the {@code placeholder} attribute.
     *
     * @param placeholder the {@code placeholder} attribute
     */
    public void setPlaceholder(final String placeholder) {
        setAttribute("placeholder", placeholder);
    }

    /**
     * Returns the value of the {@code pattern} attribute.
     *
     * @return the value of the {@code pattern} attribute
     */
    public String getPattern() {
        return getAttributeDirect("pattern");
    }

    /**
     * Sets the {@code pattern} attribute.
     *
     * @param pattern the {@code pattern} attribute
     */
    public void setPattern(final String pattern) {
        setAttribute("pattern", pattern);
    }

    /**
     * Returns the value of the {@code min} attribute.
     *
     * @return the value of the {@code min} attribute
     */
    public String getMin() {
        return getAttributeDirect("min");
    }

    /**
     * Sets the {@code min} attribute.
     *
     * @param min the {@code min} attribute
     */
    public void setMin(final String min) {
        setAttribute("min", min);
    }

    /**
     * Returns the value of the {@code max} attribute.
     *
     * @return the value of the {@code max} attribute
     */
    public String getMax() {
        return getAttributeDirect("max");
    }

    /**
     * Sets the {@code max} attribute.
     *
     * @param max the {@code max} attribute
     */
    public void setMax(final String max) {
        setAttribute("max", max);
    }

    /**
     * Returns the value of the {@code step} attribute.
     *
     * @return the value of the {@code step} attribute
     */
    public String getStep() {
        return getAttributeDirect("step");
    }

    /**
     * Sets the {@code step} attribute.
     *
     * @param step the {@code step} attribute
     */
    public void setStep(final String step) {
        setAttribute("step", step);
    }

    @Override
    public boolean isValid() {
        return super.isValid() && isMaxLengthValid() && isMinLengthValid() && isPatternValid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isRequiredSupported() {
        return true;
    }

    /**
     * Returns if the input element supports pattern validation. Refer to the
     * <a href='https://www.w3.org/TR/html5/sec-forms.html'>HTML 5</a> documentation
     * for details.
     * @return if the input element supports pattern validation
     */
    protected boolean isPatternSupported() {
        return false;
    }

    /**
     * @return if the element executes pattern validation on blank strings
     */
    protected boolean isBlankPatternValidated() {
        return true;
    }

    /**
     * Returns if the input element supports maxlength minlength validation. Refer to the
     * <a href='https://www.w3.org/TR/html5/sec-forms.html'>HTML 5</a> documentation
     * for details.
     * @return if the input element supports pattern validation
     */
    protected boolean isMinMaxLengthSupported() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode cloneNode(final boolean deep) {
        final HtmlInput newnode = (HtmlInput) super.cloneNode(deep);
        newnode.newNames_ = new HashSet<>(newNames_);

        return newnode;
    }

    /**
     * Returns if the input element has a maximum allowed value length. Refer to the
     * <a href='https://www.w3.org/TR/html5/sec-forms.html'>HTML 5</a>
     * documentation for details.
     *
     * @return if the input element has a maximum allowed value length
     */
    private boolean isMaxLengthValid() {
        if (!isMinMaxLengthSupported()
                || valueModifiedByJavascript_
                || !hasFeature(HTMLINPUT_ATTRIBUTE_MIN_MAX_LENGTH_SUPPORTED)
                || getMaxLength() == Integer.MAX_VALUE) {
            return true;
        }
        else {
            return getValueAttribute().length() <= getMaxLength();
        }
    }

    /**
     * Returns if the input element has a minimum allowed value length. Refer to the
     * <a href='https://www.w3.org/TR/html5/sec-forms.html'>HTML 5</a>
     * documentation for details.
     *
     * @return if the input element has a minimum allowed value length
     */
    private boolean isMinLengthValid() {
        if (!isMinMaxLengthSupported()
                || valueModifiedByJavascript_
                || !hasFeature(HTMLINPUT_ATTRIBUTE_MIN_MAX_LENGTH_SUPPORTED)
                || getMinLength() == Integer.MIN_VALUE) {
            return true;
        }
        else {
            return getValueAttribute().length() >= getMinLength();
        }
    }

    /**
     * Returns if the input element has a valid value pattern. Refer to the
     * <a href='https://www.w3.org/TR/html5/sec-forms.html'>HTML 5</a> documentation
     * for details.
     *
     * @return if the input element has a valid value pattern
     */
    private boolean isPatternValid() {
        if (!isPatternSupported()) {
            return true;
        }

        final String pattern = getPattern();
        if (StringUtils.isEmpty(pattern)) {
            return true;
        }

        final String value = getValueAttribute();
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        if (!isBlankPatternValidated() && StringUtils.isBlank(value)) {
            return true;
        }

        final RegExpJsToJavaConverter converter = new RegExpJsToJavaConverter();
        final String javaPattern = converter.convert(pattern);
        try {
            return Pattern.matches(javaPattern, value);
        }
        catch (final Exception e) {
            // ignore if regex invalid
        }
        return true;
    }

}
