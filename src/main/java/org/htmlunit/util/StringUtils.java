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

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlunit.html.impl.Color;

/**
 * String utility class for functions not covered by third-party libraries.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Martin Tamme
 * @author Ronald Brill
 */
public final class StringUtils {

    /**
     * The empty String {@code ""}.
     */
    public static final String EMPTY_STRING = "";

    private static final Pattern HEX_COLOR = Pattern.compile("#([\\da-fA-F]{3}|[\\da-fA-F]{6})");
    private static final Pattern RGB_COLOR =
        Pattern.compile("rgb\\(\\s*(0|[1-9]\\d?|1\\d\\d?|2[0-4]\\d|25[0-5])%?\\s*,"
                            + "\\s*(0|[1-9]\\d?|1\\d\\d?|2[0-4]\\d|25[0-5])%?\\s*,"
                            + "\\s*(0|[1-9]\\d?|1\\d\\d?|2[0-4]\\d|25[0-5])%?\\s*\\)");
    private static final Pattern RGBA_COLOR =
            Pattern.compile("rgba\\(\\s*(0|[1-9]\\d?|1\\d\\d?|2[0-4]\\d|25[0-5])%?\\s*,"
                                 + "\\s*(0|[1-9]\\d?|1\\d\\d?|2[0-4]\\d|25[0-5])%?\\s*,"
                                 + "\\s*(0|[1-9]\\d?|1\\d\\d?|2[0-4]\\d|25[0-5])%?\\s*,"
                                 + "\\s*((0?.[1-9])|[01])\\s*\\)");
    private static final Pattern HSL_COLOR =
            Pattern.compile("hsl\\(\\s*((0|[1-9]\\d?|[12]\\d\\d?|3[0-5]\\d)(.\\d*)?)\\s*,"
                                + "\\s*((0|[1-9]\\d?|100)(.\\d*)?)%\\s*,"
                                + "\\s*((0|[1-9]\\d?|100)(.\\d*)?)%\\s*\\)");
    private static final Pattern ILLEGAL_FILE_NAME_CHARS = Pattern.compile("\\\\|/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}");

    private static final Map<String, String> CAMELIZE_CACHE = new ConcurrentHashMap<>();

    /**
     * Disallow instantiation of this class.
     */
    private StringUtils() {
        // Empty.
    }

    /**
     * Returns {@code true} if the given sequence is not {@code null} and is empty.
     * Unlike {@link org.apache.commons.lang3.StringUtils#isEmpty(CharSequence)}, this returns
     * {@code false} if the sequence is {@code null}.
     *
     * @param s the string to check
     * @return {@code true} if the string is not {@code null} and has length 0
     */
    public static boolean isEmptyString(final CharSequence s) {
        return s != null && s.length() == 0;
    }

    /**
     * Returns {@code true} if the given sequence is {@code null} or empty.
     *
     * @param s the string to check
     * @return {@code true} if the string is {@code null} or has length 0
     */
    public static boolean isEmptyOrNull(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * Returns the given sequence, or the default value if it is empty or {@code null}.
     *
     * @param <T> the kind of {@link CharSequence}
     * @param s the sequence to check
     * @param defaultString the default value to return if the input is empty or {@code null}
     * @return the given sequence, or {@code defaultString}
     */
    public static <T extends CharSequence> T defaultIfEmptyOrNull(final T s, final T defaultString) {
        return isEmptyOrNull(s) ? defaultString : s;
    }

    /**
     * Returns {@code true} if the given sequence is {@code null}, empty, or contains only whitespace.
     *
     * @param s the sequence to check
     * @return {@code true} if the sequence is blank
     */
    public static boolean isBlank(final CharSequence s) {
        if (s == null) {
            return true;
        }

        final int length = s.length();
        if (length == 0) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns {@code true} if the given sequence is not {@code null}, not empty, and contains
     * at least one non-whitespace character.
     *
     * @param s the sequence to check
     * @return {@code true} if the sequence is not blank
     */
    public static boolean isNotBlank(final CharSequence s) {
        if (s == null) {
            return false;
        }

        final int length = s.length();
        if (length == 0) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns {@code true} if the given sequence has exactly one character and it equals the expected char.
     *
     * @param expected the char to compare
     * @param s the string to check
     * @return {@code true} if the string has exactly one character matching {@code expected}
     */
    public static boolean equalsChar(final char expected, final CharSequence s) {
        return s != null && s.length() == 1 && expected == s.charAt(0);
    }

    /**
     * Returns {@code true} if the given string starts with the specified prefix, ignoring case.
     *
     * @param s the string to check
     * @param expectedStart the expected prefix (must not be {@code null} or empty)
     * @return {@code true} if the string starts with the given prefix, ignoring case
     */
    public static boolean startsWithIgnoreCase(final String s, final String expectedStart) {
        if (expectedStart == null || expectedStart.length() == 0) {
            throw new IllegalArgumentException("Expected start string can't be null or empty");
        }

        if (s == null) {
            return false;
        }
        if (s == expectedStart) {
            return true;
        }

        return s.regionMatches(true, 0, expectedStart, 0, expectedStart.length());
    }

    /**
     * Returns {@code true} if the given string ends with the specified suffix, ignoring case.
     *
     * @param s the string to check
     * @param expectedEnd the expected suffix (must not be {@code null} or empty)
     * @return {@code true} if the string ends with the given suffix, ignoring case
     */
    public static boolean endsWithIgnoreCase(final String s, final String expectedEnd) {
        if (expectedEnd == null) {
            throw new IllegalArgumentException("Expected end string can't be null or empty");
        }

        final int expectedEndLength = expectedEnd.length();
        if (expectedEndLength == 0) {
            throw new IllegalArgumentException("Expected end string can't be null or empty");
        }

        if (s == null) {
            return false;
        }
        if (s == expectedEnd) {
            return true;
        }

        return s.regionMatches(true, s.length() - expectedEndLength, expectedEnd, 0, expectedEndLength);
    }

    /**
     * Returns {@code true} if the given string contains the specified substring, ignoring case.
     *
     * @param s the string to check
     * @param expected the substring to look for (must not be {@code null} or empty)
     * @return {@code true} if the string contains the substring, ignoring case
     */
    public static boolean containsIgnoreCase(final String s, final String expected) {
        if (expected == null) {
            throw new IllegalArgumentException("Expected string can't be null or empty");
        }

        final int expectedLength = expected.length();
        if (expectedLength == 0) {
            throw new IllegalArgumentException("Expected string can't be null or empty");
        }

        if (s == null) {
            return false;
        }
        if (s == expected) {
            return true;
        }

        final int max = s.length() - expectedLength;
        for (int i = 0; i <= max; i++) {
            if (s.regionMatches(true, i, expected, 0, expectedLength)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Replaces multiple characters in a string in a single pass.
     * This method can also be used to delete characters by omitting them from {@code replaceChars}.
     *
     * @param str the string to process; may be {@code null}
     * @param searchChars the set of characters to search for; may be {@code null}
     * @param replaceChars the replacement characters; may be {@code null}
     * @return the modified string, or the original if no replacement was performed
     */
    @SuppressWarnings("null")
    public static String replaceChars(final String str, final String searchChars, final String replaceChars) {
        if (isEmptyOrNull(str) || isEmptyOrNull(searchChars)) {
            return str;
        }

        final int replaceCharsLength = replaceChars == null ? 0 : replaceChars.length();
        final int strLength = str.length();

        StringBuilder buf = null;
        int i = 0;
        for ( ; i < strLength; i++) {
            final char ch = str.charAt(i);
            final int index = searchChars.indexOf(ch);
            if (index != -1) {
                buf = new StringBuilder(strLength);
                buf.append(str, 0, i);
                if (index < replaceCharsLength) {
                    buf.append(replaceChars.charAt(index));
                }
                break;
            }
        }

        if (buf == null) {
            return str;
        }

        i++;
        for ( ; i < strLength; i++) {
            final char ch = str.charAt(i);
            final int index = searchChars.indexOf(ch);
            if (index != -1) {
                if (index < replaceCharsLength) {
                    buf.append(replaceChars.charAt(index));
                }
            }
            else {
                buf.append(ch);
            }
        }

        return buf.toString();
    }

    /**
     * Returns the substring after the first occurrence of the specified separator.
     * The separator itself is not included.
     * <p>
     * Returns {@code null} for a {@code null} input string.
     * Returns an empty string for an empty input string.
     * Returns an empty string if the separator is not found.
     * Returns an empty string if the separator is {@code null} and the input is not {@code null}.
     * </p>
     *
     * @param str the string to search; may be {@code null}
     * @param find the separator to find; may be {@code null}
     * @return the substring after the first occurrence of the separator, or {@code null} if input is {@code null}
     */
    public static String substringAfter(final String str, final String find) {
        if (isEmptyOrNull(str)) {
            return str;
        }
        if (find == null) {
            return EMPTY_STRING;
        }
        final int pos = str.indexOf(find);
        if (pos == -1) {
            return EMPTY_STRING;
        }
        return str.substring(pos + find.length());
    }

    /**
     * Escapes the characters {@code <}, {@code >} and {@code &} into their XML entity equivalents.
     *
     * @param s the string to escape
     * @return the escaped form of the specified string
     */
    public static String escapeXmlChars(final String s) {
        return org.apache.commons.lang3.StringUtils.
                replaceEach(s, new String[] {"&", "<", ">"}, new String[] {"&amp;", "&lt;", "&gt;"});
    }

    /**
     * Escapes a string for use as XML 1.0 content, replacing
     * {@code "}, {@code &}, {@code '}, {@code <}, and {@code >} with their XML entity equivalents.
     * Invalid XML 1.0 code points are removed.
     *
     * @param text the text to escape
     * @return the escaped value, or {@code null} if the input is {@code null}
     */
    public static String escapeXml(final String text) {
        if (text == null) {
            return null;
        }

        StringBuilder escaped = null;

        final int offset = 0;
        final int max = text.length();

        int readOffset = offset;

        for (int i = offset; i < max; i++) {
            final int codepoint = Character.codePointAt(text, i);
            final boolean codepointValid = supportedByXML10(codepoint);

            if (!codepointValid
                    || codepoint == '<'
                    || codepoint == '>'
                    || codepoint == '&'
                    || codepoint == '\''
                    || codepoint == '"') {

                // replacement required
                if (escaped == null) {
                    escaped = new StringBuilder(max);
                }

                if (i > readOffset) {
                    escaped.append(text, readOffset, i);
                }

                if (Character.charCount(codepoint) > 1) {
                    i++;
                }
                readOffset = i + 1;

                // skip
                if (!codepointValid) {
                    continue;
                }

                if (codepoint == '<') {
                    escaped.append("&lt;");
                }
                else if (codepoint == '>') {
                    escaped.append("&gt;");
                }
                else if (codepoint == '&') {
                    escaped.append("&amp;");
                }
                else if (codepoint == '\'') {
                    escaped.append("&apos;");
                }
                else if (codepoint == '\"') {
                    escaped.append("&quot;");
                }
            }
        }

        if (escaped == null) {
            return text;
        }

        if (max > readOffset) {
            escaped.append(text, readOffset, max);
        }

        return escaped.toString();
    }

    /**
     * Escapes a string for use as an XML attribute value.
     * Only {@code <}, {@code &}, and {@code "} are escaped, as required by the
     * <a href="https://www.w3.org/TR/REC-xml/#d0e888">XML specification</a>.
     * Invalid XML 1.0 code points are removed.
     *
     * @param attValue the attribute value to escape
     * @return the escaped value, or {@code null} if the input is {@code null}
     */
    public static String escapeXmlAttributeValue(final String attValue) {
        if (attValue == null) {
            return null;
        }

        StringBuilder escaped = null;

        final int offset = 0;
        final int max = attValue.length();

        int readOffset = offset;

        for (int i = offset; i < max; i++) {
            final int codepoint = Character.codePointAt(attValue, i);
            final boolean codepointValid = supportedByXML10(codepoint);

            if (!codepointValid
                    || codepoint == '<'
                    || codepoint == '&'
                    || codepoint == '"') {

                // replacement required
                if (escaped == null) {
                    escaped = new StringBuilder(max);
                }

                if (i > readOffset) {
                    escaped.append(attValue, readOffset, i);
                }

                if (Character.charCount(codepoint) > 1) {
                    i++;
                }
                readOffset = i + 1;

                // skip
                if (!codepointValid) {
                    continue;
                }

                if (codepoint == '<') {
                    escaped.append("&lt;");
                }
                else if (codepoint == '&') {
                    escaped.append("&amp;");
                }
                else if (codepoint == '\"') {
                    escaped.append("&quot;");
                }
            }
        }

        if (escaped == null) {
            return attValue;
        }

        if (max > readOffset) {
            escaped.append(attValue, readOffset, max);
        }

        return escaped.toString();
    }

    /*
     * XML 1.0 does not allow control characters or unpaired Unicode surrogate codepoints.
     * We will remove characters that do not fit in the following ranges:
     * #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
     */
    private static boolean supportedByXML10(final int codepoint) {
        if (codepoint < 0x20) {
            return codepoint == 0x9 || codepoint == 0xA || codepoint == 0xD;
        }
        if (codepoint <= 0xD7FF) {
            return true;
        }

        if (codepoint < 0xE000) {
            return false;
        }
        if (codepoint <= 0xFFFD) {
            return true;
        }

        if (codepoint < 0x10000) {
            return false;
        }
        if (codepoint <= 0x10FFFF) {
            return true;
        }

        return true;
    }

    /**
     * Returns the index within the specified string of the first occurrence of
     * the specified search character within the given range.
     *
     * @param s the string to search
     * @param searchChar the character to search for
     * @param beginIndex the index at which to start the search
     * @param endIndex the index at which to stop the search (exclusive)
     * @return the index of the first occurrence, or {@code -1} if not found
     */
    public static int indexOf(final String s, final char searchChar, final int beginIndex, final int endIndex) {
        for (int i = beginIndex; i < endIndex; i++) {
            if (s.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a {@link Color} parsed from the given RGB hexadecimal notation,
     * or {@code null} if the token is not a valid hex color.
     *
     * @param token the token to parse
     * @return a {@link Color} if the token is a valid hex color; otherwise {@code null}
     */
    public static Color asColorHexadecimal(final String token) {
        if (token == null) {
            return null;
        }
        final Matcher tmpMatcher = HEX_COLOR.matcher(token);
        final boolean tmpFound = tmpMatcher.matches();
        if (!tmpFound) {
            return null;
        }

        final String tmpHex = tmpMatcher.group(1);
        if (tmpHex.length() == 6) {
            final int tmpRed = Integer.parseInt(tmpHex.substring(0, 2), 16);
            final int tmpGreen = Integer.parseInt(tmpHex.substring(2, 4), 16);
            final int tmpBlue = Integer.parseInt(tmpHex.substring(4, 6), 16);
            return new Color(tmpRed, tmpGreen, tmpBlue);
        }

        final int tmpRed = Integer.parseInt(tmpHex.substring(0, 1) + tmpHex.substring(0, 1), 16);
        final int tmpGreen = Integer.parseInt(tmpHex.substring(1, 2) + tmpHex.substring(1, 2), 16);
        final int tmpBlue = Integer.parseInt(tmpHex.substring(2, 3) + tmpHex.substring(2, 3), 16);
        return new Color(tmpRed, tmpGreen, tmpBlue);
    }

    /**
     * Returns a {@link Color} parsed from the first {@code rgb(...)} notation found within the token,
     * or {@code null} if none is found.
     *
     * @param token the token to parse
     * @return a {@link Color} if an RGB color is found; otherwise {@code null}
     */
    public static Color findColorRGB(final String token) {
        if (token == null) {
            return null;
        }
        final Matcher tmpMatcher = RGB_COLOR.matcher(token);
        if (!tmpMatcher.find()) {
            return null;
        }

        final int tmpRed = Integer.parseInt(tmpMatcher.group(1));
        final int tmpGreen = Integer.parseInt(tmpMatcher.group(2));
        final int tmpBlue = Integer.parseInt(tmpMatcher.group(3));
        return new Color(tmpRed, tmpGreen, tmpBlue);
    }

    /**
     * Returns a {@link Color} parsed from the first {@code rgba(...)} notation found within the token,
     * or {@code null} if none is found.
     *
     * @param token the token to parse
     * @return a {@link Color} if an RGBA color is found; otherwise {@code null}
     */
    public static Color findColorRGBA(final String token) {
        if (token == null) {
            return null;
        }
        final Matcher tmpMatcher = RGBA_COLOR.matcher(token);
        if (!tmpMatcher.find()) {
            return null;
        }

        final int tmpRed = Integer.parseInt(tmpMatcher.group(1));
        final int tmpGreen = Integer.parseInt(tmpMatcher.group(2));
        final int tmpBlue = Integer.parseInt(tmpMatcher.group(3));
        final int tmpAlpha = (int) (Float.parseFloat(tmpMatcher.group(4)) * 255);
        return new Color(tmpRed, tmpGreen, tmpBlue, tmpAlpha);
    }

    /**
     * Returns a {@link Color} parsed from the first {@code hsl(...)} notation found within the token,
     * or {@code null} if none is found.
     *
     * @param token the token to parse
     * @return a {@link Color} if an HSL color is found; otherwise {@code null}
     */
    public static Color findColorHSL(final String token) {
        if (token == null) {
            return null;
        }
        final Matcher tmpMatcher = HSL_COLOR.matcher(token);
        if (!tmpMatcher.find()) {
            return null;
        }

        final float tmpHue = Float.parseFloat(tmpMatcher.group(1)) / 360f;
        final float tmpSaturation = Float.parseFloat(tmpMatcher.group(4)) / 100f;
        final float tmpLightness = Float.parseFloat(tmpMatcher.group(7)) / 100f;
        return hslToRgb(tmpHue, tmpSaturation, tmpLightness);
    }

    /**
     * Converts an HSL color value to an RGB {@link Color}.
     * Conversion formula adapted from
     * <a href="https://en.wikipedia.org/wiki/HSL_color_space">Wikipedia: HSL color space</a>.
     * Assumes {@code h}, {@code s}, and {@code l} are in the range [0, 1].
     *
     * @param h the hue
     * @param s the saturation
     * @param l the lightness
     * @return the resulting {@link Color}
     */
    private static Color hslToRgb(final float h, final float s, final float l) {
        if (s == 0f) {
            return new Color(to255(l), to255(l), to255(l));
        }

        final float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
        final float p = 2 * l - q;
        final float r = hueToRgb(p, q, h + 1f / 3f);
        final float g = hueToRgb(p, q, h);
        final float b = hueToRgb(p, q, h - 1f / 3f);

        return new Color(to255(r), to255(g), to255(b));
    }

    private static float hueToRgb(final float p, final float q, float t) {
        if (t < 0f) {
            t += 1f;
        }

        if (t > 1f) {
            t -= 1f;
        }

        if (t < 1f / 6f) {
            return p + (q - p) * 6f * t;
        }

        if (t < 1f / 2f) {
            return q;
        }

        if (t < 2f / 3f) {
            return p + (q - p) * (2f / 3f - t) * 6f;
        }

        return p;
    }

    private static int to255(final float value) {
        return (int) Math.min(255, 256 * value);
    }

    /**
     * Formats the specified color as an {@code rgb(...)} string.
     *
     * @param color the color to format
     * @return the specified color formatted as {@code rgb(r, g, b)}
     */
    public static String formatColor(final Color color) {
        return "rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
    }

    /**
     * Sanitizes a string for use with {@link java.util.regex.Matcher#appendReplacement}.
     * Replaces all {@code \} with {@code \\} and {@code $} with {@code \$},
     * as these are control characters in {@code appendReplacement}.
     *
     * @param toSanitize the string to sanitize
     * @return the sanitized version of the string
     */
    public static String sanitizeForAppendReplacement(final String toSanitize) {
        return org.apache.commons.lang3.StringUtils.replaceEach(toSanitize,
                                    new String[] {"\\", "$"}, new String[]{"\\\\", "\\$"});
    }

    /**
     * Sanitizes a string for use as a filename by replacing illegal characters
     * ({@code \}, {@code /}, {@code |}, {@code :}, {@code ?}, {@code *},
     * {@code "}, {@code <}, {@code >}, control characters) with {@code _}.
     *
     * @param toSanitize the string to sanitize
     * @return the sanitized version of the string
     */
    public static String sanitizeForFileName(final String toSanitize) {
        return ILLEGAL_FILE_NAME_CHARS.matcher(toSanitize).replaceAll("_");
    }

    /**
     * Transforms a delimiter-separated CSS property name (e.g. {@code font-size})
     * into camel case (e.g. {@code fontSize}).
     *
     * @param string the string to camelize
     * @return the camelized string
     */
    public static String cssCamelize(final String string) {
        if (string == null) {
            return null;
        }

        String result = CAMELIZE_CACHE.get(string);
        if (null != result) {
            return result;
        }

        // not found in CamelizeCache_; convert and store in cache
        final int pos = string.indexOf('-');
        if (pos == -1 || pos == string.length() - 1) {
            // cache also this strings for performance
            CAMELIZE_CACHE.put(string, string);
            return string;
        }

        final StringBuilder builder = new StringBuilder(string);
        builder.deleteCharAt(pos);
        builder.setCharAt(pos, Character.toUpperCase(builder.charAt(pos)));

        int i = pos + 1;
        while (i < builder.length() - 1) {
            if (builder.charAt(i) == '-') {
                builder.deleteCharAt(i);
                builder.setCharAt(i, Character.toUpperCase(builder.charAt(i)));
            }
            i++;
        }
        result = builder.toString();
        CAMELIZE_CACHE.put(string, result);

        return result;
    }

    /**
     * Converts the given string to lowercase using the ROOT locale.
     * Returns {@code null} if the input is {@code null}.
     *
     * @param s the string to lowercase
     * @return the lowercased string, or {@code null}
     */
    public static String toRootLowerCase(final String s) {
        return s == null ? null : s.toLowerCase(Locale.ROOT);
    }

    /**
     * Converts the given string to a byte array using the specified charset.
     * Returns an empty byte array if the string is {@code null} or empty, or if the charset is unsupported.
     *
     * @param charset the charset to use for encoding
     * @param content the string to convert
     * @return the string as a byte array, or an empty array if the string is {@code null} or empty
     */
    public static byte[] toByteArray(final String content, final Charset charset) {
        if (content ==  null || content.isEmpty()) {
            return new byte[0];
        }

        return content.getBytes(charset);
    }

    /**
     * Splits the given text on whitespace as defined by {@link Character#isWhitespace(char)}.
     *
     * @param str the string to split; may be {@code null}
     * @return an array of parsed strings, or an empty array if the input is {@code null}
     */
    public static String[] splitAtJavaWhitespace(final String str) {
        final String[] parts = org.apache.commons.lang3.StringUtils.split(str);
        if (parts == null) {
            return new String[0];
        }
        return parts;
    }

    /**
     * Splits the given text on blank (space) characters.
     *
     * @param str the string to split; may be {@code null}
     * @return an array of parsed strings, or an empty array if the input is {@code null}
     */
    public static String[] splitAtBlank(final String str) {
        final String[] parts = org.apache.commons.lang3.StringUtils.split(str, ' ');
        if (parts == null) {
            return new String[0];
        }
        return parts;
    }

    /**
     * Splits the given text on comma characters.
     *
     * @param str the string to split; may be {@code null}
     * @return an array of parsed strings, or an empty array if the input is {@code null}
     */
    public static String[] splitAtComma(final String str) {
        final String[] parts = org.apache.commons.lang3.StringUtils.split(str, ',');
        if (parts == null) {
            return new String[0];
        }
        return parts;
    }

    /**
     * Splits the given text on comma or blank (space) characters.
     *
     * @param str the string to split; may be {@code null}
     * @return an array of parsed strings, or an empty array if the input is {@code null}
     */
    public static String[] splitAtCommaOrBlank(final String str) {
        final String[] parts = org.apache.commons.lang3.StringUtils.split(str, ", ");
        if (parts == null) {
            return new String[0];
        }
        return parts;
    }

    /**
     * Returns the substring before the first occurrence of the specified separator.
     * The separator itself is not included in the result.
     * {@code null} input returns {@code null}; empty input returns an empty string.
     *
     * @param str the string to search; may be {@code null}
     * @param find the separator to find; must not be {@code null} or empty
     * @return the substring before the first occurrence of the separator,
     *         or the full string if the separator is not found
     */
    public static String substringBefore(final String str, final String find) {
        if (isEmptyOrNull(find)) {
            throw new IllegalArgumentException("'find' string parameter has to be not empty and not null");
        }

        if (isEmptyString(str)) {
            return str;
        }

        final int pos = str.indexOf(find);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * Parses an integer from the given string, returning the default value if parsing fails
     * or the string is {@code null}.
     *
     * @param str the string to parse; may be {@code null}
     * @param defaultValue the value to return if parsing fails
     * @return the parsed integer, or {@code defaultValue}
     */
    public static int toInt(final String str, final int defaultValue) {
        try {
            return Integer.parseInt(str);
        }
        catch (final RuntimeException e) {
            return defaultValue;
        }
    }

    /**
     * Parses a float from the given string, returning the default value if parsing fails
     * or the string is {@code null}.
     *
     * @param str the string to parse; may be {@code null}
     * @param defaultValue the value to return if parsing fails
     * @return the parsed float, or {@code defaultValue}
     */
    public static float toFloat(final String str, final float defaultValue) {
        try {
            return Float.parseFloat(str);
        }
        catch (final RuntimeException e) {
            return defaultValue;
        }
    }

    /**
     * Strips trailing whitespace from the given string.
     * Returns {@code null} for a {@code null} input; returns the string unchanged if it has no trailing whitespace.
     *
     * @param str the string to trim; may be {@code null}
     * @return the trimmed string, or {@code null} if input is {@code null}
     */
    public static String trimRight(final String str) {
        if (isEmptyOrNull(str)) {
            return str;
        }

        int end = str.length();
        while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
            end--;
        }

        if (end == str.length()) {
            return str;
        }

        return str.substring(0, end);
    }

    /**
     * Returns {@code true} if the given sequence is non-{@code null} and contains only characters
     * from the provided set of valid characters.
     *
     * @param cs the sequence to check; may be {@code null}
     * @param valid the array of valid characters; must not be {@code null} or empty
     * @return {@code true} if all characters in the sequence are valid
     */
    public static boolean containsOnly(final CharSequence cs, final char... valid) {
        if (valid == null || valid.length == 0) {
            throw new IllegalArgumentException("Expected valid char[] can't be null or empty");
        }
        if (isEmptyOrNull(cs)) {
            return false;
        }

        final int csLength = cs.length();
        final int validLength = valid.length;
        for (int i = 0; i < csLength; i++) {
            final char testChar = cs.charAt(i);
            int j = 0;
            for ( ; j < validLength; j++) {
                final char validChar = valid[j];
                if (validChar == testChar) {
                    break;
                }
            }
            if (j == validLength) {
                return false;
            }
        }

        return true;
    }
}
