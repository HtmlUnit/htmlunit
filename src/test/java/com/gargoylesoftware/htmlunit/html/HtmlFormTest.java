/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * Tests for {@link HtmlForm}.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Jun Chen</a>
 * @author George Murnock
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Philip Graf
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

        ((HtmlRadioButtonInput)form.getByXPath(
                "//input[@type='radio' and @name='foo' and @value='2']" ).get(0)).setChecked(true);

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

        assertTrue( form.getByXPath(
            "//input[@type='radio' and @name='foo' and @value='4']" ).isEmpty() );
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
     * Regression test for bug 1628521
     * Nullpointer exception when submitting forms
     * @throws Exception if the test fails
     */
    public void testSubmit_onSubmitHandler_fails() throws Exception {

        final String firstContent
            = "<html><head><title>First</title></head><body>"
            + "<form method='get' action='http://second' onSubmit='return null'>"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlSubmitInput button = (HtmlSubmitInput) firstPage.getHtmlElementById("button");
        final HtmlPage secondPage = (HtmlPage) button.click();
        assertEquals("Second", secondPage.getTitleText());
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

        final String html
            = "<html><head><title>First</title></head><body>"
            + "<form method='get' action='http://second' "
            + "onReset='alert(\"clicked\");alert(event.type)'>"
            + "<input name='button' type='reset' value='PushMe' id='button'/></form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();

        final HtmlPage firstPage = loadPage(html, collectedAlerts);
        final HtmlResetInput button = (HtmlResetInput)firstPage.getHtmlElementById("button");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final HtmlPage secondPage = (HtmlPage) button.click();
        assertSame(firstPage, secondPage);

        final String[] expectedAlerts = {"clicked", "reset"};
        assertEquals(expectedAlerts, collectedAlerts);
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
     * Reset buttons should not be sucessful controls.
     * @see <a href="http://www.w3.org/TR/html4/interact/forms.html#h-17.13.2">Spec</a>
     * @throws Exception if the test fails
     */
    public void testSubmit_IgnoresResetControls() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>"
            + "<form id='form1' method='post'>"
            + "    <button type='reset' name='buttonreset' value='buttonreset'/>"
            + "    <input type='reset' name='reset' value='reset'/>"
            + "    <input type='submit' name='submit' value='submit'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");

        final HtmlSubmitInput button = (HtmlSubmitInput) form.getInputByName("submit");
        button.click();

        final List expectedParameters = Arrays.asList(new Object[] {new KeyValuePair(
                "submit",
                "submit")});
        final List collectedParameters = webConnection.getLastParameters();

        assertEquals(expectedParameters, collectedParameters);
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
            + "    <input type=checkbox name=Format value='' onclick='setFormat()'>"
            + "    <input type='submit' name='button' value='foo'/>"
            + "</form></body></html>";

        final HtmlPage page1 = loadPage( htmlContent );
        final MockWebConnection webConnection1 = getMockConnection( page1 );
        final HtmlForm form1 = (HtmlForm) page1.getHtmlElementById( "form1" );
        final HtmlSubmitInput button1 = (HtmlSubmitInput) form1.getInputByName( "button" );

        final HtmlPage page2 = (HtmlPage) button1.click();
        final List collectedParameters1 = webConnection1.getLastParameters();
        final List expectedParameters1 = Arrays.asList( new Object[] {
            new KeyValuePair( "button", "foo" )
        } );

        final MockWebConnection webConnection2 = getMockConnection( page2 );
        final HtmlForm form2 = (HtmlForm) page2.getHtmlElementById( "form1" );
        final HtmlCheckBoxInput checkBox2 = (HtmlCheckBoxInput) form2.getInputByName( "Format" );
        final HtmlSubmitInput button2 = (HtmlSubmitInput) form2.getInputByName( "button" );

        checkBox2.click();
        button2.click();
        final List collectedParameters2 = webConnection2.getLastParameters();
        final List expectedParameters2 = Arrays.asList( new Object[] {
            new KeyValuePair( "Format", "html" ),
            new KeyValuePair( "button", "foo" )
        } );

        assertEquals( expectedParameters1, collectedParameters1 );
        assertEquals( expectedParameters2, collectedParameters2 );
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
     * Test that {@link HtmlForm#getTextAreaByName(String)} returns
     * the first textarea with the given name.
     * 
     * @throws Exception If the test page can't be loaded.
     */
    public void testGetTextAreaByName() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + "    <textarea id='ta1_1' name='ta1'>hello</textarea>"
            + "    <textarea id='ta1_2' name='ta1'>world</textarea>"
            + "    <textarea id='ta2_1' name='ta2'>!</textarea>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
    
        final HtmlForm form = (HtmlForm)page.getHtmlElementById("form1");
        
        assertEquals("First textarea with name 'ta1'", form.getHtmlElementById("ta1_1"), form.getTextAreaByName("ta1"));
        assertEquals("First textarea with name 'ta2'", form.getHtmlElementById("ta2_1"), form.getTextAreaByName("ta2"));
        
        try {
            form.getTextAreaByName("ta3");
            fail("Expected ElementNotFoundException as there is no textarea with name 'ta3'");
        }
        catch (final ElementNotFoundException e) {
            // pass: exception is expected
        }
    }

    /**
     * Test that {@link HtmlForm#getButtonByName(String)} returns
     * the first button with the given name.
     * 
     * @throws Exception If the test page can't be loaded.
     */
    public void testGetButtonByName() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + "    <button id='b1_1' name='b1' value='hello' type='button'/>"
            + "    <button id='b1_2' name='b1' value='world' type='button'/>"
            + "    <button id='b2_1' name='b2' value='!' type='button'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        
        final HtmlForm form = (HtmlForm)page.getHtmlElementById("form1");
        
        assertEquals("First button with name 'b1'", form.getHtmlElementById("b1_1"), form.getButtonByName("b1"));
        assertEquals("First button with name 'b2'", form.getHtmlElementById("b2_1"), form.getButtonByName("b2"));
        
        try {
            form.getTextAreaByName("b3");
            fail("Expected ElementNotFoundException as there is no button with name 'b3'");
        }
        catch (final ElementNotFoundException e) {
            // pass: exception is expected
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
            new KeyValuePair("submit", "Submit Query")
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
     * Test the 'Referer' HTTP header
     * @throws Exception on test failure
     */
    public void testSubmit_refererHeader() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>"
            + "<form method='post' action='http://second'>"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlSubmitInput button = (HtmlSubmitInput) firstPage.getHtmlElementById("button");

        button.click();

        final Map lastAdditionalHeaders = webConnection.getLastAdditionalHeaders();
        assertEquals(URL_FIRST.toString(), lastAdditionalHeaders.get("Referer"));
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
    public void testUrlAfterSubmit()
        throws Exception {
        testUrlAfterSubmit("get", "foo", "foo?textField=foo&nonAscii=Flo%DFfahrt&button=foo");
        // for a get submit, query parameters in action are lost in browsers
        testUrlAfterSubmit("get", "foo?foo=12", "foo?textField=foo&nonAscii=Flo%DFfahrt&button=foo");
        testUrlAfterSubmit("post", "foo", "foo");
        testUrlAfterSubmit("post", "foo?foo=12", "foo?foo=12");
        testUrlAfterSubmit("post", "", "");
        testUrlAfterSubmit("post", "?a=1&b=2", "?a=1&b=2");
        testUrlAfterSubmit(new URL("http://first?a=1&b=2"), "post", "", "");

    }

    /**
     * @throws Exception if the test fails
     */
    public void testUrlAfterSubmitWithAnchor() throws Exception {
        testUrlAfterSubmit("get", "foo#anchor", "foo?textField=foo&nonAscii=Flo%DFfahrt&button=foo#anchor");
        testUrlAfterSubmit("get", "foo?foo=12#anchor", "foo?textField=foo&nonAscii=Flo%DFfahrt&button=foo#anchor");
        testUrlAfterSubmit("post", "foo#anchor", "foo#anchor");
        testUrlAfterSubmit("post", "foo?foo=12#anchor", "foo?foo=12#anchor");
    }

    /**
     * Utility for {@link #testUrlAfterSubmit()}
     * @param method The form method to use
     * @param action The form action to use
     * @param expectedUrlEnd The expected url
     * @throws Exception if the test fails
     */
    private void testUrlAfterSubmit(final URL url, final String method, final String action,
            final String expectedUrlEnd)
        throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1' method='" + method + "' action='" + action + "'>"
            + "<input type='text' name='textField' value='foo'/>"
            + "<input type='text' name='nonAscii' value='Floßfahrt'/>"
            + "<input type='submit' name='button' value='foo'/>"
            + "<input type='button' name='inputButton' value='foo'/>"
            + "<button type='button' name='buttonButton' value='foo'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent, null, url);
        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");
        final Page page2 = form.submit("button");

        assertEquals(url.toExternalForm() + expectedUrlEnd,
                page2.getWebResponse().getUrl());
    }

    /**
     * Utility for {@link #testUrlAfterSubmit()}. Calls {@link #testUrlAfterSubmit(URL, String, String, String)} with
     * URL_GARGOYLE.
     * @param method The form method to use
     * @param action The form action to use
     * @param expectedUrlEnd The expected url
     * @throws Exception if the test fails
     */
    private void testUrlAfterSubmit(final String method, final String action, final String expectedUrlEnd)
        throws Exception {
        testUrlAfterSubmit(URL_GARGOYLE, method, action, expectedUrlEnd);
    }

    /**
     * Utility for {@link #testUrlAfterSubmit()}
     * @throws Exception if the test fails
     */
    public void testSubmitRequestCharset() throws Exception {
        testSubmitRequestCharset("UTF-8", null, null, "UTF-8");
        testSubmitRequestCharset(null, "UTF-8", null, "UTF-8");
        testSubmitRequestCharset("ISO-8859-1", null, "UTF-8", "UTF-8");
        testSubmitRequestCharset("ISO-8859-1", null, "UTF-8, ISO-8859-1", "UTF-8");
        testSubmitRequestCharset("UTF-8", null, "ISO-8859-1 UTF-8", "ISO-8859-1");
        testSubmitRequestCharset("ISO-8859-1", null, "UTF-8, ISO-8859-1", "UTF-8");
    }

    /**
     * Utility for {@link #testSubmitRequestCharset()}
     * @param headerCharset the charset for the content type header if not null
     * @param metaCharset the charset for the meta http-equiv content type tag if not null
     * @param formCharset the charset for the form's accept-charset attribute if not null
     * @param expectedRequestCharset the charset expected for the form submission
     * @throws Exception if the test fails
     */
    private void testSubmitRequestCharset(final String headerCharset,
            final String metaCharset, final String formCharset,
            final String expectedRequestCharset) throws Exception {

        final String formAcceptCharset;
        if (formCharset == null) {
            formAcceptCharset = "";
        }
        else {
            formAcceptCharset = " accept-charset='" + formCharset + "'";
        }

        final String metaContentType;
        if (metaCharset == null) {
            metaContentType = "";
        }
        else {
            metaContentType = "<meta http-equiv='Content-Type' content='text/html; charset="
                + metaCharset + "'>";
        }

        final String content = "<html><head><title>foo</title>"
            + metaContentType
            + "</head><body>"
            + "<form name='form1' method='post' action='foo'"
            + formAcceptCharset + ">"
            + "<input type='text' name='textField' value='foo'/>"
            + "<input type='text' name='nonAscii' value='Floßfahrt'/>"
            + "<input type='submit' name='button' value='foo'/>"
            + "</form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        client.setWebConnection( webConnection );

        String contentType = "text/html";
        if (headerCharset != null) {
            contentType += ";charset=" + headerCharset;
        }
        webConnection.setDefaultResponse(content, 200, "ok", contentType);
        final HtmlPage page = (HtmlPage) client.getPage(URL_GARGOYLE);

        final String firstPageEncoding = StringUtils.defaultString(metaCharset, headerCharset);
        assertEquals(firstPageEncoding, page.getPageEncoding());

        final HtmlForm form = page.getFormByName("form1");
        form.getInputByName("button").click();

        assertEquals(expectedRequestCharset, webConnection.getLastWebRequestSettings().getCharset());
    }

    /**
     * @throws Exception If the test fails
     */
    public void testSumbit_submitInputValue() throws Exception {
        testSumbit_submitInputValue( BrowserVersion.INTERNET_EXPLORER_6_0 );
        //test FF separately as value is not to DEFAULT_VALUE if not specified. 
        testSumbit_submitInputValue( BrowserVersion.MOZILLA_1_0 );
    }

    private void testSumbit_submitInputValue(final BrowserVersion browserVersion) throws Exception {
        final String html =
            "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<form action='" + URL_SECOND + "'>\n"
            + "  <input id='myButton' type='submit' name='Save'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage firstPage = loadPage(browserVersion, html, null );
        final HtmlSubmitInput submitInput = (HtmlSubmitInput)firstPage.getHtmlElementById( "myButton" );
        final HtmlPage secondPage = (HtmlPage)(submitInput).click();
        assertEquals( URL_SECOND.toExternalForm() + "?Save=Submit+Query", secondPage.getWebResponse().getUrl() );
    }
}
