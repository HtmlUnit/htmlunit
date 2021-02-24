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
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

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

    private static final MatrixTransformer matrixTransformer_ = new MatrixTransformer();
    private MatrixTransformer.SvgMatrix svgMatrix_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public SVGMatrix() {
        svgMatrix_ = new MatrixTransformer.SvgMatrix();
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
        return svgMatrix_.getScaleX();
    }

    /**
     * Gets the <code>b</code> entry of the matrix.
     * @return the field
     */
    @JsxGetter
    public double getB() {
        return svgMatrix_.getShearY();
    }

    /**
     * Gets the <code>c</code> entry of the matrix.
     * @return the field
     */
    @JsxGetter
    public double getC() {
        return svgMatrix_.getShearX();
    }

    /**
     * Gets the <code>d</code> entry of the matrix.
     * @return the field
     */
    @JsxGetter
    public double getD() {
        return svgMatrix_.getScaleY();
    }

    /**
     * Gets the <code>e</code> entry of the matrix.
     * @return the field
     */
    @JsxGetter
    public double getE() {
        return svgMatrix_.getTranslateX();
    }

    /**
     * Gets the <code>f</code> entry of the matrix.
     * @return the field
     */
    @JsxGetter
    public double getF() {
        return svgMatrix_.getTranslateY();
    }

    /**
     * Sets the <code>a</code> entry of the matrix.
     * @param newValue the new value for the field
     */
    @JsxSetter
    public void setA(final double newValue) {
        svgMatrix_.setScaleX(newValue);
    }

    /**
     * Sets the <code>b</code> entry of the matrix.
     * @param newValue the new value for the field
     */
    @JsxSetter
    public void setB(final double newValue) {
        svgMatrix_.setShearY(newValue);
    }

    /**
     * Sets the <code>c</code> entry of the matrix.
     * @param newValue the new value for the field
     */
    @JsxSetter
    public void setC(final double newValue) {
        svgMatrix_.setShearX(newValue);
    }

    /**
     * Sets the <code>d</code> entry of the matrix.
     * @param newValue the new value for the field
     */
    @JsxSetter
    public void setD(final double newValue) {
        svgMatrix_.setScaleY(newValue);
    }

    /**
     * Sets the <code>e</code> entry of the matrix.
     * @param newValue the new value for the field
     */
    @JsxSetter
    public void setE(final double newValue) {
        svgMatrix_.setTranslateX(newValue);
    }

    /**
     * Sets the <code>f</code> entry of the matrix.
     * @param newValue the new value for the field
     */
    @JsxSetter
    public void setF(final double newValue) {
        svgMatrix_.setTranslateY(newValue);
    }

    /**
     * Transforms the matrix.
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix flipX() {
        final SVGMatrix result = new SVGMatrix(getWindow());
        result.svgMatrix_ = matrixTransformer_.flipX(svgMatrix_);

        return result;
    }

    /**
     * Transforms the matrix.
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix flipY() {
        final SVGMatrix result = new SVGMatrix(getWindow());
        result.svgMatrix_ = matrixTransformer_.flipY(svgMatrix_);

        return result;
    }

    /**
     * Transforms the matrix.
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix inverse() {
        try {
            final SVGMatrix result = new SVGMatrix(getWindow());
            result.svgMatrix_ = matrixTransformer_.inverse(svgMatrix_);

            return result;
        }
        catch (final IllegalArgumentException e) {
            throw ScriptRuntime.constructError("Error", e.getMessage());
        }
    }

    /**
     * Transforms the matrix.
     * @param by the matrix to multiply by
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix multiply(final SVGMatrix by) {
        final SVGMatrix result = new SVGMatrix(getWindow());
        result.svgMatrix_ = matrixTransformer_.multiply(svgMatrix_, by.svgMatrix_);
        return result;
    }

    /**
     * Rotates the matrix.
     * @param angle the rotation angle
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix rotate(final double angle) {
        final SVGMatrix result = new SVGMatrix(getWindow());
        result.svgMatrix_ = matrixTransformer_.rotate(svgMatrix_, angle);

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

        final SVGMatrix result = new SVGMatrix(getWindow());
        result.svgMatrix_ = matrixTransformer_.rotateFromVector(svgMatrix_, x, y);

        return result;
    }

    /**
     * Transforms the matrix.
     * @param factor the scale factor
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix scale(final double factor) {
        final SVGMatrix result = new SVGMatrix(getWindow());
        result.svgMatrix_ = matrixTransformer_.scale(svgMatrix_, factor);

        return result;
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
        result.svgMatrix_ = matrixTransformer_.scaleNonUniform(svgMatrix_, factorX, factorY);

        return result;
    }

    /**
     * Transforms the matrix.
     * @param angle the skew angle
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix skewX(final double angle) {
        final SVGMatrix result = new SVGMatrix(getWindow());
        result.svgMatrix_ = matrixTransformer_.skewX(svgMatrix_, angle);

        return result;
    }

    /**
     * Transforms the matrix.
     * @param angle the skew angle
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix skewY(final double angle) {
        final SVGMatrix result = new SVGMatrix(getWindow());
        result.svgMatrix_ = matrixTransformer_.skewY(svgMatrix_, angle);

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
        result.svgMatrix_ = matrixTransformer_.translate(svgMatrix_, x, y);

        return result;
    }
}
