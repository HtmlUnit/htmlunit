/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html.test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlResetInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  Tests for HtmlForm
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlFormTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlFormTest( final String name ) {
        super( name );
    }


    /**
     *  Test the good case for setCheckedRadioButton()
     *
     * @exception  Exception If the test fails
     */
    public void testSetSelectedRadioButton_ValueExists()
        throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<input type='radio' name='foo' value='1' selected='selected' id='input1'/>"
                 + "<input type='radio' name='foo' value='2' id='input2'/>"
                 + "<input type='radio' name='foo' value='3'/>"
                 + "<input type='submit' name='button' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
            new URL( "http://www.gargoylesoftware.com" ),
            SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSubmitInput pushButton = ( HtmlSubmitInput )form.getInputByName( "button" );

        form.setCheckedRadioButton( "foo", "2" );

        assertFalse( ((HtmlRadioButtonInput)page.getHtmlElementById("input1")).isChecked() );
        assertTrue( ((HtmlRadioButtonInput)page.getHtmlElementById("input2")).isChecked() );

        // Test that only one value for the radio button is being passed back to the server
        final HtmlPage secondPage = ( HtmlPage )pushButton.click();

        final List expectedParameters = new ArrayList();
        expectedParameters.add( new KeyValuePair( "foo", "2" ) );
        expectedParameters.add( new KeyValuePair( "button", "foo" ) );

        assertEquals( "url", new URL( "http://www.gargoylesoftware.com/" ),
            secondPage.getWebResponse().getUrl() );
        assertEquals( "method", SubmitMethod.GET, webConnection.getLastMethod() );
        assertEquals( "parameters", expectedParameters, webConnection.getLastParameters() );
        assertNotNull( secondPage );
    }


    /**
     *  Test setCheckedRadioButton() with a value that doesn't exist
     *
     * @exception  Exception If the test fails
     */
    public void testSetSelectedRadioButton_ValueDoesNotExist_DoNotForceSelection()
        throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<input type='radio' name='foo' value='1' selected='selected'/>"
                 + "<input type='radio' name='foo' value='2'/>"
                 + "<input type='radio' name='foo' value='3'/>"
                 + "<input type='submit' name='button' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
            new URL( "http://www.gargoylesoftware.com" ),
            SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlInput pushButton = ( HtmlInput )form.getInputByName( "button" );
		assertNotNull(pushButton);
		
        try {
            form.setCheckedRadioButton( "foo", "4" );
            fail( "Expected foo" );
        }
        catch( final ElementNotFoundException e ) {
            // Expected path
        }
    }


    /**
     *  Test setCheckedRadioButton() with a value that doesn't exist
     *
     * @exception  Exception If the test fails
     */
    public void testSetSelectedRadioButton_ValueDoesNotExist_ForceSelection()
        throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<input type='radio' name='foo' value='1' selected='selected'/>"
                 + "<input type='radio' name='foo' value='2'/>"
                 + "<input type='radio' name='foo' value='3'/>"
                 + "<input type='submit' name='button' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSubmitInput pushButton = ( HtmlSubmitInput )form.getInputByName( "button" );

        form.fakeCheckedRadioButton( "foo", "4" );

        // Test that only one value for the radio button is being passed back to the server
        final HtmlPage secondPage = ( HtmlPage )pushButton.click();

        final List expectedParameters = new ArrayList();
        expectedParameters.add( new KeyValuePair( "button", "foo" ) );
        expectedParameters.add( new KeyValuePair( "foo", "4" ) );

        assertEquals( "url", new URL( "http://www.gargoylesoftware.com/" ),
            secondPage.getWebResponse().getUrl() );
        assertEquals( "method", SubmitMethod.GET, webConnection.getLastMethod() );
        assertEquals( "parameters", expectedParameters, webConnection.getLastParameters() );
        assertNotNull( secondPage );
    }


    public void testSubmit_String()
        throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<input type='submit' name='button' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        // Regression test: this used to blow up
        form.submit( "button" );
    }


    public void testSubmit_ExtraParameters()
        throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "    <input type='text' name='textfield' value='*'/>"
                 + "    <input type='submit' name='button' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSubmitInput button = (HtmlSubmitInput)form.getInputByName("button");
        button.click();

        final List expectedParameters = Arrays.asList( new Object[]{
            new KeyValuePair("textfield", "*"), new KeyValuePair("button", "foo")
        } );
        final List collectedParameters = webConnection.getLastParameters();

        assertEquals( expectedParameters, collectedParameters );
    }


    public void testSubmit_onSubmitHandler()
        throws Exception {

        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<form method='get' action='http://second' onSubmit='alert(\"clicked\")'>"
                 + "<input name='button' type='submit' value='PushMe' id='button'/></form>"
                 + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"),secondContent,200,"OK","text/html",Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlSubmitInput button = (HtmlSubmitInput)firstPage.getHtmlElementById("button");

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
        final HtmlPage secondPage = (HtmlPage)button.click();
        assertEquals( "Second", secondPage.getTitleText() );

        assertEquals( Collections.singletonList("clicked"), collectedAlerts );
    }


    public void testSubmit_onSubmitHandler_returnFalse()
        throws Exception {

        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<form method='get' action='http://second' "
                 + "onSubmit='alert(\"clicked\");return false;'>"
                 + "<input name='button' type='submit' value='PushMe' id='button'/></form>"
                 + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"),secondContent,200,"OK","text/html",Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlSubmitInput button = (HtmlSubmitInput)firstPage.getHtmlElementById("button");

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
        final HtmlPage secondPage = (HtmlPage)button.click();
        assertEquals( firstPage.getTitleText(), secondPage.getTitleText() );

        assertEquals( Collections.singletonList("clicked"), collectedAlerts );
    }


    public void testSubmit_onSubmitHandler_javascriptDisabled() throws Exception {

        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<form method='get' action='http://second' onSubmit='alert(\"clicked\")'>"
                 + "<input name='button' type='submit' value='PushMe' id='button'/></form>"
                 + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        client.setJavaScriptEnabled(false);

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"),secondContent,200,"OK","text/html",Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlSubmitInput button = (HtmlSubmitInput)firstPage.getHtmlElementById("button");

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
        final HtmlPage secondPage = (HtmlPage)button.click();
        assertEquals( "First", firstPage.getTitleText() );
        assertEquals( "Second", secondPage.getTitleText() );

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
    }


    public void testSubmit_javascriptAction() throws Exception {

        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<form method='get' action='javascript:alert(\"clicked\")'>"
                 + "<input name='button' type='submit' value='PushMe' id='button'/></form>"
                 + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"),secondContent,200,"OK","text/html",Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlSubmitInput button = (HtmlSubmitInput)firstPage.getHtmlElementById("button");

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
        final HtmlPage secondPage = (HtmlPage)button.click();
        assertEquals( firstPage.getTitleText(), secondPage.getTitleText() );

        assertEquals( Collections.singletonList("clicked"), collectedAlerts );
    }


    public void testSubmit_javascriptAction_javascriptDisabled() throws Exception {

        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<form method='get' action='javascript:alert(\"clicked\")'>"
                 + "<input name='button' type='submit' value='PushMe' id='button'/></form>"
                 + "</body></html>";

        final WebClient client = new WebClient();
        client.setJavaScriptEnabled(false);

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlSubmitInput button = (HtmlSubmitInput)firstPage.getHtmlElementById("button");

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
        final HtmlPage secondPage = (HtmlPage)button.click();
        assertSame( firstPage, secondPage );
    }


    /**
     * Regression test for a bug that caused a NullPointer exception to be thrown during submit.
     */
    public void testSubmitRadioButton() throws Exception {
        final String firstContent
                 = "<html><body><form method='POST' action='http://first'>"
                 + "<table><tr> <td ><input type='radio' name='name1' value='foo'> "
                 + "Option 1</td> </tr>"
                 + "<tr> <td ><input type='radio' name='name1' value='bar' checked >"
                 + "Option 2</td> </tr>"
                 + "<tr> <td ><input type='radio' name='name1' value='baz'> Option 3</td> </tr>"
                 + "</table><input type='submit' value='Login' name='loginButton1'></form>"
                 + "</body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
            new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlSubmitInput loginButton
            = (HtmlSubmitInput)page.getOneHtmlElementByAttribute("input","value","Login");
        loginButton.click();
    }


    public void testReset_onResetHandler()
        throws Exception {

        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<form method='get' action='http://second' "
                 + "onReset='alert(\"clicked\");'>"
                 + "<input name='button' type='reset' value='PushMe' id='button'/></form>"
                 + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"),secondContent,200,"OK","text/html",Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlResetInput button = (HtmlResetInput)firstPage.getHtmlElementById("button");

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
        final HtmlPage secondPage = (HtmlPage)button.click();

        assertSame( firstPage, secondPage );
        assertEquals( Collections.singletonList("clicked"), collectedAlerts );
    }


    /**
     * Simulate a bug report where an anchor contained javascript that caused a form submit.
     * According to the bug report, the form would be submitted even though the onsubmit
     * handler would return false.  This wasn't reproducible but I added a test for it anyway.
     */
    public void testSubmit_AnchorCausesSubmit_onSubmitHandler_returnFalse()
        throws Exception {

        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<form name='form1' method='get' action='http://second' "
                 + "onSubmit='alert(\"clicked\");return false;'>"
                 + "<input name='button' type='submit' value='PushMe' id='button'/></form>"
                 + "<a id='link1' href='javascript:document.form1.submit()'>Click me</a>"
                 + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"),secondContent,200,"OK","text/html",Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlAnchor anchor = (HtmlAnchor)firstPage.getHtmlElementById("link1");

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
        final HtmlPage secondPage = (HtmlPage)anchor.click();
        assertSame( firstPage, secondPage );

        assertEquals( Collections.singletonList("clicked"), collectedAlerts );
    }


    public void testSubmit_NoDefaultValue()
        throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "    <input type='text' name='textfield'/>"
                 + "    <input type='submit' name='button' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSubmitInput button = (HtmlSubmitInput)form.getInputByName("button");
        button.click();

        final List expectedParameters = Arrays.asList( new Object[]{
            new KeyValuePair("textfield", ""), new KeyValuePair("button", "foo")
        } );
        final List collectedParameters = webConnection.getLastParameters();

        assertEquals( expectedParameters, collectedParameters );
    }
}

