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
 * JavaScript host object for {@code MimeTypeArray}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/MimeTypeArray">MDN Documentation</a>
 */
@JsxClass
public class MimeTypeArray extends HtmlUnitScriptable {

    private final List<MimeType> elements_ = new ArrayList<>();

    /**
     * Creates an instance of this object.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Returns the MIME type string for the given element.
     *
     * @param element a {@link MimeType}
     * @return the MIME type string
     */
    protected String getItemName(final Object element) {
        return ((MimeType) element).getType();
    }

    /**
     * Returns the {@link MimeType} at the given index.
     *
     * @param index the index
     * @return the {@link MimeType} at the given position
     */
    @JsxFunction
    public MimeType item(final int index) {
        return elements_.get(index);
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
     * {@inheritDoc}
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        if (NOT_FOUND != getWithPreemption(name)) {
            return true;
        }

        return super.has(name, start);
    }

    /**
     * Returns the element at the specified index, or {@code null} if the index is invalid.
     * {@inheritDoc}
     */
    @Override
    public final MimeType get(final int index, final Scriptable start) {
        final MimeTypeArray array = (MimeTypeArray) start;
        final List<MimeType> elements = array.elements_;

        if (index >= 0 && index < elements.size()) {
            return elements.get(index);
        }
        return null;
    }

    /**
     * Returns the {@link MimeType} with the given name.
     *
     * @param name the MIME type string to look up
     * @return the matching {@link MimeType}, or {@code null} if not found
     */
    @JsxFunction
    public MimeType namedItem(final String name) {
        for (final MimeType element : elements_) {
            if (name.equals(getItemName(element))) {
                return element;
            }
        }
        return null;
    }

    /**
     * Returns the number of MIME types in this array.
     *
     * @return the number of elements
     */
    @JsxGetter
    public int getLength() {
        return elements_.size();
    }

    /**
     * Adds a {@link MimeType} to this array.
     *
     * @param element the element to add
     */
    void add(final MimeType element) {
        elements_.add(element);
    }

    /**
     * Returns an iterator over the values in this array.
     *
     * @return the iterator
     */
    @JsxSymbol
    public Scriptable iterator() {
        return JavaScriptEngine.newArrayIteratorTypeValues(getParentScope(), this);
    }
}
