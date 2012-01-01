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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link HTMLCollection}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLCollectionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void testImplicitToStringConversion() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(document.links != 'foo')\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<a href='bla.html'>link</a>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test that <tt>toString</tt> is accessible.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = "object", FF = "function")
    public void testToStringFunction() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(typeof document.links.toString);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<a href='bla.html'>link</a>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "5", "6" })
    @Browsers(Browser.IE)
    public void getElements() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.all.length);\n"
            + "    document.appendChild(document.createElement('div'));\n"
            + "    alert(document.all.length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "1" })
    public void testChildNodes() throws Exception {
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
        final WebClient client = getWebClient();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = {"string 0", "string length", "string item", "string namedItem" },
            IE = {"string length", "string myForm" })
    public void testFor_in() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    for (i in document.forms) {\n"
            + "      alert((typeof i) + ' ' + i);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = {"string 0", "string 1", "string 2", "string 3", "string 4", "string 5",
            "string length", "string item", "string namedItem" },
            IE = {"string length", "string val1", "string 1", "string val2",
            "string first_submit", "string second_submit", "string action" })
    public void testFor_in2() throws Exception {
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

        loadPageWithAlerts(html);
    }

    /**
     * <code>document.all.tags</code> is different from <code>document.forms.tags</code>!
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = {"false", "false" },
            IE = {"true", "true" })
    public void testTags() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(document.all.tags != undefined);\n"
            + "    alert(document.forms.tags != undefined);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Depending on the method used, out of bound access give different responses.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.FF)
    @Alerts(IE = { "null", "null", "undefined", "null" },
            FF = { "null", "null", "undefined", "exception" })
    public void testOutOfBoundAccess() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var col = document.getElementsByTagName('a');\n"
            + "    alert(col.item(1));\n"
            + "    alert(col.namedItem('foo'));\n"
            + "    alert(col[1]);\n"
            + "    try {\n"
            + "      alert(col(1));\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "undefined", "undefined", "undefined" })
    public void testInexistentProperties() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var x = document.documentElement.childNodes;\n"
            + "    alert(x.split);\n"
            + "    alert(x.setInterval);\n"
            + "    alert(x.bogusNonExistentProperty);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "1", "DIV", "2" }, FF = { "3", "#text", "5" })
    public void childNodes() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.body.childNodes.length);\n"
            + "    alert(document.body.firstChild.nodeName);\n"
            + "    alert(document.getElementById('myDiv').childNodes.length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'> <div id='myDiv'> <div> </div> <div> </div> </div> </body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("object")
    public void typeof() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(typeof document.getElementsByTagName('a'));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that dollar signs don't cause exceptions in {@link HTMLCollection} (which uses Java
     * regex internally). Found via the MooTools unit tests.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "[object]", "undefined" }, FF = { "[object HTMLHeadingElement]", "undefined" })
    public void getElementWithDollarSign() throws Exception {
        final String html
            = "<h3 id='$h'>h</h3><script>\n"
            + "var hs = document.getElementsByTagName('h3');\n"
            + "alert(hs['$h']);\n"
            + "alert(hs['$n']);\n"
            + "</script>";
        loadPageWithAlerts(html);
    }

}
