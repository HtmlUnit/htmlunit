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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.NativeArray;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.Window;

/**
 * A JavaScript object for {@code DOMMatrixReadOnly}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class DOMMatrixReadOnly extends HtmlUnitScriptable {

    private double m11_;
    private double m12_;
    private double m13_;
    private double m14_;

    private double m21_;
    private double m22_;
    private double m23_;
    private double m24_;

    private double m31_;
    private double m32_;
    private double m33_;
    private double m34_;

    private double m41_;
    private double m42_;
    private double m43_;
    private double m44_;

    private boolean is2D_;

    /**
     * Ctor.
     */
    public DOMMatrixReadOnly() {
        m11_ = 1;
        m12_ = 0;
        m13_ = 0;
        m14_ = 0;

        m21_ = 0;
        m22_ = 1;
        m23_ = 0;
        m24_ = 0;

        m31_ = 0;
        m32_ = 0;
        m33_ = 1;
        m34_ = 0;

        m41_ = 0;
        m42_ = 0;
        m43_ = 0;
        m44_ = 1;

        is2D_ = true;
    }

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments to the WebSocket constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static DOMMatrixReadOnly jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {

        final DOMMatrixReadOnly matrix = new DOMMatrixReadOnly();
        matrix.init(args, ctorObj);
        return matrix;
    }

    protected void init(final Object[] args, final Function ctorObj) {
        final Window window = getWindow(ctorObj);
        setParentScope(window);
        setPrototype(((FunctionObject) ctorObj).getClassPrototype());

        if (args.length == 0 || JavaScriptEngine.isUndefined(args[0])) {
            return;
        }

        if (args.length > 0) {
            if (args[0] instanceof NativeArray) {
                final NativeArray arrayArgs = (NativeArray) args[0];
                if (arrayArgs.getLength() == 6) {
                    m11_ = JavaScriptEngine.toNumber(arrayArgs.get(0));
                    m12_ = JavaScriptEngine.toNumber(arrayArgs.get(1));

                    m21_ = JavaScriptEngine.toNumber(arrayArgs.get(2));
                    m22_ = JavaScriptEngine.toNumber(arrayArgs.get(3));

                    m41_ = JavaScriptEngine.toNumber(arrayArgs.get(4));
                    m42_ = JavaScriptEngine.toNumber(arrayArgs.get(5));

                    is2D_ = true;
                    return;
                }

                if (arrayArgs.getLength() == 16) {
                    m11_ = JavaScriptEngine.toNumber(arrayArgs.get(0));
                    m12_ = JavaScriptEngine.toNumber(arrayArgs.get(1));
                    m13_ = JavaScriptEngine.toNumber(arrayArgs.get(2));
                    m14_ = JavaScriptEngine.toNumber(arrayArgs.get(3));

                    m21_ = JavaScriptEngine.toNumber(arrayArgs.get(4));
                    m22_ = JavaScriptEngine.toNumber(arrayArgs.get(5));
                    m23_ = JavaScriptEngine.toNumber(arrayArgs.get(6));
                    m24_ = JavaScriptEngine.toNumber(arrayArgs.get(7));

                    m31_ = JavaScriptEngine.toNumber(arrayArgs.get(8));
                    m32_ = JavaScriptEngine.toNumber(arrayArgs.get(9));
                    m33_ = JavaScriptEngine.toNumber(arrayArgs.get(10));
                    m34_ = JavaScriptEngine.toNumber(arrayArgs.get(11));

                    m41_ = JavaScriptEngine.toNumber(arrayArgs.get(12));
                    m42_ = JavaScriptEngine.toNumber(arrayArgs.get(13));
                    m43_ = JavaScriptEngine.toNumber(arrayArgs.get(14));
                    m44_ = JavaScriptEngine.toNumber(arrayArgs.get(15));

                    is2D_ = false;
                    return;
                }

                throw JavaScriptEngine.typeError("DOMMatrixReadOnly constructor: Matrix init sequence must have "
                        + "a length of 6 or 16 (actual value: " + arrayArgs.getLength() + ")");
            }
        }

        throw JavaScriptEngine.asJavaScriptException(
                window,
                "An invalid or illegal string was specified",
                DOMException.SYNTAX_ERR);
    }

    /**
     * @return m11
     */
    @JsxGetter
    public double getM11() {
        return m11_;
    }

    /**
     * @return a
     */
    @JsxGetter
    public double getA() {
        return m11_;
    }

    /**
     * @return m12
     */
    @JsxGetter
    public double getM12() {
        return m12_;
    }

    /**
     * @return b
     */
    @JsxGetter
    public double getB() {
        return m12_;
    }

    /**
     * @return m13
     */
    @JsxGetter
    public double getM13() {
        return m13_;
    }

    /**
     * @return m14
     */
    @JsxGetter
    public double getM14() {
        return m14_;
    }

    /**
     * @return m21
     */
    @JsxGetter
    public double getM21() {
        return m21_;
    }

    /**
     * @return c
     */
    @JsxGetter
    public double getC() {
        return m21_;
    }

    /**
     * @return m22
     */
    @JsxGetter
    public double getM22() {
        return m22_;
    }

    /**
     * @return d
     */
    @JsxGetter
    public double getD() {
        return m22_;
    }

    /**
     * @return m23
     */
    @JsxGetter
    public double getM23() {
        return m23_;
    }

    /**
     * @return m24
     */
    @JsxGetter
    public double getM24() {
        return m24_;
    }

    /**
     * @return m31
     */
    @JsxGetter
    public double getM31() {
        return m31_;
    }

    /**
     * @return m32
     */
    @JsxGetter
    public double getM32() {
        return m32_;
    }

    /**
     * @return m33
     */
    @JsxGetter
    public double getM33() {
        return m33_;
    }

    /**
     * @return m34
     */
    @JsxGetter
    public double getM34() {
        return m34_;
    }

    /**
     * @return m41
     */
    @JsxGetter
    public double getM41() {
        return m41_;
    }

    /**
     * @return e
     */
    @JsxGetter
    public double getE() {
        return m41_;
    }

    /**
     * @return m42
     */
    @JsxGetter
    public double getM42() {
        return m42_;
    }

    /**
     * @return f
     */
    @JsxGetter
    public double getF() {
        return m42_;
    }

    /**
     * @return m43
     */
    @JsxGetter
    public double getM43() {
        return m43_;
    }

    /**
     * @return m44
     */
    @JsxGetter
    public double getM44() {
        return m44_;
    }

    /**
     * @return is2d
     */
    @JsxGetter
    public boolean getIs2D() {
        return is2D_;
    }

    /**
     * @return a new matrix being the result of the original matrix flipped about the x-axis.
     *     This is equivalent to multiplying the matrix by DOMMatrix(-1, 0, 0, 1, 0, 0).
     *     The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrixReadOnly flipX() {
        final DOMMatrixReadOnly matrix = new DOMMatrixReadOnly();
        final Window window = getWindow();
        matrix.setParentScope(window);
        matrix.setPrototype(window.getPrototype(DOMMatrixReadOnly.class));

        matrix.m11_ = -m11_;
        matrix.m12_ = -m12_;
        matrix.m13_ = -m13_;
        matrix.m14_ = -m14_;

        matrix.m21_ = m21_;
        matrix.m22_ = m22_;
        matrix.m23_ = m23_;
        matrix.m24_ = m24_;

        matrix.m31_ = m31_;
        matrix.m32_ = m32_;
        matrix.m33_ = m33_;
        matrix.m34_ = m34_;

        matrix.m41_ = m41_;
        matrix.m42_ = m42_;
        matrix.m43_ = m43_;
        matrix.m44_ = m44_;

        matrix.is2D_ = is2D_;
        return matrix;
    }

    /**
     * @return a new matrix being the result of the original matrix flipped about the x-axis.
     *     This is equivalent to multiplying the matrix by DOMMatrix(1, 0, 0, -1, 0, 0).
     *     The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrixReadOnly flipY() {
        final DOMMatrixReadOnly matrix = new DOMMatrixReadOnly();
        final Window window = getWindow();
        matrix.setParentScope(window);
        matrix.setPrototype(window.getPrototype(DOMMatrixReadOnly.class));

        matrix.m11_ = m11_;
        matrix.m12_ = m12_;
        matrix.m13_ = m13_;
        matrix.m14_ = m14_;

        matrix.m21_ = -m21_;
        matrix.m22_ = -m22_;
        matrix.m23_ = -m23_;
        matrix.m24_ = -m24_;

        matrix.m31_ = m31_;
        matrix.m32_ = m32_;
        matrix.m33_ = m33_;
        matrix.m34_ = m34_;

        matrix.m41_ = m41_;
        matrix.m42_ = m42_;
        matrix.m43_ = m43_;
        matrix.m44_ = m44_;

        matrix.is2D_ = is2D_;
        return matrix;
    }

    /**
     * @return new matrix which is the inverse of the original matrix.
     *     If the matrix cannot be inverted, the new matrix's components are all set to NaN
     *     and its is2D property is set to false. The original matrix is not changed.
     */
    @JsxFunction
    public DOMMatrixReadOnly inverse() {
        final DOMMatrixReadOnly matrix = new DOMMatrixReadOnly();
        final Window window = getWindow();
        matrix.setParentScope(window);
        matrix.setPrototype(window.getPrototype(DOMMatrixReadOnly.class));

        if (is2D_) {
            final double det = m11_ * m22_ - m12_ * m21_;
            if (det == 0) {
                // Not invertible: set all to NaN, is2D_ = false
                initWithNan(matrix);
                return matrix;
            }

            matrix.m11_ = m22_ / det;
            matrix.m12_ = -m12_ / det;

            matrix.m21_ = -m21_ / det;
            matrix.m22_ = m11_ / det;

            matrix.m41_ = (m21_ * m42_ - m22_ * m41_) / det;
            matrix.m42_ = (m12_ * m41_ - m11_ * m42_) / det;

            matrix.is2D_ = true;
            return matrix;
        }

        final double[] inv = new double[16];
        inv[0] = m22_ * m33_ * m44_
                - m22_ * m34_ * m43_
                - m32_ * m23_ * m44_
                + m32_ * m24_ * m43_
                + m42_ * m23_ * m34_
                - m42_ * m24_ * m33_;

        inv[4] = -m21_ * m33_ * m44_
                + m21_ * m34_ * m43_
                + m31_ * m23_ * m44_
                - m31_ * m24_ * m43_
                - m41_ * m23_ * m34_
                + m41_ * m24_ * m33_;

        inv[8] = m21_ * m32_ * m44_
                - m21_ * m34_ * m42_
                - m31_ * m22_ * m44_
                + m31_ * m24_ * m42_
                + m41_ * m22_ * m34_
                - m41_ * m24_ * m32_;

        inv[12] = -m21_ * m32_ * m43_
                + m21_ * m33_ * m42_
                + m31_ * m22_ * m43_
                - m31_ * m23_ * m42_
                - m41_ * m22_ * m33_
                + m41_ * m23_ * m32_;

        inv[1] = -m12_ * m33_ * m44_
                + m12_ * m34_ * m43_
                + m32_ * m13_ * m44_
                - m32_ * m14_ * m43_
                - m42_ * m13_ * m34_
                + m42_ * m14_ * m33_;

        inv[5] = m11_ * m33_ * m44_
                - m11_ * m34_ * m43_
                - m31_ * m13_ * m44_
                + m31_ * m14_ * m43_
                + m41_ * m13_ * m34_
                - m41_ * m14_ * m33_;

        inv[9] = -m11_ * m32_ * m44_
                + m11_ * m34_ * m42_
                + m31_ * m12_ * m44_
                - m31_ * m14_ * m42_
                - m41_ * m12_ * m34_
                + m41_ * m14_ * m32_;

        inv[13] = m11_ * m32_ * m43_
                - m11_ * m33_ * m42_
                - m31_ * m12_ * m43_
                + m31_ * m13_ * m42_
                + m41_ * m12_ * m33_
                - m41_ * m13_ * m32_;

        inv[2] = m12_ * m23_ * m44_
                - m12_ * m24_ * m43_
                - m22_ * m13_ * m44_
                + m22_ * m14_ * m43_
                + m42_ * m13_ * m24_
                - m42_ * m14_ * m23_;

        inv[6] = -m11_ * m23_ * m44_
                + m11_ * m24_ * m43_
                + m21_ * m13_ * m44_
                - m21_ * m14_ * m43_
                - m41_ * m13_ * m24_
                + m41_ * m14_ * m23_;

        inv[10] = m11_ * m22_ * m44_
                - m11_ * m24_ * m42_
                - m21_ * m12_ * m44_
                + m21_ * m14_ * m42_
                + m41_ * m12_ * m24_
                - m41_ * m14_ * m22_;

        inv[14] = -m11_ * m22_ * m43_
                + m11_ * m23_ * m42_
                + m21_ * m12_ * m43_
                - m21_ * m13_ * m42_
                - m41_ * m12_ * m23_
                + m41_ * m13_ * m22_;

        inv[3] = -m12_ * m23_ * m34_
                + m12_ * m24_ * m33_
                + m22_ * m13_ * m34_
                - m22_ * m14_ * m33_
                - m32_ * m13_ * m24_
                + m32_ * m14_ * m23_;

        inv[7] = m11_ * m23_ * m34_
                - m11_ * m24_ * m33_
                - m21_ * m13_ * m34_
                + m21_ * m14_ * m33_
                + m31_ * m13_ * m24_
                - m31_ * m14_ * m23_;

        inv[11] = -m11_ * m22_ * m34_
                + m11_ * m24_ * m32_
                + m21_ * m12_ * m34_
                - m21_ * m14_ * m32_
                - m31_ * m12_ * m24_
                + m31_ * m14_ * m22_;

        inv[15] = m11_ * m22_ * m33_
                - m11_ * m23_ * m32_
                - m21_ * m12_ * m33_
                + m21_ * m13_ * m32_
                + m31_ * m12_ * m23_
                - m31_ * m13_ * m22_;

        double det = m11_ * inv[0] + m12_ * inv[4] + m13_ * inv[8] + m14_ * inv[12];

        if (det == 0) {
            // Not invertible: set all to NaN, is2D_ = false
            initWithNan(matrix);
            return matrix;
        }

        det = 1.0 / det;

        matrix.m11_ = inv[0] * det;
        matrix.m12_ = inv[1] * det;
        matrix.m13_ = inv[2] * det;
        matrix.m14_ = inv[3] * det;

        matrix.m21_ = inv[4] * det;
        matrix.m22_ = inv[5] * det;
        matrix.m23_ = inv[6] * det;
        matrix.m24_ = inv[7] * det;

        matrix.m31_ = inv[8] * det;
        matrix.m32_ = inv[9] * det;
        matrix.m33_ = inv[10] * det;
        matrix.m34_ = inv[11] * det;

        matrix.m41_ = inv[12] * det;
        matrix.m42_ = inv[13] * det;
        matrix.m43_ = inv[14] * det;
        matrix.m44_ = inv[15] * det;

        matrix.is2D_ = false;
        return matrix;
    }

    private static void initWithNan(final DOMMatrixReadOnly matrix) {
        matrix.m11_ = Double.NaN;
        matrix.m12_ = Double.NaN;
        matrix.m13_ = Double.NaN;
        matrix.m14_ = Double.NaN;
        matrix.m21_ = Double.NaN;
        matrix.m22_ = Double.NaN;
        matrix.m23_ = Double.NaN;
        matrix.m24_ = Double.NaN;
        matrix.m31_ = Double.NaN;
        matrix.m32_ = Double.NaN;
        matrix.m33_ = Double.NaN;
        matrix.m34_ = Double.NaN;
        matrix.m41_ = Double.NaN;
        matrix.m42_ = Double.NaN;
        matrix.m43_ = Double.NaN;
        matrix.m44_ = Double.NaN;
        matrix.is2D_ = false;
    }

    /**
     * @param other the matrix to multiply with this matrix
     * @return a new matrix which is the dot product of the matrix and the otherMatrix parameter.
     *     If otherMatrix is omitted, the matrix is multiplied by a matrix in which every element
     *     is 0 except the bottom-right corner and the element immediately above
     *     and to its left: m33 and m34. These have the default value of 1.
     *     The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrixReadOnly multiply(final Object other) {
        final DOMMatrixReadOnly result = new DOMMatrixReadOnly();
        final Window window = getWindow();
        result.setParentScope(window);
        result.setPrototype(window.getPrototype(DOMMatrixReadOnly.class));

        // Handle null/undefined by treating as identity matrix
        if (other == null || JavaScriptEngine.isUndefined(other)) {
            return result;
        }

        if (!(other instanceof DOMMatrixReadOnly)) {
            throw JavaScriptEngine.typeError("Failed to execute 'multiply' on 'DOMMatrixReadOnly': "
                    + "parameter 1 is not of type 'DOMMatrixReadOnly'.");
        }

        final DOMMatrixReadOnly otherMatrix = (DOMMatrixReadOnly) other;

        // Matrix multiplication: result = this * otherMatrix
        // Standard matrix multiplication formula: C[i][j] = sum(A[i][k] * B[k][j])
        result.is2D_ = is2D_ && otherMatrix.is2D_;

        result.m11_ = m11_ * otherMatrix.m11_
                + m21_ * otherMatrix.m12_
                + m31_ * otherMatrix.m13_
                + m41_ * otherMatrix.m14_;
        result.m12_ = m12_ * otherMatrix.m11_
                + m22_ * otherMatrix.m12_
                + m32_ * otherMatrix.m13_
                + m42_ * otherMatrix.m14_;
        if (!result.is2D_) {
            result.m13_ = m13_ * otherMatrix.m11_
                    + m23_ * otherMatrix.m12_
                    + m33_ * otherMatrix.m13_
                    + m43_ * otherMatrix.m14_;
            result.m14_ = m14_ * otherMatrix.m11_
                    + m24_ * otherMatrix.m12_
                    + m34_ * otherMatrix.m13_
                    + m44_ * otherMatrix.m14_;
        }

        result.m21_ = m11_ * otherMatrix.m21_
                + m21_ * otherMatrix.m22_
                + m31_ * otherMatrix.m23_
                + m41_ * otherMatrix.m24_;
        result.m22_ = m12_ * otherMatrix.m21_
                + m22_ * otherMatrix.m22_
                + m32_ * otherMatrix.m23_
                + m42_ * otherMatrix.m24_;
        if (!result.is2D_) {
            result.m23_ = m13_ * otherMatrix.m21_
                    + m23_ * otherMatrix.m22_
                    + m33_ * otherMatrix.m23_
                    + m43_ * otherMatrix.m24_;
            result.m24_ = m14_ * otherMatrix.m21_
                    + m24_ * otherMatrix.m22_
                    + m34_ * otherMatrix.m23_
                    + m44_ * otherMatrix.m24_;
        }

        if (!result.is2D_) {
            result.m31_ = m11_ * otherMatrix.m31_
                    + m21_ * otherMatrix.m32_
                    + m31_ * otherMatrix.m33_
                    + m41_ * otherMatrix.m34_;
            result.m32_ = m12_ * otherMatrix.m31_
                    + m22_ * otherMatrix.m32_
                    + m32_ * otherMatrix.m33_
                    + m42_ * otherMatrix.m34_;
            result.m33_ = m13_ * otherMatrix.m31_
                    + m23_ * otherMatrix.m32_
                    + m33_ * otherMatrix.m33_
                    + m43_ * otherMatrix.m34_;
            result.m34_ = m14_ * otherMatrix.m31_
                    + m24_ * otherMatrix.m32_
                    + m34_ * otherMatrix.m33_
                    + m44_ * otherMatrix.m34_;
        }

        result.m41_ = m11_ * otherMatrix.m41_
                + m21_ * otherMatrix.m42_
                + m31_ * otherMatrix.m43_
                + m41_ * otherMatrix.m44_;
        result.m42_ = m12_ * otherMatrix.m41_
                + m22_ * otherMatrix.m42_
                + m32_ * otherMatrix.m43_
                + m42_ * otherMatrix.m44_;
        if (!result.is2D_) {
            result.m43_ = m13_ * otherMatrix.m41_
                    + m23_ * otherMatrix.m42_
                    + m33_ * otherMatrix.m43_
                    + m43_ * otherMatrix.m44_;
            result.m44_ = m14_ * otherMatrix.m41_
                    + m24_ * otherMatrix.m42_
                    + m34_ * otherMatrix.m43_
                    + m44_ * otherMatrix.m44_;
        }

        return result;
    }

    /**
     * @param rotZ the rotation angle in degrees. If omitted, defaults to 0.
     * @return a new matrix which is the result of the original matrix rotated by the specified angle.
     *     The rotation is applied around the origin (0, 0) in the 2D plane.
     *     The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrixReadOnly rotate(final Object rotZ) {
        final DOMMatrixReadOnly result = new DOMMatrixReadOnly();
        final Window window = getWindow();
        result.setParentScope(window);
        result.setPrototype(window.getPrototype(DOMMatrixReadOnly.class));

        // Handle undefined/null/missing parameter - default to 0
        double angleInDegrees = 0;
        if (rotZ != null && !JavaScriptEngine.isUndefined(rotZ)) {
            angleInDegrees = JavaScriptEngine.toNumber(rotZ);
        }

        // Convert degrees to radians
        final double angleInRadians = Math.toRadians(angleInDegrees);
        final double cos = Math.cos(angleInRadians);
        final double sin = Math.sin(angleInRadians);

        // Create rotation matrix:
        // [cos  -sin  0  0]
        // [sin   cos  0  0]
        // [ 0     0   1  0]
        // [ 0     0   0  1]

        // For 2D matrices, only apply to the 2x2 part
        if (is2D_) {
            // Matrix multiplication: result = this * rotation
            result.m11_ = m11_ * cos + m21_ * sin;
            result.m12_ = m12_ * cos + m22_ * sin;

            result.m21_ = m11_ * (-sin) + m21_ * cos;
            result.m22_ = m12_ * (-sin) + m22_ * cos;

            result.m41_ = m41_;
            result.m42_ = m42_;

            result.is2D_ = true;
        }
        else {
            // For 3D matrices, apply rotation to all relevant components
            result.m11_ = m11_ * cos + m21_ * sin;
            result.m12_ = m12_ * cos + m22_ * sin;
            result.m13_ = m13_ * cos + m23_ * sin;
            result.m14_ = m14_ * cos + m24_ * sin;

            result.m21_ = m11_ * (-sin) + m21_ * cos;
            result.m22_ = m12_ * (-sin) + m22_ * cos;
            result.m23_ = m13_ * (-sin) + m23_ * cos;
            result.m24_ = m14_ * (-sin) + m24_ * cos;

            result.m31_ = m31_;
            result.m32_ = m32_;
            result.m33_ = m33_;
            result.m34_ = m34_;

            result.m41_ = m41_;
            result.m42_ = m42_;
            result.m43_ = m43_;
            result.m44_ = m44_;

            result.is2D_ = false;
        }

        return result;
    }
}
