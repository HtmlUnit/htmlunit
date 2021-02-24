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
package com.gargoylesoftware.htmlunit.javascript;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Wrapper for a {@link Function} delegating all calls to the wrapped instance.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class FunctionWrapper implements Function {
    private final Function wrapped_;

    /**
     * Constructs a new instance.
     * @param wrapped the wrapped function
     */
    public FunctionWrapper(final Function wrapped) {
        wrapped_ = wrapped;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        return wrapped_.call(cx, scope, thisObj, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return wrapped_.getClassName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        return wrapped_.construct(cx, scope, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        return wrapped_.get(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        return wrapped_.get(index, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        return wrapped_.has(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final int index, final Scriptable start) {
        return wrapped_.has(index, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        wrapped_.put(name, wrapped_, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final int index, final Scriptable start, final Object value) {
        wrapped_.put(index, wrapped_, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final String name) {
        wrapped_.delete(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final int index) {
        wrapped_.delete(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scriptable getPrototype() {
        return wrapped_.getPrototype();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrototype(final Scriptable prototype) {
        wrapped_.setPrototype(prototype);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scriptable getParentScope() {
        return wrapped_.getParentScope();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParentScope(final Scriptable parent) {
        wrapped_.setParentScope(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getIds() {
        return wrapped_.getIds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        return wrapped_.getDefaultValue(hint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasInstance(final Scriptable instance) {
        return wrapped_.hasInstance(instance);
    }
}
