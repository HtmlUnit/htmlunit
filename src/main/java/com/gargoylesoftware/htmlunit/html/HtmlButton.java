/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;

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
 */
public class HtmlButton extends ClickableElement implements DisabledElement, SubmittableElement {

    private static final long serialVersionUID = 4828725767615187345L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "button";

    private final transient Log mainLog_ = LogFactory.getLog(getClass());

    /**
     * Creates a new instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlButton(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Sets the content of the "value" attribute.
     *
     * @param newValue the new content
     */
    public void setValueAttribute(final String newValue) {
        setAttributeValue("value", newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Page doClickAction(final Page defaultPage) throws IOException {
        final String type = getTypeAttribute().toLowerCase();

        final HtmlForm form = getEnclosingForm();
        if (form != null) {
            if (type.equals("submit")) {
                return form.submit(this);
            }
            else if (type.equals("reset")) {
                return form.reset();
            }
        }

        return defaultPage;
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isDisabled() {
        return hasAttribute("disabled");
    }

    /**
     * {@inheritDoc}
     */
    public NameValuePair[] getSubmitKeyValuePairs() {
        return new NameValuePair[]{new NameValuePair(getNameAttribute(), getValueAttribute())};
    }

    /**
     * {@inheritDoc}
     *
     * @see SubmittableElement#reset()
     */
    public void reset() {
        if (mainLog_.isDebugEnabled()) {
            mainLog_.debug("reset() not implemented for this element");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see SubmittableElement#setDefaultValue(String)
     */
    public void setDefaultValue(final String defaultValue) {
        if (mainLog_.isDebugEnabled()) {
            mainLog_.debug("setDefaultValue() not implemented for this element");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see SubmittableElement#getDefaultValue()
     */
    public String getDefaultValue() {
        if (mainLog_.isDebugEnabled()) {
            mainLog_.debug("getDefaultValue() not implemented for this element");
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
        return getAttributeValue("name");
    }

    /**
     * Returns the value of the attribute "value". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "value" or an empty string if that attribute isn't defined
     */
    public final String getValueAttribute() {
        return getAttributeValue("value");
    }

    /**
     * Returns the value of the attribute "type". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute. Note that Internet
     * Explorer doesn't follow the spec when the type isn't specified. It will return
     * "button" rather than the "submit" specified in the spec.
     *
     * @return the value of the attribute "type" or the default value if that attribute isn't defined
     */
    public final String getTypeAttribute() {
        String type = getAttributeValue("type");
        if (type == HtmlElement.ATTRIBUTE_NOT_DEFINED) {
            final BrowserVersion browser = getPage().getWebClient().getBrowserVersion();
            if (browser.isIE()) {
                type = "button";
            }
            else {
                type = "submit";
            }
        }
        return type;
    }

    /**
     * Returns the value of the attribute "disabled". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "disabled" or an empty string if that attribute isn't defined
     */
    public final String getDisabledAttribute() {
        return getAttributeValue("disabled");
    }

    /**
     * Returns the value of the attribute "tabindex". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "tabindex" or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttributeValue("tabindex");
    }

    /**
     * Returns the value of the attribute "accesskey". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "accesskey" or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttributeValue("accesskey");
    }

    /**
     * Returns the value of the attribute "onfocus". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onfocus" or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttributeValue("onfocus");
    }

    /**
     * Returns the value of the attribute "onblur". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onblur" or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttributeValue("onblur");
    }
}
