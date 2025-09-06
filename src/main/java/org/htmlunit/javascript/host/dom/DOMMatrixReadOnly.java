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

        if (args.length == 0 || JavaScriptEngine.isUndefined(args[0])) {
            setParentScope(window);
            setPrototype(((FunctionObject) ctorObj).getClassPrototype());

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

            return;
        }

        if (args.length > 0) {
            if (args[0] instanceof NativeArray) {
                final NativeArray arrayArgs = (NativeArray) args[0];
                if (arrayArgs.getLength() == 6) {
                    final DOMMatrixReadOnly matrix = new DOMMatrixReadOnly();
                    setParentScope(window);
                    setPrototype(((FunctionObject) ctorObj).getClassPrototype());

                    m11_ = JavaScriptEngine.toNumber(arrayArgs.get(0));
                    m12_ = JavaScriptEngine.toNumber(arrayArgs.get(1));
                    m13_ = 0;
                    m14_ = 0;

                    m21_ = JavaScriptEngine.toNumber(arrayArgs.get(2));
                    m22_ = JavaScriptEngine.toNumber(arrayArgs.get(3));
                    m23_ = 0;
                    m24_ = 0;

                    m31_ = 0;
                    m32_ = 0;
                    m33_ = 1;
                    m34_ = 0;

                    m41_ = JavaScriptEngine.toNumber(arrayArgs.get(4));
                    m42_ = JavaScriptEngine.toNumber(arrayArgs.get(5));
                    m43_ = 0;
                    m44_ = 1;

                    is2D_ = true;

                    return;
                }

                if (arrayArgs.getLength() == 16) {
                    final DOMMatrixReadOnly matrix = new DOMMatrixReadOnly();
                    setParentScope(window);
                    setPrototype(((FunctionObject) ctorObj).getClassPrototype());

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

        matrix.m11_ = -this.m11_;
        matrix.m12_ = -this.m12_;
        matrix.m13_ = -this.m13_;
        matrix.m14_ = -this.m14_;

        matrix.m21_ = this.m21_;
        matrix.m22_ = this.m22_;
        matrix.m23_ = this.m23_;
        matrix.m24_ = this.m24_;

        matrix.m31_ = this.m31_;
        matrix.m32_ = this.m32_;
        matrix.m33_ = this.m33_;
        matrix.m34_ = this.m34_;

        matrix.m41_ = this.m41_;
        matrix.m42_ = this.m42_;
        matrix.m43_ = this.m43_;
        matrix.m44_ = this.m44_;

        matrix.is2D_ = this.is2D_;
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

        matrix.m11_ = this.m11_;
        matrix.m12_ = this.m12_;
        matrix.m13_ = this.m13_;
        matrix.m14_ = this.m14_;

        matrix.m21_ = -this.m21_;
        matrix.m22_ = -this.m22_;
        matrix.m23_ = -this.m23_;
        matrix.m24_ = -this.m24_;

        matrix.m31_ = this.m31_;
        matrix.m32_ = this.m32_;
        matrix.m33_ = this.m33_;
        matrix.m34_ = this.m34_;

        matrix.m41_ = this.m41_;
        matrix.m42_ = this.m42_;
        matrix.m43_ = this.m43_;
        matrix.m44_ = this.m44_;

        matrix.is2D_ = this.is2D_;
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
                return matrix;
            }

            matrix.m11_ = m22_ / det;
            matrix.m12_ = -m12_ / det;
            matrix.m13_ = 0;
            matrix.m14_ = 0;

            matrix.m21_ = -m21_ / det;
            matrix.m22_ = m11_ / det;
            matrix.m23_ = 0;
            matrix.m24_ = 0;

            matrix.m31_ = 0;
            matrix.m32_ = 0;
            matrix.m33_ = 1;
            matrix.m34_ = 0;

            matrix.m41_ = (m21_ * m42_ - m22_ * m41_) / det;
            matrix.m42_ = (m12_ * m41_ - m11_ * m42_) / det;
            matrix.m43_ = 0;
            matrix.m44_ = 1;

            matrix.is2D_ = true;
            return matrix;
        }

        // 3D/4x4 matrix inversion
        final double[] m = {
            m11_, m12_, m13_, m14_,
            m21_, m22_, m23_, m24_,
            m31_, m32_, m33_, m34_,
            m41_, m42_, m43_, m44_
        };

        final double[] inv = new double[16];
        inv[0] = m[5] * m[10] * m[15]
                - m[5] * m[11] * m[14]
                - m[9] * m[6] * m[15]
                + m[9] * m[7] * m[14]
                + m[13] * m[6] * m[11]
                - m[13] * m[7] * m[10];

        inv[4] = -m[4] * m[10] * m[15]
                + m[4] * m[11] * m[14]
                + m[8] * m[6] * m[15]
                - m[8] * m[7] * m[14]
                - m[12] * m[6] * m[11]
                + m[12] * m[7] * m[10];

        inv[8] = m[4] * m[9] * m[15]
                - m[4] * m[11] * m[13]
                - m[8] * m[5] * m[15]
                + m[8] * m[7] * m[13]
                + m[12] * m[5] * m[11]
                - m[12] * m[7] * m[9];

        inv[12] = -m[4] * m[9] * m[14]
                + m[4] * m[10] * m[13]
                + m[8] * m[5] * m[14]
                - m[8] * m[6] * m[13]
                - m[12] * m[5] * m[10]
                + m[12] * m[6] * m[9];

        inv[1] = -m[1] * m[10] * m[15]
                + m[1] * m[11] * m[14]
                + m[9] * m[2] * m[15]
                - m[9] * m[3] * m[14]
                - m[13] * m[2] * m[11]
                + m[13] * m[3] * m[10];

        inv[5] = m[0] * m[10] * m[15]
                - m[0] * m[11] * m[14]
                - m[8] * m[2] * m[15]
                + m[8] * m[3] * m[14]
                + m[12] * m[2] * m[11]
                - m[12] * m[3] * m[10];

        inv[9] = -m[0] * m[9] * m[15]
                + m[0] * m[11] * m[13]
                + m[8] * m[1] * m[15]
                - m[8] * m[3] * m[13]
                - m[12] * m[1] * m[11]
                + m[12] * m[3] * m[9];

        inv[13] = m[0] * m[9] * m[14]
                - m[0] * m[10] * m[13]
                - m[8] * m[1] * m[14]
                + m[8] * m[2] * m[13]
                + m[12] * m[1] * m[10]
                - m[12] * m[2] * m[9];

        inv[2] = m[1] * m[6] * m[15]
                - m[1] * m[7] * m[14]
                - m[5] * m[2] * m[15]
                + m[5] * m[3] * m[14]
                + m[13] * m[2] * m[7]
                - m[13] * m[3] * m[6];

        inv[6] = -m[0] * m[6] * m[15]
                + m[0] * m[7] * m[14]
                + m[4] * m[2] * m[15]
                - m[4] * m[3] * m[14]
                - m[12] * m[2] * m[7]
                + m[12] * m[3] * m[6];

        inv[10] = m[0] * m[5] * m[15]
                - m[0] * m[7] * m[13]
                - m[4] * m[1] * m[15]
                + m[4] * m[3] * m[13]
                + m[12] * m[1] * m[7]
                - m[12] * m[3] * m[5];

        inv[14] = -m[0] * m[5] * m[14]
                + m[0] * m[6] * m[13]
                + m[4] * m[1] * m[14]
                - m[4] * m[2] * m[13]
                - m[12] * m[1] * m[6]
                + m[12] * m[2] * m[5];

        inv[3] = -m[1] * m[6] * m[11]
                + m[1] * m[7] * m[10]
                + m[5] * m[2] * m[11]
                - m[5] * m[3] * m[10]
                - m[9] * m[2] * m[7]
                + m[9] * m[3] * m[6];

        inv[7] = m[0] * m[6] * m[11]
                - m[0] * m[7] * m[10]
                - m[4] * m[2] * m[11]
                + m[4] * m[3] * m[10]
                + m[8] * m[2] * m[7]
                - m[8] * m[3] * m[6];

        inv[11] = -m[0] * m[5] * m[11]
                + m[0] * m[7] * m[9]
                + m[4] * m[1] * m[11]
                - m[4] * m[3] * m[9]
                - m[8] * m[1] * m[7]
                + m[8] * m[3] * m[5];

        inv[15] = m[0] * m[5] * m[10]
                - m[0] * m[6] * m[9]
                - m[4] * m[1] * m[10]
                + m[4] * m[2] * m[9]
                + m[8] * m[1] * m[6]
                - m[8] * m[2] * m[5];

        double det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];

        if (det == 0) {
            // Not invertible: set all to NaN, is2D_ = false
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

        return matrix;
    }
}
