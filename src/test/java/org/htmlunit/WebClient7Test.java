/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
import java.nio.charset.Charset;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * Tests using the {@link PrimitiveWebServer}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class WebClient7Test extends WebDriverTestCase {

    /**
     * Test that the path and query string are encoded to be valid.
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("/test.html?a=b%20c&d=%C3%A9%C3%A8")
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
    @Alerts("/test.html?param=%C2%A9%C2%A3")
    public void loadPage_EncodeRequest7() throws Exception {
        // unicode
        testRequestUrlEncoding("test.html?param=\u00A9\u00A3");
    }

    private void testRequestUrlEncoding(final String url) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>"
                + "<head><title>foo</title></head>"
                + "<body></body></html>";

        final String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + html.length() + "\r\n"
                + "Content-Type: text/html\r\n"
                + "Connection: close\r\n"
                + "\r\n"
                + html;

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, null)) {
            final URL baseUrl = new URL("http://localhost:" + primitiveWebServer.getPort() + "/");
            final WebDriver driver = getWebDriver();

            driver.get(new URL(baseUrl, url).toString());
            String reqUrl = primitiveWebServer.getRequests().get(0);
            reqUrl = reqUrl.substring(4, reqUrl.indexOf("HTTP/1.1") - 1);

            assertEquals(getExpectedAlerts()[0], reqUrl);
        }
    }

    private void contentEncoding(final boolean header,
            final String charset,
            final String responseEncodingCharset,
            final String addHeader,
            final String addHtml) throws Exception {
        String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title>foo</title>\n";
        if (!header) {
            html += "  <meta http-equiv='Content-Type' content='text/html; charset=" + charset + "'>\n";
        }
        if (addHeader != null) {
            html += addHeader + "\n";
        }

        html += "</head>\n"
                + "<body>\n"
                + addHtml + "\n"
                + "</body></html>";

        String firstResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + html.length() + "\r\n"
                + "Content-Type: text/html";
        if (header) {
            firstResponse += "; charset=" + charset;
        }
        firstResponse += "\r\n"
                + "Connection: close\r\n"
                + "\r\n" + html;

        final String secondResponse = "HTTP/1.1 404 Not Found\r\n"
                + "Content-Length: 0\r\n"
                + "Connection: close\r\n"
                + "\r\n";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer =
                new PrimitiveWebServer(Charset.forName(responseEncodingCharset), firstResponse, secondResponse)) {
            final String url = "http://localhost:" + primitiveWebServer.getPort() + "/";
            final WebDriver driver = getWebDriver();

            driver.get(url);
            final String content = driver.findElement(By.tagName("body")).getText();
            assertEquals(getExpectedAlerts()[0], content);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.html?k%C3%B6nig")
    public void anchorUrlEncodingUTF8Header() throws Exception {
        anchorUrlEncoding(true, "UTF-8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.html?k%C3%B6nig")
    public void anchorUrlEncodingUTF8Meta() throws Exception {
        anchorUrlEncoding(false, "UTF-8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.html?k%F6nig")
    public void anchorUrlEncodingISO8859_1Header() throws Exception {
        anchorUrlEncoding(true, "ISO-8859-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.html?k%F6nig")
    public void anchorUrlEncodingISO8859_1Meta() throws Exception {
        anchorUrlEncoding(false, "ISO-8859-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.html?k?nig")
    public void anchorUrlEncodingWindows_1251Header() throws Exception {
        anchorUrlEncoding(true, "Windows-1251");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.html?k?nig")
    public void anchorUrlEncodingWindows_1251Meta() throws Exception {
        anchorUrlEncoding(false, "Windows-1251");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/area.html?k%C3%B6nig")
    @BuggyWebDriver(FF = "WebDriverException",
            FF_ESR = "WebDriverException")
    public void areaUrlEncodingUTF8Header() throws Exception {
        areaUrlEncoding(true, "UTF-8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/area.html?k%C3%B6nig")
    @BuggyWebDriver(FF = "WebDriverException",
            FF_ESR = "WebDriverException")
    public void areaUrlEncodingUTF8Meta() throws Exception {
        areaUrlEncoding(false, "UTF-8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/area.html?k%F6nig")
    @BuggyWebDriver(FF = "WebDriverException",
            FF_ESR = "WebDriverException")
    public void areaUrlEncodingISO8859_1Header() throws Exception {
        areaUrlEncoding(true, "ISO-8859-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/area.html?k%F6nig")
    @BuggyWebDriver(FF = "WebDriverException",
            FF_ESR = "WebDriverException")
    public void areaUrlEncodingISO8859_1Meta() throws Exception {
        areaUrlEncoding(false, "ISO-8859-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.gif?k%C3%B6nig")
    public void imageUrlEncodingUTF8Header() throws Exception {
        imageUrlEncoding(true, "UTF-8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.gif?k%C3%B6nig")
    public void imageUrlEncodingUTF8Meta() throws Exception {
        imageUrlEncoding(false, "UTF-8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "/test.gif?k%F6nig",
            FF = "/test.gif?k%EF%BF%BDnig",
            FF_ESR = "/test.gif?k%EF%BF%BDnig")
    @HtmlUnitNYI(FF = "/test.gif?k%F6nig",
            FF_ESR = "/test.gif?k%F6nig")
    public void imageUrlEncodingISO8859_1Header() throws Exception {
        imageUrlEncoding(true, "ISO_8859_1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "/test.gif?k%F6nig",
            FF = "/test.gif?k%EF%BF%BDnig",
            FF_ESR = "/test.gif?k%EF%BF%BDnig")
    @HtmlUnitNYI(FF = "/test.gif?k%F6nig",
            FF_ESR = "/test.gif?k%F6nig")
    public void imageUrlEncodingISO8859_1Meta() throws Exception {
        imageUrlEncoding(false, "ISO_8859_1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.css?k%C3%B6nig")
    public void linkUrlEncodingUTF8Header() throws Exception {
        linkUrlEncoding(true, "UTF-8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.css?k%C3%B6nig")
    public void linkUrlEncodingUTF8Meta() throws Exception {
        linkUrlEncoding(false, "UTF-8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "/test.css?k%F6nig",
            FF = "/test.css?k%EF%BF%BDnig",
            FF_ESR = "/test.css?k%EF%BF%BDnig")
    @HtmlUnitNYI(FF = "/test.css?k%F6nig",
            FF_ESR = "/test.css?k%F6nig")
    public void linkUrlEncodingISO8859_1Header() throws Exception {
        linkUrlEncoding(true, "ISO_8859_1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "/test.css?k%F6nig",
            FF = "/test.css?k%EF%BF%BDnig",
            FF_ESR = "/test.css?k%EF%BF%BDnig")
    @HtmlUnitNYI(FF = "/test.css?k%F6nig",
            FF_ESR = "/test.css?k%F6nig")
    public void linkUrlEncodingISO8859_1Meta() throws Exception {
        linkUrlEncoding(false, "ISO_8859_1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.html?k%C3%B6nig")
    public void iframeUrlEncodingUTF8Header() throws Exception {
        iframeUrlEncoding(true, "UTF-8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.html?k%C3%B6nig")
    public void iframeUrlEncodingUTF8Meta() throws Exception {
        iframeUrlEncoding(false, "UTF-8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.html?k%F6nig")
    public void iframeUrlEncodingISO8859_1Header() throws Exception {
        framesetUrlEncoding("ISO_8859_1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("!abcd\u20AC\u00F6\u00FF")
    public void contentEncodingAscii() throws Exception {
        contentEncoding(true, "ascii", "windows-1252", null, "!abcd\u20AC\u00F6\u00FF");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("!abcd???")
    public void contentEncodingAsciiAscii() throws Exception {
        contentEncoding(true, "ascii", "ascii", null, "!abcd\u20AC\u00F6\u00FF");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("!abcd\u20AC\u00F6\u00FF")
    public void contentEncodingXUserDefined() throws Exception {
        contentEncoding(false, "x-user-defined", "windows-1252", null, "!abcd\u20AC\u00F6\u00FF");
    }

    private void anchorUrlEncoding(final boolean header, final String charset) throws Exception {
        urlEncoding(header, charset,
                null,
                "  <a id='myLink' href='test.html?k\u00F6nig'>Click me</a>",
                true);
    }

    private void areaUrlEncoding(final boolean header, final String charset) throws Exception {
        urlEncoding(header, charset,
                null,
                "  <img id='myImg' usemap='#dot' width='100' height='100'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map name='dot'>\n"
                + "    <area id='myLink' shape='rect' coords='0,0,42,42' href='area.html?k\u00F6nig'/>\n"
                + "  <map>\n",
                true);
    }

    private void imageUrlEncoding(final boolean header, final String charset) throws Exception {
        urlEncoding(header, charset,
                null,
                "  <img id='myImg' src='test.gif?k\u00F6nig'>"
                + "  <button id='myLink' onClick='document.getElementById(\"myImg\").width'></button>",
                true);
    }

    private void linkUrlEncoding(final boolean header, final String charset) throws Exception {
        urlEncoding(header, charset,
                "  <link rel='stylesheet' type='text/css' href='test.css?k\u00F6nig'>",
                "",
                false);
    }

    private void iframeUrlEncoding(final boolean header, final String charset) throws Exception {
        urlEncoding(header, charset,
                "  <iframe src='test.html?k\u00F6nig'></iframe> ",
                "",
                false);
    }

    private void urlEncoding(final boolean header, final String charset,
            final String addHeader,
            final String addHtml,
            final boolean click) throws Exception {
        String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title>foo</title>\n";
        if (!header) {
            html += "  <meta http-equiv='Content-Type' content='text/html; charset=" + charset + "'>\n";
        }
        if (addHeader != null) {
            html += addHeader + "\n";
        }

        html += "</head>\n"
                + "<body>\n"
                + addHtml + "\n"
                + "</body></html>";

        String firstResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + html.length() + "\r\n"
                + "Content-Type: text/html";
        if (header) {
            firstResponse += "; charset=" + charset;
        }
        firstResponse += "\r\n"
                + "Connection: close\r\n"
                + "\r\n" + html;

        final String secondResponse = "HTTP/1.1 404 Not Found\r\n"
                + "Content-Length: 0\r\n"
                + "Connection: close\r\n"
                + "\r\n";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer =
                new PrimitiveWebServer(Charset.forName(charset), firstResponse, secondResponse)) {
            final String url = "http://localhost:" + primitiveWebServer.getPort() + "/";
            final WebDriver driver = getWebDriver();

            driver.get(url);
            try {
                if (click) {
                    driver.findElement(By.id("myLink")).click();
                }

                String reqUrl = primitiveWebServer.getRequests().get(1);
                reqUrl = reqUrl.substring(4, reqUrl.indexOf("HTTP/1.1") - 1);

                assertEquals(getExpectedAlerts()[0], reqUrl);
            }
            catch (final WebDriverException e) {
                assertEquals(getExpectedAlerts()[0], "WebDriverException");
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.html?k%C3%B6nig")
    public void framesetUrlEncodingUTF8() throws Exception {
        framesetUrlEncoding("UTF-8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/test.html?k%F6nig")
    public void framesetUrlEncodingISO8859_1() throws Exception {
        framesetUrlEncoding("ISO_8859_1");
    }

    private void framesetUrlEncoding(final String charset) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<frameset><frame src='test.html?k\u00F6nig'></frameset>\n"
                + "</html>";

        final String firstResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: " + html.length() + "\r\n"
                + "Content-Type: text/html; charset=" + charset + "\r\n"
                + "Connection: close\r\n"
                + "\r\n" + html;

        final String secondResponse = "HTTP/1.1 404 Not Found\r\n"
                + "Content-Length: 0\r\n"
                + "Connection: close\r\n"
                + "\r\n";

        shutDownAll();
        try (PrimitiveWebServer primitiveWebServer =
                new PrimitiveWebServer(Charset.forName(charset), firstResponse, secondResponse)) {
            final String url = "http://localhost:" + primitiveWebServer.getPort() + "/";
            final WebDriver driver = getWebDriver();

            driver.get(url);

            String reqUrl = primitiveWebServer.getRequests().get(1);
            reqUrl = reqUrl.substring(4, reqUrl.indexOf("HTTP/1.1") - 1);

            assertEquals(getExpectedAlerts()[0], reqUrl);
        }
    }
}
