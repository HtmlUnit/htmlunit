/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for SVGAngle.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@JsxClass(browsers = { @WebBrowser(value = IE, minVersion = 11), @WebBrowser(FF), @WebBrowser(CHROME) })
public class SVGAngle extends SimpleScriptable {

    /** Invalid unit type. */
    @JsxConstant
    public static final short SVG_ANGLETYPE_UNKNOWN = 0;

    /** Unspecified unit type. */
    @JsxConstant
    public static final short SVG_ANGLETYPE_UNSPECIFIED = 1;

    /** Degree unit type. */
    @JsxConstant
    public static final short SVG_ANGLETYPE_DEG = 2;

    /** Radian unit type. */
    @JsxConstant
    public static final short SVG_ANGLETYPE_RAD = 3;

    /** Grad unit type. */
    @JsxConstant
    public static final short SVG_ANGLETYPE_GRAD = 4;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public SVGAngle() {
    }
}
