/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.methods.GetMethod;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.ResourceHandler;

import com.gargoylesoftware.base.testing.BaseTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests methods in {@link HttpWebConnection}.
 *
 * @version $Revision$
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HttpWebConnectionTest extends BaseTestCase {
    private HttpServer httpServer_;

    /**
     * Assert that the two byte arrays are equal
     * @param expected The expected value
     * @param actual The actual value
     */
    public static void assertEquals(final byte[] expected, final byte[] actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * Assert that the two byte arrays are equal
     * @param message The message to display on failure
     * @param expected The expected value
     * @param actual The actual value
     */
    public static void assertEquals(
            final String message, final byte[] expected, final byte[] actual) {
        assertEquals(message, expected, actual, expected.length);
    }

    /**
     * Assert that the two byte arrays are equal
     * @param message The message to display on failure
     * @param expected The expected value
     * @param actual The actual value
     * @param length How many characters at the beginning of each byte array will be compared.
     */
    public static void assertEquals(
            final String message, final byte[] expected, final byte[] actual,
            final int length) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null) {
            fail(message);
        }
        if (expected.length < length || actual.length < length) {
            fail(message);
        }
        for (int i = 0; i < length; i++) {
            assertEquals(message, expected[i], actual[i]);
        }
    }

    /**
     * Assert that the two input streams are the same.
     * @param expected The expected value
     * @param actual The actual value
     * @throws IOException If an IO problem occurs during comparison
     */
    public static void assertEquals(final InputStream expected, final InputStream actual) throws IOException {
        assertEquals(null, expected, actual);
    }

    /**
     * Assert that the two input streams are the same.
     * @param message The message to display on failure
     * @param expected The expected value
     * @param actual The actual value
     * @throws IOException If an IO problem occurs during comparison
     */
    public static void assertEquals(
            final String message, final InputStream expected,
            final InputStream actual)
        throws IOException {

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
                assertEquals(message, expectedLength, actualLength);

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
     * Create an instance.
     *
     * @param name The name of the test.
     */
    public HttpWebConnectionTest(final String name) {
        super(name);
    }

    /**
     * Test creation of a web response
     * @throws Exception If the test fails
     */
    public void testMakeWebResponse() throws Exception {

        final URL url = new URL("http://htmlunit.sourceforge.net/");
        final String content = "<html><head></head><body></body></html>";
        final int httpStatus = 200;
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
                    int.class, HttpMethodBase.class, URL.class, long.class, String.class});
        method.setAccessible(true);

        final WebResponse response =
                (WebResponse) method.invoke(connection, new Object[]{
                    new Integer(httpStatus), httpMethod, url, new Long(loadTime), TextUtil.DEFAULT_CHARSET});

        assertEquals(httpStatus, response.getStatusCode());
        assertEquals(url, response.getUrl());
        assertEquals(loadTime, response.getLoadTimeInMilliSeconds());
        assertEquals(content, response.getContentAsString());
        assertEquals(content.getBytes(), response.getResponseBody());
        assertEquals(new ByteArrayInputStream(content.getBytes()), response.getContentAsStream());
    }

    /**
     * Testing Jetty
     * @throws Exception on failure
     */
    public void testJettyProofOfConcept() throws Exception {
        setupWebServer(12345);

        final WebClient client = new WebClient();
        final Page page = client.getPage("http://localhost:12345/");
        final WebConnection defaultConnection = page
                .getEnclosingWindow()
                .getWebClient()
                .getWebConnection();
        assertInstanceOf(
                "HttpWebConnection should be the default",
                defaultConnection,
                HttpWebConnection.class);
        assertInstanceOf("Response should be (mostly) valid HTML", page, HtmlPage.class);

    }

    /**
     * Starts the webserver on the given port
     * @param port the port on which the server should be started
     * @throws Exception
     */
    private void setupWebServer(final int port) throws Exception {
        httpServer_ = new HttpServer();

        final SocketListener listener = new SocketListener();
        listener.setPort(port);
        httpServer_.addListener(listener);

        final HttpContext context = new HttpContext();
        context.setContextPath("/");
        context.setResourceBase("./");
        context.addHandler(new ResourceHandler());
        httpServer_.addContext(context);

        httpServer_.start();
    }

    /**
     * {@inheritDoc}
     * Stops the web server if it has been started.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        if (httpServer_ != null) {
            httpServer_.stop();
        }
    }

    /**
     * Test for feature request 1438216: HttpWebConnection should allow extension to create the HttpClient
     * @throws Exception if the test fails
     */
    public void testDesignedForExtension() throws Exception {
        setupWebServer(12345);

        final WebClient webClient = new WebClient();
        final boolean[] tabCalled = {false};
        final WebConnection myWebConnection = new HttpWebConnection(webClient) {
            protected HttpClient createHttpClient() {
                tabCalled[0] = true;
                return new HttpClient();
            }
        };

        webClient.setWebConnection(myWebConnection);
        webClient.getPage("http://localhost:12345/");
        assertTrue("createHttpClient as not been called", tabCalled[0]);
    }

    /**
     * Was throwing a NPE on 14.04.06
     * @throws Exception if the test fails
     */
    public void testStateAccess() throws Exception {

        final WebClient webClient = new WebClient();
        webClient.getWebConnection().getState();
    }
}
