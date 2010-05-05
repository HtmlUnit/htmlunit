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

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * An event loop to execute all the JavaScript jobs.
 *
 * @version $Revision$
 * @author Amit Manjhi
 *
 */
public class JavaScriptExecutor implements Runnable, Serializable {

	private static final long serialVersionUID = 8525230714555970165L;

	/*
     * Currently, go for a simple implementation
     */
    private final class JobExecutor {
        private final JavaScriptJobManager jobManager_;
        private final JavaScriptJob earliestJob_;

        private JobExecutor(final JavaScriptJobManager jobManager, final JavaScriptJob earliestJob) {
            jobManager_ = jobManager;
            earliestJob_ = earliestJob;
        }
    }

    // TODO: is there utility in not having these as transient?
    private transient WeakReference<WebClient> webClient_;

    private transient List<WeakReference<JavaScriptJobManager>> jobManagerList_;

    private volatile boolean shutdown_ = false;

    private transient Thread eventLoopThread_ = null;

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(JavaScriptExecutor.class);

    /** Creates an EventLoop for the webClient.
     *
     * @param webClient the provided webClient
     */
    public JavaScriptExecutor(final WebClient webClient) {
        jobManagerList_ = new ArrayList<WeakReference<JavaScriptJobManager>>();
        webClient_ = new WeakReference<WebClient>(webClient);
    }

    /**
     * Starts the eventLoopThread_.
     */
    private void startThreadIfNeeded() {
        if (eventLoopThread_ == null) {
            eventLoopThread_ = new Thread(this);
            eventLoopThread_.start();
            eventLoopThread_.setName("event loop for client");
        }
    }

    private void killThread() {
        if (eventLoopThread_ == null) {
            return;
        }
        try {
            eventLoopThread_.interrupt();
            eventLoopThread_.join(10000);
        }
        catch (final InterruptedException e) {
            LOG.warn("InterruptedException while waiting for the eventLoop thread to join " + e);
            // ignore, this doesn't matter, we want to stop it
        }
        if (eventLoopThread_.isAlive()) {
            LOG.warn("Event loop thread "
                + eventLoopThread_.getName()
                + " still alive at "
                + System.currentTimeMillis());
        }
    }

    private synchronized JobExecutor getEarliestJob() {
        JobExecutor jobExecutor = null;
        // iterate over the list and find the earliest job to run.
        for (WeakReference<JavaScriptJobManager> jobManagerRef : jobManagerList_) {
            final JavaScriptJobManager jobManager = jobManagerRef.get();
            if (jobManager != null) {
                final JavaScriptJob newJob = jobManager.getEarliestJob();
                if (newJob != null) {
                    if (jobExecutor == null
                        || jobExecutor.earliestJob_.getTargetExecutionTime() > newJob
                                .getTargetExecutionTime()) {
                        jobExecutor = new JobExecutor(jobManager, newJob);
                    }
                }
            }
        }
        return jobExecutor;
    }

    /** Runs the eventLoop. */
    public void run() {
        while (!shutdown_ && webClient_.get() != null) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("started finding earliestJob at " + System.currentTimeMillis());
            }
            final JobExecutor jobExecutor = getEarliestJob();
            if (LOG.isTraceEnabled()) {
                LOG.trace("stopped finding earliestJob at " + System.currentTimeMillis());
            }

            final long sleepInterval = 10;
            if (jobExecutor == null
                || jobExecutor.earliestJob_.getTargetExecutionTime() - System.currentTimeMillis() > sleepInterval) {
                try {
                    Thread.sleep(sleepInterval);
                }
                catch (final InterruptedException e) {
                    // nothing, probably a shutdown notification
                }
            }
            if (shutdown_ || webClient_.get() == null) {
                break;
            }
            if (jobExecutor != null) {
                // execute the earliest job.
                if (LOG.isTraceEnabled()) {
                    LOG.trace("started executing job at " + System.currentTimeMillis());
                }
                jobExecutor.jobManager_.runSingleJob(jobExecutor.earliestJob_);
                if (LOG.isTraceEnabled()) {
                    LOG.trace("stopped executing job at " + System.currentTimeMillis());
                }
            }
        }
    }

    /**
     * Register a window with the eventLoop.
     * @param newWindow the new web window
     */
    public synchronized void addWindow(final WebWindow newWindow) {
        final JavaScriptJobManager jobManager = newWindow.getJobManager();
        if (jobManager != null && !contains(jobManager)) {
            jobManagerList_.add(new WeakReference<JavaScriptJobManager>(jobManager));
            startThreadIfNeeded();
        }
    }

    private boolean contains(final JavaScriptJobManager newJobManager) {
        for (WeakReference<JavaScriptJobManager> jobManagerRef : jobManagerList_) {
            if (jobManagerRef.get() == newJobManager) {
                return true;
            }
        }
        return false;
    }

    /** Notes that this thread has been shutdown. */
    public void shutdown() {
        shutdown_ = true;
        killThread();
    }

}
