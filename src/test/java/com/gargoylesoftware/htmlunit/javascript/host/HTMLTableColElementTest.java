/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HTMLTableColElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLTableColElementTest extends WebTestCase {

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
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "p", "po", ".", "u", "8", "U8" }, IE = { "", "", "", "u", "8", "U8" })
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
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "0", "4", "", "5", "0", "abc" }, IE = { "", "", "", "5.2", "-3", "abc" })
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
        loadPageWithAlerts(html);
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
        loadPageWithAlerts(html);
    }

}
