/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * A fake {@link WebConnection} designed to mock out the actual HTTP connections.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author Marc Guillemot
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public class MockWebConnection implements WebConnection {

    private static final Log LOG = LogFactory.getLog(MockWebConnection.class);

    /**
     * Contains the raw data configured for a response.
     */
    public static class RawResponseData {
        private final List<NameValuePair> headers_;
        private final byte[] byteContent_;
        private final String stringContent_;
        private final int statusCode_;
        private final String statusMessage_;
        private String charset_;

        RawResponseData(final byte[] byteContent, final int statusCode, final String statusMessage,
                final String contentType, final List<NameValuePair> headers) {
            byteContent_ = byteContent;
            stringContent_ = null;
            statusCode_ = statusCode;
            statusMessage_ = statusMessage;
            headers_ = compileHeaders(headers, contentType);
        }

        RawResponseData(final String stringContent, final String charset, final int statusCode,
                final String statusMessage, final String contentType, final List<NameValuePair> headers) {
            byteContent_ = null;
            charset_ = charset;
            stringContent_ = stringContent;
            statusCode_ = statusCode;
            statusMessage_ = statusMessage;
            headers_ = compileHeaders(headers, contentType);
        }

        private List<NameValuePair> compileHeaders(final List<NameValuePair> headers, final String contentType) {
            final List<NameValuePair> compiledHeaders = new ArrayList<NameValuePair>();
            if (headers != null) {
                compiledHeaders.addAll(headers);
            }
            if (contentType != null) {
                compiledHeaders.add(new NameValuePair("Content-Type", contentType));
            }
            return compiledHeaders;
        }

        WebResponseData asWebResponseData() {
            final byte[] content;
            if (byteContent_ != null) {
                content = byteContent_;
            }
            else if (stringContent_ == null) {
                content = new byte[] {};
            }
            else {
                content = TextUtil.stringToByteArray(stringContent_, charset_);
            }
            return new WebResponseData(content, statusCode_, statusMessage_, headers_);
        }

        /**
         * Gets the configured headers.
         * @return the headers
         */
        public List<NameValuePair> getHeaders() {
            return headers_;
        }

        /**
         * Gets the configured content bytes.
         * @return <code>null</code> if a String content has been configured
         */
        public byte[] getByteContent() {
            return byteContent_;
        }

        /**
         * Gets the configured content String.
         * @return <code>null</code> if a byte content has been configured
         */
        public String getStringContent() {
            return stringContent_;
        }

        /**
         * Gets the configured status code.
         * @return the status code
         */
        public int getStatusCode() {
            return statusCode_;
        }

        /**
         * Gets the configured status message.
         * @return the message
         */
        public String getStatusMessage() {
            return statusMessage_;
        }

        /**
         * Gets the configured charset.
         * @return <code>null</code> for byte content
         */
        public String getCharset() {
            return charset_;
        }
    }

    private final Map<String, RawResponseData> responseMap_ = new HashMap<String, RawResponseData>(10);
    private RawResponseData defaultResponse_;
    private WebRequest lastRequest_;
    private int requestCount_ = 0;
    private final List<URL> requestedUrls_ = Collections.synchronizedList(new ArrayList<URL>());

    /**
     * {@inheritDoc}
     */
    public WebResponse getResponse(final WebRequest request) throws IOException {
        final RawResponseData rawResponse = getRawResponse(request);
        return new WebResponse(rawResponse.asWebResponseData(), request, 0);
    }

    /**
     * Gets the raw response configured for the request.
     * @param request the request
     * @return the raw response
     */
    public RawResponseData getRawResponse(final WebRequest request) {
        final URL url = request.getUrl();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting response for " + url.toExternalForm());
        }

        lastRequest_ = request;
        requestCount_++;
        requestedUrls_.add(url);

        RawResponseData rawResponse = responseMap_.get(url.toExternalForm());
        if (rawResponse == null) {
            rawResponse = defaultResponse_;
            if (rawResponse == null) {
                throw new IllegalStateException("No response specified that can handle URL ["
                    + url.toExternalForm()
                    + "]");
            }
        }

        return rawResponse;
    }

    /**
     * Gets the list of requested URLs relative to the provided URL.
     * @param relativeTo what should be removed from the requested URLs.
     * @return the list of relative URLs
     */
    public List<String> getRequestedUrls(final URL relativeTo) {
        final String baseUrl = relativeTo.toString();
        final List<String> response = new ArrayList<String>();
        for (final URL url : requestedUrls_) {
            String s = url.toString();
            if (s.startsWith(baseUrl)) {
                s = s.substring(baseUrl.length());
            }
            response.add(s);
        }

        return response;
    }

    /**
     * Returns the method that was used in the last call to submitRequest().
     *
     * @return the method that was used in the last call to submitRequest()
     */
    public HttpMethod getLastMethod() {
        return lastRequest_.getHttpMethod();
    }

    /**
     * Returns the parameters that were used in the last call to submitRequest().
     *
     * @return the parameters that were used in the last call to submitRequest()
     */
    public List<NameValuePair> getLastParameters() {
        return lastRequest_.getRequestParameters();
    }

    /**
     * Sets the response that will be returned when the specified URL is requested.
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
                TextUtil.DEFAULT_CHARSET,
                headers);
    }

    /**
     * Sets the response that will be returned when the specified URL is requested.
     * @param url the URL that will return the given response
     * @param content the content to return
     * @param statusCode the status code to return
     * @param statusMessage the status message to return
     * @param contentType the content type to return
     * @param charset the name of a supported charset
     * @param headers the response headers to return
     */
    public void setResponse(final URL url, final String content, final int statusCode,
            final String statusMessage, final String contentType, final String charset,
            final List<NameValuePair> headers) {

        final RawResponseData responseEntry = buildRawResponseData(content, charset, statusCode, statusMessage,
                contentType, headers);
        responseMap_.put(url.toExternalForm(), responseEntry);
    }

    /**
     * Sets the response that will be returned when the specified URL is requested.
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

    private RawResponseData buildRawResponseData(final byte[] content, final int statusCode, final String statusMessage,
            final String contentType, final List<NameValuePair> headers) {
        return new RawResponseData(content, statusCode, statusMessage, contentType, headers);
    }

    private RawResponseData buildRawResponseData(final String content, String charset, final int statusCode,
            final String statusMessage, final String contentType, final List<NameValuePair> headers) {

        if (charset == null) {
            charset = TextUtil.DEFAULT_CHARSET;
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
        setResponse(url, content, 200, "OK", "text/html", null);
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
     * {@link #setResponse(URL,String,int,String,String,String,List)} with a status
     * of "200 OK" and no additional headers.
     *
     * @param url the URL that will return the given response
     * @param content the content to return
     * @param contentType the content type to return
     * @param charset the name of a supported charset
     */
    public void setResponse(final URL url, final String content, final String contentType, final String charset) {
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
        final String content = "<html><head><title>" + title + "</title></head><body></body></html>";
        setResponse(url, content);
    }

    /**
     * Sets the response that will be returned when a URL is requested that does
     * not have a specific content set for it.
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
     * not have a specific content set for it.
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
     * not have a specific content set for it.
     *
     * @param content the content to return
     */
    public void setDefaultResponse(final String content) {
        setDefaultResponse(content, 200, "OK", "text/html");
    }

    /**
     * Sets the response that will be returned when a URL is requested that does
     * not have a specific content set for it.
     *
     * @param content the content to return
     * @param contentType the content type to return
     */
    public void setDefaultResponse(final String content, final String contentType) {
        setDefaultResponse(content, 200, "OK", contentType, null);
    }

    /**
     * Sets the response that will be returned when a URL is requested that does
     * not have a specific content set for it.
     *
     * @param content the content to return
     * @param contentType the content type to return
     * @param charset the name of a supported charset
     */
    public void setDefaultResponse(final String content, final String contentType, final String charset) {
        setDefaultResponse(content, 200, "OK", contentType, charset, null);
    }

    /**
     * Sets the response that will be returned when the specified URL is requested.
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
     * Sets the response that will be returned when the specified URL is requested.
     * @param content the content to return
     * @param statusCode the status code to return
     * @param statusMessage the status message to return
     * @param contentType the content type to return
     * @param charset the name of a supported charset
     * @param headers the response headers to return
     */
    public void setDefaultResponse(final String content, final int statusCode,
            final String statusMessage, final String contentType, final String charset,
            final List<NameValuePair> headers) {

        defaultResponse_ = buildRawResponseData(content, charset, statusCode, statusMessage, contentType, headers);
    }

    /**
     * Returns the additional headers that were used in the in the last call
     * to {@link #getResponse(WebRequest)}.
     * @return the additional headers that were used in the in the last call
     *         to {@link #getResponse(WebRequest)}
     */
    public Map<String, String> getLastAdditionalHeaders() {
        return lastRequest_.getAdditionalHeaders();
    }

    /**
     * Returns the {@link WebRequest} that was used in the in the last call
     * to {@link #getResponse(WebRequest)}.
     * @return the {@link WebRequest} that was used in the in the last call
     *         to {@link #getResponse(WebRequest)}
     */
    public WebRequest getLastWebRequest() {
        return lastRequest_;
    }

    /**
     * Returns the number of requests made to this mock web connection.
     * @return the number of requests made to this mock web connection
     */
    public int getRequestCount() {
        return requestCount_;
    }

    /**
     * Indicates if a response has already been configured for this URL.
     * @param url the url
     * @return <code>false</code> if no response has been configured
     */
    public boolean hasResponse(final URL url) {
        return responseMap_.containsKey(url.toExternalForm());
    }
}
