/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html.test;

import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Tests for HtmlTable
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlTableTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlTableTest( final String name ) {
        super( name );
    }


    /**
     *  Test getTableCell(int,int)
     *
     * @exception  Exception If the test fails
     */
    public void testGetTableCell()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<table id='table1' summary='Test table'>"
                 + "<tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>"
                 + "<tr><td colspan='2'>cell3</td></tr>"
                 + "</table>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlTable table = ( HtmlTable )page.getHtmlElementById( "table1" );

        final HtmlTableCell cell1 = table.getCellAt( 0, 0 );
        assertEquals( "cell1 contents", "cell1", cell1.asText() );

        final HtmlTableCell cell2 = table.getCellAt( 0, 1 );
        assertEquals( "cell2 contents", "cell2", cell2.asText() );

        final HtmlTableCell cell3 = table.getCellAt( 1, 0 );
        assertEquals( "cell3 contents", "cell3", cell3.asText() );
        assertSame( "cells (1,0) and (1,1)", cell3, table.getCellAt( 1, 1 ) );

        final HtmlTableCell cell4 = table.getCellAt( 0, 2 );
        assertEquals( "cell4 contents", "cell4", cell4.asText() );
        assertSame( "cells (0,2) and (1,2)", cell4, table.getCellAt( 1, 2 ) );
    }


    /**
     *  Test getTableCell(int,int) for a cell that doesn't exist
     *
     * @exception  Exception If the test fails
     */
    public void testGetTableCell_NotFound()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<table id='table1' summary='Test table'>"
                 + "<tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>"
                 + "<tr><td colspan='2'>cell3</td></tr>"
                 + "</table>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlTable table = ( HtmlTable )page.getHtmlElementById( "table1" );

        final HtmlTableCell cell = table.getCellAt( 99, 0 );
        assertNull( "cell", cell );
    }


    public void testGetTableRows()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<table id='table1'>"
                 + "<tr id='row1'><td>cell1</td></tr>"
                 + "<tr id='row2'><td>cell2</td></tr>"
                 + "<tr id='row3'><td>cell3</td></tr>"
                 + "<tr id='row4'><td>cell4</td></tr>"
                 + "<tr id='row5'><td>cell5</td></tr>"
                 + "<tr id='row6'><td>cell6</td></tr>"
                 + "</table>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlTable table = ( HtmlTable )page.getHtmlElementById( "table1" );

        final List expectedRows = new ArrayList();
        expectedRows.add( table.getRowById( "row1" ) );
        expectedRows.add( table.getRowById( "row2" ) );
        expectedRows.add( table.getRowById( "row3" ) );
        expectedRows.add( table.getRowById( "row4" ) );
        expectedRows.add( table.getRowById( "row5" ) );
        expectedRows.add( table.getRowById( "row6" ) );

        assertEquals( expectedRows, table.getRows() );
    }


    public void testGetTableRows_WithHeadBodyFoot()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<table id='table1'>"
                 + "<thead>"
                 + "    <tr id='row1'><td>cell1</td></tr>"
                 + "    <tr id='row2'><td>cell2</td></tr>"
                 + "</thead>"
                 + "<tbody>"
                 + "    <tr id='row3'><td>cell3</td></tr>"
                 + "    <tr id='row4'><td>cell4</td></tr>"
                 + "</tbody>"
                 + "<tfoot>"
                 + "    <tr id='row5'><td>cell5</td></tr>"
                 + "    <tr id='row6'><td>cell6</td></tr>"
                 + "</tfoot>"
                 + "</table>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlTable table = ( HtmlTable )page.getHtmlElementById( "table1" );

        final List expectedRows = new ArrayList();
        expectedRows.add( table.getRowById( "row1" ) );
        expectedRows.add( table.getRowById( "row2" ) );
        expectedRows.add( table.getRowById( "row3" ) );
        expectedRows.add( table.getRowById( "row4" ) );
        expectedRows.add( table.getRowById( "row5" ) );
        expectedRows.add( table.getRowById( "row6" ) );

        assertEquals( expectedRows, table.getRows() );
    }


    public void testRowGroupings_AllDefined()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<table id='table1'>"
                 + "<thead>"
                 + "    <tr id='row1'><td>cell1</td></tr>"
                 + "    <tr id='row2'><td>cell2</td></tr>"
                 + "</thead>"
                 + "<tbody>"
                 + "    <tr id='row3'><td>cell3</td></tr>"
                 + "</tbody>"
                 + "<tbody>"
                 + "    <tr id='row4'><td>cell4</td></tr>"
                 + "</tbody>"
                 + "<tfoot>"
                 + "    <tr id='row5'><td>cell5</td></tr>"
                 + "    <tr id='row6'><td>cell6</td></tr>"
                 + "</tfoot>"
                 + "</table>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlTable table = ( HtmlTable )page.getHtmlElementById( "table1" );

        assertNotNull( table.getHeader() );
        assertNotNull( table.getFooter() );
        assertEquals( 2, table.getBodies().size() );
    }


    public void testRowGroupings_NoneDefined()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<table id='table1'>"
                 + "    <tr id='row1'><td>cell1</td></tr>"
                 + "    <tr id='row2'><td>cell2</td></tr>"
                 + "    <tr id='row3'><td>cell3</td></tr>"
                 + "    <tr id='row4'><td>cell4</td></tr>"
                 + "    <tr id='row5'><td>cell5</td></tr>"
                 + "    <tr id='row6'><td>cell6</td></tr>"
                 + "</table>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlTable table = ( HtmlTable )page.getHtmlElementById( "table1" );

        assertEquals( null, table.getHeader() );
        assertEquals( null, table.getFooter() );
        assertEquals( 0, table.getBodies().size() );
    }


    public void testGetCaptionText()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<table id='table1' summary='Test table'>"
                 + "<caption>MyCaption</caption>"
                 + "<tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>"
                 + "<tr><td colspan='2'>cell3</td></tr>"
                 + "</table>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlTable table = ( HtmlTable )page.getHtmlElementById( "table1" );

        assertEquals("MyCaption", table.getCaptionText());
    }
}

