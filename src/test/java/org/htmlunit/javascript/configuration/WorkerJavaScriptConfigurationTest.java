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
package org.htmlunit.javascript.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WorkerJavaScriptConfiguration}.
 *
 * @author Ronald Brill
 */
public class WorkerJavaScriptConfigurationTest {


    /**
     * Test of order.
     */
    @Test
    public void treeOrder() {
        final List<String> defined = new ArrayList<>(WorkerJavaScriptConfiguration.CLASSES_.length);

        final HashMap<Integer, List<String>> levels = new HashMap<>();
        for (final Class<?> c : WorkerJavaScriptConfiguration.CLASSES_) {
            defined.add(c.getSimpleName());

            int level = 1;
            Class<?> parent = c.getSuperclass();
            while (parent != HtmlUnitScriptable.class) {
                level++;
                parent = parent.getSuperclass();
            }

            List<String> clsAtLevel = levels.get(level);
            if (clsAtLevel == null) {
                clsAtLevel = new ArrayList<>();
                levels.put(level, clsAtLevel);
            }
            clsAtLevel.add(c.getSimpleName());
        }

        final List<String> all = new ArrayList<>(WorkerJavaScriptConfiguration.CLASSES_.length);
        for (int level = 1; level <= levels.size(); level++) {
            final List<String> clsAtLevel = levels.get(level);
            Collections.sort(clsAtLevel);
            all.addAll(clsAtLevel);

            // dump
            /*
            final String indent = "       ";
            System.out.println(indent + " // level " + level);

            System.out.print(indent);
            int chars = indent.length();
            for (final String cls : clsAtLevel) {
                final String toPrint = " " + cls + ".class,";
                chars += toPrint.length();
                if (chars > 120) {
                    System.out.println();
                    System.out.print(indent);
                    chars = indent.length() + toPrint.length();
                }
                System.out.print(toPrint);
            }
            System.out.println();
            */
        }
        Assert.assertEquals(all, defined);
    }
}
