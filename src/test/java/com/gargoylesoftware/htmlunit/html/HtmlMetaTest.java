/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlMeta}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlMetaTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLMetaElement]", IE8 = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<meta id='m' http-equiv='content-type' content='text/html'>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('m'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlMeta.class.isInstance(page.getHtmlElementById("m")));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void asText() throws Exception {
        final String html = "<html><head><meta id='m' http-equiv='a' content='b'></head><body></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        final String text = driver.findElement(By.id("m")).getText();
        assertEquals("", text);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayed() throws Exception {
        final String html = "<html><head><meta id='m' http-equiv='a' content='b'></head><body></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        final boolean displayed = driver.findElement(By.id("m")).isDisplayed();
        assertFalse(displayed);
    }

}
