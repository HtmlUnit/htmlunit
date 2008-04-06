/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

        final HtmlElement doc = page.getDocumentHtmlElement();
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
