/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
        url_ = url;
    }

    /**
     * @param url the new URL
     */
    public void setUrl(final URL url) {
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
        if (requestParameters_ != null && requestParameters_.size() > 0) {
            final String msg = "Trying to set the request body, but the request parameters have already been specified;"
                       + "the two are mutually exclusive!";
            throw new RuntimeException(msg);
        }
        if (httpMethod_ != HttpMethod.POST) {
            final String msg = "The request body may only be set for POST requests!";
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
        buffer.append("url=\"" + url_.toExternalForm() + "\"");
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
