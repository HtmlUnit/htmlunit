/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import java.util.List;

import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebServerTestCase;

/**
 * Base class for jQuery Tests.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public abstract class JQueryTestBase extends WebDriverTestCase {

    private static Server SERVER_;

    /**
     * Returns the jQuery version being tested.
     * @return the jQuery version being tested
     */
    abstract String getVersion();

    /**
     * Runs the specified test.
     * @param testName the test name
     * @throws Exception if an error occurs
     */
    protected void runTest(final String testName) throws Exception {
        final int testNumber = readTestNumber(testName);
        if (testNumber == -1) {
            assertEquals("Test number not found for: " + testName, 0, getExpectedAlerts().length);
            return;
        }
        final long runTime = 60 * DEFAULT_WAIT_TIME;
        final long endTime = System.currentTimeMillis() + runTime;

        try {
            final WebDriver webdriver = getWebDriver();
            final String url = "http://localhost:" + PORT + "/jquery/test/index.html?dev&testNumber=" + testNumber;
            webdriver.get(url);

            while (!getResultElementText(webdriver).startsWith("Tests completed")) {
                Thread.sleep(100);

                if (System.currentTimeMillis() > endTime) {
                    fail("Test #" + testNumber + " runs too long (longer than " + runTime / 1000 + "s)");
                }
            }

            final WebElement output = webdriver.findElement(By.id("qunit-test-output0"));
            String result = output.getText();
            result = result.substring(0, result.indexOf("Rerun")).trim();
            final String expected = testName + " (" + getExpectedAlerts()[0] + ")";
            if (!expected.contains(result)) {
                System.out.println("--------------------------------------------");
                System.out.println("URL: " + url);
                System.out.println("--------------------------------------------");
                System.out.println("Test: " + webdriver.findElement(By.id("qunit-tests")).getText());
                System.out.println("--------------------------------------------");
                System.out.println("Failures:");
                final List<WebElement> failures = webdriver.findElements(By.cssSelector(".qunit-assert-list li.fail"));
                for (WebElement webElement : failures) {
                    System.out.println("  " + webElement.getText());
                }
                System.out.println("--------------------------------------------");

                fail(new ComparisonFailure("", expected, result).getMessage());
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            Throwable t = e;
            while ((t = t.getCause()) != null) {
                t.printStackTrace();
            }
            throw e;
        }
    }

    private static String getResultElementText(final WebDriver webdriver) {
        // if the elem is not available or stale we return an empty string
        // this will force a second try
        try {
            final WebElement elem = webdriver.findElement(By.id("qunit-testresult"));
            try {
                return elem.getText();
            }
            catch (final StaleElementReferenceException e) {
                return "";
            }
        }
        catch (final NoSuchElementException e) {
            return "";
        }
    }

    /**
     * Determine test number for test name.
     * @param testName the name
     * @return the test number
     * @throws Exception in case of problems
     */
    protected int readTestNumber(final String testName) throws Exception {
        final String testResults = loadExpectation("/libraries/jQuery/"
                                        + getVersion() + "/expectations/results", ".txt");
        final String[] lines = testResults.split("\n");
        for (int i = 0; i < lines.length; i++) {
            final String line = lines[i];
            final int pos = line.indexOf(testName);
            if (pos != -1
                    && line.indexOf('(', pos + testName.length() + 3) == -1) {
                return i + 1;
            }
        }

        return -1;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Before
    public void aaa_startSesrver() throws Exception {
        if (SERVER_ == null) {
            SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/jQuery/" + getVersion(), null);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @AfterClass
    public static void zzz_stopServer() throws Exception {
        if (SERVER_ != null) {
            SERVER_.stop();
            SERVER_ = null;
        }
    }
}
