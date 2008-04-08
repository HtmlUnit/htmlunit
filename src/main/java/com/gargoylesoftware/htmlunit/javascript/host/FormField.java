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

import org.mozilla.javascript.Function;

import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;

/**
 * Base class for all JavaScript object corresponding to form fields.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class FormField extends HTMLElement {

    private static final long serialVersionUID = 3712016051364495710L;

    /**
     * Sets the associated DOM node and sets the enclosing form as parent scope of the current element.
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#setDomNode(DomNode)
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        final HtmlForm form = ((HtmlElement) domNode).getEnclosingForm();
        if (form != null) {
            setParentScope(getScriptableFor(form));
        }
    }

    /**
     * Returns the value of the JavaScript attribute "value".
     *
     * @return the value of this attribute
     */
    public String jsxGet_value() {
        return getHtmlElementOrDie().getAttributeValue("value");
    }

    /**
     * Sets the value of the JavaScript attribute "value".
     *
     * @param newValue  the new value
     */
    public void jsxSet_value(final String newValue) {
        getHtmlElementOrDie().setAttributeValue("value", newValue);
    }

    /**
     * Returns the value of the JavaScript attribute "name".
     *
     * @return the value of this attribute
     */
    public String jsxGet_name() {
        return getHtmlElementOrDie().getAttributeValue("name");
    }

    /**
     * Sets the value of the JavaScript attribute "name".
     *
     * @param newName  the new name
     */
    public void jsxSet_name(final String newName) {
        getHtmlElementOrDie().setAttributeValue("name", newName);
    }

    /**
     * Returns the value of the JavaScript attribute "form".
     *
     * @return the value of this attribute
     */
    public HTMLFormElement jsxGet_form() {
        return (HTMLFormElement) getScriptableFor(getHtmlElementOrDie().getEnclosingForm());
    }

    /**
     * Returns the value of the JavaScript attribute "type".
     *
     * @return the value of this attribute
     */
    public String jsxGet_type() {
        return getHtmlElementOrDie().getAttributeValue("type");
    }

    /**
     * Sets the <tt>onchange</tt> event handler for this element.
     * @param onchange the <tt>onchange</tt> event handler for this element
     */
    public void jsxSet_onchange(final Object onchange) {
        setEventHandlerProp("onchange", onchange);
    }

    /**
     * Returns the <tt>onchange</tt> event handler for this element.
     * @return the <tt>onchange</tt> event handler for this element
     */
    public Function jsxGet_onchange() {
        return getEventHandler("onchange");
    }

    /**
     * Click this element. This simulates the action of the user clicking with the mouse.
     * @throws IOException if this click triggers a page load that encounters problems
     */
    public void jsxFunction_click() throws IOException {
        ((ClickableElement) getHtmlElementOrDie()).click();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean jsxGet_disabled() {
        // TODO: is this method necessary?
        return getHtmlElementOrDie().isAttributeDefined("disabled");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void jsxSet_disabled(final boolean disabled) {
        // TODO: is this method necessary?
        final HtmlElement element = getHtmlElementOrDie();
        if (disabled) {
            element.setAttributeValue("disabled", "disabled");
        }
        else {
            element.removeAttribute("disabled");
        }
    }

    /**
     * Returns the value of the tabIndex attribute.
     * @return the value of the tabIndex attribute
     */
    public int jsxGet_tabIndex() {
        int index = 0;
        try {
            index = Integer.parseInt(getHtmlElementOrDie().getAttributeValue("tabindex"));
        }
        catch (final Exception e) {
            //ignore
        }
        return index;
    }
}
