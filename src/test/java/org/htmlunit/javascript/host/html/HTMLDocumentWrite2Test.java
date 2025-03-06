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
package org.htmlunit.javascript.host.html;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.htmlunit.MockWebConnection;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for {@link HTMLDocument}'s write(ln) function.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLDocumentWrite2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLDocument]")
    public void openResult() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "function test() {\n"
            + "  var res = document.open();\n"
            + "  log(res);\n"
            + "  document.close();\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='setTimeout(test, 4);'>\n"
            + "</body>\n"
            + "</html>";

        loadPage2(html);
        verifyWindowName2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Hello There")
    public void write() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<title>Test</title>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "function test() {\n"
            + "  document.write('<html><body><scr'+'ipt>log(\"Hello There\")</scr'+'ipt></body></html>');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='setTimeout(test, 4);'>\n"
            + "</body>\n"
            + "</html>";

        loadPage2(html);
        verifyWindowName2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeSomeTags() throws Exception {
        final String html = "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "document.write(\"<div id='div1'></div>\");\n"
            + "document.write('<div', \" id='div2'>\", '</div>');\n"
            + "document.writeln('<p', \" id='p3'>\", '</p>');\n"
            + "</script>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(html);

        assertEquals("div", driver.findElement(By.id("div1")).getTagName());
        assertEquals("div", driver.findElement(By.id("div2")).getTagName());
        assertEquals("p", driver.findElement(By.id("p3")).getTagName());
    }

    /**
     * <a href="https://sourceforge.net/tracker/?func=detail&aid=2855731&group_id=47038&atid=448266">Bug 2855731</a>.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void write_nested() throws Exception {
        final String html =
              "<html><body><script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "var s = '\"<script>log(1);<\\/scr\" + \"ipt>\"';\n"
            + "document.write('<script><!--\\ndocument.write(' + s + ');\\n--><\\/script>');\n"
            + "</script></body></html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Caused infinite loop at some point of 2.6 snapshot.
     * See <a href="http://sourceforge.net/support/tracker.php?aid=2824922">Bug 2824922</a>
     * @throws Exception if the test fails
     */
    @Test
    public void write2_html_endhtml_in_head() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "document.write('<HTML></HTML>');\n"
            + "</script>\n"
            + "</head><body>\n"
            + "</body></html>\n";

        loadPage2(html);
    }

    /**
     * We couldn't document.write() script elements that contained the '&lt;' character...
     * @exception Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void writeScript() throws Exception {
        final String html =
              "<html><body><script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "  document.write('<scr'+'ipt>log(1<2)</sc'+'ript>');\n"
            + "</script></body></html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeUnicode() throws Exception {
        final String html = "<html><body><script>\n"
            + "document.open();\n"
            + "document.write('<div id=\"assert\">Hello worl\u0414</div>');\n"
            + "document.close();\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html; charset=UTF-8", UTF_8);
        final String result = driver.findElement(By.id("assert")).getText();
        assertEquals("Hello worl\u0414", result);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeISO_8859_1() throws Exception {
        final String html = "<html><body><script>\n"
            + "document.open();\n"
            + "document.write('<div id=\"assert\">\u00e4\u00f6\u00fc\u00c4\u00d6\u00dc</div>');\n"
            + "document.close();\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html; charset=ISO-8859-1", ISO_8859_1);
        final String result = driver.findElement(By.id("assert")).getText();
        assertEquals("\u00e4\u00f6\u00fc\u00c4\u00d6\u00dc", result);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void open_FF() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function performAction() {\n"
            + "    actionwindow = window.open('', '1205399746518', "
            + "'location=no,scrollbars=no,resizable=no,width=200,height=275');\n"
            + "    actionwindow.document.writeln('Please wait while connecting to server...');\n"
            + "    actionwindow.focus();\n"
            + "    actionwindow.close();\n"
            + "  }\n"
            + "</script></head><body>\n"
            + "  <input value='Click Me' type=button onclick='performAction()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        driver.findElement(By.xpath("//input")).click();
    }

    /**
     * Regression test for bug 2884585.
     * As of HtmlUnit-2.7-SNAPSHOT 17.01.2010 &lt;script src="..."... written
     * by document.write was not loaded and executed after the &lt;/script&gt;
     * when the page was loaded as result of a click.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({" after-write Hello", " after-write Hello after-write Hello"})
    public void writeExternalScriptAfterClick() throws Exception {
        shutDownAll();

        final String html = "<html><head>\n"
            + "<script>\n"
            + "document.write('<scr'+'ipt src=\"script.js\"></scr'+'ipt>');\n"
            + "window.name += ' after-write ';\n"
            + "</script>\n"
            + "<script>\n"
            + "window.name += window.foo;\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<a href='?again'>a link</a>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("window.foo = 'Hello'", MimeType.TEXT_JAVASCRIPT);
        final WebDriver driver = loadPage2(html);
        verifyJsVariable(driver, "window.top.name", getExpectedAlerts()[0]);

        driver.findElement(By.linkText("a link")).click();
        verifyJsVariable(driver, "window.top.name", getExpectedAlerts()[1]);
    }

    /**
     * Regression test for bug 2921851.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("#1")
    @HtmlUnitNYI(CHROME = "#0",
            EDGE = "#0",
            FF = "#0",
            FF_ESR = "#0")
    public void writeInNewWindowAndReadFormCollection() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var newWin = window.open('', 'myPopup', '');\n"
            + "  var newDoc = newWin.document;\n"
            + "  newDoc.write('<html><body><form name=newForm></form></body></html>');\n"
            + "  document.title = '#' + newDoc.forms.length;\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * Partial regression test for bug 2921851: use opener URL as base URL
     * for resolution of relative URLs in document.write.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "§§URL§§foo"})
    public void urlResolutionInWriteForm() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var newWin = window.open('', 'myPopup', '');\n"
            + "  var d = newWin.document;\n"
            + "  d.write('<html><body><form action=foo method=post><input type=submit id=it></form></body></html>');\n"
            + "  d.close();\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final int startCount = getMockWebConnection().getRequestCount();
        expandExpectedAlertsVariables(URL_FIRST);

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        driver.switchTo().window("myPopup");
        driver.findElement(By.id("it")).click();

        assertEquals(Integer.parseInt(getExpectedAlerts()[0]),
                getMockWebConnection().getRequestCount() - startCount);
        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * Partial regression test for bug 2921851: the window returned by <tt>window.open()</tt> should
     * be proxied (i.e. "live").
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("<form></form>#[object HTMLFormElement]")
    public void writeOnOpenedWindow_WindowIsProxied() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var w = window.open('','blah','width=460,height=420');\n"
            + "  w.document.write('<html><body><form></form></body></html>');\n"
            + "  w.document.close();\n"
            + "  document.title = w.document.body.innerHTML;\n"
            + "  document.title += '#' + w.document.forms[0];\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * Partial regression test for bug 2921851: the document returned by <tt>window.document</tt> should
     * be proxied (i.e. "live").
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("<form></form>#[object HTMLFormElement]")
    public void writeOnOpenedWindow_DocumentIsProxied() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var w = window.open('','blah','width=460,height=420');\n"
            + "  var d = w.document;\n"
            + "  d.write('<html><body><form></form></body></html>');\n"
            + "  d.close();\n"
            + "  document.title = d.body.innerHTML;\n"
            + "  document.title += '#' + d.forms[0];\n"
            + "}\n"
            + "</script></head><body onload='test()'>foo</body></html>";

        try {
            final WebDriver driver = loadPage2(html);
            assertTitle(driver, getExpectedAlerts()[0]);
        }
        finally {
            shutDownAll();
        }
    }

    /**
     * This was causing a StackOverflowError.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void writeInFrameWithOnload() throws Exception {
        final String html = "<html><head></head>\n"
            + "<body>\n"
            + "<iframe id='theIframe' src='about:blank'></iframe>\n"
            + "<script>\n"
            + "var doc = document.getElementById('theIframe').contentWindow.document;\n"
            + "doc.open();\n"
            + "doc.write('<html>');\n"
            + "doc.write('<body onload=\"document.getElementById(\\'foo\\')\">');\n"
            + "doc.write('</body></html>');\n"
            + "doc.close();\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 743241.
     * @throws Exception if the test fails
     */
    @Test
    public void write_loadScript() throws Exception {
        final String html
            = "<html><head><title>First</title></head><body>\n"
            + "<script src='script.js'></script>\n"
            + "</form></body></html>";

        final String script = "document.write(\"<div id='div1'>hello</div>\");\n";
        getMockWebConnection().setDefaultResponse(script, MimeType.TEXT_JAVASCRIPT);

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, "First");

        assertEquals("hello", driver.findElement(By.id("div1")).getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void write_fromScriptAddedWithAppendChild_inline() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<div id='it'><script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "try {\n"
            + "  var s = document.createElement('script');\n"
            + "  var t = document.createTextNode(\"document.write('in inline script'); document.title = 'done';\");\n"
            + "  s.appendChild(t);\n"
            + "  document.body.appendChild(s);\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></div></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());

        assertTitle(driver, "done");
        assertEquals("in inline script", driver.findElement(By.id("it")).getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void write_fromScriptAddedWithAppendChild_external() throws Exception {
        final String html = "<html><head></head><body>\n"
                + "<div id='it'>here</div><script>\n"
                + "  var s = document.createElement('script');\n"
                + "  s.src = 'foo.js';\n"
                + "  document.body.appendChild(s);\n"
                + "</script></body></html>";

        final String js = "document.write('from external script');\n"
                    + "document.title = 'done';";

        getMockWebConnection().setDefaultResponse(js, MimeType.TEXT_JAVASCRIPT);
        final WebDriver driver = loadPage2(html);

        assertTitle(driver, "done");
        assertEquals("here", driver.findElement(By.id("it")).getText());
        assertEquals("here", driver.findElement(By.tagName("body")).getText());
    }

    /**
     * Verifies that document.write() sends content to the correct destination (always somewhere in the body).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "[object HTMLBodyElement]", "s1 s2 s3 s4 s5"})
    public void write_Destination() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>" + LOG_WINDOW_NAME_FUNCTION + "</script>\n"
            + "    <script>log(document.body);</script>\n"
            + "    <script>document.write('<span id=\"s1\">1</span>');</script>\n"
            + "    <script>log(document.body);</script>\n"
            + "    <title>test</title>\n"
            + "    <script>document.write('<span id=\"s2\">2</span>');</script>\n"
            + "  </head>\n"
            + "  <body id='foo'>\n"
            + "    <script>document.write('<span id=\"s3\">3</span>');</script>\n"
            + "    <span id='s4'>4</span>\n"
            + "    <script>document.write('<span id=\"s5\">5</span>');</script>\n"
            + "    <script>\n"
            + "      var s = '';\n"
            + "      for(var n = document.body.firstChild; n; n = n.nextSibling) {\n"
            + "        if(n.id) {\n"
            + "          if(s.length > 0) s+= ' ';\n"
            + "            s += n.id;\n"
            + "        }\n"
            + "      }\n"
            + "      log(s);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Verifies that document.write() sends content to the correct destination (always somewhere in the body),
     * and that if a synthetic temporary body needs to be created, the attributes of the real body are eventually
     * used once the body is parsed.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "[object HTMLBodyElement]", "", "foo"})
    public void write_BodyAttributesKept() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>" + LOG_WINDOW_NAME_FUNCTION + "</script>\n"
            + "    <script>log(document.body);</script>\n"
            + "    <script>document.write('<span id=\"s1\">1</span>');</script>\n"
            + "    <script>log(document.body);</script>\n"
            + "    <script>log(document.body.id);</script>\n"
            + "    <title>test</title>\n"
            + "  </head>\n"
            + "  <body id='foo'>\n"
            + "    <script>log(document.body.id);</script>\n"
            + "  </body>\n"
            + "</html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Verifies that document.write() sends content to the correct destination (always somewhere in the body),
     * and that script elements written to the document are executed in the correct order.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "2", "3"})
    public void write_ScriptExecutionOrder() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>test</title>\n"
            + "    <script>" + LOG_WINDOW_NAME_FUNCTION + "</script>\n"
            + "    <script>log('1');</script>\n"
            + "    <script>document.write('<scrip'+'t>log(\"2\")</s'+'cript>');</script>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <script>document.write('<scrip'+'t>log(\"3\")</s'+'cript>');</script>\n"
            + "  </body>\n"
            + "</html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("outer")
    public void writeInManyTimes() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "function doTest() {\n"
            + "  log(document.getElementById('inner').parentNode.id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<script>\n"
            + "document.write('<div id=\"outer\">');\n"
            + "document.write('<div id=\"inner\"/>');\n"
            + "document.write('</div>');\n"
            + "</script>\n"
            + "</body></html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Test for bug 1185389.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"theBody", "theBody", "theBody"})
    public void writeAddNodesToCorrectParent() throws Exception {
        final String html = "<html><head><title>foo</title></head>\n"
             + "<body id=\"theBody\">\n"
             + "<script>\n"
             + LOG_WINDOW_NAME_FUNCTION
             + "document.write('<p id=\"para1\">Paragraph #1</p>');\n"
             + "document.write('<p id=\"para2\">Paragraph #2</p>');\n"
             + "document.write('<p id=\"para3\">Paragraph #3</p>');\n"
             + "log(document.getElementById('para1').parentNode.id);\n"
             + "log(document.getElementById('para2').parentNode.id);\n"
             + "log(document.getElementById('para3').parentNode.id);\n"
             + "</script>\n"
             + "</body></html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Test for bug 436.
     * http://sourceforge.net/p/htmlunit/bugs/436/
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"outer", "inner1"})
    public void writeAddNodesToCorrectParent_Bug1678826() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
             + LOG_WINDOW_NAME_FUNCTION
             + "function doTest() {\n"
             + "  log(document.getElementById('inner1').parentNode.id);\n"
             + "  log(document.getElementById('inner2').parentNode.id);\n"
             + "}\n"
             + "</script></head>\n"
             + "<body onload='doTest()'>\n"
             + "<script>\n"
             + "document.write('<div id=\"outer\">');\n"
             + "document.write('<br id=\"br1\">');\n"
             + "document.write('<div id=\"inner1\"/>');\n"
             + "document.write('<hr id=\"hr1\"/>');\n"
             + "document.write('<div id=\"inner2\"/>');\n"
             + "document.write('</div>');\n"
             + "</script>\n"
             + "</body></html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());

        releaseResources();
        shutDownAll();
    }

     /**
      * @throws Exception if the test fails
      */
    @Test
    @Alerts({"STYLE", "SCRIPT"})
    public void writeStyle() throws Exception {
        final String html = "<html><head><title>foo</title></head><body>\n"
             + "<script>\n"
             + LOG_WINDOW_NAME_FUNCTION
             + "  document.write('<style type=\"text/css\" id=\"myStyle\">');\n"
             + "  document.write('  .nwr {white-space: nowrap;}');\n"
             + "  document.write('</style>');\n"
             + "  document.write('<div id=\"myDiv\">');\n"
             + "  document.write('</div>');\n"
             + "  log(document.getElementById('myDiv').previousSibling.nodeName);\n"
             + "  log(document.getElementById('myStyle').previousSibling.nodeName);\n"
             + "</script>\n"
             + "</body></html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void openReplace() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>Test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var htmlDocument = '<html><head>"
                    + "<script>document.title = \"parsed script executed\";</' + 'script>"
                    + "</head><body>After</body</head>';\n"
            + "  var newHTML = document.open('text/html', 'replace');\n"
            + "  newHTML.write(htmlDocument);\n"
            + "  newHTML.close();\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='setTimeout(test, 4);'>\n"
            + " Before\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertTitle(driver, "parsed script executed");
        assertEquals("After", driver.findElement(By.tagName("body")).getText());
    }

    /**
     * Verifies that scripts added to the document via document.write(...) don't execute until the current script
     * finishes executing; bug found at <a href="http://code.google.com/apis/maps/">the Google Maps API site</a>.
     * @throws Exception if an error occurs
     */
    @Test
    public void write_scriptExecutionPostponed() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d'></div>\n"
            + "<script>function log(s) { document.getElementById('d').innerHTML += s + ' '; }</script>\n"
            + "<script src='a.js'></script>\n"
            + "<script>log(2);document.write('<scr'+'ipt src=\"b.js\"></scr'+'ipt>');log(3);</script>\n"
            + "<script src='c.js'></script>\n"
            + "<script>\n"
            + "  log(6);document.write('<scr'+'ipt src=\"d.js\"></scr'+'ipt>');log(7);\n"
            + "  log(8);document.write('<scr'+'ipt src=\"e.js\"></scr'+'ipt>');log(9);\n"
            + "</script>\n"
            + "<script src='f.js'></script>\n"
            + "</body></html>";
        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(new URL(URL_FIRST, "a.js"), "log(1)", MimeType.TEXT_JAVASCRIPT);
        conn.setResponse(new URL(URL_FIRST, "b.js"), "log(4)", MimeType.TEXT_JAVASCRIPT);
        conn.setResponse(new URL(URL_FIRST, "c.js"), "log(5)", MimeType.TEXT_JAVASCRIPT);
        conn.setResponse(new URL(URL_FIRST, "d.js"), "log(10)", MimeType.TEXT_JAVASCRIPT);
        conn.setResponse(new URL(URL_FIRST, "e.js"), "log(11)", MimeType.TEXT_JAVASCRIPT);
        conn.setResponse(new URL(URL_FIRST, "f.js"), "log(12)", MimeType.TEXT_JAVASCRIPT);

        final WebDriver driver = loadPage2(html);
        assertEquals("1 2 3 4 5 6 7 8 9 10 11 12", driver.findElement(By.tagName("body")).getText());
    }

    /**
     * Regression test for Bug #71.
     * @throws Exception if the test fails
     */
    @Test
    public void write_script() throws Exception {
        final URL mainUrl = new URL(URL_FIRST, "main.html");
        final URL firstUrl = new URL(URL_FIRST, "first.html");
        final URL secondUrl = new URL(URL_FIRST, "second.html");
        final URL scriptUrl = new URL(URL_FIRST, "script.js");

        final String mainHtml = "<html>\n"
            + "<head><title>Main</title></head>\n"
            + "<body>\n"
            + "  <iframe name='iframe' id='iframe' src='" + firstUrl + "'></iframe>\n"
            + "  <script type='text/javascript'>\n"
            + "    document.write('<script type=\"text/javascript\" src=\"" + scriptUrl + "\"></' + 'script>');\n"
            + "  </script>"
            + "</body></html>";

        getMockWebConnection().setResponse(mainUrl, mainHtml);

        final String firstHtml = "<html><body><h1 id='first'>First</h1></body></html>";
        getMockWebConnection().setResponse(firstUrl, firstHtml);

        final String secondHtml = "<html><body><h1 id='second'>Second</h1></body></html>";
        getMockWebConnection().setResponse(secondUrl, secondHtml);

        final String script = "document.getElementById('iframe').src = '" + secondUrl + "';\n";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script.js"), script, MimeType.TEXT_JAVASCRIPT);

        final WebDriver driver = loadPage2(mainUrl, StandardCharsets.UTF_8);
        assertEquals("Main", driver.getTitle());

        final WebElement iframe = driver.findElement(By.id("iframe"));
        assertEquals(secondUrl.toExternalForm(), iframe.getAttribute("src"));

        driver.switchTo().frame(iframe);
        assertEquals("Second", driver.findElement(By.id("second")).getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"A", "A"})
    public void write_InDOM() throws Exception {
        final String html = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "    document.write('<a id=\"blah\">Hello World</a>');\n"
            + "    document.write('<a id=\"blah2\">Hello World 2</a>');\n"
            + "    log(document.getElementById('blah').tagName);\n"
            + "    log(document.getElementById('blah2').tagName);\n"
            + "  </script>\n"
            + "  <a id='blah3'>Hello World 3</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, getExpectedAlerts());

        final List<WebElement> anchors = driver.findElements(By.tagName("a"));
        assertEquals(3, anchors.size());
        assertEquals("Hello World", anchors.get(0).getText());
        assertEquals("Hello World 2", anchors.get(1).getText());
        assertEquals("Hello World 3", anchors.get(2).getText());
    }

    /**
     * Test for bug 1950462: calling document.write inside a function (after assigning
     * document.write to a local variable) tries to invoke document.write on the prototype
     * document instance, rather than the actual document host object. This leads to an
     * {@link IllegalStateException} (DomNode has not been set for this SimpleScriptable).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"foo called", "exception occurred"})
    public void write_AssignedToVar2() throws Exception {
        final String html = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function foo() { log('foo called'); var d = document.write; d(4); }\n"
            + "  try {\n"
            + "    foo();\n"
            + "  } catch(e) { log('exception occurred'); document.write(7); }\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, getExpectedAlerts());

        assertEquals("7", driver.findElement(By.tagName("body")).getText());
    }

    /**
     * Verifies that calling document.write() after document parsing has finished results in a whole
     * new page being loaded.
     * @throws Exception if an error occurs
     */
    @Test
    public void write_WhenParsingFinished() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() { document.write(1); document.write(2); document.close(); }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <span id='s' onclick='test()'>click</span>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals("click", driver.findElement(By.tagName("body")).getText());

        driver.findElement(By.id("s")).click();
        assertEquals("12", driver.findElement(By.tagName("body")).getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeWithSplitAnchorTag() throws Exception {
        final String html = "<html>\n"
            + "<body><script>\n"
            + "document.write(\"<a href=\'start.html\");\n"
            + "document.write(\"\'>\");\n"
            + "document.write('click here</a>');\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final List<WebElement> anchors = driver.findElements(By.tagName("a"));
        assertEquals(1, anchors.size());
        assertEquals("http://localhost:22222/start.html", anchors.get(0).getAttribute("href"));
        assertEquals("click here", anchors.get(0).getText());
    }

    /**
     * Verifies that calls to document.open() are ignored while the page's HTML is being parsed.
     * @throws Exception if an error occurs
     */
    @Test
    public void open_IgnoredDuringParsing() throws Exception {
        final String html = "<html><body>1<script>document.open();document.write('2');</script>3</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals("123", driver.findElement(By.tagName("body")).getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeWithSpace() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <script>\n"
            + "    document.write('Hello ');\n"
            + "    document.write('World');\n"
            + "  </script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals("Hello World", driver.findElement(By.tagName("body")).getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "foo1", "1", "2", "3", "4", "5", "A", "B", "foo3"})
    public void writeScriptInManyTimes() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('0');\n"
            + "  document.write('<script>log(\"foo1\");</' + 'script>');\n"

            + "  log('1');\n"
            + "  document.write('<script src=\"scriptA.js\"></' + 'script>');\n"
            + "  log('2');\n"

            + "  document.write('<script src=\"scriptB.js\">');\n"
            + "  log('3');\n"
            + "  document.write('<' + '/script>');\n"
            + "  log('4');\n"
            + "  document.write('<script>log(\"foo3\");</' + 'script>');\n"
            + "  log('5');\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final URL scriptUrlA = new URL(URL_FIRST, "scriptA.js");
        final URL scriptUrlB = new URL(URL_FIRST, "scriptB.js");

        getMockWebConnection().setDefaultResponse(html);
        getMockWebConnection().setResponse(scriptUrlA, "log('A');\n", MimeType.TEXT_JAVASCRIPT);
        getMockWebConnection().setResponse(scriptUrlB, "log('B');\n", MimeType.TEXT_JAVASCRIPT);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "foo1", "1", "foo2", "2", "3", "4", "A", "foo3"})
    public void writeScriptPostponed() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('0');\n"
            + "</script>\n"

            + "<script>log(\"foo1\");</script>'\n"

            + "<script>\n"
            + "  log('1');\n"
            + "  document.write('<script>log(\"foo2\");</' + 'script>');\n"
            + "  log('2');\n"
            + "  document.write('<script src=\"scriptA.js\"></' + 'script>');\n"
            + "  log('3');\n"
            + "  document.write('<script>log(\"foo3\");</' + 'script>');\n"
            + "  log('4');\n"
            + "</script>\n"

            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final URL scriptUrlA = new URL(URL_FIRST, "scriptA.js");

        getMockWebConnection().setDefaultResponse(html);
        getMockWebConnection().setResponse(scriptUrlA, "log('A');\n", MimeType.TEXT_JAVASCRIPT);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "A", "1", "foo2", "2", "3", "4", "B", "foo3"})
    public void writeScriptPostponedBeforeWrite() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('0');\n"
            + "</script>\n"

            + "<script src='scriptA.js'></script>'\n"

            + "<script>\n"
            + "  log('1');\n"
            + "  document.write('<script>log(\"foo2\");</' + 'script>');\n"
            + "  log('2');\n"
            + "  document.write('<script src=\"scriptB.js\"></' + 'script>');\n"
            + "  log('3');\n"
            + "  document.write('<script>log(\"foo3\");</' + 'script>');\n"
            + "  log('4');\n"
            + "</script>\n"

            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final URL scriptUrlA = new URL(URL_FIRST, "scriptA.js");
        final URL scriptUrlB = new URL(URL_FIRST, "scriptB.js");

        getMockWebConnection().setDefaultResponse(html);
        getMockWebConnection().setResponse(scriptUrlA, "log('A');\n", MimeType.TEXT_JAVASCRIPT);
        getMockWebConnection().setResponse(scriptUrlB, "log('B');\n", MimeType.TEXT_JAVASCRIPT);

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for bug 1613119.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"scr1", "scr2", "null", "null", "[object HTMLScriptElement]", "[object HTMLScriptElement]"})
    public void writeAddNodesInCorrectPositions() throws Exception {
        final String html = "<html>\n"
            + "<head></head>\n"
            + "<body id=\"theBody\">\n"
            + "<div id='target1'></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "document.write(\""
            + "<div>"
            + "  <sc\"+\"ript id='scr1'>document.write('<div id=\\\"div1\\\" />');</s\"+\"cript>"
            + "  <sc\"+\"ript id='scr2'>document.write('<div id=\\\"div2\\\" />');</s\"+\"cript>"
            + "</div>"
            + "\");\n"
            + "let html = \""
            + "<div>"
            + "  <sc\"+\"ript id='scr3'>document.write('<div id=\\\"div3\\\" />');</s\"+\"cript>"
            + "  <sc\"+\"ript id='scr4'>document.write('<div id=\\\"div4\\\" />');</s\"+\"cript>"
            + "</div>"
            + "\";"
            + "document.getElementById('target1').innerHTML = html;\n"
            + "</script>\n"
            + "<script>\n"
            + "function logId(obj) { log(obj != null ? obj.id : 'null'); }\n"
            + "logId(document.getElementById('div1').previousSibling);\n"
            + "logId(document.getElementById('div2').previousSibling);\n"
            + "log(document.getElementById('div3'));\n"
            + "log(document.getElementById('div4'));\n"
            + "log(document.getElementById('scr3'));\n"
            + "log(document.getElementById('scr4'));\n"

            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void aboutURL() throws Exception {
        final String html =
            "<html><body><script language='JavaScript'>\n"
            + "w2 = window.open('about:blank', 'AboutBlank');\n"
            + "w2.document.open();\n"
            + "w2.document.write('<html><head><title>hello</title></head><body></body></html>');\n"
            + "w2.document.close();\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        driver.switchTo().window("AboutBlank");
        assertEquals("hello", driver.getTitle());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("§§URL§§")
    public void locationAfterWrite() throws Exception {
        final String html = "<html>\n"
            + "<head><script>\n"
            + "function test() {\n"
            + "  window.document.title += 'abcd';\n"
            + "  document.open();\n"
            + "  document.write("
                    + "'<html><body onload=\"window.document.title += document.location + \\'§\\'\"></body></html>');\n"
            + "  document.close();\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='setTimeout(test, 4);'></body></html>";

        loadPage2(html);
        expandExpectedAlertsVariables(URL_FIRST);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "First", "First", "FORM", "true", "true"},
            FF = {"", "First", "FORM", "true", "true", "First"},
            FF_ESR = {"", "First", "FORM", "true", "true", "First"})
    @HtmlUnitNYI(FF = {"", "First", "First", "FORM", "true", "true"},
            FF_ESR = {"", "First", "First", "FORM", "true", "true"})
    public void newElementsAfterWrite() throws Exception {
        final String html = "<html>"
            + "<head><script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "function test() {\n"
            + "  log(document.title);\n"
            + "  document.open();\n"
            + "  document.write('<html><head><title>First</title></head>');\n"
            + "  document.write('<body onload=\"log(document.title)\">');\n"
            + "  document.write('<form name=\"submitForm\" method=\"post\">');\n"
            + "  document.write('</form></body></html>');\n"
            + "  document.close();\n"
            + "  log(document.title);\n"
            + "  log(document.submitForm.tagName);\n"
            + "  log(window.document == document);\n"
            + "  log(document.submitForm == document.getElementsByTagName('form')[0]);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='setTimeout(test, 4);'></body></html>";

        loadPage2(html);
        verifyWindowName2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
