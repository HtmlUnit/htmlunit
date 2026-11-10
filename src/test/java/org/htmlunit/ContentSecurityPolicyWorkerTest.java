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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the Content-Security-Policy directive worker-src (and the fallbacks child-src,
 * script-src and default-src).
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function
 * and collects all securitypolicyviolation events. The violations are logged at the end
 * (200ms after the load event) because the browsers fire the violation events async.
 * The test code is placed in the external script 'test.js'. All policies allow these scripts.
 * The function startWorker() creates a worker; the worker posts 'worker started'.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyWorkerTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    private static final String WORKER_JS = "postMessage('worker started');\n";
    private static final String DATA_WORKER = "data:text/javascript,postMessage('worker started');";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "var violations = [];\n"
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  violations.push('violation ' + e.violatedDirective + ' ' + e.blockedURI + ' ' + e.disposition);\n"
            + "});\n"
            + "window.addEventListener('load', function() {\n"
            + "  setTimeout(function() {\n"
            + "    for (var i = 0; i < violations.length; i++) { log(violations[i]); }\n"
            + "  }, 200);\n"
            + "});\n"
            + "function startWorker(url) {\n"
            + "  try {\n"
            + "    var worker = new Worker(url);\n"
            + "    worker.onmessage = function(e) { log(e.data); };\n"
            + "    worker.onerror = function(e) { log('error'); };\n"
            + "  } catch(e) { log('exception ' + e.name); }\n"
            + "}\n";

    /**
     * Without a policy the worker is started.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("worker started")
    public void noPolicy() throws Exception {
        loadPageVerify(null, "startWorker('worker.js');\n");
    }

    /**
     * Same origin worker, worker-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("worker started")
    public void workerSrcSelf() throws Exception {
        loadPageVerify("worker-src 'self'", "startWorker('worker.js');\n");
    }

    /**
     * The directive worker-src 'none' blocks the worker.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "violation worker-src §§URL§§/worker.js enforce"})
    public void workerSrcNone() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        loadPageVerify("worker-src 'none'", "startWorker('worker.js');\n");
    }

    /**
     * Child-src is the fallback for worker-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "violation worker-src §§URL§§/worker.js enforce"})
    public void childSrcFallback() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        loadPageVerify("child-src 'none'", "startWorker('worker.js');\n");
    }

    /**
     * Script-src is the fallback for worker-src and child-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("worker started")
    public void scriptSrcFallback() throws Exception {
        loadPageVerify("script-src 'self'", "startWorker('worker.js');\n");
    }

    /**
     * Script-src is the fallback for worker-src; data: is not allowed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "violation worker-src data enforce"})
    public void scriptSrcFallback_data() throws Exception {
        loadPageVerify("script-src 'self'", "startWorker(\"" + DATA_WORKER + "\");\n");
    }

    /**
     * Default-src is the fallback for worker-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "violation worker-src data enforce"})
    public void defaultSrcFallback() throws Exception {
        loadPageVerify("default-src 'self'", "startWorker(\"" + DATA_WORKER + "\");\n");
    }

    /**
     * Worker-src wins over script-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "violation worker-src §§URL§§/worker.js enforce"})
    public void workerSrcOverridesScriptSrc() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        loadPageVerify("script-src 'self'; worker-src 'none'", "startWorker('worker.js');\n");
    }

    /**
     * Worker-src wins over child-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("worker started")
    public void workerSrcOverridesChildSrc() throws Exception {
        loadPageVerify("child-src 'none'; worker-src 'self'", "startWorker('worker.js');\n");
    }

    /**
     * Data url worker allowed by worker-src data:.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("worker started")
    public void dataUrl() throws Exception {
        loadPageVerify("worker-src data:", "startWorker(\"" + DATA_WORKER + "\");\n");
    }

    /**
     * Blob url worker blocked by worker-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "violation worker-src blob enforce"})
    public void blobUrl_self() throws Exception {
        loadPageVerify("worker-src 'self'",
                "var blob = new Blob([\"" + WORKER_JS.trim() + "\"], { type: 'text/javascript' });\n"
                + "startWorker(URL.createObjectURL(blob));\n");
    }

    /**
     * Blob url worker allowed by worker-src blob:.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("worker started")
    public void blobUrl_blob() throws Exception {
        loadPageVerify("worker-src blob:",
                "var blob = new Blob([\"" + WORKER_JS.trim() + "\"], { type: 'text/javascript' });\n"
                + "startWorker(URL.createObjectURL(blob));\n");
    }

    /**
     * Report only policies do not block the worker.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"worker started", "violation worker-src §§URL§§/worker.js report"})
    public void reportOnly() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        loadPageVerify(HEADER_REPORT_ONLY, "worker-src 'none'", "", "startWorker('worker.js');\n");
    }

    /**
     * The policy can be delivered by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "violation worker-src §§URL§§/worker.js enforce"})
    public void meta() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content=\"worker-src 'none'\">\n",
                "startWorker('worker.js');\n");
    }

    /**
     * The error event listener of a blocked worker.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"created",
             "error error false",
             "violation worker-src §§URL§§/worker.js enforce"})
    public void errorEvent() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        loadPageVerify("worker-src 'none'",
                "var worker = new Worker('worker.js');\n"
                + "worker.addEventListener('error', function(e) {\n"
                + "  log('error ' + e.type + ' ' + (e instanceof ErrorEvent));\n"
                + "});\n"
                + "log('created');\n");
    }

    private void loadPageVerify(final String policy, final String testJs) throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, policy, "", testJs);
    }

    /**
     * Loads a page containing the harness script, the given head and the test script;
     * the page is delivered with the given policy header.
     */
    private void loadPageVerify(final String header, final String policy,
            final String head, final String testJs) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script src='harness.js'></script>\n"
            + head
            + "</head>\n"
            + "<body>\n"
            + "<script src='test.js'></script>\n"
            + "</body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        if (policy != null) {
            headers.add(new NameValuePair(header, policy));
        }
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, headers);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "harness.js"), HARNESS_JS,
                MimeType.TEXT_JAVASCRIPT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"), testJs,
                MimeType.TEXT_JAVASCRIPT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), WORKER_JS,
                MimeType.TEXT_JAVASCRIPT);

        final WebDriver driver = loadPage2(URL_FIRST, ISO_8859_1);
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }
}
