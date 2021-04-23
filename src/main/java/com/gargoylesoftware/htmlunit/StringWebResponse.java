/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.TextUtils;

/**
 * A simple WebResponse created from a string. Content is assumed to be of type <tt>text/html</tt>.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Brad Clarke
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Carsten Steul
 */
public class StringWebResponse extends WebResponse {

    private boolean fromJavascript_;

    /**
     * Creates an instance associated with the specified originating URL.
     * @param content the content to return
     * @param originatingURL the URL that this should be associated with
     */
    public StringWebResponse(final String content, final URL originatingURL) {
        // use UTF-8 here to be sure, all chars in the string are part of the charset
        this(content, UTF_8, originatingURL);
    }

    /**
     * Creates an instance associated with the specified originating URL.
     * @param content the content to return
     * @param charset the charset used to convert the content
     * @param originatingURL the URL that this should be associated with
     */
    public StringWebResponse(final String content, final Charset charset, final URL originatingURL) {
        super(getWebResponseData(content, charset), buildWebRequest(originatingURL, charset), 0);
    }

    /**
     * Helper method for constructors. Converts the specified string into {@link WebResponseData}
     * with other defaults specified.
     *
     * @param contentString the string to be converted to a <tt>WebResponseData</tt>
     * @return a simple <tt>WebResponseData</tt> with defaults specified
     */
    private static WebResponseData getWebResponseData(final String contentString, final Charset charset) {
        final byte[] content = TextUtils.stringToByteArray(contentString, charset);
        final List<NameValuePair> compiledHeaders = new ArrayList<>();
        compiledHeaders.add(new NameValuePair(HttpHeader.CONTENT_TYPE, "text/html; charset=" + charset));
        return new WebResponseData(content, HttpStatus.SC_OK, "OK", compiledHeaders);
    }

    private static WebRequest buildWebRequest(final URL originatingURL, final Charset charset) {
        final WebRequest webRequest = new WebRequest(originatingURL, HttpMethod.GET);
        webRequest.setCharset(charset);
        return webRequest;
    }

    /**
     * Returns the fromJavascript property. This is true, if the response was created
     * from javascript (usually document.write).
     * @return the from fromJavascript_
     */
    public boolean isFromJavascript() {
        return fromJavascript_;
    }

    /**
     * Sets the fromJavascript_ property. Set this to true, if the response was created
     * from javascript (usually document.write).
     * @param fromJavascript the new fromJavascript
     */
    public void setFromJavascript(final boolean fromJavascript) {
        fromJavascript_ = fromJavascript;
    }
}
