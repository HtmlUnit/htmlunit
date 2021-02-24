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
package com.gargoylesoftware.htmlunit.html;

import static org.apache.commons.lang3.StringUtils.right;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * Tests for {@link HtmlAnchor}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Stefan Anzinger
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlAnchor2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click_onClickHandler() throws Exception {
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
        final List<String> collectedAlerts = new ArrayList<>();
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
    public void click_onClickHandler_returnFalse() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='" + URL_SECOND + "' id='a2' "
            + "onClick='alert(\"clicked\");return false;'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
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
        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click_onClickHandler_javascriptDisabled() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='http://www.foo2.com' id='a2' "
            + "onClick='alert(\"clicked\")'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final WebClient client = getWebClient();
        client.getOptions().setJavaScriptEnabled(false);

        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = anchor.click();

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final List<?> expectedParameters = Collections.EMPTY_LIST;

        assertEquals("url", "http://www.foo2.com/", secondPage.getUrl());
        assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
        assertEquals("parameters", expectedParameters, webConnection.getLastParameters());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click_javascriptUrl() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='javascript:alert(\"clicked\")' id='a2'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
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
    public void click_javascriptUrlMixedCase() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='JAVAscrIpt:alert(\"clicked\")' id='a2'>link to foo2</a>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
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
    public void click_javascriptUrlLeadingWhitespace() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='  javascript:alert(\"clicked\")' id='a2'>link to foo2</a>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
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
    public void click_javascriptUrl_javascriptDisabled() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='javascript:alert(\"clicked\")' id='a2'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final WebClient client = getWebClient();
        client.getOptions().setJavaScriptEnabled(false);

        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
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
    public void click_javascriptUrl_InvalidReturn_RegressionTest() throws Exception {
        final String htmlContent
            = "<html><head><SCRIPT lang=\"JavaScript\">\n"
            + "function doSubmit(formName) {\n"
            + "  return false;\n"
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
    public void click_javascriptUrl_targetPageWithIframe() throws Exception {
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
        assertEquals("url", URL_SECOND, secondPage.getUrl());
        assertEquals("title", "Page B", secondPage.getTitleText());
    }

    /**
     * Regression test for bug #894.
     * @throws Exception if the test fails
     */
    @Test
    public void click_javascriptUrl_encoded() throws Exception {
        final String htmlContent
            = "<html><body><script>function hello() { alert('hello') }</script>\n"
            + "<a href='javascript:%20hello%28%29' id='a1'>a1</a>\n"
            + "<a href='javascript: hello%28%29' id='a2'>a2</a>\n"
            + "<a href='javascript:hello%28%29' id='a3'>a3</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        page.getHtmlElementById("a1").click();
        page.getHtmlElementById("a2").click();
        page.getHtmlElementById("a3").click();
        assertEquals(new String[] {"hello", "hello", "hello"}, collectedAlerts);
    }

    /**
     * Test for new openLinkInNewWindow() method.
     * @throws Exception on test failure
     */
    @Test
    public void openLinkInNewWindow() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlAnchor anchor = page.getHtmlElementById("a1");

        assertEquals("size incorrect before test", 1, page.getWebClient().getWebWindows().size());

        final HtmlPage secondPage = (HtmlPage) anchor.openLinkInNewWindow();

        assertNotSame("new page not returned", page, secondPage);
        assertTrue("new page in wrong window type",
                TopLevelWindow.class.isInstance(secondPage.getEnclosingWindow()));
        assertEquals("new window not created", 2, page.getWebClient().getWebWindows().size());
        assertNotSame("new window not used", page.getEnclosingWindow(), secondPage
                .getEnclosingWindow());
    }

    /**
     * Links with an href and a non-false returning onclick that opens a new window should still
     * open the href in the first window.
     *
     * http://sourceforge.net/p/htmlunit/bugs/394/
     *
     * @throws Exception on test failure
     */
    @Test
    public void correctLinkTargetWhenOnclickOpensWindow() throws Exception {
        final String firstContent = "<html><head><title>First</title></head><body>\n"
            + "<a href='page2.html' id='clickme' onclick=\"window.open('popup.html', 'newWindow');\">X</a>\n"
            + "</body></html>";
        final String html2 = "<html><head><title>Second</title></head><body></body></html>";
        final String htmlPopup = "<html><head><title>Popup</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(new URL(URL_FIRST, "page2.html"), html2);
        webConnection.setResponse(new URL(URL_FIRST, "popup.html"), htmlPopup);
        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = firstPage.getHtmlElementById("clickme");
        final HtmlPage pageAfterClick = anchor.click();

        assertEquals("Second window did not open", 2, client.getWebWindows().size());
        assertNotSame("New Page was not returned", firstPage, pageAfterClick);
        assertEquals("Wrong new Page returned", "Popup", pageAfterClick.getTitleText());
        assertEquals("Original window not updated", "Second",
            ((HtmlPage) firstPage.getEnclosingWindow().getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault1() throws Exception {
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
        assertEquals(URL_FIRST, secondPage.getUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault2() throws Exception {
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
        assertEquals(URL_FIRST, secondPage.getUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault3() throws Exception {
        final String html =
              "<html><body>\n"
            + "<a href='" + URL_SECOND + "' id='a1' onclick='return false'>Test</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlAnchor a1 = page.getHtmlElementById("a1");
        final HtmlPage secondPage = a1.click();
        assertEquals(URL_FIRST, secondPage.getUrl());
    }

    /**
     * Test for bug #826.
     * @throws Exception if an error occurs
     */
    @Test
    public void hashAnchor() throws Exception {
        final String html = "<html><body>\n"
                + "<a id='a' href='#a'>a</a>\n"
                + "<a id='a_target' href='#target' target='_blank'>target</a>\n"
                + "</body></html>";
        HtmlPage page = loadPage(html);
        HtmlPage targetPage = page.getHtmlElementById("a").click();
        assertEquals(new URL(URL_FIRST, "#a"), page.getUrl());
        assertEquals(page.getEnclosingWindow(), targetPage.getEnclosingWindow());

        page = loadPage(html);
        targetPage = page.getHtmlElementById("a_target").click();
        assertEquals(new URL(URL_FIRST, "#target"), targetPage.getUrl());
        assertFalse(page.getEnclosingWindow().equals(targetPage.getEnclosingWindow()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void targetWithRelativeUrl() throws Exception {
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
    private static String getUrl(final WebWindow w) {
        return w.getEnclosedPage().getUrl().toString();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clickNestedElement_jsDisabled() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "<a href='page2.html'>\n"
            + "<span id='theSpan'>My Link</span></a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        getWebClient().getOptions().setJavaScriptEnabled(false);
        final HtmlPage page = loadPage(html);
        final HtmlElement span = page.getHtmlElementById("theSpan");
        assertEquals("span", span.getTagName());
        final HtmlPage page2 = span.click();
        assertEquals(new URL(URL_FIRST, "page2.html"), page2.getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml_emptyTag() throws Exception {
        final String html = "<html><body>\n"
            + "<a name='foo'></a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlAnchor htmlAnchor = page.getAnchorByName("foo");
        assertTrue(htmlAnchor.asXml().contains("</a>"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickShift() throws Exception {
        final String first
            = "<html><head><title>First</title></head><body>\n"
            + "  <a href='" + URL_SECOND + "' id='a2'>link to foo2</a>\n"
            + "</body></html>";
        final String second
            = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, first);
        webConnection.setResponse(URL_SECOND, second);
        client.setWebConnection(webConnection);

        assertEquals(1, getWebClient().getTopLevelWindows().size());
        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        final HtmlPage secondPage = anchor.click(true, false, false);
        assertEquals(2, getWebClient().getTopLevelWindows().size());
        assertEquals("Second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickCtrl() throws Exception {
        final String first
            = "<html><head><title>First</title></head><body>\n"
            + "  <a href='" + URL_SECOND + "' id='a2'>link to foo2</a>\n"
            + "</body></html>";
        final String second
            = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, first);
        webConnection.setResponse(URL_SECOND, second);
        client.setWebConnection(webConnection);

        assertEquals(1, getWebClient().getTopLevelWindows().size());
        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        final HtmlPage secondPage = anchor.click(false, true, false);
        assertEquals(2, getWebClient().getTopLevelWindows().size());
        assertEquals("First", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickCtrlShift() throws Exception {
        final String first
            = "<html><head><title>First</title></head><body>\n"
            + "  <a href='" + URL_SECOND + "' id='a2'>link to foo2</a>\n"
            + "</body></html>";
        final String second
            = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, first);
        webConnection.setResponse(URL_SECOND, second);
        client.setWebConnection(webConnection);

        assertEquals(1, getWebClient().getTopLevelWindows().size());
        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        final HtmlPage secondPage = anchor.click(true, true, false);
        assertEquals(2, getWebClient().getTopLevelWindows().size());
        assertEquals("First", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickShiftJavascript() throws Exception {
        final String first
            = "<html><head><title>First</title></head><body>\n"
            + "  <a href='javascript: window.location=\"" + URL_SECOND + "\"' id='a2'>link to foo2</a>\n"
            + "</body></html>";
        final String second
            = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, first);
        webConnection.setResponse(URL_SECOND, second);
        client.setWebConnection(webConnection);

        assertEquals(1, getWebClient().getTopLevelWindows().size());
        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        final HtmlPage secondPage = anchor.click(true, false, false);
        assertEquals(2, getWebClient().getTopLevelWindows().size());
        assertEquals("Second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickCtrlJavascript() throws Exception {
        final String first
            = "<html><head><title>First</title></head><body>\n"
            + "  <a href='javascript: window.location=\"" + URL_SECOND + "\"' id='a2'>link to foo2</a>\n"
            + "</body></html>";
        final String second
            = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, first);
        webConnection.setResponse(URL_SECOND, second);
        client.setWebConnection(webConnection);

        assertEquals(1, getWebClient().getTopLevelWindows().size());
        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        final HtmlPage secondPage = anchor.click(false, true, false);
        assertEquals(2, getWebClient().getTopLevelWindows().size());
        assertEquals("First", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickShiftCtrlJavascript() throws Exception {
        final String first
            = "<html><head><title>First</title></head><body>\n"
            + "  <a href='javascript: window.location=\"" + URL_SECOND + "\"' id='a2'>link to foo2</a>\n"
            + "</body></html>";
        final String second
            = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, first);
        webConnection.setResponse(URL_SECOND, second);
        client.setWebConnection(webConnection);

        assertEquals(1, getWebClient().getTopLevelWindows().size());
        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        final HtmlPage secondPage = anchor.click(true, true, false);
        assertEquals(2, getWebClient().getTopLevelWindows().size());
        assertEquals("First", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText_getTextContent() throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <a href='localhost' id='a2'>\n"
            + "    <span>Last Name</span>, <span>First Name</span>\n"
            + "  </a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlAnchor anchor = page.getHtmlElementById("a2");

        assertEquals("Last Name, First Name", anchor.asText());
        assertEquals("\n    Last Name, First Name\n  ", anchor.getTextContent());
    }

    /**
     * Not testable with Selenium.
     *
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "First"},
            IE = {"0", "First"})
    @HtmlUnitNYI(IE = {"1", "First"})
    public void clickWithDownloadAttribute() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <title>First</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <a id='clickMe' href='" + URL_SECOND + "' download='lora.html'>Click Me</a>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "<head><title>Second</title>");
        final int windowsSize = getWebClient().getWebWindows().size();
        final HtmlPage page = loadPage(html);

        page.getElementById("clickMe").click();

        assertEquals("Should have opened a new window",
                windowsSize + Integer.parseInt(getExpectedAlerts()[0]), getWebClient().getWebWindows().size());
        assertEquals("Should not have navigated away", getExpectedAlerts()[1], page.getTitleText());
    }

    /**
     * Not testable with Selenium.
     *
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "First"},
            IE = {"0", "First"})
    @HtmlUnitNYI(IE = {"1", "First"})
    public void clickWithDownloadAttributeFromJs() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <title>First</title>\n"
                + "  <script>\n"
                + "    function test(e) {\n"
                + "      var a = document.getElementById('clickMe');"
                + "      a.setAttribute('download', 'lora.png');"
                + "      a.click();"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <a id='clickMe' href='" + URL_SECOND + "' >Click Me</a>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "<head><title>Second</title>");

        final int windowsSize = getWebClient().getWebWindows().size();
        final HtmlPage page = loadPage(html);

        assertEquals("Should have opened a new window",
                windowsSize + Integer.parseInt(getExpectedAlerts()[0]), getWebClient().getWebWindows().size());
        assertEquals("Should not have navigated away", getExpectedAlerts()[1], page.getTitleText());
    }

    /**
     * Not testable with Selenium.
     *
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "First",
                        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABGdBTUEAAL"
                        + "GPC/xhBQAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9YGARc5KB0XV+IAAAAddEVYdENvbW1l"
                        + "bnQAQ3JlYXRlZCB3aXRoIFRoZSBHSU1Q72QlbgAAAF1JREFUGNO9zL0NglAAxPEfdLTs4BZM4DIO4C"
                        + "7OwQg2JoQ9LE1exdlYvBBeZ7jqch9//q1uH4TLzw4d6+ErXMMcXuHWxId3KOETnnXXV6MJpcq2MLaI"
                        + "97CER3N0vr4MkhoXe0rZigAAAABJRU5ErkJggg=="},
            IE = {"0", "First",
                        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABGdBTUEAAL"
                        + "GPC/xhBQAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9YGARc5KB0XV+IAAAAddEVYdENvbW1l"
                        + "bnQAQ3JlYXRlZCB3aXRoIFRoZSBHSU1Q72QlbgAAAF1JREFUGNO9zL0NglAAxPEfdLTs4BZM4DIO4C"
                        + "7OwQg2JoQ9LE1exdlYvBBeZ7jqch9//q1uH4TLzw4d6+ErXMMcXuHWxId3KOETnnXXV6MJpcq2MLaI"
                        + "97CER3N0vr4MkhoXe0rZigAAAABJRU5ErkJggg=="})
    @HtmlUnitNYI(IE = {"1", "First",
                        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABGdBTUEAAL"
                        + "GPC/xhBQAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9YGARc5KB0XV+IAAAAddEVYdENvbW1l"
                        + "bnQAQ3JlYXRlZCB3aXRoIFRoZSBHSU1Q72QlbgAAAF1JREFUGNO9zL0NglAAxPEfdLTs4BZM4DIO4C"
                        + "7OwQg2JoQ9LE1exdlYvBBeZ7jqch9//q1uH4TLzw4d6+ErXMMcXuHWxId3KOETnnXXV6MJpcq2MLaI"
                        + "97CER3N0vr4MkhoXe0rZigAAAABJRU5ErkJggg=="})
    public void clickWithDownloadAttributeDataUrl() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <title>First</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <a id='clickMe' href='data:image/png;base64,"
                    + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABGdBTUEAALGP"
                    + "C/xhBQAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9YGARc5KB0XV+IA"
                    + "AAAddEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIFRoZSBHSU1Q72QlbgAAAF1J"
                    + "REFUGNO9zL0NglAAxPEfdLTs4BZM4DIO4C7OwQg2JoQ9LE1exdlYvBBeZ7jq"
                    + "ch9//q1uH4TLzw4d6+ErXMMcXuHWxId3KOETnnXXV6MJpcq2MLaI97CER3N0"
                    + "vr4MkhoXe0rZigAAAABJRU5ErkJggg==' download='lora.html'>Click Me</a>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "<head><title>Second</title>");
        final int windowsSize = getWebClient().getWebWindows().size();
        final HtmlPage page = loadPage(html);

        page.getElementById("clickMe").click();

        assertEquals("Should have opened a new window",
                windowsSize + Integer.parseInt(getExpectedAlerts()[0]), getWebClient().getWebWindows().size());
        assertEquals("Should not have navigated away", getExpectedAlerts()[1], page.getTitleText());

        final WebWindow dataWindow = getWebClient().getWebWindows().get(getWebClient().getWebWindows().size() - 1);
        final Page dataPage = dataWindow.getEnclosedPage();
        assertTrue("Should be an UnexpectedPage", dataPage instanceof UnexpectedPage);

        try (InputStream resultInputStream = ((UnexpectedPage) dataPage).getInputStream()) {
            final ImageInputStream resultImageIS = ImageIO.createImageInputStream(resultInputStream);
            final BufferedImage resultBufferedIIS = ImageIO.read(resultImageIS);
            compareImages(getExpectedAlerts()[2], resultBufferedIIS);
        }
    }
}
