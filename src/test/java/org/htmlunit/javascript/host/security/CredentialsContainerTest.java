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
 * Tests for {@link CredentialsContainer}.
 *
 * @author Ronald Brill
 */
public class CredentialsContainerTest extends WebDriverTestCase {

    /**
     * Without an authenticator the browsers show a dialog and wait for the user; Chrome clamps
     * the timeout of the request to at least 3 minutes. If the promise is still pending after half a second,
     * 'still pending' is logged and the request is aborted (this closes the dialog).
     * Expects the variables {@code promise} and {@code controller}.
     */
    private static final String WAIT_FOR_SETTLED =
        "    var settled = false;\n"
        + "    promise.then(function(r) { settled = true; log('resolved ' + r); },\n"
        + "                 function(e) { settled = true; logEx(e); });\n"
        + "    window.setTimeout(function() {\n"
        + "      if (!settled) { log('still pending'); controller.abort(); }\n"
        + "    }, 500);\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"object", "[object CredentialsContainer]", "true"})
    public void navigatorCredentials() throws Exception {
        final String js =
            "    log(typeof navigator.credentials);\n"
            + "    log(String(navigator.credentials));\n"
            + "    log(navigator.credentials === navigator.credentials);\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "TypeError"})
    public void ctor() throws Exception {
        final String js =
            "    log(typeof CredentialsContainer);\n"
            + "    new CredentialsContainer();\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function", "function", "function"})
    public void functions() throws Exception {
        final String js =
            "    var c = navigator.credentials;\n"
            + "    log(typeof c.create);\n"
            + "    log(typeof c.get);\n"
            + "    log(typeof c.store);\n"
            + "    log(typeof c.preventSilentAccess);\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void createReturnsPromise() throws Exception {
        final String js =
            "    var p = navigator.credentials.create(42);\n"
            + "    p.catch(function() {});\n"
            + "    log(p instanceof Promise);\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("NotSupportedError/DOMException")
    public void createWithoutOptions() throws Exception {
        final String js =
            "    navigator.credentials.create()\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("NotSupportedError/DOMException")
    public void createEmptyOptions() throws Exception {
        final String js =
            "    navigator.credentials.create({})\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void createInvalidOptions() throws Exception {
        final String js =
            "    navigator.credentials.create(42)\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "NotSupportedError/DOMException",
            FF = "UnknownError/DOMException",
            FF_ESR = "UnknownError/DOMException")
    public void createTwoTypes() throws Exception {
        final String js =
            "    var pk = { challenge: new Uint8Array(16), rp: { name: 'HtmlUnit' },\n"
            + "               user: { id: new Uint8Array(8), name: 'joe', displayName: 'Joe' },\n"
            + "               pubKeyCredParams: [{ type: 'public-key', alg: -7 }], timeout: 1000 };\n"
            + "    navigator.credentials.create({ publicKey: pk, password: { id: 'joe', password: 'secret' } })\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"still pending", "AbortError/DOMException"},
            EDGE = "NotAllowedError/DOMException",
            FF = "UnknownError/DOMException",
            FF_ESR = "UnknownError/DOMException")
    @HtmlUnitNYI(CHROME = "NotAllowedError/DOMException",
            FF = "NotAllowedError/DOMException",
            FF_ESR = "NotAllowedError/DOMException")
    public void createPublicKey() throws Exception {
        final String js =
            "    var pk = { challenge: new Uint8Array(16), rp: { name: 'HtmlUnit' },\n"
            + "               user: { id: new Uint8Array(8), name: 'joe', displayName: 'Joe' },\n"
            + "               pubKeyCredParams: [{ type: 'public-key', alg: -7 }], timeout: 1000 };\n"
            + "    var controller = new AbortController();\n"
            + "    var promise = navigator.credentials.create({ publicKey: pk, signal: controller.signal });\n"
            + WAIT_FOR_SETTLED;
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("AbortError/DOMException")
    public void createPublicKeyAborted() throws Exception {
        final String js =
            "    var pk = { challenge: new Uint8Array(16), rp: { name: 'HtmlUnit' },\n"
            + "               user: { id: new Uint8Array(8), name: 'joe', displayName: 'Joe' },\n"
            + "               pubKeyCredParams: [{ type: 'public-key', alg: -7 }], timeout: 1000 };\n"
            + "    navigator.credentials.create({ publicKey: pk, signal: AbortSignal.abort() })\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rejected reason")
    public void createPublicKeyAbortedReason() throws Exception {
        final String js =
            "    var pk = { challenge: new Uint8Array(16), rp: { name: 'HtmlUnit' },\n"
            + "               user: { id: new Uint8Array(8), name: 'joe', displayName: 'Joe' },\n"
            + "               pubKeyCredParams: [{ type: 'public-key', alg: -7 }], timeout: 1000 };\n"
            + "    var signal = AbortSignal.abort('reason');\n"
            + "    navigator.credentials.create({ publicKey: pk, signal: signal })\n"
            + "      .catch(function(e) { log('rejected ' + e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getReturnsPromise() throws Exception {
        final String js =
            "    var p = navigator.credentials.get(42);\n"
            + "    p.catch(function() {});\n"
            + "    log(p instanceof Promise);\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("NotSupportedError/DOMException")
    public void getWithoutOptions() throws Exception {
        final String js =
            "    navigator.credentials.get()\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("NotSupportedError/DOMException")
    public void getEmptyOptions() throws Exception {
        final String js =
            "    navigator.credentials.get({})\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void getInvalidOptions() throws Exception {
        final String js =
            "    navigator.credentials.get(42)\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "resolved null",
            FF = "NotSupportedError/DOMException",
            FF_ESR = "NotSupportedError/DOMException")
    public void getPassword() throws Exception {
        final String js =
            "    navigator.credentials.get({ password: true })\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "NotAllowedError/DOMException",
            FF = "UnknownError/DOMException",
            FF_ESR = "UnknownError/DOMException")
    @HtmlUnitNYI(FF = "NotAllowedError/DOMException",
            FF_ESR = "NotAllowedError/DOMException")
    public void getPublicKey() throws Exception {
        final String js =
            "    var pk = { challenge: new Uint8Array(16), timeout: 1000 };\n"
            + "    var controller = new AbortController();\n"
            + "    var promise = navigator.credentials.get({ publicKey: pk, signal: controller.signal });\n"
            + WAIT_FOR_SETTLED;
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("AbortError/DOMException")
    public void getPublicKeyAborted() throws Exception {
        final String js =
            "    var pk = { challenge: new Uint8Array(16), timeout: 1000 };\n"
            + "    navigator.credentials.get({ publicKey: pk, signal: AbortSignal.abort() })\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void storeNoCredential() throws Exception {
        final String js =
            "    navigator.credentials.store({})\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void storeWithoutArgument() throws Exception {
        final String js =
            "    navigator.credentials.store()\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("resolved undefined")
    public void preventSilentAccess() throws Exception {
        final String js =
            "    navigator.credentials.preventSilentAccess()\n"
            + "      .then(function(r) { log('resolved ' + r); }, function(e) { logEx(e); });\n";
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