/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.html;

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

import org.htmlunit.CollectingAlertHandler;
import org.htmlunit.WebClient;
import org.htmlunit.WebServerTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.ServletContentWrapper;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlPage}.
 *
 * @author Mike Bowler
 * @author Noboru Sinohara
 * @author David K. Taylor
 * @author Andreas Hangler
 * @author Christian Sell
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlPage4Test extends WebServerTestCase {

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void refresh() throws Exception {
        final Map<String, Class<? extends Servlet>> map = new HashMap<>();
        map.put("/one.html", RefreshServlet.class);
        map.put("/two.html", RefreshServlet.class);
        startWebServer(".", null, map);
        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(URL_FIRST + "one.html");
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
            resp.setContentType(MimeType.TEXT_HTML);
            final String response = DOCTYPE_HTML
                    + "<html>\n"
                    + "<body>\n"
                    + "  <form action='two.html' method='post'>\n"
                    + "  <input type='hidden' name='some_name' value='some_value'>\n"
                    + "  <input id='myButton' type='submit'>\n"
                    + "  </form>\n"
                    + "</body>\n"
                    + "</html>";
            writer.write(response);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            resp.setContentType(MimeType.TEXT_HTML);
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
        final StringBuilder html = new StringBuilder(DOCTYPE_HTML
                + "<html><head>\n"
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
        final Map<String, Class<? extends Servlet>> map = new HashMap<>();
        map.put("/one.html", BigJavaScriptServlet1.class);
        map.put("/two.js", BigJavaScriptServlet2.class);
        map.put("/three.css", BigJavaScriptServlet3.class);
        startWebServer(".", null, map);
        try (WebClient client = getWebClient()) {
            final CollectingAlertHandler alertHandler = new CollectingAlertHandler();
            client.setAlertHandler(alertHandler);
            final HtmlPage page = client.getPage(URL_FIRST + "one.html");
            page.getEnclosingWindow().getComputedStyle(page.getBody(), null);

            assertEquals(getExpectedAlerts(), alertHandler.getCollectedAlerts());
            assertEquals(initialTempFiles + 1, getTempFiles());
        }
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

    private static int getTempFiles() {
        final File file = new File(System.getProperty("java.io.tmpdir"));
        final String[] list = file.list(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.startsWith("htmlunit");
            }
        });
        if (list == null) {
            return 0;
        }
        return list.length;
    }
}
