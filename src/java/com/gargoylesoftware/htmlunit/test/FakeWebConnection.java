/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.test;

import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *  A fake HttpProcessor for testing purposes only
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class FakeWebConnection extends WebConnection {
    private class ResponseEntry {
        public ResponseEntry(
                final String content,
                final int statusCode,
                final String statusMessage,
                final String contentType,
                final List responseHeaders ) {

            assertNotNull("content", content);
            assertNotNull("statusMessage", statusMessage);
            assertNotNull("contentType", contentType);
            assertNotNull("responseHeaders", responseHeaders);

            content_ = content;
            statusCode_ = statusCode;
            statusMessage_ = statusMessage;
            contentType_ = contentType;
            responseHeaders_ = Collections.unmodifiableList(responseHeaders);
        }

        private final int statusCode_;
        private final String statusMessage_;
        private final String content_;
        private final String contentType_;
        private final List responseHeaders_;
    }

    private final Map responseMap_ = new HashMap(89);
    private ResponseEntry defaultResponseEntry_;

    private SubmitMethod lastMethod_;
    private List lastParameters_;


    /**
     *  Create an instance
     *
     * @param  webClient
     */
    public FakeWebConnection( final WebClient webClient ) {
        super( webClient );
    }


    /**
     *  Submit a request to the processor
     *
     * @param  url The url
     * @param  method The method to use
     * @param  parameters any parameters
     * @return  The response as an input stream
     */
    public WebResponse getResponse(
            final URL url,
            final SubmitMethod method,
            final List parameters,
            final Map requestParameters ) {

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
            public String getContentType()     { return responseEntry.contentType_;     }
            public String getContentAsString() { return responseEntry.content_;         }
            public URL getUrl()                { return url;                           }
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

            public InputStream getContentAsStream() throws IOException {
                return TextUtil.toInputStream(responseEntry.content_);
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
     *  Return the parametersthat were used in the last call to submitRequest()
     *
     * @return  See above
     */
    public List getLastParameters() {
        return lastParameters_;
    }


    public void setResponse(
            final URL url,
            final String content,
            final int statusCode,
            final String statusMessage,
            final String contentType,
            final List responseHeaders ) {

        final ResponseEntry responseEntry
            = new ResponseEntry(content, statusCode, statusMessage, contentType, responseHeaders);

        responseMap_.put( url.toExternalForm(), responseEntry );
    }


    public void setDefaultResponse(
            final String content,
            final int statusCode,
            final String statusMessage,
            final String contentType ) {

        defaultResponseEntry_
            = new ResponseEntry(content, statusCode, statusMessage,
                contentType, Collections.EMPTY_LIST);
    }


    /**
     * Set the default response entry to use the specified content with default values.
     */
    public void setContent( final String content ) {
        setDefaultResponse(content, 200, "OK", "text/html");
    }


    private void assertNotNull( final String description, final Object object ) {
        if( object == null ) {
            throw new NullPointerException(description);
        }
    }
}

