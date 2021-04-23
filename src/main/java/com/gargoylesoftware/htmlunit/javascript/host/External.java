/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_IS_SEARCH_PROVIDER_INSTALLED_ZERO;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;

import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code External}.
 *
 * @author Peter Faller
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@JsxClass({CHROME, EDGE})
@JsxClass(isJSObject = false)
public class External extends SimpleScriptable {

    /**
     * The constructor.
     */
    @JsxConstructor
    public External() {
    }

    /**
     * Empty implementation.
     */
    @JsxFunction(value = IE, functionName = "AutoCompleteSaveForm")
    public void autoCompleteSaveForm() {
        // dummy
    }

    /**
     * Empty implementation.
     */
    @JsxFunction(functionName = "AddSearchProvider")
    public void addSearchProvider() {
        // dummy
    }

    /**
     * Empty implementation.
     * @return 0
     */
    @JsxFunction(functionName = "IsSearchProviderInstalled")
    public Object isSearchProviderInstalled() {
        if (getBrowserVersion().hasFeature(JS_IS_SEARCH_PROVIDER_INSTALLED_ZERO)) {
            return 0;
        }
        return Undefined.instance;
    }
}
