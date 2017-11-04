/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * Contains some missing features of Rhino NativeObject.
 *
 * @author Ahmed Ashour
 */
public final class ObjectCustom {

    private ObjectCustom() { }

    /**
     * Returns an array of all symbol properties found directly upon a given object.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return array
     */
    public static Scriptable getOwnPropertySymbols(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        if (args.length == 0) {
            throw ScriptRuntime.typeError("Cannot convert undefined or null to object");
        }
        if (!(args[0] instanceof ScriptableObject)) {
            throw ScriptRuntime.typeError("Cannot convert " + Context.toString(args[0]) + " to object");
        }
        final ScriptableObject o = (ScriptableObject) args[0];
        final List<Object> list = new ArrayList<>();
        for (final Object id : o.getAllIds()) {
            if (id.toString().startsWith("Symbol(")) {
                list.add(id);
            }
        }
        return context.newArray(thisObj, list.toArray(new Object[list.size()]));
    }
}
