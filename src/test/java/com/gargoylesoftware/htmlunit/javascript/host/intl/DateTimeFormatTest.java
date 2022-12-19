/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.intl;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

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
    @Alerts(DEFAULT = {"zh-CN", "gregory", "latn", "UTC", "undefined", "undefined", "undefined",
                       "numeric", "numeric", "numeric", "undefined", "undefined", "undefined", "undefined"},
            IE = {"zh-Hans-CN", "gregory", "latn", "UTC", "undefined", "undefined", "undefined",
                  "numeric", "numeric", "numeric", "undefined", "undefined", "undefined", "undefined"})
    @HtmlUnitNYI(CHROME = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                           "undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined"},
            EDGE = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                    "undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined"},
            FF = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                  "undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined"},
            FF_ESR = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                      "undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined"},
            IE = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
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
    @Alerts(DEFAULT = {"true", "12/20/2013"},
            IE = {"true", "\u200E12\u200E/\u200E20\u200E/\u200E2013"})
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
}
