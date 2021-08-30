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
 * Tests for {@link Line2D}.
 *
 * @author Ronald Brill
 */
public class Line2DTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void contains() throws Exception {
        // y = 4x + 3
        assertTrue(new Line2D(-0.75, 0, 0, 3).contains(-0.75, 0));
        assertTrue(new Line2D(-0.75, 0, 0, 3).contains(0, 3));

        assertTrue(new Line2D(-0.75, 0, 0, 3).contains(-0.5, 1));
        assertTrue(new Line2D(-0.75, 0, 0, 3).contains(-0.2469858418795, 2.0120566324819));

        assertFalse(new Line2D(-0.75, 0, 0, 3).contains(-0.246, 2.0120566324819));
        assertFalse(new Line2D(-0.75, 0, 0, 3).contains(-1, -1));
        assertFalse(new Line2D(-0.75, 0, 0, 3).contains(1, 7));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void containsVertical() throws Exception {
        assertTrue(new Line2D(new Point2D(4, 7), new Point2D(4, 13)).contains(4, 7));
        assertTrue(new Line2D(new Point2D(4, 7), new Point2D(4, 13)).contains(4, 13));
        assertTrue(new Line2D(new Point2D(4, 7), new Point2D(4, 13)).contains(4, 11));

        assertFalse(new Line2D(new Point2D(4, 7), new Point2D(4, 13)).contains(4, 13.1));
        assertFalse(new Line2D(new Point2D(4, 7), new Point2D(4, 13)).contains(4, 6.9));
        assertFalse(new Line2D(new Point2D(4, 7), new Point2D(4, 13)).contains(-1, 11));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void intersect() throws Exception {
        Line2D testLine = new Line2D(new Point2D(4, 7), new Point2D(7, 13));

        Line2D testLine2 = new Line2D(new Point2D(5, 7), new Point2D(2, 10));
        Point2D intersectionPoint = testLine.intersect(testLine2);
        assertEquals(4.333333, intersectionPoint.getX(), 0.00001);
        assertEquals(7.666666, intersectionPoint.getY(), 0.00001);

        testLine2 = new Line2D(new Point2D(0, 6), new Point2D(10, 8));
        intersectionPoint = testLine.intersect(testLine2);
        assertEquals(3.888888, intersectionPoint.getX(), 0.00001);
        assertEquals(6.777777, intersectionPoint.getY(), 0.00001);

        testLine = new Line2D(new Point2D(1, 3), new Point2D(1, 7));
        testLine2 = new Line2D(new Point2D(0.9, 3), new Point2D(5, 5));
        intersectionPoint = testLine.intersect(testLine2);
        assertEquals(1, intersectionPoint.getX(), 0.00001);
        assertEquals(3.0487804, intersectionPoint.getY(), 0.00001);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void intersectParallel() throws Exception {
        final Line2D testLine = new Line2D(new Point2D(4, 7), new Point2D(4, 13));

        final Line2D testLine2 = new Line2D(new Point2D(5, 7), new Point2D(5, 11));
        final Point2D intersectionPoint = testLine.intersect(testLine2);
        assertEquals(null, intersectionPoint);
    }
}
