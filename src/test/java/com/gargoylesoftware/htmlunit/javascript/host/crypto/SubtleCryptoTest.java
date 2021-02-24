/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link SubtleCrypto}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 */
@RunWith(BrowserRunner.class)
public class SubtleCryptoTest extends WebDriverTestCase {

    /**
     * Methods in SubtleCrypto should always wraps errors in a Promise and never throw directly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "TypeError true",
            IE = {})
    @HtmlUnitNYI(CHROME = "TypeError false",
            EDGE = "TypeError false",
            FF = "TypeError false",
            FF78 = "TypeError false")
    public void unsupportedCall() throws Exception {
        final String html
            = ""
            + "<html><head><script>\n"
            + "  function test() {\n"
            + "    if (window.crypto) {\n"
            + "      window.crypto.subtle.generateKey(\n"
            + "        { name: 'x' }\n"
            + "      )\n"
            + "      .catch(function(e) {\n"
            + "        alert('TypeError ' + (e instanceof TypeError));\n"
            + "      });\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME * 3);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Crypto]", "public", "true", "verify",
                        "name RSASSA-PKCS1-v1_5", "hash [object Object]", "modulusLength 2048",
                        "publicExponent 1,0,1",
                        "private", "false", "sign",
                        "name RSASSA-PKCS1-v1_5", "hash [object Object]", "modulusLength 2048",
                        "publicExponent 1,0,1"},
            IE = "undefined")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void rsassa() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(window.crypto);\n"
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
            + "        alert(key.publicKey.type);\n"
            + "        alert(key.publicKey.extractable);\n"
            + "        alert(key.publicKey.usages);\n"
            + "        for(var x in key.publicKey.algorithm) {\n"
            + "          alert(x + ' ' + key.publicKey.algorithm[x]);\n"
            + "        }\n"
            + "        alert(key.privateKey.type);\n"
            + "        alert(key.privateKey.extractable);\n"
            + "        alert(key.privateKey.usages);\n"
            + "        for(var x in key.privateKey.algorithm) {\n"
            + "          alert(x + ' ' + key.publicKey.algorithm[x]);\n"
            + "        }\n"
            + "      })\n"
            + "      .catch(function(err) {\n"
            + "        alert(err);\n"
            + "      });\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME * 3);
    }
}
