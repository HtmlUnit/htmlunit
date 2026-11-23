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

import java.util.Locale;
import java.util.TimeZone;

import org.htmlunit.BrowserVersion;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DateTimeFormat}.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author Lai Quang Duong
 */
public class DateTimeFormatTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"zh-CN", "gregory", "latn", "UTC", "undefined", "undefined", "undefined",
             "numeric", "numeric", "numeric", "undefined", "undefined", "undefined", "undefined"})
    @HtmlUnitNYI(CHROME = {"zh-CN", "undefined", "undefined", "America/New_York", "undefined", "undefined", "undefined",
                           "undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined"},
            EDGE = {"zh-CN", "undefined", "undefined", "America/New_York", "undefined", "undefined", "undefined",
                    "undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined"},
            FF = {"zh-CN", "undefined", "undefined", "America/New_York", "undefined", "undefined", "undefined",
                  "undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined"},
            FF_ESR = {"zh-CN", "undefined", "undefined", "America/New_York", "undefined", "undefined", "undefined",
                      "undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined"})
    public void resolvedOptionsValues() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var region1 = new Intl.DateTimeFormat('zh-CN', { timeZone: 'UTC' });\n"
                + "    var options = region1.resolvedOptions();\n"
                + "    log(options.locale);\n"
                + "    log(options.calendar);\n"
                + "    log(options.numberingSystem);\n"
                + "    log(options.timeZone);\n"
                + "    log(options.hour12);\n"
                + "    log(options.weekday);\n"
                + "    log(options.era);\n"
                + "    log(options.year);\n"
                + "    log(options.month);\n"
                + "    log(options.day);\n"
                + "    log(options.hour);\n"
                + "    log(options.minute);\n"
                + "    log(options.second);\n"
                + "    log(options.timeZoneName);\n"
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
                + "    var region1 = new Intl.DateTimeFormat('zh-CN', { timeZone: 'UTC' });\n"
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
    @Alerts({"true", "12/19/2013"})
    @BuggyWebDriver(FF = {"true", "12/20/2013"}, FF_ESR = {"true", "12/20/2013"})
    public void dateTimeFormat() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var dateFormat = Intl.DateTimeFormat('en');\n"
                + "    log(dateFormat instanceof Intl.DateTimeFormat);\n"

                + "    var date = new Date(Date.UTC(2013, 11, 20, 3, 0, 0));\n"
                + "    log(dateFormat.format(date));\n"
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
    @Alerts({"true", "12/20/2013"})
    public void dateTimeFormatGMT() throws Exception {
        dateTimeFormat("GMT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "12/20/2013"})
    public void dateTimeFormatUTC() throws Exception {
        dateTimeFormat("UTC");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "12/19/2013"})
    @BuggyWebDriver(FF = {"true", "12/20/2013"}, FF_ESR = {"true", "12/20/2013"})
    public void dateTimeFormatEST() throws Exception {
        dateTimeFormat("EST");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "12/20/2013"})
    public void dateTimeFormatBerlin() throws Exception {
        dateTimeFormat("Europe/Berlin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "12/19/2013"})
    @BuggyWebDriver(FF = {"true", "12/20/2013"}, FF_ESR = {"true", "12/20/2013"})
    public void dateTimeFormatNewYork() throws Exception {
        dateTimeFormat("America/New_York");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "12/20/2013"})
    public void dateTimeFormatTokyo() throws Exception {
        dateTimeFormat("Asia/Tokyo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "12/20/2013"})
    public void dateTimeFormatJST() throws Exception {
        dateTimeFormat("JST");
    }

    private void dateTimeFormat(final String tz) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var dateFormat = Intl.DateTimeFormat('en');\n"
                + "    log(dateFormat instanceof Intl.DateTimeFormat);\n"

                + "    var date = new Date(Date.UTC(2013, 11, 20, 3, 0, 0));\n"
                + "    log(dateFormat.format(date));\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        shutDownAll();
        try {
            final BrowserVersion.BrowserVersionBuilder builder
                = new BrowserVersion.BrowserVersionBuilder(getBrowserVersion());
            builder.setSystemTimezone(TimeZone.getTimeZone(tz));
            setBrowserVersion(builder.build());

            loadPageVerifyTitle2(html);
        }
        finally {
            shutDownAll();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("America/New_York")
    @BuggyWebDriver(FF = "Europe/Berlin", FF_ESR = "Europe/Berlin")
    public void timeZone() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Intl.DateTimeFormat().resolvedOptions().timeZone);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "+00:00",
            FF = "GMT",
            FF_ESR = "GMT")
    @BuggyWebDriver(FF = "Europe/Berlin", FF_ESR = "Europe/Berlin")
    @HtmlUnitNYI(CHROME = "GMT", EDGE = "GMT")
    public void timeZoneGMT() throws Exception {
        timeZone("GMT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("UTC")
    @BuggyWebDriver(FF = "Europe/Berlin", FF_ESR = "Europe/Berlin")
    public void timeZoneUTC() throws Exception {
        timeZone("UTC");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "America/Panama",
            FF = "EST",
            FF_ESR = "EST")
    @BuggyWebDriver(FF = "Europe/Berlin", FF_ESR = "Europe/Berlin")
    @HtmlUnitNYI(CHROME = "EST", EDGE = "EST")
    public void timeZoneEST() throws Exception {
        timeZone("EST");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Europe/Berlin")
    public void timeZoneBerlin() throws Exception {
        timeZone("Europe/Berlin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("America/New_York")
    @BuggyWebDriver(FF = "Europe/Berlin", FF_ESR = "Europe/Berlin")
    public void timeZoneNewYork() throws Exception {
        timeZone("America/New_York");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Asia/Tokyo")
    @BuggyWebDriver(FF = "Europe/Berlin", FF_ESR = "Europe/Berlin")
    public void timeZoneTokyo() throws Exception {
        timeZone("Asia/Tokyo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Asia/Tokyo",
            FF = "JST",
            FF_ESR = "JST")
    @BuggyWebDriver(FF = "Europe/Berlin", FF_ESR = "Europe/Berlin")
    @HtmlUnitNYI(CHROME = "JST", EDGE = "JST")
    public void timeZoneJST() throws Exception {
        timeZone("JST");
    }

    private void timeZone(final String tz) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Intl.DateTimeFormat().resolvedOptions().timeZone);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        shutDownAll();
        try {
            final BrowserVersion.BrowserVersionBuilder builder
                = new BrowserVersion.BrowserVersionBuilder(getBrowserVersion());
            builder.setSystemTimezone(TimeZone.getTimeZone(tz));
            setBrowserVersion(builder.build());

            loadPageVerifyTitle2(html);
        }
        finally {
            shutDownAll();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void locale() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Intl.DateTimeFormat().resolvedOptions().locale);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "de",
            EDGE = "de-DE")
    @BuggyWebDriver(FF = "en-US", FF_ESR = "en-US")
    @HtmlUnitNYI(CHROME = "de-DE",
            FF = "de-DE",
            FF_ESR = "de-DE")
    public void localeGermany() throws Exception {
        locale(Locale.GERMANY.toLanguageTag());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "de",
            EDGE = "de-DE")
    @BuggyWebDriver(FF = "en-US", FF_ESR = "en-US")
    @HtmlUnitNYI(EDGE = "de")
    public void localeGerman() throws Exception {
        locale(Locale.GERMAN.toLanguageTag());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("fr")
    @BuggyWebDriver(FF = "en-US", FF_ESR = "en-US")
    public void localeFrench() throws Exception {
        locale(Locale.FRENCH.toLanguageTag());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-GB")
    @BuggyWebDriver(FF = "en-US", FF_ESR = "en-US")
    @HtmlUnitNYI(CHROME = "en-CA",
            EDGE = "en-CA",
            FF = "en-CA",
            FF_ESR = "en-CA")
    public void localeCanada() throws Exception {
        locale(Locale.CANADA.toLanguageTag());
    }

    private void locale(final String language) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Intl.DateTimeFormat().resolvedOptions().locale);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        shutDownAll();
        try {
            final BrowserVersion.BrowserVersionBuilder builder
                = new BrowserVersion.BrowserVersionBuilder(getBrowserVersion());
            builder.setBrowserLanguage(language);
            setBrowserVersion(builder.build());

            loadPageVerifyTitle2(html);
        }
        finally {
            shutDownAll();
        }
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
                + "    log(Intl.DateTimeFormat.supportedLocalesOf('en'));\n"
                + "    log(Intl.DateTimeFormat.supportedLocalesOf(['en', 'xx-YY', 'ja']));\n"
                + "    log(Intl.DateTimeFormat.supportedLocalesOf([]));\n"
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
    @Alerts("en-US")
    public void ctorLocalesNoArgument() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat().resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void ctorLocalesUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat(undefined).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorLocalesNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat(null).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesEmptyString() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat('').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void ctorLocalesMixedCase() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat('EN-us').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("zh-Hant-TW")
    public void ctorLocalesScript() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('zh-hant-tw').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesInvalidTag() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('en-US!@#').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesPrivateUse() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('x-private').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesInvalidVariant() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('no-NO-NY').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void ctorLocalesNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat(42).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void ctorLocalesBoolean() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat(true).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void ctorLocalesSymbol() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat(Symbol('de')).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void ctorLocalesEmptyObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat({}).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de")
    public void ctorLocalesArrayLike() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat({ length: 2, 0: 'de', 1: 'ja' }).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesStringObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat(new String('de')).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de")
    public void ctorLocalesArrayDuplicates() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat(['de', 'DE', 'ja']).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorLocalesArrayNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat(['de', 42]).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorLocalesArrayNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat(['de', null]).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorLocalesArrayUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat(['de', undefined]).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesArrayInvalidTag() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat(['de', 'en-US!@#']).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ja")
    public void ctorLocalesArrayLocale() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat([new Intl.Locale('ja')]).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ja")
    public void ctorLocalesLocale() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat(new Intl.Locale('ja')).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void ctorLocalesEmptyArray() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat([]).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfNoArgument() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf(undefined)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void supportedLocalesOfNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf(null)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfEmptyString() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf('')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void supportedLocalesOfMixedCase() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf('EN-us')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("zh-Hant-TW")
    public void supportedLocalesOfScript() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf('zh-hant-tw')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfInvalidTag() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf('en-US!@#')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfPrivateUse() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf('x-private')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfInvalidVariant() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf('no-NO-NY')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf(42)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfBoolean() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf(true)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfSymbol() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf(Symbol('de'))"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfEmptyObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf({})"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de,ja")
    public void supportedLocalesOfArrayLike() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "Intl.DateTimeFormat.supportedLocalesOf({ length: 2, 0: 'de', 1: 'ja' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfStringObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf(new String('de'))"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de,ja")
    public void supportedLocalesOfArrayDuplicates() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "Intl.DateTimeFormat.supportedLocalesOf(['de', 'DE', 'ja'])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void supportedLocalesOfArrayNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf(['de', 42])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void supportedLocalesOfArrayNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf(['de', null])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void supportedLocalesOfArrayUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf(['de', undefined])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfArrayInvalidTag() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "Intl.DateTimeFormat.supportedLocalesOf(['de', 'en-US!@#'])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ja")
    public void supportedLocalesOfArrayLocale() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "Intl.DateTimeFormat.supportedLocalesOf([new Intl.Locale('ja')])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ja")
    public void supportedLocalesOfLocale() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "Intl.DateTimeFormat.supportedLocalesOf(new Intl.Locale('ja'))"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfEmptyArray() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.DateTimeFormat.supportedLocalesOf([])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void resolvedOptionsLocaleNoArgument() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat().resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void resolvedOptionsLocaleMixedCase() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat('EN-us').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de-DE")
    public void resolvedOptionsLocaleRegion() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat('de-DE').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de")
    public void resolvedOptionsLocaleFallback() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat('de-XX').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("zh-Hant-TW")
    public void resolvedOptionsLocaleScript() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('zh-hant-tw').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void resolvedOptionsLocaleUnsupported() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat('ban').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de")
    public void resolvedOptionsLocaleUnsupportedFirst() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat(['ban', 'de']).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("he")
    public void resolvedOptionsLocaleDeprecated() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.DateTimeFormat('iw').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ja-JP-u-ca-japanese")
    public void resolvedOptionsLocaleCalendar() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('ja-JP-u-ca-japanese').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-u-ca-buddhist")
    public void resolvedOptionsLocaleCalendarOther() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('en-u-ca-buddhist').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("th-TH-u-nu-thai")
    public void resolvedOptionsLocaleNumberingSystem() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('th-TH-u-nu-thai').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-u-hc-h23")
    public void resolvedOptionsLocaleHourCycle() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('en-u-hc-h23').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en")
    public void resolvedOptionsLocaleHourCycleInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('en-u-hc-h25').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de")
    public void resolvedOptionsLocaleCollation() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('de-u-co-phonebk').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en")
    public void resolvedOptionsLocaleNumeric() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('en-u-kn-true').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-u-ca-buddhist-nu-thai")
    public void resolvedOptionsLocaleMultipleExtensions() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.DateTimeFormat('en-u-nu-thai-ca-buddhist-co-phonebk').resolvedOptions().locale"));
    }

    /**
     * Logs the default locale, the hour cycle and the format of the default locale
     * (the browser locale, not the locale of the operating system).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"en-US", "h12", "Dec 20, 2013, 3:04 PM", "12/20/2013, 3:04:05 PM", "en-US"})
    @HtmlUnitNYI(
            CHROME = {"en-US", "undefined", "12/20/2013", "2013-12-20 10:04:05", "en-US"},
            EDGE = {"en-US", "undefined", "12/20/2013", "2013-12-20 10:04:05", "en-US"},
            FF = {"en-US", "undefined", "12/20/2013", "2013-12-20 10:04:05", "en-US"},
            FF_ESR = {"en-US", "undefined", "12/20/2013", "2013-12-20 10:04:05", "en-US"})
    public void defaultLocaleFormat() throws Exception {
        defaultLocaleFormat(null);
    }

    /**
     * Logs the default locale, the hour cycle and the format of the default locale if the browser language is de-DE.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"de", "h23", "20.12.2013, 15:04", "20.12.2013, 15:04:05", "de-DE"})
    @BuggyWebDriver(
            EDGE = {"de-DE", "h23", "20.12.2013, 15:04", "20.12.2013, 15:04:05", "de-DE"},
            FF = {"en-US", "h12", "Dec 20, 2013, 3:04 PM", "12/20/2013, 3:04:05 PM", "de-DE"},
            FF_ESR = {"en-US", "h12", "Dec 20, 2013, 3:04 PM", "12/20/2013, 3:04:05 PM", "de-DE"})
    @HtmlUnitNYI(
            CHROME = {"de-DE", "undefined", "20.12.2013", "2013-12-20 10:04:05", "de-DE"},
            EDGE = {"de-DE", "undefined", "20.12.2013", "2013-12-20 10:04:05", "de-DE"},
            FF = {"de-DE", "undefined", "20.12.2013", "2013-12-20 10:04:05", "de-DE"},
            FF_ESR = {"de-DE", "undefined", "20.12.2013", "2013-12-20 10:04:05", "de-DE"})
    public void defaultLocaleFormatGermany() throws Exception {
        defaultLocaleFormat(Locale.GERMANY.toLanguageTag());
    }

    /**
     * Logs the default locale, the hour cycle and the format of the default locale if the browser language is fr.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"fr", "h23", "20 d\u00E9c. 2013, 15:04", "20/12/2013 15:04:05", "fr"})
    @BuggyWebDriver(
            FF = {"en-US", "h12", "Dec 20, 2013, 3:04 PM", "12/20/2013, 3:04:05 PM", "fr"},
            FF_ESR = {"en-US", "h12", "Dec 20, 2013, 3:04 PM", "12/20/2013, 3:04:05 PM", "fr"})
    @HtmlUnitNYI(
            CHROME = {"fr", "undefined", "20/12/2013", "2013-12-20 10:04:05", "fr"},
            EDGE = {"fr", "undefined", "20/12/2013", "2013-12-20 10:04:05", "fr"},
            FF = {"fr", "undefined", "20/12/2013", "2013-12-20 10:04:05", "fr"},
            FF_ESR = {"fr", "undefined", "20/12/2013", "2013-12-20 10:04:05", "fr"})
    public void defaultLocaleFormatFrench() throws Exception {
        defaultLocaleFormat(Locale.FRENCH.toLanguageTag());
    }

    /**
     * Logs the default locale, the default hour cycle, a date formatted with the default locale
     * (Intl.DateTimeFormat and Date.toLocaleString()) and the default time zone.
     * @param language the browser language or {@code null} for the default
     */
    private void defaultLocaleFormat(final String language) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var date = new Date(Date.UTC(2013, 11, 20, 15, 4, 5));\n"
            + "  log(Intl.DateTimeFormat().resolvedOptions().locale);\n"
            + "  log(new Intl.DateTimeFormat(undefined, { hour: 'numeric' }).resolvedOptions().hourCycle);\n"
            + "  log(new Intl.DateTimeFormat(undefined,"
                        + " { timeZone: 'UTC', dateStyle: 'medium', timeStyle: 'short' }).format(date));\n"
            + "  log(date.toLocaleString(undefined, { timeZone: 'UTC' }));\n"
            + "  log(navigator.language);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        if (language == null) {
            loadPageVerifyTitle2(html);
            return;
        }

        shutDownAll();
        try {
            final BrowserVersion.BrowserVersionBuilder builder
                = new BrowserVersion.BrowserVersionBuilder(getBrowserVersion());
            builder.setBrowserLanguage(language);
            setBrowserVersion(builder.build());

            loadPageVerifyTitle2(html);
        }
        finally {
            shutDownAll();
        }
    }
}
