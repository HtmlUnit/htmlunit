/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Proof of concept for using WebDriver to run (some) HtmlUnit tests and have the possibility
 * to check in "real" browsers if our expectations are correct.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class SimpleWebDriverTest extends WebDriverTestCase {
    /**
     * Test event order.
     * @throws Exception if the test fails
     */
    @Test
    public void eventOrder() throws Exception {
        final File testsDir = new File("src/test/resources/testfiles");
        final File testFile = new File(testsDir, "testEventOrder.html");

        final WebDriver webDriver = getWebDriver();

        webDriver.get(testFile.toURI().toURL().toExternalForm());
        final WebElement textField = webDriver.findElement(By.id("foo"));
        textField.click(); // to give focus
        textField.sendKeys("a");
        webDriver.findElement(By.id("other")).click();

        // verifications
        assertEquals(getExpectedEntries(), getEntries("log"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickEvents() throws Exception {
        final File testsDir = new File("src/test/resources/testfiles");
        final File testFile = new File(testsDir, "testClickEvents.html");

        final WebDriver webDriver = getWebDriver();

        webDriver.get(testFile.toURI().toURL().toExternalForm());

        webDriver.findElement(By.id("testSpan")).click();
        webDriver.findElement(By.id("testInput")).click();
        webDriver.findElement(By.id("testImage")).click();
        webDriver.findElement(By.id("testTextarea")).click();

        // verifications
        assertEquals(getExpectedEntries().toString(), getEntries("log").toString());
    }

    /**
     * Test handling of &lt;script event=".." for=".."&gt;.
     * @throws Exception if the test fails
     */
    @Test
    public void scriptEventFor() throws Exception {
        final File testsDir = new File("src/test/resources/testfiles");
        final File testFile = new File(testsDir, "testScriptEventFor.html");

        final WebDriver webDriver = getWebDriver();

        webDriver.get(testFile.toURI().toURL().toExternalForm());
        webDriver.findElement(By.id("div1")).click();
        webDriver.findElement(By.id("div2")).click();

        // verifications
        assertEquals(getExpectedEntries(), getEntries("log"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void innerHTMLwithQuotes() throws Exception {
        doTest("testInnerHTML_quotesInAttribute.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void objectProperties() throws Exception {
        doTest("objectProperties.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void document_xxx_formAccess() throws Exception {
        doTest("testDocument.xxx_accessToForm.html");
    }

    private void doTest(final String fileName) throws Exception {
        final File testsDir = new File("src/test/resources/testfiles");
        final File testFile = new File(testsDir, fileName);

        getWebDriver().get(testFile.toURI().toURL().toExternalForm());

        // verifications
        assertEquals(getExpectedEntries().toString(), getEntries("log").toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    public void fireEventCopyTemplateProperties() throws Exception {
        doTest("testFireEvent_initFromTemplate.html");
    }
}
