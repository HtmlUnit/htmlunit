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

import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ProtocolVersion;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.WriterAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.KeyDataPair;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.ServletContentWrapper;

/**
 * Tests methods in {@link HttpWebConnection}.
 *
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Carsten Steul
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnectionTest extends WebServerTestCase {

    /**
     * Assert that the two byte arrays are equal.
     * @param expected the expected value
     * @param actual the actual value
     */
    public static void assertEquals(final byte[] expected, final byte[] actual) {
        assertEquals(null, expected, actual, expected.length);
    }

    /**
     * Assert that the two byte arrays are equal.
     * @param message the message to display on failure
     * @param expected the expected value
     * @param actual the actual value
     * @param length How many characters at the beginning of each byte array will be compared
     */
    public static void assertEquals(
            final String message, final byte[] expected, final byte[] actual, final int length) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || expected.length < length) {
            fail(message);
        }
        if (actual == null || actual.length < length) {
            fail(message);
        }
        for (int i = 0; i < length; i++) {
            assertEquals(message, expected[i], actual[i]);
        }
    }

    /**
     * Assert that the two input streams are the same.
     * @param expected the expected value
     * @param actual the actual value
     * @throws IOException if an IO problem occurs during comparison
     */
    public static void assertEquals(final InputStream expected, final InputStream actual) throws IOException {
        assertEquals(null, expected, actual);
    }

    /**
     * Assert that the two input streams are the same.
     * @param message the message to display on failure
     * @param expected the expected value
     * @param actual the actual value
     * @throws IOException if an IO problem occurs during comparison
     */
    public static void assertEquals(final String message, final InputStream expected,
            final InputStream actual) throws IOException {

        if (expected == null && actual == null) {
            return;
        }

        if (expected == null || actual == null) {
            try {
                fail(message);
            }
            finally {
                try {
                    if (expected != null) {
                        expected.close();
                    }
                }
                finally {
                    if (actual != null) {
                        actual.close();
                    }
                }
            }
        }

        try (InputStream expectedBuf = new BufferedInputStream(expected)) {
            try (InputStream actualBuf = new BufferedInputStream(actual)) {

                final byte[] expectedArray = new byte[2048];
                final byte[] actualArray = new byte[2048];

                int expectedLength = expectedBuf.read(expectedArray);
                while (true) {

                    final int actualLength = actualBuf.read(actualArray);
                    assertEquals(message, expectedLength, actualLength);

                    if (expectedLength == -1) {
                        break;
                    }

                    assertEquals(message, expectedArray, actualArray, expectedLength);
                    expectedLength = expectedBuf.read(expectedArray);
                }
            }
        }
    }

    /**
     * Tests creation of a web response.
     * @throws Exception if the test fails
     */
    @Test
    public void makeWebResponse() throws Exception {
        final URL url = new URL("http://htmlunit.sourceforge.net/");
        final String content = "<html><head></head><body></body></html>";
        final DownloadedContent downloadedContent = new DownloadedContent.InMemory(content.getBytes());
        final int httpStatus = HttpStatus.SC_OK;
        final long loadTime = 500L;

        final ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 0);
        final StatusLine statusLine = new StatusLine(protocolVersion, HttpStatus.SC_OK, null);
        final ClassicHttpResponse httpResponse = new BasicClassicHttpResponse(HttpStatus.SC_OK);

        final HttpEntity responseEntity = new StringEntity(content);
        httpResponse.setEntity(responseEntity);

        final HttpWebConnection connection = new HttpWebConnection(getWebClient());
        final Method method = connection.getClass().getDeclaredMethod("makeWebResponse",
                HttpResponse.class, WebRequest.class, DownloadedContent.class, long.class);
        method.setAccessible(true);
        final WebResponse response = (WebResponse) method.invoke(connection,
                httpResponse, new WebRequest(url), downloadedContent, new Long(loadTime));

        assertEquals(httpStatus, response.getStatusCode());
        assertEquals(url, response.getWebRequest().getUrl());
        assertEquals(loadTime, response.getLoadTime());
        assertEquals(content, response.getContentAsString());
        assertEquals(content.getBytes(), IOUtils.toByteArray(response.getContentAsStream()));
        assertEquals(new ByteArrayInputStream(content.getBytes()), response.getContentAsStream());
    }

    /**
     * Tests Jetty.
     * @throws Exception on failure
     */
    @Test
    public void jettyProofOfConcept() throws Exception {
        startWebServer("./");

        final WebClient client = getWebClient();
        final Page page = client.getPage(URL_FIRST + "src/test/resources/event_coordinates.html");
        final WebConnection defaultConnection = client.getWebConnection();
        assertTrue(
                "HttpWebConnection should be the default",
                HttpWebConnection.class.isInstance(defaultConnection));
        assertTrue("Response should be valid HTML", HtmlPage.class.isInstance(page));
    }

    /**
     * Test for feature request 1438216: HttpWebConnection should allow extension to create the HttpClient.
     * @throws Exception if the test fails
     */
    @Test
    public void designedForExtension() throws Exception {
        startWebServer("./");

        final WebClient webClient = getWebClient();
        final boolean[] tabCalled = {false};
        final WebConnection myWebConnection = new HttpWebConnection(webClient) {
            @Override
            protected HttpAsyncClientBuilder createHttpClientBuilder() {
                tabCalled[0] = true;

                final HttpAsyncClientBuilder builder = HttpAsyncClientBuilder.create();
                builder.setConnectionManagerShared(true);
                return builder;
            }
        };

        webClient.setWebConnection(myWebConnection);
        webClient.getPage(URL_FIRST + "LICENSE.txt");
        assertTrue("createHttpClient has not been called", tabCalled[0]);
    }

    /**
     * Test that the HttpClient is reinitialised after being shutdown.
     * @throws Exception if the test fails
     */
    @Test
    public void reinitialiseAfterClosing() throws Exception {
        startWebServer("./");

        final WebClient webClient = getWebClient();
        try (HttpWebConnection webConnection = new HttpWebConnection(webClient)) {
            webClient.setWebConnection(webConnection);
            webClient.getPage(URL_FIRST + "LICENSE.txt");
            webConnection.close();
            webClient.getPage(URL_FIRST + "pom.xml");
        }
    }

    /**
     * Test that the right file part is built for a file that doesn't exist.
     * @throws Exception if the test fails
     */
    @Test
    public void buildFilePart() throws Exception {
        final String encoding = "ISO8859-1";
        final KeyDataPair pair = new KeyDataPair("myFile", new File("this/doesnt_exist.txt"), "something",
                MimeType.TEXT_PLAIN, encoding);
        final MultipartEntityBuilder builder = MultipartEntityBuilder.create().setLaxMode();
        try (HttpWebConnection webConnection = new HttpWebConnection(getWebClient())) {
            webConnection.buildFilePart(pair, builder);
        }
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        builder.build().writeTo(baos);
        final String part = baos.toString(encoding);

        final String expected = "--(.*)\r\n"
                + "Content-Disposition: form-data; name=\"myFile\"; filename=\"doesnt_exist.txt\"\r\n"
                + "Content-Type: text/plain\r\n"
                + "\r\n"
                + "\r\n"
                + "--\\1--\r\n";
        assertTrue(part, part.matches(expected));
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void unicode() throws Exception {
        startWebServer("./");
        final WebClient client = getWebClient();
        client.getPage(URL_FIRST + "src/test/resources/event_coordinates.html?param=\u00F6");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void emptyPut() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test", EmptyPutServlet.class);
        startWebServer("./", null, servlets);

        final String[] expectedAlerts = {"1"};
        final WebClient client = getWebClient();
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        assertEquals(0, client.getCookieManager().getCookies().size());
        client.getPage(URL_FIRST + "test");
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals(1, client.getCookieManager().getCookies().size());
    }

    /**
     * Servlet for {@link #emptyPut()}.
     */
    public static class EmptyPutServlet extends ServletContentWrapper {
        /** Constructor. */
        public EmptyPutServlet() {
            super("<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      var xhr = window.ActiveXObject?new ActiveXObject('Microsoft.XMLHTTP'):new XMLHttpRequest();\n"
                + "      xhr.open('PUT', '" + URL_FIRST + "test" + "', true);\n"
                + "      xhr.send();\n"
                + "      alert(1);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'></body>\n"
                + "</html>");
        }

        @Override
        protected void doGet(final HttpServletRequest request,
                final HttpServletResponse response)
            throws ServletException, IOException {
            request.getSession().setAttribute("trigger", "session");
            super.doGet(request, response);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Host",
            IE = {})
    public void hostHeaderFirst() throws Exception {
        final Logger logger = (Logger) LogManager.getLogger("org.apache.http.headers");
        final Level oldLevel = logger.getLevel();
        Configurator.setLevel(logger.getName(), Level.DEBUG);

        final StringWriter stringWriter = new StringWriter();
        final PatternLayout layout = PatternLayout.newBuilder().withPattern("%msg%n").build();

        final WriterAppender writerAppender = WriterAppender.newBuilder().setName("writeLogger").setTarget(stringWriter)
                .setLayout(layout).build();
        writerAppender.start();

        logger.addAppender(writerAppender);
        try {
            startWebServer("./");

            final WebClient webClient = getWebClient();
            webClient.getPage(URL_FIRST + "LICENSE.txt");
            final String[] messages = StringUtils.split(stringWriter.toString(), "\n");
            for (int i = 0; i < getExpectedAlerts().length; i++) {
                assertTrue(messages[i + 1].contains(getExpectedAlerts()[i]));
            }
        }
        finally {
            logger.removeAppender(writerAppender);
            Configurator.setLevel(logger.getName(), oldLevel);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void cookiesEnabledAfterDisable() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test1", Cookie1Servlet.class);
        servlets.put("/test2", Cookie2Servlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();

        client.getCookieManager().setCookiesEnabled(false);
        HtmlPage page = client.getPage(URL_FIRST + "test1");
        assertTrue(page.asText().contains("No Cookies"));

        client.getCookieManager().setCookiesEnabled(true);
        page = client.getPage(URL_FIRST + "test1");
        assertTrue(page.asText().contains("key1=value1"));
    }

    /**
     * Servlet for {@link #cookiesEnabledAfterDisable()}.
     */
    public static class Cookie1Servlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.addCookie(new javax.servlet.http.Cookie("key1", "value1"));
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            final String location = request.getRequestURL().toString().replace("test1", "test2");
            response.setHeader("Location", location);
        }
    }

    /**
     * Servlet for {@link #cookiesEnabledAfterDisable()}.
     */
    public static class Cookie2Servlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType(MimeType.TEXT_HTML);
            final Writer writer = response.getWriter();
            if (request.getCookies() == null || request.getCookies().length == 0) {
                writer.write("No Cookies");
            }
            else {
                for (final javax.servlet.http.Cookie c : request.getCookies()) {
                    writer.write(c.getName() + '=' + c.getValue());
                }
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void remotePort() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test", RemotePortServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();

        String firstPort = null;

        for (int i = 0; i < 5; i++) {
            final HtmlPage page = client.getPage(URL_FIRST + "test");
            final String port = page.asText();
            if (firstPort == null) {
                firstPort = port;
            }
            assertEquals(firstPort, port);
        }
    }

    /**
     * Servlet for {@link #remotePort()}.
     */
    public static class RemotePortServlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType(MimeType.TEXT_HTML);
            response.getWriter().write(String.valueOf(request.getRemotePort()));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentLengthSmallerThanContent() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/contentLengthSmallerThanContent", ContentLengthSmallerThanContentServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(URL_FIRST + "contentLengthSmallerThanContent");
        assertEquals("visible text", page.asText());
    }

    /**
     * Servlet for {@link #contentLengthSmallerThanContent()}.
     */
    public static class ContentLengthSmallerThanContentServlet extends ServletContentWrapper {

        /** Constructor. */
        public ContentLengthSmallerThanContentServlet() {
            super("<html>\n"
                + "<body>\n"
                + "  <p>visible text</p>\n"
                + "  <p>missing text</p>\n"
                + "</body>\n"
                + "</html>");
        }

        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
            response.setContentLength(getContent().indexOf("<p>missing text</p>"));
            super.doGet(request, response);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentLengthSmallerThanContentLargeContent() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/contentLengthSmallerThanContent", ContentLengthSmallerThanContentLargeContentServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(URL_FIRST + "contentLengthSmallerThanContent");
        assertTrue(page.asText(), page.asText().endsWith("visible text"));
    }

    /**
     * Servlet for {@link #contentLengthSmallerThanContentLargeContent()}.
     */
    public static class ContentLengthSmallerThanContentLargeContentServlet extends ServletContentWrapper {

        /** Constructor. */
        public ContentLengthSmallerThanContentLargeContentServlet() {
            super("<html>\n"
                + "<body>\n"
                + "  <p>"
                + StringUtils.repeat("HtmlUnit  ", 1024 * 1024)
                + "</p>\n"
                + "  <p>visible text</p>\n"
                + "  <p>missing text</p>\n"
                + "</body>\n"
                + "</html>");
        }

        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
            response.setContentLength(getContent().indexOf("<p>missing text</p>"));
            super.doGet(request, response);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentLengthLargerThanContent() throws Exception {
        final String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Length: 2000\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<html><body><p>visible text</p></body></html>";

        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, null)) {
            final WebClient client = getWebClient();

            final HtmlPage page = client.getPage("http://localhost:" + primitiveWebServer.getPort());
            assertEquals("visible text", page.asText());
        }
    }

    /**
     * Test for bug #1861.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void userAgent() throws Exception {
        final WebClient webClient = getWebClient();
        final HttpWebConnection connection = (HttpWebConnection) webClient.getWebConnection();
        final HttpAsyncClientBuilder builder = connection.getHttpClientBuilder();
        final String userAgent = get(builder, "userAgent");
        assertEquals(webClient.getBrowserVersion().getUserAgent(), userAgent);
    }

    @SuppressWarnings("unchecked")
    private static <T> T get(final Object o, final String fieldName) throws Exception {
        final Field field = o.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(o);
    }

}
