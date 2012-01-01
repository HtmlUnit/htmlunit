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
package com.gargoylesoftware.htmlunit.libraries;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Base class for jQuery tests.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public abstract class JQueryTestBase extends WebServerTestCase {

    private static final Log LOG = LogFactory.getLog(JQueryTestBase.class);

    private WebClient client_;
    private List<String> failures_;

    /**
     * Before.
     */
    @Before
    public void before() {
        client_ = getWebClient();
        failures_ = new ArrayList<String>();
    }

    /**
     * After.
     * @throws Exception if an error occurs
     */
    @After
    public void after() throws Exception {
        client_.closeAllWindows();
        final StringBuilder sb = new StringBuilder();
        for (final String error : failures_) {
            sb.append('\n').append(error);
        }

        final int errorsNumber = failures_.size();
        if (errorsNumber == 1) {
            fail("Failure: " + sb);
        }
        else if (errorsNumber > 1) {
            fail(errorsNumber + " failures: " + sb);
        }
    }

    /**
     * Returns the path of the file which contains the test expectations.
     * @return the path of the file which contains the test expectations.
     * @throws Exception If an error happens
     */
    protected abstract String getExpectedPath() throws Exception;

    /**
     * Returns the page URL to load.
     * @return the path URL to load.
     */
    protected abstract String getUrl();

    /**
     * @throws Exception if an error occurs
     */
    protected void runTest() throws Exception {
        final Iterator<HtmlElement> it = loadPage();
        final List<String> lines = FileUtils.readLines(new File(getExpectedPath()), "UTF-8");
        if (lines.get(0).charAt(0) == 0xFEFF) {
            // The file has a UTF-8 BOM; remove it!
            // http://unicode.org/faq/utf_bom.html#BOM
            lines.add(0, lines.get(0).substring(1));
            lines.remove(1);
        }
        final Iterator<String> expectedIterator = lines.iterator();
        while (it.hasNext()) {
            ok(it, expectedIterator);
        }
        final String s = getNextExpectedModuleResult(expectedIterator);
        if (s != null) {
            fail("No result found for " + s + " (and following if any)");
        }
    }

    /**
     * Returns the jQuery version being tested.
     * @return the jQuery version being tested
     */
    protected String getVersion() {
        return "1.2.6";
    }

    /**
     * Loads the jQuery unit test index page using the specified browser version, allows its
     * JavaScript to run to completion, and returns a list item iterator containing the test
     * results.
     *
     * @return a list item iterator containing the test results
     * @throws Exception if an error occurs
     */
    protected Iterator<HtmlElement> loadPage() throws Exception {
        final HtmlPage page = client_.getPage(getUrl());

        client_.waitForBackgroundJavaScriptStartingBefore(4 * 60 * 1000);

        // dump the result page if not OK
        if (System.getProperty(PROPERTY_GENERATE_TESTPAGES) != null) {
            final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            final File f = new File(tmpDir,
                "jquery" + getVersion() + '_' + getBrowserVersion().getNickname() + "_result.html");
            FileUtils.writeStringToFile(f, page.asXml(), "UTF-8");
            LOG.info("Test result for "
                    + getVersion() + '_' + getBrowserVersion().getNickname()
                    + " written to: " + f.getAbsolutePath());
        }

        final HtmlElement doc = page.getDocumentElement();
        final HtmlOrderedList tests = (HtmlOrderedList) doc.getElementById("tests");
        final Iterator<HtmlElement> iter = tests.getChildElements().iterator();
        if (!iter.hasNext()) {
            fail("No result found");
        }

        return iter;
    }

    private String getNextExpectedModuleResult(final Iterator<String> expected) {
        if (!expected.hasNext()) {
            return null;
        }
        String s;
        do {
            if (!expected.hasNext()) {
                return null;
            }
            s = expected.next();
        } while(!s.contains("module: "));

        if (!getBrowserVersion().isIE()) {
            s = s.substring(s.indexOf('.') + 2);
        }
        return s;
    }

    /**
     * Verifies that the next test group result list item has the specified name, the specified
     * number of failed tests and the specified number of passed tests.
     *
     * @param iterator the iterator whose next element is the list item to be checked
     * @param expected the expected values
     * @throws Exception if an error occurs
     */
    protected void ok(final Iterator<HtmlElement> iterator, final Iterator<String> expected) throws Exception {
        final HtmlListItem li = (HtmlListItem) iterator.next();
        final String expectedLI = getNextExpectedModuleResult(expected);
        final String actualLI = li.<HtmlElement>getFirstByXPath("./strong").asText();
        if (!actualLI.equals(expectedLI)) {
            failures_.add(new ComparisonFailure("", expectedLI, actualLI).getMessage());
        }
    }

}
