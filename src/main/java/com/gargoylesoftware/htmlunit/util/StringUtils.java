/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.util.Collection;
import java.util.Date;

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
 */
public final class StringUtils {

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
     * {@link org.apache.commons.lang.StringEscapeUtils#escapeXml(String)} or
     * {@link org.apache.commons.lang.StringEscapeUtils#escapeHtml(String)} because those methods
     * escape some unicode characters as well.
     *
     * @param s the string to escape
     * @return the escaped form of the specified string
     */
    public static String escapeXmlChars(final String s) {
        return org.apache.commons.lang.StringUtils.
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
     * Returns <tt>true</tt> if the specified string is a valid float, possibly trimming the string before checking.
     *
     * @param s the string to check
     * @param trim whether or not to trim the string before checking
     * @return <tt>true</tt> if the specified string is a valid float, <tt>false</tt> otherwise
     */
    public static boolean isFloat(String s, final boolean trim) {
        if (trim) {
            s = s.trim();
        }

        boolean ok;
        try {
            Float.parseFloat(s);
            ok = true;
        }
        catch (final NumberFormatException e) {
            ok = false;
        }

        return ok;
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
        for (String s : strings) {
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
            LOG.warn("Unable to parse date: " + s);
            return null;
        }
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

}
