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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;

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
            + "  <title>Test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var res = document.open();\n"
            + "  alert(res);\n"
            + "  document.close();\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
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
            + "function test() {\n"
            + "  document.write('<html><body><scr'+'ipt>alert(\"Hello There\")</scr'+'ipt></body></html>');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        try {
            loadPageWithAlerts2(html);
        }
        finally {
            shutDownRealIE();
        }
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
            + "var s = '\"<script>alert(1);<\\/scr\" + \"ipt>\"';\n"
            + "document.write('<script><!--\\ndocument.write(' + s + ');\\n--><\\/script>');\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
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
            + "  document.write('<scr'+'ipt>alert(1<2)</sc'+'ript>');\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
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
    @Alerts("HelloHello")
    public void writeExternalScriptAfterClick() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "document.write('<scr'+'ipt src=\"script.js\"></scr'+'ipt>');\n"
            + "</script>\n"
            + "<script>\n"
            + "window.name += window.foo;\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<a href='?again'>a link</a>\n"
            + "<div id='clickMe' onclick='alert(window.name)'>click me</div>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("window.foo = 'Hello'", MimeType.APPLICATION_JAVASCRIPT);
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.linkText("a link")).click();
        driver.findElement(By.id("clickMe")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Regression test for bug 2921851.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
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

        try {
            final WebDriver driver = loadPageWithAlerts2(html);
            assertTitle(driver, "#1");
        }
        finally {
            shutDownRealIE();
        }
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
        try {
            final WebDriver driver = loadPage2(html);
            driver.switchTo().window("myPopup");
            driver.findElement(By.id("it")).click();

            assertEquals(Integer.parseInt(getExpectedAlerts()[0]),
                    getMockWebConnection().getRequestCount() - startCount);
            assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest().getUrl());
        }
        finally {
            shutDownRealIE();
        }
    }

    /**
     * Partial regression test for bug 2921851: the window returned by <tt>window.open()</tt> should
     * be proxied (i.e. "live").
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("<form></form>#[object HTMLFormElement]")
    // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
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

        try {
            final WebDriver driver = loadPage2(html);
            assertTitle(driver, getExpectedAlerts()[0]);
        }
        finally {
            shutDownRealIE();
        }
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
        getMockWebConnection().setDefaultResponse(script, MimeType.APPLICATION_JAVASCRIPT);

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
            + "try {\n"
            + "  var s = document.createElement('script');\n"
            + "  var t = document.createTextNode(\"document.write('in inline script'); document.title = 'done';\");\n"
            + "  s.appendChild(t);\n"
            + "  document.body.appendChild(s);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></div></body></html>";
        final WebDriver driver = loadPageWithAlerts2(html);

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

        getMockWebConnection().setDefaultResponse(js, MimeType.APPLICATION_JAVASCRIPT);
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
            + "    <script>alert(document.body);</script>\n"
            + "    <script>document.write('<span id=\"s1\">1</span>');</script>\n"
            + "    <script>alert(document.body);</script>\n"
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
            + "      alert(s);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
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
            + "    <script>alert(document.body);</script>\n"
            + "    <script>document.write('<span id=\"s1\">1</span>');</script>\n"
            + "    <script>alert(document.body);</script>\n"
            + "    <script>alert(document.body.id);</script>\n"
            + "    <title>test</title>\n"
            + "  </head>\n"
            + "  <body id='foo'>\n"
            + "    <script>alert(document.body.id);</script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
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
            + "    <script>alert('1');</script>\n"
            + "    <script>document.write('<scrip'+'t>alert(\"2\")</s'+'cript>');</script>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <script>document.write('<scrip'+'t>alert(\"3\")</s'+'cript>');</script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("outer")
    public void writeInManyTimes() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  alert(document.getElementById('inner').parentNode.id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<script>\n"
            + "document.write('<div id=\"outer\">');\n"
            + "document.write('<div id=\"inner\"/>');\n"
            + "document.write('</div>');\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
             + "document.write('<p id=\"para1\">Paragraph #1</p>');\n"
             + "document.write('<p id=\"para2\">Paragraph #2</p>');\n"
             + "document.write('<p id=\"para3\">Paragraph #3</p>');\n"
             + "alert(document.getElementById('para1').parentNode.id);\n"
             + "alert(document.getElementById('para2').parentNode.id);\n"
             + "alert(document.getElementById('para3').parentNode.id);\n"
             + "</script>\n"
             + "</body></html>";

        loadPageWithAlerts2(html);
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
             + "function doTest() {\n"
             + "  alert(document.getElementById('inner1').parentNode.id);\n"
             + "  alert(document.getElementById('inner2').parentNode.id);\n"
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

        loadPageWithAlerts2(html);

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
             + "  document.write('<style type=\"text/css\" id=\"myStyle\">');\n"
             + "  document.write('  .nwr {white-space: nowrap;}');\n"
             + "  document.write('</style>');\n"
             + "  document.write('<div id=\"myDiv\">');\n"
             + "  document.write('</div>');\n"
             + "  alert(document.getElementById('myDiv').previousSibling.nodeName);\n"
             + "  alert(document.getElementById('myStyle').previousSibling.nodeName);\n"
             + "</script>\n"
             + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "<body onload='test()'>\n"
            + " Before\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertTitle(driver, "parsed script executed");
        assertEquals("After", driver.findElement(By.tagName("body")).getText());
    }
}
