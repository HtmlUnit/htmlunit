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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORMFIELD_REACHABLE_BY_NEW_NAMES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_80;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_81;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FORM_ACTION_EXPANDURL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FORM_REJECT_INVALID_ENCODING;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
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
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;

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
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535249.aspx">MSDN documentation</a>
 */
@JsxClass(domClass = HtmlForm.class)
public class HTMLFormElement extends HTMLElement implements Function {

    private HTMLCollection elements_; // has to be a member to have equality (==) working

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHtmlElement(final HtmlElement htmlElement) {
        super.setHtmlElement(htmlElement);
        final HtmlForm htmlForm = getHtmlForm();
        htmlForm.setScriptObject(this);
    }

    /**
     * Returns the value of the JavaScript attribute "name".
     * @return the value of this attribute
     */
    @JsxGetter
    public String getName() {
        return getHtmlForm().getNameAttribute();
    }

    /**
     * Sets the value of the JavaScript attribute "name".
     * @param name the new value
     */
    @JsxSetter
    public void setName(final String name) {
        WebAssert.notNull("name", name);
        getHtmlForm().setNameAttribute(name);
    }

    /**
     * Returns the value of the JavaScript attribute "elements".
     * @return the value of this attribute
     */
    @JsxGetter
    public HTMLCollection getElements() {
        if (elements_ == null) {
            final HtmlForm htmlForm = getHtmlForm();

            elements_ = new HTMLCollection(htmlForm, false, "HTMLFormElement.elements") {
                private boolean filterChildrenOfNestedForms_;

                @Override
                protected List<Object> computeElements() {
                    final List<Object> response = super.computeElements();
                    // it would be more performant to avoid iterating through
                    // nested forms but as it is a corner case of ill formed HTML
                    // the needed refactoring would take too much time
                    // => filter here and not in isMatching as it won't be needed in most
                    // of the cases
                    if (filterChildrenOfNestedForms_) {
                        for (final Iterator<Object> iter = response.iterator(); iter.hasNext();) {
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

        return elements_;
    }

    /**
     * Returns the value of the JavaScript attribute "length".
     * Does not count input type=image elements as browsers (IE6, Firfox 1.7) do
     * (cf <a href="http://msdn.microsoft.com/en-us/library/ms534101.aspx">MSDN doc</a>)
     * @return the value of this attribute
     */
    @JsxGetter
    public int getLength() {
        final int all = getElements().getLength();
        final int images = getHtmlForm().getElementsByAttribute("input", "type", "image").size();
        return all - images;
    }

    /**
     * Returns the value of the JavaScript attribute "action".
     * @return the value of this attribute
     */
    @JsxGetter
    public String getAction() {
        String action = getHtmlForm().getActionAttribute();
        if (getBrowserVersion().hasFeature(JS_FORM_ACTION_EXPANDURL)) {
            try {
                action = ((HtmlPage) getHtmlForm().getPage()).getFullyQualifiedUrl(action).toExternalForm();
            }
            catch (final MalformedURLException e) {
                // nothing, return action attribute
            }
        }
        return action;
    }

    /**
     * Sets the value of the JavaScript attribute "action".
     * @param action the new value
     */
    @JsxSetter
    public void setAction(final String action) {
        WebAssert.notNull("action", action);
        getHtmlForm().setActionAttribute(action);
    }

    /**
     * Returns the value of the JavaScript attribute "method".
     * @return the value of this attribute
     */
    @JsxGetter
    public String getMethod() {
        return getHtmlForm().getMethodAttribute();
    }

    /**
     * Sets the value of the JavaScript attribute "method".
     * @param method the new value
     */
    @JsxSetter
    public void setMethod(final String method) {
        WebAssert.notNull("method", method);
        getHtmlForm().setMethodAttribute(method);
    }

    /**
     * Returns the value of the JavaScript attribute "target".
     * @return the value of this attribute
     */
    @JsxGetter
    public String getTarget() {
        return getHtmlForm().getTargetAttribute();
    }

    /**
     * Returns the <tt>onsubmit</tt> event handler for this element.
     * @return the <tt>onsubmit</tt> event handler for this element
     */
    @JsxGetter(@WebBrowser(value = IE))
    public Object getOnsubmit() {
        return getEventHandlerProp("onsubmit");
    }

    /**
     * Sets the <tt>onsubmit</tt> event handler for this element.
     * @param onsubmit the <tt>onsubmit</tt> event handler for this element
     */
    @JsxSetter(@WebBrowser(value = IE))
    public void setOnsubmit(final Object onsubmit) {
        setEventHandlerProp("onsubmit", onsubmit);
    }

    /**
     * Sets the value of the JavaScript attribute "target".
     * @param target the new value
     */
    @JsxSetter
    public void setTarget(final String target) {
        WebAssert.notNull("target", target);
        getHtmlForm().setTargetAttribute(target);
    }

    /**
     * Returns the value of the JavaScript attribute "enctype".
     * @return the value of this attribute
     */
    @JsxGetter
    public String getEnctype() {
        final String encoding = getHtmlForm().getEnctypeAttribute();
        if (!FormEncodingType.URL_ENCODED.getName().equals(encoding)
                && !FormEncodingType.MULTIPART.getName().equals(encoding)
                && !"text/plain".equals(encoding)) {
            return FormEncodingType.URL_ENCODED.getName();
        }
        return encoding;
    }

    /**
     * Sets the value of the JavaScript attribute "enctype".
     * @param enctype the new value
     */
    @JsxSetter
    public void setEnctype(final String enctype) {
        WebAssert.notNull("encoding", enctype);
        if (getBrowserVersion().hasFeature(JS_FORM_REJECT_INVALID_ENCODING)) {
            if (!FormEncodingType.URL_ENCODED.getName().equals(enctype)
                    && !FormEncodingType.MULTIPART.getName().equals(enctype)) {
                throw Context.reportRuntimeError("Cannot set the encoding property to invalid value: '"
                        + enctype + "'");
            }
        }
        getHtmlForm().setEnctypeAttribute(enctype);
    }

    /**
     * Returns the value of the JavaScript attribute "encoding".
     * @return the value of this attribute
     */
    @JsxGetter
    public String getEncoding() {
        return getEnctype();
    }

    /**
     * Sets the value of the JavaScript attribute "encoding".
     * @param encoding the new value
     */
    @JsxSetter
    public void setEncoding(final String encoding) {
        setEnctype(encoding);
    }

    private HtmlForm getHtmlForm() {
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
            final boolean isHashJump = HttpMethod.GET == request.getHttpMethod() && action.endsWith("#");
            webClient.download(page.getEnclosingWindow(), target, request, isHashJump, "JS form.submit()");
        }
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
    @JsxFunction(@WebBrowser(IE))
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
        final List<HtmlElement> elements = findElements(name);

        if (elements.isEmpty()) {
            return NOT_FOUND;
        }
        if (elements.size() == 1) {
            return getScriptableFor(elements.get(0));
        }

        final HTMLCollection collection = new HTMLCollection(getHtmlForm(), elements) {
            @Override
            protected List<Object> computeElements() {
                return new ArrayList<Object>(findElements(name));
            }
        };
        return collection;
    }

    private List<HtmlElement> findElements(final String name) {
        final List<HtmlElement> elements = new ArrayList<HtmlElement>();
        addElements(name, getHtmlForm().getHtmlElementDescendants(), elements);
        addElements(name, getHtmlForm().getLostChildren(), elements);

        // If no form fields are found, IE and Firefox are able to find img elements by ID or name.
        if (elements.isEmpty()) {
            for (final DomNode node : getHtmlForm().getChildren()) {
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
     * @return <code>true</code> if this element matches the conditions
     */
    private boolean isAccessibleByIdOrName(final HtmlElement element, final String name) {
        if ((element instanceof FormFieldWithNameHistory && !(element instanceof HtmlImageInput))) {
            if (element.getEnclosingForm() != getHtmlForm()) {
                return false; // nested forms
            }
            final FormFieldWithNameHistory elementWithNames = (FormFieldWithNameHistory) element;
            if (name.equals(elementWithNames.getOriginalName())
                    || name.equals(element.getId())) {
                return true;
            }

            if (getBrowserVersion().hasFeature(FORMFIELD_REACHABLE_BY_NEW_NAMES)) {
                if (name.equals(element.getAttribute("name"))
                        || elementWithNames.getPreviousNames().contains(name)) {
                    return true;
                }
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
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (!getBrowserVersion().hasFeature(GENERATED_80)) {
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
        return Context.getUndefinedValue();
    }

    /**
     * {@inheritDoc}
     */
    public Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        if (!getBrowserVersion().hasFeature(GENERATED_81)) {
            throw Context.reportRuntimeError("Not a function.");
        }
        return null;
    }

    @Override
    public boolean dispatchEvent(final Event event) {
        final boolean result = super.dispatchEvent(event);

        if (event.getType().equals(Event.TYPE_SUBMIT)) {
            submit();
        }
        return result;
    }
}
