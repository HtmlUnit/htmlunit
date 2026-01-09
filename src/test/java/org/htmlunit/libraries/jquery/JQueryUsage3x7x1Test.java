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
package org.htmlunit.libraries.jquery;

import org.htmlunit.WebClient;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests jQuery 3.7.0 scenarios.
 *
 * @author Ronald Brill
 */
public class JQueryUsage3x7x1Test extends JQueryTestBase {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return "3.7.1";
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("hello")
    public void test() throws Exception {
        final WebDriver webDriver = getWebDriver();

        if (webDriver instanceof HtmlUnitDriver) {
            final WebClient webClient = ((HtmlUnitDriver) webDriver).getWebClient();
            new OnlyLocalConnectionWrapper(webClient);
        }

        webDriver.get(URL_FIRST + "jquery/usage/globalVariablesInSeparateScripts.html");
        verifyTitle2(DEFAULT_WAIT_TIME, webDriver, getExpectedAlerts()[0]);
    }

}
