/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Unit tests for {@link HtmlAnchor}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Stefan Anzinger
 * @author Ahmed Ashour
 */
public class HtmlAnchorTest extends WebTestCase {

    /**
     * Create an instance
     *
     * @param name Name of the test
     */
    public HtmlAnchorTest(final String name) {
        super(name);
    }

    /**
     * Verifies that anchor href attributes are trimmed of whitespace (bug 1658064),
     * just like they are in IE and Firefox.
     * @throws Exception If an error occurs.
     */
    public void testHrefTrimmed() throws Exception {
        final String html = "<html><body onload='"
            + "alert(document.getElementById(\"a\").href.length);\n"
            + "alert(document.getElementById(\"b\").href.length);'>\n"
            + "<a href=' http://a/ ' id='a'>a</a> "
            + "<a href='  http://b/    ' id='b'>b</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(html, collectedAlerts);

        final String[] expectedAlerts = {"9", "9"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClick() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlAnchor anchor = (HtmlAnchor) page.getHtmlElementById("a2");

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = (HtmlPage) anchor.click();

        final List expectedParameters = Collections.EMPTY_LIST;
        final MockWebConnection webConnection = getMockConnection(secondPage);

        assertEquals("url", "http://www.foo2.com", secondPage.getWebResponse().getUrl());
        assertEquals("method", SubmitMethod.GET, webConnection.getLastMethod());
        assertEquals("parameters", expectedParameters, webConnection.getLastParameters());
        assertNotNull(secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClickAnchorName() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='#clickedAnchor' id='a1'>link to foo1</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlAnchor anchor = (HtmlAnchor) page.getHtmlElementById("a1");

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = (HtmlPage) anchor.click();

        // The url shouldn't contain the anchor since isn't sent to the server
        assertEquals("url", URL_GARGOYLE, secondPage.getWebResponse().getUrl());
        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
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

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlAnchor anchor = (HtmlAnchor) page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = (HtmlPage) anchor.click();

        assertEquals(new String[] {"clicked"}, collectedAlerts);
        assertEquals("Second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClick_onClickHandler_returnFalse() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='" + URL_SECOND + "' id='a2' "
            + "onClick='alert(\"clicked\");return false;'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlAnchor anchor = (HtmlAnchor) page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = (HtmlPage) anchor.click();

        assertEquals(new String[] {"clicked"}, collectedAlerts);
        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClick_onClickHandler_javascriptDisabled() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='http://www.foo2.com' id='a2' "
            + "onClick='alert(\"clicked\")'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final WebClient client = new WebClient();
        client.setJavaScriptEnabled(false);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_GARGOYLE);
        final HtmlAnchor anchor = (HtmlAnchor) page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = (HtmlPage) anchor.click();

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final List< ? > expectedParameters = Collections.EMPTY_LIST;

        assertEquals("url", "http://www.foo2.com", secondPage.getWebResponse().getUrl());
        assertEquals("method", SubmitMethod.GET, webConnection.getLastMethod());
        assertEquals("parameters", expectedParameters, webConnection.getLastParameters());
        assertNotNull(secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClick_javascriptUrl() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='javascript:alert(\"clicked\")' id='a2'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlAnchor anchor = (HtmlAnchor) page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = (HtmlPage) anchor.click();

        assertEquals(new String[] {"clicked"}, collectedAlerts);
        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClick_javascriptUrl_javascriptDisabled() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='javascript:alert(\"clicked\")' id='a2'>link to foo2</a>\n"
            + "<a href='http://www.foo3.com' id='a3'>link to foo3</a>\n"
            + "</body></html>";
        final WebClient client = new WebClient();
        client.setJavaScriptEnabled(false);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_GARGOYLE);
        final HtmlAnchor anchor = (HtmlAnchor) page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = (HtmlPage) anchor.click();

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClick_javascriptUrl_InvalidReturn_RegressionTest() throws Exception {
        final String htmlContent
            = "<html><head><SCRIPT lang=\"JavaScript\">\n"
            + "function doSubmit(formName){"
            + "    return false;\n"
            + "}"
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

        final WebClient client = new WebClient();
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        conn.setResponse(URL_THIRD, thirdContent);
        client.setWebConnection(conn);
        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlAnchor a = (HtmlAnchor) firstPage.getHtmlElementById("link");
        final HtmlPage secondPage = (HtmlPage) a.click();
        assertEquals("url", URL_SECOND, secondPage.getWebResponse().getUrl());
        assertEquals("title", "Page B", secondPage.getTitleText());
    }
    
    /**
     * Test for new openLinkInNewWindow() method
     * @throws Exception on test failure
     */
    public void testOpenLinkInNewWindow() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlAnchor anchor = (HtmlAnchor) page.getHtmlElementById("a1");

        assertEquals("size incorrect before test", 1, page.getWebClient().getWebWindows().size());

        final HtmlPage secondPage = (HtmlPage) anchor.openLinkInNewWindow();

        assertNotSame("new page not returned", page, secondPage);
        assertInstanceOf(
                "new page in wrong window type",
                secondPage.getEnclosingWindow(),
                TopLevelWindow.class);
        assertEquals("new window not created", 2, page.getWebClient().getWebWindows().size());
        assertNotSame("new window not used", page.getEnclosingWindow(), secondPage
                .getEnclosingWindow());
    }

    /**
     * Test the 'Referer' HTTP header
     * @throws Exception on test failure
     */
    public void testClick_refererHeader() throws Exception {
        final String firstContent
            = "<html><head><title>Page A</title></head>\n"
            + "<body><a href='" + URL_SECOND + "' id='link'>link</a></body>\n"
            + "</html>";
        final String secondContent
            = "<html><head><title>Page B</title></head>\n"
            + "<body></body>\n"
            + "</html>";

        final WebClient client = new WebClient();
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(conn);
        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlAnchor a = (HtmlAnchor) firstPage.getHtmlElementById("link");
        a.click();

        final Map<String, String> lastAdditionalHeaders = conn.getLastAdditionalHeaders();
        assertEquals(URL_FIRST.toString(), lastAdditionalHeaders.get("Referer"));
    }
    
    /**
     * https://sourceforge.net/tracker/index.php?func=detail&aid=1587110&group_id=47038&atid=448266
     *
     * Links with an href and a non-false returning onclick that opens a new window should still
     * open the href in the first window.
     *
     * @throws Exception on test failure
     */
    public void testCorrectLinkTargetWhenOnclickOpensWindow() throws Exception {
        final String firstContent = "<html><head><title>First</title></head><body>\n"
            + "<a href='"
            + URL_SECOND
            + "' id='clickme' onclick=\"window.open('', 'newWindow');\">X</a>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlAnchor anchor = (HtmlAnchor) firstPage.getHtmlElementById("clickme");
        final HtmlPage secondPage = (HtmlPage) anchor.click();

        assertEquals("Second window did not open", 2, client.getWebWindows().size());
        assertNotSame("New Page was not returned", firstPage, secondPage);
        assertEquals("Wrong new Page returned", "Second", secondPage.getTitleText());
        assertSame("New Page not in correct WebWindow", firstPage.getEnclosingWindow(), secondPage
                .getEnclosingWindow());
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testPreventDefault() throws Exception {
        testPreventDefault(BrowserVersion.FIREFOX_2);
        testPreventDefault(BrowserVersion.INTERNET_EXPLORER_7_0);
    }

    private void testPreventDefault(final BrowserVersion browserVersion) throws Exception {
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

        final HtmlPage page = (HtmlPage) loadPage(browserVersion, html, null);
        final HtmlAnchor a1 = (HtmlAnchor) page.getHtmlElementById("a1");
        final HtmlPage secondPage = (HtmlPage) a1.click();
        assertEquals(URL_GARGOYLE, secondPage.getWebResponse().getUrl());
    }
}
