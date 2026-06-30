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
package org.htmlunit.libraries.jasmine;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for Jasmine.
 *
 * @author Ronald Brill
 */
public class Jasmine_4_6_1_Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts
    @HtmlUnitNYI(CHROME = "org.htmlunit.ScriptException: missing ; before statement",
            EDGE = "org.htmlunit.ScriptException: missing ; before statement",
            FF = "org.htmlunit.ScriptException: missing ; before statement",
            FF_ESR = "org.htmlunit.ScriptException: missing ; before statement")
    public void hello() throws Exception {
        startWebServer("src/test/resources/libraries/jasmine/jasmine_4_6_1", null);

        try {
            getWebDriver().get(URL_FIRST + "SpecRunner.html");
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
