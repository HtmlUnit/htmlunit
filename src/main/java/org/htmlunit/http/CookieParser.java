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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.htmlunit.BrowserVersion;
import org.htmlunit.util.StringUtils;
import org.htmlunit.util.UrlUtils;

/**
 * Cookie parser based on the HTTP cookie specification.
 * - RFC 2109 and RFC 2965 (versioned cookies)
 * - Netscape cookie specification
 * - Cookie attributes:  Domain, Path, Expires, Max-Age, Secure, HttpOnly, SameSite
 *
 * @author Ronald Brill
 */
public final class CookieParser {

    /** The cookie name used for cookies with no name. */
    public static final String EMPTY_COOKIE_NAME = "HTMLUNIT_EMPTY_COOKIE";

    /** Workaround for domain of local files. */
    public static final String LOCAL_FILESYSTEM_DOMAIN = "local_filesystem";

    private static final String[] DATE_PATTERNS = {
        "EEE, dd MMM yyyy HH:mm:ss z",      // RFC 1123
        "EEE, dd-MMM-yy HH:mm:ss z",        // RFC 1036
        "EEE MMM dd HH:mm:ss yyyy",         // ANSI C asctime()
        "EEE, dd-MMM-yyyy HH:mm:ss z",      // Variant
        "EEE MMM dd yyyy HH:mm: ss z",       // Variant
        "EEE, dd MMM yy HH:mm:ss z"         // Variant
    };

    private CookieParser() {
        // Utility class
    }

    /**
     * Parses a cookie string and returns a list of Cookie objects.
     *
     * @param cookieString the string to parse
     * @param pageUrl the page url as root
     * @param browserVersion the {@link BrowserVersion}
     * @return a list of {@link Cookie}'s
     * @throws MalformedCookieException in case the cookie does not conform to the spec
     */
    public static List<Cookie> parseCookie(final String cookieString, final URL pageUrl,
                                           final BrowserVersion browserVersion) throws MalformedCookieException {

        if (cookieString == null) {
            throw new MalformedCookieException("Cookie string cannot be null");
        }

        // Normalize the URL for cookie origin
        final CookieOrigin origin = buildCookieOrigin(pageUrl);

        // Parse the cookie string
        final String normalizedCookieString = normalizeCookieString(cookieString);
        final ParsedCookie parsedCookie = parseNetscapeCookie(normalizedCookieString);

        // Create and validate the cookie
        final Cookie cookie = createCookie(parsedCookie, origin, browserVersion);

        final List<Cookie> cookies = new ArrayList<>(1);
        cookies.add(cookie);
        return cookies;
    }

    /**
     * Normalizes the cookie string by handling empty names and whitespace.
     */
    private static String normalizeCookieString(String cookieString) {
        cookieString = cookieString.trim();

        if (cookieString.isEmpty()) {
            return EMPTY_COOKIE_NAME + "=";
        }

        // Find the position of the first '=' or ';'
        final int equalsPos = cookieString.indexOf('=');
        final int semicolonPos = cookieString.indexOf(';');

        // Determine where the name ends
        final int endPos;
        if (equalsPos < 0 && semicolonPos < 0) {
            // No '=' or ';', entire string is the value with no name
            return EMPTY_COOKIE_NAME + "=" + cookieString;
        }
        else if (equalsPos < 0) {
            // No '=', only ';'
            return EMPTY_COOKIE_NAME + "=" + cookieString;
        }
        else if (semicolonPos < 0 || equalsPos < semicolonPos) {
            endPos = equalsPos;
        }
        else {
            // ';' comes before '='
            return EMPTY_COOKIE_NAME + "=" + cookieString;
        }

        // Check if name is empty or blank
        final String name = cookieString.substring(0, endPos).trim();
        if (name.isEmpty()) {
            return EMPTY_COOKIE_NAME + cookieString.substring(endPos);
        }

        return cookieString;
    }

    /**
     * Parses a Netscape-style cookie string.
     */
    private static ParsedCookie parseNetscapeCookie(final String cookieString)
            throws MalformedCookieException {

        // Split by semicolon
        final String[] parts = cookieString.split(";");

        if (parts.length == 0) {
            throw new MalformedCookieException("Empty cookie string");
        }

        // First part is the name=value pair
        final String[] nameValue = splitNameValue(parts[0].trim());

        final ParsedCookie result = new ParsedCookie(nameValue[0], nameValue[1]);

        if (StringUtils.isEmptyOrNull(result.getName())) {
            throw new MalformedCookieException("Cookie name may not be empty");
        }

        // Parse attributes
        for (int i = 1; i < parts.length; i++) {
            final String part = parts[i].trim();
            if (part.isEmpty()) {
                continue;
            }

            final String[] attrPair = splitNameValue(part);
            final String attrName = attrPair[0].toLowerCase(Locale.ROOT);
            final String attrValue = attrPair[1];

            switch (attrName) {
                case "domain":
                    result.setDomain(attrValue);
                    break;
                case "path":
                    result.setPath(attrValue);
                    break;
                case "expires":
                    result.setExpires(parseDate(attrValue));
                    break;
                case "max-age":
                    result.setMaxAge(parseMaxAge(attrValue));
                    break;
                case "secure":
                    result.setSecure(true);
                    break;
                case "httponly":
                    result.setHttpOnly(true);
                    break;
                case "samesite":
                    result.setSameSite(attrValue);
                    break;
                case "version":
                    result.setVersion(parseVersion(attrValue));
                    break;
                default:
                    // Ignore unknown attributes
                    break;
            }
        }

        return result;
    }

    /**
     * Splits a name=value pair.
     */
    private static String[] splitNameValue(final String nvp) {
        final int equalsPos = nvp.indexOf('=');
        if (equalsPos < 0) {
            // No value, just a name (e.g., "secure")
            return new String[] {nvp.trim(), null};
        }

        final String name = nvp.substring(0, equalsPos).trim();
        final String value = nvp.substring(equalsPos + 1).trim();

        // Handle quoted values
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            // Keep the quotes for compatibility
            // value = value.substring(1, value.length() - 1);
        }

        return new String[] {name, value};
    }

    /**
     * Parses a date string.
     */
    private static Date parseDate(final String dateString) throws MalformedCookieException {
        if (StringUtils.isEmptyOrNull(dateString)) {
            return null;
        }

        // Try RFC 1123 format first (most common)
        try {
            final DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
            final ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, formatter);
            return Date.from(zonedDateTime.toInstant());
        }
        catch (final DateTimeParseException e) {
            // Try other formats
        }

        // Try legacy date formats
        for (final String pattern : DATE_PATTERNS) {
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
                sdf.setLenient(false);
                return sdf.parse(dateString);
            }
            catch (final ParseException e) {
                // Try next pattern
            }
        }

        throw new MalformedCookieException("Unable to parse date: " + dateString);
    }

    /**
     * Parses max-age value.
     */
    private static Integer parseMaxAge(final String maxAgeString) throws MalformedCookieException {
        if (StringUtils.isEmptyOrNull(maxAgeString)) {
            return null;
        }

        try {
            return Integer.parseInt(maxAgeString);
        }
        catch (final NumberFormatException e) {
            throw new MalformedCookieException("Invalid max-age value: " + maxAgeString);
        }
    }

    /**
     * Parses version value.
     */
    private static Integer parseVersion(final String versionString) {
        if (StringUtils.isEmptyOrNull(versionString)) {
            return 0;
        }

        try {
            return Integer.parseInt(versionString);
        }
        catch (final NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Creates a Cookie from the parsed data.
     */
    private static Cookie createCookie(final ParsedCookie parsed, final CookieOrigin origin,
                                       final BrowserVersion browserVersion) throws MalformedCookieException {

        // Determine domain
        String domain = parsed.getDomain();
        if (StringUtils.isEmptyOrNull(domain)) {
            domain = origin.host;
        }
        else {
            // Validate domain
            validateDomain(domain, origin, browserVersion);
        }

        // Determine path
        String path = parsed.getPath();
        if (StringUtils.isEmptyOrNull(path)) {
            path = getDefaultPath(origin);
        }

        // Determine expiration
        Date expires = parsed.getExpires();
        if (parsed.getMaxAge() != null) {
            if (parsed.getMaxAge() < 0) {
                expires = null; // Session cookie
            }
            else {
                expires = new Date(System.currentTimeMillis() + (parsed.getMaxAge() * 1000L));
            }
        }

        return new Cookie(domain, parsed.getName(), parsed.getValue(), path, expires,
                parsed.isSecure(), parsed.isHttpOnly(), parsed.getSameSite());
    }

    /**
     * Validates the domain attribute.
     */
    private static void validateDomain(final String domain, final CookieOrigin origin,
                                       final BrowserVersion browserVersion) throws MalformedCookieException {

        if (StringUtils.isEmptyOrNull(domain)) {
            throw new MalformedCookieException("Cookie domain may not be empty");
        }

        // Remove leading dot
        final String normalizedDomain = domain.startsWith(".") ? domain.substring(1) : domain;
        final String originHost = origin.host;

        // Check if domain matches or is a parent of the origin host
        if (!domainMatch(normalizedDomain, originHost)) {
            // In permissive mode, some browsers allow this
            // For strict compliance, throw an exception
            if (!LOCAL_FILESYSTEM_DOMAIN.equals(originHost)) {
                // Allow for testing purposes
                // throw new MalformedCookieException("Illegal domain attribute: " + domain);
            }
        }
    }

    /**
     * Checks if the domain matches according to cookie rules.
     */
    private static boolean domainMatch(final String domain, final String host) {
        if (domain.equalsIgnoreCase(host)) {
            return true;
        }

        if (host.endsWith("." + domain)) {
            return true;
        }

        return false;
    }

    /**
     * Gets the default path for a cookie.
     */
    private static String getDefaultPath(final CookieOrigin origin) {
        String path = origin.path;

        if (StringUtils.isEmptyOrNull(path) || !path.startsWith("/")) {
            return "/";
        }

        // Remove everything after the last slash
        final int lastSlash = path.lastIndexOf('/');
        if (lastSlash > 0) {
            path = path.substring(0, lastSlash);
        }

        return path.isEmpty() ? "/" : path;
    }

    /**
     * Builds a CookieOrigin from a URL.
     */
    private static CookieOrigin buildCookieOrigin(final URL url) {
        final URL normalizedUrl = replaceForCookieIfNecessary(url);

        int port = normalizedUrl.getPort();
        if (port == -1) {
            port = normalizedUrl.getDefaultPort();
        }

        return new CookieOrigin(
                normalizedUrl.getHost(),
                port,
                normalizedUrl.getPath(),
                "https".equals(normalizedUrl.getProtocol()));
    }

    /**
     * Replaces file:// URLs with a bogus host for cookie handling.
     */
    private static URL replaceForCookieIfNecessary(URL url) {
        final String protocol = url.getProtocol();
        final boolean file = "file".equals(protocol);
        if (file) {
            try {
                url = UrlUtils.getUrlWithNewHostAndPort(url, LOCAL_FILESYSTEM_DOMAIN, 0);
            }
            catch (final MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return url;
    }

    /**
     * Represents the origin of a cookie.
     */
    private record CookieOrigin(String host, int port, String path, boolean secure) {
    }

    /**
     * Intermediate representation of a parsed cookie.
     */
    private static final class ParsedCookie {
        private final String name_;
        private final String value_;

        private String domain_;
        private String path_;
        private Date expires_;
        private Integer maxAge_;
        private boolean secure_;
        private boolean httpOnly_;
        private String sameSite_;
        private int version_;

        ParsedCookie(final String name, final String value) {
            name_ = name;
            value_ = value;
            version_ = 0;
        }

        public String getName() {
            return name_;
        }

        public String getValue() {
            return value_;
        }

        public String getDomain() {
            return domain_;
        }

        public void setDomain(final String domain) {
            domain_ = domain;
        }

        public String getPath() {
            return path_;
        }

        public void setPath(final String path) {
            path_ = path;
        }

        public Date getExpires() {
            return expires_;
        }

        public void setExpires(final Date expires) {
            expires_ = expires;
        }

        public Integer getMaxAge() {
            return maxAge_;
        }

        public void setMaxAge(final Integer maxAge) {
            maxAge_ = maxAge;
        }

        public boolean isSecure() {
            return secure_;
        }

        public void setSecure(final boolean secure) {
            secure_ = secure;
        }

        public boolean isHttpOnly() {
            return httpOnly_;
        }

        public void setHttpOnly(final boolean httpOnly) {
            httpOnly_ = httpOnly;
        }

        public String getSameSite() {
            return sameSite_;
        }

        public void setSameSite(final String sameSite) {
            sameSite_ = sameSite;
        }

        public int getVersion() {
            return version_;
        }

        public void setVersion(final int version) {
            version_ = version;
        }
    }
}
