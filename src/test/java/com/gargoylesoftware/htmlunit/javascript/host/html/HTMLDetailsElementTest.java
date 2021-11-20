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
 * Unit tests for {@link HTMLDetailsElement}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLDetailsElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false", "null", "true", "", "false", "null", "true", "",
                       "true", "", "true", "TrUE", "false", "null"},
            IE = {"undefined", "null", "true", "null", "false", "null", "true", "null",
                  "faLSE", "null", "faLSE", "TrUE", "faLSE", "null"})
    public void open() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var det = document.getElementById('detail');\n"
            + "        log(det.open);\n"
            + "        log(det.getAttribute('open'));\n"

            + "        det.open = true;\n"
            + "        log(det.open);\n"
            + "        log(det.getAttribute('open'));\n"

            + "        det.open = false;\n"
            + "        log(det.open);\n"
            + "        log(det.getAttribute('open'));\n"

            + "        det.open = 'true';\n"
            + "        log(det.open);\n"
            + "        log(det.getAttribute('open'));\n"

            + "        det.open = 'faLSE';\n"
            + "        log(det.open);\n"
            + "        log(det.getAttribute('open'));\n"

            + "        det.setAttribute('open', 'TrUE');\n"
            + "        log(det.open);\n"
            + "        log(det.getAttribute('open'));\n"

            + "        det.removeAttribute('open');\n"
            + "        log(det.open);\n"
            + "        log(det.getAttribute('open'));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <details id='detail'>\n"
            + "      <summary>Automated Status: Operational</summary>\n"
            + "      <p>Velocity: 12m/s</p>\n"
            + "      <p>Direction: North</p>\n"
            + "    </details>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false", "null", "false", "null", "true", "", "true", "blah", "false", "null"},
            IE = {"undefined", "null", "", "null", "abc", "null", "abc", "blah", "abc", "null"})
    public void openString() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var det = document.getElementById('detail');\n"
            + "        log(det.open);\n"
            + "        log(det.getAttribute('open'));\n"

            + "        det.open = '';\n"
            + "        log(det.open);\n"
            + "        log(det.getAttribute('open'));\n"

            + "        det.open = 'abc';\n"
            + "        log(det.open);\n"
            + "        log(det.getAttribute('open'));\n"

            + "        det.setAttribute('open', 'blah');\n"
            + "        log(det.open);\n"
            + "        log(det.getAttribute('open'));\n"

            + "        det.removeAttribute('open');\n"
            + "        log(det.open);\n"
            + "        log(det.getAttribute('open'));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <details id='detail'>\n"
            + "      <summary>Automated Status: Operational</summary>\n"
            + "      <p>Velocity: 12m/s</p>\n"
            + "      <p>Direction: North</p>\n"
            + "    </details>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
