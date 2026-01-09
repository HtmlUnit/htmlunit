/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.htmlunit.HttpHeader;
import org.htmlunit.MockWebConnection;
import org.htmlunit.WebClient;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HTMLIFrameElement}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HTMLIFrameElement3Test extends WebDriverTestCase {

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void style() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.getElementById('myIFrame').style == undefined);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe id='myIFrame' src='about:blank'></iframe>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "myIFrame"})
    public void referenceFromJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(window.frames.length);\n"
            + "  log(window.frames['myIFrame'].name);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe name='myIFrame' src='about:blank'></iframe></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 1562872.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"about:blank", "about:blank"})
    public void directAccessPerName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(myIFrame.location);\n"
            + "  log(Frame.location);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe name='myIFrame' src='about:blank'></iframe>\n"
            + "<iframe name='Frame' src='about:blank'></iframe>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests that the <tt>&lt;iframe&gt;</tt> node is visible from the contained page when it is loaded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("IFRAME")
    public void onLoadGetsIFrameElementByIdInParent() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<iframe id='myIFrame' src='frame.html'></iframe></body></html>";

        final String frameContent = DOCTYPE_HTML
            + "<html><head>\n"
            + "<title>Frame</title>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "function doTest() {\n"
            + "  log(parent.document.getElementById('myIFrame').tagName);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setDefaultResponse(frameContent);

        loadPage2(firstContent);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "true"})
    public void contentDocument() throws Exception {
        final String html = DOCTYPE_HTML
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
            + "<body onload='test()'>\n"
            + "  <iframe name='foo' id='myFrame' src='about:blank'></iframe>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void frameElement() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.getElementById('myFrame') == frames.foo.frameElement);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<iframe name='foo' id='myFrame' src='about:blank'></iframe>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that writing to an iframe keeps the same intrinsic variables around (window,
     * document, etc) and in a usable form. Bug detected via the jQuery 1.1.3.1 unit tests.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "true", "true", "true", "object", "object"})
    public void writeToIFrame() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var frame = document.createElement('iframe');\n"
            + "    document.body.appendChild(frame);\n"
            + "    var win = frame.contentWindow;\n"
            + "    var doc = frame.contentWindow.document;\n"
            + "    log(win == window);\n"
            + "    log(doc == document);\n"
            + "    \n"
            + "    doc.open();\n"
            + "    doc.write(\"<html><body><input type='text'/></body></html>\");\n"
            + "    doc.close();\n"
            + "    var win2 = frame.contentWindow;\n"
            + "    var doc2 = frame.contentWindow.document;\n"
            + "    log(win == win2);\n"
            + "    log(doc == doc2);\n"
            + "    \n"
            + "    var input = doc.getElementsByTagName('input')[0];\n"
            + "    var input2 = doc2.getElementsByTagName('input')[0];\n"
            + "    log(input == input2);\n"
            + "    log(typeof input);\n"
            + "    log(typeof input2);\n"
            + "  }\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that writing to an iframe keeps the same intrinsic variables around (window,
     * document, etc) and in a usable form. Bug detected via the jQuery 1.1.3.1 unit tests.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"123", "undefined"})
    public void iFrameReinitialized() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  <a id='test' href='2.html' target='theFrame'>page 2 in frame</a>\n"
            + "  <iframe name='theFrame' src='1.html'></iframe>\n"
            + "</body></html>";

        final String frame1 = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_WINDOW_NAME_FUNCTION
                + "window.foo = 123; log(window.foo);\n"
                + "</script>\n"
                + "</head></html>";
        final String frame2 = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_WINDOW_NAME_FUNCTION
                + "log(window.foo);\n"
                + "</script>\n"
                + "</head></html>";

        final String[] alerts = getExpectedAlerts();
        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setResponse(new URL(URL_FIRST, "1.html"), frame1);
        webConnection.setResponse(new URL(URL_FIRST, "2.html"), frame2);

        final WebDriver driver = loadPage2(html);
        verifyWindowName2(driver, alerts[0]);

        driver.findElement(By.id("test")).click();
        verifyWindowName2(driver, alerts[1]);

        assertEquals(3, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("about:blank")
    public void setSrc_JavascriptUrl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    document.getElementById('iframe1').src = 'javascript:void(0)';\n"
            + "    log(window.frames[0].location);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<iframe id='iframe1'></iframe>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "100", "foo", "20%", "-5", "30.2", "400", "abc", "-5", "100.2", "10%", "-12.56"})
    public void width() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<iframe id='i1'></iframe>\n"
            + "<iframe id='i2' width='100'></iframe>\n"
            + "<iframe id='i3' width='foo'></iframe>\n"
            + "<iframe id='i4' width='20%'></iframe>\n"
            + "<iframe id='i5' width='-5'></iframe>\n"
            + "<iframe id='i6' width='30.2'></iframe>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function set(e, value) {\n"
            + "  try {\n"
            + "    e.width = value;\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "var i1 = document.getElementById('i1');\n"
            + "var i2 = document.getElementById('i2');\n"
            + "var i3 = document.getElementById('i3');\n"
            + "var i4 = document.getElementById('i4');\n"
            + "var i5 = document.getElementById('i5');\n"
            + "var i6 = document.getElementById('i6');\n"
            + "log(i1.width);\n"
            + "log(i2.width);\n"
            + "log(i3.width);\n"
            + "log(i4.width);\n"
            + "log(i5.width);\n"
            + "log(i6.width);\n"
            + "set(i1, '400');\n"
            + "set(i2, 'abc');\n"
            + "set(i3, -5);\n"
            + "set(i4, 100.2);\n"
            + "set(i5, '10%');\n"
            + "set(i6, -12.56);\n"
            + "log(i1.width);\n"
            + "log(i2.width);\n"
            + "log(i3.width);\n"
            + "log(i4.width);\n"
            + "log(i5.width);\n"
            + "log(i6.width);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "100", "foo", "20%", "-5", "30.2", "400", "abc", "-5", "100.2", "10%", "-12.56"})
    public void height() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<iframe id='i1'></iframe>\n"
            + "<iframe id='i2' height='100'></iframe>\n"
            + "<iframe id='i3' height='foo'></iframe>\n"
            + "<iframe id='i4' height='20%'></iframe>\n"
            + "<iframe id='i5' height='-5'></iframe>\n"
            + "<iframe id='i6' height='30.2'></iframe>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function set(e, value) {\n"
            + "  try {\n"
            + "    e.height = value;\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "var i1 = document.getElementById('i1');\n"
            + "var i2 = document.getElementById('i2');\n"
            + "var i3 = document.getElementById('i3');\n"
            + "var i4 = document.getElementById('i4');\n"
            + "var i5 = document.getElementById('i5');\n"
            + "var i6 = document.getElementById('i6');\n"
            + "log(i1.height);\n"
            + "log(i2.height);\n"
            + "log(i3.height);\n"
            + "log(i4.height);\n"
            + "log(i5.height);\n"
            + "log(i6.height);\n"
            + "set(i1, '400');\n"
            + "set(i2, 'abc');\n"
            + "set(i3, -5);\n"
            + "set(i4, 100.2);\n"
            + "set(i5, '10%');\n"
            + "set(i6, -12.56);\n"
            + "log(i1.height);\n"
            + "log(i2.height);\n"
            + "log(i3.height);\n"
            + "log(i4.height);\n"
            + "log(i5.height);\n"
            + "log(i6.height);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test the ReadyState which is an IE feature.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"uninitialized", "complete"},
            CHROME = {"complete", "complete"},
            EDGE = {"complete", "complete"})
    @HtmlUnitNYI(CHROME = {"loading", "complete"},
            EDGE = {"loading", "complete"},
            FF = {"loading", "complete"},
            FF_ESR = {"loading", "complete"})
    public void readyState_IFrame() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "  <body>\n"
            + "    <iframe id='i'></iframe>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      log(document.getElementById('i').contentWindow.document.readyState);\n"
            + "      window.onload = function() {\n"
            + "        log(document.getElementById('i').contentWindow.document.readyState);\n"
            + "      };\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "[object HTMLBodyElement]"})
    public void body() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <iframe name='theFrame' src='1.html'></iframe>\n"
            + "</body></html>";

        final String frame = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "log(document.body);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body><script>log(document.body);</script></html>";

        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setDefaultResponse(frame);

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("128px")
    public void width_px() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var iframe = document.getElementById('myFrame');\n"
            + "    iframe.width = '128px';\n"
            + "    log(iframe.width);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <iframe id='myFrame'></iframe>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * IE: getElementById() returns a different object than with direct 'id' variable.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object HTMLIFrameElement]", "[object HTMLIFrameElement]", "", ""})
    public void idByName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(myFrame);\n"
            + "    log(document.getElementById('myFrame'));\n"
            + "    log(myFrame.width);\n"
            + "    log(document.getElementById('myFrame').width);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <iframe id='myFrame'></iframe>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 2940926.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("foo")
    public void settingInnerHtmlTriggersFrameLoad() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d' onclick='loadFrame()'>Click me to show frame</div><script>\n"
            + "function loadFrame() {\n"
            + "  var s = '<iframe id=\"i\" src=\"frame.html\">';\n"
            + "  s += '<p>Your browser does not support frames</p>';\n"
            + "  s += '</iframe>';\n"
            + "  var d = document.getElementById('d');\n"
            + "  d.innerHTML = s;\n"
            + "}\n"
            + "</script></body></html>";
        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(new URL(URL_FIRST, "frame.html"), html2);

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("d")).click();

        driver.switchTo().frame("i");
        final String content = driver.findElement(By.xpath("//html/body")).getText();
        assertEquals(getExpectedAlerts()[0], content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("something")
    public void window() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  var iframe = document.getElementById('myIFrame');\n"
            + "  iframe.contentWindow.contents = 'something';\n"
            + "  iframe.src = 'javascript:window[\\'contents\\']';\n"
            + "}\n</script></head>\n"
            + "<body onload='test()'>\n"
            + "<iframe id='myIFrame' src='about:blank'></iframe></body></html>";

        final WebDriver driver = loadPage2(html);

        driver.switchTo().frame(0);
        final String content = driver.findElement(By.xpath("//html/body")).getText();
        assertEquals(getExpectedAlerts()[0], content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("something")
    public void settingSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  var iframe = document.createElement('iframe');\n"
            + "  var content = 'something';\n"
            + "  iframe.src = 'about:blank';\n"
            + "  document.body.appendChild(iframe);\n"
            + "  iframe.contentWindow.document.open('text/html', 'replace');\n"
            + "  iframe.contentWindow.document.write(content);\n"
            + "  iframe.contentWindow.document.close();\n"
            + "}\n</script></head>\n"
            + "<body onload='test()'></body></html>";

        final WebDriver driver = loadPage2(html);

        driver.switchTo().frame(0);
        final String content = driver.findElement(By.xpath("//html/body")).getText();
        assertEquals(getExpectedAlerts()[0], content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("iframe onload")
    public void writeTriggersOnload() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + LOG_TITLE_FUNCTION
            + "  var iframe = document.createElement('iframe');\n"
            + "  var content = 'something';\n"
            + "  document.body.appendChild(iframe);\n"

            + "  iframe.onload = function() {log('iframe onload')};\n"
            + "  iframe.contentWindow.document.open('text/html', 'replace');\n"
            + "  iframe.contentWindow.document.write(content);\n"
            + "  iframe.contentWindow.document.close();\n"
            + "}\n</script></head>\n"
            + "<body>\n"
            + "  <button type='button' id='clickme' onClick='test();'>Click me</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickme")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"localhost", "localhost", "localhost", "localhost",
                "true", "true", "true"})
    public void domain() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var docDomain = document.domain;\n"
            + "      var frame1Domain = document.getElementById('frame1').contentWindow.document.domain;\n"
            + "      var frame2Domain = document.getElementById('frame2').contentWindow.document.domain;\n"
            + "      var frame3Domain = document.getElementById('frame3').contentWindow.document.domain;\n"
            + "      log(docDomain);\n"
            + "      log(frame1Domain);\n"
            + "      log(frame2Domain);\n"
            + "      log(frame3Domain);\n"
            + "      log(docDomain === frame1Domain);\n"
            + "      log(docDomain === frame2Domain);\n"
            + "      log(docDomain === frame3Domain);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <iframe id='frame1' ></iframe>\n"
            + "  <iframe id='frame2' src='about:blank'></iframe>\n"
            + "  <iframe id='frame3' src='content.html'></iframe>\n"
            + "</body>\n"
            + "</html>";

        final String left = DOCTYPE_HTML
                + "<html><head><title>Left</title></head>\n"
                + "<body>left</body>\n"
                + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "content.html"), left);

        loadPageVerifyTitle2(html);
        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"localhost", "localhost", "true"})
    public void domainDynamic() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'idMyFrame';\n"
            + "      myFrame.src = 'about:blank';\n"
            + "      document.body.appendChild(myFrame);\n"

            + "      var docDomain = document.domain;\n"
            + "      var myFrameDomain = myFrame.contentDocument.domain;\n"

            + "      log(docDomain);\n"
            + "      log(myFrameDomain);\n"
            + "      log(docDomain === myFrameDomain);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Window]", "topbody", "framebody", "[object Window]", "frame", "frameinput"})
    @Disabled
    // check expectations
    public void contentWindowAndActiveElement() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function check() {\n"
            + "      log(document.getElementById('frame').contentWindow);\n"
            + "      log(document.activeElement.id);\n"
            + "      log(window.frame.window.document.activeElement.id);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body id='topbody'>\n"
            + "  <iframe id='frame' name='frame' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String frameContent = DOCTYPE_HTML
            + "<html>\n"
            + "<body id='framebody'>\n"
            + "  <input id='frameinput'>\n"
            + "</body></html>";

        final String[] alerts = getExpectedAlerts();
        int i = 0;

        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setResponse(URL_SECOND, frameContent);

        final WebDriver driver = loadPage2(firstContent);
        final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        jsExecutor.executeScript("check();");
        verifyAlerts(driver, alerts[i++], alerts[i++], alerts[i++]);

        driver.switchTo().frame("frame");
        driver.findElement(By.id("frameinput")).click();

        driver.switchTo().defaultContent();
        jsExecutor.executeScript("check();");
        verifyTitle2(driver, alerts[i++], alerts[i++], alerts[i++]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"loaded", "null"})
    public void deny() throws Exception {
        retrictByHeader(
                new NameValuePair(HttpHeader.X_FRAME_OPTIONS, "DENY"),
                new URL(URL_FIRST, "content.html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"loaded", "null"})
    public void csp_None() throws Exception {
        retrictByHeader(
                new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "frame-ancestors 'none';"),
                new URL(URL_FIRST, "content.html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"loaded", "[object HTMLDocument]"})
    public void csp_Self() throws Exception {
        retrictByHeader(
                new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "frame-ancestors 'self';"),
                new URL(URL_FIRST, "content.html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"loaded", "[object HTMLDocument]"})
    public void csp_SelfDifferentPath() throws Exception {
        retrictByHeader(
                new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "frame-ancestors 'self';"),
                new URL(URL_FIRST, "/path2/content.html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"loaded", "[object HTMLDocument]"})
    public void csp_Url() throws Exception {
        retrictByHeader(
                new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "frame-ancestors 'self';"),
                new URL(new URL("http://localhost:" + PORT + "/"), "content.html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"loaded", "null"})
    public void csp_UrlDifferentPort() throws Exception {
        retrictByHeader(
                new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "frame-ancestors 'self';"),
                new URL(new URL("http://localhost:" + PORT2 + "/"), "content.html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"loaded", "null"})
    public void csp_many() throws Exception {
        retrictByHeader(
                new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY,
                        "default-src 'none'; script-src 'self'; frame-ancestors 'self';"),
                new URL(new URL("http://localhost:" + PORT2 + "/"), "content.html"));
    }

    private void retrictByHeader(final NameValuePair header, final URL contentUrl) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "    function check() {\n"
            + "      try {\n"
            + "        log(document.getElementById(\"frame1\").contentDocument);\n"
            + "      } catch(e) { log('error'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <iframe id='frame1' src='" + contentUrl + "' "
                    + "onLoad='log(\"loaded\")' onError='log(\"error\")'></iframe>\n"
            + "  <button type='button' id='clickme' onClick='check()'>Click me</a>\n"
            + "</body>\n"
            + "</html>";

        final String content = DOCTYPE_HTML
                + "<html><head><title>IFrame Title</title></head>\n"
                + "<body>IFrame Content</body>\n"
                + "</html>";

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(header);

        getMockWebConnection().setResponse(contentUrl, content,
                200, "OK", MimeType.TEXT_HTML, headers);

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html, new URL(URL_FIRST, "path"));
        verifyWindowName2(driver, Arrays.copyOf(expectedAlerts, expectedAlerts.length - 1));

        driver.findElement(By.id("clickme")).click();
        verifyWindowName2(driver, expectedAlerts);

        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"loaded", "[object HTMLDocument]", "2"})
    public void recursive() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function check() {\n"
            + "      try {\n"
            + "        log(document.getElementById(\"frame1\").contentDocument);\n"
            + "      } catch(e) { log('error'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <iframe id='frame1' src='" + URL_FIRST + "' "
                        + "onLoad='log(\"loaded\")'></iframe>\n"
            + "  <button type='button' id='clickme' onClick='check()'>Click me</a>\n"
            + "</body>\n"
            + "</html>";

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, expectedAlerts[0]);

        driver.findElement(By.id("clickme")).click();
        verifyTitle2(driver, expectedAlerts[0], expectedAlerts[1]);

        assertEquals(Integer.parseInt(expectedAlerts[2]), getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"loaded", "3"})
    @HtmlUnitNYI(
            CHROME = {"loaded", "2"},
            EDGE = {"loaded", "2"},
            FF = {"loaded", "2"},
            FF_ESR = {"loaded", "2"})
    public void recursiveContent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <iframe id='frame1' src='content.html' "
                        + "onLoad='log(\"loaded\")'></iframe>\n"
            + "</body>\n"
            + "</html>";

        final String content = DOCTYPE_HTML
                + "<html>"
                + "<head><title>IFrame Title</title></head>\n"
                + "<body>IFrame Content\n"
                + "  <iframe id='frame1' src='content.html'></iframe>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse(content);

        final String[] expectedAlerts = getExpectedAlerts();
        loadPage2(html);
        verifyTitle2(getWebDriver(), expectedAlerts[0]);

        assertEquals(Integer.parseInt(expectedAlerts[1]), getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"loaded", "6"},
            FF_ESR = {"loaded", "19"},
            FF = {"loaded", "19"})
    @HtmlUnitNYI(CHROME = {"loaded", "21"},
            EDGE = {"loaded", "21"},
            FF = {"loaded", "21"},
            FF_ESR = {"loaded", "21"})
    public void recursiveContentRedirectHeader() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <iframe id='frame1' src='content.html' "
                        + "onLoad='log(\"loaded\")'></iframe>\n"
            + "</body>\n"
            + "</html>";

        final String content = DOCTYPE_HTML
                + "<html>"
                + "<head><title>IFrame Title</title></head>\n"
                + "<body>IFrame Content\n"
                + "  <iframe id='frame1' src='content.html'></iframe>\n"
                + "  <input id='myButton' type=button onclick=\"javascript:sayHello('%28%A')\" value='My Button'>\n"
                + "</body>\n"
                + "</html>";

        getMockWebConnection().setDefaultResponse(content);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", "content2.html"));
        getMockWebConnection().setResponse(new URL(URL_FIRST, "content.html"), "",
                    302, "Moved", MimeType.TEXT_HTML, headers);

        final String[] expectedAlerts = getExpectedAlerts();
        loadPage2(html);
        verifyTitle2(getWebDriver(), expectedAlerts[0]);

        assertEquals(Integer.parseInt(expectedAlerts[1]), getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Injected from parent frame")
    public void writeIntoIFrameContentDocument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doIt() {\n"
            + "      var html = '<h1>Injected from parent frame</h1>';\n"
            + "      document.getElementById(\"tester\").contentDocument.write(html);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <iframe id='tester'></iframe>\n"
            + "  <input id='myButton' type=button onclick=\"javascript:doIt()\" value='Write'>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(html);
        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("myButton")).click();

        driver.switchTo().frame("tester");
        verify(() -> driver.findElement(By.tagName("body")).getText(), getExpectedAlerts()[0]);

        if (driver instanceof HtmlUnitDriver) {
            final WebClient webClient = ((HtmlUnitDriver) driver).getWebClient();

            final HtmlPage page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();

            assertEquals(1, page.getFrames().size());

            final HtmlPage framePage = (HtmlPage) page.getFrames().get(0).getEnclosedPage();
            assertEquals("Injected from parent frame", framePage.getBody().asNormalizedText());
        }
    }
}
