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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code Iterator}.
 *
 * @author Ahmed Ashour
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
    public Object next() {
        final SimpleScriptable object = new SimpleScriptable();
        object.setParentScope(getParentScope());
        final Object value;
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
        }
        else {
            value = Undefined.instance;
        }
        object.defineProperty("value", value, ScriptableObject.DONTENUM);
        return object;
    }

}
