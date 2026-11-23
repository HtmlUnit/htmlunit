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

import static org.htmlunit.BrowserVersionFeatures.JS_INTL_COLLATOR_COLLATION_IN_LOCALE;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
 * A JavaScript object for Intl.Collator.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/Collator">MDN Documentation</a>
 * @see <a href="https://tc39.es/ecma402/#collator-objects">ECMAScript Internationalization API - Collator Objects</a>
 */
@JsxClass
public class Collator extends HtmlUnitScriptable {

    /** Symbol.toStringTag support. */
    @JsxSymbolConstant
    public static final String TO_STRING_TAG = "Intl.Collator";

    private static final String CLASS_NAME = "Intl.Collator";

    private static final String SENSITIVITY_BASE = "base";
    private static final String SENSITIVITY_ACCENT = "accent";
    private static final String SENSITIVITY_CASE = "case";
    private static final String SENSITIVITY_VARIANT = "variant";

    // the rules added to the jdk collation rules of the locale for the supported collations
    private static final Map<String, String> COLLATION_RULES = Map.of(
            // German phonebook: the umlauts are sorted like the vowel followed by an 'e'
            "phonebk", "& ae ; \u00e4 & AE ; \u00c4 & oe ; \u00f6 & OE ; \u00d6 & ue ; \u00fc & UE ; \u00dc",
            // traditional Spanish: 'ch' and 'll' are letters of their own
            "trad", "& c < ch, cH, Ch, CH & l < ll, lL, Ll, LL");

    // the syntax of a unicode extension type (e.g. 'phonebk')
    private static final Pattern COLLATION_TYPE = Pattern.compile("[a-zA-Z0-9]{3,8}(-[a-zA-Z0-9]{3,8})*");

    // the resolved locale and options; locale_ is null for the prototype
    private String locale_;
    private String usage_;
    private String sensitivity_;
    private boolean ignorePunctuation_;
    private String collation_;
    private boolean numeric_;
    private String caseFirst_;

    // created on demand (java.text.Collator is not serializable); index is the strength
    private transient java.text.Collator[] collators_;
    // the bound compare function; always the same function for one collator
    private Function boundCompare_;

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments to the Collator constructor
     * @param ctorObj the function object
     * @param inNewExpr {@code true} if invoked with the {@code new} operator
     * @return the Java object that JavaScript can access
     * @see <a href="https://tc39.es/ecma402/#sec-initializecollator">spec</a>
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final VarScope scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        final List<String> locales = Intl.canonicalizeLocaleList(cx, Intl.firstArgument(args));
        final Scriptable options = Intl.coerceOptionsToObject(scope,
                args.length > 1 ? args[1] : JavaScriptEngine.UNDEFINED);

        // the options are read in the order defined by the spec
        final String usage = Intl.getStringOption(options, "usage",
                new String[] {"sort", "search"}, "sort", CLASS_NAME);
        Intl.getStringOption(options, "localeMatcher", new String[] {"lookup", "best fit"}, "best fit", CLASS_NAME);
        final String collation = Intl.getStringOption(options, "collation", null, null, CLASS_NAME);
        if (collation != null && !COLLATION_TYPE.matcher(collation).matches()) {
            throw JavaScriptEngine.rangeError("Invalid collation : " + collation);
        }
        final Boolean numeric = Intl.getBooleanOption(options, "numeric");
        final String caseFirst = Intl.getStringOption(options, "caseFirst",
                new String[] {"upper", "lower", "false"}, null, CLASS_NAME);

        final Collator collator = new Collator();
        collator.setParentScope(scope);
        collator.setPrototype(((FunctionObject) ctorObj).getClassPrototype());

        // the first supported requested locale wins; the browser locale is the fallback
        String requestedLocale = Intl.lookupSupportedLocale(locales);
        if (requestedLocale == null) {
            requestedLocale = collator.getBrowserVersion().getBrowserLocale().toLanguageTag();
        }

        // the options override the extensions of the locale
        final Map<String, String> optionValues = new HashMap<>();
        if (collation != null) {
            optionValues.put("co", collation);
        }
        if (numeric != null) {
            optionValues.put("kn", numeric.toString());
        }
        if (caseFirst != null) {
            optionValues.put("kf", caseFirst);
        }
        final boolean collationInLocale =
                collator.getBrowserVersion().hasFeature(JS_INTL_COLLATOR_COLLATION_IN_LOCALE);
        final Intl.ResolvedLocale resolved;
        if ("search".equals(usage)) {
            // a collator for searching ignores the collation (option and extension);
            // some browsers keep the collation extension in the locale
            optionValues.remove("co");
            resolved = collationInLocale
                    ? Intl.resolveLocale(requestedLocale, optionValues, "co", "kf", "kn")
                    : Intl.resolveLocale(requestedLocale, optionValues, "kf", "kn");
            collator.collation_ = "default";
        }
        else {
            resolved = Intl.resolveLocale(requestedLocale, optionValues, "co", "kf", "kn");
            final String resolvedCollation = resolved.values().get("co");
            collator.collation_ = resolvedCollation == null ? "default" : resolvedCollation;
        }
        collator.locale_ = resolved.locale();
        collator.usage_ = usage;

        // some browsers add the collation from the options to the locale,
        // if the requested locale has no collation extension
        if (collationInLocale && collation != null && collation.equals(collator.collation_)
                && java.util.Locale.forLanguageTag(requestedLocale).getUnicodeLocaleType("co") == null) {
            collator.locale_ = addCollation(collator.locale_, collation);
        }
        collator.numeric_ = "true".equals(resolved.values().get("kn"));
        final String resolvedCaseFirst = resolved.values().get("kf");
        collator.caseFirst_ = resolvedCaseFirst == null ? "false" : resolvedCaseFirst;

        final String sensitivity = Intl.getStringOption(options, "sensitivity",
                new String[] {SENSITIVITY_BASE, SENSITIVITY_ACCENT, SENSITIVITY_CASE, SENSITIVITY_VARIANT},
                null, CLASS_NAME);
        // 'variant' is the default for both usages
        collator.sensitivity_ = sensitivity == null ? SENSITIVITY_VARIANT : sensitivity;

        final Boolean ignorePunctuation = Intl.getBooleanOption(options, "ignorePunctuation");
        if (ignorePunctuation == null) {
            // the locale data of Thai ignores the punctuation
            final String language = java.util.Locale.forLanguageTag(collator.locale_).getLanguage();
            collator.ignorePunctuation_ = "th".equals(language);
        }
        else {
            collator.ignorePunctuation_ = ignorePunctuation.booleanValue();
        }

        return collator;
    }

    // adds the collation to the unicode extension of the given language tag
    private static String addCollation(final String languageTag, final String collation) {
        if (languageTag.contains("-u-co-")) {
            return languageTag;
        }
        final int pos = languageTag.indexOf("-u-");
        if (pos == -1) {
            return languageTag + "-u-co-" + collation;
        }
        // 'co' is the first of the relevant keys ('co', 'kf', 'kn')
        return languageTag.substring(0, pos) + "-u-co-" + collation + "-" + languageTag.substring(pos + 3);
    }

    /**
     * Returns the compare function of this collator; the function is bound to this collator
     * and compares two strings according to the sort order of this collator.
     *
     * @return the compare function
     */
    @JsxGetter
    public Function getCompare() {
        checkCollator("get Intl.Collator.prototype.compare");
        if (boundCompare_ == null) {
            // like a bound function: anonymous and without a prototype property
            boundCompare_ = new LambdaFunction(ScriptableObject.getTopLevelScope(getParentScope()), "", 2,
                    (cx, scope, thisObj, args) -> compare(args), false);
        }
        return boundCompare_;
    }

    private int compare(final Object[] args) {
        String x = JavaScriptEngine.toString(args.length > 0 ? args[0] : JavaScriptEngine.UNDEFINED);
        String y = JavaScriptEngine.toString(args.length > 1 ? args[1] : JavaScriptEngine.UNDEFINED);

        if (ignorePunctuation_) {
            x = removePunctuation(x);
            y = removePunctuation(y);
        }

        if (numeric_) {
            // first the base letters and the values of the numbers
            final int result = compareNumeric(x, y);
            if (result != 0) {
                return result;
            }
            // the numbers are equal; ignore the leading zeros for the other levels
            x = normalizeNumbers(x);
            y = normalizeNumbers(y);
        }

        if (SENSITIVITY_BASE.equals(sensitivity_)) {
            return Integer.signum(getCollator(java.text.Collator.PRIMARY).compare(x, y));
        }
        if (SENSITIVITY_ACCENT.equals(sensitivity_)) {
            return Integer.signum(getCollator(java.text.Collator.SECONDARY).compare(x, y));
        }
        if (SENSITIVITY_CASE.equals(sensitivity_)) {
            final int result = Integer.signum(getCollator(java.text.Collator.PRIMARY).compare(x, y));
            if (result != 0) {
                return result;
            }
            // only the case is relevant, not the accents
            return compareTertiary(removeAccents(x), removeAccents(y));
        }
        return compareTertiary(x, y);
    }

    // Compares the strings at the tertiary level (base letters, accents and case)
    // taking the caseFirst option into account.
    private int compareTertiary(final String x, final String y) {
        final java.text.Collator tertiary = getCollator(java.text.Collator.TERTIARY);
        final int result = Integer.signum(tertiary.compare(x, y));
        if (result != 0 && "upper".equals(caseFirst_)
                && getCollator(java.text.Collator.SECONDARY).compare(x, y) == 0) {
            // only the case differs; the jdk sorts the lower case letters first
            return Integer.signum(tertiary.compare(swapCase(x), swapCase(y)));
        }
        return result;
    }

    // Compares the strings at the primary level; sequences of digits are compared
    // by their numeric value.
    private int compareNumeric(final String x, final String y) {
        final java.text.Collator primary = getCollator(java.text.Collator.PRIMARY);

        int xPos = 0;
        int yPos = 0;
        while (xPos < x.length() && yPos < y.length()) {
            final int xEnd = endOfSegment(x, xPos);
            final int yEnd = endOfSegment(y, yPos);
            final String xSegment = x.substring(xPos, xEnd);
            final String ySegment = y.substring(yPos, yEnd);

            final int result;
            if (Character.isDigit(x.charAt(xPos)) && Character.isDigit(y.charAt(yPos))) {
                result = compareNumbers(xSegment, ySegment);
            }
            else {
                result = Integer.signum(primary.compare(xSegment, ySegment));
            }
            if (result != 0) {
                return result;
            }

            xPos = xEnd;
            yPos = yEnd;
        }

        if (xPos < x.length()) {
            return Integer.signum(primary.compare(x.substring(xPos), ""));
        }
        if (yPos < y.length()) {
            return Integer.signum(primary.compare("", y.substring(yPos)));
        }
        return 0;
    }

    // Returns the end of the segment starting at the given position; a segment
    // is a sequence of digits or a sequence of other characters.
    private static int endOfSegment(final String s, final int start) {
        final boolean digits = Character.isDigit(s.charAt(start));
        int end = start + 1;
        while (end < s.length() && Character.isDigit(s.charAt(end)) == digits) {
            end++;
        }
        return end;
    }

    private static int compareNumbers(final String x, final String y) {
        final String xNumber = toNumber(x);
        final String yNumber = toNumber(y);
        if (xNumber.length() != yNumber.length()) {
            return xNumber.length() < yNumber.length() ? -1 : 1;
        }
        return Integer.signum(xNumber.compareTo(yNumber));
    }

    // Converts a sequence of (unicode) digits into the ascii digits without the leading zeros.
    private static String toNumber(final String digits) {
        final StringBuilder number = new StringBuilder(digits.length());
        for (int i = 0; i < digits.length(); i++) {
            final int value = Character.digit(digits.charAt(i), 10);
            if (number.length() > 0 || value != 0) {
                number.append((char) ('0' + value));
            }
        }
        if (number.length() == 0) {
            return "0";
        }
        return number.toString();
    }

    // Replaces all sequences of digits by the ascii digits without the leading zeros.
    private static String normalizeNumbers(final String s) {
        final StringBuilder result = new StringBuilder(s.length());
        int pos = 0;
        while (pos < s.length()) {
            final int end = endOfSegment(s, pos);
            final String segment = s.substring(pos, end);
            if (Character.isDigit(s.charAt(pos))) {
                result.append(toNumber(segment));
            }
            else {
                result.append(segment);
            }
            pos = end;
        }
        return result.toString();
    }

    private static String removePunctuation(final String s) {
        final StringBuilder result = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (!isPunctuationOrWhitespace(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static boolean isPunctuationOrWhitespace(final char c) {
        if (Character.isWhitespace(c) || Character.isSpaceChar(c)) {
            return true;
        }
        switch (Character.getType(c)) {
            case Character.CONNECTOR_PUNCTUATION:
            case Character.DASH_PUNCTUATION:
            case Character.START_PUNCTUATION:
            case Character.END_PUNCTUATION:
            case Character.INITIAL_QUOTE_PUNCTUATION:
            case Character.FINAL_QUOTE_PUNCTUATION:
            case Character.OTHER_PUNCTUATION:
                return true;
            default:
                return false;
        }
    }

    private static String removeAccents(final String s) {
        final String decomposed = Normalizer.normalize(s, Normalizer.Form.NFD);
        final StringBuilder result = new StringBuilder(decomposed.length());
        for (int i = 0; i < decomposed.length(); i++) {
            final char c = decomposed.charAt(i);
            if (Character.getType(c) != Character.NON_SPACING_MARK) {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static String swapCase(final String s) {
        final StringBuilder result = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append(Character.toLowerCase(c));
            }
            else if (Character.isLowerCase(c)) {
                result.append(Character.toUpperCase(c));
            }
            else {
                result.append(c);
            }
        }
        return result.toString();
    }

    // the rules added to the jdk collation rules of the locale; null if there are none
    private String getCollationRules() {
        if ("search".equals(usage_)) {
            // the German search collation matches the umlauts like the phonebook
            if ("de".equals(java.util.Locale.forLanguageTag(locale_).getLanguage())) {
                return COLLATION_RULES.get("phonebk");
            }
            return null;
        }
        return COLLATION_RULES.get(collation_);
    }

    private java.text.Collator getCollator(final int strength) {
        if (collators_ == null) {
            collators_ = new java.text.Collator[java.text.Collator.TERTIARY + 1];
        }
        if (collators_[strength] == null) {
            java.text.Collator collator =
                    java.text.Collator.getInstance(java.util.Locale.forLanguageTag(locale_));
            final String additionalRules = getCollationRules();
            if (additionalRules != null && collator instanceof RuleBasedCollator ruleBasedCollator) {
                try {
                    collator = new RuleBasedCollator(ruleBasedCollator.getRules() + additionalRules);
                }
                catch (final ParseException e) {
                    throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
                }
            }
            collator.setStrength(strength);
            collator.setDecomposition(java.text.Collator.CANONICAL_DECOMPOSITION);
            collators_[strength] = collator;
        }
        return collators_[strength];
    }

    /**
     * Returns the locale and the collation options computed during the initialization
     * of this collator.
     *
     * @return a new object with the resolved options
     */
    @JsxFunction
    public Scriptable resolvedOptions() {
        checkCollator("Intl.Collator.prototype.resolvedOptions");
        final Scriptable options = JavaScriptEngine.newObject(getParentScope());
        options.put("locale", options, locale_);
        options.put("usage", options, usage_);
        options.put("sensitivity", options, sensitivity_);
        options.put("ignorePunctuation", options, Boolean.valueOf(ignorePunctuation_));
        options.put("collation", options, collation_);
        options.put("numeric", options, Boolean.valueOf(numeric_));
        options.put("caseFirst", options, caseFirst_);
        return options;
    }

    private void checkCollator(final String method) {
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
            return "[object Intl.Collator]";
        }
        return super.getDefaultValue(hint);
    }
}
