/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLTableElement}.
 *
 * @author David D. Kilzer
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLTableElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"caption1", "caption2", "null", "caption3", "exception",
             "[object HTMLTableCaptionElement]", "caption3", "caption4"})
    public void tableCaptions() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
            + "  <table id='table_1'><caption>caption1</caption><caption>caption2</caption>\n"
            + "    <tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>\n"
            + "    <tr><td colspan='2'>cell3</td></tr>\n"
            + "  </table>\n"
            + "  <script type='text/javascript' language='JavaScript'>\n"
            + LOG_TITLE_FUNCTION
            + "    var table = document.getElementById('table_1');\n"
            + "    log(table.caption.innerHTML);\n"

            + "    table.deleteCaption();\n"
            + "    log(table.caption.innerHTML);\n"

            + "    table.deleteCaption();\n"
            + "    log(table.caption);\n"

            + "    var newCaption = table.createCaption();\n"
            + "    newCaption.innerHTML = 'caption3';\n"
            + "    log(table.caption.innerHTML);\n"

            + "    try { table.caption = 123; } catch(e) { log('exception') }\n"
            + "    log(table.caption);\n"
            + "    if (table.caption) { log(table.caption.innerHTML) }\n"

            + "    var caption4 = document.createElement('caption');\n"
            + "    caption4.innerHTML = 'caption4';\n"
            + "    try { table.caption = caption4; } catch(e) { log('exception') }\n"
            + "    log(table.caption.innerHTML);\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"thead1", "thead2", "null", "thead3", "exception",
             "[object HTMLTableSectionElement]", "thead3", "thead4"})
    public void tableHeaders() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
            + "  <table id='table_1'>\n"
            + "    <thead id='thead1'><tr><td>cell1</td><td>cell2</td><td>cell3</td></tr></thead>\n"
            + "    <thead id='thead2'><tr><td>cell7</td><td>cell8</td><td>cell9</td></tr></thead>\n"
            + "    <tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>\n"
            + "    <tr><td colspan='2'>cell3</td></tr>\n"
            + "  </table>\n"
            + "  <script type='text/javascript' language='JavaScript'>\n"
            + LOG_TITLE_FUNCTION
            + "    var table = document.getElementById('table_1');\n"
            + "    log(table.tHead.id);\n"

            + "    table.deleteTHead();\n"
            + "    log(table.tHead.id);\n"

            + "    table.deleteTHead();\n"
            + "    log(table.tHead);\n"

            + "    var newTHead = table.createTHead();\n"
            + "    newTHead.id = 'thead3';\n"
            + "    log(table.tHead.id);\n"

            + "    try { table.tHead = 123; } catch(e) { log('exception') }\n"
            + "    log(table.tHead);\n"
            + "    if (table.tHead) { log(table.tHead.id) }\n"

            + "    var tHead4 = document.createElement('tHead');\n"
            + "    tHead4.id = 'thead4';\n"
            + "    try { table.tHead = tHead4; } catch(e) { log('exception') }\n"
            + "    log(table.tHead.id);\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "true", "4 2 2", "6 3 3"})
    public void tableBodies() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
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
            + LOG_TITLE_FUNCTION
            + "    var table = document.getElementById('table_1');\n"
            + "    var bodies = table.tBodies;\n"
            + "    log(bodies.length);\n"
            + "    log(bodies == table.tBodies);\n"
            + "    var body1 = table.tBodies[0];\n"
            + "    var body2 = table.tBodies[1];\n"
            + "    log(table.rows.length + ' ' + body1.rows.length + ' ' + body2.rows.length);\n"
            + "    table.insertRow(-1); // Should add at end, to body2.\n"
            + "    body1.insertRow(-1); // Add one to body1, as well.\n"
            + "    log(table.rows.length + ' ' + body1.rows.length + ' ' + body2.rows.length);\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "true", "3", "2", "3", "2"})
    public void tableRows() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
            + "  <table id='table_1'>\n"
            + "    <tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>\n"
            + "    <tr><td colspan='2'>cell3</td></tr>\n"
            + "  </table>\n"
            + "  <script type='text/javascript' language='JavaScript'>\n"
            + LOG_TITLE_FUNCTION
            + "    var table = document.getElementById('table_1');\n"
            + "    var rows = table.rows;\n"
            + "    log(rows.length);\n"
            + "    log(rows == table.rows);\n"
            + "    table.insertRow(1);\n"
            + "    log(rows.length);\n"
            + "    table.deleteRow(1);\n"
            + "    log(rows.length);\n"
            + "    table.insertRow(rows.length);\n"
            + "    log(rows.length);\n"
            + "    table.deleteRow(-1);\n"
            + "    log(rows.length);\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 1528024.
     * @see <a href="https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1528024&group_id=47038">bug</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1"})
    public void tableHeadRows() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var t = document.getElementById('myTable');\n"
            + "  log(t.rows[0].cells.length);\n"
            + "  log(t.rows[1].cells.length);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<table id='myTable'>\n"
            + "<tr><th>Some Heading</th></tr>\n"
            + "<tr><td>some desc</td></tr>\n"
            + "</table>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "true", "8 2 2 2 2",
             "9 2 2 2 3", "8 2 2 1 3", "9 2 3 1 3", "8 1 3 1 3"})
    public void tableRowsWithManySections() throws Exception {
        final String html = "<html><head></head>\n"
            + "<body>\n"
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
            + LOG_TITLE_FUNCTION
            + "  <!--\n"
            + "    var table = document.getElementById('table_1');\n"
            + "    var bodies = table.tBodies;\n"
            + "    log(bodies.length);\n"
            + "    log(bodies == table.tBodies);\n"
            + "    var head = table.tHead;\n"
            + "    var body1 = table.tBodies.item(0);\n"
            + "    var body2 = table.tBodies.item(1);\n"
            + "    var foot = table.tFoot;\n"
            + "    log(table.rows.length + ' ' + head.rows.length + ' ' + body1.rows.length "
            + "        + ' ' + body2.rows.length + ' ' + foot.rows.length);\n"
            + "    table.insertRow(6); // Insert a row in the footer.\n"
            + "    log(table.rows.length + ' ' + head.rows.length + ' ' + body1.rows.length "
            + "        + ' ' + body2.rows.length + ' ' + foot.rows.length);\n"
            + "    table.deleteRow(5); // Delete a row from the second body.\n"
            + "    log(table.rows.length + ' ' + head.rows.length + ' ' + body1.rows.length "
            + "        + ' ' + body2.rows.length + ' ' + foot.rows.length);\n"
            + "    table.insertRow(2); // Insert a row in the first body.\n"
            + "    log(table.rows.length + ' ' + head.rows.length + ' ' + body1.rows.length "
            + "        + ' ' + body2.rows.length + ' ' + foot.rows.length);\n"
            + "    table.deleteRow(1); // Delete a row from the header.\n"
            + "    log(table.rows.length + ' ' + head.rows.length + ' ' + body1.rows.length "
            + "        + ' ' + body2.rows.length + ' ' + foot.rows.length);\n"
            + "  // -->\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"tfoot1", "tfoot2", "null", "tfoot3", "exception",
             "[object HTMLTableSectionElement]", "tfoot3", "tfoot4"})
    public void tableFooters() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
            + "  <table id='table_1'>\n"
            + "    <tr><td>cell1</td><td>cell2</td><td rowspan='2'>cell4</td></tr>\n"
            + "    <tr><td colspan='2'>cell3</td></tr>\n"
            + "    <tfoot id='tfoot1'><tr><td>cell1</td><td>cell2</td><td>cell3</td></tr></tfoot>\n"
            + "    <tfoot id='tfoot2'><tr><td>cell7</td><td>cell8</td><td>cell9</td></tr></tfoot>\n"
            + "  </table>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var table = document.getElementById('table_1');\n"
            + "    log(table.tFoot.id);\n"

            + "    table.deleteTFoot();\n"
            + "    log(table.tFoot.id);\n"

            + "    table.deleteTFoot();\n"
            + "    log(table.tFoot);\n"

            + "    var newTFoot = table.createTFoot();\n"
            + "    newTFoot.id = 'tfoot3';\n"
            + "    log(table.tFoot.id);\n"

            + "    try { table.tFoot = 123; } catch(e) { log('exception') }\n"
            + "    log(table.tFoot);\n"
            + "    if (table.tFoot) { log(table.tFoot.id) }\n"

            + "    var tFoot4 = document.createElement('tFoot');\n"
            + "    tFoot4.id = 'tfoot4';\n"
            + "    try { table.tFoot = tFoot4; } catch(e) { log('exception') }\n"
            + "    log(table.tFoot.id);\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "0", "1", "-1"})
    public void cellIndex() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('th1').cellIndex);\n"
            + "    log(document.getElementById('th2').cellIndex);\n"
            + "    log(document.getElementById('td1').cellIndex);\n"
            + "    log(document.getElementById('td2').cellIndex);\n"
            + "    log(document.createElement('td').cellIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'><table>\n"
            + "<tr><th id='th1'>a</th><th id='th2'>b</th></tr>\n"
            + "<tr><td id='td1'>c</td><td id='td2'>d</td></tr>\n"
            + "</table></body></html>";

        loadPageVerifyTitle2(html);
    }

    private void insertRow(final String rowIndex) throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
            + "  <table id='table_1'>\n"
            + "    <tr><td>first</td></tr>\n"
            + "    <tr><td>second</td></tr>\n"
            + "  </table>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var table = document.getElementById('table_1');\n"
            + "    log(table.rows.length);\n"
            + "    try {\n"
            + "      var newRow = table.insertRow(" + rowIndex + ");\n"
            + "      log(table.rows.length);\n"
            + "      log(newRow.rowIndex);\n"
            + "    } catch (e) { log('exception'); }\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3", "2"})
    public void insertRowEmpty() throws Exception {
        insertRow("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "exception"})
    public void insertRow_MinusTwo() throws Exception {
        insertRow("-2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3", "2"})
    public void insertRow_MinusOne() throws Exception {
        insertRow("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3", "0"})
    public void insertRow_Zero() throws Exception {
        insertRow("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3", "1"})
    public void insertRow_One() throws Exception {
        insertRow("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3", "2"})
    public void insertRow_Two() throws Exception {
        insertRow("2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "exception"})
    public void insertRow_Three() throws Exception {
        insertRow("3");
    }

    /**
     * Regression test for bug 1244839.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"mytable", "mytable"})
    public void insertRowNested() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var container = document.getElementById('mytable');\n"
            + "  log(container.id);\n"
            + "  var tableRow = container.insertRow(1);\n"
            + "  log(tableRow.parentNode.parentNode.id);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<table id='mytable'>\n"
            + "<tr>\n"
            + "<td>\n"
            + "  <table id='nested'><tr><td></td></tr></table>\n"
            + "</td></tr>\n"
            + "</table>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests that a <tt>tbody</tt> is automatically created.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TBODY", "TABLE"})
    public void insertRowInEmptyTable() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var oTable = document.getElementById('mytable');\n"
            + "  var tableRow = oTable.insertRow(0);\n"
            + "  log(tableRow.parentNode.tagName);\n"
            + "  log(tableRow.parentNode.parentNode.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<table id='mytable'>\n"
            + "</table>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests that a <tt>tbody</tt> is automatically created.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TBODY", "TBODY", "TBODY"})
    public void insertRowInTableWithEmptyTbody() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var oTable = document.getElementById('mytable');\n"
            + "  log(oTable.lastChild.tagName);\n"
            + "  var tableRow = oTable.insertRow(0);\n"
            + "  log(oTable.lastChild.tagName);\n"
            + "  log(tableRow.parentNode.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<table id='mytable'><tbody></tbody></table>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests length, tBodies on nested rows.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1"})
    public void nestedTables() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var myTable = document.getElementById('mytable');\n"
            + "  log(myTable.rows.length);\n"
            + "  log(myTable.tBodies.length);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<table id='mytable'>\n"
            + "<tr>\n"
            + "<td>\n"
            + "  <table id='nested'><tr><td></td></tr></table>\n"
            + "</td></tr>\n"
            + "</table>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests string default values.
     * See <a href="https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1538136&group_id=47038">issue 370</a>.
     * Currently not working for FF as HtmlUnit's object names don't map to FF ones.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"table: [object HTMLTableElement]",
                       "row: [object HTMLTableRowElement]",
                       "headcell: [object HTMLTableCellElement]",
                       "datacell: [object HTMLTableCellElement]"},
            IE = {"table: [object HTMLTableElement]",
                  "row: [object HTMLTableRowElement]",
                  "headcell: [object HTMLTableHeaderCellElement]",
                  "datacell: [object HTMLTableDataCellElement]"})
    public void stringValues() throws Exception {
        final String html =
            "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('table: ' + document.getElementById('myTable'));\n"
            + "      log('row: ' + document.getElementById('myRow'));\n"
            + "      log('headcell: ' + document.getElementById('myHeadCell'));\n"
            + "      log('datacell: ' + document.getElementById('myDataCell'));\n"
            + "    }\n"
            + "  </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <table id='myTable'>\n"
            + "      <tr id='myRow'>\n"
            + "        <th id='myHeadCell'>Foo</th>\n"
            + "      </tr>\n"
            + "      <tr>\n"
            + "        <td id='myDataCell'>Foo</th>\n"
            + "      </tr>\n"
            + "    </table>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("21")
    public void cellSpacing() throws Exception {
        final String html
            = "<html><head></head>\n"
                + "<body>\n"
                + "<table id='tableID' cellspacing='2'><tr><td></td></tr></table>\n"
                + "<script language='javascript'>\n"
                + LOG_TITLE_FUNCTION
                + "    var table = document.getElementById('tableID');\n"
                + "    table.cellSpacing += 1;\n"
                + "    log(table.cellSpacing);\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("21")
    public void cellPadding() throws Exception {
        final String html
            = "<html><head></head>\n"
                + "<body>\n"
                + "<table id='tableID' cellpadding='2'><tr><td></td></tr></table>\n"
                + "<script language='javascript'>\n"
                + LOG_TITLE_FUNCTION
                + "    var table = document.getElementById('tableID');\n"
                + "    table.cellPadding += 1;\n"
                + "    log(table.cellPadding);\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no refresh function")
    public void refresh() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      if (document.getElementById('myTable').refresh) {\n"
            + "        document.getElementById('myTable').refresh();\n"
            + "        log('refreshed');\n"
            + "      } else {\n"
            + "        log('no refresh function');\n"
            + "      }\n"
            + "    } catch (e) {\n"
            + "      log('error');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<table id='myTable'></table>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "hello", "left", "hi", "right"},
            IE = {"", "error", "", "left", "error", "left", "right"})
    public void align() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        log(t.align);\n"
            + "        set(t, 'hello');\n"
            + "        log(t.align);\n"
            + "        set(t, 'left');\n"
            + "        log(t.align);\n"
            + "        set(t, 'hi');\n"
            + "        log(t.align);\n"
            + "        set(t, 'right');\n"
            + "        log(t.align);\n"
            + "      }\n"
            + "      function set(e, value) {\n"
            + "        try {\n"
            + "          e.align = value;\n"
            + "        } catch (e) {\n"
            + "          log('error');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <table id='t'>\n"
            + "      <thead id='th'/>\n"
            + "      <tbody id='tb'/>\n"
            + "      <tfoot id='tf'/>\n"
            + "    </table>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "#0000aa", "x"},
            IE = {"", "#0000aa", "#0"})
    public void bgColor() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var table = document.getElementById('table');\n"
            + "        log(table.bgColor);\n"
            + "        table.bgColor = '#0000aa';\n"
            + "        log(table.bgColor);\n"
            + "        table.bgColor = 'x';\n"
            + "        log(table.bgColor);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <table id='table'><tr><td>a</td></tr></table>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cell1", "[object Text]", "abc", "[object Text]", ""})
    public void innerText() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table id='tab'>\n"
            + "    <tr><td>cell1</td></tr>\n"
            + "  </table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tab');\n"
            + "  log(node.innerText);\n"
            + "  log(node.firstChild);\n"

            + "  try { node.innerText = 'abc'; } catch(e) {log('ex');}\n"
            + "  log(node.innerText);\n"
            + "  log(node.firstChild);\n"

            + "  try { node.innerText = ''; } catch(e) {log('ex');}\n"
            + "  log(node.innerText);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\\n\\s\\s\\s\\scell1\\n\\s\\s", "[object\\sText]", "abc", "[object\\sText]", ""})
    public void textContent() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table id='tab'>\n"
            + "    <tr><td>cell1</td></tr>\n"
            + "  </table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  var node = document.getElementById('tab');\n"
            + "  log(node.textContent);\n"
            + "  log(node.firstChild);\n"

            + "  try { node.textContent = 'abc'; } catch(e) {log('ex');}\n"
            + "  log(node.textContent);\n"
            + "  log(node.firstChild);\n"

            + "  try { node.textContent = ''; } catch(e) {log('ex');}\n"
            + "  log(node.textContent);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "#667788", "unknown", "undefined", "undefined", "undefined"},
            IE = {"", "#667788", "#000000", "red", "#123456", "#000000"})
    @NotYetImplemented(IE)
    public void borderColor() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table id='tab1'></table>\n"
            + "  <table id='tab2' borderColor='red'></table>\n"
            + "  <table id='tab3' borderColor='#123456'></table>\n"
            + "  <table id='tab4' borderColor='unknown'></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tab1');\n"
            + "  log(node.borderColor);\n"

            + "  node.borderColor = '#667788';\n"
            + "  log(node.borderColor);\n"

            + "  node.borderColor = 'unknown';\n"
            + "  log(node.borderColor);\n"

            + "  var node = document.getElementById('tab2');\n"
            + "  log(node.borderColor);\n"
            + "  var node = document.getElementById('tab3');\n"
            + "  log(node.borderColor);\n"
            + "  var node = document.getElementById('tab4');\n"
            + "  log(node.borderColor);\n"

            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined"},
            IE = {"", "", "", "red", "#123456", "#000000"})
    @NotYetImplemented(IE)
    public void borderColorDark() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table id='tab1'></table>\n"
            + "  <table id='tab2' borderColorDark='red'></table>\n"
            + "  <table id='tab3' borderColorDark='#123456'></table>\n"
            + "  <table id='tab4' borderColorDark='unknown'></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tab1');\n"
            + "  log(node.borderColorDark);\n"

            + "  node.borderColor = '#667788';\n"
            + "  log(node.borderColorDark);\n"

            + "  node.borderColor = 'unknown';\n"
            + "  log(node.borderColorDark);\n"

            + "  var node = document.getElementById('tab2');\n"
            + "  log(node.borderColorDark);\n"
            + "  var node = document.getElementById('tab3');\n"
            + "  log(node.borderColorDark);\n"
            + "  var node = document.getElementById('tab4');\n"
            + "  log(node.borderColorDark);\n"

            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined"},
            IE = {"", "", "", "red", "#123456", "#000000"})
    @NotYetImplemented(IE)
    public void borderColorLight() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table id='tab1'></table>\n"
            + "  <table id='tab2' borderColorLight='red'></table>\n"
            + "  <table id='tab3' borderColorLight='#123456'></table>\n"
            + "  <table id='tab4' borderColorLight='unknown'></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tab1');\n"
            + "  log(node.borderColorLight);\n"

            + "  node.borderColor = '#667788';\n"
            + "  log(node.borderColorLight);\n"

            + "  node.borderColor = 'unknown';\n"
            + "  log(node.borderColorLight);\n"

            + "  var node = document.getElementById('tab2');\n"
            + "  log(node.borderColorLight);\n"
            + "  var node = document.getElementById('tab3');\n"
            + "  log(node.borderColorLight);\n"
            + "  var node = document.getElementById('tab4');\n"
            + "  log(node.borderColorLight);\n"

            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "hello", "unknown", "exception", "", "test"})
    public void summary() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table id='tab1'></table>\n"
            + "  <table id='tab2' summary=''></table>\n"
            + "  <table id='tab3' summary='test'></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tab1');\n"
            + "  log(node.summary);\n"

            + "  node.summary = 'hello';\n"
            + "  log(node.summary);\n"

            + "  node.summary = 'unknown';\n"
            + "  log(node.summary);\n"

            + "  try { node.summary = unknown; } catch(e) { log('exception') }\n"

            + "  var node = document.getElementById('tab2');\n"
            + "  log(node.summary);\n"

            + "  var node = document.getElementById('tab3');\n"
            + "  log(node.summary);\n"

            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"none", "groups", "rows", "cols", "wrong", ""},
            IE = {"none", "groups", "rows", "cols", "", ""})
    public void getRules() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table id='t1' rules='none'></table>\n"
            + "  <table id='t2' rules='groups'></table>\n"
            + "  <table id='t3' rules='rows'></table>\n"
            + "  <table id='t4' rules='cols'></table>\n"
            + "  <table id='t5' rules='wrong'></table>\n"
            + "  <table id='t6'></table>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  for (var i = 1; i <= 6; i++) {\n"
            + "    log(document.getElementById('t' + i).rules);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"groUPs", "8", "foo", "rows", "cols"},
            IE = {"groups", "error", "groups", "error", "groups", "rows", "cols"})
    public void setRules() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table id='t1' rules='groups'></table>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setRules(elem, value) {\n"
            + "    try {\n"
            + "      elem.rules = value;\n"
            + "    } catch (e) { log('error'); }\n"
            + "    log(elem.rules);\n"
            + "  }\n"

            + "  var elem = document.getElementById('t1');\n"
            + "  setRules(elem, 'groUPs');\n"

            + "  setRules(elem, '8');\n"
            + "  setRules(elem, 'foo');\n"

            + "  setRules(elem, 'rows');\n"
            + "  setRules(elem, 'cols');\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
