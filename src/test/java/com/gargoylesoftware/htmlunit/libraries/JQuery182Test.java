/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebServerTestCase;

/**
 * Tests for compatibility with web server loading of
 * version 1.8.2 of the <a href="http://jquery.com/">jQuery</a> JavaScript library.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class JQuery182Test extends WebDriverTestCase {

    private static Server SERVER_;
    private static WebDriver WEBDRIVER_;

    /**
     * Returns the jQuery version being tested.
     * @return the jQuery version being tested
     */
    protected static String getVersion() {
        return "1.8.2";
    }

    /**
     * Overridden so it doesn't check for background threads.
     *
     * {@inheritDoc}
     */
    @After
    @Override
    public void releaseResources() {
        //nothing
    }

    /**
     * Runs the specified test.
     * @param testNumber the test number
     * @throws Exception if an error occurs
     */
    protected void runTest(final int testNumber) throws Exception {
        final long endTime = System.currentTimeMillis() + 60 * 1000;
        try {
            WEBDRIVER_.get("http://localhost:" + PORT + "/jquery/test/index.html?testNumber="
                    + testNumber);
            WebElement element = null;
            do {
                if (element == null) {
                    try {
                        element = WEBDRIVER_.findElement(By.id("qunit-test-output0"));
                    }
                    catch (final Exception e) {
                        //ignore
                    }
                }
                Thread.sleep(100);
            } while (System.currentTimeMillis() < endTime && (element == null || !element.getText().contains(",")));
            String result = element.getText();
            result = result.substring(0, result.indexOf("Rerun")).trim();
            final String expected = getExpectedAlerts()[0];
            if (!expected.contains(result)) {
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

    /**
     * This MUST be named 'aaa' so it runs at the very beginning. It initiates web client.
     * @throws Exception if an error occurs
     */
    @Test
    public void aaa_startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/jQuery/" + getVersion(), null);
        WEBDRIVER_ = getWebDriver();
    }

    /**
     * This MUST have 'zzz' so it runs at the very end. It closes the server and web client.
     * @throws Exception if an error occurs
     */
    @Test
    public void zzz_stopServer() throws Exception {
        SERVER_.stop();
        WEBDRIVER_.close();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: Unit Testing Environment (0, 2, 2)")
    public void test_1() throws Exception {
        runTest(1);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: Basic requirements (0, 7, 7)")
    public void test_2() throws Exception {
        runTest(2);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery() (0, 31, 31)")
    public void test_3() throws Exception {
        runTest(3);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: selector state (0, 31, 31)")
    public void test_4() throws Exception {
        runTest(4);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: globalEval (0, 3, 3)")
    public void test_5() throws Exception {
        runTest(5);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: noConflict (0, 7, 7)")
    public void test_6() throws Exception {
        runTest(6);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: trim (0, 13, 13)")
    public void test_7() throws Exception {
        runTest(7);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: type (0, 23, 23)")
    public void test_8() throws Exception {
        runTest(8);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: isPlainObject (0, 15, 15)")
    public void test_9() throws Exception {
        runTest(9);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: isFunction (0, 19, 19)")
    public void test_10() throws Exception {
        runTest(10);
    }

}
