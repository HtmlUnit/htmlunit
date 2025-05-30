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
import org.junit.Test;

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
        final List<String> defined = new ArrayList<>(JavaScriptConfiguration.CLASSES_.length);

        final HashMap<Integer, List<String>> levels = new HashMap<>();
        for (final Class<?> c : JavaScriptConfiguration.CLASSES_) {
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

        final List<String> all = new ArrayList<>(JavaScriptConfiguration.CLASSES_.length);
        for (int level = 1; level <= levels.size(); level++) {
            final List<String> clsAtLevel = levels.get(level);
            Collections.sort(clsAtLevel);
            all.addAll(clsAtLevel);

            // dump
            // System.out.println("// level " + level);
            // for (String cls : clsAtLevel) {
            //     System.out.print(cls + ".class, ");
            // }
            // System.out.println();
        }
        Assert.assertEquals(all, defined);
    }
}
