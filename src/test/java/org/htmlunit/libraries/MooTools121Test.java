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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Tests for compatibility with version 1.2.1 of the <a href="http://mootools.net/">MooTools JavaScript library</a>.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class MooTools121Test extends WebDriverTestCase {

    private static Server SERVER_;

    /**
     * @throws Exception if an error occurs
     */
    @BeforeAll
    public static void startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/mootools/1.2.1", null);
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
     * @throws Exception if an error occurs
     */
    @Alerts({"364", "1", "0",
             "should return the function bound to an object with multiple arguments"})
    @Test
    public void mooTools() throws Exception {
        final WebDriver driver = getWebDriver();
        driver.get(URL_FIRST + "Specs/index.html");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        driver.findElement(By.xpath("id('progress')[text() = '100']"));
        // usually this need 40s but sometimes our build machine is slower
        // this is not a performance test, we only like to ensure that all
        // functionality is running

        final List<WebElement> failed = driver.findElements(By.xpath("//li[@class = 'exception']/h4"));
        final List<String> failures = new ArrayList<>();
        for (final WebElement elt : failed) {
            failures.add(elt.getText());
        }

        // final File tmpFile = File.createTempFile("htmlunit", "mootools.html");
        // System.out.println(tmpFile.getAbsolutePath());
        // FileUtils.writeStringToFile(tmpFile, driver.getPageSource());

        assertEquals(Arrays.copyOfRange(getExpectedAlerts(), 3, getExpectedAlerts().length), failures);

        assertEquals(getExpectedAlerts()[0], driver.findElement(By.id("total_examples")).getText());
        assertEquals(getExpectedAlerts()[1], driver.findElement(By.id("total_failures")).getText());
        assertEquals(getExpectedAlerts()[2], driver.findElement(By.id("total_errors")).getText());
    }
}
