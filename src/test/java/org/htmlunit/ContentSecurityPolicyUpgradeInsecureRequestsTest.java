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
package org.htmlunit;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the Content-Security-Policy directive upgrade-insecure-requests.
 * <p>
 * The browsers do not upgrade requests to loopback hosts (localhost); the host_xxx tests
 * are loading the resources from host1.htmlunit-dev.org, this requires a special setup in
 * {@code etc/hosts} (see CookieManager4Test). The test server does not support https;
 * therefore upgraded requests are failing.
 * More tests (navigations, frames, forms) are part of ContentSecurityPolicyUpgradeInsecureRequests2Test.
 * </p>
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function
 * and collects all events and securitypolicyviolation events. The events (sorted) and the
 * violations are logged at the end (200ms after the load event).
 * The test code is placed in the external script 'test.js'. All policies allow these scripts.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyUpgradeInsecureRequestsTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";
    private static final String HOST1 = "http://host1.htmlunit-dev.org";
    private static final String UIR = "upgrade-insecure-requests";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "var events = [];\n"
            + "var violations = [];\n"
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  violations.push('violation ' + e.violatedDirective + ' ' + e.blockedURI + ' ' + e.disposition);\n"
            + "});\n"
            + "window.addEventListener('load', function() {\n"
            + "  setTimeout(function() {\n"
            + "    events.sort();\n"
            + "    for (var i = 0; i < events.length; i++) { log(events[i]); }\n"
            + "    for (var i = 0; i < violations.length; i++) { log(violations[i]); }\n"
            + "    log('done');\n"
            + "  }, 200);\n"
            + "});\n";

    private static final String TEST_JS =
            "var img = document.createElement('img');\n"
            + "img.onload = function() { events.push('img load'); };\n"
            + "img.onerror = function() { events.push('img error'); };\n"
            + "img.src = BASE + 'img.jpg';\n"
            + "document.body.appendChild(img);\n"
            + "var script = document.createElement('script');\n"
            + "script.onload = function() { events.push('script load'); };\n"
            + "script.onerror = function() { events.push('script error'); };\n"
            + "script.src = BASE + 'script.js';\n"
            + "document.body.appendChild(script);\n"
            + "var xhr = new XMLHttpRequest();\n"
            + "xhr.onload = function() { events.push('xhr ' + xhr.status + ' ' + xhr.responseURL); };\n"
            + "xhr.onerror = function() { events.push('xhr error'); };\n"
            + "xhr.open('GET', XHR_URL);\n"
            + "xhr.send();\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Without a policy.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"img load",
             "script load",
             "xhr 200 §§URL§§/data.txt",
             "done"})
    public void noPolicy() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null, "");
    }

    /**
     * Requests to localhost are not upgraded.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"img load",
             "script load",
             "xhr 200 §§URL§§/data.txt",
             "done"})
    public void localhostNotUpgraded() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, UIR, "");
    }

    /**
     * The not upgraded image url is blocked by img-src https: (Firefox treats the http url
     * like a https url because the page upgrades insecure requests).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"img error",
                       "script load",
                       "xhr 200 §§URL§§/data.txt",
                       "violation img-src http://localhost:§§PORT§§/img.jpg enforce",
                       "done"},
            FF = {"img load",
                  "script load",
                  "xhr 200 §§URL§§/data.txt",
                  "done"},
            FF_ESR = {"img load",
                      "script load",
                      "xhr 200 §§URL§§/data.txt",
                      "done"})
    public void localhostImgSrcHttps() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, "img-src https:; " + UIR, "");
    }

    /**
     * The directive is ignored for report only policies.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"img load",
             "script load",
             "xhr 200 §§URL§§/data.txt",
             "done"})
    public void reportOnly() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(HEADER_REPORT_ONLY, UIR, "");
    }

    /**
     * The directive delivered by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"img load",
             "script load",
             "xhr 200 §§URL§§/data.txt",
             "done"})
    public void meta() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content='" + UIR + "'>\n");
    }

    /**
     * Without a policy; the resources are loaded from host1.htmlunit-dev.org (not a loopback host).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"img load",
             "script load",
             "xhr 200 §§URL§§/data.txt",
             "done"})
    public void host_noPolicy() throws Exception {
        expandExpectedAlertsVariables(HOST1 + ":" + PORT, PORT2);
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null, "", HOST1);
    }

    /**
     * Requests to host1.htmlunit-dev.org are upgraded to https; the test server does
     * not support https, therefore the requests are failing.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"img error",
             "script error",
             "xhr error",
             "done"})
    public void host_upgraded() throws Exception {
        expandExpectedAlertsVariables(HOST1 + ":" + PORT, PORT2);
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, UIR, "", HOST1);
    }

    /**
     * The upgraded image url is allowed by img-src https:.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"img error",
             "script error",
             "xhr error",
             "done"})
    public void host_imgSrcHttps() throws Exception {
        expandExpectedAlertsVariables(HOST1 + ":" + PORT, PORT2);
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, "img-src https:; " + UIR, "", HOST1);
    }

    /**
     * The directive is ignored for report only policies.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"img load",
             "script load",
             "xhr 200 §§URL§§/data.txt",
             "done"})
    public void host_reportOnly() throws Exception {
        expandExpectedAlertsVariables(HOST1 + ":" + PORT, PORT2);
        loadPageVerify(HEADER_REPORT_ONLY, UIR, "", HOST1);
    }

    /**
     * The directive delivered by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"img error",
             "script error",
             "xhr error",
             "done"})
    public void host_meta() throws Exception {
        expandExpectedAlertsVariables(HOST1 + ":" + PORT, PORT2);
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content='" + UIR + "'>\n", HOST1);
    }

    private void loadPageVerify(final String header, final String policy, final String head) throws Exception {
        loadPageVerify(header, policy, head, "http://localhost");
    }

    /**
     * Loads a page containing the harness script, the given head and the test script;
     * the page is delivered with the given policy header.
     */
    private void loadPageVerify(final String header, final String policy, final String head,
            final String host) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + head
            + "<script src='harness.js'></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script src='test.js'></script>\n"
            + "</body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        if (policy != null) {
            headers.add(new NameValuePair(header, policy));
        }
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, headers);
        // the resources are loaded from the second port of the given host; the xhr from
        // the page itself (localhost) or from the given host (with cors header)
        final URL base = new URL(host + ":" + PORT2 + "/");
        final URL xhrUrl = "http://localhost".equals(host)
                ? new URL(URL_FIRST, "data.txt") : new URL(host + ":" + PORT + "/data.txt");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "harness.js"),
                "var BASE = '" + base + "';\nvar XHR_URL = '" + xhrUrl + "';\n" + HARNESS_JS,
                MimeType.TEXT_JAVASCRIPT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"), TEST_JS, MimeType.TEXT_JAVASCRIPT);
        final List<NameValuePair> corsHeaders = new ArrayList<>();
        corsHeaders.add(new NameValuePair("Access-Control-Allow-Origin", "*"));
        getMockWebConnection().setResponse(xhrUrl, "data", 200, "OK", MimeType.TEXT_PLAIN, corsHeaders);
        getMockWebConnection().setResponse(new URL(base, "script.js"), "var x = 1;", MimeType.TEXT_JAVASCRIPT);
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] bytes = IOUtils.toByteArray(is);
            getMockWebConnection().setResponse(new URL(base, "img.jpg"), bytes, 200, "ok",
                    "image/jpg", Collections.emptyList());
        }

        // the test server does not support https; the upgraded requests are failing
        final String https = host.replace("http://", "https://");
        final IOException sslError = new IOException("https is not supported by the test server");
        getMockWebConnection().setThrowable(new URL(https + ":" + PORT2 + "/img.jpg"), sslError);
        getMockWebConnection().setThrowable(new URL(https + ":" + PORT2 + "/script.js"), sslError);
        getMockWebConnection().setThrowable(new URL(https + ":" + PORT + "/data.txt"), sslError);

        final WebDriver driver = loadPage2(URL_FIRST, ISO_8859_1);
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }
}
