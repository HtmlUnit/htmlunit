/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.RecursiveFunctionObject;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

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
     * @param args the arguments to the WebSocket constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final Object[] args, final Function ctorObj,
            final boolean inNewExpr) {
        final String[] locales;
        if (args.length != 0) {
            if (args[0] instanceof NativeArray) {
                final NativeArray array = (NativeArray) args[0];
                locales = new String[(int) array.getLength()];
                for (int i = 0; i < locales.length; i++) {
                    locales[i] = Context.toString(array.get(i));
                }
            }
            else {
                locales = new String[] {Context.toString(args[0])};
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
