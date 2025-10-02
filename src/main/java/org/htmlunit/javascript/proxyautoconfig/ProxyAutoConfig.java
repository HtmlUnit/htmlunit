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
package org.htmlunit.javascript.proxyautoconfig;

import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

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
    private static final ZoneId GMT_ZONE = ZoneId.of(TIMEZONE_GMT);
    private static final DateTimeFormatter WEEKDAY_FORMATTER = DateTimeFormatter.ofPattern("EEE", Locale.ROOT);
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM", Locale.ROOT);

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
        if (null == dnsResolve) {
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
     * Checks if today is included in the specified range.
     * @param wd1 week day 1
     * @param wd2 week day 2, optional
     * @param gmt string of "GMT", or not specified
     * @return if today is in range
     */
    @JsxFunction
    public static boolean weekdayRange(final String wd1, Object wd2, final Object gmt) {
        ZoneId zoneId = ZoneId.systemDefault();
        if (TIMEZONE_GMT.equals(JavaScriptEngine.toString(gmt))
                || TIMEZONE_GMT.equals(JavaScriptEngine.toString(wd2))) {
            zoneId = GMT_ZONE;
        }
        if (JavaScriptEngine.isUndefined(wd2) || TIMEZONE_GMT.equals(JavaScriptEngine.toString(wd2))) {
            wd2 = wd1;
        }

        LocalDate today = LocalDate.now(zoneId);
        for (int i = 0; i < 7; i++) {
            final String day = today.format(WEEKDAY_FORMATTER).toUpperCase(Locale.ROOT);
            if (day.equals(wd2)) {
                return true;
            }
            if (day.equals(wd1)) {
                return i == 0;
            }
            today = today.plusDays(1);
        }
        return false;
    }

    /**
     * Checks if today is included in the specified range.
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
        final Object[] values = {value1, value2, value3, value4, value5, value6, value7};
        ZoneId zoneId = ZoneId.systemDefault();

        // actual values length
        int length;
        for (length = values.length - 1; length >= 0; length--) {
            if (TIMEZONE_GMT.equals(JavaScriptEngine.toString(values[length]))) {
                zoneId = GMT_ZONE;
                break;
            }
            else if (!JavaScriptEngine.isUndefined(values[length])) {
                length++;
                break;
            }
        }

        final int day1;
        final int day2;
        final int month1;
        final int month2;
        final int year1;
        final int year2;
        final ZonedDateTime dateTime1;
        final ZonedDateTime dateTime2;

        switch (length) {
            case 1:
                final int day = getSmallInt(value1);
                final int month = dateRange_getMonth(value1);
                final int year = dateRange_getYear(value1);
                dateTime1 = dateRange_createDateTime(zoneId, day, month, year);
                dateTime2 = dateTime1;
                break;
            case 2:
                day1 = getSmallInt(value1);
                month1 = dateRange_getMonth(value1);
                year1 = dateRange_getYear(value1);
                dateTime1 = dateRange_createDateTime(zoneId, day1, month1, year1);
                day2 = getSmallInt(value2);
                month2 = dateRange_getMonth(value2);
                year2 = dateRange_getYear(value2);
                dateTime2 = dateRange_createDateTime(zoneId, day2, month2, year2);
                break;
            case 4:
                day1 = getSmallInt(value1);
                if (day1 != -1) {
                    month1 = dateRange_getMonth(value2);
                    day2 = getSmallInt(value3);
                    month2 = dateRange_getMonth(value4);
                    dateTime1 = dateRange_createDateTime(zoneId, day1, month1, -1);
                    dateTime2 = dateRange_createDateTime(zoneId, day2, month2, -1);
                }
                else {
                    month1 = dateRange_getMonth(value1);
                    year1 = getSmallInt(value2);
                    month2 = dateRange_getMonth(value3);
                    year2 = getSmallInt(value4);
                    dateTime1 = dateRange_createDateTime(zoneId, -1, month1, year1);
                    dateTime2 = dateRange_createDateTime(zoneId, -1, month2, year2);
                }
                break;
            default:
                day1 = getSmallInt(value1);
                month1 = dateRange_getMonth(value2);
                year1 = dateRange_getYear(value3);
                day2 = getSmallInt(value4);
                month2 = dateRange_getMonth(value5);
                year2 = dateRange_getYear(value6);
                dateTime1 = dateRange_createDateTime(zoneId, day1, month1, year1);
                dateTime2 = dateRange_createDateTime(zoneId, day2, month2, year2);
        }

        final ZonedDateTime now = ZonedDateTime.now(zoneId)
                .withSecond(0)
                .withNano(0);
        final ZonedDateTime dt1 = dateTime1.withSecond(0).withNano(0);
        final ZonedDateTime dt2 = dateTime2.withSecond(0).withNano(0);

        return now.isEqual(dt1) || (now.isAfter(dt1) && now.isBefore(dt2)) || now.isEqual(dt2);
    }

    private static ZonedDateTime dateRange_createDateTime(final ZoneId zoneId,
            final int day, final int month, final int year) {
        ZonedDateTime dateTime = ZonedDateTime.now(zoneId);
        if (day != -1) {
            dateTime = dateTime.withDayOfMonth(day);
        }
        if (month != -1) {
            dateTime = dateTime.withMonth(month + 1); // Calendar months are 0-based, java.time is 1-based
        }
        if (year != -1) {
            dateTime = dateTime.withYear(year);
        }
        return dateTime;
    }

    private static int getSmallInt(final Object object) {
        final String s = JavaScriptEngine.toString(object);
        if (Character.isDigit(s.charAt(0))) {
            final int i = Integer.parseInt(s);
            if (i < 70) {
                return i;
            }
        }
        return -1;
    }

    private static int dateRange_getMonth(final Object object) {
        final String s = JavaScriptEngine.toString(object);
        if (Character.isLetter(s.charAt(0))) {
            try {
                final LocalDate date = LocalDate.parse(s + " 1",
                        DateTimeFormatter.ofPattern("MMM d", Locale.ROOT));
                return date.getMonthValue() - 1; // Return 0-based month for compatibility
            }
            catch (final DateTimeParseException ignored) {
                // empty
            }
        }
        return -1;
    }

    private static int dateRange_getYear(final Object object) {
        final String s = JavaScriptEngine.toString(object);
        if (Character.isDigit(s.charAt(0))) {
            final int i = Integer.parseInt(s);
            if (i > 1000) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if the time now is included in the specified range.
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
        final Object[] values = {value1, value2, value3, value4, value5, value6, value7};
        ZoneId zoneId = ZoneId.systemDefault();

        // actual values length
        int length;
        for (length = values.length - 1; length >= 0; length--) {
            if (TIMEZONE_GMT.equals(JavaScriptEngine.toString(values[length]))) {
                zoneId = GMT_ZONE;
                break;
            }
            else if (!JavaScriptEngine.isUndefined(values[length])) {
                length++;
                break;
            }
        }

        final int hour1;
        final int hour2;
        final int min1;
        final int min2;
        final int second1;
        final int second2;
        final LocalTime time1;
        final LocalTime time2;

        switch (length) {
            case 1:
                hour1 = getSmallInt(value1);
                time1 = timeRange_createTime(hour1, -1, -1);
                time2 = time1.plusHours(1);
                break;
            case 2:
                hour1 = getSmallInt(value1);
                time1 = timeRange_createTime(hour1, -1, -1);
                hour2 = getSmallInt(value2);
                time2 = timeRange_createTime(hour2, -1, -1);
                break;
            case 4:
                hour1 = getSmallInt(value1);
                min1 = getSmallInt(value2);
                hour2 = getSmallInt(value3);
                min2 = getSmallInt(value4);
                time1 = timeRange_createTime(hour1, min1, -1);
                time2 = timeRange_createTime(hour2, min2, -1);
                break;
            default:
                hour1 = getSmallInt(value1);
                min1 = getSmallInt(value2);
                second1 = getSmallInt(value3);
                hour2 = getSmallInt(value4);
                min2 = getSmallInt(value5);
                second2 = getSmallInt(value6);
                time1 = timeRange_createTime(hour1, min1, second1);
                time2 = timeRange_createTime(hour2, min2, second2);
        }

        final LocalTime now = LocalTime.now(zoneId);
        return now.equals(time1) || (now.isAfter(time1) && now.isBefore(time2)) || now.equals(time2);
    }

    private static LocalTime timeRange_createTime(final int hour, final int minute, final int second) {
        LocalTime time = LocalTime.now();
        if (hour != -1) {
            time = time.withHour(hour);
        }
        if (minute != -1) {
            time = time.withMinute(minute);
        }
        if (second != -1) {
            time = time.withSecond(second);
        }
        return time;
    }

    /**
     * Concatenates the four dot-separated bytes into one 4-byte word and converts it to decimal.
     * @param ip any dotted address such as an IP address or mask.
     * @return concatenates the four dot-separated bytes into one 4-byte word and converts it to decimal.
     */
    @JsxFunction(functionName = "convert_addr")
    public static long convertAddr(final String ip) {
        final String[] parts = StringUtils.split(ip, '.');
        return ((Integer.parseInt(parts[0]) & 0xff) << 24)
                | ((Integer.parseInt(parts[1]) & 0xff) << 16)
                | ((Integer.parseInt(parts[2]) & 0xff) << 8)
                | (Integer.parseInt(parts[3]) & 0xff);
    }
}
