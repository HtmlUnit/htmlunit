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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link WebResponseData}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WebResponseDataTest extends WebServerTestCase {

    private static final String GZIPPED_FILE = "testfiles/test.html.gz";
    private static final String BROTLI_FILE = "testfiles/test.html.brotli";

    /**
     * Tests that gzipped content is handled correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void gZippedContent() throws Exception {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream(GZIPPED_FILE);
        final byte[] zippedContent = IOUtils.toByteArray(stream);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Encoding", "gzip"));

        final WebResponseData data = new WebResponseData(zippedContent, HttpStatus.SC_OK, "OK", headers);
        final String body = new String(data.getBody(), UTF_8);
        assertTrue(StringUtils.contains(body, "Test"));

        final WebResponse response = new WebResponse(data, new URL("http://test.com"), HttpMethod.GET, 1000);
        assertEquals(body, response.getContentAsString(UTF_8));
    }

    /**
     * Tests that empty gzipped content is handled correctly (bug 3566999).
     * @throws Exception if the test fails
     */
    @Test
    public void emptyGZippedContent() throws Exception {
        testEmptyGZippedContent(HttpStatus.SC_OK, 0, null);
    }

    /**
     * Tests that empty gzipped content is handled correctly (bug #1510).
     * @throws Exception if the test fails
     */
    @Test
    public void contentLengthIsZero() throws Exception {
        testEmptyGZippedContent(HttpStatus.SC_OK, 0, MimeType.TEXT_HTML);
    }

    /**
     * Tests that empty gzipped content is handled correctly (bug #1510).
     * @throws Exception if the test fails
     */
    @Test
    public void contentLengthIsMissing() throws Exception {
        testEmptyGZippedContent(HttpStatus.SC_NO_CONTENT, -1, null);
    }

    /**
     * Tests that gzipped content is handled correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void htmlEncodingGzipOnlyTextHtml() throws Exception {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream(GZIPPED_FILE);
        final byte[] zippedContent = IOUtils.toByteArray(stream);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Encoding", "gzip-only-text/html"));
        headers.add(new NameValuePair("content-type", MimeType.TEXT_HTML));

        final WebResponseData data = new WebResponseData(zippedContent, HttpStatus.SC_OK, "OK", headers);
        final String body = new String(data.getBody(), UTF_8);
        assertTrue(StringUtils.contains(body, "Test"));

        final WebResponse response = new WebResponse(data, new URL("http://test.com"), HttpMethod.GET, 1000);
        assertEquals(body, response.getContentAsString(UTF_8));
    }

    /**
     * Tests that gzipped content is handled correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void binaryEncodingGzipOnlyTextHtml() throws Exception {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-png.img");
        final byte[] zippedContent = IOUtils.toByteArray(stream);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Encoding", "gzip-only-text/html"));
        headers.add(new NameValuePair("content-type", "image/png"));

        final WebResponseData data = new WebResponseData(zippedContent, HttpStatus.SC_OK, "OK", headers);
        final byte[] bytes = IOUtils.toByteArray(data.getInputStream());
        assertEquals(128, bytes.length);
        assertEquals(137 - 256, bytes[0]);
        assertEquals(80, bytes[1]);
        assertEquals(78, bytes[2]);

        final WebResponse response = new WebResponse(data, new URL("http://test.com"), HttpMethod.GET, 1000);
        assertEquals(new String(bytes, UTF_8), response.getContentAsString(UTF_8));
    }

    /**
     * Tests that gzipped content is handled correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void binaryEncodingNoGzip() throws Exception {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("testfiles/xhtml.html");
        final byte[] zippedContent = IOUtils.toByteArray(stream);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Encoding", "no-gzip"));
        headers.add(new NameValuePair("content-type", MimeType.TEXT_HTML));

        final WebResponseData data = new WebResponseData(zippedContent, HttpStatus.SC_OK, "OK", headers);
        final String body = new String(data.getBody(), UTF_8);
        assertTrue(StringUtils.contains(body, "Test"));

        final WebResponse response = new WebResponse(data, new URL("http://test.com"), HttpMethod.GET, 1000);
        assertEquals(body, response.getContentAsString(UTF_8));
    }

    private static void testEmptyGZippedContent(final int statusCode, final int contentLength,
                final String contentType) throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Encoding", "gzip"));

        if (contentLength != -1) {
            headers.add(new NameValuePair(HttpHeader.CONTENT_LENGTH, String.valueOf(contentLength)));
        }

        if (contentType != null) {
            headers.add(new NameValuePair(HttpHeader.CONTENT_TYPE, contentType));
        }

        final WebResponseData data = new WebResponseData("".getBytes(), statusCode, "OK", headers);
        final String body = new String(data.getBody(), UTF_8);

        final WebResponse response = new WebResponse(data, new URL("http://test.com"), HttpMethod.GET, 1000);
        assertEquals(body, response.getContentAsString(UTF_8));
    }

    /**
     * Tests that broken gzipped content is handled correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void brokenGZippedContent() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Encoding", "gzip"));

        final WebResponseData data = new WebResponseData("Plain Content".getBytes(), HttpStatus.SC_OK, "OK", headers);
        try {
            data.getBody();
        }
        catch (final RuntimeException e) {
            assertTrue(e.getMessage(), StringUtils.contains(e.getMessage(), "Not in GZIP format"));
        }
    }

    /**
     * Tests that brotli encoded content is handled correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void brotliContent() throws Exception {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream(BROTLI_FILE);
        final byte[] zippedContent = IOUtils.toByteArray(stream);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Encoding", "br"));

        final WebResponseData data = new WebResponseData(zippedContent, HttpStatus.SC_OK, "OK", headers);
        final String body = new String(data.getBody(), UTF_8);
        assertTrue(StringUtils.contains(body, "Test"));

        final WebResponse response = new WebResponse(data, new URL("http://test.com"), HttpMethod.GET, 1000);
        assertEquals(body, response.getContentAsString(UTF_8));
    }

    /**
     * Tests that broken gzipped content is handled correctly.
     * @throws Exception if the test fails
     */
    @Test
    public void brokenBrotliContent() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Encoding", "br"));

        final WebResponseData data = new WebResponseData("Plain Content".getBytes(), HttpStatus.SC_OK, "OK", headers);
        try {
            data.getBody();
        }
        catch (final RuntimeException e) {
            assertTrue(e.getMessage(), StringUtils.contains(e.getMessage(), "Brotli stream decoding failed"));
        }
    }

    /**
     * Verifies that a null body input stream is handled correctly. A null body may be sent, for
     * example, when a 304 (Not Modified) response is sent to the client. See bug 1706505.
     * @throws Exception if the test fails
     */
    @Test
    public void nullBody() throws Exception {
        final DownloadedContent downloadedContent = new DownloadedContent.InMemory(new byte[] {});
        final List<NameValuePair> headers = new ArrayList<>();
        final WebResponseData data = new WebResponseData(downloadedContent, 304, "NOT_MODIFIED", headers);
        assertEquals(0, data.getBody().length);

        final WebResponse response = new WebResponse(data, new URL("http://test.com"), HttpMethod.GET, 1000);
        assertEquals(0, response.getContentAsString(UTF_8).length());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void deflateCompression() throws Exception {
        startWebServer("src/test/resources/pjl-comp-filter", null);
        final WebRequest request = new WebRequest(new URL("http://localhost:"
            + PORT + "/index.html"));
        request.setAdditionalHeader(HttpHeader.ACCEPT_ENCODING, "deflate");
        final WebClient webClient = getWebClient();
        final HtmlPage page = webClient.getPage(request);
        assertEquals("Hello Compressed World!", page.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void redirection() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/folder1/page1", RedirectionServlet.class);
        servlets.put("/folder2/page2", RedirectionServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();

        final HtmlPage page = client.getPage(URL_FIRST + "folder1/page1");
        assertEquals("Hello Redirected!", page.asText());
    }

    /**
     * Servlet for {@link #redirection()}.
     */
    public static class RedirectionServlet extends HttpServlet {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            if ("/folder1/page1".equals(request.getRequestURI())) {
                response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                final String location = request.getRequestURL().toString().replace("page1", "../folder2/page2");
                response.setHeader("Location", location);
                return;
            }
            response.setContentType(MimeType.TEXT_HTML);
            final Writer writer = response.getWriter();
            writer.write("Hello Redirected!");
        }
    }

}
