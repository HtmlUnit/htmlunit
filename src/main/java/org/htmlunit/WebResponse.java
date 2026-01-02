/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit;

import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_16LE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.http.HttpStatus;
import org.htmlunit.util.EncodingSniffer;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.StringUtils;

/**
 * A response from a web server.
 *
 * @author Mike Bowler
 * @author Brad Clarke
 * @author Noboru Sinohara
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
public class WebResponse implements Serializable {

    private static final Log LOG = LogFactory.getLog(WebResponse.class);
    private static final ByteOrderMark[] BOM_HEADERS = {
        ByteOrderMark.UTF_8,
        ByteOrderMark.UTF_16LE,
        ByteOrderMark.UTF_16BE};

    private final long loadTime_;
    private final WebResponseData responseData_;
    private final WebRequest request_;
    private boolean wasContentCharsetTentative_;
    private boolean wasBlocked_;
    private String blockReason_;

    /**
     * Constructs with all data.
     *
     * @param responseData      Data that was send back
     * @param url               Where this response came from
     * @param requestMethod     the method used to get this response
     * @param loadTime          How long the response took to be sent
     */
    public WebResponse(final WebResponseData responseData, final URL url,
            final HttpMethod requestMethod, final long loadTime) {
        this(responseData, new WebRequest(url, requestMethod), loadTime);
    }

    /**
     * Constructs with all data.
     *
     * @param responseData      Data that was send back
     * @param request           the request used to get this response
     * @param loadTime          How long the response took to be sent
     */
    public WebResponse(final WebResponseData responseData,
            final WebRequest request, final long loadTime) {
        responseData_ = responseData;
        request_ = request;
        loadTime_ = loadTime;
    }

    /**
     * Returns the request used to load this response.
     * @return the request used to load this response
     */
    public WebRequest getWebRequest() {
        return request_;
    }

    /**
     * Returns the response headers as a list of {@link NameValuePair}s.
     * @return the response headers as a list of {@link NameValuePair}s
     */
    public List<NameValuePair> getResponseHeaders() {
        return responseData_.getResponseHeaders();
    }

    /**
     * Returns the value of the specified response header.
     * @param headerName the name of the header whose value is to be returned
     * @return the header value, {@code null} if no response header exists with this name
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
     * Returns the status code that was returned by the server.
     * @return the status code that was returned by the server
     */
    public int getStatusCode() {
        return responseData_.getStatusCode();
    }

    /**
     * Returns the status message that was returned from the server.
     * @return the status message that was returned from the server
     */
    public String getStatusMessage() {
        return responseData_.getStatusMessage();
    }

    /**
     * Returns the content type returned from the server, e.g. "text/html".
     * @return the content type returned from the server, e.g. "text/html"
     */
    public String getContentType() {
        final String contentTypeHeader = getResponseHeaderValue(HttpHeader.CONTENT_TYPE_LC);
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
     * Returns the content charset specified explicitly in the {@code Content-Type} header
     * or {@code null} if none was specified.
     * @return the content charset specified header or {@code null} if none was specified
     */
    public Charset getHeaderContentCharset() {
        final String contentType = getResponseHeaderValue(HttpHeader.CONTENT_TYPE_LC);
        if (contentType == null) {
            return null;
        }

        final int index = contentType.indexOf(';');
        if (index == -1 || index == 0) {
            return null;
        }
        if (StringUtils.isBlank(contentType.substring(0, index))) {
            return null;
        }

        return EncodingSniffer.extractEncodingFromContentType(contentType);
    }

    /**
     * Returns the content charset for this response, even if no charset was specified explicitly.
     * <p>
     * This method always returns a valid charset. This method first checks the {@code Content-Type}
     * header or in the content BOM for viable charset. If not found, it attempts to determine the
     * charset based on the type of the content. As a last resort, this method returns the
     * value of {@link org.htmlunit.WebRequest#getDefaultResponseContentCharset()} which is
     * {@link java.nio.charset.StandardCharsets#UTF_8} by default.
     * @return the content charset for this response
     */
    public Charset getContentCharset() {
        wasContentCharsetTentative_ = false;

        try (InputStream is = getContentAsStreamWithBomIfApplicable()) {
            if (is instanceof BOMInputStream stream) {
                final String bomCharsetName = stream.getBOMCharsetName();
                if (bomCharsetName != null) {
                    return Charset.forName(bomCharsetName);
                }
            }

            Charset charset = getHeaderContentCharset();
            if (charset != null) {
                return charset;
            }

            final String contentType = getContentType();
            switch (DefaultPageCreator.determinePageType(contentType)) {
                case HTML:
                    charset = EncodingSniffer.sniffEncodingFromMetaTag(is);
                    wasContentCharsetTentative_ = true;
                    break;
                case XML:
                    charset = EncodingSniffer.sniffEncodingFromXmlDeclaration(is);
                    if (charset == null) {
                        charset = UTF_8;
                    }
                    break;
                default:
                    if (MimeType.TEXT_CSS.equals(contentType)) {
                        charset = EncodingSniffer.sniffEncodingFromCssDeclaration(is);
                    }
                    break;
            }

            if (charset != null) {
                return charset;
            }
        }
        catch (final IOException e) {
            LOG.warn("Error trying to sniff encoding.", e);
            wasContentCharsetTentative_ = true;
        }
        return getWebRequest().getDefaultResponseContentCharset();
    }

    /**
     * Returns whether the charset of the previous call to {@link #getContentCharset()} was "tentative".
     * <p>
     * A charset is classed as "tentative" if its detection is prone to false positive/negatives.
     * <p>
     * For example, HTML meta-tag sniffing can be fooled by text that looks-like-a-meta-tag inside
     * JavaScript code (false positive) or if the meta-tag is after the first 1024 bytes (false negative).
     * @return {@code true} if the charset of the previous call to {@link #getContentCharset()} was
     *         "tentative".
     * @see <a href="https://html.spec.whatwg.org/multipage/parsing.html#concept-encoding-confidence">
     *     https://html.spec.whatwg.org/multipage/parsing.html#concept-encoding-confidence</a>
     */
    public boolean wasContentCharsetTentative() {
        return wasContentCharsetTentative_;
    }

    /**
     * Returns the response content as a string, using the charset/encoding specified in the server response.
     * @return the response content as a string, using the charset/encoding specified in the server response
     *         or null if the content retrieval was failing
     */
    public String getContentAsString() {
        return getContentAsString(getContentCharset());
    }

    /**
     * Returns the response content as a string, using the specified charset,
     * rather than the charset/encoding specified in the server response.
     * If there is a bom header the charset parameter will be overwritten by the bom.
     * @param encoding the charset/encoding to use to convert the response content into a string
     * @return the response content as a string or null if the content retrieval was failing
     */
    public String getContentAsString(final Charset encoding) {
        if (responseData_ != null) {
            try (InputStream in = responseData_.getInputStreamWithBomIfApplicable(BOM_HEADERS)) {
                if (in instanceof BOMInputStream bomIn) {
                    try () {
                        // there seems to be a bug in BOMInputStream
                        // we have to call this before hasBOM(ByteOrderMark)
                        if (bomIn.hasBOM()) {
                            if (bomIn.hasBOM(ByteOrderMark.UTF_8)) {
                                return IOUtils.toString(bomIn, UTF_8);
                            }
                            if (bomIn.hasBOM(ByteOrderMark.UTF_16BE)) {
                                return IOUtils.toString(bomIn, UTF_16BE);
                            }
                            if (bomIn.hasBOM(ByteOrderMark.UTF_16LE)) {
                                return IOUtils.toString(bomIn, UTF_16LE);
                            }
                        }
                        return IOUtils.toString(bomIn, encoding);
                    }
                }

                return IOUtils.toString(in, encoding);
            }
            catch (final IOException e) {
                LOG.warn(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * Returns length of the content data.
     * @return the length
     */
    public long getContentLength() {
        if (responseData_ == null) {
            return 0;
        }
        return responseData_.getContentLength();
    }

    /**
     * Returns the response content as an input stream.
     * @return the response content as an input stream
     * @throws IOException in case of IOProblems
     */
    public InputStream getContentAsStream() throws IOException {
        return responseData_.getInputStream();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @return the associated InputStream wrapped with a bom input stream if applicable
     * @throws IOException in case of IO problems
     */
    public InputStream getContentAsStreamWithBomIfApplicable() throws IOException {
        if (responseData_ != null) {
            return responseData_.getInputStreamWithBomIfApplicable(BOM_HEADERS);
        }
        return null;
    }

    /**
     * Returns the time it took to load this web response, in milliseconds.
     * @return the time it took to load this web response, in milliseconds
     */
    public long getLoadTime() {
        return loadTime_;
    }

    /**
     * Clean up the response data.
     */
    public void cleanUp() {
        if (responseData_ != null) {
            responseData_.cleanUp();
        }
    }

    /**
     * @return true if the 2xx
     */
    public boolean isSuccess() {
        final int statusCode = getStatusCode();
        return statusCode >= HttpStatus.OK_200 && statusCode < HttpStatus.MULTIPLE_CHOICES_300;
    }

    /**
     * @return true if the 2xx or 305
     */
    public boolean isSuccessOrUseProxy() {
        final int statusCode = getStatusCode();
        return (statusCode >= HttpStatus.OK_200 && statusCode < HttpStatus.MULTIPLE_CHOICES_300)
                || statusCode == HttpStatus.USE_PROXY_305;
    }

    /**
     * @return true if the 2xx or 305
     */
    public boolean isSuccessOrUseProxyOrNotModified() {
        final int statusCode = getStatusCode();
        return (statusCode >= HttpStatus.OK_200 && statusCode < HttpStatus.MULTIPLE_CHOICES_300)
                || statusCode == HttpStatus.USE_PROXY_305
                || statusCode == HttpStatus.NOT_MODIFIED_304;
    }

    /**
     * @return true if the request was blocked
     */
    public boolean wasBlocked() {
        return wasBlocked_;
    }

    /**
     * @return the reason for blocking or null
     */
    public String getBlockReason() {
        return blockReason_;
    }

    /**
     * Sets the wasBlocked state to true.
     *
     * @param blockReason the reason
     */
    public void markAsBlocked(final String blockReason) {
        wasBlocked_ = true;
        blockReason_ = blockReason;
    }
}
