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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlUrlInput}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Anton Demydenko
 */
@RunWith(BrowserRunner.class)
public class HtmlUrlInputTest extends WebDriverTestCase {

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
            + "  <input type='url' id='tester' value='http://htmlunit.sourceforge.net'>\n"
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
    public void typing() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='url' id='foo'>\n"
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
            + "  function test() {\n"
            + "    var input = document.getElementById('tester');\n"
            + "    alert(input.min + '-' + input.max + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='url' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts("true-false")
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
            + "  <input type='url' pattern='.*test.*' id='foo' value='http://test.com'>\n"
            + "  <input type='url' pattern='.*test.*' id='bar' value='http://example.com'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "true-true-true",
            IE = "true-false-false")
    @NotYetImplemented(IE)
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
            + "  <input type='url' pattern='.*test.*' id='foo' value=''>\n"
            + "  <input type='url' pattern='.*test.*' id='bar' value=' '>\n"
            + "  <input type='url' pattern='.*test.*' id='bar2' value='  \t'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"http://example.com", "§§URL§§", "1"},
            IE = {"http://example.com", "§§URL§§second/", "2"})
    public void minLengthValidationInvalid() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='url' minlength='20' id='foo'>\n"
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
        foo.sendKeys("http://example.com");
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
    @Alerts({"http://example.com/test", "§§URL§§second/", "2"})
    public void minLengthValidationValid() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='url' minlength='20' id='foo'>\n"
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
        foo.sendKeys("http://example.com/test");
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
    @Alerts({"http://example.com", "§§URL§§second/", "2"})
    public void maxLengthValidationValid() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='url' maxlength='20' id='foo'>\n"
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
        foo.sendKeys("http://example.com");
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
    @Alerts({"http://example.com/t", "§§URL§§second/", "2"})
    public void maxLengthValidationInvalid() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='url' maxlength='20' id='foo'>\n"
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
        foo.sendKeys("http://example.com/test");
        assertEquals(getExpectedAlerts()[0], foo.getAttribute("value"));
        //valid data
        driver.findElement(By.id("myButton")).click();
        assertEquals(getExpectedAlerts()[1], getMockWebConnection().getLastWebRequest().getUrl());

        assertEquals(Integer.parseInt(getExpectedAlerts()[2]), getMockWebConnection().getRequestCount());
    }
}
