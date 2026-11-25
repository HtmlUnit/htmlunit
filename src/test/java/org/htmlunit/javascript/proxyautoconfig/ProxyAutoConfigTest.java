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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.htmlunit.BrowserVersion;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.corejs.javascript.Undefined;
import org.htmlunit.javascript.JavaScriptEngine;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ProxyAutoConfig}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ProxyAutoConfigTest extends SimpleWebTestCase {

    /**
     * Test case.
     */
    @Test
    public void shExpMatch() {
        assertTrue(ProxyAutoConfig.shExpMatch("http://home.netscape.com/people/ari/index.html", "*/ari/*"));
        assertFalse(ProxyAutoConfig.shExpMatch("http://home.netscape.com/people/montulli/index.html", "*/ari/*"));
    }

    /**
     * Test case.
     */
    @Test
    public void dnsLevelDomains() {
        assertEquals(0, ProxyAutoConfig.dnsDomainLevels("localhost"));
        assertEquals(1, ProxyAutoConfig.dnsDomainLevels("example.com"));
        assertEquals(2, ProxyAutoConfig.dnsDomainLevels("www.example.com"));
        assertEquals(3, ProxyAutoConfig.dnsDomainLevels("api.sub.example.com"));
        assertEquals(4, ProxyAutoConfig.dnsDomainLevels("deep.api.sub.example.com"));

        assertEquals(2, ProxyAutoConfig.dnsDomainLevels(".example.com"));
        assertEquals(2, ProxyAutoConfig.dnsDomainLevels("example.com."));
        assertEquals(2, ProxyAutoConfig.dnsDomainLevels("example..com"));
        assertEquals(3, ProxyAutoConfig.dnsDomainLevels("example...com"));

        assertEquals(0, ProxyAutoConfig.dnsDomainLevels(""));
        assertEquals(1, ProxyAutoConfig.dnsDomainLevels("."));
        assertEquals(3, ProxyAutoConfig.dnsDomainLevels("..."));

        assertEquals(3, ProxyAutoConfig.dnsDomainLevels("192.168.1.1"));
    }

    /**
     * Test case.
     */
    @Test
    public void weekdayRange() {
        final Object undefined = Undefined.instance;

        final TimeZone timeZone = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

            Calendar calendar = Calendar.getInstance(Locale.ROOT);
            DateFormat dateFormat = new SimpleDateFormat("EEE", Locale.ROOT);
            String today = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ROOT);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            String tomorrow = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ROOT);
            calendar.add(Calendar.DAY_OF_MONTH, -2);
            String yesterday = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ROOT);

            assertTrue(ProxyAutoConfig.weekdayRange(today, undefined, undefined));
            assertTrue(ProxyAutoConfig.weekdayRange(today, tomorrow, undefined));
            assertTrue(ProxyAutoConfig.weekdayRange(yesterday, today, undefined));
            assertTrue(ProxyAutoConfig.weekdayRange(yesterday, tomorrow, undefined));
            assertFalse(ProxyAutoConfig.weekdayRange(tomorrow, yesterday, undefined));

            assertTrue(ProxyAutoConfig.weekdayRange(today, undefined, "GMT"));
            assertTrue(ProxyAutoConfig.weekdayRange(today, tomorrow, "GMT"));
            assertTrue(ProxyAutoConfig.weekdayRange(yesterday, today, "GMT"));
            assertTrue(ProxyAutoConfig.weekdayRange(yesterday, tomorrow, "GMT"));
            assertFalse(ProxyAutoConfig.weekdayRange(tomorrow, yesterday, "GMT"));

            TimeZone.setDefault(TimeZone.getTimeZone("PST"));

            calendar = Calendar.getInstance(Locale.ROOT);
            dateFormat = new SimpleDateFormat("EEE", Locale.ROOT);
            today = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ROOT);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            tomorrow = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ROOT);
            calendar.add(Calendar.DAY_OF_MONTH, -2);
            yesterday = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ROOT);

            assertTrue(ProxyAutoConfig.weekdayRange(today, undefined, undefined));
            assertTrue(ProxyAutoConfig.weekdayRange(today, tomorrow, undefined));
            assertTrue(ProxyAutoConfig.weekdayRange(yesterday, today, undefined));
            assertTrue(ProxyAutoConfig.weekdayRange(yesterday, tomorrow, undefined));
            assertFalse(ProxyAutoConfig.weekdayRange(tomorrow, yesterday, undefined));

            assertTrue(ProxyAutoConfig.weekdayRange(today, undefined, "GMT"));
            assertTrue(ProxyAutoConfig.weekdayRange(today, tomorrow, "GMT"));
            assertTrue(ProxyAutoConfig.weekdayRange(yesterday, today, "GMT"));
            assertTrue(ProxyAutoConfig.weekdayRange(yesterday, tomorrow, "GMT"));
            assertFalse(ProxyAutoConfig.weekdayRange(tomorrow, yesterday, "GMT"));
        }
        finally {
            TimeZone.setDefault(timeZone);
        }
    }

    /**
     * Test case.
     */
    @Test
    public void dateRange() {
        final Object undefined = Undefined.instance;

        final TimeZone timeZone = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

            Calendar calendar = Calendar.getInstance(Locale.ROOT);
            int today = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            int tomorrow = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_MONTH, -2);
            int yesterday = calendar.get(Calendar.DAY_OF_MONTH);

            assertTrue(ProxyAutoConfig.dateRange(String.valueOf(today),
                    undefined, undefined, undefined, undefined, undefined, undefined));
            assertFalse(ProxyAutoConfig.dateRange(String.valueOf(yesterday),
                    undefined, undefined, undefined, undefined, undefined, undefined));
            assertFalse(ProxyAutoConfig.dateRange(String.valueOf(tomorrow),
                    undefined, undefined, undefined, undefined, undefined, undefined));

            TimeZone.setDefault(TimeZone.getTimeZone("PST"));

            calendar = Calendar.getInstance(Locale.ROOT);
            today = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            tomorrow = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_MONTH, -2);
            yesterday = calendar.get(Calendar.DAY_OF_MONTH);

            assertTrue(ProxyAutoConfig.dateRange(String.valueOf(today),
                    undefined, undefined, undefined, undefined, undefined, undefined));
            assertFalse(ProxyAutoConfig.dateRange(String.valueOf(yesterday),
                    undefined, undefined, undefined, undefined, undefined, undefined));
            assertFalse(ProxyAutoConfig.dateRange(String.valueOf(tomorrow),
                    undefined, undefined, undefined, undefined, undefined, undefined));
        }
        finally {
            TimeZone.setDefault(timeZone);
        }
    }

    /**
     * Test case.
     */
    @Test
    public void timeRange() {
        final Object undefined = Undefined.instance;

        final TimeZone timeZone = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

            Calendar calendar = Calendar.getInstance(Locale.ROOT);
            int now = calendar.get(Calendar.HOUR_OF_DAY);
            calendar.add(Calendar.HOUR_OF_DAY, 2);
            int after = calendar.get(Calendar.HOUR_OF_DAY);
            calendar.add(Calendar.HOUR_OF_DAY, -4);
            int before = calendar.get(Calendar.HOUR_OF_DAY);

            assertTrue(ProxyAutoConfig.timeRange(String.valueOf(now),
                    undefined, undefined, undefined, undefined, undefined, undefined));
            assertFalse(ProxyAutoConfig.timeRange(String.valueOf(before),
                    undefined, undefined, undefined, undefined, undefined, undefined));
            assertFalse(ProxyAutoConfig.timeRange(String.valueOf(after),
                    undefined, undefined, undefined, undefined, undefined, undefined));

            TimeZone.setDefault(TimeZone.getTimeZone("PST"));

            calendar = Calendar.getInstance(Locale.ROOT);
            now = calendar.get(Calendar.HOUR_OF_DAY);
            calendar.add(Calendar.HOUR_OF_DAY, 2);
            after = calendar.get(Calendar.HOUR_OF_DAY);
            calendar.add(Calendar.HOUR_OF_DAY, -4);
            before = calendar.get(Calendar.HOUR_OF_DAY);

            assertTrue(ProxyAutoConfig.timeRange(String.valueOf(now),
                    undefined, undefined, undefined, undefined, undefined, undefined));
            assertFalse(ProxyAutoConfig.timeRange(String.valueOf(before),
                    undefined, undefined, undefined, undefined, undefined, undefined));
            assertFalse(ProxyAutoConfig.timeRange(String.valueOf(after),
                    undefined, undefined, undefined, undefined, undefined, undefined));
        }
        finally {
            TimeZone.setDefault(timeZone);
        }
    }

    /**
     * Test case.
     */
    @Test
    public void weekdayRangeSingleDay() {
        assertTrue(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.WEDNESDAY, List.of("WED")));
        assertFalse(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.WEDNESDAY, List.of("THU")));
        assertTrue(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.SUNDAY, List.of("SUN")));
    }

    /**
     * Test case.
     */
    @Test
    public void weekdayRangeRange() {
        assertTrue(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.MONDAY, List.of("MON", "FRI")));
        assertTrue(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.WEDNESDAY, List.of("MON", "FRI")));
        assertTrue(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.FRIDAY, List.of("MON", "FRI")));
        assertFalse(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.SATURDAY, List.of("MON", "FRI")));
        assertFalse(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.SUNDAY, List.of("MON", "FRI")));
    }

    /**
     * Test case.
     */
    @Test
    public void weekdayRangeWrapAround() {
        assertTrue(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.FRIDAY, List.of("FRI", "MON")));
        assertTrue(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.SUNDAY, List.of("FRI", "MON")));
        assertTrue(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.MONDAY, List.of("FRI", "MON")));
        assertFalse(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.TUESDAY, List.of("FRI", "MON")));
        assertFalse(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.THURSDAY, List.of("FRI", "MON")));
    }

    /**
     * Test case.
     */
    @Test
    public void weekdayRangeInvalid() {
        assertFalse(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.WEDNESDAY, List.of()));
        assertFalse(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.WEDNESDAY, List.of("wed")));
        assertFalse(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.WEDNESDAY, List.of("MON", "XYZ")));
        assertFalse(ProxyAutoConfig.isInWeekdayRange(DayOfWeek.WEDNESDAY, List.of("MON", "TUE", "WED")));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeSingleDay() {
        final LocalDate today = LocalDate.of(2026, 11, 25);
        assertTrue(ProxyAutoConfig.isInDateRange(today, List.of("25")));
        assertFalse(ProxyAutoConfig.isInDateRange(today, List.of("24")));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeSingleMonth() {
        final LocalDate today = LocalDate.of(2026, 11, 25);
        assertTrue(ProxyAutoConfig.isInDateRange(today, List.of("NOV")));
        assertFalse(ProxyAutoConfig.isInDateRange(today, List.of("DEC")));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeSingleYear() {
        final LocalDate today = LocalDate.of(2026, 11, 25);
        assertTrue(ProxyAutoConfig.isInDateRange(today, List.of("2026")));
        assertFalse(ProxyAutoConfig.isInDateRange(today, List.of("2025")));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeDays() {
        // a range of days is a range in the current month
        final LocalDate today = LocalDate.of(2026, 11, 25);
        assertTrue(ProxyAutoConfig.isInDateRange(today, List.of("1", "25")));
        assertTrue(ProxyAutoConfig.isInDateRange(today, List.of("25", "31")));
        assertFalse(ProxyAutoConfig.isInDateRange(today, List.of("1", "24")));
        assertFalse(ProxyAutoConfig.isInDateRange(today, List.of("26", "31")));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeMonths() {
        // the end month is included completely
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 1, 1), List.of("JAN", "MAR")));
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 3, 31), List.of("JAN", "MAR")));
        assertFalse(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 4, 1), List.of("JAN", "MAR")));
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 2, 28), List.of("JAN", "FEB")));
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(2028, 2, 29), List.of("JAN", "FEB")));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeMonthsWrapAround() {
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 12, 31), List.of("NOV", "FEB")));
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 1, 15), List.of("NOV", "FEB")));
        assertFalse(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 3, 1), List.of("NOV", "FEB")));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeYears() {
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(2025, 1, 1), List.of("2025", "2026")));
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 12, 31), List.of("2025", "2026")));
        assertFalse(ProxyAutoConfig.isInDateRange(LocalDate.of(2027, 1, 1), List.of("2025", "2026")));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeDayMonth() {
        final List<String> args = List.of("1", "JUN", "15", "AUG");
        assertFalse(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 5, 31), args));
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 6, 1), args));
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 8, 15), args));
        assertFalse(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 8, 16), args));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeMonthYear() {
        final List<String> args = List.of("JUN", "1995", "AUG", "1995");
        assertFalse(ProxyAutoConfig.isInDateRange(LocalDate.of(1995, 5, 31), args));
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(1995, 6, 1), args));
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(1995, 8, 31), args));
        assertFalse(ProxyAutoConfig.isInDateRange(LocalDate.of(1995, 9, 1), args));
        assertFalse(ProxyAutoConfig.isInDateRange(LocalDate.of(1996, 7, 1), args));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeDayMonthYear() {
        final List<String> args = List.of("1", "JUN", "1995", "15", "AUG", "1995");
        assertFalse(ProxyAutoConfig.isInDateRange(LocalDate.of(1995, 5, 31), args));
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(1995, 6, 1), args));
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(1995, 8, 15), args));
        assertFalse(ProxyAutoConfig.isInDateRange(LocalDate.of(1995, 8, 16), args));
        assertFalse(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 7, 1), args));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeDayAfterEndOfMonth() {
        // 31 JUN is the last day of june
        final List<String> args = List.of("1", "JUN", "31", "JUN");
        assertTrue(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 6, 30), args));
        assertFalse(ProxyAutoConfig.isInDateRange(LocalDate.of(2026, 7, 1), args));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeInvalid() {
        final LocalDate today = LocalDate.of(2026, 11, 25);
        assertFalse(ProxyAutoConfig.isInDateRange(today, List.of()));
        assertFalse(ProxyAutoConfig.isInDateRange(today, List.of("XYZ")));
        assertFalse(ProxyAutoConfig.isInDateRange(today, List.of("nov")));
        assertFalse(ProxyAutoConfig.isInDateRange(today, List.of("JAN", "XYZ")));
        assertFalse(ProxyAutoConfig.isInDateRange(today, List.of("0", "31")));
        assertFalse(ProxyAutoConfig.isInDateRange(today, List.of("1", "2", "3", "4", "5", "6", "7")));
    }

    /**
     * Test case.
     */
    @Test
    public void timeRangeHour() {
        assertTrue(ProxyAutoConfig.isInTimeRange(LocalTime.of(12, 0), List.of("12")));
        assertTrue(ProxyAutoConfig.isInTimeRange(LocalTime.of(12, 59, 59), List.of("12")));
        assertFalse(ProxyAutoConfig.isInTimeRange(LocalTime.of(13, 0), List.of("12")));
        assertFalse(ProxyAutoConfig.isInTimeRange(LocalTime.of(11, 59, 59), List.of("12")));
    }

    /**
     * Test case.
     */
    @Test
    public void timeRangeHours() {
        assertFalse(ProxyAutoConfig.isInTimeRange(LocalTime.of(7, 59, 59), List.of("8", "17")));
        assertTrue(ProxyAutoConfig.isInTimeRange(LocalTime.of(8, 0), List.of("8", "17")));
        assertTrue(ProxyAutoConfig.isInTimeRange(LocalTime.of(17, 59, 59), List.of("8", "17")));
        assertFalse(ProxyAutoConfig.isInTimeRange(LocalTime.of(18, 0), List.of("8", "17")));
    }

    /**
     * Test case.
     */
    @Test
    public void timeRangeHoursMinutes() {
        final List<String> args = List.of("8", "30", "17", "0");
        assertFalse(ProxyAutoConfig.isInTimeRange(LocalTime.of(8, 29, 59), args));
        assertTrue(ProxyAutoConfig.isInTimeRange(LocalTime.of(8, 30), args));
        assertTrue(ProxyAutoConfig.isInTimeRange(LocalTime.of(17, 0, 59), args));
        assertFalse(ProxyAutoConfig.isInTimeRange(LocalTime.of(17, 1), args));
    }

    /**
     * Test case.
     */
    @Test
    public void timeRangeHoursMinutesSeconds() {
        final List<String> args = List.of("0", "0", "0", "0", "0", "30");
        assertTrue(ProxyAutoConfig.isInTimeRange(LocalTime.of(0, 0), args));
        assertTrue(ProxyAutoConfig.isInTimeRange(LocalTime.of(0, 0, 30, 999_000_000), args));
        assertFalse(ProxyAutoConfig.isInTimeRange(LocalTime.of(0, 0, 31), args));
        assertFalse(ProxyAutoConfig.isInTimeRange(LocalTime.of(23, 59, 59), args));
    }

    /**
     * Test case.
     */
    @Test
    public void timeRangeWrapAround() {
        final List<String> args = List.of("22", "0", "6", "0");
        assertTrue(ProxyAutoConfig.isInTimeRange(LocalTime.of(23, 0), args));
        assertTrue(ProxyAutoConfig.isInTimeRange(LocalTime.of(5, 0), args));
        assertFalse(ProxyAutoConfig.isInTimeRange(LocalTime.of(12, 0), args));
    }

    /**
     * Test case.
     */
    @Test
    public void timeRangeInvalid() {
        final LocalTime now = LocalTime.of(12, 0);
        assertFalse(ProxyAutoConfig.isInTimeRange(now, List.of()));
        assertFalse(ProxyAutoConfig.isInTimeRange(now, List.of("XYZ")));
        assertFalse(ProxyAutoConfig.isInTimeRange(now, List.of("10", "0", "14")));
        assertFalse(ProxyAutoConfig.isInTimeRange(now, List.of("10", "0", "25", "0")));
        assertFalse(ProxyAutoConfig.isInTimeRange(now, List.of("1", "2", "3", "4", "5", "6", "7")));
    }

    /**
     * Test case.
     */
    @Test
    public void weekdayRangeScriptGmt() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  return weekdayRange('SUN', 'SAT', 'GMT') ? 'DIRECT' : 'PROXY proxy.example.com:8000';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void dateRangeScriptGmt() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  return dateRange('JAN', 'DEC', 'GMT') ? 'DIRECT' : 'PROXY proxy.example.com:8000';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void timeRangeScript() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  return timeRange(0, 0, 0, 23, 59, 59) ? 'DIRECT' : 'PROXY proxy.example.com:8000';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void timeRangeScriptGmt() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  return timeRange(0, 23, 'GMT') ? 'DIRECT' : 'PROXY proxy.example.com:8000';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void simple() {
        final String content = "function FindProxyForURL(url, host) { "
                + "return \"PROXY proxy.example.com:8080; DIRECT\"; }";
        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME, content, URL_FIRST);
        assertEquals("PROXY proxy.example.com:8080; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void someConditions() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (shExpMatch(host,\"*.example.com\")) {\n"
                + "    return \"DIRECT\";\n"
                + "  }\n"

                + "  return \"PROXY proxy.example.com:8000; DIRECT\";\n"
                + "}";

        String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("ftp://www.example.com"));
        assertEquals("DIRECT", value);

        value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME, content, new URL("ftp://example.com"));
        assertEquals("PROXY proxy.example.com:8000; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void bindings() {
        final String content = "ProxyConfig.bindings.com = 'my_com';\n"
            + "ProxyConfig.bindings.org = 'my_org';\n"
            + "ProxyConfig.bindings.net = 'my_net';\n"
            + "ProxyConfig.bindings.edu = 'my_edu';\n"
            + "ProxyConfig.bindings.gov = 'my_gov';\n"
            + "function FindProxyForURL(url, host) {\n"
            + "  var returnValue = '';\n"
            + "  for (var x in ProxyConfig.bindings) {\n"
            + "    returnValue += x + ' ';\n"
            + "  }\n"
            + "  return returnValue;\n"
            + "}\n";
        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME, content, URL_FIRST);
        assertEquals("com org net edu gov ", value);
    }

    /**
     * Test case for #1649.
     */
    @Test
    public void isInNet() {
        final boolean isInNet = ProxyAutoConfig.isInNet("www.1234abcd.abc", "172.16.0.0", "255.240.0.0");
        assertFalse(isInNet);
    }

    /**
     * Test case for #1911.
     */
    @Test
    public void isInNetRange() {
        final boolean isInNet = ProxyAutoConfig.isInNet("172.22.0.7", "172.16.0.0", "255.240.0.0");
        assertTrue(isInNet);
    }

    /**
     * Test case.
     */
    @Test
    public void isPlainHostNameFalse() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (isPlainHostName('www.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("PROXY proxy.example.com:8000; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void isPlainHostNameTrue() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (isPlainHostName('www')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void dnsDomainIsFalse() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (dnsDomainIs('www', '.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("PROXY proxy.example.com:8000; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void dnsDomainIsTrue() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (dnsDomainIs('www.mozilla.org', '.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void localHostOrDomainIsFalseDomain() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (localHostOrDomainIs('www.google.com', 'www.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "   return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("PROXY proxy.example.com:8000; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void localHostOrDomainIsFalseHostname() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (localHostOrDomainIs('home.mozilla.org', 'www.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("PROXY proxy.example.com:8000; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void localHostOrDomainIsTrue() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (localHostOrDomainIs('www.mozilla.org', 'www.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void localHostOrDomainIsTrueNoDomain() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (localHostOrDomainIs('www', 'www.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void isResolvableFalse() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (isResolvable('A17.2547876535.817')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("PROXY proxy.example.com:8000; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void isResolvableTrue() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (isResolvable('www.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void dnsResolve() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  return dnsResolve('www.wetator.org');\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertTrue(value, value.contains("."));
    }

    /**
     * Test case.
     */
    @Test
    public void convertAddr() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  return convert_addr('192.0.2.172');\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("-1073741140", value);
    }

    /**
     * Test case.
     */
    @Test
    public void myIpAddress() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  return myIpAddress();\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertTrue(value, value.contains("."));
    }
}
