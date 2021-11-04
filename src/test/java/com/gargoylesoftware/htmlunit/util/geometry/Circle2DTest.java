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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link Rectangle2D}.
 *
 * @author Ronald Brill
 */
public class Circle2DTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void contains() throws Exception {
        assertTrue(new Circle2D(1, 2, 3).contains(1, 2));
        assertTrue(new Circle2D(1, 2, 3).contains(4, 2));
        assertTrue(new Circle2D(1, 2, 3).contains(-2, 2));
        assertTrue(new Circle2D(1, 2, 3).contains(1, 5));
        assertTrue(new Circle2D(1, 2, 3).contains(1, -1));

        assertFalse(new Circle2D(1, 2, 3).contains(4.001, 1));
        assertFalse(new Circle2D(1, 2, 3).contains(-2.001, 2));
        assertFalse(new Circle2D(1, 2, 3).contains(1, 5.001));
        assertFalse(new Circle2D(1, 2, 3).contains(1, -1.001));
    }
}
