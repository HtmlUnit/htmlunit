/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.FakeWebConnection;
import com.gargoylesoftware.htmlunit.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  Tests for HtmlSelect
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlSelectTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name Name of the test
     */
    public HtmlSelectTest( final String name ) {
        super( name );
    }


    /**
     *  Test the good path of submitting a select
     *
     * @exception  Exception If the test fails
     */
    public void testSelect()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'><select name='select1'>"
                 + "<option value='option1'>Option1</option>"
                 + "<option value='option2' selected='selected'>Option2</option>"
                 + "<option value='option3'>Option3</option>"
                 + "</select>"
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

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );
        final HtmlSubmitInput button = ( HtmlSubmitInput )form.getInputByName( "button" );

        // Test that the select is being correctly identified as a submittable element
        assertCollectionsEqual(
                Arrays.asList( new Object[]{select} ),
                form.getAllSubmittableElements() );

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = ( HtmlPage )button.click();

        final List expectedParameters = new ArrayList();
        expectedParameters.add( new KeyValuePair( "select1", "option2" ) );
        expectedParameters.add( new KeyValuePair( "button", "foo" ) );

        assertEquals( "url", new URL( "http://www.gargoylesoftware.com/" ),
            secondPage.getWebResponse().getUrl() );
        assertEquals( "method", SubmitMethod.GET, webConnection.getLastMethod() );
        assertEquals( "parameters", expectedParameters, webConnection.getLastParameters() );
        assertNotNull( secondPage );
    }


    /**
     *  Test submitting the select with no options selected
     *
     * @exception  Exception If the test fails
     */
    public void testSelect_NoOptionsSelected()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'><select name='select1'>"
                 + "<option value='option1'>Option1</option>"
                 + "<option value='option2'>Option2</option>"
                 + "<option value='option3'>Option3</option>"
                 + "</select>"
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

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );
        assertNotNull(select);

        final HtmlSubmitInput button = ( HtmlSubmitInput )form.getInputByName( "button" );

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = ( HtmlPage )button.click();

        final List expectedParameters = new ArrayList();
        expectedParameters.add( new KeyValuePair( "button", "foo" ) );

        assertEquals( "url", new URL( "http://www.gargoylesoftware.com/" ),
            secondPage.getWebResponse().getUrl() );
        assertEquals( "method", SubmitMethod.GET, webConnection.getLastMethod() );
        assertEquals( "parameters", expectedParameters, webConnection.getLastParameters() );
        assertNotNull( secondPage );
    }


    /**
     *  Test changing the selected option
     *
     * @exception  Exception If the test fails
     */
    public void testSelect_ChangeSelectedOption_SingleSelect()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'><select name='select1'>"
                 + "<option value='option1' selected='selected'>Option1</option>"
                 + "<option value='option2'>Option2</option>"
                 + "<option value='option3'>Option3</option>"
                 + "</select>"
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

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );
        final HtmlSubmitInput button = ( HtmlSubmitInput )form.getInputByName( "button" );

        // Change the value
        select.setSelectedAttribute( "option3", true );

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = ( HtmlPage )button.click();

        final List expectedParameters = new ArrayList();
        expectedParameters.add( new KeyValuePair( "select1", "option3" ) );
        expectedParameters.add( new KeyValuePair( "button", "foo" ) );

        assertEquals( "url", new URL( "http://www.gargoylesoftware.com/" ),
            secondPage.getWebResponse().getUrl() );
        assertEquals( "method", SubmitMethod.GET, webConnection.getLastMethod() );
        assertEquals( "parameters", expectedParameters, webConnection.getLastParameters() );
        assertNotNull( secondPage );
    }


    /**
     *  Test changing the selected option
     *
     * @exception  Exception If the test fails
     */
    public void testSelect_ChangeSelectedOption_MultipleSelect()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'><select name='select1' multiple='multiple'>"
                 + "<option value='option1' selected='selected'>Option1</option>"
                 + "<option value='option2'>Option2</option>"
                 + "<option value='option3'>Option3</option>"
                 + "</select>"
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

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );
        final HtmlSubmitInput button = ( HtmlSubmitInput )form.getInputByName( "button" );

        // Change the value
        select.setSelectedAttribute( "option3", true );
        select.setSelectedAttribute( "option2", true );

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = ( HtmlPage )button.click();

        final List expectedParameters = new ArrayList();
        expectedParameters.add( new KeyValuePair( "select1", "option1" ) );
        expectedParameters.add( new KeyValuePair( "select1", "option2" ) );
        expectedParameters.add( new KeyValuePair( "select1", "option3" ) );
        expectedParameters.add( new KeyValuePair( "button", "foo" ) );

        assertEquals( "url", new URL( "http://www.gargoylesoftware.com/" ),
            secondPage.getWebResponse().getUrl() );
        assertEquals( "method", SubmitMethod.GET, webConnection.getLastMethod() );
        assertEquals( "parameters", expectedParameters, webConnection.getLastParameters() );
        assertNotNull( secondPage );
    }


    /**
     *  Test changing the selected option
     *
     * @exception  Exception If the test fails
     */
    public void testSelect_MultipleOptionsSelected()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'><select name='select1'>"
                 + "<option value='option1' selected='selected'>Option1</option>"
                 + "<option value='option2'>Option2</option>"
                 + "<option value='option3' selected='selected'>Option3</option>"
                 + "</select>"
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

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );
        final List expected = new ArrayList();
        expected.add( select.getOptionByValue( "option1" ) );
        expected.add( select.getOptionByValue( "option3" ) );

        assertEquals( expected, select.getSelectedOptions() );
    }


    /**
     *  Test changing the selected option
     *
     * @exception  Exception If the test fails
     */
    public void testSetSelected_IllegalValue()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'><select name='select1'>"
                 + "<option value='option1' selected='selected'>Option1</option>"
                 + "<option value='option2'>Option2</option>"
                 + "<option value='option3'>Option3</option>"
                 + "</select>"
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

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );
        final HtmlSubmitInput button = ( HtmlSubmitInput )form.getInputByName( "button" );

        // Change the value
        try {
            select.setSelectedAttribute( "missingOption", true );
            fail( "Expected IllegalArgumentException" );
        }
        catch( final IllegalArgumentException e ) {
            // Expected path
        }

        select.fakeSelectedAttribute( "newOption" );

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = ( HtmlPage )button.click();

        final List expectedParameters = new ArrayList();
        expectedParameters.add( new KeyValuePair( "select1", "newOption" ) );
        expectedParameters.add( new KeyValuePair( "button", "foo" ) );

        assertEquals( "url", new URL( "http://www.gargoylesoftware.com/" ),
            secondPage.getWebResponse().getUrl() );
        assertEquals( "method", SubmitMethod.GET, webConnection.getLastMethod() );
        assertEquals( "parameters", expectedParameters, webConnection.getLastParameters() );
        assertNotNull( secondPage );
    }


    public void testGetAllOptions()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'><select name='select1'>"
                 + "<option value='option1' selected='selected'>Option1</option>"
                 + "<option value='option2'>Option2</option>"
                 + "<optgroup label='group1'>"
                 + "    <option value='option3'>Option3</option>"
                 + "</optgroup>"
                 + "</select>"
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

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );

        final List expectedOptions = new ArrayList();
        expectedOptions.add( select.getOptionByValue( "option1" ) );
        expectedOptions.add( select.getOptionByValue( "option2" ) );
        expectedOptions.add( select.getOptionByValue( "option3" ) );

        assertEquals( expectedOptions, select.getAllOptions() );
    }


    public void testSelect_OptionMultiple_NoValueOnAttribute() throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'><select name='select1' id='select1' multiple>"
                 + "<option value='option1'>Option1</option>"
                 + "<option value='option2' >Option2</option>"
                 + "<option value='option3'>Option3</option>"
                 + "</select>"
                 + "<input type='submit' name='button' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        final HtmlSelect select = (HtmlSelect)page.getHtmlElementById("select1");
        assertTrue( select.isMultipleSelectEnabled() );
    }


    public void testGetOptionByValue_TwoOptionsWithSameValue() throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body><form id='form1'>"
                 + "<select name='select1'>"
                 + "    <option value='option1'>s1o1</option>"
                 + "    <option value='option2'>s1o2</option>"
                 + "</select>"
                 + "<select name='select2'>"
                 + "    <option value='option1'>s2o1</option>"
                 + "    <option value='option2'>s2o2</option>"
                 + "</select>"
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

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select2" ).get( 0 );
        assertEquals( "s2o2", select.getOptionByValue("option2").asText() );
    }


    public void testSelect_SetSelected_OnChangeHandler()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'><select name='select1' onChange='alert(\"changing\")'>"
                 + "<option value='option1' selected='selected'>Option1</option>"
                 + "<option value='option2'>Option2</option>"
                 + "<option value='option3'>Option3</option>"
                 + "</select>"
                 + "<input type='submit' name='button' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );

        // Change the value
        select.setSelectedAttribute( "option3", true );

        final List expectedAlerts = Collections.singletonList("changing");
        assertEquals( expectedAlerts, collectedAlerts );
    }
}

