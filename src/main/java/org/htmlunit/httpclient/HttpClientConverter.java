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
package org.htmlunit.httpclient;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;
import org.htmlunit.BrowserVersion;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.UrlUtils;

/**
 * Helper methods to convert from/to HttpClient.
 *
 * @author Ronald Brill
 */
public final class HttpClientConverter {

    /** Forwarder to HttpStatus.SC_OK. */
    public static final int OK = HttpStatus.SC_OK;

    /** Forwarder to HttpStatus.SC_NO_CONTENT. */
    public static final int NO_CONTENT = HttpStatus.SC_NO_CONTENT;

    /** Forwarder to HttpStatus.MULTIPLE_CHOICES. */
    public static final int MULTIPLE_CHOICES = HttpStatus.SC_MULTIPLE_CHOICES;

    /** Forwarder to HttpStatus.MOVED_PERMANENTLY. */
    public static final int MOVED_PERMANENTLY = HttpStatus.SC_MOVED_PERMANENTLY;

    /** Forwarder to HttpStatus.MOVED_TEMPORARILY. */
    public static final int MOVED_TEMPORARILY = HttpStatus.SC_MOVED_TEMPORARILY;

    /** Forwarder to HttpStatus.SEE_OTHER. */
    public static final int SEE_OTHER = HttpStatus.SC_SEE_OTHER;

    /** Forwarder to HttpStatus.TEMPORARY_REDIRECT. */
    public static final int TEMPORARY_REDIRECT = HttpStatus.SC_TEMPORARY_REDIRECT;

    /** 308. */
    public static final int PERMANENT_REDIRECT = 308;

    /** Forwarder to HttpStatus.NOT_MODIFIED. */
    public static final int NOT_MODIFIED = HttpStatus.SC_NOT_MODIFIED;

    /** Forwarder to HttpStatus.SC_USE_PROXY. */
    public static final int USE_PROXY = HttpStatus.SC_USE_PROXY;

    /** Forwarder to HttpStatus.SC_FORBIDDEN. */
    public static final int FORBIDDEN = HttpStatus.SC_FORBIDDEN;

    /** Forwarder to HttpStatus.SC_NOT_FOUND. */
    public static final int NOT_FOUND = HttpStatus.SC_NOT_FOUND;

    /** Forwarder to HttpStatus.SC_INTERNAL_SERVER_ERROR. */
    public static final int INTERNAL_SERVER_ERROR = HttpStatus.SC_INTERNAL_SERVER_ERROR;

    private HttpClientConverter() {
    }

    /**
     * Converts the specified name/value pairs into HttpClient name/value pairs.
     * @param pairs the name/value pairs to convert
     * @return the converted name/value pairs
     */
    public static List<org.apache.http.NameValuePair> nameValuePairsToHttpClient(final List<NameValuePair> pairs) {
        final List<org.apache.http.NameValuePair> resultingPairs = new ArrayList<>(pairs.size());
        for (final NameValuePair pair : pairs) {
            resultingPairs.add(new BasicNameValuePair(pair.getName(), pair.getValue()));
        }
        return resultingPairs;
    }

    /**
     * Pares url query into name/value pairs using methods from HttpClient.
     * @param query the urlencoded query
     * @param charset the charset or null (defaulting to utf-8)
     * @return the name/value pairs
     */
    public static List<NameValuePair> parseUrlQuery(final String query, final Charset charset) {
        final List<org.apache.http.NameValuePair> pairs = URLEncodedUtils.parse(query, charset);

        final List<NameValuePair> resultingPairs = new ArrayList<>();
        for (final org.apache.http.NameValuePair pair : pairs) {
            resultingPairs.add(new NameValuePair(pair.getName(), pair.getValue()));
        }
        return resultingPairs;
    }

    /**
     * @param parameters the paramters
     * @param enc the charset
     * @return the query string from the given parameters
     */
    public static String toQueryFormFields(final List<NameValuePair> parameters, final Charset enc) {
        return URLEncodedUtils.format(nameValuePairsToHttpClient(parameters), enc);
    }

    /**
     * Parses the specified date string, assuming that it is formatted according to RFC 1123, RFC 1036 or as an ANSI
     * C HTTP date header. This method returns {@code null} if the specified string is {@code null} or unparseable.
     *
     * @param s the string to parse as a date
     * @return the date version of the specified string, or {@code null}
     */
    public static Date parseHttpDate(final String s) {
        if (s == null) {
            return null;
        }
        return DateUtils.parseDate(s);
    }

    /**
     * Formats the given date according to the RFC 1123 pattern.
     *
     * @param date The date to format.
     * @return An RFC 1123 formatted date string.
     */
    public static String formatDate(final Date date) {
        return DateUtils.formatDate(date);
    }

    /**
     * @param e the exception to check
     * @return true if the provided Exception is na {@link NoHttpResponseException}
     */
    public static boolean isNoHttpResponseException(final Exception e) {
        return e instanceof NoHttpResponseException;
    }

    /**
     * Helper that builds a CookieOrigin.
     * @param url the url to be used
     * @return the new CookieOrigin
     */
    public static CookieOrigin buildCookieOrigin(final URL url) {
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
     * {@link CookieOrigin} doesn't like empty hosts and negative ports,
     * but these things happen if we're dealing with a local file.
     * This method allows us to work around this limitation in HttpClient by feeding it a bogus host and port.
     *
     * @param url the URL to replace if necessary
     * @return the replacement URL, or the original URL if no replacement was necessary
     */
    public static URL replaceForCookieIfNecessary(URL url) {
        final String protocol = url.getProtocol();
        final boolean file = "file".equals(protocol);
        if (file) {
            try {
                url = UrlUtils.getUrlWithNewHostAndPort(url,
                        HtmlUnitBrowserCompatCookieSpec.LOCAL_FILESYSTEM_DOMAIN, 0);
            }
            catch (final MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return url;
    }

    public static List<org.htmlunit.util.Cookie> parseCookie(final String cookieString, final URL pageUrl,
            final BrowserVersion browserVersion)
            throws MalformedCookieException {
        final CharArrayBuffer buffer = new CharArrayBuffer(cookieString.length() + 22);
        buffer.append("Set-Cookie: ");
        buffer.append(cookieString);

        final CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(browserVersion);
        final List<Cookie> cookies = cookieSpec.parse(new BufferedHeader(buffer), buildCookieOrigin(pageUrl));

        final List<org.htmlunit.util.Cookie> htmlUnitCookies = new ArrayList<>(cookies.size());
        for (final Cookie cookie : cookies) {
            final org.htmlunit.util.Cookie htmlUnitCookie = new org.htmlunit.util.Cookie((ClientCookie) cookie);
            htmlUnitCookies.add(htmlUnitCookie);
        }
        return htmlUnitCookies;
    }

    /**
     * Converts the specified collection of cookies into a collection of HttpClient cookies.
     * @param cookies the cookies to be converted
     * @return the specified cookies, as HttpClient cookies
     */
    public static List<Cookie> toHttpClient(final Collection<org.htmlunit.util.Cookie> cookies) {
        final ArrayList<Cookie> array = new ArrayList<>(cookies.size());
        for (final org.htmlunit.util.Cookie cookie : cookies) {
            array.add(cookie.toHttpClient());
        }
        return array;
    }

    /**
     * Converts the specified array of HttpClient cookies into a list of cookies.
     * @param cookies the cookies to be converted
     * @return the specified HttpClient cookies, as cookies
     */
    public static List<org.htmlunit.util.Cookie> fromHttpClient(final List<Cookie> cookies) {
        final List<org.htmlunit.util.Cookie> list = new ArrayList<>(cookies.size());
        for (final Cookie c : cookies) {
            list.add(new org.htmlunit.util.Cookie((ClientCookie) c));
        }
        return list;
    }

    public static void addMatching(final Set<org.htmlunit.util.Cookie> cookies,
            final URL normalizedUrl, final BrowserVersion browserVersion,
            final Set<org.htmlunit.util.Cookie> matches) {
        if (cookies.size() > 0) {
            final CookieOrigin cookieOrigin = HttpClientConverter.buildCookieOrigin(normalizedUrl);
            final CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(browserVersion);
            for (final org.htmlunit.util.Cookie cookie : cookies) {
                if (cookieSpec.match(cookie.toHttpClient(), cookieOrigin)) {
                    matches.add(cookie);
                }
            }
        }
    }
}
