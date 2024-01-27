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
import static org.htmlunit.BrowserVersionFeatures.HTMLBUTTON_SUBMIT_IGNORES_DISABLED_STATE;
import static org.htmlunit.BrowserVersionFeatures.HTMLBUTTON_WILL_VALIDATE_IGNORES_READONLY;
import static org.htmlunit.html.HtmlForm.ATTRIBUTE_FORMNOVALIDATE;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.SgmlPage;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.MouseEvent;
import org.htmlunit.util.NameValuePair;

/**
 * Wrapper for the HTML element "button".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Dmitri Zoubkov
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlButton extends HtmlElement implements DisabledElement, SubmittableElement,
                LabelableElement, FormFieldWithNameHistory, ValidatableElement {

    private static final Log LOG = LogFactory.getLog(HtmlButton.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "button";

    private static final String TYPE_SUBMIT = "submit";
    private static final String TYPE_RESET = "reset";
    private static final String TYPE_BUTTON = "button";

    private final String originalName_;
    private Collection<String> newNames_ = Collections.emptySet();
    private String customValidity_;

    /**
     * Creates a new instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlButton(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
        originalName_ = getNameAttribute();
    }

    /**
     * Sets the content of the {@code value} attribute.
     *
     * @param newValue the new content
     */
    public void setValueAttribute(final String newValue) {
        setAttribute(VALUE_ATTRIBUTE, newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doClickStateUpdate(final boolean shiftKey, final boolean ctrlKey) throws IOException {
        if (hasFeature(HTMLBUTTON_SUBMIT_IGNORES_DISABLED_STATE) || !isDisabled()) {
            final HtmlForm form = getEnclosingForm();
            if (form != null) {
                final String type = getType();
                if (TYPE_BUTTON.equals(type)) {
                    return false;
                }

                if (TYPE_RESET.equals(type)) {
                    form.reset();
                    return false;
                }

                form.submit(this);
                return false;
            }
        }

        super.doClickStateUpdate(shiftKey, ctrlKey);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isDisabled() {
        return hasAttribute(ATTRIBUTE_DISABLED);
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
    public NameValuePair[] getSubmitNameValuePairs() {
        return new NameValuePair[]{new NameValuePair(getNameAttribute(), getValueAttribute())};
    }

    /**
     * {@inheritDoc}
     *
     * @see SubmittableElement#reset()
     */
    @Override
    public void reset() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("reset() not implemented for this element");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see SubmittableElement#setDefaultValue(String)
     */
    @Override
    public void setDefaultValue(final String defaultValue) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("setDefaultValue() not implemented for this element");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see SubmittableElement#getDefaultValue()
     */
    @Override
    public String getDefaultValue() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getDefaultValue() not implemented for this element");
        }
        return "";
    }

    /**
     * {@inheritDoc}
     *
     * This implementation is empty; only checkboxes and radio buttons really care what the
     * default checked value is.
     *
     * @see SubmittableElement#setDefaultChecked(boolean)
     * @see HtmlRadioButtonInput#setDefaultChecked(boolean)
     * @see HtmlCheckBoxInput#setDefaultChecked(boolean)
     */
    @Override
    public void setDefaultChecked(final boolean defaultChecked) {
        // Empty.
    }

    /**
     * {@inheritDoc}
     *
     * This implementation returns {@code false}; only checkboxes and radio buttons really care what
     * the default checked value is.
     *
     * @see SubmittableElement#isDefaultChecked()
     * @see HtmlRadioButtonInput#isDefaultChecked()
     * @see HtmlCheckBoxInput#isDefaultChecked()
     */
    @Override
    public boolean isDefaultChecked() {
        return false;
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
     * Returns the value of the attribute {@code value}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code value} or an empty string if that attribute isn't defined
     */
    public final String getValueAttribute() {
        return getAttributeDirect(VALUE_ATTRIBUTE);
    }

    /**
     * Returns the value of the attribute {@code type}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code type} or the default value if that attribute isn't defined
     */
    public final String getTypeAttribute() {
        return getAttribute(TYPE_ATTRIBUTE);
    }

    /**
     * @return the normalized type value (submit|reset|button).
     */
    public String getType() {
        final String type = getTypeAttribute().toLowerCase(Locale.ROOT);
        if (TYPE_RESET.equals(type)) {
            return TYPE_RESET;
        }
        if (TYPE_BUTTON.equals(type)) {
            return TYPE_BUTTON;
        }
        return TYPE_SUBMIT;
    }

    /**
     * Returns the value of the attribute {@code disabled}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code disabled} or an empty string if that attribute isn't defined
     */
    @Override
    public final String getDisabledAttribute() {
        return getAttributeDirect(ATTRIBUTE_DISABLED);
    }

    /**
     * Returns the value of the attribute {@code tabindex}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code tabindex} or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttributeDirect("tabindex");
    }

    /**
     * Returns the value of the attribute {@code accesskey}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code accesskey} or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttributeDirect("accesskey");
    }

    /**
     * Returns the value of the attribute {@code onfocus}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onfocus} or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttributeDirect("onfocus");
    }

    /**
     * Returns the value of the attribute {@code onblur}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onblur} or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttributeDirect("onblur");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObservers) {
        final String qualifiedNameLC = org.htmlunit.util.StringUtils.toRootLowerCase(qualifiedName);
        if (NAME_ATTRIBUTE.equals(qualifiedNameLC)) {
            if (newNames_.isEmpty()) {
                newNames_ = new HashSet<>();
            }
            newNames_.add(attributeValue);
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
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.INLINE_BLOCK;
    }

    /**
     * {@inheritDoc}
     * @return {@code true} to make generated XML readable as HTML.
     */
    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        if (TYPE_RESET.equals(getType())) {
            return true;
        }

        return super.isValid() && !isCustomErrorValidityState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean willValidate() {
        if (TYPE_RESET.equals(getType()) || TYPE_BUTTON.equals(getType())) {
            return false;
        }

        return !isDisabled()
                && (hasFeature(HTMLBUTTON_WILL_VALIDATE_IGNORES_READONLY) || !isReadOnly());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCustomValidity(final String message) {
        customValidity_ = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCustomErrorValidityState() {
        return !StringUtils.isEmpty(customValidity_);
    }

    @Override
    public boolean isValidValidityState() {
        return !isCustomErrorValidityState();
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
}
