/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
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

        final HttpClient httpClient = getHttpClientFor( url );

        try {
            long startTime, endTime;

            HttpMethod httpMethod = makeHttpMethod( url, submitMethod, parameters, httpClient, requestHeaders );
            startTime = System.currentTimeMillis();
            int responseCode = httpClient.executeMethod( httpMethod );
            endTime = System.currentTimeMillis();
            if( responseCode == 401 ) {    // Authentication required
                final KeyValuePair pair = getCredentials( httpMethod, httpClient, url );
                if( pair != null ) {
                    httpMethod = makeHttpMethod( url, submitMethod, parameters, httpClient, requestHeaders );
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
            final HttpMethod httpMethod, final HttpClient httpClient, final URL url ) {

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
            final SubmitMethod method,
            final List parameters,
            final HttpClient httpClient,
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
            final Iterator iterator = parameters.iterator();
            while( iterator.hasNext() ) {
                final NameValuePair pair = ( NameValuePair )iterator.next();
                ( ( PostMethod )httpMethod ).setParameter( pair.getName(), pair.getValue() );
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
            client.setState(
                new HttpState() {
                    public void setCredentials(
                        final String realm, final Credentials credentials ) {
                    }


                    public Credentials getCredentials( final String realm ) {
                        return null;
                    }
                } );

            if( getProxyHost() == null ) {
                client.startSession( url );
            }
            else {
                final boolean isSecure = url.getProtocol().equals( "https" );

                final String host = url.getHost();
                final int port = url.getPort();
                client.startSession( host, port, getProxyHost(), getProxyPort(), isSecure );
            }

            // If two clients are part of the same domain then they should share the same
            // state (ie cookies)
            final HttpState sharedState = getStateForDomain( url.getHost() );
            if( sharedState != null ) {
                client.setState(sharedState);
            }
            httpClients_.put( key, client );
        }
        return client;
    }


    private synchronized HttpState getStateForDomain( final String domain ) {
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

                public String getContentAsString() throws IOException {
                    return contentAsString;
                }

                public InputStream getContentAsStream() throws IOException {
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

