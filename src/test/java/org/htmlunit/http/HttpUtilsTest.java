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
package org.htmlunit.http;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
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

        result = HttpUtils.parseUrlQuery("", StandardCharsets.UTF_8);
        Assert.assertTrue(result.isEmpty());

        result = HttpUtils.parseUrlQuery("Name0", StandardCharsets.UTF_8);
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name0", null);

        result = HttpUtils.parseUrlQuery("Name1=Value1", StandardCharsets.UTF_8);
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name1", "Value1");

        result = HttpUtils.parseUrlQuery("Name2=", StandardCharsets.UTF_8);
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name2", "");

        result = HttpUtils.parseUrlQuery("Name3", StandardCharsets.UTF_8);
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name3", null);

        result = HttpUtils.parseUrlQuery("Name4=Value%204%21", StandardCharsets.UTF_8);
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name4", "Value 4!");

        result = HttpUtils.parseUrlQuery("Name4=Value%2B4%21", StandardCharsets.UTF_8);
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name4", "Value+4!");

        result = HttpUtils.parseUrlQuery("Name4=Value%204%21%20%214", StandardCharsets.UTF_8);
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name4", "Value 4! !4");

        result = HttpUtils.parseUrlQuery("Name5=aaa&Name6=bbb", StandardCharsets.UTF_8);
        Assert.assertEquals(2, result.size());
        assertNameValuePair(result.get(0), "Name5", "aaa");
        assertNameValuePair(result.get(1), "Name6", "bbb");

        result = HttpUtils.parseUrlQuery("Name7=aaa&Name7=b%2Cb&Name7=ccc", StandardCharsets.UTF_8);
        Assert.assertEquals(3, result.size());
        assertNameValuePair(result.get(0), "Name7", "aaa");
        assertNameValuePair(result.get(1), "Name7", "b,b");
        assertNameValuePair(result.get(2), "Name7", "ccc");

        result = HttpUtils.parseUrlQuery("Name8=xx%2C%20%20yy%20%20%2Czz", StandardCharsets.UTF_8);
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "Name8", "xx,  yy  ,zz");

        result = HttpUtils.parseUrlQuery("price=10%20%E2%82%AC", StandardCharsets.UTF_8);
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "price", "10 \u20AC");

        result = HttpUtils.parseUrlQuery("a=b\"c&d=e", StandardCharsets.UTF_8);
        Assert.assertEquals(2, result.size());
        assertNameValuePair(result.get(0), "a", "b\"c");
        assertNameValuePair(result.get(1), "d", "e");
    }

    @Test
    public void testParseInvalidURLCodedContent() throws Exception {
        List<NameValuePair> result;

        result = HttpUtils.parseUrlQuery("name=%", StandardCharsets.UTF_8);
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "name", "%");

        result = HttpUtils.parseUrlQuery("name=%a", StandardCharsets.UTF_8);
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "name", "%a");

        result = HttpUtils.parseUrlQuery("name=%wa%20", StandardCharsets.UTF_8);
        Assert.assertEquals(1, result.size());
        assertNameValuePair(result.get(0), "name", "%wa ");
    }

    private static void assertNameValuePair(
            final NameValuePair parameter,
            final String expectedName,
            final String expectedValue) {
        Assert.assertEquals(parameter.getName(), expectedName);
        Assert.assertEquals(parameter.getValue(), expectedValue);
    }

    @Test
    public void toQueryFormFields() {
        final List<NameValuePair> params = new ArrayList<>();
        Assert.assertEquals("", HttpUtils.toQueryFormFields(params, StandardCharsets.US_ASCII));

        params.clear();
        params.add(new NameValuePair("Name0", null));
        Assert.assertEquals("Name0", HttpUtils.toQueryFormFields(params, StandardCharsets.US_ASCII));

        params.clear();
        params.add(new NameValuePair("Name1", "Value1"));
        Assert.assertEquals("Name1=Value1", HttpUtils.toQueryFormFields(params, StandardCharsets.US_ASCII));

        params.clear();
        params.add(new NameValuePair("Name2", ""));
        Assert.assertEquals("Name2=", HttpUtils.toQueryFormFields(params, StandardCharsets.US_ASCII));

        params.clear();
        params.add(new NameValuePair("Name4", "Value 4&"));
        Assert.assertEquals("Name4=Value+4%26", HttpUtils.toQueryFormFields(params, StandardCharsets.US_ASCII));

        params.clear();
        params.add(new NameValuePair("Name4", "Value+4&"));
        Assert.assertEquals("Name4=Value%2B4%26", HttpUtils.toQueryFormFields(params, StandardCharsets.US_ASCII));

        params.clear();
        params.add(new NameValuePair("Name4", "Value 4& =4"));
        Assert.assertEquals("Name4=Value+4%26+%3D4", HttpUtils.toQueryFormFields(params, StandardCharsets.US_ASCII));

        params.clear();
        params.add(new NameValuePair("Name5", "aaa"));
        params.add(new NameValuePair("Name6", "bbb"));
        Assert.assertEquals("Name5=aaa&Name6=bbb", HttpUtils.toQueryFormFields(params, StandardCharsets.US_ASCII));

        params.clear();
        params.add(new NameValuePair("Name7", "aaa"));
        params.add(new NameValuePair("Name7", "b,b"));
        params.add(new NameValuePair("Name7", "ccc"));
        Assert.assertEquals("Name7=aaa&Name7=b%2Cb&Name7=ccc",
                HttpUtils.toQueryFormFields(params, StandardCharsets.US_ASCII));

        params.clear();
        params.add(new NameValuePair("Name8", "xx,  yy  ,zz"));
        Assert.assertEquals("Name8=xx%2C++yy++%2Czz", HttpUtils.toQueryFormFields(params, StandardCharsets.US_ASCII));
    }
}
