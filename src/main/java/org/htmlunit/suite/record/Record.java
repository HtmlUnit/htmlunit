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

import com.google.gson.JsonObject;
import com.google.gson.Gson;

/**
 * Abstract base class for test records.
 *
 * @author Akif Esad
 */
public abstract class Record implements IRecord {
    private final String testName_;
    private final String className_;
    private final String testStatus_;
    private final JsonObject record_;

    /** Gson instance for JSON serialization. */
    protected static final Gson GSON = new Gson();

    /**
     * Creates a new Record.
     * @param testName the test name
     * @param className the class name
     * @param testStatus the test status
     */
    public Record(final String testName, final String className, final String testStatus) {
        this.testName_ = testName;
        this.className_ = className;
        this.testStatus_ = testStatus;
        this.record_ = new JsonObject();
    }

    @Override
    public String getTestName() {
        return testName_;
    }

    @Override
    public String getClassName() {
        return className_;
    }

    @Override
    public String getTestStatus() {
        return testStatus_;
    }

    @Override
    public String getRecord() {
        return GSON.toJson(record_);
    }

    /**
     * Adds a string property to the record.
     * @param key the property key
     * @param value the property value
     */
    protected void addToRecord(final String key, final String value) {
        record_.addProperty(key, value);
    }

    /**
     * Adds a numeric property to the record.
     * @param key the property key
     * @param value the property value
     */
    protected void addToRecord(final String key, final Number value) {
        record_.addProperty(key, value);
    }

    /**
     * Adds a boolean property to the record.
     * @param key the property key
     * @param value the property value
     */
    protected void addToRecord(final String key, final Boolean value) {
        record_.addProperty(key, value);
    }
}
