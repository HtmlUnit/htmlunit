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

import org.apache.commons.lang3.CharUtils;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests for {@link NumberFormat}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class NumberFormatTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object Intl]",
            IE = "[object Object]")
    public void intl() throws Exception {
        test("Intl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"zh-CN", "latn", "standard", "auto", "decimal", "1", "0", "3", "auto"},
            FF = {"zh-CN", "latn", "standard", "auto", "decimal", "1", "0", "3", "true"},
            FF_ESR = {"zh-CN", "latn", "standard", "auto", "decimal", "1", "0", "3", "true"},
            IE = {"zh-Hans-CN", "latn", "undefined", "undefined", "decimal", "1", "0", "3", "true"})
    @HtmlUnitNYI(CHROME = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                           "undefined", "undefined"},
            EDGE = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                    "undefined", "undefined"},
            FF = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                  "undefined", "undefined"},
            FF_ESR = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                      "undefined", "undefined"},
            IE = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                  "undefined", "undefined"})
    public void resolvedOptionsValues() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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

    private void test(final String... string) throws Exception {
        final StringBuilder html = new StringBuilder(HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    var number = 31415.9265359;\n"
            + "    try {\n");
        for (int i = 0; i < string.length - 1; i++) {
            html.append(string[i]).append("\n");
        }
        html.append(
            "      log(" + string[string.length - 1] + ");\n"
            + "    } catch(e) {log('exception')}\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body></html>");

        try {
            loadPageVerifyTextArea2(html.toString());
        }
        catch (final ComparisonFailure e) {
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
    @Alerts("0")
    public void format_zero() throws Exception {
        test("new Intl.NumberFormat('en').format(0)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("7")
    public void format_int() throws Exception {
        test("new Intl.NumberFormat('en').format(7)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0.012")
    public void format_zeroDot() throws Exception {
        test("new Intl.NumberFormat('en').format(0.0123)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    @BuggyWebDriver(IE = "31.415,927")
    public void format_default() throws Exception {
        test("new Intl.NumberFormat().format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_ja_jp_u_ca_japanese() throws Exception {
        test("new Intl.NumberFormat('ja-JP-u-ca-japanese').format(number)");
    }

    /**
     * Test not supported locale.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31,415.927",
            IE = "31.415,927")
    public void format_ban() throws Exception {
        test("new Intl.NumberFormat('ban').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_id() throws Exception {
        test("new Intl.NumberFormat('id').format(number)");
    }

    /**
     * Test the fallback language, in this case {@code Indonesian}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_ban_id() throws Exception {
        test("new Intl.NumberFormat(['ban', 'id']).format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31,415.927",
            FF = "\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667",
            FF_ESR = "\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667",
            IE = "\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar() throws Exception {
        test("new Intl.NumberFormat('ar').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31,415.927",
            IE = "\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_ae() throws Exception {
        test("new Intl.NumberFormat('ar-AE').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_bh() throws Exception {
        test("new Intl.NumberFormat('ar-BH').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31.415,927",
            IE = "31,415.927")
    public void format_ar_dz() throws Exception {
        test("new Intl.NumberFormat('ar-DZ').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_eg() throws Exception {
        test("new Intl.NumberFormat('ar-EG').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_iq() throws Exception {
        test("new Intl.NumberFormat('ar-IQ').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_jo() throws Exception {
        test("new Intl.NumberFormat('ar-JO').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_kw() throws Exception {
        test("new Intl.NumberFormat('ar-KW').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_lb() throws Exception {
        test("new Intl.NumberFormat('ar-LB').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31.415,927",
            IE = "31,415.927")
    public void format_ar_ly() throws Exception {
        test("new Intl.NumberFormat('ar-LY').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31.415,927",
            IE = "31,415.927")
    public void format_ar_ma() throws Exception {
        test("new Intl.NumberFormat('ar-MA').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_om() throws Exception {
        test("new Intl.NumberFormat('ar-OM').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_qa() throws Exception {
        test("new Intl.NumberFormat('ar-QA').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_sa() throws Exception {
        test("new Intl.NumberFormat('ar-SA').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_sd() throws Exception {
        test("new Intl.NumberFormat('ar-SD').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_sy() throws Exception {
        test("new Intl.NumberFormat('ar-SY').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31.415,927",
            IE = "31,415.927")
    public void format_ar_tn() throws Exception {
        test("new Intl.NumberFormat('ar-TN').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0663\u0661\u066c\u0664\u0661\u0665\u066b\u0669\u0662\u0667")
    public void format_ar_ye() throws Exception {
        test("new Intl.NumberFormat('ar-YE').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31,415.927",
            EDGE = "31\u00a0415,927",
            FF = "31\u00a0415,927",
            FF_ESR = "31\u00a0415,927",
            IE = "31\u00a0415,927")
    public void format_be() throws Exception {
        test("new Intl.NumberFormat('be').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31,415.927",
            EDGE = "31\u00a0415,927",
            FF = "31\u00a0415,927",
            FF_ESR = "31\u00a0415,927",
            IE = "31\u00a0415,927")
    public void format_be_by() throws Exception {
        test("new Intl.NumberFormat('be-BY').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_bg() throws Exception {
        test("new Intl.NumberFormat('bg').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_bg_bg() throws Exception {
        test("new Intl.NumberFormat('bg-BG').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_ca() throws Exception {
        test("new Intl.NumberFormat('ca').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_ca_es() throws Exception {
        test("new Intl.NumberFormat('ca-ES').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_cs() throws Exception {
        test("new Intl.NumberFormat('cs').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_cs_cz() throws Exception {
        test("new Intl.NumberFormat('cs-CZ').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_da() throws Exception {
        test("new Intl.NumberFormat('da').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_da_dk() throws Exception {
        test("new Intl.NumberFormat('da-DK').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_de() throws Exception {
        test("new Intl.NumberFormat('de').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_de_at() throws Exception {
        test("new Intl.NumberFormat('de-AT').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u2019415.927")
    public void format_de_ch() throws Exception {
        test("new Intl.NumberFormat('de-CH').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_de_de() throws Exception {
        test("new Intl.NumberFormat('de-DE').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_de_lu() throws Exception {
        test("new Intl.NumberFormat('de-LU').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_el() throws Exception {
        test("new Intl.NumberFormat('el').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_el_cy() throws Exception {
        test("new Intl.NumberFormat('el-CY').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_el_gr() throws Exception {
        test("new Intl.NumberFormat('el-GR').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_en() throws Exception {
        test("new Intl.NumberFormat('en').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_en_au() throws Exception {
        test("new Intl.NumberFormat('en-AU').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_en_ca() throws Exception {
        test("new Intl.NumberFormat('en-CA').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_en_gb() throws Exception {
        test("new Intl.NumberFormat('en-GB').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_en_ie() throws Exception {
        test("new Intl.NumberFormat('en-IE').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_en_in() throws Exception {
        test("new Intl.NumberFormat('en-IN').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_en_mt() throws Exception {
        test("new Intl.NumberFormat('en-MT').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_en_nz() throws Exception {
        test("new Intl.NumberFormat('en-NZ').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_en_ph() throws Exception {
        test("new Intl.NumberFormat('en-PH').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_en_sg() throws Exception {
        test("new Intl.NumberFormat('en-SG').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_en_us() throws Exception {
        test("new Intl.NumberFormat('en-US').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_en_za() throws Exception {
        test("new Intl.NumberFormat('en-ZA').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_es() throws Exception {
        test("new Intl.NumberFormat('es').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_es_ar() throws Exception {
        test("new Intl.NumberFormat('es-AR').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_es_bo() throws Exception {
        test("new Intl.NumberFormat('es-BO').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_es_cl() throws Exception {
        test("new Intl.NumberFormat('es-CL').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_es_co() throws Exception {
        test("new Intl.NumberFormat('es-CO').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_es_cr() throws Exception {
        test("new Intl.NumberFormat('es-CR').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_es_do() throws Exception {
        test("new Intl.NumberFormat('es-DO').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_es_ec() throws Exception {
        test("new Intl.NumberFormat('es-EC').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_es_es() throws Exception {
        test("new Intl.NumberFormat('es-ES').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_es_gt() throws Exception {
        test("new Intl.NumberFormat('es-GT').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_es_hn() throws Exception {
        test("new Intl.NumberFormat('es-HN').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_es_mx() throws Exception {
        test("new Intl.NumberFormat('es-MX').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_es_ni() throws Exception {
        test("new Intl.NumberFormat('es-NI').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_es_pa() throws Exception {
        test("new Intl.NumberFormat('es-PA').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_es_pe() throws Exception {
        test("new Intl.NumberFormat('es-PE').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_es_pr() throws Exception {
        test("new Intl.NumberFormat('es-PR').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_es_py() throws Exception {
        test("new Intl.NumberFormat('es-PY').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_es_sv() throws Exception {
        test("new Intl.NumberFormat('es-SV').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_es_us() throws Exception {
        test("new Intl.NumberFormat('es-US').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_es_uy() throws Exception {
        test("new Intl.NumberFormat('es-UY').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_es_ve() throws Exception {
        test("new Intl.NumberFormat('es-VE').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_et() throws Exception {
        test("new Intl.NumberFormat('et').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_et_ee() throws Exception {
        test("new Intl.NumberFormat('et-EE').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_fi() throws Exception {
        test("new Intl.NumberFormat('fi').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_fi_fi() throws Exception {
        test("new Intl.NumberFormat('fi-FI').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31\u202f415,927",
            IE = "31\u00a0415,927")
    public void format_fr() throws Exception {
        test("new Intl.NumberFormat('fr').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31\u202f415,927",
            IE = "31.415,927")
    public void format_fr_be() throws Exception {
        test("new Intl.NumberFormat('fr-BE').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_fr_ca() throws Exception {
        test("new Intl.NumberFormat('fr-CA').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31\u202f415,927",
            IE = "31\u00a0415,927")
    public void format_fr_ch() throws Exception {
        test("new Intl.NumberFormat('fr-CH').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31\u202f415,927",
            IE = "31\u00a0415,927")
    public void format_fr_fr() throws Exception {
        test("new Intl.NumberFormat('fr-FR').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_fr_lu() throws Exception {
        test("new Intl.NumberFormat('fr-LU').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_ga() throws Exception {
        test("new Intl.NumberFormat('ga').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_ga_ie() throws Exception {
        test("new Intl.NumberFormat('ga-IE').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_hi_in() throws Exception {
        test("new Intl.NumberFormat('hi-IN').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_hr() throws Exception {
        test("new Intl.NumberFormat('hr').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_hr_hr() throws Exception {
        test("new Intl.NumberFormat('hr-HR').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_hu() throws Exception {
        test("new Intl.NumberFormat('hu').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_hu_hu() throws Exception {
        test("new Intl.NumberFormat('hu-HU').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_in() throws Exception {
        test("new Intl.NumberFormat('in').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_in_id() throws Exception {
        test("new Intl.NumberFormat('in-ID').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31,415.927",
            EDGE = "31.415,927",
            FF = "31.415,927",
            FF_ESR = "31.415,927",
            IE = "31.415,927")
    public void format_is() throws Exception {
        test("new Intl.NumberFormat('is').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31,415.927",
            EDGE = "31.415,927",
            FF = "31.415,927",
            FF_ESR = "31.415,927",
            IE = "31.415,927")
    public void format_is_is() throws Exception {
        test("new Intl.NumberFormat('is-IS').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_it() throws Exception {
        test("new Intl.NumberFormat('it').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u2019415.927")
    public void format_it_ch() throws Exception {
        test("new Intl.NumberFormat('it-CH').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_it_it() throws Exception {
        test("new Intl.NumberFormat('it-IT').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_iw() throws Exception {
        test("new Intl.NumberFormat('iw').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_iw_il() throws Exception {
        test("new Intl.NumberFormat('iw-IL').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_ja() throws Exception {
        test("new Intl.NumberFormat('ja').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_ja_jp() throws Exception {
        test("new Intl.NumberFormat('ja-JP').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_ko() throws Exception {
        test("new Intl.NumberFormat('ko').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_ko_kr() throws Exception {
        test("new Intl.NumberFormat('ko-KR').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_lt() throws Exception {
        test("new Intl.NumberFormat('lt').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_lt_lt() throws Exception {
        test("new Intl.NumberFormat('lt-LT').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_lv() throws Exception {
        test("new Intl.NumberFormat('lv').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_lv_lv() throws Exception {
        test("new Intl.NumberFormat('lv-LV').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31,415.927",
            EDGE = "31.415,927",
            FF = "31.415,927",
            FF_ESR = "31.415,927",
            IE = "31.415,927")
    public void format_mk() throws Exception {
        test("new Intl.NumberFormat('mk').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31,415.927",
            EDGE = "31.415,927",
            FF = "31.415,927",
            FF_ESR = "31.415,927",
            IE = "31.415,927")
    public void format_mk_mk() throws Exception {
        test("new Intl.NumberFormat('mk-MK').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_ms() throws Exception {
        test("new Intl.NumberFormat('ms').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_ms_my() throws Exception {
        test("new Intl.NumberFormat('ms-MY').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_mt() throws Exception {
        test("new Intl.NumberFormat('mt').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_mt_mt() throws Exception {
        test("new Intl.NumberFormat('mt-MT').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_nl() throws Exception {
        test("new Intl.NumberFormat('nl').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_nl_be() throws Exception {
        test("new Intl.NumberFormat('nl-BE').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_nl_nl() throws Exception {
        test("new Intl.NumberFormat('nl-NL').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_no() throws Exception {
        test("new Intl.NumberFormat('no').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_no_no() throws Exception {
        test("new Intl.NumberFormat('no-NO').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void format_no_no_ny() throws Exception {
        test("new Intl.NumberFormat('no-NO-NY').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_pl() throws Exception {
        test("new Intl.NumberFormat('pl').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_pl_pl() throws Exception {
        test("new Intl.NumberFormat('pl-PL').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_pt() throws Exception {
        test("new Intl.NumberFormat('pt').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_pt_br() throws Exception {
        test("new Intl.NumberFormat('pt-BR').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_pt_pt() throws Exception {
        test("new Intl.NumberFormat('pt-PT').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_ro() throws Exception {
        test("new Intl.NumberFormat('ro').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_ro_ro() throws Exception {
        test("new Intl.NumberFormat('ro-RO').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_ru() throws Exception {
        test("new Intl.NumberFormat('ru').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_ru_ru() throws Exception {
        test("new Intl.NumberFormat('ru-RU').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_sk() throws Exception {
        test("new Intl.NumberFormat('sk').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_sk_sk() throws Exception {
        test("new Intl.NumberFormat('sk-SK').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_sl() throws Exception {
        test("new Intl.NumberFormat('sl').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_sl_si() throws Exception {
        test("new Intl.NumberFormat('sl-SI').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31,415.927",
            EDGE = "31\u00a0415,927",
            FF = "31\u00a0415,927",
            FF_ESR = "31\u00a0415,927",
            IE = "31\u00a0415,927")
    public void format_sq() throws Exception {
        test("new Intl.NumberFormat('sq').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "31,415.927",
            EDGE = "31\u00a0415,927",
            FF = "31\u00a0415,927",
            FF_ESR = "31\u00a0415,927",
            IE = "31\u00a0415,927")
    public void format_sq_al() throws Exception {
        test("new Intl.NumberFormat('sq-AL').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_sr() throws Exception {
        test("new Intl.NumberFormat('sr').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_sr_ba() throws Exception {
        test("new Intl.NumberFormat('sr-BA').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_sr_cs() throws Exception {
        test("new Intl.NumberFormat('sr-CS').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_sr_me() throws Exception {
        test("new Intl.NumberFormat('sr-ME').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_sr_rs() throws Exception {
        test("new Intl.NumberFormat('sr-RS').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_sv() throws Exception {
        test("new Intl.NumberFormat('sv').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_sv_se() throws Exception {
        test("new Intl.NumberFormat('sv-SE').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_th() throws Exception {
        test("new Intl.NumberFormat('th').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_th_th() throws Exception {
        test("new Intl.NumberFormat('th-TH').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_tr() throws Exception {
        test("new Intl.NumberFormat('tr').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_tr_tr() throws Exception {
        test("new Intl.NumberFormat('tr-TR').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_uk() throws Exception {
        test("new Intl.NumberFormat('uk').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31\u00a0415,927")
    public void format_uk_ua() throws Exception {
        test("new Intl.NumberFormat('uk-UA').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_vi() throws Exception {
        test("new Intl.NumberFormat('vi').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31.415,927")
    public void format_vi_vn() throws Exception {
        test("new Intl.NumberFormat('vi-VN').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_zh() throws Exception {
        test("new Intl.NumberFormat('zh').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_zh_cn() throws Exception {
        test("new Intl.NumberFormat('zh-CN').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_zh_hk() throws Exception {
        test("new Intl.NumberFormat('zh-HK').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_zh_sg() throws Exception {
        test("new Intl.NumberFormat('zh-SG').format(number)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("31,415.927")
    public void format_zh_tw() throws Exception {
        test("new Intl.NumberFormat('zh-TW').format(number)");
    }
}
