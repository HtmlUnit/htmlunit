/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import org.eclipse.jetty.server.Server;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebServerTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for Apache MyFaces.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class MyFacesTest extends WebDriverTestCase {

    /** The server. */
    protected static Server SERVER_;

    /**
     * @throws Exception if an error occurs
     */
    @BeforeClass
    public static void startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/myfaces/4_0_2", null);
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
     * @throws Exception if the test fails
     */
    @Test
    @HtmlUnitNYI(CHROME = "org.htmlunit.ScriptException: syntax error",
            EDGE = "org.htmlunit.ScriptException: syntax error",
            FF = "org.htmlunit.ScriptException: syntax error",
            FF_ESR = "org.htmlunit.ScriptException: syntax error")
    public void checkForJsCompileErrors_4_0_2() throws Exception {
        try {
            getWebDriver().get(URL_FIRST + "index.html");
        }
        catch (final Exception e) {
            if (getExpectedAlerts().length > 0) {
                assertTrue(e.getMessage(), e.getMessage().startsWith(getExpectedAlerts()[0]));
                return;
            }

            throw e;
        }
    }
}
