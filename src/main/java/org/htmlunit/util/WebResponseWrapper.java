/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;

/**
 * Provides a convenient implementation of the {@link WebResponse} interface that can be subclassed
 * by developers wishing to adapt a particular WebResponse.
 * This class implements the Wrapper or Decorator pattern. Methods default to calling through to the wrapped
 * web response object.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 */
public class WebResponseWrapper extends WebResponse {

    private final WebResponse wrappedWebResponse_;

    /**
     * Constructs a WebResponse object wrapping provided WebResponse.
     * @param webResponse the webResponse that does the real work
     * @throws IllegalArgumentException if the webResponse is {@code null}
     */
    public WebResponseWrapper(final WebResponse webResponse) throws IllegalArgumentException {
        super(null, null, 0);
        if (webResponse == null) {
            throw new IllegalArgumentException("Wrapped WebResponse can't be null");
        }
        wrappedWebResponse_ = webResponse;
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentLength() on the wrapped webResponse object.
     */
    @Override
    public long getContentLength() {
        return wrappedWebResponse_.getContentLength();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentAsStream() on the wrapped webResponse object.
     */
    @Override
    public InputStream getContentAsStream() throws IOException {
        return wrappedWebResponse_.getContentAsStream();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentAsString() on the wrapped webResponse object.
     */
    @Override
    public String getContentAsString() {
        return wrappedWebResponse_.getContentAsString(getContentCharset());
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentAsString(Charset) on the wrapped webResponse object.
     */
    @Override
    public String getContentAsString(final Charset encoding) {
        return wrappedWebResponse_.getContentAsString(encoding);
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentAsString(Charset, boolean)
     * on the wrapped webResponse object.
     */
    @Override
    public String getContentAsString(final Charset encoding, final boolean ignoreUtf8Bom) {
        return wrappedWebResponse_.getContentAsString(encoding, ignoreUtf8Bom);
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getHeaderContentCharset() on the wrapped webResponse object.
     */
    @Override
    public Charset getHeaderContentCharset() {
        return wrappedWebResponse_.getHeaderContentCharset();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentCharsetOrNull() on the wrapped webResponse object.
     */
    @Override
    @Deprecated
    public Charset getContentCharsetOrNull() {
        return wrappedWebResponse_.getContentCharsetOrNull();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentCharset() on the wrapped webResponse object.
     */
    @Override
    public Charset getContentCharset() {
        return wrappedWebResponse_.getContentCharset();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentType() on the wrapped webResponse object.
     */
    @Override
    public String getContentType() {
        return wrappedWebResponse_.getContentType();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getLoadTime() on the wrapped webResponse object.
     */
    @Override
    public long getLoadTime() {
        return wrappedWebResponse_.getLoadTime();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getResponseHeaders() on the wrapped webResponse object.
     */
    @Override
    public List<NameValuePair> getResponseHeaders() {
        return wrappedWebResponse_.getResponseHeaders();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getResponseHeaderValue() on the wrapped webResponse object.
     */
    @Override
    public String getResponseHeaderValue(final String headerName) {
        return wrappedWebResponse_.getResponseHeaderValue(headerName);
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getStatusCode() on the wrapped webResponse object.
     */
    @Override
    public int getStatusCode() {
        return wrappedWebResponse_.getStatusCode();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getStatusMessage() on the wrapped webResponse object.
     */
    @Override
    public String getStatusMessage() {
        return wrappedWebResponse_.getStatusMessage();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getWebRequest() on the wrapped webResponse object.
     */
    @Override
    public WebRequest getWebRequest() {
        return wrappedWebResponse_.getWebRequest();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to call cleanUp() on the wrapped webResponse object.
     */
    @Override
    public void cleanUp() {
        wrappedWebResponse_.cleanUp();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to call defaultCharsetUtf8() on the wrapped webResponse object.
     */
    @Deprecated
    @Override
    public void defaultCharsetUtf8() {
        wrappedWebResponse_.defaultCharsetUtf8();
    }

    @Override
    public InputStream getContentAsStreamWithBomIfApplicable() throws IOException {
        return wrappedWebResponse_.getContentAsStreamWithBomIfApplicable();
    }

    @Override
    public boolean isSuccess() {
        return wrappedWebResponse_.isSuccess();
    }

    @Override
    public boolean isSuccessOrUseProxy() {
        return wrappedWebResponse_.isSuccessOrUseProxy();
    }

    @Override
    public boolean isSuccessOrUseProxyOrNotModified() {
        return wrappedWebResponse_.isSuccessOrUseProxyOrNotModified();
    }
}
