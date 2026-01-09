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
 * A JavaScript object for {@code Plugin}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 *
 * @see <a href="http://www.xulplanet.com/references/objref/MimeTypeArray.html">XUL Planet</a>
 */
@JsxClass
public class Plugin extends HtmlUnitScriptable {
    private String description_;
    private String filename_;
    private String name_;

    private final List<MimeType> elements_ = new ArrayList<>();

    /**
     * Creates an instance.
     */
    public Plugin() {
        super();
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Ctor initializing fields.
     *
     * @param name the plugin name
     * @param description the plugin description
     * @param filename the plugin filename
     */
    public Plugin(final String name, final String description, final String filename) {
        super();
        name_ = name;
        description_ = description;
        filename_ = filename;
    }

    /**
     * Returns the item at the given index.
     * @param index the index
     * @return the item at the given position
     */
    @JsxFunction
    public MimeType item(final int index) {
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
        final MimeType response = namedItem(name);
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
    public final Object get(final int index, final Scriptable start) {
        final Plugin plugin = (Plugin) start;
        final List<MimeType> elements = plugin.elements_;

        if (index >= 0 && index < elements.size()) {
            return elements.get(index);
        }
        return NOT_FOUND;
    }

    /**
     * Returns the item at the given index.
     * @param name the item name
     * @return the item with the given name
     */
    @JsxFunction
    public MimeType namedItem(final String name) {
        for (final MimeType element : elements_) {
            if (name.equals(element.getType())) {
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
     * Adds an element.
     * @param element the element to add
     */
    void add(final MimeType element) {
        elements_.add(element);
    }

    /**
     * Gets the plugin's description.
     * @return the description
     */
    @JsxGetter
    public String getDescription() {
        return description_;
    }

    /**
     * Gets the plugin's file name.
     * @return the file name
     */
    @JsxGetter
    public String getFilename() {
        return filename_;
    }

    /**
     * Gets the plugin's name.
     * @return the name
     */
    @JsxGetter
    public String getName() {
        return name_;
    }

    /**
     * @return the Iterator symbol
     */
    @JsxSymbol
    public Scriptable iterator() {
        return JavaScriptEngine.newArrayIteratorTypeValues(getParentScope(), this);
    }
}
