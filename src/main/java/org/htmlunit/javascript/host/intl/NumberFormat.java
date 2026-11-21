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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.htmlunit.BrowserVersion;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.LambdaFunction;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxStaticFunction;
import org.htmlunit.javascript.configuration.JsxSymbolConstant;

/**
 * A JavaScript object for Intl.NumberFormat.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Lai Quang Duong
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/NumberFormat">MDN Documentation</a>
 * @see <a href="https://tc39.es/ecma402/#numberformat-objects">ECMAScript Internationalization API -
 *      NumberFormat Objects</a>
 */
@JsxClass
public class NumberFormat extends HtmlUnitScriptable {

    /** Symbol.toStringTag support. */
    @JsxSymbolConstant
    public static final String TO_STRING_TAG = "Intl.NumberFormat";

    private static final String CLASS_NAME = "Intl.NumberFormat";

    private static final ConcurrentHashMap<String, String> CHROME_FORMATS_ = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> EDGE_FORMATS_ = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> FF_FORMATS_ = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> FF_ESR_FORMATS_ = new ConcurrentHashMap<>();

    // the syntax of a unicode extension type (e.g. 'latn')
    private static final Pattern TYPE = Pattern.compile("[a-zA-Z0-9]{3,8}(-[a-zA-Z0-9]{3,8})*");
    private static final Pattern DECIMAL_LITERAL =
            Pattern.compile("[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?");
    private static final int[] ROUNDING_INCREMENTS =
        {1, 2, 5, 10, 20, 25, 50, 100, 200, 250, 500, 1000, 2000, 2500, 5000};

    // the resolved locale and options; locale_ is null for the prototype
    private String locale_;
    private String numberingSystem_;
    private NumberFormatter.Options options_;
    // the fraction and significant digits are not always part of the resolved options
    private boolean hasFractionDigits_;
    private boolean hasSignificantDigits_;
    private String roundingPriority_;

    // the data needed to create the formatter
    private String formatLocale_;
    private String separators_;
    private char zeroDigit_;

    // created on demand
    private transient NumberFormatter formatter_;
    // the bound format function; always the same function for one number format
    private Function boundFormat_;

    static {
        final Map<String, String> commonFormats = new HashMap<>();
        commonFormats.put("", "");
        commonFormats.put("ar-DZ", ".,");
        commonFormats.put("ar-LY", ".,");
        commonFormats.put("ar-MA", ".,");
        commonFormats.put("ar-TN", ".,");
        commonFormats.put("id", ".,");
        commonFormats.put("de-AT", "\u00a0");
        commonFormats.put("de-CH", "'");
        commonFormats.put("en-ZA", "\u00a0,");
        commonFormats.put("es-CR", "\u00a0,");
        commonFormats.put("fr-LU", ".,");
        commonFormats.put("hi-IN", ",.0");
        commonFormats.put("it-CH", "'");
        commonFormats.put("pt-PT", "\u00a0,");
        commonFormats.put("sq", "\u00a0,");

        commonFormats.put("ar-AE", ",.0");
        commonFormats.put("fr", "\u202f,");
        commonFormats.put("fr-CA", "\u00a0,");

        commonFormats.put("ar", ",.0");
        commonFormats.put("ar-BH", "\u066c\u066b\u0660");
        commonFormats.put("ar-EG", "\u066c\u066b\u0660");
        commonFormats.put("ar-IQ", "\u066c\u066b\u0660");
        commonFormats.put("ar-JO", "\u066c\u066b\u0660");
        commonFormats.put("ar-KW", "\u066c\u066b\u0660");
        commonFormats.put("ar-LB", "\u066c\u066b\u0660");
        commonFormats.put("ar-OM", "\u066c\u066b\u0660");
        commonFormats.put("ar-QA", "\u066c\u066b\u0660");
        commonFormats.put("ar-SA", "\u066c\u066b\u0660");
        commonFormats.put("ar-SD", "\u066c\u066b\u0660");
        commonFormats.put("ar-SY", "\u066c\u066b\u0660");
        commonFormats.put("ar-YE", "\u066c\u066b\u0660");

        FF_FORMATS_.putAll(commonFormats);
        FF_FORMATS_.put("fr-CH", "',");

        FF_ESR_FORMATS_.putAll(commonFormats);
        FF_ESR_FORMATS_.put("fr-CH", "',");

        commonFormats.put("be", ",.");
        commonFormats.put("mk", ",.");
        commonFormats.put("is", ",.");

        CHROME_FORMATS_.putAll(commonFormats);
        CHROME_FORMATS_.put("sq", ",.");

        EDGE_FORMATS_.putAll(commonFormats);
    }

    /**
     * Default constructor.
     */
    public NumberFormat() {
        super();
    }

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments to the NumberFormat constructor
     * @param ctorObj the function object
     * @param inNewExpr {@code true} if invoked with the {@code new} operator
     * @return the Java object that JavaScript can access
     * @see <a href="https://tc39.es/ecma402/#sec-initializenumberformat">spec</a>
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final VarScope scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        final List<String> locales = Intl.canonicalizeLocaleList(cx, Intl.firstArgument(args));
        final Scriptable options = Intl.coerceOptionsToObject(scope,
                args.length > 1 ? args[1] : JavaScriptEngine.UNDEFINED);

        final NumberFormat format = new NumberFormat();
        format.setParentScope(getTopLevelScope(scope));
        format.setPrototype(((FunctionObject) ctorObj).getClassPrototype());
        format.initialize(locales, options, format.getBrowserVersion());
        return format;
    }

    private void initialize(final List<String> locales, final Scriptable options,
            final BrowserVersion browserVersion) {
        Intl.getStringOption(options, "localeMatcher", new String[] {"lookup", "best fit"}, "best fit", CLASS_NAME);
        final String numberingSystem = Intl.getStringOption(options, "numberingSystem", null, null, CLASS_NAME);
        if (numberingSystem != null && !TYPE.matcher(numberingSystem).matches()) {
            throw JavaScriptEngine.rangeError("Invalid numberingSystem : " + numberingSystem);
        }

        // the first supported requested locale wins; the browser locale is the fallback
        final String requestedLocale = Intl.lookupSupportedLocale(locales);
        final String localeTag = requestedLocale == null
                ? browserVersion.getBrowserLocale().toLanguageTag() : requestedLocale;
        final Map<String, String> optionValues = new HashMap<>();
        if (numberingSystem != null) {
            optionValues.put("nu", numberingSystem);
        }
        final Intl.ResolvedLocale resolved = Intl.resolveLocale(localeTag, optionValues, "nu");
        locale_ = resolved.locale();

        // the browser specific separators and the numbering system
        final Map<String, String> formats;
        if (browserVersion.isChrome()) {
            formats = CHROME_FORMATS_;
        }
        else if (browserVersion.isEdge()) {
            formats = EDGE_FORMATS_;
        }
        else if (browserVersion.isFirefoxESR()) {
            formats = FF_ESR_FORMATS_;
        }
        else {
            formats = FF_FORMATS_;
        }
        String separators = requestedLocale == null ? null : getPattern(formats, requestedLocale);
        if (separators == null) {
            separators = formats.get("");
        }
        separators_ = separators;
        formatLocale_ = localeTag;

        final String resolvedNumberingSystem = resolved.values().get("nu");
        if (resolvedNumberingSystem == null) {
            zeroDigit_ = 0;
            final char defaultZeroDigit = separators.length() > 2 && separators.charAt(2) != ' '
                    ? separators.charAt(2)
                    : java.text.DecimalFormatSymbols.getInstance(Locale.forLanguageTag(localeTag)).getZeroDigit();
            numberingSystem_ = Intl.getNumberingSystem(defaultZeroDigit);
        }
        else {
            zeroDigit_ = Intl.getZeroDigit(resolvedNumberingSystem);
            numberingSystem_ = resolvedNumberingSystem;
        }

        final NumberFormatter.Options resolvedOptions = new NumberFormatter.Options();
        setUnitOptions(options, resolvedOptions);

        final int minimumFractionDigitsDefault;
        final int maximumFractionDigitsDefault;
        if ("currency".equals(resolvedOptions.getStyle())) {
            final int currencyDigits = NumberFormatter.currencyDigits(resolvedOptions.getCurrency());
            minimumFractionDigitsDefault = currencyDigits;
            maximumFractionDigitsDefault = currencyDigits;
        }
        else {
            minimumFractionDigitsDefault = 0;
            maximumFractionDigitsDefault = "percent".equals(resolvedOptions.getStyle()) ? 0 : 3;
        }

        resolvedOptions.setNotation(Intl.getStringOption(options, "notation",
                new String[] {"standard", "scientific", "engineering", "compact"}, "standard", CLASS_NAME));
        setDigitOptions(options, resolvedOptions, minimumFractionDigitsDefault, maximumFractionDigitsDefault);

        resolvedOptions.setCompactDisplay(Intl.getStringOption(options, "compactDisplay",
                new String[] {"short", "long"}, "short", CLASS_NAME));

        final String defaultUseGrouping = "compact".equals(resolvedOptions.getNotation()) ? "min2" : "auto";
        resolvedOptions.setUseGrouping(getUseGroupingOption(options, defaultUseGrouping));

        resolvedOptions.setSignDisplay(Intl.getStringOption(options, "signDisplay",
                new String[] {"auto", "never", "always", "exceptZero", "negative"}, "auto", CLASS_NAME));

        options_ = resolvedOptions;
    }

    // SetNumberFormatUnitOptions
    private static void setUnitOptions(final Scriptable options, final NumberFormatter.Options resolvedOptions) {
        resolvedOptions.setStyle(Intl.getStringOption(options, "style",
                new String[] {"decimal", "percent", "currency", "unit"}, "decimal", CLASS_NAME));

        final String currency = Intl.getStringOption(options, "currency", null, null, CLASS_NAME);
        if (currency != null && !currency.matches("[a-zA-Z]{3}")) {
            throw JavaScriptEngine.rangeError("Invalid currency code : " + currency);
        }
        resolvedOptions.setCurrencyDisplay(Intl.getStringOption(options, "currencyDisplay",
                new String[] {"code", "symbol", "narrowSymbol", "name"}, "symbol", CLASS_NAME));
        resolvedOptions.setCurrencySign(Intl.getStringOption(options, "currencySign",
                new String[] {"standard", "accounting"}, "standard", CLASS_NAME));

        final String unit = Intl.getStringOption(options, "unit", null, null, CLASS_NAME);
        if (unit != null && !NumberFormatter.isWellFormedUnitIdentifier(unit)) {
            throw JavaScriptEngine.rangeError("Invalid unit argument for Intl.NumberFormat() '" + unit + "'");
        }
        resolvedOptions.setUnitDisplay(Intl.getStringOption(options, "unitDisplay",
                new String[] {"short", "narrow", "long"}, "short", CLASS_NAME));

        if ("currency".equals(resolvedOptions.getStyle())) {
            if (currency == null) {
                throw JavaScriptEngine.typeError("Currency code is required with currency style.");
            }
            resolvedOptions.setCurrency(currency.toUpperCase(Locale.ROOT));
        }
        if ("unit".equals(resolvedOptions.getStyle())) {
            if (unit == null) {
                throw JavaScriptEngine.typeError("Unit is required with unit style.");
            }
            resolvedOptions.setUnit(unit);
        }
    }

    // SetNumberFormatDigitOptions
    private void setDigitOptions(final Scriptable options, final NumberFormatter.Options resolvedOptions,
            final int minimumFractionDigitsDefault, final int maximumFractionDigitsDefault) {
        final int minimumIntegerDigits = defaultNumberOption("minimumIntegerDigits",
                Intl.getOptionValue(options, "minimumIntegerDigits"), 1, 21, 1);
        final Object minimumFractionDigits = Intl.getOptionValue(options, "minimumFractionDigits");
        final Object maximumFractionDigits = Intl.getOptionValue(options, "maximumFractionDigits");
        final Object minimumSignificantDigits = Intl.getOptionValue(options, "minimumSignificantDigits");
        final Object maximumSignificantDigits = Intl.getOptionValue(options, "maximumSignificantDigits");
        resolvedOptions.setMinimumIntegerDigits(minimumIntegerDigits);

        final int roundingIncrement = defaultNumberOption("roundingIncrement",
                Intl.getOptionValue(options, "roundingIncrement"), 1, 5000, 1);
        boolean validIncrement = false;
        for (final int increment : ROUNDING_INCREMENTS) {
            validIncrement |= increment == roundingIncrement;
        }
        if (!validIncrement) {
            throw JavaScriptEngine.rangeError("roundingIncrement value is out of range.");
        }
        resolvedOptions.setRoundingMode(Intl.getStringOption(options, "roundingMode",
                new String[] {"ceil", "floor", "expand", "trunc", "halfCeil", "halfFloor", "halfExpand",
                    "halfTrunc", "halfEven"}, "halfExpand", CLASS_NAME));
        final String roundingPriority = Intl.getStringOption(options, "roundingPriority",
                new String[] {"auto", "morePrecision", "lessPrecision"}, "auto", CLASS_NAME);
        resolvedOptions.setTrailingZeroDisplay(Intl.getStringOption(options, "trailingZeroDisplay",
                new String[] {"auto", "stripIfInteger"}, "auto", CLASS_NAME));

        int maximumFractionDigitsFallback = maximumFractionDigitsDefault;
        if (roundingIncrement != 1) {
            maximumFractionDigitsFallback = minimumFractionDigitsDefault;
        }
        resolvedOptions.setRoundingIncrement(roundingIncrement);

        final boolean hasSignificantDigits = !JavaScriptEngine.isUndefined(minimumSignificantDigits)
                || !JavaScriptEngine.isUndefined(maximumSignificantDigits);
        final boolean hasFractionDigits = !JavaScriptEngine.isUndefined(minimumFractionDigits)
                || !JavaScriptEngine.isUndefined(maximumFractionDigits);
        boolean needSignificantDigits = true;
        boolean needFractionDigits = true;
        if ("auto".equals(roundingPriority)) {
            needSignificantDigits = hasSignificantDigits;
            if (needSignificantDigits || !hasFractionDigits && "compact".equals(resolvedOptions.getNotation())) {
                needFractionDigits = false;
            }
        }

        if (needSignificantDigits) {
            if (hasSignificantDigits) {
                final int minimum = defaultNumberOption("minimumSignificantDigits",
                        minimumSignificantDigits, 1, 21, 1);
                final int maximum = defaultNumberOption("maximumSignificantDigits",
                        maximumSignificantDigits, minimum, 21, 21);
                resolvedOptions.setMinimumSignificantDigits(minimum);
                resolvedOptions.setMaximumSignificantDigits(maximum);
            }
            else {
                resolvedOptions.setMinimumSignificantDigits(1);
                resolvedOptions.setMaximumSignificantDigits(21);
            }
        }

        if (needFractionDigits) {
            if (hasFractionDigits) {
                final Integer minimum = JavaScriptEngine.isUndefined(minimumFractionDigits) ? null
                        : defaultNumberOption("minimumFractionDigits", minimumFractionDigits, 0, 100, 0);
                final Integer maximum = JavaScriptEngine.isUndefined(maximumFractionDigits) ? null
                        : defaultNumberOption("maximumFractionDigits", maximumFractionDigits, 0, 100, 0);
                if (minimum == null) {
                    resolvedOptions.setMaximumFractionDigits(maximum.intValue());
                    resolvedOptions.setMinimumFractionDigits(
                            Math.min(minimumFractionDigitsDefault, maximum.intValue()));
                }
                else if (maximum == null) {
                    resolvedOptions.setMinimumFractionDigits(minimum.intValue());
                    resolvedOptions.setMaximumFractionDigits(
                            Math.max(maximumFractionDigitsFallback, minimum.intValue()));
                }
                else if (minimum.intValue() > maximum.intValue()) {
                    throw JavaScriptEngine.rangeError("maximumFractionDigits value is out of range.");
                }
                else {
                    resolvedOptions.setMinimumFractionDigits(minimum.intValue());
                    resolvedOptions.setMaximumFractionDigits(maximum.intValue());
                }
            }
            else {
                resolvedOptions.setMinimumFractionDigits(minimumFractionDigitsDefault);
                resolvedOptions.setMaximumFractionDigits(maximumFractionDigitsFallback);
            }
        }

        if (!needSignificantDigits && !needFractionDigits) {
            // the default rounding of the compact notation
            resolvedOptions.setMinimumFractionDigits(0);
            resolvedOptions.setMaximumFractionDigits(0);
            resolvedOptions.setMinimumSignificantDigits(1);
            resolvedOptions.setMaximumSignificantDigits(2);
            resolvedOptions.setRoundingType("morePrecision");
            roundingPriority_ = "morePrecision";
            hasFractionDigits_ = true;
            hasSignificantDigits_ = true;
        }
        else if ("auto".equals(roundingPriority)) {
            resolvedOptions.setRoundingType(needSignificantDigits ? "significantDigits" : "fractionDigits");
            roundingPriority_ = "auto";
            hasFractionDigits_ = needFractionDigits;
            hasSignificantDigits_ = needSignificantDigits;
        }
        else {
            resolvedOptions.setRoundingType(roundingPriority);
            roundingPriority_ = roundingPriority;
            hasFractionDigits_ = true;
            hasSignificantDigits_ = true;
        }

        if (roundingIncrement != 1) {
            if (!"fractionDigits".equals(resolvedOptions.getRoundingType())) {
                throw JavaScriptEngine.typeError(
                        "roundingIncrement is only supported with the rounding type fractionDigits.");
            }
            if (resolvedOptions.getMaximumFractionDigits() != resolvedOptions.getMinimumFractionDigits()) {
                throw JavaScriptEngine.rangeError(
                        "maximumFractionDigits must be equal to minimumFractionDigits for roundingIncrement.");
            }
        }
    }

    // DefaultNumberOption
    private static int defaultNumberOption(final String property, final Object value,
            final int minimum, final int maximum, final int fallback) {
        if (JavaScriptEngine.isUndefined(value)) {
            return fallback;
        }
        final double number = JavaScriptEngine.toNumber(value);
        if (Double.isNaN(number) || number < minimum || number > maximum) {
            throw JavaScriptEngine.rangeError(property + " value is out of range.");
        }
        return (int) Math.floor(number);
    }

    // GetBooleanOrStringNumberFormatOption for useGrouping; null means false
    private static String getUseGroupingOption(final Scriptable options, final String fallback) {
        final Object value = Intl.getOptionValue(options, "useGrouping");
        if (JavaScriptEngine.isUndefined(value)) {
            return fallback;
        }
        if (Boolean.TRUE.equals(value)) {
            return "always";
        }
        if (!JavaScriptEngine.toBoolean(value)) {
            return null;
        }
        final String stringValue = JavaScriptEngine.toString(value);
        switch (stringValue) {
            case "min2":
            case "auto":
            case "always":
                return stringValue;
            case "true":
            case "false":
                return fallback;
            default:
                throw JavaScriptEngine.rangeError("Value " + stringValue + " out of range for "
                        + CLASS_NAME + " options property useGrouping");
        }
    }

    private static String getPattern(final Map<String, String> formats, final String locale) {
        String pattern = formats.get(locale);
        if (pattern == null && locale.indexOf('-') != -1) {
            pattern = formats.get(locale.substring(0, locale.indexOf('-')));
        }
        return pattern;
    }

    /**
     * Returns the format function of this number format; the function is bound to this
     * number format and formats a number according to the locale and the options.
     *
     * @return the format function
     */
    @JsxGetter
    public Function getFormat() {
        checkNumberFormat("get Intl.NumberFormat.prototype.format");
        if (boundFormat_ == null) {
            // like a bound function: anonymous and without a prototype property
            boundFormat_ = new LambdaFunction(ScriptableObject.getTopLevelScope(getParentScope()), "", 1,
                    (cx, scope, thisObj, args) -> format(args.length > 0 ? args[0] : JavaScriptEngine.UNDEFINED),
                    false);
        }
        return boundFormat_;
    }

    private String format(final Object value) {
        final NumberFormatter.Value mathematicalValue = toIntlMathematicalValue(value);
        return getFormatter().format(mathematicalValue.number(), mathematicalValue.negative());
    }

    private NumberFormatter getFormatter() {
        if (formatter_ == null) {
            formatter_ = new NumberFormatter(options_, Locale.forLanguageTag(formatLocale_), separators_, zeroDigit_);
        }
        return formatter_;
    }

    // ToIntlMathematicalValue
    private static NumberFormatter.Value toIntlMathematicalValue(final Object value) {
        if (value instanceof BigInteger bigInteger) {
            return new NumberFormatter.Value(new BigDecimal(bigInteger), bigInteger.signum() < 0);
        }
        if (value instanceof CharSequence) {
            final String string = value.toString().trim();
            if (DECIMAL_LITERAL.matcher(string).matches()) {
                // strings are formatted with all digits
                return new NumberFormatter.Value(new BigDecimal(string), string.startsWith("-"));
            }
        }

        final double number = JavaScriptEngine.toNumber(value);
        if (Double.isNaN(number) || Double.isInfinite(number)) {
            return new NumberFormatter.Value(Double.valueOf(number), number < 0);
        }
        final boolean negative = number < 0 || number == 0 && 1 / number < 0;
        return new NumberFormatter.Value(new BigDecimal(JavaScriptEngine.toString(number)), negative);
    }

    /**
     * Formats a number into parts.
     *
     * @param value the number
     * @return an array of objects with the type and the value of the parts
     * @see <a href="https://tc39.es/ecma402/#sec-intl.numberformat.prototype.formattoparts">spec</a>
     */
    @JsxFunction
    public Scriptable formatToParts(final Object value) {
        checkNumberFormat("Intl.NumberFormat.prototype.formatToParts");
        final NumberFormatter.Value mathematicalValue = toIntlMathematicalValue(value);
        return toArray(getFormatter().formatToParts(mathematicalValue.number(), mathematicalValue.negative()));
    }

    /**
     * Formats a range of numbers.
     *
     * @param start the start of the range
     * @param end the end of the range
     * @return the formatted range
     * @see <a href="https://tc39.es/ecma402/#sec-intl.numberformat.prototype.formatrange">spec</a>
     */
    @JsxFunction
    public String formatRange(final Object start, final Object end) {
        checkNumberFormat("Intl.NumberFormat.prototype.formatRange");
        return NumberFormatter.join(formatRangeParts(start, end));
    }

    /**
     * Formats a range of numbers into parts.
     *
     * @param start the start of the range
     * @param end the end of the range
     * @return an array of objects with the type, the value and the source of the parts
     * @see <a href="https://tc39.es/ecma402/#sec-intl.numberformat.prototype.formatrangetoparts">spec</a>
     */
    @JsxFunction
    public Scriptable formatRangeToParts(final Object start, final Object end) {
        checkNumberFormat("Intl.NumberFormat.prototype.formatRangeToParts");
        return toArray(formatRangeParts(start, end));
    }

    private List<NumberFormatter.Part> formatRangeParts(final Object start, final Object end) {
        if (JavaScriptEngine.isUndefined(start) || JavaScriptEngine.isUndefined(end)) {
            throw JavaScriptEngine.typeError("start or end is undefined");
        }
        final NumberFormatter.Value startValue = toIntlMathematicalValue(start);
        final NumberFormatter.Value endValue = toIntlMathematicalValue(end);
        if (startValue.isNaN() || endValue.isNaN()) {
            throw JavaScriptEngine.rangeError("Invalid NaN value in the range");
        }
        return getFormatter().formatRangeToParts(startValue, endValue);
    }

    // the parts as array of objects ({type, value[, source]})
    private Scriptable toArray(final List<NumberFormatter.Part> parts) {
        final Object[] objects = new Object[parts.size()];
        for (int i = 0; i < objects.length; i++) {
            final NumberFormatter.Part part = parts.get(i);
            final Scriptable object = JavaScriptEngine.newObject(getParentScope());
            object.put("type", object, part.type());
            object.put("value", object, part.value());
            if (part.source() != null) {
                object.put("source", object, part.source());
            }
            objects[i] = object;
        }
        return JavaScriptEngine.newArray(getParentScope(), objects);
    }

    /**
     * Returns the resolved options for this Intl.NumberFormat object.
     * @return a new object with properties reflecting the locale and number formatting options
     *         computed during the initialization of the given {@code NumberFormat} object.
     */
    @JsxFunction
    public Scriptable resolvedOptions() {
        checkNumberFormat("Intl.NumberFormat.prototype.resolvedOptions");
        final NumberFormatter.Options o = options_;
        final Scriptable options = JavaScriptEngine.newObject(getParentScope());
        options.put("locale", options, locale_);
        options.put("numberingSystem", options, numberingSystem_);
        options.put("style", options, o.getStyle());
        if ("currency".equals(o.getStyle())) {
            options.put("currency", options, o.getCurrency());
            options.put("currencyDisplay", options, o.getCurrencyDisplay());
            options.put("currencySign", options, o.getCurrencySign());
        }
        if ("unit".equals(o.getStyle())) {
            options.put("unit", options, o.getUnit());
            options.put("unitDisplay", options, o.getUnitDisplay());
        }
        options.put("minimumIntegerDigits", options, Integer.valueOf(o.getMinimumIntegerDigits()));
        if (hasFractionDigits_) {
            options.put("minimumFractionDigits", options, Integer.valueOf(o.getMinimumFractionDigits()));
            options.put("maximumFractionDigits", options, Integer.valueOf(o.getMaximumFractionDigits()));
        }
        if (hasSignificantDigits_) {
            options.put("minimumSignificantDigits", options, Integer.valueOf(o.getMinimumSignificantDigits()));
            options.put("maximumSignificantDigits", options, Integer.valueOf(o.getMaximumSignificantDigits()));
        }
        options.put("useGrouping", options, o.getUseGrouping() == null ? Boolean.FALSE : o.getUseGrouping());
        options.put("notation", options, o.getNotation());
        if ("compact".equals(o.getNotation())) {
            options.put("compactDisplay", options, o.getCompactDisplay());
        }
        options.put("signDisplay", options, o.getSignDisplay());
        options.put("roundingIncrement", options, Integer.valueOf(o.getRoundingIncrement()));
        options.put("roundingMode", options, o.getRoundingMode());
        options.put("roundingPriority", options, roundingPriority_);
        options.put("trailingZeroDisplay", options, o.getTrailingZeroDisplay());
        return options;
    }

    private void checkNumberFormat(final String method) {
        if (locale_ == null) {
            throw JavaScriptEngine.typeError("Method " + method + " called on incompatible receiver");
        }
    }

    /**
     * Returns an array containing those of the provided locales that are supported
     * without having to fall back to the default locale.
     * @param cx the current context
     * @param scope the scope
     * @param thisObj the scriptable this
     * @param args the arguments; a string with a BCP 47 language tag or an array of such strings
     *        and the options (unused)
     * @param funObj the function object
     * @return an array containing supported locales
     */
    @JsxStaticFunction
    public static Scriptable supportedLocalesOf(final Context cx, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function funObj) {
        return Intl.supportedLocalesOf(cx, scope, args);
    }

    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (String.class.equals(hint) || hint == null) {
            return "[object Intl.NumberFormat]";
        }
        return super.getDefaultValue(hint);
    }
}
