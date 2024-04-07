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

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

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

    private HttpUtils() {
    }
}
