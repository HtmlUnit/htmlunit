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

/**
 * A manager for {@link JavaScriptJob}s.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public interface JavaScriptJobManager {

    /**
     * Returns the number of active jobs, including jobs that are currently executing and jobs that are
     * waiting to execute.
     * @return the number of active jobs
     */
    int getJobCount();

    /**
     * Adds the specified job to this job manager, assigning it an ID. If the specified page is not currently
     * loaded in the window which owns this job manager, the operation fails and this method returns <tt>0</tt>.
     * @param job the job to add to the job manager
     * @param delay the amount of time to wait before executing the specified job (in milliseconds)
     * @return the ID assigned to the job
     */
    int addJob(final JavaScriptJob job, final int delay);

    /**
     * Removes the scheduled job from the execution queue. This doesn't interrupt the job if it is currently running.
     * @param id the ID of the job to be removed from schedule
     */
    void removeScheduledJob(final int id);

    /**
     * Stops the specified job <b>now</b>, not even allowing the job to finish if it is currently executing.
     * @param id the ID of the job to be stopped
     */
    void stopJobNow(final int id);

    /**
     * Stops all jobs <b>as soon as possible</b>, allowing the jobs to finish if they are currently executing.
     */
    void stopAllJobsAsap();

    /**
     * Blocks until all active jobs have finished executing. If a job is scheduled later than
     * maxWaitMillis this method will wait for maxWaitMillis and return false.
     * @param maxWaitMillis the maximum amount of time to wait (in milliseconds)
     * @return <tt>true</tt> if all threads expired within the specified time
     */
    boolean waitForAllJobsToFinish(final long maxWaitMillis);

    /**
     * Kill this job manager.
     */
    void shutdown();

    /**
     * Waits for jobs within the delay period to complete. Any jobs that would
     * start after the delay period are ignored.
     *
     * This method will return immediately if no jobs are scheduled within the
     * given delay period.
     *
     * @param delayMillis How long this method should wait.
     */
    void waitForJobsWithinDelayToFinish(long delayMillis);

}
