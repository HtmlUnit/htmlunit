/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
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
@RunWith(BrowserRunner.class)
public class LocationTest extends WebDriverTestCase {

    /**
     * Regression test for bug 742902.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void testDocumentLocationGet() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(top.document.location);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ok")
    public void testDocumentLocationSet() throws Exception {
        final String html1 =
              "<html>\n"
            + "<head>\n"
            + "  <title>test1</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      document.location = 'foo.html';\n"
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

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "foo.html"), html2);
        loadPageWithAlerts(html1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void testDocumentLocationHref() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(top.document.location.href);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetVariousAttributes() throws Exception {
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
        testGetVariousAttributes("http://first", expectedAlerts);

        // Try page with all the appropriate parts
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
        testGetVariousAttributes("http://www.first:77/foo?bar#wahoo", expectedAlerts);
    }

    private void testGetVariousAttributes(final String url, final String[] expectedAlerts) throws Exception {
        final WebClient client = getWebClient();
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
        client.getPage(url);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "", "about:blank", "", "", "about:", "" },
            IE = { "", "about:blank", "/blank", "", "about:", "" })
    public void about_blank_attributes() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var location = frames[0].document.location;\n"
            + "    alert(location.hash);\n"
            + "    alert(location.href);\n"
            + "    alert(location.pathname);\n"
            + "    alert(location.port);\n"
            + "    alert(location.protocol);\n"
            + "    alert(location.search);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe src='about:blank'></iframe></body></html>";

        loadPageWithAlerts(html);
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
        final WebClient webClient = getWebClient();
        final MockWebConnection conn = new MockWebConnection();

        final String html
            = "<html><head><title>Test</title></head><body>\n"
            + "<a id='a' onclick='alert(location.hash);location.hash=\"b\";alert(location.hash);'>go</a>\n"
            + "<h2 id='b'>...</h2></body></html>";

        conn.setResponse(URL_FIRST, html);
        webClient.setWebConnection(conn);

        final List<String> actual = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(actual));

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getHtmlElementById("a");
        final HtmlPage page2 = anchor.click();

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
    @Alerts(FF = { "#a b", "§§URL§§#a%20b", "#a b", "§§URL§§#a%20b" },
            IE = { "#a b", "§§URL§§#a b", "#a%20b", "§§URL§§#a%20b" })
    public void testSetHash_Encoding() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "    window.location.hash = 'a b';\n"
            + "    alert(window.location.hash);\n"
            + "    alert(window.location.href);\n"
            + "    window.location.hash = 'a%20b';\n"
            + "    alert(window.location.hash);\n"
            + "    alert(window.location.href);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "#hello", "#hi" })
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

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that setting <tt>location.href</tt> to a hash behaves like setting <tt>location.hash</tt>
     * (ie doesn't result in a server hit). See bug 2089341.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSetHrefWithOnlyHash() throws Exception {
        final String html = "<html><body><script>document.location.href = '#x';</script></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Verifies that setting <tt>location.href</tt> to a new URL with a hash, where the only difference between the
     * new URL and the old URL is the hash, behaves like setting <tt>location.hash</tt> (ie doesn't result in a
     * server hit). See bug 2101735.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSetHrefWithOnlyHash2() throws Exception {
        final String html = "<script>document.location.href = '" + getDefaultUrl() + "#x';</script>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetHostname() throws Exception {
        final WebClient webClient = getWebClient();
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

        final HtmlPage page = webClient.getPage(url);
        assertEquals("Test 2", page.getTitleText());
        assertEquals(url2.toExternalForm(), page.getWebResponse().getRequestSettings().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetHostWithoutPort() throws Exception {
        final WebClient webClient = getWebClient();
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

        final HtmlPage page = webClient.getPage(url);
        assertEquals("Test 2", page.getTitleText());
        assertEquals(url2.toExternalForm(), page.getWebResponse().getRequestSettings().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetHostWithPort() throws Exception {
        final WebClient webClient = getWebClient();
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

        final HtmlPage page = webClient.getPage(url);
        assertEquals("Test 2", page.getTitleText());
        assertEquals(url2.toExternalForm(), page.getWebResponse().getRequestSettings().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetPathname() throws Exception {
        final WebClient webClient = getWebClient();
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

        final HtmlPage page = webClient.getPage(url);
        assertEquals("Test 2", page.getTitleText());
        assertEquals(url2.toExternalForm(), page.getWebResponse().getRequestSettings().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetPort() throws Exception {
        final WebClient webClient = getWebClient();
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

        final HtmlPage page = webClient.getPage(url);
        assertEquals("Test 2", page.getTitleText());
        assertEquals(url2.toExternalForm(), page.getWebResponse().getRequestSettings().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetProtocol() throws Exception {
        final WebClient webClient = getWebClient();
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

        final HtmlPage page = webClient.getPage(url);
        assertEquals("Test 2", page.getTitleText());
        assertEquals(url2.toExternalForm(), page.getWebResponse().getRequestSettings().getUrl().toExternalForm());
    }

    /**
     * Test for <tt>replace</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void testReplace() throws Exception {
        final WebClient webClient = getWebClient();
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

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("Second", page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testAssign() throws Exception {
        final WebClient webClient = getWebClient();
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

        final HtmlPage page = webClient.getPage(URL_FIRST);
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

        final HtmlPage page1 = loadPage(getBrowserVersion(), content, null);
        final HtmlAnchor link = page1.getHtmlElementById("link1");
        final HtmlPage page2 = link.click();

        assertEquals(page1.getTitleText(), page2.getTitleText());
        assertNotSame(page1, page2);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c")
    public void testLocationWithTarget() throws Exception {
        final WebClient client = getWebClient();
        final List<String> alerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));

        final URL url = getClass().getResource("LocationTest_locationWithTarget_a.html");
        assertNotNull(url);

        final HtmlPage a = client.getPage(url);
        final HtmlPage c = (HtmlPage) a.getFrameByName("c").getEnclosedPage();
        c.getElementById("anchor").click();
        assertEquals(getExpectedAlerts(), alerts);
    }

    /**
     * Regression test for http://sourceforge.net/tracker/index.php?func=detail&aid=1289060&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void testReplaceWithFrame() throws Exception {
        final WebClient webClient = getWebClient();
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
            + "onclick='parent.frames.content.location.replace(\"test.html\");return false;'>Test2</a>\n"
            + "</body></html>";
        final String frameContent = "<html><head><title>Start content</title></head><body></body></html>";
        final String frameTest = "<html><head><title>Test</title></head><body></body></html>";

        webConnection.setResponse(URL_FIRST, mainContent);
        webConnection.setResponse(new URL(URL_FIRST + "menu.html"), frameMenu);
        webConnection.setResponse(new URL(URL_FIRST + "content.html"), frameContent);
        webConnection.setResponse(new URL(URL_FIRST + "test.html"), frameTest);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals(3, webClient.getWebWindows().size());
        assertEquals("Start content", ((HtmlPage) page.getFrameByName("content").getEnclosedPage()).getTitleText());

        final HtmlPage menuPage = (HtmlPage) page.getFrameByName("menu").getEnclosedPage();
        menuPage.<HtmlElement>getHtmlElementById("myLink").click();
        assertEquals(3, webClient.getWebWindows().size());
        assertEquals("Test", ((HtmlPage) page.getFrameByName("content").getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testChangeLocationToNonHtml() throws Exception {
        final WebClient webClient = getWebClient();
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
    @Alerts("foo")
    public void jsLocation() throws Exception {
        final String html =
              "<html><head>\n"
            + "  <script>\n"
            + "      document.location.href = 'javascript:alert(\"foo\")';\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void testToString() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + " alert(window.location.toString());\n"
            + "</script>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void href() throws Exception {
        final WebClient webClient = getWebClient();
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void href_postponed() throws Exception {
        final String firstHtml =
            "<html><head><script>\n"
            + "function test() {\n"
            + "  alert('1');\n"
            + "  self.frames['frame1'].document.location.href='" + URL_SECOND + "';\n"
            + "  alert('2');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <iframe name='frame1' id='frame1'/>\n"
            + "</body></html>";
        final String secondHtml = "<html><body><script>alert('3');</script></body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setResponse(URL_SECOND, secondHtml);
        client.setWebConnection(webConnection);
        final ArrayList<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String[] expectedAlerts = {"1", "2", "3"};
        client.getPage(URL_FIRST);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onlick_set_location() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<a href='foo2.html' onclick='document.location = \"foo3.html\"'>click me</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPageWithAlerts2(html);
        driver.findElement(By.tagName("a")).click();

        final String[] expectedRequests = {"", "foo3.html", "foo2.html"};
        assertEquals(expectedRequests, getMockWebConnection().getRequestedUrls(getDefaultUrl()));

        assertEquals(new URL(getDefaultUrl(), "foo2.html").toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fileUrlFormat() throws Exception {
        final URL url = getClass().getResource("LocationTest_fileUrlFormat.html");
        assertNotNull(url);

        final WebClient client = getWebClient();
        final List<String> alerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));
        client.getPage(url);

        assertEquals(1, alerts.size());
        assertTrue(alerts.get(0).startsWith("file:///"));
    }

}
