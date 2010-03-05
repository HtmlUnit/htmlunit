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
package com.gargoylesoftware.htmlunit.html;

import static org.apache.commons.lang.StringUtils.right;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlAnchor}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Stefan Anzinger
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlAnchorTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = (HtmlPage) anchor.click();

        final List<NameValuePair> expectedParameters = Collections.emptyList();
        final MockWebConnection webConnection = getMockConnection(secondPage);

        assertEquals("url", "http://www.foo2.com/", secondPage.getWebResponse().getRequestSettings().getUrl());
        assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
        Assert.assertEquals("parameters", expectedParameters, webConnection.getLastParameters());
        assertNotNull(secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClickAnchorName() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='#clickedAnchor' id='a1'>link to foo1</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);

        final MockWebConnection conn = (MockWebConnection) page.getWebClient().getWebConnection();
        assertEquals(1, conn.getRequestCount());

        final HtmlPage secondPage = page.getElementById("a1").click();
        assertEquals(1, conn.getRequestCount()); // no second server hit
        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_onClickHandler() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='" + URL_SECOND + "' id='a2' "
            + "onClick='alert(\"clicked\")'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = anchor.click();

        assertEquals(new String[] {"clicked"}, collectedAlerts);
        assertEquals("Second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_onClickHandler_returnFalse() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='" + URL_SECOND + "' id='a2' "
            + "onClick='alert(\"clicked\");return false;'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = (HtmlPage) anchor.click();

        assertEquals(new String[] {"clicked"}, collectedAlerts);
        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_onClickHandler_javascriptDisabled() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='http://www.foo2.com' id='a2' "
            + "onClick='alert(\"clicked\")'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final WebClient client = getWebClient();
        client.setJavaScriptEnabled(false);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(getDefaultUrl());
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = anchor.click();

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final List< ? > expectedParameters = Collections.EMPTY_LIST;

        assertEquals("url", "http://www.foo2.com/", secondPage.getWebResponse().getRequestSettings().getUrl());
        assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
        Assert.assertEquals("parameters", expectedParameters, webConnection.getLastParameters());
        assertNotNull(secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_javascriptUrl() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='javascript:alert(\"clicked\")' id='a2'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = anchor.click();

        assertEquals(new String[] {"clicked"}, collectedAlerts);
        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_javascriptUrl_javascriptDisabled() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='javascript:alert(\"clicked\")' id='a2'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final WebClient client = getWebClient();
        client.setJavaScriptEnabled(false);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(getDefaultUrl());
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = anchor.click();

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_javascriptUrl_InvalidReturn_RegressionTest() throws Exception {
        final String htmlContent
            = "<html><head><SCRIPT lang=\"JavaScript\">\n"
            + "function doSubmit(formName) {\n"
            + "    return false;\n"
            + "}\n"
            + "</SCRIPT></head><body>\n"
            + "<form name='formName' method='POST' action='../foo'>\n"
            + "<a href='.' id='testJavascript' name='testJavascript'"
            + "onclick='return false'>Test Link </a>\n"
            + "<input type='submit' value='Login' name='loginButton'>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlAnchor testAnchor = page.getAnchorByName("testJavascript");
        testAnchor.click();  // blows up here
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_javascriptUrl_targetPageWithIframe() throws Exception {
        final String firstContent
            = " <html>\n"
            + "<head><title>Page A</title></head>\n"
            + "<body><a href='#' onclick=\"document.location.href='" + URL_SECOND + "'\" id='link'>link</a></body>\n"
            + "</html>";
        final String secondContent
            = "<html>\n"
            + "<head><title>Page B</title></head>\n"
            + "<body><iframe src='" + URL_THIRD + "'></iframe></body>\n"
            + "</html>";
        final String thirdContent
            = "<html>\n"
            + "<head><title>Page C</title></head>\n"
            + "<body>test</body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        conn.setResponse(URL_THIRD, thirdContent);
        client.setWebConnection(conn);
        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlAnchor a = firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = a.click();
        Assert.assertEquals("url", URL_SECOND, secondPage.getWebResponse().getRequestSettings().getUrl());
        Assert.assertEquals("title", "Page B", secondPage.getTitleText());
    }

    /**
     * Regression test for bug 2847838.
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_javascriptUrl_encoded() throws Exception {
        final String htmlContent
            = "<html><body><script>function hello() { alert('hello') }</script>\n"
            + "<a href='javascript:%20hello%28%29' id='a1'>a1</a>\n"
            + "<a href='javascript: hello%28%29' id='a2'>a2</a>\n"
            + "<a href='javascript:hello%28%29' id='a3'>a3</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        page.getElementById("a1").click();
        page.getElementById("a2").click();
        page.getElementById("a3").click();
        assertEquals(new String[] {"hello", "hello", "hello"}, collectedAlerts);
    }

    /**
     * Test for new openLinkInNewWindow() method.
     * @throws Exception on test failure
     */
    @Test
    public void testOpenLinkInNewWindow() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlAnchor anchor = page.getHtmlElementById("a1");

        Assert.assertEquals("size incorrect before test", 1, page.getWebClient().getWebWindows().size());

        final HtmlPage secondPage = (HtmlPage) anchor.openLinkInNewWindow();

        assertNotSame("new page not returned", page, secondPage);
        assertTrue("new page in wrong window type",
                TopLevelWindow.class.isInstance(secondPage.getEnclosingWindow()));
        Assert.assertEquals("new window not created", 2, page.getWebClient().getWebWindows().size());
        assertNotSame("new window not used", page.getEnclosingWindow(), secondPage
                .getEnclosingWindow());
    }

    /**
     * Tests the 'Referer' HTTP header.
     * @throws Exception on test failure
     */
    @Test
    public void testClick_refererHeader() throws Exception {
        final String firstContent
            = "<html><head><title>Page A</title></head>\n"
            + "<body><a href='" + URL_SECOND + "' id='link'>link</a></body>\n"
            + "</html>";
        final String secondContent
            = "<html><head><title>Page B</title></head>\n"
            + "<body></body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(conn);
        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlAnchor a = firstPage.getHtmlElementById("link");
        a.click();

        final Map<String, String> lastAdditionalHeaders = conn.getLastAdditionalHeaders();
        assertEquals(URL_FIRST.toString(), lastAdditionalHeaders.get("Referer"));
    }

    /**
     * Links with an href and a non-false returning onclick that opens a new window should still
     * open the href in the first window.
     *
     * https://sourceforge.net/tracker/index.php?func=detail&aid=1587110&group_id=47038&atid=448266
     *
     * @throws Exception on test failure
     */
    @Test
    public void testCorrectLinkTargetWhenOnclickOpensWindow() throws Exception {
        final String firstContent = "<html><head><title>First</title></head><body>\n"
            + "<a href='page2.html' id='clickme' onclick=\"window.open('popup.html', 'newWindow');\">X</a>\n"
            + "</body></html>";
        final String html2 = "<html><head><title>Second</title></head><body></body></html>";
        final String htmlPopup = "<html><head><title>Popup</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(new URL(URL_FIRST, "page2.html"), html2);
        webConnection.setResponse(new URL(URL_FIRST, "popup.html"), htmlPopup);
        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = firstPage.getHtmlElementById("clickme");
        final HtmlPage pageAfterClick = anchor.click();

        Assert.assertEquals("Second window did not open", 2, client.getWebWindows().size());
        assertNotSame("New Page was not returned", firstPage, pageAfterClick);
        Assert.assertEquals("Wrong new Page returned", "Popup", pageAfterClick.getTitleText());
        Assert.assertEquals("Original window not updated", "Second",
            ((HtmlPage) firstPage.getEnclosingWindow().getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testPreventDefault1() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function handler(e) {\n"
            + "    if (e)\n"
            + "      e.preventDefault();\n"
            + "    else\n"
            + "      return false;\n"
            + "  }\n"
            + "  function init() {\n"
            + "    document.getElementById('a1').onclick = handler;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "<a href='" + URL_SECOND + "' id='a1'>Test</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlAnchor a1 = page.getHtmlElementById("a1");
        final HtmlPage secondPage = a1.click();
        assertEquals(getDefaultUrl(), secondPage.getWebResponse().getRequestSettings().getUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testPreventDefault2() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function handler(e) {\n"
            + "    if (e.preventDefault)\n"
            + "      e.preventDefault();\n"
            + "    else\n"
            + "      e.returnValue = false;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<a href='" + URL_SECOND + "' id='a1' onclick='handler(event)'>Test</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlAnchor a1 = page.getHtmlElementById("a1");
        final HtmlPage secondPage = a1.click();
        assertEquals(getDefaultUrl(), secondPage.getWebResponse().getRequestSettings().getUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testPreventDefault3() throws Exception {
        final String html =
              "<html><body>\n"
            + "<a href='" + URL_SECOND + "' id='a1' onclick='return false'>Test</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlAnchor a1 = page.getHtmlElementById("a1");
        final HtmlPage secondPage = a1.click();
        assertEquals(getDefaultUrl(), secondPage.getWebResponse().getRequestSettings().getUrl());
    }

    /**
     * Attributes aren't usually quoted in IE, but <tt>href</tt> attributes of anchor elements are.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "<a id=\"a\" href=\"#x\">foo</a>", IE = "<A id=a href=\"#x\">foo</A>")
    public void testInnerHtmlHrefQuotedEvenInIE() throws Exception {
        final String html = "<html><body onload='alert(document.getElementById(\"d\").innerHTML)'>"
            + "<div id='d'><a id='a' href='#x'>foo</a></div></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test for bug 2794667.
     * @throws Exception if an error occurs
     */
    @Test
    public void testHashAnchor() throws Exception {
        final String html = "<html><body><a id='a' href='#a'>a</a></body></html>";
        HtmlPage page = loadPage(html);
        page = page.getElementById("a").click();
        assertEquals(new URL(getDefaultUrl(), "#a"), page.getWebResponse().getRequestSettings().getUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testTargetWithRelativeUrl() throws Exception {
        final WebClient client = getWebClient();

        final URL url = getClass().getResource("HtmlAnchorTest_targetWithRelativeUrl_a.html");
        assertNotNull(url);

        final HtmlPage page = client.getPage(url);
        final WebWindow a = page.getEnclosingWindow();
        final WebWindow b = page.getFrameByName("b");
        final WebWindow c = page.getFrameByName("c");

        assertEquals("a.html", right(getUrl(a), 6));
        assertEquals("b.html", right(getUrl(b), 6));
        assertEquals("c.html", right(getUrl(c), 6));

        ((HtmlPage) c.getEnclosedPage()).getAnchorByHref("#foo").click();

        assertEquals("a.html", right(getUrl(a), 6));
        assertEquals("c.html#foo", right(getUrl(b), 10));
        assertEquals("c.html", right(getUrl(c), 6));
    }

    /**
     * Returns the URL of the page loaded in the specified window.
     * @param w the window
     * @return the URL of the page loaded in the specified window
     */
    private String getUrl(final WebWindow w) {
        return w.getEnclosedPage().getWebResponse().getRequestSettings().getUrl().toString();
    }

}
