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
package org.htmlunit.javascript.host.intl;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.NativeArray;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.RecursiveFunctionObject;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.host.Window;

/**
 * A JavaScript object for {@code Collator}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class Collator extends HtmlUnitScriptable {

    /**
     * The default constructor.
     */
    public Collator() {
    }

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
        final String[] locales;
        if (args.length != 0) {
            if (args[0] instanceof NativeArray) {
                final NativeArray array = (NativeArray) args[0];
                locales = new String[(int) array.getLength()];
                for (int i = 0; i < locales.length; i++) {
                    locales[i] = JavaScriptEngine.toString(array.get(i));
                }
            }
            else {
                locales = new String[] {JavaScriptEngine.toString(args[0])};
            }
        }
        else {
            locales = new String[] {""};
        }
        final Window window = getWindow(ctorObj);
        final Collator format = new Collator(/*locales, window.getBrowserVersion()*/);
        format.setParentScope(window);
        format.setPrototype(((RecursiveFunctionObject) ctorObj).getClassPrototype());
        return format;
    }
}
