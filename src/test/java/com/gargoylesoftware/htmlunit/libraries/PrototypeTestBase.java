/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * Base class for tests for compatibility with <a href="http://prototype.conio.net/">Prototype</a>.
 *
 * @version $Revision$
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
    protected WebElement getSummaryElement(final WebDriver driver) {
        final WebElement status = driver.findElement(By.cssSelector("div.logsummary"));
        return status;
    }

    /**
     * Runs the specified test.
     * @param filename the test file to run
     * @throws Exception if the test fails
     */
    protected void test(final String filename) throws Exception {
        final WebDriver driver = getWebDriver();
        if (!(driver instanceof HtmlUnitDriver)) {
            try {
                driver.manage().window().setSize(new Dimension(1272, 768));
            }
            catch (final WebDriverException e) {
                // ChromeDriver version 0.5 (Mar 26, 2013) does not support the setSize command
                LOG.warn(e.getMessage(), e);
            }
        }
        driver.get(getBaseUrl() + filename);

        // wait
        final long runTime = 60 * DEFAULT_WAIT_TIME;
        final long endTime = System.currentTimeMillis() + runTime;
        final WebElement status = getSummaryElement(driver);
        while (!status.getText().contains("errors")) {
            Thread.sleep(100);

            if (System.currentTimeMillis() > endTime) {
                fail("Test '" + filename + "' runs too long (longer than " + runTime / 1000 + "s)");
            }
        }

        String expected = getExpectations(getBrowserVersion(), filename);
        WebElement testlog = driver.findElement(By.id("testlog"));
        String actual = getText(testlog);

        try {
            testlog = driver.findElement(By.id("testlog_2"));
            actual = actual + "\n" + getText(testlog);
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
            FileUtils.writeStringToFile(f, driver.getPageSource(), "UTF-8");
            LOG.info("Test result for " + filename + " written to: " + f.getAbsolutePath());
        }

        assertEquals(expected, actual);
    }

    private String getExpectations(final BrowserVersion browserVersion, final String filename)
        throws IOException {
        final String fileNameBase = StringUtils.substringBeforeLast(filename, ".");
        final String baseName = "src/test/resources/libraries/prototype/" + getVersion() + "/expected." + fileNameBase;

        File expectationsFile = null;
        // version specific to this browser (or browser group)?
        String browserSuffix = "." + browserVersion.getNickname();
        while (browserSuffix.length() > 0) {
            final File file = new File(baseName + browserSuffix + ".txt");
            if (file.exists()) {
                expectationsFile = file;
                break;
            }
            browserSuffix = browserSuffix.substring(0, browserSuffix.length() - 1);
        }

        // generic version
        if (expectationsFile == null) {
            expectationsFile = new File(baseName + ".txt");
            if (!expectationsFile.exists()) {
                throw new FileNotFoundException("Can't find expectations file ("
                        + expectationsFile.getName() + ") for test " + filename
                        + "(" + browserVersion.getNickname() + ")");
            }
        }

        return FileUtils.readFileToString(expectationsFile, "UTF-8");
    }

    private String getText(final WebElement webElement) throws Exception {
        // Hack for the buggy asText method in seleniums htmlunit code
        if (webElement instanceof HtmlUnitWebElement) {
            final Method method = HtmlUnitWebElement.class.getDeclaredMethod("getElement", (Class<?>[]) null);
            method.setAccessible(true);
            final HtmlElement htmlElement = (HtmlElement) method.invoke(webElement);
            String text = htmlElement.asText();
            text = text.replace('\t', ' ');
            return text;
        }
        return webElement.getText();
    }

    /**
     * @throws Exception if an error occurs
     */
    @AfterClass
    public static void zzz_stopServer() throws Exception {
        SERVER_.stop();
    }

    /**
     * @return the resource base url
     */
    protected String getBaseUrl() {
        return "http://localhost:" + PORT + "/";
    }
}
