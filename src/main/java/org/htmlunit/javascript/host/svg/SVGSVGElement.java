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

import org.htmlunit.html.HtmlSvg;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;

/**
 * A JavaScript object for {@code SVGSVGElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlSvg.class)
public class SVGSVGElement extends SVGGraphicsElement {

    /** The constant {@code SVG_ZOOMANDPAN_UNKNOWN}. */
    @JsxConstant
    public static final int SVG_ZOOMANDPAN_UNKNOWN = 0;
    /** The constant {@code SVG_ZOOMANDPAN_DISABLE}. */
    @JsxConstant
    public static final int SVG_ZOOMANDPAN_DISABLE = 1;
    /** The constant {@code SVG_ZOOMANDPAN_MAGNIFY}. */
    @JsxConstant
    public static final int SVG_ZOOMANDPAN_MAGNIFY = 2;

    /**
     * Creates an instance.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Creates a new {@link SVGMatrix}.
     * @return the new matrix
     */
    @JsxFunction
    public SVGMatrix createSVGMatrix() {
        return new SVGMatrix(getWindow());
    }

    /**
     * Creates a new {@link SVGMatrix}.
     * @return the new matrix
     */
    @JsxFunction
    public SVGMatrix getScreenCTM() {
        return new SVGMatrix(getWindow());
    }

    /**
     * Creates a new {@link SVGRect}.
     * @return the new rect
     */
    @JsxFunction
    public SVGRect createSVGRect() {
        final SVGRect rect = new SVGRect();
        rect.setPrototype(getPrototype(rect.getClass()));
        rect.setParentScope(getParentScope());
        return rect;
    }
}
