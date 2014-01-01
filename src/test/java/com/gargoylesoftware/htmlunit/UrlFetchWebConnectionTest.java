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

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Unit tests for {@link UrlFetchWebConnection}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Pieter Herroelen
 */
@RunWith(BrowserRunner.class)
public class UrlFetchWebConnectionTest extends WebServerTestCase {

    /**
     * Tests a simple GET.
     * @throws Exception if the test fails
     */
    @Test
    public void get() throws Exception {
        doGetTest(getDefaultUrl());
    }

    /**
     * GET with query parameters.
     * @throws Exception if the test fails
     */
    @Test
    public void get_withQueryParameters() throws Exception {
        final URL url = new URL(getDefaultUrl() + "?a=b&c=d&e=f");
        doGetTest(url);
    }

    private void doGetTest(final URL url) throws Exception {
        // get with default WebConnection
        getMockWebConnection().setDefaultResponse("");
        loadPage("", url);
        final WebRequest referenceRequest = getMockWebConnection().getLastWebRequest();

        // get with UrlFetchWebConnection
        final WebClient wc = getWebClient();
        wc.setWebConnection(new UrlFetchWebConnection(wc));
        wc.getPage(url);
        final WebRequest newRequest = getMockWebConnection().getLastWebRequest();

        // compare the two requests
        compareRequests(referenceRequest, newRequest);
    }

    /**
     * Simple POST url-encoded.
     * @throws Exception if the test fails
     */
    @Test
    public void post() throws Exception {
        final WebRequest referenceRequest = getPostRequest(FormEncodingType.URL_ENCODED);

        getWebClient().setWebConnection(new UrlFetchWebConnection(getWebClient()));
        final WebRequest newRequest = getPostRequest(FormEncodingType.URL_ENCODED);

        // compare the two requests
        compareRequests(referenceRequest, newRequest);
    }

    private WebRequest getPostRequest(final FormEncodingType encoding) throws Exception {
        final String html = "<html><body><form action='foo' method='post' enctype='" + encoding.getName() + "'>\n"
            + "<input name='text1' value='me &amp;amp; you'>\n"
            + "<textarea name='text2'>Hello\nworld!</textarea>\n"
            + "<input type='submit' id='submit'>\n"
            + "</form></body></html>";

        getMockWebConnection().setDefaultResponse("");
        final HtmlPage page = loadPage(html, getDefaultUrl());
        page.getHtmlElementById("submit").click();
        return getMockWebConnection().getLastWebRequest();
    }

    /**
     * Simple POST multipart.
     * This doesn't work currently and the test should be reworked as the boundary for the body varies.
     * @throws Exception if the test fails
     */
    @NotYetImplemented
    @Test
    public void post_multipart() throws Exception {
        final WebRequest referenceRequest = getPostRequest(FormEncodingType.MULTIPART);

        getWebClient().setWebConnection(new UrlFetchWebConnection(getWebClient()));
        final WebRequest newRequest = getPostRequest(FormEncodingType.MULTIPART);

        // compare the two requests
        compareRequests(referenceRequest, newRequest);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("my_key=Hello")
    public void cookie() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<NameValuePair>();
        responseHeader.add(new NameValuePair("Set-Cookie", "my_key=Hello"));
        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE,
                200, "OK", "text/html", responseHeader);

        // verify expectations with "normal" HTMLUnit
        loadPageWithAlerts(getDefaultUrl());

        getWebClient().getCookieManager().clearCookies();
        getWebClient().setWebConnection(new UrlFetchWebConnection(getWebClient()));
        loadPageWithAlerts(getDefaultUrl());
    }

    static void compareRequests(final WebRequest referenceRequest, final WebRequest newRequest) {
        assertEquals(referenceRequest.getRequestBody(), newRequest.getRequestBody());
        assertEquals(referenceRequest.getCharset(), newRequest.getCharset());
        assertEquals(referenceRequest.getProxyHost(), newRequest.getProxyHost());
        assertEquals(referenceRequest.getUrl(), newRequest.getUrl());
        assertEquals(headersToString(referenceRequest), headersToString(newRequest));
        assertEquals(referenceRequest.getEncodingType(), newRequest.getEncodingType());
        assertEquals(referenceRequest.getHttpMethod(), newRequest.getHttpMethod());
        assertEquals(referenceRequest.getProxyPort(), newRequest.getProxyPort());
        assertEquals(referenceRequest.getRequestParameters().toString(), newRequest.getRequestParameters().toString());
    }

    private static String headersToString(final WebRequest request) {
        final Set<String> caseInsensitiveHeaders = new HashSet<String>(Arrays.asList("Connection"));

        final StringBuilder sb = new StringBuilder();
        // ensure ordering for comparison
        final Map<String, String> headers = new TreeMap<String, String>(request.getAdditionalHeaders());
        for (final Entry<String, String> headerEntry : headers.entrySet()) {
            sb.append(headerEntry.getKey());
            sb.append(": ");
            if (caseInsensitiveHeaders.contains(headerEntry.getKey())) {
                sb.append(headerEntry.getValue().toLowerCase(Locale.ENGLISH));
            }
            else {
                sb.append(headerEntry.getValue());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Tests that an empty string is parsed into zero cookies.
     */
    @Test
    public void emptyStringHasNoCookies() {
        assertEquals(0, UrlFetchWebConnection.parseCookies("www.foo.com", "").size());
    }

    /**
     * Tests that a string with one cookie is parsed into one cookie with the right name and value.
     */
    @Test
    public void oneCookieStringHasOneCookie() {
        final String cookieHeader = "Name=Value; expires=Fri, 18-Nov-2011 21:13:50 GMT";
        final Set<Cookie> cookies = UrlFetchWebConnection.parseCookies("www.foo.com", cookieHeader);
        final Cookie cookie = cookies.iterator().next();
        assertEquals(1, cookies.size());
        assertEquals("Name", cookie.getName());
        assertEquals("Value", cookie.getValue());
    }

    /**
     * Tests that a string with three cookies is parsed into three cookies.
     */
    @Test
    public void threeCookiesStringHasThreeCookies() {
        final String cookieHeader = ".ASPXANONYMOUS=sl7T9zamzAEkAAAAY2RmY2U1MWEtMzllYy00MDk1LThmNDMtM2U0MzBiMmEyMTFi0;"
            + " expires=Fri, 18-Nov-2011 21:13:50 GMT; path=/; HttpOnly, ASP.NET_SessionId=dqsvrc45gpj51f45n0c1q4qa;"
            + " path=/; HttpOnly, language=en-US; path=/; HttpOnly";
        assertEquals(3, UrlFetchWebConnection.parseCookies("www.foo.com", cookieHeader).size());
    }

    /**
     * Test that redirects are handled by the WebClient and not by the UrlFetchWebConnection (issue #3557486).
     * @throws Exception if the test fails.
     */
    @Test
    public void redirect() throws Exception {
        final String html = "<html></html>";

        // get with default WebConnection
        final List<NameValuePair> headers = Collections.singletonList(
                new NameValuePair("Location", URL_SECOND.toString()));
        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, "", 302, "Some error", "text/html", headers);
        conn.setResponse(URL_SECOND, html);

        Page page = loadPageWithAlerts(URL_FIRST);
        assertEquals(URL_SECOND, page.getUrl());

        // get with UrlFetchWebConnection
        final WebClient wc = getWebClient();
        wc.setWebConnection(new UrlFetchWebConnection(wc));

        page = loadPageWithAlerts(URL_FIRST);
        assertEquals(URL_SECOND, page.getUrl());
    }
}
