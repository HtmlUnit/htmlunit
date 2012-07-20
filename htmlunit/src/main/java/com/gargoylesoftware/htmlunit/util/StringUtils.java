/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.util;

import java.awt.Color;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import com.gargoylesoftware.htmlunit.WebAssert;

/**
 * String utilities class for utility functions not covered by third party libraries.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Martin Tamme
 * @author Ronald Brill
 */
public final class StringUtils {

    private static final Pattern HEX_COLOR = Pattern.compile("#([0-9a-fA-F]{3}|[0-9a-fA-F]{6})");
    private static final Pattern RGB_COLOR =
        Pattern.compile("rgb\\s*?\\(\\s*?(\\d{1,3})\\s*?,\\s*?(\\d{1,3})\\s*?,\\s*?(\\d{1,3})\\s*?\\)");
    private static final Log LOG = LogFactory.getLog(StringUtils.class);

    /**
     * Disallow instantiation of this class.
     */
    private StringUtils() {
        // Empty.
    }

    /**
     * Escapes the characters '<', '>' and '&' into their XML entity equivalents. Note that
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
     * Only <, & and " have to be escaped (see http://www.w3.org/TR/REC-xml/#d0e888).
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
     * Returns <tt>true</tt> if the specified string contains whitespace, <tt>false</tt> otherwise.
     *
     * @param s the string to check for whitespace
     * @return <tt>true</tt> if the specified string contains whitespace, <tt>false</tt> otherwise
     */
    public static boolean containsWhitespace(final String s) {
        for (final char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                return true;
            }
        }
        return false;
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
     * Returns <tt>true</tt> if the specified collection of strings contains the specified string, ignoring case.
     *
     * @param strings the strings to search
     * @param string the string to search for
     * @return <tt>true</tt> if the specified collection of strings contains the specified string, ignoring case
     */
    public static boolean containsCaseInsensitive(final Collection<String> strings, String string) {
        string = string.toLowerCase();
        for (final String s : strings) {
            if (s.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parses the specified date string, assuming that it is formatted according to RFC 1123, RFC 1036 or as an ANSI
     * C HTTP date header. This method returns <tt>null</tt> if the specified string is <tt>null</tt> or unparseable.
     *
     * @param s the string to parse as a date
     * @return the date version of the specified string, or <tt>null</tt>
     */
    public static Date parseHttpDate(final String s) {
        if (s == null) {
            return null;
        }
        try {
            return DateUtils.parseDate(s);
        }
        catch (final DateParseException e) {
            LOG.warn("Unable to parse http date: '" + s + "'");
            return null;
        }
    }

    /**
     * Returns true if the specified token is an RGB in hexadecimal notation.
     * @param token the token to check
     * @return whether the token is a color in hexadecimal notation or not
     */
    public static boolean isColorHexadecimal(final String token) {
        if (token == null) {
            return false;
        }
        return HEX_COLOR.matcher(token.trim()).matches();
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
            final Color tmpColor = new Color(tmpRed, tmpGreen, tmpBlue);
            return tmpColor;
        }

        final int tmpRed = Integer.parseInt(tmpHex.substring(0, 1) + tmpHex.substring(0, 1), 16);
        final int tmpGreen = Integer.parseInt(tmpHex.substring(1, 2) + tmpHex.substring(1, 2), 16);
        final int tmpBlue = Integer.parseInt(tmpHex.substring(2, 3) + tmpHex.substring(2, 3), 16);
        final Color tmpColor = new Color(tmpRed, tmpGreen, tmpBlue);
        return tmpColor;
    }

    /**
     * Returns true if the specified token is in RGB notation.
     * @param token the token to check
     * @return whether the token is a color in RGB notation or not
     */
    public static boolean isColorRGB(final String token) {
        if (token == null) {
            return false;
        }
        return RGB_COLOR.matcher(token.trim()).matches();
    }

    /**
     * Returns a Color parsed from the given rgb notation.
     * @param token the token to parse
     * @return a Color whether the token is a color in RGB notation; otherwise null
     */
    public static Color asColorRGB(final String token) {
        if (token == null) {
            return null;
        }
        final Matcher tmpMatcher = RGB_COLOR.matcher(token);
        final boolean tmpFound = tmpMatcher.matches();
        if (!tmpFound) {
            return null;
        }

        final int tmpRed = Integer.parseInt(tmpMatcher.group(1));
        final int tmpGreen = Integer.parseInt(tmpMatcher.group(2));
        final int tmpBlue = Integer.parseInt(tmpMatcher.group(3));
        final Color tmpColor = new Color(tmpRed, tmpGreen, tmpBlue);
        return tmpColor;
    }

    /**
     * Returns a Color parsed from the given rgb notation.
     * @param token the token to parse
     * @return a Color whether the token is a color in RGB notation; otherwise null
     */
    public static Color findColorRGB(final String token) {
        if (token == null) {
            return null;
        }
        final Matcher tmpMatcher = RGB_COLOR.matcher(token);
        final boolean tmpFound = tmpMatcher.find();
        if (!tmpFound) {
            return null;
        }

        final int tmpRed = Integer.parseInt(tmpMatcher.group(1));
        final int tmpGreen = Integer.parseInt(tmpMatcher.group(2));
        final int tmpBlue = Integer.parseInt(tmpMatcher.group(3));
        final Color tmpColor = new Color(tmpRed, tmpGreen, tmpBlue);
        return tmpColor;
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
     * Formats the specified date according to RFC 1123.
     *
     * @param date the date to format
     * @return the specified date, formatted according to RFC 1123
     */
    public static String formatHttpDate(final Date date) {
        WebAssert.notNull("date", date);
        return DateUtils.formatDate(date);
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
        final String toReplace = org.apache.commons.lang3.StringUtils.replaceEach(toSanitize,
                new String[] {"\\", "$"}, new String[]{"\\\\", "\\$"});
        return toReplace;
    }
}
