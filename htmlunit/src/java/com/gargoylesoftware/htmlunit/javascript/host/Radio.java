/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;


/**
 * The javascript object that represents a "radio" input
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Radio extends Input {

    /**
     * Create an instance.
     */
    public Radio() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }


    /**
     * Return the type of this input.
     * @return The type
     */
    public String jsGet_type() {
        return "radio";
    }


    /**
     * Set the checked property.
     * @param checked True if this input should have the "checked" attribute set
     */
    public void jsSet_checked( final boolean checked ) {
        ((HtmlRadioButtonInput)getHtmlElementOrDie()).setChecked(checked);
    }


    /**
     * Return the value of the checked property
     * @return The checked property.
     */
    public boolean jsGet_checked() {
        return ((HtmlRadioButtonInput)getHtmlElementOrDie()).isChecked();
    }
}

