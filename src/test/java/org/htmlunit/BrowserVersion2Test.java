/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.http.HttpHeader;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Unit tests for {@link BrowserVersion}.
 * For some details on IE view you can have a look at
 * http://blogs.msdn.com/b/ieinternals/archive/2009/07/01/ie-and-the-accept-header.aspx
 *
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class BrowserVersion2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,"
                    + "image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
            FF = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            FF_ESR = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
    public void acceptHeaderGetUrl() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><body>Response</body></html>";
        loadPage2(html);

        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "Accept: text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;"
                    + "q=0.8,application/signed-exchange;v=b3;q=0.7"},
            FF = {"2", "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"},
            FF_ESR = {"2", "Accept: text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"})
    public void acceptHeaderWindowOpen() throws Exception {
        String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><body>Response</body></html>";
        getMockWebConnection().setDefaultResponse(html);

        html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head><title>First</title></head>\n"
                + "<body>\n"
                + "  <a id='clickme' href='javascript: window.open(\"" + URL_SECOND + "\")'>Click me</a>\n"
                + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickme")).click();
        // because real browsers are doing the open async, we have to be a bit patient
        Thread.sleep(DEFAULT_WAIT_TIME);

        assertEquals(getExpectedAlerts()[0], Integer.toString(getMockWebConnection().getRequestCount()));
        assertEquals(getExpectedAlerts()[1], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "Accept: text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;"
                    + "q=0.8,application/signed-exchange;v=b3;q=0.7"},
            FF = {"2", "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"},
            FF_ESR = {"2", "Accept: text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"})
    public void acceptHeaderAnchorClick() throws Exception {
        String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><body>Response</body></html>";
        getMockWebConnection().setDefaultResponse(html);

        html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head><title>First</title></head>\n"
                + "<body>\n"
                + "  <a id='clickme' href='test.html'>Click me</a>\n"
                + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickme")).click();

        assertEquals(getExpectedAlerts()[0], Integer.toString(getMockWebConnection().getRequestCount()));
        assertEquals(getExpectedAlerts()[1], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: text/html,application/xhtml+xml,application/xml;"
                    + "q=0.9,image/avif,image/webp,image/apng,*/*;"
                    + "q=0.8,application/signed-exchange;v=b3;q=0.7",
            FF = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            FF_ESR = "Accept: text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
    public void acceptHeaderAnchorClickWithType() throws Exception {
        String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><body>Response</body></html>";
        getMockWebConnection().setDefaultResponse(html);

        html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head><title>First</title></head>\n"
                + "<body>\n"
                + "  <a id='clickme' href='test.html' type='text/plain'>Click me</a>\n"
                + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickme")).click();

        // we have to be a bit patient
        Thread.sleep(100);

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8",
            FF = "Accept: image/avif,image/webp,*/*",
            FF_ESR = "Accept: image/avif,image/webp,*/*")
    public void acceptHeaderImage() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            // force access
            + "    document.getElementById('anImage').height;\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <img src='foo.gif' id='anImage'/>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);
        loadPage2(html);

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Accept: text/css,*/*;q=0.1")
    public void acceptHeaderCss() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <link href='test.css' rel='stylesheet' type='text/css'>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            // force access
            + "    var b = document.body;\n"
            + "    window.getComputedStyle(b, null);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);
        loadPage2(html);

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Accept: */*")
    public void acceptHeaderJavascript() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <script src='test.js' type='text/javascript'></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);
        loadPage2(html);

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Accept: */*")
    public void acceptHeaderJavascriptWithoutType() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <script src='test.js'></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);
        loadPage2(html);

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Accept: text/css,*/*;q=0.1")
    public void acceptHeaderCssWithoutType() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <link href='test.css' rel='stylesheet'>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            // force access
            + "    var b = document.body;\n"
            + "    window.getComputedStyle(b, null);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);
        loadPage2(html);

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "Accept: text/css,*/*;q=0.1"})
    public void acceptHeaderCssEmptyType() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <link href='test.css' rel='stylesheet' type=''>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            // force access
            + "    var b = document.body;\n"
            + "    window.getComputedStyle(b, null);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final int requests = getMockWebConnection().getRequestCount();
        loadPage2(html);

        final int count = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(count, getMockWebConnection().getRequestCount() - requests);
        assertEquals(getExpectedAlerts()[1], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "Accept: text/css,*/*;q=0.1"})
    public void acceptHeaderCssBlankType() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <link href='test.css' rel='stylesheet' type=' '>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            // force access
            + "    var b = document.body;\n"
            + "    window.getComputedStyle(b, null);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final int requests = getMockWebConnection().getRequestCount();
        loadPage2(html);

        final int count = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(count, getMockWebConnection().getRequestCount() - requests);
        assertEquals(getExpectedAlerts()[1], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "Accept: text/css,*/*;q=0.1"},
            CHROME = {"1", "Accept: text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;"
                    + "q=0.8,application/signed-exchange;v=b3;q=0.7"},
            EDGE = {"1", "Accept: text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;"
                    + "q=0.8,application/signed-exchange;v=b3;q=0.7"})
    public void acceptHeaderCssDifferentType() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <link href='test.css' rel='stylesheet' type='text/html'>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            // force access
            + "    var b = document.body;\n"
            + "    window.getComputedStyle(b, null);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final int requests = getMockWebConnection().getRequestCount();
        loadPage2(html);

        final int count = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(count, getMockWebConnection().getRequestCount() - requests);
        assertEquals(getExpectedAlerts()[1], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "Accept: text/css,*/*;q=0.1"},
            CHROME = {"1", "Accept: text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;"
                    + "q=0.8,application/signed-exchange;v=b3;q=0.7"},
            EDGE = {"1", "Accept: text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;"
                    + "q=0.8,application/signed-exchange;v=b3;q=0.7"})
    public void acceptHeaderCssWrongType() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <link href='test.css' rel='stylesheet' type='css'>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            // force access
            + "    var b = document.body;\n"
            + "    window.getComputedStyle(b, null);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final int requests = getMockWebConnection().getRequestCount();
        loadPage2(html);

        final int count = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(count, getMockWebConnection().getRequestCount() - requests);
        assertEquals(getExpectedAlerts()[1], acceptHeaderString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Accept: */*")
    public void acceptHeaderXMLHttpRequest() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "<content>blah2</content>\n"
            + "</xml>";

        getMockWebConnection().setDefaultResponse(xml);
        loadPage2(html);

        // we have to be a bit patient
        Thread.sleep(100);

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    private String acceptHeaderString() {
        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        final Map<String, String> headers = lastRequest.getAdditionalHeaders();

        final StringBuilder sb = new StringBuilder();
        for (final Entry<String, String> headerEntry : headers.entrySet()) {
            final String headerName = headerEntry.getKey();
            final String headerNameLower = headerName.toLowerCase(Locale.ROOT);
            if (HttpHeader.ACCEPT_LC.equals(headerNameLower)) {
                sb.append(headerName);
                sb.append(": ");
                sb.append(headerEntry.getValue());
            }
        }
        return sb.toString();
    }
}
