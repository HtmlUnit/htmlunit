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
package org.htmlunit.javascript.host.xml;

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
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Tests for Cross-Origin Resource Sharing for {@link XMLHttpRequest}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 * @author Lai Quang Duong
 */
public class XMLHttpRequestCORSTest extends WebDriverTestCase {

    /**
     * The second server replaces the mock-connection server on PORT2. The base class only starts the mock
     * servers if PORT2 is free, so we have to stop them; otherwise later tests (e.g. baseUrlRelativeRequest)
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
        startWebServer2(".", servlets2);
    }


    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"error [object ProgressEvent]", "error", "false", "0", "false"})
    public void noCorsHeaderCallsErrorHandler() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = '" + URL_THIRD + "';\n"
                + "    xhr.open('GET', url, true);\n"
                + "    xhr.onerror = function(event) {\n"
                + "                    log('error ' + event);\n"
                + "                    log(event.type);\n"
                + "                    log(event.lengthComputable);\n"
                + "                    log(event.loaded);\n"
                + "                    log(event.total > 0);\n"
                + "                  };\n"
                + "    xhr.send();\n"
                + "  } catch(e) { logEx(e); }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "<root><origin>§§URL§§</origin><cookie>null</cookie></root>"})
    public void simple() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/simple2';\n"
                + "    xhr.open('GET', url, false);\n"
                + "    xhr.send();\n"
                + "    log(xhr.readyState);\n"
                + "    log(xhr.status);\n"
                + "    log(xhr.responseText);\n"
                + "  } catch(e) { log(e) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "*";
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/simple2", SimpleServerServlet.class);
        startWebServer2(".", servlets2);

        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "cookie=sweet"));

        final URL url = new URL(URL_FIRST, "/simple1");
        getMockWebConnection().setResponse(url, html,
                200, "OK", "text/html;charset=ISO-8859-1", ISO_8859_1, responseHeader);

        loadPage2(url, null);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "null"})
    public void simpleHead() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/simple2';\n"
                + "    xhr.open('HEAD', url, false);\n"
                + "    xhr.send();\n"
                + "    log(xhr.readyState);\n"
                + "    log(xhr.status);\n"
                + "    log(xhr.responseXML);\n"
                + "  } catch(e) { log(e) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "*";
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/simple2", SimpleServerServlet.class);
        startWebServer2(".", servlets2);

        loadPage2(html, new URL(URL_FIRST, "/simple1"));
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "<root><origin>§§URL§§</origin><cookie>null</cookie></root>"})
    public void simplePost() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/simple2';\n"
                + "    xhr.open('POST', url, false);\n"
                + "    xhr.send('');\n"
                + "    log(xhr.readyState);\n"
                + "    log(xhr.status);\n"
                + "    log(xhr.responseText);\n"
                + "  } catch(e) { log(e) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "*";
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/simple2", SimpleServerServlet.class);
        startWebServer2(".", servlets2);

        loadPage2(html, new URL(URL_FIRST, "/simple1"));
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("NetworkError/DOMException")
    public void simplePut() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/simple2';\n"
                + "    xhr.open('PUT', url, false);\n"
                + "    xhr.send('');\n"
                + "    log(xhr.readyState);\n"
                + "    log(xhr.status);\n"
                + "    log(xhr.responseXML.firstChild.firstChild.nodeValue);\n"
                + "  } catch(e) { logEx(e) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "*";
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/simple2", SimpleServerServlet.class);
        startWebServer2(".", servlets2);

        loadPage2(html, new URL(URL_FIRST, "/simple1"));
        verifyTitle2(getWebDriver(), getExpectedAlerts());
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

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"NetworkError/DOMException", "4", "0", ""})
    public void noAccessControlAllowOrigin() throws Exception {
        incorrectAccessControlAllowOrigin(null);
    }

    private void incorrectAccessControlAllowOrigin(final String header) throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/simple2';\n"
                + "    xhr.open('GET', url, false);\n"
                + "    xhr.send();\n"
                + "  } catch(e) { logEx(e) }\n"
                + "  log(xhr.readyState);\n"
                + "  log(xhr.status);\n"
                + "  log(xhr.responseText);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = header;
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/simple2", SimpleServerServlet.class);
        startWebServer2(".", servlets2);

        loadPage2(html, new URL(URL_FIRST, "/simple1"));
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"NetworkError/DOMException", "4", "0", ""})
    public void nonMatchingAccessControlAllowOrigin() throws Exception {
        incorrectAccessControlAllowOrigin("http://www.sourceforge.net");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "§§URL§§", "§§URL§§", "GET", "x-pingother", "null"})
    public void preflight() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", MimeType.TEXT_PLAIN, null);

        releaseResources();
        shutDownAll();
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "§§URL§§", "§§URL§§", "GET", "x-pingother", "null"})
    public void preflight_cookie() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", MimeType.TEXT_PLAIN, "cookie=sweet");

        releaseResources();
        shutDownAll();
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "§§URL§§", "§§URL§§", "GET", "x-pingother", "null"})
    // unstable test case, this will work on real Chrome if individually run, but will fail if run with other cases
    public void preflight_contentTypeWithCharset() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", MimeType.TEXT_PLAIN + ";charset=utf-8", null);

        releaseResources();
        shutDownAll();
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "§§URL§§", "§§URL§§", "GET", "x-pingother", "null"})
    // unstable test case, this will work on real Chrome if individually run, but will fail if run with other cases
    public void preflightUrlEncoded() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", "application/x-www-form-urlencoded", null);

        releaseResources();
        shutDownAll();
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "§§URL§§", "§§URL§§", "GET", "x-pingother", "null"})
    // unstable test case, this will work on real Chrome if individually run, but will fail if run with other cases
    public void preflightUrlEncoded_contentTypeWithCharset() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", "application/x-www-form-urlencoded;charset=utf-8", null);

        releaseResources();
        shutDownAll();
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "§§URL§§", "§§URL§§", "GET", "x-pingother", "null"})
    // unstable test case, this will work on real Chrome if individually run, but will fail if run with other cases
    public void preflightMultipart() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", "multipart/form-data", null);

        releaseResources();
        shutDownAll();
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "§§URL§§", "§§URL§§", "GET", "x-pingother", "null"})
    // unstable test case, this will work on real Chrome if individually run, but will fail if run with other cases
    public void preflightMultipart_contentTypeWithCharset() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", "multipart/form-data;charset=utf-8", null);

        releaseResources();
        shutDownAll();
    }

    /**
     * Seems that "Access-Control-Allow-Methods" is not considered by FF.
     *
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "§§URL§§", "§§URL§§", "GET", "x-pingother", "null"})
    // unstable test case, this will fail on real Chrome if individually run, but will succeed if run with other cases
    public void preflight_incorrect_methods() throws Exception {
        doPreflightTestAllowedMethods(null, MimeType.TEXT_PLAIN, null);

        releaseResources();
        shutDownAll();
    }

    private void doPreflightTestAllowedMethods(final String allowedMethods,
                    final String contentType, final String cookie)
                            throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT)); // url without trailing "/"

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var xhr = new XMLHttpRequest();\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/preflight2';\n"
            + "    xhr.open('GET', url, false);\n"
            + "    xhr.setRequestHeader('X-PINGOTHER', 'pingpong');\n"
            + "    xhr.setRequestHeader('Content-Type' , '" + contentType + "');\n"
            + "    xhr.send();\n"
            + "    log(xhr.readyState);\n"
            + "    log(xhr.status);\n"
            + "    log(xhr.responseXML.firstChild.childNodes[0].firstChild.nodeValue);\n"
            + "    log(xhr.responseXML.firstChild.childNodes[1].firstChild.nodeValue);\n"
            + "    log(xhr.responseXML.firstChild.childNodes[2].firstChild.nodeValue);\n"
            + "    log(xhr.responseXML.firstChild.childNodes[3].firstChild.nodeValue);\n"
            + "    log(xhr.responseXML.firstChild.childNodes[4].firstChild.nodeValue);\n"
            + "  } catch(e) { log(e) }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "http://localhost:" + PORT;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = allowedMethods;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = "X-PINGOTHER";
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/preflight2", PreflightServerServlet.class);
        startWebServer2(".", servlets2);

        final URL url = new URL(URL_FIRST, "/preflight1");

        List<NameValuePair> responseHeader = null;
        if (cookie != null) {
            responseHeader = new ArrayList<>();
            responseHeader.add(new NameValuePair("Set-Cookie", cookie));
        }

        getMockWebConnection().setResponse(url, html,
                200, "OK", "text/html;charset=ISO-8859-1", ISO_8859_1, responseHeader);
        loadPage2(url, null);

        verifyTitle2(getWebDriver(), getExpectedAlerts());
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
            response.setContentType(MimeType.TEXT_XML);
            final Writer writer = response.getWriter();

            final String origin = request.getHeader(HttpHeader.ORIGIN);
            writer.write("<result>"
                + "<origin>" + origin + "</origin>"
                + "<options_origin>" + options_origin_ + "</options_origin>"
                + "<options_method>" + options_method_ + "</options_method>"
                + "<options_headers>" + options_headers_ + "</options_headers>"
                + "<options_cookie>" + options_cookie_ + "</options_cookie>"
                + "</result>");
        }
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"NetworkError/DOMException", "4", "0"})
    // unstable test case, this will fail on real Chrome if individually run, but will succeed if run with other cases
    public void preflight_incorrect_headers() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/preflight2';\n"
                + "    xhr.open('GET', url, false);\n"
                + "    xhr.setRequestHeader('X-PINGOTHER', 'pingpong');\n"
                + "    xhr.send();\n"
                + "  } catch(e) { logEx(e) }\n"
                + "  log(xhr.readyState);\n"
                + "  log(xhr.status);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "http://localhost:" + PORT;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = null;

        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/preflight2", PreflightServerServlet.class);

        loadPage2(html, new URL(URL_FIRST, "/preflight1"), new HashMap<>(), servlets2);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Same as {@link #preflight_incorrect_headers}, but the preflight (OPTIONS)
     * response omits Access-Control-Allow-Origin entirely rather than
     * Access-Control-Allow-Headers. This exercises isPreflightAuthorized()'s
     * comparison of the request's Origin header against the
     * Access-Control-Allow-Origin response header directly -- the request must be
     * denied with a NetworkError.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"NetworkError/DOMException", "4", "0"})
    public void preflight_missing_allow_origin() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/preflight2';\n"
                + "    xhr.open('GET', url, false);\n"
                + "    xhr.setRequestHeader('X-PINGOTHER', 'pingpong');\n"
                + "    xhr.send();\n"
                + "  } catch(e) { logEx(e) }\n"
                + "  log(xhr.readyState);\n"
                + "  log(xhr.status);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = null;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = "X-PINGOTHER";

        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/preflight2", PreflightServerServlet.class);

        loadPage2(html, new URL(URL_FIRST, "/preflight1"), new HashMap<>(), servlets2);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "options_headers", "x-ping,x-pong"})
    public void preflight_many_header_values() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/preflight2';\n"
                + "    xhr.open('GET', url, false);\n"
                + "    xhr.setRequestHeader('X-PING', 'ping');\n"
                + "    xhr.setRequestHeader('X-PONG', 'pong');\n"
                + "    xhr.send();\n"
                + "  } catch(e) { logEx(e) }\n"
                + "  log(xhr.readyState);\n"
                + "  log(xhr.status);\n"
                + "  log(xhr.responseXML.firstChild.childNodes[3].tagName);\n"
                + "  log(xhr.responseXML.firstChild.childNodes[3].firstChild.nodeValue);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "http://localhost:" + PORT;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = "X-PING, X-PONG";

        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/preflight2", PreflightServerServlet.class);

        loadPage2(html, new URL(URL_FIRST, "/preflight1"), new HashMap<>(), servlets2);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200"})
    public void preflight_wildcard_allow_headers() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/preflight2';\n"
                + "    xhr.open('GET', url, false);\n"
                + "    xhr.setRequestHeader('X-PING', 'ping');\n"
                + "    xhr.setRequestHeader('X-PONG', 'pong');\n"
                + "    xhr.send();\n"
                + "    log(xhr.readyState);\n"
                + "    log(xhr.status);\n"
                + "  } catch(e) { logEx(e) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "http://localhost:" + PORT;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = "*";

        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/preflight2", PreflightServerServlet.class);

        loadPage2(html, new URL(URL_FIRST, "/preflight1"), new HashMap<>(), servlets2);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("false")
    public void withCredentials_defaultValue() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/withCredentials2';\n"
                + "    xhr.open('GET', url, true);\n"
                + "    log(xhr.withCredentials);\n"
                + "  } catch(e) { log(e) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadPage2(html, new URL(URL_FIRST, "/withCredentials1"));
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"false", "true", "false", "true"})
    public void withCredentials_setBeforeOpenSync() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    log(xhr.withCredentials);\n"

                + "    try {\n"
                + "      xhr.withCredentials = true;\n"
                + "      log(xhr.withCredentials);\n"
                + "    } catch(e) { log('ex: withCredentials=true') }\n"

                + "    try {\n"
                + "      xhr.withCredentials = false;\n"
                + "      log(xhr.withCredentials);\n"
                + "    } catch(e) { log('ex: withCredentials=false') }\n"

                + "    try {\n"
                + "      xhr.withCredentials = true;\n"
                + "    } catch(e) { log('ex: withCredentials=true') }\n"

                + "    try {\n"
                + "      xhr.open('GET', '/foo.xml', false);\n"
                + "    } catch(e) { log('ex: open') }\n"
                + "    log(xhr.withCredentials);\n"
                + "  } catch(ex) { log(ex) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"false", "true", "false", "true"})
    public void withCredentials_setBeforeOpenAsync() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    log(xhr.withCredentials);\n"

                + "    try {\n"
                + "      xhr.withCredentials = true;\n"
                + "      log(xhr.withCredentials);\n"
                + "    } catch(e) { log('ex: withCredentials=true') }\n"

                + "    try {\n"
                + "      xhr.withCredentials = false;\n"
                + "      log(xhr.withCredentials);\n"
                + "    } catch(e) { log('ex: withCredentials=false') }\n"

                + "    try {\n"
                + "      xhr.withCredentials = true;\n"
                + "    } catch(e) { log('ex: withCredentials=true') }\n"

                + "    try {\n"
                + "      xhr.open('GET', '/foo.xml', true);\n"
                + "    } catch(e) { log('ex: open') }\n"
                + "    log(xhr.withCredentials);\n"
                + "  } catch(ex) { log(ex) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"false", "false", "true", "false"})
    public void withCredentials_setAfterOpenSync() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    log(xhr.withCredentials);\n"
                + "    xhr.open('GET', '/foo.xml', false);\n"
                + "    log(xhr.withCredentials);\n"

                + "    try {\n"
                + "      xhr.withCredentials = true;\n"
                + "      log(xhr.withCredentials);\n"
                + "    } catch(e) { log('ex: withCredentials=true') }\n"

                + "    try {\n"
                + "      xhr.withCredentials = false;\n"
                + "      log(xhr.withCredentials);\n"
                + "    } catch(e) { log('ex: withCredentials=false') }\n"
                + "  } catch(ex) { log(ex) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"false", "false", "true", "false"})
    public void withCredentials_setAfterOpenAsync() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    log(xhr.withCredentials);\n"
                + "    xhr.open('GET', '/foo.xml', false);\n"
                + "    log(xhr.withCredentials);\n"

                + "    try {\n"
                + "      xhr.withCredentials = true;\n"
                + "      log(xhr.withCredentials);\n"
                + "    } catch(e) { log('ex: withCredentials=true') }\n"

                + "    try {\n"
                + "      xhr.withCredentials = false;\n"
                + "      log(xhr.withCredentials);\n"
                + "    } catch(e) { log('ex: withCredentials=false') }\n"
                + "  } catch(ex) { log(ex) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"1", "0", "4", "0", ""})
    public void withCredentials() throws Exception {
        testWithCredentials("*", "true");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"1", "0", "4", "200", "<root><origin>§§URL§§</origin><cookie>cookie=sweet</cookie></root>"})
    public void withCredentialsServer() throws Exception {
        testWithCredentials("http://localhost:" + PORT, "true");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"1", "0", "4", "0", ""})
    public void withCredentialsServerSlashAtEnd() throws Exception {
        testWithCredentials(URL_FIRST.toExternalForm(), "true");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"1", "0", "4", "0", ""})
    public void withCredentials_no_header() throws Exception {
        testWithCredentials("*", null);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"1", "0", "4", "0", ""})
    public void withCredentials_no_header_Server() throws Exception {
        testWithCredentials("http://localhost:" + PORT, null);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"1", "0", "4", "0", ""})
    public void withCredentials_no_header_ServerSlashAtEnd() throws Exception {
        testWithCredentials(URL_FIRST.toExternalForm(), null);
    }

    private void testWithCredentials(final String accessControlAllowOrigin,
            final String accessControlAllowCredentials) throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/withCredentials2';\n"
                + "    xhr.open('GET', url, true);\n"
                + "    xhr.withCredentials = true;\n"
                + "    xhr.onreadystatechange = onReadyStateChange;\n"
                + "    xhr.send();\n"
                + "  } catch(e) { logEx(e) }\n"
                + "  log(xhr.readyState);\n"
                + "  try {\n"
                + "    log(xhr.status);\n"
                + "  } catch(e) { log('ex: status not available') }\n"

                + "  function onReadyStateChange() {\n"
                + "    if (xhr.readyState == 4) {\n"
                + "      log(xhr.readyState);\n"
                + "      log(xhr.status);\n"
                + "      log(xhr.responseText);\n"
                + "    }\n"
                + "  }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        WithCredentialsServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = accessControlAllowOrigin;
        WithCredentialsServerServlet.ACCESS_CONTROL_ALLOW_CREDENTIALS_ = accessControlAllowCredentials;
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/withCredentials2", WithCredentialsServerServlet.class);
        startWebServer2(".", servlets2);

        final List<NameValuePair> responseHeader = new ArrayList<>();
        responseHeader.add(new NameValuePair("Set-Cookie", "cookie=sweet"));

        final URL url = new URL(URL_FIRST, "/withCredentials1");
        getMockWebConnection().setResponse(url, html,
                200, "OK", "text/html;charset=ISO-8859-1", ISO_8859_1, responseHeader);

        final WebDriver driver = loadPage2(url, null);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("done 200")
    public void testWithCredentialsIFrame() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_WINDOW_NAME_FUNCTION

                + "function load() {\n"
                + "  try {\n"
                + "    var myContent = '<!DOCTYPE html><html><head></head><body>"
                            + "<script src=\"get.js\"><\\/script><p>tttttt</p></body></html>';\n"
                + "    window.asyncLoadIFrame = document.createElement('iframe');\n"
                + "    asyncLoadIFrame.id = 'asyncLoadIFrame';\n"
                + "    asyncLoadIFrame.src = 'about:blank';\n"
                + "    document.body.appendChild(asyncLoadIFrame);\n"

                + "    asyncLoadIFrame.contentWindow.document.open('text/html', 'replace');\n"
                + "    asyncLoadIFrame.contentWindow.document.write(myContent);\n"
                + "    asyncLoadIFrame.contentWindow.document.close();\n"
                + "  } catch(e) { log(e) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='load()'>\n"
                + "</body></html>";

        final String js =
                LOG_WINDOW_NAME_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "  try {\n"
                + "    var url = '/data';\n"
                + "    xhr.open('GET', url, true);\n"
                + "    xhr.withCredentials = true;\n"
                + "    xhr.onreadystatechange = onReadyStateChange;\n"
                + "    xhr.send();\n"
                + "  } catch(e) { log(e) }\n"

                + "  function onReadyStateChange() {\n"
                + "    if (xhr.readyState == 4) {\n"
                + "      log('done ' + xhr.status);\n"
                + "    }\n"
                + "  }\n";

        getMockWebConnection().setDefaultResponse(js, MimeType.TEXT_JAVASCRIPT);
        final String xml = "<xml><content>blah</content></xml>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "/data"), xml, MimeType.TEXT_XML);

        loadPage2(html);
        verifyWindowName2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
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
            response.setContentType(MimeType.TEXT_XML);

            String origin = request.getHeader(HttpHeader.ORIGIN);
            if (origin == null) {
                origin = "No Origin!";
            }
            final String cookie = request.getHeader(HttpHeader.COOKIE);
            response.getWriter().write("<root><origin>" + origin + "</origin><cookie>" + cookie + "</cookie></root>");
        }
    }

    /**
     * Tests that a same-origin or initial XMLHttpRequest following a 3xx redirect
     * to a cross-origin resource lacking proper CORS headers (Access-Control-Allow-Origin)
     * is correctly blocked, resulting in a NetworkError/DOMException instead of exposing the response body.
     *
     * Flow tested:
     * 1. An attacker page (origin A) makes a synchronous XMLHttpRequest to a pivot endpoint on the same origin.
     * 2. The pivot endpoint responds with a 3xx redirect pointing to an internal/secret cross-origin resource (origin B).
     * 3. The underlying client follows the redirect to origin B.
     * 4. Because origin B does not return Access-Control-Allow-Origin headers, the cross-origin response is blocked,
     *    throwing a NetworkError and preventing unauthorized body exposure.
     *
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"NetworkError/DOMException", "4", "0", ""})
    public void crossOriginRedirectWithoutCors() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT + "/pivot';\n"
                + "    xhr.open('GET', url, false);\n"
                + "    xhr.send();\n"
                + "  } catch(e) { logEx(e) }\n"
                + "  log(xhr.readyState);\n"
                + "  log(xhr.status);\n"
                + "  log(xhr.responseText);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<>();
        servlets2.put("/secret", SecretServerServlet.class);
        startWebServer2(".", servlets2);

        final URL pageUrl = new URL(URL_FIRST, "/page");
        final URL pivotUrl = new URL(URL_FIRST, "/pivot");
        final URL secretUrl = new URL("http://localhost:" + PORT2 + "/secret");

        getMockWebConnection().setResponse(pageUrl, html, 200, "OK", "text/html;charset=UTF-8", UTF_8, null);

        final List<NameValuePair> redirectHeaders = new ArrayList<>();
        redirectHeaders.add(new NameValuePair("Location", secretUrl.toExternalForm()));
        getMockWebConnection().setResponse(pivotUrl, "", 302, "Found", "text/html", redirectHeaders);

        loadPage2(pageUrl, null);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
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
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "Hello from Data URL"})
    public void dataUrl() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'data:text/plain;charset=utf-8,Hello from Data URL';\n"
                + "    xhr.open('GET', url, false);\n"
                + "    xhr.send();\n"
                + "    log(xhr.readyState);\n"
                + "    log(xhr.status);\n"
                + "    log(xhr.responseText);\n"
                + "  } catch(e) { log(e) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "Hello from Blob"})
    public void blobUrlSameOrigin() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var blob = new Blob(['Hello from Blob'], {type: 'text/plain'});\n"
                + "    var url = URL.createObjectURL(blob);\n"
                + "    xhr.open('GET', url, false);\n"
                + "    xhr.send();\n"
                + "    log(xhr.readyState);\n"
                + "    log(xhr.status);\n"
                + "    log(xhr.responseText);\n"
                + "  } catch(e) { log(e) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }
 // ---------------------------------------------------------------------------
    // XMLHttpRequest, CORS and redirects
    // methods and servlets to be added to the test class that contains crossOriginRedirectWithoutCors
    //
    // assumed to be available (as used by crossOriginRedirectWithoutCors): PORT, PORT2, URL_FIRST,
    // startWebServer2(), getMockWebConnection(), expandExpectedAlertsVariables(), verifyTitle2(),
    // loadPage2(), NameValuePair, MimeType
    // additional imports:
    //     import java.net.URLEncoder;
    // ---------------------------------------------------------------------------

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
        final URL pageUrl = new URL(URL_FIRST, "/page");
        getMockWebConnection().setResponse(pageUrl, html, 200, "OK", "text/html;charset=UTF-8", UTF_8, null);

        loadPage2(pageUrl, null);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /** synchronous request; logs readyState, status and responseText (and the exception, if there is one). */
    private static String xhrPage(final String before, final String afterOpen, final String url) {
        return DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + before
                + "  try {\n"
                + "    xhr.open('GET', '" + url + "', false);\n"
                + afterOpen
                + "    xhr.send();\n"
                + "  } catch(e) { logEx(e) }\n"
                + "  log(xhr.readyState);\n"
                + "  log(xhr.status);\n"
                + "  log(xhr.responseText);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";
    }

    /** asynchronous request; logs status and responseText or 'error', status and responseText. */
    private static String asyncXhrPage(final String before, final String afterOpen, final String url) {
        return DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "var xhr = new XMLHttpRequest();\n"
                + "function test() {\n"
                + before
                + "  xhr.onload = function() {\n"
                + "    log(xhr.status);\n"
                + "    log(xhr.responseText);\n"
                + "  };\n"
                + "  xhr.onerror = function() {\n"
                + "    log('error');\n"
                + "    log(xhr.status);\n"
                + "    log('[' + xhr.responseText + ']');\n"
                + "  };\n"
                + "  xhr.open('GET', '" + url + "');\n"
                + afterOpen
                + "  xhr.send();\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";
    }

    /** one synchronous request per url; logs the status or the exception. */
    private static String multiXhrPage(final String... urls) {
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
                + "  for (var i = 0; i < urls.length; i++) {\n"
                + "    var xhr = new XMLHttpRequest();\n"
                + "    try {\n"
                + "      xhr.open('GET', urls[i], false);\n"
                + "      xhr.send();\n"
                + "      log(xhr.status);\n"
                + "    } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";
    }

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

    // ---------------------------------------------------------------------------
    // the page origin is not left, or the target allows the access
    // ---------------------------------------------------------------------------

    /**
     * A redirect inside of the origin is always fine.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "FINAL"})
    public void sameOriginRedirect() throws Exception {
        pivot("/pivot", 302, new URL(URL_FIRST, "/final").toExternalForm());
        getMockWebConnection().setResponse(new URL(URL_FIRST, "/final"), "FINAL");

        loadAndVerify(xhrPage("", "", originA() + "/pivot"));
    }

    /**
     * The request starts inside of the origin, only the final response needs the CORS header.
     * The Origin header is the one of the page; no cookies and no authorization are sent.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "origin=§§URL§§;cookie=null;auth=null"})
    public void crossOriginRedirectWithCorsWildcard() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*");

        loadAndVerify(xhrPage("", "", originA() + "/pivot"));
    }

    /**
     * The final response is also readable if it allows exactly the origin of the page.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "origin=§§URL§§;cookie=null;auth=null"})
    public void crossOriginRedirectWithCorsOrigin() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=" + enc(originA()));

        loadAndVerify(xhrPage("", "", originA() + "/pivot"));
    }

    /**
     * The final url is available after a redirect that was allowed.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "§§URL§§/echo?acao=*"})
    public void crossOriginRedirectResponseUrl() throws Exception {
        expandExpectedAlertsVariables(new URL(originB()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*");

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var xhr = new XMLHttpRequest();\n"
                + "  xhr.open('GET', '" + originA() + "/pivot', false);\n"
                + "  xhr.send();\n"
                + "  log(xhr.readyState);\n"
                + "  log(xhr.status);\n"
                + "  log(xhr.responseURL);\n"
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
    @Alerts({"200", "200", "200", "200", "200"})
    public void crossOriginRedirectStatusCodesWithCors() throws Exception {
        startSecondServer();
        final int[] codes = {301, 302, 303, 307, 308};
        final String[] urls = new String[codes.length];
        for (int i = 0; i < codes.length; i++) {
            pivot("/pivot" + codes[i], codes[i], originB() + "/echo?acao=*");
            urls[i] = originA() + "/pivot" + codes[i];
        }

        loadAndVerify(multiXhrPage(urls));
    }

    // ---------------------------------------------------------------------------
    // the response has to be blocked
    // ---------------------------------------------------------------------------

    /**
     * An Access-Control-Allow-Origin header for another origin does not help.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"NetworkError/DOMException", "4", "0", ""})
    public void crossOriginRedirectWithWrongOrigin() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=" + enc("http://other.example.com"));

        loadAndVerify(xhrPage("", "", originA() + "/pivot"));
    }

    /**
     * All redirect status codes are blocked.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = {"200", "NetworkError/DOMException", "NetworkError/DOMException",
            "NetworkError/DOMException", "200"},
            FF = {"NetworkError/DOMException", "NetworkError/DOMException", "NetworkError/DOMException",
                  "NetworkError/DOMException", "NetworkError/DOMException"},
            FF_ESR = {"NetworkError/DOMException", "NetworkError/DOMException", "NetworkError/DOMException",
                      "NetworkError/DOMException", "NetworkError/DOMException"})
    public void crossOriginRedirectStatusCodesWithoutCors() throws Exception {
        startSecondServer();
        final int[] codes = {301, 302, 303, 307, 308};
        final String[] urls = new String[codes.length];
        for (int i = 0; i < codes.length; i++) {
            pivot("/pivot" + codes[i], codes[i], originB() + "/echo");
            urls[i] = originA() + "/pivot" + codes[i];
        }

        loadAndVerify(multiXhrPage(urls));
    }

    /**
     * A scheme relative location is a cross origin location.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"NetworkError/DOMException", "4", "0", ""})
    public void crossOriginRedirectSchemeRelativeLocation() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, "//localhost:" + PORT2 + "/echo");

        loadAndVerify(xhrPage("", "", originA() + "/pivot"));
    }

    /**
     * A redirect to a url that contains credentials is not allowed for another origin,
     * not even if the target allows the access.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"NetworkError/DOMException", "4", "0", ""})
    public void crossOriginRedirectUserInfoInLocation() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, "http://user:password@localhost:" + PORT2 + "/echo?acao=*");

        loadAndVerify(xhrPage("", "", originA() + "/pivot"));
    }

    /**
     * Only http and https locations are followed.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"NetworkError/DOMException", "NetworkError/DOMException", "NetworkError/DOMException",
             "NetworkError/DOMException", "NetworkError/DOMException"})
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

        loadAndVerify(multiXhrPage(urls));
    }

    // ---------------------------------------------------------------------------
    // credentials
    // ---------------------------------------------------------------------------

    /**
     * Cookies are shared between ports, but they must not be sent to another origin.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "origin=§§URL§§;cookie=null;auth=null"})
    public void crossOriginRedirectDoesNotForwardCookies() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*");

        loadAndVerify(xhrPage("  document.cookie = 'secretCookie=1; path=/';\n", "", originA() + "/pivot"));
    }

    /**
     * The Authorization header is removed if the request leaves the origin.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = {"4", "200", "origin=§§URL§§;cookie=null;auth=null"},
            FF = {"NetworkError/DOMException", "4", "0", ""},
            FF_ESR = {"NetworkError/DOMException", "4", "0", ""})
    public void crossOriginRedirectDropsAuthorizationHeader() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*");

        loadAndVerify(xhrPage("", "    xhr.setRequestHeader('Authorization', 'Bearer secret');\n",
                originA() + "/pivot"));
    }

    /**
     * The wildcard is not allowed for requests with credentials.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"error", "0", "[]"})
    public void crossOriginRedirectWithCredentialsAndWildcard() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acao=*&acac=true");

        loadAndVerify(asyncXhrPage("", "  xhr.withCredentials = true;\n", originA() + "/pivot"));
    }

    /**
     * With credentials the origin has to be named and the credentials have to be allowed;
     * then the cookies are sent.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"200", "origin=§§URL§§;cookie=c=1;auth=null"})
    public void crossOriginRedirectWithCredentials() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo?acac=true&acao=" + enc(originA()));

        loadAndVerify(asyncXhrPage("  document.cookie = 'c=1; path=/';\n",
                "  xhr.withCredentials = true;\n", originA() + "/pivot"));
    }

    // ---------------------------------------------------------------------------
    // the request goes to another origin and is redirected there
    // ---------------------------------------------------------------------------

    /**
     * Each response of a cross origin request needs the CORS header, also the response that redirects.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"NetworkError/DOMException", "4", "0", ""})
    public void crossOriginRequestRedirectWithoutCorsOnRedirectResponse() throws Exception {
        startSecondServer();

        final String target = originB() + "/echo?acao=*";
        loadAndVerify(xhrPage("", "", originB() + "/redirect?to=" + enc(target)));
    }

    /**
     * The Origin stays the same as long as the redirect stays inside of the origin of the server.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "origin=§§URL§§;cookie=null;auth=null"})
    public void crossOriginRequestRedirectWithCors() throws Exception {
        expandExpectedAlertsVariables(new URL(originA()));
        startSecondServer();

        final String target = originB() + "/echo?acao=*";
        loadAndVerify(xhrPage("", "", originB() + "/redirect?acao=*&to=" + enc(target)));
    }

    /**
     * After a redirect from a cross origin server to a third origin the Origin of the request is
     * 'null'; this is what the final response is checked against.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"4", "200", "origin=null;cookie=null;auth=null"})
    public void crossOriginToCrossOriginRedirectOriginIsNull() throws Exception {
        startSecondServer();

        final String target = originC() + "/echo?acao=*";
        loadAndVerify(xhrPage("", "", originB() + "/redirect?acao=*&to=" + enc(target)));
    }

    /**
     * The origin of the page is not good enough for the final response (the Origin is 'null').
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"NetworkError/DOMException", "4", "0", ""})
    public void crossOriginToCrossOriginRedirectNeedsNullOrWildcard() throws Exception {
        startSecondServer();

        final String target = originC() + "/echo?acao=" + enc(originA());
        loadAndVerify(xhrPage("", "", originB() + "/redirect?acao=*&to=" + enc(target)));
    }

    // ---------------------------------------------------------------------------
    // what is visible after the request was blocked
    // ---------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"error", "4", "0", "[]"})
    public void asyncCrossOriginRedirectWithoutCors() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo");

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var xhr = new XMLHttpRequest();\n"
                + "  xhr.onload = function() { log('load'); log(xhr.responseText); };\n"
                + "  xhr.onerror = function() {\n"
                + "    log('error');\n"
                + "    log(xhr.readyState);\n"
                + "    log(xhr.status);\n"
                + "    log('[' + xhr.responseText + ']');\n"
                + "  };\n"
                + "  xhr.open('GET', '" + originA() + "/pivot');\n"
                + "  xhr.send();\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadAndVerify(html);
    }

    /**
     * Nothing of the blocked response may be visible.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"NetworkError/DOMException", "[]", "[]", "null", "[]", "null", "[]", "[]"})
    public void crossOriginRedirectWithoutCorsExposesNothing() throws Exception {
        startSecondServer();
        pivot("/pivot", 302, originB() + "/echo");

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var xhr = new XMLHttpRequest();\n"
                + "  try {\n"
                + "    xhr.open('GET', '" + originA() + "/pivot', false);\n"
                + "    xhr.send();\n"
                + "  } catch(e) { logEx(e) }\n"
                + "  log('[' + xhr.response + ']');\n"
                + "  log('[' + xhr.responseText + ']');\n"
                + "  log(xhr.responseXML);\n"
                + "  log('[' + xhr.getAllResponseHeaders() + ']');\n"
                + "  log(xhr.getResponseHeader('Content-Type'));\n"
                + "  log('[' + xhr.statusText + ']');\n"
                + "  log('[' + xhr.responseURL + ']');\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadAndVerify(html);
    }
}

