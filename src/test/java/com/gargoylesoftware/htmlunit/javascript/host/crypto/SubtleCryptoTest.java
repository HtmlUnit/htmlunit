/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.crypto;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link SubtleCrypto}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class SubtleCryptoTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Crypto]", "public", "true", "verify",
                        "name RSASSA-PKCS1-v1_5", "hash [object Object]", "modulusLength 2048",
                        "publicExponent [object Uint8Array]",
                        "private", "false", "sign",
                        "name RSASSA-PKCS1-v1_5", "hash [object Object]", "modulusLength 2048",
                        "publicExponent [object Uint8Array]" },
            IE = "undefined")
    @NotYetImplemented({ CHROME, FF })
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
            + "      .then(function(key){\n"
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
            + "      .catch(function(err){\n"
            + "        alert(err);\n"
            + "      });\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME * 3);
    }
}
