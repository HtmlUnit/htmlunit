/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLLabelElement}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLLabelElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void htmlFor() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    document.getElementById('label1').htmlFor = 'checkbox1';\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<label id='label1'>My Label</label>\n"
            + "<input type='checkbox' id='checkbox1'><br>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("checkbox1"));
        assertFalse(checkbox.isSelected());
        driver.findElement(By.id("label1")).click();
        assertTrue(checkbox.isSelected());
    }

    /**
     * Tests that clicking the label by JavaScript does not change 'htmlFor' attribute in FF!!
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true") // in fact not used as JS alerts...
    public void htmlFor_click() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    document.getElementById('label1').htmlFor = 'checkbox1';\n"
            + "}\n"
            + "function delegateClick() {"
            + "  try {\n"
            + "    document.getElementById('label1').click();\n"
            + "  } catch (e) {}\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<label id='label1'>My Label</label>\n"
            + "<input type='checkbox' id='checkbox1'><br>\n"
            + "<input type=button id='button1' value='Test' onclick='delegateClick()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("checkbox1"));
        assertFalse(checkbox.isSelected());
        driver.findElement(By.id("button1")).click();

        assertEquals(getExpectedAlerts()[0], "" + checkbox.isSelected());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "A", "a", "A", "a8", "8Afoo", "8", "@" })
    public void accessKey() throws Exception {
        final String html
            = "<html><body><label id='a1'>a1</label><label id='a2' accesskey='A'>a2</label><script>\n"
            + "var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = 'a';\n"
            + "a2.accessKey = 'A';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = 'a8';\n"
            + "a2.accessKey = '8Afoo';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = '8';\n"
            + "a2.accessKey = '@';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLFormElement]",
            IE8 = "[object]")
    public void form() throws Exception {
        final String html
            = "<html><body><form><label id='a'>a</label></form><script>\n"
            + "alert(document.getElementById('a').form);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
