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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "42.247"})
    public void numberFormat() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
}
