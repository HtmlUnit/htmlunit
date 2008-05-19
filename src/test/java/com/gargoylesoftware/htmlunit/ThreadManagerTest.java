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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link ThreadManager}.
 *
 * @version $Revision$
 * @author Brad Clarke
 *
 */
public class ThreadManagerTest extends WebTestCase {
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
     * @throws Exception if the test fails
     */
    @Test
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
            + "<a onclick='clearTimeout(threadID);' id='clickme'/>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        startTimedTest();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final ThreadManager threadManager = page.getEnclosingWindow().getThreadManager();
        assertNotNull(threadManager);
        assertEquals(1, threadManager.activeCount());
        final HtmlAnchor a = (HtmlAnchor) page.getHtmlElementById("clickme");
        a.click();
        threadManager.joinAll(10000);
        assertEquals(0, threadManager.activeCount());
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        assertMaxTestRunTime(10000);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetClearIntervalUsesManager() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var threadID;\n"
            + "    function test() {\n"
            + "      threadID = setInterval('doAlert()', 100);\n"
            + "    }\n"
            + "    var iterationNumber=0;\n"
            + "    function doAlert() {\n"
            + "      alert('blah');\n"
            + "      if (++iterationNumber >= 3) {\n"
            + "        clearInterval(threadID);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
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
     * @throws Exception if the test fails
     */
    @Test
    public void testNavigationStopThreadsInChildWindows() throws Exception {
        final String firstContent = "<html><head><title>First</title></head><body>\n"
            + "<iframe id='iframe1' src='"
            + URL_SECOND
            + "'>\n"
            + "<a href='"
            + URL_THIRD.toExternalForm()
            + "' id='clickme'>click me</a>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body>\n"
            + "<script>\n"
            + "setInterval('', 10000);\n"
            + "</script>\n"
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
        Assert.assertEquals("inner frame should show child thread", 1, innerThreadManager.activeCount());

        final HtmlAnchor anchor = (HtmlAnchor) page.getHtmlElementById("clickme");
        final HtmlPage newPage = (HtmlPage) anchor.click();

        Assert.assertEquals("new page should load", "Third", newPage.getTitleText());
        Assert.assertEquals("frame should be gone", 0, newPage.getFrames().size());
        // this thread manager really is not accessible anymore, but this is a unit test
        innerThreadManager.joinAll(1000);
        Assert.assertEquals("thread should stop", 0, innerThreadManager.activeCount());
    }
}
