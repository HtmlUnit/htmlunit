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
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for <a href="https://htmx.org/">htmx</a>.
 *
 * @author Ronald Brill
 */
public class HtmxTest2x0x2 extends HtmxTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:579failures:1",
            FF = "passes:579failures:0",
            FF_ESR = "passes:579failures:0")
    @HtmlUnitNYI(
            CHROME = "passes:576failures:3",
            EDGE = "passes:576failures:3",
            FF = "passes:576failures:3",
            FF_ESR = "passes:576failures:3")
    public void htmx() throws Exception {
        htmx("htmx-2.0.2", false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:514failures:66",
            FF = "passes:514failures:65",
            FF_ESR = "passes:514failures:65")
    @HtmlUnitNYI(
            CHROME = "passes:511failures:68",
            EDGE = "passes:511failures:68",
            FF = "passes:511failures:68",
            FF_ESR = "passes:511failures:68")
    public void htmxMin() throws Exception {
        htmx("htmx-2.0.2", true);
    }

    @Override
    protected void setupWebClient(final WebClient webClient) {
        super.setupWebClient(webClient);

        // min
        // ReferenceError: "getInputValues" is not defined.
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }
}
