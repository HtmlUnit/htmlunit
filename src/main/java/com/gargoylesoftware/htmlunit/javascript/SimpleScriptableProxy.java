/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import java.lang.reflect.Method;

import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * Proxy for an other {@link SimpleScriptable}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public abstract class SimpleScriptableProxy extends ScriptableObject
    implements ScriptableWithFallbackGetter, Function {
    private static final long serialVersionUID = -3836061858668746684L;

    /**
     * Gets the currently wrapped JavaScript object.
     * @return the JS object
     */
    public abstract SimpleScriptable getWrappedScriptable();

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final int index) {
        getWrappedScriptable().delete(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final String name) {
        getWrappedScriptable().delete(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, Scriptable start) {
        if (start instanceof SimpleScriptableProxy) {
            start = ((SimpleScriptableProxy) start).getWrappedScriptable();
        }
        return getWrappedScriptable().get(index, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, Scriptable start) {
        if (start instanceof SimpleScriptableProxy) {
            start = ((SimpleScriptableProxy) start).getWrappedScriptable();
        }
        return getWrappedScriptable().get(name, start);
    }

    /**
     * {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        final SimpleScriptable wrapped = getWrappedScriptable();
        if (wrapped instanceof ScriptableWithFallbackGetter) {
            return ((ScriptableWithFallbackGetter) wrapped).getWithFallback(name);
        }
        return NOT_FOUND;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return getWrappedScriptable().getClassName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class< ? > hint) {
        return getWrappedScriptable().getDefaultValue(hint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getIds() {
        return getWrappedScriptable().getIds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scriptable getParentScope() {
        return getWrappedScriptable().getParentScope();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scriptable getPrototype() {
        return getWrappedScriptable().getPrototype();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final int index, Scriptable start) {
        if (start instanceof SimpleScriptableProxy) {
            start = ((SimpleScriptableProxy) start).getWrappedScriptable();
        }
        return getWrappedScriptable().has(index, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final String name, Scriptable start) {
        if (start instanceof SimpleScriptableProxy) {
            start = ((SimpleScriptableProxy) start).getWrappedScriptable();
        }
        return getWrappedScriptable().has(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasInstance(Scriptable instance) {
        if (instance instanceof SimpleScriptableProxy) {
            instance = ((SimpleScriptableProxy) instance).getWrappedScriptable();
        }
        return getWrappedScriptable().hasInstance(instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final int index, Scriptable start, final Object value) {
        if (start instanceof SimpleScriptableProxy) {
            start = ((SimpleScriptableProxy) start).getWrappedScriptable();
        }
        getWrappedScriptable().put(index, start, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, Scriptable start, final Object value) {
        if (start instanceof SimpleScriptableProxy) {
            start = ((SimpleScriptableProxy) start).getWrappedScriptable();
        }
        getWrappedScriptable().put(name, start, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParentScope(final Scriptable parent) {
        getWrappedScriptable().setParentScope(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrototype(final Scriptable prototype) {
        getWrappedScriptable().setPrototype(prototype);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean avoidObjectDetection() {
        return getWrappedScriptable().avoidObjectDetection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defineConst(final String name, Scriptable start) {
        if (start instanceof SimpleScriptableProxy) {
            start = ((SimpleScriptableProxy) start).getWrappedScriptable();
        }
        getWrappedScriptable().defineConst(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defineFunctionProperties(final String[] names, final Class< ? > clazz, final int attributes) {
        getWrappedScriptable().defineFunctionProperties(names, clazz, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defineProperty(final String propertyName, final Class< ? > clazz, final int attributes) {
        getWrappedScriptable().defineProperty(propertyName, clazz, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defineProperty(final String propertyName, final Object value, final int attributes) {
        getWrappedScriptable().defineProperty(propertyName, value, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defineProperty(final String propertyName, final Object delegateTo,
            final Method getter, final Method setter, final int attributes) {
        getWrappedScriptable().defineProperty(propertyName, delegateTo, getter, setter,
                attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        return getWrappedScriptable().equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getAllIds() {
        return getWrappedScriptable().getAllIds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttributes(final int index) {
        return getWrappedScriptable().getAttributes(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttributes(final String name) {
        return getWrappedScriptable().getAttributes(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getGetterOrSetter(final String name, final int index, final boolean isSetter) {
        return getWrappedScriptable().getGetterOrSetter(name, index, isSetter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getWrappedScriptable().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConst(final String name) {
        return getWrappedScriptable().isConst(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putConst(final String name, final Scriptable start, final Object value) {
        getWrappedScriptable().putConst(name, start, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void sealObject() {
        getWrappedScriptable().sealObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributes(final int index, final int attributes) {
        getWrappedScriptable().setAttributes(index, attributes);
    }

    /**
     * {@inheritDoc}
     * @deprecated as of used Rhino version
     */
    @Override
    @Deprecated
    public void setAttributes(final int index, final Scriptable start, final int attributes) {
        getWrappedScriptable().setAttributes(index, start, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributes(final String name, final int attributes) {
        getWrappedScriptable().setAttributes(name, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGetterOrSetter(final String name, final int index,
            final Callable getterOrSetter, final boolean isSetter) {
        getWrappedScriptable().setGetterOrSetter(name, index, getterOrSetter, isSetter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getWrappedScriptable().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object equivalentValues(final Object value) {
        return ScriptRuntime.eq(getWrappedScriptable(), value);
    }

    /**
     * {@inheritDoc}
     */
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        final SimpleScriptable wrapped = getWrappedScriptable();
        if (wrapped instanceof Function) {
            return ((Function) wrapped).call(cx, scope, thisObj, args);
        }
        throw Context.reportRuntimeError(wrapped + " is not a function.");
    }

    /**
     * {@inheritDoc}
     */
    public final Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        final SimpleScriptable wrapped = getWrappedScriptable();
        if (wrapped instanceof Function) {
            return ((Function) wrapped).construct(cx, scope, args);
        }
        throw Context.reportRuntimeError(wrapped + " is not a function.");
    }

}
