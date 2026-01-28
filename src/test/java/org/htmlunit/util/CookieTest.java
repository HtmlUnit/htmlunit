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
package org.htmlunit.util;

import org.htmlunit.http.Cookie;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link Cookie}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class CookieTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domain() throws Exception {
        try {
            new Cookie(null, "a", "one", null, null, false, false, null);
            fail("IllegalArgumentException expected");
        }
        catch (final IllegalArgumentException e) {
            // expected
        }

        final Cookie cookie1 = new Cookie("localhost", "a", "one", "/", null, false, false, null);
        assertEquals("localhost", cookie1.getDomain());

        final Cookie cookie2 = new Cookie("lOcAlHOST", "a", "one", "/", null, false, false, null);
        assertEquals("localhost", cookie2.getDomain());

        assertEquals(cookie1, cookie2);
        assertEquals(cookie1.hashCode(), cookie2.hashCode());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void value() throws Exception {
        final Cookie cookie1 = new Cookie("localhost", "a", null, "/", null, false, false, null);
        assertEquals("", cookie1.getValue());

        final Cookie cookie2 = new Cookie("localhost", "a", "", "/", null, false, false, null);
        assertEquals("", cookie2.getValue());

        assertEquals(cookie1, cookie2);
        assertEquals(cookie1.hashCode(), cookie2.hashCode());

        final Cookie cookie3 = new Cookie("localhost", "a", "Ab", "/", null, false, false, null);
        assertEquals("Ab", cookie3.getValue());

        assertEquals(cookie1, cookie3);
        assertEquals(cookie2, cookie3);
        assertEquals(cookie1.hashCode(), cookie3.hashCode());
        assertEquals(cookie2.hashCode(), cookie3.hashCode());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void properties() throws Exception {
        final Cookie cookie = new Cookie("localhost", "Name ", "Value ", "/Path  x ", new Date(1234567890),
                true, true, "SameSite");

        assertEquals("localhost", cookie.getDomain());
        assertEquals("Name ", cookie.getName());
        assertEquals("Value ", cookie.getValue());
        assertEquals("/Path  x ", cookie.getPath());
        assertEquals(new Date(1234567890), cookie.getExpires());
        assertTrue(cookie.isSecure());
        assertTrue(cookie.isHttpOnly());
        assertEquals("SameSite", cookie.getSameSite());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void nullPath() throws Exception {
        final Cookie cookie1 = new Cookie("localhost", "a", "one", null, null, false, false, null);
        final Cookie cookie2 = new Cookie("localhost", "a", "one", null, null, false, false, null);
        final Cookie cookie3 = new Cookie("localhost", "a", "one", "/", null, true, false, null);
        assertEquals(cookie1, cookie2);
        assertEquals(cookie2, cookie3);
        assertEquals(cookie1.hashCode(), cookie2.hashCode());
        assertEquals(cookie2.hashCode(), cookie3.hashCode());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void string() throws Exception {
        final Cookie cookie = new Cookie("localhost", "Name ", "Value ", "/Path  x ", new Date(1234567890),
                true, true, "SameSite");

        assertEquals("Name =Value ;domain=localhost;path=/Path  x ;"
                + "expires=Thu Jan 15 07:56:07 CET 1970;secure;httpOnly;sameSite=SameSite", cookie.toString());
    }
}
