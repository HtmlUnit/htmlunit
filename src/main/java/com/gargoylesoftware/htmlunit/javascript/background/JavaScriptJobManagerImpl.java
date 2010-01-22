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

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
 * @see MemoryLeakTest
 */
public class JavaScriptJobManagerImpl implements JavaScriptJobManager {

    /** Serial version UID. */
    private static final long serialVersionUID = 9212855747249248967L;

    /** The window to which this job manager belongs (weakly referenced, so as not to leak memory). */
    private transient WeakReference<WebWindow> window_;

    /** Single-threaded scheduled executor which executes the {@link JavaScriptJob}s behind the scenes. */
    private transient ScheduledThreadPoolExecutor executor_;

    /** The job IDs and their corresponding {@link Future}s, which can be used to cancel the associated jobs. */
    private transient Map<Integer, ScheduledFuture< ? >> futures_;

    /** The job(s) which are currently running. */
    private transient List<JavaScriptJob> currentlyRunningJobs_;

    /** A counter used to generate the IDs assigned to {@link JavaScriptJob}s. */
    private static final AtomicInteger NEXT_JOB_ID = new AtomicInteger(1);

    /** A counter used to generate the IDs assigned to threads. */
    private static final AtomicInteger NEXT_THREAD_ID = new AtomicInteger(1);

    /** Priority to use for background threads (bigger than the current thread's so that JS jobs execute ASAP). */
    private static final int PRIORITY = Math.min(Thread.MAX_PRIORITY, currentThread().getPriority() + 1);

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(JavaScriptJobManagerImpl.class);

    private Thread executorThread_; // keep a reference to be able to call join(...) on it

    /**
     * Helper to track the job being executed.
     */
    class ExecutingJobTracker implements Runnable {
        private final JavaScriptJob job_;
        ExecutingJobTracker(final JavaScriptJob job) {
            job_ = job;
        }
        public void run() {
            LOG.debug("Running job " + job_);
            synchronized (currentlyRunningJobs_) {
                currentlyRunningJobs_.add(job_);
                currentlyRunningJobs_.notifyAll();
            }
            try {
                job_.run();
            }
            catch (final RuntimeException e) {
                LOG.error("Job run failed with unexpected RuntimeException: " + e.getMessage(), e);
                throw e;
            }
            finally {
                synchronized (currentlyRunningJobs_) {
                    currentlyRunningJobs_.remove(job_);
                    currentlyRunningJobs_.notifyAll();
                }
            }
        }
    }

    /**
     * Creates a new instance.
     * @param window the window associated with the new job manager
     */
    public JavaScriptJobManagerImpl(final WebWindow window) {
        init(window);
    }

    /**
     * Initializes this job manager, using the specified window.
     * @param window the window associated with this job manager
     */
    private void init(final WebWindow window) {
        window_ = new WeakReference<WebWindow>(window);
        executor_ = new ScheduledThreadPoolExecutor(1);
        futures_ = new TreeMap<Integer, ScheduledFuture< ? >>();
        currentlyRunningJobs_ = new ArrayList<JavaScriptJob>();
        executor_.setThreadFactory(new ThreadFactory() {
            public Thread newThread(final Runnable r) {
                // Make sure the thread is a daemon thread so that it doesn't keep the JVM
                // running unnecessarily; we also bump up the thread's priority so that
                // JavaScript jobs execute ASAP.
                final String name = "JavaScript Job Thread " + NEXT_THREAD_ID.getAndIncrement();
                executorThread_ = new Thread(r, name);
                executorThread_.setDaemon(true);
                executorThread_.setPriority(PRIORITY);
                return executorThread_;
            }
        });
    }

    /** {@inheritDoc} */
    public int getJobCount() {
        if (executor_.isShutdown()) {
            return 0;
        }

        // This method reads the job count a couple of times, to make sure that the count returned
        // is stable; the underlying ThreadPoolExecutor API only guarantees approximate results, so
        // we need to do a little bit of extra work to ensure that we return results that are as
        // reliable as possible.
        int count1 = Integer.MIN_VALUE;
        int count2 = Integer.MAX_VALUE;
        while (count1 != count2) {
            count1 = getJobCountInner();
            sleep(10);
            count2 = getJobCountInner();
        }
        return count1;
    }

    /**
     * Returns the approximate number of jobs currently executing and waiting to be executed. This
     * method can only guarantee approximate results, because these are the only guarantees provided
     * by the {@link ScheduledThreadPoolExecutor} API which this method is built on.
     * @return the approximate number of jobs currently executing and waiting to be executed
     */
    private synchronized int getJobCountInner() {
        executor_.purge();
        return (int) (executor_.getTaskCount() - executor_.getCompletedTaskCount());
    }

    /** {@inheritDoc} */
    public synchronized int addJob(final JavaScriptJob job, final Page page) {
        final WebWindow w = getWindow();
        if (w == null) {
            // The window to which this job manager belongs has been garbage collected.
            // Don't spawn any more jobs for it.
            return 0;
        }
        if (w.getEnclosedPage() != page) {
            // The page requesting the addition of the job is no longer contained by our owner window.
            // Don't let it spawn any more jobs.
            return 0;
        }

        final int id = NEXT_JOB_ID.getAndIncrement();
        job.setId(id);

        final ExecutingJobTracker jobWrapper = new ExecutingJobTracker(job);
        final ScheduledFuture< ? > future;
        if (job.isPeriodic()) {
            future = executor_.scheduleAtFixedRate(jobWrapper, job.getInitialDelay(), job.getPeriod(), MILLISECONDS);
        }
        else {
            future = executor_.schedule(jobWrapper, job.getInitialDelay(), MILLISECONDS);
        }

        futures_.put(id, future);
        LOG.debug("Added job: " + job + ".");
        return id;
    }

    /** {@inheritDoc} */
    public synchronized void removeJob(final int id) {
        final ScheduledFuture< ? > future = futures_.remove(id);
        if (future != null) {
            LOG.debug("Removing job " + id + ".");
            future.cancel(false);
            LOG.debug("Removed job " + id + ".");
        }
    }

    /** {@inheritDoc} */
    public synchronized void stopJob(final int id) {
        final Future< ? > future = futures_.remove(id);
        if (future != null) {
            LOG.debug("Stopping job " + id + ".");
            future.cancel(true);
            LOG.debug("Stopped job " + id + ".");
        }
    }

    /** {@inheritDoc} */
    public synchronized void removeAllJobs() {
        LOG.debug("Removing all jobs.");
        int count = 0;
        for (final Future< ? > future : futures_.values()) {
            future.cancel(false);
            ++count;
        }
        futures_.clear();
        if (count > 0) {
            LOG.debug("Removed all jobs (" + count + ").");
        }
    }

    /** {@inheritDoc} */
    public int waitForJobs(final long timeoutMillis) {
        LOG.debug("Waiting for all jobs to finish (will wait max " + timeoutMillis + " millis).");
        if (timeoutMillis > 0) {
            final long start = System.currentTimeMillis();
            final long interval = Math.min(timeoutMillis, 100);
            while (getJobCount() > 0 && System.currentTimeMillis() - start < timeoutMillis) {
                sleep(interval);
            }
        }
        final int jobs = getJobCount();
        LOG.debug("Finished waiting for all jobs to finish (final job count is " + jobs + ").");
        return jobs;
    }

    /** {@inheritDoc} */
    public int waitForJobsStartingBefore(final long delayMillis) {
        LOG.debug("Waiting for all jobs to finish that start within " + delayMillis + " millis.");
        final long maxStartTime = System.currentTimeMillis() + delayMillis;
        try {
            ScheduledFuture< ? > lastJobWithinDelay = getLastJobStartingBefore(maxStartTime);
            if (lastJobWithinDelay == null) {
                waitForCurrentlyRunningJobs();
                lastJobWithinDelay = getLastJobStartingBefore(maxStartTime);
            }
            while (lastJobWithinDelay != null) {
                waitForCompletion(lastJobWithinDelay);
                waitForCurrentlyRunningJobs();
                lastJobWithinDelay = getLastJobStartingBefore(maxStartTime);
            }
        }
        catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
        final int jobs = getJobCount();
        LOG.debug("Finished waiting for all jobs to finish (final job count is " + jobs + ").");
        return jobs;
    }

    /**
     * Waits until currently running jobs (if any) are finished.
     */
    private void waitForCurrentlyRunningJobs() throws InterruptedException {
        synchronized (currentlyRunningJobs_) {
            while (!currentlyRunningJobs_.isEmpty()) {
                final JavaScriptJob job = currentlyRunningJobs_.get(0);
                while (currentlyRunningJobs_.contains(job)) {
                    currentlyRunningJobs_.wait();
                }
            }
        }
    }

    /**
     * Returns the last job starting before <tt>maxStartTime</tt>.
     * @param maxStartTime the maximum start time to look for
     * @return the last job starting before <tt>maxStartTime</tt>
     */
    private synchronized ScheduledFuture< ? > getLastJobStartingBefore(final long maxStartTime) {
        long currentDelay = Long.MIN_VALUE;
        ScheduledFuture< ? > job = null;
        final long maxAllowedDelay = maxStartTime - System.currentTimeMillis();
        for (final ScheduledFuture< ? > future : futures_.values()) {
            final long delay = future.getDelay(TimeUnit.MILLISECONDS);
            if (!future.isDone() && delay > currentDelay && (delay < maxAllowedDelay || delay <= 0)) {
                currentDelay = delay;
                job = future;
            }
        }
        LOG.debug("Last job starting before " + maxStartTime + ": " + job + ".");
        return job;
    }

    /**
     * Blocks until the specified job finishes running.
     * @param job the job that will be executed
     * @return <tt>true</tt> if the job finished running normally
     */
    private boolean waitForCompletion(final ScheduledFuture< ? > job) {
        try {
            final long delay = job.getDelay(TimeUnit.MILLISECONDS);
            LOG.debug("Waiting for completion of job starting in " + delay + "ms: " + job);
            // job.get() is not safe here because the job may have been completed
            // before get() is called.
            // See WebClientWaitForBackgroundJobsTest#waitForBackgroundJavaScriptStartingBefore_hangs
            job.get(delay + 100, TimeUnit.MILLISECONDS);
            LOG.debug("Job done: " + job.isDone() + ".");
        }
        catch (final CancellationException e) {
            LOG.debug("Job cancelled: " + job + ".");
            return false;
        }
        catch (final InterruptedException e) {
            LOG.debug(e.getMessage(), e);
            return false;
        }
        catch (final ExecutionException e) {
            LOG.debug(e.getMessage(), e);
            return false;
        }
        catch (final TimeoutException e) {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    public synchronized void shutdown() {
        executor_.purge();
        final List<Runnable> neverStartedTasks = executor_.shutdownNow();
        futures_.clear();
        if (executorThread_ != null) {
            try {
                executorThread_.join(5000);
            }
            catch (final InterruptedException e) {
                // ignore, this doesn't matter, we want to stop it
            }
            if (executorThread_.isAlive()) {
                LOG.warn("Executor thread " + executorThread_.getName() + " still alive");
            }
        }
        if (getJobCount() - neverStartedTasks.size() > 0) {
            LOG.warn("jobCount: " + getJobCount() + "(taskCount: " + executor_.getTaskCount()
                + ", completedTaskCount: " + executor_.getCompletedTaskCount()
                + ", never started tasks: " + neverStartedTasks.size() + ")");
        }
    }

    /**
     * Returns the window to which this job manager belongs, or <tt>null</tt> if it has been garbage collected.
     * @return the window to which this job manager belongs, or <tt>null</tt> if it has been garbage collected
     */
    private WebWindow getWindow() {
        return window_.get();
    }

    /**
     * Does a best effort attempt at sleeping the specified number of milliseconds. This method may
     * return early if the current thread is interrupted while it is sleeping.
     * @param millis the number of milliseconds to try to sleep
     */
    private void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (final InterruptedException e) {
            // Ignore; we did our best.
        }
    }

    /**
     * The only thing we want to keep when we serialize is the reference to the window.
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeObject(window_.get());
    }

    /**
     * When we deserialize, start over based on the window reference.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        final WebWindow window = (WebWindow) in.readObject();
        init(window);
    }

}
