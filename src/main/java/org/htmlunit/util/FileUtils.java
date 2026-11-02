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

/**
 * File utility class for functions not covered by third-party libraries.
 *
 * @author Ronald Brill
 */
public final class FileUtils {

    /** Characters not allowed in file names on at least one platform (control characters aside). */
    private static final String INVALID_FILENAME_CHARS = "\\/:*?\"<>|";

    /**
     * Disallow instantiation of this class.
     */
    private FileUtils() {
        // Empty.
    }

    /**
     * Makes the given name usable as the name of a file, the way the browsers do it
     * for downloaded files.
     * <ul>
     * <li>Characters that are not allowed in file names on at least one platform
     * ({@code \ / : * ? " < > |}), the control characters (U+0000 - U+001F) and DEL (U+007F)
     * are replaced by an underscore. Because the path separators are replaced, the result is always
     * a plain file name; e.g. {@code ../../evil.txt} becomes {@code _.._evil.txt}, so the name
     * can be resolved against a folder without leaving it.</li>
     * <li>Leading dots and white space (no hidden files) and trailing dots and white space
     * are removed.</li>
     * </ul>
     * <p>The name is not checked any further; e.g. names reserved by Windows (like {@code CON} or
     * {@code NUL}) and the maximum length of a file name are not taken into account.</p>
     *
     * @param name the name to sanitize, can be {@code null}
     * @return the sanitized name or {@code null} if the name is {@code null} or nothing is left
     */
    public static String sanitizeFilename(final String name) {
        if (name == null) {
            return null;
        }

        final StringBuilder sb = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            if (c < ' ' || c == 127 || INVALID_FILENAME_CHARS.indexOf(c) >= 0) {
                sb.append('_');
            }
            else {
                sb.append(c);
            }
        }

        int start = 0;
        int end = sb.length();
        while (start < end && isTrimmed(sb.charAt(start))) {
            start++;
        }
        while (end > start && isTrimmed(sb.charAt(end - 1))) {
            end--;
        }
        return start == end ? null : sb.substring(start, end);
    }

    private static boolean isTrimmed(final char c) {
        return c == '.' || Character.isWhitespace(c);
    }
}
