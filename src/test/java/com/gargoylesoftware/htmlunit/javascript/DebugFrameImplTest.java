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

import java.io.StringWriter;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.junit.After;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link DebugFrameImpl}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DebugFrameImplTest extends SimpleWebTestCase {

    private final Logger loggerDebugFrameImpl_ = Logger.getLogger(DebugFrameImpl.class);

    private Level originalLogLevel_;
    private WebClient client_;

    /**
     * Constructor.
     * @throws Exception if an exception occurs
     */
    public DebugFrameImplTest() throws Exception {
        client_ = new WebClient(BrowserVersion.FIREFOX_3_6);
        client_.getJavaScriptEngine().getContextFactory().setDebugger(new DebuggerImpl());
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
        client_.closeAllWindows();
        loggerDebugFrameImpl_.setLevel(originalLogLevel_);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void withCallable() throws Exception {
        final String content = "<html><head><title>debug test</title>"
            + "<script>"
            + "var counter = 0;"
            + "window.__defineGetter__('foo', function(a) { return counter++ });"
            + "alert(window.foo);"
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
        final String expectedLog = IOUtils.toString(getClass().getResourceAsStream("debugFrameImplTest.txt"));

        final StringWriter sw = new StringWriter();
        final Layout layout = new PatternLayout("%m%n");
        final Appender appender = new WriterAppender(layout, sw);
        loggerDebugFrameImpl_.addAppender(appender);
        try {
            client_.getPage(url);
        }
        finally {
            loggerDebugFrameImpl_.removeAppender(appender);
        }

        assertEquals(expectedLog, sw.toString());
    }
}
