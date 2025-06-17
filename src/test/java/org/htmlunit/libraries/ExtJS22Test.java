/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.libraries;

import java.util.List;

import org.eclipse.jetty.server.Server;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebServerTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for 2.2 version of <a href="http://www.extjs.com/">Ext JS</a>.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ExtJS22Test extends WebDriverTestCase {

    private static Server SERVER_;

    /**
     * @throws Exception if an error occurs
     */
    @BeforeAll
    public static void startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/ExtJS/" + getVersion(), null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @AfterAll
    public static void stopServer() throws Exception {
        if (SERVER_ != null) {
            SERVER_.stop();
            SERVER_.destroy();
            SERVER_ = null;
        }
    }

    /**
     * Returns the Ext JS version being tested.
     * @return the Ext JS version being tested
     */
    protected static String getVersion() {
        return "2.2";
    }

    /**
     * Loads the Ext JS test page using the specified example.
     *
     * @param example the example name
     * @param htmlName the page name
     * @return the loaded page
     * @throws Exception if an error occurs
     */
    protected WebDriver getPage(final String example, final String htmlName) throws Exception {
        final WebDriver driver = getWebDriver();
        driver.get(URL_FIRST + "examples/" + example + "/" + htmlName + ".html");
        return driver;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void core_templates() throws Exception {
        final WebDriver driver = getPage("core", "templates");
        final List<WebElement> buttons = driver.findElements(By.xpath("//button"));
        final List<WebElement> divs = driver.findElements(By.xpath("//div[@class='x-panel-body']"));
        assertEquals(2, buttons.size());
        assertEquals(2, divs.size());
        assertEquals("Apply the template to see results here", divs.get(0).getText());
        assertEquals("Apply the template to see results here", divs.get(1).getText());

        buttons.get(0).click();
        assertEquals("Name: Jack Slocum\n" + "Company: Ext JS, LLC\n"
            + "Location: Cleveland, Ohio", divs.get(0).getText());
        assertEquals("Apply the template to see results here", divs.get(1).getText());

        buttons.get(1).click();
        assertEquals("Name: Jack Slocum\n" + "Company: Ext JS, LLC\n"
            + "Location: Cleveland, Ohio", divs.get(0).getText());
        assertEquals("Name: Jack Slocum\n" + "Company: Ext JS, LLC\n"
            + "Location: Cleveland, Ohio\n"
            + "Kids:\n" + "1. Jack Slocum's kid - Sara Grace\n"
            + "2. Jack Slocum's kid - Zachary", divs.get(1).getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void core_spotlight() throws Exception {
        final WebDriver driver = getPage("core", "spotlight");
        final List<WebElement> buttons = driver.findElements(By.xpath("//button"));
        assertEquals(4, buttons.size());
        assertEquals("Start", buttons.get(0).getText());
        assertEquals("Next Panel", buttons.get(1).getText());
        assertEquals("Next Panel", buttons.get(2).getText());
        assertEquals("Done", buttons.get(3).getText());

        assertTrue(core_spotlight_isDisabled(buttons.get(1)));
        assertTrue(core_spotlight_isDisabled(buttons.get(2)));
        assertTrue(core_spotlight_isDisabled(buttons.get(3)));

        buttons.get(0).click();
        Thread.sleep(200);
        assertFalse(core_spotlight_isDisabled(buttons.get(1)));
        assertTrue(core_spotlight_isDisabled(buttons.get(2)));
        assertTrue(core_spotlight_isDisabled(buttons.get(3)));

        buttons.get(1).click();
        Thread.sleep(200);
        assertTrue(core_spotlight_isDisabled(buttons.get(1)));
        assertFalse(core_spotlight_isDisabled(buttons.get(2)));
        assertTrue(core_spotlight_isDisabled(buttons.get(3)));

        buttons.get(2).click();
        Thread.sleep(200);
        assertTrue(core_spotlight_isDisabled(buttons.get(1)));
        assertTrue(core_spotlight_isDisabled(buttons.get(2)));
        assertFalse(core_spotlight_isDisabled(buttons.get(3)));

        buttons.get(3).click();
        Thread.sleep(200);
        assertTrue(core_spotlight_isDisabled(buttons.get(1)));
        assertTrue(core_spotlight_isDisabled(buttons.get(2)));
        assertTrue(core_spotlight_isDisabled(buttons.get(3)));
    }

    private static boolean core_spotlight_isDisabled(final WebElement button) {
        final WebElement table = button.findElement(By.xpath("ancestor::table[1]"));
        return table.getAttribute("class").contains("disabled");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello from the Ext console.")
    public void debug_console() throws Exception {
        final WebDriver driver = getPage("debug", "debug-console");

        final List<WebElement> anchors = driver.findElements(By.xpath("//a"));
        assertEquals(2, anchors.size());
        anchors.get(1).click();
        assertEquals(getExpectedAlerts()[0],
                driver.findElement(By.xpath("//div[starts-with(text(), 'Hello')][1]")).getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void desktop_desktop() throws Exception {
        final WebDriver driver = getPage("desktop", "desktop");
        driver.findElement(By.xpath("//button[1]")).click();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void form_absform() throws Exception {
        final WebDriver driver = getPage("form", "absform");
        final String content = driver.findElement(By.xpath("//html/body")).getText();
        assertTrue(content.contains("Resize Me"));
        assertTrue(content.contains("Send To:"));
        assertTrue(content.contains("Subject:"));
        assertTrue(content.contains("Cancel"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void form_anchoring() throws Exception {
        final WebDriver driver = getPage("form", "anchoring");
        final String content = driver.findElement(By.xpath("//html/body")).getText();
        assertTrue(content.contains("Send To:"));
        assertTrue(content.contains("Subject:"));
        assertTrue(content.contains("Send"));
        assertTrue(content.contains("Cancel"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void grid_binding() throws Exception {
        final WebDriver driver = getPage("grid", "binding");

        // usually this need 1s but sometimes our build machine is slower
        // this is not a performance test, we only like to ensure that all
        // functionality is running
        Thread.sleep(DEFAULT_WAIT_TIME.multipliedBy(2).toMillis());

        final WebElement detailPanel = driver.findElement(By.id("detailPanel"));
        final WebElement resultsDiv = detailPanel.findElement(By.xpath("div/div[1]"));
        assertEquals("Please select a book to see additional details.", resultsDiv.getText());

        final WebElement firstRowDiv = driver.findElement(By.xpath("//div[@class='x-grid3-body']/div[1]"));

        firstRowDiv.click();
        assertEquals("Title: Master of the Game\n"
                + "Author: Sidney Sheldon\n"
                + "Manufacturer: Warner Books\n"
                + "Product Group: Book", resultsDiv.getText());
    }

}
