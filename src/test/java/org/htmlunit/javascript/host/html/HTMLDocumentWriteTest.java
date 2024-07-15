/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
import java.util.List;

import org.htmlunit.CollectingAlertHandler;
import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.WebWindow;
import org.htmlunit.html.FrameWindow;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link HTMLDocument}'s write(ln) function.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLDocumentWriteTest extends SimpleWebTestCase {

    /**
     * IE accepts the use of detached functions.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception occurred")
    public void write_AssignedToVar() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTheFoo() {\n"
            + "var d = document.writeln;\n"
            + "try {\n"
            + "  d('foo');\n"
            + "} catch (e) { alert('exception occurred') }\n"
            + "  document.writeln('foo');\n"
            + "}\n"
            + "</script></head><body onload='doTheFoo()'>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§", "§§URL§§"})
    public void locationAfterWrite() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "function test() {\n"
            + "  alert(document.location);\n"
            + "  document.open();\n"
            + "  document.write('<html><body onload=\"alert(document.location)\"></body></html>');\n"
            + "  document.close();\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "First", "First", "FORM", "true", "true"})
    public void newElementsAfterWrite() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "function test() {\n"
            + "  alert(document.title);\n"
            + "  document.open();\n"
            + "  document.write('<html><head><title>First</title></head>');\n"
            + "  document.write('<body onload=\"alert(document.title)\">');\n"
            + "  document.write('<form name=\"submitForm\" method=\"post\">');\n"
            + "  document.write('</form></body></html>');\n"
            + "  document.close();\n"
            + "  alert(document.title);\n"
            + "  alert(document.submitForm.tagName);\n"
            + "  alert(window.document == document);\n"
            + "  alert(document.submitForm == document.getElementsByTagName('form')[0]);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts(html);
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

        final URL scriptUrl = new URL(URL_FIRST, "script.js");
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        client.setWebConnection(webConnection);
        webConnection.setDefaultResponse(html);
        webConnection.setResponse(scriptUrl, "alert('foo');\n", MimeType.TEXT_JAVASCRIPT);

        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        client.getPage(URL_FIRST);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for bug 1613119.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"scr1", "scr2"/*, "scr3", "scr4"*/})
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
        final HtmlPage page = loadPage(html);
        assertEquals("123", page.getBody().asNormalizedText());
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

        final HtmlPage page = loadPage(html);
        assertTrue(page.asNormalizedText().contains("Hello World"));
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
