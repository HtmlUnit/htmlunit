/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.util;

import java.awt.Color;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.utils.DateUtils;

/**
 * String utilities class for utility functions not covered by third party libraries.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Martin Tamme
 * @author Ronald Brill
 */
public final class StringUtils {

    private static final Pattern HEX_COLOR = Pattern.compile("#([0-9a-fA-F]{3}|[0-9a-fA-F]{6})");
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

    /**
     * Disallow instantiation of this class.
     */
    private StringUtils() {
        // Empty.
    }

    /**
     * Escapes the characters '&lt;', '&gt;' and '&amp;' into their XML entity equivalents. Note that
     * sometimes we have to use this method instead of
     * {@link org.apache.commons.lang3.StringEscapeUtils#escapeXml(String)} or
     * {@link org.apache.commons.lang3.StringEscapeUtils#escapeHtml4(String)} because those methods
     * escape some unicode characters as well.
     *
     * @param s the string to escape
     * @return the escaped form of the specified string
     */
    public static String escapeXmlChars(final String s) {
        return org.apache.commons.lang3.StringUtils.
                replaceEach(s, new String[] {"&", "<", ">"}, new String[] {"&amp;", "&lt;", "&gt;"});
    }

    /**
     * Escape the string to be used as attribute value.
     * Only {@code <}, {@code &} and {@code "} have to be escaped (see http://www.w3.org/TR/REC-xml/#d0e888).
     * @param attValue the attribute value
     * @return the escaped value
     */
    public static String escapeXmlAttributeValue(final String attValue) {
        final int len = attValue.length();
        StringBuilder sb = null;
        for (int i = len - 1; i >= 0; --i) {
            final char c = attValue.charAt(i);
            String replacement = null;
            if (c == '<') {
                replacement = "&lt;";
            }
            else if (c == '&') {
                replacement = "&amp;";
            }
            else if (c == '\"') {
                replacement = "&quot;";
            }

            if (replacement != null) {
                if (sb == null) {
                    sb = new StringBuilder(attValue);
                }
                sb.replace(i, i + 1, replacement);
            }
        }

        if (sb != null) {
            return sb.toString();
        }
        return attValue;
    }

    /**
     * Returns the index within the specified string of the first occurrence of
     * the specified search character.
     *
     * @param s the string to search
     * @param searchChar the character to search for
     * @param beginIndex the index at which to start the search
     * @param endIndex the index at which to stop the search
     * @return the index of the first occurrence of the character in the string or <tt>-1</tt>
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
     * Parses the specified date string, assuming that it is formatted according to RFC 1123, RFC 1036 or as an ANSI
     * C HTTP date header. This method returns {@code null} if the specified string is {@code null} or unparseable.
     *
     * @param s the string to parse as a date
     * @return the date version of the specified string, or {@code null}
     */
    public static Date parseHttpDate(final String s) {
        if (s == null) {
            return null;
        }
        return DateUtils.parseDate(s);
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
     * @param aColor the color to format
     * @return the specified color, formatted
     */
    public static String formatColor(final Color aColor) {
        return "rgb(" + aColor.getRed() + ", " + aColor.getGreen() + ", " + aColor.getBlue() + ")";
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
}
