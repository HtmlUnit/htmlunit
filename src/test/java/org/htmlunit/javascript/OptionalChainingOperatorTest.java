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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for the optional chaining operator (?.).
 *
 * @author Ronald Brill
 */
public class OptionalChainingOperatorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "two"})
    public void basic() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  var o = { a: 'one', b: { c: 'two' }};\n"
            + "  log(o.x?.c);\n"
            + "  log(o.b?.c);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void parse() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  var o = {a: true};\n"
            + "  log(o?.['a']);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
