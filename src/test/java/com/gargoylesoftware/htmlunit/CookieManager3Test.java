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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * Unit tests for {@link CookieManager}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class CookieManager3Test {

    /**
     * Verifies the basic cookie manager behavior.
     */
    @Test
    public void basicBehavior() {
        // Create a new cookie manager.
        final CookieManager mgr = new CookieManager();
        assertTrue(mgr.isCookiesEnabled());
        assertTrue(mgr.getCookies().isEmpty());

        // Add a cookie to the manager.
        final Cookie cookie = new Cookie("localhost", "a", "b");
        mgr.addCookie(cookie);
        assertFalse(mgr.getCookies().isEmpty());

        // Remove the cookie from the manager.
        mgr.removeCookie(cookie);
        assertTrue(mgr.getCookies().isEmpty());

        // Add the cookie back to the manager.
        mgr.addCookie(cookie);
        assertFalse(mgr.getCookies().isEmpty());

        // Clear all cookies from the manager.
        mgr.clearCookies();
        assertTrue(mgr.getCookies().isEmpty());

        // Add a cookie before disabling cookies.
        mgr.addCookie(cookie);
        assertEquals(1, mgr.getCookies().size());

        // Disable cookies.
        mgr.setCookiesEnabled(false);
        assertFalse(mgr.isCookiesEnabled());
        assertTrue(mgr.getCookies().isEmpty());

        // Add a cookie after disabling cookies.
        final Cookie cookie2 = new Cookie("a", "b", "c", "d", new Date(System.currentTimeMillis() + 5000), false);
        mgr.addCookie(cookie2);
        assertTrue(mgr.getCookies().isEmpty());
        assertFalse(mgr.clearExpired(new Date(System.currentTimeMillis() + 10_000)));

        // Enable cookies again.
        mgr.setCookiesEnabled(true);
        assertTrue(mgr.isCookiesEnabled());
        assertEquals(1, mgr.getCookies().size());

        // Clear expired cookies
        assertFalse(mgr.clearExpired(new Date(System.currentTimeMillis() + 10_000)));
        assertEquals(1, mgr.getCookies().size());

        mgr.addCookie(cookie2);
        assertEquals(2, mgr.getCookies().size());
        assertTrue(mgr.clearExpired(new Date(System.currentTimeMillis() + 10_000)));
        assertEquals(1, mgr.getCookies().size());
    }
}
