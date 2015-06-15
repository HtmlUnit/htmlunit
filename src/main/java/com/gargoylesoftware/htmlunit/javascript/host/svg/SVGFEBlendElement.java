/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.svg.SvgFeBlend;

/**
 * A JavaScript object for SVGFEBlendElement.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(domClass = SvgFeBlend.class,
    browsers = { @WebBrowser(value = IE, minVersion = 11), @WebBrowser(FF), @WebBrowser(CHROME) })
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

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public SVGFEBlendElement() {
    }
}
