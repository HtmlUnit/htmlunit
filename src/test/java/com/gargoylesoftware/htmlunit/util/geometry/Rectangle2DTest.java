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
package com.gargoylesoftware.htmlunit.util.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link Rectangle2D}.
 *
 * @author Ronald Brill
 */
public class Rectangle2DTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void props() throws Exception {
        assertEquals(4, new Rectangle2D(4, 7, 5, 9).getLeft(), 0.00001);
        assertEquals(7, new Rectangle2D(4, 7, 5, 9).getBottom(), 0.00001);
        assertEquals(4, new Rectangle2D(5, 7, 4, 9).getLeft(), 0.00001);
        assertEquals(7, new Rectangle2D(5, 7, 4, 9).getBottom(), 0.00001);
        assertEquals(7, new Rectangle2D(5, 9, 4, 7).getBottom(), 0.00001);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void contains() throws Exception {
        assertTrue(new Rectangle2D(4, 7, 5, 9).contains(4, 7));
        assertTrue(new Rectangle2D(4, 7, 5, 9).contains(4.5, 7));
        assertTrue(new Rectangle2D(4, 7, 5, 9).contains(4.5, 8));
        assertTrue(new Rectangle2D(4, 7, 5, 9).contains(4.5, 9));

        assertTrue(new Rectangle2D(4, 7, 5, 9).contains(5, 7));

        assertFalse(new Rectangle2D(4, 7, 5, 9).contains(5.001, 7));
        assertFalse(new Rectangle2D(4, 7, 5, 9).contains(5, 9.001));
        assertFalse(new Rectangle2D(4, 7, 5, 9).contains(3.999, 6));
        assertFalse(new Rectangle2D(4, 7, 5, 9).contains(4.5, 6.999));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void extend() throws Exception {
        final Rectangle2D testRect = new Rectangle2D(4, 7, 5, 9);
        assertEquals(4, testRect.getLeft(), 0.00001);
        assertEquals(7, testRect.getBottom(), 0.00001);

        testRect.extend(20, 17);
        assertEquals(4, testRect.getLeft(), 0.00001);
        assertEquals(7, testRect.getBottom(), 0.00001);

        testRect.extend(-2, 4);
        assertEquals(-2, testRect.getLeft(), 0.00001);
        assertEquals(4, testRect.getBottom(), 0.00001);

        testRect.extend(4.5, 13.2);
        assertEquals(-2, testRect.getLeft(), 0.00001);
        assertEquals(4, testRect.getBottom(), 0.00001);

        testRect.extend(-2, 4);
        assertEquals(-2, testRect.getLeft(), 0.00001);
        assertEquals(4, testRect.getBottom(), 0.00001);
    }
}
