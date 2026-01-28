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
package org.htmlunit.libraries.mochikit;

import java.time.Duration;

import org.htmlunit.WebDriverTestCase;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for compatibility with <a href="http://mochikit.com">MochiKit</a>.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill#
 */
public abstract class MochiKitTest extends WebDriverTestCase {

    /**
     * @return the src folder containing mochikit sources
     */
    public abstract String srcFolder();

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void async() throws Exception {
        doTest("Async");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void base() throws Exception {
        doTest("Base");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void color() throws Exception {
        doTest("Color");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void dateTime() throws Exception {
        doTest("DateTime");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void dom() throws Exception {
        doTest("DOM");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domSafari() throws Exception {
        doTest("DOM-Safari");
    }

    // /**
    //  * @throws Exception if the test fails
    //  */
    // have to investigate why this fails in HtmlUnit
    // @Test
    // public void dragAndDrop() throws Exception {
    //     doTest("DragAndDrop");
    // }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void format() throws Exception {
        doTest("Format");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void iter() throws Exception {
        doTest("Iter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void logging() throws Exception {
        doTest("Logging");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void mochiKit() throws Exception {
        doTest("MochiKit");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void selector() throws Exception {
        doTest("Selector");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void signal() throws Exception {
        doTest("Signal");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void style() throws Exception {
        doTest("Style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void visual() throws Exception {
        doTest("Visual");
    }

    private void doTest(final String testName) throws Exception {
        final String url = URL_FIRST + "tests/test_MochiKit-" + testName + ".html";
        assertNotNull(url);

        final WebDriver driver = getWebDriver();
        driver.get(url);

        // make single test results visible
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.findElement(By.linkText("Toggle passed tests")).click();
        driver.findElement(By.linkText("Toggle failed tests")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        final String expected = loadExpectation(testName)
                                .trim()
                                .replace("\r\n", "\n");
        final WebElement div = driver.findElement(By.xpath("//div[@class = 'tests_report']"));

        assertNotNull(div);
        final String actual = div.getText()
                                    .trim()
                                    .replace("\n\n", "\n");
        assertEquals(expected.trim(), actual);
    }

    private String loadExpectation(final String testName) throws Exception {
        final String resourcePrefix = "/libraries/MochiKit/" + srcFolder() + "/test-" + testName;
        return loadExpectation(resourcePrefix, ".expected.txt");
    }
}
