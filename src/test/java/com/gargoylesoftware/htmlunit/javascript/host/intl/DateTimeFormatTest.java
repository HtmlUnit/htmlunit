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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF45;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

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
    public void format_default() throws Exception {
        test("new Intl.DateTimeFormat().format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F\u002f\u0661\u0662\u200F\u002f\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0662\u0660\u200F\u002f\u200F\u0661\u0662\u200F\u002f\u200F\u0662\u0660\u0661\u0662")
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0660\u0667\u200F/\u200F\u0660\u0662\u200F/\u200F\u0661\u0664\u0663\u0664")
    @NotYetImplemented(IE)
    public void format_ar() throws Exception {
        test("new Intl.DateTimeFormat('ar').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0662\u0660\u200F/\u200F\u0661\u0662\u200F/\u200F\u0662\u0660\u0661\u0662")
    public void format_ar_ae() throws Exception {
        test("new Intl.DateTimeFormat('ar-AE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0662\u0660\u200F/\u200F\u0661\u0662\u200F/\u200F\u0662\u0660\u0661\u0662")
    public void format_ar_bh() throws Exception {
        test("new Intl.DateTimeFormat('ar-BH').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            FF = "20\u200F/12\u200F/2012",
            IE = "\u200F20\u200F-\u200F12\u200F-\u200F2012")
    public void format_ar_dz() throws Exception {
        test("new Intl.DateTimeFormat('ar-DZ').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0662\u0660\u200F/\u200F\u0661\u0662\u200F/\u200F\u0662\u0660\u0661\u0662")
    public void format_ar_eg() throws Exception {
        test("new Intl.DateTimeFormat('ar-EG').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0662\u0660\u200F/\u200F\u0661\u0662\u200F/\u200F\u0662\u0660\u0661\u0662")
    public void format_ar_iq() throws Exception {
        test("new Intl.DateTimeFormat('ar-IQ').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0662\u0660\u200F/\u200F\u0661\u0662\u200F/\u200F\u0662\u0660\u0661\u0662")
    public void format_ar_jo() throws Exception {
        test("new Intl.DateTimeFormat('ar-JO').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0662\u0660\u200F/\u200F\u0661\u0662\u200F/\u200F\u0662\u0660\u0661\u0662")
    public void format_ar_kw() throws Exception {
        test("new Intl.DateTimeFormat('ar-KW').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0662\u0660\u200F/\u200F\u0661\u0662\u200F/\u200F\u0662\u0660\u0661\u0662")
    public void format_ar_lb() throws Exception {
        test("new Intl.DateTimeFormat('ar-LB').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            FF = "20\u200F/12\u200F/2012",
            IE = "\u200F20\u200F/\u200F12\u200F/\u200F2012")
    public void format_ar_ly() throws Exception {
        test("new Intl.DateTimeFormat('ar-LY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            FF = "20\u200F/12\u200F/2012",
            IE = "\u200F20\u200F-\u200F12\u200F-\u200F2012")
    public void format_ar_ma() throws Exception {
        test("new Intl.DateTimeFormat('ar-MA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0662\u0660\u200F/\u200F\u0661\u0662\u200F/\u200F\u0662\u0660\u0661\u0662")
    public void format_ar_om() throws Exception {
        test("new Intl.DateTimeFormat('ar-OM').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0662\u0660\u200F/\u200F\u0661\u0662\u200F/\u200F\u0662\u0660\u0661\u0662")
    public void format_ar_qa() throws Exception {
        test("new Intl.DateTimeFormat('ar-QA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0660\u0667\u200F/\u200F\u0660\u0662\u200F/\u200F\u0661\u0664\u0663\u0664",
            FF45 = "\u0667\u200F/\u0662\u200F/\u0661\u0664\u0663\u0664 \u0647\u0640")
    @NotYetImplemented({ IE, FF45 })
    public void format_ar_sa() throws Exception {
        test("new Intl.DateTimeFormat('ar-SA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0660\u0667\u200F/\u200F\u0660\u0662\u200F/\u200F\u0661\u0664\u0663\u0664")
    @NotYetImplemented(IE)
    public void format_ar_sd() throws Exception {
        test("new Intl.DateTimeFormat('ar-SD').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0662\u0660\u200F/\u200F\u0661\u0662\u200F/\u200F\u0662\u0660\u0661\u0662")
    public void format_ar_sy() throws Exception {
        test("new Intl.DateTimeFormat('ar-SY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            FF = "20\u200F/12\u200F/2012",
            IE = "\u200F20\u200F-\u200F12\u200F-\u200F2012")
    public void format_ar_tn() throws Exception {
        test("new Intl.DateTimeFormat('ar-TN').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0662",
            IE = "\u200F\u0662\u0660\u200F/\u200F\u0661\u0662\u200F/\u200F\u0662\u0660\u0661\u0662")
    public void format_ar_ye() throws Exception {
        test("new Intl.DateTimeFormat('ar-YE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            FF = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_be() throws Exception {
        test("new Intl.DateTimeFormat('be').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            FF = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_be_by() throws Exception {
        test("new Intl.DateTimeFormat('be-BY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012 \u0433.",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012\u200E \u0433.")
    public void format_bg() throws Exception {
        test("new Intl.DateTimeFormat('bg').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012 \u0433.",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012\u200E \u0433.")
    public void format_bg_bg() throws Exception {
        test("new Intl.DateTimeFormat('bg-BG').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_ca() throws Exception {
        test("new Intl.DateTimeFormat('ca').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_ca_es() throws Exception {
        test("new Intl.DateTimeFormat('ca-ES').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20. 12. 2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_cs() throws Exception {
        test("new Intl.DateTimeFormat('cs').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20. 12. 2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_cs_cz() throws Exception {
        test("new Intl.DateTimeFormat('cs-CZ').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E-\u200E12\u200E-\u200E2012")
    public void format_da() throws Exception {
        test("new Intl.DateTimeFormat('da').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E-\u200E12\u200E-\u200E2012")
    public void format_da_dk() throws Exception {
        test("new Intl.DateTimeFormat('da-DK').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_de() throws Exception {
        test("new Intl.DateTimeFormat('de').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_de_at() throws Exception {
        test("new Intl.DateTimeFormat('de-AT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_de_ch() throws Exception {
        test("new Intl.DateTimeFormat('de-CH').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_de_de() throws Exception {
        test("new Intl.DateTimeFormat('de-DE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_de_lu() throws Exception {
        test("new Intl.DateTimeFormat('de-LU').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_el() throws Exception {
        test("new Intl.DateTimeFormat('el').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_el_cy() throws Exception {
        test("new Intl.DateTimeFormat('el-CY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_el_gr() throws Exception {
        test("new Intl.DateTimeFormat('el-GR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2012",
            IE = "\u200E12\u200E/\u200E20\u200E/\u200E2012")
    public void format_en() throws Exception {
        test("new Intl.DateTimeFormat('en').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_en_au() throws Exception {
        test("new Intl.DateTimeFormat('en-AU').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            FF38 = "12/20/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_en_ca() throws Exception {
        test("new Intl.DateTimeFormat('en-CA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_en_gb() throws Exception {
        test("new Intl.DateTimeFormat('en-GB').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2012",
            FF = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_en_ie() throws Exception {
        test("new Intl.DateTimeFormat('en-IE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2012",
            FF = "20/12/2012",
            IE = "\u200E20\u200E-\u200E12\u200E-\u200E2012")
    public void format_en_in() throws Exception {
        test("new Intl.DateTimeFormat('en-IN').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2012",
            FF = "20/12/2012",
            IE = "\u200E12\u200E/\u200E20\u200E/\u200E2012")
    public void format_en_mt() throws Exception {
        test("new Intl.DateTimeFormat('en-MT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_en_nz() throws Exception {
        test("new Intl.DateTimeFormat('en-NZ').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2012",
            FF45 = "20/12/2012",
            IE = "\u200E12\u200E/\u200E20\u200E/\u200E2012")
    public void format_en_ph() throws Exception {
        test("new Intl.DateTimeFormat('en-PH').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2012",
            FF = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_en_sg() throws Exception {
        test("new Intl.DateTimeFormat('en-SG').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2012",
            IE = "\u200E12\u200E/\u200E20\u200E/\u200E2012")
    public void format_en_us() throws Exception {
        test("new Intl.DateTimeFormat('en-US').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012/12/20",
            IE = "\u200E2012\u200E/\u200E12\u200E/\u200E20")
    public void format_en_za() throws Exception {
        test("new Intl.DateTimeFormat('en-ZA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es() throws Exception {
        test("new Intl.DateTimeFormat('es').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_ar() throws Exception {
        test("new Intl.DateTimeFormat('es-AR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_bo() throws Exception {
        test("new Intl.DateTimeFormat('es-BO').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            FF = "20-12-2012",
            IE = "\u200E20\u200E-\u200E12\u200E-\u200E2012")
    public void format_es_cl() throws Exception {
        test("new Intl.DateTimeFormat('es-CL').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_co() throws Exception {
        test("new Intl.DateTimeFormat('es-CO').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_cr() throws Exception {
        test("new Intl.DateTimeFormat('es-CR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_do() throws Exception {
        test("new Intl.DateTimeFormat('es-DO').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_ec() throws Exception {
        test("new Intl.DateTimeFormat('es-EC').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_es() throws Exception {
        test("new Intl.DateTimeFormat('es-ES').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_gt() throws Exception {
        test("new Intl.DateTimeFormat('es-GT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_hn() throws Exception {
        test("new Intl.DateTimeFormat('es-HN').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_mx() throws Exception {
        test("new Intl.DateTimeFormat('es-MX').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_ni() throws Exception {
        test("new Intl.DateTimeFormat('es-NI').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            FF = "12/20/2012",
            IE = "\u200E12\u200E/\u200E20\u200E/\u200E2012")
    public void format_es_pa() throws Exception {
        test("new Intl.DateTimeFormat('es-PA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_pe() throws Exception {
        test("new Intl.DateTimeFormat('es-PE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            FF = "12/20/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_pr() throws Exception {
        test("new Intl.DateTimeFormat('es-PR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_py() throws Exception {
        test("new Intl.DateTimeFormat('es-PY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_sv() throws Exception {
        test("new Intl.DateTimeFormat('es-SV').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            FF38 = "12/20/2012",
            IE = "\u200E12\u200E/\u200E20\u200E/\u200E2012")
    public void format_es_us() throws Exception {
        test("new Intl.DateTimeFormat('es-US').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_uy() throws Exception {
        test("new Intl.DateTimeFormat('es-UY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_es_ve() throws Exception {
        test("new Intl.DateTimeFormat('es-VE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_et() throws Exception {
        test("new Intl.DateTimeFormat('et').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_et_ee() throws Exception {
        test("new Intl.DateTimeFormat('et-EE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_fi() throws Exception {
        test("new Intl.DateTimeFormat('fi').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_fi_fi() throws Exception {
        test("new Intl.DateTimeFormat('fi-FI').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_fr() throws Exception {
        test("new Intl.DateTimeFormat('fr').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_fr_be() throws Exception {
        test("new Intl.DateTimeFormat('fr-BE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            IE = "\u200E2012\u200E-\u200E12\u200E-\u200E20")
    public void format_fr_ca() throws Exception {
        test("new Intl.DateTimeFormat('fr-CA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_fr_ch() throws Exception {
        test("new Intl.DateTimeFormat('fr-CH').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_fr_fr() throws Exception {
        test("new Intl.DateTimeFormat('fr-FR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_fr_lu() throws Exception {
        test("new Intl.DateTimeFormat('fr-LU').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            FF45 = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_ga() throws Exception {
        test("new Intl.DateTimeFormat('ga').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            FF45 = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_ga_ie() throws Exception {
        test("new Intl.DateTimeFormat('ga-IE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E-\u200E12\u200E-\u200E2012")
    public void format_hi_in() throws Exception {
        test("new Intl.DateTimeFormat('hi-IN').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012.",
            FF38 = "20. 12. 2012.",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012\u200E.")
    public void format_hr() throws Exception {
        test("new Intl.DateTimeFormat('hr').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012.",
            FF38 = "20. 12. 2012.",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012\u200E.")
    public void format_hr_hr() throws Exception {
        test("new Intl.DateTimeFormat('hr-HR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012. 12. 20.",
            IE = "\u200E2012\u200E.\u200E12\u200E.\u200E20\u200E.")
    public void format_hu() throws Exception {
        test("new Intl.DateTimeFormat('hu').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012. 12. 20.",
            IE = "\u200E2012\u200E.\u200E12\u200E.\u200E20\u200E.")
    public void format_hu_hu() throws Exception {
        test("new Intl.DateTimeFormat('hu-HU').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_in() throws Exception {
        test("new Intl.DateTimeFormat('in').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_in_id() throws Exception {
        test("new Intl.DateTimeFormat('in-ID').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            FF = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_is() throws Exception {
        test("new Intl.DateTimeFormat('is').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            FF = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_is_is() throws Exception {
        test("new Intl.DateTimeFormat('is-IS').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_it() throws Exception {
        test("new Intl.DateTimeFormat('it').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_it_ch() throws Exception {
        test("new Intl.DateTimeFormat('it-CH').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_it_it() throws Exception {
        test("new Intl.DateTimeFormat('it-IT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_iw() throws Exception {
        test("new Intl.DateTimeFormat('iw').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_iw_il() throws Exception {
        test("new Intl.DateTimeFormat('iw-IL').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012/12/20",
            IE = "\u200E2012\u200E\u5E74\u200E12\u200E\u6708\u200E20\u200E\u65E5")
    public void format_ja() throws Exception {
        test("new Intl.DateTimeFormat('ja').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012/12/20",
            IE = "\u200E2012\u200E\u5E74\u200E12\u200E\u6708\u200E20\u200E\u65E5")
    public void format_ja_jp() throws Exception {
        test("new Intl.DateTimeFormat('ja-JP').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012. 12. 20.",
            IE = "\u200E2012\u200E\uB144 \u200E12\u200E\uC6D4 \u200E20\u200E\uC77C")
    public void format_ko() throws Exception {
        test("new Intl.DateTimeFormat('ko').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012. 12. 20.",
            IE = "\u200E2012\u200E\uB144 \u200E12\u200E\uC6D4 \u200E20\u200E\uC77C")
    public void format_ko_kr() throws Exception {
        test("new Intl.DateTimeFormat('ko-KR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            IE = "\u200E2012\u200E.\u200E12\u200E.\u200E20")
    public void format_lt() throws Exception {
        test("new Intl.DateTimeFormat('lt').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            IE = "\u200E2012\u200E.\u200E12\u200E.\u200E20")
    public void format_lt_lt() throws Exception {
        test("new Intl.DateTimeFormat('lt-LT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012.",
            IE = "\u200E2012\u200E.\u200E12\u200E.\u200E20\u200E.")
    public void format_lv() throws Exception {
        test("new Intl.DateTimeFormat('lv').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012.",
            IE = "\u200E2012\u200E.\u200E12\u200E.\u200E20\u200E.")
    public void format_lv_lv() throws Exception {
        test("new Intl.DateTimeFormat('lv-LV').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            FF = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_mk() throws Exception {
        test("new Intl.DateTimeFormat('mk').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            FF = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_mk_mk() throws Exception {
        test("new Intl.DateTimeFormat('mk-MK').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_ms() throws Exception {
        test("new Intl.DateTimeFormat('ms').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_ms_my() throws Exception {
        test("new Intl.DateTimeFormat('ms-MY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_mt() throws Exception {
        test("new Intl.DateTimeFormat('mt').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_mt_mt() throws Exception {
        test("new Intl.DateTimeFormat('mt-MT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20-12-2012",
            IE = "\u200E20\u200E-\u200E12\u200E-\u200E2012")
    public void format_nl() throws Exception {
        test("new Intl.DateTimeFormat('nl').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20-12-2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_nl_be() throws Exception {
        test("new Intl.DateTimeFormat('nl-BE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20-12-2012",
            IE = "\u200E20\u200E-\u200E12\u200E-\u200E2012")
    public void format_nl_nl() throws Exception {
        test("new Intl.DateTimeFormat('nl-NL').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_no() throws Exception {
        test("new Intl.DateTimeFormat('no').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_no_no() throws Exception {
        test("new Intl.DateTimeFormat('no-NO').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "exception")
    @NotYetImplemented
    public void format_no_no_ny() throws Exception {
        test("new Intl.DateTimeFormat('no-NO-NY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E2012\u200E-\u200E12\u200E-\u200E20")
    public void format_pl() throws Exception {
        test("new Intl.DateTimeFormat('pl').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E2012\u200E-\u200E12\u200E-\u200E20")
    public void format_pl_pl() throws Exception {
        test("new Intl.DateTimeFormat('pl-PL').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_pt() throws Exception {
        test("new Intl.DateTimeFormat('pt').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_pt_br() throws Exception {
        test("new Intl.DateTimeFormat('pt-BR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E-\u200E12\u200E-\u200E2012")
    public void format_pt_pt() throws Exception {
        test("new Intl.DateTimeFormat('pt-PT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_ro() throws Exception {
        test("new Intl.DateTimeFormat('ro').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_ro_ro() throws Exception {
        test("new Intl.DateTimeFormat('ro-RO').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_ru() throws Exception {
        test("new Intl.DateTimeFormat('ru').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_ru_ru() throws Exception {
        test("new Intl.DateTimeFormat('ru-RU').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20. 12. 2012",
            FF38 = "20.12.2012",
            IE = "\u200E20\u200E. \u200E12\u200E. \u200E2012")
    public void format_sk() throws Exception {
        test("new Intl.DateTimeFormat('sk').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20. 12. 2012",
            FF38 = "20.12.2012",
            IE = "\u200E20\u200E. \u200E12\u200E. \u200E2012")
    public void format_sk_sk() throws Exception {
        test("new Intl.DateTimeFormat('sk-SK').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20. 12. 2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_sl() throws Exception {
        test("new Intl.DateTimeFormat('sl').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20. 12. 2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_sl_si() throws Exception {
        test("new Intl.DateTimeFormat('sl-SI').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            FF38 = "20/12/2012",
            FF45 = "20.12.2012",
            IE = "\u200E2012\u200E-\u200E12\u200E-\u200E20")
    public void format_sq() throws Exception {
        test("new Intl.DateTimeFormat('sq').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            FF38 = "20/12/2012",
            FF45 = "20.12.2012",
            IE = "\u200E2012\u200E-\u200E12\u200E-\u200E20")
    public void format_sq_al() throws Exception {
        test("new Intl.DateTimeFormat('sq-AL').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012.",
            FF38 = "20. 12. 2012.",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_sr() throws Exception {
        test("new Intl.DateTimeFormat('sr').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012.",
            FF38 = "20. 12. 2012.",
            IE = "\u200E12\u200E/\u200E20\u200E/\u200E2012")
    public void format_sr_ba() throws Exception {
        test("new Intl.DateTimeFormat('sr-BA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012.",
            FF38 = "20. 12. 2012.",
            IE = "\u200E12\u200E/\u200E20\u200E/\u200E2012")
    public void format_sr_cs() throws Exception {
        test("new Intl.DateTimeFormat('sr-CS').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012.",
            FF38 = "20. 12. 2012.",
            IE = "\u200E12\u200E/\u200E20\u200E/\u200E2012")
    public void format_sr_me() throws Exception {
        test("new Intl.DateTimeFormat('sr-ME').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012.",
            FF38 = "20. 12. 2012.",
            IE = "\u200E12\u200E/\u200E20\u200E/\u200E2012")
    public void format_sr_rs() throws Exception {
        test("new Intl.DateTimeFormat('sr-RS').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            IE = "\u200E2012\u200E-\u200E12\u200E-\u200E20")
    public void format_sv() throws Exception {
        test("new Intl.DateTimeFormat('sv').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012-12-20",
            IE = "\u200E2012\u200E-\u200E12\u200E-\u200E20")
    public void format_sv_se() throws Exception {
        test("new Intl.DateTimeFormat('sv-SE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2555",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2555")
    @NotYetImplemented
    public void format_th() throws Exception {
        test("new Intl.DateTimeFormat('th').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2555",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2555")
    @NotYetImplemented
    public void format_th_th() throws Exception {
        test("new Intl.DateTimeFormat('th-TH').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_tr() throws Exception {
        test("new Intl.DateTimeFormat('tr').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_tr_tr() throws Exception {
        test("new Intl.DateTimeFormat('tr-TR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_uk() throws Exception {
        test("new Intl.DateTimeFormat('uk').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2012",
            IE = "\u200E20\u200E.\u200E12\u200E.\u200E2012")
    public void format_uk_ua() throws Exception {
        test("new Intl.DateTimeFormat('uk-UA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_vi() throws Exception {
        test("new Intl.DateTimeFormat('vi').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E20\u200E/\u200E12\u200E/\u200E2012")
    public void format_vi_vn() throws Exception {
        test("new Intl.DateTimeFormat('vi-VN').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012/12/20",
            IE = "\u200E2012\u200E\u5E74\u200E12\u200E\u6708\u200E20\u200E\u65E5")
    public void format_zh() throws Exception {
        test("new Intl.DateTimeFormat('zh').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012/12/20",
            IE = "\u200E2012\u200E\u5E74\u200E12\u200E\u6708\u200E20\u200E\u65E5")
    public void format_zh_cn() throws Exception {
        test("new Intl.DateTimeFormat('zh-CN').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2012",
            IE = "\u200E2012\u200E\u5E74\u200E12\u200E\u6708\u200E20\u200E\u65E5")
    public void format_zh_hk() throws Exception {
        test("new Intl.DateTimeFormat('zh-HK').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012\u5E7412\u670820\u65E5",
            IE = "\u200E2012\u200E\u5E74\u200E12\u200E\u6708\u200E20\u200E\u65E5")
    public void format_zh_sg() throws Exception {
        test("new Intl.DateTimeFormat('zh-SG').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2012/12/20",
            IE = "\u200E2012\u200E\u5E74\u200E12\u200E\u6708\u200E20\u200E\u65E5")
    public void format_zh_tw() throws Exception {
        test("new Intl.DateTimeFormat('zh-TW').format(date)");
    }

}
