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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF17;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF24;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLFrameElement} when used for {@link com.gargoylesoftware.htmlunit.html.HtmlFrame}.
 *
 * @version $Revision$
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
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frame id='frame1'>\n"
            + "    <frame name='Frame2' onload='alert(this.name)' id='frame2'>\n"
            + "</frameset></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void contentDocument() throws Exception {
        final String html
            = "<html><head><title>first</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  alert(document.getElementById('myFrame').contentDocument == frames.foo.document);\n"
                + "}\n"
                + "</script></head>\n"
                + "<frameset rows='*' onload='test()'>\n"
                + "<frame name='foo' id='myFrame' src='about:blank'/>\n"
                + "</frameset>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void contentWindow() throws Exception {
        final String html
            = "<html><head><title>first</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  alert(document.getElementById('myFrame').contentWindow == frames.foo);\n"
                + "}\n"
                + "</script></head>\n"
                + "<frameset rows='*' onload='test()'>\n"
                + "<frame name='foo' id='myFrame' src='about:blank'/>\n"
                + "</frameset>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 1192854.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "frame=OK", "frames.length=2", "frame=OK", "frames.length=0", "frame=OK", "frames.length=0" })
    public void testFrameTag1192854() throws Exception {
        final String html
            = "<html>\n"
            + "<script>\n"
            + "var root=this;\n"
            + "function listframes(frame) {\n"
            + "  if (frame == null) {\n"
            + "    alert('frame=null');\n"
            + "  } else {\n"
            + "    alert('frame=OK');\n"
            + "    var len = frame.frames.length;\n"
            + "    alert('frames.length=' + len);\n"
            + "    for (var i=0; i<len; i++) {\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function handler() {}", "null", "null" },
            IE8 = { "function handler() {}", "null", "exception" })
    @NotYetImplemented({ FF17, FF24, IE8 })
    // Currently a \n is put between the {}
    public void onloadNull() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function handler() {}\n"
            + "  function test() {\n"
            + "    var iframe = document.getElementById('myFrame');\n"
            + "    iframe.onload = handler;\n"
            + "    alert(iframe.onload);\n"
            + "    iframe.onload = null;\n"
            + "    alert(iframe.onload);\n"
            + "    try {\n"
            + "      iframe.onload = undefined;\n"
            + "      alert(iframe.onload);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload=test()>\n"
            + "  <iframe id='myFrame'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for http://sourceforge.net/p/htmlunit/bugs/203/.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "§§URL§§subdir/frame.html", "§§URL§§frame.html" },
            IE11 = { "§§URL§§subdir/frame.html" })
    public void location() throws Exception {
        location("Frame1.location = \"frame.html\"");
        location("Frame1.location.replace(\"frame.html\")");
    }

    private void location(final String jsExpr) throws Exception {
        final String firstContent
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='*' onload='" + jsExpr + "'>\n"
            + "    <frame name='Frame1' src='subdir/frame.html'>\n"
            + "</frameset></html>";
        final String defaultContent
            = "<html><head><script>alert(location)</script></head></html>";

        getMockWebConnection().setResponse(URL_FIRST, firstContent);
        getMockWebConnection().setDefaultResponse(defaultContent);

        final WebDriver driver = loadPage2(firstContent, URL_FIRST);
        assertEquals("first", driver.getTitle());

        expandExpectedAlertsVariables(URL_FIRST);
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Regression test for bug 1236048.
     * See http://sourceforge.net/p/htmlunit/bugs/288/.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void writeFrameset() throws Exception {
        final String content1 = "<html><head>\n"
            + "<script>\n"
            + "    document.write('<frameset>');\n"
            + "    document.write('<frame src=\"page2.html\" name=\"leftFrame\">');\n"
            + "    document.write('</frameset>');\n"
            + "</script>\n"
            + "</head></html>";
        final String content2 = "<html><head><script>alert(2)</script></head></html>";

        getMockWebConnection().setDefaultResponse(content2);

        loadPageWithAlerts2(content1);
    }

    /**
     * Regression test for bug 307.
     * See http://sourceforge.net/p/htmlunit/bugs/307/.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("DIV")
    public void frameLoadedAfterParent() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<iframe name='testFrame' src='testFrame.html'></iframe>\n"
            + "<div id='aButton'>test text</div>\n"
            + "</body></html>";
        final String frameContent
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "alert(top.document.getElementById('aButton').tagName);\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(new URL(getDefaultUrl() + "testFrame.html"), frameContent);
        loadPageWithAlerts2(html);
    }

    /**
     * Illustrates problem of issue #2314485.
     * See https://sourceforge.net/tracker/?func=detail&atid=448266&aid=2314485&group_id=47038
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "about:blank", "oFrame.foo: undefined", "/frame1.html", "oFrame.foo: foo of frame 1",
        "/frame2.html", "oFrame.foo: foo of frame 2" })
    public void changingFrameDocumentLocation() throws Exception {
        final String firstHtml = "<html><head><script>\n"
            + "var oFrame;\n"
            + "function init() {\n"
            + "  oFrame = self.frames['theFrame'];\n"
            + "}\n"
            + "function test(fileName) {\n"
            + "  if (oFrame.document.location == 'about:blank')\n" // to avoid different expectations for IE and FF
            + "    alert('about:blank');\n"
            + "  else\n"
            + "    alert(oFrame.document.location.pathname);\n"
            + "  alert('oFrame.foo: ' + oFrame.foo);\n"
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

        getMockWebConnection().setResponse(URL_FIRST, firstHtml);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame1.html"), frame1Html);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame2.html"), frame2Html);

        final WebDriver driver = loadPage2(firstHtml, URL_FIRST);
        driver.findElement(By.id("btn1")).click();
        driver.findElement(By.id("btn2")).click();
        driver.findElement(By.id("btn3")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object Window]",
            FF = "[object HTMLFrameElement]",
            FF17 = "undefined",
            IE8 = "[object]")
    @NotYetImplemented(FF17)
    public void frames_framesetOnLoad() throws Exception {
        final String mainHtml =
            "<html><head><title>frames</title></head>\n"
            + "<frameset onload=\"alert(window.frames['f1'])\">\n"
            + "<frame id='f1' src='1.html'/>\n"
            + "<frame id='f2' src='1.html'/>\n"
            + "</frameset>\n"
            + "</html>";

        final String frame1 = "<html><head><title>1</title></head>\n"
            + "<body></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_FIRST, mainHtml);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "1.html"), frame1);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLFrameElement]",
            FF17 = "undefined",
            IE8 = "[object]",
            IE11 = "[object Window]")
    @NotYetImplemented(FF17)
    public void frames_bodyOnLoad() throws Exception {
        final String mainHtml =
            "<html><head><title>frames</title></head>\n"
            + "<frameset>\n"
            + "<frame id='f1' src='1.html'/>\n"
            + "</frameset>\n"
            + "</html>";

        final String frame1 = "<html><head><title>1</title></head>\n"
            + "<body onload=\"alert(parent.frames['f1'])\"></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_FIRST, mainHtml);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "1.html"), frame1);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object Window]",
            FF = "[object HTMLFrameElement]",
            FF17 = "undefined",
            IE8 = "[object]")
    @NotYetImplemented(FF17)
    public void parent_frames() throws Exception {
        final String mainHtml =
            "<html><head><title>frames</title></head>\n"
            + "<frameset>\n"
            + "<frame id='f1' src='1.html'/>\n"
            + "</frameset>\n"
            + "</html>";

        final String frame1 = "<html><head><title>1</title></head>\n"
            + "<body onload=\"alert(parent.frames['f1'])\"></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_FIRST, mainHtml);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "1.html"), frame1);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "head", "bottom", "frameset" })
    public void onloadOrderRows() throws Exception {
        final String html = "<html><head><title>OnloadTest</title></head>\n"
                + "<frameset rows='50%,*' onLoad='alert(\"frameset\")'>\n"
                + "  <frame name='head' src='head.html'>\n"
                + "  <frame name='bottom' src='bottom.html'>\n"
                + "</frameset>\n"
                + "</html>";

        final String top = "<html><head><title>Head</title></head>\n"
                + "<body onload='alert(\"head\")'>head</body>\n"
                + "</html>";
        final String bottom = "<html><head><title>Bottom</title></head>\n"
                + "<body onload='alert(\"bottom\")'>bottom</body>\n"
                + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "head.html"), top);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "bottom.html"), bottom);

        loadPageWithAlerts2(html);
        assertEquals(3, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "left", "right", "frameset" })
    public void onloadOrderCols() throws Exception {
        final String html = "<html><head><title>OnloadTest</title></head>\n"
                + "<frameset cols='50%,*' onLoad='alert(\"frameset\")'>\n"
                + "  <frame name='left' src='left.html'>\n"
                + "  <frame name='right' src='right.html'>\n"
                + "</frameset>\n"
                + "</html>";

        final String left = "<html><head><title>Left</title></head>\n"
                + "<body onload='alert(\"left\")'>left</body>\n"
                + "</html>";
        final String right = "<html><head><title>Right</title></head>\n"
                + "<body onload='alert(\"right\")'>right</body>\n"
                + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "left.html"), left);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "right.html"), right);

        loadPageWithAlerts2(html);
        assertEquals(3, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "OnloadTest", "header -> content -> frameSet",
                "content\nClick for new frame content with onload",
                "header -> content -> frameSet -> onloadFrame",
                "onloadFrame\nNew content loaded..." })
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

        getMockWebConnection().setResponse(URL_FIRST, html);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "header.html"), headerFrame);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "content.html"), contentFrame);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "onload.html"), onloadFrame);

        final WebDriver driver = loadPage2(html, URL_FIRST);
        // top frame
        assertEquals(getExpectedAlerts()[0], driver.getTitle());

        // header frame
        driver.switchTo().frame("header");
        assertEquals(getExpectedAlerts()[1], driver.findElement(By.id("frameOrder")).getText());

        // content frame
        driver.switchTo().defaultContent();
        driver.switchTo().frame("content");
        assertEquals(getExpectedAlerts()[2],
                driver.findElement(By.tagName("body")).getText());

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
    @Alerts(DEFAULT = { "OnloadTest", "header -> content -> frameSet",
                        "content\nClick for new frame content with onload",
                        "header -> content -> frameSet -> onloadFrame",
                        "onloadFrame\nNew content loaded..." },
            FF = { "OnloadTest", "header -> frameSet", "" })
    @NotYetImplemented(FF)
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

        getMockWebConnection().setResponse(URL_FIRST, html);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "header.html"), headerFrame);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "content.html"), contentFrame);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "onload.html"), onloadFrame);

        final WebDriver driver = loadPage2(html, URL_FIRST);
        // top frame
        assertEquals(getExpectedAlerts()[0], driver.getTitle());

        // header frame
        driver.switchTo().frame("header");
        assertEquals(getExpectedAlerts()[1], driver.findElement(By.id("frameOrder")).getText());

        // content frame
        driver.switchTo().defaultContent();
        driver.switchTo().frame("content");
        assertEquals(getExpectedAlerts()[2],
                driver.findElement(By.tagName("body")).getText());

        if (StringUtils.isNotEmpty(getExpectedAlerts()[2])) {
            driver.findElement(By.name("onloadFrameAnchor")).click();
            driver.switchTo().defaultContent();
            driver.switchTo().frame("header");
            assertEquals(getExpectedAlerts()[3], driver.findElement(By.id("frameOrder")).getText());

            driver.switchTo().defaultContent();
            driver.switchTo().frame("content");
            assertEquals(getExpectedAlerts()[4], driver.findElement(By.tagName("body")).getText());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "OnloadTest", "header -> content -> frameSet",
                        "content\nClick for new frame content with onload",
                        "header -> content -> frameSet -> onloadFrame",
                        "onloadFrame\nNew content loaded..." })
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
                + "<body onload=\"alert('Onload alert.');top.header.addToFrameOrder('onloadFrame');\">\n"
                + "  <script type='text/javascript'>\n"
                + "    alert('Body alert.');\n"
                + "  </script>\n"
                + "  <h3>onloadFrame</h3>\n"
                + "  <p id='newContent'>New content loaded...</p>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_FIRST, html);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "header.html"), headerFrame);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "content.html"), contentFrame);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "onload.html"), onloadFrame);

        final WebDriver driver = loadPage2(html, URL_FIRST);
        // top frame
        assertEquals(getExpectedAlerts()[0], driver.getTitle());

        // header frame
        driver.switchTo().frame("header");
        assertEquals(getExpectedAlerts()[1], driver.findElement(By.id("frameOrder")).getText());

        // content frame
        driver.switchTo().defaultContent();
        driver.switchTo().frame("content");
        assertEquals(getExpectedAlerts()[2],
                driver.findElement(By.tagName("body")).getText());

        if (StringUtils.isNotEmpty(getExpectedAlerts()[2])) {
            driver.findElement(By.name("onloadFrameAnchor")).click();
            driver.switchTo().defaultContent();
            driver.switchTo().frame("header");
            assertEquals(getExpectedAlerts()[3], driver.findElement(By.id("frameOrder")).getText());

            driver.switchTo().defaultContent();
            driver.switchTo().frame("content");
            assertEquals(getExpectedAlerts()[4], driver.findElement(By.tagName("body")).getText());
        }
    }
}
