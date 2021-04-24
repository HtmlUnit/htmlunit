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
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * <p>Tests for <a href="https://www.polymer-project.org/">www.polymer-project.org</a>.</p>
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class PolymerWebComponentsTest extends WebDriverTestCase {

    /**
     * See https://github.com/HtmlUnit/htmlunit/issues/23.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello Unicorn :)")
    @HtmlUnitNYI(CHROME = "",
            EDGE = "",
            FF = "",
            FF78 = "",
            IE = "")
    public void hello() throws Exception {
        final String url = URL_FIRST + "index.html";

        final WebDriver driver = getWebDriver();
        driver.get(url);

        // for real FF
        Thread.sleep(200);

        assertEquals(getExpectedAlerts()[0], driver.findElement(By.tagName("body")).getText());
    }

    /**
     * Performs pre-test initialization.
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        startWebServer("src/test/resources/libraries/polymer/0_6_1", null, null);
    }
}
