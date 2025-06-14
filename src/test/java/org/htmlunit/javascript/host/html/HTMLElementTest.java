/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import java.net.URL;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for {@link HTMLElement}.
 *
 * @author Brad Clarke
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Hans Donner
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Ethan Glasser-Camp
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"all is not supported", "all is not supported",
             "all is not supported", "all is not supported", "all is not supported"})
    public void all_IndexByInt() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  dumpAll('body');\n"
            + "  dumpAll('testDiv');\n"
            + "  dumpAll('testA');\n"
            + "  dumpAll('testImg');\n"
            + "  dumpAll('testDiv2');\n"
            + "}\n"
            + "function dumpAll(_id) {\n"
            + "  var oNode = document.getElementById(_id);\n"
            + "  var col = oNode.all;\n"
            + "  if (col) {\n"
            + "    var str = 'all node for ' + _id + ': ';\n"
            + "    for (var i = 0; i < col.length; i++) {\n"
            + "      str += col[i].tagName + ' ';\n"
            + "    }\n"
            + "    log(str);\n"
            + "  } else {\n"
            + "    log('all is not supported');\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()' id='body'>\n"
            + "  <div id='testDiv'>foo<a href='foo.html' id='testA'><img src='foo.png' id='testImg'></a></div>\n"
            + "  <div id='testDiv2'>foo</div>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"a", "a", "undefined", "null"})
    public void getAttribute() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <title>test</title>\n"
                + "  <script>\n"
                + LOG_TEXTAREA_FUNCTION
                + "  function doTest() {\n"
                + "    var myNode = document.getElementById('myNode');\n"
                + "    log(myNode.title);\n"
                + "    log(myNode.getAttribute('title'));\n"
                + "    log(myNode.Title);\n"
                + "    log(myNode.getAttribute('class'));\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<p id='myNode' title='a'></p>\n"
                + LOG_TEXTAREA
                + "</body>\n"
                + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void getAttribute_styleAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var elem = document.getElementById('tester');\n"
            + "    log(elem.getAttribute('style'));\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <div id='tester'>tester</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("color: green;")
    public void getAttribute_styleAttributeWithFlag() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><div id='div' style='color: green;'>abc</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('div');\n"
            + "    log(div.getAttribute('style', 2));\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Some libraries like MochiKit looks after the number of attributes of a freshly created node.
     * When this is fixed for IE, all {@link org.htmlunit.libraries.MochiKitTest}
     * working for FF will work for IE too.
     * @throws Exception on test failure
     */
    @Test
    @Alerts("0 attribute")
    public void attributes() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function doTest() {\n"
                + "    var myNode = document.body.firstChild;\n"
                + "    if (myNode.attributes.length == 0)\n"
                + "      log('0 attribute');\n"
                + "    else\n"
                + "      log('at least 1 attribute');\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>" // no \n here!
                + "<span>test span</span>\n"
                + "</body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"null", "bla", "true"})
    public void getSetAttributeNS() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  var myNode = document.getElementById('myNode');\n"
                + "  log(myNode.getAttributeNS('myNamespaceURI', 'my:foo'));\n"
                + "  myNode.setAttributeNS('myNamespaceURI', 'my:foo', 'bla');\n"
                + "  log(myNode.getAttributeNS('myNamespaceURI', 'foo'));\n"
                + "  log(myNode.getAttributeNodeNS('myNamespaceURI', 'foo').specified);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<p id='myNode' title='a'>\n"
                + "</p>\n"
                + "</body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"text", "i", "i", "[object CSS2Properties]", "function", "undefined", "undefined"},
            CHROME = {"text", "i", "i", "[object CSSStyleDeclaration]", "function", "undefined", "undefined"},
            EDGE = {"text", "i", "i", "[object CSSStyleDeclaration]", "function", "undefined", "undefined"})
    @HtmlUnitNYI(FF = {"text", "i", "i", "[object CSSStyleDeclaration]", "function", "undefined", "undefined"},
            FF_ESR = {"text", "i", "i", "[object CSSStyleDeclaration]", "function", "undefined", "undefined"})
    public void attributesAccess() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input type='text' id='i' name='i' style='color:red' onclick='log(1)' custom1='a' />\n"
            + "  <script>\n"
            + "    var i = document.getElementById('i');\n"
            + "    log(i.type);\n"
            + "    log(i.id);\n"
            + "    log(i.name);\n"
            + "    log(i.style);\n"
            + "    log(typeof i.onclick);\n"
            + "    log(i.custom1);\n"
            + "    log(i.custom2);\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"a", "b", "undefined", "foo"})
    public void setAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var myNode = document.getElementById('myNode');\n"
            + "    log(myNode.title);\n"
            + "    myNode.setAttribute('title', 'b');\n"
            + "    log(myNode.title);\n"
            + "    log(myNode.Title);\n"
            + "    myNode.Title = 'foo';\n"
            + "    log(myNode.Title);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode' title='a'>\n"
            + "</p>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Caution: with IE if you get a node with some lowercase letters, the node will be retrieved
     * and will get as name the value passed as attribute to getAttributeNode.
     * The consequence for IE: x.getAttributeNode("Foo").nodeName != x.getAttributeNode("foo").nodeName
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"null",
             "expando=undefined",
             "firstChild=null",
             "lastChild=null",
             "name=custom_attribute",
             "nextSibling=null",
             "nodeName=custom_attribute",
             "nodeType=2",
             "nodeValue=bleh",
             "(ownerDocument === document) = true",
             "(getRootNode() === att) = true",
             "parentNode=null",
             "previousSibling=null",
             "specified=true",
             "value=bleh"})
    public void getAttributeNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var div = document.getElementById('div2');\n"
            + "      log(div.getAttributeNode('notExisting'));\n"
            + "      var customAtt = div.getAttributeNode('custom_attribute');\n"
            + "      alertAttributeProperties(customAtt);\n"
            + "    }\n"
            + "    function alertAttributeProperties(att) {\n"
            + "      log('expando=' + att.expando);\n"
            + "      log('firstChild=' + att.firstChild);\n"
            + "      log('lastChild=' + att.lastChild);\n"
            + "      log('name=' + att.name);\n"
            + "      log('nextSibling=' + att.nextSibling);\n"
            + "      log('nodeName=' + att.nodeName);\n"
            + "      log('nodeType=' + att.nodeType);\n"
            + "      log('nodeValue=' + att.nodeValue);\n"
            + "      log('(ownerDocument === document) = ' + (att.ownerDocument === document));\n"
            + "      if(att.getRootNode) {\n"
            + "        log('(getRootNode() === att) = ' + (att.getRootNode() === att));\n"
            + "      } else log('-');\n"
            + "      log('parentNode=' + att.parentNode);\n"
            + "      log('previousSibling=' + att.previousSibling);\n"
            + "      log('specified=' + att.specified);\n"
            + "      log('value=' + att.value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'></div>\n"
            + "  <div id='div2' name='blah' custom_attribute='bleh'></div>\n"
            + "  <div id='div3'></div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests setAttribute() with name of event handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onfocus1-onclick1-", "onfocus1-onclick1-onblur1-onfocus2-"})
    public void setAttribute_eventHandler() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title></title><script>\n"
            + "  function inform(msg) {\n"
            + "    document.title += msg;\n"
            + "    document.title += '-';\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('login');\n"
            + "    var password = document.getElementById('password');\n"

            + "    text.setAttribute('onclick', \"inform('onclick1');\");\n"
            + "    text.setAttribute('onFocus', \"inform('onfocus1');\");\n"
            + "    text.setAttribute('ONBLUR', \"inform('onblur1');\");\n"

            + "    password.setAttribute('onfocus', \"inform('onfocus2');\");\n"
            + "    password.setAttribute('onblur', \"inform('onblur2');\");\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input type='text' id='login' name='login'>\n"
            + "    <input type='password' id='password' name='password'>\n"
            + "  </form>\n"
            + "</body></html>";

        final String[] alerts = getExpectedAlerts();
        int i = 0;

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("login")).click();
        assertTitle(driver, alerts[i++]);

        driver.findElement(By.id("password")).click();
        assertTitle(driver, alerts[i++]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "inform('newHandler')", "null"})
    public void setAttribute_eventHandlerNull() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function inform(msg) {\n"
            + "    document.title += msg;\n"
            + "    document.title += '-';\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('login');\n"
            + "    var password = document.getElementById('password');\n"

            + "    log(text.getAttribute('onclick'));\n"
            + "    text.setAttribute('onclick', \"inform('newHandler')\");\n"
            + "    log(text.getAttribute('onclick'));\n"

            + "    text.setAttribute('onclick', null);\n"
            + "    log(text.getAttribute('onclick'));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input type='text' id='login' name='login'>\n"
            + "  </form>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTextArea2(html);

        driver.findElement(By.id("login")).click();
        assertTitle(driver, "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "inform('newHandler')", ""})
    public void setAttribute_eventHandlerEmptyString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function inform(msg) {\n"
            + "    document.title += msg;\n"
            + "    document.title += '-';\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('login');\n"
            + "    var password = document.getElementById('password');\n"

            + "    log(text.getAttribute('onclick'));\n"
            + "    text.setAttribute('onclick', \"inform('newHandler')\");\n"
            + "    log(text.getAttribute('onclick'));\n"

            + "    text.setAttribute('onclick', '');\n"
            + "    log(text.getAttribute('onclick'));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input type='text' id='login' name='login'>\n"
            + "  </form>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTextArea2(html);

        driver.findElement(By.id("login")).click();
        assertTitle(driver, "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "inform('newHandler')", "undefined"})
    public void setAttribute_eventHandlerUndefined() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function inform(msg) {\n"
            + "    document.title += msg;\n"
            + "    document.title += '-';\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('login');\n"
            + "    var password = document.getElementById('password');\n"

            + "    log(text.getAttribute('onclick'));\n"
            + "    text.setAttribute('onclick', \"inform('newHandler')\");\n"
            + "    log(text.getAttribute('onclick'));\n"

            + "    text.setAttribute('onclick', undefined);\n"
            + "    log(text.getAttribute('onclick'));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input type='text' id='login' name='login'>\n"
            + "  </form>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTextArea2(html);

        driver.findElement(By.id("login")).click();
        assertTitle(driver, "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus-click-", "focus-click-blur-"})
    public void setAttribute_eventHandlerEventArgument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title></title><script>\n"
            + "  function inform(msg) {\n"
            + "    document.title += msg;\n"
            + "    document.title += '-';\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('login');\n"

            + "    text.setAttribute('onclick', 'inform(event.type);');\n"
            + "    text.setAttribute('onFocus', 'inform(event.type);');\n"
            + "    text.setAttribute('ONBLUR', 'inform(event.type);');\n"

            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input type='text' id='login' name='login'>\n"
            + "    <input type='password' id='password' name='password'>\n"
            + "  </form>\n"
            + "</body></html>";

        final String[] alerts = getExpectedAlerts();
        int i = 0;

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("login")).click();
        assertTitle(driver, alerts[i++]);

        driver.findElement(By.id("password")).click();
        assertTitle(driver, alerts[i++]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inform(\"onclick\")", "inform('newHandler')", "newHandler"})
    public void getAttribute_eventHandler() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text = document.getElementById('login');\n"

            + "    log(text.getAttribute('onclick'));\n"
            + "    text.setAttribute('onclick', \"inform('newHandler')\");\n"
            + "    log(text.getAttribute('onclick'));\n"
            + "  }\n"
            + "  function inform(msg) {\n"
            + "    log(msg);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input type='text' id='login' name='login' onclick='inform(\"onclick\")'>\n"
            + "  </form>\n"
            + "</body></html>";

        final String[] alerts = getExpectedAlerts();

        final WebDriver webDriver = loadPage2(html);
        verifyTitle2(webDriver, alerts[0], alerts[1]);

        webDriver.findElement(By.id("login")).click();
        verifyTitle2(webDriver, alerts);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"left", "left", "right", "right"})
    public void setAttributeNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      // Get the old alignment.\n"
            + "      var div1 = document.getElementById('div1');\n"
            + "      var a1 = div1.getAttributeNode('align');\n"
            + "      log(a1.value);\n"
            + "      // Set the new alignment.\n"
            + "      var a2 = document.createAttribute('align');\n"
            + "      a2.value = 'right';\n"
            + "      a1 = div1.setAttributeNode(a2);\n"
            + "      log(a1.value);\n"
            + "      log(div1.getAttributeNode('align').value);\n"
            + "      log(div1.getAttribute('align'));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' align='left'></div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for <tt>getElementsByTagName</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"all = 4", "row = 2", "by wrong name: 0"})
    public void getElementsByTagName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var a1 = document.getElementsByTagName('td');\n"
            + "  log('all = ' + a1.length);\n"
            + "  var firstRow = document.getElementById('r1');\n"
            + "  var rowOnly = firstRow.getElementsByTagName('td');\n"
            + "  log('row = ' + rowOnly.length);\n"
            + "  log('by wrong name: ' + firstRow.getElementsByTagName('>').length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<table>\n"
            + "<tr id='r1'><td>1</td><td>2</td></tr>\n"
            + "<tr id='r2'><td>3</td><td>4</td></tr>\n"
            + "</table>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"div1", "div2"})
    public void getElementsByTagName2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    for (var f = 0; (formnode = document.getElementsByTagName('form').item(f)); f++)\n"
            + "      for (var i = 0; (node = formnode.getElementsByTagName('div').item(i)); i++)\n"
            + "        log(node.id);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form>\n"
            + "    <div id='div1'/>\n"
            + "    <div id='div2'/>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test that {@link HTMLElement#getElementsByTagName} returns an associative array.
     * Test for Bug #321.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "second", "third"})
    public void getElementsByTagNameCollection() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var form1 = document.getElementById('form1');\n"
            + "  var elements = form1.getElementsByTagName('input');\n"
            + "  log(elements['one'].name);\n"
            + "  log(elements['two'].name);\n"
            + "  log(elements['three'].name);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<form id='form1'>\n"
            + "<input id='one' name='first' type='text'>\n"
            + "<input id='two' name='second' type='text'>\n"
            + "<input id='three' name='third' type='text'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests that getElementsByTagName('*') returns all child elements, both
     * at the document level and at the element level.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"8", "3"})
    public void getElementsByTagNameAsterisk() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementsByTagName('*').length);\n"
            + "    log(document.getElementById('div').getElementsByTagName('*').length);\n"
            + "  }\n"
            + "</script>\n"
            + "<div id='div'><p>a</p><p>b</p><p>c</p></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true", "false", "false"})
    public void getElementsByTagNameEquality() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'><script>\n"
            + LOG_TITLE_FUNCTION
            + "var div = document.getElementById('d');\n"
            + "log(document.getElementsByTagName('*') == document.getElementsByTagName('*'));\n"
            + "log(document.getElementsByTagName('script') == document.getElementsByTagName('script'));\n"
            + "log(document.getElementsByTagName('foo') == document.getElementsByTagName('foo'));\n"
            + "log(document.getElementsByTagName('script') == document.getElementsByTagName('body'));\n"
            + "log(document.getElementsByTagName('script') == div.getElementsByTagName('script'));\n"
            + "</script></div></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test getting the class for the element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("the class is x")
    public void getClassName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><style>.x { font: 8pt Arial bold; }</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var ele = document.getElementById('pid');\n"
            + "  var aClass = ele.className;\n"
            + "  log('the class is ' + aClass);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test getting the class for the element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"* x # x *", "*\tx\t#\tx\t*", "*  #  *", "*\t\t#\t\t*", "*\t \n \n#\t \n \n*", "*x\ty#x\ty*"})
    public void getClassNameWhitespace() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function doTest() {\n"
            + "  var elem = document.getElementById('pid1');\n"
            + "  log('*' + elem.className + '#' + elem.getAttribute('class') + '*');\n"
            + "  elem = document.getElementById('pid2');\n"
            + "  log('*' + elem.className + '#' + elem.getAttribute('class') + '*');\n"
            + "  elem = document.getElementById('pid3');\n"
            + "  log('*' + elem.className + '#' + elem.getAttribute('class') + '*');\n"
            + "  elem = document.getElementById('pid4');\n"
            + "  log('*' + elem.className + '#' + elem.getAttribute('class') + '*');\n"
            + "  elem = document.getElementById('pid5');\n"
            + "  log('*' + elem.className + '#' + elem.getAttribute('class') + '*');\n"
            + "  elem = document.getElementById('pid6');\n"
            + "  log('*' + elem.className + '#' + elem.getAttribute('class') + '*');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <p id='pid1' class=' x '>text</p>\n"
            + "  <p id='pid2' class='\tx\t'>text</p>\n"
            + "  <p id='pid3' class='  '>text</p>\n"
            + "  <p id='pid4' class='\t\t'>text</p>\n"
            + "  <p id='pid5' class='\t \r \n'>text</p>\n"
            + "  <p id='pid6' class='x\ty'>text</p>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting the class for the element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("the class is z")
    public void setClassName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><style>.x { font: 8pt Arial bold; }</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var ele = document.getElementById('pid');\n"
            + "  ele.className = 'z';\n"
            + "  var aClass = ele.className;\n"
            + "  log('the class is ' + aClass);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("if (1 > 2 & 3 < 2) willNotHappen('yo');")
    public void getInnerHTML() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script id='theScript'>if (1 > 2 & 3 < 2) willNotHappen('yo');</script>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var myNode = document.getElementById('theScript');\n"
            + "    log(myNode.innerHTML);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<form id='myNode'></form>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("getInnerHTML() not available")
    public void getGetInnerHTML() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var myNode = document.getElementById('myNode');\n"
            + "    if (myNode.getInnerHTML) {\n"
            + "      log(myNode.getInnerHTML());\n"
            + "    } else {\n"
            + "      log('getInnerHTML() not available');\n"
            + "    }\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body>\n"
            + "<p id='myNode'><b>inner HTML</b></p>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("<div id=\"i\" foo=\"\" name=\"\"></div>")
    public void getInnerHTML_EmptyAttributes() throws Exception {
        final String html = "<body onload='alert(document.body.innerHTML)'><div id='i' foo='' name=''></div></body>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old#=#<b>Old#innerHTML</b>", "New#=#New##cell#value"})
    public void getSetInnerHTMLSimple_FF() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "function log(msg) { window.document.title += msg.replace(/\\s/g, '#') + '§';}\n"
            + "  function doTest() {\n"
            + "    var myNode = document.getElementById('myNode');\n"
            + "    log('Old = ' + myNode.innerHTML);\n"
            + "    myNode.innerHTML = 'New  cell value';\n"
            + "    log('New = ' + myNode.innerHTML);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode'><b>Old innerHTML</b></p>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test the use of innerHTML to set a new input.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getSetInnerHTMLNewInput() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var myNode = document.getElementById('myNode');\n"
            + "    myNode.innerHTML = '<input type=\"checkbox\" name=\"myCb\" checked>';\n"
            + "    log(myNode.myCb.checked);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<form id='myNode'></form>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old#=#<b>Old#innerHTML</b>",
             "New#=#New##cell#value#&amp;#\u0110#\u0110"})
    public void getSetInnerHTMLChar_FF() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "function log(msg) { window.document.title += msg.replace(/\\s/g, '#') + '§';}\n"
            + "  function doTest() {\n"
            + "    var myNode = document.getElementById('myNode');\n"
            + "    log('Old = ' + myNode.innerHTML);\n"
            + "    myNode.innerHTML = 'New  cell value &amp; \\u0110 &#272;';\n"
            + "    log('New = ' + myNode.innerHTML);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode'><b>Old innerHTML</b></p>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setInnerHTMLExecuteJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = '<scr'+'ipt>alerter();</scr'+'ipt>';\n"
            + "    var outernode = document.getElementById('myNode');\n"
            + "    outernode.innerHTML = newnode;\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setInnerHTMLExecuteNestedJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "  function test() {\n"
            + LOG_TITLE_FUNCTION
            + "    var newnode = '<div><scr'+'ipt>alerter();</scr'+'ipt></div>';\n"
            + "    var outernode = document.getElementById('myNode');\n"
            + "    outernode.innerHTML = newnode;\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ReferenceError")
    public void setInnerHTMLDeclareJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = '<scr'+'ipt>function tester() { alerter(); }</scr'+'ipt>';\n"
            + "    var outernode = document.getElementById('myNode');\n"
            + "    outernode.innerHTML = newnode;\n"
            + "    try {\n"
            + "      tester();\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('declared');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies outerHTML, innerHTML and innerText for newly created div.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void outerHTMLinNewDiv() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    log('outerHTML' in div);\n"
            + "    log('innerHTML' in div);\n"
            + "    log('innerText' in div);\n"
            + "  }\n"
            + "</script>\n"
            + "<div id='div'><span class='a b'></span></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that empty tags are not abbreviated into their &lt;tag/&gt; form.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div id=\"div\"><ul></ul></div>", "<ul></ul>", ""})
    public void getSetInnerHtmlEmptyTag_FF() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('div');\n"
            + "    log(div.outerHTML);\n"
            + "    log(div.innerHTML);\n"
            + "    log(div.innerText);\n"
            + "  }\n"
            + "</script>\n"
            + "<div id='div'><ul/></div>"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that attributes containing whitespace are always quoted.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div id=\"div\"><span class=\"a b\"></span></div>", "<span class=\"a b\"></span>", ""})
    public void getSetInnerHtmlAttributeWithWhitespace_FF() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('div');\n"
            + "    log(div.outerHTML);\n"
            + "    log(div.innerHTML);\n"
            + "    log(div.innerText);\n"
            + "  }\n"
            + "</script>\n"
            + "<div id='div'><span class='a b'></span></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test setting innerHTML to empty string.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Empty ChildrenLength: 0")
    public void setInnerHTMLEmpty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head><body>\n"
                + "<div id='testDiv'>foo</div>\n"
                + "<script language='javascript'>\n"
                + LOG_TITLE_FUNCTION
                + "    var node = document.getElementById('testDiv');\n"
                + "    node.innerHTML = '';\n"
                + "    log('Empty ChildrenLength: ' + node.childNodes.length);\n"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test setting innerHTML to null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Null ChildrenLength: 0")
    public void setInnerHTMLNull() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head><body>\n"
                + "<div id='testDiv'>foo</div>\n"
                + "<script language='javascript'>\n"
                + LOG_TITLE_FUNCTION
                + "    var node = document.getElementById('testDiv');\n"
                + "    node.innerHTML = null;\n"
                + "    log('Null ChildrenLength: ' + node.childNodes.length);\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test setting innerHTML should reset style cache.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void setInnerHTMLResetsStyle() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<div id='testDiv'></div>\n"
                + "<script language='javascript'>\n"
                + LOG_TITLE_FUNCTION
                + "    var node = document.getElementById('testDiv');\n"
                + "    var height = node.offsetHeight;\n"
                + "    node.innerHTML = 'HtmlUnit';\n"
                + "    log(height < node.offsetHeight);\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test getting <code>outerHTML</code> of a <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Outer = <div id=\"myNode\">New  cell value</div>")
    public void getOuterHTMLFromBlock() throws Exception {
        final String html = createPageForGetOuterHTML("div", "New  cell value", false);
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test getting <code>outerHTML</code> of a <code>span</code> (inline).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Outer = <span id=\"myNode\">New  cell value</span>")
    public void getOuterHTMLFromInline() throws Exception {
        final String html = createPageForGetOuterHTML("span", "New  cell value", false);
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test getting <code>outerHTML</code> of a <code>br</code> (empty).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Outer = <br id=\"myNode\">")
    public void getOuterHTMLFromEmpty() throws Exception {
        final String html = createPageForGetOuterHTML("br", "", true);
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test getting <code>outerHTML</code> of an unclosed <code>p</code>.<br>
     * Closing <code>p</code> is optional.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Outer = <p id=\"myNode\">New  cell value\n"
                    + "  <textarea id=\"myLog\" cols=\"80\" rows=\"22\"></textarea>\n\n</p>")
    @HtmlUnitNYI(CHROME = "Outer = <p id=\"myNode\">New  cell value\n"
                            + "  <textarea id=\"myLog\" cols=\"80\" rows=\"22\"></textarea>\n</p>",
            EDGE = "Outer = <p id=\"myNode\">New  cell value\n"
                    + "  <textarea id=\"myLog\" cols=\"80\" rows=\"22\"></textarea>\n</p>",
            FF = "Outer = <p id=\"myNode\">New  cell value\n"
                    + "  <textarea id=\"myLog\" cols=\"80\" rows=\"22\"></textarea>\n</p>",
            FF_ESR = "Outer = <p id=\"myNode\">New  cell value\n"
                    + "  <textarea id=\"myLog\" cols=\"80\" rows=\"22\"></textarea>\n</p>")
    public void getOuterHTMLFromUnclosedParagraph() throws Exception {
        final String html = createPageForGetOuterHTML("p", "New  cell value", true);
        loadPageVerifyTextArea2(html);
    }

    private static String createPageForGetOuterHTML(final String nodeTag, final String value, final boolean unclosed) {
        return DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TEXTAREA_FUNCTION
                + "  function doTest() {\n"
                + "    var myNode = document.getElementById('myNode');\n"
                + "    log('Outer = ' + myNode.outerHTML);\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "  <" + nodeTag + " id='myNode'>" + value + (unclosed ? "" : "</" + nodeTag + ">") + "\n"
                + LOG_TEXTAREA
                + "</body>\n"
                + "</html>";
    }

    /**
     * Test setting outerHTML to null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = ", "Children: 0"})
    public void setOuterHTMLNull() throws Exception {
        final String html = createPageForSetOuterHTML("div", null);
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting outerHTML to null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = undefined", "Children: 1"})
    public void setOuterHTMLUndefined() throws Exception {
        final String html = createPageForSetOuterHTML("div", "undefined");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting outerHTML to ''.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = ", "Children: 0"})
    public void setOuterHTMLEmpty() throws Exception {
        final String html = createPageForSetOuterHTML("div", "");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting outerHTML to ''.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New =   ", "Children: 1"})
    public void setOuterHTMLBlank() throws Exception {
        final String html = createPageForSetOuterHTML("div", "  ");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>div</code> (block) to a text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = New  cell value", "Children: 1"})
    public void setOuterHTMLAddTextToBlock() throws Exception {
        final String html = createPageForSetOuterHTML("div", "New  cell value");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>span</code> (inline) to a text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = New  cell value", "Children: 1"})
    public void setOuterHTMLAddTextToInline() throws Exception {
        final String html = createPageForSetOuterHTML("span", "New  cell value");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>div</code> (block) to a <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <div>test</div>", "Children: 1"})
    public void setOuterHTMLAddBlockToBlock() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<div>test</div>");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>span</code> (inline) to a <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <div>test</div>", "Children: 1"})
    public void setOuterHTMLAddBlockToInline() throws Exception {
        final String html = createPageForSetOuterHTML("span", "<div>test</div>");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>span</code> (inline) to a <code>span</code> (inline).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <span>test</span>", "Children: 1"})
    public void setOuterHTMLAddInlineToInline() throws Exception {
        final String html = createPageForSetOuterHTML("span", "<span>test</span>");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>div</code> (block) to a <code>span</code> (inline).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <span>test</span>", "Children: 1"})
    public void setOuterHTMLAddInlineToBlock() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<span>test</span>");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to a <code>br</code> (empty).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <br>", "Children: 1"})
    public void setOuterHTMLAddEmpty() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<br>");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to <code>tr</code> (read-only).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-0", "1", "2", "3", "-4", "5", "6", "7", "8", "9", "10", "11"})
    public void setOuterHTMLToReadOnly() throws Exception {
        final String html =  DOCTYPE_HTML
            + "<html>\n"
            + "<head>n"
            + "  <script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "  function doTest() {\n"
            + "    var nodeTypes = ['body', 'caption', 'col', 'colgroup', 'head', 'html',\n"
            + "                     'tbody', 'td', 'tfoot', 'th', 'thead', 'tr'];\n"
            + "    for (var i = 0; i < nodeTypes.length; i++) {\n"
            + "      var nodeType = nodeTypes[i];\n"
            + "      var myNode = document.getElementsByTagName(nodeType)[0];\n"
            + "      try {\n"
            + "        myNode.outerHTML = 'test';\n"
            + "        log('-' + i);\n"
            + "      } catch(e) { log(i); }\n"
            + "    }\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <table>\n"
            + "    <caption></caption>\n"
            + "    <colgroup><col></colgroup>\n"
            + "    <thead><tr><td></td><th></th></tr></thead>\n"
            + "    <tbody></tbody>\n"
            + "    <tfoot></tfoot>\n"
            + "  </table>\n"
            + "  </table>\n"
            + "</body>\n"
            + "</html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>p</code> to a <code>div</code> (block).<br>
     * <code>p</code> allows no block elements inside.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>",
             "New = <div>test</div>", "Children: 1"})
    public void setOuterHTMLAddBlockToParagraph() throws Exception {
        final String html = createPageForSetOuterHTML("p", "<div>test</div>");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>p</code> to a <code>p</code>.<br>
     * A following <code>p</code> closes the one before.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>",
             "New = <p>test</p>", "Children: 1"})
    public void setOuterHTMLAddParagraphToParagraph() throws Exception {
        final String html = createPageForSetOuterHTML("p", "<p>test</p>");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to an unclosed <code>p</code>.<br>
     * Closing <code>p</code> is optional.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <p>test</p>", "Children: 1"})
    public void setOuterHTMLAddUnclosedParagraph() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<p>test");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of an <code>a</code> to an <code>a</code>.<br>
     * <code>a</code> allows no <code>a</code> inside.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>",
             "New = <a>test</a>", "Children: 1"})
    public void setOuterHTMLAddAnchorToAnchor() throws Exception {
        final String html = createPageForSetOuterHTML("a", "<a>test</a>");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to an XHTML self-closing <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <div></div>", "Children: 1"})
    public void setOuterHTMLAddSelfClosingBlock() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<div/>");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to two XHTML self-closing <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>",
             "New = <div><div></div></div>", "Children: 1"})
    @HtmlUnitNYI(CHROME = {"Old = <span id=\"innerNode\">Old outerHTML</span>",
                           "New = <div></div><div></div>", "Children: 2"},
            EDGE = {"Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <div></div><div></div>", "Children: 2"},
            FF = {"Old = <span id=\"innerNode\">Old outerHTML</span>",
                  "New = <div></div><div></div>", "Children: 2"},
            FF_ESR = {"Old = <span id=\"innerNode\">Old outerHTML</span>",
                      "New = <div></div><div></div>", "Children: 2"})
    public void setOuterHTMLAddMultipleSelfClosingBlock() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<div/><div>");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to an XHTML self-closing <code>span</code> (inline).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <span></span>", "Children: 1"})
    public void setOuterHTMLAddSelfClosingInline() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<span/>");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to an XHTML self-closing <code>br</code> (empty).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <br>", "Children: 1"})
    public void setOuterHTMLAddSelfClosingEmpty() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<br/>");
        loadPageVerifyTextArea2(html);
    }

    private static String createPageForSetOuterHTML(final String nodeTag, final String newValue) {
        String newVal = "null";
        if ("undefined".equals(newValue)) {
            newVal = "undefined";
        }
        else if (newValue != null) {
            newVal = "'" + newValue + "'";
        }
        return DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function doTest() {\n"
            + "    var myNode = document.getElementById('myNode');\n"
            + "    var innerNode = document.getElementById('innerNode');\n"
            + "    log('Old = ' + myNode.innerHTML);\n"
            + "    innerNode.outerHTML = " + newVal + ";\n"
            + "    log('New = ' + myNode.innerHTML);\n"
            + "    log('Children: ' + myNode.childNodes.length);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <" + nodeTag + " id='myNode'><span id='innerNode'>Old outerHTML</span></" + nodeTag + ">\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>";
    }

    /**
     * Test setting <code>outerHTML</code> to two XHTML self-closing <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"Old = <span id=\"innerNode\">Old outerHTML</span>",
                       "New = <span id=\"innerNode\">Old outerHTML</span>", "Children: 1"},
            CHROME = {"Old = <span id=\"innerNode\">Old outerHTML</span>", "NoModificationAllowedError/DOMException"},
            EDGE = {"Old = <span id=\"innerNode\">Old outerHTML</span>", "NoModificationAllowedError/DOMException"})
    public void setOuterHTMLDetachedElementNull() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function doTest() {\n"
                + "    var myNode = document.getElementById('myNode');\n"
                + "    document.body.removeChild(myNode);\n"
                + "    log('Old = ' + myNode.innerHTML);\n"
                + "    try {\n"
                + "      myNode.outerHTML = null;\n"
                + "      log('New = ' + myNode.innerHTML);\n"
                + "      log('Children: ' + myNode.childNodes.length);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "  <div id='myNode'><span id='innerNode'>Old outerHTML</span></div>\n"
                + "</body>\n"
                + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to two XHTML self-closing <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"Old = <span id=\"innerNode\">Old outerHTML</span>",
                       "New = <span id=\"innerNode\">Old outerHTML</span>", "Children: 1"},
            CHROME = {"Old = <span id=\"innerNode\">Old outerHTML</span>", "NoModificationAllowedError/DOMException"},
            EDGE = {"Old = <span id=\"innerNode\">Old outerHTML</span>", "NoModificationAllowedError/DOMException"})
    public void setOuterHTMLDetachedElementUndefined() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function doTest() {\n"
                + "    var myNode = document.getElementById('myNode');\n"
                + "    document.body.removeChild(myNode);\n"
                + "    log('Old = ' + myNode.innerHTML);\n"
                + "    try {\n"
                + "      myNode.outerHTML = undefined;\n"
                + "      log('New = ' + myNode.innerHTML);\n"
                + "      log('Children: ' + myNode.childNodes.length);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "  <div id='myNode'><span id='innerNode'>Old outerHTML</span></div>\n"
                + "</body>\n"
                + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to two XHTML self-closing <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"Old = <span id=\"innerNode\">Old outerHTML</span>",
                       "New = <span id=\"innerNode\">Old outerHTML</span>", "Children: 1"},
            CHROME = {"Old = <span id=\"innerNode\">Old outerHTML</span>", "exception"},
            EDGE = {"Old = <span id=\"innerNode\">Old outerHTML</span>", "exception"})
    public void setOuterHTMLDetachedElementEmpty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function doTest() {\n"
                + "    var myNode = document.getElementById('myNode');\n"
                + "    document.body.removeChild(myNode);\n"
                + "    log('Old = ' + myNode.innerHTML);\n"
                + "    try {\n"
                + "      myNode.outerHTML = '';\n"
                + "      log('New = ' + myNode.innerHTML);\n"
                + "      log('Children: ' + myNode.childNodes.length);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "  <div id='myNode'><span id='innerNode'>Old outerHTML</span></div>\n"
                + "</body>\n"
                + "</html>";
        loadPage2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to two XHTML self-closing <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"Old = <span id=\"innerNode\">Old outerHTML</span>",
                       "New = <span id=\"innerNode\">Old outerHTML</span>", "Children: 1"},
            CHROME = {"Old = <span id=\"innerNode\">Old outerHTML</span>", "NoModificationAllowedError/DOMException"},
            EDGE = {"Old = <span id=\"innerNode\">Old outerHTML</span>", "NoModificationAllowedError/DOMException"})
    public void setOuterHTMLDetachedElementBlank() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function doTest() {\n"
                + "    var myNode = document.getElementById('myNode');\n"
                + "    document.body.removeChild(myNode);\n"
                + "    log('Old = ' + myNode.innerHTML);\n"
                + "    try {\n"
                + "      myNode.outerHTML = '';\n"
                + "      log('New = ' + myNode.innerHTML);\n"
                + "      log('Children: ' + myNode.childNodes.length);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "  <div id='myNode'><span id='innerNode'>Old outerHTML</span></div>\n"
                + "</body>\n"
                + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to two XHTML self-closing <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"Old = <span id=\"innerNode\">Old outerHTML</span>",
                       "New = <span id=\"innerNode\">Old outerHTML</span>", "Children: 1"},
            CHROME = {"Old = <span id=\"innerNode\">Old outerHTML</span>", "NoModificationAllowedError/DOMException"},
            EDGE = {"Old = <span id=\"innerNode\">Old outerHTML</span>", "NoModificationAllowedError/DOMException"})
    public void setOuterHTMLDetachedElement() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function doTest() {\n"
                + "    var myNode = document.getElementById('myNode');\n"
                + "    document.body.removeChild(myNode);\n"
                + "    log('Old = ' + myNode.innerHTML);\n"
                + "    try {\n"
                + "      myNode.outerHTML = '<p>test</p>';\n"
                + "      log('New = ' + myNode.innerHTML);\n"
                + "      log('Children: ' + myNode.childNodes.length);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "  <div id='myNode'><span id='innerNode'>Old outerHTML</span></div>\n"
                + "</body>\n"
                + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOuterHTMLExecuteJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = '<scr'+'ipt>alerter();</scr'+'ipt>';\n"
            + "    var oldnode = document.getElementById('myNode');\n"
            + "    oldnode.outerHTML = newnode;\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOuterHTMLExecuteNestedJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = '<div><scr'+'ipt>alerter();</scr'+'ipt></div>';\n"
            + "    var oldnode = document.getElementById('myNode');\n"
            + "    oldnode.outerHTML = newnode;\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ReferenceError")
    public void setOuterHTMLDeclareJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = '<scr'+'ipt>function tester() { alerter(); }</scr'+'ipt>';\n"
            + "    var oldnode = document.getElementById('myNode');\n"
            + "    oldnode.outerHTML = newnode;\n"
            + "    try {\n"
            + "      tester();\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('declared');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test the <tt>#default#clientCaps</tt> default IE behavior.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body.cpuClass = undefined", "TypeError"})
    public void addBehaviorDefaultClientCaps() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var body = document.body;\n"
            + "  log('body.cpuClass = ' + body.cpuClass);\n"
            + "  var id = body.addBehavior('#default#clientCaps');\n"
            + "  log('body.cpuClass = ' + body.cpuClass);\n"
            + "  var id2 = body.addBehavior('#default#clientCaps');\n"
            + "  body.removeBehavior(id);\n"
            + "  log('body.cpuClass = ' + body.cpuClass);\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test the removal of behaviors.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body.isHomePage = undefined", "!addBehavior", "!removeBehavior", "TypeError"})
    public void removeBehavior() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var body = document.body;\n"
            + "  log('body.isHomePage = ' + body.isHomePage);\n"

            + "  if(!body.addBehavior) { log('!addBehavior'); }\n"
            + "  if(!body.removeBehavior) { log('!removeBehavior'); }\n"

            + "  var id = body.addBehavior('#default#homePage');\n"
            + "  log('body.isHomePage = ' + body.isHomePage('not the home page'));\n"
            + "  body.removeBehavior(id);\n"
            + "  log('body.isHomePage = ' + body.isHomePage);\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"BR", "DIV", "2", "3"})
    public void children() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<div id='myDiv'><br/><div><span>test</span></div></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var oDiv = document.getElementById('myDiv');\n"
            + "  for (var i = 0; i < oDiv.children.length; i++) {\n"
            + "    log(oDiv.children[i].tagName);\n"
            + "  }\n"
            + "  var oCol = oDiv.children;\n"
            + "  log(oCol.length);\n"
            + "  oDiv.insertAdjacentHTML('beforeEnd', '<br>');\n"
            + "  log(oCol.length);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0"})
    public void childrenDoesNotCountTextNodes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  children = document.getElementById('myBody').children;\n"
            + "  log(children.length);\n"

            + "  children = document.getElementById('myId').children;\n"
            + "  log(children.length);\n"
            + "}\n"
            + "</script></head><body id='myBody' onload='test()'>\n"
            + "  <div id='myId'>abcd</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "TypeError"})
    public void childrenFunctionAccess() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<div id='myDiv'><br/><div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var oDiv = document.getElementById('myDiv');\n"
            + "  log(oDiv.children.length);\n"
            + "  log(oDiv.children(0).tagName);\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = Old\n\ninnerText", "New = New cell value"})
    @HtmlUnitNYI(CHROME = {"Old = Old\ninnerText", "New = New cell value"},
            EDGE =  {"Old = Old\ninnerText", "New = New cell value"},
            FF = {"Old = Old\ninnerText", "New = New cell value"},
            FF_ESR = {"Old = Old\ninnerText", "New = New cell value"})
    public void getSetInnerTextSimple() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function doTest() {\n"
            + "    var myNode = document.getElementById('myNode');\n"
            + "    log('Old = ' + myNode.innerText);\n"
            + "    myNode.innerText = 'New cell value';\n"
            + "    log('New = ' + myNode.innerText);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='myNode'><b>Old <p>innerText</p></b></div>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * Test the removal of attributes from HTMLElements.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"removeMe", "null"})
    public void removeAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var myDiv = document.getElementById('aDiv');\n"
            + "    log(myDiv.getAttribute('name'));\n"
            + "    myDiv.removeAttribute('name');\n"
            + "    log(myDiv.getAttribute('name'));\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'><div id='aDiv' name='removeMe'>\n"
            + "</div></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * IE doesn't really make a distinction between property and attribute...
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"hello", "null", "hello"})
    public void removeAttribute_property() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var myDiv = document.getElementById('aDiv');\n"
            + "    myDiv.foo = 'hello';\n"
            + "    log(myDiv.foo);\n"
            + "    log(myDiv.getAttribute('foo'));\n"
            + "    myDiv.removeAttribute('foo');\n"
            + "    log(myDiv.foo);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'><div id='aDiv' name='removeMe'>\n"
            + "</div></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test scrolls (real values don't matter currently).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"number", "number", "number", "number", "number", "number", "number", "number"})
    public void scrolls() throws Exception {
        final String html = DOCTYPE_HTML
              + "<html>\n"
              + "<body>\n"
              + "</div></body>\n"
              + "<div id='div1'>foo</div>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "function alertScrolls(_oElt) {\n"
              + "  log(typeof _oElt.scrollHeight);\n"
              + "  log(typeof _oElt.scrollWidth);\n"
              + "  log(typeof _oElt.scrollLeft);\n"
              + "  _oElt.scrollLeft = 123;\n"
              + "  log(typeof _oElt.scrollTop);\n"
              + "  _oElt.scrollTop = 123;\n"
              + "}\n"
              + "alertScrolls(document.body);\n"
              + "alertScrolls(document.getElementById('div1'));\n"
              + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0", "0", "0", "0", "17", "0", "0"})
    public void scrollLeft_overflowScroll() throws Exception {
        scrollLeft("scroll");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0", "0", "0", "0", "17", "0", "0"})
    public void scrollLeft_overflowAuto() throws Exception {
        scrollLeft("auto");
    }

    /**
     * NOTE: When running this test with Firefox (3.6, at least), it's important to reload the page with Ctrl+F5
     * in order to completely clear the cache; otherwise, Firefox appears to incorrectly cache some style attributes.
     * @throws Exception if an error occurs
     */
    private void scrollLeft(final String overflow) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'>\n"
            + "<div id='d1' style='width:100px;height:100px;background-color:green;'>\n"
            + "  <div id='d2' style='width:50px;height:50px;background-color:blue;'></div>\n"
            + "</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var d1 = document.getElementById('d1'), d2 = document.getElementById('d2');\n"
            + "  log(d1.scrollLeft);\n"
            + "  d1.scrollLeft = -1;\n"
            + "  log(d1.scrollLeft);\n"
            + "  d1.scrollLeft = 5;\n"
            + "  log(d1.scrollLeft);\n"
            + "  d2.style.width = '200px';\n"
            + "  d2.style.height = '200px';\n"
            + "  d1.scrollLeft = 7;\n"
            + "  log(d1.scrollLeft);\n"
            + "  d1.style.overflow = '" + overflow + "';\n"
            + "  log(d1.scrollLeft);\n"
            + "  d1.scrollLeft = 17;\n"
            + "  log(d1.scrollLeft);\n"
            + "  d1.style.overflow = 'visible';\n"
            + "  log(d1.scrollLeft);\n"
            + "  d1.scrollLeft = 19;\n"
            + "  log(d1.scrollLeft);\n"
            + "}\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "10", "0"})
    public void scrollLeft() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var outer = document.getElementById('outer');\n"
            + "    var inner = document.getElementById('inner');\n"
            + "    log(outer.scrollLeft);\n"

            + "    outer.scrollLeft = 10;\n"
            + "    log(outer.scrollLeft);\n"

            + "    outer.scrollLeft = -4;\n"
            + "    log(outer.scrollLeft);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <div id='outer' style='overflow: scroll; width: 100px'>\n"
            + "    <div id='inner' style='width: 250px'>abcdefg hijklmnop qrstuvw xyz</div>\n"
            + "  </div>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0", "0", "0", "0", "17", "0", "0"})
    public void scrollTop_overflowScroll() throws Exception {
        scrollTop("scroll");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0", "0", "0", "0", "17", "0", "0"})
    public void scrollTop_overflowAuto() throws Exception {
        scrollTop("auto");
    }

    /**
     * NOTE: When running this test with Firefox (3.6, at least), it's important to reload the page with Ctrl+F5
     * in order to completely clear the cache; otherwise, Firefox appears to incorrectly cache some style attributes.
     * @throws Exception if an error occurs
     */
    private void scrollTop(final String overflow) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'>\n"
            + "<div id='d1' style='width:100px;height:100px;background-color:green;'>\n"
            + "  <div id='d2' style='width:50px;height:50px;background-color:blue;'></div>\n"
            + "</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var d1 = document.getElementById('d1'), d2 = document.getElementById('d2');\n"
            + "  log(d1.scrollTop);\n"
            + "  d1.scrollTop = -1;\n"
            + "  log(d1.scrollTop);\n"
            + "  d1.scrollTop = 5;\n"
            + "  log(d1.scrollTop);\n"
            + "  d2.style.width = '200px';\n"
            + "  d2.style.height = '200px';\n"
            + "  d1.scrollTop = 7;\n"
            + "  log(d1.scrollTop);\n"
            + "  d1.style.overflow = '" + overflow + "';\n"
            + "  log(d1.scrollTop);\n"
            + "  d1.scrollTop = 17;\n"
            + "  log(d1.scrollTop);\n"
            + "  d1.style.overflow = 'visible';\n"
            + "  log(d1.scrollTop);\n"
            + "  d1.scrollTop = 19;\n"
            + "  log(d1.scrollTop);\n"
            + "}\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests that JavaScript scrollIntoView() function doesn't fail.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ok")
    public void scrollIntoView() throws Exception {
        final String html = DOCTYPE_HTML
              + "<html>\n"
              + "<body>\n"
              + "<script id='me'>\n"
              + LOG_TITLE_FUNCTION
              + "document.getElementById('me').scrollIntoView(); log('ok');</script>\n"
              + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("container [object HTMLDivElement]")
    @HtmlUnitNYI(CHROME = {"container [object HTMLDivElement]", "body [object HTMLBodyElement]"},
            EDGE = {"container [object HTMLDivElement]", "body [object HTMLBodyElement]"},
            FF = {"container [object HTMLDivElement]", "body [object HTMLBodyElement]"},
            FF_ESR = {"container [object HTMLDivElement]", "body [object HTMLBodyElement]"})
    public void scrollIntoViewTriggersOnScroll() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <div id='container' style='overflow-y: scroll; height: 100px;'>\n"
            + "    <div style='height: 1000px;'>spacer</div>\n"
            + "    <div id='target' style='background: red;'>Target</div>"
            + "  </div>\n"

            + "  <script>\n"
            + "    document.addEventListener('scroll', function(e) { log('document ' + e.target) });\n"
            + "    document.body.addEventListener('scroll', function(e) { log('body ' + e.target) });\n"

            + "    var c = document.getElementById('container');\n"
            + "    c.addEventListener('scroll', function(e) { log('container ' + e.target) });\n"

            + "    var s = document.getElementById('target');"
            + "    s.addEventListener('scroll', function(e) { log('target ' + e.target) });\n"

            + "    s.scrollIntoView();\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"target-1 [object HTMLDivElement]", "container [object HTMLDivElement]"})
    @HtmlUnitNYI(CHROME = {"target-1 [object HTMLDivElement]",
                           "container [object HTMLDivElement]", "body [object HTMLBodyElement]"},
            EDGE = {"target-1 [object HTMLDivElement]",
                    "container [object HTMLDivElement]", "body [object HTMLBodyElement]"},
            FF = {"target-1 [object HTMLDivElement]",
                  "container [object HTMLDivElement]", "body [object HTMLBodyElement]"},
            FF_ESR = {"target-1 [object HTMLDivElement]",
                      "container [object HTMLDivElement]", "body [object HTMLBodyElement]"})
    public void scrollIntoViewTriggersOnScrollBubbling() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <div id='container' style='overflow-y: scroll; height: 100px;'>\n"
            + "    <div style='height: 1000px;'>spacer</div>\n"
            + "    <div id='target-1' style='overflow-y: scroll; height: 100px;'>\n"
            + "      <div style='height: 1000px;'>spacer</div>\n"
            + "      <div id='target' style='background: red;'>Target</div>"
            + "    </div>\n"
            + "  </div>\n"

            + "  <script>\n"
            + "    document.addEventListener('scroll', function(e) { log('document ' + e.target) });\n"
            + "    document.body.addEventListener('scroll', function(e) { log('body ' + e.target) });\n"

            + "    var c = document.getElementById('container');\n"
            + "    c.onscroll = (e) => { log('container ' + e.target); };\n"

            + "    var t = document.getElementById('target-1');\n"
            + "    t.onscroll = (e) => { log('target-1 ' + e.target); };\n"

            + "    var s = document.getElementById('target');"
            + "    s.onscroll = (e) => { log('target ' + e.target); };\n"

            + "    s.scrollIntoView();\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("window [object HTMLDocument]")
    public void scrollIntoViewTriggersWindowOnScroll() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <div style='height: 10000px;'>spacer</div>\n"
            + "  <div id='target' style='background: red;'>Target</div>"

            + "  <script>\n"
            + "    window.addEventListener('scroll', function(e) { log('window ' + e.target) });\n"

            + "    var s = document.getElementById('target');"
            + "    s.scrollIntoView();\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for issue #942.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"50", "100"})
    public void scrollIntoViewIssue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <button id='myButton' onClick='tester();'>Press</button>\n"
            + "  <div id='table-container' style='max-height: 400px; overflow-y: auto;'>\n"
            + "    <table>\n"
            + "      <thead><tr><th>ID</th><th>Name</th></tr></thead>\n"
            + "      <tbody id='table-body'></tbody>\n"
            + "    </table>\n"
            + "  </div>\n"

            + "  <script>\n"
            + "    let counter = 1;\n"
            + "    const tableBody = document.getElementById('table-body');\n"
            + "    const container = document.getElementById('table-container');\n"

            + "    function tester() {\n"
            + "      const xPathLast = \"//tbody[@id='table-body']//tr[last()]\";\n"
            + "      const last = document.evaluate(xPathLast, document, null, "
                            + "XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;\n"
            + "      last.scrollIntoView();\n"
            + "    }\n"

            + "    function loadMoreRows() {\n"
            + "      for (let i = 0; i < 50; i++) {\n"
            + "        const row = document.createElement('tr');\n"
            + "        row.innerHTML = `<td>${counter}</td><td>Item ${counter}</td>`;\n"
            + "        tableBody.appendChild(row);\n"
            + "        counter++;\n"
            + "      }\n"
            + "    }\n"

            + "    container.addEventListener('scroll', function() {\n"
            + "      if (container.scrollTop + container.clientHeight >= container.scrollHeight) {\n"
            + "        loadMoreRows();\r\n"
            + "      };\n"
            + "    });\n"

            + "    // Initial load\n"
            + "    loadMoreRows();"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final By tableRows = By.xpath("//tbody[@id='table-body']//tr");
        int rowCount = driver.findElements(tableRows).size();
        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), rowCount);

        driver.findElement(By.id("myButton")).click();

        rowCount = driver.findElements(tableRows).size();
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), rowCount);
    }

    /**
     * Tests the offsetParent property.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"element: span3 offsetParent: td2", "element: td2 offsetParent: table2",
             "element: tr2 offsetParent: table2", "element: table2 offsetParent: td1",
             "element: td1 offsetParent: table1", "element: tr1 offsetParent: table1",
             "element: table1 offsetParent: body1", "element: span2 offsetParent: body1",
             "element: span1 offsetParent: body1", "element: div1 offsetParent: body1",
             "element: body1 offsetParent: null"})
    public void offsetParent_Basic() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "function alertOffsetParent(id) {\n"
            + "  var element = document.getElementById(id);\n"
            + "  var offsetParent = element.offsetParent;\n"
            + "  var alertMessage = 'element: ' + element.id + ' offsetParent: ';\n"
            + "  if (offsetParent) {\n"
            + "    alertMessage += offsetParent.id;\n"
            + "  }\n"
            + "  else {\n"
            + "    alertMessage += offsetParent;\n"
            + "  }\n"
            + "  log(alertMessage);\n"
            + "}\n"
            + "function test() {\n"
            + "  alertOffsetParent('span3');\n"
            + "  alertOffsetParent('td2');\n"
            + "  alertOffsetParent('tr2');\n"
            + "  alertOffsetParent('table2');\n"
            + "  alertOffsetParent('td1');\n"
            + "  alertOffsetParent('tr1');\n"
            + "  alertOffsetParent('table1');\n"
            + "  alertOffsetParent('span2');\n"
            + "  alertOffsetParent('span1');\n"
            + "  alertOffsetParent('div1');\n"
            + "  alertOffsetParent('body1');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body id='body1' onload='test()'>\n"
            + "<div id='div1'>\n"
            + "  <span id='span1'>\n"
            + "    <span id='span2'>\n"
            + "      <table id='table1'>\n"
            + "        <tr id='tr1'>\n"
            + "          <td id='td1'>\n"
            + "            <table id='table2'>\n"
            + "              <tr id='tr2'>\n"
            + "                <td id='td2'>\n"
            + "                  <span id='span3'>some text</span>\n"
            + "                </td>\n"
            + "              </tr>\n"
            + "            </table>\n"
            + "          </td>\n"
            + "        </tr>\n"
            + "      </table>\n"
            + "    </span>\n"
            + "  </span>\n"
            + "</div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Tests the offsetParent property.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null"})
    public void offsetParent_newElement() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var oNew = document.createElement('span');\n"
            + "  log(oNew.offsetParent);\n"
            + "  var fragment = document.createDocumentFragment();\n"
            + "  fragment.appendChild(oNew);\n"
            + "  log(oNew.offsetParent);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Tests the offsetParent property, including the effects of CSS "position" attributes.
     * Based on <a href="http://dump.testsuite.org/2006/dom/style/offset/spec#offsetparent">this work</a>.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "body", "TypeError", "body", "body", "body",
                       "f1", "body", "h1", "i1", "td", "TypeError", "td", "body", "body"},
            FF = {"null", "body", "body", "body", "body", "body",
                  "f1", "body", "h1", "i1", "td", "body", "td", "body", "body"},
            FF_ESR = {"null", "body", "body", "body", "body", "body",
                      "f1", "body", "h1", "i1", "td", "body", "td", "body", "body"})
    public void offsetParent_WithCSS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body id='body' onload='test()'>\n"
            + "    <div id='a1'><div id='a2'>x</div></div>\n"
            + "    <div id='b1'><div id='b2' style='position:fixed'>x</div></div>\n"
            + "    <div id='c1'><div id='c2' style='position:static'>x</div></div>\n"
            + "    <div id='d1'><div id='d2' style='position:absolute'>x</div></div>\n"
            + "    <div id='e1'><div id='e2' style='position:relative'>x</div></div>\n"
            + "    <div id='f1' style='position:fixed'><div id='f2'>x</div></div>\n"
            + "    <div id='g1' style='position:static'><div id='g2'>x</div></div>\n"
            + "    <div id='h1' style='position:absolute'><div id='h2'>x</div></div>\n"
            + "    <div id='i1' style='position:relative'><div id='i2'>x</div></div>\n"
            + "    <table id='table'>\n"
            + "      <tr id='tr'>\n"
            + "        <td id='td'>\n"
            + "          <div id='j1'><div id='j2'>x</div></div>\n"
            + "          <div id='k1'><div id='k2' style='position:fixed'>x</div></div>\n"
            + "          <div id='l1'><div id='l2' style='position:static'>x</div></div>\n"
            + "          <div id='m1'><div id='m2' style='position:absolute'>x</div></div>\n"
            + "          <div id='n1'><div id='n2' style='position:relative'>x</div></div>\n"
            + "        </td>\n"
            + "      </tr>\n"
            + "    </table>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function alertOffsetParentId(id) {\n"
            + "        try {\n"
            + "          log(document.getElementById(id).offsetParent.id);\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "      function test() {\n"
            + "        log(document.getElementById('body').offsetParent);  // null (FF) null (IE)\n"
            + "        alertOffsetParentId('a2'); // body body \n"
            + "        alertOffsetParentId('b2'); // body exception \n"
            + "        alertOffsetParentId('c2'); // body body \n"
            + "        alertOffsetParentId('d2'); // body body \n"
            + "        alertOffsetParentId('e2'); // body body \n"
            + "        alertOffsetParentId('f2'); // f1   f1 \n"
            + "        alertOffsetParentId('g2'); // body body \n"
            + "        alertOffsetParentId('h2'); // h1   h1   \n"
            + "        alertOffsetParentId('i2'); // i1   i1   \n"
            + "        alertOffsetParentId('j2'); // td   td   \n"
            + "        alertOffsetParentId('k2'); // body exception   \n"
            + "        alertOffsetParentId('l2'); // td   td   \n"
            + "        alertOffsetParentId('m2'); // body body \n"
            + "        alertOffsetParentId('n2'); // body body \n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for Bug #616.
     * @throws Exception if the test fails
     */
    @Test
    public void offsetParent_withSelectors() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><style>\n"
            + "div ul > li {\n"
            + "  font-size: xx-small;\n"
            + "}\n"
            + "</style><script>\n"
            + "function test() {\n"
            + "  var divThing = document.getElementById('outer');\n"
            + "  while (divThing) {\n"
            + "    divThing = divThing.offsetParent;\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<div id='outer'></div>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined", "undefined", "undefined",
             "undefined", "123", "from myFunction", "123", "from myFunction"})
    public void prototype() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var d = document.getElementById('foo');\n"
            + "  log(d.foo);\n"
            + "  log(d.myFunction);\n"
            + "  var link = document.getElementById('testLink');\n"
            + "  log(link.foo);\n"
            + "  log(link.myFunction);\n"
            + "  HTMLElement.prototype.foo = 123;\n"
            + "  log(HTMLElement.foo);\n"
            + "  HTMLElement.prototype.myFunction = function() { return 'from myFunction'; };\n"
            + "  log(d.foo);\n"
            + "  log(d.myFunction());\n"
            + "  log(link.foo);\n"
            + "  log(link.myFunction());\n"
            + "}\n"
            + "</script></head><body onload='test()''>\n"
            + "<div id='foo'>bla</div>\n"
            + "<a id='testLink' href='foo'>bla</a>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * 'Element' and 'HTMLElement' prototypes are synonyms.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("in selectNodes")
    public void prototype_Element() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  Element.prototype.selectNodes = function(sExpr){\n"
            + "    log('in selectNodes');\n"
            + "  }\n"
            + "  document.getElementById('myDiv').selectNodes();\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void instanceOf() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var d = document.getElementById('foo');\n"
            + "  log(d instanceof HTMLDivElement);\n"
            + "  var link = document.getElementById('testLink');\n"
            + "  log(link instanceof HTMLAnchorElement);\n"
            + "}\n"
            + "</script></head><body onload='test()''>\n"
            + "<div id='foo'>bla</div>\n"
            + "<a id='testLink' href='foo'>bla</a>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "[object HTMLBodyElement]"})
    public void parentElement() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html id='htmlID'>\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div id='divID'/>\n"
            + "<script language=\"javascript\">\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.getElementById('htmlID').parentElement);\n"
            + "  log(document.getElementById('divID' ).parentElement);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void currentStyle() throws Exception {
        style("currentStyle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void runtimeStyle() throws Exception {
        style("runtimeStyle");
    }

    private void style(final String styleProperty) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var elem = document.getElementById('myDiv');\n"
            + "    var style = elem." + styleProperty + ";\n"
            + "    log(style);\n"
            + "    if (style) { log(style.color); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0"})
    public void clientLeftTop() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<div id='div1'>hello</div><script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d1 = document.getElementById('div1');\n"
            + "  log(d1.clientLeft);\n"
            + "  log(d1.clientTop);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Another nice feature of the IE.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0"})
    public void clientLeftTop_documentElement() throws Exception {
        final String html =
              "<!DOCTYPE HTML "
            +      "PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"
            + "<html>\n"
            + "<body>"
            + "<div id='div1'>hello</div><script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d1 = document.documentElement;\n"
            + "  log(d1.clientLeft);\n"
            + "  log(d1.clientTop);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "4"})
    public void clientLeftTopWithBorder() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<div id='div1' style='border: 4px solid black;'>hello</div><script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d1 = document.getElementById('div1');\n"
            + "  log(d1.clientLeft);\n"
            + "  log(d1.clientTop);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object DOMRect]")
    public void getBoundingClientRect() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<div id='div1'>hello</div><script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d1 = document.getElementById('div1');\n"
            + "  var pos = d1.getBoundingClientRect();\n"
            + "  log(pos);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"400", "100"})
    public void getBoundingClientRect2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var pos = d1.getBoundingClientRect();\n"
            + "    log(pos.left);\n"
            + "    log(pos.top);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='outer' style='position: absolute; left: 400px; top: 100px; width: 50px; height: 80px;'>"
            + "<div id='div1'></div></div>"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "100", "100", "50"})
    public void getBoundingClientRect_Scroll() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('outer');\n"
            + "    d1.scrollTop = 150;\n"
            + "    var pos = d1.getBoundingClientRect();\n"
            + "    log(pos.left);\n"
            + "    log(pos.top);\n"

            + "    d1 = document.getElementById('div1');\n"
            + "    pos = d1.getBoundingClientRect();\n"
            + "    log(pos.left);\n"
            + "    log(pos.top);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='outer' "
               + "style='position: absolute; height: 500px; width: 500px; top: 100px; left: 0px; overflow:auto'>\n"
            + "  <div id='div1' "
               + "style='position: absolute; height: 100px; width: 100px; top: 100px; left: 100px; z-index:99;'>"
               + "</div>\n"
            + "  <div id='div2' "
              + "style='position: absolute; height: 100px; width: 100px; top: 100px; left: 300px; z-index:99;'></div>\n"
            + "  <div style='position: absolute; top: 1000px;'>way down</div>\n"
            + "</div>"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object DOMRect]", "0", "0"})
    public void getBoundingClientRectDisconnected() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.createElement('div');\n"
            + "    try {\n"
            + "      var pos = d1.getBoundingClientRect();\n"
            + "      log(pos);\n"
            + "      log(pos.left);\n"
            + "      log(pos.top);\n"
            + "    } catch(e) { logEx(e);}\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object DOMRectList]", "1"})
    public void getClientRects() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var rects = d1.getClientRects();\n"
            + "    log(rects);\n"
            + "    log(rects.length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object DOMRectList]", "0"})
    public void getClientRectsDisconnected() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.createElement('div');\n"
            + "    log(d1.getClientRects());\n"
            + "    log(d1.getClientRects().length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object DOMRectList]", "0", "[object DOMRectList]", "0"})
    public void getClientRectsDisplayNone() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    display(d1);\n"
            + "    var d2 = document.getElementById('div2');\n"
            + "    display(d2);\n"
            + "  }\n"
            + "\n"
            + "  function display(elem) {\n"
            + "    log(elem.getClientRects());\n"
            + "    log(elem.getClientRects().length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' style='display: none'>\n"
            + "    <div id='div2' />\n"
            + "  </div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object DOMRectList]", "0"})
    public void getClientRectsInputHidden() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('hidden1');\n"
            + "    display(d1);\n"
            + "  }\n"
            + "\n"
            + "  function display(elem) {\n"
            + "    log(elem.getClientRects());\n"
            + "    log(elem.getClientRects().length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input type='hidden' name='hidden' id='hidden1'></input>\n"
            + "  </form>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object DOMRectList]", "1"})
    public void getClientRectsInputText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('text1');\n"
            + "    display(d1);\n"
            + "  }\n"
            + "\n"
            + "  function display(elem) {\n"
            + "    log(elem.getClientRects());\n"
            + "    log(elem.getClientRects().length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input type='text' name='text' id='text1'></input>\n"
            + "  </form>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object DOMRectList]", "1"})
    @HtmlUnitNYI(FF = {"[object DOMRectList]", "0"},
            FF_ESR = {"[object DOMRectList]", "0"})
    public void getClientRectsArea() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('area1');\n"
            + "    display(d1);\n"
            + "  }\n"
            + "\n"
            + "  function display(elem) {\n"
            + "    log(elem.getClientRects());\n"
            + "    log(elem.getClientRects().length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <img id='myImg' usemap='#imgmap'"
                    + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                    + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
            + "  <map id='myMap' name='imgmap'>\n"
            + "    <area id='area1' shape='rect' coords='0,0,1,1'>\n"
            + "  </map>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null"})
    public void innerHTML_parentNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.createElement('div');\n"
            + "    log(div1.parentNode);\n"
            + "    div1.innerHTML = '<p>hello</p>';\n"
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
    @Alerts({"null", "null"})
    public void innerText_parentNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.createElement('div');\n"
            + "    log(div1.parentNode);\n"
            + "    div1.innerText = '<p>hello</p>';\n"
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
    @Alerts({"true", "true", "true"})
    public void uniqueID() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    var div2 = document.getElementById('div2');\n"
            + "    log(div1.uniqueID == undefined);\n"
            + "    log(div1.uniqueID == div1.uniqueID);\n"
            + "    log(div1.uniqueID == div2.uniqueID);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "  <div id='div2'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests if element.uniqueID starts with 'ms__id', and is lazily created.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void uniqueIDFormatIE() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    var div2 = document.getElementById('div2');\n"
            + "    var id2 = div2.uniqueID;\n"
            + "    //as id2 is retrieved before getting id1, id2 should be < id1;\n"
            + "    var id1 = div1.uniqueID;\n"
            + "    if (id1 === undefined) { log('undefined'); return }\n"
            + "    log(id1.substring(0, 6) == 'ms__id');\n"
            + "    var id1Int = parseInt(id1.substring(6, id1.length));\n"
            + "    var id2Int = parseInt(id2.substring(6, id2.length));\n"
            + "    log(id2Int < id1Int);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "  <div id='div2'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("TypeError")
    public void setExpression() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var div1 = document.getElementById('div1');\n"
            + "      div1.setExpression('title','id');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"ex setExpression", "ex removeExpression"})
    public void removeExpression() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.getElementById('div1');\n"

            + "    try {\n"
            + "      div1.setExpression('title','id');\n"
            + "    } catch(e) { log('ex setExpression'); }\n"

            + "    try {\n"
            + "      div1.removeExpression('title');\n"
            + "    } catch(e) { log('ex removeExpression'); }\n"

            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("clicked")
    public void dispatchEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function foo() {\n"
            + "  var e = document.createEvent('MouseEvents');\n"
            + "  e.initMouseEvent('click', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);\n"
            + "  var d = document.getElementById('d');\n"
            + "  var canceled = !d.dispatchEvent(e);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='foo()'><div id='d' onclick='log(\"clicked\")'>foo</div></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            FF = "page2 loaded",
            FF_ESR = "page2 loaded")
    public void dispatchEvent_submitOnForm() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>page 1</title></head>\n"
            + "<body>\n"
            + "<form action='page2' id='theForm'>\n"
            + "  <span id='foo'/>\n"
            + "</form>\n"
            + "<script>\n"
            + "  var e = document.createEvent('HTMLEvents');\n"
            + "  e.initEvent('submit', true, false);\n"
            + "  document.getElementById('theForm').dispatchEvent(e);\n"
            + "</script>\n"
            + "</body></html>";

        final String page2 = DOCTYPE_HTML
            + "<html><body><script>alert('page2 loaded');</script></body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "page2"), page2);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dispatchEvent_submitOnFormChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>page 1</title></head><body>\n"
            + "<form action='page2'><span id='foo'/></form>\n"
            + "<script>\n"
            + "try {\n"
            + "  var e = document.createEvent('HTMLEvents');\n"
            + "  e.initEvent('submit', true, false);\n"
            + "  document.getElementById('foo').dispatchEvent(e);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertTitle(driver, "page 1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "false"})
    public void hasAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var elt = document.body;\n"
            + "    log(elt.hasAttribute('onload'));\n"
            + "    log(elt.hasAttribute('onLoad'));\n"
            + "    log(elt.hasAttribute('foo'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function")
    public void hasAttributeTypeOf() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var elt = document.body;\n"
            + "    log(typeof elt.hasAttribute);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"function", "true", "true", "false"})
    public void hasAttributeQuirksMode() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var elt = document.body;\n"
            + "    log(typeof elt.hasAttribute);\n"
            + "    log(elt.hasAttribute('onload'));\n"
            + "    log(elt.hasAttribute('onLoad'));\n"
            + "    log(elt.hasAttribute('foo'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined", "undefined"})
    public void getComponentVersion() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.body.cpuClass);\n"
            + "  document.body.style.behavior = 'url(#default#clientCaps)';\n"
            + "  log(document.body.cpuClass);\n"
            + "  if (document.body.getComponentVersion) {\n"
            + "    var ver=document.body.getComponentVersion('{E5D12C4E-7B4F-11D3-B5C9-0050045C3C96}','ComponentID');\n"
            + "    log(ver.length);\n"
            + "  }\n"
            + "  document.body.style.behavior = '';\n"
            + "  log(document.body.cpuClass);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"36", "46"})
    public void clientWidthAndHeight() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var myDiv = document.getElementById('myDiv');\n"
            + "    log(myDiv.clientWidth);\n"
            + "    log(myDiv.clientHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "<style>#myDiv { width:30px; height:40px; padding:3px; border:5px; margin:7px; }</style>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true"})
    public void clientWidthAndHeightPositionAbsolute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    var absDiv = document.getElementById('myAbsDiv');\n"

            // + "    log(div.clientWidth +', ' + absDiv.clientWidth);\n"
            + "    log(div.clientWidth > 100);\n"
            + "    log(absDiv.clientWidth > 10);\n"
            + "    log(absDiv.clientWidth < 100);\n"

            // + "    log(div.clientHeight +', ' + absDiv.clientHeight);\n"
            + "    log(div.clientHeight > 10);\n"
            + "    log(div.clientHeight == absDiv.clientHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>Test</div>\n"
            + "  <div id='myAbsDiv' style='position: absolute'>Test</div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0"})
    public void clientWidthAndHeightPositionAbsoluteEmpty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var absDiv = document.getElementById('myAbsDiv');\n"
            + "    log(absDiv.clientWidth);\n"
            + "    log(absDiv.clientHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myAbsDiv' style='position: absolute'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "false", "true", "true", "true", "true", "true", "true", "true"})
    public void scrollWidthAndHeight() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var myDiv = document.getElementById('myDiv');\n"
            + "    log(myDiv1.scrollWidth >= myDiv1.clientWidth);\n"
            + "    log(myDiv1.scrollHeight >= myDiv1.clientHeight);\n"

            + "    log(myDiv2.scrollWidth >= myDiv1.scrollWidth);\n"
            + "    log(myDiv2.scrollHeight >= myDiv1.scrollHeight);\n"
            + "    log(myDiv2.scrollWidth >= myDiv2.clientWidth);\n"
            + "    log(myDiv2.scrollHeight >= myDiv2.clientHeight);\n"

            + "    log(myDiv3.scrollWidth >= myDiv2.scrollWidth);\n"
            + "    log(myDiv3.scrollHeight >= myDiv2.scrollHeight);\n"
            + "    log(myDiv3.scrollWidth >= myDiv3.clientWidth);\n"
            + "    log(myDiv3.scrollHeight >= myDiv3.clientHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv1'/>\n"
            + "  <div id='myDiv2' style='height: 42px; width: 42px' />\n"
            + "  <div id='myDiv3' style='height: 7em; width: 7em' />\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0"})
    public void scrollWidthAndHeightDisplayNone() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var myDiv = document.getElementById('myDiv');\n"
            + "    log(myDiv.scrollWidth);\n"
            + "    log(myDiv.scrollHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' style='display: none;' />\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0"})
    public void scrollWidthAndHeightDetached() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var myDiv = document.createElement('div');\n"
            + "    log(myDiv.scrollWidth);\n"
            + "    log(myDiv.scrollHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for Bug #655.
     * @throws Exception if the test fails
     */
    @Test
    public void stackOverflowWithInnerHTML() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>Recursion</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "     document.body.innerHTML = unescape(document.body.innerHTML);\n"
            + "</script></body></html>";
        final WebDriver driver = loadPageWithAlerts2(html);
        assertTitle(driver, "Recursion");
    }

    /**
     * Test setting the class for the element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"x", "null", "[object Attr]", "null", "x", "byClassname"})
    public void class_className_attribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var e = document.getElementById('pid');\n"
            + "  log(e.getAttribute('class'));\n"
            + "  log(e.getAttribute('className'));\n"
            + "  log(e.getAttributeNode('class'));\n"
            + "  log(e.getAttributeNode('className'));\n"
            + "  e.setAttribute('className', 'byClassname');\n"
            + "  log(e.getAttribute('class'));\n"
            + "  log(e.getAttribute('className'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-undefined-x", "null-x-null", "null-[object Attr]-null", "null-[object Attr]-null",
             "x-byClassname", "[object Attr]-[object Attr]", "byClassname-byClassname", "[object Attr]-[object Attr]"})
    public void class_className_attribute2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var e = document.getElementById('pid');\n"
            + "  log(e['lang'] + '-' + e['class'] + '-' + e['className']);\n"
            + "  log(e.getAttribute('lang') + '-' + e.getAttribute('class') + '-' + e.getAttribute('className'));\n"
            + "  log(e.getAttributeNode('lang') + '-' + e.getAttributeNode('class') + '-' + "
            + "e.getAttributeNode('className'));\n"
            + "  log(e.attributes.getNamedItem('lang') + '-' + e.attributes.getNamedItem('class') + '-' + "
            + "e.attributes.getNamedItem('className'));\n"
            + "  e.setAttribute('className', 'byClassname');\n"
            + "  log(e.getAttribute('class') + '-' + e.getAttribute('className'));\n"
            + "  log(e.getAttributeNode('class') + '-' + e.getAttributeNode('className'));\n"
            + "  e.setAttribute('class', 'byClassname');\n"
            + "  log(e.getAttribute('class') + '-' + e.getAttribute('className'));\n"
            + "  log(e.getAttributeNode('class') + '-' + e.getAttributeNode('className'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "false", "false", "false", "false", "true", "true", "false", "false"})
    public void contains() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "try {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var div2 = document.getElementById('div2');\n"
            + "  var text = div2.firstChild;\n"
            + "  var div3 = document.getElementById('div3');\n"
            + "  log(div1.contains(div1));\n"
            + "  log(div1.contains(div2));\n"
            + "  log(div1.contains(div3));\n"
            + "  log(div1.contains(div4));\n"
            + "  log(div2.contains(div1));\n"
            + "  log(div3.contains(div1));\n"
            + "  log(div4.contains(div1));\n"
            + "  log(div2.contains(div3));\n"
            + "  log(div2.contains(text));\n"
            + "  log(div3.contains(text));\n"
            + "  log(text.contains(div3));\n"
            + "} catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'>\n"
            + "  <div id='div2'>hello\n"
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
    @Alerts({"exception[]", "false", "false"})
    public void contains_invalid_argument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION

            + "try {\n"
            + "  log(document.body.contains([]));\n"
            + "} catch(e) { log('exception[]'); }\n"

            + "try {\n"
            + "  log(document.body.contains(null));\n"
            + "} catch(e) { log('exception null'); }\n"

            + "try {\n"
            + "  log(document.body.contains(undefined));\n"
            + "} catch(e) { log('exception undefined'); }\n"

            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void filters() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var defined = typeof(div1.filters) != 'undefined';\n"
            + "  log(defined ? 'defined' : 'undefined');\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'>\n"
            + "</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({">#myClass#<", ">#myId##<"})
    public void attributes_trimmed() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "function log(msg) { window.document.title += msg.replace(/\\s/g, '#') + '§';}\n"
            + "function test() {\n"
            + "  var div1 = document.body.firstChild;\n"
            + "  log('>' + div1.className + '<');\n"
            + "  log('>' + div1.id + '<');\n"
            + "}\n"
            + "</script></head><body onload='test()'>"
            + "<div id=' myId  ' class=' myClass '>\n"
            + "hello"
            + "</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "* => body: 0, div1: 0", "foo => body: 3, div1: 1", "foo red => body: 1, div1: 0",
             "red foo => body: 1, div1: 0", "blue foo => body: 0, div1: 0", "null => body: 0, div1: 0"})
    public void getElementsByClassName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test(x) {\n"
            + "  var b = document.body;\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var s = x + ' => body: ' + b.getElementsByClassName(x).length;\n"
            + "  s += ', div1: ' + div1.getElementsByClassName(x).length;\n"
            + "  log(s);\n"
            + "}\n"
            + "function doTest() {\n"
            + "  var b = document.body;\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  log(typeof document.body.getElementsByClassName);\n"
            + "  test('*');\n"
            + "  test('foo');\n"
            + "  test('foo red');\n"
            + "  test('red foo');\n"
            + "  test('blue foo');\n"
            + "  test(null);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div class='foo' id='div1'>\n"
            + "  <span class='c2'>hello</span>\n"
            + "  <span class='foo' id='span2'>World!</span>\n"
            + "</div>\n"
            + "<span class='foo red' id='span3'>again</span>\n"
            + "<span class='red' id='span4'>bye</span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "[object HTMLDivElement]"})
    public void parentElement2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var fragment = document.createDocumentFragment();\n"
            + "  var div = document.createElement('div');\n"
            + "  var bold = document.createElement('b');\n"
            + "  fragment.appendChild(div);\n"
            + "  div.appendChild(bold);\n"
            + "  log(div.parentElement);\n"
            + "  log(bold.parentElement);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The method doScroll() should throw an exception if document is not yet loaded,
     * have a look into <a href="http://javascript.nwbox.com/IEContentLoaded/">this</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "TypeError"})
    public void doScroll() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    document.documentElement.doScroll('left');\n"
            + "    log('success');\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "test();\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("removeNode not available")
    public void removeNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var div2 = document.getElementById('div2');\n"
            + "  if (!div2.removeNode) { log('removeNode not available'); return }\n"

            + "  log(div1.firstChild.id);\n"
            + "  log(div2.removeNode().firstChild);\n"
            + "  log(div1.firstChild.id);\n"
            + "  log(div1.firstChild.nextSibling.id);\n"
            + "\n"
            + "  var div5 = document.getElementById('div5');\n"
            + "  var div6 = document.getElementById('div6');\n"
            + "  log(div5.firstChild.id);\n"
            + "  log(div6.removeNode(true).firstChild.id);\n"
            + "  log(div5.firstChild);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'><div id='div2'><div id='div3'></div><div id='div4'></div></div></div>\n"
            + "  <div id='div5'><div id='div6'><div id='div7'></div><div id='div8'></div></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "false", "hello", "true"})
    public void firefox__proto__() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div1 = document.createElement('div');\n"
            + "  log(div1.myProp);\n"
            + "  var p1 = div1['__proto__'];\n"
            + "  log(p1 == undefined);\n"
            + "  if (p1)\n"
            + "    p1.myProp = 'hello';\n"
            + "  log(div1.myProp);\n"
            + "  log(p1 !== document.createElement('form')['__proto__']);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false,false,false,false,false,true,false", "clearAttributes not available"})
    public void clearAttributes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function u(o) { return typeof o == 'undefined'; }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <input type='text' id='i' name='i' style='color:red' onclick='log(1)' custom1='a' />\n"
            + "  <script>\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.custom2 = 'b';\n"
            + "    log([u(i.type), u(i.id), u(i.name), u(i.style), u(i.onclick),"
            + "           u(i.custom1), u(i.custom2)].join(','));\n"
            + "    if(i.clearAttributes) {\n"
            + "      log([u(i.type), u(i.id), u(i.name), u(i.style), u(i.onclick),"
            + "             u(i.custom1), u(i.custom2)].join(','));\n"
            + "    } else {\n"
            + "      log('clearAttributes not available');\n"
            + "    }\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("mergeAttributes not available")
    public void mergeAttributes() throws Exception {
        mergeAttributesTest("i2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("mergeAttributes not available")
    public void mergeAttributesTrue() throws Exception {
        mergeAttributesTest("i2, true");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("mergeAttributes not available")
    public void mergeAttributesfalse() throws Exception {
        mergeAttributesTest("i2, false");
    }

    private void mergeAttributesTest(final String params) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function u(o) { return typeof o == 'undefined'; }\n"
            + "</script></head>\n"
            + "<body>"
            + "<input type='text' id='i' />\n"
            + "<input type='text' id='i2' name='i2' style='color:red' onclick='log(1)' custom1='a' />\n"
            + "<script>\n"
            + "function u(o) { return typeof o == 'undefined'; }\n"
            + "  var i = document.getElementById('i');\n"
            + "  if (i.mergeAttributes) {\n"
            + "    var i2 = document.getElementById('i2');\n"
            + "    i2.custom2 = 'b';\n"
            + "    log([u(i.type), u(i.id), u(i.name), u(i.style), u(i.onclick),"
            + "           u(i.custom1), u(i.custom2)].join(','));\n"
            + "    log(i.id);\n"
            + "    log(i.name);\n"
            + "    i.mergeAttributes(" + params + ");\n"
            + "    log([u(i.type), u(i.id), u(i.name), u(i.style), u(i.onclick),"
            + "           u(i.custom1), u(i.custom2)].join(','));\n"
            + "    log(i.id);\n"
            + "    log(i.name);\n"
            + "  } else {\n"
            + "    log('mergeAttributes not available');\n"
            + "  }\n"
            + "</script>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void document() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.body.document === document);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "TypeError"})
    public void prototype_innerHTML() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(HTMLElement.prototype.innerHTML);\n"
            + "} catch(e) { logEx(e); }\n"
            + "try {\n"
            + "  var myFunc = function() {};\n"
            + "  HTMLElement.prototype.innerHTML = myFunc;\n"
            + "  log(HTMLElement.prototype.innerHTML == myFunc);\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "#0000aa", "x", "BlanchedAlmond", "aBlue", "bluex"})
    public void setColorAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(b.vLink);\n"
            + "        document.vlinkColor = '#0000aa';\n"
            + "        log(b.vLink);\n"
            + "        document.vlinkColor = 'x';\n"
            + "        log(b.vLink);\n"
            + "        document.vlinkColor = 'BlanchedAlmond';\n"
            + "        log(b.vLink);\n"
            + "        document.vlinkColor = 'aBlue';\n"
            + "        log(b.vLink);\n"
            + "        document.vlinkColor = 'bluex';\n"
            + "        log(b.vLink);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<span onclick=\"var f = &quot;hello&quot; + 'world'\">test span</span>")
    public void innerHTMLwithQuotes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.getElementById('foo').innerHTML);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='foo'><span onclick=\"var f = &quot;hello&quot; + 'world'\">test span</span></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"button", "null", "false", "true"})
    public void attributeNS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = document.getElementById('foo');\n"
            + "    log(e.getAttribute('type'));\n"
            + "    try {\n"
            + "      log(e.getAttributeNS('bar', 'type'));\n"
            + "      log(e.hasAttributeNS('bar', 'type'));\n"
            + "      e.removeAttributeNS('bar', 'type');\n"
            + "      log(e.hasAttribute('type'));\n"
            + "    } catch(e) { log('getAttributeNS() not supported') }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='foo' type='button' value='someValue'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object DOMStringMap]")
    public void dataset() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.body.dataset);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("")
    public void setAttribute_className() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    div.setAttribute('className', 't');\n"
            + "    log(div.className);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("t")
    public void setAttribute_class() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    div.setAttribute('class', 't');\n"
            + "    log(div.className);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("")
    public void setAttribute_className_standards() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    div.setAttribute('className', 't');\n"
            + "    log(div.className);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("t")
    public void setAttribute_class_standards() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    div.setAttribute('class', 't');\n"
            + "    log(div.className);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"null", "", "null", "undefined"})
    public void getAttribute2() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function doTest() {\n"
                + "    var form = document.getElementById('testForm');\n"
                + "    log(form.getAttribute('target'));\n"
                + "    log(form.target);\n"
                + "    log(form.getAttribute('target222'));\n"
                + "    log(form.target222);\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<form id='testForm' action='#' method='get'>\n"
                + "</form>\n"
                + "</body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"null", "", "null", "undefined"})
    public void getAttribute2_standards() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  function doTest() {\n"
                + "    var form = document.getElementById('testForm');\n"
                + "    log(form.getAttribute('target'));\n"
                + "    log(form.target);\n"
                + "    log(form.getAttribute('target222'));\n"
                + "    log(form.target222);\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<form id='testForm' action='#' method='get'>\n"
                + "</form>\n"
                + "</body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"DIV", "SECTION", "<div></div>", "<section></section>"})
    public void nodeNameVsOuterElement() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.createElement('div').tagName);\n"
            + "      log(document.createElement('section').tagName);\n"
            + "      log(document.createElement('div').cloneNode( true ).outerHTML);\n"
            + "      log(document.createElement('section').cloneNode( true ).outerHTML);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "ho"})
    public void getSetAttribute_in_xml() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "    text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "    text += '  <xsl:template match=\"/\">\\n';\n"
            + "    text += \"  <html xmlns='http://www.w3.org/1999/xhtml'>\\n\";\n"
            + "    text += '    <body>\\n';\n"
            + "    text += '    </body>\\n';\n"
            + "    text += '  </html>\\n';\n"
            + "    text += '  </xsl:template>\\n';\n"
            + "    text += '</xsl:stylesheet>';\n"

            + "    var parser=new DOMParser();\n"
            + "    var doc=parser.parseFromString(text,'text/xml');\n"

            + "    var elem = doc.documentElement.getElementsByTagName('html').item(0);\n"
            + "    log(elem.getAttribute('hi'));\n"
            + "    elem.setAttribute('hi', 'ho');\n"
            + "    log(elem.getAttribute('hi'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Text]", "[object Text]"})
    public void textContentShouldNotDetachNestedNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><div><div id='it'>foo</div></div><script>\n"
            + LOG_TITLE_FUNCTION
            + "  var elt = document.getElementById('it');\n"
            + "  log(elt.firstChild);\n"
            + "  elt.parentNode.textContent = '';\n"
            + "  log(elt.firstChild);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<svg id=\"svgElem2\"></svg>")
    public void innerHTML_svg() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var div = document.createElement('div');\n"
                + "      document.body.appendChild(div);\n"
                + "      var svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');\n"
                + "      svg.setAttribute('id', 'svgElem2');\n"
                + "      div.appendChild(svg);\n"
                + "      log(div.innerHTML);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("executed")
    public void appendChildExecuteJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('script');\n"
            + "    try {\n"
            + "      newnode.appendChild(document.createTextNode('alerter();'));\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      outernode.appendChild(newnode);\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"appendChild start", "executed", "appendChild done"})
    public void appendChildExecuteNestedJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('div');\n"
            + "    var newscript = document.createElement('script');\n"
            + "    newnode.appendChild(newscript);\n"
            + "    try {\n"
            + "      newscript.appendChild(document.createTextNode('alerter();'));\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('appendChild start');\n"
            + "      outernode.appendChild(newnode);\n"
            + "      log('appendChild done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"appendChild start", "appendChild done", "executed"})
    public void appendChildExecuteNestedExternalJavaScript() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('div');\n"
            + "    var newscript = document.createElement('script');\n"
            + "    newscript.setAttribute('src', 'script.js');\n"
            + "    newnode.appendChild(newscript);\n"
            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('appendChild start');\n"
            + "      outernode.appendChild(newnode);\n"
            + "      log('appendChild done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"parseFromString done", "appendChild start", "executed", "appendChild done"},
            FF = {"parseFromString done", "appendChild start", "appendChild done"},
            FF_ESR = {"parseFromString done", "appendChild start", "appendChild done"})
    public void appendChildExecuteTemplateChildJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script>alerter();<' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    log('parseFromString done');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var template = responseBody.querySelector('template');\n"
            + "    var scriptElem = template.content.firstChild;\n"
            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('appendChild start');\n"
            + "      outernode.appendChild(scriptElem);\n"
            + "      log('appendChild done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"parseFromString done", "appendChild start", "appendChild done", "executed"},
            FF = {"parseFromString done", "appendChild start", "appendChild done"},
            FF_ESR = {"parseFromString done", "appendChild start", "appendChild done"})
    public void appendChildExecuteTemplateChildExternalJavaScript() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script src=\"script.js\"><' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    log('parseFromString done');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var template = responseBody.querySelector('template');\n"
            + "    var scriptElem = template.content.firstChild;\n"
            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('appendChild start');\n"
            + "      outernode.appendChild(scriptElem);\n"
            + "      log('appendChild done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"parseFromString done", "appendChild start1", "executed",
                       "appendChild done1", "appendChild start2", "appendChild done2"},
            FF = {"parseFromString done", "appendChild start1", "appendChild done1",
                  "appendChild start2", "appendChild done2"},
            FF_ESR = {"parseFromString done", "appendChild start1", "appendChild done1",
                      "appendChild start2", "appendChild done2"})
    public void appendChildExecuteTemplateFragmentJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script>alerter();<' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    log('parseFromString done');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var templateContent = responseBody.querySelector('template').content;\n"
            + "    var scriptElem = templateContent.firstChild;\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('appendChild start1');\n"
            + "      outernode.appendChild(scriptElem);\n"
            + "      log('appendChild done1');\n"
            + "    } catch(e) { logEx(e); }\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('secondNode');\n"
            + "      log('appendChild start2');\n"
            + "      outernode.appendChild(scriptElem);\n"
            + "      log('appendChild done2');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "  <div id='secondNode'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"parseFromString done", "appendChild start1",
                       "appendChild done1", "appendChild start2", "appendChild done2", "executed"},
            FF = {"parseFromString done", "appendChild start1", "appendChild done1",
                  "appendChild start2", "appendChild done2"},
            FF_ESR = {"parseFromString done", "appendChild start1", "appendChild done1",
                      "appendChild start2", "appendChild done2"})
    public void appendChildExecuteTemplateFragmentExternalJavaScript() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script src=\"script.js\"><' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    log('parseFromString done');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var templateContent = responseBody.querySelector('template').content;\n"
            + "    var scriptElem = templateContent.firstChild;\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('appendChild start1');\n"
            + "      outernode.appendChild(scriptElem);\n"
            + "      log('appendChild done1');\n"
            + "    } catch(e) { logEx(e); }\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('secondNode');\n"
            + "      log('appendChild start2');\n"
            + "      outernode.appendChild(scriptElem);\n"
            + "      log('appendChild done2');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "  <div id='secondNode'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("declared")
    public void appendChildDeclareJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('script');\n"
            + "    newnode.appendChild(document.createTextNode('function tester() { alerter(); }'));\n"
            + "    var outernode = document.getElementById('myNode');\n"
            + "    outernode.appendChild(newnode);\n"
            + "    tester();\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('declared');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"insertBefore start", "executed", "insertBefore done"})
    public void insertBeforeExecuteJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('script');\n"
            + "    try {\n"
            + "      newnode.appendChild(document.createTextNode('alerter();'));\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('insertBefore start');\n"
            + "      outernode.insertBefore(newnode, null);\n"
            + "      log('insertBefore done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"insertBefore start", "insertBefore done", "executed"})
    public void insertBeforeExecuteExternalJavaScript() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('script');\n"
            + "    try {\n"
            + "      newnode.setAttribute('src', 'script.js');\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('insertBefore start');\n"
            + "      outernode.insertBefore(newnode, null);\n"
            + "      log('insertBefore done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"insertBefore start", "executed", "insertBefore done"})
    public void insertBeforeExecuteNestedJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('div');\n"
            + "    var newscript = document.createElement('script');\n"
            + "    newnode.appendChild(newscript);\n"
            + "    try {\n"
            + "      newscript.appendChild(document.createTextNode('alerter();'));\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('insertBefore start');\n"
            + "      outernode.insertBefore(newnode, null);\n"
            + "      log('insertBefore done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"insertBefore start", "insertBefore done", "executed"})
    public void insertBeforeExecuteNestedExternalJavaScript() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('div');\n"
            + "    var newscript = document.createElement('script');\n"
            + "    newnode.appendChild(newscript);\n"
            + "    try {\n"
            + "      newscript.setAttribute('src', 'script.js');\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('insertBefore start');\n"
            + "      outernode.insertBefore(newnode, null);\n"
            + "      log('insertBefore done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"insertBefore start", "executed", "insertBefore done"},
            FF = {"insertBefore start", "insertBefore done"},
            FF_ESR = {"insertBefore start", "insertBefore done"})
    public void insertBeforeExecuteTemplateChildJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script>alerter();<' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var template = responseBody.querySelector('template');\n"
            + "    var scriptElem = template.content.firstChild;\n"
            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('insertBefore start');\n"
            + "      outernode.insertBefore(scriptElem, null);\n"
            + "      log('insertBefore done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"insertBefore start", "insertBefore done", "executed"},
            FF = {"insertBefore start", "insertBefore done"},
            FF_ESR = {"insertBefore start", "insertBefore done"})
    public void insertBeforeExecuteTemplateChildExternalJavaScript() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script src=\"script.js\"><' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var template = responseBody.querySelector('template');\n"
            + "    var scriptElem = template.content.firstChild;\n"
            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('insertBefore start');\n"
            + "      outernode.insertBefore(scriptElem, null);\n"
            + "      log('insertBefore done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"insertBefore start1", "executed", "insertBefore done1",
                       "insertBefore start2", "insertBefore done2"},
            FF = {"insertBefore start1", "insertBefore done1", "insertBefore start2", "insertBefore done2"},
            FF_ESR = {"insertBefore start1", "insertBefore done1", "insertBefore start2", "insertBefore done2"})
    public void insertBeforeExecuteTemplateFragmentJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script>alerter();<' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var templateContent = responseBody.querySelector('template').content;\n"
            + "    var scriptElem = templateContent.firstChild;\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('insertBefore start1');\n"
            + "      outernode.insertBefore(scriptElem, null);\n"
            + "      log('insertBefore done1');\n"
            + "    } catch(e) { logEx(e); }\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('secondNode');\n"
            + "      log('insertBefore start2');\n"
            + "      outernode.insertBefore(scriptElem, null);\n"
            + "      log('insertBefore done2');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "  <div id='secondNode'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"insertBefore start1", "insertBefore done1",
                       "insertBefore start2", "insertBefore done2", "executed"},
            FF = {"insertBefore start1", "insertBefore done1", "insertBefore start2", "insertBefore done2"},
            FF_ESR = {"insertBefore start1", "insertBefore done1", "insertBefore start2", "insertBefore done2"})
    public void insertBeforeExecuteTemplateFragmentExternalJavaScript() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script src=\"script.js\"><' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var templateContent = responseBody.querySelector('template').content;\n"
            + "    var scriptElem = templateContent.firstChild;\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('insertBefore start1');\n"
            + "      outernode.insertBefore(scriptElem, null);\n"
            + "      log('insertBefore done1');\n"
            + "    } catch(e) { logEx(e); }\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('secondNode');\n"
            + "      log('insertBefore start2');\n"
            + "      outernode.insertBefore(scriptElem, null);\n"
            + "      log('insertBefore done2');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "  <div id='secondNode'></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"insertBefore start1", "executed", "insertBefore done1", "insertBefore start2", "insertBefore done2"})
    public void insertBeforeExecuteTemplateFragmentDom() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var templateContent = document.getElementById('myTemplate').content;\n"
            + "    var scriptElem = templateContent.firstChild;\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('insertBefore start1');\n"
            + "      outernode.insertBefore(scriptElem, null);\n"
            + "      log('insertBefore done1');\n"
            + "    } catch(e) { logEx(e); }\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('secondNode');\n"
            + "      log('insertBefore start2');\n"
            + "      outernode.insertBefore(scriptElem, null);\n"
            + "      log('insertBefore done2');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <template id='myTemplate'><script>alerter();</script></template>\n"
            + "  <div id='myNode'></div>\n"
            + "  <div id='secondNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"insertBefore start1", "insertBefore done1", "insertBefore start2", "insertBefore done2", "executed"})
    public void insertBeforeExecuteTemplateFragmentDomExternal() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var templateContent = document.getElementById('myTemplate').content;\n"
            + "    var scriptElem = templateContent.firstChild;\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('insertBefore start1');\n"
            + "      outernode.insertBefore(scriptElem, null);\n"
            + "      log('insertBefore done1');\n"
            + "    } catch(e) { logEx(e); }\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('secondNode');\n"
            + "      log('insertBefore start2');\n"
            + "      outernode.insertBefore(scriptElem, null);\n"
            + "      log('insertBefore done2');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <template id='myTemplate'><script src=\"script.js\"></script></template>\n"
            + "  <div id='myNode'></div>\n"
            + "  <div id='secondNode'></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"insertBefore start", "executed", "insertBefore done"})
    public void insertBeforeDomFragmentJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let fragment = new DocumentFragment();\n"
            + "    var newscript = document.createElement('script');\n"
            + "    fragment.appendChild(newscript);\n"

            + "    try {\n"
            + "      newscript.appendChild(document.createTextNode('alerter();'));\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('insertBefore start');\n"
            + "      outernode.insertBefore(fragment, null);\n"
            + "      log('insertBefore done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"insertBefore start", "insertBefore done", "executed"})
    public void insertBeforeDomFragmentExternalJavaScript() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let fragment = new DocumentFragment();\n"
            + "    var newscript = document.createElement('script');\n"
            + "    fragment.appendChild(newscript);\n"

            + "    try {\n"
            + "      newscript.setAttribute('src', 'script.js');\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('insertBefore start');\n"
            + "      outernode.insertBefore(fragment, null);\n"
            + "      log('insertBefore done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("declared")
    public void insertBeforeDeclareJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('script');\n"
            + "    newnode.appendChild(document.createTextNode('function tester() { alerter(); }'));\n"
            + "    var outernode = document.getElementById('myNode');\n"
            + "    outernode.insertBefore(newnode, null);\n"
            + "    tester();\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('declared');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"replaceChild start", "executed", "replaceChild done"})
    public void replaceChildExecuteJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('script');\n"
            + "    try {\n"
            + "      newnode.appendChild(document.createTextNode('alerter();'));\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('replaceChild start');\n"
            + "      outernode.replaceChild(newnode, document.getElementById('inner'));\n"
            + "      log('replaceChild done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'><div id='inner'></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"replaceChild start", "replaceChild done", "executed"})
    public void replaceChildExecuteExternalJavaScript() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('script');\n"
            + "    try {\n"
            + "      newnode.setAttribute('src', 'script.js');\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('replaceChild start');\n"
            + "      outernode.replaceChild(newnode, document.getElementById('inner'));\n"
            + "      log('replaceChild done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'><div id='inner'></div></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"replaceChild start", "executed", "replaceChild done"})
    public void replaceChildExecuteNestedJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('div');\n"
            + "    var newscript = document.createElement('script');\n"
            + "    newnode.appendChild(newscript);\n"
            + "    try {\n"
            + "      newscript.appendChild(document.createTextNode('alerter();'));\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('replaceChild start');\n"
            + "      outernode.replaceChild(newnode, document.getElementById('inner'));\n"
            + "      log('replaceChild done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'><div id='inner'></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"replaceChild start", "replaceChild done", "executed"})
    public void replaceChildExecuteNestedExternalJavaScript() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('div');\n"
            + "    var newscript = document.createElement('script');\n"
            + "    newnode.appendChild(newscript);\n"
            + "    try {\n"
            + "      newscript.setAttribute('src', 'script.js');\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('replaceChild start');\n"
            + "      outernode.replaceChild(newnode, document.getElementById('inner'));\n"
            + "      log('replaceChild done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'><div id='inner'></div></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"replaceChild start", "executed", "replaceChild done"},
            FF = {"replaceChild start", "replaceChild done"},
            FF_ESR = {"replaceChild start", "replaceChild done"})
    public void replaceChildExecuteTemplateChildJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script>alerter();<' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var template = responseBody.querySelector('template');\n"
            + "    var scriptElem = template.content.firstChild;\n"
            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('replaceChild start');\n"
            + "      outernode.replaceChild(scriptElem, document.getElementById('inner'));\n"
            + "      log('replaceChild done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'><div id='inner'></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"replaceChild start", "replaceChild done", "executed"},
            FF = {"replaceChild start", "replaceChild done"},
            FF_ESR = {"replaceChild start", "replaceChild done"})
    public void replaceChildExecuteTemplateChildExternalJavaScript() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script src=\"script.js\"><' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var template = responseBody.querySelector('template');\n"
            + "    var scriptElem = template.content.firstChild;\n"
            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('replaceChild start');\n"
            + "      outernode.replaceChild(scriptElem, document.getElementById('inner'));\n"
            + "      log('replaceChild done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'><div id='inner'></div></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"replaceChild start1", "executed", "replaceChild done1",
                       "replaceChild start2", "replaceChild done2"},
            FF = {"replaceChild start1", "replaceChild done1", "replaceChild start2", "replaceChild done2"},
            FF_ESR = {"replaceChild start1", "replaceChild done1", "replaceChild start2", "replaceChild done2"})
    public void replaceChildExecuteTemplateFragmentJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script>alerter();<' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var templateContent = responseBody.querySelector('template').content;\n"
            + "    var scriptElem = templateContent.firstChild;\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('replaceChild start1');\n"
            + "      outernode.replaceChild(scriptElem, document.getElementById('inner'));\n"
            + "      log('replaceChild done1');\n"
            + "    } catch(e) { logEx(e); }\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('secondNode');\n"
            + "      log('replaceChild start2');\n"
            + "      outernode.replaceChild(scriptElem, document.getElementById('inner2'));\n"
            + "      log('replaceChild done2');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'><div id='inner'></div></div>\n"
            + "  <div id='secondNode'><div id='inner2'></div></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"replaceChild start1", "replaceChild done1",
                       "replaceChild start2", "replaceChild done2", "executed"},
            FF = {"replaceChild start1", "replaceChild done1", "replaceChild start2", "replaceChild done2"},
            FF_ESR = {"replaceChild start1", "replaceChild done1", "replaceChild start2", "replaceChild done2"})
    public void replaceChildExecuteTemplateFragmentExternalJavaScript() throws Exception {
        getMockWebConnection().setDefaultResponse("alerter();", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script src=\"script.js\"><' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var templateContent = responseBody.querySelector('template').content;\n"
            + "    var scriptElem = templateContent.firstChild;\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      log('replaceChild start1');\n"
            + "      outernode.replaceChild(scriptElem, document.getElementById('inner'));\n"
            + "      log('replaceChild done1');\n"
            + "    } catch(e) { logEx(e); }\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('secondNode');\n"
            + "      log('replaceChild start2');\n"
            + "      outernode.replaceChild(scriptElem, document.getElementById('inner2'));\n"
            + "      log('replaceChild done2');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'><div id='inner'></div></div>\n"
            + "  <div id='secondNode'><div id='inner2'></div></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("declared")
    public void replaceChildDeclareJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('script');\n"
            + "    newnode.appendChild(document.createTextNode('function tester() { alerter(); }'));\n"
            + "    var outernode = document.getElementById('myNode');\n"
            + "    outernode.replaceChild(newnode, document.getElementById('inner'));\n"
            + "    tester();\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('declared');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'><div id='inner'></div></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "<!--some comment-->", "true", "false"})
    public void replaceChildAddNewChildToDocument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createComment('some comment');\n"
            + "    log(document.contains ? document.contains(newnode) : '-');\n"

            + "    var outernode = document.getElementById('myNode');\n"
            + "    var oldnode = document.getElementById('inner');\n"
            + "    outernode.replaceChild(newnode, oldnode);\n"
            + "    log(outernode.innerHTML);\n"
            + "    log(document.contains ? document.contains(newnode) : '-');\n"
            + "    log(document.contains ? document.contains(oldnode) : '-');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'><div id='inner'></div></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"outside", "1", "middle", "2", "3", "4",
        "before-begin after-begin inside before-end after-end"})
    public void insertAdjacentHTML() throws Exception {
        insertAdjacentHTML("beforeend", "afterend", "beforebegin", "afterbegin");
        insertAdjacentHTML("beforeEnd", "afterEnd", "beforeBegin", "afterBegin");
        insertAdjacentHTML("BeforeEnd", "AfterEnd", "BeFoReBeGiN", "afterbegin");
    }

    /**
     * @param beforeEnd data to insert
     * @param afterEnd data to insert
     * @param beforeBegin data to insert
     * @param afterBegin data to insert
     * @throws Exception if the test fails
     */
    private void insertAdjacentHTML(final String beforeEnd,
            final String afterEnd, final String beforeBegin, final String afterBegin) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var oNode = document.getElementById('middle');\n"
            + "  oNode.insertAdjacentHTML('" + beforeEnd + "', ' <span id=3>before-end</span> ');\n"
            + "  oNode.insertAdjacentHTML('" + afterEnd + "', ' <span id=4>after-end</span> ');\n"
            + "  oNode.insertAdjacentHTML('" + beforeBegin + "', ' <span id=1>before-begin</span> ');\n"
            + "  oNode.insertAdjacentHTML('" + afterBegin + "', ' <span id=2>after-begin</span> ');\n"
            + "  var coll = document.getElementsByTagName('SPAN');\n"
            + "  for (var i = 0; i < coll.length; i++) {\n"
            + "    log(coll[i].id);\n"
            + "  }\n"
            + "  var outside = document.getElementById('outside');\n"
            + "  var text = outside.textContent ? outside.textContent : outside.innerText;\n"
            + "  text = text.replace(/(\\r\\n|\\r|\\n)/gm, '');\n"
            + "  text = text.replace(/(\\s{2,})/g, ' ');\n"
            + "  text = text.replace(/^\\s+|\\s+$/g, '');\n"
            + "  log(text);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <span id='outside' style='color: #00ff00'>\n"
            + "    <span id='middle' style='color: #ff0000'>\n"
            + "      inside\n"
            + "    </span>\n"
            + "  </span>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void insertAdjacentHTMLExecuteJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var outernode = document.getElementById('myNode');\n"
            + "    outernode.insertAdjacentHTML('afterend', '<scr'+'ipt>alerter();</scr'+'ipt>');\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void insertAdjacentHTMLExecuteNestedJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var outernode = document.getElementById('myNode');\n"
            + "    outernode.insertAdjacentHTML('afterend', '<div><scr'+'ipt>alerter();</scr'+'ipt></div>');\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ReferenceError")
    public void insertAdjacentHTMLDeclareJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var outernode = document.getElementById('myNode');\n"
            + "    outernode.insertAdjacentHTML('afterend', "
            + "'<scr'+'ipt>function tester() { alerter(); }</scr'+'ipt>');\n"
            + "    try {\n"
            + "      tester();\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('declared');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"outside", "1", "middle", "2", "3", "4",
                "before-begin after-begin inside before-end after-end"})
    public void insertAdjacentElement() throws Exception {
        insertAdjacentElement("beforeend", "afterend", "beforebegin", "afterbegin");
        insertAdjacentElement("beforeEnd", "afterEnd", "beforeBegin", "afterBegin");
        insertAdjacentElement("BeforeEnd", "AfterEnd", "BeFoReBeGiN", "afterbegin");
    }

    private void insertAdjacentElement(final String beforeEnd,
            final String afterEnd, final String beforeBegin, final String afterBegin) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var oNode = document.getElementById('middle');\n"
            + "  if (!oNode.insertAdjacentElement) { log('insertAdjacentElement not available'); return }\n"

            + "  oNode.insertAdjacentElement('" + beforeEnd + "', makeElement(3, 'before-end'));\n"
            + "  oNode.insertAdjacentElement('" + afterEnd + "', makeElement(4, ' after-end'));\n"
            + "  oNode.insertAdjacentElement('" + beforeBegin + "', makeElement(1, 'before-begin '));\n"
            + "  oNode.insertAdjacentElement('" + afterBegin + "', makeElement(2, ' after-begin'));\n"
            + "  var coll = document.getElementsByTagName('SPAN');\n"
            + "  for (var i = 0; i < coll.length; i++) {\n"
            + "    log(coll[i].id);\n"
            + "  }\n"
            + "  var outside = document.getElementById('outside');\n"
            + "  var text = outside.textContent ? outside.textContent : outside.innerText;\n"
            + "  text = text.replace(/(\\r\\n|\\r|\\n)/gm, '');\n"
            + "  text = text.replace(/(\\s{2,})/g, ' ');\n"
            + "  text = text.replace(/^\\s+|\\s+$/g, '');\n"
            + "  log(text);\n"
            + "}\n"
            + "function makeElement(id, value) {\n"
            + "  var span = document.createElement('span');\n"
            + "  span.appendChild(document.createTextNode(value));\n"
            + "  span.id = id;\n"
            + "  return span;\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <span id='outside' style='color: #00ff00'>\n"
            + "    <span id='middle' style='color: #ff0000'>\n"
            + "      inside\n"
            + "    </span>\n"
            + "  </span>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("executed")
    public void insertAdjacentElementExecuteJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('script');\n"
            + "    newnode.appendChild(document.createTextNode('alerter();'));\n"

            + "    var outernode = document.getElementById('myNode');\n"
            + "    if (!outernode.insertAdjacentElement) { log('insertAdjacentElement not available'); return }\n"
            + "    outernode.insertAdjacentElement('afterend', newnode);\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("executed")
    public void insertAdjacentElementExecuteNestedJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('div');\n"
            + "    var newscript = document.createElement('script');\n"
            + "    newnode.appendChild(newscript);\n"
            + "    newscript.appendChild(document.createTextNode('alerter();'));\n"

            + "    var outernode = document.getElementById('myNode');\n"
            + "    if (!outernode.insertAdjacentElement) { log('insertAdjacentElement not available'); return }\n"
            + "    outernode.insertAdjacentElement('afterend', newnode);\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"executed", "insertAdjacentElement done"},
            FF = "insertAdjacentElement done",
            FF_ESR = "insertAdjacentElement done")
    public void insertAdjacentElementExecuteTemplateChildJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script>alerter();<' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var template = responseBody.querySelector('template');\n"
            + "    var scriptElem = template.content.firstChild;\n"
            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      outernode.insertAdjacentElement('afterend', scriptElem);\n"
            + "      log('insertAdjacentElement done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"executed", "insertAdjacentElement done1", "insertAdjacentElement done2"},
            FF = {"insertAdjacentElement done1", "insertAdjacentElement done2"},
            FF_ESR = {"insertAdjacentElement done1", "insertAdjacentElement done2"})
    public void insertAdjacentElementExecuteTemplateFragmentJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var html = '<body><template><script>alerter();<' + '/script></template></body>';\n"
            + "    var parser = new DOMParser();\n"
            + "    var responseDoc = parser.parseFromString(html, 'text/html');\n"
            + "    var responseBody = responseDoc.body;\n"
            + "    var templateContent = responseBody.querySelector('template').content;\n"
            + "    var scriptElem = templateContent.firstChild;\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('myNode');\n"
            + "      outernode.insertAdjacentElement('afterend', scriptElem);\n"
            + "      log('insertAdjacentElement done1');\n"
            + "    } catch(e) { logEx(e); }\n"

            + "    try {\n"
            + "      var outernode = document.getElementById('secondNode');\n"
            + "      outernode.insertAdjacentElement('afterend', scriptElem);\n"
            + "      log('insertAdjacentElement done2');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('executed');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "  <div id='secondNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("declared")
    public void insertAdjacentElementDeclareJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var newnode = document.createElement('script');\n"
            + "    newnode.appendChild(document.createTextNode('function tester() { alerter(); }'));\n"
            + "    var outernode = document.getElementById('myNode');\n"
            + "    if (!outernode.insertAdjacentElement) { log('insertAdjacentElement not available'); return }\n"
            + "    outernode.insertAdjacentElement('afterend', newnode);\n"
            + "    tester();\n"
            + "  }\n"
            + "  function alerter() {\n"
            + "    log('declared');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"outside", "middle",
                "before-begin after-begin inside before-end after-end"})
    public void insertAdjacentText() throws Exception {
        insertAdjacentText("beforeend", "afterend", "beforebegin", "afterbegin");
        insertAdjacentText("beforeEnd", "afterEnd", "beforeBegin", "afterBegin");
        insertAdjacentText("BeforeEnd", "AfterEnd", "BeFoReBeGiN", "afterbegin");
    }

    private void insertAdjacentText(final String beforeEnd,
            final String afterEnd, final String beforeBegin, final String afterBegin) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var oNode = document.getElementById('middle');\n"
            + "  if (!oNode.insertAdjacentText) { log('insertAdjacentText not available'); return }\n"
            + "  oNode.insertAdjacentText('" + beforeEnd + "', 'before-end');\n"
            + "  oNode.insertAdjacentText('" + afterEnd + "', ' after-end');\n"
            + "  oNode.insertAdjacentText('" + beforeBegin + "', 'before-begin ');\n"
            + "  oNode.insertAdjacentText('" + afterBegin + "', ' after-begin');\n"
            + "  var coll = document.getElementsByTagName('SPAN');\n"
            + "  for (var i = 0; i < coll.length; i++) {\n"
            + "    log(coll[i].id);\n"
            + "  }\n"
            + "  var outside = document.getElementById('outside');\n"
            + "  var text = outside.textContent ? outside.textContent : outside.innerText;\n"
            + "  text = text.replace(/(\\r\\n|\\r|\\n)/gm, '');\n"
            + "  text = text.replace(/(\\s{2,})/g, ' ');\n"
            + "  text = text.replace(/^\\s+|\\s+$/g, '');\n"
            + "  log(text);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <span id='outside' style='color: #00ff00'>\n"
            + "    <span id='middle' style='color: #ff0000'>\n"
            + "      inside\n"
            + "    </span>\n"
            + "  </span>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Simple test that calls setCapture.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined", "undefined", "setCapture available"},
            CHROME = "TypeError",
            EDGE = "TypeError")
    public void setCapture() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    try {\n"
            + "      log(div.setCapture());\n"
            + "      log(div.setCapture(true));\n"
            + "      log(div.setCapture(false));\n"
            + "      log('setCapture available');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Simple test that calls setCapture.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "releaseCapture available"},
            CHROME = "TypeError",
            EDGE = "TypeError")
    public void releaseCapture() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    try {\n"
            + "      log(div.releaseCapture());\n"
            + "      log('releaseCapture available');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inherit", "false", "string", "boolean"})
    public void contentEditable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    log(div.contentEditable);\n"
            + "    log(div.isContentEditable);\n"
            + "    log(typeof div.contentEditable);\n"
            + "    log(typeof div.isContentEditable);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * See issue #1702.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void oninput() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var testNode = document.createElement('div');\n"
            + "      log('oninput' in testNode);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLBodyElement]", "[object HTMLButtonElement]",
             "http://srv/htmlunit.org", "http://srv/htmlunit.org"})
    public void focus() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.activeElement);\n"

            + "      var testNode = document.getElementById('myButton');\n"
            + "      testNode.focus();\n"
            + "      log(document.activeElement);\n"

            + "      testNode = document.getElementById('myA');\n"
            + "      testNode.focus();\n"
            + "      log(document.activeElement);\n"

            + "      testNode = document.getElementById('myDiv');\n"
            + "      testNode.focus();\n"
            + "      log(document.activeElement);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>blah</div>\n"
            + "  <a id='myA' href='http://srv/htmlunit.org'>anchor</a>\n"
            + "  <button id='myButton'>Press</button>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void blur() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<body>\n"
            + "  <div id='div1' onblur='alert(\"blurred\")'>the first div</div>\n"
            + "  <div id='div2'>the second div</div>\n"
            + "</body></html>";

        final WebDriver webDriver = loadPage2(html);

        webDriver.findElement(By.id("div1")).click();
        webDriver.findElement(By.id("div2")).click();

        verifyAlerts(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("blurred")
    public void blur2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<body>\n"
            + "  <div id='div1' onblur='alert(\"blurred\")' tabindex='0'>the first div</div>\n"
            + "  <div id='div2'>the second div</div>\n"
            + "</body></html>";

        final WebDriver webDriver = loadPage2(html);

        webDriver.findElement(By.id("div1")).click();
        webDriver.findElement(By.id("div2")).click();

        verifyAlerts(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement]", "[object HTMLBodyElement]", "[object Window]"})
    public void currentTarget() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(ev) {\n"
            + "    log(ev.currentTarget);\n"
            + "  }\n"

            + "  function test() {\n"
            + "    var byId = document.getElementById.bind(document);\n"

            + "    var under = byId('under');\n"
            + "    var over = byId('over');\n"
            + "    var body = document.body;\n"

            + "    var types = ['click'];\n"
            + "    for (var i = 0, type; (type = types[i]); ++i) {\n"
            + "      under.addEventListener(type, handler);\n"
            + "      over.addEventListener(type, handler);\n"
            + "      body.addEventListener(type, handler);\n"
            + "      window.addEventListener(type, handler);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='under'>\n"
            + "    <p id='contents'>Hello</p>"
            + "  </div>\n"
            + "  <div id='over'>abc</div>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.id("over")).click();
        verifyTitle2(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement]", "[object HTMLBodyElement]"})
    public void currentTargetBody() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(ev) {\n"
            + "    log(ev.currentTarget);\n"
            + "  }\n"

            + "  function test() {\n"
            + "    var byId = document.getElementById.bind(document);\n"

            + "    var under = byId('under');\n"
            + "    var over = byId('over');\n"
            + "    var body = document.body;\n"

            + "    var types = ['click'];\n"
            + "    for (var i = 0, type; (type = types[i]); ++i) {\n"
            + "      over.addEventListener(type, handler);\n"
            + "      body.addEventListener(type, handler);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='over'>abc</div>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.id("over")).click();
        verifyTitle2(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement]", "[object Window]"})
    public void currentTargetWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(ev) {\n"
            + "    log(ev.currentTarget);\n"
            + "  }\n"

            + "  function test() {\n"
            + "    var byId = document.getElementById.bind(document);\n"

            + "    var under = byId('under');\n"
            + "    var over = byId('over');\n"
            + "    var body = document.body;\n"

            + "    var types = ['click'];\n"
            + "    for (var i = 0, type; (type = types[i]); ++i) {\n"
            + "      over.addEventListener(type, handler);\n"
            + "      window.addEventListener(type, handler);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='over'>abc</div>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.id("over")).click();
        verifyTitle2(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"added", "removed", "[object HTMLDivElement]", "[object Window]"})
    public void addRemoveEventListenerFromBody() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(ev) {\n"
            + "    log(ev.currentTarget);\n"
            + "  }\n"

            + "  function test() {\n"
            + "    var under = document.getElementById('under');\n"
            + "    var over = document.getElementById('over');\n"
            + "    var body = document.body;\n"

            + "    over.addEventListener('click', handler);\n"
            + "    body.addEventListener('click', handler);\n"
            + "    window.addEventListener('click', handler);\n"
            + "    log('added');\n"

            + "    body.removeEventListener('click', handler);\n"
            + "    log('removed');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='over'>abc</div>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver webDriver = loadPageVerifyTitle2(html,
                getExpectedAlerts()[0], getExpectedAlerts()[1]);

        webDriver.findElement(By.id("over")).click();
        verifyTitle2(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"added", "removed", "[object HTMLDivElement]", "[object Window]"})
    public void addRemoveEventListenerFromBody2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(ev) {\n"
            + "    log(ev.currentTarget);\n"
            + "  }\n"

            + "  function test() {\n"
            + "    var under = document.getElementById('under');\n"
            + "    var over = document.getElementById('over');\n"
            + "    var body = document.body;\n"

            + "    over.addEventListener('click', handler);\n"
            + "    window.addEventListener('click', handler);\n"
//            + "    body.addEventListener('click', handler);\n"
            + "    log('added');\n"

            + "    body.removeEventListener('click', handler);\n"
            + "    log('removed');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='over'>abc</div>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver webDriver = loadPageVerifyTitle2(html,
                getExpectedAlerts()[0], getExpectedAlerts()[1]);

        webDriver.findElement(By.id("over")).click();
        verifyTitle2(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"added", "removed", "[object HTMLDivElement]", "[object HTMLBodyElement]"})
    public void addRemoveEventListenerFromWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(ev) {\n"
            + "    log(ev.currentTarget);\n"
            + "  }\n"

            + "  function test() {\n"
            + "    var under = document.getElementById('under');\n"
            + "    var over = document.getElementById('over');\n"
            + "    var body = document.body;\n"

            + "    over.addEventListener('click', handler);\n"
            + "    body.addEventListener('click', handler);\n"
            + "    window.addEventListener('click', handler);\n"
            + "    log('added');\n"

            + "    window.removeEventListener('click', handler);\n"
            + "    log('removed');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='over'>abc</div>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver webDriver = loadPageVerifyTitle2(html, getExpectedAlerts()[0], getExpectedAlerts()[1]);

        webDriver.findElement(By.id("over")).click();
        verifyTitle2(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"added", "removed", "[object HTMLDivElement]", "[object HTMLBodyElement]"})
    public void addRemoveEventListenerFromWindow1() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function handler(ev) {\n"
            + "    log(ev.currentTarget);\n"
            + "  }\n"

            + "  function test() {\n"
            + "    var under = document.getElementById('under');\n"
            + "    var over = document.getElementById('over');\n"
            + "    var body = document.body;\n"

            + "    over.addEventListener('click', handler);\n"
            + "    window.addEventListener('click', handler);\n"
            + "    body.addEventListener('click', handler);\n"
            + "    log('added');\n"

            + "    window.removeEventListener('click', handler);\n"
            + "    log('removed');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='over'>abc</div>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver webDriver = loadPageVerifyTitle2(html, getExpectedAlerts()[0], getExpectedAlerts()[1]);

        webDriver.findElement(By.id("over")).click();
        verifyTitle2(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "mousedown-over-over\nmousedown-over-body\nmousedown-over-undefined\n"
                    + "mouseup--body\nmouseup--undefined",
            FF = "mousedown-over-over\nmousedown-over-body\nmousedown-over-undefined\n"
                    + "mouseup--body\nmouseup--undefined\nclick-body-body\nclick-body-undefined",
            FF_ESR = "mousedown-over-over\nmousedown-over-body\nmousedown-over-undefined\n"
                    + "mouseup--body\nmouseup--undefined\nclick-body-body\nclick-body-undefined")
    @HtmlUnitNYI(CHROME = "mousedown-over-over\nmousedown-over-body\nmousedown-over-undefined\n"
                    + "mouseup-over-over\nmouseup-over-body\nmouseup-over-undefined",
            EDGE = "mousedown-over-over\nmousedown-over-body\nmousedown-over-undefined\n"
                    + "mouseup-over-over\nmouseup-over-body\nmouseup-over-undefined",
            FF = "mousedown-over-over\nmousedown-over-body\nmousedown-over-undefined\n"
                    + "mouseup-over-over\nmouseup-over-body\nmouseup-over-undefined",
            FF_ESR = "mousedown-over-over\nmousedown-over-body\nmousedown-over-undefined\n"
                    + "mouseup-over-over\nmouseup-over-body\nmouseup-over-undefined")
    public void clickAnElementThatDisappears() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<title>Test</title>\n"
            + "<script>\n"
            + "  function handler(e) {\n"
            + "    var log = document.getElementById('log');\n"
            + "    log.innerHTML += '<p></p>';\n"
            + "    log.lastElementChild.textContent = e.type + '-' + e.target.id + '-' + e.currentTarget.id;\n"
            + "  }\n"

            + "  function test() {\n"
            + "    var over = document.getElementById('over');\n"
            + "    var body = document.body;\n"

            + "    var types = ['click', 'mousedown', 'mouseup'];\n"
            + "    for (var i = 0, type; (type = types[i]); ++i) {\n"
            + "      over.addEventListener(type, handler);\n"
            + "      body.addEventListener(type, handler);\n"
            + "      window.addEventListener(type, handler);\n"
            + "    }\n"

            + "    over.addEventListener('mousedown', function () {\n"
            + "      over.style.display = 'none';\n"
            + "    });\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='body' onload='test()'>\n"
            + "  <div id='over'>abc</div>\n"
            + "  <div id='log'></div>\n"
            + "  </div>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.id("over")).click();
        assertEquals(getExpectedAlerts()[0], webDriver.findElement(By.id("log")).getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<select id=\"myId\"><option>Two</option></select>")
    public void innerHTML() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var select = document.getElementById('myId');\n"
            + "      select.innerHTML = \"<select id='myId2'><option>Two</option></select>\";\n"
            + "      log(document.body.innerHTML.trim());\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='myId'><option>One</option></select>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void innerHTMLGetElementsByTagName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var div = document.createElement('div');\n"
            + "      div.innerHTML = \"<table></table><a href='/a'>a</a>\";\n"
            + "      log(div.getElementsByTagName('a').length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false"})
    public void hidden() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    log(d1.hidden);\n"
            + "    var d2 = document.getElementById('div2');\n"
            + "    log(d2.hidden);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' style='display: none'>\n"
            + "  </div>\n"
            + "  <div id='div2' />\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1 null/false", "2 /true", "3 /true", "4 hidden/true", "5 true/true", "6 false/true",
                       "7 until-found/until-found", "8 show/true", "9 Until-Found/until-found", "10 HIDDEN/true"},
            FF_ESR = {"1 null/false", "2 /true", "3 /true", "4 hidden/true", "5 true/true", "6 false/true",
                      "7 until-found/true", "8 show/true", "9 Until-Found/true", "10 HIDDEN/true"})
    @HtmlUnitNYI(FF_ESR = {"1 null/false", "2 /true", "3 /true", "4 hidden/true", "5 true/true", "6 false/true",
                           "7 until-found/until-found", "8 show/true", "9 Until-Found/until-found", "10 HIDDEN/true"})
    public void hiddenGet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<p id='p1'>p1</p>\n"
            + "<p id='p2' hidden>p2</p>\n"
            + "<p id='p3' hidden=''>p3</p>\n"
            + "<p id='p4' hidden='hidden'>p4</p>\n"
            + "<p id='p5' hidden='true'>p5</p>\n"
            + "<p id='p6' hidden='false'>p6</p>\n"
            + "<p id='p7' hidden='until-found'>p7</p>\n"
            + "<p id='p8' hidden='show'>p8</p>\n"

            + "<p id='p9' hidden='Until-Found'>p9</p>\n"
            + "<p id='p10' hidden='HIDDEN'>p10</p>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var p1 = document.getElementById('p1');\n"
            + "var p2 = document.getElementById('p2');\n"
            + "var p3 = document.getElementById('p3');\n"
            + "var p4 = document.getElementById('p4');\n"
            + "var p5 = document.getElementById('p5');\n"
            + "var p6 = document.getElementById('p6');\n"
            + "var p7 = document.getElementById('p7');\n"
            + "var p8 = document.getElementById('p8');\n"
            + "var p9 = document.getElementById('p9');\n"
            + "var p10 = document.getElementById('p10');\n"

            + "log('1 ' + p1.getAttribute('hidden') + '/' + p1.hidden);\n"
            + "log('2 ' + p2.getAttribute('hidden') + '/' + p2.hidden);\n"
            + "log('3 ' + p3.getAttribute('hidden') + '/' + p3.hidden);\n"
            + "log('4 ' + p4.getAttribute('hidden') + '/' + p4.hidden);\n"
            + "log('5 ' + p5.getAttribute('hidden') + '/' + p5.hidden);\n"
            + "log('6 ' + p6.getAttribute('hidden') + '/' + p6.hidden);\n"
            + "log('7 ' + p7.getAttribute('hidden') + '/' + p7.hidden);\n"
            + "log('8 ' + p8.getAttribute('hidden') + '/' + p8.hidden);\n"
            + "log('9 ' + p9.getAttribute('hidden') + '/' + p9.hidden);\n"
            + "log('10 ' + p10.getAttribute('hidden') + '/' + p10.hidden);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1 null/false", "2 null/false", "3 /true", "4 /true", "5 null/false",
                       "6 null/false", "7 /true", "8 /true",
                       "9 null/false", "10 /true", "11 /true",
                       "12 null/false", "13 until-found/until-found", "14 null/false",
                       "15 null/false", "16 until-found/until-found",
                       "17 null/false", "18 /true"},
            FF_ESR = {"1 null/false", "2 null/false", "3 /true", "4 /true", "5 null/false",
                      "6 null/false", "7 /true", "8 /true",
                      "9 null/false", "10 /true", "11 /true",
                      "12 null/false", "13 /true", "14 null/false",
                      "15 null/false", "16 /true",
                      "17 null/false", "18 /true"})
    @HtmlUnitNYI(FF_ESR = {"1 null/false", "2 null/false", "3 /true", "4 /true", "5 null/false",
                           "6 null/false", "7 /true", "8 /true",
                           "9 null/false", "10 /true", "11 /true",
                           "12 null/false", "13 until-found/until-found", "14 null/false",
                           "15 null/false", "16 until-found/until-found",
                           "17 null/false", "18 /true"})
    public void hiddenSet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<p id='p1'>p1</p>\n"
            + "<p id='p2'>p2</p>\n"
            + "<p id='p3'>p3</p>\n"
            + "<p id='p4'>p4</p>\n"
            + "<p id='p5'>p5</p>\n"
            + "<p id='p6'>p6</p>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function set(p, value) {\n"
            + "  try {\n"
            + "    p.hidden = value;\n"
            + "  } catch(e) {\n"
            + "    log('!');\n"
            + "  }\n"
            + "}\n"
            + "var p1 = document.getElementById('p1');\n"
            + "var p2 = document.getElementById('p2');\n"
            + "var p3 = document.getElementById('p3');\n"
            + "var p4 = document.getElementById('p4');\n"
            + "var p5 = document.getElementById('p5');\n"
            + "var p6 = document.getElementById('p6');\n"

            + "log('1 ' + p1.getAttribute('hidden') + '/' + p1.hidden);\n"

            + "set(p1, '');\n"
            + "log('2 ' + p1.getAttribute('hidden') + '/' + p1.hidden);\n"

            + "set(p1, 'hidden');\n"
            + "log('3 ' + p1.getAttribute('hidden') + '/' + p1.hidden);\n"

            + "set(p1, 'false');\n"
            + "log('4 ' + p1.getAttribute('hidden') + '/' + p1.hidden);\n"

            + "set(p1, false);\n"
            + "log('5 ' + p1.getAttribute('hidden') + '/' + p1.hidden);\n"

            // p2
            + "log('6 ' + p2.getAttribute('hidden') + '/' + p2.hidden);\n"
            + "set(p2, 'true');\n"
            + "log('7 ' + p2.getAttribute('hidden') + '/' + p2.hidden);\n"

            + "set(p2, 'show');\n"
            + "log('8 ' + p2.getAttribute('hidden') + '/' + p2.hidden);\n"

            // p3
            + "log('9 ' + p3.getAttribute('hidden') + '/' + p3.hidden);\n"
            + "set(p3, true);\n"
            + "log('10 ' + p3.getAttribute('hidden') + '/' + p3.hidden);\n"

            + "set(p3, 'show');\n"
            + "log('11 ' + p3.getAttribute('hidden') + '/' + p3.hidden);\n"

            // p4
            + "log('12 ' + p4.getAttribute('hidden') + '/' + p4.hidden);\n"
            + "set(p4, 'until-found');\n"
            + "log('13 ' + p4.getAttribute('hidden') + '/' + p4.hidden);\n"

            + "set(p4, '');\n"
            + "log('14 ' + p4.getAttribute('hidden') + '/' + p4.hidden);\n"

            // p5
            + "log('15 ' + p5.getAttribute('hidden') + '/' + p5.hidden);\n"
            + "set(p5, 'Until-Found');\n"
            + "log('16 ' + p5.getAttribute('hidden') + '/' + p5.hidden);\n"

            // p6
            + "log('17 ' + p6.getAttribute('hidden') + '/' + p6.hidden);\n"
            + "set(p6, 'HIDDEN');\n"
            + "log('18 ' + p6.getAttribute('hidden') + '/' + p6.hidden);\n"

            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isDisplayed() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  <div id='div1' hidden>\n"
            + "    <div id='child' />\n"
            + "  </div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement element = driver.findElement(By.id("child"));
        assertFalse(element.isDisplayed());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "go", "", "enter", "done",  "go", "next", "previous", "search", "send"})
    public void enterKeyHint() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = 'GO';\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = 'run';\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = 'enter';\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = 'done';\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = 'go';\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = 'next';\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = 'previous';\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = 'search';\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = 'send';\n"
            + "    log(d1.enterKeyHint);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' style='display: none'>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "go", "", "", ""})
    public void enterKeyHint2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = 'GO';\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = 'run';\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = undefined;\n"
            + "    log(d1.enterKeyHint);\n"

            + "    d1.enterKeyHint = null;\n"
            + "    log(d1.enterKeyHint);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' style='display: none'>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"go", "go", "", "", ""})
    public void enterKeyHintDefaults() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input1 = document.getElementById('input1');\n"
            + "    log(input1.enterKeyHint);\n"

            + "    var input2 = document.getElementById('input2');\n"
            + "    log(input2.enterKeyHint);\n"

            + "    var input3 = document.getElementById('input3');\n"
            + "    log(input3.enterKeyHint);\n"

            + "    var input4 = document.getElementById('input4');\n"
            + "    log(input4.enterKeyHint);\n"

            + "    var input5 = document.getElementById('input5');\n"
            + "    log(input5.enterKeyHint);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='input1' enterkeyhint='go'>\n"
            + "  <input id='input2' enterkeyhint='gO'>\n"
            + "  <input id='input3' enterkeyhint='run'>\n"
            + "  <input id='input4' enterkeyhint=undefined>\n"
            + "  <input id='input5' enterkeyhint=null>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "", "true", "7", "true", "seven", "false", "null"})
    public void autofocus() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <title>test</title>\n"
                + "  <script>\n"
                + LOG_TEXTAREA_FUNCTION
                + "  function doTest() {\n"
                + "    var myNode1 = document.getElementById('myNode1');\n"
                + "    log(myNode1.autofocus);\n"
                + "    log(myNode1.getAttribute('autofocus'));\n"

                + "    var myNode2 = document.getElementById('myNode2');\n"
                + "    log(myNode2.autofocus);\n"
                + "    log(myNode2.getAttribute('autofocus'));\n"

                + "    var myNode3 = document.getElementById('myNode3');\n"
                + "    log(myNode3.autofocus);\n"
                + "    log(myNode3.getAttribute('autofoCuS'));\n"

                + "    var myNode4 = document.getElementById('myNode4');\n"
                + "    log(myNode4.autofocus);\n"
                + "    log(myNode4.getAttribute('autofocus'));\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<p id='myNode1' autofocus></p>\n"
                + "<p id='myNode2' autofocus=7></p>\n"
                + "<p id='myNode3' autOFocus='seven'></p>\n"
                + "<p id='myNode4'></p>\n"
                + LOG_TEXTAREA
                + "</body>\n"
                + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "", "true", "", "false", "null", "true", "7",
             "true", "", "false", "null", "true", "", "false", "null",
             "true", "", "false", "null", "false", "null"})
    public void editAutofocus() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <title>test</title>\n"
                + "  <script>\n"
                + LOG_TEXTAREA_FUNCTION
                + "  function doTest() {\n"
                + "    var myNode1 = document.getElementById('myNode1');\n"
                + "    log(myNode1.autofocus);\n"
                + "    log(myNode1.getAttribute('autofocus'));\n"

                + "    myNode1.setAttribute('autofocus', '');\n"
                + "    log(myNode1.autofocus);\n"
                + "    log(myNode1.getAttribute('autofocus'));\n"

                + "    myNode1.removeAttribute('autofocus');\n"
                + "    log(myNode1.autofocus);\n"
                + "    log(myNode1.getAttribute('autofocus'));\n"

                + "    var myNode2 = document.getElementById('myNode2');\n"
                + "    log(myNode2.autofocus);\n"
                + "    log(myNode2.getAttribute('autofocus'));\n"

                + "    myNode2.autofocus = true;\n"
                + "    log(myNode2.autofocus);\n"
                + "    log(myNode2.getAttribute('autofocus'));\n"

                + "    myNode2.autofocus = false;\n"
                + "    log(myNode2.autofocus);\n"
                + "    log(myNode2.getAttribute('autofocus'));\n"

                + "    myNode2.autofocus = true;\n"
                + "    log(myNode2.autofocus);\n"
                + "    log(myNode2.getAttribute('autofocus'));\n"

                + "    myNode2.autofocus = false;\n"
                + "    log(myNode2.autofocus);\n"
                + "    log(myNode2.getAttribute('autofocus'));\n"

                + "    myNode2.autofocus = 'no';\n"
                + "    log(myNode2.autofocus);\n"
                + "    log(myNode2.getAttribute('autofocus'));\n"

                + "    myNode2.autofocus = null;\n"
                + "    log(myNode2.autofocus);\n"
                + "    log(myNode2.getAttribute('autofocus'));\n"

                + "    myNode2.autofocus = undefined;\n"
                + "    log(myNode2.autofocus);\n"
                + "    log(myNode2.getAttribute('autofocus'));\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<p id='myNode1' autofocus></p>\n"
                + "<p id='myNode2' autofocus=7></p>\n"
                + LOG_TEXTAREA
                + "</body>\n"
                + "</html>";

        loadPageVerifyTextArea2(html);
    }
}
