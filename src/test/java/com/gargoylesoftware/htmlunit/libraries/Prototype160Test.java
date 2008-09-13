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

import java.io.File;

import org.apache.commons.io.FileUtils;
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
 * Tests for compatibility with version 1.6.0 of
 * <a href="http://prototype.conio.net/">Prototype JavaScript library</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class Prototype160Test extends WebTestCase {

    private Server server_;
    private WebClient client_;

    /**
     * @throws Exception if test fails
     */
    @Test
    public void ajax() throws Exception {
        final String filename = "ajax.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 15, 34, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 15, 32, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void array() throws Exception {
        final String filename = "array.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 19, 97, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 19, 97, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void base() throws Exception {
        final String filename = "base.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 35, 225, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 35, 225, 0, 0);
    }

    /**
     * Note: <tt>testElementGetDimensions:</tt>, <tt>testElementGetStyle</tt>, <tt>testElementGetHeight</tt>,
     *       <tt>testElementScrollTo:</tt>, <tt>testPositionedOffset</tt>, <tt>testViewportOffset</tt>,
     *       <tt>testViewportDimensions</tt>, <tt>testViewportScrollOffsets</tt>
     *       and <tt>testElementGetWidth</tt> are expected to fail with HtmlUnit,
     *       as they need calculating width and height of all elements.
     *
     * Other tests succeed.
     *
     * @throws Exception if test fails
     */
    @Test
    public void dom() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String filename = "dom.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 87, 822, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 87, 811, 0, 0);
    }

    /**
     * Depends on {@link com.gargoylesoftware.htmlunit.javascript.SimpleScriptableTest#parentProtoFeature()}.
     *
     * @throws Exception if test fails
     */
    @Test
    public void elementMixins() throws Exception {
        final String filename = "element_mixins.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 4, 12, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 4, 12, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void enumerable() throws Exception {
        final String filename = "enumerable.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename,  25, 82, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename,  25, 82, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void event() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String filename = "event.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename,  12, 43, 1, 0);
        test(BrowserVersion.FIREFOX_2, filename,  12, 44, 0, 0);
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
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 15, 110, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 15, 109, 0, 0);
    }

    /**
     * Fails due to bug in Rhino: "encodeURIComponent wrongly uses lowercase for hex representation".
     * https://bugzilla.mozilla.org/show_bug.cgi?id=429121
     * @throws Exception if test fails
     */
    @Test
    public void hash() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String filename = "hash.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 16, 87, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 16, 87, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void number() throws Exception {
        final String filename = "number.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 4, 20, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 4, 20, 0, 0);
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
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 2, 16, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 2, 16, 0, 0);
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
        if (notYetImplemented()) {
            return;
        }
        final String filename = "selector.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 37, 169, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 37, 171, 0, 0);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void string() throws Exception {
        final String filename = "string.html";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 40, 220, 0, 0);
        test(BrowserVersion.FIREFOX_2, filename, 40, 220, 0, 0);
    }

    /**
     * Depends on {@link com.gargoylesoftware.htmlunit.javascript.regexp.HtmlUnitRegExpProxyTest#test()}.
     * @throws Exception if test fails
     */
    @Test
    public void unitTests() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String filename = "unit_tests.html";
        //The expected failure is because the server port is other than 4711
        test(BrowserVersion.INTERNET_EXPLORER_7_0, filename, 10, 82, 1, 0);
        test(BrowserVersion.FIREFOX_2, filename, 10, 82, 1, 0);
    }

    private void test(final BrowserVersion browserVersion, final String filename, final int tests,
            final int assertions, final int failures, final int errors) throws Exception {
        client_ = new WebClient(browserVersion);
        final HtmlPage page =
            client_.getPage("http://localhost:" + HttpWebConnectionTest.PORT + "/test/unit/" + filename);
        page.getEnclosingWindow().getThreadManager().joinAll(25000);

        final String summary = page.<HtmlElement>getHtmlElementById("logsummary").asText();
        final String expected = tests + " tests, " + assertions + " assertions, " + failures + " failures, "
             + errors + " errors";

        // dump the result page if not ok
        if (System.getProperty(PROPERTY_GENERATE_TESTPAGES) != null && !expected.equals(summary)) {
            final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            final File f = new File(tmpDir, "prototype160_result_" + filename);
            FileUtils.writeStringToFile(f, page.asXml(), "UTF-8");
            getLog().info("Test result for " + filename + " written to: " + f.getAbsolutePath());
        }

        assertEquals(expected, summary);
    }

    /**
     * Performs pre-test initialization.
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        server_ = HttpWebConnectionTest.startWebServer("src/test/resources/prototype/1.6.0");
    }

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        HttpWebConnectionTest.stopWebServer(server_);
        server_ = null;
        client_.closeAllWindows();
    }
}
