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
     * Verifies that generating an ECDSA key pair with only 'verify' usage fails,
     * because the resulting privateKey would have empty usages.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void generateKeyEcdsaVerifyOnly() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    window.crypto.subtle.generateKey(\n"
            + "      { name: 'ECDSA', namedCurve: 'P-256' },\n"
            + "      false,\n"
            + "      ['verify']\n"
            + "    ).then(function() {\n"
            + "      log('success');\n"
            + "    }).catch(function(e) {\n"
            + "      logEx(e);\n"
            + "    });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Verifies that generating an RSA-OAEP key pair with only 'encrypt' usage fails,
     * because the resulting privateKey would have empty usages.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void generateKeyRsaOaepEncryptOnly() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    window.crypto.subtle.generateKey(\n"
            + "      {\n"
            + "        name: 'RSA-OAEP',\n"
            + "        modulusLength: 2048,\n"
            + "        publicExponent: new Uint8Array([1, 0, 1]),\n"
            + "        hash: 'SHA-256'\n"
            + "      },\n"
            + "      false,\n"
            + "      ['encrypt']\n"
            + "    ).then(function() {\n"
            + "      log('success');\n"
            + "    }).catch(function(e) {\n"
            + "      logEx(e);\n"
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
    @Alerts("SyntaxError/DOMException")
    public void generateKeyAesGcmInvalidUsage() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    window.crypto.subtle.generateKey(\n"
            + "      { name: 'AES-GCM', length: 256 },\n"
            + "      true, ['encrypt', 'sign']\n"
            + "    ).then(function() {\n"
            + "      log('success');\n"
            + "    }).catch(function(e) {\n"
            + "      logEx(e);\n"
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
    @Alerts("SyntaxError/DOMException")
    public void importKeyHmacInvalidUsage() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var rawKey = new Uint8Array(64);\n"
            + "    window.crypto.subtle.importKey(\n"
            + "      'raw', rawKey,\n"
            + "      { name: 'HMAC', hash: { name: 'SHA-256' } },\n"
            + "      true, ['sign', 'encrypt']\n"
            + "    ).then(function() {\n"
            + "      log('success');\n"
            + "    }).catch(function(e) {\n"
            + "      logEx(e);\n"
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
    @Alerts(DEFAULT = {"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16", "rejected"},
            FF = {"rejected", "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16"},
            FF_ESR = {"rejected", "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16"})
    @HtmlUnitNYI(FF = {"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16", "rejected"},
            FF_ESR = {"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16", "rejected"})
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"AES-CBC ok", "AES-GCM ok", "AES-CTR ok"})
    public void encryptDecryptAes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function encryptDecryptRoundTrip(genParams, encParams) {\n"
            + "    var data = new TextEncoder().encode('hello world');\n"
            + "    return window.crypto.subtle.generateKey(\n"
            + "      genParams, false, ['encrypt', 'decrypt']\n"
            + "    ).then(function(key) {\n"
            + "      return window.crypto.subtle.encrypt(encParams, key, data)"
            + "        .then(function(encrypted) {\n"
            + "        return window.crypto.subtle.decrypt(encParams, key, encrypted)"
            + "          .then(function(decrypted) {\n"
            + "          var result = new TextDecoder().decode(decrypted);\n"
            + "          log(genParams.name + (result === 'hello world' ? ' ok' : ' FAIL'));\n"
            + "        });\n"
            + "      });\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var iv16 = crypto.getRandomValues(new Uint8Array(16));\n"
            + "    var iv12 = crypto.getRandomValues(new Uint8Array(12));\n"
            + "    encryptDecryptRoundTrip(\n"
            + "      { name: 'AES-CBC', length: 256 },\n"
            + "      { name: 'AES-CBC', iv: iv16 }\n"
            + "    ).then(function() {\n"
            + "      return encryptDecryptRoundTrip(\n"
            + "        { name: 'AES-GCM', length: 256 },\n"
            + "        { name: 'AES-GCM', iv: iv12 }\n"
            + "      );\n"
            + "    }).then(function() {\n"
            + "      return encryptDecryptRoundTrip(\n"
            + "        { name: 'AES-CTR', length: 256 },\n"
            + "        { name: 'AES-CTR', counter: iv16, length: 64 }\n"
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
    @Alerts("hello world")
    public void encryptDecryptRsaOaep() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var data = new TextEncoder().encode('hello world');\n"
            + "    window.crypto.subtle.generateKey(\n"
            + "      { name: 'RSA-OAEP', modulusLength: 2048,\n"
            + "        publicExponent: new Uint8Array([1, 0, 1]), hash: 'SHA-256' },\n"
            + "      false, ['encrypt', 'decrypt']\n"
            + "    ).then(function(keyPair) {\n"
            + "      return window.crypto.subtle.encrypt(\n"
            + "        { name: 'RSA-OAEP' }, keyPair.publicKey, data\n"
            + "      ).then(function(encrypted) {\n"
            + "        return window.crypto.subtle.decrypt(\n"
            + "          { name: 'RSA-OAEP' }, keyPair.privateKey, encrypted\n"
            + "        ).then(function(decrypted) {\n"
            + "          log(new TextDecoder().decode(decrypted));\n"
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
    @Alerts("hello world")
    public void encryptDecryptAesGcmAad() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var data = new TextEncoder().encode('hello world');\n"
            + "    var aad = new TextEncoder().encode('additional data');\n"
            + "    var iv = crypto.getRandomValues(new Uint8Array(12));\n"
            + "    crypto.subtle.generateKey(\n"
            + "      {name: 'AES-GCM', length: 256}, false, ['encrypt', 'decrypt']\n"
            + "    ).then(function(key) {\n"
            + "      return crypto.subtle.encrypt(\n"
            + "        {name: 'AES-GCM', iv: iv, additionalData: aad}, key, data\n"
            + "      ).then(function(encrypted) {\n"
            + "        return crypto.subtle.decrypt(\n"
            + "          {name: 'AES-GCM', iv: iv, additionalData: aad}, key, encrypted\n"
            + "        ).then(function(decrypted) {\n"
            + "          log(new TextDecoder().decode(decrypted));\n"
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
     * Decrypting data that fails the integrity or padding check must reject the promise
     * with an OperationError instead of failing inside HtmlUnit.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"OperationError/DOMException", "0",
             "OperationError/DOMException", "0",
             "OperationError/DOMException", "0"})
    public void decryptFailureRejectsWithOperationError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array([1, 2, 3, 4, 5, 6, 7, 8]);\n"
            + "    var gcmParams = { name: 'AES-GCM', iv: new Uint8Array(12) };\n"
            + "    var cbcParams = { name: 'AES-CBC', iv: new Uint8Array(16) };\n"
            + "    var rawKey = new Uint8Array(16);\n"
            + "    var otherRawKey = new Uint8Array(16).fill(1);\n"

            + "    function logFailure(e) {\n"
            + "      logEx(e);\n"
            + "      log(e.code);\n"
            + "    }\n"

            // AES-GCM: the authentication tag no longer matches
            + "    subtle.importKey('raw', rawKey, { name: 'AES-GCM' }, false, ['encrypt', 'decrypt'])\n"
            + "      .then(function(key) {\n"
            + "        return subtle.encrypt(gcmParams, key, data).then(function(ciphertext) {\n"
            + "          var tampered = new Uint8Array(ciphertext);\n"
            + "          tampered[0] ^= 1;\n"
            + "          return subtle.decrypt(gcmParams, key, tampered);\n"
            + "        });\n"
            + "      })\n"
            + "      .then(function() { log('success'); }, logFailure)\n"

            // AES-CBC: decrypting with the wrong key gives invalid padding
            + "      .then(function() {\n"
            + "        return Promise.all([\n"
            + "          subtle.importKey('raw', rawKey, { name: 'AES-CBC' }, false, ['encrypt']),\n"
            + "          subtle.importKey('raw', otherRawKey, { name: 'AES-CBC' }, false, ['decrypt'])\n"
            + "        ]);\n"
            + "      })\n"
            + "      .then(function(keys) {\n"
            + "        return subtle.encrypt(cbcParams, keys[0], data).then(function(ciphertext) {\n"
            + "          return subtle.decrypt(cbcParams, keys[1], ciphertext);\n"
            + "        });\n"
            + "      })\n"
            + "      .then(function() { log('success'); }, logFailure)\n"

            // RSA-OAEP: decrypting with the private key of another key pair
            + "      .then(function() {\n"
            + "        var params = { name: 'RSA-OAEP', modulusLength: 2048,\n"
            + "                       publicExponent: new Uint8Array([1, 0, 1]), hash: 'SHA-256' };\n"
            + "        return Promise.all([\n"
            + "          subtle.generateKey(params, false, ['encrypt', 'decrypt']),\n"
            + "          subtle.generateKey(params, false, ['encrypt', 'decrypt'])\n"
            + "        ]);\n"
            + "      })\n"
            + "      .then(function(pairs) {\n"
            + "        return subtle.encrypt({ name: 'RSA-OAEP' }, pairs[0].publicKey, data)\n"
            + "          .then(function(ciphertext) {\n"
            + "            return subtle.decrypt({ name: 'RSA-OAEP' }, pairs[1].privateKey, ciphertext);\n"
            + "          });\n"
            + "      })\n"
            + "      .then(function() { log('success'); }, logFailure);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * importKey() accepts the algorithm as a string.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"AES-GCM", "128", "secret", "TypeError"},
            FF = {"AES-GCM", "128", "secret", "SyntaxError/DOMException"},
            FF_ESR = {"AES-GCM", "128", "secret", "SyntaxError/DOMException"})
    @HtmlUnitNYI(FF = {"AES-GCM", "128", "secret", "TypeError"},
            FF_ESR = {"AES-GCM", "128", "secret", "TypeError"})
    public void importKeyRawStringAlgorithm() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var rawKey = new Uint8Array(16);\n"
            + "    window.crypto.subtle.importKey('raw', rawKey, 'AES-GCM', true, ['encrypt', 'decrypt'])\n"
            + "      .then(function(key) {\n"
            + "        log(key.algorithm.name);\n"
            + "        log(key.algorithm.length);\n"
            + "        log(key.type);\n"
            + "      })\n"
            + "      .catch(logEx)\n"

            // HMAC requires the hash member, a plain string is not enough
            + "      .then(function() {\n"
            + "        return window.crypto.subtle.importKey('raw', rawKey, 'HMAC', true, ['sign']);\n"
            + "      })\n"
            + "      .then(function() { log('success'); }, logEx);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Algorithm names are matched case-insensitive.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"20", "32", "48", "64", "NotSupportedError/DOMException"})
    public void digestAlgorithmNameCaseInsensitive() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array([1, 2, 3]);\n"
            + "    function logLength(digest) { log(digest.byteLength); }\n"
            + "    subtle.digest('sha-1', data).then(logLength, logEx)\n"
            + "      .then(function() { return subtle.digest('sha-256', data); }).then(logLength, logEx)\n"
            + "      .then(function() { return subtle.digest({ name: 'Sha-384' }, data); }).then(logLength, logEx)\n"
            + "      .then(function() { return subtle.digest('sHa-512', data); }).then(logLength, logEx)\n"
            + "      .then(function() { return subtle.digest('SHA-257', data); }).then(logLength, logEx);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Algorithm names are matched case-insensitive; the key uses the registered name.
     * The curve name is case-sensitive.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"AES-GCM 128", "HMAC SHA-512 1024", "ECDSA P-384", "RSASSA-PKCS1-v1_5 SHA-256",
                       "NotSupportedError/DOMException"},
            FF = {"AES-GCM 128", "HMAC SHA-512 1024", "ECDSA P-384", "RSASSA-PKCS1-v1_5 SHA-256", "ECDSA P-256"},
            FF_ESR = {"AES-GCM 128", "HMAC SHA-512 1024", "ECDSA P-384", "RSASSA-PKCS1-v1_5 SHA-256", "ECDSA P-256"})
    @HtmlUnitNYI(
            FF = {"AES-GCM 128", "HMAC SHA-512 1024", "ECDSA P-384", "RSASSA-PKCS1-v1_5 SHA-256",
                  "NotSupportedError/DOMException"},
            FF_ESR = {"AES-GCM 128", "HMAC SHA-512 1024", "ECDSA P-384", "RSASSA-PKCS1-v1_5 SHA-256",
                      "NotSupportedError/DOMException"})
    public void generateKeyAlgorithmNameCaseInsensitive() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    function logAlgorithm(result) {\n"
            + "      var algorithm = (result.publicKey || result).algorithm;\n"
            + "      var details = algorithm.length || algorithm.namedCurve || '';\n"
            + "      if (algorithm.hash) {\n"
            + "        details = algorithm.hash.name + (algorithm.name == 'HMAC' ? ' ' + algorithm.length : '');\n"
            + "      }\n"
            + "      log(algorithm.name + ' ' + details);\n"
            + "    }\n"
            + "    subtle.generateKey({ name: 'aes-gcm', length: 128 }, true, ['encrypt'])\n"
            + "      .then(logAlgorithm, logEx)\n"
            + "      .then(function() {\n"
            + "        return subtle.generateKey({ name: 'hmac', hash: 'sha-512' }, true, ['sign']);\n"
            + "      })\n"
            + "      .then(logAlgorithm, logEx)\n"
            + "      .then(function() {\n"
            + "        return subtle.generateKey({ name: 'ecdsa', namedCurve: 'P-384' }, true, ['sign']);\n"
            + "      })\n"
            + "      .then(logAlgorithm, logEx)\n"
            + "      .then(function() {\n"
            + "        return subtle.generateKey({ name: 'rsassa-pkcs1-v1_5', modulusLength: 2048,\n"
            + "            publicExponent: new Uint8Array([1, 0, 1]), hash: { name: 'sha-256' } }, true, ['sign']);\n"
            + "      })\n"
            + "      .then(logAlgorithm, logEx)\n"
            + "      .then(function() {\n"
            + "        return subtle.generateKey({ name: 'ECDSA', namedCurve: 'p-256' }, true, ['sign']);\n"
            + "      })\n"
            + "      .then(logAlgorithm, logEx);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Algorithm names are matched case-insensitive; the key uses the registered name.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"AES-CBC", "HMAC SHA-256 128"})
    public void importKeyAlgorithmNameCaseInsensitive() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var rawKey = new Uint8Array(16);\n"
            + "    subtle.importKey('raw', rawKey, 'aes-cbc', true, ['encrypt'])\n"
            + "      .then(function(key) { log(key.algorithm.name); }, logEx)\n"
            + "      .then(function() {\n"
            + "        return subtle.importKey('raw', rawKey, { name: 'hmac', hash: 'sha-256' }, true, ['sign']);\n"
            + "      })\n"
            + "      .then(function(key) {\n"
            + "        log(key.algorithm.name + ' ' + key.algorithm.hash.name + ' ' + key.algorithm.length);\n"
            + "      }, logEx);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * The algorithm name of encrypt/decrypt/sign/verify is matched case-insensitive
     * against the name of the key.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1,2,3", "true", "true"})
    public void operationAlgorithmNameCaseInsensitive() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array([1, 2, 3]);\n"
            + "    var iv = new Uint8Array(12);\n"
            + "    subtle.generateKey({ name: 'AES-GCM', length: 128 }, false, ['encrypt', 'decrypt'])\n"
            + "      .then(function(key) {\n"
            + "        return subtle.encrypt({ name: 'aes-gcm', iv: iv }, key, data).then(function(ciphertext) {\n"
            + "          return subtle.decrypt({ name: 'Aes-Gcm', iv: iv }, key, ciphertext);\n"
            + "        });\n"
            + "      })\n"
            + "      .then(function(plaintext) { log(new Uint8Array(plaintext).join()); }, logEx)\n"

            + "      .then(function() {\n"
            + "        return subtle.generateKey({ name: 'HMAC', hash: 'SHA-256' }, false, ['sign', 'verify']);\n"
            + "      })\n"
            + "      .then(function(key) {\n"
            + "        return subtle.sign('hmac', key, data).then(function(signature) {\n"
            + "          return subtle.verify({ name: 'Hmac' }, key, signature, data);\n"
            + "        });\n"
            + "      })\n"
            + "      .then(log, logEx)\n"

            + "      .then(function() {\n"
            + "        return subtle.generateKey({ name: 'ECDSA', namedCurve: 'P-256' }, false, ['sign', 'verify']);\n"
            + "      })\n"
            + "      .then(function(pair) {\n"
            + "        return subtle.sign({ name: 'ecdsa', hash: 'sha-256' }, pair.privateKey, data)\n"
            + "          .then(function(signature) {\n"
            + "            return subtle.verify({ name: 'ECDSA', hash: { name: 'Sha-256' } },\n"
            + "                pair.publicKey, signature, data);\n"
            + "          });\n"
            + "      })\n"
            + "      .then(log, logEx);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * A TypeError has to reject the promise with a real TypeError object.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "TypeError"})
    public void digestInvalidDataRejectsWithTypeError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    window.crypto.subtle.digest('SHA-256', {})\n"
            + "      .then(function() { log('success'); })\n"
            + "      .catch(function(e) {\n"
            + "        log(e instanceof TypeError);\n"
            + "        log(e.name);\n"
            + "      });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Verifying an invalid signature resolves to false, also when the signature
     * does not have the length of the modulus.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"RSASSA-PKCS1-v1_5 true false false false false",
             "RSA-PSS true false false false false"})
    public void verifyRsaInvalidSignature() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function check(params) {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array([1, 2, 3]);\n"
            + "    var keyParams = { name: params.name, modulusLength: 2048,\n"
            + "                      publicExponent: new Uint8Array([1, 0, 1]), hash: 'SHA-256' };\n"
            + "    return subtle.generateKey(keyParams, false, ['sign', 'verify']).then(function(pair) {\n"
            + "      return subtle.sign(params, pair.privateKey, data).then(function(signature) {\n"
            + "        var signatures = [signature, new Uint8Array(3), new Uint8Array(0),\n"
            + "                          new Uint8Array(256), new Uint8Array(256).fill(255)];\n"
            + "        return Promise.all(signatures.map(function(s) {\n"
            + "          return subtle.verify(params, pair.publicKey, s, data);\n"
            + "        }));\n"
            + "      });\n"
            + "    }).then(function(results) {\n"
            + "      log(params.name + ' ' + results.join(' '));\n"
            + "    }, logEx);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    check({ name: 'RSASSA-PKCS1-v1_5' })\n"
            + "      .then(function() { return check({ name: 'RSA-PSS', saltLength: 32 }); });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Arguments that are not a CryptoKey reject the promise with a TypeError.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"encrypt TypeError TypeError TypeError TypeError",
             "decrypt TypeError TypeError TypeError TypeError",
             "sign TypeError TypeError TypeError TypeError",
             "verify TypeError TypeError TypeError TypeError",
             "exportKey TypeError TypeError TypeError TypeError"})
    public void invalidKeyRejectsWithTypeError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array([1, 2, 3]);\n"
            + "    var aes = { name: 'AES-GCM', iv: new Uint8Array(12) };\n"
            + "    var calls = {\n"
            + "      encrypt: function(key) { return subtle.encrypt(aes, key, data); },\n"
            + "      decrypt: function(key) { return subtle.decrypt(aes, key, data); },\n"
            + "      sign: function(key) { return subtle.sign('HMAC', key, data); },\n"
            + "      verify: function(key) { return subtle.verify('HMAC', key, data, data); },\n"
            + "      exportKey: function(key) { return subtle.exportKey('raw', key); }\n"
            + "    };\n"
            + "    var invalidKeys = [undefined, null, {}, 'key'];\n"

            + "    var chain = Promise.resolve();\n"
            + "    Object.keys(calls).forEach(function(name) {\n"
            + "      chain = chain.then(function() {\n"
            + "        return Promise.all(invalidKeys.map(function(key) {\n"
            + "          try {\n"
            + "            return calls[name](key).then(function() { return 'success'; },\n"
            + "                                         function(e) { return e.name; });\n"
            + "          } catch (e) {\n"
            + "            return 'thrown ' + e.name;\n"
            + "          }\n"
            + "        }));\n"
            + "      }).then(function(results) {\n"
            + "        log(name + ' ' + results.join(' '));\n"
            + "      });\n"
            + "    });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Only the rightmost 'length' bits of the counter block are incremented;
     * they wrap around without changing the other bits.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"e9f1c305e1a6291146f6554e3a3596b50e023906a1869d5fdc89eff1abe537f8fc2dd9027baad0a6cbd00d0c5fa35d5c true",
                       "e9f1c305e1a6291146f6554e3a3596b53e5fe07a13bee3788efa8952756c6003f4620cf90f0fe037165a3c8554a651f4 true",
                       "944120ff035655e331f4f46796ad82ed8ae8274c3f6cde957f2810c5b644da8fdd16346f0caf58ff361ed9f64929a3e61ec2698313bdb396681d5dbc165eeff9 true"},
            FF = {"e9f1c305e1a6291146f6554e3a3596b5a7db27c595c7b5d2880826c22532da3ce62edec5288305b9d768b326324812a2 true",
                  "e9f1c305e1a6291146f6554e3a3596b5a7db27c595c7b5d2880826c22532da3ce62edec5288305b9d768b326324812a2 true",
                  "944120ff035655e331f4f46796ad82ed8ae8274c3f6cde957f2810c5b644da8f816132d8b1416f84901fbd102dd75bc75f44e9f45ef14e42ba79b9a77730c844 true"},
            FF_ESR = {"e9f1c305e1a6291146f6554e3a3596b5a7db27c595c7b5d2880826c22532da3ce62edec5288305b9d768b326324812a2 true",
                      "e9f1c305e1a6291146f6554e3a3596b5a7db27c595c7b5d2880826c22532da3ce62edec5288305b9d768b326324812a2 true",
                      "944120ff035655e331f4f46796ad82ed8ae8274c3f6cde957f2810c5b644da8f816132d8b1416f84901fbd102dd75bc75f44e9f45ef14e42ba79b9a77730c844 true"})
    @HtmlUnitNYI(
            FF = {"e9f1c305e1a6291146f6554e3a3596b50e023906a1869d5fdc89eff1abe537f8fc2dd9027baad0a6cbd00d0c5fa35d5c true",
                  "e9f1c305e1a6291146f6554e3a3596b53e5fe07a13bee3788efa8952756c6003f4620cf90f0fe037165a3c8554a651f4 true",
                  "944120ff035655e331f4f46796ad82ed8ae8274c3f6cde957f2810c5b644da8fdd16346f0caf58ff361ed9f64929a3e61ec2698313bdb396681d5dbc165eeff9 true"},
            FF_ESR = {"e9f1c305e1a6291146f6554e3a3596b50e023906a1869d5fdc89eff1abe537f8fc2dd9027baad0a6cbd00d0c5fa35d5c true",
                      "e9f1c305e1a6291146f6554e3a3596b53e5fe07a13bee3788efa8952756c6003f4620cf90f0fe037165a3c8554a651f4 true",
                      "944120ff035655e331f4f46796ad82ed8ae8274c3f6cde957f2810c5b644da8fdd16346f0caf58ff361ed9f64929a3e61ec2698313bdb396681d5dbc165eeff9 true"})
    public void aesCtrCounterWrap() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function toHex(buffer) {\n"
            + "    return Array.from(new Uint8Array(buffer)).map(function(b) {\n"
            + "      return ('0' + b.toString(16)).slice(-2);\n"
            + "    }).join('');\n"
            + "  }\n"
            + "  function check(key, counter, length, size) {\n"
            + "    var params = { name: 'AES-CTR', counter: counter, length: length };\n"
            + "    return window.crypto.subtle.encrypt(params, key, new Uint8Array(size))\n"
            + "      .then(function(ciphertext) {\n"
            + "        return window.crypto.subtle.decrypt(params, key, ciphertext).then(function(plaintext) {\n"
            + "          var zeros = new Uint8Array(plaintext).every(function(b) { return b === 0; });\n"
            + "          log(toHex(ciphertext) + ' ' + zeros);\n"
            + "        });\n"
            + "      });\n"
            + "  }\n"
            + "  function test() {\n"
            // nonce 01..08, the counter part (rightmost 64 bits) is at its maximum
            + "    var counter64 = new Uint8Array(16);\n"
            + "    counter64.set([1, 2, 3, 4, 5, 6, 7, 8]);\n"
            + "    counter64.fill(0xff, 8);\n"
            + "    var counter8 = new Uint8Array(16).fill(0x11);\n"
            + "    counter8[15] = 0xfe;\n"
            + "    window.crypto.subtle.importKey('raw', new Uint8Array(16), 'AES-CTR', false, ['encrypt', 'decrypt'])\n"
            + "      .then(function(key) {\n"
            + "        return check(key, counter64, 64, 48)\n"
            + "          .then(function() { return check(key, counter64, 128, 48); })\n"
            + "          .then(function() { return check(key, counter8, 8, 64); });\n"
            + "      })\n"
            + "      .catch(logEx);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * The counter must not repeat: data longer than 2^length blocks is rejected.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"32", "DataError/DOMException", "64", "DataError/DOMException"},
            FF = {"OperationError/DOMException", "OperationError/DOMException", "OperationError/DOMException", "OperationError/DOMException"},
            FF_ESR = {"OperationError/DOMException", "OperationError/DOMException", "OperationError/DOMException", "OperationError/DOMException"})
    @HtmlUnitNYI(FF =  {"32", "DataError/DOMException", "64", "DataError/DOMException"},
            FF_ESR =  {"32", "DataError/DOMException", "64", "DataError/DOMException"})
    public void aesCtrTooManyBlocks() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    function encrypt(key, length, size) {\n"
            + "      var params = { name: 'AES-CTR', counter: new Uint8Array(16), length: length };\n"
            + "      return subtle.encrypt(params, key, new Uint8Array(size))\n"
            + "        .then(function(ciphertext) { log(ciphertext.byteLength); }, logEx);\n"
            + "    }\n"
            + "    subtle.importKey('raw', new Uint8Array(16), 'AES-CTR', false, ['encrypt'])\n"
            + "      .then(function(key) {\n"
            // length 1 allows 2 blocks, length 2 allows 4 blocks
            + "        return encrypt(key, 1, 32)\n"
            + "          .then(function() { return encrypt(key, 1, 33); })\n"
            + "          .then(function() { return encrypt(key, 2, 64); })\n"
            + "          .then(function() { return encrypt(key, 2, 65); });\n"
            + "      })\n"
            + "      .catch(logEx);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * AES-CBC: wrong iv length and ciphertext length.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"OperationError/DOMException", "OperationError/DOMException",
             "OperationError/DOMException", "OperationError/DOMException"})
    public void aesCbcOperationError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array(16);\n"
            + "    function cbc(ivLength) {\n"
            + "      return { name: 'AES-CBC', iv: new Uint8Array(ivLength) };\n"
            + "    }\n"
            + "    subtle.importKey('raw', new Uint8Array(16), 'AES-CBC', false, ['encrypt', 'decrypt'])\n"
            + "      .then(function(key) {\n"
            + "        run([\n"
            + "          function() { return subtle.encrypt(cbc(8), key, data); },\n"
            + "          function() { return subtle.decrypt(cbc(8), key, data); },\n"
            + "          function() { return subtle.decrypt(cbc(16), key, new Uint8Array(17)); },\n"
            + "          function() { return subtle.decrypt(cbc(16), key, new Uint8Array(0)); }\n"
            + "        ]);\n"
            + "      });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * AES-CTR: wrong counter length and counter bit length.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"OperationError/DOMException", "OperationError/DOMException",
             "OperationError/DOMException"})
    public void aesCtrOperationError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array(16);\n"
            + "    function ctr(counterLength, length) {\n"
            + "      return { name: 'AES-CTR', counter: new Uint8Array(counterLength), length: length };\n"
            + "    }\n"
            + "    subtle.importKey('raw', new Uint8Array(16), 'AES-CTR', false, ['encrypt'])\n"
            + "      .then(function(key) {\n"
            + "        run([\n"
            + "          function() { return subtle.encrypt(ctr(8, 64), key, data); },\n"
            + "          function() { return subtle.encrypt(ctr(16, 0), key, data); },\n"
            + "          function() { return subtle.encrypt(ctr(16, 129), key, data); }\n"
            + "        ]);\n"
            + "      });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * AES-GCM: invalid tag length and ciphertext shorter than the tag.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"OperationError/DOMException", "OperationError/DOMException",
             "OperationError/DOMException"})
    public void aesGcmOperationError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array(16);\n"
            + "    function gcm(tagLength) {\n"
            + "      return { name: 'AES-GCM', iv: new Uint8Array(12), tagLength: tagLength };\n"
            + "    }\n"
            + "    subtle.importKey('raw', new Uint8Array(16), 'AES-GCM', false, ['encrypt', 'decrypt'])\n"
            + "      .then(function(key) {\n"
            + "        run([\n"
            + "          function() { return subtle.encrypt(gcm(100), key, data); },\n"
            + "          function() { return subtle.encrypt(gcm(0), key, data); },\n"
            + "          function() { return subtle.decrypt(gcm(128), key, new Uint8Array(5)); }\n"
            + "        ]);\n"
            + "      });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * generateKey() with parameters that can not be used to generate a key.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"OperationError/DOMException", "OperationError/DOMException",
             "OperationError/DOMException", "OperationError/DOMException",
             "OperationError/DOMException"})
    public void generateKeyOperationError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array(16);\n"
            + "    function rsa(publicExponent, modulusLength) {\n"
            + "      return { name: 'RSA-PSS', modulusLength: modulusLength,\n"
            + "               publicExponent: new Uint8Array(publicExponent), hash: 'SHA-256' };\n"
            + "    }\n"
            + "    run([\n"
            + "      function() { return subtle.generateKey({ name: 'AES-CBC', length: 100 }, true, ['encrypt']); },\n"
            + "      function() { return subtle.generateKey({ name: 'AES-KW', length: 512 }, true, ['wrapKey']); },\n"
            + "      function() { return subtle.generateKey(rsa([1], 2048), true, ['sign']); },\n"
            + "      function() { return subtle.generateKey(rsa([4], 2048), true, ['sign']); },\n"
            + "      function() { return subtle.generateKey(rsa([1, 0, 1], 0), true, ['sign']); }\n"
            + "    ]);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * RSA operations that fail (salt too long, message too long).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"OperationError/DOMException", "OperationError/DOMException"})
    public void rsaOperationError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array(16);\n"
            + "    function params(name) {\n"
            + "      return { name: name, modulusLength: 2048,\n"
            + "               publicExponent: new Uint8Array([1, 0, 1]), hash: 'SHA-256' };\n"
            + "    }\n"
            + "    Promise.all([\n"
            + "      subtle.generateKey(params('RSA-PSS'), false, ['sign', 'verify']),\n"
            + "      subtle.generateKey(params('RSA-OAEP'), false, ['encrypt', 'decrypt'])\n"
            + "    ]).then(function(pairs) {\n"
            + "      var pss = { name: 'RSA-PSS', saltLength: 1000 };\n"
            + "      var tooLong = new Uint8Array(300);\n"
            + "      run([\n"
            + "        function() { return subtle.sign(pss, pairs[0].privateKey, data); },\n"
            + "        function() { return subtle.encrypt({ name: 'RSA-OAEP' }, pairs[1].publicKey, tooLong); }\n"
            + "      ]);\n"
            + "    });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * generateKey() HMAC with length 0.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "OperationError/DOMException",
            FF = "DataError/DOMException",
            FF_ESR = "DataError/DOMException")
    @HtmlUnitNYI(FF = "OperationError/DOMException",
            FF_ESR = "OperationError/DOMException")
    public void generateKeyHmacLengthZero() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array(16);\n"
            + "    var params = { name: 'HMAC', hash: 'SHA-256', length: 0 };\n"
            + "    run([\n"
            + "      function() { return subtle.generateKey(params, true, ['sign']); }\n"
            + "    ]);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * importKey() raw AES with key data that is not 128, 192 or 256 bits long. The key data is checked
     * before the usages.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"DataError/DOMException", "DataError/DOMException",
             "DataError/DOMException", "SyntaxError/DOMException"})
    public void importKeyRawAesDataError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    function importAes(name, size, usages) {\n"
            + "      return function() { return subtle.importKey('raw', new Uint8Array(size), name, true, usages); };\n"
            + "    }\n"
            + "    run([\n"
            + "      importAes('AES-GCM', 20, ['encrypt']),\n"
            + "      importAes('AES-CBC', 0, ['encrypt']),\n"
            + "      importAes('AES-GCM', 20, []),\n"
            + "      importAes('AES-GCM', 16, [])\n"
            + "    ]);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * importKey() raw HMAC with empty key data or a length that does not fit the key data.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"DataError/DOMException", "DataError/DOMException",
                       "DataError/DOMException", "DataError/DOMException",
                       "success"},
            FF = {"DataError/DOMException", "success", "success", "success", "success"},
            FF_ESR = {"DataError/DOMException", "success", "success", "success", "success"})
    @HtmlUnitNYI(
            FF = {"DataError/DOMException", "DataError/DOMException",
                  "DataError/DOMException", "DataError/DOMException",
                  "success"},
            FF_ESR = {"DataError/DOMException", "DataError/DOMException",
                      "DataError/DOMException", "DataError/DOMException",
                      "success"})
    public void importKeyRawHmacDataError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    function importHmac(size, length) {\n"
            + "      var params = { name: 'HMAC', hash: 'SHA-256' };\n"
            + "      if (length !== undefined) {\n"
            + "        params.length = length;\n"
            + "      }\n"
            + "      var data = new Uint8Array(size);\n"
            + "      return function() { return subtle.importKey('raw', data, params, true, ['sign']); };\n"
            + "    }\n"
            + "    run([\n"
            + "      importHmac(0),\n"
            + "      importHmac(32, 264),\n"
            + "      importHmac(32, 248),\n"
            + "      importHmac(32, 0),\n"
            + "      importHmac(32, 256)\n"
            + "    ]);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Algorithm parameters without a required member, or a plain name where parameters are required.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"TypeError", "TypeError",
                       "TypeError", "TypeError",
                       "TypeError", "TypeError",
                       "TypeError", "TypeError",
                       "success"},
            FF = {"InvalidAccessError/DOMException", "InvalidAccessError/DOMException",
                  "OperationError/DOMException", "SyntaxError/DOMException",
                  "SyntaxError/DOMException", "SyntaxError/DOMException",
                  "SyntaxError/DOMException", "SyntaxError/DOMException",
                  "success"},
            FF_ESR = {"InvalidAccessError/DOMException", "InvalidAccessError/DOMException",
                      "OperationError/DOMException", "SyntaxError/DOMException",
                      "SyntaxError/DOMException", "SyntaxError/DOMException",
                      "SyntaxError/DOMException", "SyntaxError/DOMException",
                      "success"})
    @HtmlUnitNYI(
            FF = {"TypeError", "TypeError", "TypeError", "TypeError",
                  "TypeError", "TypeError", "TypeError", "TypeError",
                  "success"},
            FF_ESR = {"TypeError", "TypeError", "TypeError", "TypeError",
                      "TypeError", "TypeError", "TypeError", "TypeError",
                      "success"})
    public void missingAlgorithmParamsTypeError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array(16);\n"
            + "    var exponent = new Uint8Array([1, 0, 1]);\n"
            + "    function importAes(name) {\n"
            + "      return subtle.importKey('raw', data, name, false, ['encrypt', 'decrypt']);\n"
            + "    }\n"
            + "    function rsa(name) {\n"
            + "      return { name: name, modulusLength: 2048, publicExponent: exponent, hash: 'SHA-256' };\n"
            + "    }\n"
            + "    Promise.all([\n"
            + "      importAes('AES-CBC'), importAes('AES-GCM'), importAes('AES-CTR'),\n"
            + "      subtle.generateKey(rsa('RSA-PSS'), false, ['sign', 'verify']),\n"
            + "      subtle.generateKey({ name: 'ECDSA', namedCurve: 'P-256' }, false, ['sign', 'verify']),\n"
            + "      subtle.generateKey(rsa('RSA-OAEP'), false, ['encrypt', 'decrypt'])\n"
            + "    ]).then(function(keys) {\n"
            + "      run([\n"
            + "        function() { return subtle.encrypt({ name: 'AES-CBC' }, keys[0], data); },\n"
            + "        function() { return subtle.encrypt('AES-CBC', keys[0], data); },\n"
            + "        function() { return subtle.encrypt({ name: 'AES-GCM' }, keys[1], data); },\n"
            + "        function() { return subtle.encrypt({ name: 'AES-CTR', length: 64 }, keys[2], data); },\n"
            + "        function() { return subtle.encrypt({ name: 'AES-CTR', counter: data }, keys[2], data); },\n"
            + "        function() { return subtle.sign({ name: 'RSA-PSS' }, keys[3].privateKey, data); },\n"
            + "        function() { return subtle.sign({ name: 'ECDSA' }, keys[4].privateKey, data); },\n"
            + "        function() { return subtle.sign('ECDSA', keys[4].privateKey, data); },\n"
            + "        // RSA-OAEP has no required parameters\n"
            + "        function() { return subtle.encrypt('RSA-OAEP', keys[5].publicKey, data); }\n"
            + "      ]);\n"
            + "    });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Key generation/import parameters without a required member.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"TypeError", "TypeError",
                       "TypeError", "TypeError",
                       "TypeError", "TypeError",
                       "TypeError"},
            FF = {"TypeError", "TypeError",
                  "SyntaxError/DOMException", "TypeError",
                  "TypeError", "TypeError",
                  "SyntaxError/DOMException"},
            FF_ESR = {"SyntaxError/DOMException", "SyntaxError/DOMException",
                      "SyntaxError/DOMException", "SyntaxError/DOMException",
                      "SyntaxError/DOMException", "SyntaxError/DOMException",
                      "SyntaxError/DOMException"})
    @HtmlUnitNYI(
            FF = {"TypeError", "TypeError", "TypeError", "TypeError",
                  "TypeError", "TypeError", "TypeError"},
            FF_ESR = {"TypeError", "TypeError", "TypeError", "TypeError",
                      "TypeError", "TypeError", "TypeError"})
    public void missingKeyParamsTypeError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array(16);\n"
            + "    var exponent = new Uint8Array([1, 0, 1]);\n"
            + "    function generate(params) {\n"
            + "      return function() { return subtle.generateKey(params, true, ['sign']); };\n"
            + "    }\n"
            + "    run([\n"
            + "      function() { return subtle.generateKey({ name: 'AES-GCM' }, true, ['encrypt']); },\n"
            + "      generate({ name: 'HMAC' }),\n"
            + "      generate({ name: 'ECDSA' }),\n"
            + "      generate({ name: 'RSA-PSS', publicExponent: exponent, hash: 'SHA-256' }),\n"
            + "      generate({ name: 'RSA-PSS', modulusLength: 2048, hash: 'SHA-256' }),\n"
            + "      generate({ name: 'RSA-PSS', modulusLength: 2048, publicExponent: exponent }),\n"
            + "      function() { return subtle.importKey('raw', data, { name: 'HMAC' }, true, ['sign']); }\n"
            + "    ]);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * An algorithm object needs a name; other values are converted to a string.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "NotSupportedError/DOMException",
             "NotSupportedError/DOMException", "NotSupportedError/DOMException",
             "NotSupportedError/DOMException"})
    public void algorithmIdentifierConversion() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array(16);\n"
            + "    run([\n"
            + "      function() { return subtle.digest({}, data); },\n"
            + "      function() { return subtle.digest(undefined, data); },\n"
            + "      function() { return subtle.digest(null, data); },\n"
            + "      function() { return subtle.digest(42, data); },\n"
            + "      function() { return subtle.digest({ name: 42 }, data); }\n"
            + "    ]);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Key usages have to be a sequence of known usages; the format has to be a known format.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"TypeError", "TypeError", "TypeError", "success",
                       "SyntaxError/DOMException", "TypeError", "TypeError"},
            FF = {"SyntaxError/DOMException", "TypeError", "TypeError", "success",
                  "SyntaxError/DOMException", "SyntaxError/DOMException", "SyntaxError/DOMException"},
            FF_ESR = {"SyntaxError/DOMException", "TypeError", "TypeError", "success",
                      "SyntaxError/DOMException", "SyntaxError/DOMException", "SyntaxError/DOMException"})
    @HtmlUnitNYI(
            FF = {"TypeError", "TypeError", "TypeError", "success",
                  "SyntaxError/DOMException", "TypeError", "TypeError"},
            FF_ESR = {"TypeError", "TypeError", "TypeError", "success",
                      "SyntaxError/DOMException", "TypeError", "TypeError"})
    public void keyUsagesAndFormatConversion() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var params = { name: 'AES-GCM', length: 128 };\n"
            + "    var data = new Uint8Array(16);\n"
            + "    run([\n"
            + "      function() { return subtle.generateKey(params, true, ['foo']); },\n"
            + "      function() { return subtle.generateKey(params, true); },\n"
            + "      function() { return subtle.generateKey(params, true, 'encrypt'); },\n"
            + "      function() { return subtle.generateKey(params, true, new Set(['encrypt'])); },\n"
            + "      function() { return subtle.generateKey(params, true, ['sign']); },\n"
            + "      function() { return subtle.importKey('foo', data, 'AES-GCM', true, ['encrypt']); },\n"
            + "      function() {\n"
            + "        return subtle.generateKey(params, true, ['encrypt']).then(function(key) {\n"
            + "          return subtle.exportKey('foo', key);\n"
            + "        });\n"
            + "      }\n"
            + "    ]);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Arguments and members that have to be a BufferSource.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"TypeError", "TypeError", "TypeError", "TypeError", "TypeError", "TypeError"},
            FF= {"TypeError", "TypeError", "TypeError", "InvalidAccessError/DOMException", "TypeError", "TypeError"},
            FF_ESR= {"TypeError", "TypeError", "TypeError", "InvalidAccessError/DOMException", "TypeError", "TypeError"})
    @HtmlUnitNYI(FF = {"TypeError", "TypeError", "TypeError", "TypeError", "TypeError", "TypeError"},
            FF_ESR = {"TypeError", "TypeError", "TypeError", "TypeError", "TypeError", "TypeError"})
    public void bufferArgumentsTypeError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(calls) {\n"
            + "    var chain = Promise.resolve();\n"
            + "    calls.forEach(function(call) {\n"
            + "      chain = chain.then(call).then(function() { log('success'); }, logEx);\n"
            + "    });\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array(16);\n"
            + "    function importAes(name) {\n"
            + "      return subtle.importKey('raw', data, name, false, ['encrypt', 'decrypt']);\n"
            + "    }\n"
            + "    var gcm = { name: 'AES-GCM', iv: new Uint8Array(12) };\n"
            + "    var cbc = { name: 'AES-CBC', iv: '0123456789abcdef' };\n"
            + "    Promise.all([\n"
            + "      importAes('AES-GCM'), importAes('AES-CBC'),\n"
            + "      subtle.generateKey({ name: 'HMAC', hash: 'SHA-256' }, false, ['sign'])\n"
            + "    ]).then(function(keys) {\n"
            + "      run([\n"
            + "        function() { return subtle.digest('SHA-256', 'text'); },\n"
            + "        function() { return subtle.digest('SHA-256'); },\n"
            + "        function() { return subtle.encrypt(gcm, keys[0], 'text'); },\n"
            + "        function() { return subtle.encrypt(cbc, keys[1], data); },\n"
            + "        function() { return subtle.sign('HMAC', keys[2], 42); },\n"
            + "        function() { return subtle.importKey('raw', 'text', 'AES-GCM', true, ['encrypt']); }\n"
            + "      ]);\n"
            + "    });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * The algorithm and usages objects are cached per key.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false", "false", "false", "P-256", "P-256"},
            FF= {"true", "true", "false", "P-256", "P-256"},
            FF_ESR = {"true", "true", "false", "P-256", "P-256"})
    @HtmlUnitNYI(CHROME = {"true", "true", "false", "P-256", "P-256"},
            EDGE = {"true", "true", "false", "P-256", "P-256"})
    public void keyAlgorithmAndUsagesCachedPerKey() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    subtle.generateKey({ name: 'AES-GCM', length: 128 }, true, ['encrypt'])\n"
            + "      .then(function(key) {\n"
            + "        log(key.algorithm === key.algorithm);\n"
            + "        log(key.usages === key.usages);\n"
            + "        return subtle.generateKey({ name: 'ECDSA', namedCurve: 'P-256' }, true, ['sign', 'verify']);\n"
            + "      })\n"
            + "      .then(function(pair) {\n"
            + "        log(pair.publicKey.algorithm === pair.privateKey.algorithm);\n"
            + "        log(pair.publicKey.algorithm.namedCurve);\n"
            + "        log(pair.privateKey.algorithm.namedCurve);\n"
            + "      })\n"
            + "      .catch(logEx);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Changing the algorithm object of a key does not change the key.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "InvalidAccessError/DOMException", "32"})
    public void keyAlgorithmModificationIgnored() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var subtle = window.crypto.subtle;\n"
            + "    var data = new Uint8Array(16);\n"
            + "    var rsa = { name: 'RSASSA-PKCS1-v1_5', modulusLength: 2048,\n"
            + "                publicExponent: new Uint8Array([1, 0, 1]), hash: 'SHA-256' };\n"
            + "    subtle.generateKey(rsa, false, ['sign', 'verify'])\n"
            + "      .then(function(pair) {\n"
            + "        pair.privateKey.algorithm.name = 'RSA-PSS';\n"
            + "        pair.privateKey.algorithm.hash = { name: 'SHA-1' };\n"
            + "        return subtle.sign('RSASSA-PKCS1-v1_5', pair.privateKey, data)\n"
            + "          .then(function(signature) {\n"
            + "            return subtle.verify('RSASSA-PKCS1-v1_5', pair.publicKey, signature, data);\n"
            + "          })\n"
            + "          .then(log, logEx)\n"
            + "          .then(function() {\n"
            + "            return subtle.sign({ name: 'RSA-PSS', saltLength: 32 }, pair.privateKey, data);\n"
            + "          })\n"
            + "          .then(function() { log('success'); }, logEx);\n"
            + "      })\n"
            + "      .then(function() {\n"
            + "        return subtle.generateKey({ name: 'AES-GCM', length: 128 }, false, ['encrypt']);\n"
            + "      })\n"
            + "      .then(function(key) {\n"
            + "        key.algorithm.name = 'AES-CBC';\n"
            + "        return subtle.encrypt({ name: 'AES-GCM', iv: new Uint8Array(12) }, key, data);\n"
            + "      })\n"
            + "      .then(function(ciphertext) { log(ciphertext.byteLength); }, logEx);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
