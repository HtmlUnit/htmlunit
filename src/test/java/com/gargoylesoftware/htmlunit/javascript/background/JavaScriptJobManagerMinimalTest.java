/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import junit.framework.Assert;

import org.apache.commons.lang.mutable.MutableInt;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * Minimal tests for {@link JavaScriptJobManagerImpl}. Tests which use the full HtmlUnit stack
 * go in {@link JavaScriptJobManagerTest}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class JavaScriptJobManagerMinimalTest {

    private WebWindow window_;
    private Page page_;
    private JavaScriptJobManagerImpl manager_;

    /**
     * Initializes variables required by the unit tests.
     */
    @Before
    public void before() {
        window_ = EasyMock.createNiceMock(WebWindow.class);
        page_ = EasyMock.createNiceMock(Page.class);
        EasyMock.expect(window_.getEnclosedPage()).andReturn(page_).anyTimes();
        EasyMock.replay(window_, page_);
        manager_ = new JavaScriptJobManagerImpl(window_);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void addJob_singleExecution() throws Exception {
        final MutableInt count = new MutableInt(0);
        final JavaScriptJob job = new JavaScriptJob(50, null) {
            public void run() {
                count.increment();
            }
        };
        manager_.addJob(job, page_);
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
        final JavaScriptJob job = new JavaScriptJob(50, 50) {
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
        final JavaScriptJob job = new JavaScriptJob(50, 50) {
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
        final JavaScriptJob job = new JavaScriptJob(50, null) {
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

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void waitForJobsStartingBefore() throws Exception {
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
        manager_.waitForJobsStartingBefore(250);
        Assert.assertEquals(1, manager_.getJobCount());
    }

    /**
     * Cleanup after a test.
     */
    @After
    public void releaseResources() {
        if (manager_ != null) {
            manager_.shutdown();
        }
    }
}
