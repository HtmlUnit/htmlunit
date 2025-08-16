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
package org.htmlunit.html;

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.htmlunit.HttpHeader;
import org.htmlunit.MockWebConnection;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Unit tests for {@link HtmlInlineFrame}.
 *
 * @author Mike Bowler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlInlineFrame2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLIFrameElement]")
    public void simpleScriptable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <iframe id='myId'>\n"
            + "</body></html>";

        final WebDriver webDriver = loadPageVerifyTitle2(html);
        if (webDriver instanceof HtmlUnitDriver) {
            final HtmlElement element = toHtmlElement(webDriver.findElement(By.id("myId")));
            assertTrue(HtmlInlineFrame.class.isInstance(element));
        }
    }

    /**
     * Self-closing iframe tag is accepted by IE but not by FF.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "[object HTMLIFrameElement]", "null"})
    public void selfClosingIFrame() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(window.frames.length);\n"
            + "    log(document.getElementById('frame1'));\n"
            + "    log(document.getElementById('frame2'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <iframe id='frame1'/>\n"
            + "  <iframe id='frame2'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test, the correct frame is used for a target, even if some frames
     * have the same name.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void targetResolution() throws Exception {
        final String framesContent = DOCTYPE_HTML
                + "<html><head><title>Top Page</title></head>\n"
                + "<body><div id='content'>Body of top frame</div>\n"
                + "  <iframe src='left.html' id='id-left' name='left'></iframe>\n"
                + "  <iframe src='right.html' id='id-right' name='right'></iframe>\n"
                + "</body>\n"
                + "</html>";

        final String rightFrame = DOCTYPE_HTML
                + "<html><head><title>Right Frame</title></head>\n"
                + "<body><div id='content'>Body of right frame</div></body>\n"
                + "</html>";

        final String leftFrame = DOCTYPE_HTML
                + "<html><head><title>Left Frame</title></head>\n"
                + "<body>\n"
                + "  <div id='content'>Body of left frame</div>\n"
                + "  <a id='link' name='link' href='new_inner.html' target='right'>Click link</a>\n"
                + "  <iframe id='id-inner' name='right' width='100' height='100' src='inner.html'></iframe>\n"
                + "</body>\n"
                + "</html>";

        final String innerFrame = DOCTYPE_HTML
                + "<html><head><title>Inner Frame</title></head>\n"
                + "<body><div id='content'>Body of inner frame</div></body>\n"
                + "</html>";

        final String newInnerFrame = DOCTYPE_HTML
                + "<html><head><title>New inner Frame</title></head>\n"
                + "<body><div id='content'>Body of new inner frame</div></body>\n"
                + "</html>";

        final String baseUrl = URL_FIRST.toString();

        final URL leftFrameUrl = new URL(baseUrl + "left.html");
        final URL rightFrameUrl = new URL(baseUrl + "right.html");
        final URL innerFrameURL = new URL(baseUrl + "inner.html");
        final URL newInnerFrameURL = new URL(baseUrl + "new_inner.html");

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(leftFrameUrl, leftFrame);
        webConnection.setResponse(rightFrameUrl, rightFrame);
        webConnection.setResponse(innerFrameURL, innerFrame);
        webConnection.setResponse(newInnerFrameURL, newInnerFrame);

        final WebDriver driver = loadPage2(framesContent);

        // top frame
        assertTitle(driver, "Top Page");
        assertEquals("Body of top frame", driver.findElement(By.id("content")).getText());

        // left frame
        driver.switchTo().frame("id-left");
        assertEquals("Body of left frame", driver.findElement(By.id("content")).getText());
        // inner frame
        driver.switchTo().frame("id-inner");
        assertEquals("Body of inner frame", driver.findElement(By.id("content")).getText());
        // right frame
        driver.switchTo().defaultContent();
        driver.switchTo().frame("id-right");
        assertEquals("Body of right frame", driver.findElement(By.id("content")).getText());

        // clicking on a link which contains a target 'right'. But this target frame is defined two times.
        driver.switchTo().defaultContent();
        driver.switchTo().frame("id-left");
        driver.findElement(By.id("link")).click();

        // left frame
        driver.switchTo().defaultContent();
        driver.switchTo().frame("id-left");
        assertEquals("Body of left frame", driver.findElement(By.id("content")).getText());
        // inner frame
        driver.switchTo().frame("id-inner");
        assertEquals("Body of new inner frame", driver.findElement(By.id("content")).getText());
        // right frame
        driver.switchTo().defaultContent();
        driver.switchTo().frame("id-right");
        assertEquals("Body of right frame", driver.findElement(By.id("content")).getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void scriptUnderIFrame() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><body>\n"
            + "<iframe src='" + URL_SECOND + "'>\n"
            + "  <div><script>alert(1);</script></div>\n"
            + "  <script src='" + URL_THIRD + "'></script>\n"
            + "</iframe>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><body><script>alert(2);</script></body></html>";
        final String thirdContent = "alert('3');";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setResponse(URL_THIRD, thirdContent, "text/javascript");

        loadPageWithAlerts2(firstContent);
    }

    /**
     * Looks like url's with the about schema are always behave
     * like 'about:blank'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "about:blank",
            CHROME = "about://unsupported",
            EDGE = "about://unsupported")
    @HtmlUnitNYI(CHROME = "about:blank",
            EDGE = "about:blank")
    public void aboutSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var frame = document.getElementById('tstFrame');\n"
            + "    try {"
            + "      log(frame.contentWindow.location.href);\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <iframe id='tstFrame' src='about://unsupported'></iframe>\n"
            + "  <button id='test' onclick='test()'>Test</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("test")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * See issue #1897.
     * In the end the strict mode should not be used for the setup of
     * the new window.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1:true", "2:false", "3:false", "4:false"})
    @HtmlUnitNYI(CHROME = {"1:false", "2:false", "3:false", "4:false"},
            EDGE = {"1:false", "2:false", "3:false", "4:false"},
            FF = {"1:false", "2:false", "3:false", "4:false"},
            FF_ESR = {"1:false", "2:false", "3:false", "4:false"})
    public void createIframeFromStrictFunction() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    'use strict';\n"
                + "    var iframe = document.createElement('iframe');\n"
                + "    log('1:' + !this);\n"
                + "    log('2:' + !iframe);\n"
                + "  }\n"
                + "  function test2() {\n"
                + "    var iframe = document.createElement('iframe');\n"
                + "    log('3:' + !this);\n"
                + "    log('4:' + !iframe);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test();test2()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Check for the right referrer header.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§index.html?test")
    public void referrer() throws Exception {
        final String framesContent = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title>Top Page</title></head>\n"
                + "<body>\n"
                + "  <iframe src='iframe.html'></iframe>\n"
                + "</body>\n"
                + "</html>";

        final String iFrame = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>Body</body>\n"
                + "</html>";

        expandExpectedAlertsVariables(URL_FIRST);

        final URL indexUrl = new URL(URL_FIRST.toString() + "index.html");
        final URL iFrameUrl = new URL(URL_FIRST.toString() + "iframe.html");

        getMockWebConnection().setResponse(indexUrl, framesContent);
        getMockWebConnection().setResponse(iFrameUrl, iFrame);

        loadPage2(framesContent, new URL(URL_FIRST.toString() + "index.html?test#ref"));
        Thread.sleep(DEFAULT_WAIT_TIME.toMillis() / 10);
        assertEquals(2, getMockWebConnection().getRequestCount());

        final Map<String, String> lastAdditionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertEquals(getExpectedAlerts()[0], lastAdditionalHeaders.get(HttpHeader.REFERER));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void localFile() throws Exception {
        final URL fileURL = getClass().getClassLoader().getResource("testfiles/xhtml.html");
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        final String html = DOCTYPE_HTML
                + "<html>"
                + "<head><title>Top Page</title></head>\n"
                + "<body>\n"
                + "  <iframe id='myFrame' src='" + fileURL + "'></iframe>\n"
                + "  <div>HtmlUnit</div>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, "Top Page");
        assertEquals("HtmlUnit", driver.findElement(By.tagName("body")).getText());

        driver.switchTo().frame("myFrame");
        assertEquals("", driver.findElement(By.tagName("body")).getText());
    }
}
