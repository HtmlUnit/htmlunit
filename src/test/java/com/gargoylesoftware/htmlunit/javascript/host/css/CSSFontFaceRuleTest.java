/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link CSSFontFaceRule} rule.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class CSSFontFaceRuleTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "[object CSSFontFaceRule]", "5",
            "@font-face {\n  font-family: \"Delicious\";\n  src: url(\"Delicious-Bold.otf\");\n}" },
            IE = { "exception" },
            IE10 = { "[object CSSFontFaceRule]", "5",
            "@font-face {\r\n\tfont-family: Delicious;\r\n\tsrc: url(Delicious-Bold.otf);\r\n}\r\n" })
    public void simple() throws Exception {
        final String html
            = "<html><body>\n"
            + "<style>"
            + "  @font-face { font-family: Delicious; src: url('Delicious-Bold.otf'); }\n"
            + "  h3 { font-family: Delicious;  }\n"
            + "</style>\n"
            + "<script>\n"
            + "try {\n"
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  alert(rule);\n"
            + "  alert(rule.type);\n"
            + "  alert(rule.cssText);\n"
            + "}\n"
            + "catch (e) { alert('exception'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }
}
