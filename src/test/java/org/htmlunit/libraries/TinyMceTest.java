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
import java.util.List;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * <p>Tests for compatibility with <a href="http://tinymce.moxiecode.com/">TinyMCE</a>.</p>
 *
 * <p>TODO: more tests to add</p>
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class TinyMceTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"348", "0"})
    public void api() throws Exception {
        test("api", Integer.parseInt(getExpectedAlerts()[0]), Integer.parseInt(getExpectedAlerts()[1]));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"89", "0"},
            CHROME = {"89", "1"},
            EDGE = {"89", "1"})
    @HtmlUnitNYI(CHROME = {"70", "50"},
            EDGE = {"70", "50"},
            FF = {"70", "50"},
            FF_ESR = {"70", "50"})
    public void basic() throws Exception {
        test("basic", Integer.parseInt(getExpectedAlerts()[0]), Integer.parseInt(getExpectedAlerts()[1]));
    }

    private void test(final String fileName, final int expectedTotal, final int expectedFailed) throws Exception {
        final String url = URL_FIRST + "tests/" + fileName + ".html";
        assertNotNull(url);

        final WebDriver driver = getWebDriver();
        driver.get(url);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        final WebElement result = driver.findElement(By.id("testresult"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        final WebElement totalSpan = result.findElement(By.xpath("./span[@class='all']"));
        final int total = Integer.parseInt(totalSpan.getText());
        assertEquals(expectedTotal, total);

        final List<WebElement> failures = driver.findElements(By.xpath("//li[@class='fail']"));
        final StringBuilder msg = new StringBuilder();
        for (final WebElement failure : failures) {
            msg.append(failure.getText());
            msg.append("\n\n");
        }

        final WebElement failedSpan = result.findElement(By.xpath("./span[@class='bad']"));
        final int failed = Integer.parseInt(failedSpan.getText());
        assertEquals(expectedFailed, failed);
    }

    /**
     * Performs pre-test initialization.
     * @throws Exception if an error occurs
     */
    @BeforeEach
    public void setUp() throws Exception {
        startWebServer("src/test/resources/libraries/tinymce/3.2.7", null, null);
    }
}
