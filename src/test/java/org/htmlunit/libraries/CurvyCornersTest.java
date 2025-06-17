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

import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebServerTestCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for compatibility with <a href="http://www.curvycorners.net">curvyCorners</a>.
 *
 * @author Gareth Davis
 * @author Ronald Brill
 */
public class CurvyCornersTest extends WebDriverTestCase {

    /** The server. */
    protected static Server SERVER_;

    /**
     * @throws Exception if an error occurs
     */
    @BeforeAll
    public static void startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/curvyCorners/1.2.9-beta/", null);
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
    public void demo() throws Exception {
        doTest("demo.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void demo2() throws Exception {
        doTest("demo2.html");
    }

    private void doTest(final String filename) throws Exception {
        final WebDriver driver = getWebDriver();
        driver.get(getBaseUrl() + filename);
    }
}
