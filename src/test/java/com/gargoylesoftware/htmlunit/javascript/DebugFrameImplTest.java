/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DebugFrameImpl}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DebugFrameImplTest extends WebTestCase {

    private final Logger loggerDebugFrameImpl_ = Logger.getLogger(DebugFrameImpl.class);

    private Level originalLogLevel_;

    /**
     * Prepares the log to trigger problematic code.
     * @throws Exception when a problem occurs
     */
    @Before
    public void setUp() throws Exception {
        HtmlUnitContextFactory.setDebuggerEnabled(true);
        
        originalLogLevel_ = loggerDebugFrameImpl_.getLevel();
        loggerDebugFrameImpl_.setLevel(Level.TRACE);
    }
    
    /**
     * Resets the log to its original state.
     * @throws Exception when a problem occurs
     */
    @After
    public void tearDown() throws Exception {
        HtmlUnitContextFactory.setDebuggerEnabled(false);
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
        
        loadPage(BrowserVersion.FIREFOX_2, content, new ArrayList<String>());
    }

}
