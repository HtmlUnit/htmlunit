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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FakeWebConnection;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 *  Tests for HtmlForm
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Jun Chen</a>
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

        final HtmlInput pushButton = form.getInputByName( "button" );
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


    /**
     * @throws Exception if the test fails
     */
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


    /**
     * @throws Exception if the test fails
     */
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


    /**
     * @throws Exception if the test fails
     */
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


    /**
     * @throws Exception if the test fails
     */
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


    /**
     * @throws Exception if the test fails
     */
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


    /**
     * @throws Exception if the test fails
     */
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


    /**
     * @throws Exception if the test fails
     */
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
     * @throws Exception if the test fails
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
            = (HtmlSubmitInput)page.getDocumentElement().getOneHtmlElementByAttribute("input","value","Login");
        loginButton.click();
    }


    /**
     * @throws Exception if the test fails
     */
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
     * @throws Exception if the test fails
     */
    public void testSubmit_AnchorCausesSubmit_onSubmitHandler_returnFalse()
        throws Exception {

        final String firstContent
                 = "<html><head><title>First</title></head>"
                 + "<script>function doalert(message){alert(message);}</script>"
                 + "<body><form name='form1' method='get' action='http://second' "
                 + "onSubmit='doalert(\"clicked\");return false;'>"
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


    /**
     * @throws Exception if the test fails
     */
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


    /**
     * @throws Exception if the test fails
     */
    public void testGetInputByName_WithinNoScriptTags() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "    <input type='text' name='textfield' value='*'/>"
                 + "    <noscript>"
                 + "    <input type='submit' name='button' value='foo'/>"
                 + "    </noscript>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        // Was failing at this point
        form.getInputByName("button");
    }


    /**
     * @throws Exception if the test fails
     */
    public void testForSubmit_TwoInputsWithSameName() throws Exception {
        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<form id='form1' name='form1' action='http://second'>"
                 + "    <input type='hidden' name='foo' value='bar'/>"
                 + "    <input type='submit' name='foo' value='bar'/>"
                 + "</form></body></html>";
        final String secondContent
                 = "<html><head><title>Second</title></head><body'></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"),secondContent,200,"OK","text/html",Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(
                new URL( "http://first" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )firstPage.getHtmlElementById( "form1" );

        final HtmlPage secondPage = (HtmlPage) form.submit("foo");
        assertEquals( "Second", secondPage.getTitleText() );
    }

   /**
    * @throws Exception if the test fails
    */
   public void testSubmit_NoNameOnControl()
       throws Exception {
       final String htmlContent
                = "<html><head><title>foo</title></head><body>"
                + "<form id='form1'>"
                + "    <input type='text' id='textfield' value='blah'/>"
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
           new KeyValuePair("button", "foo")
       } );
       final List collectedParameters = webConnection.getLastParameters();

       assertEquals( expectedParameters, collectedParameters );
   }

    /**
     * @throws Exception if the test fails
     */
    public void testSubmit_NestedInput()
        throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "    <table><tr><td>"
                 + "        <input type='text' name='textfield' value='blah'/>"
                 + "        </td><td>"
                 + "        <input type='submit' name='button' value='foo'/>"
                 + "        </td></tr>"
                 + "     </table>"
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
            new KeyValuePair("textfield", "blah"),
            new KeyValuePair("button", "foo")
        } );
        final List collectedParameters = webConnection.getLastParameters();

        assertEquals( expectedParameters, collectedParameters );
    }

   /**
    * @throws Exception if the test fails
    */
    public void testSubmit_IgnoresDisabledControls()
        throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + "    <input type='text' name='textfield' value='blah' disabled />"
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
            new KeyValuePair("button", "foo")
        } );
        final List collectedParameters = webConnection.getLastParameters();

        assertEquals( expectedParameters, collectedParameters );
    }

    /**
     * @throws Exception if the test fails
     */
     public void testSubmit_CheckboxClicked() throws Exception {
         final String htmlContent
             = "<html><head><title>foo</title>"
             + "<script language='javascript'>"
             + "function setFormat()"
             + "{"
             + "    if(document.form1.Format.checked) {"
             + "        document.form1.Format.value='html';"
             + "    } else {"
             + "        document.form1.Format.value='plain';"
             + "    }"
             + "}"
             + "</script>"
             + "</head><body>"
             + "<form name='form1' id='form1'>"
             + "<input type=checkbox name=Format value='' onclick='setFormat()'>"
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

         final HtmlCheckBoxInput checkBox = (HtmlCheckBoxInput) form.getInputByName("Format");

         final HtmlSubmitInput button = (HtmlSubmitInput)form.getInputByName("button");

         final List expectedParameters0 = Arrays.asList( new Object[]{
             new KeyValuePair("button", "foo")
         } );
         final List expectedParameters1 = Arrays.asList( new Object[]{
             new KeyValuePair("Format", "html"),
             new KeyValuePair("button", "foo")
         } );


         button.click();
         final List collectedParameters0 = webConnection.getLastParameters();

         checkBox.click();
         button.click();
         final List collectedParameters1 = webConnection.getLastParameters();

         assertEquals( expectedParameters0, collectedParameters0 );
         assertEquals( expectedParameters1, collectedParameters1 );
     }

    /**
     * @throws Exception if the test fails
     */
    public void testGetInputByValue() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + "    <input type='submit' name='button' value='xxx'/>"
            + "    <input type='text' name='textfield' value='foo'/>"
            + "    <input type='submit' name='button1' value='foo'/>"
            + "    <input type='reset' name='button2' value='foo'/>"
            + "    <input type='submit' name='button' value='bar'/>"
            + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final List allInputsByValue = form.getInputsByValue("foo");
        final ListIterator iterator = allInputsByValue.listIterator();
        while( iterator.hasNext() ) {
            final HtmlInput input = (HtmlInput)iterator.next();
            iterator.set( input.getNameAttribute() );
        }

        final List expectedInputs = Arrays.asList( new String[] {
            "textfield", "button1", "button2"
        } );
        assertEquals( "Get all", expectedInputs, allInputsByValue );
        assertEquals( Collections.EMPTY_LIST, form.getInputsByValue("none-matching"));

        assertEquals("Get first", "button", form.getInputByValue("bar").getNameAttribute() );
        try {
            form.getInputByValue("none-matching");
            fail("Expected ElementNotFoundException");
        }
        catch( final ElementNotFoundException e ) {
            // Expected path.
        }
    }
}

