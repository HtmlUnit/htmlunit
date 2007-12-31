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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.util.List;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
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
     * Creates an instance. A default constructor is required for all JavaScript objects.
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
        getHtmlForm().submit((SubmittableElement) null);
    }
    
    /**
     * Reset this form
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
    protected Object getWithPreemption(final String name) {
        final HtmlForm form = getHtmlForm();
        final HtmlPage page = form.getPage();
        if (page != null) {
            // Try to satisfy this request using a map-backed operation before punting and using XPath.
            // XPath operations are very expensive, and this method gets invoked quite a bit.
            // Approach: Try to match the string to a name or ID, accepting only inputs (not type=image),
            // buttons, selects and textareas that are in this form. We also include img elements
            // (the second XPath search below) in the search, because any results with more than one element
            // will end up using the XPath search anyway, so it doesn't hurt when looking for single elements.
            final List elements = page.getHtmlElementsByIdAndOrName(name);
            if (elements.isEmpty()) {
                return NOT_FOUND;
            }
            if (elements.size() == 1) {
                final HtmlElement element = (HtmlElement) elements.get(0);
                final String tagName = element.getTagName();
                final String type = element.getAttribute("type").toLowerCase();
                if ((HtmlInput.TAG_NAME.equals(tagName) && !"image".equals(type))
                        || HtmlButton.TAG_NAME.equals(tagName)
                        || HtmlSelect.TAG_NAME.equals(tagName)
                        || HtmlTextArea.TAG_NAME.equals(tagName)
                        || HtmlImage.TAG_NAME.equals(tagName)) {
                    if (form.isAncestorOf(element)) {
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
        HTMLCollection collection = new HTMLCollection(this);
        try {
            final XPath xpath = new HtmlUnitXPath("//*[(@name = '" + name + "' or @id = '" + name + "')"
                + " and ((name() = 'input' and translate(@type, 'IMAGE', 'image') != 'image') or name() = 'button'"
                + " or name() = 'select' or name() = 'textarea')]", HtmlUnitXPath.buildSubtreeNavigator(form));
            collection.init(form, xpath);
        }
        catch (final JaxenException e) {
            throw Context.reportRuntimeError("Failed to initialize collection: " + e.getMessage());
        }
        int length = collection.jsxGet_length();
        // If no form fields are found, IE and Firefox are able to find img elements by ID or name.
        if (length == 0) {
            collection = new HTMLCollection(this);
            try {
                final XPath xpath = new HtmlUnitXPath("//*[(@name = '" + name + "' or @id = '" + name + "')"
                    + " and name() = 'img']", HtmlUnitXPath.buildSubtreeNavigator(form));
                collection.init(form, xpath);
            }
            catch (final JaxenException e) {
                throw Context.reportRuntimeError("Failed to initialize collection: " + e.getMessage());
            }
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
     * Return the specified indexed property
     * @param index The index of the property
     * @param start The scriptable object that was originally queried for this property
     * @return The property.
     */
    public Object get(final int index, final Scriptable start) {
        return jsxGet_elements().get(index, ((HTMLFormElement) start).jsxGet_elements());
    }
}
