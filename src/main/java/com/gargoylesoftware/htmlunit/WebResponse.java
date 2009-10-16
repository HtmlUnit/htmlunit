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
import java.io.Serializable;
import java.net.URL;
import java.util.List;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * A response from a web server.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public interface WebResponse extends Serializable {

    /**
     * Returns the request settings used to load this response.
     * @return the request settings used to load this response
     */
    WebRequestSettings getRequestSettings();

    /**
     * Returns the method used for the request resulting in this response.
     * @return the method used for the request resulting in this response
     * @deprecated as of 2.6, please use {@link #getRequestSettings()}.getHttpMethod()
     */
    @Deprecated
    HttpMethod getRequestMethod();

    /**
     * Returns the URL that was used to load this page.
     * @return the URL that was used to load this page
     * @deprecated as of 2.6, please use {@link #getRequestSettings()}.getUrl()
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
     * @return the header value, <code>null</code> if no response header exists with this name
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
     * Returns the content type returned from the server, e.g. "text/html".
     * @return the content type returned from the server, e.g. "text/html"
     */
    String getContentType();

    /**
     * Returns the content charset; may be <tt>null</tt>.
     * @return the content charset
     * @deprecated as of 2.6, please use {@link #getContentCharset()}
     */
    @Deprecated
    String getContentCharSet();

    /**
     * Returns the content charset specified explicitly in the header or in the content,
     * or <tt>null</tt> if none was specified.
     * @return the content charset specified explicitly in the header or in the content,
     *         or <tt>null</tt> if none was specified
     */
    String getContentCharsetOrNull();

    /**
     * Returns the content charset for this response, even if no charset was specified explicitly.
     * This method always returns a valid charset. This method first checks the "Content-Type"
     * header; if not found, it checks the response content; as a last resort, this method
     * returns {@link TextUtil#DEFAULT_CHARSET}.
     * @return the content charset for this response
     */
    String getContentCharset();

    /**
     * Returns the response content as a string, using the charset/encoding specified in the server response.
     * @return the response content as a string, using the charset/encoding specified in the server response
     */
    String getContentAsString();

    /**
     * Returns the response content as a string, using the specified charset/encoding,
     * rather than the charset/encoding specified in the server response. If the specified
     * charset/encoding is not supported then the default system encoding is used.
     * @param encoding the charset/encoding to use to convert the response content into a string
     * @return the response content as a string
     */
    String getContentAsString(String encoding);

    /**
     * Returns the response content as an input stream.
     * @return the response content as an input stream
     * @exception IOException if an IO problem occurs
     */
    InputStream getContentAsStream() throws IOException;

    /**
     * Returns the response content as a byte array.
     * @return the response content as a byte array
     */
    byte[] getContentAsBytes();

    /**
     * Returns the time it took to load this web response, in milliseconds.
     * @return the time it took to load this web response, in milliseconds
     */
    long getLoadTime();
}
