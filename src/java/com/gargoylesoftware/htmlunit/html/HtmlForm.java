/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.lang.StringUtils;
import org.jaxen.JaxenException;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.host.Event;

/**
 * Wrapper for the html element "form"
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Brad Clarke
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author George Murnock
 * @author Kent Tong
 * @author Ahmed Ashour
 * @author Philip Graf
 */
public class HtmlForm extends ClickableElement {

    private static final long serialVersionUID = 5338964478788825866L;

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "form";

    private static final Collection SUBMITTABLE_ELEMENT_NAMES =
        Arrays.asList(new String[]{"input", "button", "select", "textarea", "isindex"});

    private KeyValuePair fakeSelectedRadioButton_;

    /**
     *  Create an instance
     *
     * @param htmlPage The page that contains this element
     * @param attributes the initial attributes
     * @deprecated You should not directly construct HtmlForm.
     */
    //TODO: to be removed, deprecated in 23 June 2007
    public HtmlForm(final HtmlPage htmlPage, final Map attributes) {
        this(null, TAG_NAME, htmlPage, attributes);
    }

    /**
     *  Create an instance
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param htmlPage The page that contains this element
     * @param attributes the initial attributes
     */
    HtmlForm(final String namespaceURI, final String qualifiedName, final HtmlPage htmlPage,
            final Map attributes) {
        super(namespaceURI, qualifiedName, htmlPage, attributes);
    }

    /**
     *  Submit this form to the appropriate server as if a submit button had
     *  been pressed
     *
     * @param buttonName The name of a submit input element or a button element
     *      which will be sent back up with the response
     * @return A new Page that reflects the results of this submission
     * @throws IOException If an IO error occurs
     * @throws ElementNotFoundException If a button with the specified name cannot be found.
     */
    public Page submit(final String buttonName) throws IOException, ElementNotFoundException {

        final List inputList = getHtmlElementsByAttribute("input", "name", buttonName);
        final Iterator iterator = inputList.iterator();
        while (iterator.hasNext()) {
            final HtmlInput input = (HtmlInput)iterator.next();
            if (input.getTypeAttribute().equals("submit")) {
                return submit(input);
            }
        }

        final HtmlButton button = (HtmlButton)getOneHtmlElementByAttribute("button", "name", buttonName);
        return submit(button);
    }

    /**
     *  Submit this form to the appropriate server as if it had been submitted
     *  by javascript - ie no submit buttons were pressed.  Note that because we
     *  are simulating a javascript submit, the onsubmit handler will not get
     *  executed.
     *
     * @return A new Page that reflects the results of this submission
     * @exception IOException If an IO error occurs
     */
    public Page submit() throws IOException {
        return submit((SubmittableElement)null);
    }

    /**
     *  Submit this form to the appropriate server.  If submitElement is null then
     *  treat this as if it was called by javascript.  In this case, the onsubmit
     *  handler will not get executed.
     *
     * @param submitElement The element that caused the submit to occur
     * @return A new Page that reflects the results of this submission
     * @exception IOException If an IO error occurs
     */
    public Page submit(final SubmittableElement submitElement) throws IOException {

        final HtmlPage htmlPage = getPage();
        if (htmlPage.getWebClient().isJavaScriptEnabled()) {
            if (submitElement != null) {
                final ScriptResult scriptResult = fireEvent(Event.TYPE_SUBMIT);
                if (scriptResult != null && Boolean.FALSE.equals(scriptResult.getJavaScriptResult())) {
                    return scriptResult.getNewPage();
                }
            }

            final String action = getActionAttribute();
            if (TextUtil.startsWithIgnoreCase(action, "javascript:")) {
                return htmlPage.executeJavaScriptIfPossible(action, "Form action", null).getNewPage();
            }
        }
        else {
            if (TextUtil.startsWithIgnoreCase(getActionAttribute(), "javascript:")) {
                // The action is javascript but javascript isn't enabled.  Return
                // the current page.
                return htmlPage;
            }
        }

        final List parameters = getParameterListForSubmit(submitElement);
        final SubmitMethod method = SubmitMethod.getInstance(getAttributeValue("method"));

        String actionUrl = getActionAttribute();
        if (SubmitMethod.GET.equals(method)) {
            final String anchor = StringUtils.substringAfter(actionUrl, "#");
            actionUrl = StringUtils.substringBefore(actionUrl, "#");

            final NameValuePair[] pairs = new NameValuePair[parameters.size()];
            parameters.toArray(pairs);
            final String queryFromFields = EncodingUtil.formUrlEncode(pairs, getPage().getPageEncoding());
            // action may already contain some query parameters: they have to be removed
            actionUrl = StringUtils.substringBefore(actionUrl, "?") + "?" + queryFromFields;
            if (anchor.length() > 0) {
                actionUrl += "#" + anchor;
            }
            parameters.clear(); // parameters have been added to query
        }
        final URL url;
        try {
            url = htmlPage.getFullyQualifiedUrl(actionUrl);
        }
        catch (final MalformedURLException e) {
            throw new IllegalArgumentException("Not a valid url: " + actionUrl);
        }

        final WebRequestSettings settings = new WebRequestSettings(url, method);
        settings.setRequestParameters(parameters);
        settings.setEncodingType(FormEncodingType.getInstance(getEnctypeAttribute()));
        settings.setCharset(getSubmitCharset());
        settings.addAdditionalHeader("Referer", htmlPage.getWebResponse().getUrl().toExternalForm());

        final WebWindow webWindow = htmlPage.getEnclosingWindow();
        return htmlPage.getWebClient().getPage(
                webWindow,
                htmlPage.getResolvedTarget(getTargetAttribute()),
                settings);
    }

    /**
     * Gets the charset to use for the form submission. This is the first one
     * from the list provided in {@link #getAcceptCharsetAttribute()} if any
     * or the page's charset else
     * @return see above
     */
    private String getSubmitCharset() {
        if (getAcceptCharsetAttribute().length() > 0) {
            return getAcceptCharsetAttribute().trim().replaceAll("[ ,].*", "");
        }
        else {
            return getPage().getPageEncoding();
        }
    }

    /**
     * Return a list of {@link KeyValuePair}s that represent the data that will be
     * sent to the server on a form submit.  This is primarily intended to aid
     * debugging.
     *
     * @param submitElement The element that would have been pressed to submit the
     * form or null if the form was submitted by javascript.
     * @return The list of {@link KeyValuePair}s.
     */
    public final List getParameterListForSubmit(final SubmittableElement submitElement) {
        final Collection submittableElements = getSubmittableElements(submitElement);

        final List parameterList = new ArrayList(submittableElements.size());
        final Iterator iterator = submittableElements.iterator();
        while (iterator.hasNext()) {
            final SubmittableElement element = (SubmittableElement)iterator.next();
            final KeyValuePair[] pairs = element.getSubmitKeyValuePairs();

            for (int i = 0; i < pairs.length; i++) {
                parameterList.add(pairs[i]);
            }
        }

        if (fakeSelectedRadioButton_ != null) {
            adjustParameterListToAccountForFakeSelectedRadioButton(parameterList);
        }
        return parameterList;
    }

    /**
     * Reset this form to its initial values.
     * @return The page that is loaded at the end of calling this method.  Typically this
     * will be the same page that had been loaded previously but since javascript might
     * have run, this isn't guarenteed.
     */
    public Page reset() {
        final HtmlPage htmlPage = getPage();
        final ScriptResult scriptResult = fireEvent(Event.TYPE_RESET);
        if (scriptResult != null && Boolean.FALSE.equals(scriptResult.getJavaScriptResult())) {
            return scriptResult.getNewPage();
        }

        final Iterator elementIterator = getAllHtmlChildElements();
        while (elementIterator.hasNext()) {
            final Object next = elementIterator.next();
            if (next instanceof SubmittableElement) {
                ((SubmittableElement) next).reset();
            }
        }

        return htmlPage;
    }

    /**
     *  Return a collection of elements that represent all the "submittable"
     *  elements in this form
     *
     * @param submitElement The element that would have been pressed to submit the
     * form or null if the form was submitted by javascript.
     * @return See above
     */
    public Collection getSubmittableElements(final SubmittableElement submitElement) {

        final List submittableElements = new ArrayList();

        final Iterator iterator = getAllHtmlChildElements();
        while (iterator.hasNext()) {
            final HtmlElement element = (HtmlElement)iterator.next();
            if (isSubmittable(element, submitElement)) {
                submittableElements.add(element);
            }
        }

        return submittableElements;
    }

    private boolean isValidForSubmission(final HtmlElement element, final SubmittableElement submitElement) {
        final String tagName = element.getTagName();
        if (!SUBMITTABLE_ELEMENT_NAMES.contains(tagName.toLowerCase())) {
            return false;
        }
        if (element.isAttributeDefined("disabled")) {
            return false;
        }
        // clicked input type="image" is submittted even if it hasn't a name
        if (element == submitElement && element instanceof HtmlImageInput) {
            return true;
        }

        if (!tagName.equals("isindex") && !element.isAttributeDefined("name")) {
            return false;
        }

        if (!tagName.equals("isindex") && element.getAttributeValue("name").equals("")) {
            return false;
        }

        if (tagName.equals("input")) {
            final String type = element.getAttributeValue("type").toLowerCase();
            if (type.equals("radio") || type.equals("checkbox")) {
                return element.isAttributeDefined("checked");
            }
        }
        if (tagName.equals("select")) {
            return ((HtmlSelect) element).isValidForSubmission();
        }
        return true;
    }

    /**
     * @param element The element that we are checking for isSubmittable
     * @param submitElement The element that would have been pressed to submit the
     * form or null if the form was submitted by javascript.
     * @return true if element is submittable
     */
    private boolean isSubmittable(final HtmlElement element, final SubmittableElement submitElement) {
        final String tagName = element.getTagName();
        if (!isValidForSubmission(element, submitElement)) {
            return false;
        }

        // The one submit button that was clicked can be submitted but no other ones
        if (element == submitElement) {
            return true;
        }
        if (tagName.equals("input")) {
            final HtmlInput input = (HtmlInput)element;
            final String type = input.getTypeAttribute().toLowerCase();
            if (type.equals("submit") || type.equals("image") || type.equals("reset") || type.equals("button")) {
                return false;
            }
        }
        if (tagName.equals("button")) {
            return false;
        }

        return true;
    }

    /**
     *  Return the input tags that have the specified name
     *
     * @param name The name of the input
     * @return A list of HtmlInputs
     */
    public List getInputsByName(final String name) {
        return getHtmlElementsByAttribute("input", "name", name);
    }

    /**
     *  Return the first input with the specified name
     *
     * @param name The name of the input
     * @return The input
     * @throws ElementNotFoundException If no inputs could be found with the specified name.
     */
    public final HtmlInput getInputByName(final String name) throws ElementNotFoundException {
        final List inputs = getHtmlElementsByAttribute("input", "name", name);
        if (inputs.size() == 0) {
            throw new ElementNotFoundException("input", "name", name);
        }
        else {
            return (HtmlInput)inputs.get(0);
        }
    }

    /**
     *  Return the "radio" type input field that matches the specified name and value
     *
     * @param name The name of the HtmlInput
     * @param value The value of the HtmlInput
     * @return See above
     * @exception ElementNotFoundException If the field could not be found
     *
     * @deprecated Deprecated because 'name' and 'value' are sometimes not unique to select a single
     * HtmlRadioButtonInput, it should not be called,
     * you can use {@link #getByXPath(String)} instead.
     */
    //TODO: to be removed, deprecated in 4 June 2007
    public HtmlRadioButtonInput getRadioButtonInput(final String name, final String value)
        throws
            ElementNotFoundException {

        final Iterator iterator = getAllHtmlChildElements();
        while (iterator.hasNext()) {
            final HtmlElement element = (HtmlElement)iterator.next();

            if (element instanceof HtmlRadioButtonInput
                    && element.getAttributeValue("name").equals(name)) {

                final HtmlRadioButtonInput input = (HtmlRadioButtonInput)element;
                if (input.getValueAttribute().equals(value)) {
                    return input;
                }
            }
        }
        throw new ElementNotFoundException("input", "value", value);
    }

    /**
     *  Return all the HtmlSelect that match the specified name
     *
     * @param name The name
     * @return See above
     */
    public List getSelectsByName(final String name) {
        return getHtmlElementsByAttribute("select", "name", name);
    }

    /**
     * Find the first select element with the specified name
     * @param name The name of the select element
     * @return The first select.
     * @throws ElementNotFoundException If the select cannot be found.
     */
    public HtmlSelect getSelectByName(final String name) throws ElementNotFoundException {
        final List list = getSelectsByName(name);
        if (list.isEmpty()) {
            throw new ElementNotFoundException("select", "name", name);
        }
        else {
            return (HtmlSelect)list.get(0);
        }
    }

    /**
     *  Return all the HtmlButtons that match the specified name
     *
     * @param name The name
     * @return See above
     * @exception ElementNotFoundException If no matching buttons were found
     */
    public List getButtonsByName(final String name)
        throws ElementNotFoundException {
        return getHtmlElementsByAttribute("button", "name", name);
    }

    /**
     * Find the first button element with the specified name.
     * @param name The name of the button element.
     * @return The first button.
     * @throws ElementNotFoundException If the button cannot be found.
     */
    public HtmlButton getButtonByName(final String name) throws ElementNotFoundException {
        final List list = getButtonsByName(name);
        if (list.isEmpty()) {
            throw new ElementNotFoundException("button", "name", name);
        }
        else {
            return (HtmlButton)list.get(0);
        }
    }
    
    /**
     *  Return all the HtmlTextAreas that match the specified name
     *
     * @param name The name
     * @return See above
     */
    public List getTextAreasByName(final String name) {
        return getHtmlElementsByAttribute("textarea", "name", name);
    }

    /**
     * Find the first textarea element with the specified name.
     * @param name The name of the textarea element.
     * @return The first textarea.
     * @throws ElementNotFoundException If the textarea cannot be found.
     */
    public HtmlTextArea getTextAreaByName(final String name) throws ElementNotFoundException {
        final List list = getTextAreasByName(name);
        if (list.isEmpty()) {
            throw new ElementNotFoundException("textarea", "name", name);
        }
        else {
            return (HtmlTextArea)list.get(0);
        }
    }
        
    /**
     *  Return a list of HtmlInputs that are of type radio and match the
     *  specified name
     *
     * @param name The name
     * @return See above
     */
    public List getRadioButtonsByName(final String name) {

        Assert.notNull("name", name);

        final List results = new ArrayList();

        final Iterator iterator = getAllHtmlChildElements();
        while (iterator.hasNext()) {
            final HtmlElement element = (HtmlElement)iterator.next();
            if (element instanceof HtmlRadioButtonInput
                     && element.getAttributeValue("name").equals(name)) {
                results.add(element);
            }
        }

        return results;
    }

    /**
     *  Select the specified radio button in the form. <p/>
     *
     *  Only a radio button that is actually contained in the form can be
     *  selected. If you need to be able to select a button that really isn't
     *  there (ie during testing of error cases) then use {@link
     *  #fakeCheckedRadioButton(String,String)} instead
     *
     * @param name The name of the radio buttons
     * @param value The value to match
     * @exception ElementNotFoundException If the specified element could not be found
     *
     * @deprecated Deprecated because 'name' and 'value' are sometimes not unique to select a single
     * HtmlRadioButtonInput, it should not be called,
     * you can use {@link #getByXPath(String)} instead.
     */
    //TODO: to be removed, deprecated in 4 June 2007
    public void setCheckedRadioButton(
            final String name,
            final String value)
        throws
            ElementNotFoundException {

        //we could do this with one iterator, but that would set the state of the other
        //radios also in the case where the specified one is not found
        final HtmlInput inputToSelect = getRadioButtonInput(name, value);

        final Iterator iterator = getAllHtmlChildElements();
        while (iterator.hasNext()) {
            final HtmlElement element = (HtmlElement)iterator.next();
            if (element instanceof HtmlRadioButtonInput
                     && element.getAttributeValue("name").equals(name)) {

                final HtmlRadioButtonInput input = (HtmlRadioButtonInput)element;
                if (input == inputToSelect) {
                    input.setAttributeValue("checked", "checked");
                }
                else {
                    input.removeAttribute("checked");
                }
            }
        }
    }

    /**
     *  Select the specified radio button in the form.
     *
     *  Only a radio button that is actually contained in the form can be
     *  selected.
     *
     * @param radioButtonInput The radio Button
     */
    void setCheckedRadioButton(final HtmlRadioButtonInput radioButtonInput) {
        try {
            boolean isChild = false;
             
            for (DomNode parent = radioButtonInput.getParentNode(); parent != null; parent = parent.getParentNode()) {
                if (parent == this) {
                    isChild = true;
                    break;
                }
            }
            if (!isChild) {
                throw new IllegalArgumentException("HtmlRadioButtonInput is not child of this HtmlForm");
            }
            final Iterator iterator = getByXPath(
                    "//input[lower-case(@type)='radio' and @name='" + radioButtonInput.getNameAttribute() + "']"
            ).iterator();
            
            while (iterator.hasNext()) {
                final HtmlRadioButtonInput input = (HtmlRadioButtonInput)iterator.next();
                if (input == radioButtonInput) {
                    input.setAttributeValue("checked", "checked");
                }
                else {
                    input.removeAttribute("checked");
                }
            }
        }
        catch (final JaxenException e) {
            getLog().error(e);
        }
    }

    /**
     *  Set the "selected radio button" to a value that doesn't actually exist
     *  in the page. This is useful primarily for testing error cases.
     *
     * @param name The name of the radio buttons
     * @param value The value to match
     * @exception ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public final void fakeCheckedRadioButton(
            final String name,
            final String value)
        throws
            ElementNotFoundException {

        fakeSelectedRadioButton_ = new KeyValuePair(name, value);
    }

    private void adjustParameterListToAccountForFakeSelectedRadioButton(final List list) {
        final String fakeRadioButtonName = fakeSelectedRadioButton_.getKey();

        // Remove any pairs that match the name of the radio button
        final Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            final KeyValuePair pair = (KeyValuePair)iterator.next();
            if (pair.getKey().equals(fakeRadioButtonName)) {
                iterator.remove();
            }
        }

        // Now add this one back in
        list.add(fakeSelectedRadioButton_);
    }

    /**
     * Return the first checked radio button with the specified name.  If none of
     * the radio buttons by that name are checked then return null.
     *
     * @param name The name of the radio button
     * @return The first checked radio button.
     */
    public HtmlRadioButtonInput getCheckedRadioButton(final String name) {

        Assert.notNull("name", name);

        final Iterator iterator = getAllHtmlChildElements();
        while (iterator.hasNext()) {
            final HtmlElement element = (HtmlElement)iterator.next();
            if (element instanceof HtmlRadioButtonInput
                     && element.getAttributeValue("name").equals(name)) {

                final HtmlRadioButtonInput input = (HtmlRadioButtonInput)element;
                if (input.isChecked()) {
                    return input;
                }
            }
        }
        return null;
    }

    /**
     *  Return the value of the attribute "action". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return The value of the attribute "action" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getActionAttribute() {
        return getAttributeValue("action");
    }

    /**
     *  Set the value of the attribute "action". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @param action  The value of the attribute "action"
     */
    public final void setActionAttribute(final String action) {
        setAttributeValue("action", action);
    }

    /**
     *  Return the value of the attribute "method". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return The value of the attribute "method" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getMethodAttribute() {
        return getAttributeValue("method");
    }

    /**
     *  Set the value of the attribute "method". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @param method  The value of the attribute "method" or an empty string if that
     *      attribute isn't defined.
     */
    public final void setMethodAttribute(final String method) {
        setAttributeValue("method", method);
    }

    /**
     *  Return the value of the attribute "name". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return The value of the attribute "name" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttributeValue("name");
    }

    /**
     *  Set the value of the attribute "name". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.<p>
     *
     * @param name the new value
     */
    public final void setNameAttribute(final String name) {
        setAttributeValue("name", name);
    }

    /**
     *  Return the value of the attribute "enctype". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.<p>
     *
     *  Enctype is the encoding type used when submitting a form back to the server
     *
     * @return The value of the attribute "enctype" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getEnctypeAttribute() {
        return getAttributeValue("enctype");
    }

    /**
     *  Set the value of the attribute "enctype". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.<p>
     *
     *  Enctype is the encoding type used when submitting a form back to the server
     *
     * @param encoding The value of the attribute "enctype" or an empty string if that
     *      attribute isn't defined.
     */
    public final void setEnctypeAttribute(final String encoding) {
        setAttributeValue("enctype", encoding);
    }

    /**
     *  Return the value of the attribute "onsubmit". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return The value of the attribute "onsubmit" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getOnSubmitAttribute() {
        return getAttributeValue("onsubmit");
    }

    /**
     *  Return the value of the attribute "onreset". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return The value of the attribute "onreset" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getOnResetAttribute() {
        return getAttributeValue("onreset");
    }

    /**
     *  Return the value of the attribute "accept". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return The value of the attribute "accept" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getAcceptAttribute() {
        return getAttributeValue("accept");
    }

    /**
     * Return the value of the attribute "accept-charset". Refer to the <a
     * href='http://www.w3.org/TR/html401/interact/forms.html#adef-accept-charset'>
     * HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "accept-charset" or an empty string
     *      if that attribute isn't defined.
     */
    public final String getAcceptCharsetAttribute() {
        return getAttributeValue("accept-charset");
    }

    /**
     *  Return the value of the attribute "target". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return The value of the attribute "target" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getTargetAttribute() {
        return getAttributeValue("target");
    }

    /**
     *  Set the value of the attribute "target". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @param target  The value of the attribute "target" or an empty string if that
     *      attribute isn't defined.
     */
    public final void setTargetAttribute(final String target) {
        setAttributeValue("target", target);
    }

    /**
     * Return the first input with the specified value.
     * @param value The value
     * @return The first input with the specified value.
     * @throws ElementNotFoundException If no elements can be found with the specified value.
     */
    public HtmlInput getInputByValue(final String value) throws ElementNotFoundException {
        return (HtmlInput)getOneHtmlElementByAttribute("input", "value", value);
    }

    /**
     * Return all the inputs with the specified value.
     * @param value The value
     * @return all the inputs with the specified value.
     */
    public List getInputsByValue(final String value) {
        return getHtmlElementsByAttribute("input", "value", value);
    }
}
