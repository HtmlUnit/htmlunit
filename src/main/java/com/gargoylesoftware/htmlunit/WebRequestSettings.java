/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
     * @param target the URL for this request
     */
    public WebRequestSettings(final URL target) {
        setUrl(target);
    }

    /**
     * Instantiate a {@link WebRequestSettings} for the given URL using the proxy configuration from the original
     * request.
     * @param originalRequest the original request
     * @param target the URL for this request
     */
    public WebRequestSettings(final WebRequestSettings originalRequest, final URL target) {
        this(target);
        setProxyHost(originalRequest.getProxyHost());
        setProxyPort(originalRequest.getProxyPort());
    }

    /**
     * @param target the URL for this request
     * @param submitMethod the submitMethod to set
     * @deprecated As of 2.2, please use {@link #WebRequestSettings(URL, HttpMethod)} instead.
     */
    @Deprecated
    public WebRequestSettings(final URL target, final SubmitMethod submitMethod) {
        this(target);
        setSubmitMethod(submitMethod);
    }

    /**
     * @param target the URL for this request
     * @param submitMethod the submitMethod to set
     */
    public WebRequestSettings(final URL target, final HttpMethod submitMethod) {
        this(target);
        setHttpMethod(submitMethod);
    }

    /**
     * @return the URL
     * @deprecated As of 2.2, use {@link #getUrl()} instead.
     */
    @Deprecated
    public URL getURL() {
        return url_;
    }

    /**
     * @return the URL
     */
    public URL getUrl() {
        return url_;
    }

    /**
     * @param url the new URL
     * @deprecated As of 2.2, user {@link #setUrl(URL)} instead.
     */
    @Deprecated
    public void setURL(final URL url) {
        setUrl(url);
    }

    /**
     * @param url the new URL
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
     * @return the proxy host
     */
    public String getProxyHost() {
        return proxyHost_;
    }

    /**
     * @param proxyHost the new proxy host
     */
    public void setProxyHost(final String proxyHost) {
        proxyHost_ = proxyHost;
    }

    /**
     * @return the proxy port
     */
    public int getProxyPort() {
        return proxyPort_;
    }

    /**
     * @param proxyPort the new proxy port
     */
    public void setProxyPort(final int proxyPort) {
        proxyPort_ = proxyPort;
    }

    /**
     * @return the encodingType
     */
    public FormEncodingType getEncodingType() {
        return encodingType_;
    }

    /**
     * @param encodingType the encodingType to set
     */
    public void setEncodingType(final FormEncodingType encodingType) {
        encodingType_ = encodingType;
    }

    /**
     * @return the requestParameters
     */
    public List<NameValuePair> getRequestParameters() {
        return requestParameters_;
    }

    /**
     * @param requestParameters the requestParameters to set
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
     * Returns the body content to be submitted if this is a <tt>POST</tt> request. Ignored for
     * all other request types. Should not be used in combination with parameters.
     * @return the body content to be submitted if this is a <tt>POST</tt> request
     */
    public String getRequestBody() {
        return requestBody_;
    }

    /**
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
     * @return the submitMethod
     * @deprecated As of 2.2, please use {@link #getHttpMethod()} instead.
     */
    @Deprecated
    public SubmitMethod getSubmitMethod() {
        return SubmitMethod.getInstance(httpMethod_.name());
    }

    /**
     * @return the submitMethod
     */
    public HttpMethod getHttpMethod() {
        return httpMethod_;
    }

    /**
     * @param submitMethod the submitMethod to set
     * @deprecated As of 2.2, please use {@link #setHttpMethod(HttpMethod)} instead.
     */
    @Deprecated
    public void setSubmitMethod(final SubmitMethod submitMethod) {
        setHttpMethod(HttpMethod.valueOf(submitMethod.getName().toUpperCase()));
    }

    /**
     * @param submitMethod the submitMethod to set
     */
    public void setHttpMethod(final HttpMethod submitMethod) {
        httpMethod_ = submitMethod;
    }

    /**
     * @return the additionalHeaders
     */
    public Map<String, String> getAdditionalHeaders() {
        return additionalHeaders_;
    }

    /**
     * @param additionalHeaders the additionalHeaders to set
     */
    public void setAdditionalHeaders(final Map<String, String> additionalHeaders) {
        additionalHeaders_ = additionalHeaders;
    }

    /**
     * Adds the specified name/value pair to the additional headers.
     * @param name the name of the additional header
     * @param value the value of the additional header
     */
    public void addAdditionalHeader(final String name, final String value) {
        additionalHeaders_.put(name, value);
    }

    /**
     * @return the credentialsProvider
     */
    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider_;
    }

    /**
     * @param credentialsProvider the credentialProvider to set
     */
    public void setCredentialsProvider(final CredentialsProvider credentialsProvider) {
        credentialsProvider_ = credentialsProvider;
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

    /**
     * Gets the charset to use to perform the request.
     * @return the charset
     */
    public String getCharset() {
        return charset_;
    }

    /**
     * Sets the charset. Default value is {@link TextUtil#DEFAULT_CHARSET}.
     * @param charset the new charset
     */
    public void setCharset(final String charset) {
        charset_ = charset;
    }

}
