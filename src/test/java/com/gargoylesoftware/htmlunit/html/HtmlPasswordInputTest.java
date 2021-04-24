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

import static org.junit.Assert.fail;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlPasswordInput}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Anton Demydenko
 */
@RunWith(BrowserRunner.class)
public class HtmlPasswordInputTest extends WebDriverTestCase {

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
            + "  <input type='password' name='tester' id='tester' value='bla'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBody().getVisibleText());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type() throws Exception {
        final String html = "<html><head></head><body><input type='password' id='p'/></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("abc");
        assertEquals("abc", p.getAttribute("value"));
        p.sendKeys(Keys.BACK_SPACE);
        assertEquals("ab", p.getAttribute("value"));
        p.sendKeys(Keys.BACK_SPACE);
        assertEquals("a", p.getAttribute("value"));
        p.sendKeys(Keys.BACK_SPACE);
        assertEquals("", p.getAttribute("value"));
        p.sendKeys(Keys.BACK_SPACE);
        assertEquals("", p.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeWhileDisabled() throws Exception {
        final String html = "<html><body><input type='password' id='p' disabled='disabled'/></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        try {
            p.sendKeys("abc");
            fail();
        }
        catch (final InvalidElementStateException e) {
            // as expected
        }
        assertEquals("", p.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null"})
    public void typeDoesNotChangeValueAttribute() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "  <input type='password' id='p'/>\n"
                + "  <button id='check' onclick='alert(document.getElementById(\"p\").getAttribute(\"value\"));'>"
                        + "DoIt</button>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));

        final WebElement check = driver.findElement(By.id("check"));
        check.click();
        verifyAlerts(driver, getExpectedAlerts()[0]);

        p.sendKeys("abc");
        check.click();
        verifyAlerts(driver, getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HtmlUnit", "HtmlUnit"})
    public void typeDoesNotChangeValueAttributeWithInitialValue() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "  <input type='password' id='p' value='HtmlUnit'/>\n"
                + "  <button id='check' onclick='alert(document.getElementById(\"p\").getAttribute(\"value\"));'>"
                        + "DoIt</button>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));

        final WebElement check = driver.findElement(By.id("check"));
        check.click();
        verifyAlerts(driver, getExpectedAlerts()[0]);

        p.sendKeys("abc");
        check.click();
        verifyAlerts(driver, getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault_OnKeyDown() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function handler(e) {\n"
            + "    if (e && e.target.value.length > 2)\n"
            + "      e.preventDefault();\n"
            + "    else if (!e && window.event.srcElement.value.length > 2)\n"
            + "      return false;\n"
            + "  }\n"
            + "  function init() {\n"
            + "    document.getElementById('p').onkeydown = handler;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "<input type='password' id='p'></input>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("abcd");
        assertEquals("abc", p.getAttribute("value"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault_OnKeyPress() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function handler(e) {\n"
            + "    if (e && e.target.value.length > 2)\n"
            + "      e.preventDefault();\n"
            + "    else if (!e && window.event.srcElement.value.length > 2)\n"
            + "      return false;\n"
            + "  }\n"
            + "  function init() {\n"
            + "    document.getElementById('p').onkeypress = handler;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "<input type='password' id='p'></input>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("abcd");
        assertEquals("abc", p.getAttribute("value"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void typeOnChange() throws Exception {
        final String html =
              "<html><head></head><body>\n"
            + "<input type='password' id='p' value='Hello world'"
                + " onChange='alert(\"foo\");alert(event.type);'"
                + " onBlur='alert(\"boo\");alert(event.type);'>\n"
            + "<button id='b'>some button</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("HtmlUnit");

        assertTrue(getCollectedAlerts(driver, 1).isEmpty());

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        final String[] expectedAlerts1 = {"foo", "change", "boo", "blur"};
        assertEquals(expectedAlerts1, getCollectedAlerts(driver, 4));

        // set only the focus but change nothing
        p.click();
        assertTrue(getCollectedAlerts(driver, 1).isEmpty());

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        final String[] expectedAlerts2 = {"boo", "blur"};
        assertEquals(expectedAlerts2, getCollectedAlerts(driver, 2));
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
              + "  <input type='password' id='p' value='Hello world'"
                    + " onChange='alert(\"foo\");alert(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='document.getElementById(\"p\").value=\"HtmlUnit\"'>setValue</button>\n"
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
              + "  <input type='password' id='p' value='Hello world'"
                    + " onChange='alert(\"foo\");alert(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='document.getElementById(\"p\").defaultValue=\"HtmlUnit\"'>"
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
            + "    var input = document.getElementById('password1');\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'password';\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"password\">';\n"
            + "    input = builder.firstChild;\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='password' id='password1'>\n"
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
            + "    var input = document.getElementById('password1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'password';\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"password\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='password' id='password1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial-initial", "initial-initial-initial",
                "newValue-initial-initial", "newValue-initial-initial",
                "newValue-newDefault-newDefault", "newValue-newDefault-newDefault"})
    public void resetByClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var password = document.getElementById('testId');\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    password.value = 'newValue';\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    password.defaultValue = 'newDefault';\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='password' id='testId' value='initial'>\n"
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
                "newValue-initial-initial", "newValue-initial-initial",
                "newValue-newDefault-newDefault", "newValue-newDefault-newDefault"})
    public void resetByJS() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var password = document.getElementById('testId');\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    password.value = 'newValue';\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    password.defaultValue = 'newDefault';\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='password' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial-initial", "default-default-default",
                "newValue-default-default", "newValue-attribValue-attribValue",
                "newValue-newDefault-newDefault"})
    public void value() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var password = document.getElementById('testId');\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    password.defaultValue = 'default';\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    password.value = 'newValue';\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    password.setAttribute('value', 'attribValue');\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"

            + "    password.defaultValue = 'newDefault';\n"
            + "    alert(password.value + '-' + password.defaultValue + '-' + password.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='password' id='testId' value='initial'>\n"
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
            + "  <input type='password' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void selection() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(getSelection(document.getElementById('text1')).length);\n"
            + "  }\n"
            + "  function getSelection(element) {\n"
            + "    return element.value.substring(element.selectionStart, element.selectionEnd);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='password' id='text1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "11,11", "3,11", "3,10"},
            IE = {"0,0", "0,0", "3,3", "3,10"})
    public void selection2_1() throws Exception {
        selection2(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "11,11", "11,11", "11,11"},
            IE = {"0,0", "0,0", "0,0", "0,11"})
    public void selection2_2() throws Exception {
        selection2(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "11,11", "10,11", "5,5"},
            IE = {"0,0", "0,0", "10,10", "5,5"})
    public void selection2_3() throws Exception {
        selection2(10, 5);
    }

    private void selection2(final int selectionStart, final int selectionEnd) throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='Bonjour' type='password'>\n"
            + "<script>\n"
            + "  var input = document.getElementById('myTextInput');\n"
            + "  alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  input.value = 'Hello there';\n"
            + "  alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  input.selectionStart = " + selectionStart + ";\n"
            + "  alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  input.selectionEnd = " + selectionEnd + ";\n"
            + "  alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "4,5", "10,10", "4,4", "1,1"},
            IE = {"0,0", "4,5", "0,0", "0,0", "0,0"})
    public void selectionOnUpdate() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='Hello' type='password'>\n"
            + "<script>\n"
            + "  var input = document.getElementById('myTextInput');\n"
            + "  alert(input.selectionStart + ',' + input.selectionEnd);\n"

            + "  input.selectionStart = 4;\n"
            + "  input.selectionEnd = 5;\n"
            + "  alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  input.value = 'abcdefghif';\n"
            + "  alert(input.selectionStart + ',' + input.selectionEnd);\n"

            + "  input.value = 'abcd';\n"
            + "  alert(input.selectionStart + ',' + input.selectionEnd);\n"

            + "  input.selectionStart = 0;\n"
            + "  input.selectionEnd = 4;\n"

            + "  input.value = 'a';\n"
            + "  alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("password")
    public void upperCase() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId').type);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input TYPE='password' id='myId'>\n"
            + "</body></html>";
        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlPasswordInput.class.isInstance(page.getHtmlElementById("myId")));
        }
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
            + "  <input type='password' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts("false-true")
    public void patternValidation() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var foo = document.getElementById('foo');\n"
            + "    var bar = document.getElementById('bar');\n"
            + "    alert(foo.checkValidity() + '-' + bar.checkValidity() );\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='password' pattern='[0-9a-zA-Z]{10,40}' id='foo' value='0987654321!'>\n"
            + "  <input type='password' pattern='[0-9a-zA-Z]{10,40}' id='bar' value='68746d6c756e69742072756c657a21'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts("true-false-false")
    public void patternValidationEmpty() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var foo = document.getElementById('foo');\n"
            + "    var bar = document.getElementById('bar');\n"
            + "    var bar2 = document.getElementById('bar2');\n"
            + "    alert(foo.checkValidity() + '-' + bar.checkValidity() + '-' + bar2.checkValidity());\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='password' pattern='[0-9a-zA-Z]{10,40}' id='foo' value=''>\n"
            + "  <input type='password' pattern='[0-9a-zA-Z]{10,40}' id='bar' value=' '>\n"
            + "  <input type='password' pattern='[0-9a-zA-Z]{10,40}' id='bar2' value='  \t'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"abcd", "§§URL§§", "1"},
            IE = {"abcd", "§§URL§§second/", "2"})
    public void minLengthValidationInvalid() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='password' minlength='5' id='foo'>\n"
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

        final WebElement foo = driver.findElement(By.id("foo"));
        foo.sendKeys("abcd");
        assertEquals(getExpectedAlerts()[0], foo.getAttribute("value"));
        //invalid data
        driver.findElement(By.id("myButton")).click();
        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest().getUrl());

        assertEquals(Integer.parseInt(getExpectedAlerts()[2]), getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"abcdefghi", "§§URL§§second/", "2"})
    public void minLengthValidationValid() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='password' minlength='5' id='foo'>\n"
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

        final WebElement foo = driver.findElement(By.id("foo"));
        foo.sendKeys("abcdefghi");
        assertEquals(getExpectedAlerts()[0], foo.getAttribute("value"));
        //valid data
        driver.findElement(By.id("myButton")).click();
        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest().getUrl());

        assertEquals(Integer.parseInt(getExpectedAlerts()[2]), getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"abcd", "§§URL§§second/", "2"})
    public void maxLengthValidationValid() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='password' maxlength='5' id='foo'>\n"
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

        final WebElement foo = driver.findElement(By.id("foo"));
        foo.sendKeys("abcd");
        assertEquals(getExpectedAlerts()[0], foo.getAttribute("value"));
        //invalid data
        driver.findElement(By.id("myButton")).click();
        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest().getUrl());

        assertEquals(Integer.parseInt(getExpectedAlerts()[2]), getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"abcde", "§§URL§§second/", "2"})
    public void maxLengthValidationInvalid() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='password' maxlength='5' id='foo'>\n"
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

        final WebElement foo = driver.findElement(By.id("foo"));
        foo.sendKeys("abcdefghi");
        assertEquals(getExpectedAlerts()[0], foo.getAttribute("value"));
        //valid data
        driver.findElement(By.id("myButton")).click();
        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest().getUrl());

        assertEquals(Integer.parseInt(getExpectedAlerts()[2]), getMockWebConnection().getRequestCount());
    }
}
