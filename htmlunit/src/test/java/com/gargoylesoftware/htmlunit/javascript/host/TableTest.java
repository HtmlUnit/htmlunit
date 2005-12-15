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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for Table.
 * 
 * @author David D. Kilzer
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @version $Revision$
 */
public class TableTest extends WebTestCase {

    /**
     * Create an instance
     * @param name The name of the test
     */
    public TableTest( final String name ) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testTableCaptions() throws Exception {

        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "  <table id='table_1'><caption>caption1</caption><caption>caption2</caption>\n"
            + "    <tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>\n"
            + "    <tr><td colspan='2'>cell3</td></tr>\n"
            + "  </table>\n"
            + "  <script type='text/javascript' language='JavaScript'>\n"
            + "  <!--\n"
            + "    var table = document.getElementById('table_1');\n"
            + "    alert(table.caption.innerHTML);\n"
            + "    table.deleteCaption();\n"
            + "    alert(table.caption.innerHTML);\n"
            + "    table.deleteCaption();\n"
            + "    alert(table.caption);\n"
            + "    var newCaption = table.createCaption();\n"
            + "    newCaption.innerHTML = 'caption3';\n"
            + "    alert(table.caption.innerHTML);\n"
            + "  // -->\n"
            + "  </script>\n"
            + "</body></html>\n";

        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[] { "caption1", "caption2", "null", "caption3" });
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testTableHeaders() throws Exception {

        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "  <table id='table_1'>\n"
            + "    <thead id='thead1'><tr><td>cell1</td><td>cell2</td><td>cell3</td></tr></thead>\n"
            + "    <thead id='thead2'><tr><td>cell7</td><td>cell8</td><td>cell9</td></tr></thead>\n"
            + "    <tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>\n"
            + "    <tr><td colspan='2'>cell3</td></tr>\n"
            + "  </table>\n"
            + "  <script type='text/javascript' language='JavaScript'>\n"
            + "  <!--\n"
            + "    var table = document.getElementById('table_1');\n"
            + "    alert(table.tHead.id);\n"
            + "    table.deleteTHead();\n"
            + "    alert(table.tHead.id);\n"
            + "    table.deleteTHead();\n"
            + "    alert(table.tHead);\n"
            + "    var newTHead = table.createTHead();\n"
            + "    newTHead.id = 'thead3';\n"
            + "    alert(table.tHead.id);\n"
            + "  // -->\n"
            + "  </script>\n"
            + "</body></html>\n";

        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[] { "thead1", "thead2", "null", "thead3" });
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testTableBodies() throws Exception {

        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "  <table id='table_1'>\n"
            + "    <tbody id='tbody1'>\n"
            + "      <tr><td>cell1</td><td>cell2</td></tr>\n"
            + "      <tr><td>cell3</td><td>cell4</td></tr>\n"
            + "    </tbody>\n"
            + "    <tbody id='tbody2'>\n"
            + "      <tr><td>cell1</td><td>cell2</td></tr>\n"
            + "      <tr><td>cell3</td><td>cell4</td></tr>\n"
            + "    </tbody>\n"
            + "  </table>\n"
            + "  <script type='text/javascript' language='JavaScript'>\n"
            + "    var table = document.getElementById('table_1');\n"
            + "    var bodies = table.tBodies;\n"
            + "    alert(bodies.length);\n"
            + "    alert(bodies == table.tBodies);\n"
            + "    var body1 = table.tBodies[0];\n"
            + "    var body2 = table.tBodies[1];\n"
            + "    alert(table.rows.length + ' ' + body1.rows.length + ' ' + body2.rows.length);\n"
            + "    table.insertRow(-1); // Should add at end, to body2.\n"
            + "    body1.insertRow(-1); // Add one to body1, as well.\n"
            + "    alert(table.rows.length + ' ' + body1.rows.length + ' ' + body2.rows.length);\n"
            + "  </script>\n"
            + "</body></html>\n";

        final List expectedAlerts = Arrays.asList(new String[] { "2", "true", "4 2 2", "6 3 3" });
        createTestPageForRealBrowserIfNeeded(htmlContent, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testTableRows() throws Exception {

        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "  <table id='table_1'>\n"
            + "    <tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>\n"
            + "    <tr><td colspan='2'>cell3</td></tr>\n"
            + "  </table>\n"
            + "  <script type='text/javascript' language='JavaScript'>\n"
            + "  <!--\n"
            + "    var table = document.getElementById('table_1');\n"
            + "    var rows = table.rows;\n"
            + "    alert(rows.length);\n"
            + "    alert(rows == table.rows);\n"
            + "    table.insertRow(1);\n"
            + "    alert(rows.length);\n"
            + "    table.deleteRow(1);\n"
            + "    alert(rows.length);\n"
            + "    table.insertRow(rows.length);\n"
            + "    alert(rows.length);\n"
            + "  // -->\n"
            + "  </script>\n"
            + "</body></html>\n";

        final List expectedAlerts = Arrays.asList(new String[] { "2", "true", "3", "2", "3" });
        createTestPageForRealBrowserIfNeeded(htmlContent, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testTableRowsWithManySections() throws Exception {

        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "  <table id='table_1'>\n"
            + "    <thead>\n"
            + "      <tr><td>cell1</td><td>cell2</td></tr>\n"
            + "      <tr><td>cell3</td><td>cell4</td></tr>\n"
            + "    </thead>\n"
            + "    <tbody id='tbody1'>\n"
            + "      <tr><td>cell1</td><td>cell2</td></tr>\n"
            + "      <tr><td>cell3</td><td>cell4</td></tr>\n"
            + "    </tbody>\n"
            + "    <tbody id='tbody2'>\n"
            + "      <tr><td>cell1</td><td>cell2</td></tr>\n"
            + "      <tr><td>cell3</td><td>cell4</td></tr>\n"
            + "    </tbody>\n"
            + "    <tfoot>\n"
            + "      <tr><td>cell1</td><td>cell2</td></tr>\n"
            + "      <tr><td>cell3</td><td>cell4</td></tr>\n"
            + "    </tfoot>\n"
            + "  </table>\n"
            + "  <script type='text/javascript' language='JavaScript'>\n"
            + "  <!--\n"
            + "    var table = document.getElementById('table_1');\n"
            + "    var bodies = table.tBodies;\n"
            + "    alert(bodies.length);\n"
            + "    alert(bodies == table.tBodies);\n"
            + "    var head = table.tHead;\n"
            + "    var body1 = table.tBodies.item(0);\n"
            + "    var body2 = table.tBodies.item(1);\n"
            + "    var foot = table.tFoot;\n"
            + "    alert(table.rows.length + ' ' + head.rows.length + ' ' + body1.rows.length "
            + "        + ' ' + body2.rows.length + ' ' + foot.rows.length);\n"
            + "    table.insertRow(6); // Insert a row in the footer.\n"
            + "    alert(table.rows.length + ' ' + head.rows.length + ' ' + body1.rows.length "
            + "        + ' ' + body2.rows.length + ' ' + foot.rows.length);\n"
            + "    table.deleteRow(5); // Delete a row from the second body.\n"
            + "    alert(table.rows.length + ' ' + head.rows.length + ' ' + body1.rows.length "
            + "        + ' ' + body2.rows.length + ' ' + foot.rows.length);\n"
            + "    table.insertRow(2); // Insert a row in the first body.\n"
            + "    alert(table.rows.length + ' ' + head.rows.length + ' ' + body1.rows.length "
            + "        + ' ' + body2.rows.length + ' ' + foot.rows.length);\n"
            + "    table.deleteRow(1); // Delete a row from the header.\n"
            + "    alert(table.rows.length + ' ' + head.rows.length + ' ' + body1.rows.length "
            + "        + ' ' + body2.rows.length + ' ' + foot.rows.length);\n"
            + "  // -->\n"
            + "  </script>\n"
            + "</body></html>\n";

        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[] {"2", "true", "8 2 2 2 2",
            "9 2 2 2 3", "8 2 2 1 3", "9 2 3 1 3", "8 1 3 1 3"});
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testTableFooters() throws Exception {

        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "  <table id='table_1'>\n"
            + "    <tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>\n"
            + "    <tr><td colspan='2'>cell3</td></tr>\n"
            + "    <tfoot id='tfoot1'><tr><td>cell1</td><td>cell2</td><td>cell3</td></tr></tfoot>\n"
            + "    <tfoot id='tfoot2'><tr><td>cell7</td><td>cell8</td><td>cell9</td></tr></tfoot>\n"
            + "  </table>\n"
            + "  <script type='text/javascript' language='JavaScript'>\n"
            + "  <!--\n"
            + "    var table = document.getElementById('table_1');\n"
            + "    alert(table.tFoot.id);\n"
            + "    table.deleteTFoot();\n"
            + "    alert(table.tFoot.id);\n"
            + "    table.deleteTFoot();\n"
            + "    alert(table.tFoot);\n"
            + "    var newTFoot = table.createTFoot();\n"
            + "    newTFoot.id = 'tfoot3';\n"
            + "    alert(table.tFoot.id);\n"
            + "  // -->\n"
            + "  </script>\n"
            + "</body></html>\n";

        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[] { "tfoot1", "tfoot2", "null", "tfoot3" });
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testCellIndex() throws Exception {

        final String htmlContent
            = "<html><head><title>Test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('th1').cellIndex);\n"
            + "    alert(document.getElementById('th2').cellIndex);\n"
            + "    alert(document.getElementById('td1').cellIndex);\n"
            + "    alert(document.getElementById('td2').cellIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'><table>\n"
            + "<tr><th id='th1'>a</th><th id='th2'>b</th></tr>\n"
            + "<tr><td id='td1'>c</td><td id='td2'>d</td></tr>\n"
            + "</table></body></html>";

        final List expectedAlerts = Arrays.asList( new String[] { "0", "1", "0", "1" } );
        createTestPageForRealBrowserIfNeeded( htmlContent, expectedAlerts );

        final List collectedAlerts = new ArrayList();
        loadPage( htmlContent, collectedAlerts );

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testInsertRow() throws Exception {

        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "  <table id='table_1'>\n"
            + "  <tr><td>foo</td></tr>\n"
            + "  </table>\n"
            + "  <script type='text/javascript' language='JavaScript'>\n"
            + "    var table = document.getElementById('table_1');\n"
            + "    alert(table.rows.length);\n"
            + "    var newRow = table.insertRow(-1);\n"
            + "    alert(table.rows.length);\n"
            + "    alert(newRow.rowIndex);\n"
            + "    alert(newRow.cells.length);\n"
            + "    var newCell = newRow.insertCell(-1);\n"
            + "    alert(newCell.tagName);\n"
            + "    alert(newRow.cells.length);\n"
            + "    newRow.insertCell(newRow.cells.length);\n"
            + "    alert(newRow.cells.length);\n"
            + "  </script>\n"
            + "</body></html>\n";

        final List expectedAlerts = Arrays.asList(new String[] { "1", "2", "1", "0", "TD", "1", "2" });
        createTestPageForRealBrowserIfNeeded(htmlContent, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 1244839
     * @throws Exception if the test fails
     */
    public void testInsertRowNested() throws Exception {

        final String content =
            "<html><head>"
            + "<script>"
            + "function test()"
            + "{"
            + "  var container = document.getElementById('mytable');"
            + "  alert(container.id);"
            + "  var tableRow = container.insertRow(1);"
            + "  alert(tableRow.parentNode.parentNode.id);"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'>"
            + "<table id='mytable'>"
            + "<tr>"
            + "<td>"
            + "  <table id='nested'><tr><td></td></tr></table>"
            + "</td></tr>"
            + "</table>"
            + "</body>"
            + "</html>";

        final List expectedAlerts = Arrays.asList(new String[] { "mytable", "mytable" });
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that a tbody is created
     * @throws Exception if the test fails
     */
    public void testInsertRowInEmtpyTable() throws Exception {

        final String content =
            "<html><head>"
            + "<script>"
            + "function test()"
            + "{"
            + "  var oTable = document.getElementById('mytable');"
            + "  var tableRow = oTable.insertRow(0);"
            + "  alert(tableRow.parentNode.tagName);"
            + "  alert(tableRow.parentNode.parentNode.tagName);"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'>"
            + "<table id='mytable'>"
            + "</table>"
            + "</body>"
            + "</html>";

        final List expectedAlerts = Arrays.asList(new String[] { "TBODY", "TABLE" });
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }


    /**
     * Tests length, tBodies on nested rows
     * @throws Exception if the test fails
     */
    public void testNestedTables() throws Exception {

        final String content =
            "<html><head>"
            + "<script>"
            + "function test()"
            + "{"
            + "  var myTable = document.getElementById('mytable');"
            + "  alert(myTable.rows.length);"
            + "  alert(myTable.tBodies.length);"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'>"
            + "<table id='mytable'>"
            + "<tr>"
            + "<td>"
            + "  <table id='nested'><tr><td></td></tr></table>"
            + "</td></tr>"
            + "</table>"
            + "</body>"
            + "</html>";

        final List expectedAlerts = Arrays.asList(new String[] { "1", "1" });
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }
}
