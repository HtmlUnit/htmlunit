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
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxConstructorAlias;
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
}
