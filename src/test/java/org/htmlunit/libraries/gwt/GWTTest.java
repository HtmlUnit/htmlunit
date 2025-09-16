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
package org.htmlunit.libraries.gwt;

import org.htmlunit.WebDriverTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Tests for <a href="https://www.gwtproject.org/">GWT Project</a>.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public abstract class GWTTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @BeforeEach
    public void startSesrver() throws Exception {
        startWebServer("src/test/resources/libraries/GWT/" + getDirectory(), null, null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @AfterEach
    public void stopServer() throws Exception {
        stopWebServers();
    }

    /**
     * @return the GWT directory being tested
     */
    public abstract String getDirectory();

    protected WebDriver loadGWTPage(final String url, final String elementXPathToWaitFor) throws Exception {
        final WebDriver driver = getWebDriver();
        driver.get(url);

        final WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_TIME);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementXPathToWaitFor)));

        return driver;
    }

    protected void textToBePresentInElement(final WebDriver driver, final WebElement elem, final String expected) {
        final WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_TIME);
        wait.until(ExpectedConditions.textToBePresentInElement(elem, expected));
    }

    protected void textToBePresentInElementLocated(final WebDriver driver, final By by, final String expected) {
        final WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_TIME);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(by, expected));
    }
}
