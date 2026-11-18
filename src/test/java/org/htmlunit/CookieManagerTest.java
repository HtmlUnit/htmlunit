/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.DateUtils;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Unit tests for {@link CookieManager}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 * @author Lai Quang Duong
 * @author Vidya m
 */
public class CookieManagerTest extends WebDriverTestCase {

    /** HTML code with JS code <code>alert(document.cookie)</code>. */
    public static final String HTML_ALERT_COOKIE = DOCTYPE_HTML
        + "<html><head>\n"
        + "<script>\n"
        + LOG_TITLE_FUNCTION
        + "  function test() {\n"
        // there is no fixed order, sort for stable testing
        + "    var c = document.cookie;\n"
        + "    c = c.split('; ').sort().join('; ');\n"
        + "    log(c);\n"
        + "  }\n"
        + "</script>\n"
        + "</head>\n"
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

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
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

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"b=2; a=1",
             "c=1; d=2",
             "lA=2; lB=3; sA=1; sB=4"})
    public void orderCookiesByPath_fromJs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function clear(n, p) {\n"
            + "    document.cookie = n + '=; path=' + p + '; max-age=0';\n"
            + "  }\n"
            + "  function test() {\n"
            + "    document.cookie = 'a=1; path=/';\n"
            + "    document.cookie = 'b=2; path=/testpages';\n"
            + "    log(document.cookie);\n"
            + "    clear('a', '/'); clear('b', '/testpages');\n"
            + "    document.cookie = 'c=1; path=/testpages';\n"
            + "    document.cookie = 'd=2; path=/testpages';\n"
            + "    log(document.cookie);\n"
            + "    clear('c', '/testpages'); clear('d', '/testpages');\n"
            + "    document.cookie = 'sA=1; path=/';\n"
            + "    document.cookie = 'lA=2; path=/testpages';\n"
            + "    document.cookie = 'lB=3; path=/testpages';\n"
            + "    document.cookie = 'sB=4; path=/';\n"
            + "    log(document.cookie);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<a href='/testpages/next.html'>next page</a>\n"
            + "</body></html>";

        final URL pageUrl = new URL(URL_FIRST, "testpages/test.html");
        getMockWebConnection().setResponse(pageUrl, html);
        getMockWebConnection().setDefaultResponse("");

        final WebDriver webDriver = getWebDriver();
        webDriver.manage().deleteAllCookies();

        loadPage2(pageUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());

        webDriver.findElement(By.linkText("next page")).click();

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals("lA=2; lB=3; sA=1; sB=4",
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

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "value1",
            FF = "",
            FF_ESR = "")
    @HtmlUnitNYI(FF = "value1",
            FF_ESR = "value1")
    public void emptyCookieName() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "=value1"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Test that " are not discarded.
     * Regression test for issue 2973040.
     * When a cookie is set with value within quotes, this value should be sent within quotes
     * in the following requests. This is a bug in HttpClient which is not fixed in HttpClient-4.0.1.
     * @see <a href="https://issues.apache.org/jira/browse/HTTPCLIENT-1006">HttpClient bug 1006</a>
     * @throws Exception if the test fails
     */
    @Test
    public void valueQuoted() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "key=value"));
        responseHeader.add(new NameValuePair("Set-Cookie", "quoted=\"hello world\""));
        responseHeader.add(new NameValuePair("Set-Cookie", "quotedEquals=\"aa= xx==\""));
        getMockWebConnection().setResponse(URL_FIRST, "", 200, "OK", MimeType.TEXT_HTML, responseHeader);
        getMockWebConnection().setDefaultResponse("");

        final WebDriver driver = loadPageWithAlerts2(URL_FIRST);

        driver.get(URL_SECOND.toExternalForm());

        final String lastCookies = getMockWebConnection().getLastAdditionalHeaders().get(HttpHeader.COOKIE);
        final List<String> cookies = Arrays.asList(lastCookies.split(";\\s"));
        Collections.sort(cookies);
        assertEquals(new String[] {"key=value", "quoted=\"hello world\"", "quotedEquals=\"aa= xx==\""}, cookies);
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

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());

        final List<NameValuePair> responseHeader2 = new ArrayList<>();
        responseHeader2.add(new NameValuePair("Set-Cookie", "nbCalls=2"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader2);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), "nbCalls=2");
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

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());

        final List<NameValuePair> responseHeader2 = new ArrayList<>();
        responseHeader2.add(new NameValuePair("Set-Cookie", "second=2"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader2);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), "first=1; second=2");
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

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Test for document.cookie for cookies expired after the page was loaded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cookies: first=1", "cookies: "})
    public void setCookieTimeout() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function alertCookies() {\n"
                + "    log('cookies: ' + document.cookie);\n"
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

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(DEFAULT_WAIT_TIME.multipliedBy(4), getWebDriver(), getExpectedAlerts());
    }

    /**
     * Regression test for bug 3081652: it seems that expiration date should be ignored if format is incorrect.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "fourth=4; third=3",
            FF = "first=1; second=2; third=3",
            FF_ESR = "first=1; second=2; third=3")
    public void setCookieExpired_badDateFormat() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;expires=Dec-1-94 16:00:00"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "second=2;expires=Dec-1-1994 16:00:00"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "third=3;expires=Dec-1-2094 16:00:00"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "fourth=4;expires=1/1/2000; path=/"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Two digits years should be interpreted as 20xx if before 1970 and as 19xx otherwise.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cookie1=1; cookie2=2; cookie3=3")
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

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Two digits years should be interpreted as 20xx if before 1970 and as 19xx otherwise.
     * Same as the test before, only different formating was used.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cookie1=1; cookie2=2; cookie3=3")
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

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Two digits years should be interpreted as 20xx if before 1970 and as 19xx otherwise,
     * even with a quite strange date format.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cookie1=1")
    public void cookieExpires_TwoDigits3() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie1=1;expires=Sun-01 Dec 68 16:00:00 GMT"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "cookie6=6;expires=Wed-01 Dec 71 16:00:00 GMT"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cookie1=1")
    public void cookieExpires_GMT() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie",
                                                "cookie1=1;expires=Sun Jan 20 2042 17:45:00 GMT+0800 (CST)"));
        responseHeader.add(new NameValuePair("Set-Cookie",
                                                "cookie2=2;expires=Sun Jan 20 2004 17:45:00 GMT+0800 (CST)"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
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

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first=1")
    public void cookieMaxAge() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;max-age=1000"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "second=2;max-age=-1"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first=1", ""})
    public void cookieMaxAge_zeroDeletes() throws Exception {
        List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;max-age=1000"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts()[0]);

        responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;max-age=0"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first=1", ""})
    public void cookieMaxAge_negativeDeletes() throws Exception {
        List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;max-age=1000"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts()[0]);

        responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;max-age=-1"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first=1", ""})
    public void cookieMaxAge_moreThan400Days() throws Exception {
        final String moreThan400Days = "" + ((400 * 24 * 60 * 60) + 60);

        List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;max-age=" + moreThan400Days));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first=1", ""})
    public void cookieMaxAge_long() throws Exception {
        List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;max-age=99999999999"));
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader1);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts()[0]);
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
        loadPage2(HTML_ALERT_COOKIE, URL_SECOND);
        verifyTitle2(getWebDriver(), getExpectedAlerts());

        setExpectedAlerts(expectedAlerts[1]);
        driver.manage().addCookie(new org.openqa.selenium.Cookie("dog", "dalmation", "/second/"));
        loadPage2(HTML_ALERT_COOKIE, URL_SECOND);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Regression test for bug 3032380: expired cookies shouldn't be returned.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Cookies: cookie1=value1; cookie2=value2", "Cookies: cookie2=value2"})
    public void cookieExpiresAfterBeingSet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function f() {\n"
            + "    log('Cookies: ' + document.cookie);\n"
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

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME.multipliedBy(4), getWebDriver(), getExpectedAlerts());
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

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), "key1=value1; key2=value2");

        responseHeader.clear();
        responseHeader.add(new NameValuePair("Set-Cookie", "key1=; path=/; expires=Thu, 01-Jan-1970 00:00:00 GMT"));
        responseHeader.add(new NameValuePair("Set-Cookie", "key2=value2"));
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), "key2=value2");
    }

    /**
     * Response to / sets cookie to /foo/blah.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first=1; second=2")
    public void setCookieSubPath() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1;path=/foo/blah"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "second=2;path=/foo/blah/test"));
        responseHeader1.add(new NameValuePair("Location", "/foo/blah/test"));

        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE);
        getMockWebConnection().setResponse(URL_FIRST, "", 302, "Moved", MimeType.TEXT_HTML, responseHeader1);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Response to /a/b sets cookie to /foo/blah.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first=1; second=2")
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

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
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

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Test for document.cookie for cookies expired after the page was loaded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cookies: first=1", "cookies: "})
    public void setCookieDuring302() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function alertCookies() {\n"
                + "    log('cookies: ' + document.cookie);\n"
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

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(DEFAULT_WAIT_TIME.multipliedBy(4), getWebDriver(), getExpectedAlerts());
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

        final WebDriver driver = loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
        driver.get(URL_FIRST + "foo");

        final Map<String, String> lastHeaders = getMockWebConnection().getLastAdditionalHeaders();

        // strange check, but there is no order
        final String lastCookies = lastHeaders.get(HttpHeader.COOKIE);
        assertEquals(17, lastCookies.length());
        assertTrue("lastCookies: " + lastCookies, lastCookies.contains("first=1")
                    && lastCookies.contains("second=2")
                    && lastCookies.contains("; "));

        if (driver instanceof HtmlUnitDriver) {
            final CookieManager mgr = getWebClient().getCookieManager();
            assertEquals(2, mgr.getCookies().size());
            assertTrue(mgr.getCookie("first").isHttpOnly());
            assertFalse(mgr.getCookie("second").isHttpOnly());
        }
    }

    /**
     * Scripts must not be able to overwrite, delete or create HttpOnly cookies
     * (RFC 6265 section 5.3, steps 10 and 11).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("second=2; third=3")
    public void httpOnlyNotWritableFromJs() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "first=1; path=/; HttpOnly"));
        responseHeader.add(new NameValuePair("Set-Cookie", "second=2; path=/;"));

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    document.cookie = 'first=evil; path=/';\n"
            + "    document.cookie = 'third=3; path=/';\n"
            + "    document.cookie = 'fourth=4; path=/; HttpOnly';\n"
            + "    var c = document.cookie;\n"
            + "    c = c.split('; ').sort().join('; ');\n"
            + "    log(c);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts());
        driver.get(URL_FIRST + "foo");

        final Map<String, String> lastHeaders = getMockWebConnection().getLastAdditionalHeaders();
        final String lastCookies = lastHeaders.get(HttpHeader.COOKIE);
        assertTrue("lastCookies: " + lastCookies, lastCookies.contains("first=1"));
        assertFalse("lastCookies: " + lastCookies, lastCookies.contains("first=evil"));
        assertFalse("lastCookies: " + lastCookies, lastCookies.contains("fourth=4"));

        if (driver instanceof HtmlUnitDriver) {
            final CookieManager mgr = getWebClient().getCookieManager();
            assertEquals(3, mgr.getCookies().size());
            assertEquals("1", mgr.getCookie("first").getValue());
            assertTrue(mgr.getCookie("first").isHttpOnly());
            assertNull(mgr.getCookie("fourth"));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first=1; second=2; third=3", "26"})
    public void sameSite() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "first=1; path=/; SameSite=None; Secure"));
        responseHeader.add(new NameValuePair("Set-Cookie", "second=2; path=/; SameSite=Lax"));
        responseHeader.add(new NameValuePair("Set-Cookie", "third=3; path=/; SameSite=Strict"));

        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(URL_FIRST, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts()[0]);
        driver.get(URL_FIRST + "foo");

        final Map<String, String> lastHeaders = getMockWebConnection().getLastAdditionalHeaders();

        // strange check, but there is no order
        final String lastCookies = lastHeaders.get(HttpHeader.COOKIE);
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), lastCookies.length());
        assertTrue("lastCookies: " + lastCookies,
                    /* lastCookies.contains("first=1")
                    && */ lastCookies.contains("second=2")
                    && lastCookies.contains("third=3")
                    && lastCookies.contains("; "));

        if (driver instanceof HtmlUnitDriver) {
            final CookieManager mgr = getWebClient().getCookieManager();
            assertEquals(3, mgr.getCookies().size());
            assertEquals("none", mgr.getCookie("first").getSameSite());
            assertEquals("lax", mgr.getCookie("second").getSameSite());
            assertEquals("strict", mgr.getCookie("third").getSameSite());
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

        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body><script>\n"
            + "  document.cookie = 'first=new';\n"
            + "  location.replace('/a/b/-');\n"
            + "</script></body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(URL_FIRST, "/a/b");
        getMockWebConnection().setResponse(firstUrl, html, 200, "Ok", MimeType.TEXT_HTML, responseHeader1);

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
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

        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body><script>\n"
            + "  document.cookie = 'first=new';\n"
            + "  location.replace('/c/test.html');\n"
            + "</script></body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(URL_FIRST, "/a/b");
        getMockWebConnection().setResponse(firstUrl, html, 200, "Ok", MimeType.TEXT_HTML, responseHeader1);

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Setting a cookie again replaces the value.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cookie:button1", "cookie:button2"})
    public void resettingCookie() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function createCookie(name, value, days, path) {\n"
            + "    var date = new Date();\n"
            + "    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));\n"
            + "    document.cookie = name + '=' + value + '; expires=' + date.toGMTString() + '; path=' + path;\n"
            + "  }\n"
            + "\n"
            + "  function readCookie(name) {\n"
            + "    var nameEQ = name + '=';\n"
            + "    var ca = document.cookie.split(';');\n"
            + "    for (var i = 0; i < ca.length; i++) {\n"
            + "      var c = ca[i].trim();\n"
            + "      if (c.indexOf(nameEQ) == 0) {\n"
            + "        return c.substring(nameEQ.length, c.length);\n"
            + "      }\n"
            + "    }\n"
            + "    return null;\n"
            + "  }\n"
            + "</script></head><body>\n"
            + "  <input id='button1' type='button' "
            + "onclick=\"createCookie('button','button1',1,'/'); log('cookie:' + readCookie('button'));\" "
            + "value='Button 1'>\n"
            + "  <input id='button2' type='button' "
            + "onclick=\"createCookie('button','button2',1,'/'); log('cookie:' + readCookie('button'));\" "
            + "value='Button 2'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("button1")).click();
        driver.findElement(By.id("button2")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * A Secure cookie set by a page from localhost (secure context in some browsers). Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("plain=1; sec=1")
    public void secureCookieFromJs() throws Exception {
        cookiesFromJs("plain=1; Path=/",
                "sec=1; Secure; Path=/");
    }

    /**
     * A Secure cookie set by a page from localhost (secure context in some browsers). Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("plain=1; sec=1")
    public void secureCookieFromHeader() throws Exception {
        cookiesFromHeader("plain=1; Path=/",
                "sec=1; Secure; Path=/");
    }

    /**
     * A non-Secure cookie must not overwrite a Secure cookie (RFC 6265bis, section 5.7). Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("sec=2")
    public void secureCookieOverwrittenByNonSecureFromJs() throws Exception {
        cookiesFromJs("sec=1; Secure; Path=/",
                "sec=2; Path=/");
    }

    /**
     * A non-Secure cookie must not overwrite a Secure cookie (RFC 6265bis, section 5.7). Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("sec=2")
    public void secureCookieOverwrittenByNonSecureFromHeader() throws Exception {
        cookiesFromHeader("sec=1; Secure; Path=/",
                "sec=2; Path=/");
    }

    /**
     * The __Secure- prefix requires the Secure attribute. Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("__Secure-b=1")
    public void prefixSecureFromJs() throws Exception {
        cookiesFromJs("__Secure-a=1; Path=/",
                "__Secure-b=1; Secure; Path=/");
    }

    /**
     * The __Secure- prefix requires the Secure attribute. Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("__Secure-b=1")
    public void prefixSecureFromHeader() throws Exception {
        cookiesFromHeader("__Secure-a=1; Path=/",
                "__Secure-b=1; Secure; Path=/");
    }

    /**
     * The __Host- prefix requires the Secure attribute. Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("__Host-b=1")
    public void prefixHostFromJs() throws Exception {
        cookiesFromJs("__Host-a=1; Path=/",
                "__Host-b=1; Secure; Path=/");
    }

    /**
     * The __Host- prefix requires the Secure attribute. Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("__Host-b=1")
    public void prefixHostFromHeader() throws Exception {
        cookiesFromHeader("__Host-a=1; Path=/",
                "__Host-b=1; Secure; Path=/");
    }

    /**
     * The __Host- prefix does not allow a Domain attribute (also not the host itself). Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "__Host-b=1",
            FF = "__Host-a=1; __Host-b=1",
            FF_ESR = "__Host-a=1; __Host-b=1")
    public void prefixHostDomainFromJs() throws Exception {
        cookiesFromJs("__Host-a=1; Secure; Domain=localhost; Path=/",
                "__Host-b=1; Secure; Path=/");
    }

    /**
     * The __Host- prefix does not allow a Domain attribute (also not the host itself). Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "__Host-b=1",
            FF = "__Host-a=1; __Host-b=1",
            FF_ESR = "__Host-a=1; __Host-b=1")
    public void prefixHostDomainFromHeader() throws Exception {
        cookiesFromHeader("__Host-a=1; Secure; Domain=localhost; Path=/",
                "__Host-b=1; Secure; Path=/");
    }

    /**
     * The __Host- prefix requires the Path attribute with the value '/'. Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "__Host-c=1",
            FF_ESR = "__Host-b=1; __Host-c=1")
    public void prefixHostPathFromJs() throws Exception {
        cookiesFromJs("__Host-a=1; Secure; Path=/foo",
                "__Host-b=1; Secure",
                "__Host-c=1; Secure; Path=/");
    }

    /**
     * The __Host- prefix requires the Path attribute with the value '/'. Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "__Host-c=1",
            FF_ESR = "__Host-b=1; __Host-c=1")
    public void prefixHostPathFromHeader() throws Exception {
        cookiesFromHeader("__Host-a=1; Secure; Path=/foo",
                "__Host-b=1; Secure",
                "__Host-c=1; Secure; Path=/");
    }

    /**
     * The prefixes are checked case-insensitive. Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            FF = "__HOST-c=1",
            FF_ESR = "__HOST-c=1")
    public void prefixCaseInsensitiveFromJs() throws Exception {
        cookiesFromJs("__secure-a=1; Path=/",
                "__host-b=1; Path=/",
                "__HOST-c=1; Secure; Domain=localhost; Path=/");
    }

    /**
     * The prefixes are checked case-insensitive. Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            FF = "__HOST-c=1",
            FF_ESR = "__HOST-c=1")
    public void prefixCaseInsensitiveFromHeader() throws Exception {
        cookiesFromHeader("__secure-a=1; Path=/",
                "__host-b=1; Path=/",
                "__HOST-c=1; Secure; Domain=localhost; Path=/");
    }

    /**
     * A prefix only counts at the start of the name. Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x__Host-b=1; x__Secure-a=1")
    public void prefixPartOfNameFromJs() throws Exception {
        cookiesFromJs("x__Secure-a=1; Path=/",
                "x__Host-b=1; Domain=localhost; Path=/");
    }

    /**
     * A prefix only counts at the start of the name. Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x__Host-b=1; x__Secure-a=1")
    public void prefixPartOfNameFromHeader() throws Exception {
        cookiesFromHeader("x__Secure-a=1; Path=/",
                "x__Host-b=1; Domain=localhost; Path=/");
    }

    private void cookiesFromJs(final String... cookies) throws Exception {
        final StringBuilder js = new StringBuilder();
        for (final String cookie : cookies) {
            js.append("  document.cookie = '").append(cookie).append("';\n");
        }
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + js
            + "  log(document.cookie.split('; ').sort().join('; '));\n"
            + "</script>\n"
            + "</head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    private void cookiesFromHeader(final String... cookies) throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        for (final String cookie : cookies) {
            responseHeader.add(new NameValuePair("Set-Cookie", cookie));
        }
        getMockWebConnection().setDefaultResponse(HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * An about:blank iframe uses the cookies of the creator (the parent page).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("parent=1")
    public void aboutBlankIframeRead() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function cookies(doc) { return doc.cookie.split('; ').sort().join('; '); }\n"
            + "  function test() {\n"
            + "    document.cookie = 'parent=1; path=/';\n"
            + "    log(cookies(frames[0].document));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe></iframe>\n"
            + "<script>test();</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A cookie set by an about:blank iframe is a cookie of the creator (the parent page).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"frame=1", "frame=1"})
    public void aboutBlankIframeWrite() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function cookies(doc) { return doc.cookie.split('; ').sort().join('; '); }\n"
            + "  function test() {\n"
            + "    frames[0].document.cookie = 'frame=1; path=/';\n"
            + "    log(cookies(frames[0].document));\n"
            + "    log(cookies(document));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe></iframe>\n"
            + "<script>test();</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An about:blank iframe created by script uses the cookies of the creator.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"parent=1", "frame=1; parent=1"})
    public void aboutBlankIframeCreatedByScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function cookies(doc) { return doc.cookie.split('; ').sort().join('; '); }\n"
            + "  function test() {\n"
            + "    document.cookie = 'parent=1; path=/';\n"
            + "    var iframe = document.createElement('iframe');\n"
            + "    document.body.appendChild(iframe);\n"
            + "    log(cookies(iframe.contentDocument));\n"
            + "    iframe.contentDocument.cookie = 'frame=1; path=/';\n"
            + "    log(cookies(document));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<script>test();</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A srcdoc iframe uses the cookies of the parent page.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"parent=1", "frame=1; parent=1"})
    public void srcdocIframe() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function cookies(doc) { return doc.cookie.split('; ').sort().join('; '); }\n"
            + "  document.cookie = 'parent=1; path=/';\n"
            + "  function test(iframe) {\n"
            + "    log(cookies(iframe.contentDocument));\n"
            + "    iframe.contentDocument.cookie = 'frame=1; path=/';\n"
            + "    log(cookies(document));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe srcdoc='<p>srcdoc</p>' onload='test(this)'></iframe>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A sandboxed srcdoc iframe (opaque origin) has no access to the cookies.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"read SecurityError", "write SecurityError"})
    public void srcdocIframeSandboxed() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function cookies(doc) { return doc.cookie.split('; ').sort().join('; '); }\n"
            + "  document.cookie = 'parent=1; path=/';\n"
            + "  window.addEventListener('message', function(e) { log(e.data); });\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe sandbox='allow-scripts' srcdoc=\""
            + "<script>"
            + "try { parent.postMessage('cookie: ' + document.cookie, '*'); }"
            + "catch(e) { parent.postMessage('read ' + e.name, '*'); }"
            + "try { document.cookie = 'frame=1; path=/'; parent.postMessage('write ok', '*'); }"
            + "catch(e) { parent.postMessage('write ' + e.name, '*'); }"
            + "</script>\"></iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * A document created by script (no browsing context) is cookie-averse:
     * reading returns an empty string, writing is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"html []", "parser []", "xml []", "parent=1"})
    public void documentWithoutBrowsingContext() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function cookies(doc) { return doc.cookie.split('; ').sort().join('; '); }\n"
            + "  function check(name, doc) {\n"
            + "    try { log(name + ' [' + doc.cookie + ']'); } catch(e) { log(name + ' ' + e.name); }\n"
            + "    try { doc.cookie = name + '=1; path=/'; } catch(e) { log(name + ' write ' + e.name); }\n"
            + "  }\n"
            + "  document.cookie = 'parent=1; path=/';\n"
            + "  check('html', document.implementation.createHTMLDocument('test'));\n"
            + "  check('parser', new DOMParser().parseFromString('<html></html>', 'text/html'));\n"
            + "  check('xml', document.implementation.createDocument(null, 'root', null));\n"
            + "  log(cookies(document));\n"
            + "</script>\n"
            + "</head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A window opened without url (about:blank) uses the cookies of the creator.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"parent=1", "parent=1; popup=1"})
    public void windowOpenAboutBlank() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function cookies(doc) { return doc.cookie.split('; ').sort().join('; '); }\n"
            + "  function test() {\n"
            + "    document.cookie = 'parent=1; path=/';\n"
            + "    var w = window.open('');\n"
            + "    log(cookies(w.document));\n"
            + "    w.document.cookie = 'popup=1; path=/';\n"
            + "    log(cookies(document));\n"
            + "    w.close();\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<script>test();</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A page from an IP address sets cookies with domain attribute. Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("host=1; own=1; ownDot=1")
    public void ipDomainAttributeFromJs() throws Exception {
        cookiesFromJs(urlIp(),
                "host=1; Path=/",
                "own=1; Domain=127.0.0.1; Path=/",
                "ownDot=1; Domain=.127.0.0.1; Path=/",
                "part=1; Domain=0.0.1; Path=/",
                "other=1; Domain=127.0.0.2; Path=/");
    }

    /**
     * A page from an IP address sets cookies with domain attribute. Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("host=1; own=1; ownDot=1")
    public void ipDomainAttributeFromHeader() throws Exception {
        cookiesFromHeader(urlIp(),
                "host=1; Path=/",
                "own=1; Domain=127.0.0.1; Path=/",
                "ownDot=1; Domain=.127.0.0.1; Path=/",
                "part=1; Domain=0.0.1; Path=/",
                "other=1; Domain=127.0.0.2; Path=/");
    }

    /**
     * A Secure cookie from an IP address (127.0.0.1 is potentially trustworthy in some browsers); the Secure cookie
     * replaces the cookie set before if accepted. Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("sec=1; x=secure")
    public void ipSecureFromJs() throws Exception {
        cookiesFromJs(urlIp(),
                "x=plain; Path=/",
                "x=secure; Secure; Path=/",
                "sec=1; Secure; Path=/");
    }

    /**
     * A Secure cookie from an IP address (127.0.0.1 is potentially trustworthy in some browsers); the Secure cookie
     * replaces the cookie set before if accepted. Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("sec=1; x=secure")
    public void ipSecureFromHeader() throws Exception {
        cookiesFromHeader(urlIp(),
                "x=plain; Path=/",
                "x=secure; Secure; Path=/",
                "sec=1; Secure; Path=/");
    }

    /**
     * Cookies with prefix from an IP address. Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("__Host-b=1; __Host-c=1; __Secure-a=1")
    public void ipPrefixFromJs() throws Exception {
        cookiesFromJs(urlIp(),
                "__Secure-a=1; Secure; Path=/",
                "__Host-b=1; Secure; Path=/",
                "__Host-c=1; Secure; Domain=127.0.0.1; Path=/");
    }

    /**
     * Cookies with prefix from an IP address. Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("__Host-b=1; __Host-c=1; __Secure-a=1")
    public void ipPrefixFromHeader() throws Exception {
        cookiesFromHeader(urlIp(),
                "__Secure-a=1; Secure; Path=/",
                "__Host-b=1; Secure; Path=/",
                "__Host-c=1; Secure; Domain=127.0.0.1; Path=/");
    }

    // the page served from an ip address (same server as localhost)
    private static URL urlIp() throws MalformedURLException {
        return new URL("http://127.0.0.1:" + PORT + "/");
    }

    private void cookiesFromJs(final URL url, final String... cookies) throws Exception {
        final StringBuilder js = new StringBuilder();
        for (final String cookie : cookies) {
            js.append("  document.cookie = '").append(cookie).append("';\n");
        }
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + js
            + "  log(document.cookie.split('; ').sort().join('; '));\n"
            + "</script>\n"
            + "</head><body></body></html>";

        getMockWebConnection().setResponse(url, html);
        loadPage2(url, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    private void cookiesFromHeader(final URL url, final String... cookies) throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        for (final String cookie : cookies) {
            responseHeader.add(new NameValuePair("Set-Cookie", cookie));
        }
        getMockWebConnection().setResponse(url, HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        loadPage2(url, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }
}
