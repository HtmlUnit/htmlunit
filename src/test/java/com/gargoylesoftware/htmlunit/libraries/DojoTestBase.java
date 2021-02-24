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

    abstract String getUrl(String module);

    void test(final String module) throws Exception {
        test(module, 60);
    }

    void test(final String module, final long waitTime) throws Exception {
        try {

            final WebDriver webdriver = getWebDriver();
            webdriver.get(getUrl(module));

            final long runTime = waitTime * DEFAULT_WAIT_TIME;
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

            Thread.sleep(100); // to make tests a bit more stable
            final WebElement output = webdriver.findElement(By.id("logBody"));
            final List<WebElement> lines = output.findElements(By.xpath(".//div"));

            final StringBuilder result = new StringBuilder();
            for (final WebElement webElement : lines) {
                final String text = webElement.getText();
                if (StringUtils.isNotBlank(text)) {
                    result.append(text);
                    result.append("\n");
                }
            }

            String expFileName = StringUtils.replace(module, ".", "");
            expFileName = StringUtils.replace(expFileName, "_", "");
            expFileName = StringUtils.replace(expFileName, "/", "_");
            String expected = loadExpectation(expFileName);
            expected = StringUtils.replace(expected, "\r\n", "\n");

            assertEquals(normalize(expected), normalize(result.toString()));
            // assertEquals(expected, result.toString());
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

    private static String normalize(final String text) {
        StringBuilder normalized = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == 'f'
                    && (text.indexOf("function ", i) == i
                            || text.indexOf("function(", i) == i)) {
                if (normalized.toString().endsWith("(")) {
                    normalized.delete(normalized.length() - 1, normalized.length());
                }
                normalized = new StringBuilder(normalized.toString().trim());

                normalized.append("  function...");
                int count = 1;
                i = text.indexOf("{", i);
                while (i < text.length()) {
                    i++;
                    ch = text.charAt(i);
                    if ('{' == ch) {
                        count++;
                    }
                    else if ('}' == ch) {
                        count--;
                    }
                    if (count == 0) {
                        break;
                    }
                }
                if (normalized.toString().endsWith(")")) {
                    normalized.delete(normalized.length() - 1, normalized.length());
                }
            }
            else if (ch == 'T' && text.indexOf("TypeError: ", i) == i) {
                normalized.append("TypeError:...");
                while (i < text.length()) {
                    i++;
                    ch = text.charAt(i);
                    if ('\r' == ch || '\n' == ch) {
                        break;
                    }
                }
            }
            else {
                normalized.append(ch);
            }
        }
        return normalized.toString().replaceAll("\\d+ ms", "x ms");
    }

    private String loadExpectation(final String expFileName) throws Exception {
        final String resourcePrefix = "/" + BASE_FILE_PATH + "/" + getVersion() + "/expectations/" + expFileName;
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
    public void startSesrver() throws Exception {
        if (SERVER_ == null) {
            SERVER_ = WebServerTestCase.createWebServer("src/test/resources/"
                        + BASE_FILE_PATH + "/" + getVersion(), null);
        }
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
}
