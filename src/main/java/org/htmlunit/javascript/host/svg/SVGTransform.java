/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for {@code SVGTransform}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class SVGTransform extends HtmlUnitScriptable {

    /** The constant {@code SVG_TRANSFORM_UNKNOWN}. */
    @JsxConstant
    public static final int SVG_TRANSFORM_UNKNOWN = 0;
    /** The constant {@code SVG_TRANSFORM_MATRIX}. */
    @JsxConstant
    public static final int SVG_TRANSFORM_MATRIX = 1;
    /** The constant {@code SVG_TRANSFORM_TRANSLATE}. */
    @JsxConstant
    public static final int SVG_TRANSFORM_TRANSLATE = 2;
    /** The constant {@code SVG_TRANSFORM_SCALE}. */
    @JsxConstant
    public static final int SVG_TRANSFORM_SCALE = 3;
    /** The constant {@code SVG_TRANSFORM_ROTATE}. */
    @JsxConstant
    public static final int SVG_TRANSFORM_ROTATE = 4;
    /** The constant {@code SVG_TRANSFORM_SKEWX}. */
    @JsxConstant
    public static final int SVG_TRANSFORM_SKEWX = 5;
    /** The constant {@code SVG_TRANSFORM_SKEWY}. */
    @JsxConstant
    public static final int SVG_TRANSFORM_SKEWY = 6;

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }
}
