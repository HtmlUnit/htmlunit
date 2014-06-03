/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests methods in {@link HttpWebConnection}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnection2Test extends WebDriverTestCase {

    /**
     * Tests a simple POST request.
     * @throws Exception if the test fails
     */
    @Test
    public void post() throws Exception {
        final String html = "<html><body><form action='foo' method='post' accept-charset='iso-8859-1'>\n"
            + "<input name='text1' value='me &amp;amp; you'>\n"
            + "<textarea name='text2'>Hello\nworld!</textarea>\n"
            + "<input type='submit' id='submit'>\n"
            + "</form></body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html, getDefaultUrl());
        driver.findElement(By.id("submit")).click();

        // better would be to look at the HTTP traffic directly
        // rather than to examine a request that has been rebuilt but...
        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();

        assertEquals("ISO-8859-1", lastRequest.getCharset());
        assertEquals(null, lastRequest.getProxyHost());
        assertEquals(null, lastRequest.getRequestBody());
        assertEquals(getDefaultUrl() + "foo", lastRequest.getUrl());
        final String expectedHeaders = ""
            + "Connection: keep-alive\n"
            + "Content-Length: 48\n"
            + "Content-Type: application/x-www-form-urlencoded\n"
            + "Host: localhost:" + PORT + "\n"
            + "Referer: http://localhost:" + PORT + "/\n"
            + "User-Agent: " + getBrowserVersion().getUserAgent() + "\n";
        assertEquals(expectedHeaders, headersToString(lastRequest));
        assertEquals(FormEncodingType.URL_ENCODED, lastRequest.getEncodingType());
        assertEquals(HttpMethod.POST, lastRequest.getHttpMethod());
        assertEquals(0, lastRequest.getProxyPort());
        final List<NameValuePair> parameters = lastRequest.getRequestParameters();
        assertEquals(2, parameters.size());
        for (final NameValuePair pair : parameters) {
            if ("text1".equals(pair.getName())) {
                assertEquals("me &amp; you", pair.getValue());
            }
            else {
                assertEquals("Hello\r\nworld!", pair.getValue());
            }
        }
    }

    private String headersToString(final WebRequest request) {
        // why doesn't HtmlUnit send these headers whereas Firefox does?
        final List<String> ignoredHeaders = Arrays.asList("accept", "accept-charset", "accept-encoding",
            "accept-language", "keep-alive");
        final List<String> caseInsensitiveHeaders = Arrays.asList("connection");

        // ensure ordering for comparison
        final Map<String, String> headers = new TreeMap<String, String>(request.getAdditionalHeaders());

        final StringBuilder sb = new StringBuilder();
        for (final Entry<String, String> headerEntry : headers.entrySet()) {
            final String headerName = headerEntry.getKey();
            final String headerNameLower = headerName.toLowerCase(Locale.ENGLISH);
            if (ignoredHeaders.contains(headerNameLower)) {
                continue;
            }
            sb.append(headerName);
            sb.append(": ");
            if (caseInsensitiveHeaders.contains(headerNameLower)) {
                sb.append(headerEntry.getValue().toLowerCase(Locale.ENGLISH));
            }
            else if ("user-agent".equals(headerNameLower)) {
                // ignore the 64bit difference
                sb.append(headerEntry.getValue().replace("WOW64; ", ""));
            }
            else {
                sb.append(headerEntry.getValue());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Test for broken gzip content.
     * @throws Exception if the test fails
     */
    @Test
    public void brokenGzip() throws Exception {
        final byte[] content = new byte[] {-1};
        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Content-Encoding", "gzip"));
        headers.add(new NameValuePair("Content-Length", String.valueOf(content.length)));

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, content, 404, "OK", "text/html", headers);

        // only check that no exception is thrown
        loadPageWithAlerts2(URL_FIRST);
    }
}
