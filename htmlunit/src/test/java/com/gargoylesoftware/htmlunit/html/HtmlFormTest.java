/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebRequestSettings;

/**
 *  Tests for HtmlForm
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Jun Chen</a>
 * @author George Murnock
 * @author Marc Guillemot
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
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);
        
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSubmitInput pushButton = ( HtmlSubmitInput )form.getInputByName( "button" );

        form.setCheckedRadioButton( "foo", "2" );

        assertFalse( ((HtmlRadioButtonInput)page.getHtmlElementById("input1")).isChecked() );
        assertTrue( ((HtmlRadioButtonInput)page.getHtmlElementById("input2")).isChecked() );

        // Test that only one value for the radio button is being passed back to the server
        final HtmlPage secondPage = ( HtmlPage )pushButton.click();

        assertEquals("url", URL_GARGOYLE.toExternalForm() + "?foo=2&button=foo",
                secondPage.getWebResponse().getUrl());
        assertEquals("method", SubmitMethod.GET, webConnection.getLastMethod());
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
        final HtmlPage page = loadPage(htmlContent);

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
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);
        
        final HtmlForm form = (HtmlForm) page.getHtmlElementById( "form1" );

        final HtmlSubmitInput pushButton = ( HtmlSubmitInput )form.getInputByName( "button" );

        form.fakeCheckedRadioButton( "foo", "4" );

        // Test that only one value for the radio button is being passed back to the server
        final HtmlPage secondPage = ( HtmlPage )pushButton.click();

        assertEquals("url", URL_GARGOYLE.toExternalForm() + "?button=foo&foo=4",
            secondPage.getWebResponse().getUrl());
        assertEquals("method", SubmitMethod.GET, webConnection.getLastMethod() );
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
        final HtmlPage page = loadPage(htmlContent);
        
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
            + "<form id='form1' method='post'>"
            + "    <input type='text' name='textfield' value='*'/>"
            + "    <input type='submit' name='button' value='foo'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);
        
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(URL_FIRST);
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(URL_FIRST);
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(URL_FIRST);
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(URL_FIRST);
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
        final String htmlContent
            = "<html><body><form method='POST' action='http://first'>"
            + "<table><tr> <td ><input type='radio' name='name1' value='foo'> "
            + "Option 1</td> </tr>"
            + "<tr> <td ><input type='radio' name='name1' value='bar' checked >"
            + "Option 2</td> </tr>"
            + "<tr> <td ><input type='radio' name='name1' value='baz'> Option 3</td> </tr>"
            + "</table><input type='submit' value='Login' name='loginButton1'></form>"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(URL_FIRST);
        final HtmlResetInput button = (HtmlResetInput)firstPage.getHtmlElementById("button");

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
        final HtmlPage secondPage = (HtmlPage)button.click();

        assertSame( firstPage, secondPage );
        assertEquals( Collections.singletonList("clicked"), collectedAlerts );
    }


    /**
     * <p>Simulate a bug report where an anchor contained javascript that caused a form submit.
     * According to the bug report, the form would be submitted even though the onsubmit
     * handler would return false.  This wasn't reproducible but I added a test for it anyway.</p>
     * 
     * <p>UPDATE: If the form submit is triggered by javascript then the onsubmit handler is not
     * supposed to be called so it doesn't matter what value it returns.</p>
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(URL_FIRST);
        final HtmlAnchor anchor = (HtmlAnchor)firstPage.getHtmlElementById("link1");

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
        final HtmlPage secondPage = (HtmlPage)anchor.click();
        assertEquals( "Second", secondPage.getTitleText() );

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testSubmit_NoDefaultValue()
        throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1' method='post'>"
            + "    <input type='text' name='textfield'/>"
            + "    <input type='submit' name='button' value='foo'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);
        
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
        final HtmlPage page = loadPage(htmlContent);
        
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);
        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(URL_FIRST);
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
            + "<form id='form1' method='post'>"
            + "    <input type='text' id='textfield' value='blah'/>"
            + "    <input type='submit' name='button' value='foo'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);
       
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
    public void testSubmit_NoNameOnButton() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1' method='post'>"
            + "    <input type='text' id='textfield' value='blah' name='textfield' />"
            + "    <button type='submit' id='button' value='Go'>Go</button>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);
       
        final HtmlButton button = (HtmlButton) page.getHtmlElementById( "button" );
        button.click();

        final List expectedParameters = Arrays.asList( new Object[]{
            new KeyValuePair("textfield", "blah")
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
            + "<form id='form1' method='post'>"
            + "    <table><tr><td>"
            + "        <input type='text' name='textfield' value='blah'/>"
            + "        </td><td>"
            + "        <input type='submit' name='button' value='foo'/>"
            + "        </td></tr>"
            + "     </table>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);
        
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
            + "<form id='form1' method='post'>"
            + "    <input type='text' name='textfield' value='blah' disabled />"
            + "    <input type='submit' name='button' value='foo'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);
        
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
            + "<form name='form1' id='form1' method='post'>"
            + "<input type=checkbox name=Format value='' onclick='setFormat()'>"
            + "    <input type='submit' name='button' value='foo'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);
         
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
        final HtmlPage page = loadPage(htmlContent);
        
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

    /**
     * Test that the result of the form will get loaded into the window
     * specified by "target"
     * @throws Exception If the test fails.
     */
    public void testSubmitToTargetWindow() throws Exception {
        final String firstContent
            = "<html><head><title>first</title></head><body>"
            + "<form id='form1' target='window2' action='http://second' method='post'>"
            + "    <input type='submit' name='button' value='push me'/>"
            + "</form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponseAsGenericHtml(URL_SECOND, "second");
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(URL_FIRST);
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSubmitInput button = (HtmlSubmitInput)form.getInputByName("button");
        final HtmlPage secondPage = (HtmlPage)button.click();
        assertEquals("window2", secondPage.getEnclosingWindow().getName());

        final WebWindow firstWindow  = client.getCurrentWindow();
        assertEquals("first window name", "", firstWindow.getName() );
        assertSame( page, firstWindow.getEnclosedPage() );
    }
    
    /**
     * @throws Exception if the test fails
     */
    public void testSubmit_SelectHasNoOptions()throws Exception {

        final String htmlContent
            = "<html><body><form name='form' method='GET' action='action.html'>"
            + "<select name='select'>"
            + "</select>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);
        
        final HtmlPage secondPage = ( HtmlPage ) page.getFormByName("form").submit();
        
        assertNotNull( secondPage );
        assertEquals( "parameters", Collections.EMPTY_LIST, webConnection.getLastParameters() );
    }    
    
    /**
     * @throws Exception if the test fails
     */
    public void testSubmit_SelectOptionWithoutValueAttribute() throws Exception {

        final String htmlContent
            = "<html><body><form name='form' action='action.html'>"
            + "<select name='select'>"
            + "     <option>first value</option>"
            + "     <option selected>second value</option>"
            + "</select>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlPage secondPage = (HtmlPage) page.getFormByName("form").submit();
        
        assertNotNull( secondPage );
        assertEquals(page.getWebResponse().getUrl().toExternalForm() + "action.html?select=second+value",
                secondPage.getWebResponse().getUrl());
    }        
    
    /**
     * At one point this test was failing because deeply nested inputs weren't getting picked up.
     * @throws Exception if the test fails
     */
    public void testSubmit_DeepInputs() throws Exception {
        final String htmlContent
            = "<html><form method='post' action=''>"
            + "<table><tr><td>"
            + "<input value='NOT_SUBMITTED' name='data' type='text'/>"
            + "</td></tr></table>"
            + "<input id='submitButton' name='submit' type='submit'/>"
            + "</form></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);
        
        final HtmlInput submitButton = (HtmlInput)page.getHtmlElementById("submitButton");
        submitButton.click();
        
        final List collectedParameters = webConnection.getLastParameters();
        final List expectedParameters = Arrays.asList( new Object[] { 
            new KeyValuePair("data", "NOT_SUBMITTED"),
            new KeyValuePair("submit", "")
        } );
        assertEquals(expectedParameters, collectedParameters);
    }

    /**
     * Test order of submitted parameters matches order of elements in form.
     * @throws Exception if the test fails
     */
    public void testSubmit_FormElementOrder() throws Exception {
        final String htmlContent
            = "<html><head></head><body><form method='post' action=''>"
            + "<input type='submit' name='dispatch' value='Save' id='submitButton'>"
            + "<input type='hidden' name='dispatch' value='TAB'>"
            + "</form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final WebRequestSettings settings = new WebRequestSettings(URL_GARGOYLE, SubmitMethod.POST);

        final HtmlPage page = ( HtmlPage )client.getPage(settings);
        final HtmlInput submitButton = (HtmlInput)page.getHtmlElementById("submitButton");
        submitButton.click();

        final List collectedParameters = webConnection.getLastParameters();
        final List expectedParameters = Arrays.asList( new Object[] {
            new KeyValuePair("dispatch", "Save"),
            new KeyValuePair("dispatch", "TAB"),
        } );
        assertCollectionsEqual(expectedParameters, collectedParameters);
    }

     /**
      * Simulate a bug report where using JavaScript to submit a form that contains a
      * JavaScript action causes a an "IllegalArgumentException: javascript urls can only
      * be used to load content into frames and iframes."
      *
      * @throws Exception if the test fails
      */
    public void testJSSubmit_JavaScriptAction() throws Exception {
        final String htmlContent
            = "<html><head><title>First</title></head>"
            + "<body onload='document.getElementById(\"aForm\").submit()'>"
            + "<form id='aForm' action='javascript:alert(\"clicked\")'"
            + "</form>"
            + "</body></html>";

        final List expectedAlerts = Collections.singletonList("clicked");
        createTestPageForRealBrowserIfNeeded(htmlContent, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }



    /**
     * @throws Exception if the test fails
     */
    public void testUrlAfterGetSubmit()
        throws Exception {
        testUrlAfterSubmit("get", "foo", "foo?textField=foo&nonAscii=Flo%DFfahrt&button=foo");
        // for a get submit, query parameters in action are lost in browsers
        testUrlAfterSubmit("get", "foo?foo=12", "foo?textField=foo&nonAscii=Flo%DFfahrt&button=foo");
        testUrlAfterSubmit("post", "foo", "foo");
        testUrlAfterSubmit("post", "foo?foo=12", "foo?foo=12");
    }

    /**
     * Utility for {@link #testUrlAfterGetSubmit}
     * @throws Exception if the test fails
     */
    private void testUrlAfterSubmit(final String method, final String action, final String expectedUrlEnd)
        throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1' method='" + method + "' action='" + action + "'>"
            + "<input type='text' name='textField' value='foo'/>"
            + "<input type='text' name='nonAscii' value='Floßfahrt'/>"
            + "<input type='submit' name='button' value='foo'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");
        final Page page2 = form.submit("button");
        
        assertEquals(URL_GARGOYLE.toExternalForm() + expectedUrlEnd, 
                page2.getWebResponse().getUrl());
    }
}

