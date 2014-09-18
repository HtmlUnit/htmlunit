/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * An event loop to execute all the JavaScript jobs.
 *
 * @version $Revision$
 * @author Amit Manjhi
 * @author Kostadin Chikov
 * @author Ronald Brill
 */
public class DefaultJavaScriptExecutor implements JavaScriptExecutor {
    private static final long serialVersionUID = 5677978210585334168L;

    // TODO: is there utility in not having these as transient?
    private final transient WeakReference<WebClient> webClient_;

    private final transient List<WeakReference<JavaScriptJobManager>> jobManagerList_;

    private volatile boolean shutdown_ = false;

    private transient Thread eventLoopThread_ = null;

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(DefaultJavaScriptExecutor.class);

    /** Creates an EventLoop for the webClient.
     *
     * @param webClient the provided webClient
     */
    public DefaultJavaScriptExecutor(final WebClient webClient) {
        jobManagerList_ = new LinkedList<WeakReference<JavaScriptJobManager>>();
        webClient_ = new WeakReference<WebClient>(webClient);
    }

    /**
     * Starts the eventLoopThread_.
     */
    protected void startThreadIfNeeded() {
        if (eventLoopThread_ == null) {
            eventLoopThread_ = new Thread(this, getThreadName());
            eventLoopThread_.setDaemon(true);
            eventLoopThread_.start();
        }
    }

    /**
     * Defines the thread name; overload if needed.
     * @return the name of the js executor thread
     */
    protected String getThreadName() {
        return "JS executor for " + webClient_.get();
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
            LOG.warn("InterruptedException while waiting for the eventLoop thread to join ", e);
            // ignore, this doesn't matter, we want to stop it
        }
        if (eventLoopThread_.isAlive()) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Event loop thread "
                        + eventLoopThread_.getName()
                        + " still alive at "
                        + System.currentTimeMillis());
                LOG.warn("Event loop thread will be stopped");
            }

            // Und bist du nicht willig
            eventLoopThread_.stop();
        }
    }

    /**
     * Returns the JobExecutor corresponding to the earliest job.
     * @return the JobExectuor with the earliest job.
     */
    protected synchronized JavaScriptJobManager getJobManagerWithEarliestJob() {
        JavaScriptJobManager javaScriptJobManager = null;
        JavaScriptJob earliestJob = null;
        // iterate over the list and find the earliest job to run.
        final Iterator<WeakReference<JavaScriptJobManager>> managers = jobManagerList_.iterator();
        while (managers.hasNext()) {
            final JavaScriptJobManager jobManager = managers.next().get();
            if (jobManager == null) {
                managers.remove();
            }
            else {
                final JavaScriptJob newJob = jobManager.getEarliestJob();
                if (newJob != null) {
                    if (earliestJob == null || earliestJob.compareTo(newJob) > 0) {
                        earliestJob = newJob;
                        javaScriptJobManager = jobManager;
                    }
                }
            }
        }
        return javaScriptJobManager;
    }

    /**
     * Executes the jobs in the eventLoop till timeoutMillis expires or the eventLoop becomes empty.
     * No use in non-GAE mode.
     * @param timeoutMillis the timeout in milliseconds
     * @return the number of jobs executed
     */
    public int pumpEventLoop(final long timeoutMillis) {
        return 0;
    }

    /** Runs the eventLoop. */
    public void run() {
        final boolean trace = LOG.isTraceEnabled();
        // this has to be a multiple of 10ms
        // otherwise the VM has to fight with the OS to get such small periods
        final long sleepInterval = 10;
        while (!shutdown_ && webClient_.get() != null) {
            if (trace) {
                LOG.trace("started finding earliestJob at " + System.currentTimeMillis());
            }
            final JavaScriptJobManager jobManager = getJobManagerWithEarliestJob();
            if (trace) {
                LOG.trace("stopped finding earliestJob at " + System.currentTimeMillis());
            }

            if (jobManager != null) {
                final JavaScriptJob earliestJob = jobManager.getEarliestJob();
                if (earliestJob != null) {
                    final long waitTime = earliestJob.getTargetExecutionTime() - System.currentTimeMillis();

                    // do we have to execute the earliest job
                    if (waitTime < 1) {
                        // execute the earliest job
                        if (trace) {
                            LOG.trace("started executing job at " + System.currentTimeMillis());
                        }
                        jobManager.runSingleJob(earliestJob);
                        if (trace) {
                            LOG.trace("stopped executing job at " + System.currentTimeMillis());
                        }

                        // job is done, have a look for another one
                        continue;
                    }
                }
            }

            // check for cancel
            if (shutdown_ || webClient_.get() == null) {
                break;
            }

            // nothing to do, let's sleep a bit
            try {
                Thread.sleep(sleepInterval);
            }
            catch (final InterruptedException e) {
                // nothing, probably a shutdown notification
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
        for (final WeakReference<JavaScriptJobManager> jobManagerRef : jobManagerList_) {
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

        webClient_.clear();
        jobManagerList_.clear();
    }
}
