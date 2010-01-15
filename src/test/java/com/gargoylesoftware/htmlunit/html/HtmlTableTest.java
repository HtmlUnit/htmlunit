/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlTable}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HtmlTableTest extends WebTestCase {

    /**
     * Tests getTableCell(int,int).
     * @exception Exception If the test fails
     */
    @Test
    public void testGetTableCell() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<table id='table1' summary='Test table'>\n"
            + "<tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>\n"
            + "<tr><td colspan='2'>cell3</td></tr>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTable table = page.getHtmlElementById("table1");

        final HtmlTableCell cell1 = table.getCellAt(0, 0);
        Assert.assertEquals("cell1 contents", "cell1", cell1.asText());

        final HtmlTableCell cell2 = table.getCellAt(0, 1);
        Assert.assertEquals("cell2 contents", "cell2", cell2.asText());

        final HtmlTableCell cell3 = table.getCellAt(1, 0);
        Assert.assertEquals("cell3 contents", "cell3", cell3.asText());
        assertSame("cells (1,0) and (1,1)", cell3, table.getCellAt(1, 1));

        final HtmlTableCell cell4 = table.getCellAt(0, 2);
        Assert.assertEquals("cell4 contents", "cell4", cell4.asText());
        assertSame("cells (0,2) and (1,2)", cell4, table.getCellAt(1, 2));
    }

    /**
     * Tests getCellAt(int,int) with colspan.
     * @exception Exception If the test fails
     */
    @Test
    public void testGetCellAt() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<table id='table1'>\n"
            + "<tr><td>row 1 col 1</td></tr>\n"
            + "<tr><td>row 2 col 1</td><td>row 2 col 2</td></tr>\n"
            + "<tr><td colspan='1'>row 3 col 1&2</td></tr>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTable table = page.getHtmlElementById("table1");

        final HtmlTableCell cell1 = table.getCellAt(0, 0);
        Assert.assertEquals("cell (0,0) contents", "row 1 col 1", cell1.asText());

        final HtmlTableCell cell2 = table.getCellAt(0, 1);
        Assert.assertEquals("cell (0,1) contents", null, cell2);

        final HtmlTableCell cell3 = table.getCellAt(1, 0);
        Assert.assertEquals("cell (1,0) contents", "row 2 col 1", cell3.asText());

        final HtmlTableCell cell4 = table.getCellAt(1, 1);
        Assert.assertEquals("cell (1,1) contents", "row 2 col 2", cell4.asText());

        final HtmlTableCell cell5 = table.getCellAt(2, 0);
        Assert.assertEquals("cell (2, 0) contents", "row 3 col 1&2", cell5.asText());
        final HtmlTableCell cell6 = table.getCellAt(2, 1);
        Assert.assertEquals("cell (2, 1) contents", null, cell6);
    }

    /**
     * Tests getTableCell(int,int) for a cell that doesn't exist.
     * @exception Exception If the test fails
     */
    @Test
    public void testGetTableCell_NotFound() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<table id='table1' summary='Test table'>\n"
            + "<tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>\n"
            + "<tr><td colspan='2'>cell3</td></tr>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTable table = page.getHtmlElementById("table1");

        final HtmlTableCell cell = table.getCellAt(99, 0);
        Assert.assertNull("cell", cell);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetTableRows() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<table id='table1'>\n"
            + "<tr id='row1'><td>cell1</td></tr>\n"
            + "<tr id='row2'><td>cell2</td></tr>\n"
            + "<tr id='row3'><td>cell3</td></tr>\n"
            + "<tr id='row4'><td>cell4</td></tr>\n"
            + "<tr id='row5'><td>cell5</td></tr>\n"
            + "<tr id='row6'><td>cell6</td></tr>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTable table = page.getHtmlElementById("table1");

        final List<HtmlTableRow> expectedRows = new ArrayList<HtmlTableRow>();
        expectedRows.add(table.getRowById("row1"));
        expectedRows.add(table.getRowById("row2"));
        expectedRows.add(table.getRowById("row3"));
        expectedRows.add(table.getRowById("row4"));
        expectedRows.add(table.getRowById("row5"));
        expectedRows.add(table.getRowById("row6"));

        assertEquals(expectedRows, table.getRows());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetTableRows_WithHeadBodyFoot() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<table id='table1'>\n"
            + "<thead>\n"
            + "    <tr id='row1'><td>cell1</td></tr>\n"
            + "    <tr id='row2'><td>cell2</td></tr>\n"
            + "</thead>\n"
            + "<tbody>\n"
            + "    <tr id='row3'><td>cell3</td></tr>\n"
            + "    <tr id='row4'><td>cell4</td></tr>\n"
            + "</tbody>\n"
            + "<tfoot>\n"
            + "    <tr id='row5'><td>cell5</td></tr>\n"
            + "    <tr id='row6'><td>cell6</td></tr>\n"
            + "</tfoot>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTable table = page.getHtmlElementById("table1");

        final List<HtmlTableRow> expectedRows = new ArrayList<HtmlTableRow>();
        expectedRows.add(table.getRowById("row1"));
        expectedRows.add(table.getRowById("row2"));
        expectedRows.add(table.getRowById("row3"));
        expectedRows.add(table.getRowById("row4"));
        expectedRows.add(table.getRowById("row5"));
        expectedRows.add(table.getRowById("row6"));

        assertEquals(expectedRows, table.getRows());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testRowGroupings_AllDefined() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<table id='table1'>\n"
            + "<thead>\n"
            + "    <tr id='row1'><td>cell1</td></tr>\n"
            + "    <tr id='row2'><td>cell2</td></tr>\n"
            + "</thead>\n"
            + "<tbody>\n"
            + "    <tr id='row3'><td>cell3</td></tr>\n"
            + "</tbody>\n"
            + "<tbody>\n"
            + "    <tr id='row4'><td>cell4</td></tr>\n"
            + "</tbody>\n"
            + "<tfoot>\n"
            + "    <tr id='row5'><td>cell5</td></tr>\n"
            + "    <tr id='row6'><td>cell6</td></tr>\n"
            + "</tfoot>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTable table = page.getHtmlElementById("table1");

        assertNotNull(table.getHeader());
        assertNotNull(table.getFooter());
        assertEquals(2, table.getBodies().size());
    }

    /**
     * Check to ensure that the proper numbers of tags show up. Note that an extra tbody
     * will be inserted to be in compliance with the common browsers.
     * @throws Exception if the test fails
     */
    @Test
    public void testRowGroupings_NoneDefined()
        throws Exception {

        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<table id='table1'>\n"
            + "    <tr id='row1'><td>cell1</td></tr>\n"
            + "    <tr id='row2'><td>cell2</td></tr>\n"
            + "    <tr id='row3'><td>cell3</td></tr>\n"
            + "    <tr id='row4'><td>cell4</td></tr>\n"
            + "    <tr id='row5'><td>cell5</td></tr>\n"
            + "    <tr id='row6'><td>cell6</td></tr>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTable table = page.getHtmlElementById("table1");

        assertEquals(null, table.getHeader());
        assertEquals(null, table.getFooter());
        assertEquals(1, table.getBodies().size());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetCaptionText() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<table id='table1' summary='Test table'>\n"
            + "<caption>MyCaption</caption>\n"
            + "<tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>\n"
            + "<tr><td colspan='2'>cell3</td></tr>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTable table = page.getHtmlElementById("table1");

        assertEquals("MyCaption", table.getCaptionText());
    }

    /**
     * The common browsers will automatically insert tbody tags around the table rows if
     * one wasn't specified. Ensure that we do this too. Also ensure that extra ones
     * aren't inserted if a tbody was already there.
     * @throws Exception if the test fails
     */
    @Test
    public void testInsertionOfTbodyTags() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<table>\n"
            + "<tr><td id='cell1'>cell1</td></tr>\n"
            + "</table>\n"
            + "<table><tbody>\n"
            + "<tr><td id='cell2'>cell1</td></tr>\n"
            + "</tbody></table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        // Check that a <tbody> was inserted properly
        final HtmlTableDataCell cell1 = page.getHtmlElementById("cell1");
        assertTrue(HtmlTableRow.class.isInstance(cell1.getParentNode()));
        assertTrue(HtmlTableBody.class.isInstance(cell1.getParentNode().getParentNode()));
        assertTrue(HtmlTable.class.isInstance(cell1.getParentNode().getParentNode().getParentNode()));

        // Check that the existing <tbody> wasn't messed up.
        final HtmlTableDataCell cell2 = page.getHtmlElementById("cell2");
        assertTrue(HtmlTableRow.class.isInstance(cell2.getParentNode()));
        assertTrue(HtmlTableBody.class.isInstance(cell2.getParentNode().getParentNode()));
        assertTrue(HtmlTable.class.isInstance(cell2.getParentNode().getParentNode().getParentNode()));
    }

    /**
     * Regression test for bug 1210751: JavaScript inside <tt>&lt;table&gt;</tt> run twice.
     * @throws Exception if the test fails
     */
    @Test
    public void testJSInTable() throws Exception {
        final String content
            = "<html><head><title>foo</title></head><body>\n"
            + "<table>\n"
            + "<tr><td>cell1</td></tr>\n"
            + "<script>alert('foo');</script>\n"
            + "<tr><td>cell1</td></tr>\n"
            + "</table>\n"
            + "<div id='div1'>foo</div>\n"
            + "<script>alert(document.getElementById('div1').parentNode.tagName);</script>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"foo", "BODY"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSimpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <table id='myId'/>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object HTMLTableElement]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_3, html, collectedAlerts);
        assertTrue(HtmlTable.class.isInstance(page.getHtmlElementById("myId")));
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String html = "<html><head>\n"
            + "</head><body>\n"
            + "  <table id='myId'>\n"
            + "  <caption>This is the caption</caption>\n"
            + "  <tr>\n"
            + "  <td>cell 1,1</td>\n"
            + "  <td>cell 1,2</td>\n"
            + "  </tr>\n"
            + "  <tr>\n"
            + "  <td>cell 2,1</td>\n"
            + "  <td>cell 2,2</td>\n"
            + "  </tr>\n"
            + "  </table>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlElement table = page.getHtmlElementById("myId");
        final String expectedText = "This is the caption" + LINE_SEPARATOR
            + "cell 1,1\tcell 1,2" + LINE_SEPARATOR
            + "cell 2,1\tcell 2,2";

        assertEquals(expectedText, table.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml_emptyTable() throws Exception {
        final String html = "<html>\n"
            + "<head/>\n"
            + "<body>\n"
            + "<div style=\"visibility: hidden\">\n"
            + "<table>\n"
            + "</table>\n"
            + "</div>\n"
            + "after the div\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);
        assertTrue(page.asXml().contains("</table>"));
    }
}
