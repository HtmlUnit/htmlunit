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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Test class for {@link HTMLParser}.
 *
 * @version $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLParserTest extends WebTestCase {

    /**
     * Tests the new HTMLParser on a simple HTML string.
     * @throws Exception failure
     */
    @Test
    public void simpleHTMLString() throws Exception {
        final WebClient webClient = new WebClient();
        final WebResponse webResponse = new StringWebResponse(
            "<html><head><title>TITLE</title><noscript>TEST</noscript></head><body></body></html>", URL_GARGOYLE);

        final HtmlPage page = HTMLParser.parse(webResponse, webClient.getCurrentWindow());

        final String stringVal = ((HtmlNoScript) page.getFirstByXPath("//noscript")).getFirstChild().getNodeValue();
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
    public void badlyFormedHTML() throws Exception {
        final String content
            = "<html><head><title>first</title>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "  alert(document.getElementById('myInput').form.id);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<table>\n"
            + "<form name='myForm' action='foo' id='myForm'>\n"
            + "<tr><td>\n"
            + "<input type='text' name='myInput' id='myInput'/>\n"
            + "</td></tr>\n"
            + "</form>\n"
            + "</table>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"myForm"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test when an illegal tag is found in head as some websites do.
     * @throws Exception failure
     */
    @Test
    public void unknownTagInHead() throws Exception {
        // Note: the <meta> tag in this test is quite important because
        // I could adapt the TagBalancer to make it work except with this <meta http-equiv...
        // (it worked with <meta name=...)
        final String content
            = "<html><head><mainA3>\n"
            + "<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>\n"
            + "<title>first</title>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "  alert(document.title);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"first"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
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
                ((HtmlDivision) page.getFirstByXPath("//div[@id='footer']/div[@class='xright']")).asText().trim();
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
    public void duplicatedAttribute() throws Exception {
        final String content
            = "<html><head>\n"
            + "</head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.getElementById('foo') == null);\n"
            + "  alert(document.getElementById('bla') == null);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<span id='foo' id='bla'></span>"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"false", "true"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception failure
     */
    @Test
    public void namespace() throws Exception {
    	if (notYetImplemented()) {
    	    return;
    	}
        namespace(BrowserVersion.INTERNET_EXPLORER_6_0, new String[] {"1", "3", "[object]", "[object]", "[object]"});
        namespace(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"1", "3", "[object]", "[object]", "[object]"});
        namespace(BrowserVersion.FIREFOX_2, new String[] {"1", "3", "[object HTMLScriptElement]",
            "[object HTMLUnknownElement]", "[object HTMLUnknownElement]"});
    }

    private void namespace(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String content
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

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
