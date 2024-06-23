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

import org.htmlunit.WebClient;
import org.htmlunit.javascript.preprocessor.HtmxOneNineTenScriptPreProcessor;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.junit.BrowserRunner.BuggyWebDriver;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for <a href="https://htmx.org/">htmx</a>.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmxTest1x9x12 extends HtmxTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("passes:707failures:0")
    @BuggyWebDriver(CHROME = "passes:707failures:1", EDGE = "passes:707failures:1")
    public void htmx() throws Exception {
        htmx("htmx-1.9.12");
    }

    @Override
    protected void setupWebClient(final WebClient webClient) {
        super.setupWebClient(webClient);

        webClient.setScriptPreProcessor(new HtmxOneNineTenScriptPreProcessor());
    }
}
