/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.proxyautoconfig;

import java.net.InetAddress;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.util.SubnetUtils;

/**
 * Provides an implementation of Proxy Auto-Config (PAC).
 *
 * @see <a href=
 *     "https://developer.mozilla.org/en-US/docs/Web/HTTP/Proxy_servers_and_tunneling/Proxy_Auto-Configuration_PAC_file">
 *     Proxy Auto-Configuration (PAC) file</a>
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public final class ProxyAutoConfig extends HtmlUnitScriptable {

    private static final String TIMEZONE_GMT = "GMT";
    private static final List<String> WEEKDAYS = List.of("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT");
    private static final List<String> MONTHS = List.of("JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                                                        "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");

    private ProxyAutoConfig() {
        super();
    }

    /**
     * Returns true if there is no domain name in the hostname (no dots).
     * @param host the hostname from the URL (excluding port number).
     * @return true if there is no domain name in the hostname (no dots).
     */
    @JsxFunction
    public static boolean isPlainHostName(final String host) {
        return host.indexOf('.') == -1;
    }

    /**
     * Returns true if the domain of hostname matches.
     * @param host the hostname from the URL
     * @param domain the domain name to test the hostname against
     * @return true if the domain of hostname matches.
     */
    @JsxFunction
    public static boolean dnsDomainIs(final String host, final String domain) {
        return host.endsWith(domain);
    }

    /**
     * Returns true if the hostname matches exactly the specified hostname,
     * or if there is no domain name part in the hostname, but the unqualified hostname matches.
     * @param host the hostname from the URL
     * @param hostdom fully qualified hostname to match against
     * @return true if the hostname matches exactly the specified hostname,
     *         or if there is no domain name part in the hostname, but the unqualified hostname matches.
     */
    @JsxFunction
    public static boolean localHostOrDomainIs(final String host, final String hostdom) {
        return host.length() > 1 && host.equals(hostdom) || host.indexOf('.') == -1 && hostdom.startsWith(host);
    }

    /**
     * Tries to resolve the hostname. Returns true if succeeds.
     * @param host the hostname from the URL.
     * @return true if the specific hostname is resolvable.
     */
    @JsxFunction
    public static boolean isResolvable(final String host) {
        return dnsResolve(host) != null;
    }

    /**
     * Returns true if the IP address of the host matches the specified IP address pattern.
     * @param host a DNS hostname, or IP address.
     *        If a hostname is passed, it will be resolved into an IP address by this function.
     * @param pattern an IP address pattern in the dot-separated format
     * @param mask mask for the IP address pattern informing which parts of the IP address should be matched against.
     *        0 means ignore, 255 means match
     * @return true if the IP address of the host matches the specified IP address pattern.
     */
    @JsxFunction
    public static boolean isInNet(final String host, final String pattern, final String mask) {
        final String dnsResolve = dnsResolve(host);
        if (dnsResolve == null) {
            return false;
        }

        final SubnetUtils subnetUtils = new SubnetUtils(pattern, mask);
        return subnetUtils.isInRange(dnsResolve);
    }

    /**
     * Resolves the given DNS hostname into an IP address, and returns it in the dot separated format as a string.
     * @param host the hostname to resolve
     * @return the resolved IP address
     */
    @JsxFunction
    public static String dnsResolve(final String host) {
        try {
            return InetAddress.getByName(host).getHostAddress();
        }
        catch (final Exception e) {
            return null;
        }
    }

    /**
     * Returns the IP address of the local host, as a string in the dot-separated integer format.
     * @return the IP address of the local host, as a string in the dot-separated integer format.
     */
    @JsxFunction
    public static String myIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (final Exception e) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Returns the number (integer) of DNS domain levels (number of dots) in the hostname.
     * @param host the hostname from the URL
     * @return the number (integer) of DNS domain levels (number of dots) in the hostname.
     */
    @JsxFunction
    public static int dnsDomainLevels(final String host) {
        int levels = 0;
        for (int i = host.length() - 1; i >= 0; i--) {
            if (host.charAt(i) == '.') {
                levels++;
            }
        }
        return levels;
    }

    /**
     * Matches the specified string against a shell expression, not regular expression.
     * @param str a string to match
     * @param shexp the shell expression
     * @return if the string matches
     */
    @JsxFunction
    public static boolean shExpMatch(final String str, final String shexp) {
        final String regexp = shexp.replace(".", "\\.").replace("*", ".*").replace("?", ".");
        return str.matches(regexp);
    }

    /**
     * Checks if today is included in the specified range of week days.
     * Supported are {@code weekdayRange(wd1)} and {@code weekdayRange(wd1, wd2)}, each optionally
     * followed by {@code "GMT"}; the range may wrap around the end of the week (e.g. {@code FRI, MON}).
     * @param wd1 week day 1
     * @param wd2 week day 2, optional
     * @param gmt string of "GMT", or not specified
     * @return if today is in range
     */
    @JsxFunction
    public static boolean weekdayRange(final String wd1, final Object wd2, final Object gmt) {
        final Arguments args = Arguments.of(wd1, wd2, gmt);
        return isInWeekdayRange(LocalDate.now(args.zone()).getDayOfWeek(), args.values());
    }

    /**
     * Checks if the given week day is included in the specified range of week days.
     * @param today the week day to check
     * @param args the arguments (without "GMT")
     * @return if today is in range
     */
    static boolean isInWeekdayRange(final DayOfWeek today, final List<String> args) {
        if (args.isEmpty() || args.size() > 2) {
            return false;
        }

        final int day1 = WEEKDAYS.indexOf(args.get(0));
        final int day2 = args.size() == 2 ? WEEKDAYS.indexOf(args.get(1)) : day1;
        if (day1 == -1 || day2 == -1) {
            return false;
        }
        // DayOfWeek starts with MONDAY = 1 and ends with SUNDAY = 7
        return isInRange(day1, today.getValue() % 7, day2);
    }

    /**
     * Checks if today is included in the specified date range.
     * Supported are a single day, month or year and ranges of days, months, years or
     * combinations of them (e.g. {@code 1, JUN, 15, AUG} or {@code JUN, 1995, AUG, 1995}),
     * optionally followed by {@code "GMT"}; the range may wrap around the end of the year.
     * @param value1 the value 1
     * @param value2 the value 2
     * @param value3 the value 3
     * @param value4 the value 4
     * @param value5 the value 5
     * @param value6 the value 6
     * @param value7 the value 7
     * @return if today is in range
     */
    @JsxFunction
    public static boolean dateRange(final String value1, final Object value2, final Object value3,
            final Object value4, final Object value5, final Object value6, final Object value7) {
        final Arguments args = Arguments.of(value1, value2, value3, value4, value5, value6, value7);
        return isInDateRange(LocalDate.now(args.zone()), args.values());
    }

    /**
     * Checks if the given date is included in the specified date range.
     * @param today the date to check
     * @param args the arguments (without "GMT")
     * @return if today is in range
     */
    static boolean isInDateRange(final LocalDate today, final List<String> args) {
        final int count = args.size();
        if (count == 0 || count > 6) {
            return false;
        }

        if (count == 1) {
            final String arg = args.get(0);
            final int number = parseNumber(arg);
            if (number == -1) {
                return today.getMonthValue() == parseMonth(arg);
            }
            if (number < 32) {
                return today.getDayOfMonth() == number;
            }
            return today.getYear() == number;
        }

        final DateParts from = DateParts.parse(args.subList(0, count / 2));
        final DateParts to = DateParts.parse(args.subList(count / 2, count));
        if (from == null || to == null) {
            return false;
        }

        // a range of days only (e.g. 1, 15) is a range in the current month
        final boolean daysOnly = from.month_ == -1 && to.month_ == -1 && from.day_ != -1 && to.day_ != -1;
        final LocalDate start = from.toDate(today.getYear(), daysOnly ? today.getMonthValue() : 1, false);
        final LocalDate end = to.toDate(today.getYear(), daysOnly ? today.getMonthValue() : 12, true);
        return isInRange(start, today, end);
    }

    /**
     * Checks if the time now is included in the specified range.
     * Supported are {@code hour}, {@code hour1, hour2}, {@code hour1, min1, hour2, min2} and
     * {@code hour1, min1, sec1, hour2, min2, sec2}, optionally followed by {@code "GMT"}.
     * All ranges include the end (e.g. {@code 8, 17} is true until 17:59:59);
     * the minute and second ranges may wrap around midnight.
     * @param value1 the value 1
     * @param value2 the value 2
     * @param value3 the value 3
     * @param value4 the value 4
     * @param value5 the value 5
     * @param value6 the value 6
     * @param value7 the value 7
     * @return if the time now is in the range
     */
    @JsxFunction
    public static boolean timeRange(final String value1, final Object value2, final Object value3,
            final Object value4, final Object value5, final Object value6, final Object value7) {
        final Arguments args = Arguments.of(value1, value2, value3, value4, value5, value6, value7);
        return isInTimeRange(LocalTime.now(args.zone()), args.values());
    }

    /**
     * Checks if the given time is included in the specified range.
     * @param now the time to check
     * @param args the arguments (without "GMT")
     * @return if the time is in the range
     */
    static boolean isInTimeRange(final LocalTime now, final List<String> args) {
        final int[] numbers = new int[args.size()];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = parseNumber(args.get(i));
            if (numbers[i] == -1) {
                return false;
            }
        }

        final LocalTime start;
        final LocalTime end;
        try {
            switch (numbers.length) {
                case 1:
                    return now.getHour() == numbers[0];

                case 2:
                    return numbers[0] <= now.getHour() && now.getHour() <= numbers[1];

                case 4:
                    start = LocalTime.of(numbers[0], numbers[1]);
                    end = LocalTime.of(numbers[2], numbers[3], 59);
                    break;

                case 6:
                    start = LocalTime.of(numbers[0], numbers[1], numbers[2]);
                    end = LocalTime.of(numbers[3], numbers[4], numbers[5]);
                    break;

                default:
                    return false;
            }
        }
        catch (final DateTimeException e) {
            // invalid hour, minute or second
            return false;
        }
        return isInRange(start, now.truncatedTo(ChronoUnit.SECONDS), end);
    }

    /**
     * The arguments of weekdayRange(), dateRange() and timeRange() as strings, without the optional
     * trailing "GMT" and the trailing undefined ones.
     * @param values the arguments
     * @param zone UTC if the "GMT" argument was present, the default time zone otherwise
     */
    private record Arguments(List<String> values, ZoneId zone) {

        /**
         * Creates the arguments from the values passed from JavaScript.
         * @param values the values passed from JavaScript (padded with undefined)
         * @return the arguments
         */
        static Arguments of(final Object... values) {
            int length = withoutTrailingUndefined(values, values.length);
            ZoneId zone = ZoneId.systemDefault();
            if (length > 0 && TIMEZONE_GMT.equals(JavaScriptEngine.toString(values[length - 1]))) {
                zone = ZoneOffset.UTC;
                // e.g. weekdayRange('MON', undefined, 'GMT') if called from java
                length = withoutTrailingUndefined(values, length - 1);
            }

            final List<String> args = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                args.add(JavaScriptEngine.toString(values[i]));
            }
            return new Arguments(args, zone);
        }

        private static int withoutTrailingUndefined(final Object[] values, final int length) {
            int result = length;
            while (result > 0 && JavaScriptEngine.isUndefined(values[result - 1])) {
                result--;
            }
            return result;
        }
    }

    /**
     * Checks if the value is in the range.
     * @param start the start of the range
     * @param value the value to check
     * @param end the end of the range (included)
     * @return if the value is in the range; if start is after end, the range wraps around
     */
    private static <T extends Comparable<T>> boolean isInRange(final T start, final T value, final T end) {
        if (start.compareTo(end) <= 0) {
            return start.compareTo(value) <= 0 && value.compareTo(end) <= 0;
        }
        return start.compareTo(value) <= 0 || value.compareTo(end) <= 0;
    }

    /**
     * Parses the leading digits of the value.
     * @param value the value
     * @return the number if the value starts with digits (like parseInt()), -1 otherwise
     */
    private static int parseNumber(final String value) {
        final String trimmed = value.trim();
        int end = 0;
        while (end < trimmed.length() && end < 9 && Character.isDigit(trimmed.charAt(end))) {
            end++;
        }
        if (end == 0) {
            return -1;
        }
        return Integer.parseInt(trimmed.substring(0, end));
    }

    /**
     * Parses the month name (JAN - DEC).
     * @param value the value
     * @return the month (1 - 12) or -1 if the value is not a month name
     */
    private static int parseMonth(final String value) {
        final int index = MONTHS.indexOf(value);
        return index == -1 ? -1 : index + 1;
    }

    /**
     * The day, month and year of one end of a date range; -1 if not specified.
     */
    private static final class DateParts {
        private int day_ = -1;
        private int month_ = -1;
        private int year_ = -1;

        /**
         * Parses the parts of one end of a date range.
         * @param args the arguments
         * @return the parts or null if one of the arguments is invalid
         */
        static DateParts parse(final List<String> args) {
            final DateParts parts = new DateParts();
            for (final String arg : args) {
                final int number = parseNumber(arg);
                if (number == -1) {
                    parts.month_ = parseMonth(arg);
                    if (parts.month_ == -1) {
                        return null;
                    }
                }
                else if (number == 0) {
                    return null;
                }
                else if (number < 32) {
                    parts.day_ = number;
                }
                else {
                    parts.year_ = number;
                }
            }
            return parts;
        }

        /**
         * Creates the date of this end of the range.
         * @param defaultYear the year to use if not specified
         * @param defaultMonth the month to use if not specified
         * @param end whether this is the end of the range (the default day is the last day of the month)
         * @return the date
         */
        LocalDate toDate(final int defaultYear, final int defaultMonth, final boolean end) {
            final YearMonth yearMonth = YearMonth.of(year_ == -1 ? defaultYear : year_,
                                                        month_ == -1 ? defaultMonth : month_);
            if (day_ == -1) {
                return end ? yearMonth.atEndOfMonth() : yearMonth.atDay(1);
            }
            // e.g. 31 for JUN is the last day of the month
            return yearMonth.atDay(Math.min(day_, yearMonth.lengthOfMonth()));
        }
    }

    /**
     * Concatenates the four dot-separated bytes into one 4-byte word and converts it to decimal.
     * @param ip any dotted address such as an IP address or mask.
     * @return concatenates the four dot-separated bytes into one 4-byte word and converts it to decimal.
     */
    @JsxFunction(functionName = "convert_addr")
    public static long convertAddr(final String ip) {
        final String[] parts = StringUtils.split(ip, '.');

        return
            ((Integer.parseInt(parts[0]) & 0xff) << 24)
                | ((Integer.parseInt(parts[1]) & 0xff) << 16)
                | ((Integer.parseInt(parts[2]) & 0xff) << 8)
                | (Integer.parseInt(parts[3]) & 0xff);
    }
}
