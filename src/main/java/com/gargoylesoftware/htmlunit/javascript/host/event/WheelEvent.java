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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for {@code WheelEvent}.
 *
 * @author Ahmed Ashour
 */
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE),
        @WebBrowser(EDGE) })
public class WheelEvent extends MouseEvent {

    /** Constant for {@code DOM_DELTA_PIXEL}. */
    @JsxConstant
    public static final int DOM_DELTA_PIXEL = 0;
    /** Constant for {@code DOM_DELTA_LINE}. */
    @JsxConstant
    public static final int DOM_DELTA_LINE = 1;
    /** Constant for {@code DOM_DELTA_PAGE}. */
    @JsxConstant
    public static final int DOM_DELTA_PAGE = 2;

    /** Constant for {@code MOZ_SOURCE_UNKNOWN}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOZ_SOURCE_UNKNOWN = 0;
    /** Constant for {@code MOZ_SOURCE_MOUSE}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOZ_SOURCE_MOUSE = 1;
    /** Constant for {@code MOZ_SOURCE_PEN}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOZ_SOURCE_PEN = 2;
    /** Constant for {@code MOZ_SOURCE_ERASER}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOZ_SOURCE_ERASER = 3;
    /** Constant for {@code MOZ_SOURCE_CURSOR}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOZ_SOURCE_CURSOR = 4;
    /** Constant for {@code MOZ_SOURCE_TOUCH}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOZ_SOURCE_TOUCH = 5;
    /** Constant for {@code MOZ_SOURCE_KEYBOARD}. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOZ_SOURCE_KEYBOARD = 6;

    /**
     * Default constructor.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public WheelEvent() {
    }
}
