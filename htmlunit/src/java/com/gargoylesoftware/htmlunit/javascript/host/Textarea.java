/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * The javascript object that represents a textarea
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Textarea extends Input {

    /**
     * Create an instance.
     */
    public Textarea() {
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
        return "textarea";
    }


    public String jsGet_value() {
        return ((HtmlTextArea)getHtmlElementOrDie()).getValue();
    }


    public void jsSet_value( final String value ) {
        ((HtmlTextArea)getHtmlElementOrDie()).setValue(value);
    }
}

