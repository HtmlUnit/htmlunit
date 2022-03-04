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
        assertEquals(97, BrowserVersion.FIREFOX.getBrowserVersionNumeric());
        assertEquals(91, BrowserVersion.FIREFOX_ESR.getBrowserVersionNumeric());
        assertEquals(11, BrowserVersion.INTERNET_EXPLORER.getBrowserVersionNumeric());
        assertEquals(98, BrowserVersion.CHROME.getBrowserVersionNumeric());
        assertEquals(98, BrowserVersion.EDGE.getBrowserVersionNumeric());
    }

    /**
     * Test of {@link BrowserVersion#clone()}.
     */
    @Test
    public void testClone() {
        final BrowserVersion ff = BrowserVersion.INTERNET_EXPLORER;

        final PluginConfiguration flash = new PluginConfiguration("Shockwave Flash",
                "Shockwave Flash 32.0 r0", "32.0.0.445", "Flash.ocx");
        flash.getMimeTypes().add(new PluginConfiguration.MimeType("application/x-shockwave-flash",
                "Shockwave Flash", "swf"));
        ff.getPlugins().add(flash);

        final BrowserVersion clone = new BrowserVersion.BrowserVersionBuilder(ff).build();

        // Nickname is used as key for dictionaries storing browser setups
        assertTrue(ff.getNickname().equals(clone.getNickname()));

        assertFalse(ff == clone);
        assertFalse(ff.equals(clone));

        assertFalse(clone.getPlugins().isEmpty());
        clone.getPlugins().clear();
        assertTrue(clone.getPlugins().isEmpty());
        assertFalse(ff.getPlugins().isEmpty());
    }
}
