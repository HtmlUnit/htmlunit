/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
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
     * @param  parameters Any parameters
     * @param  url The url of the server
     * @param  submitMethod The submit method. Ie SubmitMethod.GET
     * @param  requestHeaders Any headers that need to be put in the request.
     * @return  See above
     * @exception  IOException If an IO error occurs
     */
    public WebResponse getResponse(
            final URL url,
            final SubmitMethod submitMethod,
            final List parameters,
            final Map requestHeaders )
        throws
            IOException {
        return this.getResponse(url, FormEncodingType.URL_ENCODED, submitMethod, parameters, requestHeaders);
    }

    /**
     *  Submit a request and retrieve a response
     *
     * @param  parameters Any parameters
     * @param  url The url of the server
     * @param  encType Encoding type of the form when done as a POST
     * @param  submitMethod The submit method. Ie SubmitMethod.GET
     * @param  requestHeaders Any headers that need to be put in the request.
     * @return  See above
     * @exception  IOException If an IO error occurs
     */
    public WebResponse getResponse(
            final URL url,
            final FormEncodingType encType,
            final SubmitMethod submitMethod,
            final List parameters,
            final Map requestHeaders )
        throws
            IOException {

        final HttpClient httpClient = getHttpClientFor( url );

        try {
            long startTime, endTime;

            HttpMethod httpMethod = makeHttpMethod( url, encType, submitMethod, parameters, requestHeaders );
            startTime = System.currentTimeMillis();
            int responseCode = httpClient.executeMethod( httpMethod );
            endTime = System.currentTimeMillis();
            if( responseCode == 401 ) {    // Authentication required
                final KeyValuePair pair = getCredentials( httpMethod, url );
                if( pair != null ) {
                    httpMethod = makeHttpMethod( url, encType, submitMethod, parameters, requestHeaders );
                    addCredentialsToHttpMethod( httpMethod, pair );
                    startTime = System.currentTimeMillis();
                    responseCode = httpClient.executeMethod( httpMethod );
                    endTime = System.currentTimeMillis();
                }
            }
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

                return getResponse( new URL(buffer.toString()), submitMethod, parameters, requestHeaders );
            }
            else {
                e.printStackTrace();
                throw new RuntimeException( "HTTP Error: " + e.getMessage() );
            }
        }
    }


    private KeyValuePair getCredentials(
            final HttpMethod httpMethod, final URL url ) {

        final Header challengeHeader = httpMethod.getResponseHeader( "WWW-Authenticate" );
        final String realmString = challengeHeader.getValue().trim();
        final String tag = "realm=\"";
        final int index = realmString.indexOf( tag );
        if( index == -1 || realmString.charAt( realmString.length() - 1 ) != '\"' ) {
            throw new IllegalStateException(
                "Unable to parse the 'WWW-Authenticate' header - can't do authentication" );
        }

        int port = url.getPort();
        if( port == -1 ) {
            final String protocol = url.getProtocol();
            if( protocol.equals( "http" ) ) {
                port = 80;
            }
            else if( protocol.equals( "https" ) ) {
                port = 443;
            }
            else {
                throw new IllegalStateException( "Unsupported protocol: " + protocol );
            }
        }

        final String realm
            = realmString.substring( index + tag.length(), realmString.length() - 1 );
        final KeyValuePair pair = getWebClient().getCredentialProvider().getCredentialsFor(
                url.getHost(), port, realm );
        return pair;
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
            if (encType == FormEncodingType.URL_ENCODED) {
                httpMethod = new PostMethod( path );
            }
            else {
                httpMethod = new MultipartPostMethod(path);
            }
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
                iterator = parameters.iterator();
                while (iterator.hasNext()) {
                    final KeyValuePair pair = (KeyValuePair) iterator.next();
                    if (pair instanceof KeyDataPair) {
                        ((MultipartPostMethod) httpMethod).addParameter(
                                pair.getName(), pair.getValue(), ((KeyDataPair) pair).getFile());
                    }
                    else {
                        ((MultipartPostMethod) httpMethod).addParameter(pair.getName(), pair.getValue());
                    }
                }
            }
        }
        else {
            throw new IllegalStateException( "Submit method not yet supported: " + method );
        }

        httpMethod.setRequestHeader(
                "User-Agent", getWebClient().getBrowserVersion().getUserAgent() );

        writeRequestHeadersToHttpMethod( httpMethod, requestHeaders );
        httpMethod.setFollowRedirects(false);
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

            // Disable certificate caching within HttpClient
            final HttpState httpState = new HttpState() {
                public void setCredentials(
                    final String realm, final String host, final Credentials credentials ) {
                }

                public Credentials getCredentials( final String realm, final String host ) {
                    return null;
                }
            };
            client.setState(httpState);
            httpState.setCookiePolicy( CookiePolicy.COMPATIBILITY );

            final HostConfiguration hostConfiguration = new HostConfiguration();
            final URI uri;
            try {
                uri = new URI(url.toExternalForm());
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
     * Return the {@link HttpState} that is being used for a given domain
     * @param url The url from which the domain will be determined
     * @return The state or null if no state can be found for this domain.
     */
    public synchronized HttpState getStateForUrl( final URL url ) {
        final String domain = url.getHost();
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
            final String host = (String)entry.getKey();
            if( host.equals(rootDomain) || host.endsWith("."+rootDomain) ) {
                return ((HttpClient)entry.getValue()).getState();
            }
        }

        return null;
    }


    private WebResponse makeWebResponse(
        final int statusCode, final HttpMethod method, final URL originatingURL, final long loadTime ) {

        final byte[] contentBuffer = method.getResponseBody();
        final String contentAsString = method.getResponseBodyAsString();

        return new WebResponse() {
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
                    return contentAsString;
                }

                public InputStream getContentAsStream() {
                    return new ByteArrayInputStream(contentBuffer);
                }

                public URL getUrl() {
                    return originatingURL;
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
                    if( method instanceof HttpMethodBase ){
                        return ((HttpMethodBase)method).getResponseCharSet();
                    }
                    else {
                        return "ISO-8859-1";
                    }
                }

                public byte [] getResponseBody(){
                    return contentBuffer;
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


    private void addCredentialsToHttpMethod(
            final HttpMethod httpMethod, final KeyValuePair pair ) {

        final String userName = pair.getKey();
        final String password = pair.getValue();

        String authString = userName + ":" + password;
        final String value = "Basic " + Base64.encode( authString );
        httpMethod.addRequestHeader( "Authorization", value );
    }
}

