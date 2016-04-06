/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.intl.Intl;

/**
 * Tests for various test cases.
 *
 * @author Ahmed Ashour
 */
public final class TestCaseTest {

    private List<String> allClassNames_;

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
                if (file.isDirectory() && !".svn".equals(file.getName())) {
                    generateTestForHtmlElements(file);
                }
                else if (file.getName().endsWith(".java")) {
                    final List<String> lines = FileUtils.readLines(file);
                    for (final String line : lines) {
                        if (line.contains("(\"xmp\")")) {
                            final String relativePath = file.getAbsolutePath().substring(
                                    new File(".").getAbsolutePath().length() - 1);
                            checkLines(relativePath, line, lines, "xmp", HtmlPageTest.HTML_TAGS_);
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
    public static List<String> getAllClassNames() throws Exception {
        final Field field = JavaScriptConfiguration.class.getDeclaredField("CLASSES_");
        field.setAccessible(true);

        final List<String> list = new ArrayList<>();
        for (final Class<?> c : (Class<?>[]) field.get(null)) {
            final String name = c.getSimpleName();
            list.add(name);
        }
        list.add(Intl.class.getSimpleName());
        list.add("Error");
        return list;
    }

    private static void checkLines(final String relativePath, final String line, final List<String> lines,
            final String elementName, final List<String> allElements) {
        final List<String> allExpectedLines = new ArrayList<>();
        for (final String element : allElements) {
            allExpectedLines.add(line.replace(elementName, element));
        }
        allExpectedLines.removeAll(lines);
        if (!allExpectedLines.isEmpty()) {
            fail("You must specify the following line in " + relativePath + ":\n"
                    + StringUtils.join(allExpectedLines, System.lineSeparator()));
        }
    }
}
