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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlSearchInput}.
 *
 * @author Marc Guillemot
 * @author Anton Demydenko
 * @author Ronald Brill
*/
@RunWith(BrowserRunner.class)
public class HtmlSearchInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type() throws Exception {
        final String html = "<html><head></head><body><input id='t' type='search'/></body></html>";

        final WebDriver webDriver = loadPage2(html);
        final WebElement input = webDriver.findElement(By.id("t"));
        input.sendKeys("abc");
        assertEquals("abc", input.getAttribute("value"));
        input.sendKeys(Keys.BACK_SPACE);
        assertEquals("ab", input.getAttribute("value"));
        input.sendKeys(Keys.BACK_SPACE);
        assertEquals("a", input.getAttribute("value"));
        input.sendKeys(Keys.BACK_SPACE);
        assertEquals("", input.getAttribute("value"));
        input.sendKeys(Keys.BACK_SPACE);
        assertEquals("", input.getAttribute("value"));
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
            + "  <input type='search' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }


    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1234", "§§URL§§", "1"},
            IE = {"1234", "§§URL§§second/", "2"})
    public void minLengthValidationInvalid() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='search' minlength='5' id='foo'>\n"
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
        foo.sendKeys("1234");
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
    @Alerts({"1234567890", "§§URL§§second/", "2"})
    public void minLengthValidationValid() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='search' minlength='5' id='foo'>\n"
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
        foo.sendKeys("1234567890");
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
    @Alerts({"1234", "§§URL§§second/", "2"})
    public void maxLengthValidationValid() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='search' maxlength='5' id='foo'>\n"
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
        foo.sendKeys("1234");
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
    @Alerts({"12345", "§§URL§§second/", "2"})
    public void maxLengthValidationInvalid() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='search' maxlength='5' id='foo'>\n"
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
        foo.sendKeys("1234567890");
        assertEquals(getExpectedAlerts()[0], foo.getAttribute("value"));
        //valid data
        driver.findElement(By.id("myButton")).click();
        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest().getUrl());

        assertEquals(Integer.parseInt(getExpectedAlerts()[2]), getMockWebConnection().getRequestCount());
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
            + "  <input type='search' pattern='[0-9a-zA-Z]{10,40}' id='foo' value='0987654321!'>\n"
            + "  <input type='search' pattern='[0-9a-zA-Z]{10,40}' id='bar' value='68746d6c756e69742072756c657a21'>\n"
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
            + "  <input type='search' pattern='[0-9a-zA-Z]{10,40}' id='foo' value=''>\n"
            + "  <input type='search' pattern='[0-9a-zA-Z]{10,40}' id='bar' value=' '>\n"
            + "  <input type='search' pattern='[0-9a-zA-Z]{10,40}' id='bar2' value='  \t'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
