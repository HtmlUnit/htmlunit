/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayUtils}.
 *
 * @author Ronald Brill
 */
public class ArrayUtilsTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void contains() throws Exception {
        assertTrue(ArrayUtils.contains(new String[] {"ab"}, "ab"));
        assertTrue(ArrayUtils.contains(new String[] {"o", "ab", "cd"}, "ab"));
        assertTrue(ArrayUtils.contains(new String[] {"cd", "ab"}, "ab"));

        assertFalse(ArrayUtils.contains(null, "ab"));
        assertFalse(ArrayUtils.contains(new String[] {}, "ab"));
        assertFalse(ArrayUtils.contains(ArrayUtils.EMPTY_STRING_ARRAY, "ab"));
        assertFalse(ArrayUtils.contains(new String[] {"cd", "ab"}, "x"));

        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.contains(new String[] {}, null));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.contains(ArrayUtils.EMPTY_STRING_ARRAY, null));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void containsIgnoreCase() throws Exception {
        assertTrue(ArrayUtils.containsIgnoreCase(new String[] {"ab"}, "ab"));
        assertTrue(ArrayUtils.containsIgnoreCase(new String[] {"o", "ab", "cd"}, "ab"));
        assertTrue(ArrayUtils.containsIgnoreCase(new String[] {"cd", "ab"}, "ab"));

        assertTrue(ArrayUtils.containsIgnoreCase(new String[] {"ab"}, "aB"));
        assertTrue(ArrayUtils.containsIgnoreCase(new String[] {"o", "ab", "cd"}, "Ab"));
        assertTrue(ArrayUtils.containsIgnoreCase(new String[] {"cd", "ab"}, "AB"));

        assertFalse(ArrayUtils.containsIgnoreCase(null, "ab"));
        assertFalse(ArrayUtils.containsIgnoreCase(new String[] {}, "ab"));
        assertFalse(ArrayUtils.containsIgnoreCase(ArrayUtils.EMPTY_STRING_ARRAY, "ab"));
        assertFalse(ArrayUtils.containsIgnoreCase(new String[] {"cd", "ab"}, "x"));

        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.containsIgnoreCase(new String[] {}, null));
        assertThrows(IllegalArgumentException.class,
                () -> ArrayUtils.containsIgnoreCase(ArrayUtils.EMPTY_STRING_ARRAY, null));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void containsChar() throws Exception {
        assertTrue(ArrayUtils.contains(new char[] {1}, (char) 1));
        assertTrue(ArrayUtils.contains(new char[] {7, 1, 9}, (char) 1));
        assertTrue(ArrayUtils.contains(new char[] {5, 2}, (char) 2));

        assertFalse(ArrayUtils.contains(null, (char) 7));
        assertFalse(ArrayUtils.contains(new char[] {}, (char) 1));
        assertFalse(ArrayUtils.contains(ArrayUtils.EMPTY_CHAR_ARRAY, (char) 1));
        assertFalse(ArrayUtils.contains(new char[] {7, 9}, (char) 4));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void containsByte() throws Exception {
        assertTrue(ArrayUtils.contains(new byte[] {1}, (byte) 1));
        assertTrue(ArrayUtils.contains(new byte[] {7, 1, 9}, (byte) 1));
        assertTrue(ArrayUtils.contains(new byte[] {5, 2}, (byte) 2));

        assertFalse(ArrayUtils.contains(null, (byte) 7));
        assertFalse(ArrayUtils.contains(new byte[] {}, (byte) 1));
        assertFalse(ArrayUtils.contains(ArrayUtils.EMPTY_BYTE_ARRAY, (byte) 1));
        assertFalse(ArrayUtils.contains(new byte[] {7, 9}, (byte) 4));
    }
}
