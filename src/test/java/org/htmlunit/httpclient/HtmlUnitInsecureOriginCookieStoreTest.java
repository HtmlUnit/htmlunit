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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlUnitInsecureOriginCookieStore}; the store is used for the
 * Set-Cookie headers of responses from an insecure origin.
 *
 * @author Ronald Brill
 */
public class HtmlUnitInsecureOriginCookieStoreTest {

    /**
     * Cookies that would overwrite or shadow a secure cookie are ignored
     * (RFC 6265bis section 5.7, step 16).
     */
    @Test
    public void secureCookieNotOverwritten() {
        final BasicCookieStore delegate = new BasicCookieStore();
        delegate.addCookie(cookie("sec", "1", "www.example.com", "/", true));
        delegate.addCookie(cookie("secPath", "1", "www.example.com", "/app", true));
        delegate.addCookie(cookie("plain", "1", "www.example.com", "/", false));

        final HtmlUnitInsecureOriginCookieStore store = new HtmlUnitInsecureOriginCookieStore(delegate);
        // same name, domain and path
        store.addCookie(cookie("sec", "2", "www.example.com", "/", false));
        // the path /sub path-matches /
        store.addCookie(cookie("sec", "3", "www.example.com", "/sub", false));
        // the domain example.com domain-matches www.example.com
        store.addCookie(cookie("sec", "4", ".example.com", "/", false));
        // other.example.com does not domain-match www.example.com
        store.addCookie(cookie("sec", "5", "other.example.com", "/", false));
        // the path / does not path-match /app; /application does not path-match /app
        store.addCookie(cookie("secPath", "2", "www.example.com", "/", false));
        store.addCookie(cookie("secPath", "3", "www.example.com", "/application", false));
        // a secure cookie is not affected
        store.addCookie(cookie("sec", "6", "www.example.com", "/sub2", true));
        store.addCookie(cookie("plain", "2", "www.example.com", "/", false));

        assertEquals("[plain=2 www.example.com /, sec=1 www.example.com / secure, "
                + "sec=5 other.example.com /, sec=6 www.example.com /sub2 secure, "
                + "secPath=1 www.example.com /app secure, secPath=2 www.example.com /, "
                + "secPath=3 www.example.com /application]", dump(delegate.getCookies()));
    }

    private static BasicClientCookie cookie(final String name, final String value,
            final String domain, final String path, final boolean secure) {
        final BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setSecure(secure);
        return cookie;
    }

    private static String dump(final List<Cookie> cookies) {
        final List<String> result = new ArrayList<>();
        for (final Cookie cookie : cookies) {
            result.add(cookie.getName() + "=" + cookie.getValue() + " " + cookie.getDomain() + " "
                    + cookie.getPath() + (cookie.isSecure() ? " secure" : ""));
        }
        Collections.sort(result);
        return result.toString();
    }
}
