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

import org.apache.commons.httpclient.NameValuePair;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.host.Event;

/**
 * Wrapper for the HTML element "input".
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
public abstract class HtmlInput extends ClickableElement implements DisabledElement, SubmittableElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "input";

    private String defaultValue_;

    /**
     * Creates an instance.
     *
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    public HtmlInput(final HtmlPage page, final Map<String, HtmlAttr> attributes) {
        this(null, TAG_NAME, page, attributes);
    }

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    public HtmlInput(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map<String, HtmlAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
        defaultValue_ = getValueAttribute();
    }

    /**
     * Sets the content of the "value" attribute, executing onchange handlers if appropriate.
     * This method returns the page conained by this element's window after the value is set,
     * which may or may not be the same as the original page.
     * @param newValue the new content
     * @return the page conained by this element's window after the value is set
     */
    public HtmlPage setValueAttribute(final String newValue) {
        WebAssert.notNull("newValue", newValue);
        setAttributeValue("value", newValue);

        return executeOnChangeHandlerIfAppropriate(this);
    }

    /**
     * {@inheritDoc}
     */
    public NameValuePair[] getSubmitKeyValuePairs() {
        return new NameValuePair[]{new NameValuePair(getNameAttribute(), getValueAttribute())};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asText() {
        return getValueAttribute();
    }

    /**
     * Returns the value of the attribute "type". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "type" or an empty string if that attribute isn't defined
     */
    public final String getTypeAttribute() {
        return getAttributeValue("type");
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
     * <p>Return the value of the attribute "value". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.</p>
     *
     * @return the value of the attribute "value" or an empty string if that attribute isn't defined
     */
    public final String getValueAttribute() {
        return getAttributeValue("value");
    }

    /**
     * Returns the value of the attribute "checked". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "checked" or an empty string if that attribute isn't defined
     */
    public final String getCheckedAttribute() {
        return getAttributeValue("checked");
    }

    /**
     * {@inheritDoc}
     */
    public final String getDisabledAttribute() {
        return getAttributeValue("disabled");
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isDisabled() {
        return isAttributeDefined("disabled");
    }

    /**
     * Returns the value of the attribute "readonly". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "readonly"
     * or an empty string if that attribute isn't defined.
     */
    public final String getReadOnlyAttribute() {
        return getAttributeValue("readonly");
    }

    /**
     * Returns the value of the attribute "size". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "size"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSizeAttribute() {
        return getAttributeValue("size");
    }

    /**
     * Returns the value of the attribute "maxlength". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "maxlength"
     * or an empty string if that attribute isn't defined.
     */
    public final String getMaxLengthAttribute() {
        return getAttributeValue("maxlength");
    }

    /**
     * Returns the value of the attribute "src". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "src"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSrcAttribute() {
        return getAttributeValue("src");
    }

    /**
     * Returns the value of the attribute "alt". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "alt"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAltAttribute() {
        return getAttributeValue("alt");
    }

    /**
     * Returns the value of the attribute "usemap". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "usemap"
     * or an empty string if that attribute isn't defined.
     */
    public final String getUseMapAttribute() {
        return getAttributeValue("usemap");
    }

    /**
     * Returns the value of the attribute "tabindex". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "tabindex"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTabIndexAttribute() {
        return getAttributeValue("tabindex");
    }

    /**
     * Returns the value of the attribute "accesskey". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "accesskey"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAccessKeyAttribute() {
        return getAttributeValue("accesskey");
    }

    /**
     * Returns the value of the attribute "onfocus". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onfocus"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnFocusAttribute() {
        return getAttributeValue("onfocus");
    }

    /**
     * Returns the value of the attribute "onblur". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onblur"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnBlurAttribute() {
        return getAttributeValue("onblur");
    }

    /**
     * Returns the value of the attribute "onselect". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onselect"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnSelectAttribute() {
        return getAttributeValue("onselect");
    }

    /**
     * Returns the value of the attribute "onchange". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onchange"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnChangeAttribute() {
        return getAttributeValue("onchange");
    }

    /**
     * Returns the value of the attribute "accept". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "accept"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAcceptAttribute() {
        return getAttributeValue("accept");
    }

    /**
     * Returns the value of the attribute "align". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "align"
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
     * Sets the "checked" attribute, returning the page that occupies this input's window after setting
     * the attribute. Note that the returned page may or may not be the original page, depending on
     * the presence of JavaScript event handlers, etc.
     *
     * @param <P> the type of the page that occupies this input's window after setting the attribute
     * @param isChecked <tt>true</tt> if this element is to be selected
     * @return the page that occupies this input's window after setting the attribute
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P setChecked(final boolean isChecked) {
        // By default this returns the current page. Derived classes will override.
        return (P) getPage();
    }

    /**
     * Returns <tt>true</tt> if this element is currently selected.
     * @return <tt>true</tt> if this element is currently selected
     */
    public boolean isChecked() {
        return isAttributeDefined("checked");
    }

    /**
     * Simulate clicking this input with a pointing device. The x and y coordinates
     * of the pointing device will be sent to the server.
     *
     * @param <P> the type of the page that is loaded after the click has taken place
     * @param x the x coordinate of the pointing device at the time of clicking
     * @param y the y coordinate of the pointing device at the time of clicking
     * @return the page that is loaded after the click has taken place
     * @exception IOException If an io error occurs
     * @exception ElementNotFoundException If a particular XML element could not be found in the DOM model
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P click(final int x, final int y)
        throws IOException, ElementNotFoundException {

        // By default this is no different than a click without coordinates.
        return (P) click();
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
    static HtmlPage executeOnChangeHandlerIfAppropriate(final HtmlElement htmlElement) {
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
