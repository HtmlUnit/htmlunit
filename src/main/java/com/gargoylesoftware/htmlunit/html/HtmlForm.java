/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Wrapper for the HTML element "form".
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
 * @author Ronald Brill
 */
public class HtmlForm extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "form";

    private static final Collection<String> SUBMITTABLE_ELEMENT_NAMES =
        Arrays.asList(new String[]{"input", "button", "select", "textarea", "isindex"});

    private static final Pattern SUBMIT_CHARSET_PATTERN = Pattern.compile("[ ,].*");

    private final List<HtmlElement> lostChildren_ = new ArrayList<HtmlElement>();

    private boolean isPreventDefault_;

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param htmlPage the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlForm(final String namespaceURI, final String qualifiedName, final SgmlPage htmlPage,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, htmlPage, attributes);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * <p>Submits this form to the server. If <tt>submitElement</tt> is <tt>null</tt>, then
     * the submission is treated as if it was triggered by JavaScript, and the <tt>onsubmit</tt>
     * handler will not be executed.</p>
     *
     * <p><b>IMPORTANT:</b> Using this method directly is not the preferred way of submitting forms.
     * Most consumers should emulate the user's actions instead, probably by using something like
     * {@link HtmlElement#click()} or {@link HtmlElement#dblClick()}.</p>
     *
     * @param submitElement the element that caused the submit to occur
     * @return a new page that reflects the results of this submission
     * @exception IOException if an IO error occurs
     */
    Page submit(final SubmittableElement submitElement) throws IOException {
        final HtmlPage htmlPage = (HtmlPage) getPage();
        final WebClient webClient = htmlPage.getWebClient();
        if (webClient.isJavaScriptEnabled()) {
            if (submitElement != null) {
                isPreventDefault_ = false;
                final ScriptResult scriptResult = fireEvent(Event.TYPE_SUBMIT);
                if (isPreventDefault_) {
                    // null means 'nothing executed'
                    if (scriptResult == null) {
                        return htmlPage;
                    }
                    return scriptResult.getNewPage();
                }
            }

            final String action = getActionAttribute().trim();
            if (StringUtils.startsWithIgnoreCase(action, JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
                return htmlPage.executeJavaScriptIfPossible(action, "Form action", getStartLineNumber()).getNewPage();
            }
        }
        else {
            if (StringUtils.startsWithIgnoreCase(getActionAttribute(), JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
                // The action is JavaScript but JavaScript isn't enabled.
                // Return the current page.
                return htmlPage;
            }
        }

        final WebRequest request = getWebRequest(submitElement);
        final String target = htmlPage.getResolvedTarget(getTargetAttribute());

        final WebWindow webWindow = htmlPage.getEnclosingWindow();
        final String action = getActionAttribute();
        final boolean isHashJump = HttpMethod.GET.equals(request.getHttpMethod()) && action.endsWith("#");
        webClient.download(webWindow, target, request, isHashJump, "JS form.submit()");
        return htmlPage;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Gets the request for a submission of this form with the specified SubmittableElement.
     * @param submitElement the element that caused the submit to occur
     * @return the request
     */
    public WebRequest getWebRequest(final SubmittableElement submitElement) {
        final HtmlPage htmlPage = (HtmlPage) getPage();
        final List<NameValuePair> parameters = getParameterListForSubmit(submitElement);
        final HttpMethod method;
        final String methodAttribute = getMethodAttribute();
        if ("post".equalsIgnoreCase(methodAttribute)) {
            method = HttpMethod.POST;
        }
        else {
            if (!"get".equalsIgnoreCase(methodAttribute) && StringUtils.isNotBlank(methodAttribute)) {
                notifyIncorrectness("Incorrect submit method >" + getMethodAttribute() + "<. Using >GET<.");
            }
            method = HttpMethod.GET;
        }

        final BrowserVersion browser = getPage().getWebClient().getBrowserVersion();
        String actionUrl = getActionAttribute();
        if (HttpMethod.GET == method) {
            final String anchor = StringUtils.substringAfter(actionUrl, "#");
            final String enc = getPage().getPageEncoding();
            final String queryFromFields =
                URLEncodedUtils.format(Arrays.asList(NameValuePair.toHttpClient(parameters)), enc);

            // action may already contain some query parameters: they have to be removed
            actionUrl = StringUtils.substringBefore(actionUrl, "#");
            actionUrl = StringUtils.substringBefore(actionUrl, "?");
            if (browser.hasFeature(BrowserVersionFeatures.FORM_SUBMISSION_URL_END_WITH_QUESTIONMARK)
                    || queryFromFields.length() > 0) {
                actionUrl += "?" + queryFromFields;
            }
            if (anchor.length() > 0
                    && !browser.hasFeature(BrowserVersionFeatures.FORM_SUBMISSION_URL_WITHOUT_HASH)) {
                actionUrl += "#" + anchor;
            }
            parameters.clear(); // parameters have been added to query
        }
        URL url;
        try {
            if (actionUrl.length() == 0) {
                url = htmlPage.getWebResponse().getWebRequest().getUrl();
                if (browser.hasFeature(BrowserVersionFeatures.FORM_SUBMISSION_URL_WITHOUT_HASH)) {
                    url = UrlUtils.getUrlWithNewRef(url, null);
                }
            }
            else if (actionUrl.startsWith("?")) {
                String urlString = htmlPage.getWebResponse().getWebRequest().getUrl().toExternalForm();
                if (urlString.indexOf('?') != -1) {
                    urlString = urlString.substring(0, urlString.indexOf('?'));
                }
                else if (urlString.indexOf('#') != -1
                        && browser.hasFeature(BrowserVersionFeatures.FORM_SUBMISSION_URL_WITHOUT_HASH)) {
                    urlString = urlString.substring(0, urlString.indexOf('#'));
                }
                url = new URL(urlString + actionUrl);
            }
            else {
                url = htmlPage.getFullyQualifiedUrl(actionUrl);
            }
        }
        catch (final MalformedURLException e) {
            throw new IllegalArgumentException("Not a valid url: " + actionUrl);
        }

        final WebRequest request = new WebRequest(url, method);
        request.setRequestParameters(parameters);
        if (HttpMethod.POST == method) {
            request.setEncodingType(FormEncodingType.getInstance(getEnctypeAttribute()));
        }
        request.setCharset(getSubmitCharset());
        request.setAdditionalHeader("Referer", htmlPage.getWebResponse().getWebRequest().getUrl()
                .toExternalForm());
        return request;
    }

    /**
     * Returns the charset to use for the form submission. This is the first one
     * from the list provided in {@link #getAcceptCharsetAttribute()} if any
     * or the page's charset else
     * @return the charset to use for the form submission
     */
    private String getSubmitCharset() {
        if (getAcceptCharsetAttribute().length() > 0) {
            return SUBMIT_CHARSET_PATTERN.matcher(getAcceptCharsetAttribute().trim()).replaceAll("");
        }
        return getPage().getPageEncoding();
    }

    /**
     * Returns a list of {@link KeyValuePair}s that represent the data that will be
     * sent to the server when this form is submitted. This is primarily intended to aid
     * debugging.
     *
     * @param submitElement the element used to submit the form, or <tt>null</tt> if the
     *        form was submitted by JavaScript
     * @return the list of {@link KeyValuePair}s that represent that data that will be sent
     *         to the server when this form is submitted
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
     * Resets this form to its initial values, returning the page contained by this form's window after the
     * reset. Note that the returned page may or may not be the same as the original page, based on JavaScript
     * event handlers, etc.
     *
     * @return the page contained by this form's window after the reset
     */
    public Page reset() {
        final SgmlPage htmlPage = getPage();
        final ScriptResult scriptResult = fireEvent(Event.TYPE_RESET);
        if (ScriptResult.isFalse(scriptResult)) {
            return scriptResult.getNewPage();
        }

        for (final HtmlElement next : getHtmlElementDescendants()) {
            if (next instanceof SubmittableElement) {
                ((SubmittableElement) next).reset();
            }
        }

        return htmlPage;
    }

    /**
     * Returns a collection of elements that represent all the "submittable" elements in this form,
     * assuming that the specified element is used to submit the form.
     *
     * @param submitElement the element used to submit the form, or <tt>null</tt> if the
     *        form is submitted by JavaScript
     * @return a collection of elements that represent all the "submittable" elements in this form
     */
    Collection<SubmittableElement> getSubmittableElements(final SubmittableElement submitElement) {
        final List<SubmittableElement> submittableElements = new ArrayList<SubmittableElement>();

        for (final HtmlElement element : getFormHtmlElementDescendants()) {
            if (isSubmittable(element, submitElement)) {
                submittableElements.add((SubmittableElement) element);
            }
        }

        for (final HtmlElement element : lostChildren_) {
            if (isSubmittable(element, submitElement)) {
                submittableElements.add((SubmittableElement) element);
            }
        }

        return submittableElements;
    }

    private boolean isValidForSubmission(final HtmlElement element, final SubmittableElement submitElement) {
        final String tagName = element.getTagName();
        if (!SUBMITTABLE_ELEMENT_NAMES.contains(tagName)) {
            return false;
        }
        if (element.hasAttribute("disabled")) {
            return false;
        }
        // clicked input type="image" is submitted even if it hasn't a name
        if (element == submitElement && element instanceof HtmlImageInput) {
            return true;
        }

        if (!"isindex".equals(tagName) && !element.hasAttribute("name")) {
            return false;
        }

        if (!"isindex".equals(tagName) && "".equals(element.getAttribute("name"))) {
            return false;
        }

        if (element instanceof HtmlInput) {
            final String type = element.getAttribute("type").toLowerCase();
            if ("radio".equals(type) || "checkbox".equals(type)) {
                return element.hasAttribute("checked");
            }
        }
        if ("select".equals(tagName)) {
            return ((HtmlSelect) element).isValidForSubmission();
        }
        return true;
    }

    /**
     * Returns <tt>true</tt> if the specified element gets submitted when this form is submitted,
     * assuming that the form is submitted using the specified submit element.
     *
     * @param element the element to check
     * @param submitElement the element used to submit the form, or <tt>null</tt> if the form is
     *        submitted by JavaScript
     * @return <tt>true</tt> if the specified element gets submitted when this form is submitted
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
        if (element instanceof HtmlInput) {
            final HtmlInput input = (HtmlInput) element;
            final String type = input.getTypeAttribute().toLowerCase();
            if ("submit".equals(type) || "image".equals(type) || "reset".equals(type) || "button".equals(type)) {
                return false;
            }
        }
        if ("button".equals(tagName)) {
            return false;
        }

        return true;
    }

    /**
     * Returns all input elements which are members of this form and have the specified name.
     *
     * @param name the input name to search for
     * @return all input elements which are members of this form and have the specified name
     */
    public List<HtmlInput> getInputsByName(final String name) {
        final List<HtmlInput> list = getFormElementsByAttribute("input", "name", name);

        // collect inputs from lost children
        for (final HtmlElement elt : getLostChildren()) {
            if (elt instanceof HtmlInput && name.equals(elt.getAttribute("name"))) {
                list.add((HtmlInput) elt);
            }
        }
        return list;
    }

    /**
     * Same as {@link #getElementsByAttribute(String, String, String)} but
     * ignoring elements that are contained in a nested form.
     */
    @SuppressWarnings("unchecked")
    private <E extends HtmlElement> List<E> getFormElementsByAttribute(
            final String elementName,
            final String attributeName,
            final String attributeValue) {

        final List<E> list = new ArrayList<E>();
        final String lowerCaseTagName = elementName.toLowerCase();

        for (final HtmlElement next : getFormHtmlElementDescendants()) {
            if (next.getTagName().equals(lowerCaseTagName)) {
                final String attValue = next.getAttribute(attributeName);
                if (attValue != null && attValue.equals(attributeValue)) {
                    list.add((E) next);
                }
            }
        }
        return list;
    }

    /**
     * Same as {@link #getHtmlElementDescendants} but
     * ignoring elements that are contained in a nested form.
     */
    private Iterable<HtmlElement> getFormHtmlElementDescendants() {
        final Iterator<HtmlElement> iter = new DescendantElementsIterator<HtmlElement>(HtmlElement.class) {
            private boolean filterChildrenOfNestedForms_;

            @Override
            protected boolean isAccepted(final DomNode node) {
                if (node instanceof HtmlForm) {
                    filterChildrenOfNestedForms_ = true;
                    return false;
                }

                final boolean accepted = super.isAccepted(node);
                if (accepted && filterChildrenOfNestedForms_) {
                    return ((HtmlElement) node).getEnclosingForm() == HtmlForm.this;
                }
                return accepted;
            }
        };
        return new Iterable<HtmlElement>() {
            public Iterator<HtmlElement> iterator() {
                return iter;
            }
        };
    }

    /**
     * Returns the first input element which is a member of this form and has the specified name.
     *
     * @param name the input name to search for
     * @param <I> the input type
     * @return the first input element which is a member of this form and has the specified name
     * @throws ElementNotFoundException if there is not input in this form with the specified name
     */
    @SuppressWarnings("unchecked")
    public final <I extends HtmlInput> I getInputByName(final String name) throws ElementNotFoundException {
        final List<HtmlInput> inputs = getInputsByName(name);

        if (inputs.isEmpty()) {
            throw new ElementNotFoundException("input", "name", name);
        }
        return (I) inputs.get(0);
    }

    /**
     * Returns all the {@link HtmlSelect} elements in this form that have the specified name.
     *
     * @param name the name to search for
     * @return all the {@link HtmlSelect} elements in this form that have the specified name
     */
    public List<HtmlSelect> getSelectsByName(final String name) {
        final List<HtmlSelect> list = getFormElementsByAttribute("select", "name", name);

        // collect selects from lost children
        for (final HtmlElement elt : getLostChildren()) {
            if (elt instanceof HtmlSelect && name.equals(elt.getAttribute("name"))) {
                list.add((HtmlSelect) elt);
            }
        }
        return list;
    }

    /**
     * Returns the first {@link HtmlSelect} element in this form that has the specified name.
     *
     * @param name the name to search for
     * @return the first {@link HtmlSelect} element in this form that has the specified name
     * @throws ElementNotFoundException if this form does not contain a {@link HtmlSelect}
     *         element with the specified name
     */
    public HtmlSelect getSelectByName(final String name) throws ElementNotFoundException {
        final List<HtmlSelect> list = getSelectsByName(name);
        if (list.isEmpty()) {
            throw new ElementNotFoundException("select", "name", name);
        }
        return list.get(0);
    }

    /**
     * Returns all the {@link HtmlButton} elements in this form that have the specified name.
     *
     * @param name the name to search for
     * @return all the {@link HtmlButton} elements in this form that have the specified name
     */
    public List<HtmlButton> getButtonsByName(final String name) {
        final List<HtmlButton> list = getFormElementsByAttribute("button", "name", name);

        // collect buttons from lost children
        for (final HtmlElement elt : getLostChildren()) {
            if (elt instanceof HtmlButton && name.equals(elt.getAttribute("name"))) {
                list.add((HtmlButton) elt);
            }
        }
        return list;
    }

    /**
     * Returns the first {@link HtmlButton} element in this form that has the specified name.
     *
     * @param name the name to search for
     * @return the first {@link HtmlButton} element in this form that has the specified name
     * @throws ElementNotFoundException if this form does not contain a {@link HtmlButton}
     *         element with the specified name
     */
    public HtmlButton getButtonByName(final String name) throws ElementNotFoundException {
        final List<HtmlButton> list = getButtonsByName(name);
        if (list.isEmpty()) {
            throw new ElementNotFoundException("button", "name", name);
        }
        return list.get(0);
    }

    /**
     * Returns all the {@link HtmlTextArea} elements in this form that have the specified name.
     *
     * @param name the name to search for
     * @return all the {@link HtmlTextArea} elements in this form that have the specified name
     */
    public List<HtmlTextArea> getTextAreasByName(final String name) {
        final List<HtmlTextArea> list = getFormElementsByAttribute("textarea", "name", name);

        // collect buttons from lost children
        for (final HtmlElement elt : getLostChildren()) {
            if (elt instanceof HtmlTextArea && name.equals(elt.getAttribute("name"))) {
                list.add((HtmlTextArea) elt);
            }
        }
        return list;
    }

    /**
     * Returns the first {@link HtmlTextArea} element in this form that has the specified name.
     *
     * @param name the name to search for
     * @return the first {@link HtmlTextArea} element in this form that has the specified name
     * @throws ElementNotFoundException if this form does not contain a {@link HtmlTextArea}
     *         element with the specified name
     */
    public HtmlTextArea getTextAreaByName(final String name) throws ElementNotFoundException {
        final List<HtmlTextArea> list = getTextAreasByName(name);
        if (list.isEmpty()) {
            throw new ElementNotFoundException("textarea", "name", name);
        }
        return list.get(0);
    }

    /**
     * Returns all the {@link HtmlRadioButtonInput} elements in this form that have the specified name.
     *
     * @param name the name to search for
     * @return all the {@link HtmlRadioButtonInput} elements in this form that have the specified name
     */
    public List<HtmlRadioButtonInput> getRadioButtonsByName(final String name) {
        WebAssert.notNull("name", name);

        final List<HtmlRadioButtonInput> results = new ArrayList<HtmlRadioButtonInput>();

        for (final HtmlElement element : getInputsByName(name)) {
            if (element instanceof HtmlRadioButtonInput) {
                results.add((HtmlRadioButtonInput) element);
            }
        }

        return results;
    }

    /**
     * Selects the specified radio button in the form. Only a radio button that is actually contained
     * in the form can be selected.
     *
     * @param radioButtonInput the radio button to select
     */
    void setCheckedRadioButton(final HtmlRadioButtonInput radioButtonInput) {
        if (!isAncestorOf(radioButtonInput) && !lostChildren_.contains(radioButtonInput)) {
            throw new IllegalArgumentException("HtmlRadioButtonInput is not child of this HtmlForm");
        }
        final List<HtmlRadioButtonInput> radios = getRadioButtonsByName(radioButtonInput.getNameAttribute());

        for (final HtmlRadioButtonInput input : radios) {
            if (input == radioButtonInput) {
                input.setAttribute("checked", "checked");
            }
            else {
                input.removeAttribute("checked");
            }
        }
    }

    /**
     * Returns the first checked radio button with the specified name. If none of
     * the radio buttons by that name are checked, this method returns <tt>null</tt>.
     *
     * @param name the name of the radio button
     * @return the first checked radio button with the specified name
     */
    public HtmlRadioButtonInput getCheckedRadioButton(final String name) {
        WebAssert.notNull("name", name);

        for (final HtmlRadioButtonInput input : getRadioButtonsByName(name)) {
            if (input.isChecked()) {
                return input;
            }
        }
        return null;
    }

    /**
     * Returns the value of the attribute "action". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute "action" or an empty string if that attribute isn't defined
     */
    public final String getActionAttribute() {
        return getAttribute("action");
    }

    /**
     * Sets the value of the attribute "action". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @param action the value of the attribute "action"
     */
    public final void setActionAttribute(final String action) {
        setAttribute("action", action);
    }

    /**
     * Returns the value of the attribute "method". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute "method" or an empty string if that attribute isn't defined
     */
    public final String getMethodAttribute() {
        return getAttribute("method");
    }

    /**
     * Sets the value of the attribute "method". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @param method the value of the attribute "method"
     */
    public final void setMethodAttribute(final String method) {
        setAttribute("method", method);
    }

    /**
     * Returns the value of the attribute "name". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute "name" or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttribute("name");
    }

    /**
     * Sets the value of the attribute "name". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @param name the value of the attribute "name"
     */
    public final void setNameAttribute(final String name) {
        setAttribute("name", name);
    }

    /**
     * Returns the value of the attribute "enctype". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute. "Enctype" is the encoding type
     * used when submitting a form back to the server.
     *
     * @return the value of the attribute "enctype" or an empty string if that attribute isn't defined
     */
    public final String getEnctypeAttribute() {
        return getAttribute("enctype");
    }

    /**
     * Sets the value of the attribute "enctype". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute. "Enctype" is the encoding type
     * used when submitting a form back to the server.
     *
     * @param encoding the value of the attribute "enctype"
     */
    public final void setEnctypeAttribute(final String encoding) {
        setAttribute("enctype", encoding);
    }

    /**
     * Returns the value of the attribute "onsubmit". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute "onsubmit" or an empty string if that attribute isn't defined
     */
    public final String getOnSubmitAttribute() {
        return getAttribute("onsubmit");
    }

    /**
     * Returns the value of the attribute "onreset". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute "onreset" or an empty string if that attribute isn't defined
     */
    public final String getOnResetAttribute() {
        return getAttribute("onreset");
    }

    /**
     * Returns the value of the attribute "accept". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute "accept" or an empty string if that attribute isn't defined
     */
    public final String getAcceptAttribute() {
        return getAttribute("accept");
    }

    /**
     * Returns the value of the attribute "accept-charset". Refer to the <a
     * href='http://www.w3.org/TR/html401/interact/forms.html#adef-accept-charset'>
     * HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "accept-charset" or an empty string if that attribute isn't defined
     */
    public final String getAcceptCharsetAttribute() {
        return getAttribute("accept-charset");
    }

    /**
     * Returns the value of the attribute "target". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute "target" or an empty string if that attribute isn't defined
     */
    public final String getTargetAttribute() {
        return getAttribute("target");
    }

    /**
     * Sets the value of the attribute "target". Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @param target the value of the attribute "target"
     */
    public final void setTargetAttribute(final String target) {
        setAttribute("target", target);
    }

    /**
     * Returns the first input in this form with the specified value.
     * @param value the value to search for
     * @param <I> the input type
     * @return the first input in this form with the specified value
     * @throws ElementNotFoundException if this form does not contain any inputs with the specified value
     */
    @SuppressWarnings("unchecked")
    public <I extends HtmlInput> I getInputByValue(final String value) throws ElementNotFoundException {
        final List<HtmlInput> list = getInputsByValue(value);
        if (list.isEmpty()) {
            throw new ElementNotFoundException("input", "value", value);
        }
        return (I) list.get(0);
    }

    /**
     * Returns all the inputs in this form with the specified value.
     * @param value the value to search for
     * @return all the inputs in this form with the specified value
     */
    public List<HtmlInput> getInputsByValue(final String value) {
        final List<HtmlInput> results = getFormElementsByAttribute("input", "value", value);

        for (final HtmlElement element : getLostChildren()) {
            if (element instanceof HtmlInput && value.equals(element.getAttribute("value"))) {
                results.add((HtmlInput) element);
            }
        }

        return results;
    }

    /**
     * Allows the parser to notify the form of a field that doesn't belong to its DOM children
     * due to malformed HTML code
     * @param element the form field
     */
    void addLostChild(final HtmlElement field) {
        lostChildren_.add(field);
        field.setOwningForm(this);
    }

    /**
     * Gets the form elements that may be submitted but that don't belong to the form's children
     * in the DOM due to incorrect HTML code.
     * @return the elements
     */
    public List<HtmlElement> getLostChildren() {
        return lostChildren_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preventDefault() {
        isPreventDefault_ = true;
    }

}
