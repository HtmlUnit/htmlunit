/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.suite.record;

/**
 * Record for JUnit test results.
 *
 * @author Akif Esad
 */
public class JUnitRecord extends Record {
    /**
     * Creates a new JUnitRecord.
     * @param testName the test name
     * @param className the class name
     * @param testStatus the test status
     * @param stackTrace the stack trace if test failed
     * @param executionTime the test execution time
     */
    public JUnitRecord(final String testName, final String className, final String testStatus,
                      final String stackTrace, final long executionTime) {
        super(testName, className, testStatus);
        addToRecord("stackTrace", stackTrace);
        addToRecord("executionTime", executionTime);
    }
}
