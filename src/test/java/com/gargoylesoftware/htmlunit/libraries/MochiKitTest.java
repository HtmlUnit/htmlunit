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

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for compatibility with <a href="http://mochikit.com">MochiKit</a>.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class MochiKitTest extends LibraryTestCase {

    private static final String BASE_FILE_PATH = "MochiKit/1.4.1";

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
     * Currently one failure.
     * not ok - methodcaller with a function: got "[org.mozilla.javascript.UniqueTag@5fcf29: DOUBLE_MARK,
     * [2, 3]]", expected "[1, [2, 3]]"
     * This is due to <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=466661">Rhino bug 466661</a>.
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
    @NotYetImplemented // currently 8 failing out of 65
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

        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(url);
        page.getEnclosingWindow().getThreadManager().joinAll(20000);
        final String expected = loadExpectation("test-" + testName);
        final HtmlDivision div = page.getFirstByXPath("//div[@class = 'tests_report']");
        getLog().debug(page.asXml());
        assertNotNull(div);
        assertEquals(expected.trim(), div.asText().trim());
    }

}
