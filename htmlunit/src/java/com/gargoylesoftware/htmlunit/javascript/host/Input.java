/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import org.w3c.dom.Element;

import com.gargoylesoftware.htmlunit.html.HtmlInput;

/**
 *  The javascript object that represents something that can be put in a form.
 *
 *@version    $Revision$
 *@author     <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Input extends HTMLElement {

    /**
     *  Create an instance.
     */
    public Input() {
    }


    /**
     *  Javascript constructor. This must be declared in every javascript file
     *  because the rhino engine won't walk up the hierarchy looking for
     *  constructors.
     */
    public void jsConstructor() {
    }


    /**
     *  Return the value of the javascript attribute "value".
     *
     *@return    The value of this attribute.
     */
    public String jsGet_value() {
        return getHtmlElementOrDie().getAttributeValue( "value" );
    }


    /**
     *  Set the value of the javascript attribute "value".
     *
     *@param  newValue  The new value.
     */
    public void jsSet_value( final String newValue ) {
        getHtmlElementOrDie().getElement().setAttribute( "value", newValue );
    }


    /**
     *  Return the value of the javascript attribute "name".
     *
     *@return    The value of this attribute.
     */
    public String jsGet_name() {
        return getHtmlElementOrDie().getAttributeValue( "name" );
    }


    /**
     *  Return the value of the javascript attribute "form".
     *
     *@return    The value of this attribute.
     */
    public Form jsGet_form() {
        return (Form)getHtmlElementOrDie().getEnclosingForm().getScriptObject();
    }


    /**
     *  Return the value of the javascript attribute "type".
     *
     *@return    The value of this attribute.
     */
    public String jsGet_type() {
        return getHtmlElementOrDie().getAttributeValue("type");
    }


    /**
     *  Set the checked property. Although this property is defined in Input it
     *  doesn't make any sense for input's other than checkbox and radio. This
     *  implementation does nothing. The implementations in Checkbox and Radio
     *  actually do the work.
     *
     *@param  checked  True if this input should have the "checked" attribute
     *      set
     */
    public void jsSet_checked( final boolean checked ) {
        String type = getHtmlElementOrDie().getAttributeValue("type");
        if (type.equals("checkbox") || type.equals("radio")){
            ((HtmlInput)getHtmlElementOrDie()).setChecked(checked);
        }
        else {
            getLog().debug( "Input.jsSet_checked(" + checked
                + ") was called for class " + getClass().getName() );
        }
    }


    /**
     *  Return the value of the checked property. Although this property is
     *  defined in Input it doesn't make any sense for input's other than
     *  checkbox and radio. This implementation does nothing. The
     *  implementations in Checkbox and Radio actually do the work.
     *
     *@return    The checked property.
     */
    public boolean jsGet_checked() {
        String type = getHtmlElementOrDie().getAttributeValue("type");
        if (type.equals("checkbox") || type.equals("radio")){
            return ((HtmlInput)getHtmlElementOrDie()).isChecked();
        }
        else {
            getLog().warn( "Input.jsGet_checked() was called for class " + getClass().getName() );
            return false;
        }
    }


    /**
     * Set the focus to this element.
     */
    public void jsFunction_focus() {
        getLog().debug( "Input.jsFunction_focus() not implemented" );
    }


    /**
     * Remove focus from this element
     */
    public void jsFunction_blur() {
        getLog().debug( "Input.jsFunction_blur() not implemented" );
    }


    /**
     * Click this element.  This simulates the action of the user clicking with the mouse.
     */
    public void jsFunction_click() {
        getLog().debug( "Input.jsFunction_click() not implemented" );
    }


    /**
     * Select this element.
     */
    public void jsFunction_select() {
        getLog().debug( "Input.jsFunction_select() not implemented" );
    }


    /**
     * Return true if this element is disabled.
     * @return True if this element is disabled.
     */
    public boolean jsGet_disabled() {
        return getHtmlElementOrDie().isAttributeDefined("disabled");
    }


    /**
     * Set whether or not to disable this element
     * @param disabled True if this is to be disabled.
     */
    public void jsSet_disabled( final boolean disabled ) {
        final Element xmlElement = getHtmlElementOrDie().getElement();
        if( disabled ) {
            xmlElement.setAttribute("disabled", "disabled");
        }
        else {
            xmlElement.removeAttribute("disabled");
        }
    }


    /**
     * Return the value of the tabindex attribute.  Currently not implemented.
     * @return the value of the tabindex attribute.
     */
    public String jsGet_tabindex() {
        getLog().debug("Input.jsGet_tabindex not implemented");
        return "";
    }
}

