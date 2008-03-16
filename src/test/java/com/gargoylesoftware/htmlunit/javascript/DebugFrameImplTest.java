/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
     * @throws Exception If the test fails
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
