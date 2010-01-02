/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlAnchor}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlAnchor2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "hi", "%28%29" })
    public void href_js_escaping() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function sayHello(text) {\n"
            + "    alert(text);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <a id='myAnchor' href=\"javascript:sayHello%28'hi'%29\">My Link</a>\n"
            + "  <input id='myButton' type=button onclick=\"javascript:sayHello('%28%29')\" value='My Button'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myAnchor")).click();
        driver.findElement(By.id("myButton")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "(*%a", "%28%A" })
    public void href_js_escaping2() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function sayHello(text) {\n"
            + "    alert(text);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <a id='myAnchor' href=\"javascript:sayHello%28'%28%2a%a'%29\">My Link</a>\n"
            + "  <input id='myButton' type=button onclick=\"javascript:sayHello('%28%A')\" value='My Button'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myAnchor")).click();
        driver.findElement(By.id("myButton")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }
}
