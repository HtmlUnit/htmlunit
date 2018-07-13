/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests using the {@link PrimitiveWebServer}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WebClient7Test extends WebDriverTestCase {

    private PrimitiveWebServer primitiveWebServer_;

    /**
     * @throws Exception if an error occurs
     */
    @After
    public void stopServer() throws Exception {
        if (primitiveWebServer_ != null) {
            primitiveWebServer_.stop();
        }
        shutDownAll();
    }

    /**
     * Test that the path and query string are encoded to be valid.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "/test.html?a=b%20c&d=%C3%A9%C3%A8",
            IE = "/test.html?a=b%20c&d=\u00E9\u00E8")
    @NotYetImplemented(IE)
    public void loadPage_EncodeRequest() throws Exception {
        // with query string not encoded
        testRequestUrlEncoding("test.html?a=b c&d=\u00E9\u00E8");
    }

    /**
     * Test that the path and query string are encoded to be valid.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("/test.html?a=b%20c&d=%C3%A9%C3%A8")
    public void loadPage_EncodeRequest2() throws Exception {
        // with query string already encoded
        testRequestUrlEncoding("test.html?a=b%20c&d=%C3%A9%C3%A8");
    }

    /**
     * Test that the path and query string are encoded to be valid.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("/test.html?a=b%20c&d=e%20f")
    public void loadPage_EncodeRequest3() throws Exception {
        // with query string partially encoded
        testRequestUrlEncoding("test.html?a=b%20c&d=e f");
    }

    /**
     * Test that the path and query string are encoded to be valid.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("/test.html?a=b%20c")
    public void loadPage_EncodeRequest4() throws Exception {
        // with anchor
        testRequestUrlEncoding("test.html?a=b c#myAnchor");
    }

    /**
     * Test that the path and query string are encoded to be valid.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("/test.html?a=%26%3D%20%2C%24")
    public void loadPage_EncodeRequest5() throws Exception {
        // with query string containing encoded "&", "=", "+", ",", and "$"
        testRequestUrlEncoding("test.html?a=%26%3D%20%2C%24");
    }

    /**
     * Test that the path and query string are encoded to be valid.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("/page%201.html")
    public void loadPage_EncodeRequest6() throws Exception {
        // with character to encode in path
        testRequestUrlEncoding("page 1.html");
    }

    /**
     * Test that the path and query string are encoded to be valid.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "/test.html?param=%C2%A9%C2%A3",
            IE = "/test.html?param=\u00A9\u00A3")
    @NotYetImplemented(IE)
    public void loadPage_EncodeRequest7() throws Exception {
        // unicode
        testRequestUrlEncoding("test.html?param=\u00A9\u00A3");
    }

    private void testRequestUrlEncoding(final String url) throws Exception {
        final String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 58\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<html><head><title>foo</title></head><body>"
                + "</body></html>";

        primitiveWebServer_ = new PrimitiveWebServer(PORT, response);
        primitiveWebServer_.start();

        final WebDriver driver = getWebDriver();

        driver.get(new URL(URL_FIRST, url).toString());
        String reqUrl = primitiveWebServer_.getRequests().get(0);
        if (reqUrl.contains("/favicon.ico")) {
            reqUrl = primitiveWebServer_.getRequests().get(1);
        }
        reqUrl = reqUrl.substring(4, reqUrl.indexOf("HTTP/1.1") - 1);

        assertEquals(getExpectedAlerts()[0], reqUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "/bug.html?k%C3%B6nig",
            IE = "/bug.html?k\u00c3\u00b6nig")
    @NotYetImplemented(IE)
    public void linkUrlEncodingUTF8() throws Exception {
        final String html = "<html>\n"
                + "<head><title>foo</title>\n"
                + "  <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <a id='myLink' href='bug.html?k\u00F6nig'>Click me</a>\n"
                + "</body></html>";

        final String firstResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + html.length() + "\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + html;

        final String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<html><head><title>foo</title></head><body>"
                + "</body></html>";

        primitiveWebServer_ = new PrimitiveWebServer(PORT, firstResponse, response);
        primitiveWebServer_.start();

        final WebDriver driver = getWebDriver();

        driver.get(URL_FIRST.toString());
        driver.findElement(By.id("myLink")).click();

        String reqUrl = primitiveWebServer_.getRequests().get(1);
        if (reqUrl.contains("/favicon.ico")) {
            reqUrl = primitiveWebServer_.getRequests().get(2);
        }
        reqUrl = reqUrl.substring(4, reqUrl.indexOf("HTTP/1.1") - 1);

        assertEquals(getExpectedAlerts()[0], reqUrl);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "/bug.html?k%F6nig",
            IE = "/bug.html?k\u00f6nig")
    @NotYetImplemented(IE)
    public void linkUrlEncodingISO8859_1() throws Exception {
        final String html = "<html>\n"
                + "<head><title>foo</title>\n"
                + "  <meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <a id='myLink' href='bug.html?k\u00F6nig'>Click me</a>\n"
                + "</body></html>";

        final String firstResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + html.length() + "\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + html;

        final String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<html><head><title>foo</title></head><body>"
                + "</body></html>";

        primitiveWebServer_ = new PrimitiveWebServer(PORT, firstResponse, response);
        primitiveWebServer_.setCharset(StandardCharsets.ISO_8859_1);
        primitiveWebServer_.start();

        final WebDriver driver = getWebDriver();

        driver.get(URL_FIRST.toString());
        driver.findElement(By.id("myLink")).click();

        String reqUrl = primitiveWebServer_.getRequests().get(1);
        if (reqUrl.contains("/favicon.ico")) {
            reqUrl = primitiveWebServer_.getRequests().get(2);
        }
        reqUrl = reqUrl.substring(4, reqUrl.indexOf("HTTP/1.1") - 1);

        assertEquals(getExpectedAlerts()[0], reqUrl);
    }
}
