/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A javascript object for a Style
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Style extends SimpleScriptable {
    private HTMLElement htmlElement_;

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public Style() {
    }


    /**
     * Initialize the object
     * @param htmlElement The element that this style describes
     */
    public void initialize( final HTMLElement htmlElement ) {
        assertNotNull("htmlElement", htmlElement);
        htmlElement_ = htmlElement;
    }
}

