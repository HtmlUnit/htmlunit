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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.NativeArray;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.json.JsonParser;
import org.htmlunit.corejs.javascript.json.JsonParser.ParseException;
import org.htmlunit.corejs.javascript.typedarrays.NativeFloat32Array;
import org.htmlunit.corejs.javascript.typedarrays.NativeFloat64Array;
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

    private static final Log LOG = LogFactory.getLog(DOMMatrixReadOnly.class);

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
     * @param m11 the new value
     */
    public void setM11(final double m11) {
        m11_ = m11;
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
     * @param m12 the new value
     */
    public void setM12(final double m12) {
        m12_ = m12;
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
     * @param m13 the new value
     */
    public void setM13(final double m13) {
        m13_ = m13;
    }

    /**
     * @return m14
     */
    @JsxGetter
    public double getM14() {
        return m14_;
    }

    /**
     * @param m14 the new value
     */
    public void setM14(final double m14) {
        m14_ = m14;
    }

    /**
     * @return m21
     */
    @JsxGetter
    public double getM21() {
        return m21_;
    }

    /**
     * @param m21 the new value
     */
    public void setM21(final double m21) {
        m21_ = m21;
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
     * @param m22 the new value
     */
    public void setM22(final double m22) {
        m22_ = m22;
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
     * @param m23 the new value
     */
    public void setM23(final double m23) {
        m23_ = m23;
    }

    /**
     * @return m24
     */
    @JsxGetter
    public double getM24() {
        return m24_;
    }

    /**
     * @param m24 the new value
     */
    public void setM24(final double m24) {
        m24_ = m24;
    }

    /**
     * @return m31
     */
    @JsxGetter
    public double getM31() {
        return m31_;
    }

    /**
     * @param m31 the new value
     */
    public void setM31(final double m31) {
        m31_ = m31;
    }

    /**
     * @return m32
     */
    @JsxGetter
    public double getM32() {
        return m32_;
    }

    /**
     * @param m32 the new value
     */
    public void setM32(final double m32) {
        m32_ = m32;
    }

    /**
     * @return m33
     */
    @JsxGetter
    public double getM33() {
        return m33_;
    }

    /**
     * @param m33 the new value
     */
    public void setM33(final double m33) {
        m33_ = m33;
    }

    /**
     * @return m34
     */
    @JsxGetter
    public double getM34() {
        return m34_;
    }

    /**
     * @param m34 the new value
     */
    public void setM34(final double m34) {
        m34_ = m34;
    }

    /**
     * @return m41
     */
    @JsxGetter
    public double getM41() {
        return m41_;
    }

    /**
     * @param m41 the new value
     */
    public void setM41(final double m41) {
        m41_ = m41;
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
     * @param m42 the new value
     */
    public void setM42(final double m42) {
        m42_ = m42;
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
     * @param m43 the new value
     */
    public void setM43(final double m43) {
        m43_ = m43;
    }

    /**
     * @return m44
     */
    @JsxGetter
    public double getM44() {
        return m44_;
    }

    /**
     * @param m44 the new value
     */
    public void setM44(final double m44) {
        m44_ = m44;
    }

    /**
     * @return is2d
     */
    @JsxGetter
    public boolean isIs2D() {
        return is2D_;
    }

    /**
     * @param is2D the new value
     */
    public void setIs2D(final boolean is2D) {
        is2D_ = is2D;
    }

    /**
     * @return true if m12 element, m13 element, m14 element, m21 element, m23 element, m24 element,
     *     m31 element, m32 element, m34 element, m41 element, m42 element, m43 element are 0 or -0
     *     and m11 element, m22 element, m33 element, m44 element are 1. Otherwise it returns false.
     */
    @JsxGetter
    public boolean getIsIdentity() {
        return m11_ == 1 && m22_ == 1 && m33_ == 1 && m44_ == 1
                && m12_ == 0 && m13_ == 0 && m14_ == 0
                && m21_ == 0 && m23_ == 0 && m24_ == 0
                && m31_ == 0 && m32_ == 0 && m34_ == 0
                && m41_ == 0 && m42_ == 0 && m43_ == 0;
    }

    /**
     * @return a new matrix being the result of the original matrix flipped about the x-axis.
     *     This is equivalent to multiplying the matrix by DOMMatrix(-1, 0, 0, 1, 0, 0).
     *     The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix flipX() {
        final DOMMatrix matrix = new DOMMatrix();
        final Window window = getWindow();
        matrix.setParentScope(window);
        matrix.setPrototype(window.getPrototype(DOMMatrix.class));

        matrix.setM11(-m11_);
        matrix.setM12(-m12_);
        matrix.setM13(-m13_);
        matrix.setM14(-m14_);

        matrix.setM21(m21_);
        matrix.setM22(m22_);
        matrix.setM23(m23_);
        matrix.setM24(m24_);

        matrix.setM31(m31_);
        matrix.setM32(m32_);
        matrix.setM33(m33_);
        matrix.setM34(m34_);

        matrix.setM41(m41_);
        matrix.setM42(m42_);
        matrix.setM43(m43_);
        matrix.setM44(m44_);

        matrix.setIs2D(is2D_);
        return matrix;
    }

    /**
     * @return a new matrix being the result of the original matrix flipped about the x-axis.
     *     This is equivalent to multiplying the matrix by DOMMatrix(1, 0, 0, -1, 0, 0).
     *     The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix flipY() {
        final DOMMatrix matrix = new DOMMatrix();
        final Window window = getWindow();
        matrix.setParentScope(window);
        matrix.setPrototype(window.getPrototype(DOMMatrix.class));

        matrix.setM11(m11_);
        matrix.setM12(m12_);
        matrix.setM13(m13_);
        matrix.setM14(m14_);

        matrix.setM21(-m21_);
        matrix.setM22(-m22_);
        matrix.setM23(-m23_);
        matrix.setM24(-m24_);

        matrix.setM31(m31_);
        matrix.setM32(m32_);
        matrix.setM33(m33_);
        matrix.setM34(m34_);

        matrix.setM41(m41_);
        matrix.setM42(m42_);
        matrix.setM43(m43_);
        matrix.setM44(m44_);

        matrix.setIs2D(is2D_);
        return matrix;
    }

    /**
     * @return new matrix which is the inverse of the original matrix.
     *     If the matrix cannot be inverted, the new matrix's components are all set to NaN
     *     and its is2D property is set to false. The original matrix is not changed.
     */
    @JsxFunction
    public DOMMatrix inverse() {
        final DOMMatrix matrix = new DOMMatrix();
        final Window window = getWindow();
        matrix.setParentScope(window);
        matrix.setPrototype(window.getPrototype(DOMMatrix.class));

        matrix.setM11(m11_);
        matrix.setM12(m12_);
        matrix.setM13(m13_);
        matrix.setM14(m14_);

        matrix.setM21(m21_);
        matrix.setM22(m22_);
        matrix.setM23(m23_);
        matrix.setM24(m24_);

        matrix.setM31(m31_);
        matrix.setM32(m32_);
        matrix.setM33(m33_);
        matrix.setM34(m34_);

        matrix.setM41(m41_);
        matrix.setM42(m42_);
        matrix.setM43(m43_);
        matrix.setM44(m44_);

        matrix.setIs2D(is2D_);
        return matrix.invertSelf();
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
    public DOMMatrix multiply(final Object other) {
        final DOMMatrix result = new DOMMatrix();
        final Window window = getWindow();
        result.setParentScope(window);
        result.setPrototype(window.getPrototype(DOMMatrix.class));

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
        result.setIs2D(is2D_ && otherMatrix.is2D_);

        result.setM11(m11_ * otherMatrix.m11_
                + m21_ * otherMatrix.m12_
                + m31_ * otherMatrix.m13_
                + m41_ * otherMatrix.m14_);
        result.setM12(m12_ * otherMatrix.m11_
                + m22_ * otherMatrix.m12_
                + m32_ * otherMatrix.m13_
                + m42_ * otherMatrix.m14_);
        if (!result.isIs2D()) {
            result.setM13(m13_ * otherMatrix.m11_
                    + m23_ * otherMatrix.m12_
                    + m33_ * otherMatrix.m13_
                    + m43_ * otherMatrix.m14_);
            result.setM14(m14_ * otherMatrix.m11_
                    + m24_ * otherMatrix.m12_
                    + m34_ * otherMatrix.m13_
                    + m44_ * otherMatrix.m14_);
        }

        result.setM21(m11_ * otherMatrix.m21_
                + m21_ * otherMatrix.m22_
                + m31_ * otherMatrix.m23_
                + m41_ * otherMatrix.m24_);
        result.setM22(m12_ * otherMatrix.m21_
                + m22_ * otherMatrix.m22_
                + m32_ * otherMatrix.m23_
                + m42_ * otherMatrix.m24_);
        if (!result.isIs2D()) {
            result.setM23(m13_ * otherMatrix.m21_
                    + m23_ * otherMatrix.m22_
                    + m33_ * otherMatrix.m23_
                    + m43_ * otherMatrix.m24_);
            result.setM24(m14_ * otherMatrix.m21_
                    + m24_ * otherMatrix.m22_
                    + m34_ * otherMatrix.m23_
                    + m44_ * otherMatrix.m24_);
        }

        if (!result.isIs2D()) {
            result.setM31(m11_ * otherMatrix.m31_
                    + m21_ * otherMatrix.m32_
                    + m31_ * otherMatrix.m33_
                    + m41_ * otherMatrix.m34_);
            result.setM32(m12_ * otherMatrix.m31_
                    + m22_ * otherMatrix.m32_
                    + m32_ * otherMatrix.m33_
                    + m42_ * otherMatrix.m34_);
            result.setM33(m13_ * otherMatrix.m31_
                    + m23_ * otherMatrix.m32_
                    + m33_ * otherMatrix.m33_
                    + m43_ * otherMatrix.m34_);
            result.setM34(m14_ * otherMatrix.m31_
                    + m24_ * otherMatrix.m32_
                    + m34_ * otherMatrix.m33_
                    + m44_ * otherMatrix.m34_);
        }

        result.setM41(m11_ * otherMatrix.m41_
                + m21_ * otherMatrix.m42_
                + m31_ * otherMatrix.m43_
                + m41_ * otherMatrix.m44_);
        result.setM42(m12_ * otherMatrix.m41_
                + m22_ * otherMatrix.m42_
                + m32_ * otherMatrix.m43_
                + m42_ * otherMatrix.m44_);
        if (!result.isIs2D()) {
            result.setM43(m13_ * otherMatrix.m41_
                    + m23_ * otherMatrix.m42_
                    + m33_ * otherMatrix.m43_
                    + m43_ * otherMatrix.m44_);
            result.setM44(m14_ * otherMatrix.m41_
                    + m24_ * otherMatrix.m42_
                    + m34_ * otherMatrix.m43_
                    + m44_ * otherMatrix.m44_);
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
        final DOMMatrix result = new DOMMatrix();
        final Window window = getWindow();
        result.setParentScope(window);
        result.setPrototype(window.getPrototype(DOMMatrix.class));

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
            result.setM11(m11_ * cos + m21_ * sin);
            result.setM12(m12_ * cos + m22_ * sin);

            result.setM21(m11_ * (-sin) + m21_ * cos);
            result.setM22(m12_ * (-sin) + m22_ * cos);

            result.setM41(m41_);
            result.setM42(m42_);

            result.setIs2D(true);
        }
        else {
            // For 3D matrices, apply rotation to all relevant components
            result.setM11(m11_ * cos + m21_ * sin);
            result.setM12(m12_ * cos + m22_ * sin);
            result.setM13(m13_ * cos + m23_ * sin);
            result.setM14(m14_ * cos + m24_ * sin);

            result.setM21(m11_ * (-sin) + m21_ * cos);
            result.setM22(m12_ * (-sin) + m22_ * cos);
            result.setM23(m13_ * (-sin) + m23_ * cos);
            result.setM24(m14_ * (-sin) + m24_ * cos);

            result.setM31(m31_);
            result.setM32(m32_);
            result.setM33(m33_);
            result.setM34(m34_);

            result.setM41(m41_);
            result.setM42(m42_);
            result.setM43(m43_);
            result.setM44(m44_);

            result.setIs2D(false);
        }

        return result;
    }

    /**
     * Rotates the matrix by a given angle around the specified axis.
     *
     * @param xObj the x component of the axis
     * @param yObj the y component of the axis
     * @param zObj the z component of the axis
     * @param alphaObj the rotation angle in degrees
     * @return a new matrix which is the result of the original matrix rotated by the specified axis and angle.
     */
    @JsxFunction
    public DOMMatrixReadOnly rotateAxisAngle(
                final Object xObj, final Object yObj, final Object zObj, final Object alphaObj) {
        // Default values
        double x = 0;
        double y = 0;
        double z = 1;
        double alpha = 0;
        if (xObj != null && !JavaScriptEngine.isUndefined(xObj)) {
            x = JavaScriptEngine.toNumber(xObj);
        }
        if (yObj != null && !JavaScriptEngine.isUndefined(yObj)) {
            y = JavaScriptEngine.toNumber(yObj);
        }
        if (zObj != null && !JavaScriptEngine.isUndefined(zObj)) {
            z = JavaScriptEngine.toNumber(zObj);
        }
        if (alphaObj != null && !JavaScriptEngine.isUndefined(alphaObj)) {
            alpha = JavaScriptEngine.toNumber(alphaObj);
        }

        // If axis is (0,0,0), throw TypeError per spec
        if (x == 0 && y == 0 && z == 0) {
            final DOMMatrix result = new DOMMatrix();
            final Window window = getWindow();
            result.setParentScope(window);
            result.setPrototype(window.getPrototype(DOMMatrix.class));
            return result;
        }

        // Normalize the axis
        final double length = Math.sqrt(x * x + y * y + z * z);
        x /= length;
        y /= length;
        z /= length;

        // Convert angle to radians
        final double angle2 = Math.toRadians(alpha) / 2;

        // Compute rotation matrix
        final double sc = Math.sin(angle2) * Math.cos(angle2);
        final double sq = Math.pow(Math.sin(angle2), 2);

        final double x2 = x * x;
        final double y2 = y * y;
        final double z2 = z * z;

        final DOMMatrix rot = new DOMMatrix();

        rot.setM11(1 - 2 * (y2 + z2) * sq);
        rot.setM12(2 * (x * y * sq + z * sc));
        rot.setM13(2 * (x * z * sq - y * sc));
        rot.setM14(0);

        rot.setM21(2 * (x * y * sq - z * sc));
        rot.setM22(1 - 2 * (x2 + z2) * sq);
        rot.setM23(2 * (y * z * sq + x * sc));
        rot.setM24(0);

        rot.setM31(2 * (x * z * sq + y * sc));
        rot.setM32(2 * (y * z * sq - x * sc));
        rot.setM33(1 - 2 * (x2 + y2) * sq);
        rot.setM34(0);

        rot.setM41(0);
        rot.setM42(0);
        rot.setM43(0);
        rot.setM44(1);

        rot.setIs2D(false);

        // Multiply this * rot
        final DOMMatrix multiplied = multiply(rot);
        multiplied.setIs2D(is2D_ && x == 0 && y == 0);
        return multiplied;
    }

    /**
     * @param alphaObj the angle, in degrees, by which to skew the matrix along the x-axis
     * @return returns a new DOMMatrix created by applying the specified skew transformation
     *     to the source matrix along its x-axis. The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrixReadOnly skewX(final Object alphaObj) {
        // Default values
        double alpha = 0;
        if (alphaObj != null && !JavaScriptEngine.isUndefined(alphaObj)) {
            alpha = JavaScriptEngine.toNumber(alphaObj);
        }

        // Convert angle to radians
        final double angle = Math.toRadians(alpha);

        // Compute rotation matrix
        final DOMMatrix rot = new DOMMatrix();

        rot.setM21(Math.tan(angle));

        // Multiply this * rot
        final DOMMatrix multiplied = multiply(rot);
        return multiplied;
    }

    /**
     * @param alphaObj the angle, in degrees, by which to skew the matrix along the y-axis
     * @return returns a new DOMMatrix created by applying the specified skew transformation
     *     to the source matrix along its x-axis. The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrixReadOnly skewY(final Object alphaObj) {
        // Default values
        double alpha = 0;
        if (alphaObj != null && !JavaScriptEngine.isUndefined(alphaObj)) {
            alpha = JavaScriptEngine.toNumber(alphaObj);
        }

        // Convert angle to radians
        final double angle = Math.toRadians(alpha);

        // Compute rotation matrix
        final DOMMatrixReadOnly rot = new DOMMatrixReadOnly();

        rot.m12_ = Math.tan(angle);

        // Multiply this * rot
        final DOMMatrixReadOnly multiplied = multiply(rot);
        return multiplied;
    }

    /**
     * Creates a new matrix being the result of the original matrix with a translation applied.
     *
     * @param xObj a number representing the abscissa (x-coordinate) of the translating vector
     * @param yObj a number representing the ordinate (y-coordinate) of the translating vector
     * @param zObj A number representing the z component of the translating vector. If not supplied,
     *        this defaults to 0. If this is anything other than 0, the resulting matrix will be 3D
     * @return a DOMMatrix containing a new matrix being the result of the matrix being translated
     *         by the given vector. The original matrix is not modified.
     *         If a translation is applied about the z-axis, the resulting matrix will be a 4x4 3D matrix.
     */
    @JsxFunction
    public DOMMatrixReadOnly translate(final Object xObj, final Object yObj, final Object zObj) {
        // Default values
        double x = 0;
        double y = 0;
        double z = 0;
        if (xObj != null && !JavaScriptEngine.isUndefined(xObj)) {
            x = JavaScriptEngine.toNumber(xObj);
        }
        if (yObj != null && !JavaScriptEngine.isUndefined(yObj)) {
            y = JavaScriptEngine.toNumber(yObj);
        }
        if (zObj != null && !JavaScriptEngine.isUndefined(zObj)) {
            z = JavaScriptEngine.toNumber(zObj);
        }

        final DOMMatrixReadOnly translate = new DOMMatrixReadOnly();

        translate.m41_ = x;
        translate.m42_ = y;
        translate.m43_ = z;

        translate.is2D_ = false;

        // Multiply this * rot
        final DOMMatrix multiplied = multiply(translate);
        multiplied.setIs2D(is2D_ && (zObj == null || JavaScriptEngine.isUndefined(zObj) || z == 0));
        return multiplied;
    }

    /**
     * @return a new Float32Array containing all 16 elements
     *     (m11, m12, m13, m14, m21, m22, m23, m24, m31, m32, m33, m34, m41, m42, m43, m44)
     *     which comprise the matrix. The elements are stored into the array
     *     as single-precision floating-point numbers in column-major (colexographical access, or "colex") order.
     *     (In other words, down the first column from top to bottom, then the second column, and so forth.)
     */
    @JsxFunction
    public NativeFloat32Array toFloat32Array() {
        final NativeFloat32Array result =
                (NativeFloat32Array) JavaScriptEngine.newObject(getParentScope(), "Float32Array", new Object[] {16});

        result.setArrayElement(0, m11_);
        result.setArrayElement(1, m12_);
        result.setArrayElement(2, m13_);
        result.setArrayElement(3, m14_);

        result.setArrayElement(4, m21_);
        result.setArrayElement(5, m22_);
        result.setArrayElement(6, m23_);
        result.setArrayElement(7, m24_);

        result.setArrayElement(8, m31_);
        result.setArrayElement(9, m32_);
        result.setArrayElement(10, m33_);
        result.setArrayElement(11, m34_);

        result.setArrayElement(12, m41_);
        result.setArrayElement(13, m42_);
        result.setArrayElement(14, m43_);
        result.setArrayElement(15, m44_);

        return result;
    }

    /**
     * @return a new Float64Array containing all 16 elements
     *     (m11, m12, m13, m14, m21, m22, m23, m24, m31, m32, m33, m34, m41, m42, m43, m44)
     *     which comprise the matrix. The elements are stored into the array
     *     as single-precision floating-point numbers in column-major (colexographical access, or "colex") order.
     *     (In other words, down the first column from top to bottom, then the second column, and so forth.)
     */
    @JsxFunction
    public NativeFloat64Array toFloat64Array() {
        final NativeFloat64Array result =
                (NativeFloat64Array) JavaScriptEngine.newObject(getParentScope(), "Float64Array", new Object[] {16});

        result.setArrayElement(0, m11_);
        result.setArrayElement(1, m12_);
        result.setArrayElement(2, m13_);
        result.setArrayElement(3, m14_);

        result.setArrayElement(4, m21_);
        result.setArrayElement(5, m22_);
        result.setArrayElement(6, m23_);
        result.setArrayElement(7, m24_);

        result.setArrayElement(8, m31_);
        result.setArrayElement(9, m32_);
        result.setArrayElement(10, m33_);
        result.setArrayElement(11, m34_);

        result.setArrayElement(12, m41_);
        result.setArrayElement(13, m42_);
        result.setArrayElement(14, m43_);
        result.setArrayElement(15, m44_);

        return result;
    }

    /**
     * @return the values of the list separated by commas,
     *     within matrix() or matrix3d() function syntax.
     */
    @JsxFunction(functionName = "toString")
    public String js_toString() {
        final StringBuilder result = new StringBuilder();

        result.append(is2D_ ? "matrix(" : "matrix3d(");
        appendDouble(result, m11_).append(", ");
        appendDouble(result, m12_).append(", ");
        if (!is2D_) {
            appendDouble(result, m13_).append(", ");
            appendDouble(result, m14_).append(", ");
        }

        appendDouble(result, m21_).append(", ");
        appendDouble(result, m22_).append(", ");
        if (!is2D_) {
            appendDouble(result, m23_).append(", ");
            appendDouble(result, m24_).append(", ");

            appendDouble(result, m31_).append(", ");
            appendDouble(result, m32_).append(", ");
            appendDouble(result, m33_).append(", ");
            appendDouble(result, m34_).append(", ");
        }

        appendDouble(result, m41_).append(", ");
        appendDouble(result, m42_);
        if (!is2D_) {
            result.append(", ");
            appendDouble(result, m43_).append(", ");
            appendDouble(result, m44_);
        }

        result.append(')');

        return result.toString();
    }

    private static StringBuilder appendDouble(final StringBuilder builder, final double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            return builder.append(d);
        }

        if (d % 1 == 0) {
            return builder.append((int) d);
        }

        return builder.append(d);
    }

    /**
     * @return the values of the list separated by commas,
     *     within matrix() or matrix3d() function syntax.
     */
    @JsxFunction
    public Object toJSON() {
        final String jsonString = new StringBuilder()
                .append("{\"a\":").append(m11_)
                .append(", \"b\":").append(m12_)
                .append(", \"c\":").append(m21_)
                .append(", \"d\":").append(m22_)
                .append(", \"e\":").append(m41_)
                .append(", \"f\":").append(m42_)

                .append(", \"m11\":").append(m11_)
                .append(", \"m12\":").append(m12_)
                .append(", \"m13\":").append(m13_)
                .append(", \"m14\":").append(m14_)

                .append(", \"m21\":").append(m21_)
                .append(", \"m22\":").append(m22_)
                .append(", \"m23\":").append(m23_)
                .append(", \"m24\":").append(m24_)

                .append(", \"m31\":").append(m31_)
                .append(", \"m32\":").append(m32_)
                .append(", \"m33\":").append(m33_)
                .append(", \"m34\":").append(m34_)

                .append(", \"m41\":").append(m41_)
                .append(", \"m42\":").append(m42_)
                .append(", \"m43\":").append(m43_)
                .append(", \"m44\":").append(m44_)

                .append(", \"is2D\":").append(is2D_)
                .append(", \"isIdentity\":").append(getIsIdentity())

                .append('}').toString();
        try {
            return new JsonParser(Context.getCurrentContext(), getParentScope()).parseValue(jsonString);
        }
        catch (final ParseException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Failed parsingJSON '" + jsonString + "'", e);
            }
        }
        return null;
    }
}
