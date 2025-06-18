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
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HtmlUrlInput}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Anton Demydenko
 */
public class HtmlUrlInputTest extends WebDriverTestCase {

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
            + "      input.type = 'url';\n"
            + "      log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "    } catch(e)  { logEx(e); }\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"url\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='url' id='text1'>\n"
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
            + "      input.type = 'url';\n"
            + "      input = input.cloneNode(false);\n"
            + "      log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "    } catch(e)  { logEx(e); }\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"url\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='url' id='text1'>\n"
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='url' id='tester' value='http://htmlunit.sourceforge.net'>\n"
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
    @Alerts({"https://htmlunit.org", ""})
    public void clearInput() throws Exception {
        final String htmlContent = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='url' name='tester' id='tester' value='https://htmlunit.org'>\n"
                + "</form>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
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
    public void typing() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='url' id='foo'>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(htmlContent);

        final WebElement input = driver.findElement(By.id("foo"));

        input.sendKeys("hello");
        assertNull(input.getDomAttribute("value"));
        assertEquals("hello", input.getDomProperty("value"));
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
            + "  <input type='url' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"http://example.com",
             "http://example.com",
             "false",
             "false-false-true-false-false-false-false-false-false-false-false",
             "true",
             "§§URL§§", "1"})
    public void patternValidationInvalid() throws Exception {
        validation("<input type='url' pattern='.*test.*' id='e1' value='http://example.com' name='k'>\n",
                    "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"http://test.com",
             "http://test.com",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=http%3A%2F%2Ftest.com", "2"})
    public void patternValidationValid() throws Exception {
        validation("<input type='url' pattern='.*test.*' id='e1' value='http://test.com' name='k'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"",
             "",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=", "2"})
    public void patternValidationEmpty() throws Exception {
        validation("<input type='url' pattern='.*test.*' id='e1' name='k' value=''>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({" ",
             "",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=", "2"})
    public void patternValidationBlank() throws Exception {
        validation("<input type='url' pattern='.*test.*' id='e1' name='k' value=' '>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"  \t",
             "",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=", "2"})
    public void patternValidationWhitespace() throws Exception {
        validation("<input type='url' pattern='.*test.*' id='e1' name='k' value='  \t'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({" http://test.com ",
             "http://test.com",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=http%3A%2F%2Ftest.com", "2"})
    public void patternValidationTrimInitial() throws Exception {
        validation("<input type='url' pattern='.*test.*' id='e1' name='k' value=' http://test.com '>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             " http://test.com ",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=+http%3A%2F%2Ftest.com+", "2"})
    public void patternValidationTrimType() throws Exception {
        validation("<input type='url' pattern='.*test.*' id='e1' name='k'>\n", "", " http://test.com ");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "http://example.com",
             "false",
             "false-false-false-false-false-false-false-true-false-false-false",
             "true",
             "§§URL§§", "1"})
    public void minLengthValidationInvalid() throws Exception {
        validation("<input type='url' minlength='20' id='e1' name='k'>\n", "", "http://example.com");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"http://example.com",
             "http://example.com",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=http%3A%2F%2Fexample.com", "2"})
    public void minLengthValidationInvalidInitial() throws Exception {
        validation("<input type='url' minlength='20' id='e1' name='k' value='http://example.com'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=", "2"})
    public void minLengthValidationInvalidNoInitial() throws Exception {
        validation("<input type='url' minlength='20' id='e1' name='k'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "http://example.com/test",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=http%3A%2F%2Fexample.com%2Ftest", "2"})
    public void minLengthValidationValid() throws Exception {
        validation("<input type='url' minlength='20' id='e1' name='k'>\n", "", "http://example.com/test");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "http://example.com",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=http%3A%2F%2Fexample.com", "2"})
    public void maxLengthValidationValid() throws Exception {
        validation("<input type='url' maxlength='20' id='e1' name='k'>\n", "", "http://example.com");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "http://example.com/t",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=http%3A%2F%2Fexample.com%2Ft", "2"})
    public void maxLengthValidationInvalid() throws Exception {
        validation("<input type='url' maxlength='20' id='e1' name='k'>\n", "", "http://example.com/test");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"http://example.com/test",
             "http://example.com/test",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=http%3A%2F%2Fexample.com%2Ftest", "2"})
    public void maxLengthValidationInvalidInitial() throws Exception {
        validation("<input type='url' maxlength='20' "
                    + "id='e1' name='k' value='http://example.com/test'>\n", "", null);
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
                + "    <input type='url' id='o1'>\n"
                + "    <input type='url' id='o2' disabled>\n"
                + "    <input type='url' id='o3' hidden>\n"
                + "    <input type='url' id='o4' readonly>\n"
                + "    <input type='url' id='o5' style='display: none'>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=", "2"})
    public void validationEmpty() throws Exception {
        validation("<input type='url' id='e1' name='k'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "",
             "false",
             "false-true-false-false-false-false-false-false-false-false-false",
             "true",
             "§§URL§§", "1"})
    public void validationCustomValidity() throws Exception {
        validation("<input type='url' id='e1' name='k'>\n", "elem.setCustomValidity('Invalid');", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "",
             "false",
             "false-true-false-false-false-false-false-false-false-false-false",
             "true",
             "§§URL§§", "1"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<input type='url' id='e1' name='k'>\n", "elem.setCustomValidity(' ');\n", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=", "2"})
    public void validationResetCustomValidity() throws Exception {
        validation("<input type='url' id='e1' name='k'>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "",
             "false",
             "false-false-false-false-false-false-false-false-false-false-true",
             "true",
             "§§URL§§", "1"})
    public void validationRequired() throws Exception {
        validation("<input type='url' id='e1' name='k' required>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=http%3A%2F%2Fexample.com", "2"})
    public void validationRequiredValueSet() throws Exception {
        validation("<input type='url' id='e1' name='k' required>\n", "elem.value='http://example.com';", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "",
             "false",
             "false-false-true-false-false-false-false-false-false-false-false",
             "true",
             "§§URL§§", "1"})
    public void validationPattern() throws Exception {
        validation("<input type='url' id='e1' name='k' pattern='.*test.*'>\n",
                    "elem.value='http://example.com';", null);
    }

    private void validation(final String htmlPart, final String jsPart, final String sendKeys) throws Exception {
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
                + "<body>\n"
                + "  <form>\n"
                + htmlPart
                + "    <button id='myTest' type='button' onclick='test()'>Test</button>\n"
                + "    <button id='myButton' type='submit'>Submit</button>\n"
                + "  </form>\n"
                + "</body></html>";

        final String secondContent = DOCTYPE_HTML
                + "<html><head><title>second</title></head><body>\n"
                + "  <p>hello world</p>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html, URL_FIRST);

        final WebElement foo = driver.findElement(By.id("e1"));
        if (sendKeys != null) {
            foo.sendKeys(sendKeys);
        }
        assertEquals(getExpectedAlerts()[0], "" + foo.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[1], foo.getDomProperty("value"));

        driver.findElement(By.id("myTest")).click();
        verifyTitle2(driver, getExpectedAlerts()[2], getExpectedAlerts()[3], getExpectedAlerts()[4]);

        driver.findElement(By.id("myButton")).click();
        if (useRealBrowser()) {
            Thread.sleep(400);
        }
        assertEquals(getExpectedAlerts()[5], getMockWebConnection().getLastWebRequest().getUrl());
        assertEquals(Integer.parseInt(getExpectedAlerts()[6]), getMockWebConnection().getRequestCount());
    }
}
