/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.ScriptRuntime;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.Symbol;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxStaticFunction;

/**
 * A JavaScript object for {@code Reflect}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
@JsxClass({CHROME, EDGE, FF, FF_ESR})
public class Reflect extends HtmlUnitScriptable {

    /**
     * Creates an instance.
     */
    public Reflect() {
        try {
            final FunctionObject hasFunctionObject = new FunctionObject("has",
                    getClass().getDeclaredMethod("has", Scriptable.class, String.class), this);
            defineProperty("has", hasFunctionObject, ScriptableObject.DONTENUM);

            final FunctionObject ownKeysFunctionObject = new FunctionObject("ownKeys",
                    getClass().getDeclaredMethod("ownKeys", Scriptable.class), this);
            defineProperty("ownKeys", ownKeysFunctionObject, ScriptableObject.DONTENUM);
        }
        catch (final Exception e) {
            Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * The static Reflect.has() method works like the in operator as a function.
     *
     * @param target The target object in which to look for the property.
     * @param propertyKey The name of the property to check.
     * @return true or false
     */
    public boolean has(final Scriptable target, final String propertyKey) {
        return ScriptableObject.hasProperty(target, propertyKey);
    }

    /**
     * Implements the ownKeys() static method as documented in docs.
     * @see https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Reflect/ownKeys
     */
    @JsxStaticFunction
    public Scriptable ownKeys(final Scriptable target) {
        final ScriptableObject obj = ensureScriptableObject(target);
        final List<Object> strings = new ArrayList<>();
        final List<Object> symbols = new ArrayList<>();

        for (Object o : obj.getAllIdsIncludingSymbols()) {
            if (o instanceof Symbol) {
                symbols.add(o);
            } else {
                strings.add(ScriptRuntime.toString(o));
            }
        }

        Object[] keys = new Object[strings.size() + symbols.size()];
        System.arraycopy(strings.toArray(), 0, keys, 0, strings.size());
        System.arraycopy(symbols.toArray(), 0, keys, strings.size(), symbols.size());

        return Context.getCurrentContext().newArray(this, keys);
    }
}
