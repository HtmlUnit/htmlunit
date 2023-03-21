/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import org.htmlunit.util.NameValuePair;

/**
 * Helper methods to convert from/to HttpClient.
 *
 * @author Ronald Brill
 */
public final class HttpClientConverter {

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
}
