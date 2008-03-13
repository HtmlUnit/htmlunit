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

import static com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection.JAVASCRIPT_PREFIX;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebAssert;
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

    private static final Collection<String> SUBMITTABLE_ELEMENT_NAMES =
        Arrays.asList(new String[]{"input", "button", "select", "textarea", "isindex"});

    /**
     * Create an instance
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param htmlPage The page that contains this element
     * @param attributes the initial attributes
     */
    HtmlForm(final String namespaceURI, final String qualifiedName, final HtmlPage htmlPage,
            final Map<String, HtmlAttr> attributes) {
        super(namespaceURI, qualifiedName, htmlPage, attributes);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Submit this form to the appropriate server.  If submitElement is null then
     * treat this as if it was called by javascript.  In this case, the onsubmit
     * handler will not get executed.
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
            if (TextUtil.startsWithIgnoreCase(action, JAVASCRIPT_PREFIX)) {
                return htmlPage.executeJavaScriptIfPossible(action, "Form action", getStartLineNumber()).getNewPage();
            }
        }
        else {
            if (TextUtil.startsWithIgnoreCase(getActionAttribute(), JAVASCRIPT_PREFIX)) {
                // The action is JavaScript but JavaScript isn't enabled.
                // Return the current page.
                return htmlPage;
            }
        }

        final List<NameValuePair> parameters = getParameterListForSubmit(submitElement);
        final SubmitMethod method;
        final String methodAttribute = getMethodAttribute();
        if ("post".equalsIgnoreCase(methodAttribute)) {
            method = SubmitMethod.POST;
        }
        else {
            if (!"get".equalsIgnoreCase(methodAttribute) && methodAttribute.trim().length() > 0) {
                notifyIncorrectness("Incorrect submit method >" + getMethodAttribute() + "<. Using >GET<.");
            }
            method = SubmitMethod.GET;
        }

        String actionUrl = getActionAttribute();
        if (SubmitMethod.GET.equals(method)) {
            final String anchor = StringUtils.substringAfter(actionUrl, "#");
            actionUrl = StringUtils.substringBefore(actionUrl, "#");

            final NameValuePair[] pairs = new NameValuePair[parameters.size()];
            parameters.toArray(pairs);
            final String queryFromFields = EncodingUtil.formUrlEncode(pairs, getPage().getPageEncoding());
            
            // action may already contain some query parameters: they have to be removed
            actionUrl = StringUtils.substringBefore(actionUrl, "?");
            final BrowserVersion browserVersion = getPage().getWebClient().getBrowserVersion();
            if (!(browserVersion.isIE() && browserVersion.getBrowserVersionNumeric() >= 7)
                    || queryFromFields.length() > 0) {
                actionUrl += "?" + queryFromFields;
            }
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
    private List<NameValuePair> getParameterListForSubmit(final SubmittableElement submitElement) {
        final Collection<SubmittableElement> submittableElements = getSubmittableElements(submitElement);

        final List<NameValuePair> parameterList = new ArrayList<NameValuePair>(submittableElements.size());
        for (final SubmittableElement element : submittableElements) {

            for (final NameValuePair pair : element.getSubmitKeyValuePairs()) {
                parameterList.add(pair);
            }
        }

        return parameterList;
    }

    /**
     * Reset this form to its initial values.
     * @return The page that is loaded at the end of calling this method.  Typically this
     * will be the same page that had been loaded previously but since javascript might
     * have run, this isn't guaranteed.
     */
    public Page reset() {
        final HtmlPage htmlPage = getPage();
        final ScriptResult scriptResult = fireEvent(Event.TYPE_RESET);
        if (scriptResult != null && Boolean.FALSE.equals(scriptResult.getJavaScriptResult())) {
            return scriptResult.getNewPage();
        }

        for (final HtmlElement next : getAllHtmlChildElements()) {
            if (next instanceof SubmittableElement) {
                ((SubmittableElement) next).reset();
            }
        }

        return htmlPage;
    }

    /**
     * Return a collection of elements that represent all the "submittable" elements in this form
     *
     * @param submitElement The element that would have been pressed to submit the
     * form or null if the form was submitted by javascript.
     * @return See above
     */
    Collection<SubmittableElement> getSubmittableElements(final SubmittableElement submitElement) {
        final List<SubmittableElement> submittableElements = new ArrayList<SubmittableElement>();

        for (final HtmlElement element : getAllHtmlChildElements()) {
            if (isSubmittable(element, submitElement)) {
                submittableElements.add((SubmittableElement) element);
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
        // clicked input type="image" is submitted even if it hasn't a name
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
            final HtmlInput input = (HtmlInput) element;
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
     * Return the input tags that have the specified name
     *
     * @param name The name of the input
     * @return A list of HtmlInputs
     */
    @SuppressWarnings("unchecked")
    public List<HtmlInput> getInputsByName(final String name) {
        return (List<HtmlInput>) getHtmlElementsByAttribute("input", "name", name);
    }

    /**
     * Return the first input with the specified name
     *
     * @param name The name of the input
     * @return The input
     * @throws ElementNotFoundException If no inputs could be found with the specified name.
     */
    public final HtmlInput getInputByName(final String name) throws ElementNotFoundException {
        final List< ? extends HtmlElement> inputs = getHtmlElementsByAttribute("input", "name", name);
        if (inputs.size() == 0) {
            throw new ElementNotFoundException("input", "name", name);
        }
        else {
            return (HtmlInput) inputs.get(0);
        }
    }

    /**
     * Return all the HtmlSelect that match the specified name
     *
     * @param name The name
     * @return See above
     */
    @SuppressWarnings("unchecked")
    public List<HtmlSelect> getSelectsByName(final String name) {
        return (List<HtmlSelect>) getHtmlElementsByAttribute("select", "name", name);
    }

    /**
     * Find the first select element with the specified name
     * @param name The name of the select element
     * @return The first select.
     * @throws ElementNotFoundException If the select cannot be found.
     */
    public HtmlSelect getSelectByName(final String name) throws ElementNotFoundException {
        final List<HtmlSelect> list = getSelectsByName(name);
        if (list.isEmpty()) {
            throw new ElementNotFoundException("select", "name", name);
        }
        else {
            return list.get(0);
        }
    }

    /**
     * Return all the HtmlButtons that match the specified name
     *
     * @param name The name
     * @return See above
     * @exception ElementNotFoundException If no matching buttons were found
     */
    @SuppressWarnings("unchecked")
    public List<HtmlButton> getButtonsByName(final String name) throws ElementNotFoundException {
        return (List<HtmlButton>) getHtmlElementsByAttribute("button", "name", name);
    }

    /**
     * Find the first button element with the specified name.
     * @param name The name of the button element.
     * @return The first button.
     * @throws ElementNotFoundException If the button cannot be found.
     */
    public HtmlButton getButtonByName(final String name) throws ElementNotFoundException {
        final List<HtmlButton> list = getButtonsByName(name);
        if (list.isEmpty()) {
            throw new ElementNotFoundException("button", "name", name);
        }
        else {
            return list.get(0);
        }
    }
    
    /**
     * Return all the HtmlTextAreas that match the specified name
     *
     * @param name The name
     * @return See above
     */
    @SuppressWarnings("unchecked")
    public List<HtmlTextArea> getTextAreasByName(final String name) {
        return (List<HtmlTextArea>) getHtmlElementsByAttribute("textarea", "name", name);
    }

    /**
     * Find the first textarea element with the specified name.
     * @param name The name of the textarea element.
     * @return The first textarea.
     * @throws ElementNotFoundException If the textarea cannot be found.
     */
    public HtmlTextArea getTextAreaByName(final String name) throws ElementNotFoundException {
        final List<HtmlTextArea> list = getTextAreasByName(name);
        if (list.isEmpty()) {
            throw new ElementNotFoundException("textarea", "name", name);
        }
        else {
            return (HtmlTextArea) list.get(0);
        }
    }
        
    /**
     * Return a list of HtmlInputs that are of type radio and match the specified name
     *
     * @param name The name
     * @return See above
     */
    public List<HtmlRadioButtonInput> getRadioButtonsByName(final String name) {
        WebAssert.notNull("name", name);

        final List<HtmlRadioButtonInput> results = new ArrayList<HtmlRadioButtonInput>();

        for (final HtmlElement element : getAllHtmlChildElements()) {
            if (element instanceof HtmlRadioButtonInput
                     && element.getAttributeValue("name").equals(name)) {
                results.add((HtmlRadioButtonInput) element);
            }
        }

        return results;
    }

    /**
     * Select the specified radio button in the form.
     *
     * Only a radio button that is actually contained in the form can be selected.
     *
     * @param radioButtonInput The radio Button
     */
    @SuppressWarnings("unchecked")
    void setCheckedRadioButton(final HtmlRadioButtonInput radioButtonInput) {
        if (!isAncestorOf(radioButtonInput)) {
            throw new IllegalArgumentException("HtmlRadioButtonInput is not child of this HtmlForm");
        }
        final List<HtmlRadioButtonInput> radios = (List<HtmlRadioButtonInput>) getByXPath(
                ".//input[lower-case(@type)='radio' and @name='" + radioButtonInput.getNameAttribute() + "']"
        );
            
        for (final HtmlRadioButtonInput input : radios) {
            if (input == radioButtonInput) {
                input.setAttributeValue("checked", "checked");
            }
            else {
                input.removeAttribute("checked");
            }
        }
    }

    /**
     * Return the first checked radio button with the specified name.  If none of
     * the radio buttons by that name are checked then return null.
     *
     * @param name The name of the radio button
     * @return The first checked radio button.
     */
    public HtmlRadioButtonInput getCheckedRadioButton(final String name) {
        WebAssert.notNull("name", name);

        for (final HtmlElement element : getAllHtmlChildElements()) {
            if (element instanceof HtmlRadioButtonInput
                     && element.getAttributeValue("name").equals(name)) {

                final HtmlRadioButtonInput input = (HtmlRadioButtonInput) element;
                if (input.isChecked()) {
                    return input;
                }
            }
        }
        return null;
    }

    /**
     * Return the value of the attribute "action". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return The value of the attribute "action" or an empty string if that attribute isn't defined.
     */
    public final String getActionAttribute() {
        return getAttributeValue("action");
    }

    /**
     * Set the value of the attribute "action". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @param action  The value of the attribute "action"
     */
    public final void setActionAttribute(final String action) {
        setAttributeValue("action", action);
    }

    /**
     * Return the value of the attribute "method". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return The value of the attribute "method" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getMethodAttribute() {
        return getAttributeValue("method");
    }

    /**
     * Set the value of the attribute "method". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @param method  The value of the attribute "method" or an empty string if that attribute isn't defined.
     */
    public final void setMethodAttribute(final String method) {
        setAttributeValue("method", method);
    }

    /**
     * Return the value of the attribute "name". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return The value of the attribute "name" or an empty string if that attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttributeValue("name");
    }

    /**
     * Set the value of the attribute "name". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for details on the use of this attribute.<p>
     *
     * @param name the new value
     */
    public final void setNameAttribute(final String name) {
        setAttributeValue("name", name);
    }

    /**
     * Return the value of the attribute "enctype". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for details on the use of this attribute.<p>
     *
     *  Enctype is the encoding type used when submitting a form back to the server
     *
     * @return The value of the attribute "enctype" or an empty string if that attribute isn't defined.
     */
    public final String getEnctypeAttribute() {
        return getAttributeValue("enctype");
    }

    /**
     * Set the value of the attribute "enctype". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.<p>
     *
     * Enctype is the encoding type used when submitting a form back to the server
     *
     * @param encoding The value of the attribute "enctype" or an empty string if that attribute isn't defined.
     */
    public final void setEnctypeAttribute(final String encoding) {
        setAttributeValue("enctype", encoding);
    }

    /**
     * Return the value of the attribute "onsubmit". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return The value of the attribute "onsubmit" or an empty string if that attribute isn't defined.
     */
    public final String getOnSubmitAttribute() {
        return getAttributeValue("onsubmit");
    }

    /**
     * Return the value of the attribute "onreset". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onreset" or an empty string if that attribute isn't defined.
     */
    public final String getOnResetAttribute() {
        return getAttributeValue("onreset");
    }

    /**
     * Return the value of the attribute "accept". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "accept" or an empty string if that attribute isn't defined.
     */
    public final String getAcceptAttribute() {
        return getAttributeValue("accept");
    }

    /**
     * Return the value of the attribute "accept-charset". Refer to the <a
     * href='http://www.w3.org/TR/html401/interact/forms.html#adef-accept-charset'>
     * HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "accept-charset" or an empty string if that attribute isn't defined.
     */
    public final String getAcceptCharsetAttribute() {
        return getAttributeValue("accept-charset");
    }

    /**
     * Return the value of the attribute "target". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "target" or an empty string if that attribute isn't defined.
     */
    public final String getTargetAttribute() {
        return getAttributeValue("target");
    }

    /**
     * Set the value of the attribute "target". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @param target  The value of the attribute "target" or an empty string if that attribute isn't defined.
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
        return (HtmlInput) getOneHtmlElementByAttribute("input", "value", value);
    }

    /**
     * Return all the inputs with the specified value.
     * @param value The value
     * @return all the inputs with the specified value.
     */
    @SuppressWarnings("unchecked")
    public List<HtmlInput> getInputsByValue(final String value) {
        return (List<HtmlInput>) getHtmlElementsByAttribute("input", "value", value);
    }
}
