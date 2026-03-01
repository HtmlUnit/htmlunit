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
package org.htmlunit.javascript.regexp.generated;

import org.htmlunit.WebClient;
import org.htmlunit.WebDriverTestCase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Comprehensive test suite for JavaScript regular expression features.
 * This was generated from some LLM's.
 *
 * @author Ronald Brill
 */
public abstract class AbstractRegExpTest extends WebDriverTestCase {

    protected void testEvaluate(final String script) throws Exception {
        final String html =
                "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TEXTAREA_FUNCTION
                + "function test() {\n"
                + "  try {\n"
                + script + "\n"
                + "  } catch(e) { log(e); }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + LOG_TEXTAREA
                + "</body></html>";

        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            final WebClient webClient = ((HtmlUnitDriver) driver).getWebClient();
            webClient.getOptions().setThrowExceptionOnScriptError(false);
        }

        loadPageVerifyTextArea2(html);
    }
}