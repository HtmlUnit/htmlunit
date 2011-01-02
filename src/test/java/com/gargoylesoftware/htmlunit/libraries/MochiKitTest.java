/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for compatibility with <a href="http://mochikit.com">MochiKit</a>.
 * <p>
 * Note: the tests test_MochiKit-DOM-Safari.html, test_MochiKit-DragAndDrop.html and test_MochiKit-JSAN.html
 * are not run as they don't even pass in a "real" Firefox 3.
 * </p>
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class MochiKitTest extends LibraryTestCase {

    private static final String BASE_FILE_PATH = "libraries/MochiKit/1.4.1";
    private WebClient webClient_;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getLibraryDir() {
        return BASE_FILE_PATH;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void async() throws Exception {
        doTest("Async");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void base() throws Exception {
        doTest("Base");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void color() throws Exception {
        doTest("Color");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void dateTime() throws Exception {
        doTest("DateTime");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void DOM() throws Exception {
        doTest("DOM");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void format() throws Exception {
        doTest("Format");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void iter() throws Exception {
        doTest("Iter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void logging() throws Exception {
        doTest("Logging");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void mochiKit() throws Exception {
        doTest("MochiKit");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void selector() throws Exception {
        doTest("Selector");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void signal() throws Exception {
        doTest("Signal");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void style() throws Exception {
        doTest("Style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void visual() throws Exception {
        doTest("Visual");
    }

    private void doTest(final String testName) throws Exception {
        final URL url = getClass().getClassLoader().getResource(BASE_FILE_PATH
            + "/tests/test_MochiKit-" + testName + ".html");
        assertNotNull(url);

        webClient_ = getWebClient();
        final HtmlPage page = webClient_.getPage(url);
        webClient_.waitForBackgroundJavaScriptStartingBefore(2000);

        // make single test results visible
        ((HtmlElement) page.getFirstByXPath("//a[text() = 'Toggle passed tests']")).click();
        ((HtmlElement) page.getFirstByXPath("//a[text() = 'Toggle failed tests']")).click();

        final String expected = loadExpectation("test-" + testName);
        final HtmlDivision div = page.getFirstByXPath("//div[@class = 'tests_report']");

        assertNotNull(div);
        assertEquals(expected.trim(), div.asText().trim());
    }

    /**
     * Closes the open windows.
     */
    @After
    public void tearDown() {
        if (webClient_ != null) {
            webClient_.closeAllWindows();
        }
    }

}
