/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for Cross-Origin Resource Sharing for {@link XMLHttpRequest}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLHttpRequestCORSTest extends WebDriverTestCase {

    private static String XHRInstantiation_ = "(window.XMLHttpRequest ? "
        + "new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'))";

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = "error",
            IE8 = { })
    public void noCorsHeaderCallsErrorHandler() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "var xhr = " + XHRInstantiation_ + ";\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = '" + URL_THIRD + "';\n"
                + "    xhr.open('GET',  url, true);\n"
                + "    xhr.onerror = function() { alert('error'); };\n"
                + "    xhr.send();\n"
                + "  } catch(e) { alert('exception'); }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(IE = { "4", "200", "No Origin!" }, DEFAULT = { "4", "200", "§§URL§§" })
    public void simple() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = "<html><head>\n"
                + "<script>\n"
                + "var xhr = " + XHRInstantiation_ + ";\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/simple2';\n"
                + "    xhr.open('GET',  url, false);\n"
                + "    xhr.send();\n"
                + "    alert(xhr.readyState);\n"
                + "    alert(xhr.status);\n"
                + "    alert(xhr.responseXML.firstChild.firstChild.nodeValue);"
                + "  } catch(e) { alert(e) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "*";
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<String, Class<? extends Servlet>>();
        servlets2.put("/simple2", SimpleServerServlet.class);
        startWebServer2(".", null, servlets2);

        loadPageWithAlerts2(html, new URL(getDefaultUrl(), "/simple1"));
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
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
            String origin = request.getHeader("Origin");
            if (origin == null) {
                origin = "No Origin!";
            }
            response.getWriter().write("<origin>" + origin + "</origin>");
        }
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(IE = { "4", "200" }, DEFAULT = { "exception", "4", "0" })
    public void noAccessControlAllowOrigin() throws Exception {
        incorrectAccessControlAllowOrigin(null);
    }

    private void incorrectAccessControlAllowOrigin(final String header) throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = "<html><head>\n"
                + "<script>\n"
                + "var xhr = " + XHRInstantiation_ + ";\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/simple2';\n"
                + "    xhr.open('GET',  url, false);\n"
                + "    xhr.send();\n"
                + "  } catch(e) { alert('exception') }\n"
                + "  alert(xhr.readyState);\n"
                + "  alert(xhr.status);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = header;
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<String, Class<? extends Servlet>>();
        servlets2.put("/simple2", SimpleServerServlet.class);
        startWebServer2(".", null, servlets2);

        loadPageWithAlerts2(html, new URL(getDefaultUrl(), "/simple1"));
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(IE = { "4", "200" }, DEFAULT = { "exception", "4", "0" })
    public void nonMatchingAccessControlAllowOrigin() throws Exception {
        incorrectAccessControlAllowOrigin("http://www.sourceforge.net");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(IE = { "4", "200", "null", "null", "null", "null" },
            DEFAULT = { "4", "200", "§§URL§§", "§§URL§§", "GET", "x-pingother" })
    public void preflight() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", "text/plain");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(IE = { "4", "200", "null", "null", "null", "null" },
            DEFAULT = { "4", "200", "§§URL§§", "§§URL§§", "GET", "x-pingother" })
    public void preflight_contentTypeWithCharset() throws Exception {
        doPreflightTestAllowedMethods("POST, GET, OPTIONS", "text/plain;charset=utf-8");
    }

    /**
     * Seems that "Access-Control-Allow-Methods" is not considered by FF.
     *
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(IE = { "4", "200", "null", "null", "null", "null" },
            DEFAULT = { "4", "200", "§§URL§§", "§§URL§§", "GET", "x-pingother" })
    public void preflight_incorrect_methods() throws Exception {
        doPreflightTestAllowedMethods(null, "text/plain");
    }

    private void doPreflightTestAllowedMethods(final String allowedMethods, final String contentType)
        throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT)); // url without trailing "/"

        final String html = "<html><head>\n"
            + "<script>\n"
            + "var xhr = " + XHRInstantiation_ + ";\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/preflight2';\n"
            + "    xhr.open('GET',  url, false);\n"
            + "    xhr.setRequestHeader('X-PINGOTHER', 'pingpong');\n"
            + "    xhr.setRequestHeader('Content-Type' , '" + contentType + "');"
            + "    xhr.send();\n"
            + "    alert(xhr.readyState);\n"
            + "    alert(xhr.status);\n"
            + "    alert(xhr.responseXML.firstChild.childNodes[0].firstChild.nodeValue);"
            + "    alert(xhr.responseXML.firstChild.childNodes[1].firstChild.nodeValue);"
            + "    alert(xhr.responseXML.firstChild.childNodes[2].firstChild.nodeValue);"
            + "    alert(xhr.responseXML.firstChild.childNodes[3].firstChild.nodeValue);"
            + "  } catch(e) { alert(e) }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "http://localhost:" + PORT;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = allowedMethods;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = "X-PINGOTHER";
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<String, Class<? extends Servlet>>();
        servlets2.put("/preflight2", PreflightServerServlet.class);
        startWebServer2(".", null, servlets2);

        loadPageWithAlerts2(html, new URL(getDefaultUrl(), "/preflight1"));
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
            options_origin_ = request.getHeader("Origin");
            options_method_ = request.getHeader("Access-Control-Request-Method");
            options_headers_ = request.getHeader("Access-Control-Request-Headers");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            if (ACCESS_CONTROL_ALLOW_ORIGIN_ != null) {
                response.setHeader("Access-Control-Allow-Origin", ACCESS_CONTROL_ALLOW_ORIGIN_);
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
            final Writer writer = response.getWriter();

            final String origin = request.getHeader("Origin");
            writer.write("<result>"
                + "<origin>" + origin + "</origin>"
                + "<options_origin>" + options_origin_ + "</options_origin>"
                + "<options_method>" + options_method_ + "</options_method>"
                + "<options_headers>" + options_headers_ + "</options_headers>"
                + "</result>");
        }
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(IE = { "4", "200" }, DEFAULT = { "exception", "4", "0" })
    public void preflight_incorrect_headers() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = "<html><head>\n"
                + "<script>\n"
                + "var xhr = " + XHRInstantiation_ + ";\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/preflight2';\n"
                + "    xhr.open('GET',  url, false);\n"
                + "    xhr.setRequestHeader('X-PINGOTHER', 'pingpong');\n"
                + "    xhr.send();\n"
                + "  } catch(e) { alert('exception') }\n"
                + "  alert(xhr.readyState);\n"
                + "  alert(xhr.status);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "http://localhost:" + PORT;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = null;
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<String, Class<? extends Servlet>>();
        servlets2.put("/preflight2", PreflightServerServlet.class);
        startWebServer2(".", null, servlets2);

        loadPageWithAlerts2(html, new URL(getDefaultUrl(), "/preflight1"));
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = { "4", "200", "options_headers", "x-ping,x-pong" },
            IE = { "4", "200", "options_headers", "null" })
    public void preflight_many_header_values() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = "<html><head>\n"
                + "<script>\n"
                + "var xhr = " + XHRInstantiation_ + ";\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/preflight2';\n"
                + "    xhr.open('GET',  url, false);\n"
                + "    xhr.setRequestHeader('X-PING', 'ping');\n"
                + "    xhr.setRequestHeader('X-PONG', 'pong');\n"
                + "    xhr.send();\n"
                + "  } catch(e) { alert('exception') }\n"
                + "  alert(xhr.readyState);\n"
                + "  alert(xhr.status);\n"
                + "  alert(xhr.responseXML.firstChild.childNodes[3].tagName);"
                + "  alert(xhr.responseXML.firstChild.childNodes[3].firstChild.nodeValue);"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "http://localhost:" + PORT;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = "X-PING, X-PONG";
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<String, Class<? extends Servlet>>();
        servlets2.put("/preflight2", PreflightServerServlet.class);
        startWebServer2(".", null, servlets2);

        loadPageWithAlerts2(html, new URL(getDefaultUrl(), "/preflight1"));
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = "false", IE8 = "undefined")
    public void withCredentials_defaultValue() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = "<html><head>\n"
                + "<script>\n"
                + "var xhr = " + XHRInstantiation_ + ";\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/withCredentials2';\n"
                + "    xhr.open('GET',  url, true);\n"
                + "    alert(xhr.withCredentials);\n"
                + "  } catch(e) { alert(e) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html, new URL(getDefaultUrl(), "/withCredentials1"));
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "ex: withCredentials=true", "ex: withCredentials=false" },
            FF17 = { "false", "false", "false", "false" },
            IE8 = { "undefined", "undefined", "true", "false" },
            IE10 = { "false", "false", "true", "false" })
    public void withCredentials_notSetableInSyncMode() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "var xhr = " + XHRInstantiation_ + ";\n"
                + "function test() {\n"
                + "  try {\n"
                + "    alert(xhr.withCredentials);\n"
                + "    xhr.open('GET',  '/foo.xml', false);\n"
                + "    alert(xhr.withCredentials);\n"

                + "    try {\n"
                + "      xhr.withCredentials = true;\n"
                + "      alert(xhr.withCredentials);\n"
                + "    } catch(e) { alert('ex: withCredentials=true') }\n"

                + "    try {\n"
                + "      xhr.withCredentials = false;\n"
                + "      alert(xhr.withCredentials);\n"
                + "    } catch(e) { alert('ex: withCredentials=false') }\n"
                + "  } catch(ex) { alert(ex) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "true", "ex: open" },
            IE8 = { "undefined", "false", "true", "open true" },
            IE10 = { "false", "ex: withCredentials=false", "ex: withCredentials=true", "open false" })
    public void withCredentials_openFailesInSyncMode() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "var xhr = " + XHRInstantiation_ + ";\n"
                + "function test() {\n"
                + "  try {\n"
                + "    alert(xhr.withCredentials);\n"

                + "    try {\n"
                + "      xhr.withCredentials = false;\n"
                + "      alert(xhr.withCredentials);\n"
                + "    } catch(e) { alert('ex: withCredentials=false') }\n"

                + "    try {\n"
                + "      xhr.withCredentials = true;\n"
                + "      alert(xhr.withCredentials);\n"
                + "    } catch(e) { alert('ex: withCredentials=true') }\n"

                + "    try {\n"
                + "      xhr.open('GET',  '/foo.xml', false);\n"
                + "      alert('open ' + xhr.withCredentials);\n"
                + "    } catch(e) { alert('ex: open') }\n"
                + "  } catch(ex) { alert(ex) }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "4", "0" },
            IE8 = { "1", "ex: status not available", "4", "200" },
            IE10 = { "1", "0", "4", "200" })
    public void withCredentials() throws Exception {
        testWithCredentials("*", "true");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "4", "200" },
            IE8 = { "1", "ex: status not available", "4", "200" })
    public void withCredentialsServer() throws Exception {
        testWithCredentials("http://localhost:" + PORT, "true");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "4", "0" },
            IE8 = { "1", "ex: status not available", "4", "200" },
            IE10 = { "1", "0", "4", "200" })
    public void withCredentialsServerSlashAtEnd() throws Exception {
        testWithCredentials("http://localhost:" + PORT + "/", "true");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "4", "0" },
            IE8 = { "1", "ex: status not available", "4", "200" },
            IE10 = { "1", "0", "4", "200" })
    public void withCredentials_no_header() throws Exception {
        testWithCredentials("*", null);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "4", "0" },
            IE8 = { "1", "ex: status not available", "4", "200" },
            IE10 = { "1", "0", "4", "200" })
    public void withCredentials_no_header_Server() throws Exception {
        testWithCredentials("http://localhost:" + PORT, null);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "4", "0" },
            IE8 = { "1", "ex: status not available", "4", "200" },
            IE10 = { "1", "0", "4", "200" })
    public void withCredentials_no_header_ServerSlashAtEnd() throws Exception {
        testWithCredentials("http://localhost:" + PORT + "/", null);
    }

    private void testWithCredentials(final String accessControlAllowOrigin,
            final String accessControlAllowCredentials) throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html = "<html><head>\n"
                + "<script>\n"
                + "var xhr = " + XHRInstantiation_ + ";\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/withCredentials2';\n"
                + "    xhr.open('GET',  url, true);\n"
                + "    xhr.withCredentials = true;\n"
                + "    xhr.onreadystatechange = onReadyStateChange;\n"
                + "    xhr.send();\n"
                + "  } catch(e) { alert(e) }\n"
                + "  alert(xhr.readyState);\n"
                + "  try {\n"
                + "    alert(xhr.status);\n"
                + "  } catch(e) { alert('ex: status not available') }\n"

                + "  function onReadyStateChange() {\n"
                + "    if (xhr.readyState == 4) {\n"
                + "      alert(xhr.readyState);\n"
                + "      alert(xhr.status);\n"
                + "    }\n"
                + "  }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        WithCredentialsServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = accessControlAllowOrigin;
        WithCredentialsServerServlet.ACCESS_CONTROL_ALLOW_CREDENTIALS_ = accessControlAllowCredentials;
        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<String, Class<? extends Servlet>>();
        servlets2.put("/withCredentials2", WithCredentialsServerServlet.class);
        startWebServer2(".", null, servlets2);

        loadPageWithAlerts2(html, new URL(getDefaultUrl(), "/withCredentials1"));
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
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
            String origin = request.getHeader("Origin");
            if (origin == null) {
                origin = "No Origin!";
            }
            response.getWriter().write("<origin>" + origin + "</origin>");
        }
    }

}
