/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A javascript object for a Screen
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Screen extends SimpleScriptable {

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public Screen() {}


    /**
     * Return the "colorDepth" property.  Currently hardcoded to return 24.
     * @return the "colorDepth" property
     */
    public int jsGet_colorDepth() {
        return 24;
    }


    /**
     * Return the "height" property.  Currently hard coded to return 600.
     * @return the "height" property
     */
    public int jsGet_height() {
        return 600;
    }
}

