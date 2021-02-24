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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link Window}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author David K. Taylor
 * @author Darrell DeBoer
 * @author Marc Guillemot
 * @author Dierk Koenig
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Frank Danek
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 */
@RunWith(BrowserRunner.class)
public class Window3Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("about:blank")
    public void openWindow_emptyUrl() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "var w = window.open('');\n"
            + "alert(w ? w.document.location : w);\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "one", "two", "three"})
    @BuggyWebDriver(IE = {"undefined", "one", "two", "three"})
    public void opener() throws Exception {
        final URL urlThird = new URL(URL_FIRST, "third/");

        final String firstContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  alert(window.opener);\n"
            + "  alert('one');\n"
            + "  open('" + URL_SECOND + "', 'foo');\n"
            + "}\n"
            + "function callAlert(text) {\n"
            + "  alert(text);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final String secondContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Second</title><script>\n"
            + "function test() {\n"
            + "  opener.callAlert('two');\n"
            + "  document.form1.submit();\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='form1' action='" + urlThird + "' method='post'><input type='submit'></form>\n"
            + "</body></html>";
        final String thirdContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Third</title><script>\n"
            + "function test() {\n"
            + "  opener.callAlert('three');\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setResponse(urlThird, thirdContent);

        final WebDriver driver = loadPageWithAlerts2(firstContent);
        assertTitle(driver, "First");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("one")
    public void windowFrames() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body><script language='JavaScript'>\n"
            + "if (typeof top.frames['anyXXXname'] == 'undefined') {\n"
            + "  alert('one');\n"
            + "}\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Variables that are defined inside JavaScript should be accessible through the
     * window object (i.e. window.myVariable). Test that this works.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void javascriptVariableFromWindow() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>first</title></head><body><script>\n"
            + "myVariable = 'foo';\n"
            + "alert(window.myVariable);\n"
            + "</script></body></head>";

        loadPageWithAlerts2(html);
    }

    /**
     * Variables that are defined inside JavaScript should be accessible through the
     * window object (ie window.myVariable). Test that this works.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"parent.myVariable = second", "top.myVariable = first"})
    public void javascriptVariableFromTopAndParentFrame() throws Exception {
        final URL urlThird = new URL(URL_FIRST, "third/");

        final String firstContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>First</title></head><body><script>\n"
            + "myVariable = 'first'"
            + "  </script><iframe name='left' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String secondContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Second</title></head><body><script>\n"
            + "myVariable = 'second'"
            + "  </script><iframe name='innermost' src='" + urlThird + "'></iframe>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final String thirdContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Third</title><script>\n"
            + "myVariable = 'third';\n"
            + "function doTest() {\n"
            + "alert('parent.myVariable = ' + parent.myVariable);\n"
            + "alert('top.myVariable = ' + top.myVariable);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'></body></html>";
        getMockWebConnection().setResponse(urlThird, thirdContent);

        final WebDriver driver = loadPageWithAlerts2(firstContent);
        assertTitle(driver, "First");
    }

    /**
     * Variables that are defined inside JavaScript should be accessible through the
     * window object (i.e. window.myVariable). Test that this works.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"parent.second.myVariable = second", "parent.third.myVariable = third"})
    public void javascriptVariableFromNamedFrame() throws Exception {
        final URL urlThird = new URL(URL_FIRST, "third/");
        final URL urlFourth = new URL(URL_FIRST, "fourth/");

        final String firstContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "  <frameset rows='30%,70%'>\n"
            + "    <frame src='" + URL_SECOND + "' name='second'>\n"
            + "    <frame src='" + urlThird + "' name='third'>\n"
            + "  </frameset>\n"
            + "  <frame src='" + urlFourth + "' name='fourth'>\n"
            + "</frameset></html>";

        final String secondContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>second</title></head><body><script>\n"
            + "myVariable = 'second';\n"
            + "</script><p>second</p></body></html>";
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final String thirdContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>third</title></head><body><script>\n"
            + "myVariable = 'third';\n"
            + "</script><p>third</p></body></html>";
        getMockWebConnection().setResponse(urlThird, thirdContent);

        final String fourthContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>fourth</title></head><body onload='doTest()'><script>\n"
            + "myVariable = 'fourth';\n"
            + "function doTest() {\n"
            + "alert('parent.second.myVariable = ' + parent.second.myVariable);\n"
            + "alert('parent.third.myVariable = ' + parent.third.myVariable);\n"
            + "}\n"
            + "</script></body></html>";
        getMockWebConnection().setResponse(urlFourth, fourthContent);

        final WebDriver driver = loadPageWithAlerts2(firstContent);
        assertTitle(driver, "first");
    }

    /**
     * Variables that have not been defined should return null when accessed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void javascriptVariableFromWindow_NotFound() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>first</title></head><body><script>\n"
            + "myVariable = 'foo';\n"
            + "alert(window.myOtherVariable == null);\n"
            + "</script></body></head>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"fourth-second=Â§Â§URL2Â§Â§", "fourth-third=Â§Â§URL3Â§Â§"})
    public void getFrameByName() throws Exception {
        final URL urlThird = new URL(URL_FIRST, "third/");
        final URL urlFourth = new URL(URL_FIRST, "fourth/");

        final String firstContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "  <frameset rows='30%,70%'>\n"
            + "    <frame src='" + URL_SECOND + "' name='second'>\n"
            + "    <frame src='" + urlThird + "' name='third'>\n"
            + "  </frameset>\n"
            + "  <frame src='" + urlFourth + "' name='fourth'>\n"
            + "</frameset></html>";

        final String secondContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>second</title></head><body><p>second</p></body></html>";
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final String thirdContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>third</title></head><body><p>third</p></body></html>";
        getMockWebConnection().setResponse(urlThird, thirdContent);

        final String fourthContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>fourth</title></head><body onload='doTest()'><script>\n"
            + "function doTest() {\n"
            + "alert('fourth-second='+parent.second.document.location);\n"
            + "alert('fourth-third='+parent.third.document.location);\n"
            + "}\n"
            + "</script></body></html>";
        getMockWebConnection().setResponse(urlFourth, fourthContent);

        final String[] expectedAlerts = getExpectedAlerts();
        for (int i = 0; i < expectedAlerts.length; i++) {
            expectedAlerts[i] = expectedAlerts[i].replaceAll("Â§Â§URL2Â§Â§", URL_SECOND.toExternalForm())
                    .replaceAll("Â§Â§URL3Â§Â§", urlThird.toExternalForm());
        }
        setExpectedAlerts(expectedAlerts);

        final WebDriver driver = loadPageWithAlerts2(firstContent);
        assertTitle(driver, "first");
    }

    /**
     * Test the <tt>window.closed</tt> property.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "true"})
    @BuggyWebDriver(IE = {"false", "false", "true"})
    public void closed() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  alert(window.closed);\n"
            + "  var newWindow = window.open('about:blank', 'foo');\n"
            + "  alert(newWindow.closed);\n"
            + "  newWindow.close();\n"
            + "  alert(newWindow.closed);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.moveTo method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void moveTo() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  window.moveTo(10, 20);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.moveBy method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void moveBy() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  window.moveBy(10, 20);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests that the Window.resizeTo method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void resizeTo() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "window.resizeTo(10, 20);\n"
            + "window.resizeTo(-10, 20);\n"
            + "</script></head><body></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests that the Window.resizeBy method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void resizeBy() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "window.resizeBy(10, 20);\n"
            + "window.resizeBy(-10, 20);\n"
            + "</script></head><body></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.scroll method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void scroll() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "window.scroll(10, 20);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"document", "body"},
            IE = "document")
    @HtmlUnitNYI(IE = {"document", "body"})
    public void scrollEvents() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    var ta = document.getElementById('myTextArea');\n"
            + "    ta.value += msg + '; ';\n"
            + "  }\n"
            + "  function test() {\n"
            + "    document.addEventListener('scroll', function(e) { log(\"document\") });\n"
            + "    window.scroll(10, 20);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()' onscroll='log(\"body\")'>\n"
            + "  <div onscroll='log(\"div\")' style='height: 1000px;'></div>\n"

            + "  <textarea id='myTextArea' cols='80' rows='30'></textarea>\n"
            + "</body>\n"
            + "</html>";
        final WebDriver driver = loadPage2(html);

        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        assertEquals(String.join("; ", getExpectedAlerts()) + "; ", textArea.getAttribute("value"));
    }

    /**
     * Test that Window.scrollBy method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void scrollBy() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "window.scrollBy(10, 20);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"document", "body"},
            IE = "document")
    @HtmlUnitNYI(IE = {"document", "body"})
    public void scrollByEvents() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    var ta = document.getElementById('myTextArea');\n"
            + "    ta.value += msg + '; ';\n"
            + "  }\n"
            + "  function test() {\n"
            + "    document.addEventListener('scroll', function(e) { log(\"document\") });\n"
            + "    window.scrollBy(10, 20);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()' onscroll='log(\"body\")'>\n"
            + "  <div onscroll='log(\"div\")' style='height: 1000px;'></div>\n"

            + "  <textarea id='myTextArea' cols='80' rows='30'></textarea>\n"
            + "</body>\n"
            + "</html>";
        final WebDriver driver = loadPage2(html);

        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        assertEquals(String.join("; ", getExpectedAlerts()) + "; ", textArea.getAttribute("value"));
    }

    /**
     * Test that Window.scrollByLines method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = {},
            FF78 = {})
    public void scrollByLines() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "try {\n"
            + "  window.scrollByLines(2);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.scrollByPages method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = {},
            FF78 = {})
    public void scrollByPages() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "try {\n"
            + "  window.scrollByPages(2);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.scrollTo method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void scrollTo() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "window.scrollTo(10, 20);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"document", "body"},
            IE = "document")
    @HtmlUnitNYI(IE = {"document", "body"})
    public void scrollToEvents() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    var ta = document.getElementById('myTextArea');\n"
            + "    ta.value += msg + '; ';\n"
            + "  }\n"
            + "  function test() {\n"
            + "    document.addEventListener('scroll', function(e) { log(\"document\") });\n"
            + "    window.scrollTo(10, 20);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()' onscroll='log(\"body\")'>\n"
            + "  <div onscroll='log(\"div\")' style='height: 1000px;'></div>\n"

            + "  <textarea id='myTextArea' cols='80' rows='30'></textarea>\n"
            + "</body>\n"
            + "</html>";
        final WebDriver driver = loadPage2(html);

        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        assertEquals(String.join("; ", getExpectedAlerts()) + "; ", textArea.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"form1", "form1", "2", "2"})
    public void formByName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(window.form1.name);\n"
            + "    alert(form1.name);\n"
            + "    alert(window.form2.length);\n"
            + "    alert(form2.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form name='form1'></form>\n"
            + "  <form name='form2'></form>\n"
            + "  <form name='form2'></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"frame1", "frame1", "0", "0"},
            IE = {"frame1", "frame1", "2", "2"})
    @NotYetImplemented(IE)
    public void frameByName() throws Exception {
        final String html = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\""
            + "\"http://www.w3.org/TR/html4/frameset.dtd\">\n"
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(window.frame1.name);\n"
            + "    alert(frame1.name);\n"
            + "    alert(window.frame2.length);\n"
            + "    alert(frame2.length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<frameset onload='test()'>\n"
            + "  <frame src='" + URL_SECOND + "' name='frame1'>\n"
            + "  <frame src='" + URL_SECOND + "' name='frame2'>\n"
            + "  <frame src='" + URL_SECOND + "' name='frame2'>\n"
            + "</frameset>\n"
            + "</html>";

        final String frame = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head><title>frame</title></head><body></body></html>";
        getMockWebConnection().setDefaultResponse(frame);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"frame1", "frame1", "0", "0"})
    public void iframeByName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(window.frame1.name);\n"
            + "    alert(frame1.name);\n"
            + "    alert(window.frame2.length);\n"
            + "    alert(frame2.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <iframe name='frame1'></iframe>\n"
            + "  <iframe name='frame2'></iframe>\n"
            + "  <iframe name='frame2'></iframe>\n"
            // iframes are treated as frames and as such have priority over other elements
            + "  <form name='frame2'></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"5", "EMBED", "FORM", "IMG", "IMG", "OBJECT", "5", "EMBED", "FORM", "IMG", "IMG", "OBJECT"},
            IE = {"11", "A", "BUTTON", "EMBED", "FORM", "IMG", "IMG", "INPUT", "MAP", "OBJECT", "SELECT", "TEXTAREA",
                "11", "A", "BUTTON", "EMBED", "FORM", "IMG", "IMG", "INPUT", "MAP", "OBJECT", "SELECT", "TEXTAREA"})
    // The following tags cause problems with WebDriver:
    // applet, body, frame, frameset, head, html, isindex, meta, plaintext, title
    // The iframe tag is treated as frame and as such has priority over the other tags, which would make the test
    // useless.
    public void elementsByName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    dump(window.element1);\n"
            + "    dump(element1);\n"
            + "  }\n"
            + "  function dump(c) {\n"
            + "    alert(c.length);\n"
            + "    for (i = 0; i < c.length; i++) {\n"
            + "      alert(c.item(i).nodeName);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <abbr name='element1'></abbr>\n"
            + "  <acronym name='element1'></acronym>\n"
            + "  <a name='element1'></a>\n"
            + "  <address name='element1'></address>\n"
            // + "  <applet name='element1'></applet>\n"
            + "  <article name='element1'></article>\n"
            + "  <audio name='element1'></audio>\n"
            + "  <bgsound name='element1'>\n"
            + "  <base name='element1'>\n"
            + "  <basefont name='element1'>\n"
            + "  <bdo name='element1'></bdo>\n"
            + "  <big name='element1'></big>\n"
            + "  <blink name='element1'></blink>\n"
            + "  <blockquote name='element1'></blockquote>\n"
            // + "  <body name='element1'></body>\n"
            + "  <b name='element1'></b>\n"
            + "  <br name='element1'>\n"
            + "  <button name='element1'></button>\n"
            + "  <canvas name='element1'></canvas>\n"
            + "  <caption name='element1'></caption>\n"
            + "  <center name='element1'></center>\n"
            + "  <cite name='element1'></cite>\n"
            + "  <code name='element1'></code>\n"
            + "  <datalist name='element1'></datalist>\n"
            + "  <dfn name='element1'></dfn>\n"
            + "  <del name='element1'></del>\n"
            + "  <dir name='element1'></dir>\n"
            + "  <div name='element1'></div>\n"
            + "  <dl name='element1'>\n"
            + "    <dt name='element1'></dt>\n"
            + "    <dd name='element1'></dd>\n"
            + "  </dl>\n"
            + "  <embed name='element1'>\n"
            + "  <em name='element1'></em>\n"
            + "  <fieldset name='element1'></fieldset>\n"
            + "  <figcaption name='element1'></figcaption>\n"
            + "  <figure name='element1'></figure>\n"
            + "  <font name='element1'></font>\n"
            + "  <form name='element1'></form>\n"
            + "  <footer name='element1'></footer>\n"
            // + "  <frame name='element1'>\n"
            // + "  <frameset name='element1'></frameset>\n"
            + "  <h1 name='element1'></h1>\n"
            + "  <h2 name='element1'></h2>\n"
            + "  <h3 name='element1'></h3>\n"
            + "  <h4 name='element1'></h4>\n"
            + "  <h5 name='element1'></h5>\n"
            + "  <h6 name='element1'></h6>\n"
            // + "  <head name='element1'></head>\n"
            + "  <header name='element1'></header>\n"
            + "  <hr name='element1'>\n"
            // + "  <html name='element1'></html>\n"
            // + "  <iframe name='element1'></iframe>\n"
            + "  <q name='element1'></q>\n"
            + "  <ruby name='element1'>\n"
            + "    <rt name='element1'></rt>\n"
            + "    <rp name='element1'></rp>\n"
            + "  </ruby>\n"
            + "  <image name='element1'></image>\n"
            + "  <img name='element1'>\n"
            + "  <input name='element1'>\n"
            + "  <ins name='element1'></ins>\n"
            // + "  <isindex name='element1'></isindex>\n"
            + "  <i name='element1'></i>\n"
            + "  <kbd name='element1'></kbd>\n"
            + "  <keygen name='element1'>\n"
            + "  <label name='element1'></label>\n"
            + "  <legend name='element1'></legend>\n"
            + "  <listing name='element1'></listing>\n"
            + "  <link name='element1'>\n"
            + "  <map name='element1'>\n"
            + "    <area name='element1'>\n"
            + "  </map>\n"
            + "  <marquee name='element1'></marquee>\n"
            + "  <mark name='element1'></mark>\n"
            + "  <menu name='element1'></menu>\n"
            // + "  <meta name='element1'>\n"
            + "  <meter name='element1'></meter>\n"
            + "  <multicol name='element1'></multicol>\n"
            + "  <nav name='element1'></nav>\n"
            + "  <nextid name='element1'></nextid>\n"
            + "  <nobr name='element1'></nobr>\n"
            + "  <noembed name='element1'></noembed>\n"
            + "  <noframes name='element1'></noframes>\n"
            + "  <noscript name='element1'></noscript>\n"
            + "  <object name='element1'>\n"
            + "    <param name='element1'>\n"
            + "  </object>\n"
            + "  <ol name='element1'>\n"
            + "    <li name='element1'></li>\n"
            + "  </ol>\n"
            + "  <output name='element1'></output>\n"
            + "  <p name='element1'></p>\n"
            // + "  <plaintext name='element1'></plaintext>\n"
            + "  <pre name='element1'></pre>\n"
            + "  <progress name='element1'></progress>\n"
            + "  <s name='element1'></s>\n"
            + "  <samp name='element1'></samp>\n"
            + "  <script name='element1'></script>\n"
            + "  <section name='element1'></section>\n"
            + "  <select name='element1'>\n"
            + "    <optgroup name='element1'>\n"
            + "      <option name='element1'></option>\n"
            + "    </optgroup>\n"
            + "  </select>\n"
            + "  <small name='element1'></small>\n"
            + "  <source name='element1'>\n"
            + "  <spacer name='element1'></spacer>\n"
            + "  <span name='element1'></span>\n"
            + "  <strike name='element1'></strike>\n"
            + "  <strong name='element1'></strong>\n"
            + "  <style name='element1'></style>\n"
            + "  <sub name='element1'></sub>\n"
            + "  <sup name='element1'></sup>\n"
            + "  <table name='element1'>\n"
            + "    <colgroup name='element1'>\n"
            + "      <col name='element1'></col>\n"
            + "    </colgroup>\n"
            + "    <thead name='element1'>\n"
            + "      <tr name='element1'>\n"
            + "        <th name='element1'></th>\n"
            + "      </tr>\n"
            + "    </thead>\n"
            + "    <tbody name='element1'>\n"
            + "      <tr name='element1'>\n"
            + "        <td name='element1'></td>\n"
            + "      </tr>\n"
            + "    </tbody>\n"
            + "    <tfoot name='element1'></tfoot>\n"
            + "  </table>\n"
            + "  <textarea name='element1'></textarea>\n"
            + "  <tt name='element1'></tt>\n"
            + "  <time name='element1'></time>\n"
            // + "  <title name='element1'></title>\n"
            + "  <u name='element1'></u>\n"
            + "  <ul name='element1'></ul>\n"
            + "  <var name='element1'></var>\n"
            + "  <video name='element1'></video>\n"
            + "  <wbr name='element1'>\n"
            + "  <xmp name='element1'></xmp>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, 2 * DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2-2", "3-3", "4-4", "5-5", "6-6", "7-7", "8-8", "9-9", "10-10", "11-11", "10-10"})
    public void elementsByName_changedAfterGet() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            // 2
            + "    var collection1 = window.image1;\n"
            + "    var collection2 = image1;\n"
            + "    if (!collection1) {\n"
            + "      collection1 = [];\n"
            + "    }\n"
            + "    if (!collection2) {\n"
            + "      collection2 = [];\n"
            + "    }\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 3
            + "    var newImage1 = document.createElement('img');\n"
            + "    newImage1.name = 'image1';\n"
            + "    document.getElementById('outer1').appendChild(newImage1);\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 4
            + "    var newImage2 = document.createElement('img');\n"
            + "    newImage2.name = 'image1';\n"
            + "    document.getElementById('outer2').insertBefore(newImage2, null);\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 5
            + "    var newImage3 = document.createElement('img');\n"
            + "    newImage3.name = 'image1';\n"
            + "    document.getElementById('outer3').replaceChild(newImage3, document.getElementById('inner3'));\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 6
            + "    document.getElementById('outer4').outerHTML = '<img name=\"image1\">';\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 7
            + "    document.getElementById('outer5').innerHTML = '<img name=\"image1\">';\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 8
            + "    document.getElementById('outer6').insertAdjacentHTML('beforeend', '<img name=\"image1\">');\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 9
            + "    document.getElementById('image3').setAttribute('name', 'image1');\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 10
            + "    var newAttr = document.createAttribute('name');\n"
            + "    newAttr.nodeValue = 'image1';\n"
            + "    document.getElementById('image4').setAttributeNode(newAttr);\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 11
            + "    document.getElementById('image5').setAttributeNS(null, 'name', 'image1');\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 10
            + "    document.getElementById('outer1').removeChild(newImage1);\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <img name='image1'>\n"
            + "  <img name='image1'>\n"
            + "  <div id='outer1'></div>\n"
            + "  <div id='outer2'></div>\n"
            + "  <div id='outer3'><div id='inner3'></div></div>\n"
            + "  <div id='outer4'></div>\n"
            + "  <div id='outer5'></div>\n"
            + "  <div id='outer6'></div>\n"
            + "  <img id='image2'>\n"
            + "  <img id='image3'>\n"
            + "  <img id='image4'>\n"
            + "  <img id='image5'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2-2", "3-3"})
    public void elementsByName_changedAfterGet2() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            // 2
            + "    var collection1 = window.image1;\n"
            + "    var collection2 = image1;\n"
            + "    if (!collection1) {\n"
            + "      collection1 = [];\n"
            + "    }\n"
            + "    if (!collection2) {\n"
            + "      collection2 = [];\n"
            + "    }\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 3
            + "    document.getElementById('image2').name = 'image1';\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <img name='image1'>\n"
            + "  <img name='image1'>\n"
            + "  <img id='image2'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"form1", "form1", "f1", "f1", "input1", "input1", "anchor1", "anchor1", "image1",
                "image1", "element1", "element1"})
    public void elementsById() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(window.form1.id);\n"
            + "    alert(form1.id);\n"
            + "    alert(window.frame1.name);\n"
            + "    alert(frame1.name);\n"
            + "    alert(window.input1.id);\n"
            + "    alert(input1.id);\n"
            + "    alert(window.anchor1.id);\n"
            + "    alert(anchor1.id);\n"
            + "    alert(window.image1.id);\n"
            + "    alert(image1.id);\n"
            + "    alert(window.element1.id);\n"
            + "    alert(element1.id);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form id='form1'></form>\n"
            + "  <iframe id='frame1' name='f1'></iframe>\n"
            + "  <input type='text' id='input1' value='1'/>\n"
            + "  <a id='anchor1'></a>\n"
            + "  <img id='image1'>\n"
            + "  <div id='element1'></table>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"f1", "f1"})
    public void frameById() throws Exception {
        final String html = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\""
            + "\"http://www.w3.org/TR/html4/frameset.dtd\">\n"
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(window.frame1.name);\n"
            + "    alert(frame1.name);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<frameset onload='test()'>\n"
            + "  <frame src='" + URL_SECOND + "' id='frame1' name='f1'>\n"
            + "</frameset>\n"
            + "</html>";

        final String frame = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head><title>frame</title></head><body></body></html>";
        getMockWebConnection().setDefaultResponse(frame);

        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.execScript method gets called correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void execScript() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      window.execScript('alert(\"JavaScript\")', 'JavaScript');\n"
            + "      window.execScript('alert(\"JScript\")',    'JScript');\n"
            + "      try {\n"
            + "        window.execScript('alert(\"VBScript\")', 'VBScript');\n"
            + "      } catch (e) { alert('exception1'); }\n"
            + "      try {\n"
            + "        window.execScript('alert(\"BadLanguage\")', 'BadLanguage');\n"
            + "      } catch (e) {\n"
            + "        alert('exception2: ' + e.message.substr(0, 20)); // msg now contains info on error location\n"
            + "      }\n"
            + "    } catch (e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'>blah</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"test2", "test"})
    public void onLoadFunction() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test()\n"
            + "  {\n"
            + "    alert('test');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<script>\n"
            + "  var oldOnLoad = window.onload;\n"
            + "  window.onload = test2;\n"
            + "  function test2()\n"
            + "  {\n"
            + "    alert('test2');\n"
            + "    oldOnLoad();\n"
            + "  }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that you can set window.onload to something else than a function.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"a", "null"})
    public void onloadNotAFunction() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body><script>\n"
            + "window.onload = new function() {alert('a')};\n"
            + "window.onload = undefined;\n"
            + "alert(window.onload);\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "test1", "test2", "onload"})
    public void addOnLoadEventListener() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test1() {alert('test1');}\n"
            + "  function test2() {alert('test2');}\n"
            + "  function test3() {alert('test3');}\n"
            + "  alert(window.addEventListener == null);\n"
            + "  alert(window.removeEventListener == null);\n"
            + "  window.addEventListener('load', test1, true);\n"
            + "  window.addEventListener('load', test1, true);\n"
            + "  window.addEventListener('load', test2, true);\n"
            + "  window.addEventListener('load', test3, true);\n"
            + "  window.removeEventListener('load', test3, true);\n"
            + "</script></head>\n"
            + "<body onload='alert(\"onload\")'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "exception", "onload"})
    public void attachOnLoadEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test1(_e) {alert('test1, param null: ' + (_e == null));}\n"
            + "  function test2() {alert('test2');}\n"
            + "  function test3() {alert('test3');}\n"
            + "  alert(window.attachEvent == null);\n"
            + "  alert(window.detachEvent == null);\n"
            + "  try {\n"
            + "    window.attachEvent('onload', test1);\n"
            + "    window.attachEvent('onload', test1);\n"
            + "    window.attachEvent('onload', test2);\n"
            + "    window.attachEvent('onload', test3);\n"
            + "    window.detachEvent('onload', test3);\n"
            + "  } catch (e) { alert('exception'); }\n"
            + "</script></head>\n"
            + "<body onload='alert(\"onload\")'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void detachEventInAttachEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  window.detachEvent('onload', test);\n"
            + "  alert('detached');\n"
            + "}\n"
            + "try {\n"
            + "  window.attachEvent('onload', test);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test <code>window.name</code>.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"window.name before: ", "window.name after: main"})
    public void windowName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>window.name test</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  alert('window.name before: ' + window.name);\n"
            + "  window.name = 'main';\n"
            + "  alert('window.name after: ' + window.name);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests viewport properties.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"number", "number", "number", "number"})
    public void viewport() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  alert(typeof window.innerWidth);\n"
            + "  alert(typeof window.innerHeight);\n"
            + "  alert(typeof window.outerWidth);\n"
            + "  alert(typeof window.outerHeight);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test the <tt>Referer</tt> HTTP header by <tt>window.open</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§",
            IE = {})
    public void openWindow_refererHeader() throws Exception {
        final String firstContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head></head>\n"
            + "<body>\n"
            + "<button id='clickme' onClick='window.open(\"" + URL_SECOND + "\");'>Click me</a>\n"
            + "</body></html>";

        final String secondContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        expandExpectedAlertsVariables(URL_FIRST);
        final String[] expectedAlerts = getExpectedAlerts();
        setExpectedAlerts();

        final WebDriver driver = loadPageWithAlerts2(firstContent);

        driver.findElement(By.id("clickme")).click();

        final Map<String, String> lastAdditionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        if (expectedAlerts.length == 0) {
            assertNull(lastAdditionalHeaders.get(HttpHeader.REFERER));
        }
        else {
            assertEquals(expectedAlerts[0], lastAdditionalHeaders.get(HttpHeader.REFERER));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void evalScopeOtherWindow() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "  <iframe src='iframe.html'></iframe>\n"
            + "</body></html>";
        final String iframe = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<script>\n"
            + "  window.parent.eval('var foo = 1');\n"
            + "  alert(window.parent.foo);\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(iframe);
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for #408 JavaScript: window.eval does evaluate local scope.
     * See http://sourceforge.net/p/htmlunit/bugs/408/.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"elementValue", "elementValue", "elementValue"})
    public void evalScopeLocal() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body><form id='formtest'><input id='element' value='elementValue'/></form>\n"
            + "<script>\n"
            + "var docPatate = 'patate';\n"
            + "function test() {\n"
            + "  var f = document.forms['formtest'];\n"
            + "  alert(eval(\"document.forms['formtest'].element.value\"));\n"
            + "  alert(f.element.value);\n"
            + "  alert(eval('f.element.value'));\n"
            + "}\n"
            + "test();\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that eval() works correctly when triggered from an event handler. Event handlers are
     * executed in a child scope of the global window scope, so variables set from inside eval()
     * should go to this child scope, and not to the window scope.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("string")
    public void evalScopeEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "     var s = 'string';\n"
            + "     var f = 'initial';\n"
            + "     eval('f = function() {alert(s);}');\n"
            + "     invoke(f);\n"
            + "   }\n"
            + "   function invoke(fn) {\n"
            + "     fn();\n"
            + "   }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void functionEquality() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<script>\n"
            + "  alert(window.focus == window.focus);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for Bug #283.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"123", "captured"})
    public void captureEvents() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function t() { alert('captured'); }\n"
            + "  window.captureEvents(Event.CLICK);\n"
            + "  window.onclick = t;\n"
            + "</script></head><body>\n"
            + "<div id='theDiv' onclick='alert(123)'>foo</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(content);

        driver.findElement(By.id("theDiv")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Verifies that the {@code onload} handler is executed with {@code this} referring to the window.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void onLoadContext() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body><script>\n"
            + "var x = function() { alert(this==window) };\n"
            + "window.onload = x;\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Added test for Bug #485.  Bad context in evaluation of the JavaScript.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("INPUT")
    public void eval() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<input type='button' id='myButton' value='Click Me' onclick='test(this)'>\n"
            + "<script>\n"
            + "function test(f) {\n"
            + "  alert(eval('f.tagName'));\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(content);

        driver.findElement(By.id("myButton")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined", "true"})
    public void undefinedProperty() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(window['something']);\n"
            + "    alert(typeof window['something']);\n"
            + "    alert(typeof window['something']=='undefined');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void frames() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>First</title></head>\n"
            + "<frameset id='fs' rows='20%,*'>\n"
            + "  <frame name='top' src='" + URL_SECOND + "' />\n"
            + "  <frame name='bottom' src='about:blank' />\n"
            + "</frameset>\n"
            + "</html>";

        final String frameContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>TopFrame</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var bottomFrame = window.top.frames['bottom'];\n"
            + "  bottomFrame.location = 'about:blank';\n"
            + "}</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, frameContent);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void openWindow_numericName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  var w1 = window.open('about:blank', 1);\n"
            + "  alert(w1 != null);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<iframe name='myFrame' id='myFrame'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("about:blank")
    public void openWindow_aboutblank_location() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  var win = window.open('about:blank', 'test');\n"
            + "  alert(win.location);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("about:blank")
    public void openWindow_empty_location() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  var win = window.open('', 'test');\n"
            + "  alert(win.location);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§img.gif")
    public void openWindow_aboutblank_img() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  var win = window.open('about:blank', 'test', '');\n"
            + "  win.document.open();\n"
            + "  win.document.writeln('<img id=\"myImg\" src=\"img.gif\" />');\n"
            + "  win.document.close();\n"
            + "  alert(win.document.getElementById('myImg').src);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found",
                com.gargoylesoftware.htmlunit.util.MimeType.TEXT_HTML);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§img.gif")
    public void openWindow_aboutblank_document_img() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  var win = window.open('about:blank', 'test');\n"
            + "  win.document.open();\n"
            + "  win.document.writeln('<html><head></head><body><img id=\"myImg\" src=\"img.gif\" /></body></html>');\n"
            + "  win.document.close();\n"
            + "  win.focus();\n"
            + "  alert(win.document.getElementById('myImg').src);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found",
                com.gargoylesoftware.htmlunit.util.MimeType.TEXT_HTML);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§img.gif")
    public void openWindow_empty_img() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  var win = window.open('', 'test');\n"
            + "  win.document.open();\n"
            + "  win.document.writeln('<img id=\"myImg\" src=\"img.gif\" />');\n"
            + "  win.document.close();\n"
            + "  alert(win.document.getElementById('myImg').src);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found",
                com.gargoylesoftware.htmlunit.util.MimeType.TEXT_HTML);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "error")
    public void stop() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    window.stop();\n"
            + "    alert(true);\n"
            + "  } catch (e) {\n"
            + "    alert('error');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void open() throws Exception {
        final String firstHtml = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head></head>\n"
            + "<body>\n"
            + "<button id='clickme' onClick='window.open(new String(\"" + URL_SECOND + "\"));'>Click me</a>\n"
            + "</body></html>";

        final String secondHtml = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final WebDriver driver = loadPageWithAlerts2(firstHtml);

        driver.findElement(By.id("clickme")).click();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "First",
            IE = "Second")
    public void navigate() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "    if (window.navigate) {\n"
            + "      window.navigate('" + URL_SECOND + "');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent, URL_FIRST);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void devicePixelRatio() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(window.devicePixelRatio);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "true",
            EDGE = "true",
            IE = "auto")
    public void offscreenBuffering() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(window.offscreenBuffering);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "no exception")
    @HtmlUnitNYI(IE = "exception")
    public void getComputedStyle() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      getComputedStyle(void 0);\n"
            + "      alert('no exception');\n"
            + "    } catch (e) {alert('exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests the ordering of DOMContentLoaded for window and document
     * as well as how capturing / bubbling phases are handled.
     * Tests the ordering of load for window and document, and how they
     * relate to the onload property of 'body'.
     * Verifies handling of the at target phase.
     * Checks the state of event.eventPhase for a non-bubbling event after the bubbling phase.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"window DOMContentLoaded 1 capture",
                        "window DOMContentLoaded 2 capture",
                        "document DOMContentLoaded 1",
                        "document DOMContentLoaded 1 capture",
                        "document DOMContentLoaded 2",
                        "document DOMContentLoaded 2 capture",
                        "window DOMContentLoaded 1",
                        "window DOMContentLoaded 2",
                        "window at load 1",
                        "window at load 1 capture",
                        "window at load 2",
                        "onload 2",
                        "window at load 2 capture",
                        "after"},
            IE = {"window DOMContentLoaded 1 capture",
                        "window DOMContentLoaded 2 capture",
                        "document DOMContentLoaded 1",
                        "document DOMContentLoaded 1 capture",
                        "document DOMContentLoaded 2",
                        "document DOMContentLoaded 2 capture",
                        "window DOMContentLoaded 1",
                        "window DOMContentLoaded 2",
                        "window at load 1",
                        "window at load 1 capture",
                        "window at load 2",
                        "onload 2",
                        "window at load 2 capture",
                        "document at load 1 capture",
                        "document at load 2 capture",
                        "document at load 1 capture",
                        "document at load 2 capture",
                        "after"})
    @NotYetImplemented(IE) // The extra 'document at load' from <script> not yet handled (waiting on onloadScript())
    public void onload() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    window.parent.document.title += msg + ';';\n"
            + "  }\n"

            // These 'load' events and 'onload' property below target 'document' when fired
            // but path 'window' only. (Chrome/FF)
            // This is unlike other events where the path always includes the target and
            // all ancestors up to 'window'. Ascertaining this is possible by inspecting
            // the 'event' object which is a property of 'window' in Chrome, or
            // obtained via the first parameter of the event function in FF: e.g. function (event) { log('xyz', event) }
            + "  window.addEventListener('load', function () { log('window at load 1') })\n"

            // This 'onload' callback is called when the 'load' event is fired.
            // Ordering of the call is preserved with respect to other 'load' callbacks and is relative to
            // the position the property is set.  Subsequent overwriting of 'window.onload' with another
            // valid function does not move this position.  However, setting 'window.onload' to null or a
            // non-function value will reset the position and a new position us determined the next time
            // the property is set. The 'body' tag with an 'onload' property behaves synonymously as
            // writing 'window.onload = function () { ... }'
            // at the position the 'body' tag appears.
            //window.onload = function () { log('onload 1') }

            + "  window.addEventListener('load', function () { log('window at load 1 capture') }, true)\n"
            // This 'DOMContentLoaded' event targets 'document' and paths [window, document] as expected. (Chrome/FF)
            + "  window.addEventListener('DOMContentLoaded', function () { log('window DOMContentLoaded 1') })\n"
            + "  window.addEventListener('DOMContentLoaded', "
                    + "function () { log('window DOMContentLoaded 1 capture') }, true)\n"

            + "  document.addEventListener('load', function () { log('document at load 1') })\n"
            + "  document.addEventListener('load', function () { log('document at load 1 capture') }, true)\n"
            + "  document.addEventListener('DOMContentLoaded', function () { log('document DOMContentLoaded 1') })\n"
            + "  document.addEventListener('DOMContentLoaded', "
                    + "function () { log('document DOMContentLoaded 1 capture') }, true)\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  window.addEventListener('load', function () { log('window at load 2') })\n"
            //window.onload = null
            //window.onload = 123
            + "  window.onload = function () { log('onload 2') }\n"
            + "  window.addEventListener('load', function () { log('window at load 2 capture') }, true)\n"
            + "  window.addEventListener('DOMContentLoaded', function () { log('window DOMContentLoaded 2') })\n"
            + "  window.addEventListener('DOMContentLoaded', "
                    + "function () { log('window DOMContentLoaded 2 capture') }, true)\n"

            + "  document.addEventListener('load', function () { log('document at load 2 capture') }, true)\n"
            + "  document.addEventListener('DOMContentLoaded', function () { log('document DOMContentLoaded 2') })\n"
            + "  document.addEventListener('DOMContentLoaded', "
                    + "function () { log('document DOMContentLoaded 2 capture') }, true)\n"

            // This is for testing the state of event.eventPhase afterwards
            + "  window.addEventListener('load', "
                    + "function (event) { var x = event; "
                        + "window.setTimeout(function () { log('after', x.eventPhase) }, 100) }, true)\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        Thread.sleep(200);
        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * Tests load and error events of 'script' tags.
     * Checks that they should be using EventTarget.fireEvent()
     * rather than Event.executeEventLocally().
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"document at load capture",
                        "element 1 onload",
                        "window at error capture",
                        "document at error capture",
                        "element 2 onerror",
                        "document DOMContentLoaded",
                        "window DOMContentLoaded",
                        "window at load",
                        "window at load capture",
                        "body onload"},
            IE = {"window at error",
                        "window at error capture",
                        "document at load capture",
                        "element 1 onload",
                        "window at error capture",
                        "document at error capture",
                        "element 2 onerror",
                        "document DOMContentLoaded",
                        "window DOMContentLoaded",
                        "window at load",
                        "window at load capture",
                        "body onload",
                        "document at load capture"})
    @NotYetImplemented
    public void onloadScript() throws Exception {
        getMockWebConnection().setResponse(URL_SECOND, "");

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    window.parent.document.title += msg + ';';\n"
            + "  }\n"

            + "  window.addEventListener('load', function () { log('window at load') })\n"
            + "  window.addEventListener('load', function () { log('window at load capture') }, true)\n"
            + "  window.addEventListener('error', function () { log('window at error') })\n"
            + "  window.addEventListener('error', function () { log('window at error capture') }, true)\n"
            + "  window.addEventListener('DOMContentLoaded', function () { log('window DOMContentLoaded') })\n"

            + "  document.addEventListener('load', function () { log('document at load') })\n"
            + "  document.addEventListener('load', function () { log('document at load capture') }, true)\n"
            + "  document.addEventListener('error', function () { log('document at error') })\n"
            + "  document.addEventListener('error', function () { log('document at error capture') }, true)\n"
            + "  document.addEventListener('DOMContentLoaded', function () { log('document DOMContentLoaded') })\n"

            + "</script>\n"
            + "</head>\n"
            + "<body onload='log(\"body onload\")'>\n"
            + "  <script src='" + URL_SECOND + "' onload='log(\"element 1 onload\")' "
                                        + "onerror='log(\"element 1 onerror\")'></script>\n"
            + "  <script src='missing.txt' onload='log(\"element 2 onload\")' "
                                        + "onerror='log(\"element 2 onerror\")'></script>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found",
                com.gargoylesoftware.htmlunit.util.MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(html);
        Thread.sleep(200);
        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * Tests load and error events of 'img' tags.
     * Checks that they should be using EventTarget.fireEvent()
     * rather than Event.executeEventLocally().
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"img2: window at error capture",
                        "img2: document at error capture",
                        "img2: element 2 onerror",
                        "#document: document DOMContentLoaded",
                        "#document: window DOMContentLoaded",
                        "img1: document at load capture",
                        "img1: element 1 onload",
                        "#document: window at load",
                        "#document: window at load capture",
                        "#document: body onload"},
            CHROME = {"#document: document DOMContentLoaded",
                        "#document: window DOMContentLoaded",
                        "img2: window at error capture",
                        "img2: document at error capture",
                        "img2: element 2 onerror",
                        "img1: document at load capture",
                        "img1: element 1 onload",
                        "#document: window at load",
                        "#document: window at load capture",
                        "#document: body onload"},
            IE = {"img1: document at load capture",
                        "img1: element 1 onload",
                        "#document: document DOMContentLoaded",
                        "#document: window DOMContentLoaded",
                        "#document: window at load",
                        "#document: window at load capture",
                        "#document: body onload",
                        "SCRIPT: document at load capture",
                        "img2: window at error capture",
                        "img2: document at error capture",
                        "img2: element 2 onerror"})
    @NotYetImplemented(IE) // The extra SCRIPT is not yet handled (waiting on onloadScript())
    public void onloadImg() throws Exception {
        final URL urlImage = new URL(URL_FIRST, "img.jpg");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function log(msg, target) {\n"
            + "    if (target) msg = (target.id ? target.id : target.nodeName) + ': ' + msg\n"
            + "    window.parent.document.title += msg + ';';\n"
            + "  }\n"

            + "  window.addEventListener('load', function (event) { log('window at load', event.target) })\n"
            + "  window.addEventListener('load', function (event) { "
                                                    + "log('window at load capture', event.target) }, true)\n"
            + "  window.addEventListener('error', function (event) { log('window at error', event.target) })\n"
            + "  window.addEventListener('error', function (event) { "
                                                    + "log('window at error capture', event.target) }, true)\n"
            + "  window.addEventListener('DOMContentLoaded', function (event) { "
                                                    + "log('window DOMContentLoaded', event.target) })\n"

            + "  document.addEventListener('load', function (event) { log('document at load', event.target) })\n"
            + "  document.addEventListener('load', function (event) { "
                                                    + "log('document at load capture', event.target) }, true)\n"
            + "  document.addEventListener('error', function (event) { log('document at error', event.target) })\n"
            + "  document.addEventListener('error', function (event) { "
                                                    + "log('document at error capture', event.target) }, true)\n"
            + "  document.addEventListener('DOMContentLoaded', function (event) { "
                                                    + "log('document DOMContentLoaded', event.target) })\n"

            + "</script>\n"
            + "</head>\n"
            + "<body onload='log(\"body onload\", document)'>\n"
            + "  <img id='img1' src='" + urlImage + "' onload='log(\"element 1 onload\", this)' "
                                       + "onerror='log(\"element 1 onerror\", this)'>\n"
            + "  <img id='img2' src='' onload='log(\"element 2 onload\", this)' "
                                       + "onerror='log(\"element 2 onerror\", this)'>\n"

            + "</body></html>";

        // Image loads are usually asynchronous in browsers and ordering of these results are somewhat coincidental.
        //
        // Chrome/FF appears be synchronizing 'body.onload' so that it comes after all image loads, while Chrome
        // alone also seems to be synchronizing 'DOMContentLoaded' so that it fires before image loads.  IE11 is a mess
        // with no clear synchronization between 'body.onload' and image loads.
        //
        // To preserve a semblance of sanity, we're sorting the results because this test isn't concerned with the
        // ordering of img1 vs img2 vs #document (or even images vs 'body.onload'), and instead are only concerned
        // the events propagate in correct order once triggered.
        final Comparator<String> sorter = Comparator.comparing(s -> StringUtils.substringBefore(s, ":"));

        final WebDriver driver = loadPage2(html);
        Thread.sleep(200);
        final String text = Pattern.compile(";").splitAsStream(driver.getTitle())
                .map(String::trim).sorted(sorter).collect(Collectors.joining("\n"));
        final String expected = Arrays.stream(getExpectedAlerts()).sorted(sorter).collect(Collectors.joining("\n"));
        assertEquals(expected, text);
    }

    /**
     * Same as {@link #onload()} but from frame.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"framing window DOMContentLoaded 1 capture",
                        "framing document DOMContentLoaded 1",
                        "framing document DOMContentLoaded 1 capture",
                        "framing window DOMContentLoaded 1",
                        "window DOMContentLoaded 1 capture",
                        "window DOMContentLoaded 2 capture",
                        "document DOMContentLoaded 1",
                        "document DOMContentLoaded 1 capture",
                        "document DOMContentLoaded 2",
                        "document DOMContentLoaded 2 capture",
                        "window DOMContentLoaded 1",
                        "window DOMContentLoaded 2",
                        "window at load 1",
                        "window at load 1 capture",
                        "window at load 2",
                        "onload 2",
                        "window at load 2 capture",
                        "framing document at load 1 capture",
                        "frame onload",
                        "framing window at load 1",
                        "framing window at load 1 capture",
                        "frameset onload",
                        "after"},
            IE = {"framing window DOMContentLoaded 1 capture",
                        "framing document DOMContentLoaded 1",
                        "framing document DOMContentLoaded 1 capture",
                        "framing window DOMContentLoaded 1",
                        "framing document at load 1 capture",
                        "window DOMContentLoaded 1 capture",
                        "window DOMContentLoaded 2 capture",
                        "document DOMContentLoaded 1",
                        "document DOMContentLoaded 1 capture",
                        "document DOMContentLoaded 2",
                        "document DOMContentLoaded 2 capture",
                        "window DOMContentLoaded 1",
                        "window DOMContentLoaded 2",
                        "window at load 1",
                        "window at load 1 capture",
                        "window at load 2",
                        "onload 2",
                        "window at load 2 capture",
                        "framing document at load 1 capture",
                        "frame onload",
                        "framing window at load 1",
                        "framing window at load 1 capture",
                        "frameset onload",
                        "document at load 1 capture",
                        "document at load 2 capture",
                        "document at load 1 capture",
                        "document at load 2 capture",
                        "after"})
    @NotYetImplemented(IE) // The extra 'document at load' from <script> not yet handled (waiting on onloadScript())
    public void onloadFrame() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    window.parent.document.title += msg + ';';\n"
            + "  }\n"

            + "  window.addEventListener('load', function () { log('window at load 1') })\n"

            + "  window.addEventListener('load', function () { log('window at load 1 capture') }, true)\n"
            + "  window.addEventListener('DOMContentLoaded', function () { log('window DOMContentLoaded 1') })\n"
            + "  window.addEventListener('DOMContentLoaded', "
                    + "function () { log('window DOMContentLoaded 1 capture') }, true)\n"

            + "  document.addEventListener('load', function () { log('document at load 1') })\n"
            + "  document.addEventListener('load', function () { log('document at load 1 capture') }, true)\n"
            + "  document.addEventListener('DOMContentLoaded', function () { log('document DOMContentLoaded 1') })\n"
            + "  document.addEventListener('DOMContentLoaded', "
                    + "function () { log('document DOMContentLoaded 1 capture') }, true)\n"
            + "</script>\n"
            + "</head>\n"
            + "<body >\n"
            + "<script>\n"
            + "  window.addEventListener('load', function () { log('window at load 2') })\n"
            + "  window.onload = function () { log('onload 2') }\n"
            + "  window.addEventListener('load', function () { log('window at load 2 capture') }, true)\n"
            + "  window.addEventListener('DOMContentLoaded', function () { log('window DOMContentLoaded 2') })\n"
            + "  window.addEventListener('DOMContentLoaded', "
                    + "function () { log('window DOMContentLoaded 2 capture') }, true)\n"

            + "  document.addEventListener('load', function () { log('document at load 2 capture') }, true)\n"
            + "  document.addEventListener('DOMContentLoaded', function () { log('document DOMContentLoaded 2') })\n"
            + "  document.addEventListener('DOMContentLoaded', "
                    + "function () { log('document DOMContentLoaded 2 capture') }, true)\n"

            + "  window.addEventListener('load', "
                    + "function (event) { var x = event; "
                        + "window.setTimeout(function () { log('after', x.eventPhase) }, 100) }, true)\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(content);

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head>\n"
                + "<script>\n"
                + "  function log(msg) {\n"
                + "    window.document.title += msg + ';';\n"
                + "  }\n"

                + "  window.addEventListener('load', function () { log('framing window at load 1') })\n"
                + "  window.addEventListener('load', function () { log('framing window at load 1 capture') }, true)\n"
                + "  window.addEventListener('DOMContentLoaded', "
                            + "function () { log('framing window DOMContentLoaded 1') })\n"
                + "  window.addEventListener('DOMContentLoaded', "
                            + "function () { log('framing window DOMContentLoaded 1 capture') }, true)\n"

                // should not fire because bubbles = false
                + "  document.addEventListener('load', "
                            + "function () { log('framing document at load 1') })\n"
                + "  document.addEventListener('load', "
                            + "function () { log('framing document at load 1 capture') }, true)\n"
                + "  document.addEventListener('DOMContentLoaded', "
                            + "function () { log('framing document DOMContentLoaded 1') })\n"
                + "  document.addEventListener('DOMContentLoaded', "
                            + "function () { log('framing document DOMContentLoaded 1 capture') }, true)\n"
                + "</script>\n"
                + "</head>\n"
                + "<frameset onload='log(\"frameset onload\")'>\n"
                + "<frame src='test_onload.html' onload='log(\"frame onload\")'>\n"
                + "</frameset>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        Thread.sleep(200);
        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function () { log(\"onload from body\") }",
                "function () { log(\"onload from body\") }",
                "function () { log(\"onload from window\") }",
                "function () { log(\"onload from window\") }",
                "null",
                "null",
                "function () { log(\"onload from body\") }",
                "function () { log(\"onload from body\") }",
                "onload from body"})
    public void onloadFromBody() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    window.parent.document.title += ('' + msg).replace(';', '') + ';';\n"
            + "  }\n"

            + "  document.body.onload = function () { log(\"onload from body\") };\n"
            + "  log(document.body.onload);\n"
            + "  log(window.onload);\n"

            + "  window.onload = function () { log(\"onload from window\") };\n"
            + "  log(document.body.onload);\n"
            + "  log(window.onload);\n"

            + "  window.onload = undefined;\n"
            + "  log(document.body.onload);\n"
            + "  log(window.onload);\n"

            + "  document.body.onload = function () { log(\"onload from body\") };\n"
            + "  log(document.body.onload);\n"
            + "  log(window.onload);\n"

            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void onloadListenerFromBody() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    window.parent.document.title += msg + ';';\n"
            + "  }\n"

            + "  document.body.addEventListener(\"load\", function () { log(\"onload from body\") });\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("onload from window")
    public void onloadListenerFromBodyAndWindow() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    window.parent.document.title += msg + ';';\n"
            + "  }\n"

            + "  document.body.addEventListener(\"load\", function () { log(\"onload from body\") });\n"
            + "  window.addEventListener(\"load\", function () { log(\"onload from window\") });\n"

            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void onloadListenerFromBodyAndWindowRemoved() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    window.parent.document.title += msg + ';';\n"
            + "  }\n"

            + "  document.body.addEventListener(\"load\", function () { log(\"onload from body\") });\n"
            + "  function evt() { log(\"onload from window\") }"
            + "  window.addEventListener(\"load\", evt);\n"
            + "  window.removeEventListener(\"load\", evt);\n"

            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * Tests propagation of a more or less basic event (click event) with regards to
     * handling of the capturing / bubbling / at target phases.
     * Tests listener and property handler ordering.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"window at click 1 capture",
                "window at click 2 capture",
                "onclick 2",
                "i1 at click 1",
                "i1 at click 1 capture",
                "i1 at click 2",
                "i1 at click 2 capture",
                "window at click 1",
                "window at click 2"})
    public void propagation() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    window.parent.document.title += msg + ';';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input id='tester' type='button' value='test' onclick='log(\"onclick\")'>\n"

            + "<script>\n"
            + "  window.addEventListener('click', function () { log('window at click 1') })\n"
            + "  window.addEventListener('click', function () { log('window at click 1 capture') }, true)\n"
            + "  window.addEventListener('click', function () { log('window at click 2') })\n"
            + "  window.addEventListener('click', function () { log('window at click 2 capture') }, true)\n"

            + "  tester.addEventListener('click', function () { log('i1 at click 1') })\n"
            + "  tester.addEventListener('click', function () { log('i1 at click 1 capture') }, true)\n"
            + "  tester.addEventListener('click', function () { log('i1 at click 2') })\n"
            + "  tester.onclick = function () { log('onclick 2') }\n"
            + "  tester.addEventListener('click', function () { log('i1 at click 2 capture') }, true)\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("tester")).click();

        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * Similar as {@link #propagation()} except with a deeper propagation path.
     * Check bubbling propagation after modification of the DOM tree by an intermediate listener.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"window at click 1 capture",
                "window at click 2 capture",
                "d1 at click 1 capture",
                "d1 at click 2 capture",
                "d2 at click 1 capture",
                "d2 at click 2 capture",
                "d3 at click 1",
                "d3 onclick",
                "d3 at click 1 capture",
                "d3 at click 2",
                "d3 at click 2 capture",
                "d2 at click 1",
                "d2 onclick",
                "d2 at click 2",
                "d1 at click 1",
                "d1 onclick",
                "d1 at click 2",
                "window at click 1",
                "window at click 2"})
    public void propagationNested() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    window.parent.document.title += msg + ';';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div id='d1' style='width: 150px; height: 150px; background-color: blue'>\n"
            + "    <div id='d2' style='width: 100px; height: 100px; background-color: green'>\n"
            + "      <div id='d3' style='width: 50px; height: 50px; background-color: red'>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </div>\n"

            + "<script>\n"
            + "  window.addEventListener('click', function () { log('window at click 1') })\n"
            + "  window.addEventListener('click', function () { log('window at click 1 capture') }, true)\n"
            + "  window.addEventListener('click', function () { log('window at click 2') })\n"
            + "  window.addEventListener('click', function () { log('window at click 2 capture') }, true)\n"

            + "  d1.addEventListener('click', function () { log('d1 at click 1') })\n"
            + "  d1.onclick = function () { log('d1 onclick') }\n"
            + "  d1.addEventListener('click', function () { log('d1 at click 1 capture') }, true)\n"
            + "  d1.addEventListener('click', function () { log('d1 at click 2') })\n"
            + "  d1.addEventListener('click', function () { log('d1 at click 2 capture') }, true)\n"

            + "  d2.addEventListener('click', function () { log('d2 at click 1') })\n"
            + "  d2.onclick = function () { log('d2 onclick'); d2.parentNode.removeChild(d2) }\n"
            + "  d2.addEventListener('click', function () { log('d2 at click 1 capture') }, true)\n"
            + "  d2.addEventListener('click', function () { log('d2 at click 2') })\n"
            + "  d2.addEventListener('click', function () { log('d2 at click 2 capture') }, true)\n"

            + "  d3.addEventListener('click', function () { log('d3 at click 1') })\n"
            + "  d3.onclick = function () { log('d3 onclick') }\n"
            + "  d3.addEventListener('click', function () { log('d3 at click 1 capture') }, true)\n"
            + "  d3.addEventListener('click', function () { log('d3 at click 2') })\n"
            + "  d3.addEventListener('click', function () { log('d3 at click 2 capture') }, true)\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("d3")).click();

        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * Similar as {@link #propagationNested()} but clicking a detached element.
     * Check bubbling propagation after modification of the DOM tree by an intermediate listener.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"window at click 1 capture",
                "window at click 2 capture",
                "begin detach click",
                "d2 at click 1 capture",
                "d2 at click 2 capture",
                "d3 at click 1",
                "d3 onclick",
                "d3 at click 1 capture",
                "d3 at click 2",
                "d3 at click 2 capture",
                "d2 at click 1",
                "d2 onclick",
                "d2 at click 2",
                "end detach click",
                "window at click 1",
                "window at click 2"})
    public void propagationNestedDetached() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    window.parent.document.title += msg + ';';\n"
            + "  }\n"

            + "  function detachAndClick() {\n"
            + "    log('begin detach click')\n"
            + "    var d2 = window.d2, d3 = window.d3\n"
            + "    d2.parentNode.removeChild(d2);\n"
            + "    d3.click();\n"
            + "    log('end detach click')\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div id='d1' style='width: 150px; height: 150px; background-color: blue'>\n"
            + "    <div id='d2' style='width: 100px; height: 100px; background-color: green'>\n"
            + "      <div id='d3' style='width: 50px; height: 50px; background-color: red'>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "  <input id='detach_click' type='button' value='Detach & click' onclick='detachAndClick()'>\n"

            + "<script>\n"
            + "  d2 = window.d2, d3 = window.d3\n" // Save because "Detach & click" removes them
            + "  window.addEventListener('click', function () { log('window at click 1') })\n"
            + "  window.addEventListener('click', function () { log('window at click 1 capture') }, true)\n"
            + "  window.addEventListener('click', function () { log('window at click 2') })\n"
            + "  window.addEventListener('click', function () { log('window at click 2 capture') }, true)\n"

            + "  d1.addEventListener('click', function () { log('d1 at click 1') })\n"
            + "  d1.onclick = function () { log('d1 onclick') }\n"
            + "  d1.addEventListener('click', function () { log('d1 at click 1 capture') }, true)\n"
            + "  d1.addEventListener('click', function () { log('d1 at click 2') })\n"
            + "  d1.addEventListener('click', function () { log('d1 at click 2 capture') }, true)\n"

            + "  d2.addEventListener('click', function () { log('d2 at click 1') })\n"
            + "  d2.onclick = function () { log('d2 onclick'); if (d2.parentNode) d2.parentNode.removeChild(d2) }\n"
            + "  d2.addEventListener('click', function () { log('d2 at click 1 capture') }, true)\n"
            + "  d2.addEventListener('click', function () { log('d2 at click 2') })\n"
            + "  d2.addEventListener('click', function () { log('d2 at click 2 capture') }, true)\n"

            + "  d3.addEventListener('click', function () { log('d3 at click 1') })\n"
            + "  d3.onclick = function () { log('d3 onclick') }\n"
            + "  d3.addEventListener('click', function () { log('d3 at click 1 capture') }, true)\n"
            + "  d3.addEventListener('click', function () { log('d3 at click 2') })\n"
            + "  d3.addEventListener('click', function () { log('d3 at click 2 capture') }, true)\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("detach_click")).click();

        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * This test determines that the return value of listeners are apparently
     * ignored and only that of the property handler is used.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"listener: stop propagation & return false",
                "FIRED a1",
                "listener: return true",
                "property: return false",
                "listener: return true",
                "listener: prevented=false returnValue: true -> false (false)",
                "listener: prevented=true returnValue: false -> true (false)",
                "listener: prevented=true returnValue: false -> preventDefault() (false)",
                "property: prevented=true returnValue: false -> return true",
                "listener: prevented=true returnValue: false -> x (false)",
                "listener: prevented=true returnValue: false -> null (false)"},
            IE = {"listener: stop propagation & return false",
                "FIRED a1",
                "listener: return true",
                "property: return false",
                "listener: return true",
                "listener: prevented=false returnValue: undefined -> false (false)",
                "listener: prevented=false returnValue: false -> true (true)",
                "listener: prevented=false returnValue: true -> preventDefault() (true)",
                "property: prevented=true returnValue: true -> return true",
                "listener: prevented=true returnValue: true -> x (x)",
                "listener: prevented=true returnValue: x -> null (null)"})
    public void stopPropagation() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    window.parent.document.title += msg + ';';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div><a id='a1' href='javascript:log(\"FIRED a1\")'>test: listener return false</a></div>\n"
            + "  <div><a id='a2' href='javascript:log(\"FIRED a2\")'>test: property return false</a></div>\n"
            + "  <div><a id='a3' href='javascript:log(\"FIRED a3\")'>test: listener returnValue = false</a></div>\n"

            + "  <textarea id='log' rows=40 cols=80></textarea>\n"

            + "<script>\n"
            // The event.stopPropagation() has no bearing on whether 'return false'
            // below is effective at preventing "href" processing.
            + "  a1.addEventListener('click', function (event) { "
                                                + "log('listener: stop propagation & return false');"
                                                + "event.stopPropagation(); return false })\n"

            // The only return value that matters is the value from the 'onclick' property.  The 'return false' below
            // prevents "href' being processed.
            + "  a2.addEventListener('click',"
            + "        function (event) { log('listener: return true'); event.stopPropagation(); return true })\n"
            + "  a2.onclick = function () { log('property: return false'); return false }\n"
            + "  a2.addEventListener('click', function (event) { log('listener: return true'); return true })\n"

            // Uncommenting this causes a2 to fire because propagation is
            // stopped before 'onclick' property is processed.
            // Again, the 'return false' here is ineffective.
            // The return values of non-property handlers are probably ignored. (tested in Chrome/FF)
            //window.addEventListener("click", function (event) {
            //                  log('window: stop propagation & return false');
            //                  event.stopPropagation(); return false }, true)

            // In Chrome, this sets event.returnValue to 'false'
            // which is synonymous with setting 'event.defaultPrevented'
            // In FF/IE11, event.returnValue is settable but does not appear to be used for anything
            + "  a3.addEventListener('click', function (event) {"
            + "      var a = event.returnValue, p = event.defaultPrevented, b = false; event.returnValue = b;"
            + "      log('listener: prevented=' + p + ' returnValue: ' + a "
                                    + "+ ' -> ' + b + ' (' + event.returnValue + ')') })\n"
             // This shows it's possible to set event.returnValue back to 'true' from 'false'
            + "  a3.addEventListener('click', function (event) {"
            + "      var a = event.returnValue, p = event.defaultPrevented, b = true; event.returnValue = b;"
            + "      log('listener: prevented=' + p + ' returnValue: ' + a "
                                    + "+ ' -> ' + b + ' (' + event.returnValue + ')') })\n"
            // The value of event.returnValue is consistent across multiple listener calls of the same event
            + "  a3.addEventListener('click', function (event) {"
            + "      var a = event.returnValue, p = event.defaultPrevented, "
                                    + "b = 'preventDefault()'; event.preventDefault();"
            + "      log('listener: prevented=' + p + ' returnValue: ' + a "
                                    + "+ ' -> ' + b + ' (' + event.returnValue + ')') })\n"
            // This shows a property handler returning 'true' will not change event.returnValue if it's already 'false'
            + "  a3.onclick = function (event) {"
            + "      var a = event.returnValue, p = event.defaultPrevented; b = true;"
            + "      log('property: prevented=' + p + ' returnValue: ' + a + ' -> return ' + b); return b }\n"
            // Instead of returning 'true', the property handler can directly set
            // event.returnValue to set it to 'true' from 'false'
            //+ "  a3.onclick = function (event) {"
            //+ "      var a = event.returnValue, p = event.defaultPrevented; b = true;"
            //+ "      log('property: prevented=' + p + ' returnValue: ' + a + ' -> true'); event.returnValue = b }\n"
            // These shows setting event.returnValue cannot be set to a non-boolean
            // value in Chrome but can in (FF/IE11)
            + "  a3.addEventListener('click', function (event) {"
            + "        var a = event.returnValue, p = event.defaultPrevented, b = 'x'; event.returnValue = b;"
            + "        log('listener: prevented=' + p + ' returnValue: ' + a "
                                    + "+ ' -> ' + b + ' (' + event.returnValue + ')') })\n"
            + "  a3.addEventListener('click', function (event) {"
            + "        var a = event.returnValue, p = event.defaultPrevented, b = null; event.returnValue = b;"
            + "        log('listener: prevented=' + p + ' returnValue: ' + a "
                                    + "+ ' -> ' + b + ' (' + event.returnValue + ')') })\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("a1")).click();
        driver.findElement(By.id("a2")).click();
        driver.findElement(By.id("a3")).click();

        final String text = driver.getTitle().trim().replaceAll(";", "\n").trim();
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }
}
