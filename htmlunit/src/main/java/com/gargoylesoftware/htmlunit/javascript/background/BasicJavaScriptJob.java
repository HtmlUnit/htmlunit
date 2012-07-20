/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
 * A JavaScript-triggered background job managed by a {@link JavaScriptJobManager}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Amit Manjhi
 */
abstract class BasicJavaScriptJob implements JavaScriptJob {

    /** The job ID. */
    private Integer id_;

    /** The initial amount of time to wait before executing this job. */
    private final int initialDelay_;

    /** The amount of time to wait between executions of this job (may be <tt>null</tt>). */
    private final Integer period_;

    private final boolean executeAsap_;

    /**
     * The time at which this job should be executed.
     * Note: the browser will make its best effort to execute the job at the target
     * time, as specified by the timeout/interval.  However, depending on other
     * scheduled jobs, this target time may not be the actual time at which the job
     * is executed.
     */
    private long targetExecutionTime_;

    /** Creates a new job instance that executes once, immediately. */
    public BasicJavaScriptJob() {
        this(0, null);
    }

    /**
     * Creates a new job instance.
     * @param initialDelay the initial amount of time to wait before executing this job
     * @param period the amount of time to wait between executions of this job (may be <tt>null</tt>)
     */
    public BasicJavaScriptJob(final int initialDelay, final Integer period) {
        initialDelay_ = initialDelay;
        period_ = period;
        setTargetExecutionTime(initialDelay + System.currentTimeMillis());
        executeAsap_ = (initialDelay == 0); // XHR are currently run as jobs and should be prioritary
    }

    /**
     * Sets the job ID.
     * @param id the job ID
     */
    public void setId(final Integer id) {
        id_ = id;
    }

    /**
     * Returns the job ID.
     * @return the job ID
     */
    public Integer getId() {
        return id_;
    }

    /**
     * Returns the initial amount of time to wait before executing this job.
     * @return the initial amount of time to wait before executing this job
     */
    public int getInitialDelay() {
        return initialDelay_;
    }

    /**
     * Returns the amount of time to wait between executions of this job (may be <tt>null</tt>).
     * @return the amount of time to wait between executions of this job (may be <tt>null</tt>)
     */
    public Integer getPeriod() {
        return period_;
    }

    /**
     * Returns <tt>true</tt> if this job executes periodically.
     * @return <tt>true</tt> if this job executes periodically
     */
    public boolean isPeriodic() {
        return period_  != null;
    }

    /**
     * Returns <tt>true</tt> if has to be executed asap.
     * @return <tt>true</tt> if has to be executed asap
     */
    public boolean isExecuteAsap() {
        return executeAsap_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "JavaScript Job " + id_;
    }

    /** {@inheritDoc} */
    public int compareTo(final JavaScriptJob other) {
        final boolean xhr1 = executeAsap_;
        final boolean xhr2 = other.isExecuteAsap();

        if (xhr1 && xhr2) {
            return getId().intValue() - other.getId().intValue();
        }

        if (xhr1) {
            return -1;
        }

        if (xhr2) {
            return 1;
        }

        return (int) (targetExecutionTime_ - other.getTargetExecutionTime());
    }

    /**
     * Returns the target execution time of the job.
     * @return the target execution time in ms
     */
    public long getTargetExecutionTime() {
        return targetExecutionTime_;
    }

    /**
     * Sets the target execution time of the job.
     * @param targetExecutionTime the new target execution time.
     */
    public void setTargetExecutionTime(final long targetExecutionTime) {
        targetExecutionTime_ = targetExecutionTime;
    }

}
