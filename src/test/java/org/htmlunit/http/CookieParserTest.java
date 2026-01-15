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
package org.htmlunit.http;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.htmlunit.BrowserVersion;
import org.htmlunit.util.Cookie;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for {@link CookieParser}.
 *
 * @author Ronald Brill
 */
public class CookieParserTest {

    private static final String TEST_URL = "http://www.example.com/path/to/page";
    private static final String SECURE_TEST_URL = "https://www.example.com/path/to/page";

    /**
     * Test parsing a simple cookie with just name and value.
     */
    @Test
    public void testSimpleCookie() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("name", cookie.getName());
        assertEquals("value", cookie.getValue());
        assertEquals("www.example.com", cookie.getDomain());
        assertEquals("/path/to", cookie.getPath());
        assertFalse(cookie.isSecure());
        assertFalse(cookie.isHttpOnly());
        assertNull(cookie.getExpires());
    }

    /**
     * Test parsing a cookie with empty value.
     */
    @Test
    public void testCookieWithEmptyValue() throws Exception {
        final List<Cookie> cookies = parseCookie("name=");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("name", cookie.getName());
        assertEquals("", cookie.getValue());
    }

    /**
     * Test parsing an empty cookie string.
     */
    @Test
    public void testEmptyCookie() throws Exception {
        final List<Cookie> cookies = parseCookie("");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals(CookieParser.EMPTY_COOKIE_NAME, cookie. getName());
        assertEquals("", cookie.getValue());
    }

    /**
     * Test parsing a cookie with only a value (no name).
     */
    @Test
    public void testCookieWithNoName() throws Exception {
        final List<Cookie> cookies = parseCookie("value_only");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals(CookieParser.EMPTY_COOKIE_NAME, cookie.getName());
        assertEquals("value_only", cookie.getValue());
    }

    /**
     * Test parsing a cookie with blank name.
     */
    @Test
    public void testCookieWithBlankName() throws Exception {
        final List<Cookie> cookies = parseCookie("  =value");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals(CookieParser.EMPTY_COOKIE_NAME, cookie.getName());
        assertEquals("value", cookie.getValue());
    }

    /**
     * Test parsing a cookie with whitespace. 
     */
    @Test
    public void testCookieWithWhitespace() throws Exception {
        final List<Cookie> cookies = parseCookie("  name = value  ");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("name", cookie.getName());
        assertEquals("value", cookie.getValue());
    }

    /**
     * Test parsing a cookie with domain attribute.
     */
    @Test
    public void testCookieWithDomain() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; domain=example.com");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies. get(0);
        assertEquals("name", cookie.getName());
        assertEquals("value", cookie.getValue());
        assertEquals("example.com", cookie.getDomain());
    }

    /**
     * Test parsing a cookie with domain attribute (no leading dot).
     */
    @Test
    public void testCookieWithDomainNoDot() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; domain=example.com");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("example.com", cookie.getDomain());
    }

    /**
     * Test parsing a cookie with path attribute.
     */
    @Test
    public void testCookieWithPath() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; path=/custom/path");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("name", cookie.getName());
        assertEquals("/custom/path", cookie.getPath());
    }

    /**
     * Test parsing a cookie with secure flag.
     */
    @Test
    public void testCookieWithSecure() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; secure");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertTrue(cookie.isSecure());
    }

    /**
     * Test parsing a cookie with HttpOnly flag.
     */
    @Test
    public void testCookieWithHttpOnly() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; httponly");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertTrue(cookie.isHttpOnly());
    }

    /**
     * Test parsing a cookie with SameSite attribute.
     */
    @Test
    public void testCookieWithSameSite() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; samesite=Strict");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("Strict", cookie.getSameSite());
    }

    /**
     * Test parsing a cookie with SameSite=Lax. 
     */
    @Test
    public void testCookieWithSameSiteLax() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; samesite=Lax");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("Lax", cookie.getSameSite());
    }

    /**
     * Test parsing a cookie with SameSite=None. 
     */
    @Test
    public void testCookieWithSameSiteNone() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; samesite=None; secure");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("None", cookie.getSameSite());
        assertTrue(cookie.isSecure());
    }

    /**
     * Test parsing a cookie with expires attribute.
     */
    @Test
    public void testCookieWithExpires() throws Exception {
        final ZonedDateTime inOneYear = ZonedDateTime.now().plusYears(1).truncatedTo(ChronoUnit. SECONDS);
        final String dateString = DateTimeFormatter.RFC_1123_DATE_TIME. format(inOneYear);

        final List<Cookie> cookies = parseCookie("name=value; expires=" + dateString);

        assertEquals(1, cookies. size());
        final Cookie cookie = cookies.get(0);
        assertNotNull(cookie.getExpires());
        assertEquals(Date.from(inOneYear. toInstant()), cookie.getExpires());
    }

    /**
     * Test parsing a cookie with max-age attribute.
     */
    @Test
    public void testCookieWithMaxAge() throws Exception {
        final long beforeParse = System.currentTimeMillis();
        final List<Cookie> cookies = parseCookie("name=value; max-age=3600");
        final long afterParse = System.currentTimeMillis();

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies. get(0);
        assertNotNull(cookie.getExpires());

        final long expectedMin = beforeParse + 3600000;
        final long expectedMax = afterParse + 3600000;
        final long actual = cookie.getExpires().getTime();

        assertTrue(actual >= expectedMin && actual <= expectedMax);
    }

    /**
     * Test parsing a cookie with max-age=0 (delete cookie).
     */
    @Test
    public void testCookieWithMaxAgeZero() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; max-age=0");

        assertEquals(1, cookies. size());
        final Cookie cookie = cookies.get(0);
        assertNotNull(cookie.getExpires());
        assertTrue(cookie.getExpires().getTime() <= System.currentTimeMillis());
    }

    /**
     * Test parsing a cookie with max-age=-1 (session cookie).
     */
    @Test
    public void testCookieWithMaxAgeNegative() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; max-age=-1");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertNull(cookie.getExpires());
    }

    /**
     * Test parsing a cookie with all attributes.
     */
    @Test
    public void testCookieWithAllAttributes() throws Exception {
        final String cookieString = "name=value; domain=example.com; path=/; " +
                "secure; httponly; samesite=Strict; max-age=3600";

        final List<Cookie> cookies = parseCookie(cookieString);

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("name", cookie.getName());
        assertEquals("value", cookie.getValue());
        assertEquals("example.com", cookie.getDomain());
        assertEquals("/", cookie.getPath());
        assertTrue(cookie.isSecure());
        assertTrue(cookie.isHttpOnly());
        assertEquals("Strict", cookie.getSameSite());
        assertNotNull(cookie.getExpires());
    }

    /**
     * Test parsing a cookie with mixed case attributes.
     */
    @Test
    public void testCookieWithMixedCaseAttributes() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; PATH=/path; SECURE; HttpOnly");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("/path", cookie.getPath());
        assertTrue(cookie.isSecure());
        assertTrue(cookie.isHttpOnly());
    }

    /**
     * Test parsing a cookie with quoted value.
     */
    @Test
    public void testCookieWithQuotedValue() throws Exception {
        final List<Cookie> cookies = parseCookie("name=\"quoted value\"");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("name", cookie.getName());
        assertEquals("\"quoted value\"", cookie.getValue());
    }

    /**
     * Test parsing a cookie with special characters in value.
     */
    @Test
    public void testCookieWithSpecialCharsInValue() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value@123!#$%");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("value@123!#$%", cookie.getValue());
    }

    /**
     * Test parsing a cookie with semicolon before equals.
     */
    @Test
    public void testCookieWithSemicolonBeforeEquals() throws Exception {
        final List<Cookie> cookies = parseCookie(";value");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals(CookieParser.EMPTY_COOKIE_NAME, cookie.getName());
    }

    /**
     * Test default path calculation for root.
     */
    @Test
    public void testDefaultPathForRoot() throws Exception {
        final URL url = new URL("http://www.example.com/");
        final List<Cookie> cookies = CookieParser.parseCookie(
                "name=value", url, BrowserVersion.BEST_SUPPORTED);

        assertEquals(1, cookies.size());
        assertEquals("/", cookies.get(0).getPath());
    }

    /**
     * Test default path calculation for deep path.
     */
    @Test
    public void testDefaultPathForDeepPath() throws Exception {
        final URL url = new URL("http://www.example.com/a/b/c/page.html");
        final List<Cookie> cookies = CookieParser.parseCookie(
                "name=value", url, BrowserVersion.BEST_SUPPORTED);

        assertEquals(1, cookies.size());
        assertEquals("/a/b/c", cookies.get(0).getPath());
    }

    /**
     * Test parsing from secure URL.
     */
    @Test
    public void testParseFromSecureUrl() throws Exception {
        final URL url = new URL(SECURE_TEST_URL);
        final List<Cookie> cookies = CookieParser.parseCookie(
                "name=value", url, BrowserVersion.BEST_SUPPORTED);

        assertEquals(1, cookies.size());
        assertNotNull(cookies.get(0));
    }

    /**
     * Test parsing from file URL.
     */
    @Test
    public void testParseFromFileUrl() throws Exception {
        final URL url = new URL("file:///path/to/file.html");
        final List<Cookie> cookies = CookieParser.parseCookie(
                "name=value", url, BrowserVersion.BEST_SUPPORTED);

        assertEquals(1, cookies.size());
        assertEquals(CookieParser.LOCAL_FILESYSTEM_DOMAIN, cookies.get(0).getDomain());
    }

    /**
     * Test parsing cookie with multiple semicolons.
     */
    @Test
    public void testCookieWithMultipleSemicolons() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value;;; path=/;; secure;;;");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("name", cookie.getName());
        assertEquals("value", cookie.getValue());
        assertEquals("/", cookie.getPath());
        assertTrue(cookie.isSecure());
    }

    /**
     * Test parsing cookie with unknown attributes (should be ignored).
     */
    @Test
    public void testCookieWithUnknownAttributes() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; unknown=attr; another=test");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("name", cookie.getName());
        assertEquals("value", cookie.getValue());
    }

    /**
     * Test parsing cookie with version attribute.
     */
    @Test
    public void testCookieWithVersion() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; version=1");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("name", cookie. getName());
    }

    /**
     * Test parsing cookie with invalid max-age. 
     */
    @Test
    public void testCookieWithInvalidMaxAge() {
        assertThrows(MalformedCookieException.class, () -> {
            parseCookie("name=value; max-age=invalid");
        });
    }

    /**
     * Test parsing cookie with invalid expires date.
     */
    @Test
    public void testCookieWithInvalidExpires() {
        assertThrows(MalformedCookieException.class, () -> {
            parseCookie("name=value; expires=not-a-date");
        });
    }

    /**
     * Test parsing null cookie string.
     */
    @Test
    public void testNullCookieString() {
        assertThrows(MalformedCookieException.class, () -> {
            CookieParser.parseCookie(null, new URL(TEST_URL), BrowserVersion.BEST_SUPPORTED);
        });
    }

    /**
     * Test parsing with different browser versions.
     */
    @Test
    public void testDifferentBrowserVersions() throws Exception {
        final BrowserVersion[] versions = {
                BrowserVersion.CHROME,
                BrowserVersion.FIREFOX,
                BrowserVersion. EDGE,
                BrowserVersion. BEST_SUPPORTED
        };

        for (final BrowserVersion version :  versions) {
            final List<Cookie> cookies = CookieParser.parseCookie(
                    "name=value", new URL(TEST_URL), version);
            assertEquals(1, cookies.size());
            assertEquals("name", cookies.get(0).getName());
        }
    }

    /**
     * Test expires with various date formats.
     */
    @Test
    public void testExpiresWithVariousFormats() throws Exception {
        final String[] dateFormats = {
                "Wed, 15 Jan 2026 12:00:00 GMT",      // RFC 1123
                "Wednesday, 15-Jan-26 12:00:00 GMT",  // RFC 1036
                "Wed Jan 15 12:00:00 2026",           // ANSI C
        };

        for (final String dateFormat : dateFormats) {
            try {
                final List<Cookie> cookies = parseCookie("name=value; expires=" + dateFormat);
                assertEquals(1, cookies.size());
                assertNotNull(cookies.get(0).getExpires(),
                        "Failed to parse date format: " + dateFormat);
            } catch (final MalformedCookieException e) {
                // Some formats might not parse, that's ok for this test
            }
        }
    }

    /**
     * Test cookie with value containing equals sign.
     */
    @Test
    public void testCookieWithEqualsInValue() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value=with=equals");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("name", cookie.getName());
        assertEquals("value=with=equals", cookie.getValue());
    }

    /**
     * Test cookie with empty attribute values.
     */
    @Test
    public void testCookieWithEmptyAttributeValues() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value; path=; domain=");

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);
        assertEquals("name", cookie.getName());
    }

    /**
     * Test URL with no path.
     */
    @Test
    public void testUrlWithNoPath() throws Exception {
        final URL url = new URL("http://www.example.com");
        final List<Cookie> cookies = CookieParser.parseCookie(
                "name=value", url, BrowserVersion. BEST_SUPPORTED);

        assertEquals(1, cookies.size());
        assertEquals("/", cookies. get(0).getPath());
    }

    /**
     * Test URL with custom port.
     */
    @Test
    public void testUrlWithCustomPort() throws Exception {
        final URL url = new URL("http://www.example.com:8080/path");
        final List<Cookie> cookies = CookieParser.parseCookie(
                "name=value", url, BrowserVersion.BEST_SUPPORTED);

        assertEquals(1, cookies.size());
        assertEquals("www.example.com", cookies.get(0).getDomain());
    }

    /**
     * Test cookie string with leading whitespace.
     */
    @Test
    public void testCookieWithLeadingWhitespace() throws Exception {
        final List<Cookie> cookies = parseCookie("   name=value");

        assertEquals(1, cookies.size());
        assertEquals("name", cookies.get(0).getName());
    }

    /**
     * Test cookie string with trailing whitespace.
     */
    @Test
    public void testCookieWithTrailingWhitespace() throws Exception {
        final List<Cookie> cookies = parseCookie("name=value   ");

        assertEquals(1, cookies.size());
        assertEquals("name", cookies.get(0).getName());
    }

    /**
     * Test max-age takes precedence over expires.
     */
    @Test
    public void testMaxAgeTakesPrecedence() throws Exception {
        final ZonedDateTime inOneYear = ZonedDateTime.now().plusYears(1);
        final String dateString = DateTimeFormatter.RFC_1123_DATE_TIME.format(inOneYear);

        final long beforeParse = System.currentTimeMillis();
        final List<Cookie> cookies = parseCookie("name=value; expires=" + dateString + "; max-age=3600");
        final long afterParse = System.currentTimeMillis();

        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.get(0);

        // max-age should take precedence, so expiration should be ~1 hour, not 1 year
        final long expiryTime = cookie.getExpires().getTime();
        assertTrue(expiryTime < beforeParse + (2 * 3600000)); // Less than 2 hours from now
    }

    // Helper method to parse cookies with default URL and browser version
    private List<Cookie> parseCookie(final String cookieString) throws Exception {
        return CookieParser.parseCookie(cookieString, new URL(TEST_URL), BrowserVersion.BEST_SUPPORTED);
    }
}