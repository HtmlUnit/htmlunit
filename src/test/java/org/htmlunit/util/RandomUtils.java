/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import java.util.Random;

public final class RandomUtils {
    private static final String LOWERCHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCHARS = LOWERCHARS.toUpperCase();
    private static final String CHARS = LOWERCHARS + UPPERCHARS;

    /**
     * Private ctor to keep Checkstyle happy
     */
    private RandomUtils() {

    }

    /**
     * Fixed length random string of lower case and uppercase chars.
     *
     * @param random the random number generator
     * @param length the length of the resulting string
     * @return a random string of the desired length
     */
    public static String randomString(final Random random, final int length) {
        return randomString(random, length, length);
    }

    /**
     * Variable length random string of lower case and upper case chars.
     *
     * @param random the random generator
     * @param from min length
     * @param to max length (inclusive)
     * @return a random string
     */
    public static String randomString(final Random random, final int from, final int to) {
        final int length = random.nextInt(to - from + 1) + from;

        final StringBuilder sb = new StringBuilder(to);

        for (int i = 0; i < length; i++) {
            final int pos = random.nextInt(CHARS.length());
            sb.append(CHARS.charAt(pos));
        }

        return sb.toString();
    }
}
