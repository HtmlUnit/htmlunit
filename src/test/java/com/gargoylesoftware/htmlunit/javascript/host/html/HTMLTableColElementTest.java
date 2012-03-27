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

/**
 * Tests for {@link HTMLTableColElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLTableColElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "left", "right", "3", "center", "8", "foo" },
        IE = { "left", "right", "", "error", "error", "center", "right", "" })
    public void align() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <col id='c1' align='left'></col>\n"
            + "  <col id='c2' align='right'></col>\n"
            + "  <col id='c3' align='3'></col>\n"
            + "  <tr>\n"
            + "    <td>a</td>\n"
            + "    <td>b</td>\n"
            + "    <td>c</td>\n"
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
            + "  var c1 = document.getElementById('c1');\n"
            + "  var c2 = document.getElementById('c2');\n"
            + "  var c3 = document.getElementById('c3');\n"
            + "  alert(c1.align);\n"
            + "  alert(c2.align);\n"
            + "  alert(c3.align);\n"
            + "  set(c1, 'center');\n"
            + "  set(c2, '8');\n"
            + "  set(c3, 'foo');\n"
            + "  alert(c1.align);\n"
            + "  alert(c2.align);\n"
            + "  alert(c3.align);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3 = { "p", "po", ".", "u", "8", "U8" },
            FF3_6 = { "p", "po", ".", "u", "8", "U8" },
            FF = { "p", "po", "", "u", "8", "U8" },
            IE = { "", "", "", "u", "8", "U8" })
    public void ch() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <col id='c1' char='p'></col>\n"
            + "  <col id='c2' char='po'></col>\n"
            + "  <col id='c3'></col>\n"
            + "  <tr>\n"
            + "    <td>a</td>\n"
            + "    <td>b</td>\n"
            + "    <td>c</td>\n"
            + "  </tr>\n"
            + "</table>\n"
            + "<script>\n"
            + "  var c1 = document.getElementById('c1');\n"
            + "  var c2 = document.getElementById('c2');\n"
            + "  var c3 = document.getElementById('c3');\n"
            + "  alert(c1.ch);\n"
            + "  alert(c2.ch);\n"
            + "  alert(c3.ch);\n"
            + "  c1.ch = 'u';\n"
            + "  c2.ch = '8';\n"
            + "  c3.ch = 'U8';\n"
            + "  alert(c1.ch);\n"
            + "  alert(c2.ch);\n"
            + "  alert(c3.ch);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3 = { "0", "4", "", "5", "0", "abc" },
            FF3_6 = { "0", "4", "", "5", "0", "abc" },
            FF = { "0", "4", "", "5.2", "-3", "abc" },
            IE = { "", "", "", "5.2", "-3", "abc" })
    public void chOff() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <col id='c1' charoff='0'></col>\n"
            + "  <col id='c2' charoff='4'></col>\n"
            + "  <col id='c3'></col>\n"
            + "  <tr>\n"
            + "    <td>a</td>\n"
            + "    <td>b</td>\n"
            + "    <td>c</td>\n"
            + "  </tr>\n"
            + "</table>\n"
            + "<script>\n"
            + "  var c1 = document.getElementById('c1');\n"
            + "  var c2 = document.getElementById('c2');\n"
            + "  var c3 = document.getElementById('c3');\n"
            + "  alert(c1.chOff);\n"
            + "  alert(c2.chOff);\n"
            + "  alert(c3.chOff);\n"
            + "  c1.chOff = '5.2';\n"
            + "  c2.chOff = '-3';\n"
            + "  c3.chOff = 'abc';\n"
            + "  alert(c1.chOff);\n"
            + "  alert(c2.chOff);\n"
            + "  alert(c3.chOff);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "1", "2", "1", "5", "1", "1" }, IE = { "1", "2", "1", "error", "error", "5", "2", "1" })
    public void span() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <col id='c1' span='0'></col>\n"
            + "  <col id='c2' span='2'></col>\n"
            + "  <col id='c3'></col>\n"
            + "  <tr>\n"
            + "    <td>a</td>\n"
            + "    <td>b</td>\n"
            + "    <td>c</td>\n"
            + "  </tr>\n"
            + "</table>\n"
            + "<script>\n"
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.span = value;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var c1 = document.getElementById('c1');\n"
            + "  var c2 = document.getElementById('c2');\n"
            + "  var c3 = document.getElementById('c3');\n"
            + "  alert(c1.span);\n"
            + "  alert(c2.span);\n"
            + "  alert(c3.span);\n"
            + "  set(c1, '5.2');\n"
            + "  set(c2, '-3');\n"
            + "  set(c3, 'abc');\n"
            + "  alert(c1.span);\n"
            + "  alert(c2.span);\n"
            + "  alert(c3.span);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3 = { "top", "baseline", "3", "middle", "8", "bottom" },
            FF3_6 = { "top", "baseline", "3", "middle", "8", "bottom" },
            FF = { "top", "baseline", "3", "middle", "8", "BOTtom" },
            IE = { "top", "baseline", "top", "error", "middle", "baseline", "bottom" })
    public void vAlign() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <col id='c1' valign='top'></col>\n"
            + "  <col id='c2' valign='baseline'></col>\n"
            + "  <col id='c3' valign='3'></col>\n"
            + "  <tr>\n"
            + "    <td>a</td>\n"
            + "    <td>b</td>\n"
            + "    <td>c</td>\n"
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
            + "  var c1 = document.getElementById('c1');\n"
            + "  var c2 = document.getElementById('c2');\n"
            + "  var c3 = document.getElementById('c3');\n"
            + "  alert(c1.vAlign);\n"
            + "  alert(c2.vAlign);\n"
            + "  alert(c3.vAlign);\n"
            + "  set(c1, 'middle');\n"
            + "  set(c2, 8);\n"
            + "  set(c3, 'BOTtom');\n"
            + "  alert(c1.vAlign);\n"
            + "  alert(c2.vAlign);\n"
            + "  alert(c3.vAlign);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3 = { "50", "75%", "foo", "0", "20", "", "80", "40", "abc", "0", "30%", "33" },
            FF3_6 = { "50", "75%", "foo", "0", "20", "", "80", "40", "abc", "0", "30%", "33" },
            FF = { "50", "75%", "foo", "-7", "20.2", "", "80", "40", "abc", "-10", "30%", "33.3" },
            IE = { "50", "75%", "", "", "20", "", "error", "error", "80", "40", "", "", "30%", "33" })
    public void width() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <col id='c1' width='50'></col>\n"
            + "  <col id='c2' width='75%'></col>\n"
            + "  <col id='c3' width='foo'></col>\n"
            + "  <col id='c4' width='-7'></col>\n"
            + "  <col id='c5' width='20.2'></col>\n"
            + "  <col id='c6'></col>\n"
            + "  <tr>\n"
            + "    <td>a</td>\n"
            + "    <td>b</td>\n"
            + "    <td>c</td>\n"
            + "    <td>d</td>\n"
            + "  </tr>\n"
            + "</table>\n"
            + "<script>\n"
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.width = value;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var c1 = document.getElementById('c1');\n"
            + "  var c2 = document.getElementById('c2');\n"
            + "  var c3 = document.getElementById('c3');\n"
            + "  var c4 = document.getElementById('c4');\n"
            + "  var c5 = document.getElementById('c5');\n"
            + "  var c6 = document.getElementById('c6');\n"
            + "  alert(c1.width);\n"
            + "  alert(c2.width);\n"
            + "  alert(c3.width);\n"
            + "  alert(c4.width);\n"
            + "  alert(c5.width);\n"
            + "  alert(c6.width);\n"
            + "  set(c1, '80');\n"
            + "  set(c2, 40);\n"
            + "  set(c3, 'abc');\n"
            + "  set(c4, -10);\n"
            + "  set(c5, '30%');\n"
            + "  set(c6, 33.3);\n"
            + "  alert(c1.width);\n"
            + "  alert(c2.width);\n"
            + "  alert(c3.width);\n"
            + "  alert(c4.width);\n"
            + "  alert(c5.width);\n"
            + "  alert(c6.width);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "128", FF10 = "128px")
    public void width_px() throws Exception {
        final String html
            = "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    myCol.width = '128px';\n"
            + "    alert(myCol.width);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<table>\n"
            + "  <col id='myCol'></col>\n"
            + "</table>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 2948498.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "string" })
    public void width_null() throws Exception {
        final String html
            = "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    myCol.width = null;\n"
            + "    alert(myCol.width);\n"
            + "    alert(typeof myCol.width);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<table>\n"
            + "  <col id='myCol'></col>\n"
            + "</table>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
