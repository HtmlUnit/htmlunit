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
package org.htmlunit.libraries;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * <p>Tests for <a href="https://www.polymer-project.org/">www.polymer-project.org</a>.</p>
 *
 * @author Ronald Brill
 */
public class PolymerWebComponentsTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @BeforeAll
    public static void startServer() throws Exception {
        startWebServer("src/test/resources/libraries/polymer/0_6_1", null, null);
    }

    /**
     * See https://github.com/HtmlUnit/htmlunit/issues/23.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello Unicorn :)")
    public void hello() throws Exception {
        final String url = URL_FIRST + "index.html";

        final WebDriver driver = getWebDriver();
        driver.get(url);

        verify(() -> driver.findElement(By.tagName("body")).getText(), getExpectedAlerts()[0]);
    }
}
