/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HTMLFrameElement} when used for {@link com.gargoylesoftware.htmlunit.html.HtmlFrame}.
 *
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Thomas Robbs
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLFrameElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Frame2")
    public void frameName() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "  <frame id='frame1'>\n"
            + "  <frame name='Frame2' onload='log(this.name)' id='frame2'>\n"
            + "</frameset></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "true"})
    public void contentDocument() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.getElementById('myFrame').contentDocument);\n"
            + "      log(document.getElementById('myFrame').contentDocument == frames.foo.document);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<frameset rows='*' onload='test()'>\n"
            + "  <frame name='foo' id='myFrame' src='about:blank'/>\n"
            + "</frameset>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void contentWindow() throws Exception {
        final String html
            = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  log(document.getElementById('myFrame').contentWindow == frames.foo);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<frameset rows='*' onload='test()'>\n"
                + "<frame name='foo' id='myFrame' src='about:blank'/>\n"
                + "</frameset>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for Bug #259.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"frame=OK", "frames.length=2", "frame=OK", "frames.length=0", "frame=OK", "frames.length=0"})
    public void frameTag1192854() throws Exception {
        final String html
            = "<html>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var root=this;\n"
            + "function listframes(frame) {\n"
            + "  if (frame == null) {\n"
            + "    log('frame=null');\n"
            + "  } else {\n"
            + "    log('frame=OK');\n"
            + "    var len = frame.frames.length;\n"
            + "    log('frames.length=' + len);\n"
            + "    for (var i = 0; i < len; i++) {\n"
            + "      listframes(frame.frames[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "document.write('<frameset id=\"frameset1\" "
            + "rows=\"50,50\"><frame id=\"frame1-1\" "
            + "src=\"about:blank\"><frame id=\"frame1-2\" "
            + "src=\"about:blank\"></frameset>');\n"
            + "listframes(root);\n"
            + "</script>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function handler() {}", "null", "null"})
    @NotYetImplemented
    // Currently a \n is put between the {}
    public void onloadNull() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler() {}\n"
            + "  function test() {\n"
            + "    var iframe = document.getElementById('myFrame');\n"
            + "    iframe.onload = handler;\n"
            + "    log(iframe.onload);\n"
            + "    iframe.onload = null;\n"
            + "    log(iframe.onload);\n"
            + "    try {\n"
            + "      iframe.onload = undefined;\n"
            + "      log(iframe.onload);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload=test()>\n"
            + "  <iframe id='myFrame'></iframe>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for Bug #203.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"§§URL§§subdir/frame.html", "§§URL§§frame.html"},
            IE = "§§URL§§subdir/frame.html")
    public void location() throws Exception {
        location("Frame1.location = \"frame.html\"");
        location("Frame1.location.replace(\"frame.html\")");
    }

    private void location(final String jsExpr) throws Exception {
        final String firstContent
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='*' onload='" + jsExpr + "'>\n"
            + "  <frame name='Frame1' src='subdir/frame.html'>\n"
            + "</frameset></html>";
        final String defaultContent
            = "<html><head><script>alert(location)</script></head></html>";

        getMockWebConnection().setDefaultResponse(defaultContent);

        final WebDriver driver = loadPage2(firstContent);

        expandExpectedAlertsVariables(URL_FIRST);
        verifyAlerts(driver, getExpectedAlerts());

        assertTitle(driver, "first");
    }

    /**
     * Regression test for Bug #288.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void writeFrameset() throws Exception {
        final String content1 = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "    document.write('<frameset>');\n"
            + "    document.write('<frame src=\"page2.html\" name=\"leftFrame\">');\n"
            + "    document.write('</frameset>');\n"
            + "</script>\n"
            + "</head></html>";
        final String content2 = "<html><head><script>parent.log(2)</script></head></html>";

        getMockWebConnection().setDefaultResponse(content2);

        loadPageVerifyTitle2(content1);
    }

    /**
     * Regression test for Bug #307.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("DIV")
    public void frameLoadedAfterParent() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe name='testFrame' src='testFrame.html'></iframe>\n"
            + "<div id='aButton'>test text</div>\n"
            + "</body></html>";
        final String frameContent
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "parent.log(top.document.getElementById('aButton').tagName);\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "testFrame.html"), frameContent);
        loadPageVerifyTitle2(html);
    }

    /**
     * Illustrates problem of issue #729.
     * See <a href="https://sourceforge.net/tracker/?func=detail&atid=448266&aid=2314485&group_id=47038">issue 729</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"about:blank", "oFrame.foo: undefined", "/frame1.html", "oFrame.foo: foo of frame 1",
             "/frame2.html", "oFrame.foo: foo of frame 2"})
    public void changingFrameDocumentLocation() throws Exception {
        final String firstHtml = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oFrame;\n"
            + "function init() {\n"
            + "  oFrame = self.frames['theFrame'];\n"
            + "}\n"
            + "function test(fileName) {\n"
            + "  if (oFrame.document.location == 'about:blank')\n" // to avoid different expectations for IE and FF
            + "    log('about:blank');\n"
            + "  else\n"
            + "    log(oFrame.document.location.pathname);\n"
            + "  log('oFrame.foo: ' + oFrame.foo);\n"
            + "  oFrame.document.location.href = fileName;\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='init()'>\n"
            + "<iframe name='theFrame'></iframe>\n"
            + "<button id='btn1' onclick='test(\"frame1.html\")'>load frame1</button>\n"
            + "<button id='btn2' onclick='test(\"frame2.html\")'>load frame2</button>\n"
            + "<button id='btn3' onclick='test(\"about:blank\")'>load about:blank</button>\n"
            + "</body></html>";

        final String frame1Html = "<html><head><title>frame 1</title>\n"
            + "<script>var foo = 'foo of frame 1'</script></head>\n"
            + "<body>frame 1</body></html>";
        final String frame2Html = frame1Html.replaceAll("frame 1", "frame 2");

        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame1.html"), frame1Html);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame2.html"), frame2Html);

        final String[] alerts = getExpectedAlerts();

        final WebDriver driver = loadPage2(firstHtml);
        driver.findElement(By.id("btn1")).click();
        verifyTitle2(driver, ArrayUtils.subarray(alerts, 0, 2));
        driver.findElement(By.id("btn2")).click();
        verifyTitle2(driver, ArrayUtils.subarray(alerts, 0, 4));
        driver.findElement(By.id("btn3")).click();
        verifyTitle2(driver, ArrayUtils.subarray(alerts, 0, 6));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLFrameElement]",
            IE = "[object Window]")
    public void frames_framesetOnLoad() throws Exception {
        final String mainHtml =
            "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>"
            + "</head>\n"
            + "<frameset onload=\"log(window.frames['f1'])\">\n"
            + "<frame id='f1' src='1.html'/>\n"
            + "<frame id='f2' src='1.html'/>\n"
            + "</frameset>\n"
            + "</html>";

        final String frame1 = "<html><head><title>1</title></head>\n"
            + "<body></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "1.html"), frame1);

        loadPageVerifyTitle2(mainHtml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLFrameElement]",
            IE = "[object Window]")
    public void frames_bodyOnLoad() throws Exception {
        final String mainHtml =
            "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<frameset>\n"
            + "<frame id='f1' src='1.html'/>\n"
            + "</frameset>\n"
            + "</html>";

        final String frame1 = "<html><head><title>1</title></head>\n"
            + "<body onload=\"parent.log(parent.frames['f1'])\"></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "1.html"), frame1);

        loadPageVerifyTitle2(mainHtml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLFrameElement]",
            IE = "[object Window]")
    public void parent_frames() throws Exception {
        final String mainHtml =
            "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<frameset>\n"
            + "<frame id='f1' src='1.html'/>\n"
            + "</frameset>\n"
            + "</html>";

        final String frame1 = "<html><head><title>f1</title>\n"
            + "<script type='text/javascript'>\n"
            + "  function test() {\n"
            + "    parent.log(parent.frames['f1']);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test();'></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "1.html"), frame1);

        loadPageVerifyTitle2(mainHtml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("OnloadTest head bottom frameset")
    public void onloadOrderRows() throws Exception {
        final String html = "<html><head><title>OnloadTest</title></head>\n"
                + "<frameset rows='50%,*' onLoad='document.title += \" frameset\"'>\n"
                + "  <frame name='head' src='head.html'>\n"
                + "  <frame name='bottom' src='bottom.html'>\n"
                + "</frameset>\n"
                + "</html>";

        final String top = "<html><head><title>Head</title></head>\n"
                + "<body onload='top.document.title += \" head\"'>head</body>\n"
                + "</html>";
        final String bottom = "<html><head><title>Bottom</title></head>\n"
                + "<body onload='top.document.title += \" bottom\"'>bottom</body>\n"
                + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "head.html"), top);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "bottom.html"), bottom);

        final WebDriver driver = loadPage2(html);
        assertEquals(3, getMockWebConnection().getRequestCount());
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("OnloadTest left right frameset")
    public void onloadOrderCols() throws Exception {
        final String html = "<html><head><title>OnloadTest</title></head>\n"
                + "<frameset cols='50%,*' onLoad='document.title += \" frameset\"'>\n"
                + "  <frame name='left' src='left.html'>\n"
                + "  <frame name='right' src='right.html'>\n"
                + "</frameset>\n"
                + "</html>";

        final String left = "<html><head><title>Left</title></head>\n"
                + "<body onload='top.document.title += \" left\"'>left</body>\n"
                + "</html>";
        final String right = "<html><head><title>Right</title></head>\n"
                + "<body onload='top.document.title += \" right\"'>right</body>\n"
                + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "left.html"), left);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "right.html"), right);

        final WebDriver driver = loadPage2(html);
        assertEquals(3, getMockWebConnection().getRequestCount());
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"OnloadTest", "header -> content -> frameSet",
             "content\nClick for new frame content with onload",
             "header -> content -> frameSet -> onloadFrame",
             "onloadFrame\nNew content loaded..."})
    public void windowLocationReplaceOnload() throws Exception {
        final String html = "<html><head><title>OnloadTest</title></head>\n"
                + "<frameset rows='50,*' onLoad=\"top.header.addToFrameOrder('frameSet');\">\n"
                + "  <frame name='header' src='header.html'>\n"
                + "  <frame name='content' id='content' "
                        + "src=\"javascript:window.location.replace('content.html')\">\n"
                + "</frameset>\n"
                + "</html>";

        final String headerFrame = "<html><head><title>headerFrame</title></head>\n"
                + "<script type='text/javascript'>\n"
                + "  function addToFrameOrder(frame) {\n"
                + "    var spacer = ' -> ';\n"
                + "    var frameOrder = document.getElementById('frameOrder').innerHTML;\n"
                + "    if (frameOrder == '') {spacer = '';}\n"
                + "    document.getElementById('frameOrder').innerHTML = frameOrder + spacer + frame;\n"
                + "  }\n"
                + "</script>\n"
                + "<body onload=\"addToFrameOrder('header');\">\n"
                + "  <div id=\"frameOrder\"></div>\n"
                + "</body></html>";

        final String contentFrame = "<html><head><title>contentFrame</title></head>\n"
                + "<body onload=\"top.header.addToFrameOrder('content');\">\n"
                + "  <h3>content</h3>\n"
                + "  <a name='onloadFrameAnchor' href='onload.html' "
                        + "target='content'>Click for new frame content with onload</a>\n"
                + "</body></html>";

        final String onloadFrame = "<html><head><title>onloadFrame</title></head>\n"
                + "<body onload=\"alert('Onload alert.');top.header.addToFrameOrder('onloadFrame');\">\n"
                + "  <script type='text/javascript'>\n"
                + "    alert('Body alert.');\n"
                + "  </script>\n"
                + "  <h3>onloadFrame</h3>\n"
                + "  <p id='newContent'>New content loaded...</p>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "header.html"), headerFrame);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "content.html"), contentFrame);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "onload.html"), onloadFrame);

        final WebDriver driver = loadPage2(html);
        // top frame
        assertTitle(driver, getExpectedAlerts()[0]);

        // header frame
        driver.switchTo().frame("header");
        assertEquals(getExpectedAlerts()[1], driver.findElement(By.id("frameOrder")).getText());

        // content frame
        driver.switchTo().defaultContent();
        driver.switchTo().frame("content");
        assertEquals(getExpectedAlerts()[2], driver.findElement(By.tagName("body")).getText());

        driver.findElement(By.name("onloadFrameAnchor")).click();
        final boolean ie = getBrowserVersion().isIE();
        verifyAlerts(driver, "Body alert.");
        if (!ie) {
            verifyAlerts(driver, "Onload alert.");
        }
        driver.switchTo().defaultContent();
        if (ie) {
            verifyAlerts(driver, "Onload alert.");
        }
        Thread.sleep(1000);

        driver.switchTo().frame("header");
        assertEquals(getExpectedAlerts()[3], driver.findElement(By.id("frameOrder")).getText());

        driver.switchTo().defaultContent();
        driver.switchTo().frame("content");
        assertEquals(getExpectedAlerts()[4], driver.findElement(By.tagName("body")).getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"OnloadTest", "header -> content -> frameSet",
             "content\nClick for new frame content with onload",
             "header -> content -> frameSet -> onloadFrame",
             "onloadFrame\nNew content loaded..."})
    public void windowLocationAssignOnload() throws Exception {
        final String html = "<html><head><title>OnloadTest</title></head>\n"
                + "<frameset rows='50,*' onLoad=\"top.header.addToFrameOrder('frameSet');\">\n"
                + "  <frame name='header' src='header.html'>\n"
                + "  <frame name='content' id='content' "
                        + "src=\"javascript:window.location.assign('content.html')\">\n"
                + "</frameset>\n"
                + "</html>";

        final String headerFrame = "<html><head><title>headerFrame</title></head>\n"
                + "<script type='text/javascript'>\n"
                + "  function addToFrameOrder(frame) {\n"
                + "    var spacer = ' -> ';\n"
                + "    var frameOrder = document.getElementById('frameOrder').innerHTML;\n"
                + "    if (frameOrder == '') {spacer = '';}\n"
                + "    document.getElementById('frameOrder').innerHTML = frameOrder + spacer + frame;\n"
                + "  }\n"
                + "</script>\n"
                + "<body onload=\"addToFrameOrder('header');\">\n"
                + "  <div id='frameOrder'></div>\n"
                + "</body></html>";

        final String contentFrame = "<html><head><title>contentFrame</title></head>\n"
                + "<body onload=\"top.header.addToFrameOrder('content');\">\n"
                + "  <h3>content</h3>\n"
                + "  <a name='onloadFrameAnchor' href='onload.html' "
                        + "target='content'>Click for new frame content with onload</a>\n"
                + "</body></html>";

        final String onloadFrame = "<html><head><title>onloadFrame</title></head>\n"
                + "<body onload=\"alert('Onload alert.');top.header.addToFrameOrder('onloadFrame');\">\n"
                + "  <script type='text/javascript'>\n"
                + "    alert('Body alert.');\n"
                + "  </script>\n"
                + "  <h3>onloadFrame</h3>\n"
                + "  <p id='newContent'>New content loaded...</p>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "header.html"), headerFrame);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "content.html"), contentFrame);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "onload.html"), onloadFrame);

        final WebDriver driver = loadPage2(html);
        // top frame
        assertTitle(driver, getExpectedAlerts()[0]);

        // header frame
        driver.switchTo().frame("header");
        assertEquals(getExpectedAlerts()[1], driver.findElement(By.id("frameOrder")).getText());

        // content frame
        driver.switchTo().defaultContent();
        driver.switchTo().frame("content");
        assertEquals(getExpectedAlerts()[2], driver.findElement(By.tagName("body")).getText());

        driver.findElement(By.name("onloadFrameAnchor")).click();
        final boolean ie = getBrowserVersion().isIE();
        verifyAlerts(driver, "Body alert.");
        if (!ie) {
            verifyAlerts(driver, "Onload alert.");
        }
        driver.switchTo().defaultContent();
        if (ie) {
            verifyAlerts(driver, "Onload alert.");
        }
        Thread.sleep(1000);

        driver.switchTo().frame("header");
        assertEquals(getExpectedAlerts()[3], driver.findElement(By.id("frameOrder")).getText());

        driver.switchTo().defaultContent();
        driver.switchTo().frame("content");
        assertEquals(getExpectedAlerts()[4], driver.findElement(By.tagName("body")).getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"OnloadTest", "header -> content -> frameSet",
             "content\nClick for new frame content with onload",
             "header -> content -> frameSet -> onloadFrame",
             "onloadFrame\nNew content loaded..."})
    @NotYetImplemented
    public void windowLocationSetOnload() throws Exception {
        final String html = "<html><head><title>OnloadTest</title></head>\n"
                + "<frameset rows='50,*' onLoad=\"top.header.addToFrameOrder('frameSet');\">\n"
                + "  <frame name='header' src='header.html'>\n"
                + "  <frame name='content' id='content' "
                        + "src=\"javascript:window.location='content.html'\">\n"
                + "</frameset>\n"
                + "</html>";

        final String headerFrame = "<html><head><title>headerFrame</title></head>\n"
                + "<script type='text/javascript'>\n"
                + "  function addToFrameOrder(frame) {\n"
                + "    var spacer = ' -> ';\n"
                + "    var frameOrder = document.getElementById('frameOrder').innerHTML;\n"
                + "    if (frameOrder == '') {spacer = '';}\n"
                + "    document.getElementById('frameOrder').innerHTML = frameOrder + spacer + frame;\n"
                + "  }\n"
                + "</script>\n"
                + "<body onload=\"addToFrameOrder('header');\">\n"
                + "  <div id='frameOrder'></div>\n"
                + "</body></html>";

        final String contentFrame = "<html><head><title>contentFrame</title></head>\n"
                + "<body onload=\"top.header.addToFrameOrder('content');\">\n"
                + "  <h3>content</h3>\n"
                + "  <a name='onloadFrameAnchor' href='onload.html' "
                        + "target='content'>Click for new frame content with onload</a>\n"
                + "</body></html>";

        final String onloadFrame = "<html><head><title>onloadFrame</title></head>\n"
                + "<body onload=\"top.header.addToFrameOrder('onloadFrame');\">\n"
                + "  <h3>onloadFrame</h3>\n"
                + "  <p id='newContent'>New content loaded...</p>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "header.html"), headerFrame);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "content.html"), contentFrame);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "onload.html"), onloadFrame);

        final WebDriver driver = loadPage2(html);
        // top frame
        assertTitle(driver, getExpectedAlerts()[0]);

        // header frame
        driver.switchTo().frame("header");
        assertEquals(getExpectedAlerts()[1], driver.findElement(By.id("frameOrder")).getText());

        // content frame
        driver.switchTo().defaultContent();
        driver.switchTo().frame("content");
        assertEquals(getExpectedAlerts()[2], driver.findElement(By.tagName("body")).getText());

        driver.findElement(By.name("onloadFrameAnchor")).click();
        driver.switchTo().defaultContent();
        driver.switchTo().frame("header");
        assertEquals(getExpectedAlerts()[3], driver.findElement(By.id("frameOrder")).getText());

        driver.switchTo().defaultContent();
        driver.switchTo().frame("content");
        assertEquals(getExpectedAlerts()[4], driver.findElement(By.tagName("body")).getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"localhost", "localhost", "localhost", "localhost"})
    public void domain() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function doTest() {\n"
                + "      log(document.domain);\n"
                + "      log(document.getElementById('left').contentWindow.document.domain);\n"
                + "      log(document.getElementById('center').contentWindow.document.domain);\n"
                + "      log(document.getElementById('right').contentWindow.document.domain);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<frameset cols='33%,33%,*' onLoad='doTest()'>\n"
                + "  <frame name='left' id='left' >\n"
                + "  <frame name='center' id='center' src='about:blank'>\n"
                + "  <frame name='right' id='right' src='left.html'>\n"
                + "</frameset>\n"
                + "</html>";

        final String left = "<html><head><title>Left</title></head>\n"
                + "<body>left</body>\n"
                + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "left.html"), left);

        loadPageVerifyTitle2(html);
        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"loaded", "null"},
            IE = {"loaded", "error"})
    public void deny() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function check() {\n"
            + "      try {\n"
            + "        log(document.getElementById(\"frame1\").contentDocument);\n"
            + "      } catch (e) { log('error'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<frameset cols='42%' >\n"
            + "  <frame name='frame1' id='frame1' src='content.html' "
                      + "onLoad='log(\"loaded\");check()' onError='log(\"error\")'>\n"
            + "</frameset>\n"
            + "</html>";

        final String left = "<html><head><title>Frame Title</title></head>\n"
                + "<body>Frame Content</body>\n"
                + "</html>";

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("X-Frame-Options", "DENY"));

        getMockWebConnection().setResponse(new URL(URL_FIRST, "content.html"), left,
                200, "OK", MimeType.TEXT_HTML, headers);

        loadPageVerifyTitle2(html);
        assertEquals(2, getMockWebConnection().getRequestCount());
    }
}
