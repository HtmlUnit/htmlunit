/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import static org.apache.commons.lang.ArrayUtils.contains;
import static org.apache.commons.lang.ArrayUtils.indexOf;
import static org.apache.commons.lang.ArrayUtils.isEquals;
import static org.apache.commons.lang.ArrayUtils.subarray;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Sniffs encoding settings from HTML, XML or other content. The HTML encoding sniffing algorithm is based on the
 * <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#determining-the-character-encoding">HTML5
 * encoding sniffing algorithm</a>.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public final class EncodingSniffer {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(EncodingSniffer.class);

    /** UTF-16 (little endian) charset name. */
    static final String UTF16_LE = "UTF-16LE";

    /** UTF-16 (big endian) charset name. */
    static final String UTF16_BE = "UTF-16BE";

    /** UTF-8 charset name. */
    static final String UTF8 = "UTF-8";

    /** Sequence(s) of bytes indicating the beginning of a comment. */
    private static final byte[][] COMMENT_START = new byte[][] {
        new byte[] {'<'},
        new byte[] {'!'},
        new byte[] {'-'},
        new byte[] {'-'}
    };

    /** Sequence(s) of bytes indicating the beginning of a <tt>meta</tt> HTML tag. */
    private static final byte[][] META_START = new byte[][] {
        new byte[] {'<'},
        new byte[] {'m', 'M'},
        new byte[] {'e', 'E'},
        new byte[] {'t', 'T'},
        new byte[] {'a', 'A'},
        new byte[] {0x09, 0x0A, 0x0C, 0x0D, 0x20, 0x2F}
    };

    /** Sequence(s) of bytes indicating the beginning of miscellaneous HTML content. */
    private static final byte[][] OTHER_START = new byte[][] {
        new byte[] {'<'},
        new byte[] {'!', '/', '?'}
    };

    /** Sequence(s) of bytes indicating the beginning of a charset specification. */
    private static final byte[][] CHARSET_START = new byte[][] {
        new byte[] {'c', 'C'},
        new byte[] {'h', 'H'},
        new byte[] {'a', 'A'},
        new byte[] {'r', 'R'},
        new byte[] {'s', 'S'},
        new byte[] {'e', 'E'},
        new byte[] {'t', 'T'}
    };

    /**
     * The number of HTML bytes to sniff for encoding info embedded in <tt>meta</tt> tags;
     * relatively large because we don't have a fallback.
     */
    private static final int SIZE_OF_HTML_CONTENT_SNIFFED = 4096;

    /**
     * The number of XML bytes to sniff for encoding info embedded in the XML declaration;
     * relatively small because it's always at the very beginning of the file.
     */
    private static final int SIZE_OF_XML_CONTENT_SNIFFED = 512;

    /**
     * Disallow instantiation of this class.
     */
    private EncodingSniffer() {
        // Empty.
    }

    /**
     * <p>If the specified content is HTML content, this method sniffs encoding settings
     * from the specified HTML content and/or the corresponding HTTP headers based on the
     * <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#determining-the-character-encoding">HTML5
     * encoding sniffing algorithm</a>.</p>
     *
     * <p>If the specified content is XML content, this method sniffs encoding settings
     * from the specified XML content and/or the corresponding HTTP headers using a custom algorithm.</p>
     *
     * <p>Otherwise, this method sniffs encoding settings from the specified content of unknown type by looking for
     * <tt>Content-Type</tt> information in the HTTP headers and
     * <a href="http://en.wikipedia.org/wiki/Byte_Order_Mark">Byte Order Mark</a> information in the content.</p>
     *
     * <p>Note that if an encoding is found but it is not supported on the current platform, this method returns
     * <tt>null</tt>, as if no encoding had been found.</p>
     *
     * @param headers the HTTP response headers sent back with the content to be sniffed
     * @param content the content to be sniffed
     * @return the encoding sniffed from the specified content and/or the corresponding HTTP headers,
     *         or <tt>null</tt> if the encoding could not be determined
     * @throws IOException if an IO error occurs
     */
    public static String sniffEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {
        if (isHtml(headers)) {
            return sniffHtmlEncoding(headers, content);
        }
        else if (isXml(headers)) {
            return sniffXmlEncoding(headers, content);
        }
        else {
            return sniffUnknownContentTypeEncoding(headers, content);
        }
    }

    /**
     * Returns <tt>true</tt> if the specified HTTP response headers indicate an HTML response.
     *
     * @param headers the HTTP response headers
     * @return <tt>true</tt> if the specified HTTP response headers indicate an HTML response
     */
    static boolean isHtml(final List<NameValuePair> headers) {
        return contentTypeEndsWith(headers, "text/html");
    }

    /**
     * Returns <tt>true</tt> if the specified HTTP response headers indicate an XML response.
     *
     * @param headers the HTTP response headers
     * @return <tt>true</tt> if the specified HTTP response headers indicate an XML response
     */
    static boolean isXml(final List<NameValuePair> headers) {
        return contentTypeEndsWith(headers, "text/xml", "application/xml", "text/vnd.wap.wml", "+xml");
    }

    /**
     * Returns <tt>true</tt> if the specified HTTP response headers contain a <tt>Content-Type</tt> that
     * ends with one of the specified strings.
     *
     * @param headers the HTTP response headers
     * @param contentTypeEndings the content type endings to search for
     * @return <tt>true</tt> if the specified HTTP response headers contain a <tt>Content-Type</tt> that
     *         ends with one of the specified strings
     */
    static boolean contentTypeEndsWith(final List<NameValuePair> headers, final String... contentTypeEndings) {
        for (final NameValuePair pair : headers) {
            final String name = pair.getName();
            if ("content-type".equalsIgnoreCase(name)) {
                String value = pair.getValue();
                final int i = value.indexOf(';');
                if (i != -1) {
                    value = value.substring(0, i);
                }
                value = value.trim();
                boolean found = false;
                for (String ending : contentTypeEndings) {
                    if (value.toLowerCase().endsWith(ending.toLowerCase())) {
                        found = true;
                        break;
                    }
                }
                return found;
            }
        }
        return false;
    }

    /**
     * <p>Sniffs encoding settings from the specified HTML content and/or the corresponding HTTP headers based on the
     * <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#determining-the-character-encoding">HTML5
     * encoding sniffing algorithm</a>.</p>
     *
     * <p>Note that if an encoding is found but it is not supported on the current platform, this method returns
     * <tt>null</tt>, as if no encoding had been found.</p>
     *
     * @param headers the HTTP response headers sent back with the HTML content to be sniffed
     * @param content the HTML content to be sniffed
     * @return the encoding sniffed from the specified HTML content and/or the corresponding HTTP headers,
     *         or <tt>null</tt> if the encoding could not be determined
     * @throws IOException if an IO error occurs
     */
    public static String sniffHtmlEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {

        String encoding = sniffEncodingFromHttpHeaders(headers);
        if (encoding != null || content == null) {
            return encoding;
        }

        byte[] bytes = read(content, 3);
        encoding = sniffEncodingFromUnicodeBom(bytes);
        if (encoding != null) {
            return encoding;
        }

        bytes = readAndPrepend(content, SIZE_OF_HTML_CONTENT_SNIFFED, bytes);
        encoding = sniffEncodingFromMetaTag(bytes);
        return encoding;
    }

    /**
     * <p>Sniffs encoding settings from the specified XML content and/or the corresponding HTTP headers using
     * a custom algorithm.</p>
     *
     * <p>Note that if an encoding is found but it is not supported on the current platform, this method returns
     * <tt>null</tt>, as if no encoding had been found.</p>
     *
     * @param headers the HTTP response headers sent back with the XML content to be sniffed
     * @param content the XML content to be sniffed
     * @return the encoding sniffed from the specified XML content and/or the corresponding HTTP headers,
     *         or <tt>null</tt> if the encoding could not be determined
     * @throws IOException if an IO error occurs
     */
    public static String sniffXmlEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {

        String encoding = sniffEncodingFromHttpHeaders(headers);
        if (encoding != null || content == null) {
            return encoding;
        }

        byte[] bytes = read(content, 3);
        encoding = sniffEncodingFromUnicodeBom(bytes);
        if (encoding != null) {
            return encoding;
        }

        bytes = readAndPrepend(content, SIZE_OF_XML_CONTENT_SNIFFED, bytes);
        encoding = sniffEncodingFromXmlDeclaration(bytes);
        return encoding;
    }

    /**
     * <p>Sniffs encoding settings from the specified content of unknown type by looking for <tt>Content-Type</tt>
     * information in the HTTP headers and <a href="http://en.wikipedia.org/wiki/Byte_Order_Mark">Byte Order Mark</a>
     * information in the content.</p>
     *
     * <p>Note that if an encoding is found but it is not supported on the current platform, this method returns
     * <tt>null</tt>, as if no encoding had been found.</p>
     *
     * @param headers the HTTP response headers sent back with the content to be sniffed
     * @param content the content to be sniffed
     * @return the encoding sniffed from the specified content and/or the corresponding HTTP headers,
     *         or <tt>null</tt> if the encoding could not be determined
     * @throws IOException if an IO error occurs
     */
    public static String sniffUnknownContentTypeEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {

        String encoding = sniffEncodingFromHttpHeaders(headers);
        if (encoding != null || content == null) {
            return encoding;
        }

        final byte[] bytes = read(content, 3);
        encoding = sniffEncodingFromUnicodeBom(bytes);
        return encoding;
    }

    /**
     * Attempts to sniff an encoding from the specified HTTP headers.
     *
     * @param headers the HTTP headers to examine
     * @return the encoding sniffed from the specified HTTP headers, or <tt>null</tt> if the encoding
     *         could not be determined
     */
    static String sniffEncodingFromHttpHeaders(final List<NameValuePair> headers) {
        String encoding = null;
        for (final NameValuePair pair : headers) {
            final String name = pair.getName();
            if ("content-type".equalsIgnoreCase(name)) {
                final String value = pair.getValue();
                encoding = extractEncodingFromContentType(value);
                if (encoding != null) {
                    break;
                }
            }
        }
        if (encoding != null && LOG.isDebugEnabled()) {
            LOG.debug("Encoding found in HTTP headers: '" + encoding + "'.");
        }
        return encoding;
    }

    /**
     * Attempts to sniff an encoding from a <a href="http://en.wikipedia.org/wiki/Byte_Order_Mark">Byte Order Mark</a>
     * in the specified byte array.
     *
     * @param bytes the bytes to check for a Byte Order Mark
     * @return the encoding sniffed from the specified bytes, or <tt>null</tt> if the encoding
     *         could not be determined
     */
    static String sniffEncodingFromUnicodeBom(final byte[] bytes) {
        String encoding = null;
        final byte[] markerUTF8 = {(byte) 0xef, (byte) 0xbb, (byte) 0xbf};
        final byte[] markerUTF16BE = {(byte) 0xfe, (byte) 0xff};
        final byte[] markerUTF16LE = {(byte) 0xff, (byte) 0xfe};
        if (bytes != null && isEquals(markerUTF8, subarray(bytes, 0, 3))) {
            encoding = UTF8;
        }
        else if (bytes != null && isEquals(markerUTF16BE, subarray(bytes, 0, 2))) {
            encoding = UTF16_BE;
        }
        else if (bytes != null && isEquals(markerUTF16LE, subarray(bytes, 0, 2))) {
            encoding = UTF16_LE;
        }
        if (encoding != null && LOG.isDebugEnabled()) {
            LOG.debug("Encoding found in Unicode Byte Order Mark: '" + encoding + "'.");
        }
        return encoding;
    }

    /**
     * Attempts to sniff an encoding from an HTML <tt>meta</tt> tag in the specified byte array.
     *
     * @param bytes the bytes to check for an HTML <tt>meta</tt> tag
     * @return the encoding sniffed from the specified bytes, or <tt>null</tt> if the encoding
     *         could not be determined
     */
    static String sniffEncodingFromMetaTag(final byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if (matches(bytes, i, COMMENT_START)) {
                i = indexOfSubArray(bytes, new byte[] {'-', '-', '>'}, i);
                if (i == -1) {
                    break;
                }
                i += 2;
            }
            else if (matches(bytes, i, META_START)) {
                i += META_START.length;
                for (Attribute att = getAttribute(bytes, i); att != null; att = getAttribute(bytes, i)) {
                    i = att.getUpdatedIndex();
                    final String name = att.getName();
                    final String value = att.getValue();
                    if ("charset".equals(name) || "content".equals(name)) {
                        String charset = null;
                        if ("charset".equals(name)) {
                            charset = value;
                        }
                        else if ("content".equals(name)) {
                            charset = extractEncodingFromContentType(value);
                            if (charset == null) {
                                continue;
                            }
                        }
                        if (UTF16_BE.equalsIgnoreCase(charset) || UTF16_LE.equalsIgnoreCase(charset)) {
                            charset = UTF8;
                        }
                        if (isSupportedCharset(charset)) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Encoding found in meta tag: '" + charset + "'.");
                            }
                            return charset;
                        }
                    }
                }
            }
            else if (i + 1 < bytes.length && bytes[i] == '<' && Character.isLetter(bytes[i + 1])) {
                i = skipToAnyOf(bytes, i, new byte[] {0x09, 0x0A, 0x0C, 0x0D, 0x20, 0x3E});
                if (i == -1) {
                    break;
                }
                Attribute att;
                while ((att = getAttribute(bytes, i)) != null) {
                    i = att.getUpdatedIndex();
                }
            }
            else if (i + 2 < bytes.length && bytes[i] == '<' && bytes[i + 1] == '/' && Character.isLetter(bytes[i + 2])) {
                i = skipToAnyOf(bytes, i, new byte[] {0x09, 0x0A, 0x0C, 0x0D, 0x20, 0x3E});
                if (i == -1) {
                    break;
                }
                Attribute attribute;
                while ((attribute = getAttribute(bytes, i)) != null) {
                    i = attribute.getUpdatedIndex();
                }
            }
            else if (matches(bytes, i, OTHER_START)) {
                i = skipToAnyOf(bytes, i, new byte[] {0x3E});
                if (i == -1) {
                    break;
                }
            }
        }
        return null;
    }

    /**
     * Extracts an attribute from the specified byte array, starting at the specified index, using the
     * <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#concept-get-attributes-when-sniffing">HTML5
     * attribute algorithm</a>.
     *
     * @param bytes the byte array to extract an attribute from
     * @param i the index to start searching from
     * @return the next attribute in the specified byte array, or <tt>null</tt> if one is not available
     */
    static Attribute getAttribute(final byte[] bytes, int i) {
        if (i >= bytes.length) {
            return null;
        }
        while (bytes[i] == 0x09 || bytes[i] == 0x0A || bytes[i] == 0x0C || bytes[i] == 0x0D || bytes[i] == 0x20 || bytes[i] == 0x2F) {
            i++;
            if (i >= bytes.length) {
                return null;
            }
        }
        if (bytes[i] == '>') {
            return null;
        }
        String name = "";
        String value = "";
        for ( ;; i++) {
            if (i >= bytes.length) {
                return new Attribute(name, value, i);
            }
            if (bytes[i] == '=' && name.length() > 0) {
                i++;
                break;
            }
            if (bytes[i] == 0x09 || bytes[i] == 0x0A || bytes[i] == 0x0C || bytes[i] == 0x0D || bytes[i] == 0x20) {
                while (bytes[i] == 0x09 || bytes[i] == 0x0A || bytes[i] == 0x0C || bytes[i] == 0x0D || bytes[i] == 0x20) {
                    i++;
                    if (i >= bytes.length) {
                        return new Attribute(name, value, i);
                    }
                }
                if (bytes[i] != '=') {
                    return new Attribute(name, value, i);
                }
                i++;
                break;
            }
            if (bytes[i] == '/' || bytes[i] == '>') {
                return new Attribute(name, value, i);
            }
            name += (char) bytes[i];
        }
        if (i >= bytes.length) {
            return new Attribute(name, value, i);
        }
        while (bytes[i] == 0x09 || bytes[i] == 0x0A || bytes[i] == 0x0C || bytes[i] == 0x0D || bytes[i] == 0x20) {
            i++;
            if (i >= bytes.length) {
                return new Attribute(name, value, i);
            }
        }
        if (bytes[i] == '"' || bytes[i] == '\'') {
            final byte b = bytes[i];
            for (i++; i < bytes.length; i++) {
                if (bytes[i] == b) {
                    i++;
                    return new Attribute(name, value, i);
                }
                else if (bytes[i] >= 'A' && bytes[i] <= 'Z') {
                    final byte b2 = (byte) (bytes[i] + 0x20);
                    value += (char) b2;
                }
                else {
                    value += (char) bytes[i];
                }
            }
            return new Attribute(name, value, i);
        }
        else if (bytes[i] == '>') {
            return new Attribute(name, value, i);
        }
        else if (bytes[i] >= 'A' && bytes[i] <= 'Z') {
            final byte b = (byte) (bytes[i] + 0x20);
            value += (char) b;
            i++;
        }
        else {
            value += (char) bytes[i];
            i++;
        }
        for ( ; i < bytes.length; i++) {
            if (bytes[i] == 0x09 || bytes[i] == 0x0A || bytes[i] == 0x0C || bytes[i] == 0x0D || bytes[i] == 0x20 || bytes[i] == 0x3E) {
                return new Attribute(name, value, i);
            }
            else if (bytes[i] >= 'A' && bytes[i] <= 'Z') {
                final byte b = (byte) (bytes[i] + 0x20);
                value += (char) b;
            }
            else {
                value += (char) bytes[i];
            }
        }
        return new Attribute(name, value, i);
    }

    /**
     * Extracts an encoding from the specified <tt>Content-Type</tt> value using
     * <a href="http://ietfreport.isoc.org/idref/draft-abarth-mime-sniff/">the IETF algorithm</a>; if
     * no encoding is found, this method returns <tt>null</tt>.
     *
     * @param s the <tt>Content-Type</tt> value to search for an encoding
     * @return the encoding found in the specified <tt>Content-Type</tt> value, or <tt>null</tt> if no
     *         encoding was found
     */
    static String extractEncodingFromContentType(final String s) {
        if (s == null) {
            return null;
        }
        final byte[] bytes = s.getBytes();
        int i;
        for (i = 0; i < bytes.length; i++) {
            if (matches(bytes, i, CHARSET_START)) {
                i += CHARSET_START.length;
                break;
            }
        }
        if (i == bytes.length) {
            return null;
        }
        while (bytes[i] == 0x09 || bytes[i] == 0x0A || bytes[i] == 0x0C || bytes[i] == 0x0D || bytes[i] == 0x20) {
            i++;
            if (i == bytes.length) {
                return null;
            }
        }
        if (bytes[i] != '=') {
            return null;
        }
        i++;
        if (i == bytes.length) {
            return null;
        }
        while (bytes[i] == 0x09 || bytes[i] == 0x0A || bytes[i] == 0x0C || bytes[i] == 0x0D || bytes[i] == 0x20) {
            i++;
            if (i == bytes.length) {
                return null;
            }
        }
        if (bytes[i] == '"') {
            if (bytes.length <= i + 1) {
                return null;
            }
            final int index = indexOf(bytes, (byte) '"', i + 1);
            if (index == -1) {
                return null;
            }
            final String charset = new String(subarray(bytes, i + 1, index));
            return isSupportedCharset(charset) ? charset : null;
        }
        if (bytes[i] == '\'') {
            if (bytes.length <= i + 1) {
                return null;
            }
            final int index = indexOf(bytes, (byte) '\'', i + 1);
            if (index == -1) {
                return null;
            }
            final String charset = new String(subarray(bytes, i + 1, index));
            return isSupportedCharset(charset) ? charset : null;
        }
        int end = skipToAnyOf(bytes, i, new byte[] {0x09, 0x0A, 0x0C, 0x0D, 0x20, 0x3B});
        if (end == -1) {
            end = bytes.length;
        }
        final String charset = new String(subarray(bytes, i, end));
        return isSupportedCharset(charset) ? charset : null;
    }

    /**
     * Searches the specified XML content for an XML declaration and returns the encoding if found,
     * otherwise returns <tt>null</tt>.
     *
     * @param bytes the XML content to sniff
     * @return the encoding of the specified XML content, or <tt>null</tt> if it could not be determined
     */
    static String sniffEncodingFromXmlDeclaration(final byte[] bytes) {
        String encoding = null;
        final byte[] declarationPrefix = "<?xml ".getBytes();
        if (isEquals(declarationPrefix, subarray(bytes, 0, declarationPrefix.length))) {
            final int index = ArrayUtils.indexOf(bytes, (byte) '?', 2);
            if (index + 1 < bytes.length && bytes[index + 1] == '>') {
                final String declaration = new String(bytes, 0, index + 2);
                int start = declaration.indexOf("encoding");
                if (start != -1) {
                    start += 8;
                    char delimiter;
                outer:
                    while (true) {
                        switch (declaration.charAt(start)) {
                            case '"':
                            case '\'':
                                delimiter = declaration.charAt(start);
                                start = start + 1;
                                break outer;

                            default:
                                start++;
                        }
                    }
                    final int end = declaration.indexOf(delimiter, start);
                    encoding = declaration.substring(start, end);
                }
            }
        }
        if (encoding != null && !isSupportedCharset(encoding)) {
            encoding = null;
        }
        if (encoding != null && LOG.isDebugEnabled()) {
            LOG.debug("Encoding found in XML declaration: '" + encoding + "'.");
        }
        return encoding;
    }

    /**
     * Returns <tt>true</tt> if the specified charset is supported on this platform.
     *
     * @param charset the charset to check
     * @return <tt>true</tt> if the specified charset is supported on this platform
     */
    static boolean isSupportedCharset(final String charset) {
        try {
            return Charset.isSupported(charset);
        }
        catch (final IllegalCharsetNameException e) {
            return false;
        }
    }

    /**
     * Returns <tt>true</tt> if the byte in the specified byte array at the specified index matches one of the
     * specified byte array patterns.
     *
     * @param bytes the byte array to search in
     * @param i the index at which to search
     * @param sought the byte array patterns to search for
     * @return <tt>true</tt> if the byte in the specified byte array at the specified index matches one of the
     *         specified byte array patterns
     */
    static boolean matches(final byte[] bytes, final int i, final byte[][] sought) {
        if (i + sought.length > bytes.length) {
            return false;
        }
        for (int x = 0; x < sought.length; x++) {
            final byte[] possibilities = sought[x];
            boolean match = false;
            for (int y = 0; y < possibilities.length; y++) {
                if (bytes[i + x] == possibilities[y]) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }
        return true;
    }

    /**
     * Skips ahead to the first occurrence of any of the specified targets within the specified array,
     * starting at the specified index. This method returns <tt>-1</tt> if none of the targets are found.
     *
     * @param bytes the array to search through
     * @param i the index to start looking at
     * @param targets the targets to search for
     * @return the index of the first occurrence of any of the specified targets within the specified array
     */
    static int skipToAnyOf(final byte[] bytes, int i, final byte[] targets) {
        for ( ; i < bytes.length; i++) {
            if (contains(targets, bytes[i])) {
                break;
            }
        }
        if (i == bytes.length) {
            i = -1;
        }
        return i;
    }

    /**
     * Finds the first index of the specified sub-array inside the specified array, starting at the
     * specified index. This method returns <tt>-1</tt> if the specified sub-array cannot be found.
     *
     * @param array the array to traverse for looking for the sub-array
     * @param subarray the sub-array to find
     * @param startIndex the start index to traverse forwards from
     * @return the index of the sub-array within the array
     */
    static int indexOfSubArray(final byte[] array, final byte[] subarray, final int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            boolean found = true;
            for (int j = 0; j < subarray.length; j++) {
                final byte a = array[i + j];
                final byte b = subarray[j];
                if (a != b) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Attempts to read <tt>size</tt> bytes from the specified input stream. Note that this method is not guaranteed
     * to be able to read <tt>size</tt> bytes; however, the returned byte array will always be the exact length of the
     * number of bytes read.
     *
     * @param content the input stream to read from
     * @param size the number of bytes to try to read
     * @return the bytes read from the specified input stream
     * @throws IOException if an IO error occurs
     */
    static byte[] read(final InputStream content, final int size) throws IOException {
        byte[] bytes = new byte[size];
        final int count = content.read(bytes);
        if (count == -1) {
            bytes = new byte[0];
        }
        else if (count < size) {
            final byte[] smaller = new byte[count];
            System.arraycopy(bytes, 0, smaller, 0, count);
            bytes = smaller;
        }
        return bytes;
    }

    /**
     * Attempts to read <tt>size</tt> bytes from the specified input stream and then prepends the specified prefix to
     * the bytes read, returning the resultant byte array. Note that this method is not guaranteed to be able to read
     * <tt>size</tt> bytes; however, the returned byte array will always be the exact length of the number of bytes
     * read plus the length of the prefix array.
     *
     * @param content the input stream to read from
     * @param size the number of bytes to try to read
     * @param prefix the byte array to prepend to the bytes read from the specified input stream
     * @return the bytes read from the specified input stream, prefixed by the specified prefix
     * @throws IOException if an IO error occurs
     */
    static byte[] readAndPrepend(final InputStream content, final int size, final byte[] prefix) throws IOException {
        final byte[] bytes = read(content, size);
        final byte[] joined = new byte[prefix.length + bytes.length];
        System.arraycopy(prefix, 0, joined, 0, prefix.length);
        System.arraycopy(bytes, 0, joined, prefix.length, bytes.length);
        return joined;
    }

    static class Attribute {
        private final String name_;
        private final String value_;
        private final int updatedIndex_;
        Attribute(final String name, final String value, final int updatedIndex) {
            name_ = name;
            value_ = value;
            updatedIndex_ = updatedIndex;
        }
        String getName() {
            return name_;
        }
        String getValue() {
            return value_;
        }
        int getUpdatedIndex() {
            return updatedIndex_;
        }
    }

}
