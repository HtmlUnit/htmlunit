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
    @Alerts(IE = {"4", "200", "No Origin!" }, DEFAULT = { "4", "200", "§§URL§§" })
    public void originHeader() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));
        final Map<String, Class<? extends Servlet>> servlets1 = new HashMap<String, Class<? extends Servlet>>();
        servlets1.put("/simple1", OriginHeader1Servlet.class);
        startWebServer(".", null, servlets1);

        final Map<String, Class<? extends Servlet>> servlets2 = new HashMap<String, Class<? extends Servlet>>();
        servlets2.put("/simple2", OriginHeader2Servlet.class);
        startWebServer2(".", null, servlets2);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/simple1");
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Servlet for {@link #originHeader()}.
     */
    public static class OriginHeader1Servlet extends ServletContentWrapper {
        /** Constructor. */
        public OriginHeader1Servlet() {
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
                    + "    alert(xhr.responseXML.childNodes[0].firstChild.nodeValue);"
                    + "  } catch(e) { alert(e) }\n"
                    + "}\n"
                    + "</script>\n"
                    + "</head>\n"
                    + "<body onload='test()'></body></html>"));
        }
    }

    /**
     * Servlet for {@link #originHeader()}.
     */
    public static class OriginHeader2Servlet extends HttpServlet {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setHeader("Access-Control-Allow-Origin", "*");
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
