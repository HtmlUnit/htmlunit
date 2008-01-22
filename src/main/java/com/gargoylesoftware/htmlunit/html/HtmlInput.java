/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.util.Map;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.host.Event;

/**
 *  Wrapper for the html element "input"
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public abstract class HtmlInput extends FocusableElement implements DisabledElement, SubmittableElement {

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "input";

    private String defaultValue_;

    /**
     * Create an instance
     *
     * @param page The page that contains this element
     * @param attributes the initial attributes
     */
    public HtmlInput(final HtmlPage page, final Map<String, HtmlAttr> attributes) {
        this(null, TAG_NAME, page, attributes);
    }

    /**
     * Create an instance
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param page The page that contains this element
     * @param attributes the initial attributes
     */
    public HtmlInput(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map<String, HtmlAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
        defaultValue_ = getValueAttribute();
    }

    /**
     * Set the content of the "value" attribute, executing onchange handlers if appropriate.
     * @param newValue The new content
     * @return the page that occupies this window after this value is set.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page setValueAttribute(final String newValue) {
        WebAssert.notNull("newValue", newValue);
        setAttributeValue("value", newValue);

        return executeOnChangeHandlerIfAppropriate(this);
    }

    /**
     * Return an array of KeyValuePairs that are the values that will be sent
     * back to the server whenever the current form is submitted.<p>
     *
     * THIS METHOD IS INTENDED FOR THE USE OF THE FRAMEWORK ONLY AND SHOULD NOT
     * BE USED BY CONSUMERS OF HTMLUNIT. USE AT YOUR OWN RISK.
     *
     * @return See above
     */
    public KeyValuePair[] getSubmitKeyValuePairs() {
        return new KeyValuePair[]{new KeyValuePair(getNameAttribute(), getValueAttribute())};
    }

    /**
     * {@inheritDoc}
     */
    public String asText() {
        return getValueAttribute();
    }

    /**
     * Return the value of the attribute "type".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "type"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTypeAttribute() {
        return getAttributeValue("type");
    }

    /**
     * Return the value of the attribute "name".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "name"
     * or an empty string if that attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttributeValue("name");
    }

    /**
     * <p>Return the value of the attribute "value".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.</p>
     *
     * @return The value of the attribute "value" or an empty string if that
     * attribute isn't defined
     */
    public final String getValueAttribute() {
        return getAttributeValue("value");
    }

    /**
     * Return the value of the attribute "checked".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "checked"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCheckedAttribute() {
        return getAttributeValue("checked");
    }

    /**
     * Return the value of the attribute "disabled".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "disabled"
     * or an empty string if that attribute isn't defined.
     */
    public final String getDisabledAttribute() {
        return getAttributeValue("disabled");
    }

    /**
     * Return true if the disabled attribute is set for this element.
     * @return Return true if this is disabled.
     */
    public final boolean isDisabled() {
        return isAttributeDefined("disabled");
    }

    /**
     * Return the value of the attribute "readonly".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "readonly"
     * or an empty string if that attribute isn't defined.
     */
    public final String getReadOnlyAttribute() {
        return getAttributeValue("readonly");
    }

    /**
     * Return the value of the attribute "size".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "size"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSizeAttribute() {
        return getAttributeValue("size");
    }

    /**
     * Return the value of the attribute "maxlength".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "maxlength"
     * or an empty string if that attribute isn't defined.
     */
    public final String getMaxLengthAttribute() {
        return getAttributeValue("maxlength");
    }

    /**
     * Return the value of the attribute "src".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "src"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSrcAttribute() {
        return getAttributeValue("src");
    }

    /**
     * Return the value of the attribute "alt".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "alt"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAltAttribute() {
        return getAttributeValue("alt");
    }

    /**
     * Return the value of the attribute "usemap".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "usemap"
     * or an empty string if that attribute isn't defined.
     */
    public final String getUseMapAttribute() {
        return getAttributeValue("usemap");
    }

    /**
     * Return the value of the attribute "tabindex".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "tabindex"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTabIndexAttribute() {
        return getAttributeValue("tabindex");
    }

    /**
     * Return the value of the attribute "accesskey".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "accesskey"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAccessKeyAttribute() {
        return getAttributeValue("accesskey");
    }

    /**
     * Return the value of the attribute "onfocus".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onfocus"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnFocusAttribute() {
        return getAttributeValue("onfocus");
    }

    /**
     * Return the value of the attribute "onblur".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onblur"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnBlurAttribute() {
        return getAttributeValue("onblur");
    }

    /**
     * Return the value of the attribute "onselect".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onselect"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnSelectAttribute() {
        return getAttributeValue("onselect");
    }

    /**
     * Return the value of the attribute "onchange".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onchange"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnChangeAttribute() {
        return getAttributeValue("onchange");
    }

    /**
     * Return the value of the attribute "accept".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "accept"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAcceptAttribute() {
        return getAttributeValue("accept");
    }

    /**
     * Return the value of the attribute "align".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "align"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAlignAttribute() {
        return getAttributeValue("align");
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#reset()
     */
    public void reset() {
        setValueAttribute(defaultValue_);
    }

    /**
     * {@inheritDoc} Also sets the value attribute when emulating Netscape browsers.
     * @see SubmittableElement#setDefaultValue(String)
     * @see HtmlFileInput#setDefaultValue(String)
     */
    public void setDefaultValue(final String defaultValue) {
        final boolean modifyValue = getPage().getWebClient().getBrowserVersion().isNetscape();
        setDefaultValue(defaultValue, modifyValue);
    }

    /**
     * Sets the default value, optionally also modifying the current value.
     * @param defaultValue The new default value.
     * @param modifyValue Whether or not to set the current value to the default value.
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
    public void setDefaultChecked(final boolean defaultChecked) {
        // Empty.
    }

    /**
     * {@inheritDoc} The default implementation returns <tt>false</tt>; only checkboxes and
     * radio buttons really care what the default checked value is.
     * @see SubmittableElement#isDefaultChecked()
     * @see HtmlRadioButtonInput#isDefaultChecked()
     * @see HtmlCheckBoxInput#isDefaultChecked()
     */
    public boolean isDefaultChecked() {
        return false;
    }

    /**
     * Set the "checked" attribute
     *
     * @param isChecked true if this element is to be selected
     * @return The page that occupies this window after setting checked status.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page setChecked(final boolean isChecked) {
        // By default this returns the current page.  Derived classes will override.
        return getPage();
    }

    /**
     * Return true if this element is currently selected
     *
     * @return See above
     */
    public boolean isChecked() {
        return isAttributeDefined("checked");
    }

    /**
     * Simulate clicking this input with a pointing device.  The x and y coordinates
     * of the pointing device will be sent to the server.
     *
     * @param x The x coordinate of the pointing device at the time of clicking
     * @param y The y coordinate of the pointing device at the time of clicking
     * @return The page that is loaded after the click has taken place.
     * @exception IOException If an io error occurs
     * @exception ElementNotFoundException If a particular xml element could not be found in the dom model
     */
    public Page click(final int x, final int y)
        throws
            IOException,
            ElementNotFoundException {

        // By default this is no different than a click without coordinates.
        return click();
    }
    
    /**
     * Executes the onchange script code for this element if this is appropriate.
     * This means that the element must have an onchange script, script must be enabled
     * and the change in the element must not have been triggered by a script.
     *
     * @param htmlElement The element that contains the onchange attribute.
     * @return The page that occupies this window after this method completes. It
     * may be this or it may be a freshly loaded page.
     */
    static Page executeOnChangeHandlerIfAppropriate(final HtmlElement htmlElement) {
        final HtmlPage page = htmlElement.getPage();

        final JavaScriptEngine engine = htmlElement.getPage().getWebClient().getJavaScriptEngine();
        if (engine.isScriptRunning()) {
            return page;
        }
        final ScriptResult scriptResult = htmlElement.fireEvent(Event.TYPE_CHANGE);

        if (page.getWebClient().getWebWindows().contains(page.getEnclosingWindow())) {
            return page.getEnclosingWindow().getEnclosedPage(); // may be itself or a newly loaded one
        }
        else if (scriptResult != null) {
            // current window doesn't exist anymore
            return scriptResult.getNewPage();
        }

        return page;
    }
}
