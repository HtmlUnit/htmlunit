/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.MockWebConnection;
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
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
    public void testSelect_MultipleSelectNoneSelected()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'><select name='select1' multiple>"
                 + "<option value='option1'>Option1</option>"
                 + "<option value='option2'>Option2</option>"
                 + "<option value='option3'>Option3</option>"
                 + "</select>"
                 + "<input type='submit' name='button' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
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
     *  Test multiple selected options on multiple select lists
     *
     * @exception  Exception If the test fails
     */
    public void testSelect_MultipleSelectMultipleSelected()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'><select name='select1' multiple>"
                 + "<option value='option1' selected='selected'>Option1</option>"
                 + "<option value='option2'>Option2</option>"
                 + "<option value='option3' selected='selected'>Option3</option>"
                 + "</select>"
                 + "<input type='submit' name='button' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );
        final List expected = new ArrayList();
        expected.add( select.getOptionByValue( "option1" ) );
        expected.add( select.getOptionByValue( "option3" ) );

        assertEquals( expected, select.getSelectedOptions() );
    }


    /**
     * Test multiple selected options on single select lists. This is erroneous HTML, but
     * browsers simply use the last option.
     *
     * @exception  Exception If the test fails
     */
    public void testSelect_SingleSelectMultipleSelected()
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );
        final List expected = new ArrayList();
        expected.add( select.getOptionByValue( "option3" ) );

        assertEquals( expected, select.getSelectedOptions() );
    }


    /**
     * Test no selected options on single select lists. This is erroneous HTML, but
     * browsers simply assume the first one to be selected
     *
     * @exception  Exception If the test fails
     */
    public void testSelect_SingleSelectNoneSelected()
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );
        final List expected = new ArrayList();
        expected.add( select.getOptionByValue( "option1" ) );

        assertEquals( expected, select.getSelectedOptions() );
    }

    /**
     * Test no selected options on single select lists with a size > 1
     * 
     * @exception  Exception If the test fails
     */
    public void testSelect_SingleSelectNoneSelectedButSizeGreaterThanOne() throws Exception {

        final String htmlContent = "<html><head><title>foo</title></head><body>"
            + "<form>"
            + "<select name='select1' size='2' id='mySelect'>"
            + "<option value='option1'>Option1</option>"
            + "<option value='option2'>Option2</option>"
            + "<option value='option3'>Option3</option>"
            + "</select>"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlSelect select = (HtmlSelect) page.getHtmlElementById("mySelect");

        assertEquals(Collections.EMPTY_LIST, select.getSelectedOptions());
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
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


    /**
     * @throws Exception if the test fails
     */
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );

        final List expectedOptions = new ArrayList();
        expectedOptions.add( select.getOptionByValue( "option1" ) );
        expectedOptions.add( select.getOptionByValue( "option2" ) );
        expectedOptions.add( select.getOptionByValue( "option3" ) );

        assertEquals( expectedOptions, select.getAllOptions() );
    }


    /**
     * @throws Exception if the test fails
     */
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );

        final HtmlSelect select = (HtmlSelect)page.getHtmlElementById("select1");
        assertTrue( select.isMultipleSelectEnabled() );
    }


    /**
     * @throws Exception if the test fails
     */
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select2" ).get( 0 );
        assertEquals( "s2o2", select.getOptionByValue("option2").asText() );
    }


    /**
     * @throws Exception if the test fails
     */
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

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );

        // Change the value
        select.setSelectedAttribute( "option3", true );

        final List expectedAlerts = Collections.singletonList("changing");
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetSelectionOnOptionWithNoName()throws Exception {

        final String htmlContent
            = "<html><body><form name='form' method='GET' action='action.html'>"
            + "<select name='select' multiple size='5'>"
            + "<option value='1'>111</option>"
            + "<option id='option2'>222</option>"
            + "</select>"
            + "</form></body></html>";
        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
            URL_FIRST, SubmitMethod.POST, Collections.EMPTY_LIST );

        final HtmlOption option = (HtmlOption)page.getHtmlElementById("option2");
        option.setSelected(true);
    }
}
