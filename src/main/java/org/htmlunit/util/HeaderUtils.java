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
package org.htmlunit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlunit.HttpHeader;
import org.htmlunit.WebResponse;

/**
 * Utility class for HTTP header analysis.
 *
 * @author Anton Demydenko
 * @author Lai Quang Duong
 * @author Ronald Brill
 */
public final class HeaderUtils {

    private static final String CACHE_CONTROL_PRIVATE = "private";
    private static final String CACHE_CONTROL_PUBLIC = "public";
    private static final String CACHE_CONTROL_NO_STORE = "no-store";
    private static final String CACHE_CONTROL_NO_CACHE = "no-cache";
    private static final String CACHE_CONTROL_MAX_AGE = "max-age";
    private static final String CACHE_CONTROL_S_MAXAGE = "s-maxage";
    private static final Pattern MAX_AGE_HEADER_PATTERN = Pattern.compile("^.*max-age=(\\d+).*$");
    private static final Pattern S_MAXAGE_HEADER_PATTERN = Pattern.compile("^.*s-maxage=(\\d+).*$");

    private HeaderUtils() {
        // utility class
    }

    /**
     * Returns whether the {@code Cache-Control} header is present and contains the {@code private} directive.
     *
     * @param response the {@link WebResponse} to check
     * @return {@code true} if the {@code Cache-Control} header contains {@code private}
     */
    public static boolean containsPrivate(final WebResponse response) {
        return containsCacheControlValue(response, CACHE_CONTROL_PRIVATE);
    }

    /**
     * Returns whether the {@code Cache-Control} header is present and contains the {@code public} directive.
     *
     * @param response the {@link WebResponse} to check
     * @return {@code true} if the {@code Cache-Control} header contains {@code public}
     */
    public static boolean containsPublic(final WebResponse response) {
        return containsCacheControlValue(response, CACHE_CONTROL_PUBLIC);
    }

    /**
     * Returns whether the {@code Cache-Control} header is present and contains the {@code no-store} directive.
     *
     * @param response the {@link WebResponse} to check
     * @return {@code true} if the {@code Cache-Control} header contains {@code no-store}
     */
    public static boolean containsNoStore(final WebResponse response) {
        return containsCacheControlValue(response, CACHE_CONTROL_NO_STORE);
    }

    /**
     * Returns whether the {@code Cache-Control} header is present and contains the {@code no-cache} directive.
     *
     * @param response the {@link WebResponse} to check
     * @return {@code true} if the {@code Cache-Control} header contains {@code no-cache}
     */
    public static boolean containsNoCache(final WebResponse response) {
        return containsCacheControlValue(response, CACHE_CONTROL_NO_CACHE);
    }

    /**
     * Returns whether the {@code ETag} header is present.
     *
     * @param response the {@link WebResponse} to check
     * @return {@code true} if the {@code ETag} header is present
     */
    public static boolean containsETag(final WebResponse response) {
        return response.getResponseHeaderValue(HttpHeader.ETAG) != null;
    }

    /**
     * Returns whether the {@code Last-Modified} header is present.
     *
     * @param response the {@link WebResponse} to check
     * @return {@code true} if the {@code Last-Modified} header is present
     */
    public static boolean containsLastModified(final WebResponse response) {
        return response.getResponseHeaderValue(HttpHeader.LAST_MODIFIED) != null;
    }

    /**
     * Returns whether the {@code Cache-Control} header is present and contains the {@code s-maxage} directive.
     *
     * @param response the {@link WebResponse} to check
     * @return {@code true} if the {@code Cache-Control} header contains {@code s-maxage}
     */
    public static boolean containsSMaxage(final WebResponse response) {
        return containsCacheControlValue(response, CACHE_CONTROL_S_MAXAGE);
    }

    /**
     * Returns whether the {@code Cache-Control} header is present and contains the {@code max-age} directive.
     *
     * @param response the {@link WebResponse} to check
     * @return {@code true} if the {@code Cache-Control} header contains {@code max-age}
     */
    public static boolean containsMaxAge(final WebResponse response) {
        return containsCacheControlValue(response, CACHE_CONTROL_MAX_AGE);
    }

    /**
     * Returns whether the {@code Cache-Control} header is present and contains
     * either the {@code max-age} or the {@code s-maxage} directive.
     *
     * @param response the {@link WebResponse} to check
     * @return {@code true} if the {@code Cache-Control} header contains {@code max-age} or {@code s-maxage}
     */
    public static boolean containsMaxAgeOrSMaxage(final WebResponse response) {
        final String cacheControl = response.getResponseHeaderValue(HttpHeader.CACHE_CONTROL);
        if (StringUtils.containsIgnoreCase(cacheControl, CACHE_CONTROL_MAX_AGE)) {
            return true;
        }
        return StringUtils.containsIgnoreCase(cacheControl, CACHE_CONTROL_S_MAXAGE);
    }

    /**
     * Returns the value of the {@code s-maxage} directive, or {@code 0} if it is absent.
     *
     * @param response the {@link WebResponse} to check
     * @return the value of the {@code s-maxage} directive, or {@code 0}
     */
    public static long sMaxage(final WebResponse response) {
        if (containsCacheControlValue(response, CACHE_CONTROL_S_MAXAGE)) {
            return directiveValue(response, S_MAXAGE_HEADER_PATTERN);
        }
        return 0;
    }

    /**
     * Returns the value of the {@code max-age} directive, or {@code 0} if it is absent.
     *
     * @param response the {@link WebResponse} to check
     * @return the value of the {@code max-age} directive, or {@code 0}
     */
    public static long maxAge(final WebResponse response) {
        if (containsCacheControlValue(response, CACHE_CONTROL_MAX_AGE)) {
            return directiveValue(response, MAX_AGE_HEADER_PATTERN);
        }

        return 0;
    }

    private static long directiveValue(final WebResponse response, final Pattern pattern) {
        final String value = response.getResponseHeaderValue(HttpHeader.CACHE_CONTROL);
        if (value != null) {
            final Matcher matcher = pattern.matcher(value);
            if (matcher.matches()) {
                return Long.parseLong(matcher.group(1));
            }
        }

        return 0;
    }

    private static boolean containsCacheControlValue(final WebResponse response, final String value) {
        final String cacheControl = response.getResponseHeaderValue(HttpHeader.CACHE_CONTROL);
        return StringUtils.containsIgnoreCase(cacheControl, value);
    }
}
