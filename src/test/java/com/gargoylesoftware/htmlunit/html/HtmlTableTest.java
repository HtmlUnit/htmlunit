/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlTable}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlTableTest extends WebTestCase {

    /**
     * Tests getTableCell(int,int).
     * @exception Exception If the test fails
     */
    @Test
    public void getCellAt() throws Exception {
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
    public void getCellAtColspan() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<table id='table1'>\n"
            + "<tr>\n"
            + "  <td>row 1 col 1</td>\n"
            + "</tr>\n"
            + "<tr>\n"
            + "  <td>row 2 col 1</td><td>row 2 col 2</td>\n"
            + "</tr>\n"
            + "<tr>\n"
            + "  <td colspan='1'>row 3 col 1&2</td>\n"
            + "</tr>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTable table = page.getHtmlElementById("table1");

        final HtmlTableCell cell1 = table.getCellAt(0, 0);
        Assert.assertEquals("cell (0,0) contents", "row 1 col 1", cell1.asText());

        final HtmlTableCell cell2 = table.getCellAt(0, 1);
        Assert.assertNull("cell (0,1) contents", cell2);

        final HtmlTableCell cell3 = table.getCellAt(1, 0);
        Assert.assertEquals("cell (1,0) contents", "row 2 col 1", cell3.asText());

        final HtmlTableCell cell4 = table.getCellAt(1, 1);
        Assert.assertEquals("cell (1,1) contents", "row 2 col 2", cell4.asText());

        final HtmlTableCell cell5 = table.getCellAt(2, 0);
        Assert.assertEquals("cell (2, 0) contents", "row 3 col 1&2", cell5.asText());
        final HtmlTableCell cell6 = table.getCellAt(2, 1);
        Assert.assertNull("cell (2, 1) contents", cell6);
    }

    /**
     * Tests getCellAt(int,int).
     * @exception Exception If the test fails
     */
    @Test
    public void getCellAtComplex() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<table id='table1' border='1'>\n"
            + "  <tr>\n"
            + "    <th colspan='1'>H 1.1</th><th>H 1.2</th>\n"
            + "    <th colspan='2' rowspan='2'>H 1.3</th><th>H 1.5</th>\n"
            + "  </tr>\n"
            + "  <tr>\n"
            + "    <th>H 2.1</th><th>H 2.2</th><th>H 2.5</th>\n"
            + "  </tr>\n"
            + "  <tr>\n"
            + "    <td rowspan='3'>1.1</td><td colspan='3'>1.2</td><td>1.5</td>\n"
            + "  </tr>\n"
            + "  <tr>\n"
            + "    <td rowspan='2'>2.2</td><td>2.3</td><td rowspan='4' colspan='2'>2.4</td>\n"
            + "  </tr>\n"
            + "  <tr>\n"
            + "    <td>3.3</td>\n"
            + "  </tr>\n"
            + "  <tr>\n"
            + "    <td>4.1</td><td>4.2</td><td>4.3</td>\n"
            + "  </tr>\n"
            + "  <tr>\n"
            + "    <td>5.1</td>\n"
            + "    <td colspan='2' rowspan='2'>5.2</td>\n"
            + "  </tr>\n"
            + "  <tr>\n"
            + "    <td>6.1</td><td>6.4</td><td>6.5</td>\n"
            + "  </tr>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTable table = page.getHtmlElementById("table1");

        // first row
        HtmlTableCell cell = table.getCellAt(0, 0);
        Assert.assertEquals("cell (0,0) contents", "H 1.1", cell.asText());
        cell = table.getCellAt(0, 1);
        Assert.assertEquals("cell (0,1) contents", "H 1.2", cell.asText());
        cell = table.getCellAt(0, 2);
        Assert.assertEquals("cell (0,2) contents", "H 1.3", cell.asText());
        cell = table.getCellAt(0, 3);
        Assert.assertEquals("cell (0,3) contents", "H 1.3", cell.asText());
        cell = table.getCellAt(0, 4);
        Assert.assertEquals("cell (0,4) contents", "H 1.5", cell.asText());
        cell = table.getCellAt(0, 5);
        Assert.assertNull("cell (0,5) contents", cell);

        // second row
        cell = table.getCellAt(1, 0);
        Assert.assertEquals("cell (1,0) contents", "H 2.1", cell.asText());
        cell = table.getCellAt(1, 1);
        Assert.assertEquals("cell (1,1) contents", "H 2.2", cell.asText());
        cell = table.getCellAt(1, 2);
        Assert.assertEquals("cell (1,2) contents", "H 1.3", cell.asText());
        cell = table.getCellAt(1, 3);
        Assert.assertEquals("cell (1,3) contents", "H 1.3", cell.asText());
        cell = table.getCellAt(1, 4);
        Assert.assertEquals("cell (1,4) contents", "H 2.5", cell.asText());
        cell = table.getCellAt(1, 5);
        Assert.assertNull("cell (0,5) contents", cell);

        // third row
        cell = table.getCellAt(2, 0);
        Assert.assertEquals("cell (2,0) contents", "1.1", cell.asText());
        cell = table.getCellAt(2, 1);
        Assert.assertEquals("cell (2,1) contents", "1.2", cell.asText());
        cell = table.getCellAt(2, 2);
        Assert.assertEquals("cell (2,2) contents", "1.2", cell.asText());
        cell = table.getCellAt(2, 3);
        Assert.assertEquals("cell (2,3) contents", "1.2", cell.asText());
        cell = table.getCellAt(2, 4);
        Assert.assertEquals("cell (2,4) contents", "1.5", cell.asText());
        cell = table.getCellAt(2, 5);
        Assert.assertNull("cell (2,5) contents", cell);

        // fourth row
        cell = table.getCellAt(3, 0);
        Assert.assertEquals("cell (3,0) contents", "1.1", cell.asText());
        cell = table.getCellAt(3, 1);
        Assert.assertEquals("cell (3,1) contents", "2.2", cell.asText());
        cell = table.getCellAt(3, 2);
        Assert.assertEquals("cell (3,2) contents", "2.3", cell.asText());
        cell = table.getCellAt(3, 3);
        Assert.assertEquals("cell (3,3) contents", "2.4", cell.asText());
        cell = table.getCellAt(3, 4);
        Assert.assertEquals("cell (3,4) contents", "2.4", cell.asText());
        cell = table.getCellAt(3, 5);
        Assert.assertNull("cell (3,5) contents", cell);

        // fifth row
        cell = table.getCellAt(4, 0);
        Assert.assertEquals("cell (4,0) contents", "1.1", cell.asText());
        cell = table.getCellAt(4, 1);
        Assert.assertEquals("cell (4,1) contents", "2.2", cell.asText());
        cell = table.getCellAt(4, 2);
        Assert.assertEquals("cell (4,2) contents", "3.3", cell.asText());
        cell = table.getCellAt(4, 3);
        Assert.assertEquals("cell (4,3) contents", "2.4", cell.asText());
        cell = table.getCellAt(4, 4);
        Assert.assertEquals("cell (4,4) contents", "2.4", cell.asText());
        cell = table.getCellAt(4, 5);
        Assert.assertNull("cell (4,5) contents", cell);

        // sixth row
        cell = table.getCellAt(5, 0);
        Assert.assertEquals("cell (5,0) contents", "4.1", cell.asText());
        cell = table.getCellAt(5, 1);
        Assert.assertEquals("cell (5,1) contents", "4.2", cell.asText());
        cell = table.getCellAt(5, 2);
        Assert.assertEquals("cell (5,2) contents", "4.3", cell.asText());
        cell = table.getCellAt(5, 3);
        Assert.assertEquals("cell (5,3) contents", "2.4", cell.asText());
        cell = table.getCellAt(5, 4);
        Assert.assertEquals("cell (5,4) contents", "2.4", cell.asText());
        cell = table.getCellAt(5, 5);
        Assert.assertNull("cell (5,5) contents", cell);

        // seventh row
        cell = table.getCellAt(6, 0);
        Assert.assertEquals("cell (6,0) contents", "5.1", cell.asText());
        cell = table.getCellAt(6, 1);
        Assert.assertEquals("cell (6,1) contents", "5.2", cell.asText());
        cell = table.getCellAt(6, 2);
        Assert.assertEquals("cell (6,2) contents", "5.2", cell.asText());
        cell = table.getCellAt(6, 3);
        Assert.assertEquals("cell (6,3) contents", "2.4", cell.asText());
        cell = table.getCellAt(6, 4);
        Assert.assertEquals("cell (6,4) contents", "2.4", cell.asText());
        cell = table.getCellAt(6, 5);
        Assert.assertNull("cell (6,5) contents", cell);

        // eighth row
        cell = table.getCellAt(7, 0);
        Assert.assertEquals("cell (7,0) contents", "6.1", cell.asText());
        cell = table.getCellAt(7, 1);
        Assert.assertEquals("cell (7,1) contents", "5.2", cell.asText());
        cell = table.getCellAt(7, 2);
        Assert.assertEquals("cell (7,2) contents", "5.2", cell.asText());
        cell = table.getCellAt(7, 3);
        Assert.assertEquals("cell (7,3) contents", "6.4", cell.asText());
        cell = table.getCellAt(7, 4);
        Assert.assertEquals("cell (7,4) contents", "6.5", cell.asText());
        cell = table.getCellAt(7, 5);
        Assert.assertNull("cell (6,5) contents", cell);

        // after the table
        cell = table.getCellAt(8, 0);
        Assert.assertNull("cell (8,0) contents", cell);
    }

    /**
     * Tests getTableCell(int,int) for a cell that doesn't exist.
     * @exception Exception If the test fails
     */
    @Test
    public void getTableCell_NotFound() throws Exception {
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
    public void getTableRows() throws Exception {
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
    public void getTableRows_WithHeadBodyFoot() throws Exception {
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
    public void rowGroupings_AllDefined() throws Exception {
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
    public void rowGroupings_NoneDefined()
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
    public void getCaptionText() throws Exception {
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
    public void insertionOfTbodyTags() throws Exception {
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
    public void jsInTable() throws Exception {
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
    @Alerts(FF = "[object HTMLTableElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <table id='myId'/>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(HtmlTable.class.isInstance(page.getHtmlElementById("myId")));
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
