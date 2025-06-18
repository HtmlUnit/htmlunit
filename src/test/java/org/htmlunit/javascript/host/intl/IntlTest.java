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
package org.htmlunit.javascript.host.intl;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Intl}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class IntlTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Intl]")
    public void intl() throws Exception {
        test("Intl");
    }

    private void test(final String string) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(" + string + ");\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function Collator() { [native code] }")
    public void collator() throws Exception {
        test("Intl.Collator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function DateTimeFormat() { [native code] }")
    public void dateTimeFormat() throws Exception {
        test("Intl.DateTimeFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function NumberFormat() { [native code] }")
    public void numberFormat() throws Exception {
        test("Intl.NumberFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function v8BreakIterator() { [native code] }",
            EDGE = "function v8BreakIterator() { [native code] }")
    public void v8BreakIterator() throws Exception {
        test("Intl.v8BreakIterator");
    }

}
