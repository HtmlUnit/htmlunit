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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.lang.StringUtils;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.SubmittableElement;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import com.gargoylesoftware.htmlunit.javascript.HTMLCollection;

/**
 * A JavaScript object for a Form.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Kent Tong
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 *
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/form.asp">MSDN documentation</a>
 */
public class HTMLFormElement extends HTMLElement {

    private static final long serialVersionUID = -1860993922147246513L;
    private HTMLCollection elements_; // has to be a member to have equality (==) working

    /**
     * Create an instance.  A default constructor is required for all javascript objects.
     */
    public HTMLFormElement() { }

    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public final void jsConstructor() {
    }

    /**
     * {@inheritDoc}
     */
    public void setHtmlElement(final HtmlElement htmlElement) {
        super.setHtmlElement(htmlElement);
        final HtmlForm htmlForm = getHtmlForm();
        htmlForm.setScriptObject(this);
    }

    /**
     * Return the value of the javascript attribute "name".
     * @return The value of this attribute.
     */
    public String jsxGet_name() {
        return getHtmlForm().getNameAttribute();
    }

    /**
     * Set the value of the javascript attribute "name".
     * @param name The new value.
     */
    public void jsxSet_name(final String name) {
        Assert.notNull("name", name);
        getHtmlForm().setNameAttribute(name);
    }

    /**
     * Return the value of the javascript attribute "elements".
     * @return The value of this attribute.
     */
    public HTMLCollection jsxGet_elements() {
        if (elements_ == null) {
            final HtmlForm htmlForm = getHtmlForm();
            elements_ = new HTMLCollection(this);
            try {
                final XPath xpath = new HtmlUnitXPath("//*[(name() = 'input' or name() = 'button'"
                        + " or name() = 'select' or name() = 'textarea')]",
                        HtmlUnitXPath.buildSubtreeNavigator(htmlForm));
                elements_.init(htmlForm, xpath);
            }
            catch (final JaxenException e) {
                throw Context.reportRuntimeError("Failed to initialize collection form.elements: " + e.getMessage());
            }
        }
        return elements_;
    }

    /**
     * Return the value of the javascript attribute "length".
     * Does not count input type=image elements as browsers (IE6, Mozilla 1.7) do
     * (cf <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/length.asp">MSDN doc</a>)
     * @return The value of this attribute.
     */
    public int jsxGet_length() {
        final int all = jsxGet_elements().jsxGet_length();
        final int images = getHtmlForm().getHtmlElementsByAttribute("input", "type", "image").size();
        return all - images;
    }

    /**
     * Return the value of the javascript attribute "action".
     * @return The value of this attribute.
     */
    public String jsxGet_action() {
        return getHtmlForm().getActionAttribute();
    }

    /**
     * Set the value of the javascript attribute "action".
     * @param action The new value.
     */
    public void jsxSet_action(final String action) {
        Assert.notNull("action", action);
        getHtmlForm().setActionAttribute(action);
    }

    /**
     * Return the value of the javascript attribute "method".
     * @return The value of this attribute.
     */
    public String jsxGet_method() {
        return getHtmlForm().getMethodAttribute();
    }

    /**
     * Set the value of the javascript attribute "method".
     * @param method The new value.
     */
    public void jsxSet_method(final String method) {
        Assert.notNull("method", method);
        getHtmlForm().setMethodAttribute(method);
    }

    /**
     * Return the value of the javascript attribute "target".
     * @return The value of this attribute.
     */
    public String jsxGet_target() {
        return getHtmlForm().getTargetAttribute();
    }

    /**
     * Get the onsubmit event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onsubmit() {
        return getEventHandlerProp("onsubmit");
    }

    /**
     * Set the onsubmit event handler for this element.
     * @param onsubmit the new handler
     */
    public void jsxSet_onsubmit(final Object onsubmit) {
        setEventHandlerProp("onsubmit", onsubmit);
    }

    /**
     * Set the value of the javascript attribute "target".
     * @param target The new value.
     */
    public void jsxSet_target(final String target) {
        Assert.notNull("target", target);
        getHtmlForm().setTargetAttribute(target);
    }

    /**
     * Return the value of the javascript attribute "encoding".
     * @return The value of this attribute.
     */
    public String jsxGet_encoding() {
        return getHtmlForm().getEnctypeAttribute();
    }

    /**
     * Set the value of the javascript attribute "encoding".
     * @param encoding The new value.
     */
    public void jsxSet_encoding(final String encoding) {
        Assert.notNull("encoding", encoding);
        getHtmlForm().setEnctypeAttribute(encoding);
    }

    private HtmlForm getHtmlForm() {
        return (HtmlForm) getHtmlElementOrDie();
    }

    /**
     * Submit the form.
     *
     * @throws IOException if an io error occurs
     */
    public void jsxFunction_submit() throws IOException {
        submit(getHtmlForm(), null);
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
        return submit(getHtmlForm(), submitElement);
    }
    
    private Page submit(final HtmlForm form, final SubmittableElement submitElement) throws IOException {
        final HtmlPage htmlPage = form.getPage();
        if (htmlPage.getWebClient().isJavaScriptEnabled()) {
            if (submitElement != null) {
                final ScriptResult scriptResult = form.fireEvent(Event.TYPE_SUBMIT);
                if (scriptResult != null && Boolean.FALSE.equals(scriptResult.getJavaScriptResult())) {
                    return scriptResult.getNewPage();
                }
            }

            final String action = form.getActionAttribute();
            if (TextUtil.startsWithIgnoreCase(action, "javascript:")) {
                return htmlPage.executeJavaScriptIfPossible(action, "Form action", null).getNewPage();
            }
        }
        else {
            if (TextUtil.startsWithIgnoreCase(form.getActionAttribute(), "javascript:")) {
                // The action is javascript but javascript isn't enabled.  Return
                // the current page.
                return htmlPage;
            }
        }

        final List parameters = form.getParameterListForSubmit(submitElement);
        final SubmitMethod method = SubmitMethod.getInstance(form.getAttributeValue("method"));

        String actionUrl = form.getActionAttribute();
        if (SubmitMethod.GET.equals(method)) {
            final String anchor = StringUtils.substringAfter(actionUrl, "#");
            actionUrl = StringUtils.substringBefore(actionUrl, "#");

            final NameValuePair[] pairs = new NameValuePair[parameters.size()];
            parameters.toArray(pairs);
            final String queryFromFields = EncodingUtil.formUrlEncode(pairs, form.getPage().getPageEncoding());
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
        settings.setEncodingType(FormEncodingType.getInstance(form.getEnctypeAttribute()));
        settings.setCharset(getSubmitCharset(form));
        settings.addAdditionalHeader("Referer", htmlPage.getWebResponse().getUrl().toExternalForm());

        final WebWindow webWindow = htmlPage.getEnclosingWindow();
        return htmlPage.getWebClient().getPage(
                webWindow,
                htmlPage.getResolvedTarget(form.getTargetAttribute()),
                settings);
    }

    /**
     * Gets the charset to use for the form submission. This is the first one
     * from the list provided in {@link #getAcceptCharsetAttribute()} if any
     * or the page's charset else
     * @return see above
     */
    private static String getSubmitCharset(final HtmlForm form) {
        if (form.getAcceptCharsetAttribute().length() > 0) {
            return form.getAcceptCharsetAttribute().trim().replaceAll("[ ,].*", "");
        }
        else {
            return form.getPage().getPageEncoding();
        }
    }

    /**
     * Reset this form
     */
    public void jsxFunction_reset() {
        getHtmlForm().reset();
    }

    /**
     * Return the specified property or NOT_FOUND if it could not be found.
     * @param name The name of the property
     * @return The property.
     */
    protected Object getWithPreemption(final String name) {
        // Try to get the element or elements specified from the form element array
        // (except input type="image" that can't be accessed this way)
        final HTMLCollection elements = new HTMLCollection(this);
        final HtmlForm htmlForm = getHtmlForm();
        try {
            final XPath xpath = new HtmlUnitXPath("//*[(@name = '" + name + "' or @id = '" + name + "')"
                    + " and ((name() = 'input' and translate(@type, 'IMAGE', 'image') != 'image') or name() = 'button'"
                    + " or name() = 'select' or name() = 'textarea')]",
                    HtmlUnitXPath.buildSubtreeNavigator(htmlForm));
            elements.init(htmlForm, xpath);
        }
        catch (final JaxenException e) {
            throw Context.reportRuntimeError("Failed to initialize collection: " + e.getMessage());
        }

        int nbElements = elements.jsxGet_length();
        // if no form field is found, IE and Firefox are able to find img by id or name
        if (nbElements == 0) {
            try {
                final XPath xpath = new HtmlUnitXPath("//*[(@name = '" + name + "' or @id = '" + name + "')"
                        + " and name() = 'img']",
                        HtmlUnitXPath.buildSubtreeNavigator(htmlForm));
                elements.init(htmlForm, xpath);
            }
            catch (final JaxenException e) {
                throw Context.reportRuntimeError("Failed to initialize collection: " + e.getMessage());
            }
        }

        Object result = elements;
        nbElements = elements.jsxGet_length();
        if (nbElements == 0) {
            result = NOT_FOUND;
        }
        else if (nbElements == 1) {
            result = elements.get(0, elements);
        }
        return result;
    }

    /**
     * Return the specified indexed property
     * @param index The index of the property
     * @param start The scriptable object that was originally queried for this property
     * @return The property.
     */
    public Object get(final int index, final Scriptable start) {
        return jsxGet_elements().get(index, ((HTMLFormElement) start).jsxGet_elements());
    }
}
