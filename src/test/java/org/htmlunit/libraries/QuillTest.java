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
package org.htmlunit.libraries;

import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.htmlunit.WebClient;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebServerTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for https://github.com/slab/quill.
 *
 * @author Ronald Brill
 */
public class QuillTest extends WebDriverTestCase {

    /** The server. */
    protected static Server SERVER_;

    /**
     * @throws Exception if an error occurs
     */
    @BeforeAll
    public static void startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/quill/", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @AfterAll
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
    @Alerts("Bold Italic\\nHello World!\\nSome initial bold text")
    @HtmlUnitNYI
    public void basic() throws Exception {
        // fails because the lib uses classes
        doTest("QuillTest.html");
    }

    private void doTest(final String filename) throws Exception {
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            final WebClient webClient = ((HtmlUnitDriver) driver).getWebClient();
            webClient.getOptions().setThrowExceptionOnScriptError(false);
        }

        driver.get(getBaseUrl() + filename);

        String content = driver.findElement(By.tagName("body")).getText();
        content = content.replace("\r", "").replace("\n", "\\n");
        assertEquals(getExpectedAlerts()[0], content);
    }
}
