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
    private static final long BIG_CONTENT_SIZE_ = 50 * 1024 * 1024;

    private byte[] body_;
    private InputStream inputStream_;
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
                body_ = getBody(new ByteArrayInputStream(body), responseHeaders);
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
        if (isBigContent()) {
            String encoding = null;
            for (final NameValuePair header : responseHeaders) {
                final String headerName = header.getName().trim();
                if (headerName.equalsIgnoreCase("content-encoding")) {
                    encoding = header.getValue();
                    break;
                }
            }
            if (encoding != null && StringUtils.contains(encoding, "gzip")) {
                inputStream_ = new GZIPInputStream(bodyStream);
            }
            else if (encoding != null && StringUtils.contains(encoding, "deflate")) {
                inputStream_ = new InflaterInputStream(bodyStream);
            }
            else {
                inputStream_ = bodyStream;
            }
        }
        else {
            body_ = getBody(bodyStream, responseHeaders);
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

    /**
     * Returns the body byte array contained by the specified input stream.
     * If the response headers indicate that the data has been compressed,
     * the data stream is handled appropriately. If the specified stream is
     * <tt>null</tt>, this method returns <tt>null</tt>.
     * @param stream the input stream which contains the body
     * @param headers the response headers
     * @return the specified body stream, as a byte array
     * @throws IOException if a stream error occurs
     */
    protected byte[] getBody(InputStream stream, final List<NameValuePair> headers) throws IOException {
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
        return IOUtils.toByteArray(stream);
    }

    /**
     * Returns the response body.
     * @return response body
     */
    public byte[] getBody() {
        if (isBigContent()) {
            throw new IllegalStateException(
                "Can not call getBody() for big content WebResponseData, use getInputStream()");
        }
        return body_;
    }

    /**
     * Returns the InputStream, if {@link #isBigContent()} is true.
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

    /**
     * Returns true if this is a big content data.
     * @return true if the content size is big
     */
    public boolean isBigContent() {
        return isBigContent(responseHeaders_);
    }

    static boolean isBigContent(final List<NameValuePair> headers) {
        for (final NameValuePair header : headers) {
            final String headerName = header.getName().trim();
            if (headerName.equalsIgnoreCase("Content-Length")
                        && Long.parseLong(header.getValue()) >= BIG_CONTENT_SIZE_) {
                return true;
            }
        }
        return false;
    }
}
