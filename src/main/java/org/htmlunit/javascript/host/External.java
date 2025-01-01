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
package org.htmlunit.javascript.host;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;

/**
 * A JavaScript object for {@code External}.
 *
 * @author Peter Faller
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@JsxClass({CHROME, EDGE})
@JsxClass(isJSObject = false)
public class External extends HtmlUnitScriptable {

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
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
        return JavaScriptEngine.UNDEFINED;
    }
}
