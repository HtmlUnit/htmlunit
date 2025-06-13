/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.TimeZone;

import org.junit.Test;

/**
 * Tests for {@link BrowserVersion}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class BrowserVersionTest {

    /**
     * Test of getBrowserVersionNumeric().
     */
    @Test
    public void getBrowserVersionNumeric() {
        assertEquals(139, BrowserVersion.FIREFOX.getBrowserVersionNumeric());
        assertEquals(128, BrowserVersion.FIREFOX_ESR.getBrowserVersionNumeric());
        assertEquals(137, BrowserVersion.CHROME.getBrowserVersionNumeric());
        assertEquals(137, BrowserVersion.EDGE.getBrowserVersionNumeric());
    }

    /**
     * Test of {@link BrowserVersion#clone()}.
     */
    @Test
    public void testClone() {
        final BrowserVersion ff = BrowserVersion.FIREFOX;

        final BrowserVersion clone = new BrowserVersion.BrowserVersionBuilder(ff).build();

        // Nickname is used as key for dictionaries storing browser setups
        assertTrue(ff.getNickname().equals(clone.getNickname()));

        assertFalse(ff == clone);
        assertFalse(ff.equals(clone));
    }

    /**
     * Test of BrowserVersion.BrowserVersionBuilder.
     */
    @Test
    public void differentTimeZone() {
        final BrowserVersion ffBerlin = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.FIREFOX)
                                                .setSystemTimezone(TimeZone.getTimeZone("Europe/Berlin"))
                                                .build();

        // Nickname is used as key for dictionaries storing browser setups
        assertTrue(BrowserVersion.FIREFOX.getNickname().equals(ffBerlin.getNickname()));

        assertFalse(BrowserVersion.FIREFOX == ffBerlin);
        assertFalse(BrowserVersion.FIREFOX.equals(ffBerlin));

        assertNotEquals(BrowserVersion.FIREFOX.getSystemTimezone(), ffBerlin.getSystemTimezone());
    }
}
