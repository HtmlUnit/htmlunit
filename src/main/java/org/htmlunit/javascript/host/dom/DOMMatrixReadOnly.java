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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.SymbolKey;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.corejs.javascript.typedarrays.NativeFloat32Array;
import org.htmlunit.corejs.javascript.typedarrays.NativeFloat64Array;
import org.htmlunit.corejs.javascript.typedarrays.NativeTypedArrayView;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxStaticFunction;
import org.htmlunit.javascript.host.Window;

/**
 * A JavaScript object for {@code DOMMatrixReadOnly}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/DOMMatrixReadOnly">MDN Documentation</a>
 */
@JsxClass
public class DOMMatrixReadOnly extends HtmlUnitScriptable {

    // private static final Log LOG = LogFactory.getLog(DOMMatrixReadOnly.class);

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
     * @param args the arguments to the DOMMatrixReadOnly constructor
     * @param ctorObj the function object
     * @param inNewExpr {@code true} if invoked with the {@code new} operator
     * @return the Java object that JavaScript can access
     */
    @JsxConstructor
    public static DOMMatrixReadOnly jsConstructor(final Context cx, final VarScope scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {

        final DOMMatrixReadOnly matrix = new DOMMatrixReadOnly();
        matrix.init(args, scope, ctorObj);
        return matrix;
    }

    /**
     * Initializes this matrix from the constructor arguments.
     * <ul>
     * <li>no argument or {@code undefined}: the identity matrix</li>
     * <li>an iterable object (e.g. an array or a typed array): a sequence of 6 (2D)
     * or 16 (3D) numbers</li>
     * <li>everything else is converted to a string and parsed as CSS transform list;
     * this is only supported in a window, workers throw a TypeError</li>
     * </ul>
     *
     * @param args the constructor arguments
     * @param scope the scope
     * @param ctorObj the constructor function
     */
    protected void init(final Object[] args, final VarScope scope, final Function ctorObj) {
        setParentScope(scope);
        setPrototype(((FunctionObject) ctorObj).getClassPrototype());

        if (args.length == 0 || JavaScriptEngine.isUndefined(args[0])) {
            return;
        }

        final Object init = args[0];
        if (init instanceof Scriptable initScriptable
                && ScriptableObject.hasProperty(initScriptable, SymbolKey.ITERATOR)) {
            final List<Double> values = new ArrayList<>();
            JavaScriptEngine.iterate(Context.getCurrentContext(), scope, initScriptable,
                    value -> values.add(JavaScriptEngine.toNumber(value)));

            if (values.size() == 6) {
                setValues2D(values.get(0), values.get(1), values.get(2),
                        values.get(3), values.get(4), values.get(5));
                return;
            }
            if (values.size() == 16) {
                final double[] matrix = new double[16];
                for (int i = 0; i < matrix.length; i++) {
                    matrix[i] = values.get(i);
                }
                setValues(matrix, false);
                return;
            }

            throw JavaScriptEngine.typeError(getClassName() + " constructor: Matrix init sequence must have "
                    + "a length of 6 or 16 (actual value: " + values.size() + ")");
        }

        // (DOMString or sequence<unrestricted double>): everything else is a string
        if (!(getWindowOrWorkerGlobalScope(this) instanceof Window)) {
            throw JavaScriptEngine.typeError(getClassName()
                    + " constructor: a transform list can only be parsed in a window context");
        }
        setTransformList(JavaScriptEngine.toString(init));
    }

    /**
     * Parses the given CSS transform list and replaces the values of this matrix
     * with the result.
     *
     * @param transformList the CSS transform list
     */
    protected void setTransformList(final String transformList) {
        try {
            DOMMatrixTransformListParser.parse(transformList, this);
        }
        catch (final IllegalArgumentException e) {
            throw JavaScriptEngine.asJavaScriptException(this,
                    "Failed to parse '" + transformList + "': " + e.getMessage(),
                    DOMException.SYNTAX_ERR);
        }
    }

    /**
     * Returns the {@code m11} property.
     * @return the {@code m11} property
     */
    @JsxGetter
    public double getM11() {
        return m11_;
    }

    /**
     * Sets the {@code m11} property.
     * @param m11 the new value
     */
    public void setM11(final double m11) {
        m11_ = m11;
    }

    /**
     * Returns the {@code a} property.
     * @return the {@code a} property
     */
    @JsxGetter
    public double getA() {
        return m11_;
    }

    /**
     * Returns the {@code m12} property.
     * @return the {@code m12} property
     */
    @JsxGetter
    public double getM12() {
        return m12_;
    }

    /**
     * Sets the {@code m12} property.
     * @param m12 the new value
     */
    public void setM12(final double m12) {
        m12_ = m12;
    }

    /**
     * Returns the {@code b} property.
     * @return the {@code b} property
     */
    @JsxGetter
    public double getB() {
        return m12_;
    }

    /**
     * Returns the {@code m13} property.
     * @return the {@code m13} property
     */
    @JsxGetter
    public double getM13() {
        return m13_;
    }

    /**
     * Sets the {@code m13} property.
     * @param m13 the new value
     */
    public void setM13(final double m13) {
        m13_ = m13;
    }

    /**
     * Returns the {@code m14} property.
     * @return the {@code m14} property
     */
    @JsxGetter
    public double getM14() {
        return m14_;
    }

    /**
     * Sets the {@code m14} property.
     * @param m14 the new value
     */
    public void setM14(final double m14) {
        m14_ = m14;
    }

    /**
     * Returns the {@code m21} property.
     * @return the {@code m21} property
     */
    @JsxGetter
    public double getM21() {
        return m21_;
    }

    /**
     * Sets the {@code m21} property.
     * @param m21 the new value
     */
    public void setM21(final double m21) {
        m21_ = m21;
    }

    /**
     * Returns the {@code c} property.
     * @return the {@code c} property
     */
    @JsxGetter
    public double getC() {
        return m21_;
    }

    /**
     * Returns the {@code m22} property.
     * @return the {@code m22} property
     */
    @JsxGetter
    public double getM22() {
        return m22_;
    }

    /**
     * Sets the {@code m22} property.
     * @param m22 the new value
     */
    public void setM22(final double m22) {
        m22_ = m22;
    }

    /**
     * Returns the {@code d} property.
     * @return the {@code d} property
     */
    @JsxGetter
    public double getD() {
        return m22_;
    }

    /**
     * Returns the {@code m23} property.
     * @return the {@code m23} property
     */
    @JsxGetter
    public double getM23() {
        return m23_;
    }

    /**
     * Sets the {@code m23} property.
     * @param m23 the new value
     */
    public void setM23(final double m23) {
        m23_ = m23;
    }

    /**
     * Returns the {@code m24} property.
     * @return the {@code m24} property
     */
    @JsxGetter
    public double getM24() {
        return m24_;
    }

    /**
     * Sets the {@code m24} property.
     * @param m24 the new value
     */
    public void setM24(final double m24) {
        m24_ = m24;
    }

    /**
     * Returns the {@code m31} property.
     * @return the {@code m31} property
     */
    @JsxGetter
    public double getM31() {
        return m31_;
    }

    /**
     * Sets the {@code m31} property.
     * @param m31 the new value
     */
    public void setM31(final double m31) {
        m31_ = m31;
    }

    /**
     * Returns the {@code m32} property.
     * @return the {@code m32} property
     */
    @JsxGetter
    public double getM32() {
        return m32_;
    }

    /**
     * Sets the {@code m32} property.
     * @param m32 the new value
     */
    public void setM32(final double m32) {
        m32_ = m32;
    }

    /**
     * Returns the {@code m33} property.
     * @return the {@code m33} property
     */
    @JsxGetter
    public double getM33() {
        return m33_;
    }

    /**
     * Sets the {@code m33} property.
     * @param m33 the new value
     */
    public void setM33(final double m33) {
        m33_ = m33;
    }

    /**
     * Returns the {@code m34} property.
     * @return the {@code m34} property
     */
    @JsxGetter
    public double getM34() {
        return m34_;
    }

    /**
     * Sets the {@code m34} property.
     * @param m34 the new value
     */
    public void setM34(final double m34) {
        m34_ = m34;
    }

    /**
     * Returns the {@code m41} property.
     * @return the {@code m41} property
     */
    @JsxGetter
    public double getM41() {
        return m41_;
    }

    /**
     * Sets the {@code m41} property.
     * @param m41 the new value
     */
    public void setM41(final double m41) {
        m41_ = m41;
    }

    /**
     * Returns the {@code e} property.
     * @return the {@code e} property
     */
    @JsxGetter
    public double getE() {
        return m41_;
    }

    /**
     * Returns the {@code m42} property.
     * @return the {@code m42} property
     */
    @JsxGetter
    public double getM42() {
        return m42_;
    }

    /**
     * Sets the {@code m42} property.
     * @param m42 the new value
     */
    public void setM42(final double m42) {
        m42_ = m42;
    }

    /**
     * Returns the {@code f} property.
     * @return the {@code f} property
     */
    @JsxGetter
    public double getF() {
        return m42_;
    }

    /**
     * Returns the {@code m43} property.
     * @return the {@code m43} property
     */
    @JsxGetter
    public double getM43() {
        return m43_;
    }

    /**
     * Sets the {@code m43} property.
     * @param m43 the new value
     */
    public void setM43(final double m43) {
        m43_ = m43;
    }

    /**
     * Returns the {@code m44} property.
     * @return the {@code m44} property
     */
    @JsxGetter
    public double getM44() {
        return m44_;
    }

    /**
     * Sets the {@code m44} property.
     * @param m44 the new value
     */
    public void setM44(final double m44) {
        m44_ = m44;
    }

    /**
     * Returns the {@code is2D} property.
     * @return the {@code is2D} property
     */
    @JsxGetter
    public boolean isIs2D() {
        return is2D_;
    }

    /**
     * Sets the {@code is2D} property.
     * @param is2D the new value
     */
    public void setIs2D(final boolean is2D) {
        is2D_ = is2D;
    }

    /**
     * Returns whether this matrix is the identity matrix.
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
     * Returns a new matrix being the result of the original matrix flipped about the x-axis.
     * @return a new matrix being the result of the original matrix flipped about the x-axis.
     *     This is equivalent to multiplying the matrix by DOMMatrix(-1, 0, 0, 1, 0, 0).
     *     The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix flipX() {
        final double[] values = getValues();
        for (int i = 0; i < 4; i++) {
            values[i] = -values[i];
        }

        final DOMMatrix matrix = copyToDOMMatrix();
        matrix.setValues(values, is2D_);
        return matrix;
    }

    /**
     * Returns a new matrix being the result of the original matrix flipped about the y-axis.
     * @return a new matrix being the result of the original matrix flipped about the y-axis.
     *     This is equivalent to multiplying the matrix by DOMMatrix(1, 0, 0, -1, 0, 0).
     *     The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix flipY() {
        final double[] values = getValues();
        for (int i = 4; i < 8; i++) {
            values[i] = -values[i];
        }

        final DOMMatrix matrix = copyToDOMMatrix();
        matrix.setValues(values, is2D_);
        return matrix;
    }

    /**
     * Returns a new matrix which is the inverse of the original matrix.
     * @return new matrix which is the inverse of the original matrix.
     *     If the matrix cannot be inverted, the new matrix's components are all set to NaN
     *     and its is2D property is set to false. The original matrix is not changed.
     */
    @JsxFunction
    public DOMMatrix inverse() {
        final DOMMatrix matrix = copyToDOMMatrix();
        matrix.invertSelfInternal();
        return matrix;
    }

    /**
     * Returns a new matrix which is the dot product of the matrix and the given matrix.
     * @param other the matrix to multiply with this matrix; this can be a matrix or an
     *     object with matrix properties (DOMMatrixInit); {@code null} and {@code undefined}
     *     are treated as identity matrix
     * @return a new matrix which is the dot product of the matrix and the other matrix.
     *     The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix multiply(final Object other) {
        final DOMMatrixReadOnly otherMatrix = fromMatrixInitForMultiply(other, "multiply", getClassName());

        final DOMMatrix matrix = copyToDOMMatrix();
        matrix.multiplySelfInternal(otherMatrix);
        return matrix;
    }

    /**
     * Returns a new matrix rotated by the specified angles.
     * If only one argument is given, this is the rotation angle around the z-axis
     * (a 2D rotation), otherwise the matrix is rotated around the z-axis, then around
     * the y-axis and finally around the x-axis.
     *
     * @param rotX the rotation angle around the x-axis in degrees (or the rotation angle
     *     around the z-axis if this is the only argument)
     * @param rotY the rotation angle around the y-axis in degrees
     * @param rotZ the rotation angle around the z-axis in degrees
     * @return a new matrix which is the result of the original matrix rotated
     *     by the specified angles. The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix rotate(final Object rotX, final Object rotY, final Object rotZ) {
        final DOMMatrix matrix = copyToDOMMatrix();
        matrix.rotateSelfInternal(rotX, rotY, rotZ);
        return matrix;
    }

    /**
     * Returns a new matrix rotated by the angle between the specified vector and (1, 0).
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @return a new matrix which is the result of the original matrix rotated
     *     by the angle of the vector. The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix rotateFromVector(final Object x, final Object y) {
        final DOMMatrix matrix = copyToDOMMatrix();
        matrix.rotateFromVectorSelfInternal(toDouble(x, 0), toDouble(y, 0));
        return matrix;
    }

    /**
     * Returns a new matrix rotated by a given angle around the specified axis.
     *
     * @param x the x component of the axis
     * @param y the y component of the axis
     * @param z the z component of the axis
     * @param angle the rotation angle in degrees
     * @return a new matrix which is the result of the original matrix rotated
     *     around the axis. The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix rotateAxisAngle(final Object x, final Object y, final Object z, final Object angle) {
        final DOMMatrix matrix = copyToDOMMatrix();
        matrix.rotateAxisAngleSelfInternal(toDouble(x, 0), toDouble(y, 0), toDouble(z, 0), toDouble(angle, 0));
        return matrix;
    }

    /**
     * Returns a new matrix scaled by the given factors, centered on the given origin.
     *
     * @param scaleX the scale factor for the x-axis (default 1)
     * @param scaleY the scale factor for the y-axis (default scaleX)
     * @param scaleZ the scale factor for the z-axis (default 1)
     * @param originX the x-coordinate of the origin (default 0)
     * @param originY the y-coordinate of the origin (default 0)
     * @param originZ the z-coordinate of the origin (default 0)
     * @return a new matrix which is the result of the original matrix scaled.
     *     The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix scale(final Object scaleX, final Object scaleY, final Object scaleZ,
                final Object originX, final Object originY, final Object originZ) {
        final DOMMatrix matrix = copyToDOMMatrix();
        final double sx = toDouble(scaleX, 1);
        matrix.scaleSelfInternal(sx, toDouble(scaleY, sx), toDouble(scaleZ, 1),
                toDouble(originX, 0), toDouble(originY, 0), toDouble(originZ, 0));
        return matrix;
    }

    /**
     * Returns a new matrix scaled by the given factor for all three axes,
     * centered on the given origin.
     *
     * @param scale the scale factor (default 1)
     * @param originX the x-coordinate of the origin (default 0)
     * @param originY the y-coordinate of the origin (default 0)
     * @param originZ the z-coordinate of the origin (default 0)
     * @return a new matrix which is the result of the original matrix scaled.
     *     The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix scale3d(final Object scale, final Object originX, final Object originY, final Object originZ) {
        final DOMMatrix matrix = copyToDOMMatrix();
        matrix.scale3dSelfInternal(toDouble(scale, 1),
                toDouble(originX, 0), toDouble(originY, 0), toDouble(originZ, 0));
        return matrix;
    }

    /**
     * Returns a new matrix scaled by the given factors for the x- and the y-axis.
     * This method is deprecated; use {@link #scale(Object, Object, Object, Object, Object, Object)}.
     *
     * @param scaleX the scale factor for the x-axis (default 1)
     * @param scaleY the scale factor for the y-axis (default 1)
     * @return a new matrix which is the result of the original matrix scaled.
     *     The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix scaleNonUniform(final Object scaleX, final Object scaleY) {
        final DOMMatrix matrix = copyToDOMMatrix();
        matrix.scaleSelfInternal(toDouble(scaleX, 1), toDouble(scaleY, 1), 1, 0, 0, 0);
        return matrix;
    }

    /**
     * Returns a new matrix skewed along the x-axis by the specified angle.
     * @param sx the angle, in degrees, by which to skew the matrix along the x-axis
     * @return returns a new DOMMatrix created by applying the specified skew transformation
     *     to the source matrix along its x-axis. The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix skewX(final Object sx) {
        final DOMMatrix matrix = copyToDOMMatrix();
        matrix.skewXSelfInternal(toDouble(sx, 0));
        return matrix;
    }

    /**
     * Returns a new matrix skewed along the y-axis by the specified angle.
     * @param sy the angle, in degrees, by which to skew the matrix along the y-axis
     * @return returns a new DOMMatrix created by applying the specified skew transformation
     *     to the source matrix along its y-axis. The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix skewY(final Object sy) {
        final DOMMatrix matrix = copyToDOMMatrix();
        matrix.skewYSelfInternal(toDouble(sy, 0));
        return matrix;
    }

    /**
     * Creates a new matrix being the result of the original matrix with a translation applied.
     *
     * @param tx the x component of the translating vector (default 0)
     * @param ty the y component of the translating vector (default 0)
     * @param tz the z component of the translating vector (default 0); if this is anything other
     *     than 0, the resulting matrix will be 3D
     * @return a new matrix being the result of the matrix being translated
     *         by the given vector. The original matrix is not modified.
     */
    @JsxFunction
    public DOMMatrix translate(final Object tx, final Object ty, final Object tz) {
        final DOMMatrix matrix = copyToDOMMatrix();
        matrix.translateSelfInternal(toDouble(tx, 0), toDouble(ty, 0), toDouble(tz, 0));
        return matrix;
    }

    /**
     * Transforms the given point with this matrix.
     *
     * @param point the point to transform; this can be a point or an object with point
     *     properties (DOMPointInit: x, y, z (default 0) and w (default 1));
     *     {@code null} and {@code undefined} are treated as the point (0, 0, 0, 1)
     * @return a new {@link DOMPoint} with the transformed coordinates
     */
    @JsxFunction
    public DOMPoint transformPoint(final Object point) {
        double x = 0;
        double y = 0;
        double z = 0;
        double w = 1;
        if (point != null && !JavaScriptEngine.isUndefined(point)) {
            if (!(point instanceof Scriptable pointScriptable)) {
                throw JavaScriptEngine.typeError("Failed to execute 'transformPoint' on '" + getClassName()
                        + "': The provided value is not of type 'DOMPointInit'.");
            }
            x = getDictionaryValue(pointScriptable, "x", 0);
            y = getDictionaryValue(pointScriptable, "y", 0);
            z = getDictionaryValue(pointScriptable, "z", 0);
            w = getDictionaryValue(pointScriptable, "w", 1);
        }

        final DOMPoint result = new DOMPoint(
                mul(m11_, x) + mul(m21_, y) + mul(m31_, z) + mul(m41_, w),
                mul(m12_, x) + mul(m22_, y) + mul(m32_, z) + mul(m42_, w),
                mul(m13_, x) + mul(m23_, y) + mul(m33_, z) + mul(m43_, w),
                mul(m14_, x) + mul(m24_, y) + mul(m34_, z) + mul(m44_, w));
        result.setParentScope(getParentScope());
        result.setPrototype(ScriptableObject.getClassPrototype(getParentScope(), "DOMPoint"));
        return result;
    }

    // ---------------------------------------------------------------------------------
    // the operations; all of them are modifying this matrix (the *Self functions
    // of DOMMatrix); the functions above are working on a copy

    /**
     * Post-multiplies this matrix with the given one (this = this * other).
     * The result is 2D if both matrices are 2D.
     *
     * @param other the matrix to multiply with
     */
    protected void multiplySelfInternal(final DOMMatrixReadOnly other) {
        final boolean is2D = is2D_ && other.is2D_;
        setValues(multiplyMatrices(getValues(), other.getValues(), is2D), is2D);
    }

    /**
     * Pre-multiplies this matrix with the given one (this = other * this).
     * The result is 2D if both matrices are 2D.
     *
     * @param other the matrix to multiply with
     */
    protected void preMultiplySelfInternal(final DOMMatrixReadOnly other) {
        final boolean is2D = is2D_ && other.is2D_;
        setValues(multiplyMatrices(other.getValues(), getValues(), is2D), is2D);
    }

    /**
     * Applies a translation to this matrix.
     *
     * @param tx the x component of the translating vector
     * @param ty the y component of the translating vector
     * @param tz the z component of the translating vector
     */
    protected void translateSelfInternal(final double tx, final double ty, final double tz) {
        final DOMMatrixReadOnly translation = new DOMMatrixReadOnly();
        translation.m41_ = tx;
        translation.m42_ = ty;
        translation.m43_ = tz;
        translation.is2D_ = tz == 0;

        multiplySelfInternal(translation);
    }

    /**
     * Applies a scaling centered on the given origin to this matrix.
     *
     * @param scaleX the scale factor for the x-axis
     * @param scaleY the scale factor for the y-axis
     * @param scaleZ the scale factor for the z-axis
     * @param originX the x-coordinate of the origin
     * @param originY the y-coordinate of the origin
     * @param originZ the z-coordinate of the origin
     */
    protected void scaleSelfInternal(final double scaleX, final double scaleY, final double scaleZ,
                final double originX, final double originY, final double originZ) {
        translateSelfInternal(originX, originY, originZ);

        final DOMMatrixReadOnly scale = new DOMMatrixReadOnly();
        scale.m11_ = scaleX;
        scale.m22_ = scaleY;
        scale.m33_ = scaleZ;
        scale.is2D_ = scaleZ == 1;
        multiplySelfInternal(scale);

        translateSelfInternal(-originX, -originY, -originZ);

        if (scaleZ != 1 || originZ != 0) {
            is2D_ = false;
        }
    }

    /**
     * Applies a uniform scaling of all three axes centered on the given origin to this matrix.
     *
     * @param scale the scale factor
     * @param originX the x-coordinate of the origin
     * @param originY the y-coordinate of the origin
     * @param originZ the z-coordinate of the origin
     */
    protected void scale3dSelfInternal(final double scale,
                final double originX, final double originY, final double originZ) {
        scaleSelfInternal(scale, scale, scale, originX, originY, originZ);
        if (scale != 1) {
            is2D_ = false;
        }
    }

    /**
     * Applies the rotation of {@link #rotate(Object, Object, Object)} to this matrix.
     *
     * @param rotXObj the rotation angle around the x-axis in degrees (or the rotation angle
     *     around the z-axis if this is the only argument)
     * @param rotYObj the rotation angle around the y-axis in degrees
     * @param rotZObj the rotation angle around the z-axis in degrees
     */
    protected void rotateSelfInternal(final Object rotXObj, final Object rotYObj, final Object rotZObj) {
        double rotX = toDouble(rotXObj, 0);
        double rotY = toDouble(rotYObj, 0);
        double rotZ = toDouble(rotZObj, 0);

        if (isMissing(rotYObj) && isMissing(rotZObj)) {
            rotZ = rotX;
            rotX = 0;
            rotY = 0;
        }

        rotateSelfInternal(rotX, rotY, rotZ);
    }

    /**
     * Rotates this matrix around the z-axis, then around the y-axis and
     * finally around the x-axis.
     *
     * @param rotX the rotation angle around the x-axis in degrees
     * @param rotY the rotation angle around the y-axis in degrees
     * @param rotZ the rotation angle around the z-axis in degrees
     */
    protected void rotateSelfInternal(final double rotX, final double rotY, final double rotZ) {
        final boolean is2D = is2D_ && rotX == 0 && rotY == 0;

        multiplySelfInternal(createRotation(0, 0, 1, rotZ));
        if (rotY != 0) {
            multiplySelfInternal(createRotation(0, 1, 0, rotY));
        }
        if (rotX != 0) {
            multiplySelfInternal(createRotation(1, 0, 0, rotX));
        }
        is2D_ = is2D;
    }

    /**
     * Rotates this matrix by the angle between the specified vector and (1, 0).
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     */
    protected void rotateFromVectorSelfInternal(final double x, final double y) {
        double angle = 0;
        if (x != 0 || y != 0) {
            angle = Math.toDegrees(Math.atan2(y, x));
        }
        multiplySelfInternal(createRotation(0, 0, 1, angle));
    }

    /**
     * Rotates this matrix by the given angle around the specified axis.
     * An axis of (0, 0, 0) does not change the matrix.
     *
     * @param x the x component of the axis
     * @param y the y component of the axis
     * @param z the z component of the axis
     * @param angle the rotation angle in degrees
     */
    protected void rotateAxisAngleSelfInternal(final double x, final double y, final double z, final double angle) {
        final boolean is2D = is2D_ && x == 0 && y == 0;
        if (x != 0 || y != 0 || z != 0) {
            multiplySelfInternal(createRotation(x, y, z, angle));
        }
        is2D_ = is2D;
    }

    /**
     * Skews this matrix along the x-axis.
     *
     * @param sx the angle in degrees
     */
    protected void skewXSelfInternal(final double sx) {
        final DOMMatrixReadOnly skew = new DOMMatrixReadOnly();
        skew.m21_ = Math.tan(Math.toRadians(sx));
        multiplySelfInternal(skew);
    }

    /**
     * Skews this matrix along the y-axis.
     *
     * @param sy the angle in degrees
     */
    protected void skewYSelfInternal(final double sy) {
        final DOMMatrixReadOnly skew = new DOMMatrixReadOnly();
        skew.m12_ = Math.tan(Math.toRadians(sy));
        multiplySelfInternal(skew);
    }

    /**
     * Inverts this matrix. If the matrix cannot be inverted, all values are set
     * to NaN and the matrix is no longer 2D.
     */
    protected void invertSelfInternal() {
        if (is2D_) {
            final double det = m11_ * m22_ - m12_ * m21_;
            if (det == 0) {
                setAllNaN();
                return;
            }

            final double a = m22_ / det;
            final double b = -m12_ / det;
            final double c = -m21_ / det;
            final double d = m11_ / det;
            final double e = (m21_ * m42_ - m22_ * m41_) / det;
            final double f = (m12_ * m41_ - m11_ * m42_) / det;
            setValues2D(a, b, c, d, e, f);
            return;
        }

        final double[] m = getValues();
        final double[] inv = new double[16];
        inv[0] = m[5] * m[10] * m[15] - m[5] * m[11] * m[14] - m[9] * m[6] * m[15]
                + m[9] * m[7] * m[14] + m[13] * m[6] * m[11] - m[13] * m[7] * m[10];
        inv[4] = -m[4] * m[10] * m[15] + m[4] * m[11] * m[14] + m[8] * m[6] * m[15]
                - m[8] * m[7] * m[14] - m[12] * m[6] * m[11] + m[12] * m[7] * m[10];
        inv[8] = m[4] * m[9] * m[15] - m[4] * m[11] * m[13] - m[8] * m[5] * m[15]
                + m[8] * m[7] * m[13] + m[12] * m[5] * m[11] - m[12] * m[7] * m[9];
        inv[12] = -m[4] * m[9] * m[14] + m[4] * m[10] * m[13] + m[8] * m[5] * m[14]
                - m[8] * m[6] * m[13] - m[12] * m[5] * m[10] + m[12] * m[6] * m[9];
        inv[1] = -m[1] * m[10] * m[15] + m[1] * m[11] * m[14] + m[9] * m[2] * m[15]
                - m[9] * m[3] * m[14] - m[13] * m[2] * m[11] + m[13] * m[3] * m[10];
        inv[5] = m[0] * m[10] * m[15] - m[0] * m[11] * m[14] - m[8] * m[2] * m[15]
                + m[8] * m[3] * m[14] + m[12] * m[2] * m[11] - m[12] * m[3] * m[10];
        inv[9] = -m[0] * m[9] * m[15] + m[0] * m[11] * m[13] + m[8] * m[1] * m[15]
                - m[8] * m[3] * m[13] - m[12] * m[1] * m[11] + m[12] * m[3] * m[9];
        inv[13] = m[0] * m[9] * m[14] - m[0] * m[10] * m[13] - m[8] * m[1] * m[14]
                + m[8] * m[2] * m[13] + m[12] * m[1] * m[10] - m[12] * m[2] * m[9];
        inv[2] = m[1] * m[6] * m[15] - m[1] * m[7] * m[14] - m[5] * m[2] * m[15]
                + m[5] * m[3] * m[14] + m[13] * m[2] * m[7] - m[13] * m[3] * m[6];
        inv[6] = -m[0] * m[6] * m[15] + m[0] * m[7] * m[14] + m[4] * m[2] * m[15]
                - m[4] * m[3] * m[14] - m[12] * m[2] * m[7] + m[12] * m[3] * m[6];
        inv[10] = m[0] * m[5] * m[15] - m[0] * m[7] * m[13] - m[4] * m[1] * m[15]
                + m[4] * m[3] * m[13] + m[12] * m[1] * m[7] - m[12] * m[3] * m[5];
        inv[14] = -m[0] * m[5] * m[14] + m[0] * m[6] * m[13] + m[4] * m[1] * m[14]
                - m[4] * m[2] * m[13] - m[12] * m[1] * m[6] + m[12] * m[2] * m[5];
        inv[3] = -m[1] * m[6] * m[11] + m[1] * m[7] * m[10] + m[5] * m[2] * m[11]
                - m[5] * m[3] * m[10] - m[9] * m[2] * m[7] + m[9] * m[3] * m[6];
        inv[7] = m[0] * m[6] * m[11] - m[0] * m[7] * m[10] - m[4] * m[2] * m[11]
                + m[4] * m[3] * m[10] + m[8] * m[2] * m[7] - m[8] * m[3] * m[6];
        inv[11] = -m[0] * m[5] * m[11] + m[0] * m[7] * m[9] + m[4] * m[1] * m[11]
                - m[4] * m[3] * m[9] - m[8] * m[1] * m[7] + m[8] * m[3] * m[5];
        inv[15] = m[0] * m[5] * m[10] - m[0] * m[6] * m[9] - m[4] * m[1] * m[10]
                + m[4] * m[2] * m[9] + m[8] * m[1] * m[6] - m[8] * m[2] * m[5];

        final double det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];
        if (det == 0) {
            setAllNaN();
            return;
        }

        final double invDet = 1.0 / det;
        for (int i = 0; i < inv.length; i++) {
            inv[i] = inv[i] * invDet;
        }
        setValues(inv, false);
    }

    private void setAllNaN() {
        final double[] values = new double[16];
        Arrays.fill(values, Double.NaN);
        setValues(values, false);
    }

    // ---------------------------------------------------------------------------------
    // helpers

    /**
     * Creates a new {@link DOMMatrix} with the values of this matrix.
     * @return the new matrix
     */
    protected DOMMatrix copyToDOMMatrix() {
        final DOMMatrix matrix = new DOMMatrix();
        matrix.setParentScope(getParentScope());
        matrix.setPrototype(ScriptableObject.getClassPrototype(getParentScope(), "DOMMatrix"));
        matrix.setValues(getValues(), is2D_);
        return matrix;
    }

    /**
     * Returns the values of this matrix in column-major order
     * (m11, m12, m13, m14, m21, ..., m44).
     * @return a new array with the 16 values
     */
    protected double[] getValues() {
        return new double[] {
            m11_, m12_, m13_, m14_,
            m21_, m22_, m23_, m24_,
            m31_, m32_, m33_, m34_,
            m41_, m42_, m43_, m44_
        };
    }

    /**
     * Sets all values of this matrix.
     * @param values the 16 values in column-major order (m11, m12, m13, m14, m21, ..., m44)
     * @param is2D the new value of the is2D flag
     */
    protected void setValues(final double[] values, final boolean is2D) {
        m11_ = values[0];
        m12_ = values[1];
        m13_ = values[2];
        m14_ = values[3];

        m21_ = values[4];
        m22_ = values[5];
        m23_ = values[6];
        m24_ = values[7];

        m31_ = values[8];
        m32_ = values[9];
        m33_ = values[10];
        m34_ = values[11];

        m41_ = values[12];
        m42_ = values[13];
        m43_ = values[14];
        m44_ = values[15];

        is2D_ = is2D;
    }

    /**
     * Sets this to the 2D matrix [a, b, c, d, e, f]; all other values are set to the
     * values of the identity matrix.
     *
     * @param a the a (m11) value
     * @param b the b (m12) value
     * @param c the c (m21) value
     * @param d the d (m22) value
     * @param e the e (m41) value
     * @param f the f (m42) value
     */
    protected void setValues2D(final double a, final double b, final double c,
                final double d, final double e, final double f) {
        setValues(new double[] {a, b, 0, 0, c, d, 0, 0, 0, 0, 1, 0, e, f, 0, 1}, true);
    }

    /**
     * Multiplies the two given matrices (left * right); both are in column-major order.
     *
     * <p>Like in the browsers, the product of two 2D matrices only calculates the 2D values
     * (a, b, c, d, e, f); the other values are not touched (and therefore never polluted
     * by NaN or infinite values).
     * A value of exactly 0 of the right matrix (the transformation applied) never contributes
     * to the result, a value of 0 of the left matrix does (0 * NaN is NaN).</p>
     *
     * @param left the left matrix
     * @param right the right matrix
     * @param only2D whether to calculate only the 2D values
     * @return the product
     */
    private static double[] multiplyMatrices(final double[] left, final double[] right, final boolean only2D) {
        if (only2D) {
            final double[] result = left.clone();
            for (final int col : new int[] {0, 1, 3}) {
                for (int row = 0; row < 2; row++) {
                    double sum = term(left[row], right[col * 4]) + term(left[4 + row], right[col * 4 + 1]);
                    if (col == 3) {
                        sum += left[12 + row];
                    }
                    result[col * 4 + row] = sum;
                }
            }
            return result;
        }

        final double[] result = new double[16];
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                double sum = 0;
                for (int k = 0; k < 4; k++) {
                    sum += term(left[k * 4 + row], right[col * 4 + k]);
                }
                result[col * 4 + row] = sum;
            }
        }
        return result;
    }

    private static double term(final double left, final double right) {
        if (right == 0) {
            return 0;
        }
        return left * right;
    }

    private static double mul(final double x, final double y) {
        if (x == 0 || y == 0) {
            return 0;
        }
        return x * y;
    }

    /**
     * Creates the matrix of a rotation by the given angle around the given axis
     * (see CSS rotate3d()). A rotation around the z-axis is a 2D matrix. For rotations
     * around one of the axes, multiples of 90 degrees result in exact values.
     *
     * @param x the x component of the axis
     * @param y the y component of the axis
     * @param z the z component of the axis
     * @param angle the angle in degrees
     * @return the rotation matrix
     */
    static DOMMatrixReadOnly createRotation(final double x, final double y, final double z, final double angle) {
        final DOMMatrixReadOnly rotation = new DOMMatrixReadOnly();

        if (x == 0 && y == 0) {
            // around the z-axis; this is 2D
            final double[] sinCos = sinCosDegrees(z < 0 ? -angle : angle);
            rotation.m11_ = sinCos[1];
            rotation.m12_ = sinCos[0];
            rotation.m21_ = -sinCos[0];
            rotation.m22_ = sinCos[1];
            return rotation;
        }

        if (y == 0 && z == 0) {
            // around the x-axis
            final double[] sinCos = sinCosDegrees(x < 0 ? -angle : angle);
            rotation.m22_ = sinCos[1];
            rotation.m23_ = sinCos[0];
            rotation.m32_ = -sinCos[0];
            rotation.m33_ = sinCos[1];
            rotation.is2D_ = false;
            return rotation;
        }

        if (x == 0 && z == 0) {
            // around the y-axis
            final double[] sinCos = sinCosDegrees(y < 0 ? -angle : angle);
            rotation.m11_ = sinCos[1];
            rotation.m13_ = -sinCos[0];
            rotation.m31_ = sinCos[0];
            rotation.m33_ = sinCos[1];
            rotation.is2D_ = false;
            return rotation;
        }

        final double length = Math.sqrt(x * x + y * y + z * z);
        final double nx = x / length;
        final double ny = y / length;
        final double nz = z / length;

        final double halfAngle = Math.toRadians(angle) / 2;
        final double sc = Math.sin(halfAngle) * Math.cos(halfAngle);
        final double sq = Math.sin(halfAngle) * Math.sin(halfAngle);

        rotation.m11_ = 1 - 2 * mul(ny * ny + nz * nz, sq);
        rotation.m12_ = 2 * (mul(nx * ny, sq) + mul(nz, sc));
        rotation.m13_ = 2 * (mul(nx * nz, sq) - mul(ny, sc));

        rotation.m21_ = 2 * (mul(nx * ny, sq) - mul(nz, sc));
        rotation.m22_ = 1 - 2 * mul(nx * nx + nz * nz, sq);
        rotation.m23_ = 2 * (mul(ny * nz, sq) + mul(nx, sc));

        rotation.m31_ = 2 * (mul(nx * nz, sq) + mul(ny, sc));
        rotation.m32_ = 2 * (mul(ny * nz, sq) - mul(nx, sc));
        rotation.m33_ = 1 - 2 * mul(nx * nx + ny * ny, sq);

        rotation.is2D_ = false;
        return rotation;
    }

    /**
     * Returns the sine and the cosine of the given angle; multiples of 90 degrees
     * are returned as exact values.
     *
     * @param angle the angle in degrees
     * @return an array with the sine and the cosine
     */
    private static double[] sinCosDegrees(final double angle) {
        if (Double.isFinite(angle) && angle % 90 == 0) {
            final int quarter = (int) (((angle / 90) % 4 + 4) % 4);
            return switch (quarter) {
                case 1 -> new double[] {1, 0};
                case 2 -> new double[] {0, -1};
                case 3 -> new double[] {-1, 0};
                default -> new double[] {0, 1};
            };
        }

        final double radians = Math.toRadians(angle);
        return new double[] {Math.sin(radians), Math.cos(radians)};
    }

    /**
     * Converts the given JavaScript value into a double; {@code undefined}
     * (or a missing argument) is converted to the given default value.
     *
     * @param value the value
     * @param defaultValue the default value
     * @return the double value
     */
    protected static double toDouble(final Object value, final double defaultValue) {
        if (isMissing(value)) {
            return defaultValue;
        }
        return JavaScriptEngine.toNumber(value);
    }

    private static boolean isMissing(final Object value) {
        return value == null || JavaScriptEngine.isUndefined(value);
    }

    private static double getDictionaryValue(final Scriptable dictionary, final String name,
                final double defaultValue) {
        final Object value = ScriptableObject.getProperty(dictionary, name);
        if (value == Scriptable.NOT_FOUND || JavaScriptEngine.isUndefined(value)) {
            return defaultValue;
        }
        return JavaScriptEngine.toNumber(value);
    }

    /**
     * Converts the given value into a matrix following the rules of the
     * DOMMatrixInit dictionary (including the 'validate and fixup' steps).
     *
     * @param init the value to convert; a matrix, an object with matrix properties,
     *     {@code null} or {@code undefined} (the identity matrix)
     * @param methodName the name of the calling method for the error message
     * @param className the name of the calling class for the error message
     * @return a new matrix (without scope)
     */
    static DOMMatrixReadOnly fromMatrixInit(final Object init, final String methodName, final String className) {
        final DOMMatrixReadOnly result = new DOMMatrixReadOnly();
        if (isMissing(init)) {
            return result;
        }

        if (init instanceof DOMMatrixReadOnly matrix) {
            result.setValues(matrix.getValues(), matrix.is2D_);
            return result;
        }

        final String errorPrefix = "Failed to execute '" + methodName + "' on '" + className + "': ";
        if (!(init instanceof Scriptable dict)) {
            throw JavaScriptEngine.typeError(errorPrefix + "The provided value is not of type 'DOMMatrixInit'.");
        }

        // validate and fixup (2D)
        final double m11 = fixup(dict, "a", "m11", 1, errorPrefix);
        final double m12 = fixup(dict, "b", "m12", 0, errorPrefix);
        final double m21 = fixup(dict, "c", "m21", 0, errorPrefix);
        final double m22 = fixup(dict, "d", "m22", 1, errorPrefix);
        final double m41 = fixup(dict, "e", "m41", 0, errorPrefix);
        final double m42 = fixup(dict, "f", "m42", 0, errorPrefix);

        // validate and fixup (3D)
        final double m13 = getDictionaryValue(dict, "m13", 0);
        final double m14 = getDictionaryValue(dict, "m14", 0);
        final double m23 = getDictionaryValue(dict, "m23", 0);
        final double m24 = getDictionaryValue(dict, "m24", 0);
        final double m31 = getDictionaryValue(dict, "m31", 0);
        final double m32 = getDictionaryValue(dict, "m32", 0);
        final double m33 = getDictionaryValue(dict, "m33", 1);
        final double m34 = getDictionaryValue(dict, "m34", 0);
        final double m43 = getDictionaryValue(dict, "m43", 0);
        final double m44 = getDictionaryValue(dict, "m44", 1);

        final boolean only2DValues = m13 == 0 && m14 == 0 && m23 == 0 && m24 == 0
                && m31 == 0 && m32 == 0 && m34 == 0 && m43 == 0
                && m33 == 1 && m44 == 1;

        final Object is2DValue = ScriptableObject.getProperty(dict, "is2D");
        final boolean is2D;
        if (is2DValue == Scriptable.NOT_FOUND || JavaScriptEngine.isUndefined(is2DValue)) {
            is2D = only2DValues;
        }
        else {
            is2D = JavaScriptEngine.toBoolean(is2DValue);
            if (is2D && !only2DValues) {
                throw JavaScriptEngine.typeError(errorPrefix
                        + "The 'is2D' property is true, but the matrix contains 3D values.");
            }
        }

        result.setValues(new double[] {
            m11, m12, m13, m14,
            m21, m22, m23, m24,
            m31, m32, m33, m34,
            m41, m42, m43, m44}, is2D);
        return result;
    }

    /**
     * Like {@link #fromMatrixInit(Object, String, String)}, but for the multiply functions;
     * for an object with matrix properties (not a matrix), the browsers ignore an
     * {@code is2D} property with the value {@code false}; only the values decide.
     *
     * @param init the value to convert
     * @param methodName the name of the calling method for the error message
     * @param className the name of the calling class for the error message
     * @return a new matrix (without scope)
     */
    static DOMMatrixReadOnly fromMatrixInitForMultiply(final Object init, final String methodName,
            final String className) {
        final DOMMatrixReadOnly result = fromMatrixInit(init, methodName, className);
        if (!(init instanceof DOMMatrixReadOnly)) {
            result.is2D_ = result.hasOnly2DValues();
        }
        return result;
    }

    /**
     * Returns whether this matrix contains only 2D values (m13, m14, m23, m24, m31, m32,
     * m34 and m43 are 0, m33 and m44 are 1).
     * @return true if the values are 2D values
     */
    boolean hasOnly2DValues() {
        return m13_ == 0 && m14_ == 0 && m23_ == 0 && m24_ == 0
                && m31_ == 0 && m32_ == 0 && m34_ == 0 && m43_ == 0
                && m33_ == 1 && m44_ == 1;
    }

    private static double fixup(final Scriptable dict, final String shortName, final String longName,
                final double defaultValue, final String errorPrefix) {
        final Object shortValue = ScriptableObject.getProperty(dict, shortName);
        final Object longValue = ScriptableObject.getProperty(dict, longName);

        final boolean hasShort = shortValue != Scriptable.NOT_FOUND && !JavaScriptEngine.isUndefined(shortValue);
        final boolean hasLong = longValue != Scriptable.NOT_FOUND && !JavaScriptEngine.isUndefined(longValue);

        if (hasLong) {
            final double value = JavaScriptEngine.toNumber(longValue);
            if (hasShort) {
                final double shortNumber = JavaScriptEngine.toNumber(shortValue);
                // SameValueZero
                final boolean same = value == shortNumber || (Double.isNaN(value) && Double.isNaN(shortNumber));
                if (!same) {
                    throw JavaScriptEngine.typeError(errorPrefix + "The '" + shortName + "' property should equal "
                            + "the '" + longName + "' property.");
                }
            }
            return value;
        }
        if (hasShort) {
            return JavaScriptEngine.toNumber(shortValue);
        }
        return defaultValue;
    }

    /**
     * Returns a new Float32Array containing all 16 elements of the matrix.
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
     * Returns a new Float64Array containing all 16 elements of the matrix.
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
     * Returns the values of the matrix as a CSS transform function string.
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

    /**
     * {@inheritDoc}
     * The string representation (also used for the conversion of a matrix into a string,
     * e.g. when passing a matrix to the constructor) is the CSS transform function.
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (String.class.equals(hint) || hint == null) {
            return js_toString();
        }
        return super.getDefaultValue(hint);
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
     * Returns a JSON representation of the DOMMatrixReadOnly object.
     * @return a JSON representation of the DOMMatrixReadOnly object
     */
    @JsxFunction
    public Scriptable toJSON() {
        final Scriptable json = JavaScriptEngine.newObject(getParentScope());
        json.put("a", json, m11_);
        json.put("b", json, m12_);
        json.put("c", json, m21_);
        json.put("d", json, m22_);
        json.put("e", json, m41_);
        json.put("f", json, m42_);

        json.put("m11", json, m11_);
        json.put("m12", json, m12_);
        json.put("m13", json, m13_);
        json.put("m14", json, m14_);

        json.put("m21", json, m21_);
        json.put("m22", json, m22_);
        json.put("m23", json, m23_);
        json.put("m24", json, m24_);

        json.put("m31", json, m31_);
        json.put("m32", json, m32_);
        json.put("m33", json, m33_);
        json.put("m34", json, m34_);

        json.put("m41", json, m41_);
        json.put("m42", json, m42_);
        json.put("m43", json, m43_);
        json.put("m44", json, m44_);

        json.put("is2D", json, is2D_);
        json.put("isIdentity", json, getIsIdentity());

        return json;
    }

    /**
     * Creates a new {@code DOMMatrixReadOnly} from the given matrix or matrix properties.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj the scriptable this object
     * @param args the arguments; the first one is the matrix or an object with matrix
     *     properties (DOMMatrixInit)
     * @param funObj the function object
     * @return the new matrix
     */
    @JsxStaticFunction(functionName = "fromMatrix")
    public static DOMMatrixReadOnly fromMatrixReadOnly(final Context cx, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function funObj) {
        final DOMMatrixReadOnly init = fromMatrixInit(args.length > 0 ? args[0] : null,
                "fromMatrix", "DOMMatrixReadOnly");
        return newInstance(new DOMMatrixReadOnly(), scope, "DOMMatrixReadOnly", init);
    }

    /**
     * Creates a new {@code DOMMatrixReadOnly} from the given Float32Array with 6 (2D)
     * or 16 (3D) values.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj the scriptable this object
     * @param args the arguments; the first one is the Float32Array
     * @param funObj the function object
     * @return the new matrix
     */
    @JsxStaticFunction(functionName = "fromFloat32Array")
    public static DOMMatrixReadOnly fromFloat32ArrayReadOnly(final Context cx, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function funObj) {
        final DOMMatrixReadOnly init = fromTypedArray(args, NativeFloat32Array.class,
                "fromFloat32Array", "DOMMatrixReadOnly");
        return newInstance(new DOMMatrixReadOnly(), scope, "DOMMatrixReadOnly", init);
    }

    /**
     * Creates a new {@code DOMMatrixReadOnly} from the given Float64Array with 6 (2D)
     * or 16 (3D) values.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj the scriptable this object
     * @param args the arguments; the first one is the Float64Array
     * @param funObj the function object
     * @return the new matrix
     */
    @JsxStaticFunction(functionName = "fromFloat64Array")
    public static DOMMatrixReadOnly fromFloat64ArrayReadOnly(final Context cx, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function funObj) {
        final DOMMatrixReadOnly init = fromTypedArray(args, NativeFloat64Array.class,
                "fromFloat64Array", "DOMMatrixReadOnly");
        return newInstance(new DOMMatrixReadOnly(), scope, "DOMMatrixReadOnly", init);
    }

    /**
     * Initializes the given new matrix object with the scope, the prototype and the values.
     *
     * @param <T> the matrix type
     * @param matrix the new matrix object
     * @param scope the scope
     * @param className the JavaScript class name used to find the prototype
     * @param values the matrix to copy the values from
     * @return the given matrix
     */
    protected static <T extends DOMMatrixReadOnly> T newInstance(final T matrix, final VarScope scope,
            final String className, final DOMMatrixReadOnly values) {
        matrix.setParentScope(scope);
        matrix.setPrototype(ScriptableObject.getClassPrototype(scope, className));
        matrix.setValues(values.getValues(), values.is2D_);
        return matrix;
    }

    /**
     * Converts the first argument (a typed array of the given type with 6 or 16 values)
     * into a matrix.
     *
     * @param args the arguments
     * @param type the expected typed array type
     * @param methodName the name of the calling method for the error message
     * @param className the name of the calling class for the error message
     * @return a new matrix (without scope)
     */
    protected static DOMMatrixReadOnly fromTypedArray(final Object[] args,
            final Class<? extends NativeTypedArrayView<?>> type,
            final String methodName, final String className) {
        final String errorPrefix = "Failed to execute '" + methodName + "' on '" + className + "': ";
        if (args.length == 0 || !type.isInstance(args[0])) {
            throw JavaScriptEngine.typeError(errorPrefix + "parameter 1 is not of type '"
                    + (type == NativeFloat32Array.class ? "Float32Array" : "Float64Array") + "'.");
        }

        final NativeTypedArrayView<?> array = (NativeTypedArrayView<?>) args[0];
        final int length = array.getArrayLength();
        final double[] values = new double[length];
        for (int i = 0; i < length; i++) {
            values[i] = JavaScriptEngine.toNumber(array.getArrayElement(i));
        }

        final DOMMatrixReadOnly result = new DOMMatrixReadOnly();
        if (length == 6) {
            result.setValues2D(values[0], values[1], values[2], values[3], values[4], values[5]);
            return result;
        }
        if (length == 16) {
            result.setValues(values, false);
            return result;
        }

        throw JavaScriptEngine.typeError(errorPrefix
                + "The sequence must contain 6 elements for a 2D matrix or 16 elements for a 3D matrix.");
    }
}
