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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.Before;
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
/**
 * Base class for the <a href="http://dojotoolkit.org/">Dojo
 * JavaScript library tests.</a>.
 *
 * Timeout was changed in: /util/doh/runner.js, line # 682
 *
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public abstract class DojoTestBase extends WebDriverTestCase {

    private static final String BASE_FILE_PATH = "libraries/dojo";

    private static Server SERVER_;

    /**
     * @return the Dojo version being tested
     */
    abstract String getVersion();

    void test() throws Exception {
        try {
            final String[] expectedAlerts = getExpectedAlerts();

            final WebDriver webdriver = getWebDriver();
            final String url = "http://localhost:" + PORT + "/util/doh/runner.html";
            webdriver.get(url);

            final long runTime = 60 * DEFAULT_WAIT_TIME;
            final long endTime = System.currentTimeMillis() + runTime;

            // wait a bit to let the tests start
            Thread.sleep(DEFAULT_WAIT_TIME);

            String status = getResultElementText(webdriver);
            while (!"Stopped".equals(status)) {
                Thread.sleep(DEFAULT_WAIT_TIME);

                if (System.currentTimeMillis() > endTime) {
                    fail("Test runs too long (longer than " + runTime / 1000 + "s)");
                }
                status = getResultElementText(webdriver);
            }

            final WebElement output = webdriver.findElement(By.id("logBody"));
            final List<WebElement> lines = output.findElements(By.xpath(".//div"));

            final StringBuilder result = new StringBuilder();
            for (WebElement webElement : lines) {
                String text = webElement.getText();
                text = ignore(text, expectedAlerts);
                if (StringUtils.isNotBlank(text)) {
                    result.append(ignore(text, expectedAlerts));
                    result.append("\n");
                }
            }

            String expected = loadExpectation();
            expected = StringUtils.replace(expected, "\r\n", "\n");
            final StringBuilder expectedIgnore = new StringBuilder();

            for (String line : expected.split("\n")) {
                final String text = ignore(line, expectedAlerts);
                if (StringUtils.isNotBlank(text)) {
                    expectedIgnore.append(text);
                    expectedIgnore.append("\n");
                }
            }

            assertEquals(expectedIgnore.toString(), result.toString());
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

    private String ignore(final String text, final String[] toIgnore) {
        if (StringUtils.isBlank(text)
                || text.startsWith("  ")
                || " WOOHOO!!".equals(text)) {
            return null;
        }

        // to get the real expectations
        if (useRealBrowser()) {
            return text;
        }
        if (text.startsWith(" Error: test timeout")
                || text.startsWith(" Error: false")
                || text.startsWith(" TypeError:")
                || text.startsWith(" doh._AssertFailure:")) {
            return null;
        }

        for (String ignore : toIgnore) {
            if (text.contains(ignore)) {
                return "ignore";
            }
        }
        return text;
    }

    private String loadExpectation() throws Exception {
        final String resourcePrefix = "/" + BASE_FILE_PATH + "/" + getVersion() + "/expected";
        return loadExpectation(resourcePrefix, ".txt");
    }

    private static String getResultElementText(final WebDriver webdriver) {
        // if the elem is not available or stale we return an empty string
        // this will force a second try
        try {
            final WebElement elem = webdriver.findElement(By.id("runningStatus"));
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
     * @throws Exception if an error occurs
     */
    @Before
    public void aaa_startSesrver() throws Exception {
        if (SERVER_ == null) {
            SERVER_ = WebServerTestCase.createWebServer("src/test/resources/"
                        + BASE_FILE_PATH + "/" + getVersion(), null);
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
