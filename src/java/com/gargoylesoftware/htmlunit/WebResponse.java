/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *  A response from a web server
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public interface WebResponse {
    /**
     *  Return the status code that was returned by the server
     *
     * @return  See above.
     */
    int getStatusCode();


    /**
     *  Return the status message that was returned from the server
     *
     * @return  See above
     */
    String getStatusMessage();


    /**
     *  Return the content type returned from the server. Ie "text/html"
     *
     * @return  See above
     */
    String getContentType();


    /**
     *  Return the content from the server as a string
     *
     * @return  See above
     * @exception  IOException If an IO problem occurs
     */
    String getContentAsString()
        throws IOException;


    /**
     *  Return the content from the server as an input stream
     *
     * @return  See above
     * @exception  IOException If an IO problem occurs
     */
    InputStream getContentAsStream()
        throws IOException;


    /**
     * Return the URL that was used to load this page.
     *
     * @return The originating URL
     */
    URL getUrl();


    /**
     * Return the value of the specified header from this response.
     *
     * @param headerName The name of the header
     * @return The value of the specified header
     */
    String getResponseHeaderValue( final String headerName );


    /**
     * Return the time it took to load this web response in milliseconds.
     * @return The load time.
     */
    long getLoadTimeInMilliSeconds();
}

