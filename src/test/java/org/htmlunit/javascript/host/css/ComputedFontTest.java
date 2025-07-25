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
package org.htmlunit.javascript.host.css;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ComputedFont}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ComputedFontTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "16px", "2em", "32px", "150%", "24px"})
    public void fontSizeEm() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('mydiv');\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    log(div.style.fontSize);\n"
            + "    log(style.fontSize);\n"
            + "    div.style.fontSize = '2em';\n"
            + "    log(div.style.fontSize);\n"
            + "    log(style.fontSize);\n"
            + "    div.style.fontSize = '150%';\n"
            + "    log(div.style.fontSize);\n"
            + "    log(style.fontSize);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "", "", "", "", "", "", "", "", "", "", "", "", "",
                       "", "16px \"Times New Roman\"",
                       "", "normal", "", "normal", "", "400", "", "16px", "", "normal", "", "\"Times New Roman\""},
            FF = {"", "", "", "", "", "", "", "", "", "", "", "", "", "",
                  "", "16px serif", "", "normal", "", "normal", "", "400",
                  "", "16px", "", "normal", "", "serif"},
            FF_ESR = {"", "", "", "", "", "", "", "", "", "", "", "", "", "",
                      "", "16px serif", "", "normal", "", "normal", "", "400",
                      "", "16px", "", "normal", "", "serif"})
    public void fontInitial() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    debug(div);\n"
            + "    document.body.appendChild(div);\n"
            + "    debug(div);\n"
            + "  }\n"
            + "  function debug(div) {\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    log(div.style.font);\n"
            + "    log(style.font);\n"
            + "    log(div.style.fontStyle);\n"
            + "    log(style.fontStyle);\n"
            + "    log(div.style.fontVariant);\n"
            + "    log(style.fontVariant);\n"
            + "    log(div.style.fontWeight);\n"
            + "    log(style.fontWeight);\n"
            + "    log(div.style.fontSize);\n"
            + "    log(style.fontSize);\n"
            + "    log(div.style.lineHeight);\n"
            + "    log(style.lineHeight);\n"
            + "    log(div.style.fontFamily);\n"
            + "    log(style.fontFamily);\n"
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
    @Alerts(DEFAULT = {"15px arial, sans-serif", "15px arial, sans-serif",
                       "normal", "normal",
                       "oblique 15px arial, sans-serif", "italic 15px arial, sans-serif",
                       "oblique", "italic"},
            FF = {"15px arial, sans-serif", "15px arial, sans-serif", "normal", "normal",
                  "oblique 15px arial, sans-serif", "oblique 15px arial, sans-serif", "oblique", "oblique"},
            FF_ESR = {"15px arial, sans-serif", "15px arial, sans-serif", "normal", "normal",
                      "oblique 15px arial, sans-serif", "oblique 15px arial, sans-serif", "oblique", "oblique"})
    @HtmlUnitNYI(CHROME = {"", "16px \"Times New Roman\"", "", "normal", "",
                           "16px \"Times New Roman\"", "oblique", "oblique"},
            EDGE = {"", "16px \"Times New Roman\"", "", "normal", "",
                    "16px \"Times New Roman\"", "oblique", "oblique"},
            FF = {"", "16px serif", "", "normal", "", "16px serif", "oblique", "oblique"},
            FF_ESR = {"", "16px serif", "", "normal", "", "16px serif", "oblique", "oblique"})
    public void fontStyle() throws Exception {
        font("15px arial, sans-serif", "fontStyle", "oblique");
    }

    private void font(final String fontToSet, final String property, final String value) throws Exception {
        String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('mydiv');\n"
            + "    div.style.font = '" + fontToSet + "';\n"
            + "    debug(div);\n";

        if (value != null) {
            html += "    div.style." + property + " = '" + value + "';\n"
                    + "    debug(div);\n";
        }

        html += "  }\n"
            + "  function debug(div) {\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    log(div.style.font);\n"
            + "    log(style.font);\n"
            + "    log(div.style." + property + ");\n"
            + "    log(style." + property + ");\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "16px \"Times New Roman\"", "", "\"Times New Roman\""},
            FF = {"", "16px serif", "", "serif"},
            FF_ESR = {"", "16px serif", "", "serif"})
    public void wrongFontFamily() throws Exception {
        font("xyz", "fontFamily", null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1px xyz", "1px xyz", "xyz", "xyz", "1px abc", "1px abc", "abc", "abc"})
    public void minimalFontFamily() throws Exception {
        font("1px xyz", "fontFamily", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "16px \"Times New Roman\"",
                       "", "\"Times New Roman\"", "", "16px abc", "abc", "abc"},
            FF = {"", "16px serif", "", "serif", "", "16px abc", "abc", "abc"},
            FF_ESR = {"", "16px serif", "", "serif", "", "16px abc", "abc", "abc"})
    @HtmlUnitNYI(CHROME = {"", "16px \"Times New Roman\"",
                           "", "\"Times New Roman\"", "", "16px \"Times New Roman\"", "abc", "abc"},
            EDGE = {"", "16px \"Times New Roman\"",
                    "", "\"Times New Roman\"", "", "16px \"Times New Roman\"", "abc", "abc"},
            FF = {"", "16px serif", "", "serif", "", "16px serif", "abc", "abc"},
            FF_ESR = {"", "16px serif", "", "serif", "", "16px serif", "abc", "abc"})
    public void minimalFontFamilyReversed() throws Exception {
        font("xyz 1px", "fontFamily", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1px / 2px xyz", "1px / 2px xyz",
             "2px", "2px", "1px xyz", "1px xyz", "normal", "normal"})
    public void minimalLineHeight() throws Exception {
        font("1px/2px xyz", "lineHeight", "normal");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1px / 2px xyz", "1px / 2px xyz",
             "2px", "2px", "1px xyz", "1px xyz", "normal", "normal"})
    @HtmlUnitNYI(CHROME = {"2px xyz", "2px xyz", "", "normal", "2px xyz", "2px xyz", "normal", "normal"},
            EDGE = {"2px xyz", "2px xyz", "", "normal", "2px xyz", "2px xyz", "normal", "normal"},
            FF = {"2px xyz", "2px xyz", "", "normal", "2px xyz", "2px xyz", "normal", "normal"},
            FF_ESR = {"2px xyz", "2px xyz", "", "normal", "2px xyz", "2px xyz", "normal", "normal"})
    public void minimalLineHeightSpace() throws Exception {
        font("1px / 2px xyz", "lineHeight", "normal");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1px / 2px xyz", "1px / 2px xyz",
             "2px", "2px", "1px xyz", "1px xyz", "normal", "normal"})
    @HtmlUnitNYI(CHROME = {"2px xyz", "2px xyz",
                           "", "normal", "2px xyz", "2px xyz", "normal", "normal"},
            EDGE = {"2px xyz", "2px xyz",
                    "", "normal", "2px xyz", "2px xyz", "normal", "normal"},
            FF = {"2px xyz", "2px xyz", "", "normal", "2px xyz", "2px xyz", "normal", "normal"},
            FF_ESR = {"2px xyz", "2px xyz", "", "normal", "2px xyz", "2px xyz", "normal", "normal"})
    public void minimalLineHeightSpace2() throws Exception {
        font("1px/ 2px xyz", "lineHeight", "normal");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1px / 2px xyz", "1px / 2px xyz",
             "2px", "2px", "1px xyz", "1px xyz", "normal", "normal"})
    @HtmlUnitNYI(CHROME = {"", "16px \"Times New Roman\"", "", "normal",
                           "", "16px \"Times New Roman\"", "normal", "normal"},
            EDGE = {"", "16px \"Times New Roman\"", "", "normal",
                    "", "16px \"Times New Roman\"", "normal", "normal"},
            FF = {"", "16px serif", "", "normal", "", "16px serif", "normal", "normal"},
            FF_ESR = {"", "16px serif", "", "normal", "", "16px serif", "normal", "normal"})
    public void minimalLineHeightSpace3() throws Exception {
        font("1px /2px xyz", "lineHeight", "normal");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1px / 2px xyz", "1px / 2px xyz",
             "2px", "2px", "1px xyz", "1px xyz", "normal", "normal"})
    @HtmlUnitNYI(CHROME = {"", "16px \"Times New Roman\"", "", "normal",
                           "", "16px \"Times New Roman\"", "normal", "normal"},
            EDGE = {"", "16px \"Times New Roman\"", "", "normal",
                    "", "16px \"Times New Roman\"", "normal", "normal"},
            FF = {"", "16px serif", "", "normal", "", "16px serif", "normal", "normal"},
            FF_ESR = {"", "16px serif", "", "normal", "", "16px serif", "normal", "normal"})
    public void minimalLineHeightSpace4() throws Exception {
        font("1px  /2px xyz", "lineHeight", "normal");
    }
}
