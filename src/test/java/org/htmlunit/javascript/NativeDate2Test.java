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

import java.util.TimeZone;

import org.htmlunit.BrowserVersion;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for the Date support.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class NativeDate2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T22:23:00.000Z")
    public void ctorDateTimeStringGMT() throws Exception {
        ctorDateTimeString("new Date('2021-12-18T22:23:00.000+00:00').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T22:23:00.000Z")
    public void ctorDateTimeStringGMT2() throws Exception {
        ctorDateTimeString("new Date('Sat, 18 Dec 2021 22:23:00 GMT').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T22:23:00.000Z")
    public void ctorDateTimeStringUTC() throws Exception {
        ctorDateTimeString("new Date('2021-12-18T22:23:00.000+00:00').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T22:23:00.000Z")
    public void ctorDateTimeStringUTC2() throws Exception {
        ctorDateTimeString("new Date('Sat, 18 Dec 2021 22:23:00 UTC').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T22:23:00.000Z")
    public void ctorDateTimeStringUT() throws Exception {
        ctorDateTimeString("new Date('Sat, 18 Dec 2021 22:23:00 UT').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T12:23:00.000Z")
    public void ctorDateTimeStringEST() throws Exception {
        ctorDateTimeString("new Date('2021-12-18T17:23:00.000+05:00').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-19T03:23:00.000Z")
    public void ctorDateTimeStringEST2() throws Exception {
        ctorDateTimeString("new Date('Sat, 18 Dec 2021 22:23:00 EST').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-19T02:23:00.000Z")
    public void ctorDateTimeStringEDT() throws Exception {
        ctorDateTimeString("new Date('Sat, 18 Dec 2021 22:23:00 EDT').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-19T04:23:00.000Z")
    public void ctorDateTimeStringCST() throws Exception {
        ctorDateTimeString("new Date('Sat, 18 Dec 2021 22:23:00 CST').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-19T03:23:00.000Z")
    public void ctorDateTimeStringCDT() throws Exception {
        ctorDateTimeString("new Date('Sat, 18 Dec 2021 22:23:00 CDT').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-19T05:23:00.000Z")
    public void ctorDateTimeStringMST() throws Exception {
        ctorDateTimeString("new Date('Sat, 18 Dec 2021 22:23:00 MST').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-19T04:23:00.000Z")
    public void ctorDateTimeStringMDT() throws Exception {
        ctorDateTimeString("new Date('Sat, 18 Dec 2021 22:23:00 MDT').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-19T06:23:00.000Z")
    public void ctorDateTimeStringPST() throws Exception {
        ctorDateTimeString("new Date('Sat, 18 Dec 2021 22:23:00 PST').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-19T05:23:00.000Z")
    public void ctorDateTimeStringPDT() throws Exception {
        ctorDateTimeString("new Date('Sat, 18 Dec 2021 22:23:00 PDT').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T18:23:00.000Z")
    public void ctorDateTimeStringBerlin() throws Exception {
        ctorDateTimeString("new Date('2021-12-18T17:23:00.000-01:00').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-19T02:23:00.000Z")
    public void ctorDateTimeStringTokyo() throws Exception {
        ctorDateTimeString("new Date('2021-12-18T17:23:00.000-09:00').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-19T02:23:00.000Z")
    public void ctorDateTimeStringJST() throws Exception {
        ctorDateTimeString("new Date('2021-12-18T17:23:00.000-09:00').toISOString()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T12:23:00.000Z")
    public void ctorDateTimeStringNewYork() throws Exception {
        ctorDateTimeString("new Date('2021-12-18T17:23:00.000+05:00').toISOString()");
    }

    private void ctorDateTimeString(final String js) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(" + js + ");\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T22:23:00.000Z")
    @BuggyWebDriver(FF = "2021-12-18T21:23:00.000Z",
            FF_ESR = "2021-12-18T21:23:00.000Z")
    public void ctorDateTimeGMT() throws Exception {
        ctorDateTime("GMT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T22:23:00.000Z")
    @BuggyWebDriver(FF = "2021-12-18T21:23:00.000Z",
            FF_ESR = "2021-12-18T21:23:00.000Z")
    public void ctorDateTimeUTC() throws Exception {
        ctorDateTime("UTC");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-19T03:23:00.000Z")
    @BuggyWebDriver(FF = "2021-12-18T21:23:00.000Z",
            FF_ESR = "2021-12-18T21:23:00.000Z")
    public void ctorDateTimeEST() throws Exception {
        ctorDateTime("EST");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T21:23:00.000Z")
    public void ctorDateTimeBerlin() throws Exception {
        ctorDateTime("Europe/Berlin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T13:23:00.000Z")
    @BuggyWebDriver(FF = "2021-12-18T21:23:00.000Z",
            FF_ESR = "2021-12-18T21:23:00.000Z")
    public void ctorDateTimeTokyo() throws Exception {
        ctorDateTime("Asia/Tokyo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T13:23:00.000Z")
    @BuggyWebDriver(FF = "2021-12-18T21:23:00.000Z",
            FF_ESR = "2021-12-18T21:23:00.000Z")
    public void ctorDateTimeJST() throws Exception {
        ctorDateTime("JST");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-19T03:23:00.000Z")
    @BuggyWebDriver(FF = "2021-12-18T21:23:00.000Z",
            FF_ESR = "2021-12-18T21:23:00.000Z")
    public void ctorDateTimeNewYork() throws Exception {
        ctorDateTime("America/New_York");
    }

    private void ctorDateTime(final String tz) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(new Date('2021-12-18T22:23').toISOString());\n"
            + "</script>\n"
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
    @Alerts("2021-12-18T00:00:00.000Z")
    public void ctorDateGMT() throws Exception {
        ctorDate("GMT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T00:00:00.000Z")
    public void ctorDateUTC() throws Exception {
        ctorDate("UTC");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T00:00:00.000Z")
    public void ctorDateEST() throws Exception {
        ctorDate("EST");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T00:00:00.000Z")
    public void ctorDateBerlin() throws Exception {
        ctorDate("Europe/Berlin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T00:00:00.000Z")
    public void ctorDateNewYork() throws Exception {
        ctorDate("America/New_York");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T00:00:00.000Z")
    public void ctorDateTokyo() throws Exception {
        ctorDate("Asia/Tokyo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021-12-18T00:00:00.000Z")
    public void ctorDateJST() throws Exception {
        ctorDate("JST");
    }

    private void ctorDate(final String tz) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(new Date('2021-12-18').toISOString());\n"
            + "</script>\n"
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
    @Alerts("2000-02-28T23:59:59.000Z")
    public void ctorInt() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(new Date(951782399000).toISOString());\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2035-11-30T01:46:40.000Z")
    public void ctorDouble() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(new Date(208e10).toISOString());\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12/18/2021, 10:23:00 PM")
    @HtmlUnitNYI(CHROME = "12/18/21, 10:23 PM",
            EDGE = "12/18/21, 10:23 PM",
            FF = "12/18/21, 10:23 PM",
            FF_ESR = "12/18/21, 10:23 PM")
    public void toLocaleEnUs() throws Exception {
        toLocale("new Date('2021-12-18T22:23').toLocaleString('en-US')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12/18/2021")
    @HtmlUnitNYI(CHROME = "12/18/21",
            EDGE = "12/18/21",
            FF = "12/18/21",
            FF_ESR = "12/18/21")
    public void toLocaleEnUsDate() throws Exception {
        toLocale("new Date('2021-12-18T22:23').toLocaleDateString('en-US')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("10:23:00 PM")
    @HtmlUnitNYI(CHROME = "10:23 PM",
            EDGE = "10:23 PM",
            FF = "10:23 PM",
            FF_ESR = "10:23 PM")
    public void toLocaleEnUsTime() throws Exception {
        toLocale("new Date('2021-12-18T22:23').toLocaleTimeString('en-US')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("18.12.2021, 22:23:00")
    @HtmlUnitNYI(CHROME = "18.12.21, 22:23",
            EDGE = "18.12.21, 22:23",
            FF = "18.12.21, 22:23",
            FF_ESR = "18.12.21, 22:23")
    public void toLocaleDeDe() throws Exception {
        toLocale("new Date('2021-12-18T22:23').toLocaleString('de-DE')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("18.12.2021")
    @HtmlUnitNYI(CHROME = "18.12.21",
            EDGE = "18.12.21",
            FF = "18.12.21",
            FF_ESR = "18.12.21")
    public void toLocaleDeDeDate() throws Exception {
        toLocale("new Date('2021-12-18T22:23').toLocaleDateString('de-DE')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("22:23:00")
    @HtmlUnitNYI(CHROME = "22:23",
            EDGE = "22:23",
            FF = "22:23",
            FF_ESR = "22:23")
    public void toLocaleDeDeTime() throws Exception {
        toLocale("new Date('2021-12-18T22:23').toLocaleTimeString('de-DE')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021/12/18 22:23:00")
    @HtmlUnitNYI(CHROME = "2021/12/18 22:23",
            EDGE = "2021/12/18 22:23",
            FF = "2021/12/18 22:23",
            FF_ESR = "2021/12/18 22:23")
    public void toLocaleJaJp() throws Exception {
        toLocale("new Date('2021-12-18T22:23').toLocaleString('ja-JP')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021/12/18")
    public void toLocaleJaJpDate() throws Exception {
        toLocale("new Date('2021-12-18T22:23').toLocaleDateString('ja-JP')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("22:23:00")
    @HtmlUnitNYI(CHROME = "22:23",
            EDGE = "22:23",
            FF = "22:23",
            FF_ESR = "22:23")
    public void toLocaleJaJpTime() throws Exception {
        toLocale("new Date('2021-12-18T22:23').toLocaleTimeString('ja-JP')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021/12/18 22:23:00")
    @HtmlUnitNYI(CHROME = "2021/12/18 22:23",
            EDGE = "2021/12/18 22:23",
            FF = "2021/12/18 22:23",
            FF_ESR = "2021/12/18 22:23")
    public void toLocaleArray() throws Exception {
        toLocale("new Date('2021-12-18T22:23').toLocaleString(['foo', 'ja-JP', 'en-US'])");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2021/12/18")
    public void toLocaleArrayDate() throws Exception {
        toLocale("new Date('2021-12-18T22:23').toLocaleDateString(['foo', 'ja-JP', 'en-US'])");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("22:23:00")
    @HtmlUnitNYI(CHROME = "22:23",
            EDGE = "22:23",
            FF = "22:23",
            FF_ESR = "22:23")
    public void toLocaleArrayTime() throws Exception {
        toLocale("new Date('2021-12-18T22:23').toLocaleTimeString(['foo', 'ja-JP', 'en-US'])");
    }

    private void toLocale(final String js) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(" + js + ");\n"
            + "</script>\n"
            + "</body></html>";

        shutDownAll();
        try {
            final BrowserVersion.BrowserVersionBuilder builder
                = new BrowserVersion.BrowserVersionBuilder(getBrowserVersion());
            builder.setSystemTimezone(TimeZone.getTimeZone("GMT"));
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
    @Alerts("Sat Dec 18 2021")
    public void toDateStringGMT() throws Exception {
        toDateString("GMT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sat Dec 18 2021")
    public void toDateStringUTC() throws Exception {
        toDateString("UTC");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sat Dec 18 2021")
    public void toDateStringEST() throws Exception {
        toDateString("EST");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sat Dec 18 2021")
    public void toDateStringBerlin() throws Exception {
        toDateString("Europe/Berlin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sat Dec 18 2021")
    public void toDateStringNewYork() throws Exception {
        toDateString("America/New_York");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sun Dec 19 2021")
    @BuggyWebDriver(FF = "Sat Dec 18 2021",
            FF_ESR = "Sat Dec 18 2021")
    public void toDateStringTokyo() throws Exception {
        toDateString("Asia/Tokyo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sun Dec 19 2021")
    @BuggyWebDriver(FF = "Sat Dec 18 2021",
            FF_ESR = "Sat Dec 18 2021")
    public void toDateStringJST() throws Exception {
        toDateString("JST");
    }

    private void toDateString(final String tz) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(new Date('Sat, 18 Dec 2021 22:23:00 UTC').toDateString());\n"
            + "</script>\n"
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
    @Alerts("22:23:00 GMT+0000 (Greenwich Mean Time)")
    @BuggyWebDriver(FF = "23:23:00 GMT+0100 (Central European Standard Time)",
            FF_ESR = "23:23:00 GMT+0100 (Central European Standard Time)")
    @HtmlUnitNYI(CHROME = "22:23:00 GMT-0000 (GMT)",
            EDGE = "22:23:00 GMT-0000 (GMT)",
            FF = "22:23:00 GMT-0000 (GMT)",
            FF_ESR = "22:23:00 GMT-0000 (GMT)")
    public void toTimeStringGMT() throws Exception {
        toTimeString("GMT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("22:23:00 GMT+0000 (Coordinated Universal Time)")
    @BuggyWebDriver(FF = "23:23:00 GMT+0100 (Central European Standard Time)",
            FF_ESR = "23:23:00 GMT+0100 (Central European Standard Time)")
    @HtmlUnitNYI(CHROME = "22:23:00 GMT-0000 (UTC)",
            EDGE = "22:23:00 GMT-0000 (UTC)",
            FF = "22:23:00 GMT-0000 (UTC)",
            FF_ESR = "22:23:00 GMT-0000 (UTC)")
    public void toTimeStringUTC() throws Exception {
        toTimeString("UTC");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("17:23:00 GMT-0500 (Eastern Standard Time)")
    @BuggyWebDriver(FF = "23:23:00 GMT+0100 (Central European Standard Time)",
            FF_ESR = "23:23:00 GMT+0100 (Central European Standard Time)")
    @HtmlUnitNYI(CHROME = "17:23:00 GMT-0500 (-05:00)",
            EDGE = "17:23:00 GMT-0500 (-05:00)",
            FF = "17:23:00 GMT-0500 (-05:00)",
            FF_ESR = "17:23:00 GMT-0500 (-05:00)")
    public void toTimeStringEST() throws Exception {
        toTimeString("EST");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("23:23:00 GMT+0100 (Central European Standard Time)")
    @HtmlUnitNYI(CHROME = "23:23:00 GMT+0100 (CET)",
            EDGE = "23:23:00 GMT+0100 (CET)",
            FF = "23:23:00 GMT+0100 (CET)",
            FF_ESR = "23:23:00 GMT+0100 (CET)")
    public void toTimeStringBerlin() throws Exception {
        toTimeString("Europe/Berlin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("17:23:00 GMT-0500 (Eastern Standard Time)")
    @BuggyWebDriver(FF = "23:23:00 GMT+0100 (Central European Standard Time)",
            FF_ESR = "23:23:00 GMT+0100 (Central European Standard Time)")
    @HtmlUnitNYI(CHROME = "17:23:00 GMT-0500 (EST)",
            EDGE = "17:23:00 GMT-0500 (EST)",
            FF = "17:23:00 GMT-0500 (EST)",
            FF_ESR = "17:23:00 GMT-0500 (EST)")
    public void toTimeStringNewYork() throws Exception {
        toTimeString("America/New_York");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("07:23:00 GMT+0900 (Japan Standard Time)")
    @BuggyWebDriver(FF = "23:23:00 GMT+0100 (Central European Standard Time)",
            FF_ESR = "23:23:00 GMT+0100 (Central European Standard Time)")
    @HtmlUnitNYI(CHROME = "07:23:00 GMT+0900 (JST)",
            EDGE = "07:23:00 GMT+0900 (JST)",
            FF = "07:23:00 GMT+0900 (JST)",
            FF_ESR = "07:23:00 GMT+0900 (JST)")
    public void toTimeStringTokyo() throws Exception {
        toTimeString("Asia/Tokyo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("07:23:00 GMT+0900 (Japan Standard Time)")
    @BuggyWebDriver(FF = "23:23:00 GMT+0100 (Central European Standard Time)",
            FF_ESR = "23:23:00 GMT+0100 (Central European Standard Time)")
    @HtmlUnitNYI(CHROME = "07:23:00 GMT+0900 (JST)",
            EDGE = "07:23:00 GMT+0900 (JST)",
            FF = "07:23:00 GMT+0900 (JST)",
            FF_ESR = "07:23:00 GMT+0900 (JST)")
    public void toTimeStringJST() throws Exception {
        toTimeString("JST");
    }

    private void toTimeString(final String tz) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(new Date('Sat, 18 Dec 2021 22:23:00 UTC').toTimeString());\n"
            + "</script>\n"
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
    @Alerts("Sat, 18 Dec 2021 22:23:00 GMT")
    public void toUTCStringGMT() throws Exception {
        toUTCString("GMT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sat, 18 Dec 2021 22:23:00 GMT")
    public void toUTCStringUTC() throws Exception {
        toUTCString("UTC");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sat, 18 Dec 2021 22:23:00 GMT")
    public void toUTCStringEST() throws Exception {
        toUTCString("EST");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sat, 18 Dec 2021 22:23:00 GMT")
    public void toUTCStringBerlin() throws Exception {
        toUTCString("Europe/Berlin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sat, 18 Dec 2021 22:23:00 GMT")
    public void toUTCStringNewYork() throws Exception {
        toUTCString("America/New_York");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sat, 18 Dec 2021 22:23:00 GMT")
    public void toUTCStringTokyo() throws Exception {
        toUTCString("Asia/Tokyo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sat, 18 Dec 2021 22:23:00 GMT")
    public void toUTCStringJST() throws Exception {
        toUTCString("JST");
    }

    private void toUTCString(final String tz) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(new Date('Sat, 18 Dec 2021 22:23:00 UTC').toUTCString());\n"
            + "</script>\n"
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
    @Alerts("0")
    @BuggyWebDriver(FF = "-60",
            FF_ESR = "-60")
    public void timezoneOffsetGMT() throws Exception {
        timezoneOffset("GMT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @BuggyWebDriver(FF = "-60",
            FF_ESR = "-60")
    public void timezoneOffsetUTC() throws Exception {
        timezoneOffset("UTC");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("300")
    @BuggyWebDriver(FF = "-60",
            FF_ESR = "-60")
    public void timezoneOffsetEST() throws Exception {
        timezoneOffset("EST");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-60")
    public void timezoneOffsetBerlin() throws Exception {
        timezoneOffset("Europe/Berlin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("300")
    @BuggyWebDriver(FF = "-60",
            FF_ESR = "-60")
    public void timezoneOffsetNewYork() throws Exception {
        timezoneOffset("America/New_York");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-540")
    @BuggyWebDriver(FF = "-60",
            FF_ESR = "-60")
    public void timezoneOffsetTokyo() throws Exception {
        timezoneOffset("Asia/Tokyo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-540")
    @BuggyWebDriver(FF = "-60",
            FF_ESR = "-60")
    public void timezoneOffsetJST() throws Exception {
        timezoneOffset("JST");
    }

    private void timezoneOffset(final String tz) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(new Date(0).getTimezoneOffset());\n"
            + "</script>\n"
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
}
