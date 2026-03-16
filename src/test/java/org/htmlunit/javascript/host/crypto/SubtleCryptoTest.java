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
    @Alerts({"secret", "true", "HMAC", "SHA-1", "512", "sign,verify"})
    public void importKeyHmac() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var rawKey = new Uint8Array([154,96,73,78,144,193,22,2,31,117,82,100,53,153,70,89,"
            + "47,64,159,6,172,145,82,124,25,206,252,42,160,14,136,161,78,165,11,207,226,149,165,112,"
            + "172,10,127,12,252,112,105,222,227,36,1,7,227,17,178,234,9,44,20,40,127,188,114,56]);\n"
            + "    window.crypto.subtle.importKey(\n"
            + "      'raw', rawKey,\n"
            + "      { name: 'HMAC', hash: { name: 'SHA-1' } },\n"
            + "      true, ['verify', 'sign', 'verify']\n"
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
    @Alerts({"secret", "false", "AES-GCM", "256", "encrypt,decrypt"})
    public void importKeyAes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var rawKey = new Uint8Array(["
            + "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,"
            + "17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32]);\n"
            + "    window.crypto.subtle.importKey(\n"
            + "      'raw', rawKey,\n"
            + "      { name: 'AES-GCM' },\n"
            + "      false, ['encrypt', 'decrypt']\n"
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16", "rejected"})
    public void exportKeyRaw() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var rawKey = new Uint8Array([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16]);\n"
            + "    window.crypto.subtle.importKey(\n"
            + "      'raw', rawKey,\n"
            + "      { name: 'AES-GCM' },\n"
            + "      true, ['encrypt', 'decrypt']\n"
            + "    ).then(function(key) {\n"
            + "      return window.crypto.subtle.exportKey('raw', key);\n"
            + "    }).then(function(exported) {\n"
            + "      log(new Uint8Array(exported).toString());\n"
            + "    });\n"
            + "    window.crypto.subtle.importKey(\n"
            + "      'raw', rawKey,\n"
            + "      { name: 'AES-GCM' },\n"
            + "      false, ['encrypt', 'decrypt']\n"
            + "    ).then(function(key) {\n"
            + "      return window.crypto.subtle.exportKey('raw', key);\n"
            + "    }).catch(function(e) {\n"
            + "      log('rejected');\n"
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
    @Alerts({"20", "true", "false"})
    public void signVerifyHmac() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var rawKey = new Uint8Array([154,96,73,78,144,193,22,2,31,117,82,100,53,153,70,89,"
            + "47,64,159,6,172,145,82,124,25,206,252,42,160,14,136,161,78,165,11,207,226,149,165,112,"
            + "172,10,127,12,252,112,105,222,227,36,1,7,227,17,178,234,9,44,20,40,127,188,114,56]);\n"
            + "    var data = new TextEncoder().encode('hello world');\n"
            + "    window.crypto.subtle.importKey(\n"
            + "      'raw', rawKey,\n"
            + "      { name: 'HMAC', hash: 'SHA-1' },\n"
            + "      false, ['sign', 'verify']\n"
            + "    ).then(function(key) {\n"
            + "      return window.crypto.subtle.sign('HMAC', key, data).then(function(sig) {\n"
            + "        log(new Uint8Array(sig).length);\n"
            + "        return window.crypto.subtle.verify('HMAC', key, sig, data).then(function(verified) {\n"
            + "          log(verified);\n"
            + "          var bad = new Uint8Array(sig);\n"
            + "          bad[0] = bad[0] ^ 0xFF;\n"
            + "          return window.crypto.subtle.verify('HMAC', key, bad, data).then(function(verified2) {\n"
            + "            log(verified2);\n"
            + "          });\n"
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
    @Alerts({"PKCS1 true", "PKCS1 false",
             "PSS true", "PSS false"})
    public void signVerifyRsa() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function signVerifyRoundTrip(algoName, genParams, signParams) {\n"
            + "    var data = new TextEncoder().encode('hello world');\n"
            + "    return window.crypto.subtle.generateKey(\n"
            + "      genParams, false, ['sign', 'verify']\n"
            + "    ).then(function(keyPair) {\n"
            + "      return window.crypto.subtle.sign(signParams, keyPair.privateKey, data)"
            + "        .then(function(sig) {\n"
            + "        return window.crypto.subtle.verify(signParams, keyPair.publicKey, sig, data)"
            + "          .then(function(verified) {\n"
            + "          log(algoName + ' ' + verified);\n"
            + "          var bad = new Uint8Array(sig);\n"
            + "          bad[0] = bad[0] ^ 0xFF;\n"
            + "          return window.crypto.subtle.verify(signParams, keyPair.publicKey, bad, data)"
            + "            .then(function(verified2) {\n"
            + "            log(algoName + ' ' + verified2);\n"
            + "          });\n"
            + "        });\n"
            + "      });\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var rsaBase = { modulusLength: 2048,\n"
            + "      publicExponent: new Uint8Array([1, 0, 1]), hash: 'SHA-256' };\n"
            + "    signVerifyRoundTrip('PKCS1',\n"
            + "      Object.assign({ name: 'RSASSA-PKCS1-v1_5' }, rsaBase),\n"
            + "      'RSASSA-PKCS1-v1_5'\n"
            + "    ).then(function() {\n"
            + "      return signVerifyRoundTrip('PSS',\n"
            + "        Object.assign({ name: 'RSA-PSS' }, rsaBase),\n"
            + "        { name: 'RSA-PSS', saltLength: 32 }\n"
            + "      );\n"
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
    @Alerts({"true", "false"})
    public void signVerifyEcdsa() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var data = new TextEncoder().encode('hello world');\n"
            + "    window.crypto.subtle.generateKey(\n"
            + "      { name: 'ECDSA', namedCurve: 'P-256' },\n"
            + "      false, ['sign', 'verify']\n"
            + "    ).then(function(keyPair) {\n"
            + "      var signParams = { name: 'ECDSA', hash: 'SHA-256' };\n"
            + "      return window.crypto.subtle.sign(signParams, keyPair.privateKey, data)"
            + "        .then(function(sig) {\n"
            + "        return window.crypto.subtle.verify(signParams, keyPair.publicKey, sig, data)"
            + "          .then(function(verified) {\n"
            + "          log(verified);\n"
            + "          var bad = new Uint8Array(sig);\n"
            + "          bad[0] = bad[0] ^ 0xFF;\n"
            + "          return window.crypto.subtle.verify(signParams, keyPair.publicKey, bad, data)"
            + "            .then(function(verified2) {\n"
            + "            log(verified2);\n"
            + "          });\n"
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
    @Alerts({"true", "false"})
    public void generateSignVerifyHmac() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var data = new TextEncoder().encode('hello world');\n"
            + "    crypto.subtle.generateKey(\n"
            + "      {name: 'HMAC', hash: 'SHA-256'}, false, ['sign', 'verify']\n"
            + "    ).then(function(key) {\n"
            + "      return crypto.subtle.sign('HMAC', key, data).then(function(sig) {\n"
            + "        return crypto.subtle.verify('HMAC', key, sig, data).then(function(verified) {\n"
            + "          log(verified);\n"
            + "          var bad = new Uint8Array(sig);\n"
            + "          bad[0] = bad[0] ^ 0xFF;\n"
            + "          return crypto.subtle.verify('HMAC', key, bad, data).then(function(verified2) {\n"
            + "            log(verified2);\n"
            + "          });\n"
            + "        });\n"
            + "      });\n"
            + "    });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
