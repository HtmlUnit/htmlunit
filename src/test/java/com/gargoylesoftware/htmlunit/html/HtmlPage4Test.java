/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import java.io.File;
import java.io.FilenameFilter;
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
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.util.ServletContentWrapper;

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

    /**
     * @exception Exception if an error occurs
     */
    @Test
    @Alerts("hello")
    public void bigJavaScript() throws Exception {
        final StringBuilder html
            = new StringBuilder("<html><head>\n"
                    + "<script src='two.js'></script>\n"
                    + "<link rel='stylesheet' type='text/css' href='three.css'/>\n"
                    + "</head>\n"
                    + "<body onload='test()'></body></html>");

        final StringBuilder javaScript = new StringBuilder("function test() {\n"
                + "alert('hello');\n"
                + "}");

        final StringBuilder css = new StringBuilder("body {color: blue}");

        final long maxInMemory = getWebClient().getOptions().getMaxInMemory();

        for (int i = 0; i < maxInMemory; i++) {
            html.append(' ');
            javaScript.append(' ');
            css.append(' ');
        }

        BigJavaScriptServlet1.CONTENT_ = html.toString();
        BigJavaScriptServlet2.CONTENT_ = javaScript.toString();
        BigJavaScriptServlet3.CONTENT_ = css.toString();

        final int initialTempFiles = getTempFiles();
        final Map<String, Class<? extends Servlet>> map = new HashMap<String, Class<? extends Servlet>>();
        map.put("/one.html", BigJavaScriptServlet1.class);
        map.put("/two.js", BigJavaScriptServlet2.class);
        map.put("/three.css", BigJavaScriptServlet3.class);
        startWebServer(".", null, map);
        final WebClient client = getWebClient();
        final CollectingAlertHandler alertHandler = new CollectingAlertHandler();
        client.setAlertHandler(alertHandler);
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/one.html");
        ((HTMLBodyElement) page.getBody().getScriptObject()).getCurrentStyle();

        assertEquals(getExpectedAlerts(), alertHandler.getCollectedAlerts());
        assertEquals(initialTempFiles + 1, getTempFiles());
        client.closeAllWindows();
        assertEquals(initialTempFiles, getTempFiles());
    }

    /**
     *  The HTML servlet for {@link #bigJavaScript()}.
     */
    public static class BigJavaScriptServlet1 extends ServletContentWrapper {
        private static String CONTENT_;
        /** The constructor. */
        public BigJavaScriptServlet1() {
            super(CONTENT_);
        }
    }

    /**
     *  The JavaScript servlet for {@link #bigJavaScript()}.
     */
    public static class BigJavaScriptServlet2 extends ServletContentWrapper {
        private static String CONTENT_;
        /** The constructor. */
        public BigJavaScriptServlet2() {
            super(CONTENT_);
        }
    }

    /**
     *  The CSS servlet for {@link #bigJavaScript()}.
     */
    public static class BigJavaScriptServlet3 extends ServletContentWrapper {
        private static String CONTENT_;
        /** The constructor. */
        public BigJavaScriptServlet3() {
            super(CONTENT_);
        }
    }

    private int getTempFiles() {
        final File file = new File(System.getProperty("java.io.tmpdir"));
        final String[] list = file.list(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.startsWith("htmlunit");
            }
        });
        return list.length;
    }
}
