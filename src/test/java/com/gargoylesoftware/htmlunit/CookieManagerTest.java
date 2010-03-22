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
package com.gargoylesoftware.htmlunit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Unit tests for {@link CookieManager}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class CookieManagerTest extends WebDriverTestCase {
    private static final String HTML_ALERT_COOKIE = "<html><head><script>alert(document.cookie)</script>"
        + "<body></body></html>";

    /**
     * Verifies the basic cookie manager behavior.
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void basicBehavior() throws Exception {
        // Create a new cookie manager.
        final CookieManager mgr = new CookieManager();
        assertTrue(mgr.isCookiesEnabled());
        assertTrue(mgr.getCookies().isEmpty());

        // Add a cookie to the manager.
        final Cookie cookie = new Cookie("a", "b");
        mgr.addCookie(cookie);
        assertFalse(mgr.getCookies().isEmpty());

        // Update an HTTP state.
        final HttpState state = new HttpState();
        mgr.updateState(state);
        assertEquals(1, state.getCookies().length);

        // Remove the cookie from the manager.
        mgr.removeCookie(cookie);
        assertTrue(mgr.getCookies().isEmpty());

        // Update an HTTP state after removing the cookie.
        mgr.updateState(state);
        assertEquals(0, state.getCookies().length);

        // Add the cookie back to the manager.
        mgr.addCookie(cookie);
        assertFalse(mgr.getCookies().isEmpty());

        // Update an HTTP state after adding the cookie back to the manager.
        mgr.updateState(state);
        assertEquals(1, state.getCookies().length);

        // Clear all cookies from the manager.
        mgr.clearCookies();
        assertTrue(mgr.getCookies().isEmpty());

        // Update an HTTP state after clearing all cookies from the manager.
        mgr.updateState(state);
        assertEquals(0, state.getCookies().length);

        // Disable cookies.
        mgr.setCookiesEnabled(false);
        assertFalse(mgr.isCookiesEnabled());

        // Add a cookie after disabling cookies.
        mgr.addCookie(cookie);
        assertFalse(mgr.getCookies().isEmpty());

        // Update an HTTP state after adding a cookie while cookies are disabled.
        mgr.updateState(state);
        assertEquals(0, state.getCookies().length);

        // Enable cookies again.
        mgr.setCookiesEnabled(true);
        assertTrue(mgr.isCookiesEnabled());

        // Update an HTTP state after enabling cookies again.
        mgr.updateState(state);
        assertEquals(1, state.getCookies().length);

        // Update the manager with a new state.
        final Cookie cookie2 = new Cookie("x", "y");
        final HttpState state2 = new HttpState();
        state2.addCookie(cookie2.toHttpClient());
        mgr.updateFromState(state2);
        assertEquals(1, mgr.getCookies().size());
        assertEquals(cookie2, mgr.getCookies().iterator().next());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("my_key=\"Hello, big, big, world\"; another_key=Hi")
    public void comma() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<NameValuePair>();
        responseHeader.add(new NameValuePair("Set-Cookie", "my_key=\"Hello, big, big, world\""));
        responseHeader.add(new NameValuePair("Set-Cookie", "another_key=Hi"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader);

        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("key1=; key2=")
    public void emptyCookie() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<NameValuePair>();
        responseHeader.add(new NameValuePair("Set-Cookie", "key1="));
        responseHeader.add(new NameValuePair("Set-Cookie", "key2="));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader);

        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * Regression test for issue 2973040.
     * When a cookie is set with value within quotes, this value should be sent within quotes
     * in the following requests. This is a problem (bug?) in HttpClient which is not fixed in HttpClient-4.0.1.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void valueQuoted() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<NameValuePair>();
        responseHeader.add(new NameValuePair("Set-Cookie", "key=value"));
        responseHeader.add(new NameValuePair("Set-Cookie", "test=\"aa= xx==\""));
        getMockWebConnection().setResponse(getDefaultUrl(), "", 200, "OK", "text/html", responseHeader);
        getMockWebConnection().setDefaultResponse("");

        final WebDriver driver = loadPageWithAlerts2(getDefaultUrl());

        driver.get(URL_SECOND.toExternalForm());

        assertEquals("key=value; test=\"aa= xx==\"", getMockWebConnection().getLastAdditionalHeaders().get("Cookie"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("nbCalls=1")
    public void serverModifiesCookieValue() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<NameValuePair>();
        responseHeader.add(new NameValuePair("Set-Cookie", "nbCalls=1"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader);

        loadPageWithAlerts2(getDefaultUrl());

        final List<NameValuePair> responseHeader2 = new ArrayList<NameValuePair>();
        responseHeader2.add(new NameValuePair("Set-Cookie", "nbCalls=2"));
        getMockWebConnection().setResponse(getDefaultUrl(), HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader2);

        setExpectedAlerts("nbCalls=2");
        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void resettingCookie() throws Exception {
        final String html
            = "<html><head><title>foo</title>"
            + "<script>\n"
            + "  function createCookie(name, value, days, path) {\n"
            + "    if (days) {\n"
            + "      var date = new Date();\n"
            + "      date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));\n"
            + "      var expires = '; expires=' + date.toGMTString();\n"
            + "    }\n"
            + "    else\n"
            + "      var expires = '';\n"
            + "    document.cookie = name + '=' + value + expires + '; path=' + path;\n"
            + "  }\n"
            + "\n"
            + "  function readCookie(name) {\n"
            + "    var nameEQ = name + '=';\n"
            + "    var ca = document.cookie.split(';');\n"
            + "    for(var i = 0; i < ca.length; i++) {\n"
            + "      var c = ca[i];\n"
            + "      while (c.charAt(0) == ' ')\n"
            + "        c = c.substring(1, c.length);\n"
            + "      if (c.indexOf(nameEQ) == 0)\n"
            + "        return c.substring(nameEQ.length, c.length);\n"
            + "    }\n"
            + "    return null;\n"
            + "  }\n"
            + "</script></head><body>\n"
            + "  <input id='button1' type='button' "
            + "onclick=\"createCookie('button','button1',1,'/'); alert('cookie:' + readCookie('button'));\" "
            + "value='Button 1'>\n"
            + "  <input id='button2' type='button' "
            + "onclick=\"createCookie('button','button2',1,'/'); alert('cookie:' + readCookie('button'));\" "
            + "value='Button 2'>\n"
            + "</form></body></html>";

        final String[] expectedAlerts = {"cookie:button1", "cookie:button2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(html, collectedAlerts);
        page.<HtmlButtonInput>getHtmlElementById("button1").click();
        page.<HtmlButtonInput>getHtmlElementById("button2").click();
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first=1")
    public void cookie2() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<NameValuePair>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1"));
        getMockWebConnection().setResponse(getDefaultUrl(), HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader1);

        loadPageWithAlerts2(getDefaultUrl());

        final List<NameValuePair> responseHeader2 = new ArrayList<NameValuePair>();
        responseHeader2.add(new NameValuePair("Set-Cookie", "second=2"));
        getMockWebConnection().setResponse(getDefaultUrl(), HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader2);

        setExpectedAlerts("first=1; second=2");
        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void cookie_nullValue() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final URL url = URL_FIRST;
        webConnection.setResponse(url, HTML_ALERT_COOKIE);
        webClient.setWebConnection(webConnection);

        final CookieManager mgr = webClient.getCookieManager();
        mgr.addCookie(new Cookie(URL_FIRST.getHost(), "my_key", null, "/", null, false));

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webClient.getPage(URL_FIRST);

        final String[] expectedAlerts = {"my_key="};
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
