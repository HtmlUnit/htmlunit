/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.util.List;

import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.SubmittableElement;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;

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
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535249.aspx">MSDN documentation</a>
 */
public class HTMLFormElement extends HTMLElement {

    private static final long serialVersionUID = -1860993922147246513L;
    private HTMLCollection elements_; // has to be a member to have equality (==) working

    /**
     * Creates an instance. A default constructor is required for all JavaScript objects.
     */
    public HTMLFormElement() { }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public final void jsConstructor() {
    }

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
    public String jsxGet_name() {
        return getHtmlForm().getNameAttribute();
    }

    /**
     * Sets the value of the JavaScript attribute "name".
     * @param name the new value
     */
    public void jsxSet_name(final String name) {
        WebAssert.notNull("name", name);
        getHtmlForm().setNameAttribute(name);
    }

    /**
     * Returns the value of the JavaScript attribute "elements".
     * @return the value of this attribute
     */
    public HTMLCollection jsxGet_elements() {
        if (elements_ == null) {
            final HtmlForm htmlForm = getHtmlForm();

            elements_ = new HTMLCollection(this) {
                private static final long serialVersionUID = -2554743215194459203L;

                @Override
                protected List<Object> computeElements() {
                    final List<Object> response = super.computeElements();
                    response.addAll(htmlForm.getLostChildren());
                    return response;
                }
            };
            final String xpath = ".//*[(name() = 'input' or name() = 'button'"
                    + " or name() = 'select' or name() = 'textarea')]";
            elements_.init(htmlForm, xpath);

        }
        return elements_;
    }

    /**
     * Returns the value of the JavaScript attribute "length".
     * Does not count input type=image elements as browsers (IE6, Mozilla 1.7) do
     * (cf <a href="http://msdn.microsoft.com/en-us/library/ms534101.aspx">MSDN doc</a>)
     * @return the value of this attribute
     */
    public int jsxGet_length() {
        final int all = jsxGet_elements().jsxGet_length();
        final int images = getHtmlForm().getElementsByAttribute("input", "type", "image").size();
        return all - images;
    }

    /**
     * Returns the value of the JavaScript attribute "action".
     * @return the value of this attribute
     */
    public String jsxGet_action() {
        return getHtmlForm().getActionAttribute();
    }

    /**
     * Sets the value of the JavaScript attribute "action".
     * @param action the new value
     */
    public void jsxSet_action(final String action) {
        WebAssert.notNull("action", action);
        getHtmlForm().setActionAttribute(action);
    }

    /**
     * Returns the value of the JavaScript attribute "method".
     * @return the value of this attribute
     */
    public String jsxGet_method() {
        return getHtmlForm().getMethodAttribute();
    }

    /**
     * Sets the value of the JavaScript attribute "method".
     * @param method the new value
     */
    public void jsxSet_method(final String method) {
        WebAssert.notNull("method", method);
        getHtmlForm().setMethodAttribute(method);
    }

    /**
     * Returns the value of the JavaScript attribute "target".
     * @return the value of this attribute
     */
    public String jsxGet_target() {
        return getHtmlForm().getTargetAttribute();
    }

    /**
     * Returns the <tt>onsubmit</tt> event handler for this element.
     * @return the <tt>onsubmit</tt> event handler for this element
     */
    public Object jsxGet_onsubmit() {
        return getEventHandlerProp("onsubmit");
    }

    /**
     * Sets the <tt>onsubmit</tt> event handler for this element.
     * @param onsubmit the <tt>onsubmit</tt> event handler for this element
     */
    public void jsxSet_onsubmit(final Object onsubmit) {
        setEventHandlerProp("onsubmit", onsubmit);
    }

    /**
     * Sets the value of the JavaScript attribute "target".
     * @param target the new value
     */
    public void jsxSet_target(final String target) {
        WebAssert.notNull("target", target);
        getHtmlForm().setTargetAttribute(target);
    }

    /**
     * Returns the value of the JavaScript attribute "encoding".
     * @return the value of this attribute
     */
    public String jsxGet_encoding() {
        return getHtmlForm().getEnctypeAttribute();
    }

    /**
     * Sets the value of the JavaScript attribute "encoding".
     * @param encoding the new value
     */
    public void jsxSet_encoding(final String encoding) {
        WebAssert.notNull("encoding", encoding);
        getHtmlForm().setEnctypeAttribute(encoding);
    }

    private HtmlForm getHtmlForm() {
        return (HtmlForm) getDomNodeOrDie();
    }

    /**
     * Submits the form (at the end of the current script execution).
     *
     * @throws IOException if an IO error occurs
     */
    public void jsxFunction_submit() throws IOException {
        final SgmlPage page = getDomNodeOrDie().getPage();
        final JavaScriptEngine jsEngine = page.getWebClient().getJavaScriptEngine();
        final PostponedAction action = new PostponedAction() {
            public void execute() throws Exception {
                getHtmlForm().submit((SubmittableElement) null);
            }
        };
        jsEngine.addPostponedAction(action);
    }

    /**
     * Resets this form.
     */
    public void jsxFunction_reset() {
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
        final HtmlForm form = getHtmlForm();
        final Page page = form.getPage();
        if (page instanceof HtmlPage) {
            // Try to satisfy this request using a map-backed operation before punting and using XPath.
            // XPath operations are very expensive, and this method gets invoked quite a bit.
            // Approach: Try to match the string to a name or ID, accepting only inputs (not type=image),
            // buttons, selects and textareas that are in this form. We also include img elements
            // (the second XPath search below) in the search, because any results with more than one element
            // will end up using the XPath search anyway, so it doesn't hurt when looking for single elements.
            final List<HtmlElement> elements = ((HtmlPage) page).getElementsByIdAndOrName(name);
            if (elements.isEmpty()) {
                return NOT_FOUND;
            }
            if (elements.size() == 1) {
                final HtmlElement element = elements.get(0);
                final String tagName = element.getTagName();
                final String type = element.getAttribute("type").toLowerCase();
                if ((HtmlInput.TAG_NAME.equals(tagName) && !"image".equals(type))
                        || HtmlButton.TAG_NAME.equals(tagName)
                        || HtmlSelect.TAG_NAME.equals(tagName)
                        || HtmlTextArea.TAG_NAME.equals(tagName)
                        || HtmlImage.TAG_NAME.equals(tagName)) {
                    if (form.isAncestorOf(element) || form.getLostChildren().contains(element)) {
                        return getScriptableFor(element);
                    }
                }
                else {
                    return NOT_FOUND;
                }
            }
        }
        // The shortcut wasn't enough, which means we probably need to perform the XPath operation anyway.
        // Note that the XPath expression below HAS TO MATCH the tag name checks performed in the shortcut above.
        // Approach: Try to match the string to a name or ID, accepting only inputs (not type=image),
        // buttons, selects and textareas that are in this form. We *don't* include img elements, which will
        // only be searched if the first search fails.
        // See IsDescendantOfContextualFormFunction for info on the "is-descendant-of-contextual-form()" function.
        HTMLCollection collection = new HTMLCollection(this);
        final String xpath = "//*[is-descendant-of-contextual-form()"
            + " and (@name = '" + name + "' or @id = '" + name + "')"
            + " and ((name() = 'input' and translate(@type, 'IMAGE', 'image') != 'image') or name() = 'button'"
            + " or name() = 'select' or name() = 'textarea')]";
        collection.init(form, xpath);
        int length = collection.jsxGet_length();
        // If no form fields are found, IE and Firefox are able to find img elements by ID or name.
        if (length == 0) {
            collection = new HTMLCollection(this);
            final String xpath2 = "//*[is-descendant-of-contextual-form()"
                + " and (@name = '" + name + "' or @id = '" + name + "')"
                + " and name() = 'img']";
            collection.init(form, xpath2);
        }
        // Return whatever we have at this point.
        Object result = collection;
        length = collection.jsxGet_length();
        if (length == 0) {
            result = NOT_FOUND;
        }
        else if (length == 1) {
            result = collection.get(0, collection);
        }
        return result;
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
        return jsxGet_elements().get(index, ((HTMLFormElement) start).jsxGet_elements());
    }
}
