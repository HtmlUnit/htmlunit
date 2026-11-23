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
public class HtmxTest2x0x10 extends HtmxTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:755failures:1",
            FF = "passes:752failures:1",
            FF_ESR = "passes:752failures:0")
    @HtmlUnitNYI(
            CHROME = "passes:742failures:15",
            EDGE = "passes:742failures:15",
            FF = "passes:741failures:13",
            FF_ESR = "passes:741failures:13")
    public void htmx() throws Exception {
        htmx("htmx-2.0.10", false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:648failures:108",
            FF = "passes:651failures:102",
            FF_ESR = "passes:651failures:101")
    @HtmlUnitNYI(
            CHROME = "passes:637failures:118",
            EDGE = "passes:637failures:118",
            FF = "passes:642failures:110",
            FF_ESR = "passes:642failures:110")
    public void htmxMin() throws Exception {
        htmx("htmx-2.0.10", true);
    }

    @Override
    protected void setupWebClient(final WebClient webClient) {
        super.setupWebClient(webClient);

        webClient.setScriptPreProcessor(new HtmxTwoZeroSevenScriptPreProcessor());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }
}
