/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.HtmlOption;

/**
 * The javascript object that represents a select
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Option extends HTMLElement {

    /**
     * Create an instance.
     */
    public Option() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }


    /**
     * Return the value of the "value" property
     * @return The value property
     */
    public String jsGet_value() {
        return ((HtmlOption)getHtmlElementOrDie()).getValueAttribute();
    }


    /**
     * Return the value of the "text" property
     * @return The text property
     */
    public String jsGet_text() {
        return jsGet_value();
    }
}
