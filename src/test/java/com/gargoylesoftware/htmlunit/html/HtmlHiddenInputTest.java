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

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

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
     * Verifies that a asText() returns an empty string.
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
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
              + "<head></head>\n"
              + "<body>\n"
              + "  <input type='hidden' id='h' value='Hello world'"
                    + " onChange='alert(\"foo\");alert(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='document.getElementById(\"h\").value=\"HtmlUnit\"'>setValue</button>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("set")).click();

        assertEquals(Collections.emptyList(), getCollectedAlerts(driver));

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        assertEquals(Collections.emptyList(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void setDefaultValueOnChange() throws Exception {
        final String html =
              "<html>\n"
              + "<head></head>\n"
              + "<body>\n"
              + "  <input type='hidden' id='h' value='Hello world'"
                    + " onChange='alert(\"foo\");alert(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='document.getElementById(\"h\").defaultValue=\"HtmlUnit\"'>"
                      + "setValue</button>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("set")).click();

        assertEquals(Collections.emptyList(), getCollectedAlerts(driver));

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        assertEquals(Collections.emptyList(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"--null", "--null", "--null"})
    public void defaultValues() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('hidden1');\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'hidden';\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"hidden\">';\n"
            + "    input = builder.firstChild;\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='hidden1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"--null", "--null", "--null"})
    public void defaultValuesAfterClone() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('hidden1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'hidden';\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"hidden\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='hidden1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial-initial", "initial-initial-initial",
                "newValue-newValue-newValue", "newValue-newValue-newValue",
                "newDefault-newDefault-newDefault", "newDefault-newDefault-newDefault"})
    public void resetByClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var hidden = document.getElementById('testId');\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.value = 'newValue';\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.defaultValue = 'newDefault';\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='testId' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial-initial", "initial-initial-initial",
                "newValue-newValue-newValue", "newValue-newValue-newValue",
                "newDefault-newDefault-newDefault", "newDefault-newDefault-newDefault"})
    public void resetByJS() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var hidden = document.getElementById('testId');\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.value = 'newValue';\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.defaultValue = 'newDefault';\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial-initial", "default-default-default",
                "newValue-newValue-newValue", "attribValue-attribValue-attribValue",
                "newDefault-newDefault-newDefault"})
    public void value() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var hidden = document.getElementById('testId');\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.defaultValue = 'default';\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.value = 'newValue';\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.setAttribute('value', 'attribValue');\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"

            + "    hidden.defaultValue = 'newDefault';\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue + '-' + hidden.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "textLength not available",
            FF = "7",
            FF78 = "7")
    public void textLength() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    if(text.textLength) {\n"
            + "      alert(text.textLength);\n"
            + "    } else {\n"
            + "      alert('textLength not available');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='text' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "  function test() {\n"
            + "    var input = document.getElementById('tester');\n"
            + "    alert(input.min + '-' + input.max + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
