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
public class HtmxTest2x0x5 extends HtmxTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:725failures:1",
            FF = "passes:720failures:2",
            FF_ESR = "passes:720failures:2")
    @HtmlUnitNYI(
            CHROME = "passes:696failures:19",
            EDGE = "passes:696failures:19",
            FF = "passes:709failures:15",
            FF_ESR = "passes:709failures:15")
    public void htmx() throws Exception {
        htmx("htmx-2.0.5", false);
    }

//    /**
//     * @throws Exception if an error occurs
//     */
//    @Test
//    @Alerts(DEFAULT = "passes:552failures:68",
//            FF = "passes:552failures:67",
//            FF_ESR = "passes:552failures:67")
//    @HtmlUnitNYI(
//            CHROME = "passes:547failures:72",
//            EDGE = "passes:547failures:72",
//            FF = "passes:547failures:72",
//            FF_ESR = "passes:547failures:72")
//    public void htmxMin() throws Exception {
//        htmx("htmx-2.0.5", true);
//    }

    @Override
    protected void setupWebClient(final WebClient webClient) {
        super.setupWebClient(webClient);

        webClient.setScriptPreProcessor(new HtmxTwoZeroSevenScriptPreProcessor());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }
}
