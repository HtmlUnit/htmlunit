/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript.background;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.TopLevelWindow;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlInlineFrame;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link JavaScriptJobManagerImpl} using the full HtmlUnit stack. Minimal unit tests
 * which do not use the full HtmlUnit stack go in {@link JavaScriptJobManagerMinimalTest}.
 *
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public class JavaScriptJobManagerTest extends SimpleWebTestCase {
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
    public void setClearTimeoutUsesManager() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var threadID;\n"
            + "    function test() {\n"
            + "      threadID = setTimeout(doAlert, 10000);\n"
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
        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        assertNotNull(jobManager);
        assertEquals(1, jobManager.getJobCount());
        final HtmlAnchor a = page.getHtmlElementById("clickme");
        a.click();
        jobManager.waitForJobs(7000);
        assertEquals(0, jobManager.getJobCount());
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        assertMaxTestRunTime(10_000);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setClearIntervalUsesManager() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var threadID;\n"
            + "    function test() {\n"
            + "      threadID = setInterval(doAlert, 100);\n"
            + "    }\n"
            + "    var iterationNumber = 0;\n"
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
        final HtmlPage page = loadPage(content, collectedAlerts);

        // loading the page takes some time (on our slow build machine);
        // start the timer after loading the page
        startTimedTest();

        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        assertNotNull(jobManager);
        assertEquals(1, jobManager.getJobCount());
        jobManager.waitForJobs(DEFAULT_WAIT_TIME.toMillis());
        assertEquals(0, jobManager.getJobCount());
        assertEquals(Collections.nCopies(3, "blah"), collectedAlerts);

        assertMaxTestRunTime(DEFAULT_WAIT_TIME.toMillis() + 100);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void navigationStopThreadsInChildWindows() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<iframe id='iframe1' src='"
            + URL_SECOND
            + "'></iframe>\n"
            + "<a href='"
            + URL_THIRD.toExternalForm()
            + "' id='clickme'>click me</a>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body>\n"
            + "<script>\n"
            + "setInterval('', 30000);\n"
            + "</script>\n"
            + "</body></html>";
        final String thirdContent = DOCTYPE_HTML
            + "<html><head><title>Third</title></head><body></body></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        final HtmlPage page = loadPage(firstContent);
        final HtmlInlineFrame iframe = page.getHtmlElementById("iframe1");
        final JavaScriptJobManager mgr = iframe.getEnclosedWindow().getJobManager();
        assertEquals("inner frame should show child thread", 1, mgr.getJobCount());

        final HtmlAnchor anchor = page.getHtmlElementById("clickme");
        final HtmlPage newPage = anchor.click();
        assertEquals("new page should load", "Third", newPage.getTitleText());
        assertTrue("frame should be gone", newPage.getFrames().isEmpty());

        mgr.waitForJobs(10_000);
        assertEquals("job manager should have no jobs left", 0, mgr.getJobCount());
    }

    /**
     * Test for Bug #487 that makes sure closing a window prevents a
     * recursive setTimeout from continuing forever.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void interruptAllWithRecursiveSetTimeout() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var threadID;\n"
            + "    function test() {\n"
            + "      alert('ping');\n"
            + "      threadID = setTimeout(test, 5);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        final HtmlPage page = loadPage(content, collectedAlerts);
        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        assertNotNull(jobManager);

        // Not perfect, but 100 chances to start should be enough for a loaded system
        Thread.sleep(500);

        assertFalse("At least one alert should have fired by now", collectedAlerts.isEmpty());
        ((TopLevelWindow) page.getEnclosingWindow()).close();

        // 100 chances to stop
        jobManager.waitForJobs(500);

        final int finalValue = collectedAlerts.size();

        // 100 chances to fail
        jobManager.waitForJobs(500);

        assertEquals("No new alerts should have happened", finalValue, collectedAlerts.size());
    }
}
