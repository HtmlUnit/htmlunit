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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.svg.SvgAltGlyph;

/**
 * A JavaScript object for {@code SVGAltGlyphElement}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClass(domClass = SvgAltGlyph.class, browsers = @WebBrowser(FF))
public class SVGAltGlyphElement extends SVGElement {

    /** Constant for {@code LENGTHADJUST_UNKNOWN}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int LENGTHADJUST_UNKNOWN = 0;
    /** Constant for {@code LENGTHADJUST_SPACING}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int LENGTHADJUST_SPACING = 1;
    /** Constant for {@code LENGTHADJUST_SPACINGANDGLYPH}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int LENGTHADJUST_SPACINGANDGLYPHS = 2;

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public SVGAltGlyphElement() {
    }
}
