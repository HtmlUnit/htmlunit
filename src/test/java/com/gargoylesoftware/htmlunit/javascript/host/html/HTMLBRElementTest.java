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
 * Tests for {@link HTMLBRElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLBRElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(
        IE = { "", "left", "all", "right", "none", "", "", "!", "!", "!", "left", "none", "right", "all", "none",
               "", "" },
        FF = { "", "left", "all", "right", "none", "2", "foo", "left", "none", "right", "all", "2", "abc", "8" })
    public void clear() throws Exception {
        final String html
            = "<html><body>\n"
            + "<br id='br1'/>\n"
            + "<br id='br2' clear='left'/>\n"
            + "<br id='br3' clear='all'/>\n"
            + "<br id='br4' clear='right'/>\n"
            + "<br id='br5' clear='none'/>\n"
            + "<br id='br6' clear='2'/>\n"
            + "<br id='br7' clear='foo'/>\n"
            + "<script>\n"
            + "function set(br, value) {\n"
            + "  try {\n"
            + "    br.clear = value;\n"
            + "  } catch(e) {\n"
            + "    alert('!');\n"
            + "  }\n"
            + "}\n"
            + "var br1 = document.getElementById('br1');\n"
            + "var br2 = document.getElementById('br2');\n"
            + "var br3 = document.getElementById('br3');\n"
            + "var br4 = document.getElementById('br4');\n"
            + "var br5 = document.getElementById('br5');\n"
            + "var br6 = document.getElementById('br6');\n"
            + "var br7 = document.getElementById('br7');\n"
            + "alert(br1.clear);\n"
            + "alert(br2.clear);\n"
            + "alert(br3.clear);\n"
            + "alert(br4.clear);\n"
            + "alert(br5.clear);\n"
            + "alert(br6.clear);\n"
            + "alert(br7.clear);\n"
            + "set(br1, 'left');\n"
            + "set(br2, 'none');\n"
            + "set(br3, 'right');\n"
            + "set(br4, 'all');\n"
            + "set(br5, 2);\n"
            + "set(br6, 'abc');\n"
            + "set(br7, '8');\n"
            + "alert(br1.clear);\n"
            + "alert(br2.clear);\n"
            + "alert(br3.clear);\n"
            + "alert(br4.clear);\n"
            + "alert(br5.clear);\n"
            + "alert(br6.clear);\n"
            + "alert(br7.clear);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

}
