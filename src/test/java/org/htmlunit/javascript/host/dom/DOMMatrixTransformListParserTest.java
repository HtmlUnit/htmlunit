/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.dom;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link DOMMatrixTransformListParser}.
 *
 * @author Ronald Brill
 */
public class DOMMatrixTransformListParserTest {

    private static final double DELTA = 1e-9;

    private static final double[] IDENTITY = {
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1};

    private static DOMMatrixReadOnly parse(final String transformList) {
        final DOMMatrixReadOnly matrix = new DOMMatrixReadOnly();
        DOMMatrixTransformListParser.parse(transformList, matrix);
        return matrix;
    }

    private static void assert2D(final String transformList,
            final double a, final double b, final double c, final double d, final double e, final double f) {
        final DOMMatrixReadOnly matrix = parse(transformList);
        assertTrue(matrix.isIs2D(), transformList + " is2D");
        assertArrayEquals(new double[] {a, b, 0, 0, c, d, 0, 0, 0, 0, 1, 0, e, f, 0, 1},
                matrix.getValues(), DELTA, transformList);
    }

    private static void assert3D(final String transformList, final double... expected) {
        final DOMMatrixReadOnly matrix = parse(transformList);
        assertFalse(matrix.isIs2D(), transformList + " is2D");
        assertArrayEquals(expected, matrix.getValues(), DELTA, transformList);
    }

    private static void assertInvalid(final String transformList) {
        final DOMMatrixReadOnly matrix = new DOMMatrixReadOnly();
        matrix.setValues2D(1, 2, 3, 4, 5, 6);

        assertThrows(IllegalArgumentException.class,
                () -> DOMMatrixTransformListParser.parse(transformList, matrix), transformList);

        // the matrix is not changed
        assertArrayEquals(new double[] {1, 2, 0, 0, 3, 4, 0, 0, 0, 0, 1, 0, 5, 6, 0, 1},
                matrix.getValues(), DELTA, transformList);
        assertTrue(matrix.isIs2D(), transformList);
    }

    /**
     * The empty string and 'none' are the identity matrix.
     */
    @Test
    public void identity() {
        assert2D("", 1, 0, 0, 1, 0, 0);
        assert2D("   ", 1, 0, 0, 1, 0, 0);
        assert2D("none", 1, 0, 0, 1, 0, 0);
        assert2D("NONE", 1, 0, 0, 1, 0, 0);
        assert2D(" \t\nnone\r\f", 1, 0, 0, 1, 0, 0);

        // the previous values are replaced
        final DOMMatrixReadOnly matrix = new DOMMatrixReadOnly();
        matrix.setValues(new double[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}, false);
        DOMMatrixTransformListParser.parse("none", matrix);
        assertArrayEquals(IDENTITY, matrix.getValues(), DELTA);
        assertTrue(matrix.isIs2D());
    }

    /**
     * Tests the matrix() and matrix3d() functions.
     */
    @Test
    public void matrix() {
        assert2D("matrix(1, 2, 3, 4, 5, 6)", 1, 2, 3, 4, 5, 6);
        assert2D("matrix(1,2,3,4,5,6)", 1, 2, 3, 4, 5, 6);
        assert2D("matrix( 1 , 2 , 3 , 4 , 5 , 6 )", 1, 2, 3, 4, 5, 6);
        assert2D("matrix(+1, -2, .5, 1e1, 1.5E-1, -0.25)", 1, -2, 0.5, 10, 0.15, -0.25);

        assert3D("matrix3d(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)",
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

        // a 3D function results in a 3D matrix, even if the values are 2D
        assert3D("matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)", IDENTITY);

        // the toString() format of Java doubles
        assert2D("matrix(1.0E-5, 0, 0, 1, 1.0E21, 0)", 1.0E-5, 0, 0, 1, 1.0E21, 0);
    }

    /**
     * Tests the translate() functions.
     */
    @Test
    public void translate() {
        assert2D("translate(10px, 20px)", 1, 0, 0, 1, 10, 20);
        assert2D("translate(10px)", 1, 0, 0, 1, 10, 0);
        assert2D("translateX(10px)", 1, 0, 0, 1, 10, 0);
        assert2D("translateY(10px)", 1, 0, 0, 1, 0, 10);
        assert2D("translate(0, 0)", 1, 0, 0, 1, 0, 0);
        assert2D("translate(-10.5px, +2px)", 1, 0, 0, 1, -10.5, 2);

        assert3D("translateZ(10px)", 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 10, 1);
        assert3D("translateZ(0)", IDENTITY);
        assert3D("translate3d(1px, 2px, 3px)", 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 2, 3, 1);
    }

    /**
     * The absolute length units.
     */
    @Test
    public void lengthUnits() {
        assert2D("translateX(1in)", 1, 0, 0, 1, 96, 0);
        assert2D("translateX(2.54cm)", 1, 0, 0, 1, 96, 0);
        assert2D("translateX(25.4mm)", 1, 0, 0, 1, 96, 0);
        assert2D("translateX(101.6q)", 1, 0, 0, 1, 96, 0);
        assert2D("translateX(72pt)", 1, 0, 0, 1, 96, 0);
        assert2D("translateX(6pc)", 1, 0, 0, 1, 96, 0);
        assert2D("translateX(10PX)", 1, 0, 0, 1, 10, 0);
    }

    /**
     * Tests the scale() functions.
     */
    @Test
    public void scale() {
        assert2D("scale(2, 3)", 2, 0, 0, 3, 0, 0);
        assert2D("scale(2)", 2, 0, 0, 2, 0, 0);
        assert2D("scale(50%)", 0.5, 0, 0, 0.5, 0, 0);
        assert2D("scale(50%, 200%)", 0.5, 0, 0, 2, 0, 0);
        assert2D("scaleX(2)", 2, 0, 0, 1, 0, 0);
        assert2D("scaleY(2)", 1, 0, 0, 2, 0, 0);

        assert3D("scaleZ(2)", 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1);
        assert3D("scale3d(2, 3, 4)", 2, 0, 0, 0, 0, 3, 0, 0, 0, 0, 4, 0, 0, 0, 0, 1);
    }

    /**
     * Tests the rotate() functions.
     */
    @Test
    public void rotate() {
        // multiples of 90 degrees are exact
        assert2D("rotate(90deg)", 0, 1, -1, 0, 0, 0);
        assert2D("rotate(180deg)", -1, 0, 0, -1, 0, 0);
        assert2D("rotate(-90deg)", 0, -1, 1, 0, 0, 0);
        assert2D("rotate(450deg)", 0, 1, -1, 0, 0, 0);
        assert2D("rotate(0)", 1, 0, 0, 1, 0, 0);

        final double cos30 = Math.cos(Math.toRadians(30));
        assert2D("rotate(30deg)", cos30, 0.5, -0.5, cos30, 0, 0);

        assert3D("rotateX(90deg)", 1, 0, 0, 0, 0, 0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 1);
        assert3D("rotateY(90deg)", 0, 0, -1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1);
        // like in the browsers, rotateZ() is a 2D function
        assert2D("rotateZ(90deg)", 0, 1, -1, 0, 0, 0);
        assert3D("rotate3d(0, 0, 1, 90deg)", 0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
        assert3D("rotate3d(0, 0, 0, 90deg)", IDENTITY);
    }

    /**
     * The angle units.
     */
    @Test
    public void angleUnits() {
        assert2D("rotate(0.25turn)", 0, 1, -1, 0, 0, 0);
        assert2D("rotate(100grad)", 0, 1, -1, 0, 0, 0);
        assert2D("rotate(1.5707963267948966rad)", 0, 1, -1, 0, 0, 0);
        assert2D("rotate(90DEG)", 0, 1, -1, 0, 0, 0);
    }

    /**
     * Tests the skew() functions.
     */
    @Test
    public void skew() {
        final double tan30 = Math.tan(Math.toRadians(30));
        final double tan20 = Math.tan(Math.toRadians(20));
        assert2D("skew(30deg, 20deg)", 1, tan20, tan30, 1, 0, 0);
        assert2D("skew(30deg)", 1, 0, tan30, 1, 0, 0);
        assert2D("skewX(30deg)", 1, 0, tan30, 1, 0, 0);
        assert2D("skewY(30deg)", 1, tan30, 0, 1, 0, 0);
    }

    /**
     * Tests the perspective() function.
     */
    @Test
    public void perspective() {
        assert3D("perspective(100px)", 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, -0.01, 0, 0, 0, 1);
        assert3D("perspective(none)", IDENTITY);

        // values below 1px are treated as 1px
        assert3D("perspective(0)", 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, -1, 0, 0, 0, 1);
        assert3D("perspective(0.5px)", 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, -1, 0, 0, 0, 1);

        assertInvalid("perspective(-10px)");
    }

    /**
     * The functions are applied from left to right (post-multiplied).
     */
    @Test
    public void list() {
        // translate first, the rotation does not change the translation
        assert2D("translate(10px, 20px) rotate(90deg)", 0, 1, -1, 0, 10, 20);
        // rotate first, the translation is rotated
        assert2D("rotate(90deg) translate(10px, 20px)", 0, 1, -1, 0, -20, 10);

        assert2D("translate(10px, 20px) rotate(90deg) scale(2)", 0, 2, -2, 0, 10, 20);
        assert2D("translate(10px,20px)scale(2)", 2, 0, 0, 2, 10, 20);
        assert2D("  translate( 10px , 20px )  scale( 2 )  ", 2, 0, 0, 2, 10, 20);
        assert2D("TRANSLATE(10PX, 20Px) Scale(2)", 2, 0, 0, 2, 10, 20);

        // one 3D function makes the whole matrix 3D
        assert3D("translate(10px, 20px) translateZ(0)", 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 10, 20, 0, 1);
    }

    /**
     * Like in CSS, the end of the input closes the last function.
     */
    @Test
    public void notClosed() {
        assert2D("translate(10px", 1, 0, 0, 1, 10, 0);
        assert2D("translate(10px, 20px) scale(2", 2, 0, 0, 2, 10, 20);
        assert2D("translate(10px, 20px", 1, 0, 0, 1, 10, 20);
        assertInvalid("translate(10px scale(2)");
        assertInvalid("translate(");
    }

    /**
     * Invalid transform lists.
     */
    @Test
    public void invalid() {
        assertInvalid("invalid");
        assertInvalid("foo(10px)");
        assertInvalid("translate10px)");
        assertInvalid("translate(10px)x");
        assertInvalid("translate(10px), scale(2)");
        assertInvalid("(10px)");
        assertInvalid("none translate(10px)");
        assertInvalid("[object DOMMatrix]");

        // wrong number of arguments
        assertInvalid("matrix(1, 2, 3)");
        assertInvalid("matrix(1, 2, 3, 4, 5, 6, 7)");
        assertInvalid("translate()");
        assertInvalid("translate(1px, 2px, 3px)");
        assertInvalid("translate3d(1px, 2px)");
        assertInvalid("rotate(1deg, 2deg)");
        assertInvalid("rotate3d(1, 0, 0)");
        assertInvalid("translate(1px,)");
        assertInvalid("translate(,1px)");
    }

    /**
     * Invalid values.
     */
    @Test
    public void invalidValues() {
        // lengths need a unit (except 0)
        assertInvalid("translate(10)");
        // relative units can't be resolved
        assertInvalid("translate(1em)");
        assertInvalid("translate(1rem)");
        assertInvalid("translate(1vw)");
        assertInvalid("translate(10%)");
        assertInvalid("translate(10deg)");

        // angles need a unit (except 0)
        assertInvalid("rotate(90)");
        assertInvalid("rotate(90px)");

        // numbers have no unit
        assertInvalid("matrix(1px, 0, 0, 1, 0, 0)");
        assertInvalid("scale(2px)");
        assertInvalid("rotate3d(1px, 0, 0, 10deg)");

        // no valid numbers
        assertInvalid("translate(px)");
        assertInvalid("translate(1.px)");
        assertInvalid("translate(1 px)");
        assertInvalid("matrix(NaN, 0, 0, 1, 0, 0)");
        assertInvalid("matrix(Infinity, 0, 0, 1, 0, 0)");
        assertInvalid("scale(calc(2))");
    }
}
