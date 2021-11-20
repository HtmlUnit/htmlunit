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
package com.gargoylesoftware.htmlunit.javascript.host.svg;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;

/**
 * A JavaScript object for {@code SVGMatrix}.
 * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/SVGMatrix">MDN doc</a>
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@JsxClass
public class SVGMatrix extends SimpleScriptable {

    private double shearX_;
    private double shearY_;
    private double scaleX_;
    private double scaleY_;
    private double translateX_;
    private double translateY_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public SVGMatrix() {
        shearX_ = 0.0;
        shearY_ = 0.0;
        scaleX_ = 1.0;
        scaleY_ = 1.0;
        translateX_ = 0.0;
        translateY_ = 0.0;
    }

    /**
     * Instantiates and configure scope and prototype.
     * @param scope the parent scope
     */
    public SVGMatrix(final Window scope) {
        this();
        setParentScope(scope);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Gets the <code>a</code> entry of the matrix.
     * @return the field
     */
    @JsxGetter
    public double getA() {
        return scaleX_;
    }

    /**
     * Gets the <code>b</code> entry of the matrix.
     * @return the field
     */
    @JsxGetter
    public double getB() {
        return shearY_;
    }

    /**
     * Gets the <code>c</code> entry of the matrix.
     * @return the field
     */
    @JsxGetter
    public double getC() {
        return shearX_;
    }

    /**
     * Gets the <code>d</code> entry of the matrix.
     * @return the field
     */
    @JsxGetter
    public double getD() {
        return scaleY_;
    }

    /**
     * Gets the <code>e</code> entry of the matrix.
     * @return the field
     */
    @JsxGetter
    public double getE() {
        return translateX_;
    }

    /**
     * Gets the <code>f</code> entry of the matrix.
     * @return the field
     */
    @JsxGetter
    public double getF() {
        return translateY_;
    }

    /**
     * Sets the <code>a</code> entry of the matrix.
     * @param newValue the new value for the field
     */
    @JsxSetter
    public void setA(final double newValue) {
        scaleX_ = newValue;
    }

    /**
     * Sets the <code>b</code> entry of the matrix.
     * @param newValue the new value for the field
     */
    @JsxSetter
    public void setB(final double newValue) {
        shearY_ = newValue;
    }

    /**
     * Sets the <code>c</code> entry of the matrix.
     * @param newValue the new value for the field
     */
    @JsxSetter
    public void setC(final double newValue) {
        shearX_ = newValue;
    }

    /**
     * Sets the <code>d</code> entry of the matrix.
     * @param newValue the new value for the field
     */
    @JsxSetter
    public void setD(final double newValue) {
        scaleY_ = newValue;
    }

    /**
     * Sets the <code>e</code> entry of the matrix.
     * @param newValue the new value for the field
     */
    @JsxSetter
    public void setE(final double newValue) {
        translateX_ = newValue;
    }

    /**
     * Sets the <code>f</code> entry of the matrix.
     * @param newValue the new value for the field
     */
    @JsxSetter
    public void setF(final double newValue) {
        translateY_ = newValue;
    }

    /**
     * Transforms the matrix.
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix flipX() {
        final SVGMatrix result = new SVGMatrix(getWindow());
        result.shearX_ = shearX_;
        result.shearY_ = -shearY_;
        result.scaleX_ = -scaleX_;
        result.scaleY_ = scaleY_;
        result.translateX_ = translateX_;
        result.translateY_ = translateY_;

        return result;
    }

    /**
     * Transforms the matrix.
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix flipY() {
        final SVGMatrix result = new SVGMatrix(getWindow());
        result.shearX_ = -shearX_;
        result.shearY_ = shearY_;
        result.scaleX_ = scaleX_;
        result.scaleY_ = -scaleY_;
        result.translateX_ = translateX_;
        result.translateY_ = translateY_;

        return result;
    }

    /**
     * Transforms the matrix.
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix inverse() {
        final double determinant = scaleX_ * scaleY_ - shearX_ * shearY_;

        if (Math.abs(determinant) < 1E-10) {
            throw ScriptRuntime.constructError("Error",
                    "Failed to execute 'inverse' on 'SVGMatrix': The matrix is not invertible.");
        }

        final SVGMatrix result = new SVGMatrix(getWindow());
        result.shearX_ = -shearX_ / determinant;
        result.shearY_ = -shearY_ / determinant;
        result.scaleX_ = scaleY_ / determinant;
        result.scaleY_ = scaleX_ / determinant;
        result.translateX_ = (shearX_ * translateY_ - scaleY_ * translateX_) / determinant;
        result.translateY_ = (shearY_ * translateX_ - scaleX_ * translateY_) / determinant;

        return result;
    }

    /**
     * Transforms the matrix.
     * @param by the matrix to multiply by
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix multiply(final SVGMatrix by) {
        final SVGMatrix result = new SVGMatrix(getWindow());

        result.shearX_ = by.shearX_ * scaleX_ + by.scaleY_ * shearX_;
        result.shearY_ = by.scaleX_ * shearY_ + by.shearY_ * scaleY_;
        result.scaleX_ = by.scaleX_ * scaleX_ + by.shearY_ * shearX_;
        result.scaleY_ = by.shearX_ * shearY_ + by.scaleY_ * scaleY_;
        result.translateX_ = by.translateX_ * scaleX_ + by.translateY_ * shearX_ + translateX_;
        result.translateY_ = by.translateX_ * shearY_ + by.translateY_ * scaleY_ + translateY_;

        return result;
    }

    /**
     * Rotates the matrix.
     * @param angle the rotation angle
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix rotate(final double angle) {
        final double theta = Math.toRadians(angle);
        final double sin = Math.sin(theta);
        final double cos = Math.cos(theta);

        final SVGMatrix result = new SVGMatrix(getWindow());

        result.shearX_ = -sin * scaleX_ + cos * shearX_;
        result.shearY_ = cos * shearY_ + sin * scaleY_;
        result.scaleX_ = cos * scaleX_ + sin * shearX_;
        result.scaleY_ = -sin * shearY_ + cos * scaleY_;
        result.translateX_ = translateX_;
        result.translateY_ = translateY_;

        return result;
    }

    /**
     * Transforms the matrix.
     * @param x the x-coordinate of the vector
     * @param y the y-coordinate of the vector
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix rotateFromVector(final double x, final double y) {
        if (x == 0 || y == 0) {
            throw ScriptRuntime.constructError("Error",
                    "Failed to execute 'rotateFromVector' on 'SVGMatrix': Arguments cannot be zero.");
        }

        final double theta = Math.atan2(y, x);
        final double sin = Math.sin(theta);
        final double cos = Math.cos(theta);

        final SVGMatrix result = new SVGMatrix(getWindow());

        result.shearX_ = -sin * scaleX_ + cos * shearX_;
        result.shearY_ = cos * shearY_ + sin * scaleY_;
        result.scaleX_ = cos * scaleX_ + sin * shearX_;
        result.scaleY_ = -sin * shearY_ + cos * scaleY_;
        result.translateX_ = translateX_;
        result.translateY_ = translateY_;

        return result;
    }

    /**
     * Transforms the matrix.
     * @param factor the scale factor
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix scale(final double factor) {
        return scaleNonUniform(factor, factor);
    }

    /**
     * Transforms the matrix.
     * @param factorX the factor for the x-axis
     * @param factorY the factor for the y-axis
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix scaleNonUniform(final double factorX, final double factorY) {
        final SVGMatrix result = new SVGMatrix(getWindow());

        result.shearX_ = factorY * shearX_;
        result.shearY_ = factorX * shearY_;
        result.scaleX_ = factorX * scaleX_;
        result.scaleY_ = factorY * scaleY_;
        result.translateX_ = translateX_;
        result.translateY_ = translateY_;

        return result;
    }

    /**
     * Transforms the matrix.
     * @param angle the skew angle
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix skewX(final double angle) {
        final double shear = Math.tan(Math.toRadians(angle));

        final SVGMatrix result = new SVGMatrix(getWindow());

        result.shearX_ = shear * scaleX_ + shearX_;
        result.shearY_ = shearY_;
        result.scaleX_ = scaleX_;
        result.scaleY_ = shear * shearY_ + scaleY_;
        result.translateX_ = translateX_;
        result.translateY_ = translateY_;

        return result;
    }

    /**
     * Transforms the matrix.
     * @param angle the skew angle
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix skewY(final double angle) {
        final double shear = Math.tan(Math.toRadians(angle));

        final SVGMatrix result = new SVGMatrix(getWindow());

        result.shearX_ = shearX_;
        result.shearY_ = shearY_ + shear * scaleY_;
        result.scaleX_ = scaleX_ + shear * shearX_;
        result.scaleY_ = scaleY_;
        result.translateX_ = translateX_;
        result.translateY_ = translateY_;

        return result;
    }

    /**
     * Translates the matrix.
     * @param x the distance along the x-axis
     * @param y the distance along the y-axis
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix translate(final double x, final double y) {
        final SVGMatrix result = new SVGMatrix(getWindow());

        result.shearX_ = shearX_;
        result.shearY_ = shearY_;
        result.scaleX_ = scaleX_;
        result.scaleY_ = scaleY_;
        result.translateX_ = x * scaleX_ + y * shearX_ + translateX_;
        result.translateY_ = x * shearY_ + y * scaleY_ + translateY_;

        return result;
    }
}
