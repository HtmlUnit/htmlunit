/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.util.ArrayUtils;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;

/**
 * A fake {@link WebConnection} designed to mock out the actual HTTP connections.
 *
 * @author Mike Bowler
 * @author Noboru Sinohara
 * @author Marc Guillemot
 * @author Brad Clarke
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class MockWebConnection implements WebConnection {

    private static final Log LOG = LogFactory.getLog(MockWebConnection.class);

    private final Map<String, IOException> throwableMap_ = new HashMap<>();
    private final Map<String, RawResponseData> responseMap_ = new HashMap<>();
    private RawResponseData defaultResponse_;
    private volatile WebRequest lastRequest_;
    private final AtomicInteger requestCount_ = new AtomicInteger();
    private final List<URL> requestedUrls_ = Collections.synchronizedList(new ArrayList<>());

    /**
     * Contains the raw data configured for a response.
     */
    public static class RawResponseData {
        private final List<NameValuePair> headers_;
        private final byte[] byteContent_;
        private final String stringContent_;
        private final int statusCode_;
        private final String statusMessage_;
        private final Charset charset_;

        RawResponseData(final byte[] byteContent, final int statusCode, final String statusMessage,
                final String contentType, final List<NameValuePair> headers) {
            byteContent_ = byteContent;
            stringContent_ = null;
            charset_ = null;
            statusCode_ = statusCode;
            statusMessage_ = statusMessage;
            headers_ = compileHeaders(headers, contentType);
        }

        RawResponseData(final String stringContent, final Charset charset, final int statusCode,
                final String statusMessage, final String contentType, final List<NameValuePair> headers) {
            byteContent_ = null;
            charset_ = charset;
            stringContent_ = stringContent;
            statusCode_ = statusCode;
            statusMessage_ = statusMessage;
            headers_ = compileHeaders(headers, contentType);
        }

        private static List<NameValuePair> compileHeaders(final List<NameValuePair> headers, final String contentType) {
            final List<NameValuePair> compiledHeaders = new ArrayList<>();
            if (headers != null) {
                compiledHeaders.addAll(headers);
            }
            if (contentType != null) {
                compiledHeaders.add(new NameValuePair(HttpHeader.CONTENT_TYPE, contentType));
            }
            return compiledHeaders;
        }

        WebResponseData asWebResponseData() {
            final byte[] content;
            if (byteContent_ != null) {
                content = byteContent_;
            }
            else if (stringContent_ == null) {
                content = ArrayUtils.EMPTY_BYTE_ARRAY;
            }
            else {
                content = stringContent_.getBytes(charset_);
            }
            return new WebResponseData(content, statusCode_, statusMessage_, headers_);
        }

        /**
         * Returns the configured response headers.
         *
         * @return the headers
         */
        public List<NameValuePair> getHeaders() {
            return headers_;
        }

        /**
         * Returns the configured response content as a byte array.
         *
         * @return the byte content, or {@code null} if string content was configured
         */
        public byte[] getByteContent() {
            return byteContent_;
        }

        /**
         * Returns the configured response content as a string.
         *
         * @return the string content, or {@code null} if byte content was configured
         */
        public String getStringContent() {
            return stringContent_;
        }

        /**
         * Returns the configured HTTP status code.
         *
         * @return the status code
         */
        public int getStatusCode() {
            return statusCode_;
        }

        /**
         * Returns the configured HTTP status message.
         *
         * @return the status message
         */
        public String getStatusMessage() {
            return statusMessage_;
        }

        /**
         * Returns the configured charset, or {@code null} if byte content was configured.
         *
         * @return the charset, or {@code null} for byte content
         */
        public Charset getCharset() {
            return charset_;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebResponse getResponse(final WebRequest request) throws IOException {
        final RawResponseData rawResponse = getRawResponse(request);
        return new WebResponse(rawResponse.asWebResponseData(), request, 0);
    }

    /**
     * Returns the raw response configured for the given request.
     *
     * <p>The request is always recorded (incrementing {@link #getRequestCount()} and
     * appending to {@link #getRequestedUrls()}) before any configured {@link IOException}
     * is thrown, mirroring real HTTP behaviour where the request was dispatched even
     * if the connection subsequently failed.
     * </p>
     *
     * <p>URL lookup first tries an exact match (including query string), then retries
     * without the query string, then falls back to the default response. If no default
     * has been set an {@link IllegalStateException} is thrown.
     * </p>
     *
     * @param request the request
     * @return the raw response
     * @throws IOException if an {@link IOException} has been registered for the URL
     *         via {@link #setThrowable(URL, IOException)}
     * @throws IllegalStateException if no response or default response is configured
     *         for the URL
     */
    public RawResponseData getRawResponse(final WebRequest request) throws IOException {
        final URL url = request.getUrl();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting response for " + url.toExternalForm());
        }

        lastRequest_ = request;
        requestCount_.incrementAndGet();
        requestedUrls_.add(url);

        String urlString = url.toExternalForm();
        final IOException throwable = throwableMap_.get(urlString);
        if (throwable != null) {
            // wrap to produce a stack trace pointing to this call site rather than
            // to where the IOException was originally constructed
            throw new IOException(throwable.getMessage(), throwable);
        }

        RawResponseData rawResponse = responseMap_.get(urlString);
        if (rawResponse == null) {
            // try to find without query params
            final int queryStart = urlString.lastIndexOf('?');
            if (queryStart > -1) {
                urlString = urlString.substring(0, queryStart);
                rawResponse = responseMap_.get(urlString);
            }

            // fall back to default
            if (rawResponse == null) {
                rawResponse = defaultResponse_;
                if (rawResponse == null) {
                    throw new IllegalStateException("No response specified that can handle URL "
                         + request.getHttpMethod()
                         + " [" + urlString + "]");
                }
            }
        }

        return rawResponse;
    }

    /**
     * Returns an unmodifiable list of all URLs requested so far, in request order.
     *
     * @return the list of requested URLs
     */
    public List<URL> getRequestedUrls() {
        return Collections.unmodifiableList(requestedUrls_);
    }

    /**
     * Returns an unmodifiable list of requested URLs relativized against the given base URL.
     * If a requested URL starts with {@code relativeTo}, the base is stripped; otherwise
     * the full URL string is returned as-is.
     *
     * <p>Note: the base URL string is compared as a plain prefix. Ensure {@code relativeTo}
     * has a trailing slash if needed to avoid unintended partial matches
     * (e.g. {@code http://localhost/} rather than {@code http://localhost}).
     * </p>
     *
     * @param relativeTo the base URL whose prefix should be stripped from each requested URL
     * @return the list of relative (or absolute, if not matching) URL strings
     */
    public List<String> getRequestedUrls(final URL relativeTo) {
        final String baseUrl = relativeTo.toString();
        final List<String> response = new ArrayList<>();
        for (final URL url : requestedUrls_) {
            String s = url.toString();
            if (s.startsWith(baseUrl)) {
                s = s.substring(baseUrl.length());
            }
            response.add(s);
        }

        return Collections.unmodifiableList(response);
    }

    /**
     * Returns the HTTP method that was used in the last call to
     * {@link #getResponse(WebRequest)}.
     *
     * @return the HTTP method of the last request
     * @throws IllegalStateException if no request has been made yet
     */
    public HttpMethod getLastMethod() {
        return getLastWebRequest().getHttpMethod();
    }

    /**
     * Returns the request parameters that were used in the last call to
     * {@link #getResponse(WebRequest)}.
     *
     * @return the parameters of the last request
     * @throws IllegalStateException if no request has been made yet
     */
    public List<NameValuePair> getLastParameters() {
        return getLastWebRequest().getRequestParameters();
    }

    /**
     * Sets the response that will be returned when the specified URL is requested.
     *
     * @param url the URL that will return the given response
     * @param content the content to return
     * @param statusCode the status code to return
     * @param statusMessage the status message to return
     * @param contentType the content type to return
     * @param headers the response headers to return
     */
    public void setResponse(final URL url, final String content, final int statusCode,
            final String statusMessage, final String contentType,
            final List<NameValuePair> headers) {

        setResponse(
                url,
                content,
                statusCode,
                statusMessage,
                contentType,
                ISO_8859_1,
                headers);
    }

    /**
     * Sets the response that will be returned when the specified URL is requested.
     *
     * @param url the URL that will return the given response
     * @param content the content to return
     * @param statusCode the status code to return
     * @param statusMessage the status message to return
     * @param contentType the content type to return
     * @param charset the charset
     * @param headers the response headers to return
     */
    public void setResponse(final URL url, final String content, final int statusCode,
            final String statusMessage, final String contentType, final Charset charset,
            final List<NameValuePair> headers) {

        final RawResponseData responseEntry = buildRawResponseData(content, charset, statusCode, statusMessage,
                contentType, headers);
        responseMap_.put(url.toExternalForm(), responseEntry);
    }

    /**
     * Sets the exception that will be thrown when the specified URL is requested.
     *
     * <p>The stored exception is wrapped at throw time so that the stack trace
     * points to the actual call site rather than to where the exception was constructed.
     * </p>
     *
     * @param url the URL that will force the exception
     * @param throwable the {@link IOException} to throw
     */
    public void setThrowable(final URL url, final IOException throwable) {
        throwableMap_.put(url.toExternalForm(), throwable);
    }

    /**
     * Sets the response that will be returned when the specified URL is requested.
     *
     * @param url the URL that will return the given response
     * @param content the content to return
     * @param statusCode the status code to return
     * @param statusMessage the status message to return
     * @param contentType the content type to return
     * @param headers the response headers to return
     */
    public void setResponse(final URL url, final byte[] content, final int statusCode,
            final String statusMessage, final String contentType,
            final List<NameValuePair> headers) {

        final RawResponseData responseEntry = buildRawResponseData(content, statusCode, statusMessage, contentType,
            headers);
        responseMap_.put(url.toExternalForm(), responseEntry);
    }

    private static RawResponseData buildRawResponseData(final byte[] content, final int statusCode,
            final String statusMessage, final String contentType, final List<NameValuePair> headers) {
        return new RawResponseData(content, statusCode, statusMessage, contentType, headers);
    }

    private static RawResponseData buildRawResponseData(final String content, Charset charset, final int statusCode,
            final String statusMessage, final String contentType, final List<NameValuePair> headers) {

        if (charset == null) {
            charset = ISO_8859_1;
        }
        return new RawResponseData(content, charset, statusCode, statusMessage, contentType, headers);
    }

    /**
     * Convenient method that is the same as calling
     * {@link #setResponse(URL,String,int,String,String,List)} with a status
     * of "200 OK", a content type of "text/html" and no additional headers.
     *
     * @param url the URL that will return the given response
     * @param content the content to return
     */
    public void setResponse(final URL url, final String content) {
        setResponse(url, content, 200, "OK", MimeType.TEXT_HTML, null);
    }

    /**
     * Convenient method that is the same as calling
     * {@link #setResponse(URL,String,int,String,String,List)} with a status
     * of "200 OK" and no additional headers.
     *
     * @param url the URL that will return the given response
     * @param content the content to return
     * @param contentType the content type to return
     */
    public void setResponse(final URL url, final String content, final String contentType) {
        setResponse(url, content, 200, "OK", contentType, null);
    }

    /**
     * Convenient method that is the same as calling
     * {@link #setResponse(URL, String, int, String, String, Charset, List)} with a status
     * of "200 OK" and no additional headers.
     *
     * @param url the URL that will return the given response
     * @param content the content to return
     * @param contentType the content type to return
     * @param charset the charset
     */
    public void setResponse(final URL url, final String content, final String contentType, final Charset charset) {
        setResponse(url, content, 200, "OK", contentType, charset, null);
    }

    /**
     * Specify a generic HTML page that will be returned when the given URL is specified.
     * The page will contain only minimal HTML to satisfy the HTML parser but will contain
     * the specified title so that tests can check for titleText.
     *
     * @param url the URL that will return the given response
     * @param title the title of the page
     */
    public void setResponseAsGenericHtml(final URL url, final String title) {
        final String content = "<!DOCTYPE html><html><head><title>" + title + "</title></head><body></body></html>";
        setResponse(url, content);
    }

    /**
     * Sets the response that will be returned when a URL is requested that does
     * not have a specific response configured for it.
     *
     * @param content the content to return
     * @param statusCode the status code to return
     * @param statusMessage the status message to return
     * @param contentType the content type to return
     */
    public void setDefaultResponse(final String content, final int statusCode,
            final String statusMessage, final String contentType) {

        defaultResponse_ = buildRawResponseData(content, null, statusCode, statusMessage, contentType, null);
    }

    /**
     * Sets the response that will be returned when a URL is requested that does
     * not have a specific response configured for it.
     *
     * @param content the content to return
     * @param statusCode the status code to return
     * @param statusMessage the status message to return
     * @param contentType the content type to return
     */
    public void setDefaultResponse(final byte[] content, final int statusCode,
            final String statusMessage, final String contentType) {

        defaultResponse_ = buildRawResponseData(content, statusCode, statusMessage, contentType, null);
    }

    /**
     * Sets the response that will be returned when a URL is requested that does
     * not have a specific response configured for it.
     *
     * @param content the content to return
     */
    public void setDefaultResponse(final String content) {
        setDefaultResponse(content, 200, "OK", MimeType.TEXT_HTML);
    }

    /**
     * Sets the response that will be returned when a URL is requested that does
     * not have a specific response configured for it.
     *
     * @param content the content to return
     * @param contentType the content type to return
     */
    public void setDefaultResponse(final String content, final String contentType) {
        setDefaultResponse(content, 200, "OK", contentType, null);
    }

    /**
     * Sets the response that will be returned when a URL is requested that does
     * not have a specific response configured for it.
     *
     * @param content the content to return
     * @param contentType the content type to return
     * @param charset the charset
     */
    public void setDefaultResponse(final String content, final String contentType, final Charset charset) {
        setDefaultResponse(content, 200, "OK", contentType, charset, null);
    }

    /**
     * Sets the response that will be returned when a URL is requested that does
     * not have a specific response configured for it.
     *
     * @param content the content to return
     * @param statusCode the status code to return
     * @param statusMessage the status message to return
     * @param contentType the content type to return
     * @param headers the response headers to return
     */
    public void setDefaultResponse(final String content, final int statusCode,
            final String statusMessage, final String contentType,
            final List<NameValuePair> headers) {

        defaultResponse_ = buildRawResponseData(content, null, statusCode, statusMessage, contentType, headers);
    }

    /**
     * Sets the response that will be returned when a URL is requested that does
     * not have a specific response configured for it.
     *
     * @param content the content to return
     * @param statusCode the status code to return
     * @param statusMessage the status message to return
     * @param contentType the content type to return
     * @param charset the charset
     * @param headers the response headers to return
     */
    public void setDefaultResponse(final String content, final int statusCode,
            final String statusMessage, final String contentType, final Charset charset,
            final List<NameValuePair> headers) {

        defaultResponse_ = buildRawResponseData(content, charset, statusCode, statusMessage, contentType, headers);
    }

    /**
     * Returns the additional headers that were used in the last call
     * to {@link #getResponse(WebRequest)}.
     *
     * @return the additional headers of the last request
     * @throws IllegalStateException if no request has been made yet
     */
    public Map<String, String> getLastAdditionalHeaders() {
        return getLastWebRequest().getAdditionalHeaders();
    }

    /**
     * Returns the {@link WebRequest} that was used in the last call
     * to {@link #getResponse(WebRequest)}.
     *
     * @return the last {@link WebRequest}
     * @throws IllegalStateException if no request has been made yet
     */
    public WebRequest getLastWebRequest() {
        if (lastRequest_ == null) {
            throw new IllegalStateException("No request has been made yet.");
        }
        return lastRequest_;
    }

    /**
     * Returns the number of requests made to this mock web connection.
     *
     * @return the number of requests made to this mock web connection
     */
    public int getRequestCount() {
        return requestCount_.get();
    }

    /**
     * Returns whether a response has been configured for the given URL.
     *
     * @param url the URL to check
     * @return {@code true} if a response has been configured for the URL; {@code false} otherwise
     */
    public boolean hasResponse(final URL url) {
        return responseMap_.containsKey(url.toExternalForm());
    }

    /**
     * Delegates to {@link #clear()}, resetting all configured responses, recorded
     * requests, and request counts.
     */
    @Override
    public void close() {
        clear();
    }

    /**
     * Resets all state: clears all configured responses and throwables, the default
     * response, the last request, the request count, and the list of requested URLs.
     *
     * <p>Note: {@link #close()} delegates to this method, so using this connection</p>
     * in a try-with-resources block will reset all configured state on exit.
     */
    public void clear() {
        throwableMap_.clear();
        responseMap_.clear();
        defaultResponse_ = null;
        lastRequest_ = null;
        requestCount_.set(0);
        requestedUrls_.clear();
    }
}
