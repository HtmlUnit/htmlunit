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
package org.htmlunit.html.csp;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the Content-Security-Policy directive connect-src (and default-src)
 * used for XMLHttpRequest.
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function
 * and logs all securitypolicyviolation events. The test code is placed in the external
 * script 'test.js'. All policies allow these scripts.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyConnectTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  log('violation ' + e.violatedDirective + ' ' + e.blockedURI);\n"
            + "});\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Without a policy all requests are allowed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load 200 cross")
    public void noPolicy() throws Exception {
        setCrossOriginResponse("data.txt");
        loadPageVerify(null, xhrAsync(URL_CROSS_ORIGIN_BASE + "data.txt"));
    }

    /**
     * Same origin request, connect-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load 200 data")
    public void connectSrcSelf_sameOrigin() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "data.txt"), "data", MimeType.TEXT_PLAIN);
        loadPageVerify("connect-src 'self'", xhrAsync("data.txt"));
    }

    /**
     * Cross origin request, connect-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation connect-src §§URL§§/data.txt", "error 4 0"})
    public void connectSrcSelf_crossOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setCrossOriginResponse("data.txt");
        loadPageVerify("connect-src 'self'", xhrAsync(URL_CROSS_ORIGIN_BASE + "data.txt"));
    }

    /**
     * Cross origin request, the origin is listed in connect-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load 200 cross")
    public void connectSrcHostSource() throws Exception {
        setCrossOriginResponse("data.txt");
        loadPageVerify("connect-src 'self' http://localhost:" + PORT2, xhrAsync(URL_CROSS_ORIGIN_BASE + "data.txt"));
    }

    /**
     * A host source with a path matches this path.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load 200 data")
    public void connectSrcPath_allowed() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "api/data.txt"), "data", MimeType.TEXT_PLAIN);
        loadPageVerify("connect-src http://localhost:" + PORT + "/api/", xhrAsync("api/data.txt"));
    }

    /**
     * A host source with a path does not match other paths.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation connect-src §§URL§§/other/data.txt", "error 4 0"})
    public void connectSrcPath_blocked() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "other/data.txt"), "other", MimeType.TEXT_PLAIN);
        loadPageVerify("connect-src http://localhost:" + PORT + "/api/", xhrAsync("other/data.txt"));
    }

    /**
     * The directive connect-src 'none' blocks all requests.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation connect-src §§URL§§/data.txt", "error 4 0"})
    public void connectSrcNone() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "data.txt"), "data", MimeType.TEXT_PLAIN);
        loadPageVerify("connect-src 'none'", xhrAsync("data.txt"));
    }

    /**
     * The wildcard allows all network requests.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load 200 cross")
    public void connectSrcStar() throws Exception {
        setCrossOriginResponse("data.txt");
        loadPageVerify("connect-src *", xhrAsync(URL_CROSS_ORIGIN_BASE + "data.txt"));
    }

    /**
     * The default-src directive is used if there is no connect-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation connect-src §§URL§§/data.txt", "error 4 0"})
    public void defaultSrcSelf_crossOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setCrossOriginResponse("data.txt");
        loadPageVerify("default-src 'self'", xhrAsync(URL_CROSS_ORIGIN_BASE + "data.txt"));
    }

    /**
     * The connect-src directive overrides default-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "load 200 data",
            FF = {"violation img-src §§URL§§/favicon.ico", "load 200 data"},
            FF_ESR = {"violation img-src §§URL§§/favicon.ico", "load 200 data"})
    @HtmlUnitNYI(FF = "load 200 data",
            FF_ESR = "load 200 data")
    public void connectSrcOverridesDefaultSrc() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "data.txt"), "data", MimeType.TEXT_PLAIN);
        loadPageVerify("default-src 'none'; script-src 'self'; connect-src 'self'", xhrAsync("data.txt"));
    }

    /**
     * The connect-src directive overrides default-src (blocking).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation connect-src §§URL§§/data.txt", "error 4 0"})
    public void connectSrcNoneDefaultSrcSelf() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "data.txt"), "data", MimeType.TEXT_PLAIN);
        loadPageVerify("default-src 'self'; connect-src 'none'", xhrAsync("data.txt"));
    }

    /**
     * A policy without connect-src and default-src does not restrict requests.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load 200 cross")
    public void scriptSrcOnly() throws Exception {
        setCrossOriginResponse("data.txt");
        loadPageVerify("script-src 'self'", xhrAsync(URL_CROSS_ORIGIN_BASE + "data.txt"));
    }

    /**
     * A data: url is not matched by 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation connect-src data", "error 4 0"})
    public void dataUrl() throws Exception {
        loadPageVerify("connect-src 'self'", xhrAsync("data:text/plain,data"));
    }

    /**
     * A data: url is allowed if the scheme is listed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load 200 data")
    public void dataUrl_dataScheme() throws Exception {
        loadPageVerify("connect-src 'self' data:", xhrAsync("data:text/plain,data"));
    }

    /**
     * Blocked synchronous request.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"NetworkError/DOMException", "violation connect-src §§URL§§/data.txt"})
    public void sync_blocked() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setCrossOriginResponse("data.txt");
        loadPageVerify("connect-src 'self'", xhrSync(URL_CROSS_ORIGIN_BASE + "data.txt"));
    }

    /**
     * Allowed synchronous request.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done 200 data")
    public void sync_allowed() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "data.txt"), "data", MimeType.TEXT_PLAIN);
        loadPageVerify("connect-src 'self'", xhrSync("data.txt"));
    }

    /**
     * A redirect to a blocked origin is blocked.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error 4 0", "violation connect-src §§URL§§/redirect.txt"},
            FF = {"violation connect-src §§URL§§/redirect.txt", "error 4 0"},
            FF_ESR = {"violation connect-src §§URL§§/redirect.txt", "error 4 0"})
    @HtmlUnitNYI(CHROME = {"violation connect-src §§URL§§/redirect.txt", "error 4 0"},
            EDGE = {"violation connect-src §§URL§§/redirect.txt", "error 4 0"})
    public void redirectToBlocked() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", URL_CROSS_ORIGIN_BASE + "data.txt"));
        getMockWebConnection().setResponse(new URL(URL_FIRST, "redirect.txt"), "", 302, "Found",
                MimeType.TEXT_PLAIN, headers);
        setCrossOriginResponse("data.txt");
        loadPageVerify("connect-src 'self'", xhrAsync("redirect.txt"));
    }

    /**
     * A redirect to an allowed origin.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load 200 cross")
    public void redirectToAllowed() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", URL_CROSS_ORIGIN_BASE + "data.txt"));
        getMockWebConnection().setResponse(new URL(URL_FIRST, "redirect.txt"), "", 302, "Found",
                MimeType.TEXT_PLAIN, headers);
        setCrossOriginResponse("data.txt");
        loadPageVerify("connect-src 'self' http://localhost:" + PORT2, xhrAsync("redirect.txt"));
    }

    /**
     * A Content-Security-Policy-Report-Only policy does not block, but reports.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation connect-src §§URL§§/data.txt", "load 200 cross"})
    public void reportOnly() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setCrossOriginResponse("data.txt");
        loadPageVerify(HEADER_REPORT_ONLY, "connect-src 'self'", "",
                xhrAsync(URL_CROSS_ORIGIN_BASE + "data.txt"));
    }

    /**
     * Policy defined by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation connect-src §§URL§§/data.txt", "error 4 0"})
    public void meta() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setCrossOriginResponse("data.txt");
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content=\"connect-src 'self'\">\n",
                xhrAsync(URL_CROSS_ORIGIN_BASE + "data.txt"));
    }

    /**
     * A blocked request is not sent to the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation connect-src §§URL§§/data.txt", "error 4 0"})
    public void blockedRequestNotSent() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "data.txt"), "data", MimeType.TEXT_PLAIN);
        loadPageVerify("connect-src 'none'", xhrAsync("data.txt"));
        // the page, harness.js and test.js
        assertEquals(3, getMockWebConnection().getRequestCount());
    }

    /**
     * The events fired for a blocked request.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"readystatechange 1",
             "loadstart",
             "sent",
             "violation connect-src §§URL§§/data.txt",
             "readystatechange 4",
             "error 0 0",
             "loadend"})
    public void events() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setCrossOriginResponse("data.txt");
        loadPageVerify("connect-src 'self'",
                "var xhr = new XMLHttpRequest();\n"
                + "xhr.onreadystatechange = function() { log('readystatechange ' + xhr.readyState); };\n"
                + "xhr.onloadstart = function(e) { log('loadstart'); };\n"
                + "xhr.onload = function(e) { log('load'); };\n"
                + "xhr.onerror = function(e) { log('error ' + e.loaded + ' ' + e.total); };\n"
                + "xhr.onloadend = function(e) { log('loadend'); };\n"
                + "xhr.open('GET', '" + URL_CROSS_ORIGIN_BASE + "data.txt', true);\n"
                + "xhr.send();\n"
                + "log('sent');\n");
    }

    /**
     * WebSocket connection to an origin not allowed by connect-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation connect-src §§URL§§/ws", "error 3", "close 1006 false"},
            FF = {"NS_ERROR_CONTENT_BLOCKED/Exception", "violation connect-src §§URL§§/ws"},
            FF_ESR = {"NS_ERROR_CONTENT_BLOCKED/Exception", "violation connect-src §§URL§§/ws"})
    @HtmlUnitNYI(FF = {"violation connect-src §§URL§§/ws", "error 3", "close 1006 false"},
            FF_ESR = {"violation connect-src §§URL§§/ws", "error 3", "close 1006 false"})
    public void webSocket_blocked() throws Exception {
        expandExpectedAlertsVariables("ws://localhost:" + PORT2);
        loadPageVerify("connect-src 'self'", webSocket("ws://localhost:" + PORT2 + "/ws"));
    }

    /**
     * The source 'self' also matches ws: urls of the same host and port.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error 3", "close 1006 false"})
    public void webSocket_selfMatchesWs() throws Exception {
        loadPageVerify("connect-src 'self'", webSocket("ws://localhost:" + PORT + "/ws"));
    }

    /**
     * WebSocket connection to an origin listed in connect-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error 3", "close 1006 false"})
    public void webSocket_hostSource() throws Exception {
        loadPageVerify("connect-src 'self' ws://localhost:" + PORT2, webSocket("ws://localhost:" + PORT2 + "/ws"));
    }

    /**
     * A http: host source does not match the ws: url of the same host.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation connect-src §§URL§§/ws", "error 3", "close 1006 false"},
            FF = {"NS_ERROR_CONTENT_BLOCKED/Exception", "violation connect-src §§URL§§/ws"},
            FF_ESR = {"NS_ERROR_CONTENT_BLOCKED/Exception", "violation connect-src §§URL§§/ws"})
    @HtmlUnitNYI(FF = {"violation connect-src §§URL§§/ws", "error 3", "close 1006 false"},
            FF_ESR = {"violation connect-src §§URL§§/ws", "error 3", "close 1006 false"})
    public void webSocket_httpHostSourceDoesNotMatchWs() throws Exception {
        expandExpectedAlertsVariables("ws://localhost:" + PORT2);
        loadPageVerify("connect-src 'self' http://localhost:" + PORT2,
                webSocket("ws://localhost:" + PORT2 + "/ws"));
    }

    /**
     * Request using navigator.sendBeacon(), same origin.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("sendBeacon true")
    public void sendBeacon_sameOrigin() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "beacon"), "", MimeType.TEXT_PLAIN);
        loadPageVerify("connect-src 'self'", sendBeacon("beacon"));
        // the page, harness.js, test.js and the beacon
        assertEquals(4, () -> getMockWebConnection().getRequestCount(), DEFAULT_WAIT_TIME);
    }

    /**
     * Request using navigator.sendBeacon(), blocked by connect-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"sendBeacon true", "violation connect-src §§URL§§/beacon"},
            FF = {"sendBeacon false", "violation connect-src §§URL§§/beacon"},
            FF_ESR = {"sendBeacon false", "violation connect-src §§URL§§/beacon"})
    @HtmlUnitNYI(FF = {"sendBeacon true", "violation connect-src §§URL§§/beacon"},
            FF_ESR = {"sendBeacon true", "violation connect-src §§URL§§/beacon"})
    public void sendBeacon_blocked() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setCrossOriginResponse("beacon");
        loadPageVerify("connect-src 'self'", sendBeacon(URL_CROSS_ORIGIN_BASE + "beacon"));
        // the page, harness.js and test.js
        assertEquals(3, getMockWebConnection().getRequestCount());
    }

    /**
     * Returns the code opening a WebSocket; logs the events.
     */
    private static String webSocket(final String url) {
        return "try {\n"
            + "  var ws = new WebSocket('" + url + "');\n"
            + "  ws.onopen = function() { log('open'); };\n"
            + "  ws.onerror = function() { log('error ' + ws.readyState); };\n"
            + "  ws.onclose = function(e) { log('close ' + e.code + ' ' + e.wasClean); };\n"
            + "} catch(e) { logEx(e); }\n";
    }

    /**
     * Returns the code sending a beacon; logs the result.
     */
    private static String sendBeacon(final String url) {
        return "try {\n"
            + "  log('sendBeacon ' + navigator.sendBeacon('" + url + "', 'data'));\n"
            + "} catch(e) { logEx(e); }\n";
    }

    /**
     * Returns the code for an async request; logs the load or error event.
     */
    private static String xhrAsync(final String url) {
        return "(function() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.onload = function() { log('load ' + xhr.status + ' ' + xhr.responseText); };\n"
            + "  xhr.onerror = function() { log('error ' + xhr.readyState + ' ' + xhr.status); };\n"
            + "  try {\n"
            + "    xhr.open('GET', '" + url + "', true);\n"
            + "    xhr.send();\n"
            + "  } catch(e) { logEx(e); }\n"
            + "})();\n";
    }

    /**
     * Returns the code for a sync request; logs the result or the exception.
     */
    private static String xhrSync(final String url) {
        return "(function() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  try {\n"
            + "    xhr.open('GET', '" + url + "', false);\n"
            + "    xhr.send();\n"
            + "    log('done ' + xhr.status + ' ' + xhr.responseText);\n"
            + "  } catch(e) { logEx(e); }\n"
            + "})();\n";
    }

    /**
     * Cross origin response (for {@link #URL_CROSS_ORIGIN_BASE}) allowing all origins.
     */
    private void setCrossOriginResponse(final String path) throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN, "*"));
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, path), "cross", 200, "OK",
                MimeType.TEXT_PLAIN, headers);
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

        final WebDriver driver = loadPage2(URL_FIRST, ISO_8859_1);
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }
}
