/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpState;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for Document
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 * @author  Barnaby Court
 */
public class DocumentTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public DocumentTest( final String name ) {
        super(name);
    }


    /**
     * @throws Exception if the test fails
     */
    public void testFormsAccessor_TwoForms() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>\n"
                 + "function doTest(){\n"
                 + "    alert(document.forms.length)\n"
                 + "    for( var i=0; i< document.forms.length; i++) {\n"
                 + "        alert( document.forms[i].name )\n"
                 + "    }\n"
                 + "}\n"
                 + "</script></head><body onload='doTest()'>\n"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <input type='text' name='textfield1' value='foo' />"
                 + "</form>"
                 + "<form name='form2'>"
                 + "    <input type='text' name='textfield2' value='foo' />"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "2", "form1", "form2"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Previously, forms with no names were not being returned by document.forms.
     * @throws Exception if the test fails
     */
    public void testFormsAccessor_FormWithNoName() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>\n"
                 + "function doTest(){\n"
                 + "    alert(document.forms.length)\n"
                 + "}\n"
                 + "</script></head><body onload='doTest()'>\n"
                 + "<p>hello world</p>"
                 + "<form>"
                 + "    <input type='text' name='textfield1' value='foo' />"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Collections.singletonList("1");

         assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testFormsAccessor_NoForms() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>\n"
                 + "function doTest(){\n"
                 + "    alert(document.forms.length)\n"
                 + "    for( var i=0; i< document.forms.length; i++) {\n"
                 + "        alert( document.forms[i].name )\n"
                 + "    }\n"
                 +"}\n"
                 + "</script></head><body onload='doTest()'>\n"
                 + "<p>hello world</p>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "0"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testDocumentWrite_AssignedToVar() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>\n"
                 + "function doTheFoo() {\n"
                 + "var d=document.writeln\n"
                 + "d('foo')\n"
                 + "document.writeln('foo')\n"
                 + "}"
                 + "</script></head><body onload='doTheFoo()'>\n"
                 + "<p>hello world</p>"
                 + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
        } );

        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testFormArray() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String firstContent
                 = "<html><head><SCRIPT lang=\"JavaScript\">"
                 + "    function doSubmit(formName){"
                 + "        var form = document.forms[formName];" // This line used to blow up
                 + "        form.submit()"
                 + "}"
                 + "</SCRIPT></head><body><form name=\"formName\" method=\"POST\" "
                 + "action=\"http://second\">"
                 + "<a href=\".\" id=\"testJavascript\" name=\"testJavascript\" "
                 + "onclick=\" doSubmit('formName');return false;\">"
                 + "Test Link </a><input type=\"submit\" value=\"Login\" "
                 + "name=\"loginButton\"></form>"
                 + "</body></html> ";
        final String secondContent
                 = "<html><head><title>second</title></head><body>\n"
                 + "<p>hello world</p>"
                 + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            URL_SECOND, secondContent, 200, "OK", "text/html",
            Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage)client.getPage(URL_FIRST);
        assertEquals( "", page.getTitleText() );

        final HtmlAnchor testAnchor = page.getAnchorByName("testJavascript");
        final HtmlPage secondPage = (HtmlPage)testAnchor.click();
        assertEquals( "second", secondPage.getTitleText() );
     }


    /**
     * @throws Exception if the test fails
     */
    public void testDocumentLocationHref() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    alert(top.document.location.href);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("http://first");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for bug 742902
     * @throws Exception if the test fails
     */
    public void testDocumentLocation() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    alert(top.document.location);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("http://first");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for RFE 741930
     * @throws Exception if the test fails
     */
    public void testDocumentCreateElement() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var div1=document.createElement('div');\n"
             + "    var body1=document.getElementById('body');\n"
             + "    body1.appendChild(div1);\n"
             + "    alert(div1.tagName);\n"
             + "    alert(div1.nodeType);\n"
             + "    alert(div1.nodeValue);\n"
             + "    alert(div1.nodeName);\n"
             + "}\n"
             + "</script></head><body onload='doTest()' id='body'>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final HtmlElement div1 = (HtmlElement)
            firstPage.getHtmlElementById("body").getLastChild();
        assertEquals("div", div1.getTagName());
        assertEquals(1, div1.getNodeType());
        assertEquals(null, div1.getNodeValue());
        assertEquals("div", div1.getNodeName());

        final List expectedAlerts = Arrays.asList( new String[]{
            "DIV", "1", "null", "div"
        } );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for createTextNode
     * @throws Exception if the test fails
     */
    public void testDocumentCreateTextNode() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
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
             + "</script></head><body onload='doTest()' id='body'>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final DomNode div1 =
            firstPage.getHtmlElementById("body").getLastChild();
        assertEquals(3, div1.getNodeType());
        assertEquals("Some Text", div1.getNodeValue());
        assertEquals("#text", div1.getNodeName());

        final List expectedAlerts = Arrays.asList( new String[]{
            "Some Text", "9", "3", "Some Text", "#text"
        } );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for RFE 741930
     * @throws Exception if the test fails
     */
    public void testAppendChild() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webClient.setWebConnection( webConnection );

        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var form = document.forms['form1'];\n"
            + "    var div = document.createElement( 'DIV' );\n"
            + "    form.appendChild( div );\n"
            + "    var elements = document.getElementsByTagName('DIV');\n"
            + "    alert( elements.length )\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "</form>"
            + "</body></html>";
        webConnection.setResponse(
            URL_FIRST, content, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "1"
        } );

        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for appendChild of a text node
     * @throws Exception if the test fails
     */
    public void testAppendChild_textNode() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webClient.setWebConnection( webConnection );

        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var form = document.forms['form1'];\n"
            + "    var child = document.createTextNode( 'Some Text' );\n"
            + "    form.appendChild( child );\n"
            + "    alert( form.lastChild.data )\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "</form>"
            + "</body></html>";
        webConnection.setResponse(
            URL_FIRST, content, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "Some Text"
        } );

        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for cloneNode
     * @throws Exception if the test fails
     */
    public void testCloneNode() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webClient.setWebConnection( webConnection );

        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var form = document.forms['form1'];\n"
            + "    var cloneShallow = form.cloneNode(false);\n"
            + "    alert( cloneShallow!=null )\n"
            + "    alert( cloneShallow.firstChild==null )\n"
            + "    var cloneDeep = form.cloneNode(true);\n"
            + "    alert( cloneDeep!=null )\n"
            + "    alert( cloneDeep.firstChild!=null )\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>"
            + "<p>hello world</p>"
            + "</form>"
            + "</body></html>";
        webConnection.setResponse(
            URL_FIRST, content, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "true", "true", "true", "true"
        } );

        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for insertBefore
     * @throws Exception if the test fails
     */
    public void testInsertBefore() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webClient.setWebConnection( webConnection );

        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var form = document.forms['form1'];\n"
            + "    var oldChild = document.getElementById('oldChild');\n"
            + "    var div = document.createElement( 'DIV' );\n"
            + "    form.insertBefore( div, oldChild );\n"
            + "    alert( form.firstChild==div )\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'><div id='oldChild'/></form>"
            + "</body></html>";
        webConnection.setResponse(
            URL_FIRST, content, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "true"
        } );

        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for removeChild
     * @throws Exception if the test fails
     */
    public void testRemoveChild() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webClient.setWebConnection( webConnection );

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
            URL_FIRST, content, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "true", "true"
        } );

        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for replaceChild
     * @throws Exception if the test fails
     */
    public void testReplaceChild() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webClient.setWebConnection( webConnection );

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
            URL_FIRST, content, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "true", "true"
        } );

        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testGetElementById() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    alert(top.document.getElementById('input1').value);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<form id='form1'>"
             + "<input id='input1' name='foo' type='text' value='bar' />"
             + "</form>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("bar");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testGetElementById_resetId() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var input1=top.document.getElementById('input1');\n"
             + "    input1.id='newId';\n"
             + "    alert(top.document.getElementById('newId').value);\n"
             + "    alert(top.document.getElementById('input1'));\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<form id='form1'>"
             + "<input id='input1' name='foo' type='text' value='bar' />"
             + "</form>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Arrays.asList( new String[] {
            "bar", "null"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testGetElementById_setNewId() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var div1=top.document.getElementById('div1');\n"
             + "    div1.nextSibling.id='newId';\n"
             + "    alert(top.document.getElementById('newId').value);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<form id='form1'>"
             + "<div id='div1'/><input name='foo' type='text' value='bar' />"
             + "</form>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("bar");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for bug 740665
     * @throws Exception if the test fails
     */
    public void testGetElementById_divId() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var element = document.getElementById('id1');\n"
             + "    alert(element.id);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<div id='id1'></div></body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("id1");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for bug 740665
     * @throws Exception if the test fails
     */
    public void testGetElementById_scriptId() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script id='script1'>"
             + "function doTest() {\n"
             + "    alert(top.document.getElementById('script1').id);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("script1");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for bug 740665
     * @throws Exception if the test fails
     */
    public void testGetElementById_scriptType() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title>"
             + "<script id='script1' type='text/javascript'>\n"
             + "doTest=function () {\n"
             + "    alert(top.document.getElementById('script1').type);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("text/javascript");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for bug 740665
     * @throws Exception if the test fails
     */
    public void testGetElementById_scriptSrc() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webClient.setWebConnection( webConnection );

        final String firstContent
             = "<html><head><title>First</title>"
             + "<script id='script1' src='http://script'>\n"
             + "</script></head><body onload='doTest()'>"
             + "</body></html>";
        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final String scriptContent
             = "doTest=function () {\n"
             + "    alert(top.document.getElementById('script1').src);\n"
             + "}\n";
        webConnection.setResponse(
            new URL("http://script"), scriptContent, 200, "OK", "text/javascript", Collections.EMPTY_LIST );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("http://script");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for parentNode with nested elements
     * @throws Exception if the test fails
     */
    public void testParentNode_Nested() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var div1=document.getElementById('childDiv');\n"
             + "    alert(div1.parentNode.id);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<div id='parentDiv'><div id='childDiv'></div></div>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final HtmlElement div1 = firstPage.getHtmlElementById("childDiv");
        assertEquals("parentDiv",
            ((HtmlElement) div1.getParentNode()).getAttributeValue("id"));

        final List expectedAlerts = Collections.singletonList("parentDiv");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for parentNode of document
     * @throws Exception if the test fails
     */
    public void testParentNode_Document() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    alert(document.parentNode==null);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("true");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for parentNode and createElement
     * @throws Exception if the test fails
     */
    public void testParentNode_CreateElement() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var div1=document.createElement('div');\n"
             + "    alert(div1.parentNode==null);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("true");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for parentNode and appendChild
     * @throws Exception if the test fails
     */
    public void testParentNode_AppendChild() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var childDiv=document.getElementById('childDiv');\n"
             + "    var parentDiv=document.getElementById('parentDiv');\n"
             + "    parentDiv.appendChild(childDiv);\n"
             + "    alert(childDiv.parentNode.id);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<div id='parentDiv'></div><div id='childDiv'></div>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final HtmlElement childDiv = firstPage.getHtmlElementById("childDiv");
        assertEquals("parentDiv",
            ((HtmlElement) childDiv.getParentNode()).getAttributeValue("id"));

        final List expectedAlerts = Collections.singletonList("parentDiv");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for documentElement of document
     * @throws Exception if the test fails
     */
    public void testDocumentElement() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    alert(document.documentElement!=null);\n"
             + "    alert(document.documentElement.tagName);\n"
             + "    alert(document.documentElement.parentNode==document);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Arrays.asList( new String[] {
            "true", "HTML", "true"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for firstChild with nested elements
     * @throws Exception if the test fails
     */
    public void testFirstChild_Nested() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var div1=document.getElementById('parentDiv');\n"
             + "    alert(div1.firstChild.id);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<div id='parentDiv'><div id='childDiv'/><div id='childDiv2'/></div>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final HtmlElement div1 = firstPage.getHtmlElementById("parentDiv");
        assertEquals("childDiv",
            ((HtmlElement) div1.getFirstChild()).getAttributeValue("id"));

        final List expectedAlerts = Collections.singletonList("childDiv");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for firstChild and appendChild
     * @throws Exception if the test fails
     */
    public void testFirstChild_AppendChild() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var childDiv=document.getElementById('childDiv');\n"
             + "    var parentDiv=document.getElementById('parentDiv');\n"
             + "    parentDiv.appendChild(childDiv);\n"
             + "    var childDiv2=document.getElementById('childDiv2');\n"
             + "    parentDiv.appendChild(childDiv2);\n"
             + "    alert(parentDiv.firstChild.id);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<div id='parentDiv'/><div id='childDiv'/><div id='childDiv2'/>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final HtmlElement parentDiv = firstPage.getHtmlElementById("parentDiv");
        assertEquals("childDiv",
            ((HtmlElement) parentDiv.getFirstChild()).getAttributeValue("id"));

        final List expectedAlerts = Collections.singletonList("childDiv");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for lastChild with nested elements
     * @throws Exception if the test fails
     */
    public void testLastChild_Nested() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String lastContent
             = "<html><head><title>Last</title><script>"
             + "function doTest() {\n"
             + "    var div1=document.getElementById('parentDiv');\n"
             + "    alert(div1.lastChild.id);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<div id='parentDiv'><div id='childDiv1'/><div id='childDiv'/></div>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://last"), lastContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage lastPage = ( HtmlPage )webClient.getPage( new URL( "http://last" ) );
        assertEquals( "Last", lastPage.getTitleText() );

        final HtmlElement parentDiv = lastPage.getHtmlElementById("parentDiv");
        assertEquals("childDiv",
            ((HtmlElement) parentDiv.getLastChild()).getAttributeValue("id"));

        final List expectedAlerts = Collections.singletonList("childDiv");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for lastChild and appendChild
     * @throws Exception if the test fails
     */
    public void testLastChild_AppendChild() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String lastContent
             = "<html><head><title>Last</title><script>"
             + "function doTest() {\n"
             + "    var childDiv1=document.getElementById('childDiv1');\n"
             + "    var parentDiv=document.getElementById('parentDiv');\n"
             + "    parentDiv.appendChild(childDiv1);\n"
             + "    var childDiv=document.getElementById('childDiv');\n"
             + "    parentDiv.appendChild(childDiv);\n"
             + "    alert(parentDiv.lastChild.id);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<div id='parentDiv'/><div id='childDiv1'/><div id='childDiv'/>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://last"), lastContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage lastPage = ( HtmlPage )webClient.getPage( new URL( "http://last" ) );
        assertEquals( "Last", lastPage.getTitleText() );

        final HtmlElement parentDiv = lastPage.getHtmlElementById("parentDiv");
        assertEquals("childDiv",
            ((HtmlElement) parentDiv.getLastChild()).getAttributeValue("id"));

        final List expectedAlerts = Collections.singletonList("childDiv");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for nextSibling with nested elements
     * @throws Exception if the test fails
     */
    public void testNextSibling_Nested() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String lastContent
             = "<html><head><title>Last</title><script>"
             + "function doTest() {\n"
             + "    var div1=document.getElementById('previousDiv');\n"
             + "    alert(div1.nextSibling.id);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<div id='parentDiv'><div id='previousDiv'/><div id='nextDiv'/></div>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://last"), lastContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage lastPage = ( HtmlPage )webClient.getPage( new URL( "http://last" ) );
        assertEquals( "Last", lastPage.getTitleText() );

        final HtmlElement div1 = lastPage.getHtmlElementById("previousDiv");
        assertEquals("nextDiv",
            ((HtmlElement) div1.getNextSibling()).getAttributeValue("id"));

        final List expectedAlerts = Collections.singletonList("nextDiv");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for nextSibling and appendChild
     * @throws Exception if the test fails
     */
    public void testNextSibling_AppendChild() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String lastContent
             = "<html><head><title>Last</title><script>"
             + "function doTest() {\n"
             + "    var previousDiv=document.getElementById('previousDiv');\n"
             + "    var parentDiv=document.getElementById('parentDiv');\n"
             + "    parentDiv.appendChild(previousDiv);\n"
             + "    var nextDiv=document.getElementById('nextDiv');\n"
             + "    parentDiv.appendChild(nextDiv);\n"
             + "    alert(previousDiv.nextSibling.id);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<div id='parentDiv'/><div id='junk1'/><div id='previousDiv'/><div id='junk2'/><div id='nextDiv'/>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://last"), lastContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage lastPage = ( HtmlPage )webClient.getPage( new URL( "http://last" ) );
        assertEquals( "Last", lastPage.getTitleText() );

        final HtmlElement previousDiv = lastPage.getHtmlElementById("previousDiv");
        assertEquals("nextDiv",
            ((HtmlElement) previousDiv.getNextSibling()).getAttributeValue("id"));

        final List expectedAlerts = Collections.singletonList("nextDiv");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for previousSibling with nested elements
     * @throws Exception if the test fails
     */
    public void testPreviousSibling_Nested() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String lastContent
             = "<html><head><title>Last</title><script>"
             + "function doTest() {\n"
             + "    var div1=document.getElementById('nextDiv');\n"
             + "    alert(div1.previousSibling.id);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<div id='parentDiv'><div id='previousDiv'/><div id='nextDiv'/></div>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://last"), lastContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage lastPage = ( HtmlPage )webClient.getPage( new URL( "http://last" ) );
        assertEquals( "Last", lastPage.getTitleText() );

        final HtmlElement div1 = lastPage.getHtmlElementById("nextDiv");
        assertEquals("previousDiv",
            ((HtmlElement) div1.getPreviousSibling()).getAttributeValue("id"));

        final List expectedAlerts = Collections.singletonList("previousDiv");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for previousSibling and appendChild
     * @throws Exception if the test fails
     */
    public void testPreviousSibling_AppendChild() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String lastContent
             = "<html><head><title>Last</title><script>"
             + "function doTest() {\n"
             + "    var previousDiv=document.getElementById('previousDiv');\n"
             + "    var parentDiv=document.getElementById('parentDiv');\n"
             + "    parentDiv.appendChild(previousDiv);\n"
             + "    var nextDiv=document.getElementById('nextDiv');\n"
             + "    parentDiv.appendChild(nextDiv);\n"
             + "    alert(nextDiv.previousSibling.id);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<div id='parentDiv'/><div id='junk1'/><div id='previousDiv'/><div id='junk2'/><div id='nextDiv'/>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://last"), lastContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage lastPage = ( HtmlPage )webClient.getPage( new URL( "http://last" ) );
        assertEquals( "Last", lastPage.getTitleText() );

        final HtmlElement nextDiv = lastPage.getHtmlElementById("nextDiv");
        assertEquals("previousDiv",
            ((HtmlElement) nextDiv.getPreviousSibling()).getAttributeValue("id"));

        final List expectedAlerts = Collections.singletonList("previousDiv");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testAllProperty_KeyByName() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    alert(document.all['input1'].value);\n"
             + "    alert(document.all['foo2'].value);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'><form id='form1'>"
             + "    <input id='input1' name='foo1' type='text' value='tangerine' />"
             + "    <input id='input2' name='foo2' type='text' value='ginger' />"
             + "</form>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Arrays.asList( new String[]{
            "tangerine", "ginger"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for bug 707750
     * @throws Exception if the test fails
     */
    public void testAllProperty_CalledDuringPageLoad() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><body>"
             + "<div id='ARSMenuDiv1' style='VISIBILITY: hidden; POSITION: absolute; z-index: 1000000'></div>"
             + "<script language='Javascript'>"
             + "    var divObj = document.all['ARSMenuDiv1'];"
             + "    alert(divObj.tagName);"
             + "</script></body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals("", firstPage.getTitleText());

        final List expectedAlerts = Collections.singletonList("DIV");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testDocumentWrite() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title></head><body>"
             + "<script>document.write(\"<div id='div1'></div>\")</script>"
             + "</form></body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        // This will blow up if the div tag hasn't been written to the document
        firstPage.getHtmlElementById("div1");
    }


    /**
     * Regression test for bug 743241
     * @throws Exception if the test fails
     */
    public void testDocumentWrite_LoadScript() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webClient.setWebConnection( webConnection );

        final String firstContent
             = "<html><head><title>First</title></head><body>"
             + "<script src='http://script'></script>"
             + "</form></body></html>";
        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final String scriptContent
             = "document.write(\"<div id='div1'></div>\");";
        webConnection.setResponse(
            new URL("http://script"), scriptContent, 200, "OK", "text/javascript", Collections.EMPTY_LIST );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        // This will blow up if the div tag hasn't been written to the document
        firstPage.getHtmlElementById("div1");
    }


    /**
     * Regression test for bug 715379
     * @throws Exception if the test fails
     */
    public void testDocumentWrite_script() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webClient.setWebConnection( webConnection );

        final String mainContent
             = "<html><head><title>Main</title></head><body>"
             + "<iframe name=\"iframe\" id=\"iframe\" src=\"http://first\"></iframe>"
             + "<script type=\"text/javascript\">"
             + "document.write('<script type=\"text\\/javascript\" src=\"http://script\"><\\/script>');"
             + "</script></body></html> ";
        webConnection.setResponse(
            new URL("http://main"), mainContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final String firstContent
             = "<html><body><h1 id=\"first\">First</h1></body></html>";
        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final String secondContent
             = "<html><body><h1 id=\"second\">Second</h1></body></html>";
        webConnection.setResponse(
            URL_SECOND, secondContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final String scriptContent
             = "document.getElementById (\"iframe\").src = \"http://second\";";
        webConnection.setResponse(
            new URL("http://script"), scriptContent, 200, "OK", "text/javascript", Collections.EMPTY_LIST );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage mainPage = ( HtmlPage )webClient.getPage( new URL( "http://main" ) );
        assertEquals( "Main", mainPage.getTitleText() );

        final HtmlInlineFrame iFrame =
          (HtmlInlineFrame) mainPage.getHtmlElementById( "iframe" );

        assertEquals( "http://second", iFrame.getSrcAttribute() );

        final HtmlPage enclosedPage = (HtmlPage) iFrame.getEnclosedPage();
        // This will blow up if the script hasn't been written to the document
        // and executed so the second page has been loaded.
        enclosedPage.getHtmlElementById( "second" );
    }
    
    /**
     * @throws Exception if the test fails
     */
    public void testDocumentWrite_InDOM() throws Exception {
        if (true) {
            notImplemented();
            return;
        }        
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webClient.setWebConnection( webConnection );

        final String mainContent
        = "<html><head><title>First</title></head><body>"
            + "<script type=\"text/javascript\">"
            + "document.write('<a id=\"blah\">Hello World</a>');" 
            + "alert(document.getElementById('blah'));"
            + "</script>" 
            + "</body></html>";
        
        webConnection.setResponse(
                URL_FIRST, mainContent, 200, "OK", "text/html", Collections.EMPTY_LIST );


        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals(1, collectedAlerts.size());
        assertFalse("Should not be 'null'", "null".equals(collectedAlerts.get(0)));
    }    


    /**
     * @throws Exception if the test fails
     */
    public void testGetReferrer() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title></head><body onload='alert(document.referrer);'>"
             + "</form></body></html>";

        final List responseHeaders = Collections.singletonList( new KeyValuePair("referrer", "http://ref") );
        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", responseHeaders );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.singletonList("http://ref"), collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testGetReferrer_NoneSpecified() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title></head><body onload='alert(document.referrer);'>"
             + "</form></body></html>";

        final List responseHeaders = Collections.EMPTY_LIST;
        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", responseHeaders );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.singletonList(""), collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testGetURL() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title></head><body onload='alert(document.URL);'>"
             + "</form></body></html>";

        final List responseHeaders = Collections.EMPTY_LIST;
        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", responseHeaders );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.singletonList("http://first"), collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testGetElementsByTagName() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var elements = document.getElementsByTagName('input');\n"
             + "    for( i=0; i<elements.length; i++ ) {\n"
             + "        alert(elements[i].type);\n"
             + "    }\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<form><input type='button' name='button1' value='pushme'></form>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("button");
        assertEquals(expectedAlerts, collectedAlerts);
    }


    /**
     * Regression test for bug 740636
     * @throws Exception if the test fails
     */
    public void testGetElementsByTagName_CaseInsensitive() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var elements = document.getElementsByTagName('InPuT');\n"
             + "    for( i=0; i<elements.length; i++ ) {\n"
             + "        alert(elements[i].type);\n"
             + "    }\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<form><input type='button' name='button1' value='pushme'></form>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("button");
        assertEquals(expectedAlerts, collectedAlerts);
    }


    /**
     * Regression test for bug 740605
     * @throws Exception if the test fails
     */
    public void testGetElementsByTagName_Inline() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webClient.setWebConnection( webConnection );

        final String firstContent
            = "<html><body><script type=\"text/javascript\">"
            + "alert(document.getElementsByTagName('script').length);"
            + "</script></body></html>";
        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        webClient.getPage( URL_FIRST );

        final List expectedAlerts = Collections.singletonList("1");
        assertEquals(expectedAlerts, collectedAlerts);
    }


    /**
     * Regression test for bug 740605
     * @throws Exception if the test fails
     */
    public void testGetElementsByTagName_LoadScript() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );
        webClient.setWebConnection( webConnection );

        final String firstContent
            = "<html><body><script src=\"http://script\"></script></body></html>";
        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        final String scriptContent
            = "alert(document.getElementsByTagName('script').length);";
        webConnection.setResponse(
            new URL("http://script"), scriptContent, 200, "OK", "text/javascript", Collections.EMPTY_LIST );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        webClient.getPage( URL_FIRST );

        final List expectedAlerts = Collections.singletonList("1");
        assertEquals(expectedAlerts, collectedAlerts);
    }


    /**
     * @throws Exception if the test fails
     */
    public void testDocumentAll_IndexByInt() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var length = document.all.length;\n"
             + "    for( i=0; i< length; i++ ) {\n"
             + "        alert(document.all[i].tagName);\n"
             + "    }\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Arrays.asList( new String[] {
            "HTML", "HEAD", "TITLE", "SCRIPT", "BODY"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testDocumentAll_tags() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    var inputs = document.all.tags('input');\n"
             + "    var inputCount = inputs.length;\n"
             + "    for( i=0; i< inputCount; i++ ) {\n"
             + "        alert(inputs[i].name);\n"
             + "    }\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<input type='text' name='a' value='1'>"
             + "<input type='text' name='b' value='1'>"
             + "</body></html>";

        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Arrays.asList( new String[] {
            "a", "b"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testCookie_read() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
            = "<html><head><title>First</title><script>"
            + "function doTest() {\n"
            + "    var cookieSet = document.cookie.split(';');\n"
            + "    var setSize = cookieSet.length;\n"
            + "    var crumbs;\n"
            + "    var x=0;\n"
            + "    for (x=0;((x<setSize)); x++) {\n"
            + "        crumbs = cookieSet[x].split('=');\n"
            + "        alert ( crumbs[0] );\n"
            + "        alert ( crumbs[1] );\n"
            + "    } \n"
            + "}\n"
            + "</script></head><body onload='doTest()'>"
            + "</body></html>";

        final URL url = URL_FIRST;
        webConnection.setResponse(
            url, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final HttpState state = webConnection.getStateForUrl(url);
        state.addCookie( new Cookie("first", "one", "two") );
        state.addCookie( new Cookie("first", "three", "four") );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Arrays.asList( new String[] {
            "one", "two", "three", "four"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testStatus() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( webClient );

        final String firstContent
            = "<html><head><title>First</title><script>"
            + "function doTest() {\n"
            + "    alert(document.status);\n"
            + "    document.status = 'newStatus';\n"
            + "    alert(document.status);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>"
            + "</body></html>";

        final URL url = URL_FIRST;
        webConnection.setResponse(
            url, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final List collectedStatus = new ArrayList();
        webClient.setStatusHandler( new StatusHandler() {
            public void statusMessageChanged( final Page page, final String message ) {
                collectedStatus.add(message);
            }
        });
        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( URL_FIRST );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Arrays.asList( new String[] {
            "", "newStatus"} );
        assertEquals( "alerts", expectedAlerts, collectedAlerts );

        final List expectedStatus = Arrays.asList( new String[] {
            "newStatus"} );
        assertEquals( "status", expectedStatus, collectedStatus );
    }
}
