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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link HTMLTableCellElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLTableCellElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "left", "right", "3", "center", "8", "foo" },
            IE = { "left", "right", "", "error", "error", "center", "right", "" })
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
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.align = value;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"
            + "  alert(td1.align);\n"
            + "  alert(td2.align);\n"
            + "  alert(td3.align);\n"
            + "  set(td1, 'center');\n"
            + "  set(td2, '8');\n"
            + "  set(td3, 'foo');\n"
            + "  alert(td1.align);\n"
            + "  alert(td2.align);\n"
            + "  alert(td3.align);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "p", "po", ".", "u", "8", "U8" }, IE = { "", "", "", "u", "8", "U8" })
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
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"
            + "  alert(td1.ch);\n"
            + "  alert(td2.ch);\n"
            + "  alert(td3.ch);\n"
            + "  td1.ch = 'u';\n"
            + "  td2.ch = '8';\n"
            + "  td3.ch = 'U8';\n"
            + "  alert(td1.ch);\n"
            + "  alert(td2.ch);\n"
            + "  alert(td3.ch);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "0", "4", "", "5", "0", "abc" }, IE = { "", "", "", "5.2", "-3", "abc" })
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
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"
            + "  alert(td1.chOff);\n"
            + "  alert(td2.chOff);\n"
            + "  alert(td3.chOff);\n"
            + "  td1.chOff = '5.2';\n"
            + "  td2.chOff = '-3';\n"
            + "  td3.chOff = 'abc';\n"
            + "  alert(td1.chOff);\n"
            + "  alert(td2.chOff);\n"
            + "  alert(td3.chOff);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "top", "baseline", "3", "middle", "8", "bottom" },
            IE = { "top", "baseline", "top", "error", "middle", "baseline", "bottom" })
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
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.vAlign = value;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"
            + "  alert(td1.vAlign);\n"
            + "  alert(td2.vAlign);\n"
            + "  alert(td3.vAlign);\n"
            + "  set(td1, 'middle');\n"
            + "  set(td2, 8);\n"
            + "  set(td3, 'BOTtom');\n"
            + "  alert(td1.vAlign);\n"
            + "  alert(td2.vAlign);\n"
            + "  alert(td3.vAlign);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "#0000aa", "#000000" })
    public void bgColor() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var td = document.getElementById('td');\n"
            + "        alert(td.bgColor);\n"
            + "        td.bgColor = '#0000aa';\n"
            + "        alert(td.bgColor);\n"
            + "        td.bgColor = 'x';\n"
            + "        alert(td.bgColor);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <table><tr><td id='td'>a</td></tr></table>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"false", "null", "true", "", "true", "", "true", "blah", "false", "null" },
            IE = {"false", "false", "true", "true", "true", "true", "true", "true", "false", "false" })
    public void noWrap() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var td = document.getElementById('td');\n"
            + "        alert(td.noWrap);\n"
            + "        alert(td.getAttribute('noWrap'));\n"
            + "        td.noWrap = 'nowrap';\n"
            + "        alert(td.noWrap);\n"
            + "        alert(td.getAttribute('noWrap'));\n"
            + "        td.noWrap = 'x';\n"
            + "        alert(td.noWrap);\n"
            + "        alert(td.getAttribute('noWrap'));\n"
            + "        td.setAttribute('noWrap', 'blah');\n"
            + "        alert(td.noWrap);\n"
            + "        alert(td.getAttribute('noWrap'));\n"
            + "        td.noWrap = '';\n"
            + "        alert(td.noWrap);\n"
            + "        alert(td.getAttribute('noWrap'));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <table><tr><td id='td'>a</td></tr></table>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "blah", "3", "" })
    public void abbr() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var td = document.getElementById('td');\n"
            + "        alert(td.abbr);\n"
            + "        td.abbr = 'blah';\n"
            + "        alert(td.abbr);\n"
            + "        td.abbr = 3;\n"
            + "        alert(td.abbr);\n"
            + "        td.abbr = '';\n"
            + "        alert(td.abbr);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <table><tr><td id='td'>a</td></tr></table>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "1", "3", "1", "2", "1", "5", "1", "2", "1" },
            IE = { "1", "3", "1", "error", "2", "3", "5", "error", "error", "2", "2", "5" })
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
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.colSpan = value;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"
            + "  alert(td1.colSpan);\n"
            + "  alert(td2.colSpan);\n"
            + "  alert(td3.colSpan);\n"
            + "  set(td1, '2');\n"
            + "  set(td2, 'blah');\n"
            + "  set(td3, 5);\n"
            + "  alert(td1.colSpan);\n"
            + "  alert(td2.colSpan);\n"
            + "  alert(td3.colSpan);\n"
            + "  set(td1, -1);\n"
            + "  set(td2, 2.2);\n"
            + "  set(td3, 0);\n"
            + "  alert(td1.colSpan);\n"
            + "  alert(td2.colSpan);\n"
            + "  alert(td3.colSpan);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "1", "3", "1", "2", "1", "5", "1", "2", "1" },
            IE = { "1", "3", "1", "error", "2", "3", "5", "error", "error", "2", "2", "5" })
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
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.rowSpan = value;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var td1 = document.getElementById('td1');\n"
            + "  var td2 = document.getElementById('td2');\n"
            + "  var td3 = document.getElementById('td3');\n"
            + "  alert(td1.rowSpan);\n"
            + "  alert(td2.rowSpan);\n"
            + "  alert(td3.rowSpan);\n"
            + "  set(td1, '2');\n"
            + "  set(td2, 'blah');\n"
            + "  set(td3, 5);\n"
            + "  alert(td1.rowSpan);\n"
            + "  alert(td2.rowSpan);\n"
            + "  alert(td3.rowSpan);\n"
            + "  set(td1, -1);\n"
            + "  set(td2, 2.2);\n"
            + "  set(td3, 0);\n"
            + "  alert(td1.rowSpan);\n"
            + "  alert(td2.rowSpan);\n"
            + "  alert(td3.rowSpan);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "blah", "abc , xyz", "3", "" })
    public void axis() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var td = document.getElementById('td');\n"
            + "        alert(td.axis);\n"
            + "        td.axis = 'blah';\n"
            + "        alert(td.axis);\n"
            + "        td.axis = 'abc , xyz';\n"
            + "        alert(td.axis);\n"
            + "        td.axis = 3;\n"
            + "        alert(td.axis);\n"
            + "        td.axis = '';\n"
            + "        alert(td.axis);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <table><tr><td id='td'>a</td></tr></table>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests some obscure table cell CSS calculations required by the MochiKit tests.
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts(FF = { "84,30", "84,30", "84,30", "82,30", "82,30", "82,30" })
    public void cellWidthHeightWithBorderCollapse() throws Exception {
        final String html
            = "<html><body><table id='t'><tr>\n"
            + "<td id='td1' style='width: 80px; height: 30px; border: 2px solid blue; padding: 0px;'>a</td>\n"
            + "<td id='td2' style='width: 80px; height: 30px; border: 2px solid blue; padding: 0px;'>a</td>\n"
            + "<td id='td3' style='width: 80px; height: 30px; border: 2px solid blue; padding: 0px;'>a</td>\n"
            + "</tr></table><script>\n"
            + "var t = document.getElementById('t');\n"
            + "var td1 = document.getElementById('td1');\n"
            + "var td2 = document.getElementById('td2');\n"
            + "var td3 = document.getElementById('td3');\n"
            + "alert(td1.offsetWidth + ',' + td1.offsetHeight);\n"
            + "alert(td2.offsetWidth + ',' + td2.offsetHeight);\n"
            + "alert(td3.offsetWidth + ',' + td3.offsetHeight);\n"
            + "t.style.borderCollapse = 'collapse';\n"
            + "alert(td1.offsetWidth + ',' + td1.offsetHeight);\n"
            + "alert(td2.offsetWidth + ',' + td2.offsetHeight);\n"
            + "alert(td3.offsetWidth + ',' + td3.offsetHeight);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "100", "200", "400", "error", "400", "error", "400", "100", "10%" },
            FF = { "100", "200", "400", "abc", "0", "100", "10%" })
    public void width() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function set(e, value) {\n"
            + "        try {\n"
            + "          e.width = value;\n"
            + "        } catch (e) {\n"
            + "          alert('error');\n"
            + "        }\n"
            + "      }\n"
            + "      function test() {\n"
            + "        var td = document.getElementById('td');\n"
            + "        set(td, '100px');\n"
            + "        alert(td.width);\n"
            + "        td.height = '200px';\n"
            + "        alert(td.height);\n"
            + "        set(td, '400');\n"
            + "        alert(td.width);\n"
            + "        set(td, 'abc');\n"
            + "        alert(td.width);\n"
            + "        set(td, -5);\n"
            + "        alert(td.width);\n"
            + "        set(td, 100.2);\n"
            + "        alert(td.width);\n"
            + "        set(td, '10%');\n"
            + "        alert(td.width);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <table><tr><td id='td'>a</td></tr></table>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }
}
