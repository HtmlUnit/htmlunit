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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

/**
 * Tests for {@link WebClient#waitForJobsWithinDelayToFinish(long)}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class WebClientWaitForBackgroundJobsTest extends WebTestCase {
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
    public void dontWaitWhenUnnecessary() throws Exception {
        final String content = "<html>\n"
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
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        final HtmlPage page = loadPage(content, collectedAlerts);
        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        assertNotNull(jobManager);
        assertEquals(1, jobManager.getJobCount());

        startTimedTest();
        page.getWebClient().waitForJobsWithinDelayToFinish(7000);
        assertMaxTestRunTime(100);
        assertEquals(1, jobManager.getJobCount());
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void dontWaitWhenUnnecessary_jobRemovesOtherJob() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var longTimeoutID;\n"
            + "    function test() {\n"
            + "      longTimeoutID = setTimeout(doAlert('late'), 10000);\n"
            + "      setTimeout(clearLongTimeout, 100);\n"
            + "      setTimeout(doAlert('hello'), 300);\n"
            + "    }\n"
            + "    function clearLongTimeout() {\n"
            + "      alert('clearLongTimeout');\n"
            + "      clearTimeout(longTimeoutID);\n"
            + "    }\n"
            + "    function doAlert(_text) {\n"
            + "      return function doAlert() {\n"
            + "        alert(_text);\n"
            + "      }\n"
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
        assertEquals(3, jobManager.getJobCount());

        startTimedTest();
        page.getWebClient().waitForJobsWithinDelayToFinish(20000);
        assertMaxTestRunTime(400);
        assertEquals(0, jobManager.getJobCount());

        final String[] expectedAlerts = {"clearLongTimeout", "hello"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * When waitForJobsWithinDelayToFinish is called while a job is being executed, it has
     * to wait for this job to finish, even if this clearXXX has been called for it.
     * In other words this means that
     * {@link com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobsSupervisor
     * #waitForJobsWithinDelayToFinish(long)} can not
     * only rely on the futures_ map as clearXxx removes entries there.
     * @throws Exception if the test fails
     */
    @Test
    public void waitForJobsWithinDelayToFinish_calledDuringJobExecution() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var intervalId;\n"
            + "    function test() {\n"
            + "      intervalId = setTimeout(doWork, 100);\n"
            + "    }\n"
            + "    function doWork() {\n"
            + "      clearTimeout(intervalId);\n"
            + "      // waitForJobsWithinDelayToFinish should be called when JS execution is here\n"
            + "      var request = new XMLHttpRequest();\n"
            + "      request.open('GET', 'wait', false);\n"
            + "      request.send('');\n"
            + "      alert('end work');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final ThreadSynchronizer threadSynchronizer = new ThreadSynchronizer();
        final MockWebConnection webConnection = new MockWebConnection() {
            @Override
            public WebResponse getResponse(final WebRequestSettings settings) throws IOException {
                if (settings.getUrl().toExternalForm().endsWith("/wait")) {
                    threadSynchronizer.waitForState("just before waitForJobsWithinDelayToFinish");
                    threadSynchronizer.sleep(400); // main thread need to be able to process next instruction
                }
                return super.getResponse(settings);
            }
        };
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse("");

        final WebClient client = new WebClient(BrowserVersion.FIREFOX_3); // just to simplify test code using XHR
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = client.getPage(URL_FIRST);
        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        assertNotNull(jobManager);
        assertEquals(1, jobManager.getJobCount());

        startTimedTest();
        threadSynchronizer.setState("just before waitForJobsWithinDelayToFinish");
        client.waitForJobsWithinDelayToFinish(20000);
        assertMaxTestRunTime(600);
        assertEquals(0, jobManager.getJobCount());

        final String[] expectedAlerts = {"end work"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * When waitForJobsWithinDelayToFinish is called and a new job is scheduled after the one that
     * is first found as the last one within the delay (a job starts a new one or simply a setInterval),
     * a few retries should be done to see if new jobs exists.
     * @throws Exception if the test fails
     */
    @Test
    public void waitForJobsWithinDelayToFinish_lastJobStartsNewOne() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      setTimeout(doWork1, 200);\n"
            + "    }\n"
            + "    function doWork1() {\n"
            + "      alert('work1');\n"
            + "      setTimeout(doWork2, 200);\n"
            + "    }\n"
            + "    function doWork2() {\n"
            + "      alert('work2');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        final HtmlPage page = loadPage(html, collectedAlerts);
        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        assertNotNull(jobManager);
        assertEquals(1, jobManager.getJobCount());

        startTimedTest();
        page.getWebClient().waitForJobsWithinDelayToFinish(20000);
        assertMaxTestRunTime(400);
        assertEquals(0, jobManager.getJobCount());

        final String[] expectedAlerts = {"work1", "work2"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * When waitForJobsWithinDelayToFinish is called and a new job is scheduled after the one that
     * is first found as the last one within the delay (a job starts a new one or simply a setInterval),
     * a few retries should be done to see if new jobs exists.
     * @throws Exception if the test fails
     */
    @Test
    public void waitForJobsWithinDelayToFinish_subWindows() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "<iframe src='nested.html'></iframe>\n"
            + "</body>\n"
            + "</html>";
        final String nested = "<html>\n"
            + "<head>\n"
            + "  <title>nested</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      setTimeout(doWork1, 200);\n"
            + "    }\n"
            + "    function doWork1() {\n"
            + "      alert('work1');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final WebClient client =  new WebClient(BrowserVersion.FIREFOX_2);
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse(nested);

        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);

        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        assertNotNull(jobManager);
        assertEquals(0, jobManager.getJobCount());

        startTimedTest();
        client.waitForJobsWithinDelayToFinish(20000);
        assertMaxTestRunTime(300);
        assertEquals(0, jobManager.getJobCount());

        final String[] expectedAlerts = {"work1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }
}

/**
 * Helper to ensure some synchronization state between threads to reproduce a particular situation in the tests.
 * @author Marc Guillemot
 */
class ThreadSynchronizer {
    private String state_ = "initial";

    synchronized void setState(final String newState) {
        state_ = newState;
        notifyAll();
    }

    /**
     * Just like {@link Thread#sleep(long)} but throws a {@link RuntimeException}.
     * @param millis the time to sleep in milliseconds
     */
    public void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized void waitForState(final String expectedState) {
        try {
            while (!state_.equals(expectedState)) {
                wait();
            }
        }
        catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
