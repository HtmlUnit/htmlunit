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
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import org.htmlunit.WebTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for issues reported by Google OSS-Fuzz
 * (https://github.com/google/oss-fuzz).
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HttpUtilsTest extends WebTestCase {

    private static Date createDate(final int year, final Month month, final int day) {
        final Instant instant = LocalDate.of(year, month, day).atStartOfDay(ZoneId.of("GMT")).toInstant();
        return new Date(instant.toEpochMilli());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testBasicDateParse() {
        final Date expected = createDate(2004, Month.NOVEMBER, 13);
        Assert.assertEquals(expected, HttpUtils.parseDate("Sat, 13 Nov 2004 00:00:00 GMT"));
        Assert.assertEquals(expected, HttpUtils.parseDate("Saturday, 13 Nov 2004 00:00:00 GMT"));
        Assert.assertEquals(expected, HttpUtils.parseDate("Sat, 13-Nov-2004 00:00:00 GMT"));
        Assert.assertEquals(expected, HttpUtils.parseDate("Saturday, 13-Nov-2004 00:00:00 GMT"));
        Assert.assertEquals(expected, HttpUtils.parseDate("Sat, 13 Nov 2004 00:00:00 GMT"));
    }

    @Test
    public void testMalformedDate() {
        Assert.assertNull(HttpUtils.parseDate("Sat, 13 Nov 2004 00:00:00 GMD"));
        Assert.assertNull(HttpUtils.parseDate("Die Feb 22 17:20:18 2024"));
        Assert.assertNull(HttpUtils.parseDate("Thu Feb 22 17:20;18 2024"));
    }

    @Test
    public void testParseInvalid() {
        Assert.assertNull(HttpUtils.parseDate("Friday, 13-Nov-04 00:00:00 GMT"));
    }

    @Test
    public void testParseNull() {
        Assert.assertNull(HttpUtils.parseDate(null));
    }

    @Test
    public void testTwoDigitYearDateParse() {
        Assert.assertEquals(createDate(2004, Month.NOVEMBER, 13),
                HttpUtils.parseDate("Saturday, 13-Nov-04 00:00:00 GMT"));

        Assert.assertEquals(createDate(2074, Month.NOVEMBER, 13),
                HttpUtils.parseDate("Tuesday, 13-Nov-74 00:00:00 GMT"));
    }

    @Test
    public void testParseQuotedDate() {
        Assert.assertEquals(createDate(2004, Month.NOVEMBER, 13),
                HttpUtils.parseDate("'Sat, 13 Nov 2004 00:00:00 GMT'"));
    }

    @Test
    public void testFormatDate() {
        Assert.assertEquals("Sat, 13 Nov 2004 00:00:00 GMT",
                HttpUtils.formatDate(createDate(2004, Month.NOVEMBER, 13)));
    }
}
