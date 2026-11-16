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
package org.htmlunit.javascript.host.dom;

import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.corejs.javascript.CompoundOperationMap;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.html.DomAttr;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for {@code DOMStringMap}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/DOMStringMap">MDN Documentation</a>
 */
@JsxClass
public final class DOMStringMap extends HtmlUnitScriptable {

    private static final String DATA_PREFIX = "data-";

    /**
     * Creates an instance.
     */
    public DOMStringMap() {
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
     * Creates an instance.
     * @param node the node which contains the underlying string
     */
    public DOMStringMap(final Node node) {
        super();
        setDomNode(node.getDomNodeOrDie(), false);
        setParentScope(node.getParentScope());
        setPrototype(getPrototype(getClass()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        final HtmlElement e = (HtmlElement) getDomNodeOrNull();
        if (e != null) {
            final String value = e.getAttribute(DATA_PREFIX + deCamelize(name));
            if (ATTRIBUTE_NOT_DEFINED != value) {
                return value;
            }
        }
        return super.get(name, start);
    }

    /**
     * Overridden to make the data attributes visible as own properties ('in' operator).
     * {@inheritDoc}
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        final HtmlElement e = (HtmlElement) getDomNodeOrNull();
        if (e != null && e.hasAttribute(DATA_PREFIX + deCamelize(name))) {
            return true;
        }
        return super.has(name, start);
    }

    /**
     * Overridden to enumerate the data attributes as own properties
     * (Object.keys(), Object.getOwnPropertyNames(), for...in, ...).
     * {@inheritDoc}
     */
    @Override
    protected Object[] getIds(final CompoundOperationMap<Scriptable> map,
            final boolean getNonEnumerable, final boolean getSymbols) {
        final Object[] ids = super.getIds(map, getNonEnumerable, getSymbols);

        final HtmlElement e = (HtmlElement) getDomNodeOrNull();
        if (e == null) {
            return ids;
        }

        final List<Object> result = new ArrayList<>();
        for (final DomAttr attr : e.getAttributesMap().values()) {
            final String name = camelize(attr.getName());
            if (name != null && !result.contains(name)) {
                result.add(name);
            }
        }
        for (final Object id : ids) {
            if (!result.contains(id)) {
                result.add(id);
            }
        }
        return result.toArray();
    }

    /**
     * Overridden to provide property descriptors for the data attributes.
     * {@inheritDoc}
     */
    @Override
    protected DescriptorInfo getOwnPropertyDescriptor(final Context cx, final Object id) {
        final DescriptorInfo descInfo = super.getOwnPropertyDescriptor(cx, id);
        if (descInfo != null) {
            return descInfo;
        }

        final HtmlElement e = (HtmlElement) getDomNodeOrNull();
        if (e != null && id instanceof CharSequence) {
            final String value = e.getAttribute(DATA_PREFIX + deCamelize(id.toString()));
            if (ATTRIBUTE_NOT_DEFINED != value) {
                return ScriptableObject.buildDataDescriptor(value, ScriptableObject.EMPTY);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        final HtmlElement e = (HtmlElement) getDomNodeOrNull();
        if (e == null) {
            super.put(name, start, value);
        }
        else {
            e.setAttribute("data-" + deCamelize(name), JavaScriptEngine.toString(value));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final String name) {
        final HtmlElement e = (HtmlElement) getDomNodeOrNull();
        if (e == null) {
            super.delete(name);
        }
        else {
            e.removeAttribute("data-" + deCamelize(name));
        }
    }

    /**
     * Transforms the specified attribute name into the name of the dataset property.
     *
     * @param attributeName the attribute name
     * @return the property name or {@code null} if the attribute is not a data attribute
     * @see <a href="https://html.spec.whatwg.org/multipage/dom.html#dom-domstringmap-interface">
     *      HTML Living Standard - The DOMStringMap interface</a>
     */
    private static String camelize(final String attributeName) {
        if (!attributeName.startsWith(DATA_PREFIX)) {
            return null;
        }
        final String name = attributeName.substring(DATA_PREFIX.length());
        final StringBuilder builder = new StringBuilder();
        boolean dash = false;
        for (int i = 0; i < name.length(); i++) {
            final char ch = name.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                return null;
            }
            if (dash && ch >= 'a' && ch <= 'z') {
                // a dash followed by a lower case letter is replaced by the upper case letter
                builder.setLength(builder.length() - 1);
                builder.append(Character.toUpperCase(ch));
                dash = false;
            }
            else {
                builder.append(ch);
                dash = ch == '-';
            }
        }
        return builder.toString();
    }

    /**
     * Transforms the specified string from camel-cased to dash separated.
     *
     * @param string the string to decamelize
     * @return the transformed string
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement/dataset#name_conversion">
     *      MDN - HTMLElement.dataset - Name conversion</a>
     */
    private static String deCamelize(final String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            final char ch = string.charAt(i);
            if (Character.isUpperCase(ch)) {
                builder.append('-').append(Character.toLowerCase(ch));
            }
            else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

}
