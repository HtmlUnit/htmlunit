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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.NONE;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
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
    /** HTML code with JS code <code>alert(document.cookie)</code>. */
    public static final String HTML_ALERT_COOKIE = "<html><head><script>alert(document.cookie)</script>"
        + "<body></body></html>";

    /**
     * Verifies the basic cookie manager behavior.
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(NONE)
    public void basicBehavior() throws Exception {
        // Create a new cookie manager.
        final CookieManager mgr = new CookieManager();
        assertTrue(mgr.isCookiesEnabled());
        assertTrue(mgr.getCookies().isEmpty());

        // Add a cookie to the manager.
        final Cookie cookie = new Cookie("localhost", "a", "b");
        mgr.addCookie(cookie);
        assertFalse(mgr.getCookies().isEmpty());

        // Remove the cookie from the manager.
        mgr.removeCookie(cookie);
        assertTrue(mgr.getCookies().isEmpty());

        // Add the cookie back to the manager.
        mgr.addCookie(cookie);
        assertFalse(mgr.getCookies().isEmpty());

        // Clear all cookies from the manager.
        mgr.clearCookies();
        assertTrue(mgr.getCookies().isEmpty());

        // Disable cookies.
        mgr.setCookiesEnabled(false);
        assertFalse(mgr.isCookiesEnabled());

        final Cookie cookie2 = new Cookie("a", "b", "c", "d", new Date(System.currentTimeMillis() + 5000), false);

        // Add a cookie after disabling cookies.
        mgr.addCookie(cookie);
        mgr.addCookie(cookie2);
        assertEquals(2, mgr.getCookies().size());

        // Enable cookies again.
        mgr.setCookiesEnabled(true);
        assertTrue(mgr.isCookiesEnabled());

        // Clear expired cookies
        assertTrue(mgr.clearExpired(new Date(System.currentTimeMillis() + 10000)));
        assertEquals(1, mgr.getCookies().size());
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
    @Browsers(NONE)
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
    @Alerts(DEFAULT = "first=1; second=2; third=3", CHROME = "third=3")
    public void setCookieExpired_badDateFormat() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<NameValuePair>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;expires=Dec-1-94 16:00:00"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "second=2;expires=Dec-1-1994 16:00:00"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "third=3;expires=Dec-1-2094 16:00:00"));
        getMockWebConnection().setResponse(getDefaultUrl(), HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader1);

        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * Two digits years should be interpreted as 20xx if before 1970 and as 19xx otherwise.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cookie1=1; cookie2=2; cookie3=3")
    public void setCookieExpires_twoDigits() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<NameValuePair>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie1=1;expires=Sun 01-Dec-68 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie2=2;expires=Thu 01-Dec-69 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie3=3;expires=Mon 31-Dec-69 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie4=4;expires=Thu 01-Jan-70 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie5=5;expires=Tue 01-Dec-70 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie6=6;expires=Wed 01-Dec-71 16:00:00 GMT"));
        getMockWebConnection().setResponse(getDefaultUrl(), HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader1);

        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * Two digits years should be interpreted as 20xx if before 1970 and as 19xx otherwise.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cookie1=1; cookie2=2; cookie3=3")
    public void setCookieExpires_twoDigits2() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<NameValuePair>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie1=1;expires=Sun,01 Dec 68 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie2=2;expires=Thu,01 Dec 69 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie3=3;expires=Mon,31 Dec 69 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie4=4;expires=Thu,01 Jan 70 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie5=5;expires=Tue,01 Dec 70 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie6=6;expires=Wed,01 Dec 71 16:00:00 GMT"));
        getMockWebConnection().setResponse(getDefaultUrl(), HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader1);

        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * Two digits years should be interpreted as 20xx if before 1970 and as 19xx otherwise,
     * even with a quite strange date format.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cookie1=1")
    public void setCookieExpires_badDateFormat() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<NameValuePair>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie1=1;expires=Sun-01 Dec 68 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie6=6;expires=Wed-01 Dec 71 16:00:00 GMT"));
        getMockWebConnection().setResponse(getDefaultUrl(), HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader1);

        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts
    public void setCookieExpired_badDateFormat2() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<NameValuePair>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;expires=Thu 01-Dec-94 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "second=2;expires=Thu 01 Dec 94 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "third=3;expires=Thu, 01-Dec-94 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "fourth=4;expires=Thu, 01 Dec 94 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "fifth=5;expires=Thu,01-Dec-94 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "sixth=6;expires=Thu,01 Dec 94 16:00:00 GMT"));
        getMockWebConnection().setResponse(getDefaultUrl(), HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader1);

        loadPageWithAlerts2(getDefaultUrl());
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

    /**
     * HttpOnly cookies should not be available from JS.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("second=2")
    public void httpOnly() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<NameValuePair>();
        responseHeader.add(new NameValuePair("Set-Cookie", "first=1; path=/; HttpOnly"));
        responseHeader.add(new NameValuePair("Set-Cookie", "second=2; path=/;"));

        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(getDefaultUrl(), HTML_ALERT_COOKIE, 200, "OK", "text/html", responseHeader);

        final WebDriver driver = loadPageWithAlerts2(getDefaultUrl());
        driver.get(getDefaultUrl().toString() + "foo");

        final Map<String, String> lastHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertEquals("first=1; second=2", lastHeaders.get("Cookie"));

        if (driver instanceof HtmlUnitDriver) {
            final CookieManager mgr = getWebWindowOf((HtmlUnitDriver) driver).getWebClient().getCookieManager();
            assertEquals(2, mgr.getCookies().size());
            assertTrue(mgr.getCookie("first").isHttpOnly());
            assertFalse(mgr.getCookie("second").isHttpOnly());
        }
    }
}
