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

import org.mozilla.javascript.Function;

import com.gargoylesoftware.htmlunit.Page;

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
     * Adds a job which executes the specified JavaScript code after the specified amount of time. If the specified
     * page is not currently loaded in the window which owns this job manager, the operation fails and this method
     * returns <tt>0</tt>.
     * @param code the JavaScript code to be executed
     * @param delay the amount of time to wait before executing the specified JavaScript code (in milliseconds)
     * @param description a text description of the job being added
     * @param page the page which is trying to add the job
     * @return the ID assigned to the job
     */
    int addJob(final String code, final int delay, final String description, final Page page);

    /**
     * Adds a job which executes the specified JavaScript code after the specified amount of time. If the specified
     * page is not currently loaded in the window which owns this job manager, the operation fails and this method
     * returns <tt>0</tt>.
     * @param code the JavaScript code to be executed
     * @param delay the amount of time to wait before executing the specified JavaScript code (in milliseconds)
     * @param description a text description of the job being added
     * @param page the page which is trying to add the job
     * @return the ID assigned to the job
     */
    int addJob(final Function code, final int delay, final String description, final Page page);

    /**
     * Adds the specified job to this job manager, assigning it an ID. If the specified page is not currently
     * loaded in the window which owns this job manager, the operation fails and this method returns <tt>0</tt>.
     * @param job the job to add to the job manager
     * @param delay the amount of time to wait before executing the specified job (in milliseconds)
     * @param page the page which is trying to add the job
     * @return the ID assigned to the job
     */
    int addJob(final JavaScriptJob job, final int delay, final Page page);

    /**
     * Adds a job which executes the specified JavaScript code periodically. If the specified page is not currently
     * loaded in the window which owns this job manager, the operation fails and this method returns <tt>0</tt>.
     * @param code the JavaScript code to be executed
     * @param period the amount of time to wait between JavaScript code executions (in milliseconds)
     * @param description a text description of the job being added
     * @param page the page which is trying to add the job
     * @return the ID assigned to the job
     */
    int addRecurringJob(final String code, final int period, final String description, final Page page);

    /**
     * Adds a job which executes the specified JavaScript code periodically. If the specified page is not currently
     * loaded in the window which owns this job manager, the operation fails and this method returns <tt>0</tt>.
     * @param code the JavaScript code to be executed
     * @param period the amount of time to wait between JavaScript code executions (in milliseconds)
     * @param description a text description of the job being added
     * @param page the page which is trying to add the job
     * @return the ID assigned to the job
     */
    int addRecurringJob(final Function code, final int period, final String description, final Page page);

    /**
     * Adds the specified recurring job to this job manager, assigning it an ID. If the specified page is not currently
     * loaded in the window which owns this job manager, the operation fails and this method returns <tt>0</tt>.
     * @param job the recurring job to add to the job manager
     * @param period the amount of time to wait between job executions (in milliseconds)
     * @param page the page which is trying to add the job
     * @return the ID assigned to the recurring job
     */
    int addRecurringJob(JavaScriptJob job, int period, final Page page);

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
}
