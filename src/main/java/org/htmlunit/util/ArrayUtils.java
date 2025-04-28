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

/**
 * Utility functions not covered by third party libraries.
 *
 * @author Ronald Brill
 */
public final class ArrayUtils {

    /**
     * Disallow instantiation of this class.
     */
    private ArrayUtils() {
        // Empty.
    }

    /**
     * @param strings the string[] to check
     * @param expected the string that we expect
     * @return true if at least one element of the array equalsIgnoreCase to the expected string
     */
    public static boolean containsIgnoreCase(final String[] strings, final String expected) {
        if (expected == null) {
            throw new IllegalArgumentException("Expected string can't be null");
        }

        if (strings == null) {
            return false;
        }

        for (final String s : strings) {
            if (expected.equalsIgnoreCase(s)) {
                return true;
            }
        }

        return false;
    }
}
