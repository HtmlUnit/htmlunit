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
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests for {@link HtmlEmailInput}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Anton Demydenko
 */
@RunWith(BrowserRunner.class)
public class HtmlEmailInputTest extends WebDriverTestCase {

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
            + "    var input = document.getElementById('text1');\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    try {\n"
            + "      input = document.createElement('input');\n"
            + "      input.type = 'email';\n"
            + "      log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "    } catch(e)  { log('exception'); }\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"email\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='email' id='text1'>\n"
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
            + "    var input = document.getElementById('text1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    try {\n"
            + "      input = document.createElement('input');\n"
            + "      input.type = 'email';\n"
            + "      input = input.cloneNode(false);\n"
            + "      log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "    } catch(e)  { log('exception'); }\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"email\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='email' id='text1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleText() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='email' name='tester' id='tester' value='dave@aol.com' >\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBody().getVisibleText());
        }
    }

    /**
     * Verifies clear().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void clearInput() throws Exception {
        final String htmlContent
                = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='email' name='tester' id='tester' value='dave@aol.com'>\n"
                + "</form>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final WebElement element = driver.findElement(By.id("tester"));

        element.clear();
        assertEquals(getExpectedAlerts()[0], element.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typing() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='email' id='foo'>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(htmlContent);

        final WebElement input = driver.findElement(By.id("foo"));
        input.sendKeys("hello");
        assertEquals("hello", input.getAttribute("value"));
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
            + "  <input type='email' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"abc@eemail.com",
                       "false",
                       "false-false-true-false-false-false-false-false-false-false-false",
                       "true",
                       "§§URL§§", "1"},
            IE = {"abc@eemail.com",
                  "false",
                  "undefined-false-true-false-false-false-false-undefined-false-false-false",
                  "true",
                  "§§URL§§", "1"})
    public void patternValidationInvalid() throws Exception {
        validation("<input type='email' pattern='.+@email.com' id='e1' name='k' value='abc@eemail.com'>\n",
                    "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"abc@email.com",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=abc%40email.com", "2"},
            IE = {"abc@email.com",
                  "true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true",
                  "§§URL§§?k=abc@email.com", "2"})
    @HtmlUnitNYI(IE = {"abc@email.com",
                       "true",
                       "undefined-false-false-false-false-false-false-undefined-false-true-false",
                       "true",
                       "§§URL§§?k=abc%40email.com", "2"})
    public void patternValidationValid() throws Exception {
        validation("<input type='email' pattern='.+@email.com' "
                + "id='e1' name='k' value='abc@email.com'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=", "2"},
            IE = {"",
                  "true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true",
                  "§§URL§§?k=", "2"})
    public void patternValidationEmpty() throws Exception {
        validation("<input type='email' pattern='.+@email.com' id='e1' name='k' value=''>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=", "2"},
            IE = {" ",
                  "false",
                  "undefined-false-true-false-false-false-false-undefined-true-false-false",
                  "true",
                  "§§URL§§", "1"})
    @HtmlUnitNYI(IE = {" ",
                       "true",
                       "undefined-false-false-false-false-false-false-undefined-false-true-false",
                       "true",
                       "§§URL§§?k=+", "2"})
    public void patternValidationBlank() throws Exception {
        validation("<input type='email' pattern='.+@email.com' id='e1' name='k' value=' '>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=", "2"},
            IE = {"  \t",
                  "false",
                  "undefined-false-true-false-false-false-false-undefined-true-false-false",
                  "true",
                  "§§URL§§", "1"})
    @HtmlUnitNYI(IE = {"  \t",
                       "true",
                       "undefined-false-false-false-false-false-false-undefined-false-true-false",
                       "true",
                       "§§URL§§?k=++%09", "2"})
    public void patternValidationWhitespace() throws Exception {
        validation("<input type='email' pattern='.+@email.com' id='e1' name='k' value='  \t'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"abc@email.com",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=abc%40email.com", "2"},
            IE = {"",
                  "false",
                  "undefined-false-true-false-false-false-false-undefined-true-false-false",
                  "true",
                  "§§URL§§", "1"})
    @HtmlUnitNYI(IE = {" abc@email.com ",
                       "false",
                       "undefined-false-true-false-false-false-false-undefined-false-false-false",
                       "true",
                       "§§URL§§", "1"})
    public void patternValidationTrimInitial() throws Exception {
        validation("<input type='email' pattern='.+@email.com' id='e1' name='k' value=' abc@email.com '>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"abc@email.com",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=abc%40email.com", "2"},
            FF = {" abc@email.com",
                  "false",
                  "false-false-false-false-false-false-false-false-true-false-false",
                  "true",
                  "§§URL§§", "1"},
            FF_ESR = {" abc@email.com",
                      "true",
                      "false-false-false-false-false-false-false-false-false-true-false",
                      "true",
                      "§§URL§§?k=abc%40email.com", "2"},
            IE = {"  \t",
                  "false",
                  "undefined-false-true-false-false-false-false-undefined-true-false-false",
                  "true",
                  "§§URL§§", "1"})
    @HtmlUnitNYI(FF = {"abc@email.com",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=abc%40email.com", "2"},
                 FF_ESR = {"abc@email.com",
                           "true",
                           "false-false-false-false-false-false-false-false-false-true-false",
                           "true",
                           "§§URL§§?k=abc%40email.com", "2"},
                 IE = {" abc@email.com",
                       "false",
                       "undefined-false-false-false-false-false-false-undefined-false-true-false",
                       "true",
                       "§§URL§§", "1"})
    public void patternValidationTrimType() throws Exception {
        validation("<input type='email' pattern='.+@email.com' id='e1' name='k'>\n", "", " abc@email.com");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"a@b.com",
                       "false",
                       "false-false-false-false-false-false-false-true-false-false-false",
                       "true",
                       "§§URL§§", "1"},
            IE = {"a@b.com",
                  "true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true",
                  "§§URL§§?k=a@b.com", "2"})
    @HtmlUnitNYI(IE = {"a@b.com",
                       "true",
                       "undefined-false-false-false-false-false-false-undefined-false-true-false",
                       "true",
                       "§§URL§§?k=a%40b.com", "2"})
    public void minLengthValidationInvalid() throws Exception {
        validation("<input type='email' minlength='10' id='e1' name='k'>\n", "", "a@b.com");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"a@b.com",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=a%40b.com", "2"},
            IE = {"a@b.com",
                  "true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true",
                  "§§URL§§?k=a@b.com", "2"})
    @HtmlUnitNYI(IE = {"a@b.com",
                       "true",
                       "undefined-false-false-false-false-false-false-undefined-false-true-false",
                       "true",
                       "§§URL§§?k=a%40b.com", "2"})
    public void minLengthValidationInvalidInitial() throws Exception {
        validation("<input type='email' minlength='10' id='e1' name='k' value='a@b.com'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=", "2"},
            IE = {"",
                  "true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true",
                  "§§URL§§?k=", "2"})
    public void minLengthValidationInvalidNoInitial() throws Exception {
        validation("<input type='email' minlength='10' id='e1' name='k'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"a@b.com",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=a%40b.com", "2"},
            IE = {"a@b.com",
                  "true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true",
                  "§§URL§§?k=a@b.com", "2"})
    @HtmlUnitNYI(IE = {"a@b.com",
                       "true",
                       "undefined-false-false-false-false-false-false-undefined-false-true-false",
                       "true",
                       "§§URL§§?k=a%40b.com", "2"})
    public void minLengthValidationValid() throws Exception {
        validation("<input type='email' minlength='5' id='e1' name='k'>\n", "", "a@b.com");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"a@b.c",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=a%40b.c", "2"},
            IE = {"a@b.c",
                  "true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true",
                  "§§URL§§?k=a@b.c", "2"})
    @HtmlUnitNYI(IE = {"a@b.c",
                       "true",
                       "undefined-false-false-false-false-false-false-undefined-false-true-false",
                       "true",
                       "§§URL§§?k=a%40b.c", "2"})
    public void maxLengthValidationValid() throws Exception {
        validation("<input type='email' maxlength='5' id='e1' name='k'>\n", "", "a@b.com");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"a@ema",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=a%40ema", "2"},
            IE = {"a@ema",
                  "true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true",
                  "§§URL§§?k=a@ema", "2"})
    @HtmlUnitNYI(IE = {"a@ema",
                       "true",
                       "undefined-false-false-false-false-false-false-undefined-false-true-false",
                       "true",
                       "§§URL§§?k=a%40ema", "2"})
    public void maxLengthValidationInvalid() throws Exception {
        validation("<input type='email' maxlength='5' id='e1' name='k'>\n", "", "a@email.com");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"a@email.com",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=a%40email.com", "2"},
            IE = {"a@email.com",
                  "true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true",
                  "§§URL§§?k=a@email.com", "2"})
    @HtmlUnitNYI(IE = {"a@email.com",
                       "true",
                       "undefined-false-false-false-false-false-false-undefined-false-true-false",
                       "true",
                       "§§URL§§?k=a%40email.com", "2"})
    public void maxLengthValidationInvalidInitial() throws Exception {
        validation("<input type='email' maxlength='5' id='e1' name='k' value='a@email.com'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
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
                + "    <input type='email' id='o1'>\n"
                + "    <input type='email' id='o2' disabled>\n"
                + "    <input type='email' id='o3' hidden>\n"
                + "    <input type='email' id='o4' readonly>\n"
                + "    <input type='email' id='o5' style='display: none'>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=", "2"},
            IE = {"",
                  "true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true",
                  "§§URL§§?k=", "2"})
    public void validationEmpty() throws Exception {
        validation("<input type='email' id='e1' name='k'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"",
                       "false",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "true",
                       "§§URL§§", "1"},
            IE = {"",
                  "false",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "true",
                  "§§URL§§", "1"})
    public void validationCustomValidity() throws Exception {
        validation("<input type='email' id='e1' name='k'>\n", "elem.setCustomValidity('Invalid');", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"",
                       "false",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "true",
                       "§§URL§§", "1"},
            IE = {"",
                  "false",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "true",
                  "§§URL§§", "1"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<input type='email' id='e1' name='k'>\n", "elem.setCustomValidity(' ');\n", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=", "2"},
            IE = {"",
                  "true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true",
                  "§§URL§§?k=", "2"})
    public void validationResetCustomValidity() throws Exception {
        validation("<input type='email' id='e1' name='k'>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"",
                       "false",
                       "false-false-false-false-false-false-false-false-false-false-true",
                       "true",
                       "§§URL§§", "1"},
            IE = {"",
                  "false",
                  "undefined-false-false-false-false-false-false-undefined-false-false-true",
                  "true",
                  "§§URL§§", "1"})
    public void validationRequired() throws Exception {
        validation("<input type='email' id='e1' name='k' required>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=test%40abc.edu", "2"},
            IE = {"",
                  "true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true",
                  "§§URL§§?k=test@abc.edu", "2"})
    @HtmlUnitNYI(IE = {"",
                       "true",
                       "undefined-false-false-false-false-false-false-undefined-false-true-false",
                       "true",
                       "§§URL§§?k=test%40abc.edu", "2"})
    public void validationRequiredValueSet() throws Exception {
        validation("<input type='email' id='e1' name='k' required>\n", "elem.value='test@abc.edu';", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"",
                       "false",
                       "false-false-true-false-false-false-false-false-false-false-false",
                       "true",
                       "§§URL§§", "1"},
            IE = {"",
                  "false",
                  "undefined-false-true-false-false-false-false-undefined-false-false-false",
                  "true",
                  "§§URL§§", "1"})
    public void validationPattern() throws Exception {
        validation("<input type='email' id='e1' name='k' pattern='.+@email.com'>\n",
                    "elem.value='abc@eemail.com';", null);
    }

    private void validation(final String htmlPart, final String jsPart, final String sendKeys) throws Exception {
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
                + "<body>\n"
                + "  <form>\n"
                + htmlPart
                + "    <button id='myTest' type='button' onclick='test()'>Test</button>\n"
                + "    <button id='myButton' type='submit'>Submit</button>\n"
                + "  </form>\n"
                + "</body></html>";

        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
                + "  <p>hello world</p>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html, URL_FIRST);

        final WebElement foo = driver.findElement(By.id("e1"));
        if (sendKeys != null) {
            foo.sendKeys(sendKeys);
        }
        assertEquals(getExpectedAlerts()[0], foo.getAttribute("value"));

        driver.findElement(By.id("myTest")).click();
        verifyTitle2(driver, getExpectedAlerts()[1], getExpectedAlerts()[2], getExpectedAlerts()[3]);

        driver.findElement(By.id("myButton")).click();
        assertEquals(getExpectedAlerts()[4], getMockWebConnection().getLastWebRequest().getUrl());
        assertEquals(Integer.parseInt(getExpectedAlerts()[5]), getMockWebConnection().getRequestCount());
    }
}
