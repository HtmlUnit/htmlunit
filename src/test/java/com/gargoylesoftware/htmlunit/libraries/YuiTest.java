/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

/**
 * Tests for compatibility with the <a href="http://developer.yahoo.com/yui/">YUI JavaScript library</a>.
 *
 * TODO: add tests for IE6 and IE7
 *
 * @version $Revision$
 * @author Rob Di Marco
 * @author Daniel Gredler
 */
public class YuiTest extends WebTestCase {

    private static final long DEFAULT_TIME_TO_WAIT = 3 * 60 * 1000L;
    private static final String BASE_FILE_PATH = "yui/2.3.0/tests/";
    private final List<String> emptyList_ = Collections.emptyList();
    private WebClient client_;

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void logger() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "logger.html", emptyList_);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void animation() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "animation.html", emptyList_);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tabView() throws Exception {
        // The tabview YUI test has a background thread that runs. We want to set the
        // maximum wait time to 5 seconds as that gives enough time for execution without
        // causing the test to run forever.
        doTest(BrowserVersion.FIREFOX_3, "tabview.html", emptyList_, null, 5 * 1000);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dateMath() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "datemath.html", emptyList_, "btnRun");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void calendar() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "calendar.html", emptyList_, "btnRun");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void colorPicker() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "colorpicker.html", emptyList_);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void config() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        // Test currently commented out as there are problems with the YUI test.
        // A bug has been filed against YUI regarding the problems with the test.
        // See http://sourceforge.net/tracker/index.php?func=detail&aid=1788014&group_id=165715&atid=836476
        // for more details.
        fail("YUI test has a bug that causes this to fail.");
        //doTest(BrowserVersion.FIREFOX_3, "config.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dataSource() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "datasource.html", emptyList_, "btnRun");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dataTable() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "datatable.html", emptyList_, "btnRun");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dom() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "dom.html", emptyList_);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dragDrop() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "dragdrop.html", emptyList_);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void editor() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_3, "editor.html", emptyList_);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void yuiLoaderRollup() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_3, "yuiloader_rollup.html", emptyList_);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void yuiLoaderConfig() throws Exception {
        // The "test_page_modules" test fails in FF, too, so it's OK.
        doTest(BrowserVersion.FIREFOX_3, "yuiloader_config.html", Arrays.asList("test_page_modules"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void yuiLoader() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "yuiloader.html", emptyList_);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void module() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "module.html", Collections.singletonList("testConstructor"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void imageLoader() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "imageloader.html", emptyList_);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void element() throws Exception {
        doTest(BrowserVersion.FIREFOX_3, "element.html", emptyList_);
    }

    /**
     * TODO: get rid of the known failing test list, eventually
     */
    private void doTest(final BrowserVersion version, final String fileName, final List<String> knownFailingTests)
        throws Exception {
        doTest(version, fileName, knownFailingTests, null);
    }

    /**
     * TODO: get rid of the known failing test list, eventually
     */
    private void doTest(final BrowserVersion version, final String fileName, final List<String> knownFailingTests,
            final String buttonToPush) throws Exception {
        doTest(version, fileName, knownFailingTests, buttonToPush, DEFAULT_TIME_TO_WAIT);
    }

    /**
     * TODO: get rid of the known failing test list, eventually
     */
    private void doTest(final BrowserVersion version, final String fileName, final List<String> knownFailingTests,
            final String buttonToPush, final long timeToWait) throws Exception {

        final URL url = getClass().getClassLoader().getResource(BASE_FILE_PATH + fileName);
        assertNotNull(url);

        client_ = new WebClient(version);
        final HtmlPage page = (HtmlPage) client_.getPage(url);
        final HtmlElement doc = page.getDocumentElement();

        if (buttonToPush != null) {
            final HtmlButtonInput button = page.getHtmlElementById(buttonToPush);
            button.click();
        }

        client_.waitForBackgroundJavaScript(timeToWait);

        final List< ? > tests = doc.getByXPath("//span[@class='pass' or @class='fail']");
        if (tests.size() == 0) {
            fail("No tests were executed!");
        }

        for (final Iterator< ? > i = tests.iterator(); i.hasNext();) {
            final HtmlSpan span = (HtmlSpan) i.next();
            final String testResult = span.getNextSibling().asText();
            final int colonIdx = testResult.indexOf(":");
            assertTrue(colonIdx > 0 && colonIdx < testResult.length() - 1);
            final String result = span.asText();
            final String testName = testResult.substring(0, colonIdx).trim();
            if (result.equalsIgnoreCase("pass")) {
                assertTrue("Test case '" + testName + "' is in the known failing list, but passes!", !knownFailingTests
                                .contains(testName));
            }
            else {
                assertTrue("Test case '" + testName + "' is not in the known failing list, but fails!",
                                knownFailingTests.contains(testName));
            }
        }
    }

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        if (client_ != null) {
            client_.closeAllWindows();
        }
    }
}
