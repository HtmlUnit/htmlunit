/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
 */
@RunWith(BrowserRunner.class)
public class JavascriptErrorListenerTest extends WebServerTestCase {

    /**
     * Test for running without a JavaScript error listener.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testNullJavaScriptErrorListener() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setJavaScriptErrorListener(null);
        final MockWebConnection webConnection = new MockWebConnection();
        final String errorContent = "<html><head><title>ERROR 500</title></head><body></body></html>";
        final List< ? extends NameValuePair> emptyList = Collections.emptyList();
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
     * Test for running with a JavaScript error listener.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testJavaScriptErrorListener() throws Exception {
        final StringBuilder scriptExceptions = new StringBuilder();
        final StringBuilder timeoutErrors = new StringBuilder();
        final StringBuilder loadScriptErrors = new StringBuilder();
        final StringBuilder malformedScriptURLErrors = new StringBuilder();

        final WebClient webClient = getWebClient();
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {

            public void loadScriptError(final HtmlPage htmlPage, final URL scriptUrl, final Exception exception) {
                loadScriptErrors.append(scriptUrl.toExternalForm());
                loadScriptErrors.append(", ");
                loadScriptErrors.append(exception.toString());
            }

            public void malformedScriptURL(final HtmlPage htmlPage, final String url,
                    final MalformedURLException malformedURLException) {
                malformedScriptURLErrors.append(url);
                malformedScriptURLErrors.append(", ");
                malformedScriptURLErrors.append(malformedURLException.toString());
            }

            public void scriptException(final HtmlPage htmlPage, final ScriptException scriptException) {
                scriptExceptions.append(scriptException.toString());
            }

            public void timeoutError(final HtmlPage htmlPage, final long allowedTime, final long executionTime) {
                timeoutErrors.append("Timeout allowed: " + allowedTime);
            }
        });

        final MockWebConnection webConnection = new MockWebConnection();
        final String errorContent = "<html><head><title>ERROR 500</title></head><body></body></html>";
        final List< ? extends NameValuePair> emptyList = Collections.emptyList();
        webConnection.setResponse(URL_SECOND, errorContent, 500, "BOOM", "text/html", emptyList);

        // test script exception
        String content = "<html><head><title>Throw JavaScript Error</title>"
            + "<script>unknown.foo();</script></head>"
            + "<body></body></html>";
        webConnection.setResponse(URL_FIRST, content);
        webClient.setWebConnection(webConnection);
        webClient.getPage(URL_FIRST);

        assertEquals("com.gargoylesoftware.htmlunit.ScriptException: "
                + "ReferenceError: \"unknown\" is not defined. "
                + "(script in http://localhost:" + PORT + "/ from (1, 58) to (1, 81)#1)",
                scriptExceptions.toString());
        assertEquals("", loadScriptErrors.toString());
        assertEquals("", malformedScriptURLErrors.toString());
        assertEquals("", timeoutErrors.toString());

        // test load script error
        content = "<html><head><title>Throw JavaScript Error</title>"
            + "<script src='" + URL_SECOND + "' type='text/javascript'></script></head>"
            + "<body></body></html>";
        webConnection.setResponse(URL_FIRST, content);
        webClient.setWebConnection(webConnection);
        try {
            webClient.getPage(URL_FIRST);
            Assert.fail("FailingHttpStatusCodeException expected");
        }
        catch (final FailingHttpStatusCodeException e) {
            // expected
        }

        assertEquals("com.gargoylesoftware.htmlunit.ScriptException: "
                + "ReferenceError: \"unknown\" is not defined. "
                + "(script in http://localhost:" + PORT + "/ from (1, 58) to (1, 81)#1)",
                scriptExceptions.toString());
        assertEquals("http://localhost:" + PORT + "/second/, "
                + "com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException: "
                + "500 BOOM for http://localhost:" + PORT + "/second/", loadScriptErrors.toString());
        assertEquals("", malformedScriptURLErrors.toString());
        assertEquals("", timeoutErrors.toString());

        // test malformed script url error
        content = "<html><head><title>Throw JavaScript Error</title>"
            + "<script src='unknown://nowhere' type='text/javascript'></script></head>"
            + "<body></body></html>";
        webConnection.setResponse(URL_FIRST, content);
        webClient.setWebConnection(webConnection);
        webClient.getPage(URL_FIRST);

        assertEquals("com.gargoylesoftware.htmlunit.ScriptException: "
                + "ReferenceError: \"unknown\" is not defined. "
                + "(script in http://localhost:" + PORT + "/ from (1, 58) to (1, 81)#1)",
                scriptExceptions.toString());
        assertEquals("http://localhost:" + PORT + "/second/, "
                + "com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException: "
                + "500 BOOM for http://localhost:" + PORT + "/second/", loadScriptErrors.toString());
        assertEquals("unknown://nowhere, java.net.MalformedURLException: unknown protocol: unknown",
                malformedScriptURLErrors.toString());
        assertEquals("", timeoutErrors.toString());

        // test timeout error
        webClient.setJavaScriptTimeout(100);

        final Locale locale = Locale.getDefault();
        // Set the default locale to US because Rhino messages are localized
        Locale.setDefault(Locale.US);

        try {
            content = "<html><head><title>Throw JavaScript Timeout Error</title>"
                + "<script>while(1) {}</script></head>"
                + "<body></body></html>";
            webConnection.setResponse(URL_FIRST, content);
            webClient.setWebConnection(webConnection);
            webClient.getPage(URL_FIRST);

            assertEquals("com.gargoylesoftware.htmlunit.ScriptException: "
                    + "ReferenceError: \"unknown\" is not defined. "
                    + "(script in http://localhost:" + PORT + "/ from (1, 58) to (1, 81)#1)",
                    scriptExceptions.toString());
            assertEquals("http://localhost:" + PORT + "/second/, "
                    + "com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException: "
                    + "500 BOOM for http://localhost:" + PORT + "/second/", loadScriptErrors.toString());
            assertEquals("unknown://nowhere, java.net.MalformedURLException: unknown protocol: unknown",
                    malformedScriptURLErrors.toString());
            assertEquals("Timeout allowed: 100", timeoutErrors.toString());
        }
        finally {
            Locale.setDefault(locale);
        }
    }

    /**
     * Test for running with a JavaScript error listener.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void parsingError() throws Exception {
        final StringBuilder scriptExceptions = new StringBuilder();

        final WebClient webClient = getWebClientWithMockWebConnection();
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {

            public void loadScriptError(final HtmlPage htmlPage, final URL scriptUrl, final Exception exception) {
                // nothing
            }

            public void malformedScriptURL(final HtmlPage htmlPage, final String url,
                    final MalformedURLException malformedURLException) {
                // nothing
            }

            public void scriptException(final HtmlPage htmlPage, final ScriptException scriptException) {
                scriptExceptions.append(scriptException.getCause() + "\n");
            }

            public void timeoutError(final HtmlPage htmlPage, final long allowedTime, final long executionTime) {
                // nothing
            }
        });

        final String html = "<html><body><script>while (</script></body></html>";
        getMockWebConnection().setDefaultResponse(html);
        webClient.getPage(getDefaultUrl());

        assertEquals("net.sourceforge.htmlunit.corejs.javascript.EvaluatorException: "
            + "Unexpected end of file (script in " + getDefaultUrl() + " from (1, 21) to (1, 37)#1)\n",
                scriptExceptions.toString());
    }
}
