/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.html;

import static org.htmlunit.BrowserVersionFeatures.EVENT_MOUSE_ON_DISABLED;
import static org.htmlunit.BrowserVersionFeatures.HTMLINPUT_ATTRIBUTE_MIN_MAX_LENGTH_SUPPORTED;
import static org.htmlunit.BrowserVersionFeatures.HTMLINPUT_DOES_NOT_CLICK_SURROUNDING_ANCHOR;
import static org.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_COLOR_NOT_SUPPORTED;
import static org.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_DATETIME_LOCAL_SUPPORTED;
import static org.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_DATETIME_SUPPORTED;
import static org.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_IMAGE_IGNORES_CUSTOM_VALIDITY;
import static org.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_MONTH_SUPPORTED;
import static org.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_WEEK_SUPPORTED;
import static org.htmlunit.BrowserVersionFeatures.JS_INPUT_CHANGE_TYPE_DROPS_VALUE;
import static org.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_TYPE_LOWERCASE;
import static org.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_UNSUPORTED_TYPE_EXCEPTION;
import static org.htmlunit.html.HtmlForm.ATTRIBUTE_FORMNOVALIDATE;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.BrowserVersion;
import org.htmlunit.HttpHeader;
import org.htmlunit.Page;
import org.htmlunit.ScriptResult;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebClient;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.MouseEvent;
import org.htmlunit.javascript.host.html.HTMLInputElement;
import org.htmlunit.javascript.regexp.RegExpJsToJavaConverter;
import org.htmlunit.util.NameValuePair;
import org.xml.sax.helpers.AttributesImpl;

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
 * @author Ronny Shapiro
 */
public abstract class HtmlInput extends HtmlElement implements DisabledElement, SubmittableElement,
    FormFieldWithNameHistory, ValidatableElement  {

    private static final Log LOG = LogFactory.getLog(HtmlInput.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "input";

    private String rawValue_;
    private boolean isValueDirty_;
    private final String originalName_;
    private Collection<String> newNames_ = Collections.emptySet();
    private boolean valueModifiedByJavascript_;
    private Object valueAtFocus_;
    private String customValidity_;

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
        rawValue_ = getValueAttribute();
        originalName_ = getNameAttribute();
    }

    /**
     * Sets the content of the {@code value} attribute.
     *
     * @param newValue the new value
     */
    public void setValueAttribute(final String newValue) {
        super.setAttribute(VALUE_ATTRIBUTE, newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameValuePair[] getSubmitNameValuePairs() {
        return new NameValuePair[]{new NameValuePair(getNameAttribute(), getValue())};
    }

    /**
     * Returns the value of the attribute {@code type}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code type} or an empty string if that attribute isn't defined
     */
    public final String getTypeAttribute() {
        final String type = getAttributeDirect(DomElement.TYPE_ATTRIBUTE);
        if (ATTRIBUTE_NOT_DEFINED == type) {
            return "text";
        }
        return type;
    }

    /**
     * Returns the value of the attribute {@code name}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code name} or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttributeDirect(NAME_ATTRIBUTE);
    }

    /**
     * <p>Return the value of the attribute "value". Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.</p>
     *
     * @return the value of the attribute {@code value} or an empty string if that attribute isn't defined
     */
    public final String getValueAttribute() {
        return getAttributeDirect(VALUE_ATTRIBUTE);
    }

    /**
     * @return the value
     */
    public String getValue() {
        return getRawValue();
    }

    /**
     * Sets the value.
     *
     * @param newValue the new value
     */
    public void setValue(final String newValue) {
        setRawValue(newValue);
        isValueDirty_ = true;
    }

    protected void valueAttributeChanged(final String attributeValue, final boolean isValueDirty) {
        if (!isValueDirty_) {
            setRawValue(attributeValue);
        }
    }

    /**
     * Returns the value of the attribute {@code checked}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code checked} or an empty string if that attribute isn't defined
     */
    public final String getCheckedAttribute() {
        return getAttributeDirect(ATTRIBUTE_CHECKED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDisabledAttribute() {
        return getAttributeDirect(ATTRIBUTE_DISABLED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isDisabled() {
        return hasAttribute(ATTRIBUTE_DISABLED);
    }

    /**
     * Returns the value of the attribute {@code readonly}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * <a href="https://www.w3.org/TR/html5/sec-forms.html">HTML 5</a>
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code src}
     * or an empty string if that attribute isn't defined.
     */
    public String getSrcAttribute() {
        return getSrcAttributeNormalized();
    }

    /**
     * Returns the value of the {@code src} value.
     * @return the value of the {@code src} value
     */
    public String getSrc() {
        final String src = getSrcAttributeNormalized();
        if (ATTRIBUTE_NOT_DEFINED == src) {
            return src;
        }

        final HtmlPage page = getHtmlPageOrNull();
        if (page != null) {
            try {
                return page.getFullyQualifiedUrl(src).toExternalForm();
            }
            catch (final MalformedURLException e) {
                // Log the error and fall through to the return values below.
                LOG.warn(e.getMessage(), e);
            }
        }
        return src;
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
        setValue(getDefaultValue());
        isValueDirty_ = true;
    }

    /**
     * {@inheritDoc}
     *
     * @see SubmittableElement#setDefaultValue(String)
     */
    @Override
    public void setDefaultValue(final String defaultValue) {
        setValueAttribute(defaultValue);
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#getDefaultValue()
     */
    @Override
    public String getDefaultValue() {
        return getValueAttribute();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @return the raw value
     */
    public String getRawValue() {
        return rawValue_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Update the raw value.
     * @param rawValue the new raw value
     */
    public void setRawValue(final String rawValue) {
        rawValue_ = rawValue;
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
        return hasAttribute(ATTRIBUTE_CHECKED);
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
        final String qualifiedNameLC = org.htmlunit.util.StringUtils.toRootLowerCaseWithCache(qualifiedName);
        if (NAME_ATTRIBUTE.equals(qualifiedNameLC)) {
            if (newNames_.isEmpty()) {
                newNames_ = new HashSet<>();
            }
            newNames_.add(attributeValue);
        }

        if (TYPE_ATTRIBUTE.equals(qualifiedNameLC)) {
            changeType(attributeValue, true);
            return;
        }

        if (VALUE_ATTRIBUTE.equals(qualifiedNameLC)) {
            super.setAttributeNS(namespaceURI, qualifiedNameLC, attributeValue, notifyAttributeChangeListeners,
                    notifyMutationObservers);

            valueAttributeChanged(attributeValue, isValueDirty_);
            return;
        }

        super.setAttributeNS(namespaceURI, qualifiedNameLC, attributeValue, notifyAttributeChangeListeners,
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

    protected Object getInternalValue() {
        return getRawValue();
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
        return !isValueMissingValidityState()
                && isCustomValidityValid()
                && isMaxLengthValid() && isMinLengthValid()
                && !hasPatternMismatchValidityState();
    }

    protected boolean isCustomValidityValid() {
        if (isCustomErrorValidityState()) {
            final String type = getAttributeDirect(DomElement.TYPE_ATTRIBUTE).toLowerCase(Locale.ROOT);
            if (!"button".equals(type)
                    && !"hidden".equals(type)
                    && !"reset".equals(type)
                    && !("image".equals(type) && hasFeature(HTMLINPUT_TYPE_IMAGE_IGNORES_CUSTOM_VALIDITY))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean isRequiredSupported() {
        return true;
    }

    /**
     * Returns if the input element supports pattern validation. Refer to the
     * <a href="https://www.w3.org/TR/html5/sec-forms.html">HTML 5</a> documentation
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
     * <a href="https://www.w3.org/TR/html5/sec-forms.html">HTML 5</a> documentation
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
     * <a href="https://www.w3.org/TR/html5/sec-forms.html">HTML 5</a>
     * documentation for details.
     *
     * @return if the input element has a maximum allowed value length
     */
    private boolean isMaxLengthValid() {
        if (!isMinMaxLengthSupported()
                || valueModifiedByJavascript_
                || !hasFeature(HTMLINPUT_ATTRIBUTE_MIN_MAX_LENGTH_SUPPORTED)
                || getMaxLength() == Integer.MAX_VALUE
                || getDefaultValue().equals(getValue())) {
            return true;
        }

        return getValue().length() <= getMaxLength();
    }

    /**
     * Returns if the input element has a minimum allowed value length. Refer to the
     * <a href="https://www.w3.org/TR/html5/sec-forms.html">HTML 5</a>
     * documentation for details.
     *
     * @return if the input element has a minimum allowed value length
     */
    private boolean isMinLengthValid() {
        if (!isMinMaxLengthSupported()
                || valueModifiedByJavascript_
                || !hasFeature(HTMLINPUT_ATTRIBUTE_MIN_MAX_LENGTH_SUPPORTED)
                || getMinLength() == Integer.MIN_VALUE
                || getDefaultValue().equals(getValue())) {
            return true;
        }

        return getValue().length() >= getMinLength();
    }

    /**
     * Returns if the input element has a valid value pattern. Refer to the
     * <a href="https://www.w3.org/TR/html5/sec-forms.html">HTML 5</a> documentation
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

        final String value = getValue();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean willValidate() {
        return !isDisabled() && !isReadOnly();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCustomValidity(final String message) {
        customValidity_ = message;
    }

    /**
     * @return whether this is a checkbox or a radio button
     */
    public boolean isCheckable() {
        final String type = getAttributeDirect(DomElement.TYPE_ATTRIBUTE).toLowerCase(Locale.ROOT);
        return "radio".equals(type) || "checkbox".equals(type);
    }

    /**
     * @return false for type submit/resest/image/button otherwise true
     */
    public boolean isSubmitable() {
        final String type = getAttributeDirect(DomElement.TYPE_ATTRIBUTE).toLowerCase(Locale.ROOT);
        return !"submit".equals(type) && !"image".equals(type) && !"reset".equals(type) && !"button".equals(type);
    }

    @Override
    public boolean hasBadInputValidityState() {
        return false;
    }

    @Override
    public boolean isCustomErrorValidityState() {
        return !StringUtils.isEmpty(customValidity_);
    }

    @Override
    public boolean hasPatternMismatchValidityState() {
        return !isPatternValid();
    }

    @Override
    public boolean isStepMismatchValidityState() {
        return false;
    }

    @Override
    public boolean isTooLongValidityState() {
        return false;
    }

    @Override
    public boolean isTooShortValidityState() {
        if (!isMinMaxLengthSupported()
                || valueModifiedByJavascript_
                || !hasFeature(HTMLINPUT_ATTRIBUTE_MIN_MAX_LENGTH_SUPPORTED)
                || getMinLength() == Integer.MIN_VALUE
                || getDefaultValue().equals(getValue())) {
            return false;
        }

        return getValue().length() < getMinLength();
    }

    @Override
    public boolean hasTypeMismatchValidityState() {
        return false;
    }

    @Override
    public boolean hasRangeOverflowValidityState() {
        return false;
    }

    @Override
    public boolean hasRangeUnderflowValidityState() {
        return false;
    }

    @Override
    public boolean isValidValidityState() {
        return !isCustomErrorValidityState()
                && !isValueMissingValidityState()
                && !isTooLongValidityState()
                && !isTooShortValidityState()
                && !hasPatternMismatchValidityState();
    }

    @Override
    public boolean isValueMissingValidityState() {
        return isRequiredSupported()
                && ATTRIBUTE_NOT_DEFINED != getAttributeDirect(ATTRIBUTE_REQUIRED)
                && getValue().isEmpty();
    }

    /**
     * @return the value of the attribute {@code formnovalidate} or an empty string if that attribute isn't defined
     */
    public final boolean isFormNoValidate() {
        return hasAttribute(ATTRIBUTE_FORMNOVALIDATE);
    }

    /**
     * Sets the value of the attribute {@code formnovalidate}.
     *
     * @param noValidate the value of the attribute {@code formnovalidate}
     */
    public final void setFormNoValidate(final boolean noValidate) {
        if (noValidate) {
            setAttribute(ATTRIBUTE_FORMNOVALIDATE, ATTRIBUTE_FORMNOVALIDATE);
        }
        else {
            removeAttribute(ATTRIBUTE_FORMNOVALIDATE);
        }
    }

    /**
     * @return the {@code type} property
     */
    public final String getType() {
        final BrowserVersion browserVersion = getPage().getWebClient().getBrowserVersion();
        String type = getTypeAttribute();
        type = org.htmlunit.util.StringUtils.toRootLowerCaseWithCache(type);
        return isSupported(type, browserVersion) ? type : "text";
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Changes the type of the current HtmlInput. Because there are several subclasses of HtmlInput,
     * changing the type attribute is not sufficient, this will replace the HtmlInput element in the
     * DOM tree with a new one (at least of the newType is different from the old one).<br>
     * The js peer object is still the same (there is only a HTMLInputElement without any sublcasses).<br>
     * This returns the new (or the old) HtmlInput element to ease the use of this method.
     * @param newType the new type to set
     * @param setThroughAttribute set type value through setAttribute()
     * @return the new or the old HtmlInput element
     */
    public HtmlInput changeType(String newType, final boolean setThroughAttribute) {
        final String currentType = getAttributeDirect(TYPE_ATTRIBUTE);

        final SgmlPage page = getPage();
        final WebClient webClient = page.getWebClient();
        final BrowserVersion browser = webClient.getBrowserVersion();
        if (!currentType.equalsIgnoreCase(newType)) {
            if (newType != null && browser.hasFeature(JS_INPUT_SET_TYPE_LOWERCASE)) {
                newType = org.htmlunit.util.StringUtils.toRootLowerCaseWithCache(newType);
            }

            if (!isSupported(org.htmlunit.util.StringUtils
                                    .toRootLowerCaseWithCache(newType), browser)) {
                if (setThroughAttribute) {
                    newType = "text";
                }
                else if (browser.hasFeature(JS_INPUT_SET_UNSUPORTED_TYPE_EXCEPTION)) {
                    throw JavaScriptEngine.reportRuntimeError("Invalid argument '" + newType
                            + "' for setting property type.");
                }
            }

            final AttributesImpl attributes = new AttributesImpl();
            boolean typeFound = false;
            for (final DomAttr entry : getAttributesMap().values()) {
                final String name = entry.getName();
                final String value = entry.getValue();

                if (TYPE_ATTRIBUTE.equals(name)) {
                    attributes.addAttribute(null, name, name, null, newType);
                    typeFound = true;
                }
                else {
                    attributes.addAttribute(null, name, name, null, value);
                }
            }

            if (!typeFound) {
                attributes.addAttribute(null, TYPE_ATTRIBUTE, TYPE_ATTRIBUTE, null, newType);
            }

            // create a new one only if we have a new type
            if (ATTRIBUTE_NOT_DEFINED != currentType || !"text".equalsIgnoreCase(newType)) {
                final HtmlInput newInput = (HtmlInput) webClient.getPageCreator().getHtmlParser()
                        .getFactory(HtmlInput.TAG_NAME)
                        .createElement(page, HtmlInput.TAG_NAME, attributes);

                if (browser.hasFeature(JS_INPUT_CHANGE_TYPE_DROPS_VALUE)) {
                    // a hack for the moment (IE)
                    if (!(newInput instanceof HtmlSubmitInput)
                            && !(newInput instanceof HtmlResetInput)
                            && !(newInput instanceof HtmlCheckBoxInput)
                            && !(newInput instanceof HtmlRadioButtonInput)
                            && !(newInput instanceof HtmlImageInput)) {
                        newInput.setRawValue(getRawValue());
                    }
                }
                else {
                    newInput.adjustValueAfterTypeChange(this, browser);
                }

                // the input hasn't yet been inserted into the DOM tree (likely has been
                // created via document.createElement()), so simply replace it with the
                // new Input instance created in the code above
                if (getParentNode() != null) {
                    getParentNode().replaceChild(newInput, this);
                }

                final WebClient client = page.getWebClient();
                if (client.isJavaScriptEngineEnabled()) {
                    final HTMLInputElement scriptable = getScriptableObject();
                    setScriptableObject(null);
                    scriptable.setDomNode(newInput, true);
                }

                return newInput;
            }
            super.setAttributeNS(null, TYPE_ATTRIBUTE, newType, true, true);
        }
        return this;
    }

    protected void adjustValueAfterTypeChange(final HtmlInput oldInput, final BrowserVersion browserVersion) {
        final String originalValue = oldInput.getValue();
        if (ATTRIBUTE_NOT_DEFINED != originalValue) {
            setValue(originalValue);
        }
    }

    /**
     * Returns whether the specified type is supported or not.
     * @param type the input type
     * @param browserVersion the browser version
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

    protected void unmarkValueDirty() {
        isValueDirty_ = false;
    }

    protected void markValueDirty() {
        isValueDirty_ = true;
    }
}
