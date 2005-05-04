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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A fake WebConnection designed to mock out the actual http connections.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author Marc Guillemot
 */
public class MockWebConnection extends WebConnection {
    private class ResponseEntry {
        private void validateParameters(
                final Object content, 
                final String statusMessage, 
                final String contentType,
                final List responseHeaders) {
            Assert.notNull("content", content);
            Assert.notNull("statusMessage", statusMessage);
            Assert.notNull("contentType", contentType);
            Assert.notNull("responseHeaders", responseHeaders);
            // Validate that the response header list only contains KeyValuePairs
            final Iterator iterator = responseHeaders.iterator();
            while( iterator.hasNext() ) {
                final Object object = iterator.next();
                if( object instanceof KeyValuePair == false ) {
                    throw new IllegalArgumentException(
                            "Only KeyValuePairs may be in the response header list but found: "
                            + object.getClass().getName());
                }
            }               
        }          

        /**
         * Create a new instance
         * @param content The content as a byte array
         * @param statusCode The status code
         * @param statusMessage The status message
         * @param contentType The content type
         * @param responseHeaders a list of response headers
         */
        public ResponseEntry(
                final byte[] content,
                final int statusCode,
                final String statusMessage,
                final String contentType,
                final List responseHeaders ) {

            validateParameters(content, statusMessage, contentType, responseHeaders);
            content_ = content;
            statusCode_ = statusCode;
            statusMessage_ = statusMessage;
            contentType_ = contentType;
            responseHeaders_ = Collections.unmodifiableList(responseHeaders);
        }

        private final int statusCode_;
        private final String statusMessage_;
        private final byte[] content_;
        private final String contentType_;
        private final List responseHeaders_;
    }
    
    private byte[] stringToByteArray(final String content) {
        byte[] contentBytes;
        try {
            contentBytes = content.getBytes("ISO-8859-1");
        }
        catch (final UnsupportedEncodingException e) {
            contentBytes = new byte[0];
        }
        return contentBytes;
    }    

    private final Map responseMap_ = new HashMap(10);
    private ResponseEntry defaultResponseEntry_;

    private SubmitMethod lastMethod_;
    private List lastParameters_;
    private HttpState httpState_ = new HttpState();

    /**
     *  Create an instance
     *
     * @param  webClient The web client
     */
    public MockWebConnection( final WebClient webClient ) {
        super( webClient );
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
     * @param  webRequestSettings Settings to make the request with
     * @return  See above
     * @throws IOException (only for extending classes)
     */
    public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException {
        final URL url = webRequestSettings.getURL();
        final SubmitMethod method = webRequestSettings.getSubmitMethod();
        final List parameters = webRequestSettings.getRequestParameters();
        
        getLog().debug("Getting response for " + url.toExternalForm());
        
        lastMethod_ = method;
        lastParameters_ = parameters;

        ResponseEntry entry = (ResponseEntry)responseMap_.get(url.toExternalForm());
        if( entry == null ) {
            entry = defaultResponseEntry_;
            if( entry == null ) {
                throw new IllegalStateException(
                    "No response specified that can handle url ["+url.toExternalForm()+"]");
            }
        }

        final ResponseEntry responseEntry = entry;
        return new WebResponse() {
            public int getStatusCode()         { return responseEntry.statusCode_;      }
            public String getStatusMessage()   { return responseEntry.statusMessage_;   }
            public String getContentType()     { 
                final String contentTypeHeaderLine = responseEntry.contentType_;
                final int index = contentTypeHeaderLine.indexOf( ';' );
                final String contentType;
                if( index == -1 ) {
                    contentType = contentTypeHeaderLine;
                }
                else {
                    contentType = contentTypeHeaderLine.substring( 0, index );
                }
                return contentType;
            }
            public String getContentAsString() { 
                try {
                    return new String(responseEntry.content_, getContentCharSet());
                }
                catch (final UnsupportedEncodingException e) {
                    return null;
                }
            }
            public URL getUrl()                { return url;                           }
            public List getResponseHeaders()   { return responseEntry.responseHeaders_; }
            public long getLoadTimeInMilliSeconds() { return 0; }

            public String getResponseHeaderValue( final String headerName ) {
                final Iterator iterator = responseEntry.responseHeaders_.iterator();
                while( iterator.hasNext() ) {
                    final KeyValuePair pair = (KeyValuePair)iterator.next();
                    if( pair.getKey().equals( headerName ) ) {
                        return pair.getValue();
                    }
                }
                return null;
            }

            public InputStream getContentAsStream() {
                return new ByteArrayInputStream(responseEntry.content_);
            }
            public byte[] getResponseBody() {
                /*
                 * this method must return raw bytes.
                 * without encoding, getBytes use locale encoding.
                 */
                return responseEntry.content_;
            }
            public String getContentCharSet() {
                final String contentType = responseEntry.contentType_;
                final String prefix = "charset=";
                final int index = contentType.indexOf(prefix);
                final String charset;
                if( index == -1 ) {
                    charset = "ISO-8859-1";
                }
                else {
                    charset = contentType.substring(index+prefix.length());
                }
                return charset;
            }
        };
    }


    /**
     *  Return the method that was used in the last call to submitRequest()
     *
     * @return  See above
     */
    public SubmitMethod getLastMethod() {
        return lastMethod_;
    }


    /**
     *  Return the parameters that were used in the last call to submitRequest()
     *
     * @return  See above
     */
    public List getLastParameters() {
        return lastParameters_;
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
    public void setResponse(
            final URL url,
            final String content,
            final int statusCode,
            final String statusMessage,
            final String contentType,
            final List responseHeaders ) {

        setResponse(url, stringToByteArray(content), statusCode, statusMessage, 
                    contentType, responseHeaders);
        
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
    public void setResponse(
            final URL url,
            final byte[] content,
            final int statusCode,
            final String statusMessage,
            final String contentType,
            final List responseHeaders ) {

        final ResponseEntry responseEntry =
            new ResponseEntry(content, statusCode, statusMessage, contentType, responseHeaders);
        responseMap_.put( url.toExternalForm(), responseEntry );
    }    

    /**
     * Convenience method that is the same as calling
     * {@link #setResponse(URL,String,int,String,String,List)} with a status
     * of "200 OK", a content type of "text/html" and no additonal headers.
     *
     * @param url The url that will return the given response
     * @param content The content to return
     */
    public void setResponse( final URL url, final String content ) {
        setResponse( url, content, 200, "OK", "text/html", Collections.EMPTY_LIST );
    }

    /**
     * Convenience method that is the same as calling
     * {@link #setResponse(URL,String,int,String,String,List)} with a status
     * of "200 OK" and no additonal headers.
     *
     * @param url The url that will return the given response
     * @param content The content to return
     * @param contentType The content type to return
     */
    public void setResponse(final URL url, final String content, final String contentType) {
        setResponse( url, content, 200, "OK", contentType, Collections.EMPTY_LIST );
    }

    /**
     * Specify a generic html page that will be returned when the given url is specified.
     * The page will contain only minimal html to satisfy the html parser but will contain
     * the specified title so that tests can check for titleText.
     *
     * @param url The url that will return the given response
     * @param title The title of the page
     */
    public void setResponseAsGenericHtml(
            final URL url,
            final String title ) {

        final String content = "<html><head><title>"+title+"</title></head><body></body></html>";
        final ResponseEntry responseEntry
            = new ResponseEntry(stringToByteArray(content), 200, "OK", "text/html", Collections.EMPTY_LIST);
        responseMap_.put( url.toExternalForm(), responseEntry );
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
    public void setDefaultResponse(
            final String content,
            final int statusCode,
            final String statusMessage,
            final String contentType ) {

        setDefaultResponse(stringToByteArray(content), statusCode, statusMessage, contentType);
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
    public void setDefaultResponse(
            final byte[] content,
            final int statusCode,
            final String statusMessage,
            final String contentType ) {

        defaultResponseEntry_ = new ResponseEntry(content, statusCode, statusMessage,
                contentType, Collections.EMPTY_LIST);
    }

    /**
     * Set the response that will be returned when a url is requested that does
     * not have a specific content set for it.
     *
     * @param content The content to return
     */
    public void setDefaultResponse( final String content ) {
        setDefaultResponse(content, 200, "OK", "text/html");
    }


    /**
     * Return the {@link HttpState} that is being used for a given domain
     * @param url The url from which the domain will be determined
     * @return The state or null if no state can be found for this domain.
     */
    public HttpState getStateForUrl( final URL url ) {
        return httpState_;
    }
}

