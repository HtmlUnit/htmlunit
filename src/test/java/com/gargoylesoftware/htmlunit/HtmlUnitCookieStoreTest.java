/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * Tests the HtmlUnitCookieStore, which is a wrapper around the CookieManager.
 * @version $Revision$
 * @author James Phillpotts
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

        final List<org.apache.http.cookie.Cookie> cookies = store_.getCookies();
        assertEquals(2, cookies.size());
        assertTrue(cookies.contains(new MyCookie("myname", "myvalue")));
        assertTrue(cookies.contains(new MyCookie("myname2", "myvalue2")));

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
                new Date(System.currentTimeMillis() + 10000), false);
        mgr_.addCookie(cookie);

        assertTrue(store_.clearExpired(new Date(System.currentTimeMillis() + 20000)));
        assertFalse(store_.clearExpired(new Date(System.currentTimeMillis() + 20000)));

        final List<org.apache.http.cookie.Cookie> cookies = store_.getCookies();
        assertEquals(1, cookies.size());
        assertTrue(cookies.contains(new MyCookie("myname", "myvalue")));
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

    private static final class MyCookie extends BasicClientCookie {
        MyCookie(final String name, final String value) {
            super(name, value);
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof org.apache.http.cookie.Cookie
                && new EqualsBuilder()
                    .append(getName(), ((org.apache.http.cookie.Cookie) obj).getName())
                    .append(getValue(), ((org.apache.http.cookie.Cookie) obj).getValue())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
