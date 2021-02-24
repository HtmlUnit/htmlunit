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
package com.gargoylesoftware.htmlunit.javascript;

import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link JavaScriptErrorListener}.
 *
 * @author Ronald Brill
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)

public class JavascriptErrorListenerTest extends WebServerTestCase {

    /**
     * Test for running without a JavaScript error listener.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void nullJavaScriptErrorListener() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setJavaScriptErrorListener(null);
        final MockWebConnection webConnection = new MockWebConnection();
        final String errorContent = "<html><head><title>ERROR 500</title></head><body></body></html>";
        final List<NameValuePair> emptyList = Collections.emptyList();
        webConnection.setResponse(URL_SECOND, errorContent, 500, "BOOM", MimeType.TEXT_HTML, emptyList);

        // test script exception
        String content = "<html><head><title>Throw JavaScript Error</title>\n"
            + "<script>unknown.foo();</script></head>\n"
            + "<body></body></html>";
        webConnection.setResponse(URL_FIRST, content);
        webClient.setWebConnection(webConnection);
        webClient.getPage(URL_FIRST);

        // test load script error
        content = "<html><head><title>Throw JavaScript Error</title>\n"
            + "<script src='" + URL_SECOND + "' type='text/javascript'></script></head>\n"
            + "<body></body></html>";
        webConnection.setResponse(URL_FIRST, content);
        try {
            webClient.getPage(URL_FIRST);
            fail("FailingHttpStatusCodeException expected");
        }
        catch (final FailingHttpStatusCodeException e) {
            // expected
        }

        // test malformed script url error
        content = "<html><head><title>Throw JavaScript Error</title>\n"
            + "<script src='unknown://nowhere' type='text/javascript'></script></head>\n"
            + "<body></body></html>";
        webConnection.setResponse(URL_FIRST, content);
        webClient.getPage(URL_FIRST);

        // test timeout error
        webClient.setJavaScriptTimeout(100);

        content = "<html><head><title>Throw JavaScript Timeout Error</title>\n"
            + "<script>while(1) {}</script></head>\n"
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

        assertEquals("", javaScriptErrorListener.getWarnings());
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

        getMockWebConnection().setDefaultResponse("", 500, "Server Error", MimeType.TEXT_HTML);

        final String html = "<html><head>\n"
            + "<script src='notExisting.js' type='text/javascript'></script></head>\n"
            + "<body></body></html>";

        try {
            loadPage(html);
            fail("FailingHttpStatusCodeException expected");
        }
        catch (final FailingHttpStatusCodeException e) {
            // expected
        }

        assertEquals("", javaScriptErrorListener.getScriptExceptions());
        assertEquals(URL_FIRST + "notExisting.js, "
                + "com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException: "
                + "500 Server Error for " + URL_FIRST + "notExisting.js",
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

        final String html = "<html>\n"
                + "<head><title>Throw JavaScript Error</title>\n"
                + "<script src='unknown://nowhere' type='text/javascript'></script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        loadPage(html);

        assertEquals("", javaScriptErrorListener.getWarnings());
        assertEquals("", javaScriptErrorListener.getScriptExceptions());
        assertEquals("", javaScriptErrorListener.getLoadScriptErrors());
        assertEquals("unknown://nowhere, java.net.MalformedURLException: unknown protocol: 'unknown'",
                    javaScriptErrorListener.getMalformedScriptURLErrors());
        assertEquals("", javaScriptErrorListener.getTimeoutErrors());
    }

    /**
     * Listener should capture timeout errors.
     * Configured with a timeout as the build server seemed to have problem with this test from time to time.
     * @throws Exception if the test fails
     */
    @Test(timeout = 10_000)
    public void listenForTimeoutError() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        final CollectingJavaScriptErrorListener javaScriptErrorListener = new CollectingJavaScriptErrorListener();
        webClient.setJavaScriptErrorListener(javaScriptErrorListener);
        webClient.setJavaScriptTimeout(100);

        final String html = "<html><head><title>Throw JavaScript Timeout Error</title>\n"
            + "<script>while(1) {}</script></head>\n"
            + "<body></body></html>";

        loadPage(html);

        assertEquals("", javaScriptErrorListener.getWarnings());
        assertEquals("", javaScriptErrorListener.getScriptExceptions());
        assertEquals("", javaScriptErrorListener.getLoadScriptErrors());
        assertEquals("", javaScriptErrorListener.getMalformedScriptURLErrors());
        assertEquals("Timeout allowed: 100", javaScriptErrorListener.getTimeoutErrors());
    }
}

class CollectingJavaScriptErrorListener implements JavaScriptErrorListener {
    private final StringBuilder warnings_ = new StringBuilder();
    private final StringBuilder scriptExceptions_ = new StringBuilder();
    private final StringBuilder timeoutErrors_ = new StringBuilder();
    private final StringBuilder loadScriptErrors_ = new StringBuilder();
    private final StringBuilder malformedScriptURLErrors_ = new StringBuilder();

    @Override
    public void warn(final String message, final String sourceName,
            final int line, final String lineSource, final int lineOffset) {
        warnings_.append(message);
    }

    @Override
    public void loadScriptError(final HtmlPage page, final URL scriptUrl, final Exception exception) {
        loadScriptErrors_.append(scriptUrl + ", " + exception);
    }

    @Override
    public void malformedScriptURL(final HtmlPage page, final String url,
            final MalformedURLException malformedURLException) {
        malformedScriptURLErrors_.append(url + ", " + malformedURLException);
    }

    @Override
    public void scriptException(final HtmlPage page, final ScriptException scriptException) {
        scriptExceptions_.append(scriptException.toString());
    }

    @Override
    public void timeoutError(final HtmlPage page, final long allowedTime, final long executionTime) {
        timeoutErrors_.append("Timeout allowed: " + allowedTime);
    }

    public String getWarnings() {
        return warnings_.toString();
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
