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
import junit.framework.AssertionFailedError;

/**
 * Tests for HtmlPage.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author David K. Taylor
 * @author Andreas Hangler
 */
public class HtmlPageTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name
     */
    public HtmlPageTest( final String name ) {
        super( name );
    }


    /**
     * @exception  Exception
     */
    public void testConstructor()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<p>hello world</p>"
                 + "<form id='form1' action='/formSubmit' method='post'>"
                 + "<input type='text' NAME='textInput1' value='textInput1'/>"
                 + "<input type='text' name='textInput2' value='textInput2'/>"
                 + "<input type='hidden' name='hidden1' value='hidden1'/>"
                 + "<input type='submit' name='submitInput1' value='push me'/>"
                 + "</form>"
                 + "</body></html>";

        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals("foo", page.getTitleText());
    }


    /**
     * @exception  Exception
     */
    public void testGetInputByName()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<p>hello world</p>"
                 + "<form id='form1' action='/formSubmit' method='post'>"
                 + "<input type='text' NAME='textInput1' value='textInput1'/>"
                 + "<input type='text' name='textInput2' value='textInput2'/>"
                 + "<input type='hidden' name='hidden1' value='hidden1'/>"
                 + "<input type='submit' name='submitInput1' value='push me'/>"
                 + "</form>"
                 + "</body></html>";

        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );
        final HtmlInput input = form.getInputByName( "textInput1" );
        assertEquals( "name", "textInput1", input.getNameAttribute() );

        assertEquals( "value", "textInput1", input.getValueAttribute() );
        assertEquals( "type", "text", input.getTypeAttribute() );
    }


    /**
     * @exception  Exception
     */
    public void testFormSubmit()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<p>hello world</p>"
                 + "<form id='form1' action='/formSubmit' method='PoSt'>"
                 + "<input type='text' NAME='textInput1' value='textInput1'/>"
                 + "<input type='text' name='textInput2' value='textInput2'/>"
                 + "<input type='hidden' name='hidden1' value='hidden1'/>"
                 + "<input type='submit' name='submitInput1' value='push me'/>"
                 + "</form>"
                 + "</body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );
        final HtmlInput textInput = form.getInputByName( "textInput1" );
        textInput.setValueAttribute( "foo" );

        final HtmlSubmitInput button = ( HtmlSubmitInput )form.getInputByName( "submitInput1" );
        final HtmlPage secondPage = ( HtmlPage )button.click();

        final List expectedParameters = new ArrayList();
        expectedParameters.add( new KeyValuePair( "textInput1", "foo" ) );
        expectedParameters.add( new KeyValuePair( "textInput2", "textInput2" ) );
        expectedParameters.add( new KeyValuePair( "hidden1", "hidden1" ) );
        expectedParameters.add( new KeyValuePair( "submitInput1", "push me" ) );

        final URL expectedUrl = new URL( "http://www.gargoylesoftware.com/formSubmit" );
        final URL actualUrl = secondPage.getWebResponse().getUrl();
        assertEquals( "url", expectedUrl.toExternalForm(), actualUrl.toExternalForm() );
        assertEquals( "method", SubmitMethod.POST, webConnection.getLastMethod() );
        assertEquals( "parameters", expectedParameters, webConnection.getLastParameters() );
        assertNotNull( secondPage );
    }


    /**
     *  Test getHtmlElement() for all elements that can be loaded
     *
     * @exception  Exception
     */
    public void testGetHtmlElement()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "    <p>hello world</p>"
                 + "    <form id='form1' id='form1' action='/formSubmit' method='post'>"
                 + "    <input type='text' NAME='textInput1' value='textInput1'/>"
                 + "    <button type='submit' name='button1'>foobar</button>"
                 + "    <select name='select1'>"
                 + "        <option value='option1'>Option1</option>"
                 + "    </select>"
                 + "    <textarea name='textArea1'>foobar</textarea>"
                 + "    </form>"
                 + "    <a href='http://www.foo.com' name='anchor1'>foo.com</a>"
                 + "    <table id='table1'>"
                 + "        <tr>"
                 + "            <th id='header1'>Header</th>"
                 + "            <td id='data1'>Data</td>"
                 + "        </tr>"
                 + "    </table>"
                 + "</body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );
        assertSame( "form1", form, page.getHtmlElementById( "form1" ) );
        assertSame( "form2", form, page.getHtmlElement( form.getElement() ) );

        final HtmlInput input = form.getInputByName( "textInput1" );
        assertSame( "input1", input, form.getInputByName( "textInput1" ) );
        assertSame( "input2", input, page.getHtmlElement( input.getElement() ) );

        final HtmlButton button = ( HtmlButton )form.getButtonsByName( "button1" ).get( 0 );
        assertSame( "button1", button, form.getButtonsByName( "button1" ).get( 0 ) );
        assertSame( "button2", button, page.getHtmlElement( button.getElement() ) );

        final HtmlSelect select = ( HtmlSelect )form.getSelectsByName( "select1" ).get( 0 );
        assertSame( "select1", select, form.getSelectsByName( "select1" ).get( 0 ) );
        assertSame( "select2", select, page.getHtmlElement( select.getElement() ) );

        final HtmlOption option = select.getOptionByValue( "option1" );
        assertSame( "option1", option, select.getOptionByValue( "option1" ) );
        assertSame( "option2", option, page.getHtmlElement( option.getElement() ) );

        final HtmlTable table = ( HtmlTable )page.getHtmlElementById( "table1" );
        assertSame( "table1", table, page.getHtmlElementById( "table1" ) );
        assertSame( "table2", table, page.getHtmlElement( table.getElement() ) );

        final HtmlAnchor anchor = page.getAnchorByName( "anchor1" );
        assertSame( "anchor1", anchor, page.getAnchorByName( "anchor1" ) );
        assertSame( "anchor2", anchor, page.getHtmlElement( anchor.getElement() ) );
        assertSame( "anchor3", anchor, page.getAnchorByHref("http://www.foo.com") );
        assertSame( "anchor4", anchor, page.getFirstAnchorByText("foo.com") );

        final HtmlTableRow tableRow = ( HtmlTableRow )table.getRows().get( 0 );
        assertSame( "tableRow1", tableRow, table.getRows().get( 0 ) );
        assertSame( "tableRow2", tableRow, page.getHtmlElement( tableRow.getElement() ) );

        final HtmlTableHeaderCell tableHeaderCell
            = ( HtmlTableHeaderCell )tableRow.getCells().get( 0 );
        assertSame( "tableHeaderCell1", tableHeaderCell, tableRow.getCells().get( 0 ) );
        assertSame( "tableHeaderCell2", tableHeaderCell, page.getHtmlElementById( "header1" ) );
        assertSame( "tableHeaderCell3", tableHeaderCell,
            page.getHtmlElement( tableHeaderCell.getElement() ) );

        final HtmlTableDataCell tableDataCell = ( HtmlTableDataCell )tableRow.getCells().get( 1 );
        assertSame( "tableDataCell1", tableDataCell, tableRow.getCells().get( 1 ) );
        assertSame( "tableDataCell2", tableDataCell, page.getHtmlElementById( "data1" ) );
        assertSame( "tableDataCell3", tableDataCell,
            page.getHtmlElement( tableDataCell.getElement() ) );

        final HtmlTextArea textArea
            = ( HtmlTextArea )form.getTextAreasByName( "textArea1" ).get( 0 );
        assertSame( "textArea1", textArea, form.getTextAreasByName( "textArea1" ).get( 0 ) );
        assertSame( "textArea2", textArea, page.getHtmlElement( textArea.getElement() ) );
    }


    public void testGetTabbableElements_None()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<p>hello world</p>"
                 + "<table><tr><td>foo</td></tr></table>"
                 + "</body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( Collections.EMPTY_LIST, page.getTabbableElements() );
    }


    public void testGetTabbableElements_OneEnabled_OneDisabled()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<form><p>hello world</p>"
                 + "<input name='foo' type='submit' disabled='disabled' id='foo'/>"
                 + "<input name='bar' type='submit' id='bar'/>"
                 + "</form></body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        final List expectedElements = new ArrayList();
        expectedElements.add( page.getHtmlElementById( "bar" ) );

        assertEquals( expectedElements, page.getTabbableElements() );
    }


    public void testGetTabbableElements()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<a id='a' tabindex='1'>foo</a>"
                 + "<a id='b'>foo</a>"
                 + "<form>"
                 + "<a id='c' tabindex='3'>foo</a>"
                 + "<a id='d' tabindex='2'>foo</a>"
                 + "<a id='e' tabindex='0'>foo</a>"
                 + "</form>"
                 + "<a id='f' tabindex='3'>foo</a>"
                 + "<a id='g' tabindex='1'>foo</a>"
                 + "<a id='q' tabindex='-1'>foo</a>"
                 + "<form><p>hello world</p>"
                 + "<input name='foo' type='submit' disabled='disabled' id='foo'/>"
                 + "<input name='bar' type='submit' id='bar'/>"
                 + "</form></body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        final List expectedElements = Arrays.asList( new Object[]{
                page.getHtmlElementById( "a" ),
                page.getHtmlElementById( "g" ),
                page.getHtmlElementById( "d" ),
                page.getHtmlElementById( "c" ),
                page.getHtmlElementById( "f" ),
                page.getHtmlElementById( "e" ),
                page.getHtmlElementById( "b" ),
                page.getHtmlElementById( "bar" )} );

        assertEquals( expectedElements, page.getTabbableElements() );

        final List expectedIds = Arrays.asList( new String[]{
                "a", "g", "d", "c", "f", "e", "b", "bar"} );
        assertEquals( expectedIds, page.getTabbableElementIds() );
    }


    public void testGetHtmlElementByAccessKey()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<a id='a' accesskey='a'>foo</a>"
                 + "<a id='b'>foo</a>"
                 + "<form>"
                 + "<a id='c' accesskey='c'>foo</a>"
                 + "</form>"
                 + "<form><p>hello world</p>"
                 + "<input name='foo' type='submit' disabled='disabled' id='foo' accesskey='f'/>"
                 + "<input name='bar' type='submit' id='bar'/>"
                 + "</form></body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        assertEquals( page.getHtmlElementById( "a" ), page.getHtmlElementByAccessKey( 'A' ) );
        assertEquals( page.getHtmlElementById( "c" ), page.getHtmlElementByAccessKey( 'c' ) );
        assertNull( page.getHtmlElementByAccessKey( 'z' ) );
    }


    public void testGetHtmlElementsByAccessKey()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head><body>"
                 + "<a id='a' accesskey='a'>foo</a>"
                 + "<a id='b' accesskey='a'>foo</a>"
                 + "<form>"
                 + "<a id='c' accesskey='c'>foo</a>"
                 + "</form></body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        final List expectedElements = Arrays.asList( new Object[]{
            page.getHtmlElementById( "a" ), page.getHtmlElementById( "b" ) } );
        final List collectedElements = page.getHtmlElementsByAccessKey('a');
        assertEquals( expectedElements, collectedElements );
    }


    public void testAssertAllIdAttributesUnique()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<a id='a' accesskey='a'>foo</a>"
                 + "<a id='b'>foo</a>"
                 + "<form>"
                 + "<a id='c' accesskey='c'>foo</a>"
                 + "</form>"
                 + "<form><p>hello world</p>"
                 + "<input name='foo' type='submit' disabled='disabled' id='foo' accesskey='f'/>"
                 + "<input name='bar' type='submit' id='bar'/>"
                 + "</form></body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        page.assertAllIdAttributesUnique();
    }


    public void testAssertAllIdAttributesUnique_Duplicates()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<a id='a' accesskey='a'>foo</a>"
                 + "<a id='b'>foo</a>"
                 + "<form>"
                 + "<a id='c' accesskey='c'>foo</a>"
                 + "</form>"
                 + "<form><p>hello world</p>"
                 + "<input name='foo' type='submit' disabled='disabled' id='a' accesskey='f'/>"
                 + "<input name='bar' type='submit' id='bar'/>"
                 + "</form></body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        try {
            page.assertAllIdAttributesUnique();
        }
        catch( final AssertionFailedError e ) {
            return;
        }

        fail( "Expected AssertionFailedError" );
    }


    public void testAssertAllAccessKeyAttributesUnique()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<a id='a' accesskey='a'>foo</a>"
                 + "<a id='b'>foo</a>"
                 + "<form>"
                 + "<a id='c' accesskey='c'>foo</a>"
                 + "</form>"
                 + "<form><p>hello world</p>"
                 + "<input name='foo' type='submit' disabled='disabled' id='foo' accesskey='f'/>"
                 + "<input name='bar' type='submit' id='bar'/>"
                 + "</form></body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        page.assertAllAccessKeyAttributesUnique();
    }


    public void testAssertAllAccessKeyAttributesUnique_Duplicates()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<a id='a' accesskey='a'>foo</a>"
                 + "<a id='b'>foo</a>"
                 + "<form>"
                 + "<a id='c' accesskey='c'>foo</a>"
                 + "</form>"
                 + "<form><p>hello world</p>"
                 + "<input name='foo' type='submit' disabled='disabled' id='foo' accesskey='f'/>"
                 + "<input name='bar' type='submit' id='bar' accesskey='a'/>"
                 + "</form></body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        try {
            page.assertAllAccessKeyAttributesUnique();
        }
        catch( final AssertionFailedError e ) {
            return;
        }

        fail( "Expected AssertionFailedError" );
    }


    public void testAssertAllTabIndexAttributesSet()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<a id='a' tabindex='1'>foo</a>"
                 + "<form>"
                 + "<a id='c' tabindex='3'>foo</a>"
                 + "<a id='d' tabindex='2'>foo</a>"
                 + "<a id='e' tabindex='0'>foo</a>"
                 + "</form>"
                 + "<a id='f' tabindex='3'>foo</a>"
                 + "<a id='g' tabindex='1'>foo</a>"
                 + "<a id='q' tabindex='5'>foo</a>"
                 + "<form><p>hello world</p>"
                 + "</form></body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        page.assertAllTabIndexAttributesSet();
    }


    public void testAssertAllTabIndexAttributesSet_SomeMissing()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<a id='a' tabindex='1'>foo</a>"
                 + "<form>"
                 + "<a id='c' tabindex='3'>foo</a>"
                 + "<a id='d' tabindex='2'>foo</a>"
                 + "<a id='e' tabindex='0'>foo</a>"
                 + "</form>"
                 + "<a id='f' tabindex='3'>foo</a>"
                 + "<a id='g'>foo</a>"
                 + "<a id='q' tabindex='1'>foo</a>"
                 + "<form><p>hello world</p>"
                 + "</form></body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        try {
            page.assertAllTabIndexAttributesSet();
        }
        catch( final AssertionFailedError e ) {
            return;
        }

        fail( "Expected AssertionFailedError" );
    }


    public void testAssertAllTabIndexAttributesSet_BadValue()
        throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<a id='a' tabindex='1'>foo</a>"
                 + "<form>"
                 + "<a id='c' tabindex='3'>foo</a>"
                 + "<a id='d' tabindex='2'>foo</a>"
                 + "<a id='e' tabindex='0'>foo</a>"
                 + "</form>"
                 + "<a id='f' tabindex='3'>foo</a>"
                 + "<a id='g' tabindex='1'>foo</a>"
                 + "<a id='q' tabindex='300000'>foo</a>"
                 + "<form><p>hello world</p>"
                 + "</form></body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        try {
            page.assertAllTabIndexAttributesSet();
        }
        catch( final AssertionFailedError e ) {
            return;
        }

        fail( "Expected AssertionFailedError" );
    }


    public void testGetFullQualifiedUrl_NoBaseSpecified() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<table><tr><td><input type='text' id='foo'/></td></tr></table>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final String urlString = "http://www.gargoylesoftware.com/";
        final HtmlPage page =
            ( HtmlPage )client.getPage( new URL( urlString ),
            SubmitMethod.POST, Collections.EMPTY_LIST );

        assertEquals( urlString, page.getFullyQualifiedUrl("").toExternalForm() );
        assertEquals( urlString+"foo", page.getFullyQualifiedUrl("foo").toExternalForm() );
        assertEquals( "http://foo.com/bar",
            page.getFullyQualifiedUrl("http://foo.com/bar").toExternalForm() );
        assertEquals( "mailto:me@foo.com",
            page.getFullyQualifiedUrl("mailto:me@foo.com").toExternalForm() );

        assertEquals( urlString+"foo", page.getFullyQualifiedUrl("foo").toExternalForm() );
        assertEquals( urlString+"bbb", page.getFullyQualifiedUrl("aaa/../bbb").toExternalForm() );
        assertEquals( urlString+"c/d", page.getFullyQualifiedUrl("c/./d").toExternalForm() );

        final HtmlPage secondPage = ( HtmlPage )client.getPage(
            new URL( urlString + "/foo/bar?a=b&c=d" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( urlString+"foo/bar", secondPage.getFullyQualifiedUrl("").toExternalForm() );
        assertEquals( urlString+"foo/one",
            secondPage.getFullyQualifiedUrl("one").toExternalForm() );
        assertEquals( urlString+"two", secondPage.getFullyQualifiedUrl("/two").toExternalForm() );
        assertEquals( urlString+"foo/two?a=b",
            secondPage.getFullyQualifiedUrl("two?a=b").toExternalForm() );

        final HtmlPage thirdPage = ( HtmlPage )client.getPage(
            new URL( "http://foo.com/dog/cat/one.html" ),
            SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals( "http://foo.com/dog/cat/one.html",
            thirdPage.getFullyQualifiedUrl("").toExternalForm() );
        assertEquals( "http://foo.com/dog/cat/two.html",
            thirdPage.getFullyQualifiedUrl("two.html").toExternalForm() );
    }


    public void testGetFullQualifiedUrl_WithBase() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title><base href='http://second'></head><body>"
                 + "<form id='form1'>"
                 + "<table><tr><td><input type='text' id='foo'/></td></tr></table>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final String urlString = "http://first/";
        final HtmlPage page =
            ( HtmlPage )client.getPage( new URL( urlString ),
            SubmitMethod.POST, Collections.EMPTY_LIST );

        assertEquals( "http://second/", page.getFullyQualifiedUrl("").toExternalForm() );
        assertEquals( "http://second/foo", page.getFullyQualifiedUrl("foo").toExternalForm() );
        assertEquals( "http://foo.com/bar",
            page.getFullyQualifiedUrl("http://foo.com/bar").toExternalForm() );
        assertEquals( "mailto:me@foo.com",
            page.getFullyQualifiedUrl("mailto:me@foo.com").toExternalForm() );

        assertEquals( "http://second/foo", page.getFullyQualifiedUrl("foo").toExternalForm() );
        assertEquals( "http://second/bbb", page.getFullyQualifiedUrl("aaa/../bbb").toExternalForm() );
        assertEquals( "http://second/c/d", page.getFullyQualifiedUrl("c/./d").toExternalForm() );
    }


    public void testOnLoadHandler_BodyStatement() throws Exception {
        final String content
                 = "<html><head><title>foo</title>"
                 + "</head><body onLoad='alert(\"foo\")'>"
                 + "</body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "foo"
        });
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for bug 713646
     */
    public void testOnLoadHandler_BodyName() throws Exception {
        final String content
                 = "<html><head><title>foo</title>"
                 + "<script type='text/javascript'>"
                 + "load=function(){alert('foo')}</script>"
                 + "</head><body onLoad='load'></body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "foo"
        });
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Regression test for bug 713646
     */
    public void testOnLoadHandler_BodyName_NotAFunction() throws Exception {
        final String content =
            "<html><head><title>foo</title></head>"
                + "<body onLoad='foo=4711'>"
                + "<a name='alert' href='javascript:alert(foo)'/>"
                + "</body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());
        
        page.getAnchorByName("alert").click();

        final List expectedAlerts = Arrays.asList(new String[] { "4711" });
        assertEquals(expectedAlerts, collectedAlerts);
    }


    /**
     * Regression test for window.onload property
     */
    public void testOnLoadHandler_BodyNameRead() throws Exception {
        final String content
                 = "<html><head><title>foo</title>"
                 + "<script type='text/javascript'>"
                 + "load=function(){}</script>"
                 + "</head><body onLoad='load'>"
                 + "<script type='text/javascript'>\n"
                 + "alert(onload);\n"
                 + "</script></body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "load"
        });
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for window.onload property
     */
    public void testOnLoadHandler_ScriptName() throws Exception {
        final String content
                 = "<html><head><title>foo</title>"
                 + "<script type='text/javascript'>\n"
                 + "load=function(){alert('foo')};\n"
                 + "onload=load\n"
                 + "</script></head><body></body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "foo"
        });
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Regression test for window.onload property
     */
    public void testOnLoadHandler_ScriptNameRead() throws Exception {
        final String content
                 = "<html><head><title>foo</title>"
                 + "<script type='text/javascript'>\n"
                 + "load=function(){};\n"
                 + "onload=load;\n"
                 + "alert(onload);\n"
                 + "</script></head><body></body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "\nfunction () {\n}\n"
        });
        assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testEmbeddedMetaTag_Regression() throws Exception {

        final String content
                 = "<html><head><title>foo</title>"
                 + "</head><body>"
                 + "<table><tr><td>\n"
                 + "<meta name=vs_targetSchema content=\"http://schemas.microsoft.com/intellisense/ie5\">"
                 + "<form name='form1'>"
                 + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
                 + "    <input type='text' name='textfield2' id='textfield2'/>"
                 + "</form>"
                 + "</td></tr></table>"
                 + "</body></html>";
        final List collectedAlerts = new ArrayList();

        // This used to blow up on page load
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Collections.EMPTY_LIST;
        assertEquals( expectedAlerts, collectedAlerts );
    }

    public void testGetPageEncoding() throws Exception {

        final String content
                 = "<html><head>"
                 + "<title>foo</title>"
                 + "<meta http-equiv='Content-Type' content='text/html ;charset=Shift_JIS'>"
                 + "</head><body>"
                 + "<table><tr><td>\n"
                 + "<meta name=vs_targetSchema content=\"http://schemas.microsoft.com/intellisense/ie5\">"
                 + "<form name='form1'>"
                 + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
                 + "    <input type='text' name='textfield2' id='textfield2'/>"
                 + "</form>"
                 + "</td></tr></table>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://justSJIS"), content,
            200, "OK", "text/html", Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );
        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://justSJIS" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        assertEquals("Shift_JIS", page.getPageEncoding());
    }


    public void testGetAllForms() throws Exception {

        final String content
                 = "<html>"
                 + "<head><title>foo</title></head>"
                 + "<body>"
                 + "<form name='one'>"
                 + "<a id='c' accesskey='c'>foo</a>"
                 + "</form>"
                 + "<form name='two'>"
                 + "<a id='c' accesskey='c'>foo</a>"
                 + "</form>"
                 + "<input name='foo' type='submit' disabled='disabled' id='foo' accesskey='f'/>"
                 + "<input name='bar' type='submit' id='bar'/>"
                 + "</form></body></html>";

        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        final List expectedForms = Arrays.asList( new HtmlForm[]{ 
            page.getFormByName("one"),
            page.getFormByName("two")
        } );
        assertEquals( expectedForms, page.getAllForms() );
    }
}

