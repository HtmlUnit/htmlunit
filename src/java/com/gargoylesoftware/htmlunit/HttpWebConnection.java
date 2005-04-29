/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.SimpleLog;

/**
 *  An object that handles the actual communication portion of page
 *  retrieval/submission <p />
 *
 *  THIS CLASS IS FOR INTERNAL USE ONLY
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author David D. Kilzer
 * @author Marc Guillemot
 */
public class HttpWebConnection extends WebConnection {
    private final Map httpClients_ = new HashMap( 9 );

    /**
     *  Create an instance that will not use a proxy server
     *
     * @param  webClient The WebClient that is using this connection
     */
    public HttpWebConnection( final WebClient webClient ) {
        super(webClient);
    }


    /**
     *  Create an instance that will use the specified proxy server
     *
     * @param  proxyHost The server that will act as proxy
     * @param  proxyPort The port to use on the proxy server
     * @param  webClient The web client that is using this connection
     */
    public HttpWebConnection( final WebClient webClient, final String proxyHost, final int proxyPort ) {
        super(webClient, proxyHost, proxyPort);
    }


    /**
     *  Submit a request and retrieve a response
     *
     * @param  webRequestSettings Settings to make the request with
     * @return  See above
     * @exception  IOException If an IO error occurs
     */
    public WebResponse getResponse(final WebRequestSettings webRequestSettings)
        throws IOException {
        
        final URL url = webRequestSettings.getURL();
        final FormEncodingType encType = webRequestSettings.getEncodingType();
        final SubmitMethod submitMethod = webRequestSettings.getSubmitMethod();
        final List parameters = webRequestSettings.getRequestParameters();
        final Map requestHeaders = webRequestSettings.getAdditionalHeaders();
        
        final HttpClient httpClient = getHttpClientFor( url );

        try {
            long startTime, endTime;

            HttpMethod httpMethod = makeHttpMethod( url, encType, submitMethod, parameters, requestHeaders );
            startTime = System.currentTimeMillis();
            int responseCode = httpClient.executeMethod( httpMethod );
            endTime = System.currentTimeMillis();
            return makeWebResponse( responseCode, httpMethod, url, endTime-startTime );
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
                newRequest.setSubmitMethod(submitMethod);
                newRequest.setRequestParameters(parameters);
                newRequest.setAdditionalHeaders(requestHeaders);
                return getResponse(newRequest);
            }
            else {
                e.printStackTrace();
                throw new RuntimeException( "HTTP Error: " + e.getMessage() );
            }
        }
    }

    private HttpMethod makeHttpMethod(
            final URL url,
            final FormEncodingType encType,
            final SubmitMethod method,
            final List parameters,
            final Map requestHeaders )
        throws
            IOException {

        final HttpMethod httpMethod;
        String path = url.getPath();
        if( path.length() == 0 ) {
            path = "/";
        }
        if( method == SubmitMethod.GET ) {
            httpMethod = new GetMethod( path );
            if( parameters.isEmpty() ) {
                final String queryString = url.getQuery();
                httpMethod.setQueryString( queryString );
            }
            else {
                final NameValuePair[] pairs = new NameValuePair[parameters.size()];
                parameters.toArray( pairs );
                httpMethod.setQueryString( pairs );
            }
        }
        else if( method == SubmitMethod.POST ) {
            httpMethod = new PostMethod( path );
            final String queryString = url.getQuery();
            if( queryString != null ) {
                httpMethod.setQueryString(queryString);
            }
            Iterator iterator;

            // Note that this has to be done in two loops otherwise it won't
            // be able to support two elements with the same name.
            iterator = parameters.iterator();
            if (encType == FormEncodingType.URL_ENCODED) {
                while( iterator.hasNext() ) {
                    final NameValuePair pair = ( NameValuePair )iterator.next();
                    ( ( PostMethod )httpMethod ).removeParameter( pair.getName(), pair.getValue() );
                }

                iterator = parameters.iterator();
                while( iterator.hasNext() ) {
                    final NameValuePair pair = ( NameValuePair )iterator.next();
                    ( ( PostMethod )httpMethod ).addParameter( pair.getName(), pair.getValue() );
                }
            }
            else {
                final List partList = new ArrayList();
                iterator = parameters.iterator();
                while (iterator.hasNext()) {
                    final Part newPart;
                    final KeyValuePair pair = (KeyValuePair) iterator.next();
                    if (pair instanceof KeyDataPair) {
                        final KeyDataPair pairWithFile = (KeyDataPair) pair;
                        newPart = new FilePart(
                                pairWithFile.getName(),
                                pairWithFile.getValue(),
                                pairWithFile.getFile(),
                                pairWithFile.getContentType(),
                                pairWithFile.getCharset());
                    } 
                    else {
                        newPart = new StringPart(pair.getName(), pair.getValue());
                    }
                    partList.add(newPart);
                }
                Part[] parts = new Part[partList.size()];
                parts = (Part[]) partList.toArray(parts);
                ((PostMethod) httpMethod).setRequestEntity(new MultipartRequestEntity(
                        parts,
                        httpMethod.getParams()));
            }
        }
        else {
            throw new IllegalStateException( "Submit method not yet supported: " + method );
        }

        httpMethod.setRequestHeader(
                "User-Agent", getWebClient().getBrowserVersion().getUserAgent() );

        writeRequestHeadersToHttpMethod( httpMethod, requestHeaders );
        httpMethod.setFollowRedirects(false);
        // http://jakarta.apache.org/commons/httpclient/3.0/exception-handling.html#Automatic%20exception%20recovery
        final HttpMethodRetryHandler noAutoRetry = new HttpMethodRetryHandler() {
            public boolean retryMethod(final HttpMethod arg0, final IOException arg1, final int arg2) {
                return false;
            }
        };
        httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, noAutoRetry);
        return httpMethod;
    }


    private synchronized HttpClient getHttpClientFor( final URL url ) {
        final String key = url.getProtocol() + "://" + url.getHost().toLowerCase();

        HttpClient client = ( HttpClient )httpClients_.get( key );
        if( client == null ) {
            client = new HttpClient();

            // Disable informational messages from httpclient
            final Log log = LogFactory.getLog("httpclient.wire");
            if( log instanceof SimpleLog ) {
                ((SimpleLog)log).setLevel( SimpleLog.LOG_LEVEL_WARN );
            }

            // Tell the client where to get its credentials from.
            client.getParams().setParameter( CredentialsProvider.PROVIDER, getWebClient().getCredentialsProvider() );

            final HostConfiguration hostConfiguration = new HostConfiguration();
            final URI uri;
            try {
                uri = new URI(url.toExternalForm(), false);
            }
            catch( final URIException e ) {
                // Theoretically impossible but ....
                throw new IllegalStateException("Unable to create URI from URL: "+url.toExternalForm());
            }
            hostConfiguration.setHost(uri);
            if( getProxyHost() != null ) {
                hostConfiguration.setProxy( getProxyHost(), getProxyPort() );
            }
            client.setHostConfiguration(hostConfiguration);
            final int timeout = this.getWebClient().getTimeout();
            client.getHttpConnectionManager().getParams().setSoTimeout(timeout);
            client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);

            // If two clients are part of the same domain then they should share the same
            // state (ie cookies)
            final HttpState sharedState = getStateForUrl( url );
            if( sharedState != null ) {
                client.setState(sharedState);
            }
            httpClients_.put( key, client );
        }
        return client;
    }



    /**
     * Return the log object for this class
     * @return The log object
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }


    /**
     * Return the {@link HttpState} that is being used for a given domain
     * @param url The url from which the domain will be determined
     * @return The state or null if no state can be found for this domain.
     */
    public synchronized HttpState getStateForUrl( final URL url ) {
        final String domain = url.getHost().toLowerCase();
        int index = domain.lastIndexOf('.');
        if( index != -1 ) {
            index = domain.lastIndexOf(".", index-1);
        }
        final String rootDomain;
        if( index == -1 ) {
            rootDomain = domain;
        }
        else {
            rootDomain = domain.substring(index+1);
        }

        final Iterator iterator = httpClients_.entrySet().iterator();
        while( iterator.hasNext() ) {
            final Map.Entry entry = (Map.Entry)iterator.next();
            final String key = (String)entry.getKey();
            final String host = key.substring(key.indexOf("://") + 3);
            if( host.equals(rootDomain) || host.endsWith("."+rootDomain) ) {
                return ((HttpClient)entry.getValue()).getState();
            }
        }

        return null;
    }


    private WebResponse makeWebResponse(
        final int statusCode, final HttpMethod method, final URL originatingURL, final long loadTime ) 
        throws IOException {
        
        // determine charset
        final String contentCharSet;
        if (method instanceof HttpMethodBase) {
            contentCharSet = ((HttpMethodBase) method).getResponseCharSet();
        }
        else {
            contentCharSet = "ISO-8859-1";
        }

        // HttpMethod.getResponseBodyAsStream may return null if no body is available
        final InputStream bodyStream = method.getResponseBodyAsStream();
        final String content;
        if (bodyStream == null) {
            content = "";
        }
        else {
            content = IOUtils.toString(bodyStream, contentCharSet);
        }

        return new WebResponse() {
            private String content_ = content;
            private String contentCharSet_ = contentCharSet;

            public int getStatusCode() {
                return statusCode;
            }

            public String getStatusMessage() {
                String message = method.getStatusText();
                if( message == null || message.length() == 0 ) {
                    message = HttpStatus.getStatusText( statusCode );
                }

                if( message == null ) {
                    message = "Unknown status code";
                }

                return message;
            }

            public String getContentType() {
                final Header contentTypeHeader  = method.getResponseHeader( "content-type" );
                if( contentTypeHeader == null ) {
                    // Not technically legal but some servers don't return a content-type
                    return "";
                }
                final String contentTypeHeaderLine = contentTypeHeader.getValue();
                final int index = contentTypeHeaderLine.indexOf( ';' );
                if( index == -1 ) {
                    return contentTypeHeaderLine;
                }
                else {
                    return contentTypeHeaderLine.substring( 0, index );
                }
            }

            public String getContentAsString() {
                return content_;
            }

            public InputStream getContentAsStream() {
                return new ByteArrayInputStream(getResponseBody());
            }

            public URL getUrl() {
                return originatingURL;
            }

            public List getResponseHeaders() {
                final List headers = new ArrayList();
                final Header[] array = method.getResponseHeaders();
                for( int i = 0; i < array.length; i++ ) {
                    headers.add( new NameValuePair( array[i].getName(), array[i].getValue() ) );
                }
                return headers;
            }

            public String getResponseHeaderValue( final String headerName ) {
                final Header header = method.getResponseHeader(headerName);
                if( header == null ) {
                    return null;
                }
                else {
                    return header.getValue();
                }
            }

            public long getLoadTimeInMilliSeconds() {
                return loadTime;
            }

            public String getContentCharSet(){
                return contentCharSet_;
            }

            public byte [] getResponseBody() {
                try {
                    return content_.getBytes(getContentCharSet());
                }
                catch (final UnsupportedEncodingException e) {
                    // should never occur
                    throw new RuntimeException(e);
                }
            }

        };
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

