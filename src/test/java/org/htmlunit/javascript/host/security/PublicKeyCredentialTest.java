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
package org.htmlunit.javascript.host.security;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PublicKeyCredential}.
 *
 * @author Ronald Brill
 */
public class PublicKeyCredentialTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "TypeError"})
    public void ctor() throws Exception {
        final String js =
            "    log(typeof PublicKeyCredential);\n"
            + "    new PublicKeyCredential();\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void prototypeChain() throws Exception {
        final String js =
            "    log(PublicKeyCredential.prototype instanceof Credential);\n"
            + "    log(Object.getPrototypeOf(PublicKeyCredential.prototype) === Credential.prototype);\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    @HtmlUnitNYI(CHROME = {"toJSON", "done"},
            EDGE = {"toJSON", "done"},
            FF = {"toJSON", "done"},
            FF_ESR = {"toJSON", "done"})
    public void prototypeMembers() throws Exception {
        final String js =
            "    var names = ['authenticatorAttachment', 'getClientExtensionResults', 'id', 'rawId', 'response',\n"
            + "                 'toJSON', 'type'];\n"
            + "    for (var i = 0; i < names.length; i++) {\n"
            + "      if (!(names[i] in PublicKeyCredential.prototype)) { log(names[i]); }\n"
            + "    }\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    @HtmlUnitNYI(CHROME = {"getClientCapabilities", "parseCreationOptionsFromJSON",
                "parseRequestOptionsFromJSON", "done"},
            EDGE = {"getClientCapabilities", "parseCreationOptionsFromJSON",
                "parseRequestOptionsFromJSON", "done"},
            FF = {"getClientCapabilities", "parseCreationOptionsFromJSON",
                "parseRequestOptionsFromJSON", "done"},
            FF_ESR = {"getClientCapabilities", "parseCreationOptionsFromJSON",
                "parseRequestOptionsFromJSON", "done"})
    public void staticMembers() throws Exception {
        final String js =
            "    var names = ['getClientCapabilities', 'isConditionalMediationAvailable',\n"
            + "                 'isUserVerifyingPlatformAuthenticatorAvailable', 'parseCreationOptionsFromJSON',\n"
            + "                 'parseRequestOptionsFromJSON'];\n"
            + "    for (var i = 0; i < names.length; i++) {\n"
            + "      if (typeof PublicKeyCredential[names[i]] != 'function') { log(names[i]); }\n"
            + "    }\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("resolved false")
    public void isUserVerifyingPlatformAuthenticatorAvailable() throws Exception {
        final String js =
            "    PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable()\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("resolved true")
    @HtmlUnitNYI(CHROME = "resolved false",
            EDGE = "resolved false",
            FF = "resolved false",
            FF_ESR = "resolved false")
    public void isConditionalMediationAvailable() throws Exception {
        final String js =
            "    PublicKeyCredential.isConditionalMediationAvailable()\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "TypeError"})
    public void authenticatorResponse() throws Exception {
        final String js =
            "    log(typeof AuthenticatorResponse);\n"
            + "    new AuthenticatorResponse();\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void authenticatorResponseMembers() throws Exception {
        final String js =
            "    log('clientDataJSON' in AuthenticatorResponse.prototype);\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "true", "TypeError"})
    public void authenticatorAttestationResponse() throws Exception {
        final String js =
            "    log(typeof AuthenticatorAttestationResponse);\n"
            + "    log(AuthenticatorAttestationResponse.prototype instanceof AuthenticatorResponse);\n"
            + "    new AuthenticatorAttestationResponse();\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void authenticatorAttestationResponseMembers() throws Exception {
        final String js =
            "    var names = ['attestationObject', 'getAuthenticatorData', 'getPublicKey', 'getPublicKeyAlgorithm',\n"
            + "                 'getTransports'];\n"
            + "    for (var i = 0; i < names.length; i++) {\n"
            + "      if (!(names[i] in AuthenticatorAttestationResponse.prototype)) { log(names[i]); }\n"
            + "    }\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "true", "TypeError"})
    public void authenticatorAssertionResponse() throws Exception {
        final String js =
            "    log(typeof AuthenticatorAssertionResponse);\n"
            + "    log(AuthenticatorAssertionResponse.prototype instanceof AuthenticatorResponse);\n"
            + "    new AuthenticatorAssertionResponse();\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void authenticatorAssertionResponseMembers() throws Exception {
        final String js =
            "    var names = ['authenticatorData', 'signature', 'userHandle'];\n"
            + "    for (var i = 0; i < names.length; i++) {\n"
            + "      if (!(names[i] in AuthenticatorAssertionResponse.prototype)) { log(names[i]); }\n"
            + "    }\n"
            + "    log('done');\n";
        test(js);
    }

    private void test(final String js) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + js
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }
}
