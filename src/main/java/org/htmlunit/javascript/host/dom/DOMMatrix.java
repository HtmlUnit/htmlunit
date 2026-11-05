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

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.corejs.javascript.typedarrays.NativeFloat32Array;
import org.htmlunit.corejs.javascript.typedarrays.NativeFloat64Array;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxConstructorAlias;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.configuration.JsxStaticFunction;

/**
 * A JavaScript object for {@code DOMMatrix}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/DOMMatrix">MDN Documentation</a>
 */
@JsxClass
public class DOMMatrix extends DOMMatrixReadOnly {

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
    @JsxConstructorAlias(alias = "WebKitCSSMatrix")
    public static DOMMatrix jsConstructor(final Context cx, final VarScope scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {

        final DOMMatrix matrix = new DOMMatrix();
        matrix.init(args, scope, ctorObj);
        return matrix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM11() {
        return super.getM11();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM11(final double m11) {
        super.setM11(m11);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM12() {
        return super.getM12();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM12(final double m12) {
        super.setM12(m12);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM13() {
        return super.getM13();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM13(final double m13) {
        super.setM13(m13);
        if (m13 != 0) {
            setIs2D(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM14() {
        return super.getM14();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM14(final double m14) {
        super.setM14(m14);
        if (m14 != 0) {
            setIs2D(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM21() {
        return super.getM21();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM21(final double m21) {
        super.setM21(m21);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM22() {
        return super.getM22();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM22(final double m22) {
        super.setM22(m22);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM23() {
        return super.getM23();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM23(final double m23) {
        super.setM23(m23);
        if (m23 != 0) {
            setIs2D(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM24() {
        return super.getM24();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM24(final double m24) {
        super.setM24(m24);
        if (m24 != 0) {
            setIs2D(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM31() {
        return super.getM31();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM31(final double m31) {
        super.setM31(m31);
        if (m31 != 0) {
            setIs2D(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM32() {
        return super.getM32();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM32(final double m32) {
        super.setM32(m32);
        if (m32 != 0) {
            setIs2D(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM33() {
        return super.getM33();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM33(final double m33) {
        super.setM33(m33);
        if (m33 != 1) {
            setIs2D(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM34() {
        return super.getM34();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM34(final double m34) {
        super.setM34(m34);
        if (m34 != 0) {
            setIs2D(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM41() {
        return super.getM41();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM41(final double m41) {
        super.setM41(m41);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM42() {
        return super.getM42();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM42(final double m42) {
        super.setM42(m42);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM43() {
        return super.getM43();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM43(final double m43) {
        super.setM43(m43);
        if (m43 != 0) {
            setIs2D(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getM44() {
        return super.getM44();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setM44(final double m44) {
        super.setM44(m44);
        if (m44 != 1) {
            setIs2D(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getA() {
        return super.getA();
    }

    /**
     * Sets the {@code a} property.
     * @param a the new value
     */
    @JsxSetter
    public void setA(final double a) {
        super.setM11(a);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getB() {
        return super.getB();
    }

    /**
     * Sets the {@code b} property.
     * @param b the new value
     */
    @JsxSetter
    public void setB(final double b) {
        super.setM12(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getC() {
        return super.getC();
    }

    /**
     * Sets the {@code c} property.
     * @param c the new value
     */
    @JsxSetter
    public void setC(final double c) {
        super.setM21(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getD() {
        return super.getD();
    }

    /**
     * Sets the {@code d} property.
     * @param d the new value
     */
    @JsxSetter
    public void setD(final double d) {
        super.setM22(d);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getE() {
        return super.getE();
    }

    /**
     * Sets the {@code e} property.
     * @param e the new value
     */
    @JsxSetter
    public void setE(final double e) {
        super.setM41(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getF() {
        return super.getF();
    }

    /**
     * Sets the {@code f} property.
     * @param f the new value
     */
    @JsxSetter
    public void setF(final double f) {
        super.setM42(f);
    }

    /**
     * Inverts this matrix. If the matrix cannot be inverted,
     * the components are all set to NaN and its is2D property is set to false.
     * @return this matrix, inverted
     */
    @JsxFunction
    public DOMMatrix invertSelf() {
        invertSelfInternal();
        return this;
    }

    /**
     * Post-multiplies this matrix with the given one (this = this * other).
     *
     * @param other the matrix to multiply with; this can be a matrix or an
     *     object with matrix properties (DOMMatrixInit); {@code null} and {@code undefined}
     *     are treated as identity matrix
     * @return this matrix
     */
    @JsxFunction
    public DOMMatrix multiplySelf(final Object other) {
        multiplySelfInternal(fromMatrixInitForMultiply(other, "multiplySelf", getClassName()));
        return this;
    }

    /**
     * Pre-multiplies this matrix with the given one (this = other * this).
     *
     * @param other the matrix to multiply with; this can be a matrix or an
     *     object with matrix properties (DOMMatrixInit); {@code null} and {@code undefined}
     *     are treated as identity matrix
     * @return this matrix
     */
    @JsxFunction
    public DOMMatrix preMultiplySelf(final Object other) {
        preMultiplySelfInternal(fromMatrixInitForMultiply(other, "preMultiplySelf", getClassName()));
        return this;
    }

    /**
     * Applies a translation to this matrix.
     *
     * @param tx the x component of the translating vector (default 0)
     * @param ty the y component of the translating vector (default 0)
     * @param tz the z component of the translating vector (default 0); if this is anything other
     *     than 0, the matrix will be 3D
     * @return this matrix
     */
    @JsxFunction
    public DOMMatrix translateSelf(final Object tx, final Object ty, final Object tz) {
        translateSelfInternal(toDouble(tx, 0), toDouble(ty, 0), toDouble(tz, 0));
        return this;
    }

    /**
     * Scales this matrix by the given factors, centered on the given origin.
     *
     * @param scaleX the scale factor for the x-axis (default 1)
     * @param scaleY the scale factor for the y-axis (default scaleX)
     * @param scaleZ the scale factor for the z-axis (default 1)
     * @param originX the x-coordinate of the origin (default 0)
     * @param originY the y-coordinate of the origin (default 0)
     * @param originZ the z-coordinate of the origin (default 0)
     * @return this matrix
     */
    @JsxFunction
    public DOMMatrix scaleSelf(final Object scaleX, final Object scaleY, final Object scaleZ,
                final Object originX, final Object originY, final Object originZ) {
        final double sx = toDouble(scaleX, 1);
        scaleSelfInternal(sx, toDouble(scaleY, sx), toDouble(scaleZ, 1),
                toDouble(originX, 0), toDouble(originY, 0), toDouble(originZ, 0));
        return this;
    }

    /**
     * Scales all three axes of this matrix by the given factor, centered on the given origin.
     *
     * @param scale the scale factor (default 1)
     * @param originX the x-coordinate of the origin (default 0)
     * @param originY the y-coordinate of the origin (default 0)
     * @param originZ the z-coordinate of the origin (default 0)
     * @return this matrix
     */
    @JsxFunction
    public DOMMatrix scale3dSelf(final Object scale, final Object originX, final Object originY,
                final Object originZ) {
        scale3dSelfInternal(toDouble(scale, 1), toDouble(originX, 0), toDouble(originY, 0), toDouble(originZ, 0));
        return this;
    }

    /**
     * Rotates this matrix by the specified angles.
     * If only one argument is given, this is the rotation angle around the z-axis
     * (a 2D rotation), otherwise the matrix is rotated around the z-axis, then around
     * the y-axis and finally around the x-axis.
     *
     * @param rotX the rotation angle around the x-axis in degrees (or the rotation angle
     *     around the z-axis if this is the only argument)
     * @param rotY the rotation angle around the y-axis in degrees
     * @param rotZ the rotation angle around the z-axis in degrees
     * @return this matrix
     */
    @JsxFunction
    public DOMMatrix rotateSelf(final Object rotX, final Object rotY, final Object rotZ) {
        rotateSelfInternal(rotX, rotY, rotZ);
        return this;
    }

    /**
     * Rotates this matrix by the angle between the specified vector and (1, 0).
     *
     * @param x the x component of the vector (default 0)
     * @param y the y component of the vector (default 0)
     * @return this matrix
     */
    @JsxFunction
    public DOMMatrix rotateFromVectorSelf(final Object x, final Object y) {
        rotateFromVectorSelfInternal(toDouble(x, 0), toDouble(y, 0));
        return this;
    }

    /**
     * Rotates this matrix by the given angle around the specified axis.
     *
     * @param x the x component of the axis (default 0)
     * @param y the y component of the axis (default 0)
     * @param z the z component of the axis (default 0)
     * @param angle the rotation angle in degrees (default 0)
     * @return this matrix
     */
    @JsxFunction
    public DOMMatrix rotateAxisAngleSelf(final Object x, final Object y, final Object z, final Object angle) {
        rotateAxisAngleSelfInternal(toDouble(x, 0), toDouble(y, 0), toDouble(z, 0), toDouble(angle, 0));
        return this;
    }

    /**
     * Skews this matrix along the x-axis by the specified angle.
     *
     * @param sx the angle in degrees (default 0)
     * @return this matrix
     */
    @JsxFunction
    public DOMMatrix skewXSelf(final Object sx) {
        skewXSelfInternal(toDouble(sx, 0));
        return this;
    }

    /**
     * Skews this matrix along the y-axis by the specified angle.
     *
     * @param sy the angle in degrees (default 0)
     * @return this matrix
     */
    @JsxFunction
    public DOMMatrix skewYSelf(final Object sy) {
        skewYSelfInternal(toDouble(sy, 0));
        return this;
    }

    /**
     * Replaces the values of this matrix with the matrix described by the given
     * CSS transform list (e.g. {@code "translate(10px, 20px) rotate(45deg)"}).
     *
     * @param transformList the CSS transform list
     * @return this matrix
     */
    @JsxFunction
    public DOMMatrix setMatrixValue(final String transformList) {
        setTransformList(transformList);
        return this;
    }

    /**
     * Creates a new {@code DOMMatrix} from the given matrix or matrix properties.
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
    public static DOMMatrix fromMatrix(final Context cx, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function funObj) {
        final DOMMatrixReadOnly init = fromMatrixInit(args.length > 0 ? args[0] : null,
                "fromMatrix", "DOMMatrix");
        return newInstance(new DOMMatrix(), scope, "DOMMatrix", init);
    }

    /**
     * Creates a new {@code DOMMatrix} from the given Float32Array with 6 (2D)
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
    public static DOMMatrix fromFloat32Array(final Context cx, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function funObj) {
        final DOMMatrixReadOnly init = fromTypedArray(args, NativeFloat32Array.class, "fromFloat32Array", "DOMMatrix");
        return newInstance(new DOMMatrix(), scope, "DOMMatrix", init);
    }

    /**
     * Creates a new {@code DOMMatrix} from the given Float64Array with 6 (2D)
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
    public static DOMMatrix fromFloat64Array(final Context cx, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function funObj) {
        final DOMMatrixReadOnly init = fromTypedArray(args, NativeFloat64Array.class, "fromFloat64Array", "DOMMatrix");
        return newInstance(new DOMMatrix(), scope, "DOMMatrix", init);
    }
}
