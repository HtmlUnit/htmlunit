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
package com.gargoylesoftware.htmlunit.javascript;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;


/**
 * Tests for {@link HTMLCollection}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLCollectionTest extends WebTestCase {

    /**
     * Create an instance
     * @param name The name of the test
     */
    public HTMLCollectionTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testImplicitToStringConversion() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(document.links != 'foo')\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<a href='bla.html'>link</a>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"true"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
    
        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * Test that toString is accessible
     * @throws Exception if the test fails
     */
    public void testToStringFunction() throws Exception {
        if (notYetImplemented()) {
            return;
        }

        final String content = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(typeof document.links.toString)\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<a href='bla.html'>link</a>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"function"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
    
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetElements() throws Exception {
        final String firstContent
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.all.length);\n"
            + "    document.appendChild(document.createElement('div'));\n"
            + "    alert(document.all.length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        loadPage(firstContent, collectedAlerts);

        final String[] expectedAlerts = {"5", "6"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testChildNodes() throws Exception {
        testChildNodes(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"true", "1"});
        testChildNodes(BrowserVersion.FIREFOX_2, new String[] {"true", "1"});
    }
    
    private void testChildNodes(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String firstContent = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + URL_SECOND + "'));\n"
            + "    alert(doc.documentElement.childNodes.length);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        
        final String secondContent = "<title>Immortality</title>";

        final List collectedAlerts = new ArrayList();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testFor_in() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    for (i in document.forms) {\n"
            + "      alert(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"length", "myForm"};
        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testTags() throws Exception {
        testTags(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"true"});
        testTags(BrowserVersion.FIREFOX_2, new String[] {"false"});
    }

    private void testTags(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(document.forms.tags != undefined);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        loadPage(browserVersion, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
