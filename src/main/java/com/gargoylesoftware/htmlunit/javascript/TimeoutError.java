/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

/**
 * An error that is thrown when a script has been running too long.
 *
 * @version $Revision$
 * @author Andre Soereng
 * @author Ronald Brill
 */
public class TimeoutError extends Error {
    private final long allowedTime_;
    private final long executionTime_;

    TimeoutError(final long allowedTime, final long executionTime) {
        super("Javascript execution takes too long (allowed: " + allowedTime
                + ", already elapsed: " + executionTime + ")");
        allowedTime_ = allowedTime;
        executionTime_ = executionTime;
    }

    /**
     * Returns the allowed time.
     * @return the allowed time
     */
    long getAllowedTime() {
        return allowedTime_;
    }

    /**
     * Returns the execution time.
     * @return the execution time
     */
    long getExecutionTime() {
        return executionTime_;
    }
}
