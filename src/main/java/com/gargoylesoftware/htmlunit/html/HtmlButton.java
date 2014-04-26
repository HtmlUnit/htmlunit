/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.BUTTON_EMPTY_TYPE_BUTTON;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Wrapper for the HTML element "button".
 *
 * @version $Revision$
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
public class HtmlButton extends HtmlElement implements DisabledElement, SubmittableElement, FormFieldWithNameHistory {

    private static final Log LOG = LogFactory.getLog(HtmlButton.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "button";
    private String originalName_;
    private Collection<String> newNames_ = Collections.emptySet();

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
     * Sets the content of the "value" attribute.
     *
     * @param newValue the new content
     */
    public void setValueAttribute(final String newValue) {
        setAttribute("value", newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doClickStateUpdate() throws IOException {
        final String type = getTypeAttribute().toLowerCase(Locale.ENGLISH);

        final HtmlForm form = getEnclosingForm();
        if (form != null) {
            if ("submit".equals(type)) {
                form.submit(this);
            }
            else if ("reset".equals(type)) {
                form.reset();
            }
            return false;
        }
        super.doClickStateUpdate();
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isDisabled() {
        return hasAttribute("disabled");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameValuePair[] getSubmitKeyValuePairs() {
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
     * This implementation returns <tt>false</tt>; only checkboxes and radio buttons really care what
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
     * Returns the value of the attribute "name". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "name" or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttribute("name");
    }

    /**
     * Returns the value of the attribute "value". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "value" or an empty string if that attribute isn't defined
     */
    public final String getValueAttribute() {
        return getAttribute("value");
    }

    /**
     * Overwritten, because Internet Explorer doesn't follow the spec
     * when the type isn't specified. It will return
     * button" rather than the "submit" specified in the spec.
     *
     * @param attributeName the name of the attribute
     * @return the value of the attribute or {@link #ATTRIBUTE_NOT_DEFINED} or {@link #ATTRIBUTE_VALUE_EMPTY}
     */
    @Override
    public String getAttribute(final String attributeName) {
        String type = super.getAttribute(attributeName);

        if (type == DomElement.ATTRIBUTE_NOT_DEFINED && "type".equalsIgnoreCase(attributeName)) {
            if (hasFeature(BUTTON_EMPTY_TYPE_BUTTON)) {
                type = "button";
            }
            else {
                type = "submit";
            }
        }
        return type;
    }

    /**
     * Returns the value of the attribute "type". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "type" or the default value if that attribute isn't defined
     */
    public final String getTypeAttribute() {
        return getAttribute("type");
    }

    /**
     * Returns the value of the attribute "disabled". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "disabled" or an empty string if that attribute isn't defined
     */
    public final String getDisabledAttribute() {
        return getAttribute("disabled");
    }

    /**
     * Returns the value of the attribute "tabindex". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "tabindex" or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttribute("tabindex");
    }

    /**
     * Returns the value of the attribute "accesskey". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "accesskey" or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttribute("accesskey");
    }

    /**
     * Returns the value of the attribute "onfocus". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onfocus" or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttribute("onfocus");
    }

    /**
     * Returns the value of the attribute "onblur". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onblur" or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttribute("onblur");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue) {
        if ("name".equals(qualifiedName)) {
            if (newNames_.isEmpty()) {
                newNames_ = new HashSet<String>();
            }
            newNames_.add(attributeValue);
        }
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue);
    }

    /**
     * {@inheritDoc}
     */
    public String getOriginalName() {
        return originalName_;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<String> getNewNames() {
        return newNames_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Returns the default display style.
     *
     * @return the default display style.
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.INLINE_BLOCK;
    }
}
