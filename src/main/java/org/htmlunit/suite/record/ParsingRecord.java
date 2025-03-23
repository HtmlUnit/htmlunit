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
 * Record for HTML parsing results.
 *
 * @author Akif Esad
 */
public class ParsingRecord extends Record {

    /**
     * Creates a new ParsingRecord.
     * @param testName the test name
     * @param className the class name
     * @param testStatus the test status
     * @param parsedContent the parsed HTML content
     * @param parsingErrors any parsing errors encountered
     * @param validParse whether the parse was valid
     */
    public ParsingRecord(final String testName, final String className, final String testStatus,
            final String parsedContent, final String parsingErrors, final boolean validParse) {
        super(testName, className, testStatus);
        addToRecord("parsedContent", parsedContent);
        addToRecord("parsingErrors", parsingErrors);
        addToRecord("validParse", validParse);
    }
}
