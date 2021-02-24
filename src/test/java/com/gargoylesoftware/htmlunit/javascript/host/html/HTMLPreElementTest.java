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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link HTMLPreElement}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLPreElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "number", "100", "77", "number", "123"},
            IE = {"", "string", "100", "77", "string", "123"})
    public void width() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var testPre = document.getElementById('testPre');\n"
            + "        alert(testPre.width);\n"
            + "        alert(typeof testPre.width);\n"
            + "        testPre.width = 100;\n"
            + "        alert(testPre.width);\n"

            + "        var testPre = document.getElementById('testPreWidth');\n"
            + "        alert(testPreWidth.width);\n"
            + "        alert(typeof testPreWidth.width);\n"
            + "        testPreWidth.width = 123;\n"
            + "        alert(testPreWidth.width);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <pre id='testPre'>pre content</pre>\n"
            + "    <pre id='testPreWidth' width='77'>pre content</pre>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                        "undefined", "left", "none", "right", "all", "2", "abc", "8"},
            IE = {"", "left", "all", "right", "none", "", "", "!", "!", "!", "left", "none", "right", "all", "none",
                   "", ""})
    public void clear() throws Exception {
        final String html
            = "<html><body>\n"
            + "<pre id='p1'>pre1</pre>\n"
            + "<pre id='p2' clear='left'>pre2</pre>\n"
            + "<pre id='p3' clear='all'>pre3</pre>\n"
            + "<pre id='p4' clear='right'>pre4</pre>\n"
            + "<pre id='p5' clear='none'>pre5</pre>\n"
            + "<pre id='p6' clear='2'>pre6</pre>\n"
            + "<pre id='p7' clear='foo'>pre7</pre>\n"
            + "<script>\n"
            + "function set(p, value) {\n"
            + "  try {\n"
            + "    p.clear = value;\n"
            + "  } catch(e) {\n"
            + "    alert('!');\n"
            + "  }\n"
            + "}\n"
            + "var p1 = document.getElementById('p1');\n"
            + "var p2 = document.getElementById('p2');\n"
            + "var p3 = document.getElementById('p3');\n"
            + "var p4 = document.getElementById('p4');\n"
            + "var p5 = document.getElementById('p5');\n"
            + "var p6 = document.getElementById('p6');\n"
            + "var p7 = document.getElementById('p7');\n"
            + "alert(p1.clear);\n"
            + "alert(p2.clear);\n"
            + "alert(p3.clear);\n"
            + "alert(p4.clear);\n"
            + "alert(p5.clear);\n"
            + "alert(p6.clear);\n"
            + "alert(p7.clear);\n"
            + "set(p1, 'left');\n"
            + "set(p2, 'none');\n"
            + "set(p3, 'right');\n"
            + "set(p4, 'all');\n"
            + "set(p5, 2);\n"
            + "set(p6, 'abc');\n"
            + "set(p7, '8');\n"
            + "alert(p1.clear);\n"
            + "alert(p2.clear);\n"
            + "alert(p3.clear);\n"
            + "alert(p4.clear);\n"
            + "alert(p5.clear);\n"
            + "alert(p6.clear);\n"
            + "alert(p7.clear);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
