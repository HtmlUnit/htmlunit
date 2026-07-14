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
package org.htmlunit.libraries.echarts;

import java.time.Duration;

import org.htmlunit.WebDriverTestCase;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for Apache ECharts.
 *
 * @author Ronald Brill
 */
public class ECharts_6_1_0_Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void sample() throws Exception {
        startWebServer("src/test/resources/libraries/echarts/echarts_6_1_0", null);

        readExpectedAlertFromPng("sample");

        final WebDriver driver = getWebDriver();
        driver.get(URL_FIRST + "index.html");

        driver.findElement(By.id("printButtonId")).click();

        final WebElement textArea = driver.findElement(By.id("myLog"));
        waitForOutput(DEFAULT_WAIT_TIME.multipliedBy(1_000), textArea);

        verifyTextArea2(driver, getExpectedAlerts());
    }

    private static void waitForOutput(final Duration maxWaitTime, final WebElement textArea) throws Exception {
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
