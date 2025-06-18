/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HTMLDivElement}.
 *
 * @author Ronald Brill
 */
public class HTMLMarqueeElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "null", "nowrap", "null", "x", "null", "x", "blah", "", "blah"})
    public void noWrap() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var marquee = document.getElementById('test');\n"
            + "        log(marquee.noWrap);\n"
            + "        log(marquee.getAttribute('noWrap'));\n"
            + "        marquee.noWrap = 'nowrap';\n"
            + "        log(marquee.noWrap);\n"
            + "        log(marquee.getAttribute('noWrap'));\n"
            + "        marquee.noWrap = 'x';\n"
            + "        log(marquee.noWrap);\n"
            + "        log(marquee.getAttribute('noWrap'));\n"
            + "        marquee.setAttribute('noWrap', 'blah');\n"
            + "        log(marquee.noWrap);\n"
            + "        log(marquee.getAttribute('noWrap'));\n"
            + "        marquee.noWrap = '';\n"
            + "        log(marquee.noWrap);\n"
            + "        log(marquee.getAttribute('noWrap'));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  <marquee id='test'>marquee</marquee>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }
}
