/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import junit.framework.TestCase;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpState;
import org.junit.Test;

/**
 * Unit tests for {@link CookieManager}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class CookieManagerTest extends TestCase {

    /**
     * Verifies the basic cookie manager behavior, whose main complexity lies in the
     * optimization of {@link CookieManager#updateState(HttpState)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void basicBehavior() throws Exception {
        // Create a new cookie manager.
        CookieManager mgr = new CookieManager();
        assertFalse(mgr.isCookiesModified());
        assertTrue(mgr.isCookiesEnabled());
        assertTrue(mgr.getCookies().isEmpty());

        // Add a cookie to the manager.
        Cookie cookie = new Cookie("a", "b", "c");
        mgr.addCookie(cookie);
        assertTrue(mgr.isCookiesModified());
        assertFalse(mgr.getCookies().isEmpty());

        // Update an HTTP state.
        HttpState state = new HttpState();
        mgr.updateState(state);
        assertFalse(mgr.isCookiesModified());
        assertEquals(1, state.getCookies().length);

        // Remove the cookie from the manager.
        mgr.removeCookie(cookie);
        assertTrue(mgr.isCookiesModified());
        assertTrue(mgr.getCookies().isEmpty());

        // Update an HTTP state after removing the cookie.
        mgr.updateState(state);
        assertFalse(mgr.isCookiesModified());
        assertEquals(0, state.getCookies().length);

        // Add the cookie back to the manager.
        mgr.addCookie(cookie);
        assertTrue(mgr.isCookiesModified());
        assertFalse(mgr.getCookies().isEmpty());

        // Update an HTTP state after adding the cookie back to the manager.
        mgr.updateState(state);
        assertFalse(mgr.isCookiesModified());
        assertEquals(1, state.getCookies().length);

        // Clear all cookies from the manager.
        mgr.clearCookies();
        assertTrue(mgr.isCookiesModified());
        assertTrue(mgr.getCookies().isEmpty());

        // Update an HTTP state after clearing all cookies from the manager.
        mgr.updateState(state);
        assertFalse(mgr.isCookiesModified());
        assertEquals(0, state.getCookies().length);

        // Disable cookies.
        mgr.setCookiesEnabled(false);
        assertFalse(mgr.isCookiesEnabled());

        // Add a cookie after disabling cookies.
        mgr.addCookie(cookie);
        assertTrue(mgr.isCookiesModified());
        assertFalse(mgr.getCookies().isEmpty());

        // Update an HTTP state after adding a cookie while cookies are disabled.
        mgr.updateState(state);
        assertTrue(mgr.isCookiesModified());
        assertEquals(0, state.getCookies().length);

        // Enable cookies again.
        mgr.setCookiesEnabled(true);
        assertTrue(mgr.isCookiesEnabled());

        // Update an HTTP state after enabling cookies again.
        mgr.updateState(state);
        assertFalse(mgr.isCookiesModified());
        assertEquals(1, state.getCookies().length);
    }

}
