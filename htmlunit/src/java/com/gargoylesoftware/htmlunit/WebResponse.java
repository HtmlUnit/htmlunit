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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *  A response from a web server
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
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
     */
    String getContentAsString();


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

    /**
     * Return the content charset value.
     * @return The charset value.
     */
    String getContentCharSet();

    /**
     * Return the response body as byte array.
     * @return response body.
     */
    byte[] getResponseBody();
}

