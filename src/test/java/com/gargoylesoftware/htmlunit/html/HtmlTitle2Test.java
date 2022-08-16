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
 * Tests for {@link HtmlTitle}.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlTitle2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLTitleElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head><title id='myId'>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertTrue(HtmlTitle.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayed() throws Exception {
        final String html = "<html><head><title id='t'>Title</title></head><body></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        final boolean displayed = driver.findElement(By.id("t")).isDisplayed();
        assertFalse(displayed);
    }
}
