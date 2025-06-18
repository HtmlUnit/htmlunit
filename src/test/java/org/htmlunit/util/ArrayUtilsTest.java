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
package org.htmlunit.util;

import static org.junit.Assert.assertThrows;

import org.htmlunit.SimpleWebTestCase;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayUtils}.
 *
 * @author Ronald Brill
 */
public class ArrayUtilsTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void containsIgnoreCase() throws Exception {
        assertTrue(ArrayUtils.containsIgnoreCase(new String[] {"ab"}, "ab"));
        assertTrue(ArrayUtils.containsIgnoreCase(new String[] {"o", "ab", "cd"}, "ab"));
        assertTrue(ArrayUtils.containsIgnoreCase(new String[] {"cd", "ab"}, "ab"));

        assertFalse(ArrayUtils.containsIgnoreCase(null, "ab"));
        assertFalse(ArrayUtils.containsIgnoreCase(new String[] {}, "ab"));
        assertFalse(ArrayUtils.containsIgnoreCase(new String[] {"cd", "ab"}, "x"));

        assertThrows(IllegalArgumentException.class, () -> StringUtils.startsWithIgnoreCase("AB", null));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.startsWithIgnoreCase(null, null));
    }
}
