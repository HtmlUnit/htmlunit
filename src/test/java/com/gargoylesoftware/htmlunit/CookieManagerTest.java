/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.StringUtils;

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
        final CookieStore state = new BasicCookieStore();
        mgr.updateState(state);
        assertEquals(1, state.getCookies().size());

        // Remove the cookie from the manager.
        mgr.removeCookie(cookie);
        assertTrue(mgr.getCookies().isEmpty());

        // Update an HTTP state after removing the cookie.
        mgr.updateState(state);
        assertEquals(0, state.getCookies().size());

        // Add the cookie back to the manager.
        mgr.addCookie(cookie);
        assertFalse(mgr.getCookies().isEmpty());

        // Update an HTTP state after adding the cookie back to the manager.
        mgr.updateState(state);
        assertEquals(1, state.getCookies().size());

        // Clear all cookies from the manager.
        mgr.clearCookies();
        assertTrue(mgr.getCookies().isEmpty());

        // Update an HTTP state after clearing all cookies from the manager.
        mgr.updateState(state);
        assertEquals(0, state.getCookies().size());

        // Disable cookies.
        mgr.setCookiesEnabled(false);
        assertFalse(mgr.isCookiesEnabled());

        // Add a cookie after disabling cookies.
        mgr.addCookie(cookie);
        assertFalse(mgr.getCookies().isEmpty());

        // Update an HTTP state after adding a cookie while cookies are disabled.
        mgr.updateState(state);
        assertEquals(0, state.getCookies().size());

        // Enable cookies again.
        mgr.setCookiesEnabled(true);
        assertTrue(mgr.isCookiesEnabled());

        // Update an HTTP state after enabling cookies again.
        mgr.updateState(state);
        assertEquals(1, state.getCookies().size());

        // Update the manager with a new state.
        final Cookie cookie2 = new Cookie("x", "y");
        final CookieStore state2 = new BasicCookieStore();
        state2.addCookie(cookie2.toHttpClient());
        mgr.updateFromState(state2);
        assertEquals(1, mgr.getCookies().size());
        assertEquals(cookie2, mgr.getCookies().iterator().next());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("my_key=\"Hello, big, big, world\"; yet_another_key=Hi")
    public void comma() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<NameValuePair>();
        responseHeader.add(new NameValuePair("Set-Cookie", "my_key=\"Hello, big, big, world\""));
        responseHeader.add(new NameValuePair("Set-Cookie", "yet_another_key=Hi"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader);

        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c_key=helloC; a_key=helloA; b_key=helloB")
    public void orderFromServer() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<NameValuePair>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c_key=helloC"));
        responseHeader.add(new NameValuePair("Set-Cookie", "a_key=helloA"));
        responseHeader.add(new NameValuePair("Set-Cookie", "b_key=helloB"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader);

        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void orderCookiesByPath_fromJs() throws Exception {
        final String html = "<html><body><script>\n"
            + "document.cookie = 'exampleCookie=rootPath;path=/';\n"
            + "document.cookie = 'exampleCookie=currentPath;path=/testpages/';\n"
            + "</script>\n"
            + "<a href='/testpages/next.html'>next page</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.linkText("next page")).click();

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals("exampleCookie=currentPath; exampleCookie=rootPath",
            lastRequest.getAdditionalHeaders().get("Cookie"));
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
     * @see <a href="https://issues.apache.org/jira/browse/HTTPCLIENT-1006">HttpClient bug 1006</a>
     * @throws Exception if the test fails
     */
    @Test
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
     * Test that " are not discarded.
     * Once this test passes, our hack in HttpWebConnection.HtmlUnitBrowserCompatCookieSpec can safely be removed.
     * @see <a href="https://issues.apache.org/jira/browse/HTTPCLIENT-1006">HttpClient bug 1006</a>
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.NONE)
    public void httpClientParsesCookiesQuotedValuesCorrectly() throws Exception {
        final Header header = new BasicHeader("Set-Cookie", "first=\"hello world\"");
        final BrowserCompatSpec spec = new BrowserCompatSpec();
        final CookieOrigin origin = new CookieOrigin("localhost", 80, "/", false);
        final List<org.apache.http.cookie.Cookie> list = spec.parse(header, origin);
        assertEquals(1, list.size());
        assertEquals("\"hello world\"", list.get(0).getValue());
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
     * Tests for expiration date.
     * Tests as well for bug 3421201 (with expiration date enclosed with quotes).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("second=2; visitor=f2")
    public void setCookieExpired() throws Exception {
        final Date aBitLater = new Date(new Date().getTime() + 60 * 60 * 1000); // one hour later
        final List<NameValuePair> responseHeader1 = new ArrayList<NameValuePair>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;expires=Fri, 02-Jan-1970 00:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie",
            "second=2;expires=" + StringUtils.formatHttpDate(aBitLater)));
        responseHeader1.add(new NameValuePair("Set-Cookie", "visit=fo; expires=\"Sat, 07-Apr-2002 13:11:33 GMT\";"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "visitor=f2; expires=\"Sat, 07-Apr-2092 13:11:33 GMT\";"));
        getMockWebConnection().setResponse(getDefaultUrl(), HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader1);

        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * Regression test for bug 3081652: it seems that expiration date should be ignored if format is incorrect.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first=1; second=2; third=3")
//    @NotYetImplemented
    public void setCookieExpired_badDateFormat() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<NameValuePair>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;expires=Dec-1-94 16:00:00"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "second=2;expires=Dec-1-1994 16:00:00"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "third=3;expires=Dec-1-2094 16:00:00"));
        getMockWebConnection().setResponse(getDefaultUrl(), HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader1);

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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("dog=dalmation")
    public void trailing_slash() throws Exception {
        final WebDriver driver = getWebDriver();
        loadPage2(HTML_ALERT_COOKIE, URL_SECOND);
        driver.manage().addCookie(new org.openqa.selenium.Cookie("dog", "dalmation", "/second/"));
        loadPageWithAlerts2(HTML_ALERT_COOKIE, URL_SECOND);
    }

    /**
     * Regression test for bug 3032380: expired cookies shouldn't be returned.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "Cookies: cookie1=value1; cookie2=value2", "Cookies: cookie2=value2" })
    public void cookieExpiresAfterBeingSet() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "function f() {\n"
            + "  alert('Cookies: ' + document.cookie);\n"
            + "}\n"
            + "\n"
            + "var date1 = new Date();\n"
            + "date1.setTime(date1.getTime() + 1000);\n"
            + "document.cookie = 'cookie1=value1; expires=' + date1.toGMTString() + '; path=/';\n"
            + "var date2 = new Date();\n"
            + "date2.setTime(date2.getTime() + 60 * 1000);\n"
            + "document.cookie = 'cookie2=value2; expires=' + date2.toGMTString() + '; path=/';\n"
            + "f();\n"
            + "setTimeout(f, 1500);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, 2000);
    }

    /**
     * Regression test for bug 3181695 (introduced during 2.9-SNAPSHOT).
     * @throws Exception if the test fails
     */
    @Test
    public void removeExpiredCookies() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<NameValuePair>();
        responseHeader.add(new NameValuePair("Set-Cookie", "key1=value1"));
        responseHeader.add(new NameValuePair("Set-Cookie", "key2=value2"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader);
        setExpectedAlerts("key1=value1; key2=value2");
        loadPageWithAlerts2(getDefaultUrl());

        responseHeader.clear();
        responseHeader.add(new NameValuePair("Set-Cookie", "key1=; path=/; expires=Thu, 01-Jan-1970 00:00:00 GMT"));
        responseHeader.add(new NameValuePair("Set-Cookie", "key2=value2"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader);

        setExpectedAlerts("key2=value2");
        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * Regression test for bug 3053526: HtmlUnit was throwing an Exception when asking for cookies
     * of "about:blank".
     * @throws Exception if the test fails
     */
    @Test
    public void cookiesForAboutBlank() throws Exception {
        final WebClient webClient = getWebClient();
        final HtmlPage htmlPage = webClient.getPage("about:blank");

        final Set<Cookie> cookies = webClient.getCookieManager().getCookies(htmlPage.getUrl());
        assertTrue(cookies.toString(), cookies.isEmpty());
    }

    /**
     * Response to / sets cookie to /foo/blah.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first=1")
    public void setCookieSubPath() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<NameValuePair>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1; path=/foo/blah"));
        responseHeader1.add(new NameValuePair("Location", "/foo/blah"));

        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE);
        getMockWebConnection().setResponse(getDefaultUrl(), "", 302, "Moved", "text/html", responseHeader1);

        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * Response to /a/b sets cookie to /foo/blah.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first=1")
    public void setCookieDifferentPath() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<NameValuePair>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1; path=/foo/blah"));
        responseHeader1.add(new NameValuePair("Location", "/foo/blah"));

        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(getDefaultUrl(), "/a/b");
        getMockWebConnection().setResponse(firstUrl, "", 302, "Moved", "text/html", responseHeader1);

        loadPageWithAlerts2(firstUrl);
    }
}
