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
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for compatibility with version 1.2.6 of the <a href="http://jquery.com/">jQuery JavaScript library</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class JQuery126Test extends WebTestCase {

    private List<String> errors_;

    /**
     * Before.
     */
    @Before
    public void before() {
        errors_ = new ArrayList<String>();
    }

    /**
     * After.
     */
    @After
    public void after() {
        final StringBuilder sb = new StringBuilder();
        for (final String error : errors_) {
            sb.append('\n').append(error);
        }

        final int errorsNumber = errors_.size();
        if (errorsNumber == 1) {
            fail("Failure: " + sb);
        }
        else if (errorsNumber > 1) {
            fail(errorsNumber + " failures: " + sb);
        }
    }

    private void addFailure(final String error) {
        errors_.add(error);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented
    @SuppressWarnings("unchecked")
    public void test() throws Exception {
        final Iterator<HtmlElement> it = loadPage();
        final String resource = "jquery/" + getVersion() + "/expected." + getBrowserVersion().getNickname() + ".txt";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);
        final List<String> lines = FileUtils.readLines(new File(url.toURI()));
        final Iterator<String> expectedIterator = lines.iterator();
        while (it.hasNext()) {
            ok(it, expectedIterator);
        }
    }

    /**
     * {@inheritDoc}
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
        final String resource = "jquery/" + getVersion() + "/test/index.html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);

        final WebClient client = getWebClient();

        final HtmlPage page = client.getPage(url);
        page.getEnclosingWindow().getThreadManager().joinAll(2 * 60 * 1000);

        // dump the result page if not OK
        if (System.getProperty(PROPERTY_GENERATE_TESTPAGES) != null) {
            final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            final File f = new File(tmpDir,
                "jquery" + getVersion() + '_' + getBrowserVersion().getNickname() + "_result.html");
            FileUtils.writeStringToFile(f, page.asXml(), "UTF-8");
            getLog().info("Test result for "
                + getVersion() + '_' + getBrowserVersion().getNickname() + " written to: " + f.getAbsolutePath());
        }

        final HtmlElement doc = page.getDocumentElement();
        final HtmlOrderedList tests = (HtmlOrderedList) doc.getHtmlElementById("tests");
        final Iterable<HtmlElement> i = tests.getChildElements();
        FileUtils.writeStringToFile(new File("c:\\use\\jquery.out.txt"), page.asXml(), "UTF-8");
        return i.iterator();
    }

    /**
     * Verifies that the next test group result list item has the specified name, the specified
     * number of failed tests and the specified number of passed tests.
     *
     * @param iterator the iterator whose next element is the list item to be checked
     * @param expected the expected values
     * @throws Exception if an error occurs
     */
    @SuppressWarnings("unchecked")
    protected void ok(final Iterator<HtmlElement> iterator, final Iterator<String> expected) throws Exception {
        final HtmlListItem li = (HtmlListItem) iterator.next();
        String expectedStringLI = expected.next();
        if (!getBrowserVersion().isIE()) {
            expectedStringLI = expectedStringLI.substring(expectedStringLI.indexOf('.') + 2);
        }
        assertEquals(expectedStringLI, ((HtmlElement) ((List) li.getByXPath("./strong")).get(0)).asText());
        for (HtmlListItem item : (List<HtmlListItem>) li.getByXPath("./ol/li")) {
            String expectedString = expected.next();
            if (!getBrowserVersion().isIE()) {
                expectedString = expectedString.substring(expectedString.indexOf('.') + 2);
            }
            final String actualString = item.asText();
            if (!expectedString.equals(actualString)) {
                addFailure(new ComparisonFailure("", expectedString, actualString).getMessage());
            }
        }
    }
}
