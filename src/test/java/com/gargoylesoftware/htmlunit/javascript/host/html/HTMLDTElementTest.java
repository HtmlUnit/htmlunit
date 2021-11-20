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

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link HTMLDTElement}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLDTElementTest extends WebDriverTestCase {

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
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var dt = document.getElementById('test');\n"
            + "        log(dt.noWrap);\n"
            + "        log(dt.getAttribute('noWrap'));\n"
            + "        dt.noWrap = 'nowrap';\n"
            + "        log(dt.noWrap);\n"
            + "        log(dt.getAttribute('noWrap'));\n"
            + "        dt.noWrap = 'x';\n"
            + "        log(dt.noWrap);\n"
            + "        log(dt.getAttribute('noWrap'));\n"
            + "        dt.setAttribute('noWrap', 'blah');\n"
            + "        log(dt.noWrap);\n"
            + "        log(dt.getAttribute('noWrap'));\n"
            + "        dt.noWrap = '';\n"
            + "        log(dt.noWrap);\n"
            + "        log(dt.getAttribute('noWrap'));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <dl><dt id='test'>dt</dt></dl>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
