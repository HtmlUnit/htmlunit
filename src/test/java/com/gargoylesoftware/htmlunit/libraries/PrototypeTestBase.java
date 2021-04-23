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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Base class for tests for compatibility with <a href="http://prototype.conio.net/">Prototype</a>.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public abstract class PrototypeTestBase extends WebDriverTestCase {

    private static final Log LOG = LogFactory.getLog(PrototypeTestBase.class);
    /** The server. */
    protected static Server SERVER_;

    /**
     * Gets the prototype tested version.
     * @return the version
     */
    protected abstract String getVersion();

    /**
     * Helper, because the element was different for the
     * different versions.
     * @param driver the WebDriver
     * @return the WebElement
     */
    protected boolean testFinished(final WebDriver driver) {
        final List<WebElement> status = driver.findElements(By.cssSelector("div.logsummary"));
        for (final WebElement webElement : status) {
            if (!webElement.getText().contains("errors")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Runs the specified test.
     * @param filename the test file to run
     * @throws Exception if the test fails
     */
    protected void test(final String filename) throws Exception {
        final WebDriver driver = getWebDriver();
        if (!(driver instanceof HtmlUnitDriver)) {
            resizeIfNeeded(driver);
        }
        driver.get(getBaseUrl() + filename);

        // wait
        final long runTime = 60 * DEFAULT_WAIT_TIME;
        final long endTime = System.currentTimeMillis() + runTime;

        while (!testFinished(driver)) {
            Thread.sleep(100);

            if (System.currentTimeMillis() > endTime) {
                fail("Test '" + filename + "' runs too long (longer than " + runTime / 1000 + "s)");
            }
        }

        final String expFileName = StringUtils.substringBeforeLast(filename, ".");
        final String resourcePrefix = "/libraries/prototype/" + getVersion() + "/expected." + expFileName;
        String expected = loadExpectation(resourcePrefix, ".txt");

        WebElement testlog = driver.findElement(By.id("testlog"));
        String actual = testlog.getText();

        try {
            testlog = driver.findElement(By.id("testlog_2"));
            actual = actual + "\n" + testlog.getText();
        }
        catch (final NoSuchElementException e) {
            // ignore
        }

        // ignore Info lines
        expected = expected.replaceAll("Info:.*", "Info: -- skipped for comparison --");
        actual = actual.replaceAll("Info:.*", "Info: -- skipped for comparison --");

        // normalize line break
        expected = expected.replaceAll("\r\n", "\n");
        actual = actual.replaceAll("\r\n", "\n");

        // dump the result page if not ok
        if (System.getProperty(PROPERTY_GENERATE_TESTPAGES) != null && !expected.equals(actual)) {
            final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            final File f = new File(tmpDir, "prototype" + getVersion() + "_result_" + filename);
            FileUtils.writeStringToFile(f, driver.getPageSource(), UTF_8);
            LOG.info("Test result for " + filename + " written to: " + f.getAbsolutePath());
        }

        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @AfterClass
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
    protected String getBaseUrl() {
        return URL_FIRST.toExternalForm();
    }
}
