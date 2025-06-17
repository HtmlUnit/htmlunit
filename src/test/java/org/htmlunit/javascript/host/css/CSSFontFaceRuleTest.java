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
package org.htmlunit.javascript.host.css;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CSSFontFaceRule}.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class CSSFontFaceRuleTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("TypeError")
    public void ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + LOG_TEXTAREA
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "try {\n"
            + "  var rule = new CSSFontFaceRule();\n"
            + "  log(rule);\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object CSSFontFaceRule]", "5",
             "@font-face { font-family: Delicious; src: url(\"Delicious-Bold.otf\"); }"})
    public void simple() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + LOG_TEXTAREA
            + "<style>\n"
            + "  @font-face { font-family: Delicious; src: url('Delicious-Bold.otf'); }\n"
            + "  h3 { font-family: Delicious;  }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "try {\n"
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  log(rule);\n"
            + "  log(rule.type);\n"
            + "  log(rule.cssText);\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("@font-face { font-family: Delicious; src: url(\"//:\"); }")
    public void urlSlashSlashColon() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + LOG_TEXTAREA
            + "<style>\n"
            + "  @font-face { font-family: Delicious; src: url(//:); }\n"
            + "  h3 { font-family: Delicious;  }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "try {\n"
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  log(rule.cssText);\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("@font-face { font-family: Delicious; src: url(\"/:\"); }")
    public void urlSlashColon() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + LOG_TEXTAREA
            + "<style>\n"
            + "  @font-face { font-family: Delicious; src: url(/:); }\n"
            + "  h3 { font-family: Delicious;  }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "try {\n"
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  log(rule.cssText);\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("@font-face { font-family: Delicious; src: url(\"//\"); }")
    public void urlSlashSlash() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + LOG_TEXTAREA
            + "<style>\n"
            + "  @font-face { font-family: Delicious; src: url(//); }\n"
            + "  h3 { font-family: Delicious;  }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "try {\n"
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  log(rule.cssText);\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTextArea2(html);
    }
}
