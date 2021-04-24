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

import com.gargoylesoftware.htmlunit.DefaultPageCreator.PageType;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * A response from a web server.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Brad Clarke
 * @author Noboru Sinohara
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class WebResponse implements Serializable {

    private static final Log LOG = LogFactory.getLog(WebResponse.class);
    private static final ByteOrderMark[] BOM_HEADERS = {
        ByteOrderMark.UTF_8,
        ByteOrderMark.UTF_16LE,
        ByteOrderMark.UTF_16BE};

    private long loadTime_;
    private WebResponseData responseData_;
    private WebRequest request_;
    private boolean defaultCharsetUtf8_;

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
     * Returns the content charset specified explicitly in the header or in the content,
     * or {@code null} if none was specified.
     * @return the content charset specified explicitly in the header or in the content,
     *         or {@code null} if none was specified
     */
    public Charset getContentCharsetOrNull() {
        try (InputStream is = getContentAsStream()) {
            return EncodingSniffer.sniffEncoding(getResponseHeaders(), is);
        }
        catch (final IOException e) {
            LOG.warn("Error trying to sniff encoding.", e);
            return null;
        }
    }

    /**
     * Returns the content charset for this response, even if no charset was specified explicitly.
     * This method always returns a valid charset. This method first checks the {@code Content-Type}
     * header; if not found, it checks the request charset; as a last resort, this method
     * returns {@link java.nio.charset.StandardCharsets#ISO_8859_1}.
     * If no charset is defined for an xml response, then UTF-8 is used
     * @see <a href="http://www.w3.org/TR/xml/#charencoding">Character Encoding</a>
     * @return the content charset for this response
     */
    public Charset getContentCharset() {
        Charset charset = getContentCharsetOrNull();
        if (charset == null) {
            final String contentType = getContentType();

            // xml pages are using a different content type
            if (null != contentType
                && (defaultCharsetUtf8_
                    || PageType.XML == DefaultPageCreator.determinePageType(contentType))) {
                return UTF_8;
            }
        }

        if (charset == null) {
            charset = ISO_8859_1;
        }
        return charset;
    }

    /**
     * Returns the response content as a string, using the charset/encoding specified in the server response.
     * @return the response content as a string, using the charset/encoding specified in the server response
     * or null if the content retrieval was failing
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
        return getContentAsString(encoding, false);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns the response content as a string, using the specified charset,
     * rather than the charset/encoding specified in the server response.
     * If there is a bom header the charset parameter will be overwritten by the bom.
     * @param encoding the charset/encoding to use to convert the response content into a string
     * @param ignoreUtf8Bom if true utf8 bom header will be ignored
     * @return the response content as a string or null if the content retrieval was failing
     */
    public String getContentAsString(final Charset encoding, final boolean ignoreUtf8Bom) {
        if (responseData_ != null) {
            try (InputStream in = responseData_.getInputStreamWithBomIfApplicable(BOM_HEADERS)) {
                if (in instanceof BOMInputStream) {
                    try (BOMInputStream bomIn = (BOMInputStream) in) {
                        // there seems to be a bug in BOMInputStream
                        // we have to call this before hasBOM(ByteOrderMark)
                        if (bomIn.hasBOM()) {
                            if (!ignoreUtf8Bom && bomIn.hasBOM(ByteOrderMark.UTF_8)) {
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
     * Mark this response for using UTF-8 as default charset.
     */
    public void defaultCharsetUtf8() {
        defaultCharsetUtf8_ = true;
    }
}
