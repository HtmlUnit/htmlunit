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

/**
 * Tests for {@link SubtleCrypto}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 * @author Lai Quang Duong
 */
public class SubtleCryptoTest extends WebDriverTestCase {

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
            + "        log(typeof SubtleCrypto);\n"
            + "        new SubtleCrypto();\n"
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
     * Methods in SubtleCrypto should always wraps errors in a Promise and never throw directly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError true")
    @HtmlUnitNYI(CHROME = "TypeError false",
            EDGE = "TypeError false",
            FF = "TypeError false",
            FF_ESR = "TypeError false")
    public void unsupportedCall() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.crypto) {\n"
            + "      window.crypto.subtle.generateKey(\n"
            + "        { name: 'x' }\n"
            + "      )\n"
            + "      .catch(function(e) {\n"
            + "        log('TypeError ' + (e instanceof TypeError));\n"
            + "      });\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html, URL_FIRST);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Crypto]", "public", "true", "verify",
             "name RSASSA-PKCS1-v1_5", "hash [object Object]", "modulusLength 2048",
             "publicExponent 1,0,1",
             "private", "false", "sign",
             "name RSASSA-PKCS1-v1_5", "hash [object Object]", "modulusLength 2048",
             "publicExponent 1,0,1", "done"})
    public void rsassa() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(window.crypto);\n"
            + "    if (window.crypto) {\n"
            + "      window.crypto.subtle.generateKey(\n"
            + "        {\n"
            + "          name: 'RSASSA-PKCS1-v1_5',\n"
            + "          modulusLength: 2048,\n"
            + "          publicExponent: new Uint8Array([0x01, 0x00, 0x01]),\n"
            + "          hash: {name: 'SHA-256'}\n"
            + "        },\n"
            + "        false, //whether the key is extractable (i.e. can be used in exportKey)\n"
            + "        ['sign', 'verify']\n"
            + "      )\n"
            + "      .then(function(key) {\n"
            + "        log(key.publicKey.type);\n"
            + "        log(key.publicKey.extractable);\n"
            + "        log(key.publicKey.usages);\n"
            + "        for(var x in key.publicKey.algorithm) {\n"
            + "          log(x + ' ' + key.publicKey.algorithm[x]);\n"
            + "        }\n"
            + "        log(key.privateKey.type);\n"
            + "        log(key.privateKey.extractable);\n"
            + "        log(key.privateKey.usages);\n"
            + "        for(var x in key.privateKey.algorithm) {\n"
            + "          log(x + ' ' + key.publicKey.algorithm[x]);\n"
            + "        }\n"
            + "        log('done');\n"
            + "      })\n"
            + "      .catch(function(err) {\n"
            + "        log(err);\n"
            + "      });\n"
            + "    } else { log('no window.crypto'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"SHA-1: 8843d7f92416211de9ebb963ff4ce28125932878",
             "SHA-256: c3ab8ff13720e8ad9047dd39466b3c8974e592c2fa383d4a3960714caef0c4f2",
             "SHA-384: 3c9c30d9f665e74d515c842960d4a451c83a0125fd3de7392d7b37231af10c72"
                     + "ea58aedfcdf89a5765bf902af93ecf06",
             "SHA-512: 0a50261ebd1a390fed2bf326f2673c145582a6342d523204973d0219337f81616a8069b012587cf"
                     + "5635f6925f1b56c360230c19b273500ee013e030601bf2425"})
    public void digest() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var data = new TextEncoder().encode('foobar');\n"
            + "    var algorithms = ['SHA-1', 'SHA-256', {name: 'SHA-384'}, {name: 'SHA-512'}];\n"
            + "    var chain = Promise.resolve();\n"
            + "    algorithms.forEach(function(algo) {\n"
            + "      chain = chain.then(function() {\n"
            + "        return window.crypto.subtle.digest(algo, data).then(function(hash) {\n"
            + "          var hex = Array.from(new Uint8Array(hash))\n"
            + "            .map(function(b) { return b.toString(16).padStart(2, '0'); }).join('');\n"
            + "          log((algo.name ? algo.name : algo) + ': ' + hex);\n"
            + "        });\n"
            + "      });\n"
            + "    });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"secret", "true", "AES-GCM", "256", "encrypt,decrypt"})
    public void generateKeyAesGcm() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    window.crypto.subtle.generateKey(\n"
            + "      { name: 'AES-GCM', length: 256 },\n"
            + "      true, ['encrypt', 'decrypt']\n"
            + "    ).then(function(key) {\n"
            + "      log(key.type);\n"
            + "      log(key.extractable);\n"
            + "      log(key.algorithm.name);\n"
            + "      log(key.algorithm.length);\n"
            + "      log(key.usages.join(','));\n"
            + "    });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"secret", "false", "HMAC", "SHA-256", "512", "sign,verify"})
    public void generateKeyHmac() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    window.crypto.subtle.generateKey(\n"
            + "      { name: 'HMAC', hash: 'SHA-256' },\n"
            + "      false, ['sign', 'verify']\n"
            + "    ).then(function(key) {\n"
            + "      log(key.type);\n"
            + "      log(key.extractable);\n"
            + "      log(key.algorithm.name);\n"
            + "      log(key.algorithm.hash.name);\n"
            + "      log(key.algorithm.length);\n"
            + "      log(key.usages.join(','));\n"
            + "    });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"public", "true", "ECDSA", "P-256", "verify",
             "private", "false", "ECDSA", "P-256", "sign"})
    public void generateKeyEc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    window.crypto.subtle.generateKey(\n"
            + "      { name: 'ECDSA', namedCurve: 'P-256' },\n"
            + "      false, ['sign', 'verify']\n"
            + "    ).then(function(key) {\n"
            + "      log(key.publicKey.type);\n"
            + "      log(key.publicKey.extractable);\n"
            + "      log(key.publicKey.algorithm.name);\n"
            + "      log(key.publicKey.algorithm.namedCurve);\n"
            + "      log(key.publicKey.usages.join(','));\n"
            + "      log(key.privateKey.type);\n"
            + "      log(key.privateKey.extractable);\n"
            + "      log(key.privateKey.algorithm.name);\n"
            + "      log(key.privateKey.algorithm.namedCurve);\n"
            + "      log(key.privateKey.usages.join(','));\n"
            + "    });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
