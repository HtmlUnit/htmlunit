/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.InputStream;

/**
 *  A generic page that is returned whenever an unexpected content type is
 *  returned by the server.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class UnexpectedPage implements Page {
    private final WebResponse webResponse_;

    private WebWindow enclosingWindow_;

    /**
     *  Create an instance
     *
     * @param  webResponse The response from the server that contains the data
     *      required to create this page
     * @param enclosingWindow The window that this page is being loaded into.
     */
    public UnexpectedPage( final WebResponse webResponse, final WebWindow enclosingWindow ) {
        webResponse_ = webResponse;
        enclosingWindow_ = enclosingWindow;
    }


    /**
     *  Return an input stream representing all the content that was returned
     *  from the server.
     *
     * @return  See above.
     * @exception  IOException If an IO error occurs
     */
    public InputStream getInputStream()
        throws IOException {
        return webResponse_.getContentAsStream();
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

