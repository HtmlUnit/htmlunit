/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
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
public class History extends SimpleScriptable {

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public History() {}


    /**
     * Return the "length" property.  Currently hardcoded to return 0;
     * @return the "length" property
     */
    public int jsGet_length() {
        getLog().debug("javascript: history.length not implemented yet - returning 0");
        return 0;
    }


    /**
     * javascript function "back".  Currently not implemented
     */
    public void jsFunction_back() {
        getLog().debug("javascript: history.back() not implemented yet");
    }


    /**
     * javascript function "forward".  Currently not implemented
     */
    public void jsFunction_forward() {
        getLog().debug("javascript: history.forward() not implemented yet");
    }


    /**
     * javascript function "go".  Currently not implemented
     * @param relativeUrl The relative url
     */
    public void jsFunction_go( final String relativeUrl) {
        getLog().debug("javascript: history.go(String) not implemented yet");
    }
}

