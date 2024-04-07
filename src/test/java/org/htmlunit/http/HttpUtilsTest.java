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

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.htmlunit.WebTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.util.NameValuePair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link HttpUtils}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HttpUtilsTest extends WebTestCase {

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

    private static Date createDate(final int year, final Month month, final int day) {
        final Instant instant = LocalDate.of(year, month, day).atStartOfDay(ZoneId.of("GMT")).toInstant();
        return new Date(instant.toEpochMilli());
    }

    @Test
    public void testParseURLCodedContent() throws Exception {
        List<NameValuePair> result;

        result = parseUrlQuery("");
        Assert.assertTrue(result.isEmpty());

        result = parseUrlQuery("Name0");
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name0", null);

        result = parseUrlQuery("Name1=Value1");
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name1", "Value1");

        result = parseUrlQuery("Name2=");
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name2", "");

        result = parseUrlQuery("Name3");
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name3", null);

        result = parseUrlQuery("Name4=Value%204%21");
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name4", "Value 4!");

        result = parseUrlQuery("Name4=Value%2B4%21");
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name4", "Value+4!");

        result = parseUrlQuery("Name4=Value%204%21%20%214");
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name4", "Value 4! !4");

        result = parseUrlQuery("Name5=aaa&Name6=bbb");
        Assert.assertEquals(2, result.size());
        assertNameValuePair(result.get(0), "Name5", "aaa");
        assertNameValuePair(result.get(1), "Name6", "bbb");

        result = parseUrlQuery("Name7=aaa&Name7=b%2Cb&Name7=ccc");
        Assert.assertEquals(3, result.size());
        assertNameValuePair(result.get(0), "Name7", "aaa");
        assertNameValuePair(result.get(1), "Name7", "b,b");
        assertNameValuePair(result.get(2), "Name7", "ccc");

        result = parseUrlQuery("Name8=xx%2C%20%20yy%20%20%2Czz");
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name8", "xx,  yy  ,zz");

        result = parseUrlQuery("price=10%20%E2%82%AC");
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "price", "10 \u20AC");

        result = parseUrlQuery("a=b\"c&d=e");
        Assert.assertEquals(2, result.size());
        assertNameValuePair(result.get(0), "a", "b\"c");
        assertNameValuePair(result.get(1), "d", "e");
    }

    @Test
    public void testParseInvalidURLCodedContent() throws Exception {
        List<NameValuePair> result;

        result = parseUrlQuery("name=%");
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "name", "%");

        result = parseUrlQuery("name=%a");
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "name", "%a");

        result = parseUrlQuery("name=%wa%20");
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "name", "%wa ");
    }

    private static List<NameValuePair> parseUrlQuery(final String params) {
        return HttpUtils.parseUrlQuery(params, StandardCharsets.UTF_8);
    }

    private static void assertNameValuePair(
            final NameValuePair parameter,
            final String expectedName,
            final String expectedValue) {
        Assert.assertEquals(parameter.getName(), expectedName);
        Assert.assertEquals(parameter.getValue(), expectedValue);
    }
}
