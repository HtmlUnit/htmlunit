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
    @Alerts({"[object CSSKeyframesRule]", "7"})
    public void simple() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @keyframes identifier { 0% { top: 0; left: 0; } 100% { top: 100px; left: 100%; }}\n"
            + "</style>\n"

            + "<script>\n"
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  if (styleSheet.cssRules) {\n"
            + "    var rule = styleSheet.cssRules[0];\n"
            + "    alert(rule);\n"
            + "    alert(rule.type);\n"
            + "  } else {\n"
            + "    alert('Your browser does not support this example');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object CSSKeyframesRule]", "identifier"})
    @NotYetImplemented
    public void name() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @keyframes identifier { 0% { top: 0; left: 0; } 100% { top: 100px; left: 100%; }}\n"
            + "</style>\n"

            + "<script>\n"
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  if (styleSheet.cssRules) {\n"
            + "    var rule = styleSheet.cssRules[0];\n"
            + "    alert(rule);\n"
            + "    alert(rule.name);\n"
            + "  } else {\n"
            + "    alert('Your browser does not support this example');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object CSSRuleList]")
    @NotYetImplemented
    public void cssRules() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @keyframes identifier { 0% { top: 0; left: 0; } 100% { top: 100px; left: 100%; }}\n"
            + "</style>\n"

            + "<script>\n"
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  if (styleSheet.cssRules) {\n"
            + "    var rule = styleSheet.cssRules[0];\n"
            + "    alert(rule.cssRules);\n"
            + "  } else {\n"
            + "    alert('Your browser does not support this example');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
