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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLIFrameElement}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLIFrameElement3Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void style() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.getElementById('myIFrame').style == undefined);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe id='myIFrame' src='about:blank'></iframe></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "myIFrame" })
    public void referenceFromJavaScript() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(window.frames.length);\n"
            + "    alert(window.frames['myIFrame'].name);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe name='myIFrame' src='about:blank'></iframe></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 1562872.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "about:blank", "about:blank" })
    public void directAccessPerName() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(myIFrame.location);\n"
            + "    alert(Frame.location);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe name='myIFrame' src='about:blank'></iframe>\n"
            + "<iframe name='Frame' src='about:blank'></iframe>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests that the <tt>&lt;iframe&gt;</tt> node is visible from the contained page when it is loaded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("IFRAME")
    public void onLoadGetsIFrameElementByIdInParent() throws Exception {
        final String firstContent
            = "<!DOCTYPE html>\n"
            + "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<iframe id='myIFrame' src='frame.html'></iframe></body></html>";

        final String frameContent
            = "<!DOCTYPE html>\n"
            + "<html><head><title>Frame</title><script>\n"
            + "function doTest() {\n"
            + "    alert(parent.document.getElementById('myIFrame').tagName);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setDefaultResponse(frameContent);
        webConnection.setResponse(URL_FIRST, firstContent);

        loadPageWithAlerts2(firstContent);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLDocument]", "true" },
            IE8 = { "[object]", "true" })
    public void contentDocument() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "  <title>first</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.getElementById('myFrame').contentDocument);\n"
            + "      alert(document.getElementById('myFrame').contentDocument == frames.foo.document);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <iframe name='foo' id='myFrame' src='about:blank'></iframe>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void frameElement() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><title>first</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.getElementById('myFrame') == frames.foo.frameElement);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<iframe name='foo' id='myFrame' src='about:blank'></iframe>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that writing to an iframe keeps the same intrinsic variables around (window,
     * document, etc) and in a usable form. Bug detected via the jQuery 1.1.3.1 unit tests.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "false", "false", "true", "true", "true", "object", "object" })
    public void writeToIFrame() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><body onload='test()'><script>\n"
            + "    function test() {\n"
            + "        \n"
            + "        var frame = document.createElement('iframe');\n"
            + "        document.body.appendChild(frame);\n"
            + "        var win = frame.contentWindow;\n"
            + "        var doc = frame.contentWindow.document;\n"
            + "        alert(win == window);\n"
            + "        alert(doc == document);\n"
            + "        \n"
            + "        doc.open();\n"
            + "        doc.write(\"<html><body><input type='text'/></body></html>\");\n"
            + "        doc.close();\n"
            + "        var win2 = frame.contentWindow;\n"
            + "        var doc2 = frame.contentWindow.document;\n"
            + "        alert(win == win2);\n"
            + "        alert(doc == doc2);\n"
            + "        \n"
            + "        var input = doc.getElementsByTagName('input')[0];\n"
            + "        var input2 = doc2.getElementsByTagName('input')[0];\n"
            + "        alert(input == input2);\n"
            + "        alert(typeof input);\n"
            + "        alert(typeof input2);\n"
            + "    }\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that writing to an iframe keeps the same intrinsic variables around (window,
     * document, etc) and in a usable form. Bug detected via the jQuery 1.1.3.1 unit tests.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "123", "undefined" },
            IE11 = { "123" })
    public void iFrameReinitialized() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><body><a id='test' href='2.html' target='theFrame'>page 2 in frame</a>\n"
            + "<iframe name='theFrame' src='1.html'></iframe>\n"
            + "</body></html>";

        final String frame1 = "<html><head><script>window.foo = 123; alert(window.foo);</script></head></html>";
        final String frame2 = "<html><head><script>alert(window.foo);</script></head></html>";

        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(new URL(URL_FIRST, "1.html"), frame1);
        webConnection.setResponse(new URL(URL_FIRST, "2.html"), frame2);

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("test")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
        assertEquals(3, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("about:blank")
    public void setSrc_JavascriptUrl() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "   document.getElementById('iframe1').src = 'javascript:void(0)';\n"
            + "   alert(window.frames[0].location);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<iframe id='iframe1'></iframe>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "100", "foo", "20%", "-5", "30.2", "400", "abc", "-5", "100.2", "10%", "-12.56" },
            IE = {"", "100", "", "20%", "-5", "30", "error", "400", "100", "-5", "100", "10%", "-12" })
    public void width() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><body>\n"
            + "<iframe id='i1'></iframe>\n"
            + "<iframe id='i2' width='100'></iframe>\n"
            + "<iframe id='i3' width='foo'></iframe>\n"
            + "<iframe id='i4' width='20%'></iframe>\n"
            + "<iframe id='i5' width='-5'></iframe>\n"
            + "<iframe id='i6' width='30.2'></iframe>\n"
            + "<script>\n"
            + "function set(e, value) {\n"
            + "  try {\n"
            + "    e.width = value;\n"
            + "  } catch (e) {\n"
            + "    alert('error');\n"
            + "  }\n"
            + "}\n"
            + "var i1 = document.getElementById('i1');\n"
            + "var i2 = document.getElementById('i2');\n"
            + "var i3 = document.getElementById('i3');\n"
            + "var i4 = document.getElementById('i4');\n"
            + "var i5 = document.getElementById('i5');\n"
            + "var i6 = document.getElementById('i6');\n"
            + "alert(i1.width);\n"
            + "alert(i2.width);\n"
            + "alert(i3.width);\n"
            + "alert(i4.width);\n"
            + "alert(i5.width);\n"
            + "alert(i6.width);\n"
            + "set(i1, '400');\n"
            + "set(i2, 'abc');\n"
            + "set(i3, -5);\n"
            + "set(i4, 100.2);\n"
            + "set(i5, '10%');\n"
            + "set(i6, -12.56);\n"
            + "alert(i1.width);\n"
            + "alert(i2.width);\n"
            + "alert(i3.width);\n"
            + "alert(i4.width);\n"
            + "alert(i5.width);\n"
            + "alert(i6.width);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "100", "foo", "20%", "-5", "30.2", "400", "abc", "-5", "100.2", "10%", "-12.56" },
            IE = {"", "100", "", "20%", "-5", "30", "error", "400", "100", "-5", "100", "10%", "-12" })
    public void height() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><body>\n"
            + "<iframe id='i1'></iframe>\n"
            + "<iframe id='i2' height='100'></iframe>\n"
            + "<iframe id='i3' height='foo'></iframe>\n"
            + "<iframe id='i4' height='20%'></iframe>\n"
            + "<iframe id='i5' height='-5'></iframe>\n"
            + "<iframe id='i6' height='30.2'></iframe>\n"
            + "<script>\n"
            + "function set(e, value) {\n"
            + "  try {\n"
            + "    e.height = value;\n"
            + "  } catch (e) {\n"
            + "    alert('error');\n"
            + "  }\n"
            + "}\n"
            + "var i1 = document.getElementById('i1');\n"
            + "var i2 = document.getElementById('i2');\n"
            + "var i3 = document.getElementById('i3');\n"
            + "var i4 = document.getElementById('i4');\n"
            + "var i5 = document.getElementById('i5');\n"
            + "var i6 = document.getElementById('i6');\n"
            + "alert(i1.height);\n"
            + "alert(i2.height);\n"
            + "alert(i3.height);\n"
            + "alert(i4.height);\n"
            + "alert(i5.height);\n"
            + "alert(i6.height);\n"
            + "set(i1, '400');\n"
            + "set(i2, 'abc');\n"
            + "set(i3, -5);\n"
            + "set(i4, 100.2);\n"
            + "set(i5, '10%');\n"
            + "set(i6, -12.56);\n"
            + "alert(i1.height);\n"
            + "alert(i2.height);\n"
            + "alert(i3.height);\n"
            + "alert(i4.height);\n"
            + "alert(i5.height);\n"
            + "alert(i6.height);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test the ReadyState which is an IE feature.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "uninitialized", "complete" },
            CHROME = { "complete", "complete" },
            IE = { "loading", "complete" })
    @NotYetImplemented(FF)
    public void readyState_IFrame() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "  <body>\n"
            + "    <iframe id='i'></iframe>\n"
            + "    <script>\n"
            + "      alert(document.getElementById('i').contentWindow.document.readyState);\n"
            + "      window.onload = function() {\n"
            + "        alert(document.getElementById('i').contentWindow.document.readyState);\n"
            + "      };\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "null", "[object HTMLBodyElement]" },
            IE8 = { "null", "[object]" })
    public void body() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><body>\n"
            + "  <iframe name='theFrame' src='1.html'></iframe>\n"
            + "</body></html>";

        final String frame = "<html><head><script>alert(document.body);</script></head>\n"
            + "<body><script>alert(document.body);</script></html>";

        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse(frame);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "128px",
            IE = "128")
    public void width_px() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    var iframe = document.getElementById('myFrame');\n"
            + "    iframe.width = '128px';\n"
            + "    alert(iframe.width);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <iframe id='myFrame'></iframe>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * IE: getElementById() returns a different object than with direct 'id' variable.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLIFrameElement]", "[object HTMLIFrameElement]", "", "" },
            IE11 = { "[object Window]", "[object HTMLIFrameElement]", "undefined", "" },
            IE8 = { "[object]", "[object]", "undefined", "" })
    @NotYetImplemented(IE)
    public void idByName() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(myFrame);\n"
            + "    alert(document.getElementById('myFrame'));\n"
            + "    alert(myFrame.width);\n"
            + "    alert(document.getElementById('myFrame').width);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <iframe id='myFrame'></iframe>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 2940926.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("foo")
    public void settingInnerHtmlTriggersFrameLoad() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><body><div id='d' onclick='loadFrame()'>Click me to show frame</div><script>\n"
            + "function loadFrame() {\n"
            + "  var s = '<iframe id=\"i\" src=\"frame.html\">';\n"
            + "  s += '<p>Your browser does not support frames</p>';\n"
            + "  s += '</iframe>';\n"
            + "  var d = document.getElementById('d');\n"
            + "  d.innerHTML = s;\n"
            + "}\n"
            + "</script></body></html>";
        final String html2 = "<html><body>foo</body></html>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, html);
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
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var iframe = document.getElementById('myIFrame');\n"
            + "    iframe.contentWindow.contents = 'something';\n"
            + "    iframe.src = 'javascript:window[\\'contents\\']';\n"
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
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var iframe = document.createElement('iframe');\n"
            + "    var content = 'something';\n"
            + "    iframe.src = 'about:blank';\n"
            + "    document.body.appendChild(iframe);\n"
            + "    iframe.contentWindow.document.open('text/html', 'replace');\n"
            + "    iframe.contentWindow.document.write(content);\n"
            + "    iframe.contentWindow.document.close();\n"
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
    @Alerts({ "localhost", "localhost", "localhost", "localhost",
                "true", "true", "true" })
    public void domain() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "  <title>OnloadTest</title>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      var docDomain = document.domain;\n"
            + "      var frame1Domain = document.getElementById('frame1').contentWindow.document.domain;\n"
            + "      var frame2Domain = document.getElementById('frame2').contentWindow.document.domain;\n"
            + "      var frame3Domain = document.getElementById('frame3').contentWindow.document.domain;\n"
            + "      alert(docDomain);\n"
            + "      alert(frame1Domain);\n"
            + "      alert(frame2Domain);\n"
            + "      alert(frame3Domain);\n"
            + "      alert(docDomain === frame1Domain);\n"
            + "      alert(docDomain === frame2Domain);\n"
            + "      alert(docDomain === frame3Domain);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <iframe id='frame1' ></iframe>\n"
            + "  <iframe id='frame2' src='about:blank'></iframe>\n"
            + "  <iframe id='frame3' src='content.html'></iframe>\n"
            + "</body>\n"
            + "</html>";

        final String left = "<html><head><title>Left</title></head>\n"
                + "<body>left</body>\n"
                + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "content.html"), left);

        loadPageWithAlerts2(html);
        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "localhost", "localhost", "true" })
    public void domainDynamic() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "  <title>OnloadTest</title>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'idMyFrame'\n"
            + "      myFrame.src = 'about:blank'\n"
            + "      document.body.appendChild(myFrame)\n"

            + "      var docDomain = document.domain;\n"
            + "      var myFrameDomain = myFrame.contentDocument.domain;\n"

            + "      alert(docDomain);\n"
            + "      alert(myFrameDomain);\n"
            + "      alert(docDomain === myFrameDomain);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Window]", "topbody", "framebody", "[object Window]", "topbody", "frameinput" },
            CHROME = { "[object Window]", "topbody", "framebody", "[object Window]", "frame", "frameinput" },
            IE11 = { "[object Window]", "topbody", "framebody", "[object Window]", "frame", "frameinput" },
            IE8 = { "[object]", "topbody", "framebody", "[object]", "topbody", "frameinput" })
    public void contentWindowAndActiveElement() throws Exception {
        final String firstContent
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function check() {"
            + "      alert(document.getElementById('frame').contentWindow);\n"
            + "      alert(document.activeElement.id);\n"
            + "      alert(window.frame.window.document.activeElement.id);\n"
            + "    }"
            + "  </script>\n"
            + "</head>\n"
            + "<body id='topbody'>\n"
            + "<iframe id='frame' name='frame' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String frameContent
            = "<html>\n"
            + "<body id='framebody'>\n"
                + "  <input id='frameinput'>\n"
            + "</body></html>";

        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, frameContent);

        final WebDriver driver = loadPage2(firstContent);
        final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        jsExecutor.executeScript("check();");
        jsExecutor.executeScript("Object(document.getElementById('frame').contentWindow);");

        driver.switchTo().frame("frame");
        driver.findElement(By.id("frameinput")).click();

        driver.switchTo().defaultContent();
        jsExecutor.executeScript("check();");

        verifyAlerts(DEFAULT_WAIT_TIME, getExpectedAlerts(), driver);
    }
}
