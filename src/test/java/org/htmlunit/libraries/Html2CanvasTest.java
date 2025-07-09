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

import java.net.URL;
import java.time.Duration;

import org.eclipse.jetty.server.Server;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebServerTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
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

    /** The server. */
    protected static Server SERVER_;

    /**
     * @throws Exception if an error occurs
     */
    @BeforeAll
    public static void startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/html2canvas/", null);
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
     * @return the resource base URL
     */
    protected URL getBaseUrl() {
        return URL_FIRST;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64")
    @HtmlUnitNYI(CHROME = "nyi",
            EDGE = "nyi",
            FF = "nyi",
            FF_ESR = "nyi")
    public void helloWorld() throws Exception {
        // this does not produce an image in html unit so far
        // have added this test to not forget it
        doTest("html2canvas.html");
    }

    private void doTest(final String filename) throws Exception {
        final WebDriver driver = getWebDriver();

        driver.get(getBaseUrl() + filename);
        driver.findElement(By.id("printButtonId")).click();

        // verifyTextArea2(driver, getExpectedAlerts());

        final WebElement textArea = driver.findElement(By.id("myLog"));
        verify(DEFAULT_WAIT_TIME, textArea);
    }

    private void verify(final Duration maxWaitTime, final WebElement textArea) throws Exception {
        final long maxWait = System.currentTimeMillis() + maxWaitTime.toMillis();

        String result = "nyi";
        while (System.currentTimeMillis() < maxWait) {
            final String value = textArea.getDomProperty("value");
            if (value != null && value.startsWith("data:image/png;base64,")) {
                result = value;
                break;
            }

            Thread.sleep(100);
        }

        Assertions.assertTrue(result.startsWith(getExpectedAlerts()[0]),
                "'" + result + "' does not start with '" + getExpectedAlerts()[0] + "'");
    }
}
