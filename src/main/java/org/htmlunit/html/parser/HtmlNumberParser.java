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
package org.htmlunit.html.parser;

import java.math.BigDecimal;

/**
 * Utility methods for parsing HTML floating-point numbers.
 * <p>
 * This parser implements the syntax defined by the HTML Standard for
 * <em>valid floating-point numbers</em>, which is used by elements such as
 * {@code <input type="number">}. The accepted syntax differs from both
 * Java number literals and JavaScript numeric literals.
 * </p>
 * <p>
 * In particular:
 * </p>
 * <ul>
 *   <li>only ASCII digits ({@code 0-9}) are accepted,</li>
 *   <li>hexadecimal, binary, and octal notation are not supported,</li>
 *   <li>{@code NaN} and {@code Infinity} are rejected, and</li>
 *   <li>leading or trailing whitespace is not permitted.</li>
 * </ul>
 *
 * @author Ronald Brill
 */
public final class HtmlNumberParser {

    /**
     * Utility class; no instances.
     */
    private HtmlNumberParser() {
    }

    /**
     * Returns whether the supplied string is a valid HTML floating-point number.
     * <p>
     * This method validates the complete input according to the HTML floating-point
     * number grammar. A value is considered valid only if it can be parsed
     * completely; partial matches are rejected.
     * </p>
     *
     * @param value the string to validate
     * @param acceptLeadingPlus set this to true to accept strings like "+7"
     * @param acceptDotAtEnd set this to true to accept strings like "1."
     * @return {@code true} if the supplied string is a valid HTML floating-point
     *         number; {@code false} otherwise
     * @see #parse(String, boolean, boolean)
     */
    public static boolean isValid(final String value, final boolean acceptLeadingPlus, final boolean acceptDotAtEnd) {
        return parse(value, acceptLeadingPlus, acceptDotAtEnd) != null;
    }

    /**
     * Parses the supplied string as an HTML floating-point number.
     * <p>
     * If the input is syntactically valid, its numeric value is returned as a
     * {@link BigDecimal}. Otherwise {@code null} is returned.
     * </p>
     * <p>
     * This method performs syntax validation only. It does not apply any
     * additional constraints that may be imposed by individual HTML algorithms
     * or form controls.
     * </p>
     *
     * @param value the string to parse
     * @param acceptLeadingPlus set this to true to accept strings like "+7"
     * @param acceptDotAtEnd set this to true to accept strings like "1."
     * @return the parsed value, or {@code null} if the supplied string is not a
     *         valid HTML floating-point number
     */
    public static BigDecimal parse(final String value, final boolean acceptLeadingPlus, final boolean acceptDotAtEnd) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        int pos = 0;
        final int len = value.length();

        final StringBuilder normalized = new StringBuilder(len + 5);

        // sign
        final char sign = value.charAt(pos);
        if (sign == '-') {
            normalized.append(sign);
            pos++;
            if (pos == len) {
                return null;
            }
        }
        else if (acceptLeadingPlus && sign == '+') {
            normalized.append(sign);
            pos++;
            if (pos == len) {
                return null;
            }
        }

        boolean digits = false;

        while (pos < len) {
            final char ch = value.charAt(pos);
            if (ch >= '0' && ch <= '9') {
                digits = true;
                normalized.append(ch);
                pos++;
            }
            else {
                break;
            }
        }

        if (pos < len && value.charAt(pos) == '.') {
            normalized.append('.');
            pos++;

            while (pos < len) {
                final char ch = value.charAt(pos);
                if (ch >= '0' && ch <= '9') {
                    digits = true;
                    normalized.append(ch);
                    pos++;
                }
                else {
                    break;
                }
            }
        }

        if (!digits) {
            return null;
        }

        if (pos < len && (value.charAt(pos) == 'e' || value.charAt(pos) == 'E')) {
            normalized.append('E');
            pos++;

            if (pos == len) {
                return null;
            }

            if (value.charAt(pos) == '+' || value.charAt(pos) == '-') {
                normalized.append(value.charAt(pos++));
                if (pos == len) {
                    return null;
                }
            }

            boolean exponentDigits = false;

            while (pos < len) {
                final char ch = value.charAt(pos);
                if (ch >= '0' && ch <= '9') {
                    exponentDigits = true;
                    normalized.append(ch);
                    pos++;
                }
                else {
                    break;
                }
            }

            if (!exponentDigits) {
                return null;
            }
        }

        if (pos != len) {
            return null;
        }

        if (!acceptDotAtEnd
                && pos > 0 && value.charAt(pos - 1) == '.') {
            return null;
        }

        try {
            return new BigDecimal(normalized.toString());
        }
        catch (final NumberFormatException e) {
            return null;
        }
    }
}
