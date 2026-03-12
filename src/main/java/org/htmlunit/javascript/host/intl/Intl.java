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
package org.htmlunit.javascript.host.intl;

import static org.htmlunit.BrowserVersionFeatures.JS_INTL_V8_BREAK_ITERATOR;

import java.lang.reflect.Method;
import java.util.Map;

import org.htmlunit.BrowserVersion;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import org.htmlunit.javascript.configuration.ClassConfiguration;
import org.htmlunit.javascript.configuration.JsxClass;

/**
 * A JavaScript object for {@code Intl}.
 *
 * @author Ahmed Ashour
 * @author Lai Quang Duong
 */
@JsxClass
public class Intl extends HtmlUnitScriptable {

    /**
     * Initialize the Intl object and register it on the global scope.
     * @param scope the top-level scope
     * @param globalThis the global object
     * @param browserVersion the browser version
     */
    public static void init(final Scriptable scope, final ScriptableObject globalThis,
            final BrowserVersion browserVersion) {
        final Intl intl = new Intl();
        intl.setParentScope(scope);
        intl.defineProperties(scope, browserVersion);

        // Configure static functions
        final ClassConfiguration intlConfig = AbstractJavaScriptConfiguration.getClassConfiguration(Intl.class, browserVersion);
        if (intlConfig != null) {
            defineStaticFunctions(intlConfig, intl, intl);
        }

        globalThis.defineProperty(intl.getClassName(), intl, ScriptableObject.DONTENUM);
    }

    private void defineProperties(final Scriptable scope, final BrowserVersion browserVersion) {
        define(scope, Collator.class, browserVersion);
        define(scope, DateTimeFormat.class, browserVersion);
        define(scope, Locale.class, browserVersion);
        define(scope, NumberFormat.class, browserVersion);
        if (browserVersion.hasFeature(JS_INTL_V8_BREAK_ITERATOR)) {
            define(scope, V8BreakIterator.class, browserVersion);
        }
    }

    private void define(final Scriptable scope, final Class<? extends HtmlUnitScriptable> c,
            final BrowserVersion browserVersion) {
        try {
            final ClassConfiguration config = AbstractJavaScriptConfiguration.getClassConfiguration(c, browserVersion);
            final HtmlUnitScriptable prototype = JavaScriptEngine.configureClass(config, scope);
            final FunctionObject constructorFn = new FunctionObject(config.getJsConstructor().getKey(),
                    config.getJsConstructor().getValue(), this);
            constructorFn.addAsConstructor(this, prototype, ScriptableObject.DONTENUM);

            defineStaticFunctions(config, this, constructorFn);
        }
        catch (final Exception e) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
        }
    }

    private static void defineStaticFunctions(final ClassConfiguration config,
            final Scriptable scope, final ScriptableObject target) {
        final Map<String, Method> staticFunctionMap = config.getStaticFunctionMap();
        if (staticFunctionMap != null) {
            for (final Map.Entry<String, Method> entry : staticFunctionMap.entrySet()) {
                final FunctionObject fn = new FunctionObject(entry.getKey(), entry.getValue(), scope);
                target.defineProperty(entry.getKey(), fn, ScriptableObject.EMPTY);
            }
        }
    }
}
