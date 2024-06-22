/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.junit.BrowserRunner.BuggyWebDriver;
import org.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DateTimeFormat}.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
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
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
        final String html
            = "<html><head><script>\n"
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
    @Alerts(DEFAULT = "Etc/GMT+5",
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
        final String html
            = "<html><head><script>\n"
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
        final String html
            = "<html><head><script>\n"
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
        final String html
            = "<html><head><script>\n"
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
}
