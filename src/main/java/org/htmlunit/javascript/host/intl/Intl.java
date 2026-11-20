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
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.Symbol;
import org.htmlunit.corejs.javascript.SymbolKey;
import org.htmlunit.corejs.javascript.TopLevel;
import org.htmlunit.corejs.javascript.VarScope;
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
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl">MDN Documentation</a>
 */
@JsxClass
public class Intl extends HtmlUnitScriptable {

    /**
     * Initialize the Intl object and register it on the global scope.
     * @param scope the top-level scope
     * @param globalThis the global object
     * @param browserVersion the browser version
     */
    public static void init(final TopLevel scope, final ScriptableObject globalThis,
            final BrowserVersion browserVersion) {
        final Intl intl = new Intl();
        intl.setParentScope(scope);
        intl.defineProperty(SymbolKey.TO_STRING_TAG, "Intl", ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        intl.defineProperties(scope, browserVersion);

        // Configure static functions (getCanonicalLocales)
        final ClassConfiguration intlConfig =
                AbstractJavaScriptConfiguration.getClassConfiguration(Intl.class, browserVersion);
        if (intlConfig != null) {
            defineStaticFunctions(intlConfig, scope, intl);
        }

        globalThis.defineProperty(intl.getClassName(), intl, ScriptableObject.DONTENUM);
    }

    private void defineProperties(final TopLevel scope, final BrowserVersion browserVersion) {
        define(scope, Collator.class, browserVersion);
        define(scope, DateTimeFormat.class, browserVersion);
        define(scope, Locale.class, browserVersion);
        define(scope, NumberFormat.class, browserVersion);
        if (browserVersion.hasFeature(JS_INTL_V8_BREAK_ITERATOR)) {
            define(scope, V8BreakIterator.class, browserVersion);
        }
    }

    private void define(final TopLevel scope, final Class<? extends HtmlUnitScriptable> c,
            final BrowserVersion browserVersion) {
        try {
            final ClassConfiguration config = AbstractJavaScriptConfiguration.getClassConfiguration(c, browserVersion);
            final HtmlUnitScriptable prototype = JavaScriptEngine.configureClass(config, scope);
            final FunctionObject constructorFn = new FunctionObject(config.getJsConstructor().getKey(),
                    config.getJsConstructor().getValue(), scope);

            JavaScriptEngine.setFunctionProtoAndParent(constructorFn, Context.getCurrentContext(), scope);
            constructorFn.setImmunePrototypeProperty(prototype);
            prototype.setParentScope(scope);
            ScriptableObject.defineProperty(prototype, "constructor", constructorFn, ScriptableObject.DONTENUM);
            constructorFn.setParentScope(scope);

            ScriptableObject.defineProperty(this, prototype.getClassName(), constructorFn, ScriptableObject.DONTENUM);

            defineStaticFunctions(config, scope, constructorFn);
        }
        catch (final Exception e) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
        }
    }

    private static void defineStaticFunctions(final ClassConfiguration config,
            final VarScope scope, final ScriptableObject target) {
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
     * @param scope the scope
     * @param thisObj the scriptable this
     * @param args the arguments
     * @param funObj the function object
     * @return an array of canonical locale names
     *
     * @see <a href="https://tc39.es/ecma402/#sec-intl.getcanonicallocales">spec</a>
     */
    @JsxStaticFunction
    public static Object getCanonicalLocales(final Context cx, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function funObj) {
        final List<String> locales = canonicalizeLocaleList(cx, firstArgument(args));
        return cx.newArray(ScriptableObject.getTopLevelScope(scope), locales.toArray());
    }

    /**
     * Converts the locales argument of the Intl constructors and functions into a list of
     * canonicalized language tags without duplicates.
     * <p>
     * A string or an {@code Intl.Locale} is a list with one entry. Any other object is processed
     * like an array; every element has to be a string or an object (converted to a string),
     * otherwise a {@code TypeError} is thrown. All other primitives (number, boolean, symbol)
     * result in an empty list.
     * </p>
     *
     * @param cx the current context
     * @param locales the locales argument
     * @return the canonicalized language tags; empty if the locales argument is {@code undefined}
     * @throws org.htmlunit.corejs.javascript.EcmaError a {@code TypeError} if the locales argument
     *         is {@code null} or contains an invalid element, a {@code RangeError} if one of the language
     *         tags is not valid
     * @see <a href="https://tc39.es/ecma402/#sec-canonicalizelocalelist">spec</a>
     */
    static List<String> canonicalizeLocaleList(final Context cx, final Object locales) {
        final Set<String> languageTags = new LinkedHashSet<>();
        if (JavaScriptEngine.isUndefined(locales)) {
            return new ArrayList<>(languageTags);
        }
        if (locales == null) {
            throw JavaScriptEngine.typeError("Cannot convert null to object");
        }

        if (locales instanceof CharSequence || locales instanceof Locale) {
            languageTags.add(canonicalizeLanguageTag(toLanguageTag(locales)));
        }
        else if (locales instanceof Scriptable scriptable && !(locales instanceof Symbol)) {
            final int length = JavaScriptEngine.toArrayLength(JavaScriptEngine.lengthOfArrayLike(cx, scriptable));
            for (int i = 0; i < length; i++) {
                final Object element = ScriptableObject.getProperty(scriptable, i);
                if (element == Scriptable.NOT_FOUND) {
                    continue;
                }
                if (!(element instanceof CharSequence)
                        && (!(element instanceof Scriptable) || element instanceof Symbol)) {
                    throw JavaScriptEngine.typeError("Invalid element in locales argument");
                }
                languageTags.add(canonicalizeLanguageTag(toLanguageTag(element)));
            }
        }
        // the remaining primitives are converted to objects without a length

        return new ArrayList<>(languageTags);
    }

    /**
     * Returns the first argument; {@code undefined} if there is none.
     *
     * @param args the arguments
     * @return the first argument or {@code undefined}
     */
    static Object firstArgument(final Object[] args) {
        return args.length == 0 ? JavaScriptEngine.UNDEFINED : args[0];
    }

    private static String toLanguageTag(final Object locale) {
        if (locale instanceof Locale intlLocale) {
            return intlLocale.jsToString();
        }
        return JavaScriptEngine.toString(locale);
    }

    private static String canonicalizeLanguageTag(final String tag) {
        try {
            final java.util.Locale locale = new java.util.Locale.Builder().setLanguageTag(tag).build();
            // private use only tags (e.g. 'x-private') are not supported
            if (!locale.getLanguage().isEmpty()) {
                return locale.toLanguageTag();
            }
        }
        catch (final IllformedLocaleException e) {
            // handled below
        }
        throw JavaScriptEngine.rangeError("Invalid language tag: " + tag);
    }

    /**
     * Shared implementation of the {@code supportedLocalesOf} functions: the canonicalized
     * language tags of the locales argument that are available.
     *
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments; the first one is the locales argument
     * @return an array of the supported language tags
     */
    static Scriptable supportedLocalesOf(final Context cx, final VarScope scope, final Object[] args) {
        final List<String> locales = canonicalizeLocaleList(cx, firstArgument(args));

        final List<Object> supportedLocales = new ArrayList<>();
        for (final String locale : locales) {
            if (bestAvailableLocale(locale) != null) {
                supportedLocales.add(locale);
            }
        }

        return cx.newArray(ScriptableObject.getTopLevelScope(scope), supportedLocales.toArray());
    }

    /**
     * Returns the first of the given (canonicalized) language tags that is supported.
     *
     * @param locales the language tags
     * @return the first supported language tag or {@code null} if none is supported
     * @see <a href="https://tc39.es/ecma402/#sec-lookupmatcher">spec</a>
     */
    static String lookupSupportedLocale(final List<String> locales) {
        for (final String locale : locales) {
            if (bestAvailableLocale(locale) != null) {
                return locale;
            }
        }
        return null;
    }

    /**
     * Searches the available locales for the given language tag (without extensions);
     * if there is no match the subtags are removed from the end one by one.
     *
     * @param languageTag the canonicalized language tag
     * @return the language tag of the available locale or {@code null} if there is none
     * @see <a href="https://tc39.es/ecma402/#sec-bestavailablelocale">spec</a>
     */
    static String bestAvailableLocale(final String languageTag) {
        String candidate = java.util.Locale.forLanguageTag(languageTag).stripExtensions().toLanguageTag();
        while (true) {
            if (LocaleUtils.isAvailableLocale(java.util.Locale.forLanguageTag(candidate))) {
                return candidate;
            }
            final int pos = candidate.lastIndexOf('-');
            if (pos == -1) {
                return null;
            }
            candidate = candidate.substring(0, pos);
        }
    }
}
