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

/**
 * A JavaScript-triggered background job managed by a {@link JavaScriptJobManager}.
 *
 * @author Daniel Gredler
 * @author Amit Manjhi
 * @author Ronald Brill
 */
public interface JavaScriptJob extends Runnable, Comparable<JavaScriptJob> {

    /**
     * Returns the job ID.
     * @return the job ID
     */
    Integer getId();

    /**
     * Sets the job ID.
     * @param id the job ID
     */
    void setId(Integer id);

    /**
     * Returns the target execution time of the job.
     * @return the target execution time in ms
     */
    long getTargetExecutionTime();

    /**
     * Sets the target execution time of the job.
     * @param targetExecutionTime the new target execution time.
     */
    void setTargetExecutionTime(long targetExecutionTime);

    /**
     * Returns the amount of time to wait between executions of this job (may be {@code null}).
     * @return the amount of time to wait between executions of this job (may be {@code null})
     */
    Integer getPeriod();

    /**
     * Returns {@code true} if this job executes periodically.
     * @return {@code true} if this job executes periodically
     */
    boolean isPeriodic();

    /**
     * Returns {@code true} if has to be executed ASAP.
     * @return {@code true} if has to be executed ASAP
     */
    boolean isExecuteAsap();
}
