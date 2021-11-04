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
 * Tests for {@link HTMLTableRowElement}.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLTableRowElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLTableRowElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <table>\n"
            + "    <tr id='myId'/>\n"
            + "  </table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "td1", "3", "td2", "td4", "2", "td3", "exception", "exception"})
    public void deleteCell() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var tr = document.getElementById('myId');\n"
            + "    log(tr.cells.length);\n"
            + "    log(tr.cells[0].id);\n"
            + "    tr.deleteCell(0);\n"
            + "    log(tr.cells.length);\n"
            + "    log(tr.cells[0].id);\n"
            + "    log(tr.cells[tr.cells.length-1].id);\n"
            + "    tr.deleteCell(-1);\n"
            + "    log(tr.cells.length);\n"
            + "    log(tr.cells[tr.cells.length-1].id);\n"
            + "    try { tr.deleteCell(25); } catch(e) { log('exception'); }\n"
            + "    try { tr.deleteCell(-2); } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <table>\n"
            + "    <tr id='myId'/>\n"
            + "      <td id='td1'>1</td>\n"
            + "      <td id='td2'>2</td>\n"
            + "      <td id='td3'>3</td>\n"
            + "      <td id='td4'>4</td>\n"
            + "    </tr>\n"
            + "  </table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"4", "exception", "4"},
            IE = {"4", "3"})
    public void deleteCell_noArg() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var tr = document.getElementById('myId');\n"
            + "    log(tr.cells.length);\n"
            + "    try { tr.deleteCell(); } catch(e) { log('exception'); }\n"
            + "    log(tr.cells.length);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <table>\n"
            + "    <tr id='myId'/>\n"
            + "      <td id='td1'>1</td>\n"
            + "      <td id='td2'>2</td>\n"
            + "      <td id='td3'>3</td>\n"
            + "      <td id='td4'>4</td>\n"
            + "    </tr>\n"
            + "  </table>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"left", "right", "3", "center", "8", "foo"},
            IE = {"left", "right", "", "error", "error", "center", "right", ""})
    public void align() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <tr id='tr1' align='left'><td>a</td></tr>\n"
            + "  <tr id='tr2' align='right'><td>b</td></tr>\n"
            + "  <tr id='tr3' align='3'><td>c</td></tr>\n"
            + "</table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.align = value;\n"
            + "    } catch (e) {\n"
            + "      log('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var tr1 = document.getElementById('tr1');\n"
            + "  var tr2 = document.getElementById('tr2');\n"
            + "  var tr3 = document.getElementById('tr3');\n"
            + "  log(tr1.align);\n"
            + "  log(tr2.align);\n"
            + "  log(tr3.align);\n"
            + "  set(tr1, 'center');\n"
            + "  set(tr2, '8');\n"
            + "  set(tr3, 'foo');\n"
            + "  log(tr1.align);\n"
            + "  log(tr2.align);\n"
            + "  log(tr3.align);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"p", "po", "", "u", "8", "U8"})
    public void ch() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <tr id='tr1' char='p'><td>a</td></tr>\n"
            + "  <tr id='tr2' char='po'><td>b</td></tr>\n"
            + "  <tr id='tr3'><td>c</td></tr>\n"
            + "</table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var tr1 = document.getElementById('tr1');\n"
            + "  var tr2 = document.getElementById('tr2');\n"
            + "  var tr3 = document.getElementById('tr3');\n"
            + "  log(tr1.ch);\n"
            + "  log(tr2.ch);\n"
            + "  log(tr3.ch);\n"
            + "  tr1.ch = 'u';\n"
            + "  tr2.ch = '8';\n"
            + "  tr3.ch = 'U8';\n"
            + "  log(tr1.ch);\n"
            + "  log(tr2.ch);\n"
            + "  log(tr3.ch);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "4", "", "5.2", "-3", "abc"})
    public void chOff() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <tr id='tr1' charoff='0'><td>a</td></tr>\n"
            + "  <tr id='tr2' charoff='4'><td>b</td></tr>\n"
            + "  <tr id='tr3'><td>c</td></tr>\n"
            + "</table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var tr1 = document.getElementById('tr1');\n"
            + "  var tr2 = document.getElementById('tr2');\n"
            + "  var tr3 = document.getElementById('tr3');\n"
            + "  log(tr1.chOff);\n"
            + "  log(tr2.chOff);\n"
            + "  log(tr3.chOff);\n"
            + "  tr1.chOff = '5.2';\n"
            + "  tr2.chOff = '-3';\n"
            + "  tr3.chOff = 'abc';\n"
            + "  log(tr1.chOff);\n"
            + "  log(tr2.chOff);\n"
            + "  log(tr3.chOff);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"top", "baseline", "3", "middle", "8", "BOTtom"},
            IE = {"top", "baseline", "top", "error", "middle", "baseline", "bottom"})
    public void vAlign() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <tr id='tr1' valign='top'><td>a</td></tr>\n"
            + "  <tr id='tr2' valign='baseline'><td>b</td></tr>\n"
            + "  <tr id='tr3' valign='3'><td>c</td></tr>\n"
            + "</table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.vAlign = value;\n"
            + "    } catch (e) {\n"
            + "      log('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var tr1 = document.getElementById('tr1');\n"
            + "  var tr2 = document.getElementById('tr2');\n"
            + "  var tr3 = document.getElementById('tr3');\n"
            + "  log(tr1.vAlign);\n"
            + "  log(tr2.vAlign);\n"
            + "  log(tr3.vAlign);\n"
            + "  set(tr1, 'middle');\n"
            + "  set(tr2, 8);\n"
            + "  set(tr3, 'BOTtom');\n"
            + "  log(tr1.vAlign);\n"
            + "  log(tr2.vAlign);\n"
            + "  log(tr3.vAlign);\n"
            + "</script>\n"
            + "</body></html>";
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
            + "        var tr = document.getElementById('tr');\n"
            + "        log(tr.bgColor);\n"
            + "        tr.bgColor = '#0000aa';\n"
            + "        log(tr.bgColor);\n"
            + "        tr.bgColor = 'x';\n"
            + "        log(tr.bgColor);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <table><tr id='tr'><td>a</td></tr></table>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0", "3", "1", "-1", "true", "false"})
    public void rowIndex_sectionRowIndex() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <tr id='tr1'><td>a</td></tr>\n"
            + "  <tr id='tr2'><td>b</td></tr>\n"
            + "  <tfoot>\n"
            + "    <tr id='trf1'><td>a</td></tr>\n"
            + "    <tr id='trf2'><td>a</td></tr>\n"
            + "  </tfoot>\n"
            + "</table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var tr1 = document.getElementById('tr1');\n"
            + "  var trf2 = document.getElementById('trf2');\n"
            + "  log(tr1.rowIndex);\n"
            + "  log(tr1.sectionRowIndex);\n"
            + "  log(trf2.rowIndex);\n"
            + "  log(trf2.sectionRowIndex);\n"
            + "  var tr3 = document.createElement('tr');\n"
            + "  log(tr3.rowIndex);\n"
            + "  log(tr3.sectionRowIndex == -1);\n"
            + "  log(tr3.sectionRowIndex > 1000);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for 3180939; same left offset for both
     * rows is expected.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void offsetLeftDifferentRows() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <tr>\n"
            + "    <td id='td_1_1'>1_1</td>\n"
            + "    <td id='td_1_2'>1_2</td>\n"
            + "  </tr>\n"
            + "  <tr>\n"
            + "    <td id='td_2_1'>2_1</td>\n"
            + "    <td id='td_2_2'>2_2</td>\n"
            + "  </tr>\n"
            + "</table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var o1 = document.getElementById('td_1_1').offsetLeft;\n"
            + "  var o2 = document.getElementById('td_2_1').offsetLeft;\n"
            + "  log(o1 == o2 ? 'true' : o1 + ' != ' + o2);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cell1", "[object HTMLTableCellElement]", "abc", "[object Text]", ""},
            IE = {"cell1", "[object HTMLTableDataCellElement]", "abc", "[object Text]", ""})
    public void innerText() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table>\n"
            + "    <tr id='tab_row'><td>cell1</td></tr>\n"
            + "  </table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tab_row');\n"
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
    @Alerts(DEFAULT = {"cell1", "[object HTMLTableCellElement]", "abc", "[object Text]", ""},
            IE = {"cell1", "[object HTMLTableDataCellElement]", "abc", "[object Text]", ""})
    public void textContent() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table>\n"
            + "    <tr id='tab_row'><td>cell1</td></tr>\n"
            + "  </table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tab_row');\n"
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

    private void insertCell(final String cellIndex) throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
            + "  <table>\n"
            + "    <tr id='myRow'>\n"
            + "      <td>first</td>\n"
            + "      <td>second</td>\n"
            + "    </tr>\n"
            + "  </table>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var row = document.getElementById('myRow');\n"
            + "    log(row.cells.length);\n"
            + "    try {\n"
            + "      var newCell = row.insertCell(" + cellIndex + ");\n"
            + "      log(row.cells.length);\n"
            + "      log(newCell.cellIndex);\n"
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
    public void insertCellEmpty() throws Exception {
        insertCell("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "exception"})
    public void insertCell_MinusTwo() throws Exception {
        insertCell("-2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3", "2"})
    public void insertCell_MinusOne() throws Exception {
        insertCell("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3", "0"})
    public void insertCell_Zero() throws Exception {
        insertCell("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3", "1"})
    public void insertCell_One() throws Exception {
        insertCell("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3", "2"})
    public void insertCell_Two() throws Exception {
        insertCell("2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "exception"})
    public void insertCell_Three() throws Exception {
        insertCell("3");
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
            + "  <table><tr id='tabr1'></tr></table>\n"
            + "  <table><tr id='tabr2' borderColor='red'></tr></table>\n"
            + "  <table><tr id='tabr3' borderColor='#123456'></tr></table>\n"
            + "  <table><tr id='tabr4' borderColor='unknown'></tr></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tabr1');\n"
            + "  log(node.borderColor);\n"

            + "  node.borderColor = '#667788';\n"
            + "  log(node.borderColor);\n"

            + "  node.borderColor = 'unknown';\n"
            + "  log(node.borderColor);\n"

            + "  var node = document.getElementById('tabr2');\n"
            + "  log(node.borderColor);\n"
            + "  var node = document.getElementById('tabr3');\n"
            + "  log(node.borderColor);\n"
            + "  var node = document.getElementById('tabr4');\n"
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
            + "  <table><tr id='tabr1'></tr></table>\n"
            + "  <table><tr id='tabr2' borderColorDark='red'></tr></table>\n"
            + "  <table><tr id='tabr3' borderColorDark='#123456'></tr></table>\n"
            + "  <table><tr id='tabr4' borderColorDark='unknown'></tr></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tabr1');\n"
            + "  log(node.borderColorDark);\n"

            + "  node.borderColor = '#667788';\n"
            + "  log(node.borderColorDark);\n"

            + "  node.borderColor = 'unknown';\n"
            + "  log(node.borderColorDark);\n"

            + "  var node = document.getElementById('tabr2');\n"
            + "  log(node.borderColorDark);\n"
            + "  var node = document.getElementById('tabr3');\n"
            + "  log(node.borderColorDark);\n"
            + "  var node = document.getElementById('tabr4');\n"
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
            + "  <table><tr id='tabr1'></tr></table>\n"
            + "  <table><tr id='tabr2' borderColorLight='red'></tr></table>\n"
            + "  <table><tr id='tabr3' borderColorLight='#123456'></tr></table>\n"
            + "  <table><tr id='tabr4' borderColorLight='unknown'></tr></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tabr1');\n"
            + "  log(node.borderColorLight);\n"

            + "  node.borderColor = '#667788';\n"
            + "  log(node.borderColorLight);\n"

            + "  node.borderColor = 'unknown';\n"
            + "  log(node.borderColorLight);\n"

            + "  var node = document.getElementById('tabr2');\n"
            + "  log(node.borderColorLight);\n"
            + "  var node = document.getElementById('tabr3');\n"
            + "  log(node.borderColorLight);\n"
            + "  var node = document.getElementById('tabr4');\n"
            + "  log(node.borderColorLight);\n"

            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }
}
