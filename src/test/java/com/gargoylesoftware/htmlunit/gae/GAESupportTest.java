/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.gae;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Tests for <a href="http://code.google.com/appengine/">Google App Engine</a>
 * support. Tests are run through the {@link GAETestRunner} which tries to enforce (some of) GAE rules
 * like for instance class white list.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(GAETestRunner.class)
public class GAESupportTest {

    /**
     * Test that the test runner prohibits loading of some classes like
     * {@link java.net.URLStreamHandler}.
     */
    @Test(expected = NoClassDefFoundError.class)
    public void whitelist() {
        new com.gargoylesoftware.htmlunit.protocol.javascript.Handler();
    }

    /**
     * Simulates GAE white list restrictions. Fails as of HtmlUnit-2.7 due to
     * usage of java.net.URLStreamHandler (and problably other classes).
     * @throws Exception if the test fails
     */
    @Test
    public void instantiation() throws Exception {
        new WebClient();
        assertEquals("http://gaeHack_about/blank", WebClient.URL_ABOUT_BLANK.toString());
    }
}
