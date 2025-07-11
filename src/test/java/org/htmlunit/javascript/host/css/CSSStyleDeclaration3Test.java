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
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CSSStyleDeclaration} background, font and border shorthand.
 *
 * @author Ronald Brill
 * @author Frank Danek
 */
public class CSSStyleDeclaration3Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", ""})
    public void backgroundEmpty() throws Exception {
        background("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"red", "none", "repeat", "0% 0%", "scroll"},
            CHROME = {"red", "initial", "initial", "initial", "initial"},
            EDGE = {"red", "initial", "initial", "initial", "initial"})
    public void backgroundColorRed() throws Exception {
        background("rEd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll"},
            CHROME = {"rgb(255, 204, 221)", "initial", "initial", "initial", "initial"},
            EDGE = {"rgb(255, 204, 221)", "initial", "initial", "initial", "initial"})
    public void backgroundColorHex() throws Exception {
        background("#fFccdd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll"},
            CHROME = {"rgb(255, 204, 221)", "initial", "initial", "initial", "initial"},
            EDGE = {"rgb(255, 204, 221)", "initial", "initial", "initial", "initial"})
    public void backgroundColorHexShort() throws Exception {
        background("#fCd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgb(20, 40, 60)", "none", "repeat", "0% 0%", "scroll"},
            CHROME = {"rgb(20, 40, 60)", "initial", "initial", "initial", "initial"},
            EDGE = {"rgb(20, 40, 60)", "initial", "initial", "initial", "initial"})
    public void backgroundColorRgb() throws Exception {
        background("rGb(20, 40, 60)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "url(\"myImage.png\")", "initial", "initial", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll"})
    public void backgroundImage() throws Exception {
        background("uRl(myImage.png)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "repeat-x", "initial", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat-x", "0% 0%", "scroll"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "none", "repeat-x", "0% 0%", "scroll"})
    public void backgroundRepeat() throws Exception {
        background("repeat-x");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "20px 100%", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "20px 100%", "scroll"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "none", "repeat", "20px 100%", "scroll"})
    public void backgroundPosition() throws Exception {
        background("20px 100%");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "right bottom", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "right bottom", "scroll"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "none", "repeat", "right bottom", "scroll"})
    public void backgroundPosition2() throws Exception {
        background("bottom right");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "10em bottom", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "10em bottom", "scroll"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "none", "repeat", "10em bottom", "scroll"})
    public void backgroundPosition3() throws Exception {
        background("10em bottom");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "10em center", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "10em center", "scroll"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "none", "repeat", "10em center", "scroll"})
    public void backgroundPosition4() throws Exception {
        background("10em center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "fixed"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "0% 0%", "fixed"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "none", "repeat", "0% 0%", "fixed"})
    public void backgroundAttachment() throws Exception {
        background("fixed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"red", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll"},
            CHROME = {"red", "url(\"myImage.png\")", "initial", "initial", "initial"},
            EDGE = {"red", "url(\"myImage.png\")", "initial", "initial", "initial"})
    public void backgroundMixed() throws Exception {
        background("red url(\"myImage.png\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgb(255, 255, 255)", "none", "no-repeat", "20px 100px", "scroll"},
            CHROME = {"rgb(255, 255, 255)", "initial", "no-repeat", "20px 100px", "initial"},
            EDGE = {"rgb(255, 255, 255)", "initial", "no-repeat", "20px 100px", "initial"})
    public void backgroundMixed2() throws Exception {
        background("#fff no-repeat 20px 100px");
    }

    private void background(final String backgroundStyle) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  <div id='tester' style='background: " + backgroundStyle + "' >hello</div>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var myStyle = document.getElementById('tester').style;\n"
            + "    log(myStyle.backgroundColor);\n"
            + "    log(myStyle.backgroundImage);\n"
            + "    log(myStyle.backgroundRepeat);\n"
            + "    log(myStyle.backgroundPosition);\n"
            + "    log(myStyle.backgroundAttachment);\n"
            + "  </script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", ""})
    public void backgroundCssEmpty() throws Exception {
        backgroundCss("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"red", "initial", "initial", "initial", "initial"},
            FF = {"red", "none", "repeat", "0% 0%", "scroll"},
            FF_ESR = {"red", "none", "repeat", "0% 0%", "scroll"})
    @HtmlUnitNYI(CHROME = {"rEd", "initial", "initial", "initial", "initial"},
            EDGE = {"rEd", "initial", "initial", "initial", "initial"},
            FF = {"rEd", "none", "repeat", "0% 0%", "scroll"},
            FF_ESR = {"rEd", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundCssColorRed() throws Exception {
        backgroundCss("rEd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgb(255, 204, 221)", "initial", "initial", "initial", "initial"},
            FF = {"rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll"},
            FF_ESR = {"rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundCssColorHex() throws Exception {
        backgroundCss("#fFccdd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgb(255, 204, 221)", "initial", "initial", "initial", "initial"},
            FF = {"rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll"},
            FF_ESR = {"rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundCssColorHexShort() throws Exception {
        backgroundCss("#fCd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgb(20, 40, 60)", "initial", "initial", "initial", "initial"},
            FF = {"rgb(20, 40, 60)", "none", "repeat", "0% 0%", "scroll"},
            FF_ESR = {"rgb(20, 40, 60)", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundCssColorRgb() throws Exception {
        backgroundCss("rGb(20, 40, 60)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "url(\"myImage.png\")", "initial", "initial", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll"})
    public void backgroundCssImage() throws Exception {
        backgroundCss("uRl(myImage.png)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "repeat-x", "initial", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat-x", "0% 0%", "scroll"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "none", "repeat-x", "0% 0%", "scroll"})
    public void backgroundCssRepeat() throws Exception {
        backgroundCss("repeat-x");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "20px 100%", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "20px 100%", "scroll"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "none", "repeat", "20px 100%", "scroll"})
    public void backgroundCssPosition() throws Exception {
        backgroundCss("20px 100%");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "right bottom", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "right bottom", "scroll"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "none", "repeat", "right bottom", "scroll"})
    public void backgroundCssPosition2() throws Exception {
        backgroundCss("bottom right");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "left bottom", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "left bottom", "scroll"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "none", "repeat", "left bottom", "scroll"})
    public void backgroundCssPosition3() throws Exception {
        backgroundCss("left bottom");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "center top", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "center top", "scroll"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "none", "repeat", "center top", "scroll"})
    public void backgroundCssPosition4() throws Exception {
        backgroundCss("top center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "fixed"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "0% 0%", "fixed"},
            FF_ESR = {"rgba(0, 0, 0, 0)", "none", "repeat", "0% 0%", "fixed"})
    public void backgroundCssAttachment() throws Exception {
        backgroundCss("fixed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"red", "url(\"myImage.png\")", "initial", "initial", "initial"},
            FF = {"red", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll"},
            FF_ESR = {"red", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll"})
    public void backgroundCssMixed() throws Exception {
        backgroundCss("red url(\"myImage.png\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgb(255, 255, 255)", "initial", "no-repeat", "20px 100px", "initial"},
            FF = {"rgb(255, 255, 255)", "none", "no-repeat", "20px 100px", "scroll"},
            FF_ESR = {"rgb(255, 255, 255)", "none", "no-repeat", "20px 100px", "scroll"})
    public void backgroundCssMixed2() throws Exception {
        backgroundCss("#fff no-repeat 20px 100px");
    }

    private void backgroundCss(final String backgroundStyle) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "</head>\n"
            + "  <style type='text/css'>div { background: " + backgroundStyle + " }</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div id='tester'>hello</div>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var myStyle = document.styleSheets[0].cssRules[0].style;\n"
            + "    log(myStyle.backgroundColor);\n"
            + "    log(myStyle.backgroundImage);\n"
            + "    log(myStyle.backgroundRepeat);\n"
            + "    log(myStyle.backgroundPosition);\n"
            + "    log(myStyle.backgroundAttachment);\n"
            + "  </script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgba(0, 0, 0, 0)", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundComputedEmpty() throws Exception {
        backgroundComputed("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(255, 0, 0)", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundComputedColorRed() throws Exception {
        backgroundComputed("rEd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundComputedColorHex() throws Exception {
        backgroundComputed("#fFccdd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundComputedColorHexShort() throws Exception {
        backgroundComputed("#fCd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(20, 40, 60)", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundComputedColorRgb() throws Exception {
        backgroundComputed("rGb(20, 40, 60)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgba(0, 0, 0, 0)", "url(\"§§URL§§myImage.png\")", "repeat", "0% 0%", "scroll"})
    public void backgroundComputedImage() throws Exception {
        backgroundComputed("uRl(myImage.png)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgba(0, 0, 0, 0)", "none", "repeat-x", "0% 0%", "scroll"})
    public void backgroundComputedRepeat() throws Exception {
        backgroundComputed("repeat-x");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgba(0, 0, 0, 0)", "none", "repeat", "20px 100%", "scroll"})
    public void backgroundComputedPosition() throws Exception {
        backgroundComputed("20px 100%");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgba(0, 0, 0, 0)", "none", "repeat", "100% 100%", "scroll"})
    public void backgroundComputedPosition2() throws Exception {
        backgroundComputed("bottom right");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgba(0, 0, 0, 0)", "none", "repeat", "0% 100%", "scroll"})
    public void backgroundComputedPosition3() throws Exception {
        backgroundComputed("left bottom");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgba(0, 0, 0, 0)", "none", "repeat", "50% 0%", "scroll"})
    public void backgroundComputedPosition4() throws Exception {
        backgroundComputed("top center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgba(0, 0, 0, 0)", "none", "repeat", "0% 0%", "fixed"})
    public void backgroundComputedAttachment() throws Exception {
        backgroundComputed("fixed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(255, 0, 0)", "url(\"§§URL§§myImage.png\")", "repeat", "0% 0%", "scroll"})
    public void backgroundComputedMixed() throws Exception {
        backgroundComputed("red url(\"myImage.png\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(255, 255, 255)", "none", "no-repeat", "20px 100px", "scroll"})
    public void backgroundComputedMixed2() throws Exception {
        backgroundComputed("#fff no-repeat 20px 100px");
    }

    private void backgroundComputed(final String backgroundStyle) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "</head>\n"
            + "  <style type='text/css'>div { background: " + backgroundStyle + " }</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div id='tester'>hello</div>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var myDiv = document.getElementById('tester');\n"
            + "    var myStyle = window.getComputedStyle(myDiv, null);\n"
            + "    log(myStyle.backgroundColor);\n"
            + "    log(myStyle.backgroundImage);\n"
            + "    log(myStyle.backgroundRepeat);\n"
            + "    log(myStyle.backgroundPosition);\n"
            + "    log(myStyle.backgroundAttachment);\n"
            + "  </script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", "", ""})
    public void fontEmpty() throws Exception {
        font("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", "", ""})
    public void fontSizeOnly() throws Exception {
        font("14px");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", "", ""})
    public void fontFamilyOnly() throws Exception {
        font("sans-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", "", ""})
    public void fontAllExceptSizeAndFamily() throws Exception {
        font("italic small-caps bold");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontSizeAndFamily() throws Exception {
        font("14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"normal", "normal", "normal", "14px", "normal", "\"Gill Sans Extrabold\""},
            FF = {"normal", "normal", "normal", "14px", "normal", "Gill Sans Extrabold"},
            FF_ESR = {"normal", "normal", "normal", "14px", "normal", "Gill Sans Extrabold"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontFamilyWithSpaces() throws Exception {
        font("14pX Gill Sans Extrabold");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "\"Gill Sans Extrabold\""})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontFamilyQuoted() throws Exception {
        font("14pX \"Gill Sans Extrabold\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "\"Gill Sans Extrabold\", serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontFamilyMultiple() throws Exception {
        font("14pX \"Gill Sans Extrabold\", serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14%", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontSizePercent() throws Exception {
        font("14.0% sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"italic", "normal", "normal", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontStyle() throws Exception {
        font("iTalic 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"oblique 10deg", "normal", "normal", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontStyleWithDegree() throws Exception {
        font("oBlique 10deg 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "small-caps", "normal", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontVariant() throws Exception {
        font("sMall-caps 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "bold", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontWeight() throws Exception {
        font("bOld 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "800", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontWeightNumber() throws Exception {
        font("800 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "18em", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontLineHeight() throws Exception {
        font("14pX/18eM sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "18%", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontLineHeightPercent() throws Exception {
        font("14pX/18.0% sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"italic", "small-caps", "bold", "14px", "18em", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontAll() throws Exception {
        font("iTalic sMall-caps bOld 14pX/18eM sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "sans-serif", "none", "normal"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "0.5", ""},
            EDGE = {"", "", "", "", "", "", "0.5", ""},
            FF = {"", "", "", "", "", "", "0.5", ""},
            FF_ESR = {"", "", "", "", "", "", "0.5", ""})
    public void fontSizeAdjustBefore() throws Exception {
        font("14pX sAns-serif", "font-size-adjust: 0.5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "sans-serif", "0.5", "normal"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "0.5", ""},
            EDGE = {"", "", "", "", "", "", "0.5", ""},
            FF = {"", "", "", "", "", "", "0.5", ""},
            FF_ESR = {"", "", "", "", "", "", "0.5", ""})
    public void fontSizeAdjustAfter() throws Exception {
        font("14pX sAns-serif; font-size-adjust: 0.5", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "sans-serif", "none", "normal"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "", "expanded"},
            EDGE = {"", "", "", "", "", "", "", "expanded"},
            FF = {"", "", "", "", "", "", "", "expanded"},
            FF_ESR = {"", "", "", "", "", "", "", "expanded"})
    public void fontStretchBefore() throws Exception {
        font("14pX sAns-serif", "font-stretch: expanded");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "sans-serif", "none", "expanded"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "", "expanded"},
            EDGE = {"", "", "", "", "", "", "", "expanded"},
            FF = {"", "", "", "", "", "", "", "expanded"},
            FF_ESR = {"", "", "", "", "", "", "", "expanded"})
    public void fontStretchAfter() throws Exception {
        font("14pX sAns-serif; font-stretch: expanded", "");
    }

    private void font(final String fontStyle) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "  <div id='tester' style='font: " + fontStyle + "' >hello</div>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    var myStyle = document.getElementById('tester').style;\n"
                + "    log(myStyle.fontStyle);\n"
                + "    log(myStyle.fontVariant);\n"
                + "    log(myStyle.fontWeight);\n"
                + "    log(myStyle.fontSize);\n"
                + "    log(myStyle.lineHeight);\n"
                + "    log(myStyle.fontFamily);\n"
                + "  </script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    private void font(final String fontStyle, final String otherStyle) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  <div id='tester' style='" + otherStyle + "; font: " + fontStyle + "' >hello</div>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var myStyle = document.getElementById('tester').style;\n"
            + "    log(myStyle.fontStyle);\n"
            + "    log(myStyle.fontVariant);\n"
            + "    log(myStyle.fontWeight);\n"
            + "    log(myStyle.fontSize);\n"
            + "    log(myStyle.lineHeight);\n"
            + "    log(myStyle.fontFamily);\n"
            + "    log(myStyle.fontSizeAdjust);\n"
            + "    log(myStyle.fontStretch);\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", "", ""})
    public void fontCssEmpty() throws Exception {
        fontCss("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", "", ""})
    public void fontCssSizeOnly() throws Exception {
        fontCss("14px");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", "", ""})
    public void fontCssFamilyOnly() throws Exception {
        fontCss("sans-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", "", ""})
    public void fontCssAllExceptSizeAndFamily() throws Exception {
        fontCss("italic small-caps bold");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssSizeAndFamily() throws Exception {
        fontCss("14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"normal", "normal", "normal", "14px", "normal", "\"Gill Sans Extrabold\""},
            FF = {"normal", "normal", "normal", "14px", "normal", "Gill Sans Extrabold"},
            FF_ESR = {"normal", "normal", "normal", "14px", "normal", "Gill Sans Extrabold"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssFamilyWithSpaces() throws Exception {
        fontCss("14pX Gill Sans Extrabold");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "\"Gill Sans Extrabold\""})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssFamilyQuoted() throws Exception {
        fontCss("14pX \"Gill Sans Extrabold\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "\"Gill Sans Extrabold\", serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssFamilyMultiple() throws Exception {
        fontCss("14pX \"Gill Sans Extrabold\", serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14%", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssSizePercent() throws Exception {
        fontCss("14.0% sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"italic", "normal", "normal", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssStyle() throws Exception {
        fontCss("iTalic 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"oblique 10deg", "normal", "normal", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssStyleWithDegree() throws Exception {
        fontCss("oBlique 10deg 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "small-caps", "normal", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssVariant() throws Exception {
        fontCss("sMall-caps 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "bold", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssWeight() throws Exception {
        fontCss("bOld 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "800", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssWeightNumber() throws Exception {
        fontCss("800 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "18em", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssLineHeight() throws Exception {
        fontCss("14pX/18eM sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "18%", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssLineHeightPercent() throws Exception {
        fontCss("14pX/18.0% sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"italic", "small-caps", "bold", "14px", "18em", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", ""},
            EDGE = {"", "", "", "", "", ""},
            FF = {"", "", "", "", "", ""},
            FF_ESR = {"", "", "", "", "", ""})
    public void fontCssAll() throws Exception {
        fontCss("iTalic sMall-caps bOld 14pX/18eM sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "sans-serif", "none", "normal"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "0.5", ""},
            EDGE = {"", "", "", "", "", "", "0.5", ""},
            FF = {"", "", "", "", "", "", "0.5", ""},
            FF_ESR = {"", "", "", "", "", "", "0.5", ""})
    public void fontCssSizeAdjustBefore() throws Exception {
        fontCss("14pX sAns-serif", "font-size-adjust: 0.5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "sans-serif", "0.5", "normal"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "0.5", ""},
            EDGE = {"", "", "", "", "", "", "0.5", ""},
            FF = {"", "", "", "", "", "", "0.5", ""},
            FF_ESR = {"", "", "", "", "", "", "0.5", ""})
    public void fontCssSizeAdjustAfter() throws Exception {
        fontCss("14pX sAns-serif; font-size-adjust: 0.5", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "sans-serif", "none", "normal"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "", "expanded"},
            EDGE = {"", "", "", "", "", "", "", "expanded"},
            FF = {"", "", "", "", "", "", "", "expanded"},
            FF_ESR = {"", "", "", "", "", "", "", "expanded"})
    public void fontCssStretchBefore() throws Exception {
        fontCss("14pX sAns-serif", "font-stretch: expanded");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "normal", "14px", "normal", "sans-serif", "none", "expanded"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "", "expanded"},
            EDGE = {"", "", "", "", "", "", "", "expanded"},
            FF = {"", "", "", "", "", "", "", "expanded"},
            FF_ESR = {"", "", "", "", "", "", "", "expanded"})
    public void fontCssStretchAfter() throws Exception {
        fontCss("14pX sAns-serif; font-stretch: expanded", "");
    }

    private void fontCss(final String fontStyle) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "</head>\n"
                + "  <style type='text/css'>div { font: " + fontStyle + " }</style>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <div id='tester'>hello</div>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    var myStyle = document.styleSheets[0].cssRules[0].style;\n"
                + "    log(myStyle.fontStyle);\n"
                + "    log(myStyle.fontVariant);\n"
                + "    log(myStyle.fontWeight);\n"
                + "    log(myStyle.fontSize);\n"
                + "    log(myStyle.lineHeight);\n"
                + "    log(myStyle.fontFamily);\n"
                + "  </script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    private void fontCss(final String fontStyle, final String otherStyle) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "</head>\n"
            + "  <style type='text/css'>div { " + otherStyle + "; font: " + fontStyle + " }</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div id='tester'>hello</div>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var myStyle = document.styleSheets[0].cssRules[0].style;\n"
            + "    log(myStyle.fontStyle);\n"
            + "    log(myStyle.fontVariant);\n"
            + "    log(myStyle.fontWeight);\n"
            + "    log(myStyle.fontSize);\n"
            + "    log(myStyle.lineHeight);\n"
            + "    log(myStyle.fontFamily);\n"
            + "    log(myStyle.fontSizeAdjust);\n"
            + "    log(myStyle.fontStretch);\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedEmpty() throws Exception {
        fontComputed("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedSizeOnly() throws Exception {
        fontComputed("14px");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedFamilyOnly() throws Exception {
        fontComputed("sans-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedAllExceptSizeAndFamily() throws Exception {
        fontComputed("italic small-caps bold");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "400", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedSizeAndFamily() throws Exception {
        fontComputed("14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"normal", "normal", "400", "14px", "normal", "\"Gill Sans Extrabold\""},
            FF = {"normal", "normal", "400", "14px", "normal", "Gill Sans Extrabold"},
            FF_ESR = {"normal", "normal", "400", "14px", "normal", "Gill Sans Extrabold"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedFamilyWithSpaces() throws Exception {
        fontComputed("14pX Gill Sans Extrabold");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "400", "14px", "normal", "\"Gill Sans Extrabold\""})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedFamilyQuoted() throws Exception {
        fontComputed("14pX \"Gill Sans Extrabold\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "400", "14px", "normal", "\"Gill Sans Extrabold\", serif"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedFamilyMultiple() throws Exception {
        fontComputed("14pX \"Gill Sans Extrabold\", serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"normal", "normal", "400", "6px", "normal", "sans-serif"},
            FF = {"normal", "normal", "400", "2.24px", "normal", "sans-serif"},
            FF_ESR = {"normal", "normal", "400", "2.24px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedSizePercent() throws Exception {
        fontComputed("14.0% sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"italic", "normal", "400", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedStyle() throws Exception {
        fontComputed("iTalic 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"oblique 10deg", "normal", "400", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedStyleWithDegree() throws Exception {
        fontComputed("oBlique 10deg 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "small-caps", "400", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedVariant() throws Exception {
        fontComputed("sMall-caps 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "700", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedWeight() throws Exception {
        fontComputed("bOld 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "800", "14px", "normal", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedWeightNumber() throws Exception {
        fontComputed("800 14pX sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "400", "14px", "252px", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedLineHeight() throws Exception {
        fontComputed("14pX/18eM sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"normal", "normal", "400", "14px", "2.52px", "sans-serif"},
            FF = {"normal", "normal", "400", "14px", "2.51667px", "sans-serif"},
            FF_ESR = {"normal", "normal", "400", "14px", "2.51667px", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedLineHeightPercent() throws Exception {
        fontComputed("14pX/18.0% sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"italic", "small-caps", "700", "14px", "252px", "sans-serif"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\""},
            FF = {"normal", "normal", "400", "16px", "normal", "serif"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif"})
    public void fontComputedAll() throws Exception {
        fontComputed("iTalic sMall-caps bOld 14pX/18eM sAns-serif");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "400", "14px", "normal", "sans-serif", "none", "100%"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\"", "0.5", "100%"},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\"", "0.5", "100%"},
            FF = {"normal", "normal", "400", "16px", "normal", "serif", "0.5", "100%"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif", "0.5", "100%"})
    public void fontComputedSizeAdjustBefore() throws Exception {
        fontComputed("14pX sAns-serif", "font-size-adjust: 0.5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "400", "14px", "normal", "sans-serif", "0.5", "100%"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\"", "0.5", "100%"},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\"", "0.5", "100%"},
            FF = {"normal", "normal", "400", "16px", "normal", "serif", "0.5", "100%"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif", "0.5", "100%"})
    public void fontComputedSizeAdjustAfter() throws Exception {
        fontComputed("14pX sAns-serif; font-size-adjust: 0.5", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "400", "14px", "normal", "sans-serif", "none", "100%"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\"", "none", "expanded"},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\"", "none", "expanded"},
            FF = {"normal", "normal", "400", "16px", "normal", "serif", "none", "expanded"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif", "none", "expanded"})
    public void fontComputedStretchBefore() throws Exception {
        fontComputed("14pX sAns-serif", "font-stretch: expanded");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"normal", "normal", "400", "14px", "normal", "sans-serif", "none", "125%"})
    @HtmlUnitNYI(CHROME = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\"", "none", "expanded"},
            EDGE = {"normal", "normal", "400", "16px", "normal", "\"Times New Roman\"", "none", "expanded"},
            FF = {"normal", "normal", "400", "16px", "normal", "serif", "none", "expanded"},
            FF_ESR = {"normal", "normal", "400", "16px", "normal", "serif", "none", "expanded"})
    public void fontComputedStretchAfter() throws Exception {
        fontComputed("14pX sAns-serif; font-stretch: expanded", "");
    }

    private void fontComputed(final String fontStyle) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "</head>\n"
                + "  <style type='text/css'>div { font: " + fontStyle + " }</style>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <div id='tester'>hello</div>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    var myDiv = document.getElementById('tester');\n"
                + "    var myStyle = window.getComputedStyle(myDiv, null);\n"
                + "    log(myStyle.fontStyle);\n"
                + "    log(myStyle.fontVariant);\n"
                + "    log(myStyle.fontWeight);\n"
                + "    log(myStyle.fontSize);\n"
                + "    log(myStyle.lineHeight);\n"
                + "    log(myStyle.fontFamily);\n"
                + "  </script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    private void fontComputed(final String fontStyle, final String otherStyle) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "</head>\n"
            + "  <style type='text/css'>div { " + otherStyle + "; font: " + fontStyle + " }</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div id='tester'>hello</div>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var myDiv = document.getElementById('tester');\n"
            + "    var myStyle = window.getComputedStyle(myDiv, null);\n"
            + "    log(myStyle.fontStyle);\n"
            + "    log(myStyle.fontVariant);\n"
            + "    log(myStyle.fontWeight);\n"
            + "    log(myStyle.fontSize);\n"
            + "    log(myStyle.lineHeight);\n"
            + "    log(myStyle.fontFamily);\n"
            + "    log(myStyle.fontSizeAdjust);\n"
            + "    log(myStyle.fontStretch);\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", "", "", "", "", "", "", "", ""})
    public void borderEmpty() throws Exception {
        border("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"thin", "thin", "thin", "thin", "initial", "initial", "initial", "initial",
                       "initial", "initial", "initial", "initial"},
            FF = {"thin", "thin", "thin", "thin", "none", "none", "none", "none",
                  "currentcolor", "currentcolor", "currentcolor", "currentcolor"},
            FF_ESR = {"thin", "thin", "thin", "thin", "none", "none", "none", "none",
                      "currentcolor", "currentcolor", "currentcolor", "currentcolor"})
    @HtmlUnitNYI(CHROME = {"thin", "thin", "thin", "thin", "", "", "", "", "", "", "", ""},
            EDGE = {"thin", "thin", "thin", "thin", "", "", "", "", "", "", "", ""},
            FF = {"thin", "thin", "thin", "thin", "", "", "", "", "", "", "", ""},
            FF_ESR = {"thin", "thin", "thin", "thin", "", "", "", "", "", "", "", ""})
    public void borderWidth() throws Exception {
        border("tHin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2px", "2px", "2px", "2px", "initial", "initial", "initial", "initial",
                       "initial", "initial", "initial", "initial"},
            FF = {"2px", "2px", "2px", "2px", "none", "none", "none", "none",
                  "currentcolor", "currentcolor", "currentcolor", "currentcolor"},
            FF_ESR = {"2px", "2px", "2px", "2px", "none", "none", "none", "none",
                      "currentcolor", "currentcolor", "currentcolor", "currentcolor"})
    @HtmlUnitNYI(CHROME = {"2px", "2px", "2px", "2px", "", "", "", "", "", "", "", ""},
            EDGE = {"2px", "2px", "2px", "2px", "", "", "", "", "", "", "", ""},
            FF = {"2px", "2px", "2px", "2px", "", "", "", "", "", "", "", ""},
            FF_ESR = {"2px", "2px", "2px", "2px", "", "", "", "", "", "", "", ""})
    public void borderWidthNumber() throws Exception {
        border("2pX");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "solid", "solid", "solid", "solid",
                       "initial", "initial", "initial", "initial"},
            FF = {"medium", "medium", "medium", "medium", "solid", "solid", "solid", "solid",
                  "currentcolor", "currentcolor", "currentcolor", "currentcolor"},
            FF_ESR = {"medium", "medium", "medium", "medium", "solid", "solid", "solid", "solid",
                      "currentcolor", "currentcolor", "currentcolor", "currentcolor"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "solid", "solid", "solid", "solid", "", "", "", ""},
            EDGE = {"", "", "", "", "solid", "solid", "solid", "solid", "", "", "", ""},
            FF = {"", "", "", "", "solid", "solid", "solid", "solid", "", "", "", ""},
            FF_ESR = {"", "", "", "", "solid", "solid", "solid", "solid", "", "", "", ""})
    public void borderStyle() throws Exception {
        border("sOlid");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"thin", "thin", "thin", "thin", "solid", "solid", "solid", "solid",
                       "initial", "initial", "initial", "initial"},
            FF = {"thin", "thin", "thin", "thin", "solid", "solid", "solid", "solid",
                  "currentcolor", "currentcolor", "currentcolor", "currentcolor"},
            FF_ESR = {"thin", "thin", "thin", "thin", "solid", "solid", "solid", "solid",
                      "currentcolor", "currentcolor", "currentcolor", "currentcolor"})
    @HtmlUnitNYI(CHROME = {"thin", "thin", "thin", "thin", "solid", "solid", "solid", "solid", "", "", "", ""},
            EDGE = {"thin", "thin", "thin", "thin", "solid", "solid", "solid", "solid", "", "", "", ""},
            FF = {"thin", "thin", "thin", "thin", "solid", "solid", "solid", "solid", "", "", "", ""},
            FF_ESR = {"thin", "thin", "thin", "thin", "solid", "solid", "solid", "solid", "", "", "", ""})
    public void borderWidthAndStyle() throws Exception {
        border("tHin sOlid");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2px", "2px", "2px", "2px", "solid", "solid", "solid", "solid",
                       "initial", "initial", "initial", "initial"},
            FF = {"2px", "2px", "2px", "2px", "solid", "solid", "solid", "solid",
                  "currentcolor", "currentcolor", "currentcolor", "currentcolor"},
            FF_ESR = {"2px", "2px", "2px", "2px", "solid", "solid", "solid", "solid",
                      "currentcolor", "currentcolor", "currentcolor", "currentcolor"})
    @HtmlUnitNYI(CHROME = {"2px", "2px", "2px", "2px", "solid", "solid", "solid", "solid", "", "", "", ""},
            EDGE = {"2px", "2px", "2px", "2px", "solid", "solid", "solid", "solid", "", "", "", ""},
            FF = {"2px", "2px", "2px", "2px", "solid", "solid", "solid", "solid", "", "", "", ""},
            FF_ESR = {"2px", "2px", "2px", "2px", "solid", "solid", "solid", "solid", "", "", "", ""})
    public void borderWidthNumberAndStyle() throws Exception {
        border("2pX sOlid");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "initial", "initial", "initial", "initial",
                       "red", "red", "red", "red"},
            FF = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                  "red", "red", "red", "red"},
            FF_ESR = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                      "red", "red", "red", "red"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "", "", "red", "red", "red", "red"},
            EDGE = {"", "", "", "", "", "", "", "", "red", "red", "red", "red"},
            FF = {"", "", "", "", "", "", "", "", "red", "red", "red", "red"},
            FF_ESR = {"", "", "", "", "", "", "", "", "red", "red", "red", "red"})
    public void borderColor() throws Exception {
        border("rEd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "initial", "initial", "initial", "initial",
                       "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                  "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF_ESR = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                      "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "", "",
                           "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            EDGE = {"", "", "", "", "", "", "", "",
                    "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF = {"", "", "", "", "", "", "", "",
                  "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF_ESR = {"", "", "", "", "", "", "", "",
                      "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    public void borderColorHex() throws Exception {
        border("#fFccdd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "initial", "initial", "initial", "initial",
                       "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                  "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF_ESR = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                      "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "", "",
                           "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            EDGE = {"", "", "", "", "", "", "", "",
                    "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF = {"", "", "", "", "", "", "", "",
                  "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF_ESR = {"", "", "", "", "", "", "", "",
                      "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    public void borderColorHexShort() throws Exception {
        border("#fCd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "initial", "initial", "initial", "initial",
                       "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"},
            FF = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                  "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"},
            FF_ESR = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                      "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "", "",
                           "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"},
            EDGE = {"", "", "", "", "", "", "", "",
                    "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"},
            FF = {"", "", "", "", "", "", "", "",
                  "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"},
            FF_ESR = {"", "", "", "", "", "", "", "",
                      "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"})
    public void borderColorRgb() throws Exception {
        border("rGb(20, 40, 60)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"thin", "thin", "thin", "thin", "solid", "solid", "solid", "solid",
             "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    public void borderAll() throws Exception {
        border("tHin sOlid #fFccdd");
    }

    private void border(final String borderStyle) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  <div id='tester' style='border: " + borderStyle + "' >hello</div>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var myStyle = document.getElementById('tester').style;\n"
            + "    log(myStyle.borderTopWidth);\n"
            + "    log(myStyle.borderRightWidth);\n"
            + "    log(myStyle.borderBottomWidth);\n"
            + "    log(myStyle.borderLeftWidth);\n"
            + "    log(myStyle.borderTopStyle);\n"
            + "    log(myStyle.borderRightStyle);\n"
            + "    log(myStyle.borderBottomStyle);\n"
            + "    log(myStyle.borderLeftStyle);\n"
            + "    log(myStyle.borderTopColor);\n"
            + "    log(myStyle.borderRightColor);\n"
            + "    log(myStyle.borderBottomColor);\n"
            + "    log(myStyle.borderLeftColor);\n"
            + "  </script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", "", "", "", "", "", "", "", ""})
    public void borderCssEmpty() throws Exception {
        borderCss("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"thin", "thin", "thin", "thin", "initial", "initial", "initial", "initial",
                       "initial", "initial", "initial", "initial"},
            FF = {"thin", "thin", "thin", "thin", "none", "none", "none", "none",
                  "currentcolor", "currentcolor", "currentcolor", "currentcolor"},
            FF_ESR = {"thin", "thin", "thin", "thin", "none", "none", "none", "none",
                      "currentcolor", "currentcolor", "currentcolor", "currentcolor"})
    @HtmlUnitNYI(CHROME = {"tHin", "tHin", "tHin", "tHin", "", "", "", "", "", "", "", ""},
            EDGE = {"tHin", "tHin", "tHin", "tHin", "", "", "", "", "", "", "", ""},
            FF = {"tHin", "tHin", "tHin", "tHin", "", "", "", "", "", "", "", ""},
            FF_ESR = {"tHin", "tHin", "tHin", "tHin", "", "", "", "", "", "", "", ""})
    public void borderCssWidth() throws Exception {
        borderCss("tHin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2px", "2px", "2px", "2px", "initial", "initial", "initial", "initial",
                       "initial", "initial", "initial", "initial"},
            FF = {"2px", "2px", "2px", "2px", "none", "none", "none", "none",
                  "currentcolor", "currentcolor", "currentcolor", "currentcolor"},
            FF_ESR = {"2px", "2px", "2px", "2px", "none", "none", "none", "none",
                      "currentcolor", "currentcolor", "currentcolor", "currentcolor"})
    @HtmlUnitNYI(CHROME = {"2px", "2px", "2px", "2px", "", "", "", "", "", "", "", ""},
            EDGE = {"2px", "2px", "2px", "2px", "", "", "", "", "", "", "", ""},
            FF = {"2px", "2px", "2px", "2px", "", "", "", "", "", "", "", ""},
            FF_ESR = {"2px", "2px", "2px", "2px", "", "", "", "", "", "", "", ""})
    public void borderCssWidthNumber() throws Exception {
        borderCss("2pX");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "solid", "solid", "solid", "solid",
                       "initial", "initial", "initial", "initial"},
            FF = {"medium", "medium", "medium", "medium", "solid", "solid", "solid", "solid",
                  "currentcolor", "currentcolor", "currentcolor", "currentcolor"},
            FF_ESR = {"medium", "medium", "medium", "medium", "solid", "solid", "solid", "solid",
                      "currentcolor", "currentcolor", "currentcolor", "currentcolor"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "sOlid", "sOlid", "sOlid", "sOlid", "", "", "", ""},
            EDGE = {"", "", "", "", "sOlid", "sOlid", "sOlid", "sOlid", "", "", "", ""},
            FF = {"", "", "", "", "sOlid", "sOlid", "sOlid", "sOlid", "", "", "", ""},
            FF_ESR = {"", "", "", "", "sOlid", "sOlid", "sOlid", "sOlid", "", "", "", ""})
    public void borderCssStyle() throws Exception {
        borderCss("sOlid");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"thin", "thin", "thin", "thin", "solid", "solid", "solid", "solid",
                       "initial", "initial", "initial", "initial"},
            FF = {"thin", "thin", "thin", "thin", "solid", "solid", "solid", "solid",
                  "currentcolor", "currentcolor", "currentcolor", "currentcolor"},
            FF_ESR = {"thin", "thin", "thin", "thin", "solid", "solid", "solid", "solid",
                      "currentcolor", "currentcolor", "currentcolor", "currentcolor"})
    @HtmlUnitNYI(CHROME = {"tHin", "tHin", "tHin", "tHin", "sOlid", "sOlid", "sOlid", "sOlid", "", "", "", ""},
            EDGE = {"tHin", "tHin", "tHin", "tHin", "sOlid", "sOlid", "sOlid", "sOlid", "", "", "", ""},
            FF = {"tHin", "tHin", "tHin", "tHin", "sOlid", "sOlid", "sOlid", "sOlid", "", "", "", ""},
            FF_ESR = {"tHin", "tHin", "tHin", "tHin", "sOlid", "sOlid", "sOlid", "sOlid", "", "", "", ""})
    public void borderCssWidthAndStyle() throws Exception {
        borderCss("tHin sOlid");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2px", "2px", "2px", "2px", "solid", "solid", "solid", "solid",
                       "initial", "initial", "initial", "initial"},
            FF = {"2px", "2px", "2px", "2px", "solid", "solid", "solid", "solid",
                  "currentcolor", "currentcolor", "currentcolor", "currentcolor"},
            FF_ESR = {"2px", "2px", "2px", "2px", "solid", "solid", "solid", "solid",
                      "currentcolor", "currentcolor", "currentcolor", "currentcolor"})
    @HtmlUnitNYI(CHROME = {"2px", "2px", "2px", "2px", "sOlid", "sOlid", "sOlid", "sOlid", "", "", "", ""},
            EDGE = {"2px", "2px", "2px", "2px", "sOlid", "sOlid", "sOlid", "sOlid", "", "", "", ""},
            FF = {"2px", "2px", "2px", "2px", "sOlid", "sOlid", "sOlid", "sOlid", "", "", "", ""},
            FF_ESR = {"2px", "2px", "2px", "2px", "sOlid", "sOlid", "sOlid", "sOlid", "", "", "", ""})
    public void borderCssWidthNumberAndStyle() throws Exception {
        borderCss("2pX sOlid");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "initial", "initial", "initial", "initial",
                       "red", "red", "red", "red"},
            FF = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                  "red", "red", "red", "red"},
            FF_ESR = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                      "red", "red", "red", "red"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "", "", "rEd", "rEd", "rEd", "rEd"},
            EDGE = {"", "", "", "", "", "", "", "", "rEd", "rEd", "rEd", "rEd"},
            FF = {"", "", "", "", "", "", "", "", "rEd", "rEd", "rEd", "rEd"},
            FF_ESR = {"", "", "", "", "", "", "", "", "rEd", "rEd", "rEd", "rEd"})
    public void borderCssColor() throws Exception {
        borderCss("rEd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "initial", "initial", "initial", "initial",
                       "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                  "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF_ESR = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                      "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "", "",
                           "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            EDGE = {"", "", "", "", "", "", "", "",
                    "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF = {"", "", "", "", "", "", "", "",
                  "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF_ESR = {"", "", "", "", "", "", "", "",
                      "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    public void borderCssColorHex() throws Exception {
        borderCss("#fFccdd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "initial", "initial", "initial", "initial",
                       "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                  "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF_ESR = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                      "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "", "",
                           "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            EDGE = {"", "", "", "", "", "", "", "",
                    "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF = {"", "", "", "", "", "", "", "",
                  "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF_ESR = {"", "", "", "", "", "", "", "",
                      "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    public void borderCssColorHexShort() throws Exception {
        borderCss("#fCd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "initial", "initial", "initial", "initial",
                       "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"},
            FF = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                  "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"},
            FF_ESR = {"medium", "medium", "medium", "medium", "none", "none", "none", "none",
                      "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"})
    @HtmlUnitNYI(CHROME = {"", "", "", "", "", "", "", "",
                           "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"},
            EDGE = {"", "", "", "", "", "", "", "",
                    "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"},
            FF = {"", "", "", "", "", "", "", "",
                  "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"},
            FF_ESR = {"", "", "", "", "", "", "", "",
                      "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"})
    public void borderCssColorRgb() throws Exception {
        borderCss("rGb(20, 40, 60)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"thin", "thin", "thin", "thin", "solid", "solid", "solid", "solid",
             "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    @HtmlUnitNYI(CHROME = {"tHin", "tHin", "tHin", "tHin", "sOlid", "sOlid", "sOlid", "sOlid",
                           "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            EDGE = {"tHin", "tHin", "tHin", "tHin", "sOlid", "sOlid", "sOlid", "sOlid",
                    "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF = {"tHin", "tHin", "tHin", "tHin", "sOlid", "sOlid", "sOlid", "sOlid",
                  "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF_ESR = {"tHin", "tHin", "tHin", "tHin", "sOlid", "sOlid", "sOlid", "sOlid",
                      "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    public void borderCssAll() throws Exception {
        borderCss("tHin sOlid #fFccdd");
    }

    private void borderCss(final String borderStyle) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "</head>\n"
            + "  <style type='text/css'>div { border: " + borderStyle + " }</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div id='tester'>hello</div>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var myStyle = document.styleSheets[0].cssRules[0].style;\n"
            + "    log(myStyle.borderTopWidth);\n"
            + "    log(myStyle.borderRightWidth);\n"
            + "    log(myStyle.borderBottomWidth);\n"
            + "    log(myStyle.borderLeftWidth);\n"
            + "    log(myStyle.borderTopStyle);\n"
            + "    log(myStyle.borderRightStyle);\n"
            + "    log(myStyle.borderBottomStyle);\n"
            + "    log(myStyle.borderLeftStyle);\n"
            + "    log(myStyle.borderTopColor);\n"
            + "    log(myStyle.borderRightColor);\n"
            + "    log(myStyle.borderBottomColor);\n"
            + "    log(myStyle.borderLeftColor);\n"
            + "  </script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0px", "0px", "0px", "0px", "none", "none", "none", "none",
             "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"})
    public void borderComputedEmpty() throws Exception {
        borderComputed("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0px", "0px", "0px", "0px", "none", "none", "none", "none",
             "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"})
    public void borderComputedWidth() throws Exception {
        borderComputed("tHin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0px", "0px", "0px", "0px", "none", "none", "none", "none",
             "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"})
    @HtmlUnitNYI(CHROME = {"2px", "2px", "2px", "2px", "none", "none", "none", "none",
                           "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"},
            EDGE = {"2px", "2px", "2px", "2px", "none", "none", "none", "none",
                    "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"},
            FF = {"2px", "2px", "2px", "2px", "none", "none", "none", "none",
                  "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"},
            FF_ESR = {"2px", "2px", "2px", "2px", "none", "none", "none", "none",
                      "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"})
    public void borderComputedWidthNumber() throws Exception {
        borderComputed("2pX");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3px", "3px", "3px", "3px", "solid", "solid", "solid", "solid",
             "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"})
    @HtmlUnitNYI(CHROME = {"0px", "0px", "0px", "0px", "solid", "solid", "solid", "solid",
                           "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"},
            EDGE = {"0px", "0px", "0px", "0px", "solid", "solid", "solid", "solid",
                    "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"},
            FF = {"0px", "0px", "0px", "0px", "solid", "solid", "solid", "solid",
                  "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"},
            FF_ESR = {"0px", "0px", "0px", "0px", "solid", "solid", "solid", "solid",
                      "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"})
    public void borderComputedStyle() throws Exception {
        borderComputed("sOlid");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1px", "1px", "1px", "1px", "solid", "solid", "solid", "solid",
             "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"})
    @HtmlUnitNYI(CHROME = {"0px", "0px", "0px", "0px", "solid", "solid", "solid", "solid",
                           "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"},
            EDGE = {"0px", "0px", "0px", "0px", "solid", "solid", "solid", "solid",
                    "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"},
            FF = {"0px", "0px", "0px", "0px", "solid", "solid", "solid", "solid",
                  "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"},
            FF_ESR = {"0px", "0px", "0px", "0px", "solid", "solid", "solid", "solid",
                      "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"})
    public void borderComputedWidthAndStyle() throws Exception {
        borderComputed("tHin sOlid");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2px", "2px", "2px", "2px", "solid", "solid", "solid", "solid",
             "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)", "rgb(0, 0, 0)"})
    public void borderComputedWidthNumberAndStyle() throws Exception {
        borderComputed("2pX sOlid");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0px", "0px", "0px", "0px", "none", "none", "none", "none",
             "rgb(255, 0, 0)", "rgb(255, 0, 0)", "rgb(255, 0, 0)", "rgb(255, 0, 0)"})
    @HtmlUnitNYI(CHROME = {"0px", "0px", "0px", "0px", "none", "none", "none", "none", "red", "red", "red", "red"},
            EDGE = {"0px", "0px", "0px", "0px", "none", "none", "none", "none", "red", "red", "red", "red"},
            FF = {"0px", "0px", "0px", "0px", "none", "none", "none", "none", "red", "red", "red", "red"},
            FF_ESR = {"0px", "0px", "0px", "0px", "none", "none", "none", "none", "red", "red", "red", "red"})
    public void borderComputedColor() throws Exception {
        borderComputed("rEd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0px", "0px", "0px", "0px", "none", "none", "none", "none",
             "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    public void borderComputedColorHex() throws Exception {
        borderComputed("#fFccdd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0px", "0px", "0px", "0px", "none", "none", "none", "none",
             "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    public void borderComputedColorHexShort() throws Exception {
        borderComputed("#fCd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0px", "0px", "0px", "0px", "none", "none", "none", "none",
             "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)", "rgb(20, 40, 60)"})
    public void borderComputedColorRgb() throws Exception {
        borderComputed("rGb(20, 40, 60)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1px", "1px", "1px", "1px", "solid", "solid", "solid", "solid",
             "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    @HtmlUnitNYI(CHROME = {"0px", "0px", "0px", "0px", "solid", "solid", "solid", "solid",
                           "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            EDGE = {"0px", "0px", "0px", "0px", "solid", "solid", "solid", "solid",
                    "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF = {"0px", "0px", "0px", "0px", "solid", "solid", "solid", "solid",
                  "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"},
            FF_ESR = {"0px", "0px", "0px", "0px", "solid", "solid", "solid", "solid",
                      "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)", "rgb(255, 204, 221)"})
    public void borderComputedAll() throws Exception {
        borderComputed("tHin sOlid #fFccdd");
    }

    private void borderComputed(final String borderStyle) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "</head>\n"
            + "  <style type='text/css'>div { border: " + borderStyle + " }</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div id='tester'>hello</div>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var myDiv = document.getElementById('tester');\n"
            + "    var myStyle = window.getComputedStyle(myDiv, null);\n"
            + "    log(myStyle.borderTopWidth);\n"
            + "    log(myStyle.borderRightWidth);\n"
            + "    log(myStyle.borderBottomWidth);\n"
            + "    log(myStyle.borderLeftWidth);\n"
            + "    log(myStyle.borderTopStyle);\n"
            + "    log(myStyle.borderRightStyle);\n"
            + "    log(myStyle.borderBottomStyle);\n"
            + "    log(myStyle.borderLeftStyle);\n"
            + "    log(myStyle.borderTopColor);\n"
            + "    log(myStyle.borderRightColor);\n"
            + "    log(myStyle.borderBottomColor);\n"
            + "    log(myStyle.borderLeftColor);\n"
            + "  </script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

}
