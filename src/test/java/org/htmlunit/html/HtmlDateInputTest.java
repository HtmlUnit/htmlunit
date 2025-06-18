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
package org.htmlunit.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for {@link HtmlDateInput}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Anton Demydenko
 */
public class HtmlDateInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"--null", "--null", "--null"})
    public void defaultValues() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('text1');\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    try {\n"
            + "      input = document.createElement('input');\n"
            + "      input.type = 'date';\n"
            + "      log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "    } catch(e)  { logEx(e); }\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"date\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='date' id='text1'>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('text1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    try {\n"
            + "      input = document.createElement('input');\n"
            + "      input.type = 'date';\n"
            + "      input = input.cloneNode(false);\n"
            + "      log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "    } catch(e)  { logEx(e); }\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"date\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='date' id='text1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"text-datetime", "date-Date"})
    public void type() throws Exception {
        final String html = DOCTYPE_HTML
              + "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "  function test() {\n"
              + "    var input = document.getElementById('input1');\n"
              + "    log(input.type + '-' + input.getAttribute('type'));\n"
              + "    input = document.getElementById('input2');\n"
              + "    log(input.type + '-' + input.getAttribute('type'));\n"
              + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body onload='test()'>\n"
              + "  <input id='input1' type='datetime'>\n"
              + "  <input id='input2' type='Date'>\n"
              + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2018-03-22")
    @BuggyWebDriver(CHROME = "80322-02-01",
                    EDGE = "80322-02-01")
    public void typeInto() throws Exception {
        final String html = DOCTYPE_HTML
              + "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + "  function test() {\n"
              + "    var input = document.getElementById('input');\n"
              + "    alert(input.value);\n"
              + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input id='input' type='date'>\n"
              + "  <button id='tester' onclick='test()'>Test</button>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("input")).sendKeys("2018-03-22");
        driver.findElement(By.id("tester")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2018-03-22", ""})
    public void clearInput() throws Exception {
        final String html = DOCTYPE_HTML
              + "<html>\n"
              + "<body>\n"
              + "  <input id='input' type='date' value='2018-03-22'>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement input = driver.findElement(By.id("input"));

        assertEquals(getExpectedAlerts()[0], input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[0], input.getDomProperty("value"));

        input.clear();
        assertEquals(getExpectedAlerts()[0], input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[1], input.getDomProperty("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("--")
    public void minMaxStep() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
            + "  <input type='date' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false-true")
    public void minValidation() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var foo = document.getElementById('foo');\n"
            + "    var bar = document.getElementById('bar');\n"
            + "    log(foo.checkValidity() + '-' + bar.checkValidity() );\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='date' min='2018-12-01' id='foo' value='2018-11-01'>\n"
            + "  <input type='date' min='2018-12-01' id='bar' value='2018-12-02'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false-true")
    public void maxValidation() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var foo = document.getElementById('foo');\n"
            + "    var bar = document.getElementById('bar');\n"
            + "    log(foo.checkValidity() + '-' + bar.checkValidity() );\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='date' max='2018-12-01' id='foo' value='2018-12-11'>\n"
            + "  <input type='date' max='2018-12-01' id='bar' value='2018-11-01'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidate() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
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
                + "    <input type='date' id='o1'>\n"
                + "    <input type='date' id='o2' disabled>\n"
                + "    <input type='date' id='o3' hidden>\n"
                + "    <input type='date' id='o4' readonly>\n"
                + "    <input type='date' id='o5' style='display: none'>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationEmpty() throws Exception {
        validation("<input type='date' id='e1'>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-true-false-false-false-false-false-false-false-false-false",
             "true"})
    public void validationCustomValidity() throws Exception {
        validation("<input type='date' id='e1'>\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-true-false-false-false-false-false-false-false-false-false",
             "true"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<input type='date' id='e1'>\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationResetCustomValidity() throws Exception {
        validation("<input type='date' id='e1'>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-false-false-false-false-false-false-false-false-false-true",
             "true"})
    public void validationRequired() throws Exception {
        validation("<input type='date' id='e1' required>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequiredValueSet() throws Exception {
        validation("<input type='date' id='e1' required>\n", "elem.value='2018-12-01';");
    }

    private void validation(final String htmlPart, final String jsPart) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
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
