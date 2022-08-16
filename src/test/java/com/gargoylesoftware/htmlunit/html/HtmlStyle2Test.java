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
 * Tests for {@link HtmlStyle}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlStyle2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLStyleElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<style type='text/css' id='myId'>\n"
            + "img { border: 0px }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertTrue(HtmlStyle.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void getText() throws Exception {
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <title>foo</title>\n"
                + "  <style type='text/css' id='s'>\n"
                + "    img { border: 0px }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.findElement(By.id("s")).getText();
        assertEquals("", text);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayed() throws Exception {
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <title>foo</title>\n"
                + "  <style type='text/css' id='s'>\n"
                + "    img { border: 0px }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        final boolean displayed = driver.findElement(By.id("s")).isDisplayed();
        assertFalse(displayed);
    }
}
