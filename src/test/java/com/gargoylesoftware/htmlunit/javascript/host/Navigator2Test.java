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
package com.gargoylesoftware.htmlunit.javascript.host;

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
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link Navigator}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class Navigator2Test extends WebServerTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF10 = { "yes", "undefined" }, DEFAULT = { "undefined", "undefined" })
    //IE9 = { "undefined", "1" }
    public void doNotTrack() throws Exception {
        doNotTrack(true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF10 = { "unspecified", "undefined" }, DEFAULT = { "undefined", "undefined" })
    //IE9 = { "undefined", "0" }
    public void doNotTrack_disabled() throws Exception {
        doNotTrack(false);
    }

    private void doNotTrack(final boolean doNotTrack) throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/test", DoNotTrackServlet.class);
        startWebServer("./", new String[0], servlets);

        final WebClient client = getWebClient();
        if (doNotTrack) {
            client.getOptions().setDoNotTrackEnabled(true);
        }
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/test");

        assertEquals(getExpectedAlerts(), collectedAlerts);
        if (doNotTrack) {
            assertTrue(page.asText().contains("DNT : 1"));
        }
        else {
            assertTrue(page.asText().contains("DNT : null"));
        }
    }

    /**
     * Servlet for {@link #doNotTrack()}.
     */
    public static class DoNotTrackServlet extends HttpServlet {

        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            resp.setContentType("text/html");
            final Writer writer = resp.getWriter();
            final String dntHeader = req.getHeader("DNT");
            writer.write("<html><head><title>First</title>\n"
                        + "<script>\n"
                        + "function test() {\n"
                        + "  alert(navigator.doNotTrack);\n"
                        + "  alert(navigator.msDoNotTrack);\n"
                        + "}\n"
                        + "</script>\n"
                        + "</head><body onload='test()'>DNT : " + dntHeader + "</body>\n"
                        + "</html>");
            writer.close();
        }
    }
}
