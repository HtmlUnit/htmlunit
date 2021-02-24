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

import java.io.Serializable;

import com.gargoylesoftware.htmlunit.Page;

/**
 * A manager for {@link JavaScriptJob}s.
 *
 * @author Daniel Gredler
 * @author Ronald Brill
 */
public interface JavaScriptJobManager extends Serializable {

    /**
     * Simple filter interface. The caller can use this to filter
     * the jobs of interest in the job list.
     */
    interface JavaScriptJobFilter {

        /**
         * Check if the job passes the filter.
         * @param job the job to check
         * @return true if the job passes the filter
         */
        boolean passes(JavaScriptJob job);
    }

    /**
     * Returns the number of active jobs, including jobs that are currently executing and jobs that are
     * waiting to execute.
     * @return the number of active jobs
     */
    int getJobCount();

    /**
     * Returns the number of active jobs, including jobs that are currently executing and jobs that are
     * waiting to execute. Only jobs passing the filter are counted.
     * @param filter the JavaScriptJobFilter
     * @return the number of active jobs
     */
    int getJobCount(JavaScriptJobFilter filter);

    /**
     * Adds the specified job to this job manager, assigning it an ID. If the specified page is not currently
     * loaded in the window which owns this job manager, the operation fails and this method returns <tt>0</tt>.
     * @param job the job to add to the job manager
     * @param page the page which is trying to add the job
     * @return the ID assigned to the job
     */
    int addJob(JavaScriptJob job, Page page);

    /**
     * Removes the specified job from the execution queue. This doesn't interrupt the job if it is currently running.
     * @param id the ID of the job to be removed from the execution queue
     */
    void removeJob(int id);

    /**
     * Removes all jobs from the execution queue. This doesn't interrupt any jobs that may be currently running.
     */
    void removeAllJobs();

    /**
     * Stops the specified job and removes it from the execution queue, not even allowing the job to finish if it is
     * currently executing.
     * @param id the ID of the job to be stopped
     */
    void stopJob(int id);

    /**
     * Blocks until all active jobs have finished executing. If a job is scheduled to begin executing after
     * <tt>(now + timeoutMillis)</tt>, this method will wait for <tt>timeoutMillis</tt> milliseconds and then
     * return {@code false}.
     * @param timeoutMillis the maximum amount of time to wait (in milliseconds); may be negative, in which
     *        case this method returns immediately
     * @return the number of background JavaScript jobs still executing or waiting to be executed when this
     *         method returns; will be <tt>0</tt> if there are no jobs left to execute
     */
    int waitForJobs(long timeoutMillis);

    /**
     * Blocks until all jobs scheduled to start executing before <tt>(now + delayMillis)</tt> have finished executing.
     * If there is no background JavaScript task currently executing, and there is no background JavaScript task
     * scheduled to start executing within the specified time, this method returns immediately -- even if there are
     * tasks scheduled to be executed after <tt>(now + delayMillis)</tt>.
     * @param delayMillis the delay which determines the background tasks to wait for (in milliseconds);
     *        may be negative, as it is relative to the current time
     * @return the number of background JavaScript jobs still executing or waiting to be executed when this
     *         method returns; will be <tt>0</tt> if there are no jobs left to execute
     */
    int waitForJobsStartingBefore(long delayMillis);

    /**
     * Blocks until all jobs scheduled to start executing before <tt>(now + delayMillis)</tt> have finished executing.
     * If there is no background JavaScript task currently executing, and there is no background JavaScript task
     * scheduled to start executing within the specified time, this method returns immediately -- even if there are
     * tasks scheduled to be executed after <tt>(now + delayMillis)</tt>.
     * @param delayMillis the delay which determines the background tasks to wait for (in milliseconds);
     *        may be negative, as it is relative to the current time
     * @param filter the JavaScriptJobFilter
     * @return the number of background JavaScript jobs still executing or waiting to be executed when this
     *         method returns; will be <tt>0</tt> if there are no jobs left to execute
     */
    int waitForJobsStartingBefore(long delayMillis, JavaScriptJobFilter filter);

    /**
     * Shuts down this job manager and stops all of its jobs.
     */
    void shutdown();

    /**
     * Gets the earliest job for this manager.
     * @return {@code null} if none
     */
    JavaScriptJob getEarliestJob();

    /**
     * Gets the earliest job for this manager.
     * @param filter the JavaScriptJobFilter
     * @return {@code null} if none
     */
    JavaScriptJob getEarliestJob(JavaScriptJobFilter filter);

    /**
     * Runs the provided job if it is the right time for it.
     * @param job the job to run
     * @return returns true if the job was run.
     */
    boolean runSingleJob(JavaScriptJob job);

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Utility method to report the current job status.
     * Might help some tools.
     * @param filter the JavaScriptJobFilter
     * @return the job status report as string
     */
    String jobStatusDump(JavaScriptJobFilter filter);
}
