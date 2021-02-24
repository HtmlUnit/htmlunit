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

import static org.apache.http.client.utils.DateUtils.formatDate;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link Cache}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Anton Demydenko
 * @author Ronald Brill
*/
@RunWith(BrowserRunner.class)
public class CacheTest extends SimpleWebTestCase {

    /**
     * Test.
     */
    @Test
    public void isCacheableContent() {
        final Cache cache = new Cache();
        final Map<String, String> headers = new HashMap<>();
        final WebResponse response = new DummyWebResponse() {
            @Override
            public String getResponseHeaderValue(final String headerName) {
                return headers.get(headerName);
            }
        };

        assertFalse(cache.isCacheableContent(response));

        headers.put("Last-Modified", "Sun, 15 Jul 2007 20:46:27 GMT");
        assertTrue(cache.isCacheableContent(response));

        headers.put("Last-Modified", formatDate(DateUtils.addMinutes(new Date(), -5)));
        assertFalse(cache.isCacheableContent(response));

        headers.put("Expires", formatDate(DateUtils.addMinutes(new Date(), 5)));
        assertFalse(cache.isCacheableContent(response));

        headers.put("Expires", formatDate(DateUtils.addHours(new Date(), 1)));
        assertTrue(cache.isCacheableContent(response));

        headers.remove("Last-Modified");
        assertTrue(cache.isCacheableContent(response));

        headers.put("Expires", "0");
        assertFalse(cache.isCacheableContent(response));

        headers.put("Expires", "-1");
        assertFalse(cache.isCacheableContent(response));

        headers.put("Cache-Control", "no-store");
        assertFalse(cache.isCacheableContent(response));
    }

    /**
     *@throws Exception if the test fails
     */
    @Test
    public void usage() throws Exception {
        final String content = "<html><head><title>page 1</title>\n"
            + "<script src='foo1.js'></script>\n"
            + "<script src='foo2.js'></script>\n"
            + "</head><body>\n"
            + "<a href='page2.html'>to page 2</a>\n"
            + "</body></html>";

        final String content2 = "<html><head><title>page 2</title>\n"
            + "<script src='foo2.js'></script>\n"
            + "</head><body>\n"
            + "<a href='page1.html'>to page 1</a>\n"
            + "</body></html>";

        final String script1 = "alert('in foo1');";
        final String script2 = "alert('in foo2');";

        final WebClient webClient = getWebClient();
        final MockWebConnection connection = new MockWebConnection();
        webClient.setWebConnection(connection);

        final URL urlPage1 = new URL(URL_FIRST, "page1.html");
        connection.setResponse(urlPage1, content);
        final URL urlPage2 = new URL(URL_FIRST, "page2.html");
        connection.setResponse(urlPage2, content2);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Last-Modified", "Sun, 15 Jul 2007 20:46:27 GMT"));
        connection.setResponse(new URL(URL_FIRST, "foo1.js"), script1, 200, "ok",
                MimeType.APPLICATION_JAVASCRIPT, headers);
        connection.setResponse(new URL(URL_FIRST, "foo2.js"), script2, 200, "ok",
                MimeType.APPLICATION_JAVASCRIPT, headers);

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page1 = webClient.getPage(urlPage1);
        final String[] expectedAlerts = {"in foo1", "in foo2"};
        assertEquals(expectedAlerts, collectedAlerts);

        collectedAlerts.clear();
        page1.getAnchors().get(0).click();

        assertEquals(new String[] {"in foo2"}, collectedAlerts);
        assertEquals("no request for scripts should have been performed",
                urlPage2, connection.getLastWebRequest().getUrl());
    }

    /**
     *@throws Exception if the test fails
     */
    @Test
    public void jsUrlEncoded() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <title>page 1</title>\n"
            + "  <script src='foo1.js'></script>\n"
            + "  <script src='foo2.js?foo[1]=bar/baz'></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <a href='page2.html'>to page 2</a>\n"
            + "</body>\n"
            + "</html>";

        final String content2 = "<html>\n"
            + "<head>\n"
            + "  <title>page 2</title>\n"
            + "  <script src='foo2.js?foo[1]=bar/baz'></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <a href='page1.html'>to page 1</a>\n"
            + "</body>\n"
            + "</html>";

        final String script1 = "alert('in foo1');";
        final String script2 = "alert('in foo2');";

        final URL urlPage1 = new URL(URL_FIRST, "page1.html");
        getMockWebConnection().setResponse(urlPage1, content);
        final URL urlPage2 = new URL(URL_FIRST, "page2.html");
        getMockWebConnection().setResponse(urlPage2, content2);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Last-Modified", "Sun, 15 Jul 2007 20:46:27 GMT"));
        getMockWebConnection().setResponse(new URL(URL_FIRST, "foo1.js"), script1,
                200, "ok", MimeType.APPLICATION_JAVASCRIPT, headers);
        getMockWebConnection().setDefaultResponse(script2, 200, "ok", MimeType.APPLICATION_JAVASCRIPT, headers);

        final WebClient webClient = getWebClientWithMockWebConnection();

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page1 = webClient.getPage(urlPage1);
        final String[] expectedAlerts = {"in foo1", "in foo2"};
        assertEquals(expectedAlerts, collectedAlerts);

        collectedAlerts.clear();
        page1.getAnchors().get(0).click();

        assertEquals(new String[] {"in foo2"}, collectedAlerts);
        assertEquals("no request for scripts should have been performed",
                urlPage2, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     *@throws Exception if the test fails
     */
    @Test
    public void cssUrlEncoded() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <title>page 1</title>\n"
            + "  <link href='foo1.css' type='text/css' rel='stylesheet'>\n"
            + "  <link href='foo2.js?foo[1]=bar/baz' type='text/css' rel='stylesheet'>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <a href='page2.html'>to page 2</a>\n"
            + "  <script>\n"
            + "    var sheets = document.styleSheets;\n"
            + "    alert(sheets.length);\n"
            + "    var rules = sheets[0].cssRules || sheets[0].rules;\n"
            + "    alert(rules.length);\n"
            + "    rules = sheets[1].cssRules || sheets[1].rules;\n"
            + "    alert(rules.length);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        final String content2 = "<html>\n"
            + "<head>\n"
            + "  <title>page 2</title>\n"
            + "  <link href='foo2.js?foo[1]=bar/baz' type='text/css' rel='stylesheet'>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <a href='page1.html'>to page 1</a>\n"
            + "  <script>\n"
            + "    var sheets = document.styleSheets;\n"
            + "    alert(sheets.length);\n"
            + "    var rules = sheets[0].cssRules || sheets[0].rules;\n"
            + "    alert(rules.length);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        final URL urlPage1 = new URL(URL_FIRST, "page1.html");
        getMockWebConnection().setResponse(urlPage1, content);
        final URL urlPage2 = new URL(URL_FIRST, "page2.html");
        getMockWebConnection().setResponse(urlPage2, content2);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Last-Modified", "Sun, 15 Jul 2007 20:46:27 GMT"));
        getMockWebConnection().setResponse(new URL(URL_FIRST, "foo1.js"), "",
                200, "ok", MimeType.TEXT_CSS, headers);
        getMockWebConnection().setDefaultResponse("", 200, "ok", MimeType.TEXT_CSS, headers);

        final WebClient webClient = getWebClientWithMockWebConnection();

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page1 = webClient.getPage(urlPage1);
        final String[] expectedAlerts = {"2", "0", "0"};
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals(3, getMockWebConnection().getRequestCount());

        collectedAlerts.clear();
        page1.getAnchors().get(0).click();

        assertEquals(new String[] {"1", "0"}, collectedAlerts);
        assertEquals(4, getMockWebConnection().getRequestCount());
        assertEquals("no request for scripts should have been performed",
                urlPage2, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     *@throws Exception if the test fails
     */
    @Test
    public void maxSizeMaintained() throws Exception {
        final String html = "<html><head><title>page 1</title>\n"
            + "<script src='foo1.js' type='text/javascript'/>\n"
            + "<script src='foo2.js' type='text/javascript'/>\n"
            + "</head><body>abc</body></html>";

        final WebClient client = getWebClient();
        client.getCache().setMaxSize(1);

        final MockWebConnection connection = new MockWebConnection();
        client.setWebConnection(connection);

        final URL pageUrl = new URL(URL_FIRST, "page1.html");
        connection.setResponse(pageUrl, html);

        final List<NameValuePair> headers =
            Collections.singletonList(new NameValuePair("Last-Modified", "Sun, 15 Jul 2007 20:46:27 GMT"));
        connection.setResponse(new URL(URL_FIRST, "foo1.js"), ";", 200, "ok", MimeType.APPLICATION_JAVASCRIPT, headers);
        connection.setResponse(new URL(URL_FIRST, "foo2.js"), ";", 200, "ok", MimeType.APPLICATION_JAVASCRIPT, headers);

        client.getPage(pageUrl);
        assertEquals(1, client.getCache().getSize());

        client.getCache().clear();
        assertEquals(0, client.getCache().getSize());
    }

    /**
     * TODO: improve CSS caching to cache a COPY of the object as stylesheet objects can be modified dynamically.
     * @throws Exception if the test fails
     */
    @Test
    public void cssIsCached() throws Exception {
        final String html = "<html><head><title>page 1</title>\n"
            + "<style>.x { color: red; }</style>\n"
            + "<link rel='stylesheet' type='text/css' href='foo.css' />\n"
            + "</head>\n"
            + "<body onload='document.styleSheets.item(0); document.styleSheets.item(1);'>x</body>\n"
            + "</html>";

        final WebClient client = getWebClient();

        final MockWebConnection connection = new MockWebConnection();
        client.setWebConnection(connection);

        final URL pageUrl = new URL(URL_FIRST, "page1.html");
        connection.setResponse(pageUrl, html);

        final List<NameValuePair> headers =
            Collections.singletonList(new NameValuePair("Last-Modified", "Sun, 15 Jul 2007 20:46:27 GMT"));
        connection.setResponse(new URL(URL_FIRST, "foo.css"), "", 200, "OK", MimeType.TEXT_CSS, headers);

        client.getPage(pageUrl);
        assertEquals(2, client.getCache().getSize());
    }

    /**
     * Check for correct caching if the css request gets redirected.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void cssIsCachedIfUrlWasRedirected() throws Exception {
        final String html = "<html><head><title>page 1</title>\n"
            + "<link rel='stylesheet' type='text/css' href='foo.css' />\n"
            + "</head>\n"
            + "<body onload='document.styleSheets.item(0); document.styleSheets.item(1);'>x</body>\n"
            + "</html>";

        final String css = ".x { color: red; }";

        final WebClient client = getWebClient();

        final MockWebConnection connection = new MockWebConnection();
        client.setWebConnection(connection);

        final URL pageUrl = new URL(URL_FIRST, "page1.html");
        connection.setResponse(pageUrl, html);

        final URL cssUrl = new URL(URL_FIRST, "foo.css");
        final URL redirectUrl = new URL(URL_FIRST, "fooContent.css");

        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", redirectUrl.toExternalForm()));
        connection.setResponse(cssUrl, "", 301, "Redirect", null, headers);

        headers = Collections.singletonList(new NameValuePair("Last-Modified", "Sun, 15 Jul 2007 20:46:27 GMT"));
        connection.setResponse(redirectUrl, css, 200, "OK", MimeType.TEXT_CSS, headers);

        client.getPage(pageUrl);
        client.getPage(pageUrl);

        // page1.html - foo.css - fooContent.css - page1.html
        assertEquals(4, connection.getRequestCount());
        // foo.css - fooContent.css
        assertEquals(2, client.getCache().getSize());
    }

    /**
     * Test that content retrieved with XHR is cached when right headers are here.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"hello", "hello"})
    public void xhrContentCached() throws Exception {
        final String html = "<html><head><title>page 1</title>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('GET', 'foo.txt', false);\n"
            + "    xhr.send('');\n"
            + "    alert(xhr.responseText);\n"
            + "    xhr.send('');\n"
            + "    alert(xhr.responseText);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>x</body>\n"
            + "</html>";

        final MockWebConnection connection = getMockWebConnection();

        final List<NameValuePair> headers =
            Collections.singletonList(new NameValuePair("Last-Modified", "Sun, 15 Jul 2007 20:46:27 GMT"));
        connection.setResponse(new URL(URL_FIRST, "foo.txt"), "hello", 200, "OK", MimeType.TEXT_PLAIN, headers);

        loadPageWithAlerts(html);

        assertEquals(2, connection.getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testNoStoreCacheControl() throws Exception {
        final String html = "<html><head><title>page 1</title>\n"
            + "<link rel='stylesheet' type='text/css' href='foo.css' />\n"
            + "</head>\n"
            + "<body>x</body>\n"
            + "</html>";

        final WebClient client = getWebClient();

        final MockWebConnection connection = new MockWebConnection();
        client.setWebConnection(connection);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Cache-Control", "some-other-value, no-store"));

        final URL pageUrl = new URL(URL_FIRST, "page1.html");
        connection.setResponse(pageUrl, html, 200, "OK", "text/html;charset=ISO-8859-1", headers);
        connection.setResponse(new URL(URL_FIRST, "foo.css"), "", 200, "OK", MimeType.APPLICATION_JAVASCRIPT, headers);

        client.getPage(pageUrl);
        assertEquals(0, client.getCache().getSize());
        assertEquals(2, connection.getRequestCount());

        client.getPage(pageUrl);
        assertEquals(0, client.getCache().getSize());
        assertEquals(4, connection.getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testMaxAgeCacheControl() throws Exception {
        final String html = "<html><head><title>page 1</title>\n"
            + "<link rel='stylesheet' type='text/css' href='foo.css' />\n"
            + "</head>\n"
            + "<body>x</body>\n"
            + "</html>";

        final WebClient client = getWebClient();

        final MockWebConnection connection = new MockWebConnection();
        client.setWebConnection(connection);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Last-Modified", "Tue, 20 Feb 2018 10:00:00 GMT"));
        headers.add(new NameValuePair("Cache-Control", "some-other-value, max-age=1"));

        final URL pageUrl = new URL(URL_FIRST, "page1.html");
        connection.setResponse(pageUrl, html, 200, "OK", "text/html;charset=ISO-8859-1", headers);
        connection.setResponse(new URL(URL_FIRST, "foo.css"), "", 200, "OK", MimeType.APPLICATION_JAVASCRIPT, headers);

        client.getPage(pageUrl);
        assertEquals(2, client.getCache().getSize());
        assertEquals(2, connection.getRequestCount());
        // resources should be still in cache
        client.getPage(pageUrl);
        assertEquals(2, client.getCache().getSize());
        assertEquals(2, connection.getRequestCount());
        // wait for max-age seconds + 1 for recache
        Thread.sleep(2 * 1000);
        client.getPage(pageUrl);
        assertEquals(2, client.getCache().getSize());
        assertEquals(4, connection.getRequestCount());

        // wait for max-age seconds + 1 for recache
        Thread.sleep(2 * 1000);
        client.getCache().clearOutdated();
        assertEquals(0, client.getCache().getSize());
        assertEquals(4, connection.getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSMaxageCacheControl() throws Exception {
        final String html = "<html><head><title>page 1</title>\n"
            + "<link rel='stylesheet' type='text/css' href='foo.css' />\n"
            + "</head>\n"
            + "<body>x</body>\n"
            + "</html>";

        final WebClient client = getWebClient();

        final MockWebConnection connection = new MockWebConnection();
        client.setWebConnection(connection);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Last-Modified", "Tue, 20 Feb 2018 10:00:00 GMT"));
        headers.add(new NameValuePair("Cache-Control", "public, s-maxage=1, some-other-value, max-age=10"));

        final URL pageUrl = new URL(URL_FIRST, "page1.html");
        connection.setResponse(pageUrl, html, 200, "OK", "text/html;charset=ISO-8859-1", headers);
        connection.setResponse(new URL(URL_FIRST, "foo.css"), "", 200, "OK", MimeType.APPLICATION_JAVASCRIPT, headers);

        client.getPage(pageUrl);
        assertEquals(2, client.getCache().getSize());
        assertEquals(2, connection.getRequestCount());
        // resources should be still in cache
        client.getPage(pageUrl);
        assertEquals(2, client.getCache().getSize());
        assertEquals(2, connection.getRequestCount());
        // wait for s-maxage seconds + 1 for recache
        Thread.sleep(2 * 1000);
        client.getPage(pageUrl);
        assertEquals(2, client.getCache().getSize());
        assertEquals(4, connection.getRequestCount());

        // wait for s-maxage seconds + 1 for recache
        Thread.sleep(2 * 1000);
        client.getCache().clearOutdated();
        assertEquals(0, client.getCache().getSize());
        assertEquals(4, connection.getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testExpiresCacheControl() throws Exception {
        final String html = "<html><head><title>page 1</title>\n"
            + "<link rel='stylesheet' type='text/css' href='foo.css' />\n"
            + "</head>\n"
            + "<body>x</body>\n"
            + "</html>";

        final WebClient client = getWebClient();

        final MockWebConnection connection = new MockWebConnection();
        client.setWebConnection(connection);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Last-Modified", "Tue, 20 Feb 2018 10:00:00 GMT"));
        headers.add(new NameValuePair("Expires", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date(
            System.currentTimeMillis() + 2 * 1000 + 10 * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_MINUTE))));
        headers.add(new NameValuePair("Cache-Control", "public, some-other-value"));

        final URL pageUrl = new URL(URL_FIRST, "page1.html");
        connection.setResponse(pageUrl, html, 200, "OK", "text/html;charset=ISO-8859-1", headers);
        connection.setResponse(new URL(URL_FIRST, "foo.css"), "", 200, "OK", MimeType.APPLICATION_JAVASCRIPT, headers);

        client.getPage(pageUrl);
        assertEquals(2, client.getCache().getSize());
        assertEquals(2, connection.getRequestCount());
        // resources should be still in cache
        client.getPage(pageUrl);
        assertEquals(2, client.getCache().getSize());
        assertEquals(2, connection.getRequestCount());
        // wait for expires
        Thread.sleep(2 * 1000);
        client.getPage(pageUrl);
        assertEquals(0, client.getCache().getSize());
        assertEquals(4, connection.getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testMaxAgeOverrulesExpiresCacheControl() throws Exception {
        final String html = "<html><head><title>page 1</title>\n"
            + "<link rel='stylesheet' type='text/css' href='foo.css' />\n"
            + "</head>\n"
            + "<body>x</body>\n"
            + "</html>";

        final WebClient client = getWebClient();

        final MockWebConnection connection = new MockWebConnection();
        client.setWebConnection(connection);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Last-Modified", "Tue, 20 Feb 2018 10:00:00 GMT"));
        headers.add(new NameValuePair("Expires", "0"));
        headers.add(new NameValuePair("Cache-Control", "max-age=20"));

        final URL pageUrl = new URL(URL_FIRST, "page1.html");
        connection.setResponse(pageUrl, html, 200, "OK", "text/html;charset=ISO-8859-1", headers);
        connection.setResponse(new URL(URL_FIRST, "foo.css"), "", 200, "OK", MimeType.APPLICATION_JAVASCRIPT, headers);

        client.getPage(pageUrl);
        assertEquals(2, client.getCache().getSize());
        assertEquals(2, connection.getRequestCount());
        // resources should be still in cache
        client.getPage(pageUrl);
        assertEquals(2, client.getCache().getSize());
        assertEquals(2, connection.getRequestCount());
    }

    /**
     * Ensures {@link WebResponse#cleanUp()} is called for overflow deleted entries.
     * @throws Exception if the test fails
     */
    @Test
    public void cleanUpOverflow() throws Exception {
        final WebRequest request1 = new WebRequest(URL_FIRST, HttpMethod.GET);
        final WebResponse response1 = createMock(WebResponse.class);
        expect(response1.getWebRequest()).andReturn(request1);
        expectLastCall().atLeastOnce();
        expect(response1.getResponseHeaderValue(HttpHeader.CACHE_CONTROL)).andReturn(null);
        expect(response1.getResponseHeaderValue(HttpHeader.CACHE_CONTROL)).andReturn(null);
        expect(response1.getResponseHeaderValue(HttpHeader.LAST_MODIFIED)).andReturn(null);
        expect(response1.getResponseHeaderValue(HttpHeader.EXPIRES)).andReturn(
                formatDate(DateUtils.addHours(new Date(), 1)));

        final WebRequest request2 = new WebRequest(URL_SECOND, HttpMethod.GET);
        final WebResponse response2 = createMock(WebResponse.class);
        expect(response2.getWebRequest()).andReturn(request2);
        expectLastCall().atLeastOnce();
        expect(response2.getResponseHeaderValue(HttpHeader.CACHE_CONTROL)).andReturn(null);
        expect(response2.getResponseHeaderValue(HttpHeader.CACHE_CONTROL)).andReturn(null);
        expect(response2.getResponseHeaderValue(HttpHeader.LAST_MODIFIED)).andReturn(null);
        expect(response2.getResponseHeaderValue(HttpHeader.EXPIRES)).andReturn(
                formatDate(DateUtils.addHours(new Date(), 1)));

        response1.cleanUp();

        replay(response1, response2);

        final Cache cache = new Cache();
        cache.setMaxSize(1);
        cache.cacheIfPossible(request1, response1, null);
        Thread.sleep(10);
        cache.cacheIfPossible(request2, response2, null);

        verify(response1);
    }

    /**
     * Ensures {@link WebResponse#cleanUp()} is called on calling {@link Cache#clear()}.
     */
    @Test
    public void cleanUpOnClear() {
        final WebRequest request1 = new WebRequest(URL_FIRST, HttpMethod.GET);
        final WebResponse response1 = createMock(WebResponse.class);
        expect(response1.getWebRequest()).andReturn(request1);
        expectLastCall().atLeastOnce();
        expect(response1.getResponseHeaderValue(HttpHeader.CACHE_CONTROL)).andReturn(null);
        expect(response1.getResponseHeaderValue(HttpHeader.CACHE_CONTROL)).andReturn(null);
        expect(response1.getResponseHeaderValue(HttpHeader.LAST_MODIFIED)).andReturn(null);
        expect(response1.getResponseHeaderValue(HttpHeader.EXPIRES)).andReturn(
                formatDate(DateUtils.addHours(new Date(), 1)));

        response1.cleanUp();

        replay(response1);

        final Cache cache = new Cache();
        cache.cacheIfPossible(request1, response1, null);

        cache.clear();

        verify(response1);
    }
}

class DummyWebResponse extends WebResponse {

    DummyWebResponse() {
        super(null, null, 0);
    }

    @Override
    public InputStream getContentAsStream() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public String getContentAsString() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Charset getContentCharset() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Charset getContentCharsetOrNull() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public String getContentType() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public long getLoadTime() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<NameValuePair> getResponseHeaders() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public String getResponseHeaderValue(final String headerName) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public int getStatusCode() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public String getStatusMessage() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public WebRequest getWebRequest() {
        throw new RuntimeException("not implemented");
    }
}
