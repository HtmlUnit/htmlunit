/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import org.apache.commons.lang.mutable.MutableInt;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.gae.GAETestRunner;

/**
 * Minimal tests for {@link JavaScriptJobManagerImpl} on GoogleAppEngine.
 *
 * @version $Revision$
 * @author Amit Manjhi
 */
@RunWith(GAETestRunner.class)
public class JavaScriptJobManagerGaeMinimalTest {

    private WebClient client_;
    private WebWindow window_;
    private Page page_;
    private JavaScriptJobManagerImpl manager_;
    private JavaScriptExecutor eventLoop_;
    enum WaitingMode {
        WAIT_STARTING_BEFORE, WAIT_TIMELIMIT,
    }

    /**
     * Initializes variables required by the unit tests.
     */
    @Before
    public void before() {
        // set the GAE mode
        client_ = new WebClient();
        window_ = EasyMock.createNiceMock(WebWindow.class);
        page_ = EasyMock.createNiceMock(Page.class);
        manager_ = new JavaScriptJobManagerImpl(window_);
        EasyMock.expect(window_.getEnclosedPage()).andReturn(page_).anyTimes();
        EasyMock.expect(window_.getJobManager()).andReturn(manager_).anyTimes();
        EasyMock.replay(window_, page_);
        eventLoop_ = new GAEJavaScriptExecutor(client_);
        eventLoop_.addWindow(window_);
    }

    /**
     * Shuts down the event loop.
     */
    @After
    public void after() {
        if (eventLoop_ != null) {
            eventLoop_.shutdown();
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void addJob_singleExecution() throws Exception {
        final MutableInt count = new MutableInt(0);
        final JavaScriptJob job = new JavaScriptJob(5, null) {
            public void run() {
                count.increment();
            }
        };
        manager_.addJob(job, page_);
        assertEquals(1, manager_.getJobCount());
        final int executedJobs = eventLoop_.pumpEventLoop(10000);
        Assert.assertEquals(1, executedJobs);
        Assert.assertEquals(1, count.intValue());
        assertEquals(0, manager_.getJobCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void addJob_multipleExecution_removeJob() throws Exception {
        final MutableInt id = new MutableInt();
        final MutableInt count = new MutableInt(0);
        final JavaScriptJob job = new JavaScriptJob(50, 50) {
            public void run() {
                count.increment();
                if (count.intValue() >= 5) {
                    manager_.removeJob(id.intValue());
                }
            }
        };
        id.setValue(manager_.addJob(job, page_));
        final int executedJobs = eventLoop_.pumpEventLoop(1000);
        Assert.assertEquals(5, executedJobs);
        Assert.assertEquals(5, count.intValue());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void addJob_multipleExecution_removeAllJobs() throws Exception {
        final MutableInt count = new MutableInt(0);
        final JavaScriptJob job = new JavaScriptJob(50, 50) {
            public void run() {
                count.increment();
                if (count.intValue() >= 5) {
                    manager_.removeAllJobs();
                }
            }
        };
        manager_.addJob(job, page_);
        final int executedJobs = eventLoop_.pumpEventLoop(1000);
        Assert.assertEquals(5, executedJobs);
        Assert.assertEquals(5, count.intValue());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void getJobCount() throws Exception {
        final MutableInt count = new MutableInt();
        final JavaScriptJob job = new JavaScriptJob(50, null) {
            public void run() {
                count.setValue(manager_.getJobCount());
            }
        };
        Assert.assertEquals(0, manager_.getJobCount());
        manager_.addJob(job, page_);
        final int executedJobs = eventLoop_.pumpEventLoop(1000);
        Assert.assertEquals(1, executedJobs);
        Assert.assertEquals(1, count.intValue());
        Assert.assertEquals(0, manager_.getJobCount());
    }

    /**
     * Tests waiting for the current job.
     */
    @Test
    public void waitForCurrentLongJob() {
        final JavaScriptJob job = new JavaScriptJob(50, null) {
            // Long job
            public void run() {
                try {
                    Thread.sleep(500);
                }
                catch (final InterruptedException e) {
                    // ignore
                }
            }
        };
        Assert.assertEquals(0, manager_.getJobCount());
        manager_.addJob(job, page_);
        final int executedJobs = eventLoop_.pumpEventLoop(1000);
        Assert.assertEquals(1, executedJobs);
        Assert.assertEquals(0, manager_.getJobCount());
    }

    /**
     * Tests if waiting for simple jobs works.
     */
    @Test
    public void waitForSimpleJobs() {
        final JavaScriptJob job1 = new JavaScriptJob(50, null) {
            public void run() {
            // Empty.
            }
        };
        final JavaScriptJob job2 = new JavaScriptJob(1000, null) {
            public void run() {
            // Empty.
            }
        };
        Assert.assertEquals(0, manager_.getJobCount());
        manager_.addJob(job1, page_);
        manager_.addJob(job2, page_);
        final int executedJobs = eventLoop_.pumpEventLoop(200);
        Assert.assertEquals(1, executedJobs);
        Assert.assertEquals(1, manager_.getJobCount());
    }

    /**
     * Tests if waiting for complex jobs work.
     */
    @Test
    public void waitForComplexJobs() {
        final JavaScriptJob job1 = new JavaScriptJob(50, null) {
            // This job takes 30ms to complete.
            public void run() {
                try {
                    Thread.sleep(30);
                }
                catch (final InterruptedException e) {
                    // ignore
                }
            }
        };
        final JavaScriptJob job2 = new JavaScriptJob(60, null) {
            public void run() {
            // Empty.
            }
        };
        Assert.assertEquals(0, manager_.getJobCount());
        manager_.addJob(job1, page_);
        manager_.addJob(job2, page_);
        final int executedJobs = eventLoop_.pumpEventLoop(70);
        Assert.assertEquals(1, executedJobs);
        Assert.assertEquals(1, manager_.getJobCount());
    }
}
