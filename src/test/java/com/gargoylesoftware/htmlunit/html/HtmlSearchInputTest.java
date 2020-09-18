/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlSearchInput}.
 *
 * @author Marc Guillemot
 * @author Anton Demydenko
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
    @NotYetImplemented(IE)
    public void minLengthValidation() throws Exception {
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

        final WebDriver driver = loadPage2(html, URL_FIRST);

        final WebElement foo = driver.findElement(By.id("foo"));
        foo.sendKeys("1234");
        assertEquals("1234", foo.getAttribute("value"));
        //invalid data
        driver.findElement(By.id("myButton")).click();
        assertEquals(URL_FIRST.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        //valid data
        foo.sendKeys("567890");
        assertEquals("1234567890", foo.getAttribute("value"));
        driver.findElement(By.id("myButton")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void maxLengthValidation() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND
                    + "' method='" + HttpMethod.POST + "'>\n"
            + "    <input type='search' maxlength='5' id='foo'>\n"
            + "    <input type='search' maxlength='5' id='bar'>\n"
            + "    <button id='myButton' type='submit'>Submit</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html, URL_FIRST);

        final WebElement foo = driver.findElement(By.id("foo"));
        final WebElement bar = driver.findElement(By.id("bar"));
        foo.sendKeys("12345");
        bar.sendKeys("1234567890");

        assertEquals("12345", foo.getAttribute("value"));
        assertEquals("12345", bar.getAttribute("value"));

        driver.findElement(By.id("myButton")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
    }
}
