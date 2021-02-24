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
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for {@code SVGLength}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class SVGLength extends SimpleScriptable {

    /** The constant {@code SVG_LENGTHTYPE_UNKNOWN}. */
    @JsxConstant
    public static final int SVG_LENGTHTYPE_UNKNOWN = 0;
    /** The constant {@code SVG_LENGTHTYPE_NUMBER}. */
    @JsxConstant
    public static final int SVG_LENGTHTYPE_NUMBER = 1;
    /** The constant {@code SVG_LENGTHTYPE_PERCENTAGE}. */
    @JsxConstant
    public static final int SVG_LENGTHTYPE_PERCENTAGE = 2;
    /** The constant {@code SVG_LENGTHTYPE_EMS}. */
    @JsxConstant
    public static final int SVG_LENGTHTYPE_EMS = 3;
    /** The constant {@code SVG_LENGTHTYPE_EXS}. */
    @JsxConstant
    public static final int SVG_LENGTHTYPE_EXS = 4;
    /** The constant {@code SVG_LENGTHTYPE_PX}. */
    @JsxConstant
    public static final int SVG_LENGTHTYPE_PX = 5;
    /** The constant {@code SVG_LENGTHTYPE_CM}. */
    @JsxConstant
    public static final int SVG_LENGTHTYPE_CM = 6;
    /** The constant {@code SVG_LENGTHTYPE_MM}. */
    @JsxConstant
    public static final int SVG_LENGTHTYPE_MM = 7;
    /** The constant {@code SVG_LENGTHTYPE_IN}. */
    @JsxConstant
    public static final int SVG_LENGTHTYPE_IN = 8;
    /** The constant {@code SVG_LENGTHTYPE_PT}. */
    @JsxConstant
    public static final int SVG_LENGTHTYPE_PT = 9;
    /** The constant {@code SVG_LENGTHTYPE_PC}. */
    @JsxConstant
    public static final int SVG_LENGTHTYPE_PC = 10;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public SVGLength() {
    }
}
