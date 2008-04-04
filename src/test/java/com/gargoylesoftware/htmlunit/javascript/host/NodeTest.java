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
package com.gargoylesoftware.htmlunit.javascript.host;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link Node}.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Bruce Faulkner
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class NodeTest extends WebTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    public void test_hasChildNodes_true() throws Exception {
        final String content = "<html><head><title>test_hasChildNodes</title>\n"
                + "<script>\n"
                + "function doTest(){"
                + "    alert(document.getElementById('myNode').hasChildNodes());\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "<p id='myNode'>hello world<span>Child Node</span></p>\n"
                + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test_hasChildNodes", page.getTitleText());

        final String[] expectedAlerts = {"true"};
        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * @throws Exception on test failure
     */
    @Test
    public void test_hasChildNodes_false() throws Exception {
        final String content = "<html><head><title>test_hasChildNodes</title>\n"
                + "<script>\n"
                + "function doTest(){"
                + "    alert(document.getElementById('myNode').hasChildNodes());\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "<p id='myNode'></p>\n"
                + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test_hasChildNodes", page.getTitleText());

        final String[] expectedAlerts = {"false"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for removeChild.
     * @throws Exception if the test fails
     */
    @Test
    public void testRemoveChild() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webClient.setWebConnection(webConnection);

        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var form = document.forms['form1'];\n"
            + "    var div = form.firstChild;\n"
            + "    var removedDiv = form.removeChild(div);\n"
            + "    alert(div==removedDiv);\n"
            + "    alert(form.firstChild==null);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'><div id='formChild'/></form>\n"
            + "</body></html>";

        final List< ? extends NameValuePair> emptyList = Collections.emptyList();
        webConnection.setResponse(URL_FIRST, content, 200, "OK", "text/html", emptyList);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"true", "true"};

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for replaceChild.
     * @throws Exception if the test fails
     */
    @Test
    public void testReplaceChild_Normal() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var form = document.forms['form1'];\n"
            + "    var div1 = form.firstChild;\n"
            + "    var div2 = document.getElementById('newChild');\n"
            + "    var removedDiv = form.replaceChild(div2,div1);\n"
            + "    alert(div1==removedDiv);\n"
            + "    alert(form.firstChild==div2);\n"
            + "    var newDiv = document.createElement('div');\n"
            + "    form.replaceChild(newDiv, div2);\n"
            + "    alert(form.firstChild==newDiv);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'><div id='formChild'/></form>\n"
            + "</body><div id='newChild'/></html>";

        final String[] expectedAlerts = {"true", "true", "true"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testReplaceChild_WithSameNode() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var a = document.getElementById('a');\n"
            + "    var b = document.getElementById('b');\n"
            + "    a.replaceChild(b, b);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'><div id='a'><div id='b'/></div></html>";
        final HtmlPage page = loadPage(html);
        assertNotNull(page.getHtmlElementById("b").getParentNode());
    }

    /**
     * The common browsers always return node names in uppercase. Test this.
     * @throws Exception on test failure
     */
    @Test
    public void testNodeNameIsUppercase() throws Exception {
        final String content = "<html><head><title>test_hasChildNodes</title>\n"
                + "<script>\n"
                + "function doTest(){"
                + "    alert(document.getElementById('myNode').nodeName);\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "<div id='myNode'>hello world<span>Child Node</span></div>\n"
                + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test_hasChildNodes", page.getTitleText());

        final String[] expectedAlerts = {"DIV"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void test_getChildNodes() throws Exception {
        final String content = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "var aNode = document.getElementById('myNode');\n"
            + "alert(aNode.childNodes.length);\n"
            + "alert(aNode.childNodes[0].nodeName);\n"
            + "alert(aNode.childNodes[0].childNodes.length);\n"
            + "alert(aNode.childNodes[0].childNodes[0].nodeName);\n"
            + "alert(aNode.childNodes[0].childNodes[1].nodeName);\n"
            + "alert(aNode.childNodes[1].nodeName);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "<div id='myNode'><span>Child Node 1-A"
            + "<h1>Child Node 1-B</h1></span>"
            + "<h2>Child Node 2-A</h2></div>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"2", "SPAN", "2", "#text", "H1", "H2"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test_getChildNodes", page.getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void testChildNodes_Comments() throws Exception {
        final String content = "<html><head><title>test</title>\n"
            + "<html><head></head>\n"
            + "<body><!-- comment --><script>\n"
            + "var nodes = document.body.childNodes;\n"
            + "alert('nb nodes: ' + nodes.length);\n"
            + "for (var i=0; i<nodes.length; i++)\n"
            + "{\n"
            + " alert(nodes[i].nodeType);\n"
            + "}\n"
            + "</script></body></html>";

        final String[] expectedAlerts = {"nb nodes: 2", "8", "1"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void test_getChildNodesProperties() throws Exception {
        final String content = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var testForm = document.getElementById('testForm');\n"
            + "    var childNodes = testForm.childNodes;\n"
            + "    var length = childNodes.length;\n"
            + "    alert('length: ' + length);\n"
            + "    for (var i=0; i < length; i++) {\n"
            + "        var tempNode = childNodes.item(i);\n"
            + "        alert('tempNode.name: ' + tempNode.name);\n"
            + "    }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "<form name='testForm' id='testForm'>foo\n" // some text, because IE doesn't see "\n" as a text node here
            + "<input type='hidden' name='input1' value='1'>\n"
            + "<input type='hidden' name='input2' value='2'>\n"
            + "</form>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"length: 5",
            "tempNode.name: undefined", "tempNode.name: input1", "tempNode.name: undefined",
            "tempNode.name: input2", "tempNode.name: undefined"};

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);

        assertEquals("test_getChildNodes", page.getTitleText());
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test to verify that insertBefore correctly appends
     * the new child object when the reference child object is null.
     * @throws Exception if the test fails
     */
    @Test
    public void test_insertBefore_nullRef() throws Exception {
        test_insertBefore(BrowserVersion.FIREFOX_2, "aNode.insertBefore(nodeToInsert, null);");
        test_insertBefore(BrowserVersion.INTERNET_EXPLORER_6_0, "aNode.insertBefore(nodeToInsert, null);");
    }

    /**
     * Regression test to verify that insertBefore correctly appends
     * the new child object when the reference child object is null.
     * @throws Exception if the test fails
     */
    @Test
    public void test_insertBefore_noSecondArg() throws Exception {
        test_insertBefore(BrowserVersion.INTERNET_EXPLORER_6_0, "aNode.insertBefore(nodeToInsert);");
        try {
            test_insertBefore(BrowserVersion.FIREFOX_2, "aNode.insertBefore(nodeToInsert);");
            fail();
        }
        catch (final ScriptException e) {
            final String message = e.getMessage();
            assertTrue(message, message.indexOf("not enough arguments") > -1);
        }
    }
    
    /**
     * @throws Exception if the test fails
     */
    void test_insertBefore(final BrowserVersion browserVersion, final String insertJSLine) throws Exception {
        final String content = "<html><head><title>test_insertBefore</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "var nodeToInsert = document.getElementById('nodeToInsert');\n"
            + "var aNode = document.getElementById('myNode');\n"
            + insertJSLine
            + "alert(aNode.childNodes.length);\n"
            + "alert(aNode.childNodes[2].nodeName);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "<h2 id='nodeToInsert'>Bottom</h2>\n"
            + "<div id='myNode'><span>Child Node 1-A</span>"
            + "<h1>Child Node 2-A</h1></div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();

        final String[] expectedAlerts = {"3", "H2"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * The common browsers always return node names in uppercase. Test this.
     * @throws Exception on test failure
     */
    @Test
    public void testNodeType() throws Exception {
        final String content = "<html><head><title>test</title>\n"
                + "<script>\n"
                + "function doTest(){\n"
                + "    alert('document: ' + document.nodeType);\n"
                + "    alert('document.body: ' + document.body.nodeType);\n"
                + "    alert('body child 1: ' + document.body.childNodes[0].nodeType);\n"
                + "    alert('body child 2: ' + document.body.childNodes[1].nodeType);\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "some text<!-- some comment -->\n"
                + "</body></html>";

        final String[] expectedAlerts = {"document: 9", "document.body: 1",
            "body child 1: 3", "body child 2: 8"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for bug 1716129.
     * @throws Exception on test failure
     */
    @Test
    public void testAttachEvent() throws Exception {
        final String content = "<html><head>\n"
            + "<title>First</title>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "    var oField = document.getElementById('div1');\n"
            + "    oField.attachEvent('onclick', foo1);\n"
            + "    oField.attachEvent('onclick', foo2);\n"
            + "}\n"
            + "function foo1() {alert('in foo1');}\n"
            + "function foo2() {alert('in foo2');}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'>bla</div>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"in foo1", "in foo2"};

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);
        ((ClickableElement) page.getHtmlElementById("div1")).click();

        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void testIsSameNode() throws Exception {
        testIsSameNode(BrowserVersion.FIREFOX_2);
        try {
            testIsSameNode(BrowserVersion.INTERNET_EXPLORER_7_0);
            fail("'isSameNode' is not supported in IE7.");
        }
        catch (final Exception e) {
            //expected
        }
    }

    private void testIsSameNode(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var d2 = document.getElementById('div2');\n"
            + "    alert(d1.isSameNode(d1));\n"
            + "    alert(d1.isSameNode(d2));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'/>\n"
            + "<div id='div2'/>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"true", "false"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test element.appendChild: If the parent has a null parentNode,
     * IE creates a DocumentFragment be the parent's parentNode.
     *
     * @throws Exception If the test fails.
     */
    @Test
    public void testAppendChild_parentNode() throws Exception {
        final String[] expectedAlertsIE = {"null", "#document-fragment"};
        testAppendChild_parentNode(BrowserVersion.INTERNET_EXPLORER_7_0, expectedAlertsIE);
        final String[] expectedAlertsFF = {"null", "null"};
        testAppendChild_parentNode(BrowserVersion.FIREFOX_2, expectedAlertsFF);
    }

    private void testAppendChild_parentNode(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var div1 = document.createElement('div');\n"
            + "    var div2 = document.createElement('div');\n"
            + "    alert(div1.parentNode);\n"
            + "    div1.appendChild(div2);\n"
            + "    if(div1.parentNode)\n"
            + "      alert(div1.parentNode.nodeName);\n"
            + "    else\n"
            + "      alert(div1.parentNode);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test element.appendChild: If the parent has a null parentNode,
     * IE creates a DocumentFragment be the parent's parentNode.
     *
     * @throws Exception If the test fails.
     */
    @Test
    public void testInsertBefore_parentNode() throws Exception {
        final String[] expectedAlertsIE = {"null", "#document-fragment"};
        testInsertBefore_parentNode(BrowserVersion.INTERNET_EXPLORER_7_0, expectedAlertsIE);
        final String[] expectedAlertsFF = {"null", "null"};
        testInsertBefore_parentNode(BrowserVersion.FIREFOX_2, expectedAlertsFF);
    }

    private void testInsertBefore_parentNode(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var div1 = document.createElement('div');\n"
            + "    var div2 = document.createElement('div');\n"
            + "    alert(div1.parentNode);\n"
            + "    div1.insertBefore(div2,null);\n"
            + "    if(div1.parentNode)\n"
            + "      alert(div1.parentNode.nodeName);\n"
            + "    else\n"
            + "      alert(div1.parentNode);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testAppendChild_of_DocumentFragment() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div1 = document.createElement('div');\n"
            + "    div1.id = 'div1';\n"
            + "    var div2 = document.createElement('div');\n"
            + "    div2.id = 'div2';\n"
            + "    fragment.appendChild(div1);\n"
            + "    fragment.appendChild(div2);\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    div.appendChild(fragment);\n"
            + "    alert(div.childNodes.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='myDiv'></div>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testNodePrototype() throws Exception {
        testNodePrototype(BrowserVersion.FIREFOX_2, new String[] {"3"});
        try {
            testNodePrototype(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"3"});
            fail("'Node' is not supported in IE.");
        }
        catch (final Exception e) {
            //expected
        }
    }
    private void testNodePrototype(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(Node.TEXT_NODE);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReplaceChild() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var element = document.getElementById('myDiv2');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('<div>one</div><div>two</div>');\n"
            + "    var parent = element.parentNode;\n"
            + "    alert(parent.innerHTML);\n"
            + "    alert(parent.replaceChild(fragment, parent.firstChild).id);\n"
            + "    alert(parent.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'><div id='myDiv2'></div><div id='myDiv3'></div></div>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"<div id=\"myDiv2\"></div><div id=\"myDiv3\"></div>", "myDiv2",
            "<div>one</div><div>two</div><div id=\"myDiv3\"></div>"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReplaceChild_EmptyDocumentFragment() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var element = document.getElementById('myDiv2');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('');\n"
            + "    var parent = element.parentNode;\n"
            + "    alert(parent.innerHTML);\n"
            + "    alert(parent.replaceChild(fragment, parent.firstChild).id);\n"
            + "    alert(parent.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'><div id='myDiv2'></div><div id='myDiv3'></div></div>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"<div id=\"myDiv2\"></div><div id=\"myDiv3\"></div>", "myDiv2",
            "<div id=\"myDiv3\"></div>"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
