/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for binary content.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class BinaryPageTest extends WebServerTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void binary() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/big", BinaryServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();

        final Page page = client.getPage(URL_FIRST + "big");
        assertTrue(page instanceof UnexpectedPage);
    }

    /**
     * Servlet for {@link #binary()}.
     */
    public static class BinaryServlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            final int length = 1000;
            response.setContentLength(length);
            final byte[] buffer = new byte[1024];
            try (OutputStream out = response.getOutputStream()) {
                for (int i = length / buffer.length; i >= 0; i--) {
                    out.write(buffer);
                }
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void chunkedBigContent() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/bigChunked", ChunkedBigContentServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();

        final Page page = client.getPage(URL_FIRST + "bigChunked");
        assertTrue(page instanceof UnexpectedPage);
    }

    /**
     * Servlet for {@link #chunkedBigContent()}.
     */
    public static class ChunkedBigContentServlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setHeader("Transfer-Encoding", "chunked");
            final int length = 60 * 1024 * 1024;
            final byte[] buffer = new byte[1024];
            try (OutputStream out = response.getOutputStream()) {
                for (int i = length / buffer.length; i >= 0; i--) {
                    out.write(buffer);
                }
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void chunked() throws Exception {
        final String response = "HTTP/1.1 200 OK\r\n"
            + "Transfer-Encoding: chunked\r\n\r\n"
            + "5\r\n"
            + "ABCDE\r\n"
            + "5\r\n"
            + "FGHIJ\r\n"
            + "5\r\n"
            + "KLMNO\r\n"
            + "5\r\n"
            + "PQRST\r\n"
            + "5\r\n"
            + "UVWXY\r\n"
            + "1\r\n"
            + "Z\r\n"
            + "0\r\n\r\n";

        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, null)) {
            final WebClient client = getWebClient();

            final TextPage page = client.getPage("http://localhost:" + primitiveWebServer.getPort() + "/" + "chunked");
            assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", page.getContent());
        }
    }
}
