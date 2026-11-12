/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript;

import java.io.Serializable;

import org.htmlunit.corejs.javascript.Delegator;
import org.htmlunit.corejs.javascript.Scriptable;

/**
 * Proxy for a {@link HtmlUnitScriptable}.
 *
 * @param <T> the type of scriptable object being wrapped
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ronald Brill
 */
public abstract class HtmlUnitScriptableProxy<T extends HtmlUnitScriptable> extends Delegator
        implements Serializable {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        return getDelegee().get(index, unwrap(start));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        return getDelegee().get(name, unwrap(start));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final int index, final Scriptable start) {
        return getDelegee().has(index, unwrap(start));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        return getDelegee().has(name, unwrap(start));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasInstance(final Scriptable instance) {
        return getDelegee().hasInstance(unwrap(instance));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final int index, final Scriptable start, final Object value) {
        getDelegee().put(index, unwrap(start), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        getDelegee().put(name, unwrap(start), value);
    }

    /**
     * Delegates call to delegee.
     * @param hint the type hint
     * @return the default value
     *
     * @see org.htmlunit.corejs.javascript.Scriptable#getDefaultValue
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        return getDelegee().getDefaultValue(hint);
    }

    /**
     * Unwraps the given scriptable if it is a proxy.
     *
     * @param scriptable the scriptable to unwrap
     * @return the delegee if the given scriptable is a proxy; the scriptable itself otherwise
     */
    private static Scriptable unwrap(final Scriptable scriptable) {
        if (scriptable instanceof HtmlUnitScriptableProxy<?> proxy) {
            return proxy.getDelegee();
        }
        return scriptable;
    }
}
