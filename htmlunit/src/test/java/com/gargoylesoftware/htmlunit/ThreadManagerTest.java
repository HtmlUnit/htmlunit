/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for ThreadManager
 * 
 * @version  $Revision$
 * @author Brad Clarke
 *
 */
public class ThreadManagerTest extends WebTestCase {
    /**
     * Make a test
     * @param name test name
     */
    public ThreadManagerTest(final String name) {
        super(name);
    }

    private long startTime_;
    private void startTimedTest() {
        startTime_ = System.currentTimeMillis();
    }
    private void assertMaxTestRunTime(final long maxRunTimeMilliseconds) {
        final long endTime = System.currentTimeMillis();
        final long runTime = endTime - startTime_;
        assertTrue("\nTest took too long to run and results may not be accurate. Please try again. "
            + "\n  Actual Run Time: "
            + runTime
            + "\n  Max Run Time: "
            + maxRunTimeMilliseconds, runTime < maxRunTimeMilliseconds);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testSetClearTimeoutUsesManager() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var threadID;\n"
            + "    function test() {\n"
            + "      threadID = setTimeout('doAlert()', 10000);\n"
            + "    }\n"
            + "    function doAlert() {\n"
            + "      alert('blah');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<a onclick='clearTimeout(threadID);' id='clickme'/>"
            + "</body>\n"
            + "</html>";

        final List collectedAlerts = Collections.synchronizedList(new ArrayList());
        startTimedTest();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final ThreadManager threadManager = page.getEnclosingWindow().getThreadManager();
        assertNotNull(threadManager);
        assertEquals(1, threadManager.activeCount());
        final HtmlAnchor a = (HtmlAnchor) page.getHtmlElementById("clickme");
        a.click();
        assertEquals(0, threadManager.activeCount());
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        assertMaxTestRunTime(10000);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testSetClearIntervalUsesManager() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var threadID;\n"
            + "    function test() {\n"
            + "      threadID = setInterval('doAlert()', 100);\n"
            + "    }\n"
            + "    var iterationNumber=0;"
            + "    function doAlert() {\n"
            + "      alert('blah');\n"
            + "      if (++iterationNumber >= 3) {"
            + "        clearInterval(threadID);"
            + "      }"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final List collectedAlerts = Collections.synchronizedList(new ArrayList());
        startTimedTest();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final ThreadManager threadManager = page.getEnclosingWindow().getThreadManager();
        assertNotNull(threadManager);
        assertEquals(1, threadManager.activeCount());
        threadManager.joinAll(1000);
        assertEquals(0, threadManager.activeCount());
        assertEquals(Collections.nCopies(3, "blah"), collectedAlerts);
        assertMaxTestRunTime(1000);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testNavigationStopThreadsInChildWindows() throws Exception {
        final String firstContent = "<html><head><title>First</title></head><body>"
            + "<iframe id='iframe1' src='"
            + URL_SECOND.toExternalForm()
            + "'>"
            + "<a href='"
            + URL_THIRD.toExternalForm()
            + "' id='clickme'>click me</a>"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body>"
            + "<script>"
            + "setInterval('', 10000);"
            + "</script>"
            + "</body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlInlineFrame iframe = (HtmlInlineFrame) page.getHtmlElementById("iframe1");
        final ThreadManager innerThreadManager = iframe.getEnclosedWindow().getThreadManager();
        assertEquals("inner frame should show child thread", 1, innerThreadManager.activeCount());

        final HtmlAnchor anchor = (HtmlAnchor) page.getHtmlElementById("clickme");
        final HtmlPage newPage = (HtmlPage) anchor.click();

        assertEquals("new page should load", "Third", newPage.getTitleText());
        assertEquals("frame should be gone", 0, newPage.getFrames().size());
        // this thread manager really is not accessible anymore, but this is a unit test
        assertEquals("thread should stop", 0, innerThreadManager.activeCount());
    }
}
