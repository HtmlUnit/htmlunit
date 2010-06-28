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
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Simple data object to simplify WebResponse creation.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class WebResponseData implements Serializable {

    private static final long serialVersionUID = 2979956380280496543L;
    private static final int IN_MEMORY_SIZE = 300;

    private byte[] body_;
    private InputStream inputStream_;
    private boolean isBinary_;
    private int statusCode_;
    private String statusMessage_;
    private List<NameValuePair> responseHeaders_;

    /**
     * Constructs with a raw byte[] (mostly for testing).
     *
     * @param body              Body of this response
     * @param statusCode        Status code from the server
     * @param statusMessage     Status message from the server
     * @param responseHeaders   Headers in this response
     */
    public WebResponseData(final byte[] body, final int statusCode, final String statusMessage,
            final List<NameValuePair> responseHeaders) {
        statusCode_ = statusCode;
        statusMessage_ = statusMessage;
        responseHeaders_ = Collections.unmodifiableList(responseHeaders);

        if (body != null) {
            try {
                body_ = IOUtils.toByteArray(getStream(new ByteArrayInputStream(body), responseHeaders));
            }
            catch (final IOException e) {
                body_ = body;
            }
        }
    }

    /**
     * Constructs with a data stream to minimize copying of the entire body.
     *
     * @param bodyStream        Stream of this response's body
     * @param statusCode        Status code from the server
     * @param statusMessage     Status message from the server
     * @param responseHeaders   Headers in this response
     *
     * @throws IOException on stream errors
     */
    public WebResponseData(final InputStream bodyStream, final int statusCode,
            final String statusMessage, final List<NameValuePair> responseHeaders) throws IOException {
        statusCode_ = statusCode;
        statusMessage_ = statusMessage;
        responseHeaders_ = Collections.unmodifiableList(responseHeaders);
        if (bodyStream != null) {
            final MemoryInputStream memoryInputStream = new MemoryInputStream(
                    getStream(bodyStream, responseHeaders), IN_MEMORY_SIZE);
            if (memoryInputStream.isBinary()) {
                inputStream_ = memoryInputStream;
                isBinary_ = true;
            }
            else {
                body_ = IOUtils.toByteArray(memoryInputStream);
            }
        }
    }

    /**
     * Constructs without data stream for subclasses that override getBody().
     *
     * @param statusCode        Status code from the server
     * @param statusMessage     Status message from the server
     * @param responseHeaders   Headers in this response
     *
     * @throws IOException on stream errors
     */
    protected WebResponseData(final int statusCode,
            final String statusMessage, final List<NameValuePair> responseHeaders) throws IOException {
        statusCode_ = statusCode;
        statusMessage_ = statusMessage;
        responseHeaders_ = Collections.unmodifiableList(responseHeaders);
    }

    private InputStream getStream(InputStream stream, final List<NameValuePair> headers) throws IOException {
        if (stream == null) {
            return null;
        }
        String encoding = null;
        for (final NameValuePair header : headers) {
            final String headerName = header.getName().trim();
            if (headerName.equalsIgnoreCase("content-encoding")) {
                encoding = header.getValue();
                break;
            }
        }
        if (encoding != null && StringUtils.contains(encoding, "gzip")) {
            stream = new GZIPInputStream(stream);
        }
        else if (encoding != null && StringUtils.contains(encoding, "deflate")) {
            stream = new InflaterInputStream(stream);
        }
        return stream;
    }

    /**
     * Returns true for binary content, in which case you must use {@link #getInputStream()}.
     * @return true for binary content
     */
    public boolean isBinary() {
        return isBinary_;
    }

    /**
     * Returns the response body.
     * @return response body
     */
    public byte[] getBody() {
        if (isBinary()) {
            throw new IllegalStateException(
                "Can not call getBody() for binary content WebResponseData, use getInputStream()");
        }
        return body_;
    }

    /**
     * Returns the InputStream, if {@link #isBinary()} is true.
     * @return the associated InputStream
     */
    public InputStream getInputStream() {
        return inputStream_;
    }

    /**
     * @return response headers
     */
    public List<NameValuePair> getResponseHeaders() {
        return responseHeaders_;
    }

    /**
     * @return response status code
     */
    public int getStatusCode() {
        return statusCode_;
    }

    /**
     * @return response status message
     */
    public String getStatusMessage() {
        return statusMessage_;
    }
}
