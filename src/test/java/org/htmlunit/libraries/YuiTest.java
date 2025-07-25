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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * Tests for compatibility with the <a href="http://developer.yahoo.com/yui/">YUI JavaScript library</a>.
 *
 * @author Rob Di Marco
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class YuiTest extends WebDriverTestCase {
    private static final Log LOG = LogFactory.getLog(YuiTest.class);

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void logger() throws Exception {
        doTest("logger.html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void animation() throws Exception {
        doTest("animation.html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tabView() throws Exception {
        doTest("tabview.html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dateMath() throws Exception {
        doTest("datemath.html", "btnRun");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void calendar() throws Exception {
        doTest("calendar.html", "btnRun");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void colorPicker() throws Exception {
        doTest("colorpicker.html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Disabled
    public void config() throws Exception {
        // Test currently commented out as there are problems with the YUI test.
        // A bug has been filed against YUI regarding the problems with the test.
        Assertions.fail("YUI test has a bug that causes this to fail.");
        //doTest("config.html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dataSource() throws Exception {
        doTest("datasource.html", "btnRun");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dom() throws Exception {
        doTest("dom.html", Arrays.asList(getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dragDrop() throws Exception {
        doTest("dragdrop.html", Arrays.asList(getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dataTable() throws Exception {
        doTest("datatable.html", "btnRun");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = {"test_blank_image", "test_insertimage", "test_image_props",
                      "test_close_window", "test_regex",
                      "test_createlink", "test_selected_element", "test_dom_path"},
            EDGE = {"test_blank_image", "test_insertimage", "test_image_props",
                    "test_close_window", "test_regex",
                    "test_createlink", "test_selected_element", "test_dom_path"},
            FF = "test_createlink",
            FF_ESR = "test_createlink")
    @HtmlUnitNYI(CHROME = {"test_blank_image", "test_insertimage", "test_image_props",
                           "test_close_window", "test_bold", "test_selected_element",
                           "test_dom_path", "test_createlink"},
            EDGE = {"test_blank_image", "test_insertimage", "test_image_props",
                    "test_close_window", "test_bold", "test_selected_element",
                    "test_dom_path", "test_createlink"},
            FF = {"test_blank_image", "test_insertimage", "test_image_props",
                  "test_bold", "test_createlink", "test_hidden_elements"},
            FF_ESR = {"test_blank_image", "test_insertimage", "test_image_props",
                      "test_bold", "test_createlink", "test_hidden_elements"})
    public void editor() throws Exception {
        doTest("editor.html", Arrays.asList(getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("")
    @HtmlUnitNYI(CHROME = "org.htmlunit.ScriptException: TypeError: Cannot call method \"appendChild\" of null",
            EDGE = "org.htmlunit.ScriptException: TypeError: Cannot call method \"appendChild\" of null",
            FF = "org.htmlunit.ScriptException: TypeError: Cannot call method \"appendChild\" of null",
            FF_ESR = "org.htmlunit.ScriptException: TypeError: Cannot call method \"appendChild\" of null")
    public void yuiLoaderRollup() throws Exception {
        doTest("yuiloader_rollup.html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("test_page_modules")
    public void yuiLoaderConfig() throws Exception {
        // The "test_page_modules" test fails in FF, too, so it's OK.
        doTest("yuiloader_config.html", Arrays.asList(getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void yuiLoader() throws Exception {
        doTest("yuiloader.html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("testConstructor")
    public void module() throws Exception {
        doTest("module.html", Arrays.asList(getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void imageLoader() throws Exception {
        doTest("imageloader.html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void element() throws Exception {
        doTest("element.html");
    }

    private void doTest(final String fileName, final String buttonToClick) throws Exception {
        doTest(fileName, Collections.<String>emptyList(), buttonToClick, 0);
    }

    private void doTest(final String fileName) throws Exception {
        doTest(fileName, Collections.<String>emptyList(), null, 0);
    }

    private void doTest(final String fileName, final List<String> knownFailingTests) throws Exception {
        doTest(fileName, knownFailingTests, null, 0);
    }

    private void doTest(final String fileName, final List<String> knownFailingTests,
            final String buttonToPush, final long timeToWait) throws Exception {

        // final URL url = getClass().getClassLoader().getResource("tests/" + fileName);
        final String url = URL_FIRST + "tests/" + fileName;
        assertNotNull(url);

        final WebDriver driver = getWebDriver();
        try {
            driver.get(url);
        }
        catch (final WebDriverException e) {
            assertTrue(e.getMessage(), e.getMessage().startsWith(getExpectedAlerts()[0]));
            return;
        }

        if (buttonToPush != null) {
            driver.findElement(By.id(buttonToPush)).click();
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        final WebElement logDiv = driver.findElement(By.className("yui-log-bd"));
        final WebElement lastMessage = logDiv.findElement(
            By.xpath("pre[last() and contains(string(.), 'Testing completed')]"));

        LOG.info(lastMessage.getText());

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        final List<WebElement> tests = driver.findElements(By.xpath("//p[span[@class='pass' or @class='fail']]"));
        if (tests.isEmpty()) {
            Assertions.fail("No tests were executed!");
        }

        for (final WebElement pre : tests) {
            final String[] parts = pre.getText().split(" ");
            final String result = parts[0];
            final String testName = parts[1].substring(0, parts[1].length() - 1);
            if ("pass".equalsIgnoreCase(result)) {
                assertTrue("Test case '" + testName + "' is in the known failing list, but passes!", !knownFailingTests
                                .contains(testName));
            }
            else {
                assertTrue("Test case '" + testName + "' is not in the known failing list, but fails!",
                                knownFailingTests.contains(testName));
            }
        }
    }

    /**
     * Performs pre-test initialization.
     * @throws Exception if an error occurs
     */
    @BeforeEach
    public void setUp() throws Exception {
        startWebServer("src/test/resources/libraries/yui/2.3.0", null, null);
    }
}
