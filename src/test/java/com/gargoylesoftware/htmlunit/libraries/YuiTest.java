/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.libraries;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for compatibility with the <a href="http://developer.yahoo.com/yui/">YUI JavaScript library</a>.
 *
 * @version $Revision$
 * @author Rob Di Marco
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
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
    @NotYetImplemented(IE8)
    public void calendar() throws Exception {
        doTest("calendar.html", "btnRun");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(IE8)
    public void colorPicker() throws Exception {
        doTest("colorpicker.html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Ignore
    public void config() throws Exception {
        // Test currently commented out as there are problems with the YUI test.
        // A bug has been filed against YUI regarding the problems with the test.
        // See http://sourceforge.net/tracker/index.php?func=detail&aid=1788014&group_id=165715&atid=836476
        // for more details.
        fail("YUI test has a bug that causes this to fail.");
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
    @NotYetImplemented(IE8)
    public void dom() throws Exception {
        doTest("dom.html", Arrays.asList(getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE8 = { "test_startDrag", "test_dragOver", "test_containerScroll" })
    @NotYetImplemented(IE8)
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
    @Alerts(DEFAULT = "test_createlink",
            FF17 = { "test_createlink", "test_regex" },
            IE11 = { "test_bold", "test_createlink" })
    @NotYetImplemented
    public void editor() throws Exception {
        doTest("editor.html", Arrays.asList(getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented
    public void yuiLoaderRollup() throws Exception {
        doTest("yuiloader_rollup.html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "test_page_modules")
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
    @Alerts(DEFAULT = "testConstructor",
            FF24 = { "testConstructor", "test_regex" })
    public void module() throws Exception {
        doTest("module.html", Arrays.asList(getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(IE8)
    public void imageLoader() throws Exception {
        doTest("imageloader.html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(IE8)
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
        final String url = "http://localhost:" + PORT + "/tests/" + fileName;
        assertNotNull(url);

        final WebDriver driver = getWebDriver();
        driver.get(url);

        if (buttonToPush != null) {
            driver.findElement(By.id(buttonToPush)).click();
        }

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        final WebElement logDiv = driver.findElement(By.className("yui-log-bd"));
        final WebElement lastMessage = logDiv.findElement(
            By.xpath("pre[last() and contains(string(.), 'Testing completed')]"));

        LOG.info(lastMessage.getText());

        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        final List<WebElement> tests = driver.findElements(By.xpath("//p[span[@class='pass' or @class='fail']]"));
        if (tests.isEmpty()) {
            fail("No tests were executed!");
        }

        for (final WebElement pre : tests) {
            final String[] parts;
            try {
                parts = pre.getText().split(" ");
            }
            catch (final StaleElementReferenceException e) {
                continue; // happens for FF17 on imageLoader test
            }
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
    @Before
    public void setUp() throws Exception {
        startWebServer("src/test/resources/libraries/yui/2.3.0", null, null);
    }
}
