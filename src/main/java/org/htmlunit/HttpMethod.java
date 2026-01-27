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
package org.htmlunit;

/**
 * Represents the various ways a page can be submitted.
 *
 * @see <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>
 * @see <a href="http://tools.ietf.org/html/rfc5789">RFC5789</a>
 *
 * @author Mike Bowler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public enum HttpMethod {
    /** OPTIONS. */
    OPTIONS,
    /** GET. */
    GET,
    /** HEAD. */
    HEAD,
    /** POST. */
    POST,
    /** PUT. */
    PUT,
    /** DELETE. */
    DELETE,
    /** TRACE. */
    TRACE,
    /** PATCH. */
    PATCH;

    /**
     * Validates that an HTTP method string contains only ASCII characters
     * and matches the HTTP token specification (RFC 7230).
     *
     * @param methodName the HTTP method to validate
     * @throws IllegalArgumentException if method is null or empty
     */
    public static void validateHttpMethodName(final String methodName) {
        if (methodName == null || methodName.isEmpty()) {
            throw new IllegalArgumentException("HTTP method cannot be null or empty");
        }

        for (int i = 0; i < methodName.length(); i++) {
            final char c = methodName.charAt(i);

            // Check if non-ASCII
            if (c > 127) {
                throw new IllegalArgumentException(
                    "'" + methodName + "' is not a valid HTTP method (contains non-ASCII character)");
            }

            // Check if invalid token character
            if (!isHttpTokenChar(c)) {
                throw new IllegalArgumentException(
                    "'" + methodName + "' is not a valid HTTP method (invalid character: '" + c + "')");
            }
        }
    }

    private static boolean isHttpTokenChar(final char c) {
        return (c >= 'A' && c <= 'Z')
                || (c >= 'a' && c <= 'z')
                || (c >= '0' && c <= '9')
                || c == '!'
                || c == '#'
                || c == '$'
                || c == '%'
                || c == '&'
                || c == '\''
                || c == '*'
                || c == '+'
                || c == '-'
                || c == '.'
                || c == '^'
                || c == '_'
                || c == '`'
                || c == '|'
                || c == '~';
    }
}
