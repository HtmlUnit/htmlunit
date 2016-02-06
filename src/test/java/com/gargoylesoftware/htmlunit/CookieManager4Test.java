/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
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
        startWebServer(getMockWebConnection());
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
        responseHeader.add(new NameValuePair("Set-Cookie", "c1=1; Domain=.htmlunit.org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c2=2; Domain=htmlunit.org; Path=/"));
        responseHeader.add(new NameValuePair("Set-Cookie", "c3=3; Domain=.htmlunit.org:" + PORT + "; Path=/"));
        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "Ok",
                "text/html", responseHeader);

        final URL firstUrl = new URL(URL_HOST1);

        loadPageWithAlerts2(firstUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("c4=4")
    public void domain2() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "c1=1; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c2=2; Domain=host1.htmlunit.org; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c3=3; Domain=host2.htmlunit.org; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c4=4; Domain=htmlunit.org; Path=/"));
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
        getMockWebConnection().setResponse(firstUrl, html, 200, "Ok", "text/html", responseHeader1);

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
        responseHeader1.add(new NameValuePair("Set-Cookie", "c2=2; Domain=host1.htmlunit.org; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c3=3; Domain=host2.htmlunit.org; Path=/"));
        responseHeader1.add(new NameValuePair("Set-Cookie", "c4=4; Domain=htmlunit.org; Path=/"));
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
        getMockWebConnection().setResponse(firstUrl, html, 200, "Ok", "text/html", responseHeader1);

        loadPageWithAlerts2(firstUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cross-domain=1")
    public void differentHostsSameDomain() throws Exception {
        final List<NameValuePair> responseHeader1 = new ArrayList<>();
        responseHeader1.add(new NameValuePair("Set-Cookie", "cross-domain=1; Domain=.htmlunit.org; Path=/"));

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
        getMockWebConnection().setResponse(firstUrl, html, 200, "Ok", "text/html", responseHeader1);

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
            + "  document.cookie='cross-domain=1; Domain=.htmlunit.org; Path=/';\n"
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
            + "  <meta http-equiv='Set-Cookie', content='cross-domain=1; Domain=.htmlunit.org; Path=/'>"
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
        getMockWebConnection().setDefaultResponse(CookieManagerTest.HTML_ALERT_COOKIE, 200, "OK", "text/html",
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
