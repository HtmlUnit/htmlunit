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
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Abstract base class for <a href="http://jquery.com/">jQuery JavaScript library</a> compatibility tests.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public abstract class JQueryTestBase extends WebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void ie6() throws Exception {
        final Iterator<HtmlElement> i = loadPage(BrowserVersion.INTERNET_EXPLORER_6_0);
        verify(i, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void ie7() throws Exception {
        final Iterator<HtmlElement> i = loadPage(BrowserVersion.INTERNET_EXPLORER_7_0);
        verify(i, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void firefox2() throws Exception {
        final Iterator<HtmlElement> i = loadPage(BrowserVersion.FIREFOX_2);
        verify(i, false);
    }

    /**
     * Returns the jQuery version being tested.
     *
     * @return the jQuery version being tested
     */
    protected abstract String getVersion();

    /**
     * Verifies that the specified test result iterator contains the expected results.
     *
     * @param i the test result iterator
     * @param ie whether or not the browser used was MSIE
     * @throws Exception if an error occurs
     */
    protected abstract void verify(final Iterator<HtmlElement> i, final boolean ie) throws Exception;

    /**
     * Loads the jQuery unit test index page using the specified browser version, allows its
     * JavaScript to run to completion, and returns a list item iterator containing the test
     * results.
     *
     * @param version the browser version to use
     * @return a list item iterator containing the test results
     * @throws Exception if an error occurs
     */
    protected Iterator<HtmlElement> loadPage(final BrowserVersion version) throws Exception {
        final String resource = "jquery/" + getVersion() + "/test/index.html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);

        final WebClient client = new WebClient(version);

        final HtmlPage page = (HtmlPage) client.getPage(url);
        page.getEnclosingWindow().getThreadManager().joinAll(2 * 60 * 1000);

        // dump the result page if not OK
        if (System.getProperty(PROPERTY_GENERATE_TESTPAGES) != null) {
            final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            final File f = new File(tmpDir, "jquery" + getVersion() + "_result.html");
            FileUtils.writeStringToFile(f, page.asXml(), "UTF-8");
            getLog().info("Test result for " + getVersion() + " written to: " + f.getAbsolutePath());
        }

        final HtmlElement doc = page.getDocumentElement();
        final HtmlOrderedList tests = (HtmlOrderedList) doc.getHtmlElementById("tests");
        final Iterable<HtmlElement> i = tests.getChildElements();
        return i.iterator();
    }

    /**
     * Verifies that the next test group result list item has the specified name, the specified
     * number of failed tests and the specified number of passed tests.
     *
     * @param i the iterator whose next element is the list item to be checked
     * @param name the expected test group name
     * @param failed the expected number of failed unit tests
     * @param passed the expected number of passed unit tests
     * @throws Exception if an error occurs
     */
    protected void ok(final Iterator<HtmlElement> i, final String name, final int failed, final int passed)
        throws Exception {
        final HtmlListItem li = (HtmlListItem) i.next();
        final String n = li.getFirstByXPath(".//text()").toString().trim();
        assertEquals(name, n);

        final int f = Integer.parseInt(li.getFirstByXPath(".//b[@class='fail']/text()").toString());
        final int p = Integer.parseInt(li.getFirstByXPath(".//b[@class='pass']/text()").toString());
        if (f != failed || p != passed) {
            fail("Expected " + passed + " passed and " + failed + " failed for test: " + li.asText());
        }
    }

}
