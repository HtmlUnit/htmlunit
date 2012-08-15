/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * Tests for {@link HTMLDocument}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLDocument2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(FF = { "loading,[object HTMLBodyElement]-complete,[object HTMLBodyElement]-" })
    public void readyState() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <script>\n"
            + "    var doc;"
            + "    function test() {\n"
            + "      var iframe = document.createElement('iframe');\n"
            + "      var textarea = document.getElementById('myTextarea');\n"
            + "      textarea.parentNode.appendChild(iframe);\n"
            + "      doc = iframe.contentWindow.document;\n"
            + "      check();\n"
            + "      setTimeout(check, 100);\n"
            + "    }\n"
            + "    function check() {\n"
            + "      var textarea = document.getElementById('myTextarea');\n"
            + "      textarea.value += doc.readyState + ',' + doc.body + '-';\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div>\n"
            + "  <textarea id='myTextarea' cols='80'></textarea>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);
        page.getWebClient().waitForBackgroundJavaScript(500);

        final List<String> actual = new LinkedList<String>();
        actual.add(page.<HtmlTextArea>getHtmlElementById("myTextarea").getText());

        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "www.gargoylesoftware.com", "gargoylesoftware.com" })
    public void domain() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'gargoylesoftware.com';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html, new URL("http://www.gargoylesoftware.com/"), -1);
    }

  /**
    * @throws Exception if the test fails
    */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "www.gargoylesoftware.com", "gargoylesoftware.com" })
    public void domainMixedCaseNetscape() throws Exception {
        final URL urlGargoyleUpperCase = new URL("http://WWW.GARGOYLESOFTWARE.COM/");

        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'GaRgOyLeSoFtWaRe.CoM';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(new URL("http://www.gargoylesoftware.com/"), html);
        loadPageWithAlerts(html, urlGargoyleUpperCase, -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "www.gargoylesoftware.com", "gargoylesoftware.com" },
            IE = { "www.gargoylesoftware.com", "GaRgOyLeSoFtWaRe.CoM" })
    public void domainMixedCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'GaRgOyLeSoFtWaRe.CoM';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html, new URL("http://www.gargoylesoftware.com/"), -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "d4.d3.d2.d1.gargoylesoftware.com", "d4.d3.d2.d1.gargoylesoftware.com", "d1.gargoylesoftware.com" })
    public void domainLong() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'd4.d3.d2.d1.gargoylesoftware.com';\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'd1.gargoylesoftware.com';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(getWebClient(), html, collectedAlerts, new URL("http://d4.d3.d2.d1.gargoylesoftware.com"));
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "localhost", "localhost" })
    public void domainSetSelf() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'localhost';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(getWebClient(), html, collectedAlerts, new URL("http://localhost"));
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domainTooShort() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'com';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        try {
            loadPage(getWebClient(), html, collectedAlerts);
        }
        catch (final ScriptException ex) {
            return;
        }
        fail();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "www.gargoylesoftware.com", "www.gargoylesoftware.com" },
            IE = { "www.gargoylesoftware.com", "www.gargoylesoftware.com", "exception" })
    public void domain_set_for_about_blank() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "  var domain = document.domain;\n"
            + "  alert(domain);\n"
            + "  var frameDoc = frames[0].document;\n"
            + "  alert(frameDoc.domain);\n"
            + "  try {\n"
            + "    frameDoc.domain = domain;\n"
            + "  } catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe src='about:blank'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts(html, new URL("http://www.gargoylesoftware.com/"), -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "one", "two", "three", "four" })
    public void cookie_read() throws Exception {
        final WebClient webClient = getWebClientWithMockWebConnection();

        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var cookieSet = document.cookie.split('; ');\n"
            + "    var setSize = cookieSet.length;\n"
            + "    var crumbs;\n"
            + "    var x=0;\n"
            + "    for (x=0;((x<setSize)); x++) {\n"
            + "        crumbs = cookieSet[x].split('=');\n"
            + "        alert(crumbs[0]);\n"
            + "        alert(crumbs[1]);\n"
            + "    } \n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final URL url = getDefaultUrl();
        getMockWebConnection().setResponse(url, html);

        final CookieManager mgr = webClient.getCookieManager();
        mgr.addCookie(new Cookie(url.getHost(), "one", "two", "/", null, false));
        mgr.addCookie(new Cookie(url.getHost(), "three", "four", "/", null, false));

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage firstPage = webClient.getPage(url);
        assertEquals("First", firstPage.getTitleText());

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * This one can't be tested with WebDriver.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "false", "", "", "" })
    public void cookie_write_cookiesDisabled() throws Exception {
        final String html = HTMLDocumentTest.getCookieWriteHtmlCode();

        final WebClient client = getWebClientWithMockWebConnection();
        client.getCookieManager().setCookiesEnabled(false);

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that cookies work when working with local files (not remote sites with real domains).
     * Required for local testing of Dojo 1.1.1.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "", "blah=bleh" })
    public void cookieInLocalFile() throws Exception {
        final WebClient client = getWebClient();

        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final URL url = getClass().getResource("HTMLDocumentTest_cookieInLocalFile.html");
        client.getPage(url);

        assertEquals(getExpectedAlerts(), actual);
    }

}
