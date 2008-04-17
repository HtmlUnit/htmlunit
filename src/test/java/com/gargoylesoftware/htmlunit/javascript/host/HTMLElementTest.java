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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
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
 */
public class HTMLElementTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testAll_IndexByInt() throws Exception {
        final String firstContent = "<html><head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "  dumpAll('body');\n"
            + "  dumpAll('testDiv');\n"
            + "  dumpAll('testA');\n"
            + "  dumpAll('testImg');\n"
            + "  dumpAll('testDiv2');\n"
            + "}\n"
            + "function dumpAll(_id)\n"
            + "{\n"
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

        final String[] expectedAlerts = {"all node for body: DIV A IMG DIV ", "all node for testDiv: A IMG ",
            "all node for testA: IMG ", "all node for testImg: ", "all node for testDiv2: "};

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, firstContent, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
        
    /**
     * @throws Exception on test failure
     */
    @Test
    public void testGetAttribute() throws Exception {
        final String content = "<html>\n"
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
        final String[] expectedAlerts = {"a", "a", "undefined", "null"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test", page.getTitleText());
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void testGetSetAttributeNS() throws Exception {
        final String content = "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "       var myNode = document.getElementById('myNode');\n"
                + "       alert(myNode.getAttributeNS('myNamespaceURI', 'my:foo'));\n"
                + "       myNode.setAttributeNS('myNamespaceURI', 'my:foo', 'bla');\n"
                + "       alert(myNode.getAttributeNS('myNamespaceURI', 'foo'));\n"
                + "   }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<p id='myNode' title='a'>\n"
                + "</p>\n"
                + "</body>\n"
                + "</html>";

        final String[] expectedAlerts = {"", "bla"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void testSetAttribute() throws Exception {
        final String content = "<html>\n"
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
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test", page.getTitleText());
        final String[] expectedAlerts = {"a", "b", "undefined", "foo"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void testGetAttributeNode() throws Exception {
        final String content =
              "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var div = document.getElementById('div2');\n"
            + "      var customAtt = div.getAttributeNode('customAttribute');\n"
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
            + "  <div id='div2' name='blah' customAttribute='bleh'></div>\n"
            + "  <div id='div3'></div>\n"
            + "</body>\n"
            + "</html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test", page.getTitleText());
        final String[] expectedAlerts = {
            "expando=true",
            "firstChild=null",
            "lastChild=null",
            "name=customAttribute",
            "nextSibling=null",
            "nodeName=customAttribute",
            "nodeType=2",
            "nodeValue=bleh",
            "(ownerDocument==document)=true",
            "parentNode=null",
            "previousSibling=null",
            "specified=true",
            "value=bleh",
        };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void testSetAttributeNode() throws Exception {
        final String content =
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
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test", page.getTitleText());
        final String[] expectedAlerts = {"left", "left", "right", "right"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for <tt>getElementsByTagName</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void testGetElementsByTagName() throws Exception {
        final String content
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
        final String[] expectedAlerts = {"all = 4", "row = 2", "by wrong name: 0"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that {@link HTMLElement#jsxFunction_getElementsByTagName} returns an associative array.
     * Test for bug 1369514.
     * @throws Exception if the test fails
     */
    @Test
    public void testGetElementsByTagNameCollection() throws Exception {
        final String content
            = "<html><head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
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

        final String[] expectedAlerts = {"first", "second", "third"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests that getElementsByTagName('*') returns all child elements, both
     * at the document level and at the element level.
     * @throws Exception if the test fails
     */
    @Test
    public void testGetElementsByTagNameAsterisk() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      alert(document.getElementsByTagName('*').length);\n"
            + "      alert(document.getElementById('div').getElementsByTagName('*').length);\n"
            + "   }\n"
            + "</script>\n"
            + "<div id='div'><p>a</p><p>b</p><p>c</p></div>\n"
            + "</body></html>";
        final String[] expected = {"8", "3"};
        final List<String> actual = new ArrayList<String>();
        loadPage(html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Test getting the class for the element.
     * @throws Exception if the test fails
     */
    @Test
    public void testGetClassName() throws Exception {
        final String content
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

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        final String[] expectedAlerts = {"the class is x"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test setting the class for the element.
     * @throws Exception if the test fails
     */
    @Test
    public void testSetClassName() throws Exception {
        final String content
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

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        final String[] expectedAlerts = {"the class is z"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetInnerHTML() throws Exception {
        final String content = "<html>\n"
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
        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"if (1 > 2 & 3 < 2) willNotHappen('yo');"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetInnerHTMLSimple_FF() throws Exception {
        final String[] expected = {"Old = <b>Old innerHTML</b>", "New = New cell value"};
        testGetSetInnerHTMLSimple(BrowserVersion.FIREFOX_2, expected);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetInnerHTMLSimple_IE() throws Exception {
        final String[] expected = {"Old = <B>Old innerHTML</B>", "New = New cell value"};
        testGetSetInnerHTMLSimple(BrowserVersion.INTERNET_EXPLORER_6_0, expected);
    }

    private void testGetSetInnerHTMLSimple(final BrowserVersion version, final String[] expected) throws Exception {
        final String content = "<html>\n"
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
        final List<String> actual = new ArrayList<String>();
        loadPage(version, content, actual);
        assertEquals(expected, actual);
    }

    /**
     * Test the use of innerHTML to set new HTML code in Firefox.
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetInnerHTMLComplex_FF() throws Exception {
        final String[] expected = {
            "Old = <b>Old innerHTML</b><!-- old comment -->",
            "New =  <b><i id=\"newElt\">New cell value</i></b>",
            "I" };
        testGetSetInnerHTMLComplex(BrowserVersion.FIREFOX_2, expected);
    }

    /**
     * Test the use of innerHTML to set new HTML code in IE.
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetInnerHTMLComplex_IE() throws Exception {
        final String[] expected = {
            "Old = <B>Old innerHTML</B><!-- old comment -->",
            "New =  <B><I id=newElt>New cell value</I></B>",
            "I" };
        testGetSetInnerHTMLComplex(BrowserVersion.INTERNET_EXPLORER_6_0, expected);
    }

    private void testGetSetInnerHTMLComplex(final BrowserVersion version, final String[] expected)
        throws Exception {
        final String content = "<html>\n"
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

        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(version, content, actual);
        assertEquals(expected, actual);

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
    public void testGetSetInnerHTMLNewInput() throws Exception {
        final String content = "<html>\n"
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
        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"true"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetInnerHTMLChar_FF() throws Exception {
        final String[] expected = {
            "Old = <b>Old innerHTML</b>",
            "New = New cell value &amp; \u0110 \u0110" };
        testGetSetInnerHTMLChar(BrowserVersion.FIREFOX_2, expected);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetInnerHTMLChar_IE() throws Exception {
        final String[] expected = {
            "Old = <B>Old innerHTML</B>",
            "New = New cell value &amp; \u0110 \u0110" };
        testGetSetInnerHTMLChar(BrowserVersion.INTERNET_EXPLORER_6_0, expected);
    }

    private void testGetSetInnerHTMLChar(final BrowserVersion version, final String[] expected)
        throws Exception {
        final String content = "<html>\n"
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
        createTestPageForRealBrowserIfNeeded(content, expected);
        final List<String> actual = new ArrayList<String>();
        loadPage(version, content, actual);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that empty tags are not abbreviated into their &lt;tag/&gt; form.
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetInnerHtmlEmptyTag_FF() throws Exception {
        final String[] expected = {"undefined", "<ul></ul>", "undefined"};
        testGetSetInnerHtmlEmptyTag(BrowserVersion.FIREFOX_2, expected);
    }

    /**
     * Verifies that empty tags are not abbreviated into their &lt;tag/&gt; form.
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetInnerHtmlEmptyTag_IE() throws Exception {
        final String[] expected = {"<DIV id=div><UL></UL></DIV>", "<UL></UL>", ""};
        testGetSetInnerHtmlEmptyTag(BrowserVersion.INTERNET_EXPLORER_6_0, expected);
    }

    private void testGetSetInnerHtmlEmptyTag(final BrowserVersion version, final String[] expected)
        throws Exception {
        final String content = "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      var div = document.getElementById('div');\n"
            + "      alert(div.outerHTML);\n"
            + "      alert(div.innerHTML);\n"
            + "      alert(div.innerText);\n"
            + "   }\n"
            + "</script>\n"
            + "<div id='div'><ul/></div>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, content, actual);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that attributes containing whitespace are always quoted.
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetInnerHtmlAttributeWithWhitespace_FF() throws Exception {
        final String[] expected = {"undefined", "<span class=\"a b\"></span>", "undefined"};
        testGetSetInnerHtmlAttributeWithWhitespace(BrowserVersion.FIREFOX_2, expected);
    }

    /**
     * Verifies that attributes containing whitespace are always quoted.
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetInnerHtmlAttributeWithWhitespace_IE() throws Exception {
        final String[] expected = {"<DIV id=div><SPAN class=\"a b\"></SPAN></DIV>", "<SPAN class=\"a b\"></SPAN>", ""};
        testGetSetInnerHtmlAttributeWithWhitespace(BrowserVersion.INTERNET_EXPLORER_6_0, expected);
    }

    private void testGetSetInnerHtmlAttributeWithWhitespace(final BrowserVersion version, final String[] expected)
        throws Exception {
        final String content = "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      var div = document.getElementById('div');\n"
            + "      alert(div.outerHTML);\n"
            + "      alert(div.innerHTML);\n"
            + "      alert(div.innerText);\n"
            + "   }\n"
            + "</script>\n"
            + "<div id='div'><span class='a b'></span></div>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, content, actual);
        assertEquals(expected, actual);
    }

    /**
     * Test setting innerHTML to empty string.
     * @throws Exception if the test fails
     */
    @Test
    public void testSetInnerHTMLEmpty() throws Exception {
        final String content = "<html><head></head><body>\n"
                + "<div id='testDiv'>foo</div>\n"
                + "<script language='javascript'>\n"
                + "    var node = document.getElementById('testDiv');\n"
                + "    node.innerHTML = '';\n"
                + "    alert('Empty ChildrenLength: ' + node.childNodes.length);\n"
                + "</script></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"Empty ChildrenLength: 0"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test setting innerHTML to null.
     * @throws Exception if the test fails
     */
    @Test
    public void testSetInnerHTMLNull() throws Exception {
        final String[] alertsIE = {"Null ChildrenLength: 1"};
        testSetInnerHTMLNull(BrowserVersion.INTERNET_EXPLORER_6_0, alertsIE);
        final String[] alertsFF = {"Null ChildrenLength: 0"};
        testSetInnerHTMLNull(BrowserVersion.FIREFOX_2, alertsFF);
    }

    /**
     * Test setting innerHTML to null.
     * @see #testSetInnerHTMLNull()
     */
    private void testSetInnerHTMLNull(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String content = "<html><head></head><body>\n"
                + "<div id='testDiv'>foo</div>\n"
                + "<script language='javascript'>\n"
                + "    var node = document.getElementById('testDiv');\n"
                + "    node.innerHTML = null;\n"
                + "    alert('Null ChildrenLength: ' + node.childNodes.length);\n"
                + "</script></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetOuterHTMLSimple() throws Exception {
        final String content = "<html>\n"
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
        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {
            "Old = <B id=innerNode>Old outerHTML</B>",
            "New = New cell value"
        };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test the use of outerHTML to set new HTML code.
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetOuterHTMLComplex() throws Exception {
        final String content = "<html>\n"
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
        final String[] expectedAlerts = {
            "Old = <B id=innerNode>Old outerHTML</B>",
            "New =  <B><I id=newElt>New cell value</I></B>",
            "I"
        };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
        final HtmlElement pElt = page.getHtmlElementById("myNode");
        assertEquals("p", pElt.getNodeName());
        final HtmlElement elt = page.getHtmlElementById("newElt");
        assertEquals("New cell value", elt.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testInsertAdjacentHTML() throws Exception {
        testInsertAdjacentHTML("beforeEnd", "afterEnd", "beforeBegin", "afterBegin");
        testInsertAdjacentHTML("BeforeEnd", "AfterEnd", "BeFoReBeGiN", "afterbegin");
    }

    /**
     * @param beforeEnd data to insert
     * @param afterEnd data to insert
     * @param beforeBegin data to insert
     * @param afterBegin data to insert
     * @throws Exception if the test fails
     */
    private void testInsertAdjacentHTML(final String beforeEnd,
            final String afterEnd, final String beforeBegin, final String afterBegin)
        throws Exception {
        final String content = "<html><head><title>First</title>\n"
                + "<script>\n"
                + "function test()\n"
                + "{\n"
                + "  var oDiv = document.getElementById('middle');\n"
                + "  oDiv.insertAdjacentHTML('" + beforeEnd + "', ' <div id=3>before end</div> ');\n"
                + "  oDiv.insertAdjacentHTML('" + afterEnd + "', ' <div id=4>after end</div> ');\n"
                + "  oDiv.insertAdjacentHTML('" + beforeBegin + "', ' <div id=1>before begin</div> ');\n"
                + "  oDiv.insertAdjacentHTML('" + afterBegin + "', ' <div id=2>after begin</div> ');\n"
                + "  var coll = document.getElementsByTagName('DIV');\n"
                + "  for (var i=0; i<coll.length; i++) {\n"
                + "    alert(coll[i].id);\n"
                + "  }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "<div id='outside' style='color: #00ff00'>\n"
                + "<div id='middle' style='color: #ff0000'>\n"
                + "inside\n"
                + "</div>\n"
                + "</div>\n"
                + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content,
                collectedAlerts);
        final String[] expectedAlerts = {"outside", "1", "middle", "2", "3", "4"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
        final HtmlElement elt = page.getHtmlElementById("outside");
        assertEquals("before begin after begin inside before end after end", elt.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testInsertAdjacentElement() throws Exception {
        testInsertAdjacentElement("beforeEnd", "afterEnd", "beforeBegin", "afterBegin");
        testInsertAdjacentElement("BeforeEnd", "AfterEnd", "BeFoReBeGiN", "afterbegin");
    }

    private void testInsertAdjacentElement(final String beforeEnd,
            final String afterEnd, final String beforeBegin, final String afterBegin)
        throws Exception {
        final String content = "<html><head><title>First</title>\n"
                + "<script>\n"
                + "function test()\n"
                + "{\n"
                + "  var oDiv = document.getElementById('middle');\n"
                + "  oDiv.insertAdjacentElement('" + beforeEnd + "', makeElement(3, 'before end' ));\n"
                + "  oDiv.insertAdjacentElement('" + afterEnd + "', makeElement(4, 'after end' ));\n"
                + "  oDiv.insertAdjacentElement('" + beforeBegin + "', makeElement(1, 'before begin' ));\n"
                + "  oDiv.insertAdjacentElement('" + afterBegin + "', makeElement(2, 'after begin' ));\n"
                + "  var coll = document.getElementsByTagName('DIV');\n"
                + "  for (var i=0; i<coll.length; i++) {\n"
                + "    alert(coll[i].id);\n"
                + "  }\n"
                + "}\n"
                + "function makeElement(id, value ) {\n"
                + "  var div = document.createElement('div');\n"
                + "  div.appendChild(document.createTextNode(value ));\n"
                + "  div.id = id;\n"
                + "  return div;\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "<div id='outside' style='color: #00ff00'>\n"
                + "<div id='middle' style='color: #ff0000'>\n"
                + "inside\n"
                + "</div>\n"
                + "</div>\n"
                + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content,
                collectedAlerts);
        final String[] expectedAlerts = {"outside", "1", "middle", "2", "3", "4"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
        final HtmlElement elt = page.getHtmlElementById("outside");
        assertEquals("before begin after begin inside before end after end", elt.asText());
    }

    /**
     * Test the <tt>#default#clientCaps</tt> default IE behavior.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testAddBehaviorDefaultClientCaps() throws Exception {
        final String content = "<html>\n"
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
        final String[] expectedAlerts = {
            "body.cpuClass = undefined",
            "body.cpuClass = " + BrowserVersion.getDefault().getCpuClass(),
            "body.cpuClass = undefined"
        };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test the <tt>#default#homePage</tt> default IE behavior.
     * @throws Exception if the test fails
     */
    @Test
    public void testAddBehaviorDefaultHomePage() throws Exception {
        final URL content1Url = new URL("http://www.domain1.com/");
        final URL content2Url = new URL("http://www.domain2.com/");
        final String content1 =
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
            + "       var url = '" + content1Url.toExternalForm() + "';\n"
            + "       alert('isHomePage = ' + body.isHomePage(url));\n"
            + "       body.setHomePage(url);\n"
            + "       alert('isHomePage = ' + body.isHomePage(url));\n"
            + "       // Test behavior added via style attribute.\n"
            + "       // Also test case-insensitivity of default behavior names.\n"
            + "       alert('isHomePage = ' + hp.isHomePage(url));\n"
            + "       // Make sure that (as mentioned above) isHomePage() always\n"
            + "       // returns false when the url specified is the actual\n"
            + "       // homepage, but the document checking is on a DIFFERENT domain.\n"
            + "       hp.setHomePage('" + content2Url.toExternalForm() + "');\n"
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
        final String content2 = "<html></html>";

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(content1Url, content1);
        webConnection.setResponse(content2Url, content2);
        client.setWebConnection(webConnection);
        final HtmlPage page = client.getPage(content1Url);
        final String[] expectedAlerts = {
            "isHomePage = false",
            "isHomePage = true",
            "isHomePage = true",
            "isHomePage = false"
        };
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals(content2Url.toExternalForm(), page.getWebResponse().getUrl().toExternalForm());
    }

    /**
    * Test the <tt>#default#download</tt> default IE behavior.
    *
    * @throws Exception if the test fails
    */
    @Test
    public void testAddBehaviorDefaultDownload() throws Exception {
        final URL content1Url = new URL("http://htmlunit.sourceforge.net/");
        final URL content2Url = new URL("http://htmlunit.sourceforge.net/test.txt");
        // The download behavior doesn't accept downloads from a different domain ...
        final URL content3Url = new URL("http://www.domain2.com/test.txt");

        final String content1 =
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

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(content1Url, content1);
        webConnection.setResponse(content2Url, "foo");
        webConnection.setResponse(content3Url, "foo2");
        client.setWebConnection(webConnection);
        client.getPage(content1Url);

        final String[] expectedAlerts = {"Refused", "foo"};
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
    public void testRemoveBehavior() throws Exception {
        final String content = "<html>\n"
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
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        final String[] expectedAlerts = {
            "body.isHomePage = undefined",
            "body.isHomePage = false",
            "body.isHomePage = undefined"
        };
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testChildren() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
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
        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"BR", "DIV", "2", "3"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOnclick() throws Exception {
        eventHandlerSetterGetterTest("onclick");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOndblclick() throws Exception {
        eventHandlerSetterGetterTest("ondblclick");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOnblur() throws Exception {
        eventHandlerSetterGetterTest("onblur");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOnfocus() throws Exception {
        eventHandlerSetterGetterTest("onfocus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOnkeydown() throws Exception {
        eventHandlerSetterGetterTest("onkeydown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOnkeypress() throws Exception {
        eventHandlerSetterGetterTest("onkeypress");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOnkeyup() throws Exception {
        eventHandlerSetterGetterTest("onkeyup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOnmousedown() throws Exception {
        eventHandlerSetterGetterTest("onmousedown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOnmouseup() throws Exception {
        eventHandlerSetterGetterTest("onmouseup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOnmouseover() throws Exception {
        eventHandlerSetterGetterTest("onmouseover");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOnmouseout() throws Exception {
        eventHandlerSetterGetterTest("onmouseout");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOnmousemove() throws Exception {
        eventHandlerSetterGetterTest("onmousemove");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetOnresize() throws Exception {
        eventHandlerSetterGetterTest("onresize");
    }

    /**
     * @param eventName the name of the event
     * @throws Exception if the test fails
     */
    private void eventHandlerSetterGetterTest(final String eventName) throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function handler(event) {}\n"
            + "function test()\n"
            + "{\n"
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
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final HtmlPage page = loadPage(content, collectedAlerts);
        final HtmlElement div = page.getHtmlElementById("myDiv");

        assertNotNull("Event handler was not set", div.getEventHandler(eventName));

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSetInnerTextSimple() throws Exception {
        final String content = "<html>\n"
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
        final String[] expectedAlerts = {"Old = Old \r\ninnerText", "New = New cell value"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClickHashAnchor() throws Exception {
        final String content
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
        final HtmlPage loadPage = loadPage(content, loadCollectedAlerts);
        final HtmlAnchor loadHashAnchor = loadPage.getAnchorByName("hash");
        loadHashAnchor.click();

        assertEquals(expectedAlerts, loadCollectedAlerts);

        // finally try via the client
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, content);
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
    public void testRemoveAttribute() throws Exception {
        final String content = "<html>\n"
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
        final String[] expectedAlerts = {"removeMe", "null"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test offsets (real values don't matter currently).
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testOffsets() throws Exception {
        final String content = "<html>\n"
              + "<head>\n"
              + "    <title>Test</title>\n"
              + "</head>\n"
              + "<body>\n"
              + "</div></body>\n"
              + "<div id='div1'>foo</div>\n"
              + "<script>\n"
              + "function alertOffsets(_oElt)\n"
              + "{\n"
              + "  alert(typeof _oElt.offsetHeight);\n"
              + "  alert(typeof _oElt.offsetWidth);\n"
              + "  alert(typeof _oElt.offsetLeft);\n"
              + "  alert(typeof _oElt.offsetTop);\n"
              + "}\n"
              + "alertOffsets(document.body);\n"
              + "alertOffsets(document.getElementById('div1'));\n"
              + "</script></body></html>";
        final List<String> expectedAlerts = Collections.nCopies(8, "number");
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testOffsetWidthAndHeight() throws Exception {
        final String content =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    e.style.width = 30;\n"
            + "    alert(e.style.width);\n"
            + "    alert(e.offsetWidth);\n"
            + "    e.style.height = 55;\n"
            + "    alert(e.style.height);\n"
            + "    alert(e.offsetHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' style='border: 3px solid #fff; padding: 5px;'></div>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"30px", "46", "55px", "71"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testOffsetTopAndLeft_Padding() throws Exception {
        testOffsetTopAndLeft_Padding(BrowserVersion.FIREFOX_2, "15", "15");
        testOffsetTopAndLeft_Padding(BrowserVersion.INTERNET_EXPLORER_7_0, "15", "15");
        testOffsetTopAndLeft_Padding(BrowserVersion.INTERNET_EXPLORER_6_0, "15", "15");
    }

    private void testOffsetTopAndLeft_Padding(final BrowserVersion version, final String... expected)
        throws Exception {
        final String content =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        alert(e.offsetTop);\n"
            + "        alert(e.offsetLeft);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()' style='padding: 3px; margin: 0px; border: 0px solid green;'>\n"
            + "    <div style='padding: 5px; margin: 0px; border: 0px solid blue;'>\n"
            + "      <div style='padding: 7px; margin: 0px; border: 0px solid red;'>\n"
            + "        <div id='d' style='padding: 13px; margin: 0px; border: 0px solid black;'>d</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, content, actual);
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testOffsetTopAndLeft_Margins() throws Exception {
        testOffsetTopAndLeft_Margins(BrowserVersion.FIREFOX_2, "13", "28");
        testOffsetTopAndLeft_Margins(BrowserVersion.INTERNET_EXPLORER_7_0, "13", "28");
        testOffsetTopAndLeft_Margins(BrowserVersion.INTERNET_EXPLORER_6_0, "13", "28");
    }

    private void testOffsetTopAndLeft_Margins(final BrowserVersion version, final String... expected)
        throws Exception {
        final String content =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        alert(e.offsetTop);\n"
            + "        alert(e.offsetLeft);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()' style='padding: 0px; margin: 3px; border: 0px solid green;'>\n"
            + "    <div style='padding: 0px; margin: 5px; border: 0px solid blue;'>\n"
            + "      <div style='padding: 0px; margin: 7px; border: 0px solid red;'>\n"
            + "        <div id='d' style='padding: 0px; margin: 13px; border: 0px solid black;'>d</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, content, actual);
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testOffsetTopAndLeft_Borders() throws Exception {
        testOffsetTopAndLeft_Borders(BrowserVersion.FIREFOX_2, "12", "12");
        testOffsetTopAndLeft_Borders(BrowserVersion.INTERNET_EXPLORER_7_0, "12", "12");
        testOffsetTopAndLeft_Borders(BrowserVersion.INTERNET_EXPLORER_6_0, "12", "12");
    }

    private void testOffsetTopAndLeft_Borders(final BrowserVersion version, final String... expected)
        throws Exception {
        final String content =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        alert(e.offsetTop);\n"
            + "        alert(e.offsetLeft);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()' style='padding: 0px; margin: 0px; border: 3px solid green;'>\n"
            + "    <div style='padding: 0px; margin: 0px; border: 5px solid blue;'>\n"
            + "      <div style='padding: 0px; margin: 0px; border: 7px solid red;'>\n"
            + "        <div id='d' style='padding: 0px; margin: 0px; border: 13px solid black;'>d</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, content, actual);
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testOffsetTopAndLeft_Nothing() throws Exception {
        testOffsetTopAndLeft_Nothing(BrowserVersion.FIREFOX_2, "0", "0");
        testOffsetTopAndLeft_Nothing(BrowserVersion.INTERNET_EXPLORER_7_0, "0", "0");
        testOffsetTopAndLeft_Nothing(BrowserVersion.INTERNET_EXPLORER_6_0, "0", "0");
    }

    private void testOffsetTopAndLeft_Nothing(final BrowserVersion version, final String... expected)
        throws Exception {
        final String content =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        alert(e.offsetTop);\n"
            + "        alert(e.offsetLeft);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()' style='padding: 0px; margin: 0px; border: 0px solid green;'>\n"
            + "    <div style='padding: 0px; margin: 0px; border: 0px solid blue;'>\n"
            + "      <div style='padding: 0px; margin: 0px; border: 0px solid red;'>\n"
            + "        <div id='d' style='padding: 0px; margin: 0px; border: 0px solid black;'>d</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, content, actual);
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testOffsetTopAndLeft_AbsolutelyPositioned() throws Exception {
        testOffsetTopAndLeft_AbsolutelyPositioned(BrowserVersion.FIREFOX_2, "50", "50");
        testOffsetTopAndLeft_AbsolutelyPositioned(BrowserVersion.INTERNET_EXPLORER_7_0, "50", "50");
        testOffsetTopAndLeft_AbsolutelyPositioned(BrowserVersion.INTERNET_EXPLORER_6_0, "50", "50");
    }

    private void testOffsetTopAndLeft_AbsolutelyPositioned(final BrowserVersion version, final String... expected)
        throws Exception {
        final String content =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        alert(e.offsetTop);\n"
            + "        alert(e.offsetLeft);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <div>\n"
            + "      <div>\n"
            + "        <div id='d' style='position:absolute; top:50; left:50;'>d</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, content, actual);
        assertEquals(expected, actual);
    }

    /**
     * Test scrolls (real values don't matter currently).
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testScrolls() throws Exception {
        final String content = "<html>\n"
              + "<head>\n"
              + "    <title>Test</title>\n"
              + "</head>\n"
              + "<body>\n"
              + "</div></body>\n"
              + "<div id='div1'>foo</div>\n"
              + "<script>\n"
              + "function alertScrolls(_oElt)\n"
              + "{\n"
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
        final List<String> expectedAlerts = Collections.nCopies(8, "number");
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests that JavaScript scrollIntoView() function doesn't fail.
     * @throws Exception if the test fails
     */
    @Test
    public void testScrollIntoView() throws Exception {
        final String content = "<html>\n"
              + "<body>\n"
              + "<script id='me'>document.getElementById('me').scrollIntoView(); alert('ok');</script>\n"
              + "</body></html>";
        final String[] expectedAlerts = {"ok"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests the offsetParent property.
     * @throws Exception if the test fails
     */
    @Test
    public void testOffsetParent_Basic() throws Exception {
        final String content = "<html><head>\n"
            + "<script type='text/javascript'>\n"
            + "function alertOffsetParent(id)\n"
            + "{\n"
            + "    var element = document.getElementById(id);\n"
            + "    var offsetParent = element.offsetParent;\n"
            + "    var alertMessage = 'element: ' + element.id + ' offsetParent: ';\n"
            + "    if (offsetParent)\n"
            + "    {\n"
            + "        alertMessage += offsetParent.id;\n"
            + "    }\n"
            + "    else\n"
            + "    {\n"
            + "        alertMessage += offsetParent;\n"
            + "    }\n"
            + "    alert(alertMessage);\n"
            + "}\n"
            + "function test()\n"
            + "{\n"
            + "alertOffsetParent('span3');\n"
            + "alertOffsetParent('td2');\n"
            + "alertOffsetParent('tr2');\n"
            + "alertOffsetParent('table2');\n"
            + "alertOffsetParent('td1');\n"
            + "alertOffsetParent('tr1');\n"
            + "alertOffsetParent('table1');\n"
            + "alertOffsetParent('span2');\n"
            + "alertOffsetParent('span1');\n"
            + "alertOffsetParent('div1');\n"
            + "alertOffsetParent('body1');\n"
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

        final String[] expectedAlerts = {
            "element: span3 offsetParent: td2", "element: td2 offsetParent: table2",
            "element: tr2 offsetParent: table2", "element: table2 offsetParent: td1",
            "element: td1 offsetParent: table1", "element: tr1 offsetParent: table1",
            "element: table1 offsetParent: body1", "element: span2 offsetParent: body1",
            "element: span1 offsetParent: body1", "element: div1 offsetParent: body1",
            "element: body1 offsetParent: null"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests the offsetParent property, including the effects of CSS "position" attributes.
     * Based on <a href="http://dump.testsuite.org/2006/dom/style/offset/spec#offsetparent">this work</a>.
     * @throws Exception if an error occurs
     */
    @Test
    public void testOffsetParent_WithCSS() throws Exception {
        testOffsetParent_WithCSS(BrowserVersion.FIREFOX_2, "null", "body", "body", "body", "body", "body",
            "f1", "body", "h1", "i1", "td", "body", "td", "body", "body");
        testOffsetParent_WithCSS(BrowserVersion.INTERNET_EXPLORER_7_0, "null", "body", "body", "body", "body", "body",
            "body", "body", "h1", "i1", "td", "td", "td", "body", "body");
    }

    private void testOffsetParent_WithCSS(final BrowserVersion version, final String... expected) throws Exception {
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
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testPrototype() throws Exception {
        final String content = "<html><head><title>Prototype test</title>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "    var d = document.getElementById('foo');\n"
            + "    alert(d.foo);\n"
            + "    alert(d.myFunction);\n"
            + "    var link = document.getElementById('testLink');\n"
            + "    alert(link.foo);\n"
            + "    alert(link.myFunction);\n"
            + "    HTMLElement.prototype.foo = 123;\n"
            + "    HTMLElement.prototype.myFunction = function() { return 'from myFunction'; };\n"
            + "    alert(d.foo);\n"
            + "    alert(d.myFunction());\n"
            + "    alert(link.foo);\n"
            + "    alert(link.myFunction());\n"
            + "}\n"
            + "</script></head><body onload='test()''>\n"
            + "<div id='foo'>bla</div>\n"
            + "<a id='testLink' href='foo'>bla</a>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"undefined", "undefined", "undefined", "undefined",
            "123", "from myFunction", "123", "from myFunction"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * 'Element' and 'HTMLElement' prototypes are synonyms.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testPrototype_Element() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    Element.prototype.selectNodes = function(sExpr){\n"
            + "      alert('in selectNodes');\n"
            + "    }\n"
            + "    document.getElementById('myDiv').selectNodes();\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"in selectNodes"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testInstanceOf() throws Exception {
        final String content = "<html><head><title>instanceof test</title>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "    var d = document.getElementById('foo');\n"
            + "    alert(d instanceof HTMLDivElement);\n"
            + "    var link = document.getElementById('testLink');\n"
            + "    alert(link instanceof HTMLAnchorElement);\n"
            + "}\n"
            + "</script></head><body onload='test()''>\n"
            + "<div id='foo'>bla</div>\n"
            + "<a id='testLink' href='foo'>bla</a>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"true", "true"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testParentElement() throws Exception {
        final String[] alertsIE = {"null", "[object]"};
        testParentElement(BrowserVersion.INTERNET_EXPLORER_6_0, alertsIE);
        testParentElement(BrowserVersion.INTERNET_EXPLORER_7_0, alertsIE);
        final String[] alertsFF = {"undefined", "undefined"};
        testParentElement(BrowserVersion.FIREFOX_2, alertsFF);
    }

    private void testParentElement(final BrowserVersion browser, final String[] expectedAlerts) throws Exception {
        final String content
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

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browser, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testCurrentStyle() throws Exception {
        testStyle(BrowserVersion.INTERNET_EXPLORER_6_0, "currentStyle", "rgb(0, 0, 0)");
        try {
            testStyle(BrowserVersion.FIREFOX_2, "currentStyle", "");
            fail("'currentStyle' is defined for only IE");
        }
        catch (final Exception e) {
            //expected
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testRuntimeStyle() throws Exception {
        testStyle(BrowserVersion.INTERNET_EXPLORER_6_0, "runtimeStyle", "");
        try {
            testStyle(BrowserVersion.FIREFOX_2, "runtimeStyle", "");
            fail("'runtimeStyle' is defined for only IE");
        }
        catch (final Exception e) {
            //expected
        }
    }

    private void testStyle(final BrowserVersion browserVersion, final String styleProperty, final String expectedAlert)
        throws Exception {

        final String content = "<html>\n"
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
        
        final String[] expectedAlerts = {expectedAlert};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetBoundingClientRect() throws Exception {
        testGetBoundingClientRect(BrowserVersion.INTERNET_EXPLORER_7_0);
        try {
            testGetBoundingClientRect(BrowserVersion.FIREFOX_2);
            fail("'getBoundingClientRect' is defined for only IE");
        }
        catch (final Exception e) {
            //expected
        }
    }

    private void testGetBoundingClientRect(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    d1.getBoundingClientRect().left;\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'/>\n"
            + "</body></html>";
        loadPage(browserVersion, content, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetClientRects() throws Exception {
        testGetClientRects(BrowserVersion.INTERNET_EXPLORER_7_0);
        try {
            testGetClientRects(BrowserVersion.FIREFOX_2);
            fail("'getClientRects' is defined for only IE");
        }
        catch (final Exception e) {
            //expected
        }
    }

    private void testGetClientRects(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    d1.getClientRects();\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'/>\n"
            + "</body></html>";
        loadPage(browserVersion, content, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testInnerHTML_parentNode() throws Exception {
        final String[] expectedAlertsIE = {"null", "#document-fragment"};
        testInnerHTML_parentNode(BrowserVersion.INTERNET_EXPLORER_7_0, expectedAlertsIE);
        final String[] expectedAlertsFF = {"null", "null"};
        testInnerHTML_parentNode(BrowserVersion.FIREFOX_2, expectedAlertsFF);
    }

    private void testInnerHTML_parentNode(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
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
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testInnerText_parentNode() throws Exception {
        final String[] expectedAlertsIE = {"null", "#document-fragment"};
        testInnerText_parentNode(BrowserVersion.INTERNET_EXPLORER_7_0, expectedAlertsIE);
        final String[] expectedAlertsFF = {"null", "null"};
        testInnerText_parentNode(BrowserVersion.FIREFOX_2, expectedAlertsFF);
    }

    private void testInnerText_parentNode(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
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
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testUniqueID() throws Exception {
        testUniqueID(BrowserVersion.INTERNET_EXPLORER_6_0, false, new String[] {"true", "false"});
        testUniqueID(BrowserVersion.FIREFOX_2, true, new String[] {"true", "true"});
    }

    private void testUniqueID(final BrowserVersion browserVersion, final boolean isUndefined,
        final String[] expectedAlerts) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     var div2 = document.getElementById('div2');\n"
            + "     alert(div1.uniqueID);\n"
            + "     alert(div1.uniqueID == div1.uniqueID);\n"
            + "     alert(div1.uniqueID == div2.uniqueID);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "  <div id='div2'/>\n"
            + "</body></html>";
        
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        if (isUndefined) {
            assertEquals("undefined", collectedAlerts.get(0));
        }
        else {
            assertFalse("undefined".equals(collectedAlerts.get(0)));
        }
        assertEquals(expectedAlerts, collectedAlerts.subList(1, collectedAlerts.size()));
    }

    /**
     * Tests if element.uniqueID starts with 'ms__id', and is lazily created.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testUniqueIDFormatIE() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
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
        
        final String[] expectedAlerts = {"true", "true"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests setAttribute() with name of event handler.
     * @throws Exception if the test fails
     */
    @Test
    public void testSetAttribute_eventHandler() throws Exception {
        testSetAttribute_eventHandler(BrowserVersion.INTERNET_EXPLORER_7_0, 0);
        testSetAttribute_eventHandler(BrowserVersion.FIREFOX_2, 3);
    }
    
    private void testSetAttribute_eventHandler(final BrowserVersion browserVersion, final int expectedAlertNumber)
        throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
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
        final HtmlPage page = loadPage(browserVersion, content, collectedAlerts);
        final HtmlForm form = page.getForms().get(0);
        final HtmlTextInput inputLogin = (HtmlTextInput) form.getInputByName("login");
        final HtmlPasswordInput inputPassword = (HtmlPasswordInput) form.getInputByName("password");

        inputLogin.focus();
        inputPassword.focus();

        assertEquals(expectedAlertNumber, collectedAlerts.size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testFireEvent_WithoutTemplate() throws Exception {
        final String html =
              "<html><body>\n"
            + "<div id='a' onclick='alert(\"clicked\")'>foo</div>\n"
            + "<div id='b' onmouseover='document.getElementById(\"a\").fireEvent(\"onclick\")'>bar</div>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, html, actual);
        ((HtmlDivision) page.getHtmlElementById("a")).click();
        ((HtmlDivision) page.getHtmlElementById("b")).mouseOver();
        final String[] expected = {"clicked", "clicked"};
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testFireEvent_WithTemplate() throws Exception {
        final String html =
              "<html><body>\n"
            + "<script>var template = document.createEventObject();</script>\n"
            + "<script>function doAlert(e) { alert(e.type); }</script>\n"
            + "<div id='a' onclick='doAlert(event)'>foo</div>\n"
            + "<div id='b' onmouseover='document.getElementById(\"a\").fireEvent(\"onclick\")'>bar</div>\n"
            + "<div id='c' onmouseover='document.getElementById(\"a\").fireEvent(\"onclick\", template)'>baz</div>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, html, actual);
        ((HtmlDivision) page.getHtmlElementById("a")).click();
        ((HtmlDivision) page.getHtmlElementById("b")).mouseOver();
        ((HtmlDivision) page.getHtmlElementById("c")).mouseOver();
        final String[] expected = {"click", "click", "click"};
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testSetExpression() throws Exception {
        testSetExpression(BrowserVersion.INTERNET_EXPLORER_7_0);
        try {
            testSetExpression(BrowserVersion.FIREFOX_2);
            fail("setExpression is not defined for Firefox");
        }
        catch (final Exception e) {
            //expected
        }
    }
    
    private void testSetExpression(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     div1.setExpression('title','id');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";

        loadPage(browserVersion, content, null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testRemoveExpression() throws Exception {
        testRemoveExpression(BrowserVersion.INTERNET_EXPLORER_7_0);
        try {
            testRemoveExpression(BrowserVersion.FIREFOX_2);
            fail("removeExpression is not defined for Firefox");
        }
        catch (final Exception e) {
            //expected
        }
    }

    private void testRemoveExpression(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     div1.setExpression('title','id');\n"
            + "     div1.removeExpression('title');"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";

        loadPage(browserVersion, content, null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testDispatchEvent() throws Exception {
        final String html =
            "<html>\n"
            + "<script>\n"
            + "  function click() {\n"
            + "    var e = document.createEvent('MouseEvents');\n"
            + "    e.initMouseEvent('click', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);\n"
            + "    var d = document.getElementById('d');\n"
            + "    var canceled = !d.dispatchEvent(e);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='click()'><div id='d' onclick='alert(\"clicked\")'>foo</div></body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, actual);
        final String[] expected = {"clicked"};
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testHasAttribute() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var elt = document.body;\n"
            + "    alert(elt.hasAttribute('onload'));\n"
            + "    alert(elt.hasAttribute('onLoad'));\n"
            + "    alert(elt.hasAttribute('foo'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        final String[] expectedAlerts = {"true", "true", "false"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final List<String> actual = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, actual);

        assertEquals(expectedAlerts, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testDispatchEvent2() throws Exception {
        testHTMLFile("HTMLElementTest_dispatchEvent2.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetComponentVersion() throws Exception {
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

        final String[] expectedAlerts = {"undefined", "x86", "0", "undefined"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clientWidthAndHeight() throws Exception {
        clientWidthAndHeight(BrowserVersion.FIREFOX_2, "36", "46");
        clientWidthAndHeight(BrowserVersion.INTERNET_EXPLORER_7_0, "30", "40");
        clientWidthAndHeight(BrowserVersion.INTERNET_EXPLORER_6_0, "30", "40");
    }

    private void clientWidthAndHeight(final BrowserVersion version, final String... expected) throws Exception {
        final String content =
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
        final List<String> actual = new ArrayList<String>();
        loadPage(version, content, actual);
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementsByTagName() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
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

        final String[] expectedAlerts = {"div1", "div2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
