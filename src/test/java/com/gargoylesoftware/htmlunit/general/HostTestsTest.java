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
package com.gargoylesoftware.htmlunit.general;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * Tests the general host tests have all JavaScript objects names defined in all the other tests.
 *
 * This classes parses all the test code, searching for [object some_name], and ensures that
 * {@link HostClassNameTest} and {@link HostTypeOfTest} include those objects.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostTestsTest {

    private Pattern pattern_ = Pattern.compile("\"\\[object (\\w+)\\]\"");

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void test() throws Exception {
        final Set<String> set = new HashSet<>();
        final File testRoot = new File("src/test/java");
        collectionObjectNames(testRoot, set);

        // Remove all Prototypes, as we plan to have test cases separate for them soon
        // TODO: add Prototype tests (e.g. alert(Element.prototype)
        for (final Iterator<String> it = set.iterator(); it.hasNext();) {
            if (it.next().endsWith("Prototype")) {
                it.remove();
            }
        }
        if (set.contains("Arguments")) {
            set.remove("Arguments");
            set.add("arguments");
        }

        set.remove("Iterator");
        set.remove("URLSearchParamsIterator");

        // Worker
        set.remove("DedicatedWorkerGlobalScope");
        set.remove("WorkerGlobalScope");
        set.remove("global");

        ensure(new File(testRoot, HostClassNameTest.class.getName().replace('.', '/') + ".java"), set);
        ensure(new File(testRoot, HostTypeOfTest.class.getName().replace('.', '/') + ".java"), set);
    }

    private void collectionObjectNames(final File dir, final Set<String> set) throws IOException {
        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory() && !".git".equals(file.getName())) {
                    collectionObjectNames(file, set);
                }
                else if (file.getName().endsWith(".java")) {
                    final List<String> lines = FileUtils.readLines(file, ISO_8859_1);
                    for (final String line : lines) {
                        final Matcher matcher = pattern_.matcher(line);
                        while (matcher.find()) {
                            set.add(matcher.group(1));
                        }
                    }
                }
            }
        }
    }

    private static void ensure(final File file, final Set<String> set) throws IOException {
        final Set<String> unusedNames = new HashSet<>(set);
        final List<String> lines = FileUtils.readLines(file, ISO_8859_1);
        for (final String line : lines) {
            for (final Iterator<String> it = unusedNames.iterator(); it.hasNext();) {
                if (line.contains("(\"" + it.next() + "\")")) {
                    it.remove();
                }
            }
        }
        if (!unusedNames.isEmpty()) {
            fail("You must specify the following line"
                    + (unusedNames.size() == 1 ? "" : "s") + " in " + file.getName() + ":\n"
                    + String.join(", ", unusedNames));
        }
    }
}
