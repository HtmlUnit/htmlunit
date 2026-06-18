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

import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;

import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.svg.SvgTextPath;

/**
 * A JavaScript object for {@code SVGTextPathElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = SvgTextPath.class)
public class SVGTextPathElement extends SVGTextContentElement {

    /** The constant {@code TEXTPATH_METHODTYPE_UNKNOWN}. */
    @JsxConstant
    public static final int TEXTPATH_METHODTYPE_UNKNOWN = 0;

    /** The constant {@code TEXTPATH_SPACINGTYPE_UNKNOWN}. */
    @JsxConstant
    public static final int TEXTPATH_SPACINGTYPE_UNKNOWN = 0;

    /** The constant {@code TEXTPATH_SIDETYPE_UNKNOWN}. */
    @JsxConstant(FF)
    public static final int TEXTPATH_SIDETYPE_UNKNOWN = 0;

    /** The constant {@code TEXTPATH_METHODTYPE_ALIGN}. */
    @JsxConstant
    public static final int TEXTPATH_METHODTYPE_ALIGN = 1;

    /** The constant {@code TEXTPATH_SPACINGTYPE_AUTO}. */
    @JsxConstant
    public static final int TEXTPATH_SPACINGTYPE_AUTO = 1;

    /** The constant {@code TEXTPATH_SIDETYPE_LEFT}. */
    @JsxConstant(FF)
    public static final int TEXTPATH_SIDETYPE_LEFT = 1;

    /** The constant {@code TEXTPATH_METHODTYPE_STRETCH}. */
    @JsxConstant
    public static final int TEXTPATH_METHODTYPE_STRETCH = 2;

    /** The constant {@code TEXTPATH_SPACINGTYPE_EXACT}. */
    @JsxConstant
    public static final int TEXTPATH_SPACINGTYPE_EXACT = 2;

    /** The constant {@code TEXTPATH_SIDETYPE_RIGHT}. */
    @JsxConstant(FF)
    public static final int TEXTPATH_SIDETYPE_RIGHT = 2;

    /**
     * Creates an instance.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }
}
