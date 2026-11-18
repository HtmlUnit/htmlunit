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

import static org.htmlunit.CookieManagerTest.HTML_ALERT_COOKIE;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Unit tests for {@link CookieManager}.
 * <p>
 * These tests require a special setup of the name resolution, see {@link WebTestCase#HOSTS_DOMAIN}.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author Jacob Childress
 */
public class CookieManager4Test extends WebDriverTestCase {

    /**
     * Assumes, the host configurations are present.
     */
    @BeforeAll
    public static void checkSettings() {
        assertHostsConfigured();
    }

    /**
     * Clear browser cookies.
     *
     * @throws Exception if the test fails
     */
    @BeforeEach
    public void clearCookies() throws Exception {
        shutDownAll();

        getMockWebConnection().setDefaultResponse(DOCTYPE_HTML + "<html><head></head><body></body></html>");
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
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=." + HOSTS_DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + HOSTS_DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=3; Domain=." + HOSTS_DOMAIN + ":" + PORT + "; Path=/"));
        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final URL firstUrl = new URL(URL_HOST1);
        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"c1=1; c2=2; c3=3; c4=4",
                       "c1=1; path=/; domain=.htmlunit-dev.org; sameSite=Lax",
                       "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=Lax",
                       "c3=3; path=/; domain=.host1.htmlunit-dev.org; sameSite=Lax",
                       "c4=4; path=/; domain=.host1.htmlunit-dev.org; sameSite=Lax"},
            FF = {"c1=1; c2=2; c3=3; c4=4",
                  "c1=1; path=/; domain=.htmlunit-dev.org; sameSite=None",
                  "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=None",
                  "c3=3; path=/; domain=.host1.htmlunit-dev.org; sameSite=None",
                  "c4=4; path=/; domain=.host1.htmlunit-dev.org; sameSite=None"},
            FF_ESR = {"c1=1; c2=2; c3=3; c4=4",
                      "c1=1; path=/; domain=.htmlunit-dev.org; sameSite=None",
                      "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=None",
                      "c3=3; path=/; domain=.host1.htmlunit-dev.org; sameSite=None",
                      "c4=4; path=/; domain=.host1.htmlunit-dev.org; sameSite=None"})
    @HtmlUnitNYI(CHROME = {"c1=1; c2=2; c3=3; c4=4",
                           "c1=1; path=/; domain=.htmlunit-dev.org",
                           "c2=2; path=/; domain=.htmlunit-dev.org",
                           "c3=3; path=/; domain=.host1.htmlunit-dev.org",
                           "c4=4; path=/; domain=.host1.htmlunit-dev.org"},
            EDGE = {"c1=1; c2=2; c3=3; c4=4",
                    "c1=1; path=/; domain=.htmlunit-dev.org",
                    "c2=2; path=/; domain=.htmlunit-dev.org",
                    "c3=3; path=/; domain=.host1.htmlunit-dev.org",
                    "c4=4; path=/; domain=.host1.htmlunit-dev.org"},
            FF = {"c1=1; c2=2; c3=3; c4=4",
                  "c1=1; path=/; domain=.htmlunit-dev.org",
                  "c2=2; path=/; domain=.htmlunit-dev.org",
                  "c3=3; path=/; domain=.host1.htmlunit-dev.org",
                  "c4=4; path=/; domain=.host1.htmlunit-dev.org"},
            FF_ESR = {"c1=1; c2=2; c3=3; c4=4",
                      "c1=1; path=/; domain=.htmlunit-dev.org",
                      "c2=2; path=/; domain=.htmlunit-dev.org",
                      "c3=3; path=/; domain=.host1.htmlunit-dev.org",
                      "c4=4; path=/; domain=.host1.htmlunit-dev.org"})
    public void storedDomain1() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=." + HOSTS_DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + HOSTS_DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=3; Domain=." + HOST1 + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=4; Domain=" + HOST1 + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=5; Domain=." + HOSTS_DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c6=6; Domain=" + HOSTS_DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c7=7; Domain=." + HOST1 + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c8=8; Domain=" + HOST1 + ":" + PORT + "; Path=/"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c9=9; Domain=.org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c10=10; Domain=org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c11=11; Domain=.htmlunit-dev; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c12=12; Domain=htmlunit-dev; Path=/"));

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(new URL(URL_HOST1), StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts()[0]);

        assertEquals(getExpectedAlerts()[1], driver.manage().getCookieNamed("c1").toString());
        assertEquals(getExpectedAlerts()[2], driver.manage().getCookieNamed("c2").toString());
        assertEquals(getExpectedAlerts()[3], driver.manage().getCookieNamed("c3").toString());
        assertEquals(getExpectedAlerts()[4], driver.manage().getCookieNamed("c4").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"c1=1; c2=2",
                       "c1=1; path=/; domain=.htmlunit-dev.org; sameSite=Lax",
                       "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=Lax"},
            FF = {"c1=1; c2=2",
                  "c1=1; path=/; domain=.htmlunit-dev.org; sameSite=None",
                  "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=None"},
            FF_ESR = {"c1=1; c2=2",
                      "c1=1; path=/; domain=.htmlunit-dev.org; sameSite=None",
                      "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=None"})
    @HtmlUnitNYI(
            CHROME = {"c1=1; c2=2", "c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"},
            EDGE = {"c1=1; c2=2", "c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"},
            FF = {"c1=1; c2=2", "c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"},
            FF_ESR = {"c1=1; c2=2", "c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"})
    public void storedDomain2() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=." + HOSTS_DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + HOSTS_DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=3; Domain=." + HOST1 + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=4; Domain=" + HOST1 + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=5; Domain=." + HOSTS_DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c6=6; Domain=" + HOSTS_DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c7=7; Domain=." + HOST1 + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c8=8; Domain=" + HOST1 + ":" + PORT + "; Path=/"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c9=9; Domain=.org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c10=10; Domain=org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c11=11; Domain=.htmlunit-dev; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c12=12; Domain=htmlunit-dev; Path=/"));

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(new URL(URL_HOST2), StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts()[0]);

        assertEquals(getExpectedAlerts()[1], driver.manage().getCookieNamed("c1").toString());
        assertEquals(getExpectedAlerts()[2], driver.manage().getCookieNamed("c2").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"c1=1; c2=2",
                       "c1=1; path=/; domain=.htmlunit-dev.org; sameSite=Lax",
                       "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=Lax"},
            FF = {"c1=1; c2=2",
                  "c1=1; path=/; domain=.htmlunit-dev.org; sameSite=None",
                  "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=None"},
            FF_ESR = {"c1=1; c2=2",
                      "c1=1; path=/; domain=.htmlunit-dev.org; sameSite=None",
                      "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=None"})
    @HtmlUnitNYI(
            CHROME = {"c1=1; c2=2", "c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"},
            EDGE = {"c1=1; c2=2", "c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"},
            FF = {"c1=1; c2=2", "c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"},
            FF_ESR = {"c1=1; c2=2", "c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"})
    public void storedDomain3() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=." + HOSTS_DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + HOSTS_DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=3; Domain=." + HOST1 + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=4; Domain=" + HOST1 + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=5; Domain=." + HOSTS_DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c6=6; Domain=" + HOSTS_DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c7=7; Domain=." + HOST1 + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c8=8; Domain=" + HOST1 + ":" + PORT + "; Path=/"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c9=9; Domain=.org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c10=10; Domain=org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c11=11; Domain=.htmlunit-dev; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c12=12; Domain=htmlunit-dev; Path=/"));

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(new URL(URL_HOST3), StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts()[0]);

        assertEquals(getExpectedAlerts()[1], driver.manage().getCookieNamed("c1").toString());
        assertEquals(getExpectedAlerts()[2], driver.manage().getCookieNamed("c2").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"c11=11; c12=12",
                       "c12=12; path=/; domain=htmlunit-dev; sameSite=Lax",
                       "c11=11; path=/; domain=htmlunit-dev; sameSite=Lax"},
            FF = {"c11=11; c12=12",
                  "c12=12; path=/; domain=htmlunit-dev; sameSite=None",
                  "c11=11; path=/; domain=htmlunit-dev; sameSite=None"},
            FF_ESR = {"c11=11; c12=12",
                      "c12=12; path=/; domain=htmlunit-dev; sameSite=None",
                      "c11=11; path=/; domain=htmlunit-dev; sameSite=None"})
    @HtmlUnitNYI(CHROME = {"c11=11; c12=12",
                           "c12=12; path=/; domain=htmlunit-dev",
                           "c11=11; path=/; domain=htmlunit-dev"},
            EDGE = {"c11=11; c12=12",
                    "c12=12; path=/; domain=htmlunit-dev",
                    "c11=11; path=/; domain=htmlunit-dev"},
            FF = {"c11=11; c12=12",
                  "c12=12; path=/; domain=htmlunit-dev",
                  "c11=11; path=/; domain=htmlunit-dev"},
            FF_ESR = {"c11=11; c12=12",
                      "c12=12; path=/; domain=htmlunit-dev",
                      "c11=11; path=/; domain=htmlunit-dev"})
    public void storedDomain4() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=." + HOSTS_DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + HOSTS_DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=3; Domain=." + HOST1 + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=4; Domain=" + HOST1 + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=5; Domain=." + HOSTS_DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c6=6; Domain=" + HOSTS_DOMAIN + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c7=7; Domain=." + HOST1 + ":" + PORT + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c8=8; Domain=" + HOST1 + ":" + PORT + "; Path=/"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c9=9; Domain=.org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c10=10; Domain=org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c11=11; Domain=.htmlunit-dev; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c12=12; Domain=htmlunit-dev; Path=/"));

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(new URL(URL_HOST4), StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts()[0]);

        assertEquals(getExpectedAlerts()[1], driver.manage().getCookieNamed("c12").toString());
        assertEquals(getExpectedAlerts()[2], driver.manage().getCookieNamed("c11").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"c1=1; path=/; domain=.htmlunit-dev.org; sameSite=Lax",
                       "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=Lax",
                       "c3=3; path=/; domain=.host1.htmlunit-dev.org; sameSite=Lax",
                       "c4=4; path=/; domain=.host1.htmlunit-dev.org; sameSite=Lax"},
            FF = {"c1=1; path=/; domain=.htmlunit-dev.org; sameSite=None",
                  "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=None",
                  "c3=3; path=/; domain=.host1.htmlunit-dev.org; sameSite=None",
                  "c4=4; path=/; domain=.host1.htmlunit-dev.org; sameSite=None"},
            FF_ESR = {"c1=1; path=/; domain=.htmlunit-dev.org; sameSite=None",
                      "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=None",
                      "c3=3; path=/; domain=.host1.htmlunit-dev.org; sameSite=None",
                      "c4=4; path=/; domain=.host1.htmlunit-dev.org; sameSite=None"})
    @HtmlUnitNYI(
            CHROME = {"c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org",
                      "c3=3; path=/; domain=.host1.htmlunit-dev.org", "c4=4; path=/; domain=.host1.htmlunit-dev.org"},
            EDGE = {"c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org",
                    "c3=3; path=/; domain=.host1.htmlunit-dev.org", "c4=4; path=/; domain=.host1.htmlunit-dev.org"},
            FF = {"c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org",
                  "c3=3; path=/; domain=.host1.htmlunit-dev.org", "c4=4; path=/; domain=.host1.htmlunit-dev.org"},
            FF_ESR = {"c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org",
                      "c3=3; path=/; domain=.host1.htmlunit-dev.org", "c4=4; path=/; domain=.host1.htmlunit-dev.org"})
    public void storedDomainFromJs1() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p>Cookie Domain Test</p>\n"
                + "<script>\n"
                + "  document.cookie = 'c1=1; Domain=." + HOSTS_DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c2=2; Domain=" + HOSTS_DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c3=3; Domain=." + HOST1 + "; Path=/';\n"
                + "  document.cookie = 'c4=4; Domain=" + HOST1 + "; Path=/';\n"
                + "  document.cookie = 'c5=5; Domain=." + HOSTS_DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c6=6; Domain=" + HOSTS_DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c7=7; Domain=." + HOST1 + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c8=8; Domain=" + HOST1 + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c9=9; Domain=.org; Path=/';\n"
                + "  document.cookie = 'c10=10; Domain=org; Path=/';\n"
                + "  document.cookie = 'c11=11; Domain=.htmlunit; Path=/';\n"
                + "  document.cookie = 'c12=12; Domain=htmlunit; Path=/';\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html, new URL(URL_HOST1));
        verifyTitle2(driver);

        assertEquals(4, driver.manage().getCookies().size());
        assertEquals(getExpectedAlerts()[0], driver.manage().getCookieNamed("c1").toString());
        assertEquals(getExpectedAlerts()[1], driver.manage().getCookieNamed("c2").toString());
        assertEquals(getExpectedAlerts()[2], driver.manage().getCookieNamed("c3").toString());
        assertEquals(getExpectedAlerts()[3], driver.manage().getCookieNamed("c4").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"c1=1; path=/; domain=.htmlunit-dev.org; sameSite=Lax",
                       "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=Lax"},
            FF = {"c1=1; path=/; domain=.htmlunit-dev.org; sameSite=None",
                  "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=None"},
            FF_ESR = {"c1=1; path=/; domain=.htmlunit-dev.org; sameSite=None",
                      "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=None"})
    @HtmlUnitNYI(CHROME = {"c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"},
            EDGE = {"c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"},
            FF = {"c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"},
            FF_ESR = {"c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"})
    public void storedDomainFromJs2() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p>Cookie Domain Test</p>\n"
                + "<script>\n"
                + "  document.cookie = 'c1=1; Domain=." + HOSTS_DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c2=2; Domain=" + HOSTS_DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c3=3; Domain=." + HOST1 + "; Path=/';\n"
                + "  document.cookie = 'c4=4; Domain=" + HOST1 + "; Path=/';\n"
                + "  document.cookie = 'c5=5; Domain=." + HOSTS_DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c6=6; Domain=" + HOSTS_DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c7=7; Domain=." + HOST1 + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c8=8; Domain=" + HOST1 + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c9=9; Domain=.org; Path=/';\n"
                + "  document.cookie = 'c10=10; Domain=org; Path=/';\n"
                + "  document.cookie = 'c11=11; Domain=.htmlunit; Path=/';\n"
                + "  document.cookie = 'c12=12; Domain=htmlunit; Path=/';\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html, new URL(URL_HOST2));
        verifyTitle2(driver);

        assertEquals(2, driver.manage().getCookies().size());
        assertEquals(getExpectedAlerts()[0], driver.manage().getCookieNamed("c1").toString());
        assertEquals(getExpectedAlerts()[1], driver.manage().getCookieNamed("c2").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"c1=1; path=/; domain=.htmlunit-dev.org; sameSite=Lax",
                       "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=Lax"},
            FF = {"c1=1; path=/; domain=.htmlunit-dev.org; sameSite=None",
                  "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=None"},
            FF_ESR = {"c1=1; path=/; domain=.htmlunit-dev.org; sameSite=None",
                      "c2=2; path=/; domain=.htmlunit-dev.org; sameSite=None"})
    @HtmlUnitNYI(CHROME = {"c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"},
            EDGE = {"c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"},
            FF = {"c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"},
            FF_ESR = {"c1=1; path=/; domain=.htmlunit-dev.org", "c2=2; path=/; domain=.htmlunit-dev.org"})
    public void storedDomainFromJs3() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p>Cookie Domain Test</p>\n"
                + "<script>\n"
                + "  document.cookie = 'c1=1; Domain=." + HOSTS_DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c2=2; Domain=" + HOSTS_DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c3=3; Domain=." + HOST1 + "; Path=/';\n"
                + "  document.cookie = 'c4=4; Domain=" + HOST1 + "; Path=/';\n"
                + "  document.cookie = 'c5=5; Domain=." + HOSTS_DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c6=6; Domain=" + HOSTS_DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c7=7; Domain=." + HOST1 + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c8=8; Domain=" + HOST1 + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c9=9; Domain=.org; Path=/';\n"
                + "  document.cookie = 'c10=10; Domain=org; Path=/';\n"
                + "  document.cookie = 'c11=11; Domain=.htmlunit; Path=/';\n"
                + "  document.cookie = 'c12=12; Domain=htmlunit; Path=/';\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html, new URL(URL_HOST3));
        verifyTitle2(driver);

        assertEquals(2, driver.manage().getCookies().size());
        assertEquals(getExpectedAlerts()[0], driver.manage().getCookieNamed("c1").toString());
        assertEquals(getExpectedAlerts()[1], driver.manage().getCookieNamed("c2").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2",
                       "c12=12; path=/; domain=htmlunit-dev; sameSite=Lax",
                       "c11=11; path=/; domain=htmlunit-dev; sameSite=Lax"},
            FF = {"2",
                  "c12=12; path=/; domain=htmlunit-dev; sameSite=None",
                  "c11=11; path=/; domain=htmlunit-dev; sameSite=None"},
            FF_ESR = {"2",
                      "c12=12; path=/; domain=htmlunit-dev; sameSite=None",
                      "c11=11; path=/; domain=htmlunit-dev; sameSite=None"})
    @HtmlUnitNYI(CHROME = {"2",
                           "c12=12; path=/; domain=htmlunit-dev",
                           "c11=11; path=/; domain=htmlunit-dev"},
            EDGE = {"2",
                    "c12=12; path=/; domain=htmlunit-dev",
                    "c11=11; path=/; domain=htmlunit-dev"},
            FF = {"2",
                  "c12=12; path=/; domain=htmlunit-dev",
                  "c11=11; path=/; domain=htmlunit-dev"},
            FF_ESR = {"2",
                      "c12=12; path=/; domain=htmlunit-dev",
                      "c11=11; path=/; domain=htmlunit-dev"})
    public void storedDomainFromJs4() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p>Cookie Domain Test</p>\n"
                + "<script>\n"
                + "  document.cookie = 'c1=1; Domain=." + HOSTS_DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c2=2; Domain=" + HOSTS_DOMAIN + "; Path=/';\n"
                + "  document.cookie = 'c3=3; Domain=." + HOST1 + "; Path=/';\n"
                + "  document.cookie = 'c4=4; Domain=" + HOST1 + "; Path=/';\n"
                + "  document.cookie = 'c5=5; Domain=." + HOSTS_DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c6=6; Domain=" + HOSTS_DOMAIN + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c7=7; Domain=." + HOST1 + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c8=8; Domain=" + HOST1 + ":" + PORT + "; Path=/';\n"
                + "  document.cookie = 'c9=9; Domain=.org; Path=/';\n"
                + "  document.cookie = 'c10=10; Domain=org; Path=/';\n"
                + "  document.cookie = 'c11=11; Domain=.htmlunit-dev; Path=/';\n"
                + "  document.cookie = 'c12=12; Domain=htmlunit-dev; Path=/';\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html, new URL(URL_HOST4));
        verifyTitle2(driver);

        final String[] expected = getExpectedAlerts();
        assertEquals(Integer.parseInt(expected[0]), driver.manage().getCookies().size());
        assertEquals(expected[1], driver.manage().getCookieNamed("c12").toString());
        assertEquals(expected[2], driver.manage().getCookieNamed("c11").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c1=1; c2=2")
    public void domainDuplicate() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=" + HOSTS_DOMAIN + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + HOSTS_DOMAIN + "; Path=/"));
        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final URL firstUrl = new URL(URL_HOST1);

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c1=1; c2=2")
    public void domainDuplicateLeadingDot() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=" + HOST1 + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=." + HOST1 + "; Path=/"));
        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                MimeType.TEXT_HTML, responseHeader);

        final URL firstUrl = new URL(URL_HOST1);

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domainDuplicateLeadingDotSend() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<a href='next.html'>next page</a>\n"
                + "</body></html>";

        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=" + HOST1 + "; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=." + HOST1 + "; Path=/"));

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
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<a href='next.html'>next page</a>\n"
                + "</body></html>";

        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=host1." + HOSTS_DOMAIN
                                                + "; path=/; expires=Fri, 04-Feb-2099 09:00:32 GMT"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=.host1." + HOSTS_DOMAIN
                                                + "; path=/; expires=Fri, 04-Feb-2099 09:00:32 GMT"));

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
        responseHeader1.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + HOST1 + "; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c3=3; Domain=" + HOST2 + "; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c4=4; Domain=" + HOSTS_DOMAIN + "; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c5=5; Domain=.org; Path=/"));

        final String html = DOCTYPE_HTML
            + "<html>\n"
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

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c1=1; c4=4")
    public void domain3() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "c1=1; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c2=2; Domain=" + HOST1 + "; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c3=3; Domain=" + HOST2 + "; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c4=4; Domain=" + HOSTS_DOMAIN + "; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c5=5; Domain=.org; Path=/"));

        final String html = DOCTYPE_HTML
            + "<html>\n"
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

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cross-domain=1")
    public void differentHostsSameDomain() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "cross-domain=1; Domain=." + HOSTS_DOMAIN + "; Path=/"));

        final String html = DOCTYPE_HTML
            + "<html>\n"
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

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cross-domain=1")
    public void differentHostsSameDomainCookieFromJS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p>Cookie Domain Test</p>\n"
            + "<script>\n"
            + "  document.cookie='cross-domain=1; Domain=." + HOSTS_DOMAIN + "; Path=/';\n"
            + "  location.replace('" + URL_HOST2 + "');\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE);
        final URL firstUrl = new URL(URL_HOST1);
        getMockWebConnection().setResponse(firstUrl, html);

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void differentHostsSameDomainCookieFromMeta() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Set-Cookie', content='cross-domain=1; Domain=." + HOSTS_DOMAIN + "; Path=/'>\n"
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

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Method document.cookie must not set cookies for another domain or for
     * a public suffix (RFC 6265 section 5.3, steps 5 and 6).
     * The page on host1 sets the cookies, logs document.cookie and
     * navigates to host2; host2 logs the value from host1 and its own document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"host=1; own=1", "own=1"})
    public void documentCookieForeignDomain() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  document.cookie = 'host=1; Path=/';\n"
            + "  document.cookie = 'own=1; Domain=" + HOSTS_DOMAIN + "; Path=/';\n"
            + "  document.cookie = 'sibling=1; Domain=" + HOST2 + "; Path=/';\n"
            + "  document.cookie = 'foreign=1; Domain=victim.example; Path=/';\n"
            + "  document.cookie = 'unqualified=1; Domain=" + HOST_WITHOUT_DOMAIN + "; Path=/';\n"
            + "  document.cookie = 'tld=1; Domain=org; Path=/';\n"
            + "  document.cookie = 'tldDot=1; Domain=.org; Path=/';\n"
            + "  var c = document.cookie.split('; ').sort().join('; ');\n"
            + "  location.replace('" + URL_HOST2 + "?' + encodeURIComponent(c));\n"
            + "</script>\n"
            + "</body></html>";

        verifyForeignDomain(html);
    }

    /**
     * Meta http-equiv set-cookie is ignored by the browsers; therefore there are no
     * cookies for another domain or for a public suffix also.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void metaSetCookieForeignDomain() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "  <meta http-equiv='Set-Cookie' content='own=1; Domain=" + HOSTS_DOMAIN + "; Path=/'>\n"
            + "  <meta http-equiv='Set-Cookie' content='sibling=1; Domain=" + HOST2 + "; Path=/'>\n"
            + "  <meta http-equiv='Set-Cookie' content='foreign=1; Domain=victim.example; Path=/'>\n"
            + "  <meta http-equiv='Set-Cookie' content='tld=1; Domain=org; Path=/'>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  var c = document.cookie.split('; ').sort().join('; ');\n"
            + "  location.replace('" + URL_HOST2 + "?' + encodeURIComponent(c));\n"
            + "</script>\n"
            + "</body></html>";

        verifyForeignDomain(html);
    }

    /**
     * Loads the page from host1 (html or the response already defined, if html is null);
     * the page has to navigate to host2 passing its cookies as query.
     */
    private void verifyForeignDomain(final String html) throws Exception {
        // host2 logs the cookies of host1 (passed as query) and its own cookies
        final String html2 = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(decodeURIComponent(location.search.substring(1)));\n"
            + "  log(document.cookie.split('; ').sort().join('; '));\n"
            + "</script>\n"
            + "</head><body></body></html>";

        getMockWebConnection().setDefaultResponse(html2);
        final URL firstUrl = new URL(URL_HOST1);
        if (html != null) {
            getMockWebConnection().setResponse(firstUrl, html);
        }

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * An insecure origin must not set a Secure cookie (RFC 6265bis, section 5.7); the Secure cookie would replace
     * (and hide) the cookie set before. Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"x=plain", ""})
    public void secureFromInsecureOriginFromJs() throws Exception {
        cookiesHost1FromJs("x=plain; Path=/",
                "x=secure; Secure; Path=/");
    }

    /**
     * An insecure origin must not set a Secure cookie (RFC 6265bis, section 5.7); the Secure cookie would replace
     * (and hide) the cookie set before. Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"x=plain", ""})
    public void secureFromInsecureOriginFromHeader() throws Exception {
        cookiesHost1FromHeader("x=plain; Path=/",
                "x=secure; Secure; Path=/");
    }

    /**
     * Cookies with prefix from an insecure origin. Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"plain=1", ""})
    public void prefixFromInsecureOriginFromJs() throws Exception {
        cookiesHost1FromJs("__Secure-a=1; Path=/",
                "__Host-b=1; Path=/",
                "plain=1; Path=/");
    }

    /**
     * Cookies with prefix from an insecure origin. Set by Set-Cookie headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"plain=1", ""})
    public void prefixFromInsecureOriginFromHeader() throws Exception {
        cookiesHost1FromHeader("__Secure-a=1; Path=/",
                "__Host-b=1; Path=/",
                "plain=1; Path=/");
    }

    /**
     * A __Host- cookie with Domain attribute must not be visible on another host of the domain. Set by document.cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"own=1", "own=1"})
    public void prefixHostDomainFromInsecureOriginFromJs() throws Exception {
        cookiesHost1FromJs("__Host-a=1; Domain=" + HOSTS_DOMAIN + "; Path=/",
                "own=1; Domain=" + HOSTS_DOMAIN + "; Path=/");
    }

    /**
     * A __Host- cookie with Domain attribute must not be visible on another host of the domain. Set by Set-Cookie
     * headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"own=1", "own=1"})
    public void prefixHostDomainFromInsecureOriginFromHeader() throws Exception {
        cookiesHost1FromHeader("__Host-a=1; Domain=" + HOSTS_DOMAIN + "; Path=/",
                "own=1; Domain=" + HOSTS_DOMAIN + "; Path=/");
    }

    private void cookiesHost1FromJs(final String... cookies) throws Exception {
        final StringBuilder js = new StringBuilder();
        for (final String cookie : cookies) {
            js.append("  document.cookie = '").append(cookie).append("';\n");
        }
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + js
            + "  var c = document.cookie.split('; ').sort().join('; ');\n"
            + "  location.replace('" + URL_HOST2 + "?' + encodeURIComponent(c));\n"
            + "</script>\n"
            + "</body></html>";

        verifyForeignDomain(html);
    }

    private void cookiesHost1FromHeader(final String... cookies) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  var c = document.cookie.split('; ').sort().join('; ');\n"
            + "  location.replace('" + URL_HOST2 + "?' + encodeURIComponent(c));\n"
            + "</script>\n"
            + "</body></html>";

        final List<NameValuePair> responseHeader = new ArrayList<>();
        for (final String cookie : cookies) {
            responseHeader.add(new NameValuePair("Set-Cookie", cookie));
        }

        final URL firstUrl = new URL(URL_HOST1);
        getMockWebConnection().setResponse(firstUrl, html, 200, "OK", MimeType.TEXT_HTML, responseHeader);
        verifyForeignDomain(null);
    }

    /**
     * Cookies of a host with a single label name (htmlunit-dev); the page on this host sets the cookies
     * (document.cookie), logs its cookies and navigates to htmlunit-dev.org (another host); this host logs the
     * value from the first one and its own cookies.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"host=1; own=1; ownDot=1", ""})
    public void unqualifiedHostFromJs() throws Exception {
        cookiesUnqualifiedHostJs("host=1; Path=/",
                "own=1; Domain=" + HOST_WITHOUT_DOMAIN + "; Path=/",
                "ownDot=1; Domain=." + HOST_WITHOUT_DOMAIN + "; Path=/",
                "foreign=1; Domain=localhost; Path=/",
                "tld=1; Domain=org; Path=/");
    }

    /**
     * Cookies of a host with a single label name (htmlunit-dev); the page on this host sets the cookies
     * (Set-Cookie headers), logs its cookies and navigates to htmlunit-dev.org (another host); this host logs the
     * value from the first one and its own cookies.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"host=1; own=1; ownDot=1", ""})
    public void unqualifiedHostFromHeader() throws Exception {
        cookiesUnqualifiedHostHeader("host=1; Path=/",
                "own=1; Domain=" + HOST_WITHOUT_DOMAIN + "; Path=/",
                "ownDot=1; Domain=." + HOST_WITHOUT_DOMAIN + "; Path=/",
                "foreign=1; Domain=localhost; Path=/",
                "tld=1; Domain=org; Path=/");
    }

    private void cookiesUnqualifiedHostJs(final String... cookies) throws Exception {
        final StringBuilder js = new StringBuilder();
        for (final String cookie : cookies) {
            js.append("  document.cookie = '").append(cookie).append("';\n");
        }
        verifyUnqualifiedHost(js.toString(), new ArrayList<>());
    }

    private void cookiesUnqualifiedHostHeader(final String... cookies) throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        for (final String cookie : cookies) {
            responseHeader.add(new NameValuePair("Set-Cookie", cookie));
        }
        verifyUnqualifiedHost("", responseHeader);
    }

    private void verifyUnqualifiedHost(final String js, final List<NameValuePair> responseHeader) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + js
            + "  var c = document.cookie.split('; ').sort().join('; ');\n"
            + "  location.replace('" + URL_HOST3 + "?' + encodeURIComponent(c));\n"
            + "</script>\n"
            + "</body></html>";

        // the second host logs the cookies of the first one (passed as query) and its own cookies
        final String html2 = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(decodeURIComponent(location.search.substring(1)));\n"
            + "  log(document.cookie.split('; ').sort().join('; '));\n"
            + "</script>\n"
            + "</head><body></body></html>";

        getMockWebConnection().setDefaultResponse(html2);
        final URL firstUrl = new URL(URL_HOST4);
        getMockWebConnection().setResponse(firstUrl, html, 200, "OK", MimeType.TEXT_HTML, responseHeader);

        loadPage2(firstUrl, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Cookies without SameSite attribute (default), with Lax, Strict, None (without Secure; insecure origin) and an
     * invalid value are set by host1; the page from htmlunit-dev (another site) includes an iframe from host1.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void sameSiteDefaultCrossSiteIFrame() throws Exception {
        crossSite("<iframe src='" + URL_HOST1 + "target'></iframe>\n", null);
    }

    /**
     * Cookies without SameSite attribute (default), with Lax, Strict, None (without Secure; insecure origin) and an
     * invalid value are set by host1; the page from htmlunit-dev (another site) includes an image from host1.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void sameSiteDefaultCrossSiteImage() throws Exception {
        // HtmlUnit loads images only if there is an event handler (or the download is enabled)
        crossSite("<img src='" + URL_HOST1 + "target' onerror='void 0'>\n", null);
    }

    /**
     * Cookies without SameSite attribute (default), with Lax, Strict, None (without Secure; insecure origin) and an
     * invalid value are set by host1; the page from htmlunit-dev (another site) includes a script from host1.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void sameSiteDefaultCrossSiteScript() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_HOST1 + "target"), "var x = 1;", MimeType.TEXT_JAVASCRIPT);
        crossSite("<script src='" + URL_HOST1 + "target'></script>\n", null);
    }

    /**
     * Cookies without SameSite attribute (default), with Lax, Strict, None (without Secure; insecure origin) and an
     * invalid value are set by host1; the page from htmlunit-dev (another site) navigates (link) to host1.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("default=1; invalid=1; lax=1")
    public void sameSiteDefaultCrossSiteLink() throws Exception {
        crossSite("<a id='go' href='" + URL_HOST1 + "target'>go</a>\n", "go");
    }

    /**
     * Cookies without SameSite attribute (default), with Lax, Strict, None (without Secure; insecure origin) and an
     * invalid value are set by host1; the page from htmlunit-dev (another site) submits a form (GET) to host1.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("default=1; invalid=1; lax=1")
    public void sameSiteDefaultCrossSiteFormGet() throws Exception {
        crossSite("<form method='get' action='" + URL_HOST1 + "target'>"
                + "<input type='submit' id='go'></form>\n", "go");
    }

    /**
     * Cookies without SameSite attribute (default), with Lax, Strict, None (without Secure; insecure origin) and an
     * invalid value are set by host1; the page from htmlunit-dev (another site) submits a form (POST) to host1; some
     * browsers send cookies without SameSite attribute for a short time after they were set (Lax-allowing-unsafe).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("default=1; invalid=1")
    public void sameSiteDefaultCrossSiteFormPost() throws Exception {
        crossSite("<form method='post' action='" + URL_HOST1 + "target'>"
                + "<input type='submit' id='go'></form>\n", "go");
    }

    /**
     * Cookies without SameSite attribute (default), with Lax, Strict, None (without Secure; insecure origin) and an
     * invalid value are set by host1; the page from host2 (same site) includes an iframe from host1.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("default=1; invalid=1; lax=1; strict=1")
    public void sameSiteDefaultSameSiteIFrame() throws Exception {
        sameSite("<iframe src='" + URL_HOST1 + "target'></iframe>\n", null);
    }

    /**
     * The SameSite value of the cookies reported by the WebDriver api.
     * <p>HtmlUnitDriver reports the attribute value as it is (there is no default).</p>
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "default=1; path=/; domain=host1.htmlunit-dev.org; sameSite=Lax\n"
                + "lax=1; path=/; domain=host1.htmlunit-dev.org; sameSite=Lax\n"
                + "strict=1; path=/; domain=host1.htmlunit-dev.org; sameSite=Strict\n"
                + "none -\n"
                + "invalid=1; path=/; domain=host1.htmlunit-dev.org; sameSite=Lax",
            FF = "default=1; path=/; domain=host1.htmlunit-dev.org; sameSite=None\n"
                    + "lax=1; path=/; domain=host1.htmlunit-dev.org; sameSite=Lax\n"
                    + "strict=1; path=/; domain=host1.htmlunit-dev.org; sameSite=Strict\n"
                    + "none -\n"
                    + "invalid=1; path=/; domain=host1.htmlunit-dev.org; sameSite=None",
            FF_ESR = "default=1; path=/; domain=host1.htmlunit-dev.org; sameSite=None\n"
                    + "lax=1; path=/; domain=host1.htmlunit-dev.org; sameSite=Lax\n"
                    + "strict=1; path=/; domain=host1.htmlunit-dev.org; sameSite=Strict\n"
                    + "none -\n"
                    + "invalid=1; path=/; domain=host1.htmlunit-dev.org; sameSite=None")
    @HtmlUnitNYI(CHROME = "default=1; path=/; domain=host1.htmlunit-dev.org\n"
                    + "lax=1; path=/; domain=host1.htmlunit-dev.org; sameSite=lax\n"
                    + "strict=1; path=/; domain=host1.htmlunit-dev.org; sameSite=strict\n"
                    + "none -\n"
                    + "invalid=1; path=/; domain=host1.htmlunit-dev.org; sameSite=invalid",
            EDGE = "default=1; path=/; domain=host1.htmlunit-dev.org\n"
                    + "lax=1; path=/; domain=host1.htmlunit-dev.org; sameSite=lax\n"
                    + "strict=1; path=/; domain=host1.htmlunit-dev.org; sameSite=strict\n"
                    + "none -\n"
                    + "invalid=1; path=/; domain=host1.htmlunit-dev.org; sameSite=invalid",
            FF = "default=1; path=/; domain=host1.htmlunit-dev.org\n"
                    + "lax=1; path=/; domain=host1.htmlunit-dev.org; sameSite=lax\n"
                    + "strict=1; path=/; domain=host1.htmlunit-dev.org; sameSite=strict\n"
                    + "none -\n"
                    + "invalid=1; path=/; domain=host1.htmlunit-dev.org; sameSite=invalid",
            FF_ESR = "default=1; path=/; domain=host1.htmlunit-dev.org\n"
                    + "lax=1; path=/; domain=host1.htmlunit-dev.org; sameSite=lax\n"
                    + "strict=1; path=/; domain=host1.htmlunit-dev.org; sameSite=strict\n"
                    + "none -\n"
                    + "invalid=1; path=/; domain=host1.htmlunit-dev.org; sameSite=invalid")
    public void sameSiteDefaultReported() throws Exception {
        final URL url = new URL(URL_HOST1);
        getMockWebConnection().setResponse(url, "", 200, "OK", MimeType.TEXT_HTML, sameSiteCookies());
        final WebDriver driver = loadPage2(url, StandardCharsets.ISO_8859_1);

        final List<String> result = new ArrayList<>();
        for (final String name : new String[] {"default", "lax", "strict", "none", "invalid"}) {
            final org.openqa.selenium.Cookie cookie = driver.manage().getCookieNamed(name);
            result.add(cookie == null ? name + " -" : cookie.toString());
        }
        assertEquals(String.join("\n", getExpectedAlerts()), String.join("\n", result));
    }

    /**
     * Cookies without SameSite attribute (default), with Lax, Strict and an invalid value are set by host1 (top-
     * level). The top-level page is from htmlunit-dev (another site). The iframe from host1 logs document.cookie,
     * sets cookies (default, Lax, Strict) by document.cookie and logs document.cookie again; the top-level page gets
     * the values (postMessage) and navigates to host1, this page logs the values and its own cookies.
     *
     * <p>FF uses a partitioned cookie jar for the iframe (Total Cookie Protection); the cookie set by
     * the iframe is visible inside the iframe only. HtmlUnit ignores it like Chrome.</p>
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "", "default=1; invalid=1; lax=1; strict=1"},
            FF = {"", "jsDefault=1", "default=1; invalid=1; lax=1; strict=1"},
            FF_ESR = {"", "jsDefault=1", "default=1; invalid=1; lax=1; strict=1"})
    @HtmlUnitNYI(FF = {"", "", "default=1; invalid=1; lax=1; strict=1"},
            FF_ESR = {"", "", "default=1; invalid=1; lax=1; strict=1"})
    public void documentCookieCrossSiteIFrame() throws Exception {
        documentCookieInFrame(new URL(URL_HOST4 + "start"), null);
    }

    /**
     * Cookies without SameSite attribute (default), with Lax, Strict and an invalid value are set by host1 (top-
     * level). The top-level page is from host2 (same site). The iframe from host1 logs document.cookie, sets cookies
     * (default, Lax, Strict) by document.cookie and logs document.cookie again; the top-level page gets the values
     * (postMessage) and navigates to host1, this page logs the values and its own cookies.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"default=1; invalid=1; lax=1; strict=1",
             "default=1; invalid=1; jsDefault=1; jsLax=1; jsStrict=1; lax=1; strict=1",
             "default=1; invalid=1; jsDefault=1; jsLax=1; jsStrict=1; lax=1; strict=1"})
    public void documentCookieSameSiteIFrame() throws Exception {
        documentCookieInFrame(new URL(URL_HOST2 + "start"), null);
    }

    /**
     * Cookies without SameSite attribute (default), with Lax, Strict and an invalid value are set by host1 (top-
     * level). The top-level page is from htmlunit-dev (another site); it includes an iframe from host2 and this
     * includes the iframe from host1 (same site as its parent, but not as the top-level page). The iframe from host1
     * logs document.cookie, sets cookies (default, Lax, Strict) by document.cookie and logs document.cookie again;
     * the top-level page gets the values (postMessage) and navigates to host1, this page logs the values and its own
     * cookies.
     *
     * <p>FF uses a partitioned cookie jar for the iframe (Total Cookie Protection); the cookie set by
     * the iframe is visible inside the iframe only. HtmlUnit ignores it like Chrome.</p>
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "", "default=1; invalid=1; lax=1; strict=1"},
            FF = {"", "jsDefault=1", "default=1; invalid=1; lax=1; strict=1"},
            FF_ESR = {"", "jsDefault=1", "default=1; invalid=1; lax=1; strict=1"})
    @HtmlUnitNYI(FF = {"", "", "default=1; invalid=1; lax=1; strict=1"},
            FF_ESR = {"", "", "default=1; invalid=1; lax=1; strict=1"})
    public void documentCookieNestedCrossSiteIFrame() throws Exception {
        documentCookieInFrame(new URL(URL_HOST4 + "start"), new URL(URL_HOST2 + "middle"));
    }

    /**
     * Cookies without SameSite attribute (default), with Lax, Strict and an invalid value are set by host1 (top-
     * level). The top-level page is from htmlunit-dev (another site); it includes an iframe from host2 and this
     * includes the iframe from host1 (same site as its parent, but not as the top-level page); records the cookies
     * sent with the request for the iframe from host1.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void sameSiteNestedCrossSiteIFrameRequest() throws Exception {
        final URL middleUrl = new URL(URL_HOST2 + "middle");
        getMockWebConnection().setResponse(middleUrl, DOCTYPE_HTML
                + "<html><body><iframe src='" + URL_HOST1 + "target'></iframe></body></html>");
        crossSite("<iframe src='" + middleUrl + "'></iframe>\n", null);
    }

    /**
     * Sets the cookies (host1), loads the top-level page (including the iframe from host1, optional
     * wrapped by a middle iframe) and verifies the values reported by the iframe and the cookies of host1.
     */
    private void documentCookieInFrame(final URL startUrl, final URL middleUrl) throws Exception {
        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(new URL(URL_HOST1), "", 200, "OK", MimeType.TEXT_HTML, sameSiteCookies());

        final URL frameUrl = new URL(URL_HOST1 + "frame");
        final String frameHtml = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  function cookies() { return document.cookie.split('; ').sort().join('; '); }\n"
            + "  var r = [cookies()];\n"
            + "  document.cookie = 'jsDefault=1; path=/';\n"
            + "  document.cookie = 'jsLax=1; SameSite=Lax; path=/';\n"
            + "  document.cookie = 'jsStrict=1; SameSite=Strict; path=/';\n"
            + "  r.push(cookies());\n"
            + "  top.postMessage(r.join('|'), '*');\n"
            + "</script>\n"
            + "</head><body></body></html>";
        getMockWebConnection().setResponse(frameUrl, frameHtml);

        String content = "<iframe src='" + frameUrl + "'></iframe>\n";
        if (middleUrl != null) {
            getMockWebConnection().setResponse(middleUrl, DOCTYPE_HTML
                    + "<html><body>" + content + "</body></html>");
            content = "<iframe src='" + middleUrl + "'></iframe>\n";
        }

        final String startHtml = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  window.addEventListener('message', function(e) {\n"
            + "    location.replace('" + URL_HOST1 + "check?' + encodeURIComponent(e.data));\n"
            + "  });\n"
            + "</script>\n"
            + "</head><body>\n"
            + content
            + "</body></html>";
        getMockWebConnection().setResponse(startUrl, startHtml);

        final String checkHtml = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var r = decodeURIComponent(location.search.substring(1)).split('|');\n"
            + "  log(r[0]);\n"
            + "  log(r[1]);\n"
            + "  log(document.cookie.split('; ').sort().join('; '));\n"
            + "</script>\n"
            + "</head><body></body></html>";
        getMockWebConnection().setResponse(new URL(URL_HOST1 + "check"), checkHtml);

        // set the cookies
        final WebDriver driver = loadPage2(new URL(URL_HOST1), StandardCharsets.ISO_8859_1);

        driver.get(startUrl.toExternalForm());
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    private static List<NameValuePair> sameSiteCookies() {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Set-Cookie", "default=1; Path=/"));
        headers.add(new NameValuePair("Set-Cookie", "lax=1; SameSite=Lax; Path=/"));
        headers.add(new NameValuePair("Set-Cookie", "strict=1; SameSite=Strict; Path=/"));
        headers.add(new NameValuePair("Set-Cookie", "none=1; SameSite=None; Path=/"));
        headers.add(new NameValuePair("Set-Cookie", "invalid=1; SameSite=invalid; Path=/"));
        return headers;
    }

    private void crossSite(final String content, final String clickId) throws Exception {
        // htmlunit-dev is another site than host1.htmlunit-dev.org; not localhost because
        // localhost is a secure context (FF asks before submitting a form to an insecure host)
        verifySameSiteCookies(new URL(URL_HOST4 + "start"), content, clickId);
    }

    private void sameSite(final String content, final String clickId) throws Exception {
        verifySameSiteCookies(new URL(URL_HOST2 + "start"), content, clickId);
    }

    /**
     * Sets the cookies (host1), loads the start page and records the cookies
     * sent with the request to host1/target.
     */
    private void verifySameSiteCookies(final URL startUrl, final String content, final String clickId)
            throws Exception {
        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(new URL(URL_HOST1), "", 200, "OK", MimeType.TEXT_HTML, sameSiteCookies());

        final String startHtml = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body onload='log(\"done\")'>\n"
            + content
            + "</body></html>";
        getMockWebConnection().setResponse(startUrl, startHtml);

        final String targetHtml = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('target');\n"
            + "</script>\n"
            + "</head><body></body></html>";
        final URL targetUrl = new URL(URL_HOST1 + "target");
        if (!getMockWebConnection().hasResponse(targetUrl)) {
            getMockWebConnection().setResponse(targetUrl, targetHtml);
        }

        // set the cookies
        final WebDriver driver = loadPage2(new URL(URL_HOST1), StandardCharsets.ISO_8859_1);

        driver.get(startUrl.toExternalForm());
        verifyTitle2(DEFAULT_WAIT_TIME, driver, "done");
        if (clickId != null) {
            driver.findElement(By.id(clickId)).click();
            verifyTitle2(DEFAULT_WAIT_TIME, driver, "target");
        }

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        // ignore the query (form get)
        assertEquals(targetUrl.getHost() + targetUrl.getPath(),
                lastRequest.getUrl().getHost() + lastRequest.getUrl().getPath());

        // there is no order
        final String cookieHeader = lastRequest.getAdditionalHeaders().get(HttpHeader.COOKIE);
        final String cookies = cookieHeader == null ? "" : String.join("; ",
                Arrays.stream(cookieHeader.split(";\\s*")).sorted().toList());
        assertEquals(getExpectedAlerts()[0], cookies);
    }

    private void testCookies(final URL url, final String cookie1, final String cookie2) throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", cookie1));
        responseHeader.add(new NameValuePair("Set-Cookie", cookie2));
        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "OK", MimeType.TEXT_HTML,
                responseHeader);

        loadPage2(url, StandardCharsets.ISO_8859_1);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c2=Lax; c3=Strict; c4=empty; c5=unknown; c6=strict; c7=stRiCT; first=1")
    public void sameSite() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "first=1;"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=None; SameSite=None; Secure"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=Lax; SameSite=Lax"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=Strict; SameSite=Strict"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=empty; SameSite="));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=unknown; SameSite=unknown"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c6=strict; SameSite=strict"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c7=stRiCT; SameSite=stRiCT"));

        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(new URL(URL_HOST1), HTML_ALERT_COOKIE,
                                        200, "OK", MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(new URL(URL_HOST1), StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts());
        driver.get(URL_HOST1 + "foo");

        final Map<String, String> lastHeaders = getMockWebConnection().getLastAdditionalHeaders();

        // strange check, but there is no order
        final String lastCookies = lastHeaders.get(HttpHeader.COOKIE);
        assertEquals(70, lastCookies.length());

        assertTrue("lastCookies: " + lastCookies, lastCookies.contains("first=1")
                    && lastCookies.contains("c2=Lax")
                    && lastCookies.contains("c3=Strict")
                    && lastCookies.contains("c4=empty")
                    && lastCookies.contains("c5=unknown")
                    && lastCookies.contains("c6=strict")
                    && lastCookies.contains("c7=stRiCT"));

        if (driver instanceof HtmlUnitDriver) {
            final CookieManager mgr = getWebClient().getCookieManager();
            // c1 (secure) from an insecure origin is ignored
            assertEquals(7, mgr.getCookies().size());
            assertNull(mgr.getCookie("first").getSameSite());
            assertEquals("lax", mgr.getCookie("c2").getSameSite());
            assertEquals("strict", mgr.getCookie("c3").getSameSite());
            assertEquals("", mgr.getCookie("c4").getSameSite());
            assertEquals("unknown", mgr.getCookie("c5").getSameSite());
            assertEquals("strict", mgr.getCookie("c6").getSameSite());
            assertEquals("strict", mgr.getCookie("c7").getSameSite());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c2=Lax; c3=Strict; c4=empty; c5=unknown; first=1")
    public void sameSiteOtherSubdomain() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "first=1;"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c1=None; SameSite=None; Secure"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=Lax; SameSite=Lax"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=Strict; SameSite=Strict"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=empty; SameSite="));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=unknown; SameSite=unknown"));

        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(new URL(URL_HOST1), HTML_ALERT_COOKIE,
                                        200, "OK", MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(new URL(URL_HOST1), StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts());
        driver.get(URL_HOST2 + "foo");

        final Map<String, String> lastHeaders = getMockWebConnection().getLastAdditionalHeaders();

        // strange check, but there is no order
        final String lastCookies = lastHeaders.get(HttpHeader.COOKIE);
        assertNull(lastCookies);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c2=Lax; c3=Strict; c4=empty; c5=unknown; first=1")
    public void sameSiteOriginOtherSubdomain() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "first=1;"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c1=None; SameSite=None; Secure"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=Lax; SameSite=Lax"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=Strict; SameSite=Strict"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=empty; SameSite="));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=unknown; SameSite=unknown"));

        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(new URL(URL_HOST1), HTML_ALERT_COOKIE,
                                        200, "OK", MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(new URL(URL_HOST1), StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts());
        driver.get(URL_HOST2 + "foo");
        driver.get(URL_HOST1 + "foo");

        final Map<String, String> lastHeaders = getMockWebConnection().getLastAdditionalHeaders();

        // strange check, but there is no order
        final String lastCookies = lastHeaders.get(HttpHeader.COOKIE);
        assertEquals(48, lastCookies.length());

        assertTrue("lastCookies: " + lastCookies, lastCookies.contains("first=1")
                    && lastCookies.contains("c2=Lax")
                    && lastCookies.contains("c3=Strict")
                    && lastCookies.contains("c4=empty")
                    && lastCookies.contains("c5=unknown"));

        if (driver instanceof HtmlUnitDriver) {
            final CookieManager mgr = getWebClient().getCookieManager();
            // c1 (secure) from an insecure origin is ignored
            assertEquals(5, mgr.getCookies().size());
            assertNull(mgr.getCookie("first").getSameSite());
            assertEquals("lax", mgr.getCookie("c2").getSameSite());
            assertEquals("strict", mgr.getCookie("c3").getSameSite());
            assertEquals("", mgr.getCookie("c4").getSameSite());
            assertEquals("unknown", mgr.getCookie("c5").getSameSite());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c2=Lax; c3=Strict; c4=empty; c5=unknown; first=1")
    public void sameSiteIncludeFromSameDomain() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "first=1;"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c1=None; SameSite=None; Secure"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=Lax; SameSite=Lax"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=Strict; SameSite=Strict"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=empty; SameSite="));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=unknown; SameSite=unknown"));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <link rel='stylesheet' href='" + URL_HOST1 + "css/style.css'>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";
        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(new URL(URL_HOST1), HTML_ALERT_COOKIE,
                200, "OK", MimeType.TEXT_HTML, responseHeader);
        getMockWebConnection().setResponse(new URL(URL_HOST1 + "include"), html,
                                        200, "OK", MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(new URL(URL_HOST1), StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts());
        driver.get(URL_HOST1 + "include");

        assertEquals(URL_HOST1 + "css/style.css", getMockWebConnection().getLastWebRequest().getUrl());
        final Map<String, String> lastHeaders = getMockWebConnection().getLastAdditionalHeaders();

        // strange check, but there is no order
        final String lastCookies = lastHeaders.get(HttpHeader.COOKIE);
        assertEquals(48, lastCookies.length());

        assertTrue("lastCookies: " + lastCookies, lastCookies.contains("first=1")
                    && lastCookies.contains("c2=Lax")
                    && lastCookies.contains("c3=Strict")
                    && lastCookies.contains("c4=empty")
                    && lastCookies.contains("c5=unknown"));

        if (driver instanceof HtmlUnitDriver) {
            final CookieManager mgr = getWebClient().getCookieManager();
            // c1 (secure) from an insecure origin is ignored
            assertEquals(5, mgr.getCookies().size());
            assertNull(mgr.getCookie("first").getSameSite());
            assertEquals("lax", mgr.getCookie("c2").getSameSite());
            assertEquals("strict", mgr.getCookie("c3").getSameSite());
            assertEquals("", mgr.getCookie("c4").getSameSite());
            assertEquals("unknown", mgr.getCookie("c5").getSameSite());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c2=Lax; c3=Strict; c4=empty; c5=unknown; first=1")
    public void sameSiteIncludeFromSubDomain() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "first=1;"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c1=None; SameSite=None; Secure"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=Lax; SameSite=Lax"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=Strict; SameSite=Strict"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=empty; SameSite="));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=unknown; SameSite=unknown"));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <link rel='stylesheet' href='" + URL_HOST1 + "css/style.css'>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";
        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(new URL(URL_HOST1), HTML_ALERT_COOKIE,
                200, "OK", MimeType.TEXT_HTML, responseHeader);
        getMockWebConnection().setResponse(new URL(URL_HOST2), html,
                                        200, "OK", MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(new URL(URL_HOST1), StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts());
        driver.get(URL_HOST2);

        assertEquals(URL_HOST1 + "css/style.css", getMockWebConnection().getLastWebRequest().getUrl());
        final Map<String, String> lastHeaders = getMockWebConnection().getLastAdditionalHeaders();

        // strange check, but there is no order
        final String lastCookies = lastHeaders.get(HttpHeader.COOKIE);
        assertEquals(48, lastCookies.length());

        assertTrue("lastCookies: " + lastCookies, lastCookies.contains("first=1")
                    && lastCookies.contains("c2=Lax")
                    && lastCookies.contains("c3=Strict")
                    && lastCookies.contains("c4=empty")
                    && lastCookies.contains("c5=unknown"));

        if (driver instanceof HtmlUnitDriver) {
            final CookieManager mgr = getWebClient().getCookieManager();
            // c1 (secure) from an insecure origin is ignored
            assertEquals(10, mgr.getCookies().size());
            assertNull(mgr.getCookie("first").getSameSite());
            assertEquals("lax", mgr.getCookie("c2").getSameSite());
            assertEquals("strict", mgr.getCookie("c3").getSameSite());
            assertEquals("", mgr.getCookie("c4").getSameSite());
            assertEquals("unknown", mgr.getCookie("c5").getSameSite());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c2=Lax; c3=Strict; c4=empty; c5=unknown; first=1")
    public void sameSiteIFrameFromSameDomain() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "first=1;"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c1=None; SameSite=None; Secure"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=Lax; SameSite=Lax"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=Strict; SameSite=Strict"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=empty; SameSite="));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=unknown; SameSite=unknown"));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<iframe src='" + URL_HOST1 + "iframe.html'></iframe>\n"
                + "</body></html>";
        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(new URL(URL_HOST1), HTML_ALERT_COOKIE,
                                        200, "OK", MimeType.TEXT_HTML, responseHeader);
        getMockWebConnection().setResponse(new URL(URL_HOST1 + "include"), html,
                                        200, "OK", MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(new URL(URL_HOST1), StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts());
        driver.get(URL_HOST1 + "include");

        assertEquals(URL_HOST1 + "iframe.html", getMockWebConnection().getLastWebRequest().getUrl());
        final Map<String, String> lastHeaders = getMockWebConnection().getLastAdditionalHeaders();

        // strange check, but there is no order
        final String lastCookies = lastHeaders.get(HttpHeader.COOKIE);
        assertEquals(48, lastCookies.length());

        assertTrue("lastCookies: " + lastCookies, lastCookies.contains("first=1")
                    && lastCookies.contains("c2=Lax")
                    && lastCookies.contains("c3=Strict")
                    && lastCookies.contains("c4=empty")
                    && lastCookies.contains("c5=unknown"));

        if (driver instanceof HtmlUnitDriver) {
            final CookieManager mgr = getWebClient().getCookieManager();
            // c1 (secure) from an insecure origin is ignored
            assertEquals(5, mgr.getCookies().size());
            assertNull(mgr.getCookie("first").getSameSite());
            assertEquals("lax", mgr.getCookie("c2").getSameSite());
            assertEquals("strict", mgr.getCookie("c3").getSameSite());
            assertEquals("", mgr.getCookie("c4").getSameSite());
            assertEquals("unknown", mgr.getCookie("c5").getSameSite());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c2=Lax; c3=Strict; c4=empty; c5=unknown; first=1")
    public void sameSiteIFrameFromSubDomain() throws Exception {
        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "first=1;"));

        responseHeader.add(new NameValuePair("Set-Cookie", "c1=None; SameSite=None; Secure"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=Lax; SameSite=Lax"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=Strict; SameSite=Strict"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c4=empty; SameSite="));
        responseHeader.add(new NameValuePair("Set-Cookie", "c5=unknown; SameSite=unknown"));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<iframe src='" + URL_HOST1 + "iframe.html'></iframe>\n"
                + "</body></html>";
        getMockWebConnection().setDefaultResponse("");
        getMockWebConnection().setResponse(new URL(URL_HOST1), HTML_ALERT_COOKIE,
                                        200, "OK", MimeType.TEXT_HTML, responseHeader);
        getMockWebConnection().setResponse(new URL(URL_HOST2 + "include"), html,
                                        200, "OK", MimeType.TEXT_HTML, responseHeader);

        final WebDriver driver = loadPage2(new URL(URL_HOST1), StandardCharsets.ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts());
        driver.get(URL_HOST2 + "include");

        assertEquals(URL_HOST1 + "iframe.html", getMockWebConnection().getLastWebRequest().getUrl());
        final Map<String, String> lastHeaders = getMockWebConnection().getLastAdditionalHeaders();

        // strange check, but there is no order
        final String lastCookies = lastHeaders.get(HttpHeader.COOKIE);
        assertEquals(48, lastCookies.length());

        assertTrue("lastCookies: " + lastCookies, lastCookies.contains("first=1")
                    && lastCookies.contains("c2=Lax")
                    && lastCookies.contains("c3=Strict")
                    && lastCookies.contains("c4=empty")
                    && lastCookies.contains("c5=unknown"));

        if (driver instanceof HtmlUnitDriver) {
            final CookieManager mgr = getWebClient().getCookieManager();
            // c1 (secure) from an insecure origin is ignored
            assertEquals(10, mgr.getCookies().size());
            assertNull(mgr.getCookie("first").getSameSite());
            assertEquals("lax", mgr.getCookie("c2").getSameSite());
            assertEquals("strict", mgr.getCookie("c3").getSameSite());
            assertEquals("", mgr.getCookie("c4").getSameSite());
            assertEquals("unknown", mgr.getCookie("c5").getSameSite());
        }
    }

    /**
     * Test for issue #270.
     * @throws Exception in case of error
     */
    @Test
    @Alerts("JDSessionID=1234567890")
    public void issue270() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "first=1; path=/c"));

        final String html = DOCTYPE_HTML
            + "<html>\n"
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
            + "setCookie('JDSessionID', '1234567890', now, '/', 'htmlunit-dev.org');\n"

//             + "alert('cookies: ' + document.cookie);\n"

            + "</script></body>\n"
            + "</html>";

        final URL firstUrl = new URL(URL_HOST1);
        getMockWebConnection().setResponse(firstUrl, html);
        loadPage2(html, firstUrl);

        loadPage2(HTML_ALERT_COOKIE, firstUrl);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    @Override
    protected final WebDriver getWebDriver() {
        final WebDriver driver = super.getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            // set timeout to fail fast when the url not mapped
            ((HtmlUnitDriver) driver).getWebClient().getOptions().setTimeout(1000);
        }

        return driver;
    }
}
