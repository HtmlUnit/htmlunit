/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.lang.ClassUtils;

/**
 * Parameter object for making web requests.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Hans Donner
 * @author Ahmed Ashour
 */
public class WebRequestSettings implements Serializable {

    private static final long serialVersionUID = -7405507885099274031L;
    private URL url_;
    private String proxyHost_;
    private int proxyPort_;
    private HttpMethod httpMethod_ = HttpMethod.GET;
    private FormEncodingType encodingType_ = FormEncodingType.URL_ENCODED;
    private Map<String, String> additionalHeaders_ = new HashMap<String, String>();
    private CredentialsProvider credentialsProvider_;
    private String charset_ = TextUtil.DEFAULT_CHARSET;

    /* These two are mutually exclusive; additionally, requestBody_ should only be set for POST requests. */
    private List<NameValuePair> requestParameters_ = Collections.emptyList();
    private String requestBody_;

    /**
     * Instantiates a {@link WebRequestSettings} for the specified URL.
     * @param url the target URL
     */
    public WebRequestSettings(final URL url) {
        setUrl(url);
        addAdditionalHeader("Accept", "*/*");
        addAdditionalHeader("Accept-Language", "en");
    }

    /**
     * Instantiates a {@link WebRequestSettings} for the specified URL using the proxy configuration from the
     * specified original request.
     * @param originalRequest the original request
     * @param url the target URL
     */
    public WebRequestSettings(final WebRequestSettings originalRequest, final URL url) {
        this(url);
        setProxyHost(originalRequest.getProxyHost());
        setProxyPort(originalRequest.getProxyPort());
    }

    /**
     * Instantiates a {@link WebRequestSettings} for the specified URL using the specified HTTP submit method.
     * @param url the target URL
     * @param submitMethod the HTTP submit method to use
     */
    public WebRequestSettings(final URL url, final HttpMethod submitMethod) {
        this(url);
        setHttpMethod(submitMethod);
    }

    /**
     * Returns the target URL.
     * @return the target URL
     */
    public URL getUrl() {
        return url_;
    }

    /**
     * Sets the target URL.
     * @param url the target URL
     */
    public void setUrl(URL url) {
        if (url != null && url.getPath().length() == 0) {
            try {
                String file = "/" + url.getFile();
                if (url.getRef() != null) {
                    file += '#' + url.getRef();
                }
                url = new URL(url.getProtocol(), url.getHost(), url.getPort(), file);
            }
            catch (final Exception e) {
                throw new RuntimeException("WebRequestSettings: Can not set URL: " + url.toExternalForm());
            }
        }
        url_ = url;
    }

    /**
     * Returns the proxy host to use.
     * @return the proxy host to use
     */
    public String getProxyHost() {
        return proxyHost_;
    }

    /**
     * Sets the proxy host to use.
     * @param proxyHost the proxy host to use
     */
    public void setProxyHost(final String proxyHost) {
        proxyHost_ = proxyHost;
    }

    /**
     * Returns the proxy port to use.
     * @return the proxy port to use
     */
    public int getProxyPort() {
        return proxyPort_;
    }

    /**
     * Sets the proxy port to use.
     * @param proxyPort the proxy port to use
     */
    public void setProxyPort(final int proxyPort) {
        proxyPort_ = proxyPort;
    }

    /**
     * Returns the form encoding type to use.
     * @return the form encoding type to use
     */
    public FormEncodingType getEncodingType() {
        return encodingType_;
    }

    /**
     * Sets the form encoding type to use.
     * @param encodingType the form encoding type to use
     */
    public void setEncodingType(final FormEncodingType encodingType) {
        encodingType_ = encodingType;
    }

    /**
     * Retrieves the request parameters to use. If set, these request parameters will overwrite any
     * request parameters which may be present in the {@link #getUrl() URL}. Should not be used in
     * combination with the {@link #setRequestBody(String) request body}.
     * @return the request parameters to use
     */
    public List<NameValuePair> getRequestParameters() {
        return requestParameters_;
    }

    /**
     * Sets the request parameters to use. If set, these request parameters will overwrite any request
     * parameters which may be present in the {@link #getUrl() URL}. Should not be used in combination
     * with the {@link #setRequestBody(String) request body}.
     * @param requestParameters the request parameters to use
     * @throws RuntimeException if the request body has already been set
     */
    public void setRequestParameters(final List<NameValuePair> requestParameters) throws RuntimeException {
        if (requestBody_ != null) {
            final String msg = "Trying to set the request parameters, but the request body has already been specified;"
                             + "the two are mutually exclusive!";
            throw new RuntimeException(msg);
        }
        requestParameters_ = requestParameters;
    }

    /**
     * Returns the body content to be submitted if this is a <tt>POST</tt> request. Ignored for all other request
     * types. Should not be used in combination with {@link #setRequestParameters(List) request parameters}.
     * @return the body content to be submitted if this is a <tt>POST</tt> request
     */
    public String getRequestBody() {
        return requestBody_;
    }

    /**
     * Sets the body content to be submitted if this is a <tt>POST</tt> request. Ignored for all other request
     * types. Should not be used in combination with {@link #setRequestParameters(List) request parameters}.
     * @param requestBody the body content to be submitted if this is a <tt>POST</tt> request
     * @throws RuntimeException if the request parameters have already been set or this is not a <tt>POST</tt> request
     */
    public void setRequestBody(final String requestBody) throws RuntimeException {
        if (requestParameters_ != null && !requestParameters_.isEmpty()) {
            final String msg = "Trying to set the request body, but the request parameters have already been specified;"
                       + "the two are mutually exclusive!";
            throw new RuntimeException(msg);
        }
        if (httpMethod_ != HttpMethod.POST && httpMethod_ != HttpMethod.PUT) {
            final String msg = "The request body may only be set for POST or PUT requests!";
            throw new RuntimeException(msg);
        }
        requestBody_ = requestBody;
    }

    /**
     * Returns the HTTP submit method to use.
     * @return the HTTP submit method to use
     */
    public HttpMethod getHttpMethod() {
        return httpMethod_;
    }

    /**
     * Sets the HTTP submit method to use.
     * @param submitMethod the HTTP submit method to use
     */
    public void setHttpMethod(final HttpMethod submitMethod) {
        httpMethod_ = submitMethod;
    }

    /**
     * Returns the additional HTTP headers to use.
     * @return the additional HTTP headers to use
     */
    public Map<String, String> getAdditionalHeaders() {
        return additionalHeaders_;
    }

    /**
     * Sets the additional HTTP headers to use.
     * @param additionalHeaders the additional HTTP headers to use
     */
    public void setAdditionalHeaders(final Map<String, String> additionalHeaders) {
        additionalHeaders_ = additionalHeaders;
    }

    /**
     * Adds the specified name/value pair to the additional HTTP headers.
     * @param name the name of the additional HTTP header
     * @param value the value of the additional HTTP header
     */
    public void addAdditionalHeader(String name, final String value) {
        for (final String key : additionalHeaders_.keySet()) {
            if (name.equalsIgnoreCase(key)) {
                name = key;
                break;
            }
        }
        additionalHeaders_.put(name, value);
    }

    /**
     * Removed the specified name/value pair from the additional HTTP headers.
     * @param name the name of the additional HTTP header
     */
    public void removeAdditionalHeader(String name) {
        for (final String key : additionalHeaders_.keySet()) {
            if (name.equalsIgnoreCase(key)) {
                name = key;
                break;
            }
        }
        additionalHeaders_.remove(name);
    }

    /**
     * Returns the credentials provider to use.
     * @return the credentials provider to use
     */
    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider_;
    }

    /**
     * Sets the credentials provider to use.
     * @param credentialsProvider the credentials provider to use
     */
    public void setCredentialsProvider(final CredentialsProvider credentialsProvider) {
        credentialsProvider_ = credentialsProvider;
    }

    /**
     * Returns the character set to use to perform the request.
     * @return the character set to use to perform the request
     */
    public String getCharset() {
        return charset_;
    }

    /**
     * Sets the character set to use to perform the request. The default value
     * is {@link TextUtil#DEFAULT_CHARSET}.
     * @param charset the character set to use to perform the request
     */
    public void setCharset(final String charset) {
        charset_ = charset;
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();

        buffer.append(ClassUtils.getShortClassName(getClass()));

        buffer.append("[<");
        buffer.append("url=\"" + url_ + '"');
        buffer.append(", " + httpMethod_);
        buffer.append(", " + encodingType_);
        buffer.append(", " + requestParameters_);
        buffer.append(", " + additionalHeaders_);
        buffer.append(", " + credentialsProvider_);
        buffer.append(">]");

        return buffer.toString();
    }

}
