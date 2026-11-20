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
 * Tests for {@link NumberFormat}.
 *
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
public class NumberFormatTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"zh-CN", "latn", "standard", "auto", "decimal", "1", "0", "3", "auto"})
    @HtmlUnitNYI(CHROME = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                           "undefined", "undefined"},
            EDGE = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                    "undefined", "undefined"},
            FF = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                  "undefined", "undefined"},
            FF_ESR = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                      "undefined", "undefined"})
    public void resolvedOptionsValues() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var region1 = new Intl.NumberFormat('zh-CN');\n"
                + "    var options = region1.resolvedOptions();\n"
                + "    log(options.locale);\n"
                + "    log(options.numberingSystem);\n"
                + "    log(options.notation);\n"
                + "    log(options.signDisplay);\n"
                + "    log(options.style);\n"
                + "    log(options.minimumIntegerDigits);\n"
                + "    log(options.minimumFractionDigits);\n"
                + "    log(options.maximumFractionDigits);\n"
                + "    log(options.useGrouping);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Object]")
    public void resolvedOptions() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var region1 = new Intl.NumberFormat('zh-CN');\n"
                + "    var options = region1.resolvedOptions();\n"
                + "    log(options);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "42.247"})
    public void numberFormat() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var numberFormat = Intl.NumberFormat('en');\n"
                + "    log(numberFormat instanceof Intl.NumberFormat);\n"

                + "    log(numberFormat.format(42.2468));\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"en", "en,ja", ""})
    public void supportedLocalesOf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(Intl.NumberFormat.supportedLocalesOf('en'));\n"
                + "    log(Intl.NumberFormat.supportedLocalesOf(['en', 'xx-YY', 'ja']));\n"
                + "    log(Intl.NumberFormat.supportedLocalesOf([]));\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void ctorLocalesNoArgument() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat().format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void ctorLocalesUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat(undefined).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorLocalesNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat(null).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesEmptyString() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('').format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void ctorLocalesMixedCase() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('EN-us').format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void ctorLocalesScript() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('zh-hant-tw').format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesInvalidTag() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en-US!@#').format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesPrivateUse() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('x-private').format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesInvalidVariant() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('no-NO-NY').format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void ctorLocalesNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat(42).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void ctorLocalesBoolean() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat(true).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void ctorLocalesSymbol() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat(Symbol('de')).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void ctorLocalesEmptyObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat({}).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1.234,5")
    public void ctorLocalesArrayLike() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat({ length: 2, 0: 'de', 1: 'ja' }).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesStringObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat(new String('de')).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1.234,5")
    public void ctorLocalesArrayDuplicates() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat(['de', 'DE', 'ja']).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorLocalesArrayNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat(['de', 42]).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorLocalesArrayNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat(['de', null]).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorLocalesArrayUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat(['de', undefined]).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesArrayInvalidTag() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat(['de', 'en-US!@#']).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void ctorLocalesArrayLocale() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat([new Intl.Locale('ja')]).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void ctorLocalesLocale() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat(new Intl.Locale('ja')).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void ctorLocalesEmptyArray() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat([]).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfNoArgument() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf(undefined)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void supportedLocalesOfNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf(null)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfEmptyString() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf('')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void supportedLocalesOfMixedCase() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf('EN-us')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("zh-Hant-TW")
    public void supportedLocalesOfScript() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf('zh-hant-tw')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfInvalidTag() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf('en-US!@#')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfPrivateUse() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf('x-private')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfInvalidVariant() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf('no-NO-NY')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf(42)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfBoolean() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf(true)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfSymbol() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf(Symbol('de'))"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfEmptyObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf({})"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de,ja")
    public void supportedLocalesOfArrayLike() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "Intl.NumberFormat.supportedLocalesOf({ length: 2, 0: 'de', 1: 'ja' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfStringObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf(new String('de'))"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de,ja")
    public void supportedLocalesOfArrayDuplicates() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf(['de', 'DE', 'ja'])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void supportedLocalesOfArrayNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf(['de', 42])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void supportedLocalesOfArrayNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf(['de', null])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void supportedLocalesOfArrayUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf(['de', undefined])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfArrayInvalidTag() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf(['de', 'en-US!@#'])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ja")
    public void supportedLocalesOfArrayLocale() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "Intl.NumberFormat.supportedLocalesOf([new Intl.Locale('ja')])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ja")
    public void supportedLocalesOfLocale() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "Intl.NumberFormat.supportedLocalesOf(new Intl.Locale('ja'))"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfEmptyArray() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.supportedLocalesOf([])"));
    }
}
