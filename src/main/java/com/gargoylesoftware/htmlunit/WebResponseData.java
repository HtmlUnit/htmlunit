/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.brotli.dec.BrotliInputStream;

import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Simple data object to simplify WebResponse creation.
 *
 * @author Brad Clarke
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class WebResponseData implements Serializable {
    private static final Log LOG = LogFactory.getLog(WebResponseData.class);

    private final int statusCode_;
    private final String statusMessage_;
    private final List<NameValuePair> responseHeaders_;
    private final DownloadedContent downloadedContent_;

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
        this(new DownloadedContent.InMemory(body), statusCode, statusMessage, responseHeaders);
    }

    /**
     * Constructs without data stream for subclasses that override getBody().
     *
     * @param statusCode        Status code from the server
     * @param statusMessage     Status message from the server
     * @param responseHeaders   Headers in this response
     */
    protected WebResponseData(final int statusCode,
            final String statusMessage, final List<NameValuePair> responseHeaders) {
        this(ArrayUtils.EMPTY_BYTE_ARRAY, statusCode, statusMessage, responseHeaders);
    }

    /**
     * Constructor.
     * @param downloadedContent the downloaded content
     * @param statusCode        Status code from the server
     * @param statusMessage     Status message from the server
     * @param responseHeaders   Headers in this response
     */
    public WebResponseData(final DownloadedContent downloadedContent, final int statusCode, final String statusMessage,
            final List<NameValuePair> responseHeaders) {
        statusCode_ = statusCode;
        statusMessage_ = statusMessage;
        responseHeaders_ = Collections.unmodifiableList(responseHeaders);
        downloadedContent_ = downloadedContent;
    }

    private InputStream getStream(final DownloadedContent downloadedContent,
                final List<NameValuePair> headers, final ByteOrderMark[] bomHeaders) throws IOException {
        InputStream stream = downloadedContent_.getInputStream();
        if (downloadedContent.isEmpty()) {
            return stream;
        }

        final String encoding = getHeader(headers, "content-encoding");
        if (encoding != null) {
            boolean isGzip = StringUtils.contains(encoding, "gzip") && !"no-gzip".equals(encoding);
            if ("gzip-only-text/html".equals(encoding)) {
                isGzip = MimeType.TEXT_HTML.equals(getHeader(headers, "content-type"));
            }
            if (isGzip) {
                try {
                    stream = new GZIPInputStream(stream);
                }
                catch (final IOException e) {
                    LOG.error("Reading gzip encodec content failed.", e);
                    stream.close();
                    stream = IOUtils.toInputStream(
                                "<html>\n"
                                 + "<head><title>Problem loading page</title></head>\n"
                                 + "<body>\n"
                                 + "<h1>Content Encoding Error</h1>\n"
                                 + "<p>The page you are trying to view cannot be shown because"
                                 + " it uses an invalid or unsupported form of compression.</p>\n"
                                 + "</body>\n"
                                 + "</html>", ISO_8859_1);
                }
                return stream;
            }

            if ("br".equals(encoding)) {
                try {
                    stream = new BrotliInputStream(stream);
                }
                catch (final IOException e) {
                    LOG.error("Reading Brotli encodec content failed.", e);
                    stream.close();
                    stream = IOUtils.toInputStream(
                                "<html>\n"
                                 + "<head><title>Problem loading page</title></head>\n"
                                 + "<body>\n"
                                 + "<h1>Content Encoding Error</h1>\n"
                                 + "<p>The page you are trying to view cannot be shown because"
                                 + " it uses an invalid or unsupported form of compression.</p>\n"
                                 + "</body>\n"
                                 + "</html>", ISO_8859_1);
                }
                return stream;
            }

            if (StringUtils.contains(encoding, "deflate")) {
                boolean zlibHeader = false;
                if (stream.markSupported()) { // should be always the case as the content is in a byte[] or in a file
                    stream.mark(2);
                    final byte[] buffer = new byte[2];
                    final int byteCount = stream.read(buffer, 0, 2);
                    zlibHeader = byteCount == 2 && (((buffer[0] & 0xff) << 8) | (buffer[1] & 0xff)) == 0x789c;
                    stream.reset();
                }
                if (zlibHeader) {
                    stream = new InflaterInputStream(stream);
                }
                else {
                    stream = new InflaterInputStream(stream, new Inflater(true));
                }
                return stream;
            }
        }

        if (stream != null && bomHeaders != null) {
            stream = new BOMInputStream(stream, bomHeaders);
        }
        return stream;
    }

    private static String getHeader(final List<NameValuePair> headers, final String name) {
        for (final NameValuePair header : headers) {
            final String headerName = header.getName().trim();
            if (name.equalsIgnoreCase(headerName)) {
                return header.getValue();
            }
        }

        return null;
    }

    /**
     * Returns the response body.
     * This may cause memory problem for very large responses.
     * @return response body
     */
    public byte[] getBody() {
        try (InputStream is = getInputStream()) {
            return IOUtils.toByteArray(is);
        }
        catch (final IOException e) {
            throw new RuntimeException(e); // shouldn't we allow the method to throw IOException?
        }
    }

    /**
     * Returns a new {@link InputStream} allowing to read the downloaded content.
     * @return the associated InputStream
     * @throws IOException in case of IO problems
     */
    public InputStream getInputStream() throws IOException {
        return getStream(downloadedContent_, getResponseHeaders(), null);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param bomHeaders the supported bomHeaders
     * @return the associated InputStream wrapped with a bom input stream if applicable
     * @throws IOException in case of IO problems
     */
    public InputStream getInputStreamWithBomIfApplicable(final ByteOrderMark[] bomHeaders) throws IOException {
        return getStream(downloadedContent_, getResponseHeaders(), bomHeaders);
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
     * Returns length of the content data.
     * @return the length
     */
    public long getContentLength() {
        return downloadedContent_.length();
    }

    /**
     * Clean up the downloaded content.
     */
    public void cleanUp() {
        downloadedContent_.cleanUp();
    }
}
