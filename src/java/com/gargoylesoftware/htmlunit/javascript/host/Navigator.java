/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A javascript object for a Navigator
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class Navigator extends SimpleScriptable {

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public Navigator() {}


    /**
     * Return the property "appName"
     * @return The property
     */
    public String jsGet_appName() {
        return getBrowserVersion().getApplicationName();
    }


    /**
     * Return the property "appVersion"
     * @return The property
     */
    public String jsGet_appVersion() {
        return getBrowserVersion().getApplicationVersion();
    }


    /**
     * Return the property "userAgent"
     * @return The property
     */
    public String jsGet_userAgent() {
        return getBrowserVersion().getUserAgent();
    }


    private BrowserVersion getBrowserVersion() {
        return BrowserVersion.getDefault();
    }


    public boolean jsGet_javaEnabled() {
        return false;
    }
}

