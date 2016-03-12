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

import java.util.HashMap;
import java.util.Map.Entry;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code Symbol}.
 *
 * @author Ahmed Ashour
 */
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 38), @WebBrowser(EDGE) })
public class Symbol extends SimpleScriptable {

    static final String ITERATOR_STRING = "Symbol(Symbol.iterator)";
    private static java.util.Map<BrowserVersion, java.util.Map<String, Symbol>> SYMBOL_MAP_ = new HashMap<>();

    private Object name_;

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
    public Symbol(final Object name) {
        name_ = name;
        for (final StackTraceElement stackElements : new Throwable().getStackTrace()) {
            if (stackElements.getClassName().contains("BaseFunction")) {
                throw ScriptRuntime.typeError("Symbol is not a constructor");
            }
        }
    }

    /**
     * Returns the {@code iterator} static property.
     * @param thisObj the scriptable
     * @return the {@code iterator} static property
     */
    @JsxStaticGetter
    public static Symbol getIterator(final Scriptable thisObj) {
        return getSymbol(thisObj, "iterator");
    }

    private static Symbol getSymbol(final Scriptable thisObj, final String name) {
        final SimpleScriptable scope = (SimpleScriptable) thisObj.getParentScope();
        final BrowserVersion browserVersion = scope.getBrowserVersion();

        java.util.Map<String, Symbol> map = SYMBOL_MAP_.get(browserVersion);
        if (map == null) {
            map = new HashMap<>();
            SYMBOL_MAP_.put(browserVersion, map);
        }

        Symbol symbol = map.get(name);
        if (symbol == null) {
            symbol = new Symbol(name);
            symbol.setParentScope(scope);
            symbol.setPrototype(scope.getPrototype(symbol.getClass()));
            map.put(name, symbol);
        }

        return symbol;
    }

    /**
     * Returns the {@code unscopables} static property.
     * @param thisObj the scriptable
     * @return the {@code unscopables} static property
     */
    @JsxStaticGetter(@WebBrowser(CHROME))
    public static Symbol getUnscopables(final Scriptable thisObj) {
        return getSymbol(thisObj, "unscopables");
    }

    /**
     * Returns the {@code isConcatSpreadable} static property.
     * @param thisObj the scriptable
     * @return the {@code isConcatSpreadable} static property
     */
    @JsxStaticGetter(@WebBrowser(CHROME))
    public static Symbol getIsConcatSpreadable(final Scriptable thisObj) {
        return getSymbol(thisObj, "isConcatSpreadable");
    }

    /**
     * Returns the {@code toPrimitive} static property.
     * @param thisObj the scriptable
     * @return the {@code toPrimitive} static property
     */
    @JsxStaticGetter({ @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 45) })
    public static Symbol getToPrimitive(final Scriptable thisObj) {
        return getSymbol(thisObj, "toPrimitive");
    }

    /**
     * Returns the {@code toStringTag} static property.
     * @param thisObj the scriptable
     * @return the {@code toStringTag} static property
     */
    @JsxStaticGetter(@WebBrowser(CHROME))
    public static Symbol getToStringTag(final Scriptable thisObj) {
        return getSymbol(thisObj, "toStringTag");
    }

    /**
     * Returns the {@code match} static property.
     * @param thisObj the scriptable
     * @return the {@code match} static property
     */
    @JsxStaticGetter(@WebBrowser(value = FF, minVersion = 45))
    public static Symbol getMatch(final Scriptable thisObj) {
        return getSymbol(thisObj, "match");
    }

    /**
     * Returns the {@code species} static property.
     * @param thisObj the scriptable
     * @return the {@code species} static property
     */
    @JsxStaticGetter(@WebBrowser(value = FF, minVersion = 45))
    public static Symbol getSpecies(final Scriptable thisObj) {
        return getSymbol(thisObj, "species");
    }

    /**
     * Searches for existing symbols in a runtime-wide symbol registry with the given key and returns it if found.
     * Otherwise a new symbol gets created in the global symbol registry with this key.
     * @param context the context
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     * @return the symbol
     */
    @JsxStaticFunction(functionName = "for")
    public static Symbol forFunction(final Context context, final Scriptable thisObj, final Object[] args,
            final Function function) {
        final String key = Context.toString(args.length != 0 ? args[0] : Undefined.instance);

        Symbol symbol = (Symbol) ((ScriptableObject) thisObj).get(key);
        if (symbol == null) {
            final SimpleScriptable parentScope = (SimpleScriptable) thisObj.getParentScope();

            symbol = new Symbol(key);
            symbol.setParentScope(parentScope);
            symbol.setPrototype(parentScope.getPrototype(symbol.getClass()));
            thisObj.put(key, thisObj, symbol);
        }
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
        String name;
        if (name_ == Undefined.instance) {
            name = "";
        }
        else {
            name = Context.toString(name_);
            final ClassConfiguration config = AbstractJavaScriptConfiguration
                    .getClassConfiguration(getClass(), getBrowserVersion());

            for (final Entry<String, ClassConfiguration.PropertyInfo> propertyEntry
                    : config.getStaticPropertyEntries()) {
                if (propertyEntry.getKey().equals(name)) {
                    name = "Symbol." + name;
                    break;
                }
            }
        }
        return "Symbol(" + name + ')';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (String.class.equals(hint) || hint == null) {
            return toString();
        }
        return super.getDefaultValue(hint);
    }
}
