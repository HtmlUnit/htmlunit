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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Element;

/**
 * A JavaScript object for a CSSPrimitiveValue.
 *
 * @see org.w3c.dom.css.CSSPrimitiveValue
 * @version $Revision$
 * @author Marc Guillemot
 */
@JsxClass(browsers = @WebBrowser(FF))
public class CSSPrimitiveValue extends CSSValue {

    /**
     * The value is not a recognized CSS2 value. The value can only be
     * obtained by using the <code>cssText</code> attribute.
     */
    @JsxConstant
    public static final short CSS_UNKNOWN = org.w3c.dom.css.CSSPrimitiveValue.CSS_UNKNOWN;

    /**
     * The value is a simple number. The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_NUMBER = org.w3c.dom.css.CSSPrimitiveValue.CSS_NUMBER;

    /**
     * The value is a percentage. The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_PERCENTAGE = org.w3c.dom.css.CSSPrimitiveValue.CSS_PERCENTAGE;

    /**
     * The value is a length (ems). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_EMS = org.w3c.dom.css.CSSPrimitiveValue.CSS_EMS;

    /**
     * The value is a length (exs). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_EXS = org.w3c.dom.css.CSSPrimitiveValue.CSS_EXS;

    /**
     * The value is a length (px). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_PX = org.w3c.dom.css.CSSPrimitiveValue.CSS_PX;

    /**
     * The value is a length (cm). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_CM = org.w3c.dom.css.CSSPrimitiveValue.CSS_CM;

    /**
     * The value is a length (mm). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_MM = org.w3c.dom.css.CSSPrimitiveValue.CSS_MM;

    /**
     * The value is a length (in). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_IN = org.w3c.dom.css.CSSPrimitiveValue.CSS_IN;

    /**
     * The value is a length (pt). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_PT = org.w3c.dom.css.CSSPrimitiveValue.CSS_PT;

    /**
     * The value is a length (pc). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_PC = org.w3c.dom.css.CSSPrimitiveValue.CSS_PC;

    /**
     * The value is an angle (deg). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_DEG = org.w3c.dom.css.CSSPrimitiveValue.CSS_DEG;

    /**
     * The value is an angle (rad). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_RAD = org.w3c.dom.css.CSSPrimitiveValue.CSS_RAD;

    /**
     * The value is an angle (grad). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_GRAD = org.w3c.dom.css.CSSPrimitiveValue.CSS_GRAD;

    /**
     * The value is a time (ms). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_MS = org.w3c.dom.css.CSSPrimitiveValue.CSS_MS;

    /**
     * The value is a time (s). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_S = org.w3c.dom.css.CSSPrimitiveValue.CSS_S;

    /**
     * The value is a frequency (Hz). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_HZ = org.w3c.dom.css.CSSPrimitiveValue.CSS_HZ;

    /**
     * The value is a frequency (kHz). The value can be obtained by using the
     * <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_KHZ = org.w3c.dom.css.CSSPrimitiveValue.CSS_KHZ;

    /**
     * The value is a number with an unknown dimension. The value can be
     * obtained by using the <code>getFloatValue</code> method.
     */
    @JsxConstant
    public static final short CSS_DIMENSION = org.w3c.dom.css.CSSPrimitiveValue.CSS_DIMENSION;

    /**
     * The value is a STRING. The value can be obtained by using the
     * <code>getStringValue</code> method.
     */
    @JsxConstant
    public static final short CSS_STRING = org.w3c.dom.css.CSSPrimitiveValue.CSS_STRING;

    /**
     * The value is a URI. The value can be obtained by using the
     * <code>getStringValue</code> method.
     */
    @JsxConstant
    public static final short CSS_URI = org.w3c.dom.css.CSSPrimitiveValue.CSS_URI;

    /**
     * The value is an identifier. The value can be obtained by using the
     * <code>getStringValue</code> method.
     */
    @JsxConstant
    public static final short CSS_IDENT = org.w3c.dom.css.CSSPrimitiveValue.CSS_IDENT;

    /**
     * The value is a attribute function. The value can be obtained by using
     * the <code>getStringValue</code> method.
     */
    @JsxConstant
    public static final short CSS_ATTR = org.w3c.dom.css.CSSPrimitiveValue.CSS_ATTR;

    /**
     * The value is a counter or counters function. The value can be obtained
     * by using the <code>getCounterValue</code> method.
     */
    @JsxConstant
    public static final short CSS_COUNTER = org.w3c.dom.css.CSSPrimitiveValue.CSS_COUNTER;

    /**
     * The value is a rect function. The value can be obtained by using the
     * <code>getRectValue</code> method.
     */
    @JsxConstant
    public static final short CSS_RECT = org.w3c.dom.css.CSSPrimitiveValue.CSS_RECT;

    /**
     * The value is a RGB color. The value can be obtained by using the
     * <code>getRGBColorValue</code> method.
     */
    @JsxConstant
    public static final short CSS_RGBCOLOR = org.w3c.dom.css.CSSPrimitiveValue.CSS_RGBCOLOR;

    private org.w3c.dom.css.CSSPrimitiveValue wrappedCssPrimitiveValue_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor to instantiate prototype.
     */
    public CSSPrimitiveValue() {
        // Empty.
    }

    /**
     * Creates an instance and sets its parent scope to the one of the provided element.
     * @param element the element to which this style is bound
     */
    CSSPrimitiveValue(final Element element, final org.w3c.dom.css.CSSPrimitiveValue cssValue) {
        super(element, cssValue);
        setParentScope(element.getParentScope());
        setPrototype(getPrototype(getClass()));
        setDomNode(element.getDomNodeOrNull(), false);
        wrappedCssPrimitiveValue_ = cssValue;
    }

    /**
     * Gets the float value in the specified unit.
     * @param unitType the type of unit
     * @return the value
     */
    @JsxFunction
    public double getFloatValue(final int unitType) {
        return wrappedCssPrimitiveValue_.getFloatValue((short) unitType);
    }
}
