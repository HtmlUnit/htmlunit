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
package com.gargoylesoftware.htmlunit;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;

/**
 * Tests for various test cases.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class TestCaseTest {

    private Set<String> allClassNames_;

    /**
     * Tests that all test cases with the pattern used by
     * {@link com.gargoylesoftware.htmlunit.source.ElementTestSource#generateTestForHtmlElements}
     * are up to date.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void generateTestForHtmlElements() throws Exception {
        allClassNames_ = getAllClassNames();
        generateTestForHtmlElements(new File("src/test/java"));
    }

    private void generateTestForHtmlElements(final File dir) throws Exception {
        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory() && !".git".equals(file.getName())) {
                    generateTestForHtmlElements(file);
                }
                else if (file.getName().endsWith(".java")) {
                    final List<String> lines = FileUtils.readLines(file, ISO_8859_1);
                    for (final String line : lines) {
                        if (line.contains("(\"xmp\")")) {
                            final String relativePath = file.getAbsolutePath().substring(
                                    new File(".").getAbsolutePath().length() - 1);
                            checkLines(relativePath, line, lines, "xmp", new HashSet<String>(HtmlPageTest.HTML_TAGS_));
                        }
                        else if (line.contains("(\"ClientRect\")")) {
                            final String relativePath = file.getAbsolutePath().substring(
                                    new File(".").getAbsolutePath().length() - 1);
                            checkLines(relativePath, line, lines, "ClientRect", allClassNames_);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns list of all host classes defined.
     * @return the list
     * @throws Exception if an error occurs.
     */
    public static Set<String> getAllClassNames() throws Exception {
        final Set<String> names = new HashSet<>();

        for (final BrowserVersion browser : BrowserVersion.ALL_SUPPORTED_BROWSERS) {
            final JavaScriptConfiguration jsConfig = JavaScriptConfiguration.getInstance(browser);
            for (final ClassConfiguration config : jsConfig.getAll()) {
                names.add(config.getClassName());
            }
        }

        return names;
    }

    private static void checkLines(final String relativePath, final String line, final List<String> lines,
            final String elementName, final Set<String> allElements) {
        final List<String> allExpectedLines = new ArrayList<>();
        for (final String element : allElements) {
            allExpectedLines.add(line.replace(elementName, element));
        }
        allExpectedLines.removeAll(lines);
        if (!allExpectedLines.isEmpty()) {
            fail("You must specify the following line in " + relativePath + ":\n"
                    + String.join(System.lineSeparator(), allExpectedLines));
        }
    }
}
