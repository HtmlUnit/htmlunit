/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static org.junit.Assert.assertNotSame;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link Location}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Michael Ottati
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class LocationTest extends WebTestCase {

    /**
     * Regression test for bug 742902.
     * @throws Exception if the test fails
     */
    @Test
    public void testDocumentLocationGet() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(top.document.location);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();

        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {URL_GARGOYLE.toExternalForm()};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDocumentLocationSet() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String html1 =
              "<html>\n"
            + "<head>\n"
            + "  <title>test1</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      document.location = '" + URL_SECOND + "';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        final String html2 =
              "<html>\n"
            + "<head>\n"
            + "  <title>test2</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert('ok');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        webConnection.setResponse(URL_FIRST, html1);
        webConnection.setResponse(URL_SECOND, html2);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("test2", page.getTitleText());

        final String[] expectedAlerts = {"ok"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDocumentLocationHref() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(top.document.location.href);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {URL_GARGOYLE.toExternalForm()};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetVariousAttributes() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var location = document.location;\n"
            + "    alert(location.hash);\n"
            + "    alert(location.host);\n"
            + "    alert(location.hostname);\n"
            + "    alert(location.href);\n"
            + "    alert(location.pathname);\n"
            + "    alert(location.port);\n"
            + "    alert(location.protocol);\n"
            + "    alert(location.search);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'></body></html>";

        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        // Try page with only a server name
        client.getPage("http://first");
        String[] expectedAlerts = {
            "",               // hash
            "first",          // host
            "first",          // hostname
            "http://first/",  // href
            "/",              // pathname
            "",               // port
            "http:",          // protocol
            ""                // search
        };
        assertEquals("simple url", expectedAlerts, collectedAlerts);

        collectedAlerts.clear();

        // Try page with all the appropriate parts
        client.getPage("http://www.first:77/foo?bar#wahoo");
        expectedAlerts = new String[] {
            "#wahoo",                            // hash
            "www.first:77",                      // host
            "www.first",                         // hostname
            "http://www.first:77/foo?bar#wahoo", // href
            "/foo",                              // pathname
            "77",                                // port
            "http:",                             // protocol
            "?bar"                               // search
        };
        assertEquals("complete url", expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that modifying <tt>window.location.hash</tt> works, but that it doesn't
     * force the page to reload from the server. This is very important for the Dojo
     * unit tests, which will keep reloading themselves if the page gets reloaded.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testSetHash() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection conn = new MockWebConnection();

        final String html
            = "<html><head><title>Test</title></head><body>\n"
            + "<a id='a' onclick='alert(location.hash);location.hash=\"b\";alert(location.hash);'>go</a>\n"
            + "<h2 id='b'>...</h2></body></html>";

        conn.setResponse(URL_FIRST, html);
        webClient.setWebConnection(conn);

        final List<String> actual = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(actual));

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        final HtmlAnchor anchor = (HtmlAnchor) page.getHtmlElementById("a");
        final HtmlPage page2 = (HtmlPage) anchor.click();

        // Verify that it worked.
        final String[] expected = new String[] {"", "#b"};
        assertEquals(expected, actual);

        // Verify that we didn't reload the page.
        assertTrue(page == page2);
        assertEquals(URL_FIRST, conn.getLastWebRequestSettings().getUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testSetHash_Encoding() throws Exception {
        testSetHash_Encoding(BrowserVersion.FIREFOX_2,
            "#a b", "http://www.gargoylesoftware.com/#a%20b",
            "#a b", "http://www.gargoylesoftware.com/#a%20b");
        testSetHash_Encoding(BrowserVersion.INTERNET_EXPLORER_6_0,
            "#a b", "http://www.gargoylesoftware.com/#a b",
            "#a%20b", "http://www.gargoylesoftware.com/#a%20b");
        testSetHash_Encoding(BrowserVersion.INTERNET_EXPLORER_7_0,
            "#a b", "http://www.gargoylesoftware.com/#a b",
            "#a%20b", "http://www.gargoylesoftware.com/#a%20b");
    }

    private void testSetHash_Encoding(final BrowserVersion version, final String... expected) throws Exception {
        final String html =
              "<html><head><title>Test</title></head><body>\n"
            + "<a id='a' onclick='location.hash=\"a b\"; alert(location.hash); alert(location.href);'>go1</a>\n"
            + "<a id='b' onclick='location.hash=\"a%20b\"; alert(location.hash); alert(location.href);'>go2</a>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(version, html, actual);
        ((HtmlAnchor) page.getHtmlElementById("a")).click();
        ((HtmlAnchor) page.getHtmlElementById("b")).click();
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetHash2() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "    window.location.hash = 'hello';\n"
            + "    alert(window.location.hash);\n"
            + "    window.location.hash = '#hi';\n"
            + "    alert(window.location.hash);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"#hello", "#hi"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that setting <tt>location.href</tt> to a hash behaves like setting <tt>location.hash</tt>
     * (ie doesn't result in a server hit). See bug 2089341.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSetHrefWithOnlyHash() throws Exception {
        final String html = "<html><body><script>document.location.href = '#x';</script></body></html>";
        loadPage(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetHostname() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final URL url = new URL("http://abc.com/index.html#bottom");
        final URL url2 = new URL("http://xyz.com/index.html#bottom");
        final String html
            = "<html><head><title>Test 1</title></head>\n"
            + "<body onload='location.hostname=\"xyz.com\"'>...</body></html>";
        final String html2 = "<html><head><title>Test 2</title></head><body>...</body></html>";
        webConnection.setResponse(url, html);
        webConnection.setResponse(url2, html2);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(url);
        assertEquals("Test 2", page.getTitleText());
        assertEquals(url2.toExternalForm(), page.getWebResponse().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetHostWithoutPort() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final URL url = new URL("http://abc.com/index.html#bottom");
        final URL url2 = new URL("http://xyz.com/index.html#bottom");
        final String html
            = "<html><head><title>Test 1</title></head>\n"
            + "<body onload='location.host=\"xyz.com\"'>...</body></html>";
        final String html2 = "<html><head><title>Test 2</title></head><body>...</body></html>";
        webConnection.setResponse(url, html);
        webConnection.setResponse(url2, html2);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(url);
        assertEquals("Test 2", page.getTitleText());
        assertEquals(url2.toExternalForm(), page.getWebResponse().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetHostWithPort() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final URL url = new URL("http://abc.com/index.html#bottom");
        final URL url2 = new URL("http://xyz.com:8080/index.html#bottom");
        final String html
            = "<html><head><title>Test 1</title></head>\n"
            + "<body onload='location.host=\"xyz.com:8080\"'>...</body></html>";
        final String html2 = "<html><head><title>Test 2</title></head><body>...</body></html>";
        webConnection.setResponse(url, html);
        webConnection.setResponse(url2, html2);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(url);
        assertEquals("Test 2", page.getTitleText());
        assertEquals(url2.toExternalForm(), page.getWebResponse().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetPathname() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final URL url = new URL("http://abc.com/index.html?blah=bleh");
        final URL url2 = new URL("http://abc.com/en/index.html?blah=bleh");
        final String html
            = "<html><head><title>Test 1</title></head>\n"
            + "<body onload='location.pathname=\"/en/index.html\"'>...</body></html>";
        final String html2 = "<html><head><title>Test 2</title></head><body>...</body></html>";
        webConnection.setResponse(url, html);
        webConnection.setResponse(url2, html2);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(url);
        assertEquals("Test 2", page.getTitleText());
        assertEquals(url2.toExternalForm(), page.getWebResponse().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetPort() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final URL url = new URL("http://abc.com/index.html#bottom");
        final URL url2 = new URL("http://abc.com:88/index.html#bottom");
        final String html
            = "<html><head><title>Test 1</title></head>\n"
            + "<body onload='location.port=\"88\"'>...</body></html>";
        final String html2 = "<html><head><title>Test 2</title></head><body>...</body></html>";
        webConnection.setResponse(url, html);
        webConnection.setResponse(url2, html2);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(url);
        assertEquals("Test 2", page.getTitleText());
        assertEquals(url2.toExternalForm(), page.getWebResponse().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetProtocol() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final URL url = new URL("http://abc.com/index.html?blah=bleh");
        final URL url2 = new URL("ftp://abc.com/index.html?blah=bleh");
        final String html
            = "<html><head><title>Test 1</title></head>\n"
            + "<body onload='location.protocol=\"ftp\"'>...</body></html>";
        final String html2 = "<html><head><title>Test 2</title></head><body>...</body></html>";
        webConnection.setResponse(url, html);
        webConnection.setResponse(url2, html2);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(url);
        assertEquals("Test 2", page.getTitleText());
        assertEquals(url2.toExternalForm(), page.getWebResponse().getUrl().toExternalForm());
    }

    /**
     * Test for <tt>replace</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void testReplace() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    location.replace('" + URL_SECOND + "');\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("Second", page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testAssign() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "    location.assign('" + URL_SECOND + "');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("Second", page.getTitleText());
    }

    /**
     * Tests that location.reload() works correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void testReload() throws Exception {
        final String content =
              "<html>\n"
            + "  <head><title>test</title></head>\n"
            + "  <body>\n"
            + "    <a href='javascript:window.location.reload();' id='link1'>reload</a>\n"
            + "  </body>\n"
            + "</html>";

        final HtmlPage page1 = loadPage(content);
        final HtmlAnchor link = (HtmlAnchor) page1.getHtmlElementById("link1");
        final HtmlPage page2 = (HtmlPage) link.click();

        assertEquals(page1.getTitleText(), page2.getTitleText());
        assertNotSame(page1, page2);
    }

    /**
     * Regression test for http://sourceforge.net/tracker/index.php?func=detail&aid=1289060&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void testReplaceWithFrame() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String mainContent
            = " <html>\n"
            + " <frameset rows='100,*'>\n"
            + "     <frame name='menu' src='menu.html'/>\n"
            + "     <frame name='content' src='content.html'/>\n"
            + " </frameset>\n"
            + " </html>";
        final String frameMenu =
            "<html><body>\n"
            + "<a href='#' id='myLink' target='content' "
            + "onclick='parent.frames.content.location.replace(\"test.html\");'>Test2</a>\n"
            + "</body></html>";
        final String frameContent = "<html><head><title>Start content</title></head><body></body></html>";
        final String frameTest = "<html><head><title>Test</title></head><body></body></html>";

        webConnection.setResponse(URL_FIRST, mainContent);
        webConnection.setResponse(new URL(URL_FIRST + "menu.html"), frameMenu);
        webConnection.setResponse(new URL(URL_FIRST + "content.html"), frameContent);
        webConnection.setResponse(new URL(URL_FIRST + "test.html"), frameTest);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals(3, webClient.getWebWindows().size());
        assertEquals("Start content", ((HtmlPage) page.getFrameByName("content").getEnclosedPage()).getTitleText());

        final HtmlPage menuPage = (HtmlPage) page.getFrameByName("menu").getEnclosedPage();
        ((ClickableElement) menuPage.getHtmlElementById("myLink")).click();
        assertEquals(3, webClient.getWebWindows().size());
        assertEquals("Test", ((HtmlPage) page.getFrameByName("content").getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testChangeLocationToNonHtml() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String html =
              "<html><head>\n"
            + "  <script>\n"
            + "      document.location.href = 'foo.txt';\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body></body></html>";

        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(new URL(URL_FIRST + "foo.txt"), "bla bla", "text/plain");
        webClient.setWebConnection(webConnection);

        final Page page = webClient.getPage(URL_FIRST);
        assertEquals("bla bla", page.getWebResponse().getContentAsString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void jsLocation() throws Exception {
        final String html =
              "<html><head>\n"
            + "  <script>\n"
            + "      document.location.href = 'javascript:alert(\"foo\")';\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body></body></html>";

        final String[] expectedAlerts = {"foo"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(html, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testToString() throws Exception {
        final String content =
            "<html><head>\n"
            + "<script>\n"
            + " alert(window.location.toString());\n"
            + "</script>\n"
            + "<body>\n"
            + "</body></html>";

        final String[] expectedAlerts = {URL_GARGOYLE.toExternalForm()};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void href() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String content
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  alert(window.location.href);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        webConnection.setResponse(new URL("http://myHostName/"), content);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webClient.getPage("http://myHostName");
        final String[] expectedAlerts = {"http://myHostName/" };
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
