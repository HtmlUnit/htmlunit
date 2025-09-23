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
package org.htmlunit.javascript.host.intl;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;

import java.text.BreakIterator;
import java.util.Locale;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.NativeArray;
import org.htmlunit.corejs.javascript.NativeObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.host.Window;

/**
 * A JavaScript object for {@code V8BreakIterator}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Sven Strickroth
 */
@JsxClass(value = {CHROME, EDGE}, className = "v8BreakIterator")
public class V8BreakIterator extends HtmlUnitScriptable {

    private transient BreakIterator breakIterator_;
    private String text_;
    private boolean typeAlwaysNone_;

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments to the WebSocket constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        Locale locale = new Locale.Builder().setLanguage("en").setRegion("US").build();
        if (args.length != 0) {
            final Object locales = args[0];
            if (locales instanceof NativeArray) {
                if (((NativeArray) locales).getLength() != 0) {
                    locale = new Locale.Builder().setLanguage(((NativeArray) locales).get(0).toString()).build();
                }
            }
            else if (locales instanceof String) {
                locale = new Locale.Builder().setLanguage(locales.toString()).build();
            }
            else if (!JavaScriptEngine.isUndefined(locales)) {
                throw JavaScriptEngine.throwAsScriptRuntimeEx(
                        new Exception("Unknown type " + locales.getClass().getName()));
            }
        }

        final V8BreakIterator iterator = new V8BreakIterator();
        if (args.length > 1) {
            final Object types = args[1];
            if (types instanceof NativeObject) {
                final Object obj = ((NativeObject) types).get("type", (NativeObject) types);
                if ("character".equals(obj)) {
                    iterator.breakIterator_ = BreakIterator.getCharacterInstance(locale);
                    iterator.typeAlwaysNone_ = true;
                }
                else if ("line".equals(obj)) {
                    iterator.breakIterator_ = BreakIterator.getLineInstance(locale);
                    iterator.typeAlwaysNone_ = true;
                }
                else if ("sentence".equals(obj)) {
                    iterator.breakIterator_ = BreakIterator.getSentenceInstance(locale);
                    iterator.typeAlwaysNone_ = true;
                }
            }
        }
        if (iterator.breakIterator_ == null) {
            iterator.breakIterator_ = BreakIterator.getWordInstance(locale);
        }

        final Window window = getWindow(ctorObj);
        iterator.setParentScope(window);
        iterator.setPrototype(((FunctionObject) ctorObj).getClassPrototype());
        return iterator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        return getClassName();
    }

    /**
     * Returns the resolved options.
     * @return the options
     */
    @JsxFunction
    public Object resolvedOptions() {
        return Context.getCurrentContext().evaluateString(getParentScope(),
                "var x = {locale: 'en-US'}; x", "", -1, null);
    }

    /**
     * Returns the index of the first break and moves pointer to it.
     * @return the index of the first break
     */
    @JsxFunction
    public int first() {
        return breakIterator_.first();
    }

    /**
     * Returns the index of the next break and moves pointer to it.
     * @return the index of the next break
     */
    @JsxFunction
    public int next() {
        return breakIterator_.next();
    }

    /**
     * Returns the index of the current break.
     * @return the index of the current break
     */
    @JsxFunction
    public int current() {
        return breakIterator_.current();
    }

    /**
     * Assigns text to be segmented to the iterator.
     * @param text the text
     */
    @JsxFunction
    public void adoptText(final String text) {
        text_ = text;
        breakIterator_.setText(text);
    }

    /**
     * Returns the type of the break.
     * @return {@code none}, {@code number}, {@code letter}, {@code kana}, {@code ideo} or {@code unknown}
     */
    @JsxFunction
    public String breakType() {
        if (!typeAlwaysNone_) {
            final int current = current();
            final int previous = breakIterator_.previous();
            if (previous == BreakIterator.DONE) {
                first();
            }
            else {
                next();
            }
            if (current != BreakIterator.DONE && previous != BreakIterator.DONE) {
                final String token = text_.substring(previous, current);
                if (token.matches(".*[a-zA-Z]+.*")) {
                    return "letter";
                }
                if (token.matches("\\d+")) {
                    return "number";
                }
            }
        }
        return "none";
    }

}
