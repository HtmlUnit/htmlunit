/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import static org.junit.Assert.assertArrayEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link WebClient} using WebDriverTestCase.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class WebClient3Test extends WebDriverTestCase {

    /**
     * Regression test for bug 2822048: a 302 redirect without Location header.
     * @throws Exception if an error occurs
     */
    @Test
    public void redirect302WithoutLocation() throws Exception {
        final String html = "<html><body><a href='page2'>to redirect</a></body></html>";
        getMockWebConnection().setDefaultResponse("", 302, "Found", null);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("a")).click();
        assertEquals(getDefaultUrl() + "page2", driver.getCurrentUrl());
    }

    /**
     * Regression test for bug 3017719: a 302 redirect should change the page url.
     * @throws Exception if an error occurs
     */
    @Test
    public void redirect302() throws Exception {
        final String html = "<html><body><a href='redirect.html'>redirect</a></body></html>";

        final URL url = new URL(getDefaultUrl(), "page2.html");
        getMockWebConnection().setResponse(url, html);

        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Location", "/page2.html"));
        getMockWebConnection().setDefaultResponse("", 302, "Found", null, headers);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("a")).click();
        assertEquals(url.toString(), driver.getCurrentUrl());
    }

    /**
     * Regression test for bug 3012067: a null pointer exception was occurring.
     * @throws Exception if an error occurs
     */
    @Test
    public void bug3012067_npe() throws Exception {
        final String html = "<html><body>\n"
            + "<form action='" + getDefaultUrl() + "#foo' method='post'></form>\n"
            + "<script>\n"
            + "function doWork() {\n"
            + "  var f = document.forms[0];\n"
            + "  f.submit();\n"
            + "  f.submit();\n"
            + "}\n"
            + "</script>\n"
            + "<span id='clickMe' onclick='doWork()'>click</span>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
    }

    /**
     * Ensure that response stream can be read more than one time.
     * @throws Exception if an error occurs
     */
    @Test
    public void readStreamTwice() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<iframe src='binaryFile.bin'></iframe>\n"
            + "<iframe src='foo.html'></iframe>\n"
            + "</body></html>";

        final MockWebConnection mockConnection = getMockWebConnection();
        final byte[] binaryContent = new byte[4818];
        for (int i = 0; i < binaryContent.length; ++i) {
            binaryContent[i] = (byte) (Math.random() * Byte.MAX_VALUE);
        }
        mockConnection.setDefaultResponse(binaryContent, 200, "OK", "application/octet-stream");
        final URL urlFoo = new URL(getDefaultUrl(), "foo.html");
        mockConnection.setResponse(urlFoo, "<html></html>");

        final WebDriver driver = loadPage2(html);
        final WebElement iframe1 = driver.findElement(By.tagName("iframe"));
        if (driver instanceof HtmlUnitDriver) {
            final HtmlInlineFrame htmlUnitIFrame1 = (HtmlInlineFrame) toHtmlElement(iframe1);
            final WebResponse iframeWebResponse = htmlUnitIFrame1.getEnclosedPage().getWebResponse();
            byte[] receivedBytes = IOUtils.toByteArray(iframeWebResponse.getContentAsStream());
            receivedBytes = IOUtils.toByteArray(iframeWebResponse.getContentAsStream());
            assertArrayEquals(binaryContent, receivedBytes);
        }
    }

    /**
     * Was causing an Exception in IE simulation
     * as of HtmlUnit-2.8-SNAPSHOT on Aug. 04, 2010.
     * @throws Exception if the test fails
     */
    @Test
    public void escapeRequestQuery() throws Exception {
        getMockWebConnection().setDefaultResponse("");

        loadPage2("", new URL(getDefaultUrl(), "foo?a=<b>i</b>"));
    }
}
