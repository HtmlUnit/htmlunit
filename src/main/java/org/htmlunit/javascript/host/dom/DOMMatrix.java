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
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxConstructorAlias;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * A JavaScript object for {@code DOMMatrix}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
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
    public static DOMMatrix jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {

        final DOMMatrix matrix = new DOMMatrix();
        matrix.init(args, ctorObj);
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
     * @param f the new value
     */
    @JsxSetter
    public void setF(final double f) {
        super.setM42(f);
    }

    /**
     * @return inverts the original matrix. If the matrix cannot be inverted,
     *     the new matrix's components are all set to NaN and its is2D property is set to false.
     */
    @JsxFunction
    public DOMMatrix invertSelf() {
        if (isIs2D()) {
            final double det = getM11() * getM22() - getM12() * getM21();
            if (det == 0) {
                // Not invertible: set all to NaN, is2D_ = false
                initWithNan();
                return this;
            }

            final double[] inv = new double[6];
            inv[0] = getM22() / det;
            inv[1] = -getM12() / det;

            inv[2] = -getM21() / det;
            inv[3] = getM11() / det;

            inv[4] = (getM21() * getM42() - getM22() * getM41()) / det;
            inv[5] = (getM12() * getM41() - getM11() * getM42()) / det;

            setM11(inv[0]);
            setM12(inv[1]);
            setM21(inv[2]);
            setM22(inv[3]);
            setM41(inv[4]);
            setM42(inv[5]);
            setIs2D(true);
            return this;
        }

        final double[] inv = new double[16];
        inv[0] = getM22() * getM33() * getM44()
                - getM22() * getM34() * getM43()
                - getM32() * getM23() * getM44()
                + getM32() * getM24() * getM43()
                + getM42() * getM23() * getM34()
                - getM42() * getM24() * getM33();

        inv[4] = -getM21() * getM33() * getM44()
                + getM21() * getM34() * getM43()
                + getM31() * getM23() * getM44()
                - getM31() * getM24() * getM43()
                - getM41() * getM23() * getM34()
                + getM41() * getM24() * getM33();

        inv[8] = getM21() * getM32() * getM44()
                - getM21() * getM34() * getM42()
                - getM31() * getM22() * getM44()
                + getM31() * getM24() * getM42()
                + getM41() * getM22() * getM34()
                - getM41() * getM24() * getM32();

        inv[12] = -getM21() * getM32() * getM43()
                + getM21() * getM33() * getM42()
                + getM31() * getM22() * getM43()
                - getM31() * getM23() * getM42()
                - getM41() * getM22() * getM33()
                + getM41() * getM23() * getM32();

        inv[1] = -getM12() * getM33() * getM44()
                + getM12() * getM34() * getM43()
                + getM32() * getM13() * getM44()
                - getM32() * getM14() * getM43()
                - getM42() * getM13() * getM34()
                + getM42() * getM14() * getM33();

        inv[5] = getM11() * getM33() * getM44()
                - getM11() * getM34() * getM43()
                - getM31() * getM13() * getM44()
                + getM31() * getM14() * getM43()
                + getM41() * getM13() * getM34()
                - getM41() * getM14() * getM33();

        inv[9] = -getM11() * getM32() * getM44()
                + getM11() * getM34() * getM42()
                + getM31() * getM12() * getM44()
                - getM31() * getM14() * getM42()
                - getM41() * getM12() * getM34()
                + getM41() * getM14() * getM32();

        inv[13] = getM11() * getM32() * getM43()
                - getM11() * getM33() * getM42()
                - getM31() * getM12() * getM43()
                + getM31() * getM13() * getM42()
                + getM41() * getM12() * getM33()
                - getM41() * getM13() * getM32();

        inv[2] = getM12() * getM23() * getM44()
                - getM12() * getM24() * getM43()
                - getM22() * getM13() * getM44()
                + getM22() * getM14() * getM43()
                + getM42() * getM13() * getM24()
                - getM42() * getM14() * getM23();

        inv[6] = -getM11() * getM23() * getM44()
                + getM11() * getM24() * getM43()
                + getM21() * getM13() * getM44()
                - getM21() * getM14() * getM43()
                - getM41() * getM13() * getM24()
                + getM41() * getM14() * getM23();

        inv[10] = getM11() * getM22() * getM44()
                - getM11() * getM24() * getM42()
                - getM21() * getM12() * getM44()
                + getM21() * getM14() * getM42()
                + getM41() * getM12() * getM24()
                - getM41() * getM14() * getM22();

        inv[14] = -getM11() * getM22() * getM43()
                + getM11() * getM23() * getM42()
                + getM21() * getM12() * getM43()
                - getM21() * getM13() * getM42()
                - getM41() * getM12() * getM23()
                + getM41() * getM13() * getM22();

        inv[3] = -getM12() * getM23() * getM34()
                + getM12() * getM24() * getM33()
                + getM22() * getM13() * getM34()
                - getM22() * getM14() * getM33()
                - getM32() * getM13() * getM24()
                + getM32() * getM14() * getM23();

        inv[7] = getM11() * getM23() * getM34()
                - getM11() * getM24() * getM33()
                - getM21() * getM13() * getM34()
                + getM21() * getM14() * getM33()
                + getM31() * getM13() * getM24()
                - getM31() * getM14() * getM23();

        inv[11] = -getM11() * getM22() * getM34()
                + getM11() * getM24() * getM32()
                + getM21() * getM12() * getM34()
                - getM21() * getM14() * getM32()
                - getM31() * getM12() * getM24()
                + getM31() * getM14() * getM22();

        inv[15] = getM11() * getM22() * getM33()
                - getM11() * getM23() * getM32()
                - getM21() * getM12() * getM33()
                + getM21() * getM13() * getM32()
                + getM31() * getM12() * getM23()
                - getM31() * getM13() * getM22();

        double det = getM11() * inv[0] + getM12() * inv[4] + getM13() * inv[8] + getM14() * inv[12];

        if (det == 0) {
            // Not invertible: set all to NaN, is2D_ = false
            initWithNan();
            return this;
        }

        det = 1.0 / det;

        setM11(inv[0] * det);
        setM12(inv[1] * det);
        setM13(inv[2] * det);
        setM14(inv[3] * det);

        setM21(inv[4] * det);
        setM22(inv[5] * det);
        setM23(inv[6] * det);
        setM24(inv[7] * det);

        setM31(inv[8] * det);
        setM32(inv[9] * det);
        setM33(inv[10] * det);
        setM34(inv[11] * det);

        setM41(inv[12] * det);
        setM42(inv[13] * det);
        setM43(inv[14] * det);
        setM44(inv[15] * det);

        setIs2D(false);
        return this;
    }

    private void initWithNan() {
        setM11(Double.NaN);
        setM12(Double.NaN);
        setM13(Double.NaN);
        setM14(Double.NaN);
        setM21(Double.NaN);
        setM22(Double.NaN);
        setM23(Double.NaN);
        setM24(Double.NaN);
        setM31(Double.NaN);
        setM32(Double.NaN);
        setM33(Double.NaN);
        setM34(Double.NaN);
        setM41(Double.NaN);
        setM42(Double.NaN);
        setM43(Double.NaN);
        setM44(Double.NaN);
        setIs2D(false);
    }
}
