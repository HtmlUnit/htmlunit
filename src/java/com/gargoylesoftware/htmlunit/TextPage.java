/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import java.io.IOException;

/**
 *  A generic page that will be returned for any text related content.
 *  Specifically any content types that start with "text/"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class TextPage implements Page {
    private final WebResponse webResponse_;
    private final String content_;

    private WebWindow enclosingWindow_;

    /**
     *  Create an instance
     *
     * @param  webResponse The response from the server
     * @param  enclosingWindow The window that holds the page.
     * @exception  IOException If an IO error occurs
     */
    public TextPage( final WebResponse webResponse, final WebWindow enclosingWindow )
        throws IOException {
        webResponse_ = webResponse;
        content_ = webResponse.getContentAsString();
        enclosingWindow_ = enclosingWindow;
    }


    /**
     * Initialize this page.
     */
    public void initialize() {
    }


    /**
     *  Return the content of the page
     *
     * @return  See above
     */
    public String getContent() {
        return content_;
    }


    /**
     *  Return the web response that was originally used to create this page.
     *
     * @return  The web response
     */
    public WebResponse getWebResponse() {
        return webResponse_;
    }


    /**
     * Return the window that this page is sitting inside.
     *
     * @return The enclosing frame or null if this page isn't inside a frame.
     */
    public WebWindow getEnclosingWindow() {
        return enclosingWindow_;
    }
}

