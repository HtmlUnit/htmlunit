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
 * A fake WebConnection designed to mock out the actual http connections.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author Marc Guillemot
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public class MockWebConnection extends WebConnectionImpl {
    private final Map responseMap_ = new HashMap(10);
    private WebResponseData defaultResponse_;

    private WebRequestSettings lastRequest_;
    private HttpState httpState_ = new HttpState();

    /**
     *  Create an instance
     *
     * @param webClient The web client
     */
    public MockWebConnection(final WebClient webClient) {
        super(webClient);
    }

    /**
     * Return the log that is being used for all scripting objects
     * @return The log.
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }

    /**
     *  Submit a request and retrieve a response
     *
     * @param webRequestSettings Settings to make the request with
     * @return See above
     * @throws IOException (only for extending classes)
     */
    public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException {
        final URL url = webRequestSettings.getURL();

        getLog().debug("Getting response for " + url.toExternalForm());

        lastRequest_ = webRequestSettings;

        WebResponseData response = (WebResponseData) responseMap_.get(url.toExternalForm());
        if (response == null) {
            response = defaultResponse_;
            if (response == null) {
                throw new IllegalStateException("No response specified that can handle url ["
                    + url.toExternalForm()
                    + "]");
            }
        }

        return new WebResponseImpl(response, webRequestSettings.getCharset(), 
                webRequestSettings.getURL(), webRequestSettings.getSubmitMethod(), 0);
    }

    /**
     *  Return the method that was used in the last call to submitRequest()
     *
     * @return See above
     */
    public SubmitMethod getLastMethod() {
        return lastRequest_.getSubmitMethod();
    }

    /**
     *  Return the parameters that were used in the last call to submitRequest()
     *
     * @return See above
     */
    public List getLastParameters() {
        return lastRequest_.getRequestParameters();
    }

    /**
     * Set the response that will be returned when the specified url is requested.
     * @param url The url that will return the given response
     * @param content The content to return
     * @param statusCode The status code to return
     * @param statusMessage The status message to return
     * @param contentType The content type to return
     * @param responseHeaders A list of {@link KeyValuePair}s that will be returned as
     * response headers.
     */
    public void setResponse(final URL url, final String content, final int statusCode,
            final String statusMessage, final String contentType, final List responseHeaders) {

        setResponse(
                url,
                TextUtil.stringToByteArray(content),
                statusCode,
                statusMessage,
                contentType,
                responseHeaders);
    }

    /**
     * Set the response that will be returned when the specified url is requested.
     * @param url The url that will return the given response
     * @param content The content to return
     * @param statusCode The status code to return
     * @param statusMessage The status message to return
     * @param contentType The content type to return
     * @param responseHeaders A list of {@link KeyValuePair}s that will be returned as
     * response headers.
     */
    public void setResponse(final URL url, final byte[] content, final int statusCode,
            final String statusMessage, final String contentType, final List responseHeaders) {

        final List compiledHeaders = new ArrayList(responseHeaders);
        compiledHeaders.add(new NameValuePair("Content-Type", contentType));
        final WebResponseData responseEntry = new WebResponseData(
                content,
                statusCode,
                statusMessage,
                compiledHeaders);
        responseMap_.put(url.toExternalForm(), responseEntry);
    }

    /**
     * Convenience method that is the same as calling
     * {@link #setResponse(URL,String,int,String,String,List)} with a status
     * of "200 OK", a content type of "text/html" and no additional headers.
     *
     * @param url The url that will return the given response
     * @param content The content to return
     */
    public void setResponse(final URL url, final String content) {
        setResponse(url, content, 200, "OK", "text/html", Collections.EMPTY_LIST);
    }

    /**
     * Convenience method that is the same as calling
     * {@link #setResponse(URL,String,int,String,String,List)} with a status
     * of "200 OK" and no additional headers.
     *
     * @param url The url that will return the given response
     * @param content The content to return
     * @param contentType The content type to return
     */
    public void setResponse(final URL url, final String content, final String contentType) {
        setResponse(url, content, 200, "OK", contentType, Collections.EMPTY_LIST);
    }

    /**
     * Specify a generic html page that will be returned when the given url is specified.
     * The page will contain only minimal html to satisfy the html parser but will contain
     * the specified title so that tests can check for titleText.
     *
     * @param url The url that will return the given response
     * @param title The title of the page
     */
    public void setResponseAsGenericHtml(final URL url, final String title) {

        final String content = "<html><head><title>" + title + "</title></head><body></body></html>";
        setResponse(url, content);
    }

    /**
     * Set the response that will be returned when a url is requested that does
     * not have a specific content set for it.
     *
     * @param content The content to return
     * @param statusCode The status code to return
     * @param statusMessage The status message to return
     * @param contentType The content type to return
     */
    public void setDefaultResponse(final String content, final int statusCode,
            final String statusMessage, final String contentType) {

        setDefaultResponse(TextUtil.stringToByteArray(content), statusCode, statusMessage, contentType);
    }

    /**
     * Set the response that will be returned when a url is requested that does
     * not have a specific content set for it.
     *
     * @param content The content to return
     * @param statusCode The status code to return
     * @param statusMessage The status message to return
     * @param contentType The content type to return
     */
    public void setDefaultResponse(final byte[] content, final int statusCode,
            final String statusMessage, final String contentType) {

        final List compiledHeaders = new ArrayList();
        compiledHeaders.add(new NameValuePair("Content-Type", contentType));
        final WebResponseData responseEntry = new WebResponseData(
                content,
                statusCode,
                statusMessage,
                compiledHeaders);
        defaultResponse_ = responseEntry;
    }

    /**
     * Set the response that will be returned when a url is requested that does
     * not have a specific content set for it.
     *
     * @param content The content to return
     */
    public void setDefaultResponse(final String content) {
        setDefaultResponse(content, 200, "OK", "text/html");
    }

    /**
     * Return the {@link HttpState}
     * @return The state.
     */
    public HttpState getState() {
        return httpState_;
    }

    /**
     *  Return the additional headers that were used in the in the last call 
     *  to {@link #getResponse(WebRequestSettings)}.
     * @return See above
     */
    public Map getLastAdditionalHeaders() {
        return lastRequest_.getAdditionalHeaders();
    }

    /**
     * Return the {@link WebRequestSettings} that was used in the in the last call 
     * to {@link #getResponse(WebRequestSettings)}.
     * @return See above
     */
    public WebRequestSettings getLastWebRequestSettings() {
        return lastRequest_;
    }
}
