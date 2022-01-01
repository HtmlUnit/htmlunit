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
package com.gargoylesoftware.htmlunit.util.geometry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link Polygon2D}.
 * Some tests cases are taken from
 * https://github.com/sromku/polygon-contains-point/blob/master/src/test/java/Tests.java
 *
 * @author Ronald Brill
 */
public class Polygon2DTest {

    @Test
    public void testSimplePolygon() {
        final Polygon2D polygon =
                new Polygon2D(1, 3)
                    .lineTo(2, 8)
                    .lineTo(5, 4)
                    .lineTo(5, 9)
                    .lineTo(7, 5)
                    .lineTo(6, 1)
                    .lineTo(3, 1);

        assertTrue(polygon.contains(5.5, 7));
        assertFalse(polygon.contains(4.5, 7));

        assertTrue(polygon.contains(5, 9));
        assertTrue(polygon.contains(2, 2));

        assertFalse(polygon.contains(100, 200));
    }

    @Test
    public void testPolygonFigure6() {
        // example 1
        Polygon2D polygon =
                new Polygon2D(1, 3)
                    .lineTo(9, 3)
                    .lineTo(9, 7)
                    .lineTo(7, 5)
                    .lineTo(5, 7)
                    .lineTo(3, 5)
                    .lineTo(1, 7);

        assertTrue(polygon.contains(5, 5));

        // example 2
        polygon = new Polygon2D(1, 3)
                    .lineTo(3, 5)
                    .lineTo(5, 3)
                    .lineTo(7, 5)
                    .lineTo(9, 3)
                    .lineTo(9, 7)
                    .lineTo(1, 7);

        assertTrue(polygon.contains(5, 5));
    }

    /**
     * Test issue: https://github.com/sromku/polygon-contains-point/issues/1.
     */
    @Test
    public void testMapCoordinates1() {
        final Polygon2D polygon =
                new Polygon2D(42.499148, 27.485196)
                    .lineTo(42.498600, 27.480000)
                    .lineTo(42.503800, 27.474680)
                    .lineTo(42.510000, 27.468270)
                    .lineTo(42.510788, 27.466904)
                    .lineTo(42.512116, 27.465350)
                    .lineTo(42.512000, 27.467000)
                    .lineTo(42.513579, 27.471027)
                    .lineTo(42.512938, 27.472668)
                    .lineTo(42.511829, 27.474922)
                    .lineTo(42.507945, 27.480124)
                    .lineTo(42.509082, 27.482892)
                    .lineTo(42.536026, 27.490519)
                    .lineTo(42.534470, 27.499703)
                    .lineTo(42.499148, 27.485196);

        assertTrue(polygon.contains(42.508956f, 27.483328f));
        assertTrue(polygon.contains(42.505f, 27.48f));
    }

    /**
     * Test issue: https://github.com/sromku/polygon-contains-point/issues/1.
     */
    @Test
    public void testMapCoordinates2() {
        final Polygon2D polygon =
                new Polygon2D(40.481171, 6.4107070)
                    .lineTo(40.480248, 6.4101200)
                    .lineTo(40.480237, 6.4062790)
                    .lineTo(40.481161, 6.4062610);

        assertTrue(polygon.contains(40.480890f, 6.4081030f));
    }

    /**
     * Test issue: https://github.com/sromku/polygon-contains-point/issues/3.
     */
    @Test
    public void testParallel() {
        final Polygon2D polygon =
                new Polygon2D(0, 0)
                    .lineTo(0, 1)
                    .lineTo(1, 2)
                    .lineTo(1, 99)
                    .lineTo(100, 0);

        assertTrue(polygon.contains(3, 4));
        assertTrue(polygon.contains(3, 4.1));
        assertTrue(polygon.contains(3, 3.9));
    }

    @Test
    public void testBorders() {
        /*
         * Unfortunately, this method won't work if the point is on the edge of the polygon.
         * https://en.wikipedia.org/wiki/Point_in_polygon#Ray_casting_algorithm
         */
        final Polygon2D polygon =
                new Polygon2D(-1, -1)
                    .lineTo(-1, 1)
                    .lineTo(1, 1)
                    .lineTo(1, -1);

        assertTrue(polygon.contains(0, 1));
        assertTrue(polygon.contains(-1, 0));
    }
}
