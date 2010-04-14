/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.ChunkedOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link BigContentPage}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class BigContentPageTest extends WebServerTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void bigContent() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/big", BigContentServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();

        final Page page = client.getPage("http://localhost:" + PORT + "/big");
        assertTrue(page instanceof BigContentPage);
    }

    /**
     * Servlet for {@link #bigContent()}.
     */
    public static class BigContentServlet extends HttpServlet {

        private static final long serialVersionUID = -7979736717576809489L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            final int length = 60 * 1024 * 1024;
            response.setContentLength(length);
            final byte[] buffer = new byte[1024];
            final OutputStream out = response.getOutputStream();
            for (int i = length / buffer.length; i >= 0; i--) {
                out.write(buffer);
            }
            out.close();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void chunkedBigContent() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/bigChunked", ChunkedBigContentServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();

        final Page page = client.getPage("http://localhost:" + PORT + "/bigChunked");
        assertTrue(page instanceof BigContentPage);
    }

    /**
     * Servlet for {@link #chunkedBigContent()}.
     */
    public static class ChunkedBigContentServlet extends HttpServlet {

        private static final long serialVersionUID = 8341425725771441517L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            final int length = 60 * 1024 * 1024;
            final byte[] buffer = new byte[1024];
            final OutputStream out = new ChunkedOutputStream(response.getOutputStream());
            for (int i = length / buffer.length; i >= 0; i--) {
                out.write(buffer);
            }
            out.close();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void chunked() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/chunked", ChunkedServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();

        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/chunked");
        assertTrue(page.asText().startsWith("ABCDEF"));
    }

    /**
     * Servlet for {@link #chunked()}.
     */
    public static class ChunkedServlet extends HttpServlet {

        private static final long serialVersionUID = 1454574979267029963L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            final OutputStream out = new ChunkedOutputStream(response.getOutputStream(), 5);
            for (char ch = 'A'; ch <= 'Z'; ch++) {
                out.write(ch);
            }
            out.close();
        }
    }
}
