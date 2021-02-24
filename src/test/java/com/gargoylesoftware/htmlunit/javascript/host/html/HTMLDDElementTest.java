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
 * Unit tests for {@link HTMLDDElement}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLDDElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "null", "nowrap", "null", "x", "null", "x", "blah", "", "blah"},
            IE = {"false", "null", "true", "", "true", "", "true", "blah", "false", "null"})
    public void noWrap() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var dd = document.getElementById('test');\n"
            + "        alert(dd.noWrap);\n"
            + "        alert(dd.getAttribute('noWrap'));\n"
            + "        dd.noWrap = 'nowrap';\n"
            + "        alert(dd.noWrap);\n"
            + "        alert(dd.getAttribute('noWrap'));\n"
            + "        dd.noWrap = 'x';\n"
            + "        alert(dd.noWrap);\n"
            + "        alert(dd.getAttribute('noWrap'));\n"
            + "        dd.setAttribute('noWrap', 'blah');\n"
            + "        alert(dd.noWrap);\n"
            + "        alert(dd.getAttribute('noWrap'));\n"
            + "        dd.noWrap = '';\n"
            + "        alert(dd.noWrap);\n"
            + "        alert(dd.getAttribute('noWrap'));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <dl><dd id='test'>dd</dd></dl>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }
}
