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
package com.gargoylesoftware.htmlunit.javascript.host.media;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

/**
 * A JavaScript object for {@code TextTrack}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class TextTrack extends EventTarget {

    /** Constant for {@code NONE}. */
    @JsxConstant(@WebBrowser(IE))
    public static final int NONE = 0;
    /** Constant for {@code LOADING}. */
    @JsxConstant(@WebBrowser(IE))
    public static final int LOADING = 1;
    /** Constant for {@code LOADED}. */
    @JsxConstant(@WebBrowser(IE))
    public static final int LOADED = 2;
    /** Constant for {@code ERROR}. */
    @JsxConstant(@WebBrowser(IE))
    public static final int ERROR = 3;
    /** Constant for {@code DISABLED}. */
    @JsxConstant(@WebBrowser(IE))
    public static final int DISABLED = 0;
    /** Constant for {@code HIDDEN}. */
    @JsxConstant(@WebBrowser(IE))
    public static final int HIDDEN = 1;
    /** Constant for {@code SHOWING}. */
    @JsxConstant(@WebBrowser(IE))
    public static final int SHOWING = 2;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public TextTrack() {
    }

}
