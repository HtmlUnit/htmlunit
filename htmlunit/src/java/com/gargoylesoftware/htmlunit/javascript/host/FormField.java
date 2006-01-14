/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * Base class for all javascript object correspondig to form fields.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 */
public class FormField extends FocusableHostElement {

    private static final long serialVersionUID = 3712016051364495710L;


    /**
     *  Return the value of the javascript attribute "value".
     *
     *@return    The value of this attribute.
     */
    public String jsxGet_value() {
        return getHtmlElementOrDie().getAttributeValue( "value" );
    }


    /**
     *  Set the value of the javascript attribute "value".
     *
     *@param  newValue  The new value.
     */
    public void jsxSet_value( final String newValue ) {
        getHtmlElementOrDie().setAttributeValue( "value", newValue );
    }


    /**
     *  Return the value of the javascript attribute "name".
     *
     *@return    The value of this attribute.
     */
    public String jsxGet_name() {
        return getHtmlElementOrDie().getAttributeValue( "name" );
    }
    
    /**
     *  Set the value of the javascript attribute "name".
     *
     *@param  newName  The new name.
     */
    public void jsxSet_name( final String newName ) {
        getHtmlElementOrDie().setAttributeValue( "name", newName );
    }    


    /**
     *  Return the value of the javascript attribute "form".
     *
     *@return The value of this attribute.
     */
    public Form jsxGet_form() {
        return (Form)getScriptableFor(getHtmlElementOrDie().getEnclosingForm());
    }

    /**
     * Return the value of the javascript attribute "type".
     *
     *@return The value of this attribute.
     */
    public String jsxGet_type() {
        return getHtmlElementOrDie().getAttributeValue("type");
    }

    /**
     * Set the onchange event handler for this element.
     * @param onchange the new handler
     */
    public void jsxSet_onchange(final Function onchange) {
        getHtmlElementOrDie().setEventHandler("onchange", onchange);
    }

    /**
     * Get the onchange event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onchange() {
        return getHtmlElementOrDie().getEventHandler("onchange");
    }

    /**
     * Click this element.  This simulates the action of the user clicking with the mouse.
     * @throws IOException if this click triggers a page load that encouters problems
     */
    public void jsxFunction_click() throws IOException {
        ((ClickableElement) getHtmlElementOrDie()).click();
    }

    /**
     * Select this element.
     */
    public void jsxFunction_select() {
        getLog().debug( "Input.jsxFunction_select() not implemented" );
    }

    /**
     * Return true if this element is disabled.
     * @return True if this element is disabled.
     */
    public boolean jsxGet_disabled() {
        return getHtmlElementOrDie().isAttributeDefined("disabled");
    }


    /**
     * Set whether or not to disable this element
     * @param disabled True if this is to be disabled.
     */
    public void jsxSet_disabled( final boolean disabled ) {
        final HtmlElement element = getHtmlElementOrDie();
        if( disabled ) {
            element.setAttributeValue("disabled", "disabled");
        }
        else {
            element.removeAttribute("disabled");
        }
    }

    /**
     * Return the value of the tabindex attribute.
     * @return the value of the tabindex attribute.
     */
    public String jsxGet_tabindex() {
        return getHtmlElementOrDie().getAttributeValue("tabindex");
    }
}

