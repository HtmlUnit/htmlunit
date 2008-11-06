/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

/**
 * String utilities class for utility functions not covered by third party libraries.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Martin Tamme
 */
public final class StringUtils {

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
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
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
     * Returns the index within a given string of the first occurrence of
     * the specified search character.
     *
     * @param s          a string.
     * @param searchChar a search character.
     * @param beginIndex the index to start the search from.
     * @param endIndex   the index to stop the search.
     * @return the index of the first occurrence of the character in the string or <tt>-1</tt>.
     */
    public static int indexOf(
            final String s,
            final char searchChar,
            final int beginIndex,
            final int endIndex) {
        for (int i = beginIndex; i < endIndex; i++) {
            if (s.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns <tt>true</tt> if the specified string is a valid float, possibly triming the string before checking.
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

}
