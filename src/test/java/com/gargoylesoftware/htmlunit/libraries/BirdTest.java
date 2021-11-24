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

import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for Bird.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class BirdTest extends WebDriverTestCase {

    /** The server. */
    protected static Server SERVER_;

    /**
     * @throws Exception if an error occurs
     */
    @BeforeClass
    public static void startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/bird/", null);
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
    protected URL getBaseUrl() {
        return URL_FIRST;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"IE false", "IE6 undefined", "IE7 undefined", "IE8 undefined", "Mozilla true",
                      "Firefox false", "Firefox2 undefined", "Firefox3 undefined", "Gecko true",
                      "Safari true", "KHTML true", "Opera false"},
            EDGE = {"IE false", "IE6 undefined", "IE7 undefined", "IE8 undefined", "Mozilla true",
                    "Firefox false", "Firefox2 undefined", "Firefox3 undefined", "Gecko true",
                    "Safari true", "KHTML true", "Opera false"},
            FF = {"IE false", "IE6 undefined", "IE7 undefined", "IE8 undefined", "Mozilla true",
                  "Firefox true", "Firefox2 undefined", "Firefox3 undefined", "Gecko true",
                  "Safari false", "KHTML false", "Opera false"},
            FF_ESR = {"IE false", "IE6 undefined", "IE7 undefined", "IE8 undefined", "Mozilla true",
                      "Firefox true", "Firefox2 undefined", "Firefox3 undefined", "Gecko true",
                      "Safari false", "KHTML false", "Opera false"},
            IE = {"IE false", "IE6 undefined", "IE7 undefined", "IE8 undefined", "Mozilla true",
                  "Firefox false", "Firefox2 undefined", "Firefox3 undefined", "Gecko true",
                  "Safari false", "KHTML false", "Opera false"})
    public void browserUtility() throws Exception {
        doTest("BrowserUtilityTest.html");
    }

    private void doTest(final String filename) throws Exception {
        final WebDriver driver = getWebDriver();
        driver.get(getBaseUrl() + filename);

        verifyTextArea2(driver, getExpectedAlerts());
    }
}
