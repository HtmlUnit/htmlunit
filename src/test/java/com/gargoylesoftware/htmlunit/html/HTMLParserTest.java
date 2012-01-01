/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Test class for {@link HTMLParser}.
 *
 * @version $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 */
@RunWith(BrowserRunner.class)
public class HTMLParserTest extends WebServerTestCase {

    /**
     * Tests the new HTMLParser on a simple HTML string.
     * @throws Exception failure
     */
    @Test
    public void simpleHTMLString() throws Exception {
        final WebClient webClient = getWebClient();
        final WebResponse webResponse = new StringWebResponse(
            "<html><head><title>TITLE</title><div>TEST</div></head><body></body></html>", getDefaultUrl());

        final HtmlPage page = HTMLParser.parseHtml(webResponse, webClient.getCurrentWindow());

        final String stringVal = page.<HtmlDivision>getFirstByXPath("//div").getFirstChild().getNodeValue();
        assertEquals("TEST", stringVal);

        final HtmlElement node = (HtmlElement) page.getFirstByXPath("//*[./text() = 'TEST']");
        assertEquals(node.getTagName(), HtmlDivision.TAG_NAME);
    }

    /**
     * Tests that inserted TBODY and TFOOT don't conflict.
     * @throws Exception failure
     */
    @Test
    @Alerts("TABLE")
    public void table_tfoot() throws Exception {
        final String html = "<html><body>"
            + "<table><tr><td>hello</td></tr>\n"
            + "<tfoot id='tf'><tr><td>foot</td></tr></tfoot>"
            + "</table>\n"
            + "<script>\n"
            + "alert(document.getElementById('tf').parentNode.nodeName)\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test for the condition when there is a <tt>&lt;form&gt;</tt> inside of a <tt>&lt;table&gt;</tt> and before
     * a <tt>&lt;tr&gt;</tt>.
     * @throws Exception failure
     */
    @Test
    @Alerts("myForm")
    public void badlyFormedHTML() throws Exception {
        final String html
            = "<html><head><title>first</title>\n"
            + "     <script>\n"
            + "         function test(){\n"
            + "             alert(document.getElementById('myInput').form.id);\n"
            + "         }\n"
            + "     </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "     <table>\n"
            + "         <form name='myForm' action='foo' id='myForm'>\n"
            + "         <tr><td>\n"
            + "         <input type='text' name='myInput' id='myInput'/>\n"
            + "         </td></tr>\n"
            + "         </form>\n"
            + "     </table>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test when an illegal tag is found in head as some websites do.
     * @throws Exception failure
     */
    @Test
    @Alerts("first")
    public void unknownTagInHead() throws Exception {
        // Note: the <meta> tag in this test is quite important because
        // I could adapt the TagBalancer to make it work except with this <meta http-equiv...
        // (it worked with <meta name=...)
        final String html
            = "<html><head><mainA3>\n"
            + "     <meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>\n"
            + "     <title>first</title>\n"
            + "     <script>\n"
            + "         function test(){\n"
            + "             alert(document.title);\n"
            + "         }\n"
            + "     </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Works since NekoHtml 0.9.5.
     * @exception Exception If the test fails
     */
    @Test
    public void badTagInHead() throws Exception {
        final String htmlContent = "<html>\n" + "<head><foo/>\n<title>foo\n</head>\n"
                + "<body>\nfoo\n</body>\n</html>";

        final HtmlPage page = loadPageWithAlerts(htmlContent);
        assertEquals("foo", page.getTitleText());
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"false", "true" })
    public void duplicatedAttribute() throws Exception {
        final String html
            = "<html><head>\n"
            + "</head>\n"
            + "     <script>\n"
            + "         function test() {\n"
            + "             alert(document.getElementById('foo') == null);\n"
            + "             alert(document.getElementById('bla') == null);\n"
            + "         }\n"
            + "     </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "     <span id='foo' id='bla'></span>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts(IE = {"1", "3", "[object]", "[object]", "[object]", "[object]" },
            FF = {"1", "3", "[object HTMLScriptElement]",
                "[object HTMLUnknownElement]", "[object HTMLUnknownElement]", "[object HTMLFormElement]" })
    public void namespace() throws Exception {
        final String html
            = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:app='http://www.appcelerator.org'>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('script1'));\n"
            + "    alert(document.getElementById('script2'));\n"
            + "    alert(document.getElementById('message1'));\n"
            + "    alert(document.getElementById('form1'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<script id='script1'>alert(1)</script>\n"
            + "<app:script id='script2'>alert(2)</app:script>\n"
            + "<script>alert(3)</script>\n"
            + "<app:message name='r:tasks.request' id='message1'>hello</app:message>\n"
            + "<form id='form1' xmlns='http://org.pentaho'/>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts(IE = {"1", "createElementNS() is not defined",
                "[object]", "SCRIPT,SCRIPT,undefined,undefined,undefined",
                "[object]", "script,script,undefined,undefined,undefined" },
            FF = {"1", "[object Element]", "app:script,app:script,http://www.appcelerator.org,app,script",
                "[object HTMLScriptElement]", "SCRIPT,SCRIPT,null,null,SCRIPT",
                "[object HTMLUnknownElement]", "APP:SCRIPT,APP:SCRIPT,null,null,APP:SCRIPT" })
    public void namespace2() throws Exception {
        final String html
            = "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:app='http://www.appcelerator.org'>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      div = document.createElementNS('http://www.appcelerator.org', 'app:script');\n"
            + "      debug(div);\n"
            + "    } catch (e) {alert('createElementNS() is not defined')}\n"
            + "    debug(document.getElementById('script1'));\n"
            + "    debug(document.getElementById('script2'));\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    alert(e);\n"
            + "    alert(e.nodeName + ',' + e.tagName + ',' + e.namespaceURI + ',' + e.prefix + ',' + e.localName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<script id='script1'>alert(1)</script>\n"
            + "<app:script id='script2'>alert(2)</app:script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test for a case where complete HTML page is present inside DIV tag.
     * IE ignores the HTML tag, BODY tag and complete HEAD along with content inside HEAD
     * FF ignores only HTML, HEAD and BODY tags. Contents of HEAD are added to head of the page
     * and content of BODY are added to the current node (DIV tag in test case).
     *
     * @throws Exception failure
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = {"HEAD", "Outer Html", "outerDiv" },
            FF = {"HEAD", "Outer Html", "HEAD", "Inner Html", "outerDiv" })
    public void completeHtmlInsideDiv() throws Exception {
        final String html
            = "<html><head>\n"
            + "    <title>Outer Html</title>\n"
            + "    <script>\n"
            + "         function test() {\n"
            + "         var titles = document.getElementsByTagName('title');\n"
            + "         for(var i=0; i < titles.length; ++i) {\n"
            + "             alert(titles[i].parentNode.nodeName);\n"
            + "             alert(titles[i].text);\n"
            + "         }\n"
            + "         var bodyTitles = document.body.getElementsByTagName('title');\n"
            + "         for(var i=0; i < bodyTitles.length; ++i) {\n"
            + "             alert(bodyTitles[i].parentNode.nodeName);\n"
            + "             alert(bodyTitles[i].text);\n"
            + "         }\n"
            + "         var innerDiv = document.getElementById('innerDiv');\n"
            + "         alert(innerDiv.parentNode.id);\n"
            + "     }\n"
            + "     </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "    <DIV id=outerDiv>\n"
            + "         Outer DIV\n"
            + "         <html>\n"
            + "             <head><title>Inner Html</title></head>\n"
            + "             <body>\n"
            + "                 <DIV id=innerDiv>Inner DIV</DIV>\n"
            + "             </body>\n"
            + "         </html>\n"
            + "    </DIV>\n"
            + "</body>\n"
            + "</html>\n";

        loadPageWithAlerts(html);
    }

    /**
     * Test for a case where complete HTML page is added using document.write() inside DIV tag.
     * IE ignores the HTML tag, BODY tag and complete HEAD along with content inside HEAD
     * FF ignores only HTML, HEAD and BODY tags. Contents of HEAD are added to head of the page
     * and content of BODY are added to the current node (DIV tag in test case).
     *
     * @throws Exception failure
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = {"HEAD", "Outer Html", "outerDiv" },
            FF = {"HEAD", "Outer Html", "HEAD", "Inner Html", "outerDiv" })
    public void writeCompleteHtmlInsideDIV() throws Exception {
        final String html
            = "<html><head>\n"
            + "    <title>Outer Html</title>\n"
            + "    <script>\n"
            + "         function test() {\n"
            + "         var titles = document.getElementsByTagName('title');\n"
            + "         for(var i=0; i < titles.length; ++i) {\n"
            + "             alert(titles[i].parentNode.nodeName);\n"
            + "             alert(titles[i].text);\n"
            + "         }\n"
            + "         var bodyTitles = document.body.getElementsByTagName('title');\n"
            + "         for(var i=0; i < bodyTitles.length; ++i) {\n"
            + "             alert(bodyTitles[i].parentNode.nodeName);\n"
            + "             alert(bodyTitles[i].text);\n"
            + "         }\n"
            + "         var innerDiv = document.getElementById('innerDiv');\n"
            + "         alert(innerDiv.parentNode.id);\n"
            + "     }\n"
            + "     </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "    <DIV id=outerDiv>\n"
            + "         Outer DIV\n"
            + "         <script>\n"
            + "         document.write('<html><head><title>Inner Html</title></head>"
            + "             <body><DIV id=innerDiv>Inner DIV</DIV></body></html>');\n"
            + "         </script>\n"
            + "    </DIV>\n"
            + "</body>\n"
            + "</html>\n";

        loadPageWithAlerts(html);
    }

    /**
     * Test for a case where complete HTML page is set in innerHTML of DIV tag.
     * Behavior is same for any TAG inside body including BODY tag.
     * IE ignores the HTML tag, BODY tag and complete HEAD along with content inside HEAD
     * FF ignores only HTML, HEAD and BODY tags. Contents of HEAD and BODY are added to
     * the current node (DIV tag in test case).
     *
     * @throws Exception failure
     */
    @Test
    @Alerts(IE = {"HEAD", "Outer Html", "outerDiv" },
            FF = {"HEAD", "Outer Html", "DIV", "Inner Html", "DIV", "Inner Html", "outerDiv" })
    public void setCompleteHtmlToDIV_innerHTML() throws Exception {
        final String html
            = "<html><head>\n"
            + "    <title>Outer Html</title>\n"
            + "    <script>\n"
            + "         function test() {\n"
            + "         var titles = document.getElementsByTagName('title');\n"
            + "         for(var i=0; i < titles.length; ++i) {\n"
            + "             alert(titles[i].parentNode.nodeName);\n"
            + "             alert(titles[i].text);\n"
            + "         }\n"
            + "         var bodyTitles = document.body.getElementsByTagName('title');\n"
            + "         for(var i=0; i < bodyTitles.length; ++i) {\n"
            + "             alert(bodyTitles[i].parentNode.nodeName);\n"
            + "             alert(bodyTitles[i].text);\n"
            + "         }\n"
            + "         var innerDiv = document.getElementById('innerDiv');\n"
            + "         alert(innerDiv.parentNode.id);\n"
            + "     }\n"
            + "     </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "    <DIV id=outerDiv>\n"
            + "         Outer DIV\n"
            + "    </DIV>\n"
            + "         <script>\n"
            + "         document.getElementById('outerDiv').innerHTML ="
            + "             '<html><head><title>Inner Html</title></head>"
            + "             <body><DIV id=innerDiv>Inner DIV</DIV></body></html>';\n"
            + "         </script>\n"
            + "</body>\n"
            + "</html>\n";

        loadPageWithAlerts(html);
    }

    /**
     * Test for a case where complete HTML page is set in innerHTML of HTML tag.
     * IE throws JavaScript error as innerHTML of HTML is read only
     * FF ignores HTML, HEAD and BODY tags. Contents of HEAD and BODY are added to HTML node.
     * Resulting DOM has only TITLE and DIV tag under HTML. There is no HEAD or BODY node.
     *
     * @throws Exception failure
     */
    @Test
    @Alerts(IE = { "exception", "HEAD", "Outer Html", "true" },
            FF = { "HTML", "Inner Html", "false", "HTML" })
    public void setComplteHtmlToHTML_innerHTML() throws Exception {
        final String html
            = "<html><head>\n"
            + "    <title>Outer Html</title>\n"
            + "    <script>\n"
            + "         function test() {\n"
            + "         var titles = document.getElementsByTagName('title');\n"
            + "         for(var i=0; i < titles.length; ++i) {\n"
            + "             alert(titles[i].parentNode.nodeName);\n"
            + "             alert(titles[i].text);\n"
            + "         }\n"
            + "         alert(document.body != null);\n"
            + "         var innerDiv = document.getElementById('innerDiv');\n"
            + "         if (innerDiv != null) {\n"
            + "             alert(innerDiv.parentNode.nodeName);\n"
            + "         }\n"
            + "     }\n"
            + "     </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "    <DIV id=outerDiv>\n"
            + "         Outer DIV\n"
            + "    </DIV>\n"
            + "<script>\n"
            + "  try {\n"
            + "    document.getElementsByTagName('html')[0].innerHTML ="
            + "      '<html><head><title>Inner Html</title></head>"
            + "      <body><DIV id=innerDiv>Inner DIV</DIV></body></html>';\n"
            + "  } catch(e) { alert('exception') }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>\n";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = { "2", "1", "2", "1", "1", "1", "2", "2", "1", "1", "1", "1" },
            FF = { "2", "2", "3", "3", "2", "2", "3", "2", "2", "3", "2", "2" })
    public void childNodes_p() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  for (var i = 1; i <= 12; i++) {\n"
            + "    alert(document.getElementById('p' + i).childNodes.length);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<p id='p1'><input> </p>\n"
            + "<p id='p2'> <input></p>\n"
            + "<p id='p3'> <input> </p>\n"
            + "<p id='p4'> <a></a> </p>\n"
            + "<p id='p5'><a></a> </p>\n"
            + "<p id='p6'> <a></a></p>\n"
            + "<p id='p7'> <a href='hi'>there</a> </p>\n"
            + "<p id='p8'><a href='hi'>there</a> </p>\n"
            + "<p id='p9'> <a href='hi'>there</a></p>\n"
            + "<p id='p10'> <a href='hi'></a> </p>\n"
            + "<p id='p11'><a href='hi'></a> </p>\n"
            + "<p id='p12'> <a href='hi'></a></p>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = { "2", "1", "2", "1", "1", "1", "2", "2", "1", "1", "1", "1", "1" },
            FF = { "2", "2", "3", "3", "2", "2", "3", "2", "2", "3", "2", "2", "3" })
    public void childNodes_f() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  for (var i = 1; i <= 13; i++) {\n"
            + "    alert(document.getElementById('f' + i).childNodes.length);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form id='f1'><input> </form>\n"
            + "<form id='f2'> <input></form>\n"
            + "<form id='f3'> <input> </form>\n"
            + "<form id='f4'> <a></a> </form>\n"
            + "<form id='f5'><a></a> </form>\n"
            + "<form id='f6'> <a></a></form>\n"
            + "<form id='f7'> <a href='hi'>there</a> </form>\n"
            + "<form id='f8'><a href='hi'>there</a> </form>\n"
            + "<form id='f9'> <a href='hi'>there</a></form>\n"
            + "<form id='f10'> <a href='hi'></a> </form>\n"
            + "<form id='f11'><a href='hi'></a> </form>\n"
            + "<form id='f12'> <a href='hi'></a></form>\n"
            + "<form id='f13'> <div> </div> </form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = { "1", "1", "1", "2", "2", "1" }, FF = { "3", "2", "2", "3", "2", "2" })
    public void childNodes_span() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  for (var i = 1; i <= 6; i++) {\n"
            + "    alert(document.getElementById('p' + i).childNodes.length);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<p id='p1'> <span></span> </p>\n"
            + "<p id='p2'><span></span> </p>\n"
            + "<p id='p3'> <span></span></p>\n"
            + "<p id='p4'> <span>something</span> </p>\n"
            + "<p id='p5'><span>something</span> </p>\n"
            + "<p id='p6'> <span>something</span></p>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = { "1", "1", "1", "2", "2", "1" }, FF = { "3", "2", "2", "3", "2", "2" })
    public void childNodes_font() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  for (var i = 1; i <= 6; i++) {\n"
            + "    alert(document.getElementById('p' + i).childNodes.length);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<p id='p1'> <font></font> </p>\n"
            + "<p id='p2'><font></font> </p>\n"
            + "<p id='p3'> <font></font></p>\n"
            + "<p id='p4'> <font>something</font> </p>\n"
            + "<p id='p5'><font>something</font> </p>\n"
            + "<p id='p6'> <font>something</font></p>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headerVsMetaTagContentType_both() throws Exception {
        HeaderVsMetaTagContentTypeServlet.setEncoding("UTF-8", "ISO-8859-1");
        headerVsMetaTagContentType(true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headerVsMetaTagContentType_bothReversed() throws Exception {
        HeaderVsMetaTagContentTypeServlet.setEncoding("ISO-8859-1", "UTF-8");
        headerVsMetaTagContentType(false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headerVsMetaTagContentType4_headerOnly() throws Exception {
        HeaderVsMetaTagContentTypeServlet.setEncoding("UTF-8", null);
        headerVsMetaTagContentType(true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headerVsMetaTagContentType_metaOnly() throws Exception {
        HeaderVsMetaTagContentTypeServlet.setEncoding(null, "UTF-8");
        headerVsMetaTagContentType(true);
    }

    private void headerVsMetaTagContentType(final boolean utf8Encoded) throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", HeaderVsMetaTagContentTypeServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/test");
        assertEquals(utf8Encoded, HeaderVsMetaTagContentTypeServlet.utf8String.equals(page.asText()));
    }

    /**
     * Servlet for {@link #headerVsMetaTagContentType(boolean)}.
     */
    public static class HeaderVsMetaTagContentTypeServlet extends HttpServlet {
        private static final String utf8String = "\u064A\u0627 \u0644\u064A\u064A\u064A\u064A\u0644";
        private static String HEADER_ENCODING_;
        private static String META_TAG_ENCODING_;

        private static void setEncoding(final String headerEncoding, final String metaTagEncoding) {
            HEADER_ENCODING_ = headerEncoding;
            META_TAG_ENCODING_ = metaTagEncoding;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            if (HEADER_ENCODING_ != null) {
                response.setCharacterEncoding(HEADER_ENCODING_);
            }
            final Writer writer = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            String html = "<html><head>";
            if (META_TAG_ENCODING_ != null) {
                html += "<META HTTP-EQUIV='Content-Type' CONTENT='text/html; charset=" + META_TAG_ENCODING_ + "'>";
            }
            html += "</head><body>" + utf8String + "</body></html>";
            writer.write(html);
            writer.close();
        }
    }

    /**
     * Regression test for bug 2523870: parse failure when parsing page with UTF-8 BOM (byte order mark).
     * The HTML file used is from NekoHTML's bug number 2560899.
     * @throws Exception if an error occurs
     */
    @Test
    public void bomUtf8() throws Exception {
        final String resource = "bom-utf8.html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);

        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(url);
        assertEquals("Welcome to Suffolk Coastal District Council online", page.getTitleText());
    }

    /**
     * This HTML was causing an EmptyStackException to be thrown.
     * @throws Exception if an error occurs
     */
    @Test
    public void emptyStack() throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='document.getElementById(\"s\").innerHTML="
            + "    \"<h1><span><span></span></span><span><span></span></span></h1>\";'>\n"
            + "    <div>\n"
            + "      <div>\n"
            + "        <table>\n"
            + "          <tbody>\n"
            + "            <tr>\n"
            + "              <td>\n"
            + "                <table>\n"
            + "                  <tbody>\n"
            + "                    <tr>\n"
            + "                      <td>\n"
            + "                        <div>\n"
            + "                          <div>\n"
            + "                            <h1>\n"
            + "                              <span id='s'>blah</span>\n"
            + "                            </h1>\n"
            + "                          </div>\n"
            + "                        </div>\n"
            + "                      </td>\n"
            + "                    </tr>\n"
            + "                  </tbody>\n"
            + "                </table>\n"
            + "              </td>\n"
            + "            </tr>\n"
            + "          </tbody>\n"
            + "        </table>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";
        final HtmlPage page = loadPageWithAlerts(html);
        assertNotNull(page);
    }

}
