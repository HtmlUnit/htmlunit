/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link CSSKeyframesRule}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CSSKeyframesRuleTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "[object CSSKeyframesRule], 7, identifier, 2" },
            FF = { "[object MozCSSKeyframesRule]", "7", "identifier", "2" })
    @NotYetImplemented
    public void simple() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>"
            + "  @keyframes identifier { 0% { top: 0; left: 0; } 100% { top: 100px; left: 100%; }}"
            + "</style>\n"

            + "<script>\n"
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  if (styleSheet.cssRules) {\n"
            + "    var rule = styleSheet.cssRules[0];\n"
            + "    alert(rule);\n"
            + "    alert(rule.type);\n"
            + "    alert(rule.name);\n"
            + "    alert(rule.cssRules.length)\n"
            + "  } else {\n"
            + "    alert('Your browser does not support this example');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
