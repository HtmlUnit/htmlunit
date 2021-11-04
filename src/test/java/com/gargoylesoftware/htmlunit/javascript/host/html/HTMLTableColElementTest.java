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
 * Tests for {@link HTMLTableColElement}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLTableColElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"left", "right", "justify", "char", "center", "wrong", ""},
            IE = {"left", "right", "", "", "center", "", ""})
    @NotYetImplemented(IE)
    public void getAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table>\n"
            + "    <col id='c1' align='left' ></col>\n"
            + "    <col id='c2' align='right' ></col>\n"
            + "    <col id='c3' align='justify' ></col>\n"
            + "    <col id='c4' align='char' ></col>\n"
            + "    <col id='c5' align='center' ></col>\n"
            + "    <col id='c6' align='wrong' ></col>\n"
            + "    <col id='c7' ></col>\n"
            + "  </table>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  for (var i = 1; i <= 7; i++) {\n"
            + "    log(document.getElementById('c' + i).align);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"CenTer", "8", "foo", "left", "right", "justify", "char", "center"},
            IE = {"center", "error", "center", "error", "center", "left", "right",
                  "error", "right", "error", "right", "center"})
    @NotYetImplemented(IE)
    public void setAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table>\n"
            + "    <col id='c1' align='left' ></col>\n"
            + "  </table>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch (e) { log('error'); }\n"
            + "    log(elem.align);\n"
            + "  }\n"

            + "  var elem = document.getElementById('c1');\n"
            + "  setAlign(elem, 'CenTer');\n"

            + "  setAlign(elem, '8');\n"
            + "  setAlign(elem, 'foo');\n"

            + "  setAlign(elem, 'left');\n"
            + "  setAlign(elem, 'right');\n"
            + "  setAlign(elem, 'justify');\n"
            + "  setAlign(elem, 'char');\n"
            + "  setAlign(elem, 'center');\n"
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
            + LOG_TITLE_FUNCTION
            + "  var c1 = document.getElementById('c1');\n"
            + "  var c2 = document.getElementById('c2');\n"
            + "  var c3 = document.getElementById('c3');\n"
            + "  log(c1.ch);\n"
            + "  log(c2.ch);\n"
            + "  log(c3.ch);\n"
            + "  c1.ch = 'u';\n"
            + "  c2.ch = '8';\n"
            + "  c3.ch = 'U8';\n"
            + "  log(c1.ch);\n"
            + "  log(c2.ch);\n"
            + "  log(c3.ch);\n"
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
            + LOG_TITLE_FUNCTION
            + "  var c1 = document.getElementById('c1');\n"
            + "  var c2 = document.getElementById('c2');\n"
            + "  var c3 = document.getElementById('c3');\n"
            + "  log(c1.chOff);\n"
            + "  log(c2.chOff);\n"
            + "  log(c3.chOff);\n"
            + "  c1.chOff = '5.2';\n"
            + "  c2.chOff = '-3';\n"
            + "  c3.chOff = 'abc';\n"
            + "  log(c1.chOff);\n"
            + "  log(c2.chOff);\n"
            + "  log(c3.chOff);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1", "2", "1", "5", "1", "1"},
            IE = {"1", "2", "1", "error", "error", "5", "2", "1"})
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
            + LOG_TITLE_FUNCTION
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.span = value;\n"
            + "    } catch (e) {\n"
            + "      log('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var c1 = document.getElementById('c1');\n"
            + "  var c2 = document.getElementById('c2');\n"
            + "  var c3 = document.getElementById('c3');\n"
            + "  log(c1.span);\n"
            + "  log(c2.span);\n"
            + "  log(c3.span);\n"
            + "  set(c1, '5.2');\n"
            + "  set(c2, '-3');\n"
            + "  set(c3, 'abc');\n"
            + "  log(c1.span);\n"
            + "  log(c2.span);\n"
            + "  log(c3.span);\n"
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
            + LOG_TITLE_FUNCTION
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.vAlign = value;\n"
            + "    } catch (e) {\n"
            + "      log('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var c1 = document.getElementById('c1');\n"
            + "  var c2 = document.getElementById('c2');\n"
            + "  var c3 = document.getElementById('c3');\n"
            + "  log(c1.vAlign);\n"
            + "  log(c2.vAlign);\n"
            + "  log(c3.vAlign);\n"
            + "  set(c1, 'middle');\n"
            + "  set(c2, 8);\n"
            + "  set(c3, 'BOTtom');\n"
            + "  log(c1.vAlign);\n"
            + "  log(c2.vAlign);\n"
            + "  log(c3.vAlign);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"50", "75%", "foo", "-7", "20.2", "", "80", "40", "abc", "-10", "30%", "33.3"},
            IE = {"50", "75%", "", "", "20", "", "error", "error", "80", "40", "", "", "30%", "33"})
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
            + LOG_TITLE_FUNCTION
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.width = value;\n"
            + "    } catch (e) {\n"
            + "      log('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var c1 = document.getElementById('c1');\n"
            + "  var c2 = document.getElementById('c2');\n"
            + "  var c3 = document.getElementById('c3');\n"
            + "  var c4 = document.getElementById('c4');\n"
            + "  var c5 = document.getElementById('c5');\n"
            + "  var c6 = document.getElementById('c6');\n"
            + "  log(c1.width);\n"
            + "  log(c2.width);\n"
            + "  log(c3.width);\n"
            + "  log(c4.width);\n"
            + "  log(c5.width);\n"
            + "  log(c6.width);\n"
            + "  set(c1, '80');\n"
            + "  set(c2, 40);\n"
            + "  set(c3, 'abc');\n"
            + "  set(c4, -10);\n"
            + "  set(c5, '30%');\n"
            + "  set(c6, 33.3);\n"
            + "  log(c1.width);\n"
            + "  log(c2.width);\n"
            + "  log(c3.width);\n"
            + "  log(c4.width);\n"
            + "  log(c5.width);\n"
            + "  log(c6.width);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "128px",
            IE = "128")
    public void width_px() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    myCol.width = '128px';\n"
            + "    log(myCol.width);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<table>\n"
            + "  <col id='myCol'></col>\n"
            + "</table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 2948498.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "string"},
            IE = {"", "string"})
    public void width_null() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    myCol.width = null;\n"
            + "    log(myCol.width);\n"
            + "    log(typeof myCol.width);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<table>\n"
            + "  <col id='myCol'></col>\n"
            + "</table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("<table><colgroup><col></colgroup></table>")
    public void parsing() throws Exception {
        final String html
            = "<html><body><div><table><colgroup><col></colgroup></table></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.body.firstChild.innerHTML);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
