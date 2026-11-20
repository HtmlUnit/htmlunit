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

import java.util.List;

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

    // the resolved locale; null for the prototype
    private String locale_;
    // created on demand (java.text.Collator is not serializable)
    private transient java.text.Collator collator_;
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
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final VarScope scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        final List<String> locales = Intl.canonicalizeLocaleList(cx, Intl.firstArgument(args));

        final Collator collator = new Collator();
        collator.setParentScope(scope);
        collator.setPrototype(((FunctionObject) ctorObj).getClassPrototype());

        // the first supported requested locale wins; the browser locale is the fallback
        String requestedLocale = Intl.lookupSupportedLocale(locales);
        if (requestedLocale == null) {
            requestedLocale = collator.getBrowserVersion().getBrowserLocale().toLanguageTag();
        }
        collator.locale_ = Intl.resolveLocale(requestedLocale, "co", "kf", "kn");
        return collator;
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
        final String x = JavaScriptEngine.toString(args.length > 0 ? args[0] : JavaScriptEngine.UNDEFINED);
        final String y = JavaScriptEngine.toString(args.length > 1 ? args[1] : JavaScriptEngine.UNDEFINED);
        return Integer.signum(getCollator().compare(x, y));
    }

    private java.text.Collator getCollator() {
        if (collator_ == null) {
            final java.text.Collator collator =
                    java.text.Collator.getInstance(java.util.Locale.forLanguageTag(locale_));
            // the default sensitivity 'variant' distinguishes base letters, accents and case
            collator.setStrength(java.text.Collator.TERTIARY);
            collator.setDecomposition(java.text.Collator.CANONICAL_DECOMPOSITION);
            collator_ = collator;
        }
        return collator_;
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
        // the options are not supported so far; these are the defaults
        options.put("usage", options, "sort");
        options.put("sensitivity", options, "variant");
        options.put("ignorePunctuation", options, Boolean.FALSE);
        options.put("collation", options, "default");
        options.put("numeric", options, Boolean.FALSE);
        options.put("caseFirst", options, "false");
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
