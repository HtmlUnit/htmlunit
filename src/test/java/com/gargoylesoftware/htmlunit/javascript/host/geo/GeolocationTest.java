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
package com.gargoylesoftware.htmlunit.javascript.host.geo;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.util.ServletContentWrapper;

/**
 * Tests for {@link Geolocation}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class GeolocationTest extends WebServerTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "12.34567891 98.76543211")
    @NotYetImplemented //since it runs on Windows only (for now)
    public void getCurrentPosition() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/test", GetCurrentPositionTestServlet.class);
        servlets.put("/browserLocation", BrowserLocationServlet.class);
        startWebServer("./", new String[0], servlets);

        Geolocation.setProviderUrl("http://localhost:" + PORT + "/browserLocation");
        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        client.getPage("http://localhost:" + PORT + "/test");
        client.waitForBackgroundJavaScript(4000);

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Helper class for {@link #getCurrentPosition()}.
     */
    public static class GetCurrentPositionTestServlet extends ServletContentWrapper {
        /**
         * Constructor.
         */
        public GetCurrentPositionTestServlet() {
            super("<html><head><script>\n"
                    + "    function test() {\n"
                    + "        if (navigator.geolocation) {\n"
                    + "            navigator.geolocation.getCurrentPosition(\n"
                    + "            function(position) {\n"
                    + "                alert (position.coords.latitude + ' ' + position.coords.longitude);\n"
                    + "            },\n"
                    + "            function(error) {\n"
                    + "                switch (error.code) {\n"
                    + "                    case error.TIMEOUT:\n"
                    + "                        alert('Timeout');\n"
                    + "                        break;\n"
                    + "                    case error.POSITION_UNAVAILABLE:\n"
                    + "                        alert('Position unavailable');\n"
                    + "                        break;\n"
                    + "                    case error.PERMISSION_DENIED:\n"
                    + "                        alert('Permission denied');\n"
                    + "                        break;\n"
                    + "                    case error.UNKNOWN_ERROR:\n"
                    + "                        alert('Unknown error');\n"
                    + "                        break;\n"
                    + "                }\n"
                    + "            });\n"
                    + "        }\n"
                    + "    }\n"
                    + "</script></head><body onload='test()'>\n"
                    + "</body></html");
        }
    }

    /**
     * Helper class for {@link #getCurrentPosition()}.
     */
    public static class BrowserLocationServlet extends HttpServlet {

        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            resp.setContentType("application/json");
            final Writer writer = resp.getWriter();
            writer.write("{\n"
                    + "                \"accuracy\" : 12.3,\n"
                    + "                \"location\" : {\n"
                    + "                   \"lat\" : 12.34567891,\n"
                    + "                   \"lng\" : 98.76543211\n"
                    + "                },\n"
                    + "                \"status\" : \"OK\"\n"
                    + "             }");
            writer.close();
        }
    }
}
