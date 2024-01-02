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

import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for <a href="https://htmx.org/">htmx</a>.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmxTest1x9x7 extends HtmxTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "passes:653failures:1",
            FF = "passes:653failures:0",
            FF_ESR = "passes:653failures:0",
            IE = "not testable")
    @HtmlUnitNYI(
            CHROME = "passes:653failures:0",
            EDGE = "passes:653failures:0")
    public void htmx() throws Exception {
        // the test page runs in an endless loop in ie
        if (getBrowserVersion().isIE()) {
            return;
        }
        htmx("htmx-1.9.7");
    }
}
