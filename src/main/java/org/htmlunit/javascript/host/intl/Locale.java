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

import java.util.IllformedLocaleException;
import java.util.List;

import org.apache.commons.lang3.LocaleUtils;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSymbolConstant;

/**
 * A JavaScript object for Intl.Locale.
 *
 * @author Lai Quang Duong
 */
@JsxClass
public class Locale extends HtmlUnitScriptable {

    /** Symbol.toStringTag support. */
    @JsxSymbolConstant
    public static final String TO_STRING_TAG = "Intl.Locale";

    private static final List<String> ALLOWED_HOUR_CYCLES = List.of("h11", "h12", "h23", "h24");
    private static final List<String> ALLOWED_CASE_FIRSTS = List.of("upper", "lower", "false");

    private java.util.Locale locale_;
    private String language_;
    private String script_;
    private String region_;
    private String calendar_;
    private String collation_;
    private String numberingSystem_;
    private String caseFirst_;
    private String hourCycle_;
    private boolean numeric_;

    /**
     * Default constructor.
     */
    public Locale() {
        super();
    }

    private Locale(final java.util.Locale locale) {
        super();
        locale_ = locale;
        language_ = locale.getLanguage();
        if (!locale.getScript().isEmpty()) {
            script_ = locale.getScript();
        }
        if (!locale.getCountry().isEmpty()) {
            region_ = locale.getCountry();
        }
        if (locale.hasExtensions()) {
            calendar_ = locale.getUnicodeLocaleType("ca");
            collation_ = locale.getUnicodeLocaleType("co");
            numberingSystem_ = locale.getUnicodeLocaleType("nu");
            caseFirst_ = locale.getUnicodeLocaleType("kf");
            hourCycle_ = locale.getUnicodeLocaleType("hc");
            numeric_ = Boolean.parseBoolean(locale.getUnicodeLocaleType("kn"));
        }
    }

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments
     * @param ctorObj the constructor function
     * @param inNewExpr whether called via new
     * @return the new Locale instance
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        if (args.length == 0 || JavaScriptEngine.isUndefined(args[0])) {
            throw JavaScriptEngine.typeError("Invalid element in locales argument");
        }

        final String languageTag = JavaScriptEngine.toString(args[0]);
        if (languageTag.isEmpty()) {
            throw JavaScriptEngine.rangeError("Invalid language tag: ");
        }

        java.util.Locale locale;
        try {
            locale = new java.util.Locale.Builder()
                    .setLanguageTag(languageTag)
                    .build();
        }
        catch (final IllformedLocaleException e) {
            throw JavaScriptEngine.rangeError("Invalid language tag: " + languageTag);
        }

        // Override by options if present
        if (args.length > 1 && !JavaScriptEngine.isUndefined(args[1])) {
            locale = overrideExistingWithOptions(locale, ScriptableObject.ensureScriptableObject(args[1]));
        }

        final Locale l = new Locale(locale);
        l.setParentScope(getTopLevelScope(ctorObj));
        l.setPrototype(((FunctionObject) ctorObj).getClassPrototype());
        return l;
    }

    private static java.util.Locale overrideExistingWithOptions(
            final java.util.Locale existing, final ScriptableObject options) {
        final java.util.Locale.Builder builder = new java.util.Locale.Builder().setLocale(existing);

        setStringOption(builder, options, "language");
        setStringOption(builder, options, "script");
        setStringOption(builder, options, "region");
        setUnicodeKeyword(builder, options, "calendar", "ca", null);
        setUnicodeKeyword(builder, options, "collation", "co", null);
        setUnicodeKeyword(builder, options, "numberingSystem", "nu", null);
        setUnicodeKeyword(builder, options, "caseFirst", "kf", ALLOWED_CASE_FIRSTS);
        setUnicodeKeyword(builder, options, "hourCycle", "hc", ALLOWED_HOUR_CYCLES);

        final Object numeric = ScriptableObject.getProperty(options, "numeric");
        if (numeric != Scriptable.NOT_FOUND && !JavaScriptEngine.isUndefined(numeric)) {
            final boolean isNumeric = numeric instanceof Boolean ? (Boolean) numeric : true;
            builder.setUnicodeLocaleKeyword("kn", Boolean.toString(isNumeric));
        }

        return builder.build();
    }

    private static void setStringOption(final java.util.Locale.Builder builder,
            final ScriptableObject options, final String optionName) {
        final Object value = ScriptableObject.getProperty(options, optionName);
        if (value == Scriptable.NOT_FOUND || JavaScriptEngine.isUndefined(value)) {
            return;
        }
        try {
            final String s = JavaScriptEngine.toString(value);
            switch (optionName) {
                case "language":
                    builder.setLanguage(s);
                    break;
                case "script":
                    builder.setScript(s);
                    break;
                case "region":
                    builder.setRegion(s);
                    break;
                default:
                    break;
            }
        }
        catch (final Exception e) {
            throw JavaScriptEngine.rangeError("Invalid value for option \"" + optionName + "\"");
        }
    }

    private static void setUnicodeKeyword(final java.util.Locale.Builder builder,
            final ScriptableObject options, final String optionName, final String unicodeKey,
            final List<String> allowedValues) {
        final Object value = ScriptableObject.getProperty(options, optionName);
        if (value == Scriptable.NOT_FOUND || JavaScriptEngine.isUndefined(value)) {
            return;
        }
        final String s;
        try {
            s = JavaScriptEngine.toString(value);
        }
        catch (final Exception e) {
            throw JavaScriptEngine.rangeError("Invalid value for option \"" + optionName + "\"");
        }
        if (allowedValues != null && !allowedValues.contains(s)) {
            throw JavaScriptEngine.rangeError("Invalid value for option \"" + optionName + "\"");
        }
        builder.setUnicodeLocaleKeyword(unicodeKey, s);
    }

    /**
     * @return the language
     */
    @JsxGetter
    public Object getLanguage() {
        return language_ != null ? language_ : JavaScriptEngine.UNDEFINED;
    }

    /**
     * @return the script
     */
    @JsxGetter
    public Object getScript() {
        return script_ != null ? script_ : JavaScriptEngine.UNDEFINED;
    }

    /**
     * @return the region
     */
    @JsxGetter
    public Object getRegion() {
        return region_ != null ? region_ : JavaScriptEngine.UNDEFINED;
    }

    /**
     * @return the calendar type
     */
    @JsxGetter
    public Object getCalendar() {
        return calendar_ != null ? calendar_ : JavaScriptEngine.UNDEFINED;
    }

    /**
     * @return the collation type
     */
    @JsxGetter
    public Object getCollation() {
        return collation_ != null ? collation_ : JavaScriptEngine.UNDEFINED;
    }

    /**
     * @return the numbering system
     */
    @JsxGetter
    public Object getNumberingSystem() {
        return numberingSystem_ != null ? numberingSystem_ : JavaScriptEngine.UNDEFINED;
    }

    /**
     * @return the case first setting
     */
    @JsxGetter
    public Object getCaseFirst() {
        return caseFirst_ != null ? caseFirst_ : JavaScriptEngine.UNDEFINED;
    }

    /**
     * @return the hour cycle
     */
    @JsxGetter
    public Object getHourCycle() {
        return hourCycle_ != null ? hourCycle_ : JavaScriptEngine.UNDEFINED;
    }

    /**
     * @return whether numeric sorting is used
     */
    @JsxGetter
    public boolean isNumeric() {
        return numeric_;
    }

    /**
     * @return the base name (without Unicode extensions)
     */
    @JsxGetter
    public Object getBaseName() {
        final String variant = locale_.getVariant().replace("_", "-");
        return language_
                + (script_ != null ? "-" + script_ : "")
                + (region_ != null ? "-" + region_ : "")
                + (!variant.isEmpty() ? "-" + variant : "");
    }

    /**
     * Returns a Locale with maximized subtags.
     * @return a new Locale instance with maximized subtags
     */
    @JsxFunction
    public Locale maximize() {
        final String region;
        if (region_ != null) {
            region = region_;
        }
        else {
            final java.util.List<java.util.Locale> locales =
                    LocaleUtils.countriesByLanguage(language_);
            if (!locales.isEmpty()) {
                region = locales.get(0).getCountry();
            }
            else {
                region = null;
            }
        }

        final java.util.Locale locale = new java.util.Locale.Builder()
                .setLanguage(language_)
                .setScript(script_)
                .setRegion(region)
                .setExtension('u', locale_.getExtension('u'))
                .build();

        final Locale l = new Locale(locale);
        l.setParentScope(getWindow(this));
        l.setPrototype(this.getPrototype());
        return l;
    }

    /**
     * Returns a Locale with minimized subtags.
     * @return a new Locale instance with minimized subtags
     */
    @JsxFunction
    public Locale minimize() {
        final java.util.Locale locale = new java.util.Locale.Builder()
                .setLanguage(language_)
                .setExtension('u', locale_.getExtension('u'))
                .build();

        final Locale l = new Locale(locale);
        l.setParentScope(getWindow(this));
        l.setPrototype(this.getPrototype());
        return l;
    }

    /**
     * @return the locale's Unicode locale identifier string
     */
    @JsxFunction(functionName = "toString")
    public String jsToString() {
        if (locale_ == null) {
            return super.toString();
        }
        return locale_.toLanguageTag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (getPrototype() != null && (String.class.equals(hint) || hint == null)) {
            return jsToString();
        }
        return super.getDefaultValue(hint);
    }
}
