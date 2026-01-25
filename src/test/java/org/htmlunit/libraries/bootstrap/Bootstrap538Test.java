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
package org.htmlunit.libraries.bootstrap;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for Apache MyFaces.
 *
 * @author Ronald Brill
 */
public class Bootstrap538Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @BeforeAll
    public static void startServer() throws Exception {
        startWebServer("src/test/resources/libraries/bootstrap/5.3.8", null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @HtmlUnitNYI(CHROME = "org.htmlunit.ScriptException: syntax error",
            EDGE = "org.htmlunit.ScriptException: syntax error",
            FF = "org.htmlunit.ScriptException: syntax error",
            FF_ESR = "org.htmlunit.ScriptException: syntax error")
    public void checkForJsCompileErrors() throws Exception {
        try {
            getWebDriver().get(URL_FIRST + "bootstrap.bundle.html");
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
