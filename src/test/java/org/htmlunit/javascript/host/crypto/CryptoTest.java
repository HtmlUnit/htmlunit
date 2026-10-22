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
package org.htmlunit.javascript.host.crypto;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link Crypto}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class CryptoTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "TypeError"})
    public void ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
    @Alerts("[0-9a-f]{8}\\-[0-9a-f]{4}\\-4[0-9a-f]{3}\\-[89ab][0-9a-f]{3}\\-[0-9a-f]{12}§")
    public void randomUUID() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
    @Alerts(DEFAULT = "QuotaExceededError/DOMException",
            CHROME = "QuotaExceededError/QuotaExceededError",
            EDGE = "QuotaExceededError/QuotaExceededError")
    @HtmlUnitNYI(CHROME = "QuotaExceededError/DOMException",
            EDGE = "QuotaExceededError/DOMException")
    public void getRandomValuesQuotaExceeded() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(window.crypto.subtle);\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void subtleSingleton() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  let s = window.crypto.subtle;\n"
            + "  log(window.crypto.subtle === s);\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Uint8ClampedArray must get uniformly distributed values,
     * not only 0 and 255 because of clamping.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void getRandomValuesUint8ClampedArray() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var array = new Uint8ClampedArray(1024);\n"
            + "  var res = window.crypto.getRandomValues(array);\n"
            + "  var distinct = new Set(array).size;\n"
            + "  log(distinct > 128);\n"
            + "  log(res === array);\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Every bit of a BigInt64Array / BigUint64Array element must be random,
     * not only the lower 32 bits.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void getRandomValuesBigIntArrays() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function upperBitsVary(array) {\n"
            + "    window.crypto.getRandomValues(array);\n"
            + "    for (var i = 0; i < array.length; i++) {\n"
            + "      var upper = BigInt.asUintN(32, array[i] >> 32n);\n"
            + "      if (upper !== 0n && upper !== 0xffffffffn) {\n"
            + "        return true;\n"
            + "      }\n"
            + "    }\n"
            + "    return false;\n"
            + "  }\n"
            + "try {\n"
            + "  log(upperBitsVary(new BigUint64Array(64)));\n"
            + "  log(upperBitsVary(new BigInt64Array(64)));\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Float arrays are not supported; the array has to stay untouched.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeMismatchError/DOMException", "17", "0",
             "TypeMismatchError/DOMException", "17", "0",
             "TypeMismatchError/DOMException", "17", "0"})
    public void getRandomValuesFloatArrays() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(array) {\n"
            + "    try {\n"
            + "      window.crypto.getRandomValues(array);\n"
            + "      log('no exception');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "      log(e.code);\n"
            + "    }\n"
            + "    log(array[0]);\n"
            + "  }\n"
            + "  test(new Float16Array(4));\n"
            + "  test(new Float32Array(4));\n"
            + "  test(new Float64Array(4));\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A DataView is an ArrayBufferView but not an integer array.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeMismatchError/DOMException", "17"})
    public void getRandomValuesDataView() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  window.crypto.getRandomValues(new DataView(new ArrayBuffer(8)));\n"
            + "  log('no exception');\n"
            + "}\n"
            + "catch(e) {\n"
            + "  logEx(e);\n"
            + "  log(e.code);\n"
            + "}\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Arguments that are not an ArrayBufferView.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "TypeError", "TypeError", "TypeError", "TypeError"})
    public void getRandomValuesNoArrayBufferView() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(value) {\n"
            + "    try {\n"
            + "      window.crypto.getRandomValues(value);\n"
            + "      log('no exception');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "  test(new ArrayBuffer(8));\n"
            + "  test({});\n"
            + "  test([1, 2, 3]);\n"
            + "  test(null);\n"
            + "  test(undefined);\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Only the bytes of the view are filled, the rest of the buffer stays untouched.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void getRandomValuesByteOffset() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var buffer = new ArrayBuffer(32);\n"
            + "  var view = new Uint16Array(buffer, 8, 8);\n"
            + "  window.crypto.getRandomValues(view);\n"
            + "  var bytes = new Uint8Array(buffer);\n"
            + "  log(bytes.slice(0, 8).every(function(b) { return b === 0; }));\n"
            + "  log(bytes.slice(24).every(function(b) { return b === 0; }));\n"
            + "  log(bytes.slice(8, 24).some(function(b) { return b !== 0; }));\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script></head></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * getRandomValues() with a view on a detached buffer.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "true", "0"})
    public void getRandomValuesDetached() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var array = new Uint8Array(8);\n"
            + "    array.buffer.transfer();\n"
            + "    log(array.length);\n"
            + "    try {\n"
            + "      var result = window.crypto.getRandomValues(array);\n"
            + "      log(result === array);\n"
            + "      log(result.length);\n"
            + "    } catch (e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
