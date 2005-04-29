/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpState;

/**
 *  An object that handles the actual communication portion of page
 *  retrieval/submission <p />
 *
 *  THIS CLASS IS FOR INTERNAL USE ONLY
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public abstract class WebConnection {
    private final String proxyHost_;
    private final int proxyPort_;
    private final WebClient webClient_;


    /**
     *  Create an instance that will not use a proxy server
     *
     * @param  webClient The WebClient that is using this connection
     */
    public WebConnection( final WebClient webClient ) {
        webClient_ = webClient;
        proxyHost_ = null;
        proxyPort_ = 0;
    }


    /**
     *  Create an instance that will use the specified proxy server
     *
     * @param  proxyHost The server that will act as proxy
     * @param  proxyPort The port to use on the proxy server
     * @param  webClient The web client that is using this connection
     */
    public WebConnection( final WebClient webClient, final String proxyHost, final int proxyPort ) {
        webClient_ = webClient;
        proxyHost_ = proxyHost;
        proxyPort_ = proxyPort;
    }

    /**
     *  Submit a request and retrieve a response
     *
     * @param  webRequestSettings Settings to make the request with
     * @return  See above
     * @exception  IOException If an IO error occurs
     */
    public abstract WebResponse getResponse(final WebRequestSettings webRequestSettings)
        throws IOException;

    /**
     *  Submit a request and retrieve a response
     *
     * @param  parameters Any parameters
     * @param  url The url of the server
     * @param  submitMethod The submit method. Ie SubmitMethod.GET
     * @param  requestHeaders Any headers that need to be put into the request.
     * @return  See above
     * @exception  IOException If an IO error occurs
     * @deprecated Use {@link #getResponse(WebRequestSettings)}
     */
    public WebResponse getResponse(
            final URL url,
            final SubmitMethod submitMethod,
            final List parameters,
            final Map requestHeaders )
        throws
            IOException {
        final WebRequestSettings wrs = new WebRequestSettings(url);
        wrs.setSubmitMethod(submitMethod);
        wrs.setRequestParameters(parameters);
        wrs.setAdditionalHeaders(requestHeaders);
        return getResponse(wrs);
    }

    /**
     *  Submit a request and retrieve a response
     *
     * @param  parameters Any parameters
     * @param  url The url of the server
     * @param  encType Encoding type of the form when done as a POST
     * @param  submitMethod The submit method. Ie SubmitMethod.GET
     * @param  requestHeaders Any headers that need to be put into the request.
     * @return  See above
     * @exception  IOException If an IO error occurs
     * @deprecated Use {@link #getResponse(WebRequestSettings)}
     */
    public WebResponse getResponse(
            final URL url,
            final FormEncodingType encType,
            final SubmitMethod submitMethod,
            final List parameters,
            final Map requestHeaders )
        throws
            IOException {
        final WebRequestSettings wrs = new WebRequestSettings(url);
        wrs.setEncodingType(encType);
        wrs.setSubmitMethod(submitMethod);
        wrs.setRequestParameters(parameters);
        wrs.setAdditionalHeaders(requestHeaders);
        return getResponse(wrs);
    }

    /**
     * Return the web client
     * @return The web client.
     */
    public final WebClient getWebClient() {
        return webClient_;
    }


    /**
     * Return the proxy host
     * @return The proxy host.
     */
    public final String getProxyHost() {
        return proxyHost_;
    }


    /**
     * Return the proxy port.
     * @return The proxy port.
     */
    public final int getProxyPort() {
        return proxyPort_;
    }


    /**
     * Return the {@link HttpState} that is being used for a given domain
     * @param url The url from which the domain will be determined
     * @return The state or null if no state can be found for this domain.
     */
    public abstract HttpState getStateForUrl( final URL url );
}

