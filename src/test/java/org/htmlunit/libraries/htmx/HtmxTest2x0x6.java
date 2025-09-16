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
public class HtmxTest2x0x6 extends HtmxTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:727failures:1",
            FF = "passes:722failures:2",
            FF_ESR = "passes:722failures:2")
    @HtmlUnitNYI(
            CHROME = "passes:696failures:21",
            EDGE = "passes:696failures:21",
            FF = "passes:709failures:17",
            FF_ESR = "passes:709failures:17")
    public void htmx() throws Exception {
        htmx("htmx-2.0.6", false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:624failures:104",
            FF = "passes:625failures:99",
            FF_ESR = "passes:625failures:99")
    @HtmlUnitNYI(
            CHROME = "passes:596failures:119",
            EDGE = "passes:596failures:119",
            FF = "passes:614failures:110",
            FF_ESR = "passes:614failures:110")
    public void htmxMin() throws Exception {
        htmx("htmx-2.0.6", true);
    }

    @Override
    protected void setupWebClient(final WebClient webClient) {
        super.setupWebClient(webClient);

        webClient.setScriptPreProcessor(new HtmxTwoZeroSevenScriptPreProcessor());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }
}
