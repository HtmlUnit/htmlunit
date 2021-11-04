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
 * Tests for {@link HtmlHead}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlHeadTest extends WebDriverTestCase {

    /**
     * IE and FF both add a head element when it's not present in the HTML code.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HEAD")
    public void addedWhenMissing() throws Exception {
        final String htmlContent = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.firstChild.firstChild.tagName);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(htmlContent);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLHeadElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head id='myId'><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlElement element = toHtmlElement(driver.findElement(By.id("myId")));
            assertTrue(element instanceof HtmlHead);
        }
    }
}
