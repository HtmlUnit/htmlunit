/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link JavaScriptErrorListener}.
 *
 * @version $Revision$
 * @author Ronald Brill
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class JavascriptErrorListenerTest extends WebServerTestCase {
    private static final Locale locale = Locale.getDefault();

    /**
     * Sets the locale to US to allow tests to express expectations concerning error messages.
     */
    @BeforeClass
    public static void setupLocale() {
        // Set the default locale to US because Rhino messages are localized
        Locale.setDefault(Locale.US);
    }

    /**
     * Restore the locale to the original value.
     */
    @AfterClass
    public static void restoreLocale() {
        Locale.setDefault(locale);
    }

    /**
     * Test for running without a JavaScript error listener.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testNullJavaScriptErrorListener() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setJavaScriptErrorListener(null);
        final MockWebConnection webConnection = new MockWebConnection();
        final String errorContent = "<html><head><title>ERROR 500</title></head><body></body></html>";
        final List<NameValuePair> emptyList = Collections.emptyList();
        webConnection.setResponse(URL_SECOND, errorContent, 500, "BOOM", "text/html", emptyList);

        // test script exception
        String content = "<html><head><title>Throw JavaScript Error</title>"
            + "<script>unknown.foo();</script></head>"
            + "<body></body></html>";
        webConnection.setResponse(URL_FIRST, content);
        webClient.setWebConnection(webConnection);
        webClient.getPage(URL_FIRST);

        // test load script error
        content = "<html><head><title>Throw JavaScript Error</title>"
            + "<script src='" + URL_SECOND + "' type='text/javascript'></script></head>"
            + "<body></body></html>";
        webConnection.setResponse(URL_FIRST, content);
        try {
            webClient.getPage(URL_FIRST);
            Assert.fail("FailingHttpStatusCodeException expected");
        }
        catch (final FailingHttpStatusCodeException e) {
            // expected
        }

        // test malformed script url error
        content = "<html><head><title>Throw JavaScript Error</title>"
            + "<script src='unknown://nowhere' type='text/javascript'></script></head>"
            + "<body></body></html>";
        webConnection.setResponse(URL_FIRST, content);
        webClient.getPage(URL_FIRST);

        // test timeout error
        webClient.setJavaScriptTimeout(100);

        content = "<html><head><title>Throw JavaScript Timeout Error</title>"
            + "<script>while(1) {}</script></head>"
            + "<body></body></html>";
        webConnection.setResponse(URL_FIRST, content);
        webClient.getPage(URL_FIRST);
    }

    /**
     * Listener should capture script exception.
     * @throws Exception if the test fails
     */
    @Test
    public void listenForScriptException() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        final CollectingJavaScriptErrorListener javaScriptErrorListener = new CollectingJavaScriptErrorListener();
        webClient.setJavaScriptErrorListener(javaScriptErrorListener);

        // test script exception
        final String html = "<html><head><title>Throw JavaScript Error</title>"
            + "<script>unknown.foo();</script></head>"
            + "<body></body></html>";
        loadPage(html);

        assertEquals("com.gargoylesoftware.htmlunit.ScriptException: "
                + "ReferenceError: \"unknown\" is not defined. "
                + "(script in http://localhost:" + PORT + "/ from (1, 58) to (1, 81)#1)",
                javaScriptErrorListener.getScriptExceptions());
        assertEquals("", javaScriptErrorListener.getLoadScriptErrors());
        assertEquals("", javaScriptErrorListener.getMalformedScriptURLErrors());
        assertEquals("", javaScriptErrorListener.getTimeoutErrors());
    }

    /**
     * Listener should capture script exception.
     * @throws Exception if the test fails
     */
    @Test
    public void listenForLoadScriptError() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        final CollectingJavaScriptErrorListener javaScriptErrorListener = new CollectingJavaScriptErrorListener();
        webClient.setJavaScriptErrorListener(javaScriptErrorListener);

        getMockWebConnection().setDefaultResponse("", 500, "Server Error", "text/html");

        final String html = "<html><head>"
            + "<script src='notExisting.js' type='text/javascript'></script></head>"
            + "<body></body></html>";

        try {
            loadPage(html);
            Assert.fail("FailingHttpStatusCodeException expected");
        }
        catch (final FailingHttpStatusCodeException e) {
            // expected
        }

        assertEquals("", javaScriptErrorListener.getScriptExceptions());
        assertEquals(getDefaultUrl() + "notExisting.js, "
                + "com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException: "
                + "500 Server Error for " + getDefaultUrl() + "notExisting.js",
                javaScriptErrorListener.getLoadScriptErrors());
        assertEquals("", javaScriptErrorListener.getMalformedScriptURLErrors());
        assertEquals("", javaScriptErrorListener.getTimeoutErrors());
    }

    /**
     * Listener should capture script exception.
     * @throws Exception if the test fails
     */
    @Test
    public void listenForMalformedScriptUrl() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        final CollectingJavaScriptErrorListener javaScriptErrorListener = new CollectingJavaScriptErrorListener();
        webClient.setJavaScriptErrorListener(javaScriptErrorListener);

        final String html = "<html><head><title>Throw JavaScript Error</title>"
                + "<script src='unknown://nowhere' type='text/javascript'></script></head>"
                + "<body></body></html>";

        loadPage(html);

        assertEquals("", javaScriptErrorListener.getScriptExceptions());
        assertEquals("", javaScriptErrorListener.getLoadScriptErrors());
        assertEquals("unknown://nowhere, java.net.MalformedURLException: unknown protocol: unknown",
                    javaScriptErrorListener.getMalformedScriptURLErrors());
        assertEquals("", javaScriptErrorListener.getTimeoutErrors());
    }

    /**
     * Listener should capture timeout errors.
     * Configured with a timeout as the build server seemed to have problem with this test from time to time.
     * @throws Exception if the test fails
     */
    @Test(timeout = 10000)
    public void listenForTimeoutError() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        final CollectingJavaScriptErrorListener javaScriptErrorListener = new CollectingJavaScriptErrorListener();
        webClient.setJavaScriptErrorListener(javaScriptErrorListener);
        webClient.setJavaScriptTimeout(100);

        final String html = "<html><head><title>Throw JavaScript Timeout Error</title>"
            + "<script>while(1) {}</script></head>"
            + "<body></body></html>";

        loadPage(html);

        assertEquals("", javaScriptErrorListener.getScriptExceptions());
        assertEquals("", javaScriptErrorListener.getLoadScriptErrors());
        assertEquals("", javaScriptErrorListener.getMalformedScriptURLErrors());
        assertEquals("Timeout allowed: 100", javaScriptErrorListener.getTimeoutErrors());
    }
}

class CollectingJavaScriptErrorListener implements JavaScriptErrorListener {
    private final StringBuilder scriptExceptions_ = new StringBuilder();
    private final StringBuilder timeoutErrors_ = new StringBuilder();
    private final StringBuilder loadScriptErrors_ = new StringBuilder();
    private final StringBuilder malformedScriptURLErrors_ = new StringBuilder();

    public void loadScriptError(final HtmlPage htmlPage, final URL scriptUrl, final Exception exception) {
        loadScriptErrors_.append(scriptUrl + ", " + exception);
    }

    public void malformedScriptURL(final HtmlPage htmlPage, final String url,
            final MalformedURLException malformedURLException) {
        malformedScriptURLErrors_.append(url + ", " + malformedURLException);
    }

    public void scriptException(final HtmlPage htmlPage, final ScriptException scriptException) {
        scriptExceptions_.append(scriptException.toString());
    }

    public void timeoutError(final HtmlPage htmlPage, final long allowedTime, final long executionTime) {
        timeoutErrors_.append("Timeout allowed: " + allowedTime);
    }

    public String getScriptExceptions() {
        return scriptExceptions_.toString();
    }

    public String getLoadScriptErrors() {
        return loadScriptErrors_.toString();
    }

    public String getMalformedScriptURLErrors() {
        return malformedScriptURLErrors_.toString();
    }

    public String getTimeoutErrors() {
        return timeoutErrors_.toString();
    }
}
