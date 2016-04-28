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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_INPUT_DISPLAY_INLINE_BLOCK;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_DOES_NOT_CLICK_SURROUNDING_ANCHOR;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
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
 */
public abstract class HtmlInput extends HtmlElement implements DisabledElement, SubmittableElement,
    FormFieldWithNameHistory {
    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "input";

    private String defaultValue_;
    private String originalName_;
    private Collection<String> newNames_ = Collections.emptySet();
    private boolean createdByJavascript_;
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
     * Sets the content of the {@code value} attribute, executing onchange handlers if appropriate.
     * This method returns the page contained by this element's window after the value is set,
     * which may or may not be the same as the original page.
     * @param newValue the new content
     * @return the page contained by this element's window after the value is set
     */
    public Page setValueAttribute(final String newValue) {
        WebAssert.notNull("newValue", newValue);
        setAttribute("value", newValue);

        final Page page = executeOnChangeHandlerIfAppropriate(this);
        valueAtFocus_ = getInternalValue();
        return page;
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
        final String type = getAttribute("type");
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
        return getAttribute("name");
    }

    /**
     * <p>Return the value of the attribute "value". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.</p>
     *
     * @return the value of the attribute {@code value} or an empty string if that attribute isn't defined
     */
    public final String getValueAttribute() {
        return getAttribute("value");
    }

    /**
     * Returns the value of the attribute {@code checked}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code checked} or an empty string if that attribute isn't defined
     */
    public final String getCheckedAttribute() {
        return getAttribute("checked");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDisabledAttribute() {
        return getAttribute("disabled");
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
        return getAttribute("readonly");
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
        return getAttribute("size");
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
     * Returns the value of the attribute {@code src}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code src}
     * or an empty string if that attribute isn't defined.
     */
    public final String getSrcAttribute() {
        return getSrcAttributeNormalized();
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
        return getAttribute("alt");
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
        return getAttribute("usemap");
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
        return getAttribute("tabindex");
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
        return getAttribute("accesskey");
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
        return getAttribute("onfocus");
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
        return getAttribute("onblur");
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
        return getAttribute("onselect");
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
        return getAttribute("onchange");
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
        return getAttribute("accept");
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
        return getAttribute("align");
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
     * Also sets the value attribute when emulating Netscape browsers.
     * @see SubmittableElement#setDefaultValue(String)
     * @see HtmlFileInput#setDefaultValue(String)
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
        defaultValue_ = defaultValue;
        if (modifyValue) {
            setValueAttribute(defaultValue);
        }
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
     * {@inheritDoc} The default implementation is empty; only checkboxes and radio buttons
     * really care what the default checked value is.
     * @see SubmittableElement#setDefaultChecked(boolean)
     * @see HtmlRadioButtonInput#setDefaultChecked(boolean)
     * @see HtmlCheckBoxInput#setDefaultChecked(boolean)
     */
    @Override
    public void setDefaultChecked(final boolean defaultChecked) {
        // Empty.
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
     * Simulate clicking this input with a pointing device. The x and y coordinates
     * of the pointing device will be sent to the server.
     *
     * @param <P> the page type
     * @param x the x coordinate of the pointing device at the time of clicking
     * @param y the y coordinate of the pointing device at the time of clicking
     * @return the page that is loaded after the click has taken place
     * @exception IOException If an io error occurs
     * @exception ElementNotFoundException If a particular XML element could not be found in the DOM model
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P click(final int x, final int y)
        throws
            IOException,
            ElementNotFoundException {

        // By default this is no different than a click without coordinates.
        return (P) click();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean propagateClickStateUpdateToParent() {
        return !hasFeature(HTMLINPUT_DOES_NOT_CLICK_SURROUNDING_ANCHOR);
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

        final JavaScriptEngine engine = htmlElement.getPage().getWebClient().getJavaScriptEngine();
        if (engine.isScriptRunning()) {
            return page;
        }
        final ScriptResult scriptResult = htmlElement.fireEvent(Event.TYPE_CHANGE);

        if (page.getWebClient().containsWebWindow(page.getEnclosingWindow())) {
            // may be itself or a newly loaded one
            return page.getEnclosingWindow().getEnclosedPage();
        }

        if (scriptResult != null) {
            // current window doesn't exist anymore
            return scriptResult.getNewPage();
        }

        return page;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue) {
        if ("name".equals(qualifiedName)) {
            if (newNames_.isEmpty()) {
                newNames_ = new HashSet<>();
            }
            newNames_.add(attributeValue);
        }
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue);
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

        if (!valueAtFocus_.equals(getInternalValue())) {
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
        if (hasFeature(CSS_INPUT_DISPLAY_INLINE_BLOCK)) {
            return DisplayStyle.INLINE_BLOCK;
        }
        return DisplayStyle.INLINE;
    }

    /**
     * Returns the {@code required} attribute.
     * @return the {@code required} attribute
     */
    @JsxGetter
    public boolean isRequired() {
        return hasAttribute("required");
    }

    /**
     * Sets the {@code required} attribute.
     * @param required the new attribute value
     */
    @JsxSetter
    public void setRequired(final boolean required) {
        if (required) {
            setAttribute("required", "required");
        }
        else {
            removeAttribute("required");
        }
    }

    /**
     * Returns the value of the {@code size} attribute.
     *
     * @return the value of the {@code size} attribute
     */
    public String getSize() {
        return getAttribute("size");
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
        return getAttribute("accept");
    }

    /**
     * Sets the {@code accept} attribute.
     *
     * @param accept the {@code accept} attribute
     */
    public void setAccept(final String accept) {
        setAttribute("accept", accept);
    }

    /**
     * Returns the value of the {@code autocomplete} attribute.
     *
     * @return the value of the {@code autocomplete} attribute
     */
    public String getAutocomplete() {
        return getAttribute("autocomplete");
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
        return getAttribute("placeholder");
    }

    /**
     * Sets the {@code placeholder} attribute.
     *
     * @param placeholder the {@code placeholder} attribute
     */
    public void setPlaceholder(final String placeholder) {
        setAttribute("placeholder", placeholder);
    }
}
