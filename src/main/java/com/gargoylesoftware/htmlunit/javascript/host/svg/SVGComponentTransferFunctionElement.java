/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for {@code SVGComponentTransferFunctionElement}.
 *
 * @author Ahmed Ashour
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
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public SVGComponentTransferFunctionElement() {
    }

}
