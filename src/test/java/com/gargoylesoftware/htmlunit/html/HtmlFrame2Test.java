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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlFrame}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlFrame2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void crossFrameJavascript() throws Exception {
        final String firstHtml = "<html><body>\n"
            + "<script>function render() { window.bar.real_render(); }</script>\n"
            + "<iframe src='" + URL_SECOND + "' onload='render();' name='bar'></iframe>\n"
            + "</body></html>";

        final String secondHtml = "<html><body>\n"
            + "<script>function real_render() { alert(2); }</script>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);
        loadPageWithAlerts2(firstHtml);
    }

    /**
     * Regression test for for bug
     * <a href="http://sf.net/support/tracker.php?aid=2819477">2819477</a>: onload handler should be called only one
     * time!
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void iframeOnloadCalledOnlyOnce() throws Exception {
        final String firstHtml = "<html><body>\n"
            + "<iframe src='" + URL_SECOND + "' onload='alert(1)'></iframe>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "");
        loadPageWithAlerts2(firstHtml);
    }

    /**
     * URL about:blank has as special meaning as it is what is loaded in the frame.
     * before the real content is loaded.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void iframeOnloadAboutBlank() throws Exception {
        final String html = "<html><body>\n"
            + "<iframe src='about:blank' onload='alert(1)'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for for bug
     * <a href="http://sf.net/support/tracker.php?aid=2873802">2873802</a>.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "parent [object]", "second [object]", "third [object]" },
            FF = { "second [object HTMLFormElement]", "third [object HTMLFormElement]",
            "parent [object HTMLFormElement]" })
    // real FF sometimes alerts 'third' before 'second'
    public void postponeLoading() throws Exception {
        final String html = "<FRAMESET onload=\"alert('parent ' + window.parent.frames.third.document.frm)\">\n"
            + "  <FRAME name=second frameborder=0 src='second.html'>\n"
            + "  <FRAME name=third frameborder=0 src='third.html'>\n"
            + "</FRAMESET>";

        final String secondHtml = "<html>\n"
            + "<body onload=\"alert('second ' + window.parent.frames.third.document.frm)\">\n"
            + "  <h1>second</h1>\n"
            + "</body></html>";

        final String thirdHtml = "<html>\n"
            + "<body onload=\"alert('third ' + window.parent.frames.third.document.frm)\">\n"
            + "  <form name='frm' id='frm'>\n"
            + "      <input type='text' id='one' name='one' value='something'>\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "second.html"), secondHtml);
        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "third.html"), thirdHtml);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void frameOnload() throws Exception {
        final String html = "<FRAMESET rows='50%,50%' onload=\"alert('first')\">\n"
            + "  <FRAME name='second' src='second.html'>\n"
            + "  <FRAME name='third' src='third.html'>\n"
            + "</FRAMESET>";

        final String secondHtml = "<html>\n"
            + "<body onload=\"alert('second')\">\n"
            + "  <h1>second</h1>\n"
            + "</body></html>";

        final String thirdHtml = "<html>\n"
                + "<body onload=\"alert('third')\">\n"
                + "  <h1>third</h1>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "second.html"), secondHtml);
        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "third.html"), thirdHtml);

        final WebDriver driver = loadPage2(html);
        final List<String> actualAlerts = getCollectedAlerts(driver);

        // tested with real ff17 and ie6; running in selenium returns different results
        Assert.assertEquals(3, actualAlerts.size());

        // ignore order of frame windows
        if (getBrowserVersion().isIE()) {
            // returns 'first' 'third' 'second'
            Assert.assertEquals("first", actualAlerts.get(0));
        }
        else {
            // returns 'third' 'second' 'first'
            Assert.assertEquals("first", actualAlerts.get(2));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(IE)
    public void frameOnloadFrameInFrame() throws Exception {
        final String html = "<FRAMESET rows='50%,50%' onload=\"alert('first')\">\n"
            + "  <FRAME name='second' src='second.html'>\n"
            + "  <FRAME name='third' src='third.html'>\n"
            + "</FRAMESET>";

        final String secondHtml = "<html>\n"
            + "<body onload=\"alert('second')\">\n"
            + "  <h1>second</h1>\n"
            + "</body></html>";

        final String thirdHtml = "<FRAMESET cols='100%' onload=\"alert('third')\">\n"
                + "  <FRAME name='fourth' src='fourth.html'>\n"
                + "</FRAMESET>";

        final String fourthHtml = "<html>\n"
            + "<body onload=\"alert('fourth')\">\n"
            + "  <h1>fourth</h1>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "second.html"), secondHtml);
        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "third.html"), thirdHtml);
        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "fourth.html"), fourthHtml);

        final WebDriver driver = loadPage2(html);
        final List<String> actualAlerts = getCollectedAlerts(driver);

        // tested with real ff17 and ie6; running in selenium returns different results
        Assert.assertEquals(4, actualAlerts.size());

        // ignore order of frame windows
        if (getBrowserVersion().isIE()) {
            // returns 'first' 'third' 'fourth' 'second'
            Assert.assertEquals("first", actualAlerts.get(0));
        }
        else {
            // returns 'first' at last
            Assert.assertEquals("first", actualAlerts.get(3));
        }
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
        final String html = "<html>\n"
            + "<head><title>FooBar</title></head>\n"
            + "<body>\n"
            + "<iframe id='myFrame' name='myFrame'></iframe>\n"

            + "<script type='text/javascript'>\n"
            + "function writeForm(frame) {\n"
            + "  var div=frame.document.getElementById('div');\n"
            + "  div.innerHTML=\"<form id='myForm'><input type='text' id='myInput' name='myInputName'/></form>\";\n"
            + "};\n"

            + "function writeFrame(frame) {\n"
            + "  var frameHtml=\"<html><head><title>Inner Frame</title></head>"
            + "<body><div id='div'></div></body></html>\";\n"
            + "  frame.document.write(frameHtml);\n"
            + "  frame.document.close();\n"
            + "};\n"

            + "function loadFrame() {\n"
            + "  var frame=parent.frames['myFrame'];\n"
            + "  writeFrame(frame);\n"
            + "  writeForm(frame);\n"
            + "  alert(frame.document.getElementById('myInput').name);"
            + "};\n"

            + "loadFrame();\n"
            + "</script>\n"

            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Was failing as of HtmlUnit-2.11.
     * @see <a href="sourceforge.net/p/htmlunit/bugs/1443/">Bug 1443</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void onloadInNavigatedFrame() throws Exception {
        final String html = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frame src='frame1.html' id='frame1'>\n"
            + "</frameset></html>";

        final String firstHtml = "<html><body>\n"
                + "<a id='a1' href='frame2.html'>hello</a>\n"
                + "</body></html>";

        final String secondHtml = "<html><body onload='alert(\"foo\")'></body></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(new URL(getDefaultUrl(), "frame1.html"), firstHtml);
        webConnection.setResponse(new URL(getDefaultUrl(), "frame2.html"), secondHtml);

        final WebDriver driver = loadPage2(html);
        driver.switchTo().frame(0);

        driver.findElement(By.id("a1")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }
}
