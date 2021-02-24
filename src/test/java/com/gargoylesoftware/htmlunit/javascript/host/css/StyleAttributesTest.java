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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;

/**
 * Tests for {@link StyleAttributes}.
 *
 * @author Ahmed Ashour
 */
public class StyleAttributesTest {

    /**
     * Test of alphabetical order.
     */
    @Test
    public void lexicographicOrder() {
        String lastName = null;
        for (final Definition definition : StyleAttributes.Definition.values()) {
            final String name = definition.name();
            if (lastName != null && name.compareToIgnoreCase(lastName) < 1) {
                fail("StyleAttributes.Definition: '"
                    + name + "' should be before '" + lastName + "'");
            }
            lastName = name;
        }
    }

    /**
     * Test the uniqueness of the property names.
     */
    @Test
    public void unique() {
        final List<String> nameList = new ArrayList<>();
        for (final Definition definition : StyleAttributes.Definition.values()) {
            final String propertyName = definition.getPropertyName();
            if (nameList.contains(propertyName)) {
                fail("StyleAttributes.Definition: the property name '"
                    + propertyName + "' is defined more than once");
            }
            nameList.add(propertyName);
        }
    }

    /**
     * Test the naming convention.
     */
    @Test
    public void name() {
        for (final Definition definition : StyleAttributes.Definition.values()) {
            final String propertyName = definition.getPropertyName();
            if (propertyName.indexOf('-') == -1) {
                if (definition.name().endsWith("_") && Character.isLowerCase(definition.name().charAt(0))) {
                    fail("StyleAttributes.Definition: '" + definition.name() + "' must not end with underscore");
                }
            }
            else {
                if (!definition.name().endsWith("_")) {
                    fail("StyleAttributes.Definition: '" + definition.name() + "' must end with underscore");
                }
            }
        }
    }

    /**
     * Test the name in the JavaDoc matches the definition name.
     * @throws IOException if an error occurs
     */
    @Test
    public void javaDoc() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get("src/main/java/"
                + getClass().getPackage().getName().replace('.', '/') + "/StyleAttributes.java"))) {
            final List<String> lines = stream.collect(Collectors.toList());
            final Pattern pattern = Pattern.compile("\\s+[A-Z_]+\\(\"(.*?)\",");
            for (int i = 1; i < lines.size(); i++) {
                final Matcher matcher = pattern.matcher(lines.get(i));
                if (matcher.find() && !lines.get(i - 1).contains("{@code " + matcher.group(1) + "}")) {
                    fail("StyleAttributes: not matching JavaDoc in line " + i);
                }
            }
        }
    }

}
