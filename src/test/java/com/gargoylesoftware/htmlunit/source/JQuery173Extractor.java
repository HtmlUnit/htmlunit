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
package com.gargoylesoftware.htmlunit.source;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Extracts the needed expectation from the real browsers output.
 *
 * In IE8 raw file, test outputs should be manually separated in a new line.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public final class JQuery173Extractor {

    private JQuery173Extractor() {
    }

    /**
     * Transforms the raw expectation, to the needed one by HtmlUnit.
     * This methods puts only the main line of the test output, without the details.
     *
     * @param input the raw file of real browser, with header and footer removed
     * @param output the expectation
     * @throws IOException if an error occurs
     */
    public static void run(final File input, final File output) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(input));
        final BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        int testNumber = 1;
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("" + testNumber + '.') && line.endsWith("Rerun")) {
                line = line.substring(0, line.length() - 5);
                writer.write(line + "\n");
                testNumber++;
            }
        }
        System.out.println("Last output #" + (testNumber - 1));
        reader.close();
        writer.close();
    }

}
