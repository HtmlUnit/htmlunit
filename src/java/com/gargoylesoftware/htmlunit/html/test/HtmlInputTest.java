/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html.test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Tests for HtmlInput
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class HtmlInputTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlInputTest( final String name ) {
        super( name );
    }


    /**
     *  Test that selecting one radio button will deselect all the others
     *
     * @exception  Exception If the test fails
     */
    public void testRadioButtonsAreMutuallyExclusive()
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

        final HtmlRadioButtonInput radioButton = form.getRadioButtonInput( "foo", "2" );
        final HtmlSubmitInput pushButton = ( HtmlSubmitInput )form.getInputByName( "button" );

        radioButton.setChecked( true );

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


    public void testSetChecked_CheckBox()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<input type='checkbox' name='foo'/>"
                 + "<input type='checkbox' name='bar'/>"
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

        final HtmlCheckBoxInput checkbox = ( HtmlCheckBoxInput )form.getInputByName( "foo" );
        assertFalse( "Initial state", checkbox.isChecked() );
        checkbox.setChecked( true );
        assertTrue( "After setSelected(true)", checkbox.isChecked() );
        checkbox.setChecked( false );
        assertFalse( "After setSelected(false)", checkbox.isChecked() );
    }


    public void testGetChecked_RadioButton()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<input type='radio' name='radio1'>"
                 + "<input type='RADIO' name='radio1' value='bar' checked>"
                 + "<input type='submit' name='button' value='foo'>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final List radioButtons = form.getRadioButtonsByName("radio1");
        assertEquals( 2, radioButtons.size() );

        assertFalse( ((HtmlRadioButtonInput)radioButtons.get(0)).isChecked() );
        assertTrue( ((HtmlRadioButtonInput)radioButtons.get(1)).isChecked() );
    }


    public void testOnChangeHandler() throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<input type='text' name='text1' onchange='alert(\"changed\")')>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );
        final HtmlTextInput input = (HtmlTextInput)form.getInputByName("text1");

        assertEquals( Collections.EMPTY_LIST, collectedAlerts );
        input.setValueAttribute("foo");
        assertEquals( Collections.singletonList("changed"), collectedAlerts );
    }
}

