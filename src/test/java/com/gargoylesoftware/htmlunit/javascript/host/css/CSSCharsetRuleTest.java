/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link CSSImportRule}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class CSSCharsetRuleTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "1", "[object CSSCharsetRule]", "UTF-8" },
            IE = { "0", "undefined" })
    @NotYetImplemented(Browser.IE) // TODO: check which IE versions support what
    public void simple() throws Exception {
        final String html
            = "<html><body>\n"
            + "<style>@charset 'UTF-8';</style>\n"
            + "<script>\n"
            + "var rules = document.styleSheets[0].cssRules;\n"
            + "if (!rules)\n"
            + "  rules = document.styleSheets[0].rules;\n"
            + "alert(rules.length);\n"
            + "alert(rules[0]);\n"
            + "if (rules[0])\n"
            + "  alert(rules[0].encoding);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
