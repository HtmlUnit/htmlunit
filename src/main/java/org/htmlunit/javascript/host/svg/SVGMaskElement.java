/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.svg.SvgMask;

/**
 * A JavaScript object for {@code SVGMaskElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = SvgMask.class)
public class SVGMaskElement extends SVGElement {

    /** Constant for {@code SVG_MASKTYPE_LUMINANCE}. */
    @JsxConstant({FF, FF_ESR})
    public static final int SVG_MASKTYPE_LUMINANCE = 0;
    /** Constant for {@code SVG_MASKTYPE_ALPHA}. */
    @JsxConstant({FF, FF_ESR})
    public static final int SVG_MASKTYPE_ALPHA = 1;

    /**
     * Creates an instance.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }
}
