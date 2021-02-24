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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORMFIELD_REACHABLE_BY_NEW_NAMES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORMFIELD_REACHABLE_BY_ORIGINAL_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORM_SUBMISSION_DOWNLOWDS_ALSO_IF_ONLY_HASH_CHANGED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FORM_ACTION_EXPANDURL_NOT_DEFINED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FORM_DISPATCHEVENT_SUBMITS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FORM_REJECT_INVALID_ENCODING;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FORM_SUBMIT_FORCES_DOWNLOAD;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FORM_USABLE_AS_FUNCTION;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FormFieldWithNameHistory;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.SubmittableElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;
import com.gargoylesoftware.htmlunit.util.MimeType;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code HTMLFormElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Kent Tong
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535249.aspx">MSDN documentation</a>
 */
@JsxClass(domClass = HtmlForm.class)
public class HTMLFormElement extends HTMLElement implements Function {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLFormElement() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHtmlElement(final HtmlElement htmlElement) {
        super.setHtmlElement(htmlElement);
        final HtmlForm htmlForm = getHtmlForm();
        htmlForm.setScriptableObject(this);
    }

    /**
     * Returns the value of the property {@code name}.
     * @return the value of this property
     */
    @JsxGetter
    @Override
    public String getName() {
        return getHtmlForm().getNameAttribute();
    }

    /**
     * Sets the value of the property {@code name}.
     * @param name the new value
     */
    @JsxSetter
    @Override
    public void setName(final String name) {
        getHtmlForm().setNameAttribute(name);
    }

    /**
     * Returns the value of the property {@code elements}.
     * @return the value of this property
     */
    @JsxGetter
    public HTMLCollection getElements() {
        final HtmlForm htmlForm = getHtmlForm();

        return new HTMLCollection(htmlForm, false) {
            private boolean filterChildrenOfNestedForms_;

            @Override
            protected List<DomNode> computeElements() {
                final List<DomNode> response = super.computeElements();
                // it would be more performant to avoid iterating through
                // nested forms but as it is a corner case of ill formed HTML
                // the needed refactoring would take too much time
                // => filter here and not in isMatching as it won't be needed in most
                // of the cases
                if (filterChildrenOfNestedForms_) {
                    for (final Iterator<DomNode> iter = response.iterator(); iter.hasNext();) {
                        final HtmlElement field = (HtmlElement) iter.next();
                        if (field.getEnclosingForm() != htmlForm) {
                            iter.remove();
                        }
                    }
                }
                response.addAll(htmlForm.getLostChildren());
                return response;
            }

            @Override
            protected Object getWithPreemption(final String name) {
                return HTMLFormElement.this.getWithPreemption(name);
            }

            @Override
            public EffectOnCache getEffectOnCache(final HtmlAttributeChangeEvent event) {
                return EffectOnCache.NONE;
            }

            @Override
            protected boolean isMatching(final DomNode node) {
                if (node instanceof HtmlForm) {
                    filterChildrenOfNestedForms_ = true;
                    return false;
                }

                return node instanceof HtmlInput || node instanceof HtmlButton
                        || node instanceof HtmlTextArea || node instanceof HtmlSelect;
            }
        };
    }

    /**
     * Returns the value of the property {@code length}.
     * Does not count input {@code type=image} elements
     * (<a href="http://msdn.microsoft.com/en-us/library/ms534101.aspx">MSDN doc</a>)
     * @return the value of this property
     */
    @JsxGetter
    public int getLength() {
        final int all = getElements().getLength();
        final int images = getHtmlForm().getElementsByAttribute(HtmlInput.TAG_NAME, "type", "image").size();
        return all - images;
    }

    /**
     * Returns the value of the property {@code action}.
     * @return the value of this property
     */
    @JsxGetter
    public String getAction() {
        final String action = getHtmlForm().getActionAttribute();

        if (action == DomElement.ATTRIBUTE_NOT_DEFINED
                && !getBrowserVersion().hasFeature(JS_FORM_ACTION_EXPANDURL_NOT_DEFINED)) {
            return action;
        }

        try {
            return ((HtmlPage) getHtmlForm().getPage()).getFullyQualifiedUrl(action).toExternalForm();
        }
        catch (final MalformedURLException e) {
            // nothing, return action attribute
        }
        return action;
    }

    /**
     * Sets the value of the property {@code action}.
     * @param action the new value
     */
    @JsxSetter
    public void setAction(final String action) {
        WebAssert.notNull("action", action);
        getHtmlForm().setActionAttribute(action);
    }

    /**
     * Returns the value of the property {@code method}.
     * @return the value of this property
     */
    @JsxGetter
    public String getMethod() {
        return getHtmlForm().getMethodAttribute();
    }

    /**
     * Sets the value of the property {@code method}.
     * @param method the new property
     */
    @JsxSetter
    public void setMethod(final String method) {
        WebAssert.notNull("method", method);
        getHtmlForm().setMethodAttribute(method);
    }

    /**
     * Returns the value of the property {@code target}.
     * @return the value of this property
     */
    @JsxGetter
    public String getTarget() {
        return getHtmlForm().getTargetAttribute();
    }

    /**
     * Sets the value of the property {@code target}.
     * @param target the new value
     */
    @JsxSetter
    public void setTarget(final String target) {
        WebAssert.notNull("target", target);
        getHtmlForm().setTargetAttribute(target);
    }

    /**
     * Returns the value of the property {@code enctype}.
     * @return the value of this property
     */
    @JsxGetter
    public String getEnctype() {
        final String encoding = getHtmlForm().getEnctypeAttribute();
        if (!FormEncodingType.URL_ENCODED.getName().equals(encoding)
                && !FormEncodingType.MULTIPART.getName().equals(encoding)
                && !MimeType.TEXT_PLAIN.equals(encoding)) {
            return FormEncodingType.URL_ENCODED.getName();
        }
        return encoding;
    }

    /**
     * Sets the value of the property {@code enctype}.
     * @param enctype the new value
     */
    @JsxSetter
    public void setEnctype(final String enctype) {
        WebAssert.notNull("encoding", enctype);
        if (getBrowserVersion().hasFeature(JS_FORM_REJECT_INVALID_ENCODING)
                && !FormEncodingType.URL_ENCODED.getName().equals(enctype)
                && !FormEncodingType.MULTIPART.getName().equals(enctype)
                && !FormEncodingType.TEXT_PLAIN.getName().equals(enctype)) {
            throw Context.reportRuntimeError("Cannot set the encoding property to invalid value: '" + enctype + "'");
        }
        getHtmlForm().setEnctypeAttribute(enctype);
    }

    /**
     * Returns the value of the property {@code encoding}.
     * @return the value of this property
     */
    @JsxGetter
    public String getEncoding() {
        return getEnctype();
    }

    /**
     * Sets the value of the property {@code encoding}.
     * @param encoding the new value
     */
    @JsxSetter
    public void setEncoding(final String encoding) {
        setEnctype(encoding);
    }

    /**
     * @return the associated HtmlForm
     */
    public HtmlForm getHtmlForm() {
        return (HtmlForm) getDomNodeOrDie();
    }

    /**
     * Submits the form (at the end of the current script execution).
     */
    @JsxFunction
    public void submit() {
        final HtmlPage page = (HtmlPage) getDomNodeOrDie().getPage();
        final WebClient webClient = page.getWebClient();

        final String action = getHtmlForm().getActionAttribute().trim();
        if (StringUtils.startsWithIgnoreCase(action, JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
            final String js = action.substring(JavaScriptURLConnection.JAVASCRIPT_PREFIX.length());
            webClient.getJavaScriptEngine().execute(page, js, "Form action", 0);
        }
        else {
            // download should be done ASAP, response will be loaded into a window later
            final WebRequest request = getHtmlForm().getWebRequest(null);
            final String target = page.getResolvedTarget(getTarget());
            final boolean forceDownload = webClient.getBrowserVersion().hasFeature(JS_FORM_SUBMIT_FORCES_DOWNLOAD);
            final boolean checkHash =
                    !webClient.getBrowserVersion().hasFeature(FORM_SUBMISSION_DOWNLOWDS_ALSO_IF_ONLY_HASH_CHANGED);
            webClient.download(page.getEnclosingWindow(),
                        target, request, checkHash, forceDownload, "JS form.submit()");
        }
    }

    /**
     * Submits the form by submitted using a specific submit button.
     * @param submitter The submit button whose attributes describe the method
     * by which the form is to be submitted. This may be either
     * an &lt;input&gt; or &lt;button&gt; element whose type attribute is submit.
     * If you omit the submitter parameter, the form element itself is used as the submitter.
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public void requestSubmit(final Object submitter) {
        SubmittableElement submittable = null;
        if (Undefined.isUndefined(submitter)) {
            submit();
            return;
        }

        if (submitter instanceof HTMLElement) {
            final HTMLElement subHtmlElement = (HTMLElement) submitter;
            if (subHtmlElement instanceof HTMLButtonElement) {
                if ("submit".equals(((HTMLButtonElement) subHtmlElement).getType())) {
                    submittable = (SubmittableElement) subHtmlElement.getDomNodeOrDie();
                }
            }
            else if (subHtmlElement instanceof HTMLInputElement) {
                if ("submit".equals(((HTMLInputElement) subHtmlElement).getType())) {
                    submittable = (SubmittableElement) subHtmlElement.getDomNodeOrDie();
                }
            }

            if (submittable != null && subHtmlElement.getForm() != this) {
                throw ScriptRuntime.typeError(
                        "Failed to execute 'requestSubmit' on 'HTMLFormElement': "
                        + "The specified element is not owned by this form element.");
            }
        }

        if (submittable == null) {
            throw ScriptRuntime.typeError(
                    "Failed to execute 'requestSubmit' on 'HTMLFormElement': "
                    + "The specified element is not a submit button.");
        }

        this.getHtmlForm().submit(submittable);
    }

    /**
     * Retrieves a form object or an object from an elements collection.
     * @param index Integer or String that specifies the object or collection to retrieve.
     *              If this parameter is an integer, it is the zero-based index of the object.
     *              If this parameter is a string, all objects with matching name or id properties are retrieved,
     *              and a collection is returned if more than one match is made
     * @param subIndex Optional. Integer that specifies the zero-based index of the object to retrieve
     *              when a collection is returned
     * @return an object or a collection of objects if successful, or null otherwise
     */
    @JsxFunction(IE)
    public Object item(final Object index, final Object subIndex) {
        if (index instanceof Number) {
            return getElements().item(index);
        }

        final String name = Context.toString(index);
        final Object response = getWithPreemption(name);
        if (subIndex instanceof Number && response instanceof HTMLCollection) {
            return ((HTMLCollection) response).item(subIndex);
        }

        return response;
    }

    /**
     * Resets this form.
     */
    @JsxFunction
    public void reset() {
        getHtmlForm().reset();
    }

    /**
     * Overridden to allow the retrieval of certain form elements by ID or name.
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected Object getWithPreemption(final String name) {
        if (getDomNodeOrNull() == null) {
            return NOT_FOUND;
        }
        final List<HtmlElement> elements = findElements(name);

        if (elements.isEmpty()) {
            return NOT_FOUND;
        }
        if (elements.size() == 1) {
            return getScriptableFor(elements.get(0));
        }
        final List<DomNode> nodes = new ArrayList<>(elements);
        return new HTMLCollection(getHtmlForm(), nodes) {
            @Override
            protected List<DomNode> computeElements() {
                return new ArrayList<>(findElements(name));
            }
        };
    }

    List<HtmlElement> findElements(final String name) {
        final List<HtmlElement> elements = new ArrayList<>();
        addElements(name, getHtmlForm().getHtmlElementDescendants(), elements);
        addElements(name, getHtmlForm().getLostChildren(), elements);

        // If no form fields are found, browsers are able to find img elements by ID or name.
        if (elements.isEmpty()) {
            for (final DomNode node : getHtmlForm().getHtmlElementDescendants()) {
                if (node instanceof HtmlImage) {
                    final HtmlImage img = (HtmlImage) node;
                    if (name.equals(img.getId()) || name.equals(img.getNameAttribute())) {
                        elements.add(img);
                    }
                }
            }
        }

        return elements;
    }

    private void addElements(final String name, final Iterable<HtmlElement> nodes,
        final List<HtmlElement> addTo) {
        for (final HtmlElement node : nodes) {
            if (isAccessibleByIdOrName(node, name)) {
                addTo.add(node);
            }
        }
    }

    /**
     * Indicates if the element can be reached by id or name in expressions like "myForm.myField".
     * @param element the element to test
     * @param name the name used to address the element
     * @return {@code true} if this element matches the conditions
     */
    private boolean isAccessibleByIdOrName(final HtmlElement element, final String name) {
        if (element instanceof FormFieldWithNameHistory && !(element instanceof HtmlImageInput)) {
            if (element.getEnclosingForm() != getHtmlForm()) {
                return false; // nested forms
            }
            if (name.equals(element.getId())) {
                return true;
            }
            final FormFieldWithNameHistory elementWithNames = (FormFieldWithNameHistory) element;
            if (getBrowserVersion().hasFeature(FORMFIELD_REACHABLE_BY_ORIGINAL_NAME)) {
                if (name.equals(elementWithNames.getOriginalName())) {
                    return true;
                }
            }
            else if (name.equals(element.getAttributeDirect("name"))) {
                return true;
            }

            if (getBrowserVersion().hasFeature(FORMFIELD_REACHABLE_BY_NEW_NAMES)
                    && elementWithNames.getNewNames().contains(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the specified indexed property.
     * @param index the index of the property
     * @param start the scriptable object that was originally queried for this property
     * @return the property
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (getDomNodeOrNull() == null) {
            return NOT_FOUND; // typically for the prototype
        }
        return getElements().get(index, ((HTMLFormElement) start).getElements());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (!getBrowserVersion().hasFeature(JS_FORM_USABLE_AS_FUNCTION)) {
            throw Context.reportRuntimeError("Not a function.");
        }
        if (args.length > 0) {
            final Object arg = args[0];
            if (arg instanceof String) {
                return ScriptableObject.getProperty(this, (String) arg);
            }
            else if (arg instanceof Number) {
                return ScriptableObject.getProperty(this, ((Number) arg).intValue());
            }
        }
        return Undefined.instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        if (!getBrowserVersion().hasFeature(JS_FORM_USABLE_AS_FUNCTION)) {
            throw Context.reportRuntimeError("Not a function.");
        }
        return null;
    }

    @Override
    public boolean dispatchEvent(final Event event) {
        final boolean result = super.dispatchEvent(event);

        if (Event.TYPE_SUBMIT.equals(event.getType())
                && getBrowserVersion().hasFeature(JS_FORM_DISPATCHEVENT_SUBMITS)) {
            submit();
        }
        return result;
    }

    /**
     * Checks whether the element has any constraints and whether it satisfies them.
     * @return if the element is valid
     */
    @JsxFunction
    public boolean checkValidity() {
        return getDomNodeOrDie().isValid();
    }

}
