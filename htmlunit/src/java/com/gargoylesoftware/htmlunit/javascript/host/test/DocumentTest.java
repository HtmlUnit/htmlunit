/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host.test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
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
                 + "alert(document.forms.length)\n"
                 + "for( var i=0; i< document.forms.length; i++) {\n"
                 + "    alert( document.forms[i].name )\n"
                 + "}\n"
                 + "</script></head><body>\n"
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

         final List expectedAlerts = Arrays.asList( new String[]{
             "2", "form1", "form2"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testFormsAccessor_NoForms() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>\n"
                 + "alert(document.forms.length)\n"
                 + "for( var i=0; i< document.forms.length; i++) {\n"
                 + "    alert( document.forms[i].name )\n"
                 + "}\n"
                 + "</script></head><body>\n"
                 + "<p>hello world</p>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);

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
}
