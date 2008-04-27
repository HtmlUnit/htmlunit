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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A fake WebConnection designed to mock out the actual HTTP connections.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author Marc Guillemot
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public class MockWebConnection extends WebConnectionImpl {
    private final Map<String, WebResponseData> responseMap_ = new HashMap<String, WebResponseData>(10);
    private WebResponseData defaultResponse_;

    private WebRequestSettings lastRequest_;
    private HttpState httpState_ = new HttpState();

    /**
     * Creates an instance.
     *
     * @param webClient the web client
     */
    public MockWebConnection(final WebClient webClient) {
        super(webClient);
    }

    /**
     * Returns the log that is being used for all scripting objects.
     * @return the log
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException {
        final URL url = webRequestSettings.getUrl();

        getLog().debug("Getting response for " + url.toExternalForm());

        lastRequest_ = webRequestSettings;

        WebResponseData response = responseMap_.get(url.toExternalForm());
        if (response == null) {
            response = defaultResponse_;
            if (response == null) {
                throw new IllegalStateException("No response specified that can handle URL ["
                    + url.toExternalForm()
                    + "]");
            }
        }

        return new WebResponseImpl(response, webRequestSettings.getCharset(), webRequestSettings, 0);
    }

    /**
     * Returns the method that was used in the last call to submitRequest().
     *
     * @return the method that was used in the last call to submitRequest()
     */
    public SubmitMethod getLastMethod() {
        return lastRequest_.getSubmitMethod();
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
     * @param responseHeaders the response headers to return
     */
    public void setResponse(final URL url, final String content, final int statusCode,
            final String statusMessage, final String contentType,
            final List< ? extends NameValuePair> responseHeaders) {

        setResponse(
                url,
                TextUtil.stringToByteArray(content),
                statusCode,
                statusMessage,
                contentType,
                responseHeaders);
    }

    /**
     * Sets the response that will be returned when the specified URL is requested.
     * @param url the URL that will return the given response
     * @param content the content to return
     * @param statusCode the status code to return
     * @param statusMessage the status message to return
     * @param contentType the content type to return
     * @param responseHeaders the response headers to return
     */
    public void setResponse(final URL url, final byte[] content, final int statusCode,
            final String statusMessage, final String contentType,
            final List< ? extends NameValuePair> responseHeaders) {

        final List<NameValuePair> compiledHeaders = new ArrayList<NameValuePair>(responseHeaders);
        compiledHeaders.add(new NameValuePair("Content-Type", contentType));
        final WebResponseData responseEntry = new WebResponseData(content, statusCode, statusMessage, compiledHeaders);
        responseMap_.put(url.toExternalForm(), responseEntry);
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
        final List< ? extends NameValuePair> emptyList = Collections.emptyList();
        setResponse(url, content, 200, "OK", "text/html", emptyList);
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
        final List< ? extends NameValuePair> emptyList = Collections.emptyList();
        setResponse(url, content, 200, "OK", contentType, emptyList);
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

        setDefaultResponse(TextUtil.stringToByteArray(content), statusCode, statusMessage, contentType);
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

        final List<NameValuePair> compiledHeaders = new ArrayList<NameValuePair>();
        compiledHeaders.add(new NameValuePair("Content-Type", contentType));
        final WebResponseData responseEntry = new WebResponseData(content, statusCode, statusMessage, compiledHeaders);
        defaultResponse_ = responseEntry;
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
     * Returns the {@link HttpState}.
     * @return the state
     */
    @Override
    public HttpState getState() {
        return httpState_;
    }

    /**
     * Returns the additional headers that were used in the in the last call
     * to {@link #getResponse(WebRequestSettings)}.
     * @return the additional headers that were used in the in the last call
     *         to {@link #getResponse(WebRequestSettings)}
     */
    public Map<String, String> getLastAdditionalHeaders() {
        return lastRequest_.getAdditionalHeaders();
    }

    /**
     * Returns the {@link WebRequestSettings} that was used in the in the last call
     * to {@link #getResponse(WebRequestSettings)}.
     * @return the {@link WebRequestSettings} that was used in the in the last call
     *         to {@link #getResponse(WebRequestSettings)}
     */
    public WebRequestSettings getLastWebRequestSettings() {
        return lastRequest_;
    }
}
