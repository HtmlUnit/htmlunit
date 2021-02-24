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

import static org.junit.Assume.assumeTrue;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Unit tests for {@link CookieManager}.
 *
 * These tests require a special setup in {@code etc/hosts}:
 * <pre>
 * 127.0.0.1       host1.htmlunit.org
 * 127.0.0.1       host2.htmlunit.org
 * 127.0.0.1       htmlunit.org
 * 127.0.0.1       htmlunit
 * </pre>
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author Jacob Childress
 */
@RunWith(BrowserRunner.class)
public class CookieManager4Test extends WebDriverTestCase {

    private static final String DOMAIN = "htmlunit.org";
    private static final String URL_HOST1 = "http://host1." + DOMAIN + ":" + PORT + '/';
    private static final String URL_HOST2 = "http://host2." + DOMAIN + ":" + PORT + '/';
    private static final String URL_HOST3 = "http://" + DOMAIN + ":" + PORT + '/';
    private static final String URL_HOST4 = "http://" + "htmlunit" + ":" + PORT + '/';

    /**
     * {@link org.junit.Assume Assumes} that the host configurations are present.
     *
     * @throws Exception if the test fails
     */
    @BeforeClass
    public static void checkSettings() throws Exception {
        try {
            InetAddress.getByName(new URL(URL_HOST1).getHost());
        }
        catch (final UnknownHostException e) {
            assumeTrue("Host configuration '" + URL_HOST1 + "' are not present", false);
        }

        try {
            InetAddress.getByName(new URL(URL_HOST2).getHost());
        }
        catch (final UnknownHostException e) {
            assumeTrue("Host configuration '" + URL_HOST2 + "' are not present", false);
        }

        try {
            InetAddress.getByName(new URL(URL_HOST3).getHost());
        }
        catch (final UnknownHostException e) {
            assumeTrue("Host configuration '" + URL_HOST3 + "' are not present", false);
        }

        try {
            InetAddress.getByName(new URL(URL_HOST4).getHost());
        }
        catch (final UnknownHostException e) {
            assumeTrue("Host configuration '" + URL_HOST4 + "' are not present", false);
        }
    }

    /**
     * Clear browser cookies.
     *
     * @throws Exception if the test fails
     */
    @Before
    public void clearCookies() throws Exception {
        getMockWebConnection().setDefaultResponse("<html><head></head><body></body></html>");
        startWebServer(getMockWebConnection(), null);
        final WebDriver driver = getWebDriver();
        driver.get(URL_HOST1);
        driver.manage().deleteAllCookies();
        driver.get(URL_HOST2);
        driver.manage().deleteAllCookies();
        driver.get(URL_HOST3);
        driver.manage().deleteAllCookies();
        driver.get(URL_HOST4);
        driver.manage().deleteAllCookies();
        stopWebServers();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c1=1; c2=2")
    public void domain() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=3; Domain=." + DOMAIN + ":" + PORT + "; Path=/"));
        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final URL firstUrl = new URL(URL_HOST1);
        loadPageWithAlerts2(firstUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c1=1; c2=2; c3=3; c4=4")
    public void storedDomain1() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=3; Domain=.host1." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=4; Domain=host1." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=5; Domain=." + DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c6=6; Domain=" + DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c7=7; Domain=.host1." + DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c8=8; Domain=host1." + DOMAIN + ":" + PORT + "; Path=/"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c9=9; Domain=.org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c10=10; Domain=org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c11=11; Domain=.htmlunit; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c12=12; Domain=htmlunit; Path=/"));

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPageWithAlerts2(new URL(URL_HOST1));

        assertEquals("c1=1; path=/; domain=.htmlunit.org", driver.manage().getCookieNamed("c1").toString());
        assertEquals("c2=2; path=/; domain=.htmlunit.org", driver.manage().getCookieNamed("c2").toString());
        assertEquals("c3=3; path=/; domain=.host1.htmlunit.org", driver.manage().getCookieNamed("c3").toString());
        assertEquals("c4=4; path=/; domain=.host1.htmlunit.org", driver.manage().getCookieNamed("c4").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c1=1; c2=2")
    public void storedDomain2() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=3; Domain=.host1." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=4; Domain=host1." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=5; Domain=." + DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c6=6; Domain=" + DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c7=7; Domain=.host1." + DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c8=8; Domain=host1." + DOMAIN + ":" + PORT + "; Path=/"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c9=9; Domain=.org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c10=10; Domain=org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c11=11; Domain=.htmlunit; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c12=12; Domain=htmlunit; Path=/"));

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPageWithAlerts2(new URL(URL_HOST2));

        assertEquals("c1=1; path=/; domain=.htmlunit.org", driver.manage().getCookieNamed("c1").toString());
        assertEquals("c2=2; path=/; domain=.htmlunit.org", driver.manage().getCookieNamed("c2").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c1=1; c2=2")
    public void storedDomain3() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=3; Domain=.host1." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=4; Domain=host1." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=5; Domain=." + DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c6=6; Domain=" + DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c7=7; Domain=.host1." + DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c8=8; Domain=host1." + DOMAIN + ":" + PORT + "; Path=/"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c9=9; Domain=.org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c10=10; Domain=org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c11=11; Domain=.htmlunit; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c12=12; Domain=htmlunit; Path=/"));

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPageWithAlerts2(new URL(URL_HOST3));

        assertEquals("c1=1; path=/; domain=.htmlunit.org", driver.manage().getCookieNamed("c1").toString());
        assertEquals("c2=2; path=/; domain=.htmlunit.org", driver.manage().getCookieNamed("c2").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "c11=11; c12=12",
            CHROME = "c12=12")
    public void storedDomain4() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=3; Domain=.host1." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=4; Domain=host1." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=5; Domain=." + DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c6=6; Domain=" + DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c7=7; Domain=.host1." + DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c8=8; Domain=host1." + DOMAIN + ":" + PORT + "; Path=/"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c9=9; Domain=.org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c10=10; Domain=org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c11=11; Domain=.htmlunit; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c12=12; Domain=htmlunit; Path=/"));

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPageWithAlerts2(new URL(URL_HOST4));

        assertEquals("c12=12; path=/; domain=htmlunit", driver.manage().getCookieNamed("c12").toString());
        if (driver.manage().getCookieNamed("c11") != null) {
            assertEquals("c11=11; path=/; domain=htmlunit", driver.manage().getCookieNamed("c11").toString());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void storedDomainFromJs1() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p>Cookie Domain Test</p>\n"
                + "<script>\n"
                + "  document.cookie = 'c1=1; Domain=." + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c2=2; Domain=" + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c3=3; Domain=.host1." + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c4=4; Domain=host1." + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c5=5; Domain=." + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c6=6; Domain=" + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c7=7; Domain=.host1." + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c8=8; Domain=host1." + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c9=9; Domain=.org; Path=/';\n"
                + "  document.cookie = 'c10=10; Domain=org; Path=/';\n"
                + "  document.cookie = 'c11=11; Domain=.htmlunit; Path=/';\n"
                + "  document.cookie = 'c12=12; Domain=htmlunit; Path=/';\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPageWithAlerts2(html, new URL(URL_HOST1));

        assertEquals(4, driver.manage().getCookies().size());
        assertEquals("c1=1; path=/; domain=.htmlunit.org", driver.manage().getCookieNamed("c1").toString());
        assertEquals("c2=2; path=/; domain=.htmlunit.org", driver.manage().getCookieNamed("c2").toString());
        assertEquals("c3=3; path=/; domain=.host1.htmlunit.org", driver.manage().getCookieNamed("c3").toString());
        assertEquals("c4=4; path=/; domain=.host1.htmlunit.org", driver.manage().getCookieNamed("c4").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void storedDomainFromJs2() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p>Cookie Domain Test</p>\n"
                + "<script>\n"
                + "  document.cookie = 'c1=1; Domain=." + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c2=2; Domain=" + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c3=3; Domain=.host1." + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c4=4; Domain=host1." + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c5=5; Domain=." + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c6=6; Domain=" + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c7=7; Domain=.host1." + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c8=8; Domain=host1." + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c9=9; Domain=.org; Path=/';\n"
                + "  document.cookie = 'c10=10; Domain=org; Path=/';\n"
                + "  document.cookie = 'c11=11; Domain=.htmlunit; Path=/';\n"
                + "  document.cookie = 'c12=12; Domain=htmlunit; Path=/';\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPageWithAlerts2(html, new URL(URL_HOST2));

        assertEquals(2, driver.manage().getCookies().size());
        assertEquals("c1=1; path=/; domain=.htmlunit.org", driver.manage().getCookieNamed("c1").toString());
        assertEquals("c2=2; path=/; domain=.htmlunit.org", driver.manage().getCookieNamed("c2").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void storedDomainFromJs3() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p>Cookie Domain Test</p>\n"
                + "<script>\n"
                + "  document.cookie = 'c1=1; Domain=." + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c2=2; Domain=" + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c3=3; Domain=.host1." + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c4=4; Domain=host1." + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c5=5; Domain=." + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c6=6; Domain=" + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c7=7; Domain=.host1." + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c8=8; Domain=host1." + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c9=9; Domain=.org; Path=/';\n"
                + "  document.cookie = 'c10=10; Domain=org; Path=/';\n"
                + "  document.cookie = 'c11=11; Domain=.htmlunit; Path=/';\n"
                + "  document.cookie = 'c12=12; Domain=htmlunit; Path=/';\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPageWithAlerts2(html, new URL(URL_HOST3));

        assertEquals(2, driver.manage().getCookies().size());
        assertEquals("c1=1; path=/; domain=.htmlunit.org", driver.manage().getCookieNamed("c1").toString());
        assertEquals("c2=2; path=/; domain=.htmlunit.org", driver.manage().getCookieNamed("c2").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2", "c12=12; path=/; domain=htmlunit", "c11=11; path=/; domain=htmlunit"},
            CHROME = {"1", "c12=12; path=/; domain=htmlunit"})
    public void storedDomainFromJs4() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p>Cookie Domain Test</p>\n"
                + "<script>\n"
                + "  document.cookie = 'c1=1; Domain=." + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c2=2; Domain=" + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c3=3; Domain=.host1." + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c4=4; Domain=host1." + DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c5=5; Domain=." + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c6=6; Domain=" + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c7=7; Domain=.host1." + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c8=8; Domain=host1." + DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c9=9; Domain=.org; Path=/';\n"
                + "  document.cookie = 'c10=10; Domain=org; Path=/';\n"
                + "  document.cookie = 'c11=11; Domain=.htmlunit; Path=/';\n"
                + "  document.cookie = 'c12=12; Domain=htmlunit; Path=/';\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html, new URL(URL_HOST4));

        final String[] expected = getExpectedAlerts();
        assertEquals(Integer.parseInt(expected[0]), driver.manage().getCookies().size());
        assertEquals(expected[1], driver.manage().getCookieNamed("c12").toString());
        if (Integer.parseInt(expected[0]) > 1) {
            assertEquals(expected[2], driver.manage().getCookieNamed("c11").toString());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c1=1; c2=2")
    public void domainDuplicate() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=" + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + DOMAIN + "; Path=/"));
        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final URL firstUrl = new URL(URL_HOST1);

        loadPageWithAlerts2(firstUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c1=1; c2=2")
    public void domainDuplicateLeadingDot() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=host1." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=.host1." + DOMAIN + "; Path=/"));
        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final URL firstUrl = new URL(URL_HOST1);

        loadPageWithAlerts2(firstUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domainDuplicateLeadingDotSend() throws Exception {
        final String html = "<html><body>\n"
                + "<a href='next.html'>next page</a>\n"
                + "</body></html>";

        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=host1." + DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=.host1." + DOMAIN + "; Path=/"));

        getMockWebConnection().setDefaultResponse(html, 200, "Ok", MimeType.TEXT_HTML, responseHeader);

        final WebDriver webDriver = getWebDriver();
        webDriver.manage().deleteAllCookies();

        loadPageWithAlerts2(new URL(URL_HOST1));
        WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertNull(lastRequest.getAdditionalHeaders().get(HttpHeader.COOKIE));

        webDriver.findElement(By.linkText("next page")).click();
        lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals("c1=1; c2=2", lastRequest.getAdditionalHeaders().get(HttpHeader.COOKIE));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domainDuplicateLeadingDotRedirect() throws Exception {
        final String html = "<html><body>\n"
                + "<a href='next.html'>next page</a>\n"
                + "</body></html>";

        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=host1." + DOMAIN
                                                + "; path=/; expires=Fri, 04-Feb-2022 09:00:32 GMT"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=.host1." + DOMAIN
                                                + "; path=/; expires=Fri, 04-Feb-2022 09:00:32 GMT"));

        getMockWebConnection().setDefaultResponse(html, 200, "Ok", MimeType.TEXT_HTML, responseHeader);

        responseHeader.add(new NameValuePair("Location", URL_HOST1 + "next.html"));
        getMockWebConnection().setResponse(new URL(URL_HOST1), "redirect", 301, "Ok", MimeType.TEXT_HTML,
                responseHeader);

        final WebDriver webDriver = getWebDriver();
        webDriver.manage().deleteAllCookies();

        final int startCount = getMockWebConnection().getRequestCount();
        loadPageWithAlerts2(new URL(URL_HOST1));
        assertEquals(2, getMockWebConnection().getRequestCount() - startCount);
        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals("c1=1; c2=2", lastRequest.getAdditionalHeaders().get(HttpHeader.COOKIE));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c4=4")
    public void domain2() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "c1=1; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c2=2; Domain=host1." + DOMAIN + "; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c3=3; Domain=host2." + DOMAIN + "; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c4=4; Domain=" + DOMAIN + "; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c5=5; Domain=.org; Path=/"));

        final String html = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<p>Cookie Domain Test</p>\n"
            + "<script>\n"
            + "  location.replace('" + URL_HOST3 + "');\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(URL_HOST1);
        getMockWebConnection().setResponse(firstUrl, html, 200, "Ok", MimeType.TEXT_HTML, responseHeader1);

        loadPageWithAlerts2(firstUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c1=1; c4=4")
    public void domain3() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "c1=1; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c2=2; Domain=host1." + DOMAIN + "; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c3=3; Domain=host2." + DOMAIN + "; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c4=4; Domain=" + DOMAIN + "; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c5=5; Domain=.org; Path=/"));

        final String html = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<p>Cookie Domain Test</p>\n"
            + "<script>\n"
            + "  location.replace('" + URL_HOST3 + "test.html');\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(URL_HOST3);
        getMockWebConnection().setResponse(firstUrl, html, 200, "Ok", MimeType.TEXT_HTML, responseHeader1);

        loadPageWithAlerts2(firstUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cross-domain=1")
    public void differentHostsSameDomain() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "cross-domain=1; Domain=." + DOMAIN + "; Path=/"));

        final String html = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<p>Cookie Domain Test</p>\n"
            + "<script>\n"
            + "  location.replace('" + URL_HOST2 + "');\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(URL_HOST1);
        getMockWebConnection().setResponse(firstUrl, html, 200, "Ok", MimeType.TEXT_HTML, responseHeader1);

        loadPageWithAlerts2(firstUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cross-domain=1")
    public void differentHostsSameDomainCookieFromJS() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p>Cookie Domain Test</p>\n"
            + "<script>\n"
            + "  document.cookie='cross-domain=1; Domain=." + DOMAIN + "; Path=/';\n"
            + "  location.replace('" + URL_HOST2 + "');\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(URL_HOST1);
        getMockWebConnection().setResponse(firstUrl, html);

        loadPageWithAlerts2(firstUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cross-domain=1")
    public void differentHostsSameDomainCookieFromMeta() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Set-Cookie', content='cross-domain=1; Domain=." + DOMAIN + "; Path=/'>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p>Cookie Domain Test</p>\n"
            + "<script>\n"
            + "  location.replace('" + URL_HOST2 + "');\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(URL_HOST1);
        getMockWebConnection().setResponse(firstUrl, html);

        loadPageWithAlerts2(firstUrl);
    }

    private void testCookies(final URL url, final String cookie1, final String cookie2) throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", cookie1));
        responseHeader.add(new NameValuePair("Set-Cookie", cookie2));
        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader);

        loadPageWithAlerts2(url);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("key1=\"Hi there\"; key2=Howdy")
    public void unqualifiedHost() throws Exception {
        testCookies(new URL(URL_HOST4), "key1=\"Hi there\"", "key2=Howdy");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("key1=\"Hi there\"; key2=Howdy")
    public void fullyQualifiedHost() throws Exception {
        testCookies(new URL(URL_HOST1), "key1=\"Hi there\"", "key2=Howdy");
    }

}
