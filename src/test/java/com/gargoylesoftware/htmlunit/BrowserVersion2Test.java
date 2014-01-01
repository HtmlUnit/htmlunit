/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link BrowserVersion}.
 * For some details on IE view you can have a look at
 * http://blogs.msdn.com/b/ieinternals/archive/2009/07/01/ie-and-the-accept-header.aspx
 *
 * @version $Revision$
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class BrowserVersion2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            IE = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, */*",
            IE11 = "Accept: text/html, application/xhtml+xml, */*")
    public void acceptHeaderGetUrl() throws Exception {
        final String html = "<html><body>Response</body></html>";
        loadPage2(html, getDefaultUrl());

        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "2", "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" },
            IE = { "2", "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, */*" },
            IE11 = { "2", "Accept: text/html, application/xhtml+xml, */*" })
    public void acceptHeaderWindowOpen() throws Exception {
        String html = "<html><body>Response</body></html>";
        getMockWebConnection().setDefaultResponse(html);

        html = "<html><head><title>First</title></head>\n"
                + "<body>\n"
                + "  <a id='clickme' href='javascript: window.open(\"" + URL_SECOND + "\")'>Click me</a>\n"
                + "</body></html>";
        final WebDriver driver = loadPage2(html, getDefaultUrl());
        driver.findElement(By.id("clickme")).click();

        assertEquals(getExpectedAlerts()[0], Integer.toString(getMockWebConnection().getRequestCount()));
        assertEquals(getExpectedAlerts()[1], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" },
            IE = {"2", "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, */*" },
            IE11 = {"2", "Accept: text/html, application/xhtml+xml, */*" })
    public void acceptHeaderAnchorClick() throws Exception {
        String html = "<html><body>Response</body></html>";
        getMockWebConnection().setDefaultResponse(html);

        html = "<html><head><title>First</title></head>\n"
                + "<body>\n"
                + "  <a id='clickme' href='test.html'>Click me</a>\n"
                + "</body></html>";
        final WebDriver driver = loadPage2(html, getDefaultUrl());
        driver.findElement(By.id("clickme")).click();

        assertEquals(getExpectedAlerts()[0], Integer.toString(getMockWebConnection().getRequestCount()));
        assertEquals(getExpectedAlerts()[1], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            IE = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, */*",
            IE11 = "Accept: text/html, application/xhtml+xml, */*")
    public void acceptHeaderAnchorClickWithType() throws Exception {
        String html = "<html><body>Response</body></html>";
        getMockWebConnection().setDefaultResponse(html);

        html = "<html><head><title>First</title></head>\n"
                + "<body>\n"
                + "  <a id='clickme' href='test.html' type='text/plain'>Click me</a>\n"
                + "</body></html>";
        final WebDriver driver = loadPage2(html, getDefaultUrl());
        driver.findElement(By.id("clickme")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: image/png,image/*;q=0.8,*/*;q=0.5",
            CHROME = "Accept: image/webp,*/*;q=0.8",
            IE = "Accept: */*",
            IE11 = "Accept: image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5")
    public void acceptHeaderImage() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            // force access
            + "    document.getElementById('anImage').height;\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <img src='foo.gif' id='anImage'/>\n"
            + "</body></html>";
        loadPage2(html, getDefaultUrl());

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: text/css,*/*;q=0.1",
            IE = "Accept: */*",
            IE11 = "Accept: text/css, */*")
    public void acceptHeaderCss() throws Exception {
        final String html
            = "<html><head>\n"
            + "  <link href='test.css' rel='stylesheet' type='text/css'>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            // force access
            + "    var b = document.body;\n"
            + "    window.getComputedStyle ? window.getComputedStyle(b,null) : b.currentStyle;\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";
        loadPage2(html, getDefaultUrl());

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: */*",
            IE11 = "Accept: application/javascript, */*;q=0.8")
    public void acceptHeaderJavascript() throws Exception {
        final String html
            = "<html><head>\n"
            + "  <script src='test.js' type='text/javascript'>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";
        loadPage2(html, getDefaultUrl());

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: */*",
            IE11 = "Accept: application/javascript, */*;q=0.8")
    public void acceptHeaderJavascriptWithoutType() throws Exception {
        final String html
            = "<html><head>\n"
            + "  <script src='test.js'>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";
        loadPage2(html, getDefaultUrl());

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: text/css,*/*;q=0.1",
            IE = "Accept: */*",
            IE11 = "Accept: text/css, */*")
    public void acceptHeaderCssWithoutType() throws Exception {
        final String html
            = "<html><head>\n"
            + "  <link href='test.css' rel='stylesheet'>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            // force access
            + "    var b = document.body;\n"
            + "    window.getComputedStyle ? window.getComputedStyle(b,null) : b.currentStyle;\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";
        loadPage2(html, getDefaultUrl());

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: text/css,*/*;q=0.1",
            IE = "Accept: */*",
            IE11 = "Accept: text/css, */*")
    public void acceptHeaderCssDifferentType() throws Exception {
        final String html
            = "<html><head>\n"
            + "  <link href='test.css' rel='stylesheet' type='text/html'>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            // force access
            + "    var b = document.body;\n"
            + "    window.getComputedStyle ? window.getComputedStyle(b,null) : b.currentStyle;\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";
        loadPage2(html, getDefaultUrl());

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            IE = "Accept: */*")
    public void acceptHeaderXMLHttpRequest() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request;\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
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

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    private String acceptHeaderString() {
        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        final Map<String, String> headers = lastRequest.getAdditionalHeaders();

        final StringBuilder sb = new StringBuilder();
        for (final Entry<String, String> headerEntry : headers.entrySet()) {
            final String headerName = headerEntry.getKey();
            final String headerNameLower = headerName.toLowerCase(Locale.ENGLISH);
            if ("accept".equals(headerNameLower)) {
                sb.append(headerName);
                sb.append(": ");
                sb.append(headerEntry.getValue());
            }
        }
        return sb.toString();
    }
}
