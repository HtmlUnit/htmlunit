/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Wrapper for a {@link Function} delegating all calls to the wrapped instance.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
class FunctionWrapper implements Function {
    private final Function wrapped_;

    FunctionWrapper(final Function wrapped) {
        wrapped_ = wrapped;
    }

    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        return wrapped_.call(cx, scope, thisObj, args);
    }

    public String getClassName() {
        return wrapped_.getClassName();
    }

    public Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        return wrapped_.construct(cx, scope, args);
    }

    public Object get(final String name, final Scriptable start) {
        return wrapped_.get(name, start);
    }

    public Object get(final int index, final Scriptable start) {
        return wrapped_.get(index, start);
    }

    public boolean has(final String name, final Scriptable start) {
        return wrapped_.has(name, start);
    }

    public boolean has(final int index, final Scriptable start) {
        return wrapped_.has(index, start);
    }

    public void put(final String name, final Scriptable start, final Object value) {
        wrapped_.put(name, wrapped_, value);
    }

    public void put(final int index, final Scriptable start, final Object value) {
        wrapped_.put(index, wrapped_, value);
    }

    public void delete(final String name) {
        wrapped_.delete(name);
    }

    public void delete(final int index) {
        wrapped_.delete(index);
    }

    public Scriptable getPrototype() {
        return wrapped_.getPrototype();
    }

    public void setPrototype(final Scriptable prototype) {
        wrapped_.setPrototype(prototype);
    }

    public Scriptable getParentScope() {
        return wrapped_.getParentScope();
    }

    public void setParentScope(final Scriptable parent) {
        wrapped_.setParentScope(parent);
    }

    public Object[] getIds() {
        return wrapped_.getIds();
    }

    public Object getDefaultValue(final Class<?> hint) {
        return wrapped_.getDefaultValue(hint);
    }

    public boolean hasInstance(final Scriptable instance) {
        return wrapped_.hasInstance(instance);
    }

}
