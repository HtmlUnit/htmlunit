/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlOrderedList}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlOrderedList2Test extends WebDriverTestCase {

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nsecond item\nsomething without li node\nthird item")
    public void getVisibleText() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <ol id='tester'>\n"
            + "    <li>first item</li>\n"
            + "    <li>second item</li>\n"
            + "something without li node\n"
            + "    <li>third item</li>\n"
            + "  </ol>\n"
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
    @Alerts("[object HTMLOListElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <ol id='myId'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertTrue(HtmlOrderedList.class.isInstance(page.getHtmlElementById("myId")));
        }
    }
}
