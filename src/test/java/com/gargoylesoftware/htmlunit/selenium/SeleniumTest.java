/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.selenium;

import org.junit.Before;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * The parent class of Selenium tests.
 *
 * @author Ahmed Ashour
 */
public class SeleniumTest extends WebDriverTestCase {

    /**
     * Starts the web server.
     */
    @Override
    @Before
    public void beforeTest() {
        try {
            startWebServer("src/test/resources/selenium", null, null);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the {@code WebDriver} after loading the URL with the specified relative path.
     * @param relativePath the relative path
     * @return the {@code WebDriver}
     */
    protected WebDriver getWebDriver(final String relativePath) {
        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + relativePath);
        return driver;
    }
}
