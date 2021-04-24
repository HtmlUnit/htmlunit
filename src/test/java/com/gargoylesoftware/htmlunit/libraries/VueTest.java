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
package com.gargoylesoftware.htmlunit.libraries;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * <p>Tests for <a href="https://vuejs.org/">Vue.js</a>.</p>
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class VueTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello World")
    public void hello() throws Exception {
        final String url = URL_FIRST + "hello.html";

        final WebDriver driver = getWebDriver();
        driver.get(url);

        assertEquals(getExpectedAlerts()[0], driver.findElement(By.id("app")).getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello World")
    public void helloMin() throws Exception {
        final String url = URL_FIRST + "hello.min.html";

        final WebDriver driver = getWebDriver();
        driver.get(url);

        assertEquals(getExpectedAlerts()[0], driver.findElement(By.id("app")).getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello from HtmlUnit!")
    public void helloButton() throws Exception {
        final String url = URL_FIRST + "hello_button.html";

        final WebDriver driver = getWebDriver();
        driver.get(url);

        driver.findElement(By.id("tester")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello from HtmlUnit!")
    public void helloButtonMin() throws Exception {
        final String url = URL_FIRST + "hello_button.min.html";

        final WebDriver driver = getWebDriver();
        driver.get(url);

        driver.findElement(By.id("tester")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Performs pre-test initialization.
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        startWebServer("src/test/resources/libraries/vue/hello_world", null, null);
    }
}
