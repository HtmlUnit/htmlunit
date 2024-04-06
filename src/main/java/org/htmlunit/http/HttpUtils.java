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

import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Http related utils.
 *
 * @author Ronald Brill
 */
public final class HttpUtils {

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final Date DEFAULT_TWO_DIGIT_YEAR_START;

    private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>
        THREADLOCAL_FORMATS = new ThreadLocal<>();

    private static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

    private static final String[] DEFAULT_PATTERNS = new String[] {
        PATTERN_RFC1123,
        "EEE, dd-MMM-yy HH:mm:ss zzz", // RFC1036
        "EEE MMM d HH:mm:ss yyyy" // ASCTIME
    };

    static {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(GMT);
        calendar.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        DEFAULT_TWO_DIGIT_YEAR_START = calendar.getTime();
    }

    /**
     * Parses the specified date string, assuming that it is formatted
     * according to RFC 1123, RFC 1036 or as an ANSI C HTTP date header.
     *
     * @param dateString the string to parse as a date
     * @return the date version of the specified string,
     *  or {@code null} if the specified string is {@code null} or unparseable
     */
    public static Date parseDate(final String dateString) {
        if (dateString == null) {
            return null;
        }

        String dateValue = dateString;
        // trim single quotes around date if present
        if (dateValue.length() > 1 && dateValue.startsWith("'") && dateValue.endsWith("'")) {
            dateValue = dateValue.substring(1, dateValue.length() - 1);
        }

        for (final String datePattern : DEFAULT_PATTERNS) {
            final SimpleDateFormat dateFormat = formatFor(datePattern);

            final ParsePosition pos = new ParsePosition(0);
            final Date result = dateFormat.parse(dateValue, pos);
            if (pos.getIndex() != 0) {
                return result;
            }
        }
        return null;
    }

    /**
     * Formats the given date according to the RFC 1123 pattern
     * 'EEE, dd MMM yyyy HH:mm:ss zzz'.
     *
     * @param date The date to format.
     * @return An RFC 1123 formatted date string.
     */
    public static String formatDate(final Date date) {
        final SimpleDateFormat formatter = formatFor(PATTERN_RFC1123);
        return formatter.format(date);
    }

    // cache the format per thread
    private static SimpleDateFormat formatFor(final String pattern) {
        final SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
        Map<String, SimpleDateFormat> formats = ref == null ? null : ref.get();
        if (formats == null) {
            formats = new HashMap<>();
            THREADLOCAL_FORMATS.set(new SoftReference<>(formats));
        }

        SimpleDateFormat format = formats.get(pattern);
        if (format == null) {
            format = new SimpleDateFormat(pattern, Locale.US);
            format.setTimeZone(GMT);
            format.set2DigitYearStart(DEFAULT_TWO_DIGIT_YEAR_START);
            formats.put(pattern, format);
        }

        return format;
    }

    private HttpUtils() {
    }
}
