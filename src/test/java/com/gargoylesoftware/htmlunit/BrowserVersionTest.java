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

import org.junit.Test;

/**
 * Tests for {@link BrowserVersion}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class BrowserVersionTest extends WebTestCase {

    /**
     * Test of getBrowserVersionNumeric().
     */
    @Test
    public void getBrowserVersionNumeric() {
        assertEquals(85, BrowserVersion.FIREFOX.getBrowserVersionNumeric());
        assertEquals(78, BrowserVersion.FIREFOX_78.getBrowserVersionNumeric());
        assertEquals(11, BrowserVersion.INTERNET_EXPLORER.getBrowserVersionNumeric());
        assertEquals(88, BrowserVersion.CHROME.getBrowserVersionNumeric());
        assertEquals(88, BrowserVersion.EDGE.getBrowserVersionNumeric());
    }

    /**
     * Test of {@link BrowserVersion#clone()}.
     */
    @Test
    public void testClone() {
        final BrowserVersion ff = BrowserVersion.INTERNET_EXPLORER;
        final BrowserVersion clone = new BrowserVersion.BrowserVersionBuilder(ff).build();

        // Nickname is used as key for dictionaries storing browser setups
        assertTrue(ff.getNickname().equals(clone.getNickname()));

        assertFalse(ff == clone);
        assertFalse(ff.equals(clone));

        clone.getPlugins().clear();
        assertFalse(ff.getPlugins().isEmpty());
    }
}
