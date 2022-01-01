/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.crypto;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link Crypto}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CryptoTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "true", "false", "false", "false", "10", "true"},
            IE = {"true", "true", "true", "exception"})
    public void getRandomValues() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var array = new Uint32Array(10);\n"
            + "  log(array[0] == 0);\n"
            + "  log(array[3] == 0);\n"
            + "  log(array[9] == 0);\n"
            + "  var res = window.crypto.getRandomValues(array);\n"
            + "  log(array[0] == 0);\n"
            + "  log(array[3] == 0);\n"
            + "  log(array[9] == 0);\n"

            + "  log(res.length);\n"
            + "  log(res === array);\n"
            + "}\n"
            + "catch(e) { log('exception'); }\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void getRandomValuesQuotaExceeded() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var array = new Uint32Array(16385);\n"
            + "  window.crypto.getRandomValues(array);\n"
            + "}\n"
            + "catch(e) { log('exception'); }\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object SubtleCrypto]",
            IE = "exception")
    public void subtle() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(window.crypto.subtle);\n"
            + "}\n"
            + "catch(e) { log('exception'); }\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }
}
