/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link CSSStyleDeclaration} background shorthand.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CSSStyleDeclaration3Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "", "", "", "", "" },
            IE = { "transparent", "none", "repeat", "0% 0%", "scroll" })
    @NotYetImplemented(Browser.IE)
    public void backgroundEmpty() throws Exception {
        background("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "red", "none", "repeat", "0% 0%", "scroll" })
    @NotYetImplemented
    public void backgroundColorRed() throws Exception {
        background("red");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "rgb(20, 40, 60)", "none", "repeat", "0% 0%", "scroll" })
    @NotYetImplemented
    public void backgroundColorRgb() throws Exception {
        background("rgb(20, 40, 60)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF3 = { "transparent", "url(myImage.png)", "repeat", "0% 0%", "scroll" },
            FF3_6 = { "transparent", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll" },
            IE = { "transparent", "url(myImage.png)", "repeat", "0% 0%", "scroll" })
    @NotYetImplemented
    public void backgroundImage() throws Exception {
        background("url(myImage.png)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "transparent", "none", "repeat-x", "0% 0%", "scroll" })
    @NotYetImplemented
    public void backgroundRepeat() throws Exception {
        background("repeat-x");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "transparent", "none", "repeat", "20px 100%", "scroll" })
    @NotYetImplemented
    public void backgroundPosition() throws Exception {
        background("20px 100%");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "transparent", "none", "repeat", "right bottom", "scroll" })
    @NotYetImplemented
    public void backgroundPosition2() throws Exception {
        background("bottom right");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "transparent", "none", "repeat", "10em bottom", "scroll" })
    @NotYetImplemented
    public void backgroundPosition3() throws Exception {
        background("10em bottom");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "transparent", "none", "repeat", "10em center", "scroll" })
    @NotYetImplemented
    public void backgroundPosition4() throws Exception {
        background("10em center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "transparent", "none", "repeat", "0% 0%", "fixed" })
    @NotYetImplemented
    public void backgroundAttachment() throws Exception {
        background("fixed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll" },
            IE = { "#ffccdd", "none", "repeat", "0% 0%", "scroll" })
    @NotYetImplemented
    public void backgroundColorHex() throws Exception {
        background("#ffccdd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF3 = { "red", "url(myImage.png)", "repeat", "0% 0%", "scroll" },
            FF3_6 = { "red", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll" },
            IE = { "red", "url(myImage.png)", "repeat", "0% 0%", "scroll" })
    @NotYetImplemented
    public void backgroundMixed() throws Exception {
        background("red url(\"myImage.png\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "rgb(255, 255, 255)", "none", "no-repeat", "20px 100px", "scroll" },
            IE = { "#fff", "none", "no-repeat", "20px 100px", "scroll" })
    @NotYetImplemented
    public void backgroundMixed2() throws Exception {
        background("#fff no-repeat 20px 100px");
    }

    private void background(final String backgroundStyle) throws Exception {
        final String html =
            "<html>\n"
            + "<body>\n"
            + "  <div id='tester' style='background: " + backgroundStyle + "' >hello</div>\n"
            + "  <script>\n"
            + "    var myDivStyle = document.getElementById('tester').style;\n"
            + "    alert(myDivStyle.backgroundColor);\n"
            + "    alert(myDivStyle.backgroundImage);\n"
            + "    alert(myDivStyle.backgroundRepeat);\n"
            + "    alert(myDivStyle.backgroundPosition);\n"
            + "    alert(myDivStyle.backgroundAttachment);\n"
            + "  </script>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "transparent", "none", "repeat", "0% 0%", "scroll" },
            IE = { "transparent", "none", "repeat", "undefined", "scroll" })
    @NotYetImplemented
    public void backgroundCssEmpty() throws Exception {
        backgroundCss("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "rgb(255, 0, 0)", "none", "repeat", "0% 0%", "scroll" },
            IE = { "red", "none", "repeat", "undefined", "scroll" })
    @NotYetImplemented
    public void backgroundCssColorRed() throws Exception {
        backgroundCss("red");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "rgb(20, 40, 60)", "none", "repeat", "0% 0%", "scroll" },
            IE = { "rgb(20,40,60)", "none", "repeat", "undefined", "scroll" })
    @NotYetImplemented
    public void backgroundCssColorRgb() throws Exception {
        backgroundCss("rgb(20, 40, 60)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF3 = { "transparent", "url(http://localhost:12345/myImage.png)", "repeat", "0% 0%", "scroll" },
            FF3_6 = { "transparent", "url(\"http://localhost:12345/myImage.png\")", "repeat", "0% 0%", "scroll" },
            IE = { "transparent", "url(\"http://localhost:12346/myImage.png\")", "repeat", "undefined", "scroll" })
    @NotYetImplemented
    public void backgroundCssImage() throws Exception {
        backgroundCss("url(myImage.png)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "transparent", "none", "repeat-x", "0% 0%", "scroll" },
            IE = { "transparent", "none", "repeat-x", "undefined", "scroll" })
    @NotYetImplemented
    public void backgroundCssRepeat() throws Exception {
        backgroundCss("repeat-x");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "transparent", "none", "repeat", "20px 100%", "scroll" },
            IE = { "transparent", "none", "repeat", "undefined", "scroll" })
    @NotYetImplemented
    public void backgroundCssPosition() throws Exception {
        backgroundCss("20px 100%");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "transparent", "none", "repeat", "100% 100%", "scroll" },
            IE = { "transparent", "none", "repeat", "undefined", "scroll" })
    @NotYetImplemented
    public void backgroundCssPosition2() throws Exception {
        backgroundCss("bottom right");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "transparent", "none", "repeat", "160px 100%", "scroll" },
            IE = { "transparent", "none", "repeat", "undefined", "scroll" })
    @NotYetImplemented
    public void backgroundCssPosition3() throws Exception {
        backgroundCss("10em bottom");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "transparent", "none", "repeat", "160px 50%", "scroll" },
            IE = { "transparent", "none", "repeat", "undefined", "scroll" })
    @NotYetImplemented
    public void backgroundCssPosition4() throws Exception {
        backgroundCss("10em center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "transparent", "none", "repeat", "0% 0%", "fixed" },
            IE = { "transparent", "none", "repeat", "undefined", "fixed" })
    @NotYetImplemented
    public void backgroundCssAttachment() throws Exception {
        backgroundCss("fixed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll" },
            IE = { "#ffccdd", "none", "repeat", "undefined", "scroll" })
    @NotYetImplemented
    public void backgroundCssColorHex() throws Exception {
        backgroundCss("#ffccdd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF3 = { "rgb(255, 0, 0)", "url(http://localhost:12345/myImage.png)", "repeat", "0% 0%", "scroll" },
            FF3_6 = { "rgb(255, 0, 0)", "url(\"http://localhost:12345/myImage.png\")", "repeat", "0% 0%", "scroll" },
            IE = { "red", "url(\"http://localhost:12346/myImage.png\")", "repeat", "undefined", "scroll" })
    @NotYetImplemented
    public void backgroundCssMixed() throws Exception {
        backgroundCss("red url(\"myImage.png\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            FF = { "rgb(255, 255, 255)", "none", "no-repeat", "20px 100px", "scroll" },
            IE = { "#fff", "none", "no-repeat", "undefined", "scroll" })
    @NotYetImplemented
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
            + "    var myDiv = document.getElementById('tester');\n"
            + "    var myDivStyle = myDiv.currentStyle;\n"
            + "    if(!myDivStyle) myDivStyle = window.getComputedStyle(myDiv, null);\n"
            + "    alert(myDivStyle.backgroundColor);\n"
            + "    alert(myDivStyle.backgroundImage);\n"
            + "    alert(myDivStyle.backgroundRepeat);\n"
            + "    alert(myDivStyle.backgroundPosition);\n"
            + "    alert(myDivStyle.backgroundAttachment);\n"
            + "  </script>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
