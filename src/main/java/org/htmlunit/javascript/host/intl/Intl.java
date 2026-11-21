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
import java.util.Arrays;
import java.util.HashMap;
import java.util.IllformedLocaleException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
            makeNotEnumerable(prototype, config);
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

    /**
     * The accessor properties and the methods of the ECMAScript built-in objects are
     * not enumerable (unlike the attributes and operations of the Web IDL interfaces).
     *
     * @param prototype the prototype
     * @param config the class configuration
     */
    private static void makeNotEnumerable(final ScriptableObject prototype, final ClassConfiguration config) {
        final Map<String, ClassConfiguration.PropertyInfo> propertyMap = config.getPropertyMap();
        if (propertyMap != null) {
            for (final String name : propertyMap.keySet()) {
                prototype.setAttributes(name, ScriptableObject.DONTENUM);
            }
        }
        final Map<String, Method> functionMap = config.getFunctionMap();
        if (functionMap != null) {
            for (final String name : functionMap.keySet()) {
                prototype.setAttributes(name, ScriptableObject.DONTENUM);
            }
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
    private static String bestAvailableLocale(final String languageTag) {
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

    /**
     * The result of the locale resolution: the resolved language tag and the values of the
     * relevant unicode extension keys (from the locale or the options).
     *
     * @param locale the resolved language tag
     * @param values the values of the relevant extension keys; keys without a value are missing
     */
    record ResolvedLocale(String locale, Map<String, String> values) {
    }

    /**
     * Resolves the locale of an Intl object: the available locale for the requested one
     * plus the given (relevant) unicode extension keys of the requested locale, if their
     * values are supported.
     *
     * @param requestedLocale the requested (canonicalized) language tag
     * @param relevantExtensionKeys the unicode extension keys relevant for the Intl object
     *        (e.g. 'ca', 'hc' and 'nu' for a date time format)
     * @return the resolved language tag
     * @see <a href="https://tc39.es/ecma402/#sec-resolvelocale">spec</a>
     */
    static String resolveLocale(final String requestedLocale, final String... relevantExtensionKeys) {
        return resolveLocale(requestedLocale, new HashMap<>(), relevantExtensionKeys).locale();
    }

    /**
     * Resolves the locale of an Intl object: the available locale for the requested one
     * plus the given (relevant) unicode extension keys of the requested locale, if their
     * values are supported.
     * <p>
     * A supported value from the options overrides the value of the extension; if the value
     * differs, the key is removed from the resolved locale.
     * </p>
     *
     * @param requestedLocale the requested (canonicalized) language tag
     * @param optionValues the values of the extension keys given by the options (e.g. 'kn' for the
     *        option 'numeric'); keys without a value are missing
     * @param relevantExtensionKeys the unicode extension keys relevant for the Intl object
     *        (e.g. 'co', 'kf' and 'kn' for a collator)
     * @return the resolved locale
     * @see <a href="https://tc39.es/ecma402/#sec-resolvelocale">spec</a>
     */
    static ResolvedLocale resolveLocale(final String requestedLocale, final Map<String, String> optionValues,
            final String... relevantExtensionKeys) {
        final java.util.Locale locale = java.util.Locale.forLanguageTag(requestedLocale);
        String availableLocale = bestAvailableLocale(requestedLocale);
        if (availableLocale == null) {
            availableLocale = locale.stripExtensions().toLanguageTag();
        }
        final String language = java.util.Locale.forLanguageTag(availableLocale).getLanguage();

        final Map<String, String> values = new HashMap<>();
        // the keys are sorted like in the canonicalized tag
        final StringBuilder extension = new StringBuilder();
        for (final String key : new TreeSet<>(Arrays.asList(relevantExtensionKeys))) {
            String value = null;
            boolean partOfTag = false;

            String extensionValue = locale.getUnicodeLocaleType(key);
            if (extensionValue != null) {
                // a key without a value means 'true'
                if (extensionValue.isEmpty()) {
                    extensionValue = "true";
                }
                if (isSupportedExtensionValue(key, extensionValue, language)) {
                    value = extensionValue;
                    partOfTag = true;
                }
            }

            final String optionValue = optionValues.get(key);
            if (optionValue != null && !optionValue.equals(value)
                    && isSupportedExtensionValue(key, optionValue, language)) {
                value = optionValue;
                partOfTag = false;
            }

            if (value != null) {
                values.put(key, value);
                if (partOfTag) {
                    extension.append('-').append(key);
                    // 'true' is the default value of a key; it is not part of the tag
                    if (!"true".equals(value)) {
                        extension.append('-').append(value);
                    }
                }
            }
        }

        if (extension.length() == 0) {
            return new ResolvedLocale(availableLocale, values);
        }
        return new ResolvedLocale(availableLocale + "-u" + extension, values);
    }

    private static boolean isSupportedExtensionValue(final String key, final String value, final String language) {
        switch (key) {
            case "co":
                return isSupportedCollation(value, language);
            case "kf":
                return "upper".equals(value) || "lower".equals(value) || "false".equals(value);
            case "kn":
                return "true".equals(value) || "false".equals(value);
            case "hc":
                return "h11".equals(value) || "h12".equals(value) || "h23".equals(value) || "h24".equals(value);
            case "nu":
                return getZeroDigit(value) != 0;
            default:
                // e.g. 'ca' and 'nu'
                return !value.isEmpty();
        }
    }

    /**
     * Returns the zero digit of the given numbering system.
     *
     * @param numberingSystem the numbering system (e.g. 'latn' or 'arab')
     * @return the zero digit or {@code 0} if the numbering system is not supported
     */
    static char getZeroDigit(final String numberingSystem) {
        switch (numberingSystem) {
            case "latn":
                return '0';
            case "arab":
                return '\u0660';
            case "arabext":
                return '\u06f0';
            case "beng":
                return '\u09e6';
            case "deva":
                return '\u0966';
            case "fullwide":
                return '\uff10';
            case "gujr":
                return '\u0ae6';
            case "guru":
                return '\u0a66';
            case "khmr":
                return '\u17e0';
            case "knda":
                return '\u0ce6';
            case "laoo":
                return '\u0ed0';
            case "mlym":
                return '\u0d66';
            case "mymr":
                return '\u1040';
            case "orya":
                return '\u0b66';
            case "tamldec":
                return '\u0be6';
            case "telu":
                return '\u0c66';
            case "thai":
                return '\u0e50';
            case "tibt":
                return '\u0f20';
            default:
                return 0;
        }
    }

    /**
     * Returns the numbering system of the given zero digit.
     *
     * @param zeroDigit the zero digit
     * @return the numbering system; 'latn' for unknown zero digits
     */
    static String getNumberingSystem(final char zeroDigit) {
        for (final String numberingSystem : new String[] {"arab", "arabext", "beng", "deva", "fullwide", "gujr",
            "guru", "khmr", "knda", "laoo", "mlym", "mymr", "orya", "tamldec", "telu", "thai", "tibt"}) {
            if (getZeroDigit(numberingSystem) == zeroDigit) {
                return numberingSystem;
            }
        }
        return "latn";
    }

    /**
     * Returns whether the given collation is supported for the given language
     * ('standard' and 'search' are never allowed as collation).
     *
     * @param collation the collation type
     * @param language the language
     * @return true if supported
     */
    private static boolean isSupportedCollation(final String collation, final String language) {
        switch (collation) {
            case "emoji":
            case "eor":
                // supported for all languages
                return true;
            case "phonebk":
                return "de".equals(language);
            case "trad":
                return "es".equals(language);
            case "pinyin":
            case "stroke":
            case "zhuyin":
                return "zh".equals(language);
            case "unihan":
                return "zh".equals(language) || "ja".equals(language) || "ko".equals(language);
            default:
                return false;
        }
    }

    /**
     * Converts the options argument of the Intl constructors into an object
     * (the abstract operation CoerceOptionsToObject).
     *
     * @param scope the scope
     * @param options the options argument
     * @return the options object or {@code null} if the options argument is {@code undefined}
     * @throws org.htmlunit.corejs.javascript.EcmaError a {@code TypeError} if the options argument
     *         is {@code null}
     * @see <a href="https://tc39.es/ecma402/#sec-coerceoptionstoobject">spec</a>
     */
    static Scriptable coerceOptionsToObject(final VarScope scope, final Object options) {
        if (JavaScriptEngine.isUndefined(options)) {
            return null;
        }
        if (options == null) {
            throw JavaScriptEngine.typeError("Cannot convert null to object");
        }
        return JavaScriptEngine.toObject(scope, options);
    }

    /**
     * Returns the string value of an option (the abstract operation GetOption).
     *
     * @param options the options object; may be {@code null}
     * @param property the name of the option
     * @param values the allowed values; {@code null} if all values are allowed
     * @param fallback the value used if the option is {@code undefined}
     * @param className the name of the Intl object (for the error message)
     * @return the value of the option or the fallback
     * @throws org.htmlunit.corejs.javascript.EcmaError a {@code RangeError} if the value is not allowed
     * @see <a href="https://tc39.es/ecma402/#sec-getoption">spec</a>
     */
    static String getStringOption(final Scriptable options, final String property, final String[] values,
            final String fallback, final String className) {
        final Object value = getOptionValue(options, property);
        if (JavaScriptEngine.isUndefined(value)) {
            return fallback;
        }

        final String stringValue = JavaScriptEngine.toString(value);
        if (values != null && !Arrays.asList(values).contains(stringValue)) {
            throw JavaScriptEngine.rangeError("Value " + stringValue + " out of range for "
                    + className + " options property " + property);
        }
        return stringValue;
    }

    /**
     * Returns the boolean value of an option (the abstract operation GetOption).
     *
     * @param options the options object; may be {@code null}
     * @param property the name of the option
     * @return the value of the option or {@code null} if the option is {@code undefined}
     * @see <a href="https://tc39.es/ecma402/#sec-getoption">spec</a>
     */
    static Boolean getBooleanOption(final Scriptable options, final String property) {
        final Object value = getOptionValue(options, property);
        if (JavaScriptEngine.isUndefined(value)) {
            return null;
        }
        return JavaScriptEngine.toBoolean(value);
    }

    /**
     * Returns the value of an option.
     *
     * @param options the options object; may be {@code null}
     * @param property the name of the option
     * @return the value or {@code undefined}
     */
    static Object getOptionValue(final Scriptable options, final String property) {
        if (options == null) {
            return JavaScriptEngine.UNDEFINED;
        }
        final Object value = ScriptableObject.getProperty(options, property);
        if (value == Scriptable.NOT_FOUND) {
            return JavaScriptEngine.UNDEFINED;
        }
        return value;
    }
}
