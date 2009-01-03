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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Test class which verifies that recursive calls to <tt>setTimeout(...)</tt> in JavaScript are
 * correctly interrupted when the associated <tt>WebClient</tt> instance moves on to a new page.
 * See bug 1728883 for more information.
 *
 * @version $Revision$
 * @author Karel Kolman
 * @author Daniel Gredler
 */
public class ThreadManagerRecursiveSetTimeoutTest extends WebTestCase {

    /** The number of test threads to use. */
    private static final int TEST_THREAD_COUNT = 100;

    /** The number of milliseconds which each setTimeout(...) invocation should wait. */
    private static final int TIMEOUT_MILLIS = 100;

    /** The number of seconds to allow the test page to idle. */
    private static final int SLEEP_SECS_1 = 1;

    /** The number of seconds to allow the second (empty) page to idle. */
    private static final int SLEEP_SECS_2 = 7;

    /** Fuzz factor for the lower bound check (takes non-deterministic test results into account). */
    private static final double LOWER_TEST_FACTOR = 0.8;

    /** Fuzz factor for the upper bound check (takes non-deterministic test results into account). */
    private static final double UPPER_TEST_FACTOR = 2.5;

    /**
     * Verifies that recursive calls to <tt>setTimeout(...)</tt> in JavaScript are correctly
     * interrupted when the associated <tt>WebClient</tt> instance moves on to a new page. See
     * bug 1728883 for more information.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void interruptAllWithRecursiveSetTimeout() throws Exception {

        if (notYetImplemented()) {
            return;
        }

        // Start all of the test threads.
        final List<TestThread> threads = new ArrayList<TestThread>();
        for (int i = 0; i < TEST_THREAD_COUNT; i++) {
            final TestThread thread = new TestThread();
            threads.add(thread);
            thread.start();
        }

        // Wait for all of the test threads to finish.
        do {
            boolean allFinished = true;
            for (TestThread thread : threads) {
                if (!thread.isFinished()) {
                    allFinished = false;
                    break;
                }
            }
            if (!allFinished) {
                Thread.sleep(100);
            }
            else {
                break;
            }
        } while (true);

        // Verify the state of all of the test threads.
        for (TestThread thread : threads) {

            final int approxExpectedAlerts = SLEEP_SECS_1 * 1000 / TIMEOUT_MILLIS;
            final int approxErrorExpectedAlerts = (SLEEP_SECS_1 + SLEEP_SECS_2) * 1000 / TIMEOUT_MILLIS;

            // Verify that the number of collected alerts is not too high.
            assertTrue("Expected approx. " + approxExpectedAlerts + " alerts, got " + thread.getAlertCount()
                + ", would expect to catch approximately " + approxErrorExpectedAlerts
                + " if thread manager had not been not interrupted.",
                !(thread.getAlertCount() > approxExpectedAlerts * UPPER_TEST_FACTOR));

            // Verify that the number of collected alerts is not too low.
            assertTrue("Expected approx. " + approxExpectedAlerts + " alerts, got " + thread.getAlertCount()
                + ", would expect to catch approximately " + approxErrorExpectedAlerts
                + " if thread manager had not been not interrupted.",
                thread.getAlertCount() > approxExpectedAlerts * LOWER_TEST_FACTOR);
        }

    }

    /**
     * Custom test thread class.
     */
    class TestThread extends Thread {

        /** Whether or not this thread has finished. */
        private boolean finished_ = false;

        /** The number of <tt>alert(...)</tt> invocations triggered by the HTML page loaded by this thread. */
        private int alertCount_ = 0;

        /** {@inheritDoc} */
        @Override
        public void run() {
            try {
                // Load the HTML page with the recursive setTimeout(...) calls.
                final String content =
                      "<html><head><script>function recursion() {"
                    + "window.setTimeout(recursion, " + TIMEOUT_MILLIS + "); alert();}"
                    + "</script></head><body onload='recursion();'></body></html>";
                final List<String> alerts = new ArrayList<String>();
                final HtmlPage page = loadPage(BrowserVersion.FIREFOX_3, content, alerts);

                // Let the page idle for a bit.
                try {
                    Thread.sleep(SLEEP_SECS_1 * 1000);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }

                // Load a new HTML page, thus triggering ThreadManager's interruptAll().
                page.getEnclosingWindow().setEnclosedPage(loadPage("<html></html>"));

                // Let the new page idle for a (longer) bit.
                try {
                    Thread.sleep(SLEEP_SECS_2 * 1000);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }

                // Save the number of collected alerts.
                alertCount_ = alerts.size();
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            finally {
                finished_ = true;
            }
        }

        /**
         * Returns whether or not this thread has finished.
         * @return whether or not this thread has finished
         */
        public boolean isFinished() {
            return finished_;
        }

        /**
         * Returns the number of <tt>alert(...)</tt> invocations triggered by the HTML page loaded by this thread.
         * @return the number of <tt>alert(...)</tt> invocations triggered by the HTML page loaded by this thread
         */
        public int getAlertCount() {
            return alertCount_;
        }
    }

}
