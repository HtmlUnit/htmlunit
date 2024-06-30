/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.http;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.htmlunit.util.NameValuePair;

/**
 * Http related utils.
 *
 * @author Ronald Brill
 */
public final class HttpUtils {

    /**
     * Safe characters for x-www-form-urlencoded data;
     * i.e. alphanumeric plus {@code "-", "_", ".", "*"}
     */
    private static final BitSet URLENCODER   = new BitSet(256);

    static {
        for (int i = 'a'; i <= 'z'; i++) {
            URLENCODER.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            URLENCODER.set(i);
        }

        for (int i = '0'; i <= '9'; i++) {
            URLENCODER.set(i);
        }
        URLENCODER.set('_');
        URLENCODER.set('-');
        URLENCODER.set('.');
        URLENCODER.set('*');
    }

    /**
     * Date format pattern used to parse HTTP date headers in RFC 1123 format.
     */
    private static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

    /** RFC 1123 date formatter. */
    private static final DateTimeFormatter FORMATTER_RFC1123 = new DateTimeFormatterBuilder()
            .parseLenient()
            .parseCaseInsensitive()
            .appendPattern(PATTERN_RFC1123)
            .toFormatter(Locale.ENGLISH);

    /**
     * Date format pattern used to parse HTTP date headers in RFC 1036 format.
     */
    public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";

    /** RFC 1036 date formatter. */
    private static final DateTimeFormatter FORMATTER_RFC1036 = new DateTimeFormatterBuilder()
            .parseLenient()
            .parseCaseInsensitive()
            .appendPattern(PATTERN_RFC1036)
            .toFormatter(Locale.ENGLISH);

    /**
     * Date format pattern used to parse HTTP date headers in ANSI C
     * {@code asctime()} format.
     */
    private static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";

    /** ASCII time date formatter. */
    private static final DateTimeFormatter FORMATTER_ASCTIME = new DateTimeFormatterBuilder()
            .parseLenient()
            .parseCaseInsensitive()
            .appendPattern(PATTERN_ASCTIME)
            .toFormatter(Locale.ENGLISH);

    /**
     * Standard date formatters: {@link #FORMATTER_RFC1123}, {@link #FORMATTER_RFC1036}, {@link #FORMATTER_ASCTIME}.
     */
    private static final DateTimeFormatter[] STANDARD_PATTERNS = new DateTimeFormatter[] {
        FORMATTER_RFC1123,
        FORMATTER_RFC1036,
        FORMATTER_ASCTIME
    };

    private static final ZoneId GMT_ID = ZoneId.of("GMT");

    /**
     * Parses a date value.  The formats used for parsing the date value are retrieved from
     * the default http params.
     *
     * @param dateValue the date value to parse
     *
     * @return the parsed date or null if input could not be parsed
     */
    public static Date parseDate(final String dateValue) {
        if (dateValue == null) {
            return null;
        }

        String v = dateValue;
        // trim single quotes around date if present
        if (v.length() > 1 && v.startsWith("'") && v.endsWith("'")) {
            v = v.substring(1, v.length() - 1);
        }

        for (final DateTimeFormatter dateFormatter : STANDARD_PATTERNS) {
            try {
                return new Date(Instant.from(dateFormatter.parse(v)).toEpochMilli());
            }
            catch (final DateTimeParseException ignore) {
                // ignore
            }
        }
        return null;
    }

    /**
     * Formats the given date according to the RFC 1123 pattern.
     *
     * @param date The date to format.
     * @return An RFC 1123 formatted date string.
     *
     * @see #PATTERN_RFC1123
     */
    public static String formatDate(final Date date) {
        final Instant instant = Instant.ofEpochMilli(date.getTime());
        return FORMATTER_RFC1123.format(instant.atZone(GMT_ID));
    }

    /**
     * Returns a list of {@link NameValuePair}s URI query parameters.
     * By convention, {@code '&'} and {@code ';'} are accepted as parameter separators.
     *
     * @param s URI query component.
     * @param charset charset to use when decoding the parameters.
     * @return list of query parameters.
     */
    public static List<NameValuePair> parseUrlQuery(final String s, final Charset charset) {
        if (s == null) {
            return new ArrayList<>(0);
        }

        final BitSet delimSet = new BitSet();
        delimSet.set('&');
        delimSet.set(';');

        final ParseRange cursor = new ParseRange(0, s.length());
        final List<NameValuePair> list = new ArrayList<>();
        while (!cursor.atEnd()) {
            delimSet.set('=');
            final String name = parseToken(s, cursor, delimSet);
            String value = null;
            if (!cursor.atEnd()) {
                final int delim = s.charAt(cursor.getPos());
                cursor.updatePos(cursor.getPos() + 1);
                if (delim == '=') {
                    delimSet.clear('=');
                    value = parseToken(s, cursor, delimSet);
                    if (!cursor.atEnd()) {
                        cursor.updatePos(cursor.getPos() + 1);
                    }
                }
            }
            if (!name.isEmpty()) {
                list.add(new NameValuePair(
                        decodeFormFields(name, charset),
                        decodeFormFields(value, charset)));
            }
        }
        return list;
    }

    private static String decodeFormFields(final String content, Charset charset) {
        if (content == null) {
            return null;
        }

        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }

        final ByteBuffer bb = ByteBuffer.allocate(content.length());
        final CharBuffer cb = CharBuffer.wrap(content);
        while (cb.hasRemaining()) {
            final char c = cb.get();
            if (c == '%' && cb.remaining() >= 2) {
                final char uc = cb.get();
                final char lc = cb.get();
                final int u = Character.digit(uc, 16);
                final int l = Character.digit(lc, 16);
                if (u != -1 && l != -1) {
                    bb.put((byte) ((u << 4) + l));
                }
                else {
                    bb.put((byte) '%');
                    bb.put((byte) uc);
                    bb.put((byte) lc);
                }
            }
            else if (c == '+') {
                bb.put((byte) ' ');
            }
            else {
                bb.put((byte) c);
            }
        }
        bb.flip();
        return charset.decode(bb).toString();
    }

    /**
     * @param parameters the paramters
     * @param charset the charset
     * @return the query string from the given parameters
     */
    public static String toQueryFormFields(final Iterable<? extends NameValuePair> parameters, final Charset charset) {
        final StringBuilder result = new StringBuilder();
        for (final NameValuePair parameter : parameters) {
            final String encodedName = encodeFormFields(parameter.getName(), charset);
            final String encodedValue = encodeFormFields(parameter.getValue(), charset);
            if (result.length() > 0) {
                result.append('&');
            }
            result.append(encodedName);
            if (encodedValue != null) {
                result.append('=').append(encodedValue);
            }
        }
        return result.toString();
    }

    private static String encodeFormFields(final String content, Charset charset) {
        if (content == null) {
            return null;
        }
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }

        final StringBuilder buf = new StringBuilder();
        final ByteBuffer bb = charset.encode(content);
        while (bb.hasRemaining()) {
            final int b = bb.get() & 0xff;
            if (URLENCODER.get(b)) {
                buf.append((char) b);
            }
            else if (b == ' ') {
                buf.append('+');
            }
            else {
                buf.append("%");
                final char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
                final char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
                buf.append(hex1).append(hex2);
            }
        }
        return buf.toString();
    }

    private HttpUtils() {
    }

    /**
     * Extracts from the sequence of chars a token terminated with any of the given delimiters
     * discarding semantically insignificant whitespace characters.
     *
     * @param buf buffer with the sequence of chars to be parsed
     * @param range defines the bounds and current position of the buffer
     * @param delimiters set of delimiting characters. Can be {@code null} if the token
     *  is not delimited by any character.
     */
    private static String parseToken(final String buf, final ParseRange range, final BitSet delimiters) {
        final StringBuilder dst = new StringBuilder();
        boolean whitespace = false;
        while (!range.atEnd()) {
            final char current = buf.charAt(range.getPos());
            if (delimiters.get(current)) {
                break;
            }
            else if (isWhitespace(current)) {
                skipWhiteSpace(buf, range);
                whitespace = true;
            }
            else {
                if (whitespace && dst.length() > 0) {
                    dst.append(' ');
                }
                copyContent(buf, range, delimiters, dst);
                whitespace = false;
            }
        }
        return dst.toString();
    }

    /**
     * Skips semantically insignificant whitespace characters and moves the cursor to the closest
     * non-whitespace character.
     *
     * @param buf buffer with the sequence of chars to be parsed
     * @param range defines the bounds and current position of the buffer
     */
    private static void skipWhiteSpace(final String buf, final ParseRange range) {
        int pos = range.getPos();
        final int indexTo = range.getUpperBound();

        for (int i = pos; i < indexTo; i++) {
            if (!isWhitespace(buf.charAt(i))) {
                break;
            }
            pos++;
        }
        range.updatePos(pos);
    }

    /**
     * Transfers content into the destination buffer until a whitespace character or any of
     * the given delimiters is encountered.
     *
     * @param buf buffer with the sequence of chars to be parsed
     * @param range defines the bounds and current position of the buffer
     * @param delimiters set of delimiting characters. Can be {@code null} if the value
     *  is delimited by a whitespace only.
     * @param dst destination buffer
     */
    private static void copyContent(final String buf, final ParseRange range,
            final BitSet delimiters, final StringBuilder dst) {
        int pos = range.getPos();
        final int indexTo = range.getUpperBound();

        for (int i = pos; i < indexTo; i++) {
            final char current = buf.charAt(i);
            if ((delimiters.get(current)) || isWhitespace(current)) {
                break;
            }
            pos++;
            dst.append(current);
        }

        range.updatePos(pos);
    }

    private static boolean isWhitespace(final char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    private static final class ParseRange {
        private final int upperBound_;
        private int pos_;

        ParseRange(final int pos, final int upperBound) {
            upperBound_ = upperBound;
            pos_ = pos;
        }

        int getPos() {
            return pos_;
        }

        int getUpperBound() {
            return upperBound_;
        }

        void updatePos(final int pos) {
            pos_ = pos;
        }

        boolean atEnd() {
            return pos_ >= upperBound_;
        }
    }
}

