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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FontFaceSet}.
 *
 * @author Ronald Brill
 */
public class FontFaceSetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF = "function FontFaceSet() { [native code] }",
            FF_ESR = "function FontFaceSet() { [native code] }")
    public void window() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(window.FontFaceSet);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object FontFaceSet]")
    public void documentFonts() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.fonts);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("then: ")
    public void load() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (document.fonts) {\n"
            + "    document.fonts.load('12px Arial', 'HtmlUnit').then(function(value) {\n"
            + "        log('then: ' + value);"
            + "      });\n"
            + "  } else {\n"
            + "    log('document.fonts is undefined');\n"
            + "  }"
            + "</script>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError",
                       "false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError"},
            FF = {"false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError",
                  "true", "function FontFaceSet() { [native code] }", "function FontFaceSet() { [native code] }",
                  "[object FontFaceSet]", "function EventTarget() { [native code] }"},
            FF_ESR = {"false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError",
                      "true", "function FontFaceSet() { [native code] }", "function FontFaceSet() { [native code] }",
                      "[object FontFaceSet]", "function EventTarget() { [native code] }"})
    public void windowScope() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html></body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('fontFaceSet' in window);\n"
            + "  log(window.fontFaceSet);\n"
            + "  try { log(fontFaceSet); } catch(e) { logEx(e); };\n"
            + "  try { log(fontFaceSet.prototype); } catch(e) { logEx(e); };\n"
            + "  try { log(fontFaceSet.__proto__); } catch(e) { logEx(e); };\n"

            + "  log('FontFaceSet' in window);\n"
            + "  log(window.FontFaceSet);\n"
            + "  try { log(FontFaceSet); } catch(e) { logEx(e); };\n"
            + "  try { log(FontFaceSet.prototype); } catch(e) { logEx(e); };\n"
            + "  try { log(FontFaceSet.__proto__); } catch(e) { logEx(e); };\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
