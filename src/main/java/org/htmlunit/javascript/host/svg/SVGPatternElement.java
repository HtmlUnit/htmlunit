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

import static org.htmlunit.javascript.configuration.SupportedBrowser.IE;

import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.svg.SvgPattern;

/**
 * A JavaScript object for {@code SVGPatternElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = SvgPattern.class)
public class SVGPatternElement extends SVGElement {

    /** Constant for {@code SVG_UNIT_TYPE_UNKNOWN}. */
    @JsxConstant(IE)
    public static final int SVG_UNIT_TYPE_UNKNOWN = 0;
    /** Constant for {@code SVG_UNIT_TYPE_USERSPACEONUSE}. */
    @JsxConstant(IE)
    public static final int SVG_UNIT_TYPE_USERSPACEONUSE = 1;
    /** Constant for {@code SVG_UNIT_TYPE_OBJECTBOUNDINGBOX}. */
    @JsxConstant(IE)
    public static final int SVG_UNIT_TYPE_OBJECTBOUNDINGBOX = 2;

    /**
     * Creates an instance.
     */
    public SVGPatternElement() {
    }

    /**
     * Creates an instance.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }
}
