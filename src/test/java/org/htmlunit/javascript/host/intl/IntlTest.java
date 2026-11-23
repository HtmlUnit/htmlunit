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
import org.htmlunit.junit.annotation.HtmlUnitNYI;
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
    @Alerts({"[object Intl.Collator]",
             "function Collator() { [native code] }",
             "[object Intl.Collator]"})
    public void collatorCtor() throws Exception {
        testCtor("new Intl.Collator('de')");
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
    @Alerts({"[object Intl.DateTimeFormat]",
             "function DateTimeFormat() { [native code] }",
             "[object Intl.DateTimeFormat]"})
    public void dateTimeFormatCtor() throws Exception {
        testCtor("new Intl.DateTimeFormat('en-US')");
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
    @Alerts({"de",
             "function Locale() { [native code] }",
             "TypeError"})
    @HtmlUnitNYI(
            CHROME = {"de", "function Locale() { [native code] }", "[object Locale]"},
            EDGE = {"de", "function Locale() { [native code] }", "[object Locale]"},
            FF = {"de", "function Locale() { [native code] }", "[object Locale]"},
            FF_ESR = {"de", "function Locale() { [native code] }", "[object Locale]"})
    public void localeCtor() throws Exception {
        testCtor("new Intl.Locale('de')");
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
    @Alerts({"[object Intl.NumberFormat]",
             "function NumberFormat() { [native code] }",
             "[object Intl.NumberFormat]"})
    public void numberFormatCtor() throws Exception {
        testCtor("new Intl.NumberFormat('de-DE')");
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
    @Alerts(DEFAULT = "TypeError",
            CHROME = "[object Object]",
            EDGE = "[object Object]")
    public void v8BreakIteratorCtor() throws Exception {
        test("new Intl.v8BreakIterator('de-DE')");
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

    private void testCtor(final String string) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      let ctor = " + string + ";\n"
            + "      log(ctor);\n"
            + "      log(ctor.constructor);\n"
            + "      log(ctor.constructor.prototype);\n"
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
    @Alerts({"en-US", "en-US,fr-FR,ja-JP", "zh-Hant-TW", "en-Latn-US",
             "ja-JP", "en-US,fr", "en-US", "RangeError", "ja-JP",
             "TypeError", "", "", "RangeError", "RangeError", ""})
    public void getCanonicalLocales() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try { log(Intl.getCanonicalLocales('EN-us')); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales(['en-US', 'fr-FR', 'ja-JP', 'EN-US'])); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales('zh-hant-tw')); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales('en-latn-us')); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales(new Intl.Locale('ja-JP'))); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales([new Intl.Locale('en-US'), new Intl.Locale('fr')])); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales(String('en-US'))); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales(new String('en-US'))); } catch(e) { logEx(e) }\n"
            + "    try { log(Intl.getCanonicalLocales([new String('ja-JP')])); } catch(e) { logEx(e) }\n"
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

    /**
     * Builds a page that logs the result of the given expression or the name of the exception.
     *
     * @param expression the expression
     * @return the html
     */
    static String localesArgumentHtml(final String expression) {
        return DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try { log(" + expression + "); } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getCanonicalLocalesNoArgument() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getCanonicalLocalesUndefined() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales(undefined)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void getCanonicalLocalesNull() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales(null)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void getCanonicalLocalesEmptyString() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales('')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void getCanonicalLocalesMixedCase() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales('EN-us')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("zh-Hant-TW")
    public void getCanonicalLocalesScript() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales('zh-hant-tw')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void getCanonicalLocalesInvalidTag() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales('en-US!@#')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void getCanonicalLocalesPrivateUse() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales('x-private')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void getCanonicalLocalesInvalidVariant() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales('no-NO-NY')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getCanonicalLocalesNumber() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales(42)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getCanonicalLocalesBoolean() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales(true)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getCanonicalLocalesSymbol() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales(Symbol('de'))"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getCanonicalLocalesEmptyObject() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales({})"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de,ja")
    public void getCanonicalLocalesArrayLike() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales({ length: 2, 0: 'de', 1: 'ja' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void getCanonicalLocalesStringObject() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales(new String('de'))"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de,ja")
    public void getCanonicalLocalesArrayDuplicates() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales(['de', 'DE', 'ja'])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void getCanonicalLocalesArrayNumber() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales(['de', 42])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void getCanonicalLocalesArrayNull() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales(['de', null])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void getCanonicalLocalesArrayUndefined() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales(['de', undefined])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void getCanonicalLocalesArrayInvalidTag() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales(['de', 'en-US!@#'])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ja")
    public void getCanonicalLocalesArrayLocale() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales([new Intl.Locale('ja')])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ja")
    public void getCanonicalLocalesLocale() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales(new Intl.Locale('ja'))"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getCanonicalLocalesEmptyArray() throws Exception {
        loadPageVerifyTitle2(localesArgumentHtml("Intl.getCanonicalLocales([])"));
    }
}
