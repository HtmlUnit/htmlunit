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

 /**
  * @author Akif Esad
  */
package org.htmlunit.suite.recorder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.htmlunit.suite.record.IRecord;

public abstract class Recorder {
    protected String outputPath;
    protected boolean appendMode;

    public Recorder(String outputPath, boolean appendMode) {
        this.outputPath = outputPath;
        this.appendMode = appendMode;
    }

    /**
     * Records a single test result.
     * @param testResult the test result to record
     * @throws IOException if there's an error writing the record
     */
    public abstract void recordTestResult(IRecord testResult) throws IOException;

    /**
     * Records multiple test results.
     * @param testResults list of test results to record
     * @throws IOException if there's an error writing the records
     */
    public abstract void recordTestResults(List<IRecord> testResults) throws IOException;

    /**
     * Finalizes the recording process. Should be called after all records are written.
     * @throws IOException if there's an error during finalization
     */
    public abstract void close() throws IOException;

    protected File getOutputFile() {
        return new File(outputPath);
    }

    protected void ensureOutputDirectoryExists() {
        File outputFile = getOutputFile();
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }
}
