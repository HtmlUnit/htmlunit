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
package org.htmlunit.javascript;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.WebConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;

/**
 * Tests for {@link DebugFrameImpl}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class DebugFrameImplTest extends SimpleWebTestCase {

    private final Logger loggerDebugFrameImpl_ = (Logger) LoggerFactory.getLogger(DebugFrameImpl.class);

    private final Level originalLogLevel_;
    private final WebClient client_;

    /**
     * Constructor.
     * @throws Exception if an exception occurs
     */
    public DebugFrameImplTest() throws Exception {
        client_ = new WebClient(BrowserVersion.FIREFOX);
        client_.getJavaScriptEngine().getContextFactory().setDebugger(new DebuggerImpl());

        originalLogLevel_ = loggerDebugFrameImpl_.getLevel();
        loggerDebugFrameImpl_.setLevel(Level.TRACE);
    }

    /**
     * Cleans up the client, and resets the log to its original state.
     * @throws Exception when a problem occurs
     */
    @AfterEach
    public void tearDown() throws Exception {
        client_.getJavaScriptEngine().getContextFactory().setDebugger(null);
        client_.close();
        loggerDebugFrameImpl_.setLevel(originalLogLevel_);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void withCallable() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head><title>debug test</title>\n"
            + "<script>\n"
            + "  var counter = 0;\n"
            + "  window.__defineGetter__('foo', function(a) { return counter++ });\n"
            + "  alert(window.foo);\n"
            + "</script></head><body></body></html>";
        final WebConnection old = client_.getWebConnection();
        try {
            final MockWebConnection mock = new MockWebConnection();
            mock.setDefaultResponse(content);
            client_.setWebConnection(mock);
            client_.getPage(URL_FIRST);
        }
        finally {
            client_.setWebConnection(old);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    void loggedCalls() throws Exception {
        final URL url = getClass().getResource("debugFrameImplTest.html");
        final String expectedLog = IOUtils.toString(getClass().getResourceAsStream("debugFrameImplTest.txt"),
                ISO_8859_1);

        final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%msg%n");
        encoder.start();

        final OutputStreamAppender<ILoggingEvent> appender = new OutputStreamAppender<>();
        appender.setContext(context);
        appender.setEncoder(encoder);
        appender.setOutputStream(baos);
        appender.start();

        loggerDebugFrameImpl_.addAppender(appender);
        try {
            client_.getPage(url);
        }
        finally {
            loggerDebugFrameImpl_.detachAppender(appender);
            appender.stop();
        }

        assertEquals(expectedLog, baos.toString(ISO_8859_1));
    }
}
