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
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;

import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Provides a convenient implementation of the {@link WebResponse} interface that can be subclassed
 * by developers wishing to adapt a particular WebResponse.
 * This class implements the Wrapper or Decorator pattern. Methods default to calling through to the wrapped
 * web connection object.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class WebResponseWrapper implements WebResponse {
    private final WebResponse wrappedWebResponse_;

    /**
     * Constructs a WebResponse object wrapping provided WebResponse.
     * @param webResponse the webResponse that does the real work
     * @throws IllegalArgumentException if the connection is <code>null</code>
     */
    public WebResponseWrapper(final WebResponse webResponse) throws IllegalArgumentException {
        if (webResponse == null) {
            throw new IllegalArgumentException("Wrapped WebResponse can't be null");
        }
        wrappedWebResponse_ = webResponse;
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentAsStream() on the wrapped connection object.
     */
    public InputStream getContentAsStream() throws IOException {
        return wrappedWebResponse_.getContentAsStream();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentAsString() on the wrapped connection object.
     */
    public String getContentAsString() {
        return wrappedWebResponse_.getContentAsString();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentCharSet() on the wrapped connection object.
     */
    public String getContentCharSet() {
        return wrappedWebResponse_.getContentCharSet();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentType() on the wrapped connection object.
     */
    public String getContentType() {
        return wrappedWebResponse_.getContentType();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getLoadTimeInMilliSeconds() on the wrapped connection object.
     */
    public long getLoadTimeInMilliSeconds() {
        return wrappedWebResponse_.getLoadTimeInMilliSeconds();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getRequestMethod() on the wrapped connection object.
     */
    public SubmitMethod getRequestMethod() {
        return wrappedWebResponse_.getRequestMethod();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getResponseBody() on the wrapped connection object.
     */
    public byte[] getResponseBody() {
        return wrappedWebResponse_.getResponseBody();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getResponseHeaders() on the wrapped connection object.
     */
    public List<NameValuePair> getResponseHeaders() {
        return wrappedWebResponse_.getResponseHeaders();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getResponseHeaderValue() on the wrapped connection object.
     */
    public String getResponseHeaderValue(final String headerName) {
        return wrappedWebResponse_.getResponseHeaderValue(headerName);
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getStatusCode() on the wrapped connection object.
     */
    public int getStatusCode() {
        return wrappedWebResponse_.getStatusCode();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getStatusMessage() on the wrapped connection object.
     */
    public String getStatusMessage() {
        return wrappedWebResponse_.getStatusMessage();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getUrl() on the wrapped connection object.
     */
    public URL getUrl() {
        return wrappedWebResponse_.getUrl();
    }

}
