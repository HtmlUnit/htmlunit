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
package com.gargoylesoftware.htmlunit.html;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for elements with onblur and onfocus attributes.
 *
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class FocusableElement2Test extends WebDriverTestCase {

    /**
     * We like to start with a new browser for each test.
     * @throws Exception If an error occurs
     */
    @After
    public void shutDownRealBrowsers() throws Exception {
        super.shutDownAll();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"body", "active: body", "onload", "active: body",
                "onfocus:[object Window]", "active: body"},
            CHROME = {"body", "active: body", "onload", "active: body"},
            EDGE = {"body", "active: body", "onload", "active: body"},
            IE = {"body", "active: null", "onfocusin:body", "active: body", "onload", "active: body",
                "onfocus:[object Window]", "active: body"})
    @HtmlUnitNYI(FF = {"body", "active: body", "onload", "active: body"},
            FF78 = {"body", "active: body", "onload", "active: body"},
            IE = {"body", "active: body", "onload", "active: body"})
    // TODO FF & FF68 fail due to wrong body vs. window event handling
    public void bodyLoad() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + logger()
            + "      function test() {\n"
            + "        log('onload');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()' " + logEvents("") + ">\n"
            + "    <script>\n"
            + "      log('body');"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + (getExpectedAlerts().length > 0 ? ";" : ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocus:[object Window]", "active: focusId",
                "before", "active: focusId", "after", "active: focusId"},
            CHROME = {"before", "active: focusId", "after", "active: focusId"},
            EDGE = {"before", "active: focusId", "after", "active: focusId"},
            IE = {"onfocusin:focusId", "active: focusId", "onfocus:[object Window]", "active: focusId",
                "before", "active: focusId", "after", "active: focusId"})
    @HtmlUnitNYI(FF = {"before", "active: focusId", "after", "active: focusId"},
            FF78 = {"before", "active: focusId", "after", "active: focusId"},
            IE = {"before", "active: focusId", "onfocusout:focusId", "active: focusId", "after", "active: focusId"})
    // TODO FF & FF68 fail due to wrong body vs. window event handling
    public void body() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + logger()
            + "      function test() {\n"
            + "        log('before');\n"
            + "        document.getElementById('focusId').focus();\n"
            + "        document.getElementById('focusId').focus();\n"
            + "        document.getElementById('focusId').blur();\n"
            + "        document.getElementById('focusId').blur();\n"
            + "        log('after');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='focusId' onload='setTimeout(test, 10)' " + logEvents("") + ">\n"
            + "  </body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + (getExpectedAlerts().length > 0 ? ";" : ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocus:[object Window]", "active: body",
                "before", "active: body", "after", "active: body"},
            CHROME = {"before", "active: body", "after", "active: body"},
            EDGE = {"before", "active: body", "after", "active: body"},
            IE = {"onfocusin:body", "active: body", "onfocus:[object Window]", "active: body",
                "before", "active: body", "after", "active: body"})
    @HtmlUnitNYI(FF = {"before", "active: body", "after", "active: body"},
            FF78 = {"before", "active: body", "after", "active: body"},
            IE = {"before", "active: body", "after", "active: body"})
    // TODO FF & FF68 fail due to wrong body vs. window event handling
    public void bodySwitchFromBodyToNotFocusable() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + logger()
            + "      function test() {\n"
            + "        log('before');\n"
            + "        document.getElementById('focusId').focus();\n"
            + "        document.getElementById('focusId').blur();\n"
            + "        log('after');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='setTimeout(test, 10)' " + logEvents("") + ">\n"
            + "    <div id='focusId' " + logEvents("F") + ">div</div>\n"
            + "  </body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + (getExpectedAlerts().length > 0 ? ";" : ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocus:[object Window]", "active: body",
                "before", "active: body",
                "onfocusF:focusId", "active: focusId", "onfocusinF:focusId",
                "active: focusId", "onfocusin:focusId", "active: focusId",
                "onblurF:focusId", "active: body", "onfocusoutF:focusId",
                "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            CHROME = {"before", "active: body",
                "onfocusF:focusId", "active: focusId", "onfocusinF:focusId",
                "active: focusId", "onfocusin:focusId", "active: focusId",
                "onblurF:focusId", "active: body", "onfocusoutF:focusId",
                "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            EDGE = {"before", "active: body",
                "onfocusF:focusId", "active: focusId", "onfocusinF:focusId",
                "active: focusId", "onfocusin:focusId", "active: focusId",
                "onblurF:focusId", "active: body", "onfocusoutF:focusId",
                "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"onfocusin:body", "active: body", "onfocus:[object Window]", "active: body",
                "before", "active: body", "onfocusout:body", "active: focusId",
                "onfocusinF:focusId", "active: focusId", "onfocusin:focusId",
                "active: focusId", "onfocusoutF:focusId", "active: body",
                "onfocusout:focusId", "active: body", "onfocusin:body", "active: body", "after", "active: body",
                "onfocusF:focusId", "active: body", "onblurF:focusId", "active: body"})
    @HtmlUnitNYI(FF = {"before", "active: body",
                "onfocusF:focusId", "active: focusId", "onfocusinF:focusId",
                "active: focusId", "onfocusin:focusId", "active: focusId",
                "onblurF:focusId", "active: body", "onfocusoutF:focusId",
                "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            FF78 = {"before", "active: body",
                "onfocusF:focusId", "active: focusId", "onfocusinF:focusId",
                "active: focusId", "onfocusin:focusId", "active: focusId",
                "onblurF:focusId", "active: body", "onfocusoutF:focusId",
                "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body", "onfocusout:body", "active: body",
                "onfocusinF:focusId", "active: body", "onfocusin:focusId",
                "active: body", "onfocusF:focusId", "active: focusId",
                "onfocusoutF:focusId", "active: body", "onfocusout:focusId",
                "active: body", "onblurF:focusId", "active: body",
                "after", "active: body"})
    // TODO FF & FF68 fail due to wrong body vs. window event handling
    public void bodySwitchFromBodyToFocusable() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + logger()
            + "      function test() {\n"
            + "        log('before');\n"
            + "        document.getElementById('focusId').focus();\n"
            + "        document.getElementById('focusId').blur();\n"
            + "        log('after');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='setTimeout(test, 10)' " + logEvents("") + ">\n"
            + "    <input type='text' id='focusId' " + logEvents("F") + ">\n"
            + "  </body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + (getExpectedAlerts().length > 0 ? ";" : ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocus:[object Window]", "active: body",
                "before", "active: body", "onfocusin:focusId1", "active: focusId1", "after", "active: focusId1"},
            CHROME = {"before", "active: body", "onfocusin:focusId1", "active: focusId1", "after", "active: focusId1"},
            EDGE = {"before", "active: body", "onfocusin:focusId1", "active: focusId1", "after", "active: focusId1"},
            IE = {"onfocusin:body", "active: body", "onfocus:[object Window]", "active: body",
                "before", "active: body", "onfocusout:body", "active: focusId1",
                "onfocusin:focusId1", "active: focusId1", "after", "active: focusId1"})
    @HtmlUnitNYI(FF = {"before", "active: body", "onfocusin:focusId1", "active: focusId1", "after", "active: focusId1"},
            FF78 = {"before", "active: body", "onfocusin:focusId1", "active: focusId1", "after", "active: focusId1"},
            IE = {"before", "active: body", "onfocusout:body", "active: body", "onfocusin:focusId1",
                "active: body", "after", "active: focusId1"})
    // TODO FF & FF68 fail due to wrong body vs. window event handling
    public void bodySwitchFromFocusableToNotFocusable() throws Exception {
        testBodySwitchWithCallFocusAndBlur("<input type='text' id='focusId1'>\n"
            + "<div id='focusId2'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocus:[object Window]", "active: body", "before", "active: body",
                "onfocusin:focusId1", "active: focusId1",
                "onfocusout:focusId1", "active: body", "onfocusin:focusId2", "active: focusId2",
                "after", "active: focusId2"},
            CHROME = {"before", "active: body",
                "onfocusin:focusId1", "active: focusId1",
                "onfocusout:focusId1", "active: body", "onfocusin:focusId2", "active: focusId2",
                "after", "active: focusId2"},
            EDGE = {"before", "active: body",
                "onfocusin:focusId1", "active: focusId1",
                "onfocusout:focusId1", "active: body", "onfocusin:focusId2", "active: focusId2",
                "after", "active: focusId2"},
            IE = {"onfocusin:body", "active: body", "onfocus:[object Window]", "active: body",
                "before", "active: body", "onfocusout:body", "active: focusId1",
                "onfocusin:focusId1", "active: focusId1", "onfocusout:focusId1", "active: focusId2",
                "onfocusin:focusId2", "active: focusId2",
                "after", "active: focusId2"})
    @HtmlUnitNYI(FF = {"before", "active: body",
                "onfocusin:focusId1", "active: focusId1",
                "onfocusout:focusId1", "active: body", "onfocusin:focusId2", "active: focusId2",
                "after", "active: focusId2"},
            FF78 = {"before", "active: body",
                "onfocusin:focusId1", "active: focusId1",
                "onfocusout:focusId1", "active: body", "onfocusin:focusId2", "active: focusId2",
                "after", "active: focusId2"},
            IE = {"before", "active: body", "onfocusout:body", "active: body",
                "onfocusin:focusId1", "active: body",
                "onfocusout:focusId1", "active: body", "onfocusin:focusId2", "active: body",
                "after", "active: focusId2"})
    // TODO FF & FF68 fail due to wrong body vs. window event handling
    public void bodySwitchFromFocusableToFocusable() throws Exception {
        testBodySwitchWithCallFocusAndBlur("<input type='text' id='focusId1'>\n"
            + "<input type='text' id='focusId2'>");
    }

    private void testBodySwitchWithCallFocusAndBlur(final String snippet) throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + logger()
            + "      function test() {\n"
            + "        log('before');\n"
            + "        document.getElementById('focusId1').focus();\n"
            + "        document.getElementById('focusId2').focus();\n"
            + "        log('after');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='setTimeout(test, 10)' " + logEvents("") + ">\n"
            + snippet
            + "  </body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + (getExpectedAlerts().length > 0 ? ";" : ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void notFocusable() throws Exception {
        testWithCallFocusAndBlur("<div id='focusId'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void notFocusableWithTabIndexEmpty() throws Exception {
        testWithCallFocusAndBlur("<div tabindex='' id='focusId'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: focusId", "between", "active: focusId",
                "onfocusout:focusId", "active: body", "after", "active: body",
                "onfocus:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "between", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    public void notFocusableWithTabIndexNegative() throws Exception {
        testWithCallFocusAndBlur("<div tabindex='-1' id='focusId'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: focusId", "between", "active: focusId",
                "onfocusout:focusId", "active: body", "after", "active: body",
                "onfocus:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "between", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    public void notFocusableWithTabIndexZero() throws Exception {
        testWithCallFocusAndBlur("<div tabindex='0' id='focusId'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: focusId", "between", "active: focusId",
                "onfocusout:focusId", "active: body", "after", "active: body",
                "onfocus:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "between", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    public void notFocusableWithTabIndexPositive() throws Exception {
        testWithCallFocusAndBlur("<div tabindex='1' id='focusId'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void notFocusableWithTabIndexNotDisplayed() throws Exception {
        testWithCallFocusAndBlur("<div tabindex='0' style='display: none;' id='focusId'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void notFocusableWithTabIndexNotVisible() throws Exception {
        testWithCallFocusAndBlur("<div tabindex='0' style='visibility: hidden;' id='focusId'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void notFocusableWithTabIndexHidden() throws Exception {
        testWithCallFocusAndBlur("<div tabindex='0' hidden id='focusId'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: focusId", "between", "active: focusId",
                "onfocusout:focusId", "active: body", "after", "active: body",
                "onfocus:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "between", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    public void anchor() throws Exception {
        testWithCallFocusAndBlur("<a href='#' id='focusId'>link</a>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: focusId", "between", "active: focusId",
                "onfocusout:focusId", "active: body", "after", "active: body",
                "onfocus:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "between", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    public void anchorWithEmptyHref() throws Exception {
        testWithCallFocusAndBlur("<a href='' id='focusId'>link</a>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void anchorWithoutHref() throws Exception {
        testWithCallFocusAndBlur("<a id='focusId'>link</a>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: focusId", "between", "active: focusId",
                "onfocusout:focusId", "active: body", "after", "active: body",
                "onfocus:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "between", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    public void area() throws Exception {
        testWithCallFocusAndBlur("<map name='dot'><area shape='rect' coords='0,0,5,5' href='#' id='focusId' /></map>"
            + "<img usemap='#dot'"
            + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
            + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: focusId", "between", "active: focusId",
                "onfocusout:focusId", "active: body", "after", "active: body",
                "onfocus:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "between", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    public void areaWithEmptyHref() throws Exception {
        testWithCallFocusAndBlur("<img usemap='#dot'"
            + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
            + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
            + "<map name='dot'><area shape='rect' coords='0,0,1,1' href='' id='focusId' /></map>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body", "between", "active: body", "after", "active: body"},
            FF = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId",
                "active: body", "after", "active: body"},
            FF78 = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId",
                "active: body", "after", "active: body"})
    public void areaWithoutHref() throws Exception {
        testWithCallFocusAndBlur("<img usemap='#dot'"
            + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
            + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
            + "<map name='dot'><area shape='rect' coords='0,0,1,1' id='focusId' /></map>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: focusId", "between", "active: focusId",
                "onfocusout:focusId", "active: body", "after", "active: body",
                "onfocus:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "between", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    public void button() throws Exception {
        testWithCallFocusAndBlur("<button id='focusId'>button</button>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: focusId", "between", "active: focusId",
                "onfocusout:focusId", "active: body", "after", "active: body",
                "onfocus:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "between", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    public void input() throws Exception {
        testWithCallFocusAndBlur("<input type='text' id='focusId'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void inputHidden() throws Exception {
        testWithCallFocusAndBlur("<input type='hidden' id='focusId'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocusT:textId", "active: textId", "onfocusinT:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"},
            IE = {"before", "active: body", "between", "active: body", "after", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusinT:textId", "active: body", "onfocusT:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"})
    public void labelFor() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' id='focusId'>label</label><input type='text' id='textId' "
                + logEvents("T") + ">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void labelForDisabled() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' id='focusId'>label</label>"
                + "<input type='text' disabled id='textId' " + logEvents("T") + ">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocusT:textId", "active: textId", "onfocusinT:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"},
            IE = {"before", "active: body", "between", "active: body", "after", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusinT:textId", "active: body", "onfocusT:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"})
    public void labelForReadonly() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' id='focusId'>label</label>"
                + "<input type='text' readonly id='textId' " + logEvents("T") + ">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void labelForNotDisplayed() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' id='focusId'>label"
                + "</label><input type='text' style='display: none;' id='textId' " + logEvents("T") + ">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void labelForNotVisible() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' id='focusId'>label</label>"
                + "<input type='text' style='visibility: hidden;' id='textId' " + logEvents("T") + ">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void labelForHidden() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' id='focusId'>label</label>"
                + "<input type='text' hidden id='textId' " + logEvents("T") + ">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocusT:textId", "active: textId", "onfocusinT:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"},
            IE = {"before", "active: body", "between", "active: body", "after", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusinT:textId", "active: body", "onfocusT:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"})
    public void labelNotDisplayedFor() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' style='display: none;' id='focusId'>label</label>"
                + "<input type='text' id='textId' " + logEvents("T") + ">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocusT:textId", "active: textId", "onfocusinT:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"},
            IE = {"before", "active: body", "between", "active: body", "after", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusinT:textId", "active: body", "onfocusT:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"})
    public void labelNotVisibleFor() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' style='visibility: hidden;' id='focusId'>label</label>"
                + "<input type='text' id='textId' " + logEvents("T") + ">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocusT:textId", "active: textId", "onfocusinT:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"},
            IE = {"before", "active: body", "between", "active: body", "after", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusinT:textId", "active: body", "onfocusT:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"})
    public void labelHiddenFor() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' hidden id='focusId'>label</label>"
                + "<input type='text' id='textId' " + logEvents("T") + ">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocusT:textId", "active: textId", "onfocusinT:textId",
                "active: textId", "onfocusin:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"},
            IE = {"before", "active: body", "between", "active: body", "after", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusinT:textId", "active: body", "onfocusin:textId",
                "active: body", "onfocusT:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"})
    public void labelNested() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' id='focusId'>label <input type='text' id='textId' "
                + logEvents("T") + "></label>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void labelNestedDisabled() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' id='focusId'>label "
                + "<input type='text' disabled id='textId' " + logEvents("T") + "></label>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocusT:textId", "active: textId", "onfocusinT:textId",
                "active: textId", "onfocusin:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"},
            IE = {"before", "active: body", "between", "active: body", "after", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusinT:textId", "active: body", "onfocusin:textId",
                "active: body", "onfocusT:textId", "active: textId",
                "between", "active: textId", "after", "active: textId"})
    public void labelNestedReadonly() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' id='focusId'>label <input type='text' readonly id='textId' "
                + logEvents("T") + "></label>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void labelNestedNotDisplayed() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' id='focusId'>label "
                + "<input type='text' style='display: none;' id='textId' " + logEvents("T") + "></label>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void labelNestedNotVisible() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' id='focusId'>label "
                + "<input type='text' style='display: none;' id='textId' "
                + logEvents("T") + "></label>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void labelNestedHidden() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' id='focusId'>label <input type='text' hidden id='textId' "
                + logEvents("T") + "></label>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void labelNotDisplayedNested() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' style='display: none;' id='focusId'>label "
                + "<input type='text' id='textId' " + logEvents("T") + "></label>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void labelNotVisibleNested() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' style='visibility: hidden;' id='focusId'>label "
                + "<input type='text' id='textId' " + logEvents("T") + "></label>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void labelHiddenNested() throws Exception {
        testWithCallFocusAndBlur("<label for='textId' hidden id='focusId'>label "
                + "<input type='text' id='textId' " + logEvents("T") + "></label>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void optionGroup() throws Exception {
        testWithCallFocusAndBlur("<select><optgroup label='group' id='focusId'><option>1</option></optgroup></select>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void option() throws Exception {
        testWithCallFocusAndBlur("<select><option id='focusId'>1</option></select>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: focusId", "between", "active: focusId",
                "onfocusout:focusId", "active: body", "after", "active: body",
                "onfocus:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "between", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    public void select() throws Exception {
        testWithCallFocusAndBlur("<select id='focusId'><option>1</option></select>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: focusId", "between", "active: focusId",
                "onfocusout:focusId", "active: body", "after", "active: body",
                "onfocus:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "between", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    public void textArea() throws Exception {
        testWithCallFocusAndBlur("<textarea id='focusId'></textarea>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void focusableDisabled() throws Exception {
        testWithCallFocusAndBlur("<input type='text' disabled id='focusId'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "between", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: focusId", "between", "active: focusId",
                "onfocusout:focusId", "active: body", "after", "active: body",
                "onfocus:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "between", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    public void focusableReadonly() throws Exception {
        testWithCallFocusAndBlur("<input type='text' readonly id='focusId'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void focusableNotDisplayed() throws Exception {
        testWithCallFocusAndBlur("<input type='text' style='display: none;' id='focusId'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void focusableNotVisible() throws Exception {
        testWithCallFocusAndBlur("<input type='text' style='visibility: hidden;' id='focusId'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "between", "active: body", "after", "active: body"})
    public void focusableHidden() throws Exception {
        testWithCallFocusAndBlur("<input type='text' hidden id='focusId'>");
    }

    private void testWithCallFocusAndBlur(String snippet) throws Exception {
        snippet = snippet.replaceFirst("id='focusId'( /)?>", "id='focusId' " + logEvents("") + "$1>");

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + logger()
            + "      function test() {\n"
            + "        log('before');\n"
            + "        document.getElementById('focusId').focus();\n"
            + "        document.getElementById('focusId').focus();\n"
            + "        log('between');\n"
            + "        document.getElementById('focusId').blur();\n"
            + "        document.getElementById('focusId').blur();\n"
            + "        log('after');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='setTimeout(test, 10)'>\n"
            + snippet
            + "  </body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + (getExpectedAlerts().length > 0 ? ";" : ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1",
                "after", "active: focusId1"},
            IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: focusId1",
                "after", "active: focusId1",
                "onfocus1:focusId1", "active: focusId1"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: body", "onfocus1:focusId1", "active: focusId1",
                "after", "active: focusId1"})
    public void switchFromFocusableToNotFocusable() throws Exception {
        testSwitchWithCallFocusAndBlur("<input type='text' id='focusId1'>\n"
            + "<div id='focusId2'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1",
                "onblur1:focusId1", "active: body", "onfocusout1:focusId1", "active: body",
                "onfocus2:focusId2", "active: focusId2", "onfocusin2:focusId2", "active: focusId2",
                "after", "active: focusId2"},
            IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: focusId1", "onfocusout1:focusId1", "active: focusId2",
                "onfocusin2:focusId2", "active: focusId2",
                "after", "active: focusId2",
                "onfocus1:focusId1", "active: focusId2", "onblur1:focusId1", "active: focusId2",
                "onfocus2:focusId2", "active: focusId2"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: body", "onfocus1:focusId1", "active: focusId1",
                "onfocusout1:focusId1", "active: body", "onfocusin2:focusId2", "active: body",
                "onblur1:focusId1", "active: body", "onfocus2:focusId2", "active: focusId2",
                "after", "active: focusId2"})
    public void switchFromFocusableToFocusable() throws Exception {
        testSwitchWithCallFocusAndBlur("<input type='text' id='focusId1'>\n"
            + "<input type='text' id='focusId2'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1",
                "after", "active: focusId1"},
            IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: focusId1",
                "after", "active: focusId1",
                "onfocus1:focusId1", "active: focusId1"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: body", "onfocus1:focusId1", "active: focusId1",
                "after", "active: focusId1"})
    public void switchFromFocusableToFocusableDisabled() throws Exception {
        testSwitchWithCallFocusAndBlur("<input type='text' id='focusId1'>\n"
            + "<input type='text' disabled id='focusId2'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1",
                "onblur1:focusId1", "active: body", "onfocusout1:focusId1", "active: body",
                "onfocus2:focusId2", "active: focusId2", "onfocusin2:focusId2", "active: focusId2",
                "after", "active: focusId2"},
            IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: focusId1", "onfocusout1:focusId1", "active: focusId2",
                "onfocusin2:focusId2", "active: focusId2",
                "after", "active: focusId2",
                "onfocus1:focusId1", "active: focusId2", "onblur1:focusId1", "active: focusId2",
                "onfocus2:focusId2", "active: focusId2"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: body", "onfocus1:focusId1", "active: focusId1",
                "onfocusout1:focusId1", "active: body", "onfocusin2:focusId2", "active: body",
                "onblur1:focusId1", "active: body", "onfocus2:focusId2", "active: focusId2",
                "after", "active: focusId2"})
    public void switchFromFocusableToFocusableReadonly() throws Exception {
        testSwitchWithCallFocusAndBlur("<input type='text' id='focusId1'>\n"
            + "<input type='text' readonly id='focusId2'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1",
                "after", "active: focusId1"},
            IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: focusId1",
                "after", "active: focusId1",
                "onfocus1:focusId1", "active: focusId1"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: body", "onfocus1:focusId1", "active: focusId1",
                "after", "active: focusId1"})
    public void switchFromFocusableToFocusableNotDisplayed() throws Exception {
        testSwitchWithCallFocusAndBlur("<input type='text' id='focusId1'>\n"
            + "<input type='text' style='display: none;' id='focusId2'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1",
                "after", "active: focusId1"},
            IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: focusId1",
                "after", "active: focusId1",
                "onfocus1:focusId1", "active: focusId1"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: body", "onfocus1:focusId1", "active: focusId1",
                "after", "active: focusId1"})
    public void switchFromFocusableToFocusableNotVisible() throws Exception {
        testSwitchWithCallFocusAndBlur("<input type='text' id='focusId1'>\n"
            + "<input type='text' style='visibility: hidden;' id='focusId2'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "active: body",
                "onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1",
                "after", "active: focusId1"},
            IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: focusId1",
                "after", "active: focusId1",
                "onfocus1:focusId1", "active: focusId1"})
    @HtmlUnitNYI(IE = {"before", "active: body",
                "onfocusin1:focusId1", "active: body", "onfocus1:focusId1", "active: focusId1",
                "after", "active: focusId1"})
    public void switchFromFocusableToFocusableHidden() throws Exception {
        testSwitchWithCallFocusAndBlur("<input type='text' id='focusId1'>\n"
            + "<input type='text' hidden id='focusId2'>");
    }

    private void testSwitchWithCallFocusAndBlur(String snippet) throws Exception {
        snippet = snippet.replaceFirst("id='focusId1'( /)?>", "id='focusId1' " + logEvents("1") + "$1>");
        snippet = snippet.replaceFirst("id='focusId2'( /)?>", "id='focusId2' " + logEvents("2") + "$1>");

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + logger()
            + "      function test() {\n"
            + "        log('before');\n"
            + "        document.getElementById('focusId1').focus();\n"
            + "        document.getElementById('focusId2').focus();\n"
            + "        log('after');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='setTimeout(test, 10)'>\n"
            + snippet
            + "  </body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + (getExpectedAlerts().length > 0 ? ";" : ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "after", "active: body"})
    public void jsClickOnNotFocusable() throws Exception {
        testWithCallClick("<div id='focusId'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "after", "active: body"})
    @HtmlUnitNYI(CHROME = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            EDGE = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            FF = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            FF78 = {"before", "active: body",
                "onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body",
                "after", "active: body"},
            IE = {"before", "active: body",
                "onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body",
                "after", "active: body"})
    // FIXME click for either divs or inputs seems broken... :(
    public void jsClickOnNotFocusableWithTabIndex() throws Exception {
        testWithCallClick("<div tabindex='0' id='focusId'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "active: body", "after", "active: body"})
    public void jsClickOnFocusable() throws Exception {
        testWithCallClick("<input type='text' id='focusId'>");
    }

    private void testWithCallClick(String snippet) throws Exception {
        snippet = snippet.replaceFirst("id='focusId'( /)?>", "id='focusId' " + logEvents("") + "$1>");

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + logger()
            + "      function test() {\n"
            + "        log('before');\n"
            + "        document.getElementById('focusId').click();\n"
            + "        document.getElementById('focusId').click();\n"
            + "        document.getElementById('otherId').click();\n"
            + "        log('after');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='setTimeout(test, 10)'>\n"
            + snippet
            + "    <div id='otherId'>div</div>\n"
            + "  </body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + (getExpectedAlerts().length > 0 ? ";" : ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickOnNotFocusable() throws Exception {
        testWithClick("<div id='focusId'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body"},
            IE = {"onfocusin:focusId", "active: focusId", "onfocus:focusId", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body"})
    public void clickOnNotFocusableWithTabIndex() throws Exception {
        testWithClick("<div tabindex='0' id='focusId'>div</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body"},
            IE = {"onfocusin:focusId", "active: focusId", "onfocus:focusId", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body"})
    public void clickOnFocusable() throws Exception {
        testWithClick("<input type='text' id='focusId'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickOnFocusableDisabled() throws Exception {
        testWithClick("<input type='text' disabled id='focusId'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocus:focusId", "active: focusId", "onfocusin:focusId", "active: focusId",
                "onblur:focusId", "active: body", "onfocusout:focusId", "active: body"},
            IE = {"onfocusin:focusId", "active: focusId", "onfocus:focusId", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body"})
    @HtmlUnitNYI(IE = {"onfocusin:focusId", "active: body", "onfocus:focusId", "active: focusId",
                "onfocusout:focusId", "active: body", "onblur:focusId", "active: body"})
    public void clickOnFocusableReadonly() throws Exception {
        testWithClick("<input type='text' readonly id='focusId'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocusT:textId", "active: textId", "onfocusinT:textId", "active: textId",
                "onblurT:textId", "active: body", "onfocusoutT:textId", "active: body"},
            IE = {"onfocusinT:textId", "active: textId", "onfocusT:textId", "active: textId",
                "onfocusoutT:textId", "active: body", "onblurT:textId", "active: body"})
    @HtmlUnitNYI(IE = {"onfocusinT:textId", "active: body", "onfocusT:textId", "active: textId",
                "onfocusoutT:textId", "active: body", "onblurT:textId", "active: body"})
    public void clickOnLabelFor() throws Exception {
        testWithClick("<label for='textId' id='focusId'>label</label><input type='text' id='textId' "
                + logEvents("T") + ">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocusT:textId", "active: textId", "onfocusinT:textId",
                "active: textId", "onfocusin:textId", "active: textId",
                "onblurT:textId", "active: body", "onfocusoutT:textId",
                "active: body", "onfocusout:textId", "active: body"},
            IE = {"onfocusinT:textId", "active: textId", "onfocusin:textId",
                "active: textId", "onfocusT:textId", "active: textId",
                "onfocusoutT:textId", "active: body", "onfocusout:textId",
                "active: body", "onblurT:textId", "active: body"})
    @HtmlUnitNYI(IE = {"onfocusinT:textId", "active: body", "onfocusin:textId",
                "active: body", "onfocusT:textId", "active: textId",
                "onfocusoutT:textId", "active: body", "onfocusout:textId",
                "active: body", "onblurT:textId", "active: body"})
    public void clickOnLabelNested() throws Exception {
        testWithClick("<label for='textId' id='focusId'>label <input type='text' id='textId' "
                + logEvents("T") + "></label>");
    }

    private void testWithClick(String snippet) throws Exception {
        snippet = snippet.replaceFirst("id='focusId'( /)?>", "id='focusId' " + logEvents("") + "$1>");

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + logger()
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body'>\n"
            + snippet
            + "    <div id='otherId'>div</div>\n"
            + "  </body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("focusId")).click();
        driver.findElement(By.id("otherId")).click();

        assertTitle(driver, String.join(";", getExpectedAlerts()) + (getExpectedAlerts().length > 0 ? ";" : ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1",
                "onblur1:focusId1", "active: body", "onfocusout1:focusId1", "active: body",
                "onfocus2:focusId2", "active: focusId2", "onfocusin2:focusId2", "active: focusId2"},
            IE = {"onfocusin1:focusId1", "active: focusId1", "onfocus1:focusId1", "active: focusId1",
                "onfocusout1:focusId1", "active: focusId2", "onfocusin2:focusId2", "active: focusId2",
                "onblur1:focusId1", "active: focusId2", "onfocus2:focusId2", "active: focusId2"})
    @HtmlUnitNYI(IE = {"onfocusin1:focusId1", "active: body", "onfocus1:focusId1", "active: focusId1",
                "onfocusout1:focusId1", "active: body", "onfocusin2:focusId2", "active: body",
                "onblur1:focusId1", "active: body", "onfocus2:focusId2", "active: focusId2"})
    public void clickFromFocusableToFocusable() throws Exception {
        testSwitchWithClick("<input type='text' id='focusId1'>\n"
            + "<input type='text' id='focusId2'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1",
                "onblur1:focusId1", "active: body", "onfocusout1:focusId1", "active: body"},
            IE = {"onfocusin1:focusId1", "active: focusId1", "onfocus1:focusId1", "active: focusId1",
                "onfocusout1:focusId1", "active: body", "onblur1:focusId1", "active: body"})
    @HtmlUnitNYI(CHROME = {"onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1"},
            EDGE = {"onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1"},
            FF = {"onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1"},
            FF78 = {"onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1"},
            IE = {"onfocusin1:focusId1", "active: body", "onfocus1:focusId1", "active: focusId1"})
    public void clickFromFocusableToFocusableDisabled() throws Exception {
        testSwitchWithClick("<input type='text' id='focusId1'>\n"
            + "<input type='text' disabled id='focusId2'>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onfocus1:focusId1", "active: focusId1", "onfocusin1:focusId1", "active: focusId1",
                "onblur1:focusId1", "active: body", "onfocusout1:focusId1", "active: body",
                "onfocus2:focusId2", "active: focusId2", "onfocusin2:focusId2", "active: focusId2"},
            IE = {"onfocusin1:focusId1", "active: focusId1", "onfocus1:focusId1", "active: focusId1",
                "onfocusout1:focusId1", "active: focusId2", "onfocusin2:focusId2", "active: focusId2",
                "onblur1:focusId1", "active: focusId2", "onfocus2:focusId2", "active: focusId2"})
    @HtmlUnitNYI(IE = {"onfocusin1:focusId1", "active: body", "onfocus1:focusId1", "active: focusId1",
                "onfocusout1:focusId1", "active: body", "onfocusin2:focusId2", "active: body",
                "onblur1:focusId1", "active: body", "onfocus2:focusId2", "active: focusId2"})
    public void clickFromFocusableToFocusableReadonly() throws Exception {
        testSwitchWithClick("<input type='text' id='focusId1'>\n"
            + "<input type='text' readonly id='focusId2'>");
    }

    private void testSwitchWithClick(String snippet) throws Exception {
        snippet = snippet.replaceFirst("id='focusId1'( /)?>", "id='focusId1' " + logEvents("1") + "$1>");
        snippet = snippet.replaceFirst("id='focusId2'( /)?>", "id='focusId2' " + logEvents("2") + "$1>");

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + logger()
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body'>\n"
            + snippet
            + "  </body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("focusId1")).click();
        driver.findElement(By.id("focusId2")).click();

        assertTitle(driver, String.join(";", getExpectedAlerts()) + (getExpectedAlerts().length > 0 ? ";" : ""));
    }

    private String logger() {
        return  "      function log(x, e) {\n"
            + "        document.title += x + (e ? ':' + (e.target.id ? e.target.id : e.target) : '') + ';';\n"
            + "        document.title += 'active: ' "
                        + "+ (document.activeElement ? document.activeElement.id : 'null') + ';';\n"
            + "      }\n";
    }

    private String logEvents(final String aSuffix) {
        return "onblur=\"log('onblur" + aSuffix + "', event)\" "
            + "onfocusin=\"log('onfocusin" + aSuffix + "', event)\" "
            + "onfocusout=\"log('onfocusout" + aSuffix + "', event)\" "
            + "onfocus=\"log('onfocus" + aSuffix + "', event)\"";
    }
}
