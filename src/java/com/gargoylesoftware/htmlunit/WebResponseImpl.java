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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;

/**
 * Simple base class for WebResponse
 * 
 * @version  $Revision$
 * @author Brad Clarke
 */
public class WebResponseImpl implements WebResponse {
    private URL url_;
    private SubmitMethod requestMethod_;
    private long loadTime_;

    private WebResponseData responseData_;

    /**
     * Construct with all data
     * 
     * @param responseData      Data that was send back
     * @param url               Where this response came from
     * @param requestMethod     The method used to get this response
     * @param loadTime          How long the response took to be sent
     */
    public WebResponseImpl(final WebResponseData responseData, final URL url,
            final SubmitMethod requestMethod, final long loadTime) {
        responseData_ = responseData;
        url_ = url;
        requestMethod_ = requestMethod;
        loadTime_ = loadTime;
    }

    /**
     * {@inheritDoc}
     */
    public int getStatusCode() {
        return responseData_.getStatusCode();
    }

    /**
     * {@inheritDoc}
     */
    public String getStatusMessage() {
        return responseData_.getStatusMessage();
    }

    /**
     * {@inheritDoc}
     */
    public String getContentType() {
        final String contentTypeHeader = getResponseHeaderValue("content-type");
        if (contentTypeHeader == null) {
            // Not technically legal but some servers don't return a content-type
            return "";
        }
        final int index = contentTypeHeader.indexOf(';');
        if (index == -1) {
            return contentTypeHeader;
        }
        return contentTypeHeader.substring(0, index);
    }

    /**
     * {@inheritDoc}
     */
    public String getContentAsString() {
        try {
            return new String(responseData_.getBody(), getContentCharSet());
        }
        catch (final UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getContentAsStream() throws IOException {
        return new ByteArrayInputStream(responseData_.getBody());
    }

    /**
     * {@inheritDoc}
     */
    public URL getUrl() {
        return url_;
    }

    /**
     * {@inheritDoc}
     */
    public SubmitMethod getRequestMethod() {
        return requestMethod_;
    }

    /**
     * {@inheritDoc}
     */
    public List getResponseHeaders() {
        return responseData_.getResponseHeaders();
    }

    /**
     * {@inheritDoc}
     */
    public String getResponseHeaderValue(final String headerName) {
        final Iterator iterator = responseData_.getResponseHeaders().iterator();
        while (iterator.hasNext()) {
            final NameValuePair pair = (NameValuePair) iterator.next();
            if (pair.getName().equalsIgnoreCase(headerName)) {
                return pair.getValue();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public long getLoadTimeInMilliSeconds() {
        return loadTime_;
    }

    /**
     * {@inheritDoc}
     * This redoes some of the work that HttpMethodBase will do for use but
     * we must redo it for MockWebConnection to be consistent.
     */
    public String getContentCharSet() {
        final String contentTypeHeader = getResponseHeaderValue("content-type");
        if (contentTypeHeader == null) {
            // Not technically legal but some servers don't return a content-type
            return "ISO-8859-1";
        }
        final String prefix = "charset=";
        final int index = contentTypeHeader.indexOf(prefix);
        if (index == -1) {
            return "ISO-8859-1";
        }
        return contentTypeHeader.substring(index + prefix.length());
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getResponseBody() {
        return responseData_.getBody();
    }
}
