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
package org.htmlunit.javascript.host.fetch;

import java.net.URL;

import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebRequest;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Fetch API in (dedicated) workers.
 *
 * @author Ronald Brill
 */
public class FetchWorkerTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function function function function function function")
    public void available() throws Exception {
        testWorker("postMessage(typeof fetch + ' ' + typeof Headers + ' ' + typeof Request + ' '\n"
                + "    + typeof Response + ' ' + typeof AbortController + ' ' + typeof AbortSignal);\n");
    }

    /**
     * Relative urls are resolved against the url of the worker script.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200 basic http://localhost:22222/sub/data.txt", "data"})
    public void get() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "/sub/data.txt"), "data", MimeType.TEXT_PLAIN);

        testWorker("fetch('data.txt')\n"
                + "  .then(r => { postMessage(r.status + ' ' + r.type + ' ' + r.url); return r.text(); })\n"
                + "  .then(t => postMessage(t))\n"
                + "  .catch(e => postMessage('error ' + e.name));\n");

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals(new URL(URL_FIRST, "/sub/data.txt"), lastRequest.getUrl());
        assertEquals(new URL(URL_FIRST, "/sub/worker.js").toExternalForm(),
                lastRequest.getAdditionalHeader(HttpHeader.REFERER));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a=1")
    public void post() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "/sub/data.txt"), "{\"a\": 1}",
                MimeType.APPLICATION_JSON);

        testWorker("fetch('data.txt', {method: 'POST', body: 'abc', headers: {'X-Custom': 'v'}})\n"
                + "  .then(r => r.json())\n"
                + "  .then(j => postMessage('a=' + j.a))\n"
                + "  .catch(e => postMessage('error ' + e.name));\n");

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals("abc", lastRequest.getRequestBody());
        assertEquals("v", lastRequest.getAdditionalHeader("X-Custom"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http://localhost:22222/sub/data.txt POST", "x"})
    public void request() throws Exception {
        testWorker("var req = new Request('data.txt', {method: 'POST', body: 'x'});\n"
                + "postMessage(req.url + ' ' + req.method);\n"
                + "req.text().then(t => postMessage(t));\n");
    }

    /**
     * The iterators of Headers, URLSearchParams and FormData.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a,1|b,2", "x,1|y,2", "k,v"})
    public void iterators() throws Exception {
        testWorker("postMessage(Array.from(new Headers({b: '2', a: '1'})).join('|'));\n"
                + "postMessage(Array.from(new URLSearchParams('x=1&y=2')).join('|'));\n"
                + "var fd = new FormData();\n"
                + "fd.append('k', 'v');\n"
                + "postMessage(Array.from(fd.entries()).join('|'));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"AbortError", "reason"},
            FF = {"reason", "AbortError"},
            FF_ESR = {"reason", "AbortError"})
    @HtmlUnitNYI(FF = {"AbortError", "reason"},
            FF_ESR = {"AbortError", "reason"})
    public void abort() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "/sub/data.txt"), "data", MimeType.TEXT_PLAIN);

        testWorker("var ctrl = new AbortController();\n"
                + "fetch('data.txt', {signal: ctrl.signal})\n"
                + "  .then(r => postMessage(r.status), e => postMessage(e.name));\n"
                + "ctrl.abort();\n"
                + "fetch('data.txt', {signal: AbortSignal.abort('reason')})\n"
                + "  .then(r => postMessage(r.status), e => postMessage(e));\n");
    }

    /**
     * The abort event inside of a worker.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"listener abort", "handler abort", "true stop"})
    public void abortEvent() throws Exception {
        testWorker("var ctrl = new AbortController();\n"
                + "ctrl.signal.addEventListener('abort', e => postMessage('listener ' + e.type));\n"
                + "ctrl.signal.onabort = e => postMessage('handler ' + e.type);\n"
                + "ctrl.abort('stop');\n"
                + "postMessage(ctrl.signal.aborted + ' ' + ctrl.signal.reason);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"TypeError", "TypeError", "TypeError"},
            FF = "TypeError",
            FF_ESR = "TypeError")
    // Firefox does not report the error of the request to the closed port within the wait time
    @HtmlUnitNYI(FF = {"TypeError", "TypeError", "TypeError"},
            FF_ESR = {"TypeError", "TypeError", "TypeError"})
    public void errors() throws Exception {
        testWorker("fetch()\n"
                + "  .then(r => postMessage(r.status), e => postMessage(e.name))\n"
                + "  .then(() => fetch('" + URL_CROSS_ORIGIN + "'))\n"
                + "  .then(r => postMessage(r.status), e => postMessage(e.name))\n"
                + "  .then(() => fetch('data.txt', {mode: 'navigate'}))\n"
                + "  .then(r => postMessage(r.status), e => postMessage(e.name));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http://localhost:22222/sub/target", "error", "{\"a\":1}"})
    public void responseStatics() throws Exception {
        testWorker("postMessage(Response.redirect('target', 302).headers.get('location'));\n"
                + "Response.json({a: 1}).text().then(t => postMessage(t));\n"
                + "postMessage(Response.error().type);\n");
    }

    /**
     * Loads the worker (/sub/worker.js) with the given code; the messages of the worker are logged.
     */
    private void testWorker(final String workerJs) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var myWorker = new Worker('/sub/worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    log(e.data);\n"
            + "  };\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "/sub/worker.js"), workerJs, MimeType.TEXT_JAVASCRIPT);

        expandExpectedAlertsVariables(URL_FIRST);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
