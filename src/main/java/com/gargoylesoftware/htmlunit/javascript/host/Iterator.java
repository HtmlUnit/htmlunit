/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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

import java.util.function.Consumer;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ES6Iterator;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.SymbolKey;
import net.sourceforge.htmlunit.corejs.javascript.SymbolScriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code Iterator}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class Iterator extends SimpleScriptable {

    private final java.util.Iterator<?> iterator_;

    /**
     * Constructs a new instance.
     *
     * @param className the class name
     * @param iterator the wrapped iterator.
     */
    public Iterator(final String className, final java.util.Iterator<?> iterator) {
        setClassName(className);
        iterator_ = iterator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParentScope(final Scriptable scope) {
        super.setParentScope(scope);
        try {
            final FunctionObject functionObject = new FunctionObject("next", getClass().getDeclaredMethod("next"),
                    scope);
            defineProperty("next", functionObject, ScriptableObject.DONTENUM);
        }
        catch (final Exception e) {
            Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Returns the next object.
     * @return the next object
     */
    public SimpleScriptable next() {
        final SimpleScriptable object = new SimpleScriptable();
        object.setParentScope(getParentScope());
        final Object value;
        final boolean done;
        if (iterator_ != null && iterator_.hasNext()) {
            final Object next = iterator_.next();
            if (next instanceof java.util.Map.Entry) {
                final java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) next;
                final NativeArray array = new NativeArray(new Object[] {entry.getKey(), entry.getValue()}) {

                    @Override
                    public Object getDefaultValue(final Class<?> hint) {
                        return Context.toString(entry.getKey()) + ',' + Context.toString(entry.getValue());
                    }
                };
                value = array;
            }
            else {
                value = next;
            }
            done = false;
        }
        else {
            value = Undefined.instance;
            done = true;
        }
        object.defineProperty("done", done, ScriptableObject.EMPTY);
        object.defineProperty("value", value, ScriptableObject.EMPTY);
        return object;
    }

    /**
     * Helper to support objects implementing the iterator interface.
     *
     * @param context the Context
     * @param thisObj the this Object
     * @param scriptable the scriptable
     * @param processor the method to be called for every object
     * @return true if @@iterator property was available
     */
    public static boolean iterate(final Context context,
            final Scriptable thisObj,
            final Scriptable scriptable,
            final Consumer<Object> processor) {

        if (scriptable instanceof ES6Iterator) {
            ScriptableObject next = (ScriptableObject) ScriptableObject.callMethod(scriptable, "next", null);
            boolean done = (boolean) next.get(ES6Iterator.DONE_PROPERTY);
            Object value = next.get(ES6Iterator.VALUE_PROPERTY);

            while (!done) {
                processor.accept(value);

                next = (ScriptableObject) ScriptableObject.callMethod(scriptable, "next", null);
                done = (boolean) next.get(ES6Iterator.DONE_PROPERTY);
                value = next.get(ES6Iterator.VALUE_PROPERTY);
            }
            return true;
        }

        if (!(scriptable instanceof SymbolScriptable)) {
            return false;
        }

        final Object iterator = ScriptableObject.getProperty(scriptable, SymbolKey.ITERATOR);
        if (iterator == Scriptable.NOT_FOUND) {
            return false;
        }

        final Object obj = ((BaseFunction) iterator).call(context, thisObj.getParentScope(), scriptable, new Object[0]);

        if (obj instanceof Iterator) {
            final Iterator it = (Iterator) obj;
            SimpleScriptable next = it.next();
            boolean done = (boolean) next.get(ES6Iterator.DONE_PROPERTY);
            Object value = next.get(ES6Iterator.VALUE_PROPERTY);

            while (!done) {
                processor.accept(value);

                next = it.next();
                done = (boolean) next.get(ES6Iterator.DONE_PROPERTY);
                value = next.get(ES6Iterator.VALUE_PROPERTY);
            }
            return true;
        }
        if (obj instanceof Scriptable) {
            // handle user defined iterator
            final Scriptable scriptableIterator = (Scriptable) obj;
            final Object nextFunct = ScriptableObject.getProperty(scriptableIterator, ES6Iterator.NEXT_METHOD);
            if (!(nextFunct instanceof BaseFunction)) {
                throw ScriptRuntime.typeError("undefined is not a function");
            }
            final Object nextObj = ((BaseFunction) nextFunct)
                    .call(context, thisObj.getParentScope(), scriptableIterator, new Object[0]);

            ScriptableObject next = (ScriptableObject) nextObj;
            boolean done = (boolean) next.get(ES6Iterator.DONE_PROPERTY);
            Object value = next.get(ES6Iterator.VALUE_PROPERTY);

            while (!done) {
                processor.accept(value);

                next = (ScriptableObject) ((BaseFunction) nextFunct)
                        .call(context, thisObj.getParentScope(), scriptableIterator, new Object[0]);
                done = (boolean) next.get(ES6Iterator.DONE_PROPERTY);
                value = next.get(ES6Iterator.VALUE_PROPERTY);
            }
            return true;
        }
        return false;
    }
}
