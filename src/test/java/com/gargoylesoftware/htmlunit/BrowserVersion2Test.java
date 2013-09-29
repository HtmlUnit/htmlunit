/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Unit tests for {@link BrowserVersion}.
 * For some details on IE view you can have a look at
 * http://blogs.msdn.com/b/ieinternals/archive/2009/07/01/ie-and-the-accept-header.aspx
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class BrowserVersion2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            IE = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, */*")
    @NotYetImplemented
    public void acceptHeaderGetUrl() throws Exception {
        final String html = "<html><body>Response</body></html>";
        loadPage2(html, getDefaultUrl());

        assertEquals(getExpectedAlerts()[0], acceptHeaderString());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Accept: image/png,image/*;q=0.8,*/*;q=0.5",
            IE = "Accept: */*",
            CHROME = "Accept: image/webp,*/*;q=0.8")
    @NotYetImplemented(Browser.FF)
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
            IE = "Accept: */*")
    @NotYetImplemented(Browser.FF)
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
    @Alerts(DEFAULT = "Accept: text/css,*/*;q=0.1",
            IE = "Accept: */*")
    @NotYetImplemented(Browser.FF)
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
            IE = "Accept: */*")
    @NotYetImplemented(Browser.FF)
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
