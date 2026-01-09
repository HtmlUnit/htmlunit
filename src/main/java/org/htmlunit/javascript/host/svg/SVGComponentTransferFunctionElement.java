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

/**
 * A JavaScript object for {@code SVGComponentTransferFunctionElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class SVGComponentTransferFunctionElement extends SVGElement {

    /** Constant for {@code SVG_FECOMPONENTTRANSFER_TYPE_UNKNOWN}. */
    @JsxConstant
    public static final int SVG_FECOMPONENTTRANSFER_TYPE_UNKNOWN = 0;
    /** Constant for {@code SVG_FECOMPONENTTRANSFER_TYPE_IDENTITY}. */
    @JsxConstant
    public static final int SVG_FECOMPONENTTRANSFER_TYPE_IDENTITY = 1;
    /** Constant for {@code SVG_FECOMPONENTTRANSFER_TYPE_TABLE}. */
    @JsxConstant
    public static final int SVG_FECOMPONENTTRANSFER_TYPE_TABLE = 2;
    /** Constant for {@code SVG_FECOMPONENTTRANSFER_TYPE_DISCRETE}. */
    @JsxConstant
    public static final int SVG_FECOMPONENTTRANSFER_TYPE_DISCRETE = 3;
    /** Constant for {@code SVG_FECOMPONENTTRANSFER_TYPE_LINEAR}. */
    @JsxConstant
    public static final int SVG_FECOMPONENTTRANSFER_TYPE_LINEAR = 4;
    /** Constant for {@code SVG_FECOMPONENTTRANSFER_TYPE_GAMMA}. */
    @JsxConstant
    public static final int SVG_FECOMPONENTTRANSFER_TYPE_GAMMA = 5;

    /**
     * Creates an instance.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }
}
