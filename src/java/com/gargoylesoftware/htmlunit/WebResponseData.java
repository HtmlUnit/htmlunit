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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Simple data object to simplify WebResponse creation.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class WebResponseData implements Serializable {

    private static final long serialVersionUID = 2979956380280496543L;

    private byte[] body_;
    private int statusCode_;
    private String statusMessage_;
    private List responseHeaders_;

    /**
     * Construct with a raw byte[] (mostly for testing)
     *
     * @param body              Body of this response
     * @param statusCode        Status code from the server
     * @param statusMessage     Status message from the server
     * @param responseHeaders   Headers in this response
     */
    public WebResponseData(final byte[] body, final int statusCode, final String statusMessage,
            final List responseHeaders) {

        validateHeaders(responseHeaders);
        statusCode_ = statusCode;
        statusMessage_ = statusMessage;
        responseHeaders_ = Collections.unmodifiableList(responseHeaders);

        try {
            body_ = getBody(new ByteArrayInputStream(body), responseHeaders);
        }
        catch (final IOException e) {
            body_ = body;
        }
    }

    /**
     * Construct with a data stream to minimize copying of the entire body.
     *
     * @param bodyStream        Stream of this response's body
     * @param statusCode        Status code from the server
     * @param statusMessage     Status message from the server
     * @param responseHeaders   Headers in this response
     *
     * @throws IOException on stream errors
     */
    public WebResponseData(final InputStream bodyStream, final int statusCode,
            final String statusMessage, final List responseHeaders) throws IOException {

        validateHeaders(responseHeaders);
        statusCode_ = statusCode;
        statusMessage_ = statusMessage;
        responseHeaders_ = Collections.unmodifiableList(responseHeaders);
        body_ = getBody(bodyStream, responseHeaders);
    }

    /**
     * Construct without data stream for subclasses that override getBody()
     *
     * @param statusCode        Status code from the server
     * @param statusMessage     Status message from the server
     * @param responseHeaders   Headers in this response
     *
     * @throws IOException on stream errors
     */
    protected WebResponseData(final int statusCode,
            final String statusMessage, final List responseHeaders) throws IOException {

        validateHeaders(responseHeaders);
        statusCode_ = statusCode;
        statusMessage_ = statusMessage;
        responseHeaders_ = Collections.unmodifiableList(responseHeaders);
    }
    
    /**
     * Validate that the response header list only contains KeyValuePairs
     * (Java5 generics will obsolete this method)
     *
     * @param responseHeaders Header list to be validated
     */
    private void validateHeaders(final List responseHeaders) {
        final Iterator iterator = responseHeaders.iterator();
        while (iterator.hasNext()) {
            final Object object = iterator.next();
            if (object instanceof NameValuePair == false) {
                final String name = object.getClass().getName();
                final String msg = "Only NameValuePairs may be in the response header list, but found a " + name + ".";
                throw new IllegalArgumentException(msg);
            }
        }
    }

    /**
     * Returns the body byte array contained by the specified input stream.
     * If the response headers indicate that the data has been compressed,
     * the data stream is handled appropriately. If the specified stream is
     * <tt>null</tt>, this method returns <tt>null</tt>.
     * @param stream The input stream which contains the body.
     * @param headers The response headers.
     * @return The specified body stream, as a byte array.
     * @throws IOException If a stream error occurs.
     */
    protected byte[] getBody(InputStream stream, final List headers) throws IOException {
        if (stream == null) {
            return null;
        }
        String encoding = null;
        for (final Iterator i = headers.iterator(); i.hasNext();) {
            final NameValuePair header = (NameValuePair) i.next();
            final String headerName = header.getName().trim();
            if (headerName.equalsIgnoreCase("content-encoding")) {
                encoding = header.getValue();
                break;
            }
        }
        if (encoding != null) {
            if (StringUtils.contains(encoding, "gzip")) {
                stream = new GZIPInputStream(stream);
            }
        }
        return IOUtils.toByteArray(stream);
    }

    /**
     * Return the response body.
     * @return response body.
     */
    public byte[] getBody() {
        return body_;
    }

    /**
     *
     * @return response headers
     */
    public List getResponseHeaders() {
        return responseHeaders_;
    }

    /**
     * @return response status code
     */
    public int getStatusCode() {
        return statusCode_;
    }

    /**
     * @return response status message
     */
    public String getStatusMessage() {
        return statusMessage_;
    }

}
