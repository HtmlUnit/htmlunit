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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link HtmlInlineFrame}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlInlineFrame2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLIFrameElement]",
            IE8 = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <iframe id='myId'>\n"
            + "</body></html>";

        final WebDriver webDriver = loadPageWithAlerts2(html);

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
    @Alerts(DEFAULT = { "1", "[object HTMLIFrameElement]", "null" },
            IE8 = { "2", "[object]", "[object]" })
    public void selfClosingIFrame() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(window.frames.length);\n"
            + "    alert(document.getElementById('frame1'));\n"
            + "    alert(document.getElementById('frame2'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <iframe id='frame1'/>\n"
            + "  <iframe id='frame2'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test, the right frame is used for a target, even if some frames
     * have the same name.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void targetResolution() throws Exception {
        final String framesContent = "<html><head><title>Top Page</title></head>\n"
                + "<body><div id='content'>Body of top frame</div>\n"
                + "  <iframe src='left.html' id='id-left' name='left'></iframe>\n"
                + "  <iframe src='right.html' id='id-right' name='right'></iframe>\n"
                + "</body>\n"
                + "</html>";

        final String rightFrame = "<html><head><title>Right Frame</title></head>\n"
                + "<body><div id='content'>Body of right frame</div></body>\n"
                + "</html>";

        final String leftFrame = "<html><head><title>Left Frame</title></head>\n"
                + "<body>\n"
                + "  <div id='content'>Body of left frame</div>\n"
                + "  <a id='link' name='link' href='new_inner.html' target='right'>Click link</a>"
                + "  <iframe id='id-inner' name='right' width='100' height='100' src='inner.html'></iframe>"
                + "</body>\n"
                + "</html>";

        final String innerFrame = "<html><head><title>Inner Frame</title></head>\n"
                + "<body><div id='content'>Body of inner frame</div></body>\n"
                + "</html>";

        final String newInnerFrame = "<html><head><title>New inner Frame</title></head>\n"
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
        assertEquals("Top Page", driver.getTitle());
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
}
