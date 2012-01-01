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
package com.gargoylesoftware.htmlunit.source;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
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
public class JQueryExtractorSample extends WebTestCase {

    private static final Log LOG = LogFactory.getLog(JQueryExtractorSample.class);

    private static Iterator<HtmlElement> ITERATOR_;
    private static BrowserVersion BROWSER_VERSION_;
    private HtmlListItem listItem_;
    private int itemIndex_;

    /**
     * Before.
     */
    @Before
    public void init() {
        if (getBrowserVersion() != BROWSER_VERSION_) {
            try {
                ITERATOR_ = loadPage();
                BROWSER_VERSION_ = getBrowserVersion();
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        listItem_ = (HtmlListItem) ITERATOR_.next();
        itemIndex_ = 0;
    }

    private void assertResult(final String expectedTestResult) {
        final String actual = ((HtmlElement) ((List<?>) listItem_.getByXPath("./strong")).get(0)).asText();
        assertEquals(expectedTestResult, actual);
    }

    private void assertAssertion(final String expectedAssertion) {
        final String actual = ((HtmlListItem) ((List<?>) listItem_.getByXPath("./ol/li")).get(itemIndex_++)).asText();
        assertEquals(expectedAssertion, actual);
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
        client.waitForBackgroundJavaScript(2 * 60 * 1000);

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
        final Iterable<HtmlElement> i = tests.getChildElements();
        return i.iterator();
    }

    /**
     * Returns the jQuery version being tested.
     * @return the jQuery version being tested
     */
    protected String getVersion() {
        return "1.2.6";
    }
}
