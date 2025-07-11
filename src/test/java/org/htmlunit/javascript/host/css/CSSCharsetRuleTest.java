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

import java.net.URL;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.cssparser.dom.CSSCharsetRuleImpl;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CSSCharsetRuleImpl}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
public class CSSCharsetRuleTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void inStyle() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"

            + "<style>@charset \"UTF-8\";</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var rules = document.styleSheets[0].cssRules;\n"
            + "  log(rules.length);\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void inLink() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"

            + "<link rel='stylesheet' href='imp.css'>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var rules = document.styleSheets[0].cssRules;\n"
            + "  log(rules.length);\n"
            + "</script>\n"

            + "</body></html>";

        final String css = "@charset \"UTF-8\";";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "imp.css"), css, MimeType.TEXT_CSS);

        loadPageVerifyTitle2(html);
    }
}
