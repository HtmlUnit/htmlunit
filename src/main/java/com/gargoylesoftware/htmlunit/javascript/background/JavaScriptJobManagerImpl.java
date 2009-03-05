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
package com.gargoylesoftware.htmlunit.javascript.background;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Default implementation of {@link JavaScriptJobManager}.</p>
 *
 * <p>This job manager class is guaranteed not to keep old windows in memory (no window memory leaks).</p>
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @see MemoryLeakTest
 */
public class JavaScriptJobManagerImpl implements JavaScriptJobManager {

    /** Single-threaded scheduled executor which executes the {@link JavaScriptJob}s behind the scenes. */
    private final HtmlUnitExecutor executor_;

    /** The job IDs and their corresponding {@link Future}s, which can be used to cancel the associated jobs. */
    private final Map<Integer, ScheduledFuture< ? >> futures_ = Collections
            .synchronizedMap(new TreeMap<Integer, ScheduledFuture< ? >>());

    /** A counter used to generate the IDs assigned to {@link JavaScriptJob}s. */
    private static final AtomicInteger NEXT_JOB_ID = new AtomicInteger(1);

    /** A counter used to generate the IDs assigned to threads. */
    private static final AtomicInteger NEXT_THREAD_ID = new AtomicInteger(1);

    /** Priority to use for background threads (bigger than the current thread's so that JS jobs execute ASAP). */
    private static final int PRIORITY = Math.min(
            Thread.MAX_PRIORITY,
            currentThread().getPriority() + 1);

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(JavaScriptJobManagerImpl.class);

    /**
     * Creates a new instance.
     */
    public JavaScriptJobManagerImpl() {
        executor_ = new HtmlUnitExecutor();
        executor_.setThreadFactory(new ThreadFactory() {
            public Thread newThread(final Runnable r) {
                // Make sure the thread is a daemon thread so that it doesn't keep the JVM
                // running unnecessarily; we also bump up the thread's priority so that
                // JavaScript jobs execute ASAP.
                final String name = "JavaScript Job Thread " + NEXT_THREAD_ID.getAndIncrement();
                final Thread t = new Thread(r, name);
                t.setDaemon(true);
                t.setPriority(PRIORITY);
                return t;
            }
        });
        executor_.setEventHandler(new HtmlUnitExecutor.EventHandler() {
            @Override
            public void onFinish(final ScheduledFuture< ? > future) {
                synchronized (JavaScriptJobManagerImpl.this) {
                    if (future.isDone()) {
                        LOG.debug("Done: " + future);
                        final Collection<ScheduledFuture< ? >> values = futures_.values();
                        LOG.debug("Searching for future: " + future);
                        if (values.contains(future)) {
                            LOG.debug("Removing future: " + future);
                            values.remove(future);
                        }
                        LOG.debug("Futures left: " + futures_.size());
                    }
                    else {
                        LOG.debug("NOT Done: " + future);
                    }
                    JavaScriptJobManagerImpl.this.notifyAll();
                }
            }

            @Override
            public void onStart(final ScheduledFuture< ? > future) {
                synchronized (JavaScriptJobManagerImpl.this) {
                    JavaScriptJobManagerImpl.this.notifyAll();
                }
            }

        });
    }
    /** {@inheritDoc} */
    public synchronized int getJobCount() {
        return futures_.size();
    }

    /** {@inheritDoc} */
    public synchronized int addJob(final JavaScriptJob job, final int delay) {
        final int id = NEXT_JOB_ID.getAndIncrement();
        job.setId(id);

        final ScheduledFuture< ? > future;
        if (job.isRecurring()) {
            future = executor_.scheduleAtFixedRate(job, delay, delay, MILLISECONDS);
        }
        else {
            future = executor_.schedule(job, delay, MILLISECONDS);
        }

        futures_.put(id, future);
        LOG.debug("Added job: " + job);
        LOG.debug("Added future: " + future);
        return id;
    }

    /** {@inheritDoc} */
    public synchronized void removeScheduledJob(final int id) {
        final ScheduledFuture< ? > future = futures_.remove(id);
        if (future != null) {
            LOG.debug("Canceling Future: " + future);
            if (!future.cancel(false)) {
                LOG.debug("future.cancel() error!");
            }
            LOG.debug("Removed job from schedule: " + id);
        }
        else {
            LOG.debug("Attempted to remove non-existant Job with ID: " + id);
        }
    }

    /** {@inheritDoc} */
    public synchronized void stopJobNow(final int id) {
        final ScheduledFuture< ? > future = futures_.remove(id);
        if (future != null) {
            LOG.debug("Stopping now " + future);
            future.cancel(true);
            LOG.debug("Stopped job (now): " + id);
        }
        else {
            LOG.debug("Attempted to remove non-existant Job with ID: " + id);
        }
    }

    /** {@inheritDoc} */
    public synchronized void stopAllJobsAsap() {
        LOG.debug("stopAllJobsAsap");
        int nb = 0;
        for (final ScheduledFuture< ? > future : futures_.values()) {
            LOG.debug("Stopping " + future);
            future.cancel(false);
            ++nb;
        }
        futures_.clear();
        if (nb > 0) {
            LOG.debug("Stopped all jobs (" + nb + ")");
        }
    }

    /** {@inheritDoc} */
    public synchronized boolean waitForAllJobsToFinish(final long maxWaitMillis) {
        LOG.debug("Waiting for all jobs to finish (will wait max " + maxWaitMillis + " millis).");
        waitForJobsWithinDelayToFinish(maxWaitMillis);
        final int jobs = getJobCount();
        LOG.debug("Finished waiting for all jobs to finish (final job count is " + jobs + ").");
        return jobs == 0;
    }

    /**
     * Does a best effort attempt at sleeping the specified number of milliseconds. This method may
     * return early if the current thread is interrupted while it is sleeping.
     * @param millis the number of milliseconds to try to sleep
     */
    private synchronized void sleep(final long millis) {
        if (millis > 0) {
            try {
                this.wait(millis);
            }
            catch (final InterruptedException e) {
                LOG.debug("sleep interrupted");
            }
        }
    }

    /** {@inheritDoc} */
    public synchronized void shutdown() {
        executor_.purge();
        final List<Runnable> jobsStillRunning = executor_.shutdownNow();
        futures_.clear(); // This is dangerous
        if (jobsStillRunning.size() > 0) {
            LOG.debug("Jobs still running after shutdown: " + jobsStillRunning.size());
        }
    }

    /** {@inheritDoc} */
    public synchronized void waitForJobsWithinDelayToFinish(final long delay) {
        final long waitUntil = System.currentTimeMillis() + delay;
        LOG.debug("Waiting for delay: " + delay);

        ScheduledFuture< ? > nextJob = getNextStartingJob();
        while (nextJob != null && System.currentTimeMillis() < waitUntil) {
            final long waitTime = nextJob.getDelay(TimeUnit.MILLISECONDS);
            if (System.currentTimeMillis() + waitTime > waitUntil) {
                break;
            }
            final long remainingWaitTime = waitUntil - System.currentTimeMillis();
            LOG.debug("Waiting for " + remainingWaitTime + "ms");
            sleep(remainingWaitTime);
            LOG.debug("Done waiting for " + remainingWaitTime + "ms");
            nextJob = getNextStartingJob();
        }

        LOG.debug("Finished waiting for all jobs to finish that start within provided delay");
    }
    /**
     * @return The next job that's scheduled to start
     */
    public synchronized ScheduledFuture< ? > getNextStartingJob() {
        long lowestDelay = Long.MAX_VALUE;
        ScheduledFuture< ? > nextScheduledFuture = null;
        for (final ScheduledFuture< ? > future : futures_.values()) {
            if (future != null) {
                if (future.isDone() || future.isCancelled()) {
                    continue;
                }
                final long delay = future.getDelay(TimeUnit.MILLISECONDS);
                if (delay < lowestDelay) {
                    lowestDelay = delay;
                    nextScheduledFuture = future;
                }
            }
        }

        LOG.debug("Next Job: " + nextScheduledFuture);
        return nextScheduledFuture;
    }
}
