/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Tries;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

/**
 * Tests for {@link WebClient#waitForBackgroundJavaScriptStartingBefore(long)} and
 * {@link WebClient#waitForBackgroundJavaScript(long)}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class WebClientWaitForBackgroundJobsTest extends SimpleWebTestCase {
    private static String XHRInstantiation_ = "(window.XMLHttpRequest ? "
        + "new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'))";

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
        assertEquals(1, page.getWebClient().waitForBackgroundJavaScriptStartingBefore(7000));
        assertMaxTestRunTime(100);
        assertEquals(1, jobManager.getJobCount());
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Tries(3)
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
        assertEquals(0, page.getWebClient().waitForBackgroundJavaScriptStartingBefore(20000));
        assertMaxTestRunTime(400);
        assertEquals(0, jobManager.getJobCount());

        final String[] expectedAlerts = {"clearLongTimeout", "hello"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * When waitForBackgroundJavaScriptStartingBefore is called while a job is being executed, it has
     * to wait for this job to finish, even if this clearXXX has been called for it.
     * @throws Exception if the test fails
     */
    @Test
    @Tries(3)
    public void waitCalledDuringJobExecution() throws Exception {
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
            + "      // waitForBackgroundJavaScriptStartingBefore should be called when JS execution is here\n"
            + "      var request = " + XHRInstantiation_ + ";\n"
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
            public WebResponse getResponse(final WebRequest request) throws IOException {
                if (request.getUrl().toExternalForm().endsWith("/wait")) {
                    threadSynchronizer.waitForState("just before waitForBackgroundJavaScriptStartingBefore");
                    threadSynchronizer.sleep(400); // main thread need to be able to process next instruction
                }
                return super.getResponse(request);
            }
        };
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse("");

        final WebClient client = getWebClient();
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = client.getPage(URL_FIRST);
        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        assertNotNull(jobManager);
        assertEquals(1, jobManager.getJobCount());

        startTimedTest();
        threadSynchronizer.setState("just before waitForBackgroundJavaScriptStartingBefore");
        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(20000));
        assertMaxTestRunTime(600);
        assertEquals(0, jobManager.getJobCount());

        final String[] expectedAlerts = {"end work"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * When waitForBackgroundJavaScriptStartingBefore is called and a new job is scheduled after the one that
     * is first found as the last one within the delay (a job starts a new one or simply a setInterval),
     * a few retries should be done to see if new jobs exists.
     * @throws Exception if the test fails
     */
    @Test
    public void waitWhenLastJobStartsNewOne() throws Exception {
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
        assertEquals(0, page.getWebClient().waitForBackgroundJavaScriptStartingBefore(20000));
        assertMaxTestRunTime(1000);
        assertEquals(0, jobManager.getJobCount());

        final String[] expectedAlerts = {"work1", "work2"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * When waitForBackgroundJavaScriptStartingBefore is called and a new job is scheduled after the one that
     * is first found as the last one within the delay (a job starts a new one or simply a setInterval),
     * a few retries should be done to see if new jobs exists.
     * @throws Exception if the test fails
     */
    @Test
    @Tries(3)
    public void waitWithsubWindows() throws Exception {
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

        final WebClient client = getWebClient();
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
        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(20000));
        assertMaxTestRunTime(300);
        assertEquals(0, jobManager.getJobCount());

        final String[] expectedAlerts = {"work1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test the case where a job is being executed at the time where waitForBackgroundJavaScriptStartingBefore
     * and where this jobs schedules a new job after call to waitForBackgroundJavaScriptStartingBefore,
     * .
     * @throws Exception if the test fails
     */
    @Test
    @Tries(3)
    public void newJobStartedAfterWait() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var request;\n"
            + "    function onReadyStateChange() {\n"
            + "      if (request.readyState == 4) {\n"
            + "        alert('xhr onchange');\n"
            + "        setTimeout(doWork1, 200);\n"
            + "      }\n"
            + "    }\n"
            + "    function test() {\n"
            + "      request = " + XHRInstantiation_ + ";\n"
            + "      request.open('GET', 'wait', true);\n"
            + "      request.onreadystatechange = onReadyStateChange;\n"
            + "      // waitForBackgroundJavaScriptStartingBefore should be called when JS execution is in send()\n"
            + "      request.send('');\n"
            + "    }\n"
            + "    function doWork1() {\n"
            + "      alert('work1');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final ThreadSynchronizer threadSynchronizer = new ThreadSynchronizer();
        final MockWebConnection webConnection = new MockWebConnection() {
            @Override
            public WebResponse getResponse(final WebRequest request) throws IOException {
                if (request.getUrl().toExternalForm().endsWith("/wait")) {
                    threadSynchronizer.waitForState("just before waitForBackgroundJavaScriptStartingBefore");
                    threadSynchronizer.sleep(400); // main thread need to be able to process next instruction
                }
                return super.getResponse(request);
            }
        };
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse("");

        final WebClient client = getWebClient();
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = client.getPage(URL_FIRST);
        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        assertNotNull(jobManager);
        assertEquals(1, jobManager.getJobCount());

        startTimedTest();
        threadSynchronizer.setState("just before waitForBackgroundJavaScriptStartingBefore");
        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(20000));
        assertMaxTestRunTime(1000);
        assertEquals(0, jobManager.getJobCount());

        final String[] expectedAlerts = {"xhr onchange", "work1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests that waitForBackgroundJavaScriptStartingBefore waits for jobs that should have been started earlier
     * but that are "late" due to processing of previous job.
     * This test needs to start many setTimeout to expect to reach the state, where a check for future
     * jobs occurs when one of this job is not active.
     * @throws Exception if the test fails
     */
    @Test
    @Tries(3)
    public void waitForJobThatIsAlreadyLate() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    var counter = 0;\n"
            + "    function test() {\n"
            + "      setTimeout(doWork1, 0);\n"
            + "    }\n"
            + "    function doWork1() {\n"
            + "      if (counter++ < 50) {\n"
            + "        setTimeout(doWork1, 0);\n"
            + "      }\n"
            + "      alert('work1');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse("");

        final WebClient client = getWebClient();
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);

        startTimedTest();
        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));
        assertMaxTestRunTime(1000);
        assertEquals(51, collectedAlerts.size());
    }

    /**
     * {@link WebClient#waitForBackgroundJavaScript(long)} should have an overview of all windows.
     * @throws Exception if the test fails
     */
    @Test
    public void jobSchedulesJobInOtherWindow1() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    var counter = 0;\n"
            + "    function test() {\n"
            + "      var w = window.open('about:blank');\n"
            + "      w.setTimeout(doWork1, 200);\n"
            + "    }\n"
            + "    function doWork1() {\n"
            + "      alert('work1');\n"
            + "      setTimeout(doWork2, 400);\n"
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

        startTimedTest();
        assertEquals(0, page.getWebClient().waitForBackgroundJavaScript(1000));
        assertMaxTestRunTime(1000);

        final String[] expectedAlerts = {"work1", "work2"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * {@link WebClient#waitForBackgroundJavaScriptStartingBefore(long)} should have an overview of all windows.
     * @throws Exception if the test fails
     */
    @Test
    public void jobSchedulesJobInOtherWindow2() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    var counter = 0;\n"
            + "    function test() {\n"
            + "      var w = window.open('about:blank');\n"
            + "      w.setTimeout(doWork1, 200);\n"
            + "    }\n"
            + "    function doWork1() {\n"
            + "      alert('work1');\n"
            + "      setTimeout(doWork2, 400);\n"
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

        startTimedTest();
        assertEquals(0, page.getWebClient().waitForBackgroundJavaScriptStartingBefore(1000));
        assertMaxTestRunTime(1000);

        final String[] expectedAlerts = {"work1", "work2"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * HtmlUnit-2.7-SNAPSHOT (as of 29.10.09) had bug with
     * WebClient.waitForBackgroundJavaScriptStartingBefore: it could be totally blocking
     * under some circumstances. This test reproduces the problem but ensures
     * that the test terminates (calling clearInterval when waitForBackgroundJavaScriptStartingBefore
     * has not done its job correctly).
     * @throws Exception if the test fails
     */
    @Test
    @Tries(3)
    public void waitForBackgroundJavaScriptStartingBefore_hangs() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var start = new Date().getTime();\n"
            + "    var id = setInterval(doWork1, 35);\n"
            + "    function stopTimer() {\n"
            + "      clearInterval(id);\n"
            + "    }\n"
            + "    function doWork1() {\n"
            + "      if (start + 8000 < new Date().getTime()) {\n"
            + "        document.title = 'failed';\n"
            + "        clearInterval(id);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<button onclick='stopTimer()'>stop</button>\n"
            + "</body>\n"
            + "</html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);

        int noOfJobs = client.waitForBackgroundJavaScriptStartingBefore(500);
        assertTrue(noOfJobs == 1 || noOfJobs == 2); // maybe one is running

        assertEquals("test", page.getTitleText());
        noOfJobs = client.waitForBackgroundJavaScriptStartingBefore(500);
        assertTrue(noOfJobs == 1 || noOfJobs == 2); // maybe one is running
    }
}

/**
 * Helper to ensure some synchronization state between threads to reproduce a particular situation in the tests.
 * @author Marc Guillemot
 */
class ThreadSynchronizer {
    private String state_ = "initial";
    private static final Log LOG = LogFactory.getLog(ThreadSynchronizer.class);

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
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sleeping for " + millis + "ms");
            }
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
