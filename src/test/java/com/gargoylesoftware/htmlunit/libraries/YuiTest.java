/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
 */
public class YuiTest extends WebTestCase {

    private static final long DEFAULT_TIME_TO_WAIT = 2 * 60 * 1000L;
    private static final String BASE_FILE_PATH = "yui/2.3.0/tests/";

    /**
     * Creates an instance.
     *
     * @param name The name of the test.
     */
    public YuiTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testLogger() throws Exception {
        doTest(BrowserVersion.FIREFOX_2, "logger.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testAnimation() throws Exception {
        final List l = new ArrayList();
        l.add("test_onStart");
        doTest(BrowserVersion.FIREFOX_2, "animation.html", l);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testTabView() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        // The tabview YUI test has a background thread that runs.  We want to set the
        // maximum wait time to 5 seconds as that gives enough time for execution without
        // causing the test to run forever.
        doTest(BrowserVersion.FIREFOX_2, "tabview.html", Collections.EMPTY_LIST, null, 5 * 1000);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testDateMath() throws Exception {
        doTest(BrowserVersion.FIREFOX_2, "datemath.html", Collections.EMPTY_LIST, "btnRun");
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testYUI() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_2, "YUI.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testCalendar() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_2, "calendar.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testColorPicker() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_2, "colorpicker.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testConfig() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        // Test currently commented out as there are problems with the YUI test.
        // A bug has been filed against YUI regarding the problems with the test.
        // See http://sourceforge.net/tracker/index.php?func=detail&aid=1788014&group_id=165715&atid=836476
        // for more details.
        fail("YUI test has a bug that causes this to fail.");
        //doTest(BrowserVersion.FIREFOX_2, "config.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testDataSource() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_2, "datasource.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testDataTable() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_2, "datatable.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testDom() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_2, "dom.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testDragDrop() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_2, "dragdrop.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testEditor() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_2, "editor.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testYuiLoaderRollup() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_2, "yuiloader_rollup.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testYuiLoaderConfig() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_2, "yuiloader_config.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testYuiLoader() throws Exception {
        doTest(BrowserVersion.FIREFOX_2, "yuiloader.html", Collections.singletonList("test_calculate"));
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testModule() throws Exception {
        doTest(BrowserVersion.FIREFOX_2, "module.html", Collections.singletonList("testConstructor"));
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testImageLoader() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        doTest(BrowserVersion.FIREFOX_2, "imageloader.html", Collections.EMPTY_LIST);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testElement() throws Exception {
        doTest(BrowserVersion.FIREFOX_2, "element.html", Collections.singletonList("test_DomEvent"));
    }

    /**
     * TODO: get rid of the known failing test list, eventually
     */
    private void doTest(final BrowserVersion version, final String fileName, final List knownFailingTests)
        throws Exception {
        doTest(version, fileName, knownFailingTests, null);
    }

    /**
     * TODO: get rid of the known failing test list, eventually
     */
    private void doTest(final BrowserVersion version, final String fileName, final List knownFailingTests,
            final String buttonToPush) throws Exception {
        doTest(version, fileName, knownFailingTests, buttonToPush, DEFAULT_TIME_TO_WAIT);
    }

    /**
     * TODO: get rid of the known failing test list, eventually
     */
    private void doTest(final BrowserVersion version, final String fileName, final List knownFailingTests,
            final String buttonToPush, final long timeToWait) throws Exception {

        final URL url = getClass().getClassLoader().getResource(BASE_FILE_PATH + fileName);
        assertNotNull(url);

        final WebClient client = new WebClient(version);
        final HtmlPage page = (HtmlPage) client.getPage(url);
        final HtmlElement doc = page.getDocumentHtmlElement();

        if (buttonToPush != null) {
            final HtmlButtonInput button = ((HtmlButtonInput) page.getHtmlElementById(buttonToPush));
            button.click();
        }

        page.getEnclosingWindow().getThreadManager().joinAll(timeToWait);

        final List tests = doc.getByXPath("//span[@class='pass' or @class='fail']");
        if (tests.size() == 0) {
            fail("No tests were executed!");
        }

        for (final Iterator i = tests.iterator(); i.hasNext();) {
            final HtmlSpan span = (HtmlSpan) i.next();
            final String testResult = span.getNextDomSibling().asText();
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

}
