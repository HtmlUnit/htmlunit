/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host.test;

import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
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
public class FormTest extends WebTestCase {
    public FormTest( final String name ) {
        super(name);
    }


    public void testElementsAccessor_TwoElements() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>\n"
                 + "alert(document.form1.length)\n"
                 + "for( var i=0; i< document.form1.length; i++) {\n"
                 + "    var element = document.form1.elements[i];"
                 + "    if( element != document.form1[element.name] ) {\n"
                 + "        alert('name index not working for '+element.name);\n"
                 + "    }\n"
                 + "    alert( element.name )\n"
                 + "}\n"
                 + "</script></head><body>\n"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <input type='button' name='button1' />"
                 + "    <button type='button' name='button2' />"
                 + "    <input type='checkbox' name='checkbox1' />"
                 + "    <input type='file' name='fileupload1' />"
                 + "    <input type='hidden' name='hidden1' />"
                 + "    <select name='select1'>"
                 + "        <option>foo</option>"
                 + "    </select>"
                 + "    <select multiple='multiple' name='select2'>"
                 + "        <option>foo</option>"
                 + "    </select>"
                 + "    <input type='password' name='password1' />"
                 + "    <input type='reset' name='reset1' />"
                 + "    <button type='reset' name='reset2' />"
                 + "    <input type='submit' name='submit1' />"
                 + "    <button type='submit' name='submit2' />"
                 + "    <input type='text' name='textInput1' />"
                 + "    <textarea name='textarea1'>foo</textarea>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);

         final List expectedAlerts = Arrays.asList( new String[]{
             "14", "button1", "button2", "checkbox1", "fileupload1", "hidden1",
             "select1", "select2", "password1", "reset1",
             "reset2", "submit1", "submit2", "textInput1", "textarea1"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testRadioButtonArray() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>\n"
                 + "var radioArray = document.form1['radio1'];"
                 + "alert(radioArray.length)\n"
                 + "for( var i=0; i< radioArray.length; i++) {\n"
                 + "    var element = radioArray[i];"
                 + "    alert( element.value )\n"
                 + "}\n"
                 + "</script></head><body>\n"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <input type='radio' name='radio1' value='1'/>"
                 + "    <input type='radio' name='radio1' value='2'/>"
                 + "    <input type='radio' name='radio1' value='3'/>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);

         final List expectedAlerts = Arrays.asList( new String[]{
             "3", "1", "2", "3"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testActionProperty() throws Exception {
        final String jsProperty = "action";
        final String htmlProperty = "action";
        final String oldValue = "http://foo.com";
        final String newValue = "mailto:me@bar.com";

        final HtmlForm form = doTestProperty( jsProperty, htmlProperty, oldValue, newValue );
        assertEquals( newValue, form.getActionAttribute() );
    }


    public void testEncodingProperty() throws Exception {
        final String jsProperty = "encoding";
        final String htmlProperty = "enctype";
        final String oldValue = "myEncoding";
        final String newValue = "newEncoding";

        final HtmlForm form = doTestProperty( jsProperty, htmlProperty, oldValue, newValue );
        assertEquals( newValue, form.getEnctypeAttribute() );
    }


    public void testMethodProperty() throws Exception {
        final String jsProperty = "method";
        final String htmlProperty = "method";
        final String oldValue = "get";
        final String newValue = "post";

        final HtmlForm form = doTestProperty( jsProperty, htmlProperty, oldValue, newValue );
        assertEquals( newValue, form.getMethodAttribute() );
    }


    public void testTargetProperty() throws Exception {
        final String jsProperty = "target";
        final String htmlProperty = "target";
        final String oldValue = "_top";
        final String newValue = "_parent";

        final HtmlForm form = doTestProperty( jsProperty, htmlProperty, oldValue, newValue );
        assertEquals( newValue, form.getTargetAttribute() );
    }


    private HtmlForm doTestProperty(
            final String jsProperty,
            final String htmlProperty,
            final String oldValue,
            final String newValue )
        throws
            Exception {

        final String content
                 = "<html><head><title>foo</title><script>\n"
                 + "alert(document.form1."+jsProperty+");\n"
                 + "document.form1."+jsProperty+"='"+newValue+"'\n"
                 + "alert(document.form1."+jsProperty+");\n"
                 + "</script></head><body>\n"
                 + "<p>hello world</p>"
                 + "<form name='form1' "+htmlProperty+"='"+oldValue+"'>"
                 + "    <input type='button' name='button1' />"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);

         final List expectedAlerts = Arrays.asList( new String[]{
             oldValue, newValue
         } );

         assertEquals( expectedAlerts, collectedAlerts );
         return page.getFormByName("form1");
    }


    public void testFormSubmit() throws Exception {
        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );

        final String firstContent
                 = "<html><head><title>first</title></head><body>\n"
                 + "<p>hello world</p>"
                 + "<form name='form1' method='get' action='http://second'>"
                 + "    <input type='button' name='button1' />"
                 + "</form>"
                 + "</body></html>";
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
        assertEquals( "first", page.getTitleText() );

        final HtmlPage secondPage
            = (HtmlPage)page.executeJavaScriptIfPossible("document.form1.submit()", "test", true).getNewPage();
        assertEquals( "second", secondPage.getTitleText() );
    }


    public void testInputNamedId() throws Exception {
        doTestInputWithName("id");
    }


    public void testInputNamedAction() throws Exception {
        doTestInputWithName("action");
    }


    private void doTestInputWithName( final String name ) throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function go() {\n"
                 + "   alert(document.simple_form."+name+".value);\n"
                 + "   document.simple_form."+name+".value='foo';\n"
                 + "   alert(document.simple_form."+name+".value);\n"
                 + "}</script></head>"
                 + "<body onload='go()'>"
                 + "<p>hello world</p>"
                 + "<form action='login.jsp' name='simple_form'>"
                 + "    <input name='"+name+"' type='hidden' value='"+name+"2'>"
                 + "</form>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final List expectedAlerts = Arrays.asList( new String[]{
            name+"2", "foo"
        } );
        assertEquals( expectedAlerts, collectedAlerts );
    }
}
