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
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.DateUtil;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Tests for {@link Document}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Barnaby Court
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Michael Ottati
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Ahmed Ashour
 * @author Rob Di Marco
 */
public class DocumentTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formsAccessor_TwoForms() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.forms.length)\n"
            + "    for(var i=0; i< document.forms.length; i++) {\n"
            + "        alert(document.forms[i].name )\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' value='foo' />\n"
            + "</form>\n"
            + "<form name='form2'>\n"
            + "    <input type='text' name='textfield2' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"2", "form1", "form2"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Previously, forms with no names were not being returned by document.forms.
     * @throws Exception if the test fails
     */
    @Test
    public void formsAccessor_FormWithNoName() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.forms.length)\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form>\n"
            + "    <input type='text' name='textfield1' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formsAccessor_NoForms() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.forms.length)\n"
            + "    for(var i=0; i< document.forms.length; i++) {\n"
            + "        alert(document.forms[i].name )\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"0"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formArray() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String firstContent
            = "<html><head><SCRIPT lang='JavaScript'>\n"
            + "    function doSubmit(formName){\n"
            + "        var form = document.forms[formName];\n" // This line used to blow up
            + "        form.submit()\n"
            + "}\n"
            + "</SCRIPT></head><body><form name='formName' method='POST' "
            + "action='" + URL_SECOND + "'>\n"
            + "<a href='.' id='testJavascript' name='testJavascript' "
            + "onclick=\" doSubmit('formName');return false;\">\n"
            + "Test Link </a><input type='submit' value='Login' "
            + "name='loginButton'></form>\n"
            + "</body></html> ";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals("", page.getTitleText());

        final HtmlAnchor testAnchor = page.getAnchorByName("testJavascript");
        final HtmlPage secondPage = (HtmlPage) testAnchor.click();
        assertEquals("second", secondPage.getTitleText());
    }

    /**
     * Test that forms is a live collection.
     * @throws Exception if the test fails
     */
    @Test
    public void formsLive() throws Exception {
        final String content =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "var oCol = document.forms;\n"
            + "alert(oCol.length);\n"
            + "function test()\n"
            + "{\n"
            + "    alert(oCol.length);\n"
            + "    alert(document.forms.length);\n"
            + "    alert(document.forms == oCol);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form id='myForm' action='foo.html'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"0", "1", "1", "true"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests for <tt>document.anchors</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void anchors() throws Exception {
        anchors(BrowserVersion.FIREFOX_2,
                new String[] {"0", "1", "1", "true", "name: end"});
        anchors(BrowserVersion.INTERNET_EXPLORER_6_0,
            new String[] {"0", "3", "3", "true", "id: firstLink"});
    }

    /**
     * Tests for <tt>document.anchors</tt>.
     * @throws Exception if the test fails
     */
    void anchors(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String content =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "var oCol = document.anchors;\n"
            + "alert(oCol.length);\n"
            + "function test()\n"
            + "{\n"
            + "    alert(oCol.length);\n"
            + "    alert(document.anchors.length);\n"
            + "    alert(document.anchors == oCol);\n"
            + "    if (document.anchors[0].name)\n"
            + "     alert('name: ' + document.anchors[0].name);\n"
            + "    else\n"
            + "     alert('id: ' + document.anchors[0].id);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<a href='foo.html' id='firstLink'>foo</a>\n"
            + "<a href='foo2.html'>foo2</a>\n"
            + "<a name='end'/>\n"
            + "<a href=''>null2</a>\n"
            + "<a id='endId'/>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(browserVersion, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests for <tt>document.links</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void links() throws Exception {
        final String content =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "var oCol = document.links;\n"
            + "alert(oCol.length);\n"
            + "function test()\n"
            + "{\n"
            + "    alert(oCol.length);\n"
            + "    alert(document.links.length);\n"
            + "    alert(document.links == oCol);\n"
            + "    alert(document.links[0].id);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<a href='foo.html' id='firstLink'>foo</a>\n"
            + "<a href='foo2.html'>foo2</a>\n"
            + "<a name='end'/>\n"
            + "<a href=''>null2</a>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"0", "3", "3", "true", "firstLink"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Ensures that <tt>document.createElement()</tt> works correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void createElement() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "  <head>\n"
            + "    <title>First</title>\n"
            + "    <script>\n"
            + "      function doTest() {\n"
            + "        // Create a DIV element.\n"
            + "        var div1 = document.createElement('div');\n"
            + "        alert('parentNode: ' + div1.parentNode);\n"
            + "        div1.id = 'div1';\n"
            + "        document.body.appendChild(div1);\n"
            + "        alert(div1.tagName);\n"
            + "        alert(div1.nodeType);\n"
            + "        alert(div1.nodeValue);\n"
            + "        alert(div1.nodeName);\n"
            + "        // Create an INPUT element.\n"
            + "        var input = document.createElement('input');\n"
            + "        input.id = 'text1id';\n"
            + "        input.name = 'text1name';\n"
            + "        input.value = 'text1value';\n"
            + "        var form = document.getElementById('form1');\n"
            + "        form.appendChild(input);\n"
            + "        alert(document.getElementById('button1id').value);\n"
            + "        alert(document.getElementById('text1id').value);\n"
            + "        // The default type of an INPUT element is 'text'.\n"
            + "        alert(document.getElementById('text1id').type);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <form name='form1' id='form1'>\n"
            + "      <input type='button' id='button1id' name='button1name' value='button1value'/>\n"
            + "      This is form1.\n"
            + "    </form>\n"
            + "  </body>\n"
            + "</html>";

        final String[] expectedAlerts = {"parentNode: null", "DIV", "1", "null",
            "DIV", "button1value", "text1value", "text"};
        createTestPageForRealBrowserIfNeeded(htmlContent, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("First", page.getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);

        final HtmlElement div1 = page.getHtmlElementById("div1");
        assertEquals("div", div1.getTagName());
        assertEquals((short) 1, div1.getNodeType());
        assertEquals(null, div1.getNodeValue());
        assertEquals("div", div1.getNodeName());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void documentCreateElement2() throws Exception {
        documentCreateElement2(BrowserVersion.INTERNET_EXPLORER_6_0, new String[] {
            "DIV,DIV,undefined,undefined,undefined", "HI:DIV,HI:DIV,undefined,undefined,undefined"});
        documentCreateElement2(BrowserVersion.FIREFOX_2, new String[] {
            "DIV,DIV,null,null,DIV", "HI:DIV,HI:DIV,null,null,HI:DIV"});
    }

    private void documentCreateElement2(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String htmlContent
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function doTest() {\n"
            + "        div = document.createElement('Div');\n"
            + "        alert(div.nodeName + ',' + div.tagName + ',' + div.namespaceURI + ',' + "
            +   "div.prefix + ',' + div.localName);\n"
            + "        div = document.createElement('Hi:Div');\n"
            + "        alert(div.nodeName + ',' + div.tagName + ',' + div.namespaceURI + ',' + "
            +   "div.prefix + ',' + div.localName);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "  </body>\n"
            + "</html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, htmlContent, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Ensures that <tt>document.createElementNS()</tt> works correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void createElementNS() throws Exception {
        try {
            createElementNS(BrowserVersion.INTERNET_EXPLORER_6_0, new String[] {});
            fail("IE6 does not support createElementNS");
        }
        catch (final Exception e) {
            //expected exception
        }
        createElementNS(BrowserVersion.FIREFOX_2, new String[] {
            "Some:Div,Some:Div,myNS,Some,Div"
        });
    }

    private void createElementNS(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String htmlContent
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function doTest() {\n"
            + "        div = document.createElementNS('myNS', 'Some:Div');\n"
            + "        alert(div.nodeName + ',' + div.tagName + ',' + div.namespaceURI + ',' + "
            +   "div.prefix + ',' + div.localName);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "  </body>\n"
            + "</html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, htmlContent, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for <tt>createTextNode</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void createTextNode() throws Exception {
        final String htmlContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var text1=document.createTextNode('Some Text');\n"
            + "    var body1=document.getElementById('body');\n"
            + "    body1.appendChild(text1);\n"
            + "    alert(text1.data);\n"
            + "    alert(text1.length);\n"
            + "    alert(text1.nodeType);\n"
            + "    alert(text1.nodeValue);\n"
            + "    alert(text1.nodeName);\n"
            + "}\n"
            + "</script></head><body onload='doTest()' id='body'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("First", page.getTitleText());

        final DomNode div1 =
            page.getHtmlElementById("body").getLastChild();
        assertEquals((short) 3, div1.getNodeType());
        assertEquals("Some Text", div1.getNodeValue());
        assertEquals("#text", div1.getNodeName());

        final String[] expectedAlerts = {"Some Text", "9", "3", "Some Text", "#text"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void createTextNodeWithHtml_FF() throws Exception {
        final String undefined = "undefined";
        final String original = "<p>a & b</p> &amp; \u0162 \" '";
        final String escaped = "&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '";
        final String[] expected = {original, original, undefined, escaped, undefined};
        createTextNodeWithHtml(BrowserVersion.FIREFOX_2, expected);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void createTextNodeWithHtml_IE() throws Exception {
        final String original = "<p>a & b</p> &amp; \u0162 \" '";
        final String escaped = "&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '";
        final String divPlusEscaped = "<DIV id=div>" + escaped + "</DIV>";
        final String[] expected = {original, original, divPlusEscaped, escaped, original};
        createTextNodeWithHtml(BrowserVersion.INTERNET_EXPLORER_6_0, expected);
    }

    /**
     * Verifies that when we create a text node and append it to an existing DOM node,
     * its <tt>outerHTML</tt>, <tt>innerHTML</tt> and <tt>innerText</tt> properties are
     * properly escaped.
     * @param browserVersion the browser version to use to run the test
     * @param expected the expected alerts
     * @throws Exception if the test fails
     */
    private void createTextNodeWithHtml(final BrowserVersion browserVersion, final String[] expected)
        throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      var node = document.createTextNode('<p>a & b</p> &amp; \\u0162 \" \\'');\n"
            + "      alert(node.data);\n"
            + "      alert(node.nodeValue);\n"
            + "      var div = document.getElementById('div');\n"
            + "      div.appendChild(node);\n"
            + "      alert(div.outerHTML);\n"
            + "      alert(div.innerHTML);\n"
            + "      alert(div.innerText);\n"
            + "   };\n"
            + "</script>\n"
            + "<div id='div'></div>\n"
            + "</body></html>";
        
        final List<String> actual = new ArrayList<String>();
        loadPage(browserVersion, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Regression test for RFE 741930.
     * @throws Exception if the test fails
     */
    @Test
    public void appendChild() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var form = document.forms['form1'];\n"
            + "    var div = document.createElement('DIV');\n"
            + "    form.appendChild(div);\n"
            + "    var elements = document.getElementsByTagName('DIV');\n"
            + "    alert(elements.length )\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that <tt>document.appendChild()</tt> works in IE and doesn't work in FF.
     * @throws Exception if an error occurs
     */
    @Test
    public void appendChildAtDocumentLevel() throws Exception {
        appendChildAtDocumentLevel(BrowserVersion.FIREFOX_2, "1", "exception");
        appendChildAtDocumentLevel(BrowserVersion.INTERNET_EXPLORER_6_0, "1", "2", "HTML", "DIV", "1");
        appendChildAtDocumentLevel(BrowserVersion.INTERNET_EXPLORER_7_0, "1", "2", "HTML", "DIV", "1");
    }

    private void appendChildAtDocumentLevel(final BrowserVersion version, final String... expected)
        throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var div = document.createElement('div');\n"
            + "      div.innerHTML = 'test';\n"
            + "      try {\n"
            + "        alert(document.childNodes.length); // 1\n"
            + "        document.appendChild(div); // Error in FF\n"
            + "        alert(document.childNodes.length); // 2\n"
            + "        alert(document.childNodes[0].tagName); // HTML\n"
            + "        alert(document.childNodes[1].tagName); // DIV\n"
            + "        alert(document.getElementsByTagName('div').length); // 1\n"
            + "      } catch(ex) {\n"
            + "        alert('exception');\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Regression test for appendChild of a text node.
     * @throws Exception if the test fails
     */
    @Test
    public void appendChild_textNode() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var form = document.forms['form1'];\n"
            + "    var child = document.createTextNode('Some Text');\n"
            + "    form.appendChild(child);\n"
            + "    alert(form.lastChild.data )\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"Some Text"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for <tt>cloneNode</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void cloneNode() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var form = document.forms['form1'];\n"
            + "    var cloneShallow = form.cloneNode(false);\n"
            + "    alert(cloneShallow!=null )\n"
            + "    alert(cloneShallow.firstChild==null )\n"
            + "    var cloneDeep = form.cloneNode(true);\n"
            + "    alert(cloneDeep!=null )\n"
            + "    alert(cloneDeep.firstChild!=null )\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "<p>hello world</p>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"true", "true", "true", "true"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for <tt>insertBefore</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void insertBefore() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var form = document.forms['form1'];\n"
            + "    var oldChild = document.getElementById('oldChild');\n"
            + "    var div = document.createElement('DIV');\n"
            + "    form.insertBefore(div, oldChild);\n"
            + "    alert(form.firstChild==div )\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'><div id='oldChild'/></form>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"true"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getBoxObjectFor() throws Exception {
        testHTMLFile("DocumentTest_getBoxObjectFor.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementById() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(top.document.getElementById('input1').value);\n"
            + "    alert(document.getElementById(''));\n"
            + "    alert(document.getElementById('non existing'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form id='form1'>\n"
            + "<input id='input1' name='foo' type='text' value='bar' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("First", page.getTitleText());

        final String[] expectedAlerts = {"bar", "null", "null"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementById_resetId() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input1=top.document.getElementById('input1');\n"
            + "    input1.id='newId';\n"
            + "    alert(top.document.getElementById('newId').value);\n"
            + "    alert(top.document.getElementById('input1'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form id='form1'>\n"
            + "<input id='input1' name='foo' type='text' value='bar' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("First", page.getTitleText());

        final String[] expectedAlerts = {"bar", "null"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementById_setNewId() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=top.document.getElementById('div1');\n"
            + "    div1.nextSibling.id='newId';\n"
            + "    alert(top.document.getElementById('newId').value);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form id='form1'>\n"
            + "<div id='div1'/><input name='foo' type='text' value='bar' />\n"
            + "</form>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"bar"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 740665.
     * @throws Exception if the test fails
     */
    @Test
    public void getElementById_divId() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var element = document.getElementById('id1');\n"
            + "    alert(element.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='id1'></div></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"id1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 740665.
     * @throws Exception if the test fails
     */
    @Test
    public void getElementById_scriptId() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script id='script1'>\n"
            + "function doTest() {\n"
            + "    alert(top.document.getElementById('script1').id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"script1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 740665.
     * @throws Exception if the test fails
     */
    @Test
    public void getElementById_scriptType() throws Exception {
        final String firstContent
            = "<html><head><title>First</title>\n"
            + "<script id='script1' type='text/javascript'>\n"
            + "doTest=function () {\n"
            + "    alert(top.document.getElementById('script1').type);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"text/javascript"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 740665.
     * @throws Exception if the test fails
     */
    @Test
    public void getElementById_scriptSrc() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webClient.setWebConnection(webConnection);

        final String firstContent
            = "<html><head><title>First</title>\n"
            + "<script id='script1' src='http://script'>\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";
        webConnection.setResponse(URL_FIRST, firstContent);

        final String scriptContent
            = "doTest=function () {\n"
            + "    alert(top.document.getElementById('script1').src);\n"
            + "}";
        webConnection.setResponse(new URL("http://script"), scriptContent, "text/javascript");

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"http://script"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for <tt>parentNode</tt> with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    public void parentNode_Nested() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('childDiv');\n"
            + "    alert(div1.parentNode.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='childDiv'></div></div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final HtmlElement div1 = firstPage.getHtmlElementById("childDiv");
        assertEquals("parentDiv", ((HtmlElement) div1.getParentNode()).getAttributeValue("id"));

        final String[] expectedAlerts = {"parentDiv"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for <tt>parentNode</tt> of document.
     * @throws Exception if the test fails
     */
    @Test
    public void parentNode_Document() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.parentNode==null);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"true"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for <tt>parentNode</tt> and <tt>createElement</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void parentNode_CreateElement() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.createElement('div');\n"
            + "    alert(div1.parentNode==null);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"true"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for <tt>parentNode</tt> and <tt>appendChild</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void parentNode_AppendChild() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var childDiv=document.getElementById('childDiv');\n"
            + "    var parentDiv=document.getElementById('parentDiv');\n"
            + "    parentDiv.appendChild(childDiv);\n"
            + "    alert(childDiv.parentNode.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'></div><div id='childDiv'></div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final HtmlElement childDiv = firstPage.getHtmlElementById("childDiv");
        assertEquals("parentDiv", ((HtmlElement) childDiv.getParentNode()).getAttributeValue("id"));

        final String[] expectedAlerts = {"parentDiv"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for <tt>documentElement</tt> of document.
     * @throws Exception if the test fails
     */
    @Test
    public void documentElement() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.documentElement!=null);\n"
            + "    alert(document.documentElement.tagName);\n"
            + "    alert(document.documentElement.parentNode==document);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"true", "HTML", "true"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for <tt>firstChild</tt> with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    public void firstChild_Nested() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('parentDiv');\n"
            + "    alert(div1.firstChild.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='childDiv'/><div id='childDiv2'/></div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final HtmlElement div1 = firstPage.getHtmlElementById("parentDiv");
        assertEquals("childDiv", ((HtmlElement) div1.getFirstChild()).getAttributeValue("id"));

        final String[] expectedAlerts = {"childDiv"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for <tt>firstChild</tt> and <tt>appendChild</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void firstChild_AppendChild() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var childDiv=document.getElementById('childDiv');\n"
            + "    var parentDiv=document.getElementById('parentDiv');\n"
            + "    parentDiv.appendChild(childDiv);\n"
            + "    var childDiv2=document.getElementById('childDiv2');\n"
            + "    parentDiv.appendChild(childDiv2);\n"
            + "    alert(parentDiv.firstChild.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'/><div id='childDiv'/><div id='childDiv2'/>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final HtmlElement parentDiv = firstPage.getHtmlElementById("parentDiv");
        assertEquals("childDiv", ((HtmlElement) parentDiv.getFirstChild()).getAttributeValue("id"));

        final String[] expectedAlerts = {"childDiv"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for lastChild with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    public void lastChild_Nested() throws Exception {
        final String lastContent
            = "<html><head><title>Last</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('parentDiv');\n"
            + "    alert(div1.lastChild.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='childDiv1'/><div id='childDiv'/></div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage lastPage = loadPage(lastContent, collectedAlerts);
        assertEquals("Last", lastPage.getTitleText());

        final HtmlElement parentDiv = lastPage.getHtmlElementById("parentDiv");
        assertEquals("childDiv", ((HtmlElement) parentDiv.getLastChild()).getAttributeValue("id"));

        final String[] expectedAlerts = {"childDiv"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for lastChild and appendChild.
     * @throws Exception if the test fails
     */
    @Test
    public void lastChild_AppendChild() throws Exception {
        final String lastContent
            = "<html><head><title>Last</title><script>\n"
            + "function doTest() {\n"
            + "    var childDiv1=document.getElementById('childDiv1');\n"
            + "    var parentDiv=document.getElementById('parentDiv');\n"
            + "    parentDiv.appendChild(childDiv1);\n"
            + "    var childDiv=document.getElementById('childDiv');\n"
            + "    parentDiv.appendChild(childDiv);\n"
            + "    alert(parentDiv.lastChild.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'/><div id='childDiv1'/><div id='childDiv'/>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage lastPage = loadPage(lastContent, collectedAlerts);
        assertEquals("Last", lastPage.getTitleText());

        final HtmlElement parentDiv = lastPage.getHtmlElementById("parentDiv");
        assertEquals("childDiv", ((HtmlElement) parentDiv.getLastChild()).getAttributeValue("id"));

        final String[] expectedAlerts = {"childDiv"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for nextSibling with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    public void nextSibling_Nested() throws Exception {
        final String lastContent
            = "<html><head><title>Last</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('previousDiv');\n"
            + "    alert(div1.nextSibling.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='previousDiv'/><div id='nextDiv'/></div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage lastPage = loadPage(lastContent, collectedAlerts);
        assertEquals("Last", lastPage.getTitleText());

        final HtmlElement div1 = lastPage.getHtmlElementById("previousDiv");
        assertEquals("nextDiv", ((HtmlElement) div1.getNextSibling()).getAttributeValue("id"));

        final String[] expectedAlerts = {"nextDiv"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for nextSibling and appendChild.
     * @throws Exception if the test fails
     */
    @Test
    public void nextSibling_AppendChild() throws Exception {
        final String lastContent
            = "<html><head><title>Last</title><script>\n"
            + "function doTest() {\n"
            + "    var previousDiv=document.getElementById('previousDiv');\n"
            + "    var parentDiv=document.getElementById('parentDiv');\n"
            + "    parentDiv.appendChild(previousDiv);\n"
            + "    var nextDiv=document.getElementById('nextDiv');\n"
            + "    parentDiv.appendChild(nextDiv);\n"
            + "    alert(previousDiv.nextSibling.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'/><div id='junk1'/><div id='previousDiv'/><div id='junk2'/><div id='nextDiv'/>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage lastPage = loadPage(lastContent, collectedAlerts);
        assertEquals("Last", lastPage.getTitleText());

        final HtmlElement previousDiv = lastPage.getHtmlElementById("previousDiv");
        assertEquals("nextDiv", ((HtmlElement) previousDiv.getNextSibling()).getAttributeValue("id"));

        final String[] expectedAlerts = {"nextDiv"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for previousSibling with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    public void previousSibling_Nested() throws Exception {
        final String lastContent
            = "<html><head><title>Last</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('nextDiv');\n"
            + "    alert(div1.previousSibling.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='previousDiv'/><div id='nextDiv'/></div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage lastPage = loadPage(lastContent, collectedAlerts);
        assertEquals("Last", lastPage.getTitleText());

        final HtmlElement div1 = lastPage.getHtmlElementById("nextDiv");
        assertEquals("previousDiv", ((HtmlElement) div1.getPreviousSibling()).getAttributeValue("id"));

        final String[] expectedAlerts = {"previousDiv"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for previousSibling and appendChild.
     * @throws Exception if the test fails
     */
    @Test
    public void previousSibling_AppendChild() throws Exception {
        final String lastContent
            = "<html><head><title>Last</title><script>\n"
            + "function doTest() {\n"
            + "    var previousDiv=document.getElementById('previousDiv');\n"
            + "    var parentDiv=document.getElementById('parentDiv');\n"
            + "    parentDiv.appendChild(previousDiv);\n"
            + "    var nextDiv=document.getElementById('nextDiv');\n"
            + "    parentDiv.appendChild(nextDiv);\n"
            + "    alert(nextDiv.previousSibling.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'/><div id='junk1'/><div id='previousDiv'/><div id='junk2'/><div id='nextDiv'/>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage lastPage = loadPage(lastContent, collectedAlerts);
        assertEquals("Last", lastPage.getTitleText());

        final HtmlElement nextDiv = lastPage.getHtmlElementById("nextDiv");
        assertEquals("previousDiv", ((HtmlElement) nextDiv.getPreviousSibling()).getAttributeValue("id"));

        final String[] expectedAlerts = {"previousDiv"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void allProperty_KeyByName() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.all['input1'].value);\n"
            + "    alert(document.all['foo2'].value);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'><form id='form1'>\n"
            + "    <input id='input1' name='foo1' type='text' value='tangerine' />\n"
            + "    <input id='input2' name='foo2' type='text' value='ginger' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"tangerine", "ginger"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 707750.
     * @throws Exception if the test fails
     */
    @Test
    public void allProperty_CalledDuringPageLoad() throws Exception {
        final String firstContent
            = "<html><body>\n"
            + "<div id='ARSMenuDiv1' style='VISIBILITY: hidden; POSITION: absolute; z-index: 1000000'></div>\n"
            + "<script language='Javascript'>\n"
            + "    var divObj = document.all['ARSMenuDiv1'];\n"
            + "    alert(divObj.tagName);\n"
            + "</script></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("", firstPage.getTitleText());

        final String[] expectedAlerts = {"DIV"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void write() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<script>\n"
            + "document.write(\"<div id='div1'></div>\");\n"
            + "document.write('<div', \" id='div2'>\", '</div>');\n"
            + "document.writeln('<div', \" id='div3'>\", '</div>');\n"
            + "</script>\n"
            + "</form></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        try {
            firstPage.getHtmlElementById("div1");
            firstPage.getHtmlElementById("div2");
            firstPage.getHtmlElementById("div3");
        }
        catch (final ElementNotFoundException e) {
            fail("Element not written to page as expected: " + e.getMessage());
        }
    }

    /**
     * Regression test for bug 743241.
     * @throws Exception if the test fails
     */
    @Test
    public void write_LoadScript() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webClient.setWebConnection(webConnection);

        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<script src='http://script'></script>\n"
            + "</form></body></html>";
        webConnection.setResponse(URL_FIRST, firstContent);

        final String scriptContent
            = "document.write(\"<div id='div1'></div>\");\n";
        webConnection.setResponse(new URL("http://script"), scriptContent, "text/javascript");

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        try {
            firstPage.getHtmlElementById("div1");
        }
        catch (final ElementNotFoundException e) {
            fail("Element not written to page as expected");
        }
    }

    /**
     * Regression test for bug 715379.
     * @throws Exception if the test fails
     */
    @Test
    public void write_script() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webClient.setWebConnection(webConnection);

        final String mainContent
            = "<html><head><title>Main</title></head><body>\n"
            + "<iframe name='iframe' id='iframe' src='http://first'></iframe>\n"
            + "<script type='text/javascript'>\n"
            + "document.write('<script type=\"text/javascript\" src=\"http://script\"></' + 'script>');\n"
            + "</script></body></html>";
        webConnection.setResponse(new URL("http://main"), mainContent);

        final String firstContent
            = "<html><body><h1 id='first'>First</h1></body></html>";
        webConnection.setResponse(URL_FIRST, firstContent);

        final String secondContent = "<html><body><h1 id='second'>Second</h1></body></html>";
        webConnection.setResponse(URL_SECOND, secondContent);

        final String scriptContent = "document.getElementById('iframe').src = '" + URL_SECOND + "';\n";
        webConnection.setResponse(new URL("http://script"), scriptContent, "text/javascript");

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage mainPage = (HtmlPage) webClient.getPage("http://main");
        assertEquals("Main", mainPage.getTitleText());

        final HtmlInlineFrame iFrame = (HtmlInlineFrame) mainPage.getHtmlElementById("iframe");

        assertEquals(URL_SECOND.toExternalForm(), iFrame.getSrcAttribute());

        final HtmlPage enclosedPage = (HtmlPage) iFrame.getEnclosedPage();
        // This will blow up if the script hasn't been written to the document
        // and executed so the second page has been loaded.
        enclosedPage.getHtmlElementById("second");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void write_InDOM() throws Exception {
        final String html
            = "<html><head><title>First</title></head><body>\n"
            + "<script type='text/javascript'>\n"
            + "document.write('<a id=\"blah\">Hello World</a>');\n"
            + "document.write('<a id=\"blah2\">Hello World 2</a>');\n"
            + "alert(document.getElementById('blah').tagName);\n"
            + "</script>\n"
            + "<a id='blah3'>Hello World 3</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(html, collectedAlerts);

        assertEquals("First", page.getTitleText());
        assertEquals(3, page.getElementsByTagName("a").getLength());
        assertEquals(new String[] {"A"}, collectedAlerts);
    }

    /**
     * Verifies that document.write() sends content to the correct destination (always somewhere in the body).
     * @throws Exception if an error occurs
     */
    @Test
    public void write_Destination() throws Exception {
        write_Destination(BrowserVersion.FIREFOX_2, "null", "[object HTMLBodyElement]", "s1 s2 s3 s4 s5");
        write_Destination(BrowserVersion.INTERNET_EXPLORER_6_0, "null", "[object]", "s1 s2 s3 s4 s5");
        write_Destination(BrowserVersion.INTERNET_EXPLORER_7_0, "null", "[object]", "s1 s2 s3 s4 s5");
    }

    private void write_Destination(final BrowserVersion version, final String... expected)
        throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>alert(document.body);</script>\n"
            + "    <script>document.write('<span id=\"s1\">1</span>');</script>\n"
            + "    <script>alert(document.body);</script>\n"
            + "    <title>test</title>\n"
            + "    <script>document.write('<span id=\"s2\">2</span>');</script>\n"
            + "  </head>\n"
            + "  <body id='foo'>\n"
            + "    <script>document.write('<span id=\"s3\">3</span>');</script>\n"
            + "    <span id='s4'>4</span>\n"
            + "    <script>document.write('<span id=\"s5\">5</span>');</script>\n"
            + "    <script>\n"
            + "      var s = '';\n"
            + "      for(var n = document.body.firstChild; n; n = n.nextSibling) {\n"
            + "        if(n.id) {\n"
            + "          if(s.length > 0) s+= ' ';\n"
            + "            s += n.id;\n"
            + "        }\n"
            + "      }\n"
            + "      alert(s);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that document.write() sends content to the correct destination (always somewhere in the body),
     * and that if a synthetic temporary body needs to be created, the attributes of the real body are eventually
     * used once the body is parsed.
     * @throws Exception if an error occurs
     */
    @Test
    public void write_BodyAttributesKept() throws Exception {
        write_BodyAttributesKept(BrowserVersion.FIREFOX_2, "null", "[object HTMLBodyElement]", "", "foo");
        write_BodyAttributesKept(BrowserVersion.INTERNET_EXPLORER_6_0, "null", "[object]", "", "foo");
        write_BodyAttributesKept(BrowserVersion.INTERNET_EXPLORER_7_0, "null", "[object]", "", "foo");
    }

    private void write_BodyAttributesKept(final BrowserVersion version, final String... expected)
        throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>alert(document.body);</script>\n"
            + "    <script>document.write('<span id=\"s1\">1</span>');</script>\n"
            + "    <script>alert(document.body);</script>\n"
            + "    <script>alert(document.body.id);</script>\n"
            + "    <title>test</title>\n"
            + "  </head>\n"
            + "  <body id='foo'>\n"
            + "    <script>alert(document.body.id);</script>\n"
            + "  </body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that document.write() sends content to the correct destination (always somewhere in the body),
     * and that script elements written to the document are executed in the correct order.
     * @throws Exception if an error occurs
     */
    @Test
    public void write_ScriptExecutionOrder() throws Exception {
        write_ScriptExecutionOrder(BrowserVersion.FIREFOX_2, "1", "2", "3");
        write_ScriptExecutionOrder(BrowserVersion.INTERNET_EXPLORER_6_0, "1", "2", "3");
        write_ScriptExecutionOrder(BrowserVersion.INTERNET_EXPLORER_7_0, "1", "2", "3");
    }

    private void write_ScriptExecutionOrder(final BrowserVersion version, final String... expected)
        throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>test</title>\n"
            + "    <script>alert('1');</script>\n"
            + "    <script>document.write('<scrip'+'t>alert(\"2\")</s'+'cript>');</script>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <script>document.write('<scrip'+'t>alert(\"3\")</s'+'cript>');</script>\n"
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
    public void write_AssignedToVar() throws Exception {
        // IE accepts the use of detached functions
        final String[] expectedAlertsIE = {};
        write_AssignedToVar(BrowserVersion.INTERNET_EXPLORER_6_0, expectedAlertsIE);

        // but FF doesn't
        final String[] expectedAlertsFF = {"exception occurred"};
        write_AssignedToVar(BrowserVersion.FIREFOX_2, expectedAlertsFF);
    }

    /**
     * @throws Exception if the test fails
     */
    private void write_AssignedToVar(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {

        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTheFoo() {\n"
            + "var d = document.writeln\n"
            + "try {\n"
            + "  d('foo')\n"
            + "} catch (e) { alert('exception occurred') }\n"
            + "  document.writeln('foo')\n"
            + "}\n"
            + "</script></head><body onload='doTheFoo()'>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(browserVersion, content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that calling document.write() after document parsing has finished results in an whole
     * new page being loaded.
     * @throws Exception if an error occurs
     */
    @Test
    public void write_WhenParsingFinished() throws Exception {
        write_WhenParsingFinished(BrowserVersion.FIREFOX_2);
        write_WhenParsingFinished(BrowserVersion.INTERNET_EXPLORER_6_0);
        write_WhenParsingFinished(BrowserVersion.INTERNET_EXPLORER_7_0);
    }

    private void write_WhenParsingFinished(final BrowserVersion browserVersion) throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() { document.write(1); document.write(2); document.close(); }\n"
            + "</script></head>\n"
            + "<body><span id='s' onclick='test()'>click</span></body></html>";

        final List<String> actual = new ArrayList<String>();
        HtmlPage page = loadPage(browserVersion, html, actual);
        assertEquals("click", page.getBody().asText());

        final HtmlSpan span = (HtmlSpan) page.getHtmlElementById("s");
        page = (HtmlPage) span.click();
        assertEquals("12", page.getBody().asText());
    }

    /**
     * Verifies that calls to document.open() are ignored while the page's HTML is being parsed.
     * @throws Exception if an error occurs
     */
    @Test
    public void open_IgnoredDuringParsing() throws Exception {
        final String html = "<html><body>1<script>document.open();document.write('2');</script>3</body></html>";
        final HtmlPage page = loadPage(html);
        assertEquals("123", page.getBody().asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void referrer() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final String firstContent
            = "<html><head><title>First</title></head><body onload='alert(document.referrer);'>\n"
            + "</form></body></html>";

        final List<NameValuePair> responseHeaders =
            Collections.singletonList(new NameValuePair("referrer", "http://ref"));
        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", responseHeaders);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        assertEquals(new String[] {"http://ref"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void referrer_NoneSpecified() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body onload='alert(document.referrer);'>\n"
            + "</form></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        assertEquals(new String[] {""}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void url() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body onload='alert(document.URL);'>\n"
            + "</form></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        assertEquals(new String[] {URL_GARGOYLE.toExternalForm()}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementsByTagName() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var elements = document.getElementsByTagName('input');\n"
            + "    for (var i=0; i<elements.length; i++) {\n"
            + "        alert(elements[i].type);\n"
            + "        alert(elements.item(i).type);\n"
            + "    }\n"
            + "    alert(elements == document.getElementsByTagName('input'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"button", "button", "true"};
        createTestPageForRealBrowserIfNeeded(firstContent, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(firstContent, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 740636.
     * @throws Exception if the test fails
     */
    @Test
    public void getElementsByTagName_CaseInsensitive() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var elements = document.getElementsByTagName('InPuT');\n"
            + "    for(i=0; i<elements.length; i++ ) {\n"
            + "        alert(elements[i].type);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"button"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 740605.
     * @throws Exception if the test fails
     */
    @Test
    public void getElementsByTagName_Inline() throws Exception {
        final String firstContent
            = "<html><body><script type=\"text/javascript\">\n"
            + "alert(document.getElementsByTagName('script').length);\n"
            + "</script></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(firstContent, collectedAlerts);
        final String[] expectedAlerts = {"1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 740605.
     * @throws Exception if the test fails
     */
    @Test
    public void getElementsByTagName_LoadScript() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webClient.setWebConnection(webConnection);

        final String firstContent
            = "<html><body><script src=\"http://script\"></script></body></html>";
        webConnection.setResponse(URL_FIRST, firstContent);

        final String scriptContent
            = "alert(document.getElementsByTagName('script').length);\n";
        webConnection.setResponse(new URL("http://script"), scriptContent, "text/javascript");

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webClient.getPage(URL_FIRST);

        final String[] expectedAlerts = {"1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void all_WithParentheses() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var length = document.all.length;\n"
            + "    for(i=0; i< length; i++ ) {\n"
            + "        alert(document.all(i).tagName);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"HTML", "HEAD", "TITLE", "SCRIPT", "BODY"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void all_IndexByInt() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var length = document.all.length;\n"
            + "    for(i=0; i< length; i++ ) {\n"
            + "        alert(document.all[i].tagName);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"HTML", "HEAD", "TITLE", "SCRIPT", "BODY"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void all_Item() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.all.item(0).tagName);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"HTML"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void all_NamedItem() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.all.namedItem('form1').name);\n"
            + "    alert(document.all.namedItem('form2').id);\n"
            + "    alert(document.all.namedItem('form3').length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'></form>\n"
            + "<form id='form2'></form>\n"
            + "<form name='form3'></form>\n"
            + "<form name='form3'></form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"form1", "form2", "2"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void all_tags() throws Exception {
        all_tags(BrowserVersion.INTERNET_EXPLORER_6_0);
        all_tags(BrowserVersion.FIREFOX_2);
    }

    private void all_tags(final BrowserVersion browerVersion) throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var inputs = document.all.tags('input');\n"
            + "    var inputCount = inputs.length;\n"
            + "    for(i=0; i< inputCount; i++ ) {\n"
            + "        alert(inputs[i].name);\n"
            + "    }\n"
            + "    // Make sure tags() returns an element array that you can call item() on.\n"
            + "    alert(document.all.tags('input').item(0).name);\n"
            + "    alert(document.all.tags('input').item(1).name);\n"
            + "    // Make sure tags() returns an empty element array if there are no matches.\n"
            + "    alert(document.all.tags('xxx').length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<input type='text' name='a' value='1'>\n"
            + "<input type='text' name='b' value='1'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"a", "b", "a", "b", "0"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(browerVersion, content, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Firefox supports document.all, but it is "hidden".
     * @throws Exception if the test fails
     */
    @Test
    public void all_AsBoolean() throws Exception {
        final String[] alertsIE = {"true", "true"};
        all_AsBoolean(BrowserVersion.INTERNET_EXPLORER_6_0, alertsIE);
        final String[] alertsFF = {"false", "true"};
        all_AsBoolean(BrowserVersion.FIREFOX_2, alertsFF);
    }

    private void all_AsBoolean(final BrowserVersion browser,
        final String[] expectedAlerts) throws Exception {
        final String content = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.all ? true : false);\n"
            + "    alert(Boolean(document.all));\n"
            + "}\n"
            + "</script><body onload='doTest()'>\n"
            + "</body></html>";

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browser, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Makes sure that the document.all collection contents are not cached if the
     * collection is accessed before the page has finished loading.
     * @throws Exception if the test fails
     */
    @Test
    public void all_Caching() throws Exception {
        final String firstContent
            = "<html><head><title>Test</title></head>\n"
            + "<body onload='alert(document.all.b.value)'>\n"
            + "<input type='text' name='a' value='1'>\n"
            + "<script>alert(document.all.a.value)</script>\n"
            + "<input type='text' name='b' value='2'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("Test", firstPage.getTitleText());

        final String[] expectedAlerts = {"1", "2"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void all_NotExisting() throws Exception {
        final String content = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.all('notExisting'));\n"
            + "}\n"
            + "</script><body onload='doTest()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"null"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void cookie_read() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var cookieSet = document.cookie.split('; ');\n"
            + "    var setSize = cookieSet.length;\n"
            + "    var crumbs;\n"
            + "    var x=0;\n"
            + "    for (x=0;((x<setSize)); x++) {\n"
            + "        crumbs = cookieSet[x].split('=');\n"
            + "        alert (crumbs[0]);\n"
            + "        alert (crumbs[1]);\n"
            + "    } \n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final URL url = URL_FIRST;
        webConnection.setResponse(url, firstContent);
        webClient.setWebConnection(webConnection);

        final HttpState state = webConnection.getState();
        state.addCookie(new Cookie("first", "one", "two", "/", -1, false));
        state.addCookie(new Cookie("first", "three", "four", "/", -1, false));

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"one", "two", "three", "four" };
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementsByName() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var elements = document.getElementsByName('name1');\n"
            + "    for (var i=0; i<elements.length; i++) {\n"
            + "        alert(elements[i].value);\n"
            + "        alert(elements.item(i).value);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form>\n"
            + "<input type='radio' name='name1' value='value1'>\n"
            + "<input type='radio' name='name1' value='value2'>\n"
            + "<input type='button' name='name2' value='value3'>\n"
            + "</form>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"value1", "value1", "value2", "value2"};
        createTestPageForRealBrowserIfNeeded(firstContent, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(firstContent, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void body_read() throws Exception {
        final String html = "<html><head><title>First</title></head>\n"
            + "<body id='IAmTheBody' onload='alert(document.body.id)'>\n"
            + "</body></html>";

        final List<String> expectedAlerts  = new ArrayList<String>();
        expectedAlerts.add("IAmTheBody");
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);

        final String html2 = "<html>\n"
            + "<frameset onload='alert(document.body.tagName)'>\n"
            + "<frame src='about:blank' name='foo'>\n"
            + "</frameset></html>";

        expectedAlerts.clear();
        collectedAlerts.clear();

        expectedAlerts.add("FRAMESET");

        loadPage(html2, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test the access to the images value. This should return the 2 images in the document
     * @throws Exception if the test fails
     */
    @Test
    public void images() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.images.length);\n"
            + "    alert(allImages.length);\n"
            + "    alert(document.images == allImages);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<img src='firstImage'>\n"
            + "<script>\n"
            + "var allImages = document.images;\n"
            + "alert(allImages.length);\n"
            + "</script>\n"
            + "<form>\n"
            + "<img src='2ndImage'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage firstPage = loadPage(firstContent, collectedAlerts);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"1", "2", "2", "true"};
        createTestPageForRealBrowserIfNeeded(firstContent, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test setting and reading the title for an existing title.
     * @throws Exception if the test fails
     */
    @Test
    public void settingTitle() throws Exception {
        final String content
            = "<html><head><title>Bad Title</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "    document.title = 'correct title';\n"
            + "    alert(document.title)\n"
            + "</script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("correct title", page.getTitleText());

        final String[] expectedAlerts = {"correct title"};
        assertEquals("Test the alert", expectedAlerts, collectedAlerts);
    }

    /**
     * Test setting and reading the title for when the is not in the page to begin.
     * @throws Exception if the test fails
     */
    @Test
    public void settingMissingTitle() throws Exception {
        final String content = "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "    document.title = 'correct title';\n"
            + "    alert(document.title)\n"
            + "</script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        final String[] expectedAlerts = {"correct title"};
        assertEquals("Test the alert", expectedAlerts, collectedAlerts);
    }

    /**
     * Test setting and reading the title for when the is not in the page to begin.
     * @throws Exception if the test fails
     */
    @Test
    public void settingBlankTitle() throws Exception {
        final String content = "<html><head><title></title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "    document.title = 'correct title';\n"
            + "    alert(document.title)\n"
            + "</script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("correct title", page.getTitleText());

        final String[] expectedAlerts = {"correct title"};
        assertEquals("Test the alert", expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void title() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.title)\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"foo"};

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test the ReadyState. This should only work in IE.
     * Currently locked out since the browser type of code is not working.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void readyStateNonIE() throws Exception {
        final WebClient client = new WebClient(BrowserVersion.FIREFOX_2);
        final MockWebConnection webConnection = new MockWebConnection(client);
        final String content = "<html><head>\n"
            + "<script>\n"
            + "function testIt() {\n"
            + "  alert(document.readyState);\n"
            + "}\n"
            + "alert(document.readyState);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onLoad='testIt()'></body></html>";

        webConnection.setResponse(URL_FIRST, content);
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        client.getPage(URL_FIRST);

        final String[] expectedAlerts = {"undefined", "undefined"};

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test the ReadyState. This should only work in IE
     *
     * @throws Exception if the test fails
     */
    @Test
    public void readyStateIE() throws Exception {
        final WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_6_0);
        final MockWebConnection webConnection = new MockWebConnection(client);
        final String content = "<html><head>\n"
            + "<script>\n"
            + "function testIt() {\n"
            + "  alert(document.readyState);\n"
            + "}\n"
            + "alert(document.readyState);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onLoad='testIt()'></body></html>";
        webConnection.setResponse(URL_FIRST, content);
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        client.getPage(URL_FIRST);

        final String[] expectedAlerts = {"loading", "complete"};

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Calling document.body before the page is fully loaded used to cause an exception.
     * @throws Exception if the test fails
     */
    @Test
    public void documentWithNoBody() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "alert(document.body)\n"
            + "</script></head><body></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        final String[] expectedAlerts = {"null"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * IE has a bug which returns the element by name if it can not find it by ID.
     * @throws Exception if the test fails
     */
    @Test
    public void getElementByIdForIE() throws Exception {
        final String content
            = "<html><head><title>foo</title></head>\n"
            + "<body>\n"
            + "<input type='text' name='findMe'>\n"
            + "<input type='text' id='findMe2' name='byId'>\n"
            + "<script>\n"
            + "alert(document.getElementById('findMe').name)\n"
            + "alert(document.getElementById('findMe2').name)\n"
            + "</script></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        final String[] expectedAlerts = {"findMe", "byId"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * IE has a bug which returns the element by name if it can not find it by ID.
     * @throws Exception if the test fails
     */
    @Test
    public void getElementByIdForNetscape() throws Exception {
        final WebClient client = new WebClient(BrowserVersion.FIREFOX_2);
        final MockWebConnection webConnection = new MockWebConnection(client);
        final String content
            = "<html><head><title>foo</title></head>\n"
            + "<body>\n"
            + "<input type='text' name='findMe'>\n"
            + "<input type='text' id='findMe2' name='byId'>\n"
            + "<script>\n"
            + "alert(document.getElementById('findMe'))\n"
            + "alert(document.getElementById('findMe2').name)\n"
            + "</script></body></html>";

        webConnection.setResponse(URL_FIRST, content);
        client.setWebConnection(webConnection);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);

        final String[] expectedAlerts = {"null", "byId"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buildCookie() throws Exception {
        checkCookie(Document.buildCookie("toto=foo", URL_FIRST), "toto", "foo", "", "first", false, null);
        checkCookie(Document.buildCookie("toto=", URL_FIRST), "toto", "", "", "first", false, null);
        checkCookie(Document.buildCookie("toto=foo;secure", URL_FIRST), "toto", "foo", "", "first", true, null);
        checkCookie(Document.buildCookie("toto=foo;path=/myPath;secure", URL_FIRST),
                "toto", "foo", "/myPath", "first", true, null);

        // Check that leading and trailing whitespaces are ignored
        checkCookie(Document.buildCookie("   toto=foo;  path=/myPath  ; secure  ", URL_FIRST),
                "toto", "foo", "/myPath", "first", true, null);

        // Check that we accept reserved attribute names (e.g expires, domain) in any case
        checkCookie(Document.buildCookie("toto=foo; PATH=/myPath; SeCURE", URL_FIRST),
                "toto", "foo", "/myPath", "first", true, null);

        // Check that we are able to parse and set the expiration date correctly
        final String dateString = "Fri, 21 Jul 2006 20:47:11 UTC";
        final Date date = DateUtil.parseDate(dateString);
        checkCookie(Document.buildCookie("toto=foo; expires=" + dateString, URL_FIRST),
                "toto", "foo", "", "first", false, date);
    }

    private void checkCookie(final Cookie cookie, final String name, final String value,
            final String path, final String domain, final boolean secure, final Date date) {
        assertEquals(name, cookie.getName());
        assertEquals(value, cookie.getValue());
        assertNull(cookie.getComment());
        assertEquals(path, cookie.getPath());
        assertEquals(domain, cookie.getDomain());
        assertEquals(secure, cookie.getSecure());
        assertEquals(date, cookie.getExpiryDate());
    }

    /**
     * Test that <tt>img</tt> and <tt>form</tt> can be retrieved directly by name, but not <tt>a</tt>, <tt>input</tt>
     * or <tt>button</tt>.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void directAccessByName() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.myImage.id);\n"
            + "    alert(document.myImage2.length);\n"
            + "    alert(document.myForm.tagName);\n"
            + "    alert(document.myAnchor);\n"
            + "    alert(document.myInput);\n"
            + "    alert(document.myInputImage);\n"
            + "    alert(document.myButton);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<img src='foo' name='myImage' id='myImageId'>\n"
            + "<img src='foo' name='myImage2'>\n"
            + "<img src='foo' name='myImage2'>\n"
            + "<a name='myAnchor'/>\n"
            + "<form name='myForm'>\n"
            + "<input name='myInput' type='text'>\n"
            + "<input name='myInputImage' type='image' src='foo'>\n"
            + "<button name='myButton'>foo</button>\n"
            + "</form>\n"
            + "</body></html>";

        final String[] expectedAlerts
            = {"myImageId", "2", "FORM", "undefined", "undefined", "undefined", "undefined"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeInManyTimes() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.getElementById('inner').parentNode.id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<script>\n"
            + "document.write('<div id=\"outer\">');\n"
            + "document.write('<div id=\"inner\"/>');\n"
            + "document.write('</div>');\n"
            + "</script>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"outer"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeWithSpace() throws Exception {
        final String content = "<html><body><script>\n"
            + "document.write('Hello ');\n"
            + "document.write('World');\n"
            + "</script>\n"
            + "</body></html>";

        final String[] expectedAlerts = {};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        client.setWebConnection(webConnection);
        webConnection.setResponse(URL_FIRST, content);
        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        assertTrue(page.asText().indexOf("Hello World") >= 0);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeWithSplitAnchorTag() throws Exception {
        final String content = "<html><body><script>\n"
            + "document.write(\"<a href=\'start.html\");\n"
            + "document.write(\"\'>\");\n"
            + "document.write('click here</a>');\n"
            + "</script>\n"
            + "</body></html>";

        final String[] expectedAlerts = {};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        client.setWebConnection(webConnection);
        webConnection.setResponse(URL_FIRST, content);
        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final List<HtmlAnchor> anchorList = page.getAnchors();
        assertEquals(1, anchorList.size());
        final HtmlAnchor anchor = anchorList.get(0);
        assertEquals("start.html", anchor.getHrefAttribute());
        assertEquals("click here", anchor.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeScriptInManyTimes() throws Exception {
        final String content = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "document.write('<script src=\"script.js\">');\n"
            + "document.write('<' + '/script>');\n"
            + "document.write('<script>alert(\"foo2\");</' + 'script>');\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"foo", "foo2"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final URL scriptUrl = new URL(URL_FIRST + "/script.js");
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        client.setWebConnection(webConnection);
        webConnection.setDefaultResponse(content);
        webConnection.setResponse(scriptUrl, "alert('foo');\n", "text/javascript");

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        client.getPage(URL_FIRST);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for bug 1613119.
     * @throws Exception if the test fails
     */
    @Test
    public void writeAddNodesInCorrectPositions() throws Exception {
        final String content = "<html><head><title>foo</title></head>\n"
            + "<body id=\"theBody\">\n"
            + "<div id='target1'></div>\n"
            + "<script>\n"
            + "document.write(\""
            + "<div>"
            + "  <sc\"+\"ript id='scr1'>document.write('<div id=\\\"div1\\\" />');</s\"+\"cript>"
            + "  <sc\"+\"ript id='scr2'>document.write('<div id=\\\"div2\\\" />');</s\"+\"cript>"
            + "</div>"
            + "\");\n"
 /*           + "document.getElementById('target1').innerHTML = \""
            + "<div>\n"
            + "  <sc\"+\"ript id='scr3'>document.write('<div id=\\\"div3\\\" />');</s\"+\"cript>\n"
            + "  <sc\"+\"ript id='scr4'>document.write('<div id=\\\"div4\\\" />');</s\"+\"cript>\n"
            + "</div>\n"
            + "\";\n"
  */
            + "</script>\n"
            + "<script>\n"
            + "function alertId(obj) { alert(obj != null ? obj.id : 'null'); }\n"
            + "alertId(document.getElementById('div1').previousSibling);\n"
            + "alertId(document.getElementById('div2').previousSibling);\n"
 /*           + "alertId(document.getElementById('div3').previousSibling);\n"
            + "alertId(document.getElementById('div4').previousSibling);\n"
  */
            + "</script>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"scr1", "scr2"/*, "scr3", "scr4"*/};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        
        assertEquals(expectedAlerts, collectedAlerts);
    }
  
    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domain() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'gargoylesoftware.com';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {
            "www.gargoylesoftware.com", "gargoylesoftware.com"
        };

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

  /**
    * @throws Exception if the test fails
    */
    @Test
    public void domainMixedCaseNetscape() throws Exception {
        final URL urlGargoyleUpperCase = new URL("http://WWW.GARGOYLESOFTWARE.COM");

        final WebClient client = new WebClient(BrowserVersion.FIREFOX_2);
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String content = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'GaRgOyLeSoFtWaRe.CoM';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        webConnection.setResponse(urlGargoyleUpperCase, content);
        client.setWebConnection(webConnection);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(urlGargoyleUpperCase);

        final String[] expectedAlerts = {"www.gargoylesoftware.com", "gargoylesoftware.com"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

  /**
    * @throws Exception if the test fails
    */
    @Test
    public void domainMixedCase() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'GaRgOyLeSoFtWaRe.CoM';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        final String[] expectedAlerts = {"www.gargoylesoftware.com", "GaRgOyLeSoFtWaRe.CoM"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domainLong() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'd4.d3.d2.d1.gargoylesoftware.com';\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'd1.gargoylesoftware.com';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final String[] expectedAlerts =
        {"d4.d3.d2.d1.gargoylesoftware.com", "d4.d3.d2.d1.gargoylesoftware.com", "d1.gargoylesoftware.com"};

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts, new URL("http://d4.d3.d2.d1.gargoylesoftware.com"));
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domainSetSelf() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'localhost';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"localhost", "localhost"};

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts, new URL("http://localhost"));
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domainTooShort() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'com';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        try {
            loadPage(content, collectedAlerts);
        }
        catch (final ScriptException ex) {
            return;
        }
        fail();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeFrameRelativeURLMultipleFrameset() throws Exception {
        final String framesetContent = "<html><head><title>frameset</title></head>\n"
            + "<script>\n"
            + "    document.write('<frameset><frame src=\"frame.html\"/></frameset>');\n"
            + "</script>\n"
            + "<frameset><frame src='blank.html'/></frameset>\n"
            + "</html>";

        final URL baseURL = new URL("http://base/subdir/");
        final URL framesetURL = new URL(baseURL.toExternalForm() + "test.html");
        final URL frameURL = new URL(baseURL.toExternalForm() + "frame.html");
        final URL blankURL = new URL(baseURL.toExternalForm() + "blank.html");

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(framesetURL, framesetContent);
        webConnection.setResponseAsGenericHtml(frameURL, "frame");
        webConnection.setResponseAsGenericHtml(blankURL, "blank");
        client.setWebConnection(webConnection);

        final HtmlPage framesetPage = (HtmlPage) client.getPage(framesetURL);
        final FrameWindow frame = (FrameWindow) framesetPage.getFrames().get(0);

        assertNotNull(frame);
        assertEquals(frameURL.toExternalForm(), frame.getEnclosedPage().getWebResponse().getUrl().toExternalForm());
        assertEquals("frame", ((HtmlPage) frame.getEnclosedPage()).getTitleText());
    }

   /**
    * Test for bug 1185389.
    * @throws Exception if the test fails
    */
    @Test
    public void writeAddNodesToCorrectParent() throws Exception {
        if (notYetImplemented()) {
            return;
        }

        final String content = "<html><head><title>foo</title></head>\n"
            + "<body id=\"theBody\">\n"
            + "<script>\n"
            + "document.write('<p id=\"para1\">Paragraph #1</p>');\n"
            + "document.write('<p id=\"para2\">Paragraph #2</p>');\n"
            + "document.write('<p id=\"para3\">Paragraph #3</p>');\n"
            + "alert(document.getElementById('para1').parentNode.id);\n"
            + "alert(document.getElementById('para2').parentNode.id);\n"
            + "alert(document.getElementById('para3').parentNode.id);\n"
            + "</script>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"theBody", "theBody", "theBody"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
   
   /**
     * @throws Exception if the test fails
     */
    @Test
    public void scriptsArray() throws Exception {
        final String htmlContent = "<html><head><script lang='JavaScript'>\n"
            + "    function doTest(){\n"
            + "        alert(document.scripts.length);\n" // This line used to blow up
            + "}\n"
            + "</script></head><body onload='doTest();'>\n"
            + "<script>var scriptTwo = 1;</script>\n"
            + "</body></html> ";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(htmlContent, collectedAlerts);
        assertEquals(new String[] {"2"}, collectedAlerts);
    }

    /**
     * Any document.XXX should first look at elements named XXX before using standard functions.
     * @throws Exception if the test fails
     */
    @Test
    public void precedence() throws Exception {
        final String htmlContent = "<html><head></head>\n"
            + "<body>\n"
            + "<form name='writeln'>foo</form>\n"
            + "<script>alert(document.writeln.tagName);</script>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"FORM"};
        createTestPageForRealBrowserIfNeeded(htmlContent, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(htmlContent, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void frames() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test(){\n"
            + "  if (document.frames)\n"
            + "  {\n"
            + "    alert(document.frames == window.frames);\n"
            + "    alert(document.frames.length);\n"
            + "    alert(document.frames(0).location);\n"
            + "    alert(document.frames('foo').location);\n"
            + "  }\n"
            + "  else\n"
            + "    alert('not defined');\n"
            + "}\n"
            + "</script></head><body onload='test();'>\n"
            + "<iframe src='about:blank' name='foo'></iframe>\n"
            + "</body></html> ";

        final List<String> collectedAlerts = new ArrayList<String>();

        // test for IE
        final String[] expectedAlerts = {"true", "1", "about:blank", "about:blank"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);

        // test for Mozilla
        collectedAlerts.clear();
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(new String[] {"not defined"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void defaultViewAndParentWindow() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test(){\n"
            + "    alert(document.defaultView == window);\n"
            + "    alert(document.parentWindow == window);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html> ";

        final List<String> collectedAlerts = new ArrayList<String>();

        // test for Mozilla
        final String[] expectedAlertsMoz = {"true", "false"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlertsMoz);
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(expectedAlertsMoz, collectedAlerts);

        // test for IE
        final String[] expectedAlertsIE = {"false", "true"};
        collectedAlerts.clear();
        createTestPageForRealBrowserIfNeeded(html, expectedAlertsIE);
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, html, collectedAlerts);
        assertEquals(expectedAlertsIE, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void put() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + "alert(document.foo);\n"
                + "if (!document.foo) document.foo = 123;\n"
                + "alert(document.foo);\n"
                + "</script>\n"
                + "</form>\n" + "</body>\n" + "</html>";

        final String[] expectedAlerts = {"undefined", "123"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(html, collectedAlerts);
        
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests <tt>document.cloneNode()</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void documentCloneNode() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        
        final String html = "<html><body id='hello' onload='doTest()'>\n"
                + "  <script id='jscript'>\n"
                + "    function doTest() {\n"
                + "      var clone = document.cloneNode(true);\n"
                + "      alert(clone.body);\n"
                + "      assert(clone,'clone.body !== document.body');\n"
                + "      assert(clone,'clone.getElementById(\"id1\") !== document.getElementById(\"id1\")');\n"
                + "      assert(clone,'document.ownerDocument == null');\n"
                + "      assert(clone,'clone.ownerDocument == document');\n"
                + "      assert(clone,'document.getElementById(\"id1\").ownerDocument === document');\n"
                + "      assert(clone,'clone.getElementById(\"id1\").ownerDocument === document');\n"
                + "    }\n"
                + "    function assert(clone, expr, info) {\n"
                + "      if (!eval(expr)) {\n"
                + "        alert('failed assertion: ' + expr + ', info: ' + info);\n"
                + "      }\n"
                + "    }\n"
                + "  </script>\n"
                + "  <div id='id1'>hello</div>\n"
                + "</body>\n" + "</html>";

        final String[] expectedAlerts = {"[object]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(html, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void createStyleSheet() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "var s = document.createStyleSheet('foo.css', 1);\n"
            + "alert(s);\n"
            + "</script></head><body>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        final String[] expectedAlerts = {"[object]"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void createDocumentFragment() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var textarea = document.getElementById('myTextarea');\n"
            + "    textarea.value += fragment.nodeName + '_';\n"
            + "    textarea.value += fragment.nodeValue + '_';\n"
            + "    textarea.value += fragment.nodeType + '_';\n"
            + "    textarea.value += fragment.parentNode + '_';\n"
            + "    textarea.value += fragment.childNodes.length + '_';\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<textarea id='myTextarea' cols='40'></textarea>\n"
            + "</body></html>";

        final String expected = "#document-fragment_null_11_null_0_";
        final HtmlPage page = loadPage(content);
        final HtmlTextArea textArea = (HtmlTextArea) page.getHtmlElementById("myTextarea");
        assertEquals(expected, textArea.getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void createEvents_FF() throws Exception {
        createEvents_FF("Event", true, BrowserVersion.FIREFOX_2);
        createEvents_FF("Events", true, BrowserVersion.FIREFOX_2);
        createEvents_FF("HTMLEvents", true, BrowserVersion.FIREFOX_2);
        createEvents_FF("Bogus", false, BrowserVersion.FIREFOX_2);
    }

    private void createEvents_FF(final String eventType, final boolean isSupportedType,
        final BrowserVersion version) throws Exception {
        final String content =
              "<html><head><title>foo</title><script>\n"
            + "var e = document.createEvent('" + eventType + "');\n"
            + "alert(e != null);\n"
            + "alert(typeof e);\n"
            + "alert(e);\n"
            + "alert(e.cancelable);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        try {
            final String[] expected = {"true", "object", "[object Event]", "true"};
            createTestPageForRealBrowserIfNeeded(content, expected);
            loadPage(version, content, actual);
            assertTrue("Test was expected to fail, but did not: type=" + eventType, isSupportedType);
            assertEquals(expected, actual);
        }
        catch (final Exception e) {
            assertTrue("Test was not expected to fail, but did with message " + e.getMessage() + " for type="
                + eventType, !isSupportedType);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void createEventObject_IE() throws Exception {
        final String content =
              "<html><head><title>foo</title><script>\n"
            + "var e = document.createEventObject();\n"
            + "alert(e != null);\n"
            + "alert(typeof e);\n"
            + "alert(e);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, actual);
        final String[] expected = {"true", "object", "[object]"};
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void lementFromPoint() throws Exception {
        elementFromPoint(BrowserVersion.INTERNET_EXPLORER_7_0);
        try {
            elementFromPoint(BrowserVersion.FIREFOX_2);
            fail("elementFromPoint is not supported in Firefox.");
        }
        catch (final Exception e) {
            //expected
        }
    }

    private void elementFromPoint(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var e = document.elementFromPoint(-1,-1);\n"
            + "    alert(e.nodeName);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals("BODY", collectedAlerts.get(0));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void createElementWithAngleBrackets() throws Exception {
        createElementWithAngleBrackets(BrowserVersion.INTERNET_EXPLORER_7_0);
        createElementWithAngleBrackets(BrowserVersion.FIREFOX_2);
    }
    
    private void createElementWithAngleBrackets(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var select = document.createElement('<select>');\n"
            + "    alert(select.add);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertFalse(collectedAlerts.get(0).equals("undefined"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void createElementWithHtml() throws Exception {
        createElementWithHtml(BrowserVersion.INTERNET_EXPLORER_7_0);
        try {
            createElementWithHtml(BrowserVersion.FIREFOX_2);
            fail("document.createElement(html) is not supported with Firefox");
        }
        catch (final Exception e) {
            //expected
        }
    }

    private void createElementWithHtml(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var select = document.createElement(\"<select id='mySelect'><option>hello</option>\");\n"
            + "    alert(select.add);\n"
            + "    alert(select.id);\n"
            + "    alert(select.childNodes.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertFalse(collectedAlerts.get(0).equals("undefined"));
        assertEquals("mySelect", collectedAlerts.get(1));
        //make sure the element has no children
        assertEquals("0", collectedAlerts.get(2));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void styleSheets() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(document.styleSheets);\n"
            + "    alert(document.styleSheets.length);\n"
            + "    alert(document.styleSheets == document.styleSheets);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"[object]", "0", "true"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void queryCommandSupported() throws Exception {
        testHTMLFile("DocumentTest_queryCommandSupported.html");
    }

    /**
     * Minimal test for <tt>execCommand</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void execCommand() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    document.designMode = 'On';\n"
            + "    document.execCommand('Bold', false, null);\n"
            + "    try {\n"
            + "      document.execCommand('foo', false, null);\n"
            + "    }\n"
            + "    catch (e) {\n"
            + "      alert('command foo not supported');\n"
            + "    }\n"
            + "    document.designMode = 'Off';\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expected = {"command foo not supported"};
        createTestPageForRealBrowserIfNeeded(content, expected);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expected, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void evaluate_caseInsensitiveAttribute() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var expr = './/*[@CLASS]';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    alert(result.iterateNext());\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <h1 class='title'>Some text</h1>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object HTMLHeadingElement]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writeStyle() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        writeStyle(BrowserVersion.FIREFOX_2, new String[] {"SCRIPT", "TITLE"});
        writeStyle(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"STYLE", "SCRIPT"});
    }

    private void writeStyle(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String content = "<html><head><title>foo</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  document.write('<style type=\"text/css\" id=\"myStyle\">');\n"
            + "  document.write('  .nwr {white-space: nowrap;}');\n"
            + "  document.write('</style>');\n"
            + "  document.write('<div id=\"myDiv\">');\n"
            + "  document.write('</div>');\n"
            + "  alert(document.getElementById('myDiv').previousSibling.nodeName);\n"
            + "  alert(document.getElementById('myStyle').previousSibling.nodeName);\n"
            + "</script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void importNode() throws Exception {
        importNode(false, new String[] {"div1", "null", "0"});
        importNode(true, new String[] {"div1", "null", "2", "1"});
    }

    private void importNode(final boolean deep, final String[] expectedAlerts) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var node = document.importNode(document.getElementById('div1'), " + deep + ");\n"
            + "    alert(node.id);\n"
            + "    alert(node.parentNode);\n"
            + "    alert(node.childNodes.length);\n"
            + "    if (node.childNodes.length != 0)\n"
            + "      alert(node.childNodes[0].childNodes.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'><div id='div1_1'><div id='div1_1_1'></div></div><div id='div1_2'></div></div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that HtmlUnit behaves correctly when a document is missing the <tt>body</tt> tag (it
     * needs to be added once the document has finished loading).
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void noBodyTag() throws Exception {
        noBodyTag(BrowserVersion.FIREFOX_2, new String[] {"1: null", "2: null", "3: [object HTMLBodyElement]"});
        noBodyTag(BrowserVersion.INTERNET_EXPLORER_6_0, new String[] {"1: null", "2: [object]", "3: [object]"});
        noBodyTag(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"1: null", "2: [object]", "3: [object]"});
    }

    private void noBodyTag(final BrowserVersion version, final String[] expected) throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <script>alert('1: ' + document.body);</script>\n"
            + "    <script defer=''>alert('2: ' + document.body);</script>\n"
            + "    <script>window.onload = function() { alert('3: ' + document.body); }</script>\n"
            + "  </head>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that HtmlUnit behaves correctly when an iframe's document is missing the <tt>body</tt> tag (it
     * needs to be added once the document has finished loading).
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void noBodyTag_IFrame() throws Exception {
        noBodyTag_IFrame(BrowserVersion.FIREFOX_2, "1: [object HTMLBodyElement]", "2: [object HTMLBodyElement]");
        noBodyTag_IFrame(BrowserVersion.INTERNET_EXPLORER_6_0, "1: null", "2: [object]");
        noBodyTag_IFrame(BrowserVersion.INTERNET_EXPLORER_7_0, "1: null", "2: [object]");
    }

    private void noBodyTag_IFrame(final BrowserVersion version, final String... expected) throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <iframe id='i'></iframe>\n"
            + "    <script>\n"
            + "      alert('1: ' + document.getElementById('i').contentWindow.document.body);\n"
            + "      window.onload = function() {\n"
            + "        alert('2: ' + document.getElementById('i').contentWindow.document.body);\n"
            + "      };\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Verifies that the document object has a <tt>fireEvent</tt> method and that it works correctly (IE only).
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void fireEvent() throws Exception {
        fireEvent(BrowserVersion.FIREFOX_2);
        fireEvent(BrowserVersion.INTERNET_EXPLORER_6_0, "x");
        fireEvent(BrowserVersion.INTERNET_EXPLORER_7_0, "x");
    }

    private void fireEvent(final BrowserVersion version, final String... expected) throws Exception {
        final String html =
              "<html><body>\n"
            + " <span id='s' onclick='\n"
            + "  if(document.fireEvent) {\n"
            + "    document.onkeydown=function(){alert(\"x\")};\n"
            + "    document.fireEvent(\"onkeydown\")\n"
            + "  }\n"
            + " '>abc</span>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(version, html, actual);
        final HtmlSpan span = (HtmlSpan) page.getHtmlElementById("s");
        span.click();
        assertEquals(expected, actual);
    }
}
