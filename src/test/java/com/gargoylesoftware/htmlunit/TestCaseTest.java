/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for various test cases.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public final class TestCaseTest {

    /**
     * Tests that all test cases with the pattern used by
     * {@link com.gargoylesoftware.htmlunit.source.TestSource#generateTestForHtmlElements(String, String, String)}
     * are up to date.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void generateTestForHtmlElements() throws Exception {
        generateTestForHtmlElements(new File("src/test/java"));
    }

    private void generateTestForHtmlElements(final File dir) throws Exception {
        for (final File file : dir.listFiles()) {
            if (file.isDirectory() && !".svn".equals(file.getName())) {
                generateTestForHtmlElements(file);
            }
            else if (file.getName().endsWith(".java")) {
                final List<String> lines = FileUtils.readLines(file);
                for (final String line : lines) {
                    if (line.contains("(\"xmp\")")) {
                        final String relativePath = file.getAbsolutePath().substring(
                                new File(".").getAbsolutePath().length() - 1);
                        generateTestForHtmlElements(relativePath, line, lines);
                    }
                }
            }
        }
    }

    private void generateTestForHtmlElements(final String relativePath, final String line, final List<String> lines) {
        final List<String> allExpectedLines = new ArrayList<String>();
        for (final String tag : HtmlPageTest.HTML_TAGS_) {
            allExpectedLines.add(line.replace("xmp", tag));
        }
        allExpectedLines.removeAll(lines);
        if (!allExpectedLines.isEmpty()) {
            Assert.fail("You must specify the following line in " + relativePath + ":\n" + allExpectedLines.get(0));
        }
    }
}
