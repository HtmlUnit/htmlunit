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
package com.gargoylesoftware.htmlunit.javascript;

import java.io.Serializable;

import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

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
    public Object get(final int index, Scriptable start) {
        if (start instanceof HtmlUnitScriptableProxy<?>) {
            start = ((HtmlUnitScriptableProxy<?>) start).getDelegee();
        }
        return getDelegee().get(index, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, Scriptable start) {
        if (start instanceof HtmlUnitScriptableProxy<?>) {
            start = ((HtmlUnitScriptableProxy<?>) start).getDelegee();
        }
        return getDelegee().get(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final int index, Scriptable start) {
        if (start instanceof HtmlUnitScriptableProxy<?>) {
            start = ((HtmlUnitScriptableProxy<?>) start).getDelegee();
        }
        return getDelegee().has(index, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final String name, Scriptable start) {
        if (start instanceof HtmlUnitScriptableProxy<?>) {
            start = ((HtmlUnitScriptableProxy<?>) start).getDelegee();
        }
        return getDelegee().has(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasInstance(Scriptable instance) {
        if (instance instanceof HtmlUnitScriptableProxy<?>) {
            instance = ((HtmlUnitScriptableProxy<?>) instance).getDelegee();
        }
        return getDelegee().hasInstance(instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final int index, Scriptable start, final Object value) {
        if (start instanceof HtmlUnitScriptableProxy<?>) {
            start = ((HtmlUnitScriptableProxy<?>) start).getDelegee();
        }
        getDelegee().put(index, start, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, Scriptable start, final Object value) {
        if (start instanceof HtmlUnitScriptableProxy<?>) {
            start = ((HtmlUnitScriptableProxy<?>) start).getDelegee();
        }
        getDelegee().put(name, start, value);
    }

    /**
     * Delegates call to delegee.
     * @param hint the type hint
     * @return the default value
     *
     * @see net.sourceforge.htmlunit.corejs.javascript.Scriptable#getDefaultValue
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        return getDelegee().getDefaultValue(hint);
    }
}
