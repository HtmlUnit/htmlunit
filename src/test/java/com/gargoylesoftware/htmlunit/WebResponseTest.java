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

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link WebResponse}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WebResponseTest extends WebServerTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void encodingCharsetUtf8() throws Exception {
        final String title = "\u6211\u662F\u6211\u7684FOCUS";
        final String content =
            "<html><head>\n"
            + "<title>" + title + "</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, content.getBytes(UTF_8), 200, "OK", "text/html; charset=UTF-8", null);
        client.setWebConnection(webConnection);
        final WebRequest request = new WebRequest(URL_FIRST);
        request.setCharset(UTF_8);
        final HtmlPage page = client.getPage(request);
        assertEquals(title, page.getTitleText());

        assertEquals(content, page.getWebResponse().getContentAsString());
        assertEquals(content, page.getWebResponse().getContentAsString(UTF_8));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void quotedCharset() throws Exception {
        final String xml
            = "<books id='myId'>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        final List<String> collectedAlerts = new ArrayList<>();
        final WebClient client = getWebClient();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, xml, HttpStatus.SC_OK, "OK", "text/xml; charset=\"ISO-8859-1\"", null);
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);
    }

    /**
     * Test that extracting charset from Content-Type header is forgiving.
     * @throws Exception if the test fails
     */
    @Test
    public void illegalCharset() throws Exception {
        illegalCharset("text/html; text/html; charset=ISO-8859-1;", ISO_8859_1);
        illegalCharset("text/html; charset=UTF-8; charset=UTF-8", UTF_8);
        illegalCharset("text/html; charset=#sda+s", ISO_8859_1);
        illegalCharset("text/html; charset=UnknownCharset", ISO_8859_1);
    }

    private void illegalCharset(final String cntTypeHeader, final Charset expectedCharset) throws Exception {
        final MockWebConnection conn = new MockWebConnection();
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_TYPE, cntTypeHeader));
        conn.setDefaultResponse("<html/>", 200, "OK", MimeType.TEXT_HTML, headers);
        final WebClient webClient = getWebClient();
        webClient.setWebConnection(conn);

        final Page page = webClient.getPage(URL_FIRST);
        assertEquals(expectedCharset, page.getWebResponse().getContentCharset());
        assertEquals(cntTypeHeader, page.getWebResponse().getResponseHeaderValue(HttpHeader.CONTENT_TYPE));
        assertEquals("<html/>", page.getWebResponse().getContentAsString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void responseHeaders() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test", ResponseHeadersServlet.class);
        startWebServer("./", null, servlets);
        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(URL_FIRST + "test");
        assertEquals("some_value", page.getWebResponse().getResponseHeaderValue("some_header"));
    }

    /**
     * Servlet for {@link #responseHeaders()}.
     */
    public static class ResponseHeadersServlet extends HttpServlet {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType(MimeType.TEXT_HTML);
            response.setHeader("some_header", "some_value");
            final Writer writer = response.getWriter();
            writer.write("<html/>");
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getContentAsStringIllegalCharset() throws Exception {
        final MockWebConnection conn = new MockWebConnection();
        final List<NameValuePair> headers = new ArrayList<>();
        conn.setDefaultResponse("<html/>", 200, "OK", MimeType.TEXT_HTML, headers);
        final WebClient webClient = getWebClient();
        webClient.setWebConnection(conn);

        final Page page = webClient.getPage(URL_FIRST);
        final WebResponse webResponse = page.getWebResponse();
        assertEquals("<html/>", webResponse.getContentAsString(webResponse.getContentCharset()));
    }

    /**
     * Servlet for {@link #binaryResponseHeaders()}.
     */
    public static class BinaryResponseHeadersServlet extends HttpServlet {
        private static final String RESPONSE = "<html><head><title>Foo</title></head><body>This is foo!</body></html>";

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            final byte[] bytes = RESPONSE.getBytes(UTF_8);
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final GZIPOutputStream gout = new GZIPOutputStream(bos);
            gout.write(bytes);
            gout.finish();

            final byte[] encoded = bos.toByteArray();

            response.setContentType(MimeType.TEXT_HTML);
            response.setCharacterEncoding(UTF_8.name());
            response.setStatus(200);
            response.setContentLength(encoded.length);
            response.setHeader("Content-Encoding", "gzip");

            final OutputStream rout = response.getOutputStream();
            rout.write(encoded);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void binaryResponseHeaders() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test", BinaryResponseHeadersServlet.class);
        startWebServer("./", null, servlets);

        final HtmlPage page = getWebClient().getPage(URL_FIRST + "test");
        assertEquals(BinaryResponseHeadersServlet.RESPONSE,
                page.getWebResponse().getContentAsString(UTF_8));

        assertEquals("gzip", page.getWebResponse().getResponseHeaderValue("Content-Encoding"));
        assertEquals("73", page.getWebResponse().getResponseHeaderValue(HttpHeader.CONTENT_LENGTH));
    }

    /**
     * Stop the WebServer.
     * @throws Exception if it fails
     */
    @After
    public void stopServer() throws Exception {
        stopWebServer();
    }
}
