/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import java.net.URL;

/**
 * A javascript object for a Location
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Location extends SimpleScriptable {
    private Window window_;

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public Location() {
    }


    /**
     * Initialize the object
     * @param window The window that this location belongs to.
     */
    public void initialize( final Window window ) {
        window_ = window;
    }


    /**
     * Set the "href" property
     * @param href The new location
     */
    public void jsSet_href( final String href ) {
        window_.jsSet_location(href);
    }


    /**
     * Return the value of the href property;
     * @return the value of the href property
     */
    public String jsGet_href() {
        final Page page = window_.getWebWindow().getEnclosedPage();
        if( page == null ) {
            return "Unknown";
        }
        else {
            return page.getWebResponse().getUrl().toExternalForm();
        }
    }


    /**
     * Reload the window with the specified url
     * @param href The new url
     */
    public void jsFunction_reload( final String href ) {
        getLog().debug("Not implemented yet: reload("+href+")");
    }


    /**
     * Reload the window with the specified url
     * @param href The new url
     */
    public void jsFunction_replace( final String href ) {
        getLog().debug("Not implemented yet: replace("+href+")");
    }


    /**
     * Return the hostname that is part of the location url
     * @return The hostname
     */
    public String jsGet_hostname() {
        final URL url = window_.getWebWindow().getEnclosedPage().getWebResponse().getUrl();
        return url.getHost();
    }


    /**
     * Return the string value of the location, which is the full URL.
     * @return The string URL
     */
    public String toString() {
        return jsGet_href();
    }
}

