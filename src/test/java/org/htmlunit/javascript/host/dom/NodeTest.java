/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript.host.dom;

import static org.htmlunit.javascript.host.xml.XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION;
import static org.htmlunit.javascript.host.xml.XMLDocumentTest.SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION;
import static org.htmlunit.javascript.host.xml.XMLDocumentTest.callLoadXMLDocumentFromString;
import static org.htmlunit.javascript.host.xml.XMLDocumentTest.callSerializeXMLDocumentToString;

import org.apache.commons.lang3.ArrayUtils;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for {@link Node}.
 *
 * @author Brad Clarke
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Bruce Faulkner
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class NodeTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[object HTMLSpanElement]", "[object Text]", "null"})
    public void lastChild() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  log(document.getElementById('myNode').lastChild);\n"
                + "  log(document.getElementById('onlyTextNode').lastChild);\n"
                + "  log(document.getElementById('emptyNode').lastChild);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "  <div id='myNode'>hello world<span>Child Node</span></div>\n"
                + "  <div id='onlyTextNode'>hello</div>\n"
                + "  <div id='emptyNode'></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("true")
    public void hasChildNodes_true() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  log(document.getElementById('myNode').hasChildNodes());\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "<p id='myNode'>hello world<span>Child Node</span></p>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("false")
    public void hasChildNodes_false() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  log(document.getElementById('myNode').hasChildNodes());\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "<p id='myNode'></p>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"4", "function", "3"},
            IE = {"4", "undefined", "exception"})
    public void remove() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='div1'></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var div1 = document.getElementById('div1');\n"
            + "try {\n"
            + "  log(document.body.childNodes.length);\n"
            + "  log(typeof div1.remove);\n"
            + "  div1.remove();\n"
            + "  log(document.body.childNodes.length);\n"
            + "}\n"
            + "catch (e) { log('exception'); }\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for removeChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void removeChild() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var form = document.forms['form1'];\n"
            + "  var div = form.firstChild;\n"
            + "  var removedDiv = form.removeChild(div);\n"
            + "  log(div==removedDiv);\n"
            + "  log(form.firstChild == null);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'><div id='formChild'/></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void removeChildSibling() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var div2 = document.getElementById('div2');\n"
            + "  try {\n"
            + "    div1.removeChild(div2);\n"
            + "  } catch(e) { log('exception') }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <div id='div1'></div>\n"
            + "  <div id='div2'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for replaceChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void replaceChild_Normal() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var form = document.forms['form1'];\n"
            + "  var div1 = form.firstChild;\n"
            + "  var div2 = document.getElementById('newChild');\n"
            + "  var removedDiv = form.replaceChild(div2,div1);\n"
            + "  log(div1 == removedDiv);\n"
            + "  log(form.firstChild == div2);\n"
            + "  var newDiv = document.createElement('div');\n"
            + "  form.replaceChild(newDiv, div2);\n"
            + "  log(form.firstChild == newDiv);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'><div id='formChild'/></form>\n"
            + "</body><div id='newChild'/></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The common browsers always return node names in uppercase. Test this.
     * @throws Exception on test failure
     */
    @Test
    @Alerts("DIV")
    public void nodeNameIsUppercase() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  log(document.getElementById('myNode').nodeName);\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "<div id='myNode'>hello world<span>Child Node</span></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"2", "SPAN", "2", "#text", "H1", "H2"})
    public void getChildNodes() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var aNode = document.getElementById('myNode');\n"
            + "  log(aNode.childNodes.length);\n"
            + "  log(aNode.childNodes[0].nodeName);\n"
            + "  log(aNode.childNodes[0].childNodes.length);\n"
            + "  log(aNode.childNodes[0].childNodes[0].nodeName);\n"
            + "  log(aNode.childNodes[0].childNodes[1].nodeName);\n"
            + "  log(aNode.childNodes[1].nodeName);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "<div id='myNode'><span>Child Node 1-A"
            + "<h1>Child Node 1-B</h1></span>"
            + "<h2>Child Node 2-A</h2></div>"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"nb nodes: 2", "8", "1"})
    public void childNodes_Comments() throws Exception {
        final String html = "<html><head>\n"
            + "</head>\n"
            + "<body><!-- comment --><script>\n"
            + LOG_TITLE_FUNCTION
            + "var nodes = document.body.childNodes;\n"
            + "log('nb nodes: ' + nodes.length);\n"
            + "for (var i = 0; i < nodes.length; i++)\n"
            + "  log(nodes[i].nodeType);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"length: 5",
        "tempNode.name: undefined", "tempNode.name: input1", "tempNode.name: undefined",
        "tempNode.name: input2", "tempNode.name: undefined"})
    public void getChildNodesProperties() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var testForm = document.getElementById('testForm');\n"
            + "  var childNodes = testForm.childNodes;\n"
            + "  var length = childNodes.length;\n"
            + "  log('length: ' + length);\n"
            + "  for (var i = 0; i < length; i++) {\n"
            + "    var tempNode = childNodes.item(i);\n"
            + "    log('tempNode.name: ' + tempNode.name);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "<form name='testForm' id='testForm'>foo\n" // some text, because IE doesn't see "\n" as a text node here
            + "<input type='hidden' name='input1' value='1'>\n"
            + "<input type='hidden' name='input2' value='2'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The common browsers always return node names in uppercase. Test this.
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"document: 9", "document.body: 1", "body child 1: 3", "body child 2: 8"})
    public void nodeType() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  log('document: ' + document.nodeType);\n"
                + "  log('document.body: ' + document.body.nodeType);\n"
                + "  log('body child 1: ' + document.body.childNodes[0].nodeType);\n"
                + "  log('body child 2: ' + document.body.childNodes[1].nodeType);\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "some text<!-- some comment -->\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for bug 1716129.
     * @throws Exception on test failure
     */
    @Test
    @Alerts("exception")
    public void attachEvent() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var oField = document.getElementById('div1');\n"
            + "  try {\n"
            + "    oField.attachEvent('onclick', foo1);\n"
            + "    oField.attachEvent('onclick', foo2);\n"
            + "  } catch(e) { log('exception') }\n"
            + "}\n"
            + "function foo1() {log('in foo1');}\n"
            + "function foo2() {log('in foo2');}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'>bla</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, getExpectedAlerts());

        driver.findElement(By.id("div1")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "true", "false", "false", "false"})
    public void isEqualNode() throws Exception {
        final String html = "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var list1 = document.getElementById(\"list1\");\n"
                + "    var list2 = document.getElementById(\"list2\");\n"
                + "    log(list1.isEqualNode(list2));\n"
                + "\n"
                + "    var nodes = document.getElementsByClassName(\"test\");\n"
                + "    log(nodes[0].isEqualNode(nodes[1]));\n"
                + "    log(nodes[0].isEqualNode(nodes[2]));\n"
                + "    log(nodes[0].isEqualNode(nodes[3]));\n"
                + "    log(nodes[0].isEqualNode(nodes[4]));\n"
                + "    log(nodes[0].isEqualNode(nodes[5]));"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "<ul id=\"list1\">\n"
                + "    <li>foo</li>\n"
                + "    <li>bar</li>\n"
                + "</ul>\n"
                + "<ul id=\"list2\">\n"
                + "    <li>foo</li>\n"
                + "    <li>bar</li>\n"
                + "</ul>\n"
                + "\n"
                + "<ul class=\"test\">\n"
                + "    <li>foo</li>\n"
                + "    <li>bar</li>\n"
                + "</ul>\n"
                + "<ul class=\"test\">\n"
                + "    <li>foo</li>     \n"
                + "    <li>bar</li>\n"
                + "</ul>\n"
                + "<ul class=\"test\">\n"
                + "    <li>foo</li>\n"
                + "    <li>bar</li>\n"
                + "</ul>\n"
                + "<ul class=\"test\">\n"
                + "    <li>foo</li>\n"
                + "\n"
                + "    <li>bar</li>\n"
                + "</ul>\n"
                + "<ul class=\"test\">\n"
                + "    <li>foo</li>\n"
                + "    <li>bar</li>\n"
                + "    <li></li>\n"
                + "</ul>\n"
                + "<ul class=\"test\">\n"
                + "    <li>foobar</li>\n"
                + "    <li></li>\n"
                + "</ul>"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void isSameNode() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var d2 = document.getElementById('div2');\n"
            + "    try {\n"
            + "      log(d1.isSameNode(d1));\n"
            + "      log(d1.isSameNode(d2));\n"
            + "    } catch(e) {\n"
            + "      log('isSameNode not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'/>\n"
            + "<div id='div2'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test element.appendChild: If the parent has a null parentNode,
     * IE creates a DocumentFragment be the parent's parentNode.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null"})
    public void appendChild_parentNode() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.createElement('div');\n"
            + "    var div2 = document.createElement('div');\n"
            + "    log(div1.parentNode);\n"
            + "    div1.appendChild(div2);\n"
            + "    if(div1.parentNode)\n"
            + "      log(div1.parentNode.nodeName);\n"
            + "    else\n"
            + "      log(div1.parentNode);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The HTML node can't be inserted anywhere else!
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "exception", "1", "exception", "1", "exception", "1"})
    public void append_insert_html_node() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var htmlNode = document.documentElement;\n"
            + "  var body = document.body;\n"
            + "  log(body.childNodes.length);\n"
            + "  try { body.appendChild(htmlNode); } catch(e) { log('exception'); }\n"
            + "  log(body.childNodes.length);\n"
            + "  try { body.insertBefore(htmlNode, body.firstChild); } catch(e) { log('exception'); }\n"
            + "  log(body.childNodes.length);\n"
            + "  try { body.replaceChild(htmlNode, body.firstChild); } catch(e) { log('exception'); }\n"
            + "  log(body.childNodes.length);\n"
            + "}\n"
            + "</script></head><body onload='test()'><span>hi</span></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void appendChild_of_DocumentFragment() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
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
            + "    log(div.childNodes.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "3", "3", "3", "3", "3", "3"})
    public void nodePrototype() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.body.TEXT_NODE);\n"
            + "      log(Node.TEXT_NODE);\n"
            + "      document.body.TEXT_NODE = 123;\n"
            + "      log(document.body.TEXT_NODE);\n"
            + "      log(Node.TEXT_NODE);\n"
            + "      Node.TEXT_NODE = 456;\n"
            + "      log(document.body.TEXT_NODE);\n"
            + "      log(Node.TEXT_NODE);\n"
            + "      delete Node.TEXT_NODE;\n"
            + "      delete document.body.TEXT_NODE;\n"
            + "      log(document.body.TEXT_NODE);\n"
            + "      log(Node.TEXT_NODE);\n"
            + "    } catch(e) {\n"
            + "      log('not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div id=\"myDiv2\"></div><div id=\"myDiv3\"></div>", "myDiv2",
             "<div>one</div><div>two</div><div id=\"myDiv3\"></div>"})
    public void replaceChild() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var element = document.getElementById('myDiv2');\n"
            + "      var range = element.ownerDocument.createRange();\n"
            + "      range.setStartAfter(element);\n"
            + "      var fragment = range.createContextualFragment('<div>one</div><div>two</div>');\n"
            + "      var parent = element.parentNode;\n"
            + "      log(parent.innerHTML);\n"
            + "      log(parent.replaceChild(fragment, parent.firstChild).id);\n"
            + "      log(parent.innerHTML);\n"
            + "    } catch(e) {\n"
            + "      log('exception thrown');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'><div id='myDiv2'></div><div id='myDiv3'></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div id=\"myDiv2\"></div><div id=\"myDiv3\"></div>", "myDiv2",
             "<div id=\"myDiv3\"></div>"})
    public void replaceChild_EmptyDocumentFragment() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var element = document.getElementById('myDiv2');\n"
            + "    try {\n"
            + "      var range = element.ownerDocument.createRange();\n"
            + "      range.setStartAfter(element);\n"
            + "      var fragment = range.createContextualFragment('');\n"
            + "      var parent = element.parentNode;\n"
            + "      log(parent.innerHTML);\n"
            + "      log(parent.replaceChild(fragment, parent.firstChild).id);\n"
            + "      log(parent.innerHTML);\n"
            + "    } catch(e) {\n"
            + "      log('exception thrown');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'><div id='myDiv2'></div><div id='myDiv3'></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that listeners are copied only for IE.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("in click")
    public void cloneNode_copiesListenerOnlyForIE() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function go() {\n"
            + "      var node = document.createElement('button');\n"
            + "      var f = function() { log('in click') };\n"
            + "      if (node.attachEvent)\n"
            + "        node.attachEvent('onclick', f);\n"
            + "      else\n"
            + "        node.addEventListener('click', f, true);\n"
            + "      document.body.appendChild(node);\n"
            + "      node.click();\n"
            + "      var clone = node.cloneNode(true);\n"
            + "      document.body.appendChild(clone);\n"
            + "      clone.click();\n"
            + "      var div = document.createElement('div');\n"
            + "      div.appendChild(node);\n"
            + "      var cloneDiv = div.cloneNode(true);\n"
            + "      document.body.appendChild(cloneDiv);\n"
            + "      cloneDiv.firstChild.click();\n"
            + "    }\n"
            + "  </script>\n"
            + "  </head>\n"
            + "  <body onload='go()'>\n"
            + "    <div id='foo'></div>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "2", "4", "8", "16", "32"})
    public void documentPositionConstants() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.body.DOCUMENT_POSITION_DISCONNECTED);\n"
            + "      log(Node.DOCUMENT_POSITION_DISCONNECTED);\n"
            + "      log(Node.DOCUMENT_POSITION_PRECEDING);\n"
            + "      log(Node.DOCUMENT_POSITION_FOLLOWING);\n"
            + "      log(Node.DOCUMENT_POSITION_CONTAINS);\n"
            + "      log(Node.DOCUMENT_POSITION_CONTAINED_BY);\n"
            + "      log(Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC);\n"
            + "    } catch(e) {\n"
            + "      log('not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "20", "20", "4", "10", "10", "2", "20", "exception"})
    public void compareDocumentPosition() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var div2 = document.getElementById('div2');\n"
            + "  var div3 = document.getElementById('div3');\n"
            + "  if (!div1.compareDocumentPosition) { log('compareDocumentPosition not available'); return }\n"

            + "  log(div1.compareDocumentPosition(div1));\n"
            + "  log(div1.compareDocumentPosition(div2));\n"
            + "  log(div1.compareDocumentPosition(div3));\n"
            + "  log(div1.compareDocumentPosition(div4));\n"
            + "  log(div2.compareDocumentPosition(div1));\n"
            + "  log(div3.compareDocumentPosition(div1));\n"
            + "  log(div4.compareDocumentPosition(div1));\n"
            + "  log(div2.compareDocumentPosition(div3));\n"
            + "  try {\n"
            + "    log(div2.compareDocumentPosition({}));\n"
            + "  } catch(e) { log('exception'); }\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "16"})
    public void compareDocumentPosition2() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    var childDiv = document.createElement('div');\n"
            + "    try {\n"
            + "      log(div.compareDocumentPosition(childDiv) & Node.DOCUMENT_POSITION_CONTAINED_BY);\n"
            + "      div.appendChild(childDiv);\n"
            + "      log(div.compareDocumentPosition(childDiv) & Node.DOCUMENT_POSITION_CONTAINED_BY);\n"
            + "    } catch(e) {log('exception');}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bk")
    public void prefix() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var text = \"<bk:book xmlns:bk='urn:loc.gov:books'></bk:book>\";\n"
            + "  var doc = " + callLoadXMLDocumentFromString("text") + ";\n"
            + "  log(doc.documentElement.prefix);\n"
            + "}\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<root><![CDATA[abc]]><![CDATA[def]]></root>")
    public void xml() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    var root = doc.appendChild(doc.createElement('root'));\n"
            + "    var cdata = root.appendChild(doc.createCDATASection('abcdef'));\n"
            + "    cdata.splitText(3);\n"
            + "    log(" + callSerializeXMLDocumentToString("doc") + ");\n"
            + "  }\n"
            + SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test to verify that insertBefore correctly appends
     * the new child object when the reference child object is null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "SPAN"})
    public void insertBefore_nullRef() throws Exception {
        insertBefore("aNode.insertBefore(newNode, null);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void insertBefore_myself() throws Exception {
        insertBefore("aNode.insertBefore(newNode, newNode);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void insertBefore_sibling() throws Exception {
        insertBefore("aNode.insertBefore(newNode, siblingNode);");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("done")
    public void insertBefore_undefinedRef() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  try {\n"
                + "    var e = document.createElement('div');\n"
                + "    e.innerHTML = 'new element';\n"
                + "    document.body.insertBefore(e, undefined);\n"
                + "    log('done');"
                + "  } catch(e) {log('exception');}\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test to verify that insertBefore correctly appends
     * the new child object when the reference child object is null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void insertBefore_noArgs() throws Exception {
        insertBefore("aNode.insertBefore();");
    }

    /**
     * Regression test to verify that insertBefore correctly appends
     * the new child object when the reference child object is null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = {"3", "SPAN"})
    public void insertBefore_noSecondArg() throws Exception {
        insertBefore("aNode.insertBefore(newNode);");
    }

    /**
     * @throws Exception if the test fails
     */
    private void insertBefore(final String insertJSLine) throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var newNode = document.createElement('span');\n"
            + "      var siblingNode = document.getElementById('sibingNode');\n"
            + "      var aNode = document.getElementById('myNode');\n"
            + "      try {\n"
            + insertJSLine
            + "        log(aNode.childNodes.length);\n"
            + "        log(aNode.childNodes[2].nodeName);\n"
            + "      }\n"
            + "      catch (e) { log('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='myNode'><span>Child Node 1-A</span><h1>Child Node 2-A</h1></div>\n"
            + "<h2 id='sibingNode'>Sibling</h2>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test to verify that insertBefore correctly appends
     * the new child object when the reference child object is null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "SPAN"})
    public void insertBeforeFragment_nullRef() throws Exception {
        insertBeforeFragment("aNode.insertBefore(fragment, null);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void insertBeforeFragment_myself() throws Exception {
        insertBeforeFragment("aNode.insertBefore(fragment, fragment);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void insertBeforeFragment_sibling() throws Exception {
        insertBeforeFragment("aNode.insertBefore(fragment, siblingNode);");
    }

    /**
     * Regression test to verify that insertBefore correctly appends
     * the new child object when the reference child object is null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = {"3", "SPAN"})
    public void insertBeforeFragment_noSecondArg() throws Exception {
        insertBeforeFragment("aNode.insertBefore(fragment);");
    }

    /**
     * @throws Exception if the test fails
     */
    private void insertBeforeFragment(final String insertJSLine) throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var fragment = document.createDocumentFragment('span');\n"
            + "      fragment.appendChild(document.createElement('span'));\n"
            + "      var aNode = document.getElementById('myNode');\n"
            + "      try {\n"
            + insertJSLine
            + "        log(aNode.childNodes.length);\n"
            + "        log(aNode.childNodes[2].nodeName);\n"
            + "      }\n"
            + "      catch (e) { log('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='myNode'><h6>Child Node 1-A</h6><h1>Child Node 2-A</h1></div>\n"
            + "<h2 id='sibingNode'>Sibling</h2>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test element.appendChild: If the parent has a null parentNode,
     * IE creates a DocumentFragment be the parent's parentNode.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null"})
    public void insertBefore_parentNode() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.createElement('div');\n"
            + "    var div2 = document.createElement('div');\n"
            + "    log(div1.parentNode);\n"
            + "    div1.insertBefore(div2, null);\n"
            + "    if(div1.parentNode)\n"
            + "      log(div1.parentNode.nodeName);\n"
            + "    else\n"
            + "      log(div1.parentNode);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLTableColElement]")
    public void insertBefore_inTable() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var table = document.getElementById('myTable');\n"
            + "  var colGroup = table.insertBefore(document.createElement('colgroup'), null);\n"
            + "  log(colGroup);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <table id='myTable'></table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = {})
    public void insertBefore_newElement() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  try {\n"
                + "    var e = document.createElement('div');\n"
                + "    e.innerHTML = 'new element';\n"
                + "    document.body.insertBefore(e);\n"
                + "  } catch(e) {log('exception');}\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='doTest()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"4", "3", "abc", "def", "123456", "true", "0", "2", "123", "456", "1", "true"},
            IE = {"4", "3", "abc", "def", "123456", "false", "0", "2", "123", "456", "1", "false"})
    public void normalize() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    var root = doc.appendChild(doc.createElement('root'));\n"
            + "    var cdata = root.appendChild(doc.createCDATASection('abcdef'));\n"
            + "    cdata.splitText(3);\n"
            + "    var text = root.appendChild(doc.createTextNode('123456'));\n"
            + "    text.splitText(3);\n"
            + "    log(root.childNodes.length);\n"
            + "    root.normalize();\n"
            + "    log(root.childNodes.length);\n"
            + "    log(root.childNodes.item(0).data);\n"
            + "    log(root.childNodes.item(1).data);\n"
            + "    log(root.childNodes.item(2).data);\n"
            + "    log(root.childNodes.item(2) == text);\n"
            + "\n"
            + "    var body = document.body;\n"
            + "    log(body.childNodes.length);\n"
            + "    text = body.appendChild(document.createTextNode('123456'));\n"
            + "    text.splitText(3);\n"
            + "    log(body.childNodes.length);\n"
            + "    log(body.childNodes.item(0).nodeValue);\n"
            + "    log(body.childNodes.item(1).nodeValue);\n"
            + "    body.normalize();\n"
            + "    log(body.childNodes.length);\n"
            + "    log(body.childNodes.item(0) == text);\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Element]", "[object HTMLHtmlElement]"},
            IE = {"undefined", "[object HTMLHtmlElement]"})
    public void parentElement() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var text = '<hello>hi</hello>';\n"
            + "  var doc = " + callLoadXMLDocumentFromString("text") + ";\n"
            + "  log(doc.documentElement.firstChild.parentElement);\n"
            + "  log(document.body.parentElement);\n"
            + "}\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"hi", "undefined", "abcd", "undefined"},
            IE = {"hi", "null", "abcd", "null"})
    public void attributes() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var text = '<hello>hi</hello>';\n"
            + "  var doc = " + callLoadXMLDocumentFromString("text") + ";\n"
            + "  var node = doc.documentElement.firstChild;\n"
            + "  log(node.nodeValue);\n"
            + "  log(node.attributes);\n"
            + "\n"
            + "  node = document.getElementById('myId').firstChild;\n"
            + "  log(node.nodeValue);\n"
            + "  log(node.attributes);\n"
            + "}\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myId'>abcd</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true"})
    public void addEventListener() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var node = document.createElement('button');\n"
            + "      log(node.addEventListener !== undefined);\n"
            + "      log(node.removeEventListener !== undefined);\n"
            + "    }\n"
            + "  </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void event() throws Exception {
        final String firstHtml = "<html>\n"
            + "<head><title>First Page</title>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "  function test() {\n"
            + "    var iframe = document.createElement('iframe');\n"
            + "    document.body.appendChild(iframe);\n"
            + "    iframe.contentWindow.location.replace('" + URL_SECOND + "');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='button' id='myInput' value='Test me'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body>\n"
            + "</html>";
        final String secondHtml =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "      var handler = function() {\n"
            + "        log(parent.event);\n"
            + "        parent.document.getElementById('myDiv').style.display = 'none';\n"
            + "        log(parent.event);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          parent.document.body.attachEvent('onclick', handler);\n"
            + "        } catch(e) { log('exception') }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final WebDriver driver = loadPage2(firstHtml);
        Thread.sleep(200);
        verifyWindowName2(driver, getExpectedAlerts());

        driver.findElement(By.id("myInput")).click();
    }

    /**
     * Verifies that attributes belonging to cloned nodes are available via JavaScript.
     * http://sourceforge.net/p/htmlunit/bugs/659/
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("id=bar")
    public void cloneAttributesAvailable() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function go() {\n"
            + "      var node = document.getElementById('foo');\n"
            + "      var clone = node.cloneNode(true);\n"
            + "      clone.id = 'bar';\n"
            + "      node.appendChild(clone);\n"
            + "      log(clone.attributes['id'].nodeName + '=' + clone.attributes['id'].nodeValue);\n"
            + "    }\n"
            + "  </script>\n"
            + "  </head>\n"
            + "  <body onload='go()'>\n"
            + "    <div id='foo'></div>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPageVerifyTitle2(html);

        final WebElement element = driver.findElement(By.id("bar"));
        final String value = element.getAttribute("id");
        assertEquals("bar", value);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello")
    public void setTextContent() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var foo = document.getElementById('foo');\n"
            + "      foo.textContent = 'Hello';\n"
            + "      if (foo.firstChild) {\n"
            + "        log(foo.firstChild.wholeText);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <span id='foo'></span>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("null")
    public void cloneParent() throws Exception {
        final String html =
              "<!DOCTYPE><html>\n"
            + "  <head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var foo = document.getElementById('foo');\n"
            + "      var clone = foo.cloneNode(true);\n"
            + "      log(clone.parentNode);\n"
            + "      if (clone.parentNode) {\n"
            + "        log(clone.parentNode.URL);\n"
            + "        log(clone.parentNode.nodeType);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <span id='foo'></span>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "<div><span></span>nullundefinedhello<p></p></div>",
            IE = "<div><p></p></div>")
    public void before() throws Exception {
        final String html =
              "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var parent = document.createElement('div');\n"
            + "      var child = document.createElement('p');\n"
            + "      parent.appendChild(child);\n"
            + "      var span = document.createElement('span');\n"
            + "      if (child.before) {"
            + "        child.before(span, null, undefined, 'hello');\n"
            + "      }\n"
            + "      log(parent.outerHTML);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "<div><p></p><span></span>nullundefinedhello</div>",
            IE = "<div><p></p></div>")
    public void after() throws Exception {
        final String html =
              "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var parent = document.createElement('div');\n"
            + "      var child = document.createElement('p');\n"
            + "      parent.appendChild(child);\n"
            + "      var span = document.createElement('span');\n"
            + "      if (child.after) {"
            + "        child.after(span, null, undefined, 'hello');\n"
            + "      }\n"
            + "      log(parent.outerHTML);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "<div><span></span>nullundefinedhello</div>",
            IE = "<div><p></p></div>")
    public void replaceWith() throws Exception {
        final String html =
              "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var parent = document.createElement('div');\n"
            + "      var child = document.createElement('p');\n"
            + "      parent.appendChild(child);\n"
            + "      var span = document.createElement('span');\n"
            + "      if (child.replaceWith) {"
            + "        child.replaceWith(span, null, undefined, 'hello');\n"
            + "      }\n"
            + "      log(parent.outerHTML);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2", "§§URL§§second"})
    public void eventListener() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "  function clicking1() {\n"
            + "    log(1);\n"
            + "  }\n"
            + "  function clicking2() {\n"
            + "    log(2);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myAnchor');\n"
            + "    e.addEventListener('click', clicking1, false);\n"
            + "    e.addEventListener('click', clicking2, false);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <a href='second' id='myAnchor'>Click me</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><body>Test</body></html>");
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myAnchor")).click();
        verifyWindowName2(driver, ArrayUtils.subarray(getExpectedAlerts(), 0, 2));
        Thread.sleep(200);
        assertEquals(getExpectedAlerts()[2], driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2", "§§URL§§second"})
    public void eventListener_return_false() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "  function clicking1() {\n"
            + "    log(1);\n"
            + "  }\n"
            + "  function clicking2() {\n"
            + "    log(2);\n"
            + "    return false;\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myAnchor');\n"
            + "    e.addEventListener('click', clicking1, false);\n"
            + "    e.addEventListener('click', clicking2, false);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <a href='second' id='myAnchor'>Click me</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><body>Test</body></html>");
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myAnchor")).click();
        verifyWindowName2(driver, ArrayUtils.subarray(getExpectedAlerts(), 0, 2));
        Thread.sleep(200);
        assertEquals(getExpectedAlerts()[2], driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "2", "§§URL§§"},
            IE = {"1", "2", "§§URL§§second"})
    public void eventListener_returnValue_false() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "  function clicking1() {\n"
            + "    log(1);\n"
            + "  }\n"
            + "  function clicking2() {\n"
            + "    log(2);\n"
            + "    if (window.event)\n"
            + "      window.event.returnValue = false;\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myAnchor');\n"
            + "    e.addEventListener('click', clicking1, false);\n"
            + "    e.addEventListener('click', clicking2, false);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <a href='second' id='myAnchor'>Click me</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><body>Test</body></html>");
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myAnchor")).click();
        verifyWindowName2(driver, ArrayUtils.subarray(getExpectedAlerts(), 0, 2));
        Thread.sleep(200);
        assertEquals(getExpectedAlerts()[2], driver.getCurrentUrl());
    }
}
