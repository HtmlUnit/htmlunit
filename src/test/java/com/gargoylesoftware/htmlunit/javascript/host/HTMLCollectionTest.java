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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

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
     * @throws Exception if the test fails
     */
    @Test
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

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that <tt>toString</tt> is accessible.
     * @throws Exception if the test fails
     */
    @Test
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

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
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

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(firstContent, collectedAlerts);

        final String[] expectedAlerts = {"5", "6"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
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

        final List<String> collectedAlerts = new ArrayList<String>();
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
    @Test
    public void testFor_in() throws Exception {
        final String[] expectedAlertsIE = {"string length", "string myForm"};
        testFor_in(BrowserVersion.INTERNET_EXPLORER_7_0, expectedAlertsIE);
        final String[] expectedAlertsFF = {"string 0", "string length", "string item", "string namedItem"};
        testFor_in(BrowserVersion.FIREFOX_2, expectedAlertsFF);
    }

    private void testFor_in(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    for (i in document.forms) {\n"
            + "      alert((typeof i) + ' ' + i);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testFor_in2() throws Exception {
        final String[] expectedAlertsIE = {"string length", "string val1", "string 1", "string val2",
            "string first_submit", "string second_submit", "string action"};
        testFor_in2(BrowserVersion.INTERNET_EXPLORER_7_0, expectedAlertsIE);
        final String[] expectedAlertsFF = {"string 0", "string 1", "string 2", "string 3", "string 4", "string 5",
            "string length", "string item", "string namedItem"};
        testFor_in2(BrowserVersion.FIREFOX_2, expectedAlertsFF);
    }

    private void testFor_in2(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var form = document.getElementById('myForm');\n"
            + "    var x = form.getElementsByTagName('*');\n"
            + "    for (i in x){\n"
            + "      alert((typeof i) + ' ' + i);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form id='myForm'>\n"
            + "  <input type='text' id='id1' name='val1' id='input_enabled' value='4'>\n"
            + "  <div>This is not a form element</div>\n"
            + "  <input type='text' name='val2' id='input_disabled' disabled='disabled' value='5'>\n"
            + "  <input type='submit' name='first_submit' value='Commit it!'>\n"
            + "  <input type='submit' id='second_submit' value='Delete it!'>\n"
            + "  <input type='text' name='action' value='blah'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * <code>document.all.tags</code> is different from <code>document.forms.tags</code>!
     * @throws Exception if the test fails
     */
    @Test
    public void testTags() throws Exception {
        testTags(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"true", "true"});
        testTags(BrowserVersion.FIREFOX_2, new String[] {"true", "false"});
    }

    private void testTags(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(document.all.tags != undefined);\n"
            + "    alert(document.forms.tags != undefined);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Depending on the method used, out of bound access give different responses.
     * @throws Exception if the test fails
     */
    @Test
    public void testOutOfBoundAccess() throws Exception {
        final String[] expectedAlerts = {"null", "null", "undefined", "null"};
        // in fact this is not fully correct as FF support the col(index) syntax only for special
        // collections where it simulates IE like document.all
        testOutOfBoundAccess(BrowserVersion.FIREFOX_2, expectedAlerts);
        testOutOfBoundAccess(BrowserVersion.INTERNET_EXPLORER_6_0, expectedAlerts);
    }

    private void testOutOfBoundAccess(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {

        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var col = document.getElementsByTagName('a');\n"
            + "    alert(col.item(1));\n"
            + "    alert(col.namedItem('foo'));\n"
            + "    alert(col[1]);\n"
            + "    alert(col(1));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, html, collectedAlerts);
        assertEquals("Using " + browserVersion.getApplicationName(), expectedAlerts, collectedAlerts);
    }
}
