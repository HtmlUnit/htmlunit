/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * @author Anton Demydenko
 */
public final class HeaderUtils {

    private static final String CACHE_CONTROL_PRIVATE = "private";
    private static final String CACHE_CONTROL_PUBLIC = "public";
    private static final String CACHE_CONTROL_NO_STORE = "no-store";
    private static final String CACHE_CONTROL_NO_CACHE = "no-cache";
    private static final String CACHE_CONTROL_MAX_AGE = "max-age";
    private static final String CACHE_CONTROL_S_MAXAGE = "s-maxage";
    private static final Pattern MAX_AGE_HEADER_PATTERN = Pattern.compile("^.*max-age=([\\d]+).*$");
    private static final Pattern S_MAXAGE_HEADER_PATTERN = Pattern.compile("^.*s-maxage=([\\d]+).*$");

    private HeaderUtils() {
        // utility class
    }

    /**
     * @param response {@code WebResponse}
     * @return if 'Cache-Control' header is present and contains 'private' value
     */
    public static boolean containsPrivate(final WebResponse response) {
        return containsCacheControlValue(response, CACHE_CONTROL_PRIVATE);
    }

    /**
     * @param response {@code WebResponse}
     * @return if 'Cache-Control' header is present and contains 'public' value
     */
    public static boolean containsPublic(final WebResponse response) {
        return containsCacheControlValue(response, CACHE_CONTROL_PUBLIC);
    }

    /**
     * @param response {@code WebResponse}
     * @return if 'Cache-Control' header is present and contains 'no-store' value
     */
    public static boolean containsNoStore(final WebResponse response) {
        return containsCacheControlValue(response, CACHE_CONTROL_NO_STORE);
    }

    /**
     * @param response {@code WebResponse}
     * @return if 'Cache-Control' header is present and contains 'no-cache' value@return
     */
    public static boolean containsNoCache(final WebResponse response) {
        return containsCacheControlValue(response, CACHE_CONTROL_NO_CACHE);
    }

    /**
     * @param response {@code WebResponse}
     * @return if 'Cache-Control' header is present and contains 's-maxage' value
     */
    public static boolean containsSMaxage(final WebResponse response) {
        return containsCacheControlValue(response, CACHE_CONTROL_S_MAXAGE);
    }

    /**
     * @param response {@code WebResponse}
     * @return if 'Cache-Control' header is present and contains 'max-age' value
     */
    public static boolean containsMaxAge(final WebResponse response) {
        return containsCacheControlValue(response, CACHE_CONTROL_MAX_AGE);
    }

    /**
     * @param response {@code WebResponse}
     * @return if 'Cache-Control' header is present and contains 'max-age' value
     */
    public static boolean containsMaxAgeOrSMaxage(final WebResponse response) {
        final String cacheControl = response.getResponseHeaderValue(HttpHeader.CACHE_CONTROL);
        if (StringUtils.contains(cacheControl, CACHE_CONTROL_MAX_AGE)) {
            return true;
        }
        return StringUtils.contains(cacheControl, CACHE_CONTROL_S_MAXAGE);
    }

    /**
     * @param response {@code WebResponse}
     * @return value of 's-maxage' directive and 0 if it is absent
     */
    public static long sMaxage(final WebResponse response) {
        if (containsCacheControlValue(response, CACHE_CONTROL_S_MAXAGE)) {
            return directiveValue(response, S_MAXAGE_HEADER_PATTERN);
        }
        return 0;
    }

    /**
     * @param response {@code WebResponse}
     * @return value of 'max-age' directive and 0 if it is absent
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
        return StringUtils.contains(cacheControl, value);
    }
}
