/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link CSSStyleDeclaration} background shorthand.
 *
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
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
        background("red");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgb(20, 40, 60)", "none", "repeat", "0% 0%", "scroll"},
            CHROME = {"rgb(20, 40, 60)", "initial", "initial", "initial", "initial"},
            EDGE = {"rgb(20, 40, 60)", "initial", "initial", "initial", "initial"})
    public void backgroundColorRgb() throws Exception {
        background("rgb(20, 40, 60)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "url(\"myImage.png\")", "initial", "initial", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll"},
            FF78 = {"rgba(0, 0, 0, 0)", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll"},
            IE = {"transparent", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll"})
    public void backgroundImage() throws Exception {
        background("url(myImage.png)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "repeat-x", "initial", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat-x", "0% 0%", "scroll"},
            FF78 = {"rgba(0, 0, 0, 0)", "none", "repeat-x", "0% 0%", "scroll"},
            IE = {"transparent", "none", "repeat-x", "0% 0%", "scroll"})
    public void backgroundRepeat() throws Exception {
        background("repeat-x");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "20px 100%", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "20px 100%", "scroll"},
            FF78 = {"rgba(0, 0, 0, 0)", "none", "repeat", "20px 100%", "scroll"},
            IE = {"transparent", "none", "repeat", "20px 100%", "scroll"})
    public void backgroundPosition() throws Exception {
        background("20px 100%");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "right bottom", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "right bottom", "scroll"},
            FF78 = {"rgba(0, 0, 0, 0)", "none", "repeat", "right bottom", "scroll"},
            IE = {"transparent", "none", "repeat", "right bottom", "scroll"})
    public void backgroundPosition2() throws Exception {
        background("bottom right");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "10em bottom", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "10em bottom", "scroll"},
            FF78 = {"rgba(0, 0, 0, 0)", "none", "repeat", "10em bottom", "scroll"},
            IE = {"transparent", "none", "repeat", "10em bottom", "scroll"})
    public void backgroundPosition3() throws Exception {
        background("10em bottom");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "10em center", "initial"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "10em center", "scroll"},
            FF78 = {"rgba(0, 0, 0, 0)", "none", "repeat", "10em center", "scroll"},
            IE = {"transparent", "none", "repeat", "10em", "scroll"})
    public void backgroundPosition4() throws Exception {
        background("10em center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"initial", "initial", "initial", "initial", "fixed"},
            FF = {"rgba(0, 0, 0, 0)", "none", "repeat", "0% 0%", "fixed"},
            FF78 = {"rgba(0, 0, 0, 0)", "none", "repeat", "0% 0%", "fixed"},
            IE = {"transparent", "none", "repeat", "0% 0%", "fixed"})
    public void backgroundAttachment() throws Exception {
        background("fixed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll"},
            CHROME = {"rgb(255, 204, 221)", "initial", "initial", "initial", "initial"},
            EDGE = {"rgb(255, 204, 221)", "initial", "initial", "initial", "initial"})
    public void backgroundColorHex() throws Exception {
        background("#ffccdd");
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
        final String html =
            "<html>\n"
            + "<body>\n"
            + "  <div id='tester' style='background: " + backgroundStyle + "' >hello</div>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var myDivStyle = document.getElementById('tester').style;\n"
            + "    log(myDivStyle.backgroundColor);\n"
            + "    log(myDivStyle.backgroundImage);\n"
            + "    log(myDivStyle.backgroundRepeat);\n"
            + "    log(myDivStyle.backgroundPosition);\n"
            + "    log(myDivStyle.backgroundAttachment);\n"
            + "  </script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgba(0, 0, 0, 0)", "none", "repeat", "0% 0%", "scroll"},
            IE = {"transparent", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundCssEmpty() throws Exception {
        backgroundCss("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(255, 0, 0)", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundCssColorRed() throws Exception {
        backgroundCss("red");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(20, 40, 60)", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundCssColorRgb() throws Exception {
        backgroundCss("rgb(20, 40, 60)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgba(0, 0, 0, 0)", "url(\"§§URL§§myImage.png\")", "repeat", "0% 0%", "scroll"},
            IE = {"transparent", "url(\"§§URL§§myImage.png\")", "repeat", "0% 0%", "scroll"})
    public void backgroundCssImage() throws Exception {
        backgroundCss("url(myImage.png)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgba(0, 0, 0, 0)", "none", "repeat-x", "0% 0%", "scroll"},
            IE = {"transparent", "none", "repeat-x", "0% 0%", "scroll"})
    public void backgroundCssRepeat() throws Exception {
        backgroundCss("repeat-x");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgba(0, 0, 0, 0)", "none", "repeat", "20px 100%", "scroll"},
            IE = {"transparent", "none", "repeat", "20px 100%", "scroll"})
    public void backgroundCssPosition() throws Exception {
        backgroundCss("20px 100%");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgba(0, 0, 0, 0)", "none", "repeat", "100% 100%", "scroll"},
            IE = {"transparent", "none", "repeat", "100% 100%", "scroll"})
    public void backgroundCssPosition2() throws Exception {
        backgroundCss("bottom right");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgba(0, 0, 0, 0)", "none", "repeat", "0% 100%", "scroll"},
            IE = {"transparent", "none", "repeat", "0% 100%", "scroll"})
    public void backgroundCssPosition3() throws Exception {
        backgroundCss("left bottom");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgba(0, 0, 0, 0)", "none", "repeat", "50% 0%", "scroll"},
            IE = {"transparent", "none", "repeat", "top", "scroll"})
    public void backgroundCssPosition4() throws Exception {
        backgroundCss("top center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgba(0, 0, 0, 0)", "none", "repeat", "0% 0%", "fixed"},
            IE = {"transparent", "none", "repeat", "0% 0%", "fixed"})
    public void backgroundCssAttachment() throws Exception {
        backgroundCss("fixed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll"})
    public void backgroundCssColorHex() throws Exception {
        backgroundCss("#ffccdd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(255, 0, 0)", "url(\"§§URL§§myImage.png\")", "repeat", "0% 0%", "scroll"})
    public void backgroundCssMixed() throws Exception {
        backgroundCss("red url(\"myImage.png\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(255, 255, 255)", "none", "no-repeat", "20px 100px", "scroll"})
    public void backgroundCssMixed2() throws Exception {
        backgroundCss("#fff no-repeat 20px 100px");
    }

    private void backgroundCss(final String backgroundStyle) throws Exception {
        final String html =
            "<html>\n"
            + "</head>\n"
            + "  <style type='text/css'>div { background: " + backgroundStyle + " }</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div id='tester'>hello</div>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var myDiv = document.getElementById('tester');\n"
            + "    var myDivStyle = window.getComputedStyle(myDiv, null);\n"
            + "    log(myDivStyle.backgroundColor);\n"
            + "    log(myDivStyle.backgroundImage);\n"
            + "    log(myDivStyle.backgroundRepeat);\n"
            + "    log(myDivStyle.backgroundPosition);\n"
            + "    log(myDivStyle.backgroundAttachment);\n"
            + "  </script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0px")
    public void widthAbsolute() throws Exception {
        final String html =
            "<html>\n"
            + "</head>\n"
            + "  <style type='text/css'>div {position: absolute;}</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div id='tester'></div>\n"
            + "  <script>\n"
            + "    var myDiv = document.getElementById('tester');\n"
            + "    var myDivStyle = window.getComputedStyle(myDiv, null);\n"
            + "    alert(myDivStyle.width);\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
