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
 * Unit tests for {@link HTMLTimeElement}.
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLTimeElementTest extends WebDriverTestCase {

    /**
     * Text attribute is no longer supported.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined"})
    public void text() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var time1 = document.getElementById('time1');\n"
            + "        log(time1.text);\n"
            + "        var time2 = document.getElementById('time1');\n"
            + "        log(time2.text);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <p>start <time id='time1'>20:00</time></p>\n"
            + "    <p>start <time id='time2' datetime='2001-05-15 19:00'>15. Mai</time></p>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "20:40", "2001-05-15 19:00", ""},
            IE = {"undefined", "20:40", "undefined", ""})
    public void datetime() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var time1 = document.getElementById('time1');\n"
            + "        log(time1.dateTime);\n"
            + "        time1.dateTime = '20:40';\n"
            + "        log(time1.dateTime);\n"

            + "        var time2 = document.getElementById('time2');\n"
            + "        log(time2.dateTime);\n"
            + "        time2.dateTime = '';\n"
            + "        log(time2.dateTime);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <p>start <time id='time1'>20:00</time></p>\n"
            + "    <p>start <time id='time2' datetime='2001-05-15 19:00'>15. Mai</time></p>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
