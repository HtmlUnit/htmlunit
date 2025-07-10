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

import java.net.URL;

import org.htmlunit.MockWebConnection;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link HtmlFrame}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class HtmlFrame2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void crossFrameJavascript() throws Exception {
        final String firstHtml = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>function render() { window.bar.real_render(); }</script>\n"
            + "<iframe src='" + URL_SECOND + "' onload='render();' name='bar'></iframe>\n"
            + "</body></html>";

        final String secondHtml = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>"
            + LOG_WINDOW_NAME_FUNCTION
            + "function real_render() { log(2); }</script>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);
        loadPage2(firstHtml);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Regression test for for bug
     * <a href="http://sf.net/support/tracker.php?aid=2819477">2819477</a>: onload handler should be called only one
     * time!
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            CHROME = {"2", "1", "1"},
            EDGE = {"2", "1", "1"})
    @HtmlUnitNYI(CHROME = "1",
            EDGE = "1")
    public void iframeOnloadCalledOnlyOnce() throws Exception {
        final String firstHtml = DOCTYPE_HTML
            + "<html><body>\n"
            + "<iframe src='" + URL_SECOND + "' onload='window.top.name += \"1\\u00a7\"'></iframe>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "");
        loadPage2(firstHtml);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * URL {@code about:blank} has a special meaning as it is what is loaded in the frame
     * before the real content is loaded.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "1"},
            FF = "1",
            FF_ESR = "1")
    @HtmlUnitNYI(CHROME = "1",
            EDGE = "1")
    public void iframeOnloadAboutBlank() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<iframe src='about:blank' onload='window.top.name += \"1\\u00a7\"'></iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Regression test for for bug
     * <a href="http://sourceforge.net/p/htmlunit/bugs/925/">925</a>.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("second [object HTMLFormElement] third [object HTMLFormElement] "
                        + "parent [object HTMLFormElement]")
    // real FF/CHROME sometimes alerts 'third' before 'second'
    public void postponeLoading() throws Exception {
        final String html = "<FRAMESET rows='50%,*' "
                + "onload=\"document.title += ' parent ' + window.parent.frames['third'].document.frm\">\n"
            + "  <FRAME name='second' frameborder='0' src='second.html'>\n"
            + "  <FRAME name='third' frameborder='0' src='third.html'>\n"
            + "</FRAMESET>";

        final String secondHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<body onload=\"top.document.title += ' second ' + window.parent.frames['third'].document.frm\">\n"
            + "  <h1>second</h1>\n"
            + "</body></html>";

        final String thirdHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<body onload=\"top.document.title += ' third ' + window.parent.frames['third'].document.frm\">\n"
            + "  <form name='frm' id='frm'>\n"
            + "    <input type='text' id='one' name='one' value='something'>\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "second.html"), secondHtml);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "third.html"), thirdHtml);

        final WebDriver driver = loadPage2(html);
        assertEquals(3, getMockWebConnection().getRequestCount());
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("second third first")
    public void frameOnload() throws Exception {
        final String html = "<FRAMESET rows='50%,50%' onload=\"document.title += ' first'\">\n"
            + "  <FRAME name='second' src='second.html'>\n"
            + "  <FRAME name='third' src='third.html'>\n"
            + "</FRAMESET>";

        final String secondHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<body onload=\"top.document.title += ' second'\">\n"
            + "  <h1>second</h1>\n"
            + "</body></html>";

        final String thirdHtml = DOCTYPE_HTML
                + "<html>\n"
                + "<body onload=\"top.document.title += ' third'\">\n"
                + "  <h1>third</h1>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "second.html"), secondHtml);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "third.html"), thirdHtml);

        final WebDriver driver = loadPage2(html);
        assertEquals(3, getMockWebConnection().getRequestCount());
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("second fourth third first")
    @HtmlUnitNYI(CHROME = "fourth second third first",
            EDGE = "fourth second third first",
            FF = "fourth second third first",
            FF_ESR = "fourth second third first")
    public void frameOnloadFrameInFrame() throws Exception {
        final String html = "<FRAMESET rows='50%,50%' onload=\"document.title += ' first'\">\n"
            + "  <FRAME name='second' src='second.html'>\n"
            + "  <FRAME name='third' src='third.html'>\n"
            + "</FRAMESET>";

        final String secondHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<body onload=\"top.document.title += ' second'\">\n"
            + "  <h1>second</h1>\n"
            + "</body></html>";

        final String thirdHtml = "<FRAMESET cols='100%' onload=\"top.document.title += ' third'\">\n"
                + "  <FRAME name='fourth' src='fourth.html'>\n"
                + "</FRAMESET>";

        final String fourthHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<body onload=\"top.document.title += ' fourth'\">\n"
            + "  <h1>fourth</h1>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "second.html"), secondHtml);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "third.html"), thirdHtml);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "fourth.html"), fourthHtml);

        final WebDriver driver = loadPage2(html);
        assertEquals(4, getMockWebConnection().getRequestCount());
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * Another regression test for for bug
     * <a href="http://sf.net/support/tracker.php?aid=2819477">2819477</a>: frame content
     * sometimes not loaded.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("myInputName")
    public void iframeContentNotLoaded() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>FooBar</title></head>\n"
            + "<body>\n"
            + "<iframe id='myFrame' name='myFrame'></iframe>\n"

            + "<script type='text/javascript'>\n"
            + "function writeForm(frame) {\n"
            + "  var div=frame.document.getElementById('div');\n"
            + "  div.innerHTML = \"<form id='myForm'><input type='text' id='myInput' name='myInputName'/></form>\";\n"
            + "}\n"

            + "function writeFrame(frame) {\n"
            + "  var frameHtml=\"<html><head><title>Inner Frame</title></head>"
            + "<body><div id='div'></div></body></html>\";\n"
            + "  frame.document.write(frameHtml);\n"
            + "  frame.document.close();\n"
            + "}\n"

            + "function loadFrame() {\n"
            + "  var frame=parent.frames['myFrame'];\n"
            + "  writeFrame(frame);\n"
            + "  writeForm(frame);\n"
            + "  alert(frame.document.getElementById('myInput').name);\n"
            + "}\n"

            + "loadFrame();\n"
            + "</script>\n"

            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @see <a href="sourceforge.net/p/htmlunit/bugs/1443/">Bug #1443</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void onloadInNavigatedFrame() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "  <frame src='frame1.html' id='frame1'>\n"
            + "</frameset></html>";

        final String firstHtml = DOCTYPE_HTML
                + "<html><body>\n"
                + "<a id='a1' href='frame2.html'>hello</a>\n"
                + "</body></html>";

        final String secondHtml = DOCTYPE_HTML + "<html><body onload='alert(\"foo\")'></body></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(new URL(URL_FIRST, "frame1.html"), firstHtml);
        webConnection.setResponse(new URL(URL_FIRST, "frame2.html"), secondHtml);

        final WebDriver driver = loadPage2(html);
        driver.switchTo().frame(0);

        driver.findElement(By.id("a1")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("loaded")
    public void lineBreaksInUrl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "</head>\n"
            + "<body onload='alert(\"loaded\");'>Test</body>\n"
            + "</html>";

        getMockWebConnection().setResponse(new URL(URL_SECOND, "abcd"), html);
        expandExpectedAlertsVariables(URL_SECOND);

        final String frame = DOCTYPE_HTML
            + "<html>\n"
            + "  <frameset cols='100%'>\n"
            + "    <frame src='" + URL_SECOND + "a\rb\nc\r\nd' id='myFrame'>\n"
            + "  </frameset></html>";

        expandExpectedAlertsVariables(URL_SECOND);
        loadPageWithAlerts2(frame);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("loaded")
    public void lineBreaksInUrlIFrame() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "</head>\n"
            + "<body onload='alert(\"loaded\");'>Test</body>\n"
            + "</html>";

        getMockWebConnection().setResponse(new URL(URL_SECOND, "abcd"), html);
        expandExpectedAlertsVariables(URL_SECOND);

        final String frame = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <iframe src='" + URL_SECOND + "a\rb\nc\r\nd' id='myFrame'></iframe>\n"
            + "  </body>\n"
            + "</html>";

        expandExpectedAlertsVariables(URL_SECOND);
        loadPageWithAlerts2(frame);
    }
}
