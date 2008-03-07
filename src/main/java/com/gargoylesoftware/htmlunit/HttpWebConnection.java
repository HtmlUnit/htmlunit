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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.SimpleLog;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 *
 * An object that handles the actual communication portion of page
 * retrieval/submission
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public class HttpWebConnection extends WebConnectionImpl {
    private HttpClient httpClient_;

    private String virtualHost_;

    /**
     * Create a new HTTP web connection instance.
     * @param webClient The WebClient that is using this connection
     */
    public HttpWebConnection(final WebClient webClient) {
        super(webClient);
    }

    /**
     * Submit a request and retrieve a response
     *
     * @param webRequestSettings Settings to make the request with
     * @return See above
     * @exception IOException If an IO error occurs
     */
    @Override
    public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException {
        final URL url = webRequestSettings.getURL();

        final HttpClient httpClient = getHttpClient();

        final HttpMethodBase httpMethod = makeHttpMethod(webRequestSettings);
        try {
            final HostConfiguration hostConfiguration = getHostConfiguration(webRequestSettings);
            final long startTime = System.currentTimeMillis();
            final int responseCode = httpClient.executeMethod(hostConfiguration, httpMethod);
            final long endTime = System.currentTimeMillis();
            return makeWebResponse(responseCode, httpMethod, url, endTime - startTime, webRequestSettings.getCharset());
        }
        catch (final HttpException e) {
            // KLUDGE: hitting www.yahoo.com will cause an exception to be thrown while
            // www.yahoo.com/ (note the trailing slash) will not.  If an exception is
            // caught here then check to see if this is the situation.  If so, then retry
            // it with a trailing slash.  The bug manifests itself with httpClient
            // complaining about not being able to find a line with HTTP/ on it.
            if (url.getPath().length() == 0) {
                final StringBuilder buffer = new StringBuilder();
                buffer.append(url.getProtocol());
                buffer.append("://");
                buffer.append(url.getHost());
                buffer.append("/");
                if (url.getQuery() != null) {
                    buffer.append(url.getQuery());
                }
                //TODO: There might be a bug here since the original encoding type is lost.
                final WebRequestSettings newRequest = new WebRequestSettings(new URL(buffer.toString()));
                newRequest.setSubmitMethod(webRequestSettings.getSubmitMethod());
                newRequest.setRequestParameters(webRequestSettings.getRequestParameters());
                newRequest.setAdditionalHeaders(webRequestSettings.getAdditionalHeaders());
                return getResponse(newRequest);
            }
            else {
                e.printStackTrace();
                throw new RuntimeException("HTTP Error: " + e.getMessage());
            }
        }
        finally {
            onResponseGenerated(httpMethod);
        }
    }

    /**
     * Called when the response has been generated. Default action is to release
     * the HttpMethod's connection. Subclasses may override.
     * @param httpMethod the httpMethod used.
     */
    protected void onResponseGenerated(final HttpMethodBase httpMethod) {
        httpMethod.releaseConnection();
    }
        
    /**
     * Gets the host configuration for the request.
     * Should we cache it?
     * @param webRequestSettings the current request settings
     * @return the host configuration to use for this request
     */
    private HostConfiguration getHostConfiguration(final WebRequestSettings webRequestSettings) {
        final HostConfiguration hostConfiguration = new HostConfiguration();
        final URL url = webRequestSettings.getURL();
        final URI uri;
        try {
            uri = new URI(url.toExternalForm(), false);
        }
        catch (final URIException e) {
            // Theoretically impossible but ....
            throw new IllegalStateException("Unable to create URI from URL: " + url.toExternalForm());
        }
        hostConfiguration.setHost(uri);
        if (webRequestSettings.getProxyHost() != null) {
            final String proxyHost = webRequestSettings.getProxyHost();
            final int proxyPort = webRequestSettings.getProxyPort();
            hostConfiguration.setProxy(proxyHost, proxyPort);
        }
        return hostConfiguration;
    }

    /**
     * Creates an <tt>HttpMethod</tt> instance according to the specified parameters.
     * @param webRequestSettings the parameters.
     * @return The <tt>HttpMethod</tt> instance constructed according to the specified parameters.
     * @throws IOException
     */
    private HttpMethodBase makeHttpMethod(final WebRequestSettings webRequestSettings)
        throws IOException {

        String path = webRequestSettings.getURL().getPath();
        if (path.length() == 0) {
            path = "/";
        }
        else if (path.startsWith("//")) {
            path = "//" + path; // cf https://issues.apache.org/jira/browse/HTTPCLIENT-727
        }
        final HttpMethodBase httpMethod = buildHttpMethod(webRequestSettings.getSubmitMethod(), path);
        if (!(httpMethod instanceof EntityEnclosingMethod)) {
            // this is the case for GET as well as TRACE, DELETE, OPTIONS and HEAD

            if (webRequestSettings.getRequestParameters().isEmpty()) {
                final String queryString = webRequestSettings.getURL().getQuery();
                httpMethod.setQueryString(queryString);
            }
            else {
                final NameValuePair[] pairs = new NameValuePair[webRequestSettings.getRequestParameters().size()];
                webRequestSettings.getRequestParameters().toArray(pairs);
                httpMethod.setQueryString(pairs);
            }
        }
        else { // POST as well as PUT
            final EntityEnclosingMethod method = (EntityEnclosingMethod) httpMethod;
            method.getParams().setContentCharset(webRequestSettings.getCharset());

            final String queryString = webRequestSettings.getURL().getQuery();
            if (queryString != null) {
                method.setQueryString(queryString);
            }
            if (webRequestSettings.getRequestBody() != null) {
                final String body = webRequestSettings.getRequestBody();
                final String charset = webRequestSettings.getCharset();
                method.setRequestEntity(new StringRequestEntity(body, null, charset));
            }

            // Note that this has to be done in two loops otherwise it won't
            // be able to support two elements with the same name.
            if (webRequestSettings.getEncodingType() == FormEncodingType.URL_ENCODED
                    && method instanceof PostMethod) {
                final PostMethod postMethod = (PostMethod) httpMethod;
                for (final NameValuePair pair : webRequestSettings.getRequestParameters()) {
                    postMethod.removeParameter(pair.getName(), pair.getValue());
                }

                for (final NameValuePair pair : webRequestSettings.getRequestParameters()) {
                    postMethod.addParameter(pair.getName(), pair.getValue());
                }
            }
            else {
                final List<PartBase> partList = new ArrayList<PartBase>();
                for (final KeyValuePair pair : webRequestSettings.getRequestParameters()) {
                    final PartBase newPart;
                    if (pair instanceof KeyDataPair) {
                        final KeyDataPair pairWithFile = (KeyDataPair) pair;
                        final String charset = webRequestSettings.getCharset();
                        newPart = buildFilePart(pairWithFile, charset);
                    }
                    else {
                        newPart = new StringPart(pair.getName(), pair.getValue(), webRequestSettings.getCharset());
                        newPart.setContentType(null); // Firefox and IE seem not to send a content type
                    }
                    newPart.setTransferEncoding(null); // Firefox and IE don't send transfer encoding headers
                    partList.add(newPart);
                }
                Part[] parts = new Part[partList.size()];
                parts = (Part[]) partList.toArray(parts);
                method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
            }
        }

        httpMethod.setRequestHeader("User-Agent", getWebClient().getBrowserVersion().getUserAgent());

        writeRequestHeadersToHttpMethod(httpMethod, webRequestSettings.getAdditionalHeaders());
        httpMethod.setFollowRedirects(false);

        if (webRequestSettings.getCredentialsProvider() != null) {
            httpMethod.getParams().setParameter(CredentialsProvider.PROVIDER,
                    webRequestSettings.getCredentialsProvider());
        }

        if (getWebClient().isCookiesEnabled()) {
            // Cookies are enabled. Note that it's important that we enable single cookie headers,
            // for compatibility purposes.
            httpMethod.getParams().setBooleanParameter(HttpMethodParams.SINGLE_COOKIE_HEADER, true);
            httpMethod.getParams().setCookiePolicy(WebClient.HTMLUNIT_COOKIE_POLICY);
        }
        else {
            // Cookies are disabled.
            httpMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        }

        return httpMethod;
    }

    FilePart buildFilePart(final KeyDataPair pairWithFile, final String charset) throws FileNotFoundException {
        final FilePart part = new FilePart(pairWithFile.getName(), pairWithFile.getValue(), pairWithFile.getFile(),
                pairWithFile.getContentType(), null) {

            /**
             * This implementation overrides the super one by encoding filename
             * according to the page charset.
             * @see http://issues.apache.org/jira/browse/HTTPCLIENT-293
             * {@inheritDoc}
             */
            @Override
            protected void sendDispositionHeader(final OutputStream out) throws IOException {
                out.write(CONTENT_DISPOSITION_BYTES);
                out.write(QUOTE_BYTES);
                out.write(EncodingUtil.getAsciiBytes(getName()));
                out.write(QUOTE_BYTES);
                final String filename = getSource().getFileName();
                if (filename != null) {
                    out.write(EncodingUtil.getAsciiBytes(FILE_NAME));
                    out.write(QUOTE_BYTES);
                    out.write(EncodingUtil.getBytes(getFileName(), charset));
                    out.write(QUOTE_BYTES);
                }
            }

            private String getFileName() {
                if (pairWithFile.getFile() == null) {
                    return pairWithFile.getValue();
                }
                else if (getWebClient().getBrowserVersion().isIE()) {
                    return pairWithFile.getFile().getAbsolutePath();
                }
                else {
                    return pairWithFile.getValue();
                }
            }
        };
        // Firefox and IE seem not to specify a charset for a file part
        part.setCharSet(null);
        
        return part;
    }

    private HttpMethodBase buildHttpMethod(final SubmitMethod submitMethod, final String path) {
        final HttpMethodBase method;

        if (SubmitMethod.GET == submitMethod) {
            method = new GetMethod(path);
        }
        else if (SubmitMethod.POST == submitMethod) {
            method = new PostMethod(path);
        }
        else {
            throw new IllegalStateException("Submit method not yet supported: " + submitMethod);
        }
        return method;
    }

    /**
     * Lazily initialize the httpClient
     * @return the initialized client
     */
    protected synchronized HttpClient getHttpClient() {
        if (httpClient_ == null) {
            httpClient_ = createHttpClient();

            // Disable informational messages from httpclient
            final Log log = LogFactory.getLog("httpclient.wire");
            if (log instanceof SimpleLog) {
                ((SimpleLog) log).setLevel(SimpleLog.LOG_LEVEL_WARN);
            }

            httpClient_.getHttpConnectionManager().getParams().setSoTimeout(getTimeout());
            httpClient_.getHttpConnectionManager().getParams().setConnectionTimeout(getTimeout());

            if (virtualHost_ != null) {
                httpClient_.getParams().setVirtualHost(virtualHost_);
            }
        }

        // Tell the client where to get its credentials from
        // (it may have changed on the webClient since last call to getHttpClientFor(...))
        httpClient_.getParams().setParameter(CredentialsProvider.PROVIDER, getWebClient().getCredentialsProvider());

        return httpClient_;
    }
    
    /**
     * Return the timeout to use for socket and connection timeouts for HttpConnectionManager.
     * is overridden to 0 by StreamingWebConnection which keeps reading after a timeout and
     * must have long running connections explicitly terminated.
     * @return the WebClient's timeout.
     */
    protected int getTimeout() {
        return getWebClient().getTimeout();
    }

    /**
     * Creates the httpClient that will be used by this WebConnection.
     * Extensions may override this method to create the HttpClient with for instance a custom
     * {@link org.apache.commons.httpclient.HttpConnectionManager} to perform some tracking
     * (see feature request 1438216).
     * @return the client
     */
    protected HttpClient createHttpClient() {
        final MultiThreadedHttpConnectionManager connectionManager =
            new MultiThreadedHttpConnectionManager();
        return new HttpClient(connectionManager);
    }

    /**
     * Return the log object for this class
     * @return The log object
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }

    /**
     * set the virtual host
     * @param virtualHost The virtualHost to set.
     */
    public void setVirtualHost(final String virtualHost) {
        virtualHost_ = virtualHost;
    }

    /**
     * Get the virtual host
     * @return virtualHost The current virtualHost
     */
    public String getVirtualHost() {
        return virtualHost_;
    }

    /**
     * Return the {@link HttpState} that is being used.
     * @return The state.
     */
    @Override
    public HttpState getState() {
        return getHttpClient().getState();
    }

    /**
     * Converts the HttpMethod into a WebResponse
     */
    private WebResponse makeWebResponse(final int statusCode, final HttpMethodBase method,
            final URL originatingURL, final long loadTime, final String charset) throws IOException {

        String statusMessage = method.getStatusText();
        if (statusMessage == null || statusMessage.length() == 0) {
            statusMessage = HttpStatus.getStatusText(statusCode);
        }
        if (statusMessage == null) {
            statusMessage = "Unknown status code";
        }
        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        for (final Header header : method.getResponseHeaders()) {
            headers.add(new NameValuePair(header.getName(), header.getValue()));
        }
        final WebResponseData responseData = newWebResponseDataInstance(statusMessage, headers, statusCode, method);

        final SubmitMethod requestMethod = SubmitMethod.getInstance(method.getName());
        return newWebResponseInstance(charset, responseData, loadTime, requestMethod, originatingURL);
    }

    /**
     * Construct an appropriate WebResponseData.
     * May be overridden by subclasses to return a specialized WebResponseData.
     * @param statusMessage StatusMessage from the response
     * @param headers response headers
     * @param statusCode response status code
     * @param method request method
     * @return The WebResponseData to use for this response.
     * @throws IOException if there is a problem reading the response body.
     */
    protected WebResponseData newWebResponseDataInstance(
            final String statusMessage,
            final List<NameValuePair> headers,
            final int statusCode,
            final HttpMethodBase method
    ) throws IOException {
        return new WebResponseData(method.getResponseBodyAsStream(), statusCode, statusMessage, headers);
    }

    /**
     * Construct an appropriate WebResponse.
     * May be overridden by subclasses to return a specialized WebResponse.
     * @param responseData Data that was send back
     * @param charset Charset used if not returned in the response.
     * @param originatingURL Where this response came from
     * @param requestMethod The method used to get this response
     * @param loadTime How long the response took to be sent
     * @return the new WebResponse.
     */
    protected WebResponse newWebResponseInstance(
            final String charset,
            final WebResponseData responseData,
            final long loadTime,
            final SubmitMethod requestMethod,
            final URL originatingURL) {
        return new WebResponseImpl(responseData, charset, originatingURL, requestMethod, loadTime);
    }

    private void writeRequestHeadersToHttpMethod(final HttpMethod httpMethod,
            final Map<String, String> requestHeaders) {
        synchronized (requestHeaders) {
            for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                httpMethod.setRequestHeader(entry.getKey(), entry.getValue());
            }
        }
    }

}
