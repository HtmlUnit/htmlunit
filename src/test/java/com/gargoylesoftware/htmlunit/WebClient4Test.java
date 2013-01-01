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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Writer;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link WebClient} that run with BrowserRunner.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class WebClient4Test extends WebServerTestCase {

    /**
     * Verifies that a WebClient can be serialized and deserialized after it has been used.
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization_afterUse() throws Exception {
        startWebServer("./");

        final WebClient client = getWebClient();
        TextPage textPage = client.getPage("http://localhost:" + PORT + "/LICENSE.txt");
        assertTrue(textPage.getContent().contains("Gargoyle Software"));

        final WebClient copy = clone(client);
        assertNotNull(copy);

        final WebWindow window = copy.getCurrentWindow();
        assertNotNull(window);

        final WebWindow topWindow = window.getTopWindow();
        assertNotNull(topWindow);

        final Page page = topWindow.getEnclosedPage();
        assertNotNull(page);

        final WebResponse response = page.getWebResponse();
        assertNotNull(response);

        final String content = response.getContentAsString();
        assertNotNull(content);
        assertTrue(content.contains("Gargoyle Software"));

        textPage = copy.getPage("http://localhost:" + PORT + "/LICENSE.txt");
        assertTrue(textPage.getContent().contains("Gargoyle Software"));

        copy.closeAllWindows();
    }

    /**
     * Verifies that a redirect limit kicks in even if the redirects aren't for the same URL
     * and don't use the same redirect HTTP status code (see bug 2915453).
     * @throws Exception if an error occurs
     */
    @Test
    public void redirectInfinite303And307() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put(RedirectServlet307.URL, RedirectServlet307.class);
        servlets.put(RedirectServlet303.URL, RedirectServlet303.class);
        startWebServer("./", new String[0], servlets);

        final WebClient client = getWebClient();

        try {
            client.getPage("http://localhost:" + PORT + RedirectServlet307.URL);
        }
        catch (final Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("Too much redirect"));
        }
    }

    /**
     * Helper class for {@link #redirectInfinite303And307}.
     */
    public static class RedirectServlet extends HttpServlet {
        private int count_;
        private int status_;
        private String location_;
        /**
         * Creates a new instance.
         * @param status the HTTP status to return
         * @param location the location to redirect to
         */
        public RedirectServlet(final int status, final String location) {
            count_ = 0;
            status_ = status;
            location_ = location;
        }
        /** {@inheritDoc} */
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            count_++;
            resp.setStatus(status_);
            resp.setHeader("Location", location_);
            resp.getWriter().write(status_ + " " + req.getContextPath() + " " + count_);
        }
    }

    /**
     * Helper class for {@link #redirectInfinite303And307}.
     */
    public static class RedirectServlet303 extends RedirectServlet {
        static final String URL = "/test";
        /** Creates a new instance. */
        public RedirectServlet303() {
            super(303, RedirectServlet307.URL);
        }
    }

    /**
     * Helper class for {@link #redirectInfinite303And307}.
     */
    public static class RedirectServlet307 extends RedirectServlet {
        static final String URL = "/test2";
        /** Creates a new instance. */
        public RedirectServlet307() {
            super(307, RedirectServlet303.URL);
        }
    }

    /**
     * Regression test for bug 2903223: body download time was not taken into account
     * in {@link WebResponse#getLoadTime()}.
     * @throws Exception if an error occurs
     */
    @Test
    public void bodyDowloadTime() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/*", ServeBodySlowlyServlet.class);
        startWebServer("./", new String[0], servlets);

        final Page page = getWebClient().getPage(getDefaultUrl());
        final long loadTime = page.getWebResponse().getLoadTime();
        assertTrue("Load time: " + loadTime + ", last request time: " + ServeBodySlowlyServlet.LastRequestTime_,
            loadTime >= ServeBodySlowlyServlet.LastRequestTime_);
    }

    /**
     * Helper class for {@link #bodyDowloadTime}.
     */
    public static class ServeBodySlowlyServlet extends HttpServlet {
        private static volatile long LastRequestTime_ = -1;
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            final long before = System.currentTimeMillis();
            final Writer writer = resp.getWriter();
            writeSomeContent(writer); // some content quickly
            writer.flush();
            try {
                Thread.sleep(500);
            }
            catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
            writeSomeContent(writer); // and some content later
            LastRequestTime_ = System.currentTimeMillis() - before;
        }

        private void writeSomeContent(final Writer writer) throws IOException {
            for (int i = 0; i < 1000; ++i) {
                writer.append((char) ('a' + (i % 26)));
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testUseProxy() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/test", UseProxyHeaderServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/test");
        assertEquals("Going anywhere?", page.asText());
    }

    /**
     * Servlet for {@link #testUseProxy()}.
     */
    public static class UseProxyHeaderServlet extends HttpServlet {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setStatus(HttpServletResponse.SC_USE_PROXY);
            //Won't matter!
            //response.setHeader("Location", "http://www.google.com");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            writer.write("<html><body>Going anywhere?</body></html>");
            writer.close();
        }
    }

    /**
     * Regression test for bug 2803378: GET or POST to a URL that returns HTTP 204 (No Content).
     * @throws Exception if an error occurs
     */
    @Test
    public void testNoContent() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/test1", NoContentServlet1.class);
        servlets.put("/test2", NoContentServlet2.class);
        startWebServer("./", null, servlets);
        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/test1");
        final HtmlPage page2 = page.getHtmlElementById("submit").click();
        assertEquals(page, page2);
    }

    /**
     * First servlet for {@link #testNoContent()}.
     */
    public static class NoContentServlet1 extends HttpServlet {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
            res.setContentType("text/html");
            final Writer writer = res.getWriter();
            writer.write("<html><body><form action='test2'>"
                + "<input id='submit' type='submit' value='submit'></input>"
                + "</form></body></html>");
            writer.close();
        }
    }

    /**
     * Second servlet for {@link #testNoContent()}.
     */
    public static class NoContentServlet2 extends HttpServlet {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse res) {
            res.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    /**
     * Regression test for bug 2821888: HTTP 304 (Not Modified) was being treated as a redirect. Note that a 304
     * response doesn't really make sense because we're not sending any If-Modified-Since headers, but we want to
     * at least make sure that we're not throwing exceptions when we receive one of these responses.
     * @throws Exception if an error occurs
     */
    @Test
    public void testNotModified() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/test", NotModifiedServlet.class);
        startWebServer("./", null, servlets);
        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/test");
        final TextPage page2 = client.getPage("http://localhost:" + PORT + "/test");
        assertNotNull(page);
        assertNotNull(page2);
    }

    /**
     * Servlet for {@link #testNotModified()}.
     */
    public static class NotModifiedServlet extends HttpServlet {
        private boolean first_ = true;
        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
            if (first_) {
                first_ = false;
                res.setContentType("text/html");
                final Writer writer = res.getWriter();
                writer.write("<html><body>foo</body></html>");
                writer.close();
            }
            else {
                res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            }
        }
    }

    /**
     * .
     * @throws Exception if an error occurs
     */
    @Test
    public void testTimeout() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/*", DelayDeliverServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();
        client.getOptions().setTimeout(500);

        try {
            client.getPage(getDefaultUrl());
            fail("timeout expected!");
        }
        catch (final SocketTimeoutException e) {
            // as expected
        }

        // now configure higher timeout allowing to get the page
        client.getOptions().setTimeout(5000);
        client.getPage(getDefaultUrl());
    }

    /**
     * Servlet for {@link #testTimeout()}.
     */
    public static class DelayDeliverServlet extends HttpServlet {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
            try {
                Thread.sleep(1000);
            }
            catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.setContentType("text/html");
            final Writer writer = res.getWriter();
            writer.write("<html><head><title>hello</title></head><body>foo</body></html>");
            writer.close();
        }
    }
}
