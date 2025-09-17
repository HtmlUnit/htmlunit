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
import org.junit.jupiter.api.Test;

/**
 * Tests for <a href="https://htmx.org/">htmx</a>.
 *
 * @author Ronald Brill
 */
public class HtmxTest2x0x7 extends HtmxTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:739failures:1",
            FF = "passes:734failures:2",
            FF_ESR = "passes:734failures:2")
    @HtmlUnitNYI(
            CHROME = "passes:723failures:18",
            EDGE = "passes:723failures:18",
            FF = "passes:722failures:16",
            FF_ESR = "passes:722failures:16")
    public void htmx() throws Exception {
        htmx("htmx-2.0.7", false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:636failures:104",
            FF = "passes:637failures:99",
            FF_ESR = "passes:637failures:99")
    @HtmlUnitNYI(
            CHROME = "passes:622failures:117",
            EDGE = "passes:622failures:117",
            FF = "passes:627failures:109",
            FF_ESR = "passes:627failures:109")
    public void htmxMin() throws Exception {
        htmx("htmx-2.0.7", true);
    }

    @Override
    protected void setupWebClient(final WebClient webClient) {
        super.setupWebClient(webClient);

        webClient.setScriptPreProcessor(new HtmxTwoZeroSevenScriptPreProcessor());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }
}
