/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host.test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class DocumentTest extends WebTestCase {
    public DocumentTest( final String name ) {
        super(name);
    }


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


    // Regression test
    public void testFormArray() throws Exception {
        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );

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
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"), secondContent, 200, "OK", "text/html",
            Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage)client.getPage(new URL("http://first"));
        assertEquals( "", page.getTitleText() );

        final HtmlAnchor testAnchor = (HtmlAnchor)page.getAnchorByName("testJavascript");
        final HtmlPage secondPage = (HtmlPage)testAnchor.click();
        assertEquals( "second", secondPage.getTitleText() );
     }


    public void testDocumentLocation() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    alert(top.document.location.href);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( new URL( "http://first" ) );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("http://first");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testGetElementById() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title><script>"
             + "function doTest() {\n"
             + "    alert(top.document.getElementById('input1').value);\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<form id='form1'><input id='input1' name='foo' type='text' value='bar' /></form>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( new URL( "http://first" ) );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("bar");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testAllProperty_KeyByName() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

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
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( new URL( "http://first" ) );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Arrays.asList( new String[]{
            "tangerine", "ginger"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for bug 707750
     */
    public void testAllProperty_CalledDuringPageLoad() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

        final String firstContent
             = "<html><body>"
             + "<div id='ARSMenuDiv1' style='VISIBILITY: hidden; POSITION: absolute; z-index: 1000000'></div>"
             + "<script language='Javascript'>"
             + "    var divObj = document.all['ARSMenuDiv1'];"
             + "    alert(divObj.tagName);"
             + "</script></body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( new URL( "http://first" ) );

        final List expectedAlerts = Collections.singletonList("DIV");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testDocumentWrite() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title></head><body>"
             + "<script>document.write(\"<div id='div1'></div>\")</script>"
             + "</form></body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( new URL( "http://first" ) );
        assertEquals( "First", firstPage.getTitleText() );

        // This will blow up if the div tag hasn't been written to the document
        firstPage.getHtmlElementById("div1");
    }


    public void testGetReferrer() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title></head><body onload='alert(document.referrer);'>"
             + "</form></body></html>";

        final List responseHeaders = Collections.singletonList( new KeyValuePair("referrer", "http://ref") );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", responseHeaders );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( new URL( "http://first" ) );
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.singletonList("http://ref"), collectedAlerts );
    }


    public void testGetReferrer_NoneSpecified() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title></head><body onload='alert(document.referrer);'>"
             + "</form></body></html>";

        final List responseHeaders = Collections.EMPTY_LIST;
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", responseHeaders );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( new URL( "http://first" ) );
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.singletonList(""), collectedAlerts );
    }


    public void testGetURL() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

        final String firstContent
             = "<html><head><title>First</title></head><body onload='alert(document.URL);'>"
             + "</form></body></html>";

        final List responseHeaders = Collections.EMPTY_LIST;
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", responseHeaders );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( new URL( "http://first" ) );
        assertEquals( "First", firstPage.getTitleText() );

        assertEquals( Collections.singletonList("http://first"), collectedAlerts );
    }


    public void testGetElementsByTagName() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

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
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( new URL( "http://first" ) );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Collections.singletonList("button");
        assertEquals(expectedAlerts, collectedAlerts);
    }


    public void testDocumentAll_IndexByInt() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

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
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( new URL( "http://first" ) );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Arrays.asList( new String[] {
            "HTML", "HEAD", "TITLE", "SCRIPT", "BODY"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testDocumentAll_tags() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );

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
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage firstPage = ( HtmlPage )webClient.getPage( new URL( "http://first" ) );
        assertEquals( "First", firstPage.getTitleText() );

        final List expectedAlerts = Arrays.asList( new String[] {
            "a", "b"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }
}
