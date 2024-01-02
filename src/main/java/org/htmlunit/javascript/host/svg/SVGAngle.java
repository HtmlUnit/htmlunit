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

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for {@code SVGAngle}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@JsxClass
public class SVGAngle extends HtmlUnitScriptable {

    /** Invalid unit type. */
    @JsxConstant
    public static final int SVG_ANGLETYPE_UNKNOWN = 0;

    /** Unspecified unit type. */
    @JsxConstant
    public static final int SVG_ANGLETYPE_UNSPECIFIED = 1;

    /** Degree unit type. */
    @JsxConstant
    public static final int SVG_ANGLETYPE_DEG = 2;

    /** Radian unit type. */
    @JsxConstant
    public static final int SVG_ANGLETYPE_RAD = 3;

    /** Grad unit type. */
    @JsxConstant
    public static final int SVG_ANGLETYPE_GRAD = 4;

    /**
     * Creates an instance.
     */
    public SVGAngle() {
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public void jsConstructor() {
    }
}
