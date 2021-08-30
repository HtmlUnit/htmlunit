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

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_16LE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.HttpHeader;

/**
 * Sniffs encoding settings from HTML, XML or other content. The HTML encoding sniffing algorithm is based on the
 * <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/parsing.html#determining-the-character-encoding">HTML5
 * encoding sniffing algorithm</a>.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
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

    /** Sequence(s) of bytes indicating the beginning of a <tt>meta</tt> HTML tag. */
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

    /** <a href="http://encoding.spec.whatwg.org/#encodings">Reference</a> */
    private static final Map<String, String> ENCODING_FROM_LABEL;
    static {
        ENCODING_FROM_LABEL = new HashMap<>();

        // The Encoding
        // ------------
        ENCODING_FROM_LABEL.put("unicode-1-1-utf-8", "utf-8");
        ENCODING_FROM_LABEL.put("utf-8", "utf-8");
        ENCODING_FROM_LABEL.put("utf8", "utf-8");

        // Legacy single-byte encodings
        // ----------------------------

        // ibm866
        ENCODING_FROM_LABEL.put("866", "ibm866");
        ENCODING_FROM_LABEL.put("cp866", "ibm866");
        ENCODING_FROM_LABEL.put("csibm866", "ibm866");
        ENCODING_FROM_LABEL.put("ibm866", "ibm866");

        // iso-8859-2
        ENCODING_FROM_LABEL.put("csisolatin2", "iso-8859-2");
        ENCODING_FROM_LABEL.put("iso-8859-2", "iso-8859-2");
        ENCODING_FROM_LABEL.put("iso-ir-101", "iso-8859-2");
        ENCODING_FROM_LABEL.put("iso8859-2", "iso-8859-2");
        ENCODING_FROM_LABEL.put("iso88592", "iso-8859-2");
        ENCODING_FROM_LABEL.put("iso_8859-2", "iso-8859-2");
        ENCODING_FROM_LABEL.put("iso_8859-2:1987", "iso-8859-2");
        ENCODING_FROM_LABEL.put("l2", "iso-8859-2");
        ENCODING_FROM_LABEL.put("latin2", "iso-8859-2");

        // iso-8859-3
        ENCODING_FROM_LABEL.put("csisolatin2", "iso-8859-3");
        ENCODING_FROM_LABEL.put("csisolatin3", "iso-8859-3");
        ENCODING_FROM_LABEL.put("iso-8859-3", "iso-8859-3");
        ENCODING_FROM_LABEL.put("iso-ir-109", "iso-8859-3");
        ENCODING_FROM_LABEL.put("iso8859-3", "iso-8859-3");
        ENCODING_FROM_LABEL.put("iso88593", "iso-8859-3");
        ENCODING_FROM_LABEL.put("iso_8859-3", "iso-8859-3");
        ENCODING_FROM_LABEL.put("iso_8859-3:1988", "iso-8859-3");
        ENCODING_FROM_LABEL.put("l3", "iso-8859-3");
        ENCODING_FROM_LABEL.put("latin3", "iso-8859-3");

        // iso-8859-4
        ENCODING_FROM_LABEL.put("csisolatin4", "iso-8859-4");
        ENCODING_FROM_LABEL.put("iso-8859-4", "iso-8859-4");
        ENCODING_FROM_LABEL.put("iso-ir-110", "iso-8859-4");
        ENCODING_FROM_LABEL.put("iso8859-4", "iso-8859-4");
        ENCODING_FROM_LABEL.put("iso88594", "iso-8859-4");
        ENCODING_FROM_LABEL.put("iso_8859-4", "iso-8859-4");
        ENCODING_FROM_LABEL.put("iso_8859-4:1988", "iso-8859-4");
        ENCODING_FROM_LABEL.put("l4", "iso-8859-4");
        ENCODING_FROM_LABEL.put("latin4", "iso-8859-4");

        // iso-8859-5
        ENCODING_FROM_LABEL.put("csisolatincyrillic", "iso-8859-5");
        ENCODING_FROM_LABEL.put("csisolatincyrillic", "iso-8859-5");
        ENCODING_FROM_LABEL.put("cyrillic", "iso-8859-5");
        ENCODING_FROM_LABEL.put("iso-8859-5", "iso-8859-5");
        ENCODING_FROM_LABEL.put("iso-ir-144", "iso-8859-5");
        ENCODING_FROM_LABEL.put("iso8859-5", "iso-8859-5");
        ENCODING_FROM_LABEL.put("iso88595", "iso-8859-5");
        ENCODING_FROM_LABEL.put("iso_8859-5", "iso-8859-5");
        ENCODING_FROM_LABEL.put("iso_8859-5:1988", "iso-8859-5");

        // iso-8859-6
        ENCODING_FROM_LABEL.put("arabic", "iso-8859-6");
        ENCODING_FROM_LABEL.put("asmo-708", "iso-8859-6");
        ENCODING_FROM_LABEL.put("csiso88596e", "iso-8859-6");
        ENCODING_FROM_LABEL.put("csiso88596i", "iso-8859-6");
        ENCODING_FROM_LABEL.put("csisolatinarabic", "iso-8859-6");
        ENCODING_FROM_LABEL.put("ecma-114", "iso-8859-6");
        ENCODING_FROM_LABEL.put("iso-8859-6", "iso-8859-6");
        ENCODING_FROM_LABEL.put("iso-8859-6-e", "iso-8859-6");
        ENCODING_FROM_LABEL.put("iso-8859-6-i", "iso-8859-6");
        ENCODING_FROM_LABEL.put("iso-ir-127", "iso-8859-6");
        ENCODING_FROM_LABEL.put("iso8859-6", "iso-8859-6");
        ENCODING_FROM_LABEL.put("iso88596", "iso-8859-6");
        ENCODING_FROM_LABEL.put("iso_8859-6", "iso-8859-6");
        ENCODING_FROM_LABEL.put("iso_8859-6:1987", "iso-8859-6");

        // iso-8859-7
        ENCODING_FROM_LABEL.put("csisolatingreek", "iso-8859-7");
        ENCODING_FROM_LABEL.put("ecma-118", "iso-8859-7");
        ENCODING_FROM_LABEL.put("elot_928", "iso-8859-7");
        ENCODING_FROM_LABEL.put("greek", "iso-8859-7");
        ENCODING_FROM_LABEL.put("greek8", "iso-8859-7");
        ENCODING_FROM_LABEL.put("iso-8859-7", "iso-8859-7");
        ENCODING_FROM_LABEL.put("iso-ir-126", "iso-8859-7");
        ENCODING_FROM_LABEL.put("iso8859-7", "iso-8859-7");
        ENCODING_FROM_LABEL.put("iso88597", "iso-8859-7");
        ENCODING_FROM_LABEL.put("iso_8859-7", "iso-8859-7");
        ENCODING_FROM_LABEL.put("iso_8859-7:1987", "iso-8859-7");
        ENCODING_FROM_LABEL.put("sun_eu_greek", "iso-8859-7");

        // iso-8859-8
        ENCODING_FROM_LABEL.put("csisolatingreek", "iso-8859-8");
        ENCODING_FROM_LABEL.put("csiso88598e", "iso-8859-8");
        ENCODING_FROM_LABEL.put("csisolatinhebrew", "iso-8859-8");
        ENCODING_FROM_LABEL.put("hebrew", "iso-8859-8");
        ENCODING_FROM_LABEL.put("iso-8859-8", "iso-8859-8");
        ENCODING_FROM_LABEL.put("iso-8859-8-e", "iso-8859-8");
        ENCODING_FROM_LABEL.put("iso-ir-138", "iso-8859-8");
        ENCODING_FROM_LABEL.put("iso8859-8", "iso-8859-8");
        ENCODING_FROM_LABEL.put("iso88598", "iso-8859-8");
        ENCODING_FROM_LABEL.put("iso_8859-8", "iso-8859-8");
        ENCODING_FROM_LABEL.put("iso_8859-8:1988", "iso-8859-8");
        ENCODING_FROM_LABEL.put("visual", "iso-8859-8");

        // iso-8859-8-i
        ENCODING_FROM_LABEL.put("csiso88598i", "iso-8859-8-i");
        ENCODING_FROM_LABEL.put("iso-8859-8-i", "iso-8859-8-i");
        ENCODING_FROM_LABEL.put("logical", "iso-8859-8-i");

        // iso-8859-10
        ENCODING_FROM_LABEL.put("csisolatin6", "iso-8859-10");
        ENCODING_FROM_LABEL.put("iso-8859-10", "iso-8859-10");
        ENCODING_FROM_LABEL.put("iso-ir-157", "iso-8859-10");
        ENCODING_FROM_LABEL.put("iso8859-10", "iso-8859-10");
        ENCODING_FROM_LABEL.put("iso885910", "iso-8859-10");
        ENCODING_FROM_LABEL.put("l6", "iso-8859-10");
        ENCODING_FROM_LABEL.put("latin6", "iso-8859-10");

        // iso-8859-13
        ENCODING_FROM_LABEL.put("iso-8859-13", "iso-8859-13");
        ENCODING_FROM_LABEL.put("iso8859-13", "iso-8859-13");
        ENCODING_FROM_LABEL.put("iso885913", "iso-8859-13");

        // iso-8859-14
        ENCODING_FROM_LABEL.put("iso-8859-14", "iso-8859-14");
        ENCODING_FROM_LABEL.put("iso8859-14", "iso-8859-14");
        ENCODING_FROM_LABEL.put("iso885914", "iso-8859-14");

        // iso-8859-15
        ENCODING_FROM_LABEL.put("csisolatin9", "iso-8859-15");
        ENCODING_FROM_LABEL.put("iso-8859-15", "iso-8859-15");
        ENCODING_FROM_LABEL.put("iso8859-15", "iso-8859-15");
        ENCODING_FROM_LABEL.put("iso885915", "iso-8859-15");
        ENCODING_FROM_LABEL.put("iso_8859-15", "iso-8859-15");
        ENCODING_FROM_LABEL.put("l9", "iso-8859-15");

        // iso-8859-16
        ENCODING_FROM_LABEL.put("iso-8859-16", "iso-8859-16");

        // koi8-r
        ENCODING_FROM_LABEL.put("cskoi8r", "koi8-r");
        ENCODING_FROM_LABEL.put("koi", "koi8-r");
        ENCODING_FROM_LABEL.put("koi8", "koi8-r");
        ENCODING_FROM_LABEL.put("koi8-r", "koi8-r");
        ENCODING_FROM_LABEL.put("koi8_r", "koi8-r");

        // koi8-u
        ENCODING_FROM_LABEL.put("koi8-u", "koi8-u");

        // macintosh
        ENCODING_FROM_LABEL.put("csmacintosh", "macintosh");
        ENCODING_FROM_LABEL.put("mac", "macintosh");
        ENCODING_FROM_LABEL.put("macintosh", "macintosh");
        ENCODING_FROM_LABEL.put("x-mac-roman", "macintosh");

        // windows-874
        ENCODING_FROM_LABEL.put("dos-874", "windows-874");
        ENCODING_FROM_LABEL.put("iso-8859-11", "windows-874");
        ENCODING_FROM_LABEL.put("iso8859-11", "windows-874");
        ENCODING_FROM_LABEL.put("iso885911", "windows-874");
        ENCODING_FROM_LABEL.put("tis-620", "windows-874");
        ENCODING_FROM_LABEL.put("windows-874", "windows-874");

        // windows-1250
        ENCODING_FROM_LABEL.put("cp1250", "windows-1250");
        ENCODING_FROM_LABEL.put("windows-1250", "windows-1250");
        ENCODING_FROM_LABEL.put("x-cp1250", "windows-1250");

        // windows-1251
        ENCODING_FROM_LABEL.put("cp1251", "windows-1251");
        ENCODING_FROM_LABEL.put("windows-1251", "windows-1251");
        ENCODING_FROM_LABEL.put("x-cp1251", "windows-1251");

        // windows-1252
        ENCODING_FROM_LABEL.put("ansi_x3.4-1968", "windows-1252");
        ENCODING_FROM_LABEL.put("ascii", "windows-1252");
        ENCODING_FROM_LABEL.put("cp1252", "windows-1252");
        ENCODING_FROM_LABEL.put("cp819", "windows-1252");
        ENCODING_FROM_LABEL.put("csisolatin1", "windows-1252");
        ENCODING_FROM_LABEL.put("ibm819", "windows-1252");
        ENCODING_FROM_LABEL.put("iso-8859-1", "windows-1252");
        ENCODING_FROM_LABEL.put("iso-ir-100", "windows-1252");
        ENCODING_FROM_LABEL.put("iso8859-1", "windows-1252");
        ENCODING_FROM_LABEL.put("iso88591", "windows-1252");
        ENCODING_FROM_LABEL.put("iso_8859-1", "windows-1252");
        ENCODING_FROM_LABEL.put("iso_8859-1:1987", "windows-1252");
        ENCODING_FROM_LABEL.put("l1", "windows-1252");
        ENCODING_FROM_LABEL.put("latin1", "windows-1252");
        ENCODING_FROM_LABEL.put("us-ascii", "windows-1252");
        ENCODING_FROM_LABEL.put("windows-1252", "windows-1252");
        ENCODING_FROM_LABEL.put("x-cp1252", "windows-1252");

        // windows-1253
        ENCODING_FROM_LABEL.put("cp1253", "windows-1253");
        ENCODING_FROM_LABEL.put("windows-1253", "windows-1253");
        ENCODING_FROM_LABEL.put("x-cp1253", "windows-1253");

        // windows-1254
        ENCODING_FROM_LABEL.put("cp1254", "windows-1254");
        ENCODING_FROM_LABEL.put("csisolatin5", "windows-1254");
        ENCODING_FROM_LABEL.put("iso-8859-9", "windows-1254");
        ENCODING_FROM_LABEL.put("iso-ir-148", "windows-1254");
        ENCODING_FROM_LABEL.put("iso8859-9", "windows-1254");
        ENCODING_FROM_LABEL.put("iso88599", "windows-1254");
        ENCODING_FROM_LABEL.put("iso_8859-9", "windows-1254");
        ENCODING_FROM_LABEL.put("iso_8859-9:1989", "windows-1254");
        ENCODING_FROM_LABEL.put("l5", "windows-1254");
        ENCODING_FROM_LABEL.put("latin5", "windows-1254");
        ENCODING_FROM_LABEL.put("windows-1254", "windows-1254");
        ENCODING_FROM_LABEL.put("x-cp1254", "windows-1254");

        // windows-1255
        ENCODING_FROM_LABEL.put("cp1255", "windows-1255");
        ENCODING_FROM_LABEL.put("windows-1255", "windows-1255");
        ENCODING_FROM_LABEL.put("x-cp1255", "windows-1255");

        // windows-1256
        ENCODING_FROM_LABEL.put("cp1256", "windows-1256");
        ENCODING_FROM_LABEL.put("windows-1256", "windows-1256");
        ENCODING_FROM_LABEL.put("x-cp1256", "windows-1256");

        // windows-1257
        ENCODING_FROM_LABEL.put("cp1257", "windows-1257");
        ENCODING_FROM_LABEL.put("windows-1257", "windows-1257");
        ENCODING_FROM_LABEL.put("x-cp1257", "windows-1257");

        // windows-1258
        ENCODING_FROM_LABEL.put("cp1258", "windows-1258");
        ENCODING_FROM_LABEL.put("windows-1258", "windows-1258");
        ENCODING_FROM_LABEL.put("x-cp1258", "windows-1258");

        // x-mac-cyrillic
        ENCODING_FROM_LABEL.put("x-mac-cyrillic", "x-mac-cyrillic");
        ENCODING_FROM_LABEL.put("x-mac-ukrainian", "x-mac-cyrillic");

        // Legacy multi-byte Chinese (simplified) encodings
        // ------------------------------------------------

        // gb18030
        ENCODING_FROM_LABEL.put("chinese", "gb18030");
        ENCODING_FROM_LABEL.put("csgb2312", "gb18030");
        ENCODING_FROM_LABEL.put("csiso58gb231280", "gb18030");
        ENCODING_FROM_LABEL.put("gb18030", "gb18030");
        ENCODING_FROM_LABEL.put("gb2312", "gb18030");
        ENCODING_FROM_LABEL.put("gb_2312", "gb18030");
        ENCODING_FROM_LABEL.put("gb_2312-80", "gb18030");
        ENCODING_FROM_LABEL.put("gbk", "gb18030");
        ENCODING_FROM_LABEL.put("iso-ir-58", "gb18030");
        ENCODING_FROM_LABEL.put("x-gbk", "gb18030");

        // hz-gb-2312
        ENCODING_FROM_LABEL.put("hz-gb-2312", "hz-gb-2312");

        // Legacy multi-byte Chinese (traditional) encodings
        // ------------------------------------------------

        // big5
        ENCODING_FROM_LABEL.put("big5", "big5");
        ENCODING_FROM_LABEL.put("big5-hkscs", "big5");
        ENCODING_FROM_LABEL.put("cn-big5", "big5");
        ENCODING_FROM_LABEL.put("csbig5", "big5");
        ENCODING_FROM_LABEL.put("x-x-big5", "big5");

        // Legacy multi-byte Japanese encodings
        // ------------------------------------

        // euc-jp
        ENCODING_FROM_LABEL.put("cseucpkdfmtjapanese", "euc-jp");
        ENCODING_FROM_LABEL.put("euc-jp", "euc-jp");
        ENCODING_FROM_LABEL.put("x-euc-jp", "euc-jp");

        // iso-2022-jp
        ENCODING_FROM_LABEL.put("csiso2022jp", "iso-2022-jp");
        ENCODING_FROM_LABEL.put("iso-2022-jp", "iso-2022-jp");

        // iso-2022-jp
        ENCODING_FROM_LABEL.put("csshiftjis", "shift_jis");
        ENCODING_FROM_LABEL.put("ms_kanji", "shift_jis");
        ENCODING_FROM_LABEL.put("shift-jis", "shift_jis");
        ENCODING_FROM_LABEL.put("shift_jis", "shift_jis");
        ENCODING_FROM_LABEL.put("sjis", "shift_jis");
        ENCODING_FROM_LABEL.put("windows-31j", "shift_jis");
        ENCODING_FROM_LABEL.put("x-sjis", "shift_jis");

        // Legacy multi-byte Korean encodings
        // ------------------------------------

        // euc-kr
        ENCODING_FROM_LABEL.put("cseuckr", "euc-kr");
        ENCODING_FROM_LABEL.put("csksc56011987", "euc-kr");
        ENCODING_FROM_LABEL.put("euc-kr", "euc-kr");
        ENCODING_FROM_LABEL.put("iso-ir-149", "euc-kr");
        ENCODING_FROM_LABEL.put("korean", "euc-kr");
        ENCODING_FROM_LABEL.put("ks_c_5601-1987", "euc-kr");
        ENCODING_FROM_LABEL.put("ks_c_5601-1989", "euc-kr");
        ENCODING_FROM_LABEL.put("ksc5601", "euc-kr");
        ENCODING_FROM_LABEL.put("ksc_5601", "euc-kr");
        ENCODING_FROM_LABEL.put("windows-949", "euc-kr");

        // Legacy miscellaneous encodings
        // ------------------------------------

        // replacement
        ENCODING_FROM_LABEL.put("csiso2022kr", "replacement");
        ENCODING_FROM_LABEL.put("iso-2022-cn", "replacement");
        ENCODING_FROM_LABEL.put("iso-2022-cn-ext", "replacement");
        ENCODING_FROM_LABEL.put("iso-2022-kr", "replacement");

        // utf-16be
        ENCODING_FROM_LABEL.put("utf-16be", "utf-16be");

        // utf-16le
        ENCODING_FROM_LABEL.put("utf-16", "utf-16le");
        ENCODING_FROM_LABEL.put("utf-16le", "utf-16le");

        // x-user-defined
        ENCODING_FROM_LABEL.put("x-user-defined", "x-user-defined");
    }

    private static final byte[] XML_DECLARATION_PREFIX = "<?xml ".getBytes(US_ASCII);

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
     * {@code null}, as if no encoding had been found.</p>
     *
     * @param headers the HTTP response headers sent back with the content to be sniffed
     * @param content the content to be sniffed
     * @return the encoding sniffed from the specified content and/or the corresponding HTTP headers,
     *         or {@code null} if the encoding could not be determined
     * @throws IOException if an IO error occurs
     */
    public static Charset sniffEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {
        final Charset charset;
        if (isHtml(headers)) {
            charset = sniffHtmlEncoding(headers, content);
        }
        else if (isXml(headers)) {
            charset = sniffXmlEncoding(headers, content);
        }
        else {
            charset = sniffUnknownContentTypeEncoding(headers, content);
        }

        // this is was browsers do
        if (charset != null && "GB2312".equals(charset.name())) {
            return Charset.forName("GBK");
        }
        return charset;
    }

    /**
     * Returns {@code true} if the specified HTTP response headers indicate an HTML response.
     *
     * @param headers the HTTP response headers
     * @return {@code true} if the specified HTTP response headers indicate an HTML response
     */
    static boolean isHtml(final List<NameValuePair> headers) {
        return contentTypeEndsWith(headers, MimeType.TEXT_HTML);
    }

    /**
     * Returns {@code true} if the specified HTTP response headers indicate an XML response.
     *
     * @param headers the HTTP response headers
     * @return {@code true} if the specified HTTP response headers indicate an XML response
     */
    static boolean isXml(final List<NameValuePair> headers) {
        return contentTypeEndsWith(headers, MimeType.TEXT_XML, "application/xml", "text/vnd.wap.wml", "+xml");
    }

    /**
     * Returns {@code true} if the specified HTTP response headers contain a <tt>Content-Type</tt> that
     * ends with one of the specified strings.
     *
     * @param headers the HTTP response headers
     * @param contentTypeEndings the content type endings to search for
     * @return {@code true} if the specified HTTP response headers contain a <tt>Content-Type</tt> that
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
     */
    public static Charset sniffHtmlEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {

        Charset encoding = sniffEncodingFromHttpHeaders(headers);
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
     * {@code null}, as if no encoding had been found.</p>
     *
     * @param headers the HTTP response headers sent back with the XML content to be sniffed
     * @param content the XML content to be sniffed
     * @return the encoding sniffed from the specified XML content and/or the corresponding HTTP headers,
     *         or {@code null} if the encoding could not be determined
     * @throws IOException if an IO error occurs
     */
    public static Charset sniffXmlEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {

        Charset encoding = sniffEncodingFromHttpHeaders(headers);
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
     * {@code null}, as if no encoding had been found.</p>
     *
     * @param headers the HTTP response headers sent back with the content to be sniffed
     * @param content the content to be sniffed
     * @return the encoding sniffed from the specified content and/or the corresponding HTTP headers,
     *         or {@code null} if the encoding could not be determined
     * @throws IOException if an IO error occurs
     */
    public static Charset sniffUnknownContentTypeEncoding(final List<NameValuePair> headers, final InputStream content)
        throws IOException {

        Charset encoding = sniffEncodingFromHttpHeaders(headers);
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
     * @return the encoding sniffed from the specified HTTP headers, or {@code null} if the encoding
     *         could not be determined
     */
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
     * Attempts to sniff an encoding from an HTML <tt>meta</tt> tag in the specified byte array.
     *
     * @param bytes the bytes to check for an HTML <tt>meta</tt> tag
     * @return the encoding sniffed from the specified bytes, or {@code null} if the encoding
     *         could not be determined
     */
    static Charset sniffEncodingFromMetaTag(final byte[] bytes) {
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
                    final String name = att.getName();
                    final String value = att.getValue();
                    if ("charset".equals(name) || "content".equals(name)) {
                        Charset charset = null;
                        if ("charset".equals(name)) {
                            charset = toCharset(value);
                        }
                        else if ("content".equals(name)) {
                            charset = extractEncodingFromContentType(value);
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
     * @param i the index to start searching from
     * @return the next attribute in the specified byte array, or {@code null} if one is not available
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
        final StringBuilder name = new StringBuilder();
        final StringBuilder value = new StringBuilder();
        for ( ;; i++) {
            if (i >= bytes.length) {
                return new Attribute(name.toString(), value.toString(), i);
            }
            if (bytes[i] == '=' && name.length() != 0) {
                i++;
                break;
            }
            if (bytes[i] == 0x09 || bytes[i] == 0x0A || bytes[i] == 0x0C || bytes[i] == 0x0D || bytes[i] == 0x20) {
                while (bytes[i] == 0x09 || bytes[i] == 0x0A || bytes[i] == 0x0C || bytes[i] == 0x0D || bytes[i] == 0x20) {
                    i++;
                    if (i >= bytes.length) {
                        return new Attribute(name.toString(), value.toString(), i);
                    }
                }
                if (bytes[i] != '=') {
                    return new Attribute(name.toString(), value.toString(), i);
                }
                i++;
                break;
            }
            if (bytes[i] == '/' || bytes[i] == '>') {
                return new Attribute(name.toString(), value.toString(), i);
            }
            name.append((char) bytes[i]);
        }
        if (i >= bytes.length) {
            return new Attribute(name.toString(), value.toString(), i);
        }
        while (bytes[i] == 0x09 || bytes[i] == 0x0A || bytes[i] == 0x0C || bytes[i] == 0x0D || bytes[i] == 0x20) {
            i++;
            if (i >= bytes.length) {
                return new Attribute(name.toString(), value.toString(), i);
            }
        }
        if (bytes[i] == '"' || bytes[i] == '\'') {
            final byte b = bytes[i];
            for (i++; i < bytes.length; i++) {
                if (bytes[i] == b) {
                    i++;
                    return new Attribute(name.toString(), value.toString(), i);
                }
                else if (bytes[i] >= 'A' && bytes[i] <= 'Z') {
                    final byte b2 = (byte) (bytes[i] + 0x20);
                    value.append((char) b2);
                }
                else {
                    value.append((char) bytes[i]);
                }
            }
            return new Attribute(name.toString(), value.toString(), i);
        }
        else if (bytes[i] == '>') {
            return new Attribute(name.toString(), value.toString(), i);
        }
        else if (bytes[i] >= 'A' && bytes[i] <= 'Z') {
            final byte b = (byte) (bytes[i] + 0x20);
            value.append((char) b);
            i++;
        }
        else {
            value.append((char) bytes[i]);
            i++;
        }
        for ( ; i < bytes.length; i++) {
            if (bytes[i] == 0x09 || bytes[i] == 0x0A || bytes[i] == 0x0C || bytes[i] == 0x0D || bytes[i] == 0x20 || bytes[i] == 0x3E) {
                return new Attribute(name.toString(), value.toString(), i);
            }
            else if (bytes[i] >= 'A' && bytes[i] <= 'Z') {
                final byte b = (byte) (bytes[i] + 0x20);
                value.append((char) b);
            }
            else {
                value.append((char) bytes[i]);
            }
        }
        return new Attribute(name.toString(), value.toString(), i);
    }

    /**
     * Extracts an encoding from the specified <tt>Content-Type</tt> value using
     * <a href="http://ietfreport.isoc.org/idref/draft-abarth-mime-sniff/">the IETF algorithm</a>; if
     * no encoding is found, this method returns {@code null}.
     *
     * @param s the <tt>Content-Type</tt> value to search for an encoding
     * @return the encoding found in the specified <tt>Content-Type</tt> value, or {@code null} if no
     *         encoding was found
     */
    static Charset extractEncodingFromContentType(final String s) {
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
     */
    static Charset sniffEncodingFromXmlDeclaration(final byte[] bytes) {
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
     * Returns {@code Charset} if the specified charset name is supported on this platform.
     *
     * @param charsetName the charset name to check
     * @return {@code Charset} if the specified charset name is supported on this platform
     */
    public static Charset toCharset(final String charsetName) {
        if (StringUtils.isEmpty(charsetName)) {
            return null;
        }
        try {
            return Charset.forName(charsetName);
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
     * starting at the specified index. This method returns <tt>-1</tt> if none of the targets are found.
     *
     * @param bytes the array to search through
     * @param i the index to start looking at
     * @param targets the targets to search for
     * @return the index of the first occurrence of any of the specified targets within the specified array
     */
    static int skipToAnyOf(final byte[] bytes, int i, final byte[] targets) {
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

    /**
     * Translates the given encoding label into a normalized form
     * according to <a href="http://encoding.spec.whatwg.org/#encodings">Reference</a>.
     * @param encodingLabel the label to translate
     * @return the normalized encoding name or null if not found
     */
    public static String translateEncodingLabel(final Charset encodingLabel) {
        if (null == encodingLabel) {
            return null;
        }
        final String encLC = encodingLabel.name().toLowerCase(Locale.ROOT);
        final String enc = ENCODING_FROM_LABEL.get(encLC);
        if (encLC.equals(enc)) {
            return encodingLabel.name();
        }
        return enc;
    }
}
