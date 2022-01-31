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
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlHiddenInput}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlHiddenInputTest extends WebDriverTestCase {

    /**
     * Verifies that getText() returns an empty string.
     * @throws Exception if the test fails
     */
    @Test
    public void getText() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='hidden' name='foo' id='foo' value='bla'>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(htmlContent);

        final WebElement input = driver.findElement(By.id("foo"));
        assertEquals("", input.getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void setValueOnChange() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "</script>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input type='hidden' id='h' value='Hello world'"
                    + " onChange='log(\"foo\");log(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='document.getElementById(\"h\").value=\"HtmlUnit\"'>setValue</button>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("set")).click();

        assertEquals("", driver.getTitle());

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        assertEquals("", driver.getTitle());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void setDefaultValueOnChange() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "</script>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input type='hidden' id='h' value='Hello world'"
                    + " onChange='log(\"foo\");log(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='document.getElementById(\"h\").defaultValue=\"HtmlUnit\"'>"
                      + "setValue</button>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("set")).click();

        assertEquals("", driver.getTitle());

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        assertEquals("", driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"--null", "--null", "--null"})
    public void defaultValues() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('hidden1');\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'hidden';\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"hidden\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='hidden1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"--null", "--null", "--null"})
    public void defaultValuesAfterClone() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('hidden1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'hidden';\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"hidden\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='hidden1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial-initial", "initial-initial-initial",
                "newValue-newValue-newValue", "newValue-newValue-newValue",
                "newDefault-newDefault-newDefault", "newDefault-newDefault-newDefault"})
    public void resetByClick() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var hidden = document.getElementById('testId');\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.value = 'newValue';\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.defaultValue = 'newDefault';\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='testId' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial-initial", "initial-initial-initial",
                "newValue-newValue-newValue", "newValue-newValue-newValue",
                "newDefault-newDefault-newDefault", "newDefault-newDefault-newDefault"})
    public void resetByJS() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var hidden = document.getElementById('testId');\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.value = 'newValue';\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.defaultValue = 'newDefault';\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial-initial", "default-default-default",
                "newValue-newValue-newValue", "attribValue-attribValue-attribValue",
                "newDefault-newDefault-newDefault"})
    public void value() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var hidden = document.getElementById('testId');\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.defaultValue = 'default';\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.value = 'newValue';\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.setAttribute('value', 'attribValue');\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.defaultValue = 'newDefault';\n"
            + "    log(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "textLength not available",
            FF = "7",
            FF_ESR = "7")
    public void textLength() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    if(text.textLength) {\n"
            + "      log(text.textLength);\n"
            + "    } else {\n"
            + "      log('textLength not available');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='text' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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
            + "  <input type='hidden' id='tester'>\n"
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
                + "    <input type='hidden' id='o1'>\n"
                + "    <input type='hidden' id='o2' disabled>\n"
                + "    <input type='hidden' id='o3' hidden>\n"
                + "    <input type='hidden' id='o4' readonly>\n"
                + "    <input type='hidden' id='o5' style='display: none'>\n"
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
        validation("<input type='hidden' id='e1'>\n", "");
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
        validation("<input type='hidden' id='e1'>\n", "elem.setCustomValidity('Invalid');");
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
        validation("<input type='hidden' id='e1'>\n", "elem.setCustomValidity(' ');\n");
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
        validation("<input type='hidden' id='e1'>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
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
    public void validationRequired() throws Exception {
        validation("<input type='hidden' id='e1' required>\n", "");
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
    public void validationRequiredValueSet() throws Exception {
        validation("<input type='hidden' id='e1' required>\n", "elem.value='secret';");
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
