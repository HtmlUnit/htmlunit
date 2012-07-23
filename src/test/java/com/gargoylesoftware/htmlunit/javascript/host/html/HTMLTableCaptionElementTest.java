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
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLTableCaptionElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLTableCaptionElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "left", "right", "3", "center", "8", "foo" },
        IE = { "left", "right", "", "error", "error", "center", "right", "" })
    public void align() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <caption id='c1' align='left'>a</caption>\n"
            + "  <caption id='c2' align='right'>b</caption>\n"
            + "  <caption id='c3' align='3'>c</caption>\n"
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
    @Alerts(FF = { "undefined", "undefined", "undefined", "middle", "8", "BOTtom" },
        IE = { "top", "", "", "error", "error", "top", "", "bottom" })
    public void vAlign() throws Exception {
        final String html
            = "<html><body><table>\n"
            + "  <caption id='c1' valign='top'>a</caption>\n"
            + "  <caption id='c2' valign='baseline'>b</caption>\n"
            + "  <caption id='c3' valign='3'>c</caption>\n"
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

}
