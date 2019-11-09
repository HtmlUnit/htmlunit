/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlDateInput}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlDateInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"text-datetime", "text-Date"},
            CHROME = {"text-datetime", "date-Date"},
            FF = {"text-datetime", "date-Date"})
    public void type() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + "  function test() {\n"
              + "    var input = document.getElementById('input1');\n"
              + "    alert(input.type + '-' + input.getAttribute('type'));\n"
              + "    input = document.getElementById('input2');\n"
              + "    alert(input.type + '-' + input.getAttribute('type'));\n"
              + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body onload='test()'>\n"
              + "  <input id='input1' type='datetime'>\n"
              + "  <input id='input2' type='Date'>\n"
              + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2018-03-22")
    @BuggyWebDriver(CHROME)
    public void typeInto() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + "  function test() {\n"
              + "    var input = document.getElementById('input');\n"
              + "    alert(input.value);\n"
              + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input id='input' type='date'>\n"
              + "  <button id='tester' onclick='test()'>Test</button>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("input")).sendKeys("2018-03-22");
        driver.findElement(By.id("tester")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("--")
    public void minMaxStep() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('tester');\n"
            + "    alert(input.min + '-' + input.max + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='date' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
