/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XHtmlPage}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XHtmlPage2Test extends WebDriverTestCase {

    /**
     * Self closing tags are valid in XHtml.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false")
    public void selfClosingDiv() throws Exception {
        final String html
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE html PUBLIC \n"
            + "    \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n"
            + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:xhtml='http://www.w3.org/1999/xhtml'>\n"
            + "<body>\n"
            + "  <div id='div1'/>\n"
            + "  <div id='div2'>not empty</div>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html, URL_FIRST, "application/xhtml+xml", TextUtil.DEFAULT_CHARSET);

        assertEquals("", driver.findElement(By.id("div1")).getText());
        assertEquals("not empty", driver.findElement(By.id("div2")).getText());
    }
}
