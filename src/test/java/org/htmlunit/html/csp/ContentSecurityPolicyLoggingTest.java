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
package org.htmlunit.html.csp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
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
 * Tests for the warnings about Content-Security-Policy violations
 * (logger {@value ContentSecurityPolicy#LOGGER_NAME}).
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyLoggingTest extends SimpleWebTestCase {

    private final Logger logger_ = (Logger) LoggerFactory.getLogger(ContentSecurityPolicy.LOGGER_NAME);
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

    private List<String> loadWithPolicy(final String headerName, final String policy) throws Exception {
        final URL dataUrl = new URL(URL_CROSS_ORIGIN2, "data");
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "  try {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('GET', '" + dataUrl + "', false);\n"
            + "    xhr.send();\n"
            + "  } catch(e) {}\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML,
                List.of(new NameValuePair(headerName, policy)));
        getMockWebConnection().setResponse(dataUrl, "data", 200, "OK", MimeType.TEXT_PLAIN,
                List.of(new NameValuePair("Access-Control-Allow-Origin", "*")));

        final WebClient client = getWebClientWithMockWebConnection();
        client.getPage(URL_FIRST);

        final List<String> messages = new ArrayList<>();
        for (final ILoggingEvent event : appender_.list) {
            messages.add(event.getLevel() + " " + event.getFormattedMessage());
        }
        return messages;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void violation() throws Exception {
        final List<String> messages = loadWithPolicy("Content-Security-Policy", "connect-src 'self'");

        assertEquals(1, messages.size());
        assertTrue(messages.get(0),
                messages.get(0).startsWith("WARN Content-Security-Policy violation: 'connect-src' blocked '"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void violationReportOnly() throws Exception {
        final List<String> messages = loadWithPolicy("Content-Security-Policy-Report-Only", "connect-src 'self'");

        assertEquals(1, messages.size());
        assertTrue(messages.get(0), messages.get(0).startsWith(
                "WARN [Report Only] Content-Security-Policy violation: 'connect-src' blocked '"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void noViolation() throws Exception {
        final List<String> messages = loadWithPolicy("Content-Security-Policy", "connect-src *");

        assertEquals(new ArrayList<>(), messages);
    }
}
