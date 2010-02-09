/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Simple base class for {@link WebResponse}.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public class WebResponseImpl implements WebResponse {

    private static final long serialVersionUID = 2842434739251092348L;
    private static final Log LOG = LogFactory.getLog(WebResponseImpl.class);

    private long loadTime_;
    private WebResponseData responseData_;
    private WebRequestSettings requestSettings_;

    /**
     * Constructs with all data.
     *
     * @param responseData      Data that was send back
     * @param url               Where this response came from
     * @param requestMethod     the method used to get this response
     * @param loadTime          How long the response took to be sent
     */
    public WebResponseImpl(final WebResponseData responseData, final URL url,
            final HttpMethod requestMethod, final long loadTime) {
        this(responseData, new WebRequestSettings(url, requestMethod), loadTime);
    }

    /**
     * Constructs with all data.
     *
     * @param responseData      Data that was send back
     * @param requestSettings   the request settings used to get this response
     * @param loadTime          How long the response took to be sent
     */
    public WebResponseImpl(final WebResponseData responseData,
            final WebRequestSettings requestSettings, final long loadTime) {
        responseData_ = responseData;
        requestSettings_ = requestSettings;
        loadTime_ = loadTime;
    }

    /**
     * {@inheritDoc}
     */
    public WebRequestSettings getRequestSettings() {
        return requestSettings_;
    }

    /**
     * {@inheritDoc}
     */
    public List<NameValuePair> getResponseHeaders() {
        return responseData_.getResponseHeaders();
    }

    /**
     * {@inheritDoc}
     */
    public String getResponseHeaderValue(final String headerName) {
        for (final NameValuePair pair : responseData_.getResponseHeaders()) {
            if (pair.getName().equalsIgnoreCase(headerName)) {
                return pair.getValue();
            }
        }
        return null;
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
    public String getContentCharsetOrNull() {
        try {
            return EncodingSniffer.sniffEncoding(getResponseHeaders(), getContentAsStream());
        }
        catch (final IOException e) {
            LOG.warn("Error trying to sniff encoding.", e);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getContentCharset() {
        String charset = getContentCharsetOrNull();
        if (charset == null) {
            charset = getRequestSettings().getCharset();
        }
        if (charset == null) {
            charset = TextUtil.DEFAULT_CHARSET;
        }
        return charset;
    }

    /**
     * {@inheritDoc}
     */
    public String getContentAsString() {
        return getContentAsString(getContentCharset());
    }

    /**
     * {@inheritDoc}
     */
    public String getContentAsString(final String encoding) {
        final byte[] body = responseData_.getBody();
        if (body != null) {
            try {
                return new String(body, encoding);
            }
            catch (final UnsupportedEncodingException e) {
                LOG.warn("Attempted to use unsupported encoding '" + encoding + "'; using default system encoding.");
                return new String(body);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getContentAsStream() throws IOException {
        final byte[] body = responseData_.getBody();
        if (body != null) {
            return new ByteArrayInputStream(body);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getContentAsBytes() {
        return responseData_.getBody();
    }

    /**
     * {@inheritDoc}
     */
    public long getLoadTime() {
        return loadTime_;
    }

    /**
     * {@inheritDoc}
     */
    public URL getRequestUrl() {
        return getRequestSettings().getUrl();
    }
}
