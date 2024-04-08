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
import org.htmlunit.http.HttpStatus;
import org.htmlunit.http.HttpUtils;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.UrlUtils;

/**
 * Helper methods to convert from/to HttpClient.
 *
 * @author Ronald Brill
 */
public final class HttpClientConverter {

    /** Forwarder to HttpStatus.SC_OK.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#OK_200} instead
     */
    @Deprecated
    public static final int OK = org.apache.http.HttpStatus.SC_OK;

    /** Forwarder to HttpStatus.SC_NO_CONTENT.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#NO_CONTENT_204} instead
     */
    @Deprecated
    public static final int NO_CONTENT = org.apache.http.HttpStatus.SC_NO_CONTENT;

    /** Forwarder to HttpStatus.MULTIPLE_CHOICES.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#MULTIPLE_CHOICES_300} instead
     */
    @Deprecated
    public static final int MULTIPLE_CHOICES = org.apache.http.HttpStatus.SC_MULTIPLE_CHOICES;

    /** Forwarder to HttpStatus.MOVED_PERMANENTLY.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#MOVED_PERMANENTLY_301} instead
     */
    @Deprecated
    public static final int MOVED_PERMANENTLY = org.apache.http.HttpStatus.SC_MOVED_PERMANENTLY;

    /** Forwarder to HttpStatus.MOVED_TEMPORARILY.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#FOUND_302} instead
     */
    @Deprecated
    public static final int MOVED_TEMPORARILY = org.apache.http.HttpStatus.SC_MOVED_TEMPORARILY;

    /** Forwarder to HttpStatus.SEE_OTHER.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#SEE_OTHER_303} instead
     */
    @Deprecated
    public static final int SEE_OTHER = org.apache.http.HttpStatus.SC_SEE_OTHER;

    /** Forwarder to HttpStatus.TEMPORARY_REDIRECT.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#TEMPORARY_REDIRECT_307} instead
     */
    @Deprecated
    public static final int TEMPORARY_REDIRECT = org.apache.http.HttpStatus.SC_TEMPORARY_REDIRECT;

    /** 308.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#PERMANENT_REDIRECT_308} instead
     */
    @Deprecated
    public static final int PERMANENT_REDIRECT = 308;

    /** Forwarder to HttpStatus.NOT_MODIFIED.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#NOT_MODIFIED_304} instead
     */
    @Deprecated
    public static final int NOT_MODIFIED = org.apache.http.HttpStatus.SC_NOT_MODIFIED;

    /** Forwarder to HttpStatus.SC_USE_PROXY.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#USE_PROXY_305} instead
     */
    @Deprecated
    public static final int USE_PROXY = org.apache.http.HttpStatus.SC_USE_PROXY;

    /** Forwarder to HttpStatus.SC_FORBIDDEN.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#FORBIDDEN_403} instead
     */
    @Deprecated
    public static final int FORBIDDEN = org.apache.http.HttpStatus.SC_FORBIDDEN;

    /** Forwarder to HttpStatus.SC_NOT_FOUND.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#NOT_FOUND_404} instead
     */
    @Deprecated
    public static final int NOT_FOUND = org.apache.http.HttpStatus.SC_NOT_FOUND;

    /** Forwarder to HttpStatus.SC_INTERNAL_SERVER_ERROR.
     * @deprecated as of version 4.1.0; use {@link HttpStatus#INTERNAL_SERVER_ERROR_500} instead
     */
    @Deprecated
    public static final int INTERNAL_SERVER_ERROR = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

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
     * Parses url query into name/value pairs using methods from HttpClient.
     * @param query the urlencoded query
     * @param charset the charset or null (defaulting to utf-8)
     * @return the name/value pairs
     *
     * @deprecated as of version 4.1.0; use {@link HttpUtils#parseUrlQuery(String, Charset)} instead
     */
    @Deprecated
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
     *
     * @deprecated as of version 4.1.0; use {@link HttpUtils#toQueryFormFields(Iterable, Charset)} instead
     */
    @Deprecated
    public static String toQueryFormFields(final List<NameValuePair> parameters, final Charset enc) {
        return URLEncodedUtils.format(nameValuePairsToHttpClient(parameters), enc);
    }

    /**
     * Parses the specified date string, assuming that it is formatted according to RFC 1123, RFC 1036 or as an ANSI
     * C HTTP date header. This method returns {@code null} if the specified string is {@code null} or unparseable.
     *
     * @param s the string to parse as a date
     * @return the date version of the specified string, or {@code null}
     *
     * @deprecated as of version 4.1.0; use {@link HttpUtils#parseDate(String)} instead
     */
    @Deprecated
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
     *
     * @deprecated as of version 4.1.0; use {@link HttpUtils#parseDate(String)} instead
     */
    @Deprecated
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

    public static List<org.htmlunit.http.Cookie> parseCookie(final String cookieString, final URL pageUrl,
            final BrowserVersion browserVersion)
            throws MalformedCookieException {
        final CharArrayBuffer buffer = new CharArrayBuffer(cookieString.length() + 22);
        buffer.append("Set-Cookie: ");
        buffer.append(cookieString);

        final CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(browserVersion);
        final List<Cookie> cookies = cookieSpec.parse(new BufferedHeader(buffer), buildCookieOrigin(pageUrl));

        final List<org.htmlunit.http.Cookie> htmlUnitCookies = new ArrayList<>(cookies.size());
        for (final Cookie cookie : cookies) {
            final org.htmlunit.http.Cookie htmlUnitCookie = new org.htmlunit.http.Cookie((ClientCookie) cookie);
            htmlUnitCookies.add(htmlUnitCookie);
        }
        return htmlUnitCookies;
    }

    /**
     * Converts the specified collection of cookies into a collection of HttpClient cookies.
     * @param cookies the cookies to be converted
     * @return the specified cookies, as HttpClient cookies
     */
    public static List<Cookie> toHttpClient(final Collection<org.htmlunit.http.Cookie> cookies) {
        final ArrayList<Cookie> array = new ArrayList<>(cookies.size());
        for (final org.htmlunit.http.Cookie cookie : cookies) {
            array.add(cookie.toHttpClient());
        }
        return array;
    }

    /**
     * Converts the specified array of HttpClient cookies into a list of cookies.
     * @param cookies the cookies to be converted
     * @return the specified HttpClient cookies, as cookies
     */
    public static List<org.htmlunit.http.Cookie> fromHttpClient(final List<Cookie> cookies) {
        final List<org.htmlunit.http.Cookie> list = new ArrayList<>(cookies.size());
        for (final Cookie c : cookies) {
            list.add(new org.htmlunit.http.Cookie((ClientCookie) c));
        }
        return list;
    }

    public static void addMatching(final Set<org.htmlunit.http.Cookie> cookies,
            final URL normalizedUrl, final BrowserVersion browserVersion,
            final Set<org.htmlunit.http.Cookie> matches) {
        if (cookies.size() > 0) {
            final CookieOrigin cookieOrigin = HttpClientConverter.buildCookieOrigin(normalizedUrl);
            final CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(browserVersion);
            for (final org.htmlunit.http.Cookie cookie : cookies) {
                if (cookieSpec.match(cookie.toHttpClient(), cookieOrigin)) {
                    matches.add(cookie);
                }
            }
        }
    }
}
