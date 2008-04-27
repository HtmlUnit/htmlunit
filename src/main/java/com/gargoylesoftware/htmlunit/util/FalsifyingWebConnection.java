/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.util;

import java.io.IOException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.WebResponseImpl;

/**
 * Extension of {@link WebConnectionWrapper} providing facility methods to deliver something else than
 * what the wrapped connection would deliver.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public abstract class FalsifyingWebConnection extends WebConnectionWrapper {

    /**
     * Constructs a WebConnection object wrapping provided WebConnection.
     * @param webConnection the webConnection that does the real work
     * @throws IllegalArgumentException if the connection is <code>null</code>
     */
    public FalsifyingWebConnection(final WebConnection webConnection) throws IllegalArgumentException {
        super(webConnection);
    }

    /**
     * Constructs an instance and places itself as connection of the WebClient.
     * @param webClient the WebClient which WebConnection should be wrapped
     * @throws IllegalArgumentException if the WebClient is <code>null</code>
     */
    public FalsifyingWebConnection(final WebClient webClient) throws IllegalArgumentException {
        super(webClient);
    }

    /**
     * Delivers the content for an alternate URL as if it comes from the requested URL.
     * @param webRequestSettings the original web request settings
     * @param url the URL from which the content should be retrieved
     * @return the response
     * @throws IOException if a problem occurred
     */
    protected WebResponse deliverFromAlternateUrl(final WebRequestSettings webRequestSettings, final URL url)
        throws IOException {
        final URL originalUrl = webRequestSettings.getUrl();
        webRequestSettings.setUrl(url);
        final WebResponse resp = super.getResponse(webRequestSettings);
        return new WebResponseWrapper(resp) {
            @Override
            public URL getUrl() {
                return originalUrl;
            }
        };
    }

    /**
     * Builds a WebResponse with new content, preserving all other information.
     * @param webResponse the web response to adapt
     * @param newContent the new content to place in the response
     * @return a web response with the new content
     * @throws IOException if an encoding problem occurred
     */
    protected WebResponse replaceContent(final WebResponse webResponse, final String newContent) throws IOException {
        final byte[] body = newContent.getBytes(webResponse.getContentCharSet());
        final WebResponseData wrd = new WebResponseData(body, webResponse.getStatusCode(),
                webResponse.getStatusMessage(), webResponse.getResponseHeaders());
        return new WebResponseImpl(wrd, webResponse.getUrl(), webResponse.getRequestMethod(),
                webResponse.getLoadTimeInMilliSeconds());
    }
}
