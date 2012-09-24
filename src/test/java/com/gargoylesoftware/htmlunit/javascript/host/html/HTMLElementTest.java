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

import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * Tests for {@link HTMLElement}.
 *
 * @version $Revision$
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
 */
@RunWith(BrowserRunner.class)
public class HTMLElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.FF)
    @Alerts(IE = { "all node for body: DIV A IMG DIV ", "all node for testDiv: A IMG ",
            "all node for testA: IMG ", "all node for testImg: ", "all node for testDiv2: " })
    public void all_IndexByInt() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
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
            + "  var str = 'all node for ' + _id + ': ';\n"
            + "  for (var i=0; i<col.length; i++)\n"
            + "  {\n"
            + "    str += col[i].tagName + ' ';\n"
            + "  }\n"
            + "  alert(str);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()' id='body'>\n"
            + "<div id='testDiv'>foo<a href='foo.html' id='testA'><img src='foo.png' id='testImg'></a></div>\n"
            + "<div id='testDiv2'>foo</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "a", "a", "undefined", "null" })
    public void getAttribute() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "       var myNode = document.getElementById('myNode');\n"
                + "       alert(myNode.title);\n"
                + "       alert(myNode.getAttribute('title'));\n"
                + "       alert(myNode.Title);\n"
                + "       alert(myNode.getAttribute('class'));\n"
                + "   }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<p id='myNode' title='a'>\n"
                + "</p>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals("test", webDriver.getTitle());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "color: green;", IE = "")
    public void getAttribute_styleAttributeWithFlag() throws Exception {
        final String html =
              "<html><body onload='test()'><div id='div' style='color: green;'>abc</div>\n"
            + "<script>\n"
            + "  function test(){\n"
            + "    var div = document.getElementById('div');\n"
            + "    alert(div.getAttribute('style', 2));\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Some libraries like MochiKit looks after the number of attributes of a freshly created node.
     * When this is fixed for IE, all {@link com.gargoylesoftware.htmlunit.libraries.MochiKitTest}
     * working for FF will work for IE too.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "0 attribute", IE = "at least 1 attribute")
    public void attributes() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "       var myNode = document.body.firstChild;\n"
                + "       if (myNode.attributes.length == 0)\n"
                + "         alert('0 attribute');\n"
                + "       else\n"
                + "         alert('at least 1 attribute');\n"
                + "   }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>" // no \n here!
                + "<span>test span</span>\n"
                + "</body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "", "bla", "true" }, IE = "exception")
    public void getSetAttributeNS() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "try {\n"
                + "       var myNode = document.getElementById('myNode');\n"
                + "       alert(myNode.getAttributeNS('myNamespaceURI', 'my:foo'));\n"
                + "       myNode.setAttributeNS('myNamespaceURI', 'my:foo', 'bla');\n"
                + "       alert(myNode.getAttributeNS('myNamespaceURI', 'foo'));\n"
                + "       alert(myNode.getAttributeNodeNS('myNamespaceURI', 'foo').specified);\n"
                + "} catch (e) { alert('exception'); }\n"
                + "   }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<p id='myNode' title='a'>\n"
                + "</p>\n"
                + "</body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "a", "b", "undefined", "foo" })
    public void setAttribute() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       alert(myNode.title);\n"
            + "       myNode.setAttribute('title', 'b');\n"
            + "       alert(myNode.title);\n"
            + "       alert(myNode.Title);\n"
            + "       myNode.Title = 'foo';\n"
            + "       alert(myNode.Title);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode' title='a'>\n"
            + "</p>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals("test", webDriver.getTitle());
    }

    /**
     * Caution: with IE if you get a node with some lowercase letters, the node will be retrieved
     * and will get as name the value passed as attribute to getAttributeNode.
     * The consequence for IE: x.getAttributeNode("Foo").nodeName != x.getAttributeNode("foo").nodeName
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = {
            "null",
            "expando=true",
            "firstChild=null",
            "lastChild=null",
            "name=custom_attribute",
            "nextSibling=null",
            "nodeName=custom_attribute",
            "nodeType=2",
            "nodeValue=bleh",
            "(ownerDocument==document)=true",
            "parentNode=null",
            "previousSibling=null",
            "specified=true",
            "value=bleh" },
            DEFAULT = {
            "null",
            "expando=undefined",
            "firstChild=[object Text]",
            "lastChild=[object Text]",
            "name=custom_attribute",
            "nextSibling=null",
            "nodeName=custom_attribute",
            "nodeType=2",
            "nodeValue=bleh",
            "(ownerDocument==document)=true",
            "parentNode=null",
            "previousSibling=null",
            "specified=true",
            "value=bleh"
            })
    public void getAttributeNode() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var div = document.getElementById('div2');\n"
            + "      alert(div.getAttributeNode('notExisting'));\n"
            + "      var customAtt = div.getAttributeNode('custom_attribute');\n"
            + "      alertAttributeProperties(customAtt);\n"
            + "    }\n"
            + "    function alertAttributeProperties(att) {\n"
            + "      alert('expando=' + att.expando);\n"
            + "      alert('firstChild=' + att.firstChild);\n"
            + "      alert('lastChild=' + att.lastChild);\n"
            + "      alert('name=' + att.name);\n"
            + "      alert('nextSibling=' + att.nextSibling);\n"
            + "      alert('nodeName=' + att.nodeName);\n"
            + "      alert('nodeType=' + att.nodeType);\n"
            + "      alert('nodeValue=' + att.nodeValue);\n"
            + "      alert('(ownerDocument==document)=' + (att.ownerDocument==document));\n"
            + "      alert('parentNode=' + att.parentNode);\n"
            + "      alert('previousSibling=' + att.previousSibling);\n"
            + "      alert('specified=' + att.specified);\n"
            + "      alert('value=' + att.value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'></div>\n"
            + "  <div id='div2' name='blah' custom_attribute='bleh'></div>\n"
            + "  <div id='div3'></div>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals("test", webDriver.getTitle());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "left", "left", "right", "right" })
    public void setAttributeNode() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      // Get the old alignment.\n"
            + "      var div1 = document.getElementById('div1');\n"
            + "      var a1 = div1.getAttributeNode('align');\n"
            + "      alert(a1.value);\n"
            + "      // Set the new alignment.\n"
            + "      var a2 = document.createAttribute('align');\n"
            + "      a2.value = 'right';\n"
            + "      a1 = div1.setAttributeNode(a2);\n"
            + "      alert(a1.value);\n"
            + "      alert(div1.getAttributeNode('align').value);\n"
            + "      alert(div1.getAttribute('align'));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' align='left'></div>\n"
            + "</body>\n"
            + "</html>";
        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals("test", webDriver.getTitle());
    }

    /**
     * Test for <tt>getElementsByTagName</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "all = 4", "row = 2", "by wrong name: 0" })
    public void getElementsByTagName() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "  var a1 = document.getElementsByTagName('td');\n"
            + "  alert('all = ' + a1.length);\n"
            + "  var firstRow = document.getElementById('r1');\n"
            + "  var rowOnly = firstRow.getElementsByTagName('td');\n"
            + "  alert('row = ' + rowOnly.length);\n"
            + "  alert('by wrong name: ' + firstRow.getElementsByTagName('>').length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<table>\n"
            + "<tr id='r1'><td>1</td><td>2</td></tr>\n"
            + "<tr id='r2'><td>3</td><td>4</td></tr>\n"
            + "</table>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "div1", "div2" })
    public void getElementsByTagName2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    for (var f = 0; (formnode = document.getElementsByTagName('form').item(f)); f++)\n"
            + "      for (var i = 0; (node = formnode.getElementsByTagName('div').item(i)); i++)\n"
            + "        alert(node.id);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form>\n"
            + "    <div id='div1'/>\n"
            + "    <div id='div2'/>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that {@link HTMLElement#jsxFunction_getElementsByTagName} returns an associative array.
     * Test for bug 1369514.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "first", "second", "third" })
    public void getElementsByTagNameCollection() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var form1 = document.getElementById('form1');\n"
            + "  var elements = form1.getElementsByTagName('input');\n"
            + "  alert(elements['one'].name);\n"
            + "  alert(elements['two'].name);\n"
            + "  alert(elements['three'].name);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<form id='form1'>\n"
            + "<input id='one' name='first' type='text'>\n"
            + "<input id='two' name='second' type='text'>\n"
            + "<input id='three' name='third' type='text'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests that getElementsByTagName('*') returns all child elements, both
     * at the document level and at the element level.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "8", "3" })
    public void getElementsByTagNameAsterisk() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      alert(document.getElementsByTagName('*').length);\n"
            + "      alert(document.getElementById('div').getElementsByTagName('*').length);\n"
            + "   }\n"
            + "</script>\n"
            + "<div id='div'><p>a</p><p>b</p><p>c</p></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "true", "true", "false", "false" })
    public void getElementsByTagNameEquality() throws Exception {
        final String html =
              "<html><body><div id='d'><script>\n"
            + "var div = document.getElementById('d');\n"
            + "alert(document.getElementsByTagName('*') == document.getElementsByTagName('*'));\n"
            + "alert(document.getElementsByTagName('script') == document.getElementsByTagName('script'));\n"
            + "alert(document.getElementsByTagName('foo') == document.getElementsByTagName('foo'));\n"
            + "alert(document.getElementsByTagName('script') == document.getElementsByTagName('body'));\n"
            + "alert(document.getElementsByTagName('script') == div.getElementsByTagName('script'));\n"
            + "</script></div></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test getting the class for the element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("the class is x")
    public void getClassName() throws Exception {
        final String html
            = "<html><head><style>.x {  font: 8pt Arial bold;  }</style>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var ele = document.getElementById('pid');\n"
            + "    var aClass = ele.className;\n"
            + "    alert('the class is ' + aClass);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test setting the class for the element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("the class is z")
    public void setClassName() throws Exception {
        final String html
            = "<html><head><style>.x {  font: 8pt Arial bold;  }</style>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var ele = document.getElementById('pid');\n"
            + "    ele.className = 'z';\n"
            + "    var aClass = ele.className;\n"
            + "    alert('the class is ' + aClass);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("if (1 > 2 & 3 < 2) willNotHappen('yo');")
    public void getInnerHTML() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script id='theScript'>"
            + "if (1 > 2 & 3 < 2) willNotHappen('yo');"
            + "</script>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('theScript');\n"
            + "       alert(myNode.innerHTML);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<form id='myNode'></form>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = "<DIV id=i foo=\"\" name=\"\"></DIV>", DEFAULT = "<div id=\"i\" foo=\"\" name=\"\"></div>")
    public void getInnerHTML_EmptyAttributes() throws Exception {
        final String html = "<body onload='alert(document.body.innerHTML)'><div id='i' foo='' name=''></div></body>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <B>Old innerHTML</B>", "New = New cell value" },
            DEFAULT = { "Old = <b>Old innerHTML</b>", "New = New  cell value" })
    public void getSetInnerHTMLSimple_FF() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       alert('Old = ' + myNode.innerHTML);\n"
            + "       myNode.innerHTML = 'New  cell value';\n"
            + "       alert('New = ' + myNode.innerHTML);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode'><b>Old innerHTML</b></p>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test the use of innerHTML to set new HTML code.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {
            "Old = <B>Old innerHTML</B><!-- old comment -->",
            "New = <B><I id=newElt>New cell value</I></B>",
            "I" },
            DEFAULT = {
            "Old = <b>Old innerHTML</b><!-- old comment -->",
            "New =  <b><i id=\"newElt\">New cell value</i></b>",
             "I" })
    public void getSetInnerHTMLComplex_FF() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       alert('Old = ' + myNode.innerHTML);\n"
            + "       myNode.innerHTML = ' <b><i id=\"newElt\">New cell value</i></b>';\n"
            + "       alert('New = ' + myNode.innerHTML);\n"
            + "       alert(document.getElementById('newElt').tagName);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode'><b>Old innerHTML</b><!-- old comment --></p>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPageWithAlerts(html);

        final HtmlElement pElt = page.getHtmlElementById("myNode");
        assertEquals("p", pElt.getNodeName());
        final HtmlElement elt = page.getHtmlElementById("newElt");
        assertEquals("New cell value", elt.asText());
        assertEquals(1, page.getWebClient().getWebWindows().size());
    }

    /**
     * Test the use of innerHTML to set a new input.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getSetInnerHTMLNewInput() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       myNode.innerHTML = '<input type=\"checkbox\" name=\"myCb\" checked>';\n"
            + "       alert(myNode.myCb.checked);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<form id='myNode'></form>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {
            "Old = <B>Old innerHTML</B>",
            "New = New cell value &amp; \u0110 \u0110" },
            DEFAULT = {
            "Old = <b>Old innerHTML</b>",
            "New = New  cell value &amp; \u0110 \u0110" })
    public void getSetInnerHTMLChar_FF() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       alert('Old = ' + myNode.innerHTML);\n"
            + "       myNode.innerHTML = 'New  cell value &amp; \\u0110 &#272;';\n"
            + "       alert('New = ' + myNode.innerHTML);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode'><b>Old innerHTML</b></p>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that empty tags are not abbreviated into their &lt;tag/&gt; form.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "<DIV id=div><UL></UL></DIV>", "<UL></UL>", "" },
            CHROME = { "<div id=\"div\"><ul></ul></div>", "<ul></ul>", "" },
            FF = { "undefined", "<ul></ul>", "undefined" })
    public void getSetInnerHtmlEmptyTag_FF() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      var div = document.getElementById('div');\n"
            + "      alert(div.outerHTML);\n"
            + "      alert(div.innerHTML);\n"
            + "      alert(div.innerText);\n"
            + "   }\n"
            + "</script>\n"
            + "<div id='div'><ul/></div>"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that attributes containing whitespace are always quoted.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "<DIV id=div><SPAN class=\"a b\"></SPAN></DIV>", "<SPAN class=\"a b\"></SPAN>", "" },
            CHROME = { "<div id=\"div\"><span class=\"a b\"></span></div>", "<span class=\"a b\"></span>", "" },
            DEFAULT = { "undefined", "<span class=\"a b\"></span>", "undefined" })
    public void getSetInnerHtmlAttributeWithWhitespace_FF() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      var div = document.getElementById('div');\n"
            + "      alert(div.outerHTML);\n"
            + "      alert(div.innerHTML);\n"
            + "      alert(div.innerText);\n"
            + "   }\n"
            + "</script>\n"
            + "<div id='div'><span class='a b'></span></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting innerHTML to empty string.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Empty ChildrenLength: 0")
    public void setInnerHTMLEmpty() throws Exception {
        final String html = "<html><head></head><body>\n"
                + "<div id='testDiv'>foo</div>\n"
                + "<script language='javascript'>\n"
                + "    var node = document.getElementById('testDiv');\n"
                + "    node.innerHTML = '';\n"
                + "    alert('Empty ChildrenLength: ' + node.childNodes.length);\n"
                + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting innerHTML to null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "Null ChildrenLength: 1", DEFAULT = "Null ChildrenLength: 0")
    public void setInnerHTMLNull() throws Exception {
        final String html = "<html><head></head><body>\n"
                + "<div id='testDiv'>foo</div>\n"
                + "<script language='javascript'>\n"
                + "    var node = document.getElementById('testDiv');\n"
                + "    node.innerHTML = null;\n"
                + "    alert('Null ChildrenLength: ' + node.childNodes.length);\n"
                + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <B id=innerNode>Old outerHTML</B>", "New = New cell value" },
            CHROME = { "Old = <b id=\"innerNode\">Old outerHTML</b>", "New = New  cell value" },
            FF = { "Old = undefined", "New = <b id=\"innerNode\">Old outerHTML</b>" })
    public void getSetOuterHTMLSimple() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       var innerNode = document.getElementById('innerNode');\n"
            + "       alert('Old = ' + innerNode.outerHTML);\n"
            + "       innerNode.outerHTML = 'New  cell value';\n"
            + "       alert('New = ' + myNode.innerHTML);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode'><b id='innerNode'>Old outerHTML</b></p>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test the use of outerHTML to set new HTML code.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts(IE = { "Old = <B id=innerNode>Old outerHTML</B>", "New = <B><I id=newElt>New cell value</I></B>", "I" })
    public void getSetOuterHTMLComplex() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       var innerNode = document.getElementById('innerNode');\n"
            + "       alert('Old = ' + innerNode.outerHTML);\n"
            + "       innerNode.outerHTML = ' <b><i id=\"newElt\">New cell value</i></b>';\n"
            + "       alert('New = ' + myNode.innerHTML);\n"
            + "       alert(document.getElementById('newElt').tagName);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode'><b id='innerNode'>Old outerHTML</b></p>\n"
            + "</body>\n"
            + "</html>";
        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlElement pElt = page.getHtmlElementById("myNode");
        assertEquals("p", pElt.getNodeName());
        final HtmlElement elt = page.getHtmlElementById("newElt");
        assertEquals("New cell value", elt.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "outside", "1", "middle", "2", "3", "4" })
    public void insertAdjacentHTML() throws Exception {
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
        final String html = "<html><head><title>First</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  var oNode = document.getElementById('middle');\n"
                + "  oNode.insertAdjacentHTML('" + beforeEnd + "', ' <span id=3>before end</span> ');\n"
                + "  oNode.insertAdjacentHTML('" + afterEnd + "', ' <span id=4>after end</span> ');\n"
                + "  oNode.insertAdjacentHTML('" + beforeBegin + "', ' <span id=1>before begin</span> ');\n"
                + "  oNode.insertAdjacentHTML('" + afterBegin + "', ' <span id=2>after begin</span> ');\n"
                + "  var coll = document.getElementsByTagName('SPAN');\n"
                + "  for (var i=0; i<coll.length; i++) {\n"
                + "    alert(coll[i].id);\n"
                + "  }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "<span id='outside' style='color: #00ff00'>\n"
                + "<span id='middle' style='color: #ff0000'>\n"
                + "inside\n"
                + "</span>\n"
                + "</span>\n"
                + "</body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlElement elt = page.getHtmlElementById("outside");
        assertEquals("before begin after begin inside before end after end", elt.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "outside", "1", "middle", "2", "3", "4" })
    public void insertAdjacentElement() throws Exception {
        insertAdjacentElement("beforeEnd", "afterEnd", "beforeBegin", "afterBegin");
        insertAdjacentElement("BeforeEnd", "AfterEnd", "BeFoReBeGiN", "afterbegin");
    }

    private void insertAdjacentElement(final String beforeEnd,
            final String afterEnd, final String beforeBegin, final String afterBegin) throws Exception {
        final String html = "<html><head><title>First</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  var oNode = document.getElementById('middle');\n"
                + "  oNode.insertAdjacentElement('" + beforeEnd + "', makeElement(3, 'before end'));\n"
                + "  oNode.insertAdjacentElement('" + afterEnd + "', makeElement(4, ' after end'));\n"
                + "  oNode.insertAdjacentElement('" + beforeBegin + "', makeElement(1, 'before begin '));\n"
                + "  oNode.insertAdjacentElement('" + afterBegin + "', makeElement(2, ' after begin'));\n"
                + "  var coll = document.getElementsByTagName('SPAN');\n"
                + "  for (var i=0; i<coll.length; i++) {\n"
                + "    alert(coll[i].id);\n"
                + "  }\n"
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
                + "<span id='outside' style='color: #00ff00'>\n"
                + "<span id='middle' style='color: #ff0000'>\n"
                + "inside\n"
                + "</span>\n"
                + "</span>\n"
                + "</body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlElement elt = page.getHtmlElementById("outside");
        assertEquals("before begin after begin inside before end after end", elt.asText());
    }

    /**
     * Test the <tt>#default#clientCaps</tt> default IE behavior.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts(IE = {
            "body.cpuClass = undefined",
            "body.cpuClass = x86",
            "body.cpuClass = undefined" })
    public void addBehaviorDefaultClientCaps() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "       var body = document.body;\n"
            + "       alert('body.cpuClass = ' + body.cpuClass);\n"
            + "       var id = body.addBehavior('#default#clientCaps');\n"
            + "       alert('body.cpuClass = ' + body.cpuClass);\n"
            + "       var id2 = body.addBehavior('#default#clientCaps');\n"
            + "       body.removeBehavior(id);\n"
            + "       alert('body.cpuClass = ' + body.cpuClass);\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>Test</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test the <tt>#default#homePage</tt> default IE behavior.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts(IE = { "isHomePage = false", "isHomePage = true", "isHomePage = true", "isHomePage = false" })
    public void addBehaviorDefaultHomePage() throws Exception {
        final URL url1 = URL_FIRST;
        final URL url2 = URL_SECOND;
        final String html1 =
              "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "       // Test adding the behavior via script. Note that the URL\n"
            + "       // used to test must be part of the SAME domain as this\n"
            + "       // document, otherwise isHomePage() always returns false.\n"
            + "       var body = document.body;\n"
            + "       body.addBehavior('#default#homePage');\n"
            + "       var url = '" + url1 + "';\n"
            + "       alert('isHomePage = ' + body.isHomePage(url));\n"
            + "       body.setHomePage(url);\n"
            + "       alert('isHomePage = ' + body.isHomePage(url));\n"
            + "       // Test behavior added via style attribute.\n"
            + "       // Also test case-insensitivity of default behavior names.\n"
            + "       alert('isHomePage = ' + hp.isHomePage(url));\n"
            + "       // Make sure that (as mentioned above) isHomePage() always\n"
            + "       // returns false when the url specified is the actual\n"
            + "       // homepage, but the document checking is on a DIFFERENT domain.\n"
            + "       hp.setHomePage('" + url2 + "');\n"
            + "       alert('isHomePage = ' + hp.isHomePage(url));\n"
            + "       // Test navigation to homepage.\n"
            + "       body.navigateHomePage();\n"
            + "    }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <span id='hp' style='behavior:url(#default#homepage)'></span>\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = "<html></html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(url1, html1);
        webConnection.setResponse(url2, html2);
        client.setWebConnection(webConnection);
        final HtmlPage page = client.getPage(url1);
        assertEquals(getExpectedAlerts(), collectedAlerts);
        assertEquals(url2.toExternalForm(), page.getWebResponse().getWebRequest().getUrl().toExternalForm());
    }

    /**
    * Test the <tt>#default#download</tt> default IE behavior.
    *
    * @throws Exception if the test fails
    */
    @Test
    @Browsers(Browser.IE)
    @Alerts(IE = { "Refused", "foo" })
    public void addBehaviorDefaultDownload() throws Exception {
        final URL url1 = new URL("http://htmlunit.sourceforge.net/");
        final URL url2 = new URL("http://htmlunit.sourceforge.net/test.txt");
        // The download behavior doesn't accept downloads from a different domain ...
        final URL url3 = new URL("http://www.domain2.com/test.txt");

        final String html1 =
             "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "      try {\n"
            + "        hp.startDownload('http://www.domain2.com/test.txt', callback);\n"
            + "      }\n"
            + "      catch (e)\n"
            + "      {\n"
            + "        alert('Refused');\n"
            + "      }\n"
            + "      hp.startDownload('test.txt', callback);\n"
            + "    }\n"
            + "    function callback(content) {\n"
            + "      alert(content);\n"
            + "    }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <span id='hp' style='behavior:url(#default#download)'></span>\n"
            + "  </body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(url1, html1);
        webConnection.setResponse(url2, "foo");
        webConnection.setResponse(url3, "foo2");
        client.setWebConnection(webConnection);
        client.getPage(url1);

        final String[] expectedAlerts = getExpectedAlerts();
        final int waitTime = 50;
        final int maxTime = 1000;
        for (int time = 0; time < maxTime; time += waitTime) {
            if (expectedAlerts.length <= collectedAlerts.size()) {
                assertEquals(expectedAlerts, collectedAlerts);
                return;
            }
            Thread.sleep(waitTime);
        }
        fail("Unable to collect expected alerts within " + maxTime + "ms; collected alerts: " + collectedAlerts);
    }

   /**
     * Test the removal of behaviors.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "body.isHomePage = undefined", "body.isHomePage = false", "body.isHomePage = undefined" })
    public void removeBehavior() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "       var body = document.body;\n"
            + "       alert('body.isHomePage = ' + body.isHomePage);\n"
            + "       var id = body.addBehavior('#default#homePage');\n"
            + "       alert('body.isHomePage = ' + body.isHomePage('not the home page'));\n"
            + "       body.removeBehavior(id);\n"
            + "       alert('body.isHomePage = ' + body.isHomePage);\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>Test</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "BR", "DIV", "2", "3" })
    public void children() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "    var oDiv = document.getElementById('myDiv');\n"
            + "    for (var i=0; i<oDiv.children.length; i++) \n"
            + "        alert(oDiv.children(i).tagName);\n"
            + "    var oCol = oDiv.children;\n"
            + "    alert(oCol.length);\n"
            + "    oDiv.insertAdjacentHTML('beforeEnd', '<br>');\n"
            + "    alert(oCol.length);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'><br/><div><span>test</span></div></div>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnclick() throws Exception {
        eventHandlerSetterGetterTest("onclick");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOndblclick() throws Exception {
        eventHandlerSetterGetterTest("ondblclick");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnblur() throws Exception {
        eventHandlerSetterGetterTest("onblur");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnfocus() throws Exception {
        eventHandlerSetterGetterTest("onfocus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnkeydown() throws Exception {
        eventHandlerSetterGetterTest("onkeydown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnkeypress() throws Exception {
        eventHandlerSetterGetterTest("onkeypress");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnkeyup() throws Exception {
        eventHandlerSetterGetterTest("onkeyup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnmousedown() throws Exception {
        eventHandlerSetterGetterTest("onmousedown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnmouseup() throws Exception {
        eventHandlerSetterGetterTest("onmouseup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnmouseover() throws Exception {
        eventHandlerSetterGetterTest("onmouseover");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnmouseout() throws Exception {
        eventHandlerSetterGetterTest("onmouseout");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnmousemove() throws Exception {
        eventHandlerSetterGetterTest("onmousemove");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnresize() throws Exception {
        eventHandlerSetterGetterTest("onresize");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnerror() throws Exception {
        eventHandlerSetterGetterTest("onerror");
    }

    /**
     * @param eventName the name of the event
     * @throws Exception if the test fails
     */
    private void eventHandlerSetterGetterTest(final String eventName) throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function handler(event) {}\n"
            + "function test() {\n"
            + "    var oDiv = document.getElementById('myDiv');\n"
            + "    oDiv." + eventName + " = handler;\n"
            + "    if (oDiv." + eventName + " == handler) {\n"
            + "        alert('success');\n"
            + "    } else {\n"
            + "        alert('fail');\n"
            + "    }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'><br/><div><span>test</span></div></div>\n"
            + "</body>\n"
            + "</html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"success"};
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlElement div = page.getHtmlElementById("myDiv");

        assertTrue("Event handler was not set", div.hasEventHandlers(eventName));
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = Old \r\ninnerText", "New = New cell value" },
            CHROME = { "Old = Old\ninnerText", "New = New cell value" },
            FF = { "Old = undefined", "New = New cell value" })
    public void getSetInnerTextSimple() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       alert('Old = ' + myNode.innerText);\n"
            + "       myNode.innerText = 'New cell value';\n"
            + "       alert('New = ' + myNode.innerText);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='myNode'><b>Old <p>innerText</p></b></div>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickHashAnchor() throws Exception {
        final String html
            = "<html><head><title>HashAnchor</title></head>\n"
            + "<body>\n"
            + "  <script language='javascript'>\n"
            + "    function test() {alert('test hash');}\n"
            + "  </script>\n"
            + "  <a onClick='javascript:test();' href='#' name='hash'>Click</a>\n"
            + "</body>\n"
            + "</html>";
        final String[] expectedAlerts = {"test hash"};
        // first use direct load
        final List<String> loadCollectedAlerts = new ArrayList<String>();
        final HtmlPage loadPage = loadPage(getBrowserVersion(), html, loadCollectedAlerts);
        final HtmlAnchor loadHashAnchor = loadPage.getAnchorByName("hash");
        loadHashAnchor.click();

        assertEquals(expectedAlerts, loadCollectedAlerts);

        // finally try via the client
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        webClient.setWebConnection(webConnection);
        final CollectingAlertHandler clientCollectedAlertsHandler = new CollectingAlertHandler();
        webClient.setAlertHandler(clientCollectedAlertsHandler);
        final HtmlPage clientPage = webClient.getPage(URL_FIRST);
        final HtmlAnchor clientHashAnchor = clientPage.getAnchorByName("hash");
        clientHashAnchor.click();

        assertEquals(expectedAlerts, clientCollectedAlertsHandler.getCollectedAlerts());
    }

    /**
     * Test the removal of attributes from HTMLElements.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "removeMe", "null" })
    public void removeAttribute() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "       var myDiv = document.getElementById('aDiv');\n"
            + "       alert(myDiv.getAttribute('name'));\n"
            + "       myDiv.removeAttribute('name');\n"
            + "       alert(myDiv.getAttribute('name'));\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'><div id='aDiv' name='removeMe'>\n"
            + "</div></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * IE doesn't really make a distinction between property and attribute...
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "hello", "null", "hello" }, IE = { "hello", "hello", "undefined" })
    public void removeAttribute_property() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "       var myDiv = document.getElementById('aDiv');\n"
            + "       myDiv.foo = 'hello';\n"
            + "       alert(myDiv.foo);\n"
            + "       alert(myDiv.getAttribute('foo'));\n"
            + "       myDiv.removeAttribute('foo');\n"
            + "       alert(myDiv.foo);\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'><div id='aDiv' name='removeMe'>\n"
            + "</div></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test scrolls (real values don't matter currently).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "number", "number", "number", "number", "number", "number", "number", "number" })
    public void scrolls() throws Exception {
        final String html = "<html>\n"
              + "<head>\n"
              + "  <title>Test</title>\n"
              + "</head>\n"
              + "<body>\n"
              + "</div></body>\n"
              + "<div id='div1'>foo</div>\n"
              + "<script>\n"
              + "function alertScrolls(_oElt) {\n"
              + "  alert(typeof _oElt.scrollHeight);\n"
              + "  alert(typeof _oElt.scrollWidth);\n"
              + "  alert(typeof _oElt.scrollLeft);\n"
              + "  _oElt.scrollLeft = 123;\n"
              + "  alert(typeof _oElt.scrollTop);\n"
              + "  _oElt.scrollTop = 123;\n"
              + "}\n"
              + "alertScrolls(document.body);\n"
              + "alertScrolls(document.getElementById('div1'));\n"
              + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "0", "0", "0", "0", "0", "17", "0", "0" })
    public void scrollLeft_overflowScroll() throws Exception {
        scrollLeft("scroll");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "0", "0", "0", "0", "0", "17", "0", "0" })
    public void scrollLeft_overflowAuto() throws Exception {
        scrollLeft("auto");
    }

    /**
     * NOTE: When running this test with Firefox (3.6, at least), it's important to reload the page with Ctrl+F5
     * in order to completely clear the cache; otherwise, Firefox appears to incorrectly cache some style attributes.
     * @throws Exception if an error occurs
     */
    private void scrollLeft(final String overflow) throws Exception {
        final String html
            = "<html><body onload='test()'>\n"
            + "<div id='d1' style='width:100px;height:100px;background-color:green;'>\n"
            + "  <div id='d2' style='width:50px;height:50px;background-color:blue;'></div>\n"
            + "</div>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var d1 = document.getElementById('d1'), d2 = document.getElementById('d2');\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d1.scrollLeft = -1;\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d1.scrollLeft = 5;\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d2.style.width = '200px';\n"
            + "  d2.style.height = '200px';\n"
            + "  d1.scrollLeft = 7;\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d1.style.overflow = '" + overflow + "';\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d1.scrollLeft = 17;\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d1.style.overflow = 'visible';\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d1.scrollLeft = 19;\n"
            + "  alert(d1.scrollLeft);\n"
            + "}\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "0", "0", "0", "0", "0", "17", "0", "0" })
    public void scrollTop_overflowScroll() throws Exception {
        scrollTop("scroll");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "0", "0", "0", "0", "0", "17", "0", "0" })
    public void scrollTop_overflowAuto() throws Exception {
        scrollTop("auto");
    }

    /**
     * NOTE: When running this test with Firefox (3.6, at least), it's important to reload the page with Ctrl+F5
     * in order to completely clear the cache; otherwise, Firefox appears to incorrectly cache some style attributes.
     * @throws Exception if an error occurs
     */
    private void scrollTop(final String overflow) throws Exception {
        final String html
            = "<html><body onload='test()'>\n"
            + "<div id='d1' style='width:100px;height:100px;background-color:green;'>\n"
            + "  <div id='d2' style='width:50px;height:50px;background-color:blue;'></div>\n"
            + "</div>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var d1 = document.getElementById('d1'), d2 = document.getElementById('d2');\n"
            + "  alert(d1.scrollTop);\n"
            + "  d1.scrollTop = -1;\n"
            + "  alert(d1.scrollTop);\n"
            + "  d1.scrollTop = 5;\n"
            + "  alert(d1.scrollTop);\n"
            + "  d2.style.width = '200px';\n"
            + "  d2.style.height = '200px';\n"
            + "  d1.scrollTop = 7;\n"
            + "  alert(d1.scrollTop);\n"
            + "  d1.style.overflow = '" + overflow + "';\n"
            + "  alert(d1.scrollTop);\n"
            + "  d1.scrollTop = 17;\n"
            + "  alert(d1.scrollTop);\n"
            + "  d1.style.overflow = 'visible';\n"
            + "  alert(d1.scrollTop);\n"
            + "  d1.scrollTop = 19;\n"
            + "  alert(d1.scrollTop);\n"
            + "}\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests that JavaScript scrollIntoView() function doesn't fail.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ok")
    public void scrollIntoView() throws Exception {
        final String html = "<html>\n"
              + "<body>\n"
              + "<script id='me'>document.getElementById('me').scrollIntoView(); alert('ok');</script>\n"
              + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests the offsetParent property.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({
            "element: span3 offsetParent: td2", "element: td2 offsetParent: table2",
            "element: tr2 offsetParent: table2", "element: table2 offsetParent: td1",
            "element: td1 offsetParent: table1", "element: tr1 offsetParent: table1",
            "element: table1 offsetParent: body1", "element: span2 offsetParent: body1",
            "element: span1 offsetParent: body1", "element: div1 offsetParent: body1",
            "element: body1 offsetParent: null" })
    public void offsetParent_Basic() throws Exception {
        final String html = "<html><head>\n"
            + "<script type='text/javascript'>\n"
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
            + "  alert(alertMessage);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * Tests the offsetParent property.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "null", "null" }, IE = { "exception", "null" })
    public void offsetParent_newElement() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "try {\n"
            + "  var oNew = document.createElement('span');\n"
            + "  alert(oNew.offsetParent);\n"
            + "} catch(e) { alert('exception') }\n"
            + "var fragment = document.createDocumentFragment();\n"
            + "fragment.appendChild(oNew);\n"
            + "alert(oNew.offsetParent);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests the offsetParent property, including the effects of CSS "position" attributes.
     * Based on <a href="http://dump.testsuite.org/2006/dom/style/offset/spec#offsetparent">this work</a>.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "null", "body", "body", "body", "body", "body",
            "body", "body", "h1", "i1", "td", "td", "td", "body", "body" },
            FF = { "null", "body", "body", "body", "body", "body",
            "f1", "body", "h1", "i1", "td", "body", "td", "body", "body" },
            CHROME = { "null", "body", "exception" })
    public void offsetParent_WithCSS() throws Exception {
        final String html = "<html>\n"
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
            + "      function test() {\n"
            + "      try {\n"
            + "                                                              // FF   IE   \n"
            + "        alert(document.getElementById('body').offsetParent);  // null null \n"
            + "        alert(document.getElementById('a2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('b2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('c2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('d2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('e2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('f2').offsetParent.id); // f1   body \n"
            + "        alert(document.getElementById('g2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('h2').offsetParent.id); // h1   h1   \n"
            + "        alert(document.getElementById('i2').offsetParent.id); // i1   i1   \n"
            + "        alert(document.getElementById('j2').offsetParent.id); // td   td   \n"
            + "        alert(document.getElementById('k2').offsetParent.id); // body td   \n"
            + "        alert(document.getElementById('l2').offsetParent.id); // td   td   \n"
            + "        alert(document.getElementById('m2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('n2').offsetParent.id); // body body \n"
            + "      } catch (e) { alert('exception'); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test for bug https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1960512&group_id=47038.
     * @throws Exception if the test fails
     */
    @Test
    public void offsetParent_withSelectors() throws Exception {
        final String html = "<html><head><style>\n"
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
    @Alerts(DEFAULT = { "undefined", "undefined", "undefined", "undefined",
            "undefined", "123", "from myFunction", "123", "from myFunction" },
            IE = { "undefined", "undefined", "undefined", "undefined", "exception" })
    public void prototype() throws Exception {
        final String html = "<html><head><title>Prototype test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "    var d = document.getElementById('foo');\n"
            + "    alert(d.foo);\n"
            + "    alert(d.myFunction);\n"
            + "    var link = document.getElementById('testLink');\n"
            + "    alert(link.foo);\n"
            + "    alert(link.myFunction);\n"
            + "    HTMLElement.prototype.foo = 123;\n"
            + "    alert(HTMLElement.foo);\n"
            + "    HTMLElement.prototype.myFunction = function() { return 'from myFunction'; };\n"
            + "    alert(d.foo);\n"
            + "    alert(d.myFunction());\n"
            + "    alert(link.foo);\n"
            + "    alert(link.myFunction());\n"
            + "} catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script></head><body onload='test()''>\n"
            + "<div id='foo'>bla</div>\n"
            + "<a id='testLink' href='foo'>bla</a>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * 'Element' and 'HTMLElement' prototypes are synonyms.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "in selectNodes", IE = "exception")
    public void prototype_Element() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "try {\n"
            + "    Element.prototype.selectNodes = function(sExpr){\n"
            + "      alert('in selectNodes');\n"
            + "    }\n"
            + "    document.getElementById('myDiv').selectNodes();\n"
            + "} catch (e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true" }, IE = "exception")
    public void instanceOf() throws Exception {
        final String html = "<html><head><title>instanceof test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "    var d = document.getElementById('foo');\n"
            + "    alert(d instanceof HTMLDivElement);\n"
            + "    var link = document.getElementById('testLink');\n"
            + "    alert(link instanceof HTMLAnchorElement);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script></head><body onload='test()''>\n"
            + "<div id='foo'>bla</div>\n"
            + "<a id='testLink' href='foo'>bla</a>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "null", "[object]" },
            FF3 = { "undefined", "undefined" },
            FF3_6 = { "undefined", "undefined" },
            DEFAULT = { "null", "[object HTMLBodyElement]" })
    public void parentElement() throws Exception {
        final String html
            = "<html id='htmlID'>\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div id='divID'/>\n"
            + "<script language=\"javascript\">\n"
            + "    alert(document.getElementById('htmlID').parentElement);\n"
            + "    alert(document.getElementById('divID' ).parentElement);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(0, 0, 0)")
    @Browsers(Browser.IE)
    public void currentStyle() throws Exception {
        style("currentStyle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    @NotYetImplemented(Browser.FF)
    public void runtimeStyle() throws Exception {
        style("runtimeStyle");
    }

    private void style(final String styleProperty) throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myDiv')." + styleProperty + ".color);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({ "0", "0" })
    @Test
    public void clientLeftTop() throws Exception {
        final String html = "<html><body>"
            + "<div id='div1'>hello</div><script>\n"
            + "  var d1 = document.getElementById('div1');\n"
            + "  alert(d1.clientLeft);\n"
            + "  alert(d1.clientTop);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Another nice feature of the IE.
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "0", "0" }, IE = { "2", "2" })
    @Test
    public void clientLeftTop_documentElement() throws Exception {
        final String html =
              "<!DOCTYPE HTML "
            +      "PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"
            + "<html>\n"
            + "<body>"
            + "<div id='div1'>hello</div><script>\n"
            + "  var d1 = document.documentElement;\n"
            + "  alert(d1.clientLeft);\n"
            + "  alert(d1.clientTop);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "4", "4" }, IE = { "0", "0" })
    @Test
    public void clientLeftTopWithBorder() throws Exception {
        final String html = "<html><body>"
            + "<div id='div1' style='border: 4px solid black;'>hello</div><script>\n"
            + "  var d1 = document.getElementById('div1');\n"
            + "  alert(d1.clientLeft);\n"
            + "  alert(d1.clientTop);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = "[object ClientRect]", IE = "[object]")
    @Test
    public void getBoundingClientRect() throws Exception {
        final String html = "<html><body><div id='div1'>hello</div><script>\n"
            + "try {\n"
            + "  var d1 = document.getElementById('div1');\n"
            + "  var pos = d1.getBoundingClientRect();\n"
            + "  alert(pos);\n"
            + "} catch (e) { alert('exception');}\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "400", "100" }, IE = { "402", "102" })
    @Test
//    @NotYetImplemented({ Browser.FF, Browser.CHROME })
    public void getBoundingClientRect2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var pos = d1.getBoundingClientRect();\n"
            + "    alert(pos.left);\n"
            + "    alert(pos.top);\n"
            + "    } catch (e) { alert('exception');}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='outer' style='position: absolute; left: 400px; top: 100px; width: 50px; height: 80px;'>"
            + "<div id='div1'></div></div>"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "0", "100", "100", "50" }, IE = { "2", "102", "102", "52" })
    @Test
    public void getBoundingClientRect_Scroll() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var d1 = document.getElementById('outer');\n"
            + "      d1.scrollTop=150;\n"
            + "      var pos = d1.getBoundingClientRect();\n"
            + "      alert(pos.left);\n"
            + "      alert(pos.top);\n"

            + "      d1 = document.getElementById('div1');\n"
            + "      pos = d1.getBoundingClientRect();\n"
            + "      alert(pos.left);\n"
            + "      alert(pos.top);\n"
            + "    } catch (e) { alert('exception');}\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.FF)
    public void getClientRects() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    d1.getClientRects();\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "null", "#document-fragment" }, DEFAULT = { "null", "null" })
    public void innerHTML_parentNode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var div1 = document.createElement('div');\n"
            + "    alert(div1.parentNode);\n"
            + "    div1.innerHTML='<p>hello</p>';\n"
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
    @Alerts(IE = { "null", "#document-fragment" }, DEFAULT = { "null", "null" })
    public void innerText_parentNode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var div1 = document.createElement('div');\n"
            + "    alert(div1.parentNode);\n"
            + "    div1.innerText='<p>hello</p>';\n"
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
    @Alerts(IE = { "false", "true", "false" }, DEFAULT = { "true", "true", "true" })
    public void uniqueID() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     var div2 = document.getElementById('div2');\n"
            + "     alert(div1.uniqueID == undefined);\n"
            + "     alert(div1.uniqueID == div1.uniqueID);\n"
            + "     alert(div1.uniqueID == div2.uniqueID);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "  <div id='div2'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests if element.uniqueID starts with 'ms__id', and is lazily created.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "true", "true" })
    public void uniqueIDFormatIE() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     var div2 = document.getElementById('div2');\n"
            + "     var id2 = div2.uniqueID;\n"
            + "     //as id2 is retrieved before getting id1, id2 should be < id1;\n"
            + "     var id1 = div1.uniqueID;\n"
            + "     alert(id1.substring(0, 6) == 'ms__id');\n"
            + "     var id1Int = parseInt(id1.substring(6, id1.length));\n"
            + "     var id2Int = parseInt(id2.substring(6, id2.length));\n"
            + "     alert(id2Int < id1Int);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "  <div id='div2'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests setAttribute() with name of event handler.
     * @throws Exception if the test fails
     */
    @Test
    public void setAttribute_eventHandler() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('login');\n"
            + "    var password = document.getElementById('password');\n"
            + "    text.setAttribute('onfocus', \"alert('onfocus1');\");\n"
            + "    text.setAttribute('onblur', \"alert('onblur1');\");\n"
            + "    password.setAttribute('onfocus', \"alert('onfocus2');\");\n"
            + "    password.setAttribute('onblur', \"alert('onblur2');\");\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form>\n"
            + "    <input type='text' id='login' name='login'>\n"
            + "    <input type='password' id='password' name='password'>\n"
            + "  </form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlForm form = page.getForms().get(0);
        final HtmlTextInput inputLogin = form.getInputByName("login");
        final HtmlPasswordInput inputPassword = form.getInputByName("password");

        inputLogin.focus();
        inputPassword.focus();

        final int expectedAlertNumber;
        if (getBrowserVersion().isIE()) {
            expectedAlertNumber = 0;
        }
        else {
            expectedAlertNumber = 3;
        }
        assertEquals(expectedAlertNumber, collectedAlerts.size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    public void fireEvent_WithoutTemplate() throws Exception {
        final String html =
              "<html><body>\n"
            + "<div id='a' onclick='alert(\"clicked\")'>foo</div>\n"
            + "<div id='b' onmouseover='document.getElementById(\"a\").fireEvent(\"onclick\")'>bar</div>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, actual);
        page.<HtmlDivision>getHtmlElementById("a").click();
        page.<HtmlDivision>getHtmlElementById("b").mouseOver();
        final String[] expected = {"clicked", "clicked"};
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    public void fireEvent_WithTemplate() throws Exception {
        final String html =
              "<html><body>\n"
            + "<script>var template = document.createEventObject();</script>\n"
            + "<script>function doAlert(e) { alert(e.type); }</script>\n"
            + "<div id='a' onclick='doAlert(event)'>foo</div>\n"
            + "<div id='b' onmouseover='document.getElementById(\"a\").fireEvent(\"onclick\")'>bar</div>\n"
            + "<div id='c' onmouseover='document.getElementById(\"a\").fireEvent(\"onclick\", template)'>baz</div>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, actual);
        page.<HtmlDivision>getHtmlElementById("a").click();
        page.<HtmlDivision>getHtmlElementById("b").mouseOver();
        page.<HtmlDivision>getHtmlElementById("c").mouseOver();
        final String[] expected = {"click", "click", "click"};
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(Browser.FF)
    public void setExpression() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     div1.setExpression('title','id');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(Browser.FF)
    public void removeExpression() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     div1.setExpression('title','id');\n"
            + "     div1.removeExpression('title');"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "clicked", IE = "exception")
    public void dispatchEvent() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function foo() {\n"
            + "try {\n"
            + "    var e = document.createEvent('MouseEvents');\n"
            + "    e.initMouseEvent('click', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);\n"
            + "    var d = document.getElementById('d');\n"
            + "    var canceled = !d.dispatchEvent(e);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='foo()'><div id='d' onclick='alert(\"clicked\")'>foo</div></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "false" }, IE = "exception")
    public void hasAttribute() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "try {\n"
            + "    var elt = document.body;\n"
            + "    alert(elt.hasAttribute('onload'));\n"
            + "    alert(elt.hasAttribute('onLoad'));\n"
            + "    alert(elt.hasAttribute('foo'));\n"
            + "} catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dispatchEvent2() throws Exception {
        testHTMLFile("HTMLElementTest_dispatchEvent2.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "undefined", "x86", "0", "undefined" })
    public void getComponentVersion() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  alert(document.body.cpuClass);\n"
            + "  document.body.style.behavior='url(#default#clientCaps)';\n"
            + "  alert(document.body.cpuClass);\n"
            + "  var ver=document.body.getComponentVersion('{E5D12C4E-7B4F-11D3-B5C9-0050045C3C96}','ComponentID');\n"
            + "  alert(ver.length);\n"
            + "  document.body.style.behavior='';\n"
            + "  alert(document.body.cpuClass);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "30", "40" }, DEFAULT = { "36", "46" })
    public void clientWidthAndHeight() throws Exception {
        final String html =
              "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var myDiv = document.getElementById('myDiv');\n"
            + "    alert(myDiv.clientWidth);\n"
            + "    alert(myDiv.clientHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "<style>#myDiv { width:30px; height:40px; padding:3px; border:5px; margin:7px; }</style>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({"body1", "button1", "text1", "[object]", "onfocus text2", "text2", "onfocus text1", "onfocus text2" })
    public void setActiveAndFocus() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstHtml = "<html><head><title>First</title>\n"
            + "<script>var win2;</script></head>\n"
            + "<body id='body1' onload='alert(document.activeElement.id)'><form name='form1'>\n"
            + "<input id='text1' onfocus='alert(\"onfocus text1\");win2.focus();'>\n"
            + "<button id='button1' onClick='win2=window.open(\"" + URL_SECOND + "\");'>Click me</a>\n"
            + "</form></body></html>";
        webConnection.setResponse(URL_FIRST, firstHtml);

        final String secondHtml = "<html><head><title>Second</title></head>\n"
            + "<body id='body2'>\n"
            + "<input id='text2' onfocus='alert(\"onfocus text2\")'>\n"
            + "<button id='button2' onClick='doTest();'>Click me</a>\n"
            + "<script>\n"
            + "     function doTest() {\n"
            + "         alert(opener.document.activeElement.id);\n"
            + "         opener.document.getElementById('text1').setActive();\n"
            + "         alert(opener.document.activeElement.id);\n"
            + "         alert(document.activeElement);\n"
            + "         document.getElementById('text2').setActive();\n"
            + "         alert(document.activeElement.id);\n"
            + "         opener.focus();\n"
            + "    }\n"
            + "</script></body></html>";
        webConnection.setResponse(URL_SECOND, secondHtml);

        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final HtmlButton button1 = firstPage.getHtmlElementById("button1");
        final HtmlPage secondPage = button1.click();
        assertEquals("Second", secondPage.getTitleText());
        secondPage.<HtmlButton>getHtmlElementById("button2").click();
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onfocus text1", "onfocus text2", "onfocus text1", "onfocus text2" })
    public void onFocusOnWindowFocusGain() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstHtml = "<html><head><title>First</title></head>\n"
                + "<body><form name='form1'>\n"
                + "<input id='text1' onfocus='alert(\"onfocus text1\")'>\n"
                + "<button type='button' id='clickme' onClick='window.open(\"" + URL_SECOND + "\");'>Click me</a>\n"
                + "</form></body></html>";
        webConnection.setResponse(URL_FIRST, firstHtml);

        final String secondHtml = "<html><head><title>Second</title></head>\n"
                + "<body onLoad='doTest()'>\n"
                + "<input id='text2' onfocus='alert(\"onfocus text2\")'>\n"
                + "<script>\n"
                + "     function doTest() {\n"
                + "         opener.document.getElementById('text1').focus();\n"
                + "         document.getElementById('text2').focus();\n"
                + "    }\n"
                + "</script></body></html>";

        webConnection.setResponse(URL_SECOND, secondHtml);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final HtmlButton buttonA = firstPage.getHtmlElementById("clickme");
        final HtmlPage secondPage = buttonA.click();
        assertEquals("Second", secondPage.getTitleText());
        webClient.setCurrentWindow(firstPage.getEnclosingWindow());
        webClient.setCurrentWindow(secondPage.getEnclosingWindow());
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onblur text2", "onblur text1" })
    public void onBlurOnWindowFocusChange() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstHtml = "<html><head><title>First</title></head>\n"
                + "<body><form name='form1'>\n"
                + "<input id='text1' onblur='alert(\"onblur text1\")'>\n"
                + "<button type='button' id='clickme' onClick='window.open(\"" + URL_SECOND + "\");'>Click me</a>\n"
                + "</form></body></html>";
        webConnection.setResponse(URL_FIRST, firstHtml);

        final String secondHtml = "<html><head><title>Second</title></head>\n"
                + "<body onLoad='doTest()'>\n"
                + "<input id='text2' onblur='alert(\"onblur text2\")'>\n"
                + "<script>\n"
                + "     function doTest() {\n"
                + "         opener.document.getElementById('text1').focus();\n"
                + "         document.getElementById('text2').focus();\n"
                + "    }\n"
                + "</script></body></html>";

        webConnection.setResponse(URL_SECOND, secondHtml);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final HtmlButton buttonA = firstPage.getHtmlElementById("clickme");
        final HtmlPage secondPage = buttonA.click();
        assertEquals("Second", secondPage.getTitleText());
        webClient.setCurrentWindow(firstPage.getEnclosingWindow());
        webClient.setCurrentWindow(secondPage.getEnclosingWindow());
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Regression test for https://sourceforge.net/tracker/?func=detail&aid=2022578&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void stackOverflowWithInnerHTML() throws Exception {
        final String html = "<html><head><title>Recursion</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "     document.body.innerHTML = unescape(document.body.innerHTML);\n"
            + "</script></body></html>";
        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals("Recursion", webDriver.getTitle());
    }

    /**
     * Document.write after setting innerHTML.
     * @throws Exception if the test fails
     */
    @Test
    public void setInnerHTMLDocumentWrite() throws Exception {
        final String html = "<html><head><title>test</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "     var a = document.createElement('a');\n"
            + "     a.innerHTML = 'break';\n"
            + "     document.write('hello');\n"
            + "</script></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("test" + LINE_SEPARATOR + "hello", page.asText());
    }

    /**
     * Test setting the class for the element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "x", "null", "[object Attr]", "null", "x", "byClassname" },
            IE = { "null", "x", "[object]", "null", "null", "byClassname" })
    public void class_className_attribute() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var e = document.getElementById('pid');\n"
            + "    alert(e.getAttribute('class'));\n"
            + "    alert(e.getAttribute('className'));\n"
            + "    alert(e.getAttributeNode('class'));\n"
            + "    alert(e.getAttributeNode('className'));\n"
            + "    e.setAttribute('className', 'byClassname');\n"
            + "    alert(e.getAttribute('class'));\n"
            + "    alert(e.getAttribute('className'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "-undefined-x", "null-x-null", "null-[object Attr]-null", "null-[object Attr]-null",
            "x-byClassname", "[object Attr]-[object Attr]", "byClassname-byClassname", "[object Attr]-[object Attr]" },
            IE = { "-undefined-x", "-null-x", "[object]-[object]-null", "[object]-[object]-null", "null-byClassname",
            "[object]-null", "byClassname-byClassname", "[object]-null" })
    public void class_className_attribute2() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var e = document.getElementById('pid');\n"
            + "  alert(e['lang'] + '-' + e['class'] + '-' + e['className']);\n"
            + "  alert(e.getAttribute('lang') + '-' + e.getAttribute('class') + '-' + e.getAttribute('className'));\n"
            + "  alert(e.getAttributeNode('lang') + '-' + e.getAttributeNode('class') + '-' + "
            + "e.getAttributeNode('className'));\n"
            + "  alert(e.attributes.getNamedItem('lang') + '-' + e.attributes.getNamedItem('class') + '-' + "
            + "e.attributes.getNamedItem('className'));\n"
            + "  e.setAttribute('className', 'byClassname');\n"
            + "  alert(e.getAttribute('class') + '-' + e.getAttribute('className'));\n"
            + "  alert(e.getAttributeNode('class') + '-' + e.getAttributeNode('className'));\n"
            + "  e.setAttribute('class', 'byClassname');\n"
            + "  alert(e.getAttribute('class') + '-' + e.getAttribute('className'));\n"
            + "  alert(e.getAttributeNode('class') + '-' + e.getAttributeNode('className'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3 = "exception", FF3_6 = "exception",
            DEFAULT = { "true", "true", "true", "false", "false", "false", "false", "true" })
    public void contains() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var div2 = document.getElementById('div2');\n"
            + "  var div3 = document.getElementById('div3');\n"
            + "  alert(div1.contains(div1));\n"
            + "  alert(div1.contains(div2));\n"
            + "  alert(div1.contains(div3));\n"
            + "  alert(div1.contains(div4));\n"
            + "  alert(div2.contains(div1));\n"
            + "  alert(div3.contains(div1));\n"
            + "  alert(div4.contains(div1));\n"
            + "  alert(div2.contains(div3));\n"
            + "} catch(e) { alert('exception'); }\n"
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
    @Alerts(DEFAULT = "undefined", IE = "defined")
    public void filters() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var defined = typeof(div1.filters) != 'undefined';\n" // "unknown" for IE6!!!
            + "  alert(defined ? 'defined' : 'undefined');\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'>\n"
            + "</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3 = { ">myClass<", "> myId  <" },
            DEFAULT = { "> myClass <", "> myId  <" })
    public void attributes_trimmed() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var div1 = document.body.firstChild;\n"
            + "  alert('>' + div1.className + '<');\n"
            + "  alert('>' + div1.id + '<');\n"
            + "}\n"
            + "</script></head><body onload='test()'>"
            + "<div id=' myId  ' class=' myClass '>\n"
            + "hello"
            + "</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function", "* => body: 0, div1: 0", "foo => body: 3, div1: 1", "foo red => body: 1, div1: 0",
            "red foo => body: 1, div1: 0", "blue foo => body: 0, div1: 0", "null => body: 0, div1: 0" },
            IE = { "undefined", "exception" })
    public void getElementsByClassName() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test(x) {\n"
            + "    var b = document.body;\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    var s = x + ' => body: ' + b.getElementsByClassName(x).length;\n"
            + "    s += ', div1: ' + div1.getElementsByClassName(x).length;\n"
            + "    alert(s);\n"
            + "}\n"
            + "function doTest() {\n"
            + "    var b = document.body;\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    alert(typeof document.body.getElementsByClassName);\n"
            + "    try {\n"
            + "      test('*');\n"
            + "      test('foo');\n"
            + "      test('foo red');\n"
            + "      test('red foo');\n"
            + "      test('blue foo');\n"
            + "      test(null);\n"
            + "    }\n"
            + "    catch (e) { alert('exception') }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div class='foo' id='div1'>\n"
            + "  <span class='c2'>hello</span>\n"
            + "  <span class='foo' id='span2'>World!</span>\n"
            + "</div>\n"
            + "<span class='foo red' id='span3'>again</span>\n"
            + "<span class='red' id='span4'>bye</span>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3 = { "undefined", "undefined" },
            FF3_6 = { "undefined", "undefined" },
            DEFAULT = { "null", "[object HTMLDivElement]" },
            IE = { "null", "[object]" })
    public void parentElement2() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var fragment = document.createDocumentFragment();\n"
            + "  var div = document.createElement('div');\n"
            + "  var bold = document.createElement('b');\n"
            + "  fragment.appendChild(div);\n"
            + "  div.appendChild(bold);\n"
            + "  alert(div.parentElement);\n"
            + "  alert(bold.parentElement);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * The method doScroll() should throw an exception if document is not yet loaded,
     * have a look into <a href="http://javascript.nwbox.com/IEContentLoaded/">this</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "exception", "exception" }, IE = { "exception", "success" })
    public void doScroll() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    document.documentElement.doScroll('left');\n"
            + "    alert('success');\n"
            + "  } catch (e) {\n"
            + "    alert('exception');\n"
            + "  }\n"
            + "}\n"
            + "test();\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "div2", "null", "div3", "div4", "div6", "div7", "null" })
    public void removeNode() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var div2 = document.getElementById('div2');\n"
            + "  alert(div1.firstChild.id);\n"
            + "  alert(div2.removeNode().firstChild);\n"
            + "  alert(div1.firstChild.id);\n"
            + "  alert(div1.firstChild.nextSibling.id);\n"
            + "\n"
            + "  var div5 = document.getElementById('div5');\n"
            + "  var div6 = document.getElementById('div6');\n"
            + "  alert(div5.firstChild.id);\n"
            + "  alert(div6.removeNode(true).firstChild.id);\n"
            + "  alert(div5.firstChild);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'><div id='div2'><div id='div3'></div><div id='div4'></div></div></div>\n"
            + "  <div id='div5'><div id='div6'><div id='div7'></div><div id='div8'></div></div></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "false", "hello", "true" }, IE = { "undefined", "true", "undefined", "false" })
    public void firefox__proto__() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var div1 = document.createElement('div');\n"
            + "  alert(div1.myProp);\n"
            + "  var p1 = div1['__proto__'];\n"
            + "  alert(p1 == undefined);\n"
            + "  if (p1)\n"
            + "    p1.myProp = 'hello';\n"
            + "  alert(div1.myProp);\n"
            + "  alert(p1 !== document.createElement('form')['__proto__']);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts(IE = { "false,false,false,false,false,false,false", "false,false,false,false,false,true,true" })
    public void clearAttributes() throws Exception {
        final String html
            = "<input type='text' id='i' name='i' style='color:red' onclick='alert(1)' custom1='a' /><script>\n"
            + "function u(o) { return typeof o == 'undefined'; }\n"
            + "var i = document.getElementById('i');\n"
            + "i.custom2 = 'b';\n"
            + "alert([u(i.type), u(i.id), u(i.name), u(i.style), u(i.onclick),"
            + "       u(i.custom1), u(i.custom2)].join(','));\n"
            + "i.clearAttributes();\n"
            + "alert([u(i.type), u(i.id), u(i.name), u(i.style), u(i.onclick),"
            + "       u(i.custom1), u(i.custom2)].join(','));\n"
            + "</script>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    public void mergeAttributes() throws Exception {
        mergeAttributes("i2", "false,false,false,false,false,true,true", "i", "",
            "false,false,false,false,false,false,false", "i", "");
        mergeAttributes("i2, true", "false,false,false,false,false,true,true", "i", "",
            "false,false,false,false,false,false,false", "i", "");
        mergeAttributes("i2, false", "false,false,false,false,false,true,true", "i", "",
            "false,false,false,false,false,false,false", "i2", "i2");
    }

    private void mergeAttributes(final String params, final String... expectedAlerts) throws Exception {
        final String html
            = "<input type='text' id='i' />\n"
            + "<input type='text' id='i2' name='i2' style='color:red' onclick='alert(1)' custom1='a' />\n"
            + "<script>\n"
            + "function u(o) { return typeof o == 'undefined'; }\n"
            + "var i = document.getElementById('i');\n"
            + "var i2 = document.getElementById('i2');\n"
            + "i2.custom2 = 'b';\n"
            + "alert([u(i.type), u(i.id), u(i.name), u(i.style), u(i.onclick),"
            + "       u(i.custom1), u(i.custom2)].join(','));\n"
            + "alert(i.id);\n"
            + "alert(i.name);\n"
            + "i.mergeAttributes(" + params + ");\n"
            + "alert([u(i.type), u(i.id), u(i.name), u(i.style), u(i.onclick),"
            + "       u(i.custom1), u(i.custom2)].join(','));\n"
            + "alert(i.id);\n"
            + "alert(i.name);\n"
            + "</script>";

        setExpectedAlerts(expectedAlerts);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false", IE = "true")
    public void document() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.body.document === document);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3 = { "undefined", "true" }, FF3_6 = { "undefined", "true" }, CHROME = { "undefined", "true" },
            DEFAULT = "exception")
    @NotYetImplemented(Browser.FF10)
    public void prototype_innerHTML() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "try {\n"
            + "  alert(HTMLElement.prototype.innerHTML);\n"
            + "  var myFunc = function() {};\n"
            + "  HTMLElement.prototype.innerHTML = myFunc;\n"
            + "  alert(HTMLElement.prototype.innerHTML == myFunc);\n"
            + "} catch (e) { alert('exception') }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
