/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;

import com.gargoylesoftware.base.testing.BaseTestCase;



/**
 * Tests methods in {@link HttpWebConnection} class.
 *
 * @author David D. Kilzer
 * @version $Revision$
 */
public class HttpWebConnectionTest extends BaseTestCase {

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
            while(true) {

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
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>gargoylesoftware.com</code>.
     */
    public void testGetStateForUrl_gargoylesoftware_com() {
        assertGetStateForUrl("gargoylesoftware.com");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>www.gargoylesoftware.com</code>.
     */
    public void testGetStateForUrl_www_gargoylesoftware_com() {
        assertGetStateForUrl("www.gargoylesoftware.com");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>www.sub.gargoylesoftware.com</code>.
     */
    public void testGetStateForUrl_www_sub_gargoylesoftware_com() {
        assertGetStateForUrl("www.sub.gargoylesoftware.com");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>localhost</code>.
     */
    public void testGetStateForUrl_localhost() {
        assertGetStateForUrl("localhost");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>GARGOYLESOFTWARE.COM</code>.
     */
    public void testGetStateForUrl_GARGOYLESOFTWARE_COM() {
        assertGetStateForUrl("GARGOYLESOFTWARE.COM");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>WWW.GARGOYLESOFTWARE.COM</code>.
     */
    public void testGetStateForUrl_WWW_GARGOYLESOFTWARE_COM() {
        assertGetStateForUrl("WWW.GARGOYLESOFTWARE.COM");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>WWW.SUB.GARGOYLESOFTWARE.COM</code>.
     */
    public void testGetStateForUrl_WWW_SUB_GARGOYLESOFTWARE_COM() {
        assertGetStateForUrl("WWW.SUB.GARGOYLESOFTWARE.COM");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>LOCALHOST</code>.
     */
    public void testGetStateForUrl_LOCALHOST() {
        assertGetStateForUrl("LOCALHOST");
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

        final HttpMethod httpMethod = new GetMethod(url.toString());
        final Field field = HttpMethodBase.class.getDeclaredField("responseBody");
        field.setAccessible(true);
        field.set(httpMethod, content.getBytes());

        final HttpWebConnection connection = new HttpWebConnection(new WebClient());
        final Method method =
                connection.getClass().getDeclaredMethod("makeWebResponse", new Class[]{
                    int.class, HttpMethod.class, URL.class, long.class});
        method.setAccessible(true);

        final WebResponse response =
                (WebResponse) method.invoke(connection, new Object[]{
                    new Integer(httpStatus), httpMethod, url, new Long(loadTime)});

        assertEquals(httpStatus, response.getStatusCode());
        assertEquals(url, response.getUrl());
        assertEquals(loadTime, response.getLoadTimeInMilliSeconds());
        assertEquals(content, response.getContentAsString());
        assertEquals(content.getBytes(), response.getResponseBody());
        assertEquals(new ByteArrayInputStream(content.getBytes()), response.getContentAsStream());
    }


    /**
     * Tests the {@link HttpWebConnection#getStateForUrl(java.net.URL)} using reflection.
     *
     * @param hostname The hostname of the url to test
     */
    private void assertGetStateForUrl(final String hostname) {

        final HttpWebConnection connection = new HttpWebConnection(new WebClient());
        try {

            final Field httpClients = connection.getClass().getDeclaredField("httpClients_");
            httpClients.setAccessible(true);
            final Map map = (Map) httpClients.get(connection);

            final HttpState expectedHttpState = new HttpState();

            final HttpClient httpClient = new HttpClient();
            final Field httpState = httpClient.getClass().getDeclaredField("state");
            httpState.setAccessible(true);
            httpState.set(httpClient, expectedHttpState);

            map.put("http://" + hostname.toLowerCase(), httpClient);

            final URL url = new URL("http://" + hostname + "/context");
            final HttpState actualHttpState = connection.getStateForUrl(url);
            assertSame(expectedHttpState, actualHttpState);
        }
        catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
