/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.fetch;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

/**
 * Tests for the warnings about requests blocked by the CORS checks
 * (logger {@value CorsLoader#LOGGER_NAME}).
 *
 * @author Ronald Brill
 */
public class CorsLoggingTest extends SimpleWebTestCase {

    private static final String ORIGIN = "http://localhost:" + PORT;

    private final Logger logger_ = (Logger) LoggerFactory.getLogger(CorsLoader.LOGGER_NAME);
    private ListAppender<ILoggingEvent> appender_;
    private Level originalLevel_;

    /**
     * Captures the log messages (the test configuration logs only errors of HtmlUnit).
     */
    @BeforeEach
    public void addAppender() {
        appender_ = new ListAppender<>();
        appender_.start();
        logger_.addAppender(appender_);
        // no output on the console
        logger_.setAdditive(false);
        originalLevel_ = logger_.getLevel();
        logger_.setLevel(Level.WARN);
    }

    /**
     * Removes the appender and restores the level.
     */
    @AfterEach
    public void removeAppender() {
        logger_.setLevel(originalLevel_);
        logger_.setAdditive(true);
        logger_.detachAppender(appender_);
        appender_.stop();
    }

    private List<String> getMessages() {
        final List<String> messages = new ArrayList<>();
        for (final ILoggingEvent event : appender_.list) {
            messages.add(event.getLevel() + " " + event.getFormattedMessage());
        }
        return messages;
    }

    private static URL crossOriginUrl(final String path) throws Exception {
        return new URL(URL_CROSS_ORIGIN2, path);
    }

    private void setCrossOriginResponse(final String path, final int status, final String... headers)
            throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        for (int i = 0; i < headers.length; i += 2) {
            responseHeaders.add(new NameValuePair(headers[i], headers[i + 1]));
        }
        getMockWebConnection().setResponse(crossOriginUrl(path), "data", status, "Status",
                MimeType.TEXT_PLAIN, responseHeaders);
    }

    private void sendXhr(final String method, final String url, final boolean withCredentials,
            final String header) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('" + method + "', '" + url + "', false);\n"
            + "    xhr.withCredentials = " + withCredentials + ";\n"
            + (header == null ? "" : "    xhr.setRequestHeader('" + header + "', 'value');\n")
            + "    xhr.send();\n"
            + "  } catch(e) {}\n"
            + "</script></head><body></body></html>";

        loadPage(html);
    }

    private static String blocked(final String initiator, final String url, final String reason) {
        return "WARN Access to " + initiator + " at '" + url + "' from origin '" + ORIGIN
                + "' has been blocked by CORS policy: " + reason;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void allowed() throws Exception {
        setCrossOriginResponse("data", 200, "Access-Control-Allow-Origin", "*");
        sendXhr("GET", crossOriginUrl("data").toExternalForm(), false, null);

        assertEquals(new ArrayList<>(), getMessages());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void noAllowOriginHeader() throws Exception {
        setCrossOriginResponse("data", 200);
        sendXhr("GET", crossOriginUrl("data").toExternalForm(), false, null);

        assertEquals(List.of(blocked("XMLHttpRequest", crossOriginUrl("data").toExternalForm(),
                "No 'Access-Control-Allow-Origin' header is present on the requested resource.")),
                getMessages());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void otherAllowOrigin() throws Exception {
        setCrossOriginResponse("data", 200, "Access-Control-Allow-Origin", "http://www.example.com");
        sendXhr("GET", crossOriginUrl("data").toExternalForm(), false, null);

        assertEquals(List.of(blocked("XMLHttpRequest", crossOriginUrl("data").toExternalForm(),
                "The 'Access-Control-Allow-Origin' header has a value 'http://www.example.com'"
                + " that is not equal to the supplied origin.")),
                getMessages());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void wildcardWithCredentials() throws Exception {
        setCrossOriginResponse("data", 200, "Access-Control-Allow-Origin", "*");
        sendXhr("GET", crossOriginUrl("data").toExternalForm(), true, null);

        assertEquals(List.of(blocked("XMLHttpRequest", crossOriginUrl("data").toExternalForm(),
                "The value of the 'Access-Control-Allow-Origin' header in the response must not be"
                + " the wildcard '*' when the request's credentials mode is 'include'.")),
                getMessages());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void allowCredentialsMissing() throws Exception {
        setCrossOriginResponse("data", 200, "Access-Control-Allow-Origin", ORIGIN);
        sendXhr("GET", crossOriginUrl("data").toExternalForm(), true, null);

        assertEquals(List.of(blocked("XMLHttpRequest", crossOriginUrl("data").toExternalForm(),
                "The value of the 'Access-Control-Allow-Credentials' header in the response is ''"
                + " which must be 'true' when the request's credentials mode is 'include'.")),
                getMessages());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void preflightStatus() throws Exception {
        setCrossOriginResponse("data", 500, "Access-Control-Allow-Origin", "*");
        sendXhr("PUT", crossOriginUrl("data").toExternalForm(), false, null);

        assertEquals(List.of(blocked("XMLHttpRequest", crossOriginUrl("data").toExternalForm(),
                "Response to preflight request doesn't pass access control check:"
                + " It does not have HTTP ok status (500).")),
                getMessages());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void preflightNoAllowOriginHeader() throws Exception {
        setCrossOriginResponse("data", 200);
        sendXhr("PUT", crossOriginUrl("data").toExternalForm(), false, null);

        assertEquals(List.of(blocked("XMLHttpRequest", crossOriginUrl("data").toExternalForm(),
                "Response to preflight request doesn't pass access control check:"
                + " No 'Access-Control-Allow-Origin' header is present on the requested resource.")),
                getMessages());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void preflightMethodNotAllowed() throws Exception {
        setCrossOriginResponse("data", 200, "Access-Control-Allow-Origin", "*",
                "Access-Control-Allow-Methods", "POST, DELETE");
        sendXhr("PUT", crossOriginUrl("data").toExternalForm(), false, null);

        assertEquals(List.of(blocked("XMLHttpRequest", crossOriginUrl("data").toExternalForm(),
                "Method PUT is not allowed by Access-Control-Allow-Methods in preflight response.")),
                getMessages());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void preflightHeaderNotAllowed() throws Exception {
        setCrossOriginResponse("data", 200, "Access-Control-Allow-Origin", "*",
                "Access-Control-Allow-Headers", "x-other");
        sendXhr("GET", crossOriginUrl("data").toExternalForm(), false, "X-Custom");

        assertEquals(List.of(blocked("XMLHttpRequest", crossOriginUrl("data").toExternalForm(),
                "Request header field x-custom is not allowed by Access-Control-Allow-Headers"
                + " in preflight response.")),
                getMessages());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void redirected() throws Exception {
        final URL redirectUrl = new URL(URL_FIRST, "redirect");
        getMockWebConnection().setResponse(redirectUrl, "", 302, "Found", MimeType.TEXT_PLAIN,
                List.of(new NameValuePair("Location", crossOriginUrl("data").toExternalForm())));
        setCrossOriginResponse("data", 200);
        sendXhr("GET", redirectUrl.toExternalForm(), false, null);

        assertEquals(List.of("WARN Access to XMLHttpRequest at '" + crossOriginUrl("data")
                + "' (redirected from '" + redirectUrl + "') from origin '" + ORIGIN
                + "' has been blocked by CORS policy:"
                + " No 'Access-Control-Allow-Origin' header is present on the requested resource."),
                getMessages());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void fetch() throws Exception {
        setCrossOriginResponse("data", 200);
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "  fetch('" + crossOriginUrl("data") + "').catch(function(e) {});\n"
            + "</script></head><body></body></html>";

        loadPage(html);
        getWebClient().waitForBackgroundJavaScript(DEFAULT_WAIT_TIME.toMillis());

        assertEquals(List.of(blocked("fetch", crossOriginUrl("data").toExternalForm(),
                "No 'Access-Control-Allow-Origin' header is present on the requested resource.")),
                getMessages());
    }

    /**
     * Requests in mode 'no-cors' are not checked, nothing is logged.
     * @throws Exception if the test fails
     */
    @Test
    public void fetchNoCors() throws Exception {
        setCrossOriginResponse("data", 200);
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "  fetch('" + crossOriginUrl("data") + "', { mode: 'no-cors' }).catch(function(e) {});\n"
            + "</script></head><body></body></html>";

        loadPage(html);
        getWebClient().waitForBackgroundJavaScript(DEFAULT_WAIT_TIME.toMillis());

        assertEquals(new ArrayList<>(), getMessages());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void image() throws Exception {
        setCrossOriginResponse("img.png", 200);
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <img id='i' src='" + crossOriginUrl("img.png") + "' crossorigin>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        ((HtmlImage) page.getElementById("i")).getWebResponse(true);

        assertEquals(List.of(blocked("image", crossOriginUrl("img.png").toExternalForm(),
                "No 'Access-Control-Allow-Origin' header is present on the requested resource.")),
                getMessages());
    }
}
