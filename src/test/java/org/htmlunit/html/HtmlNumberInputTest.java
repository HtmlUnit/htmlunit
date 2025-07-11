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

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HtmlNumberInput}.
 *
 * @author Ronald Brill
 * @author Anton Demydenko
 * @author Raik Bieniek
 */
public class HtmlNumberInputTest extends WebDriverTestCase {

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextInteger() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
            final HtmlPage page = (HtmlPage) getEnclosedPage();
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBody().getVisibleText());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeInteger() throws Exception {
        final String html = DOCTYPE_HTML + "<html><head></head><body><input type='number' id='inpt'/></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("inpt"));

        assertNull(t.getDomAttribute("value"));
        assertEquals("", t.getDomProperty("value"));

        t.sendKeys("123");
        assertNull(t.getDomAttribute("value"));
        assertEquals("123", t.getDomProperty("value"));

        t.sendKeys(Keys.BACK_SPACE);
        assertNull(t.getDomAttribute("value"));
        assertEquals("12", t.getDomProperty("value"));

        t.sendKeys(Keys.BACK_SPACE);
        assertNull(t.getDomAttribute("value"));
        assertEquals("1", t.getDomProperty("value"));

        t.sendKeys(Keys.BACK_SPACE);
        assertNull(t.getDomAttribute("value"));
        assertEquals("", t.getDomProperty("value"));

        t.sendKeys(Keys.BACK_SPACE);
        assertNull(t.getDomAttribute("value"));
        assertEquals("", t.getDomProperty("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"123", "true"})
    public void typeIntegerValid() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
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
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[0], input.getDomProperty("value"));

        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"12", "true", "123", "false"})
    public void typeIntegerTooLarge() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
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
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[0], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        input.sendKeys("3");
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[2], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"12", "false", "123", "true"})
    public void typeIntegerTooSmall() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
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
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[0], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        input.sendKeys("3");
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[2], input.getDomProperty("value"));

        check.click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1--null-true", "1", "1--null-true", "1.2", "1.2--null-false"})
    @HtmlUnitNYI(CHROME = {"1", "1--null-true", "1.", "1.--null-true", "1.2", "1.2--null-false"},
            EDGE = {"1", "1--null-true", "1.", "1.--null-true", "1.2", "1.2--null-false"},
            FF = {"1", "1--null-true", "", "--null-false", "1.2", "1.2--null-false"},
            FF_ESR = {"1", "1--null-true", "", "--null-false", "1.2", "1.2--null-false"})
    public void typeIntegerWithDot() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
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
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[0], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        input.sendKeys(".");
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[2], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());

        input.sendKeys("2");
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[4], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[5], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "--null-false", "-12", "-12--null-true", "-123", "-123--null-false"})
    public void typeIntegerNegativeValid() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
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
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[0], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        input.sendKeys("12");
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[2], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());

        input.sendKeys("3");
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[4], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[5], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "--null-false", "-12", "-12--null-false"})
    public void typeIntegerNegativeInvalid() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
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
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[0], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        input.sendKeys("12");
        assertNull(input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[2], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeDouble() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head><body>\n"
                + "<input type='number' step='0.01' id='t'/>\n"
                + "</body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));
        t.sendKeys("1.23");
        assertNull(t.getDomAttribute("value"));
        assertEquals("1.23", t.getDomProperty("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeWhileDisabled() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><input type='number' id='p' disabled='disabled'/></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        try {
            p.sendKeys("abc");
            fail();
        }
        catch (final InvalidElementStateException e) {
            // as expected
        }

        assertNull(p.getDomAttribute("value"));
        assertEquals("", p.getDomProperty("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null"})
    public void typeDoesNotChangeValueAttribute() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
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
        final String html = DOCTYPE_HTML
                + "<html>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
        assertNull(p.getDomAttribute("value"));
        assertEquals("123", p.getDomProperty("value"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault_OnKeyPress() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
        assertNull(p.getDomAttribute("value"));
        assertEquals("123", p.getDomProperty("value"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void typeOnChange() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
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
        final String html = DOCTYPE_HTML
              + "<html>\n"
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
        final String html = DOCTYPE_HTML
              + "<html>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"8-8-8-true", "-abc-abc-true", "---true",
             "99999999999999999999999999999-99999999999999999999999999999"
                               + "-99999999999999999999999999999-true"})
    public void defaultValuesInvalidValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
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

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"8-8-8-true", "-\\s\\s-\\s\\s-true",
             "-\\s\\s\\n\\s\\s\\t\\s-\\s\\s\\n\\s\\s\\t\\s-true",
             "-\\s3\\s9\\s-\\s3\\s9\\s-true"})
    public void defaultValuesBlankValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
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
            + "  <input type='number' id='bar' value='  '>\n"
            + "  <input type='number' id='baz' value='  \n  \t '>\n"
            + "  <input type='number' id='another' value=' 3 9 '>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"8-8-8-true", "7-7-7-false", "6-6-6-false"})
    public void defaultValuesIntegerValueOutside() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
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

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"8-8-8-true", "7-7-7-false", "7.13-7.13-7.13-false"})
    public void defaultValuesInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
    @Alerts({"123-123-123-true", "2-2-2-false", "20000-2-2-false", "20000-9-9-false"})
    public void valueOutside() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
    @Alerts({"12-12-12-true", "12", "12312", "12312-12-12-false"})
    public void typeValueOutside() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        assertEquals(getExpectedAlerts()[1], t.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[2], t.getDomProperty("value"));

        driver.findElement(By.id("testBtn")).click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2-2-2-false", "2", "42", "42-2-2-false"})
    public void typeValueNotReachableByStep() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        assertEquals(getExpectedAlerts()[1], t.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[2], t.getDomProperty("value"));

        driver.findElement(By.id("testBtn")).click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2.1-2.1-2.1-false", "2.1", "42.1", "42.1-2.1-2.1-false"})
    public void typeValueNotReachableByStepDouble() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        assertEquals(getExpectedAlerts()[1], t.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[2], t.getDomProperty("value"));

        driver.findElement(By.id("testBtn")).click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"--null-true", "4", "4--null-true", "4", "4--null-true"},
            FF = {"--null-true", "4", "4--null-true", "", "--null-false"},
            FF_ESR = {"--null-true", "4", "4--null-true", "", "--null-false"})
    public void typeInvalidChars() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        assertNull(t.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[1], t.getDomProperty("value"));

        driver.findElement(By.id("testBtn")).click();
        assertEquals(getExpectedAlerts()[2], driver.getTitle());

        t.sendKeys(Keys.END, "a");
        assertNull(t.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[3], t.getDomProperty("value"));

        driver.findElement(By.id("testBtn")).click();
        assertEquals(getExpectedAlerts()[4], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"120", "120-0-0-true", "", "-0-0-true", "", "-0-0-true"},
            FF = {"120", "120-0-0-true", "", "-0-0-true", "", "-0-0-false"},
            FF_ESR = {"120", "120-0-0-true", "", "-0-0-true", "", "-0-0-false"})
    public void typeCharsAndClear() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
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
        assertEquals("0", input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[0], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        input.clear();
        assertEquals("0", input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[2], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[3], driver.getTitle());

        input.sendKeys("abc");
        assertEquals("0", input.getDomAttribute("value"));
        assertEquals(getExpectedAlerts()[4], input.getDomProperty("value"));
        check.click();
        assertEquals(getExpectedAlerts()[5], driver.getTitle());
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-0-0-true",
            FF = "-0-0-false",
            FF_ESR = "-0-0-false")
    public void issue321() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
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
    @Alerts({"13-13-13-true", "15-15-15-false", "17-15-15-false", "17-19-19-false"})
    public void valueNotReachableByStep() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
    @Alerts({"1.3-1.3-1.3-true", "1.5-1.5-1.5-false", "1.7-1.5-1.5-false", "1.7-1.9-1.9-false"})
    public void valueNotReachableByStepDouble() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
            FF_ESR = "7")
    public void textLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
            + "    } catch(e) { logEx(e); }\n"
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
    @Alerts({"null,null", "null,null", "InvalidStateError/DOMException",
             "null,null", "InvalidStateError/DOMException", "null,null"})
    public void selection2_1() throws Exception {
        selection2(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts({"null,null", "null,null", "InvalidStateError/DOMException",
             "null,null", "InvalidStateError/DOMException", "null,null"})
    public void selection2_2() throws Exception {
        selection2(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts({"null,null", "null,null", "InvalidStateError/DOMException",
             "null,null", "InvalidStateError/DOMException", "null,null"})
    public void selection2_3() throws Exception {
        selection2(10, 5);
    }

    private void selection2(final int selectionStart, final int selectionEnd) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='1234567' type='number'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var input = document.getElementById('myTextInput');\n"

            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { logEx(e); }\n"

            + "  input.value = '12345678900';\n"
            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { logEx(e); }\n"

            + "  try {\n"
            + "    input.selectionStart = " + selectionStart + ";\n"
            + "  } catch(e) { logEx(e); }\n"
            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { logEx(e); }\n"

            + "  try {\n"
            + "    input.selectionEnd = " + selectionEnd + ";\n"
            + "  } catch(e) { logEx(e); }\n"
            + "  try {\n"
            + "    log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts({"null,null", "InvalidStateError/DOMException"})
    public void selectionOnUpdate() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
            + "  } catch(e) { logEx(e); }\n"
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
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
        if (useRealBrowser()) {
            Thread.sleep(400);
        }

        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void sendKeysEnterWithoutForm() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
    @Test
    public void submitWithoutForm() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  <input id='t' type='number' value='1234'>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> driver.findElement(By.id("t")).submit());

        assertEquals(1, getMockWebConnection().getRequestCount());
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
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<form>\n"
            + "  <input type='number' id='tester' value='10'>\n"
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
     * @throws Exception if an error occurs
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
            + "  <input type='number' max='10' id='foo' value='12'>\n"
            + "  <input type='number' max='10' id='bar' value='8'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
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
            + "  <input type='number' min='10' id='foo' value='8'>\n"
            + "  <input type='number' min='10' id='bar' value='10'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"123456789",
             "123456789",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=123456789",
             "2"})
    public void patternValidationInvalid() throws Exception {
        validation("<input type='number' pattern='[0-7]{10,40}' id='e1' name='k' value='123456789'>\n",
                    "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"123456701234567012345670",
             "123456701234567012345670",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=123456701234567012345670",
             "2"})
    public void patternValidationValid() throws Exception {
        validation("<input type='number' pattern='[0-7]{10,40}' "
                + "id='e1' name='k' value='123456701234567012345670'>\n", "", null);
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
             "§§URL§§?k=",
             "2"})
    public void patternValidationEmpty() throws Exception {
        validation("<input type='number' pattern='[0-9]{10,40}' id='e1' name='k' value=''>\n", "", null);
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
        validation("<input type='number' pattern='[0-9]{10,40}' id='e1' name='k' value=' '>\n", "", null);
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
             "§§URL§§?k=",
             "2"})
    public void patternValidationWhitespace() throws Exception {
        validation("<input type='number' pattern='[0-9]{10,40}' id='e1' name='k' value='  \t'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({" 210 ",
             "",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=",
             "2"})
    public void patternValidationTrimInitial() throws Exception {
        validation("<input type='number' pattern='[ 012]{3,10}' id='e1' name='k' value=' 210 '>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null",
                       "210",
                       "true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§?k=210", "2"},
            FF = {"null",
                  "",
                  "false",
                  "true-false-false-false-false-false-false-false-false-false-false",
                  "true",
                  "§§URL§§", "1"},
            FF_ESR = {"null",
                      "",
                      "false",
                      "true-false-false-false-false-false-false-false-false-false-false",
                      "true",
                      "§§URL§§", "1"})
    @HtmlUnitNYI(FF = {"null",
                       " 210 ",
                       "false",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true",
                       "§§URL§§", "1"},
                 FF_ESR = {"null",
                           " 210 ",
                           "false",
                           "false-false-false-false-false-false-false-false-false-true-false",
                           "true",
                           "§§URL§§", "1"})
    public void patternValidationTrimType() throws Exception {
        validation("<input type='number' pattern='[ 012]{3,10}' id='e1' name='k'>\n", "", " 210 ");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "1234",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=1234", "2"})
    public void minLengthValidationInvalid() throws Exception {
        validation("<input type='number' minlength='5' id='e1' name='k'>\n", "", "1234");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"12",
             "12",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=12",
             "2"})
    public void minLengthValidationInvalidInitial() throws Exception {
        validation("<input type='number' minlength='5' id='e1' name='k' value='12'>\n", "", null);
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
             "§§URL§§?k=",
             "2"})
    public void minLengthValidationInvalidNoInitial() throws Exception {
        validation("<input type='number' minlength='5' id='e1' name='k'>\n", "", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "123456789",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=123456789",
             "2"})
    public void minLengthValidationValid() throws Exception {
        validation("<input type='number' minlength='5' id='e1' name='k'>\n", "", "123456789");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "1234",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=1234", "2"})
    public void maxLengthValidationValid() throws Exception {
        validation("<input type='number' maxlength='5' id='e1' name='k'>\n", "", "1234");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null",
             "123456789",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=123456789",
             "2"})
    public void maxLengthValidationInvalid() throws Exception {
        validation("<input type='number' maxlength='5' id='e1' name='k'>\n", "", "123456789");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"123456789",
             "123456789",
             "true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true",
             "§§URL§§?k=123456789",
             "2"})
    public void maxLengthValidationInvalidInitial() throws Exception {
        validation("<input type='number' maxlength='5' id='e1' name='k' value='123456789'>\n", "", null);
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
                + "    <input type='number' id='o1'>\n"
                + "    <input type='number' id='o2' disabled>\n"
                + "    <input type='number' id='o3' hidden>\n"
                + "    <input type='number' id='o4' readonly>\n"
                + "    <input type='number' id='o5' style='display: none'>\n"
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
        validation("<input type='number' id='e1' name='k'>\n", "", null);
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
        validation("<input type='number' id='e1' name='k'>\n", "elem.setCustomValidity('Invalid');", null);
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
        validation("<input type='number' id='e1' name='k'>\n", "elem.setCustomValidity(' ');\n", null);
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
             "§§URL§§?k=",
             "2"})
    public void validationResetCustomValidity() throws Exception {
        validation("<input type='number' id='e1' name='k'>\n",
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
        validation("<input type='number' id='e1' name='k' required>\n", "", null);
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
             "§§URL§§?k=42",
             "2"})
    public void validationRequiredValueSet() throws Exception {
        validation("<input type='number' id='e1' name='k' required>\n", "elem.value='42';", null);
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
             "§§URL§§?k=567",
             "2"})
    public void validationPattern() throws Exception {
        validation("<input type='number' id='e1' name='k' pattern='[012]{3}'>\n", "elem.value='567';", null);
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
