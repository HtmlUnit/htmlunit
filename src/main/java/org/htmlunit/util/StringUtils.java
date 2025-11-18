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

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlunit.html.impl.Color;

/**
 * String utilities class for utility functions not covered by third party libraries.
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
     * Returns true if the param is not null and empty. This is different from
     * {@link org.apache.commons.lang3.StringUtils#isEmpty(CharSequence)} because
     * this returns false if the provided string is null.
     *
     * @param s the string to check
     * @return true if the param is not null and empty
     */
    public static boolean isEmptyString(final CharSequence s) {
        return s != null && s.length() == 0;
    }

    /**
     * Returns true if the param is null or empty.
     *
     * @param s the string to check
     * @return true if the param is null or empty
     */
    public static boolean isEmptyOrNull(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * Returns either the passed in CharSequence, or if the CharSequence is
     * empty or {@code null}, the default value.
     *
     * @param <T> the kind of CharSequence
     * @param s  the CharSequence to check
     * @param defaultString the default to return if the input is empty or null
     * @return the passed in CharSequence, or the defaultString
     */
    public static <T extends CharSequence> T defaultIfEmptyOrNull(final T s, final T defaultString) {
        return isEmptyOrNull(s) ? defaultString : s;
    }

    /**
     * Tests if a CharSequence is null, empty, or contains only whitespace.
     *
     * @param s the CharSequence to check
     * @return true if a CharSequence is null, empty, or contains only whitespace
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
     * Tests if a CharSequence is NOT null, empty, or contains only whitespace.
     *
     * @param s the CharSequence to check
     * @return false if a CharSequence is null, empty, or contains only whitespace
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
     * @param expected the char that we expect
     * @param s the string to check
     * @return true if the provided string has only one char and this matches the expectation
     */
    public static boolean equalsChar(final char expected, final CharSequence s) {
        return s != null && s.length() == 1 && expected == s.charAt(0);
    }

    /**
     * Tests if a CharSequence starts with a specified prefix.
     *
     * @param s the string to check
     * @param expectedStart the string that we expect at the beginning (has to be not null and not empty)
     * @return true if the provided string has only one char and this matches the expectation
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
     * Tests if a CharSequence ends with a specified prefix.
     *
     * @param s the string to check
     * @param expectedEnd the string that we expect at the end (has to be not null and not empty)
     * @return true if the provided string has only one char and this matches the expectation
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
     * Tests if a CharSequence ends with a specified prefix.
     *
     * @param s the string to check
     * @param expected the string that we expect to be a substring (has to be not null and not empty)
     * @return true if the provided string has only one char and this matches the expectation
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
     * Replaces multiple characters in a String in one go.
     * This method can also be used to delete characters.
     *
     * @param str          String to replace characters in, may be null.
     * @param searchChars  a set of characters to search for, may be null.
     * @param replaceChars a set of characters to replace, may be null.
     * @return modified String, or the input string if no replace was done.
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
     * Gets the substring after the first occurrence of a separator. The separator is not returned.
     * <p>
     * A {@code null} string input will return {@code null}.
     * An empty ("") string input will return the empty string.
     * A {@code null} separator will return the empty string if the input string is not {@code null}.
     * <p>
     * If nothing is found, the empty string is returned.
     * <p>
     *
     * @param str the String to get a substring from, may be null.
     * @param find the String to find, may be null.
     * @return the substring after the first occurrence of the specified string, {@code null} if null String input.
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
     * Escapes the characters '&lt;', '&gt;' and '&amp;' into their XML entity equivalents.
     *
     * @param s the string to escape
     * @return the escaped form of the specified string
     */
    public static String escapeXmlChars(final String s) {
        return org.apache.commons.lang3.StringUtils.
                replaceEach(s, new String[] {"&", "<", ">"}, new String[] {"&amp;", "&lt;", "&gt;"});
    }

    /**
     * Escape the string to be used as xml 1.0 content be replacing the
     * characters '&quot;', '&amp;', '&#39;', '&lt;', and '&gt;' into their XML entity equivalents.
     * @param text the attribute value
     * @return the escaped value
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
     * Escape the string to be used as attribute value.
     * Only {@code <}, {@code &} and {@code "} have to be escaped (see
     * <a href="http://www.w3.org/TR/REC-xml/#d0e888">http://www.w3.org/TR/REC-xml/#d0e888</a>).
     * @param attValue the attribute value
     * @return the escaped value
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
     * the specified search character.
     *
     * @param s the string to search
     * @param searchChar the character to search for
     * @param beginIndex the index at which to start the search
     * @param endIndex the index at which to stop the search
     * @return the index of the first occurrence of the character in the string or <code>-1</code>
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
     * Returns a Color parsed from the given RGB in hexadecimal notation.
     * @param token the token to parse
     * @return a Color whether the token is a color RGB in hexadecimal notation; otherwise null
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
     * Returns a Color parsed from the given rgb notation if found inside the given string.
     * @param token the token to parse
     * @return a Color whether the token contains a color in RGB notation; otherwise null
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
     * Returns a Color parsed from the given rgb notation.
     * @param token the token to parse
     * @return a Color whether the token is a color in RGB notation; otherwise null
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
     * Returns a Color parsed from the given hsl notation if found inside the given string.
     * @param token the token to parse
     * @return a Color whether the token contains a color in RGB notation; otherwise null
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
     * Converts an HSL color value to RGB. Conversion formula
     * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
     * Assumes h, s, and l are contained in the set [0, 1]
     *
     * @param h the hue
     * @param s the saturation
     * @param l the lightness
     * @return {@link Color}
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
     * Formats the specified color.
     *
     * @param color the color to format
     * @return the specified color, formatted
     */
    public static String formatColor(final Color color) {
        return "rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
    }

    /**
     * Sanitize a string for use in Matcher.appendReplacement.
     * Replaces all \ with \\ and $ as \$ because they are used as control
     * characters in appendReplacement.
     *
     * @param toSanitize the string to sanitize
     * @return sanitized version of the given string
     */
    public static String sanitizeForAppendReplacement(final String toSanitize) {
        return org.apache.commons.lang3.StringUtils.replaceEach(toSanitize,
                                    new String[] {"\\", "$"}, new String[]{"\\\\", "\\$"});
    }

    /**
     * Sanitizes a string for use as filename.
     * Replaces \, /, |, :, ?, *, &quot;, &lt;, &gt;, control chars by _ (underscore).
     *
     * @param toSanitize the string to sanitize
     * @return sanitized version of the given string
     */
    public static String sanitizeForFileName(final String toSanitize) {
        return ILLEGAL_FILE_NAME_CHARS.matcher(toSanitize).replaceAll("_");
    }

    /**
     * Transforms the specified string from delimiter-separated (e.g. <code>font-size</code>)
     * to camel-cased (e.g. <code>fontSize</code>).
     * @param string the string to camelize
     * @return the transformed string
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
     * Lowercases a string by checking and check for null first. There
     * is no cache involved and the ROOT locale is used to convert it.
     *
     * @param s the string to lowercase
     * @return the lowercased string
     */
    public static String toRootLowerCase(final String s) {
        return s == null ? null : s.toLowerCase(Locale.ROOT);
    }

    /**
     * Transforms the specified string from camel-cased (e.g. <code>fontSize</code>)
     * to delimiter-separated (e.g. <code>font-size</code>).
     * to camel-cased .
     * @param string the string to decamelize
     * @return the transformed string
     */
    public static String cssDeCamelize(final String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            final char ch = string.charAt(i);
            if (Character.isUpperCase(ch)) {
                builder.append('-').append(Character.toLowerCase(ch));
            }
            else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    /**
     * Converts a string into a byte array using the specified encoding.
     *
     * @param charset the charset
     * @param content the string to convert
     * @return the String as a byte[]; if the specified encoding is not supported an empty byte[] will be returned
     */
    public static byte[] toByteArray(final String content, final Charset charset) {
        if (content ==  null || content.isEmpty()) {
            return new byte[0];
        }

        return content.getBytes(charset);
    }

    /**
     * Splits the provided text into an array, using whitespace as the
     * separator.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     *
     * @param str  the String to parse, may be null
     * @return an array of parsed Strings, an empty array if null String input
     */
    public static String[] splitAtJavaWhitespace(final String str) {
        final String[] parts = org.apache.commons.lang3.StringUtils.split(str);
        if (parts == null) {
            return new String[0];
        }
        return parts;
    }

    /**
     * Splits the provided text into an array, using blank as the
     * separator.
     *
     * @param str  the String to parse, may be null
     * @return an array of parsed Strings, an empty array if null String input
     */
    public static String[] splitAtBlank(final String str) {
        final String[] parts = org.apache.commons.lang3.StringUtils.split(str, ' ');
        if (parts == null) {
            return new String[0];
        }
        return parts;
    }

    /**
     * Splits the provided text into an array, using blank as the
     * separator.
     *
     * @param str  the String to parse, may be null
     * @return an array of parsed Strings, an empty array if null String input
     */
    public static String[] splitAtComma(final String str) {
        final String[] parts = org.apache.commons.lang3.StringUtils.split(str, ',');
        if (parts == null) {
            return new String[0];
        }
        return parts;
    }

    /**
     * Splits the provided text into an array, using comma or blank as the
     * separator.
     *
     * @param str the String to parse, may be null
     * @return an array of parsed Strings, an empty array if null String input
     */
    public static String[] splitAtCommaOrBlank(final String str) {
        final String[] parts = org.apache.commons.lang3.StringUtils.split(str, ", ");
        if (parts == null) {
            return new String[0];
        }
        return parts;
    }

    /**
     * Gets the substring before the first occurrence of a separator. The separator is not returned.
     * A {@code null} string input will return {@code null}.
     * An empty ("") string input will return the empty string.
     * A {@code null} or empty separator is not allowed (will throw).
     *
     * @param str the String to get a substring from, may be null.
     * @param find the String to find, not null and not empty
     * @return the substring before the first occurrence of the specified string, {@code null} if null String input.
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
     * Tries to converts a {@link String} into an {@code int}, returning a default value if the conversion fails.
     * If the string is {@code null}, the default value is returned.
     *
     * @param str the string to convert, may be null.
     * @param defaultValue the default value.
     * @return the int represented by the string, or the default if conversion fails or the provides str is {@code null}
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
     * Tries to converts a {@link String} into an {@code float}, returning a default value if the conversion fails.
     * If the string is {@code null}, the default value is returned.
     *
     * @param str the string to convert, may be null.
     * @param defaultValue the default value.
     * @return the float represented by the string, or the default if conversion fails or the provides str is {@code null}
     */
    public static float toFloat(final String str, final float defaultValue) {
        try {
            return Float.parseFloat(str);
        }
        catch (final RuntimeException e) {
            return defaultValue;
        }
    }
}
