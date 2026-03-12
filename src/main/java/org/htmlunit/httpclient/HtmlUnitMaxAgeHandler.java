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
package org.htmlunit.httpclient;

import java.util.Date;
import java.util.regex.Pattern;

import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.cookie.BasicMaxAgeHandler;
import org.apache.http.util.Args;

/**
 * Customized BasicMaxAgeHandler for HtmlUnit.
 *
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
final class HtmlUnitMaxAgeHandler extends BasicMaxAgeHandler {

    // Max-Age should be 400 days at most
    // https://httpwg.org/http-extensions/draft-ietf-httpbis-rfc6265bis.html#section-5.5
    private static final int MAX_MAX_AGE = 400 * 24 * 60 * 60;

    private static final Pattern MAX_AGE_PATTERN = Pattern.compile("-?[0-9]+");

    @Override
    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (value == null || value.isEmpty()) {
            throw new MalformedCookieException("Missing value for 'max-age' attribute");
        }
        if (!MAX_AGE_PATTERN.matcher(value).matches()) {
            throw new MalformedCookieException("Invalid 'max-age' attribute: " + value);
        }
        if (value.startsWith("-")) {
            cookie.setExpiryDate(new Date(0L));
            return;
        }
        int age;
        try {
            age = Math.min(Integer.parseInt(value), MAX_MAX_AGE);
        }
        catch (final NumberFormatException e) {
            age = MAX_MAX_AGE;
        }
        cookie.setExpiryDate(new Date(System.currentTimeMillis() + age * 1000L));
    }

}
