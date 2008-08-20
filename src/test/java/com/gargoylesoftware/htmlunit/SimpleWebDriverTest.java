/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Proof of concept for using WebDriver to run (some) HtmlUnit tests and have the possibility
 * to check in "real" browsers if our expectations are correct.
 * <p>
 * This test runs with HtmlUnit unless the system property "htmlunit.webdriver" is set to "firefox"
 * in which case the test will run in the "real" firefox browser.
 * </p>
 * <p>
 * Examples:
 * mvn -Dtest=SimpleWebDriverTest (runs the test with HtmlUnit)<br/>
 * mvn -Dtest=SimpleWebDriverTest -Dhtmlunit.webdriver=firefox (runs the test with Firefox from the path)<br/>
 * mvn -Dtest=SimpleWebDriverTest -Dhtmlunit.webdriver=firefox -Dwebdriver.firefox.bin=/home/marc/misc/firefox/firefox-3.0.1/firefox (runs the test with the specified Firefox version)<br/>
 * </p> 
 * @version $Revision$
 * @author Marc Guillemot
 */
public class SimpleWebDriverTest extends WebTestCase {
    private WebDriver webDriver_;

    /**
     * Configures the driver.
     */
    @Before
    public void setUp() {
        webDriver_ = buildWebDriver();
    }

    /**
     * Closes the driver.
     */
    @After
    public void tearDown() {
        webDriver_.close();
    }

    /**
     * Test event order.
     * In fact the assumption is is correct for FF3 and IE but not for FF2.
     * @throws Exception if the test fails
     */
    @Test
    public void eventOrder() throws Exception {
        final File testsDir = new File("src/test/resources/testfiles");
        final File testFile = new File(testsDir, "testEventOrder.html");

        webDriver_.get(testFile.toURL().toExternalForm());
        final WebElement textField = webDriver_.findElement(By.id("foo"));
        textField.click(); // to give focus
        textField.sendKeys("a");
        webDriver_.findElement(By.id("other")).click();

        // verifications
        assertEquals(getEntries("expected"), getEntries("log"));
    }

    private List<String> getEntries(final String id) {
        final List<WebElement> log = webDriver_.findElements(By.xpath("id('" + id + "')/li"));
        final List<String> entries = new ArrayList<String>();
        for (final WebElement elt : log) {
            entries.add(elt.getText());
        }

        return entries;
    }

    private WebDriver buildWebDriver() {
        if ("firefox".equalsIgnoreCase(System.getProperty("htmlunit.webdriver"))) {
            return new FirefoxDriver();
        }
        // TODO: IEDriver
        else {
            final HtmlUnitDriver driver = new HtmlUnitDriver();
            driver.setJavascriptEnabled(true);
            return driver;
        }
    }
}
