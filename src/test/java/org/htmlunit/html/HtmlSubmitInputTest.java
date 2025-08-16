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

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.htmlunit.MockWebConnection;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for {@link HtmlSubmitInput}.
 *
 * @author Mike Bowler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlSubmitInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post'>\n"
            + "<input type='submit' name='aButton' value='foo'/>\n"
            + "<input type='suBMit' name='button' value='foo'/>\n"
            + "<input type='submit' name='anotherButton' value='foo'/>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement button = driver.findElement(By.name("button"));
        button.click();

        assertTitle(driver, "foo");

        assertEquals(Collections.singletonList(new NameValuePair("button", "foo")),
            getMockWebConnection().getLastParameters());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "bar"})
    public void click_onClick() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head><body>\n"
            + "<form id='form1' onSubmit='log(\"bar\"); return false;'>\n"
            + "  <input type='submit' name='button' value='foo' onClick='log(\"foo\")'/>\n"
            + "</form></body></html>";

        final WebDriver webDriver = loadPage2(html);

        final WebElement button = webDriver.findElement(By.name("button"));
        button.click();

        verifyTitle2(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click_onClick_JavascriptReturnsTrue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<form name='form1' method='get' action='foo.html'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button1'"
            + "onclick='return true'/></form>\n"
            + "</body></html>";
        final String secondHtml = DOCTYPE_HTML + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "foo.html"), secondHtml);

        final WebDriver driver = loadPage2(html);

        final WebElement button = driver.findElement(By.id("button1"));
        button.click();

        assertTitle(driver, "Second");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void outsideForm() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<input id='myInput' type='submit' onclick='log(1)'>\n"
            + "</body></html>";

        final WebDriver webDriver = loadPage2(html);
        final WebElement input = webDriver.findElement(By.id("myInput"));
        input.click();

        verifyTitle2(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void onclickDisables() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script type='text/javascript'>\n"
            + "    function submitForm() {\n"
            + "      document.deliveryChannelForm.submitBtn.disabled = true;\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form action='test' name='deliveryChannelForm'>\n"
            + "    <input name='submitBtn' type='submit' value='Save' title='Save' onclick='submitForm();'>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver webDriver = loadPage2(html);
        final WebElement input = webDriver.findElement(By.name("submitBtn"));
        input.click();

        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void doubleSubmission() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script type='text/javascript'>\n"
            + "    function submitForm() {\n"
            + "      document.deliveryChannelForm.submitBtn.disabled = true;\n"
            + "      document.deliveryChannelForm.submit();\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form action='test' name='deliveryChannelForm'>\n"
            + "    <input name='submitBtn' type='submit' value='Save' title='Save' onclick='submitForm();'>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver webDriver = loadPage2(html);
        final WebElement input = webDriver.findElement(By.name("submitBtn"));
        input.click();

        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void doubleSubmissionWithRedirect() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + "function submitForm(btn) {\n"
            + "  btn.form.submit();\n"
            + "  btn.disabled = true;\n"
            + "}\n"
            + "</script>\n"
            + "<form method='post' action='test'>\n"
            + "  <input name='text' type='text'>\n"
            + "  <input name='btn' type='submit' onclick='submitForm(this);'>\n"
            + "</form>\n"
            + "</body></html>";

        final MockWebConnection mockWebConnection = getMockWebConnection();

        final List<NameValuePair> redirectHeaders = Arrays.asList(new NameValuePair("Location", "/nextPage"));
        mockWebConnection.setResponse(new URL(URL_FIRST, "test"), "", 302, "Found", null, redirectHeaders);

        mockWebConnection.setResponse(new URL(URL_FIRST, "nextPage"),
                DOCTYPE_HTML + "<html><head><title>next page</title></head></html>");

        final WebDriver driver = loadPage2(html);
        final WebElement input = driver.findElement(By.name("btn"));
        input.click();

        assertTitle(driver, "next page");
        assertEquals(3, mockWebConnection.getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-", "-", "-"})
    public void defaultValues() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('submit1');\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'submit';\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"submit\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='submit' id='submit1'>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('submit1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'submit';\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"submit\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='submit' id='submit1'>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var submit = document.getElementById('testId');\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"

            + "    submit.value = 'newValue';\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"

            + "    submit.defaultValue = 'newDefault';\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='submit' id='testId' value='initial'>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var submit = document.getElementById('testId');\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"

            + "    submit.value = 'newValue';\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"

            + "    submit.defaultValue = 'newDefault';\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='submit' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial", "default-default", "newValue-newValue", "newdefault-newdefault"})
    public void defaultValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var submit = document.getElementById('testId');\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"

            + "    submit.defaultValue = 'default';\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"

            + "    submit.value = 'newValue';\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"
            + "    submit.defaultValue = 'newdefault';\n"
            + "    log(submit.value + '-' + submit.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='submit' id='testId' value='initial'>\n"
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
            + "  <input type='submit' id='tester'>\n"
            + "</form>\n"
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
                + "    <input type='submit' id='o1'>\n"
                + "    <input type='submit' id='o2' disabled>\n"
                + "    <input type='submit' id='o3' hidden>\n"
                + "    <input type='submit' id='o4' readonly>\n"
                + "    <input type='submit' id='o5' style='display: none'>\n"
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
        validation("<input type='submit' id='e1'>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-true-false-false-false-false-false-false-false-false-false",
             "true"})
    public void validationCustomValidity() throws Exception {
        validation("<input type='submit' id='e1'>\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-true-false-false-false-false-false-false-false-false-false",
             "true"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<input type='submit' id='e1'>\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationResetCustomValidity() throws Exception {
        validation("<input type='submit' id='e1'>\n",
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
        validation("<input type='submit' id='e1' required>\n", "");
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
