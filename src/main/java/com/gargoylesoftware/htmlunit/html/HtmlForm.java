/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORM_PARAMETRS_NOT_SUPPORTED_FOR_IMAGE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORM_SUBMISSION_DOWNLOWDS_ALSO_IF_ONLY_HASH_CHANGED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORM_SUBMISSION_FORM_ATTRIBUTE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORM_SUBMISSION_HEADER_CACHE_CONTROL_MAX_AGE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORM_SUBMISSION_HEADER_CACHE_CONTROL_NO_CACHE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORM_SUBMISSION_HEADER_ORIGIN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORM_SUBMISSION_URL_WITHOUT_HASH;
import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hc.core5.net.URLEncodedUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Wrapper for the HTML element "form".
 *
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
 * @author Frank Danek
 * @author Anton Demydenko
 */
public class HtmlForm extends HtmlElement {
    private static final Log LOG = LogFactory.getLog(HtmlForm.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "form";

    private static final Collection<String> SUBMITTABLE_ELEMENT_NAMES = Arrays.asList(HtmlInput.TAG_NAME,
        HtmlButton.TAG_NAME, HtmlSelect.TAG_NAME, HtmlTextArea.TAG_NAME, HtmlIsIndex.TAG_NAME);

    private static final Pattern SUBMIT_CHARSET_PATTERN = Pattern.compile("[ ,].*");

    private final List<HtmlElement> lostChildren_ = new ArrayList<>();

    private boolean isPreventDefault_;

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param htmlPage the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlForm(final String qualifiedName, final SgmlPage htmlPage,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, htmlPage, attributes);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * <p>Submits this form to the server. If <tt>submitElement</tt> is {@code null}, then
     * the submission is treated as if it was triggered by JavaScript, and the <tt>onsubmit</tt>
     * handler will not be executed.</p>
     *
     * <p><b>IMPORTANT:</b> Using this method directly is not the preferred way of submitting forms.
     * Most consumers should emulate the user's actions instead, probably by using something like
     * {@link HtmlElement#click()} or {@link HtmlElement#dblClick()}.</p>
     *
     * @param submitElement the element that caused the submit to occur
     */
    public void submit(final SubmittableElement submitElement) {
        final HtmlPage htmlPage = (HtmlPage) getPage();
        final WebClient webClient = htmlPage.getWebClient();

        if (webClient.isJavaScriptEnabled()) {
            if (submitElement != null) {
                isPreventDefault_ = false;

                boolean validate = true;
                if (submitElement instanceof HtmlSubmitInput
                        && ((HtmlSubmitInput) submitElement).getAttributeDirect("formnovalidate")
                                != ATTRIBUTE_NOT_DEFINED) {
                    validate = false;
                }
                else if (submitElement instanceof HtmlButton) {
                    final HtmlButton htmlButton = (HtmlButton) submitElement;
                    if ("submit".equalsIgnoreCase(htmlButton.getType())
                            && htmlButton.getAttributeDirect("formnovalidate") != ATTRIBUTE_NOT_DEFINED) {
                        validate = false;
                    }
                }

                if (validate
                        && getAttributeDirect("novalidate") != ATTRIBUTE_NOT_DEFINED) {
                    validate = false;
                }

                if (validate && !areChildrenValid()) {
                    return;
                }

                final ScriptResult scriptResult = fireEvent(Event.TYPE_SUBMIT);
                if (isPreventDefault_) {
                    // null means 'nothing executed'
                    if (scriptResult == null) {
                        return;
                    }
                    return;
                }
            }

            final String action = getActionAttribute().trim();
            if (StringUtils.startsWithIgnoreCase(action, JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
                htmlPage.executeJavaScript(action, "Form action", getStartLineNumber());
                return;
            }
        }
        else {
            if (StringUtils.startsWithIgnoreCase(getActionAttribute(), JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
                // The action is JavaScript but JavaScript isn't enabled.
                return;
            }
        }

        // html5 attribute's support
        if (submitElement != null) {
            updateHtml5Attributes(submitElement);
        }

        final WebRequest request = getWebRequest(submitElement);
        final String target = htmlPage.getResolvedTarget(getTargetAttribute());

        final WebWindow webWindow = htmlPage.getEnclosingWindow();
        /** Calling form.submit() twice forces double download. */
        final boolean checkHash =
                !webClient.getBrowserVersion().hasFeature(FORM_SUBMISSION_DOWNLOWDS_ALSO_IF_ONLY_HASH_CHANGED);
        webClient.download(webWindow, target, request, checkHash, false, false, "JS form.submit()");
    }

    /**
     * Check if element which cause submit contains new html5 attributes
     * (formaction, formmethod, formtarget, formenctype)
     * and override existing values
     * @param submitElement
     */
    private void updateHtml5Attributes(final SubmittableElement submitElement) {
        if (submitElement instanceof HtmlElement) {
            final HtmlElement element = (HtmlElement) submitElement;

            final String type = element.getAttributeDirect("type");
            boolean typeImage = false;
            final boolean typeSubmit = "submit".equalsIgnoreCase(type);
            final boolean isInput = HtmlInput.TAG_NAME.equals(element.getTagName());
            if (isInput) {
                typeImage = "image".equalsIgnoreCase(type);
            }

            // IE does not support formxxx attributes for input with 'image' types
            final BrowserVersion browser = getPage().getWebClient().getBrowserVersion();
            if (browser.hasFeature(FORM_PARAMETRS_NOT_SUPPORTED_FOR_IMAGE) && typeImage) {
                return;
            }

            // could be excessive validation but support of html5 fromxxx
            // attributes available for:
            // - input with 'submit' and 'image' types
            // - button with 'submit'
            if (isInput && !typeSubmit && !typeImage) {
                return;
            }
            else if (HtmlButton.TAG_NAME.equals(element.getTagName())
                && !"submit".equalsIgnoreCase(type)) {
                return;
            }

            final String formaction = element.getAttributeDirect("formaction");
            if (DomElement.ATTRIBUTE_NOT_DEFINED != formaction) {
                setActionAttribute(formaction);
            }
            final String formmethod = element.getAttributeDirect("formmethod");
            if (DomElement.ATTRIBUTE_NOT_DEFINED != formmethod) {
                setMethodAttribute(formmethod);
            }
            final String formtarget = element.getAttributeDirect("formtarget");
            if (DomElement.ATTRIBUTE_NOT_DEFINED != formtarget) {
                setTargetAttribute(formtarget);
            }
            final String formenctype = element.getAttributeDirect("formenctype");
            if (DomElement.ATTRIBUTE_NOT_DEFINED != formenctype) {
                setEnctypeAttribute(formenctype);
            }
        }
    }

    private boolean areChildrenValid() {
        boolean valid = true;
        for (final HtmlElement element : getFormHtmlElementDescendants()) {
            if (element instanceof HtmlInput && !((HtmlInput) element).isValid()) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Form validation failed; element '" + element + "' was not valid. Submit cancelled.");
                }
                valid = false;
                break;
            }
        }
        return valid;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
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
        String anchor = null;
        String queryFormFields = "";
        Charset enc = getSubmitCharset();
        if (UTF_16 == enc) {
            enc = UTF_8;
        }

        if (HttpMethod.GET == method) {
            if (actionUrl.contains("#")) {
                anchor = StringUtils.substringAfter(actionUrl, "#");
            }
            queryFormFields = URLEncodedUtils.format(NameValuePair.toHttpClient(parameters), enc);

            // action may already contain some query parameters: they have to be removed
            actionUrl = StringUtils.substringBefore(actionUrl, "#");
            actionUrl = StringUtils.substringBefore(actionUrl, "?");
            parameters.clear(); // parameters have been added to query
        }

        URL url;
        try {
            if (actionUrl.isEmpty()) {
                url = WebClient.expandUrl(htmlPage.getUrl(), actionUrl);
            }
            else {
                url = htmlPage.getFullyQualifiedUrl(actionUrl);
            }

            if (!queryFormFields.isEmpty()) {
                url = UrlUtils.getUrlWithNewQuery(url, queryFormFields);
            }

            if (HttpMethod.GET == method && browser.hasFeature(FORM_SUBMISSION_URL_WITHOUT_HASH)
                    && UrlUtils.URL_ABOUT_BLANK != url) {
                url = UrlUtils.getUrlWithNewRef(url, null);
            }
            else if (HttpMethod.POST == method
                    && browser.hasFeature(FORM_SUBMISSION_URL_WITHOUT_HASH)
                    && UrlUtils.URL_ABOUT_BLANK != url
                    && StringUtils.isEmpty(actionUrl)) {
                url = UrlUtils.getUrlWithNewRef(url, null);
            }
            else if (anchor != null
                    && UrlUtils.URL_ABOUT_BLANK != url) {
                url = UrlUtils.getUrlWithNewRef(url, anchor);
            }
        }
        catch (final MalformedURLException e) {
            throw new IllegalArgumentException("Not a valid url: " + actionUrl, e);
        }

        final WebRequest request = new WebRequest(url, browser.getHtmlAcceptHeader(),
                                                        browser.getAcceptEncodingHeader());
        request.setHttpMethod(method);
        request.setRequestParameters(parameters);
        if (HttpMethod.POST == method) {
            request.setEncodingType(FormEncodingType.getInstance(getEnctypeAttribute()));
        }
        request.setCharset(enc);
        request.setRefererlHeader(htmlPage.getUrl());

        if (HttpMethod.POST == method
                && browser.hasFeature(FORM_SUBMISSION_HEADER_ORIGIN)) {
            try {
                request.setAdditionalHeader(HttpHeader.ORIGIN,
                        UrlUtils.getUrlWithProtocolAndAuthority(htmlPage.getUrl()).toExternalForm());
            }
            catch (final MalformedURLException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.info("Invalid origin url '" + htmlPage.getUrl() + "'");
                }
            }
        }
        if (HttpMethod.POST == method) {
            if (browser.hasFeature(FORM_SUBMISSION_HEADER_CACHE_CONTROL_MAX_AGE)) {
                request.setAdditionalHeader(HttpHeader.CACHE_CONTROL, "max-age=0");
            }

            if (browser.hasFeature(FORM_SUBMISSION_HEADER_CACHE_CONTROL_NO_CACHE)) {
                request.setAdditionalHeader(HttpHeader.CACHE_CONTROL, "no-cache");
            }
        }

        return request;
    }

    /**
     * Returns the charset to use for the form submission. This is the first one
     * from the list provided in {@link #getAcceptCharsetAttribute()} if any
     * or the page's charset else
     * @return the charset to use for the form submission
     */
    private Charset getSubmitCharset() {
        String charset = getAcceptCharsetAttribute();
        if (!charset.isEmpty()) {
            charset = charset.trim();
            return EncodingSniffer.toCharset(
                    SUBMIT_CHARSET_PATTERN.matcher(charset).replaceAll("").toUpperCase(Locale.ROOT));
        }
        return getPage().getCharset();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns a list of {@link NameValuePair}s that represent the data that will be
     * sent to the server when this form is submitted. This is primarily intended to aid
     * debugging.
     *
     * @param submitElement the element used to submit the form, or {@code null} if the
     *        form was submitted by JavaScript
     * @return the list of {@link NameValuePair}s that represent that data that will be sent
     *         to the server when this form is submitted
     */
    public List<NameValuePair> getParameterListForSubmit(final SubmittableElement submitElement) {
        final Collection<SubmittableElement> submittableElements = getSubmittableElements(submitElement);

        final List<NameValuePair> parameterList = new ArrayList<>(submittableElements.size());
        for (final SubmittableElement element : submittableElements) {
            for (final NameValuePair pair : element.getSubmitNameValuePairs()) {
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
            return htmlPage.getWebClient().getCurrentWindow().getEnclosedPage();
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
     * @param submitElement the element used to submit the form, or {@code null} if the
     *        form is submitted by JavaScript
     * @return a collection of elements that represent all the "submittable" elements in this form
     */
    Collection<SubmittableElement> getSubmittableElements(final SubmittableElement submitElement) {
        final List<SubmittableElement> submittableElements = new ArrayList<>();

        for (final HtmlElement element : getFormHtmlElementDescendants()) {
            if (isSubmittable(element, submitElement)) {
                submittableElements.add((SubmittableElement) element);
            }
        }

        if (getPage().getWebClient().getBrowserVersion().hasFeature(FORM_SUBMISSION_FORM_ATTRIBUTE)) {
            final String formId = getId();
            if (formId != ATTRIBUTE_NOT_DEFINED) {
                for (final DomNode domNode : ((HtmlPage) getPage()).getBody().getDescendants()) {
                    if (domNode instanceof HtmlElement) {
                        final HtmlElement element = (HtmlElement) domNode;
                        final String formIdRef = element.getAttribute("form");
                        if (formId.equals(formIdRef) && isSubmittable(element, submitElement)) {
                            final SubmittableElement submittable = (SubmittableElement) element;
                            if (!submittableElements.contains(submittable)) {
                                submittableElements.add(submittable);
                            }
                        }
                    }
                }
            }
        }

        for (final HtmlElement element : lostChildren_) {
            if (isSubmittable(element, submitElement)) {
                submittableElements.add((SubmittableElement) element);
            }
        }

        return submittableElements;
    }

    private static boolean isValidForSubmission(final HtmlElement element, final SubmittableElement submitElement) {
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

        if (!HtmlIsIndex.TAG_NAME.equals(tagName) && !element.hasAttribute("name")) {
            return false;
        }

        if (!HtmlIsIndex.TAG_NAME.equals(tagName) && "".equals(element.getAttributeDirect("name"))) {
            return false;
        }

        if (element instanceof HtmlInput) {
            final String type = element.getAttributeDirect("type").toLowerCase(Locale.ROOT);
            if ("radio".equals(type) || "checkbox".equals(type)) {
                return ((HtmlInput) element).isChecked();
            }
        }
        if (HtmlSelect.TAG_NAME.equals(tagName)) {
            return ((HtmlSelect) element).isValidForSubmission();
        }
        return true;
    }

    /**
     * Returns {@code true} if the specified element gets submitted when this form is submitted,
     * assuming that the form is submitted using the specified submit element.
     *
     * @param element the element to check
     * @param submitElement the element used to submit the form, or {@code null} if the form is
     *        submitted by JavaScript
     * @return {@code true} if the specified element gets submitted when this form is submitted
     */
    private static boolean isSubmittable(final HtmlElement element, final SubmittableElement submitElement) {
        if (!isValidForSubmission(element, submitElement)) {
            return false;
        }

        // The one submit button that was clicked can be submitted but no other ones
        if (element == submitElement) {
            return true;
        }
        if (element instanceof HtmlInput) {
            final HtmlInput input = (HtmlInput) element;
            final String type = input.getTypeAttribute().toLowerCase(Locale.ROOT);
            if ("submit".equals(type) || "image".equals(type) || "reset".equals(type) || "button".equals(type)) {
                return false;
            }
        }

        return !HtmlButton.TAG_NAME.equals(element.getTagName());
    }

    /**
     * Returns all input elements which are members of this form and have the specified name.
     *
     * @param name the input name to search for
     * @return all input elements which are members of this form and have the specified name
     */
    public List<HtmlInput> getInputsByName(final String name) {
        final List<HtmlInput> list = getFormElementsByAttribute(HtmlInput.TAG_NAME, "name", name);

        // collect inputs from lost children
        for (final HtmlElement elt : getLostChildren()) {
            if (elt instanceof HtmlInput && name.equals(elt.getAttributeDirect("name"))) {
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

        final List<E> list = new ArrayList<>();
        final String lowerCaseTagName = elementName.toLowerCase(Locale.ROOT);

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
            @Override
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
            throw new ElementNotFoundException(HtmlInput.TAG_NAME, "name", name);
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
        final List<HtmlSelect> list = getFormElementsByAttribute(HtmlSelect.TAG_NAME, "name", name);

        // collect selects from lost children
        for (final HtmlElement elt : getLostChildren()) {
            if (elt instanceof HtmlSelect && name.equals(elt.getAttributeDirect("name"))) {
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
            throw new ElementNotFoundException(HtmlSelect.TAG_NAME, "name", name);
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
        final List<HtmlButton> list = getFormElementsByAttribute(HtmlButton.TAG_NAME, "name", name);

        // collect buttons from lost children
        for (final HtmlElement elt : getLostChildren()) {
            if (elt instanceof HtmlButton && name.equals(elt.getAttributeDirect("name"))) {
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
            throw new ElementNotFoundException(HtmlButton.TAG_NAME, "name", name);
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
        final List<HtmlTextArea> list = getFormElementsByAttribute(HtmlTextArea.TAG_NAME, "name", name);

        // collect buttons from lost children
        for (final HtmlElement elt : getLostChildren()) {
            if (elt instanceof HtmlTextArea && name.equals(elt.getAttributeDirect("name"))) {
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
            throw new ElementNotFoundException(HtmlTextArea.TAG_NAME, "name", name);
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

        final List<HtmlRadioButtonInput> results = new ArrayList<>();

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
        if (radioButtonInput.getEnclosingForm() == null && !lostChildren_.contains(radioButtonInput)) {
            throw new IllegalArgumentException("HtmlRadioButtonInput is not child of this HtmlForm");
        }
        final List<HtmlRadioButtonInput> radios = getRadioButtonsByName(radioButtonInput.getNameAttribute());

        for (final HtmlRadioButtonInput input : radios) {
            input.setCheckedInternal(input == radioButtonInput);
        }
    }

    /**
     * Returns the first checked radio button with the specified name. If none of
     * the radio buttons by that name are checked, this method returns {@code null}.
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
     * Returns the value of the attribute {@code action}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute {@code action} or an empty string if that attribute isn't defined
     */
    public final String getActionAttribute() {
        return getAttributeDirect("action");
    }

    /**
     * Sets the value of the attribute {@code action}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @param action the value of the attribute {@code action}
     */
    public final void setActionAttribute(final String action) {
        setAttribute("action", action);
    }

    /**
     * Returns the value of the attribute {@code method}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute {@code method} or an empty string if that attribute isn't defined
     */
    public final String getMethodAttribute() {
        return getAttributeDirect("method");
    }

    /**
     * Sets the value of the attribute {@code method}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @param method the value of the attribute {@code method}
     */
    public final void setMethodAttribute(final String method) {
        setAttribute("method", method);
    }

    /**
     * Returns the value of the attribute {@code name}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute {@code name} or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttributeDirect("name");
    }

    /**
     * Sets the value of the attribute {@code name}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @param name the value of the attribute {@code name}
     */
    public final void setNameAttribute(final String name) {
        setAttribute("name", name);
    }

    /**
     * Returns the value of the attribute {@code enctype}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute. "Enctype" is the encoding type
     * used when submitting a form back to the server.
     *
     * @return the value of the attribute {@code enctype} or an empty string if that attribute isn't defined
     */
    public final String getEnctypeAttribute() {
        return getAttributeDirect("enctype");
    }

    /**
     * Sets the value of the attribute {@code enctype}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute. "Enctype" is the encoding type
     * used when submitting a form back to the server.
     *
     * @param encoding the value of the attribute {@code enctype}
     */
    public final void setEnctypeAttribute(final String encoding) {
        setAttribute("enctype", encoding);
    }

    /**
     * Returns the value of the attribute {@code onsubmit}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute {@code onsubmit} or an empty string if that attribute isn't defined
     */
    public final String getOnSubmitAttribute() {
        return getAttributeDirect("onsubmit");
    }

    /**
     * Returns the value of the attribute {@code onreset}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute {@code onreset} or an empty string if that attribute isn't defined
     */
    public final String getOnResetAttribute() {
        return getAttributeDirect("onreset");
    }

    /**
     * Returns the value of the attribute {@code accept}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute {@code accept} or an empty string if that attribute isn't defined
     */
    public final String getAcceptAttribute() {
        return getAttribute(HttpHeader.ACCEPT_LC);
    }

    /**
     * Returns the value of the attribute {@code accept-charset}. Refer to the <a
     * href='http://www.w3.org/TR/html401/interact/forms.html#adef-accept-charset'>
     * HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code accept-charset} or an empty string if that attribute isn't defined
     */
    public final String getAcceptCharsetAttribute() {
        return getAttribute("accept-charset");
    }

    /**
     * Returns the value of the attribute {@code target}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute {@code target} or an empty string if that attribute isn't defined
     */
    public final String getTargetAttribute() {
        return getAttributeDirect("target");
    }

    /**
     * Sets the value of the attribute {@code target}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @param target the value of the attribute {@code target}
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
            throw new ElementNotFoundException(HtmlInput.TAG_NAME, "value", value);
        }
        return (I) list.get(0);
    }

    /**
     * Returns all the inputs in this form with the specified value.
     * @param value the value to search for
     * @return all the inputs in this form with the specified value
     */
    public List<HtmlInput> getInputsByValue(final String value) {
        final List<HtmlInput> results = getFormElementsByAttribute(HtmlInput.TAG_NAME, "value", value);

        for (final HtmlElement element : getLostChildren()) {
            if (element instanceof HtmlInput && value.equals(element.getAttributeDirect("value"))) {
                results.add((HtmlInput) element);
            }
        }

        return results;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Allows the parser to notify the form of a field that doesn't belong to its DOM children
     * due to malformed HTML code
     * @param field the form field
     */
    public void addLostChild(final HtmlElement field) {
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

    /**
     * Browsers have problems with self closing form tags.
     */
    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }
}
