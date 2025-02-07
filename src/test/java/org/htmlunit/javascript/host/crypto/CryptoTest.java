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
package org.htmlunit.javascript.host.crypto;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

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
    @Alerts({"function", "TypeError"})
    public void ctor() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION

            + "    function test() {\n"
            + "      try {\n"
            + "        log(typeof Crypto);\n"
            + "        new Crypto();\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "false", "false", "false", "10", "true"})
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
            + "catch(e) { logEx(e); }\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[0-9a-f]{8}\\-[0-9a-f]{4}\\-[0-9a-f]{4}\\-[0-9a-f]{4}\\-[0-9a-f]{12}§")
    public void randomUUID() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(window.crypto.randomUUID());\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script></head></html>";

        final WebDriver driver = loadPage2(html);
        final String title = driver.getTitle();

        assertTrue(title, title.matches(getExpectedAlerts()[0]));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("QuotaExceededError/DOMException")
    public void getRandomValuesQuotaExceeded() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var array = new Uint32Array(16385);\n"
            + "  window.crypto.getRandomValues(array);\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object SubtleCrypto]")
    public void subtle() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(window.crypto.subtle);\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }
}
