/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
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
@RunWith(BrowserRunner.class)
public class NodeTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("true")
    public void test_hasChildNodes_true() throws Exception {
        final String html = "<html><head><title>test_hasChildNodes</title>\n"
                + "<script>\n"
                + "function doTest(){\n"
                + "    alert(document.getElementById('myNode').hasChildNodes());\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "<p id='myNode'>hello world<span>Child Node</span></p>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("false")
    public void test_hasChildNodes_false() throws Exception {
        final String html = "<html><head><title>test_hasChildNodes</title>\n"
                + "<script>\n"
                + "function doTest(){\n"
                + "    alert(document.getElementById('myNode').hasChildNodes());\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "<p id='myNode'></p>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for removeChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "true" })
    public void testRemoveChild() throws Exception {
        final String html
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

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for replaceChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "true", "true" })
    public void testReplaceChild_Normal() throws Exception {
        final String html
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

        loadPageWithAlerts2(html);
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
        final HtmlPage page = loadPageWithAlerts(html);
        assertNotNull(page.<HtmlElement>getHtmlElementById("b").getParentNode());
    }

    /**
     * The common browsers always return node names in uppercase. Test this.
     * @throws Exception on test failure
     */
    @Test
    @Alerts("DIV")
    public void testNodeNameIsUppercase() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "function doTest(){\n"
                + "    alert(document.getElementById('myNode').nodeName);\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "<div id='myNode'>hello world<span>Child Node</span></div>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "2", "SPAN", "2", "#text", "H1", "H2" })
    public void test_getChildNodes() throws Exception {
        final String html = "<html><head>\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "nb nodes: 2", "8", "1" })
    public void testChildNodes_Comments() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<html><head></head>\n"
            + "<body><!-- comment --><script>\n"
            + "var nodes = document.body.childNodes;\n"
            + "alert('nb nodes: ' + nodes.length);\n"
            + "for (var i=0; i<nodes.length; i++)\n"
            + "  alert(nodes[i].nodeType);\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "length: 5",
        "tempNode.name: undefined", "tempNode.name: input1", "tempNode.name: undefined",
        "tempNode.name: input2", "tempNode.name: undefined" })
    public void test_getChildNodesProperties() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test to verify that insertBefore correctly appends
     * the new child object when the reference child object is null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "H2" })
    public void test_insertBefore_nullRef() throws Exception {
        test_insertBefore("aNode.insertBefore(nodeToInsert, null);");
    }

    /**
     * Regression test to verify that insertBefore correctly appends
     * the new child object when the reference child object is null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = { "3", "H2" })
    public void test_insertBefore_noSecondArg() throws Exception {
        test_insertBefore("aNode.insertBefore(nodeToInsert);");
    }

    /**
     * @throws Exception if the test fails
     */
    void test_insertBefore(final String insertJSLine) throws Exception {
        final String html = "<html><head><title>test_insertBefore</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var nodeToInsert = document.getElementById('nodeToInsert');\n"
            + "  var aNode = document.getElementById('myNode');\n"
            + "  try {\n"
            + insertJSLine
            + "    alert(aNode.childNodes.length);\n"
            + "    alert(aNode.childNodes[2].nodeName);\n"
            + "  }\n"
            + "  catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "<h2 id='nodeToInsert'>Bottom</h2>\n"
            + "<div id='myNode'><span>Child Node 1-A</span>"
            + "<h1>Child Node 2-A</h1></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * The common browsers always return node names in uppercase. Test this.
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "document: 9", "document.body: 1", "body child 1: 3", "body child 2: 8" })
    public void testNodeType() throws Exception {
        final String html = "<html><head><title>test</title>\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * Test for bug 1716129.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(FF = "exception", IE = { "in foo1", "in foo2" })
    public void testAttachEvent() throws Exception {
        final String html = "<html><head>\n"
            + "<title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var oField = document.getElementById('div1');\n"
            + "  try {\n"
            + "    oField.attachEvent('onclick', foo1);\n"
            + "    oField.attachEvent('onclick', foo2);\n"
            + "  } catch(e) { alert('exception') };\n"
            + "}\n"
            + "function foo1() {alert('in foo1');}\n"
            + "function foo2() {alert('in foo2');}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'>bla</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("div1")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "true", "false" }, IE = { "isSameNode not supported" })
    public void testIsSameNode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var d2 = document.getElementById('div2');\n"
            + "    try {\n"
            + "      alert(d1.isSameNode(d1));\n"
            + "      alert(d1.isSameNode(d2));\n"
            + "    } catch(e) {\n"
            + "      alert('isSameNode not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'/>\n"
            + "<div id='div2'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test element.appendChild: If the parent has a null parentNode,
     * IE creates a DocumentFragment be the parent's parentNode.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "null", "null" }, IE = { "null", "#document-fragment" })
    public void testAppendChild_parentNode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * The HTML node can't be inserted anywhere else!
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "1", "exception", "1", "exception", "1", "exception", "1" },
            IE = { "1", "1", "1", "exception", "1" })
    public void append_insert_html_node() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var htmlNode = document.documentElement;\n"
            + "  var body = document.body;\n"
            + "  alert(body.childNodes.length);\n"
            + "  try { body.appendChild(htmlNode); } catch(e) { alert('exception'); };\n"
            + "  alert(body.childNodes.length);\n"
            + "  try { body.insertBefore(htmlNode, body.firstChild); } catch(e) { alert('exception'); };\n"
            + "  alert(body.childNodes.length);\n"
            + "  try { body.replaceChild(htmlNode, body.firstChild); } catch(e) { alert('exception'); };\n"
            + "  alert(body.childNodes.length);\n"
            + "}\n"
            + "</script></head><body onload='test()'><span>hi</span></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test element.appendChild: If the parent has a null parentNode,
     * IE creates a DocumentFragment be the parent's parentNode.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "null", "null" }, IE = { "null", "#document-fragment" })
    public void testInsertBefore_parentNode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void testAppendChild_of_DocumentFragment() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "3", "3", "123", "3", "123", "456", "3", "undefined" }, IE = { "undefined", "not supported" })
    public void testNodePrototype() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(document.body.TEXT_NODE);\n"
            + "      alert(Node.TEXT_NODE);\n"
            + "      document.body.TEXT_NODE = 123;\n"
            + "      alert(document.body.TEXT_NODE);\n"
            + "      alert(Node.TEXT_NODE);\n"
            + "      Node.TEXT_NODE = 456;\n"
            + "      alert(document.body.TEXT_NODE);\n"
            + "      alert(Node.TEXT_NODE);\n"
            + "      delete Node.TEXT_NODE;\n"
            + "      delete document.body.TEXT_NODE;\n"
            + "      alert(document.body.TEXT_NODE);\n"
            + "      alert(Node.TEXT_NODE);\n"
            + "    } catch(e) {\n"
            + "      alert('not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "<div id=\"myDiv2\"></div><div id=\"myDiv3\"></div>", "myDiv2",
            "<div>one</div><div>two</div><div id=\"myDiv3\"></div>" }, IE = { "exception thrown" })
    public void testReplaceChild() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var element = document.getElementById('myDiv2');\n"
            + "      var range = element.ownerDocument.createRange();\n"
            + "      range.setStartAfter(element);\n"
            + "      var fragment = range.createContextualFragment('<div>one</div><div>two</div>');\n"
            + "      var parent = element.parentNode;\n"
            + "      alert(parent.innerHTML);\n"
            + "      alert(parent.replaceChild(fragment, parent.firstChild).id);\n"
            + "      alert(parent.innerHTML);\n"
            + "    } catch(e) {\n"
            + "      alert('exception thrown');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'><div id='myDiv2'></div><div id='myDiv3'></div></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "<div id=\"myDiv2\"></div><div id=\"myDiv3\"></div>", "myDiv2",
            "<div id=\"myDiv3\"></div>" }, IE = { "exception thrown" })
    public void testReplaceChild_EmptyDocumentFragment() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var element = document.getElementById('myDiv2');\n"
            + "    try {\n"
            + "      var range = element.ownerDocument.createRange();\n"
            + "      range.setStartAfter(element);\n"
            + "      var fragment = range.createContextualFragment('');\n"
            + "      var parent = element.parentNode;\n"
            + "      alert(parent.innerHTML);\n"
            + "      alert(parent.replaceChild(fragment, parent.firstChild).id);\n"
            + "      alert(parent.innerHTML);\n"
            + "    } catch(e) {\n"
            + "      alert('exception thrown');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'><div id='myDiv2'></div><div id='myDiv3'></div></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = { "[object]", "[object]" })
    public void event() throws Exception {
        final String firstHtml = "<html>\n"
            + "<head><title>First Page</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var iframe = document.createElement('iframe');\n"
            + "    document.body.appendChild(iframe);\n"
            + "    iframe.contentWindow.location.replace('" + URL_SECOND + "');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "    <input type='button' id='myInput' value='Test me'>\n"
            + "    <div id='myDiv'></div>\n"
            + "</body>\n"
            + "</html>";
        final String secondHtml =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      var handler = function() {\n"
            + "        alert(parent.event);\n"
            + "        parent.document.getElementById('myDiv').style.display = 'none';\n"
            + "        alert(parent.event);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          parent.document.body.attachEvent('onclick', handler);\n"
            + "        } catch(e) { alert('exception') };\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstHtml);
        conn.setResponse(URL_SECOND, secondHtml);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        page.<HtmlButtonInput>getHtmlElementById("myInput").click();
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Verifies that attributes belonging to cloned nodes are available via JavaScript.
     * http://sourceforge.net/tracker/index.php?func=detail&aid=2024741&group_id=47038&atid=448266
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("id=bar")
    public void testCloneAttributesAvailable() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "  <script type='text/javascript'>\n"
            + "    function go() {\n"
            + "        var node = document.getElementById('foo');\n"
            + "        var clone = node.cloneNode(true);\n"
            + "        clone.id = 'bar';\n"
            + "        node.appendChild(clone);\n"
            + "        alert(clone.attributes[0].nodeName + '=' + clone.attributes[0].nodeValue);\n"
            + "    }\n"
            + "  </script>\n"
            + "  </head>\n"
            + "  <body onload='go()'>\n"
            + "    <div id='foo'></div>\n"
            + "  </body>\n"
            + "</html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlElement element = page.getHtmlElementById("bar");
        final String value = element.getAttribute("id");
        assertEquals("bar", value);
    }

    /**
     * Verifies that listeners are copied only for IE.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "in click", IE = { "in click", "in click", "in click" })
    public void testCloneNode_copiesListenerOnlyForIE() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "  <script type='text/javascript'>\n"
            + "    function go() {\n"
            + "        var node = document.createElement('button');\n"
            + "        var f = function() { alert('in click') };\n"
            + "        if (node.attachEvent)\n"
            + "          node.attachEvent('onclick', f);\n"
            + "        else\n"
            + "          node.addEventListener('click', f, true);\n"
            + "        document.body.appendChild(node);\n"
            + "        node.click();\n"
            + "        var clone = node.cloneNode(true);\n"
            + "        document.body.appendChild(clone);\n"
            + "        clone.click();\n"
            + "        var div = document.createElement('div');\n"
            + "        div.appendChild(node);\n"
            + "        var cloneDiv = div.cloneNode(true);\n"
            + "        document.body.appendChild(cloneDiv);\n"
            + "        cloneDiv.firstChild.click();\n"
            + "    }\n"
            + "  </script>\n"
            + "  </head>\n"
            + "  <body onload='go()'>\n"
            + "    <div id='foo'></div>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "1", "1", "2", "4", "8", "16", "32" }, IE = { "undefined", "not supported" })
    public void testDocumentPositionConstants() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(document.body.DOCUMENT_POSITION_DISCONNECTED);\n"
            + "      alert(Node.DOCUMENT_POSITION_DISCONNECTED);\n"
            + "      alert(Node.DOCUMENT_POSITION_PRECEDING);\n"
            + "      alert(Node.DOCUMENT_POSITION_FOLLOWING);\n"
            + "      alert(Node.DOCUMENT_POSITION_CONTAINS);\n"
            + "      alert(Node.DOCUMENT_POSITION_CONTAINED_BY);\n"
            + "      alert(Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC);\n"
            + "    } catch(e) {\n"
            + "      alert('not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "0", "20", "20", "4", "10", "10", "2", "20" })
    public void compareDocumentPosition() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var div2 = document.getElementById('div2');\n"
            + "  var div3 = document.getElementById('div3');\n"
            + "  alert(div1.compareDocumentPosition(div1));\n"
            + "  alert(div1.compareDocumentPosition(div2));\n"
            + "  alert(div1.compareDocumentPosition(div3));\n"
            + "  alert(div1.compareDocumentPosition(div4));\n"
            + "  alert(div2.compareDocumentPosition(div1));\n"
            + "  alert(div3.compareDocumentPosition(div1));\n"
            + "  alert(div4.compareDocumentPosition(div1));\n"
            + "  alert(div2.compareDocumentPosition(div3));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'>\n"
            + "  <div id='div2'>\n"
            + "    <div id='div3'>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "</div>\n"
            + "<div id='div4'>\n"
            + "</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "0", "16" }, IE = "exception")
    public void compareDocumentPosition2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    var childDiv = document.createElement('div');\n"
            + "    try {\n"
            + "      alert(div.compareDocumentPosition(childDiv) & Node.DOCUMENT_POSITION_CONTAINED_BY);\n"
            + "      div.appendChild(childDiv);\n"
            + "      alert(div.compareDocumentPosition(childDiv) & Node.DOCUMENT_POSITION_CONTAINED_BY);\n"
            + "    } catch(e) {alert('exception');}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "[object]", FF = "[object HTMLTableColElement]")
    public void insertBefore() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var table = document.getElementById('myTable');\n"
            + "  var colGroup = table.insertBefore(document.createElement('colgroup'), null);\n"
            + "  alert(colGroup);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <table id='myTable'></table>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2" })
    public void eventListener() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function clicking1() {\n"
            + "    alert(1);\n"
            + "  }\n"
            + "  function clicking2() {\n"
            + "    alert(2);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myAnchor');\n"
            + "    if (e.addEventListener) {\n"
            + "      e.addEventListener('click', clicking1, false);\n"
            + "      e.addEventListener('click', clicking2, false);\n"
            + "    } else if (e.attachEvent) {\n"
            + "      e.attachEvent('onclick', clicking1);\n"
            + "      e.attachEvent('onclick', clicking2);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <a href='" + URL_SECOND + "' id='myAnchor'>Click me</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlPage page2 = page.<HtmlAnchor>getHtmlElementById("myAnchor").click();
        //IE doesn't have specific order
        Collections.sort(collectedAlerts);
        assertEquals(getExpectedAlerts(), collectedAlerts);
        assertEquals(URL_SECOND.toExternalForm(),
                page2.getWebResponse().getRequestSettings().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2" })
    public void eventListener_return_false() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function clicking1() {\n"
            + "    alert(1);\n"
            + "  }\n"
            + "  function clicking2() {\n"
            + "    alert(2);\n"
            + "    return false;\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myAnchor');\n"
            + "    if (e.addEventListener) {\n"
            + "      e.addEventListener('click', clicking1, false);\n"
            + "      e.addEventListener('click', clicking2, false);\n"
            + "    } else if (e.attachEvent) {\n"
            + "      e.attachEvent('onclick', clicking1);\n"
            + "      e.attachEvent('onclick', clicking2);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <a href='" + URL_SECOND + "' id='myAnchor'>Click me</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlPage page2 = page.<HtmlAnchor>getHtmlElementById("myAnchor").click();
        //IE doesn't have specific order
        Collections.sort(collectedAlerts);
        assertEquals(getExpectedAlerts(), collectedAlerts);

        final URL expectedURL;
        if (getBrowserVersion().isIE()) {
            expectedURL = getDefaultUrl();
        }
        else {
            expectedURL = URL_SECOND;
        }
        assertEquals(expectedURL.toExternalForm(),
                page2.getWebResponse().getRequestSettings().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2" })
    public void eventListener_returnValue_false() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function clicking1() {\n"
            + "    alert(1);\n"
            + "  }\n"
            + "  function clicking2() {\n"
            + "    alert(2);\n"
            + "    if (window.event)\n"
            + "      window.event.returnValue = false;\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myAnchor');\n"
            + "    if (e.addEventListener) {\n"
            + "      e.addEventListener('click', clicking1, false);\n"
            + "      e.addEventListener('click', clicking2, false);\n"
            + "    } else if (e.attachEvent) {\n"
            + "      e.attachEvent('onclick', clicking1);\n"
            + "      e.attachEvent('onclick', clicking2);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <a href='" + URL_SECOND + "' id='myAnchor'>Click me</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlPage page2 = page.<HtmlAnchor>getHtmlElementById("myAnchor").click();
        //IE doesn't have specific order
        Collections.sort(collectedAlerts);
        assertEquals(getExpectedAlerts(), collectedAlerts);

        final URL expectedURL;
        if (getBrowserVersion().isIE()) {
            expectedURL = getDefaultUrl();
        }
        else {
            expectedURL = URL_SECOND;
        }
        assertEquals(expectedURL.toExternalForm(),
                page2.getWebResponse().getRequestSettings().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bk")
    public void prefix() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var text = \"<bk:book xmlns:bk='urn:loc.gov:books'></bk:book>\";\n"
            + "  if (window.ActiveXObject) {\n"
            + "    var doc = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    doc.async = false;\n"
            + "    doc.loadXML(text);\n"
            + "  } else {\n"
            + "    var parser = new DOMParser();\n"
            + "    var doc = parser.parseFromString(text, 'text/xml');\n"
            + "  }\n"
            + "  alert(doc.documentElement.prefix);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<root><![CDATA[abc]]><![CDATA[def]]></root>")
    public void xml() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    var root = doc.appendChild(doc.createElement('root'));\n"
            + "    var cdata = root.appendChild(doc.createCDATASection('abcdef'));\n"
            + "    cdata.splitText(3);\n"
            + "    if (document.all)"
            + "      alert(root.xml);\n"
            + "    else\n"
            + "      alert(new XMLSerializer().serializeToString(root));\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = "exception")
    public void insertBefore2() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
                + "<script>\n"
                + "function doTest(){\n"
                + "  try {\n"
                + "    var e = document.createElement('div');\n"
                + "    e.innerHTML='new element';\n"
                + "    document.body.insertBefore(e, undefined);\n"
                + "  } catch(e) {alert('exception');}\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(FF = "exception")
    public void insertBefore3() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
                + "<script>\n"
                + "function doTest(){\n"
                + "  try {\n"
                + "    var e = document.createElement('div');\n"
                + "    e.innerHTML='new element';\n"
                + "    document.body.insertBefore(e);\n"
                + "  } catch(e) {alert('exception');}\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "4", "3", "abc", "def", "123456", "false", "0", "2", "123", "456", "1", "false" },
            FF = { "4", "3", "abc", "def", "123456", "true", "0", "2", "123", "456", "1", "true" })
    public void normalize() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    var root = doc.appendChild(doc.createElement('root'));\n"
            + "    var cdata = root.appendChild(doc.createCDATASection('abcdef'));\n"
            + "    cdata.splitText(3);\n"
            + "    var text = root.appendChild(doc.createTextNode('123456'));\n"
            + "    text.splitText(3);\n"
            + "    alert(root.childNodes.length);\n"
            + "    root.normalize();\n"
            + "    alert(root.childNodes.length);\n"
            + "    alert(root.childNodes.item(0).data);\n"
            + "    alert(root.childNodes.item(1).data);\n"
            + "    alert(root.childNodes.item(2).data);\n"
            + "    alert(root.childNodes.item(2) == text);\n"
            + "\n"
            + "    var body = document.body;\n"
            + "    alert(body.childNodes.length);\n"
            + "    text = body.appendChild(document.createTextNode('123456'));\n"
            + "    text.splitText(3);\n"
            + "    alert(body.childNodes.length);\n"
            + "    alert(body.childNodes.item(0).nodeValue);\n"
            + "    alert(body.childNodes.item(1).nodeValue);\n"
            + "    body.normalize();\n"
            + "    alert(body.childNodes.length);\n"
            + "    alert(body.childNodes.item(0) == text);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";
        loadPageWithAlerts2(html);
    }

}
