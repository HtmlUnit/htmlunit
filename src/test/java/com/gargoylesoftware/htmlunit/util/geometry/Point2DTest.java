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

import org.junit.Test;

/**
 * Tests for {@link Point2D}.
 *
 * @author Ronald Brill
 */
public class Point2DTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        assertEquals(0.0, new Point2D(0, 1).getX(), 0.00001);
        assertEquals(1.0, new Point2D(0, 1).getY(), 0.00001);
    }
}
