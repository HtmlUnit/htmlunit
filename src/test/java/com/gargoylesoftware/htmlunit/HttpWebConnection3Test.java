/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static org.junit.Assert.fail;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests using the {@link PrimitiveWebServer}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnection3Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {HttpHeader.HOST, HttpHeader.CONNECTION, HttpHeader.UPGRADE_INSECURE_REQUESTS,
                        HttpHeader.USER_AGENT,
                        HttpHeader.SEC_FETCH_USER, HttpHeader.ACCEPT,
                        HttpHeader.SEC_FETCH_SITE, HttpHeader.SEC_FETCH_MODE,
                        HttpHeader.ACCEPT_ENCODING, HttpHeader.ACCEPT_LANGUAGE},
            FF = {HttpHeader.HOST, HttpHeader.USER_AGENT, HttpHeader.ACCEPT, HttpHeader.ACCEPT_LANGUAGE,
                        HttpHeader.ACCEPT_ENCODING, HttpHeader.CONNECTION, HttpHeader.UPGRADE_INSECURE_REQUESTS},
            IE = {HttpHeader.ACCEPT, HttpHeader.ACCEPT_LANGUAGE, HttpHeader.USER_AGENT,
                        HttpHeader.ACCEPT_ENCODING, HttpHeader.HOST, HttpHeader.CONNECTION})
    public void headers() throws Exception {
        final String response = "HTTP/1.1 200 OK\r\n"
            + "Content-Length: 2\r\n"
            + "Content-Type: text/plain\r\n"
            + "\r\n"
            + "Hi";

        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, null)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());
            final String request = primitiveWebServer.getRequests().get(0);
            final String[] headers = request.split("\\r\\n");
            final String[] result = new String[headers.length - 1];
            for (int i = 0; i < result.length; i++) {
                final String header = headers[i + 1];
                result[i] = header.substring(0, header.indexOf(':'));
            }
            assertEquals(Arrays.asList(getExpectedAlerts()).toString(), Arrays.asList(result).toString());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {HttpHeader.HOST, HttpHeader.CONNECTION, HttpHeader.UPGRADE_INSECURE_REQUESTS,
                        HttpHeader.USER_AGENT,
                        HttpHeader.SEC_FETCH_USER, HttpHeader.ACCEPT,
                        HttpHeader.SEC_FETCH_SITE, HttpHeader.SEC_FETCH_MODE,
                        HttpHeader.REFERER, HttpHeader.ACCEPT_ENCODING, HttpHeader.ACCEPT_LANGUAGE,
                        HttpHeader.COOKIE},
            FF60 = {HttpHeader.HOST, HttpHeader.USER_AGENT, HttpHeader.ACCEPT, HttpHeader.ACCEPT_LANGUAGE,
                        HttpHeader.ACCEPT_ENCODING, HttpHeader.REFERER, HttpHeader.COOKIE, HttpHeader.CONNECTION,
                        HttpHeader.UPGRADE_INSECURE_REQUESTS},
            FF68 = {HttpHeader.HOST, HttpHeader.USER_AGENT, HttpHeader.ACCEPT, HttpHeader.ACCEPT_LANGUAGE,
                    HttpHeader.ACCEPT_ENCODING, HttpHeader.REFERER, HttpHeader.CONNECTION, HttpHeader.COOKIE,
                    HttpHeader.UPGRADE_INSECURE_REQUESTS},
            IE = {HttpHeader.ACCEPT, HttpHeader.REFERER, HttpHeader.ACCEPT_LANGUAGE, HttpHeader.USER_AGENT,
                        HttpHeader.ACCEPT_ENCODING, HttpHeader.HOST, HttpHeader.CONNECTION,
                        HttpHeader.COOKIE})
    public void headers_cookie_referer() throws Exception {
        final String htmlResponse = "<a href='2.html'>Click me</a>";
        final String response = "HTTP/1.1 200 OK\r\n"
            + "Content-Length: " + htmlResponse.length() + "\r\n"
            + "Content-Type: text/html\r\n"
            + "Set-Cookie: name=value\r\n"
            + "\r\n"
            + htmlResponse;

        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, null)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());
            driver.findElement(By.linkText("Click me")).click();

            final Wait<WebDriver> wait = new WebDriverWait(driver, 5);
            wait.until(currentUrlContains("2.html"));

            int index = 1;
            String request;
            do {
                request = primitiveWebServer.getRequests().get(index++);
            }
            while (request.contains("/favicon.ico"));

            final String[] headers = request.split("\\r\\n");
            final String[] result = new String[headers.length - 1];
            for (int i = 0; i < result.length; i++) {
                final String header = headers[i + 1];
                result[i] = header.substring(0, header.indexOf(':'));
            }
            assertEquals(Arrays.asList(getExpectedAlerts()).toString(), Arrays.asList(result).toString());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "gzip, deflate",
            CHROME = "gzip, deflate, br")
    public void acceptEncoding() throws Exception {
        final String response = "HTTP/1.1 200 OK\r\n"
            + "Content-Length: 2\r\n"
            + "Content-Type: text/plain\r\n"
            + "\r\n"
            + "Hi";

        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, null)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());
            final String request = primitiveWebServer.getRequests().get(0);
            final String[] headers = request.split("\\r\\n");
            for (int i = 0; i < headers.length; i++) {
                final String header = headers[i];
                if (StringUtils.startsWithIgnoreCase(header, HttpHeader.ACCEPT_ENCODING_LC)) {
                    final String value = header.substring(header.indexOf(':') + 1);
                    assertEquals(getExpectedAlerts()[0], value.trim());
                    return;
                }
            }
            fail("No accept-encoding header found.");
        }
    }

    /**
     * An expectation for checking that the current url contains a case-sensitive substring.
     *
     * @param url the fragment of url expected
     * @return true when the url matches, false otherwise
     */
    public static ExpectedCondition<Boolean> currentUrlContains(final String url) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                final String currentUrl = driver.getCurrentUrl();
                return currentUrl != null && currentUrl.contains(url);
            }
        };
    }

    /**
     * Test for bug #1898.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("§§URL§§?????")
    // seems to work only when running alone
    public void locationUTF() throws Exception {
        final String url = "http://localhost:" + PORT_PRIMITIVE_SERVER + "/";

        final String response = "HTTP/1.1 302 Found\r\n"
                + "Content-Length: 0\r\n"
                + "Location: " +  url + "\u0623\u0647\u0644\u0627\u064b" + "\r\n"
                + "\r\n";

        final String response2 = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "Hi";

        expandExpectedAlertsVariables(new URL(url));

        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, response2)) {
            final WebDriver driver = getWebDriver();

            driver.get(url);
            assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());
            assertTrue(driver.getPageSource().contains("Hi"));

            assertEquals(2, primitiveWebServer.getRequests().size());
        }
    }

    /**
     * Test for bug #1898.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§test?%D8%A3%D9%87%D9%84%D8%A7%D9%8B",
            CHROME = "§§URL§§")
    @NotYetImplemented(CHROME)
    public void locationQueryUTF8Encoded() throws Exception {
        final String url = "http://localhost:" + PORT_PRIMITIVE_SERVER + "/";

        final String response = "HTTP/1.1 302 Found\r\n"
                + "Content-Length: 0\r\n"
                + "Location: "
                    +  url
                    + "test?"
                    + URLEncoder.encode("\u0623\u0647\u0644\u0627\u064b", "UTF-8")
                    + "\r\n"
                + "\r\n";

        final String response2 = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "Hi";

        expandExpectedAlertsVariables(new URL(url));

        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, response2)) {
            final WebDriver driver = getWebDriver();

            driver.get(url);
            assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());
            assertTrue(driver.getPageSource().contains("Hi"));
            assertEquals(2, primitiveWebServer.getRequests().size());
        }
    }

    /**
     * Test for bug #1898.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("§§URL§§%D8%A3%D9%87%D9%84%D8%A7%D9%8B")
    public void locationUTF8Encoded() throws Exception {
        final String url = "http://localhost:" + PORT_PRIMITIVE_SERVER + "/";

        final String response = "HTTP/1.1 302 Found\r\n"
                + "Content-Length: 0\r\n"
                + "Location: "
                    +  url
                    + URLEncoder.encode("\u0623\u0647\u0644\u0627\u064b", "UTF-8")
                    + "\r\n"
                + "\r\n";

        final String response2 = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "Hi";

        expandExpectedAlertsVariables(new URL(url));

        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, response2)) {
            final WebDriver driver = getWebDriver();

            driver.get(url);
            assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());
            assertTrue(driver.getPageSource().contains("Hi"));

            assertEquals(2, primitiveWebServer.getRequests().size());
        }
    }

    /**
     * Test case for Bug #1882.
     *
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void queryString() throws Exception {
        final String response = "HTTP/1.1 302 Found\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n";

        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, null)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort() + "?para=%u65E5");
            assertTrue(primitiveWebServer.getRequests().get(0),
                        primitiveWebServer.getRequests().get(0).contains("para=%u65E5"));
        }
    }
}
