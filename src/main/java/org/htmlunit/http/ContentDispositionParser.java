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
package org.htmlunit.http;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Parser for the {@code Content-Disposition} header.
 * - RFC 6266 (HTTP) and RFC 2183 (disposition type and parameters)
 * - RFC 8187 extended parameters ({@code filename*=UTF-8''...}); supported charsets are
 *   UTF-8 and ISO-8859-1
 * <p>
 * The parser is lenient like the browsers are: parameter names are case-insensitive,
 * white space around names and values is ignored, an unterminated quoted string
 * ends at the end of the header, a broken percent encoding in an extended value is kept as is.
 * </p>
 *
 * @author Ronald Brill
 */
public final class ContentDispositionParser {

    private ContentDispositionParser() {
        // Utility class
    }

    /**
     * Parses the value of a {@code Content-Disposition} header.
     *
     * @param header the header value
     * @return the parsed header or {@code null} if the given value is {@code null}
     */
    public static ContentDisposition parse(final String header) {
        if (header == null) {
            return null;
        }

        final Map<String, String> parameters = new LinkedHashMap<>();
        final Map<String, String> extendedParameters = new LinkedHashMap<>();

        final int length = header.length();
        int pos = header.indexOf(';');
        final String type = (pos < 0 ? header : header.substring(0, pos)).trim().toLowerCase(Locale.ROOT);

        while (pos >= 0 && pos < length) {
            pos++; // skip ';'

            // name
            while (pos < length && Character.isWhitespace(header.charAt(pos))) {
                pos++;
            }
            final int nameStart = pos;
            while (pos < length && header.charAt(pos) != '=' && header.charAt(pos) != ';') {
                pos++;
            }
            String name = header.substring(nameStart, pos).trim().toLowerCase(Locale.ROOT);

            // value
            String value = "";
            if (pos < length && header.charAt(pos) == '=') {
                pos++;
                while (pos < length && Character.isWhitespace(header.charAt(pos))) {
                    pos++;
                }
                if (pos < length && header.charAt(pos) == '"') {
                    // quoted string; a backslash escapes the next character
                    final StringBuilder sb = new StringBuilder();
                    pos++;
                    while (pos < length && header.charAt(pos) != '"') {
                        if (header.charAt(pos) == '\\' && pos + 1 < length) {
                            pos++;
                        }
                        sb.append(header.charAt(pos));
                        pos++;
                    }
                    if (pos < length) {
                        pos++; // closing quote
                    }
                    // ignore everything up to the next ';'
                    while (pos < length && header.charAt(pos) != ';') {
                        pos++;
                    }
                    value = sb.toString();
                }
                else {
                    final int valueStart = pos;
                    while (pos < length && header.charAt(pos) != ';') {
                        pos++;
                    }
                    value = header.substring(valueStart, pos).trim();
                }
            }

            if (!name.isEmpty()) {
                if (name.endsWith("*")) {
                    name = name.substring(0, name.length() - 1);
                    if (!name.isEmpty() && !extendedParameters.containsKey(name)) {
                        final String decoded = decodeExtendedValue(value);
                        if (decoded != null) {
                            extendedParameters.put(name, decoded);
                        }
                    }
                }
                else if (!parameters.containsKey(name)) {
                    parameters.put(name, value);
                }
            }

            pos = header.indexOf(';', pos);
        }

        return new ContentDisposition(type, parameters, extendedParameters);
    }

    /**
     * Decodes a RFC 8187 value (charset'language'percent-encoded).
     *
     * @param value the raw value of the extended parameter
     * @return the decoded value or {@code null} if the format is wrong or the charset is not supported
     */
    private static String decodeExtendedValue(final String value) {
        final int first = value.indexOf('\'');
        final int second = first < 0 ? -1 : value.indexOf('\'', first + 1);
        if (second < 0) {
            return null;
        }

        final String charsetName = value.substring(0, first).trim();
        final Charset charset;
        if ("UTF-8".equalsIgnoreCase(charsetName)) {
            charset = StandardCharsets.UTF_8;
        }
        else if ("ISO-8859-1".equalsIgnoreCase(charsetName)) {
            charset = StandardCharsets.ISO_8859_1;
        }
        else {
            return null;
        }

        final String encoded = value.substring(second + 1);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < encoded.length(); i++) {
            final char c = encoded.charAt(i);
            if (c == '%' && i + 2 < encoded.length() && isHex(encoded.charAt(i + 1))
                    && isHex(encoded.charAt(i + 2))) {
                out.write((Character.digit(encoded.charAt(i + 1), 16) << 4)
                        | Character.digit(encoded.charAt(i + 2), 16));
                i += 2;
            }
            else {
                // invalid or no percent encoding: keep it
                final byte[] bytes = String.valueOf(c).getBytes(charset);
                out.write(bytes, 0, bytes.length);
            }
        }
        return new String(out.toByteArray(), charset);
    }

    /**
     * Checks for a hexadecimal digit (ASCII only).
     *
     * @param c the character to check
     * @return {@code true} if the character is a hexadecimal digit
     */
    private static boolean isHex(final char c) {
        return c < 128 && Character.digit(c, 16) >= 0;
    }
}
