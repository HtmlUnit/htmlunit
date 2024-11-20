/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSymbol;

/**
 * A JavaScript object for {@code PluginArray}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/PluginArray">PluginArray</a>
 */
@JsxClass
public class PluginArray extends HtmlUnitScriptable {

    private final List<Plugin> elements_ = new ArrayList<>();

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Returns the item at the given index.
     * @param index the index
     * @return the item at the given position
     */
    @JsxFunction
    public Plugin item(final int index) {
        if (index >= 0 && index < elements_.size()) {
            return elements_.get(index);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getWithPreemption(final String name) {
        final Plugin response = namedItem(name);
        if (response != null) {
            return response;
        }
        return NOT_FOUND;
    }

    /**
     * Returns the element at the specified index, or {@code null} if the index is invalid.
     * {@inheritDoc}
     */
    @Override
    public final Plugin get(final int index, final Scriptable start) {
        final PluginArray array = (PluginArray) start;
        final List<Plugin> elements = array.elements_;

        if (index >= 0 && index < elements.size()) {
            return elements.get(index);
        }
        return null;
    }

    /**
     * Returns the item at the given index.
     * @param name the item name
     * @return the item with the given name
     */
    @JsxFunction
    public Plugin namedItem(final String name) {
        for (final Plugin element : elements_) {
            if (name.equals(getItemName(element))) {
                return element;
            }
        }
        return null;
    }

    /**
     * Gets the array size.
     * @return the number elements
     */
    @JsxGetter
    public int getLength() {
        return elements_.size();
    }

    /**
     * Current implementation does nothing.
     * @param reloadDocuments reload yes / no
     * @see <a href="http://www.xulplanet.com/references/objref/PluginArray.html#method_refresh">XUL Planet</a>
     */
    @JsxFunction
    public void refresh(final boolean reloadDocuments) {
        // nothing
    }

    /**
     * Gets the name of the plugin.
     * @param element a {@link Plugin}
     * @return the name
     */
    protected String getItemName(final Object element) {
        return ((Plugin) element).getName();
    }

    /**
     * Adds an element.
     * @param element the element to add
     */
    void add(final Plugin element) {
        elements_.add(element);
    }

    @JsxSymbol
    public Scriptable iterator() {
        return JavaScriptEngine.newArrayIteratorTypeValues(getParentScope(), this);
    }
}
