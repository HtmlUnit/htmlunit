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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
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

    private String virtualHost_ = null;

    // http://jakarta.apache.org/commons/httpclient/3.0/exception-handling.html#Automatic%20exception%20recovery
    private static final HttpMethodRetryHandler NoAutoRetry = new HttpMethodRetryHandler() {
        public boolean retryMethod(final HttpMethod arg0, final IOException arg1, final int arg2) {
            return false;
        }
    };


    /**
     * Create a new HTTP web connection instance.
     * @param  webClient The WebClient that is using this connection
     */
    public HttpWebConnection( final WebClient webClient ) {
        super(webClient);
    }


    /**
     *  Submit a request and retrieve a response
     *
     * @param  webRequestSettings Settings to make the request with
     * @return  See above
     * @exception  IOException If an IO error occurs
     */
    public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException {

        final URL url = webRequestSettings.getURL();

        final HttpClient httpClient = getHttpClient();

        final HttpMethodBase httpMethod = makeHttpMethod(webRequestSettings);
        try {
            final HostConfiguration hostConfiguration = getHostConfiguration(webRequestSettings);
            final long startTime = System.currentTimeMillis();
            final int responseCode = httpClient.executeMethod(hostConfiguration, httpMethod);
            final long endTime = System.currentTimeMillis();
            return makeWebResponse( responseCode, httpMethod, url, endTime-startTime, webRequestSettings.getCharset() );
        }
        catch( final HttpException e ) {
            // KLUDGE: hitting www.yahoo.com will cause an exception to be thrown while
            // www.yahoo.com/ (note the trailing slash) will not.  If an exception is
            // caught here then check to see if this is the situation.  If so, then retry
            // it with a trailing slash.  The bug manifests itself with httpClient
            // complaining about not being able to find a line with HTTP/ on it.
            if( url.getPath().length() == 0 ) {
                final StringBuffer buffer = new StringBuffer();
                buffer.append(url.getProtocol());
                buffer.append("://");
                buffer.append(url.getHost());
                buffer.append("/");
                if( url.getQuery() != null ) {
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
                throw new RuntimeException( "HTTP Error: " + e.getMessage() );
            }
        }
        finally {
            httpMethod.releaseConnection();
        }
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
        catch( final URIException e ) {
            // Theoretically impossible but ....
            throw new IllegalStateException("Unable to create URI from URL: "+url.toExternalForm());
        }
        hostConfiguration.setHost(uri);
        if( webRequestSettings.getProxyHost() != null ) {
            final String proxyHost = webRequestSettings.getProxyHost();
            final int proxyPort = webRequestSettings.getProxyPort();
            hostConfiguration.setProxy( proxyHost, proxyPort );
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

        final HttpMethodBase httpMethod;
        String path = webRequestSettings.getURL().getPath();
        if( path.length() == 0 ) {
            path = "/";
        }
        if (SubmitMethod.GET == webRequestSettings.getSubmitMethod()) {
            httpMethod = new GetMethod( path );

            if (webRequestSettings.getRequestParameters().isEmpty() ) {
                final String queryString = webRequestSettings.getURL().getQuery();
                httpMethod.setQueryString( queryString );
            }
            else {
                final NameValuePair[] pairs = new NameValuePair[webRequestSettings.getRequestParameters().size()];
                webRequestSettings.getRequestParameters().toArray( pairs );
                httpMethod.setQueryString( pairs );
            }
        }
        else if (SubmitMethod.POST  == webRequestSettings.getSubmitMethod()) {
            final PostMethod postMethod = new PostMethod( path );
            postMethod.getParams().setContentCharset(webRequestSettings.getCharset());

            final String queryString = webRequestSettings.getURL().getQuery();
            if( queryString != null ) {
                postMethod.setQueryString(queryString);
            }
            if (webRequestSettings.getRequestBody() != null ) {
                final String body = webRequestSettings.getRequestBody();
                final String charset = webRequestSettings.getCharset();
                postMethod.setRequestEntity( new StringRequestEntity(body, null, charset) );
            }

            // Note that this has to be done in two loops otherwise it won't
            // be able to support two elements with the same name.
            if (webRequestSettings.getEncodingType() == FormEncodingType.URL_ENCODED) {
                Iterator iterator = webRequestSettings.getRequestParameters().iterator();
                while( iterator.hasNext() ) {
                    final NameValuePair pair = ( NameValuePair )iterator.next();
                    postMethod.removeParameter( pair.getName(), pair.getValue() );
                }

                iterator = webRequestSettings.getRequestParameters().iterator();
                while( iterator.hasNext() ) {
                    final NameValuePair pair = ( NameValuePair )iterator.next();
                    postMethod.addParameter( pair.getName(), pair.getValue() );
                }
            }
            else {
                final List partList = new ArrayList();
                final Iterator iterator = webRequestSettings.getRequestParameters().iterator();
                while (iterator.hasNext()) {
                    final PartBase newPart;
                    final KeyValuePair pair = (KeyValuePair) iterator.next();
                    if (pair instanceof KeyDataPair) {
                        final KeyDataPair pairWithFile = (KeyDataPair) pair;
                        newPart = new FilePart(
                                pairWithFile.getName(),
                                pairWithFile.getValue(),
                                pairWithFile.getFile(),
                                pairWithFile.getContentType(),
                                null);
                        // Firefox and IE seem not to specify a charset for a file part
                        newPart.setCharSet(null);
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
                postMethod.setRequestEntity(new MultipartRequestEntity(
                        parts,
                        postMethod.getParams()));
            }
            httpMethod = postMethod;
        }
        else {
            throw new IllegalStateException("Submit method not yet supported: " + webRequestSettings.getSubmitMethod());
        }

        httpMethod.setRequestHeader(
                "User-Agent", getWebClient().getBrowserVersion().getUserAgent() );

        writeRequestHeadersToHttpMethod(httpMethod, webRequestSettings.getAdditionalHeaders());
        httpMethod.setFollowRedirects(false);

        httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, NoAutoRetry);
        if (webRequestSettings.getCredentialsProvider() != null) {
            httpMethod.getParams().setParameter(CredentialsProvider.PROVIDER,
                    webRequestSettings.getCredentialsProvider());
        }
        
        if( !getWebClient().isCookiesEnabled() ) {
            httpMethod.getParams().setCookiePolicy( CookiePolicy.IGNORE_COOKIES );
        }
        return httpMethod;
    }

    private synchronized HttpClient getHttpClient() {

        if (httpClient_ == null ) {
            httpClient_ = createHttpClient();

            // Disable informational messages from httpclient
            final Log log = LogFactory.getLog("httpclient.wire");
            if( log instanceof SimpleLog ) {
                ((SimpleLog)log).setLevel( SimpleLog.LOG_LEVEL_WARN );
            }

            final int timeout = getWebClient().getTimeout();
            httpClient_.getHttpConnectionManager().getParams().setSoTimeout(timeout);
            httpClient_.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);


            if (virtualHost_ != null) {
                httpClient_.getParams().setVirtualHost(virtualHost_);
            }
        }

        // Tell the client where to get its credentials from
        // (it may have changed on the webClient since last call to getHttpClientFor(...))
        httpClient_.getParams().setParameter( CredentialsProvider.PROVIDER, getWebClient().getCredentialsProvider() );

        return httpClient_;
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
        final List headers = new ArrayList();
        final Header[] array = method.getResponseHeaders();
        for (int i = 0; i < array.length; i++) {
            headers.add(new NameValuePair(array[i].getName(), array[i].getValue()));
        }

        final WebResponseData responseData = new WebResponseData(
                method.getResponseBodyAsStream(),
                statusCode,
                statusMessage,
                headers);

        final SubmitMethod requestMethod = SubmitMethod.getInstance(method.getName());
        return new WebResponseImpl(responseData, charset, originatingURL, requestMethod, loadTime);
    }


    private void writeRequestHeadersToHttpMethod( final HttpMethod httpMethod, final Map requestHeaders ) {
        synchronized( requestHeaders ) {
            final Iterator iterator = requestHeaders.entrySet().iterator();
            while( iterator.hasNext() ) {
                final Map.Entry entry = ( Map.Entry )iterator.next();
                httpMethod.setRequestHeader( ( String )entry.getKey(), ( String )entry.getValue() );
            }
        }
    }

}

