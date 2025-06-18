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
package org.htmlunit.javascript.host.intl;

import org.apache.commons.lang3.CharUtils;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * Tests for {@link DateTimeFormat}.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
public class DateTimeFormat2Test extends WebDriverTestCase {

    private void test(final String... string) throws Exception {
        final StringBuilder html = new StringBuilder(DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    var date = new Date(Date.UTC(2013, 11, 20, 7, 0, 0));\n"
            + "    try {\n");
        for (int i = 0; i < string.length - 1; i++) {
            html.append(string[i]).append("\n");
        }
        html.append(
            "      log(" + string[string.length - 1] + ");\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body></html>");

        try {
            loadPageVerifyTextArea2(html.toString());
        }
        catch (final AssertionFailedError e) {
            final String msg = e.getMessage();
            for (int i = 0; i < msg.length(); i++) {
                final char c = msg.charAt(i);
                if (CharUtils.isAscii(c)) {
                    System.out.print(c);
                }
                else {
                    System.out.print(CharUtils.unicodeEscaped(c));
                }
            }
            System.out.println();
            throw e;
        }
    }

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
    @Alerts("12/20/2013")
    public void format_default() throws Exception {
        test("new Intl.DateTimeFormat().format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("H25/12/20")
    public void format_ja_jp_u_ca_japanese() throws Exception {
        test("new Intl.DateTimeFormat('ja-JP-u-ca-japanese').format(date)");
    }

    /**
     * Test not supported locale.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12/20/2013")
    public void format_ban() throws Exception {
        test("new Intl.DateTimeFormat('ban').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_id() throws Exception {
        test("new Intl.DateTimeFormat('id').format(date)");
    }

    /**
     * Test the fallback language, in this case {@code Indonesian}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_ban_id() throws Exception {
        test("new Intl.DateTimeFormat(['ban', 'id']).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Freitag, 20. Dezember 2013")
    @HtmlUnitNYI(CHROME = "20.12.2013",
            EDGE = "20.12.2013",
            FF = "20.12.2013",
            FF_ESR = "20.12.2013")
    public void format_weekday_long() throws Exception {
        test("var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };",
                "new Intl.DateTimeFormat('de-DE', options).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Freitag, 20. Dezember 2013 n. Chr. um 02:00:00")
    @BuggyWebDriver(FF = "Freitag, 20. Dezember 2013 n. Chr. um 08:00:00",
                    FF_ESR = "Freitag, 20. Dezember 2013 n. Chr. um 08:00:00")
    @HtmlUnitNYI(CHROME = "20.12.2013",
            EDGE = "20.12.2013",
            FF = "20.12.2013",
            FF_ESR = "20.12.2013")
    public void format_weekday_long_all() throws Exception {
        test("var options = { weekday: 'long', era: 'long', year: 'numeric', month: 'long', day: 'numeric',"
                + " hour: 'numeric', minute: 'numeric', second: 'numeric' };",
                "new Intl.DateTimeFormat('de-DE', options).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Freitag")
    @HtmlUnitNYI(CHROME = "20.12.2013",
            EDGE = "20.12.2013",
            FF = "20.12.2013",
            FF_ESR = "20.12.2013")
    public void format_weekday_long_all_weekday() throws Exception {
        test("var options = { weekday: 'long' };",
                "new Intl.DateTimeFormat('de-DE', options).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("n. Chr. Freitag")
    @HtmlUnitNYI(CHROME = "20.12.2013",
            EDGE = "20.12.2013",
            FF = "20.12.2013",
            FF_ESR = "20.12.2013")
    public void format_weekday_long_weekday_era() throws Exception {
        test("var options = { weekday: 'long', era: 'long' };",
                "new Intl.DateTimeFormat('de-DE', options).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013 n. Chr.")
    @HtmlUnitNYI(CHROME = "20.12.2013",
            EDGE = "20.12.2013",
            FF = "20.12.2013",
            FF_ESR = "20.12.2013")
    public void format_weekday_long_era() throws Exception {
        test("var options = { era: 'long' };",
                "new Intl.DateTimeFormat('de-DE', options).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013 n. Chr. Freitag")
    @HtmlUnitNYI(CHROME = "20.12.2013",
            EDGE = "20.12.2013",
            FF = "20.12.2013",
            FF_ESR = "20.12.2013")
    public void format_weekday_long_weekday_era_year() throws Exception {
        test("var options = { weekday: 'long', era: 'long', year: 'numeric' };",
                "new Intl.DateTimeFormat('de-DE', options).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Dezember 2013 n. Chr. Freitag")
    @HtmlUnitNYI(CHROME = "20.12.2013",
            EDGE = "20.12.2013",
            FF = "20.12.2013",
            FF_ESR = "20.12.2013")
    public void format_weekday_long_weekday_era_year_month() throws Exception {
        test("var options = { weekday: 'long', era: 'long', year: 'numeric', month: 'long' };",
                "new Intl.DateTimeFormat('de-DE', options).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0627\u0644\u062c\u0645\u0639\u0629\u060c 20 \u062f\u064a\u0633\u0645\u0628\u0631 2013 "
                    + "\u0645\u064a\u0644\u0627\u062f\u064a \u0641\u064a 2:00:00 \u0635",
            FF = "\u0627\u0644\u062c\u0645\u0639\u0629\u060c 20 \u062f\u064a\u0633\u0645\u0628\u0631 2013 "
                    + "\u0645\u064a\u0644\u0627\u062f\u064a \u0641\u064a 8:00:00 \u0635",
            FF_ESR = "\u0627\u0644\u062c\u0645\u0639\u0629\u060c \u0662\u0660 \u062f\u064a\u0633\u0645\u0628\u0631 "
                    + "\u0662\u0660\u0661\u0663 \u0645\u064a\u0644\u0627\u062f\u064a "
                    + "\u0641\u064a \u0668:\u0660\u0660:\u0660\u0660 \u0635")
    @HtmlUnitNYI(CHROME = "\u0662\u0660\u200f/\u0661\u0662\u200f/\u0662\u0660\u0661\u0663",
            EDGE = "\u0662\u0660\u200f/\u0661\u0662\u200f/\u0662\u0660\u0661\u0663",
            FF = "\u0662\u0660\u200f/\u0661\u0662\u200f/\u0662\u0660\u0661\u0663",
            FF_ESR = "\u0662\u0660\u200f/\u0661\u0662\u200f/\u0662\u0660\u0661\u0663")
    public void format_weekday_long_all_ar() throws Exception {
        test("var options = { weekday: 'long', era: 'long', year: 'numeric', month: 'long', day: 'numeric',"
                + " hour: 'numeric', minute: 'numeric', second: 'numeric' };",
                "new Intl.DateTimeFormat('ar', options).format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "Friday, December 20, 2013 at UTC",
            EDGE = "Friday, December 20, 2013 at UTC",
            FF = "Friday, December 20, 2013 at UTC",
            FF_ESR = "Friday, December 20, 2013 at UTC")
    @HtmlUnitNYI(CHROME = "12/20/2013",
            EDGE = "12/20/2013",
            FF = "12/20/2013",
            FF_ESR = "12/20/2013")
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
    @Alerts("2:00:00 am GMT-5")
    @BuggyWebDriver(FF = "8:00:00 am GMT+1", FF_ESR = "8:00:00 am GMT+1")
    @HtmlUnitNYI(CHROME = "20/12/2013",
            EDGE = "20/12/2013",
            FF = "20/12/2013",
            FF_ESR = "20/12/2013")
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
    @Alerts("12/20/2013, 02:00:00")
    @BuggyWebDriver(FF = "12/20/2013, 08:00:00", FF_ESR = "12/20/2013, 08:00:00")
    @HtmlUnitNYI(CHROME = "12/20/2013",
            EDGE = "12/20/2013",
            FF = "12/20/2013",
            FF_ESR = "12/20/2013")
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
    @Alerts(DEFAULT = "20\u200f/12\u200f/2013",
            FF_ESR = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    @HtmlUnitNYI(CHROME = "\u0662\u0660\u200f/\u0661\u0662\u200f/\u0662\u0660\u0661\u0663",
            EDGE = "\u0662\u0660\u200f/\u0661\u0662\u200f/\u0662\u0660\u0661\u0663",
            FF = "\u0662\u0660\u200f/\u0661\u0662\u200f/\u0662\u0660\u0661\u0663")
    public void format_ar() throws Exception {
        test("new Intl.DateTimeFormat('ar').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20‏/12‏/2013")
    @HtmlUnitNYI(CHROME = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663",
                 EDGE = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663",
                 FF = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663",
                 FF_ESR = "\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    public void format_ar_ae() throws Exception {
        test("new Intl.DateTimeFormat('ar-AE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    public void format_ar_bh() throws Exception {
        test("new Intl.DateTimeFormat('ar-BH').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20\u200F/12\u200F/2013")
    public void format_ar_dz() throws Exception {
        test("new Intl.DateTimeFormat('ar-DZ').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    public void format_ar_eg() throws Exception {
        test("new Intl.DateTimeFormat('ar-EG').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    public void format_ar_iq() throws Exception {
        test("new Intl.DateTimeFormat('ar-IQ').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    public void format_ar_jo() throws Exception {
        test("new Intl.DateTimeFormat('ar-JO').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    public void format_ar_kw() throws Exception {
        test("new Intl.DateTimeFormat('ar-KW').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    public void format_ar_lb() throws Exception {
        test("new Intl.DateTimeFormat('ar-LB').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20\u200F/12\u200F/2013")
    public void format_ar_ly() throws Exception {
        test("new Intl.DateTimeFormat('ar-LY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20\u200F/12\u200F/2013")
    public void format_ar_ma() throws Exception {
        test("new Intl.DateTimeFormat('ar-MA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    public void format_ar_om() throws Exception {
        test("new Intl.DateTimeFormat('ar-OM').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    public void format_ar_qa() throws Exception {
        test("new Intl.DateTimeFormat('ar-QA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "\u0661\u0667\u200f/\u0662\u200f/\u0661\u0664\u0663\u0665 \u0647\u0640",
            FF = "\u0662\u0660\u200f/\u0661\u0662\u200f/\u0662\u0660\u0661\u0663")
    public void format_ar_sa() throws Exception {
        test("new Intl.DateTimeFormat('ar-SA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    public void format_ar_sd() throws Exception {
        test("new Intl.DateTimeFormat('ar-SD').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    public void format_ar_sy() throws Exception {
        test("new Intl.DateTimeFormat('ar-SY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20\u200F/12\u200F/2013")
    public void format_ar_tn() throws Exception {
        test("new Intl.DateTimeFormat('ar-TN').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0662\u0660\u200F/\u0661\u0662\u200F/\u0662\u0660\u0661\u0663")
    public void format_ar_ye() throws Exception {
        test("new Intl.DateTimeFormat('ar-YE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2013",
            CHROME = "12/20/2013",
            EDGE = "12/20/2013")
    public void format_be() throws Exception {
        test("new Intl.DateTimeFormat('be').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2013",
            CHROME = "12/20/2013",
            EDGE = "12/20/2013")
    public void format_be_by() throws Exception {
        test("new Intl.DateTimeFormat('be-BY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013 \u0433.")
    public void format_bg() throws Exception {
        test("new Intl.DateTimeFormat('bg').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013 \u0433.")
    public void format_bg_bg() throws Exception {
        test("new Intl.DateTimeFormat('bg-BG').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_ca() throws Exception {
        test("new Intl.DateTimeFormat('ca').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_ca_es() throws Exception {
        test("new Intl.DateTimeFormat('ca-ES').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013")
    public void format_cs() throws Exception {
        test("new Intl.DateTimeFormat('cs').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013")
    public void format_cs_cz() throws Exception {
        test("new Intl.DateTimeFormat('cs-CZ').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_da() throws Exception {
        test("new Intl.DateTimeFormat('da').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_da_dk() throws Exception {
        test("new Intl.DateTimeFormat('da-DK').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_de() throws Exception {
        test("new Intl.DateTimeFormat('de').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_de_at() throws Exception {
        test("new Intl.DateTimeFormat('de-AT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_de_ch() throws Exception {
        test("new Intl.DateTimeFormat('de-CH').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_de_de() throws Exception {
        test("new Intl.DateTimeFormat('de-DE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_de_lu() throws Exception {
        test("new Intl.DateTimeFormat('de-LU').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_el() throws Exception {
        test("new Intl.DateTimeFormat('el').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_el_cy() throws Exception {
        test("new Intl.DateTimeFormat('el-CY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_el_gr() throws Exception {
        test("new Intl.DateTimeFormat('el-GR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12/20/2013")
    public void format_en() throws Exception {
        test("new Intl.DateTimeFormat('en').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_en_au() throws Exception {
        test("new Intl.DateTimeFormat('en-AU').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013-12-20")
    public void format_en_ca() throws Exception {
        test("new Intl.DateTimeFormat('en-CA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_en_gb() throws Exception {
        test("new Intl.DateTimeFormat('en-GB').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_en_ie() throws Exception {
        test("new Intl.DateTimeFormat('en-IE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_en_in() throws Exception {
        test("new Intl.DateTimeFormat('en-IN').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_en_mt() throws Exception {
        test("new Intl.DateTimeFormat('en-MT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_en_nz() throws Exception {
        test("new Intl.DateTimeFormat('en-NZ').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12/20/2013")
    public void format_en_ph() throws Exception {
        test("new Intl.DateTimeFormat('en-PH').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_en_sg() throws Exception {
        test("new Intl.DateTimeFormat('en-SG').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12/20/2013")
    public void format_en_us() throws Exception {
        test("new Intl.DateTimeFormat('en-US').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013/12/20")
    public void format_en_za() throws Exception {
        test("new Intl.DateTimeFormat('en-ZA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es() throws Exception {
        test("new Intl.DateTimeFormat('es').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_ar() throws Exception {
        test("new Intl.DateTimeFormat('es-AR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_bo() throws Exception {
        test("new Intl.DateTimeFormat('es-BO').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20-12-2013")
    public void format_es_cl() throws Exception {
        test("new Intl.DateTimeFormat('es-CL').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_co() throws Exception {
        test("new Intl.DateTimeFormat('es-CO').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_cr() throws Exception {
        test("new Intl.DateTimeFormat('es-CR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_do() throws Exception {
        test("new Intl.DateTimeFormat('es-DO').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_ec() throws Exception {
        test("new Intl.DateTimeFormat('es-EC').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_es() throws Exception {
        test("new Intl.DateTimeFormat('es-ES').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_gt() throws Exception {
        test("new Intl.DateTimeFormat('es-GT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_hn() throws Exception {
        test("new Intl.DateTimeFormat('es-HN').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_mx() throws Exception {
        test("new Intl.DateTimeFormat('es-MX').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_ni() throws Exception {
        test("new Intl.DateTimeFormat('es-NI').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12/20/2013")
    public void format_es_pa() throws Exception {
        test("new Intl.DateTimeFormat('es-PA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_pe() throws Exception {
        test("new Intl.DateTimeFormat('es-PE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12/20/2013")
    public void format_es_pr() throws Exception {
        test("new Intl.DateTimeFormat('es-PR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_py() throws Exception {
        test("new Intl.DateTimeFormat('es-PY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_sv() throws Exception {
        test("new Intl.DateTimeFormat('es-SV').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_us() throws Exception {
        test("new Intl.DateTimeFormat('es-US').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_uy() throws Exception {
        test("new Intl.DateTimeFormat('es-UY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_es_ve() throws Exception {
        test("new Intl.DateTimeFormat('es-VE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_et() throws Exception {
        test("new Intl.DateTimeFormat('et').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_et_ee() throws Exception {
        test("new Intl.DateTimeFormat('et-EE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_fi() throws Exception {
        test("new Intl.DateTimeFormat('fi').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_fi_fi() throws Exception {
        test("new Intl.DateTimeFormat('fi-FI').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_fr() throws Exception {
        test("new Intl.DateTimeFormat('fr').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_fr_be() throws Exception {
        test("new Intl.DateTimeFormat('fr-BE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013-12-20")
    public void format_fr_ca() throws Exception {
        test("new Intl.DateTimeFormat('fr-CA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_fr_ch() throws Exception {
        test("new Intl.DateTimeFormat('fr-CH').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_fr_fr() throws Exception {
        test("new Intl.DateTimeFormat('fr-FR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_fr_lu() throws Exception {
        test("new Intl.DateTimeFormat('fr-LU').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2013",
            CHROME = "12/20/2013",
            EDGE = "12/20/2013")
    public void format_ga() throws Exception {
        test("new Intl.DateTimeFormat('ga').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20/12/2013",
            CHROME = "12/20/2013",
            EDGE = "12/20/2013")
    public void format_ga_ie() throws Exception {
        test("new Intl.DateTimeFormat('ga-IE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_hi_in() throws Exception {
        test("new Intl.DateTimeFormat('hi-IN').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013.")
    public void format_hr() throws Exception {
        test("new Intl.DateTimeFormat('hr').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013.")
    public void format_hr_hr() throws Exception {
        test("new Intl.DateTimeFormat('hr-HR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013. 12. 20.")
    public void format_hu() throws Exception {
        test("new Intl.DateTimeFormat('hu').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013. 12. 20.")
    public void format_hu_hu() throws Exception {
        test("new Intl.DateTimeFormat('hu-HU').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_in() throws Exception {
        test("new Intl.DateTimeFormat('in').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_in_id() throws Exception {
        test("new Intl.DateTimeFormat('in-ID').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2013",
            CHROME = "12/20/2013",
            EDGE = "12/20/2013")
    public void format_is() throws Exception {
        test("new Intl.DateTimeFormat('is').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2013",
            CHROME = "12/20/2013",
            EDGE = "12/20/2013")
    public void format_is_is() throws Exception {
        test("new Intl.DateTimeFormat('is-IS').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_it() throws Exception {
        test("new Intl.DateTimeFormat('it').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_it_ch() throws Exception {
        test("new Intl.DateTimeFormat('it-CH').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_it_it() throws Exception {
        test("new Intl.DateTimeFormat('it-IT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_iw() throws Exception {
        test("new Intl.DateTimeFormat('iw').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_iw_il() throws Exception {
        test("new Intl.DateTimeFormat('iw-IL').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013/12/20")
    public void format_ja() throws Exception {
        test("new Intl.DateTimeFormat('ja').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013/12/20")
    public void format_ja_jp() throws Exception {
        test("new Intl.DateTimeFormat('ja-JP').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013. 12. 20.")
    public void format_ko() throws Exception {
        test("new Intl.DateTimeFormat('ko').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013. 12. 20.")
    public void format_ko_kr() throws Exception {
        test("new Intl.DateTimeFormat('ko-KR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013-12-20")
    public void format_lt() throws Exception {
        test("new Intl.DateTimeFormat('lt').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013-12-20")
    public void format_lt_lt() throws Exception {
        test("new Intl.DateTimeFormat('lt-LT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013.")
    public void format_lv() throws Exception {
        test("new Intl.DateTimeFormat('lv').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013.")
    public void format_lv_lv() throws Exception {
        test("new Intl.DateTimeFormat('lv-LV').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2013",
            FF = "20.12.2013 \u0433.",
            FF_ESR = "20.12.2013")
    public void format_mk() throws Exception {
        test("new Intl.DateTimeFormat('mk').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12/20/2013",
            FF = "20.12.2013 \u0433.",
            FF_ESR = "20.12.2013")
    public void format_mk_mk() throws Exception {
        test("new Intl.DateTimeFormat('mk-MK').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_ms() throws Exception {
        test("new Intl.DateTimeFormat('ms').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_ms_my() throws Exception {
        test("new Intl.DateTimeFormat('ms-MY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12/20/2013")
    public void format_mt() throws Exception {
        test("new Intl.DateTimeFormat('mt').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12/20/2013")
    public void format_mt_mt() throws Exception {
        test("new Intl.DateTimeFormat('mt-MT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20-12-2013")
    public void format_nl() throws Exception {
        test("new Intl.DateTimeFormat('nl').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_nl_be() throws Exception {
        test("new Intl.DateTimeFormat('nl-BE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20-12-2013")
    public void format_nl_nl() throws Exception {
        test("new Intl.DateTimeFormat('nl-NL').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_no() throws Exception {
        test("new Intl.DateTimeFormat('no').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_no_no() throws Exception {
        test("new Intl.DateTimeFormat('no-NO').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void format_no_no_ny() throws Exception {
        test("new Intl.DateTimeFormat('no-NO-NY').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_pl() throws Exception {
        test("new Intl.DateTimeFormat('pl').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_pl_pl() throws Exception {
        test("new Intl.DateTimeFormat('pl-PL').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_pt() throws Exception {
        test("new Intl.DateTimeFormat('pt').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_pt_br() throws Exception {
        test("new Intl.DateTimeFormat('pt-BR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_pt_pt() throws Exception {
        test("new Intl.DateTimeFormat('pt-PT').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_ro() throws Exception {
        test("new Intl.DateTimeFormat('ro').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_ro_ro() throws Exception {
        test("new Intl.DateTimeFormat('ro-RO').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_ru() throws Exception {
        test("new Intl.DateTimeFormat('ru').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_ru_ru() throws Exception {
        test("new Intl.DateTimeFormat('ru-RU').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013")
    public void format_sk() throws Exception {
        test("new Intl.DateTimeFormat('sk').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013")
    public void format_sk_sk() throws Exception {
        test("new Intl.DateTimeFormat('sk-SK').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013")
    public void format_sl() throws Exception {
        test("new Intl.DateTimeFormat('sl').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013")
    public void format_sl_si() throws Exception {
        test("new Intl.DateTimeFormat('sl-SI').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2013",
            CHROME = "12/20/2013")
    public void format_sq() throws Exception {
        test("new Intl.DateTimeFormat('sq').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20.12.2013",
            CHROME = "12/20/2013")
    public void format_sq_al() throws Exception {
        test("new Intl.DateTimeFormat('sq-AL').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013.")
    public void format_sr() throws Exception {
        test("new Intl.DateTimeFormat('sr').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013.")
    public void format_sr_ba() throws Exception {
        test("new Intl.DateTimeFormat('sr-BA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013.")
    public void format_sr_cs() throws Exception {
        test("new Intl.DateTimeFormat('sr-CS').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013.")
    public void format_sr_me() throws Exception {
        test("new Intl.DateTimeFormat('sr-ME').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20. 12. 2013.")
    public void format_sr_rs() throws Exception {
        test("new Intl.DateTimeFormat('sr-RS').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013-12-20")
    public void format_sv() throws Exception {
        test("new Intl.DateTimeFormat('sv').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013-12-20")
    public void format_sv_se() throws Exception {
        test("new Intl.DateTimeFormat('sv-SE').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2556")
    public void format_th() throws Exception {
        test("new Intl.DateTimeFormat('th').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2556")
    public void format_th_th() throws Exception {
        test("new Intl.DateTimeFormat('th-TH').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_tr() throws Exception {
        test("new Intl.DateTimeFormat('tr').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_tr_tr() throws Exception {
        test("new Intl.DateTimeFormat('tr-TR').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_uk() throws Exception {
        test("new Intl.DateTimeFormat('uk').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20.12.2013")
    public void format_uk_ua() throws Exception {
        test("new Intl.DateTimeFormat('uk-UA').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_vi() throws Exception {
        test("new Intl.DateTimeFormat('vi').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_vi_vn() throws Exception {
        test("new Intl.DateTimeFormat('vi-VN').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013/12/20")
    public void format_zh() throws Exception {
        test("new Intl.DateTimeFormat('zh').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013/12/20")
    public void format_zh_cn() throws Exception {
        test("new Intl.DateTimeFormat('zh-CN').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20/12/2013")
    public void format_zh_hk() throws Exception {
        test("new Intl.DateTimeFormat('zh-HK').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013\u5E7412\u670820\u65E5")
    public void format_zh_sg() throws Exception {
        test("new Intl.DateTimeFormat('zh-SG').format(date)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2013/12/20")
    public void format_zh_tw() throws Exception {
        test("new Intl.DateTimeFormat('zh-TW').format(date)");
    }
}
