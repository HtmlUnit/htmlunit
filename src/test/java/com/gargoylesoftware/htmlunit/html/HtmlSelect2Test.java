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
package com.gargoylesoftware.htmlunit.html;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.BuggyWebDriver;

/**
 * Tests for {@link HtmlSelect}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlSelect2Test extends WebDriverTestCase {

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts({"false", "false", "false", "true"})
    @BuggyWebDriver(IE = {"false", "false", "true", "false"})
    public void select() throws Exception {
        final String html = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1' multiple>\n"
            + "  <option value='option1'>Option1</option>\n"
            + "  <option value='option2'>Option2</option>\n"
            + "  <option value='option3' selected='selected'>Option3</option>\n"
            + "  <option value='option4'>Option4</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(html);

        final List<WebElement> options = driver.findElements(By.tagName("option"));

        final Actions actions = new Actions(driver);
        final Action selectThreeOptions = actions.click(options.get(1))
            .click(options.get(2))
            .click(options.get(3))
            .build();

        selectThreeOptions.perform();

        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[0]), options.get(0).isSelected());
        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[1]), options.get(1).isSelected());
        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[2]), options.get(2).isSelected());
        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[3]), options.get(3).isSelected());
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts({"false", "true", "true", "true"})
    @BuggyWebDriver(IE = {"false", "false", "true", "false"})
    public void shiftClick() throws Exception {
        final String html = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1' multiple>\n"
            + "  <option value='option1'>Option1</option>\n"
            + "  <option value='option2'>Option2</option>\n"
            + "  <option value='option3' selected='selected'>Option3</option>\n"
            + "  <option value='option4'>Option4</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(html);

        final List<WebElement> options = driver.findElements(By.tagName("option"));

        final Actions actions = new Actions(driver);
        final Action selectThreeOptions = actions.click(options.get(1))
                .keyDown(Keys.SHIFT)
                .click(options.get(3))
                .keyUp(Keys.SHIFT)
                .build();

        selectThreeOptions.perform();

        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[0]), options.get(0).isSelected());
        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[1]), options.get(1).isSelected());
        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[2]), options.get(2).isSelected());
        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[3]), options.get(3).isSelected());
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts({"false", "true", "false", "true"})
    @BuggyWebDriver(IE = {"false", "false", "true", "false"})
    public void controlClick() throws Exception {
        final String html = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1' multiple>\n"
            + "  <option value='option1'>Option1</option>\n"
            + "  <option value='option2'>Option2</option>\n"
            + "  <option value='option3' selected='selected'>Option3</option>\n"
            + "  <option value='option4'>Option4</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(html);

        final List<WebElement> options = driver.findElements(By.tagName("option"));

        final Actions actions = new Actions(driver);
        final Action selectThreeOptions = actions.click(options.get(1))
                .keyDown(Keys.CONTROL)
                .click(options.get(3))
                .keyUp(Keys.CONTROL)
                .build();

        selectThreeOptions.perform();

        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[0]), options.get(0).isSelected());
        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[1]), options.get(1).isSelected());
        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[2]), options.get(2).isSelected());
        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[3]), options.get(3).isSelected());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "false", "true", "false", "true"},
            FF = {"true", "false", "true", "true", "true"},
            FF_ESR = {"true", "false", "true", "true", "true"},
            IE = {"true", "true", "true", "true", "true"})
    public void willValidate() throws Exception {
        final String html =
                "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('s1').willValidate);\n"
                + "      log(document.getElementById('s2').willValidate);\n"
                + "      log(document.getElementById('s3').willValidate);\n"
                + "      log(document.getElementById('s4').willValidate);\n"
                + "      log(document.getElementById('s5').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <select id='s1'>s1</select>\n"
                + "    <select id='s2' disabled>s2</select>\n"
                + "    <select id='s3' hidden>s3</select>\n"
                + "    <select id='s4' readonly>s4</select>\n"
                + "    <select id='s5' style='display: none'>s5</select>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true"})
    public void validationEmpty() throws Exception {
        validation("<select id='e1'>s1</select>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "true"},
            IE = {"false",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "true"})
    public void validationCustomValidity() throws Exception {
        validation("<select id='e1'>s1</select>\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "true"},
            IE = {"false",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "true"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<select id='e1'>s1</select>\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true"})
    public void validationResetCustomValidity() throws Exception {
        validation("<select id='e1'>s1</select>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-false-false-false-false-false-false-false-false-false-true",
                       "true"},
            IE = {"false",
                  "undefined-false-false-false-false-false-false-undefined-false-false-true",
                  "true"})
    public void validationRequired() throws Exception {
        validation("<select id='e1' required>s1</select>\n", "");
    }

    private void validation(final String htmlPart, final String jsPart) throws Exception {
        final String html =
                "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function logValidityState(s) {\n"
                + "      log(s.badInput"
                        + "+ '-' + s.customError"
                        + "+ '-' + s.patternMismatch"
                        + "+ '-' + s.rangeOverflow"
                        + "+ '-' + s.rangeUnderflow"
                        + "+ '-' + s.stepMismatch"
                        + "+ '-' + s.tooLong"
                        + "+ '-' + s.tooShort"
                        + " + '-' + s.typeMismatch"
                        + " + '-' + s.valid"
                        + " + '-' + s.valueMissing);\n"
                + "    }\n"
                + "    function test() {\n"
                + "      var elem = document.getElementById('e1');\n"
                + jsPart
                + "      log(elem.checkValidity());\n"
                + "      logValidityState(elem.validity);\n"
                + "      log(elem.willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + htmlPart
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
