/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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
    private static final long serialVersionUID = -2907220432378132233L;
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


    private URL getUrl() {
        return window_.getWebWindow().getEnclosedPage().getWebResponse().getUrl();
    }

    /**
     * Return the hostname that is part of the location url
     * @return The hostname
     */
    public String jsGet_hostname() {
        return getUrl().getHost();
    }


    /**
     * Return the string value of the location, which is the full URL.
     * @return The string URL
     */
    public String toString() {
        return jsGet_href();
    }

    /**
     * Return the search string
     * @return The value.
     */
    public String jsGet_search() {
        final String search = getUrl().getQuery();
        if( search == null ) {
            return "";
        }
        else {
            return "?"+search;
        }
    }

    /**
     * Return the value of "hash"
     * @return The value.
     */
    public String jsGet_hash() {
        final String hash = getUrl().getRef();
        if( hash == null ) {
            return "";
        }
        else {
            return hash;
        }
    }

    /**
     * Return the value of "host"
     * @return The value.
     */
    public String jsGet_host() {
        final URL url = getUrl();
        final int port = url.getPort();
        final String host = url.getHost();

        if( port == - 1 ) {
            return host;
        }
        else {
            return host+":"+port;
        }
    }

    /**
     * Return the value of "pathname"
     * @return The value.
     */
    public String jsGet_pathname() {
        return getUrl().getPath();
    }

    /**
     * Return the value of "port"
     * @return The value.
     */
    public String jsGet_port() {
        final int port = getUrl().getPort();
        if( port == -1 ) {
            return "";
        }
        else {
            return String.valueOf(port);
        }
    }

    /**
     * Return the value of "protocol"
     * @return The value.
     */
    public String jsGet_protocol() {
        return getUrl().getProtocol();
    }

}

