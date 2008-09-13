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
package com.gargoylesoftware.htmlunit.libraries;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpWebConnectionTest;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for compatibility with version 1.5.0-rc1 of
 * <a href="http://prototype.conio.net/">Prototype JavaScript library</a>.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class Prototype150rc1Test extends WebTestCase {

    private Server server_;

    /**
     * @throws Exception if test fails
     */
    @Test
    public void ajax() throws Exception {
        final String filename = "ajax.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 3, 11, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 3, 11, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void array() throws Exception {
        final String filename = "array.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 12, 49, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 12, 49, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void base() throws Exception {
        final String filename = "base.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 4, 48, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 4, 48, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void testDom() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String filename = "dom.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 25, 253, 1, 0);
        test(BrowserVersion.FIREFOX_2, filename, 25, 254, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void elementMixins() throws Exception {
        final String filename = "element_mixins.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 4, 7, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 4, 7, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void enumerable() throws Exception {
        final String filename = "enumerable.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename,  23, 67, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename,  23, 67, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void form() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String filename = "form.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 4, 21, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 4, 21, 0, 0);
    }

    /**
     * Blocked by Rhino bug 370279 (https://bugzilla.mozilla.org/show_bug.cgi?id=370279).
     * @throws Exception if test fails
     */
    @Test
    public void hash() throws Exception {
        final String filename = "hash.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 5, 19, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 5, 19, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void position() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String filename = "position.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 5, 25, 3, 0);
        test(BrowserVersion.FIREFOX_2, filename, 5, 28, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void range() throws Exception {
        final String filename = "range.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 6, 21, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 6, 21, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void selector() throws Exception {
        final String filename = "selector.html";
        //HtmlUnit with IE succeeds for all :)
        // It should be test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 18, 35, 9, 1);
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 18, 46, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 18, 46, 0, 0);
    }

    /**
     * Blocked by Rhino bug 369860 (https://bugzilla.mozilla.org/show_bug.cgi?id=369860).
     * @throws Exception if test fails
     */
    @Test
    public void string() throws Exception {
        final String filename = "string.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 19, 76, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 19, 76, 0, 0);
    }

    private void test(final BrowserVersion browserVersion, final String filename, final int tests,
            final int assertions, final int failures, final int errors) throws Exception {
        final WebClient client = new WebClient(browserVersion);
        final HtmlPage page =
            client.getPage("http://localhost:" + HttpWebConnectionTest.PORT + "/test/unit/" + filename);

        page.getEnclosingWindow().getThreadManager().joinAll(25000);

        final String summary = page.<HtmlElement>getHtmlElementById("logsummary").asText();
        final String expected = tests + " tests, " + assertions + " assertions, " + failures + " failures, "
             + errors + " errors";
        assertEquals(expected, summary);
    }

    /**
     * Performs pre-test initialization.
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        server_ = HttpWebConnectionTest.startWebServer("src/test/resources/prototype/1.5.0-rc1");
    }

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        HttpWebConnectionTest.stopWebServer(server_);
        server_ = null;
    }
}
