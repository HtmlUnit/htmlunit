/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;

/**
 * Tests that {@link HostClassNameStandardsTest}, {@link HostClassNameTest}, {@link HostTypeOfTest}
 * contain all host classes defined.
 *
 * @version $Revision: 9935 $
 * @author Ahmed Ashour
 */
public class HostCompleteTest {

    /**
     * @throws Exception if an error occurs.
     */
    @Test
    public void hostClassNameStandardsTest() throws Exception {
        hostTest(new File("./src/test/java/com/gargoylesoftware/htmlunit/javascript/HostClassNameStandardsTest.java"));
    }

    private void hostTest(final File file) throws Exception {
        assertTrue(file.exists());
        try (final Reader reader = new FileReader(file)) {
            final List<String> lines = IOUtils.readLines(reader);

            final Field field = JavaScriptConfiguration.class.getDeclaredField("CLASSES_");
            field.setAccessible(true);

            final StringBuilder notFoundList = new StringBuilder();
            for (final Class<?> c : (Class<?>[]) field.get(null)) {
                final String name = c.getSimpleName();
                boolean found = false;
                for (final String line : lines) {
                    if (line.contains("\"" + name + "\"")) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    if (notFoundList.length() != 0) {
                        notFoundList.append(", ");
                    }
                    notFoundList.append(name);
                }
            }
            if (notFoundList.length() != 0) {
                fail(file.getName() + ": Could not find test case for " + notFoundList);
            }
        }
    }

    /**
     * @throws Exception if an error occurs.
     */
    @Test
    public void hostClassNameTest() throws Exception {
        hostTest(new File("./src/test/java/com/gargoylesoftware/htmlunit/javascript/HostClassNameTest.java"));
    }

    /**
     * @throws Exception if an error occurs.
     */
    @Test
    public void hostTypeOfTest() throws Exception {
        hostTest(new File("./src/test/java/com/gargoylesoftware/htmlunit/javascript/HostTypeOfTest.java"));
    }

}
