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
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

/**
 * A {@link CookieStore} used for the response of a request to an insecure origin (http);
 * like the browsers, a cookie that would overwrite or shadow a secure cookie is ignored
 * (RFC 6265bis section 5.7, step 16). All other operations are delegated.
 *
 * @author Ronald Brill
 * @see HttpClientConverter#shadowsSecureCookie(org.htmlunit.http.Cookie, java.util.Collection)
 */
public final class HtmlUnitInsecureOriginCookieStore implements CookieStore {

    private final CookieStore delegate_;

    /**
     * Ctor.
     *
     * @param delegate the cookie store
     */
    public HtmlUnitInsecureOriginCookieStore(final CookieStore delegate) {
        delegate_ = delegate;
    }

    /**
     * Adds the cookie if it does not overwrite or shadow a secure cookie.
     * {@inheritDoc}
     */
    @Override
    public void addCookie(final Cookie cookie) {
        if (!cookie.isSecure() && HttpClientConverter.shadowsSecureCookie(cookie, delegate_.getCookies())) {
            return;
        }
        delegate_.addCookie(cookie);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Cookie> getCookies() {
        return delegate_.getCookies();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clearExpired(final Date date) {
        return delegate_.clearExpired(date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        delegate_.clear();
    }
}
