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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlMenu}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlMenuTest extends WebDriverTestCase {

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("First Item\nSecond Item")
    public void getVisibleText() throws Exception {
        final String html = "<html><head></head>\n"
                + "<body>\n"
                + "  <menu id='tester'>\n"
                + "    <li>First Item</li>\n"
                + "    <li>Second Item</li>\n"
                + "  </menu>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLMenuElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <menu id='myId'>\n"
            + "    <li>First Item</li>\n"
            + "    <li>Second Item</li>\n"
            + "  </menu>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlMenu.class.isInstance(page.getHtmlElementById("myId")));
        }
    }
}
