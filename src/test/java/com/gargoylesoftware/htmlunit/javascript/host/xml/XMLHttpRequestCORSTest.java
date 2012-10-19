/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.ServletContentWrapper;

/**
 * Tests for Cross-Origin Resource Sharing for {@link XMLHttpRequest}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class XMLHttpRequestCORSTest extends WebDriverTestCase {

    private static String XHRInstantiation_ = "(window.XMLHttpRequest ? "
        + "new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'))";

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(IE = { "4", "200", "No Origin!" }, DEFAULT = { "4", "200", "§§URL§§" })
    public void simple() throws Exception {
        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "*";
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));
        final Map<String, Class<? extends Servlet>> servlets1 = new HashMap<String, Class<? extends Servlet>>();
        servlets1.put("/simple1", SimpleServlet.class);
        startWebServer(".", null, servlets1);

        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<String, Class<? extends Servlet>>();
        servlets2.put("/simple2", SimpleServerServlet.class);
        startWebServer2(".", null, servlets2);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/simple1");
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Servlet for {@link #simple()}.
     */
    public static class SimpleServlet extends ServletContentWrapper {
        /** Constructor. */
        public SimpleServlet() {
            super(getModifiedContent("<html><head>\n"
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
                    + "<body onload='test()'></body></html>"));
        }
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
        SimpleServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = header;
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));
        final Map<String, Class<? extends Servlet>> servlets1 = new HashMap<String, Class<? extends Servlet>>();
        servlets1.put("/simple1", UnauthorizedSimpleServlet.class);
        startWebServer(".", null, servlets1);

        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<String, Class<? extends Servlet>>();
        servlets2.put("/simple2", SimpleServerServlet.class);
        startWebServer2(".", null, servlets2);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/simple1");
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Servlet for {@link #noAccessControlAllowOrigin()}.
     */
    public static class UnauthorizedSimpleServlet extends ServletContentWrapper {
        /** Constructor. */
        public UnauthorizedSimpleServlet() {
            super(getModifiedContent("<html><head>\n"
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
                    + "<body onload='test()'></body></html>"));
        }
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
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "http://localhost:" + PORT;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = "X-PINGOTHER";
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));
        final Map<String, Class<? extends Servlet>> servlets1 = new HashMap<String, Class<? extends Servlet>>();
        servlets1.put("/preflight1", PreflightServlet.class);
        startWebServer(".", null, servlets1);

        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<String, Class<? extends Servlet>>();
        servlets2.put("/preflight2", PreflightServerServlet.class);
        startWebServer2(".", null, servlets2);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/preflight1");
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
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
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "http://localhost:" + PORT;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = null;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = "X-PINGOTHER";
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));
        final Map<String, Class<? extends Servlet>> servlets1 = new HashMap<String, Class<? extends Servlet>>();
        servlets1.put("/preflight1", PreflightServlet.class);
        startWebServer(".", null, servlets1);

        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<String, Class<? extends Servlet>>();
        servlets2.put("/preflight2", PreflightServerServlet.class);
        startWebServer2(".", null, servlets2);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/preflight1");
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Servlet for {@link #preflight()}.
     */
    public static class PreflightServlet extends ServletContentWrapper {
        /** Constructor. */
        public PreflightServlet() {
            super(getModifiedContent("<html><head>\n"
                    + "<script>\n"
                    + "var xhr = " + XHRInstantiation_ + ";\n"
                    + "function test() {\n"
                    + "  try {\n"
                    + "    var url = 'http://' + window.location.hostname + ':" + PORT2 + "/preflight2';\n"
                    + "    xhr.open('GET',  url, false);\n"
                    + "    xhr.setRequestHeader('X-PINGOTHER', 'pingpong');\n"
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
                    + "<body onload='test()'></body></html>"));
        }
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
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_ORIGIN_ = "http://localhost:" + PORT;
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_METHODS_ = "POST, GET, OPTIONS";
        PreflightServerServlet.ACCESS_CONTROL_ALLOW_HEADERS_ = null;
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));
        final Map<String, Class<? extends Servlet>> servlets1 = new HashMap<String, Class<? extends Servlet>>();
        servlets1.put("/preflight1", UnauthorizedPreflightServlet.class);
        startWebServer(".", null, servlets1);

        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<String, Class<? extends Servlet>>();
        servlets2.put("/preflight2", PreflightServerServlet.class);
        startWebServer2(".", null, servlets2);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/preflight1");
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Servlet for unauthorized preflight requests.
     */
    public static class UnauthorizedPreflightServlet extends ServletContentWrapper {
        /** Constructor. */
        public UnauthorizedPreflightServlet() {
            super(getModifiedContent("<html><head>\n"
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
                    + "<body onload='test()'></body></html>"));
        }
    }

}
