/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import org.w3c.dom.Element;

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
        return "Foobar";
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
        getLog().debug( "Input.jsSet_checked(" + checked
            + ") was called for class " + getClass().getName() );
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
        getLog().warn( "Input.jsGet_checked() was called for class " + getClass().getName() );
        return false;
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


    public boolean jsGet_disabled() {
        return getHtmlElementOrDie().isAttributeDefined("disabled");
    }


    public void jsSet_disabled( final boolean disabled ) {
        final Element xmlElement = getHtmlElementOrDie().getElement();
        if( disabled ) {
            xmlElement.setAttribute("disabled", "disabled");
        }
        else {
            xmlElement.removeAttribute("disabled");
        }
    }
}

