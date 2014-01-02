/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import org.junit.Test;

/**
 * Tests for {@link BrowserVersion}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 */
public class BrowserVersionTest extends SimpleWebTestCase {

    /**
     * Test of getBrowserVersionNumeric().
     */
    @Test
    public void getBrowserVersionNumeric() {
        assertEquals(17.0f, BrowserVersion.FIREFOX_17.getBrowserVersionNumeric());
        assertEquals(8.0f, BrowserVersion.INTERNET_EXPLORER_8.getBrowserVersionNumeric());
        assertEquals(9.0f, BrowserVersion.INTERNET_EXPLORER_9.getBrowserVersionNumeric());
        assertEquals(11.0f, BrowserVersion.INTERNET_EXPLORER_11.getBrowserVersionNumeric());
        assertEquals(31.0f, BrowserVersion.CHROME.getBrowserVersionNumeric());
    }

    /**
     * Test of {@link BrowserVersion#clone()}.
     */
    @Test
    public void testClone() {
        final BrowserVersion ff = BrowserVersion.FIREFOX_17;
        final BrowserVersion clone = ff.clone();

        assertFalse(ff == clone);
        assertEquals(ff, clone);

        clone.getPlugins().clear();
        assertFalse(ff.equals(clone));
    }
}
