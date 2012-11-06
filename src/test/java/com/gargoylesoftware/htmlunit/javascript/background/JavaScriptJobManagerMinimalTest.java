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
package com.gargoylesoftware.htmlunit.javascript.background;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.apache.commons.lang3.mutable.MutableInt;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * Minimal tests for {@link JavaScriptJobManagerImpl}. Tests which use the full HtmlUnit stack
 * go in {@link JavaScriptJobManagerTest}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Amit Manjhi
 * @author Ronald Brill
 */
public class JavaScriptJobManagerMinimalTest {

    private WebClient client_;
    private WebWindow window_;
    private Page page_;
    private JavaScriptJobManagerImpl manager_;
    private DefaultJavaScriptExecutor eventLoop_;
    enum WaitingMode {
        WAIT_STARTING_BEFORE, WAIT_TIMELIMIT,
    }

    /**
     * Initializes variables required by the unit tests.
     */
    @Before
    public void before() {
        client_ = new WebClient();
        window_ = EasyMock.createNiceMock(WebWindow.class);
        page_ = EasyMock.createNiceMock(Page.class);
        manager_ = new JavaScriptJobManagerImpl(window_);
        EasyMock.expect(window_.getEnclosedPage()).andReturn(page_).anyTimes();
        EasyMock.expect(window_.getJobManager()).andReturn(manager_).anyTimes();
        EasyMock.replay(window_, page_);
        eventLoop_ = new DefaultJavaScriptExecutor(client_);
        eventLoop_.addWindow(window_);
    }

    /**
     * Shuts down the event loop.
     */
    @After
    public void after() {
        eventLoop_.shutdown();
    }

    /**
     * Didn't pass reliably.
     * @throws Exception if an error occurs
     */
    @Test
    public void addJob_periodicJob() throws Exception {
        final MutableInt count = new MutableInt(0);
        final JavaScriptJob job = new BasicJavaScriptJob(5, Integer.valueOf(100)) {
            public void run() {
                count.increment();
            }
        };
        manager_.addJob(job, page_);
        assertEquals(1, manager_.getJobCount());
        final int remainingJobs = manager_.waitForJobs(1090);
        Assert.assertTrue("At least one remaining job expected.", remainingJobs >= 1);
        Assert.assertTrue("Less than 10 jobs (" + count.intValue() + ") processed.", count.intValue() >= 10);
    }

    /**
     * Test for changes of revision 5589.
     * Ensures that interval jobs are scheduled at fix rate, no matter how long each one takes.
     * This test failed as of revision 5588 as consequence of the single threaded JS execution changes.
     * @throws Exception if an error occurs
     */
    @Test
    public void addJob_periodicJob2() throws Exception {
        final MutableInt count = new MutableInt(0);
        final JavaScriptJob job = new BasicJavaScriptJob(5, Integer.valueOf(200)) {
            public void run() {
                if (count.intValue() == 0) {
                    try {
                        Thread.sleep(200);
                    }
                    catch (final InterruptedException e) {
                        // ignore
                    }
                }
                count.increment();
            }
        };
        manager_.addJob(job, page_);
        final int remainingJobs = manager_.waitForJobs(300);
        Assert.assertTrue(remainingJobs >= 1);
        // first interval starts at 5 and ends at 205
        // with a fix delay (what would be wrong), we would have second interval start at 205+200 = 405
        // and therefore count == 1
        // with a fix rate (correct), second interval starts at 205 and therefore count == 2
        Assert.assertEquals(2, count.intValue());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void addJob_singleExecution() throws Exception {
        final MutableInt count = new MutableInt(0);
        final JavaScriptJob job = new BasicJavaScriptJob(5, null) {
            public void run() {
                count.increment();
            }
        };
        manager_.addJob(job, page_);
        assertEquals(1, manager_.getJobCount());
        manager_.waitForJobs(1000);
        Assert.assertEquals(1, count.intValue());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void addJob_multipleExecution_removeJob() throws Exception {
        final MutableInt id = new MutableInt();
        final MutableInt count = new MutableInt(0);
        final JavaScriptJob job = new BasicJavaScriptJob(50, Integer.valueOf(50)) {
            public void run() {
                count.increment();
                if (count.intValue() >= 5) {
                    manager_.removeJob(id.intValue());
                }
            }
        };
        id.setValue(manager_.addJob(job, page_));
        manager_.waitForJobs(1000);
        Assert.assertEquals(5, count.intValue());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void addJob_multipleExecution_removeAllJobs() throws Exception {
        final MutableInt count = new MutableInt(0);
        final JavaScriptJob job = new BasicJavaScriptJob(50, Integer.valueOf(50)) {
            public void run() {
                count.increment();
                if (count.intValue() >= 5) {
                    manager_.removeAllJobs();
                }
            }
        };
        manager_.addJob(job, page_);
        manager_.waitForJobs(1000);
        Assert.assertEquals(5, count.intValue());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void getJobCount() throws Exception {
        final MutableInt count = new MutableInt();
        final JavaScriptJob job = new BasicJavaScriptJob(50, null) {
            public void run() {
                count.setValue(manager_.getJobCount());
            }
        };
        Assert.assertEquals(0, manager_.getJobCount());
        manager_.addJob(job, page_);
        manager_.waitForJobs(1000);
        Assert.assertEquals(1, count.intValue());
        Assert.assertEquals(0, manager_.getJobCount());
    }

    private void waitForCurrentLongJob(final WaitingMode waitingMode, final int expectedFinalJobCount) {
        final JavaScriptJob job = new BasicJavaScriptJob(50, null) {
            // Long job
            public void run() {
                try {
                    Thread.sleep(500);
                }
                catch (final InterruptedException e) {
                    // ignore, this is normal
                }
            }
        };
        assertEquals(0, manager_.getJobCount());
        manager_.addJob(job, page_);
        final long delayMillis = 100;
        switch (waitingMode) {
            case WAIT_STARTING_BEFORE:
                manager_.waitForJobsStartingBefore(delayMillis);
                break;
            case WAIT_TIMELIMIT:
                manager_.waitForJobs(delayMillis);
                break;
            default:
                throw new IllegalArgumentException("Not handled");
        }
        Assert.assertEquals(expectedFinalJobCount, manager_.getJobCount());
    }

    /**
     * Tests the waitForJobs call when there is an executing long job.
     */
    @Test
    public void waitForJobs_currentLongJob() {
        waitForCurrentLongJob(WaitingMode.WAIT_TIMELIMIT, 1);
    }

    /**
     * Tests the waitForJobsStartingBefore call when there is an executing long job.
     */
    @Test
    public void waitForJobsStartingBefore_currentLongJob() {
        waitForCurrentLongJob(WaitingMode.WAIT_STARTING_BEFORE, 0);
    }

    private void waitForSimpleJobs(final WaitingMode waitingMode, final int expectedFinalJobCount) {
        final JavaScriptJob job1 = new BasicJavaScriptJob(50, null) {
            public void run() {
            // Empty.
            }
        };
        final JavaScriptJob job2 = new BasicJavaScriptJob(1000, null) {
            public void run() {
            // Empty.
            }
        };
        Assert.assertEquals(0, manager_.getJobCount());
        manager_.addJob(job1, page_);
        manager_.addJob(job2, page_);
        final long delayMillis = 250;
        switch (waitingMode) {
            case WAIT_STARTING_BEFORE:
                manager_.waitForJobsStartingBefore(delayMillis);
                break;
            case WAIT_TIMELIMIT:
                manager_.waitForJobs(delayMillis);
                break;
            default:
                throw new IllegalArgumentException("Not handled");
        }
        Assert.assertEquals(expectedFinalJobCount, manager_.getJobCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void waitForJobs_simpleJobs() throws Exception {
        waitForSimpleJobs(WaitingMode.WAIT_TIMELIMIT, 1);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void waitForJobsStartingBefore_simpleJobs() throws Exception {
        waitForSimpleJobs(WaitingMode.WAIT_STARTING_BEFORE, 1);
    }

    private void waitForComplexJobs(final WaitingMode waitingMode, final int expectedFinalJobCount) {
        final long start = System.currentTimeMillis();
        final JavaScriptJob job1 = new BasicJavaScriptJob(50, null) {
            // This job takes 30ms to complete.
            public void run() {
                try {
                    Thread.sleep(30);
                }
                catch (final InterruptedException e) {
                    // ignore, this is normal
                }
            }
        };
        final JavaScriptJob job2 = new BasicJavaScriptJob(60, null) {
            public void run() {
            // Empty.
            }
        };
        Assert.assertEquals(0, manager_.getJobCount());
        manager_.addJob(job1, page_);
        manager_.addJob(job2, page_);
        // sometimes it takes some time to reach this point
        // to make this test stable we have to take care of that
        final long delayMillis = 70 - (System.currentTimeMillis() - start);
        switch (waitingMode) {
            case WAIT_STARTING_BEFORE:
                manager_.waitForJobsStartingBefore(delayMillis);
                break;
            case WAIT_TIMELIMIT:
                manager_.waitForJobs(delayMillis);
                break;
            default:
                throw new RuntimeException("Unknown value for waitingMode enum " + waitingMode);
        }
        Assert.assertEquals(expectedFinalJobCount, manager_.getJobCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void waitForJobs_complexJobs() throws Exception {
        //job1 is still running, job2 has not started.
        waitForComplexJobs(WaitingMode.WAIT_TIMELIMIT, 2);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void waitForJobsStartingBefore_complexJobs() throws Exception {
        // the call waits until both job1 and job2 finish.
        waitForComplexJobs(WaitingMode.WAIT_STARTING_BEFORE, 0);
    }
}
