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
 * A JavaScript object for {@code SVGPreserveAspectRatio}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class SVGPreserveAspectRatio extends SimpleScriptable {

    /** The constant {@code SVG_MEETORSLICE_UNKNOWN}. */
    @JsxConstant
    public static final int SVG_MEETORSLICE_UNKNOWN = 0;
    /** The constant {@code SVG_PRESERVEASPECTRATIO_UNKNOWN}. */
    @JsxConstant
    public static final int SVG_PRESERVEASPECTRATIO_UNKNOWN = 0;
    /** The constant {@code SVG_MEETORSLICE_MEET}. */
    @JsxConstant
    public static final int SVG_MEETORSLICE_MEET = 1;
    /** The constant {@code SVG_PRESERVEASPECTRATIO_NONE}. */
    @JsxConstant
    public static final int SVG_PRESERVEASPECTRATIO_NONE = 1;
    /** The constant {@code SVG_MEETORSLICE_SLICE}. */
    @JsxConstant
    public static final int SVG_MEETORSLICE_SLICE = 2;
    /** The constant {@code SVG_PRESERVEASPECTRATIO_XMINYMIN}. */
    @JsxConstant
    public static final int SVG_PRESERVEASPECTRATIO_XMINYMIN = 2;
    /** The constant {@code SVG_PRESERVEASPECTRATIO_XMIDYMIN}. */
    @JsxConstant
    public static final int SVG_PRESERVEASPECTRATIO_XMIDYMIN = 3;
    /** The constant {@code SVG_PRESERVEASPECTRATIO_XMAXYMIN}. */
    @JsxConstant
    public static final int SVG_PRESERVEASPECTRATIO_XMAXYMIN = 4;
    /** The constant {@code SVG_PRESERVEASPECTRATIO_XMINYMID}. */
    @JsxConstant
    public static final int SVG_PRESERVEASPECTRATIO_XMINYMID = 5;
    /** The constant {@code SVG_PRESERVEASPECTRATIO_XMIDYMID}. */
    @JsxConstant
    public static final int SVG_PRESERVEASPECTRATIO_XMIDYMID = 6;
    /** The constant {@code SVG_PRESERVEASPECTRATIO_XMAXYMID}. */
    @JsxConstant
    public static final int SVG_PRESERVEASPECTRATIO_XMAXYMID = 7;
    /** The constant {@code SVG_PRESERVEASPECTRATIO_XMINYMAX}. */
    @JsxConstant
    public static final int SVG_PRESERVEASPECTRATIO_XMINYMAX = 8;
    /** The constant {@code SVG_PRESERVEASPECTRATIO_XMIDYMAX}. */
    @JsxConstant
    public static final int SVG_PRESERVEASPECTRATIO_XMIDYMAX = 9;
    /** The constant {@code SVG_PRESERVEASPECTRATIO_XMAXYMAX}. */
    @JsxConstant
    public static final int SVG_PRESERVEASPECTRATIO_XMAXYMAX = 10;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public SVGPreserveAspectRatio() {
    }
}
