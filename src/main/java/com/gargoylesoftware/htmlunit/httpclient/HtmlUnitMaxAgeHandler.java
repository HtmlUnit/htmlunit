/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.httpclient;

import java.util.Date;

import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.cookie.BasicMaxAgeHandler;
import org.apache.http.util.Args;

/**
 * Customized BasicMaxAgeHandler for HtmlUnit.
 *
 * @author Ronald Brill
 */
final class HtmlUnitMaxAgeHandler extends BasicMaxAgeHandler {

    HtmlUnitMaxAgeHandler() {
        super();
    }

    @Override
    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (value == null) {
            throw new MalformedCookieException("Missing value for 'max-age' attribute");
        }
        final int age;
        try {
            age = Integer.parseInt(value);
        }
        catch (final NumberFormatException e) {
            throw new MalformedCookieException("Invalid 'max-age' attribute: " + value);
        }
        cookie.setExpiryDate(new Date(System.currentTimeMillis() + age * 1000L));
    }

}
