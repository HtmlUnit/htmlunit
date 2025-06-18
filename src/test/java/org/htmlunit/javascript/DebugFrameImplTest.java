/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import java.io.StringWriter;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.WriterAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.htmlunit.BrowserVersion;
import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.WebConnection;
import org.junit.After;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DebugFrameImpl}.
 *
 * @author Marc Guillemot
 */
public class DebugFrameImplTest extends SimpleWebTestCase {

    private final Logger loggerDebugFrameImpl_ = (Logger) LogManager.getLogger(DebugFrameImpl.class);

    private Level originalLogLevel_;
    private WebClient client_;

    /**
     * Constructor.
     * @throws Exception if an exception occurs
     */
    public DebugFrameImplTest() throws Exception {
        client_ = new WebClient(BrowserVersion.FIREFOX);
        ((JavaScriptEngine) client_.getJavaScriptEngine()).getContextFactory().setDebugger(new DebuggerImpl());

        originalLogLevel_ = loggerDebugFrameImpl_.getLevel();
        loggerDebugFrameImpl_.setLevel(Level.TRACE);
    }

    /**
     * Cleans up the client, and resets the log to its original state.
     * @throws Exception when a problem occurs
     */
    @After
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

        final StringWriter stringWriter = new StringWriter();
        final PatternLayout layout = PatternLayout.newBuilder().withPattern("%msg%n").build();

        final WriterAppender writerAppender = WriterAppender.newBuilder().setName("writeLogger").setTarget(stringWriter)
                .setLayout(layout).build();
        writerAppender.start();

        loggerDebugFrameImpl_.addAppender(writerAppender);
        try {
            client_.getPage(url);
        }
        finally {
            loggerDebugFrameImpl_.removeAppender(writerAppender);
        }

        assertEquals(expectedLog, stringWriter.toString());
    }
}
