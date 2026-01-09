/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.css;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StyleMedia}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class StyleMediaTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object StyleMedia]", "screen"},
            FF = "undefined",
            FF_ESR = "undefined")
    public void type() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(window.styleMedia);\n"
            + "    if (window.styleMedia) {\n"
            + "      log(window.styleMedia.type);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "true", "false"},
            FF = {},
            FF_ESR = {})
    public void matchMedium() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.styleMedia) {\n"
            + "      log(window.styleMedia.matchMedium('screen'));\n"
            + "      log(window.styleMedia.matchMedium('SCREEN'));\n"
            + "      log(window.styleMedia.matchMedium('screen, handheld'));\n"
            + "      log(window.styleMedia.matchMedium('handheld'));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "[object StyleMedia]", "[object StyleMedia]", "undefined", "[object StyleMedia]",
                       "false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError"},
            FF = {"false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError",
                  "false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError"},
            FF_ESR = {"false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError",
                      "false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError"})
    public void windowScope() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html></body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('styleMedia' in window);\n"
            + "  log(window.styleMedia);\n"
            + "  try { log(styleMedia); } catch(e) { logEx(e); };\n"
            + "  try { log(styleMedia.prototype); } catch(e) { logEx(e); };\n"
            + "  try { log(styleMedia.__proto__); } catch(e) { logEx(e); };\n"

            + "  log('StyleMedia' in window);\n"
            + "  log(window.StyleMedia);\n"
            + "  try { log(StyleMedia); } catch(e) { logEx(e); };\n"
            + "  try { log(StyleMedia.prototype); } catch(e) { logEx(e); };\n"
            + "  try { log(StyleMedia.__proto__); } catch(e) { logEx(e); };\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
