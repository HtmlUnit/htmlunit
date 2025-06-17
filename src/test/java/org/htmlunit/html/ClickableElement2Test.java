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
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * Tests for various clickable elements.
 *
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ClickableElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void clickOnFocus() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>" + LOG_TITLE_FUNCTION + "</script></head><body>\n"
            + "<form>\n"
            + "  <input type='button' id='textfield1' onfocus='log(1)'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("textfield1")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"click", "click", "dblclick"})
    public void dblClick() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function clickMe() {\n"
            + "    log('click');\n"
            + "  }\n"
            + "  function dblClickMe() {\n"
            + "    log('dblclick');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onclick='clickMe()' ondblclick='dblClickMe()'>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(content);

        final Actions action = new Actions(driver);
        action.doubleClick(driver.findElement(By.id("myBody")));
        action.perform();

        verifyTextArea2(driver, getExpectedAlerts());
    }
}
