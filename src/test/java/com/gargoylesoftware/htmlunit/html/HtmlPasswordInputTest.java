/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlPasswordInput}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlPasswordInputTest extends WebDriverTestCase {

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
        p.sendKeys("\b");
        assertEquals("ab", p.getAttribute("value"));
        p.sendKeys("\b");
        assertEquals("a", p.getAttribute("value"));
        p.sendKeys("\b");
        assertEquals("", p.getAttribute("value"));
        p.sendKeys("\b");
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
            Assert.fail();
        }
        catch (final InvalidElementStateException e) {
            // as expected
        }
        assertEquals("", p.getAttribute("value"));
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
            + " onBlur='alert(\"boo\");alert(event.type);'"
            + "><br>\n"
            + "<button id='b'>some button</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("HtmlUnit");

        assertEquals(Collections.emptyList(), getCollectedAlerts(driver));

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        final String[] expectedAlerts1 = {"foo", "change", "boo", "blur"};
        assertEquals(expectedAlerts1, getCollectedAlerts(driver));

        // set only the focus but change nothing
        p.click();
        assertEquals(expectedAlerts1, getCollectedAlerts(driver));

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        final String[] expectedAlerts2 = {"foo", "change", "boo", "blur", "boo", "blur"};
        assertEquals(expectedAlerts2, getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "-", "-", "-" })
    public void defaultValues() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('password1');\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'password';\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"password\">';\n"
            + "    input = builder.firstChild;\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"
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
    @Alerts({ "-", "-", "-" })
    public void defaultValuesAfterClone() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('password1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'password';\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"password\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"
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
    @Alerts({ "initial-initial", "initial-initial", "newValue-initial", "newValue-initial",
                "newValue-newDefault", "newValue-newDefault" })
    public void resetByClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var password = document.getElementById('testId');\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"

            + "    password.value = 'newValue';\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"

            + "    password.defaultValue = 'newDefault';\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"
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
    @Alerts({ "initial-initial", "initial-initial", "newValue-initial", "newValue-initial",
                "newValue-newDefault", "newValue-newDefault" })
    public void resetByJS() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var password = document.getElementById('testId');\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"

            + "    password.value = 'newValue';\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"

            + "    password.defaultValue = 'newDefault';\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"
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
    @Alerts(DEFAULT = { "initial-initial", "default-default", "newValue-default", "newValue-newdefault" },
            IE8 = { "initial-initial", "initial-default", "newValue-default", "newValue-newdefault" })
    public void defaultValue() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var password = document.getElementById('testId');\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"

            + "    password.defaultValue = 'default';\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"

            + "    password.value = 'newValue';\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"
            + "    password.defaultValue = 'newdefault';\n"
            + "    alert(password.value + '-' + password.defaultValue);\n"
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
    @Alerts(DEFAULT = { "7" },
            IE = { "textLength not available" })
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
            + "    if (typeof element.selectionStart == 'number') {\n"
            + "      return element.value.substring(element.selectionStart, element.selectionEnd);\n"
            + "    } else if (document.selection && document.selection.createRange) {\n"
            + "      return document.selection.createRange().text;\n"
            + "    }\n"
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
    @Alerts(DEFAULT = { "0,0", "11,11", "3,11", "3,10" },
            IE11 = { "0,0", "0,0", "3,3", "3,10" },
            IE8 = { "undefined,undefined", "undefined,undefined", "3,undefined", "3,10" })
    public void selection2_1() throws Exception {
        selection2(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = { "0,0", "11,11", "0,11", "0,11" },
            IE11 = { "0,0", "0,0", "0,0", "0,11" },
            IE8 = { "undefined,undefined", "undefined,undefined", "-3,undefined", "-3,15" })
    public void selection2_2() throws Exception {
        selection2(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = { "0,0", "11,11", "10,11", "5,5" },
            IE11 = { "0,0", "0,0", "10,10", "5,5" },
            IE = { "undefined,undefined", "undefined,undefined", "10,undefined", "10,5" })
    public void selection2_3() throws Exception {
        selection2(10, 5);
    }

    private void selection2(final int selectionStart, final int selectionEnd) throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='Bonjour' type='password'>\n"
            + "<script>\n"
            + "    var input = document.getElementById('myTextInput');\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "    input.value = 'Hello there';\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "    input.selectionStart = " + selectionStart + ";\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "    input.selectionEnd = " + selectionEnd + ";\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }
}
