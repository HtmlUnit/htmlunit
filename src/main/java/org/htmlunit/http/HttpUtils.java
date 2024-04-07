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
     * Date format pattern used to parse HTTP date headers in RFC 1123 format.
     */
    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

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
                final Instant instant = Instant.from(dateFormatter.parse(v));
                return new Date(instant.toEpochMilli());
            }
            catch (final DateTimeParseException ignore) {
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

        final TokenParser tokenParser = TokenParser.INSTANCE;
        final ParseRange cursor = new ParseRange(0, s.length());

        final BitSet delimSet = new BitSet();
        delimSet.set('&');
        delimSet.set(';');

        final List<NameValuePair> list = new ArrayList<>();
        while (!cursor.atEnd()) {
            delimSet.set('=');
            final String name = tokenParser.parseToken(s, cursor, delimSet);
            String value = null;
            if (!cursor.atEnd()) {
                final int delim = s.charAt(cursor.getPos());
                cursor.updatePos(cursor.getPos() + 1);
                if (delim == '=') {
                    delimSet.clear('=');
                    value = tokenParser.parseToken(s, cursor, delimSet);
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

    private HttpUtils() {
    }

    private static class TokenParser {

        /** US-ASCII CR, carriage return (13). */
        private static final char CR = '\r';

        /** US-ASCII LF, line feed (10). */
        private static final char LF = '\n';

        /** US-ASCII SP, space (32). */
        private static final char SP = ' ';

        /** US-ASCII HT, horizontal-tab (9). */
        private static final char HT = '\t';

        public static boolean isWhitespace(final char ch) {
            return ch == SP || ch == HT || ch == CR || ch == LF;
        }

        private static final TokenParser INSTANCE = new TokenParser();

        /**
         * Extracts from the sequence of chars a token terminated with any of the given delimiters
         * discarding semantically insignificant whitespace characters.
         *
         * @param buf buffer with the sequence of chars to be parsed
         * @param range defines the bounds and current position of the buffer
         * @param delimiters set of delimiting characters. Can be {@code null} if the token
         *  is not delimited by any character.
         */
        private String parseToken(final String buf, final ParseRange range, final BitSet delimiters) {
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
        public void skipWhiteSpace(final String buf, final ParseRange range) {
            int pos = range.getPos();
            final int indexFrom = pos;
            final int indexTo = range.getUpperBound();
            for (int i = indexFrom; i < indexTo; i++) {
                final char current = buf.charAt(i);
                if (!isWhitespace(current)) {
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
        public void copyContent(final String buf, final ParseRange range,
                final BitSet delimiters, final StringBuilder dst) {
            int pos = range.getPos();
            final int indexFrom = pos;
            final int indexTo = range.getUpperBound();
            for (int i = indexFrom; i < indexTo; i++) {
                final char current = buf.charAt(i);
                if ((delimiters.get(current)) || isWhitespace(current)) {
                    break;
                }
                pos++;
                dst.append(current);
            }
            range.updatePos(pos);
        }
    }

    private static final class ParseRange {
        private final int upperBound_;
        private int pos_;

        ParseRange(final int pos, final int upperBound) {
            super();
            if (pos < 0) {
                throw new IndexOutOfBoundsException("Lower bound cannot be negative");
            }
            if (pos > upperBound) {
                throw new IndexOutOfBoundsException("Lower bound cannot be greater then upper bound");
            }
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
            if (pos > upperBound_) {
                throw new IndexOutOfBoundsException("pos: " + pos + " > upperBound: " + upperBound_);
            }
            pos_ = pos;
        }

        boolean atEnd() {
            return pos_ >= upperBound_;
        }
    }
}

