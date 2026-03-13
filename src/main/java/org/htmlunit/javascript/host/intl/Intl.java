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
import java.util.ArrayList;
import java.util.IllformedLocaleException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.LocaleUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.NativeArray;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.SymbolKey;
import org.htmlunit.corejs.javascript.TopLevel;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import org.htmlunit.javascript.configuration.ClassConfiguration;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxStaticFunction;

/**
 * A JavaScript object for Intl.
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
        intl.defineProperty(SymbolKey.TO_STRING_TAG, "Intl", ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        intl.defineProperties(scope, browserVersion);

        // Configure static functions (getCanonicalLocales)
        final ClassConfiguration intlConfig =
                AbstractJavaScriptConfiguration.getClassConfiguration(Intl.class, browserVersion);
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
                target.defineProperty(entry.getKey(), fn, ScriptableObject.DONTENUM);
            }
        }
    }

    /**
     * Returns an array containing the canonical locale names.
     * Duplicates will be omitted and elements will be validated as structurally valid language tags.
     *
     * @param cx the current context
     * @param thisObj the scriptable this
     * @param args the arguments
     * @param funObj the function object
     * @return an array of canonical locale names
     *
     * @see <a href="https://tc39.es/ecma402/#sec-intl.getcanonicallocales">spec</a>
     */
    @JsxStaticFunction
    public static Object getCanonicalLocales(final Context cx, final Scriptable thisObj,
            final Object[] args, final Function funObj) {
        if (args.length == 0 || JavaScriptEngine.isUndefined(args[0])) {
            return cx.newArray(TopLevel.getTopLevelScope(thisObj), new Object[0]);
        }

        final Object localesArgument = args[0];
        if (localesArgument == null) {
            throw JavaScriptEngine.typeError("Cannot convert null to object");
        }

        final List<String> languageTags = new ArrayList<>();
        if (localesArgument instanceof String s) {
            languageTags.add(s);
        }
        else if (localesArgument instanceof Scriptable scriptable) {
            if (JavaScriptEngine.isArrayLike(scriptable)) {
                final long len = JavaScriptEngine.lengthOfArrayLike(cx, scriptable);
                for (int i = 0; i < len; i++) {
                    final Object elem = scriptable.get(i, scriptable);
                    if (elem instanceof String s) {
                        languageTags.add(s);
                    }
                    else if (elem instanceof ScriptableObject) {
                        languageTags.add(JavaScriptEngine.toString(elem));
                    }
                    else {
                        throw JavaScriptEngine.typeError("Invalid element in locales argument");
                    }
                }
            }
            else {
                languageTags.add(JavaScriptEngine.toString(localesArgument));
            }
        }

        final Set<String> canonicalLocales = new LinkedHashSet<>(languageTags.size());
        for (final String tag : languageTags) {
            try {
                canonicalLocales.add(
                        new java.util.Locale.Builder().setLanguageTag(tag).build().toLanguageTag());
            }
            catch (final IllformedLocaleException e) {
                throw JavaScriptEngine.rangeError("Invalid language tag: '" + tag + "'");
            }
        }

        return cx.newArray(TopLevel.getTopLevelScope(thisObj), canonicalLocales.toArray());
    }

    /**
     * Shared utility for {@code supportedLocalesOf} implementations.
     * @param localesArgument the locales argument
     * @return a Scriptable array of supported locale strings
     */
    static Scriptable supportedLocalesOf(final Scriptable localesArgument) {
        final String[] locales;
        if (localesArgument instanceof NativeArray array) {
            locales = new String[(int) array.getLength()];
            for (int i = 0; i < locales.length; i++) {
                locales[i] = JavaScriptEngine.toString(array.get(i));
            }
        }
        else {
            locales = new String[] {JavaScriptEngine.toString(localesArgument)};
        }

        final List<String> supportedLocales = new ArrayList<>();
        for (final String locale : locales) {
            if (locale.isEmpty()) {
                throw JavaScriptEngine.rangeError("Invalid language tag: '" + locale + "'");
            }
            final java.util.Locale l = java.util.Locale.forLanguageTag(locale);
            if (LocaleUtils.isAvailableLocale(l)) {
                supportedLocales.add(locale);
            }
        }

        return Context.getCurrentContext().newArray(
                TopLevel.getTopLevelScope(localesArgument), supportedLocales.toArray());
    }
}
