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

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.PrimitiveWebServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Tests using the {@link PrimitiveWebServer}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HttpWebConnection3Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {HttpHeader.HOST, HttpHeader.CONNECTION, HttpHeader.SEC_CH_UA, HttpHeader.SEC_CH_UA_MOBILE,
                       HttpHeader.SEC_CH_UA_PLATFORM,
                       HttpHeader.UPGRADE_INSECURE_REQUESTS, HttpHeader.USER_AGENT, HttpHeader.ACCEPT,
                       HttpHeader.SEC_FETCH_SITE, HttpHeader.SEC_FETCH_MODE, HttpHeader.SEC_FETCH_USER,
                       HttpHeader.SEC_FETCH_DEST, HttpHeader.ACCEPT_ENCODING, HttpHeader.ACCEPT_LANGUAGE},
            FF = {HttpHeader.HOST, HttpHeader.USER_AGENT, HttpHeader.ACCEPT, HttpHeader.ACCEPT_LANGUAGE,
                  HttpHeader.ACCEPT_ENCODING, HttpHeader.CONNECTION, HttpHeader.UPGRADE_INSECURE_REQUESTS,
                  HttpHeader.SEC_FETCH_DEST, HttpHeader.SEC_FETCH_MODE, HttpHeader.SEC_FETCH_SITE,
                  HttpHeader.SEC_FETCH_USER, HttpHeader.PRIORITY},
            FF_ESR = {HttpHeader.HOST, HttpHeader.USER_AGENT, HttpHeader.ACCEPT, HttpHeader.ACCEPT_LANGUAGE,
                      HttpHeader.ACCEPT_ENCODING, HttpHeader.CONNECTION, HttpHeader.UPGRADE_INSECURE_REQUESTS,
                      HttpHeader.SEC_FETCH_DEST, HttpHeader.SEC_FETCH_MODE, HttpHeader.SEC_FETCH_SITE,
                      HttpHeader.SEC_FETCH_USER, HttpHeader.PRIORITY})
    public void headers() throws Exception {
        final String response = "HTTP/1.1 200 OK\r\n"
            + "Content-Length: 2\r\n"
            + "Content-Type: text/plain\r\n"
            + "Connection: close\r\n"
            + "\r\n"
            + "Hi";

        shutDownAll();
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
    @Alerts(DEFAULT = {HttpHeader.HOST, HttpHeader.CONNECTION,
                       HttpHeader.SEC_CH_UA, HttpHeader.SEC_CH_UA_MOBILE,
                       HttpHeader.SEC_CH_UA_PLATFORM,
                       HttpHeader.UPGRADE_INSECURE_REQUESTS,
                       HttpHeader.USER_AGENT, HttpHeader.ACCEPT, HttpHeader.SEC_FETCH_SITE,
                       HttpHeader.SEC_FETCH_MODE, HttpHeader.SEC_FETCH_USER, HttpHeader.SEC_FETCH_DEST,
                       HttpHeader.REFERER, HttpHeader.ACCEPT_ENCODING, HttpHeader.ACCEPT_LANGUAGE,
                       HttpHeader.COOKIE},
            FF = {HttpHeader.HOST, HttpHeader.USER_AGENT, HttpHeader.ACCEPT, HttpHeader.ACCEPT_LANGUAGE,
                  HttpHeader.ACCEPT_ENCODING, HttpHeader.CONNECTION, HttpHeader.REFERER, HttpHeader.COOKIE,
                  HttpHeader.UPGRADE_INSECURE_REQUESTS, HttpHeader.SEC_FETCH_DEST, HttpHeader.SEC_FETCH_MODE,
                  HttpHeader.SEC_FETCH_SITE, HttpHeader.SEC_FETCH_USER, HttpHeader.PRIORITY},
            FF_ESR = {HttpHeader.HOST, HttpHeader.USER_AGENT, HttpHeader.ACCEPT, HttpHeader.ACCEPT_LANGUAGE,
                      HttpHeader.ACCEPT_ENCODING, HttpHeader.CONNECTION, HttpHeader.REFERER, HttpHeader.COOKIE,
                      HttpHeader.UPGRADE_INSECURE_REQUESTS, HttpHeader.SEC_FETCH_DEST, HttpHeader.SEC_FETCH_MODE,
                      HttpHeader.SEC_FETCH_SITE, HttpHeader.SEC_FETCH_USER, HttpHeader.PRIORITY})
    public void headers_cookie_referer() throws Exception {
        final String htmlResponse = "<a href='2.html'>Click me</a>";
        final String response = "HTTP/1.1 200 OK\r\n"
            + "Content-Length: " + htmlResponse.length() + "\r\n"
            + "Content-Type: text/html\r\n"
            + "Connection: close\r\n"
            + "Set-Cookie: name=value\r\n"
            + "\r\n"
            + htmlResponse;

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, null)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());
            driver.findElement(By.linkText("Click me")).click();

            final Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
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
    @Alerts("gzip, deflate, br, zstd")
    @HtmlUnitNYI(CHROME = "gzip, deflate, br",
            EDGE = "gzip, deflate, br",
            FF = "gzip, deflate, br",
            FF_ESR = "gzip, deflate, br")
    public void acceptEncoding() throws Exception {
        final String response = "HTTP/1.1 200 OK\r\n"
            + "Content-Length: 2\r\n"
            + "Content-Type: text/plain\r\n"
            + "Connection: close\r\n"
            + "\r\n"
            + "Hi";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, null)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());
            final String request = primitiveWebServer.getRequests().get(0);
            final String[] headers = request.split("\\r\\n");
            for (final String header : headers) {
                if (StringUtils.startsWithIgnoreCase(header, HttpHeader.ACCEPT_ENCODING_LC)) {
                    final String value = header.substring(header.indexOf(':') + 1);
                    assertEquals(getExpectedAlerts()[0], value.trim());
                    return;
                }
            }
            Assertions.fail("No accept-encoding header found.");
        }
    }

    /**
     * An expectation for checking that the current url contains a case-sensitive substring.
     *
     * @param url the fragment of url expected
     * @return true when the url matches, false otherwise
     */
    public static ExpectedCondition<Boolean> currentUrlContains(final String url) {
        return driver -> {
            final String currentUrl = driver.getCurrentUrl();
            return currentUrl != null && currentUrl.contains(url);
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
                + "Connection: close\r\n"
                + "\r\n"
                + "Hi";

        expandExpectedAlertsVariables(new URL(url));

        shutDownAll();
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
    @Alerts("§§URL§§test?%D8%A3%D9%87%D9%84%D8%A7%D9%8B")
    public void locationQueryUTF8Encoded() throws Exception {
        final String url = "http://localhost:" + PORT_PRIMITIVE_SERVER + "/";

        final String response = "HTTP/1.1 302 Found\r\n"
                + "Content-Length: 0\r\n"
                + "Location: "
                    +  url
                    + "test?"
                    + URLEncoder.encode("\u0623\u0647\u0644\u0627\u064b", StandardCharsets.UTF_8)
                    + "\r\n"
                + "\r\n";

        final String response2 = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + "Hi";

        expandExpectedAlertsVariables(new URL(url));

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, response2)) {
            final WebDriver driver = getWebDriver();

            driver.get(url);
            Thread.sleep(DEFAULT_WAIT_TIME.toMillis());
            assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());
            assertEquals(2, primitiveWebServer.getRequests().size());
            assertTrue(driver.getPageSource(), driver.getPageSource().contains("Hi"));
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
                    + URLEncoder.encode("\u0623\u0647\u0644\u0627\u064b", StandardCharsets.UTF_8)
                    + "\r\n"
                + "\r\n";

        final String response2 = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + "Hi";

        expandExpectedAlertsVariables(new URL(url));

        shutDownAll();
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
    @Alerts("para=%u65E5")
    @HtmlUnitNYI(CHROME = "para=%25u65E5",
            EDGE = "para=%25u65E5",
            FF = "para=%25u65E5",
            FF_ESR = "para=%25u65E5")
    public void queryString() throws Exception {
        final String response = "HTTP/1.1 302 Found\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, null)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort() + "?para=%u65E5");
            assertTrue(primitiveWebServer.getRequests().get(0),
                        primitiveWebServer.getRequests().get(0).contains(getExpectedAlerts()[0]));
        }
    }

    /**
     * Tests a form get request.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"GET /foo?text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21 HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "Connection: keep-alive",
                      "sec-ch-ua: §§SEC_USER_AGENT§§",
                      "sec-ch-ua-mobile: ?0",
                      "sec-ch-ua-platform: \"Windows\"",
                      "Upgrade-Insecure-Requests: 1",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-User: ?1",
                      "Sec-Fetch-Dest: document",
                      "Referer: http://localhost:§§PORT§§/",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /foo?text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21 HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-User: ?1",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /foo?text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21 HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Sec-Fetch-User: ?1",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /foo?text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21 HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-User: ?1",
                      "Priority: u=0, i"})
    @HtmlUnitNYI(CHROME = {"GET /foo?text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21 HTTP/1.1",
                           "Host: localhost:§§PORT§§",
                           "Connection: keep-alive",
                           "sec-ch-ua: §§SEC_USER_AGENT§§",
                           "sec-ch-ua-mobile: ?0",
                           "sec-ch-ua-platform: \"Windows\"",
                           "Upgrade-Insecure-Requests: 1",
                           "User-Agent: §§USER_AGENT§§",
                           "Accept: §§ACCEPT§§",
                           "Sec-Fetch-Site: same-origin",
                           "Sec-Fetch-Mode: navigate",
                           "Sec-Fetch-User: ?1",
                           "Sec-Fetch-Dest: document",
                           "Referer: http://localhost:§§PORT§§/",
                           "Accept-Encoding: gzip, deflate, br",
                           "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /foo?text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21 HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-User: ?1",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /foo?text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21 HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Sec-Fetch-User: ?1",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /foo?text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21 HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-User: ?1",
                      "Priority: u=0, i"})
    public void formGet() throws Exception {
        String html = DOCTYPE_HTML
            + "<html><body><form action='foo' method='get' accept-charset='iso-8859-1'>\n"
            + "<input name='text1' value='me &amp;amp; you'>\n"
            + "<textarea name='text2'>Hello\nworld!</textarea>\n"
            + "<input type='submit' id='submit'>\n"
            + "</form></body></html>";
        html = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + (html.length()) + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String hi = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/plain\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + "Hi";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, html, hi)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());
            driver.findElement(By.id("submit")).click();

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§ACCEPT§§",
                        getBrowserVersion().getHtmlAcceptHeader());
            }
            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }

    /**
     * Tests a form post request.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"POST /foo HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "Connection: keep-alive",
                      "Content-Length: 48",
                      "Cache-Control: max-age=0",
                      "sec-ch-ua: §§SEC_USER_AGENT§§",
                      "sec-ch-ua-mobile: ?0",
                      "sec-ch-ua-platform: \"Windows\"",
                      "Upgrade-Insecure-Requests: 1",
                      "Content-Type: application/x-www-form-urlencoded",
                      "User-Agent: §§USER_AGENT§§",
                      "Origin: http://localhost:§§PORT§§",
                      "Accept: §§ACCEPT§§",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-User: ?1",
                      "Sec-Fetch-Dest: document",
                      "Referer: http://localhost:§§PORT§§/",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Accept-Language: en-US,en;q=0.9",
                      "",
                      "text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21"},
            EDGE = {"POST /foo HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "Content-Length: 48",
                    "Cache-Control: max-age=0",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "Content-Type: application/x-www-form-urlencoded",
                    "User-Agent: §§USER_AGENT§§",
                    "Origin: http://localhost:§§PORT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-User: ?1",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9",
                    "",
                    "text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21"},
            FF = {"POST /foo HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Content-Type: application/x-www-form-urlencoded",
                  "Content-Length: 48",
                  "Origin: http://localhost:§§PORT§§",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Sec-Fetch-User: ?1",
                  "Priority: u=0, i",
                  "",
                  "text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21"},
            FF_ESR = {"POST /foo HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Content-Type: application/x-www-form-urlencoded",
                      "Content-Length: 48",
                      "Origin: http://localhost:§§PORT§§",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-User: ?1",
                      "Priority: u=0, i",
                      "",
                      "text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21"})
    @HtmlUnitNYI(CHROME = {"POST /foo HTTP/1.1",
                           "Host: localhost:§§PORT§§",
                           "Connection: keep-alive",
                           "sec-ch-ua: §§SEC_USER_AGENT§§",
                           "sec-ch-ua-mobile: ?0",
                           "sec-ch-ua-platform: \"Windows\"",
                           "Upgrade-Insecure-Requests: 1",
                           "User-Agent: §§USER_AGENT§§",
                           "Accept: §§ACCEPT§§",
                           "Sec-Fetch-Site: same-origin",
                           "Sec-Fetch-Mode: navigate",
                           "Sec-Fetch-User: ?1",
                           "Sec-Fetch-Dest: document",
                           "Referer: http://localhost:§§PORT§§/",
                           "Accept-Encoding: gzip, deflate, br",
                           "Accept-Language: en-US,en;q=0.9",
                           "Origin: http://localhost:§§PORT§§",
                           "Cache-Control: max-age=0",
                           "Content-Length: 48",
                           "Content-Type: application/x-www-form-urlencoded",
                           "",
                           "text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21"},
            EDGE = {"POST /foo HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-User: ?1",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9",
                    "Origin: http://localhost:§§PORT§§",
                    "Cache-Control: max-age=0",
                    "Content-Length: 48",
                    "Content-Type: application/x-www-form-urlencoded",
                    "",
                    "text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21"},
            FF = {"POST /foo HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Sec-Fetch-User: ?1",
                  "Priority: u=0, i",
                  "Origin: http://localhost:§§PORT§§",
                  "Content-Length: 48",
                  "Content-Type: application/x-www-form-urlencoded",
                  "",
                  "text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21"},
            FF_ESR = {"POST /foo HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-User: ?1",
                      "Priority: u=0, i",
                      "Origin: http://localhost:§§PORT§§",
                      "Content-Length: 48",
                      "Content-Type: application/x-www-form-urlencoded",
                      "",
                      "text1=me+%26amp%3B+you&text2=Hello%0D%0Aworld%21"})
    public void formPost() throws Exception {
        String html = DOCTYPE_HTML
            + "<html><body><form action='foo' method='post' accept-charset='iso-8859-1'>\n"
            + "<input name='text1' value='me &amp;amp; you'>\n"
            + "<textarea name='text2'>Hello\nworld!</textarea>\n"
            + "<input type='submit' id='submit'>\n"
            + "</form></body></html>";
        html = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + (html.length()) + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String hi = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/plain\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + "Hi";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, html, hi)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());
            Thread.sleep(4_000);
            driver.findElement(By.id("submit")).click();

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§ACCEPT§§",
                        getBrowserVersion().getHtmlAcceptHeader());
            }
            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }

    /**
     * Tests a anchor click request.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"GET /2.html HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "Connection: keep-alive",
                      "sec-ch-ua: §§SEC_USER_AGENT§§",
                      "sec-ch-ua-mobile: ?0",
                      "sec-ch-ua-platform: \"Windows\"",
                      "Upgrade-Insecure-Requests: 1",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-User: ?1",
                      "Sec-Fetch-Dest: document",
                      "Referer: http://localhost:§§PORT§§/",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /2.html HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-User: ?1",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /2.html HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Sec-Fetch-User: ?1",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /2.html HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-User: ?1",
                      "Priority: u=0, i"})
    @HtmlUnitNYI(CHROME = {"GET /2.html HTTP/1.1",
                           "Host: localhost:§§PORT§§",
                           "Connection: keep-alive",
                           "sec-ch-ua: §§SEC_USER_AGENT§§",
                           "sec-ch-ua-mobile: ?0",
                           "sec-ch-ua-platform: \"Windows\"",
                           "Upgrade-Insecure-Requests: 1",
                           "User-Agent: §§USER_AGENT§§",
                           "Accept: §§ACCEPT§§",
                           "Sec-Fetch-Site: same-origin",
                           "Sec-Fetch-Mode: navigate",
                           "Sec-Fetch-User: ?1",
                           "Sec-Fetch-Dest: document",
                           "Referer: http://localhost:§§PORT§§/",
                           "Accept-Encoding: gzip, deflate, br",
                           "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /2.html HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-User: ?1",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /2.html HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Sec-Fetch-User: ?1",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /2.html HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-User: ?1",
                      "Priority: u=0, i"})
    public void anchor() throws Exception {
        String html = DOCTYPE_HTML
                + "<html><body><a id='my' href='2.html'>Click me</a></body></html>";
        html = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + (html.length()) + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String hi = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/plain\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + "Hi";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, html, hi)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());
            driver.findElement(By.id("my")).click();

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§ACCEPT§§",
                        getBrowserVersion().getHtmlAcceptHeader());
            }
            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }

    /**
     * Tests a anchor click request.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"POST /test2?h HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "Connection: keep-alive",
                      "Content-Length: 4",
                      "sec-ch-ua-platform: \"Windows\"",
                      "Cache-Control: max-age=0",
                      "sec-ch-ua: §§SEC_USER_AGENT§§",
                      "sec-ch-ua-mobile: ?0",
                      "Ping-To: http://localhost:§§PORT§§/2.html",
                      "User-Agent: §§USER_AGENT§§",
                      "Content-Type: text/ping",
                      "Ping-From: http://localhost:§§PORT§§/",
                      "Accept: */*",
                      "Origin: http://localhost:§§PORT§§",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Dest: empty",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Accept-Language: en-US,en;q=0.9",
                      "",
                      "PING"},
            EDGE = {"POST /test2?h HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "Content-Length: 4",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Cache-Control: max-age=0",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "Ping-To: http://localhost:§§PORT§§/2.html",
                    "User-Agent: §§USER_AGENT§§",
                    "Content-Type: text/ping",
                    "Ping-From: http://localhost:§§PORT§§/",
                    "Accept: */*",
                    "Origin: http://localhost:§§PORT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: no-cors",
                    "Sec-Fetch-Dest: empty",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9",
                    "",
                    "PING"},
            FF = {"GET /2.html HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Sec-Fetch-User: ?1",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /2.html HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-User: ?1",
                      "Priority: u=0, i"})
    @HtmlUnitNYI(
            CHROME = {"POST /test2?h HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "Connection: keep-alive",
                      "sec-ch-ua: §§SEC_USER_AGENT§§",
                      "sec-ch-ua-mobile: ?0",
                      "sec-ch-ua-platform: \"Windows\"",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: */*",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Dest: empty",
                      "Accept-Encoding: gzip, deflate, br",
                      "Accept-Language: en-US,en;q=0.9",
                      "Origin: http://localhost:§§PORT§§",
                      "Cache-Control: max-age=0",
                      "Ping-To: http://localhost:§§PORT§§/2.html",
                      "Ping-From: http://localhost:§§PORT§§/",
                      "Content-Length: 4",
                      "Content-Type: text/plain; charset=ISO-8859-1", // text/ping
                      "",
                      "PING"},
            EDGE = {"POST /test2?h HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: */*",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: no-cors",
                    "Sec-Fetch-Dest: empty",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9",
                    "Origin: http://localhost:§§PORT§§",
                    "Cache-Control: max-age=0",
                    "Ping-To: http://localhost:§§PORT§§/2.html",
                    "Ping-From: http://localhost:§§PORT§§/",
                    "Content-Length: 4",
                    "Content-Type: text/plain; charset=ISO-8859-1", // text/ping
                    "",
                    "PING"},
            FF = {"GET /2.html HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Sec-Fetch-User: ?1",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /2.html HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-User: ?1",
                      "Priority: u=0, i"})
    public void anchorPing() throws Exception {
        String html = DOCTYPE_HTML
                + "<html><body><a id='my' href='2.html' ping='test2?h'>Click me</a></body></html>";
        html = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + (html.length()) + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String hi = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/plain\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + "Hi";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, html, hi)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());
            driver.findElement(By.id("my")).click();

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§ACCEPT§§",
                        getBrowserVersion().getHtmlAcceptHeader());
            }
            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }

    /**
     * Tests a location href change.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"GET /foo HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "Connection: keep-alive",
                      "sec-ch-ua: §§SEC_USER_AGENT§§",
                      "sec-ch-ua-mobile: ?0",
                      "sec-ch-ua-platform: \"Windows\"",
                      "Upgrade-Insecure-Requests: 1",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Dest: document",
                      "Referer: http://localhost:§§PORT§§/",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /foo HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /foo HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /foo HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=0, i"})
    @HtmlUnitNYI(CHROME = {"GET /foo HTTP/1.1",
                           "Host: localhost:§§PORT§§",
                           "Connection: keep-alive",
                           "sec-ch-ua: §§SEC_USER_AGENT§§",
                           "sec-ch-ua-mobile: ?0",
                           "sec-ch-ua-platform: \"Windows\"",
                           "Upgrade-Insecure-Requests: 1",
                           "User-Agent: §§USER_AGENT§§",
                           "Accept: §§ACCEPT§§",
                           "Sec-Fetch-Site: same-origin",
                           "Sec-Fetch-Mode: navigate",
                           "Sec-Fetch-Dest: document",
                           "Referer: http://localhost:§§PORT§§/",
                           "Accept-Encoding: gzip, deflate, br",
                           "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /foo HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /foo HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /foo HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=0, i"})
    public void locationSetHref() throws Exception {
        final String url = "http://localhost:" + WebTestCase.PORT_PRIMITIVE_SERVER;
        String html = DOCTYPE_HTML
                + "<html><body><script>location.href='" + url + "/foo';</script></body></html>";
        html = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + (html.length()) + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String hi = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/plain\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + "Hi";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, html, hi)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§ACCEPT§§",
                        getBrowserVersion().getHtmlAcceptHeader());
            }
            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }

    /**
     * Tests a location href change.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"GET /?newSearch HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "Connection: keep-alive",
                      "sec-ch-ua: §§SEC_USER_AGENT§§",
                      "sec-ch-ua-mobile: ?0",
                      "sec-ch-ua-platform: \"Windows\"",
                      "Upgrade-Insecure-Requests: 1",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Dest: document",
                      "Referer: http://localhost:§§PORT§§/",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /?newSearch HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /?newSearch HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /?newSearch HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=0, i"})
    @HtmlUnitNYI(CHROME = {"GET /?newSearch HTTP/1.1",
                           "Host: localhost:§§PORT§§",
                           "Connection: keep-alive",
                           "sec-ch-ua: §§SEC_USER_AGENT§§",
                           "sec-ch-ua-mobile: ?0",
                           "sec-ch-ua-platform: \"Windows\"",
                           "Upgrade-Insecure-Requests: 1",
                           "User-Agent: §§USER_AGENT§§",
                           "Accept: §§ACCEPT§§",
                           "Sec-Fetch-Site: same-origin",
                           "Sec-Fetch-Mode: navigate",
                           "Sec-Fetch-Dest: document",
                           "Referer: http://localhost:§§PORT§§/",
                           "Accept-Encoding: gzip, deflate, br",
                           "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /?newSearch HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /?newSearch HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /?newSearch HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=0, i"})
    public void locationSetSearch() throws Exception {
        String html = DOCTYPE_HTML
                + "<html><body><script>location.search='newSearch';</script></body></html>";
        html = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + (html.length()) + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String hi = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/plain\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + "Hi";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, html, hi)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§ACCEPT§§",
                        getBrowserVersion().getHtmlAcceptHeader());
            }
            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"GET /script.js HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "Connection: keep-alive",
                      "sec-ch-ua-platform: \"Windows\"",
                      "User-Agent: §§USER_AGENT§§",
                      "sec-ch-ua: §§SEC_USER_AGENT§§",
                      "sec-ch-ua-mobile: ?0",
                      "Accept: §§ACCEPT§§",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Dest: script",
                      "Referer: http://localhost:§§PORT§§/",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /script.js HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua-platform: \"Windows\"",
                    "User-Agent: §§USER_AGENT§§",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: no-cors",
                    "Sec-Fetch-Dest: script",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /script.js HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Sec-Fetch-Dest: script",
                  "Sec-Fetch-Mode: no-cors",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=2"},
            FF_ESR = {"GET /script.js HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Sec-Fetch-Dest: script",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=2"})
    @HtmlUnitNYI(CHROME = {"GET /script.js HTTP/1.1",
                           "Host: localhost:§§PORT§§",
                           "Connection: keep-alive",
                           "sec-ch-ua: §§SEC_USER_AGENT§§",
                           "sec-ch-ua-mobile: ?0",
                           "sec-ch-ua-platform: \"Windows\"",
                           "User-Agent: §§USER_AGENT§§",
                           "Accept: §§ACCEPT§§",
                           "Sec-Fetch-Site: same-origin",
                           "Sec-Fetch-Mode: no-cors",
                           "Sec-Fetch-Dest: script",
                           "Referer: http://localhost:§§PORT§§/",
                           "Accept-Encoding: gzip, deflate, br",
                           "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /script.js HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: no-cors",
                    "Sec-Fetch-Dest: script",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /script.js HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Sec-Fetch-Dest: script",
                  "Sec-Fetch-Mode: no-cors",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /script.js HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Sec-Fetch-Dest: script",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=0, i"})
    public void loadJavascript() throws Exception {
        String html = DOCTYPE_HTML
                + "<html><head> <script src=\"script.js\"></script> </head><body></body></html>";
        html = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + (html.length()) + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String hi = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/javascript\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + ";;";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, html, hi)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§ACCEPT§§",
                        getBrowserVersion().getScriptAcceptHeader());
            }
            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"GET /script.js?x=%CE%D2%CA%C7%CE%D2%B5%C4%20?%20Abc HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "Connection: keep-alive",
                      "sec-ch-ua-platform: \"Windows\"",
                      "User-Agent: §§USER_AGENT§§",
                      "sec-ch-ua: §§SEC_USER_AGENT§§",
                      "sec-ch-ua-mobile: ?0",
                      "Accept: §§ACCEPT§§",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Dest: script",
                      "Referer: http://localhost:§§PORT§§/",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /script.js?x=%CE%D2%CA%C7%CE%D2%B5%C4%20?%20Abc HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua-platform: \"Windows\"",
                    "User-Agent: §§USER_AGENT§§",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: no-cors",
                    "Sec-Fetch-Dest: script",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /script.js?x=%CE%D2%CA%C7%CE%D2%B5%C4%20?%20Abc HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Sec-Fetch-Dest: script",
                  "Sec-Fetch-Mode: no-cors",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=2"},
            FF_ESR = {"GET /script.js?x=%CE%D2%CA%C7%CE%D2%B5%C4%20?%20Abc HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Sec-Fetch-Dest: script",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=2"})
    @HtmlUnitNYI(CHROME = {"GET /script.js?x=%CE%D2%CA%C7%CE%D2%B5%C4%20?%20Abc HTTP/1.1",
                           "Host: localhost:§§PORT§§",
                           "Connection: keep-alive",
                           "sec-ch-ua: §§SEC_USER_AGENT§§",
                           "sec-ch-ua-mobile: ?0",
                           "sec-ch-ua-platform: \"Windows\"",
                           "User-Agent: §§USER_AGENT§§",
                           "Accept: §§ACCEPT§§",
                           "Sec-Fetch-Site: same-origin",
                           "Sec-Fetch-Mode: no-cors",
                           "Sec-Fetch-Dest: script",
                           "Referer: http://localhost:§§PORT§§/",
                           "Accept-Encoding: gzip, deflate, br",
                           "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /script.js?x=%CE%D2%CA%C7%CE%D2%B5%C4%20?%20Abc HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: no-cors",
                    "Sec-Fetch-Dest: script",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /script.js?x=%CE%D2%CA%C7%CE%D2%B5%C4%20?%20Abc HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Sec-Fetch-Dest: script",
                  "Sec-Fetch-Mode: no-cors",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /script.js?x=%CE%D2%CA%C7%CE%D2%B5%C4%20?%20Abc HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Sec-Fetch-Dest: script",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=0, i"})
    // this fails on our CI but I have no idea why
    // seems like the request for downloading the script never reaches the
    // PrimitiveWebServer
    @DisabledOnOs(OS.LINUX)
    public void loadJavascriptCharset() throws Exception {
        String html = DOCTYPE_HTML
                + "<html><head>"
                + "<meta http-equiv='Content-Type' content='text/html; charset=GB2312'>"
                + "<script src=\"script.js?x=\u6211\u662F\u6211\u7684 \u4eb8 Abc\"></script>"
                + "</head><body></body></html>";
        html = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + (html.length()) + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String hi = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 0\r\n"
                + "Content-Type: text/javascript\r\n"
                + "Connection: close\r\n"
                + "\r\n";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(Charset.forName("GB2312"), html, hi)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§ACCEPT§§",
                        getBrowserVersion().getScriptAcceptHeader());
            }

            // let's try some wait on our CI server
            final long endTime = System.currentTimeMillis() + Duration.ofSeconds(4).toMillis();
            while (primitiveWebServer.getRequests().isEmpty()
                        && System.currentTimeMillis() < endTime) {
                Thread.sleep(100);
            }

            if (primitiveWebServer.getRequests().size() < 2) {
                Assertions.fail("Still no request / request count:" + primitiveWebServer.getRequests().size());
            }

            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }

    /**
     * Tests the Sec-Fetch-* headers sent for an &lt;img&gt; request.
     * Real browsers: Sec-Fetch-Mode: no-cors, Sec-Fetch-Dest: image, no Sec-Fetch-User
     * (the image was not requested by direct user activation).
     * HtmlUnit currently hardcodes Sec-Fetch-Mode: navigate, Sec-Fetch-Dest: document
     * and always adds Sec-Fetch-User: ?1.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"GET /image.png HTTP/1.1",
                       "Host: localhost:§§PORT§§",
                       "Connection: keep-alive",
                       "sec-ch-ua-platform: \"Windows\"",
                       "User-Agent: §§USER_AGENT§§",
                       "sec-ch-ua: §§SEC_USER_AGENT§§",
                       "sec-ch-ua-mobile: ?0",
                       "Accept: image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8",
                       "Sec-Fetch-Site: same-origin",
                       "Sec-Fetch-Mode: no-cors",
                       "Sec-Fetch-Dest: image",
                       "Referer: http://localhost:§§PORT§§/",
                       "Accept-Encoding: gzip, deflate, br, zstd",
                       "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /image.png HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua-platform: \"Windows\"",
                    "User-Agent: §§USER_AGENT§§",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "Accept: image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: no-cors",
                    "Sec-Fetch-Dest: image",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /image.png HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Sec-Fetch-Dest: image",
                  "Sec-Fetch-Mode: no-cors",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=4, i"},
            FF_ESR = {"GET /image.png HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Sec-Fetch-Dest: image",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=4, i"})
    @HtmlUnitNYI(
            CHROME = {"GET /image.png HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "Connection: keep-alive",
                      "sec-ch-ua: §§SEC_USER_AGENT§§",
                      "sec-ch-ua-mobile: ?0",
                      "sec-ch-ua-platform: \"Windows\"",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Dest: image",
                      "Referer: http://localhost:§§PORT§§/",
                      "Accept-Encoding: gzip, deflate, br",
                      "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /image.png HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: no-cors",
                    "Sec-Fetch-Dest: image",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /image.png HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Sec-Fetch-Dest: image",
                  "Sec-Fetch-Mode: no-cors",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /image.png HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Sec-Fetch-Dest: image",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=0, i"})
    public void image() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head><body><img src='image.png'></body></html>";
        final String htmlResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + html.length() + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String imageResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: image/png\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + "Hi";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, htmlResponse, imageResponse)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());

            // force image download in htmlunit
            driver.findElement(By.tagName("img")).getAttribute("height");

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
            }
            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }

    /**
     * Tests the Sec-Fetch-* headers sent for a &lt;link rel="stylesheet"&gt; request.
     * Real browsers: Sec-Fetch-Mode: no-cors, Sec-Fetch-Dest: style, no Sec-Fetch-User.
     * HtmlUnit currently hardcodes Sec-Fetch-Mode: navigate, Sec-Fetch-Dest: document
     * and always adds Sec-Fetch-User: ?1.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"GET /style.css HTTP/1.1",
                       "Host: localhost:§§PORT§§",
                       "Connection: keep-alive",
                       "sec-ch-ua-platform: \"Windows\"",
                       "User-Agent: §§USER_AGENT§§",
                       "sec-ch-ua: §§SEC_USER_AGENT§§",
                       "sec-ch-ua-mobile: ?0",
                       "Accept: text/css,*/*;q=0.1",
                       "Sec-Fetch-Site: same-origin",
                       "Sec-Fetch-Mode: no-cors",
                       "Sec-Fetch-Dest: style",
                       "Referer: http://localhost:§§PORT§§/",
                       "Accept-Encoding: gzip, deflate, br, zstd",
                       "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /style.css HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua-platform: \"Windows\"",
                    "User-Agent: §§USER_AGENT§§",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "Accept: text/css,*/*;q=0.1",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: no-cors",
                    "Sec-Fetch-Dest: style",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /style.css HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: text/css,*/*;q=0.1",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Sec-Fetch-Dest: style",
                  "Sec-Fetch-Mode: no-cors",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=2"},
            FF_ESR = {"GET /style.css HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: text/css,*/*;q=0.1",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Sec-Fetch-Dest: style",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=2"})
    @HtmlUnitNYI(CHROME = {"GET /style.css HTTP/1.1",
                           "Host: localhost:§§PORT§§",
                           "Connection: keep-alive",
                           "sec-ch-ua: §§SEC_USER_AGENT§§",
                           "sec-ch-ua-mobile: ?0",
                           "sec-ch-ua-platform: \"Windows\"",
                           "User-Agent: §§USER_AGENT§§",
                           "Accept: text/css,*/*;q=0.1",
                           "Sec-Fetch-Site: same-origin",
                           "Sec-Fetch-Mode: no-cors",
                           "Sec-Fetch-Dest: style",
                           "Referer: http://localhost:§§PORT§§/",
                           "Accept-Encoding: gzip, deflate, br",
                           "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /style.css HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: text/css,*/*;q=0.1",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: no-cors",
                    "Sec-Fetch-Dest: style",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /style.css HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: text/css,*/*;q=0.1",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Sec-Fetch-Dest: style",
                  "Sec-Fetch-Mode: no-cors",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /style.css HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: text/css,*/*;q=0.1",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Sec-Fetch-Dest: style",
                      "Sec-Fetch-Mode: no-cors",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=0, i"})
    public void stylesheet() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><link rel='stylesheet' href='style.css'></head><body></body></html>";
        final String htmlResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + html.length() + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String cssResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 0\r\n"
                + "Content-Type: text/css\r\n"
                + "Connection: close\r\n"
                + "\r\n";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, htmlResponse, cssResponse)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
            }
            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }

    /**
     * Tests the Sec-Fetch-* headers sent for a same-origin XMLHttpRequest.
     * Real browsers: Sec-Fetch-Mode: cors, Sec-Fetch-Dest: empty, no Sec-Fetch-User.
     * HtmlUnit currently hardcodes Sec-Fetch-Mode: navigate, Sec-Fetch-Dest: document
     * and always adds Sec-Fetch-User: ?1.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"GET /ajax.json HTTP/1.1",
                       "Host: localhost:§§PORT§§",
                       "Connection: keep-alive",
                       "sec-ch-ua-platform: \"Windows\"",
                       "User-Agent: §§USER_AGENT§§",
                       "sec-ch-ua: §§SEC_USER_AGENT§§",
                       "sec-ch-ua-mobile: ?0",
                       "Accept: */*",
                       "Sec-Fetch-Site: same-origin",
                       "Sec-Fetch-Mode: cors",
                       "Sec-Fetch-Dest: empty",
                       "Referer: http://localhost:§§PORT§§/",
                       "Accept-Encoding: gzip, deflate, br, zstd",
                       "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /ajax.json HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua-platform: \"Windows\"",
                    "User-Agent: §§USER_AGENT§§",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "Accept: */*",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: cors",
                    "Sec-Fetch-Dest: empty",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /ajax.json HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: */*",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Sec-Fetch-Dest: empty",
                  "Sec-Fetch-Mode: cors",
                  "Sec-Fetch-Site: same-origin"},
            FF_ESR = {"GET /ajax.json HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: */*",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Sec-Fetch-Dest: empty",
                      "Sec-Fetch-Mode: cors",
                      "Sec-Fetch-Site: same-origin"})
    @HtmlUnitNYI(
            CHROME = {"GET /ajax.json HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "Connection: keep-alive",
                      "sec-ch-ua: §§SEC_USER_AGENT§§",
                      "sec-ch-ua-mobile: ?0",
                      "sec-ch-ua-platform: \"Windows\"",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: */*",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-Mode: cors",
                      "Sec-Fetch-Dest: empty",
                      "Referer: http://localhost:§§PORT§§/",
                      "Accept-Encoding: gzip, deflate, br",
                      "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /ajax.json HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: */*",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: cors",
                    "Sec-Fetch-Dest: empty",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /ajax.json HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: */*",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Sec-Fetch-Dest: empty",
                  "Sec-Fetch-Mode: cors",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /ajax.json HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: */*",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Sec-Fetch-Dest: empty",
                      "Sec-Fetch-Mode: cors",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=0, i"})
    public void xmlHttpRequestGet() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + "  function doAjax() {\n"
                + "    var x = new XMLHttpRequest();\n"
                + "    x.open('GET', 'ajax.json', true);\n"
                + "    x.send();\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='doAjax()'></body></html>";
        final String htmlResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + html.length() + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String ajaxResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: application/json\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + "{}";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, htmlResponse, ajaxResponse)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());

            final long endTime = System.currentTimeMillis() + Duration.ofSeconds(4).toMillis();
            while (primitiveWebServer.getRequests().size() < 2
                        && System.currentTimeMillis() < endTime) {
                Thread.sleep(100);
            }

            if (primitiveWebServer.getRequests().size() < 2) {
                Assertions.fail("Still no request / request count:" + primitiveWebServer.getRequests().size());
            }

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
            }
            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }

    /**
     * Tests a form submitted purely by script ({@code form.submit()}), as opposed to via
     * a click on a submit control. Real browsers do not consider this a user-activated
     * navigation, so no Sec-Fetch-User header is sent at all (never {@code ?0}).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"GET /foo?text1=me HTTP/1.1",
                       "Host: localhost:§§PORT§§",
                       "Connection: keep-alive",
                       "sec-ch-ua: §§SEC_USER_AGENT§§",
                       "sec-ch-ua-mobile: ?0",
                       "sec-ch-ua-platform: \"Windows\"",
                       "Upgrade-Insecure-Requests: 1",
                       "User-Agent: §§USER_AGENT§§",
                       "Accept: §§ACCEPT§§",
                       "Sec-Fetch-Site: same-origin",
                       "Sec-Fetch-Mode: navigate",
                       "Sec-Fetch-Dest: document",
                       "Referer: http://localhost:§§PORT§§/",
                       "Accept-Encoding: gzip, deflate, br, zstd",
                       "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /foo?text1=me HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /foo?text1=me HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /foo?text1=me HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=0, i"})
    @HtmlUnitNYI(
            CHROME = {"GET /foo?text1=me HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9"},
             EDGE = {"GET /foo?text1=me HTTP/1.1",
                     "Host: localhost:§§PORT§§",
                     "Connection: keep-alive",
                     "sec-ch-ua: §§SEC_USER_AGENT§§",
                     "sec-ch-ua-mobile: ?0",
                     "sec-ch-ua-platform: \"Windows\"",
                     "Upgrade-Insecure-Requests: 1",
                     "User-Agent: §§USER_AGENT§§",
                     "Accept: §§ACCEPT§§",
                     "Sec-Fetch-Site: same-origin",
                     "Sec-Fetch-Mode: navigate",
                     "Sec-Fetch-Dest: document",
                     "Referer: http://localhost:§§PORT§§/",
                     "Accept-Encoding: gzip, deflate, br",
                     "Accept-Language: en-US,en;q=0.9"},
             FF = {"GET /foo?text1=me HTTP/1.1",
                   "Host: localhost:§§PORT§§",
                   "User-Agent: §§USER_AGENT§§",
                   "Accept: §§ACCEPT§§",
                   "Accept-Language: en-US,en;q=0.9",
                   "Accept-Encoding: gzip, deflate, br",
                   "Connection: keep-alive",
                   "Referer: http://localhost:§§PORT§§/",
                   "Upgrade-Insecure-Requests: 1",
                   "Sec-Fetch-Dest: document",
                   "Sec-Fetch-Mode: navigate",
                   "Sec-Fetch-Site: same-origin",
                   "Priority: u=0, i"},
             FF_ESR = {"GET /foo?text1=me HTTP/1.1",
                       "Host: localhost:§§PORT§§",
                       "User-Agent: §§USER_AGENT§§",
                       "Accept: §§ACCEPT§§",
                       "Accept-Language: en-US,en;q=0.5",
                       "Accept-Encoding: gzip, deflate, br",
                       "Connection: keep-alive",
                       "Referer: http://localhost:§§PORT§§/",
                       "Upgrade-Insecure-Requests: 1",
                       "Sec-Fetch-Dest: document",
                       "Sec-Fetch-Mode: navigate",
                       "Sec-Fetch-Site: same-origin",
                       "Priority: u=0, i"})
    public void formSubmitFromScript() throws Exception {
        String html = DOCTYPE_HTML
            + "<html><body><form id='f' action='foo' method='get' accept-charset='iso-8859-1'>\n"
            + "<input name='text1' value='me'>\n"
            + "</form>\n"
            + "<script>document.getElementById('f').submit();</script>\n"
            + "</body></html>";
        html = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + (html.length()) + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String hi = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/plain\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + "Hi";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, html, hi)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§ACCEPT§§",
                        getBrowserVersion().getHtmlAcceptHeader());
            }
            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }

    /**
     * Tests a link "clicked" purely by script ({@code anchor.click()}), as opposed to a
     * real (WebDriver-simulated) click. Real browsers do not consider this a user-activated
     * navigation, so no Sec-Fetch-User header is sent at all.
     * <p>
     * Unlike {@link #formSubmitFromScript()}, HtmlAnchor#doClickStateUpdate() currently has
     * no way to distinguish this from a real click, so it still (incorrectly) sends
     * Sec-Fetch-User: ?1 - see the {@code TODO} in HtmlAnchor for the missing plumbing.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"GET /2.html HTTP/1.1",
                       "Host: localhost:§§PORT§§",
                       "Connection: keep-alive",
                       "sec-ch-ua: §§SEC_USER_AGENT§§",
                       "sec-ch-ua-mobile: ?0",
                       "sec-ch-ua-platform: \"Windows\"",
                       "Upgrade-Insecure-Requests: 1",
                       "User-Agent: §§USER_AGENT§§",
                       "Accept: §§ACCEPT§§",
                       "Sec-Fetch-Site: same-origin",
                       "Sec-Fetch-Mode: navigate",
                       "Sec-Fetch-Dest: document",
                       "Referer: http://localhost:§§PORT§§/",
                       "Accept-Encoding: gzip, deflate, br, zstd",
                       "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /2.html HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br, zstd",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /2.html HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br, zstd",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Priority: u=0, i"},
            FF_ESR = {"GET /2.html HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br, zstd",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Priority: u=0, i"})
    @HtmlUnitNYI(CHROME = {"GET /2.html HTTP/1.1",
                           "Host: localhost:§§PORT§§",
                           "Connection: keep-alive",
                           "sec-ch-ua: §§SEC_USER_AGENT§§",
                           "sec-ch-ua-mobile: ?0",
                           "sec-ch-ua-platform: \"Windows\"",
                           "Upgrade-Insecure-Requests: 1",
                           "User-Agent: §§USER_AGENT§§",
                           "Accept: §§ACCEPT§§",
                           "Sec-Fetch-Site: same-origin",
                           "Sec-Fetch-Mode: navigate",
                           "Sec-Fetch-User: ?1", // wrong
                           "Sec-Fetch-Dest: document",
                           "Referer: http://localhost:§§PORT§§/",
                           "Accept-Encoding: gzip, deflate, br",
                           "Accept-Language: en-US,en;q=0.9"},
            EDGE = {"GET /2.html HTTP/1.1",
                    "Host: localhost:§§PORT§§",
                    "Connection: keep-alive",
                    "sec-ch-ua: §§SEC_USER_AGENT§§",
                    "sec-ch-ua-mobile: ?0",
                    "sec-ch-ua-platform: \"Windows\"",
                    "Upgrade-Insecure-Requests: 1",
                    "User-Agent: §§USER_AGENT§§",
                    "Accept: §§ACCEPT§§",
                    "Sec-Fetch-Site: same-origin",
                    "Sec-Fetch-Mode: navigate",
                    "Sec-Fetch-User: ?1", // wrong
                    "Sec-Fetch-Dest: document",
                    "Referer: http://localhost:§§PORT§§/",
                    "Accept-Encoding: gzip, deflate, br",
                    "Accept-Language: en-US,en;q=0.9"},
            FF = {"GET /2.html HTTP/1.1",
                  "Host: localhost:§§PORT§§",
                  "User-Agent: §§USER_AGENT§§",
                  "Accept: §§ACCEPT§§",
                  "Accept-Language: en-US,en;q=0.9",
                  "Accept-Encoding: gzip, deflate, br",
                  "Connection: keep-alive",
                  "Referer: http://localhost:§§PORT§§/",
                  "Upgrade-Insecure-Requests: 1",
                  "Sec-Fetch-Dest: document",
                  "Sec-Fetch-Mode: navigate",
                  "Sec-Fetch-Site: same-origin",
                  "Sec-Fetch-User: ?1", // wrong
                  "Priority: u=0, i"},
            FF_ESR = {"GET /2.html HTTP/1.1",
                      "Host: localhost:§§PORT§§",
                      "User-Agent: §§USER_AGENT§§",
                      "Accept: §§ACCEPT§§",
                      "Accept-Language: en-US,en;q=0.5",
                      "Accept-Encoding: gzip, deflate, br",
                      "Connection: keep-alive",
                      "Referer: http://localhost:§§PORT§§/",
                      "Upgrade-Insecure-Requests: 1",
                      "Sec-Fetch-Dest: document",
                      "Sec-Fetch-Mode: navigate",
                      "Sec-Fetch-Site: same-origin",
                      "Sec-Fetch-User: ?1", // wrong
                      "Priority: u=0, i"})
    public void anchorClickFromScript() throws Exception {
        String html = DOCTYPE_HTML
                + "<html><body><a id='my' href='2.html'>Click me</a>\n"
                + "<script>document.getElementById('my').click();</script>\n"
                + "</body></html>";
        html = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + (html.length()) + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;
        final String hi = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/plain\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + "Hi";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, html, hi)) {
            final WebDriver driver = getWebDriver();

            driver.get("http://localhost:" + primitiveWebServer.getPort());

            final String[] expectedHeaders = getExpectedAlerts();
            for (int i = 0; i < expectedHeaders.length; i++) {
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§PORT§§", "" + primitiveWebServer.getPort());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§USER_AGENT§§",
                        getBrowserVersion().getUserAgent());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§SEC_USER_AGENT§§",
                        getBrowserVersion().getSecClientHintUserAgentHeader());
                expectedHeaders[i] = expectedHeaders[i].replaceAll("§§ACCEPT§§",
                        getBrowserVersion().getHtmlAcceptHeader());
            }
            final String request = primitiveWebServer.getRequests().get(1);
            final String[] headers = request.split("\\r\\n");
            assertEquals(Arrays.asList(expectedHeaders).toString(), Arrays.asList(headers).toString());
        }
    }
}