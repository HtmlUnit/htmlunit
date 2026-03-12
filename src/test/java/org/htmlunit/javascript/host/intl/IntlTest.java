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
package org.htmlunit.javascript.host.intl;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Intl}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Lai Quang Duong
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
    @Alerts("function Locale() { [native code] }")
    public void locale() throws Exception {
        test("Intl.Locale");
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"en-US", "en-US,fr-FR,ja-JP",
             "zh-Hant-TW", "en-Latn-US",
             "ja-JP", "en-US,fr", "en-US", "ja-JP",
             "TypeError", "", "", "RangeError", "RangeError", ""})
    public void getCanonicalLocales() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(Intl.getCanonicalLocales('EN-us'));\n"
            + "    log(Intl.getCanonicalLocales(['en-US', 'fr-FR', 'ja-JP', 'EN-US']));\n"
            + "    log(Intl.getCanonicalLocales('zh-hant-tw'));\n"
            + "    log(Intl.getCanonicalLocales('en-latn-us'));\n"
            + "    log(Intl.getCanonicalLocales(new Intl.Locale('ja-JP')));\n"
            + "    log(Intl.getCanonicalLocales([new Intl.Locale('en-US'), new Intl.Locale('fr')]));\n"
            + "    log(Intl.getCanonicalLocales(new String('en-US')));\n"
            + "    log(Intl.getCanonicalLocales([new String('ja-JP')]));\n"
            + "    try { log(Intl.getCanonicalLocales(null)); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales(undefined)); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales([])); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales('')); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales('en-US!@#')); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales(42)); } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
