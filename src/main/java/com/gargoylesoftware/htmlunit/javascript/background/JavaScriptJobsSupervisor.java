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

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * A supervisor for all {@link JavaScriptJob}s of a {@link com.gargoylesoftware.htmlunit.WebClient}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class JavaScriptJobsSupervisor implements Serializable {
    private static final long serialVersionUID = -5446829461640355490L;
    private static final Log LOG = LogFactory.getLog(JavaScriptJobsSupervisor.class);
    private final Map<Runnable, WeakReference<ScheduledFuture< ? >>> job2Future_
        = new WeakHashMap<Runnable, WeakReference<ScheduledFuture< ? >>>();
    private final List<Runnable> currentlyRunningJobs_ = new ArrayList<Runnable>();

    /**
     * Blocks until all jobs scheduled within the specified time have finished executing.
     * If no job is scheduled within the specified delay, this method will return immediately even if
     * jobs are scheduled for a later time.
     * @param delayMillis the delay determining jobs that should be executed
     */
    public void waitForJobsWithinDelayToFinish(final long delayMillis) {
        LOG.debug("Waiting for all jobs to finish that start within " + delayMillis + "ms.");
        final long maxStartTime = System.currentTimeMillis() + delayMillis;

        try {
            do_waitForJobsWithinDelayToFinish(maxStartTime);
        }
        catch (final InterruptedException e) {
            // should we throw this directly or wrap it?
            throw new RuntimeException(e);
        }
    }

    private void do_waitForJobsWithinDelayToFinish(final long maxStartTime) throws InterruptedException {
        // look for jobs that will start within the given time
        ScheduledFuture< ? > lastJobWithinDelay = getLastJobStartingBefore(maxStartTime);

        if (lastJobWithinDelay == null) {
            waitForCurrentlyRunningJobs();
            lastJobWithinDelay = getLastJobStartingBefore(maxStartTime);
        }
        // if lastJobWithinDelay is cancelled, we have to look for an other one
        while (lastJobWithinDelay != null) {
            waitForCompletion(lastJobWithinDelay);
            waitForCurrentlyRunningJobs();
            lastJobWithinDelay = getLastJobStartingBefore(maxStartTime);
        }

        LOG.debug("Finished waiting for all jobs to finish that start within provided delay");
    }

    /**
     * Waits until currently jobs (if any) are finished.
     */
    private void waitForCurrentlyRunningJobs() throws InterruptedException {
        synchronized (currentlyRunningJobs_) {
            while (!currentlyRunningJobs_.isEmpty()) {
                // pick one running job
                final Runnable job = currentlyRunningJobs_.get(0);
                while (currentlyRunningJobs_.contains(job)) {
                    currentlyRunningJobs_.wait();
                }
            }
            LOG.debug("No job running currently");
        }
    }

    /**
     * Wait for completion of a job
     * @param job the job that will be executed
     * @return <code>true</code> if the job finished normally
     */
    private boolean waitForCompletion(final ScheduledFuture< ? > job) {
        try {
            LOG.debug("waiting for completion of: " + job);
            job.get();
            LOG.debug("job done: " + job.isDone());
        }
        catch (final CancellationException e) {
            LOG.debug("CancellationException for " + job);
            return false;
        }
        catch (final InterruptedException e) {
            LOG.debug("InterruptedException", e);
            return false;
        }
        catch (final ExecutionException e) {
            LOG.debug("ExecutionException", e);
            return false;
        }
        return true;
    }

    private synchronized ScheduledFuture< ? > getLastJobStartingBefore(final long maxStartTime) {
        long currentDelay = 0;
        ScheduledFuture< ? > job = null;
        final long maxAllowedDelay = maxStartTime - System.currentTimeMillis();
        for (final WeakReference<ScheduledFuture< ? >> futureRef : job2Future_.values()) {
            final ScheduledFuture< ? > future = futureRef.get();
            if (future != null) {
                final long delay = future.getDelay(TimeUnit.MILLISECONDS);
                if (future.isDone()) {
                    // TODO
                }
                else if (delay > currentDelay && delay < maxAllowedDelay) {
                    currentDelay = delay;
                    job = future;
                }
            }
        }

        LOG.debug("Last job starting before " + maxStartTime + ": " + job);
        return job;
    }

    void executionStarted(final Runnable job) {
        synchronized (currentlyRunningJobs_) {
            currentlyRunningJobs_.add(job);
            LOG.debug("Currently running jobs: " + currentlyRunningJobs_.size());
            currentlyRunningJobs_.notifyAll();
        }
    }

    void executionFinished(final Runnable job) {
        synchronized (currentlyRunningJobs_) {
            currentlyRunningJobs_.remove(job);
            LOG.debug("Currently running jobs: " + currentlyRunningJobs_.size());
            currentlyRunningJobs_.notifyAll();
        }
    }

    synchronized void jobAdded(final JavaScriptJob job, final ScheduledFuture< ? > future) {
        job2Future_.put(job, new WeakReference<ScheduledFuture< ? >>(future));
    }

    /**
     * Creates a job manager for the specified window.
     * @param webWindow the window
     * @return a JS job manager for this window
     */
    public JavaScriptJobManager createJobManager(final WebWindow webWindow) {
        return new JavaScriptJobManagerImpl(this, webWindow);
    }
}
