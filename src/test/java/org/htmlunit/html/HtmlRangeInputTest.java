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
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for {@link HtmlRangeInput}.
 *
 * @author Ronald Brill
 * @author Anton Demydenko
 */
public class HtmlRangeInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"50----", "50----", "50----"})
    public void defaultValues() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('range1');\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'range';\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"range\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='range1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"50----", "50----", "50----"})
    public void defaultValuesAfterClone() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('range1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'range';\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"range\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='range1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7-7---", "7-7---", "4-7---", "4-7---", "4-2---", "4-2---"})
    public void resetByClick() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input.value = '4';\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input.defaultValue = '2';\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    document.forms[0].reset;\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='testId' value='7'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7-7---", "7-7---", "4-7---", "4-7---", "4-2---", "4-2---"})
    public void resetByJS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    document.forms[0].reset;\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input.value = '4';\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    document.forms[0].reset;\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input.defaultValue = '2';\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    document.forms[0].reset;\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='testId' value='7'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7-7---", "4-4---", "2-4---", "2-8---"})
    public void defaultValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input.defaultValue = '4';\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input.value = '2';\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input.defaultValue = '8';\n"
            + "    log(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='testId' value='7'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"50----", "50--100-0-", "5--10-0-", "4--7-0-",
                "2--7--4-", "4.3--7.01-1.3-"})
    public void valueDependsOnMinMax() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    for (i = 1; i <= 6; i++) {\n"
            + "      var input = document.getElementById('testId' + i);\n"
            + "      log(input.value + '-' + input.defaultValue"
                        + " + '-' + input.max + '-' + input.min"
                        + " + '-' + input.step);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='testId1'>\n"
            + "  <input type='range' id='testId2' min='0' max='100'>\n"
            + "  <input type='range' id='testId3' min='0' max='10'>\n"
            + "  <input type='range' id='testId4' min='0' max='7'>\n"
            + "  <input type='range' id='testId5' min='-4' max='7'>\n"
            + "  <input type='range' id='testId6' min='1.3' max='7.01'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"41-42-1234-2-13", "5-5-10-2-1", "6-5-10-2-2"})
    public void properties() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    for (i = 1; i <= 3; i++) {\n"
            + "      var input = document.getElementById('testId' + i);\n"
            + "      log(input.value + '-' + input.defaultValue"
                        + " + '-' + input.max + '-' + input.min"
                        + " + '-' + input.step);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='testId1'"
                        + " min='2' max='1234' value='42' step='13'>\n"
            + "  <input type='range' id='testId2'"
                        + " min='2' max='10' value='5' step='1'>\n"
            + "  <input type='range' id='testId3'"
                        + " min='2' max='10' value='5' step='2'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"42", "50"})
    public void clearInput() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<form>\n"
            + "  <input type='range' id='tester' value='42'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement element = driver.findElement(By.id("tester"));

        assertEquals(getExpectedAlerts()[0], element.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[0], element.getDomProperty("value"));

        element.clear();
        assertEquals(getExpectedAlerts()[0], element.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[1], element.getDomProperty("value"));
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
            + "  <input type='range' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true-true-true-true-true-true", "55-10-10-100-0-0"})
    public void minValidation() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('id1').checkValidity() + '-'\n"
            + "         + document.getElementById('id2').checkValidity() + '-'\n"
            + "         + document.getElementById('id3').checkValidity() + '-'\n"
            + "         + document.getElementById('id4').checkValidity() + '-'\n"
            + "         + document.getElementById('id5').checkValidity() + '-'\n"
            + "         + document.getElementById('id6').checkValidity());\n"
            + "    log(document.getElementById('id1').value + '-'\n"
            + "         + document.getElementById('id2').value + '-'\n"
            + "         + document.getElementById('id3').value + '-'\n"
            + "         + document.getElementById('id4').value + '-'\n"
            + "         + document.getElementById('id5').value + '-'\n"
            + "         + document.getElementById('id6').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='range' id='id1' min='10'>\n"
            + "  <input type='range' id='id2' min='10' value='1'>\n"
            + "  <input type='range' id='id3' min='10' value='10'>\n"
            + "  <input type='range' id='id4' min='10' value='100'>\n"
            + "  <input type='range' id='id5' value='0'>\n"
            + "  <input type='range' id='id6' min='foo' value='0'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true-true-true-true-true-true", "5-1-10-10-0-0"})
    public void maxValidation() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('id1').checkValidity() + '-'\n"
            + "         + document.getElementById('id2').checkValidity() + '-'\n"
            + "         + document.getElementById('id3').checkValidity() + '-'\n"
            + "         + document.getElementById('id4').checkValidity() + '-'\n"
            + "         + document.getElementById('id5').checkValidity() + '-'\n"
            + "         + document.getElementById('id6').checkValidity());\n"
            + "    log(document.getElementById('id1').value + '-'\n"
            + "         + document.getElementById('id2').value + '-'\n"
            + "         + document.getElementById('id3').value + '-'\n"
            + "         + document.getElementById('id4').value + '-'\n"
            + "         + document.getElementById('id5').value + '-'\n"
            + "         + document.getElementById('id6').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<body onload='test()'>\n"
            + "  <input type='range' id='id1' max='10'>\n"
            + "  <input type='range' id='id2' max='10' value='1'>\n"
            + "  <input type='range' id='id3' max='10' value='10'>\n"
            + "  <input type='range' id='id4' max='10' value='100'>\n"
            + "  <input type='range' id='id5' value='0'>\n"
            + "  <input type='range' id='id6' max='foo' value='0'>\n"
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
                + "    <input type='range' id='o1'>\n"
                + "    <input type='range' id='o2' disabled>\n"
                + "    <input type='range' id='o3' hidden>\n"
                + "    <input type='range' id='o4' readonly>\n"
                + "    <input type='range' id='o5' style='display: none'>\n"
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
        validation("<input type='range' id='e1'>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-true-false-false-false-false-false-false-false-false-false",
             "true"})
    public void validationCustomValidity() throws Exception {
        validation("<input type='range' id='e1'>\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-true-false-false-false-false-false-false-false-false-false",
             "true"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<input type='range' id='e1'>\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationResetCustomValidity() throws Exception {
        validation("<input type='range' id='e1'>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequired() throws Exception {
        validation("<input type='range' id='e1' required>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequiredValueSet() throws Exception {
        validation("<input type='range' id='e1' required>\n", "elem.value='7';");
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
