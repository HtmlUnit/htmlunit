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
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link HtmlNumberInput}.
 *
 * @author Ronald Brill
 * @author Anton Demydenko
 * @author Raik Bieniek
 */
@RunWith(BrowserRunner.class)
public class HtmlNumberInputTest extends WebDriverTestCase {

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextInteger() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='number' name='tester' id='tester' value='123'>\n"
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
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextDouble() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='number' name='tester' id='tester' value='1.23'>\n"
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
    public void typeInteger() throws Exception {
        final String html = "<html><head></head><body><input type='number' id='inpt'/></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("inpt"));
        t.sendKeys("123");
        assertEquals("123", t.getAttribute("value"));
        t.sendKeys(Keys.BACK_SPACE);
        assertEquals("12", t.getAttribute("value"));
        t.sendKeys(Keys.BACK_SPACE);
        assertEquals("1", t.getAttribute("value"));
        t.sendKeys(Keys.BACK_SPACE);
        assertEquals("", t.getAttribute("value"));
        t.sendKeys(Keys.BACK_SPACE);
        assertEquals("", t.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"123", "true"})
    public void typeIntegerValid() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "  <input type='number' id='inpt'/>\n"
                + "  <button id='check' "
                        + "onclick='document.title = document.getElementById(\"inpt\").checkValidity()');'>"
                + "DoIt</button>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        final WebElement input = driver.findElement(By.id("inpt"));
        final WebElement check = driver.findElement(By.id("check"));

        input.sendKeys("123");
        assertEquals(getExpectedAlerts()[0], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"12", "true", "123", "false"})
    public void typeIntegerTooLarge() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "  <input type='number' id='inpt' max='100'/>\n"
                + "  <button id='check' "
                        + "onclick='document.title = document.getElementById(\"inpt\").checkValidity()');'>"
                + "DoIt</button>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        final WebElement input = driver.findElement(By.id("inpt"));
        final WebElement check = driver.findElement(By.id("check"));

        input.sendKeys("12");
        assertEquals(getExpectedAlerts()[0], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        input.sendKeys("3");
        assertEquals(getExpectedAlerts()[2], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"12", "false", "123", "true"})
    public void typeIntegerTooSmall() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "  <input type='number' id='inpt' min='100'/>\n"
                + "  <button id='check' "
                        + "onclick='document.title = document.getElementById(\"inpt\").checkValidity()');'>"
                + "DoIt</button>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        final WebElement input = driver.findElement(By.id("inpt"));
        final WebElement check = driver.findElement(By.id("check"));

        input.sendKeys("12");
        assertEquals(getExpectedAlerts()[0], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        input.sendKeys("3");
        assertEquals(getExpectedAlerts()[2], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "1--null-true", "1", "1--null-true", "1.2", "1.2--null-false"},
            FF = {"1", "1--null-true", "", "--null-false", "1.2", "1.2--null-false"},
            IE = {"1", "1--null-true", "1.", "1.--null-true", "1.2", "1.2--null-false"})
    @HtmlUnitNYI(CHROME = {"1", "1--null-true", "1.", "1.--null-true", "1.2", "1.2--null-false"},
            EDGE = {"1", "1--null-true", "1.", "1.--null-true", "1.2", "1.2--null-false"},
            FF = {"1", "1--null-true", "1.", "--null-false", "1.2", "1.2--null-false"},
            FF78 = {"1", "1--null-true", "1.", "1.--null-true", "1.2", "1.2--null-false"})
    public void typeIntegerWithDot() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var input = document.getElementById('inpt');\n"
                + "    document.title = input.value + '-' "
                                + "+ input.defaultValue + '-' "
                                + "+ input.getAttribute('value')+ '-' "
                                + "+ input.checkValidity();\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <input type='number' id='inpt' />\n"
                + "  <button id='check' onclick='test()');'>"
                + "DoIt</button>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        final WebElement input = driver.findElement(By.id("inpt"));
        final WebElement check = driver.findElement(By.id("check"));

        input.sendKeys("1");
        assertEquals(getExpectedAlerts()[0], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        input.sendKeys(".");
        assertEquals(getExpectedAlerts()[2], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());

        input.sendKeys("2");
        assertEquals(getExpectedAlerts()[4], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[5], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "--null-false", "-12", "-12--null-true", "-123", "-123--null-false"},
            IE = {"", "--null-true", "12", "12--null-true", "123", "123--null-true"})
    @HtmlUnitNYI(CHROME = {"-", "--null-false", "-12", "-12--null-true", "-123", "-123--null-false"},
            EDGE = {"-", "--null-false", "-12", "-12--null-true", "-123", "-123--null-false"},
            FF = {"-", "--null-false", "-12", "-12--null-true", "-123", "-123--null-false"},
            FF78 = {"-", "--null-false", "-12", "-12--null-true", "-123", "-123--null-false"},
            IE = {"-", "--null-false", "-12", "-12--null-true", "-123", "-123--null-false"})
    public void typeIntegerNegativeValid() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var input = document.getElementById('inpt');\n"
                + "    document.title = input.value + '-' "
                                + "+ input.defaultValue + '-' "
                                + "+ input.getAttribute('value')+ '-' "
                                + "+ input.checkValidity();\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <input type='number' id='inpt' min='-42' max='1234'/>\n"
                + "  <button id='check' onclick='test()');'>"
                + "DoIt</button>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        final WebElement input = driver.findElement(By.id("inpt"));
        final WebElement check = driver.findElement(By.id("check"));

        input.sendKeys("-");
        assertEquals(getExpectedAlerts()[0], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        input.sendKeys("12");
        assertEquals(getExpectedAlerts()[2], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());

        input.sendKeys("3");
        assertEquals(getExpectedAlerts()[4], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[5], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "--null-false", "-12", "-12--null-false"},
            IE = {"", "--null-true", "12", "12--null-true"})
    @HtmlUnitNYI(CHROME = {"-", "--null-false", "-12", "-12--null-false"},
            EDGE = {"-", "--null-false", "-12", "-12--null-false"},
            FF = {"-", "--null-false", "-12", "-12--null-false"},
            FF78 = {"-", "--null-false", "-12", "-12--null-false"},
            IE = {"-", "--null-false", "-12", "-12--null-false"})
    public void typeIntegerNegativeInvalid() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var input = document.getElementById('inpt');\n"
                + "    document.title = input.value + '-' "
                                + "+ input.defaultValue + '-' "
                                + "+ input.getAttribute('value')+ '-' "
                                + "+ input.checkValidity();\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <input type='number' id='inpt' min='1' max='1234'/>\n"
                + "  <button id='check' onclick='test()');'>"
                + "DoIt</button>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        final WebElement input = driver.findElement(By.id("inpt"));
        final WebElement check = driver.findElement(By.id("check"));

        input.sendKeys("-");
        assertEquals(getExpectedAlerts()[0], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        input.sendKeys("12");
        assertEquals(getExpectedAlerts()[2], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeDouble() throws Exception {
        final String html = "<html><head></head><body>\n"
                + "<input type='number' step='0.01' id='t'/>\n"
                + "</body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));
        t.sendKeys("1.23");
        assertEquals("1.23", t.getAttribute("value"));
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
                + "  <button id='check' onclick='document.title = "
                                    + "document.getElementById(\"t\").getAttribute(\"value\");'>"
                        + "DoIt</button>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        final WebElement check = driver.findElement(By.id("check"));
        check.click();
        assertEquals(getExpectedAlerts()[0], driver.getTitle());

        t.sendKeys("abc");
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());
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
                + "  <button id='check' onclick='document.title = "
                                    + "document.getElementById(\"t\").getAttribute(\"value\");'>"
                        + "DoIt</button>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        final WebElement check = driver.findElement(By.id("check"));
        check.click();
        assertEquals(getExpectedAlerts()[0], driver.getTitle());

        t.sendKeys("987");
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());
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
                "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "<input type='number' id='p' value='1234'"
                    + " onChange='log(\"foo\");log(event.type);'"
                    + " onBlur='log(\"boo\");log(event.type);'>\n"
            + "<button id='b'>some button</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("567");

        assertEquals(Collections.emptyList(), getCollectedAlerts(driver));

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        final String[] expectedAlerts1 = {"foo", "change", "boo", "blur"};
        verifyTitle2(driver, expectedAlerts1);

        // set only the focus but change nothing
        p.click();
        assertTrue(getCollectedAlerts(driver, 1).isEmpty());

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        final String[] expectedAlerts2 = {"foo", "change", "boo", "blur", "boo", "blur"};
        verifyTitle2(driver, expectedAlerts2);
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
              + "  <input type='number' id='t' value='1234'"
                    + " onChange='log(\"foo\");log(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='document.getElementById(\"t\").value=\"1234\"'>setValue</button>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("set")).click();
        verifyTitle2(driver, new String[]{});

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        verifyTitle2(driver, new String[]{});
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void setDefaultValueOnChange() throws Exception {
        final String html =
              "<html>\n"
              + "<head>"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "</script>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input type='number' id='t' value='1234'"
                    + " onChange='log(\"foo\");log(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='document.getElementById(\"t\").defaultValue=\"1234\"'>"
                      + "setValue</button>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("set")).click();
        verifyTitle2(driver, new String[]{});

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        verifyTitle2(driver, new String[]{});
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"--null-true", "--null-true", "--null-true"})
    public void defaultValues() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('inpt');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'number';\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"number\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='inpt'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts(DEFAULT = {"8-8-8-true", "-abc-abc-true", "---true",
                       "99999999999999999999999999999-99999999999999999999999999999"
                               + "-99999999999999999999999999999-true"},
            IE = {"8-8-8-true", "---true", "---true",
                  "99999999999999999999999999999-99999999999999999999999999999-99999999999999999999999999999-true"})
    @HtmlUnitNYI(IE = {"8-8-8-true", "-abc-abc-true", "---true",
                       "99999999999999999999999999999-99999999999999999999999999999"
                               + "-99999999999999999999999999999-true"})
    public void defaultValuesInvalidValue() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('foo');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input = document.getElementById('bar');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input = document.getElementById('baz');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input = document.getElementById('another');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='number' id='foo' value='8'>\n"
            + "  <input type='number' id='bar' value='abc'>\n"
            + "  <input type='number' id='baz' value=''>\n"
            + "  <input type='number' id='another' value='99999999999999999999999999999'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts({"8-8-8-true", "7-7-7-false", "6-6-6-false"})
    public void defaultValuesIntegerValueOutside() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('foo');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input = document.getElementById('bar');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input = document.getElementById('baz');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='number' id='foo' value='8' min='1' max='10'>\n"
            + "  <input type='number' id='bar' value='7'  min='9' max='10'>\n"
            + "  <input type='number' id='baz' value='6'  min='1' max='4'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts({"8-8-8-true", "7-7-7-false", "7.13-7.13-7.13-false"})
    public void defaultValuesInvalid() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('foo');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input = document.getElementById('bar');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input = document.getElementById('baz');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='number' id='foo' value='8' min='2' max='10' step='2' >\n"
            + "  <input type='number' id='bar' value='7' min='2' max='10' step='2' >\n"
            + "  <input type='number' id='baz' value='7.13' min='2' max='10' step='2' >\n"
            + "</body>\n"
            + "</html>";

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
            + "    var input = document.getElementById('inpt');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'number';\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"number\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='inpt'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234-1234-1234", "1234-1234-1234",
                "5678-1234-1234", "5678-1234-1234",
                "5678-2345-2345", "5678-2345-2345"})
    public void resetByClick() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    log(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    text.value = '5678';\n"
            + "    log(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    text.defaultValue = '2345';\n"
            + "    log(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId' value='1234'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234-1234-1234", "1234-1234-1234",
                "5678-1234-1234", "5678-1234-1234",
                "5678-2345-2345", "5678-2345-2345"})
    public void resetByJS() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    log(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    text.value = '5678';\n"
            + "    log(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    text.defaultValue = '2345';\n"
            + "    log(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(text.value + '-' + text.defaultValue + '-' + text.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId' value='1234'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234-1234-1234-true", "2345-2345-2345-true",
                "3456-2345-2345-true", "3456-9876-9876-true",
                "3456-44-44-true"})
    public void value() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.defaultValue = '2345';\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.value = '3456';\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.setAttribute('value', '9876');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.defaultValue = '44';\n"
            + "    log(input.value + '-' "
                        + "+ input.defaultValue + '-' "
                        + "+ input.getAttribute('value')+ '-' "
                        + "+ input.checkValidity());\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId' value='1234'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"123-123-123-true", "2-2-2-false", "20000-2-2-false", "20000-9-9-false"},
            IE = {"123-123-123-true", "2-2-2-true", "20000-2-2-false", "20000-9-9-false"})
    @HtmlUnitNYI(IE = {"123-123-123-true", "2-2-2-false", "20000-2-2-false", "20000-9-9-false"})
    public void valueOutside() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.defaultValue = '2';\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.value = '20000';\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.setAttribute('value', '9');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId' value='123' min='10' max='1000' >\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"12-12-12-true", "12312", "12312-12-12-false"})
    public void typeValueOutside() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    document.title = input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity();\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId' value='12' min='10' max='20' >\n"
            + "  <input type='button' id='testBtn' onclick='test()' >\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals(getExpectedAlerts()[0], driver.getTitle());

        final WebElement t = driver.findElement(By.id("testId"));
        t.sendKeys(Keys.HOME);
        t.sendKeys("123");
        assertEquals(getExpectedAlerts()[1], t.getAttribute("value"));

        driver.findElement(By.id("testBtn")).click();
        assertEquals(getExpectedAlerts()[2], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2-2-2-false", "42", "42-2-2-false"})
    public void typeValueNotReachableByStep() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    document.title = input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity();\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId' value='2' min='0' max='200' step='51' >\n"
            + "  <input type='button' id='testBtn' onclick='test()' >\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals(getExpectedAlerts()[0], driver.getTitle());

        final WebElement t = driver.findElement(By.id("testId"));
        t.sendKeys(Keys.HOME);
        t.sendKeys("4");
        assertEquals(getExpectedAlerts()[1], t.getAttribute("value"));

        driver.findElement(By.id("testBtn")).click();
        assertEquals(getExpectedAlerts()[2], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2.1-2.1-2.1-false", "42.1", "42.1-2.1-2.1-false"})
    public void typeValueNotReachableByStepDouble() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    document.title = input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity();\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId' value='2.1' min='0' max='200' step='0.5' >\n"
            + "  <input type='button' id='testBtn' onclick='test()' >\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals(getExpectedAlerts()[0], driver.getTitle());

        final WebElement t = driver.findElement(By.id("testId"));
        t.sendKeys(Keys.HOME);
        t.sendKeys("4");
        assertEquals(getExpectedAlerts()[1], t.getAttribute("value"));

        driver.findElement(By.id("testBtn")).click();
        assertEquals(getExpectedAlerts()[2], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"--null-true", "4", "4--null-true", "4", "4--null-true"},
            FF = {"--null-true", "4", "4--null-true", "", "--null-false"},
            FF78 = {"--null-true", "4", "4--null-true", "", "--null-false"},
            IE = {"--null-true", "4", "4--null-true", "4a", "4a--null-true"})
    @HtmlUnitNYI(FF = {"--null-true", "4", "4--null-true", "4a", "--null-false"},
            FF78 = {"--null-true", "4", "4--null-true", "4a", "--null-false"},
            IE = {"--null-true", "4", "4--null-true", "4a", "--null-false"})
    public void typeInvalidChars() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    document.title = input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity();\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId'>\n"
            + "  <input type='button' id='testBtn' onclick='test()' >\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals(getExpectedAlerts()[0], driver.getTitle());

        final WebElement t = driver.findElement(By.id("testId"));
        t.sendKeys(Keys.END, "4");
        assertEquals(getExpectedAlerts()[1], t.getAttribute("value"));

        driver.findElement(By.id("testBtn")).click();
        assertEquals(getExpectedAlerts()[2], driver.getTitle());

        t.sendKeys(Keys.END, "a");
        assertEquals(getExpectedAlerts()[3], t.getAttribute("value"));

        driver.findElement(By.id("testBtn")).click();
        assertEquals(getExpectedAlerts()[4], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"120", "120-0-0-true", "", "-0-0-true", "", "-0-0-true"},
            FF = {"120", "120-0-0-true", "", "-0-0-true", "", "-0-0-false"},
            FF78 = {"120", "120-0-0-true", "", "-0-0-true", "", "-0-0-false"},
            IE = {"012", "012-0-0-true", "", "-0-0-true", "", "-0-0-true"})
    @HtmlUnitNYI(FF = {"120", "120-0-0-true", "", "-0-0-true", "abc", "-0-0-false"},
            FF78 = {"120", "120-0-0-true", "", "-0-0-true", "abc", "-0-0-false"},
            IE = {"120", "120-0-0-true", "", "-0-0-true", "abc", "-0-0-false"})
    public void typeCharsAndClear() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var input = document.getElementById('inpt');\n"
                + "    document.title = input.value + '-' "
                                + "+ input.defaultValue + '-' "
                                + "+ input.getAttribute('value')+ '-' "
                                + "+ input.checkValidity();\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <input type='number' id='inpt' min='0' max='999' value='0' />\n"
                + "  <button id='check' onclick='test()');'>"
                + "DoIt</button>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        final WebElement input = driver.findElement(By.id("inpt"));
        final WebElement check = driver.findElement(By.id("check"));

        input.sendKeys("12");
        assertEquals(getExpectedAlerts()[0], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        input.clear();
        assertEquals(getExpectedAlerts()[2], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());

        input.sendKeys("abc");
        assertEquals(getExpectedAlerts()[4], input.getAttribute("value"));
        check.click();
        assertEquals(getExpectedAlerts()[5], driver.getTitle());
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-0-0-true",
            FF = "-0-0-false",
            FF78 = "-0-0-false")
    @HtmlUnitNYI(IE = "-0-0-false")
    public void issue321() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var input = document.getElementById('inpt');\n"
                + "    log(input.value + '-' "
                                + "+ input.defaultValue + '-' "
                                + "+ input.getAttribute('value')+ '-' "
                                + "+ input.checkValidity());\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <input type='number' id='inpt' min='0' max='999' value='0' />\n"
                + "  <button id='check' onclick='test()');'>"
                + "DoIt</button>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        final WebElement input = driver.findElement(By.id("inpt"));

        input.clear();
        input.sendKeys("abc");
        ((JavascriptExecutor) driver).executeScript("test();");
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"13-13-13-true", "15-15-15-false", "17-15-15-false", "17-19-19-false"},
            IE = {"13-13-13-true", "15-15-15-true", "17-15-15-false", "17-19-19-false"})
    @HtmlUnitNYI(IE = {"13-13-13-true", "15-15-15-false", "17-15-15-false", "17-19-19-false"})
    public void valueNotReachableByStep() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.defaultValue = '15';\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.value = '17';\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.setAttribute('value', '19');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId' value='13' min='10' max='20' step='3' >\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1.3-1.3-1.3-true", "1.5-1.5-1.5-false", "1.7-1.5-1.5-false", "1.7-1.9-1.9-false"},
            IE = {"1.3-1.3-1.3-true", "1.5-1.5-1.5-true", "1.7-1.5-1.5-false", "1.7-1.9-1.9-false"})
    @HtmlUnitNYI(IE = {"1.3-1.3-1.3-true", "1.5-1.5-1.5-false", "1.7-1.5-1.5-false", "1.7-1.9-1.9-false"})
    public void valueNotReachableByStepDouble() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.defaultValue = '1.5';\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.value = '1.7';\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"

            + "    input.setAttribute('value', '1.9');\n"
            + "    log(input.value + '-' "
                            + "+ input.defaultValue + '-' "
                            + "+ input.getAttribute('value')+ '-' "
                            + "+ input.checkValidity());\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='number' id='testId' value='1.3' min='1.0' max='2.0' step='0.3' >\n"
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
            FF78 = "7")
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
            + "  <input type='number' id='testId' value='1234567'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void selection() throws Exception {
        final String html =
              "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var s = getSelection(document.getElementById('inpt'));\n"
            + "    if (s != undefined) {\n"
            + "      log(s.length);\n"
            + "    }\n"
            + "  }\n"
            + "  function getSelection(element) {\n"
            + "    try {\n"
            + "      return element.value.substring(element.selectionStart, element.selectionEnd);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='number' id='inpt'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"null,null", "null,null", "exception",
                       "null,null", "exception", "null,null"},
            IE = {"0,0", "0,0", "3,3", "3,10"})
    public void selection2_1() throws Exception {
        selection2(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"null,null", "null,null", "exception",
                       "null,null", "exception", "null,null"},
            IE = {"0,0", "0,0", "0,0", "0,11"})
    public void selection2_2() throws Exception {
        selection2(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"null,null", "null,null", "exception",
                       "null,null", "exception", "null,null"},
            IE = {"0,0", "0,0", "10,10", "5,5"})
    public void selection2_3() throws Exception {
        selection2(10, 5);
    }

    private void selection2(final int selectionStart, final int selectionEnd) throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='1234567' type='number'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var input = document.getElementById('myTextInput');\n"

            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { log('exception'); }\n"

            + "  input.value = '12345678900';\n"
            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { log('exception'); }\n"

            + "  try {\n"
            + "    input.selectionStart = " + selectionStart + ";\n"
            + "  } catch(e) { log('exception'); }\n"
            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { log('exception'); }\n"

            + "  try {\n"
            + "    input.selectionEnd = " + selectionEnd + ";\n"
            + "  } catch(e) { log('exception'); }\n"
            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { log('exception'); }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"null,null", "exception"},
            IE = {"0,0", "4,5", "0,0", "0,0", "0,0"})
    public void selectionOnUpdate() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='1234567' type='number'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var input = document.getElementById('myTextInput');\n"

            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"

            + "    input.selectionStart = 4;\n"
            + "    input.selectionEnd = 5;\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "    input.value = '1234567890';\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"

            + "    input.value = '9876';\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"

            + "    input.selectionStart = 0;\n"
            + "    input.selectionEnd = 4;\n"

            + "    input.value = '7';\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { log('exception'); }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(html);
        final WebElement field = driver.findElement(By.id("t"));

        field.sendKeys(Keys.ENTER);

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
    @Test(expected = JavascriptException.class)
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
            + "  <input type='number' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"10", ""})
    public void clearInput() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<form>\n"
            + "  <input type='number' id='tester' value='10'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement element = driver.findElement(By.id("tester"));
        assertEquals(getExpectedAlerts()[0], element.getAttribute("value"));

        element.clear();
        assertEquals(getExpectedAlerts()[1], element.getAttribute("value"));
    }

    @Test
    @Alerts("false-true")
    public void maxValidation() throws Exception {
        final String html = "<html>\n"
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
            + "  <input type='number' max='10' id='foo' value='12'>\n"
            + "  <input type='number' max='10' id='bar' value='8'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("false-true")
    public void minValidation() throws Exception {
        final String html = "<html>\n"
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
            + "  <input type='number' min='10' id='foo' value='8'>\n"
            + "  <input type='number' min='10' id='bar' value='10'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
