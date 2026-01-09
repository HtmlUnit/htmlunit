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
package org.htmlunit.javascript.host.svg;

import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.svg.SvgFeBlend;

/**
 * A JavaScript object for {@code SVGFEBlendElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = SvgFeBlend.class)
public class SVGFEBlendElement extends SVGElement {

    /** The constant {@code SVG_FEBLEND_MODE_UNKNOWN}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_UNKNOWN = 0;
    /** The constant {@code SVG_FEBLEND_MODE_NORMAL}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_NORMAL = 1;
    /** The constant {@code SVG_FEBLEND_MODE_MULTIPLY}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_MULTIPLY = 2;
    /** The constant {@code SVG_FEBLEND_MODE_SCREEN}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_SCREEN = 3;
    /** The constant {@code SVG_FEBLEND_MODE_DARKEN}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_DARKEN = 4;
    /** The constant {@code SVG_FEBLEND_MODE_LIGHTEN}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_LIGHTEN = 5;
    /** The constant {@code SVG_FEBLEND_MODE_OVERLAY}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_OVERLAY = 6;
    /** The constant {@code SVG_FEBLEND_MODE_COLOR_DODGE}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_COLOR_DODGE = 7;
    /** The constant {@code SVG_FEBLEND_MODE_COLOR_BURN}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_COLOR_BURN = 8;
    /** The constant {@code SVG_FEBLEND_MODE_HARD_LIGHT}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_HARD_LIGHT = 9;
    /** The constant {@code SVG_FEBLEND_MODE_SOFT_LIGHT}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_SOFT_LIGHT = 10;
    /** The constant {@code SVG_FEBLEND_MODE_DIFFERENCE}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_DIFFERENCE = 11;
    /** The constant {@code SVG_FEBLEND_MODE_EXCLUSION}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_EXCLUSION = 12;
    /** The constant {@code SVG_FEBLEND_MODE_HUE}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_HUE = 13;
    /** The constant {@code SVG_FEBLEND_MODE_SATURATION}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_SATURATION = 14;
    /** The constant {@code SVG_FEBLEND_MODE_COLOR}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_COLOR = 15;
    /** The constant {@code SVG_FEBLEND_MODE_LUMINOSITY}. */
    @JsxConstant
    public static final int SVG_FEBLEND_MODE_LUMINOSITY = 16;

    /**
     * Creates an instance.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }
}
