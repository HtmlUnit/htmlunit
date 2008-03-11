/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

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
     * test the new HTMLParser on a simple HTML string and use the Jaxen XPath navigator
     * to validate results
     * @throws Exception failure
     */
    @Test
    public void simpleHTMLString() throws Exception {
        final WebClient webClient = new WebClient();
        final WebResponse webResponse = new StringWebResponse(
            "<html><head><title>TITLE</title><noscript>TEST</noscript></head><body></body></html>");

        final HtmlPage page = HTMLParser.parse(webResponse, webClient.getCurrentWindow());

        final String stringVal = ((HtmlNoScript) page.getFirstByXPath("//noscript")).getFirstChild().getNodeValue();
        assertEquals("TEST", stringVal);

        final HtmlElement node = (HtmlElement) page.getFirstByXPath("//*[./text() = 'TEST']");
        assertEquals(node.getTagName(), HtmlNoScript.TAG_NAME);
    }

    /**
     * Test when <form> inside <table> and before <tr>
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
     * Test when an illegal tag is found in head as some websites do
     * @throws Exception failure
     */
    @Test
    public void unknownTagInHead() throws Exception {
        if (notYetImplemented()) {
            return;
        }

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
     * Works since NekoHtml 0.9.5
     * @exception Exception If the test fails
     */
    @Test
    public void badTagInHead() throws Exception {
        final String htmlContent = "<html>\n" + "<head><foo/>\n<title>foo\n</head>\n"
                + "<body>\nfoo\n</body>\n</html>";

        final HtmlPage page = loadPage(htmlContent);
        assertEquals("foo", page.getTitleText());
    }
}
