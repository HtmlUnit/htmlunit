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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;

/**
 * Tests for {@link HtmlPage}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author David K. Taylor
 * @author Andreas Hangler
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlPage4Test extends WebServerTestCase {

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void refresh() throws Exception {
        final Map<String, Class<? extends Servlet>> map = new HashMap<String, Class<? extends Servlet>>();
        map.put("/one.html", RefreshServlet.class);
        map.put("/two.html", RefreshServlet.class);
        startWebServer(".", null, map);
        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/one.html");
        final HtmlSubmitInput submit = page.getHtmlElementById("myButton");
        final HtmlPage secondPage = submit.click();
        assertEquals("0\nPOST\nsome_name some_value\n", secondPage.getWebResponse().getContentAsString());
        final HtmlPage secondPage2 = (HtmlPage) secondPage.refresh();
        assertEquals("1\nPOST\nsome_name some_value\n", secondPage2.getWebResponse().getContentAsString());
    }

    /**
     * Refresh servlet.
     */
    public static class RefreshServlet extends HttpServlet {

        private int counter_;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            final Writer writer = resp.getWriter();
            resp.setContentType("text/html");
            final String response = "<html>\n"
                + "<body>\n"
                + "  <form action='two.html' method='post'>\n"
                + "  <input type='hidden' name='some_name' value='some_value'>\n"
                + "  <input id='myButton' type='submit'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";
            writer.write(response);
            writer.close();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            resp.setContentType("text/html");
            final StringBuilder builder = new StringBuilder();
            builder.append(counter_++).append("\n");
            builder.append(req.getMethod()).append("\n");
            for (final Enumeration<?> en = req.getParameterNames(); en.hasMoreElements();) {
                final String name = (String) en.nextElement();
                final String value = req.getParameter(name);
                builder.append(name).append(' ').append(value).append('\n');
            }
            resp.getWriter().write(builder.toString());
        }
    }
}
