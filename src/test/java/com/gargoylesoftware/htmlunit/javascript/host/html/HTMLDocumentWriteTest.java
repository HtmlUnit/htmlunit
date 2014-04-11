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

import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

/**
 * Tests for {@link HTMLDocument}'s write(ln) function.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLDocumentWriteTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void write2() throws Exception {
        final String html
            = "<html><head><title>First</title></head><body>\n"
            + "<script>\n"
            + "document.write(\"<div id='div1'></div>\");\n"
            + "document.write('<div', \" id='div2'>\", '</div>');\n"
            + "document.writeln('<div', \" id='div3'>\", '</div>');\n"
            + "</script>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("First", page.getTitleText());

        page.getHtmlElementById("div1");
        page.getHtmlElementById("div2");
        page.getHtmlElementById("div3");
    }

    /**
     * Regression test for bug 715379.
     * @throws Exception if the test fails
     */
    @Test
    public void write_script() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webClient.setWebConnection(webConnection);

        final String mainHtml
            = "<html><head><title>Main</title></head><body>\n"
            + "<iframe name='iframe' id='iframe' src='http://first'></iframe>\n"
            + "<script type='text/javascript'>\n"
            + "document.write('<script type=\"text/javascript\" src=\"http://script\"></' + 'script>');\n"
            + "</script></body></html>";
        webConnection.setResponse(new URL("http://main/"), mainHtml);

        final String firstHtml = "<html><body><h1 id='first'>First</h1></body></html>";
        webConnection.setResponse(URL_FIRST, firstHtml);

        final String secondHtml = "<html><body><h1 id='second'>Second</h1></body></html>";
        webConnection.setResponse(URL_SECOND, secondHtml);

        final String script = "document.getElementById('iframe').src = '" + URL_SECOND + "';\n";
        webConnection.setResponse(new URL("http://script/"), script, JAVASCRIPT_MIME_TYPE);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage mainPage = webClient.getPage("http://main");
        assertEquals("Main", mainPage.getTitleText());

        final HtmlInlineFrame iFrame = mainPage.getHtmlElementById("iframe");

        assertEquals(URL_SECOND.toExternalForm(), iFrame.getSrcAttribute());

        final HtmlPage enclosedPage = (HtmlPage) iFrame.getEnclosedPage();
        // This will blow up if the script hasn't been written to the document
        // and executed so the second page has been loaded.
        enclosedPage.getHtmlElementById("second");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A")
    public void write_InDOM() throws Exception {
        final String html
            = "<html><head><title>First</title></head><body>\n"
            + "<script type='text/javascript'>\n"
            + "document.write('<a id=\"blah\">Hello World</a>');\n"
            + "document.write('<a id=\"blah2\">Hello World 2</a>');\n"
            + "alert(document.getElementById('blah').tagName);\n"
            + "</script>\n"
            + "<a id='blah3'>Hello World 3</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);

        assertEquals("First", page.getTitleText());
        assertEquals(3, page.getElementsByTagName("a").getLength());
    }

    /**
     * IE accepts the use of detached functions, but FF doesn't.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "", FF = "exception occurred")
    public void write_AssignedToVar() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTheFoo() {\n"
            + "var d = document.writeln;\n"
            + "try {\n"
            + "  d('foo')\n"
            + "} catch (e) { alert('exception occurred') }\n"
            + "  document.writeln('foo')\n"
            + "}\n"
            + "</script></head><body onload='doTheFoo()'>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test for bug 1950462: calling document.write inside a function (after assigning
     * document.write to a local variable) tries to invoke document.write on the prototype
     * document instance, rather than the actual document host object. This leads to an
     * {@link IllegalStateException} (DomNode has not been set for this SimpleScriptable).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = "", FF = "exception occurred")
    public void write_AssignedToVar2() throws Exception {
        final String html =
            "<html><head><title>Test</title></head><body>\n"
            + "<script>\n"
            + "  function foo() { var d = document.write; d(4); }\n"
            + "  try {"
            + "    foo();"
            + "  } catch (e) { alert('exception occurred'); document.write(4); }\n"
            + "</script>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        assertEquals("Test", page.getTitleText());
        assertEquals("4", page.getBody().asText());
    }

    /**
     * Verifies that calling document.write() after document parsing has finished results in an whole
     * new page being loaded.
     * @throws Exception if an error occurs
     */
    @Test
    public void write_WhenParsingFinished() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() { document.write(1); document.write(2); document.close(); }\n"
            + "</script></head>\n"
            + "<body><span id='s' onclick='test()'>click</span></body></html>";

        HtmlPage page = loadPage(getBrowserVersion(), html, null);
        assertEquals("click", page.getBody().asText());

        final HtmlSpan span = page.getHtmlElementById("s");
        page = span.click();
        assertEquals("12", page.getBody().asText());
    }

    /**
     * Verifies that scripts added to the document via document.write(...) don't execute until the current script
     * finishes executing; bug found at <a href="http://code.google.com/apis/maps/">the Google Maps API site</a>.
     * @throws Exception if an error occurs
     */
    @Test
    public void write_scriptExecutionPostponed() throws Exception {
        final String html
            = "<html><body><div id='d'></div>\n"
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
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(new URL(URL_FIRST, "a.js"), "log(1)", JAVASCRIPT_MIME_TYPE);
        conn.setResponse(new URL(URL_FIRST, "b.js"), "log(4)", JAVASCRIPT_MIME_TYPE);
        conn.setResponse(new URL(URL_FIRST, "c.js"), "log(5)", JAVASCRIPT_MIME_TYPE);
        conn.setResponse(new URL(URL_FIRST, "d.js"), "log(10)", JAVASCRIPT_MIME_TYPE);
        conn.setResponse(new URL(URL_FIRST, "e.js"), "log(11)", JAVASCRIPT_MIME_TYPE);
        conn.setResponse(new URL(URL_FIRST, "f.js"), "log(12)", JAVASCRIPT_MIME_TYPE);
        client.setWebConnection(conn);
        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("1 2 3 4 5 6 7 8 9 10 11 12", page.getBody().asText().trim());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "§§URL§§", "§§URL§§" })
    public void locationAfterWrite() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "function test() {\n"
            + "  alert(document.location);\n"
            + "  document.open();\n"
            + "  document.write('<html><body onload=\"alert(document.location)\"></body></html>');\n"
            + "  document.close();\n"
            + "}"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "First", "First", "FORM", "true", "true" })
    public void newElementsAfterWrite() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "function test() {\n"
            + "  alert(document.title);\n"
            + "  document.open();\n"
            + "  document.write('<html><head><title>First</title></head>');"
            + "  document.write('<body onload=\"alert(document.title)\">');"
            + "  document.write('<form name=\"submitForm\" method=\"post\">');"
            + "  document.write('</form></body></html>');\n"
            + "  document.close();\n"
            + "  alert(document.title);\n"
            + "  alert(document.submitForm.tagName);\n"
            + "  alert(window.document == document);\n"
            + "  alert(document.submitForm == document.getElementsByTagName('form')[0]);\n"
            + "}"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeWithSplitAnchorTag() throws Exception {
        final String html = "<html><body><script>\n"
            + "document.write(\"<a href=\'start.html\");\n"
            + "document.write(\"\'>\");\n"
            + "document.write('click here</a>');\n"
            + "</script>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final List<HtmlAnchor> anchorList = page.getAnchors();
        assertEquals(1, anchorList.size());
        final HtmlAnchor anchor = anchorList.get(0);
        assertEquals("start.html", anchor.getHrefAttribute());
        assertEquals("click here", anchor.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeScriptInManyTimes() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "document.write('<script src=\"script.js\">');\n"
            + "document.write('<' + '/script>');\n"
            + "document.write('<script>alert(\"foo2\");</' + 'script>');\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"foo", "foo2"};

        final URL scriptUrl = new URL(URL_FIRST + "script.js");
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        client.setWebConnection(webConnection);
        webConnection.setDefaultResponse(html);
        webConnection.setResponse(scriptUrl, "alert('foo');\n", JAVASCRIPT_MIME_TYPE);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        client.getPage(URL_FIRST);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for bug 1613119.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "scr1", "scr2"/*, "scr3", "scr4"*/ })
    public void writeAddNodesInCorrectPositions() throws Exception {
        final String html = "<html><head><title>foo</title></head>\n"
            + "<body id=\"theBody\">\n"
            + "<div id='target1'></div>\n"
            + "<script>\n"
            + "document.write(\""
            + "<div>"
            + "  <sc\"+\"ript id='scr1'>document.write('<div id=\\\"div1\\\" />');</s\"+\"cript>"
            + "  <sc\"+\"ript id='scr2'>document.write('<div id=\\\"div2\\\" />');</s\"+\"cript>"
            + "</div>"
            + "\");\n"
 /*           + "document.getElementById('target1').innerHTML = \""
            + "<div>\n"
            + "  <sc\"+\"ript id='scr3'>document.write('<div id=\\\"div3\\\" />');</s\"+\"cript>\n"
            + "  <sc\"+\"ript id='scr4'>document.write('<div id=\\\"div4\\\" />');</s\"+\"cript>\n"
            + "</div>\n"
            + "\";\n"
  */
            + "</script>\n"
            + "<script>\n"
            + "function alertId(obj) { alert(obj != null ? obj.id : 'null'); }\n"
            + "alertId(document.getElementById('div1').previousSibling);\n"
            + "alertId(document.getElementById('div2').previousSibling);\n"
 /*           + "alertId(document.getElementById('div3').previousSibling);\n"
            + "alertId(document.getElementById('div4').previousSibling);\n"
  */
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeFrameRelativeURLMultipleFrameset() throws Exception {
        final String html = "<html><head><title>frameset</title></head>\n"
            + "<script>\n"
            + "    document.write('<frameset><frame src=\"frame.html\"/></frameset>');\n"
            + "</script>\n"
            + "<frameset><frame src='blank.html'/></frameset>\n"
            + "</html>";

        final URL baseURL = new URL("http://base/subdir/");
        final URL framesetURL = new URL(baseURL + "test.html");
        final URL frameURL = new URL(baseURL + "frame.html");
        final URL blankURL = new URL(baseURL + "blank.html");

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(framesetURL, html);
        webConnection.setResponseAsGenericHtml(frameURL, "frame");
        webConnection.setResponseAsGenericHtml(blankURL, "blank");
        client.setWebConnection(webConnection);

        final HtmlPage framesetPage = client.getPage(framesetURL);
        final FrameWindow frame = framesetPage.getFrames().get(0);
        final HtmlPage framePage = (HtmlPage) frame.getEnclosedPage();

        assertNotNull(frame);
        assertEquals(frameURL.toExternalForm(), framePage.getUrl().toExternalForm());
        assertEquals("frame", framePage.getTitleText());
    }

    /**
     * Verifies that calls to document.open() are ignored while the page's HTML is being parsed.
     * @throws Exception if an error occurs
     */
    @Test
    public void open_IgnoredDuringParsing() throws Exception {
        final String html = "<html><body>1<script>document.open();document.write('2');</script>3</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        assertEquals("123", page.getBody().asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeWithSpace() throws Exception {
        final String html = "<html><body><script>\n"
            + "document.write('Hello ');\n"
            + "document.write('World');\n"
            + "</script>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        assertTrue(page.asText().contains("Hello World"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void aboutURL() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final String firstContent =
            "<html><body><script language='JavaScript'>\n"
            + "w2 = window.open('about:blank', 'AboutBlank');\n"
            + "w2.document.open();\n"
            + "w2.document.write('<html><head><title>hello</title></head><body></body></html>');\n"
            + "w2.document.close();\n"
            + "</script></body></html>";
        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection(webConnection);

        webClient.getPage(URL_FIRST);
        final WebWindow webWindow = webClient.getWebWindowByName("AboutBlank");
        assertNotNull(webWindow);

        //  final HtmlPage page = (HtmlPage) webWindow.getEnclosedPage();
        //  assertEquals("<html><head><title>hello</title></head><body></body></html>",page.getDocument().toString());
    }

}
