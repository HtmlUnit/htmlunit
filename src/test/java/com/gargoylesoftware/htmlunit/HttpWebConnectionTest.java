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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.KeyDataPair;
import com.gargoylesoftware.htmlunit.util.ServletContentWrapper;

/**
 * Tests methods in {@link HttpWebConnection}.
 *
 * @version $Revision$
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnectionTest extends WebServerTestCase {

    /**
     * Assert that the two byte arrays are equal.
     * @param expected the expected value
     * @param actual the actual value
     */
    public static void assertEquals(final byte[] expected, final byte[] actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * Assert that the two byte arrays are equal.
     * @param message the message to display on failure
     * @param expected the expected value
     * @param actual the actual value
     */
    public static void assertEquals(final String message, final byte[] expected, final byte[] actual) {
        assertEquals(message, expected, actual, expected.length);
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
        if (expected == null || actual == null) {
            Assert.fail(message);
        }
        if (expected.length < length || actual.length < length) {
            Assert.fail(message);
        }
        for (int i = 0; i < length; i++) {
            Assert.assertEquals(message, expected[i], actual[i]);
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
                Assert.fail(message);
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

        InputStream expectedBuf = null;
        InputStream actualBuf = null;
        try {
            expectedBuf = new BufferedInputStream(expected);
            actualBuf = new BufferedInputStream(actual);

            final byte[] expectedArray = new byte[2048];
            final byte[] actualArray = new byte[2048];

            int expectedLength = expectedBuf.read(expectedArray);
            while (true) {

                final int actualLength = actualBuf.read(actualArray);
                Assert.assertEquals(message, expectedLength, actualLength);

                if (expectedLength == -1) {
                    break;
                }

                assertEquals(message, expectedArray, actualArray, expectedLength);
                expectedLength = expectedBuf.read(expectedArray);
            }
        }
        finally {
            try {
                if (expectedBuf != null) {
                    expectedBuf.close();
                }
            }
            finally {
                if (actualBuf != null) {
                    actualBuf.close();
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
        final int httpStatus = HttpStatus.SC_OK;
        final long loadTime = 500L;

        final HttpMethodBase httpMethod = new GetMethod(url.toString());
        final Field responseBodyField = HttpMethodBase.class.getDeclaredField("responseBody");
        responseBodyField.setAccessible(true);
        responseBodyField.set(httpMethod, content.getBytes());

        final StatusLine statusLine = new StatusLine("HTTP/1.0 200 OK");
        final Field statusLineField = HttpMethodBase.class.getDeclaredField("statusLine");
        statusLineField.setAccessible(true);
        statusLineField.set(httpMethod, statusLine);

        final HttpWebConnection connection = new HttpWebConnection(new WebClient());
        final Method method =
                connection.getClass().getDeclaredMethod("makeWebResponse", new Class[]{
                    int.class, HttpMethodBase.class, WebRequestSettings.class, long.class});
        method.setAccessible(true);

        final WebResponse response =
                (WebResponse) method.invoke(connection, new Object[]{
                    new Integer(httpStatus), httpMethod, new WebRequestSettings(url),
                    new Long(loadTime)});

        Assert.assertEquals(httpStatus, response.getStatusCode());
        Assert.assertEquals(url, response.getRequestSettings().getUrl());
        Assert.assertEquals(loadTime, response.getLoadTime());
        Assert.assertEquals(content, response.getContentAsString());
        assertEquals(content.getBytes(), response.getContentAsBytes());
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
        final Page page = client.getPage("http://localhost:" + PORT + "/src/test/resources/event_coordinates.html");
        final WebConnection defaultConnection = client.getWebConnection();
        Assert.assertTrue(
                "HttpWebConnection should be the default",
                HttpWebConnection.class.isInstance(defaultConnection));
        Assert.assertTrue("Response should be valid HTML", HtmlPage.class.isInstance(page));
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
            protected HttpClient createHttpClient() {
                tabCalled[0] = true;
                return new HttpClient();
            }
        };

        webClient.setWebConnection(myWebConnection);
        webClient.getPage("http://localhost:" + PORT + "/LICENSE.txt");
        Assert.assertTrue("createHttpClient has not been called", tabCalled[0]);
    }

    /**
     * Test that the right file part is built for a file that doesn't exist.
     * @throws Exception if the test fails
     */
    @Test
    public void buildFilePart() throws Exception {
        final String encoding = "ISO8859-1";
        final KeyDataPair pair = new KeyDataPair("myFile", new File("this/doesnt_exist.txt"), "text/plain", encoding);
        final FilePart part = new HttpWebConnection(new WebClient()).buildFilePart(pair, encoding);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        part.send(baos);

        final String expected = "------------------314159265358979323846\r\n"
            + "Content-Disposition: form-data; name=\"myFile\"; filename=\"doesnt_exist.txt\"\r\n"
            + "Content-Type: text/plain\r\n"
            + "Content-Transfer-Encoding: binary\r\n"
            + "\r\n"
            + "\r\n";
        Assert.assertEquals(expected, baos.toString(encoding));
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void unicode() throws Exception {
        startWebServer("./");
        final WebClient client = getWebClient();
        client.getPage("http://localhost:" + PORT + "/src/test/resources/event_coordinates.html?param=\u00F6");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void emptyPut() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", EmptyPutServlet.class);
        startWebServer("./", null, servlets);

        final String[] expectedAlerts = {"1"};
        final WebClient client = getWebClient();
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage("http://localhost:" + PORT + "/test");
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Servlet for {@link #emptyPut()}.
     */
    public static class EmptyPutServlet extends ServletContentWrapper {
        private static final long serialVersionUID = -8674500186401667484L;

        /** Constructor. */
        public EmptyPutServlet() {
            super("<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      var xhr = window.ActiveXObject?new ActiveXObject('Microsoft.XMLHTTP'):new XMLHttpRequest();\n"
                + "      xhr.open('PUT', '" + "http://localhost:" + PORT + "/test" + "', true);\n"
                + "      xhr.send();\n"
                + "      alert(1);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'></body>\n"
                + "</html>");
        }
    }
}
