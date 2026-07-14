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
 * JavaScript host object for {@code Plugin}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Plugin">MDN Documentation</a>
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
     * Creates an instance of this object.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Creates an instance with the given properties.
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
     * Returns the {@link MimeType} at the given index.
     *
     * @param index the index
     * @return the {@link MimeType} at the given position, or {@code null} if out of range
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
     * Returns the element at the specified index, or {@link #NOT_FOUND} if the index is invalid.
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
     * Returns the {@link MimeType} with the given MIME type string.
     *
     * @param name the MIME type string to look up
     * @return the matching {@link MimeType}, or {@code null} if not found
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
     * Returns the number of MIME types supported by this plugin.
     *
     * @return the number of elements
     */
    @JsxGetter
    public int getLength() {
        return elements_.size();
    }

    /**
     * Adds a {@link MimeType} to this plugin.
     *
     * @param element the element to add
     */
    void add(final MimeType element) {
        elements_.add(element);
    }

    /**
     * Returns the plugin description.
     *
     * @return the description
     */
    @JsxGetter
    public String getDescription() {
        return description_;
    }

    /**
     * Returns the plugin filename.
     *
     * @return the filename
     */
    @JsxGetter
    public String getFilename() {
        return filename_;
    }

    /**
     * Returns the plugin name.
     *
     * @return the name
     */
    @JsxGetter
    public String getName() {
        return name_;
    }

    /**
     * Returns an iterator over the values in this plugin.
     *
     * @return the iterator
     */
    @JsxSymbol
    public Scriptable iterator() {
        return JavaScriptEngine.newArrayIteratorTypeValues(getParentScope(), this);
    }
}
