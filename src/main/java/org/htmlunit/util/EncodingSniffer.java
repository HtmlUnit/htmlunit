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

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_16LE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.HttpHeader;
import org.htmlunit.cyberneko.xerces.util.StandardEncodingTranslator;

/**
 * Sniffs encoding settings from HTML, XML or other content. The HTML encoding sniffing algorithm is based on the
 * <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#determining-the-character-encoding">HTML5
 * encoding sniffing algorithm</a>.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
public final class EncodingSniffer {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(EncodingSniffer.class);

    /** Sequence(s) of bytes indicating the beginning of a comment. */
    private static final byte[][] COMMENT_START = {
        new byte[] {'<'},
        new byte[] {'!'},
        new byte[] {'-'},
        new byte[] {'-'}
    };

    /** Sequence(s) of bytes indicating the beginning of a <code>meta</code> HTML tag. */
    private static final byte[][] META_START = {
        new byte[] {'<'},
        new byte[] {'m', 'M'},
        new byte[] {'e', 'E'},
        new byte[] {'t', 'T'},
        new byte[] {'a', 'A'},
        new byte[] {0x09, 0x0A, 0x0C, 0x0D, 0x20, 0x2F}
    };

    /** Sequence(s) of bytes indicating the beginning of miscellaneous HTML content. */
    private static final byte[][] OTHER_START = {
        new byte[] {'<'},
        new byte[] {'!', '/', '?'}
    };

    /** Sequence(s) of bytes indicating the beginning of a charset specification. */
    private static final byte[][] CHARSET_START = {
        new byte[] {'c', 'C'},
        new byte[] {'h', 'H'},
        new byte[] {'a', 'A'},
        new byte[] {'r', 'R'},
        new byte[] {'s', 'S'},
        new byte[] {'e', 'E'},
        new byte[] {'t', 'T'}
    };

    private static final byte[] WHITESPACE = {0x09, 0x0A, 0x0C, 0x0D, 0x20, 0x3E};
    private static final byte[] COMMENT_END = {'-', '-', '>'};

    private static final byte[] XML_DECLARATION_PREFIX = "<?xml ".getBytes(US_ASCII);

    private static final byte[] CSS_CHARSET_DECLARATION_PREFIX = "@charset \"".getBytes(US_ASCII);

    /**
     * The number of HTML bytes to sniff for encoding info embedded in <code>meta</code> tags;
     */
    private static final int SIZE_OF_HTML_CONTENT_SNIFFED = 1024;

    /**
     * The number of XML bytes to sniff for encoding info embedded in the XML declaration;
     * relatively small because it's always at the very beginning of the file.
     */
    private static final int SIZE_OF_XML_CONTENT_SNIFFED = 512;

    private static final int SIZE_OF_CSS_CONTENT_SNIFFED = 1024;

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
     * <code>Content-Type</code> information in the HTTP headers and
     * <a href="http://en.wikipedia.org/wiki/Byte_Order_Mark">Byte Order Mark</a> information in the content.</p>
     *
     * <p>Note that if an encoding is found but it is not supported on the current platform, this method returns
     * {@code null}, as if no encoding had been found.</p>
     *
     * @param headers the HTTP response headers sent back with the content to be sniffed
     * @param content the content to be sniffed
     * @return the encoding sniffed from the specified content and/or the corresponding HTTP headers,
     *         or {@code null} if the encoding could not be determined
     * @throws IOException if an IO error occurs
     *
     * @deprecated as of version 4.0.0; depending on the content use {@link #sniffEncodingFromMetaTag(InputStream)},
     * {@link #sniffEncodingFromXmlDeclaration(InputStream)}, or {@link #sniffEncodingFromCssDeclaration(InputStream) }
     * instead
     */
    @Deprecated
    public static Charset sniffEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {
        final Charset charset;
        if (isHtml(headers)) {
            charset = sniffHtmlEncoding(headers, content);
        }
        else if (isXml(headers)) {
            charset = sniffXmlEncoding(headers, content);
        }
        else if (contentTypeEndsWith(headers, MimeType.TEXT_CSS)) {
            charset = sniffCssEncoding(headers, content);
        }
        else {
            charset = sniffUnknownContentTypeEncoding(headers, content);
        }
        return charset;
    }

    /**
     * Returns {@code true} if the specified HTTP response headers indicate an HTML response.
     *
     * @param headers the HTTP response headers
     * @return {@code true} if the specified HTTP response headers indicate an HTML response
     *
     * @deprecated as of version 4.0.0; method will be removed without replacement
     */
    @Deprecated
    static boolean isHtml(final List<NameValuePair> headers) {
        return contentTypeEndsWith(headers, MimeType.TEXT_HTML);
    }

    /**
     * Returns {@code true} if the specified HTTP response headers indicate an XML response.
     *
     * @param headers the HTTP response headers
     * @return {@code true} if the specified HTTP response headers indicate an XML response
     *
     * @deprecated as of version 4.0.0; method will be removed without replacement
     */
    @Deprecated
    static boolean isXml(final List<NameValuePair> headers) {
        return contentTypeEndsWith(headers, MimeType.TEXT_XML, MimeType.APPLICATION_XML, "text/vnd.wap.wml", "+xml");
    }

    /**
     * Returns {@code true} if the specified HTTP response headers contain a <code>Content-Type</code> that
     * ends with one of the specified strings.
     *
     * @param headers the HTTP response headers
     * @param contentTypeEndings the content type endings to search for
     * @return {@code true} if the specified HTTP response headers contain a <code>Content-Type</code> that
     *         ends with one of the specified strings
     */
    static boolean contentTypeEndsWith(final List<NameValuePair> headers, final String... contentTypeEndings) {
        for (final NameValuePair pair : headers) {
            final String name = pair.getName();
            if (HttpHeader.CONTENT_TYPE_LC.equalsIgnoreCase(name)) {
                String value = pair.getValue();
                final int i = value.indexOf(';');
                if (i != -1) {
                    value = value.substring(0, i);
                }
                value = value.trim().toLowerCase(Locale.ROOT);
                for (final String ending : contentTypeEndings) {
                    if (value.endsWith(ending.toLowerCase(Locale.ROOT))) {
                        return true;
                    }
                }
                return false;
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
     * {@code null}, as if no encoding had been found.</p>
     *
     * @param headers the HTTP response headers sent back with the HTML content to be sniffed
     * @param content the HTML content to be sniffed
     * @return the encoding sniffed from the specified HTML content and/or the corresponding HTTP headers,
     *         or {@code null} if the encoding could not be determined
     * @throws IOException if an IO error occurs
     *
     * @deprecated as of version 4.0.0; depending on the content use {@link #sniffEncodingFromMetaTag(InputStream)},
     * {@link #sniffEncodingFromXmlDeclaration(InputStream)}, or {@link #sniffEncodingFromCssDeclaration(InputStream) }
     * instead
     */
    @Deprecated
    public static Charset sniffHtmlEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {

        byte[] bytes = read(content, 3);
        Charset encoding = sniffEncodingFromUnicodeBom(bytes);
        if (encoding != null) {
            return encoding;
        }

        encoding = sniffEncodingFromHttpHeaders(headers);
        if (encoding != null || content == null) {
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
     * {@code null}, as if no encoding had been found.</p>
     *
     * @param headers the HTTP response headers sent back with the XML content to be sniffed
     * @param content the XML content to be sniffed
     * @return the encoding sniffed from the specified XML content and/or the corresponding HTTP headers,
     *         or {@code null} if the encoding could not be determined
     * @throws IOException if an IO error occurs
     *
     * @deprecated as of version 4.0.0; depending on the content use {@link #sniffEncodingFromMetaTag(InputStream)},
     * {@link #sniffEncodingFromXmlDeclaration(InputStream)}, or {@link #sniffEncodingFromCssDeclaration(InputStream) }
     * instead
     */
    @Deprecated
    public static Charset sniffXmlEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {

        byte[] bytes = read(content, 3);
        Charset encoding = sniffEncodingFromUnicodeBom(bytes);
        if (encoding != null) {
            return encoding;
        }

        encoding = sniffEncodingFromHttpHeaders(headers);
        if (encoding != null || content == null) {
            return encoding;
        }

        bytes = readAndPrepend(content, SIZE_OF_XML_CONTENT_SNIFFED, bytes);
        encoding = sniffEncodingFromXmlDeclaration(bytes);
        return encoding;
    }

   /**
     * @deprecated as of version 4.0.0; depending on the content use {@link #sniffEncodingFromMetaTag(InputStream)},
     * {@link #sniffEncodingFromXmlDeclaration(InputStream)}, or {@link #sniffEncodingFromCssDeclaration(InputStream) }
     * instead
    */
    @Deprecated
    private static Charset sniffCssEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {

        byte[] bytes = read(content, 3);
        Charset encoding = sniffEncodingFromUnicodeBom(bytes);
        if (encoding != null) {
            return encoding;
        }

        encoding = sniffEncodingFromHttpHeaders(headers);
        if (encoding != null || content == null) {
            return encoding;
        }

        bytes = readAndPrepend(content, SIZE_OF_CSS_CONTENT_SNIFFED, bytes);
        encoding = sniffEncodingFromCssDeclaration(bytes);
        return encoding;
    }

    /**
     * <p>Sniffs encoding settings from the specified content of unknown type by looking for <code>Content-Type</code>
     * information in the HTTP headers and <a href="http://en.wikipedia.org/wiki/Byte_Order_Mark">Byte Order Mark</a>
     * information in the content.</p>
     *
     * <p>Note that if an encoding is found but it is not supported on the current platform, this method returns
     * {@code null}, as if no encoding had been found.</p>
     *
     * @param headers the HTTP response headers sent back with the content to be sniffed
     * @param content the content to be sniffed
     * @return the encoding sniffed from the specified content and/or the corresponding HTTP headers,
     *         or {@code null} if the encoding could not be determined
     * @throws IOException if an IO error occurs
     *
     * @deprecated as of version 4.0.0; depending on the content use {@link #sniffEncodingFromMetaTag(InputStream)},
     * {@link #sniffEncodingFromXmlDeclaration(InputStream)}, or {@link #sniffEncodingFromCssDeclaration(InputStream) }
     * instead
     */
    @Deprecated
    public static Charset sniffUnknownContentTypeEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {

        final byte[] bytes = read(content, 3);
        Charset encoding = sniffEncodingFromUnicodeBom(bytes);
        if (encoding != null) {
            return encoding;
        }

        encoding = sniffEncodingFromHttpHeaders(headers);
        if (encoding != null || content == null) {
            return encoding;
        }
        return encoding;
    }

    /**
     * Attempts to sniff an encoding from the specified HTTP headers.
     *
     * @param headers the HTTP headers to examine
     * @return the encoding sniffed from the specified HTTP headers, or {@code null} if the encoding
     *         could not be determined
     *
     * @deprecated as of version 4.0.0; method will be removed without replacement
     */
    @Deprecated
    public static Charset sniffEncodingFromHttpHeaders(final List<NameValuePair> headers) {
        for (final NameValuePair pair : headers) {
            final String name = pair.getName();
            if (HttpHeader.CONTENT_TYPE_LC.equalsIgnoreCase(name)) {
                final Charset encoding = extractEncodingFromContentType(pair.getValue());
                if (encoding != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Encoding found in HTTP headers: '" + encoding + "'.");
                    }
                    return encoding;
                }
            }
        }
        return null;
    }

    /**
     * Attempts to sniff an encoding from a <a href="http://en.wikipedia.org/wiki/Byte_Order_Mark">Byte Order Mark</a>
     * in the specified byte array.
     *
     * @param bytes the bytes to check for a Byte Order Mark
     * @return the encoding sniffed from the specified bytes, or {@code null} if the encoding
     *         could not be determined
     */
    static Charset sniffEncodingFromUnicodeBom(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        Charset encoding = null;
        if (startsWith(bytes, ByteOrderMark.UTF_8)) {
            encoding = UTF_8;
        }
        else if (startsWith(bytes, ByteOrderMark.UTF_16BE)) {
            encoding = UTF_16BE;
        }
        else if (startsWith(bytes, ByteOrderMark.UTF_16LE)) {
            encoding = UTF_16LE;
        }

        if (encoding != null && LOG.isDebugEnabled()) {
            LOG.debug("Encoding found in Unicode Byte Order Mark: '" + encoding + "'.");
        }
        return encoding;
    }

    /**
     * Returns whether the specified byte array starts with the given {@link ByteOrderMark}, or not.
     * @param bytes the byte array to check
     * @param bom the {@link ByteOrderMark}
     * @return whether the specified byte array starts with the given {@link ByteOrderMark}, or not
     */
    private static boolean startsWith(final byte[] bytes, final ByteOrderMark bom) {
        final byte[] bomBytes = bom.getBytes();
        final byte[] firstBytes = Arrays.copyOfRange(bytes, 0, Math.min(bytes.length, bomBytes.length));
        return Arrays.equals(firstBytes, bomBytes);
    }

    /**
     * Attempts to sniff an encoding from an HTML <code>meta</code> tag in the specified byte array.
     *
     * @param bytes the bytes to check for an HTML <code>meta</code> tag
     * @return the encoding sniffed from the specified bytes, or {@code null} if the encoding
     *         could not be determined
     *
     * @deprecated as of version 4.0.0; method will be removed without replacement
     */
    @Deprecated
    static Charset sniffEncodingFromMetaTag(final byte[] bytes)throws IOException {
        return sniffEncodingFromMetaTag(new ByteArrayInputStream(bytes));
    }

    /**
     * Attempts to sniff an encoding from an HTML <code>meta</code> tag in the specified byte array.
     *
     * @param is the content stream to check for an HTML <code>meta</code> tag
     * @return the encoding sniffed from the specified bytes, or {@code null} if the encoding
     *         could not be determined
     * @throws IOException if an IO error occurs
     */
    public static Charset sniffEncodingFromMetaTag(final InputStream is) throws IOException {
        final byte[] bytes = read(is, SIZE_OF_HTML_CONTENT_SNIFFED);
        for (int i = 0; i < bytes.length; i++) {
            if (matches(bytes, i, COMMENT_START)) {
                i = indexOfSubArray(bytes, COMMENT_END, i);
                if (i == -1) {
                    break;
                }
                i += 2;
            }
            else if (matches(bytes, i, META_START)) {
                i += META_START.length;
                for (Attribute att = getAttribute(bytes, i); att != null; att = getAttribute(bytes, i)) {
                    i = att.getUpdatedIndex();
                    final String name = att.getName().toLowerCase(Locale.ROOT);
                    final String value = att.getValue().toLowerCase(Locale.ROOT);
                    if ("charset".equals(name) || "content".equals(name)) {
                        Charset charset = null;
                        if ("charset".equals(name)) {
                            charset = toCharset(value);
                            // https://html.spec.whatwg.org/multipage/parsing.html#prescan-a-byte-stream-to-determine-its-encoding
                            if (charset == null && "x-user-defined".equals(value)) {
                                charset = Charset.forName("windows-1252");
                            }
                        }
                        else if ("content".equals(name)) {
                            charset = extractEncodingFromContentType(value);
                            // https://html.spec.whatwg.org/multipage/parsing.html#prescan-a-byte-stream-to-determine-its-encoding
                            if (charset == null && value != null && value.contains("x-user-defined")) {
                                charset = Charset.forName("windows-1252");
                            }
                            if (charset == null) {
                                continue;
                            }
                        }
                        if (UTF_16BE == charset || UTF_16LE == charset) {
                            charset = UTF_8;
                        }
                        if (charset != null) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Encoding found in meta tag: '" + charset + "'.");
                            }
                            return charset;
                        }
                    }
                }
            }
            else if (i + 1 < bytes.length && bytes[i] == '<' && Character.isLetter(bytes[i + 1])) {
                i = skipToAnyOf(bytes, i, WHITESPACE);
                if (i == -1) {
                    break;
                }
                Attribute att = getAttribute(bytes, i);
                while (att != null) {
                    i = att.getUpdatedIndex();
                    att = getAttribute(bytes, i);
                }
            }
            else if (i + 2 < bytes.length && bytes[i] == '<' && bytes[i + 1] == '/' && Character.isLetter(bytes[i + 2])) {
                i = skipToAnyOf(bytes, i, new byte[] {0x09, 0x0A, 0x0C, 0x0D, 0x20, 0x3E});
                if (i == -1) {
                    break;
                }
                Attribute attribute = getAttribute(bytes, i);
                while (attribute != null) {
                    i = attribute.getUpdatedIndex();
                    attribute = getAttribute(bytes, i);
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
     * @param startFrom the index to start searching from
     * @return the next attribute in the specified byte array, or {@code null} if one is not available
     */
    static Attribute getAttribute(final byte[] bytes, final int startFrom) {
        if (startFrom >= bytes.length) {
            return null;
        }

        int pos = startFrom;
        while (bytes[pos] == 0x09 || bytes[pos] == 0x0A || bytes[pos] == 0x0C || bytes[pos] == 0x0D || bytes[pos] == 0x20 || bytes[pos] == 0x2F) {
            pos++;
            if (pos >= bytes.length) {
                return null;
            }
        }
        if (bytes[pos] == '>') {
            return null;
        }
        final StringBuilder name = new StringBuilder();
        final StringBuilder value = new StringBuilder();
        for ( ;; pos++) {
            if (pos >= bytes.length) {
                return new Attribute(name.toString(), value.toString(), pos);
            }
            if (bytes[pos] == '=' && name.length() != 0) {
                pos++;
                break;
            }
            if (bytes[pos] == 0x09 || bytes[pos] == 0x0A || bytes[pos] == 0x0C || bytes[pos] == 0x0D || bytes[pos] == 0x20) {
                while (bytes[pos] == 0x09 || bytes[pos] == 0x0A || bytes[pos] == 0x0C || bytes[pos] == 0x0D || bytes[pos] == 0x20) {
                    pos++;
                    if (pos >= bytes.length) {
                        return new Attribute(name.toString(), value.toString(), pos);
                    }
                }
                if (bytes[pos] != '=') {
                    return new Attribute(name.toString(), value.toString(), pos);
                }
                pos++;
                break;
            }
            if (bytes[pos] == '/' || bytes[pos] == '>') {
                return new Attribute(name.toString(), value.toString(), pos);
            }
            name.append((char) bytes[pos]);
        }
        if (pos >= bytes.length) {
            return new Attribute(name.toString(), value.toString(), pos);
        }
        while (bytes[pos] == 0x09 || bytes[pos] == 0x0A || bytes[pos] == 0x0C || bytes[pos] == 0x0D || bytes[pos] == 0x20) {
            pos++;
            if (pos >= bytes.length) {
                return new Attribute(name.toString(), value.toString(), pos);
            }
        }
        if (bytes[pos] == '"' || bytes[pos] == '\'') {
            final byte b = bytes[pos];
            for (pos++; pos < bytes.length; pos++) {
                if (bytes[pos] == b) {
                    pos++;
                    return new Attribute(name.toString(), value.toString(), pos);
                }
                else if (bytes[pos] >= 'A' && bytes[pos] <= 'Z') {
                    final byte b2 = (byte) (bytes[pos] + 0x20);
                    value.append((char) b2);
                }
                else {
                    value.append((char) bytes[pos]);
                }
            }
            return new Attribute(name.toString(), value.toString(), pos);
        }
        else if (bytes[pos] == '>') {
            return new Attribute(name.toString(), value.toString(), pos);
        }
        else if (bytes[pos] >= 'A' && bytes[pos] <= 'Z') {
            final byte b = (byte) (bytes[pos] + 0x20);
            value.append((char) b);
            pos++;
        }
        else {
            value.append((char) bytes[pos]);
            pos++;
        }
        for ( ; pos < bytes.length; pos++) {
            if (bytes[pos] == 0x09 || bytes[pos] == 0x0A || bytes[pos] == 0x0C || bytes[pos] == 0x0D || bytes[pos] == 0x20 || bytes[pos] == 0x3E) {
                return new Attribute(name.toString(), value.toString(), pos);
            }
            else if (bytes[pos] >= 'A' && bytes[pos] <= 'Z') {
                final byte b = (byte) (bytes[pos] + 0x20);
                value.append((char) b);
            }
            else {
                value.append((char) bytes[pos]);
            }
        }
        return new Attribute(name.toString(), value.toString(), pos);
    }

    /**
     * Extracts an encoding from the specified <code>Content-Type</code> value using
     * <a href="http://ietfreport.isoc.org/idref/draft-abarth-mime-sniff/">the IETF algorithm</a>; if
     * no encoding is found, this method returns {@code null}.
     *
     * @param s the <code>Content-Type</code> value to search for an encoding
     * @return the encoding found in the specified <code>Content-Type</code> value, or {@code null} if no
     *         encoding was found
     */
    public static Charset extractEncodingFromContentType(final String s) {
        if (s == null) {
            return null;
        }
        final byte[] bytes = s.getBytes(US_ASCII);
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
            final int index = ArrayUtils.indexOf(bytes, (byte) '"', i + 1);
            if (index == -1) {
                return null;
            }
            final String charsetName = new String(ArrayUtils.subarray(bytes, i + 1, index), US_ASCII);
            return toCharset(charsetName);
        }
        if (bytes[i] == '\'') {
            if (bytes.length <= i + 1) {
                return null;
            }
            final int index = ArrayUtils.indexOf(bytes, (byte) '\'', i + 1);
            if (index == -1) {
                return null;
            }
            final String charsetName = new String(ArrayUtils.subarray(bytes, i + 1, index), US_ASCII);
            return toCharset(charsetName);
        }
        int end = skipToAnyOf(bytes, i, new byte[] {0x09, 0x0A, 0x0C, 0x0D, 0x20, 0x3B});
        if (end == -1) {
            end = bytes.length;
        }
        final String charsetName = new String(ArrayUtils.subarray(bytes, i, end), US_ASCII);
        return toCharset(charsetName);
    }

    /**
     * Searches the specified XML content for an XML declaration and returns the encoding if found,
     * otherwise returns {@code null}.
     *
     * @param bytes the XML content to sniff
     * @return the encoding of the specified XML content, or {@code null} if it could not be determined
     *
     * @deprecated as of version 4.0.0; use {@link #sniffEncodingFromXmlDeclaration(InputStream)} instead
     */
    @Deprecated
    static Charset sniffEncodingFromXmlDeclaration(final byte[] bytes) throws IOException {
        return sniffEncodingFromXmlDeclaration(new ByteArrayInputStream(bytes));
    }

    /**
     * Searches the specified XML content for an XML declaration and returns the encoding if found,
     * otherwise returns {@code null}.
     *
     * @param is the content stream to check for the charset declaration
     * @return the encoding of the specified XML content, or {@code null} if it could not be determined
     * @throws IOException if an IO error occurs
     */
    public static Charset sniffEncodingFromXmlDeclaration(final InputStream is) throws IOException {
        final byte[] bytes = read(is, SIZE_OF_XML_CONTENT_SNIFFED);
        Charset encoding = null;
        if (bytes.length > 5
                && XML_DECLARATION_PREFIX[0] == bytes[0]
                && XML_DECLARATION_PREFIX[1] == bytes[1]
                && XML_DECLARATION_PREFIX[2] == bytes[2]
                && XML_DECLARATION_PREFIX[3] == bytes[3]
                && XML_DECLARATION_PREFIX[4] == bytes[4]
                && XML_DECLARATION_PREFIX[5] == bytes[5]) {
            final int index = ArrayUtils.indexOf(bytes, (byte) '?', 2);
            if (index + 1 < bytes.length && bytes[index + 1] == '>') {
                final String declaration = new String(bytes, 0, index + 2, US_ASCII);
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
                    encoding = toCharset(declaration.substring(start, end));
                }
            }
        }
        if (encoding != null && LOG.isDebugEnabled()) {
            LOG.debug("Encoding found in XML declaration: '" + encoding + "'.");
        }
        return encoding;
    }

    /**
     * Parses and returns the charset declaration at the start of a css file if any, otherwise returns {@code null}.
     * @param bytes the input bytes to sniff the encoding from
     * @return the charset declaration at the start of a css file if any, otherwise returns {@code null}.
     *
     * <p>e.g. <pre>@charset "UTF-8"</pre>
     *
     * @deprecated as of version 4.0.0; depending on the content use {@link #sniffEncodingFromMetaTag(InputStream)},
     * {@link #sniffEncodingFromXmlDeclaration(InputStream)}, or {@link #sniffEncodingFromCssDeclaration(InputStream) }
     * instead
     */
    @Deprecated
    static Charset sniffEncodingFromCssDeclaration(final byte[] bytes) throws IOException {
        return sniffEncodingFromXmlDeclaration(new ByteArrayInputStream(bytes));
    }

    /**
     * Parses and returns the charset declaration at the start of a css file if any, otherwise returns {@code null}.
     * <p>e.g. <pre>@charset "UTF-8"</pre>
     *
     * @param is the input stream to parse
     * @return the charset declaration at the start of a css file if any, otherwise returns {@code null}.
     * @throws IOException if an IO error occurs
     */
    public static Charset sniffEncodingFromCssDeclaration(final InputStream is) throws IOException {
        final byte[] bytes = read(is, SIZE_OF_CSS_CONTENT_SNIFFED);
        if (bytes.length < CSS_CHARSET_DECLARATION_PREFIX.length) {
            return null;
        }
        for (int i = 0; i < CSS_CHARSET_DECLARATION_PREFIX.length; i++) {
            if (bytes[i] != CSS_CHARSET_DECLARATION_PREFIX[i]) {
                return null;
            }
        }

        Charset encoding = null;
        final int index = ArrayUtils.indexOf(bytes, (byte) '"', CSS_CHARSET_DECLARATION_PREFIX.length);
        if (index + 1 < bytes.length && bytes[index + 1] == ';') {
            encoding = toCharset(new String(bytes, CSS_CHARSET_DECLARATION_PREFIX.length, index - CSS_CHARSET_DECLARATION_PREFIX.length, US_ASCII));
            // https://www.w3.org/TR/css-syntax-3/#input-byte-stream "Why use utf-8 when the declaration says utf-16?"
            if (encoding == UTF_16BE || encoding == UTF_16LE) {
                encoding = UTF_8;
            }
        }
        return encoding;
    }

    /**
     * Returns {@code Charset} if the specified charset name is supported on this platform.
     *
     * @param charsetName the charset name to check
     * @return {@code Charset} if the specified charset name is supported on this platform
     */
    public static Charset toCharset(final String charsetName) {
        final String nameFromLabel = translateEncodingLabel(charsetName);
        if (nameFromLabel == null) {
            return null;
        }
        try {
            return Charset.forName(nameFromLabel);
        }
        catch (final IllegalCharsetNameException | UnsupportedCharsetException e) {
            return null;
        }
    }

    /**
     * Returns {@code true} if the byte in the specified byte array at the specified index matches one of the
     * specified byte array patterns.
     *
     * @param bytes the byte array to search in
     * @param i the index at which to search
     * @param sought the byte array patterns to search for
     * @return {@code true} if the byte in the specified byte array at the specified index matches one of the
     *         specified byte array patterns
     */
    static boolean matches(final byte[] bytes, final int i, final byte[][] sought) {
        if (i + sought.length > bytes.length) {
            return false;
        }
        for (int x = 0; x < sought.length; x++) {
            final byte[] possibilities = sought[x];
            boolean match = false;
            for (final byte possibility : possibilities) {
                if (bytes[i + x] == possibility) {
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
     * starting at the specified index. This method returns <code>-1</code> if none of the targets are found.
     *
     * @param bytes the array to search through
     * @param startFrom the index to start looking at
     * @param targets the targets to search for
     * @return the index of the first occurrence of the specified targets within the specified array
     */
    static int skipToAnyOf(final byte[] bytes, final int startFrom, final byte[] targets) {
        int i = startFrom;
        for ( ; i < bytes.length; i++) {
            if (ArrayUtils.contains(targets, bytes[i])) {
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
     * specified index. This method returns <code>-1</code> if the specified sub-array cannot be found.
     *
     * @param array the array to traverse for looking for the sub-array
     * @param subarray the sub-array to find
     * @param startIndex the start index to traverse forwards from
     * @return the index of the sub-array within the array
     */
    static int indexOfSubArray(final byte[] array, final byte[] subarray, final int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            boolean found = true;
            if (i + subarray.length > array.length) {
                break;
            }
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
     * Attempts to read <code>size</code> bytes from the specified input stream. Note that this method is not guaranteed
     * to be able to read <code>size</code> bytes; however, the returned byte array will always be the exact length of the
     * number of bytes read.
     *
     * @param content the input stream to read from
     * @param size the number of bytes to try to read
     * @return the bytes read from the specified input stream
     * @throws IOException if an IO error occurs
     */
    static byte[] read(final InputStream content, final int size) throws IOException {
        byte[] bytes = new byte[size];
        // using IOUtils guarantees that it will read as many bytes as possible before giving up;
        // this may not always be the case for subclasses of InputStream} - eg. GZIPInputStream
        final int count = IOUtils.read(content, bytes);
        if (count < size) {
            final byte[] smaller = new byte[count];
            System.arraycopy(bytes, 0, smaller, 0, count);
            bytes = smaller;
        }
        return bytes;
    }

    /**
     * Attempts to read <code>size</code> bytes from the specified input stream and then prepends the specified prefix to
     * the bytes read, returning the resultant byte array. Note that this method is not guaranteed to be able to read
     * <code>size</code> bytes; however, the returned byte array will always be the exact length of the number of bytes
     * read plus the length of the prefix array.
     *
     * @param content the input stream to read from
     * @param size the number of bytes to try to read
     * @param prefix the byte array to prepend to the bytes read from the specified input stream
     * @return the bytes read from the specified input stream, prefixed by the specified prefix
     * @throws IOException if an IO error occurs
     */
    static byte[] readAndPrepend(final InputStream content, final int size, final byte[] prefix) throws IOException {
        final int prefixLength = prefix.length;
        final byte[] joined = new byte[prefixLength + size];

        // using IOUtils guarantees that it will read as many bytes as possible before giving up;
        // this may not always be the case for subclasses of InputStream} - eg. GZIPInputStream
        final int count = IOUtils.read(content, joined, prefixLength, joined.length - prefixLength);
        if (count < size) {
            final byte[] smaller = new byte[prefixLength + count];
            System.arraycopy(prefix, 0, smaller, 0, prefix.length);
            System.arraycopy(joined, prefixLength, smaller, prefixLength, count);
            return smaller;
        }

        System.arraycopy(prefix, 0, joined, 0, prefix.length);
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

    /**
     * Translates the given encoding label into a normalized form
     * according to <a href="http://encoding.spec.whatwg.org/#encodings">Reference</a>.
     * @param encodingLabel the label to translate
     * @return the normalized encoding name or null if not found
     *
     * @deprecated as of version 4.0.0; method will be removed without replacement
     */
    @Deprecated
    public static String translateEncodingLabel(final Charset encodingLabel) {
        return translateEncodingLabel(encodingLabel.name());
    }

    /**
     * Translates the given encoding label into a normalized form
     * according to <a href="http://encoding.spec.whatwg.org/#encodings">Reference</a>.
     * @param encodingLabel the label to translate
     * @return the normalized encoding name or null if not found
     */
    public static String translateEncodingLabel(final String encodingLabel) {
        if (StringUtils.isEmpty(encodingLabel)) {
            return null;
        }

        final String encLC = encodingLabel.toLowerCase(Locale.ROOT);
        final String enc = StandardEncodingTranslator.INSTANCE.encodingNameFromLabel(encodingLabel);
        if (encLC.equals(enc)) {
            return encodingLabel;
        }
        return enc;
    }
}
