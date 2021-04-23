/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlImage}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlImage2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void loadImageWithoutSource() throws Exception {
        loadImage("");
        loadImageInnerHtml("");
        loadImageImportNodeHtml("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void loadImageEmptySource() throws Exception {
        loadImage("src=''");
        loadImageInnerHtml("src=''");
        loadImageImportNodeHtml("src=''");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF = "2",
            FF78 = "2")
    public void loadImageBlankSource() throws Exception {
        loadImage("src=' '");
        loadImageInnerHtml("src=' '");
        loadImageImportNodeHtml("src=' '");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE = "1")
    @NotYetImplemented(IE)
    public void loadImage() throws Exception {
        loadImage("src='img.jpg'");
        loadImageInnerHtml("src='img.jpg'");
        loadImageImportNodeHtml("src='img.jpg'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void loadImageUnknown() throws Exception {
        loadImage("src='unknown'");
        loadImageInnerHtml("src='unknown'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            CHROME = "1",
            EDGE = "1")
    @NotYetImplemented({CHROME, EDGE})
    public void loadImageUnknown2() throws Exception {
        loadImageImportNodeHtml("src='unknown'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void loadImageBrokenUrl() throws Exception {
        loadImage("src='rbri://nowhere'");
        loadImageInnerHtml("src='rbri://nowhere'");
        loadImageImportNodeHtml("src='rbri://nowhere'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void loadImageAboutBlank() throws Exception {
        loadImage("src='about:blank'");
        loadImageInnerHtml("src='about:blank'");
        loadImageImportNodeHtml("src='about:blank'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void loadImageAboutX() throws Exception {
        loadImage("src='about:x'");
        loadImageInnerHtml("src='about:x'");
        loadImageImportNodeHtml("src='about:x'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE = "1")
    @NotYetImplemented(IE)
    public void loadImageWrongType() throws Exception {
        loadImage("src='" + URL_FIRST + "'");
        loadImageInnerHtml("src='" + URL_FIRST + "'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            CHROME = "1",
            EDGE = "1",
            IE = "1")
    @NotYetImplemented({CHROME, EDGE, IE})
    public void loadImageWrongType2() throws Exception {
        loadImageImportNodeHtml("src='" + URL_FIRST + "'");
    }

    private void loadImage(final String src) throws Exception {
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var img = document.getElementById('myImage');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage' " + src + " >\n"
            + "</body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);
        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount() - count);
    }

    private void loadImageInnerHtml(final String src) throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var tester = document.getElementById('tester');\n"
            + "    tester.innerHTML = \"<img id='myImage' " + src + " >\";\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <button id='test' onclick='test()'>Test</button>\n"
            + "  <div id='tester'></div>\n"
            + "</body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        driver.findElement(By.id("test")).click();
        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount() - count);
    }

    private void loadImageImportNodeHtml(final String src) throws Exception {
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var tester = document.getElementById('tester');\n"

            + "    var doc = document.implementation.createHTMLDocument('test');\n"
            + "    doc.body.innerHTML = \"<img id='myImage' " + src + " >\";\n"

            + "    var srcNode = doc.getElementById('myImage');\n"
            + "    var newNode = document.importNode(srcNode, true);\n"
            + "    document.body.replaceChild(newNode, tester);\n"
            + "    alert('before');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <button id='test' onclick='test()'>Test</button>\n"
            + "  <div id='tester'></div>\n"
            + "</body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        driver.findElement(By.id("test")).click();
        verifyAlerts(driver, "before");

        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount() - count);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void isDisplayed() throws Exception {
        isDisplayed("src='img.jpg'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void isDisplayedNoSource() throws Exception {
        isDisplayed("");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void isDisplayedEmptySource() throws Exception {
        isDisplayed("src=''");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            CHROME = "false",
            EDGE = "false")
    public void isDisplayedBlankSource() throws Exception {
        isDisplayed("src=' '");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void isDisplayedInvalidSource() throws Exception {
        isDisplayed("src='unknown.gif'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void isDisplayedWrongType() throws Exception {
        isDisplayed("src='" + URL_FIRST + "'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false")
    public void isDisplayedDisplayNone() throws Exception {
        isDisplayed("src='img.jpg' style='display: none'");
    }

    private void isDisplayed(final String src) throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);

            getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);
        }

        final String html = "<html><head><title>Page A</title></head>\n"
                + "<body>\n"
                + "  <img id='myImg' " + src + " >\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final boolean displayed = driver.findElement(By.id("myImg")).isDisplayed();
        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[0]), displayed);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "§§URL§§abcd/img.gif"})
    public void lineBreaksInUrl() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-gif.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_SECOND, "abcd/img.gif");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/gif", emptyList);
        }

        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var img = document.getElementById('myImg');\n"
            + "    img.width;\n" // this forces image loading in htmlunit
            + "    alert(img.width);\n"
            + "    alert(img.src);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <img id='myImg' src='" + URL_SECOND + "a\rb\nc\r\nd/img.gif' onError='alert(\"broken\");'>\n"
            + "</body>\n"
            + "</html>";

        expandExpectedAlertsVariables(URL_SECOND);
        loadPageWithAlerts2(html);

        shutDownRealIE();
    }
}
