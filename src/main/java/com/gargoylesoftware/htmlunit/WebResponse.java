/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;

/**
 * A response from a web server.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public interface WebResponse {

    /**
     * Returns the request settings used to load this response.
     * @return the request settings used to load this response
     */
    WebRequestSettings getRequestSettings();

    /**
     * Returns the method used for the request resulting in this response.
     * @return the method used for the request resulting in this response
     * @deprecated As of 2.6, please use {@link #getRequestSettings()}.getHttpMethod()
     */
    @Deprecated
    HttpMethod getRequestMethod();

    /**
     * Returns the URL that was used to load this page.
     * @return the URL that was used to load this page
     * @deprecated As of 2.6, please use {@link #getRequestSettings()}.getUrl()
     */
    @Deprecated
    URL getRequestUrl();

    /**
     * Returns the response headers as a list of {@link NameValuePair}s.
     * @return the response headers as a list of {@link NameValuePair}s
     */
    List<NameValuePair> getResponseHeaders();

    /**
     * Returns the value of the specified response header.
     * @param headerName the name of the header whose value is to be returned
     * @return the value of the specified response header
     */
    String getResponseHeaderValue(final String headerName);

    /**
     * Returns the status code that was returned by the server.
     * @return the status code that was returned by the server
     */
    int getStatusCode();

    /**
     * Returns the status message that was returned from the server.
     * @return the status message that was returned from the server
     */
    String getStatusMessage();

    /**
     * Returns the content type returned from the server, i.e. "text/html".
     * @return the content type returned from the server, i.e. "text/html"
     */
    String getContentType();

    /**
     * Returns the content charset. can be null
     * @return the content charset
     * @deprecated As of 2.6, please use @link {@link #getContentCharset()}
     */
    @Deprecated
    String getContentCharSet();

    /**
     * Returns the content charset if specified in the header or from the content.
     * @return the content charset, null if can not be detected
     */
    String getContentCharsetOrNull();

    /**
     * Returns the content charset. The charset returned by this method is a valid.
     * Supported charset is determined from the "Content-Type" header, if not from the response content,
     * if not from request charset, if not uses {@link TextUtil#DEFAULT_CHARSET}
     * @return the content charset
     */
    String getContentCharset();

    /**
     * Returns the content from the server as a string, using the charset/encoding
     * specified in the server response.
     * @return the content from the server as a string
     */
    String getContentAsString();

    /**
     * Returns the content from the server as a string, using the specified charset/encoding,
     * rather than the charset/encoding specified in the server response. If the specified
     * charset/encoding is not supported, the default system encoding is used.
     * @param encoding the charset/encoding to use the convert the content into a string
     * @return the content from the server as a string
     */
    String getContentAsString(String encoding);

    /**
     * Returns the content from the server as an input stream.
     * @return the content from the server as an input stream
     * @exception IOException if an IO problem occurs
     */
    InputStream getContentAsStream() throws IOException;

    /**
     * Returns the content from the server as a byte array.
     * @return the content from the server as a byte array
     */
    byte[] getContentAsBytes();

    /**
     * Returns the time it took to load this web response, in milliseconds.
     * @return the time it took to load this web response, in milliseconds
     */
    long getLoadTime();

    /**
     * Returns the response body, as a byte array.
     * @return the response body, as a byte array
     * @deprecated since 2.4, please use {@link #getContentAsBytes()} instead
     */
    @Deprecated
    byte[] getResponseBody();
}
