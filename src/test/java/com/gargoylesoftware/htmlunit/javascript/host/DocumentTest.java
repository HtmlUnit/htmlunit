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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

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
 * @author Sudhan Moghe
 */
@RunWith(BrowserRunner.class)
public class DocumentTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2", "form1", "form2" })
    public void formsAccessor_TwoForms() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * Previously, forms with no names were not being returned by document.forms.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void formsAccessor_FormWithNoName() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void formsAccessor_NoForms() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formArray() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstHtml
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
        final String secondHtml
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setResponse(URL_SECOND, secondHtml);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
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
    @Alerts({ "0", "1", "1", "true" })
    public void formsLive() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "var oCol = document.forms;\n"
            + "alert(oCol.length);\n"
            + "function test() {\n"
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

        loadPageWithAlerts(html);
    }

    /**
     * Tests for <tt>document.anchors</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "0", "3", "3", "true", "id: firstLink" },
            FF = { "0", "1", "1", "true", "name: end" })
    public void anchors() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "var oCol = document.anchors;\n"
            + "alert(oCol.length);\n"
            + "function test() {\n"
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

        loadPageWithAlerts(html);
    }

    /**
     * Tests for <tt>document.links</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "3", "3", "true", "firstLink" })
    public void links() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "var oCol = document.links;\n"
            + "alert(oCol.length);\n"
            + "function test() {\n"
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

        loadPageWithAlerts(html);
    }

    /**
     * Ensures that <tt>document.createElement()</tt> works correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "parentNode: null", "DIV", "1", "null", "DIV", "button1value", "text1value", "text" })
    public void createElement() throws Exception {
        final String html
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

        final HtmlPage page = loadPageWithAlerts(html);

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
    @Alerts(IE = { "DIV,DIV,undefined,undefined,undefined", "HI:DIV,HI:DIV,undefined,undefined,undefined" },
            FF = { "DIV,DIV,null,null,DIV", "HI:DIV,HI:DIV,null,null,HI:DIV" })
    public void documentCreateElement2() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function doTest() {\n"
            + "        div = document.createElement('Div');\n"
            + "        alert(div.nodeName + ',' + div.tagName + ',' + div.namespaceURI + ',' + "
            + "div.prefix + ',' + div.localName);\n"
            + "        div = document.createElement('Hi:Div');\n"
            + "        alert(div.nodeName + ',' + div.tagName + ',' + div.namespaceURI + ',' + "
            + "div.prefix + ',' + div.localName);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Ensures that <tt>document.createElementNS()</tt> works correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts(FF = { "Some:Div,Some:Div,myNS,Some,Div", "svg,svg,http://www.w3.org/2000/svg,null,svg" })
    public void createElementNS() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest() {\n"
            + "  var div = document.createElementNS('myNS', 'Some:Div');\n"
            + "  alert(div.nodeName + ',' + div.tagName + ',' + div.namespaceURI + ',' + "
            + "div.prefix + ',' + div.localName);\n"
            + "  var svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');\n"
            + "  alert(svg.nodeName + ',' + svg.tagName + ',' + svg.namespaceURI + ',' + "
            + "svg.prefix + ',' + svg.localName);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for <tt>createTextNode</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "Some Text", "9", "3", "Some Text", "#text" })
    public void createTextNode() throws Exception {
        final String html
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

        final HtmlPage page = loadPageWithAlerts(html);

        final DomNode div1 = page.getHtmlElementById("body").getLastChild();
        assertEquals((short) 3, div1.getNodeType());
        assertEquals("Some Text", div1.getNodeValue());
        assertEquals("#text", div1.getNodeName());
    }

    /**
     * Verifies that when we create a text node and append it to an existing DOM node,
     * its <tt>outerHTML</tt>, <tt>innerHTML</tt> and <tt>innerText</tt> properties are
     * properly escaped.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "<p>a & b</p> &amp; \u0162 \" '",
                "<p>a & b</p> &amp; \u0162 \" '",
                "undefined",
                "&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '",
                "undefined" },
            IE = { "<p>a & b</p> &amp; \u0162 \" '",
                "<p>a & b</p> &amp; \u0162 \" '",
                "<DIV id=div>&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '</DIV>",
                "&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '",
                "<p>a & b</p> &amp; \u0162 \" '" })
    public void createTextNodeWithHtml_FF() throws Exception {
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

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for RFE 741930.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void appendChild() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that <tt>document.appendChild()</tt> works in IE and doesn't work in FF.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "1", "2", "HTML", "DIV", "1" }, FF = { "1", "exception" })
    public void appendChildAtDocumentLevel() throws Exception {
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

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for appendChild of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some Text")
    public void appendChild_textNode() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for <tt>cloneNode</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "true", "true", "true" })
    public void cloneNode() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var form = document.forms['form1'];\n"
            + "    var cloneShallow = form.cloneNode(false);\n"
            + "    alert(cloneShallow!=null);\n"
            + "    alert(cloneShallow.firstChild==null);\n"
            + "    var cloneDeep = form.cloneNode(true);\n"
            + "    alert(cloneDeep!=null);\n"
            + "    alert(cloneDeep.firstChild!=null);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "<p>hello world</p>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for <tt>insertBefore</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void insertBefore() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "bar", "null", "null" })
    public void getElementById() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "bar", "null" })
    public void getElementById_resetId() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bar")
    public void getElementById_setNewId() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    div1.firstChild.id='newId';\n"
            + "    alert(document.getElementById('newId').value);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form id='form1'>\n"
            + "<div id='div1'><input name='foo' type='text' value='bar'></div>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 740665.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("id1")
    public void getElementById_divId() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var element = document.getElementById('id1');\n"
            + "    alert(element.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='id1'></div></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 740665.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("script1")
    public void getElementById_scriptId() throws Exception {
        final String html
            = "<html><head><title>First</title><script id='script1'>\n"
            + "function doTest() {\n"
            + "    alert(top.document.getElementById('script1').id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 740665.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text/javascript")
    public void getElementById_scriptType() throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script id='script1' type='text/javascript'>\n"
            + "doTest=function () {\n"
            + "    alert(top.document.getElementById('script1').type);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 740665.
     * @throws Exception if the test fails
     */
    @Test
    public void getElementById_scriptSrc() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webClient.setWebConnection(webConnection);

        final String html
            = "<html><head><title>First</title>\n"
            + "<script id='script1' src='http://script'>\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";
        webConnection.setResponse(URL_FIRST, html);

        final String script
            = "doTest=function () {\n"
            + "    alert(top.document.getElementById('script1').src);\n"
            + "}";
        webConnection.setResponse(new URL("http://script/"), script, "text/javascript");

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final String[] expectedAlerts = {"http://script"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for <tt>parentNode</tt> with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("parentDiv")
    public void parentNode_Nested() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('childDiv');\n"
            + "    alert(div1.parentNode.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='childDiv'></div></div>\n"
            + "</body></html>";

        final HtmlPage firstPage = loadPageWithAlerts(html);

        final HtmlElement div1 = firstPage.getHtmlElementById("childDiv");
        assertEquals("parentDiv", ((HtmlElement) div1.getParentNode()).getAttribute("id"));
    }

    /**
     * Regression test for <tt>parentNode</tt> of document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void parentNode_Document() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.parentNode==null);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for <tt>parentNode</tt> and <tt>createElement</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void parentNode_CreateElement() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.createElement('div');\n"
            + "    alert(div1.parentNode==null);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for <tt>parentNode</tt> and <tt>appendChild</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("parentDiv")
    public void parentNode_AppendChild() throws Exception {
        final String html
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

        final HtmlPage firstPage = loadPageWithAlerts(html);

        final HtmlElement childDiv = firstPage.getHtmlElementById("childDiv");
        assertEquals("parentDiv", ((HtmlElement) childDiv.getParentNode()).getAttribute("id"));
    }

    /**
     * Regression test for <tt>documentElement</tt> of document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "HTML", "true" })
    public void documentElement() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.documentElement!=null);\n"
            + "    alert(document.documentElement.tagName);\n"
            + "    alert(document.documentElement.parentNode==document);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for <tt>firstChild</tt> with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("childDiv")
    public void firstChild_Nested() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('parentDiv');\n"
            + "    alert(div1.firstChild.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='childDiv'/><div id='childDiv2'/></div>\n"
            + "</body></html>";

        final HtmlPage firstPage = loadPageWithAlerts(html);
        assertEquals("First", firstPage.getTitleText());

        final HtmlElement div1 = firstPage.getHtmlElementById("parentDiv");
        assertEquals("childDiv", ((HtmlElement) div1.getFirstChild()).getAttribute("id"));
    }

    /**
     * Regression test for <tt>firstChild</tt> and <tt>appendChild</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("childDiv")
    public void firstChild_AppendChild() throws Exception {
        final String html
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

        final HtmlPage firstPage = loadPageWithAlerts(html);
        assertEquals("First", firstPage.getTitleText());

        final HtmlElement parentDiv = firstPage.getHtmlElementById("parentDiv");
        assertEquals("childDiv", ((HtmlElement) parentDiv.getFirstChild()).getAttribute("id"));
    }

    /**
     * Regression test for lastChild with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("childDiv")
    public void lastChild_Nested() throws Exception {
        final String html
            = "<html><head><title>Last</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('parentDiv');\n"
            + "    alert(div1.lastChild.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='childDiv1'></div><div id='childDiv'></div></div>\n"
            + "</body></html>";

        final HtmlPage lastPage = loadPageWithAlerts(html);
        assertEquals("Last", lastPage.getTitleText());

        final HtmlElement parentDiv = lastPage.getHtmlElementById("parentDiv");
        assertEquals("childDiv", ((HtmlElement) parentDiv.getLastChild()).getAttribute("id"));
    }

    /**
     * Regression test for lastChild and appendChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("childDiv")
    public void lastChild_AppendChild() throws Exception {
        final String html
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

        final HtmlPage lastPage = loadPageWithAlerts(html);
        assertEquals("Last", lastPage.getTitleText());

        final HtmlElement parentDiv = lastPage.getHtmlElementById("parentDiv");
        assertEquals("childDiv", ((HtmlElement) parentDiv.getLastChild()).getAttribute("id"));
    }

    /**
     * Regression test for nextSibling with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("nextDiv")
    public void nextSibling_Nested() throws Exception {
        final String html
            = "<html><head><title>Last</title><script>\n"
            + "function doTest() {\n"
            + "    var div1 = document.getElementById('previousDiv');\n"
            + "    alert(div1.nextSibling.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='previousDiv'></div><div id='nextDiv'></div></div>\n"
            + "</body></html>";

        final HtmlPage lastPage = loadPageWithAlerts(html);
        assertEquals("Last", lastPage.getTitleText());

        final HtmlElement div1 = lastPage.getHtmlElementById("previousDiv");
        assertEquals("nextDiv", ((HtmlElement) div1.getNextSibling()).getAttribute("id"));
    }

    /**
     * Regression test for nextSibling and appendChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("nextDiv")
    public void nextSibling_AppendChild() throws Exception {
        final String html
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

        final HtmlPage lastPage = loadPageWithAlerts(html);
        assertEquals("Last", lastPage.getTitleText());

        final HtmlElement previousDiv = lastPage.getHtmlElementById("previousDiv");
        assertEquals("nextDiv", ((HtmlElement) previousDiv.getNextSibling()).getAttribute("id"));
    }

    /**
     * Regression test for previousSibling with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("previousDiv")
    public void previousSibling_Nested() throws Exception {
        final String html
            = "<html><head><title>Last</title><script>\n"
            + "function doTest() {\n"
            + "    var div1 = document.getElementById('nextDiv');\n"
            + "    alert(div1.previousSibling.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='previousDiv'></div><div id='nextDiv'></div></div>\n"
            + "</body></html>";

        final HtmlPage lastPage = loadPageWithAlerts(html);
        assertEquals("Last", lastPage.getTitleText());

        final HtmlElement div1 = lastPage.getHtmlElementById("nextDiv");
        assertEquals("previousDiv", ((HtmlElement) div1.getPreviousSibling()).getAttribute("id"));
    }

    /**
     * Regression test for previousSibling and appendChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("previousDiv")
    public void previousSibling_AppendChild() throws Exception {
        final String html
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

        final HtmlPage lastPage = loadPageWithAlerts(html);
        assertEquals("Last", lastPage.getTitleText());

        final HtmlElement nextDiv = lastPage.getHtmlElementById("nextDiv");
        assertEquals("previousDiv", ((HtmlElement) nextDiv.getPreviousSibling()).getAttribute("id"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "tangerine", "ginger" })
    public void allProperty_KeyByName() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 707750.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("DIV")
    public void allProperty_CalledDuringPageLoad() throws Exception {
        final String html
            = "<html><body>\n"
            + "<div id='ARSMenuDiv1' style='VISIBILITY: hidden; POSITION: absolute; z-index: 1000000'></div>\n"
            + "<script language='Javascript'>\n"
            + "    var divObj = document.all['ARSMenuDiv1'];\n"
            + "    alert(divObj.tagName);\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void referrer() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String firstHtml = "<html><head><title>First</title></head><body>\n"
            + "<a href='" + URL_SECOND + "'>click me</a></body></html>";

        final String secondHtml = "<html><head><title>Second</title></head><body onload='alert(document.referrer);'>\n"
            + "</form></body></html>";
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setResponse(URL_SECOND, secondHtml);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());
        firstPage.getAnchors().get(0).click();

        assertEquals(new String[] {URL_FIRST.toExternalForm()}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void referrer_NoneSpecified() throws Exception {
        final String html
            = "<html><head><title>First</title></head><body onload='alert(document.referrer);'>\n"
            + "</form></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void url() throws Exception {
        final String html
            = "<html><head><title>First</title></head><body onload='alert(document.URL);'>\n"
            + "</form></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "button", "button", "true" })
    public void getElementsByTagName() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 740636.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("button")
    public void getElementsByTagName_CaseInsensitive() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 740605.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void getElementsByTagName_Inline() throws Exception {
        final String html
            = "<html><body><script type=\"text/javascript\">\n"
            + "alert(document.getElementsByTagName('script').length);\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 740605.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void getElementsByTagName_LoadScript() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webClient.setWebConnection(webConnection);

        final String html = "<html><body><script src=\"http://script\"></script></body></html>";
        webConnection.setResponse(URL_FIRST, html);

        final String script = "alert(document.getElementsByTagName('script').length);\n";
        webConnection.setResponse(new URL("http://script/"), script, "text/javascript");

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webClient.getPage(URL_FIRST);

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "HTML", "HEAD", "TITLE", "SCRIPT", "BODY" })
    public void all_WithParentheses() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var length = document.all.length;\n"
            + "    for(i=0; i< length; i++ ) {\n"
            + "        alert(document.all(i).tagName);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "HTML", "HEAD", "TITLE", "SCRIPT", "BODY" })
    public void all_IndexByInt() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var length = document.all.length;\n"
            + "    for(i=0; i< length; i++ ) {\n"
            + "        alert(document.all[i].tagName);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HTML")
    public void all_Item() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.all.item(0).tagName);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "form1", "form2", "2" })
    public void all_NamedItem() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception",
            IE = { "a", "b", "a", "b", "0" })
    public void all_tags() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "  try {\n"
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
            + "  } catch (e) { alert('exception') }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<input type='text' name='a' value='1'>\n"
            + "<input type='text' name='b' value='1'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Firefox supports document.all, but it is "hidden".
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "false", "true" },
            IE = { "true", "true" })
    public void all_AsBoolean() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.all ? true : false);\n"
            + "    alert(Boolean(document.all));\n"
            + "}\n"
            + "</script><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Makes sure that the document.all collection contents are not cached if the
     * collection is accessed before the page has finished loading.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2" })
    public void all_Caching() throws Exception {
        final String html
            = "<html><head><title>Test</title></head>\n"
            + "<body onload='alert(document.all.b.value)'>\n"
            + "<input type='text' name='a' value='1'>\n"
            + "<script>alert(document.all.a.value)</script>\n"
            + "<input type='text' name='b' value='2'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "undefined", "undefined" }, IE = { "null", "null" })
    public void all_NotExisting() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.all('notExisting'));\n"
            + "    alert(document.all.item('notExisting'));\n"
            + "}\n"
            + "</script><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "value1", "value1", "value2", "value2" })
    public void getElementsByName() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("IAmTheBody")
    public void body_read() throws Exception {
        final String html = "<html><head><title>First</title></head>\n"
            + "<body id='IAmTheBody' onload='alert(document.body.id)'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("FRAMESET")
    public void body_readFrameset() throws Exception {
        final String html = "<html>\n"
            + "<frameset onload='alert(document.body.tagName)'>\n"
            + "<frame src='about:blank' name='foo'>\n"
            + "</frameset></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test the access to the images value. This should return the 2 images in the document
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2", "2", "true" })
    public void images() throws Exception {
        final String html
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

        loadPageWithAlerts(html);
    }

    /**
     * Test setting and reading the title for an existing title.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("correct title")
    public void settingTitle() throws Exception {
        final String html
            = "<html><head><title>Bad Title</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "    document.title = 'correct title';\n"
            + "    alert(document.title)\n"
            + "</script>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("correct title", page.getTitleText());
    }

    /**
     * Test setting and reading the title for when the is not in the page to begin.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("correct title")
    public void settingMissingTitle() throws Exception {
        final String html = "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "    document.title = 'correct title';\n"
            + "    alert(document.title)\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test setting and reading the title for when the is not in the page to begin.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("correct title")
    public void settingBlankTitle() throws Exception {
        final String html = "<html><head><title></title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "    document.title = 'correct title';\n"
            + "    alert(document.title)\n"
            + "</script>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("correct title", page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void title() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.title)\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("foo", page.getTitleText());
    }

    /**
     * Test the ReadyState which is an IE feature.
     * FF supports this since 3.6.
     * http://sourceforge.net/tracker/?func=detail&aid=3030247&group_id=47038&atid=448266
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "loading", "complete" })
    public void readyState() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function testIt() {\n"
            + "  alert(document.readyState);\n"
            + "}\n"
            + "alert(document.readyState);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onLoad='testIt()'></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Calling document.body before the page is fully loaded used to cause an exception.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void documentWithNoBody() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "alert(document.body)\n"
            + "</script></head><body></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * IE has a bug which returns the element by name if it cannot find it by ID.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "null", "byId" },
            IE = { "findMe", "byId" })
    public void getElementById_findByName() throws Exception {
        final String html
            = "<html><head><title>foo</title></head>\n"
            + "<body>\n"
            + "<input type='text' name='findMe'>\n"
            + "<input type='text' id='findMe2' name='byId'>\n"
            + "<script>\n"
            + "var o = document.getElementById('findMe');\n"
            + "alert(o ? o.name : 'null');\n"
            + "alert(document.getElementById('findMe2').name);\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test that <tt>img</tt> and <tt>form</tt> can be retrieved directly by name, but not <tt>a</tt>, <tt>input</tt>
     * or <tt>button</tt>.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "myImageId", "2", "FORM", "undefined", "undefined", "undefined", "undefined" })
    public void directAccessByName() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
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

        loadPageWithAlerts(html);
    }

     /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3_6 = { "undefined", "exception occured" },
            FF = { "[object HTMLCollection]", "2" },
            IE = { "[object HTMLCollection]", "2" })
    public void scriptsArray() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><script lang='JavaScript'>\n"
            + "    function doTest(){\n"
            + "        alert(document.scripts);\n"
            + "        try {\n"
            + "          alert(document.scripts.length);\n" // This line used to blow up
            + "        }\n"
            + "        catch (e) { alert('exception occured') }\n"
            + "}\n"
            + "</script></head><body onload='doTest();'>\n"
            + "<script>var scriptTwo = 1;</script>\n"
            + "</body></html> ";

        loadPageWithAlerts(html);
    }

    /**
     * Any document.foo should first look at elements named "foo" before using standard functions.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "function", "undefined" }, IE = { "object", "FORM" })
    public void precedence() throws Exception {
        final String html = "<html><head></head>\n"
            + "<body>\n"
            + "<form name='writeln'>foo</form>\n"
            + "<script>alert(typeof document.writeln);</script>\n"
            + "<script>alert(document.writeln.tagName);</script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "true", "false" },
            IE = { "false", "true" })
    public void defaultViewAndParentWindow() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test(){\n"
            + "    alert(document.defaultView == window);\n"
            + "    alert(document.parentWindow == window);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html> ";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "undefined", "123" })
    public void put() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + "alert(document.foo);\n"
                + "if (!document.foo) document.foo = 123;\n"
                + "alert(document.foo);\n"
                + "</script>\n"
                + "</form>\n" + "</body>\n" + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Tests <tt>document.cloneNode()</tt>.
     * IE specific.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "[object HTMLDocument]", "[object HTMLBodyElement]",
            "true", "true", "true", "false", "true", "false" },
            IE = { "[object]", "[object]", "true", "true", "true", "true", "true", "true" })
    @NotYetImplemented
    public void documentCloneNode() throws Exception {
        final String html = "<html><body id='hello' onload='doTest()'>\n"
                + "  <script id='jscript'>\n"
                + "    function doTest() {\n"
                + "      var clone = document.cloneNode(true);\n"
                + "      alert(clone);\n"
                + "      if (clone != null) {\n"
                + "        alert(clone.body);\n"
                + "        alert(clone.body !== document.body);\n"
                + "        alert(clone.getElementById(\"id1\") !== document.getElementById(\"id1\"));\n"
                + "        alert(document.ownerDocument == null);\n"
                + "        alert(clone.ownerDocument == document);\n"
                + "        alert(document.getElementById(\"id1\").ownerDocument === document);\n"
                + "        alert(clone.getElementById(\"id1\").ownerDocument === document);\n"
                + "      }\n"
                + "    }\n"
                + "  </script>\n"
                + "  <div id='id1'>hello</div>\n"
                + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = "[object]")
    public void createStyleSheet() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "try {\n"
            + "  var s = document.createStyleSheet('foo.css', 1);\n"
            + "  alert(s);\n"
            + "}\n"
            + "catch(ex) {\n"
            + "  alert('exception');\n"
            + "}\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void createDocumentFragment() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
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
        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals(expected, textArea.getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "true", "object", "[object Event]", "true" })
    public void createEvent_FF_Event() throws Exception {
        createEvent_FF("Event");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "true", "object", "[object Event]", "true" })
    public void createEvent_FF_Events() throws Exception {
        createEvent_FF("Events");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "true", "object", "[object Event]", "true" })
    public void createEvent_FF_HTMLEvents() throws Exception {
        createEvent_FF("HTMLEvents");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void createEvent_FF_Bogus() throws Exception {
        createEvent_FF("Bogus");
    }

    private void createEvent_FF(final String eventType) throws Exception {
        final String html =
              "<html><head><title>foo</title><script>\n"
            + "try {\n"
            + "  var e = document.createEvent('" + eventType + "');\n"
            + "  alert(e != null);\n"
            + "  alert(typeof e);\n"
            + "  alert(e);\n"
            + "  alert(e.cancelable);\n"
            + "}\n"
            + "catch (e) { alert('exception') }\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts(FF = { "null", "null", "[object HTMLDivElement]" })
    public void createEvent_FF_Target() throws Exception {
        final String html =
              "<html>\n"
            + "    <body onload='test()'>\n"
            + "        <div id='d' onclick='alert(event.target)'>abc</div>\n"
            + "        <script>\n"
            + "            function test() {\n"
            + "                var event = document.createEvent('MouseEvents');\n"
            + "                alert(event.target);\n"
            + "                event.initMouseEvent('click', true, true, window,\n"
            + "                    1, 0, 0, 0, 0, false, false, false, false, 0, null);\n"
            + "                alert(event.target);\n"
            + "                document.getElementById('d').dispatchEvent(event);\n"
            + "            }\n"
            + "        </script>\n"
            + "    </body>\n"
            + "</html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "exception", IE = { "true", "object", "[object]" })
    public void createEventObject_IE() throws Exception {
        final String html =
              "<html><head><title>foo</title><script>\n"
            + "try {\n"
            + "  var e = document.createEventObject();\n"
            + "  alert(e != null);\n"
            + "  alert(typeof e);\n"
            + "  alert(e);\n"
            + "} catch(ex) {\n"
            + "  alert('exception');\n"
            + "}\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "null", IE = "BODY")
    public void elementFromPoint() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var e = document.elementFromPoint(-1,-1);\n"
            + "    alert(e != null ? e.nodeName : null);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "[object StyleSheetList]", "0", "true" },
            IE = { "[object]", "0", "true" })
    public void styleSheets() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(document.styleSheets);\n"
            + "    alert(document.styleSheets.length);\n"
            + "    alert(document.styleSheets == document.styleSheets);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Various <tt>document.designMode</tt> tests when the document is in the root HTML page.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "off", "off", "on", "on", "on", "off", "off", "off", "off" },
            IE = { "Off", "!", "Off", "Off", "Off", "!", "Off", "Off", "Off", "Off", "Off" })
    public void designMode_root() throws Exception {
        designMode("document");
    }

    /**
     * Various <tt>document.designMode</tt> tests when the document is in an <tt>iframe</tt>.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "off", "off", "on", "on", "on", "off", "off", "off", "off" },
            IE = { "Inherit", "!", "Inherit", "On", "On", "!", "On", "Off", "Off", "Inherit", "Inherit" })
    public void designMode_iframe() throws Exception {
        designMode("window.frames['f'].document");
    }

    private void designMode(final String doc) throws Exception {
        final String html = "<html><body><iframe name='f' id='f'></iframe><script>\n"
            + "var d = " + doc + ";\n"
            + "alert(d.designMode);\n"
            + "try{d.designMode='abc';}catch(e){alert('!');}\n"
            + "alert(d.designMode);\n"
            + "try{d.designMode='on';}catch(e){alert('!');}\n"
            + "alert(d.designMode);\n"
            + "try{d.designMode='On';}catch(e){alert('!');}\n"
            + "alert(d.designMode);\n"
            + "try{d.designMode='abc';}catch(e){alert('!');}\n"
            + "alert(d.designMode);\n"
            + "try{d.designMode='Off';}catch(e){alert('!');}\n"
            + "alert(d.designMode);\n"
            + "try{d.designMode='off';}catch(e){alert('!');}\n"
            + "alert(d.designMode);\n"
            + "try{d.designMode='Inherit';}catch(e){alert('!');}\n"
            + "alert(d.designMode);\n"
            + "try{d.designMode='inherit';}catch(e){alert('!');}\n"
            + "alert(d.designMode);\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that enabling design mode on a document in Firefox implicitly creates a selection range.
     * Required for YUI rich text editor unit tests.
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts(FF = { "0", "1", "1" })
    public void designMode_createsSelectionRange() throws Exception {
        final String html1 = "<html><body><iframe id='i' src='" + URL_SECOND + "'></iframe></body></html>";
        final String html2 = "<html><body onload='test()'>\n"
            + "<script>\n"
            + "  var selection = document.selection; // IE\n"
            + "  if(!selection) selection = window.getSelection(); // FF\n"
            + "  function test() {\n"
            + "    alert(selection.rangeCount);\n"
            + "    document.designMode='on';\n"
            + "    alert(selection.rangeCount);\n"
            + "    document.designMode='off';\n"
            + "    alert(selection.rangeCount);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        final WebClient client = getWebClient();
        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(URL_SECOND, html2);
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "error", "error", "true", "true", "true" },
            IE = { "true", "true", "true", "true", "true" })
    public void queryCommandEnabled() throws Exception {
        final String html = "<html><body onload='x()'><iframe name='f' id='f'></iframe><script>\n"
            + "function x() {\n"
            + "var d = window.frames['f'].document;\n"
            + "try { alert(d.queryCommandEnabled('SelectAll')); } catch(e) { alert('error'); }\n"
            + "try { alert(d.queryCommandEnabled('sElectaLL')); } catch(e) { alert('error'); }\n"
            + "d.designMode='on';\n"
            + "alert(d.queryCommandEnabled('SelectAll'));\n"
            + "alert(d.queryCommandEnabled('selectall'));\n"
            + "alert(d.queryCommandEnabled('SeLeCtALL'));}\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Minimal test for <tt>execCommand</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "true", "false" },
            IE = { "true", "command foo not supported" })
    public void execCommand() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    document.designMode = 'On';\n"
            + "    alert(document.execCommand('Bold', false, null));\n"
            + "    try {\n"
            + "      alert(document.execCommand('foo', false, null));\n"
            + "    }\n"
            + "    catch (e) {\n"
            + "      alert('command foo not supported');\n"
            + "    }\n"
            + "    document.designMode = 'Off';\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLHeadingElement]")
    @Browsers(Browser.FF)
    public void evaluate_caseInsensitiveAttribute() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var expr = './/*[@CLASS]';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    alert(result.iterateNext());\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <h1 class='title'>Some text</h1>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLHtmlElement]")
    @Browsers(Browser.FF)
    public void evaluate_caseInsensitiveTagName() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var expr = '/hTmL';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    alert(result.iterateNext());\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <h1 class='title'>Some text</h1>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "div1", "null", "2", "1" })
    @Browsers(Browser.FF)
    public void importNode_deep() throws Exception {
        importNode(true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "div1", "null", "0" })
    @Browsers(Browser.FF)
    public void importNode_notDeep() throws Exception {
        importNode(false);
    }

    private void importNode(final boolean deep) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
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

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that HtmlUnit behaves correctly when a document is missing the <tt>body</tt> tag (it
     * needs to be added once the document has finished loading).
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "1: null", "2: null", "3: [object HTMLBodyElement]" },
            IE = { "1: null", "2: [object]", "3: [object]" })
    public void noBodyTag() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <script>alert('1: ' + document.body);</script>\n"
            + "    <script defer=''>alert('2: ' + document.body);</script>\n"
            + "    <script>window.onload = function() { alert('3: ' + document.body); }</script>\n"
            + "  </head>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that HtmlUnit behaves correctly when an iframe's document is missing the <tt>body</tt> tag (it
     * needs to be added once the document has finished loading).
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "1: [object HTMLBodyElement]", "2: [object HTMLBodyElement]" },
            IE = { "1: null", "2: [object]" })
    public void noBodyTag_IFrame() throws Exception {
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

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that the document object has a <tt>fireEvent</tt> method and that it works correctly (IE only).
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = "x")
    public void fireEvent() throws Exception {
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
        final HtmlPage page = loadPage(getBrowserVersion(), html, actual);
        final HtmlSpan span = page.getHtmlElementById("s");
        span.click();
        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * Test the value of document.ownerDocument.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("null")
    public void ownerDocument() throws Exception {
        final String html = "<html><body id='hello' onload='doTest()'>\n"
                + "  <script>\n"
                + "    function doTest() {\n"
                + "      alert(document.ownerDocument);\n"
                + "    }\n"
                + "  </script>\n"
                + "</body>\n" + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ Browser.IE, Browser.FF3_6, Browser.FF10 })
    public void activeElement() throws Exception {
        final String html = "<html><head><script>\n"
            + "  alert(document.activeElement);"
            + "  function test() { \n"
            + "     alert(document.activeElement.id);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<input id='text1' onclick='test()'>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlTextInput text1 = page.getHtmlElementById("text1");
        text1.click();
        assertEquals(new String[]{"null", "text1"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ Browser.IE })
    public void setActive() throws Exception {
        final String html = "<html><head><script>\n"
            + "  alert(document.activeElement);"
            + "  function test() { \n"
            + "     alert(document.activeElement.id);\n"
            + "     document.getElementById('text2').setActive();\n"
            + "     alert(document.activeElement.id);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<input id='text1' onclick='test()'>\n"
            + "<input id='text2' onfocus='alert(\"onfocus text2\")'>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlTextInput text1 = page.getHtmlElementById("text1");
        text1.focus();
        text1.click();
        assertEquals(new String[]{"null", "text1", "onfocus text2", "text2"}, collectedAlerts);
    }

    /**
     * Test for bug 2024729 (we were missing the document.captureEvents(...) method).
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    public void captureEvents() throws Exception {
        final String content = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function t() { alert('captured'); }\n"
            + "document.captureEvents(Event.CLICK);\n"
            + "document.onclick = t;\n"
            + "</script></head><body>\n"
            + "<div id='theDiv' onclick='alert(123)'>foo</div>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        page.getHtmlElementById("theDiv").click();

        final String[] expectedAlerts = {"123", "captured"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "[object]", FF = "[object Comment]")
    public void createComment() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<title>Test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var elt = document.createComment('some comment');\n"
            + "  alert(elt);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "true", "books", "books", "1", "book", "0" },
            FF = { "true", "books", "books", "3", "#text", "0" })
    public void createAttribute() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    var cid = document.createAttribute('id');\n"
            + "    cid.nodeValue = 'a1';\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + URL_SECOND + "'));\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "    alert(doc.childNodes[0].nodeName);\n"
            + "    alert(doc.childNodes[0].childNodes.length);\n"
            + "    alert(doc.childNodes[0].childNodes[0].nodeName);\n"
            + "    alert(doc.getElementsByTagName('books').item(0).attributes.length);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = getWebClient();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "0", "1" })
    public void getElementsByTagNameNS() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    doc.load('" + URL_SECOND + "');\n"
            + "    if (!document.all) {\n"
            + "      alert(document.getElementsByTagNameNS('*', 'books').length);\n"
            + "      alert(doc.getElementsByTagNameNS('*', 'books').length);\n"
            + "    }\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n"
            + "  <books xmlns='http://www.example.com/ns1'>\n"
            + "    <book>\n"
            + "      <title>Immortality</title>\n"
            + "      <author>John Smith</author>\n"
            + "    </book>\n"
            + "  </books>\n"
            + "</soap:Envelope>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = getWebClient();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }
}
