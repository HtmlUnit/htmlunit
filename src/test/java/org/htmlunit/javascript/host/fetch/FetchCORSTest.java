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

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Tests for Cross-Origin Resource Sharing for {@code fetch()}.
 *
 * <p>The scenarios are the same as in {@code XMLHttpRequestCORSTest}, but the tests (and the servlets)
 * are independent, to be able to follow the browsers whatever they do.</p>
 *
 * @author Ronald Brill
 */
public class FetchCORSTest extends WebDriverTestCase {

    /**
     * The second server replaces the mock-connection server on PORT2. The base class only starts the mock
     * servers if PORT2 is free, so we have to stop them; otherwise later tests
     * would talk to our servlets on PORT2 instead of the mock connection.
     * @throws Exception if stopping fails
     */
    @AfterEach
    public void stopSecondServer() throws Exception {
        stopWebServers();
    }

    private static void startSecondServer() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/echo", EchoServlet.class);
        servlets2.put("/redirect", RedirectServlet.class);
        servlets2.put("/expose", ExposeServlet.class);
        startWebServer2(".", servlets2);
    }

    // origin A: the page (localhost:PORT), origin B: localhost:PORT2, origin C: 127.0.0.1:PORT2
    // (the host differs, so C is a third origin that is served by the second server)

    private static String originA() {
        return "http://localhost:" + PORT;
    }

    private static String originB() {
        return "http://localhost:" + PORT2;
    }

    private static String originC() {
        return "http://127.0.0.1:" + PORT2;
    }

    private static String enc(final String value) {
        return URLEncoder.encode(value, UTF_8);
    }

    private void pivot(final String path, final int status, final String location) throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", location));
        getMockWebConnection().setResponse(new URL(URL_FIRST, path), "", status, "Redirect", "text/html", headers);
    }

    private void loadAndVerify(final String html) throws Exception {
        loadAndVerify(html, null);
    }

    private void loadAndVerify(final String html, final String cookie) throws Exception {
        final URL pageUrl = new URL(URL_FIRST, "/page");

        List<NameValuePair> responseHeader = null;
        if (cookie != null) {
            responseHeader = new ArrayList<>();
            responseHeader.add(new NameValuePair("Set-Cookie", cookie));
        }
        getMockWebConnection().setResponse(pageUrl, html, 200, "OK", "text/html;charset=ISO-8859-1",
                ISO_8859_1, responseHeader);

        loadPage2(pageUrl, null);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Fetches the url; logs status, type and the text of the response or the error.
     */
    private static String fetchPage(final String before, final String init, final String url) {
        return DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + before
                + "  fetch('" + url + "'" + (init == null ? "" : ", " + init) + ")\n"
                + "    .then(function(r) { log(r.status); log(r.type); return r.text(); })\n"
                + "    .then(function(t) { log(t); })\n"
                + "    .catch(function(e) { logEx(e); });\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";
    }

    /**
     * Fetches the urls one after the other; logs the status or the error.
     */
    private static String multiFetchPage(final String... urls) {
        final StringBuilder list = new StringBuilder();
        for (final String url : urls) {
            if (list.length() > 0) {
                list.append(", ");
            }
            list.append('\'').append(url).append('\'');
        }

        return DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var urls = [" + list + "];\n"
                + "  urls.reduce(function(p, url) {\n"
                + "    return p.then(function() {\n"
                + "      return fetch(url).then(function(r) { log(r.status); }, function(e) { logEx(e); });\n"
                + "    });\n"
                + "  }, Promise.resolve());\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";
    }

    /**
     * Fetches the url; logs the names of the visible headers and the values of some headers.
     */
    private static String headersPage(final String init, final String url) {
        return DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  fetch('" + url + "'" + (init == null ? "" : ", " + init) + ")\n"
                + "    .then(function(r) {\n"
                + "      log(Array.from(r.headers.keys()).join());\n"
                + "      log(r.headers.get('X-Custom'));\n"
                + "      log(r.headers.get('x-other'));\n"
                + "      log(r.headers.get('Pragma'));\n"
                + "      log(r.headers.get('Content-Language'));\n"
                + "      log(r.headers.get('Content-Length'));\n"
                + "      log(r.headers.get('Access-Control-Expose-Headers'));\n"
                + "    })\n"
                + "    .catch(function(e) { logEx(e); });\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";
    }

    // ---------------------------------------------------------------------------
    // simple requests
    // ---------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void noCorsHeader() throws Exception {
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadAndVerify(fetchPage("", null, URL_THIRD.toExternalForm()));
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "<root><origin>§§URL§§</origin><cookie>null</cookie></root>"})
    public void simple() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "*";
        startSimpleServer();

        loadAndVerify(fetchPage("", null, originB() + "/simple2"), "cookie=sweet");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             ""})
    public void simpleHead() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "*";
        startSimpleServer();

        loadAndVerify(fetchPage("", "{method: 'HEAD'}", originB() + "/simple2"));
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "<root><origin>§§URL§§</origin><cookie>null</cookie></root>"})
    public void simplePost() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "*";
        startSimpleServer();

        loadAndVerify(fetchPage("", "{method: 'POST', body: 'data'}", originB() + "/simple2"));
    }

    /**
     * PUT requires a preflight; the servlet does not answer the preflight with CORS headers.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void simplePut() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "*";
        startSimpleServer();

        loadAndVerify(fetchPage("", "{method: 'PUT', body: ''}", originB() + "/simple2"));
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void noAccessControlAllowOrigin() throws Exception {
        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = null;
        startSimpleServer();

        loadAndVerify(fetchPage("", null, originB() + "/simple2"));
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void nonMatchingAccessControlAllowOrigin() throws Exception {
        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "http://www.sourceforge.net";
        startSimpleServer();

        loadAndVerify(fetchPage("", null, originB() + "/simple2"));
    }

    /**
     * The response of a cross origin request has the type 'cors' and the url of the response.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200 OK true cors false",
             "§§URL§§/simple2?x=1"})
    public void responseProperties() throws Exception {
        expandExpectedAlertsVariables(new URL(originB()));
        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "*";
        startSimpleServer();

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  fetch('" + originB() + "/simple2?x=1#hash')\n"
                + "    .then(function(r) {\n"
                + "      log(r.status + ' ' + r.statusText + ' ' + r.ok + ' ' + r.type + ' ' + r.redirected);\n"
                + "      log(r.url);\n"
                + "    })\n"
                + "    .catch(function(e) { logEx(e); });\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadAndVerify(html);
    }

    private static void startSimpleServer() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/simple2", SimpleServerServlet.class);
        startWebServer2(".", servlets2);
    }

    /**
     * Simple CORS scenario Servlet.
     */
    public static class SimpleServerServlet extends HttpServlet {
        private static String ACCESS_CONTROL_ALLOW_ORIGIN_;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            if (ACCESS_CONTROL_ALLOW_ORIGIN_ != null) {
                response.setHeader("Access-Control-Allow-Origin", ACCESS_CONTROL_ALLOW_ORIGIN_);
            }
            response.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_XML);

            String origin = request.getHeader(HttpHeader.ORIGIN);
            if (origin == null) {
                origin = "No Origin!";
            }
            final String cookie = request.getHeader(HttpHeader.COOKIE);
            response.getWriter().write("<root><origin>" + origin + "</origin><cookie>" + cookie + "</cookie></root>");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            doGet(request, response);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPut(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            doGet(request, response);
        }
    }

    // ---------------------------------------------------------------------------
    // preflight
    // ---------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=§§URL§§;options_method=GET;options_headers=x-pingother;"
                 + "options_cookie=null"})
    public void preflight() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", MimeType.TEXT_PLAIN, null);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=§§URL§§;options_method=GET;options_headers=x-pingother;"
                 + "options_cookie=null"})
    public void preflight_cookie() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", MimeType.TEXT_PLAIN, "cookie=sweet");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=§§URL§§;options_method=GET;options_headers=x-pingother;"
                 + "options_cookie=null"})
    public void preflight_contentTypeWithCharset() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", MimeType.TEXT_PLAIN + ";charset=utf-8", null);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=§§URL§§;options_method=GET;options_headers=x-pingother;"
                 + "options_cookie=null"})
    public void preflightUrlEncoded() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", "application/x-www-form-urlencoded", null);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=§§URL§§;options_method=GET;options_headers=x-pingother;"
                 + "options_cookie=null"})
    public void preflightUrlEncoded_contentTypeWithCharset() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", "application/x-www-form-urlencoded;charset=utf-8", null);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=§§URL§§;options_method=GET;options_headers=x-pingother;"
                 + "options_cookie=null"})
    public void preflightMultipart() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", "multipart/form-data", null);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=§§URL§§;options_method=GET;options_headers=x-pingother;"
                 + "options_cookie=null"})
    public void preflightMultipart_contentTypeWithCharset() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", "multipart/form-data;charset=utf-8", null);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_contentTypeJson() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", MimeType.APPLICATION_JSON, null);
    }

    /**
     * Without Access-Control-Allow-Methods (GET is a CORS-safelisted method).
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=§§URL§§;options_method=GET;options_headers=x-pingother;"
                 + "options_cookie=null"})
    public void preflight_incorrect_methods() throws Exception {
        doPreflightTestAllowedMethods(null, MimeType.TEXT_PLAIN, null);
    }

    private void doPreflightTestAllowedMethods(final String allowedMethods,
                    final String contentType, final String cookie) throws Exception {
        expandExpectedAlertsVariables(new URL(originA())); // url without trailing "/"

        // a unique url for every test; browsers cache the preflight result per url
        final String url = originB() + "/preflight2?id=" + System.currentTimeMillis();

        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = originA();
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = allowedMethods;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = "X-PINGOTHER";
        startPreflightServer();

        loadAndVerify(fetchPage("",
                "{headers: {'X-PINGOTHER': 'pingpong', 'Content-Type': '" + contentType + "'}}", url), cookie);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_incorrect_headers() throws Exception {
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = originA();
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = null;
        startPreflightServer();

        loadAndVerify(fetchPage("", "{headers: {'X-PINGOTHER': 'pingpong'}}",
                originB() + "/preflight2?id=" + System.currentTimeMillis()));
    }

    /**
     * A safelisted Accept-Language value needs no preflight.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=null;options_method=null;options_headers=null;options_cookie=null"})
    public void preflight_acceptLanguageSafelisted() throws Exception {
        doPreflightTestHeader("Accept-Language", "de-DE,de;q=0.9");
    }

    /**
     * A safelisted Content-Language value needs no preflight.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=null;options_method=null;options_headers=null;options_cookie=null"})
    public void preflight_contentLanguageSafelisted() throws Exception {
        doPreflightTestHeader("Content-Language", "de-DE");
    }

    /**
     * A safelisted Accept value needs no preflight.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=null;options_method=null;options_headers=null;options_cookie=null"})
    public void preflight_acceptSafelisted() throws Exception {
        doPreflightTestHeader("Accept", "application/json, text/plain, */*");
    }

    /**
     * Accept values with CORS-unsafe bytes need a preflight.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_acceptNotSafelisted() throws Exception {
        doPreflightTestHeader("Accept", "text/html(1)");
    }

    /**
     * A Content-Type value with 128 characters is still safelisted.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=null;options_method=null;options_headers=null;options_cookie=null"})
    public void preflight_contentTypeLength128() throws Exception {
        doPreflightTestHeader("Content-Type", "text/plain;x=" + "a".repeat(115));
    }

    /**
     * A simple range is safelisted.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=null;options_method=null;options_headers=null;options_cookie=null"})
    public void preflight_rangeSimple() throws Exception {
        doPreflightTestHeader("Range", "bytes=0-499");
    }

    /**
     * A simple range without end is safelisted.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=null;options_method=null;options_headers=null;options_cookie=null"})
    public void preflight_rangeOpenEnd() throws Exception {
        doPreflightTestHeader("Range", "bytes=500-");
    }

    /**
     * A suffix range is not safelisted.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_rangeSuffix() throws Exception {
        doPreflightTestHeader("Range", "bytes=-500");
    }

    /**
     * More than one range is not safelisted.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_rangeMultiple() throws Exception {
        doPreflightTestHeader("Range", "bytes=0-1,5-9");
    }

    /**
     * A range with whitespace is not safelisted.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_rangeWhitespace() throws Exception {
        doPreflightTestHeader("Range", "bytes = 0-499");
    }

    /**
     * A range with the start after the end is not safelisted.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_rangeStartAfterEnd() throws Exception {
        doPreflightTestHeader("Range", "bytes=500-100");
    }

    /**
     * The unit of a simple range is case-sensitive.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_rangeUpperCase() throws Exception {
        doPreflightTestHeader("Range", "BYTES=0-499");
    }

    /**
     * The essence of the content type has to be one of the three safelisted ones.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_contentTypeNotSafelisted() throws Exception {
        doPreflightTestHeader("Content-Type", "application/x-www-form-urlencoded+json");
    }

    /**
     * Accept-Language values with other characters are not safelisted.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_acceptLanguageNotSafelisted() throws Exception {
        doPreflightTestHeader("Accept-Language", "en_US");
    }

    /**
     * Values longer than 128 characters are not safelisted.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_contentLanguageTooLong() throws Exception {
        doPreflightTestHeader("Content-Language", "de".repeat(65));
    }

    /**
     * Two Accept headers (append); each value is safelisted, the combined value is longer
     * than 128 characters.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_acceptAppendedTwice() throws Exception {
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = originA();
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = null;
        startPreflightServer();

        final String value = "text/plain;x=" + "a".repeat(80);
        loadAndVerify(fetchPage("  var headers = new Headers();\n"
                + "  headers.append('Accept', '" + value + "');\n"
                + "  headers.append('Accept', '" + value + "');\n",
                "{headers: headers}",
                originB() + "/preflight2?id=" + System.currentTimeMillis()));
    }

    /**
     * Fetches the url with the given header; the preflight response does not allow any header.
     * Logs the response (no preflight) or the error (preflight).
     */
    private void doPreflightTestHeader(final String name, final String value) throws Exception {
        expandExpectedAlertsVariables(new URL(originA())); // url without trailing "/"

        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = originA();
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = null;
        startPreflightServer();

        loadAndVerify(fetchPage("", "{headers: {'" + name + "': '" + value + "'}}",
                originB() + "/preflight2?id=" + System.currentTimeMillis()));
    }

    /**
     * The preflight response has no Access-Control-Allow-Origin header.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void preflight_missing_allow_origin() throws Exception {
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = null;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = "X-PINGOTHER";
        startPreflightServer();

        loadAndVerify(fetchPage("", "{headers: {'X-PINGOTHER': 'pingpong'}}",
                originB() + "/preflight2?id=" + System.currentTimeMillis()));
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=§§URL§§;options_method=GET;options_headers=x-ping,x-pong;"
                 + "options_cookie=null"})
    public void preflight_many_header_values() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = originA();
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = "X-PING, X-PONG";
        startPreflightServer();

        loadAndVerify(fetchPage("", "{headers: {'X-PING': 'ping', 'X-PONG': 'pong'}}",
                originB() + "/preflight2?id=" + System.currentTimeMillis()));
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;options_origin=§§URL§§;options_method=GET;options_headers=x-ping,x-pong;"
                 + "options_cookie=null"})
    public void preflight_wildcard_allow_headers() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = originA();
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = "*";
        startPreflightServer();

        loadAndVerify(fetchPage("", "{headers: {'X-PING': 'ping', 'X-PONG': 'pong'}}",
                originB() + "/preflight2?id=" + System.currentTimeMillis()));
    }

    private static void startPreflightServer() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/preflight2", PreflightServerServlet.class);
        startWebServer2(".", servlets2);
    }

    /**
     * Preflight CORS scenario Servlet.
     */
    public static class PreflightServerServlet extends HttpServlet {
        private static String ACCESS_CONTROL_ALLOW_ORIGIN_;
        private static String ACCESS_CONTROL_ALLOW_METHODS_;
        private static String ACCESS_CONTROL_ALLOW_HEADERS_;
        private String options_origin_;
        private String options_method_;
        private String options_headers_;
        private String options_cookie_;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doOptions(final HttpServletRequest request, final HttpServletResponse response) {
            if (ACCESS_CONTROL_ALLOW_ORIGIN_ != null) {
                response.setHeader("Access-Control-Allow-Origin", ACCESS_CONTROL_ALLOW_ORIGIN_);
            }
            if (ACCESS_CONTROL_ALLOW_METHODS_ != null) {
                response.setHeader("Access-Control-Allow-Methods", ACCESS_CONTROL_ALLOW_METHODS_);
            }
            if (ACCESS_CONTROL_ALLOW_HEADERS_ != null) {
                response.setHeader("Access-Control-Allow-Headers", ACCESS_CONTROL_ALLOW_HEADERS_);
            }
            options_origin_ = request.getHeader(HttpHeader.ORIGIN);
            options_method_ = request.getHeader("Access-Control-Request-Method");
            options_headers_ = request.getHeader("Access-Control-Request-Headers");
            options_cookie_ = request.getHeader(HttpHeader.COOKIE);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            if (ACCESS_CONTROL_ALLOW_ORIGIN_ != null) {
                response.setHeader("Access-Control-Allow-Origin", ACCESS_CONTROL_ALLOW_ORIGIN_);
            }
            response.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_PLAIN);
            final Writer writer = response.getWriter();

            final String origin = request.getHeader(HttpHeader.ORIGIN);
            writer.write("origin=" + origin
                + ";options_origin=" + options_origin_
                + ";options_method=" + options_method_
                + ";options_headers=" + options_headers_
                + ";options_cookie=" + options_cookie_);
        }
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "PUT null"})
    public void preflight_put() throws Exception {
        doPreflightTestReceived("same-origin", "PUT", originA(), null, "PUT", "X-PING");
    }

    /**
     * A method that is not CORS-safelisted has to be listed in Access-Control-Allow-Methods.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"TypeError",
             "nothing"})
    public void preflight_put_methodNotAllowed() throws Exception {
        doPreflightTestReceived("same-origin", "PUT", originA(), null, "POST, GET, OPTIONS", "X-PING");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "PUT null"})
    public void preflight_put_wildcardMethods() throws Exception {
        doPreflightTestReceived("same-origin", "PUT", originA(), null, "*", "X-PING");
    }

    /**
     * The method names are case-sensitive.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"TypeError",
             "nothing"})
    public void preflight_patch_lowerCaseMethod() throws Exception {
        doPreflightTestReceived("same-origin", "PATCH", originA(), null, "patch", "X-PING");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "PUT cookie=sweet"})
    public void preflight_put_include() throws Exception {
        doPreflightTestReceived("include", "PUT", originA(), "true", "PUT", "X-PING");
    }

    /**
     * The wildcard origin is not allowed for requests with credentials, the request must not be sent.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"TypeError",
             "nothing"})
    public void preflight_put_include_wildcardOrigin() throws Exception {
        doPreflightTestReceived("include", "PUT", "*", "true", "PUT", "X-PING");
    }

    /**
     * A request with credentials needs Access-Control-Allow-Credentials in the preflight response.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"TypeError",
             "nothing"})
    public void preflight_put_include_noAllowCredentials() throws Exception {
        doPreflightTestReceived("include", "PUT", originA(), null, "PUT", "X-PING");
    }

    /**
     * The wildcard methods are not allowed for requests with credentials.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"TypeError",
             "nothing"})
    public void preflight_put_include_wildcardMethods() throws Exception {
        doPreflightTestReceived("include", "PUT", originA(), "true", "*", "X-PING");
    }

    /**
     * The wildcard headers are not allowed for requests with credentials.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"TypeError",
             "nothing"})
    public void preflight_include_wildcardHeaders() throws Exception {
        doPreflightTestReceived("include", "GET", originA(), "true", null, "*");
    }

    /**
     * The credentials mode 'omit' does not need Access-Control-Allow-Credentials and allows the wildcards.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "PUT null"})
    public void preflight_put_omit_wildcards() throws Exception {
        doPreflightTestReceived("omit", "PUT", "*", null, "*", "*");
    }

    /**
     * Sends a request that needs a preflight and logs afterwards the request that
     * reached the server ('nothing' if the request was not sent).
     */
    private void doPreflightTestReceived(final String credentials, final String method,
            final String allowOrigin, final String allowCredentials,
            final String allowMethods, final String allowHeaders) throws Exception {
        final String url = originB() + "/preflight2";
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                // a unique url for every test; browsers cache the preflight result per url
                + "  fetch('" + url + "?id=" + System.currentTimeMillis() + "', {method: '" + method + "', "
                + "credentials: '" + credentials + "', headers: {'X-PING': 'ping'}})\n"
                + "    .then(function(r) { log(r.status); }, function(e) { logEx(e); })\n"
                + "    .then(function() { return fetch('" + url + "?received'); })\n"
                + "    .then(function(r) { return r.text(); })\n"
                + "    .then(function(t) { log(t); })\n"
                + "    .catch(function(e) { logEx(e); });\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        PreflightReceivedServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = allowOrigin;
        PreflightReceivedServlet.ACCESS_CONTROL_ALLOW_CREDENTIALS_ = allowCredentials;
        PreflightReceivedServlet.ACCESS_CONTROL_ALLOW_METHODS_ = allowMethods;
        PreflightReceivedServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = allowHeaders;
        PreflightReceivedServlet.RECEIVED_ = null;

        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/preflight2", PreflightReceivedServlet.class);
        startWebServer2(".", servlets2);

        loadAndVerify(html, "cookie=sweet");
    }

    /**
     * Preflight CORS scenario Servlet; remembers the method and the cookie of the request
     * that was sent after the preflight and answers the 'received' GET request with them.
     */
    public static class PreflightReceivedServlet extends HttpServlet {
        private static String ACCESS_CONTROL_ALLOW_ORIGIN_;
        private static String ACCESS_CONTROL_ALLOW_CREDENTIALS_;
        private static String ACCESS_CONTROL_ALLOW_METHODS_;
        private static String ACCESS_CONTROL_ALLOW_HEADERS_;
        private static String RECEIVED_;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doOptions(final HttpServletRequest request, final HttpServletResponse response) {
            setCorsHeaders(response);
            if (ACCESS_CONTROL_ALLOW_METHODS_ != null) {
                response.setHeader("Access-Control-Allow-Methods", ACCESS_CONTROL_ALLOW_METHODS_);
            }
            if (ACCESS_CONTROL_ALLOW_HEADERS_ != null) {
                response.setHeader("Access-Control-Allow-Headers", ACCESS_CONTROL_ALLOW_HEADERS_);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            if ("received".equals(request.getQueryString())) {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setContentType(MimeType.TEXT_PLAIN);
                response.getWriter().write(RECEIVED_ == null ? "nothing" : RECEIVED_);
                return;
            }
            received(request, response);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPut(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            received(request, response);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void service(final HttpServletRequest request, final HttpServletResponse response)
                throws ServletException, IOException {
            if ("PATCH".equals(request.getMethod())) {
                received(request, response);
                return;
            }
            super.service(request, response);
        }

        private static void received(final HttpServletRequest request, final HttpServletResponse response)
                throws IOException {
            RECEIVED_ = request.getMethod() + " " + request.getHeader(HttpHeader.COOKIE);
            setCorsHeaders(response);
            response.setContentType(MimeType.TEXT_PLAIN);
            response.getWriter().write("ok");
        }

        private static void setCorsHeaders(final HttpServletResponse response) {
            if (ACCESS_CONTROL_ALLOW_ORIGIN_ != null) {
                response.setHeader("Access-Control-Allow-Origin", ACCESS_CONTROL_ALLOW_ORIGIN_);
            }
            if (ACCESS_CONTROL_ALLOW_CREDENTIALS_ != null) {
                response.setHeader("Access-Control-Allow-Credentials", ACCESS_CONTROL_ALLOW_CREDENTIALS_);
            }
        }
    }

    // ---------------------------------------------------------------------------
    // credentials
    // ---------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void include_wildcardOrigin() throws Exception {
        testCredentials("include", "*", "true");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;cookie=cookie=sweet"})
    public void include_origin() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        testCredentials("include", originA(), "true");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void include_originSlashAtEnd() throws Exception {
        testCredentials("include", URL_FIRST.toExternalForm(), "true");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void include_noAllowCredentials_wildcardOrigin() throws Exception {
        testCredentials("include", "*", null);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void include_noAllowCredentials_origin() throws Exception {
        testCredentials("include", originA(), null);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void include_noAllowCredentials_originSlashAtEnd() throws Exception {
        testCredentials("include", URL_FIRST.toExternalForm(), null);
    }

    /**
     * The default credentials mode 'same-origin' does not send cookies to other origins.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;cookie=null"})
    public void sameOrigin_wildcardOrigin() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        testCredentials("same-origin", "*", null);
    }

    /**
     * Access-Control-Allow-Credentials does not matter for 'same-origin'.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;cookie=null"})
    public void sameOrigin_origin_allowCredentials() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        testCredentials("same-origin", originA(), "true");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;cookie=null"})
    public void omit_wildcardOrigin() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        testCredentials("omit", "*", null);
    }

    /**
     * Access-Control-Allow-Credentials does not matter for 'omit'.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;cookie=null"})
    public void omit_origin_allowCredentials() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        testCredentials("omit", originA(), "true");
    }

    private void testCredentials(final String credentials, final String accessControlAllowOrigin,
            final String accessControlAllowCredentials) throws Exception {
        WithCredentialsServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = accessControlAllowOrigin;
        WithCredentialsServerServlet.ACCESS_CONTROL_ALLOW_CREDENTIALS_ = accessControlAllowCredentials;
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/withCredentials2", WithCredentialsServerServlet.class);
        startWebServer2(".", servlets2);

        loadAndVerify(fetchPage("", "{credentials: '" + credentials + "'}", originB() + "/withCredentials2"),
                "cookie=sweet");
    }

    /**
     * The credentials mode 'omit' does not send the cookies to the origin of the page.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "basic"})
    public void omit_sameOriginRequest() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "/data"), "data", MimeType.TEXT_PLAIN);

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  document.cookie = 'c=1; path=/';\n"
                + "  fetch('/data', {credentials: 'omit'})\n"
                + "    .then(function(r) { log(r.status); log(r.type); })\n"
                + "    .catch(function(e) { logEx(e); });\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadAndVerify(html);
        assertNull(getMockWebConnection().getLastWebRequest().getAdditionalHeader(HttpHeader.COOKIE));
    }

    /**
     * CORS "With Credentials" scenario Servlet.
     */
    public static class WithCredentialsServerServlet extends HttpServlet {
        private static String ACCESS_CONTROL_ALLOW_ORIGIN_;
        private static String ACCESS_CONTROL_ALLOW_CREDENTIALS_;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            if (ACCESS_CONTROL_ALLOW_ORIGIN_ != null) {
                response.setHeader("Access-Control-Allow-Origin", ACCESS_CONTROL_ALLOW_ORIGIN_);
            }
            if (ACCESS_CONTROL_ALLOW_CREDENTIALS_ != null) {
                response.setHeader("Access-Control-Allow-Credentials", ACCESS_CONTROL_ALLOW_CREDENTIALS_);
            }
            response.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_PLAIN);

            String origin = request.getHeader(HttpHeader.ORIGIN);
            if (origin == null) {
                origin = "No Origin!";
            }
            final String cookie = request.getHeader(HttpHeader.COOKIE);
            response.getWriter().write("origin=" + origin + ";cookie=" + cookie);
        }
    }

    // ---------------------------------------------------------------------------
    // the request modes
    // ---------------------------------------------------------------------------

    /**
     * The mode 'same-origin' does not allow requests to other origins.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void modeSameOrigin() throws Exception {
        startSecondServer();
        loadAndVerify(fetchPage("", "{mode: 'same-origin'}", originB() + "/echo?acao=*"));
    }

    /**
     * The mode 'same-origin' does not allow redirects to other origins.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void modeSameOriginRedirect() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*");

        loadAndVerify(fetchPage("", "{mode: 'same-origin'}", originA() + "/pivot"));
    }

    /**
     * The mode 'no-cors' returns an opaque response.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"opaque 0 [] false",
             "[]",
             "",
             "[]"})
    public void modeNoCors() throws Exception {
        startSecondServer();

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  fetch('" + originB() + "/echo', {mode: 'no-cors'})\n"
                + "    .then(function(r) {\n"
                + "      log(r.type + ' ' + r.status + ' [' + r.statusText + '] ' + r.ok);\n"
                + "      log('[' + r.url + ']');\n"
                + "      log(Array.from(r.headers.keys()).join());\n"
                + "      return r.text();\n"
                + "    })\n"
                + "    .then(function(t) { log('[' + t + ']'); })\n"
                + "    .catch(function(e) { logEx(e); });\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadAndVerify(html);
    }

    /**
     * A GET in mode 'no-cors' has no Origin header; the non-safelisted headers are not sent,
     * cookies only with 'include'.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"opaque 0",
             "GET origin=null;cookie=null;x-custom=null;content-type=null;accept=text/plain;content-language=de"})
    public void modeNoCorsGet() throws Exception {
        noCors("{mode: 'no-cors', headers: {'X-Custom': 'v', 'Content-Type': 'application/json', "
                + "'Accept': 'text/plain', 'Content-Language': 'de'}}");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"opaque 0",
             "POST origin=§§URL§§;cookie=null;x-custom=null;content-type=text/plain;accept=*/*;content-language=null"})
    public void modeNoCorsPost() throws Exception {
        noCors("{mode: 'no-cors', method: 'POST', body: 'data', headers: {'Content-Type': 'text/plain'}}");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"opaque 0",
             "GET origin=null;cookie=cookie=sweet;x-custom=null;content-type=null;accept=*/*;content-language=null"})
    public void modeNoCorsInclude() throws Exception {
        noCors("{mode: 'no-cors', credentials: 'include'}");
    }

    /**
     * A cross origin request in mode 'no-cors' has to follow the redirects.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"TypeError",
             "nothing"})
    public void modeNoCorsRedirectManual() throws Exception {
        noCors("{mode: 'no-cors', redirect: 'manual'}");
    }

    /**
     * The integrity of an opaque response can't be checked.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"TypeError",
             "GET origin=null;cookie=null;x-custom=null;content-type=null;accept=*/*;content-language=null"})
    public void modeNoCorsIntegrity() throws Exception {
        noCors("{mode: 'no-cors', integrity: 'sha256-ungWv48Bz+pBQUDeXa4iI7ADYaOWF3qctBD/YfIAFa0='}");
    }

    /**
     * A redirect to another origin in mode 'no-cors' results in an opaque response.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"0",
             "opaque",
             ""})
    public void modeNoCorsRedirectToOtherOrigin() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo");

        loadAndVerify(fetchPage("", "{mode: 'no-cors'}", originA() + "/pivot"));
    }

    /**
     * Sends the request in mode 'no-cors' and logs afterwards what the server has received.
     */
    private void noCors(final String init) throws Exception {
        final String url = originB() + "/nocors";
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  fetch('" + url + "', " + init + ")\n"
                + "    .then(function(r) { log(r.type + ' ' + r.status); }, function(e) { logEx(e); })\n"
                + "    .then(function() { return fetch('" + url + "?received'); })\n"
                + "    .then(function(r) { return r.text(); })\n"
                + "    .then(function(t) { log(t); })\n"
                + "    .catch(function(e) { logEx(e); });\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        NoCorsServlet.RECEIVED_ = null;
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/nocors", NoCorsServlet.class);
        startWebServer2(".", servlets2);

        expandExpectedAlertsVariables(new URL(originA()));
        loadAndVerify(html, "cookie=sweet");
    }

    /**
     * Remembers the request (without sending CORS headers) and answers the 'received' GET request
     * with it.
     */
    public static class NoCorsServlet extends HttpServlet {
        private static String RECEIVED_;

        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
                throws IOException {
            if ("received".equals(request.getQueryString())) {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setContentType(MimeType.TEXT_PLAIN);
                response.getWriter().write(RECEIVED_ == null ? "nothing" : RECEIVED_);
                return;
            }
            received(request, response);
        }

        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
                throws IOException {
            received(request, response);
        }

        private static void received(final HttpServletRequest request, final HttpServletResponse response)
                throws IOException {
            RECEIVED_ = request.getMethod()
                    + " origin=" + request.getHeader(HttpHeader.ORIGIN)
                    + ";cookie=" + request.getHeader(HttpHeader.COOKIE)
                    + ";x-custom=" + request.getHeader("X-Custom")
                    + ";content-type=" + request.getHeader(HttpHeader.CONTENT_TYPE)
                    + ";accept=" + request.getHeader(HttpHeader.ACCEPT)
                    + ";content-language=" + request.getHeader("Content-Language");
            response.setContentType(MimeType.TEXT_PLAIN);
            response.getWriter().write("abc");
        }
    }

    // ---------------------------------------------------------------------------
    // redirects; the page origin is not left, or the target allows the access
    // ---------------------------------------------------------------------------

    /**
     * A redirect inside of the origin is always fine.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "basic",
             "FINAL"})
    public void sameOriginRedirect() throws Exception {
        pivot("/pivot", 302, new URL(URL_FIRST, "/final").toExternalForm());
        getMockWebConnection().setResponse(new URL(URL_FIRST, "/final"), "FINAL");

        loadAndVerify(fetchPage("", null, originA() + "/pivot"));
    }

    /**
     * The request starts inside of the origin, only the final response needs the CORS header.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;cookie=null;auth=null"})
    public void crossOriginRedirectWithCorsWildcard() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*");

        loadAndVerify(fetchPage("", null, originA() + "/pivot"));
    }

    /**
     * The final response is also readable if it allows exactly the origin of the page.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;cookie=null;auth=null"})
    public void crossOriginRedirectWithCorsOrigin() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=" + enc(originA()));

        loadAndVerify(fetchPage("", null, originA() + "/pivot"));
    }

    /**
     * The url, the type and the redirected flag of the response after a redirect that was allowed.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200 cors true",
             "§§URL§§/echo?acao=*"})
    public void crossOriginRedirectResponseProperties() throws Exception {
        expandExpectedAlertsVariables(new URL(originB()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*");

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  fetch('" + originA() + "/pivot')\n"
                + "    .then(function(r) {\n"
                + "      log(r.status + ' ' + r.type + ' ' + r.redirected);\n"
                + "      log(r.url);\n"
                + "    })\n"
                + "    .catch(function(e) { logEx(e); });\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadAndVerify(html);
    }

    /**
     * All redirect status codes are followed.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "200",
             "200",
             "200",
             "200"})
    public void crossOriginRedirectStatusCodesWithCors() throws Exception {
        startSecondServer();
        final int[] codes = {301, 302, 303, 307, 308};
        final String[] urls = new String[codes.length];
        for (int i = 0; i < codes.length; i++) {
            pivot("/pivot" + codes[i], codes[i], originB() + "/echo?acao=*");
            urls[i] = originA() + "/pivot" + codes[i];
        }

        loadAndVerify(multiFetchPage(urls));
    }

    /**
     * The redirect mode 'manual' returns an opaque redirect also for a cross origin location.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"opaqueredirect 0 false",
             "§§URL§§/pivot"})
    public void crossOriginRedirectManual() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*");

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  fetch('" + originA() + "/pivot', {redirect: 'manual'})\n"
                + "    .then(function(r) {\n"
                + "      log(r.type + ' ' + r.status + ' ' + r.redirected);\n"
                + "      log(r.url);\n"
                + "    })\n"
                + "    .catch(function(e) { logEx(e); });\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadAndVerify(html);
    }

    /**
     * The redirect mode 'error' does not follow the redirect.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void crossOriginRedirectError() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*");

        loadAndVerify(fetchPage("", "{redirect: 'error'}", originA() + "/pivot"));
    }

    // ---------------------------------------------------------------------------
    // the response has to be blocked
    // ---------------------------------------------------------------------------

    /**
     * Redirect to another origin without CORS headers.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void crossOriginRedirectWithoutCors() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/secret", SecretServerServlet.class);
        startWebServer2(".", servlets2);

        pivot("/pivot", 302, originB() + "/secret");

        loadAndVerify(fetchPage("", null, originA() + "/pivot"));
    }

    /**
     * Secret resource servlet without CORS headers (running on PORT2).
     */
    public static class SecretServerServlet extends HttpServlet {
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_HTML);
            response.getWriter().write("INTERNAL_SECRET");
        }
    }

    /**
     * An Access-Control-Allow-Origin header for another origin does not help.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void crossOriginRedirectWithWrongOrigin() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=" + enc("http://other.example.com"));

        loadAndVerify(fetchPage("", null, originA() + "/pivot"));
    }

    /**
     * All redirect status codes are blocked.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = {"200",
                       "TypeError",
                       "TypeError",
                       "TypeError",
                       "200"},
            FF = {"TypeError",
                  "TypeError",
                  "TypeError",
                  "TypeError",
                  "TypeError"},
            FF_ESR = {"TypeError",
                      "TypeError",
                      "TypeError",
                      "TypeError",
                      "TypeError"})
    // the 200 results of Chrome/Edge (301 and 308) are probably caused by the permanent redirects
    // cached during crossOriginRedirectStatusCodesWithCors
    @HtmlUnitNYI(CHROME = {"TypeError", "TypeError", "TypeError", "TypeError", "TypeError"},
            EDGE = {"TypeError", "TypeError", "TypeError", "TypeError", "TypeError"})
    public void crossOriginRedirectStatusCodesWithoutCors() throws Exception {
        startSecondServer();
        final int[] codes = {301, 302, 303, 307, 308};
        final String[] urls = new String[codes.length];
        for (int i = 0; i < codes.length; i++) {
            pivot("/pivot" + codes[i], codes[i], originB() + "/echo");
            urls[i] = originA() + "/pivot" + codes[i];
        }

        loadAndVerify(multiFetchPage(urls));
    }

    /**
     * A scheme relative location is a cross origin location.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void crossOriginRedirectSchemeRelativeLocation() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, "//localhost:" + PORT2 + "/echo");

        loadAndVerify(fetchPage("", null, originA() + "/pivot"));
    }

    /**
     * A redirect to a url that contains credentials is not allowed for another origin,
     * not even if the target allows the access.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void crossOriginRedirectUserInfoInLocation() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, "http://user:password@localhost:" + PORT2 + "/echo?acao=*");

        loadAndVerify(fetchPage("", null, originA() + "/pivot"));
    }

    /**
     * Only http and https locations are followed.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"TypeError",
             "TypeError",
             "TypeError",
             "TypeError",
             "TypeError"})
    public void redirectToNonHttpSchemes() throws Exception {
        final String[] locations = {
            "file:///etc/passwd", "data:text/plain,secret", "about:blank", "ftp://localhost/secret",
            "javascript:alert(1)"
        };
        final String[] urls = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            pivot("/pivot" + i, 302, locations[i]);
            urls[i] = originA() + "/pivot" + i;
        }

        loadAndVerify(multiFetchPage(urls));
    }

    // ---------------------------------------------------------------------------
    // credentials and redirects
    // ---------------------------------------------------------------------------

    /**
     * Cookies are shared between ports, but they must not be sent to another origin.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;cookie=null;auth=null"})
    public void crossOriginRedirectDoesNotForwardCookies() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*");

        loadAndVerify(fetchPage("  document.cookie = 'secretCookie=1; path=/';\n", null, originA() + "/pivot"));
    }

    /**
     * The Authorization header is removed if the request leaves the origin.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = {"200",
                       "cors",
                       "origin=§§URL§§;cookie=null;auth=null"},
            FF = "TypeError",
            FF_ESR = "TypeError")
    @HtmlUnitNYI(FF = {"200", "cors", "origin=§§URL§§;cookie=null;auth=null"},
            FF_ESR = {"200", "cors", "origin=§§URL§§;cookie=null;auth=null"})
    public void crossOriginRedirectDropsAuthorizationHeader() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*");

        loadAndVerify(fetchPage("", "{headers: {'Authorization': 'Bearer secret'}}", originA() + "/pivot"));
    }

    /**
     * The wildcard is not allowed for requests with credentials.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void crossOriginRedirectIncludeAndWildcard() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*&acac=true");

        loadAndVerify(fetchPage("", "{credentials: 'include'}", originA() + "/pivot"));
    }

    /**
     * With credentials the origin has to be named and the credentials have to be allowed;
     * then the cookies are sent.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;cookie=c=1;auth=null"})
    public void crossOriginRedirectInclude() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acac=true&acao=" + enc(originA()));

        loadAndVerify(fetchPage("  document.cookie = 'c=1; path=/';\n",
                "{credentials: 'include'}", originA() + "/pivot"));
    }

    // ---------------------------------------------------------------------------
    // the request goes to another origin and is redirected there
    // ---------------------------------------------------------------------------

    /**
     * Each response of a cross origin request needs the CORS header, also the response that redirects.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void crossOriginRequestRedirectWithoutCorsOnRedirectResponse() throws Exception {
        startSecondServer();

        final String target = originB() + "/echo?acao=*";
        loadAndVerify(fetchPage("", null, originB() + "/redirect?to=" + enc(target)));
    }

    /**
     * The Origin stays the same as long as the redirect stays inside of the origin of the server.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;cookie=null;auth=null"})
    public void crossOriginRequestRedirectWithCors() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();

        final String target = originB() + "/echo?acao=*";
        loadAndVerify(fetchPage("", null, originB() + "/redirect?acao=*&to=" + enc(target)));
    }

    /**
     * After a redirect from a cross origin server to a third origin the Origin of the request is
     * 'null'; this is what the final response is checked against.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=null;cookie=null;auth=null"})
    public void crossOriginToCrossOriginRedirectOriginIsNull() throws Exception {
        startSecondServer();

        final String target = originC() + "/echo?acao=*";
        loadAndVerify(fetchPage("", null, originB() + "/redirect?acao=*&to=" + enc(target)));
    }

    /**
     * The origin of the page is not good enough for the final response (the Origin is 'null').
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void crossOriginToCrossOriginRedirectNeedsNullOrWildcard() throws Exception {
        startSecondServer();

        final String target = originC() + "/echo?acao=" + enc(originA());
        loadAndVerify(fetchPage("", null, originB() + "/redirect?acao=*&to=" + enc(target)));
    }

    /**
     * A redirect back to the origin of the page is still a cors response (the request left the origin).
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("TypeError")
    public void crossOriginRedirectBackToPageOrigin() throws Exception {
        startSecondServer();
        getMockWebConnection().setResponse(new URL(URL_FIRST, "/final"), "FINAL");

        final String target = originA() + "/final";
        loadAndVerify(fetchPage("", null, originB() + "/redirect?acao=*&to=" + enc(target)));
    }

    // ---------------------------------------------------------------------------
    // the response headers of a cross-origin response: only the CORS-safelisted
    // and the exposed (Access-Control-Expose-Headers) headers are visible
    // ---------------------------------------------------------------------------

    /**
     * Without Access-Control-Expose-Headers only the CORS-safelisted response headers are visible.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"cache-control,content-language,content-length,content-type,expires,last-modified,pragma",
             "null",
             "null",
             "no-cache",
             "de",
             "7",
             "null"})
    public void exposeHeadersNone() throws Exception {
        startSecondServer();
        loadAndVerify(headersPage(null, originB() + "/expose?acao=*"));
    }

    /**
     * The headers listed in Access-Control-Expose-Headers are visible (case-insensitive).
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"cache-control,content-language,content-length,content-type,expires,last-modified,pragma,x-custom",
             "abc",
             "null",
             "no-cache",
             "de",
             "7",
             "null"})
    public void exposeHeadersList() throws Exception {
        startSecondServer();
        loadAndVerify(headersPage(null, originB() + "/expose?acao=*&aceh=" + enc("x-CUSTOM, Pragma")));
    }

    /**
     * The wildcard exposes all headers (but not Set-Cookie) for a request without credentials.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"access-control-allow-origin,access-control-expose-headers,cache-control,content-language,content-length,c"
                 + "ontent-type,date,expires,last-modified,pragma,server,x-custom,x-other",
             "abc",
             "def",
             "no-cache",
             "de",
             "7",
             "*"})
    public void exposeHeadersWildcard() throws Exception {
        startSecondServer();
        loadAndVerify(headersPage(null, originB() + "/expose?acao=*&aceh=*"));
    }

    /**
     * For a request with credentials the wildcard is a header name only.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"cache-control,content-language,content-length,content-type,expires,last-modified,pragma",
             "null",
             "null",
             "no-cache",
             "de",
             "7",
             "null"})
    public void exposeHeadersWildcardWithCredentials() throws Exception {
        startSecondServer();
        loadAndVerify(headersPage("{credentials: 'include'}",
                originB() + "/expose?acac=true&acao=" + enc(originA()) + "&aceh=*"));
    }

    /**
     * For a request with credentials the header names besides the wildcard are exposed.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"cache-control,content-language,content-length,content-type,expires,last-modified,pragma,x-custom",
             "abc",
             "null",
             "no-cache",
             "de",
             "7",
             "null"})
    public void exposeHeadersWildcardAndNameWithCredentials() throws Exception {
        startSecondServer();
        loadAndVerify(headersPage("{credentials: 'include'}",
                originB() + "/expose?acac=true&acao=" + enc(originA()) + "&aceh=" + enc("*, X-Custom")));
    }

    /**
     * A request that starts inside of the origin and is redirected to another origin
     * gets a CORS filtered response also.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"cache-control,content-language,content-length,content-type,expires,last-modified,pragma,x-custom",
             "abc",
             "null",
             "no-cache",
             "de",
             "7",
             "null"})
    public void exposeHeadersAfterCrossOriginRedirect() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, originB() + "/expose?acao=*&aceh=X-Custom");
        loadAndVerify(headersPage(null, originA() + "/pivot"));
    }

    /**
     * The Origin header can not be set by the page; the server gets the origin of the page.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200",
             "cors",
             "origin=§§URL§§;cookie=null;auth=null"})
    public void originHeaderIgnored() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();

        loadAndVerify(fetchPage("", "{headers: {'Origin': '" + originB() + "'}}",
                originB() + "/echo?acao=*"));
    }

    /**
     * A page can not pass the CORS check of a server that only trusts another origin
     * by setting the Origin header to that origin.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void originHeaderDoesNotPassCorsCheck() throws Exception {
        startSecondServer();

        loadAndVerify(fetchPage("  document.cookie = 'c=1; path=/';\n",
                "{credentials: 'include', headers: {'Origin': '" + originB() + "'}}",
                originB() + "/echo?acac=true&acao=" + enc(originB())));
    }

    // ---------------------------------------------------------------------------
    // servlets
    // ---------------------------------------------------------------------------

    /**
     * Answers with the Origin, Cookie and Authorization header of the request.
     * The response header Access-Control-Allow-Origin is taken from the parameter 'acao' (none if missing),
     * Access-Control-Allow-Credentials is sent if the parameter 'acac' is 'true'.
     */
    public static class EchoServlet extends HttpServlet {
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
                throws IOException {
            final String acao = request.getParameter("acao");
            if (acao != null) {
                response.setHeader("Access-Control-Allow-Origin", acao);
            }
            if ("true".equals(request.getParameter("acac"))) {
                response.setHeader("Access-Control-Allow-Credentials", "true");
            }

            response.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_PLAIN);
            response.getWriter().write("origin=" + request.getHeader("Origin")
                    + ";cookie=" + request.getHeader("Cookie")
                    + ";auth=" + request.getHeader("Authorization"));
        }
    }

    /**
     * Responds with some CORS-safelisted and some other headers. The response headers
     * Access-Control-Allow-Origin, Access-Control-Allow-Credentials and Access-Control-Expose-Headers
     * are taken from the parameters 'acao', 'acac' and 'aceh' (none if missing).
     */
    public static class ExposeServlet extends HttpServlet {
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
                throws IOException {
            final String acao = request.getParameter("acao");
            if (acao != null) {
                response.setHeader("Access-Control-Allow-Origin", acao);
            }
            if ("true".equals(request.getParameter("acac"))) {
                response.setHeader("Access-Control-Allow-Credentials", "true");
            }
            final String aceh = request.getParameter("aceh");
            if (aceh != null) {
                response.setHeader("Access-Control-Expose-Headers", aceh);
            }

            response.setHeader("X-Custom", "abc");
            response.setHeader("X-Other", "def");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Content-Language", "de");
            response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
            response.setHeader("Last-Modified", "Thu, 01 Jan 1970 00:00:00 GMT");
            response.setHeader("Pragma", "no-cache");

            final byte[] content = "exposed".getBytes(UTF_8);
            response.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_PLAIN);
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
        }
    }

    /**
     * Redirects to the url of the parameter 'to' using the status of the parameter 'status' (default 302).
     * The response header Access-Control-Allow-Origin is taken from the parameter 'acao' (none if missing).
     */
    public static class RedirectServlet extends HttpServlet {
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
                throws IOException {
            final String acao = request.getParameter("acao");
            if (acao != null) {
                response.setHeader("Access-Control-Allow-Origin", acao);
            }

            final String status = request.getParameter("status");
            response.setStatus(status == null ? 302 : Integer.parseInt(status));
            response.setHeader("Location", request.getParameter("to"));
        }
    }
}
