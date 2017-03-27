/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import static org.junit.Assert.fail;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlTextInput}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlNumberInputTest extends WebDriverTestCase {

    /**
     * Verifies that a asText() returns an empty string.
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='number' name='foo' id='foo' value='123'>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(htmlContent);

        final WebElement input = driver.findElement(By.id("foo"));
        assertEquals("", input.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type() throws Exception {
        final String html = "<html><head></head><body><input type='number' id='t'/></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));
        t.sendKeys("123");
        assertEquals("123", t.getAttribute("value"));
        t.sendKeys("\b");
        assertEquals("12", t.getAttribute("value"));
        t.sendKeys("\b");
        assertEquals("1", t.getAttribute("value"));
        t.sendKeys("\b");
        assertEquals("", t.getAttribute("value"));
        t.sendKeys("\b");
        assertEquals("", t.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeWhileDisabled() throws Exception {
        final String html = "<html><body><input type='number' id='p' disabled='disabled'/></body></html>";
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
                + "  <input type='number' id='t'/>\n"
                + "  <button id='check' onclick='alert(document.getElementById(\"t\").getAttribute(\"value\"));'>"
                        + "DoIt</button>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        final WebElement check = driver.findElement(By.id("check"));
        check.click();
        verifyAlerts(driver, getExpectedAlerts()[0]);

        t.sendKeys("abc");
        check.click();
        verifyAlerts(driver, getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234", "1234"})
    public void typeDoesNotChangeValueAttributeWithInitialValue() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "  <input type='number' id='t' value='1234'/>\n"
                + "  <button id='check' onclick='alert(document.getElementById(\"t\").getAttribute(\"value\"));'>"
                        + "DoIt</button>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        final WebElement check = driver.findElement(By.id("check"));
        check.click();
        verifyAlerts(driver, getExpectedAlerts()[0]);

        t.sendKeys("987");
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
            + "<input type='number' id='p'></input>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("1234");
        assertEquals("123", p.getAttribute("value"));
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
            + "<input type='number' id='p'></input>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("1234");
        assertEquals("123", p.getAttribute("value"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void typeOnChange() throws Exception {
        final String html =
              "<html><head></head><body>\n"
            + "<input type='number' id='p' value='1234'"
                + " onChange='alert(\"foo\");alert(event.type);'"
                + " onBlur='alert(\"boo\");alert(event.type);'>\n"
            + "<button id='b'>some button</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("567");

        assertEquals(Collections.emptyList(), getCollectedAlerts(driver));

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
              + "  <input type='number' id='t' value='1234'"
                    + " onChange='alert(\"foo\");alert(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='document.getElementById(\"t\").value=\"1234\"'>setValue</button>\n"
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
              + "  <input type='number' id='t' value='1234'"
                    + " onChange='alert(\"foo\");alert(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='document.getElementById(\"t\").defaultValue=\"1234\"'>"
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
            + "    var input = document.getElementById('text1');\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'number';\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"text\">';\n"
            + "    input = builder.firstChild;\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='text1'>\n"
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
            + "    var input = document.getElementById('text1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'number';\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"text\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='text1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234-1234-1234", "1234-1234-1234",
                "5678-1234-1234", "5678-1234-1234",
                "5678-2345-2345", "5678-2345-2345"})
    public void resetByClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    text.value = '5678';\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    text.defaultValue = '2345';\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId' value='1234'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234-1234-1234", "1234-1234-1234",
                "5678-1234-1234", "5678-1234-1234",
                "5678-2345-2345", "5678-2345-2345"})
    public void resetByJS() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    text.value = '5678';\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    text.defaultValue = '2345';\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId' value='1234'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234-1234-1234", "2345-2345-2345",
                "3456-2345-2345", "3456-9876-9876",
                "3456-44-44"})
    public void value() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    text.defaultValue = '2345';\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    text.value = '3456';\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    text.setAttribute('value', '9876');\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    text.defaultValue = '44';\n"
            + "    alert(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId' value='1234'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"textLength not available"},
            FF = {"7"})
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
            + "  <input type='number' id='testId' value='1234567'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "0",
            CHROME = "exception")
    public void selection() throws Exception {
        final String html =
              "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s = getSelection(document.getElementById('text1'));\n"
            + "    if (s != undefined) {\n"
            + "      alert(s.length);\n"
            + "    }\n"
            + "  }\n"
            + "  function getSelection(element) {\n"
            + "    try {\n"
            + "      return element.value.substring(element.selectionStart, element.selectionEnd);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='number' id='text1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "11,11", "3,11", "3,10"},
            CHROME = {"exception", "exception", "exception",
                        "exception", "exception", "exception"},
            IE = {"0,0", "0,0", "3,3", "3,10"})
    public void selection2_1() throws Exception {
        selection2(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "11,11", "0,11", "0,11"},
            CHROME = {"exception", "exception", "exception",
                        "exception", "exception", "exception"},
            IE = {"0,0", "0,0", "0,0", "0,11"})
    public void selection2_2() throws Exception {
        selection2(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "11,11", "10,11", "5,5"},
            CHROME = {"exception", "exception", "exception",
                        "exception", "exception", "exception"},
            IE = {"0,0", "0,0", "10,10", "5,5"})
    public void selection2_3() throws Exception {
        selection2(10, 5);
    }

    private void selection2(final int selectionStart, final int selectionEnd) throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='1234567' type='number'>\n"
            + "<script>\n"
            + "  var input = document.getElementById('myTextInput');\n"

            + "  try {\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { alert('exception'); }\n"

            + "  input.value = '12345678900';\n"
            + "  try {\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { alert('exception'); }\n"

            + "  try {\n"
            + "    input.selectionStart = " + selectionStart + ";\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "  try {\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { alert('exception'); }\n"

            + "  try {\n"
            + "    input.selectionEnd = " + selectionEnd + ";\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "  try {\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { alert('exception'); }\n"
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
            CHROME = "exception",
            IE = {"0,0", "4,5", "0,0", "0,0", "0,0"})
    public void selectionOnUpdate() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='1234567' type='number'>\n"
            + "<script>\n"
            + "  var input = document.getElementById('myTextInput');\n"

            + "  try {\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"

            + "    input.selectionStart = 4;\n"
            + "    input.selectionEnd = 5;\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "    input.value = '1234567890';\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"

            + "    input.value = '9876';\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"

            + "    input.selectionStart = 0;\n"
            + "    input.selectionEnd = 4;\n"

            + "    input.value = '7';\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submitOnEnter() throws Exception {
        final String html =
            "<html>\n"
            + "<body>\n"
            + "  <form action='result.html'>\n"
            + "    <input id='t' value='1234'/>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys("\n");

        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void sendKeysEnterWithoutForm() throws Exception {
        final String html =
            "<html>\n"
            + "<body>\n"
            + "  <input id='t' type='number' value='1234'>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("t")).sendKeys("\n");

        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test(expected = NoSuchElementException.class)
    public void submitWithoutForm() throws Exception {
        final String html =
            "<html>\n"
            + "<body>\n"
            + "  <input id='t' type='number' value='1234'>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("t")).submit();

        assertEquals(1, getMockWebConnection().getRequestCount());
    }
}
