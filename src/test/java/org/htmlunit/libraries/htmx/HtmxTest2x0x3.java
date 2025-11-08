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
package org.htmlunit.libraries.htmx;

import org.htmlunit.WebClient;
import org.htmlunit.javascript.preprocessor.HtmxTwoZeroSevenScriptPreProcessor;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for <a href="https://htmx.org/">htmx</a>.
 *
 * @author Ronald Brill
 */
public class HtmxTest2x0x3 extends HtmxTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:605failures:1",
            FF = "passes:605failures:0",
            FF_ESR = "passes:605failures:0")
    @HtmlUnitNYI(
            CHROME = "passes:599failures:8",
            EDGE = "passes:599failures:8",
            FF = "passes:599failures:8",
            FF_ESR = "passes:599failures:8")
    public void htmx() throws Exception {
        htmx("htmx-2.0.3", false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:539failures:67",
            FF = "passes:539failures:66",
            FF_ESR = "passes:539failures:66")
    @HtmlUnitNYI(
            CHROME = "passes:535failures:70",
            EDGE = "passes:535failures:70",
            FF = "passes:535failures:70",
            FF_ESR = "passes:535failures:70")
    @Disabled
    public void htmxMin() throws Exception {
        htmx("htmx-2.0.3", true);
    }

    @Override
    protected void setupWebClient(final WebClient webClient) {
        super.setupWebClient(webClient);

        webClient.setScriptPreProcessor(new HtmxTwoZeroSevenScriptPreProcessor());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }
}
