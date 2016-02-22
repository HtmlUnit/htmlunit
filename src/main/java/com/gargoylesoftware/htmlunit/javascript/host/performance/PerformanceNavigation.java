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
package com.gargoylesoftware.htmlunit.javascript.host.performance;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for {@code PerformanceNavigation}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class PerformanceNavigation extends SimpleScriptable {

    /** Navigate. */
    @JsxConstant
    public static final int TYPE_NAVIGATE = 0;

    /** Reload. */
    @JsxConstant
    public static final int TYPE_RELOAD = 1;

    /** Back forward. */
    @JsxConstant
    public static final int TYPE_BACK_FORWARD = 2;

    /** Reserved. */
    @JsxConstant
    public static final int TYPE_RESERVED = 255;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public PerformanceNavigation() {
    }

    /**
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @JsxGetter
    public int getType() {
        return TYPE_NAVIGATE;
    }

    /**
     * Returns the {@code redirectCount} property.
     * @return the {@code redirectCount} property
     */
    @JsxGetter
    public int getRedirectCount() {
        return 0;
    }

    /**
     * The {@code toJSON} function.
     * @return the {@code toJSON} object
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(IE) })
    public Object toJSON() {
        throw new UnsupportedOperationException();
    }

}
