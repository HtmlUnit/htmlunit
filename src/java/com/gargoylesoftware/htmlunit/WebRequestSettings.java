/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.lang.ClassUtils;

/**
 * Parameter object for making web requests
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Hans Donner
 * @author Ahmed Ashour
 */
public class WebRequestSettings {
    private URL url_;
    private String proxyHost_ = null;
    private int proxyPort_ = 0;
    private SubmitMethod submitMethod_ = SubmitMethod.GET;
    private FormEncodingType encodingType_ = FormEncodingType.URL_ENCODED;
    private Map additionalHeaders_ = new HashMap();
    private CredentialsProvider credentialsProvider_ = null;
    private String charset_ = TextUtil.DEFAULT_CHARSET;

    /* These two are mutually exclusive; additionally, requestBody_ should only be set for POST requests. */
    private List requestParameters_ = Collections.EMPTY_LIST;
    private String requestBody_ = null;

    /**
     * @param target The URL for this request
     */
    public WebRequestSettings(final URL target) {
        setURL(target);
    }

    /**
     * Instantiate a {@link WebRequestSettings} for the given url using the proxy configuration from the original
     * request
     * @param originalRequest the original request
     * @param target The URL for this request
     */
    public WebRequestSettings(final WebRequestSettings originalRequest, final URL target) {
        this(target);
        setProxyHost(originalRequest.getProxyHost());
        setProxyPort(originalRequest.getProxyPort());
    }

    /**
     * @param target The URL for this request
     * @param submitMethod The submitMethod to set.
     */
    public WebRequestSettings(final URL target, final SubmitMethod submitMethod) {
        this(target);
        setSubmitMethod(submitMethod);
    }

    /**
     * @return the URL
     */
    public URL getURL() {
        return url_;
    }

    /**
     * @param url The new URL
     */
    public void setURL(final URL url) {
        url_ = url;
    }

    /**
     * @return The proxy host.
     */
    public String getProxyHost() {
        return proxyHost_;
    }

    /**
     * @param proxyHost The new proxy host.
     */
    public void setProxyHost( final String proxyHost ) {
        proxyHost_ = proxyHost;
    }

    /**
     * @return The proxy port.
     */
    public int getProxyPort() {
        return proxyPort_;
    }

    /**
     * @param proxyPort The new proxy port.
     */
    public void setProxyPort( final int proxyPort ) {
        proxyPort_ = proxyPort;
    }

    /**
     * @return Returns the encodingType.
     */
    public FormEncodingType getEncodingType() {
        return encodingType_;
    }

    /**
     * @param encodingType The encodingType to set.
     */
    public void setEncodingType(final FormEncodingType encodingType) {
        encodingType_ = encodingType;
    }

    /**
     * @return Returns the requestParameters.
     */
    public List getRequestParameters() {
        return requestParameters_;
    }

    /**
     * @param requestParameters The requestParameters to set.
     * @throws RuntimeException If the request body has already been set.
     */
    public void setRequestParameters(final List requestParameters) throws RuntimeException {
        if (requestBody_ != null) {
            final String msg = "Trying to set the request parameters, but the request body has already been specified;"
                             + "the two are mutually exclusive!";
            throw new RuntimeException( msg );
        }
        requestParameters_ = requestParameters;
    }

    /**
     * Returns the body content to be submitted if this is a <tt>POST</tt> request. Ignored for
     * all other request types. Should not be used in combination with parameters.
     * @return The body content to be submitted if this is a <tt>POST</tt> request.
     */
    public String getRequestBody() {
        return requestBody_;
    }

    /**
     * @param requestBody The body content to be submitted if this is a <tt>POST</tt> request.
     * @throws RuntimeException If the request parameters have already been set or this is not a <tt>POST</tt> request.
     */
    public void setRequestBody(final String requestBody) throws RuntimeException {
        if (requestParameters_ != null && requestParameters_.size() > 0) {
            final String msg = "Trying to set the request body, but the request parameters have already been specified;"
                       + "the two are mutually exclusive!";
            throw new RuntimeException(msg);
        }
        if (submitMethod_ != SubmitMethod.POST) {
            final String msg = "The request body may only be set for POST requests!";
            throw new RuntimeException( msg );
        }
        requestBody_ = requestBody;
    }

    /**
     * @return Returns the submitMethod.
     */
    public SubmitMethod getSubmitMethod() {
        return submitMethod_;
    }

    /**
     * @param submitMethod The submitMethod to set.
     */
    public void setSubmitMethod(final SubmitMethod submitMethod) {
        submitMethod_ = submitMethod;
    }
    
    /**
     * @return Returns the additionalHeaders.
     */
    public Map getAdditionalHeaders() {
        return additionalHeaders_;
    }

    /**
     * @param additionalHeaders The additionalHeaders to set.
     */
    public void setAdditionalHeaders(final Map additionalHeaders) {
        additionalHeaders_ = additionalHeaders;
    }

    /**
     * Adds the specified name/value pair to the additional headers.
     * @param name The name of the additional header.
     * @param value The value of the additional header.
     */
    public void addAdditionalHeader(final String name, final String value) {
        additionalHeaders_.put( name, value );
    }

    /**
     * @return Returns the credentialsProvider.
     */
    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider_;
    }
    
    /**
     * @param credentialsProvider The credentialProvider to set.
     */
    public void setCredentialsProvider(final CredentialsProvider credentialsProvider) {
        credentialsProvider_ = credentialsProvider;
    }

    /**
     * Return a string representation of this object
     * @return See above
     */
    public String toString() {
        final StringBuffer buffer = new StringBuffer();

        buffer.append(ClassUtils.getShortClassName(getClass()));

        buffer.append("[<");
        buffer.append("url=\"" + url_.toExternalForm() + "\"");
        buffer.append(", " + submitMethod_);
        buffer.append(", " + encodingType_);
        buffer.append(", " + requestParameters_);
        buffer.append(", " + additionalHeaders_);
        buffer.append(", " + credentialsProvider_);
        buffer.append(">]");

        return buffer.toString();
    }

    /**
     * Gets the charset to use to perform the request
     * @return the charset.
     */
    public String getCharset() {
        return charset_;
    }

    /**
     * Sets the charset. Default value is {@link TextUtil#DEFAULT_CHARSET}
     * @param charset the new charset
     */
    public void setCharset(final String charset) {
        charset_ = charset;
    }
}
