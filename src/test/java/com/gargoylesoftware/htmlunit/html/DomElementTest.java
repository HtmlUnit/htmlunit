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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * Tests for {@link DomElement}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public final class DomElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2", "2" })
    public void getElementsByTagName() throws Exception {
        final String html = "<html><head><script>\n"
                + "function test() {\n"
                + "    alert(document.f1.getElementsByTagName('input').length);\n"
                + "    alert(document.f1.getElementsByTagName('INPUT').length);\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "  <form name='f1'>\n"
                + "    <input>\n"
                + "    <INPUT>\n"
                + "  </form>\n"
                + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final WebWindow webWindow = getWebWindowOf((HtmlUnitDriver) driver);
            final HtmlPage page = (HtmlPage) webWindow.getEnclosedPage();
            assertEquals(2, page.getForms().get(0).getElementsByTagName("input").size());
            assertEquals(2, page.getForms().get(0).getElementsByTagName("INPUT").size());
        }
    }
}
