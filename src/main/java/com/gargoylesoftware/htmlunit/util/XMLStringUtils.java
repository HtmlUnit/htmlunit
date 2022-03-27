package com.gargoylesoftware.htmlunit.util;

public class XMLStringUtils {

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
}
