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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlButtonInput}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlButtonInput2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-", "-", "-"})
    public void defaultValues() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('button1');\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'button';\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"button\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='button' id='button1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-", "-", "-"})
    public void defaultValuesAfterClone() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('button1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'button';\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"button\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='button' id='button1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial", "initial-initial", "newValue-newValue", "newValue-newValue",
                "newDefault-newDefault", "newDefault-newDefault"})
    public void resetByClick() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    button.value = 'newValue';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    button.defaultValue = 'newDefault';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(button.value + '-' + button.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='button' id='testId' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial", "initial-initial", "newValue-newValue", "newValue-newValue",
                "newDefault-newDefault", "newDefault-newDefault"})
    public void resetByJS() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    button.value = 'newValue';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    button.defaultValue = 'newDefault';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(button.value + '-' + button.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='button' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial", "default-default", "newValue-newValue", "newDefault-newDefault"})
    public void defaultValue() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    button.defaultValue = 'default';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    button.value = 'newValue';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"
            + "    button.defaultValue = 'newDefault';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='button' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void click_onClick() throws Exception {
        final String html = "<html>\n"
                + "<head>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "<form id='form1' onSubmit='log(\"bar\")'>\n"
                + "  <input type='button' name='button' id='button' onClick='log(\"foo\")'>Push me</button>\n"
                + "</form>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("button")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void click_onClickIgnoreCase() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='button' name='button' id='button' oNclICK='log(\"foo\")'>Push me</button>\n"
                + "</form>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("button")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("--")
    public void minMaxStep() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('tester');\n"
            + "    log(input.min + '-' + input.max + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='button' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "false", "false", "false"})
    public void willValidate() throws Exception {
        final String html =
                "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('o1').willValidate);\n"
                + "      log(document.getElementById('o2').willValidate);\n"
                + "      log(document.getElementById('o3').willValidate);\n"
                + "      log(document.getElementById('o4').willValidate);\n"
                + "      log(document.getElementById('o5').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <input type='button' id='o1'>\n"
                + "    <input type='button' id='o2' disabled>\n"
                + "    <input type='button' id='o3' hidden>\n"
                + "    <input type='button' id='o4' readonly>\n"
                + "    <input type='button' id='o5' style='display: none'>\n"
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
                       "false"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "false"})
    public void validationEmpty() throws Exception {
        validation("<input type='button' id='e1'>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "false"},
            IE = {"true",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "false"})
    public void validationCustomValidity() throws Exception {
        validation("<input type='button' id='e1'>\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "false"},
            IE = {"true",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "false"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<input type='button' id='e1'>\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "false"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "false"})
    public void validationResetCustomValidity() throws Exception {
        validation("<input type='button' id='e1'>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
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
