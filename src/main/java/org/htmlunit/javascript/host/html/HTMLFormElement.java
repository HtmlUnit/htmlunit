/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import static org.htmlunit.BrowserVersionFeatures.JS_FORM_DISPATCHEVENT_SUBMITS;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.htmlunit.FormEncodingType;
import org.htmlunit.WebAssert;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.FormFieldWithNameHistory;
import org.htmlunit.html.HtmlAttributeChangeEvent;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.SubmittableElement;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.configuration.JsxSymbol;
import org.htmlunit.javascript.host.dom.AbstractList.EffectOnCache;
import org.htmlunit.javascript.host.dom.DOMTokenList;
import org.htmlunit.javascript.host.dom.RadioNodeList;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.util.MimeType;

/**
 * A JavaScript object {@code HTMLFormElement}.
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
 * @author Lai Quang Duong
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535249.aspx">MSDN documentation</a>
 */
@JsxClass(domClass = HtmlForm.class)
public class HTMLFormElement extends HTMLElement implements Function {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
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
    public HTMLFormControlsCollection getElements() {
        final HtmlForm htmlForm = getHtmlForm();

        final HTMLFormControlsCollection elements = new HTMLFormControlsCollection(htmlForm,
                false) {
            @Override
            protected Object getWithPreemption(final String name) {
                return HTMLFormElement.this.getWithPreemption(name);
            }
        };

        elements.setElementsSupplier(
                (Supplier<List<DomNode>> & Serializable)
                () -> {
                    final DomNode domNode = getDomNodeOrNull();
                    if (domNode == null) {
                        return new ArrayList<>();
                    }
                    return new ArrayList<>(((HtmlForm) domNode).getElementsJS());
                });

        elements.setEffectOnCacheFunction(
                (java.util.function.Function<HtmlAttributeChangeEvent, EffectOnCache> & Serializable)
                event -> EffectOnCache.NONE);

        return elements;
    }

    @JsxSymbol
    public Scriptable iterator() {
        return getElements().iterator();
    }

    /**
     * Returns the value of the property {@code length}.
     * Does not count input {@code type=image} elements
     * (<a href="http://msdn.microsoft.com/en-us/library/ms534101.aspx">MSDN doc</a>)
     * @return the value of this property
     */
    @JsxGetter
    public int getLength() {
        return getElements().getLength();
    }

    /**
     * Returns the value of the property {@code action}.
     * @return the value of this property
     */
    @JsxGetter
    public String getAction() {
        final String action = getHtmlForm().getActionAttribute();

        try {
            return ((HtmlPage) getHtmlForm().getPage()).getFullyQualifiedUrl(action).toExternalForm();
        }
        catch (final MalformedURLException ignored) {
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
     * Returns the value of the rel property.
     * @return the rel property
     */
    @JsxGetter
    public String getRel() {
        return getHtmlForm().getRelAttribute();
    }

    /**
     * Sets the rel property.
     * @param rel rel attribute value
     */
    @JsxSetter
    public void setRel(final String rel) {
        getHtmlForm().setAttribute("rel", rel);
    }

    /**
     * Returns the {@code relList} attribute.
     * @return the {@code relList} attribute
     */
    @JsxGetter
    public DOMTokenList getRelList() {
        return new DOMTokenList(this, "rel");
    }

    /**
     * Sets the relList property.
     * @param rel attribute value
     */
    @JsxSetter
    public void setRelList(final Object rel) {
        if (JavaScriptEngine.isUndefined(rel)) {
            setRel("undefined");
            return;
        }
        setRel(JavaScriptEngine.toString(rel));
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
        getHtmlForm().submit(null);
    }

    /**
     * Submits the form by submitted using a specific submit button.
     * @param submitter The submit button whose attributes describe the method
     * by which the form is to be submitted. This may be either
     * an &lt;input&gt; or &lt;button&gt; element whose type attribute is submit.
     * If you omit the submitter parameter, the form element itself is used as the submitter.
     */
    @JsxFunction
    public void requestSubmit(final Object submitter) {
        if (JavaScriptEngine.isUndefined(submitter)) {
            submit();
            return;
        }

        SubmittableElement submittable = null;
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
                throw JavaScriptEngine.typeError(
                        "Failed to execute 'requestSubmit' on 'HTMLFormElement': "
                        + "The specified element is not owned by this form element.");
            }
        }

        if (submittable == null) {
            throw JavaScriptEngine.typeError(
                    "Failed to execute 'requestSubmit' on 'HTMLFormElement': "
                    + "The specified element is not a submit button.");
        }

        this.getHtmlForm().submit(submittable);
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

        final RadioNodeList nodeList = new RadioNodeList(getHtmlForm(), nodes);
        nodeList.setElementsSupplier(
                (Supplier<List<DomNode>> & Serializable)
                () -> new ArrayList<>(findElements(name)));
        return nodeList;
    }

    /**
     * Overridden to allow the retrieval of certain form elements by ID or name.
     *
     * @param name {@inheritDoc}
     * @param start {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        if (super.has(name, start)) {
            return true;
        }

        return findFirstElement(name) != null;
    }

    /**
     * Overridden to allow the retrieval of certain form elements by ID or name.
     *
     * @param cx {@inheritDoc}
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected ScriptableObject getOwnPropertyDescriptor(final Context cx, final Object id) {
        final ScriptableObject desc = super.getOwnPropertyDescriptor(cx, id);
        if (desc != null) {
            return desc;
        }

        if (id instanceof CharSequence) {
            final HtmlElement element = findFirstElement(id.toString());
            if (element != null) {
                return ScriptableObject.buildDataDescriptor(this, element.getScriptableObject(),
                                            ScriptableObject.READONLY | ScriptableObject.DONTENUM);
            }
        }

        return null;
    }

    List<HtmlElement> findElements(final String name) {
        final List<HtmlElement> elements = new ArrayList<>();
        final HtmlForm form = (HtmlForm) getDomNodeOrNull();
        if (form == null) {
            return elements;
        }

        for (final HtmlElement element : form.getElementsJS()) {
            if (isAccessibleByIdOrName(element, name)) {
                elements.add(element);
            }
        }

        // If no form fields are found, browsers are able to find img elements by ID or name.
        if (elements.isEmpty()) {
            for (final DomNode node : form.getHtmlElementDescendants()) {
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

    private HtmlElement findFirstElement(final String name) {
        final HtmlForm form = (HtmlForm) getDomNodeOrNull();
        if (form == null) {
            return null;
        }

        for (final HtmlElement node : form.getElementsJS()) {
            if (isAccessibleByIdOrName(node, name)) {
                return node;
            }
        }

        // If no form fields are found, browsers are able to find img elements by ID or name.
        for (final DomNode node : form.getHtmlElementDescendants()) {
            if (node instanceof HtmlImage) {
                final HtmlImage img = (HtmlImage) node;
                if (name.equals(img.getId()) || name.equals(img.getNameAttribute())) {
                    return img;
                }
            }
        }

        return null;
    }

    /**
     * Indicates if the element can be reached by id or name in expressions like "myForm.myField".
     * @param element the element to test
     * @param name the name used to address the element
     * @return {@code true} if this element matches the conditions
     */
    private static boolean isAccessibleByIdOrName(final HtmlElement element, final String name) {
        if (name.equals(element.getId())) {
            return true;
        }

        if (name.equals(element.getAttributeDirect(DomElement.NAME_ATTRIBUTE))) {
            return true;
        }

        if (element instanceof FormFieldWithNameHistory) {
            final FormFieldWithNameHistory elementWithNames = (FormFieldWithNameHistory) element;

            if (name.equals(elementWithNames.getOriginalName())) {
                return true;
            }

            if (elementWithNames.getNewNames().contains(name)) {
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
        throw JavaScriptEngine.reportRuntimeError("Not a function.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        throw JavaScriptEngine.reportRuntimeError("Not a function.");
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

    /**
     * Returns the value of the property {@code novalidate}.
     * @return the value of this property
     */
    @JsxGetter
    public boolean isNoValidate() {
        return getHtmlForm().isNoValidate();
    }

    /**
     * Sets the value of the property {@code novalidate}.
     * @param value the new value
     */
    @JsxSetter
    public void setNoValidate(final boolean value) {
        getHtmlForm().setNoValidate(value);
    }
}
