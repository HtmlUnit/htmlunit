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
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link HTMLTableCellElement}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLTableCellElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"left", "right", "3", "center", "8", "foo"},
            IE = {"left", "right", "", "error", "error", "center", "right", ""})
    public void align() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <tr>\n"
            + "    <td id='td1' align='left'>a</td>\n"
            + "    <td id='td2' align='right'>b</td>\n"
            + "    <td id='td3' align='3'>c</td>\n"
            + "  </tr>\n"
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
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"
            + "  log(td1.align);\n"
            + "  log(td2.align);\n"
            + "  log(td3.align);\n"
            + "  set(td1, 'center');\n"
            + "  set(td2, '8');\n"
            + "  set(td3, 'foo');\n"
            + "  log(td1.align);\n"
            + "  log(td2.align);\n"
            + "  log(td3.align);\n"
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
            + "  <tr>\n"
            + "    <td id='td1' char='p'>a</td>\n"
            + "    <td id='td2' char='po'>b</td>\n"
            + "    <td id='td3'>c</td>\n"
            + "  </tr>\n"
            + "</table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"
            + "  log(td1.ch);\n"
            + "  log(td2.ch);\n"
            + "  log(td3.ch);\n"
            + "  td1.ch = 'u';\n"
            + "  td2.ch = '8';\n"
            + "  td3.ch = 'U8';\n"
            + "  log(td1.ch);\n"
            + "  log(td2.ch);\n"
            + "  log(td3.ch);\n"
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
            + "  <tr>\n"
            + "    <td id='td1' charoff='0'>a</td>\n"
            + "    <td id='td2' charoff='4'>b</td>\n"
            + "    <td id='td3'>c</td>\n"
            + "  </tr>\n"
            + "</table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"
            + "  log(td1.chOff);\n"
            + "  log(td2.chOff);\n"
            + "  log(td3.chOff);\n"
            + "  td1.chOff = '5.2';\n"
            + "  td2.chOff = '-3';\n"
            + "  td3.chOff = 'abc';\n"
            + "  log(td1.chOff);\n"
            + "  log(td2.chOff);\n"
            + "  log(td3.chOff);\n"
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
            + "  <tr>\n"
            + "    <td id='td1' valign='top'>a</td>\n"
            + "    <td id='td2' valign='baseline'>b</td>\n"
            + "    <td id='td3' valign='3'>c</td>\n"
            + "  </tr>\n"
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
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"
            + "  log(td1.vAlign);\n"
            + "  log(td2.vAlign);\n"
            + "  log(td3.vAlign);\n"
            + "  set(td1, 'middle');\n"
            + "  set(td2, 8);\n"
            + "  set(td3, 'BOTtom');\n"
            + "  log(td1.vAlign);\n"
            + "  log(td2.vAlign);\n"
            + "  log(td3.vAlign);\n"
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
            + "        var td = document.getElementById('td');\n"
            + "        log(td.bgColor);\n"
            + "        td.bgColor = '#0000aa';\n"
            + "        log(td.bgColor);\n"
            + "        td.bgColor = 'x';\n"
            + "        log(td.bgColor);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <table><tr><td id='td'>a</td></tr></table>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "null", "true", "", "true", "", "true", "blah", "false", "null"})
    public void noWrap() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var td = document.getElementById('td');\n"
            + "        log(td.noWrap);\n"
            + "        log(td.getAttribute('noWrap'));\n"
            + "        td.noWrap = 'nowrap';\n"
            + "        log(td.noWrap);\n"
            + "        log(td.getAttribute('noWrap'));\n"
            + "        td.noWrap = 'x';\n"
            + "        log(td.noWrap);\n"
            + "        log(td.getAttribute('noWrap'));\n"
            + "        td.setAttribute('noWrap', 'blah');\n"
            + "        log(td.noWrap);\n"
            + "        log(td.getAttribute('noWrap'));\n"
            + "        td.noWrap = '';\n"
            + "        log(td.noWrap);\n"
            + "        log(td.getAttribute('noWrap'));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <table><tr><td id='td'>a</td></tr></table>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "blah", "3", ""})
    public void abbr() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var td = document.getElementById('td');\n"
            + "        log(td.abbr);\n"
            + "        td.abbr = 'blah';\n"
            + "        log(td.abbr);\n"
            + "        td.abbr = 3;\n"
            + "        log(td.abbr);\n"
            + "        td.abbr = '';\n"
            + "        log(td.abbr);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <table><tr><td id='td'>a</td></tr></table>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1", "3", "1", "2", "1", "5", "1", "2", "1"},
            IE = {"1", "3", "1", "error", "2", "3", "5", "error", "error", "2", "2", "5"})
    public void colSpan() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <tr>\n"
            + "    <td id='td1'>a</td>\n"
            + "    <td id='td2' colspan='3'>b</td>\n"
            + "    <td id='td3' colspan='foo'>c</td>\n"
            + "  </tr>\n"
            + "</table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.colSpan = value;\n"
            + "    } catch (e) {\n"
            + "      log('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"
            + "  log(td1.colSpan);\n"
            + "  log(td2.colSpan);\n"
            + "  log(td3.colSpan);\n"
            + "  set(td1, '2');\n"
            + "  set(td2, 'blah');\n"
            + "  set(td3, 5);\n"
            + "  log(td1.colSpan);\n"
            + "  log(td2.colSpan);\n"
            + "  log(td3.colSpan);\n"
            + "  set(td1, -1);\n"
            + "  set(td2, 2.2);\n"
            + "  set(td3, 0);\n"
            + "  log(td1.colSpan);\n"
            + "  log(td2.colSpan);\n"
            + "  log(td3.colSpan);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1", "3", "1", "2", "1", "5", "1", "2", "1"},
            CHROME = {"1", "3", "1", "2", "0", "5", "1", "2", "0"},
            EDGE = {"1", "3", "1", "2", "0", "5", "1", "2", "0"},
            IE = {"1", "3", "1", "error", "2", "3", "5", "error", "error", "2", "2", "5"})
    public void rowSpan() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <tr>\n"
            + "    <td id='td1'>a</td>\n"
            + "    <td id='td2' rowspan='3'>b</td>\n"
            + "    <td id='td3' rowspan='foo'>c</td>\n"
            + "  </tr>\n"
            + "  <tr><td>a</td><td>b</td><td>c</td></tr>\n"
            + "  <tr><td>a</td><td>b</td><td>c</td></tr>\n"
            + "  <tr><td>a</td><td>b</td><td>c</td></tr>\n"
            + "  <tr><td>a</td><td>b</td><td>c</td></tr>\n"
            + "  <tr><td>a</td><td>b</td><td>c</td></tr>\n"
            + "</table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.rowSpan = value;\n"
            + "    } catch (e) {\n"
            + "      log('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"
            + "  log(td1.rowSpan);\n"
            + "  log(td2.rowSpan);\n"
            + "  log(td3.rowSpan);\n"
            + "  set(td1, '2');\n"
            + "  set(td2, 'blah');\n"
            + "  set(td3, 5);\n"
            + "  log(td1.rowSpan);\n"
            + "  log(td2.rowSpan);\n"
            + "  log(td3.rowSpan);\n"
            + "  set(td1, -1);\n"
            + "  set(td2, 2.2);\n"
            + "  set(td3, 0);\n"
            + "  log(td1.rowSpan);\n"
            + "  log(td2.rowSpan);\n"
            + "  log(td3.rowSpan);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "blah", "abc , xyz", "3", ""})
    public void axis() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var td = document.getElementById('td');\n"
            + "        log(td.axis);\n"
            + "        td.axis = 'blah';\n"
            + "        log(td.axis);\n"
            + "        td.axis = 'abc , xyz';\n"
            + "        log(td.axis);\n"
            + "        td.axis = 3;\n"
            + "        log(td.axis);\n"
            + "        td.axis = '';\n"
            + "        log(td.axis);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <table><tr><td id='td'>a</td></tr></table>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests some obscure table cell CSS calculations required by the MochiKit tests.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"100,42", "90,36"})
    @NotYetImplemented
    public void cellWidthHeightWithBorderCollapse() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body><table id='t'><tr>\n"
            + "<td id='td1' style='width: 80px; height: 30px; "
                        + "border: 2px solid blue; border-width: 2px 7px 10px 13px; padding: 0px;'>a</td>\n"
            + "</tr></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var t = document.getElementById('t');\n"
            + "  var td1 = document.getElementById('td1');\n"

            + "  log(td1.offsetWidth + ',' + td1.offsetHeight);\n"

            + "  t.style.borderCollapse = 'collapse';\n"
            + "  log(td1.offsetWidth + ',' + td1.offsetHeight);\n"

            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"84,42", "84,42", "100,42", "82,36", "88,36", "90,36"})
    @NotYetImplemented
    public void cellWidthHeightWithBorderCollapseCellsInRow() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body><table id='t'><tr>\n"
            + "<td id='td1' style='width: 80px; height: 30px; border: 2px solid blue; padding: 0px;'>a</td>\n"
            + "<td id='td2' style='width: 80px; height: 30px; "
                        + "border: solid blue; border-width: 2px; padding: 0px;'>a</td>\n"
            + "<td id='td3' style='width: 80px; height: 30px; "
                        + "border: 2px solid blue; border-width: 2px 7px 10px 13px; padding: 0px;'>a</td>\n"
            + "</tr></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var t = document.getElementById('t');\n"
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"

            + "  log(td1.offsetWidth + ',' + td1.offsetHeight);\n"
            + "  log(td2.offsetWidth + ',' + td2.offsetHeight);\n"
            + "  log(td3.offsetWidth + ',' + td3.offsetHeight);\n"

            + "  t.style.borderCollapse = 'collapse';\n"
            + "  log(td1.offsetWidth + ',' + td1.offsetHeight);\n"
            + "  log(td2.offsetWidth + ',' + td2.offsetHeight);\n"
            + "  log(td3.offsetWidth + ',' + td3.offsetHeight);\n"

            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests some obscure table cell CSS calculations required by the MochiKit tests.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"84,34", "84,34", "84,34", "82,32", "82,32", "82,32"})
    @NotYetImplemented
    public void cellWidthHeightWithBorderCollapseSameCellLayout() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body><table id='t'><tr>\n"
            + "<td id='td1' style='width: 80px; height: 30px; border: 2px solid blue; padding: 0px;'>a</td>\n"
            + "<td id='td2' style='width: 80px; height: 30px; border: 2px solid blue; padding: 0px;'>a</td>\n"
            + "<td id='td3' style='width: 80px; height: 30px; border: 2px solid blue; padding: 0px;'>a</td>\n"
            + "</tr></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var t = document.getElementById('t');\n"
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"

            + "  log(td1.offsetWidth + ',' + td1.offsetHeight);\n"
            + "  log(td2.offsetWidth + ',' + td2.offsetHeight);\n"
            + "  log(td3.offsetWidth + ',' + td3.offsetHeight);\n"

            + "  t.style.borderCollapse = 'collapse';\n"
            + "  log(td1.offsetWidth + ',' + td1.offsetHeight);\n"
            + "  log(td2.offsetWidth + ',' + td2.offsetHeight);\n"
            + "  log(td3.offsetWidth + ',' + td3.offsetHeight);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"100px", "200px", "400", "abc", "-5", "100.2", "10%"},
            IE = {"100", "200", "400", "error", "400", "error", "400", "100", "10%"})
    public void width() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function set(e, value) {\n"
            + "        try {\n"
            + "          e.width = value;\n"
            + "        } catch (e) {\n"
            + "          log('error');\n"
            + "        }\n"
            + "      }\n"
            + "      function test() {\n"
            + "        var td = document.getElementById('td');\n"
            + "        set(td, '100px');\n"
            + "        log(td.width);\n"
            + "        td.height = '200px';\n"
            + "        log(td.height);\n"
            + "        set(td, '400');\n"
            + "        log(td.width);\n"
            + "        set(td, 'abc');\n"
            + "        log(td.width);\n"
            + "        set(td, -5);\n"
            + "        log(td.width);\n"
            + "        set(td, 100.2);\n"
            + "        log(td.width);\n"
            + "        set(td, '10%');\n"
            + "        log(td.width);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <table><tr><td id='td'>a</td></tr></table>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void offsetHeight() throws Exception {
        final String html =
            "<html><body>\n"
            + "<table><tr>\n"
            + "<td style='padding:0' id='it'></td>\n"
            + "<td style='display: none'>t</td>\n"
            + "</tr></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var it = document.getElementById('it');\n"
            + "log(it.offsetHeight);\n"
            + "</script>\n"
            + "</body></html>";

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
            + "  <table><tr><td id='tabd1'></td></tr></table>\n"
            + "  <table><tr><td id='tabd2' borderColor='red'></td></tr></table>\n"
            + "  <table><tr><td id='tabd3' borderColor='#123456'></td></tr></table>\n"
            + "  <table><tr><td id='tabd4' borderColor='unknown'></td></tr></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tabd1');\n"
            + "  log(node.borderColor);\n"

            + "  node.borderColor = '#667788';\n"
            + "  log(node.borderColor);\n"

            + "  node.borderColor = 'unknown';\n"
            + "  log(node.borderColor);\n"

            + "  var node = document.getElementById('tabd2');\n"
            + "  log(node.borderColor);\n"
            + "  var node = document.getElementById('tabd3');\n"
            + "  log(node.borderColor);\n"
            + "  var node = document.getElementById('tabd4');\n"
            + "  log(node.borderColor);\n"

            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined"},
            IE = {"", "", "", "", "", ""})
    public void borderColorDark() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table><tr><td id='tabd1'></td></tr></table>\n"
            + "  <table><tr><td id='tabd2' borderColor='red'></td></tr></table>\n"
            + "  <table><tr><td id='tabd3' borderColor='#123456'></td></tr></table>\n"
            + "  <table><tr><td id='tabd4' borderColor='unknown'></td></tr></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tabd1');\n"
            + "  log(node.borderColorDark);\n"

            + "  node.borderColor = '#667788';\n"
            + "  log(node.borderColorDark);\n"

            + "  node.borderColor = 'unknown';\n"
            + "  log(node.borderColorDark);\n"

            + "  var node = document.getElementById('tabd2');\n"
            + "  log(node.borderColorDark);\n"
            + "  var node = document.getElementById('tabd3');\n"
            + "  log(node.borderColorDark);\n"
            + "  var node = document.getElementById('tabd4');\n"
            + "  log(node.borderColorDark);\n"

            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined"},
            IE = {"", "", "", "", "", ""})
    public void borderColorLight() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table><tr><td id='tabd1'></td></tr></table>\n"
            + "  <table><tr><td id='tabd2' borderColor='red'></td></tr></table>\n"
            + "  <table><tr><td id='tabd3' borderColor='#123456'></td></tr></table>\n"
            + "  <table><tr><td id='tabd4' borderColor='unknown'></td></tr></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var node = document.getElementById('tabd1');\n"
            + "  log(node.borderColorLight);\n"

            + "  node.borderColor = '#667788';\n"
            + "  log(node.borderColorLight);\n"

            + "  node.borderColor = 'unknown';\n"
            + "  log(node.borderColorLight);\n"

            + "  var node = document.getElementById('tabd2');\n"
            + "  log(node.borderColorLight);\n"
            + "  var node = document.getElementById('tabd3');\n"
            + "  log(node.borderColorLight);\n"
            + "  var node = document.getElementById('tabd4');\n"
            + "  log(node.borderColorLight);\n"

            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "false", "false"})
    public void offsetHeightParentHidden() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var table = document.getElementById('table1');\n"
            + "      var td = document.getElementById('td1');\n"
            + "      log(td.offsetWidth != 0);\n"
            + "      log(td.offsetHeight != 0);\n"
            + "      td.style.display = 'none';\n"
            + "      log(td.offsetWidth != 0);\n"
            + "      log(td.offsetHeight != 0);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <table id='table1'>\n"
            + "    <tr><td id='td1'>One</td></tr>\n"
            + "  </table>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

}
