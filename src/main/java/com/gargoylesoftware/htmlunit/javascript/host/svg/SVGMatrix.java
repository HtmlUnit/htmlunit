/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.svg;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A JavaScript object for SVGMatrix.
 * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/SVGMatrix">MDN doc</a>
 * @version $Revision$
 * @author Marc Guillemot
 */
@JsxClass(browsers = { @WebBrowser(value = IE, minVersion = 9), @WebBrowser(FF), @WebBrowser(CHROME) })
public class SVGMatrix extends SimpleScriptable {
    private double fieldA_ = 1;
    private double fieldB_ = 0;
    private double fieldC_ = 0;
    private double fieldD_ = 1;
    private double fieldE_ = 0;
    private double fieldF_ = 0;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public SVGMatrix() {
    }

    /**
     * Instantiates and configure scope and prototype.
     * @param scope the parent scope
     */
    public SVGMatrix(final Window scope) {
        setParentScope(scope);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Gets the <code>a</code> entry of the matrix.
     * @return the field
     **/
    @JsxGetter
    public double getA() {
        return fieldA_;
    }

    /**
     * Gets the <code>b</code> entry of the matrix.
     * @return the field
     **/
    @JsxGetter
    public double getB() {
        return fieldB_;
    }

    /**
     * Gets the <code>c</code> entry of the matrix.
     * @return the field
     **/
    @JsxGetter
    public double getC() {
        return fieldC_;
    }

    /**
     * Gets the <code>d</code> entry of the matrix.
     * @return the field
     **/
    @JsxGetter
    public double getD() {
        return fieldD_;
    }

    /**
     * Gets the <code>e</code> entry of the matrix.
     * @return the field
     **/
    @JsxGetter
    public double getE() {
        return fieldE_;
    }

    /**
     * Gets the <code>f</code> entry of the matrix.
     * @return the field
     **/
    @JsxGetter
    public double getF() {
        return fieldF_;
    }

    /**
     * Sets the <code>a</code> entry of the matrix.
     * @param newValue the new value for the field
     **/
    @JsxSetter
    public void setA(final double newValue) {
        fieldA_ = newValue;
    }

    /**
     * Sets the <code>b</code> entry of the matrix.
     * @param newValue the new value for the field
     **/
    @JsxSetter
    public void setB(final double newValue) {
        fieldB_ = newValue;
    }

    /**
     * Sets the <code>c</code> entry of the matrix.
     * @param newValue the new value for the field
     **/
    @JsxSetter
    public void setC(final double newValue) {
        fieldC_ = newValue;
    }

    /**
     * Sets the <code>d</code> entry of the matrix.
     * @param newValue the new value for the field
     **/
    @JsxSetter
    public void setD(final double newValue) {
        fieldD_ = newValue;
    }

    /**
     * Sets the <code>e</code> entry of the matrix.
     * @param newValue the new value for the field
     **/
    @JsxSetter
    public void setE(final double newValue) {
        fieldE_ = newValue;
    }

    /**
     * Sets the <code>f</code> entry of the matrix.
     * @param newValue the new value for the field
     **/
    @JsxSetter
    public void setF(final double newValue) {
        fieldF_ = newValue;
    }

    /**
     * Transforms the matrix.
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix flipX() {
        return new SVGMatrix(getWindow()); // TODO: this is wrong, compute it!
    }

    /**
     * Transforms the matrix.
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix flipY() {
        return new SVGMatrix(getWindow()); // TODO: this is wrong, compute it!
    }

    /**
     * Transforms the matrix.
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix inverse() {
        return new SVGMatrix(getWindow()); // TODO: this is wrong, compute it!
    }

    /**
     * Transforms the matrix.
     * @param by the matrix to multiply by
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix multiply(final SVGMatrix by) {
        return new SVGMatrix(getWindow()); // TODO: this is wrong, compute it!
    }

    /**
     * Rotates the matrix.
     * @param angle the rotation angle
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix rotate(final double angle) {
        return new SVGMatrix(getWindow()); // TODO: this is wrong, compute it!
    }

    /**
     * Transforms the matrix.
     * @param x the x-coordinate of the vector
     * @param y the y-coordinate of the vector
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix rotateFromVector(final double x, final double y) {
        return new SVGMatrix(getWindow()); // TODO: this is wrong, compute it!
    }

    /**
     * Transforms the matrix.
     * @param factor the scale factor
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix scale(final double factor) {
        return new SVGMatrix(getWindow()); // TODO: this is wrong, compute it!
    }

    /**
     * Transforms the matrix.
     * @param factorX the factor for the x-axis
     * @param factorY the factor for the y-axis
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix scaleNonUniform(final double factorX, final double factorY) {
        return new SVGMatrix(getWindow()); // TODO: this is wrong, compute it!
    }

    /**
     * Transforms the matrix.
     * @param angle the skew angle
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix skewX(final double angle) {
        return new SVGMatrix(getWindow()); // TODO: this is wrong, compute it!
    }

    /**
     * Transforms the matrix.
     * @param angle the skew angle
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix skewY(final double angle) {
        return new SVGMatrix(getWindow()); // TODO: this is wrong, compute it!
    }

    /**
     * Translates the matrix.
     * @param x the distance along the x-axis
     * @param y the distance along the y-axis
     * @return the resulting matrix
     */
    @JsxFunction
    public SVGMatrix translate(final double x, final double y) {
        return new SVGMatrix(getWindow()); // TODO: this is wrong, compute it!
    }
}
