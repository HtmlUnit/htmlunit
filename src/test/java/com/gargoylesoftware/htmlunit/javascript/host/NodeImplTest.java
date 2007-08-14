/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link NodeImpl}.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Bruce Faulkner
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class NodeImplTest extends WebTestCase {

    /**
     * @param name The name of the test case
     */
    public NodeImplTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception on test failure
     */
    public void test_hasChildNodes_true() throws Exception {
        final String content = "<html><head><title>test_hasChildNodes</title>"
                + "<script>"
                + "function doTest(){"
                + "    alert(document.getElementById('myNode').hasChildNodes());"
                + "}"
                + "</script>"
                + "</head><body onload='doTest()'>"
                + "<p id='myNode'>hello world<span>Child Node</span></p>"
                + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test_hasChildNodes", page.getTitleText());

        final String[] expectedAlerts = {"true"};
        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * @throws Exception on test failure
     */
    public void test_hasChildNodes_false() throws Exception {
        final String content = "<html><head><title>test_hasChildNodes</title>"
                + "<script>"
                + "function doTest(){"
                + "    alert(document.getElementById('myNode').hasChildNodes());"
                + "}"
                + "</script>"
                + "</head><body onload='doTest()'>"
                + "<p id='myNode'></p>"
                + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test_hasChildNodes", page.getTitleText());

        final String[] expectedAlerts = {"false"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for removeChild
     * @throws Exception if the test fails
     */
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
            + "<form name='form1'><div id='formChild'/></form>"
            + "</body></html>";
        webConnection.setResponse(
            URL_FIRST, content, 200, "OK", "text/html", Collections.EMPTY_LIST);

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"true", "true"};

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for replaceChild
     * @throws Exception if the test fails
     */
    public void testReplaceChild() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webClient.setWebConnection(webConnection);

        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var form = document.forms['form1'];\n"
            + "    var div1 = form.firstChild;\n"
            + "    var div2 = document.getElementById('newChild');\n"
            + "    var removedDiv = form.replaceChild(div2,div1);\n"
            + "    alert(div1==removedDiv);\n"
            + "    alert(form.firstChild==div2);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'><div id='formChild'/></form>"
            + "</body><div id='newChild'/></html>";
        webConnection.setResponse(
            URL_FIRST, content, 200, "OK", "text/html", Collections.EMPTY_LIST);

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"true", "true"};

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * The common browsers always return node names in uppercase.  Test this.
     * @throws Exception on test failure
     */
    public void testNodeNameIsUppercase() throws Exception {
        final String content = "<html><head><title>test_hasChildNodes</title>"
                + "<script>"
                + "function doTest(){"
                + "    alert(document.getElementById('myNode').nodeName);"
                + "}"
                + "</script>"
                + "</head><body onload='doTest()'>"
                + "<div id='myNode'>hello world<span>Child Node</span></div>"
                + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test_hasChildNodes", page.getTitleText());

        final String[] expectedAlerts = {"DIV"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    public void test_getChildNodes() throws Exception {
        final String content = "<html><head><title>test_getChildNodes</title>"
            + "<script>"
            + "function doTest() {"
            + "var aNode = document.getElementById('myNode');"
            + "alert(aNode.childNodes.length);"
            + "alert(aNode.childNodes[0].nodeName);"
            + "alert(aNode.childNodes[0].childNodes.length);"
            + "alert(aNode.childNodes[0].childNodes[0].nodeName);"
            + "alert(aNode.childNodes[0].childNodes[1].nodeName);"
            + "alert(aNode.childNodes[1].nodeName);"
            + "}"
            + "</script>"
            + "</head><body onload='doTest()'>"
            + "<div id='myNode'><span>Child Node 1-A"
            + "<h1>Child Node 1-B</h1></span>"
            + "<h2>Child Node 2-A</h2></div>"
            + "</body></html>";

        final String[] expectedAlerts = {"2", "SPAN", "2", "#text", "H1", "H2"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test_getChildNodes", page.getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    public void testChildNodes_Comments() throws Exception {
        final String content = "<html><head><title>test</title>"
            + "<html><head></head>"
            + "<body><!-- comment --><script>"
            + "var nodes = document.body.childNodes;"
            + "alert('nb nodes: ' + nodes.length);"
            + "for (var i=0; i<nodes.length; i++)"
            + "{"
            + " alert(nodes[i].nodeType);"
            + "}"
            + "</script></body></html>";

        final String[] expectedAlerts = {"nb nodes: 2", "8", "1"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    public void test_getChildNodesProperties() throws Exception {
        final String content = "<html><head><title>test_getChildNodes</title>"
            + "<script>"
            + "function doTest() {"
            + "    var testForm = document.getElementById('testForm');"
            + "    var childNodes = testForm.childNodes;"
            + "    var length = childNodes.length;"
            + "    alert('length: ' + length);"
            + "    for (var i=0; i < length; i++) {"
            + "        var tempNode = childNodes.item(i);"
            + "        alert('tempNode.name: ' + tempNode.name);"
            + "    }"
            + "}"
            + "</script>"
            + "</head><body onload='doTest()'>"
            + "<form name='testForm' id='testForm'>foo\n" // some text, because IE doesn't see "\n" as a text node here
            + "<input type='hidden' name='input1' value='1'>\n"
            + "<input type='hidden' name='input2' value='2'>\n"
            + "</form>"
            + "</body></html>";

        final String[] expectedAlerts = {"length: 5",
            "tempNode.name: undefined", "tempNode.name: input1", "tempNode.name: undefined",
            "tempNode.name: input2", "tempNode.name: undefined"};

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);

        assertEquals("test_getChildNodes", page.getTitleText());
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test to verify that insertBefore correctly appends
     * the new child object when the reference child object is null.
     * @throws Exception if the test fails
     */
    public void test_insertBefore_nullRef() throws Exception {
        test_insertBefore(BrowserVersion.FIREFOX_2, "aNode.insertBefore(nodeToInsert, null);");
        test_insertBefore(BrowserVersion.INTERNET_EXPLORER_6_0, "aNode.insertBefore(nodeToInsert, null);");
    }

    /**
     * Regression test to verify that insertBefore correctly appends
     * the new child object when the reference child object is null.
     * @throws Exception if the test fails
     */
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
        final String content = "<html><head><title>test_insertBefore</title>"
            + "<script>"
            + "function doTest() {"
            + "var nodeToInsert = document.getElementById('nodeToInsert');"
            + "var aNode = document.getElementById('myNode');"
            + insertJSLine
            + "alert(aNode.childNodes.length);"
            + "alert(aNode.childNodes[2].nodeName);"
            + "}"
            + "</script>"
            + "</head><body onload='doTest()'>"
            + "<h2 id='nodeToInsert'>Bottom</h2>"
            + "<div id='myNode'><span>Child Node 1-A</span>"
            + "<h1>Child Node 2-A</h1></div>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();

        final String[] expectedAlerts = {"3", "H2"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * The common browsers always return node names in uppercase.  Test this.
     * @throws Exception on test failure
     */
    public void testNodeType() throws Exception {
        final String content = "<html><head><title>test</title>"
                + "<script>"
                + "function doTest(){\n"
                + "    alert('document: ' + document.nodeType);\n"
                + "    alert('document.body: ' + document.body.nodeType);\n"
                + "    alert('body child 1: ' + document.body.childNodes[0].nodeType);\n"
                + "    alert('body child 2: ' + document.body.childNodes[1].nodeType);\n"
                + "}\n"
                + "</script>"
                + "</head><body onload='doTest()'>"
                + "some text<!-- some comment -->"
                + "</body></html>";

        final String[] expectedAlerts = {"document: 9", "document.body: 1",
            "body child 1: 3", "body child 2: 8"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for 1716129
     * @throws Exception on test failure
     */
    public void testAttachEvent() throws Exception {
        final String content = "<html><head>"
            + "<title>First</title>"
            + "<script>"
            + "function test()"
            + "{"
            + "    var oField = document.getElementById('div1');"
            + "    oField.attachEvent('onclick', foo1);"
            + "    oField.attachEvent('onclick', foo2);"
            + "}"
            + "function foo1() { alert('in foo1');}"
            + "function foo2() { alert('in foo2');}"
            + "</script></head><body onload='test()'>"
            + "<div id='div1'>bla</div>"
            + "</body></html>";

        final String[] expectedAlerts = {"in foo1", "in foo2"};

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);
        ((ClickableElement) page.getHtmlElementById("div1")).click();

        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * @throws Exception If the test fails.
     */
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
        final String content = "<html><head><title>foo</title><script>"
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
        final List collectedAlerts = new ArrayList();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * element.appendChild: If the parent has a null parentNode,
     * IE creates a DocumentFragment be the parent's parentNode.
     *
     * @throws Exception If the test fails.
     */
    public void testAppendChild_parentNode() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        testAppendChild_parentNode(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"null", "#document-fragment"});
        testAppendChild_parentNode(BrowserVersion.FIREFOX_2, new String[] {"null", "null"});
    }

    private void testAppendChild_parentNode(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String content = "<html><head><title>foo</title><script>"
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
        final List collectedAlerts = new ArrayList();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * element.appendChild: If the parent has a null parentNode,
     * IE creates a DocumentFragment be the parent's parentNode.
     *
     * @throws Exception If the test fails.
     */
    public void testInsertBefore_parentNode() throws Exception {
        testInsertBefore_parentNode(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"null", "#document-fragment"});
        testInsertBefore_parentNode(BrowserVersion.FIREFOX_2, new String[] {"null", "null"});
    }

    private void testInsertBefore_parentNode(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String content = "<html><head><title>foo</title><script>"
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
        final List collectedAlerts = new ArrayList();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAppencChild_of_DocumentFragment() throws Exception {
        final String content = "<html><head><title>foo</title><script>"
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
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
