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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * <p>Default implementation of {@link JavaScriptJobManager}.</p>
 *
 * <p>This job manager class is guaranteed not to keep old windows in memory (no window memory leaks).</p>
 *
 * <p>This job manager is serializable, but any running jobs are transient and are not serialized.</p>
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Katharina Probst
 * @author Amit Manjhi
 * @see MemoryLeakTest
 */
public class JavaScriptJobManagerImpl implements JavaScriptJobManager {

    /** Serial version UID. */
    private static final long serialVersionUID = -8550108829091685593L;

    /**
     * The window to which this job manager belongs (weakly referenced, so as not
     * to leak memory).
     */
    private final transient WeakReference<WebWindow> window_;

    /**
     * Queue of jobs that are scheduled to run. This is a priority queue, sorted
     * by closest target execution time.
     */
    private final PriorityQueue<JavaScriptJob> scheduledJobsQ_ = new PriorityQueue<JavaScriptJob>();

    private final ArrayList<Integer> cancelledJobs_ = new ArrayList<Integer>();

    private JavaScriptJob currentlyRunningJob_ = null;

    /** A counter used to generate the IDs assigned to {@link JavaScriptJob}s. */
    private static final AtomicInteger NEXT_JOB_ID_ = new AtomicInteger(1);

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(JavaScriptJobManagerImpl.class);

    /**
     * Creates a new instance.
     *
     * @param window the window associated with the new job manager
     */
    public JavaScriptJobManagerImpl(final WebWindow window) {
        window_ = new WeakReference<WebWindow>(window);
    }

    /** {@inheritDoc} */
    public synchronized int getJobCount() {
        return scheduledJobsQ_.size() + (currentlyRunningJob_ != null ? 1 : 0);
    }

    /** {@inheritDoc} */
    public synchronized int addJob(final JavaScriptJob job, final Page page) {
        final WebWindow w = getWindow();
        if (w == null) {
            /*
             * The window to which this job manager belongs has been garbage
             * collected. Don't spawn any more jobs for it.
             */
            return 0;
        }
        if (w.getEnclosedPage() != page) {
            /*
             * The page requesting the addition of the job is no longer contained by
             * our owner window. Don't let it spawn any more jobs.
             */
            return 0;
        }
        final int id = NEXT_JOB_ID_.getAndIncrement();
        job.setId(id);

        // compute the target time at which this job should be executed (again)
        final long targetExecutionTime = (new Date()).getTime() + job.getInitialDelay();
        job.setTargetExecutionTime(targetExecutionTime);
        scheduledJobsQ_.add(job);

        if (LOG.isDebugEnabled()) {
            LOG.debug("\twindow is: " + getWindow());
            LOG.debug("\tadded job: " + job.toString());
            LOG.debug("after adding job to the queue, the queue is: ");
            printQueue();
        }

        return id;
    }

    /** {@inheritDoc} */
    public synchronized void removeJob(final int id) {
        for (JavaScriptJob job : scheduledJobsQ_) {
            if (job.getId() == id) {
                scheduledJobsQ_.remove(job);
                break;
            }
        }
        cancelledJobs_.add(id);
    }

    /** {@inheritDoc} */
    public synchronized void stopJob(final int id) {
        for (JavaScriptJob job : scheduledJobsQ_) {
            if (job.getId() == id) {
                scheduledJobsQ_.remove(job);
                // TODO: should we try to interrupt the job if it is running?
                break;
            }
        }
        cancelledJobs_.add(id);
    }

    /** {@inheritDoc} */
    public synchronized void removeAllJobs() {
        if (currentlyRunningJob_ != null) {
            cancelledJobs_.add(currentlyRunningJob_.getId());
        }
        for (JavaScriptJob job : scheduledJobsQ_) {
            cancelledJobs_.add(job.getId());
        }
        scheduledJobsQ_.clear();
    }

    /** {@inheritDoc} */
    public int waitForJobs(final long timeoutMillis) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Waiting for all jobs to finish (will wait max " + timeoutMillis + " millis).");
        }
        if (timeoutMillis > 0) {
            final long start = System.currentTimeMillis();
            final long interval = Math.min(timeoutMillis, 100);
            while (getJobCount() > 0 && System.currentTimeMillis() - start < timeoutMillis) {
                try {
                    Thread.sleep(interval);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                    LOG.error("InterruptedException while in waitForJobs");
                }
            }
        }
        final int jobs = getJobCount();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finished waiting for all jobs to finish (final job count is " + jobs + ").");
        }
        return jobs;
    }

    /** {@inheritDoc} */
    public int waitForJobsStartingBefore(final long delayMillis) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Waiting for all jobs that have execution time before " + delayMillis + " to finish");
        }
        final long targetExecutionTime = System.currentTimeMillis() + delayMillis;
        JavaScriptJob earliestJob = getEarliestJob();
        while ((currentlyRunningJob_ != null && currentlyRunningJob_.getTargetExecutionTime() < targetExecutionTime)
            || (earliestJob != null && earliestJob.getTargetExecutionTime() < targetExecutionTime)) {
            try {
                /* TODO (amitmanjhi): how to set this value? For tests in WebClientWaitForBackgroundJobsTest to
                 * pass reliably, this value can't be more than 100. */
                Thread.sleep(50);
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
                LOG.error("InterruptedException while in waitForJobsStartingBefore");
            }
            earliestJob = getEarliestJob();
        }
        final int jobs = getJobCount();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finished waiting for all jobs that have target execution time earlier than "
                + targetExecutionTime + ", final job count is " + jobs);
        }
        return jobs;
    }

    /** {@inheritDoc} */
    public synchronized void shutdown() {
        scheduledJobsQ_.clear();
    }

    /**
     * Returns the window to which this job manager belongs, or <tt>null</tt> if
     * it has been garbage collected.
     *
     * @return the window to which this job manager belongs, or <tt>null</tt> if
     *         it has been garbage collected
     */
    private WebWindow getWindow() {
        return window_.get();
    }

    /**
     * Runs a JavaScript job that is non-null and ready to be executed.
     *
     * @param job
     */
    private void runJob(final JavaScriptJob job) {
        try {
            job.run();
        }
        catch (final RuntimeException e) {
            LOG.error("Job run failed with unexpected RuntimeException: " + e.getMessage(), e);
        }
    }

    /**
     * Utility method to print current queue.
     */
    private void printQueue() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("------ printing JavaScript job queue -----");
            LOG.debug("number of jobs on the queue: " + scheduledJobsQ_.size());
            for (JavaScriptJob job : scheduledJobsQ_) {
                LOG.debug("\tJob target execution time:" + job.getTargetExecutionTime());
                LOG.debug("\tjob to string: " + job.toString());
                LOG.debug("\tjob id: " + job.getId());
            }
            LOG.debug("------------------------------------------");
        }
    }

    /**
     * Sets currentlyRunningJob to job.
     *
     * @return true if succeeds, false otherwise.
     */
    private synchronized boolean setCurrentlyRunningJob(final JavaScriptJob job) {
        if (job.getTargetExecutionTime() < System.currentTimeMillis()) {
            if (scheduledJobsQ_.remove(job)) {
                currentlyRunningJob_ = job;
            }
            return currentlyRunningJob_ != null;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public JavaScriptJob getEarliestJob() {
        return scheduledJobsQ_.peek();
    }

    /**
     * Attempts to run the job passed in.
     * @param givenJob the job to run
     */
    public void runSingleJob(final JavaScriptJob givenJob) {
        assert givenJob != null;
        JavaScriptJob job = scheduledJobsQ_.peek();
        if (job != givenJob) {
            return;
        }
        setCurrentlyRunningJob(job);
        if (currentlyRunningJob_ == null) {
            return;
        }
        job = currentlyRunningJob_;
        final boolean isIntervalJob = job.getPeriod() != null;
        if (isIntervalJob) {
            job.setTargetExecutionTime(job.getTargetExecutionTime() + job.getPeriod());
            // queue
            if (!cancelledJobs_.contains(job.getId())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Reschedulling job " + job);
                }
                scheduledJobsQ_.add(job);
            }
        }
        final String intervalJob = (isIntervalJob ? "interval " : "");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Starting " + intervalJob + "job " + job);
        }
        runJob(job);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finished " + intervalJob + "job " + job);
        }
        currentlyRunningJob_ = null;
    }
}
