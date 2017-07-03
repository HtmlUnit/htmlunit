/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
 * Tests for {@link HtmlLabel}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlLabelTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLLabelElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<label id='myId'>Item</label>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlLabel.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "chboxclick"})
    public void clickFor() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='checkbox1' onclick='alert(\"labelclick\")'>Toggle checkbox</label>\n"
            + "  <input type='checkbox' id='checkbox1' onclick='alert(\"chboxclick\")'>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label",
                "click listItem1", "click list"})
    public void triggerRadioComplexCase() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='radio1' name='radios' value='1' type='radio'"
                            + "onclick='alert(\"click radio1\");'>\n"
            + "        <span id='radio1Span' onclick='alert(\"click span1\")'>Radio 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Span")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label",
                "click listItem1", "click list"})
    public void triggerRadioComplexCaseHidden() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='radio1' name='radios' value='1' type='radio' style='display: none;'"
                            + "onclick='alert(\"click radio1\");'>\n"
            + "        <span id='radio1Span' onclick='alert(\"click span1\")'>Radio 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Span")).click();
        verifyAlerts(2000000, driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list",
                "click radio1Label", "click listItem1", "click list"})
    public void triggerRadioComplexCaseDisabled() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='radio1' name='radios' value='1' type='radio' disabled"
                            + "onclick='alert(\"click radio1\");'>\n"
            + "        <span id='radio1Span' onclick='alert(\"click span1\")'>Radio 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Span")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }
}
