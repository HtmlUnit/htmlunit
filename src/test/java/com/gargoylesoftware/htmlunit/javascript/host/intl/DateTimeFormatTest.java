/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link DateTimeFormat}.
 *
 * @author Roanld Brill
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class DateTimeFormatTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Object]")
    public void intl() throws Exception {
        test("Intl");
    }

    private void test(final String... string) throws Exception {
        String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var date = new Date(Date.UTC(2012, 11, 20, 3, 0, 0));\n"
            + "    try {\n";
        for (int i = 0; i < string.length - 1; i++) {
            html += string[i] + "\n";
        }
        html +=
            "      alert(" + string[string.length - 1] + ");\n"
            + "    } catch(e) {alert('exception')}\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2012",
            IE = "\u200E12\u200E/\u200E20\u200E/\u200E2012")
    @NotYetImplemented
    public void format_default() throws Exception {
        test("new Intl.DateTimeFormat().format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2012",
            IE = "\u200E12\u200E/\u200E20\u200E/\u200E2012")
    @NotYetImplemented
    public void format_en_us() throws Exception {
        test("new Intl.DateTimeFormat('en-US').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    @NotYetImplemented
    public void format_en_gb() throws Exception {
        test("new Intl.DateTimeFormat('en-GB').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012. 12. 20.",
            IE = "\u200E2012\u200E\uB144 \u200E12\u200E\uC6D4 \u200E20\u200E\uC77C")
    @NotYetImplemented
    public void format_ko_kr() throws Exception {
        test("new Intl.DateTimeFormat('ko-KR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200f\u002f\u0661\u0662\u200f\u002f\u0662\u0660\u0661\u0662",
            IE = "\u200f\u0662\u0660\u200f\u002f\u200f\u0661\u0662\u200f\u002f\u200f\u0662\u0660\u0661\u0662")
    @NotYetImplemented
    public void format_ar_EG() throws Exception {
        test("new Intl.DateTimeFormat('ar-EG').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u5e73\u621024/12/20",
            FF = "24/12/20",
            IE = "\u200e\u5e73\u6210\u200e\u0020\u200e24\u200e\u5e74\u200e12\u200e\u6708\u200e20\u200e\u65e5")
    @NotYetImplemented
    public void format_ja_jp_u_ca_japanese() throws Exception {
        test("new Intl.DateTimeFormat('ja-JP-u-ca-japanese').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200e20\u200e\u002f\u200e12\u200e\u002f\u200e2012")
    @NotYetImplemented
    public void format_ban_id() throws Exception {
        test("new Intl.DateTimeFormat(['ban', 'id']).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Donnerstag, 20. Dezember 2012",
            IE = "\u200eDonnerstag\u200e, \u200e20\u200e. \u200eDezember\u200e\u0020\u200e2012")
    @NotYetImplemented
    public void format_weekday_long() throws Exception {
        test("var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };",
                "new Intl.DateTimeFormat('de-DE', options).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Thursday, December 20, 2012, GMT",
            IE = "\u200eThursday‎, ‎December‎ ‎20‎, ‎2012")
    @NotYetImplemented
    public void format_utc_short() throws Exception {
        test("var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };",
                "options.timeZone = 'UTC';",
                "options.timeZoneName = 'short';",
                "new Intl.DateTimeFormat('en-US', options).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "4:00:00 am GMT+1",
            IE = "‎4‎:‎00‎:‎00‎ ‎AM")
    @NotYetImplemented
    public void format_detailed() throws Exception {
        test("options = {",
                " hour: 'numeric', minute: 'numeric', second: 'numeric',",
                " timeZoneName: 'short'",
                "};",
                "new Intl.DateTimeFormat('en-AU', options).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2012, 04:00:00",
            IE = "\u200e12\u200e\u002f\u200e20\u200e\u002f\u200e2012\u200e\u0020"
                    + "\u200e\u0034\u200e\u003a\u200e00\u200e\u003a\u200e00")
    @NotYetImplemented
    public void format_detailed_24h() throws Exception {
        test("var options = {",
                " year: 'numeric', month: 'numeric', day: 'numeric',",
                " hour: 'numeric', minute: 'numeric', second: 'numeric',",
                " hour12: false",
                "};",
                "new Intl.DateTimeFormat('en-US', options).format(date)");
    }
}
