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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitCookieStore;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * Tests the HtmlUnitCookieStore, which is a wrapper around the CookieManager.
 *
 * @author James Phillpotts
 * @author Joerg Werner
 */
public class HtmlUnitCookieStoreTest {

    private CookieManager mgr_;
    private HtmlUnitCookieStore store_;

    /**
     * Setup.
     */
    @Before
    public void setUp() {
        mgr_ = new CookieManager();
        store_ = new HtmlUnitCookieStore(mgr_);
    }

    /**
     * Simple test for addCookie.
     */
    @Test
    public void addCookie() {
        final BasicClientCookie c = new BasicClientCookie("myname", "myvalue");
        c.setDomain("localhost");
        store_.addCookie(c);

        assertEquals(1, mgr_.getCookies().size());
        assertEquals("myvalue", mgr_.getCookie("myname").getValue());
    }

    /**
     * Simple test for getCookies.
     */
    @Test
    public void getCookies() {
        mgr_.addCookie(new Cookie("localhost", "myname", "myvalue"));
        mgr_.addCookie(new Cookie("localhost", "myname2", "myvalue2"));

        final List<org.apache.hc.client5.http.cookie.Cookie> cookies = store_.getCookies();
        assertEquals(2, cookies.size());
        assertTrue(containsCookie(cookies, "myname", "myvalue"));
        assertTrue(containsCookie(cookies, "myname2", "myvalue2"));

        mgr_.setCookiesEnabled(false);
        assertTrue(store_.getCookies().isEmpty());
    }

    /**
     * Simple test for clearExpired.
     */
    @Test
    public void clearExpired() {
        mgr_.addCookie(new Cookie("localhost", "myname", "myvalue"));
        final Cookie cookie = new Cookie("localhost", "myname2", "myvalue2", null,
                new Date(System.currentTimeMillis() + 10_000), false);
        mgr_.addCookie(cookie);

        assertTrue(store_.clearExpired(new Date(System.currentTimeMillis() + 20_000)));
        assertFalse(store_.clearExpired(new Date(System.currentTimeMillis() + 20_000)));

        final List<org.apache.hc.client5.http.cookie.Cookie> cookies = store_.getCookies();
        assertEquals(1, cookies.size());
        assertTrue(containsCookie(cookies, "myname", "myvalue"));
    }

    /**
     * Simple test for clear.
     */
    @Test
    public void clear() {
        mgr_.addCookie(new Cookie("localhost", "myname", "myvalue"));
        mgr_.addCookie(new Cookie("localhost", "myname2", "myvalue2"));

        assertFalse(store_.getCookies().isEmpty());

        store_.clear();
        assertTrue(store_.getCookies().isEmpty());
    }

    /**
     * Checks if the given list of cookies contains a cookie with the passed name and value.
     * @param cookies the list of cookies to search
     * @param name the cookie name
     * @param value the cookie value
     * @return <code>true</code> if such a cookie was found, <code>false</code> otherwise
     */
    private static boolean containsCookie(final List<org.apache.hc.client5.http.cookie.Cookie> cookies,
                                          final String name, final String value) {
        for (final org.apache.hc.client5.http.cookie.Cookie cookie : cookies) {
            if (cookie.getName().equals(name) && cookie.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
