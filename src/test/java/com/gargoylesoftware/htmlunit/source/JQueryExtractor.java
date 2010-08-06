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
package com.gargoylesoftware.htmlunit.source;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Generates java test code to be included in JQueryTest.
 *
 * Steps to use it:
 *   1- Generate "Expected" files by 'copying all' the text of the corresponding browser
 *      and remove the header and footer.
 *   2- Firefox automatically adds "1. " in front of the results, no issue, this class will trim that.
 *   3- E.g. in JQuery 1.2.6, the first line with IE is "core module: Basic requirements (0, 7, 7)"
 *      and last line is "Make sure that overflow is reset (Old: visible Cur: visible): visible"
 *   4- Save all possibilities in a single folder, namely: "expected.FF2.txt", "expected.FF3.txt",
 *      "expected.IE6.txt", and "expected.IE7.txt"
 *   5- Call {@link JQueryExtractor#run(File, File)} with appropriate parameters
 *   6- Add the generated code to {@link JQueryExtractorSample}
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public final class JQueryExtractor {

    private final Writer writer_;
    private final List<String[]> ie6_;
    private final List<String[]> ie7_;
    private final List<String[]> ff3_;

    /**
     * NOT FULLY OPERATIONAL, IT HAS TWO ISSUES:
     *  1. It needs to handle differences between browsers expected values.
     *  2. It currently throws OutOfMemoryError.
     *
     * Prints JQuery java test code from the "expected" files in the specified directory.
     * @param dir the folder that has the expected files.
     * @param output the output file to save the code into
     * @throws IOException If an error occurs
     */
    public static void run(final File dir, final File output) throws IOException {
        new JQueryExtractor(dir, output);
    }

    private JQueryExtractor(final File dir, final File output) throws IOException {
        writer_ = new BufferedWriter(new FileWriter(output));
        ie6_ = getExpected(dir, BrowserVersion.INTERNET_EXPLORER_6);
        ie7_ = getExpected(dir, BrowserVersion.INTERNET_EXPLORER_7);
        ff3_ = getExpected(dir, BrowserVersion.FIREFOX_3);
        for (int i = 0; i < ie6_.size(); i++) {
            process(i);
        }
        writer_.close();
    }

    private void process(final int index) throws IOException {
        final String[] lines = ie7_.get(index);
        String methodName = lines[0];
        final String replacement = ": ()[]<>{}.',|#-=";
        final int p0 = methodName.lastIndexOf('(');
        methodName = methodName.substring(0, p0).trim();
        for (int i = 0; i < replacement.length(); i++) {
            methodName = methodName.replace(replacement.charAt(i), '_');
        }
        writer_.write("\n");
        writer_.write("    /**\n");
        writer_.write("     * Test.\n");
        writer_.write("     */\n");
        writer_.write("    @Test\n");
        writer_.write("    public void " + methodName + "() {\n");
        writer_.write("        assertResult(\"" + lines[0].replaceAll("\"", "\\\"") + "\");\n");
        for (int i = 1; i < lines.length; i++) {
            final String assertion = lines[i].replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\"");
            writer_.write("        assertAssertion(\"" + assertion + "\");\n");
        }
        writer_.write("    }\n");
    }

    @SuppressWarnings("unchecked")
    private static List<String[]> getExpected(final File dir, final BrowserVersion browserVersion) throws IOException {
        final List<String[]> list = new ArrayList<String[]>();
        final List<String> lines = FileUtils.readLines(
                new File(dir, "expected." + browserVersion.getNickname() + ".txt"), "UTF-8");
        final List<String> testList = new ArrayList<String>();
        final boolean ie = browserVersion.isIE();
        for (String line : lines) {
            if (!ie) {
                line = line.substring(line.indexOf('.') + 2);
            }
            if (line.contains(" module: ") && !testList.isEmpty()) {
                list.add(testList.toArray(new String[testList.size()]));
                testList.clear();
            }
            testList.add(line);
        }
        list.add(testList.toArray(new String[testList.size()]));
        return list;
    }
}
