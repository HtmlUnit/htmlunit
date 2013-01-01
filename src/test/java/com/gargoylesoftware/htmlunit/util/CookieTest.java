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
package com.gargoylesoftware.htmlunit.util;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link Cookie}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class CookieTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void nullPath() throws Exception {
        final Cookie cookie1 = new Cookie("localhost", "a", "one");
        final Cookie cookie2 = new Cookie("localhost", "a", "one", null, null, false);
        final Cookie cookie3 = new Cookie("localhost", "a", "one", "/", null, false);
        assertEquals(cookie1, cookie2);
        assertEquals(cookie2, cookie3);
        assertEquals(cookie1.hashCode(), cookie2.hashCode());
        assertEquals(cookie2.hashCode(), cookie3.hashCode());
    }
}
