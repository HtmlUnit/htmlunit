/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link CSSStyleDeclaration} background shorthand.
 *
 * @version $Revision$
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class CSSStyleDeclaration3Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "", "", "", "" },
            IE8 = { "transparent", "none", "repeat", "0% 0%", "scroll" })
    @NotYetImplemented(IE8)
    public void backgroundEmpty() throws Exception {
        background("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "red", "none", "repeat", "0% 0%", "scroll" })
    public void backgroundColorRed() throws Exception {
        background("red");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "rgb(20, 40, 60)", "none", "repeat", "0% 0%", "scroll" })
    public void backgroundColorRgb() throws Exception {
        background("rgb(20, 40, 60)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "transparent", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll" },
            IE8 = { "transparent", "url(myImage.png)", "repeat", "0% 0%", "scroll" })
    public void backgroundImage() throws Exception {
        background("url(myImage.png)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "transparent", "none", "repeat-x", "0% 0%", "scroll" })
    public void backgroundRepeat() throws Exception {
        background("repeat-x");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "transparent", "none", "repeat", "20px 100%", "scroll" })
    public void backgroundPosition() throws Exception {
        background("20px 100%");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "transparent", "none", "repeat", "right bottom", "scroll" })
    public void backgroundPosition2() throws Exception {
        background("bottom right");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "transparent", "none", "repeat", "10em bottom", "scroll" })
    public void backgroundPosition3() throws Exception {
        background("10em bottom");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "transparent", "none", "repeat", "10em center", "scroll" },
            IE11 = { "transparent", "none", "repeat", "10em", "scroll" })
    public void backgroundPosition4() throws Exception {
        background("10em center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "transparent", "none", "repeat", "0% 0%", "fixed" })
    public void backgroundAttachment() throws Exception {
        background("fixed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll" },
            IE8 = { "#ffccdd", "none", "repeat", "0% 0%", "scroll" })
    public void backgroundColorHex() throws Exception {
        background("#ffccdd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "red", "url(\"myImage.png\")", "repeat", "0% 0%", "scroll" },
            IE8 = { "red", "url(myImage.png)", "repeat", "0% 0%", "scroll" })
    public void backgroundMixed() throws Exception {
        background("red url(\"myImage.png\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "rgb(255, 255, 255)", "none", "no-repeat", "20px 100px", "scroll" },
            IE8 = { "#fff", "none", "no-repeat", "20px 100px", "scroll" })
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
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "transparent", "none", "repeat", "0% 0%", "scroll" },
            IE8 = { "transparent", "none", "repeat", "undefined", "scroll" })
    @NotYetImplemented(IE8)
    public void backgroundCssEmpty() throws Exception {
        backgroundCss("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "rgb(255, 0, 0)", "none", "repeat", "0% 0%", "scroll" },
            IE = { "red", "none", "repeat", "undefined", "scroll" },
            IE11 = { "red", "none", "repeat", "0% 0%", "scroll" })
    @NotYetImplemented(IE8)
    public void backgroundCssColorRed() throws Exception {
        backgroundCss("red");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "rgb(20, 40, 60)", "none", "repeat", "0% 0%", "scroll" },
            IE8 = { "rgb(20,40,60)", "none", "repeat", "undefined", "scroll" })
    @NotYetImplemented(IE8)
    public void backgroundCssColorRgb() throws Exception {
        backgroundCss("rgb(20, 40, 60)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "transparent", "url(\"http://localhost:12345/myImage.png\")", "repeat", "0% 0%", "scroll" },
            IE8 = { "transparent", "url(\"http://localhost:12346/myImage.png\")", "repeat", "undefined", "scroll" })
    @NotYetImplemented
    public void backgroundCssImage() throws Exception {
        backgroundCss("url(myImage.png)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "transparent", "none", "repeat-x", "0% 0%", "scroll" },
            IE8 = { "transparent", "none", "repeat-x", "undefined", "scroll" })
    @NotYetImplemented(IE8)
    public void backgroundCssRepeat() throws Exception {
        backgroundCss("repeat-x");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "transparent", "none", "repeat", "20px 100%", "scroll" },
            IE8 = { "transparent", "none", "repeat", "undefined", "scroll" })
    @NotYetImplemented(IE8)
    public void backgroundCssPosition() throws Exception {
        backgroundCss("20px 100%");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "transparent", "none", "repeat", "100% 100%", "scroll" },
            IE = { "transparent", "none", "repeat", "undefined", "scroll" },
            IE11 = { "transparent", "none", "repeat", "right bottom", "scroll" })
    @NotYetImplemented(IE8)
    public void backgroundCssPosition2() throws Exception {
        backgroundCss("bottom right");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "transparent", "none", "repeat", "0% 100%", "scroll" },
            IE = { "transparent", "none", "repeat", "undefined", "scroll" },
            IE11 = { "transparent", "none", "repeat", "left bottom", "scroll" })
    @NotYetImplemented(IE8)
    public void backgroundCssPosition3() throws Exception {
        backgroundCss("left bottom");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "transparent", "none", "repeat", "50% 0%", "scroll" },
            IE = { "transparent", "none", "repeat", "undefined", "scroll" },
            IE11 = { "transparent", "none", "repeat", "top", "scroll" })
    @NotYetImplemented(IE8)
    public void backgroundCssPosition4() throws Exception {
        backgroundCss("top center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "transparent", "none", "repeat", "0% 0%", "fixed" },
            IE8 = { "transparent", "none", "repeat", "undefined", "fixed" })
    @NotYetImplemented(IE8)
    public void backgroundCssAttachment() throws Exception {
        backgroundCss("fixed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "rgb(255, 204, 221)", "none", "repeat", "0% 0%", "scroll" },
            IE = { "#ffccdd", "none", "repeat", "undefined", "scroll" },
            IE11 = { "#ffccdd", "none", "repeat", "0% 0%", "scroll" })
    @NotYetImplemented(IE8)
    public void backgroundCssColorHex() throws Exception {
        backgroundCss("#ffccdd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "rgb(255, 0, 0)", "url(\"http://localhost:12345/myImage.png\")", "repeat", "0% 0%", "scroll" },
            IE = { "red", "url(\"http://localhost:12346/myImage.png\")", "repeat", "undefined", "scroll" },
            IE11 = { "red", "url(\"http://localhost:12345/myImage.png\")", "repeat", "0% 0%", "scroll" })
    @NotYetImplemented
    public void backgroundCssMixed() throws Exception {
        backgroundCss("red url(\"myImage.png\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "rgb(255, 255, 255)", "none", "no-repeat", "20px 100px", "scroll" },
            IE = { "#fff", "none", "no-repeat", "undefined", "scroll" },
            IE11 = { "#fff", "none", "no-repeat", "20px 100px", "scroll" })
    @NotYetImplemented(IE8)
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
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0px",
            IE = "auto")
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
            + "    var myDivStyle = myDiv.currentStyle;\n"
            + "    if(!myDivStyle) myDivStyle = window.getComputedStyle(myDiv, null);\n"
            + "    alert(myDivStyle.width);\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
