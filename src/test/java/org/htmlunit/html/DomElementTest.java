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

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link DomElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class DomElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "2"})
    public void getElementsByTagName() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  log(document.f1.getElementsByTagName('input').length);\n"
                + "  log(document.f1.getElementsByTagName('INPUT').length);\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "  <form name='f1'>\n"
                + "    <input>\n"
                + "    <INPUT>\n"
                + "  </form>\n"
                + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(2, page.getForms().get(0).getElementsByTagName("input").size());
            assertEquals(2, page.getForms().get(0).getElementsByTagName("INPUT").size());
        }
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void clickInvisible() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "  <a id='link' style='display: none'>Click me</a>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        Assertions.assertThrows(ElementNotInteractableException.class,
                        () -> driver.findElement(By.id("link")).click());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "true"})
    public void clickFromJavaScript() throws Exception {
        final String html = "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + " function test() {\n"
            + "   try {\n"
            + "     var e = document.getElementById('id1');\n"
            + "     log(e.checked);\n"
            + "     e.click();\n"
            + "     log(e.checked);\n"
            + "   } catch(e) {log(e)}\n"
            + " }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div style='display: none;'>\n"
            + "    <input type='checkbox' id='id1'>\n"
            + "  </div>\n"
            + "</body>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void clickDisabled() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "  <button id='id1' disabled>Click Me</button>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("id1")).click();
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void sendKeysToDisabled() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "  <input id='id1' disabled>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        Assertions.assertThrows(InvalidElementStateException.class,
                () -> driver.findElement(By.id("id1")).sendKeys("Hello"));
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void sendEnterKeyWithHiddenSubmit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <input id='myText' type='text'>\n"
            + "    <input id='myButton' type='submit' style='display: none;'>Submit</input>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myText")).sendKeys(Keys.ENTER);
        if (useRealBrowser()) {
            Thread.sleep(400);
        }

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(URL_SECOND, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void sendEnterKey() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <input id='myText' type='text'>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myText")).sendKeys(Keys.ENTER);
        if (useRealBrowser()) {
            Thread.sleep(400);
        }

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(URL_SECOND, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void clickHiddenSubmit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <input id='myButton' type='submit' style='display: none;'>Submit</input>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent = "second content";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(html);
        Assertions.assertThrows(ElementNotInteractableException.class,
                () -> driver.findElement(By.id("myButton")).click());
    }

}
