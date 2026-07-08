/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.libraries.html2canvas;

import java.net.URL;
import java.time.Duration;

import org.htmlunit.WebDriverTestCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for html2canvas.
 *
 * @author Ronald Brill
 */
public class Html2CanvasTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @BeforeAll
    public static void startServer() throws Exception {
        startWebServer("src/test/resources/libraries/html2canvas/", null);
    }

    /**
     * @return the resource base URL
     */
    protected URL getBaseUrl() {
        return URL_FIRST;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void helloWorld() throws Exception {
        readExpectedAlertFromPng("helloworld");

        doTest("html2canvas.html");
    }

    private void doTest(final String filename) throws Exception {
        final WebDriver driver = getWebDriver();

        driver.get(getBaseUrl() + filename);
        driver.findElement(By.id("printButtonId")).click();

        final WebElement textArea = driver.findElement(By.id("myLog"));
        wait(DEFAULT_WAIT_TIME, textArea);
        verifyTextArea2(driver, getExpectedAlerts());
    }

    private static void wait(final Duration maxWaitTime, final WebElement textArea) throws Exception {
        final long maxWait = System.currentTimeMillis() + maxWaitTime.toMillis();

        while (System.currentTimeMillis() < maxWait) {
            final String value = textArea.getDomProperty("value");
            if (value != null && value.startsWith("data:image/png;base64,")) {
                break;
            }

            Thread.sleep(100);
        }
    }
}
