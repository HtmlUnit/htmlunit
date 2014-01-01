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

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLDocument}'s write(ln) function.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLDocumentWrite2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Hello There")
    // TODO [IE11] real IE11 waits for the page to load until infinity
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
            + "<body onload='test()'>"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
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
     * We couldn't document.write() script elements that contained the '<' character...
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

        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html; charset=UTF-8", "UTF-8");
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

        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html; charset=ISO-8859-1", "ISO-8859-1");
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
            + "    <input value='Click Me' type=button onclick='performAction()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        driver.findElement(By.xpath("//input")).click();
    }

    /**
     * Regression test for bug 2884585.
     * As of HtmlUnit-2.7-SNAPSHOT 17.01.2010 <script src="..."... written
     * by document.write was not loaded and executed after the </script>
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

        getMockWebConnection().setDefaultResponse("window.foo = 'Hello'", JAVASCRIPT_MIME_TYPE);
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.linkText("a link")).click();
        driver.findElement(By.id("clickMe")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Regression test for bug 2921851.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void writeInNewWindowAndReadFormCollection() throws Exception {
        final String html = "<html><head>"
            + "<script>"
            + "function test() {"
            + "  var newWin = window.open('', 'myPopup', '');"
            + "  var newDoc = newWin.document;"
            + "  newDoc.write('<html><body><form name=newForm></form></body></html>');"
            + "  alert(newDoc.forms.length);"
            + "}\n"
            + "</script></head>"
            + "<body onload='test()'>"
            + "</body></html>";

        loadPageWithAlerts2(html);

        // for some reason, the selenium driven browser is in an invalid state after this test
        shutDownAll();
    }

    /**
     * Partial regression test for bug 2921851: use opener URL as base URL
     * for resolution of relative URLs in document.write.
     * @throws Exception if the test fails
     */
    @Test
    public void urlResolutionInWriteForm() throws Exception {
        final String html = "<html><head>"
            + "<script>"
            + "function test() {"
            + "  var newWin = window.open('', 'myPopup', '');"
            + "  var d = newWin.document;"
            + "  d.write('<html><body><form action=foo method=post><input type=submit id=it></form></body></html>');"
            + "  d.close();\n"
            + "}\n"
            + "</script></head>"
            + "<body onload='test()'>"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        driver.switchTo().window("myPopup");
        driver.findElement(By.id("it")).click();

        assertEquals(new URL(getDefaultUrl(), "foo"), getMockWebConnection().getLastWebRequest().getUrl());

        // for some reason, the selenium driven browser is in an invalid state after this test
        shutDownAll();
    }

    /**
     * Partial regression test for bug 2921851: the window returned by <tt>window.open()</tt> should
     * be proxied (i.e. "live").
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "<form></form>", "[object HTMLFormElement]" },
            IE8 = { "<FORM></FORM>", "[object]" })
    // TODO [IE11]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void writeOnOpenedWindow_WindowIsProxied() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test(){\n"
            + "var w = window.open('','blah','width=460,height=420');\n"
            + "w.document.write('<html><body><form></form></body></html>');\n"
            + "w.document.close();\n"
            + "alert(w.document.body.innerHTML);\n"
            + "alert(w.document.forms[0]);\n"
            + "}\n"
            + "</script></head><body onload='test()'>foo</body></html>";
        loadPageWithAlerts2(html);

        // for some reason, the selenium driven browser is in an invalid state after this test
        shutDownAll();
    }

    /**
     * Partial regression test for bug 2921851: the document returned by <tt>window.document</tt> should
     * be proxied (i.e. "live").
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "<form></form>", "[object HTMLFormElement]" },
            IE8 = { "<FORM></FORM>", "[object]" })
    public void writeOnOpenedWindow_DocumentIsProxied() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test(){\n"
            + "var w = window.open('','blah','width=460,height=420');\n"
            + "var d = w.document;\n"
            + "d.write('<html><body><form></form></body></html>');\n"
            + "d.close();\n"
            + "alert(d.body.innerHTML);\n"
            + "alert(d.forms[0]);\n"
            + "}\n"
            + "</script></head><body onload='test()'>foo</body></html>";
        loadPageWithAlerts2(html);

        // for some reason, the selenium driven browser is in an invalid state after this test
        shutDownAll();
    }

    /**
     * This was causing a StackOverflowError in HtmlUnit-2.10-SNAPSHOT when simulating IE as of 14.10.2011
     * and probably in release 2.9 as well.
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
        getMockWebConnection().setDefaultResponse(script, JAVASCRIPT_MIME_TYPE);

        final WebDriver driver = loadPage2(html);
        assertEquals("First", driver.getTitle());

        assertEquals("hello", driver.findElement(By.id("div1")).getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE8 = "exception")
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

        if (getExpectedAlerts().length == 0) {
            assertEquals("done", driver.getTitle());
            assertEquals("in inline script", driver.findElement(By.id("it")).getText());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void write_fromScriptAddedWithAppendChild_external() throws Exception {
        final String html = "<html><head></head><body>\n"
                + "<div id='it'>here</div><script>\n"
                + "  var s = document.createElement('script');\n"
                + "  s.src = 'foo.js'\n"
                + "  document.body.appendChild(s);\n"
                + "</script></body></html>";

        final String js = "document.write('from external script');\n"
                    + "document.title = 'done';";

        getMockWebConnection().setDefaultResponse(js, JAVASCRIPT_MIME_TYPE);
        final WebDriver driver = loadPage2(html);

        assertEquals("done", driver.getTitle());
        assertEquals("here", driver.findElement(By.id("it")).getText());
        assertEquals("here", driver.findElement(By.tagName("body")).getText());
    }
}
