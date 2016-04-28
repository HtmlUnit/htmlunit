/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for various clickable elements.
 *
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ClickableElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void clickOnFocus() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form>\n"
            + "    <input type='button' id='textfield1' onfocus='alert(1)'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("textfield1")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("click click dblclick ")
    @BuggyWebDriver({ FF, CHROME })
    public void dblClick() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    document.getElementById('myTextarea').value+='click ';\n"
            + "  }\n"
            + "  function dblClickMe() {\n"
            + "    document.getElementById('myTextarea').value+='dblclick ';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onclick='clickMe()' ondblclick='dblClickMe()'>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(content);

        final Actions action = new Actions(driver);
        action.doubleClick(driver.findElement(By.id("myBody")));
        action.perform();

        assertEquals(getExpectedAlerts()[0], driver.findElement(By.id("myTextarea")).getAttribute("value"));
    }
}
