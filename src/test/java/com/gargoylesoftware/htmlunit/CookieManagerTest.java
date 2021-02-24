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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.DateUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Unit tests for {@link CookieManager}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CookieManagerTest extends WebDriverTestCase {

    /**
     * Closes the real IE because clearing all cookies seem to be not working
     * at the moment.
     */
    @After
    public void shutDownRealBrowsersAfter() {
        shutDownRealIE();
    }

    /** HTML code with JS code <code>alert(document.cookie)</code>. */
    public static final String HTML_ALERT_COOKIE
        = HtmlPageTest.STANDARDS_MODE_PREFIX_
        + "<html><head><title>foo</title><script>\n"
        + "  function test() {\n"
        // there is no fixed order, sort for stable testing
        + "    var c = document.cookie;\n"
        + "    c = c.split('; ').sort().join('; ');\n"
        + "    alert(c);\n"
        + "  }\n"
        + "</script></head>\n"
        + "<body onload='test()'>\n"
        + "</body></html>";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("my_key=\"Hello, big, big, world\"; yet_another_key=Hi")
    public void comma() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "my_key=\"Hello, big, big, world\""));
        responseHeader.add(new NameValuePair("Set-Cookie", "yet_another_key=Hi"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a_key=helloA; b_key=helloB; c_key=helloC")
    public void orderFromServer() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c_key=helloC"));
        responseHeader.add(new NameValuePair("Set-Cookie", "a_key=helloA"));
        responseHeader.add(new NameValuePair("Set-Cookie", "b_key=helloB"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void orderCookiesByPath_fromJs() throws Exception {
        final String html = "<html><body><script>\n"
            + "document.cookie = 'exampleCookie=rootPath;path=/';\n"
            + "document.cookie = 'exampleCookie=currentPath;path=/testpages/';\n"
            + "</script>\n"
            + "<a href='/testpages/next.html'>next page</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");

        final WebDriver webDriver = getWebDriver();
        webDriver.manage().deleteAllCookies();

        loadPage2(html);
        webDriver.findElement(By.linkText("next page")).click();

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals("exampleCookie=currentPath; exampleCookie=rootPath",
            lastRequest.getAdditionalHeaders().get(HttpHeader.COOKIE));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("key1=; key2=")
    public void emptyCookie() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "key1="));
        responseHeader.add(new NameValuePair("Set-Cookie", "key2="));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("value1")
    public void emptyCookieName() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "=value1"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        loadPageWithAlerts2(URL_FIRST);
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
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "key=value"));
        responseHeader.add(new NameValuePair("Set-Cookie", "test=\"aa= xx==\""));
        getMockWebConnection().setResponse(URL_FIRST, "", 200, "OK", MimeType.TEXT_HTML, responseHeader);
        getMockWebConnection().setDefaultResponse("");

        final WebDriver driver = loadPageWithAlerts2(URL_FIRST);

        driver.get(URL_SECOND.toExternalForm());

        // strange check, but there is no order
        final String lastCookies = getMockWebConnection().getLastAdditionalHeaders().get(HttpHeader.COOKIE);
        assertEquals(26, lastCookies.length());
        assertTrue("lastCookies: " + lastCookies, lastCookies.contains("key=value")
                && lastCookies.contains("test=\"aa= xx==\"")
                && lastCookies.contains("; "));
    }

    /**
     * Regression test for issue 1735.
     * @throws Exception if the test fails
     */
    @Test
    public void valueEmpty() throws Exception {
        ensureCookieValueIsSentBackUnquoted("SID=");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void valueUnsafe() throws Exception {
        // fails with jetty 9.4.6.v20170531 but was working before
        // ensureCookieValueIsSentBackUnquoted("SID=\"");

        ensureCookieValueIsSentBackUnquoted("SID=\"\"");
        ensureCookieValueIsSentBackUnquoted("SID=ab\"cd");

        ensureCookieValueIsSentBackUnquoted("SID=\\");
        ensureCookieValueIsSentBackUnquoted("SID=ab\\cd");
    }

    /**
     * If a Version 1 cookie is set with a value that requires quoting,
     * but wasn't quoted by the server, then this value should be
     * sent back unquoted as well.
     * @throws Exception if the test fails
     */
    @Test
    public void unquotedCookieValueIsSentBackUnquotedAsWell() throws Exception {
        ensureCookieValueIsSentBackUnquoted("SID=1234");

        // even if there are special chars
        ensureCookieValueIsSentBackUnquoted("SID=1234=");
        ensureCookieValueIsSentBackUnquoted("SID=1234:");
        ensureCookieValueIsSentBackUnquoted("SID=1234<");
    }

    private void ensureCookieValueIsSentBackUnquoted(final String cookie) throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("Set-Cookie", cookie + "; Path=/; Version=1"));
        getMockWebConnection().setResponse(URL_FIRST, "", 200, "OK", MimeType.TEXT_HTML, responseHeaders);
        getMockWebConnection().setDefaultResponse("");

        final WebDriver driver = loadPageWithAlerts2(URL_FIRST);
        driver.get(URL_SECOND.toExternalForm());

        final String lastCookie = getMockWebConnection().getLastAdditionalHeaders().get(HttpHeader.COOKIE);
        assertEquals(cookie, lastCookie);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("nbCalls=1")
    public void serverModifiesCookieValue() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "nbCalls=1"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        loadPageWithAlerts2(URL_FIRST);

        final List<NameValuePair> responseHeader2 = new ArrayList<>();
        responseHeader2.add(new NameValuePair("Set-Cookie", "nbCalls=2"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader2);

        setExpectedAlerts("nbCalls=2");
        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first=1")
    public void cookie2() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPageWithAlerts2(URL_FIRST);

        final List<NameValuePair> responseHeader2 = new ArrayList<>();
        responseHeader2.add(new NameValuePair("Set-Cookie", "second=2"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader2);

        setExpectedAlerts("first=1; second=2");
        loadPageWithAlerts2(URL_FIRST);
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
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;expires=Fri, 02-Jan-1970 00:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie",
            "second=2;expires=" + DateUtils.formatDate(aBitLater)));
        responseHeader1.add(new NameValuePair("Set-Cookie", "visit=fo; expires=\"Sat, 07-Apr-2002 13:11:33 GMT\";"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "visitor=f2; expires=\"Sat, 07-Apr-2092 13:11:33 GMT\";"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * Test for document.cookie for cookies expired after the page was loaded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cookies: first=1", "cookies: "})
    public void setCookieTimeout() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head><title>foo</title><script>\n"
                + "  function alertCookies() {\n"
                + "    alert('cookies: ' + document.cookie);\n"
                + "  }\n"

                + "  function test() {\n"
                + "    alertCookies();\n"
                + "    window.setTimeout(alertCookies, 2500);\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        final String expires = DateUtils.formatDate(new Date(System.currentTimeMillis() + 2_000));
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1; expires=" + expires + ";"));
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, responseHeader1);

        loadPageWithAlerts2(URL_FIRST, 4_000);
    }

    /**
     * Regression test for bug 3081652: it seems that expiration date should be ignored if format is incorrect.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "fourth=4; third=3",
            FF = "first=1; second=2; third=3",
            FF78 = "first=1; second=2; third=3",
            IE = "first=1; fourth=4; second=2; third=3")
    public void setCookieExpired_badDateFormat() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;expires=Dec-1-94 16:00:00"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "second=2;expires=Dec-1-1994 16:00:00"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "third=3;expires=Dec-1-2094 16:00:00"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "fourth=4;expires=1/1/2000; path=/"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * Two digits years should be interpreted as 20xx if before 1970 and as 19xx otherwise.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "cookie1=1",
            FF = "cookie1=1; cookie2=2; cookie3=3",
            FF78 = "cookie1=1; cookie2=2; cookie3=3",
            IE = "cookie1=1; cookie2=2; cookie3=3; cookie4=4; cookie5=5; cookie6=6")
    public void setCookieExpires_twoDigits() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie1=1;expires=Sun 01-Dec-68 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie2=2;expires=Thu 01-Dec-69 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie3=3;expires=Mon 31-Dec-69 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie4=4;expires=Thu 01-Jan-70 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie5=5;expires=Tue 01-Dec-70 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie6=6;expires=Wed 01-Dec-71 16:00:00 GMT"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * Two digits years should be interpreted as 20xx if before 1970 and as 19xx otherwise.
     * Same as the test before, only different formating was used.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "cookie1=1",
            FF = "cookie1=1; cookie2=2; cookie3=3",
            FF78 = "cookie1=1; cookie2=2; cookie3=3",
            IE = "cookie1=1; cookie2=2; cookie3=3; cookie4=4; cookie5=5; cookie6=6")
    public void setCookieExpires_twoDigits2() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie1=1;expires=Sun,01 Dec 68 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie2=2;expires=Thu,01 Dec 69 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie3=3;expires=Mon,31 Dec 69 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie4=4;expires=Thu,01 Jan 70 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie5=5;expires=Tue,01 Dec 70 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie6=6;expires=Wed,01 Dec 71 16:00:00 GMT"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * Two digits years should be interpreted as 20xx if before 1970 and as 19xx otherwise,
     * even with a quite strange date format.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "cookie1=1",
            IE = "cookie1=1; cookie6=6")
    public void cookieExpires_TwoDigits3() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie1=1;expires=Sun-01 Dec 68 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie6=6;expires=Wed-01 Dec 71 16:00:00 GMT"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "cookie1=1",
            IE = "cookie1=1; cookie2=2")
    @NotYetImplemented(IE)
    public void cookieExpires_GMT() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie",
                                                "cookie1=1;expires=Sun Jan 20 2042 17:45:00 GMT+0800 (CST)"));
        responseHeader.add(new NameValuePair("Set-Cookie",
                                                "cookie2=2;expires=Sun Jan 20 2004 17:45:00 GMT+0800 (CST)"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * Test some formating errors.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("fifth=5; first=1; fourth=4; second=2; sixth=6; third=3")
    public void cookieExpires_badDateFormat() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;expires=Thu 01-Dec-42 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "second=2;expires=Thu 01 Dec 42 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "third=3;expires=Thu, 01-Dec-42 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "fourth=4;expires=Thu, 01 Dec 42 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "fifth=5;expires=Thu,01-Dec-42 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "sixth=6;expires=Thu,01 Dec 42 16:00:00 GMT"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "dog=dalmation"})
    public void trailing_slash() throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = getWebDriver();

        setExpectedAlerts(expectedAlerts[0]);
        loadPageWithAlerts2(HTML_ALERT_COOKIE, URL_SECOND);

        setExpectedAlerts(expectedAlerts[1]);
        driver.manage().addCookie(new org.openqa.selenium.Cookie("dog", "dalmation", "/second/"));
        loadPageWithAlerts2(HTML_ALERT_COOKIE, URL_SECOND);
    }

    /**
     * Regression test for bug 3032380: expired cookies shouldn't be returned.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Cookies: cookie1=value1; cookie2=value2", "Cookies: cookie2=value2"})
    public void cookieExpiresAfterBeingSet() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function f() {\n"
            + "    alert('Cookies: ' + document.cookie);\n"
            + "  }\n"

            + "  function test() {\n"
            + "    var date1 = new Date();\n"
            + "    date1.setTime(date1.getTime() + 1000);\n"
            + "    document.cookie = 'cookie1=value1; expires=' + date1.toGMTString() + '; path=/';\n"
            + "    var date2 = new Date();\n"
            + "    date2.setTime(date2.getTime() + 60 * 1000);\n"
            + "    document.cookie = 'cookie2=value2; expires=' + date2.toGMTString() + '; path=/';\n"
            + "    f();\n"
            + "    setTimeout(f, 1500);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, 4000);
    }

    /**
     * Regression test for bug 3181695 (introduced during 2.9-SNAPSHOT).
     * @throws Exception if the test fails
     */
    @Test
    public void removeExpiredCookies() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "key1=value1"));
        responseHeader.add(new NameValuePair("Set-Cookie", "key2=value2"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);
        setExpectedAlerts("key1=value1; key2=value2");
        loadPageWithAlerts2(URL_FIRST);

        responseHeader.clear();
        responseHeader.add(new NameValuePair("Set-Cookie", "key1=; path=/; expires=Thu, 01-Jan-1970 00:00:00 GMT"));
        responseHeader.add(new NameValuePair("Set-Cookie", "key2=value2"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        setExpectedAlerts("key2=value2");
        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * Response to / sets cookie to /foo/blah.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "first=1; second=2",
            IE = "first=1")
    public void setCookieSubPath() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;path=/foo/blah"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "second=2;path=/foo/blah/test"));
        responseHeader1.add(new NameValuePair("Location", "/foo/blah/test"));

        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE);
        getMockWebConnection().setResponse(URL_FIRST, "", 302, "Moved", MimeType.TEXT_HTML, responseHeader1);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * Response to /a/b sets cookie to /foo/blah.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "first=1; second=2",
            IE = "first=1")
    public void setCookieDifferentPath() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1; path=/foo/blah"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "second=2; path=/foo/blah/test"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "third=3; path=/foo/other"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "fourth=4; path=/other/path"));
        responseHeader1.add(new NameValuePair("Location", "/foo/blah/test"));

        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(URL_FIRST, "/a/b");
        getMockWebConnection().setResponse(firstUrl, "", 302, "Moved", MimeType.TEXT_HTML, responseHeader1);

        loadPageWithAlerts2(firstUrl);
    }

    /**
     * Response to /a/b sets cookie to /foo/blah.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first=1; second=2")
    public void setCookieDifferentPathSlashAtEnd() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1; path=/foo"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "second=2; path=/foo/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "third=3; path=/foo/other"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "fourth=4; path=/other/path"));
        responseHeader1.add(new NameValuePair("Location", "/foo/"));

        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(URL_FIRST, "/a/b");
        getMockWebConnection().setResponse(firstUrl, "", 302, "Moved", MimeType.TEXT_HTML, responseHeader1);

        loadPageWithAlerts2(firstUrl);
    }

    /**
     * Test for document.cookie for cookies expired after the page was loaded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cookies: first=1", "cookies: "},
            IE = {})
    @HtmlUnitNYI(IE = {"cookies: first=1", "cookies: "})
    public void setCookieDuring302() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head><title>foo</title><script>\n"
                + "  function alertCookies() {\n"
                + "    alert('cookies: ' + document.cookie);\n"
                + "  }\n"

                + "  function test() {\n"
                + "    alertCookies();\n"
                + "    window.setTimeout(alertCookies, 2500);\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

        getMockWebConnection().setDefaultResponse(html);
        final URL firstUrl = new URL(URL_FIRST, "/foo/test.html");

        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        final String expires = DateUtils.formatDate(new Date(System.currentTimeMillis() + 2_000));
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1; expires=" + expires + "; path=/foo"));
        responseHeader1.add(new NameValuePair("Location", "/foo/content.html"));
        getMockWebConnection().setResponse(firstUrl, "", 302, "Moved", MimeType.TEXT_HTML, responseHeader1);

        loadPageWithAlerts2(firstUrl, 4_000);
    }

    /**
     * HttpOnly cookies should not be available from JS.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("second=2")
    public void httpOnly() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "first=1; path=/; HttpOnly"));
        responseHeader.add(new NameValuePair("Set-Cookie", "second=2; path=/;"));

        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPageWithAlerts2(URL_FIRST);
        driver.get(URL_FIRST + "foo");

        final Map<String, String> lastHeaders = getMockWebConnection().getLastAdditionalHeaders();

        // strange check, but there is no order
        final String lastCookies = lastHeaders.get(HttpHeader.COOKIE);
        assertEquals(17, lastCookies.length());
        assertTrue("lastCookies: " + lastCookies, lastCookies.contains("first=1")
                    && lastCookies.contains("second=2")
                    && lastCookies.contains("; "));

        if (driver instanceof HtmlUnitDriver) {
            final CookieManager mgr = getWebWindowOf((HtmlUnitDriver) driver).getWebClient().getCookieManager();
            assertEquals(2, mgr.getCookies().size());
            assertTrue(mgr.getCookie("first").isHttpOnly());
            assertFalse(mgr.getCookie("second").isHttpOnly());
        }
    }

    /**
     * A cookie set with document.cookie without path applies only for the current path
     * and overrides cookie previously set for this path.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first=new")
    public void cookieSetFromJSWithoutPathUsesCurrentLocation() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1"));

        final String html = "<html>\n"
            + "<head></head>\n"
            + "<body><script>\n"
            + "  document.cookie = 'first=new';\n"
            + "  location.replace('/a/b/-');\n"
            + "</script></body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(URL_FIRST, "/a/b");
        getMockWebConnection().setResponse(firstUrl, html, 200, "Ok", MimeType.TEXT_HTML, responseHeader1);

        loadPageWithAlerts2(firstUrl);
    }

    /**
     * A cookie set with document.cookie without path applies only for the current path.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first=1")
    public void cookieSetFromJSWithoutPathUsesCurrentLocation2() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1; path=/c"));

        final String html = "<html>\n"
            + "<head></head>\n"
            + "<body><script>\n"
            + "  document.cookie = 'first=new';\n"
            + "  location.replace('/c/test.html');\n"
            + "</script></body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(URL_FIRST, "/a/b");
        getMockWebConnection().setResponse(firstUrl, html, 200, "Ok", MimeType.TEXT_HTML, responseHeader1);

        loadPageWithAlerts2(firstUrl);
    }

    /**
     * Test for issue #270.
     * @throws Exception in case of error
     *
     * This requires an entry in your hosts file
     * 127.0.0.1       www.htmlunit-local.com
     */
    @Test
    @Alerts("JDSessionID=1234567890")
    public void issue270() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1; path=/c"));

        final String html = "<html>\n"
            + "<head></head>\n"
            + "<body><script>\n"

            + "function setCookie(name, value, expires, path, domain, secure) {\n"
            + "  var curCookie = name + '=' + escape(value) +\n"
            + "    ((expires) ? '; expires=' + expires.toGMTString() : '') +\n"
            + "    ((path) ? '; path=' + path : '') +\n"
            + "    ((domain) ? '; domain=' + domain : '') +\n"
            + "    ((secure) ? '; secure' : '');\n"

            + "  document.cookie = curCookie;\n"
            + "}\n"

            + "var now = new Date();\n"
            + "now.setTime(now.getTime() + 60 * 60 * 1000);\n"
            + "setCookie('JDSessionID', '1234567890', now, '/', 'htmlunit-local.com');\n"

//             + "alert('cookies: ' + document.cookie);\n"

            + "</script></body>\n"
            + "</html>";

        final URL firstUrl = new URL("http://www.htmlunit-local.com:" + PORT + "/");
        getMockWebConnection().setResponse(firstUrl, html);
        loadPage2(html, firstUrl);

        loadPageWithAlerts2(HTML_ALERT_COOKIE, firstUrl);
    }
}
