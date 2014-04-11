/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

/**
 * Tests for {@link HTMLOptionElement}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
*/
@RunWith(BrowserRunner.class)
public class HTMLOptionElementTest extends SimpleWebTestCase {
    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "value1", "text1", "label1", "value2", "text2", "" })
    public void label() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var s = document.getElementById('testSelect');\n"
            + "  var lastIndex = s.length;\n"
            + "  s.length += 1;\n"
            + "  s[lastIndex].value = 'value2';\n"
            + "  s[lastIndex].text  = 'text2';\n"
            + "  alert(s[0].value);\n"
            + "  alert(s[0].text);\n"
            + "  alert(s[0].label);\n"
            + "  alert(s[1].value);\n"
            + "  alert(s[1].text);\n"
            + "  alert(s[1].label);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <select id='testSelect'>\n"
            + "    <option value='value1' label='label1'>text1</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlSelect select = page.getHtmlElementById("testSelect");
        assertEquals("value1", select.getOption(0).getValueAttribute());
        assertEquals("text1", select.getOption(0).getTextContent());
        assertEquals("label1", select.getOption(0).getLabelAttribute());
        assertEquals("value2", select.getOption(1).getValueAttribute());
        assertEquals("text2", select.getOption(1).getTextContent());
        assertEquals("", select.getOption(1).getLabelAttribute());
    }
}
