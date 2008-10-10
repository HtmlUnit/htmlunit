/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
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
public class HTMLParserTest extends WebTestCase {

    /**
     * Tests the new HTMLParser on a simple HTML string.
     * @throws Exception failure
     */
    @Test
    public void simpleHTMLString() throws Exception {
        final WebClient webClient = getWebClient();
        final WebResponse webResponse = new StringWebResponse(
            "<html><head><title>TITLE</title><noscript>TEST</noscript></head><body></body></html>", URL_GARGOYLE);

        final HtmlPage page = HTMLParser.parse(webResponse, webClient.getCurrentWindow());

        final String stringVal = page.<HtmlNoScript>getFirstByXPath("//noscript").getFirstChild().getNodeValue();
        assertEquals("TEST", stringVal);

        final HtmlElement node = (HtmlElement) page.getFirstByXPath("//*[./text() = 'TEST']");
        assertEquals(node.getTagName(), HtmlNoScript.TAG_NAME);
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
    @Alerts({"first" })
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
     * Test the HTMLParser by accessing the HtmlUnit home page and detecting the copyright
     * string.
     *
     * @throws Exception failure
     */
    @Test
    public void htmlUnitHomePage() throws Exception {
        final HtmlPage page = loadUrl("http://htmlunit.sourceforge.net");
        if (page != null) {
            // No connectivity issues.
            final String stringVal =
                page.<HtmlDivision>getFirstByXPath("//div[@id='footer']/div[@class='xright']").asText().trim();
            assertTrue(stringVal.matches("\u00A9 2002-\\d\\d\\d\\d Gargoyle Software Inc."));
        }
    }

    /**
     * Works since NekoHtml 0.9.5.
     * @exception Exception If the test fails
     */
    @Test
    public void badTagInHead() throws Exception {
        final String htmlContent = "<html>\n" + "<head><foo/>\n<title>foo\n</head>\n"
                + "<body>\nfoo\n</body>\n</html>";

        final HtmlPage page = loadPage(htmlContent);
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
    @Browsers({Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7, Browser.FIREFOX_2, Browser.FIREFOX_3 })
    @Alerts(IE = {"1", "3", "[object]", "[object]", "[object]" },
            FF = {"1", "3", "[object HTMLScriptElement]",
                "[object HTMLUnknownElement]", "[object HTMLUnknownElement]" })
    public void namespace() throws Exception {
        final String html
            = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:app='http://www.appcelerator.org'>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('script1'));\n"
            + "    alert(document.getElementById('script2'));\n"
            + "    alert(document.getElementById('message1'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<script id='script1'>alert(1)</script>\n"
            + "<app:script id='script2'>alert(2)</app:script>\n"
            + "<script>alert(3)</script>\n"
            + "<app:message name='r:tasks.request' id='message1'>hello</app:message>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Browsers({Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7, Browser.FIREFOX_2, Browser.FIREFOX_3 })
    @Alerts(IE = {"1", "createElementNS() is not defined",
                "[object]", "SCRIPT,SCRIPT,undefined,undefined,undefined",
                "[object]", "script,script,undefined,undefined,undefined" },
            FF = {"1", "[object Element]", "app:script,app:script,http://www.appcelerator.org,app,script",
                "[object HTMLScriptElement]", "SCRIPT,SCRIPT,null,null,SCRIPT",
                "[object HTMLUnknownElement]", "APP:SCRIPT,APP:SCRIPT,null,null,APP:SCRIPT" })
    public void namespace2() throws Exception {
        final String html
            = "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:app='http://www.appcelerator.org'>\n"
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
    @Browsers({Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7, Browser.FIREFOX_2, Browser.FIREFOX_3 })
    @NotYetImplemented
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
    @Browsers({Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7, Browser.FIREFOX_2, Browser.FIREFOX_3 })
    @NotYetImplemented
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
    @Browsers({Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7, Browser.FIREFOX_2, Browser.FIREFOX_3 })
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
    @Browsers({Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7, Browser.FIREFOX_2, Browser.FIREFOX_3 })
    @Alerts(IE = {"HEAD", "Outer Html", "true" },
            FF = {"HTML", "Inner Html", "false", "HTML" })
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
            + "         <script>\n"
            + "         document.getElementsByTagName('html')[0].innerHTML ="
            + "             '<html><head><title>Inner Html</title></head>"
            + "             <body><DIV id=innerDiv>Inner DIV</DIV></body></html>';\n"
            + "         </script>\n"
            + "</body>\n"
            + "</html>\n";

        createTestPageForRealBrowserIfNeeded(html, getExpectedAlerts());

        final WebClient client = getWebClient();
        client.setThrowExceptionOnScriptError(false);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_GARGOYLE, html);
        client.setWebConnection(webConnection);

        client.getPage(URL_GARGOYLE);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }
}
