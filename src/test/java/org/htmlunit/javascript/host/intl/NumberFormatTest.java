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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void resolvedOptionsLocaleNoArgument() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat().resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void resolvedOptionsLocaleMixedCase() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('EN-us').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de-DE")
    public void resolvedOptionsLocaleRegion() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('de-DE').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de")
    public void resolvedOptionsLocaleFallback() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('de-XX').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("zh-Hant-TW")
    public void resolvedOptionsLocaleScript() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('zh-hant-tw').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void resolvedOptionsLocaleUnsupported() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('ban').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de")
    public void resolvedOptionsLocaleUnsupportedFirst() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat(['ban', 'de']).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("he")
    public void resolvedOptionsLocaleDeprecated() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('iw').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ja-JP")
    public void resolvedOptionsLocaleCalendar() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('ja-JP-u-ca-japanese').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en")
    public void resolvedOptionsLocaleCalendarOther() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en-u-ca-buddhist').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("th-TH-u-nu-thai")
    public void resolvedOptionsLocaleNumberingSystem() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('th-TH-u-nu-thai').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en")
    public void resolvedOptionsLocaleHourCycle() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en-u-hc-h23').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en")
    public void resolvedOptionsLocaleHourCycleInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en-u-hc-h25').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de")
    public void resolvedOptionsLocaleCollation() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('de-u-co-phonebk').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en")
    public void resolvedOptionsLocaleNumeric() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en-u-kn-true').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-u-nu-thai")
    public void resolvedOptionsLocaleMultipleExtensions() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en-u-nu-thai-ca-buddhist-co-phonebk').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "1", "''", "true", "false", "1,234.5;2"})
    public void formatFunction() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en');\n"
                + "    try { log(typeof nf.format); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format.length); } catch(e) { logEx(e) }\n"
                + "    try { log('\\'' + nf.format.name + '\\''); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format === nf.format); } catch(e) { logEx(e) }\n"
                + "    try { log('prototype' in nf.format); } catch(e) { logEx(e) }\n"
                + "    try { log([1234.5, 2].map(nf.format).join(';')); } catch(e) { logEx(e) }\n"
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
    @Alerts("TypeError")
    public void formatOnPrototype() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.prototype.format"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void resolvedOptionsOnPrototype() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.prototype.resolvedOptions()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void optionsNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', null).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void optionsUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', undefined).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void optionsNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', 42).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("localeMatcher,numberingSystem,style,currency,currencyDisplay,currencySign,unit,unitDisplay,notation,"
            + "minimumIntegerDigits,minimumFractionDigits,maximumFractionDigits,minimumSignificantDigits,"
            + "maximumSignificantDigits,roundingIncrement,roundingMode,roundingPriority,trailingZeroDisplay,"
            + "compactDisplay,useGrouping,signDisplay")
    public void optionsGetterOrder() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var read = [];\n"
                + "    var options = {};\n"
                + "    var names = [];\n"
                + "    names.push('localeMatcher', 'numberingSystem', 'style');\n"
                + "    names.push('currency', 'currencyDisplay', 'currencySign');\n"
                + "    names.push('unit', 'unitDisplay', 'notation');\n"
                + "    names.push('minimumIntegerDigits', 'minimumFractionDigits', 'maximumFractionDigits');\n"
                + "    names.push('minimumSignificantDigits', 'maximumSignificantDigits', 'roundingIncrement');\n"
                + "    names.push('roundingMode', 'roundingPriority', 'trailingZeroDisplay');\n"
                + "    names.push('compactDisplay', 'useGrouping', 'signDisplay');\n"
                + "    names.forEach(function(p) {\n"
                + "      Object.defineProperty(options, p, { get: function() { read.push(p); } });\n"
                + "    });\n"
                + "    new Intl.NumberFormat('en', options);\n"
                + "    try { log(read.join()); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void localeMatcherInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { localeMatcher: 'invalid' }).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"locale,numberingSystem,style,minimumIntegerDigits,minimumFractionDigits,maximumFractionDigits,"
                 + "useGrouping,notation,signDisplay,roundingIncrement,roundingMode,roundingPriority,"
                 + "trailingZeroDisplay", "en,latn,decimal,1,0,3,auto,standard,auto,1,halfExpand,auto,auto"})
    public void resolvedOptionsDefault() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var o = new Intl.NumberFormat('en').resolvedOptions();\n"
                + "    try { log(Object.keys(o).join()); } catch(e) { logEx(e) }\n"
                + "    try { log(Object.keys(o).map(function(k) { return o[k]; }).join()); } catch(e) { logEx(e) }\n"
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
    @Alerts({"locale,numberingSystem,style,minimumIntegerDigits,minimumFractionDigits,maximumFractionDigits,"
                 + "useGrouping,notation,signDisplay,roundingIncrement,roundingMode,roundingPriority,"
                 + "trailingZeroDisplay", "en,latn,percent,1,0,0,auto,standard,auto,1,halfExpand,auto,auto"})
    public void resolvedOptionsPercent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var o = new Intl.NumberFormat('en', { style: 'percent' }).resolvedOptions();\n"
                + "    try { log(Object.keys(o).join()); } catch(e) { logEx(e) }\n"
                + "    try { log(Object.keys(o).map(function(k) { return o[k]; }).join()); } catch(e) { logEx(e) }\n"
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
    @Alerts({"locale,numberingSystem,style,currency,currencyDisplay,currencySign,minimumIntegerDigits,"
                 + "minimumFractionDigits,maximumFractionDigits,useGrouping,notation,signDisplay,roundingIncrement,"
                 + "roundingMode,roundingPriority,trailingZeroDisplay",
             "en,latn,currency,EUR,symbol,standard,1,2,2,auto,standard,auto,1,halfExpand,auto,auto"})
    public void resolvedOptionsCurrency() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var o = new Intl.NumberFormat('en', { style: 'currency', currency: 'EUR'"
                + " }).resolvedOptions();\n"
                + "    try { log(Object.keys(o).join()); } catch(e) { logEx(e) }\n"
                + "    try { log(Object.keys(o).map(function(k) { return o[k]; }).join()); } catch(e) { logEx(e) }\n"
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
    @Alerts({"locale,numberingSystem,style,unit,unitDisplay,minimumIntegerDigits,minimumFractionDigits,"
                 + "maximumFractionDigits,useGrouping,notation,signDisplay,roundingIncrement,roundingMode,"
                 + "roundingPriority,trailingZeroDisplay",
             "en,latn,unit,meter,short,1,0,3,auto,standard,auto,1,halfExpand,auto,auto"})
    public void resolvedOptionsUnit() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var o = new Intl.NumberFormat('en', { style: 'unit', unit: 'meter' }).resolvedOptions();\n"
                + "    try { log(Object.keys(o).join()); } catch(e) { logEx(e) }\n"
                + "    try { log(Object.keys(o).map(function(k) { return o[k]; }).join()); } catch(e) { logEx(e) }\n"
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
    @Alerts({"locale,numberingSystem,style,minimumIntegerDigits,minimumFractionDigits,maximumFractionDigits,"
                 + "minimumSignificantDigits,maximumSignificantDigits,useGrouping,notation,compactDisplay,signDisplay,"
                 + "roundingIncrement,roundingMode,roundingPriority,trailingZeroDisplay",
             "en,latn,decimal,1,0,0,1,2,min2,compact,short,auto,1,halfExpand,morePrecision,auto"})
    public void resolvedOptionsCompact() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var o = new Intl.NumberFormat('en', { notation: 'compact' }).resolvedOptions();\n"
                + "    try { log(Object.keys(o).join()); } catch(e) { logEx(e) }\n"
                + "    try { log(Object.keys(o).map(function(k) { return o[k]; }).join()); } catch(e) { logEx(e) }\n"
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
    @Alerts({"locale,numberingSystem,style,minimumIntegerDigits,minimumSignificantDigits,maximumSignificantDigits,"
                 + "useGrouping,notation,signDisplay,roundingIncrement,roundingMode,roundingPriority,"
                 + "trailingZeroDisplay", "en,latn,decimal,1,1,3,auto,standard,auto,1,halfExpand,auto,auto"})
    public void resolvedOptionsSignificant() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var o = new Intl.NumberFormat('en', { maximumSignificantDigits: 3 }).resolvedOptions();\n"
                + "    try { log(Object.keys(o).join()); } catch(e) { logEx(e) }\n"
                + "    try { log(Object.keys(o).map(function(k) { return o[k]; }).join()); } catch(e) { logEx(e) }\n"
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
    @Alerts({"locale,numberingSystem,style,minimumIntegerDigits,minimumFractionDigits,maximumFractionDigits,"
                 + "minimumSignificantDigits,maximumSignificantDigits,useGrouping,notation,signDisplay,"
                 + "roundingIncrement,roundingMode,roundingPriority,trailingZeroDisplay",
             "en,latn,decimal,1,0,3,1,21,auto,standard,auto,1,halfExpand,morePrecision,auto"})
    public void resolvedOptionsMorePrecision() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var o = new Intl.NumberFormat('en', { roundingPriority: 'morePrecision'"
                + " }).resolvedOptions();\n"
                + "    try { log(Object.keys(o).join()); } catch(e) { logEx(e) }\n"
                + "    try { log(Object.keys(o).map(function(k) { return o[k]; }).join()); } catch(e) { logEx(e) }\n"
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
    @Alerts({"\u0661\u066C\u0662\u0663\u0664\u066B\u0665", "arab", "en"})
    public void numberingSystemArab() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { numberingSystem: 'arab' });\n"
                + "    try { log(nf.format(1234.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.resolvedOptions().numberingSystem); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts({"\u0E51,\u0E52\u0E53\u0E54.\u0E55", "thai", "en-u-nu-thai"})
    public void numberingSystemExtension() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en-u-nu-thai');\n"
                + "    try { log(nf.format(1234.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.resolvedOptions().numberingSystem); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts({"\u0967,\u0968\u0969\u096A.\u096B", "deva", "en"})
    public void numberingSystemOptionOverridesExtension() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en-u-nu-thai', { numberingSystem: 'deva' });\n"
                + "    try { log(nf.format(1234.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.resolvedOptions().numberingSystem); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1,234.5", "latn"})
    public void numberingSystemUnsupported() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { numberingSystem: 'abcd' });\n"
                + "    try { log(nf.format(1234.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.resolvedOptions().numberingSystem); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void numberingSystemInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { numberingSystem: 'a' }).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"arab", "\u0661\u066C\u0662\u0663\u0664\u066B\u0665"})
    public void numberingSystemDefaultArabic() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('ar-EG');\n"
                + "    try { log(nf.resolvedOptions().numberingSystem); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(1234.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"26%", "-26%", "100%", "12%", "123,450%"})
    public void stylePercent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { style: 'percent' }).format;\n"
                + "    try { log(f(0.256)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0.256)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0.12345)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1234.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"26\u00A0%", "-26\u00A0%"})
    public void stylePercentGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('de', { style: 'percent' }).format;\n"
                + "    try { log(f(0.256)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0.256)); } catch(e) { logEx(e) }\n"
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
    @Alerts("26\u00A0%")
    public void stylePercentFrench() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('fr', { style: 'percent' }).format;\n"
                + "    try { log(f(0.256)); } catch(e) { logEx(e) }\n"
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
    @Alerts("12.35%")
    public void stylePercentFractionDigits() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { style: 'percent', maximumFractionDigits: 2 }).format;\n"
                + "    try { log(f(0.12345)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void styleInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { style: 'invalid' }).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void currencyMissing() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { style: 'currency' }).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void currencyInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { style: 'currency', currency: 'US' }).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("USD")
    public void currencyLowerCase() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { style: 'currency', currency: 'usd' }).resolvedOptions().currency"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,234.5")
    public void currencyWithoutStyle() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { currency: 'USD' }).format(1234.5)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"$1,234.57", "-$1,234.57", "$0.00"})
    public void currencyUsd() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1234.567)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1.234,57\u00A0$", "-1.234,57\u00A0$"})
    public void currencyUsdGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('de-DE', { style: 'currency', currency: 'USD' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1.234,57\u00A0\u20AC", "-1.234,57\u00A0\u20AC"})
    public void currencyEurGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('de-DE', { style: 'currency', currency: 'EUR' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts("1\u202F234,57\u00A0\u20AC")
    public void currencyEurFrench() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts("\uFFE51,235")
    public void currencyJpy() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts("\u00A51,235")
    public void currencyJpyEnglish() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'JPY' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts("KWD\u00A01,234.568")
    public void currencyKwd() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'KWD' }).format;\n"
                + "    try { log(f(1234.5678)); } catch(e) { logEx(e) }\n"
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
    @Alerts("XYZ\u00A01,234.57")
    public void currencyUnknown() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'XYZ' }).format;\n"
                + "    try { log(f(1234.5678)); } catch(e) { logEx(e) }\n"
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
    @Alerts("CHF\u00A01'234.57")
    public void currencyChf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('de-CH', { style: 'currency', currency: 'CHF' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts("US$1,234.57")
    public void currencyUsdCanada() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en-CA', { style: 'currency', currency: 'USD' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"USD\u00A01,234.57", "-USD\u00A01,234.57"})
    public void currencyDisplayCode() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en-US',\n"
                + "        { style: 'currency', currency: 'USD', currencyDisplay: 'code' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts("1.234,57\u00A0USD")
    public void currencyDisplayCodeGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('de-DE',\n"
                + "        { style: 'currency', currency: 'USD', currencyDisplay: 'code' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1,234.57 US dollars", "1.00 US dollars"})
    public void currencyDisplayName() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en-US',\n"
                + "        { style: 'currency', currency: 'USD', currencyDisplay: 'name' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
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
    @Alerts("$1,234.57")
    public void currencyDisplayNarrowSymbol() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en-CA',\n"
                + "        { style: 'currency', currency: 'USD', currencyDisplay: 'narrowSymbol' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void currencyDisplayInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { style: 'currency', currency: 'USD', currencyDisplay: 'x' }).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"$1,234.57", "($1,234.57)"})
    public void currencySignAccounting() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en-US',\n"
                + "        { style: 'currency', currency: 'USD', currencySign: 'accounting' }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts("-1.234,57\u00A0\u20AC")
    public void currencySignAccountingGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('de-DE',\n"
                + "        { style: 'currency', currency: 'EUR', currencySign: 'accounting' }).format;\n"
                + "    try { log(f(-1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void currencySignInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { style: 'currency', currency: 'USD', currencySign: 'x' }).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("$1,235")
    public void currencyFractionDigits() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en-US',\n"
                + "        { style: 'currency', currency: 'USD', maximumFractionDigits: 0 }).format;\n"
                + "    try { log(f(1234.567)); } catch(e) { logEx(e) }\n"
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
    @Alerts("TypeError")
    public void unitMissing() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', { style: 'unit' }).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void unitInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { style: 'unit', unit: 'foo' }).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void unitInvalidPer() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { style: 'unit', unit: 'meter-per-foo' }).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void unitWithoutStyle() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', { unit: 'meter' }).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"50 km/h", "1 km/h", "-1.5 km/h"})
    public void unitKilometerPerHourShort() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { style: 'unit', unit: 'kilometer-per-hour', unitDisplay: 'short' }).format;\n"
                + "    try { log(f(50)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1 day", "2 days", "1.5 days"})
    public void unitDayShort() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { style: 'unit', unit: 'day', unitDisplay: 'short' }).format;\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"50km/h", "1km/h", "-1.5km/h"})
    public void unitKilometerPerHourNarrow() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { style: 'unit', unit: 'kilometer-per-hour', unitDisplay: 'narrow' }).format;\n"
                + "    try { log(f(50)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1d", "2d", "1.5d"})
    public void unitDayNarrow() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { style: 'unit', unit: 'day', unitDisplay: 'narrow' }).format;\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"50 kilometers per hour", "1 kilometer per hour", "-1.5 kilometers per hour"})
    public void unitKilometerPerHourLong() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { style: 'unit', unit: 'kilometer-per-hour', unitDisplay: 'long' }).format;\n"
                + "    try { log(f(50)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1 day", "2 days", "1.5 days"})
    public void unitDayLong() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { style: 'unit', unit: 'day', unitDisplay: 'long' }).format;\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts("16 L")
    public void unitLiter() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { style: 'unit', unit: 'liter' }).format;\n"
                + "    try { log(f(16)); } catch(e) { logEx(e) }\n"
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
    @Alerts("21.5\u00B0C")
    public void unitCelsius() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { style: 'unit', unit: 'celsius' }).format;\n"
                + "    try { log(f(21.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts("50%")
    public void unitPercent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { style: 'unit', unit: 'percent' }).format;\n"
                + "    try { log(f(50)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1 foot", "2 feet"})
    public void unitFoot() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { style: 'unit', unit: 'foot', unitDisplay: 'long' }).format;\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2)); } catch(e) { logEx(e) }\n"
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
    @Alerts("5 kB/s")
    public void unitCompound() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { style: 'unit', unit: 'kilobyte-per-second' }).format;\n"
                + "    try { log(f(5)); } catch(e) { logEx(e) }\n"
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
    @Alerts("5 kilobytes per second")
    public void unitCompoundLong() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { style: 'unit', unit: 'kilobyte-per-second', unitDisplay: 'long' }).format;\n"
                + "    try { log(f(5)); } catch(e) { logEx(e) }\n"
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
    @Alerts("1.234,5 km")
    public void unitGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('de', { style: 'unit', unit: 'kilometer' }).format;\n"
                + "    try { log(f(1234.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void unitDisplayInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { style: 'unit', unit: 'meter', unitDisplay: 'x' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3.142E4", "1.23E-3", "0E0", "-1.234E3", "1E0"})
    public void notationScientific() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { notation: 'scientific' }).format;\n"
                + "    try { log(f(31415.9265)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0.00123)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"31.416E3", "1.23E-3", "1.235E6"})
    public void notationEngineering() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { notation: 'engineering' }).format;\n"
                + "    try { log(f(31415.9265)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0.00123)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1234567)); } catch(e) { logEx(e) }\n"
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
    @Alerts("3,142E4")
    public void notationScientificGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('de', { notation: 'scientific' }).format;\n"
                + "    try { log(f(31415.9265)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"31K", "1.2K", "1.5M", "1M", "999", "1.2", "-1.2K", "1T", "0"})
    public void notationCompactShort() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { notation: 'compact' }).format;\n"
                + "    try { log(f(31415.9)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1500000)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(999999)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(999)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1.234)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1e12)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"31 thousand", "1 thousand", "2 billion"})
    public void notationCompactLong() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { notation: 'compact', compactDisplay: 'long' }).format;\n"
                + "    try { log(f(31415.9)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1000)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2e9)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"31.416", "1,5\u00A0Mio."})
    public void notationCompactGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('de', { notation: 'compact' }).format;\n"
                + "    try { log(f(31415.9)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1500000)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"31 Tausend", "2 Milliarden"})
    public void notationCompactGermanLong() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('de',\n"
                + "        { notation: 'compact', compactDisplay: 'long' }).format;\n"
                + "    try { log(f(31415.9)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2e9)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"3.1\u4E07", "150\u4E07"})
    public void notationCompactJapanese() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('ja', { notation: 'compact' }).format;\n"
                + "    try { log(f(31415.9)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1500000)); } catch(e) { logEx(e) }\n"
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
    @Alerts("1.23K")
    public void notationCompactFractionDigits() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { notation: 'compact', maximumFractionDigits: 2 }).format;\n"
                + "    try { log(f(1234)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void notationInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { notation: 'invalid' }).format(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void compactDisplayInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { notation: 'compact', compactDisplay: 'x' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"005", "12,345", "001.5"})
    public void minimumIntegerDigits() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { minimumIntegerDigits: 3 }).format;\n"
                + "    try { log(f(5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(12345)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void minimumIntegerDigitsZero() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', { minimumIntegerDigits: 0 })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void minimumIntegerDigitsTooLarge() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', { minimumIntegerDigits: 22 })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"5.00", "1.235"})
    public void minimumFractionDigits() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { minimumFractionDigits: 2 }).format;\n"
                + "    try { log(f(5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1.23456)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"5", "1.3", "1.4"})
    public void maximumFractionDigits() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { maximumFractionDigits: 1 }).format;\n"
                + "    try { log(f(5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1.25)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1.35)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"2", "3", "-2"})
    public void maximumFractionDigitsZero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { maximumFractionDigits: 0 }).format;\n"
                + "    try { log(f(1.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"5.0", "1.2346"})
    public void fractionDigitsBoth() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { minimumFractionDigits: 1, maximumFractionDigits: 4 }).format;\n"
                + "    try { log(f(5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1.234567)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void fractionDigitsMinGreaterMax() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { minimumFractionDigits: 3, maximumFractionDigits: 1 })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void fractionDigitsTooLarge() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { maximumFractionDigits: 101 })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void fractionDigitsNaN() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { maximumFractionDigits: 'x' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"5", "5"})
    public void fractionDigitsMinOnly() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var o = new Intl.NumberFormat('en', { minimumFractionDigits: 5 })\n"
                + "      .resolvedOptions();\n"
                + "    try { log(o.minimumFractionDigits); } catch(e) { logEx(e) }\n"
                + "    try { log(o.maximumFractionDigits); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1", "1"})
    public void fractionDigitsMaxOnly() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var o = new Intl.NumberFormat('en', { style: 'currency', currency: 'USD',"
                + " maximumFractionDigits: 1 })\n"
                + "      .resolvedOptions();\n"
                + "    try { log(o.minimumFractionDigits); } catch(e) { logEx(e) }\n"
                + "    try { log(o.maximumFractionDigits); } catch(e) { logEx(e) }\n"
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
    @Alerts({"31,400", "0.00123", "1.5"})
    public void significantDigits() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { maximumSignificantDigits: 3 }).format;\n"
                + "    try { log(f(31415.9)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0.0012345)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1.5000", "123,456.789"})
    public void significantDigitsMinimum() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { minimumSignificantDigits: 5 }).format;\n"
                + "    try { log(f(1.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(123456.789)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void significantDigitsZero() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { maximumSignificantDigits: 0 })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void significantDigitsTooLarge() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { minimumSignificantDigits: 22 })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void significantDigitsMinGreaterMax() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { minimumSignificantDigits: 3, maximumSignificantDigits: 2 })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1.2", "120"})
    public void significantAndFractionDigits() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { maximumSignificantDigits: 2, maximumFractionDigits: 3 }).format;\n"
                + "    try { log(f(1.23456)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(123.456)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1.23", "123.46", "0.0012"})
    public void roundingPriorityMorePrecision() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { maximumSignificantDigits: 2, maximumFractionDigits: 2, roundingPriority:"
                + " 'morePrecision' }).format;\n"
                + "    try { log(f(1.23456)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(123.456)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0.00123)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1.2", "120", "0"})
    public void roundingPriorityLessPrecision() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { maximumSignificantDigits: 2, maximumFractionDigits: 2, roundingPriority:"
                + " 'lessPrecision' }).format;\n"
                + "    try { log(f(1.23456)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(123.456)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0.00123)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void roundingPriorityInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', { roundingPriority: 'x' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1.25", "1.25", "-1.25"})
    public void roundingIncrement() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { roundingIncrement: 5, minimumFractionDigits: 2, maximumFractionDigits: 2 }).format;\n"
                + "    try { log(f(1.234)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1.226)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1.226)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1,225", "1,225"})
    public void roundingIncrementInteger() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { roundingIncrement: 25, maximumFractionDigits: 0 }).format;\n"
                + "    try { log(f(1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1220)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void roundingIncrementInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', { roundingIncrement: 3 })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void roundingIncrementSignificant() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { roundingIncrement: 5, maximumSignificantDigits: 2 })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void roundingIncrementFractionMismatch() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.NumberFormat('en', { roundingIncrement: 5, minimumFractionDigits: 1,"
                + " maximumFractionDigits: 2 })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            FF = {"0", "0", "5"},
            FF_ESR = {"0", "0", "5"})
    // chrome and edge throw a RangeError (the default maximumFractionDigits are not adjusted)
    @HtmlUnitNYI(CHROME = {"0", "0", "5"},
            EDGE = {"0", "0", "5"})
    public void roundingIncrementDefaultFraction() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var o = new Intl.NumberFormat('en', { roundingIncrement: 5 }).resolvedOptions();\n"
                + "    try { log(o.minimumFractionDigits); } catch(e) { logEx(e) }\n"
                + "    try { log(o.maximumFractionDigits); } catch(e) { logEx(e) }\n"
                + "    try { log(o.roundingIncrement); } catch(e) { logEx(e) }\n"
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
    @Alerts({"3", "-2", "4", "3", "-2"})
    public void roundingModeCeil() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { maximumFractionDigits: 0, roundingMode: 'ceil' }).format;\n"
                + "    try { log(f(2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(3.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2.4)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.6)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"2", "-3", "3", "2", "-3"})
    public void roundingModeFloor() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { maximumFractionDigits: 0, roundingMode: 'floor' }).format;\n"
                + "    try { log(f(2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(3.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2.4)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.6)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"3", "-3", "4", "3", "-3"})
    public void roundingModeExpand() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { maximumFractionDigits: 0, roundingMode: 'expand' }).format;\n"
                + "    try { log(f(2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(3.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2.4)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.6)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"2", "-2", "3", "2", "-2"})
    public void roundingModeTrunc() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { maximumFractionDigits: 0, roundingMode: 'trunc' }).format;\n"
                + "    try { log(f(2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(3.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2.4)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.6)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"3", "-2", "4", "2", "-3"})
    public void roundingModeHalfCeil() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { maximumFractionDigits: 0, roundingMode: 'halfCeil' }).format;\n"
                + "    try { log(f(2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(3.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2.4)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.6)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"2", "-3", "3", "2", "-3"})
    public void roundingModeHalfFloor() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { maximumFractionDigits: 0, roundingMode: 'halfFloor' }).format;\n"
                + "    try { log(f(2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(3.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2.4)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.6)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"3", "-3", "4", "2", "-3"})
    public void roundingModeHalfExpand() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { maximumFractionDigits: 0, roundingMode: 'halfExpand' }).format;\n"
                + "    try { log(f(2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(3.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2.4)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.6)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"2", "-2", "3", "2", "-3"})
    public void roundingModeHalfTrunc() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { maximumFractionDigits: 0, roundingMode: 'halfTrunc' }).format;\n"
                + "    try { log(f(2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(3.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2.4)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.6)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"2", "-2", "4", "2", "-3"})
    public void roundingModeHalfEven() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { maximumFractionDigits: 0, roundingMode: 'halfEven' }).format;\n"
                + "    try { log(f(2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(3.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(2.4)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-2.6)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void roundingModeInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', { roundingMode: 'x' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"5", "5.50", "5.001"})
    public void trailingZeroDisplayStripIfInteger() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { minimumFractionDigits: 2, trailingZeroDisplay: 'stripIfInteger' }).format;\n"
                + "    try { log(f(5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(5.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(5.001)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void trailingZeroDisplayInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', { trailingZeroDisplay: 'x' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "1234", "12345"})
    public void useGroupingFalse() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { useGrouping: false });\n"
                + "    try { log(nf.resolvedOptions().useGrouping); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(12345)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"always", "1,234", "12,345"})
    public void useGroupingTrue() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { useGrouping: true });\n"
                + "    try { log(nf.resolvedOptions().useGrouping); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(12345)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"always", "1,234", "12,345"})
    public void useGroupingAlways() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { useGrouping: 'always' });\n"
                + "    try { log(nf.resolvedOptions().useGrouping); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(12345)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"auto", "1,234", "12,345"})
    public void useGroupingAuto() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { useGrouping: 'auto' });\n"
                + "    try { log(nf.resolvedOptions().useGrouping); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(12345)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"min2", "1234", "12,345"})
    public void useGroupingMin2() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { useGrouping: 'min2' });\n"
                + "    try { log(nf.resolvedOptions().useGrouping); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(12345)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"auto", "1,234", "12,345"})
    public void useGroupingStringTrue() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { useGrouping: 'true' });\n"
                + "    try { log(nf.resolvedOptions().useGrouping); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(12345)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"auto", "1,234", "12,345"})
    public void useGroupingStringFalse() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { useGrouping: 'false' });\n"
                + "    try { log(nf.resolvedOptions().useGrouping); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(12345)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"false", "1234", "12345"})
    public void useGroupingZero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { useGrouping: 0 });\n"
                + "    try { log(nf.resolvedOptions().useGrouping); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(12345)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"false", "1234", "12345"})
    public void useGroupingEmptyString() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { useGrouping: '' });\n"
                + "    try { log(nf.resolvedOptions().useGrouping); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(12345)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void useGroupingInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', { useGrouping: 'x' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"min2", "1235T"})
    public void useGroupingCompactDefault() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { notation: 'compact' });\n"
                + "    try { log(nf.resolvedOptions().useGrouping); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(1234567890000000)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1234", "12.345"})
    public void useGroupingSpanish() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('es');\n"
                + "    try { log(nf.format(1234)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.format(12345)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1", "-1", "0", "-0", "NaN", "-0"})
    public void signDisplayAuto() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { signDisplay: 'auto' }).format;\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(NaN)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0.0001)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1", "1", "0", "0", "NaN", "0"})
    public void signDisplayNever() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { signDisplay: 'never' }).format;\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(NaN)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0.0001)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"+1", "-1", "+0", "-0", "+NaN", "-0"})
    public void signDisplayAlways() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { signDisplay: 'always' }).format;\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(NaN)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0.0001)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"+1", "-1", "0", "0", "NaN", "0"})
    public void signDisplayExceptZero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { signDisplay: 'exceptZero' }).format;\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(NaN)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0.0001)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1", "-1", "0", "0", "NaN", "0"})
    public void signDisplayNegative() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { signDisplay: 'negative' }).format;\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(NaN)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0.0001)); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void signDisplayInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en', { signDisplay: 'x' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"+$1.00", "-$1.00"})
    public void signDisplayAlwaysCurrency() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { style: 'currency', currency: 'USD', signDisplay: 'always' }).format;\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"+10\u00A0%", "-10\u00A0%"})
    public void signDisplayAlwaysPercent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('de', { style: 'percent', signDisplay: 'always' }).format;\n"
                + "    try { log(f(0.1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0.1)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"+$1.00", "($1.00)", "$0.00"})
    public void signDisplayExceptZeroAccounting() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en',\n"
                + "        { style: 'currency', currency: 'USD', currencySign: 'accounting', signDisplay:"
                + " 'exceptZero' }).format;\n"
                + "    try { log(f(1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-1)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(0)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"NaN", "0", "1", "0", "42", "0", "5", "NaN", "7"})
    public void formatValues() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', {}).format;\n"
                + "    try { log(f(undefined)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(null)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(true)); } catch(e) { logEx(e) }\n"
                + "    try { log(f('')); } catch(e) { logEx(e) }\n"
                + "    try { log(f('  42  ')); } catch(e) { logEx(e) }\n"
                + "    try { log(f([])); } catch(e) { logEx(e) }\n"
                + "    try { log(f([5])); } catch(e) { logEx(e) }\n"
                + "    try { log(f({})); } catch(e) { logEx(e) }\n"
                + "    try { log(f({ valueOf: function() { return 7; } })); } catch(e) { logEx(e) }\n"
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
    @Alerts({"NaN", "\u221E", "-\u221E", "-0", "1,000,000,000,000,000,000,000", "0",
             "179,769,313,486,231,570,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,"
                 + "000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,"
                 + "000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,"
                 + "000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,"
                 + "000,000,000,000,000"})
    public void formatSpecialNumbers() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', {}).format;\n"
                + "    try { log(f(NaN)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(Infinity)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-Infinity)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1e21)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(1e-7)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(Number.MAX_VALUE)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1,234.5678", "12,345,678,901,234,567,890.123", "-0", "16", "\u221E", "1,000", "NaN"})
    public void formatStrings() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { maximumFractionDigits: 20 }).format;\n"
                + "    try { log(f('1234.5678')); } catch(e) { logEx(e) }\n"
                + "    try { log(f('12345678901234567890.123')); } catch(e) { logEx(e) }\n"
                + "    try { log(f('-0')); } catch(e) { logEx(e) }\n"
                + "    try { log(f('0x10')); } catch(e) { logEx(e) }\n"
                + "    try { log(f('Infinity')); } catch(e) { logEx(e) }\n"
                + "    try { log(f('1e3')); } catch(e) { logEx(e) }\n"
                + "    try { log(f('abc')); } catch(e) { logEx(e) }\n"
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
    @Alerts({"12,345,678,901,234,567,890", "-5"})
    public void formatBigInt() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', {}).format;\n"
                + "    try { log(f(BigInt('12345678901234567890'))); } catch(e) { logEx(e) }\n"
                + "    try { log(f(BigInt(-5))); } catch(e) { logEx(e) }\n"
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
    @Alerts({"NaN%", "\u221E%"})
    public void formatSpecialNumbersPercent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('en', { style: 'percent' }).format;\n"
                + "    try { log(f(NaN)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(Infinity)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1.234.567,891", "-0,5"})
    public void formatGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('de', {}).format;\n"
                + "    try { log(f(1234567.891)); } catch(e) { logEx(e) }\n"
                + "    try { log(f(-0.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts("\u22121\u00A0234,5")
    public void formatSwedish() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var f = new Intl.NumberFormat('sv', {}).format;\n"
                + "    try { log(f(-1234.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"function", "1", "formatToParts"})
    public void formatToPartsFunction() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try { log(typeof Intl.NumberFormat.prototype.formatToParts); } catch(e) { logEx(e) }\n"
                + "    try { log(Intl.NumberFormat.prototype.formatToParts.length); } catch(e) { logEx(e) }\n"
                + "    try { log(Intl.NumberFormat.prototype.formatToParts.name); } catch(e) { logEx(e) }\n"
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
    @Alerts("TypeError")
    public void formatToPartsOnPrototype() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.prototype.formatToParts(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "3", "type,value", "string"})
    public void formatToPartsResult() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var r = new Intl.NumberFormat('en').formatToParts(1.5);\n"
                + "    try { log(Array.isArray(r)); } catch(e) { logEx(e) }\n"
                + "    try { log(r.length); } catch(e) { logEx(e) }\n"
                + "    try { log(Object.keys(r[0]).join()); } catch(e) { logEx(e) }\n"
                + "    try { log(typeof r[0].value); } catch(e) { logEx(e) }\n"
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
    @Alerts({"minusSign:-|integer:1|group:,|integer:234|group:,|integer:567|decimal:.|fraction:891", "integer:0",
             "integer:1|group:,|integer:234"})
    public void formatToPartsDecimal() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', {});\n"
                + "    try { log(parts(nf.formatToParts(-1234567.891))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(0))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(1234))); } catch(e) { logEx(e) }\n"
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
    @Alerts("minusSign:-|integer:1|group:.|integer:234|group:.|integer:567|decimal:,|fraction:891")
    public void formatToPartsGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('de', {});\n"
                + "    try { log(parts(nf.formatToParts(-1234567.891))); } catch(e) { logEx(e) }\n"
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
    @Alerts("integer:1234567")
    public void formatToPartsNoGrouping() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { useGrouping: false });\n"
                + "    try { log(parts(nf.formatToParts(1234567))); } catch(e) { logEx(e) }\n"
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
    @Alerts("integer:005")
    public void formatToPartsMinimumIntegerDigits() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { minimumIntegerDigits: 3 });\n"
                + "    try { log(parts(nf.formatToParts(5))); } catch(e) { logEx(e) }\n"
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
    @Alerts({"integer:25|percentSign:%", "minusSign:-|integer:25|percentSign:%"})
    public void formatToPartsPercent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'percent' });\n"
                + "    try { log(parts(nf.formatToParts(0.25))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(-0.25))); } catch(e) { logEx(e) }\n"
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
    @Alerts("integer:25|literal:\u00A0|percentSign:%")
    public void formatToPartsPercentGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('de', { style: 'percent' });\n"
                + "    try { log(parts(nf.formatToParts(0.25))); } catch(e) { logEx(e) }\n"
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
    @Alerts({"currency:$|integer:1|group:,|integer:234|decimal:.|fraction:50",
             "minusSign:-|currency:$|integer:1|group:,|integer:234|decimal:.|fraction:50"})
    public void formatToPartsCurrency() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'currency', currency: 'USD' });\n"
                + "    try { log(parts(nf.formatToParts(1234.5))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(-1234.5))); } catch(e) { logEx(e) }\n"
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
    @Alerts("minusSign:-|integer:1|group:.|integer:234|decimal:,|fraction:50|literal:\u00A0|currency:\u20AC")
    public void formatToPartsCurrencyGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('de', { style: 'currency', currency: 'EUR' });\n"
                + "    try { log(parts(nf.formatToParts(-1234.5))); } catch(e) { logEx(e) }\n"
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
    @Alerts("currency:USD|literal:\u00A0|integer:1|group:,|integer:234|decimal:.|fraction:50")
    public void formatToPartsCurrencyCode() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'currency', currency: 'USD',"
                + " currencyDisplay: 'code' });\n"
                + "    try { log(parts(nf.formatToParts(1234.5))); } catch(e) { logEx(e) }\n"
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
    @Alerts({"integer:1|group:,|integer:234|decimal:.|fraction:50|literal: |currency:US dollars",
             "minusSign:-|integer:1|decimal:.|fraction:00|literal: |currency:US dollars"})
    public void formatToPartsCurrencyName() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'currency', currency: 'USD',"
                + " currencyDisplay: 'name' });\n"
                + "    try { log(parts(nf.formatToParts(1234.5))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(-1))); } catch(e) { logEx(e) }\n"
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
    @Alerts("literal:(|currency:$|integer:1|group:,|integer:234|decimal:.|fraction:50|literal:)")
    public void formatToPartsCurrencyAccounting() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'currency', currency: 'USD', currencySign:"
                + " 'accounting' });\n"
                + "    try { log(parts(nf.formatToParts(-1234.5))); } catch(e) { logEx(e) }\n"
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
    @Alerts({"integer:50|literal: |unit:km/h", "minusSign:-|integer:50|literal: |unit:km/h"})
    public void formatToPartsUnit() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'unit', unit: 'kilometer-per-hour' });\n"
                + "    try { log(parts(nf.formatToParts(50))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(-50))); } catch(e) { logEx(e) }\n"
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
    @Alerts("integer:50|literal: |unit:kilometers per hour")
    public void formatToPartsUnitLong() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'unit', unit: 'kilometer-per-hour',"
                + " unitDisplay: 'long' });\n"
                + "    try { log(parts(nf.formatToParts(50))); } catch(e) { logEx(e) }\n"
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
    @Alerts("integer:21|decimal:.|fraction:5|unit:\u00B0C")
    public void formatToPartsUnitNarrow() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'unit', unit: 'celsius', unitDisplay:"
                + " 'narrow' });\n"
                + "    try { log(parts(nf.formatToParts(21.5))); } catch(e) { logEx(e) }\n"
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
    @Alerts("integer:50|unit:%")
    public void formatToPartsUnitPercent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'unit', unit: 'percent' });\n"
                + "    try { log(parts(nf.formatToParts(50))); } catch(e) { logEx(e) }\n"
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
    @Alerts({"integer:1|decimal:.|fraction:23|exponentSeparator:E|exponentMinusSign:-|exponentInteger:3",
             "minusSign:-|integer:3|decimal:.|fraction:142|exponentSeparator:E|exponentInteger:4"})
    public void formatToPartsScientific() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { notation: 'scientific' });\n"
                + "    try { log(parts(nf.formatToParts(0.00123))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(-31415.9))); } catch(e) { logEx(e) }\n"
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
    @Alerts("integer:1|decimal:.|fraction:235|exponentSeparator:E|exponentInteger:6")
    public void formatToPartsEngineering() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { notation: 'engineering' });\n"
                + "    try { log(parts(nf.formatToParts(1234567))); } catch(e) { logEx(e) }\n"
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
    @Alerts({"integer:1|decimal:.|fraction:2|compact:K", "minusSign:-|integer:1|decimal:.|fraction:5|compact:M"})
    public void formatToPartsCompact() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { notation: 'compact' });\n"
                + "    try { log(parts(nf.formatToParts(1234))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(-1500000))); } catch(e) { logEx(e) }\n"
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
    @Alerts("integer:31|literal: |compact:thousand")
    public void formatToPartsCompactLong() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { notation: 'compact', compactDisplay: 'long' });\n"
                + "    try { log(parts(nf.formatToParts(31415))); } catch(e) { logEx(e) }\n"
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
    @Alerts("integer:1|decimal:,|fraction:5|literal:\u00A0|compact:Mio.")
    public void formatToPartsCompactGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('de', { notation: 'compact' });\n"
                + "    try { log(parts(nf.formatToParts(1500000))); } catch(e) { logEx(e) }\n"
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
    @Alerts("integer:3|decimal:.|fraction:1|compact:\u4E07")
    public void formatToPartsCompactJapanese() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('ja', { notation: 'compact' });\n"
                + "    try { log(parts(nf.formatToParts(31415))); } catch(e) { logEx(e) }\n"
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
    @Alerts({"nan:NaN", "infinity:\u221E", "minusSign:-|infinity:\u221E", "minusSign:-|integer:0"})
    public void formatToPartsSpecialValues() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', {});\n"
                + "    try { log(parts(nf.formatToParts(NaN))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(Infinity))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(-Infinity))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(-0))); } catch(e) { logEx(e) }\n"
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
    @Alerts({"plusSign:+|integer:5", "minusSign:-|integer:5", "plusSign:+|integer:0"})
    public void formatToPartsSignDisplay() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { signDisplay: 'always' });\n"
                + "    try { log(parts(nf.formatToParts(5))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(-5))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(0))); } catch(e) { logEx(e) }\n"
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
    @Alerts("literal:\u061C|minusSign:-|integer:\u0661|group:\u066C|integer:\u0662\u0663\u0664|decimal:\u066B|fraction:"
            + "\u0665")
    public void formatToPartsArabic() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('ar-EG', {});\n"
                + "    try { log(parts(nf.formatToParts(-1234.5))); } catch(e) { logEx(e) }\n"
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
    @Alerts({"integer:12|group:,|integer:345|group:,|integer:678|group:,|integer:901|group:,|integer:234|group:,"
                 + "|integer:567|group:,|integer:890", "integer:1|group:,|integer:234|decimal:.|fraction:5678",
             "nan:NaN", "integer:1"})
    public void formatToPartsInputs() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value;"
                + " }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { maximumFractionDigits: 20 });\n"
                + "    try { log(parts(nf.formatToParts(BigInt('12345678901234567890')))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts('1234.5678'))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(undefined))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatToParts(true))); } catch(e) { logEx(e) }\n"
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
    @Alerts({"function", "2", "formatRange", "function", "2"})
    public void formatRangeFunction() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try { log(typeof Intl.NumberFormat.prototype.formatRange); } catch(e) { logEx(e) }\n"
                + "    try { log(Intl.NumberFormat.prototype.formatRange.length); } catch(e) { logEx(e) }\n"
                + "    try { log(Intl.NumberFormat.prototype.formatRange.name); } catch(e) { logEx(e) }\n"
                + "    try { log(typeof Intl.NumberFormat.prototype.formatRangeToParts); } catch(e) { logEx(e) }\n"
                + "    try { log(Intl.NumberFormat.prototype.formatRangeToParts.length); } catch(e) { logEx(e) }\n"
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
    @Alerts("TypeError")
    public void formatRangeOnPrototype() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.prototype.formatRange(1, 2)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void formatRangeToPartsOnPrototype() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.NumberFormat.prototype.formatRangeToParts(1, 2)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3\u20135", "3.142\u20135.5", "1,234\u20135,678", "0\u20131"})
    public void formatRange() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', {});\n"
                + "    try { log(nf.formatRange(3, 5)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(3.14159, 5.5)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(1234, 5678)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(0, 1)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"~5", "~-5", "0\u2013-0"})
    public void formatRangeSame() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', {});\n"
                + "    try { log(nf.formatRange(5, 5)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(-5, -5)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(0, -0)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"~3", "2\u20133"})
    public void formatRangeRoundsToSame() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { maximumFractionDigits: 0 });\n"
                + "    try { log(nf.formatRange(2.9, 3.1)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(2.4, 3.4)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"-5 \u2013 -3", "-5 \u2013 3", "5\u2013-3"})
    public void formatRangeNegative() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', {});\n"
                + "    try { log(nf.formatRange(-5, -3)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(-5, 3)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(5, -3)); } catch(e) { logEx(e) }\n"
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
    @Alerts("5\u20133")
    public void formatRangeStartGreaterThanEnd() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', {});\n"
                + "    try { log(nf.formatRange(5, 3)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"3\u2013\u221E", "-\u221E \u2013 \u221E"})
    public void formatRangeInfinity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', {});\n"
                + "    try { log(nf.formatRange(3, Infinity)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(-Infinity, Infinity)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"RangeError", "RangeError"})
    public void formatRangeNaN() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', {});\n"
                + "    try { log(nf.formatRange(NaN, 5)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(3, NaN)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"TypeError", "TypeError"})
    public void formatRangeUndefined() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', {});\n"
                + "    try { log(nf.formatRange(undefined, 5)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(3, undefined)); } catch(e) { logEx(e) }\n"
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
    @Alerts("TypeError")
    public void formatRangeMissingEnd() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.NumberFormat('en').formatRange(3)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3\u20135", "1.5\u20132.5", "0\u20131"})
    public void formatRangeInputs() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { maximumFractionDigits: 20 });\n"
                + "    try { log(nf.formatRange(BigInt(3), BigInt(5))); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange('1.5', '2.5')); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(null, true)); } catch(e) { logEx(e) }\n"
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
    @Alerts("1.234,5\u20135.678,5")
    public void formatRangeGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('de', {});\n"
                + "    try { log(nf.formatRange(1234.5, 5678.5)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"$3.00 \u2013 $5.00", "~$3.00"})
    public void formatRangeCurrency() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'currency', currency: 'USD' });\n"
                + "    try { log(nf.formatRange(3, 5)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(3, 3)); } catch(e) { logEx(e) }\n"
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
    @Alerts("3,00\u20135,00\u00A0\u20AC")
    public void formatRangeCurrencyGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('de', { style: 'currency', currency: 'EUR' });\n"
                + "    try { log(nf.formatRange(3, 5)); } catch(e) { logEx(e) }\n"
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
    @Alerts("3% \u2013 5%")
    public void formatRangePercent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'percent' });\n"
                + "    try { log(nf.formatRange(0.03, 0.05)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"3\u20135 km", "1\u20132 km"})
    public void formatRangeUnit() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'unit', unit: 'kilometer' });\n"
                + "    try { log(nf.formatRange(3, 5)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(1, 2)); } catch(e) { logEx(e) }\n"
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
    @Alerts("1\u20132 kilometers")
    public void formatRangeUnitLong() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'unit', unit: 'kilometer', unitDisplay:"
                + " 'long' });\n"
                + "    try { log(nf.formatRange(1, 2)); } catch(e) { logEx(e) }\n"
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
    @Alerts("20\u201325\u00B0C")
    public void formatRangeUnitNarrow() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'unit', unit: 'celsius', unitDisplay:"
                + " 'narrow' });\n"
                + "    try { log(nf.formatRange(20, 25)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1K \u2013 5K", "1K \u2013 5M"})
    public void formatRangeCompact() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { notation: 'compact' });\n"
                + "    try { log(nf.formatRange(1000, 5000)); } catch(e) { logEx(e) }\n"
                + "    try { log(nf.formatRange(1000, 5000000)); } catch(e) { logEx(e) }\n"
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
    @Alerts("1E3 \u2013 5E3")
    public void formatRangeScientific() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { notation: 'scientific' });\n"
                + "    try { log(nf.formatRange(1000, 5000)); } catch(e) { logEx(e) }\n"
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
    @Alerts("+3 \u2013 +5")
    public void formatRangeSignDisplay() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nf = new Intl.NumberFormat('en', { signDisplay: 'always' });\n"
                + "    try { log(nf.formatRange(3, 5)); } catch(e) { logEx(e) }\n"
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
    @Alerts({"integer:3:startRange|literal:\u2013:shared|integer:5:endRange",
             "approximatelySign:~:shared|integer:5:shared"})
    public void formatRangeToParts() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value + ':' +"
                + " p.source; }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', {});\n"
                + "    try { log(parts(nf.formatRangeToParts(3, 5))); } catch(e) { logEx(e) }\n"
                + "    try { log(parts(nf.formatRangeToParts(5, 5))); } catch(e) { logEx(e) }\n"
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
    @Alerts("currency:$:startRange|integer:3:startRange|decimal:.:startRange|fraction:00:startRange|literal: \u2013 "
            + ":shared|currency:$:endRange|integer:5:endRange|decimal:.:endRange|fraction:00:endRange")
    public void formatRangeToPartsCurrency() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value + ':' +"
                + " p.source; }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'currency', currency: 'USD' });\n"
                + "    try { log(parts(nf.formatRangeToParts(3, 5))); } catch(e) { logEx(e) }\n"
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
    @Alerts("integer:3:startRange|literal:\u2013:shared|integer:5:endRange|literal: :shared|unit:km:shared")
    public void formatRangeToPartsUnit() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value + ':' +"
                + " p.source; }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { style: 'unit', unit: 'kilometer' });\n"
                + "    try { log(parts(nf.formatRangeToParts(3, 5))); } catch(e) { logEx(e) }\n"
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
    @Alerts("integer:1:startRange|compact:K:startRange|literal: \u2013 :shared|integer:5:endRange|compact:K:endRange")
    public void formatRangeToPartsCompact() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value + ':' +"
                + " p.source; }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', { notation: 'compact' });\n"
                + "    try { log(parts(nf.formatRangeToParts(1000, 5000))); } catch(e) { logEx(e) }\n"
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
    @Alerts("minusSign:-:startRange|integer:5:startRange|literal: \u2013 "
            + ":shared|minusSign:-:endRange|integer:3:endRange")
    public void formatRangeToPartsNegative() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    function parts(a) { return a.map(function(p) { return p.type + ':' + p.value + ':' +"
                + " p.source; }).join('|'); }\n"
                + "    var nf = new Intl.NumberFormat('en', {});\n"
                + "    try { log(parts(nf.formatRangeToParts(-5, -3))); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
