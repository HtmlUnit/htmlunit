/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.background;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
 * @author Daniel Gredler
 * @author Katharina Probst
 * @author Amit Manjhi
 * @author Ronald Brill
 * @author Carsten Steul
 */
class JavaScriptJobManagerImpl implements JavaScriptJobManager {

    /**
     * The window to which this job manager belongs (weakly referenced, so as not
     * to leak memory).
     */
    private final transient WeakReference<WebWindow> window_;

    /**
     * Queue of jobs that are scheduled to run. This is a priority queue, sorted
     * by closest target execution time.
     */
    private transient PriorityQueue<JavaScriptJob> scheduledJobsQ_ = new PriorityQueue<>();

    private transient ArrayList<Integer> cancelledJobs_ = new ArrayList<>();

    private transient JavaScriptJob currentlyRunningJob_;

    /** A counter used to generate the IDs assigned to {@link JavaScriptJob}s. */
    private static final AtomicInteger NEXT_JOB_ID_ = new AtomicInteger(1);

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(JavaScriptJobManagerImpl.class);

    /**
     * Creates a new instance.
     *
     * @param window the window associated with the new job manager
     */
    JavaScriptJobManagerImpl(final WebWindow window) {
        window_ = new WeakReference<>(window);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized int getJobCount() {
        return scheduledJobsQ_.size() + (currentlyRunningJob_ != null ? 1 : 0);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized int getJobCount(final JavaScriptJobFilter filter) {
        if (filter == null) {
            return scheduledJobsQ_.size() + (currentlyRunningJob_ != null ? 1 : 0);
        }

        int count = 0;
        if (currentlyRunningJob_ != null && filter.passes(currentlyRunningJob_)) {
            count++;
        }
        for (final JavaScriptJob job : scheduledJobsQ_) {
            if (filter.passes(job)) {
                count++;
            }
        }
        return count;
    }

    /** {@inheritDoc} */
    @Override
    public int addJob(final JavaScriptJob job, final Page page) {
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
        job.setId(Integer.valueOf(id));

        synchronized (this) {
            scheduledJobsQ_.add(job);

            if (LOG.isDebugEnabled()) {
                LOG.debug("job added to queue");
                LOG.debug("    window is: " + w);
                LOG.debug("    added job: " + job.toString());
                LOG.debug("after adding job to the queue, the queue is: ");
                printQueue();
            }

            notify();
        }

        return id;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void removeJob(final int id) {
        for (final JavaScriptJob job : scheduledJobsQ_) {
            final int jobId = job.getId().intValue();
            if (jobId == id) {
                scheduledJobsQ_.remove(job);
                break;
            }
        }
        cancelledJobs_.add(Integer.valueOf(id));
        notify();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void stopJob(final int id) {
        for (final JavaScriptJob job : scheduledJobsQ_) {
            final int jobId = job.getId().intValue();
            if (jobId == id) {
                scheduledJobsQ_.remove(job);
                // TODO: should we try to interrupt the job if it is running?
                break;
            }
        }
        cancelledJobs_.add(Integer.valueOf(id));
        notify();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void removeAllJobs() {
        if (currentlyRunningJob_ != null) {
            cancelledJobs_.add(currentlyRunningJob_.getId());
        }
        for (final JavaScriptJob job : scheduledJobsQ_) {
            cancelledJobs_.add(job.getId());
        }
        scheduledJobsQ_.clear();
        notify();
    }

    /** {@inheritDoc} */
    @Override
    public int waitForJobs(final long timeoutMillis) {
        final boolean debug = LOG.isDebugEnabled();
        if (debug) {
            LOG.debug("Waiting for all jobs to finish (will wait max " + timeoutMillis + " millis).");
        }
        if (timeoutMillis > 0) {
            long now = System.currentTimeMillis();
            final long end = now + timeoutMillis;

            synchronized (this) {
                while (getJobCount() > 0 && now < end) {
                    try {
                        wait(end - now);
                    }
                    catch (final InterruptedException e) {
                        LOG.error("InterruptedException while in waitForJobs", e);
                    }
                    // maybe a change triggers the wakup; we have to recalculate the
                    // wait time
                    now = System.currentTimeMillis();
                }
            }
        }
        final int jobs = getJobCount();
        if (debug) {
            LOG.debug("Finished waiting for all jobs to finish (final job count is " + jobs + ").");
        }
        return jobs;
    }

    /** {@inheritDoc} */
    @Override
    public int waitForJobsStartingBefore(final long delayMillis) {
        return waitForJobsStartingBefore(delayMillis, null);
    }

    /** {@inheritDoc} */
    @Override
    public int waitForJobsStartingBefore(final long delayMillis, final JavaScriptJobFilter filter) {
        final boolean debug = LOG.isDebugEnabled();

        final long latestExecutionTime = System.currentTimeMillis() + delayMillis;
        if (debug) {
            LOG.debug("Waiting for all jobs that have execution time before "
                  + delayMillis + " (" + latestExecutionTime + ") to finish");
        }

        final long interval = Math.max(40, delayMillis);
        synchronized (this) {
            JavaScriptJob earliestJob = getEarliestJob(filter);
            boolean pending = earliestJob != null && earliestJob.getTargetExecutionTime() < latestExecutionTime;
            pending = pending
                    || (
                            currentlyRunningJob_ != null
                            && (filter == null || filter.passes(currentlyRunningJob_))
                            && currentlyRunningJob_.getTargetExecutionTime() < latestExecutionTime
                       );

            while (pending) {
                try {
                    wait(interval);
                }
                catch (final InterruptedException e) {
                    LOG.error("InterruptedException while in waitForJobsStartingBefore", e);
                }

                earliestJob = getEarliestJob(filter);
                pending = earliestJob != null && earliestJob.getTargetExecutionTime() < latestExecutionTime;
                pending = pending
                        || (
                                currentlyRunningJob_ != null
                                && (filter == null || filter.passes(currentlyRunningJob_))
                                && currentlyRunningJob_.getTargetExecutionTime() < latestExecutionTime
                           );
            }
        }

        final int jobs = getJobCount(filter);
        if (debug) {
            LOG.debug("Finished waiting for all jobs that have target execution time earlier than "
                + latestExecutionTime + ", final job count is " + jobs);
        }
        return jobs;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void shutdown() {
        scheduledJobsQ_.clear();
        notify();
    }

    /**
     * Returns the window to which this job manager belongs, or {@code null} if
     * it has been garbage collected.
     *
     * @return the window to which this job manager belongs, or {@code null} if
     *         it has been garbage collected
     */
    private WebWindow getWindow() {
        return window_.get();
    }

    /**
     * Utility method to print current queue.
     */
    private void printQueue() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("------ printing JavaScript job queue -----");
            LOG.debug("  number of jobs on the queue: " + scheduledJobsQ_.size());
            int count = 1;
            for (final JavaScriptJob job : scheduledJobsQ_) {
                LOG.debug("  " + count + ")  Job target execution time: " + job.getTargetExecutionTime());
                LOG.debug("      job to string: " + job.toString());
                LOG.debug("      job id: " + job.getId());
                if (job.isPeriodic()) {
                    LOG.debug("      period: " + job.getPeriod().intValue());
                }
                count++;
            }
            LOG.debug("------------------------------------------");
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     */
    @Override
    public synchronized String jobStatusDump(final JavaScriptJobFilter filter) {
        final String lineSeparator = System.lineSeparator();

        final StringBuilder status = new StringBuilder(110)
                .append("------ JavaScript job status -----")
                .append(lineSeparator);

        if (null != currentlyRunningJob_ && (filter == null || filter.passes(currentlyRunningJob_))) {
            status.append("  current running job: ").append(currentlyRunningJob_.toString())
                .append("      job id: " + currentlyRunningJob_.getId())
                .append(lineSeparator)
                .append(lineSeparator)
                .append(lineSeparator);
        }
        status.append("  number of jobs on the queue: ")
            .append(Integer.toString(scheduledJobsQ_.size()))
            .append(lineSeparator);

        int count = 1;
        for (final JavaScriptJob job : scheduledJobsQ_) {
            if (filter == null || filter.passes(job)) {
                final long now = System.currentTimeMillis();
                final long execTime = job.getTargetExecutionTime();
                status.append("  " + count)
                    .append(")  Job target execution time: " + execTime)
                    .append(" (should start in " + ((execTime - now) / 1000d) + "s)")
                    .append(lineSeparator)
                    .append("      job to string: ").append(job.toString())
                    .append(lineSeparator)
                    .append("      job id: " + job.getId())
                    .append(lineSeparator);
                if (job.isPeriodic()) {
                    status.append("      period: ")
                        .append(job.getPeriod().toString())
                        .append(lineSeparator);
                }
                count++;
            }
        }
        status.append("------------------------------------------")
            .append(lineSeparator);

        return status.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JavaScriptJob getEarliestJob() {
        return scheduledJobsQ_.peek();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized JavaScriptJob getEarliestJob(final JavaScriptJobFilter filter) {
        if (filter == null) {
            return scheduledJobsQ_.peek();
        }

        for (final JavaScriptJob job : scheduledJobsQ_) {
            if (filter.passes(job)) {
                return job;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean runSingleJob(final JavaScriptJob givenJob) {
        assert givenJob != null;
        final JavaScriptJob job = getEarliestJob();
        if (job != givenJob) {
            return false;
        }

        final long currentTime = System.currentTimeMillis();
        if (job.getTargetExecutionTime() > currentTime) {
            return false;
        }
        synchronized (this) {
            if (scheduledJobsQ_.remove(job)) {
                currentlyRunningJob_ = job;
            }
            // no need to notify if processing is started
        }

        final boolean debug = LOG.isDebugEnabled();
        final boolean isPeriodicJob = job.isPeriodic();
        if (isPeriodicJob) {
            final long jobPeriod = job.getPeriod().longValue();

            // reference: http://ejohn.org/blog/how-javascript-timers-work/
            long timeDifference = currentTime - job.getTargetExecutionTime();
            timeDifference = (timeDifference / jobPeriod) * jobPeriod + jobPeriod;
            job.setTargetExecutionTime(job.getTargetExecutionTime() + timeDifference);

            // queue
            synchronized (this) {
                if (!cancelledJobs_.contains(job.getId())) {
                    if (debug) {
                        LOG.debug("Reschedulling job " + job);
                    }
                    scheduledJobsQ_.add(job);
                    notify();
                }
            }
        }
        if (debug) {
            final String periodicJob = isPeriodicJob ? "interval " : "";
            LOG.debug("Starting " + periodicJob + "job " + job);
        }
        try {
            job.run();
        }
        catch (final RuntimeException e) {
            LOG.error("Job run failed with unexpected RuntimeException: " + e.getMessage(), e);
        }
        finally {
            synchronized (this) {
                if (job == currentlyRunningJob_) {
                    currentlyRunningJob_ = null;
                }
                notify();
            }
        }
        if (debug) {
            final String periodicJob = isPeriodicJob ? "interval " : "";
            LOG.debug("Finished " + periodicJob + "job " + job);
        }
        return true;
    }

    /**
     * Our own serialization (to handle the weak reference)
     * @param in the stream to read form
     * @throws IOException in case of error
     * @throws ClassNotFoundException in case of error
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        // we do not store the jobs (at the moment)
        scheduledJobsQ_ = new PriorityQueue<>();
        cancelledJobs_ = new ArrayList<>();
        currentlyRunningJob_ = null;
    }
}
