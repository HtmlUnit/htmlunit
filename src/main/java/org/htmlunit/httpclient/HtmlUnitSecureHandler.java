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

import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.BasicSecureHandler;
import org.apache.http.util.Args;

/**
 * Customized BasicSecureHandler for HtmlUnit.
 *
 * @author Ronald Brill
 */
final class HtmlUnitSecureHandler extends BasicSecureHandler {

    /**
     * {@inheritDoc}
     * Like the browsers, a page from an insecure origin can't set a secure cookie
     * (RFC 6265bis section 5.7); localhost and the loopback addresses are handled as secure
     * (see {@link HttpClientConverter#isSecureCookieOrigin(CookieOrigin)}).
     */
    @Override
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        if (cookie.isSecure() && !HttpClientConverter.isSecureCookieOrigin(origin)) {
            throw new CookieRestrictionViolationException(
                    "Cookie '" + cookie.getName() + "' with 'secure' attribute from an insecure origin");
        }
    }

    @Override
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        return !cookie.isSecure() || HttpClientConverter.isSecureCookieOrigin(origin);
    }
}
