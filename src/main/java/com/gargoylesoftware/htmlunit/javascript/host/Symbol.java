/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A JavaScript object for {@code Symbol}.
 *
 * @author Ahmed Ashour
 */
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 38), @WebBrowser(EDGE) })
public class Symbol extends SimpleScriptable {

    private static final String INTERNAL_PREFIX = "~~Internal~~";

    static final String ITERATOR_STRING = INTERNAL_PREFIX + "Symbol(Symbol.iterator)";

    private String name_;

    /**
     * Default constructor.
     */
    public Symbol() {
    }

    /**
     * Creates an instance.
     * @param name the name
     */
    @JsxConstructor
    public Symbol(final String name) {
        name_ = name;
    }

    /**
     * Returns the {@code iterator} static property.
     * @param thisObj the scriptable
     * @return the {@code iterator} static property
     */
    @JsxStaticGetter
    public static Symbol getIterator(final Scriptable thisObj) {
        final Symbol symbol = new Symbol("iterator");
        final SimpleScriptable scope = (SimpleScriptable) thisObj.getParentScope();
        symbol.setParentScope(scope);
        symbol.setPrototype(scope.getPrototype(symbol.getClass()));
        return symbol;
    }

    /**
     * Returns the {@code unscopables} static property.
     * @param thisObj the scriptable
     * @return the {@code unscopables} static property
     */
    @JsxStaticGetter(@WebBrowser(CHROME))
    public static Symbol getUnscopables(final Scriptable thisObj) {
        final Symbol symbol = new Symbol("unscopables");
        final SimpleScriptable scope = (SimpleScriptable) thisObj.getParentScope();
        symbol.setParentScope(scope);
        symbol.setPrototype(scope.getPrototype(symbol.getClass()));
        return symbol;
    }

    /**
     * Returns the {@code isConcatSpreadable} static property.
     * @param thisObj the scriptable
     * @return the {@code isConcatSpreadable} static property
     */
    @JsxStaticGetter(@WebBrowser(CHROME))
    public static Symbol getIsConcatSpreadable(final Scriptable thisObj) {
        final Symbol symbol = new Symbol("isConcatSpreadable");
        final SimpleScriptable scope = (SimpleScriptable) thisObj.getParentScope();
        symbol.setParentScope(scope);
        symbol.setPrototype(scope.getPrototype(symbol.getClass()));
        return symbol;
    }

    /**
     * Returns the {@code toPrimitive} static property.
     * @param thisObj the scriptable
     * @return the {@code toPrimitive} static property
     */
    @JsxStaticGetter(@WebBrowser(CHROME))
    public static Symbol getToPrimitive(final Scriptable thisObj) {
        final Symbol symbol = new Symbol("toPrimitive");
        final SimpleScriptable scope = (SimpleScriptable) thisObj.getParentScope();
        symbol.setParentScope(scope);
        symbol.setPrototype(scope.getPrototype(symbol.getClass()));
        return symbol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeOf() {
        return "symbol";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction
    public String toString() {
        return "Symbol(Symbol." + name_ + ')';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        final StackTraceElement stackElement = new Throwable().getStackTrace()[3];
        new Throwable().printStackTrace();
        if (("getObjectElem".equals(stackElement.getMethodName())
                || "getElemFunctionAndThis".equals(stackElement.getMethodName()))
                    && ScriptRuntime.class.getName().equals(stackElement.getClassName())) {
            return INTERNAL_PREFIX + toString();
        }
        throw Context.reportRuntimeError("TypeError: Cannot convert a Symbol value to a string");
    }
}
