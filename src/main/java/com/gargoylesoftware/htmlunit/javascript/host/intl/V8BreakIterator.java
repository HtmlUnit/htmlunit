/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.intl;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;

import java.text.BreakIterator;
import java.util.Locale;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code V8BreakIterator}.
 *
 * @author Ahmed Ashour
 */
@JsxClass({CHROME, EDGE})
public class V8BreakIterator extends SimpleScriptable {

    private transient BreakIterator breakIterator_;
    private String text_;
    private boolean typeAlwaysNone_;

    /**
     * The default constructor.
     */
    public V8BreakIterator() {
    }

    /**
     * The JavaScript constructor, with optional parameters.
     * @param locales the locales
     * @param types the types, can be {@code character}, {@code word}, {@code sentence} or {@code line},
     *        default is {@code word}
     */
    @JsxConstructor
    public V8BreakIterator(final Object locales, final Object types) {
        Locale locale = new Locale("en", "US");
        if (locales instanceof NativeArray) {
            if (((NativeArray) locales).getLength() != 0) {
                locale = new Locale(((NativeArray) locales).get(0).toString());
            }
        }
        else if (locales instanceof String) {
            locale = new Locale(locales.toString());
        }
        else if (!Undefined.isUndefined(locales)) {
            throw Context.throwAsScriptRuntimeEx(new Exception("Unknown type " + locales.getClass().getName()));
        }

        if (types instanceof NativeObject) {
            final Object obj = ((NativeObject) types).get("type", (NativeObject) types);
            if ("character".equals(obj)) {
                breakIterator_ = BreakIterator.getCharacterInstance(locale);
                typeAlwaysNone_ = true;
            }
            else if ("line".equals(obj)) {
                breakIterator_ = BreakIterator.getLineInstance(locale);
                typeAlwaysNone_ = true;
            }
            else if ("sentence".equals(obj)) {
                breakIterator_ = BreakIterator.getSentenceInstance(locale);
                typeAlwaysNone_ = true;
            }
        }
        if (breakIterator_ == null) {
            breakIterator_ = BreakIterator.getWordInstance(locale);
        }
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
                if (token.matches("[0-9]+")) {
                    return "number";
                }
            }
        }
        return "none";
    }

}
