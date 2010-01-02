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

/**
 * A JavaScript-triggered background job managed by a {@link JavaScriptJobManager}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public abstract class JavaScriptJob implements Runnable {

    /** The job ID. */
    private Integer id_;

    /** The initial amount of time to wait before executing this job. */
    private final int initialDelay_;

    /** The amount of time to wait between executions of this job (may be <tt>null</tt>). */
    private final Integer period_;

    /** Creates a new job instance that executes once, immediately. */
    public JavaScriptJob() {
        this(0, null);
    }

    /**
     * Creates a new job instance.
     * @param initialDelay the initial amount of time to wait before executing this job
     * @param period the amount of time to wait between executions of this job (may be <tt>null</tt>)
     */
    public JavaScriptJob(final int initialDelay, final Integer period) {
        initialDelay_ = initialDelay;
        period_ = period;
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

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "JavaScript Job " + id_;
    }

}
